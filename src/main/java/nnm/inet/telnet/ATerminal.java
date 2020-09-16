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
interface ATerminal {
    //final Style style = new Style();
    final class Style {
        final static short 
            PLAIN = (short) 0,
            BOLD = (short) 1,
            ITALIC = (short) (1 << 1),
            INVERSE = (short) (1 << 2),
            UNDERLINE = (short) (1 << 3);
    }

    abstract public void setEmulator(AEmulator emu);

    abstract public void CR();
    abstract public void LF();
    abstract public void BS();
    abstract public void HT();

    abstract public void receive(char ch);
    abstract public void receive(char ch, int format);
    abstract public void send(char ch);

    //abstract public void designateCharSet(char buf,char set);
    //abstract public void invokeCharSet(char code,char buf);

    abstract public int getNumRows();
    abstract public int getNumCols();

    abstract public int getCursorRow();
    abstract public int getCursorCol();

    abstract public void setCursorPos(int row, int col);

    abstract public void setCursorVisible(boolean state);
    abstract public boolean getCursorVisible();

    abstract public int getAreaTop();
    abstract public int getAreaBottom();
    abstract public int getAreaLeft();
    abstract public int getAreaRight();

    abstract public void resetArea();
    abstract public void setArea(int top, int bottom);
    abstract public void setArea(int top, int bottom, int left, int right);

    abstract public short getStyle();
    abstract public void setStyle(int style);

    abstract public short getForeColor();
    abstract public short getBackColor();
    abstract public void setForeColor(int color);
    abstract public void setBackColor(int color);

    abstract public void setChars(int left, int right, char ch, int style);
    abstract public void delChars(int left, int right, char ch, int style);
    abstract public void insChars(int left, int right, char ch, int style);

    abstract public void clearEOL(char ch, int style);
    abstract public void clearBOL(char ch, int style);
    abstract public void clearLine(char ch, int style);

    abstract public void clearEOD(char ch, int style);
    abstract public void clearBOD(char ch, int style);
    abstract public void clearAll(char ch, int style);

    abstract public void rollRegion(int top, int bottom, int amount);
    abstract public void rollScreen(int amount);
}