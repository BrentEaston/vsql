package	CASL.GameBuilder;
// This snippet creates a new dialog box
// with buttons on the bottom.

//Title:
//Version:
//Copyright:
//Author:
//Company:
//Description:

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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class NewScenerioDialog extends JDialog {

	GameEditFrame	frame;

	JPanel NewScenerioPanel = new JPanel();
	JPanel jPanel1 = new JPanel();
	JPanel panel2 = new JPanel();

	JButton button1 = new JButton();
	JButton button2 = new JButton();
	Border border1;

	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridLayout gridLayout1 = new GridLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();

	// labels
	JLabel headerLabel 		= new JLabel();
	JLabel scenerioNameLabel 	= new JLabel();
	JLabel mapNameLabel 		= new JLabel();

	// fields
	JTextField scenerioNameTextField = new JTextField();
	JTextField theaterTextField = new JTextField();
	JTextField mapNameTextField = new JTextField();
    JLabel theaterLabel = new JLabel();


	public NewScenerioDialog(Frame frame, String title, boolean modal) {

		super(frame, title, modal);

		this.frame = (GameEditFrame) frame;
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
		panel2.setMaximumSize(new Dimension(450, 200));
		panel2.setMinimumSize(new Dimension(450, 200));
		panel2.setLayout(gridBagLayout2);
		button1.setText("OK");
		button1.addActionListener(new newScenerioDialog_button1_actionAdapter(this));
		button2.setText("Cancel");
		gridLayout1.setHgap(4);
		button2.addActionListener(new newScenerioDialog_button2_actionAdapter(this));
		this.addWindowListener(new newScenerioDialog_this_windowAdapter(this));
		NewScenerioPanel.setLayout(gridBagLayout1);

		headerLabel.setText("Please enter the following scenerio information:");
		scenerioNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		scenerioNameLabel.setText("Scenerio name:");
		mapNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		mapNameLabel.setText("Map name:");

		scenerioNameTextField.setMinimumSize(new Dimension(150, 21));
		mapNameTextField.setMinimumSize(new Dimension(200, 21));
		theaterTextField.setMinimumSize(new Dimension(200, 21));

		theaterLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        theaterLabel.setText("Theater:");
        NewScenerioPanel.setMinimumSize(new Dimension(450, 235));
        NewScenerioPanel.setPreferredSize(new Dimension(450, 235));





        this.getContentPane().add(NewScenerioPanel, BorderLayout.CENTER);
        NewScenerioPanel.add(jPanel1, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 8, 4, 8), 0, 0));
        jPanel1.add(button1, null);
        jPanel1.add(button2, null);
        NewScenerioPanel.add(panel2, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(13, 13, 15, 12), 0, 0));
        panel2.add(headerLabel, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(17, 134, 9, 230), 4, 5));
        panel2.add(scenerioNameLabel, new GridBagConstraints(0, 1, 2, 2, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 118, 0, 1), 83, 5));
        panel2.add(scenerioNameTextField, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 66), 44, 0));
        panel2.add(theaterTextField, new GridBagConstraints(2, 2, 1, 2, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), -6, 0));
        panel2.add(mapNameLabel, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 88, 2, 12), 137, 5));
        panel2.add(mapNameTextField, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), -5, 0));
        panel2.add(theaterLabel, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(2, 0, 30, 14), 24, 0));


		panel2.setPreferredSize(new Dimension(450, 200));
		this.validate();
	}

	// OK
	void button1_actionPerformed(ActionEvent e) {

		String scenerioName 	= scenerioNameTextField.getText();
		String mapName 		= mapNameTextField.getText();
		String theater		= theaterTextField.getText();

		// enforce required fields
		if (mapName == null || scenerioName == null || mapName.equals("") || scenerioName.equals("")){

			// not for now
			dispose();
//			frame.newScenerio(scenerioName, Scenario.EAST_FRONT, Scenario.MODERATE, new Date(), 8, false, 3,3,3,3);

/*		String 	scenarioName,
		int 		theater,
		int 		EC,
		Date 		date,
		int		turns,
		boolean	halfTurn,
		int		axisELR,
		int		alliedELR,
		int		axisSAN,
		int		alliedSAN) {
*/

		}
		else {
			// create a new scenrio
			dispose();
//			frame.newScenerio(scenerioName, mapName, theater);
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

class newScenerioDialog_button1_actionAdapter implements ActionListener {
	NewScenerioDialog adaptee;

	newScenerioDialog_button1_actionAdapter(NewScenerioDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.button1_actionPerformed(e);
	}
}

class newScenerioDialog_button2_actionAdapter implements ActionListener {
	NewScenerioDialog adaptee;

	newScenerioDialog_button2_actionAdapter(NewScenerioDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.button2_actionPerformed(e);
	}
}

class newScenerioDialog_this_windowAdapter extends WindowAdapter {
	NewScenerioDialog adaptee;

	newScenerioDialog_this_windowAdapter(NewScenerioDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.this_windowClosing(e);
	}
}
