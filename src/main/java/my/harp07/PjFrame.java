package my.harp07;

import java.awt.Desktop;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import static my.harp07.PjCalc.CIDRS_MASKS;
import static my.harp07.PjPing.COUNTS;
import static my.harp07.PjPing.TIMEOUTS;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;

public class PjFrame extends javax.swing.JFrame {

    public static PjFrame frame;
    public static int FW = 700;
    public static int FH = 540;    
    private static InetAddressValidator ipv = InetAddressValidator.getInstance();
    public static List<String> lookAndFeelsDisplay = new ArrayList<>();
    public static List<String> lookAndFeelsRealNames = new ArrayList<>();
    public static String currentLAF="net.sf.tinylaf.TinyLookAndFeel";
    public static String currentTheme="lib/themes/My_Cyan.theme";
    public static List<String> tinyTemes = new ArrayList<>();
    //public static String currentLAF = "org.pushingpixels.substance.api.skin.SubstanceSaharaLookAndFeel";
    //public static String currentLAF = "javax.swing.plaf.metal.MetalLookAndFeel";

    public PjFrame() {
        initComponents();
        this.setTitle("Pure Java Network Tools");
        this.comboPingCounts.setModel(new javax.swing.DefaultComboBoxModel(COUNTS));
        this.comboPingTimeouts.setModel(new javax.swing.DefaultComboBoxModel(TIMEOUTS));
        this.comboCalcMasks.setModel(new javax.swing.DefaultComboBoxModel(CIDRS_MASKS));
        ImageIcon icone = new ImageIcon(getClass().getResource("/img/globe-net-16.png"));
        this.setIconImage(icone.getImage());
        this.nizInfoLabel.setText(" Version 1.0.10, build 16-09-2020.");
        //this.epAbout.setEditorKit(new HTMLEditorKit());
        this.epAbout.setContentType("text/html");
        String msg = "<html><body><p style='margin-left: 50px'><br>PJ-NET-TOOLS:<br><br>"
                + "\nPure Java Network Tools. <br>Include:<br>"
                + "\n1. ICMP-ping;<br>"
                + "\n2. ICMP-trace;<br>"                
                + "\n3. DNS-checker.<br>"
                + "\n4. TCP-scaner.<br>"
                + "\n5. IP-calculator.<br>"
                + "\n6. Syslog-server.<br>"
                + "\n7. Telnet-client.<br>"
                + "\nCreate by Roman Koldaev,<br>"
                + "\nSaratov city, Russia.<br>"
                + "\nmail: <A HREF='mailto:harp07@mail.ru'> harp07@mail.ru </A><br>"
                + "\nSourceForge: <a href='https://sf.net/u/harp07/profile/'>https://sf.net/u/harp07/profile/</a><br>"
                + "\nGitHub: <a href='https://github.com/harp077/'>https://github.com/harp077/</a><br>"
                + "\nneed JRE-1.8<br></p></body></html>";
        this.epAbout.setText(msg);
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

    }

    public static void MyInstLF(String lf) {
        //UIManager.installLookAndFeel(lf,lf);  
        lookAndFeelsDisplay.add(lf);
        lookAndFeelsRealNames.add(lf);
    }

    public static void InstallLF() {
        tinyTemes.add("lib/themes/Default.theme");
        tinyTemes.add("lib/themes/Forest.theme");
        tinyTemes.add("lib/themes/Golden.theme");
        tinyTemes.add("lib/themes/Plastic.theme");
        tinyTemes.add("lib/themes/Silver.theme");
        tinyTemes.add("lib/themes/Nightly.theme");
        //tinyTemes.add("lib/themes/Unicode.theme");
        tinyTemes.add("lib/themes/My_Cyan.theme");
        tinyTemes.add("lib/themes/My_Yellow.theme");  
        tinyTemes.add("lib/themes/My_AquaMarine.theme"); 
        tinyTemes.add("lib/themes/My_Magenta.theme");  
        tinyTemes.add("lib/themes/My_Green.theme"); 
        MyInstLF("net.sf.tinylaf.TinyLookAndFeel"); 
        MyInstLF("javax.swing.plaf.metal.MetalLookAndFeel");
        ///////////////////////        
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
    }
    
