package my.harp07.udp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import static my.harp07.PjFrame.btnBooleanTftp;
import static my.harp07.PjFrame.btnTftpSave;
import static my.harp07.PjFrame.iconOn;
import static my.harp07.PjFrame.taTftpResult;
import static my.harp07.PjFrame.tfTftpFolder;
import org.apache.commons.net.io.FromNetASCIIOutputStream;
import org.apache.commons.net.io.ToNetASCIIInputStream;
import org.apache.commons.net.tftp.TFTP;
import org.apache.commons.net.tftp.TFTPAckPacket;
import org.apache.commons.net.tftp.TFTPDataPacket;
import org.apache.commons.net.tftp.TFTPErrorPacket;
import org.apache.commons.net.tftp.TFTPPacket;
import org.apache.commons.net.tftp.TFTPPacketException;
import org.apache.commons.net.tftp.TFTPReadRequestPacket;
import org.apache.commons.net.tftp.TFTPWriteRequestPacket;

 /* A fully multi-threaded tftp server. Can handle multiple clients at the same
 time. Implements RFC 1350 and wrapping block numbers for large file support.
    To launch, just create an instance of the class. An IOException will be
 thrown if the server fails to start for reasons such as port in use, port
 denied, etc.
    To stop, use the shutdown method.
    To check to see if the server is still running (or if it stopped because of
an error), call the isRunning() method.
    By default, events are not logged to stdout/stderr. This can be changed with
the setLog and setLogError methods.
    Example usage is below:
public static void main(String[] args) throws Exception {
       if (args.length != 1) {
           System.out
           .println("You must provide 1 argument - the base path for the server to serve from.");
           System.exit(1);
       }
       TFTPServer ts = new TFTPServer(new File(args[0]), new File(args[0]), GET_AND_PUT);
       ts.setSocketTimeout(2000);
       System.out.println("TFTP Server running.  Press enter to stop.");
       new InputStreamReader(System.in).read();
       ts.shutdown();
       System.out.println("Server shut down.");
       System.exit(0);
 */

public class TFTPServer implements Runnable {

    public static java.util.logging.Logger jul;
    //public static org.apache.log4j.Logger jul;

    static {
            // for java.util.logging:
        try (FileInputStream ins = new FileInputStream("log/jul.properties")) {
            LogManager.getLogManager().readConfiguration(ins);
            jul = java.util.logging.Logger.getLogger(TFTPServer.class.getName());
        } catch (Exception ignore) { ignore.printStackTrace(); }

            // for log4j:
        //File propertiesFile = new File("cfg/log4j.properties");
        //PropertyConfigurator.configure(propertiesFile.toString());
        //PropertyConfigurator.configure("log/jul.properties");
        //DOMConfigurator.configure("cfg/log4j.xml"); 
        //jul = java.util.logging.Logger.getLogger(TFTPServer.class.getName());
    }

    private static final int DEFAULT_TFTP_PORT = 69;

    public enum ServerMode {
        GET_ONLY, PUT_ONLY, GET_AND_PUT;
    }

    private final HashSet<TFTPTransfer> transfers_ = new HashSet<>();
    private volatile boolean shutdownServer = false;
    private TFTP serverTftp_;
    private File serverReadDirectory_;
    private File serverWriteDirectory_;
    private final int port_;
    private final InetAddress laddr_;
    private Exception serverException = null;
    private final ServerMode mode_;
    private int maxTimeoutRetries_ = 3;
    private int socketTimeout_;
    private Thread serverThread;
    private static TFTPServer ts;

     /* Start a TFTP Server on the default port (69). Gets and Puts occur in the
     * specified directories.
     * The server will start in another thread, allowing this constructor to
     * return immediately.
     * If a get or a put comes in with a relative path that tries to get outside
     * of the serverDirectory, then the get or put will be denied.
     * GET_ONLY mode only allows gets, PUT_ONLY mode only allows puts, and
     * GET_AND_PUT allows both. Modes are defined as int constants in this
     * class.
     * @param serverReadDirectory directory for GET requests
     * @param serverWriteDirectory directory for PUT requests
     * @param mode A value as specified above.
     * @throws IOException if the server directory is invalid or does not exist.
     */
    
