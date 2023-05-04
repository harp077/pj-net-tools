package org.jrobin.mrtg.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.Arrays;
import java.util.Date;
import javax.swing.BorderFactory;
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
        jToolBar1.setRollover(false);
        jToolBar1.setMargin(new Insets(11, 11, 11, 11));
        //jToolBar1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,1,true), tip));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        //
        MrtgData mrtg = ClientMRTG.getMrtgData();
        // LINEAR CALCULATE
        for (RouterInfo ri : mrtg.getInfo()) {
            System.out.println(ri.getInfo());
            for (LinkInfo li : ri.getLinkInfo()) {
                this.jToolBar1.addSeparator();//.add(separator);
                this.jToolBar1.add(new JSeparator());
                ImageIcon icon = null;
                try {
                    if (client == null) {
                        client = new RpcClient(mrtg.getMrtgHost());
                    }
                    switch (tip) {
                        case "Day":
                            graphBytes = client.getPngGraph(ri, li, new Date(System.currentTimeMillis() - 1 * 24 * 60 * 60 * 1000L), new Date(System.currentTimeMillis() + 1 * 60 * 60 * 1000L));
                            break;
                        case "Week":
                            graphBytes = client.getPngGraph(ri, li, new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L), new Date(System.currentTimeMillis() + 1 * 60 * 60 * 1000L));
                            break;
                        case "Month":
                            graphBytes = client.getPngGraph(ri, li, new Date(System.currentTimeMillis() - 31 * 24 * 60 * 60 * 1000L), new Date(System.currentTimeMillis() + 1*24 * 60 * 60 * 1000L));
                            break;
                    }
                    icon = new ImageIcon(graphBytes, "PNG graph");
                } catch (Exception e) {
                    Util.error(this, "Graph could not be generated:\n" + e);
                }
                this.jToolBar1.add(new javax.swing.JLabel(icon));
                this.jToolBar1.add(new JSeparator());
                this.jToolBar1.addSeparator();//.add(separator);
            }
        }
        //
        // PARRALLEL CALCULATE
        /*Arrays.asList(mrtg.getInfo())
            .parallelStream()
            //.stream()
            //.flatMap(ri -> Arrays.asList(ri.getLinkInfo()).stream())
            .forEach(ri -> { Arrays.asList(ri.getLinkInfo()).parallelStream()
                .forEach(li -> {
                    this.jToolBar1.addSeparator();//.add(separator);
                    this.jToolBar1.add(new JSeparator());
                    ImageIcon icon = null;
                    try {
                        if (client == null) {
                            client = new RpcClient(mrtg.getMrtgHost());
                        }
                        switch (tip) {
                            case "Day":
                                graphBytes = client.getPngGraph(ri, li, new Date(System.currentTimeMillis() - 1 * 24 * 60 * 60 * 1000L), new Date(System.currentTimeMillis() + 1 * 60 * 60 * 1000L));
                                break;
                            case "Week":
                                graphBytes = client.getPngGraph(ri, li, new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L), new Date(System.currentTimeMillis() + 1 * 60 * 60 * 1000L));
                                break;
                            case "Month":
                                graphBytes = client.getPngGraph(ri, li, new Date(System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000L), new Date(System.currentTimeMillis() + 1 * 60 * 60 * 1000L));
                                break;
                        }
                        icon = new ImageIcon(graphBytes, "PNG graph");
                    } catch (Exception e) {
                        Util.error(this, "Graph could not be generated:\n" + e);
                    }
                    this.jToolBar1.add(new javax.swing.JLabel(icon));
                    this.jToolBar1.add(new JSeparator());
                    this.jToolBar1.addSeparator();//.add(separator);
                });                   
            });*/
        //
        jScrollPane1.setViewportView(jToolBar1);
        //jScrollPane1.setAlignmentY(11.0f);
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
                frameAllTraffic.setLocation(333, 222);
                //frameAllTraffic.setPreferredSize(GRAPH_SIZE);
                frameAllTraffic.setSize(680, 640);
                frameAllTraffic.setVisible(true);
            }
        });
    }

}
