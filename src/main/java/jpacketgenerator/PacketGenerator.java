package jpacketgenerator;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacketGenerator {

    private ScheduledExecutorService executor;
    private DatagramSocket sock;
    private final Object packetLock = new Object();
    private InetSocketAddress dstAddress;
    private boolean fClosing = false;
    private int speed_bps;
    private Integer requiredPktSize;

    public PacketGenerator(InetSocketAddress scrAddress, InetSocketAddress destAddress, int packetSize, int speed_bps) throws SocketException {
        sock = new DatagramSocket(scrAddress);
        this.dstAddress = destAddress;
        requiredPktSize = packetSize;
        this.speed_bps = speed_bps;
        restartStream();
        System.out.println("Packet generator: streaming !");
    }

    public synchronized void close() {
        stopExecutor();
        sock.close();
        System.out.println("Packet generator: stopped !");
    }

    public final void restartStream() {
        synchronized (packetLock) {
            stopExecutor();
            executor = Executors.newSingleThreadScheduledExecutor();
            //+44 byte udp->ethernet
            //+2 generate packet mette 2 byte in +
            long timePeriod = evaluateTimePeriod(speed_bps, requiredPktSize);
            if (timePeriod > 0) {
                System.out.println("Period Time: " + (Double) ((double) timePeriod / 1000000D) + " msec");
                executor.scheduleAtFixedRate(new PacketGenerator.PacketSender(), 0, timePeriod, TimeUnit.NANOSECONDS);
            } else {
                System.out.println("NOT VALID Period Time: " + (Double) ((double) timePeriod / 1000D) + " msec");
            }
        }
    }

    private void stopExecutor() {
        if (executor != null) {
            executor.shutdown();
        }
    }

    private synchronized long evaluateTimePeriod(int speed_bps, int packetSize) {
        long timePeriod = (long) ((1D / ((double) speed_bps / (packetSize * 8))) * 1000000000);
        return timePeriod;
    }

    /*public synchronized void setPacketSize(Integer iPktSize) {
        this.requiredPktSize = iPktSize;
        restartStream();
    }*/

    /*public synchronized void setStreamSpeed(int speed) {
        this.speed_bps = speed;
        restartStream();
    }*/

    private DatagramPacket createNewPacket() {
        TestPacket testPkt = new TestPacket(requiredPktSize);
        byte[] data = testPkt.serialize();
        return new DatagramPacket(data, data.length, dstAddress.getAddress(), dstAddress.getPort());
    }

    class PacketSender implements Runnable {
        @Override
        public void run() {
            try {
                synchronized (packetLock) {
                    DatagramPacket pkt = createNewPacket();
                    sock.send(pkt);
                }
            } catch (Exception ex) {
                if (!fClosing) {
                    Logger.getLogger(PacketGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
