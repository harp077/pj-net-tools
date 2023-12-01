/*
 * Visit url for update: http://sourceforge.net/projects/jvtelnet
 *
 * JvTelnet was developed by Bea Petrovicova <beapetrovicova@yahoo.com>.
 * The sources was donated to sourceforge.net under the terms
 * of GNU General Public License (GPL). Redistribution of any
 * part of JvTelnet or any derivative works must include this notice.
 */
package nnm.inet.telnet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.Timer;

/**
 * @version 0.23 06/09/2003
 * @author Bea Petrovicova <beapetrovicova@yahoo.com>
 */
public class JTerminal extends JPanel implements ATerminal {
    /* ****** Colors & Styles ****** */

    final static class COLOR {
        final static Color
            BLACK = new Color((0x00 << 16) | (0x00 << 8) | 0x00),
            DARK_BULE = new Color((0x00 << 16) | (0x00 << 8) | 0x80),
            DARK_GREEN = new Color((0x00 << 16) | (0x80 << 8) | 0x00),
            DARK_TURQUOISE = new Color((0x00 << 16) | (0x80 << 8) | 0x80),
            DARK_RED = new Color((0x80 << 16) | (0x00 << 8) | 0x00),
            DARK_VIOLET = new Color((0x80 << 16) | (0x00 << 8) | 0x80),
            DARK_YELLOW = new Color((0x80 << 16) | (0x80 << 8) | 0x00),
            GRAY = new Color((0xCE << 16) | (0xCE << 8) | 0xCE),
            DARK_GRAY = new Color((0x80 << 16) | (0x80 << 8) | 0x80),
            BLUE = new Color((0x00 << 16) | (0x00 << 8) | 0xFF),
            GREEN = new Color((0x00 << 16) | (0xFF << 8) | 0x00),
            TURQUOISE = new Color((0x00 << 16) | (0xFF << 8) | 0xFF),
            RED = new Color((0xFF << 16) | (0x00 << 8) | 0x00),
            VIOLET = new Color((0xFF << 16) | (0x00 << 8) | 0xFF),
            YELLOW = new Color((0xFF << 16) | (0xFF << 8) | 0x00),
            WHITE = new Color((0xFF << 16) | (0xFF << 8) | 0xFF);
    }

    final static Color COLORS[] = {
            COLOR.BLACK,
            COLOR.DARK_BULE,
            COLOR.DARK_GREEN,
            COLOR.DARK_TURQUOISE,
            COLOR.DARK_RED,
            COLOR.DARK_VIOLET,
            COLOR.DARK_YELLOW,
            COLOR.GRAY,
            COLOR.DARK_GRAY,
            COLOR.BLUE,
            COLOR.GREEN,
            COLOR.TURQUOISE,
            COLOR.RED,
            COLOR.VIOLET,
            COLOR.YELLOW,
            COLOR.WHITE };

    Default DEFAULT;
    public static class Default {
        protected final int
            WIDTH,  // Width in Characters
            HEIGHT, // Height in Characters
            BUFFER, // Height of buffer in Characters
            BORDER; // Thickness in Pixels
        protected final short
            BACK, TEXT, FORMAT;

        Default() {
            this(111, 35, 9999);
        }

        Default(final int width, final int height, final int buffer) {
            WIDTH  = width;  // Width in Characters
            HEIGHT = height;  // Height in Characters
            BUFFER = buffer; // Height of buffer in Characters
            BORDER = 4;   // Thickness in Pixels
            //BACK   = (short)0x1; TEXT   = (short)0xE; // Blues
            //BACK = (short) 0x7; TEXT = (short) 0x1; // New-Age
            //BACK = (short) 0x7; TEXT = (short) 0x0; // Netwhistler
            BACK   = (short)0xF; TEXT   = (short)0x0; // Whitney
            //BACK   = (short)0x0; TEXT   = (short)0x7; // Midnight
            ///////////////////////////////////////////////////////////////////////////
            //BACK   = (short)0x0; TEXT   = (short)0xA; // my black + green
            //BACK   = (short)0x0; TEXT   = (short)0xB; // my black + cyan
            //BACK   = (short)0x0; TEXT   = (short)0xC; // my black + red
            //BACK   = (short)0x0; TEXT   = (short)0xD; // my black + magenta
            //BACK   = (short)0x0; TEXT   = (short)0xE; //  black + yellow
            //BACK   = (short)0x0; TEXT   = (short)0xF; //  black + white
            FORMAT = (short) ((BACK << 12) | (TEXT << 8) | Style.PLAIN);
        }
    }

    /* ****** Dim & Rect ****** */

    final class Dim {
        int x = -1, y = -1;

        void set(int x, int y) {
            this.x = x;
            this.y = y;
        }
        void set(Dim orig) {
            x = orig.x;
            y = orig.y;
        }
        void set(Dimension orig) {
            x = orig.width;
            y = orig.height;
        }

        void reset() {
            x = -1;
            y = -1;
        }

