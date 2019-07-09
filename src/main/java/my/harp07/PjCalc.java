package my.harp07;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static my.harp07.PjFrame.frame;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.validator.routines.InetAddressValidator;

public class PjCalc {

    private static SubnetUtils su;
    private static InetAddressValidator ipv = InetAddressValidator.getInstance();
    public static String[] CIDRS_MASKS = {
        "/8=255.0.0.0",
        "/9=255.128.0.0",
        "/10=255.192.0.0",
        "/11=255.224.0.0",
        "/12=255.240.0.0",
        "/13=255.248.0.0",
        "/14=255.252.0.0",
        "/15=255.254.0.0",
        "/16=255.255.0.0",
        "/17=255.255.128.0",
        "/18=255.255.192.0",
        "/19=255.255.224.0",
        "/20=255.255.240.0",
        "/21=255.255.248.0",
        "/22=255.255.252.0",
        "/23=255.255.254.0",
        "/24=255.255.255.0",
        "/25=255.255.255.128",
        "/26=255.255.255.192",
        "/27=255.255.255.224",
        "/28=255.255.255.240",
        "/29=255.255.255.248",
        "/30=255.255.255.252"
    };

    public static String getResult(String ipadr) {
        su = new SubnetUtils(ipadr);
        //su=new SubnetUtils("10.73.2.111/23");
        //su=new SubnetUtils("10.73.2.111", "255.255.254.0");
        String result = "\n IP-data:\n";
        result = result + "\n Low Address = " + su.getInfo().getLowAddress();
        result = result + "\n High Address = " + su.getInfo().getHighAddress();        
        result = result + "\n Broadcast Address = " + su.getInfo().getBroadcastAddress();
        result = result + "\n Netmask = " + su.getInfo().getNetmask();
        result = result + "\n Network Address = " + su.getInfo().getNetworkAddress();
        result = result + "\n Host Addresses Count = " + su.getInfo().getAddressCountLong();
        result = result + "\n CIDR notation = " + su.getInfo().getCidrSignature();
        result = result + "\n MASK notation = " + StringUtils.substringBefore(ipadr, "/") + " " + su.getInfo().getNetmask();
        return result;
    }

    public static void runGetResult(JTextField ipq, JComboBox maskq, JTextArea taq) {
        String input = ipq.getText().trim() + maskq.getSelectedItem().toString().split("=")[0].trim();
        //System.out.println(input);
        if (!ipv.isValid(ipq.getText().trim())) {
            JOptionPane.showMessageDialog(frame, "Wrong IP !", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            taq.setText(getResult(input));
        }
    }

    /*public static void main(String[] args) {
        JLabel lmask = new JLabel("Select Prefix and Mask: ");
        JComboBox mask = new JComboBox();
        mask.setModel(new javax.swing.DefaultComboBoxModel(CIDRS_MASKS));
        //mask.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mask.setToolTipText("Select Prefix and Mask");
        JLabel lip = new JLabel("Enter IP-address: ");
        JTextField ip = new JTextField();
        ip.setBorder(BorderFactory.createLineBorder(Color.BLUE,2));
        ip.setToolTipText("Enter IP-address");
        JLabel lta = new JLabel("Result: ");
        JTextArea ta = new JTextArea();
        ta.setColumns(33);
        ta.setRows(13);
        ta.setBorder(BorderFactory.createLineBorder(Color.MAGENTA,2));
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
        mask.addActionListener((ActionEvent e) -> {
            runGetResult(ip, mask, ta);
        });
        get.addActionListener((ActionEvent e) -> {
            runGetResult(ip, mask, ta);
        });
        ip.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) runGetResult(ip, mask, ta);
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
            String msg = "Java IP-calculator"
                    + "\nCreate by Roman Koldaev,"
                    + "\nSaratov city, Russia."
                    + "\nmail: harp07@mail.ru"
                    + "\nSF-page: https://sf.net/u/harp07/profile/"
                    + "\nGitHub-page: https://github.com/harp077/"
                    + "\nneed JRE-1.8";
            JOptionPane.showMessageDialog(null, msg, "About", JOptionPane.INFORMATION_MESSAGE);
        });
        Object[] ob = {split0, lip, ip, split1, lmask, mask, split2, lta, ta, split4, panel, split5};
        ImageIcon ipicon = new ImageIcon("lib/ip-48x48-1.png");
        //JOptionPane.showMessageDialog(null, ob, "Java IP-calculator", JOptionPane.CLOSED_OPTION, ipicon);
        //JOptionPane.showMessageDialog(null, ob, "Java IP-calculator", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showOptionDialog(null, ob, "Java IP-calculator", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, ipicon, new Object[]{}, null);
    }*/

}