    public TFTPServer(final File serverReadDirectory, final File serverWriteDirectory, final ServerMode mode)
            throws IOException {
        //this(serverReadDirectory, serverWriteDirectory, DEFAULT_TFTP_PORT, mode, null, null);
        this(serverReadDirectory, serverWriteDirectory, DEFAULT_TFTP_PORT, mode, jul, jul);
    }

    
    /* Start a TFTP Server on the specified port. Gets and Puts occur in the
     * specified directory.
     * The server will start in another thread, allowing this constructor to
     * return immediately.
     * If a get or a put comes in with a relative path that tries to get outside
     * of the serverDirectory, then the get or put will be denied.
     * GET_ONLY mode only allows gets, PUT_ONLY mode only allows puts, and
     * GET_AND_PUT allows both. Modes are defined as int constants in this
     * class.
     * @param serverReadDirectory directory for GET requests
     * @param serverWriteDirectory directory for PUT requests
     * @param port the port to use
     * @param mode A value as specified above.
     * @param log Stream to write log message to. If not provided, uses
     * System.out
     * @param errorLog Stream to write error messages to. If not provided, uses
     * System.err.
     * @throws IOException if the server directory is invalid or does not exist.
     */
    
    public TFTPServer(final File serverReadDirectory, final File serverWriteDirectory, final int port, final ServerMode mode,
            final Logger log, final Logger errorLog) throws IOException {
        port_ = port;
        mode_ = mode;
        //log_ = log == null ? nullStream : log;
        //logError_ = errorLog == null ? nullStream : errorLog;
        laddr_ = null;
        launch(serverReadDirectory, serverWriteDirectory);
    }

     /* Start a TFTP Server on the specified port. Gets and Puts occur in the
     * specified directory.
     * The server will start in another thread, allowing this constructor to
     * return immediately.
     * If a get or a put comes in with a relative path that tries to get outside
     * of the serverDirectory, then the get or put will be denied.
     * GET_ONLY mode only allows gets, PUT_ONLY mode only allows puts, and
     * GET_AND_PUT allows both. Modes are defined as int constants in this
     * class.
     * @param serverReadDirectory directory for GET requests
     * @param serverWriteDirectory directory for PUT requests
     * @param port The local port to bind to.
     * @param localaddr The local address to bind to.
     * @param mode A value as specified above.
     * @param log Stream to write log message to. If not provided, uses
     * System.out
     * @param errorLog Stream to write error messages to. If not provided, uses
     * System.err.
     * @throws IOException if the server directory is invalid or does not exist.
     */
    
    public TFTPServer(final File serverReadDirectory, final File serverWriteDirectory, final int port,
            final InetAddress localaddr, final ServerMode mode, final Logger log, final Logger errorLog)
            throws IOException {
        port_ = port;
        mode_ = mode;
        laddr_ = localaddr;
        //log_ = log == null ? nullStream : log;
        //logError_ = errorLog == null ? nullStream : errorLog;
        launch(serverReadDirectory, serverWriteDirectory);
    }

    /**
     * Start a TFTP Server on the specified port. Gets and Puts occur in the
     * specified directory.
     *
     * The server will start in another thread, allowing this constructor to
     * return immediately.
     *
     * If a get or a put comes in with a relative path that tries to get outside
     * of the serverDirectory, then the get or put will be denied.
     *
     * GET_ONLY mode only allows gets, PUT_ONLY mode only allows puts, and
     * GET_AND_PUT allows both. Modes are defined as int constants in this
     * class.
     *
     * @param serverReadDirectory directory for GET requests
     * @param serverWriteDirectory directory for PUT requests
     * @param port the port to use
     * @param localiface The local network interface to bind to. The interface's
     * first address wil be used.
     * @param mode A value as specified above.
     * @param log Stream to write log message to. If not provided, uses
     * System.out
     * @param errorLog Stream to write error messages to. If not provided, uses
     * System.err.
     * @throws IOException if the server directory is invalid or does not exist.
     */
    public TFTPServer(final File serverReadDirectory, final File serverWriteDirectory, final int port,
            final NetworkInterface localiface, final ServerMode mode, final Logger log, final Logger errorLog)
            throws IOException {
        mode_ = mode;
        port_ = port;
        InetAddress iaddr = null;
        if (localiface != null) {
            final Enumeration<InetAddress> ifaddrs = localiface.getInetAddresses();
            if (ifaddrs != null) {
                if (ifaddrs.hasMoreElements()) {
                    iaddr = ifaddrs.nextElement();
                }
            }
        }
        //log_ = log == null ? nullStream : log;
        //logError_ = errorLog == null ? nullStream : errorLog;
        laddr_ = iaddr;
        launch(serverReadDirectory, serverWriteDirectory);
    }