        void setInMinZero() {
            x = (x < 0 ? 0 : x);
            y = (y < 0 ? 0 : y);
        }
        void setExMax(Dim max) {
            x = (x >= max.x ? max.x - 1 : x);
            y = (y >= max.y ? max.y - 1 : y);
        }
        void setExMax(int maxx, int maxy) {
            x = (x >= maxx ? maxx - 1 : x);
            y = (y >= maxy ? maxy - 1 : y);
        }
    }

    final class Rect {
        Dim fm = new Dim();
        Dim to = new Dim();

        void set(Rect orig) {
            if (orig.fm.x <= orig.to.x) {
                fm.x = orig.fm.x;
                to.x = orig.to.x;
            } else {
                to.x = orig.fm.x;
                fm.x = orig.to.x;
            }
            if (orig.fm.y <= orig.to.y) {
                fm.y = orig.fm.y;
                to.y = orig.to.y;
            } else {
                to.y = orig.fm.y;
                fm.y = orig.to.y;
            }
        }
        void set(Rectangle orig) {
            fm.x = orig.x;
            fm.y = orig.y;
            to.x = orig.x + orig.width;
            to.y = orig.y + orig.height;
        }

        void reset() {
            fm.reset();
            to.reset();
        }

        void union(Dim dim) {
            if (fm.x > dim.x)
                fm.x = dim.x;
            if (fm.y > dim.y)
                fm.y = dim.y;
            if (to.x < dim.x)
                to.x = dim.x;
            if (to.y < dim.y)
                to.y = dim.y;
        }

        void setInMinZero() {
            fm.setInMinZero();
            to.setInMinZero();
        }
        void setExMax(Dim max) {
            fm.setExMax(max);
            to.setExMax(max);
        }
        void setExMax(int maxx, int maxy) {
            fm.setExMax(maxx, maxy);
            to.setExMax(maxx, maxy);
        }
    }

    /* ****** Fonts ****** */

    final FontInfo font = new FontInfo();
    final class FontInfo {
        boolean init = false;

        String name = "Courier New";
        Font face[] = new Font[4];

        int size = -1, width, height, ascent, descent;

        void installFont(int style) {
            final int fStyle[] =
                { Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD + Font.ITALIC };
            final String fName[] =
                { "Plain ", "Bold ", "Italic ", "Bold Italic " };
            try {
                face[style] = new Font(name, fStyle[style], size);
            } catch (Exception e) {
                //System.out.println(
                    //"telnet: Courier " + fName + size + " Not Available!");
            }
        }

        void install() {
            if (init == false) {
                init = true;
                Graphics g = view.getGraphics();
                Font f = g.getFont();
                if (size < 6)
                    size = f.getSize();
                face[0] = face[1] = face[2] = face[3] = f;

                installFont(Style.PLAIN);
                size = face[Style.PLAIN].getSize();
                installFont(Style.ITALIC);
                installFont(Style.BOLD);
                installFont(Style.BOLD | Style.ITALIC);

                FontMetrics m = g.getFontMetrics(face[0]);
                font.ascent = m.getAscent();
                font.descent = m.getDescent();
                font.width = m.charWidth('W');
                font.height = ascent + descent;

                Dimension s = new Dimension(font.width * DEFAULT.WIDTH /*+1*/
                , font.height * DEFAULT.HEIGHT);
                view.setPreferredSize(s);
            }
        }
    }

    /* ****** Row ****** */

    final public class Row {
        private char chars[];
        private short formats[];
        private int col;
        private boolean valid = true;

        Row(int n, Row o) {
            chars = new char[n];
            formats = new short[n];
            for (int i = 0; i < (o != null ? o.chars.length : 0); i++) {
                chars[i] = o.chars[i];
                formats[i] = o.formats[i];
            }
            for (int i = (o != null ? o.chars.length : 0);
                i < chars.length;
                i++) {
                chars[i] = ' ';
                formats[i] = DEFAULT.FORMAT;
            }
            int col = 0;
        }

        boolean isValid() {
            boolean valid = this.valid;
            this.valid = false;
            return valid;
        }

        short getFormat(int col) {
            return formats[col];
        }
        void setFormat(short format) {
            formats[col] = (short) (format & 0xFF0F);
            valid = false;
        }

        short getStyle() {
            return (short) (formats[col] & 0xF);
        }
        void setStyle(short style) {
            formats[col] = (short) ((formats[col] & 0xFF00) | (style & 0xF));
        }

        Font getFont() {
            return font.face[formats[col] & (Style.BOLD | Style.ITALIC)];
        }
        Color getForeground() {
            return COLORS[(formats[col] >> ((formats[col] & Style.INVERSE) == Style.INVERSE ? 12 : 8)) & 0xF];
        }
        Color getBackground() {
            return COLORS[(formats[col] >> ((formats[col] & Style.INVERSE) == Style.INVERSE ? 8 : 12)) & 0xF];
        }
        boolean isDefBackground() {
            return (((formats[col] >> ((formats[col] & Style.INVERSE) == Style.INVERSE ? 8 : 12)) & 0xF) == DEFAULT.BACK);
        }
        boolean isInvBackground() {
            return (((formats[col] >> ((formats[col] & Style.INVERSE) == Style.INVERSE ? 8 : 12)) & 0xF) == DEFAULT.TEXT);
        }

