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
package CASL.MapBuilder;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.UIManager;

import CASL.MapBuilder.Utility.CASLProperties;

/**
 * Title:        MapBuilder.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class MapBuilder { 

	boolean packFrame = false;

	// windows
	private MapEditFrame frame			= null;
	private	MessageFrame messageFrame 	= null;

	// error file
	private static PrintStream	errors = null;

	//Construct the application
	public MapBuilder(String mapName) {

		// set up the map editing frame
		frame = new MapEditFrame();

		// open the map
		if (mapName != null) {

			frame.openMap(mapName);
		}
	}

	//Main method
	public static void main(String[] args) {
		try  {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e) {
		}

		// create the error file
		try {

			errors = new PrintStream(new BufferedOutputStream(new FileOutputStream("err.txt")));

		} catch(Exception e) {
			e.printStackTrace();
		}

		// read the CASL properties file, quit if there's a problem
		if (CASLProperties.getCASLHome() == null){

			writeError("Cannot read the properties file CASL.properties");
		    System.exit(0);
		}

		// map name provided?
		if (args.length == 1) {

			new MapBuilder(args[0]);
		}
		else {

			new MapBuilder(null);
		}
	}

	// write error routine
	public static void writeError(String s) {

		if (errors == null) {

			System.err.println(s);
		}
		else {

			errors.println(s);
			errors.flush();
		}
	}

	// write exceptions
	public static void writeException(Exception e) {

		if (errors == null) {

			e.printStackTrace(System.err);
		}
		else {

			e.printStackTrace(errors);
			errors.flush();
		}
	}
}