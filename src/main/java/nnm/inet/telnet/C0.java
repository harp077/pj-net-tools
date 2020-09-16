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
class C0 {
    /** NULL */
    public static final char NUL = 0;
    /** START OF HEADING */
    public static final char SOH = (char) 0x01;
    /** START OF TEXT */
    public static final char STX = (char) 0x02;
    /** END OF TEXT */
    public static final char ETX = (char) 0x03;
    /** END OF TRANSMISSION */
    public static final char EOT = (char) 0x04;
    /** ENQUIRY */
    public static final char ENQ = (char) 0x05;
    /** ACKNOWLEDGE */
    public static final char ACK = (char) 0x06;
    /** BELL */
    public static final char BEL = (char) 0x07;
    /** BACKSPACE */
    public static final char BS = (char) 0x08;
    /** HORIZONTAL TAB */
    public static final char HT = (char) 0x09;
    /** LINE FEED */
    public static final char LF = (char) 0x0a;
    /** VERTICAL TAB */
    public static final char VT = (char) 0x0b;
    /** FORM FEED */
    public static final char FF = (char) 0x0c;
    /** CARRIAGE RETURN */
    public static final char CR = (char) 0x0d;
    /** SHIFT OUT / LOCK SHIFT 1 */
    public static final char SO = (char) 0x0e, LS1 = (char) 0x0e;
    /** SHIFT IN / LOCK SHIFT 0 */
    public static final char SI = (char) 0x0f, LS0 = (char) 0x0f;
    /** DATA LINK ESCAPE */
    public static final char DLE = (char) 0x10;
    /** DEVICE CONTROL 1 (XON) */
    public static final char DC1 = (char) 0x11, XON = (char) 0x11;
    /** DEVICE CONTROL 2 */
    public static final char DC2 = (char) 0x12;
    /** DEVICE CONTROL 3 (XOFF) */
    public static final char DC3 = (char) 0x13, XOFF = (char) 0x13;
    /** DEVICE CONTROL 4 */
    public static final char DC4 = (char) 0x14;
    /** NEGATIVE ACKNOWLEDGE */
    public static final char NAK = (char) 0x15;
    /** SYNCHRONOUS IDLE */
    public static final char SYN = (char) 0x16;
    /** END OF TRANSMISSION BLOCK */
    public static final char ETB = (char) 0x17;
    /** CANCEL */
    public static final char CAN = (char) 0x18;
    /** END OF MEDIUM */
    public static final char EM = (char) 0x19;
    /** SUBSTITUTE */
    public static final char SUB = (char) 0x1a;
    /** ESCAPE */
    public static final char ESC = (char) 0x1b;
    /** FILE SEPARATOR */
    public static final char FS = (char) 0x1c;
    /** GROUP SEPARATOR */
    public static final char GS = (char) 0x1d;
    /** RECORD SEPARATOR */
    public static final char RS = (char) 0x1e;
    /** UNIT SEPARATOR */
    public static final char US = (char) 0x1f;

    private static final String abbs[] =
    {   "NUL", "SOH", "STX", "ETX", "EOT", "ENQ", "ACK", "BEL", 
        "BS",  "HT",  "LF","VT","FF","CR",  "SO",  "SI",  
        "DLE", "DC1", "DC2", "DC3", "DC4", "NAK", "SYN", "ETB", 
        "CAN", "EM",  "SUB", "ESC", "FS",  "GS",  "RS",  "US","[]"};

    public static String toString(char ch) {
        if (ch < 0x20)
            return (ch == ESC ? "\n<" : "<") 
                + abbs[ch]
                + (ch == LF || ch == VT || ch == FF ? ">\n" : ">");
        else
            return "" + ch;
    }
}
