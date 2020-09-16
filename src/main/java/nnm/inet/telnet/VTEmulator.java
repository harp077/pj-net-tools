/*
 * Version 0.23 11/11/2001
 *
 * Visit my url for update: http://www.geocities.com/beapetrovicova/
 *
 * jTerm was developed by Bea Petrovicova <beapetrovicova@yahoo.com>.
 * The design and implementation of jTerm are available for royalty-free
 * adoption and use. This software is provided 'as is' without any
 * guarantees. Copyright is retained by Bea Petrovicova. Redistribution
 * of any part of jTerm or any derivative works must include this notice.
 *
 */
package nnm.inet.telnet;

/**
 * @version 0.23 06/09/2003
 * @author Bea Petrovicova <beapetrovicova@yahoo.com>
 */
import java.io.IOException;

/** VT220 class: a subclass of Emulator that implements
 * standard XTerm,VT220,VT100,and VT52 emulations. */
public class VTEmulator extends AEmulator
{
   //Serial Port support - JavaCOMM
   Serial serial =null;
   boolean printermode =false;

   public static final String charToString (char c)
   {  if (c < (char)0x20)
         return C0.toString(c);
      else if (c >= (char)0x7f && c <= (char)0x9f)
	 return C1.toString(c);
      else if (c == (char)0xff)
         { return  "$ff"; }
      else if (c >= (char)0x100)
         { return  "$" + Integer.toString((int)c,16); }
      return  String.valueOf(c);
   }

   public static final String sequenceToString (String seq)
   {  StringBuffer res = new StringBuffer(128);
      for (int i = 0; i < seq.length(); i++)
      {  if (i > 0)
            res.append(' ');
         res.append(charToString(seq.charAt(i)));
      }
      return  res.toString();
   }

   /* ------------------------------------------------------------
    CONSTANT VALUES SECTION
    ------------------------------------------------------------ */

   /* Basic types of characters:

    A "control character" is of the range 0x00-0x1f or 0x80-0x9f;
    the DEC control characters are defined below.
    An "intermediate character" is in the range 0x20-0x2f; they may
    occure multiple times before the last character of an escape
    sequence or C1.CSI command.
    A "parameter character" is in the range 0x30-0x39 or 0x3b; it
    appears as "23;7;45".
    A "flag character" is in the range 0x3c-0x3f; it sets a boolean
    flag if it appears anywhere.
    A "final character" is in the range 0x40-0x7e; it terminates an
    escape sequence or C1.CSI command. */

   /* State of character parser. */

   /* In the normal state,non-control characters is simply rendered to
    the screen. */

   /** An escape sequence has the following form:<BR>
    1. C0.ESC<BR>
    2. Zero or more intermediate characters<BR>
    3. Final character */
   private static final int STATE_ASCII = VTCommand.TYPE_NONE;
   /** A control sequence has the following form:<BR>
    1. C1.CSI (or C0.ESC-[)<BR>
    2. Zero or more parameter characters<BR>
    3. Zero or more intermediate characters<BR>
    4. Final character */
   private static final int STATE_ESC = C0.ESC;
   /** A control sequence has the following form:<BR>
    1. C1.CSI (or C0.ESC-[)<BR>
    2. Zero or more parameter characters<BR>
    3. Zero or more intermediate characters<BR>
    4. Final character */
   private static final int STATE_CSI = C1.CSI;
   /** A Device Control Sequence has the following form:<BR>
    1. C1.DCS (or C0.ESC-P)<BR>
    2. Zero or more parameter characters<BR>
    3. Zero or more intermediate characters<BR>
    4. Final character<BR>
    5. Data string<BR>
    6. C1.ST (or C0.ESC-\) */
   private static final int STATE_DCS = C1.DCS;
   /* An operating system command has the following form:
    1. C1.OSC (or C0.ESC-])
    2. Data string
    3. C1.ST (or C0.ESC-\) */
   private static final int STATE_OSC = C1.OSC;
   /* A privacy message has the following form:
    1. C1.PM (or C0.ESC-^)
    2. Data string
    3. C1.ST (or C0.ESC-\) */
   private static final int STATE_PM = C1.PM;
   /* An Application Program VTCommand has the following form:
    1. C1.APC (or C0.ESC-_)
    2. Data string
    3. C1.ST (or C0.ESC-\) */
   private static final int STATE_APC = C1.APC;

   /** The state when processing a command's final data string */
   private static final int STATE_DATA = STATE_APC + 1; // Ends with C1.ST.
   /** The first state in a VT52 direct cursor address; the next
    character received is the line,with ' ' being the top line. */
   private static final int STATE_DCA1 = STATE_APC + 2;
   /** The second state in a VT52 direct cursor address; the next
    character received is the column,with ' ' being the far left. */
   private static final int STATE_DCA2 = STATE_APC + 3;

   /* Operating modes.  We assign an ID to each mode we understand,
    then create arrays to help us map these IDs to particular command
    sequences and values. */

   /** Character codes that need to be directly handled by emulator. */
   private final char[] special_chars =
   {  C0.NUL,C0.ENQ,C0.BEL,C0.BS,C0.HT,C0.LF,C0.VT,C0.FF,C0.CR,C0.SO,C0.SI,C0.DC1,C0.DC3,C0.CAN,C0.SUB,C0.ESC,
      C1.DEL,C1.IND,C1.NEL,C1.HTS,C1.RI,C1.SS2,C1.SS3,C1.DCS,C1.CSI,C1.ST,C1.OSC,C1.PM,C1.APC };

   /* Types of emulations available. */

   private static final int EMU_XTERM = 0;
   private static final int EMU_VT220 = 1;
   private static final int EMU_VT100 = 2;
   private static final int EMU_VT52 = 3;

   /* Instance data section. */

   /** The current parsing state. */
   private int state = STATE_ASCII;
   /** Current command being parsed. */
   private VTCommand cmd = new VTCommand();
   /** Sub-command being executed (e.g.,C1.IND within C1.CSI).
    We need this because we transform 8-bit codes to their
    C0.ESC-Fe counterpart,and call doESC to execute them. */
   private VTCommand sub_cmd = new VTCommand();

   /* 'true' for characters we need to directly handle. */
   private boolean[] char_flags = new boolean[256];