    /* Set the max number of retries in response to a timeout. Default 3. Min 0.
       @param retries number of retries, must be &gt; 0  */
    public void setMaxTimeoutRetries(final int retries) {
        if (retries < 0) {
            throw new RuntimeException("Invalid Value");
        }
        maxTimeoutRetries_ = retries;
    }

    /* Get the current value for maxTimeoutRetries
      @return the max allowed number of retries */
    public int getMaxTimeoutRetries() {
        return maxTimeoutRetries_;
    }

    /* Set the socket timeout in milliseconds used in transfers. Defaults to the
     * value here:
     * https://commons.apache.org/net/apidocs/org/apache/commons/net/tftp/TFTP.html#DEFAULT_TIMEOUT
     * (5000 at the time I write this) Min value of 10.
     * @param timeout the timeout; must be larger than 10
     */
    public void setSocketTimeout(final int timeout) {
        if (timeout < 10) {
            throw new RuntimeException("Invalid Value");
        }
        socketTimeout_ = timeout;
    }

    /* The current socket timeout used during transfers in milliseconds.
     * @return the timeout value
     */
    public int getSocketTimeout() {
        return socketTimeout_;
    }

    // start the server, throw an error if it can't start.
    private void launch(final File serverReadDirectory, final File serverWriteDirectory) throws IOException {
        jul.log(Level.INFO, "Starting TFTP Server on port " + port_ + ".  \nRead directory: "
                + serverReadDirectory + " \nWrite directory: " + serverWriteDirectory
                + " \nServer Mode is " + mode_);

        serverReadDirectory_ = serverReadDirectory.getCanonicalFile();
        if (!serverReadDirectory_.exists() || !serverReadDirectory.isDirectory()) {
            throw new IOException("The server read directory " + serverReadDirectory_
                    + " does not exist");
        }

        serverWriteDirectory_ = serverWriteDirectory.getCanonicalFile();
        if (!serverWriteDirectory_.exists() || !serverWriteDirectory.isDirectory()) {
            throw new IOException("The server write directory " + serverWriteDirectory_
                    + " does not exist");
        }

        serverTftp_ = new TFTP();

        // This is the value used in response to each client.
        socketTimeout_ = serverTftp_.getDefaultTimeout();

        // we want the server thread to listen forever.
        serverTftp_.setDefaultTimeout(0);

        if (laddr_ != null) {
            serverTftp_.open(port_, laddr_);
        } else {
            serverTftp_.open(port_);
        }

        serverThread = new Thread(this);
        serverThread.setDaemon(true);
        serverThread.start();
    }

    @Override
    protected void finalize() throws Throwable {
        shutdown();
    }

    /* check if the server thread is still running.
     * @return true if running, false if stopped.
     * @throws Exception throws the exception that stopped the server if the
     * server is stopped from an exception. */
    public boolean isRunning() throws Exception {
        if (shutdownServer && serverException != null) {
            throw serverException;
        }
        return !shutdownServer;
    }

