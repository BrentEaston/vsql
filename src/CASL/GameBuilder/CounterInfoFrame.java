
//Title:        Map Builder
//Version:
//Copyright:    Copyright (c) 1999
//Author:       David Sullivan
//Company:      Harmony Consulting Inc.
//Description:  Map Builder

package CASL.GameBuilder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import CASL.Unit.Counter;

public class CounterInfoFrame extends JFrame {

	public final static int ALL_INFORMATION 	= 1;

	CounterInfoPanel	counterPanel;
	Counter 		counter;
	int			infoLevel;

	public CounterInfoFrame(Counter counter, int infoLevel, Image checkMark) {

		//intialize
		this.counter	= counter;
		this.infoLevel	= infoLevel;

		counterPanel = new CounterInfoPanel(counter, infoLevel, checkMark);

		try  {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {

		Rectangle area;
		String name;

		// set the window name
		if (counter.getNationality() == Counter.NONE) {

			name = counter.getName();
		}
		else {
			name = Counter.nationalityNames[counter.getNationality()] + " " + counter.getName();
		}

		if (infoLevel == ALL_INFORMATION) {

			setTitle("All unit information: " + name);
			area = counter.getPaintAllInformationArea();
			counterPanel.setPreferredSize(new Dimension((int) area.getWidth(), (int) area.getHeight()));
			getContentPane().add(counterPanel, BorderLayout.CENTER);

			// setup the window
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			setSize((int) area.getWidth() + 6, (int) area.getHeight() + 25);
			setLocation((int) (screenSize.getWidth() - area.getWidth())/2, (int) (screenSize.getHeight() - area.getHeight())/2);
			setResizable(false);
			setVisible(true);
			validate();

		}
	}
}

class CounterInfoPanel extends JPanel {

	Counter 	counter;
	int		infoLevel;
	Image		checkMark;
	Rectangle	area;



	public CounterInfoPanel (Counter counter, int infoLevel, Image checkMark){

		this.counter	= counter;
		this.infoLevel	= infoLevel;
		this.checkMark	= checkMark;
		if (counter != null) {
			area = counter.getPaintAllInformationArea();
		}
	}

	public void paint(Graphics g) {

		Graphics2D  screen2D 	= (Graphics2D) g;

//		super.repaint();

		// show the current counter image if selected
		if (counter != null){

			counter.paintAllInformation(
				screen2D,
				0,
				0,
				this,
				Color.white,
				Color.black,
				checkMark);
		}

		// free resources
		screen2D.dispose();
	}
}