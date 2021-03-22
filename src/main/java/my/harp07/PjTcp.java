package my.harp07;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static my.harp07.GenericPJ.ipv;
import static my.harp07.PjFrame.frame;
import org.apache.commons.validator.routines.InetAddressValidator;

public class PjTcp {

    private static String name;
    //private static InetAddressValidator ipv = InetAddressValidator.getInstance();
    private static List<String> allTcpList
            = Arrays.asList("21:ftp-22:ssh-23:telnet-25:smtp-49:tacacs-53:dns-80:http-88:kerberos-110:pop3-135:msrpc-139:netbios_ssn-143:imap-179:bgp-220:imap3-389:ldap-443:https-445:microsoft_ds-465:smtp+ssl-587:smtp+tls-636:ldaps-993:imaps-995:po3s-1433:ms_sql_srv-1434:ms_sql_mon-1720:h323-3128:proxy-3306:mysql-3389:ms_terminal-4899:radmin-5060:sip-5432:postgresql-8080:webcache-".split("-"));
    private static Map<String, String> namesMap = new HashMap<>();

    // CONSTRUCTOR NOT WORK !!!!
    static {
        allTcpList.stream().forEach(x -> namesMap.put(x.split(":")[0], x.split(":")[1]));
    }

    public static Double scanTCP(String ip, int port) {
        try {
            SocketAddress sockaddr = new InetSocketAddress(ip, port);
            Socket sc = new Socket();
            int timeout = 333;
            sc.connect(sockaddr, timeout);
            sc.close();
            return 0.0 + port;
        } catch (UnknownHostException ex) {
            //System.out.println("UnknownHostException");
        } catch (ConnectException ex) {
            //System.out.println("ConnectException");
        } catch (SocketTimeoutException ex) {
            //System.out.println("SocketTimeoutException");
        } catch (IOException ex) {
            //Logger.getLogger(TCPScan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0.0;
    }

    public static String getResult(JTextField tf, JTextArea ta) {
        name = tf.getText().trim();
        String res = "Open TCP-services for " + name + ":\n-------------\n";
        ta.setText(res);
        for (String buf : allTcpList.stream().map(x -> x.split(":")[0]).collect(Collectors.toList())) {
            double dbl = scanTCP(name.trim(), Integer.parseInt(buf));
            if (dbl > 0) {
                res = res + String.format("%.0f", dbl) + " : " + namesMap.get(buf) + "\n";
                ta.setText(res);
            }
        }
        frame.btnTcpRun.setEnabled(true);
        return res + "-------------\nend.";
    }

    public static void runGetResult(JTextField ipq, JTextArea ta) {
        ta.setText("");
        if (ipv.isValid(ipq.getText().trim())) {
            frame.btnTcpRun.setEnabled(false);
            new Thread(() -> ta.setText(getResult(ipq, ta))).start();
        } else {
            JOptionPane.showMessageDialog(frame, "Wrong IP !", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