   /** Current emulation type. */
   private int emulation = EMU_VT220;
   /** The line value processed in a VT52 direct cursor address. */
   private int dca_line = 0;

   /* NOTE: We also need to save the rendering mode and some flags. */
   private int saved_row = 0;
   private int saved_col = 0;
   private int saved_style = ATerminal.Style.PLAIN;

   /* ------------------------------------------------------------
    CONSTRUCTOR AND EMULATOR METHODS WE OVERRIDE.
    ------------------------------------------------------------ */

   public VTEmulator(VTMapper map, ATranslator tran)
   {  super(map,tran);
      this.mode =new VTMode();
      map.mode =this.mode;
      // Flag the ASCII characters we need to directly deal with.
      for (int i = 0; i < char_flags.length; i++)
         char_flags[i] =false;
      for (int i = 0; i < special_chars.length; i++)
         char_flags[(int)special_chars[i]] =true;
      resetterm(false);
   }

   public int getCurType ()
   {
      switch (emulation)
      {
         case EMU_XTERM:
            return  0;
         case EMU_VT220:
            return  1;
         case EMU_VT100:
            return  3;
         case EMU_VT52:
            return  5;
      }
      return  -1;
   }

   public int getTypeNum ()
   {  return  emulation; }

   public String getTypeName (int num)
   {/* switch (num)
      {
         case 0:
            mode.states[VTMode.DECANM] = true;
            emulation = EMU_XTERM;
            return  "XTERM";
         case 1:
            mode.states[VTMode.DECANM] = true;
            emulation = EMU_VT220;
            return  "DEC-VT220";
         case 2:
            mode.states[VTMode.DECANM] = true;
            emulation = EMU_VT220;
            return  "VT220";
         case 3:
            mode.states[VTMode.DECANM] = true;
            emulation = EMU_VT100;
            return  "DEC-VT100";
         case 4:
            mode.states[VTMode.DECANM] = true;
            emulation = EMU_VT100;
            return  "VT100";
         case 5:
            mode.states[VTMode.DECANM] = false;
            emulation = EMU_VT52;
            return  "DEC-VT52";
         case 6:
            mode.states[VTMode.DECANM] = false;
            emulation = EMU_VT52;
            return  "VT52";
      } */
      return  "VT220";
   }

   public void receive (char ch)
   {  if(checkChar(ch))
      {  if(serial!=null && printermode)
         {  try { System.out.println("COM OUT a: "+C0.toString(ch));
                 serial.write(ch); serial.flush(); }
            catch (IOException i) { System.out.println(i); } }
	 else term.receive(ch);
      } else doChar(ch); }

   public boolean checkChar (char c)
   {  return (state == STATE_ASCII && !char_flags[(int)c]); }

