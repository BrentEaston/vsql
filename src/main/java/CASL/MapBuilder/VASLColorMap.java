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

import java.awt.Color;

import CASL.Map.Terrain;

/**
 * Title:        VASLColorMap.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class VASLColorMap {

	private Color 	color;
	private Terrain	terrain;
	private int		height;
	private boolean	heightKnown;

	public static int 		NO_HEIGHT		= 99;
	public static Terrain	NO_TERRAIN		= null;

	VASLColorMap(Color c, Terrain t, int h){

		color 	= c;
		terrain	= t;
		height	= h;
	}

	VASLColorMap(Color c, Terrain t){

		color 	= c;
		terrain	= t;
		height	= NO_HEIGHT;
	}

	// setters
	public void setColor(Color c) {color = c;}
	public void setTerrain(Terrain t) {terrain = t;}
	public void setHeight(int h) {height = h;}

	// getters
	public Color getColor() {return color;}
	public Terrain getTerrain() {return terrain;}
	public int getHeight() {return height;}
}

