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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import CASL.Map.GameMap;
import CASL.Map.Hex;
import CASL.Map.Location;
import CASL.Map.Terrain;

/**
 * Title:        VASLColorMap.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */

/**
 * Title:        VASLMapConverter.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class VASLMapConverter{

	private static int MAX_COLORS		= 200;
	private static int TERRAIN_OFFSET	= 128;
	private static int MAX_BOARDS   	= 100;

	// lists
	private Terrain                 terrainList[];
	private VASLColorMap            colorMap[];
	private VASLMultilevelBuildings buildingList[] = new VASLMultilevelBuildings[MAX_BOARDS];

	private MapEditFrame frame;

	private GameMap		map;
	private BufferedImage	image;

	private int currentBoard;

	VASLMapConverter(GameMap m, MapEditFrame f) {

		terrainList = Terrain.getTerrainList();
		colorMap = new VASLColorMap[MAX_COLORS];
		map = m;
		frame = f;

		// set up the multilevel terrain information for all boards
		for (int x = 0; x < MAX_BOARDS; x++){

		    buildingList[x] = new VASLMultilevelBuildings(x);
		}

		// Board 1
		buildingList[1].addBuilding("E1", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("E4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("F3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("G3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("G4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("F5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("F6", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("G6", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("H5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("J4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("J5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("K4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("K5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("L6", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("L7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("M7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("M5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("N3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("N4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("N5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("P3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("Q3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("Q4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("T2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("U2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("U3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("V2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("W4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("W5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("X2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("X3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("X4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("X5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("Y3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("Y4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("Y5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("Y7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("Y8", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("Z6", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("Z7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("AA7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("AA4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("AA5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[1].addBuilding("BB4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);

		buildingList[1].addBuilding("P7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("P8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("P5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("Q6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("R1", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("S1", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("S5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("T4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("T7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("U8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("CC3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("DD3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		buildingList[1].addBuilding("F1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("G1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("K7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("K8", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("Z1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("AA2", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("AA9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[1].addBuilding("BB8", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		// Board 3
		buildingList[3].addBuilding("M2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[3].addBuilding("N1", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[3].addBuilding("N2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[3].addBuilding("R3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[3].addBuilding("S3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[3].addBuilding("R5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[3].addBuilding("R6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		buildingList[3].addBuilding("L4", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[3].addBuilding("M5", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[3].addBuilding("T6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[3].addBuilding("U6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		// Board 4
		buildingList[4].addBuilding("O6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[4].addBuilding("P6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		// Board 6
		buildingList[6].addBuilding("K1", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[6].addBuilding("K2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		buildingList[6].addBuilding("J8", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[6].addBuilding("K8", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[6].addBuilding("K9", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[6].addBuilding("M5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[6].addBuilding("M6", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[6].addBuilding("N3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[6].addBuilding("N4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[6].addBuilding("N5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[6].addBuilding("O3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[6].addBuilding("O4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);

		// Board 8
		buildingList[8].addBuilding("C9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[8].addBuilding("D9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[8].addBuilding("CC9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[8].addBuilding("DD9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[8].addBuilding("EE1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[8].addBuilding("FF1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[8].addBuilding("F1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[8].addBuilding("G2", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[8].addBuilding("H2", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[8].addBuilding("H3", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[8].addBuilding("T1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[8].addBuilding("U1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[8].addBuilding("CC2", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[8].addBuilding("DD2", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		buildingList[8].addBuilding("E3", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[8].addBuilding("F3", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[8].addBuilding("G4", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[8].addBuilding("J2", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[8].addBuilding("K2", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[8].addBuilding("K3", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[8].addBuilding("M2", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[8].addBuilding("M3", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[8].addBuilding("N2", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[8].addBuilding("O2", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[8].addBuilding("R9", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[8].addBuilding("S9", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[8].addBuilding("S10", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);

		buildingList[8].addBuilding("V6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[8].addBuilding("W6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		buildingList[8].addBuilding("B1", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[8].addBuilding("B2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[8].addBuilding("C1", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[8].addBuilding("C3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);

		// Board 9
		buildingList[9].addBuilding("DD3", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[9].addBuilding("EE4", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		// Board 10
		buildingList[10].addBuilding("U5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("U6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("V7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("W8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("X8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("W3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("W4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("W5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("X2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("Y7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("Z6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("Z7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("AA7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("AA4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("AA5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("BB5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("DD3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("DD4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("DD5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[10].addBuilding("EE5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		// Board 12
		buildingList[12].addBuilding("M3", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[12].addBuilding("S9", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);

		buildingList[12].addBuilding("O6", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[12].addBuilding("O7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[12].addBuilding("O4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[12].addBuilding("P3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[12].addBuilding("Q4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[12].addBuilding("R2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[12].addBuilding("U5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[12].addBuilding("V4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[12].addBuilding("V5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);

		buildingList[12].addBuilding("Q6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[12].addBuilding("Q8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[12].addBuilding("R6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		buildingList[12].addBuilding("R7", terrainList[Terrain.STONE_MARKET_PLACE]);

		// board 14
		buildingList[14].addBuilding("H9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[14].addBuilding("I9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[14].addBuilding("S9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[14].addBuilding("T9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		buildingList[14].addBuilding("C1", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[14].addBuilding("D1", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[14].addBuilding("L5", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);

		// board 15
		buildingList[15].addBuilding("G5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[15].addBuilding("H5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);

		buildingList[15].addBuilding("G9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[15].addBuilding("H9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		buildingList[15].addBuilding("Q8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[15].addBuilding("R8", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);

		// board 16
		buildingList[16].addBuilding("O3", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[16].addBuilding("O4", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		// board 17
		buildingList[17].addBuilding("P4", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[17].addBuilding("Q4", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[17].addBuilding("W3", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[17].addBuilding("W4", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		// board 20
		buildingList[20].addBuilding("B1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("C1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("C3", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("D2", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("E1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("F1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("L1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("M1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("N6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("O6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("M9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("M10", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("R6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("S6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("S7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("Z8", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("Z9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("AA6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("BB5", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("CC5", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("CC2", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("DD2", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("BB8", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("CC9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("DD9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("EE9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("FF8", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		buildingList[20].addBuilding("K1", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("K2", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("M7", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("EE1", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("FF1", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);

		buildingList[20].addBuilding("D4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("E5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("F5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("G6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("H6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("I7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("M3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("M4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("N3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("O3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("P2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("Q2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("L6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("M6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("N5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("O5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("W4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("X3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("U8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("V7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("W7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("X6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("V9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("W9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("BB6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("CC7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("DD6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("DD5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("FF6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[20].addBuilding("FF7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		buildingList[20].addBuilding("B6", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("B7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("C6", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("C7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("C8", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("C9", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("D6", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("D7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("D8", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("D9", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("E7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("G3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("G4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("H2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("H3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("H4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("I3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("O8", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("O9", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("P8", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("P9", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("S4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("T2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("T3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("U2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("U3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("V2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("Z2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("Z3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("Z4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("AA3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("AA4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[20].addBuilding("Z7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);

		// board 21
		buildingList[21].addBuilding("G4", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("G5", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("J1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("K2", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("N1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("O1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("T1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("U2", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("T5", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("U6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("T8", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("U9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("V9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("W10", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("X2", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("X3", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("AA6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("AA7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("CC5", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("CC6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("CC7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("CC2", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("DD2", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("DD7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("EE7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		buildingList[21].addBuilding("B2", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("B3", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("C2", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("D1", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("T4", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("Y2", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("Y3", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("Y4", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("Z2", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("AA2", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("AA3", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);

		buildingList[21].addBuilding("F2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("G2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("B6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("B7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("D5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("D6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("E5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("E6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("J9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("J10", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("K9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("M9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("N9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("O10", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("O8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("P7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("P8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("Q7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("R2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("R3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("R4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("R5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("R6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("S3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("X5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("X6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("X7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("Y8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("Z8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("Z5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("Z6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("Z7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("EE10", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[21].addBuilding("FF9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		buildingList[21].addBuilding("E9", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("F9", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("G10", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("F7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("G7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("G8", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("I4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("I5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("J3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("J4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("J5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("K4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("S8", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("CC8", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("DD3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("DD4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[21].addBuilding("EE4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);

		// board 22
		buildingList[22].addBuilding("D5", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("D6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("H9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("I9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("I7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("J7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("K6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("K7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("M2", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("N2", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("O3", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("O4", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("O9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("P8", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("P9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("T6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("U6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("U3", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("V2", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("V3", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("Z2", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("Z3", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("BB6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("CC6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("DD5", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("DD7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("DD8", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("EE7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("EE8", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		buildingList[22].addBuilding("C3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("C4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("K10", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[22].addBuilding("L9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		buildingList[22].addBuilding("F3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[22].addBuilding("F4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[22].addBuilding("F5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[22].addBuilding("G4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[22].addBuilding("G5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[22].addBuilding("G6", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[22].addBuilding("H4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[22].addBuilding("W8", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[22].addBuilding("X7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[22].addBuilding("X8", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);

		// board 23
		buildingList[23].addBuilding("C7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("D6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("D7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("J3", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("K4", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		buildingList[23].addBuilding("E9", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);
		buildingList[23].addBuilding("F9", terrainList[Terrain.WOODEN_BUILDING_2_LEVEL]);

		buildingList[23].addBuilding("G8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("H7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("K7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("L7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("M8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("M5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("N5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("N9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("O10", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("Z1", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("Z2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("AA3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("BB3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("CC2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("DD1", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("DD2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("CC7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("CC8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("CC9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[23].addBuilding("DD7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		buildingList[23].addBuilding("D4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[23].addBuilding("E3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[23].addBuilding("E4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[23].addBuilding("F3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[23].addBuilding("F4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[23].addBuilding("X6", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[23].addBuilding("X7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[23].addBuilding("Y6", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[23].addBuilding("Y7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[23].addBuilding("Y8", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[23].addBuilding("Z6", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[23].addBuilding("Z7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);

		// board 24
		buildingList[24].addBuilding("M4", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[24].addBuilding("N3", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[24].addBuilding("S7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[24].addBuilding("T6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		buildingList[24].addBuilding("R4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[24].addBuilding("R5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[24].addBuilding("S5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		buildingList[24].addBuilding("U4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[24].addBuilding("V3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);

		// board 32
		buildingList[32].addBuilding("C1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[32].addBuilding("D1", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		// board 33
		buildingList[33].addBuilding("R6", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[33].addBuilding("R7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		// board 35
		buildingList[35].addBuilding("BB4", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[35].addBuilding("CC5", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		// board 41
		buildingList[41].addBuilding("V6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[41].addBuilding("V7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[41].addBuilding("V8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[41].addBuilding("W9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[41].addBuilding("X1", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[41].addBuilding("X2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		buildingList[41].addBuilding("FF2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[41].addBuilding("FF3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);

		// board 42
		buildingList[42].addBuilding("V1", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[42].addBuilding("V2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		// board 43
		buildingList[43].addBuilding("M9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[43].addBuilding("N9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[43].addBuilding("O7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[43].addBuilding("O8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		// board 44
		buildingList[44].addBuilding("R7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[44].addBuilding("S7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[44].addBuilding("BB8", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[44].addBuilding("CC9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		// board 45
		buildingList[45].addBuilding("D7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		buildingList[45].addBuilding("C2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("D1", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("D3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("D4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("D5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("E6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("D9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("E10", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("E8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("F8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("G9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("F6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("G7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("H6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("H7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("I6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("I7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("I8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("J7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("J8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("K9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("M9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("O9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("P8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("Q9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("R2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("S3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("T3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("U4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("U2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("V2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("W2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("V6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("V7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("W7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("W8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("X7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("X8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("W5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("X5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("Y6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("X3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("Y3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("Y4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("Z3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("Z4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("Z7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("Z8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("AA8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("AA9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("AA2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("BB2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("AA6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("BB4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("BB5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("CC4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("CC5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("DD3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("CC7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("CC8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("DD6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[45].addBuilding("EE4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		buildingList[45].addBuilding("I4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("I5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("J2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("J3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("J4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("J5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("K2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("K3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("L2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("M7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("M8", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("N7", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("N8", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("O8", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("O4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("P3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("P4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("Q4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("Q5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("R4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("R5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("S6", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("T5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[45].addBuilding("T6", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);

		// board 46
		buildingList[46].addBuilding("AA3", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("BB3", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("BB4", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("CC5", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("DD5", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		buildingList[46].addBuilding("J6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("J7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("V8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("V9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("W7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("X6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("X7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("Z5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("AA9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("CC7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("DD6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("DD7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("EE4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("EE5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("FF3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[46].addBuilding("EE8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		// board 49
		buildingList[49].addBuilding("G7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[49].addBuilding("H7", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[49].addBuilding("AA8", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);
		buildingList[49].addBuilding("AA9", terrainList[Terrain.WOODEN_BUILDING_1_LEVEL]);

		buildingList[49].addBuilding("D8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[49].addBuilding("E8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[49].addBuilding("E9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[49].addBuilding("E10", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[49].addBuilding("F8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[49].addBuilding("F9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[49].addBuilding("J9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[49].addBuilding("K9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[49].addBuilding("K10", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[49].addBuilding("L9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[49].addBuilding("Q7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[49].addBuilding("P6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[49].addBuilding("P7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[49].addBuilding("Q9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[49].addBuilding("P9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		// board 51
		buildingList[51].addBuilding("B7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("C7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("C9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("C10", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("D4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("E4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("D5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("E5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("F4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("F7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("G7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("H6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("D8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("E9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("F9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("G9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("G10", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("H8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("H9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("J1", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("K1", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("J3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("K3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("K4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("J5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("K6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("L6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("M7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("K8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("L8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("L3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("M4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("N4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("O5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("L4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("M5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("N5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("O6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("N7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("O8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("P8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("N9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("O10", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("R4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("S4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("Q8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("R7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("S7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("R8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("S8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("T7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("S10", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("T9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("T5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("U5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("V4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("W4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("X3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("V6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("W6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("X5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("Y5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("W7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("X6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("X7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("U9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("V8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("W9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("V9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("Y7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("Y8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("Z6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("AA7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("AA8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("Z8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("Z5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("AA6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("BB6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("AA5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("BB5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("BB4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("CC5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("AA2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("BB1", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("CC1", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("BB2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("CC2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("CC3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("DD3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("EE3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("FF3", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("BB7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("CC8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("DD8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("DD5", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("DD6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("EE10", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("FF9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		buildingList[51].addBuilding("E1", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("F1", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("F2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("G1", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("E7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("F6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("G6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("G4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("H4", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("J7", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("P1", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("Q9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("X9", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("Y6", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("X2", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("Z1", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);
		buildingList[51].addBuilding("EE8", terrainList[Terrain.STONE_BUILDING_1_LEVEL]);

		buildingList[51].addBuilding("B2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("C2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("C3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("D1", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("D2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("E2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("G2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("H1", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("H2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("G4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("H4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("M2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("N1", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("N2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("O2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("O3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("P4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("P5", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("P6", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("Q6", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("R2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("R3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("S2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("S3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("T2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("T3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("U2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("U3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("V2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("V3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("Z3", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("Z4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("AA4", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("DD1", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("DD2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);
		buildingList[51].addBuilding("EE2", terrainList[Terrain.STONE_BUILDING_2_LEVEL]);

		// create the color mappings
		int index = 0;

//*********		Ignore the following:
//		/* L1Winter */		colorMap[index++] = new VASLColorMap(new Color(244,237,216), terrainList[Terrain.OPEN_GROUND], 1);
//		/* L2Winter */		colorMap[index++] = new VASLColorMap(new Color(232,215,187), terrainList[Terrain.OPEN_GROUND], 2);
//		/* L3Winter */		colorMap[index++] = new VASLColorMap(new Color(212,180,161), terrainList[Terrain.OPEN_GROUND], 3);
//		/* L4Winter */		colorMap[index++] = new VASLColorMap(new Color(205,156,131), terrainList[Terrain.OPEN_GROUND], 4);
//		/* L_1Winter */		colorMap[index++] = new VASLColorMap(new Color(237,255,213), terrainList[Terrain.OPEN_GROUND], -1);
//		/* PavedRdWinter */	colorMap[index++] = new VASLColorMap(new Color(235,235,235), terrainList[Terrain.PAVED_ROAD]);
//		/* DirtRdWinter */	colorMap[index++] = new VASLColorMap(new Color(232,227,194), terrainList[Terrain.DIRT_ROAD]);
//		/* WoodRowhouseBar */	colorMap[index++] = new VASLColorMap(new Color(2,0,0), terrainList[Terrain.ROWHOUSE_WALL]);
//		/* WoodRowBarWinter */	colorMap[index++] = new VASLColorMap(new Color(72,70,70), terrainList[Terrain.ROWHOUSE_WALL]);
//		/* StoneRowhouseBar */	colorMap[index++] = new VASLColorMap(new Color(1,0,0), terrainList[Terrain.ROWHOUSE_WALL]);
//		/* StoneRowBarWinter */	colorMap[index++] = new VASLColorMap(new Color(71,70,70), terrainList[Terrain.ROWHOUSE_WALL]);
//		/* WoodBldg1Winter */	colorMap[index++] = new VASLColorMap(new Color(213,182,144), terrainList[Terrain.WOODEN_BUILDING]);
//		/* WoodBldg2Winter */	colorMap[index++] = new VASLColorMap(new Color(206,170,126), terrainList[Terrain.WOODEN_BUILDING]);
//		/* WoodBldg3Winter */	colorMap[index++] = new VASLColorMap(new Color(199,158,107), terrainList[Terrain.WOODEN_BUILDING]);
//		/* WoodBldg4Winter */	colorMap[index++] = new VASLColorMap(new Color(192,145,88), terrainList[Terrain.WOODEN_BUILDING]);
//		/* WoodBldg5Winter */	colorMap[index++] = new VASLColorMap(new Color(185,133,70), terrainList[Terrain.WOODEN_BUILDING]);
//		/* WoodBldg6Winter */	colorMap[index++] = new VASLColorMap(new Color(167,120,63), terrainList[Terrain.WOODEN_BUILDING]);
//		/* WoodBldg7Winter */	colorMap[index++] = new VASLColorMap(new Color(148,107,56), terrainList[Terrain.WOODEN_BUILDING]);
//		/* StoneBldg1Winter */	colorMap[index++] = new VASLColorMap(new Color(175,175,175), terrainList[Terrain.STONE_BUILDING]);
//		/* StoneBldg2Winter */	colorMap[index++] = new VASLColorMap(new Color(165,165,165), terrainList[Terrain.STONE_BUILDING]);
//		/* StoneBldg3Winter */	colorMap[index++] = new VASLColorMap(new Color(125,125,125), terrainList[Terrain.STONE_BUILDING]);
//		/* StoneBldg4Winter */	colorMap[index++] = new VASLColorMap(new Color(153,153,153), terrainList[Terrain.STONE_BUILDING]);
//		/* StoneBldg5Winter */	colorMap[index++] = new VASLColorMap(new Color(145,145,145), terrainList[Terrain.STONE_BUILDING]);
//		/* StoneBldg6Winter */	colorMap[index++] = new VASLColorMap(new Color(131,131,131), terrainList[Terrain.STONE_BUILDING]);
//		/* StoneBldg7Winter */	colorMap[index++] = new VASLColorMap(new Color(110,110,110), terrainList[Terrain.STONE_BUILDING]);
//		/* WoodsGreenWinter */	colorMap[index++] = new VASLColorMap(new Color(200,230,200), terrainList[Terrain.WOODS]);
//		/* WoodsBlackWinter */	colorMap[index++] = new VASLColorMap(new Color(76,76,76), terrainList[Terrain.WOODS]);
//		/* HedgeGreen1Winter */	colorMap[index++] = new VASLColorMap(new Color(180,220,180), terrainList[Terrain.HEDGE]);
//		/* HedgeGreen2Winter */	colorMap[index++] = new VASLColorMap(new Color(150,200,150), terrainList[Terrain.HEDGE]);
//		/* WinterCliff */	colorMap[index++] = new VASLColorMap(new Color(131,105,100), terrainList[Terrain.CLIFF]);
//		/* BrushWinter */ 	colorMap[index++] = new VASLColorMap(new Color(220,235,220), terrainList[Terrain.BRUSH]);
//		/* BrushIndicator */ 	colorMap[index++] = new VASLColorMap(new Color(67,63,27), terrainList[Terrain.BRUSH]);
//		/* DryStream1 */ 		colorMap[index++] = new VASLColorMap(new Color(254,255,255), terrainList[Terrain.SHALLOW_STREAM], -1);
//		/* DryStream2 */ 		colorMap[index++] = new VASLColorMap(new Color(164,111,8), terrainList[Terrain.SHALLOW_STREAM], -1);
//		/* DesertL0 */		colorMap[index++] = new VASLColorMap(new Color(251,223,147), terrainList[Terrain.OPEN_GROUND], 1);
//		/* Hammada */		colorMap[index++] = new VASLColorMap(new Color(59,55,19), terrainList[Terrain.HAMMADA], 0);
//		/* Scrub1 */	 	colorMap[index++] = new VASLColorMap(new Color(119,67,0), terrainList[Terrain.SCRUB], 0);
//		/* Scrub2 */	 	colorMap[index++] = new VASLColorMap(new Color(167,107,11), terrainList[Terrain.SCRUB], 0);
//		/* OrchDesert */	 	colorMap[index++] = new VASLColorMap(new Color(155,171,0), terrainList[Terrain.ORCHARD], 0);
//		/* BrushDesert */	 	colorMap[index++] = new VASLColorMap(new Color(179,179,47), terrainList[Terrain.BRUSH], 0);
//		/* Wadi1 */			colorMap[index++] = new VASLColorMap(new Color(231,167,0), terrainList[Terrain.WADI], -1);
//		/* Wadi2 */			colorMap[index++] = new VASLColorMap(new Color(191,111,7), terrainList[Terrain.WADI], -1);
//		/* Wadi3 */		 	colorMap[index++] = new VASLColorMap(new Color(151,99,43), terrainList[Terrain.WADI], -1);
//		/* MudflatL0 */ 		colorMap[index++] = new VASLColorMap(new Color(127,91,41), terrainList[Terrain.MARSH], 0);
//		/* MudflatL_1 */ 		colorMap[index++] = new VASLColorMap(new Color(127,90,41), terrainList[Terrain.MARSH], 0);
//		/* VineyardGreen */	colorMap[index++] = new VASLColorMap(new Color(49,163,50), terrainList[Terrain.VINEYARD]);
//		/* Ice */ 			colorMap[index++] = new VASLColorMap(new Color(204,255,255), terrainList[Terrain.ICE], -1);
//		/* Shellholes */		colorMap[index++] = new VASLColorMap(new Color(206,175,83), terrainList[Terrain.SHELL_HOLES]);
//*********

		/* Level_2 */		colorMap[index++] = new VASLColorMap(new Color(112,148,51), terrainList[Terrain.OPEN_GROUND], -2);
		/* Level_1 */		colorMap[index++] = new VASLColorMap(new Color(107,183,67), terrainList[Terrain.OPEN_GROUND], -1);
		/* Level0 */		colorMap[index++] = new VASLColorMap(new Color(204,240,153), terrainList[Terrain.OPEN_GROUND], 0);
		/* Level1 */		colorMap[index++] = new VASLColorMap(new Color(207,175,83), terrainList[Terrain.OPEN_GROUND], 1);
		/* Level2 */		colorMap[index++] = new VASLColorMap(new Color(167,107,11), terrainList[Terrain.OPEN_GROUND], 2);
		/* Level3 */		colorMap[index++] = new VASLColorMap(new Color(130,75,0), terrainList[Terrain.OPEN_GROUND], 3);
		/* Level4 */		colorMap[index++] = new VASLColorMap(new Color(89,65,33), terrainList[Terrain.OPEN_GROUND], 4);
		/* Level5 */		colorMap[index++] = new VASLColorMap(new Color(111,101,71), terrainList[Terrain.OPEN_GROUND], 5);
		/* Level6 */		colorMap[index++] = new VASLColorMap(new Color(144,134,105), terrainList[Terrain.OPEN_GROUND], 6);

		/* WoodsGreen */		colorMap[index++] = new VASLColorMap(new Color(47,131,47), terrainList[Terrain.WOODS]);
		/* WoodsBlack */		colorMap[index++] = new VASLColorMap(new Color(67,63,27), terrainList[Terrain.WOODS]);

		/* WoodBldg1 */		colorMap[index++] = new VASLColorMap(new Color(163,115,63), terrainList[Terrain.WOODEN_BUILDING]);
		/* WoodBldg2 */		colorMap[index++] = new VASLColorMap(new Color(163,111,7), terrainList[Terrain.WOODEN_BUILDING]);
		/* WoodBldg3 */		colorMap[index++] = new VASLColorMap(new Color(151,99,43), terrainList[Terrain.WOODEN_BUILDING]);
		/* WoodBldg4 */		colorMap[index++] = new VASLColorMap(new Color(147,95,35), terrainList[Terrain.WOODEN_BUILDING]);
		/* WoodBldg5 */		colorMap[index++] = new VASLColorMap(new Color(120,67,0), terrainList[Terrain.WOODEN_BUILDING]);
		/* WoodBldg6 */		colorMap[index++] = new VASLColorMap(new Color(111,55,15), terrainList[Terrain.WOODEN_BUILDING]);
		/* WoodBldg7 */		colorMap[index++] = new VASLColorMap(new Color(59,55,19), terrainList[Terrain.WOODEN_BUILDING]);
		/* WoodStairwell */	colorMap[index++] = new VASLColorMap(new Color(250,255,255), terrainList[Terrain.WOODEN_BUILDING]);

		/* StoneBldg1 */		colorMap[index++] = new VASLColorMap(new Color(155,155,155), terrainList[Terrain.STONE_BUILDING]);
		/* StoneBldg2 */		colorMap[index++] = new VASLColorMap(new Color(135,135,135), terrainList[Terrain.STONE_BUILDING]);
		/* StoneBldg3 */		colorMap[index++] = new VASLColorMap(new Color(125,125,125), terrainList[Terrain.STONE_BUILDING]);
		/* StoneBldg4 */		colorMap[index++] = new VASLColorMap(new Color(115,115,115), terrainList[Terrain.STONE_BUILDING]);
		/* StoneBldg5 */		colorMap[index++] = new VASLColorMap(new Color(103,103,103), terrainList[Terrain.STONE_BUILDING]);
		/* StoneBldg6 */		colorMap[index++] = new VASLColorMap(new Color(91,91,91), terrainList[Terrain.STONE_BUILDING]);
		/* StoneBldg7 */		colorMap[index++] = new VASLColorMap(new Color(70,70,70), terrainList[Terrain.STONE_BUILDING]);
		/* StoneStairwell */	colorMap[index++] = new VASLColorMap(new Color(251,255,255), terrainList[Terrain.STONE_BUILDING]);

		/* GrainL_2 */		colorMap[index++] = new VASLColorMap(new Color(255,255,125), terrainList[Terrain.GRAIN], -2);
		/* GrainL_1 */		colorMap[index++] = new VASLColorMap(new Color(255,255,126), terrainList[Terrain.GRAIN], -1);
		/* Grain0 */		colorMap[index++] = new VASLColorMap(new Color(255,255,127), terrainList[Terrain.GRAIN], 0);
		/* Grain1 */		colorMap[index++] = new VASLColorMap(new Color(255,255,128), terrainList[Terrain.GRAIN], 1);
		/* Grain2 */		colorMap[index++] = new VASLColorMap(new Color(255,255,129), terrainList[Terrain.GRAIN], 2);
		/* Grain3 */		colorMap[index++] = new VASLColorMap(new Color(255,255,130), terrainList[Terrain.GRAIN], 3);
		/* Grain4 */		colorMap[index++] = new VASLColorMap(new Color(255,255,131), terrainList[Terrain.GRAIN], 4);
		/* Grain5 */		colorMap[index++] = new VASLColorMap(new Color(255,255,132), terrainList[Terrain.GRAIN], 5);
		/* Grain6 */		colorMap[index++] = new VASLColorMap(new Color(255,255,133), terrainList[Terrain.GRAIN], 6);
		/* PlowedFields */	colorMap[index++] = new VASLColorMap(new Color(230,204,143), terrainList[Terrain.GRAIN], 6);

		/* HedgeGreen1 */		colorMap[index++] = new VASLColorMap(new Color(51,163,50), terrainList[Terrain.HEDGE]);
		/* HedgeGreen2 */		colorMap[index++] = new VASLColorMap(new Color(46,131,47), terrainList[Terrain.HEDGE]);

		/* WallBrown */			colorMap[index++] = new VASLColorMap(new Color(152,99,43), terrainList[Terrain.WALL]);
		/* WallGrey1 */			colorMap[index++] = new VASLColorMap(new Color(136,135,135), terrainList[Terrain.WALL]);
		/* WallGrey2 */			colorMap[index++] = new VASLColorMap(new Color(128,127,127), terrainList[Terrain.WALL]);
		/* WallGrey3 */			colorMap[index++] = new VASLColorMap(new Color(92,91,91), terrainList[Terrain.WALL]);

		/* FurrowsL_2 */		colorMap[index++] = new VASLColorMap(new Color(254,255,125), terrainList[Terrain.GRAIN], -2);
		/* FurrowsL_1 */		colorMap[index++] = new VASLColorMap(new Color(254,255,126), terrainList[Terrain.GRAIN], -1);
		/* FurrowsL0 */		colorMap[index++] = new VASLColorMap(new Color(254,255,127), terrainList[Terrain.GRAIN], 0);
		/* FurrowsL1 */		colorMap[index++] = new VASLColorMap(new Color(254,255,128), terrainList[Terrain.GRAIN], 1);
		/* FurrowsL2 */		colorMap[index++] = new VASLColorMap(new Color(254,255,129), terrainList[Terrain.GRAIN], 2);
		/* FurrowsL3 */		colorMap[index++] = new VASLColorMap(new Color(254,255,130), terrainList[Terrain.GRAIN], 3);
		/* FurrowsL4 */		colorMap[index++] = new VASLColorMap(new Color(254,255,131), terrainList[Terrain.GRAIN], 4);
		/* FurrowsL5 */		colorMap[index++] = new VASLColorMap(new Color(254,255,132), terrainList[Terrain.GRAIN], 5);
		/* FurrowsL6 */		colorMap[index++] = new VASLColorMap(new Color(254,255,133), terrainList[Terrain.GRAIN], 6);

		/* PavedRdL_2 */		colorMap[index++] = new VASLColorMap(new Color(212,215,215), terrainList[Terrain.PAVED_ROAD], -2);
		/* PavedRdL_1 */		colorMap[index++] = new VASLColorMap(new Color(214,215,215), terrainList[Terrain.PAVED_ROAD], -1);
		/* PavedRdL0 */		colorMap[index++] = new VASLColorMap(new Color(215,215,215), terrainList[Terrain.PAVED_ROAD], 0);
		/* PavedRdL1 */		colorMap[index++] = new VASLColorMap(new Color(216,215,215), terrainList[Terrain.PAVED_ROAD], 1);
		/* PavedRdL2 */		colorMap[index++] = new VASLColorMap(new Color(217,215,215), terrainList[Terrain.PAVED_ROAD], 2);
		/* PavedRdL3 */		colorMap[index++] = new VASLColorMap(new Color(218,215,215), terrainList[Terrain.PAVED_ROAD], 3);
		/* PavedRdL4 */		colorMap[index++] = new VASLColorMap(new Color(219,215,215), terrainList[Terrain.PAVED_ROAD], 4);
		/* PavedRdL5 */		colorMap[index++] = new VASLColorMap(new Color(220,215,215), terrainList[Terrain.PAVED_ROAD], 5);
		/* PavedRdL6 */		colorMap[index++] = new VASLColorMap(new Color(221,215,215), terrainList[Terrain.PAVED_ROAD], 6);

		/* DirtRdL_1 */		colorMap[index++] = new VASLColorMap(new Color(228,227,143), terrainList[Terrain.DIRT_ROAD], -1);
		/* DirtRdL_2 */		colorMap[index++] = new VASLColorMap(new Color(230,227,143), terrainList[Terrain.DIRT_ROAD], -2);
		/* DirtRdL0 */		colorMap[index++] = new VASLColorMap(new Color(231,227,143), terrainList[Terrain.DIRT_ROAD], 0);
		/* DirtRdL1 */		colorMap[index++] = new VASLColorMap(new Color(232,227,143), terrainList[Terrain.DIRT_ROAD], 1);
		/* DirtRdL2 */		colorMap[index++] = new VASLColorMap(new Color(233,227,143), terrainList[Terrain.DIRT_ROAD], 2);
		/* DirtRdL3 */		colorMap[index++] = new VASLColorMap(new Color(234,227,143), terrainList[Terrain.DIRT_ROAD], 3);
		/* DirtRdL4 */		colorMap[index++] = new VASLColorMap(new Color(235,227,143), terrainList[Terrain.DIRT_ROAD], 4);
		/* DirtRdL5 */		colorMap[index++] = new VASLColorMap(new Color(236,227,143), terrainList[Terrain.DIRT_ROAD], 5);
		/* DirtRdL6 */		colorMap[index++] = new VASLColorMap(new Color(237,227,143), terrainList[Terrain.DIRT_ROAD], 6);

		/* SunkRoad1 */		colorMap[index++] = new VASLColorMap(new Color(113,55,15), terrainList[Terrain.SUNKEN_ROAD], -1);
		/* SunkRoad2 */		colorMap[index++] = new VASLColorMap(new Color(168,107,11), terrainList[Terrain.SUNKEN_ROAD], -1);

		/* BrushL_2 */		colorMap[index++] = new VASLColorMap(new Color(169,224,104), terrainList[Terrain.BRUSH], -2);
		/* BrushL_1 */		colorMap[index++] = new VASLColorMap(new Color(170,224,104), terrainList[Terrain.BRUSH], -1);
		/* BrushL0 */		colorMap[index++] = new VASLColorMap(new Color(171,224,104), terrainList[Terrain.BRUSH], 0);
		/* BrushL1 */		colorMap[index++] = new VASLColorMap(new Color(172,224,104), terrainList[Terrain.BRUSH], 1);
		/* BrushL2 */		colorMap[index++] = new VASLColorMap(new Color(173,224,104), terrainList[Terrain.BRUSH], 2);
		/* BrushL3 */		colorMap[index++] = new VASLColorMap(new Color(174,224,104), terrainList[Terrain.BRUSH], 3);
		/* BrushL4 */		colorMap[index++] = new VASLColorMap(new Color(175,224,104), terrainList[Terrain.BRUSH], 4);
		/* BrushL5 */		colorMap[index++] = new VASLColorMap(new Color(176,224,104), terrainList[Terrain.BRUSH], 5);
		/* BrushL6 */		colorMap[index++] = new VASLColorMap(new Color(177,224,104), terrainList[Terrain.BRUSH], 6);

		/* BrushBlackL_2 */ 	colorMap[index++] = new VASLColorMap(new Color(67,62,27), terrainList[Terrain.BRUSH], -2);
		/* BrushBlackL_1 */ 	colorMap[index++] = new VASLColorMap(new Color(67,64,27), terrainList[Terrain.BRUSH], -1);
		/* BrushBlackL0 */ 	colorMap[index++] = new VASLColorMap(new Color(67,65,27), terrainList[Terrain.BRUSH], 0);
		/* BrushBlackL1 */ 	colorMap[index++] = new VASLColorMap(new Color(67,66,27), terrainList[Terrain.BRUSH], 1);
		/* BrushBlackL2 */ 	colorMap[index++] = new VASLColorMap(new Color(67,67,27), terrainList[Terrain.BRUSH], 2);
		/* BrushBlackL3 */ 	colorMap[index++] = new VASLColorMap(new Color(67,68,27), terrainList[Terrain.BRUSH], 3);
		/* BrushBlackL4 */ 	colorMap[index++] = new VASLColorMap(new Color(67,69,27), terrainList[Terrain.BRUSH], 4);
		/* BrushBlackL5 */ 	colorMap[index++] = new VASLColorMap(new Color(67,70,27), terrainList[Terrain.BRUSH], 5);
		/* BrushBlackL6 */ 	colorMap[index++] = new VASLColorMap(new Color(67,71,27), terrainList[Terrain.BRUSH], 6);

		/* OrchL_2 */			colorMap[index++] = new VASLColorMap(new Color(48,163,51), terrainList[Terrain.ORCHARD], -2);
		/* OrchL_1 */			colorMap[index++] = new VASLColorMap(new Color(50,163,51), terrainList[Terrain.ORCHARD], -1);
		/* OrchL0 */			colorMap[index++] = new VASLColorMap(new Color(51,163,51), terrainList[Terrain.ORCHARD], 0);
		/* OrchL1 */			colorMap[index++] = new VASLColorMap(new Color(52,163,51), terrainList[Terrain.ORCHARD], 1);
		/* OrchL2 */			colorMap[index++] = new VASLColorMap(new Color(53,163,51), terrainList[Terrain.ORCHARD], 2);
		/* OrchL3 */			colorMap[index++] = new VASLColorMap(new Color(54,163,51), terrainList[Terrain.ORCHARD], 3);
		/* OrchL4 */			colorMap[index++] = new VASLColorMap(new Color(55,163,51), terrainList[Terrain.ORCHARD], 4);
		/* OrchL5 */			colorMap[index++] = new VASLColorMap(new Color(56,163,51), terrainList[Terrain.ORCHARD], 5);
		/* OrchL6 */			colorMap[index++] = new VASLColorMap(new Color(57,163,51), terrainList[Terrain.ORCHARD], 6);
		/* OrchTrees */			colorMap[index++] = new VASLColorMap(new Color(49,163,51), terrainList[Terrain.ORCHARD]);

		/* GraveyardPath */		colorMap[index++] = new VASLColorMap(new Color(69,64,27), terrainList[Terrain.GRAVE_YARD]);
		/* GraveyardWhite */		colorMap[index++] = new VASLColorMap(new Color(252,255,255), terrainList[Terrain.GRAVE_YARD]);
		/* GraveyardGreen */		colorMap[index++] = new VASLColorMap(new Color(48,131,47), terrainList[Terrain.GRAVE_YARD]);

		/* CragGrey */		colorMap[index++] = new VASLColorMap(new Color(215,215,214), terrainList[Terrain.CRAGS]);
		/* CragBlack */		colorMap[index++] = new VASLColorMap(new Color(68,63,27), terrainList[Terrain.CRAGS]);

		/* Cliff */			colorMap[index++] = new VASLColorMap(new Color(61,55,19), terrainList[Terrain.CLIFF]);
		/* GullyCliff */		colorMap[index++] = new VASLColorMap(new Color(58,55,19), terrainList[Terrain.CLIFF]);

		/* GullyGreen1 */ 	colorMap[index++] = new VASLColorMap(new Color(57,139,24), terrainList[Terrain.GULLY], -1);
		/* GullyGreen2 */ 	colorMap[index++] = new VASLColorMap(new Color(95,163,47), terrainList[Terrain.GULLY], -1);
		/* GullyOutline */ 	colorMap[index++] = new VASLColorMap(new Color(171,223,104), terrainList[Terrain.GULLY], -1);
		/* GullyBrown1 */ 	colorMap[index++] = new VASLColorMap(new Color(148,95,35), terrainList[Terrain.GULLY], -1);
		/* GullyBrown2 */ 	colorMap[index++] = new VASLColorMap(new Color(112,55,15), terrainList[Terrain.GULLY], -1);
		/* GullyBlack */ 		colorMap[index++] = new VASLColorMap(new Color(69,63,27), terrainList[Terrain.GULLY], -1);

		/* StreamGreen1 */ 	colorMap[index++] = new VASLColorMap(new Color(108,183,67), terrainList[Terrain.SHALLOW_STREAM], -1);
		/* StreamGreen2 */ 	colorMap[index++] = new VASLColorMap(new Color(171,224,103), terrainList[Terrain.SHALLOW_STREAM], -1);
		/* StreamGreen3 */ 	colorMap[index++] = new VASLColorMap(new Color(95,164,47), terrainList[Terrain.SHALLOW_STREAM], -1);
		/* StreamBrown */ 	colorMap[index++] = new VASLColorMap(new Color(148,96,35), terrainList[Terrain.SHALLOW_STREAM], -1);
		/* StreamWhite */ 	colorMap[index++] = new VASLColorMap(new Color(254,255,255), terrainList[Terrain.SHALLOW_STREAM], -1);
		/* StreamWater */ 	colorMap[index++] = new VASLColorMap(new Color(172,231,255), terrainList[Terrain.SHALLOW_STREAM], -1);

		/* Water */ 		colorMap[index++] = new VASLColorMap(new Color(171,231,255), terrainList[Terrain.WATER], -1);
		/* WaterWhite */ 	colorMap[index++] = new VASLColorMap(new Color(253,255,255), terrainList[Terrain.WATER], -1);
		/* PondWater */ 	colorMap[index++] = new VASLColorMap(new Color(173,231,255), terrainList[Terrain.WATER], -1);
		/* PondWhite */ 	colorMap[index++] = new VASLColorMap(new Color(249,255,255), terrainList[Terrain.WATER], -1);

		/* MarshGreenL1 */ 	colorMap[index++] = new VASLColorMap(new Color(48,99,35), terrainList[Terrain.MARSH], 1);
		/* MarshBrownL1 */ 	colorMap[index++] = new VASLColorMap(new Color(52,0,19), terrainList[Terrain.MARSH], 1);
		/* MarshGreenL0 */ 	colorMap[index++] = new VASLColorMap(new Color(47,99,35), terrainList[Terrain.MARSH], 0);
		/* MarshBrownL0 */ 	colorMap[index++] = new VASLColorMap(new Color(51,0,19), terrainList[Terrain.MARSH], 0);
		/* MarshGreenL_1 */ 	colorMap[index++] = new VASLColorMap(new Color(46,99,35), terrainList[Terrain.MARSH], -1);
		/* MarshBrownL_1 */ 	colorMap[index++] = new VASLColorMap(new Color(50,0,19), terrainList[Terrain.MARSH], -1);
		/* MarshGreenL_2 */ 	colorMap[index++] = new VASLColorMap(new Color(45,99,35), terrainList[Terrain.MARSH], -2);
		/* MarshBrownL_2 */ 	colorMap[index++] = new VASLColorMap(new Color(49,0,19), terrainList[Terrain.MARSH], -2);
		/* SepMarshGreenL0 */ 	colorMap[index++] = new VASLColorMap(new Color(47,98,35), terrainList[Terrain.MARSH], 0);
		/* SepMarshBrownL0 */ 	colorMap[index++] = new VASLColorMap(new Color(51,0,18), terrainList[Terrain.MARSH], 0);
		/* SepMarshGreenL_1 */ 	colorMap[index++] = new VASLColorMap(new Color(46,98,35), terrainList[Terrain.MARSH], -1);
		/* SepMarshBrownL_1 */ 	colorMap[index++] = new VASLColorMap(new Color(50,0,18), terrainList[Terrain.MARSH], -1);
		/* SepMarshGreenL_2 */ 	colorMap[index++] = new VASLColorMap(new Color(45,98,35), terrainList[Terrain.MARSH], -2);
		/* SepMarshBrownL_2 */ 	colorMap[index++] = new VASLColorMap(new Color(49,0,18), terrainList[Terrain.MARSH], -2);
		/* SepMarshGreenL1 */ 	colorMap[index++] = new VASLColorMap(new Color(48,98,35), terrainList[Terrain.MARSH], 1);
		/* SepMarshBrownL1 */ 	colorMap[index++] = new VASLColorMap(new Color(52,0,18), terrainList[Terrain.MARSH], 1);

		/* SwampGreenL1 */ 	colorMap[index++] = new VASLColorMap(new Color(48,99,36), terrainList[Terrain.MARSH], 1);
		/* SwampBrownL1 */ 	colorMap[index++] = new VASLColorMap(new Color(52,1,19), terrainList[Terrain.MARSH], 1);
		/* SwampGreenL0 */ 	colorMap[index++] = new VASLColorMap(new Color(47,99,36), terrainList[Terrain.MARSH], 0);
		/* SwampBrownL0 */ 	colorMap[index++] = new VASLColorMap(new Color(51,1,19), terrainList[Terrain.MARSH], 0);
		/* SwampGreenL_1 */ 	colorMap[index++] = new VASLColorMap(new Color(46,99,36), terrainList[Terrain.MARSH], -1);
		/* SwampBrownL_1 */ 	colorMap[index++] = new VASLColorMap(new Color(50,1,19), terrainList[Terrain.MARSH], -1);
		/* SwampGreenL_2 */ 	colorMap[index++] = new VASLColorMap(new Color(45,99,36), terrainList[Terrain.MARSH], -2);
		/* SwampBrownL_2 */ 	colorMap[index++] = new VASLColorMap(new Color(49,1,19), terrainList[Terrain.MARSH], -2);

		// Map bridges to open ground
		/* BridgeBrown */ 	colorMap[index++] = new VASLColorMap(new Color(153,99,43), terrainList[Terrain.OPEN_GROUND], 0);
		/* BridgeGrey1 */ 	colorMap[index++] = new VASLColorMap(new Color(129,127,127), terrainList[Terrain.OPEN_GROUND], 0);
		/* BridgeGrey2 */ 	colorMap[index++] = new VASLColorMap(new Color(93,91,91), terrainList[Terrain.OPEN_GROUND], 0);
		/* BridgeHighlight */ 	colorMap[index++] = new VASLColorMap(new Color(157,157,157), terrainList[Terrain.OPEN_GROUND], 0);
		/* DirtRdBridge */ 	colorMap[index++] = new VASLColorMap(new Color(213,215,215), terrainList[Terrain.OPEN_GROUND], 0);
		/* PavedRdBridge */ 	colorMap[index++] = new VASLColorMap(new Color(229,227,143), terrainList[Terrain.OPEN_GROUND], 0);

		/* PathBrown1 */ 		colorMap[index++] = new VASLColorMap(new Color(60,55,19), terrainList[Terrain.PATH]);
		/* PathBrown2 */ 		colorMap[index++] = new VASLColorMap(new Color(164,115,63), terrainList[Terrain.PATH]);
		/* PathBrown3 */ 		colorMap[index++] = new VASLColorMap(new Color(200,175,83), terrainList[Terrain.PATH]);

		/* RicePaddy */ 		colorMap[index++] = new VASLColorMap(new Color(205,175,84), terrainList[Terrain.RICE_PADDY_IRRIGATED]);
		/* RicePaddyIterior */ 	colorMap[index++] = new VASLColorMap(new Color(203,240,154), terrainList[Terrain.RICE_PADDY_IRRIGATED]);

		/* Lumberyard1 */       colorMap[index++] = new VASLColorMap(new Color(65,55,19), terrainList[Terrain.LUMBER_YARD]);
		/* Lumberyard2 */       colorMap[index++] = new VASLColorMap(new Color(169,115,63), terrainList[Terrain.LUMBER_YARD]);

		/* Shellhole1 */		colorMap[index++] = new VASLColorMap(new Color(66,55,19), terrainList[Terrain.SHELL_HOLES]);
		/* Shellhole2 */		colorMap[index++] = new VASLColorMap(new Color(170,115,63), terrainList[Terrain.SHELL_HOLES]);
		/* Shellhole3 */		colorMap[index++] = new VASLColorMap(new Color(206,175,83), terrainList[Terrain.SHELL_HOLES]);
	}

	public void setImage(BufferedImage i){

		// see if the image is the correct size
		if (i.getWidth() != map.getImageWidth()){

			MapBuilder.writeError("The image width is not the right size for the map");
			MapBuilder.writeError("The image width: " + i.getWidth());
			MapBuilder.writeError("The map width: " + map.getImageWidth());
			return;

		} else if (i.getHeight() != map.getImageHeight()){

			MapBuilder.writeError("The image height is not the right size for the map");
			MapBuilder.writeError("The image height: " + i.getHeight());
			MapBuilder.writeError("The map height: " + map.getImageHeight());
			return;
		}

		image = i;
	}

	public GameMap getMap() {return map;}

	// get the height value for a pixel color
	private int getHeight(int rbg){

		// boards that are all level 0 only or take too long
		if (currentBoard == 1  || currentBoard == 4  || currentBoard == 6  || currentBoard == 16 || currentBoard == 19 ||
		    currentBoard == 21 || currentBoard == 33 || currentBoard == 43 || currentBoard == 44 ||
			currentBoard == 52) {

		    return 0;
		}

		for (int x = 0; x < MAX_COLORS; x++){

			if (colorMap[x] == null) return VASLColorMap.NO_HEIGHT;

			if (colorMap[x].getColor().getRGB() == rbg) {

				return colorMap[x].getHeight();
			}
		}

		return VASLColorMap.NO_HEIGHT;
	}

	private int getNearestHeight(int x, int y){

		int min = 0;
		int max = 1;
		int height = VASLColorMap.NO_HEIGHT;
		int circumference = 50;

		// boards that use a reduced circumference (mostly level one with some level -1)
		if (currentBoard == 5  || currentBoard == 7  || currentBoard == 10 || currentBoard == 12 || currentBoard == 13 ||
		    currentBoard == 14 || currentBoard == 17 || currentBoard == 20 || currentBoard == 22 || currentBoard == 23 ||
		    currentBoard == 32 || currentBoard == 34 || currentBoard == 35 || currentBoard == 37 || currentBoard == 38 ||
		    currentBoard == 42 || currentBoard == 45 || currentBoard == 46 || currentBoard == 48 || currentBoard == 51) {

		    circumference = 5;
		}

		while (max <= circumference) {
			for (int i = x - max; i <= x + max; i++){
				for (int j = y - max; j <= y + max; j++){

					if( map.onMap(i, j) && Point.distance((double)x, (double)y, (double)i, (double)j) > min &&
						Point.distance((double)x, (double)y, (double)i, (double)j) <= max){

						height = map.getGridGroundLevel(i, j);

						// anything over 10 is unknown for purposes of nearest height
						if (height < 10 ) return height;
					}
				}
			}

			min++;
			max++;
		}

		return 0;
	}

	private Terrain getTerrain(int rbg){

		for (int x = 0; x < MAX_COLORS; x++){

			if (colorMap[x] == null) return VASLColorMap.NO_TERRAIN;

			if (colorMap[x].getColor().getRGB() == rbg) {

				return colorMap[x].getTerrain();
			}
		}

		return VASLColorMap.NO_TERRAIN;
	}

	private Terrain getNearestTerrain(int x, int y){

		int min = 0;
		int max = 1;
		int terrainType = 0;

		while (max <= 50 ) {
			for (int i = x - max; i <= x + max; i++){
				for (int j = y - max; j <= y + max; j++){

					if(	map.onMap(i, j) && Point.distance((double)x, (double)y, (double)i, (double)j) > min &&
						Point.distance((double)x, (double)y, (double)i, (double)j) <= max &&
						!map.isUnknownTerrain(i, j) &&
						 map.getRawTerrain(i, j) < TERRAIN_OFFSET){

						terrainType = map.getGridTerrain(i, j).getType();

						if (!terrainList[terrainType].isInherentTerrain()){
							return terrainList[terrainType];
						}
					}
				}
			}

			min++;
			max++;
		}

		return terrainList[Terrain.OPEN_GROUND];
	}

	public void convertMap(int board){

		// ensure the image is set
		if (image == null){

			MapBuilder.writeError("No image has been set");
			return;
		}

		// store the board number
		currentBoard = board;

		// add the known heights to the elevation grid
		int height;
		frame.setStatusBarText("Creating the elevation grid...  ");
		frame.paintImmediately();
		for (int x = 0; x < map.getImageWidth(); x++){

			for (int y = 0; y < map.getImageHeight(); y++){

				height = getHeight(image.getRGB(x,y));
				map.setGridGroundLevel(new Rectangle(x,y,1,1), null, height);
			}
		}

		// determine unknown heights in the elevation grid
		frame.setStatusBarText("Finding nearest height for pixels with an unknown elevation...");
		frame.paintImmediately();
		for (int x = 0; x < map.getImageWidth(); x++){
			for (int y = 0; y < map.getImageHeight(); y++){

				if (map.getGridGroundLevel(x, y) == VASLColorMap.NO_HEIGHT) {

					// code the height so it won't get picked up by its neighbors when doing nearest height
					map.setGridGroundLevel(new Rectangle(x,y,1,1), null, 50 + getNearestHeight(x,y));
				}
			}
		}

		// remove elevation coding
		for (int x = 0; x < map.getImageWidth(); x++){
			for (int y = 0; y < map.getImageHeight(); y++){

				height = map.getGridGroundLevel(x, y);
				if (height > 10) {

					map.setGridGroundLevel(new Rectangle(x,y,1,1), null, height - 50);
				}
			}
		}

		// set the hex base height
		frame.setStatusBarText("Setting the hex base height...");
		frame.paintImmediately();
		Hex h = null;
		Point p = null;
		for (int col = 0; col < map.getWidth(); col++) {
			for(int row = 0; row < map.getHeight() + (col%2); row++) {

				h = map.getHex(col, row);
				p = h.getCenterLocation().getLOSPoint();
				h.setBaseHeight(map.getGridGroundLevel((int) p.getX(), (int) p.getY()));
			}
		}

		// set inherent terrain in the hexgrid
		frame.setStatusBarText("Creating inherent terrain...");
		frame.paintImmediately();
		Terrain t = null;
		Location l = null;
		for (int x = 0; x < map.getImageWidth(); x++){
			for (int y = 0; y < map.getImageHeight(); y++){

				h = map.gridToHex(x, y);
				l = h.getCenterLocation();

				// use the bypass flag to remember if the inherent terrain for this hex has already been set
				if (!l.isBypassAllowed()) {

					t = getTerrain(image.getRGB(x,y));
					if (t != VASLColorMap.NO_TERRAIN && t.isInherentTerrain() &&
						h.nearestLocation(x,y).isCenterLocation() ){

						map.setGridTerrain((Shape) l.getHex().getExtendedHexBorder(), t);
						map.setHexTerrain((Shape) l.getHex().getExtendedHexBorder(), t);

						// mark this hex as done
						l.setBypassAllowed(true);
					}
				}
			}
		}

		// add the known terrain to the terrain grid
		frame.setStatusBarText("Creating the terrain grid...");
		frame.paintImmediately();
		for (int x = 0; x < map.getImageWidth(); x++){
			for (int y = 0; y < map.getImageHeight(); y++){

				t = getTerrain(image.getRGB(x,y));
				if (t == VASLColorMap.NO_TERRAIN && !map.gridToHex(x, y).getCenterLocation().getTerrain().isInherentTerrain()){

					// we don't know the terrain, so mark it in the map
					map.setGridTerrainUnknown(x, y);
				}
				else if (t != VASLColorMap.NO_TERRAIN &&
						!t.isInherentTerrain() &&
						// ignore open ground pixels in hexes containing inherent terrain
						!(t.getType() == Terrain.OPEN_GROUND &&
						 map.gridToHex(x,y).getCenterLocation().isBypassAllowed())){

					map.setGridTerrain(new Rectangle(x,y,1,1), t);
					// ignore hexside and degression terrain
					if (!t.isEdgeTerrain() && !t.isDepressionTerrain()){

						map.setHexTerrain(new Rectangle(x,y,1,1), t);
					}
				}
			}
		}

		// reset all of the bypass flags to no
		for (int col = 0; col < map.getWidth(); col++) {
			for(int row = 0; row < map.getHeight() + (col%2); row++) {

				map.getHex(col, row).getCenterLocation().setBypassAllowed(false);
			}
		}

		// determine unknown terrains in the terrain grid
		// save as a raw terrain value
		frame.setStatusBarText("Finding nearest terrain for pixels with an unknown terrain...");
		frame.paintImmediately();
		for (int x = 0; x < map.getImageWidth(); x++){
			for (int y = 0; y < map.getImageHeight(); y++){

				if (map.isUnknownTerrain(x, y)) {

					t = getNearestTerrain(x,y);
					map.setRawTerrain(x, y, t.getType() + TERRAIN_OFFSET);
				}
			}
		}
		// remove the terrain coding
		for (int x = 0; x < map.getImageWidth(); x++){
			for (int y = 0; y < map.getImageHeight(); y++){

				if (map.getRawTerrain(x, y) >= TERRAIN_OFFSET) {

					t = terrainList[map.getRawTerrain(x, y) - TERRAIN_OFFSET];
					map.setGridTerrain(new Rectangle(x,y,1,1), t);
					// ignore hexside and degression terrain
					if (!t.isEdgeTerrain() && !t.isDepressionTerrain()){

						map.setHexTerrain(new Rectangle(x,y,1,1), t);
					}
				}
			}
		}

		// set the hexside and depression terrains
		map.resetTerrain();

		// set the hexside bypass flags
		for (int col = 0; col < map.getWidth(); col++) {
			for(int row = 0; row < map.getHeight() + (col%2); row++) {

				map.getHex(col, row).setBypassFlags();
			}
		}

		// add upper level buildings
		if (buildingList[board].getCount() > 0) {

			frame.setStatusBarText("Adding upper level buildings...");
	    	frame.paintImmediately();

		    map.convertMultiLevelBuildings(buildingList[board]);
		}


		frame.setStatusBarText("Done");
		frame.paintImmediately();
	}
}
