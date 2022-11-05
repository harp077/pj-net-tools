// Copyright (C) 2005 Mila NetWhistler.  All rights reserved.
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.                                                            
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//       
// For more information contact: 
//      Mila NetWhistler        <netwhistler@gmail.com>
//      http://www.netwhistler.spb.ru/
package nnm.util;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import nnm.NetworkManagerGUI;

public class MessageDialog extends JDialog implements ActionListener {

	public MessageDialog(JDialog parent, String message, String title) {

		super(parent, title, true);
		JPanel messagePane = new JPanel();
		JLabel mesLab = new JLabel(message);
		//mesLab.setFont(NetworkManagerGUI.baseFont);
		messagePane.add(mesLab);
		getContentPane().add(messagePane);
		//setBackground(NetworkManagerGUI.sysBackColor);
		JPanel buttonPane = new JPanel();
		JButton y = new JButton("OK");
		//y.setBackground(NetworkManagerGUI.sysBackColor);
		//y.setFont(NetworkManagerGUI.baseFont);
		buttonPane.add(y);
		y.addActionListener(this);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		pack();
		setResizable(false);
		setLocationRelativeTo(JOptionPane.getFrameForComponent(parent));
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		setVisible(false);
	}

}