    public static void setLF() {
        if (currentLAF.contains("tinylaf")) {
            net.sf.tinylaf.Theme.loadTheme(new File(currentTheme));
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

    public void changeLF() {
        //Actions.currentTheme="";
        //for (UIManager.LookAndFeelInfo each : UIManager.getInstalledLookAndFeels()) {
        //lookAndFeelsDisplay.add(each.getName());
        //lookAndFeelsRealNames.add(each.getClassName());
        //}
        //String changeLook = (String) JOptionPane.showInputDialog(this, "Choose Look and Feel Here:", "Select Look and Feel", JOptionPane.QUESTION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/JSK/img/color_swatch.png")), lookAndFeelsDisplay.toArray(), null);
        String changeLook = "net.sf.tinylaf.TinyLookAndFeel";
        if (changeLook.contains("net.sf.tinylaf")) {
            currentTheme = (String) JOptionPane.showInputDialog(this, "Set TinyLF Theme:", "Select TinyLF Theme", JOptionPane.QUESTION_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/img/color_swatch.png")), tinyTemes.toArray(), null);
        }
        if (changeLook != null) {
            for (int a = 0; a < lookAndFeelsDisplay.size(); a++) {
                if (changeLook.equals(lookAndFeelsDisplay.get(a))) {
                    currentLAF = lookAndFeelsRealNames.get(a);
                    setLF();
                    break;
                }
            }
        }
    }    

    /*public void changeLF() {
        String changeLook = (String) JOptionPane.showInputDialog(frame, "Choose Look and Feel Here:", "Select Look and Feel", JOptionPane.QUESTION_MESSAGE, new ImageIcon(getClass().getResource("/img/color_swatch.png")), lookAndFeelsDisplay.toArray(), null);
        if (changeLook != null) {
            for (int a = 0; a < lookAndFeelsDisplay.size(); a++) {
                if (changeLook.equals(lookAndFeelsDisplay.get(a))) {
                    currentLAF = lookAndFeelsRealNames.get(a);
                    setLF(frame);
                    break;
                }
            }
        }
    }*/

    /*public void setLF(JFrame frame) {
        try {
            UIManager.setLookAndFeel(currentLAF);
        } catch (InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
        }
        SwingUtilities.updateComponentTreeUI(frame);
        //frame.pack();
    }*/

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
        jSeparator1 = new javax.swing.JToolBar.Separator();
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
        btnPingReset = new javax.swing.JButton();
        jScrollPane14 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        taTraceResult = new javax.swing.JTextArea();
        jToolBar13 = new javax.swing.JToolBar();
        jSeparator35 = new javax.swing.JToolBar.Separator();
        jLabel9 = new javax.swing.JLabel();
        jSeparator36 = new javax.swing.JToolBar.Separator();
        tfTraceInput = new javax.swing.JTextField();
        jSeparator37 = new javax.swing.JToolBar.Separator();
        jToolBar14 = new javax.swing.JToolBar();
        jSeparator38 = new javax.swing.JToolBar.Separator();
        btnTraceRun = new javax.swing.JButton();
        jSeparator39 = new javax.swing.JToolBar.Separator();
        btnSaveTrace = new javax.swing.JButton();
        jSeparator40 = new javax.swing.JToolBar.Separator();
        btnTraceReset = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        taTcpResult = new javax.swing.JTextArea();
        jToolBar5 = new javax.swing.JToolBar();
        jSeparator22 = new javax.swing.JToolBar.Separator();
        jLabel4 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        tfTcpInput = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jToolBar6 = new javax.swing.JToolBar();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        btnTcpRun = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        btnSaveTcp = new javax.swing.JButton();
        jSeparator30 = new javax.swing.JToolBar.Separator();
        btnTcpReset = new javax.swing.JButton();
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
        btnDnsReset = new javax.swing.JButton();
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
        btnCalcReset = new javax.swing.JButton();
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
        jScrollPane16 = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        jToolBar15 = new javax.swing.JToolBar();
        jSeparator41 = new javax.swing.JToolBar.Separator();
        jLabel11 = new javax.swing.JLabel();
        jSeparator42 = new javax.swing.JToolBar.Separator();
        tfTelnetInput = new javax.swing.JTextField();
        jSeparator43 = new javax.swing.JToolBar.Separator();
        jToolBar16 = new javax.swing.JToolBar();
        jSeparator44 = new javax.swing.JToolBar.Separator();
        btnTelnetRun = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        taLocalResult = new javax.swing.JTextArea();
        jScrollPane13 = new javax.swing.JScrollPane();
        epAbout = new javax.swing.JEditorPane();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator23 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator33 = new javax.swing.JToolBar.Separator();
        btnExit = new javax.swing.JButton();
        jToolBar4 = new javax.swing.JToolBar();
        jSeparator34 = new javax.swing.JToolBar.Separator();
        nizInfoLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Pinger"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane6.setBorder(javax.swing.BorderFactory.createTitledBorder("Result:"));

        taPingResult.setEditable(false);
        taPingResult.setColumns(20);
        taPingResult.setRows(5);
        jScrollPane6.setViewportView(taPingResult);

        jPanel1.add(jScrollPane6, java.awt.BorderLayout.CENTER);

        jToolBar2.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar2.setRollover(true);
        jToolBar2.add(jSeparator13);

        jLabel2.setText("Enter IP/DNS:");
        jToolBar2.add(jLabel2);
        jToolBar2.add(jSeparator1);

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
        jToolBar3.setRollover(true);
        jToolBar3.add(jSeparator3);

        btnPingRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/get-16.png"))); // NOI18N
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

        btnPingReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit_clear-16.png"))); // NOI18N
        btnPingReset.setText("Clear ");
        btnPingReset.setFocusable(false);
        btnPingReset.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnPingReset.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPingReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPingResetActionPerformed(evt);
            }
        });
        jToolBar3.add(btnPingReset);

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
        jToolBar13.setRollover(true);
        jToolBar13.add(jSeparator35);