    @Override
    public void run() {
        try {
            while (!shutdownServer) {
                TFTPPacket tftpPacket;

                tftpPacket = serverTftp_.receive();

                final TFTPTransfer tt = new TFTPTransfer(tftpPacket);
                synchronized (transfers_) {
                    transfers_.add(tt);
                }

                final Thread thread = new Thread(tt);
                thread.setDaemon(true);
                thread.start();
            }
        } catch (final Exception e) {
            if (!shutdownServer) {
                serverException = e;
                jul.log(Level.WARNING, "Unexpected Error in TFTP Server - Server shut down! + " + e);
            }
        } finally {
            shutdownServer = true; // set this to true, so the launching thread can check to see if it started.
            if (serverTftp_ != null && serverTftp_.isOpen()) {
                serverTftp_.close();
            }
        }
    }

    /* Stop the tftp server (and any currently running transfers) and release
     * all opened network resources.      */
    public void shutdown() {
        shutdownServer = true;

        synchronized (transfers_) {
            final Iterator<TFTPTransfer> it = transfers_.iterator();
            while (it.hasNext()) {
                it.next().shutdown();
            }
        }

        try {
            serverTftp_.close();
        } catch (final RuntimeException e) {
            // noop
        }

        try {
            serverThread.join();
        } catch (final InterruptedException e) {
            // we've done the best we could, return
        }
    }

    // An instance of an ongoing transfer.
    private class TFTPTransfer implements Runnable {

        private final TFTPPacket tftpPacket_;

        private boolean shutdownTransfer = false;

        TFTP transferTftp_ = null;

        public TFTPTransfer(final TFTPPacket tftpPacket) {
            tftpPacket_ = tftpPacket;
        }

        public void shutdown() {
            shutdownTransfer = true;
            try {
                transferTftp_.close();
            } catch (final RuntimeException e) {
                // noop
            }
        }

        @Override
        public void run() {
            try {
                transferTftp_ = newTFTP();

                transferTftp_.beginBufferedOps();
                transferTftp_.setDefaultTimeout(socketTimeout_);

                transferTftp_.open();

                if (tftpPacket_ instanceof TFTPReadRequestPacket) {
                    handleRead((TFTPReadRequestPacket) tftpPacket_);
                } else if (tftpPacket_ instanceof TFTPWriteRequestPacket) {
                    handleWrite((TFTPWriteRequestPacket) tftpPacket_);
                } else {
                    jul.log(Level.WARNING, "Unsupported TFTP request (" + tftpPacket_ + ") - ignored.");
                }
            } catch (final Exception e) {
                if (!shutdownTransfer) {
                    jul.log(Level.WARNING, "Unexpected Error in during TFTP file transfer.  Transfer aborted. " + e);
                }
            } finally {
                try {
                    if (transferTftp_ != null && transferTftp_.isOpen()) {
                        transferTftp_.endBufferedOps();
                        transferTftp_.close();
                    }
                } catch (final Exception e) {
                    // noop
                }
                synchronized (transfers_) {
                    transfers_.remove(this);
                }
            }
        }