   /** Main character parsing function: handles control characters and the
    main state machine; dispatches command sequences to approriate
    methods for executing them. */
   public boolean doChar (char c)
   {

      // Bypass XON/XOFF and ACK/NAK to serial.
      if(serial!=null && printermode) switch (c)
      {  case C0.STX: case C0.ETX: case C0.ETB:
         case C0.ACK: case C0.NAK:
         case C0.XON: case C0.XOFF:
         case C0.LF:  case C0.CR:
	 case C0.VT:  case C0.FF:
            try { System.out.println("COM OUT b: "+C0.toString(c));
	          serial.write(c); serial.flush(); }
            catch (IOException i) { System.out.println(i); }
	    return false; }

      // Handle an Xterm non-ANSIness in the DATA state --
      // the data is terminated by -any- non-printing character.
      if (emulation == EMU_XTERM && state == STATE_DATA)
         if (c <= (char)0x1f || (c >= (char)0x80 && c <= (char)0x9f)) c = C1.ST;

      // First,process control characters.
      switch (c)
      {
	 case C0.NUL:
         // These first two cancel the current command.
         case C0.CAN:
            reset_state();
            return  false;
         case C0.SUB:
            reset_state();
            if (term != null)
            {  term.setStyle(term.getStyle() ^ ATerminal.Style.INVERSE);
               term.receive('?',0);
               term.setStyle(term.getStyle() ^ ATerminal.Style.INVERSE); }
            return  false;
            // C0.ENQ immediately sends an C0.ACK.
         case C0.ENQ:
            if (telnet != null)
               send(answerback); send(C0.ACK);
            return  false;
         case C0.BEL:
            // Should make some sound ...
	    return  false;
            // C0.BS moves cursor to the left.
         case C0.BS:
	    term.BS(); /*
            if (term != null)
            {
               int col = term.getCursorCol();
               if (col > 0)
                  col--;
               term.setCursorPos(term.getCursorRow(),col);
            } */
            // An Xterm non-ANSIness.
            if (emulation == EMU_XTERM)
               reset_state();
            return  false;
            // C0.HT moves to next horizontal tab stop.
            // XXX tabs aren't fully implemented!
         case C0.HT:
	    term.HT(); /*
            if (term != null)
            {
               int col = term.getCursorCol() + 1;
               int last = term.getAreaRight();
               while (col < tabs.length && col <= last && !tabs[col])
                  col++;
               if (col > last)
                  col = last;
               term.setCursorPos(term.getCursorRow(),col);
            } */
            // An Xterm non-ANSIness.
            if (emulation == EMU_XTERM)
               reset_state();
            return  false;
            // C0.LF,C0.VT,and C0.FF all move to next line.
         case C0.CR:
	    term.CR();
            return  false;
         case C0.LF:case C0.VT:case C0.FF:
	    term.LF(); /*
            if (term != null)
               term.receive('\n',0); */
            // An Xterm non-ANSIness.
            if (emulation == EMU_XTERM)
               reset_state();
            return  false;
            // Character sets are not currently implented.  Awww...
	 case C0.SI: // LS0
	       tran.invoke('L','(');
            // An Xterm non-ANSIness.
            if (emulation == EMU_XTERM)
               reset_state();
            return  false;
         case C0.SO: // LS1
	       tran.invoke('L',')');
            // An Xterm non-ANSIness.
            if (emulation == EMU_XTERM)
               reset_state();
            return  false;
            // C0.DC1 and C0.DC3 are ignored.
         case C0.DC1:case C0.DC3:
            return  false;
            // Escape cancels the current command,and starts a new one.
            // However,be aware that C0.ESC-\ is C1.ST,the command data terminator!
            // We thus can't just initialize our state right now...
         case C0.ESC:
            state = STATE_ESC;
            return  false;
            // Transform 8-bit escapes into their respective escape sequence.
         default:
            if (c >= (char)0x80 && c <= (char)0x9f)
            {  sub_cmd.init();
               sub_cmd.type = STATE_ESC;
               sub_cmd.term = (char)(c - ((char)0x80 - '@'));
               doESC(sub_cmd);
               return  false; }
      }
      switch (state)
      {
         /* Process normally -- display the characters. */
         case STATE_ASCII:
            return  true;
            /* Process a basic escape sequence -- intermediate
             characters followed by a term character. */

         case STATE_ESC:
            if (c == '\\')
            {  sub_cmd.init();
               sub_cmd.type = STATE_ESC;
               sub_cmd.term = '\\';
               doESC(sub_cmd);
               return  false; }
            else if (mode.states[VTMode.DECANM] && c >= (char)0x20 && c <= (char)0x2f)
            {  cmd.parseInterChar(c);
               return  false; }
            else if ((c >= (char)0x20 && c <= (char)0x7e) || c >= (char)0xa0)
            {  cmd.parseTermChar(c);
               cmd.type = C0.ESC;
               doESC(cmd);
               if (state == STATE_ESC)
               {  reset_state(); }
               return  false; }
            return  true;
            /* Process control sequence -- parameters followed by
             intermediate characters followed by term character. */

         case STATE_CSI:
            if (c >= (char)0x30 && c <= (char)0x3f)
            {  cmd.parseParamChar(c);
               return  false; }
            else if (c >= (char)0x20 && c <= (char)0x2f)
            {  cmd.parseInterChar(c);
               return  false; }
            else if ((c >= (char)0x20 && c <= (char)0x7e) ||
                  c >= (char)0xa0)
            {  // We let all printable characters terminate the control sequence,
               // to handle some non-ANSI sequences.
               cmd.parseTermChar(c);
               doCSI(cmd);
               reset_state();
               return  false; }
            return  true;
             /* Process other sequence -- parameters followed by
             intermediate characters followed by term character,
             then go into data processing state. */

         case STATE_DCS:case STATE_OSC:case STATE_PM:case STATE_APC:
            // Another Xterm non-ANSIness.  *sigh*
            if (emulation == EMU_XTERM)
            {
	       if (c < '0' || c > '9')
               {  cmd.parseTermChar(c);
                  state = STATE_DATA;
                  return  false; }
            }
            if (c >= (char)0x30 && c <= (char)0x3f)
            {  cmd.parseParamChar(c);
               return  false; }
            else if (c >= (char)0x20 && c <= (char)0x2f)
            {  cmd.parseInterChar(c);
               return  false; }
            else if ((c >= (char)0x20 && c <= (char)0x7e) || c >= (char)0xa0)
            {  cmd.parseTermChar(c);
               state = STATE_DATA;
               return  false; }
            return  true;
            /* Collect command data.  C1.ST will be intercepted above,
            to cause the final command to actually be executed. */

         case STATE_DATA:
            if ((c >= (char)0x20 && c <= (char)0x7e) || c >= (char)0xa0)
            {  cmd.parseDataChar(c);
               return  false; }
            return  true;
            /* Process a VT52 direct-cursor address command. */

         case STATE_DCA1:
            dca_line = (int)(c - ' ');
            state = STATE_DCA2;
            return  false;
         case STATE_DCA2:
            if (term != null)
            {  term.setCursorPos(dca_line,(int)(c - ' ')); }
            reset_state();
            return  false;
         default:
            state = STATE_ASCII;
            System.out.println("jTerm: *** Unknown VT100 emulator state!");
            return  false;
      }
   }

   /* ------------------------------------------------------------
    INITIALIZATION AND GLOBAL CONTROL.
    ------------------------------------------------------------ */
   /* Set modes to initial states. */
   void init_modes ()
   {  for (int i = 0; i < VTMode.N; i++)
         mode.states[i] = mode.saves[i] = VTMode.inits[i]; }

   /** Set tabs to defaults : every eight characters. */
   void init_tabs ()
   {  for (int i = 0; i < tabs.length; i += 1)
         tabs[i] = (i%8) == 0; }

   /** Reset the state machine back to displaying ASCII characters. */
   void reset_state ()
   {  if (state != STATE_ASCII)
         cmd.init();
      state = STATE_ASCII; }

   /** Reflect the emulation mode back into the term's settings. */
   void update_term_mode ()
   {  /*
      if (term != null)
      {  int mode = 0;
         if (mode.states[VTMode.DECSCLM])
            mode |= term.VTMode.SMOOTH;
         if (!mode.states[VTMode.DECAWM])
            mode |= term.mode.NOWRAP;
         if (mode.states[VTMode.DECSCNM])
            mode |= term.mode.INVERSE;
         if (mode.states[VTMode.LNM])
            mode |= term.mode.NEWLINE;
         term.setMode(mode);
         term.setCursorVisible(mode.states[VTMode.DECTCEM]);
      } */
   }

   /** Do a reset of the entire term.
    If "hard" is true,this is a full hard reset. */
   public void resetterm (boolean hard)
   {  init_modes();
      init_tabs();
      reset_state();
      if (term != null)
      {  //term.reset(hard);
         update_term_mode(); }
   }

