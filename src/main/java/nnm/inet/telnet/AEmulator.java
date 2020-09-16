/*
 * Visit url for update: http://sourceforge.net/projects/jvtelnet
 * 
 * JvTelnet was developed by Bea Petrovicova <beapetrovicova@yahoo.com>.
 * The sources was donated to sourceforge.net under the terms 
 * of GNU General Public License (GPL). Redistribution of any 
 * part of JvTelnet or any derivative works must include this notice.
 */
package nnm.inet.telnet;

import java.io.IOException;

/** 
 * @version 0.23 06/09/2003
 * @author Bea Petrovicova <beapetrovicova@yahoo.com>  
 */
public class AEmulator {
    Telnet telnet;
    ATerminal term;

    AMapper map;
    ATranslator tran;
    VTMode mode = null;

    String commport = "COM1";
    int baudrate = 9600;
    String flowcontrol = "none";
    String answerback = "";

    /** Current tabstop columns settings - true is a tabstop. */
    boolean[] tabs = new boolean[512]; // lots of room!

    public AEmulator(AMapper map, ATranslator tran) {
        this.map = map;
        this.tran = tran;
        map.emu = this;

        for (int i = 8; i < tabs.length; i += 8)
            tabs[i] = true;
    }

    public int getTypeNum() {
        return 0;
    }

    public String getTypeName(int num) {
        return "DUMB";
    }

    public void receive(char ch) { /* *** Filter Special Characters *** */
        if (term != null
            && ((ch >= (char) 0x20 && ch < (char) 0x7F)
                || (ch > (char) 0x9F && ch < (char) 0xFF)
                || ch == '\n'
                || ch == '\r'
                || ch == '\b'
                || ch == '\t'))
            term.receive(ch);
    }

    public void send(char ch) {
        try {
            telnet.send(ch);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void send(byte[] b) {
        try {
            telnet.send(b);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void send(byte[] b, int off, int len) {
        try {
            telnet.send(b, off, len);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void send(String str) {
        try {
            telnet.send(str);
        } catch (IOException e) {
            System.out.println(str);
        }
    }

    public void setWindowSize(int cols, int rows) {
        if (telnet != null)
            telnet.setWindowSize(cols, rows);
    }
}