        // Handle a tftp read request.
        private void handleRead(final TFTPReadRequestPacket trrp) throws IOException, TFTPPacketException {
            InputStream is = null;
            try {
                if (mode_ == ServerMode.PUT_ONLY) {
                    transferTftp_.bufferedSend(new TFTPErrorPacket(trrp.getAddress(), trrp
                            .getPort(), TFTPErrorPacket.ILLEGAL_OPERATION,
                            "Read not allowed by server."));
                    return;
                }

                try {
                    is = new BufferedInputStream(new FileInputStream(buildSafeFile(
                            serverReadDirectory_, trrp.getFilename(), false)));
                } catch (final FileNotFoundException e) {
                    transferTftp_.bufferedSend(new TFTPErrorPacket(trrp.getAddress(), trrp
                            .getPort(), TFTPErrorPacket.FILE_NOT_FOUND, e.getMessage()));
                    return;
                } catch (final Exception e) {
                    transferTftp_.bufferedSend(new TFTPErrorPacket(trrp.getAddress(), trrp
                            .getPort(), TFTPErrorPacket.UNDEFINED, e.getMessage()));
                    return;
                }

                if (trrp.getMode() == TFTP.NETASCII_MODE) {
                    is = new ToNetASCIIInputStream(is);
                }

                final byte[] temp = new byte[TFTPDataPacket.MAX_DATA_LENGTH];

                TFTPPacket answer;

                int block = 1;
                boolean sendNext = true;

                int readLength = TFTPDataPacket.MAX_DATA_LENGTH;

                TFTPDataPacket lastSentData = null;

                // We are reading a file, so when we read less than the
                // requested bytes, we know that we are at the end of the file.
                while (readLength == TFTPDataPacket.MAX_DATA_LENGTH && !shutdownTransfer) {
                    if (sendNext) {
                        readLength = is.read(temp);
                        if (readLength == -1) {
                            readLength = 0;
                        }

                        lastSentData = new TFTPDataPacket(trrp.getAddress(), trrp.getPort(), block,
                                temp, 0, readLength);
                        sendData(transferTftp_, lastSentData); // send the data
                    }

                    answer = null;

                    int timeoutCount = 0;

                    while (!shutdownTransfer
                            && (answer == null || !answer.getAddress().equals(trrp.getAddress()) || answer
                            .getPort() != trrp.getPort())) {
                        // listen for an answer.
                        if (answer != null) {
                            // The answer that we got didn't come from the
                            // expected source, fire back an error, and continue
                            // listening.
                            jul.log(Level.INFO, "TFTP Server ignoring message from unexpected source.");
                            transferTftp_.bufferedSend(new TFTPErrorPacket(answer.getAddress(),
                                    answer.getPort(), TFTPErrorPacket.UNKNOWN_TID,
                                    "Unexpected Host or Port"));
                        }
                        try {
                            answer = transferTftp_.bufferedReceive();
                        } catch (final SocketTimeoutException e) {
                            if (timeoutCount >= maxTimeoutRetries_) {
                                throw e;
                            }
                            // didn't get an ack for this data. need to resend
                            // it.
                            timeoutCount++;
                            transferTftp_.bufferedSend(lastSentData);
                            continue;
                        }
                    }

                    if (answer == null || !(answer instanceof TFTPAckPacket)) {
                        if (!shutdownTransfer) {
                            jul.log(Level.WARNING, "Unexpected response from tftp client during transfer ("
                                    + answer + ").  Transfer aborted.");
                        }
                        break;
                    }
                    // once we get here, we know we have an answer packet
                    // from the correct host.
                    final TFTPAckPacket ack = (TFTPAckPacket) answer;
                    if (ack.getBlockNumber() != block) {
                        /*
                         * The origional tftp spec would have called on us to resend the
                         * previous data here, however, that causes the SAS Syndrome.
                         * http://www.faqs.org/rfcs/rfc1123.html section 4.2.3.1 The modified
                         * spec says that we ignore a duplicate ack. If the packet was really
                         * lost, we will time out on receive, and resend the previous data at
                         * that point.
                         */
                        sendNext = false;
                    } else {
                        // send the next block
                        block++;
                        if (block > 65535) {
                            // wrap the block number
                            block = 0;
                        }
                        sendNext = true;
                    }
                }
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (final IOException e) {
                    // noop
                }
            }
        }