        char getChar() {
            return chars[col];
        }
        char getChar(int col) {
            return chars[col];
        }
        void setChar(char ch) {
            chars[col] = ch;
            valid = false;
        }

        void setCol(int col) {
            this.col = col;
        }

        public char[] getChars() {
            return chars;
        }
        int length() {
            return chars.length;
        }
    }

    /* ****** Buffer ****** */

    public void setStyle(short style) {
        buffer.format = (short) ((buffer.format & 0xFF00) | (style & 0xF));
    }
    public void setForeground(short color) {
        buffer.format =
            (short) ((buffer.format & 0xF00F) | ((color & 0xF) << 8));
    }
    public void setBackground(short color) {
        buffer.format =
            (short) ((buffer.format & 0x0F0F) | ((color & 0xF) << 12));
    }

    final public Buffer buffer = new Buffer();
    final public class Buffer {
        private boolean init = false;

        Row rows[] = null;
        boolean tabs[];
        int width = -1;
        int offset = 0;

        final Dim curr = new Dim();
        short format = 0;

        public Row getRow(int rely) {
            return rows[rely + offset];
        }

        void install() {
            if (!init) {
                rows = new Row[DEFAULT.BUFFER];
                format = DEFAULT.FORMAT;
                init = true;
                curr.x = 0;
                curr.y = 0;
                resize(DEFAULT.WIDTH);
            }
        }

        int resize(int x) {
            if (tabs != null && x > tabs.length)
                x = tabs.length;
            if (x > width) {
                for (int i = 0; i < rows.length; i++)
                    rows[i] = new Row(x, rows[i]);
                width = x;
            }
            return x;
        }

        void roll(int n) { //roll(n,0,rows.length);
            for (int i = 0; i < (rows.length - n); i++)
                rows[i] = rows[i + n];
            for (int i = (rows.length - n); i < rows.length; i++)
                rows[i] = new Row(width, null);
            edit.page(n);
        }

        void roll(int n, int fmy, int toy) {
            if (n < 0) {
                n = -n;
                for (int i = fmy; i < (toy - n); i++)
                    rows[i] = rows[i + n];
                for (int i = (toy - n); i < toy; i++)
                    rows[i] = new Row(width, null);
            } else {
                for (int i = toy; i >= (fmy + n); i--)
                    rows[i] = rows[i - n];
                for (int i = fmy; i < (fmy + n); i++)
                    rows[i] = new Row(width, null);
            }
        }

        void page(int n) {
            if (n < 0)
                n = -n;
            if (n > 22)
                n = DEFAULT.WIDTH;
            int x = rows.length - offset - DEFAULT.HEIGHT;
            if (x < n) {
                offset += x;
                view.page(x);
                roll(n - x);
            } else {
                offset += n;
                view.page(n);
            }
            view.repaint();
        }

        void feed() {
            page(1);
        }

        void set(char ch, short format) {
            rows[curr.y].setCol(curr.x);
            rows[curr.y].setFormat(format);
            rows[curr.y].setChar(ch);
            view.repaintCell(curr);
        }

        void put(char ch) {
            set(tran.code(ch), format);
            if (curr.x < (width - 1))
                curr.x++;
            //else
            //if(mode!=null && mode.states[VTMode.DECAWM])
            //{ cr(); lf(); }
        }

        void cr() {
            curr.x = 0;
        }

        void lf() {
            curr.y++;
            if (curr.y >= rows.length) {
                curr.y = rows.length - 1;
                feed();
            } else {
                if (curr.y - offset >= DEFAULT.HEIGHT)
                    offset++;
                scroll.relimit(curr.y);
            }
        }

        void bs() {
            if (curr.x > 0) {
                curr.x--;
                /*set(' ',DEFAULT.FORMAT);*/
            }
        }

        void ht() {
            if (curr.x < view.size.x)
                curr.x++;
            while (curr.x < view.size.x && !tabs[curr.x])
                curr.x++;
        }
    }

    /* ****** Border ****** */

    final private JBorder border = new JBorder();
    final class JBorder extends JPanel {
        public void paintBorder(Graphics g) {
          //  super.paintBorder(g);
            g.setColor(COLORS[(view.hasFocus() ? DEFAULT.TEXT : DEFAULT.BACK)]);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
        protected void paintComponent(Graphics g) {
        }
    }

    /* ****** View ****** */

    final private JView view = new JView();
    final class JView extends JPanel {
        private boolean init = false;

        final Dim size = new Dim();
        int offset = 0;

        /* Receives focus */
        public boolean isFocusTraversable() {
            return true;
        }

        public boolean isManagingFocus() {
            return true;
        }

        /* Setup & Destroy */
        public void addNotify() {
            super.addNotify();
            font.install();
            scroll.install();
            view.install();
            caret.install();
            edit.install();
        }
        public void removeNotify() {
            caret.uninstall();
            super.removeNotify();
        }

        Row getRow(int rely) {
            return buffer.rows[rely + offset];
        }

        int toPxX(int x) {
            return x * font.width;
        }
        int toPxY(int y) {
            return y * font.height;
        }

