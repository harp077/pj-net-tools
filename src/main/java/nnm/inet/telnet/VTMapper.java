/*
 * Visit url for update: http://sourceforge.net/projects/jvtelnet
 * 
 * JvTelnet was developed by Bea Petrovicova <beapetrovicova@yahoo.com>.
 * The sources was donated to sourceforge.net under the terms 
 * of GNU General Public License (GPL). Redistribution of any 
 * part of JvTelnet or any derivative works must include this notice.
 */
package nnm.inet.telnet;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/** 
 * @version 0.23 06/09/2003
 * @author Bea Petrovicova <beapetrovicova@yahoo.com>  
 */
class VTMapper extends AMapper {
    private final static String ESC = "" + C0.ESC;
    private final static String CSI = "" + C1.CSI;
    private final static String SS3 = "" + C1.SS3;

    VTMode mode = null;

    public VTMapper() {
    }

    /*DEC VT52 *
    final static class VT52 {
        private final static String
            // Application Mode ???
            NUMPAD0 = ESC + "?p",
            NUMPAD1 = ESC + "?q",
            NUMPAD2 = ESC + "?r",
            NUMPAD3 = ESC + "?s",
            NUMPAD4 = ESC + "?t",
            NUMPAD5 = ESC + "?u",
            NUMPAD6 = ESC + "?v",
            NUMPAD7 = ESC + "?w",
            NUMPAD8 = ESC + "?x",
            NUMPAD9 = ESC + "?y",
            // Application Mode ???
            ADD = ESC + "?l", // NumPad + instead non existent NumPad , 
            SUBTRACT = ESC + "?m", // NumPad - 
            DECIMAL = ESC + "?n", // NumPad . 
            ENTER = ESC + "?M", // \n       instead non existent NumPad \n 
            // Application Mode & Cursor ???
            UP = ESC + "A", 
            DOWN = ESC + "B", 
            RIGHT = ESC + "C", 
            LEFT = ESC + "D";
    }; */ 

    //DEC VT420/320/220/102/100
    final static class VT100 {
        private final static String

            // Application Mode ???
            NUMPAD0 = ESC + "Op",
            NUMPAD1 = ESC + "Oq",
            NUMPAD2 = ESC + "Or",
            NUMPAD3 = ESC + "Os",
            NUMPAD4 = ESC + "Ot",
            NUMPAD5 = ESC + "Ou",
            NUMPAD6 = ESC + "Ov",
            NUMPAD7 = ESC + "Ow",
            NUMPAD8 = ESC + "Ox",
            NUMPAD9 = ESC + "Oy",

            // Application Mode ???
            ADD = ESC + "Ol", // NumPad + instead non existent NumPad , 
            SUBTRACT = ESC + "Om", // NumPad - 
            DECIMAL = ESC + "On", // NumPad . 
            ENTER = ESC + "OM", // \n       instead non existent NumPad \n 
            //KPENTER CSI+"29~"
            //SENTER ^M=CR

            // Cursor Mode OK
            CUP = ESC + "[A",
            CDOWN = ESC + "[B",
            CRIGHT = ESC + "[C",
            CLEFT = ESC + "[D",

            // Application Mode ???
            AUP = ESC + "OA",
            ADOWN = ESC + "OB",
            ARIGHT = ESC + "OC",
            ALEFT = ESC + "OD";
    };

    //DEC VT420/320/220/102/100
    final static class VT200 {
        private final static String 
            INSERT = ESC + "OS",
            DELETE = "" + C1.DEL,
            //???
            HOME = ESC + "[H",
            END = ESC + "[25~",
            PAGE_UP = ESC + "[U",
            PAGE_DOWN = ESC + "[V",
            ENTER = "\r",
            KPENTER = ESC + "[29~",
            NUM_LOCK = ESC + "OP",

            //TAB       = ""+C0.HT,
            //STAB      = ESC+"[Z", //??? 

            // "\r\n", ESC OM SS3 M ESC ? M 
            //zz        = ESC+"OP", 

            F1 = ESC + "[?5i",
            F2 = ESC + "[?3i",
            F3 = ESC + "[2i",
            F4 = ESC + "[@",
            F5 = ESC + "[M",
            F6 = ESC + "[17~",
            F7 = ESC + "[18~",
            F8 = ESC + "[19~",
            F9 = ESC + "[20~",
            F10 = ESC + "[21~",
            F11 = ESC + "[23~",
            F12 = ESC + "[24~",
            SF1 = ESC + "[5i",
            SF2 = ESC + "[?1i",
            SF3 = ESC + "[0i",
            SF4 = ESC + "[L",
            SF5 = ESC + "[K",
            SF6 = ESC + "[31~",
            SF7 = ESC + "[32~",
            SF8 = ESC + "[33~",
            SF9 = ESC + "[34~",
            SF10 = ESC + "[35~",
            SF11 = ESC + "[1~",
            SF12 = ESC + "[2~";
    };

