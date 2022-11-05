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

//import nnm.NetworkManagerGUI;
import nnm.util.MessageDialog;

public class Util {

	static JPanel getPanelFor(JComponent comp1, JComponent comp2) {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.add(comp1);
		panel.add(comp2);
		return panel;
	}

	static JPanel getPanelFor(JComponent comp1, JComponent comp2,
			JComponent comp3) {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.add(comp1);
		panel.add(comp2);
		panel.add(comp3);
		return panel;
	}

	static void error(Component parent, String message) {
		JDialog tmp = new JDialog();
		new MessageDialog(tmp, message, "Error");

		// JMessOptionPane.showMessageDialog(parent, message, "Error",
		// JOptionPane.ERROR_MESSAGE);
	}

	static void warn(Component parent, String message) {
		JDialog tmp = new JDialog();
		new MessageDialog(tmp, message, "Warning");

		// JOptionPane.showMessageDialog(parent, message, "Warning",
		// JOptionPane.WARNING_MESSAGE);
	}

	static void info(Component parent, String message) {
		JDialog tmp = new JDialog();
		new MessageDialog(tmp, message, "Info");

		// JOptionPane.showMessageDialog(parent, message, "Info",
		// JOptionPane.INFORMATION_MESSAGE);
	}

	static void centerOnScreen(Window window) {
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension screenSize = t.getScreenSize();
		Dimension frameSize = window.getPreferredSize();
		double x = (screenSize.getWidth() - frameSize.getWidth()) / 2;
		double y = (screenSize.getHeight() - frameSize.getHeight()) / 2;
		window.setLocation((int) x, (int) y);
	}

	static final JButton PLACEHOLDER_BUTTON = new JButton("123456789012");

	static final Dimension BUTTON_SIZE = PLACEHOLDER_BUTTON.getPreferredSize();

	static JButton standardButton(String caption) {
		JButton button = new JButton(caption);
		button.setPreferredSize(BUTTON_SIZE);
		button.setMinimumSize(BUTTON_SIZE);
		button.setMaximumSize(BUTTON_SIZE);
		//button.setBackground(NetworkManagerGUI.sysBackColor);
		return button;
	}

	static final JButton BIG_PLACEHOLDER_BUTTON = new JButton(
			"12345678901234567");

	static final Dimension BIG_BUTTON_SIZE = BIG_PLACEHOLDER_BUTTON
			.getPreferredSize();

	static JButton largeButton(String caption) {
		JButton button = new JButton(caption);
		button.setPreferredSize(BIG_BUTTON_SIZE);
		button.setMinimumSize(BIG_BUTTON_SIZE);
		button.setMaximumSize(BIG_BUTTON_SIZE);
		//button.setBackground(NetworkManagerGUI.sysBackColor);
		return button;
	}

	static final JLabel PLACEHOLDER_LABEL = new JLabel("nnnnnnnnnnnnnnn");

	static final Dimension LABEL_SIZE = PLACEHOLDER_LABEL.getPreferredSize();

	static JLabel standardLabel(String text) {
		JLabel label = new JLabel(text);
		label.setPreferredSize(LABEL_SIZE);
		return label;
	}

	static JLabel standardLabel() {
		return standardLabel("");
	}

	static final int INPUT_FIELD_SIZE = 20;

	static JTextField standardTextField() {
		JTextField textField = new JTextField();
		textField.setColumns(INPUT_FIELD_SIZE);
		return textField;
	}

	static final int SCROLL_PANE_HEIGHT = 30;

	static JScrollPane standardScrollPane(JComponent component) {
		JTextField placeholder = Util.standardTextField();
		int width = (int) placeholder.getPreferredSize().getWidth();
		JScrollPane pane = new JScrollPane(component);
		pane.setPreferredSize(new Dimension(width, 150));
		return pane;
	}
}
