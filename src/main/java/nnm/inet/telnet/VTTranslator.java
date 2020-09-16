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
public class VTTranslator extends ATranslator {
    protected static final char c0[] = new char[0x20];

    public VTTranslator() {
        super();
        /* CONTROL PICTURES / C0 */
        for (int i = 0; i < 0x20; i++)
            c0[i] = (char) (0x2400 + i);

        /* CONTROL PICTURE / DEL */
        //uk[0x7F] = asc[0x7F] = dec[0x7F] = 0x0020; // DEL

        /* DEC SPECIAL GRAPHICS */
        dec[0x59] = 0x0020; // (blank)
        //not mapped        // PIKE SIGN
        dec[0x61] = 0x2592; // (hatched)
        dec[0x62] = 0x2409; // CONTROL PICTURE / HT
        dec[0x63] = 0x240c; // CONTROL PICTURE / FF
        dec[0x64] = 0x240d; // CONTROL PICTURE / CR
        dec[0x65] = 0x240a; // CONTROL PICTURE / LF
        dec[0x66] = 0x00B0; // (circle)
        dec[0x67] = 0x00B1; // +/- SIGN
        dec[0x68] = 0x2400; // CONTROL PICTURE / NL
        dec[0x69] = 0x240b; // CONTROL PICTURE / VT
        dec[0x6a] = 0x2518; // BOX DRAWING / TOP RIGHT CORNER
        dec[0x6b] = 0x2510; // BOX DRAWING / BOTTOM RIGHT CORNER
        dec[0x6c] = 0x250c; // BOX DRAWING / TOP LEFT CORNER
        dec[0x6d] = 0x2514; // BOX DRAWING / BOTTOM LEFT CORNER
        dec[0x6e] = 0x253c; // BOX DRAWING / CROSS CORNER
        //not mapped        //               HORIZONTAL LINE - SCAN 1
        //not mapped        //               HORIZONTAL LINE - SCAN 3
        dec[0x71] = 0x2500; // BOX DRAWING / HORIZONTAL LINE
        //not mapped        //               HORIZONTAL LINE - SCAN 7
        //not mapped        //               HORIZONTAL LINE - SCAN 9
        dec[0x74] = 0x251c; // BOX DRAWING / VERTICAL RIGHT BI-CORNER
        dec[0x75] = 0x2524; // BOX DRAWING / VERTICAL LEFT BI-CORNER
        dec[0x76] = 0x2534; // BOX DRAWING / HORIZONTAL TOP BI-CORNER
        dec[0x77] = 0x252c; // BOX DRAWING / HORIZONTAL BOTTOM BI-CORNER
        dec[0x78] = 0x2502; // BOX DRAWING / VERTICAL LINE
        dec[0x79] = 0x2264; // EQUAL OR SMALLER THAN SIGN
        dec[0x7a] = 0x2265; // EQUAL OR GREATER THAN SIGN
        dec[0x7b] = 0x03c0; // PI SIGN
        dec[0x7b] = 0x2260; // NOT EQUAL SIGN
        dec[0x7d] = 0x00A3; // POUND SIGN 
        dec[0x7e] = 0x00B7; // CENTERED DOT
    }

    public char code(char ch) {
        return (ch < 0x20 ? c0[ch] : super.code(ch));
    }
}
