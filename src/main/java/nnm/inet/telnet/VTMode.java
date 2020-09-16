/*
 * Visit url for update: http://sourceforge.net/projects/jvtelnet
 * 
 * JvTelnet was developed by Bea Petrovicova <beapetrovicova@yahoo.com>.
 * The sources was donated to sourceforge.net under the terms 
 * of GNU General Public License (GPL). Redistribution of any 
 * part of JvTelnet or any derivative works must include this notice.
 */
package nnm.inet.telnet;

/** 
 * @version 0.23 06/09/2003
 * @author Bea Petrovicova <beapetrovicova@yahoo.com>  
 */
class VTMode {
    /**  2  KAM,       keyboard action mode lock/unlock */
    public static final int KAM = 0;
    /*   3  CRM,       control representation (monitor) */
    /**  4  IRM,!      insert/replace mode */
    public static final int IRM = 1;
    /**  10 HEM,       horizontal editing mode on/off */
    public static final int HEM = 2;
    /**  12 SRM,       send receive mode (local echo on/off) */
    public static final int SRM = 3;
    /**  13 FEAM,      disable control execution */
    /**  16 TTM,       cursor transf termination */
    /**  20 LNM,!      new-line/line-feed mode */
    public static final int LNM = 4;
    /*   30 WYDSCM,    display disble */
    /*   31 WYSTLINM,  status display */
    /*   32 WYCRTSAVM, screen saver */
    /*   33 WYSTCURM,  steady cursor */
    /*   34 WYULCURM,  under cursor */
    /*   35 WYCLRM,    width-chg-clr disable */
    /*   36 WYDELKM,   delete key redefinition */
    /*   37 WYGATM,    nonerasable area transmit */
    /*   38 WYTEXM,    transmit screen */
    /*   39 WYCELL,    10x13 char cell */
    /*   40 WYEXTDM,   25th data line on/off */

    /** ?1  DECCKM,!   cursor key application/cursor mode */
    public static final int DECCKM = 5;
    /** ?2  DECANM,    emulation ANSI/VT52 */
    public static final int DECANM = 6;
    /** ?3  DECCOLM,   132-column/80 column mode */
    public static final int DECCOLM = 7;
    /** ?4  DECSCLM,   smooth/jump scroll */
    public static final int DECSCLM = 8;
    /** ?5  DECSCNM,   screen reverse/normal mode */
    public static final int DECSCNM = 9;
    /** ?6  DECOM,     cursor origin/absolute mode */
    public static final int DECOM = 10;
    /** ?7  DECAWM,    auto wrap on/off */
    public static final int DECAWM = 11;
    /** ?8  DECARM,    auto repeat on/off */
    public static final int DECARM = 12;
    /** ?9  send MIT mouse rep on/off */
    public static final int MITMRM = 13;
    /*  ?10 DECEDM,    block mode */
    /*  ?16 DECEKEM,   local f-keys mode */
    /** ?18 DECPFF,    print form feed on/off */
    public static final int DECPFF = 14;
    /** ?19 DECPEX,    print extent full-screen/scrolling-region */
    public static final int DECPEX = 15;
    /** ?25 DECTCEM,   enable/disable visible cursor mode */
    public static final int DECTCEM = 16;
    /** ?38 DECTEK,    Enter Tektronix mode */
    public static final int DECTEK = 17;
    /** ?40 XTMACOL,   Allow 80-132 switch on/off */
    public static final int XTMACOL = 18;
    /** ?41 XTMCFIX,   Curses(5) fix on/off */
    public static final int XTMCFIX = 19;
    /** ?42 DECNRCM,   national/multinational replacement character set */
    public static final int DECNRCM = 20;

    /** ?44 XTMMBM,    Margin bell on/off */
    public static final int XTMMBM = 21;
    /** ?45 XTMRWM,    Reverse-wrap on/off */
    public static final int XTMRWM = 22;
    /** ?46 XTMLOGM,   Logging on/off */
    public static final int XTMLOGM = 23;
    /** ?47 XTMASM,    alternate/normal screen buffer */

    /*  ?67 DECBKM,    destructive backspace on/off */
    /*  ?68 DECKBUM,   data processing keys on/off */

    public static final int XTMASM = 24;
    /** ?1000 XTMMRM,  VT200 mouse report on / off */
    public static final int XTMMRM = 25;
    /** ?1001 XTMHMRM, VT200 hilight mouse rep on / off */
    public static final int XTMHMRM = 26;

    /**     DECKPAM,
        ?66 DECNKM,    application/numberic keypad */
    public static final int DECKPAM = 27;
    /**     DECELR,    Enable DEC locator reports */
    public static final int DECELR = 28;
    /**     DECOLR,    one-shot locator report */
    public static final int DECOLR = 29;
    /**     DECPLR,    Locator reports in pixels / chars */
    public static final int DECPLR = 30;

    /** Number of modes */
    public static final int N = 31;

