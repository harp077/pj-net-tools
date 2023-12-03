package my.harp07.tcp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import static my.harp07.PjFrame.taFtpResult;
import static my.harp07.PjFrame.tfFtpFolder;
import org.apache.ftpserver.ConnectionConfig;
import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerConfigurationException;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

public class FTPServer {

    public static FtpServer server;
    public static Boolean running = false;
    public static SimpleDateFormat sdtf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
    public static int timeout=999;
    public static int maxLogins=11;
    public static int maxLoginsPerIP=3;
    public static int maxThreads=22;
    public static int maxSpeed=125_000_000;
    public static int port=21;

    public static void go() {
        try {
            PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
            UserManager userManager = userManagerFactory.createUserManager();
            BaseUser user = new BaseUser();
            user.setName("anonymous");
            user.setPassword("jer@sey.com");
            user.setHomeDirectory(tfFtpFolder.getText());
            List<Authority> authorities = new ArrayList<>();
            //if (ConfigFTP.writable) 
                authorities.add(new WritePermission());
            authorities.add(new ConcurrentLoginPermission(maxLogins, maxLoginsPerIP));
            authorities.add(new TransferRatePermission(maxSpeed, maxSpeed));
            user.setAuthorities(authorities);
            user.setMaxIdleTime(timeout);
            userManager.save(user);

            ListenerFactory listenerFactory = new ListenerFactory();
            listenerFactory.setPort(port);
            //if (!DEFAULT_IP.equals(ConfigFTP.listenIP)) listenerFactory.setServerAddress(ConfigFTP.listenIP);
            // else all interfaces
            listenerFactory.setIdleTimeout(timeout);

            FtpServerFactory ftpServerFactory = new FtpServerFactory();
            ftpServerFactory.setUserManager(userManager);
            ftpServerFactory.addListener("default", listenerFactory.createListener());

            ConnectionConfigFactory configFactory = new ConnectionConfigFactory();
            //configFactory.setAnonymousLoginEnabled(true);
            configFactory.setMaxThreads(maxThreads);
            configFactory.setMaxAnonymousLogins(maxLogins);
            configFactory.setMaxLogins(maxLogins);
            ConnectionConfig connectionConfig = configFactory.createConnectionConfig();
            ftpServerFactory.setConnectionConfig(connectionConfig);
            server = ftpServerFactory.createServer();
            server.start();
            taFtpResult.setText("");
            taFtpResult.append("\nFully multi-threaded anonymous FTP-server running = " + !server.isStopped());
            taFtpResult.append("\nMax Threads = " + connectionConfig.getMaxThreads());
            taFtpResult.append("\nAnonymous Login Enabled by default = " + connectionConfig.isAnonymousLoginEnabled());
            taFtpResult.append("\nMax concurrent Anonymous Logins = " + connectionConfig.getMaxAnonymousLogins());
            taFtpResult.append("\nMax concurrent Logins = " + connectionConfig.getMaxLogins());
            taFtpResult.append("\nMax concurrent Logins per IP = " + maxLoginsPerIP);
            taFtpResult.append(("\nServer Listen Address = " + listenerFactory.getServerAddress()).replace(" null", " all"));
            taFtpResult.append("\nServer Listen Port = " + listenerFactory.getPort());
            taFtpResult.append("\nServer Idle TimeOut = " + listenerFactory.getIdleTimeout() + " sec");
            taFtpResult.append("\nWritable = true");
            taFtpResult.append("\nFolder = " + tfFtpFolder.getText());
            taFtpResult.append("\nFTP-server main thread = " + Thread.currentThread().getName());
            running = true;
        } catch (FtpServerConfigurationException | FtpException ff) {
            taFtpResult.append("Exception at start FTP-server: " + ff.getMessage());
        }
    }

    public static void stop() {
        try {
            server.stop();
            taFtpResult.setText("");
            taFtpResult.append("\nServer running = " + !server.isStopped());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FTPServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        taFtpResult.append("\nServer shut down = " + server.isStopped());
    }

}
