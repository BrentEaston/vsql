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
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * Title:        RectangularSelection.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class RectangularSelection
		extends Selection {

	// private variables
	private Shape	paintShape;
	private boolean	round;
	private boolean	rectangle;

	// getters
	public Shape	getUpdateShape()	{

		if(round && rectangle) {
			return new Ellipse2D.Float(
				(float) ((Rectangle) paintShape).getX(),
				(float) ((Rectangle) paintShape).getY(),
				(float) ((Rectangle) paintShape).getWidth(),
				(float) ((Rectangle) paintShape).getHeight());
		}
		else {
			return paintShape;
		}
	}

	public boolean	isRound()		{ return round;}
	public boolean	isRectangle()	{ return rectangle;}

	// constructor
	RectangularSelection(Shape paintShape, boolean isRound, boolean isRectangle){

		this.paintShape	= paintShape;
		this.round		= isRound;
		this.rectangle	= isRectangle;
	}

	//paint
	public void paint(Graphics2D g){

		g.setColor(paintColor);
		if(round && rectangle) {
			g.fillArc(
				(int)((Rectangle)paintShape).getX(),
				(int)((Rectangle)paintShape).getY(),
				(int)((Rectangle)paintShape).getWidth(),
				(int)((Rectangle)paintShape).getHeight(),
				0,
				360);
		}
		else {
			g.fill(paintShape);
		}
	}
}
