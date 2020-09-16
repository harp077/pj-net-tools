/*
 * Visit url for update: http://sourceforge.net/projects/jvtelnet
 *
 * JvTelnet was developed by Bea Petrovicova <beapetrovicova@yahoo.com>.
 * The sources was donated to sourceforge.net under the terms
 * of GNU General Public License (GPL). Redistribution of any
 * part of JvTelnet or any derivative works must include this notice.
 */
package nnm.inet.telnet;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RepaintManager;
import java.util.StringTokenizer;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.awt.Image;
//import nnm.NetworkManagerGUI;

/**
 * @version 0.23 06/09/2003
 * @author Bea Petrovicova <beapetrovicova@yahoo.com>
 */
public class JTerm {
    static AEmulator emu;
    static JTerminal term;
    static Telnet telnet;
    static String server = null;
    static int port = 23;

    public static void telnet(String server) { // String[] args
        emu = new VTEmulator(new VTMapper(), new VTTranslator());
        term = new JTerminal();
        term.setEmulator(emu);
        telnet = new Telnet(emu);

        final JFrame frame = new JFrame("Telnet-client");
        frame.setSize(200,200);
        //NetworkManagerGUI.imageURL = NetworkManagerGUI.class.getResource("icons/nw.gif");

        //ImageIcon icon = new ImageIcon(NetworkManagerGUI.imageURL );
        //Image image = icon.getImage();
        //frame.setIconImage(image);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension optSize = frame.getSize();
        if (optSize.height > screenSize.height) {
           optSize.height = screenSize.height;
        }
        if (optSize.width > screenSize.width) {
           optSize.width = screenSize.width;
          }
        frame.setLocation( (screenSize.width - optSize.width)/2-optSize.width,
                            (screenSize.height - optSize.height)/2-optSize.height);

        frame.setContentPane(term);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                //System.exit(0);
            }
        });

   //me    parse(args);

        frame.pack();
        frame.setVisible(true);

        /*if(server==null) {
            server = JOptionPane.showInputDialog(frame, "Connect To: ");
        }*/
        try {
            telnet.connect(server, port);
        } catch (Exception e) {
            frame.dispose();
            JOptionPane.showMessageDialog(frame, "Can't connect", "Connect error", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void parse(String[] args) {
        for (int i = 0; i < args.length; i++)
            if (args[i].equals("-a") || args[i].equals("--answerback")) {
                // answerback message is next argument
                if (++i < args.length)
                    emu.answerback = args[i];
            } else if (args[i].equals("-b") || args[i].equals("--baudrate")) {
                // baudrate is next argument
                if (++i < args.length)
                    try {
                        emu.baudrate = Integer.parseInt(args[i]);
                    } catch (Exception e) {}
            } else if (args[i].equals("-s") || args[i].equals("--sockport")) {
                // socket port is next argument
                if (++i < args.length)
                    try {
                        port = Integer.parseInt(args[i]);
                    } catch (Exception e) {}
            } else if (args[i].equals("-c") || args[i].equals("--commport")) {
                // commport is next argument
                if (++i < args.length)
                    emu.commport = args[i];
            } else if (args[i].equals("-k") || args[i].equals("--flowcontrol")) {
                // flowcontrol is next argument
                if (++i < args.length) {
                    if (args[i].equals("none") || args[i].equals("hw") || args[i].equals("sw")) {
                        emu.flowcontrol = args[i];
                    }
                }
            } else if (args[i].equals("-d") || args[i].equals("--debugon")) {
                // debug mode on
                telnet.debug(new File("jterm-" + emu.answerback + ".out"));
                try {
                    System.setOut(new PrintStream(new FileOutputStream(
                        "jterm-" + emu.answerback + ".out", true), true));
                } catch (Exception e) {
                }
            } else if (args[i].equals("-l") || args[i].equals("--loopconnect")) {
                // reconnect in loop
                telnet.loop(true);
            } else if (args[i].equals("-e") || args[i].equals("--unicodeoff")) {
                // do not use extended unicode characters
                term.tran = new ATranslator();
            } else if (args[i].equals("-f") || args[i].equals("--fontname")) {
                // fontname is next argument
                if (++i < args.length)
                    term.font.name = args[i];
            } else if (args[i].equals("-i") || args[i].equals("--config")) {
                // config file is next argument
                if (++i < args.length)
                    parse(args[i]);
            } else if (args[i].equals("-r") || args[i].equals("--doublebuffer")) {
                // turn of screen doublebuffer - might help on graphics terminals
                RepaintManager.currentManager(null).setDoubleBufferingEnabled(false);
                //scroller.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
            } else if (args[i].equals("-h") || args[i].equals("--help")) {
                // this help screen
               /* System.out.print(
                          "java -jar telnet.jar [options] telnet.server.com \n"
                        + "\n --sockport     -s  socket port is next argument \n"
                        + "\n Other \n"
                        + " --loopconnect  -l  reconnect in loop \n"
                        + " --debugon      -d  debug mode on \n"
                        + " --unicodeoff   -e  do not use extended unicode characters \n"
                        + " --fontname     -f  fontname is next argument \n"
                        + " --config       -i  config file is next argument \n"
                        + " --doublebuffer -r  turn off screen doublebuffer \n"
                        + "                       (might help on graphics terminals) \n"
                        + " --help         -h  this help screen\n");
                */
              ;
             //System.exit(1);
            } else {
                server = args[i];
            }
    }

    static void parse(String file) {
        BufferedReader in = null;
        try {
            in = new BufferedReader( new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = in.readLine()) != null) {
                StringTokenizer argt = new StringTokenizer(line);
                int n = argt.countTokens();
                String argn[] = new String[n];
                for (int i = 0; i < n; i++)
                    argn[i] = argt.nextToken();
                parse(argn);
            }
        } catch (IOException e) {
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }
}