   /** Parse C0.ESC. */
   public void doESC (VTCommand mycmd)
   {
      if (mycmd.cur_inter <= 0)
      {
         switch (mycmd.term)
         {
            // VT52 cursor up sequence
            case 'A':
               if (term != null)
               {
                  int row = term.getCursorRow() - 1;
                  if (row < 0)
                     row = 0;
                  term.setCursorPos(row,term.getCursorCol());
               }
               return;
               // VT52 cursor down sequence
            case 'B':
               if (term != null)
               {
                  int row = term.getCursorRow() + 1;
                  if (row >= term.getNumRows())
                     row = term.getNumRows() - 1;
                  term.setCursorPos(row,term.getCursorCol());
               }
               return;
               // VT52 cursor right sequence
            case 'C':
               if (term != null)
               {
                  int col = term.getCursorCol() + 1;
                  if (col >= term.getNumCols())
                     col = term.getNumCols() - 1;
                  term.setCursorPos(term.getCursorRow(),col);
               }
               return;
               // C1.IND moves cursor down,possibly scrolling.
               // In VT52 mode,this is cursor left.
            case 'D':
               if (mode.states[VTMode.DECANM])
               {  if (term != null)
                     term.receive('\n',0); }
               else
               {
                  if (term != null)
                  {
                     int col = term.getCursorCol() - 1;
                     if (col < 0)
                        col = 0;
                     term.setCursorPos(term.getCursorRow(),col);
                  }
               }
               return;
               // C1.NEL moves cursor down and to column zero,possibly scrolling.
            case 'E':
               if (term != null)
               {
                  int row = term.getCursorRow();
                  if (row < term.getNumRows())
                     row++;
                  else
                     term.rollScreen(-1);
                  term.setCursorPos(row,0);
               }
               return;
               // C1.HTS sets a tab stop.
               // In VT52 mode,this is cursor home.
            case 'H':
               if (mode.states[VTMode.DECANM])
               {
                  if (term != null)
                  {  int col = term.getCursorCol();
                     if (col < tabs.length)
                        tabs[col] = true; }
               }
               else
               {  if (term != null)
                     term.setCursorPos(0,0); }
               return;
               // C1.HTJ.  Not a VT220 code.
               // In VT52 mode,this is reverse line feed.
            case 'I':
               if (mode.states[VTMode.DECANM])
               { }
                  else
               {
                  if (term != null)
                  {
                     int row = term.getCursorRow();
                     if (row > 0)
                        row--;
                     else
                        term.rollScreen(1);
                     term.setCursorPos(row,term.getCursorCol());
                  }
               }
               return;
               // VT52 Erase to end of screen sequence
            case 'J':
               if (term != null)
                  term.clearEOD(' ',term.getStyle());
               return;
               // C1.PLD and C1.PLU.  Not a VT220 code.
               // In VT52 mode,this is erase to end of line.
            case 'K':
               if (mode.states[VTMode.DECANM])
                  ;
	       else
               {  if (term != null)
                     term.clearEOL(' ',term.getStyle()); }
               return;
            case 'L':
               return;
               // C1.RI moves cursor up,possible scrolling
            case 'M':
               if (term != null)
               {
                  int row = term.getCursorRow();
                  if (row > 0)
                     row--;
                  else
                     term.rollScreen(1);
                  term.setCursorPos(row,term.getCursorCol());
               }
               return;
               // C1.SS2 and C1.SS3.  XXX Character sets are not implemented!
            case 'N':
               return;
            case 'O':
               return;
               // C1.DCS cancels the current command and starts a new
               // device control string.
            case 'P':
               reset_state();
               cmd.type = state = STATE_DCS;
               return;
               // VT52 print cursor line: a do-nothing for this emulation.
            case 'V':
               return;
               // VT52 printer controller modes: a do-nothing for this emulation.
            case 'W':
               return;
            case 'X':
               return;
               // VT52 direct cursor address sequence.
            case 'Y':
               state = STATE_DCA1;
               return;
               // VT52 identify sequence. XXX Not implemented!
            case 'Z':
               sendDA(DA_VT52);
               return;
               // VT52 enter ANSI sequence.
            case '<':
               doMode(VTMode.DECANM,VTMode.SET);
               return;
               // VT52 enter autoprint mode: a do-nothing for this emulation.
            case ' ':
               return;
               // RIS resets the term.
            case 'c':
               reset_state();
               resetterm(true);
               return;
               // C1.CSI cancels the current command and starts a new
               // control sequence.
            case '[':
               reset_state();
               cmd.type = state = STATE_CSI;
               return;
               // C1.OSC cancels the current command and starts a new
               // operating system command.
               // In VT52 mode,this is print screen: a do-nothing for this emulation.
            case ']':
               if (mode.states[VTMode.DECANM])
               {  reset_state();
                  cmd.type = state = STATE_OSC; }
               return;
               // C1.PM cancels the current command and starts a new
               // privacy message.
            case '^':
               reset_state();
               cmd.type = state = STATE_PM;
               return;
               // C1.APC cancels the current command and starts a new
               // application program command.
               // In VT52 mode,this is the exit autoprint sequence.
            case '_':
               if (mode.states[VTMode.DECANM])
               {  reset_state();
                  cmd.type = state = STATE_APC; }
               return;
               // C1.ST terminates the collection of a command's data,
               // and executes the final command.
            case '\\':
               if (state == STATE_DATA)
               {
                  switch (cmd.type)
                  {
                     case STATE_DCS:
                        doDCS(cmd);
                        reset_state();
                        return;
                     case STATE_OSC:
                        doOSC(cmd);
                        reset_state();
                        return;
                     case STATE_PM:
                        doPM(cmd);
                        reset_state();
                        return;
                     case STATE_APC:
                        doAPC(cmd);
                        reset_state();
                        return;
                     default:
                        System.out.println("jTerm: Unknown state at string terminator.");
                        reset_state();
                        return;
                  }
               }
               System.out.println("jTerm: String terminator outside of string.");
               return;
               // The rest are non-ANSI escape sequences.
            case '>':           // Normal keypad mode
               doMode(VTMode.DECKPAM,VTMode.RESET);
               return;
            case '=':           // Application keypad mode
               doMode(VTMode.DECKPAM,VTMode.SET);
               return;
            case '8':           // Restore cursor position
               if (term != null)
               {  term.setCursorPos(saved_row,saved_col);
                  term.setStyle(saved_style); }
               return;
            case '7':           // Save cursor position
               if (term != null)
               {  saved_row = term.getCursorRow();
                  saved_col = term.getCursorCol();
                  saved_style = term.getStyle(); }
               return;
            case '(':           // VT52 Character Set

	       return;
         }
      }
      if (mycmd.cur_inter == 1 &&
	  (mycmd.inters[0]=='(' || mycmd.inters[0]==')' ||
	   mycmd.inters[0]=='*' || mycmd.inters[0]=='+') )
      {  switch (mycmd.term)
         {  case 'A': case 'B': case '0':
	       tran.designate(mycmd.inters[0],mycmd.term);
	       tran.invoke('L',mycmd.inters[0]);
               return;
	 }
      }
      // Commands with "#" intermediate character.
      if (mycmd.cur_inter == 1 && mycmd.inters[0] == '#')
      {  switch (mycmd.term)
         {  case '3': return;
            case '4': return;
            case '5': return;
            case '6': return; }
      }

      mycmd.setType(C0.ESC);
      System.out.println("jTerm: Unknown C0.ESC: " + mycmd.toString());
      System.out.println("jTerm: term: " + mycmd.term);
      System.out.println("jTerm: inters: " + new String(mycmd.inters,0,mycmd.cur_inter));
   }

