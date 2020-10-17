package my.harp07;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import javax.swing.JTextArea;
import org.apache.commons.lang3.StringUtils;
import org.xbill.DNS.ResolverConfig;

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
        } catch (SocketException | NullPointerException ex) {
            //Logger.getLogger(CdiSysInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        info = info + "-----------\nLocal DNS-servers:\n";
        Arrays.asList(ResolverConfig.getCurrentConfig().servers())
                .stream()
                .forEach(x -> info = info + x + "\n");
        info = info + "\n";
        ta.setText(info);
    }

}