        void toCh(Dim dim) {
            dim.x /= font.width;
            dim.y /= font.height;
        }
        void toCh(Rect rect) {
            toCh(rect.fm);
            rect.to.x--;
            rect.to.y--;
            toCh(rect.to);
        }

        int setInMinZero(int i) {
            return (i < 0 ? 0 : i);
        }
        int setExMax(int i, int max) {
            return (i >= max ? max - 1 : i);
        }

        void install() {
            if (!init) {
                init = true;
                addComponentListener(new ComponentAdapter() {
                    public void componentResized(ComponentEvent e) {
                        resize();
                    }
                    public void componentShown(ComponentEvent e) {
                        resize();
                    }
                });
            }
        }

        void resize() {
            Dim pend = new Dim();
            pend.set(getSize());
            toCh(pend);

            /* Width */
            size.x = buffer.resize(pend.x);

            /* Height */
            if (pend.y < size.y) {
                if (offset + size.y == buffer.rows.length)
                    offset += size.y - pend.y;
                // whole screen is invalid;
            } else if (pend.y > size.y)
                if (offset + pend.y > buffer.rows.length)
                    offset = buffer.rows.length - pend.y;
            // whole screen is invalid;
            size.y = pend.y;

            scroll.rescale();
        }

        void page(int n) {
            if (buffer.rows.length + offset + size.y < n)
                offset = buffer.rows.length - size.y;
            else
                offset += n;
            scroll.setValue(offset);
        }

        //void page(int n) {
        //    if(n<=0)
        //        n = size.y;
        //    int x = buffer.rows.length -offset -size.y;
        //    if(x>0 && x<n) {
        //        offset+=x;
        //        if(buffer.rows.length-DEFAULT.HEIGHT<offset)
        //            buffer.offset = buffer.rows.length-DEFAULT.HEIGHT;
        //        else buffer.offset = offset;
        //        buffer.page(n-x);
        //    } else {
        //        buffer.page(n);
        //        repaint(); }
        //    scroll.setValue(offset);
        //}

        //void feed()
        //{   page(1); }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            /* Paint Clip */
            Rect clip = new Rect();
            Rectangle bounds = g.getClipBounds();
            if (bounds != null) {
                //System.out.println("paint bnd:"+bounds.x+","+bounds.y+":"+bounds.width+","+bounds.height);
                clip.set(bounds);
                toCh(clip);
                clip.setInMinZero();
                clip.setExMax(size);
            }

                /* Type Text */
            for (int y = clip.fm.y; y <= clip.to.y; y++)
                for (int x = clip.fm.x; x <= clip.to.x; x++) {
                    Row r = getRow(y);
                    r.setCol(x);
                    boolean sel = edit.isSelectedCell(x, y + offset);

                    /* Fill Background */
                    // FILTER IS POSSIBLE IF super.paintComponent(g); WAS CALLED
                    if (sel || !r.isDefBackground()) {
                        g.setColor((sel ? COLORS[DEFAULT.TEXT] : r.getBackground()));
                        g.fillRect(toPxX(x), toPxY(y), font.width, font.height);
                    }

                    /* Type Character */
                    g.setFont(r.getFont());
                    g.setColor((sel ? COLORS[DEFAULT.BACK] : r.getForeground()));
                    g.drawChars(r.getChars(), x, 1, toPxX(x), toPxY(y) + font.ascent);

                    /* Draw Underline */
                    if ((r.getStyle() & Style.UNDERLINE) == Style.UNDERLINE)
                        g.drawLine( toPxX(x), toPxY(y) + font.ascent,
                            toPxX(x + 1) - 1, toPxY(y) + font.ascent);
                }
        }

        /** is visible cell buffer-coordinates */
        boolean isVisibleCell(Dim abs) {
            return isVisibleCell(abs.x, abs.y - offset);
        }

        /** is visible cell view-coordinates */
        boolean isVisibleCell(int relx, int rely) {
            return (relx >= 0 && relx < size.x && rely >= 0 && rely < size.y);
        }

        /** is visible rect buffer-coordinates */
        boolean isVisibleRect(Rect abs) {
            return isVisibleRect(abs.fm.x, abs.fm.y - offset, abs.to.x, abs.to.y - offset);
        }

        /** is visible rect view-coordinates */
        boolean isVisibleRect(int relfmx, int relfmy, int reltox, int reltoy) {
            return (relfmx >= 0 && reltox < size.x && reltoy >= 0 && relfmy < size.x);
        }

        /** repaint cell buffer-coordinates */
        void repaintCell(Dim abs) {
            if (isVisibleCell(abs))
                repaintCell(abs.x, abs.y - offset);
        }

        /** repaint cell view-coordinates */
        void repaintCell(int relx, int rely) {
            if (isVisibleCell(relx, rely))
                repaint(new Rectangle(toPxX(relx), toPxY(rely), font.width, font.height));
        }

        /** repaint rect buffer-coordinates */
        void repaintLine(int absy) {
            repaintRect(0, absy - offset, size.x - 1, absy - offset);
        }

        /** repaint rect buffer-coordinates */
        void repaintLine(int absfmx, int abstox, int absy) {
            repaintRect(absfmx, absy - offset, abstox, absy - offset);
        }

