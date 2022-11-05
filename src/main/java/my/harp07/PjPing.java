package my.harp07;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static my.harp07.GenericPJ.dnsv;
import static my.harp07.GenericPJ.ipv;
import static my.harp07.GenericPJ.pingIp;
import static my.harp07.GenericPJ.ping_remark;
import static my.harp07.PjFrame.frame;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;

public class PjPing {

    //private static InetAddressValidator ipv = InetAddressValidator.getInstance();
    //private static DomainValidator dnsv = DomainValidator.getInstance();
    private static int pingtimeout;
    //private static JComboBox timeouts;
    //private static JComboBox counts;
    //private static JTextArea ta;
    private static Long RTT_1, RTT_2;
    public static final String[] TIMEOUTS = {
        "100",
        "200",
        "300",
        "400",
        "500",
        "600",
        "700",
        "800",
        "900",
        "1000"
    };
    public static final String[] COUNTS = {
        "10",
        "20",
        "30",
        "40",
        "50",
        "60",
        "70",
        "80",
        "90",
        "100",
        "999"
    };    

    public static String getResult(String ip_dns, JTextArea ta) {
        pingtimeout=Integer.parseInt(PjFrame.comboPingTimeouts.getSelectedItem().toString());
        int loss=0;
        String result = ping_remark + "TimeOut = " + pingtimeout + " ms, Count = "+PjFrame.comboPingCounts.getSelectedItem().toString()+":\n\n";
        if (ip_dns != null) {
            for (int j = 1; j <= 1+Integer.parseInt(PjFrame.comboPingCounts.getSelectedItem().toString()); j++) {
                if (j==1) {
                    pingIp(ip_dns, pingtimeout);
                    continue;
                }
                RTT_1 = System.currentTimeMillis();
                if (pingIp(ip_dns, pingtimeout)) {   // = UP 
                    RTT_2=System.currentTimeMillis();
                    result = result + (j-1)+") OK response from " + ip_dns + ", time=" + (1+RTT_2 - RTT_1) + " ms\n";
                } else {
                    //RTT_2=System.currentTimeMillis();
                    result = result + (j-1)+") NOT response from " + ip_dns + ", TIME OUT !\n";
                    loss++;
                }
                ta.setText(result);
            }
            result = result + "\nLoss packets = "+loss+" from "+PjFrame.comboPingCounts.getSelectedItem().toString()+"\n";
            if (!result.contains(", time=")) result = result + "\nNode DOWN/TimeOut or ICMP-protocol blocked !\n";
        } else {
            JOptionPane.showMessageDialog(frame, "Empty IP/DNS !", "Error", JOptionPane.ERROR_MESSAGE);
        }
        ta.setText("");
        frame.btnPingRun.setEnabled(true);
        return result;
    }

    public static void runGetResult(JTextField ipq, JTextArea ta) {
       // ta.setText("");
        ta.setText("ICMP-ping works well when run as Root on Linux !\n");
        String input = ipq.getText().trim();
        //System.out.println(input);
        if (ipv.isValid(input))  {
            frame.btnPingRun.setEnabled(false);
            //ta.setText("\nPlease, Wait !");
            //ta.setText(getResult(input));
            new Thread(()->ta.setText(getResult(input,ta))).start();
            return;
        } 
        if (dnsv.isValid(input)) {
            frame.btnPingRun.setEnabled(false);
            //ta.setText("\nPlease, Wait !");
            //ta.setText(getResult(input));
            new Thread(()->ta.setText(getResult(input,ta))).start();
            return;
        }
        if (!ipv.isValid(input) && !dnsv.isValid(input)) {
            JOptionPane.showMessageDialog(frame, "Wrong IP/DNS !", "Error", JOptionPane.ERROR_MESSAGE);
        } 
    }

    /*public static void main(String[] args) {
        JLabel lip = new JLabel("Enter IP/DNS: ");
        JTextField ip = new JTextField();
        ip.setBorder(BorderFactory.createLineBorder(Color.BLUE,2));
        ip.setToolTipText("Enter IP/DNS");
        //ip.setText("demo.snmplabs.com");
        ///
        JLabel ltimeouts = new JLabel("Choose ping TimeOut, ms: ");
        JComboBox timeouts = new JComboBox();
        timeouts.setModel(new javax.swing.DefaultComboBoxModel(TIMEOUTS));
        //timeouts.setSelectedIndex(2);
        JLabel lcounts = new JLabel("Choose packets count: ");
        JComboBox counts = new JComboBox();
        counts.setModel(new javax.swing.DefaultComboBoxModel(COUNTS));         
        JLabel lta = new JLabel("Result: ");
        JTextArea ta = new JTextArea();
        ta.setColumns(35);
        ta.setRows(15);
        JScrollPane scrollPane = new JScrollPane(ta);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.MAGENTA,2));
        JSeparator split0 = new JSeparator(SwingConstants.HORIZONTAL);
        JSeparator split1 = new JSeparator(SwingConstants.HORIZONTAL);
        JSeparator split2 = new JSeparator(SwingConstants.HORIZONTAL);
        JSeparator split3 = new JSeparator(SwingConstants.HORIZONTAL);
        JSeparator split4 = new JSeparator(SwingConstants.HORIZONTAL);
        JSeparator split5 = new JSeparator(SwingConstants.HORIZONTAL);        
        JButton get = new JButton("Get Result", new ImageIcon("lib/get-16.png"));
        JButton reset = new JButton("Reset Input", new ImageIcon("lib/reset-16.png"));
        JButton about = new JButton("About", new ImageIcon("lib/about-16.png"));
        JButton quit = new JButton("Quit", new ImageIcon("lib/quit-16.png"));
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(11, 11));
        panel.add(get, BorderLayout.WEST);
        panel.add(about, BorderLayout.EAST);
        panel.add(reset, BorderLayout.CENTER);
        panel.add(quit, BorderLayout.PAGE_END);
        get.addActionListener((ActionEvent e) -> {
            runGetResult(ip,ta);
        });
        ip.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) runGetResult(ip,ta);
            }
        });  
        reset.addActionListener((ActionEvent e) -> {
            ip.setText("");
            ta.setText("");
        });
        quit.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });        
        about.addActionListener((ActionEvent e) -> {
            String msg = "PJ-PING:"
                    + "\n100% Pure Java Ping by ICMP-protocol."
                    + "\nUse standart OS ICMP-packet size: "
                    + "\nLinux=64 bytes, Windows=32 bytes."
                    + "\nCreate by Roman Koldaev,"
                    + "\nSaratov city, Russia."
                    + "\nmail: harp07@mail.ru"
                    + "\nSF-page: https://sf.net/u/harp07/profile/"
                    + "\nGitHub-page: https://github.com/harp077/"
                    + "\nneed JRE-1.8";
            JOptionPane.showMessageDialog(null, msg, "About PJ-PING", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("lib/pj-logo-48.png"));
        });
        Object[] ob = {split0, lip, ip, split1, ltimeouts, timeouts, split2, lcounts, counts, split3, lta, scrollPane, split4, panel, split5};
        //ImageIcon ipicon = new ImageIcon("lib/globus-net-48.png");
        //JOptionPane.showMessageDialog(null, ob, "Java IP-calculator", JOptionPane.CLOSED_OPTION, ipicon);
        //JOptionPane.showMessageDialog(null, ob, "Java IP-calculator", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showOptionDialog(null, ob, "PJ-PING: 100% Pure Java Ping", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon("lib/pj-logo-48.png"), new Object[]{}, null);
    }*/

}
