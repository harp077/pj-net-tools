/* ============================================================
 * JRobin : Pure java implementation of RRDTool's functionality
 * ============================================================
 *
 * Project Info:  http://www.jrobin.org
 * Project Lead:  Sasa Markovic (saxon@jrobin.org);
 *
 * (C) Copyright 2003, by Sasa Markovic.
 *
 * Developers:    Sasa Markovic (saxon@jrobin.org)
 *                Arne Vandamme (cobralord@jrobin.org)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */
// Copyright (C) 2005 Mila NetWhistler.  All rights reserved.
//
// For more information contact: 
//      Mila NetWhistler        <netwhistler@gmail.com>
//      http://www.netwhistler.spb.ru/
package org.jrobin.mrtg.client;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jpacketgenerator.JPacketGeneratorGUI;
import my.harp07.PjFrame;
//import nnm.NetworkManagerGUI;

public class ClientMRTG extends JFrame {
    
        public static ClientMRTG frameClientMRTG;
    
	static final String RESOURCE_PATH = "mrtg/icons/";
        
        static final String helpStr = "Started SNMP-polling after running pj-net-tools and ending SNMP-polling after killing pj-net-tools. \nKeeps SNMP-polling even after closing MRTG-window. \nUse 32-bit counters for speed <= 100 Mbit/s.\nPolling every 300 sec.";

	static final String TITLE = "MRTG  ( keeps polling even after closing this window )";

	static final String SUBTITLE = "http://netwhistler.spb.ru";

	static final String COPYRIGHT = "Copyright \u00A9 2005 NetWhistler";

	static Dimension MAIN_TREE_SIZE = new Dimension(400, 400);

	static Dimension INFO_PANE_SIZE = new Dimension(400, 400);

	static String DEFAULT_HOST = "localhost";

	static final int HGAP = 1, VGAP = 1, DIVIDER_SIZE = 3;

	// controls
	JTree mainTree = new JTree();

	JTextPane infoTextPane = new JTextPane();

	// menubar
	JMenuBar menuBar = new JMenuBar();
    
	// MRTG menu
	JMenu mrtgMenu = new JMenu("MRTG");

	JMenuItem reloadMenuItem = new JMenuItem("Reload Data", KeyEvent.VK_L);

	JMenuItem exitMenuItem = new JMenuItem("Close", KeyEvent.VK_X);

	// Routers menu
	JMenu routersMenu = new JMenu("Node Actions");

	JMenuItem newRouterItem = new JMenuItem("Add SNMP Node...", KeyEvent.VK_A);

	JMenuItem editRouterItem = new JMenuItem("Edit Node Data...",
			KeyEvent.VK_E);

	JMenuItem deleteRouterItem = new JMenuItem("Remove Node", KeyEvent.VK_V);

	// Interfaces menu
	JMenu linksMenu = new JMenu("Interface Actions");

	JMenuItem newLinkItem = new JMenuItem("Add Interface(s)...", KeyEvent.VK_N);

	JMenuItem editLinkItem = new JMenuItem("Edit Interface data...",
			KeyEvent.VK_E);

	JMenuItem deleteLinkItem = new JMenuItem("Remove Interface", KeyEvent.VK_V);

	JMenuItem quickGraphItem = new JMenuItem("Quick Graph (last 24hr)...",
			KeyEvent.VK_Q);

	JMenuItem dailyGraphItem = new JMenuItem("Daily Graph...", KeyEvent.VK_D);

	JMenuItem weeklyGraphItem = new JMenuItem("Weekly Graph...", KeyEvent.VK_W);

	JMenuItem monthlyGraphItem = new JMenuItem("Monthly Graph...",
			KeyEvent.VK_O);

	//JMenuItem yearlyGraphItem = new JMenuItem("Yearly Graph...", KeyEvent.VK_Y);

	JMenuItem customGraphItem = new JMenuItem("Custom Graph...", KeyEvent.VK_C);

	// Help menu
	JMenu helpMenu = new JMenu("Info");

	JMenuItem helpItem = new JMenuItem("Info...", KeyEvent.VK_H);

	// MRTG popup menu
	JPopupMenu mrtgPopupMenu = new JPopupMenu();

