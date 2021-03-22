
package my.harp07;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;

public class GenericPJ {
    
    public static SubnetUtils su;
    public static InetAddressValidator ipv = InetAddressValidator.getInstance();
    public static DomainValidator dnsv = DomainValidator.getInstance();
    
}
