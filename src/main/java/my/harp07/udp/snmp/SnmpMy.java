
package my.harp07.udp.snmp;

import java.io.Closeable;
import java.io.IOException;
import org.snmp4j.CommandResponder;
import org.snmp4j.Session;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.Address;


public class SnmpMy extends Snmp implements Closeable, Session, CommandResponder {
    
    
    public SnmpMy(TransportMapping<? extends Address> transportMapping) {
        super(transportMapping);
    }  
    
    @Override
    public void close() throws IOException {
        super.close();
    }
    
}
