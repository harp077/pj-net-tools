package my.harp07;

import static jpacketgenerator.JPacketGeneratorGUI.goUdpFlood;

public class UdpFlood_Thread extends Thread {
    
    public UdpFlood_Thread () {  
        start();
    }
    
    @Override
    public void run () {
        goUdpFlood();
    }
    
}
