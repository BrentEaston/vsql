/*
 * Copyright (c) 2000-2003 by David Sullivan
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License (LGPL) as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, copies are available
 * at http://www.opensource.org.
 */
package	CASL.MapBuilder;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * Title:        VASLMapConverterDialog.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class VASLMapConverterDialog extends JDialog {

	private MapEditFrame	frame;

	private JPanel VASLMapPanel = new JPanel();
	private JPanel panel2 = new JPanel();
	private JButton button1 = new JButton();
	private JButton button2 = new JButton();
	private Border border1;
	private JPanel jPanel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridLayout gridLayout1 = new GridLayout();
	private JLabel boardLabel = new JLabel();
	private JLabel headerLabel = new JLabel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private JTextField boardTextField = new JTextField();

	public VASLMapConverterDialog(Frame frame, boolean modal) {

		super(frame, "Select the VASL board number to convert", modal);

		this.frame = (MapEditFrame) frame;
		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		pack();
	}

	private void jbInit() throws Exception {
		border1 = BorderFactory.createRaisedBevelBorder();
		jPanel1.setLayout(gridLayout1);
		panel2.setBorder(border1);
		panel2.setMaximumSize(new Dimension(400, 200));
		panel2.setMinimumSize(new Dimension(400, 200));
		panel2.setLayout(gridBagLayout2);
		button1.setText("OK");
		button1.addActionListener(new VASLMapConverterDialog_button1_actionAdapter(this));
		button2.setText("Cancel");
		gridLayout1.setHgap(4);
		button2.addActionListener(new VASLMapConverterDialog_button2_actionAdapter(this));
		this.addWindowListener(new VASLMapConverterDialog_this_windowAdapter(this));
		VASLMapPanel.setLayout(gridBagLayout1);
		boardLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		boardLabel.setText("Board number:");
		headerLabel.setText("Please enter the VASL map number:");
		VASLMapPanel.add(jPanel1, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 8, 4, 8), 0, 0));
		jPanel1.add(button1, null);
		jPanel1.add(button2, null);
		VASLMapPanel.add(panel2, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
			,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));
		panel2.add(boardLabel, new GridBagConstraints(0, 1, 1, 2, 0.0, 0.0
			,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 118, 4, 18), 17, 5));
		panel2.add(boardTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 65, 0));
		panel2.add(headerLabel, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
			,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(17, 112, 9, 230), 4, 5));
		getContentPane().add(VASLMapPanel);

		panel2.setPreferredSize(new Dimension(400,200));
		this.validate();
	}

	// OK
	void button1_actionPerformed(ActionEvent e) {
		int 	board = 0;
		boolean error = false;

		// convert input to integer values
		try {
			board = Integer.parseInt(boardTextField.getText());
		}
		catch(Exception exp) {
			error = true;
		}

		if(!error) {

			// convert the map
			dispose();
			frame.convertVASLImage(board);

		}
		else {

			// show error dialog box
		}
	}

	// Cancel
	void button2_actionPerformed(ActionEvent e) {
		dispose();
	}

	void this_windowClosing(WindowEvent e) {
		dispose();
	}
}

class VASLMapConverterDialog_button1_actionAdapter implements ActionListener {
	VASLMapConverterDialog adaptee;

	VASLMapConverterDialog_button1_actionAdapter(VASLMapConverterDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.button1_actionPerformed(e);
	}
}

class VASLMapConverterDialog_button2_actionAdapter implements ActionListener {
	VASLMapConverterDialog adaptee;

	VASLMapConverterDialog_button2_actionAdapter(VASLMapConverterDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.button2_actionPerformed(e);
	}
}

class VASLMapConverterDialog_this_windowAdapter extends WindowAdapter {
	VASLMapConverterDialog adaptee;

	VASLMapConverterDialog_this_windowAdapter(VASLMapConverterDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.this_windowClosing(e);
	}
}