    /** Flag status of each mode's command sequence. */
    static final int[] flags =  
    {   0,0,0,0,0,                                        // KAM,IRM,HEM,SRM,LNM
        VTCommand.FLAG_QUES_MASK,VTCommand.FLAG_QUES_MASK,// DECCKM,DECANM
        VTCommand.FLAG_QUES_MASK,VTCommand.FLAG_QUES_MASK,// DECCOLM,DECSCLM
        VTCommand.FLAG_QUES_MASK,VTCommand.FLAG_QUES_MASK,// DECSCNM,DECOM
        VTCommand.FLAG_QUES_MASK,VTCommand.FLAG_QUES_MASK,// DECAWM,DECARM
        VTCommand.FLAG_QUES_MASK,VTCommand.FLAG_QUES_MASK,// MITMRM,DECPFF
        VTCommand.FLAG_QUES_MASK,VTCommand.FLAG_QUES_MASK,// DECPEX,DECTCEM
        VTCommand.FLAG_QUES_MASK,VTCommand.FLAG_QUES_MASK,// DECTEX,XTMACOL
        VTCommand.FLAG_QUES_MASK,VTCommand.FLAG_QUES_MASK,// XTMCFIX,DECNRCM
        VTCommand.FLAG_QUES_MASK,VTCommand.FLAG_QUES_MASK,// XTMMBM,XTMRWM
        VTCommand.FLAG_QUES_MASK,VTCommand.FLAG_QUES_MASK,// XTMLOGM,XTMASM
        VTCommand.FLAG_QUES_MASK,VTCommand.FLAG_QUES_MASK,// XTMMRM,XTMHMRM
        VTCommand.FLAG_QUES_MASK,0,                       // DECKPAM,DECELR
        0,0 };                                            // DECOLR,DECPLR

    /** Parameter value of each mode's command sequence. */
    static final int[] values =  
    {    2, 4,10,12,20,                                   // KAM,IRM,HEM,SRM,LNM
         1, 2, 3, 4,                                      // DECCKM,DECANM,DECCOLM,DECSCLM
         5, 6, 7, 8,                                      // DECSCNM,DECOM,DECAWM,DECARM
         9,18,19,25,                                      // MITMRM,DECPFF,DECPEX,DECTCEM
        38,40,41,42,                                      // DECTEX,XTMACOL,XTMCFIX,DECNRCM
        44,45,46,47,                                      // XTMMBM,XTMRWM,XTMLOGM,XTMASM,
        1000,1001,                                        // XTMMRM,XTMHMRM
    // These are not standard mode syntax.
        66, 2, 2, 2 };                                    // DECKPAM,DECELR,DECOLR,DECPLR

    /** Initial values of modes. */
    static final boolean[] inits =  
    {   false,false,false,false,false,                    // KAM,IRM,HEM,SRM,LNM
        false, true,false,false,                          // DECCKM,DECANM,DECCOLM,DECSCLM
        false,false, true, true,                          // DECSCNM,DECOM,DECAWM,DECARM
        false,false, true, true,                          // MITMRM,DECPFF,DECPEX,DECTCEM
        false,false,false, true,                          // DECTEX,XTMACOL,XTMCFIX,DECNRCM
        false,false,false,false,                          // XTMMBM,XTMRWM,XTMLOGM,XTMASM,
        false,false,                                      // XTMMRM,XTMHMRM
        false,false,false,false };                        // DECKPAM,DECELR,DECOLR,DECPLR

    /** Names of the modes. */
    static final String[] names =  
    {   "Keyboard action","Insertion","horizontal editing","Send-receive","Linefeed",
        "Cursor key","ANSI/VT52","Column","Scrolling",
        "Screen","Origin","Wraparound","Auto-repeat",
        "Send MIT mouse","Print formfeed","Print extent",
        "Text cursor","Tektronix","Allow column switch",
        "Curses fix","Character set","Margin bell","Reverse-wraparound",
        "Logging","Alternate screen","VT200 mouse report",
        "VT200 hilight mouse report","Keypad","Enable locator reports",
        "One-shot locator report","Pixel-unit locator reports" };

    /** Abbreviations of mode names. */
    static final String[] abbrvs =  
    {   "KAM","IRM","HEM","SRM","LNM","DECCKM","DECANM","DECCOLM",
        "DECSCLM","DECSCNM","DECOM","DECAWM","DECARM",
        "MITMRM","DECPFF","DECPEX","DECTCEM","DECTEX",
        "XTMACOL","XTMCFIX","DECNRCM","XTMMBM","XTMRWM",
        "XTMLOGM","XTMASM","XTMMWM","XTMHMRM","DECKPAM",
        "DECELR","DECOLR","DECPLR" };

    /** Current state of modes. */
    boolean[] states = new boolean[N];
    /** Saved state of modes. */
    boolean[] saves = new boolean[N];
    
    /** Operation CSI Pn l */
    static final int RESET = 0;
    /** Operation CSI Pn h */
    static final int SET = 1;
    /** Operation CSI Pn r */
    static final int SAVE = 2;
    /** Operation CSI Pn s */
    static final int RESTORE = 3;
}
