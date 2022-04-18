package my.harp07;

import javax.swing.SwingUtilities;
import static jpacketgenerator.JPacketGeneratorGUI.goUdpFlood;
import nnm.inet.telnet.JTerm;

public class UdpFlood_Thread extends Thread {
    
    public static String ipadres;
    public static int port;
    
    public UdpFlood_Thread () {  
        start();
        //this.ipadres=adres;
        //this.port=port;
    }
    
    @Override
    public void run () {
        goUdpFlood();
        //SwingUtilities.invokeLater
        /*java.awt.EventQueue.invokeLater(new Runnable() {
        //SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Logger.getLogger(DJPgui.class.getName()).log(Level.SEVERE, null, ex);
                JTerm.telnet(ipadres,port);
            }
        }); */        
    }
    
}