        jLabel9.setText("Enter IP-address/DNS-name:");
        jToolBar13.add(jLabel9);
        jToolBar13.add(jSeparator36);

        tfTraceInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTraceInputKeyPressed(evt);
            }
        });
        jToolBar13.add(tfTraceInput);
        jToolBar13.add(jSeparator37);

        jPanel7.add(jToolBar13, java.awt.BorderLayout.NORTH);

        jToolBar14.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar14.setRollover(true);
        jToolBar14.add(jSeparator38);

        btnTraceRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/get-16.png"))); // NOI18N
        btnTraceRun.setText("Trace run ");
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

        btnTraceReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit_clear-16.png"))); // NOI18N
        btnTraceReset.setText("Clear  ");
        btnTraceReset.setFocusable(false);
        btnTraceReset.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnTraceReset.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTraceReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraceResetActionPerformed(evt);
            }
        });
        jToolBar14.add(btnTraceReset);

        jPanel7.add(jToolBar14, java.awt.BorderLayout.SOUTH);

        jScrollPane14.setViewportView(jPanel7);

        jTabbedPane1.addTab("ICMP-Trace", new javax.swing.ImageIcon(getClass().getResource("/img/trace-16.png")), jScrollPane14, ""); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("TCP-scaner"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jScrollPane7.setBorder(javax.swing.BorderFactory.createTitledBorder("Result:"));

        taTcpResult.setEditable(false);
        taTcpResult.setColumns(20);
        taTcpResult.setRows(5);
        jScrollPane7.setViewportView(taTcpResult);

        jPanel2.add(jScrollPane7, java.awt.BorderLayout.CENTER);

        jToolBar5.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar5.setRollover(true);
        jToolBar5.add(jSeparator22);

        jLabel4.setText("Enter IP-address:");
        jToolBar5.add(jLabel4);
        jToolBar5.add(jSeparator5);

        tfTcpInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTcpInputKeyPressed(evt);
            }
        });
        jToolBar5.add(tfTcpInput);
        jToolBar5.add(jSeparator6);

        jPanel2.add(jToolBar5, java.awt.BorderLayout.NORTH);

        jToolBar6.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar6.setRollover(true);
        jToolBar6.add(jSeparator7);

        btnTcpRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/get-16.png"))); // NOI18N
        btnTcpRun.setText("Run TCP-scaner ");
        btnTcpRun.setFocusable(false);
        btnTcpRun.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnTcpRun.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTcpRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTcpRunActionPerformed(evt);
            }
        });
        jToolBar6.add(btnTcpRun);
        jToolBar6.add(jSeparator8);

        btnSaveTcp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save-16.png"))); // NOI18N
        btnSaveTcp.setText("Save Result ");
        btnSaveTcp.setFocusable(false);
        btnSaveTcp.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSaveTcp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveTcpActionPerformed(evt);
            }
        });
        jToolBar6.add(btnSaveTcp);
        jToolBar6.add(jSeparator30);

        btnTcpReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit_clear-16.png"))); // NOI18N
        btnTcpReset.setText("Clear ");
        btnTcpReset.setFocusable(false);
        btnTcpReset.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnTcpReset.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTcpReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTcpResetActionPerformed(evt);
            }
        });
        jToolBar6.add(btnTcpReset);

        jPanel2.add(jToolBar6, java.awt.BorderLayout.SOUTH);

        jScrollPane4.setViewportView(jPanel2);

        jTabbedPane1.addTab("TCP-scan", new javax.swing.ImageIcon(getClass().getResource("/img/radiolocator-16.png")), jScrollPane4); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("DNS-checker"));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jScrollPane8.setBorder(javax.swing.BorderFactory.createTitledBorder("Result:"));

        taDnsResult.setEditable(false);
        taDnsResult.setColumns(20);
        taDnsResult.setRows(5);
        jScrollPane8.setViewportView(taDnsResult);

        jPanel3.add(jScrollPane8, java.awt.BorderLayout.CENTER);

        jToolBar7.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar7.setRollover(true);
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
        jToolBar8.setRollover(true);
        jToolBar8.add(jSeparator11);

        btnDnsRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/get-16.png"))); // NOI18N
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

        btnDnsReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit_clear-16.png"))); // NOI18N
        btnDnsReset.setText("Clear  ");
        btnDnsReset.setFocusable(false);
        btnDnsReset.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnDnsReset.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDnsReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDnsResetActionPerformed(evt);
            }
        });
        jToolBar8.add(btnDnsReset);

        jPanel3.add(jToolBar8, java.awt.BorderLayout.SOUTH);

        jScrollPane2.setViewportView(jPanel3);

        jTabbedPane1.addTab("DNS-check", new javax.swing.ImageIcon(getClass().getResource("/img/dns-16.png")), jScrollPane2); // NOI18N

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("IP-calculator"));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jScrollPane9.setBorder(javax.swing.BorderFactory.createTitledBorder("Result:"));

        taCalcResult.setEditable(false);
        taCalcResult.setColumns(20);
        taCalcResult.setRows(5);
        jScrollPane9.setViewportView(taCalcResult);

        jPanel4.add(jScrollPane9, java.awt.BorderLayout.CENTER);

        jToolBar9.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar9.setRollover(true);
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
        jToolBar10.setRollover(true);
        jToolBar10.add(jSeparator15);

        btnCalcRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/get-16.png"))); // NOI18N
        btnCalcRun.setText("Calculate IP ");
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

        btnCalcReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit_clear-16.png"))); // NOI18N
        btnCalcReset.setText("Clear ");
        btnCalcReset.setFocusable(false);
        btnCalcReset.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnCalcReset.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCalcReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcResetActionPerformed(evt);
            }
        });
        jToolBar10.add(btnCalcReset);

        jPanel4.add(jToolBar10, java.awt.BorderLayout.SOUTH);

        jScrollPane3.setViewportView(jPanel4);

        jTabbedPane1.addTab("IP-calculator", new javax.swing.ImageIcon(getClass().getResource("/img/ip-blue-16.png")), jScrollPane3); // NOI18N

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Syslog-Server"));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jScrollPane11.setBorder(javax.swing.BorderFactory.createTitledBorder("Received messages:"));

        taSyslogResult.setEditable(false);
        taSyslogResult.setColumns(20);
        taSyslogResult.setRows(5);
        jScrollPane11.setViewportView(taSyslogResult);

        jPanel5.add(jScrollPane11, java.awt.BorderLayout.CENTER);

        jToolBar11.setBorder(javax.swing.BorderFactory.createTitledBorder("UDP-port:"));
        jToolBar11.setRollover(true);
        jToolBar11.add(jSeparator24);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Select syslog-server UDP-port:  ");
        jToolBar11.add(jLabel10);
        jToolBar11.add(tfSyslogInput);
        jToolBar11.add(jSeparator29);

        jPanel5.add(jToolBar11, java.awt.BorderLayout.NORTH);

        jToolBar12.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar12.setRollover(true);
        jToolBar12.add(jSeparator27);

        btnBooleanSyslog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/get-16.png"))); // NOI18N
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

        jTabbedPane1.addTab("Syslog", new javax.swing.ImageIcon(getClass().getResource("/img/syslog-16.png")), jScrollPane10); // NOI18N

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("telnet"));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jToolBar15.setBorder(javax.swing.BorderFactory.createTitledBorder("Input:"));
        jToolBar15.setRollover(true);
        jToolBar15.add(jSeparator41);

        jLabel11.setText("Enter IP-address:");
        jToolBar15.add(jLabel11);
        jToolBar15.add(jSeparator42);

        tfTelnetInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTelnetInputKeyPressed(evt);
            }
        });
        jToolBar15.add(tfTelnetInput);
        jToolBar15.add(jSeparator43);

        jPanel8.add(jToolBar15, java.awt.BorderLayout.NORTH);

        jToolBar16.setBorder(javax.swing.BorderFactory.createTitledBorder("actions:"));
        jToolBar16.setRollover(true);
        jToolBar16.add(jSeparator44);

        btnTelnetRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/get-16.png"))); // NOI18N
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

        jTabbedPane1.addTab("Telnet-client", new javax.swing.ImageIcon(getClass().getResource("/img/connect_16.png")), jScrollPane16); // NOI18N

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

        jToolBar1.setBorder(javax.swing.BorderFactory.createTitledBorder("Tools"));
        jToolBar1.setFloatable(false);
        jToolBar1.add(jSeparator23);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/select-color-24.png"))); // NOI18N
        jButton1.setToolTipText("Cange Skin");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jToolBar1.add(jSeparator33);

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit-24.png"))); // NOI18N
        btnExit.setFocusable(false);
        btnExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        jToolBar1.add(btnExit);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);

        jToolBar4.setBorder(javax.swing.BorderFactory.createTitledBorder("Info:"));
        jToolBar4.setRollover(true);
        jToolBar4.add(jSeparator34);

        nizInfoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/globe-net-16.png"))); // NOI18N
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

    private void btnPingResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPingResetActionPerformed
        tfPingInput.setText("");
        taPingResult.setText("");
    }//GEN-LAST:event_btnPingResetActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        changeLF();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnCalcResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcResetActionPerformed
        tfCalcInput.setText("");
        taCalcResult.setText("");
    }//GEN-LAST:event_btnCalcResetActionPerformed

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

    private void btnDnsResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDnsResetActionPerformed
        tfDnsInput.setText("");
        taDnsResult.setText("");
    }//GEN-LAST:event_btnDnsResetActionPerformed

    private void btnTcpRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTcpRunActionPerformed
        PjTcp.runGetResult(tfTcpInput, taTcpResult);
    }//GEN-LAST:event_btnTcpRunActionPerformed

    private void tfTcpInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTcpInputKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            PjTcp.runGetResult(tfTcpInput, taTcpResult);
        }
    }//GEN-LAST:event_tfTcpInputKeyPressed

    private void btnTcpResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTcpResetActionPerformed
        tfTcpInput.setText("");
        taTcpResult.setText("");
    }//GEN-LAST:event_btnTcpResetActionPerformed

    private void btnSyslogResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSyslogResetActionPerformed
        taSyslogResult.setText("");
    }//GEN-LAST:event_btnSyslogResetActionPerformed

    private void btnBooleanSyslogItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnBooleanSyslogItemStateChanged
        if (!StringUtils.isNumeric(frame.tfSyslogInput.getText())) {
            JOptionPane.showMessageDialog(frame, "Wrong Port !", "Error", JOptionPane.ERROR_MESSAGE);
            btnBooleanSyslog.setSelected(false);
            return;
        }
        ImageIcon iconOn = new ImageIcon(getClass().getResource("/img/get-16.png"));
        ImageIcon iconOf = new ImageIcon(getClass().getResource("/img/stop-16.png"));
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

    private void btnSaveTcpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveTcpActionPerformed
        PjSaveResult.Save(taTcpResult);
    }//GEN-LAST:event_btnSaveTcpActionPerformed

    private void btnSaveDnsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveDnsActionPerformed
        PjSaveResult.Save(taDnsResult);
    }//GEN-LAST:event_btnSaveDnsActionPerformed

    private void btnSaveCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveCalcActionPerformed
        PjSaveResult.Save(taCalcResult);
    }//GEN-LAST:event_btnSaveCalcActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        int r = JOptionPane.showConfirmDialog(frame, "Really Quit ?", "Quit ?", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_btnExitActionPerformed

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

    private void btnTraceResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraceResetActionPerformed
        tfTraceInput.setText("");
        taTraceResult.setText("");
    }//GEN-LAST:event_btnTraceResetActionPerformed

    private void tfTelnetInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTelnetInputKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (ipv.isValid(tfTelnetInput.getText().trim())) {
                new TelnetNNM_Thread(tfTelnetInput.getText().trim());
            }
            else {
                JOptionPane.showMessageDialog(frame, "Wrong IP !", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_tfTelnetInputKeyPressed

    private void btnTelnetRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTelnetRunActionPerformed
        if (ipv.isValid(tfTelnetInput.getText().trim())) {
            new TelnetNNM_Thread(tfTelnetInput.getText().trim());
        }
        else {
            JOptionPane.showMessageDialog(frame, "Wrong IP !", "Error", JOptionPane.ERROR_MESSAGE);
        }        
    }//GEN-LAST:event_btnTelnetRunActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
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
            frame.setVisible(true);
            System.out.println("Main Thead = " + Thread.currentThread().getName());
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JToggleButton btnBooleanSyslog;
    private javax.swing.JButton btnCalcReset;
    private javax.swing.JButton btnCalcRun;
    private javax.swing.JButton btnDnsReset;
    private javax.swing.JButton btnDnsRun;
    private javax.swing.JButton btnExit;
    public static javax.swing.JButton btnPingReset;
    public javax.swing.JButton btnPingRun;
    private javax.swing.JButton btnSaveCalc;
    private javax.swing.JButton btnSaveDns;
    private javax.swing.JButton btnSavePing;
    private javax.swing.JButton btnSaveTcp;
    private javax.swing.JButton btnSaveTrace;
    private javax.swing.JButton btnSyslogReset;
    private javax.swing.JButton btnSyslogSave;
    private javax.swing.JButton btnTcpReset;
    public javax.swing.JButton btnTcpRun;
    public static javax.swing.JButton btnTelnetRun;
    private javax.swing.JButton btnTraceReset;
    public javax.swing.JButton btnTraceRun;
    public static javax.swing.JComboBox<String> comboCalcMasks;
    public static javax.swing.JComboBox<String> comboPingCounts;
    public static javax.swing.JComboBox<String> comboPingTimeouts;
    public static javax.swing.JEditorPane epAbout;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator10;
    private javax.swing.JToolBar.Separator jSeparator11;
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
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar10;
    private javax.swing.JToolBar jToolBar11;
    private javax.swing.JToolBar jToolBar12;
    private javax.swing.JToolBar jToolBar13;
    private javax.swing.JToolBar jToolBar14;
    private javax.swing.JToolBar jToolBar15;
    private javax.swing.JToolBar jToolBar16;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JToolBar jToolBar7;
    private javax.swing.JToolBar jToolBar8;
    private javax.swing.JToolBar jToolBar9;
    public static javax.swing.JLabel nizInfoLabel;
    public static javax.swing.JTextArea taCalcResult;
    public static javax.swing.JTextArea taDnsResult;
    public static javax.swing.JTextArea taLocalResult;
    public static javax.swing.JTextArea taPingResult;
    public static javax.swing.JTextArea taSyslogResult;
    public static javax.swing.JTextArea taTcpResult;
    public static javax.swing.JTextArea taTraceResult;
    public static javax.swing.JTextField tfCalcInput;
    public static javax.swing.JTextField tfDnsInput;
    public static javax.swing.JTextField tfPingInput;
    public static javax.swing.JTextField tfSyslogInput;
    public static javax.swing.JTextField tfTcpInput;
    public static javax.swing.JTextField tfTelnetInput;
    public static javax.swing.JTextField tfTraceInput;
    // End of variables declaration//GEN-END:variables
}
