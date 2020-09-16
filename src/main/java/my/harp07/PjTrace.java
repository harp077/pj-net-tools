package my.harp07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static my.harp07.PjFrame.frame;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;

public class PjTrace {

    private static InetAddressValidator ipv = InetAddressValidator.getInstance();
    private static DomainValidator dnsv = DomainValidator.getInstance();
    //private static String name;
    private static String result;
    private static String os;
    private static String cmd;

    public static String trace(String name, JTextArea ta) {
        os = System.getProperties().getProperty("os.name");
        //name = tf.getText().trim();
        ta.setText("Please wait !");
        if (os.toLowerCase().contains("win")) {
            cmd = "tracert -d " + name;
            result = "OS = Windows: \n" + cmd + "\n";
        } else {
            cmd = "traceroute -n --icmp " + name;
            result = "OS = Linux: \n" + cmd + "\n";
        }
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(cmd.split(" "));
            Process pcs = pb.start(); //Runtime.getRuntime().exec(cmd);
            InputStream is = pcs.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, Charset.defaultCharset());
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            int j = 1;
            while ((line = br.readLine()) != null) {
                if (os.toLowerCase().contains("win") && j == 2) {
                    result = result + "trace to " + name + " with 30 hops max:";
                    line = " ";
                }
                result = result + line + "\n";
                ta.setText(result);
                j++;
            }
            pcs.destroy();
            //ta.setText(result);
        } catch (IOException ex) {
            result = "Unknown Host";
            JOptionPane.showMessageDialog(frame, "Unknown Host Exception !", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("TRACE IOException: " + ex.getMessage());
        }
        ta.setText("");
        frame.btnTraceRun.setEnabled(true);
        return result + "\n===========\n end";
    }

    public static void runTrace(JTextField ipq, JTextArea ta) {
        ta.setText("");
        String input = ipq.getText().trim();
        //System.out.println(input);
        if (ipv.isValid(input)) {
            frame.btnTraceRun.setEnabled(false);
            //ta.setText("\nPlease, Wait !");
            //ta.setText(getResult(input));
            new Thread(() -> ta.setText(trace(input, ta))).start();
            return;
        }
        if (dnsv.isValid(input)) {
            frame.btnTraceRun.setEnabled(false);
            //ta.setText("\nPlease, Wait !");
            //ta.setText(getResult(input));
            new Thread(() -> ta.setText(trace(input, ta))).start();
            return;
        }
        if (!ipv.isValid(input) && !dnsv.isValid(input)) {
            JOptionPane.showMessageDialog(frame, "Wrong IP/DNS !", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
