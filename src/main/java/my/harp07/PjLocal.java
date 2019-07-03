package my.harp07;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import javax.swing.JTextArea;
import org.xbill.DNS.ResolverConfig;

public class PjLocal {

    private static String info;

    public static void runLocalInfo(JTextArea ta) {
        info = "\nLocal network information:\n";
        Enumeration<NetworkInterface> enumerationNI = null;
        try {
            enumerationNI = NetworkInterface.getNetworkInterfaces();
            int j=1;
            while (enumerationNI.hasMoreElements()) {
                NetworkInterface ni = enumerationNI.nextElement();
                info = info + "-----------\n" + j + ") Interface Name = " + ni.getName() + "\n";
                info = info + "MTU = " + ni.getMTU() + "\n";
                info = info + "State is UP = " + ni.isUp() + "\n";
                //info = info  + "mac = " +  Base64.encodeBase64String(ni.getHardwareAddress()) + "\n";
                Enumeration<InetAddress> niInetAddr = ni.getInetAddresses();
                while (niInetAddr.hasMoreElements()) {
                    InetAddress ia = niInetAddr.nextElement();
                    info = info + ia.getHostAddress() + "\n";
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