	JMenuItem mrtgPopupReloadMenuItem = new JMenuItem("Reload Data");

	JMenuItem mrtgPopupAddRouterMenuItem = new JMenuItem("Add SNMP Node...");

	JMenuItem mrtgPopupExitMenuItem = new JMenuItem("Exit");

	// ROUTER popup menu
	JPopupMenu routerPopupMenu = new JPopupMenu();

	JMenuItem routerPopupEditRouterMenuItem = new JMenuItem(
			"Edit Node data...");

	JMenuItem routerPopupAddLinksMenuItem = new JMenuItem("Add Interface(s)...");

	JMenuItem routerPopupRemoveRouterMenuItem = new JMenuItem("Remove Node");

	// INTERFACES popup menu
	JPopupMenu linksPopupMenu = new JPopupMenu();

	JMenuItem linksPopupEditLinkMenuItem = new JMenuItem(
			"Edit Interface Data...");

	JMenuItem linksPopupRemoveLinkMenuItem = new JMenuItem("Remove Interface");

	JMenuItem linksPopupQuickGraphMenuItem = new JMenuItem(
			"Quick Graph (last 24hr)...");

	JMenuItem linksPopupDailyGraphMenuItem = new JMenuItem("Daily Graph...");

	JMenuItem linksPopupWeeklyGraphMenuItem = new JMenuItem("Weekly Graph...");

	JMenuItem linksPopupMonthlyGraphMenuItem = new JMenuItem("Monthly Graph...");

	//JMenuItem linksPopupYearlyGraphMenuItem = new JMenuItem("Yearly Graph...");

	JMenuItem linksPopupCustomGraphMenuItem = new JMenuItem("Custom Graph...");

	private MrtgData mrtgData = MrtgData.getInstance();

	public ClientMRTG() throws IOException {
		super(TITLE);
		/*menuBar.setBackground(NetworkManagerGUI.sysBackColor);
		mrtgMenu.setFont(NetworkManagerGUI.baseFont);
		reloadMenuItem.setFont(NetworkManagerGUI.baseFont);
		exitMenuItem.setFont(NetworkManagerGUI.baseFont);

		// Routers menu
		routersMenu.setFont(NetworkManagerGUI.baseFont);
		newRouterItem.setFont(NetworkManagerGUI.baseFont);
		editRouterItem.setFont(NetworkManagerGUI.baseFont);
		deleteRouterItem.setFont(NetworkManagerGUI.baseFont);
		// Interfaces menu
		linksMenu.setFont(NetworkManagerGUI.baseFont);
		newLinkItem.setFont(NetworkManagerGUI.baseFont);
		editLinkItem.setFont(NetworkManagerGUI.baseFont);
		deleteLinkItem.setFont(NetworkManagerGUI.baseFont);
		quickGraphItem.setFont(NetworkManagerGUI.baseFont);
		dailyGraphItem.setFont(NetworkManagerGUI.baseFont);
		weeklyGraphItem.setFont(NetworkManagerGUI.baseFont);
		monthlyGraphItem.setFont(NetworkManagerGUI.baseFont);
		//yearlyGraphItem.setFont(NetworkManagerGUI.baseFont);
		customGraphItem.setFont(NetworkManagerGUI.baseFont);
		// Help menu
		helpMenu.setFont(NetworkManagerGUI.baseFont);
		helpItem.setFont(NetworkManagerGUI.baseFont);
		// MRTG popup menu
		mrtgPopupMenu.setFont(NetworkManagerGUI.baseFont);
		mrtgPopupReloadMenuItem.setFont(NetworkManagerGUI.baseFont);
		mrtgPopupAddRouterMenuItem.setFont(NetworkManagerGUI.baseFont);
		mrtgPopupExitMenuItem.setFont(NetworkManagerGUI.baseFont);
		// ROUTER popup menu
		routerPopupMenu.setFont(NetworkManagerGUI.baseFont);
		routerPopupEditRouterMenuItem.setFont(NetworkManagerGUI.baseFont);
		routerPopupAddLinksMenuItem.setFont(NetworkManagerGUI.baseFont);
		routerPopupRemoveRouterMenuItem.setFont(NetworkManagerGUI.baseFont);
		// INTERFACES popup menu

		linksPopupEditLinkMenuItem.setFont(NetworkManagerGUI.baseFont);
		linksPopupRemoveLinkMenuItem.setFont(NetworkManagerGUI.baseFont);
		linksPopupQuickGraphMenuItem.setFont(NetworkManagerGUI.baseFont);
		linksPopupDailyGraphMenuItem.setFont(NetworkManagerGUI.baseFont);
		linksPopupWeeklyGraphMenuItem.setFont(NetworkManagerGUI.baseFont);
		linksPopupMonthlyGraphMenuItem.setFont(NetworkManagerGUI.baseFont);
		//linksPopupYearlyGraphMenuItem.setFont(NetworkManagerGUI.baseFont);
		linksPopupCustomGraphMenuItem.setFont(NetworkManagerGUI.baseFont);

		NetworkManagerGUI.recursivelySetFonts(this, NetworkManagerGUI.baseFont);*/
		constructUI();
		pack();
	}

