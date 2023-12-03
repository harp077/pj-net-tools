package my.harp07.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import static my.harp07.PjFrame.frame;
import org.apache.commons.lang3.StringUtils;

public class PjSyslog {

    private static DatagramSocket sd;
    private static int port;
    //private static String portBug; 

    public static void runEmbeddedSyslog(JTextArea ta) {
        ta.setText("");
        /*if (!StringUtils.isNumeric(frame.tfSyslogInput.getText())) {
            ta.append("port must be a number !\n");
            JOptionPane.showMessageDialog(frame, "Wrong Port !", "Error", JOptionPane.ERROR_MESSAGE);
            frame.btnBooleanSyslog.setSelected(false);
            return;
        }*/
        port = Integer.parseInt(frame.tfSyslogInput.getText());
        if (!frame.btnBooleanSyslog.isSelected()) {
            ta.append("port " + port + " NOT listen\n");
            if (sd != null) {
                sd.close();
            }
            return;
        }
        try {
            sd = new DatagramSocket(port);
            ta.append("\nUDP-port " + port + " was free and START listen");
            ta.append("\nService syslog START on udp-port " + port + ", potok = " + Thread.currentThread().getName() + " start\n");
        } catch (SocketException e) {
            frame.btnBooleanSyslog.setSelected(false);
            ta.append("\nException - can't start listening: " + e.getMessage()+"\n");
            //System.out.println("\nCan't start listening: " + e.getMessage());
            if (sd != null) {
                sd.close();
            }
            return;
        }
        try {
            while (frame.btnBooleanSyslog.isSelected()) {
                DatagramPacket dato = new DatagramPacket(new byte[2048], 2048);
                sd.receive(dato);
                String message = new String(dato.getData(), 0, dato.getLength());
                ta.append(message+"\n");
            }
        } catch (IOException ex) {
            //syslogEmbeddedMSG=syslogEmbeddedMSG+("\nIOException - " + ex.getMessage());
            System.out.println("\nIOException - " + ex.getMessage());
        }
        ta.append("udp-port " + port + " STOP listen\n");
        ta.append("Service syslog STOP, udp-port " + port + ", potok = " + Thread.currentThread().getName()+" stop\n");
        if (sd != null) {
            sd.close();
        }
    }

    public static void go(JTextArea ta) {
        java.awt.EventQueue.invokeLater(() -> {
            new Thread(() -> runEmbeddedSyslog(ta)).start();
        });
    }
    
    public static void stopDS() {
        if (sd != null) {
            sd.close();
        }        
    }

}
