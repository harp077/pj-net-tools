package my.harp07;

import javax.swing.JTextArea;

public class PjSnmpOidHelp {

    private static String info;

    public static void runSnmpHelp(JTextArea ta) {

        info = "\n      Main generic snmp-oids:\n";

        info = info + "\n   1.3.6.1.2.1.2.2.1.* = standard interface MIB with only 32-bit counters:\n";
        info = info + "   1.3.6.1.2.1.2.2.1.1 = snmp-indexes of interfaces \n";
        info = info + "   1.3.6.1.2.1.2.2.1.2.<interface snmp-index> = interface description \n";
        info = info + "   1.3.6.1.2.1.2.2.1.3.<interface snmp-index> = interface type \n";
        info = info + "   1.3.6.1.2.1.2.2.1.4.<interface snmp-index> = interface mtu \n";
        info = info + "   1.3.6.1.2.1.2.2.1.5.<interface snmp-index> = interface speed \n";
        info = info + "   1.3.6.1.2.1.2.2.1.6.<interface snmp-index> = interface MAC-address \n";
        info = info + "   1.3.6.1.2.1.2.2.1.8.<interface snmp-index> = interface status \n";
        info = info + "   1.3.6.1.2.1.2.2.1.9.<interface snmp-index> = interface last change \n";
        info = info + "   1.3.6.1.2.1.2.2.1.10.<interface snmp-index> = interface input octets (bytes)\n";
        info = info + "   1.3.6.1.2.1.2.2.1.11.<interface snmp-index> = interface input unicast packets\n";
        info = info + "   1.3.6.1.2.1.2.2.1.12.<interface snmp-index> = interface input non-unicast packets\n";
        info = info + "   1.3.6.1.2.1.2.2.1.13.<interface snmp-index> = interface input discard packets\n";
        info = info + "   1.3.6.1.2.1.2.2.1.14.<interface snmp-index> = interface input error packets\n";
        info = info + "   1.3.6.1.2.1.2.2.1.16.<interface snmp-index> = interface output octets (bytes)\n";
        info = info + "   1.3.6.1.2.1.2.2.1.17.<interface snmp-index> = interface output unicast packets\n";
        info = info + "   1.3.6.1.2.1.2.2.1.18.<interface snmp-index> = interface output non-unicast packets\n";
        info = info + "   1.3.6.1.2.1.2.2.1.19.<interface snmp-index> = interface output discard packets\n";
        info = info + "   1.3.6.1.2.1.2.2.1.20.<interface snmp-index> = interface output error packets\n";

        info = info + "\n   1.3.6.1.2.1.31.1.1.1.* = extended interface MIB with 32 and 64-bit counters:\n";
        info = info + "   1.3.6.1.2.1.31.1.1.1.1.<interface snmp-index> = interface name\n";
        info = info + "   1.3.6.1.2.1.31.1.1.1.2.<interface snmp-index> = interface input multicast packets, 32-bit counter\n";
        info = info + "   1.3.6.1.2.1.31.1.1.1.3.<interface snmp-index> = interface input broadcast packets, 32-bit counter\n";
        info = info + "   1.3.6.1.2.1.31.1.1.1.4.<interface snmp-index> = interface output multicast packets, 32-bit counter\n";
        info = info + "   1.3.6.1.2.1.31.1.1.1.5.<interface snmp-index> = interface output broadcast packets, 32-bit counter\n";
        info = info + "   1.3.6.1.2.1.31.1.1.1.6.<interface snmp-index> = interface input octets, 64-bit counter\n";
        info = info + "   1.3.6.1.2.1.31.1.1.1.7.<interface snmp-index> = interface input unicast packets, 64-bit counter\n";
        info = info + "   1.3.6.1.2.1.31.1.1.1.8.<interface snmp-index> = interface input multicast packets, 64-bit counter\n";
        info = info + "   1.3.6.1.2.1.31.1.1.1.9.<interface snmp-index> = interface input broadcast packets, 64-bit counter\n";
        info = info + "   1.3.6.1.2.1.31.1.1.1.10.<interface snmp-index> = interface output octets, 64-bit counter\n";
        info = info + "   1.3.6.1.2.1.31.1.1.1.11.<interface snmp-index> = interface output unicast packets, 64-bit counter \n";
        info = info + "   1.3.6.1.2.1.31.1.1.1.12.<interface snmp-index> = interface output multicast packets, 64-bit counter\n";
        info = info + "   1.3.6.1.2.1.31.1.1.1.13.<interface snmp-index> = interface output broadcast packets, 64-bit counter\n";
        info = info + "   1.3.6.1.2.1.31.1.1.1.15.<interface snmp-index> = interface speed\n";
        info = info + "   1.3.6.1.2.1.31.1.1.1.17.<interface snmp-index> = interface connector present\n";
        info = info + "   1.3.6.1.2.1.31.1.1.1.18.<interface snmp-index> = interface alias (port description)\n";

        info = info + "\n   1.3.6.1.2.1.1.* = system information:\n";
        info = info + "   1.3.6.1.2.1.1.1.0 = system description\n";
        info = info + "   1.3.6.1.2.1.1.3.0 = system uptime\n";
        info = info + "   1.3.6.1.2.1.1.4.0 = system contact\n";
        info = info + "   1.3.6.1.2.1.1.5.0 = system name \n";
        info = info + "   1.3.6.1.2.1.1.6.0 = system location \n";

        /*info = info + "\n   1.3.6.1.2.1.4.* = IP-mib:\n";
        info = info + "   1.3.6.1.2.1.4.22.1.2 = ip Net To Media Physical Address \n";
        info = info + "   1.3.6.1.2.1.4.22.1.3 = ip Net To Media Network Address \n";

        info = info + "\n   1.3.6.1.2.1.6.* = TCP-mib:\n";
        info = info + "   1.3.6.1.2.1.6.13.1.2 = tcp Connection Local Address\n";
        info = info + "   1.3.6.1.2.1.6.13.1.3 = tcp Connection Local Port\n";

        info = info + "\n   1.3.6.1.2.1.7.* = UDP-mib:\n";
        info = info + "   1.3.6.1.2.1.7.5.1.1 = local IP-address of UDP-listener \n";
        info = info + "   1.3.6.1.2.1.7.5.1.2 = local UDP-port of UDP-listener \n";*/
        info = info + "\n";
        ta.setText(info);
    }

}