        /** repaint rect buffer-coordinates */
        void repaintLines(int absfmy, int abstoy) {
            repaintRect(0, absfmy - offset, size.x - 1, abstoy - offset);
        }

        /** repaint rect buffer-coordinates */
        void repaintRect(Rect abs) {
            if (isVisibleRect(abs))
                repaintRect(abs.fm.x, abs.fm.y - offset, abs.to.x, abs.to.y - offset);
        }

        /** repaint rect view-coordinates */
        void repaintRect(int relfmx, int relfmy, int reltox, int reltoy) {
            if (isVisibleRect(relfmx, relfmy, reltox, reltoy))
                repaint(new Rectangle(
                    toPxX((relfmx >= 0 ? relfmx : 0)), toPxY((relfmy >= 0 ? relfmy : 0)),
                    toPxX((reltox - relfmx + 1 < size.x ? reltox - relfmx + 1 : size.x - 1)),
                    toPxY((reltoy - relfmy + 1 <= size.y ? reltoy - relfmy + 1 : size.y - 1))));
        }
    }

    /* ****** Scroll ****** */

    final private JScroll scroll = new JScroll();
    final class JScroll extends JScrollBar {
        private boolean init = false;

        private boolean block = false;
        private int rely = 0;

        void install() {
            if (!init) {
                init = true;
                addAdjustmentListener(new AdjustmentListener() {
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        reoffset();
                    }
                });
            }
        }

        JScroll() {
            super(JScrollBar.VERTICAL);
        }

        void rescale() {
            setBlocked(true);
            if (view.offset < getValue())
                setValue(view.offset);

            setVisibleAmount(view.size.y);
            setBlockIncrement(view.size.y - 1);
            setUnitIncrement(1);
            setMaximum(buffer.rows.length);
            setMinimum(0);

            setBlocked(false);
            if (view.offset > getValue())
                setValue(view.offset);
        }

        void reoffset() {
            int tempoffset = getValue();
            if (!isBlocked() && tempoffset != view.offset) {
                //int pendrely = tempoffset-view.offset;
                view.offset = tempoffset;
                view.repaint();
            }
        }

        void relimit(int absy) {
            if (absy == view.offset + view.size.y) {
                view.offset++;
                setValue(view.offset);
                view.repaint();
            }
        }

