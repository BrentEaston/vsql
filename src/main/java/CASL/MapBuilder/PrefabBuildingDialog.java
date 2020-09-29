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
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import CASL.Map.PrefabBuilding;

/**
 * Title:        PrefabBuildingDialog.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class PrefabBuildingDialog extends JDialog {
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
	private JLabel jLabel5 = new JLabel();
	private JComboBox buildingComboBox = new JComboBox();

	private MapEditFrame frame;

	private PrefabBuilding[] 	buildingList;
	private int			buildingCount;
	private int		 	currentBuilding;

  public PrefabBuildingDialog(
	Frame 	frame,
	String 	title,
	boolean 	modal,
	PrefabBuilding[] 	buildingList,
	int		 	currentBuilding) {

	super(frame, title, modal);

	this.frame = (MapEditFrame) frame;
	this.buildingList		= buildingList;
	this.currentBuilding	= currentBuilding;

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
	button1.addActionListener(new PrefabBuildingDialog_button1_actionAdapter(this));
	button2.setText("Cancel");
	gridLayout1.setHgap(4);
	button2.addActionListener(new PrefabBuildingDialog_button2_actionAdapter(this));
	this.addWindowListener(new PrefabBuildingDialog_this_windowAdapter(this));
	panel1.setLayout(gridBagLayout1);
	jLabel1.setText("Enter the prefab building parameters:");
	jLabel1.setVerticalAlignment(SwingConstants.TOP);
	jLabel1.setVerticalTextPosition(SwingConstants.TOP);
	jLabel5.setText("Building name:");
	panel1.setMinimumSize(new Dimension(400, 220));
	panel1.setMaximumSize(new Dimension(400, 220));
	buildingComboBox.setMaximumSize(new Dimension(124, 24));
	panel1.add(panel2, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
		,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	panel2.add(jLabel3, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 20, 0));
	panel2.add(jLabel2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 112, 0, 1), 24, 0));
	panel2.add(jLabel5, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(7, 0, 3, 0), 18, 0));
	panel2.add(buildingComboBox, new GridBagConstraints(1, 1, 1, 4, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 0, 0, 0), 51, 0));
	panel2.add(jLabel1, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(1, 75, 17, 111), 0, 0));
	panel1.add(jPanel1, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
		,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 8, 4, 8), 0, 0));
	jPanel1.add(button1, null);
	jPanel1.add(button2, null);
	getContentPane().add(panel1);

	// load the building names into the combo box
	int current = 0;
	while (buildingList[current] != null){

		buildingComboBox.addItem(buildingList[current++].getImageName());
	}

	//set current building
	buildingComboBox.setSelectedItem(buildingList[currentBuilding].getImageName());
}

  // OK
  void button1_actionPerformed(ActionEvent e) {

	frame.setPrefabBuildingParameters((String) buildingComboBox.getSelectedItem());
	dispose();
  }

  // Cancel
  void button2_actionPerformed(ActionEvent e) {
	dispose();
  }

  void this_windowClosing(WindowEvent e) {
	dispose();
  }

}

class PrefabBuildingDialog_button1_actionAdapter implements java.awt.event.ActionListener {
  PrefabBuildingDialog adaptee;

  PrefabBuildingDialog_button1_actionAdapter(PrefabBuildingDialog adaptee) {
	this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
	adaptee.button1_actionPerformed(e);
  }
}

class PrefabBuildingDialog_button2_actionAdapter implements ActionListener {
  PrefabBuildingDialog adaptee;

  PrefabBuildingDialog_button2_actionAdapter(PrefabBuildingDialog adaptee) {
	this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
	adaptee.button2_actionPerformed(e);
  }
}

class PrefabBuildingDialog_this_windowAdapter extends WindowAdapter {
	PrefabBuildingDialog adaptee;

	PrefabBuildingDialog_this_windowAdapter(PrefabBuildingDialog adaptee) {
	this.adaptee = adaptee;
	  }

  public void windowClosing(WindowEvent e) {
	adaptee.this_windowClosing(e);
  }
}
