
package jpacketgenerator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestPacket implements Serializable {

    static final Object serialLock = new Object();
    static long currentSerialNumber = 0;
    public long serialNumber = 0;
    public long serializeTime = 0;
    byte data[];

    public TestPacket(int pktLen) {
        initPacket(pktLen, false);
    }

    /*private TestPacket(int pktLen, boolean fTest) {
        initPacket(pktLen, true);
    }*/

    private void initPacket(int pktLen, boolean fTest) {
        synchronized (serialLock) {
            assert serializeTime == 0;
            serialNumber = currentSerialNumber;
            if (!fTest) {
                currentSerialNumber++;
            }
            // ??!! exception throw then packet < 127 !!!
            data = new byte[pktLen - 127];
        }
    }

    /*static public int estimatePktLenght(int dataSize) {
        TestPacket test = new TestPacket(dataSize, true);
        return test.serialize().length;
    }*/

    public byte[] serialize() {
        ByteArrayOutputStream fos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            this.serializeTime = System.currentTimeMillis();
            oos.writeObject(this);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(TestPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fos.toByteArray();
    }

    /*public static TestPacket fromDatagramPacket(DatagramPacket dpPkt) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(dpPkt.getData());
        ObjectInputStream ois = new ObjectInputStream(bais);
        TestPacket testPacket = null;
        try {
            testPacket = (TestPacket) ois.readObject();
        } catch (ClassNotFoundException ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, null, ex);
        }
        ois.close();
        return testPacket;
    }*/
    
}