        synchronized void setBlocked(boolean block) {
            this.block = block;
        }
        synchronized boolean isBlocked() {
            return block;
        }
    }

    /* ****** Caret ****** */

    final private Caret caret = new Caret();
    final class Caret {
        private boolean init = false;

        void install() {
            if (!init) {
                init = true;
                view.addFocusListener(new FocusAdapter() {
                    public void focusGained(FocusEvent e) {
                        border.paintBorder(border.getGraphics());
                        setBlinkRate(500);
                    }
                    public void focusLost(FocusEvent e) {
                       // border.paintBorder(border.getGraphics());
                        setBlinkRate(0);
                    }
                });
            }
        }

        void uninstall() {
            setBlinkRate(0);
        }

        Timer flasher = null;
        public void setBlinkRate(int rate) {
            if (rate != 0) {
                if (flasher == null) {
                    flasher = new Timer(rate, flashEvent);
                    flasher.start();
                }
                flasher.setDelay(rate);
            } else if (flasher != null) {
                flasher.stop();
                flasher.removeActionListener(flashEvent);
                flasher = null;
            }
        }

        ActionListener flashEvent = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (mark.x < 0)
                    paint(buffer.curr);
                else
                    damage();
            }
        };

        public void paint(Dim abs) {
            if (view.isVisibleCell(abs)) {
                Graphics g = view.getGraphics();
                Row r = buffer.rows[abs.y];
                r.setCol(abs.x);
                g.setColor(COLORS[(r.isInvBackground() ? DEFAULT.BACK : DEFAULT.TEXT)]);
                g.fillRect(view.toPxX(abs.x), view.toPxY(abs.y - view.offset) + font.ascent, font.width - 1, font.descent - 1);
                mark.x = abs.x;
                mark.y = abs.y - view.offset;
            }
        }

        Dim mark = new Dim();
        public void damage() {
            if (mark.x >= 0) {
                view.repaintCell(mark.x, mark.y);
                mark.reset();
            }
        }
    }

    /* ****** Edit ****** */

    final private Edit edit = new Edit();
    final class Edit {
        private boolean init = false;

        private Rect drag = new Rect();
        private Rect fill = new Rect();
        private Rect clip = new Rect();

        void install() {
            if (!init) {
                init = true;
                view.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) == MouseEvent.BUTTON3_MASK) {
                            clicked();
                        }
                    }
                    public void mousePressed(MouseEvent e) {
                        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK) {
                            pressed(e.getX(), e.getY());
                        }
                    }
                    public void mouseReleased(MouseEvent e) {
                        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK) {
                            released(e.getX(), e.getY());
                        }
                    }
                });
                view.addMouseMotionListener(new MouseMotionAdapter() {
                    public void mouseDragged(MouseEvent e) {
                        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK) {
                            dragged(e.getX(), e.getY());
                        }
                    }
                });
            }
        }

        void page(int n) { /* Page Drag */
            if (drag.fm.y >= 0) {
                drag.fm.y -= n;
                //drag.to.y-=n;
                drag.setInMinZero();
                drag.setExMax(buffer.width, buffer.rows.length);
                /* Paint Fill */
                fill.set(drag);
                fill.setInMinZero();
                fill.setExMax(buffer.width, buffer.rows.length);
            } else
                /* Page Fill */
                if (fill.fm.y >= 0 && fill.to.y >= n) {
                    fill.fm.y -= n;
                    fill.to.y -= n;
                    fill.setInMinZero();
                    fill.setExMax(buffer.width, buffer.rows.length);
                } else
                    fill.reset();
            /* Null Clip */
            clip.reset();
        }

        void clicked() {
            final String s = paste();
            (new Thread(new Runnable() {
                public void run() {
                    for (int i = 0; i < s.length(); i++) {
                        send(s.charAt(i));
                        if (s.charAt(i) == '\n')
                            try {
                                synchronized (this) {
                                    wait(100);
                                }
                            } catch (InterruptedException e) { }
                    }
                }
            })).start();
        }

        void pressed(int relx, int rely) { /* Paint Clip */
            if (fill.fm.x >= 0)
                clip.set(fill);
            else
                clip.reset();
            /* Setup Drag */
            drag.fm.x = drag.to.x = relx;
            drag.fm.y = drag.to.y = rely;
            view.toCh(drag);
            drag.fm.y += view.offset;
            drag.to.y += view.offset;
            drag.setInMinZero();
            drag.setExMax(buffer.width, buffer.rows.length);
            /* Paint Fill */
            fill.set(drag);
            fill.setInMinZero();
            fill.setExMax(buffer.width, buffer.rows.length);
            /* Paint Clip */
            if (clip.fm.x >= 0) {
                clip.setInMinZero();
                clip.setExMax(buffer.width, buffer.rows.length);
                view.repaintRect(clip);
            }
            view.repaintCell(drag.fm);
        }

        void released(int relx, int rely) {
            dragged(relx, rely);
            drag.reset();
            StringBuffer b =
                new StringBuffer((fill.to.y - fill.fm.y + 1) * (fill.to.x - fill.fm.x + 2));
            for (int i = fill.fm.y; i <= fill.to.y; i++) {
                b.append( buffer.rows[i].getChars(), fill.fm.x, fill.to.x - fill.fm.x + 1);
                if (i < fill.to.y)
                    b.append('\n');
            }
            copy(b.toString());
        }

        void dragged(int relx, int rely) {
            /* Paint Clip */
            clip.set(drag);
            /* Update Drag */
            drag.to.x = relx;
            drag.to.y = rely;
            view.toCh(drag.to);
            drag.to.y += view.offset;
            drag.setInMinZero();
            drag.setExMax(buffer.width, buffer.rows.length);
            /* Paint Clip */
            clip.union(drag.to);
            clip.setInMinZero();
            clip.setExMax(buffer.width, buffer.rows.length);
            /* Paint Fill */
            fill.set(drag);
            fill.setInMinZero();
            fill.setExMax(buffer.width, buffer.rows.length);

            view.repaintRect(clip);
        }

        /** Writes data string into the system clipboard. */
        void copy(String data) {
            try {
                Clipboard clipboard = getToolkit().getSystemClipboard();
                StringSelection contents = new StringSelection(data);
                clipboard.setContents(contents, contents);
            } catch (Exception e) {
                getToolkit().beep();
            }
        }

        /** Reads the contents of the system clipboard into string. */
        String paste() {
            try {
                Clipboard clipboard = getToolkit().getSystemClipboard();
                Transferable content = clipboard.getContents(this);
                return (String)
                    (content.getTransferData(DataFlavor.stringFlavor));
            } catch (Exception e) {
                getToolkit().beep();
                return "";
            }
        }

        boolean isSelectedCell(int absx, int absy) {
            return (   absx >= fill.fm.x
                    && absx <= fill.to.x
                    && absy >= fill.fm.y
                    && absy <= fill.to.y);
        }
    }
    /** <P>Creates a new MultiLabel instance.</P> */
    public JTerminal() {
        this(new JTerminal.Default());
    }

    /** <P>Creates a new MultiLabel instance.</P> */
    public JTerminal(Default DEFAULT) {
        this.DEFAULT = DEFAULT;
        buffer.install();
        /* Structure */
        setOpaque(true);
        setLayout(new BorderLayout(3, 3));
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        add(border, BorderLayout.CENTER);
        border.setLayout(new BorderLayout());
        border.setBorder( BorderFactory.createLineBorder(
            COLORS[DEFAULT.BACK], DEFAULT.BORDER));
        border.setBackground(COLORS[DEFAULT.BACK]);
        border.setOpaque(true);
        border.add(view, BorderLayout.CENTER);
        view.setBackground(COLORS[DEFAULT.BACK]);
        view.setOpaque(true);
        add(scroll, BorderLayout.EAST);

        area.fm.setInMinZero();
        area.to.set(DEFAULT.WIDTH - 1, DEFAULT.HEIGHT - 1);

    }

    //public boolean isValidateRoot() { return true; }
    //public void paintComponent(Graphics g) { }

    /** Terminal Emulator */
    private AEmulator emu;
    /** Keyboard Mapper */
    private AMapper map;
    /* private */
    ATranslator tran = new VTTranslator();
    private VTMode mode = null;

    public void setEmulator(AEmulator emu) {
        this.emu = emu;
        if (this.map != null) {
            view.removeKeyListener(this.map);
        }
        this.map = emu.map;
        this.tran = emu.tran;
        this.mode = emu.mode;
        buffer.tabs = emu.tabs;
        if (emu != null) {
            emu.term = this;
            emu.setWindowSize(view.size.x, view.size.y);
        }
        if (map != null) {
            view.addKeyListener(map);
        }
    }

    public void CR() {
        buffer.cr();
    }
    public void LF() {
        buffer.lf();
    }
    public void BS() {
        buffer.bs();
    }
    public void HT() {
        buffer.ht();
    }

    public void receive(char ch) {
        buffer.put(ch);
    }
    public void receive(char ch, int format) {
        buffer.put(ch);
    }

    public void send(char ch) {
        emu.send(ch);
    }
    public void send(byte[] b) {
        emu.send(b);
    }
    public void send(byte[] b, int off, int len) {
        emu.send(b, off, len);
    }
    public void send(String str) {
        emu.send(str);
    }

    /////////////////////////////////////////////////////

    public void designateCharSet(char buf, char set) {
        tran.designate(buf, set);
    }
    public void invokeCharSet(char code, char buf) {
        tran.invoke(code, buf);
    }

    final public int getNumRows() {
        return DEFAULT.HEIGHT;
    }
    final public int getNumCols() {
        return DEFAULT.WIDTH;
    }

    final public int getCursorRow() {
        return buffer.curr.y - buffer.offset;
    }
    final public int getCursorCol() {
        return buffer.curr.x;
    }

    final public void setCursorPos(
        int row,
        int col) { //System.out.println("setCursorPos"+row+","+col);
        Dim dim = new Dim();
        dim.set(col, row);

        dim.setInMinZero();
        dim.setExMax(DEFAULT.WIDTH, DEFAULT.HEIGHT);

        buffer.curr.set(dim.x, dim.y + buffer.offset);
    }

    final public void setCursorVisible(boolean state) {
    }
    final public boolean getCursorVisible() {
        return true;
    }

    Rect area = new Rect();

    final public synchronized int getAreaTop() {
        return area.fm.y;
    }
    final public synchronized int getAreaBottom() {
        return area.to.y;
    }
    final public synchronized int getAreaLeft() {
        return area.fm.x;
    }
    final public synchronized int getAreaRight() {
        return area.to.x;
    }

    final public synchronized void resetArea() {
        area.fm.set(0, 0);
        area.to.set(DEFAULT.WIDTH - 1, DEFAULT.HEIGHT - 1);
    }

    final public synchronized void setArea(int top, int bottom) {
        setArea(top, bottom, 0, view.size.x - 1);
    }

    final public synchronized void setArea(int top, int bottom, int left, int right) {
        area.fm.set(left, top);
        area.to.set(right, bottom);
        area.setInMinZero();
        area.setExMax(DEFAULT.WIDTH - 1, DEFAULT.HEIGHT - 1);
        if (area.fm.y >= area.to.y - 2) {
            if (area.fm.y > 2)
                area.fm.y = 0;
            else
                area.to.y = DEFAULT.HEIGHT - 1;
        }
        if (area.fm.x >= area.to.x - 2) {
            if (area.fm.x > 2)
                area.fm.x = 0;
            else
                area.to.x = DEFAULT.WIDTH - 1;
        }
        // System.out.println("setArea" + area.fm.x + "," + area.fm.y + "|" + area.to.x + "," + area.to.y);
    }

    public short getStyle() {
        return (short) (buffer.format & 0xF);
    }
    public void setStyle(int style) {
        buffer.format = (short) ((buffer.format & 0xFF00) | (style & 0xF));
    }

    public short getForeColor() {
        return (short) ((buffer.format & 0x0F00) >> 8);
    }
    public short getBackColor() {
        return (short) ((buffer.format & 0xF000) >> 12);
    }
    public void setForeColor(int color) { /* buffer.format = (short)((buffer.format& 0xF00F) | ((color& 0xF) <<8)); */
    }
    public void setBackColor(int color) { /* buffer.format = (short)((buffer.format& 0x0F0F) | ((color& 0xF) <<12)); */
    }

    public synchronized void setChars(int left, int right, char ch, int style) {
       // System.out.println("setChars" + left + "," + right);
        if (left < area.fm.x)
            left = area.fm.x;
        if (right > area.to.x)
            right = area.to.x;
        Row r = buffer.rows[buffer.curr.y];
        while (left <= right) {
            r.setCol(left);
            r.setChar(ch);
            r.setStyle((short) style);
            left++;
        }
        view.repaintLine(buffer.curr.y);
    }

    public synchronized void delChars(int left, int right, char ch, int style) {
        //System.out.println("delChars" + left + "," + right);
        if (left < area.fm.x)
            left = area.fm.x;
        if (right > area.to.x)
            right = area.to.x;
        int over = right + 1;
        Row r = buffer.rows[buffer.curr.y];
        while (over <= area.to.x && left <= area.to.x) {
            r.setCol(left);
            r.setChar(r.getChar(over));
            r.setFormat(r.getFormat(over));
            left++;
            over++;
        }
        r = buffer.rows[buffer.curr.y];
        while (left <= area.to.x) {
            r.setCol(left);
            r.setChar(ch);
            r.setStyle((short) style);
            left++;
        }
        view.repaintLine(buffer.curr.y);
    }

    public synchronized void insChars(int left, int right, char ch, int style) {
       // System.out.println("insChars" + left + "," + right);
        if (left < area.fm.x)
            left = area.fm.x;
        if (right > area.to.x)
            right = area.to.x;
        if (left == right)
            return;
        int inner = area.to.x - (right - left);
        right = area.to.x;
        Row r = buffer.rows[buffer.curr.y];
        while (inner >= left && right >= left) {
            r.setCol(right);
            r.setChar(r.getChar(inner));
            r.setFormat(r.getFormat(inner));
            inner--;
            right--;
        }
        r = buffer.rows[buffer.curr.y];
        while (right >= left) {
            r.setCol(right);
            r.setChar(ch);
            r.setStyle((short) style);
            right--;
        }
        view.repaintLine(buffer.curr.y);
    }

    public synchronized void clearEOL(char ch, int style) {
       // System.out.println("clearEOL");
        Row r = buffer.rows[buffer.curr.y];
        for (int i = buffer.curr.x; i <= area.to.x; i++) {
            r.setCol(i);
            r.setChar(ch);
            r.setStyle((short) style);
        }
        view.repaintLine(buffer.curr.x, area.to.x, buffer.curr.y);
    }

    public synchronized void clearBOL(char ch, int style) {
        //System.out.println("clearBOL");
        Row r = buffer.rows[buffer.curr.y];
        for (int i = area.fm.x; i <= buffer.curr.x; i++) {
            r.setCol(i);
            r.setChar(ch);
            r.setStyle((short) style);
        }
        view.repaintLine(area.fm.x, buffer.curr.x, buffer.curr.y);
    }

    public synchronized void clearLine(char ch, int style) {
        //System.out.println("clearLine");
        Row r = buffer.rows[buffer.curr.y];
        for (int i = area.fm.x; i <= area.to.x; i++) {
            r.setCol(i);
            r.setChar(ch);
            r.setStyle((short) style);
        }
        view.repaintLine(buffer.curr.y);
    }

    public synchronized void clearEOD(char ch, int style) {
        //System.out.println("clearEOD");
        int left = buffer.curr.x;
        for (int j = buffer.curr.y - buffer.offset; j <= area.to.y; j++) {
            Row r = buffer.getRow(j);
            for (int i = left; i <= area.to.x; i++) {
                r.setCol(i);
                r.setChar(ch);
                r.setStyle((short) style);
            }
            left = area.fm.x;
        }
        view.repaintLines(buffer.curr.y, area.to.y + buffer.offset);
    }

    public synchronized void clearBOD(char ch, int style) {
        //System.out.println("clearBOD");
        for (int j = area.fm.y; j < buffer.curr.y - buffer.offset; j++) {
            Row r = buffer.getRow(j);
            for (int i = area.fm.x; i <= area.to.x; i++) {
                r.setCol(i);
                r.setChar(ch);
                r.setStyle((short) style);
            }
        }
        Row r = buffer.rows[buffer.curr.y];
        for (int i = area.fm.x; i < buffer.curr.x; i++) {
            r.setCol(i);
            r.setChar(ch);
            r.setStyle((short) style);
        }
        view.repaintLines(area.fm.y + buffer.offset, buffer.curr.y);
    }

    public synchronized void clearAll(char ch, int style) {
        //System.out.println("clearAll");
        for (int j = area.fm.y; j <= area.to.y; j++) {
            Row r = buffer.getRow(j);
            for (int i = area.fm.x; i <= area.to.x; i++) {
                r.setCol(i);
                r.setChar(ch);
                r.setStyle((short) style);
            }
        }
        view.repaintLines(area.fm.y + buffer.offset, area.to.y + buffer.offset);
    }

    public synchronized void rollRegion(int top, int bottom, int amount) {
       // System.out.println("page area" + amount);
        buffer.roll(amount, top + buffer.offset, bottom + buffer.offset);
        view.repaintLines(top + buffer.offset, bottom + buffer.offset);
    }

    public synchronized void rollScreen(int amount) {
        //System.out.println("page" + amount);
        buffer.page(amount);
        buffer.curr.set(0, buffer.offset);
    }

    /** Terminal OUTput Flag Don't interpret \n \b etc.? */
    public static final int OUTF_RAW = 1;
    /** Terminal OUTput Flag Don't update screen? */
    public static final int OUTF_PARTIAL = 1 << 1;
}
