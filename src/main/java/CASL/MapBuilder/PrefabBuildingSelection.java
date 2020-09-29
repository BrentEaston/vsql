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

import CASL.Map.PrefabBuilding;

public class PrefabBuildingSelection
		extends Selection {

	// private variables
	private Shape		paintShape;
	private PrefabBuilding	building;

	// getters
	public PrefabBuilding	getBuilding() { return building;}
	public Shape getUpdateShape()	{ return paintShape;}

	// constructor
	PrefabBuildingSelection(Shape paintShape, PrefabBuilding building){

		this.paintShape	= paintShape;
		this.building	= building;
	}

	//paint
	public void paint(Graphics2D g){

		g.setColor(paintColor);
		g.fill(paintShape);
	}
}
