/*
 * Visit url for update: http://sourceforge.net/projects/jvtelnet
 * 
 * JvTelnet was developed by Bea Petrovicova <beapetrovicova@yahoo.com>.
 * The sources was donated to sourceforge.net under the terms 
 * of GNU General Public License (GPL). Redistribution of any 
 * part of JvTelnet or any derivative works must include this notice.
 */
package nnm.inet.telnet;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/** 
 * @version 0.23 06/09/2003
 * @author Bea Petrovicova <beapetrovicova@yahoo.com>  
 */
class AMapper extends KeyAdapter {
    AEmulator emu;

    AMapper() {
    }

    public void keyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
        if (ch == '\n')
            ch = '\r';
        send(ch);
    }

    void send(char ch) {
        emu.send(ch);
    }

    public synchronized void send(String s) {
        for (int i = 0; i < s.length(); i++)
            send(s.charAt(i));
    }
}
