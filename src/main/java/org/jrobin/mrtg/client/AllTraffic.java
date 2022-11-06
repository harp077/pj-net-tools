package org.jrobin.mrtg.client;

import java.awt.Dimension;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

public class AllTraffic extends javax.swing.JFrame {

    public static JFrame frameAllTraffic;
    private RpcClient client;
    private byte[] graphBytes;
    static Dimension GRAPH_SIZE = new Dimension(680, 480);

    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;

    public AllTraffic() {
        
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new JToolBar();
        jToolBar1.setOrientation(JToolBar.VERTICAL);
        jToolBar1.setFloatable(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        MrtgData mrtg = ClientMRTG.getMrtgData();
        for (RouterInfo ri : mrtg.getInfo()) {
            System.out.println(ri.getInfo());
            for (LinkInfo li : ri.getLinkInfo()) {
                this.jToolBar1.add(new JSeparator());
                ImageIcon icon=null;
                try {
                    if (client == null) {
                        client = new RpcClient(mrtg.getMrtgHost());
                    }
                    graphBytes = client.getPngGraph(ri, li, new Date(System.currentTimeMillis() - 24*60*60*1000L), new Date(System.currentTimeMillis() + 1*60*60*1000L));
                    icon = new ImageIcon(graphBytes, "PNG graph");
                    /*if (icon.getIconWidth() != GRAPH_SIZE.getWidth() || icon.getIconHeight() != GRAPH_SIZE.getHeight()) {
                        GRAPH_SIZE = new Dimension(icon.getIconWidth(), icon.getIconHeight());
                        graphLabel.setPreferredSize(GRAPH_SIZE);
                        pack();
                    }
                    graphLabel.setIcon(icon);*/
                } catch (Exception e) {
                    Util.error(this, "Graph could not be generated:\n" + e);
                }
                this.jToolBar1.add(new javax.swing.JLabel(icon));
                System.out.println(li.getInfo());
            }
            //jToolBar1.getComponents()
        }
        /*for (int k=1; k< 11 ; k++) {
            this.jToolBar1.add(new javax.swing.JLabel("label"+k));
        }*/
        jScrollPane1.setViewportView(jPanel2);
        jPanel1.add(jScrollPane1);        
        jPanel2.add(jToolBar1);
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        pack();
    }

    public static void go() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frameAllTraffic = new AllTraffic();
                ImageIcon icone = new ImageIcon(getClass().getResource("/mrtg.png"));
                frameAllTraffic.setIconImage(icone.getImage());
                frameAllTraffic.setLocation(333, 333);
                frameAllTraffic.setPreferredSize(GRAPH_SIZE);//.setPrefferedSize(333, 333);
                frameAllTraffic.setVisible(true);
            }
        });
    }

}
