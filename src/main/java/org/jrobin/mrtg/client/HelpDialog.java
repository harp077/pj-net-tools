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
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//import nnm.NetworkManagerGUI;

public class HelpDialog extends JDialog {
	static final String TITLE = "Help";

	static final String HTML = "docs/mrtg_help.html";

	static final Dimension SIZE = new Dimension(600, 300);

	HelpDialog(Frame parent) {
		super(parent, TITLE);
		constructUI();
		pack();
		Util.centerOnScreen(this);
		setVisible(true);
	}

	private void constructUI() {
		Box box = Box.createVerticalBox();
		JTextPane textPane = new JTextPane();
		textPane.setContentType("text/html");
		textPane.setEditable(false);

		try {
			textPane.read(new FileReader(HTML), null);
		} catch (FileNotFoundException e1) {
			// e1.printStackTrace();
		} catch (IOException e1) {
			// e1.printStackTrace();
		}

		textPane.setCaretPosition(0);

		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setPreferredSize(SIZE);
		scrollPane.setAlignmentX(0.5F);
		box.add(scrollPane);
		box.add(Box.createVerticalStrut(2));
		JButton okButton = Util.standardButton("Close");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ok();
			}
		});
		okButton.setAlignmentX(0.5F);
		//okButton.setFont(NetworkManagerGUI.baseFont);
		box.add(okButton);
		box.add(Box.createVerticalStrut(2));
		getContentPane().add(box);
		getRootPane().setDefaultButton(okButton);
	}

	private void ok() {
		close();
	}

	private void close() {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
}