   /** Parse C1.CSI. */
   public void doCSI (VTCommand mycmd)
   {
      int mop = -1;
      switch (mycmd.term)
      {
         case 'h':
            mop = VTMode.SET;
            break;
         case 'l':
            mop = VTMode.RESET;
            break;
         case 's':
            if (mycmd.flags == VTCommand.FLAG_QUES_MASK)
               mop = VTMode.SAVE;
            break;
         case 'r':
            if (mycmd.flags == VTCommand.FLAG_QUES_MASK)
               mop = VTMode.RESTORE;
            break;
      }
      if (mop >= 0)
      {
         int flags = mycmd.flags;
         // Cycle through each parameter
         for (int i = 0; i < mycmd.cur_param; i++)
         {
            int param = mycmd.params[i];
            // Look for a mode matching the parameter.
            int j = 0;
            while (j < VTMode.N)
            {
               if (VTMode.flags[j] == flags && VTMode.values[j] == param)
               {  doMode(j,mop);
                  break; }
               j++;
            }
            if (j >= VTMode.N)
               System.out.println("jTerm: Unknown Mode: " +
                     param + "(Flags=" + flags + ",Op=" + mop + ")");
         }
         return;
      }
      // Standard commands.
      if (mycmd.flags == 0 && mycmd.cur_inter <= 0)
      {
         int cnt = mycmd.cur_param;
         int[] args = mycmd.params;
         boolean[] given = mycmd.has_params;
         switch (mycmd.term)
         {
            case '@':           // ICH Insert blanks
               if (term != null)
               {
                  int num = args[0];
                  if (!given[0])
                     num = 1;
                  term.insChars(term.getCursorCol(),
                        term.getCursorCol() + num,' ', ATerminal.Style.PLAIN);
               }
               break;
            case 'A':           // CUU: Cursor up
               if (term != null)
               {
                  int num = args[0];
                  int row;
                  if (!given[0])
                     num = 1;
                  row = term.getCursorRow() - num;
                  term.setCursorPos(row,term.getCursorCol());
               }
               break;
            case 'B':           // CUD: Cursor down
               if (term != null)
               {
                  int num = args[0];
                  int row;
                  if (!given[0])
                     num = 1;
                  row = term.getCursorRow() + num;
                  term.setCursorPos(row,term.getCursorCol());
               }
               break;
            case 'C':           // CUF: Cursor right
               if (term != null)
               {
                  int num = args[0];
                  int col;
                  if (!given[0])
                     num = 1;
                  col = term.getCursorCol() + num;
                  term.setCursorPos(term.getCursorRow(),
                        col);
               }
               break;
            case 'D':           // CUB: Cursor left
               if (term != null)
               {
                  int num = args[0];
                  int col;
                  if (!given[0])
                     num = 1;
                  col = term.getCursorCol() - num;
                  term.setCursorPos(term.getCursorRow(),
                        col);
               }
               break;
            case 'E':           // CNL: Cursor to next line
               if (term != null)
               {
                  // XXX Don't know the correct implementation...
                  int num = args[0];
                  int row,last;
                  if (!given[0])
                     num = 1;
                  row = term.getCursorRow() + num;
                  last = term.getAreaBottom();
                  if (row > last)
                  {  term.rollScreen(last - row);
                     row = last; }
                  term.setCursorPos(row,0);
               }
               break;
            case 'F':           // CPL: Cursor to previous line
               if (term != null)
               {
                  // XXX Don't know the correct implementation...
                  int num = args[0];
                  int row,first;
                  if (!given[0])
                     num = 1;
                  row = term.getCursorRow() - num;
                  first = term.getAreaTop();
                  if (row < first)
                  {  term.rollScreen(first - row);
                     row = first; }
                  term.setCursorPos(row,0);
               }
               break;
            case 'H':           // CUP or HVP: Set cursor pos
            case 'f':
               if (term != null)
               {
                  int row = term.getCursorRow();
                  int col = term.getCursorCol();
                  // Yeach,this is not written well!
                  if (cnt == 0)
                  {
                     if (mode.states[VTMode.DECOM])
                     {  row = term.getAreaTop();
                        col = term.getAreaLeft(); }
                     else
                        row = col = 0;
                  }
                  else
                  {
                     if (given[0] && cnt >= 1)
                     {  row = args[0] - 1;
                        if (mode.states[VTMode.DECOM])
                           row += term.getAreaTop(); }
                     if (given[1] && cnt >= 2)
                     {  col = args[1] - 1;
                        if (mode.states[VTMode.DECOM])
                           col += term.getAreaLeft(); }
                  }
                  if (mode.states[VTMode.DECOM])
                  {
                     if (row > term.getAreaBottom())
                        row = term.getAreaBottom();
                     if (col > term.getAreaRight())
                        col = term.getAreaRight();
                  }
                  term.setCursorPos(row,col);
               }
               break;
            case 'I':           // CHT: Move to tabstop N
               // XXX this may not be completely right...
               if (term != null)
               {
                  int i = 0;
                  int right = term.getAreaRight();
                  if (right >= tabs.length)
                     right = tabs.length - 1;
                  for (int num = 0; i <= right && num <= mycmd.cur_param; i++)
                     if (tabs[i])
                        num++;
                  term.setCursorPos(term.getCursorRow(),i);
               }
               break;
            case 'J':           // ED: Erase in display
               if (term != null)
               {
                  if (!given[0] || args[0] == 0)
                     term.clearEOD(' ',term.getStyle());
                  else if (args[0] == 1)
                     term.clearBOD(' ',term.getStyle());
                  else if (args[0] == 2)
                     term.rollScreen(-(term.getAreaBottom()
                           - term.getAreaTop() + 1));
               }
               break;
            case 'K':           // EL: Erase in line
               if (term != null)
               {
                  if (!given[0] || args[0] == 0)
                     term.clearEOL(' ',term.getStyle());
                  else if (args[0] == 1)
                     term.clearBOL(' ',term.getStyle());
                  else if (args[0] == 2)
                     term.clearLine(' ',term.getStyle());
               }
               break;
            case 'L':           // IL: Insert lines at cursor
               if (term != null)
               {
                  int num = args[0];
                  if (!given[0])
                     num = 1;
                  int row = term.getCursorRow();
                  if (row < 0)
                     row = 0;
                  term.rollRegion(row,term.getAreaBottom(),num);
               }
               break;
            case 'M':           // DL: Delete lines at cursor
               if (term != null)
               {
                  int num = args[0];
                  if (!given[0])
                     num = 1;
                  term.rollRegion(term.getCursorRow(),
                        term.getAreaBottom(),-num);
               }
               break;
            case 'S':           // Pan Down
	       term.rollScreen(mycmd.params[0]);
               break;
            case 'T':           // Pan Up
	       System.out.println("C1.CSI x T -  Pan Up not implemented");
               break;
            case 'P':           // DCH: Delete character
               if (term != null)
               {
                  int num = args[0];
                  if (!given[0])
                     num = 0;
                  term.delChars(term.getCursorCol(),
                        term.getCursorCol(),' ',ATerminal.Style.PLAIN);
               }
               break;
            case 'X':           // ECH: Erase character
               if (term != null)
               {
                  int num = args[0];
                  if (!given[0])
                     num = 0;
                  term.setChars(term.getCursorCol(),
                        term.getCursorCol() + num,' ',ATerminal.Style.PLAIN);
               }
               break;
            case 'c':           // DA: Device attr report
               if (cnt <= 0 || (cnt == 1 && args[0] == 0))
               {
                  sendDA(DA_PRIMARY);
               }
               else
               {
                  System.out.println("jTerm: Unknown DA: "+ mycmd.toString());
               }
               break;
            case 'g':
               if (term != null)
               {
                  if (mycmd.cur_param <= 0 || mycmd.params[0] == 0)
		  {  int col = term.getCursorCol();
		        tabs[col] = false; }
                     //for (int i = 0; i < tabs.length; i++)
                        //tabs[i] = false;
                  else if (mycmd.params[0] == 3)
                  {
                     int col; //term.getCursorCol();
                     for(col=0; col <tabs.length; col++)
                        tabs[col] = false;
                  }
               }
               break;
            case 'i':
	       if (mycmd.cur_param >= 0)
	       {  if (mycmd.params[0] == 7)
		     if(serial==null)
		  {  serial = new Serial();
                     System.out.println("Allow auxiliary input mode!");
		     System.out.println("open: "+serial.open(commport,baudrate,flowcontrol));
	             serial.addEventListener(new SerialEventListener()
		     {  Serial serial = VTEmulator.this.serial;
			public void serialEvent()
	                {  /* switch(event.getEventType())
		           {  case SerialPortEvent.DATA_AVAILABLE: */
			         try
			         {  System.out.println("COM IN available: "+serial.available());
				    while(serial.available() > 0)
				    {  byte b[] = new byte[serial.available()];
				       int n = serial.read(b);
				       if(n>0) telnet.send(b,0,n); } }
		                 catch (IOException i) { System.out.println(i); }
                            /*  break; } */
			}
		      } );
	             serial.notifyOnDataAvailable(true);

		     /* {  public void run()
			{  while(!isEnd)
			   {  try
			      {  //System.out.println("available(): "+available());
	                         if(available() > 0)
				 {
				    byte b[] = new byte[available()];
				    int n = read(b);
				    if(n>0)
				    {  telnet.send(b,0,n); *telnet.flush(); * }
				    //System.out.print(sequenceToString(C0.toString((char)ch));
				 } //synchronized(this) { wait(100); }
				 //System.out.println("trying read...");
				 //this.serial.sendBreak(25);
				 //this.serial.setRTS(true);
                                 int x = read();
				 if(x>=0)
				    telnet.send((char)x);
			      }
		              //catch (InterruptedException i) { System.out.println(i); }
		              catch (IOException i) {  System.out.println(i); }
			   }
		        }
		     }; */

                     //new Thread(serial).start();
		     reset_state();
		  }
                  if (mycmd.params[0] == 6)
		  {  System.out.println("\nEnd auxiliary input mode!");
		     if(serial!=null)
		     {  serial.close();
		        serial=null; }
		     reset_state();
		  }
                  if (mycmd.params[0] == 5)
		  {  printermode = true; reset_state(); }
                  if (mycmd.params[0] == 4)
		  {  printermode = false; reset_state(); }
               }
               break;
            case 'm':
               if (cnt <= 0)
               {  term.setStyle(ATerminal.Style.PLAIN);
                  term.setForeColor(0);
                  term.setBackColor(0); }
               else
               {
                  for (int i = 0; i < cnt; i++)
                  {
                     switch (args[i])
                     {
                        case 0:
                           term.setStyle(ATerminal.Style.PLAIN);
                           term.setForeColor(0);
                           term.setBackColor(0);
                           break;
                        case 1:
                           term.setStyle(term.getStyle() | ATerminal.Style.BOLD);
                           break;
                        case 4:
                           term.setStyle(term.getStyle() | ATerminal.Style.UNDERLINE);
                           break;
                        case 5:
                           term.setStyle(term.getStyle() | ATerminal.Style.ITALIC);
                           break;
                        case 7:
                           term.setStyle(term.getStyle() | ATerminal.Style.INVERSE);
                           break;
                           /* VT220 control code */

                        case 22:
                           term.setStyle(term.getStyle() & ~ATerminal.Style.BOLD);
                           break;
                        case 24:
                           term.setStyle(term.getStyle() & ~ATerminal.Style.UNDERLINE);
                           break;
                        case 25:
                           term.setStyle(term.getStyle() & ~ATerminal.Style.ITALIC);
                           break;
                        case 27:
                           term.setStyle(term.getStyle() & ~ATerminal.Style.INVERSE);
                           break;
                        default:
                           if (args[i] >= 30 && args[i] <= 39)
                              term.setForeColor(args[i] - 30 + 1);
                           else if (args[i] >= 40 && args[i] <= 49)
                              term.setBackColor(args[i] - 40 + 1);
                           break;
                     }
                  }
               }
               break;
            case 'n':           // Device status report
               doDSR(mycmd);
               break;
            case 'q':           // DECLL: Load LEDs
               break;
            case 'r':           // DECSTBM: Set scroll region
               if (term != null)
               {
                  int top = 0;
                  int bottom = term.getNumRows() - 1;
                  if (given[0] && cnt >= 1)
                     top = args[0] - 1;
                  if (given[1] && cnt >= 2)
                     bottom = args[1] - 1;
                  term.setArea(top,bottom);
                  if (mode.states[VTMode.DECOM])
                     term.setCursorPos(term.getAreaTop(),term.getAreaLeft());
                  else term.setCursorPos(0,0);
               }
               break;
            case 'x':           // DECREQTPARM: Request term params
               if (telnet != null)
               {  VTCommand scmd = new VTCommand();
                  scmd.setType(C1.CSI);
                  scmd.addParam(2,0);          // Only reporting on request
                  scmd.addParam(1,0);          // No parity set
                  scmd.addParam(1,0);          // 8 bits per character
                  scmd.addParam(120,0);        // 19200 bps transmit speed
                  scmd.addParam(120,0);        // 19200 bps receive speed
                  scmd.addParam(1,0);          // Bit rate multiplier is 16
                  scmd.addParam(0,0);          // Switch flags.   XXX not implemented.
                  scmd.setTerminator('x');
                  send(scmd.toSequence()); }
               break;
            case 'y':           // DECTST: Invoke confidence test
               if (mycmd.cur_param == 2 && mycmd.params[0] == 2 && mycmd.params[1] == 0)
               {  // This one does a reset.
                  reset_state();
                  resetterm(true); }
               break;
            default:
               System.out.println("jTerm: Unknown C1.CSI: "+mycmd.toString());
               break;
         }
         return;
      }
      // VTCommand with intermediate sequence '' and '?' flag
      if (mycmd.flags == VTCommand.FLAG_QUES_MASK && mycmd.cur_inter <= 0)
      {
         switch (mycmd.term)
         {
            // DSR - device status report.
            case 'n':
               doDSR(cmd);
               return;
         }
      }
      // VTCommand with intermediate sequence '' and '>' flag
      if (mycmd.flags == VTCommand.FLAG_GT_MASK && mycmd.cur_inter <= 0)
      {
         switch (mycmd.term)
         {
            // DA - secondary device attribute report.
            case 'c':
               if (mycmd.cur_param <= 0 || (mycmd.cur_param == 1 && mycmd.params[0] == 0))
                  sendDA(DA_SECONDARY);
               else
                  System.out.println("jTerm: Unknown DA: " + mycmd.toString());
               return;
         }
      }
      // VTCommand with intermediate sequence '!' and no flags
      if (mycmd.flags == 0 && mycmd.cur_inter == 1 && mycmd.inters[0] == '!')
      {
         switch (mycmd.term)
         {
            // DECSTR does a soft-reset of the term
            case 'p':
               reset_state();
               resetterm(false);
               return;
         }
      }
      // VTCommand with intermediate sequence ''' and no flags
      if (mycmd.flags == 0 && mycmd.cur_inter == 1 && mycmd.inters[0]
            == '\'')
      {
         switch (mycmd.term)
         {
            /* These aren't implemented yet...
             // DECELR - enable locator reporting.
            case 'z':
               mode.states[VTMode.DECELR] = false;
               mode.states[VTMode.DECOLR] = false;
               mode.states[VTMode.DECPLR] = false;
               if( mycmd.cur_param > 0 && mycmd.params[0] == 1 )
               {  mode.states[VTMode.DECELR] = true;
               } else if( mycmd.cur_param > 0 && mycmd.params[0] == 2 )
               {  mode.states[VTMode.DECELR] = true;
                  mode.states[VTMode.DECOLR] = true; }
               if( mycmd.cur_param > 1 && mycmd.params[1] == 1 )
               {  mode.states[VTMode.DECPLR] = true; }
               return;
               // DECRLP - request current locator position.
            case '|':
               if( mycmd.cur_param <= 0 || mycmd.params[0] <= 1 )
               {  VTCommand scmd = new VTCommand();
                  build_lr(scmd,null);
                  send(scmd.toSequence());
                  return;
               } */
         }
      }
      System.out.println("jTerm: Unknown C1.CSI: " + mycmd.toString());
   }

