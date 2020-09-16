/*
 * Visit url for update: http://sourceforge.net/projects/jvtelnet
 *
 * JvTelnet was developed by Bea Petrovicova <beapetrovicova@yahoo.com>.
 * The sources was donated to sourceforge.net under the terms
 * of GNU General Public License (GPL). Redistribution of any
 * part of JvTelnet or any derivative works must include this notice.
 */
package nnm.inet.telnet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Telnet class,a standard TELNET client,implementing
 * the "user" host side of a TELNET connection.
 *
 * @version 0.23 06/09/2003
 * @author Bea Petrovicova <beapetrovicova@yahoo.com>
 */
public class Telnet implements Runnable {

    private boolean end = false;
    private boolean loop = false;

    /* *** Telnet Options *** */
    private class OPT
    {
        private static final int BINARY =0;
        /** Telnet OPTION We ask for the remote to do so. */
        private static final int ECHO =1;
        private static final int RCP =2;
        /** Suppress Go Ahead.  We ask for the remote to do so. */
        private static final int SGA =3;
        private static final int NAMS =4,
                                 STATUS =5,
                                 TM =6,
                                 RCTE =7,
                                 NAOL =8,
                                 NAOP =9,
                                 NAOCRD =10,
                                 NAOHTS =11,
                                 NAOHTD =12,
                                 NAOFFD =13,
                                 NAOVTS =14,
                                 NAOVTD =15,
                                 NAOLFD =16,
                                 XASCII =17,
                                 LOGOUT =18,
                                 BM =19,
                                 DET =20,
                                 SUPDUP =21,
                                 SUPDUPOUTPUT =22,
                                 SNDLOC =23;
        /** Negotiate term type. */
        private static final int TTYPE =24;
        private static final int EOR =25,
                                 TUID =26,
                                 OUTMRK =27,
                                 TTYLOC =28,
                                 REGIME3270 =29,
                                 X3PAD =30;
        /** Negotiate about window size. */
        private static final int NAWS =31;
        private static final int TSPEED =32,
                                 LFLOW =33,
                                 LINEMODE =34,
                                 XDISPLOC =35,
                                 OLDENV =36,
                                 AUTHENTICATION =37,
                                 ENCRYPT =38,
                                 NEWENV = 39;
                                 //EXOPL =255;

        private static final int FIRST =BINARY,
                                 LAST =NEWENV;
    }

    /* *** Telnet Commands ***
     A command consists of a 0xFF byte
     followed by one of the following codes. */

    private class CMD
    {
        private static final int EOF =236;
        private static final int SUSP =237;
        private static final int ABORT =238;
        private static final int EOR =239;

        /** End of subnegotiation */
        private static final int SE =240;

        /** No Operation */
        private static final int NOP =241;

        /** Data Mark (Data portion of Synch). */
        private static final int DM =242;
        /* private static final int SYNC =242; */
        /** Break */
        private static final int BREAK =243;
        /** Interrupt Process */
        private static final int IP =244;
        /** Abort Output */
        private static final int AO =245;
        /** Are You There */
        private static final int AYT =246;
        /** Erase Character */
        private static final int EC =247;
        /** Erase Line */
        private static final int EL =248;
        /** Go Ahead */
        private static final int GA =249;
        /** Perform subnegotiation of the option. */
        private static final int SB =250;

        /** Indicate desire to begin performing
         an option or confirm that it is
         now being performed. */
        private static final int WILL =251;

        /** Refuse to perform or continue
         performing an option. */
        private static final int WONT =252;

        /** Ask the remote system to start
         performing an option,or confirm
         we now expect it to perform
         the option. */
        private static final int DO =253;

        /** Ask the remote system to stop
         performing an option,or confirm
         we no longer expect it to perform
         the option. */
        private static final int DONT =254;

        /** Interpret next by as a command;
         IAC IAC is a 255 data byte. */
        private static final int IAC =255;

        private static final int FIRST =EOF,
                                 LAST =IAC;
    }

