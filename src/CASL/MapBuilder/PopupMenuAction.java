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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;


/**
 * Title:        PopupMenuAction.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class PopupMenuAction
	extends AbstractAction {

	private MapEditor	mapEditor;
	private String		action;
	private int			x;
	private int			y;

	PopupMenuAction (String action) {
		super(action);
		putValue(Action.SHORT_DESCRIPTION, action);
		this.action = action;
	}

	PopupMenuAction (String action, MapEditor e, int x, int y) {
		super(action);
		putValue(Action.SHORT_DESCRIPTION, action);
		this.action = action;
		this.mapEditor = e;
		this.x = x;
		this.y = y;
	}

	public void setMapEditor(MapEditor e) {this.mapEditor = e;}

	public void actionPerformed(ActionEvent actionEvent) {
		mapEditor.mousePopupMenuEvent(action, actionEvent, x, y);
	}

	public void setX(int x){ this.x = x;}
	public void setY(int y){ this.y = y;}

	public int getX(){ return x;}
	public int getY(){ return y;}
}
