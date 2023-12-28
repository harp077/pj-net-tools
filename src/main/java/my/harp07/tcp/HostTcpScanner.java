package my.harp07.tcp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static my.harp07.GenericPJ.ipv;
import my.harp07.PjFrame;
import static my.harp07.PjFrame.frame;

public class HostTcpScanner {

    private static String name;
    //private static InetAddressValidator ipv = InetAddressValidator.getInstance();
    private static List<String> genericTcpList
            = Arrays.asList("21:ftp-22:ssh-23:telnet-25:smtp-49:tacacs-53:dns-80:http-88:kerberos-110:pop3-135:msrpc-139:netbios_ssn-143:imap-179:bgp-220:imap3-389:ldap-443:https-445:microsoft_ds-465:smtp+ssl-587:smtp+tls-636:ldaps-993:imaps-995:pop3s-1433:ms_sql_srv-1434:ms_sql_mon-1720:h323-3128:proxy-3306:mysql-3389:ms_terminal-4899:radmin-5060:sip-5432:postgresql-8080:webcache-".split("-"));
    private static Map<String, String> namesMap = new HashMap<>();
    //private static int[] rangeTCP = new int[65536];
    private static List<Integer> allTcpList = new ArrayList<>();
    public static volatile String result;
    public static volatile String resultUP;
    public static volatile String resultDOWN;
    public static ConcurrentHashMap<String, String> hmresult = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> hmresultUP = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> hmresultDOWN = new ConcurrentHashMap<>();
    private static int timeoutTCP;

    public static final String[] scannerTIMEOUTS = {
        "100",
        "200",
        "300",
        "400",
        "500"
    };

    public static final String[] arrayUpDown = {
        "ALL",
        "UP",
        "DOWN"
    };

    // CONSTRUCTOR NOT WORK !!!!
    static {
        //rangeTCP = IntStream.iterate(1, n -> n + 1).limit(65535).toArray();
        //rangeTCP = Arrays.asList(IntStream.iterate(1, n -> n + 1).limit(65535).toArray());
        //Arrays.setAll(rangeTCP, i -> i + 1);
        for (int k = 1; k < 65536; k++) {
            allTcpList.add(k);
        }
        genericTcpList.stream().forEach(x -> namesMap.put(x.split(":")[0], x.split(":")[1]));
        //System.out.println("TCP-ports = " + Arrays.toString(allTcpList.toArray()));
    }

    public static void changeInterface(Boolean bbb) {
        frame.btnHostTcpScannerRun.setEnabled(bbb);
        frame.btnHostTcpScannerSave.setEnabled(bbb);
        frame.tfHostTcpScannerIP.setEnabled(bbb);
        frame.comboHostTcpScannerShow.setEnabled(bbb);
        frame.comboHostTcpScannerTimeouts.setEnabled(bbb);
        frame.comboHostTcpScannerType.setEnabled(bbb);
    }

    public static String getResult(JTextField tf, JTextArea ta) {
        changeInterface(false);
        name = tf.getText().trim();
        result = " Scan TCP-ports.\n TCP-services for " + name + ":\n-------------\n";
        resultUP = result + "\n Use parallel streams, CPU cores = " + Runtime.getRuntime().availableProcessors() + "\n TCP-Scanner data for UP:\n\n";
        resultDOWN = result + "\n Use parallel streams, CPU cores = " + Runtime.getRuntime().availableProcessors() + "\n TCP-Scanner data for DOWN:\n\n";
        result = result + "\n Use parallel streams, CPU cores = " + Runtime.getRuntime().availableProcessors() + "\n TCP-Scanner data:\n\n";
        ta.setText(result);
        timeoutTCP = Integer.parseInt(PjFrame.comboHostTcpScannerTimeouts.getSelectedItem().toString());
        hmresult.clear();
        hmresultUP.clear();
        hmresultDOWN.clear();
        //
        if (PjFrame.comboHostTcpScannerType.getSelectedItem().toString().toLowerCase().equals("generic")) {
            //for (String x : allTcpList.stream().map(x -> x.split(":")[0]).collect(Collectors.toList())) {
            genericTcpList.stream().map(x -> x.split(":")[0]).collect(Collectors.toList())
                    .parallelStream()
                    .forEach(x -> {
                        ModelPing mts = PingTCP.pingTCP(name.trim(), Integer.parseInt(x), timeoutTCP);
                        if (mts.getPort() > 0) {
                            String buf = "";
                            try {
                                buf = namesMap.get("" + x).replace("null", "?").toUpperCase();
                            } catch (NullPointerException nn) {
                            }
                            hmresult.put("" + x, "-> " + x + " : " + buf + ", TCP-" + x + " = UP\n");
                            hmresultUP.put("" + x, "-> " + x + " : " + buf + ", TCP-" + x + " = UP\n");
                        } else {
                            hmresult.put("" + x, "-> " + x + ", TCP-" + x + " = DOWN\n");
                            hmresultDOWN.put("" + x, "-> " + x + ", TCP-" + x + " = DOWN\n");
                        }
                        ta.setText("\n Parallel check by TCP-ping for generic TCP-ports.\n\n Please Wait !  ..........." + x);
                    });
        }
        ///
        if (PjFrame.comboHostTcpScannerType.getSelectedItem().toString().toLowerCase().equals("all")) {
            allTcpList
                    .parallelStream()
                    .forEach(x -> {
                        ModelPing mp = PingTCP.pingTCP(name.trim(), x, timeoutTCP);
                        if (mp.getPort() == x) {
                            String buf = "";
                            try {
                                buf = namesMap.get("" + x).replace("null", "?").toUpperCase();
                            } catch (NullPointerException nn) {
                            }
                            hmresult.put("" + x, "-> " + x + " : " + buf + ", TCP-" + x + " = UP\n");
                            hmresultUP.put("" + x, "-> " + x + " : " + buf + ", TCP-" + x + " = UP\n");
                        } else {
                            hmresult.put("" + x, "-> " + x + ", TCP-" + x + " = DOWN\n");
                            hmresultDOWN.put("" + x, "-> " + x + ", TCP-" + x + " = DOWN\n");
                        }
                        synchronized (ta) {
                            ta.setText("\n Parallel check by TCP-ping for all TCP-ports: 1-65535.\n\n Please Wait !  ..........." + x);
                        }
                    });
        }
        result = result + hmresult.values().toString();
        resultUP = resultUP + hmresultUP.values().toString();
        resultDOWN = resultDOWN + hmresultDOWN.values().toString();
        changeInterface(true);
        return result + "-------------\nend.";
    }

    public static void runGetResult(JTextField ipq, JTextArea ta) {
        ta.setText("");
        frame.comboHostTcpScannerShow.setSelectedItem("ALL");
        if (ipv.isValid(ipq.getText().trim())) {
            new Thread(() -> ta.setText(getResult(ipq, ta))).start();
        } else {
            JOptionPane.showMessageDialog(frame, "Wrong IP !", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
