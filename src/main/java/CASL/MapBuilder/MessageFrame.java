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

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Title:        MessageFrame.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class MessageFrame extends JFrame {

	private JScrollPane MessageScrollPane = new JScrollPane();
	private JTextArea messageTextArea = new JTextArea();

	private String message;

	public MessageFrame() {
		try  {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		MessageScrollPane.setPreferredSize(new Dimension(200, 200));
		this.setTitle("MapBuilder Messages");
		messageTextArea.setEditable(false);
		this.getContentPane().add(MessageScrollPane, BorderLayout.CENTER);
		MessageScrollPane.getViewport().add(messageTextArea, null);
	}

	public void addMessage(String message){

		this.message += message;
		messageTextArea.append(message);

	}
}