	private void constructUI() {
		JPanel content = (JPanel) getContentPane();
		content.setLayout(new BorderLayout(HGAP, VGAP));

		// tree pane
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		JScrollPane treePane = new JScrollPane(mainTree);
		//treePane.setFont(NetworkManagerGUI.baseFont);
		leftPanel.add(treePane);
		leftPanel.setPreferredSize(MAIN_TREE_SIZE);
		mainTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		mainTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				nodeChangedAction();
			}
		});
		DefaultTreeCellRenderer renderer = new TreeRenderer();
		mainTree.setCellRenderer(renderer);

		// info pane
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		JScrollPane infoPane = new JScrollPane(infoTextPane);
		//infoPane.setFont(NetworkManagerGUI.baseFont);
		rightPanel.add(infoPane);
		infoPane.setPreferredSize(INFO_PANE_SIZE);
		infoTextPane.setEditable(false);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				true, leftPanel, rightPanel);
		splitPane.setDividerSize(DIVIDER_SIZE);

		// add split pane to content
		content.add(splitPane, BorderLayout.CENTER);

		// mrtg menu

		reloadMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadDataFromHost();
			}
		});
		mrtgMenu.add(reloadMenuItem);
		mrtgMenu.addSeparator();
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		mrtgMenu.add(exitMenuItem);
		mrtgMenu.setMnemonic(KeyEvent.VK_M);
		menuBar.add(mrtgMenu);

		// routers menu
		newRouterItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addRouter();
			}
		});
		routersMenu.add(newRouterItem);
		editRouterItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editRouter();
			}
		});
		routersMenu.add(editRouterItem);
		routersMenu.addSeparator();
		deleteRouterItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeRouter();
			}
		});
		routersMenu.add(deleteRouterItem);
		routersMenu.setMnemonic(KeyEvent.VK_R);
		menuBar.add(routersMenu);

		// interfaces menu
		newLinkItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addLink();
			}
		});
		linksMenu.add(newLinkItem);
		editLinkItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editLink();
			}
		});
		linksMenu.add(editLinkItem);
		linksMenu.addSeparator();
		deleteLinkItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeLink();
			}
		});
		linksMenu.add(deleteLinkItem);
		linksMenu.addSeparator();
		quickGraphItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph(GraphFrame.TYPE_QUICK);
			}
		});
		linksMenu.add(quickGraphItem);
		dailyGraphItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph(GraphFrame.TYPE_DAILY);
			}
		});
		linksMenu.add(dailyGraphItem);
		weeklyGraphItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph(GraphFrame.TYPE_WEEKLY);
			}
		});
		linksMenu.add(weeklyGraphItem);
		monthlyGraphItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph(GraphFrame.TYPE_MONTHLY);
			}
		});
		linksMenu.add(monthlyGraphItem);
		/*yearlyGraphItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph(GraphFrame.TYPE_YEARLY);
			}
		});
		linksMenu.add(yearlyGraphItem);*/
		linksMenu.addSeparator();
		customGraphItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph(GraphFrame.TYPE_CUSTOM);
			}
		});
		linksMenu.add(customGraphItem);
		linksMenu.setMnemonic(KeyEvent.VK_I);
		menuBar.add(linksMenu);

		// help menu
		helpMenu.setMnemonic(KeyEvent.VK_H);
		helpItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//new HelpDialog(ClientMRTG.this);
                            JOptionPane.showMessageDialog(frameClientMRTG, helpStr, " Info ", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		helpMenu.add(helpItem);

		menuBar.add(helpMenu);

		// MRTG popup menu
		mrtgPopupReloadMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadDataFromHost();
			}
		});
		mrtgPopupMenu.add(mrtgPopupReloadMenuItem);
		mrtgPopupAddRouterMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addRouter();
			}
		});
		mrtgPopupMenu.add(mrtgPopupAddRouterMenuItem);
		mrtgPopupMenu.addSeparator();
		mrtgPopupExitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		mrtgPopupMenu.add(mrtgPopupExitMenuItem);

		// ROUTER popup menu
		routerPopupEditRouterMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editRouter();
			}
		});
		routerPopupMenu.add(routerPopupEditRouterMenuItem);
		routerPopupAddLinksMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addLink();
			}
		});
		routerPopupMenu.add(routerPopupAddLinksMenuItem);
		routerPopupMenu.addSeparator();
		routerPopupRemoveRouterMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeRouter();
			}
		});
		routerPopupMenu.add(routerPopupRemoveRouterMenuItem);

		// LINKS popup menu
		linksPopupEditLinkMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editLink();
			}
		});
		linksPopupMenu.add(linksPopupEditLinkMenuItem);
		linksPopupMenu.addSeparator();
		linksPopupQuickGraphMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph(GraphFrame.TYPE_QUICK);
			}
		});
		linksPopupMenu.add(linksPopupQuickGraphMenuItem);
		linksPopupDailyGraphMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph(GraphFrame.TYPE_DAILY);
			}
		});
		linksPopupMenu.add(linksPopupDailyGraphMenuItem);
		linksPopupWeeklyGraphMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph(GraphFrame.TYPE_WEEKLY);
			}
		});
		linksPopupMenu.add(linksPopupWeeklyGraphMenuItem);
		linksPopupMonthlyGraphMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph(GraphFrame.TYPE_MONTHLY);
			}
		});
		linksPopupMenu.add(linksPopupMonthlyGraphMenuItem);
		/*linksPopupYearlyGraphMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph(GraphFrame.TYPE_YEARLY);
			}
		});
		linksPopupMenu.add(linksPopupYearlyGraphMenuItem);*/
		linksPopupMenu.addSeparator();
		linksPopupCustomGraphMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph(GraphFrame.TYPE_CUSTOM);
			}
		});
		linksPopupMenu.add(linksPopupCustomGraphMenuItem);
		linksPopupMenu.addSeparator();
		linksPopupRemoveLinkMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeLink();
			}
		});
		linksPopupMenu.add(linksPopupRemoveLinkMenuItem);

		MouseAdapter adapter = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				showPopup(e);
			}

			public void mouseReleased(MouseEvent e) {
				showPopup(e);
			}

			private void showPopup(MouseEvent e) {
				if (e.isPopupTrigger()) {
					int selRow = mainTree.getRowForLocation(e.getX(), e.getY());
					TreePath treePath = mainTree.getPathForLocation(e.getX(), e
							.getY());
					if (selRow != -1) {
						mainTree.setSelectionPath(treePath);
						DefaultMutableTreeNode inf = (DefaultMutableTreeNode) treePath
								.getLastPathComponent();
						Object obj = inf.getUserObject();
						if (obj instanceof ServerInfo) {
							mrtgPopupMenu.show(e.getComponent(), e.getX(), e
									.getY());
						} else if (obj instanceof RouterInfo) {
							routerPopupMenu.show(e.getComponent(), e.getX(), e
									.getY());
						} else if (obj instanceof LinkInfo) {
							linksPopupMenu.show(e.getComponent(), e.getX(), e
									.getY());
						}
					}
				}
			}
		};
		mainTree.addMouseListener(adapter);
		/*java.net.URL imageURL = NetworkManagerGUI.class
				.getResource("icons/nw.gif");
		ImageIcon frameIcon = new ImageIcon(imageURL);
		Image image = frameIcon.getImage();*/

		// finalize UI

		//setIconImage(image);

		setJMenuBar(menuBar);
		clearUI();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}

			public void windowOpened(WindowEvent e) {
				selectNewHost();
			}
		});
		Util.centerOnScreen(this);
		mainTree.requestFocus();
	}

	private void clearUI() {
		mainTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode(
				"Not connected")));
		infoTextPane.setText("No info available");
	}

	private void selectNewHost() {

		try {
			mrtgData.setHost(DEFAULT_HOST);
		} catch (IOException ex) {
		}
		reloadData();

	}

	private void nodeChangedAction() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) mainTree
				.getLastSelectedPathComponent();
		if (node != null) {
			Object nodeObj = node.getUserObject();
			if (nodeObj instanceof TreeElementInfo) {
				TreeElementInfo treeElement = (TreeElementInfo) nodeObj;
				infoTextPane.setText(treeElement.getInfo());
			}
		}
	}

	private void reloadDataFromHost() {
		try {
			mrtgData.reload();
			reloadData();
		} catch (Exception e) {
			clearUI();
			handleException("Could not reload data from host", e);
		}
	}

	private void reloadData() {
		try {
			TreeModel treeModel = mrtgData.getTreeModel();
			mainTree.setModel(treeModel);
			mainTree.setSelectionRow(0);
			for (int i = mainTree.getRowCount() - 1; i > 0; i--) {
				mainTree.expandRow(i);
			}
		} catch (Exception e) {
			clearUI();
			handleException("Could not reload data from host", e);
		}
	}

	private boolean isConnected() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) mainTree
				.getModel().getRoot();
		return node.getUserObject() instanceof ServerInfo;
	}

	private void addRouter() {
		if (!isConnected()) {
			Util.error(this, "Connect to MRTG host first");
			return;
		}
		EditRouterDialog newRouterDialog = new EditRouterDialog(this);
		RouterInfo routerInfo = newRouterDialog.getRouterInfo();
		if (routerInfo != null) {
			try {
				if (mrtgData.addRouter(routerInfo) == 0) {
					Util.info(this, "Node " + routerInfo.getHost()
							+ " added succesfully");
					reloadData();
				} else {
					Util.error(this, "Node " + routerInfo.getHost()
							+ " not added");
				}
			} catch (Exception e) {
				handleException("Could not add node", e);
			}
		}
	}

	private void editRouter() {
		RouterInfo routerInfo = findSelectedRouter();
		if (routerInfo == null) {
			Util.warn(this, "Please, select node first");
			return;
		}
		EditRouterDialog newRouterDialog = new EditRouterDialog(this,
				routerInfo);
		routerInfo = newRouterDialog.getRouterInfo();
		if (routerInfo != null) {
			try {
				if (mrtgData.updateRouter(routerInfo) == 0) {
					Util.info(this, "Node " + routerInfo.getHost()
							+ " updated succesfully");
					reloadData();
				} else {
					Util.error(this, "Node " + routerInfo.getHost()
							+ " not updated");
				}
			} catch (Exception e) {
				handleException("Could not edit node data", e);
			}
		}
	}

	private void removeRouter() {
		RouterInfo routerInfo = findSelectedRouter();
		if (routerInfo == null) {
			Util.warn(this, "Please, select node first");
			return;
		}
		int linkCount = routerInfo.getLinkInfo().length;
		if (linkCount > 0) {
			Util.error(this, "To remove node " + routerInfo.getHost()
					+ " please delete associated interfaces first ("
					+ linkCount + " found)");
			return;
		}
		try {
			if (mrtgData.deleteRouter(routerInfo) == 0) {
				Util.info(this, "Node " + routerInfo.getHost()
						+ " deleted succesfully");
				reloadData();
			} else {
				Util.error(this, "Node " + routerInfo.getHost()
						+ " not deleted");
			}
		} catch (Exception e) {
			handleException("Could not delete node", e);
		}
	}

	private void addLink() {
		RouterInfo routerInfo = findSelectedRouter();
		if (routerInfo == null) {
			Util.warn(this, "Please, select node first");
			return;
		}
		try {
			String[] links = mrtgData.getAvailableLinks(routerInfo);
			if (links == null || links.length == 0) {
				// no links available
				Util.error(this, "No interfaces are available on this node");
				return;
			}
			EditLinkDialog newLinkDialog = new EditLinkDialog(this, routerInfo,
					links);
			LinkInfo[] linkInfo = newLinkDialog.getLinkInfo();
			if (linkInfo != null) {
				int ok = 0, bad = 0;
				String failedInterfaces = "";
				for (int i = 0; i < linkInfo.length; i++) {
					if (mrtgData.addLink(routerInfo, linkInfo[i]) == 0) {
						ok++;
					} else {
						bad++;
						failedInterfaces += linkInfo[i].getIfDescr() + "@"
								+ routerInfo.getHost() + "\n";
					}
				}
				String message = ok + " interface(s) added successfully\n";
				if (bad != 0) {
					message += bad + " interface(s) not added:\n"
							+ failedInterfaces;
					Util.error(this, message);
				} else {
					Util.info(this, message);
				}
				reloadData();
			}
		} catch (Exception e) {
			handleException("Could not add new link", e);
		}
	}

	private void editLink() {
		RouterInfo routerInfo = findSelectedRouter();
		LinkInfo linkInfo = findSelectedLink();
		if (routerInfo == null || linkInfo == null) {
			Util.warn(this, "Please, select interface first");
			return;
		}
		try {
			EditLinkDialog newLinkDialog = new EditLinkDialog(this, routerInfo,
					linkInfo);
			LinkInfo[] linkInfoUpdated = newLinkDialog.getLinkInfo();
			if (linkInfoUpdated != null) {
				if (mrtgData.updateLink(routerInfo, linkInfoUpdated[0]) == 0) {
					Util.info(this, "Interface "
							+ linkInfoUpdated[0].getIfDescr() + "@"
							+ routerInfo.getHost() + " updated successfully");
					reloadData();
				} else {
					Util.error(this, "Interface "
							+ linkInfoUpdated[0].getIfDescr() + "@"
							+ routerInfo.getHost() + " NOT updated");
				}
			}
		} catch (Exception e) {
			handleException("Could not edit link data", e);
		}
	}

	private void removeLink() {
		RouterInfo routerInfo = findSelectedRouter();
		LinkInfo linkInfo = findSelectedLink();
		if (routerInfo == null || linkInfo == null) {
			Util.warn(this, "Please, select interface first");
			return;
		}
		try {
			if (mrtgData.removeLink(routerInfo, linkInfo) == 0) {
				Util.info(this, "Interface " + linkInfo.getIfDescr() + "@"
						+ routerInfo.getHost() + " removed successfully");
				reloadData();
			} else {
				Util.error(this, "Interface " + linkInfo.getIfDescr() + "@"
						+ routerInfo.getHost() + " NOT removed");
			}

		} catch (Exception e) {
			handleException("Could not remove link", e);
		}
	}

	private void graph(int type) {
		RouterInfo routerInfo = findSelectedRouter();
		LinkInfo linkInfo = findSelectedLink();
		if (routerInfo == null || linkInfo == null) {
			Util.warn(this, "Please, select interface first");
			return;
		}
		new GraphFrame(this, routerInfo, linkInfo, type);
	}

	private RouterInfo findSelectedRouter() {
		TreePath path = mainTree.getSelectionPath();
		if (path == null || path.getPathCount() < 2) {
			return null;
		} else {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getPathComponent(1);
			return (RouterInfo) node.getUserObject();
		}
	}

	private LinkInfo findSelectedLink() {
		TreePath path = mainTree.getSelectionPath();
		if (path == null || path.getPathCount() < 3) {
			return null;
		} else {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			return (LinkInfo) node.getUserObject();
		}
	}

	private void handleException(String msg, Exception e) {
		Util.error(this, msg + ":\n" + e);
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		ClientMRTG client = new ClientMRTG();
		client.setVisible(true);

	}
        
    public static void runMRTG() {
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
                    frameClientMRTG = new ClientMRTG();
                    ImageIcon icone = new ImageIcon(getClass().getResource("/mrtg.png"));
                    frameClientMRTG.setIconImage(icone.getImage());
                } catch (IOException ex) {
                    Logger.getLogger(ClientMRTG.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }        

}