    /** Current local and remote option status. */
    private boolean[] locopts = new boolean[OPT.LAST -OPT.FIRST +1],
                      remopts = new boolean[OPT.LAST -OPT.FIRST +1];

    /** The local options that we have implemented. */
    private static final boolean[] implocopts =
    {   /*  1 */ true,false,false,true,false,
        /*  2 */ false,false,false,false,false,
        /*  3 */ false,false,true,false,false,
        /*  4 */ false,false,false,false,false,
        /*  5 */ false,false,false,
        /*  6 */ false,true,false,
        /*  7 */ false,false,false,
        /*  8 */ false,false,true,true,false,
        /*  9 */ false,false,false,false,
        /* 10 */ false,false,false };

    /** The remote options that we have implemented. */
    private static final boolean[] impremopts =
    {   /*  1 */ true,true,false,true,false,
        /*  2 */ false,false,false,false,false,
        /*  3 */ false,false,false,false,false,
        /*  4 */ false,false,false,false,false,
        /*  5 */ false,false,false,
        /*  6 */ false,false,false,
        /*  7 */ false,false,false,
        /*  8 */ false,false,false,false,false,
        /*  9 */ false,false,false,false,
        /* 10 */ false,false,false };

    /** Socket */
    private Socket control;
    /** Socket Output Stream */
    private OutputStream out;
    /** Socket Input Stream */
    private InputStream in;

    /** An Telnet Session? */
    private boolean isTelnet = false;
    /** Selected Emulation. */
    private int emuNum = -1;
    /** Current window dimensions. */
    private int width = -1, height = -1;

    private Context context = new Context();
    private class Context {
        /** Prints message line to output console. */
        public synchronized void printlog(String message) {
            for (int i = 0; i < message.length(); i++) {
                receive(message.charAt(i));
            }
            receive('\r');
            receive('\n');
        }

        /** Prints object to standard output. */
        public void printerr(Exception exception) {
            //System.out.println("Thread: " + Thread.currentThread().getName());
           // System.out.println("Exception:");
            //exception.printStackTrace();
        }
    }

    boolean isCmd(int cmd) {
        return (cmd <= CMD.LAST && cmd >= CMD.FIRST);
    }

    boolean isOpt(int opt) {
        return (opt <= OPT.LAST && opt >= OPT.FIRST);
    }

    boolean isImpLocOpt(int opt) {
        return (isOpt(opt) && implocopts[opt - OPT.FIRST]);
    }

    boolean isImpRemOpt(int opt) {
        return (isOpt(opt) && impremopts[opt - OPT.FIRST]);
    }

    /** Terminal Emulator. */
    private AEmulator emu;

    OutputStream log = null;

    public Telnet(AEmulator emu) {
        this.emu = emu;
        emu.telnet = this;
       context.printlog("\n");
        //context.printlog("\nNetWhistler Telnet\n");
      }

    public void debug(File logFile) {
        try {
            log =
                new BufferedOutputStream(
                    new FileOutputStream(logFile));
        } catch (Exception e) {
        }
    }

    public void debug(String logFile) {
        try {
            debug (new File (logFile));
        } catch (Exception e) {
        }
    }

    public void debug() {
        debug ("telnet.log");
    }

    public void loop(boolean loop) {
        this.loop = loop;
    }

    String server = "";
    int port = 23;

    public synchronized boolean connect(String server, int port) {
        /* Info for Auto Reconnect */
        this.server = server;
        this.port = port;

        boolean done = false;
        if (control == null) {
            for (int i = 0; i < locopts.length; i++)
                locopts[i] = remopts[i] = false;
            try { /* Get host name */
               // context.printlog("Getting host by name: " + server);
                InetAddress addr = InetAddress.getByName(server);

                /* Connect to host */
                context.printlog("Connecting to host: " + addr.getHostAddress());
                control = new Socket(addr, port);
                //control.setSoTimeout(20000);

                /* Open input / output streams */
                in = new BufferedInputStream(control.getInputStream());
                out = /*new BufferedOutputStream(*/
                control.getOutputStream() /*)*/;

                Thread thread = new Thread(this, "Telnet-queue " + server);
                thread.start();
                done = true;
            } catch (UnknownHostException e) {
                context.printlog(" Can't resolve host address! ");
            } catch (IOException e) {
                disconnect();
                context.printlog(" Can't connect to host! ");
            } catch (Exception e) {
                context.printlog(" Permission denied! ");
            }
        }
        return done;
    }

