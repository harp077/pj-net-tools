package my.harp07.udp.snmp;

import javax.swing.JTextArea;

public class PjSnmpOidHelp {

    private static String info;

    public static void runSnmpHelp(JTextArea ta) {

        info = "\n      Main generic standard snmp-oids:\n";

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
        
        info = info + "\n   1.3.6.1.2.1.4.* = IP, IP-forward mib`s:\n";
        info = info + "   1.3.6.1.2.1.4.7.0 =  ip in unknown protocol\n";
        info = info + "   1.3.6.1.2.1.4.8.0 =  ip in discards \n";
        info = info + "   1.3.6.1.2.1.4.11.0 = ip out discards \n";
        info = info + "   1.3.6.1.2.1.4.12.0 = ip out no routes \n";
        info = info + "   1.3.6.1.2.1.4.22.1.2 = ip Net To Media Physical Address \n";  
        info = info + "   1.3.6.1.2.1.4.22.1.3 = ip Net To Media Network Address \n";        
        info = info + "   1.3.6.1.2.1.4.23.0 = ip routing discards\n"; 
        info = info + "   1.3.6.1.2.1.4.24.3.0 = ip routes number\n";
        info = info + "   1.3.6.1.2.1.4.24.6.0 = inet routes number\n";
        info = info + "   1.3.6.1.2.1.4.35.1.4 = ip Net-To-Physical-Phys-Address\n";        

        info = info + "\n   1.3.6.1.2.1.5.* = ICMP-mib\n";
        info = info + "   1.3.6.1.2.1.5.2.0 = icmp input errors\n";
        info = info + "   1.3.6.1.2.1.5.15.0 = icmp output errors\n";

        info = info + "\n   1.3.6.1.2.1.6.* = TCP-mib:\n";
        info = info + "   1.3.6.1.2.1.6.14.0 = tcp input errors  \n";
        info = info + "   1.3.6.1.2.1.6.5.0 = tcp active opens\n";
        info = info + "   1.3.6.1.2.1.6.6.0 = tcp passive opens\n";
        info = info + "   1.3.6.1.2.1.6.13.1.1 = tcp Connection state\n";
        info = info + "   1.3.6.1.2.1.6.13.1.2 = tcp Connection Local Address\n";
        info = info + "   1.3.6.1.2.1.6.13.1.3 = tcp Connection Local Port\n";
        info = info + "   1.3.6.1.2.1.6.13.1.4 = tcp Connection Remote Address\n";
        info = info + "   1.3.6.1.2.1.6.13.1.5 = tcp Connection Remote Port\n";
        info = info + "   1.3.6.1.2.1.6.7.0 = tcp Attempt Fails\n";
        info = info + "   1.3.6.1.2.1.6.8.0 = tcp Estab Resets\n";
        info = info + "   1.3.6.1.2.1.6.9.0 = tcp Current Established\n";
        info = info + "   1.3.6.1.2.1.6.10.0 = tcp Inp Segs\n";
        info = info + "   1.3.6.1.2.1.6.11.0 = tcp Out Segs\n";
        info = info + "   1.3.6.1.2.1.6.12.0 = tcp Retrans Segs\n";
        info = info + "   1.3.6.1.2.1.6.15.0 = tcp Out Rsts\n";       

        info = info + "\n   1.3.6.1.2.1.7.* = UDP-mib:\n";
        info = info + "   1.3.6.1.2.1.7.2.0 = udp no-ports\n";
        info = info + "   1.3.6.1.2.1.7.3.0 = udp input errors\n";
        info = info + "   1.3.6.1.2.1.7.1.0 = udp input datagrams\n";
        info = info + "   1.3.6.1.2.1.7.4.0 = udp output datagrams\n";  
        info = info + "   1.3.6.1.2.1.7.5.1.1 = local IP-address of UDP-listener\n"; 
        info = info + "   1.3.6.1.2.1.7.5.1.2 = local UDP-port of UDP-listener\n";                
        
        info = info + "\n   1.3.6.1.2.1.10.* = EtherLike-mib:\n";
        info = info + "   1.3.6.1.2.1.10.7.2.1.19 = Ethernet-Port Duplex \n";       

        info = info + "\n   1.3.6.1.2.1.11.* = SNMP-mib\n";
        info = info + "   1.3.6.1.2.1.11.3.0 = snmp in bad versions\n";
        info = info + "   1.3.6.1.2.1.11.4.0 = snmp in bad community names\n";
        
        info = info + "\n   1.3.6.1.2.1.17.* = BRIDGE-mib\n"; 
        info = info + "   1.3.6.1.2.1.17.2.2.0 = dot1d-Stp-Priority\n";
        info = info + "   1.3.6.1.2.1.17.2.3.0 = dot1d-Stp-Time-Since-Topology-Change\n";
        info = info + "   1.3.6.1.2.1.17.2.5.0 = dot1d-Stp-Designated-Root\n";
        info = info + "   1.3.6.1.2.1.17.2.7.0 = dot1d-Stp-Root-Port\n";        

        info = info + "\n   1.3.6.1.2.1.25.* = HOST-RESOURCES-mib \n";
        info = info + "   1.3.6.1.2.1.25.1.1.0 = System Uptime\n";
        info = info + "   1.3.6.1.2.1.25.1.6.0 = System Processes\n";
        info = info + "   1.3.6.1.2.1.25.2.2.0 = Memory Size\n";
        info = info + "   1.3.6.1.2.1.25.2.3.1.5 = Storage Size\n"; 
        info = info + "   1.3.6.1.2.1.25.2.3.1.1 = Storage Index\n"; 
        info = info + "   1.3.6.1.2.1.25.2.3.1.6 = Storage Used\n";
        info = info + "   1.3.6.1.2.1.25.2.3.1.4 = Storage Allocation Units\n"; 
        info = info + "   1.3.6.1.2.1.25.3.2.1.1 = Device Index\n"; 
        info = info + "   1.3.6.1.2.1.25.3.2.1.2 = Device Type\n"; 
        info = info + "   1.3.6.1.2.1.25.3.4.1.1 = Network If Index\n"; 
        info = info + "   1.3.6.1.2.1.25.3.2.1.6 = Device Errors\n"; 
        info = info + "   1.3.6.1.2.1.25.3.3.1.2 = Processor Load\n";        

        info = info + "\n   1.3.6.1.2.1.33.* = UPS-mib:\n";
        info = info + "   1.3.6.1.2.1.33.1.2.2.0 = Seconds-On-Battery\n";
        info = info + "   1.3.6.1.2.1.33.1.2.3.0 = Estimated-Minutes-Remaining\n";
        info = info + "   1.3.6.1.2.1.33.1.2.7.0 = Battery-Temperature\n";
        info = info + "   1.3.6.1.2.1.33.1.3.3.1.2.1 = Input-Frequency\n";
        info = info + "   1.3.6.1.2.1.33.1.3.3.1.3.1 = Input-Voltage\n";
        info = info + "   1.3.6.1.2.1.33.1.4.2.0 = Output-Frequency\n";
        info = info + "   1.3.6.1.2.1.33.1.4.4.1.5.1 = Output-Percent-Load \n";  
        info = info + "   1.3.6.1.2.1.33.1.4.4.1.2.1 = Output-Voltage  \n";   
        
        info = info + "\n   1.3.6.1.2.1.43.* = Printer-mib:\n";
        info = info + "   1.3.6.1.2.1.43.11.1.1.6.1.1 = black-toner-info\n";
        info = info + "   1.3.6.1.2.1.43.11.1.1.8.1.1 = black-toner-max\n";
        info = info + "   1.3.6.1.2.1.43.11.1.1.9.1.1 = black-toner-current\n"; 

        info = info + "\n";
        ta.setText(info);
    }

}
