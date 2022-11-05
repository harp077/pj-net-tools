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

public class EditLinkDialog extends JDialog {
	static final String ADD_TITLE = "New interface";

	static final String EDIT_TITLE = "Edit interface data";

	static final String DEFAULT_SAMPLING_INTERVAL = "300";

	static final int MAX_SAMPLING_INTERVAL = 600;

	static final int MIN_SAMPLING_INTERVAL = 10;

	// result comes here
	private LinkInfo[] linkInfo;

	private String[] ifDescrs;

	private RouterInfo routerInfo;

	boolean insertMode = false;

	// labels
	private JLabel routerLabel = Util.standardLabel("Node: ");

	private JLabel linksListLabel = Util.standardLabel("Interface: ");

	private JLabel descrLabel = Util.standardLabel("Description: ");

	private JLabel samplingLabel = Util.standardLabel("Poll-period (sec):");

	private JLabel activeLabel = Util.standardLabel("Active: ");

	// values
	private JLabel routerValueLabel = new JLabel();

	private JList linksList = new JList();

	private JTextField descrField = Util.standardTextField();

	private JTextField samplingField = Util.standardTextField();

	private JCheckBox activeBox = new JCheckBox("", true);

	private JButton okButton = Util.standardButton("OK");
    
	private JButton cancelButton = Util.standardButton("Cancel");

	EditLinkDialog(Frame parent, RouterInfo routerInfo, String[] ifDescrs) {
		// add link
		super(parent, ADD_TITLE, true);
		this.insertMode = true;
		this.routerInfo = routerInfo;
		this.ifDescrs = ifDescrs;
		constructUserInterface();
		pack();
		setVisible(true);
	}

	EditLinkDialog(Frame parent, RouterInfo routerInfo, LinkInfo linkInfo) {
		// edit link
		super(parent, EDIT_TITLE, true);
		this.insertMode = false;
		this.routerInfo = routerInfo;
		this.linkInfo = new LinkInfo[] { linkInfo };

		constructUserInterface();
		pack();
		setVisible(true);
	}

	private void constructUserInterface() {
		JPanel content = (JPanel) getContentPane();
		/*routerLabel.setFont(NetworkManagerGUI.baseFont);
		linksListLabel.setFont(NetworkManagerGUI.baseFont);
		descrLabel.setFont(NetworkManagerGUI.baseFont);
		samplingLabel.setFont(NetworkManagerGUI.baseFont);
		activeLabel.setFont(NetworkManagerGUI.baseFont);

		// values
		routerValueLabel.setFont(NetworkManagerGUI.baseFont);
		linksList.setFont(NetworkManagerGUI.baseFont);
		descrField.setFont(NetworkManagerGUI.baseFont);
		samplingField.setFont(NetworkManagerGUI.baseFont);
		activeBox.setFont(NetworkManagerGUI.baseFont);
		okButton.setFont(NetworkManagerGUI.baseFont);
		cancelButton.setFont(NetworkManagerGUI.baseFont);

		linksListLabel.setFont(NetworkManagerGUI.baseFont);
		descrLabel.setFont(NetworkManagerGUI.baseFont);
		samplingLabel.setFont(NetworkManagerGUI.baseFont);
		activeLabel.setFont(NetworkManagerGUI.baseFont);

		// values
		routerValueLabel.setFont(NetworkManagerGUI.baseFont);
		linksList.setFont(NetworkManagerGUI.baseFont);
		descrField.setFont(NetworkManagerGUI.baseFont);
		samplingField.setFont(NetworkManagerGUI.baseFont);
		activeBox.setFont(NetworkManagerGUI.baseFont);
		okButton.setFont(NetworkManagerGUI.baseFont);
		cancelButton.setFont(NetworkManagerGUI.baseFont);*/

		Box box = Box.createVerticalBox();
		box.add(Util.getPanelFor(routerLabel, routerValueLabel));
		box.add(Util.getPanelFor(linksListLabel, Util
				.standardScrollPane(linksList)));
		//box.add(Util.getPanelFor(descrLabel, descrField));
		box.add(Util.getPanelFor(samplingLabel, samplingField));
                samplingField.setEditable(false);
                samplingField.setForeground(Color.GRAY);
		box.add(Util.getPanelFor(activeLabel, activeBox));
		box.add(Util
				.getPanelFor(Util.standardLabel(""), okButton, cancelButton));
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
		// populate controls
		routerValueLabel.setText(routerInfo.getHost());
		if (insertMode) {
			linksList.setModel(new DefaultComboBoxModel(ifDescrs));
			linksList.setSelectedIndex(0);
			samplingField.setText(DEFAULT_SAMPLING_INTERVAL);
			activeBox.setSelected(true);
		} else {
			linksList.setModel(new DefaultComboBoxModel(routerInfo
					.getInterfaces()));
			linksList.setSelectedValue(linkInfo[0].getIfDescr(), true);
			linksList.setEnabled(false);
			descrField.setText(linkInfo[0].getDescr());
			samplingField.setText("" + linkInfo[0].getSamplingInterval());
			activeBox.setSelected(linkInfo[0].isActive());
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
		int samplingInterval;
		try {
			samplingInterval = Integer.parseInt(samplingField.getText());
			if (samplingInterval < MIN_SAMPLING_INTERVAL
					|| samplingInterval > MAX_SAMPLING_INTERVAL) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException nfe) {
			Util.error(this, "Sampling interval must be a number between "
					+ MIN_SAMPLING_INTERVAL + " and " + MAX_SAMPLING_INTERVAL);
			return;
		}
		if (insertMode) {
			// new link
			Object[] selectedLinks = linksList.getSelectedValues();
			int count = selectedLinks.length;
			if (count == 0) {
				Util.error(this, "Select at least one interface to add");
				return;
			}
			linkInfo = new LinkInfo[count];
			for (int i = 0; i < count; i++) {
				linkInfo[i] = new LinkInfo();
				linkInfo[i].setIfDescr((String) selectedLinks[i]);
				linkInfo[i].setActive(activeBox.isSelected());
				linkInfo[i].setDescr(descrField.getText());
				linkInfo[i].setSamplingInterval(samplingInterval);
			}
		} else {
			// update link
			linkInfo[0].setActive(activeBox.isSelected());
			linkInfo[0].setDescr(descrField.getText());
			linkInfo[0].setSamplingInterval(samplingInterval);
		}
		close();
	}

	private void cancel() {
		linkInfo = null;
		close();
	}

	LinkInfo[] getLinkInfo() {
		return linkInfo;
	}
}
