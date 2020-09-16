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
public class ATranslator {
    protected static final char uk[] = new char[0x80],
        asc[] = new char[0x80],
        dec[] = new char[0x80],
        rt[]  = new char[0x80];
    private char g0[] = asc,
        g1[] = dec,
        g2[] = rt,
        g3[] = rt,
        gl[] = asc,
        gr[] = rt;
    private static final class CHARSET {
        private static final char UK = 'A', ASC = 'B', DEC = '0';
    }

    public ATranslator() { 
        /* 1:1 - DEFAULTS */
        for (int i = 0x0; i < 0x7F; i++) {
            uk[i] = asc[i] = dec[i] = (char) i;
            rt[i] = (char) (0x80 + i);
        }

        /* POUND SIGN */
        uk[0x23] = 0x00A3;

        /* DEC SPECIAL GRAPHICS / BOX DRAWING */
        dec[0x6a] = '+'; // BOX DRAWING / TOP RIGHT CORNER
        dec[0x6b] = '+'; // BOX DRAWING / BOTTOM RIGHT CORNER
        dec[0x6c] = '+'; // BOX DRAWING / TOP LEFT CORNER
        dec[0x6d] = '+'; // BOX DRAWING / BOTTOM LEFT CORNER
        dec[0x6e] = '+'; // BOX DRAWING / CROSS CORNER
        dec[0x71] = '-'; // BOX DRAWING / HORIZONTAL LINE
        dec[0x74] = '+'; // BOX DRAWING / VERTICAL RIGHT BI-CORNER
        dec[0x75] = '+'; // BOX DRAWING / VERTICAL LEFT BI-CORNER
        dec[0x76] = '+'; // BOX DRAWING / HORIZONTAL TOP BI-CORNER
        dec[0x77] = '+'; // BOX DRAWING / HORIZONTAL BOTTOM BI-CORNER
        dec[0x78] = '|'; // BOX DRAWING / VERTICAL LINE 
    }

    public void designate(char buf, char set) {
        char a[];
        switch (set) {
            case 'A' :
                a = uk;
                break;
            case 'B' :
                a = asc;
                break;
            case '0' :
                a = dec;
                break;
            default :
                return;
        }
        switch (buf) {
            case '(' :
                g0 = a;
                break;
            case ')' :
                g1 = a;
                break;
            case '*' :
                g2 = a;
                break;
            case '+' :
                g3 = a;
                break;
            default :
                return;
        }
    }

    public void invoke(char code, char buf) {
        char a[];
        switch (buf) {
            case '(' :
                a = g0;
                break;
            case ')' :
                a = g1;
                break;
            case '*' :
                a = g2;
                break;
            case '+' :
                a = g3;
                break;
            default :
                return;
        }
        if (code == 'L')
            gl = a;
        else
            gr = a;
    }

    public char code(char ch) {
        if (ch < 0x80)
            return gl[ch];
        else if (ch <= 0xFF)
            return gr[ch - 0x80];
        else
            return ch;
    }
}
