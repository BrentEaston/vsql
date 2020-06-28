package	CASL.GameBuilder;

import java.awt.BorderLayout;
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
import java.text.DateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import CASL.Scenario.Scenario;

/**
 * Title:        NewScenarioDialog.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class NewScenarioDialog extends JDialog {

	GameEditFrame	frame;

	boolean newScenario;

	DateFormat dateFormatter = DateFormat.getDateInstance();

	JPanel NewScenarioPanel = new JPanel();
	JPanel jPanel1 	= new JPanel();
	JPanel mainPanel 	= new JPanel();
	JPanel SANPanel	= new JPanel();

	JButton button1 = new JButton();
	JButton button2 = new JButton();
	Border border1;

	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridLayout gridLayout1 = new GridLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();

	// labels
	JLabel headerLabel 		= new JLabel("Please enter the initial scenario information:");
	JLabel scenarioNameLabel 	= new JLabel("Scenario name: ");
	JLabel dateLabel 			= new JLabel("Scenario date: ");
	JLabel theaterLabel 		= new JLabel("Theater: ");
	JLabel turnsLabel	 		= new JLabel("Turns: ");
	JLabel halfTurnLabel	 	= new JLabel("Additional half turn?");
	JLabel ECLabel	 		= new JLabel("EC: ");
	JLabel SANLabel	 		= new JLabel("SAN: ");
	JLabel ELRLabel	 		= new JLabel("ELR: ");
	JLabel alliedHeaderLabel	= new JLabel("Allies");
	JLabel axisHeaderLabel		= new JLabel("Axis");

	// fields
	JTextField	scenarioNameTextField = new JTextField();
	JTextField	dateTextField = new JTextField();
	JComboBox 	theaterComboBox = new JComboBox(Scenario.theaterNames);
	JComboBox 	ECComboBox = new JComboBox(Scenario.ECNames);
	JTextField	alliedSANTextField = new JTextField();
	JTextField	turnsTextField = new JTextField();
	JCheckBox	halfTurnCheckBox = new JCheckBox();
	JTextField	axisSANTextField = new JTextField();
	JTextField	alliedELRTextField = new JTextField();
	JTextField	axisELRTextField = new JTextField();

	// for initializing the scenario
	public NewScenarioDialog(Frame frame, String title, boolean modal) {

		super(frame, title, modal);

		this.frame = (GameEditFrame) frame;
		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		newScenario = true;
		pack();
	}

	// for updating the scenario values
	public NewScenarioDialog(
		Frame 	frame,
		String 	title,
		boolean modal,
		String 	scenarioName,
		int 	theater,
		int 	EC,
		Date 	date,
		int		turns,
		boolean	halfTurn,
		int		axisELR,
		int		alliedELR,
		int		axisSAN,
		int		alliedSAN
	) {

		super(frame, title, modal);

		this.frame = (GameEditFrame) frame;
		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// set the existing values
		scenarioNameTextField.setText(scenarioName);
		theaterComboBox.setSelectedIndex(theater);
		ECComboBox.setSelectedIndex(EC);
		dateTextField.setText(dateFormatter.format(date));
		turnsTextField.setText(Integer.toString(turns));
		halfTurnCheckBox.setSelected(halfTurn);
		axisELRTextField.setText(Integer.toString(axisELR));
		alliedELRTextField.setText(Integer.toString(alliedELR));
		axisSANTextField.setText(Integer.toString(axisSAN));
		alliedSANTextField.setText(Integer.toString(alliedSAN));

		newScenario = false;
		pack();
	}

	private void jbInit() throws Exception {

		border1 = BorderFactory.createRaisedBevelBorder();
		jPanel1.setLayout(gridLayout1);
		mainPanel.setBorder(border1);
		mainPanel.setMaximumSize(new Dimension(450, 350));
		mainPanel.setMinimumSize(new Dimension(450, 350));
		mainPanel.setPreferredSize(new Dimension(450, 350));
		mainPanel.setLayout(gridBagLayout2);

		button1.setText("OK");
		button1.addActionListener(new newScenarioDialog_button1_actionAdapter(this));
		button2.setText("Cancel");
		gridLayout1.setHgap(4);
		button2.addActionListener(new newScenarioDialog_button2_actionAdapter(this));
		this.addWindowListener(new newScenarioDialog_this_windowAdapter(this));
		NewScenarioPanel.setLayout(gridBagLayout1);

		scenarioNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		scenarioNameTextField.setMinimumSize(new Dimension(150, 21));
		scenarioNameTextField.setText("New Scenario");

		theaterComboBox.setMinimumSize(new Dimension(200, 21));
		theaterLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		theaterComboBox.setSelectedIndex(Scenario.WEST_FRONT);

		ECComboBox.setMinimumSize(new Dimension(200, 21));
		ECLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		ECComboBox.setSelectedIndex(Scenario.MODERATE);

		dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		dateTextField.setMinimumSize(new Dimension(200, 21));
		dateTextField.setHorizontalAlignment(SwingConstants.LEFT);
		dateTextField.setText(dateFormatter.format(new Date()));

		turnsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		turnsTextField.setMinimumSize(new Dimension(200, 21));
		turnsTextField.setHorizontalAlignment(SwingConstants.LEFT);
		turnsTextField.setText("8");

		NewScenarioPanel.setMinimumSize(new Dimension(450, 400));
		NewScenarioPanel.setPreferredSize(new Dimension(450, 400));


		halfTurnCheckBox.setText("Addt\'l Half Turn?");
		halfTurnCheckBox.setSelected(false);

        this.getContentPane().add(NewScenarioPanel, BorderLayout.CENTER);
		NewScenarioPanel.add(jPanel1, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 8, 4, 8), 0, 0));
		jPanel1.add(button1, null);
		jPanel1.add(button2, null);
		NewScenarioPanel.add(mainPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
			,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(13, 13, 15, 12), 0, 0));

		mainPanel.add(headerLabel, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(17, 134, 23, 230), 4, 5));

		mainPanel.add(scenarioNameLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 118, 0, 15), 35, 5));
		mainPanel.add(scenarioNameTextField, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 66), 44, 0));

		mainPanel.add(theaterLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 10, 14), 24, 0));
		mainPanel.add(theaterComboBox, new GridBagConstraints(1, 2, 2, 2, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), -6, 0));

		mainPanel.add(ECLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 10, 14), 24, 0));
		mainPanel.add(ECComboBox, new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), -6, 0));

		mainPanel.add(dateLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 10, 14), 24, 0));
		mainPanel.add(dateTextField, new GridBagConstraints(1, 5, 2, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), -108, 0));

		mainPanel.add(turnsLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 10, 14), 24, 0));
		mainPanel.add(turnsTextField, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), -107, 0));

        mainPanel.add(halfTurnCheckBox, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 13, 0, 0), -1, -5));

		// setup the SAN/ELR panel
		mainPanel.setMaximumSize(new Dimension(400, 400));
		mainPanel.setMinimumSize(new Dimension(400, 400));
		JLabel blankLabel = new JLabel("  ");
		blankLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		blankLabel.setMinimumSize(new Dimension(100, 21));
		alliedHeaderLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		alliedHeaderLabel.setMinimumSize(new Dimension(100, 21));
		axisHeaderLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		axisHeaderLabel.setMinimumSize(new Dimension(100, 21));
		SANLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		SANLabel.setMinimumSize(new Dimension(100, 21));
		alliedSANTextField.setMinimumSize(new Dimension(75, 21));
		alliedSANTextField.setText("3");
		alliedSANTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		axisSANTextField.setMinimumSize(new Dimension(75, 21));
		axisSANTextField.setText("3");
		axisSANTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		ELRLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		ELRLabel.setMinimumSize(new Dimension(100, 21));
		alliedELRTextField.setMinimumSize(new Dimension(75, 21));
		alliedELRTextField.setText("3");
		alliedELRTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		axisELRTextField.setMinimumSize(new Dimension(75, 21));
		axisELRTextField.setText("3");
		axisELRTextField.setHorizontalAlignment(SwingConstants.RIGHT);

		//set up the SAN/ELR panel
		SANPanel.setMaximumSize(new Dimension(200, 65));
		SANPanel.setMinimumSize(new Dimension(200, 65));
		SANPanel.setLayout(new GridLayout(3,3));
		SANPanel.add(blankLabel);
		SANPanel.add(axisHeaderLabel);
		SANPanel.add(alliedHeaderLabel);
		SANPanel.add(SANLabel);
		SANPanel.add(axisSANTextField);
		SANPanel.add(alliedSANTextField);
		SANPanel.add(ELRLabel);
		SANPanel.add(axisELRTextField);
		SANPanel.add(alliedELRTextField);

		mainPanel.add(SANPanel, new GridBagConstraints(0, 7, 4, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 4, 5));

		this.validate();
	}

	// OK
	void button1_actionPerformed(ActionEvent e) {

		int		axisELR		= 0;
		int		alliedELR	= 0;
		int		axisSAN		= 0;
		int		alliedSAN	= 0;
		int		turns		= 0;
		boolean	halfTurn	= halfTurnCheckBox.isSelected();
		Date	date		= null;

		String	scenarioName 	= scenarioNameTextField.getText();
		int		theater			= theaterComboBox.getSelectedIndex();
		int		EC				= ECComboBox.getSelectedIndex();

		boolean error = false;

		// convert input to numeric/date values
		try {
			axisELR 	= Integer.parseInt(axisELRTextField.getText());
			alliedELR 	= Integer.parseInt(alliedELRTextField.getText());
			axisSAN 	= Integer.parseInt(axisSANTextField.getText());
			alliedSAN 	= Integer.parseInt(alliedSANTextField.getText());
			turns 		= Integer.parseInt(turnsTextField.getText());
			date	 	= dateFormatter.parse(dateTextField.getText());
		}
		catch(Exception exp) {
			error = true;
		}

		// enforce required fields
		if (!error && scenarioName != null && !scenarioName.equals("") && date != null){

			dispose();

			if (newScenario){

				frame.newScenario(
					scenarioName,
					theater,
					EC,
					date,
					turns,
					halfTurn,
					axisELR,
					alliedELR,
					axisSAN,
					alliedSAN);
			}
			else {

				frame.initializeScenario(
					scenarioName,
					theater,
					EC,
					date,
					turns,
					halfTurn,
					axisELR,
					alliedELR,
					axisSAN,
					alliedSAN);
			}
		}
		else {

			// display the error
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

class newScenarioDialog_button1_actionAdapter implements ActionListener {
	NewScenarioDialog adaptee;

	newScenarioDialog_button1_actionAdapter(NewScenarioDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.button1_actionPerformed(e);
	}
}

class newScenarioDialog_button2_actionAdapter implements ActionListener {
	NewScenarioDialog adaptee;

	newScenarioDialog_button2_actionAdapter(NewScenarioDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.button2_actionPerformed(e);
	}
}

class newScenarioDialog_this_windowAdapter extends WindowAdapter {
	NewScenarioDialog adaptee;

	newScenarioDialog_this_windowAdapter(NewScenarioDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.this_windowClosing(e);
	}
}