   /** Parse DSR. */
   public boolean doDSR (VTCommand mycmd)
   {
      VTCommand scmd = new VTCommand();
      if (mycmd.cur_param >= 1 && mycmd.cur_inter <= 0)
      {
         if (mycmd.flags == 0 && mycmd.params[0] == 6)
         {
            scmd.setType(C1.CSI);
            int row = 1,col = 1;
            if (term != null)
            {
               row = term.getCursorRow() + 1;
               col = term.getCursorCol() + 1;
               if (mode.states[VTMode.DECOM])
               {  row -= term.getAreaTop();
                  col -= term.getAreaLeft(); }
            }
            if (row < 1)
               row = 1;
            if (col < 1)
               col = 1;
            scmd.addParam(row,0);
            scmd.addParam(col,0);
            scmd.setTerminator('R');
         }
         else if (mycmd.flags == 0 && mycmd.params[0] == 5)
         {  scmd.setType(C1.CSI);
            scmd.addParam(0,0);
            scmd.setTerminator('n'); }
         else if (mycmd.flags == VTCommand.FLAG_QUES_MASK && mycmd.params[0] == 15)
         {  scmd.setType(C1.CSI);
            scmd.addParam(13,VTCommand.FLAG_QUES_MASK);          // No printer.
            scmd.setTerminator('n');
         }
         else if (mycmd.flags == VTCommand.FLAG_QUES_MASK && mycmd.params[0] == 25)
         {  scmd.setType(C1.CSI);
            scmd.addParam(21,VTCommand.FLAG_QUES_MASK);          // Locked
            scmd.setTerminator('n'); }
         else
            System.out.println("jTerm: Unknown DSR: " + mycmd.toString());
      }
      else
         System.out.println("jTerm: Unknown DSR: " + mycmd.toString());
      if (scmd.type != VTCommand.TYPE_NONE && telnet != null)
      {  send(scmd.toSequence());
         return  true; }
      return  false;
   }