    public synchronized void disconnect() {
        end = true;  // leave the loop
        while (in != null || out != null) {
            try {
                if (in != null) {
                    InputStream i = in;
                    in = null;
                    i.close();
                }
                if (out != null) {
                    OutputStream o = out;
                    out = null;
                    o.close();
                }
                if (log != null) {
                    OutputStream l = log;
                    log = null;
                    l.close();
                }
            } catch (IOException e) {
                context.printerr(e);
            }
        }
        if (control != null) {
            try {
                control.close();
            } catch (IOException e) {
                context.printerr(e);
            } finally {
                control = null;
            }
            //context.printlog("< Ctrl: Disconnected! >"); }
        }
    }

    public boolean isConnected() {
        return (control != null);
    }

    public void run() {
        while (!end) {
            try {
                int b = in.read();
                if (b < 0)
                    break;
                if (b == CMD.IAC)
                    processCmd();
                else
                    processData((char) b);
            } catch (IOException e) {
                context.printerr(e);
            }
        }
        context.printlog("");
        context.printlog("Connection closed by remote host.");
        context.printlog("");
        disconnect();

        if(loop) {
            // reconnect in loop
            connect(server, port);
        }
    }

    public void receive(char ch) {
        if (emu != null) {
            try {
                if (log != null) {
                    log.write(C0.toString(ch).getBytes());
                    log.flush();
                }
            } catch (IOException e) {
            }
            emu.receive(ch);
        }
    }

    public void send(char ch) throws IOException {
        if (out != null) {
            out.write((byte) ch);
            out.flush();
        }
        //if(ch=='\n') out.flush();
        //if (!remopts[OPT.ECHO -OPT.FIRST])
        //    receive(c); }
    }

    public void send(byte[] b) throws IOException {
        if (out != null) {
            out.write(b);
            out.flush();
        }
        //if (!remopts[OPT.ECHO -OPT.FIRST])
        //    emu.receive(new String(b)); }
    }

    public void send(byte[] b, int off, int len) throws IOException {
        if (out != null) {
            out.write(b, off, len);
            out.flush();
        }
        //if (!remopts[OPT.ECHO -OPT.FIRST])
        //    emu.receive(new String(b)); }
    }

    public void send(String str) throws IOException {
        if (out != null) {
            out.write(str.getBytes());
            out.flush();
        }
        //if (!remopts[OPT.ECHO -OPT.FIRST])
        //    emu.receive(str); }
    }

    public void flush() throws IOException {
        if (out != null) {
            out.flush();
        }
    }

    public synchronized void setWindowSize(int width, int height) {
        this.width = width;
        this.height = height;
        try {
            sendNaws(width, height);
        } catch (IOException e) {
            context.printerr(e);
        }
    }

    /** Process Telnet Data */
    synchronized private void processData(char c) throws IOException {
        if (emu != null) {
            receive(c);
        }
    }

    /** Process Telnet Command */
    private void processCmd() throws IOException {
        int cmd = in.read();
        switch (cmd) {
            case CMD.IAC :
                processData((char) cmd);
                break;
            case CMD.DONT :
            case CMD.DO :
            case CMD.WONT :
            case CMD.WILL :
                int opt = in.read();
                processOpt(cmd, opt);
                break;
            case CMD.SB :
                opt = in.read();
                processSub(opt);
                break;
            default :
                return;
        }
        if (!isTelnet) {
            isTelnet = true;
            sendOpt(CMD.WILL, OPT.TSPEED, false);
            sendOpt(CMD.WILL, OPT.TTYPE, false);
            sendOpt(CMD.WILL, OPT.NAWS, false);
            sendOpt(CMD.WILL, OPT.NAOHTD, false);
            sendOpt(CMD.DO, OPT.ECHO, false);
            sendOpt(CMD.DO, OPT.SGA, false);
        }
    }

