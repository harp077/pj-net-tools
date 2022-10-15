package jpacketgenerator;

import java.awt.event.ItemEvent;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

// First maked by = https://sourceforge.net/projects/jpktgenerator/
// changed = https://github.com/mathewbray/Java-PacketGenerator
public class JPacketGeneratorGUI extends javax.swing.JFrame {

    private PacketGenerator pGenerator;

    public JPacketGeneratorGUI() throws SocketException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        initComponents();
        initSourceAddress();
        //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        //UIManager.setLookAndFeel(new MetalLookAndFeel());
        //--- Make buttons and stuff look less crappy - this also allows background coloring
        //String laf = UIManager.getSystemLookAndFeelClassName();
        //UIManager.setLookAndFeel(laf);
        ImageIcon icone = new ImageIcon(getClass().getResource("/FrameIcon-3.png"));
        this.setIconImage(icone.getImage());        
        this.setSize(350, 280);
        this.setLocation(222, 222);
    }
    
    private void initSourceAddress() throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress addr : Collections.list(inetAddresses)) {
                if (addr instanceof java.net.Inet4Address) {
                    CB_srcAddresses.addItem((Object) addr.getHostAddress());
                }
            }
        }
        //CB_srcAddresses.setSelectedItem("127.0.0.1");
    }    
    
    public void btnStart() {
        try {
            InetSocketAddress srcaddr = new InetSocketAddress(CB_srcAddresses.getSelectedItem().toString(), 0);
            InetSocketAddress dstaddr = new InetSocketAddress(TB_dtsAddress.getText(), new Integer(TB_dtsPort.getText()));
            if (pGenerator != null) {
                pGenerator.close();
            }
            pGenerator = new PacketGenerator(srcaddr, dstaddr,
                    (Integer) S_packetSize_KB.getValue(),
                    ((Integer) S_streamSpeed.getValue()) * 1000);
            CB_srcAddresses.setEnabled(false);
            S_packetSize_KB.setEnabled(false);
            TB_dtsAddress.setEnabled(false);
            S_streamSpeed.setEnabled(false);
            TB_dtsPort.setEnabled(false);
        } catch (SocketException ex) {
            Logger.getLogger(PacketGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public void btnStop() {
        if (pGenerator != null) {
            pGenerator.close();
            CB_srcAddresses.setEnabled(true);
            S_packetSize_KB.setEnabled(true);
            TB_dtsAddress.setEnabled(true);
            S_streamSpeed.setEnabled(true);
             TB_dtsPort.setEnabled(true);
        }
    }    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        CB_srcAddresses = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        TB_dtsAddress = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        TB_dtsPort = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        S_packetSize_KB = new javax.swing.JSpinner();
        S_streamSpeed = new javax.swing.JSpinner();
        jtb = new javax.swing.JToggleButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("UDP-flood utility");
        setSize(new java.awt.Dimension(405, 170));
        getContentPane().setLayout(null);

        CB_srcAddresses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CB_srcAddressesActionPerformed(evt);
            }
        });
        getContentPane().add(CB_srcAddresses);
        CB_srcAddresses.setBounds(170, 10, 160, 20);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Source Address: ");
        jLabel1.setRequestFocusEnabled(false);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 10, 120, 20);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Dest. Address & Port: ");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 40, 140, 20);

        TB_dtsAddress.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TB_dtsAddress.setText("127.0.0.1");
        getContentPane().add(TB_dtsAddress);
        TB_dtsAddress.setBounds(170, 40, 100, 20);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Packet Size (Bytes):");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(10, 70, 130, 20);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Stream Speed (Kbps): ");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(10, 100, 140, 20);

        TB_dtsPort.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TB_dtsPort.setText("12345");
        getContentPane().add(TB_dtsPort);
        TB_dtsPort.setBounds(280, 40, 50, 20);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText(":");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(270, 40, 10, 20);

        S_packetSize_KB.setModel(new javax.swing.SpinnerNumberModel(1000, 127, 1500, 1));
        S_packetSize_KB.setValue(1000);
        getContentPane().add(S_packetSize_KB);
        S_packetSize_KB.setBounds(170, 70, 80, 20);

        S_streamSpeed.setValue(500);
        getContentPane().add(S_streamSpeed);
        S_streamSpeed.setBounds(170, 100, 80, 20);

        jtb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/get-16.png"))); // NOI18N
        jtb.setText("Run");
        jtb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jtbItemStateChanged(evt);
            }
        });
        getContentPane().add(jtb);
        jtb.setBounds(150, 140, 90, 26);

        jLabel6.setText("ONLY FOR CHECK NETWORK NODES DEFENCE !");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(20, 190, 310, 15);

        jLabel7.setText("NOT USE FOR ATTACK !");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(20, 220, 310, 15);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CB_srcAddressesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CB_srcAddressesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CB_srcAddressesActionPerformed

    private void jtbItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jtbItemStateChanged
        ImageIcon iconOn = new ImageIcon(getClass().getResource("/img/get-16.png"));
        ImageIcon iconOf = new ImageIcon(getClass().getResource("/img/stop-16.png"));
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            jtb.setText("Stop ");
            jtb.setIcon(iconOf);
            btnStart();
        } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
            jtb.setText("Run ");
            jtb.setIcon(iconOn);
            btnStop();
        }
    }//GEN-LAST:event_jtbItemStateChanged

    public static void goUdpFlood() {
        /*try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Metal".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(JPacketGeneratorGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }*/
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new JPacketGeneratorGUI().setVisible(true);
                } catch (SocketException | UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(JPacketGeneratorGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox CB_srcAddresses;
    private javax.swing.JSpinner S_packetSize_KB;
    private javax.swing.JSpinner S_streamSpeed;
    private javax.swing.JTextField TB_dtsAddress;
    private javax.swing.JTextField TB_dtsPort;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    public static javax.swing.JToggleButton jtb;
    // End of variables declaration//GEN-END:variables

}