   /** Parse C1.DCS. */
   public void doDCS (VTCommand mycmd)
   {
      // Standard commands.
      if (mycmd.flags == 0 && mycmd.cur_inter <= 0)
      {
         switch (mycmd.term)
         {
            default:
               System.out.println("jTerm: Unknown C1.DCS: "+ mycmd.toString());
               break;
         }
         return;
      }
      System.out.println("jTerm: Unknown C1.DCS: " + mycmd.toString());
   }

   /** Parse C1.OSC. */
   public void doOSC (VTCommand mycmd)
   {
      // Standard commands.
      if (mycmd.flags == 0 && mycmd.cur_inter <= 0)
      {
         switch (mycmd.term)
         {
            default:
               System.out.println("jTerm: Unknown C1.OSC: "+ mycmd.toString());
               break;
         }
         return;
      }
      System.out.println("jTerm: Unknown C1.OSC: " + mycmd.toString());
   }

   /** Parse C1.PM. */
   public void doPM (VTCommand mycmd)
   {
      // Standard commands.
      if (mycmd.flags == 0 && mycmd.cur_inter <= 0)
      {
         switch (mycmd.term)
         {
            default:
               System.out.println("jTerm: Unknown C1.PM: "+ mycmd.toString());
               break;
         }
         return;
      }
      System.out.println("jTerm: Unknown C1.PM: " + mycmd.toString());
   }

