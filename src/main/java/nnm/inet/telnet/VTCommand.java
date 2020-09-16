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
 * This class encapsulates all of the information about a 
 * parsed command,and some basic methods to help the parser.  
 * See the VT200 class for what these values mean and details 
 * on the format of commands.
 *  
 * @version 0.23 06/09/2003
 * @author Bea Petrovicova <beapetrovicova@yahoo.com>  
 */
class VTCommand {
    /* Types of commands. */

    /** VTCommand TYPE Non-command */
    public static final int TYPE_NONE = 0;
    /** VTCommand TYPE ESC-term */
    public static final int TYPE_ESC = 1;
    /** VTCommand TYPE ESC-[-params-inter-term */
    public static final int TYPE_CSI = 2;
    /** VTCommand TYPE ESC-P-params-inter-term-data */
    public static final int TYPE_DCS = 3;
    /** VTCommand TYPE ESC-]-params-inter-term-data */
    public static final int TYPE_OSC = 4;
    /** VTCommand TYPE ESC-^-params-inter-term-data */
    public static final int TYPE_PM = 5;
    /** VTCommand TYPE ESC-_-params-inter-term-data */
    public static final int TYPE_APC = 6;

    /* This one is used for constructing commands that 
      don't follow a standard ASCII command structure. */

    /** Just use raw data string. */
    public static final int TYPE_RAW = 7;

    /* Flags encountered during parameter parsing. These are bit values. */

    /** VTCommand FLAG '<' encountered in params */
    public static final int FLAG_LT = 0; // 
    /** VTCommand FLAG '<' encountered in params MASK */
    public static final int FLAG_LT_MASK = (1 << FLAG_LT);
    /** VTCommand FLAG '=' encountered in params */
    public static final int FLAG_EQ = 1;
    /** VTCommand FLAG '=' encountered in params MASK */
    public static final int FLAG_EQ_MASK = (1 << FLAG_EQ);
    /** VTCommand FLAG '>' encountered in params */
    public static final int FLAG_GT = 2;
    /** VTCommand FLAG '>' encountered in params MASK */
    public static final int FLAG_GT_MASK = (1 << FLAG_GT);
    /** VTCommand FLAG '?' encountered in params */
    public static final int FLAG_QUES = 3;
    /** VTCommand FLAG '?' encountered in params MASK */
    public static final int FLAG_QUES_MASK = (1 << FLAG_QUES);
    /** The maximum number of parameters we can handle at one time. */
    public static final int MAX_PARAMS = 64;
    /** The maximum number of intermediate characters we can handle at one time. */
    public static final int MAX_INTERS = 16;

    /** One of VT100Emulator's state codes */
    public int type = 0;
    /** Current parameters */
    public int cur_param = 0;
    /** Parsed parameter values */
    public int[] params = new int[MAX_PARAMS];
    /** Was a value supplied? */
    public boolean[] has_params = new boolean[MAX_PARAMS];
    /** Param flags: <,=,>,? */
    public int flags;
    /** Current intermediate character */
    public int cur_inter = 0;
    /** Intermediate characters supplied */
    public char[] inters = new char[MAX_INTERS];
    /** Data string. */
    public StringBuffer data = new StringBuffer(64);
    /** Terminating character */
    public char term = 0;

    public VTCommand() {
    }

    /** Reset the command. */
    public void init() {
        type = 0;
        for (int i = 0; i <= cur_param && i < MAX_PARAMS; i++) {
            params[i] = 0;
            has_params[i] = false;
        }
        cur_param = 0;
        flags = 0;
        cur_inter = 0;
        data.setLength(0);
        term = 0;
    }

    /** Set the command type,e.g. TYPE_CSI. */
    public boolean setType(int type) {
        this.type = type;
        return true;
    }

    /* This is the programmic interface to the command: it 
     allows commands to be constructed in their logical units. */

    /** Directly add a paramter to the command. */
    public boolean addParam(int param, int flags) {
        if (cur_param < MAX_PARAMS) {
            params[cur_param] = param;
            has_params[cur_param] = true;
            this.flags |= flags;
            cur_param++;
            return true;
        }
        return false;
    }

