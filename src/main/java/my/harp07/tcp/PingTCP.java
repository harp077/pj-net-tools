package my.harp07.tcp;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static my.harp07.GenericPJ.ipv;
import static my.harp07.PjFrame.frame;

/**
 *
 * @author root
 */
public class PingTCP {

    public static final int timeoutTCP = 500; // ms
    private static String scaner_txt = " ";

    public static ModelPing pingTCP(String ip, int port, int timeout) {
        long start = System.currentTimeMillis();
        ModelPing mpt = new ModelPing();
        try (Socket scanTcpSocket = new Socket()) {
            SocketAddress sockaddr = new InetSocketAddress(ip, port);
            scanTcpSocket.connect(sockaddr, timeout);
            // try-with-resources !!!
            //scanTcpSocket.close(); 
            long end = System.currentTimeMillis();
            mpt.setPort(0.0 + port);
            mpt.setTime(1.0 + end - start);
            mpt.setInfo("UP");
            return mpt; //new ModelPingTCP(0.0 + port, 0.0 + end-start, "UP");//0.0+port;
            // Strongly Exception !!!!! - Ð¸Ð¼ÐµÐ½Ð½Ð¾ ÑÐ°Ðº !!!
        } catch (Exception ex) {
            //System.out.println("scanTCP method Exception: " + ip + ":" + port + ", exception = " + ex.getMessage());
            //ex.printStackTrace();
        }
        mpt.setPort(0.0);
        mpt.setTime(0.0);
        mpt.setInfo("DOWN");
        return mpt;
        //return new ModelPingTCP(0.0, 0.0, "DOWN");//0.0;
    }

    public static String getResult(JTextField tfIp, JTextField tfPorts, JTextArea ta) {
        scaner_txt = "Please, wait !";
        scaner_txt = "TCP-timeout = " + timeoutTCP + " ms\nTCP-ping for " + tfIp.getText().trim() + ":\n-------------\n\n";
        Arrays.asList(tfPorts.getText().split(",")).forEach(buferPort -> {
            ModelPing mts = pingTCP(tfIp.getText().trim(), Integer.parseInt(buferPort.trim()), timeoutTCP);
            if (mts.getPort() > 0) {
                scaner_txt = scaner_txt + buferPort + " = UP, response-time: " + mts.getTime() + " ms\n";
            } else {
                scaner_txt = scaner_txt + buferPort + " = timeout or DOWN....\n";
            }
        });
        frame.btnTcpPingRun.setEnabled(true);
        return scaner_txt + "\n-------------\nend.";
    }

    public static void runGetResult(JTextField tfIp, JTextField tfPorts, JTextArea ta) {
        ta.setText("");
        try {
            Arrays.asList(tfPorts.getText().split(",")).stream().map(x -> Integer.parseInt(x)).toArray();
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(frame, "Wrong  TCP-ports !", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (ipv.isValid(tfIp.getText().trim())) {
            frame.btnTcpPingRun.setEnabled(false);
            new Thread(() -> ta.setText(getResult(tfIp, tfPorts, ta))).start();
        } else {
            JOptionPane.showMessageDialog(frame, "Wrong  IP !", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