        // * handle a tftp write request.
        private void handleWrite(final TFTPWriteRequestPacket twrp) throws IOException,
                TFTPPacketException {
            OutputStream bos = null;
            try {
                if (mode_ == ServerMode.GET_ONLY) {
                    transferTftp_.bufferedSend(new TFTPErrorPacket(twrp.getAddress(), twrp
                            .getPort(), TFTPErrorPacket.ILLEGAL_OPERATION,
                            "Write not allowed by server."));
                    return;
                }

                int lastBlock = 0;
                final String fileName = twrp.getFilename();

                try {
                    final File temp = buildSafeFile(serverWriteDirectory_, fileName, true);
                    if (temp.exists()) {
                        transferTftp_.bufferedSend(new TFTPErrorPacket(twrp.getAddress(), twrp
                                .getPort(), TFTPErrorPacket.FILE_EXISTS, "File already exists"));
                        return;
                    }
                    bos = new BufferedOutputStream(new FileOutputStream(temp));

                    if (twrp.getMode() == TFTP.NETASCII_MODE) {
                        bos = new FromNetASCIIOutputStream(bos);
                    }
                } catch (final Exception e) {
                    transferTftp_.bufferedSend(new TFTPErrorPacket(twrp.getAddress(), twrp
                            .getPort(), TFTPErrorPacket.UNDEFINED, e.getMessage()));
                    return;
                }

                TFTPAckPacket lastSentAck = new TFTPAckPacket(twrp.getAddress(), twrp.getPort(), 0);
                sendData(transferTftp_, lastSentAck); // send the data

                while (true) {
                    // get the response - ensure it is from the right place.
                    TFTPPacket dataPacket = null;

                    int timeoutCount = 0;

                    while (!shutdownTransfer
                            && (dataPacket == null
                            || !dataPacket.getAddress().equals(twrp.getAddress()) || dataPacket
                            .getPort() != twrp.getPort())) {
                        // listen for an answer.
                        if (dataPacket != null) {
                            // The data that we got didn't come from the
                            // expected source, fire back an error, and continue
                            // listening.
                            jul.log(Level.INFO, "TFTP Server ignoring message from unexpected source.");
                            transferTftp_.bufferedSend(new TFTPErrorPacket(dataPacket.getAddress(),
                                    dataPacket.getPort(), TFTPErrorPacket.UNKNOWN_TID,
                                    "Unexpected Host or Port"));
                        }

                        try {
                            dataPacket = transferTftp_.bufferedReceive();
                        } catch (final SocketTimeoutException e) {
                            if (timeoutCount >= maxTimeoutRetries_) {
                                throw e;
                            }
                            // It didn't get our ack. Resend it.
                            transferTftp_.bufferedSend(lastSentAck);
                            timeoutCount++;
                            continue;
                        }
                    }

                    if (dataPacket != null && dataPacket instanceof TFTPWriteRequestPacket) {
                        // it must have missed our initial ack. Send another.
                        lastSentAck = new TFTPAckPacket(twrp.getAddress(), twrp.getPort(), 0);
                        transferTftp_.bufferedSend(lastSentAck);
                    } else if (dataPacket == null || !(dataPacket instanceof TFTPDataPacket)) {
                        if (!shutdownTransfer) {
                            jul.log(Level.WARNING, "Unexpected response from tftp client during transfer ("
                                    + dataPacket + ").  Transfer aborted.");
                        }
                        break;
                    } else {
                        final int block = ((TFTPDataPacket) dataPacket).getBlockNumber();
                        final byte[] data = ((TFTPDataPacket) dataPacket).getData();
                        final int dataLength = ((TFTPDataPacket) dataPacket).getDataLength();
                        final int dataOffset = ((TFTPDataPacket) dataPacket).getDataOffset();

                        if (block > lastBlock || lastBlock == 65535 && block == 0) {
                            // it might resend a data block if it missed our ack
                            // - don't rewrite the block.
                            bos.write(data, dataOffset, dataLength);
                            lastBlock = block;
                        }

                        lastSentAck = new TFTPAckPacket(twrp.getAddress(), twrp.getPort(), block);
                        sendData(transferTftp_, lastSentAck); // send the data
                        if (dataLength < TFTPDataPacket.MAX_DATA_LENGTH) {
                            // end of stream signal - The tranfer is complete.
                            bos.close();

                            // But my ack may be lost - so listen to see if I
                            // need to resend the ack.
                            for (int i = 0; i < maxTimeoutRetries_; i++) {
                                try {
                                    dataPacket = transferTftp_.bufferedReceive();
                                } catch (final SocketTimeoutException e) {
                                    // this is the expected route - the client
                                    // shouldn't be sending any more packets.
                                    break;
                                }

                                if (dataPacket != null
                                        && (!dataPacket.getAddress().equals(twrp.getAddress()) || dataPacket
                                        .getPort() != twrp.getPort())) {
                                    // make sure it was from the right client...
                                    transferTftp_
                                            .bufferedSend(new TFTPErrorPacket(dataPacket
                                                    .getAddress(), dataPacket.getPort(),
                                                    TFTPErrorPacket.UNKNOWN_TID,
                                                    "Unexpected Host or Port"));
                                } else {
                                    // This means they sent us the last
                                    // datapacket again, must have missed our
                                    // ack. resend it.
                                    transferTftp_.bufferedSend(lastSentAck);
                                }
                            }

                            // all done.
                            break;
                        }
                    }
                }
            } finally {
                if (bos != null) {
                    bos.close();
                }
            }
        }