   /** Parse C1.APC. */
   public void doAPC (VTCommand mycmd)
   {
      // Standard commands.
      if (mycmd.flags == 0 && mycmd.cur_inter <= 0)
      {
         switch (mycmd.term)
         {
            default:
               System.out.println("jTerm: Unknown C1.APC: "+ mycmd.toString());
               break;
         }
         return;
      }
      System.out.println("jTerm: Unknown C1.APC: " + mycmd.toString());
   }

   /* ------------------------------------------------------------
    EMULATION SUPPORT.
    ------------------------------------------------------------ */

   /* Send a Device Attribute string,of the given type,to the remote host. */
   static final int DA_PRIMARY = 0;
   static final int DA_SECONDARY = 1;
   static final int DA_VT52 = 2;

   public boolean sendDA (int type)
   {  //EXAMPLE: CSI>1;10;0c = VT220 version 1.0, no options
      VTCommand scmd = new VTCommand();
      scmd.setType(C1.CSI);
      scmd.setTerminator('c');
      if (type == DA_PRIMARY || type == DA_VT52)
      {
         if (emulation == EMU_VT220)
         {
            scmd.addParam(62,VTCommand.FLAG_QUES_MASK); // C0.VT 220
            if (term != null && term.getNumCols() >= 132)
               scmd.addParam(1,VTCommand.FLAG_QUES_MASK); // 132 columns
            //scmd.addParam(2,VTCommand.FLAG_QUES_MASK); // printer port
            scmd.addParam(6,VTCommand.FLAG_QUES_MASK); // selective erase
            //scmd.addParam(7,VTCommand.FLAG_QUES_MASK); // DRCS
            //scmd.addParam(8,VTCommand.FLAG_QUES_MASK); // UDK
            //scmd.addParam(9,VTCommand.FLAG_QUES_MASK); // 7-bit nat. repl. chars
         }
         else if (emulation == EMU_VT52 && type == DA_VT52)
         {  scmd.setType(C0.ESC);
            scmd.addIntermediates("/");
            scmd.setTerminator('Z'); }
         else
         {
            scmd.addParam(6,VTCommand.FLAG_QUES_MASK); // C0.VT 102
            // Others:
            // C0.ESC [ ? 1; 2 c     -- C0.VT 100 with AVO
            // C0.ESC [ ? 1; 0 c     -- C0.VT 101
            //            ^--------- 1 = STP,     2 = AVO, 4 = GO
            //                       processor op,video op,graphics op
         }
      }
      else if (type == DA_SECONDARY)
      {
         scmd.addParam(1,VTCommand.FLAG_GT_MASK);  // C0.VT 220
         scmd.addParam(10,VTCommand.FLAG_GT_MASK); // Firmware version 1.0
         scmd.addParam(0,VTCommand.FLAG_GT_MASK);  // No options
      }
      if (scmd.type != VTCommand.TYPE_NONE && telnet != null)
      {  System.out.println("DA Escape: "+scmd.toSequence());
	 send(scmd.toSequence());
         return  true; }
      return  false;
   }

   /** Do the given operation 'op' on the mode state 'mode'. */
   public void doMode (int md,int op)
   {  System.out.println(""+VTMode.abbrvs[md]+"="+VTMode.names[md]+":"+op);
      switch (op)
      {
         case VTMode.RESET:
            mode.states[md] = false;
            break;
         case VTMode.SET:
            mode.states[md] = true;
            break;
         case VTMode.SAVE:
            mode.saves[md] = mode.states[md];
            return;
         case VTMode.RESTORE:
            mode.states[md] = mode.saves[md];
            break;
         default:
            System.out.println("jTerm: Unknown mode operation: " + op);
      }
      if (md == VTMode.DECOM)
      {
         if (term != null)
         {
            if (mode.states[VTMode.DECOM])
               term.setCursorPos(term.getAreaTop(),
                     term.getAreaLeft());
            else
               term.setCursorPos(0,0);
         }
      }
      else if (md == VTMode.MITMRM && op == VTMode.SET)
      {  mode.states[VTMode.MITMRM] = false;
         mode.states[VTMode.XTMHMRM] = false; }
      else if (md == VTMode.XTMMRM && op == VTMode.SET)
      {  mode.states[VTMode.MITMRM] = false;
         mode.states[VTMode.XTMHMRM] = false; }
      else if (md == VTMode.XTMHMRM && op == VTMode.SET)
      {  mode.states[VTMode.MITMRM] = false;
         mode.states[VTMode.XTMMRM] = false; }
      else if (md == VTMode.DECSCLM || md == VTMode.DECAWM ||
               md == VTMode.DECSCNM || md == VTMode.LNM || md == VTMode.DECTCEM)
      {  update_term_mode(); }
   }
}