    /** Take action on a process comment received from the server. */
    synchronized private void processOpt(int cmd, int opt) throws IOException {
        /* If this is a local option we don't understand
         or have not implemented, refuse any 'DO' request. */
        if (cmd == CMD.DO && !isImpLocOpt(opt)) {
            sendOpt(CMD.WONT, opt, true);
        }
        /* If this is a server option we don't understand
            or have not implemented,refuse any 'DO' request. */
        else if (cmd == CMD.WILL && !isImpRemOpt(opt)) {
            sendOpt(CMD.DONT, opt, true);
        }
        /* If this is a DONT request,(possibly)
            send a reply and turn off the option. */
        else if (cmd == CMD.DONT) {
            sendOpt(CMD.WONT, opt, false);
        }
        /* If this is a WONT request, (possibly)
            send a reply and turn off the option. */
        else if (cmd == CMD.WONT) {
            sendOpt(CMD.DONT, opt, false);
        }
        /* If this is a DO request, (possibly)
            send a reply and turn on the option. */
        else if (cmd == CMD.DO) {
            sendOpt(CMD.WILL, opt, false);
            if (opt == OPT.NAWS)
                sendNaws(width, height);
            else if (opt == OPT.TTYPE)
                emuNum = -1;
            else if (opt == OPT.NAOHTD)
                sendNaohtd();
        }
        /* If this is a WILL request,(possibly)
            send a reply and turn on the option. */
        else if (cmd == CMD.WILL) {
            sendOpt(CMD.DO, opt, false);
        }
    }

    /** Process a subnegotiation sequence.
     Note that this CAN NOT be synchronized,
     because it may block while reading the
     subnegotiation data! */
    private void processSub(int opt) throws IOException {
        switch (opt) {
            case OPT.TTYPE :
                int b = in.read();
                skipSub();
                if (b != 1)
                    break;
                emuNum++;
                sendTtype();
                break;
            case OPT.TSPEED :
                b = in.read();
                skipSub();
                if (b != 1)
                    break;
                sendTspeed();
                break;
            default :
                skipSub();
                break;
        }
    }

    /** Skip rest of the subnegotiation request. */
    private void skipSub() throws IOException {
        while (!end) {
            int b = in.read();
            if (b == CMD.IAC) {
                b = in.read();
                if (b == CMD.SE)
                    break;
            }
        }
    }

    /** Send an option command to the remote server. */
    synchronized private void sendOpt(int cmd, int opt, boolean force)
        throws IOException { /* Send this command if we are being forced to,
           OR if it is a change of state of the server options,
           OR if it is a change in the state of our local options. */
        if (force || (isImpRemOpt(opt)
                && (cmd == CMD.DONT && remopts[opt - OPT.FIRST])
                || (cmd == CMD.DO && !remopts[opt - OPT.FIRST]))
            || (isImpLocOpt(opt)
                && (cmd == CMD.WONT && locopts[opt - OPT.FIRST])
                || (cmd == CMD.WILL && !locopts[opt - OPT.FIRST]))) {
            byte[] reply = new byte[3];
            reply[0] = (byte) CMD.IAC;
            reply[1] = (byte) cmd;
            reply[2] = (byte) opt;
            send(reply);
        }
        /* Change our options state. We really shouldn't be turning options on
         until we get a reply, but this isn't a problem yet for the options that
         are currently implemented... */
        if (cmd == CMD.WILL) {
            if (isImpLocOpt(opt))
                locopts[opt - OPT.FIRST] = true;
        } else if (cmd == CMD.WONT) {
            if (isImpLocOpt(opt))
                locopts[opt - OPT.FIRST] = false;
        } else if (cmd == CMD.DO) {
            if (isImpRemOpt(opt))
                remopts[opt - OPT.FIRST] = true;
        } else if (cmd == CMD.DONT) {
            if (isImpRemOpt(opt))
                remopts[opt - OPT.FIRST] = false;
        }
    }

