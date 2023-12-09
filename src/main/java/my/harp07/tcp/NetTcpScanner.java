package my.harp07.tcp;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import my.harp07.PjFrame;
import static my.harp07.GenericPJ.ipv;
import static my.harp07.GenericPJ.su;
import static my.harp07.PjFrame.frame;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.net.util.SubnetUtils;

public class NetTcpScanner {

    private static int timeoutTCP;
    private static int port;
    public static volatile String result;
    public static volatile String resultUP;
    public static volatile String resultDOWN;
    public static ConcurrentHashMap<String, String> hmresult = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> hmresultUP = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> hmresultDOWN = new ConcurrentHashMap<>();
    //private static AtomicInteger j = new AtomicInteger(1);

    public static String[] scannerCIDRS_MASKS = {
        "/30=255.255.255.252",
        "/29=255.255.255.248",
        "/28=255.255.255.240",
        "/27=255.255.255.224",
        "/26=255.255.255.192",
        "/25=255.255.255.128",
        "/24=255.255.255.0",
        "/23=255.255.254.0",
        "/22=255.255.252.0",
        "/21=255.255.248.0",
        "/20=255.255.240.0",
        "/19=255.255.224.0"
    };

    public static final String[] scannerTIMEOUTS = {
        "100",
        "200",
        "300",
        "400",
        "500",
        "900"
    };

    public static final String[] arrayUpDown = {
        "ALL",
        "UP",
        "DOWN"
    };

    public static void changeInterface(Boolean bbb) {
        frame.btnNetTcpScannerRun.setEnabled(bbb);
        frame.btnNetTcpScannerSave.setEnabled(bbb);
        frame.tfNetTcpScannerIP.setEnabled(bbb);
        frame.comboNetTcpScannerShow.setEnabled(bbb);
        frame.comboNetTcpScannerMasks.setEnabled(bbb);
        frame.comboNetTcpScannerTimeouts.setEnabled(bbb);
        frame.tfNetTcpScannerPort.setEnabled(bbb);
    }

    public static String getResult(String ipadr, JTextArea tap) {
        changeInterface(false);
        su = new SubnetUtils(ipadr);
        //su=new SubnetUtils("10.73.2.111/23");
        //su=new SubnetUtils("10.73.2.111", "255.255.254.0");        
        result = "\n Network IP-data:\n";
        result = result + "\n Low Address = " + su.getInfo().getLowAddress();
        result = result + "\n High Address = " + su.getInfo().getHighAddress();
        result = result + "\n Broadcast Address = " + su.getInfo().getBroadcastAddress();
        //result = result + "\n Netmask = " + su.getInfo().getNetmask();
        result = result + "\n Network Address = " + su.getInfo().getNetworkAddress();
        result = result + "\n Host Addresses Count = " + su.getInfo().getAddressCountLong();
        result = result + "\n CIDR notation = " + su.getInfo().getCidrSignature();
        result = result + "\n MASK notation = " + StringUtils.substringBefore(ipadr, "/") + " " + su.getInfo().getNetmask();
        resultUP = result + "\n\n Use parallel streams, CPU cores = " + Runtime.getRuntime().availableProcessors() + "\n TCP-Scanner data for UP:\n\n";
        resultDOWN = result + "\n\n Use parallel streams, CPU cores = " + Runtime.getRuntime().availableProcessors() + "\n TCP-Scanner data for DOWN:\n\n";
        result = result + "\n\n Use parallel streams, CPU cores = " + Runtime.getRuntime().availableProcessors() + "\n TCP-Scanner data:\n\n";
        timeoutTCP = Integer.parseInt(PjFrame.comboNetTcpScannerTimeouts.getSelectedItem().toString());
        port = Integer.parseInt(PjFrame.tfNetTcpScannerPort.getText());
        hmresult.clear();
        hmresultUP.clear();
        hmresultDOWN.clear();
        Arrays.asList(su.getInfo().getAllAddresses())
                .parallelStream()
                .forEach(x -> {
                    ModelPing mp = PingTCP.pingTCP(x, port, timeoutTCP);
                    if (mp.getPort() == port) {
                        hmresult.put(x, "-> " + x + ", TCP-" + port + " = UP\n");
                        hmresultUP.put(x, "-> " + x + ", TCP-" + port + " = UP\n");
                    } else {
                        hmresult.put(x, "-> " + x + ", TCP-" + port + " = DOWN\n");
                        hmresultDOWN.put(x, "-> " + x + ", TCP-" + port + " = DOWN\n");
                    }
                    synchronized (tap) {
                        tap.setText("\nParallel check by TCP-ping for all IP.\n\n Please Wait !  ..........." + x);
                    }
                });
        result = result + hmresult.values().toString();
        resultUP = resultUP + hmresultUP.values().toString();
        resultDOWN = resultDOWN + hmresultDOWN.values().toString();
        changeInterface(true);
        return result;
    }

    public static void runGetResult(JTextField ipq, JComboBox maskq, JTextField port, JTextArea ta) {
        String input = ipq.getText().trim() + maskq.getSelectedItem().toString().split("=")[0].trim();
        if (!ipv.isValid(ipq.getText().trim())) {
            JOptionPane.showMessageDialog(frame, "Wrong IP !", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!NumberUtils.isParsable(port.getText())) {
            JOptionPane.showMessageDialog(frame, "Wrong TCP-port !", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        frame.comboNetTcpScannerShow.setSelectedItem("ALL");
        ta.setText("\n Please Wait !");
        new Thread(() -> ta.setText(getResult(input, ta))).start();

    }

}
