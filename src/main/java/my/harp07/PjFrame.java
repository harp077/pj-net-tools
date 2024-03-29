package my.harp07;

import my.harp07.tcp.TelnetNNM_Thread;
import my.harp07.tcp.HostTcpScanner;
import my.harp07.udp.snmp.PjSnmpOidHelp;
import my.harp07.udp.snmp.PjSnmpGet;
import my.harp07.udp.UdpFlood_Thread;
import my.harp07.udp.TFTPServer;
import my.harp07.udp.SimpleNTPServer;
import my.harp07.udp.PjSyslog;
import my.harp07.udp.PjDns;
import my.harp07.icmp.PjTrace;
import my.harp07.icmp.PjPingScanner;
import my.harp07.icmp.PjPingFlood;
import my.harp07.icmp.PjPing;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import static my.harp07.GenericPJ.ipv;
import static my.harp07.GenericPJ.ping_remark;
import static my.harp07.PjCalc.CIDRS_MASKS;
//import static my.harp07.icmp.PjPing.COUNTS;
//import static my.harp07.icmp.PjPing.TIMEOUTS;
import static my.harp07.icmp.PjPingFlood.floodTIMEOUTS;
//import static my.harp07.icmp.PjPingScanner.arrayUpDown;
//import static my.harp07.icmp.PjPingScanner.scannerCIDRS_MASKS;
//import static my.harp07.icmp.PjPingScanner.scannerTIMEOUTS;
import my.harp07.tcp.FTPServer;
import my.harp07.tcp.NetTcpScanner;
import my.harp07.tcp.PingTCP;
import my.harp07.udp.NetDnsScanner;
import my.harp07.udp.snmp.NetSnmpScanner;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jrobin.mrtg.client.ClientMRTG;

public class PjFrame extends javax.swing.JFrame {

    public static PjFrame frame;
    public static Thread mrtgThread;
    public static int FW = 960;
    public static int FH = 600;
    public static ImageIcon iconOn = new ImageIcon(PjFrame.class.getResource("/img/go-green-krug-16.png"));
    public static ImageIcon iconOf = new ImageIcon(PjFrame.class.getResource("/img/stop-16.png"));
    public static List<String> lookAndFeelsDisplay = new ArrayList<>();
    public static List<String> lookAndFeelsRealNames = new ArrayList<>();
    public static String currentLAF = "de.muntjak.tinylookandfeel.TinyLookAndFeel";
    public static String currentTheme = "/themes/Default.theme";
    public static List<String> tinyTemes = new ArrayList<>();
    public static String zagolovok = "Pure Java Network Tools,  v1.0.105, build 28-12-2023";

