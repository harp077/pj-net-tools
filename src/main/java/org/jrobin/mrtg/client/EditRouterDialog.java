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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
//import nnm.NetworkManagerGUI;

public class EditRouterDialog extends JDialog {
	static final String ADD_TITLE = "New SNMP Node";

	static final String EDIT_TITLE = "Edit Node data";

	private RouterInfo routerInfo;

	private JLabel hostLabel = Util.standardLabel("Address: ");

	private JLabel communityLabel = Util.standardLabel("Community: ");

	private JLabel descrLabel = Util.standardLabel("Description: ");

	private JLabel activeLabel = Util.standardLabel("Active: ");

	private JTextField hostField = Util.standardTextField();

	private JTextField communityField = Util.standardTextField();

	private JTextField descrField = Util.standardTextField();

	private JCheckBox activeBox = new JCheckBox("", true);

	private JButton okButton = Util.standardButton("OK");

	private JButton cancelButton = Util.standardButton("Cancel");

	EditRouterDialog(Frame parent) {
		this(parent, null);
	}

	EditRouterDialog(Frame parent, RouterInfo routerInfo) {
		super(parent, routerInfo == null ? ADD_TITLE : EDIT_TITLE, true);
		this.routerInfo = routerInfo;
		constructUserInterface();
		pack();
		setVisible(true);
	}

	private void constructUserInterface() {
		JPanel content = (JPanel) getContentPane();
		/*hostLabel.setFont(NetworkManagerGUI.baseFont);
		communityLabel.setFont(NetworkManagerGUI.baseFont);
		descrLabel.setFont(NetworkManagerGUI.baseFont);
		activeLabel.setFont(NetworkManagerGUI.baseFont);
		hostField.setFont(NetworkManagerGUI.baseFont);
		communityField.setFont(NetworkManagerGUI.baseFont);
		descrField.setFont(NetworkManagerGUI.baseFont);
		activeBox.setFont(NetworkManagerGUI.baseFont);
		okButton.setFont(NetworkManagerGUI.baseFont);
		cancelButton.setFont(NetworkManagerGUI.baseFont);*/

		Box box = Box.createVerticalBox();
		box.add(Util.getPanelFor(hostLabel, hostField));
		box.add(Util.getPanelFor(communityLabel, communityField));
		box.add(Util.getPanelFor(descrLabel, descrField));
		box.add(Util.getPanelFor(activeLabel, activeBox));
		box.add(Util.getPanelFor(Util.standardLabel(), okButton, cancelButton));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ok();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		content.add(box);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		// populate controls if possible
		if (routerInfo != null) {
			hostField.setText(routerInfo.getHost());
			hostField.setEnabled(false);
			communityField.setText(routerInfo.getCommunity());
			descrField.setText(routerInfo.getDescr());
			activeBox.setSelected(routerInfo.isActive());
		}

		okButton.setMnemonic(KeyEvent.VK_O);
		cancelButton.setMnemonic(KeyEvent.VK_C);
		getRootPane().setDefaultButton(okButton);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		Util.centerOnScreen(this);
	}

	private void close() {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	private void ok() {
		if (hostField.getText().length() == 0) {
			Util.warn(this, "Please enter node address");
		} else if (communityField.getText().length() == 0) {
			Util.warn(this, "Please eneter node community");
		} else {
			routerInfo = new RouterInfo();
			routerInfo.setHost(hostField.getText());
			routerInfo.setCommunity(communityField.getText());
			routerInfo.setDescr(descrField.getText());
			routerInfo.setActive(activeBox.isSelected());
			close();
		}
	}

	private void cancel() {
		routerInfo = null;
		close();
	}

	RouterInfo getRouterInfo() {
		return routerInfo;
	}
}
