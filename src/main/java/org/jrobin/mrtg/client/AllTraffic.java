package org.jrobin.mrtg.client;

import java.awt.Dimension;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;

public class AllTraffic extends javax.swing.JFrame {

    private static JFrame frameAllTraffic;

    private RpcClient client;
    private byte[] graphBytes;
    static Dimension GRAPH_SIZE = new Dimension(680, 480);

    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private static String tip;

    public AllTraffic(String tip) {
        
        jScrollPane1 = new javax.swing.JScrollPane();
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
                    switch(tip) {
                        case "Day": 
                            graphBytes = client.getPngGraph(ri, li, new Date(System.currentTimeMillis() -  1*24*60*60*1000L), new Date(System.currentTimeMillis() + 1*60*60*1000L));
                            break;
                        case "Week": 
                            graphBytes = client.getPngGraph(ri, li, new Date(System.currentTimeMillis() -  7*24*60*60*1000L), new Date(System.currentTimeMillis() + 1*60*60*1000L));
                            break;  
                        case "Month": 
                            graphBytes = client.getPngGraph(ri, li, new Date(System.currentTimeMillis() - 30*24*60*60*1000L), new Date(System.currentTimeMillis() + 1*60*60*1000L));
                            break;                            
                    }
                    icon = new ImageIcon(graphBytes, "PNG graph");
                } catch (Exception e) {
                    Util.error(this, "Graph could not be generated:\n" + e);
                }
                this.jToolBar1.add(new javax.swing.JLabel(icon));
                System.out.println(li.getInfo());
            }
        }
        jScrollPane1.setViewportView(jToolBar1);
        jScrollPane1.setVerticalScrollBar(new JScrollBar());
        jScrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(jScrollPane1);
        pack();
    }

    public static void go(String tip) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frameAllTraffic = new AllTraffic(tip);
                frameAllTraffic.setTitle(" All interfaces traffic - " + tip);
                ImageIcon icone = new ImageIcon(getClass().getResource("/mrtg.png"));
                frameAllTraffic.setIconImage(icone.getImage());
                frameAllTraffic.setLocation(333, 111);
                //frameAllTraffic.setPreferredSize(GRAPH_SIZE);
                frameAllTraffic.setSize(660, 800);
                frameAllTraffic.setVisible(true);
            }
        });
    }
    
}