        /* Utility method to make sure that paths provided by tftp clients do not get outside of the
         * serverRoot directory.         */
        private File buildSafeFile(final File serverDirectory, final String fileName, final boolean createSubDirs)
                throws IOException {
            File temp = new File(serverDirectory, fileName);
            temp = temp.getCanonicalFile();

            if (!isSubdirectoryOf(serverDirectory, temp)) {
                throw new IOException("Cannot access files outside of tftp server root.");
            }

            // ensure directory exists (if requested)
            if (createSubDirs) {
                createDirectory(temp.getParentFile());
            }

            return temp;
        }

        // * recursively create subdirectories
        private void createDirectory(final File file) throws IOException {
            final File parent = file.getParentFile();
            if (parent == null) {
                throw new IOException("Unexpected error creating requested directory");
            }
            if (!parent.exists()) {
                // recurse...
                createDirectory(parent);
            }

            if (parent.isDirectory()) {
                if (file.isDirectory()) {
                    return;
                }
                final boolean result = file.mkdir();
                if (!result) {
                    throw new IOException("Couldn't create requested directory");
                }
            } else {
                throw new IOException(
                        "Invalid directory path - file in the way of requested folder");
            }
        }

        // * recursively check to see if one directory is a parent of another.
        private boolean isSubdirectoryOf(final File parent, final File child) {
            final File childsParent = child.getParentFile();
            if (childsParent == null) {
                return false;
            }
            if (childsParent.equals(parent)) {
                return true;
            }
            return isSubdirectoryOf(parent, childsParent);
        }
    }

    // * Allow test code to customise the TFTP instance
    TFTP newTFTP() {
        return new TFTP();
    }

    /*
     * Also allow customisation of sending data/ack so can generate errors if needed
     */
    void sendData(final TFTP tftp, final TFTPPacket data) throws IOException {
        tftp.bufferedSend(data);
    }

    public static void go() {
        //TFTPServer ts;
        try {
            ts = new TFTPServer(new File(tfTftpFolder.getText()), new File(tfTftpFolder.getText()), ServerMode.GET_AND_PUT);
            ts.setSocketTimeout(2000);
            jul.log(Level.INFO, "Fully multi-threaded TFTP Server running, folder = " + tfTftpFolder.getText());
            taTftpResult.setText("");
            taTftpResult.append("\nFully multi-threaded TFTP Server running, folder = " + tfTftpFolder.getText());
            taTftpResult.append("\nserver port = " + ts.port_);
            taTftpResult.append("\nserver mode = " + ts.mode_);
            taTftpResult.append("\nserver read-folder = " + ts.serverReadDirectory_);
            taTftpResult.append("\nserver write-folder = " + ts.serverWriteDirectory_);
            taTftpResult.append("\nserver main-thread name = " + ts.serverThread.getName());
        } catch (Exception ff) {
            //java.util.logging.Logger.getLogger(TFTPServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            taTftpResult.setText("");
            taTftpResult.append("\nException at start TFTP-server: " + ff.getMessage());
            btnBooleanTftp.setText("Run TFTP-server ");
            btnBooleanTftp.setIcon(iconOn);
            btnTftpSave.setEnabled(true);            
        }        
    }
    
    public static void stop() {
        try {
            ts.shutdown();
            taTftpResult.setText("");
            taTftpResult.append("\n\nServer running = " + ts.isRunning());
            //ts=null;
            //taTftpResult.append("\nServer shut down.");            
        } catch (Exception ff) {
            //java.util.logging.Logger.getLogger(TFTPServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            taTftpResult.setText("");
            taTftpResult.append("\nException at stop TFTP-server: " + ff.getMessage()); 
            ts=null;
        }
    }

}
