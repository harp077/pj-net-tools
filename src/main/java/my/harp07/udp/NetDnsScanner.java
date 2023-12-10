package my.harp07.udp;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static my.harp07.GenericPJ.ipv;
import static my.harp07.GenericPJ.su;
import static my.harp07.PjFrame.frame;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.xbill.DNS.ResolverConfig;

public class NetDnsScanner {

    public static String result;
    public static String resultUP;
    public static String resultDOWN;
    public static Map<String, String> hmresult = new ConcurrentHashMap<>();
    public static Map<String, String> hmresultUP = new ConcurrentHashMap<>();
    public static Map<String, String> hmresultDOWN = new ConcurrentHashMap<>();

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

    public static final String[] arrayUpDown = {
        "all",
        "with name",
        "without name"
    };

    public static void changeInterface(Boolean bbb) {
        frame.btnNetDnsScannerRun.setEnabled(bbb);
        frame.btnNetDnsScannerSave.setEnabled(bbb);
        frame.tfNetDnsScannerIP.setEnabled(bbb);
        frame.comboNetDnsScannerShow.setEnabled(bbb);
        frame.comboNetDnsScannerMasks.setEnabled(bbb);
    }

    public static String getResult(String ipadr, JTextArea tap) {
        changeInterface(false);
        su = new SubnetUtils(ipadr);
        //su=new SubnetUtils("10.73.2.111/23");
        //su=new SubnetUtils("10.73.2.111", "255.255.254.0");   
        result = " System use DNS-servers:  ";
        // throw NullPointerException !!!
        try {        
            Arrays.asList(ResolverConfig.getCurrentConfig().servers())
                .stream()
                .forEach(x -> result = result + x + "\n");
        } catch (Exception ee) {
            result = result + "no DNS-servers\n";
        }        
        result = result + " Network IP-data:\n";
        result = result + "\n Low Address = " + su.getInfo().getLowAddress();
        result = result + "\n High Address = " + su.getInfo().getHighAddress();
        result = result + "\n Broadcast Address = " + su.getInfo().getBroadcastAddress();
        //result = result + "\n Netmask = " + su.getInfo().getNetmask();
        result = result + "\n Network Address = " + su.getInfo().getNetworkAddress();
        result = result + "\n Host Addresses Count = " + su.getInfo().getAddressCountLong();
        result = result + "\n CIDR notation = " + su.getInfo().getCidrSignature();
        result = result + "\n MASK notation = " + StringUtils.substringBefore(ipadr, "/") + " " + su.getInfo().getNetmask();
        resultUP = result + "\n\n Use parallel streams, CPU cores = " + Runtime.getRuntime().availableProcessors() + "\n DNS-scanner data with names:\n\n";
        resultDOWN = result + "\n\n Use parallel streams, CPU cores = " + Runtime.getRuntime().availableProcessors() + "\n DNS-scanner data without names:\n\n";
        result = result + "\n\n Use parallel streams, CPU cores = " + Runtime.getRuntime().availableProcessors() + "\n DNS-scanner data:\n\n";
        hmresult.clear();
        hmresultUP.clear();
        hmresultDOWN.clear();
        Arrays.asList(su.getInfo().getAllAddresses())
                .parallelStream()
                //.stream()
                .forEach(x -> {
                    //synchronized (x) {
                    try {
                        Arrays.asList(InetAddress.getAllByName(x))
                                .stream()
                                .forEach(y -> {
                                    if (!y.getHostName().equals(x)) {
                                        hmresult.put(y.toString(), "-> " + x + " = " + y.getHostName() + "\n");
                                        hmresultUP.put(y.toString(), "-> " + x + " = " + y.getHostName() + "\n");
                                    } else {
                                        hmresult.put(y.toString(), "-> " + x + " = " + y.getHostName() + "\n");
                                        hmresultDOWN.put(y.toString(), "-> " + x + " = " + y.getHostName() + "\n");                                        
                                    }
                                });
                    } catch (Exception ex) {
                        hmresult.put(x, "-> " + x + " = DOWN\n");
                        hmresultDOWN.put(x, "-> " + x + " = DOWN\n");
                    }
                    //}
                    synchronized (tap) {
                        tap.setText("\nParallel DNS-get for all IP.\n\n Please Wait !  ..........." + x);
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
        } else {
            //frame.taPingScannerResult.setText("Please Wait !");
            frame.comboNetDnsScannerShow.setSelectedItem("all");
            ta.setText("\n Please Wait !");
            new Thread(() -> ta.setText(getResult(input, ta))).start();
        }
    }

}
