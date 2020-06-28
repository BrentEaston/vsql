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

import java.awt.Graphics2D;
import java.awt.Shape;

import CASL.Map.Bridge;
import CASL.Map.Hex;

public class BridgeSelection
		extends Selection {

	// private variables
	private Bridge  bridge;

	// getters and setters
	public Shape	getUpdateShape() 	{return bridge.getShape();}
	public Bridge	getBridge() 	    {return bridge;}
	public Hex		getHex() {

		if (bridge.getLocation() == null) {

		    return null;
		}
		else {

		    return bridge.getLocation().getHex();
		}
	}

	// constructor
	BridgeSelection(Bridge newBridge){bridge = newBridge;}

	//paint
	public void paint(Graphics2D g){

		g.setColor(paintColor);
		g.fill(bridge.getShape());
	}
}

