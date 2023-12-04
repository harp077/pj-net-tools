package my.harp07;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import javax.swing.JTextArea;
import org.apache.commons.lang3.StringUtils;
import org.xbill.DNS.ResolverConfig;
//import sun.net.dns.ResolverConfiguration;

public class PjLocal {

    private static String info;

    public static void runLocalInfo(JTextArea ta) {
        info = "\nLocal network information:\n";
        Enumeration<NetworkInterface> enumerationNI = null;
        try {
            enumerationNI = NetworkInterface.getNetworkInterfaces();
            int j = 1;
            while (enumerationNI.hasMoreElements()) {
                NetworkInterface ni = enumerationNI.nextElement();
                info = info + "-----------\n" + j + ") Interface Name = " + ni.getName() + "\n";
                if (ni.isUp()) {
                    info = info + "State = UP \n";
                } else {
                    info = info + "State = DOWN \n";
                }
                info = info + "MTU = " + ni.getMTU() + "\n";
                byte[] hardwareAddress = ni.getHardwareAddress();
                if (hardwareAddress != null) {
                    String[] hexadecimalFormat = new String[hardwareAddress.length];
                    for (int i = 0; i < hardwareAddress.length; i++) {
                        hexadecimalFormat[i] = String.format("%02X", hardwareAddress[i]);
                    }
                    info = info + "MAC-address = " + String.join("-", hexadecimalFormat) + "\n";
                }
                Enumeration<InetAddress> niInetAddr = ni.getInetAddresses();
                while (niInetAddr.hasMoreElements()) {
                    InetAddress ia = niInetAddr.nextElement();
                    if (ia.getHostAddress().contains("%")) {
                        info = info + "IPv6-address = " + StringUtils.substringBefore(ia.getHostAddress(), "%") + "\n";
                    } else {
                        info = info + "IPv4-address = " + ia.getHostAddress() + "\n";
                    }
                }
                j++;
            }
        } catch (Exception ex) {
            //Logger.getLogger(CdiSysInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        info = info + "-----------\nLocal DNS-servers:\n";
        // throw NullPointerException !!!
        try {
            //Arrays.asList(ResolverConfiguration.open().nameservers())
            ResolverConfig.getCurrentConfig().servers()
                .stream()
                .forEach(x -> info = info + x + "\n");
            //System.out.println(ResolverConfig.getCurrentConfig().servers());
        } catch (Exception ee) {
            info = info + "no DNS-servers\n";
        }
        info = info + "\n";
        ta.setText(info);
    }

}
