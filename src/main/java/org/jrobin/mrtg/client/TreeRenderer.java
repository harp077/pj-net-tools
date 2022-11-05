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
package org.jrobin.mrtg.client;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

//import nnm.NetworkManagerGUI;
import nnm.util.ColorIcon;

public class TreeRenderer extends DefaultTreeCellRenderer {
	private static ImageIcon MRTG_ICON;

	private static ImageIcon ROUTER_ICON;

	private static ImageIcon LINK_ICON;

	private static ImageIcon INACTIVE_ROUTER_ICON;

	private static ImageIcon INACTIVE_LINK_ICON;

	static {
		//java.net.URL imageURL = TreeRenderer.class.getResource("icons/mrtg.png");
		//MRTG_ICON = new ImageIcon(getClass().getResource("/icons/mrtg.png")); //new ImageIcon(imageURL);
		//imageURL = TreeRenderer.class.getResource("icons/router.png");
		//ROUTER_ICON = new ImageIcon(getClass().getResource("/icons/router.png")); //new ImageIcon(imageURL);
		//imageURL = TreeRenderer.class.getResource("icons/link.png");
		//LINK_ICON = new ImageIcon(getClass().getResource("/icons/link.png"));//new ImageIcon(imageURL);
		//INACTIVE_ROUTER_ICON = new ImageIcon(ColorIcon.colorize(ROUTER_ICON.getImage(), Color.red));
		//INACTIVE_LINK_ICON = new ImageIcon(ColorIcon.colorize(LINK_ICON.getImage(), Color.red));

	}

	TreeRenderer() {
            MRTG_ICON = new ImageIcon(getClass().getResource("/icons/mrtg.png"));
            ROUTER_ICON = new ImageIcon(getClass().getResource("/icons/router.png"));
            LINK_ICON = new ImageIcon(getClass().getResource("/icons/link.png"));
            INACTIVE_ROUTER_ICON = new ImageIcon(ColorIcon.colorize(ROUTER_ICON.getImage(), Color.red));
            INACTIVE_LINK_ICON = new ImageIcon(ColorIcon.colorize(LINK_ICON.getImage(), Color.red));            
            setLeafIcon(null);
            setClosedIcon(null);
            setOpenIcon(null);

	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		Object nodeObj = node.getUserObject();
		if (nodeObj instanceof ServerInfo) {
			 setFont(getFont().deriveFont(Font.BOLD));
			//setFont(NetworkManagerGUI.baseFont);
			setIcon(MRTG_ICON);
		} else if (nodeObj instanceof RouterInfo) {
			 setFont(getFont().deriveFont(Font.BOLD));
			//setFont(NetworkManagerGUI.baseFont);
			RouterInfo routerInfo = (RouterInfo) nodeObj;
			// setForeground(routerInfo.isActive()? Color.BLACK: Color.RED);
			setIcon(routerInfo.isActive() ? ROUTER_ICON : INACTIVE_ROUTER_ICON);
		} else if (nodeObj instanceof LinkInfo) {
			 setFont(getFont().deriveFont(Font.PLAIN));
			//setFont(NetworkManagerGUI.baseFont);
			LinkInfo linkInfo = (LinkInfo) nodeObj;
			// setForeground(linkInfo.isActive()? Color.BLACK: Color.RED);
			setIcon(linkInfo.isActive() ? LINK_ICON : INACTIVE_LINK_ICON);
		} else {
			 setFont(getFont().deriveFont(Font.PLAIN));
			//setFont(NetworkManagerGUI.baseFont);
			// setForeground(Color.BLACK);
		}
		return this;
	}
}
