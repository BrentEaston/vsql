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

import CASL.Map.Terrain;

/**
 * Title:        VASLMultilevelBuildings.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class VASLMultilevelBuildings {

	public static int MAX_BUILDINGS = 500;

	private int 	    boardNumber;
	private String[]	hexNames     = new String[MAX_BUILDINGS];
	private Terrain[]	terrainTypes = new Terrain[MAX_BUILDINGS];

	VASLMultilevelBuildings (int boardNumber){

		this.boardNumber = boardNumber;
	}

	public int getBoardNumber() {return boardNumber;}

	public void addBuilding(String hexName, Terrain terrain){

		for (int x = 0; x < MAX_BUILDINGS; x++){

		    if (hexNames[x] == null){

				hexNames[x]     = hexName;
				terrainTypes[x] = terrain;
				return;
		    }
		}
	}

	public Terrain getTerrain(String hexName){

		for (int x = 0; x < MAX_BUILDINGS; x++){

		    if (hexNames[x] != null && hexNames[x].equals(hexName)){

				return terrainTypes[x];
		    }
		}

		return null;
	}

	public int getCount(){

		int count = 0;
		for (int x = 0; x < MAX_BUILDINGS; x++){

		    if (hexNames[x] == null) {

				return count;
		    }
		    count++;
		}

		return count;
	}
}