    /** Skip [set to default] next parameter in command. */
    public boolean skipParam() {
        if (cur_param < MAX_PARAMS) {
            params[cur_param] = 0;
            has_params[cur_param] = false;
            cur_param++;
            return true;
        }
        return false;
    }

    /** Directly add intermediate characters to the command. */
    public boolean addIntermediates(String inter) {
        int i = 0;
        while (i < inter.length() && cur_inter < MAX_INTERS) {
            parseInterChar(inter.charAt(i));
            i++;
        }
        if (i >= inter.length())
            return true;
        return false;
    }

    /** Directly add string data to the command. */
    public boolean addData(String data) {
        this.data.append(data);
        return true;
    }

    /** Directly add data to the command. */
    public boolean addData(char data) {
        this.data.append(data);
        return true;
    }

    /** Directly set the terminating character. */
    public boolean setTerminator(char t) {
        term = t;
        return true;
    }

    /* This is the parser interface to the command: it allows commands
     to be constructed one character at a time,as they occur in the
     command sequence. */

    /** Parse the next character in a parameter stream or flag character. */
    public boolean parseParamChar(char c) {
        if (c >= '0' && c <= '9') {
            if (cur_param < MAX_PARAMS) {
                params[cur_param] *= 10;
                params[cur_param] += (int) (c - '0');
                has_params[cur_param] = true;
            }
            return true;
        } else if (c == ';' || c == ':') {
            cur_param++;
            if (cur_param > MAX_PARAMS)
                cur_param = MAX_PARAMS;
            if (cur_param < MAX_PARAMS)
                has_params[cur_param] = true;
            return true;
        } else if (c >= '<' && c <= '?') {
            flags |= 1 << ((int) (c - '<'));
            return true;
        }
        return false;
    }

    /** Add the next intermediate character. */
    public boolean parseInterChar(char c) {
        if (cur_inter < MAX_INTERS) {
            inters[cur_inter] = c;
            cur_inter++;
        }
        return true;
    }

    /** Add the next data character. */
    public boolean parseDataChar(char c) {
        data.append(c);
        return true;
    }

    /** Set the terminating character. */
    public boolean parseTermChar(char c) {
        if (cur_param >= MAX_PARAMS)
            cur_param = MAX_PARAMS - 1;
        if (cur_param < MAX_PARAMS) {
            if (has_params[cur_param])
                cur_param++;
        }
        term = c;
        return true;
    }

    public String paramsToString() {
        int i;
        String str;
        str = "Params:";
        for (i = 0; i < cur_param; i++) {
            if (has_params[i])
                str = str + " " + params[i];
            else
                str = str + " <DEF>";
        }
        return str;
    }

    public String toSequence() {
        StringBuffer str = new StringBuffer(64);
        switch (type) {
            case TYPE_ESC :
                str.append("\033");
                break;
            case TYPE_CSI :
                str.append("\033[");
                break;
            case TYPE_DCS :
                str.append("\033P");
                break;
            case TYPE_OSC :
                str.append("\033]");
                break;
            case TYPE_PM :
                str.append("\033^");
                break;
            case TYPE_APC :
                str.append("\033_");
                break;
            case TYPE_RAW :
                return data.toString();
        }
        if ((flags & FLAG_LT_MASK) != 0)
            str.append('<');
        if ((flags & FLAG_EQ_MASK) != 0)
            str.append('=');
        if ((flags & FLAG_GT_MASK) != 0)
            str.append('>');
        if ((flags & FLAG_QUES_MASK) != 0)
            str.append('?');
        for (int i = 0; i < cur_param; i++) {
            if (has_params[i]) {
                str.append(params[i]);
            }
            if (i < (cur_param - 1))
                str.append(';');
        }
        if (cur_inter > 0) {
            str.append(inters, 0, cur_inter);
        }
        str.append(term);
        if (data.length() > 0) {
            str.append(data.toString());
            str.append("\033\\");
        }
        return str.toString();
    }

    public String toString() {
        return VTEmulator.sequenceToString(toSequence());
    }
}