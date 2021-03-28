package my.harp07;

import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static my.harp07.GenericPJ.ipv;
import static my.harp07.GenericPJ.pingIp;
import static my.harp07.GenericPJ.su;
import static my.harp07.PjFrame.frame;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.validator.routines.InetAddressValidator;

public class PjPingScanner {
    
    private static int pingtimeout;
    public static String result;
    public static String resultUP;
    public static String resultDOWN;
    private static int j=1;

    public static String[] scannerCIDRS_MASKS = {
        //"/22=255.255.252.0",
        "/23=255.255.254.0",
        "/24=255.255.255.0",
        "/25=255.255.255.128",
        "/26=255.255.255.192",
        "/27=255.255.255.224",
        "/28=255.255.255.240",
        "/29=255.255.255.248",
        "/30=255.255.255.252"
    };
    
    public static final String[] scannerTIMEOUTS = {
        "100",
        "200",
        "300"
    }; 
    
    public static final String[] arrayUpDown = {
        "ALL",
        "UP",
        "DOWN"
    };  
    
    public static void changeInterface(Boolean bbb) {
        frame.btnPingScannerRun.setEnabled(bbb);
        frame.btnPingScannerSave.setEnabled(bbb);
        frame.btnPingScannerClear.setEnabled(bbb);  
        //frame.taPingScannerResult.setEnabled(bbb);
        frame.tfPingScannerInput.setEnabled(bbb);
        frame.comboPingScannerShow.setEnabled(bbb);
        frame.comboPingScannerMasks.setEnabled(bbb);
        frame.comboPingScannerTimeouts.setEnabled(bbb);
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
        resultUP = result + "\n\n Ping-Scanner data for UP:\n\n";
        resultDOWN = result + "\n\n Ping-Scanner data for DOWN:\n\n";
        result = result + "\n\n Ping-Scanner data:\n\n";
        pingtimeout=Integer.parseInt(PjFrame.comboPingScannerTimeouts.getSelectedItem().toString());
        j=1;
        Arrays.asList(su.getInfo().getAllAddresses())
                //.parallelStream()
                .stream()
                .forEach(x -> {
                    if (pingIp(x, pingtimeout) || pingIp(x, pingtimeout)) {
                        result = result + j + ") " + x + " = UP\n";
                        resultUP = resultUP + j + ") " + x + " = UP\n";
                        j++;
                    } else {
                        result = result + j + ") " + x + " = DOWN\n";
                        resultDOWN = resultDOWN + j +  ") " + x + " = DOWN\n";
                        j++;
                    }
                    tap.setText("\nCheck by small 2 ping for every IP.\n\n Please Wait !  ........" + j);
                });
        changeInterface(true);
        return result;
    }

    public static void runGetResult(JTextField ipq, JComboBox maskq, JTextArea ta) {
        String input = ipq.getText().trim() + maskq.getSelectedItem().toString().split("=")[0].trim();
        //System.out.println(input);
        if (!ipv.isValid(ipq.getText().trim())) {
            JOptionPane.showMessageDialog(frame, "Wrong IP !", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            //frame.taPingScannerResult.setText("Please Wait !");
            frame.comboPingScannerShow.setSelectedItem("ALL");
            ta.setText("\n Please Wait !");
            new Thread(()->ta.setText(getResult(input,ta))).start();
        }
    }

}
