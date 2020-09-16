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
class C1 {
    /** DELETE (acctually not part of C1) */
    public static final char DEL = (char) 0x7f;
    /** BREAK PERMITTED HERE */
    public static final char BPH = (char) 0x82;
    /** NO BREAK HERE */
    public static final char NBH = (char) 0x83;
    /** INDEX */
    public static final char IND = (char) 0x84;
    /** NEXT LINE */
    public static final char NEL = (char) 0x85;
    /** START OF SELECTED AREA  */
    public static final char SSA = (char) 0x86;
    /** END OF SELECTED AREA */
    public static final char ESA = (char) 0x87;
    /** HORIZONTAL TABULATION SET */
    public static final char HTS = (char) 0x88;
    /** HORIZONTAL TABULATION SET WITH JUSTIFICATION */
    public static final char HTJ = (char) 0x89;
    /** VERTICAL TABULATION SET */
    public static final char VTS = (char) 0x8a;
    /** PARTIAL LINE DOWN */
    public static final char PLD = (char) 0x8b;
    /** PARTIAL LINE UP */
    public static final char PLU = (char) 0x8c;
    /** REVERSE INDEX */
    public static final char RI = (char) 0x8d;
    /** SINGLE SHIFT 2 */
    public static final char SS2 = (char) 0x8e;
    /** SINGLE SHIFT 3 */
    public static final char SS3 = (char) 0x8f;
    /** DEVICE CONTROL STRING */
    public static final char DCS = (char) 0x90;
    /** PRIVATE USE 1 */
    public static final char PU1 = (char) 0x91;
    /** PRIVATE USE 2 */
    public static final char PU2 = (char) 0x92;
    /** SET TRANSMIT STATE */
    public static final char STS = (char) 0x93;
    /** CANCEL CHARACTER */
    public static final char CCH = (char) 0x94;
    /** MESSAGE WAITING */
    public static final char MW = (char) 0x95;
    /** START OF PROTECTED AREA */
    public static final char SPA = (char) 0x96;
    /** END OF PROTECTED AREA */
    public static final char EPA = (char) 0x97;
    /** START OF STRING */
    public static final char SOS = (char) 0x98;
    /** SINGLE CHARACTER INTRODUCER */
    public static final char SCI = (char) 0x9a;
    /** CONTROL SEQUENCE INTRODUCER */
    public static final char CSI = (char) 0x9b;
    /** STRING TERMINATOR */
    public static final char ST = (char) 0x9c;
    /** OPERATING SYSTEM COMMAND */
    public static final char OSC = (char) 0x9d;
    /** PRIVACY MESSAGE */
    public static final char PM = (char) 0x9e;
    /** APPLICATION PROGRAM COMMAND */
    public static final char APC = (char) 0x9f;

    private static final String abbs[] =
    {   "DEL", 
        "x80", "x81", "BPH", "NBH", "IND", "NEL", "SSA", "ESA", 
        "HTS", "HTJ", "VTS", "PLD", "PLU", "RI",  "SS2", "SS3", 
        "DCS", "PU1", "PU2", "STS", "CCH", "MW",  "SPA", "EPA", 
        "SOS", "x99", "SCI", "CSI", "ST",  "OSC", "PM",  "APC"};

    public static String toString(char ch) {
        if (ch >= 0x7f && ch <= 0x9f)
            return abbs[ch];
        else
            return "x??";
    }
}