    public PjFrame() {
        initComponents();
        this.setTitle(zagolovok);
        this.comboPingCounts.setModel(new javax.swing.DefaultComboBoxModel(PjPing.COUNTS));
        this.comboPingTimeouts.setModel(new javax.swing.DefaultComboBoxModel(PjPing.TIMEOUTS));
        //
        this.comboPingFloodTimeouts.setModel(new javax.swing.DefaultComboBoxModel(floodTIMEOUTS));
        this.comboCalcMasks.setModel(new javax.swing.DefaultComboBoxModel(CIDRS_MASKS));
        //
        this.comboPingScannerTimeouts.setModel(new javax.swing.DefaultComboBoxModel(PjPingScanner.scannerTIMEOUTS));
        this.comboPingScannerMasks.setModel(new javax.swing.DefaultComboBoxModel(PjPingScanner.scannerCIDRS_MASKS));
        this.comboPingScannerShow.setModel(new javax.swing.DefaultComboBoxModel(PjPingScanner.arrayUpDown));
        //
        this.comboNetDnsScannerMasks.setModel(new javax.swing.DefaultComboBoxModel(NetDnsScanner.scannerCIDRS_MASKS));
        this.comboNetDnsScannerShow.setModel(new javax.swing.DefaultComboBoxModel(NetDnsScanner.arrayUpDown));        
        //
        this.comboHostTcpScannerTimeouts.setModel(new javax.swing.DefaultComboBoxModel(HostTcpScanner.scannerTIMEOUTS));
        this.comboHostTcpScannerShow.setModel(new javax.swing.DefaultComboBoxModel(HostTcpScanner.arrayUpDown));          
        //
        this.comboNetTcpScannerTimeouts.setModel(new javax.swing.DefaultComboBoxModel(NetTcpScanner.scannerTIMEOUTS));
        this.comboNetTcpScannerMasks.setModel(new javax.swing.DefaultComboBoxModel(NetTcpScanner.scannerCIDRS_MASKS));
        this.comboNetTcpScannerShow.setModel(new javax.swing.DefaultComboBoxModel(NetTcpScanner.arrayUpDown));  
        //
        this.comboNetSnmpScannerTimeouts.setModel(new javax.swing.DefaultComboBoxModel(NetSnmpScanner.scannerTIMEOUTS));
        this.comboNetSnmpScannerMasks.setModel(new javax.swing.DefaultComboBoxModel(NetSnmpScanner.scannerCIDRS_MASKS));
        this.comboNetSnmpScannerShow.setModel(new javax.swing.DefaultComboBoxModel(NetSnmpScanner.arrayUpDown));
        this.comboNetSnmpScannerVersion.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"1", "2c"}));
        this.comboNetSnmpScannerOID.setModel(new javax.swing.DefaultComboBoxModel(NetSnmpScanner.mainListSNMP_OIDS.toArray()));
        //
        this.comboSnmpVersion.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"1", "2c"}));
        ImageIcon icone = new ImageIcon(getClass().getResource("/FrameIcon-3.png"));
        this.setIconImage(icone.getImage());
        this.nizInfoLabel.setText(zagolovok);
        this.tfTelnetPort.setText("23");
        //this.tfTelnetPort.setColumns(11);
        //this.epAbout.setEditorKit(new HTMLEditorKit());
        this.epAbout.setContentType("text/html");
        this.epAbout.setText(GenericPJ.about());
        this.epAbout.setEditable(false);//so its not editable
        this.epAbout.setOpaque(false);//so we dont see whit background        
        this.epAbout.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent hle) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                    System.out.println(hle.getURL());
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(hle.getURL().toURI());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        PjSnmpOidHelp.runSnmpHelp(taSnmpOidHelp);
        taSnmpOidHelp.setEditable(false);
        taPingResult.setText(ping_remark);
        taPingScannerResult.setText(ping_remark);
        taPingFloodResult.setText(ping_remark);
        //spinnerNTP.setValue(123);
        spinnerNTP.setModel(new SpinnerNumberModel(123, 1, 65535, 1));
    }

    public static void MyInstLF(String lf) {
        //UIManager.installLookAndFeel(lf,lf);  
        lookAndFeelsDisplay.add(lf);
        lookAndFeelsRealNames.add(lf);
    }

    public static void InstallLF() {
        MyInstLF("de.muntjak.tinylookandfeel.TinyLookAndFeel");
        MyInstLF("javax.swing.plaf.metal.MetalLookAndFeel");
        /*MyInstLF("org.pushingpixels.substance.api.skin.SubstanceSaharaLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceAutumnLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceCremeCoffeeLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceModerateLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceMagellanLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceMistAquaLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceMistSilverLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceOfficeBlue2007LookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceOfficeBlack2007LookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceOfficeSilver2007LookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceNebulaLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceNebulaBrickWallLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceGeminiLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceDustCoffeeLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceDustLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceRavenLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceGraphiteAquaLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceGraphiteGlassLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceBusinessBlueSteelLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceBusinessLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceMarinerLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceCeruleanLookAndFeel");
        MyInstLF("org.pushingpixels.substance.api.skin.SubstanceTwilightLookAndFeel");*/
        //
        /*MyInstLF("com.jtattoo.plaf.acryl.AcrylLookAndFeel"); 
        MyInstLF("com.jtattoo.plaf.aero.AeroLookAndFeel");
        MyInstLF("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        MyInstLF("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
        MyInstLF("com.jtattoo.plaf.fast.FastLookAndFeel");
        MyInstLF("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
        MyInstLF("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
        MyInstLF("com.jtattoo.plaf.mint.MintLookAndFeel");
        MyInstLF("com.jtattoo.plaf.noire.NoireLookAndFeel"); 
        MyInstLF("com.jtattoo.plaf.smart.SmartLookAndFeel");
        MyInstLF("com.jtattoo.plaf.luna.LunaLookAndFeel");
        MyInstLF("com.jtattoo.plaf.texture.TextureLookAndFeel");
        MyInstLF("com.jtattoo.plaf.graphite.GraphiteLookAndFeel"); */      
    }

    public static void setLF() {
        if (currentLAF.contains("tinyl")) {
            try {
                de.muntjak.tinylookandfeel.Theme.loadTheme(PjFrame.class.getResource(currentTheme));
            } catch (Exception ex) {
                Logger.getLogger(PjFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            UIManager.setLookAndFeel(currentLAF);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(PjFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        SwingUtilities.updateComponentTreeUI(frame);
        //frame.pack();
        //frame.setSize(FW, FH);
    }

    public static void telnetClientHandler() {
        int port = 0;
        if (NumberUtils.isParsable(tfTelnetPort.getText().trim())) {
            port = Integer.parseInt(tfTelnetPort.getText().trim());
        } else {
            JOptionPane.showMessageDialog(frame, "Wrong port !", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (ipv.isValid(tfTelnetAdres.getText().trim()) && port > 0 && port < 65536) {
            new TelnetNNM_Thread(tfTelnetAdres.getText().trim(), Integer.parseInt(tfTelnetPort.getText().trim()));
        } else {
            JOptionPane.showMessageDialog(frame, "Wrong IP or port !", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        taPingResult = new javax.swing.JTextArea();
        jToolBar2 = new javax.swing.JToolBar();
        jSeparator13 = new javax.swing.JToolBar.Separator();
        jLabel2 = new javax.swing.JLabel();
        tfPingInput = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        comboPingTimeouts = new javax.swing.JComboBox<>();
        jSeparator17 = new javax.swing.JToolBar.Separator();
        jLabel7 = new javax.swing.JLabel();
        comboPingCounts = new javax.swing.JComboBox<>();
        jSeparator18 = new javax.swing.JToolBar.Separator();
        jToolBar3 = new javax.swing.JToolBar();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnPingRun = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btnSavePing = new javax.swing.JButton();
        jSeparator26 = new javax.swing.JToolBar.Separator();
        jScrollPane14 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        taTraceResult = new javax.swing.JTextArea();
        jToolBar13 = new javax.swing.JToolBar();
        jLabel9 = new javax.swing.JLabel();
        tfTraceInput = new javax.swing.JTextField();
        jSeparator37 = new javax.swing.JToolBar.Separator();
        jToolBar14 = new javax.swing.JToolBar();
        jSeparator38 = new javax.swing.JToolBar.Separator();
        btnTraceRun = new javax.swing.JButton();
        jSeparator39 = new javax.swing.JToolBar.Separator();
        btnSaveTrace = new javax.swing.JButton();
        jSeparator40 = new javax.swing.JToolBar.Separator();
        jScrollPane25 = new javax.swing.JScrollPane();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane26 = new javax.swing.JScrollPane();
        taPingFloodResult = new javax.swing.JTextArea();
        jToolBar23 = new javax.swing.JToolBar();
        jLabel21 = new javax.swing.JLabel();
        tfPingFloodIP = new javax.swing.JTextField();
        jSeparator73 = new javax.swing.JToolBar.Separator();
        jLabel22 = new javax.swing.JLabel();
        comboPingFloodTimeouts = new javax.swing.JComboBox<>();
        jSeparator75 = new javax.swing.JToolBar.Separator();
        jToolBar24 = new javax.swing.JToolBar();
        jSeparator72 = new javax.swing.JToolBar.Separator();
        btnBooleanPingFlood = new javax.swing.JToggleButton();
        jScrollPane19 = new javax.swing.JScrollPane();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane20 = new javax.swing.JScrollPane();
        taPingScannerResult = new javax.swing.JTextArea();
        jToolBar19 = new javax.swing.JToolBar();
        jSeparator51 = new javax.swing.JToolBar.Separator();
        jLabel13 = new javax.swing.JLabel();
        tfPingScannerInput = new javax.swing.JTextField();
        jSeparator52 = new javax.swing.JToolBar.Separator();
        jLabel14 = new javax.swing.JLabel();
        comboPingScannerMasks = new javax.swing.JComboBox<>();
        jSeparator53 = new javax.swing.JToolBar.Separator();
        jLabel3 = new javax.swing.JLabel();
        comboPingScannerTimeouts = new javax.swing.JComboBox<>();
        jSeparator57 = new javax.swing.JToolBar.Separator();
        jLabel15 = new javax.swing.JLabel();
        comboPingScannerShow = new javax.swing.JComboBox<>();
        jSeparator58 = new javax.swing.JToolBar.Separator();
        jToolBar20 = new javax.swing.JToolBar();
        jSeparator54 = new javax.swing.JToolBar.Separator();
        btnPingScannerRun = new javax.swing.JButton();
        jSeparator55 = new javax.swing.JToolBar.Separator();
        btnPingScannerSave = new javax.swing.JButton();
        jSeparator56 = new javax.swing.JToolBar.Separator();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        taHostTcpScannerResult = new javax.swing.JTextArea();
        jToolBar5 = new javax.swing.JToolBar();
        jSeparator22 = new javax.swing.JToolBar.Separator();
        jLabel4 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        tfHostTcpScannerIP = new javax.swing.JTextField();
        jSeparator112 = new javax.swing.JToolBar.Separator();
        jLabel44 = new javax.swing.JLabel();
        comboHostTcpScannerType = new javax.swing.JComboBox<>();
        jSeparator105 = new javax.swing.JToolBar.Separator();
        jLabel43 = new javax.swing.JLabel();
        comboHostTcpScannerTimeouts = new javax.swing.JComboBox<>();
        jSeparator111 = new javax.swing.JToolBar.Separator();
        jLabel41 = new javax.swing.JLabel();
        comboHostTcpScannerShow = new javax.swing.JComboBox<>();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jToolBar6 = new javax.swing.JToolBar();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        btnHostTcpScannerRun = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        btnHostTcpScannerSave = new javax.swing.JButton();
        jSeparator30 = new javax.swing.JToolBar.Separator();
        jScrollPane31 = new javax.swing.JScrollPane();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane32 = new javax.swing.JScrollPane();
        taPingTCPout = new javax.swing.JTextArea();
        jToolBar25 = new javax.swing.JToolBar();
        jSeparator82 = new javax.swing.JToolBar.Separator();
        jLabel25 = new javax.swing.JLabel();
        jSeparator83 = new javax.swing.JToolBar.Separator();
        tfPingTCPip = new javax.swing.JTextField();
        jSeparator84 = new javax.swing.JToolBar.Separator();
        jLabel26 = new javax.swing.JLabel();
        tfPingTCPports = new javax.swing.JTextField();
        jSeparator88 = new javax.swing.JToolBar.Separator();
        jToolBar28 = new javax.swing.JToolBar();
        jSeparator85 = new javax.swing.JToolBar.Separator();
        btnTcpPingRun = new javax.swing.JButton();
        jSeparator86 = new javax.swing.JToolBar.Separator();
        btnSaveTcpPing = new javax.swing.JButton();
        jSeparator87 = new javax.swing.JToolBar.Separator();
        jScrollPane35 = new javax.swing.JScrollPane();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane36 = new javax.swing.JScrollPane();
        taNetTcpScannerResult = new javax.swing.JTextArea();
        jToolBar30 = new javax.swing.JToolBar();
        jLabel28 = new javax.swing.JLabel();
        tfNetTcpScannerIP = new javax.swing.JTextField();
        jSeparator76 = new javax.swing.JToolBar.Separator();
        jLabel29 = new javax.swing.JLabel();
        comboNetTcpScannerMasks = new javax.swing.JComboBox<>();
        jSeparator92 = new javax.swing.JToolBar.Separator();
        jLabel32 = new javax.swing.JLabel();
        tfNetTcpScannerPort = new javax.swing.JTextField();
        jSeparator33 = new javax.swing.JToolBar.Separator();
        jLabel30 = new javax.swing.JLabel();
        comboNetTcpScannerTimeouts = new javax.swing.JComboBox<>();
        jSeparator93 = new javax.swing.JToolBar.Separator();
        jLabel31 = new javax.swing.JLabel();
        comboNetTcpScannerShow = new javax.swing.JComboBox<>();
        jSeparator35 = new javax.swing.JToolBar.Separator();
        jToolBar31 = new javax.swing.JToolBar();
        jSeparator95 = new javax.swing.JToolBar.Separator();
        btnNetTcpScannerRun = new javax.swing.JButton();
        jSeparator96 = new javax.swing.JToolBar.Separator();
        btnNetTcpScannerSave = new javax.swing.JButton();
        jSeparator97 = new javax.swing.JToolBar.Separator();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        taCalcResult = new javax.swing.JTextArea();
        jToolBar9 = new javax.swing.JToolBar();
        jSeparator20 = new javax.swing.JToolBar.Separator();
        jLabel6 = new javax.swing.JLabel();
        tfCalcInput = new javax.swing.JTextField();
        jSeparator19 = new javax.swing.JToolBar.Separator();
        jLabel8 = new javax.swing.JLabel();
        comboCalcMasks = new javax.swing.JComboBox<>();
        jSeparator14 = new javax.swing.JToolBar.Separator();
        jToolBar10 = new javax.swing.JToolBar();
        jSeparator15 = new javax.swing.JToolBar.Separator();
        btnCalcRun = new javax.swing.JButton();
        jSeparator16 = new javax.swing.JToolBar.Separator();
        btnSaveCalc = new javax.swing.JButton();
        jSeparator32 = new javax.swing.JToolBar.Separator();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        taDnsResult = new javax.swing.JTextArea();
        jToolBar7 = new javax.swing.JToolBar();
        jSeparator21 = new javax.swing.JToolBar.Separator();
        jLabel5 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JToolBar.Separator();
        tfDnsInput = new javax.swing.JTextField();
        jSeparator10 = new javax.swing.JToolBar.Separator();
        jToolBar8 = new javax.swing.JToolBar();
        jSeparator11 = new javax.swing.JToolBar.Separator();
        btnDnsRun = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JToolBar.Separator();
        btnSaveDns = new javax.swing.JButton();
        jSeparator31 = new javax.swing.JToolBar.Separator();
        jScrollPane39 = new javax.swing.JScrollPane();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane40 = new javax.swing.JScrollPane();
        taNetDnsScannerResult = new javax.swing.JTextArea();
        jToolBar34 = new javax.swing.JToolBar();
        jSeparator64 = new javax.swing.JToolBar.Separator();
        jLabel39 = new javax.swing.JLabel();
        tfNetDnsScannerIP = new javax.swing.JTextField();
        jSeparator104 = new javax.swing.JToolBar.Separator();
        jLabel40 = new javax.swing.JLabel();
        comboNetDnsScannerMasks = new javax.swing.JComboBox<>();
        jSeparator106 = new javax.swing.JToolBar.Separator();
        jLabel42 = new javax.swing.JLabel();
        comboNetDnsScannerShow = new javax.swing.JComboBox<>();
        jSeparator107 = new javax.swing.JToolBar.Separator();
        jToolBar35 = new javax.swing.JToolBar();
        jSeparator108 = new javax.swing.JToolBar.Separator();
        btnNetDnsScannerRun = new javax.swing.JButton();
        jSeparator109 = new javax.swing.JToolBar.Separator();
        btnNetDnsScannerSave = new javax.swing.JButton();
        jSeparator110 = new javax.swing.JToolBar.Separator();
        jScrollPane16 = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        jToolBar15 = new javax.swing.JToolBar();
        jSeparator41 = new javax.swing.JToolBar.Separator();
        jLabel11 = new javax.swing.JLabel();
        jSeparator42 = new javax.swing.JToolBar.Separator();
        tfTelnetAdres = new javax.swing.JTextField();
        jSeparator59 = new javax.swing.JToolBar.Separator();
        jLabel16 = new javax.swing.JLabel();
        jSeparator60 = new javax.swing.JToolBar.Separator();
        tfTelnetPort = new javax.swing.JTextField();
        jSeparator43 = new javax.swing.JToolBar.Separator();
        jToolBar16 = new javax.swing.JToolBar();
        jSeparator44 = new javax.swing.JToolBar.Separator();
        btnTelnetRun = new javax.swing.JButton();
        jScrollPane17 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        taArpResult = new javax.swing.JTextArea();
        jToolBar17 = new javax.swing.JToolBar();
        jSeparator45 = new javax.swing.JToolBar.Separator();
        jLabel12 = new javax.swing.JLabel();
        jSeparator46 = new javax.swing.JToolBar.Separator();
        tfArpInput = new javax.swing.JTextField();
        jSeparator47 = new javax.swing.JToolBar.Separator();
        jToolBar18 = new javax.swing.JToolBar();
        jSeparator48 = new javax.swing.JToolBar.Separator();
        btnArpRun = new javax.swing.JButton();
        jSeparator49 = new javax.swing.JToolBar.Separator();
        btnArpALL = new javax.swing.JButton();
        jSeparator23 = new javax.swing.JToolBar.Separator();
        btnSaveArp = new javax.swing.JButton();
        jSeparator50 = new javax.swing.JToolBar.Separator();
        jScrollPane21 = new javax.swing.JScrollPane();
        jPanel11 = new javax.swing.JPanel();
        jToolBar21 = new javax.swing.JToolBar();
        jSeparator61 = new javax.swing.JToolBar.Separator();
        jLabel17 = new javax.swing.JLabel();
        tfSnmpGetIP = new javax.swing.JTextField();
        jSeparator63 = new javax.swing.JToolBar.Separator();
        jLabel18 = new javax.swing.JLabel();
        tfSnmpGetCommunity = new javax.swing.JTextField();
        jSeparator65 = new javax.swing.JToolBar.Separator();
        jLabel19 = new javax.swing.JLabel();
        tfSnmpGetOID = new javax.swing.JTextField();
        jSeparator70 = new javax.swing.JToolBar.Separator();
        jLabel20 = new javax.swing.JLabel();
        comboSnmpVersion = new javax.swing.JComboBox<>();
        jSeparator62 = new javax.swing.JToolBar.Separator();
        jToolBar22 = new javax.swing.JToolBar();
        jSeparator66 = new javax.swing.JToolBar.Separator();
        btnSnmpGet = new javax.swing.JButton();
        jSeparator69 = new javax.swing.JToolBar.Separator();
        btnSnmpMibsAll = new javax.swing.JButton();
        jSeparator67 = new javax.swing.JToolBar.Separator();
        btnSnmpMibsStd = new javax.swing.JButton();
        jScrollPane22 = new javax.swing.JScrollPane();
        taSnmpGet = new javax.swing.JTextArea();
        jScrollPane37 = new javax.swing.JScrollPane();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane38 = new javax.swing.JScrollPane();
        taNetSnmpScannerResult = new javax.swing.JTextArea();
        jToolBar32 = new javax.swing.JToolBar();
        jLabel33 = new javax.swing.JLabel();
        tfNetSnmpScannerIP = new javax.swing.JTextField();
        jSeparator94 = new javax.swing.JToolBar.Separator();
        jLabel34 = new javax.swing.JLabel();
        comboNetSnmpScannerMasks = new javax.swing.JComboBox<>();
        jSeparator98 = new javax.swing.JToolBar.Separator();
        jLabel35 = new javax.swing.JLabel();
        tfNetSnmpScannerCommunity = new javax.swing.JTextField();
        jSeparator36 = new javax.swing.JToolBar.Separator();
        jLabel38 = new javax.swing.JLabel();
        comboNetSnmpScannerVersion = new javax.swing.JComboBox<>();
        jSeparator103 = new javax.swing.JToolBar.Separator();
        jLabel36 = new javax.swing.JLabel();
        comboNetSnmpScannerTimeouts = new javax.swing.JComboBox<>();
        jSeparator99 = new javax.swing.JToolBar.Separator();
        jLabel37 = new javax.swing.JLabel();
        comboNetSnmpScannerShow = new javax.swing.JComboBox<>();
        jSeparator68 = new javax.swing.JToolBar.Separator();
        jToolBar33 = new javax.swing.JToolBar();
        jSeparator100 = new javax.swing.JToolBar.Separator();
        btnNetSnmpScannerRun = new javax.swing.JButton();
        jSeparator101 = new javax.swing.JToolBar.Separator();
        btnNetSnmpScannerSave = new javax.swing.JButton();
        jSeparator102 = new javax.swing.JToolBar.Separator();
        comboNetSnmpScannerOID = new javax.swing.JComboBox<>();
        jScrollPane23 = new javax.swing.JScrollPane();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane24 = new javax.swing.JScrollPane();
        taSnmpOidHelp = new javax.swing.JTextArea();
        jScrollPane10 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        taSyslogResult = new javax.swing.JTextArea();
        jToolBar11 = new javax.swing.JToolBar();
        jSeparator24 = new javax.swing.JToolBar.Separator();
        jLabel10 = new javax.swing.JLabel();
        tfSyslogInput = new javax.swing.JTextField();
        jSeparator29 = new javax.swing.JToolBar.Separator();
        jToolBar12 = new javax.swing.JToolBar();
        jSeparator27 = new javax.swing.JToolBar.Separator();
        btnBooleanSyslog = new javax.swing.JToggleButton();
        jSeparator28 = new javax.swing.JToolBar.Separator();
        btnSyslogReset = new javax.swing.JButton();
        jSeparator25 = new javax.swing.JToolBar.Separator();
        btnSyslogSave = new javax.swing.JButton();
        jScrollPane27 = new javax.swing.JScrollPane();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane28 = new javax.swing.JScrollPane();
        taTftpResult = new javax.swing.JTextArea();
        jToolBar26 = new javax.swing.JToolBar();
        jSeparator77 = new javax.swing.JToolBar.Separator();
        btnBooleanTftp = new javax.swing.JToggleButton();
        jSeparator79 = new javax.swing.JToolBar.Separator();
        btnTftpSave = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        tfTftpFolder = new javax.swing.JTextField();
        jSeparator78 = new javax.swing.JToolBar.Separator();
        jScrollPane33 = new javax.swing.JScrollPane();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane34 = new javax.swing.JScrollPane();
        taFtpResult = new javax.swing.JTextArea();
        jToolBar29 = new javax.swing.JToolBar();
        jSeparator89 = new javax.swing.JToolBar.Separator();
        btnBooleanFtp = new javax.swing.JToggleButton();
        jSeparator90 = new javax.swing.JToolBar.Separator();
        btnFtpSave = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        tfFtpFolder = new javax.swing.JTextField();
        jSeparator91 = new javax.swing.JToolBar.Separator();
        jScrollPane29 = new javax.swing.JScrollPane();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane30 = new javax.swing.JScrollPane();
        taNtpResult = new javax.swing.JTextArea();
        jToolBar27 = new javax.swing.JToolBar();
        jSeparator80 = new javax.swing.JToolBar.Separator();
        btnBooleanNtp = new javax.swing.JToggleButton();
        jSeparator81 = new javax.swing.JToolBar.Separator();
        jLabel24 = new javax.swing.JLabel();
        spinnerNTP = new javax.swing.JSpinner();
        jSeparator71 = new javax.swing.JToolBar.Separator();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        taLocalResult = new javax.swing.JTextArea();
        jScrollPane13 = new javax.swing.JScrollPane();
        epAbout = new javax.swing.JEditorPane();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnUdpFlood = new javax.swing.JButton();
        jSeparator74 = new javax.swing.JToolBar.Separator();
        btnMRTG = new javax.swing.JButton();
        jToolBar4 = new javax.swing.JToolBar();
        jSeparator34 = new javax.swing.JToolBar.Separator();
        nizInfoLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Pinger"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane6.setBorder(javax.swing.BorderFactory.createTitledBorder("Result:"));

        taPingResult.setEditable(false);
        taPingResult.setColumns(20);
        taPingResult.setRows(5);
        jScrollPane6.setViewportView(taPingResult);

        jPanel1.add(jScrollPane6, java.awt.BorderLayout.CENTER);

        jToolBar2.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar2.setFloatable(false);
        jToolBar2.add(jSeparator13);

        jLabel2.setText(" IP/DNS:  ");
        jToolBar2.add(jLabel2);

        tfPingInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfPingInputKeyPressed(evt);
            }
        });
        jToolBar2.add(tfPingInput);
        jToolBar2.add(jSeparator2);

        jLabel1.setText("TimeOut, ms: ");
        jToolBar2.add(jLabel1);

        comboPingTimeouts.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jToolBar2.add(comboPingTimeouts);
        jToolBar2.add(jSeparator17);

        jLabel7.setText("Count: ");
        jToolBar2.add(jLabel7);

        comboPingCounts.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jToolBar2.add(comboPingCounts);
        jToolBar2.add(jSeparator18);

        jPanel1.add(jToolBar2, java.awt.BorderLayout.NORTH);

        jToolBar3.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar3.setFloatable(false);
        jToolBar3.add(jSeparator3);

        btnPingRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnPingRun.setText("Run Pinger ");
        btnPingRun.setFocusable(false);
        btnPingRun.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnPingRun.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPingRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPingRunActionPerformed(evt);
            }
        });
        jToolBar3.add(btnPingRun);
        jToolBar3.add(jSeparator4);

        btnSavePing.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save-16.png"))); // NOI18N
        btnSavePing.setText("Save Result ");
        btnSavePing.setFocusable(false);
        btnSavePing.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSavePing.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSavePing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSavePingActionPerformed(evt);
            }
        });
        jToolBar3.add(btnSavePing);
        jToolBar3.add(jSeparator26);

        jPanel1.add(jToolBar3, java.awt.BorderLayout.SOUTH);

        jScrollPane1.setViewportView(jPanel1);

        jTabbedPane1.addTab("ICMP-Ping", new javax.swing.ImageIcon(getClass().getResource("/img/ping-16.png")), jScrollPane1); // NOI18N

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Traceroute"));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jScrollPane15.setBorder(javax.swing.BorderFactory.createTitledBorder("Result:"));

        taTraceResult.setEditable(false);
        taTraceResult.setColumns(20);
        taTraceResult.setRows(5);
        jScrollPane15.setViewportView(taTraceResult);

        jPanel7.add(jScrollPane15, java.awt.BorderLayout.CENTER);

        jToolBar13.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar13.setFloatable(false);

        jLabel9.setText(" IP-address/DNS-name: ");
        jToolBar13.add(jLabel9);

        tfTraceInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTraceInputKeyPressed(evt);
            }
        });
        jToolBar13.add(tfTraceInput);
        jToolBar13.add(jSeparator37);

        jPanel7.add(jToolBar13, java.awt.BorderLayout.NORTH);

        jToolBar14.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar14.setFloatable(false);
        jToolBar14.add(jSeparator38);

        btnTraceRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnTraceRun.setText("Run TraceRoute ");
        btnTraceRun.setToolTipText("");
        btnTraceRun.setFocusable(false);
        btnTraceRun.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnTraceRun.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTraceRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraceRunActionPerformed(evt);
            }
        });
        jToolBar14.add(btnTraceRun);
        btnTraceRun.getAccessibleContext().setAccessibleName("Trace");

        jToolBar14.add(jSeparator39);

        btnSaveTrace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save-16.png"))); // NOI18N
        btnSaveTrace.setText("Save Result ");
        btnSaveTrace.setFocusable(false);
        btnSaveTrace.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSaveTrace.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveTrace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveTraceActionPerformed(evt);
            }
        });
        jToolBar14.add(btnSaveTrace);
        jToolBar14.add(jSeparator40);

        jPanel7.add(jToolBar14, java.awt.BorderLayout.SOUTH);

        jScrollPane14.setViewportView(jPanel7);

        jTabbedPane1.addTab("ICMP-Trace", new javax.swing.ImageIcon(getClass().getResource("/img/trace-16.png")), jScrollPane14, ""); // NOI18N

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("ICMP-flood utility"));
        jPanel13.setLayout(new java.awt.BorderLayout());

        jScrollPane26.setBorder(javax.swing.BorderFactory.createTitledBorder("Received messages:"));

        taPingFloodResult.setEditable(false);
        taPingFloodResult.setColumns(20);
        taPingFloodResult.setRows(5);
        jScrollPane26.setViewportView(taPingFloodResult);

        jPanel13.add(jScrollPane26, java.awt.BorderLayout.CENTER);

        jToolBar23.setBorder(javax.swing.BorderFactory.createTitledBorder("Target IP:"));
        jToolBar23.setFloatable(false);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("  Target IP-address:  ");
        jToolBar23.add(jLabel21);
        jToolBar23.add(tfPingFloodIP);
        jToolBar23.add(jSeparator73);

        jLabel22.setText("TimeOut, ms: ");
        jToolBar23.add(jLabel22);

        comboPingFloodTimeouts.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jToolBar23.add(comboPingFloodTimeouts);
        jToolBar23.add(jSeparator75);

        jPanel13.add(jToolBar23, java.awt.BorderLayout.NORTH);

        jToolBar24.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar24.setFloatable(false);
        jToolBar24.add(jSeparator72);

        btnBooleanPingFlood.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnBooleanPingFlood.setText("Run Ping Flood");
        btnBooleanPingFlood.setFocusable(false);
        btnBooleanPingFlood.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnBooleanPingFlood.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBooleanPingFlood.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                btnBooleanPingFloodItemStateChanged(evt);
            }
        });
        jToolBar24.add(btnBooleanPingFlood);

        jPanel13.add(jToolBar24, java.awt.BorderLayout.SOUTH);

        jScrollPane25.setViewportView(jPanel13);

        jTabbedPane1.addTab("ICMP-flood", new javax.swing.ImageIcon(getClass().getResource("/img/flood-car-16.png")), jScrollPane25); // NOI18N

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Network Ping-Scanner"));
        jPanel10.setLayout(new java.awt.BorderLayout());

        jScrollPane20.setBorder(javax.swing.BorderFactory.createTitledBorder("Result:"));

        taPingScannerResult.setEditable(false);
        taPingScannerResult.setColumns(20);
        taPingScannerResult.setRows(5);
        jScrollPane20.setViewportView(taPingScannerResult);

        jPanel10.add(jScrollPane20, java.awt.BorderLayout.CENTER);

        jToolBar19.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar19.setFloatable(false);
        jToolBar19.add(jSeparator51);

        jLabel13.setText("Network IP-address: ");
        jToolBar19.add(jLabel13);
        jToolBar19.add(tfPingScannerInput);
        jToolBar19.add(jSeparator52);

        jLabel14.setText("Prefix/Mask: ");
        jToolBar19.add(jLabel14);

        comboPingScannerMasks.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jToolBar19.add(comboPingScannerMasks);
        jToolBar19.add(jSeparator53);

        jLabel3.setText("TimeOut, ms: ");
        jToolBar19.add(jLabel3);

        comboPingScannerTimeouts.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jToolBar19.add(comboPingScannerTimeouts);
        jToolBar19.add(jSeparator57);

        jLabel15.setText("Show: ");
        jToolBar19.add(jLabel15);

        comboPingScannerShow.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboPingScannerShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboPingScannerShowActionPerformed(evt);
            }
        });
        jToolBar19.add(comboPingScannerShow);
        jToolBar19.add(jSeparator58);

        jPanel10.add(jToolBar19, java.awt.BorderLayout.NORTH);

        jToolBar20.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar20.setFloatable(false);
        jToolBar20.add(jSeparator54);

        btnPingScannerRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnPingScannerRun.setText("Run Ping-Scanner");
        btnPingScannerRun.setFocusable(false);
        btnPingScannerRun.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnPingScannerRun.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPingScannerRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPingScannerRunActionPerformed(evt);
            }
        });
        jToolBar20.add(btnPingScannerRun);
        jToolBar20.add(jSeparator55);

        btnPingScannerSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save-16.png"))); // NOI18N
        btnPingScannerSave.setText("Save Result ");
        btnPingScannerSave.setFocusable(false);
        btnPingScannerSave.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnPingScannerSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPingScannerSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPingScannerSaveActionPerformed(evt);
            }
        });
        jToolBar20.add(btnPingScannerSave);
        jToolBar20.add(jSeparator56);

        jPanel10.add(jToolBar20, java.awt.BorderLayout.SOUTH);

        jScrollPane19.setViewportView(jPanel10);

        jTabbedPane1.addTab("Network Ping-Scanner", new javax.swing.ImageIcon(getClass().getResource("/img/scan_16.png")), jScrollPane19); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Host TCP-scaner"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jScrollPane7.setBorder(javax.swing.BorderFactory.createTitledBorder("Result:"));

        taHostTcpScannerResult.setEditable(false);
        taHostTcpScannerResult.setColumns(20);
        taHostTcpScannerResult.setRows(5);
        jScrollPane7.setViewportView(taHostTcpScannerResult);

        jPanel2.add(jScrollPane7, java.awt.BorderLayout.CENTER);

        jToolBar5.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar5.setFloatable(false);
        jToolBar5.add(jSeparator22);

        jLabel4.setText("Enter IP-address:");
        jToolBar5.add(jLabel4);
        jToolBar5.add(jSeparator5);

        tfHostTcpScannerIP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfHostTcpScannerIPKeyPressed(evt);
            }
        });
        jToolBar5.add(tfHostTcpScannerIP);
        jToolBar5.add(jSeparator112);

        jLabel44.setText("Scan TCP-ports: ");
        jToolBar5.add(jLabel44);

        comboHostTcpScannerType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GENERIC", "ALL" }));
        jToolBar5.add(comboHostTcpScannerType);
        jToolBar5.add(jSeparator105);

        jLabel43.setText("TimeOut: ");
        jToolBar5.add(jLabel43);

        comboHostTcpScannerTimeouts.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jToolBar5.add(comboHostTcpScannerTimeouts);
        jToolBar5.add(jSeparator111);

        jLabel41.setText("Show: ");
        jToolBar5.add(jLabel41);

        comboHostTcpScannerShow.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboHostTcpScannerShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboHostTcpScannerShowActionPerformed(evt);
            }
        });
        jToolBar5.add(comboHostTcpScannerShow);
        jToolBar5.add(jSeparator6);

        jPanel2.add(jToolBar5, java.awt.BorderLayout.NORTH);

        jToolBar6.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar6.setFloatable(false);
        jToolBar6.add(jSeparator7);

        btnHostTcpScannerRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnHostTcpScannerRun.setText("Run TCP-scaner ");
        btnHostTcpScannerRun.setFocusable(false);
        btnHostTcpScannerRun.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnHostTcpScannerRun.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnHostTcpScannerRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHostTcpScannerRunActionPerformed(evt);
            }
        });
        jToolBar6.add(btnHostTcpScannerRun);
        jToolBar6.add(jSeparator8);

        btnHostTcpScannerSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save-16.png"))); // NOI18N
        btnHostTcpScannerSave.setText("Save Result ");
        btnHostTcpScannerSave.setFocusable(false);
        btnHostTcpScannerSave.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnHostTcpScannerSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHostTcpScannerSaveActionPerformed(evt);
            }
        });
        jToolBar6.add(btnHostTcpScannerSave);
        jToolBar6.add(jSeparator30);

        jPanel2.add(jToolBar6, java.awt.BorderLayout.SOUTH);

        jScrollPane4.setViewportView(jPanel2);

        jTabbedPane1.addTab("Host TCP-scanner", new javax.swing.ImageIcon(getClass().getResource("/img/free-icon-radar-1257389.png")), jScrollPane4); // NOI18N

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Host TCP-ping"));
        jPanel16.setLayout(new java.awt.BorderLayout());

        jScrollPane32.setBorder(javax.swing.BorderFactory.createTitledBorder("Result:"));

        taPingTCPout.setEditable(false);
        taPingTCPout.setColumns(20);
        taPingTCPout.setRows(5);
        jScrollPane32.setViewportView(taPingTCPout);

        jPanel16.add(jScrollPane32, java.awt.BorderLayout.CENTER);

        jToolBar25.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar25.setFloatable(false);
        jToolBar25.add(jSeparator82);

        jLabel25.setText("IP-address: ");
        jToolBar25.add(jLabel25);
        jToolBar25.add(jSeparator83);
        jToolBar25.add(tfPingTCPip);
        jToolBar25.add(jSeparator84);

        jLabel26.setText("TCP-ports (21,22,23,80,...): ");
        jToolBar25.add(jLabel26);
        jToolBar25.add(tfPingTCPports);
        jToolBar25.add(jSeparator88);

        jPanel16.add(jToolBar25, java.awt.BorderLayout.NORTH);

        jToolBar28.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar28.setFloatable(false);
        jToolBar28.add(jSeparator85);

        btnTcpPingRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnTcpPingRun.setText("Run TCP-ping ");
        btnTcpPingRun.setFocusable(false);
        btnTcpPingRun.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnTcpPingRun.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTcpPingRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTcpPingRunActionPerformed(evt);
            }
        });
        jToolBar28.add(btnTcpPingRun);
        jToolBar28.add(jSeparator86);

        btnSaveTcpPing.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save-16.png"))); // NOI18N
        btnSaveTcpPing.setText("Save Result ");
        btnSaveTcpPing.setFocusable(false);
        btnSaveTcpPing.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSaveTcpPing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveTcpPingActionPerformed(evt);
            }
        });
        jToolBar28.add(btnSaveTcpPing);
        jToolBar28.add(jSeparator87);

        jPanel16.add(jToolBar28, java.awt.BorderLayout.SOUTH);

        jScrollPane31.setViewportView(jPanel16);

        jTabbedPane1.addTab("Host TCP-ping", new javax.swing.ImageIcon(getClass().getResource("/img/icons8-ping-pong-16.png")), jScrollPane31); // NOI18N

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder("Network TCP-scanner"));
        jPanel18.setLayout(new java.awt.BorderLayout());

        jScrollPane36.setBorder(javax.swing.BorderFactory.createTitledBorder("Result:"));

        taNetTcpScannerResult.setEditable(false);
        taNetTcpScannerResult.setColumns(20);
        taNetTcpScannerResult.setRows(5);
        jScrollPane36.setViewportView(taNetTcpScannerResult);

        jPanel18.add(jScrollPane36, java.awt.BorderLayout.CENTER);

        jToolBar30.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar30.setFloatable(false);

        jLabel28.setText(" Network IP: ");
        jToolBar30.add(jLabel28);
        jToolBar30.add(tfNetTcpScannerIP);
        jToolBar30.add(jSeparator76);

        jLabel29.setText("Prefix/Mask: ");
        jToolBar30.add(jLabel29);

        comboNetTcpScannerMasks.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jToolBar30.add(comboNetTcpScannerMasks);
        jToolBar30.add(jSeparator92);

        jLabel32.setText(" TCP-port: ");
        jToolBar30.add(jLabel32);

        tfNetTcpScannerPort.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tfNetTcpScannerPort.setText("80");
        jToolBar30.add(tfNetTcpScannerPort);
        jToolBar30.add(jSeparator33);

        jLabel30.setText("TimeOut: ");
        jToolBar30.add(jLabel30);

        comboNetTcpScannerTimeouts.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "111", "222" }));
        jToolBar30.add(comboNetTcpScannerTimeouts);
        jToolBar30.add(jSeparator93);

        jLabel31.setText("Show: ");
        jToolBar30.add(jLabel31);

        comboNetTcpScannerShow.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboNetTcpScannerShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboNetTcpScannerShowActionPerformed(evt);
            }
        });
        jToolBar30.add(comboNetTcpScannerShow);
        jToolBar30.add(jSeparator35);

        jPanel18.add(jToolBar30, java.awt.BorderLayout.NORTH);

        jToolBar31.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar31.setFloatable(false);
        jToolBar31.add(jSeparator95);

        btnNetTcpScannerRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnNetTcpScannerRun.setText("Run Network TCP-scanner");
        btnNetTcpScannerRun.setActionCommand("Run Network TCP-scanner ");
        btnNetTcpScannerRun.setFocusable(false);
        btnNetTcpScannerRun.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnNetTcpScannerRun.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNetTcpScannerRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNetTcpScannerRunActionPerformed(evt);
            }
        });
        jToolBar31.add(btnNetTcpScannerRun);
        jToolBar31.add(jSeparator96);

        btnNetTcpScannerSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save-16.png"))); // NOI18N
        btnNetTcpScannerSave.setText("Save Result ");
        btnNetTcpScannerSave.setFocusable(false);
        btnNetTcpScannerSave.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnNetTcpScannerSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNetTcpScannerSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNetTcpScannerSaveActionPerformed(evt);
            }
        });
        jToolBar31.add(btnNetTcpScannerSave);
        jToolBar31.add(jSeparator97);

        jPanel18.add(jToolBar31, java.awt.BorderLayout.SOUTH);

        jScrollPane35.setViewportView(jPanel18);

        jTabbedPane1.addTab("Network TCP-scanner", new javax.swing.ImageIcon(getClass().getResource("/img/radar-16.png")), jScrollPane35); // NOI18N

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("IP-calculator"));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jScrollPane9.setBorder(javax.swing.BorderFactory.createTitledBorder("Result:"));

        taCalcResult.setEditable(false);
        taCalcResult.setColumns(20);
        taCalcResult.setRows(5);
        jScrollPane9.setViewportView(taCalcResult);

        jPanel4.add(jScrollPane9, java.awt.BorderLayout.CENTER);

        jToolBar9.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar9.setFloatable(false);
        jToolBar9.add(jSeparator20);

        jLabel6.setText("Enter IP-address: ");
        jToolBar9.add(jLabel6);

        tfCalcInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfCalcInputKeyPressed(evt);
            }
        });
        jToolBar9.add(tfCalcInput);
        jToolBar9.add(jSeparator19);

        jLabel8.setText("Prefix/Mask: ");
        jToolBar9.add(jLabel8);

        comboCalcMasks.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboCalcMasks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCalcMasksActionPerformed(evt);
            }
        });
        jToolBar9.add(comboCalcMasks);
        jToolBar9.add(jSeparator14);

        jPanel4.add(jToolBar9, java.awt.BorderLayout.NORTH);

        jToolBar10.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar10.setFloatable(false);
        jToolBar10.add(jSeparator15);

        btnCalcRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnCalcRun.setText("Calculate IP-data ");
        btnCalcRun.setFocusable(false);
        btnCalcRun.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnCalcRun.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCalcRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcRunActionPerformed(evt);
            }
        });
        jToolBar10.add(btnCalcRun);
        jToolBar10.add(jSeparator16);

        btnSaveCalc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save-16.png"))); // NOI18N
        btnSaveCalc.setText("Save Result ");
        btnSaveCalc.setFocusable(false);
        btnSaveCalc.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSaveCalc.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveCalcActionPerformed(evt);
            }
        });
        jToolBar10.add(btnSaveCalc);
        jToolBar10.add(jSeparator32);

        jPanel4.add(jToolBar10, java.awt.BorderLayout.SOUTH);

        jScrollPane3.setViewportView(jPanel4);

        jTabbedPane1.addTab("IP-calculator", new javax.swing.ImageIcon(getClass().getResource("/img/ip-blue-16.png")), jScrollPane3); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("DNS-checker"));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jScrollPane8.setBorder(javax.swing.BorderFactory.createTitledBorder("Result:"));

        taDnsResult.setEditable(false);
        taDnsResult.setColumns(20);
        taDnsResult.setRows(5);
        jScrollPane8.setViewportView(taDnsResult);

        jPanel3.add(jScrollPane8, java.awt.BorderLayout.CENTER);

        jToolBar7.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar7.setFloatable(false);
        jToolBar7.add(jSeparator21);

        jLabel5.setText("Enter IP-address/DNS-name:");
        jToolBar7.add(jLabel5);
        jToolBar7.add(jSeparator9);

        tfDnsInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfDnsInputKeyPressed(evt);
            }
        });
        jToolBar7.add(tfDnsInput);
        jToolBar7.add(jSeparator10);

        jPanel3.add(jToolBar7, java.awt.BorderLayout.NORTH);

        jToolBar8.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar8.setFloatable(false);
        jToolBar8.add(jSeparator11);

        btnDnsRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnDnsRun.setText("Check DNS ");
        btnDnsRun.setFocusable(false);
        btnDnsRun.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnDnsRun.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDnsRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDnsRunActionPerformed(evt);
            }
        });
        jToolBar8.add(btnDnsRun);
        jToolBar8.add(jSeparator12);

        btnSaveDns.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save-16.png"))); // NOI18N
        btnSaveDns.setText("Save Result ");
        btnSaveDns.setFocusable(false);
        btnSaveDns.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSaveDns.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveDns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveDnsActionPerformed(evt);
            }
        });
        jToolBar8.add(btnSaveDns);
        jToolBar8.add(jSeparator31);

        jPanel3.add(jToolBar8, java.awt.BorderLayout.SOUTH);

        jScrollPane2.setViewportView(jPanel3);

        jTabbedPane1.addTab("DNS-check", new javax.swing.ImageIcon(getClass().getResource("/img/dns-16.png")), jScrollPane2); // NOI18N

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Network DNS-scanner"));
        jPanel20.setLayout(new java.awt.BorderLayout());

        jScrollPane40.setBorder(javax.swing.BorderFactory.createTitledBorder("Result:"));

        taNetDnsScannerResult.setEditable(false);
        taNetDnsScannerResult.setColumns(20);
        taNetDnsScannerResult.setRows(5);
        jScrollPane40.setViewportView(taNetDnsScannerResult);

        jPanel20.add(jScrollPane40, java.awt.BorderLayout.CENTER);

        jToolBar34.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar34.setFloatable(false);
        jToolBar34.add(jSeparator64);

        jLabel39.setText("Network IP-address: ");
        jToolBar34.add(jLabel39);
        jToolBar34.add(tfNetDnsScannerIP);
        jToolBar34.add(jSeparator104);

        jLabel40.setText("Prefix/Mask: ");
        jToolBar34.add(jLabel40);

        comboNetDnsScannerMasks.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jToolBar34.add(comboNetDnsScannerMasks);
        jToolBar34.add(jSeparator106);

        jLabel42.setText("Show: ");
        jToolBar34.add(jLabel42);

        comboNetDnsScannerShow.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboNetDnsScannerShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboNetDnsScannerShowActionPerformed(evt);
            }
        });
        jToolBar34.add(comboNetDnsScannerShow);
        jToolBar34.add(jSeparator107);

        jPanel20.add(jToolBar34, java.awt.BorderLayout.NORTH);

        jToolBar35.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar35.setFloatable(false);
        jToolBar35.add(jSeparator108);

        btnNetDnsScannerRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnNetDnsScannerRun.setText("Run DNS-scanner");
        btnNetDnsScannerRun.setFocusable(false);
        btnNetDnsScannerRun.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnNetDnsScannerRun.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNetDnsScannerRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNetDnsScannerRunActionPerformed(evt);
            }
        });
        jToolBar35.add(btnNetDnsScannerRun);
        jToolBar35.add(jSeparator109);

        btnNetDnsScannerSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save-16.png"))); // NOI18N
        btnNetDnsScannerSave.setText("Save Result ");
        btnNetDnsScannerSave.setFocusable(false);
        btnNetDnsScannerSave.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnNetDnsScannerSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNetDnsScannerSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNetDnsScannerSaveActionPerformed(evt);
            }
        });
        jToolBar35.add(btnNetDnsScannerSave);
        jToolBar35.add(jSeparator110);

        jPanel20.add(jToolBar35, java.awt.BorderLayout.SOUTH);

        jScrollPane39.setViewportView(jPanel20);

        jTabbedPane1.addTab("Network DNS-scanner", new javax.swing.ImageIcon(getClass().getResource("/img/free-icon-radar-16.png")), jScrollPane39); // NOI18N

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("telnet"));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jToolBar15.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar15.setFloatable(false);
        jToolBar15.add(jSeparator41);

        jLabel11.setText("Enter IP-address:");
        jToolBar15.add(jLabel11);
        jToolBar15.add(jSeparator42);

        tfTelnetAdres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTelnetAdresKeyPressed(evt);
            }
        });
        jToolBar15.add(tfTelnetAdres);
        jToolBar15.add(jSeparator59);

        jLabel16.setText("Port (1-65535, default=23) : ");
        jToolBar15.add(jLabel16);
        jToolBar15.add(jSeparator60);

        tfTelnetPort.setText("23");
        tfTelnetPort.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTelnetPortKeyPressed(evt);
            }
        });
        jToolBar15.add(tfTelnetPort);
        jToolBar15.add(jSeparator43);

        jPanel8.add(jToolBar15, java.awt.BorderLayout.NORTH);

        jToolBar16.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar16.setFloatable(false);
        jToolBar16.add(jSeparator44);

        btnTelnetRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnTelnetRun.setText("Run Telnet ");
        btnTelnetRun.setFocusable(false);
        btnTelnetRun.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnTelnetRun.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTelnetRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTelnetRunActionPerformed(evt);
            }
        });
        jToolBar16.add(btnTelnetRun);

        jPanel8.add(jToolBar16, java.awt.BorderLayout.SOUTH);

        jScrollPane16.setViewportView(jPanel8);

        jTabbedPane1.addTab("Telnet-client", new javax.swing.ImageIcon(getClass().getResource("/img/telnet-web-16.png")), jScrollPane16); // NOI18N

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Request to local ARP-cache"));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jScrollPane18.setBorder(javax.swing.BorderFactory.createTitledBorder("Result:"));

        taArpResult.setEditable(false);
        taArpResult.setColumns(20);
        taArpResult.setRows(5);
        jScrollPane18.setViewportView(taArpResult);

        jPanel9.add(jScrollPane18, java.awt.BorderLayout.CENTER);

        jToolBar17.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar17.setFloatable(false);
        jToolBar17.add(jSeparator45);

        jLabel12.setText("Enter IP-address:");
        jToolBar17.add(jLabel12);
        jToolBar17.add(jSeparator46);

        tfArpInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfArpInputKeyPressed(evt);
            }
        });
        jToolBar17.add(tfArpInput);
        jToolBar17.add(jSeparator47);

        jPanel9.add(jToolBar17, java.awt.BorderLayout.NORTH);

        jToolBar18.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar18.setFloatable(false);
        jToolBar18.add(jSeparator48);

        btnArpRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnArpRun.setText("Get Arp for IP ");
        btnArpRun.setToolTipText("");
        btnArpRun.setFocusable(false);
        btnArpRun.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnArpRun.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnArpRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArpRunActionPerformed(evt);
            }
        });
        jToolBar18.add(btnArpRun);
        jToolBar18.add(jSeparator49);

        btnArpALL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/get-double-16.png"))); // NOI18N
        btnArpALL.setText("Get all local ARP-cache");
        btnArpALL.setFocusable(false);
        btnArpALL.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnArpALL.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnArpALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArpALLActionPerformed(evt);
            }
        });
        jToolBar18.add(btnArpALL);
        jToolBar18.add(jSeparator23);

        btnSaveArp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save-16.png"))); // NOI18N
        btnSaveArp.setText("Save Result ");
        btnSaveArp.setFocusable(false);
        btnSaveArp.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSaveArp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveArp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveArpActionPerformed(evt);
            }
        });
        jToolBar18.add(btnSaveArp);
        jToolBar18.add(jSeparator50);

        jPanel9.add(jToolBar18, java.awt.BorderLayout.SOUTH);

        jScrollPane17.setViewportView(jPanel9);

        jTabbedPane1.addTab("ARP-get", new javax.swing.ImageIcon(getClass().getResource("/img/net-card-green-16.png")), jScrollPane17, ""); // NOI18N

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("SNMP get concrete value utility"));
        jPanel11.setLayout(new java.awt.BorderLayout());

        jToolBar21.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar21.setFloatable(false);
        jToolBar21.add(jSeparator61);

        jLabel17.setText("IP-address: ");
        jToolBar21.add(jLabel17);

        tfSnmpGetIP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfSnmpGetIP.setText("127.0.0.1");
        tfSnmpGetIP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfSnmpGetIPKeyPressed(evt);
            }
        });
        jToolBar21.add(tfSnmpGetIP);
        jToolBar21.add(jSeparator63);

        jLabel18.setText("Read-Community: ");
        jToolBar21.add(jLabel18);

        tfSnmpGetCommunity.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfSnmpGetCommunity.setText("public");
        tfSnmpGetCommunity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfSnmpGetCommunityKeyPressed(evt);
            }
        });
        jToolBar21.add(tfSnmpGetCommunity);
        jToolBar21.add(jSeparator65);

        jLabel19.setText("OID (1.3.6..): ");
        jToolBar21.add(jLabel19);

        tfSnmpGetOID.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfSnmpGetOID.setText("1.3.6.1.");
        tfSnmpGetOID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfSnmpGetOIDKeyPressed(evt);
            }
        });
        jToolBar21.add(tfSnmpGetOID);
        jToolBar21.add(jSeparator70);

        jLabel20.setText("Version: ");
        jToolBar21.add(jLabel20);

        comboSnmpVersion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2c", " " }));
        comboSnmpVersion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboSnmpVersionActionPerformed(evt);
            }
        });
        jToolBar21.add(comboSnmpVersion);
        jToolBar21.add(jSeparator62);

        jPanel11.add(jToolBar21, java.awt.BorderLayout.NORTH);

        jToolBar22.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar22.setFloatable(false);
        jToolBar22.add(jSeparator66);

        btnSnmpGet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnSnmpGet.setText("Get concrete value ");
        btnSnmpGet.setFocusable(false);
        btnSnmpGet.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSnmpGet.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSnmpGet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSnmpGetActionPerformed(evt);
            }
        });
        jToolBar22.add(btnSnmpGet);
        jToolBar22.add(jSeparator69);

        btnSnmpMibsAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/info-16.png"))); // NOI18N
        btnSnmpMibsAll.setText("All Snmp-MIBs structure");
        btnSnmpMibsAll.setFocusable(false);
        btnSnmpMibsAll.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSnmpMibsAll.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSnmpMibsAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSnmpMibsAllActionPerformed(evt);
            }
        });
        jToolBar22.add(btnSnmpMibsAll);
        jToolBar22.add(jSeparator67);

        btnSnmpMibsStd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/info-16.png"))); // NOI18N
        btnSnmpMibsStd.setText("Standard Snmp-MIBs structure");
        btnSnmpMibsStd.setFocusable(false);
        btnSnmpMibsStd.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSnmpMibsStd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSnmpMibsStd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSnmpMibsStdActionPerformed(evt);
            }
        });
        jToolBar22.add(btnSnmpMibsStd);

        jPanel11.add(jToolBar22, java.awt.BorderLayout.SOUTH);

        jScrollPane22.setBorder(javax.swing.BorderFactory.createTitledBorder("Result:"));

        taSnmpGet.setEditable(false);
        taSnmpGet.setColumns(20);
        taSnmpGet.setRows(5);
        jScrollPane22.setViewportView(taSnmpGet);

        jPanel11.add(jScrollPane22, java.awt.BorderLayout.CENTER);

        jScrollPane21.setViewportView(jPanel11);

        jTabbedPane1.addTab("Snmp-Get", new javax.swing.ImageIcon(getClass().getResource("/img/snmp-get-16.png")), jScrollPane21); // NOI18N

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Network SNMP-scanner"));
        jPanel19.setLayout(new java.awt.BorderLayout());

        jScrollPane38.setBorder(javax.swing.BorderFactory.createTitledBorder("Result:"));

        taNetSnmpScannerResult.setEditable(false);
        taNetSnmpScannerResult.setColumns(20);
        taNetSnmpScannerResult.setRows(5);
        jScrollPane38.setViewportView(taNetSnmpScannerResult);

        jPanel19.add(jScrollPane38, java.awt.BorderLayout.CENTER);

        jToolBar32.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar32.setFloatable(false);

        jLabel33.setText(" Net IP: ");
        jToolBar32.add(jLabel33);

        tfNetSnmpScannerIP.setText("1.1.1.1");
        jToolBar32.add(tfNetSnmpScannerIP);
        jToolBar32.add(jSeparator94);

        jLabel34.setText("Prefix/Mask: ");
        jToolBar32.add(jLabel34);

        comboNetSnmpScannerMasks.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jToolBar32.add(comboNetSnmpScannerMasks);
        jToolBar32.add(jSeparator98);

        jLabel35.setText("Read-Community: ");
        jToolBar32.add(jLabel35);

        tfNetSnmpScannerCommunity.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tfNetSnmpScannerCommunity.setText("public");
        jToolBar32.add(tfNetSnmpScannerCommunity);
        jToolBar32.add(jSeparator36);

        jLabel38.setText("Ver: ");
        jToolBar32.add(jLabel38);

        comboNetSnmpScannerVersion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2c" }));
        jToolBar32.add(comboNetSnmpScannerVersion);
        jToolBar32.add(jSeparator103);

        jLabel36.setText("TimeOut: ");
        jToolBar32.add(jLabel36);

        comboNetSnmpScannerTimeouts.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "111", "222" }));
        jToolBar32.add(comboNetSnmpScannerTimeouts);
        jToolBar32.add(jSeparator99);

        jLabel37.setText("Show: ");
        jToolBar32.add(jLabel37);

        comboNetSnmpScannerShow.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboNetSnmpScannerShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboNetSnmpScannerShowActionPerformed(evt);
            }
        });
        jToolBar32.add(comboNetSnmpScannerShow);
        jToolBar32.add(jSeparator68);

        jPanel19.add(jToolBar32, java.awt.BorderLayout.NORTH);

        jToolBar33.setBorder(javax.swing.BorderFactory.createTitledBorder("Actions & SNMP-oid's"));
        jToolBar33.setFloatable(false);
        jToolBar33.add(jSeparator100);

        btnNetSnmpScannerRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnNetSnmpScannerRun.setText(" Run ");
        btnNetSnmpScannerRun.setActionCommand("Run Network TCP-scanner ");
        btnNetSnmpScannerRun.setFocusable(false);
        btnNetSnmpScannerRun.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnNetSnmpScannerRun.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNetSnmpScannerRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNetSnmpScannerRunActionPerformed(evt);
            }
        });
        jToolBar33.add(btnNetSnmpScannerRun);
        jToolBar33.add(jSeparator101);

        btnNetSnmpScannerSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save-16.png"))); // NOI18N
        btnNetSnmpScannerSave.setText("Save Result ");
        btnNetSnmpScannerSave.setFocusable(false);
        btnNetSnmpScannerSave.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnNetSnmpScannerSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNetSnmpScannerSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNetSnmpScannerSaveActionPerformed(evt);
            }
        });
        jToolBar33.add(btnNetSnmpScannerSave);
        jToolBar33.add(jSeparator102);

        comboNetSnmpScannerOID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", " " }));
        comboNetSnmpScannerOID.setToolTipText("Simply Edit the /cfg/snmp-oids.txt File !");
        jToolBar33.add(comboNetSnmpScannerOID);

        jPanel19.add(jToolBar33, java.awt.BorderLayout.SOUTH);

        jScrollPane37.setViewportView(jPanel19);

        jTabbedPane1.addTab("Network SNMP-scanner", new javax.swing.ImageIcon(getClass().getResource("/img/radarr-16.png")), jScrollPane37); // NOI18N

        jPanel12.setLayout(new java.awt.BorderLayout());

        taSnmpOidHelp.setEditable(false);
        taSnmpOidHelp.setColumns(20);
        taSnmpOidHelp.setRows(5);
        taSnmpOidHelp.setMinimumSize(new java.awt.Dimension(300, 300));
        jScrollPane24.setViewportView(taSnmpOidHelp);

        jPanel12.add(jScrollPane24, java.awt.BorderLayout.CENTER);

        jScrollPane23.setViewportView(jPanel12);

        jTabbedPane1.addTab("Snmp OID-help", new javax.swing.ImageIcon(getClass().getResource("/img/help-green-16.png")), jScrollPane23); // NOI18N

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Syslog-Server"));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jScrollPane11.setBorder(javax.swing.BorderFactory.createTitledBorder("Received messages:"));

        taSyslogResult.setEditable(false);
        taSyslogResult.setColumns(20);
        taSyslogResult.setRows(5);
        jScrollPane11.setViewportView(taSyslogResult);

        jPanel5.add(jScrollPane11, java.awt.BorderLayout.CENTER);

        jToolBar11.setBorder(javax.swing.BorderFactory.createTitledBorder("UDP-port:"));
        jToolBar11.setFloatable(false);
        jToolBar11.add(jSeparator24);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Select syslog-server UDP-port:  ");
        jToolBar11.add(jLabel10);
        jToolBar11.add(tfSyslogInput);
        jToolBar11.add(jSeparator29);

        jPanel5.add(jToolBar11, java.awt.BorderLayout.NORTH);

        jToolBar12.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar12.setFloatable(false);
        jToolBar12.add(jSeparator27);

        btnBooleanSyslog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnBooleanSyslog.setText("Run Syslog-server ");
        btnBooleanSyslog.setFocusable(false);
        btnBooleanSyslog.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnBooleanSyslog.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBooleanSyslog.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                btnBooleanSyslogItemStateChanged(evt);
            }
        });
        jToolBar12.add(btnBooleanSyslog);
        jToolBar12.add(jSeparator28);

        btnSyslogReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit_clear-16.png"))); // NOI18N
        btnSyslogReset.setText("Clear Messages ");
        btnSyslogReset.setFocusable(false);
        btnSyslogReset.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSyslogReset.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSyslogReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSyslogResetActionPerformed(evt);
            }
        });
        jToolBar12.add(btnSyslogReset);
        jToolBar12.add(jSeparator25);

        btnSyslogSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save-16.png"))); // NOI18N
        btnSyslogSave.setText("Save Log ");
        btnSyslogSave.setFocusable(false);
        btnSyslogSave.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSyslogSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSyslogSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSyslogSaveActionPerformed(evt);
            }
        });
        jToolBar12.add(btnSyslogSave);

        jPanel5.add(jToolBar12, java.awt.BorderLayout.SOUTH);

        jScrollPane10.setViewportView(jPanel5);

        jTabbedPane1.addTab("Syslog-server", new javax.swing.ImageIcon(getClass().getResource("/img/syslog-16.png")), jScrollPane10); // NOI18N

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("TFTP-Server on default UDP/69"));
        jPanel14.setLayout(new java.awt.BorderLayout());

        jScrollPane28.setBorder(javax.swing.BorderFactory.createTitledBorder("Received messages:"));

        taTftpResult.setEditable(false);
        taTftpResult.setColumns(20);
        taTftpResult.setRows(5);
        jScrollPane28.setViewportView(taTftpResult);

        jPanel14.add(jScrollPane28, java.awt.BorderLayout.CENTER);

        jToolBar26.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar26.setFloatable(false);
        jToolBar26.add(jSeparator77);

        btnBooleanTftp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnBooleanTftp.setText("Run TFTP-server ");
        btnBooleanTftp.setFocusable(false);
        btnBooleanTftp.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnBooleanTftp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBooleanTftp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                btnBooleanTftpItemStateChanged(evt);
            }
        });
        jToolBar26.add(btnBooleanTftp);
        jToolBar26.add(jSeparator79);

        btnTftpSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/folder-green-16.png"))); // NOI18N
        btnTftpSave.setText("Folder ");
        btnTftpSave.setFocusable(false);
        btnTftpSave.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnTftpSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTftpSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTftpSaveActionPerformed(evt);
            }
        });
        jToolBar26.add(btnTftpSave);

        jLabel23.setText(" = ");
        jToolBar26.add(jLabel23);

        tfTftpFolder.setEditable(false);
        tfTftpFolder.setText("/tmp");
        jToolBar26.add(tfTftpFolder);
        jToolBar26.add(jSeparator78);

        jPanel14.add(jToolBar26, java.awt.BorderLayout.SOUTH);

        jScrollPane27.setViewportView(jPanel14);

        jTabbedPane1.addTab("TFTP-server", new javax.swing.ImageIcon(getClass().getResource("/img/tftp-well-16.png")), jScrollPane27); // NOI18N

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Anonymous FTP-Server on TCP/21"));
        jPanel17.setLayout(new java.awt.BorderLayout());

        jScrollPane34.setBorder(javax.swing.BorderFactory.createTitledBorder("Received messages:"));

        taFtpResult.setEditable(false);
        taFtpResult.setColumns(20);
        taFtpResult.setRows(5);
        jScrollPane34.setViewportView(taFtpResult);

        jPanel17.add(jScrollPane34, java.awt.BorderLayout.CENTER);

        jToolBar29.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar29.setFloatable(false);
        jToolBar29.add(jSeparator89);

        btnBooleanFtp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnBooleanFtp.setText("Run FTP-server ");
        btnBooleanFtp.setFocusable(false);
        btnBooleanFtp.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnBooleanFtp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBooleanFtp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                btnBooleanFtpItemStateChanged(evt);
            }
        });
        jToolBar29.add(btnBooleanFtp);
        jToolBar29.add(jSeparator90);

        btnFtpSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/folder-green-16.png"))); // NOI18N
        btnFtpSave.setText("Folder ");
        btnFtpSave.setFocusable(false);
        btnFtpSave.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnFtpSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFtpSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFtpSaveActionPerformed(evt);
            }
        });
        jToolBar29.add(btnFtpSave);

        jLabel27.setText(" = ");
        jToolBar29.add(jLabel27);

        tfFtpFolder.setEditable(false);
        tfFtpFolder.setText("/tmp");
        jToolBar29.add(tfFtpFolder);
        jToolBar29.add(jSeparator91);

        jPanel17.add(jToolBar29, java.awt.BorderLayout.SOUTH);

        jScrollPane33.setViewportView(jPanel17);

        jTabbedPane1.addTab("FTP-server", new javax.swing.ImageIcon(getClass().getResource("/img/ftp-16.png")), jScrollPane33); // NOI18N

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("NTP-server"));
        jPanel15.setLayout(new java.awt.BorderLayout());

        jScrollPane30.setBorder(javax.swing.BorderFactory.createTitledBorder("Received messages:"));

        taNtpResult.setEditable(false);
        taNtpResult.setColumns(20);
        taNtpResult.setRows(5);
        jScrollPane30.setViewportView(taNtpResult);

        jPanel15.add(jScrollPane30, java.awt.BorderLayout.CENTER);

        jToolBar27.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar27.setFloatable(false);
        jToolBar27.add(jSeparator80);

        btnBooleanNtp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/go-green-krug-16.png"))); // NOI18N
        btnBooleanNtp.setText("Run NTP-server ");
        btnBooleanNtp.setFocusable(false);
        btnBooleanNtp.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnBooleanNtp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBooleanNtp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                btnBooleanNtpItemStateChanged(evt);
            }
        });
        jToolBar27.add(btnBooleanNtp);
        jToolBar27.add(jSeparator81);

        jLabel24.setText("UDP-port = ");
        jToolBar27.add(jLabel24);
        jToolBar27.add(spinnerNTP);
        jToolBar27.add(jSeparator71);

        jPanel15.add(jToolBar27, java.awt.BorderLayout.SOUTH);

        jScrollPane29.setViewportView(jPanel15);

        jTabbedPane1.addTab("NTP-server", new javax.swing.ImageIcon(getClass().getResource("/img/time-well-16.png")), jScrollPane29); // NOI18N

        jPanel6.setLayout(new java.awt.BorderLayout());

        taLocalResult.setEditable(false);
        taLocalResult.setColumns(20);
        taLocalResult.setRows(5);
        jScrollPane12.setViewportView(taLocalResult);

        jPanel6.add(jScrollPane12, java.awt.BorderLayout.CENTER);

        jScrollPane5.setViewportView(jPanel6);

        jTabbedPane1.addTab("Local IP", new javax.swing.ImageIcon(getClass().getResource("/img/pc-net-16.png")), jScrollPane5); // NOI18N

        jScrollPane13.setViewportView(epAbout);

        jTabbedPane1.addTab("Info", new javax.swing.ImageIcon(getClass().getResource("/img/info-cyan-16.png")), jScrollPane13); // NOI18N

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jToolBar1.setFloatable(false);
        jToolBar1.add(jSeparator1);

        btnUdpFlood.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/flood-sea-level-16.png"))); // NOI18N
        btnUdpFlood.setText("  UDP-flood ");
        btnUdpFlood.setFocusable(false);
        btnUdpFlood.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnUdpFlood.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnUdpFlood.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUdpFloodActionPerformed(evt);
            }
        });
        jToolBar1.add(btnUdpFlood);
        jToolBar1.add(jSeparator74);

        btnMRTG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mrtg.png"))); // NOI18N
        btnMRTG.setText("MRTG SNMP-poller ");
        btnMRTG.setFocusable(false);
        btnMRTG.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnMRTG.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMRTG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMRTGActionPerformed(evt);
            }
        });
        jToolBar1.add(btnMRTG);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        jToolBar4.setBorder(javax.swing.BorderFactory.createTitledBorder("Info:"));
        jToolBar4.setFloatable(false);
        jToolBar4.add(jSeparator34);

        nizInfoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/globe-net-16.png"))); // NOI18N
        nizInfoLabel.setText("info");
        jToolBar4.add(nizInfoLabel);

        getContentPane().add(jToolBar4, java.awt.BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPingRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPingRunActionPerformed
        PjPing.runGetResult(tfPingInput, taPingResult);
    }//GEN-LAST:event_btnPingRunActionPerformed

    private void tfPingInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPingInputKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            PjPing.runGetResult(tfPingInput, taPingResult);
        }
    }//GEN-LAST:event_tfPingInputKeyPressed

    private void btnCalcRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcRunActionPerformed
        PjCalc.runGetResult(tfCalcInput, comboCalcMasks, taCalcResult);
    }//GEN-LAST:event_btnCalcRunActionPerformed

    private void tfCalcInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfCalcInputKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            PjCalc.runGetResult(tfCalcInput, comboCalcMasks, taCalcResult);
        }
    }//GEN-LAST:event_tfCalcInputKeyPressed

    private void comboCalcMasksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCalcMasksActionPerformed
        if (tfCalcInput.getText().length() > 0) {
            PjCalc.runGetResult(tfCalcInput, comboCalcMasks, taCalcResult);
        }
    }//GEN-LAST:event_comboCalcMasksActionPerformed

    private void btnDnsRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDnsRunActionPerformed
        PjDns.check(tfDnsInput, taDnsResult);
    }//GEN-LAST:event_btnDnsRunActionPerformed

    private void tfDnsInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfDnsInputKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            PjDns.check(tfDnsInput, taDnsResult);
        }
    }//GEN-LAST:event_tfDnsInputKeyPressed

    private void btnHostTcpScannerRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHostTcpScannerRunActionPerformed
        HostTcpScanner.runGetResult(tfHostTcpScannerIP, taHostTcpScannerResult);
    }//GEN-LAST:event_btnHostTcpScannerRunActionPerformed

    private void tfHostTcpScannerIPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfHostTcpScannerIPKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            HostTcpScanner.runGetResult(tfHostTcpScannerIP, taHostTcpScannerResult);
        }
    }//GEN-LAST:event_tfHostTcpScannerIPKeyPressed

    private void btnSyslogResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSyslogResetActionPerformed
        taSyslogResult.setText("");
    }//GEN-LAST:event_btnSyslogResetActionPerformed

    private void btnBooleanSyslogItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnBooleanSyslogItemStateChanged
        if (!StringUtils.isNumeric(frame.tfSyslogInput.getText())) {
            JOptionPane.showMessageDialog(frame, "Wrong Port !", "Error", JOptionPane.ERROR_MESSAGE);
            btnBooleanSyslog.setSelected(false);
            return;
        }
        int ii=Integer.parseInt(frame.tfSyslogInput.getText());
        if (!(ii>0 && ii<65535)) {
            JOptionPane.showMessageDialog(frame, "Wrong Port !", "Error", JOptionPane.ERROR_MESSAGE);
            btnBooleanSyslog.setSelected(false);
            return;
        }        
        //ImageIcon iconOn = new ImageIcon(getClass().getResource("/img/get-16.png"));
        //ImageIcon iconOf = new ImageIcon(getClass().getResource("/img/stop-16.png"));
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PjSyslog.go(taSyslogResult);
            btnBooleanSyslog.setText("Stop syslog-server ");
            btnBooleanSyslog.setIcon(iconOf);
            tfSyslogInput.setEnabled(false);//.setEditable(false);
            System.out.println("button is selected");
        } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
            PjSyslog.stopDS();
            btnBooleanSyslog.setText("Run syslog-server ");
            btnBooleanSyslog.setIcon(iconOn);
            tfSyslogInput.setEnabled(true);//.setEditable(true);
            System.out.println("button is not selected");
        }
    }//GEN-LAST:event_btnBooleanSyslogItemStateChanged

    private void btnSyslogSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSyslogSaveActionPerformed
        PjSaveResult.Save(taSyslogResult);
    }//GEN-LAST:event_btnSyslogSaveActionPerformed

    private void btnSavePingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSavePingActionPerformed
        PjSaveResult.Save(taPingResult);
    }//GEN-LAST:event_btnSavePingActionPerformed

    private void btnHostTcpScannerSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHostTcpScannerSaveActionPerformed
        PjSaveResult.Save(taHostTcpScannerResult);
    }//GEN-LAST:event_btnHostTcpScannerSaveActionPerformed

    private void btnSaveDnsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveDnsActionPerformed
        PjSaveResult.Save(taDnsResult);
    }//GEN-LAST:event_btnSaveDnsActionPerformed

    private void btnSaveCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveCalcActionPerformed
        PjSaveResult.Save(taCalcResult);
    }//GEN-LAST:event_btnSaveCalcActionPerformed

    private void tfTraceInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTraceInputKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            PjTrace.runTrace(tfTraceInput, taTraceResult);
        }
    }//GEN-LAST:event_tfTraceInputKeyPressed

    private void btnTraceRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraceRunActionPerformed
        PjTrace.runTrace(tfTraceInput, taTraceResult);
    }//GEN-LAST:event_btnTraceRunActionPerformed

    private void btnSaveTraceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveTraceActionPerformed
        PjSaveResult.Save(taTraceResult);
    }//GEN-LAST:event_btnSaveTraceActionPerformed

    private void tfTelnetAdresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTelnetAdresKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            telnetClientHandler();
        }
    }//GEN-LAST:event_tfTelnetAdresKeyPressed

    private void btnTelnetRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTelnetRunActionPerformed
        telnetClientHandler();
    }//GEN-LAST:event_btnTelnetRunActionPerformed

    private void tfArpInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfArpInputKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            PjArp.runArpIP(tfArpInput, taArpResult);
        }
    }//GEN-LAST:event_tfArpInputKeyPressed

    private void btnArpRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArpRunActionPerformed
        PjArp.runArpIP(tfArpInput, taArpResult);
    }//GEN-LAST:event_btnArpRunActionPerformed

    private void btnSaveArpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveArpActionPerformed
        PjSaveResult.Save(taArpResult);
    }//GEN-LAST:event_btnSaveArpActionPerformed

    private void btnArpALLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArpALLActionPerformed
        PjArp.runArpAll(taArpResult);
    }//GEN-LAST:event_btnArpALLActionPerformed

    private void btnPingScannerRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPingScannerRunActionPerformed
        //taPingScannerResult.setText("Please Wait !");
        PjPingScanner.runGetResult(tfPingScannerInput, comboPingScannerMasks, taPingScannerResult);
    }//GEN-LAST:event_btnPingScannerRunActionPerformed

    private void btnPingScannerSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPingScannerSaveActionPerformed
        PjSaveResult.Save(taPingScannerResult);
    }//GEN-LAST:event_btnPingScannerSaveActionPerformed

    private void comboPingScannerShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboPingScannerShowActionPerformed
        switch (comboPingScannerShow.getSelectedItem().toString()) {
            case "ALL":
                this.taPingScannerResult.setText(PjPingScanner.result);
                break;
            case "UP":
                this.taPingScannerResult.setText(PjPingScanner.resultUP);
                break;
            case "DOWN":
                this.taPingScannerResult.setText(PjPingScanner.resultDOWN);
                break;
        }
    }//GEN-LAST:event_comboPingScannerShowActionPerformed

    private void tfTelnetPortKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTelnetPortKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            telnetClientHandler();
        }
    }//GEN-LAST:event_tfTelnetPortKeyPressed

    private void tfSnmpGetIPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfSnmpGetIPKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            PjSnmpGet.runGetResult(tfSnmpGetIP, tfSnmpGetOID, tfSnmpGetCommunity, taSnmpGet);
        }
    }//GEN-LAST:event_tfSnmpGetIPKeyPressed

    private void tfSnmpGetCommunityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfSnmpGetCommunityKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            PjSnmpGet.runGetResult(tfSnmpGetIP, tfSnmpGetOID, tfSnmpGetCommunity, taSnmpGet);
        }
    }//GEN-LAST:event_tfSnmpGetCommunityKeyPressed

    private void btnSnmpGetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSnmpGetActionPerformed
        PjSnmpGet.runGetResult(tfSnmpGetIP, tfSnmpGetOID, tfSnmpGetCommunity, taSnmpGet);
    }//GEN-LAST:event_btnSnmpGetActionPerformed

    private void tfSnmpGetOIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfSnmpGetOIDKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            PjSnmpGet.runGetResult(tfSnmpGetIP, tfSnmpGetOID, tfSnmpGetCommunity, taSnmpGet);
        }
    }//GEN-LAST:event_tfSnmpGetOIDKeyPressed

    private void comboSnmpVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboSnmpVersionActionPerformed
        PjSnmpGet.runGetResult(tfSnmpGetIP, tfSnmpGetOID, tfSnmpGetCommunity, taSnmpGet);
    }//GEN-LAST:event_comboSnmpVersionActionPerformed

    private void btnSnmpMibsAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSnmpMibsAllActionPerformed
        ImageIcon ii11 = new ImageIcon(new ImageIcon(getClass().getResource("/mib-tree-3.jpg")).getImage().getScaledInstance(480, 360, Image.SCALE_SMOOTH));
        ImageIcon ii22 = new ImageIcon(new ImageIcon(getClass().getResource("/snmp-all-well.gif")).getImage().getScaledInstance(480, 360, Image.SCALE_SMOOTH));
        JLabel lb11 = new JLabel("");
        JLabel lb22 = new JLabel("");
        lb11.setBorder(new TitledBorder("Example 1:"));
        lb22.setBorder(new TitledBorder("Example 2:"));
        //lb11.setVerticalTextPosition(SwingConstants.TOP);
        //lb11.setComponentOrientation(ComponentOrientation.);
        lb11.setIcon(ii11);
        lb22.setIcon(ii22);
        JPanel jp = new JPanel();
        jp.add(lb11);
        jp.add(lb22);
        //jp.se
        //JTextArea ta = new JTextArea(15,45);
        //ta.add(lb11);
        //ta.add(lb22);
        //JScrollPane jsc=new JScrollPane(jp);
        //jsc.setViewportView(jp); 
        //jsc.add(lb11);
        //jsc.add(lb22);
        Object[] ob = {jp};//{lb11,lb22};
        ImageIcon ii33 = new ImageIcon(getClass().getResource("/FrameIcon-3.png"));
        JOptionPane.showMessageDialog(frame, ob, "All snmp-mib structure:  1.3.6.1.2.1.* = Standard Snmp-MIB OIDs, 1.3.6.1.4.1.* = Enterprise Snmp-MIB OIDs", JOptionPane.INFORMATION_MESSAGE, ii33);
    }//GEN-LAST:event_btnSnmpMibsAllActionPerformed

    private void btnSnmpMibsStdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSnmpMibsStdActionPerformed
        //ImageIcon ii = new ImageIcon(new ImageIcon(getClass().getResource("/mib-tree-4.png")).getImage().getScaledInstance(672, 467, Image.SCALE_SMOOTH));
        ImageIcon ii11 = new ImageIcon(new ImageIcon(getClass().getResource("/mib-tree-4.png")).getImage().getScaledInstance(480, 360, Image.SCALE_SMOOTH));
        ImageIcon ii22 = new ImageIcon(new ImageIcon(getClass().getResource("/snmp-standart-well.jpg")).getImage().getScaledInstance(480, 360, Image.SCALE_SMOOTH));
        JLabel lb11 = new JLabel("");
        JLabel lb22 = new JLabel("");
        lb11.setBorder(new TitledBorder("Example 1:"));
        lb22.setBorder(new TitledBorder("Example 2:"));
        lb11.setIcon(ii11);
        lb22.setIcon(ii22);
        JPanel jp = new JPanel();
        jp.add(lb11);
        jp.add(lb22);
        Object[] ob = {jp};//{lb11,lb22};
        ImageIcon ii33 = new ImageIcon(getClass().getResource("/FrameIcon-3.png"));
        JOptionPane.showMessageDialog(frame, ob, "Structure of Standard Snmp-MIB OIDs = 1.3.6.1.2.1.* ", JOptionPane.INFORMATION_MESSAGE, ii33);
    }//GEN-LAST:event_btnSnmpMibsStdActionPerformed

    private void btnBooleanPingFloodItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnBooleanPingFloodItemStateChanged
        // TODO add your handling code here:
        if (!ipv.isValid(frame.tfPingFloodIP.getText().trim())) {
            JOptionPane.showMessageDialog(frame, "Wrong IP !", "Error", JOptionPane.ERROR_MESSAGE);
            btnBooleanPingFlood.setSelected(false);
            return;
        }
        //ImageIcon iconOn = new ImageIcon(getClass().getResource("/img/get-16.png"));
        //ImageIcon iconOf = new ImageIcon(getClass().getResource("/img/stop-16.png"));
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            PjPingFlood.go(taPingFloodResult);
            btnBooleanPingFlood.setText("Stop Ping Flood ");
            btnBooleanPingFlood.setIcon(iconOf);
            tfPingFloodIP.setEnabled(false);//.setEditable(false);
            //btnUdpFlood.setEnabled(false);
            System.out.println("button is selected");
        } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
            PjPingFlood.stop();
            btnBooleanPingFlood.setText("Run Ping Flood ");
            btnBooleanPingFlood.setIcon(iconOn);
            tfPingFloodIP.setEnabled(true);//.setEditable(true);
            //btnUdpFlood.setEnabled(true);
            System.out.println("button is not selected");
        }
    }//GEN-LAST:event_btnBooleanPingFloodItemStateChanged

    private void btnUdpFloodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUdpFloodActionPerformed
        // TODO add your handling code here:
        new UdpFlood_Thread();
    }//GEN-LAST:event_btnUdpFloodActionPerformed

    private void btnBooleanTftpItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnBooleanTftpItemStateChanged
        //ImageIcon iconOn = new ImageIcon(getClass().getResource("/img/get-16.png"));
        //ImageIcon iconOf = new ImageIcon(getClass().getResource("/img/stop-16.png"));
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            btnBooleanTftp.setText("Stop TFTP-server ");
            btnBooleanTftp.setIcon(iconOf);
            btnTftpSave.setEnabled(false);
            new Thread(() -> {
                TFTPServer.go();
            }).start();            
        } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
            btnBooleanTftp.setText("Run TFTP-server ");
            btnBooleanTftp.setIcon(iconOn);
            btnTftpSave.setEnabled(true);
            TFTPServer.stop();
        }
    }//GEN-LAST:event_btnBooleanTftpItemStateChanged

    private void btnTftpSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTftpSaveActionPerformed
        JFileChooser myd = new JFileChooser();
        //myd.addChoosableFileFilter(new AudioFileFilter());
        //myd.setAcceptAllFileFilterUsed(false);
        myd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        switch (myd.showDialog(frame, "Select Folder")) {
            case JFileChooser.APPROVE_OPTION:
                //ftpFolder = myd.getSelectedFile().getPath();
                tfTftpFolder.setText(myd.getSelectedFile().getPath());
                //ConfigFTP.folder=myd.getSelectedFile().getPath();
                //putd = myd.getSelectedFile() + "";
                break;
            case JFileChooser.CANCEL_OPTION:
                break;
        }//switch
    }//GEN-LAST:event_btnTftpSaveActionPerformed

    private void btnBooleanNtpItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnBooleanNtpItemStateChanged
        //ImageIcon iconOn = new ImageIcon(getClass().getResource("/img/get-16.png"));
        //ImageIcon iconOf = new ImageIcon(getClass().getResource("/img/stop-16.png"));
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            btnBooleanNtp.setText("Stop NTP-server ");
            btnBooleanNtp.setIcon(iconOf);
            spinnerNTP.setEnabled(false);
            new Thread(() -> {
                SimpleNTPServer.go();
            }).start();            
        } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
            btnBooleanNtp.setText("Run NTP-server ");
            btnBooleanNtp.setIcon(iconOn);
            spinnerNTP.setEnabled(true);
            SimpleNTPServer.stop();
        }
    }//GEN-LAST:event_btnBooleanNtpItemStateChanged

    private void btnMRTGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMRTGActionPerformed
        ClientMRTG.frameClientMRTG.setVisible(true);
        System.out.println("main MRTG thread = " + mrtgThread.getName());
    }//GEN-LAST:event_btnMRTGActionPerformed

    private void btnTcpPingRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTcpPingRunActionPerformed
        PingTCP.runGetResult(tfPingTCPip, tfPingTCPports, taPingTCPout);
    }//GEN-LAST:event_btnTcpPingRunActionPerformed

    private void btnSaveTcpPingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveTcpPingActionPerformed
        PjSaveResult.Save(taPingTCPout);
    }//GEN-LAST:event_btnSaveTcpPingActionPerformed

    private void btnBooleanFtpItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnBooleanFtpItemStateChanged
        //ImageIcon iconOn = new ImageIcon(getClass().getResource("/img/get-16.png"));
        //ImageIcon iconOf = new ImageIcon(getClass().getResource("/img/stop-16.png"));
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            btnBooleanFtp.setText("Stop FTP-server ");
            btnBooleanFtp.setIcon(iconOf);
            btnFtpSave.setEnabled(false);
            new Thread(() -> {
                FTPServer.go();
            }).start();            
        } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
            btnBooleanFtp.setText("Run FTP-server ");
            btnBooleanFtp.setIcon(iconOn);
            btnFtpSave.setEnabled(true);
            FTPServer.stop();
        }
    }//GEN-LAST:event_btnBooleanFtpItemStateChanged

    private void btnFtpSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFtpSaveActionPerformed
        JFileChooser myd = new JFileChooser();
        myd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        switch (myd.showDialog(frame, "Select Folder")) {
            case JFileChooser.APPROVE_OPTION:
                tfFtpFolder.setText(myd.getSelectedFile().getPath());
                break;
            case JFileChooser.CANCEL_OPTION:
                break;
        }//switch
    }//GEN-LAST:event_btnFtpSaveActionPerformed

    private void comboNetTcpScannerShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboNetTcpScannerShowActionPerformed
        switch (comboNetTcpScannerShow.getSelectedItem().toString()) {
            case "ALL":
                this.taNetTcpScannerResult.setText(NetTcpScanner.result);
                break;
            case "UP":
                this.taNetTcpScannerResult.setText(NetTcpScanner.resultUP);
                break;
            case "DOWN":
                this.taNetTcpScannerResult.setText(NetTcpScanner.resultDOWN);
                break;
        }        
    }//GEN-LAST:event_comboNetTcpScannerShowActionPerformed

    private void btnNetTcpScannerRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNetTcpScannerRunActionPerformed
        NetTcpScanner.runGetResult(tfNetTcpScannerIP, comboNetTcpScannerMasks, tfNetTcpScannerPort, taNetTcpScannerResult);
    }//GEN-LAST:event_btnNetTcpScannerRunActionPerformed

    private void btnNetTcpScannerSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNetTcpScannerSaveActionPerformed
        PjSaveResult.Save(taNetTcpScannerResult);
    }//GEN-LAST:event_btnNetTcpScannerSaveActionPerformed

    private void comboNetSnmpScannerShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboNetSnmpScannerShowActionPerformed
        switch (comboNetSnmpScannerShow.getSelectedItem().toString()) {
            case "ALL":
                this.taNetSnmpScannerResult.setText(NetSnmpScanner.result);
                break;
            case "UP":
                this.taNetSnmpScannerResult.setText(NetSnmpScanner.resultUP);
                break;
            case "DOWN":
                this.taNetSnmpScannerResult.setText(NetSnmpScanner.resultDOWN);
                break;
        }  
    }//GEN-LAST:event_comboNetSnmpScannerShowActionPerformed

    private void btnNetSnmpScannerRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNetSnmpScannerRunActionPerformed
        NetSnmpScanner.runGetResult(tfNetSnmpScannerIP, comboNetSnmpScannerMasks, taNetSnmpScannerResult);
    }//GEN-LAST:event_btnNetSnmpScannerRunActionPerformed

    private void btnNetSnmpScannerSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNetSnmpScannerSaveActionPerformed
        PjSaveResult.Save(taNetSnmpScannerResult);
    }//GEN-LAST:event_btnNetSnmpScannerSaveActionPerformed

    private void comboNetDnsScannerShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboNetDnsScannerShowActionPerformed
        switch (comboNetDnsScannerShow.getSelectedItem().toString()) {
            case "all":
                this.taNetDnsScannerResult.setText(NetDnsScanner.result);
                break;
            case "with name":
                this.taNetDnsScannerResult.setText(NetDnsScanner.resultUP);
                break;
            case "without name":
                this.taNetDnsScannerResult.setText(NetDnsScanner.resultDOWN);
                break;
        } 
    }//GEN-LAST:event_comboNetDnsScannerShowActionPerformed

    private void btnNetDnsScannerRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNetDnsScannerRunActionPerformed
        NetDnsScanner.runGetResult(tfNetDnsScannerIP, comboNetDnsScannerMasks, taNetDnsScannerResult);
    }//GEN-LAST:event_btnNetDnsScannerRunActionPerformed

    private void btnNetDnsScannerSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNetDnsScannerSaveActionPerformed
        PjSaveResult.Save(taNetDnsScannerResult);
    }//GEN-LAST:event_btnNetDnsScannerSaveActionPerformed

    private void comboHostTcpScannerShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboHostTcpScannerShowActionPerformed
        switch (comboHostTcpScannerShow.getSelectedItem().toString()) {
            case "ALL":
                this.taHostTcpScannerResult.setText(HostTcpScanner.result);
                break;
            case "UP":
                this.taHostTcpScannerResult.setText(HostTcpScanner.resultUP);
                break;
            case "DOWN":
                this.taHostTcpScannerResult.setText(HostTcpScanner.resultDOWN);
                break;
        }   
    }//GEN-LAST:event_comboHostTcpScannerShowActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            //FlatLightLaf.setup();
            //com.formdev.flatlaf.intellijthemes.FlatArcIJTheme.setup();
            //com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme.setup();
            //com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubIJTheme.setup();
            //com.formdev.flatlaf.intellijthemes.FlatHighContrastIJTheme.setup();
            //com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightIJTheme.setup();
            //com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightContrastIJTheme.setup();
            frame = new PjFrame();
            frame.InstallLF();
            frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
            //frame.setLF(frame);
            frame.setLF();
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            frame.setSize(FW, FH);
            //frame.setResizable(false);
            PjLocal.runLocalInfo(taLocalResult);
            //PjSnmpOidHelp.runSnmpHelp(taSnmpOidHelp);
            frame.setLocation(222, 222);
            frame.setVisible(true);
            System.out.println("Main Thead = " + Thread.currentThread().getName());
            mrtgThread = new Thread(() -> ClientMRTG.runMRTG());
            mrtgThread.start();
            System.out.println("main MRTG thread = " + mrtgThread.getName());
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnArpALL;
    public javax.swing.JButton btnArpRun;
    public static javax.swing.JToggleButton btnBooleanFtp;
    public static javax.swing.JToggleButton btnBooleanNtp;
    public static javax.swing.JToggleButton btnBooleanPingFlood;
    public static javax.swing.JToggleButton btnBooleanSyslog;
    public static javax.swing.JToggleButton btnBooleanTftp;
    private javax.swing.JButton btnCalcRun;
    private javax.swing.JButton btnDnsRun;
    public static javax.swing.JButton btnFtpSave;
    public javax.swing.JButton btnHostTcpScannerRun;
    public static javax.swing.JButton btnHostTcpScannerSave;
    private javax.swing.JButton btnMRTG;
    public static javax.swing.JButton btnNetDnsScannerRun;
    public static javax.swing.JButton btnNetDnsScannerSave;
    public static javax.swing.JButton btnNetSnmpScannerRun;
    public static javax.swing.JButton btnNetSnmpScannerSave;
    public static javax.swing.JButton btnNetTcpScannerRun;
    public static javax.swing.JButton btnNetTcpScannerSave;
    public javax.swing.JButton btnPingRun;
    public static javax.swing.JButton btnPingScannerRun;
    public static javax.swing.JButton btnPingScannerSave;
    private javax.swing.JButton btnSaveArp;
    private javax.swing.JButton btnSaveCalc;
    private javax.swing.JButton btnSaveDns;
    private javax.swing.JButton btnSavePing;
    private javax.swing.JButton btnSaveTcpPing;
    private javax.swing.JButton btnSaveTrace;
    public static javax.swing.JButton btnSnmpGet;
    public static javax.swing.JButton btnSnmpMibsAll;
    public static javax.swing.JButton btnSnmpMibsStd;
    private javax.swing.JButton btnSyslogReset;
    private javax.swing.JButton btnSyslogSave;
    public javax.swing.JButton btnTcpPingRun;
    public static javax.swing.JButton btnTelnetRun;
    public static javax.swing.JButton btnTftpSave;
    public javax.swing.JButton btnTraceRun;
    public static javax.swing.JButton btnUdpFlood;
    public static javax.swing.JComboBox<String> comboCalcMasks;
    public static javax.swing.JComboBox<String> comboHostTcpScannerShow;
    public static javax.swing.JComboBox<String> comboHostTcpScannerTimeouts;
    public static javax.swing.JComboBox<String> comboHostTcpScannerType;
    public static javax.swing.JComboBox<String> comboNetDnsScannerMasks;
    public static javax.swing.JComboBox<String> comboNetDnsScannerShow;
    public static javax.swing.JComboBox<String> comboNetSnmpScannerMasks;
    public static javax.swing.JComboBox<String> comboNetSnmpScannerOID;
    public static javax.swing.JComboBox<String> comboNetSnmpScannerShow;
    public static javax.swing.JComboBox<String> comboNetSnmpScannerTimeouts;
    public static javax.swing.JComboBox<String> comboNetSnmpScannerVersion;
    public static javax.swing.JComboBox<String> comboNetTcpScannerMasks;
    public static javax.swing.JComboBox<String> comboNetTcpScannerShow;
    public static javax.swing.JComboBox<String> comboNetTcpScannerTimeouts;
    public static javax.swing.JComboBox<String> comboPingCounts;
    public static javax.swing.JComboBox<String> comboPingFloodTimeouts;
    public static javax.swing.JComboBox<String> comboPingScannerMasks;
    public static javax.swing.JComboBox<String> comboPingScannerShow;
    public static javax.swing.JComboBox<String> comboPingScannerTimeouts;
    public static javax.swing.JComboBox<String> comboPingTimeouts;
    public static javax.swing.JComboBox<String> comboSnmpVersion;
    public static javax.swing.JEditorPane epAbout;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane29;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane30;
    private javax.swing.JScrollPane jScrollPane31;
    private javax.swing.JScrollPane jScrollPane32;
    private javax.swing.JScrollPane jScrollPane33;
    private javax.swing.JScrollPane jScrollPane34;
    private javax.swing.JScrollPane jScrollPane35;
    private javax.swing.JScrollPane jScrollPane36;
    private javax.swing.JScrollPane jScrollPane37;
    private javax.swing.JScrollPane jScrollPane38;
    private javax.swing.JScrollPane jScrollPane39;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane40;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator10;
    private javax.swing.JToolBar.Separator jSeparator100;
    private javax.swing.JToolBar.Separator jSeparator101;
    private javax.swing.JToolBar.Separator jSeparator102;
    private javax.swing.JToolBar.Separator jSeparator103;
    private javax.swing.JToolBar.Separator jSeparator104;
    private javax.swing.JToolBar.Separator jSeparator105;
    private javax.swing.JToolBar.Separator jSeparator106;
    private javax.swing.JToolBar.Separator jSeparator107;
    private javax.swing.JToolBar.Separator jSeparator108;
    private javax.swing.JToolBar.Separator jSeparator109;
    private javax.swing.JToolBar.Separator jSeparator11;
    private javax.swing.JToolBar.Separator jSeparator110;
    private javax.swing.JToolBar.Separator jSeparator111;
    private javax.swing.JToolBar.Separator jSeparator112;
    private javax.swing.JToolBar.Separator jSeparator12;
    private javax.swing.JToolBar.Separator jSeparator13;
    private javax.swing.JToolBar.Separator jSeparator14;
    private javax.swing.JToolBar.Separator jSeparator15;
    private javax.swing.JToolBar.Separator jSeparator16;
    private javax.swing.JToolBar.Separator jSeparator17;
    private javax.swing.JToolBar.Separator jSeparator18;
    private javax.swing.JToolBar.Separator jSeparator19;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator20;
    private javax.swing.JToolBar.Separator jSeparator21;
    private javax.swing.JToolBar.Separator jSeparator22;
    private javax.swing.JToolBar.Separator jSeparator23;
    private javax.swing.JToolBar.Separator jSeparator24;
    private javax.swing.JToolBar.Separator jSeparator25;
    private javax.swing.JToolBar.Separator jSeparator26;
    private javax.swing.JToolBar.Separator jSeparator27;
    private javax.swing.JToolBar.Separator jSeparator28;
    private javax.swing.JToolBar.Separator jSeparator29;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator30;
    private javax.swing.JToolBar.Separator jSeparator31;
    private javax.swing.JToolBar.Separator jSeparator32;
    private javax.swing.JToolBar.Separator jSeparator33;
    private javax.swing.JToolBar.Separator jSeparator34;
    private javax.swing.JToolBar.Separator jSeparator35;
    private javax.swing.JToolBar.Separator jSeparator36;
    private javax.swing.JToolBar.Separator jSeparator37;
    private javax.swing.JToolBar.Separator jSeparator38;
    private javax.swing.JToolBar.Separator jSeparator39;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator40;
    private javax.swing.JToolBar.Separator jSeparator41;
    private javax.swing.JToolBar.Separator jSeparator42;
    private javax.swing.JToolBar.Separator jSeparator43;
    private javax.swing.JToolBar.Separator jSeparator44;
    private javax.swing.JToolBar.Separator jSeparator45;
    private javax.swing.JToolBar.Separator jSeparator46;
    private javax.swing.JToolBar.Separator jSeparator47;
    private javax.swing.JToolBar.Separator jSeparator48;
    private javax.swing.JToolBar.Separator jSeparator49;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator50;
    private javax.swing.JToolBar.Separator jSeparator51;
    private javax.swing.JToolBar.Separator jSeparator52;
    private javax.swing.JToolBar.Separator jSeparator53;
    private javax.swing.JToolBar.Separator jSeparator54;
    private javax.swing.JToolBar.Separator jSeparator55;
    private javax.swing.JToolBar.Separator jSeparator56;
    private javax.swing.JToolBar.Separator jSeparator57;
    private javax.swing.JToolBar.Separator jSeparator58;
    private javax.swing.JToolBar.Separator jSeparator59;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator60;
    private javax.swing.JToolBar.Separator jSeparator61;
    private javax.swing.JToolBar.Separator jSeparator62;
    private javax.swing.JToolBar.Separator jSeparator63;
    private javax.swing.JToolBar.Separator jSeparator64;
    private javax.swing.JToolBar.Separator jSeparator65;
    private javax.swing.JToolBar.Separator jSeparator66;
    private javax.swing.JToolBar.Separator jSeparator67;
    private javax.swing.JToolBar.Separator jSeparator68;
    private javax.swing.JToolBar.Separator jSeparator69;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator70;
    private javax.swing.JToolBar.Separator jSeparator71;
    private javax.swing.JToolBar.Separator jSeparator72;
    private javax.swing.JToolBar.Separator jSeparator73;
    private javax.swing.JToolBar.Separator jSeparator74;
    private javax.swing.JToolBar.Separator jSeparator75;
    private javax.swing.JToolBar.Separator jSeparator76;
    private javax.swing.JToolBar.Separator jSeparator77;
    private javax.swing.JToolBar.Separator jSeparator78;
    private javax.swing.JToolBar.Separator jSeparator79;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToolBar.Separator jSeparator80;
    private javax.swing.JToolBar.Separator jSeparator81;
    private javax.swing.JToolBar.Separator jSeparator82;
    private javax.swing.JToolBar.Separator jSeparator83;
    private javax.swing.JToolBar.Separator jSeparator84;
    private javax.swing.JToolBar.Separator jSeparator85;
    private javax.swing.JToolBar.Separator jSeparator86;
    private javax.swing.JToolBar.Separator jSeparator87;
    private javax.swing.JToolBar.Separator jSeparator88;
    private javax.swing.JToolBar.Separator jSeparator89;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JToolBar.Separator jSeparator90;
    private javax.swing.JToolBar.Separator jSeparator91;
    private javax.swing.JToolBar.Separator jSeparator92;
    private javax.swing.JToolBar.Separator jSeparator93;
    private javax.swing.JToolBar.Separator jSeparator94;
    private javax.swing.JToolBar.Separator jSeparator95;
    private javax.swing.JToolBar.Separator jSeparator96;
    private javax.swing.JToolBar.Separator jSeparator97;
    private javax.swing.JToolBar.Separator jSeparator98;
    private javax.swing.JToolBar.Separator jSeparator99;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar10;
    private javax.swing.JToolBar jToolBar11;
    private javax.swing.JToolBar jToolBar12;
    private javax.swing.JToolBar jToolBar13;
    private javax.swing.JToolBar jToolBar14;
    private javax.swing.JToolBar jToolBar15;
    private javax.swing.JToolBar jToolBar16;
    private javax.swing.JToolBar jToolBar17;
    private javax.swing.JToolBar jToolBar18;
    private javax.swing.JToolBar jToolBar19;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar20;
    private javax.swing.JToolBar jToolBar21;
    private javax.swing.JToolBar jToolBar22;
    private javax.swing.JToolBar jToolBar23;
    private javax.swing.JToolBar jToolBar24;
    private javax.swing.JToolBar jToolBar25;
    private javax.swing.JToolBar jToolBar26;
    private javax.swing.JToolBar jToolBar27;
    private javax.swing.JToolBar jToolBar28;
    private javax.swing.JToolBar jToolBar29;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar30;
    private javax.swing.JToolBar jToolBar31;
    private javax.swing.JToolBar jToolBar32;
    private javax.swing.JToolBar jToolBar33;
    private javax.swing.JToolBar jToolBar34;
    private javax.swing.JToolBar jToolBar35;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JToolBar jToolBar7;
    private javax.swing.JToolBar jToolBar8;
    private javax.swing.JToolBar jToolBar9;
    public static javax.swing.JLabel nizInfoLabel;
    public static javax.swing.JSpinner spinnerNTP;
    public static javax.swing.JTextArea taArpResult;
    public static javax.swing.JTextArea taCalcResult;
    public static javax.swing.JTextArea taDnsResult;
    public static javax.swing.JTextArea taFtpResult;
    public static javax.swing.JTextArea taHostTcpScannerResult;
    public static javax.swing.JTextArea taLocalResult;
    public static javax.swing.JTextArea taNetDnsScannerResult;
    public static javax.swing.JTextArea taNetSnmpScannerResult;
    public static javax.swing.JTextArea taNetTcpScannerResult;
    public static javax.swing.JTextArea taNtpResult;
    public static javax.swing.JTextArea taPingFloodResult;
    public static javax.swing.JTextArea taPingResult;
    public static javax.swing.JTextArea taPingScannerResult;
    public static javax.swing.JTextArea taPingTCPout;
    public static javax.swing.JTextArea taSnmpGet;
    public static javax.swing.JTextArea taSnmpOidHelp;
    public static javax.swing.JTextArea taSyslogResult;
    public static javax.swing.JTextArea taTftpResult;
    public static javax.swing.JTextArea taTraceResult;
    public static javax.swing.JTextField tfArpInput;
    public static javax.swing.JTextField tfCalcInput;
    public static javax.swing.JTextField tfDnsInput;
    public static javax.swing.JTextField tfFtpFolder;
    public static javax.swing.JTextField tfHostTcpScannerIP;
    public static javax.swing.JTextField tfNetDnsScannerIP;
    public static javax.swing.JTextField tfNetSnmpScannerCommunity;
    public static javax.swing.JTextField tfNetSnmpScannerIP;
    public static javax.swing.JTextField tfNetTcpScannerIP;
    public static javax.swing.JTextField tfNetTcpScannerPort;
    public static javax.swing.JTextField tfPingFloodIP;
    public static javax.swing.JTextField tfPingInput;
    public static javax.swing.JTextField tfPingScannerInput;
    public static javax.swing.JTextField tfPingTCPip;
    public static javax.swing.JTextField tfPingTCPports;
    public static javax.swing.JTextField tfSnmpGetCommunity;
    public static javax.swing.JTextField tfSnmpGetIP;
    public static javax.swing.JTextField tfSnmpGetOID;
    public static javax.swing.JTextField tfSyslogInput;
    public static javax.swing.JTextField tfTelnetAdres;
    public static javax.swing.JTextField tfTelnetPort;
    public static javax.swing.JTextField tfTftpFolder;
    public static javax.swing.JTextField tfTraceInput;
    // End of variables declaration//GEN-END:variables
}
