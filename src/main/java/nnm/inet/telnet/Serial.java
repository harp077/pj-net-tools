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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

/** 
 * Class requires JavaCOMM to be present durring 
 * compilation. On runtime it's only needed 
 * if terminal printer functionality is used. 
 * 
 * <P>See: <A HREF="http://java.sun.com/products/javacomm/"
 * >http://java.sun.com/products/javacomm/</A></P>
 *
 * <P>If you don't want to install JavaCOMM simply
 * store comm.jar to jvtelnet\lib\ext\comm.jar .</P>
 *  
 * <P>Short JavaCOMM installation hints for Win: <BR>
 * <CODE>
 *    JAVA_HOME\jre\bin\win32comm.dll <BR>
 *    JAVA_HOME\jre\lib\ext\comm.jar <BR>
 *    JAVA_HOME\jre\lib\javax.comm.properties <BR>
 * </CODE>
 * PS: When SDK is installed, there is second 
 * copy of JRE stored in C:\Program Files\Java.</P>
 * 
 * @version 0.23 06/09/2003
 * @author Bea Petrovicova <beapetrovicova@yahoo.com>  
 */
class Serial {
    private SerialPort serial = null;
    private InputStream in = null;
    private OutputStream out = null;

    void addEventListener(final SerialEventListener lsnr) {
        try {
            serial.addEventListener(new SerialPortEventListener() {
                Serial serial = Serial.this;
                public void serialEvent(SerialPortEvent event) {
                    lsnr.serialEvent();
                }
            });
        } catch (TooManyListenersException e) {
            System.out.println(e);
        }
    }

    void notifyOnDataAvailable(boolean enable) {
        if (serial != null)
            serial.notifyOnDataAvailable(enable);
    }

    boolean open(String commport, int baudrate, String anFlowcontrol 
        /*,int dataBits,int stopBits,int parity*/) {
        boolean done = false;
        Enumeration list = CommPortIdentifier.getPortIdentifiers();
        while (list.hasMoreElements()) {
            CommPortIdentifier id = (CommPortIdentifier) list.nextElement();
            if (id.getPortType() == CommPortIdentifier.PORT_SERIAL)
                // examples of Name: win "COM1" linux "/dev/term/a"
                if (id.getName().equals(commport)) {
                    try {
                        int flowcontrol;
                        if (anFlowcontrol.equals("none")) {
                            flowcontrol = SerialPort.FLOWCONTROL_NONE;
                        } else if (anFlowcontrol.equals("hw")) {
                            flowcontrol = SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT;
                        } else if (anFlowcontrol.equals("sw")) {
                            flowcontrol = SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT;
                        } else
                            throw new UnsupportedCommOperationException();
                        serial = (SerialPort) id.open("jTerm", 2000);
                        serial.setFlowControlMode(flowcontrol);
                        serial.setSerialPortParams(
                            baudrate,
                            SerialPort.DATABITS_7,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_EVEN);
                        out = /*new BufferedOutputStream(*/ serial.getOutputStream() /*)*/;
                        in = /*new BufferedInputStream(*/ serial.getInputStream() /*)*/;
                        done = true;
                    } catch (UnsupportedCommOperationException e) {
                    } catch (PortInUseException e) {
                    } catch (IOException e) {
                    }
                    if (!done)
                        close();
                    break;
                }
        }
        return done;
    }

    void close() {
        while (in != null || out != null) {
            try {
                InputStream i;
                OutputStream o;
                if (in != null) {
                    i = in;
                    in = null;
                    i.close();
                }
                if (out != null) {
                    o = out;
                    out = null;
                    o.close();
                }
            } catch (IOException e) {
            }
        }
        if (serial != null) {
            serial.close();
            serial = null;
        }
    }

    int available() throws IOException {
        if (in != null)
            return in.available();
        else
            return 0;
    }

    int read() throws IOException {
        if (in != null)
            return in.read();
        else
            throw new IOException("Port Not Connected");
    }

    int read(byte[] b) throws IOException {
        if (in != null)
            return in.read(b);
        else
            throw new IOException("Port Not Connected");
    }

    int read(byte[] b, int off, int len) throws IOException {
        if (in != null)
            return in.read(b, off, len);
        else
            throw new IOException("Port Not Connected");
    }

    void write(int b) throws IOException {
        if (out != null)
            out.write(b);
        else
            throw new IOException("Port Not Connected");
    }

    void write(byte b[]) throws IOException {
        if (out != null)
            out.write(b);
        else
            throw new IOException("Port Not Connected");
    }

    void write(byte b[], int off, int len) throws IOException {
        if (out != null)
            out.write(b, off, len);
        else
            throw new IOException("Port Not Connected");
    }

    void flush() throws IOException {
        if (out != null)
            out.flush();
        else
            throw new IOException("Port Not Connected");
    }

}
