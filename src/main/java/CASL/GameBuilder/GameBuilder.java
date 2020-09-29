package CASL.GameBuilder;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.UIManager;

/**
 * Title:        GameBuilder.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class GameBuilder {

	boolean packFrame = false;

	// windows
	private GameEditFrame frame			= null;

	//Construct the application
	public GameBuilder() {

		// set up the map editing frame
		frame = new GameEditFrame();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		frame.setSize(screenSize.width, screenSize.height);
		frame.setSize(screenSize.width, screenSize.height);
		frame.setLocation(0, 0);
		frame.setVisible(true);

		//Validate frames that have preset sizes
		//Pack frames that have useful preferred size info, e.g. from their layout
		if (packFrame)
			frame.pack();
		else
			frame.validate();

		frame.requestFocus();;
	}

	//Main method
	public static void main(String[] args) {
		try  {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e) {
		}

		// redirect the standard error stream
		PrintStream		 stderr = null;
		try {
			stderr = new PrintStream(new BufferedOutputStream(new FileOutputStream("err.txt")));
		} catch(Exception e) {
			e.printStackTrace();
		}

		System.out.println("Redirecting errors to err.txt...");
		System.setErr(stderr);

		new GameBuilder();

	}
}