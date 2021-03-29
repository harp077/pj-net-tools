package my.harp07;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static my.harp07.GenericPJ.ipv;
import static my.harp07.GenericPJ.su;
import static my.harp07.PjFrame.frame;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class PjSnmpGet {

    private static final int Retries = 1;
    private static final int Timeout = 555;
    private static String snmp_ip;
    private static String snmp_vers = "1";
    private static String snmp_comm = "public";
    private static String snmp_port = "161";
    private static String snmp_oid;
    private static String snmp_result;
    private static Map<String, Integer> versionMap = new HashMap<>();
    
    static {
        versionMap.put("1", SnmpConstants.version1);
        versionMap.put("2c", SnmpConstants.version2c);
        versionMap.put("3", SnmpConstants.version3);        
    }

    public static String snmpGet(String ip, String oid, String comm, String port, String vers) {
        String str = "";
        Snmp snmpG = null;
        TransportMapping transportG = null;
        try {
            CommunityTarget comtarget = new CommunityTarget();
            comtarget.setCommunity(new OctetString(comm));
            comtarget.setVersion(versionMap.get(vers));
            comtarget.setAddress(new UdpAddress(ip + "/" + port));
            comtarget.setRetries(Retries);
            comtarget.setTimeout(Timeout);
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(oid)));
            pdu.setType(PDU.GET);
            //TransportMapping 
            transportG = new DefaultUdpTransportMapping();
            transportG.listen();
            //transportGbufer=transportG;
            snmpG = new Snmp(transportG);
            //snmpGbufer=snmpG;
            ResponseEvent response = snmpG.get(pdu, comtarget);
            if (response != null) {
                if (response.getResponse().getErrorStatusText().equalsIgnoreCase("Success")) {
                    PDU pduresponse = response.getResponse();
                    str = pduresponse.getVariableBindings().firstElement().toString();
                    if (str.contains("=")) {
                        int len = str.indexOf("=");
                        str = str.substring(len + 1, str.length());
                    }
                }
            } else {
                System.out.println(ip + " = snmpGet TIMEOUT");
            }
            //transportG.close();
            snmpG.close();
            transportG.close();
        } catch (IOException | NullPointerException ex) {
            //snmpG=null;
            //transportG=null;
            System.out.println("__snmpGet exception: " + ex.getMessage() + ", ip=" + ip);
            return "";
        } finally {
            try {
                if (snmpG != null) {
                    snmpG.close();
                }
                if (transportG != null) {
                    transportG.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(PjSnmpGet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return str.trim();
    }

    public static void runGetResult(JTextField ips, JTextField oids, JTextField comm, JTextArea tas) {
        snmp_ip = frame.tfSnmpGetIP.getText().trim();
        snmp_oid = frame.tfSnmpGetOID.getText().trim();
        snmp_comm = frame.tfSnmpGetCommunity.getText().trim();
        snmp_vers = frame.comboSnmpVersion.getSelectedItem().toString();
        if (!ipv.isValid(ips.getText().trim())) {
            JOptionPane.showMessageDialog(frame, "Wrong IP !", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        snmp_result = snmpGet(snmp_ip, snmp_oid, snmp_comm, snmp_port, snmp_vers);
        if (snmp_result != null && snmp_result.equals("")) {
            snmp_result = "no value";
        }
        tas.setText("\nSnmp version = " + snmp_vers + "\n\nSnmp result value = " + snmp_result);
    }

}
