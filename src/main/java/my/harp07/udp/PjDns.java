package my.harp07.udp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static my.harp07.GenericPJ.dnsv;
import static my.harp07.GenericPJ.ipv;
import static my.harp07.PjFrame.frame;
import org.xbill.DNS.ResolverConfig;

public class PjDns {

    //private static InetAddressValidator ipv = InetAddressValidator.getInstance();
    //private static DomainValidator dnsv = DomainValidator.getInstance();
    private static String name;
    private static String result;

    public static void check(JTextField tf, JTextArea ta) {
        name = tf.getText().trim();
        result = "System use DNS-servers:\n";
        // throw NullPointerException !!!
        try {        
            Arrays.asList(ResolverConfig.getCurrentConfig().servers())
                .stream()
                .forEach(x -> result = result + x + "\n");
        } catch (Exception ee) {
            result = result + "no DNS-servers\n";
        }            
        result = result + "\nResult for " + name + ":\n\n";
        if (dnsv.isValid(name.trim())) {
            try {
                Arrays.asList(InetAddress.getAllByName(name.trim()))
                        //Arrays.asList(org.xbill.DNS.Address.getAllByName(name.trim()))
                        .stream()
                        .forEach(x -> result = result + x.getHostAddress() + "\n");
            } catch (UnknownHostException ex) {
                result = "Unknown Host";
                JOptionPane.showMessageDialog(frame, "Unknown Host Exception !", "Error", JOptionPane.ERROR_MESSAGE);
            }
            ta.setText(result);
        }
        if (ipv.isValid(name.trim())) {
            try {
                Arrays.asList(InetAddress.getAllByName(name.trim()))
                        .stream()
                        .forEach(x -> result = result + x.getHostName() + "\n");
            } catch (UnknownHostException ex) {
                result = "Unknown Host";
                JOptionPane.showMessageDialog(frame, "Unknown Host Exception !", "Error", JOptionPane.ERROR_MESSAGE);
            }
            ta.setText(result);
        }
        if (!ipv.isValid(name.trim()) && !dnsv.isValid(name.trim())) {
            JOptionPane.showMessageDialog(frame, "Wrong IP/DNS !", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