    boolean processCursor(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP :
                send(mode.states[VTMode.DECCKM] ? VT100.AUP : VT100.CUP);
                return true;
            case KeyEvent.VK_DOWN :
                send(mode.states[VTMode.DECCKM] ? VT100.ADOWN : VT100.CDOWN);
                return true;
            case KeyEvent.VK_RIGHT :
                send(mode.states[VTMode.DECCKM] ? VT100.ARIGHT : VT100.CRIGHT);
                return true;
            case KeyEvent.VK_LEFT :
                send(mode.states[VTMode.DECCKM] ? VT100.ALEFT : VT100.CLEFT);
                return true;
            default :
                return false;
        }
    }

    boolean processNumber(KeyEvent e) {
        switch (e.getKeyCode()) {
            // Application Mode 
            case KeyEvent.VK_NUMPAD0 :
                send(VT100.NUMPAD0);
                return true;
            case KeyEvent.VK_NUMPAD1 :
                send(VT100.NUMPAD1);
                return true;
            case KeyEvent.VK_NUMPAD2 :
                send(VT100.NUMPAD2);
                return true;
            case KeyEvent.VK_NUMPAD3 :
                send(VT100.NUMPAD3);
                return true;
            case KeyEvent.VK_NUMPAD4 :
                send(VT100.NUMPAD4);
                return true;
            case KeyEvent.VK_NUMPAD5 :
                send(VT100.NUMPAD5);
                return true;
            case KeyEvent.VK_NUMPAD6 :
                send(VT100.NUMPAD6);
                return true;
            case KeyEvent.VK_NUMPAD7 :
                send(VT100.NUMPAD7);
                return true;
            case KeyEvent.VK_NUMPAD8 :
                send(VT100.NUMPAD8);
                return true;
            case KeyEvent.VK_NUMPAD9 :
                send(VT100.NUMPAD9);
                return true;
                // Application Mode 
            case KeyEvent.VK_ADD :
                send(VT100.ADD);
                return true;
            case KeyEvent.VK_SUBTRACT :
                send(VT100.SUBTRACT);
                return true;
            case KeyEvent.VK_DECIMAL :
                send(VT100.DECIMAL);
                return true;
            case KeyEvent.VK_ENTER :
                send(VT100.ENTER);
                return true;
            default :
                return false;
        }
    }
    /** is200 (or+) Emulation Mode? */
    private boolean is200 = true;

    boolean isShift(KeyEvent e) {
        return (e.getModifiers() == InputEvent.SHIFT_MASK);
    }

    boolean process200(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_INSERT :
                send(VT200.INSERT);
                return true;
            case KeyEvent.VK_DELETE :
                send(VT200.DELETE);
                return true;
            case KeyEvent.VK_HOME :
                send(VT200.HOME);
                return true;
            case KeyEvent.VK_END :
                send(VT200.END);
                return true;
            case KeyEvent.VK_PAGE_UP :
                send(VT200.PAGE_UP);
                return true;
            case KeyEvent.VK_PAGE_DOWN :
                send(VT200.PAGE_DOWN);
                return true;

            case KeyEvent.VK_ENTER :
                send(isShift(e) ? VT200.KPENTER : VT200.ENTER);
                return true;
            case KeyEvent.VK_NUM_LOCK :
                send(VT200.NUM_LOCK);
                return true;

            case KeyEvent.VK_F1 :
                send(isShift(e) ? VT200.SF1 : VT200.F1);
                return true;
            case KeyEvent.VK_F2 :
                send(isShift(e) ? VT200.SF2 : VT200.F2);
                return true;
            case KeyEvent.VK_F3 :
                send(isShift(e) ? VT200.SF3 : VT200.F3);
                return true;
            case KeyEvent.VK_F4 :
                send(isShift(e) ? VT200.SF4 : VT200.F4);
                return true;
            case KeyEvent.VK_F5 :
                send(isShift(e) ? VT200.SF5 : VT200.F5);
                return true;
            case KeyEvent.VK_F6 :
                send(isShift(e) ? VT200.SF6 : VT200.F6);
                return true;
            case KeyEvent.VK_F7 :
                send(isShift(e) ? VT200.SF7 : VT200.F7);
                return true;
            case KeyEvent.VK_F8 :
                send(isShift(e) ? VT200.SF8 : VT200.F8);
                return true;
            case KeyEvent.VK_F9 :
                send(isShift(e) ? VT200.SF9 : VT200.F9);
                return true;
            case KeyEvent.VK_F10 :
                send(isShift(e) ? VT200.SF10 : VT200.F10);
                return true;
            case KeyEvent.VK_F11 :
                send(isShift(e) ? VT200.SF11 : VT200.F11);
                return true;
            case KeyEvent.VK_F12 :
                send(isShift(e) ? VT200.SF12 : VT200.F12);
                return true;
            default :
                return false;
        }
    }

    KeyEvent cKey = null;
    public void keyPressed(KeyEvent e) {
        if (!mode.states[VTMode.KAM]) {
            cKey = e;
            if (processCursor(e))
                return;
            if (is200)
                if (process200(e))
                    return;
        }
    }

    public void keyTyped(KeyEvent e) {
        if (!mode.states[VTMode.KAM]
            || e.getKeyChar() == C0.ESC) { 
            //if(processTab(cKey)) return;
            if (mode.states[VTMode.DECKPAM])
                if (processNumber(cKey))
                    return;
            char ch = e.getKeyChar();
            if (ch == KeyEvent.VK_ENTER)
                return;
            send(ch);
        }
    }

/*  boolean processTab(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_TAB :
                send(isShift(e) ? VT200.STAB : VT200.TAB);
                return true;
            default :
                return false;
        }
    } */
}

