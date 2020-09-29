// This snippet creates a new dialog box
// with buttons on the bottom.

//Title:
//Version:
//Copyright:
//Author:
//Company:
//Description:

package 	CASL.GameBuilder;
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
import javax.swing.JPanel;
import javax.swing.border.Border;

public class ScenarioDialog extends JDialog {

	GameEditFrame frame;

	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JButton yesButton = new JButton();
	JButton cancelButton = new JButton();
	JButton noButton = new JButton();

	Border border1;
	JPanel jPanel1 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridLayout gridLayout1 = new GridLayout();

	public ScenarioDialog(Frame newFrame, String title, boolean modal) {
	super(newFrame, title, modal);

	frame = (GameEditFrame) newFrame;

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
	yesButton.setText("Yes");
	yesButton.addActionListener(new ScenarioDialog_yesButton_actionAdapter(this));
	cancelButton.setText("Cancel");
	gridLayout1.setHgap(4);
	cancelButton.addActionListener(new ScenarioDialog_cancelButton_actionAdapter(this));
	this.addWindowListener(new ScenarioDialog_this_windowAdapter(this));
	panel1.setLayout(gridBagLayout1);
	noButton.addActionListener(new ScenarioDialog_noButton_actionAdapter(this));
		noButton.setText("No");
		panel1.add(panel2, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
			,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	panel1.add(jPanel1, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 8, 4, 8), 0, 0));
	jPanel1.add(yesButton, null);
		jPanel1.add(noButton, null);
	jPanel1.add(cancelButton, null);
	getContentPane().add(panel1);
}

	// Yes
	void yesButton_actionPerformed(ActionEvent e) {
	dispose();
	}

	// No
	void noButton_actionPerformed(ActionEvent e) {
	dispose();
	}

	// Cancel
	void cancelButton_actionPerformed(ActionEvent e) {
	dispose();
	}

	void this_windowClosing(WindowEvent e) {
	dispose();
	}
}

class ScenarioDialog_yesButton_actionAdapter implements ActionListener {
	ScenarioDialog adaptee;

	ScenarioDialog_yesButton_actionAdapter(ScenarioDialog adaptee) {
	this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
	adaptee.yesButton_actionPerformed(e);
	}
}

class ScenarioDialog_noButton_actionAdapter implements ActionListener {
	ScenarioDialog adaptee;

	ScenarioDialog_noButton_actionAdapter(ScenarioDialog adaptee) {
	this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
	adaptee.noButton_actionPerformed(e);
	}
}

class ScenarioDialog_cancelButton_actionAdapter implements ActionListener {
	ScenarioDialog adaptee;

	ScenarioDialog_cancelButton_actionAdapter(ScenarioDialog adaptee) {
	this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
	adaptee.cancelButton_actionPerformed(e);
	}
}

class ScenarioDialog_this_windowAdapter extends WindowAdapter {
	ScenarioDialog adaptee;

	ScenarioDialog_this_windowAdapter(ScenarioDialog adaptee) {
	this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
	adaptee.this_windowClosing(e);
	}
}
