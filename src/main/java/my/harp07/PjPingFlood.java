package my.harp07;

import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JTextArea;
import static my.harp07.GenericPJ.pingIp;
import static my.harp07.PjFrame.frame;

public class PjPingFlood {

    private static boolean pingFloodEnabled = false;
    private static String ip;
    private static long N = 0;
    private static long M = 0;
    private static final int time = 100;
    private static long run;
    private static long end;
    private static int smp;
    private static ConcurrentHashMap<Integer, String> chm = new ConcurrentHashMap<>();
    private static long k = 0;

    public static void runPingFlood(String ip, int timeout, JTextArea ta) {
        chm.clear();
        for (int j = 1; j < 1111; j++) {
            chm.put(j, ip);
        }
        new Thread(() -> {
            while (pingFloodEnabled) {
                k++;
                chm.values().parallelStream()
                        .forEach(x -> {
                            if (pingIp(x, timeout)) {
                                N++;
                            } else {
                                M++;
                            }
                        });
                end = System.currentTimeMillis();
                if (k % 9 == 0) {
                    ta.append("\n-> " + 1000 * (M + N) / (end - run) + " pps");
                } else {
                    ta.append(", -> " + 1000 * (M + N) / (end - run) + " pps");
                }
            }
            end = System.currentTimeMillis();
            ta.append("\n===========\nResult:\nall ping's sended = " + (N + M));
            ta.append("\nall time = " + (end - run) + " msec");
            ta.append("\nping's with response = " + N);
            ta.append("\nping's with not response = " + M);
            ta.append("\nAverage Packets Per Second = " + 1000 * (M + N) / (end - run) + " pps\n");
        }).start();
    }

    public static void runEmbeddedPingFlood(JTextArea ta) {
        ta.setText("");
        ta.append("ONLY FOR CHECK NETWORK NODES DEFENCE ! \nNOT FOR ATTACK !");
        smp = Runtime.getRuntime().availableProcessors();
        if (smp > 1) {
            ta.append("\nUse parallel calculation: Detected CPU's = " + smp);
        }
        ta.append("\nUse small packets - for example: linux=32 byte, win=64 byte, \nFlood-ping running.\n");
        ip = frame.tfPingFloodIP.getText().trim();
        System.out.println("ip = " + ip);
        pingFloodEnabled = true;
        run = System.currentTimeMillis();
        runPingFlood(frame.tfPingFloodIP.getText().trim(), time, ta);
        System.out.println("stop flood");
    }

    public static void go(JTextArea ta) {
        java.awt.EventQueue.invokeLater(() -> {
            new Thread(() -> runEmbeddedPingFlood(ta)).start();
        });
    }

    public static void stop() {
        pingFloodEnabled = false;
    }

}
