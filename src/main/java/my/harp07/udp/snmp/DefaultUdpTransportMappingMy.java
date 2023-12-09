
package my.harp07.udp.snmp;

import java.io.Closeable;
import java.io.IOException;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;


public class DefaultUdpTransportMappingMy extends DefaultUdpTransportMapping implements Closeable {
    

    public DefaultUdpTransportMappingMy() throws IOException {
        super();
    }
    
    public DefaultUdpTransportMappingMy(UdpAddress ua) throws IOException {
        super(ua);
    }    

    public DefaultUdpTransportMappingMy(UdpAddress ua, boolean bln) throws IOException {
        super(ua, bln);
    }

    @Override
    public void close() throws IOException {
        super.close();
    }    
    
}
