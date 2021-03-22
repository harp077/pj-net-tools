
package my.harp07;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;

public class GenericPJ {
    
    public static SubnetUtils su;
    public static InetAddressValidator ipv = InetAddressValidator.getInstance();
    public static DomainValidator dnsv = DomainValidator.getInstance();
    
    // ВИСНЕТ КОГДА НЕ РАБОТАЕТ DNS И БЛОКИ CATCH ПУСТЫЕ !!!
    public static Boolean pingIp(String ipad, int timeout) {
        try {
            return InetAddress.getByName(ipad).isReachable(timeout);
        } 
        catch (UnknownHostException ex) {
            System.out.println("//_ping_DNS-error_UnknownHostException="+ex.getMessage());
        } 
        catch (IOException | NullPointerException ex) {  
            System.out.println("//_ping_IOException/NullPointerException="+ex.getMessage());
        }
        return false;
    }     
    
}
