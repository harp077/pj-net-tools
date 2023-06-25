package my.harp07.tcp;

import javax.swing.SwingUtilities;
import nnm.inet.telnet.JTerm;

public class TelnetNNM_Thread extends Thread {
    
    public static String ipadres;
    public static int port;
    
    public TelnetNNM_Thread (String adres, int port) {  
        start();
        this.ipadres=adres;
        this.port=port;
    }
    
    @Override
    public void run () {
        //SwingUtilities.invokeLater
        java.awt.EventQueue.invokeLater(new Runnable() {
        //SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Logger.getLogger(DJPgui.class.getName()).log(Level.SEVERE, null, ex);
                JTerm.telnet(ipadres,port);
            }
        });         
    }
    
}