    /** Used for constructing messages. */
    int put_byte(byte[] b, int pos, byte val) {
        b[pos++] = val;
        if (val == CMD.IAC)
            b[pos++] = (byte) val;
        return pos;
    }

    /** Send a window size negotiation. */
    synchronized private void sendNaws(int width, int height)
        throws IOException {
        if (locopts[OPT.NAWS - OPT.FIRST]) {
            byte[] reply = new byte[14];
            int i = 0;
            reply[i++] = (byte) CMD.IAC;
            reply[i++] = (byte) CMD.SB;
            reply[i++] = (byte) OPT.NAWS;
            i = put_byte(reply, i, (byte) ((width >> 8) & 0xFF));
            i = put_byte(reply, i, (byte) (width & 0xFF));
            i = put_byte(reply, i, (byte) ((height >> 8) & 0xFF));
            i = put_byte(reply, i, (byte) (height & 0xFF));
            reply[i++] = (byte) CMD.IAC;
            reply[i++] = (byte) CMD.SE;
            send(reply, 0, i);
        }
    }

    /** Send a term type negotiation. */
    synchronized private void sendTtype() throws IOException {
        if (emu != null) {
            String s = emu.getTypeName(emuNum);
            if (s == null)
                s = "UNKNOWN";
            char[] c = s.toCharArray();
            byte reply[] = new byte[c.length + 6];
            int i = 0;
            reply[i++] = (byte) CMD.IAC;
            reply[i++] = (byte) CMD.SB;
            reply[i++] = (byte) OPT.TTYPE;
            reply[i++] = 0;
            for (int j = 0; j < c.length; j++)
                reply[i++] = (byte) c[j];
            reply[i++] = (byte) CMD.IAC;
            reply[i++] = (byte) CMD.SE;
            send(reply);
        }
    }

    /** Negotiate output horizontal tab disposition: let us do it all. */
    synchronized private void sendNaohtd() throws IOException {
        if (locopts[OPT.NAOHTD - OPT.FIRST]) {
            byte[] reply = new byte[14];
            int i = 0;
            reply[i++] = (byte) CMD.IAC;
            reply[i++] = (byte) CMD.SB;
            reply[i++] = (byte) OPT.NAOHTD;
            reply[i++] = (byte) 0;
            reply[i++] = (byte) 0;
            reply[i++] = (byte) CMD.IAC;
            reply[i++] = (byte) CMD.SE;
            send(reply, 0, i);
        }
    }

    /** Negotiate about term speed: fast as possible! */
    synchronized private void sendTspeed() throws IOException {
        if (locopts[OPT.TSPEED - OPT.FIRST]) {
            byte[] reply = new byte[20];
            int i = 0;
            reply[i++] = (byte) CMD.IAC;
            reply[i++] = (byte) CMD.SB;
            reply[i++] = (byte) OPT.TSPEED;
            reply[i++] = (byte) 0;
            reply[i++] = (byte) '5';
            reply[i++] = (byte) '2';
            reply[i++] = (byte) '0';
            reply[i++] = (byte) '0';
            reply[i++] = (byte) '0';
            reply[i++] = (byte) ',';
            reply[i++] = (byte) '5';
            reply[i++] = (byte) '2';
            reply[i++] = (byte) '0';
            reply[i++] = (byte) '0';
            reply[i++] = (byte) '0';
            reply[i++] = (byte) CMD.IAC;
            reply[i++] = (byte) CMD.SE;
            send(reply, 0, i);
        }
    }
}
