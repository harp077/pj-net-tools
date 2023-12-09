package my.harp07.udp.snmp;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import my.harp07.PjFrame;
import static my.harp07.GenericPJ.ipv;
import static my.harp07.GenericPJ.su;
import static my.harp07.PjFrame.frame;
import my.harp07.tcp.ModelPing;
import my.harp07.tcp.PingTCP;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.net.util.SubnetUtils;

public class NetSnmpScanner {

    private static int snmp_timeout;
    private static String snmp_community;
    private static String snmp_version;
    private static String snmp_oid;
    private static String snmp_test_type;
    private static String snmp_port = "161";
    public static volatile String result;
    public static volatile String resultUP;
    public static volatile String resultDOWN;
    public static ConcurrentHashMap<String, String> hmresult = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> hmresultUP = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> hmresultDOWN = new ConcurrentHashMap<>();
    //private static AtomicInteger j = new AtomicInteger(1);
    public static Map<String, String> mainMapSNMP_OIDS = new ConcurrentHashMap<>();
    public static List<String> mainListSNMP_OIDS;
    //private static String snmp_OidTestValue;

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

    static {
        try {
            mainListSNMP_OIDS = Files.readAllLines(Paths.get("cfg/snmp-oids.txt"))
                    .stream()
                    .filter(x -> x.contains("=1.3.6.1.") || x.contains("=.1.3.6.1."))
                    .distinct()
                    .collect(Collectors.toList());
            for (String line : mainListSNMP_OIDS) {
                System.out.println(line);
            }
            mainListSNMP_OIDS
                    .stream()
                    .forEach(x -> {
                        mainMapSNMP_OIDS.put(x.trim().split("=")[0], x.trim().split("=")[1]);
                    }
                    );
            System.out.println(mainMapSNMP_OIDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeInterface(Boolean bbb) {
        frame.btnNetSnmpScannerRun.setEnabled(bbb);
        frame.btnNetSnmpScannerSave.setEnabled(bbb);
        frame.tfNetSnmpScannerIP.setEnabled(bbb);
        frame.comboNetSnmpScannerShow.setEnabled(bbb);
        frame.comboNetSnmpScannerMasks.setEnabled(bbb);
        frame.comboNetSnmpScannerTimeouts.setEnabled(bbb);
        frame.tfNetSnmpScannerCommunity.setEnabled(bbb);
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
        resultUP = result + "\n\n Use parallel streams, CPU cores = " + Runtime.getRuntime().availableProcessors() + "\n SNMP-scanner data for UP:\n\n";
        resultDOWN = result + "\n\n Use parallel streams, CPU cores = " + Runtime.getRuntime().availableProcessors() + "\n SNMP-scanner data for DOWN:\n\n";
        result = result + "\n\n Use parallel streams, CPU cores = " + Runtime.getRuntime().availableProcessors() + "\n SNMP-scanner data:\n\n";
        snmp_community = PjFrame.tfNetSnmpScannerCommunity.getText().trim();
        snmp_version = PjFrame.comboNetSnmpScannerVersion.getSelectedItem().toString();
        snmp_oid = PjFrame.comboNetSnmpScannerOID.getSelectedItem().toString().split("=")[1];
        snmp_test_type = PjFrame.comboNetSnmpScannerOID.getSelectedItem().toString().split("=")[0];
        snmp_timeout = Integer.parseInt(PjFrame.comboNetSnmpScannerTimeouts.getSelectedItem().toString());
        hmresult.clear();
        hmresultUP.clear();
        hmresultDOWN.clear();
        Arrays.asList(su.getInfo().getAllAddresses())
                .parallelStream()
                .forEach(x -> {
                    String snmp_OidTestValue="";
                    snmp_OidTestValue=PjSnmpGet.snmpGet(x, snmp_oid, snmp_community, snmp_port, snmp_version, snmp_timeout);                    
                    if (snmp_OidTestValue.length() > 0) {
                        hmresult.put(x, "-------> " + x + ", snmp UP, " + snmp_test_type + " = " + snmp_OidTestValue + "\n");
                        hmresultUP.put(x, "-------> " + x + ", snmp UP, " + snmp_test_type + " = " + snmp_OidTestValue + "\n");
                    } else {
                        hmresult.put(x, snmp_test_type + ", " + x + ",  snmp DOWN\n");
                        hmresultDOWN.put(x, snmp_test_type + ", " + x + ",  snmp DOWN\n");
                    }
                    synchronized (tap) {
                        tap.setText("\nParallel SNMP-get for all IP.\n\n Please Wait !  ..........." + x);
                    }
                });
        result = result + hmresult.values().toString();
        resultUP = resultUP + hmresultUP.values().toString();
        resultDOWN = resultDOWN + hmresultDOWN.values().toString();
        changeInterface(true);
        return result;
    }

    public static void runGetResult(JTextField ipq, JComboBox maskq, JTextArea ta) {
        String input = ipq.getText().trim() + maskq.getSelectedItem().toString().split("=")[0].trim();
        System.out.println(input);
        if (!ipv.isValid(ipq.getText().trim())) {
            JOptionPane.showMessageDialog(frame, "Wrong IP !", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        frame.comboNetSnmpScannerShow.setSelectedItem("ALL");
        ta.setText("\n Please Wait !");
        new Thread(() -> ta.setText(getResult(input, ta))).start();

    }

}
