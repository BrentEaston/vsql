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
package  CASL.MapBuilder;
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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * Title:        CustomBuildingDialog.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class CustomBuildingDialog extends JDialog {
  private JPanel panel1 = new JPanel();
  private JPanel panel2 = new JPanel();
  private JButton button1 = new JButton();
  private JButton button2 = new JButton();
  private Border border1;
  private JPanel jPanel1 = new JPanel();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private GridLayout gridLayout1 = new GridLayout();
	private JLabel jLabel1 = new JLabel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private JLabel jLabel2 = new JLabel();
	private JLabel jLabel3 = new JLabel();
	private JTextField widthTextField = new JTextField();
	private JTextField heightTextField = new JTextField();
	private JLabel jLabel5 = new JLabel();
	private JComboBox terrainComboBox = new JComboBox();

	private MapEditFrame frame;

	// size variables
	String terrainName;
	int	width;
	int	height;

  public CustomBuildingDialog(Frame frame, String title, boolean modal) {
	super(frame, title, modal);

	this.frame = (MapEditFrame) frame;

	try {
	  jbInit();
	}
	catch (Exception e) {
	  e.printStackTrace();
	}

	pack();
  }

  public CustomBuildingDialog(Frame frame, String title, boolean modal, String terr, int w, int h) {
	super(frame, title, modal);

	this.frame = (MapEditFrame) frame;
	terrainName = terr;
	width 	= w;
	height 	= h;

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
	panel2.setMaximumSize(new Dimension(400, 300));
	panel2.setMinimumSize(new Dimension(400, 300));
	panel2.setPreferredSize(new Dimension(400, 300));
	panel2.setLayout(gridBagLayout2);
	button1.setText("OK");
	button1.addActionListener(new CustomBuildingDialog_button1_actionAdapter(this));
	button2.setText("Cancel");
	gridLayout1.setHgap(4);
	button2.addActionListener(new CustomBuildingDialog_button2_actionAdapter(this));
	this.addWindowListener(new CustomBuildingDialog_this_windowAdapter(this));
	panel1.setLayout(gridBagLayout1);
	jLabel1.setText("Enter the custom building parameters:");
	jLabel1.setVerticalAlignment(SwingConstants.TOP);
	jLabel1.setVerticalTextPosition(SwingConstants.TOP);
	jLabel2.setText("Width:");
	jLabel3.setText("Height:");
	widthTextField.setText(Integer.toString(width));
	heightTextField.setText(Integer.toString(height));
	heightTextField.addActionListener(new ActionListener() {

	public void actionPerformed(ActionEvent e) {
		heightTextField_actionPerformed(e);
	}
	});
	jLabel5.setText("Terrain:");
	panel1.setMinimumSize(new Dimension(400, 220));
	panel1.setMaximumSize(new Dimension(400, 220));
	terrainComboBox.setMaximumSize(new Dimension(124, 24));
	panel1.add(panel2, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
		,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	panel2.add(widthTextField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(8, 0, 0, 0), 40, 0));
	panel2.add(heightTextField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 40, 0));
	panel2.add(jLabel3, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 20, 0));
	panel2.add(jLabel2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 112, 0, 1), 24, 0));
	panel2.add(jLabel5, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(7, 0, 3, 0), 18, 0));
	panel2.add(terrainComboBox, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 0, 0, 0), 51, 1));
	panel2.add(jLabel1, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(1, 75, 17, 111), 0, 0));
panel1.add(jPanel1, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
		,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 8, 4, 8), 0, 0));
	jPanel1.add(button1, null);
	jPanel1.add(button2, null);
	getContentPane().add(panel1);

	terrainComboBox.addItem("Wooden Building");
	terrainComboBox.addItem("Wooden Building, One level");
	terrainComboBox.addItem("Wooden Building, Two levels");
	terrainComboBox.addItem("Wooden Building, Three levels");
	terrainComboBox.addItem("Stone Building");
	terrainComboBox.addItem("Stone Building, One level");
	terrainComboBox.addItem("Stone Building, Two levels");
	terrainComboBox.addItem("Stone Building, Three levels");

	//set current terrain
	terrainComboBox.setSelectedItem(terrainName);
}

  // OK
  void button1_actionPerformed(ActionEvent e) {
	int 	width 		= 0,
			height 		= 0;
	boolean error = false;

	// convert input to integer values
	try {
		width 		= Integer.parseInt(widthTextField.getText());
		height 		= Integer.parseInt(heightTextField.getText());
	}
	catch(Exception exp) {
		error = true;
	}

	frame.setCustomBuildingParameters(
		(String) terrainComboBox.getSelectedItem(),
		width,
		height
	);

	if(!error){
		dispose();
	}
  }

  // Cancel
  void button2_actionPerformed(ActionEvent e) {
	dispose();
  }

  void this_windowClosing(WindowEvent e) {
	dispose();
  }

	void heightTextField_actionPerformed(ActionEvent e) {

	}
}

class CustomBuildingDialog_button1_actionAdapter implements java.awt.event.ActionListener {
  CustomBuildingDialog adaptee;

  CustomBuildingDialog_button1_actionAdapter(CustomBuildingDialog adaptee) {
	this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
	adaptee.button1_actionPerformed(e);
  }
}

class CustomBuildingDialog_button2_actionAdapter implements ActionListener {
  CustomBuildingDialog adaptee;

  CustomBuildingDialog_button2_actionAdapter(CustomBuildingDialog adaptee) {
	this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
	adaptee.button2_actionPerformed(e);
  }
}

class CustomBuildingDialog_this_windowAdapter extends WindowAdapter {
	CustomBuildingDialog adaptee;

	CustomBuildingDialog_this_windowAdapter(CustomBuildingDialog adaptee) {
	this.adaptee = adaptee;
	  }

  public void windowClosing(WindowEvent e) {
	adaptee.this_windowClosing(e);
  }
}
