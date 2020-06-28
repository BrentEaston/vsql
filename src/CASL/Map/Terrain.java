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
package CASL.Map;

import java.awt.Color;
import java.io.Serializable;

/**
 * Title:        Terrain.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class Terrain
	implements Serializable {

	// serial ID: version 1.0.0
	static final long serialVersionUID = 000100L;

	public  final static int HEIGHT_PER_LEVEL 	= 5;
	public  final static int HEIGHT_PER_HALF_LEVEL = 2;
	public  final static int EDGE_TERRAIN_WIDTH 	= 39;
	public  final static int EDGE_TERRAIN_HEIGHT	= 5;
	private	   static Terrain terrainList[];

	// private variables
	private String  name;
	private int	 type;
	private boolean LOSObstacle			= false;
	private boolean LOSHindrance		= false;
	private boolean lowerLOSObstacle	= false;
	private boolean lowerLOSHindrance	= false;
	private boolean halfLevelHeight		= false;
	private int	 height					= 0;
	private float   split				= (float) 0.0;
	private Color   mapColor			= new Color(0, 0, 0);

	public	String  getName()			   {return name;}
	public	void 	setName(String newName) {name = newName;}


	public boolean 	isHalfLevelHeight() {return halfLevelHeight;}
	public void 	setHalfLevelHeight(boolean newHalfLevelHeight) {
		halfLevelHeight = newHalfLevelHeight;
	}

	public boolean 	isLOSObstacle() {return LOSObstacle;}
	public void 	setLOSObstacle(boolean newLOSObstacle) {
		LOSObstacle = newLOSObstacle;
	}

	public boolean 	isLOSHindrance() {return LOSHindrance;}
	public void 	setLOSHindrance(boolean newLOSHindrance) {
		LOSHindrance = newLOSHindrance;
	}

	public boolean 	isLowerLOSObstacle() {return lowerLOSObstacle;}
	public void 	setLowerLOSObstacle(boolean newLowerLOSObstacle) {
		lowerLOSObstacle = newLowerLOSObstacle;
	}

	public boolean 	isLowerLOSHindrance() {return lowerLOSHindrance;}
	public void 	setLowerLOSHindrance(boolean newLowerLOSHindrance) {
		lowerLOSHindrance = newLowerLOSHindrance;
	}

	public	Color	getMapColor() {return mapColor;}
	public 	void 	setMapColor(Color newColor) {mapColor = newColor;}

	public  int		getHeight() {return height;}
	public void 	setHeight(int newHeight){height = newHeight;}

	public  int	 getType()			   {return type;}

	public  boolean hasSplit()			  {return split != 0;}

	// standard colors
	public final static Color VALLEY2_COLOR		= new Color(82, 138, 52);
	public final static Color VALLEY1_COLOR		= new Color(107, 183, 67);
	public final static Color OPEN_GROUND_COLOR	= new Color(204, 240, 153);
	public final static Color LEVEL1_COLOR		= new Color(207, 175, 83);
	public final static Color LEVEL2_COLOR		= new Color(167, 107, 11);
	public final static Color LEVEL3_COLOR		= new Color(119, 67, 0);
	public final static Color LEVEL4_COLOR		= new Color(99, 55, 0);
	public final static Color LEVEL5_COLOR		= new Color(79, 45, 0);
	public final static Color LEVEL6_COLOR		= new Color(59, 35, 0);
	public final static Color LEVEL7_COLOR		= new Color(60, 25, 0);
	public final static Color LEVEL8_COLOR		= new Color(50, 20, 0);
	public final static Color LEVEL9_COLOR		= new Color(40, 15, 0);
	public final static Color LEVEL10_COLOR		= new Color(30, 10, 0);
	public final static Color WATER_EDGE_COLOR	= new Color(253, 255, 255);
	public final static Color GULLY_INTERIOR_COLOR	= new Color(148, 95, 35);

	public final static int UNKNOWN		= -1;

	// type definitions
	// ETO
	public final static int OPEN_GROUND		= 0;
	public final static int PLOWED_FIELD	= 1;
	public final static int ROOFTOP			= 2;
	public final static int SNOW			= 3;
	public final static int DEEP_SNOW		= 4;
	public final static int ICE				= 5;
	public final static int MUD				= 6;
	public final static int MUD_FLATS		= 7;

	public final static int WATER			= 8;
	public final static int SHALLOW_WATER	= 9;
	public final static int RIVER			= 10;
	public final static int SHALLOW_RIVER	= 11;
	public final static int FORD			= 12;
	public final static int CANAL			= 13;
	public final static int MARSH			= 14;

	public final static int SHELL_HOLES		= 15;

	public final static int FOX_HOLES		= 20;
	public final static int TRENCH			= 21;
	public final static int ANTITANK_TRENCH	= 22;
	public final static int PILLBOX			= 23;

	public final static int GRAIN			= 24;
	public final static int BRUSH			= 25;
	public final static int CRAGS			= 26;
	public final static int DEBRIS			= 27;
	public final static int GRAVE_YARD		= 28;
	public final static int LUMBER_YARD		= 29;

	public final static int GULLY				= 30;
	public final static int DRY_STREAM			= 31;
	public final static int SHALLOW_STREAM		= 32;
	public final static int DEEP_STREAM			= 33;
	public final static int FLOODED_STREAM		= 34;
	public final static int IRREGATION_DITCH	= 35;

	public final static int STONE_BUILDING					= 40;
	public final static int STONE_BUILDING_1_LEVEL			= 41;
	public final static int STONE_BUILDING_2_LEVEL			= 42;
	public final static int STONE_BUILDING_3_LEVEL			= 43;
	public final static int STONE_BUILDING_4_LEVEL			= 44;
	public final static int STONE_FACTORY_WALL_1_5_LEVEL	= 45;
	public final static int STONE_FACTORY_WALL_2_5_LEVEL	= 46;
	public final static int STONE_FACTORY_1_5_LEVEL			= 47;
	public final static int STONE_FACTORY_2_5_LEVEL			= 48;
	public final static int STONE_MARKET_PLACE				= 49;

	public final static int WOODEN_BUILDING					= 50;
	public final static int WOODEN_BUILDING_1_LEVEL			= 51;
	public final static int WOODEN_BUILDING_2_LEVEL			= 52;
	public final static int WOODEN_BUILDING_3_LEVEL			= 53;
	public final static int WOODEN_BUILDING_4_LEVEL			= 54;
	public final static int WOODEN_FACTORY_WALL_1_5_LEVEL	= 55;
	public final static int WOODEN_FACTORY_WALL_2_5_LEVEL	= 56;
	public final static int WOODEN_FACTORY_1_5_LEVEL		= 57;
	public final static int WOODEN_FACTORY_2_5_LEVEL		= 58;
	public final static int WOODEN_MARKET_PLACE				= 59;

	public final static int WOODS							= 60;
	public final static int FOREST							= 61;
	public final static int PINE_WOODS						= 62;
	public final static int ORCHARD							= 63;
	public final static int ORCHARD_OUT_OF_SEASON			= 64;

	public final static int DIRT_ROAD			= 65;
	public final static int PAVED_ROAD			= 66;
	public final static int ELEVATED_ROAD		= 67;
	public final static int SUNKEN_ROAD			= 68;
	public final static int RUNWAY				= 69;
	public final static int PATH				= 70;
	public final static int RAILROAD_EMBANKMENT	= 71;

	public final static int WALL					= 72;
	public final static int HEDGE					= 73;
	public final static int BOCAGE					= 74;
	public final static int CLIFF					= 75;
	public final static int ROWHOUSE_WALL			= 76;
	public final static int ROWHOUSE_WALL_1_LEVEL	= 77;
	public final static int ROWHOUSE_WALL_2_LEVEL	= 78;
	public final static int ROWHOUSE_WALL_3_LEVEL	= 79;
	public final static int ROWHOUSE_WALL_4_LEVEL	= 80;
	public final static int ROAD_BLOCK				= 81;

	public final static int SINGLE_HEX_STONE_BRIDGE   	= 82;
	public final static int SINGLE_HEX_WOODEN_BRIDGE	= 83;
	public final static int STONE_BRIDGE   	            = 84;
	public final static int WOODEN_BRIDGE	            = 85;
	public final static int PONTOON_BRIDGE              = 86;
	public final static int FOOT_BRIDGE  	            = 87;

	public final static int STONE_RUBBLE   	= 90;
	public final static int WOODEN_RUBBLE	= 91;

	public final static int SEWER			= 95;
	public final static int TUNNEL			= 96;
	public final static int CAVE			= 97;

	// desert
	public final static int SCRUB			= 100;
	public final static int HAMMADA			= 101;
	public final static int DEIR			= 102;
	public final static int DEIR_LIP	   	= 103;
	public final static int WADI		   	= 104;
	public final static int HILLOCK			= 105;
	public final static int HILLOCK_SUMMIT	= 106;
	public final static int SAND			= 107;
	public final static int SAND_DUNE		= 108;
	public final static int DUNE_CREST_LOW	= 109;
	public final static int DUNE_CREST_HIGH	= 110;
	public final static int SANGAR			= 111;
	public final static int TRACKS			= 112;
	public final static int MAUSOLEUM		= 113;
	public final static int BEDOUIN_CAMP	= 114;
	public final static int BUILDING_CLUSTER= 115;
	public final static int CACTUS_HEDGE	= 116;
	public final static int CACTUS_PATCH	= 117;
	public final static int OLIVE_GROVE		= 118;
	public final static int VINEYARD		= 119;
	public final static int BROKEN_GROUND	= 120;

	// PTO
	public final static int TEMPLE			= 125;
	public final static int LIGHT_JUNGLE  	= 126;
	public final static int DENSE_JUNGLE	= 127;
	public final static int BAMBOO			= 128;
	public final static int PALM_TREES		= 129;
	public final static int HUTS			= 130;
	public final static int COLLAPSED_HUTS	= 131;
	public final static int KUNAI			= 132;
	public final static int SWAMP			= 133;

	public final static int RICE_PADDY_DRAINED		= 135;
	public final static int RICE_PADDY_IRRIGATED	= 136;
	public final static int RICE_PADDY_IN_SEASON	= 137;
	public final static int RICE_PADDY_BANK			= 138;
	public final static int PANJI					= 139;

	public final static int OCEAN					= 140;
	public final static int SHALLOW_OCEAN			= 141;
	public final static int EFFLUENT				= 142;
	public final static int SOFT_SAND				= 143;
	public final static int BEACH_SLIGHT_SLOPE		= 144;
	public final static int BEACH_MODERATE_SLOPE	= 145;
	public final static int BEACH_STEEP_SLOPE		= 146;
	public final static int EXPOSED_REEF			= 147;
	public final static int SUBMERGED_REEF			= 148;

	public final static int NARROW_STREET			= 150;
	public final static int STEEPLE					= 151;
	public final static int CELLAR					= 152;

	public final static int GROUND_LEVEL_RAILROAD	= 153;
	public final static int EMBANKMENT_RAILROAD		= 154;
	public final static int ELEVATED_RAILROAD		= 155;
	public final static int SUNKEN_RAILROAD			= 156;

	public final static int TOWER_1_LEVEL_HINDRANCE	= 160;
	public final static int TOWER_2_LEVEL_HINDRANCE	= 161;
	public final static int TOWER_3_LEVEL_HINDRANCE	= 162;
	public final static int TOWER_1_LEVEL_OBSTACLE	= 163;
	public final static int TOWER_2_LEVEL_OBSTACLE	= 164;
	public final static int TOWER_3_LEVEL_OBSTACLE	= 165;


	public static synchronized final Terrain[] getTerrainList() {

		if (terrainList == null) {
			terrainList	 = new Terrain[256];

			terrainList[OPEN_GROUND]  = new Terrain();
				terrainList[OPEN_GROUND].name = "Open Ground";
				terrainList[OPEN_GROUND].type = OPEN_GROUND;
				terrainList[OPEN_GROUND].mapColor = OPEN_GROUND_COLOR;

			terrainList[PLOWED_FIELD]  = new Terrain();
				terrainList[PLOWED_FIELD].name = "Plowed Field";
				terrainList[PLOWED_FIELD].type = PLOWED_FIELD;
				terrainList[PLOWED_FIELD].mapColor = new Color(255, 255, 127);

			terrainList[ROOFTOP]  = new Terrain();
				terrainList[ROOFTOP].name = "Rooftop";
				terrainList[ROOFTOP].type = ROOFTOP;
				terrainList[ROOFTOP].mapColor = new Color(155, 155, 155);

			terrainList[SNOW]  = new Terrain();
				terrainList[SNOW].name = "Snow";
				terrainList[SNOW].type = SNOW;
				terrainList[SNOW].mapColor = new Color(255, 255, 255);

			terrainList[DEEP_SNOW]  = new Terrain();
				terrainList[DEEP_SNOW].name = "Deep Snow";
				terrainList[DEEP_SNOW].type = DEEP_SNOW;
				terrainList[DEEP_SNOW].mapColor = new Color(255, 255, 255);

			terrainList[ICE]  = new Terrain();
				terrainList[ICE].name = "Ice";
				terrainList[ICE].type = ICE;
				terrainList[ICE].mapColor = new Color(255, 255, 255);

			terrainList[MUD]  = new Terrain();
				terrainList[MUD].name = "Mud";
				terrainList[MUD].type = MUD;
				terrainList[MUD].mapColor = OPEN_GROUND_COLOR;

			terrainList[MUD_FLATS]  = new Terrain();
				terrainList[MUD_FLATS].name = "Mudflats";
				terrainList[MUD_FLATS].type = MUD_FLATS;
				terrainList[MUD_FLATS].mapColor = new Color(47, 99, 35);

			terrainList[WATER]  = new Terrain();
				terrainList[WATER].name = "Water";
				terrainList[WATER].type = WATER;
				terrainList[WATER].mapColor = new Color(171, 231, 255);

			terrainList[SHALLOW_WATER]  = new Terrain();
				terrainList[SHALLOW_WATER].name = "Shallow Water";
				terrainList[SHALLOW_WATER].type = SHALLOW_WATER;
				terrainList[SHALLOW_WATER].mapColor = new Color(171, 231, 255);

			terrainList[RIVER]  = new Terrain();
				terrainList[RIVER].name = "River";
				terrainList[RIVER].type = RIVER;
				terrainList[RIVER].mapColor = new Color(171, 231, 255);

			terrainList[SHALLOW_RIVER]  = new Terrain();
				terrainList[SHALLOW_RIVER].name = "Shallow River";
				terrainList[SHALLOW_RIVER].type = SHALLOW_RIVER;
				terrainList[SHALLOW_RIVER].mapColor = new Color(171, 231, 255);

			terrainList[FORD]  = new Terrain();
				terrainList[FORD].name = "Ford";
				terrainList[FORD].type = FORD;
				terrainList[FORD].mapColor = new Color(171, 231, 255);

			terrainList[CANAL]  = new Terrain();
				terrainList[CANAL].name = "Canal";
				terrainList[CANAL].type = CANAL;
				terrainList[CANAL].mapColor = new Color(171, 231, 255);

			terrainList[MARSH]  = new Terrain();
				terrainList[MARSH].name = "Marsh";
				terrainList[MARSH].type = MARSH;
				terrainList[MARSH].LOSHindrance = true;
				terrainList[MARSH].lowerLOSHindrance = true;
				terrainList[MARSH].halfLevelHeight = true;
				terrainList[MARSH].mapColor = new Color(47, 99, 35);

			terrainList[SHELL_HOLES]  = new Terrain();
				terrainList[SHELL_HOLES].name = "Shell Holes";
				terrainList[SHELL_HOLES].type = SHELL_HOLES;
				terrainList[SHELL_HOLES].mapColor = new Color(164, 115, 63);

			terrainList[FOX_HOLES]  = new Terrain();
				terrainList[FOX_HOLES].name = "Fox Holes";
				terrainList[FOX_HOLES].type = FOX_HOLES;
				terrainList[FOX_HOLES].mapColor = new Color(164, 115, 63);

			terrainList[TRENCH]  = new Terrain();
				terrainList[TRENCH].name = "Trench";
				terrainList[TRENCH].type = TRENCH;
				terrainList[TRENCH].mapColor = new Color(164, 115, 63);

			terrainList[ANTITANK_TRENCH]  = new Terrain();
				terrainList[ANTITANK_TRENCH].name = "Antitank Trench";
				terrainList[ANTITANK_TRENCH].type = ANTITANK_TRENCH;
				terrainList[ANTITANK_TRENCH].mapColor = new Color(164, 115, 63);

			terrainList[PILLBOX]  = new Terrain();
				terrainList[PILLBOX].name = "Pillbox";
				terrainList[PILLBOX].type = PILLBOX;
				terrainList[PILLBOX].mapColor = new Color(164, 115, 63);

			terrainList[GRAIN]  = new Terrain();
				terrainList[GRAIN].name = "Grain";
				terrainList[GRAIN].type = GRAIN;
				terrainList[GRAIN].LOSHindrance = true;
				terrainList[GRAIN].lowerLOSHindrance = true;
				terrainList[GRAIN].halfLevelHeight = true;
				terrainList[GRAIN].mapColor = new Color(255, 255, 127);

			terrainList[BRUSH]  = new Terrain();
				terrainList[BRUSH].name = "Brush";
				terrainList[BRUSH].type = BRUSH;
				terrainList[BRUSH].LOSHindrance = true;
				terrainList[BRUSH].lowerLOSHindrance = true;
				terrainList[BRUSH].halfLevelHeight = true;
				terrainList[BRUSH].mapColor = new Color(171, 224, 104);

			terrainList[CRAGS] = new Terrain();
				terrainList[CRAGS].name = "Crags";
				terrainList[CRAGS].type = CRAGS;
				terrainList[CRAGS].LOSHindrance = true;
				terrainList[CRAGS].lowerLOSHindrance = true;
				terrainList[CRAGS].halfLevelHeight = true;
				terrainList[CRAGS].mapColor = new Color(215, 215, 214);

			terrainList[DEBRIS]  = new Terrain();
				terrainList[DEBRIS].name = "Debris";
				terrainList[DEBRIS].type = DEBRIS;
				terrainList[DEBRIS].LOSHindrance = true;
				terrainList[DEBRIS].lowerLOSHindrance = true;
				terrainList[DEBRIS].halfLevelHeight = true;
				terrainList[DEBRIS].mapColor = new Color(200, 200, 200);

			terrainList[GRAVE_YARD]  = new Terrain();
				terrainList[GRAVE_YARD].name = "Grave Yard";
				terrainList[GRAVE_YARD].type = GRAVE_YARD;
				terrainList[GRAVE_YARD].LOSHindrance = true;
				terrainList[GRAVE_YARD].lowerLOSHindrance = true;
				terrainList[GRAVE_YARD].halfLevelHeight = true;
				terrainList[GRAVE_YARD].mapColor = new Color(48, 131, 47);

			terrainList[LUMBER_YARD]  = new Terrain();
				terrainList[LUMBER_YARD].name = "Lumber Yard";
				terrainList[LUMBER_YARD].type = LUMBER_YARD;
				terrainList[LUMBER_YARD].LOSObstacle = true;
				terrainList[LUMBER_YARD].lowerLOSObstacle = true;
				terrainList[LUMBER_YARD].height = 1;
				terrainList[LUMBER_YARD].mapColor = new Color(151, 99, 43);

			terrainList[GULLY]  = new Terrain();
				terrainList[GULLY].name = "Gully";
				terrainList[GULLY].type = GULLY;
				terrainList[GULLY].mapColor = new Color(148, 95, 35);

			terrainList[DRY_STREAM]  = new Terrain();
				terrainList[DRY_STREAM].name = "Dry Stream";
				terrainList[DRY_STREAM].type = DRY_STREAM;
				terrainList[DRY_STREAM].mapColor = new Color(148, 95, 35);

			terrainList[SHALLOW_STREAM]  = new Terrain();
				terrainList[SHALLOW_STREAM].name = "Shallow Stream";
				terrainList[SHALLOW_STREAM].type = SHALLOW_STREAM;
				terrainList[SHALLOW_STREAM].mapColor = new Color(172, 231, 255);

			terrainList[DEEP_STREAM]  = new Terrain();
				terrainList[DEEP_STREAM].name = "Deep Stream";
				terrainList[DEEP_STREAM].type = DEEP_STREAM;
				terrainList[DEEP_STREAM].mapColor = new Color(172, 231, 255);

			terrainList[FLOODED_STREAM]  = new Terrain();
				terrainList[FLOODED_STREAM].name = "Flooded Stream";
				terrainList[FLOODED_STREAM].type = FLOODED_STREAM;
				terrainList[FLOODED_STREAM].mapColor = new Color(172, 231, 255);

			terrainList[IRREGATION_DITCH]  = new Terrain();
				terrainList[IRREGATION_DITCH].name = "Irregation Ditch";
				terrainList[IRREGATION_DITCH].type = IRREGATION_DITCH;
				terrainList[IRREGATION_DITCH].mapColor = new Color(171, 231, 255);

			terrainList[STONE_BUILDING]  = new Terrain();
				terrainList[STONE_BUILDING].name = "Stone Building";
				terrainList[STONE_BUILDING].type = STONE_BUILDING;
				terrainList[STONE_BUILDING].LOSObstacle = true;
				terrainList[STONE_BUILDING].lowerLOSObstacle = true;
				terrainList[STONE_BUILDING].height = 1;
				terrainList[STONE_BUILDING].mapColor = new Color(155, 155, 155);

			terrainList[STONE_BUILDING_1_LEVEL]  = new Terrain();
				terrainList[STONE_BUILDING_1_LEVEL].name = "Stone Building, 1 Level";
				terrainList[STONE_BUILDING_1_LEVEL].type = STONE_BUILDING_1_LEVEL;
				terrainList[STONE_BUILDING_1_LEVEL].LOSObstacle = true;
				terrainList[STONE_BUILDING_1_LEVEL].lowerLOSObstacle = true;
				terrainList[STONE_BUILDING_1_LEVEL].halfLevelHeight = true;
				terrainList[STONE_BUILDING_1_LEVEL].height = 1;
				terrainList[STONE_BUILDING_1_LEVEL].mapColor = new Color(135, 135, 135);

			terrainList[STONE_BUILDING_2_LEVEL]  = new Terrain();
				terrainList[STONE_BUILDING_2_LEVEL].name = "Stone Building, 2 Level";
				terrainList[STONE_BUILDING_2_LEVEL].type = STONE_BUILDING_2_LEVEL;
				terrainList[STONE_BUILDING_2_LEVEL].LOSObstacle = true;
				terrainList[STONE_BUILDING_2_LEVEL].lowerLOSObstacle = true;
				terrainList[STONE_BUILDING_2_LEVEL].halfLevelHeight = true;
				terrainList[STONE_BUILDING_2_LEVEL].height = 2;
				terrainList[STONE_BUILDING_2_LEVEL].mapColor = new Color(115, 115, 115);

			terrainList[STONE_BUILDING_3_LEVEL]  = new Terrain();
				terrainList[STONE_BUILDING_3_LEVEL].name = "Stone Building, 3 Level";
				terrainList[STONE_BUILDING_3_LEVEL].type = STONE_BUILDING_3_LEVEL;
				terrainList[STONE_BUILDING_3_LEVEL].LOSObstacle = true;
				terrainList[STONE_BUILDING_3_LEVEL].lowerLOSObstacle = true;
				terrainList[STONE_BUILDING_3_LEVEL].halfLevelHeight = true;
				terrainList[STONE_BUILDING_3_LEVEL].height = 3;
				terrainList[STONE_BUILDING_3_LEVEL].mapColor = new Color(103, 103, 103);

			terrainList[STONE_BUILDING_4_LEVEL]  = new Terrain();
				terrainList[STONE_BUILDING_4_LEVEL].name = "Stone Building, 4 Level";
				terrainList[STONE_BUILDING_4_LEVEL].type = STONE_BUILDING_4_LEVEL;
				terrainList[STONE_BUILDING_4_LEVEL].LOSObstacle = true;
				terrainList[STONE_BUILDING_4_LEVEL].lowerLOSObstacle = true;
				terrainList[STONE_BUILDING_4_LEVEL].halfLevelHeight = true;
				terrainList[STONE_BUILDING_4_LEVEL].height = 4;
				terrainList[STONE_BUILDING_4_LEVEL].mapColor = new Color(91, 91, 91);

			terrainList[STONE_FACTORY_1_5_LEVEL]  = new Terrain();
				terrainList[STONE_FACTORY_1_5_LEVEL].name = "Stone Factory, 1.5 Level";
				terrainList[STONE_FACTORY_1_5_LEVEL].type = STONE_FACTORY_1_5_LEVEL;
				terrainList[STONE_FACTORY_1_5_LEVEL].LOSHindrance = true;
				terrainList[STONE_FACTORY_1_5_LEVEL].lowerLOSHindrance = true;
				terrainList[STONE_FACTORY_1_5_LEVEL].split = 1;
				terrainList[STONE_FACTORY_1_5_LEVEL].halfLevelHeight = true;
				terrainList[STONE_FACTORY_1_5_LEVEL].height = 1;
				terrainList[STONE_FACTORY_1_5_LEVEL].mapColor = new Color(135, 135, 135);

			terrainList[STONE_FACTORY_2_5_LEVEL]  = new Terrain();
				terrainList[STONE_FACTORY_2_5_LEVEL].name = "Stone Factory, 2.5 Level";
				terrainList[STONE_FACTORY_2_5_LEVEL].type = STONE_FACTORY_2_5_LEVEL;
				terrainList[STONE_FACTORY_2_5_LEVEL].LOSHindrance = true;
				terrainList[STONE_FACTORY_2_5_LEVEL].lowerLOSHindrance = true;
				terrainList[STONE_FACTORY_2_5_LEVEL].split = 2;
				terrainList[STONE_FACTORY_2_5_LEVEL].halfLevelHeight = true;
				terrainList[STONE_FACTORY_2_5_LEVEL].height = 2;
				terrainList[STONE_FACTORY_2_5_LEVEL].mapColor = new Color(115, 115, 115);

			terrainList[STONE_FACTORY_WALL_1_5_LEVEL]  = new Terrain();
				terrainList[STONE_FACTORY_WALL_1_5_LEVEL].name = "Stone Factory Wall, 1.5 Level";
				terrainList[STONE_FACTORY_WALL_1_5_LEVEL].type = STONE_FACTORY_WALL_1_5_LEVEL;
				terrainList[STONE_FACTORY_WALL_1_5_LEVEL].LOSObstacle = true;
				terrainList[STONE_FACTORY_WALL_1_5_LEVEL].lowerLOSObstacle = true;
				terrainList[STONE_FACTORY_WALL_1_5_LEVEL].halfLevelHeight = true;
				terrainList[STONE_FACTORY_WALL_1_5_LEVEL].height = 1;
				terrainList[STONE_FACTORY_WALL_1_5_LEVEL].mapColor = new Color(0, 0, 0);

			terrainList[STONE_FACTORY_WALL_2_5_LEVEL]  = new Terrain();
				terrainList[STONE_FACTORY_WALL_2_5_LEVEL].name = "Stone Factory Wall, 2.5 Level";
				terrainList[STONE_FACTORY_WALL_2_5_LEVEL].type = STONE_FACTORY_WALL_2_5_LEVEL;
				terrainList[STONE_FACTORY_WALL_2_5_LEVEL].LOSObstacle = true;
				terrainList[STONE_FACTORY_WALL_2_5_LEVEL].lowerLOSObstacle = true;
				terrainList[STONE_FACTORY_WALL_2_5_LEVEL].halfLevelHeight = true;
				terrainList[STONE_FACTORY_WALL_2_5_LEVEL].height = 2;
				terrainList[STONE_FACTORY_WALL_2_5_LEVEL].mapColor = new Color(0, 0, 0);

			terrainList[STONE_MARKET_PLACE]  = new Terrain();
				terrainList[STONE_MARKET_PLACE].name = "Stone Market Place";
				terrainList[STONE_MARKET_PLACE].type = STONE_MARKET_PLACE;
				terrainList[STONE_MARKET_PLACE].LOSObstacle = true;
				terrainList[STONE_MARKET_PLACE].lowerLOSObstacle = false;
				terrainList[STONE_MARKET_PLACE].halfLevelHeight = true;
				terrainList[STONE_MARKET_PLACE].split = (float) 0.5;
				terrainList[STONE_MARKET_PLACE].height = 1;
				terrainList[STONE_MARKET_PLACE].mapColor = new Color(125, 125, 125);

			terrainList[WOODEN_BUILDING]  = new Terrain();
				terrainList[WOODEN_BUILDING].name = "Wooden Building";
				terrainList[WOODEN_BUILDING].type = WOODEN_BUILDING;
				terrainList[WOODEN_BUILDING].LOSObstacle = true;
				terrainList[WOODEN_BUILDING].lowerLOSObstacle = true;
				terrainList[WOODEN_BUILDING].height = 1;
				terrainList[WOODEN_BUILDING].mapColor = new Color(163, 115, 63);

			terrainList[WOODEN_BUILDING_1_LEVEL]  = new Terrain();
				terrainList[WOODEN_BUILDING_1_LEVEL].name = "Wooden Building, 1 Level";
				terrainList[WOODEN_BUILDING_1_LEVEL].type = WOODEN_BUILDING_1_LEVEL;
				terrainList[WOODEN_BUILDING_1_LEVEL].LOSObstacle = true;
				terrainList[WOODEN_BUILDING_1_LEVEL].lowerLOSObstacle = true;
				terrainList[WOODEN_BUILDING_1_LEVEL].halfLevelHeight = true;
				terrainList[WOODEN_BUILDING_1_LEVEL].height = 1;
				terrainList[WOODEN_BUILDING_1_LEVEL].mapColor = new Color(163, 111, 7);

			terrainList[WOODEN_BUILDING_2_LEVEL]  = new Terrain();
				terrainList[WOODEN_BUILDING_2_LEVEL].name = "Wooden Building, 2 Level";
				terrainList[WOODEN_BUILDING_2_LEVEL].type = WOODEN_BUILDING_2_LEVEL;
				terrainList[WOODEN_BUILDING_2_LEVEL].LOSObstacle = true;
				terrainList[WOODEN_BUILDING_2_LEVEL].lowerLOSObstacle = true;
				terrainList[WOODEN_BUILDING_2_LEVEL].halfLevelHeight = true;
				terrainList[WOODEN_BUILDING_2_LEVEL].height = 2;
				terrainList[WOODEN_BUILDING_2_LEVEL].mapColor = new Color(151, 99, 43);

			terrainList[WOODEN_BUILDING_3_LEVEL]  = new Terrain();
				terrainList[WOODEN_BUILDING_3_LEVEL].name = "Wooden Building, 3 Level";
				terrainList[WOODEN_BUILDING_3_LEVEL].type = WOODEN_BUILDING_3_LEVEL;
				terrainList[WOODEN_BUILDING_3_LEVEL].LOSObstacle = true;
				terrainList[WOODEN_BUILDING_3_LEVEL].lowerLOSObstacle = true;
				terrainList[WOODEN_BUILDING_3_LEVEL].halfLevelHeight = true;
				terrainList[WOODEN_BUILDING_3_LEVEL].height = 3;
				terrainList[WOODEN_BUILDING_3_LEVEL].mapColor = new Color(120, 67, 0);

			terrainList[WOODEN_BUILDING_4_LEVEL]  = new Terrain();
				terrainList[WOODEN_BUILDING_4_LEVEL].name = "Wooden Building, 4 Level";
				terrainList[WOODEN_BUILDING_4_LEVEL].type = WOODEN_BUILDING_4_LEVEL;
				terrainList[WOODEN_BUILDING_4_LEVEL].LOSObstacle = true;
				terrainList[WOODEN_BUILDING_4_LEVEL].lowerLOSObstacle = true;
				terrainList[WOODEN_BUILDING_4_LEVEL].halfLevelHeight = true;
				terrainList[WOODEN_BUILDING_4_LEVEL].height = 4;
				terrainList[WOODEN_BUILDING_4_LEVEL].mapColor = new Color(59, 55, 19);

			terrainList[WOODEN_FACTORY_1_5_LEVEL]  = new Terrain();
				terrainList[WOODEN_FACTORY_1_5_LEVEL].name = "Wooden Factory, 1.5 Level";
				terrainList[WOODEN_FACTORY_1_5_LEVEL].type = WOODEN_FACTORY_1_5_LEVEL;
				terrainList[WOODEN_FACTORY_1_5_LEVEL].LOSHindrance = true;
				terrainList[WOODEN_FACTORY_1_5_LEVEL].lowerLOSHindrance = true;
				terrainList[WOODEN_FACTORY_1_5_LEVEL].split = 1;
				terrainList[WOODEN_FACTORY_1_5_LEVEL].halfLevelHeight = true;
				terrainList[WOODEN_FACTORY_1_5_LEVEL].height = 1;
				terrainList[WOODEN_FACTORY_1_5_LEVEL].mapColor = new Color(163, 111, 7);

			terrainList[WOODEN_FACTORY_2_5_LEVEL]  = new Terrain();
				terrainList[WOODEN_FACTORY_2_5_LEVEL].name = "Wooden Factory, 2.5 Level";
				terrainList[WOODEN_FACTORY_2_5_LEVEL].type = WOODEN_FACTORY_2_5_LEVEL;
				terrainList[WOODEN_FACTORY_2_5_LEVEL].LOSHindrance = true;
				terrainList[WOODEN_FACTORY_2_5_LEVEL].lowerLOSHindrance = true;
				terrainList[WOODEN_FACTORY_2_5_LEVEL].split = 2;
				terrainList[WOODEN_FACTORY_2_5_LEVEL].halfLevelHeight = true;
				terrainList[WOODEN_FACTORY_2_5_LEVEL].height = 2;
				terrainList[WOODEN_FACTORY_2_5_LEVEL].mapColor = new Color(151, 99, 43);

			terrainList[WOODEN_FACTORY_WALL_1_5_LEVEL]  = new Terrain();
				terrainList[WOODEN_FACTORY_WALL_1_5_LEVEL].name = "Wooden Factory Wall, 1.5 Level";
				terrainList[WOODEN_FACTORY_WALL_1_5_LEVEL].type = WOODEN_FACTORY_WALL_1_5_LEVEL;
				terrainList[WOODEN_FACTORY_WALL_1_5_LEVEL].LOSObstacle = true;
				terrainList[WOODEN_FACTORY_WALL_1_5_LEVEL].lowerLOSObstacle = true;
				terrainList[WOODEN_FACTORY_WALL_1_5_LEVEL].split = 1;
				terrainList[WOODEN_FACTORY_WALL_1_5_LEVEL].halfLevelHeight = true;
				terrainList[WOODEN_FACTORY_WALL_1_5_LEVEL].height = 1;
				terrainList[WOODEN_FACTORY_WALL_1_5_LEVEL].mapColor = new Color(0, 0, 0);

			terrainList[WOODEN_FACTORY_WALL_2_5_LEVEL]  = new Terrain();
				terrainList[WOODEN_FACTORY_WALL_2_5_LEVEL].name = "Wooden Factory Wall, 2.5 Level";
				terrainList[WOODEN_FACTORY_WALL_2_5_LEVEL].type = WOODEN_FACTORY_WALL_2_5_LEVEL;
				terrainList[WOODEN_FACTORY_WALL_2_5_LEVEL].LOSObstacle = true;
				terrainList[WOODEN_FACTORY_WALL_2_5_LEVEL].lowerLOSObstacle = true;
				terrainList[WOODEN_FACTORY_WALL_2_5_LEVEL].split = 2;
				terrainList[WOODEN_FACTORY_WALL_2_5_LEVEL].halfLevelHeight = true;
				terrainList[WOODEN_FACTORY_WALL_2_5_LEVEL].height = 2;
				terrainList[WOODEN_FACTORY_WALL_2_5_LEVEL].mapColor = new Color(0, 0, 0);

			terrainList[WOODEN_MARKET_PLACE]  = new Terrain();
				terrainList[WOODEN_MARKET_PLACE].name = "Wooden Market Place";
				terrainList[WOODEN_MARKET_PLACE].type = WOODEN_MARKET_PLACE;
				terrainList[WOODEN_MARKET_PLACE].LOSObstacle = true;
				terrainList[WOODEN_MARKET_PLACE].lowerLOSObstacle = false;
				terrainList[WOODEN_MARKET_PLACE].split = (float) 0.5;
				terrainList[WOODEN_MARKET_PLACE].halfLevelHeight = true;
				terrainList[WOODEN_MARKET_PLACE].height = 1;
				terrainList[WOODEN_MARKET_PLACE].mapColor = new Color(163, 111, 7);

			terrainList[WOODS]  = new Terrain();
				terrainList[WOODS].name = "Woods";
				terrainList[WOODS].type = WOODS;
				terrainList[WOODS].LOSObstacle = true;
				terrainList[WOODS].lowerLOSObstacle = true;
				terrainList[WOODS].height = 1;
				terrainList[WOODS].mapColor = new Color(47, 131, 47);

			terrainList[FOREST]  = new Terrain();
				terrainList[FOREST].name = "Forest";
				terrainList[FOREST].type = FOREST;
				terrainList[FOREST].LOSObstacle = true;
				terrainList[FOREST].lowerLOSObstacle = true;
				terrainList[FOREST].height = 2;
				terrainList[FOREST].mapColor = new Color(47, 131, 47);

			terrainList[PINE_WOODS]  = new Terrain();
				terrainList[PINE_WOODS].name = "Pine Woods";
				terrainList[PINE_WOODS].type = PINE_WOODS;
				terrainList[PINE_WOODS].LOSObstacle = true;
				terrainList[PINE_WOODS].lowerLOSObstacle = true;
				terrainList[PINE_WOODS].height = 2;
				terrainList[PINE_WOODS].mapColor = new Color(47, 131, 47);

			terrainList[ORCHARD]  = new Terrain();
				terrainList[ORCHARD].name = "Orchard";
				terrainList[ORCHARD].type = ORCHARD;
				terrainList[ORCHARD].LOSObstacle = true;
				terrainList[ORCHARD].lowerLOSHindrance = true;
				terrainList[ORCHARD].split = (float) 0.5;
				terrainList[ORCHARD].height = 1;
				terrainList[ORCHARD].mapColor = new Color(51, 163, 51);

			terrainList[ORCHARD_OUT_OF_SEASON]  = new Terrain();
				terrainList[ORCHARD_OUT_OF_SEASON].name = "Orchard, Out of Season";
				terrainList[ORCHARD_OUT_OF_SEASON].type = ORCHARD_OUT_OF_SEASON;
				terrainList[ORCHARD_OUT_OF_SEASON].LOSHindrance = true;
				terrainList[ORCHARD_OUT_OF_SEASON].lowerLOSHindrance = true;
				terrainList[ORCHARD_OUT_OF_SEASON].split = (float) 0.5;
				terrainList[ORCHARD_OUT_OF_SEASON].height = 1;
				terrainList[ORCHARD_OUT_OF_SEASON].mapColor = new Color(51, 163, 51);

			terrainList[DIRT_ROAD]  = new Terrain();
				terrainList[DIRT_ROAD].name = "Dirt Road";
				terrainList[DIRT_ROAD].type = DIRT_ROAD;
				terrainList[DIRT_ROAD].mapColor = new Color(231, 227, 143);

			terrainList[PAVED_ROAD]  = new Terrain();
				terrainList[PAVED_ROAD].name = "Paved Road";
				terrainList[PAVED_ROAD].type = PAVED_ROAD;
				terrainList[PAVED_ROAD].mapColor = new Color(215, 215, 215);

			terrainList[ELEVATED_ROAD]  = new Terrain();
				terrainList[ELEVATED_ROAD].name = "Elevated Road";
				terrainList[ELEVATED_ROAD].type = ELEVATED_ROAD;
				terrainList[ELEVATED_ROAD].mapColor = new Color(231, 227, 143);

			terrainList[SUNKEN_ROAD]  = new Terrain();
				terrainList[SUNKEN_ROAD].name = "Sunken Road";
				terrainList[SUNKEN_ROAD].type = SUNKEN_ROAD;
				terrainList[SUNKEN_ROAD].mapColor = new Color(231, 227, 143);

			terrainList[RUNWAY]  = new Terrain();
				terrainList[RUNWAY].name = "Runway";
				terrainList[RUNWAY].type = RUNWAY;
				terrainList[RUNWAY].mapColor = new Color(215, 215, 215);

			terrainList[PATH]  = new Terrain();
				terrainList[PATH].name = "Path";
				terrainList[PATH].type = PATH;
				terrainList[PATH].mapColor = new Color(200, 175, 83);

			terrainList[RAILROAD_EMBANKMENT] = new Terrain();
				terrainList[RAILROAD_EMBANKMENT].name = "Railroad Embankment";
				terrainList[RAILROAD_EMBANKMENT].type = RAILROAD_EMBANKMENT;
				terrainList[RAILROAD_EMBANKMENT].mapColor = new Color(128, 0, 0);

			terrainList[WALL] = new Terrain();
				terrainList[WALL].name = "Wall";
				terrainList[WALL].type = WALL;
				terrainList[WALL].LOSObstacle = true;
				terrainList[WALL].lowerLOSObstacle = true;
				terrainList[WALL].halfLevelHeight = true;
				terrainList[WALL].mapColor = new Color(128, 127, 127);

			terrainList[HEDGE]  = new Terrain();
				terrainList[HEDGE].name = "Hedge";
				terrainList[HEDGE].type = HEDGE;
				terrainList[HEDGE].LOSObstacle = true;
				terrainList[HEDGE].lowerLOSObstacle = true;
				terrainList[HEDGE].halfLevelHeight = true;
				terrainList[HEDGE].mapColor = new Color(46, 131, 47);

			terrainList[CLIFF]  = new Terrain();
				terrainList[CLIFF].name = "Cliff";
				terrainList[CLIFF].type = CLIFF;
				terrainList[CLIFF].mapColor = new Color(61, 55, 19);

			terrainList[BOCAGE]  = new Terrain();
				terrainList[BOCAGE].name = "Bocage";
				terrainList[BOCAGE].type = BOCAGE;
				terrainList[BOCAGE].LOSObstacle = true;
				terrainList[BOCAGE].lowerLOSObstacle = true;
				terrainList[BOCAGE].height = 1;
				terrainList[BOCAGE].mapColor = new Color(51, 163, 50);

			terrainList[ROWHOUSE_WALL]  = new Terrain();
				terrainList[ROWHOUSE_WALL].name = "Rowhouse Wall";
				terrainList[ROWHOUSE_WALL].type = ROWHOUSE_WALL;
				terrainList[ROWHOUSE_WALL].LOSObstacle = true;
				terrainList[ROWHOUSE_WALL].lowerLOSObstacle = true;
				terrainList[ROWHOUSE_WALL].height = 1;
				terrainList[ROWHOUSE_WALL].mapColor = new Color(1, 0, 0);

			terrainList[ROWHOUSE_WALL_1_LEVEL]  = new Terrain();
				terrainList[ROWHOUSE_WALL_1_LEVEL].name = "Rowhouse Wall, 1 Level";
				terrainList[ROWHOUSE_WALL_1_LEVEL].type = ROWHOUSE_WALL_1_LEVEL;
				terrainList[ROWHOUSE_WALL_1_LEVEL].LOSObstacle = true;
				terrainList[ROWHOUSE_WALL_1_LEVEL].lowerLOSObstacle = true;
				terrainList[ROWHOUSE_WALL_1_LEVEL].halfLevelHeight = true;
				terrainList[ROWHOUSE_WALL_1_LEVEL].height = 1;
				terrainList[ROWHOUSE_WALL_1_LEVEL].mapColor = new Color(1, 0, 0);

			terrainList[ROWHOUSE_WALL_2_LEVEL]  = new Terrain();
				terrainList[ROWHOUSE_WALL_2_LEVEL].name = "Rowhouse Wall, 2 Level";
				terrainList[ROWHOUSE_WALL_2_LEVEL].type = ROWHOUSE_WALL_2_LEVEL;
				terrainList[ROWHOUSE_WALL_2_LEVEL].LOSObstacle = true;
				terrainList[ROWHOUSE_WALL_2_LEVEL].lowerLOSObstacle = true;
				terrainList[ROWHOUSE_WALL_2_LEVEL].halfLevelHeight = true;
				terrainList[ROWHOUSE_WALL_2_LEVEL].height = 2;
				terrainList[ROWHOUSE_WALL_2_LEVEL].mapColor = new Color(1, 0, 0);

			terrainList[ROWHOUSE_WALL_3_LEVEL]  = new Terrain();
				terrainList[ROWHOUSE_WALL_3_LEVEL].name = "Rowhouse Wall, 3 Level";
				terrainList[ROWHOUSE_WALL_3_LEVEL].type = ROWHOUSE_WALL_3_LEVEL;
				terrainList[ROWHOUSE_WALL_3_LEVEL].LOSObstacle = true;
				terrainList[ROWHOUSE_WALL_3_LEVEL].lowerLOSObstacle = true;
				terrainList[ROWHOUSE_WALL_3_LEVEL].halfLevelHeight = true;
				terrainList[ROWHOUSE_WALL_3_LEVEL].height = 3;
				terrainList[ROWHOUSE_WALL_3_LEVEL].mapColor = new Color(1, 0, 0);

			terrainList[ROWHOUSE_WALL_4_LEVEL]  = new Terrain();
				terrainList[ROWHOUSE_WALL_4_LEVEL].name = "Rowhouse Wall, 4 Level";
				terrainList[ROWHOUSE_WALL_4_LEVEL].type = ROWHOUSE_WALL_4_LEVEL;
				terrainList[ROWHOUSE_WALL_4_LEVEL].LOSObstacle = true;
				terrainList[ROWHOUSE_WALL_4_LEVEL].lowerLOSObstacle = true;
				terrainList[ROWHOUSE_WALL_4_LEVEL].halfLevelHeight = true;
				terrainList[ROWHOUSE_WALL_4_LEVEL].height = 3;
				terrainList[ROWHOUSE_WALL_4_LEVEL].mapColor = new Color(1, 0, 0);

			terrainList[ROAD_BLOCK] = new Terrain();
				terrainList[ROAD_BLOCK].name = "Road Block";
				terrainList[ROAD_BLOCK].type = ROAD_BLOCK;
				terrainList[ROAD_BLOCK].LOSObstacle = true;
				terrainList[ROAD_BLOCK].lowerLOSObstacle = true;
				terrainList[ROAD_BLOCK].halfLevelHeight = true;
				terrainList[ROAD_BLOCK].mapColor = new Color(100, 100, 100);

			terrainList[SINGLE_HEX_STONE_BRIDGE]  = new Terrain();
				terrainList[SINGLE_HEX_STONE_BRIDGE].name = "Single Hex Stone Bridge";
				terrainList[SINGLE_HEX_STONE_BRIDGE].type = SINGLE_HEX_STONE_BRIDGE;
				terrainList[SINGLE_HEX_STONE_BRIDGE].LOSHindrance = true;
				terrainList[SINGLE_HEX_STONE_BRIDGE].lowerLOSHindrance = true;
				terrainList[SINGLE_HEX_STONE_BRIDGE].split = 1;
				terrainList[SINGLE_HEX_STONE_BRIDGE].halfLevelHeight = true;
				terrainList[SINGLE_HEX_STONE_BRIDGE].mapColor = new Color(129, 127, 127);

			terrainList[SINGLE_HEX_WOODEN_BRIDGE]  = new Terrain();
				terrainList[SINGLE_HEX_WOODEN_BRIDGE].name = "Single Hex Wooden Bridge";
				terrainList[SINGLE_HEX_WOODEN_BRIDGE].type = SINGLE_HEX_WOODEN_BRIDGE;
				terrainList[SINGLE_HEX_WOODEN_BRIDGE].LOSHindrance = true;
				terrainList[SINGLE_HEX_WOODEN_BRIDGE].lowerLOSHindrance = true;
				terrainList[SINGLE_HEX_WOODEN_BRIDGE].split = 1;
				terrainList[SINGLE_HEX_WOODEN_BRIDGE].halfLevelHeight = true;
				terrainList[SINGLE_HEX_WOODEN_BRIDGE].height = 0;
				terrainList[SINGLE_HEX_WOODEN_BRIDGE].mapColor = new Color(153, 99, 43);

			terrainList[STONE_BRIDGE]  = new Terrain();
				terrainList[STONE_BRIDGE].name = "Stone Bridge";
				terrainList[STONE_BRIDGE].type = STONE_BRIDGE;
				terrainList[STONE_BRIDGE].LOSHindrance = true;
				terrainList[STONE_BRIDGE].lowerLOSHindrance = true;
				terrainList[STONE_BRIDGE].split = 1;
				terrainList[STONE_BRIDGE].halfLevelHeight = true;
				terrainList[STONE_BRIDGE].mapColor = new Color(93, 91, 91);

			terrainList[WOODEN_BRIDGE]  = new Terrain();
				terrainList[WOODEN_BRIDGE].name = "Wooden Bridge";
				terrainList[WOODEN_BRIDGE].type = WOODEN_BRIDGE;
				terrainList[WOODEN_BRIDGE].LOSHindrance = true;
				terrainList[WOODEN_BRIDGE].lowerLOSHindrance = true;
				terrainList[WOODEN_BRIDGE].split = 1;
				terrainList[WOODEN_BRIDGE].halfLevelHeight = true;
				terrainList[WOODEN_BRIDGE].height = 0;
				terrainList[WOODEN_BRIDGE].mapColor = new Color(153, 99, 43);

			terrainList[PONTOON_BRIDGE]  = new Terrain();
				terrainList[PONTOON_BRIDGE].name = "Pontoon Bridge";
				terrainList[PONTOON_BRIDGE].type = PONTOON_BRIDGE;
				terrainList[PONTOON_BRIDGE].mapColor = new Color(153, 99, 43);

			terrainList[FOOT_BRIDGE]  = new Terrain();
				terrainList[FOOT_BRIDGE].name = "Foot Bridge";
				terrainList[FOOT_BRIDGE].type = FOOT_BRIDGE;
				terrainList[FOOT_BRIDGE].mapColor = new Color(153, 99, 43);

			terrainList[STONE_RUBBLE]  = new Terrain();
				terrainList[STONE_RUBBLE].name = "Stone Rubble";
				terrainList[STONE_RUBBLE].type = STONE_RUBBLE;
				terrainList[STONE_RUBBLE].LOSObstacle = true;
				terrainList[STONE_RUBBLE].lowerLOSObstacle = true;
				terrainList[STONE_RUBBLE].height = 0;
				terrainList[STONE_RUBBLE].halfLevelHeight = true;
				terrainList[STONE_RUBBLE].mapColor = new Color(115, 115, 115);

			terrainList[WOODEN_RUBBLE]  = new Terrain();
				terrainList[WOODEN_RUBBLE].name = "Wooden Rubble";
				terrainList[WOODEN_RUBBLE].type = WOODEN_RUBBLE;
				terrainList[WOODEN_RUBBLE].LOSObstacle = true;
				terrainList[WOODEN_RUBBLE].lowerLOSObstacle = true;
				terrainList[WOODEN_RUBBLE].height = 0;
				terrainList[WOODEN_RUBBLE].halfLevelHeight = true;
				terrainList[WOODEN_RUBBLE].mapColor = new Color(147, 95,35);

			terrainList[SEWER]  = new Terrain();
				terrainList[SEWER].name = "Sewer";
				terrainList[SEWER].type = SEWER;
				terrainList[SEWER].mapColor = new Color(0, 0, 0);

			terrainList[TUNNEL]  = new Terrain();
				terrainList[TUNNEL].name = "Tunnel";
				terrainList[TUNNEL].type = TUNNEL;
				terrainList[TUNNEL].mapColor = new Color(0, 0, 0);

			terrainList[CAVE]  = new Terrain();
				terrainList[CAVE].name = "Cave";
				terrainList[CAVE].type = CAVE;
				terrainList[CAVE].mapColor = new Color(0, 0, 0);

			terrainList[SCRUB]  = new Terrain();
				terrainList[SCRUB].name = "Scrub";
				terrainList[SCRUB].type = SCRUB;
				terrainList[SCRUB].mapColor = new Color(119, 67, 0);

			terrainList[HAMMADA]  = new Terrain();
				terrainList[HAMMADA].name = "Hammada";
				terrainList[HAMMADA].type = HAMMADA;
				terrainList[HAMMADA].mapColor = new Color(59, 55, 19);

			terrainList[DEIR]  = new Terrain();
				terrainList[DEIR].name = "Deir";
				terrainList[DEIR].type = DEIR;
				terrainList[DEIR].mapColor = new Color(251, 223, 147);

			terrainList[DEIR_LIP]  = new Terrain();
				terrainList[DEIR_LIP].name = "Deir Lip";
				terrainList[DEIR_LIP].type = DEIR_LIP;
				terrainList[DEIR_LIP].mapColor = new Color(251, 223, 147);

			terrainList[WADI]  = new Terrain();
				terrainList[WADI].name = "Wadi";
				terrainList[WADI].type = WADI;
				terrainList[WADI].mapColor = new Color(191, 111, 7);

			terrainList[HILLOCK]  = new Terrain();
				terrainList[HILLOCK].name = "Hillock";
				terrainList[HILLOCK].type = HILLOCK;
				terrainList[HILLOCK].LOSObstacle = true;
				terrainList[HILLOCK].lowerLOSObstacle = true;
				terrainList[HILLOCK].halfLevelHeight = true;
				terrainList[HILLOCK].mapColor = new Color(251, 223, 147);

			terrainList[HILLOCK_SUMMIT]  = new Terrain();
				terrainList[HILLOCK_SUMMIT].name = "Hillock Summit";
				terrainList[HILLOCK_SUMMIT].type = HILLOCK_SUMMIT;
				terrainList[HILLOCK_SUMMIT].mapColor = new Color(251, 223, 147);

			terrainList[SAND]  = new Terrain();
				terrainList[SAND].name = "Sand";
				terrainList[SAND].type = SAND;
				terrainList[SAND].mapColor = new Color(251, 223, 147);

			terrainList[SAND_DUNE]  = new Terrain();
				terrainList[SAND_DUNE].name = "Sand Dune";
				terrainList[SAND_DUNE].type = SAND_DUNE;
				terrainList[SAND_DUNE].LOSObstacle = true;
				terrainList[SAND_DUNE].lowerLOSObstacle = true;
				terrainList[SAND_DUNE].halfLevelHeight = true;
				terrainList[SAND_DUNE].mapColor = new Color(251, 223, 147);

			terrainList[DUNE_CREST_LOW]  = new Terrain();
				terrainList[DUNE_CREST_LOW].name = "Dune, Crest Low";
				terrainList[DUNE_CREST_LOW].type = DUNE_CREST_LOW;
				terrainList[DUNE_CREST_LOW].LOSObstacle = true;
				terrainList[DUNE_CREST_LOW].lowerLOSObstacle = true;
				terrainList[DUNE_CREST_LOW].halfLevelHeight = true;
				terrainList[DUNE_CREST_LOW].mapColor = new Color(251, 223, 147);

			terrainList[DUNE_CREST_HIGH]  = new Terrain();
				terrainList[DUNE_CREST_HIGH].name = "Dune, Crest High";
				terrainList[DUNE_CREST_HIGH].type = DUNE_CREST_HIGH;
				terrainList[DUNE_CREST_HIGH].LOSObstacle = true;
				terrainList[DUNE_CREST_HIGH].lowerLOSObstacle = true;
				terrainList[DUNE_CREST_HIGH].halfLevelHeight = true;
				terrainList[DUNE_CREST_HIGH].mapColor = new Color(251, 223, 147);

			terrainList[SANGAR]  = new Terrain();
				terrainList[SANGAR].name = "Sangar";
				terrainList[SANGAR].type = SANGAR;
				terrainList[SANGAR].mapColor = new Color(250, 235, 130);

			terrainList[TRACKS]  = new Terrain();
				terrainList[TRACKS].name = "Tracks";
				terrainList[TRACKS].type = TRACKS;
				terrainList[TRACKS].mapColor = new Color(200, 175, 83);

			terrainList[MAUSOLEUM]  = new Terrain();
				terrainList[MAUSOLEUM].name = "Mausoleum";
				terrainList[MAUSOLEUM].type = MAUSOLEUM;
				terrainList[MAUSOLEUM].LOSObstacle = true;
				terrainList[MAUSOLEUM].lowerLOSObstacle = true;
				terrainList[MAUSOLEUM].height = 1;
				terrainList[MAUSOLEUM].mapColor = new Color(155, 155, 155);

			terrainList[BEDOUIN_CAMP]  = new Terrain();
				terrainList[BEDOUIN_CAMP].name = "Bedouin Camp";
				terrainList[BEDOUIN_CAMP].type = BEDOUIN_CAMP;
				terrainList[BEDOUIN_CAMP].LOSHindrance = true;
				terrainList[BEDOUIN_CAMP].lowerLOSHindrance = true;
				terrainList[BEDOUIN_CAMP].halfLevelHeight = true;
				terrainList[BEDOUIN_CAMP].mapColor = new Color(155, 155, 155);

			terrainList[BUILDING_CLUSTER]  = new Terrain();
				terrainList[BUILDING_CLUSTER].name = "Building Cluster";
				terrainList[BUILDING_CLUSTER].type = BUILDING_CLUSTER;
				terrainList[BUILDING_CLUSTER].LOSObstacle = true;
				terrainList[BUILDING_CLUSTER].lowerLOSObstacle = true;
				terrainList[BUILDING_CLUSTER].height = 1;
				terrainList[BUILDING_CLUSTER].mapColor = new Color(155, 155, 155);

			terrainList[CACTUS_HEDGE]  = new Terrain();
				terrainList[CACTUS_HEDGE].name = "Cactus Hedge";
				terrainList[CACTUS_HEDGE].type = CACTUS_HEDGE;
				terrainList[CACTUS_HEDGE].LOSObstacle = true;
				terrainList[CACTUS_HEDGE].lowerLOSObstacle = true;
				terrainList[CACTUS_HEDGE].halfLevelHeight = true;
				terrainList[CACTUS_HEDGE].mapColor = new Color(46, 131, 47);

			terrainList[CACTUS_PATCH]  = new Terrain();
				terrainList[CACTUS_PATCH].name = "Cactus Patch";
				terrainList[CACTUS_PATCH].type = CACTUS_PATCH;
				terrainList[CACTUS_PATCH].LOSObstacle = true;
				terrainList[CACTUS_PATCH].lowerLOSObstacle = true;
				terrainList[CACTUS_PATCH].halfLevelHeight = true;
				terrainList[CACTUS_PATCH].mapColor = new Color(46, 131, 47);

			terrainList[OLIVE_GROVE]  = new Terrain();
				terrainList[OLIVE_GROVE].name = "Olive Grove";
				terrainList[OLIVE_GROVE].type = OLIVE_GROVE;
				terrainList[OLIVE_GROVE].LOSObstacle = true;
				terrainList[OLIVE_GROVE].lowerLOSHindrance = true;
				terrainList[OLIVE_GROVE].split = (float) 0.5;
				terrainList[OLIVE_GROVE].height = 1;
				terrainList[OLIVE_GROVE].mapColor = new Color(51, 163, 51);

			terrainList[VINEYARD]  = new Terrain();
				terrainList[VINEYARD].name = "Vineyard";
				terrainList[VINEYARD].type = VINEYARD;
				terrainList[VINEYARD].LOSHindrance = true;
				terrainList[VINEYARD].lowerLOSHindrance = true;
				terrainList[VINEYARD].halfLevelHeight = true;
				terrainList[VINEYARD].mapColor = new Color(46, 131, 47);

			terrainList[BROKEN_GROUND]  = new Terrain();
				terrainList[BROKEN_GROUND].name = "Broken Ground";
				terrainList[BROKEN_GROUND].type = BROKEN_GROUND;
				terrainList[BROKEN_GROUND].mapColor = new Color(251, 223, 147);

			terrainList[TEMPLE]  = new Terrain();
				terrainList[TEMPLE].name = "Temple";
				terrainList[TEMPLE].type = TEMPLE;
				terrainList[TEMPLE].LOSObstacle = true;
				terrainList[TEMPLE].lowerLOSObstacle = true;
				terrainList[TEMPLE].height = 1;
				terrainList[TEMPLE].mapColor = new Color(115, 115, 115);

			terrainList[LIGHT_JUNGLE]  = new Terrain();
				terrainList[LIGHT_JUNGLE].name = "Light Jungle";
				terrainList[LIGHT_JUNGLE].type = LIGHT_JUNGLE;
				terrainList[LIGHT_JUNGLE].LOSObstacle = true;
				terrainList[LIGHT_JUNGLE].lowerLOSObstacle = true;
				terrainList[LIGHT_JUNGLE].height = 2;
				terrainList[LIGHT_JUNGLE].mapColor = new Color(47, 131, 47);

			terrainList[DENSE_JUNGLE]  = new Terrain();
				terrainList[DENSE_JUNGLE].name = "Dense Jungle";
				terrainList[DENSE_JUNGLE].type = DENSE_JUNGLE;
				terrainList[DENSE_JUNGLE].LOSObstacle = true;
				terrainList[DENSE_JUNGLE].lowerLOSObstacle = true;
				terrainList[DENSE_JUNGLE].height = 2;
				terrainList[DENSE_JUNGLE].mapColor = new Color(47, 131, 47);

			terrainList[BAMBOO]  = new Terrain();
				terrainList[BAMBOO].name = "Bamboo";
				terrainList[BAMBOO].type = BAMBOO;
				terrainList[BAMBOO].LOSObstacle = true;
				terrainList[BAMBOO].lowerLOSObstacle = true;
				terrainList[BAMBOO].height = 1;
				terrainList[BAMBOO].mapColor = new Color(0, 128, 0);

			terrainList[PALM_TREES]  = new Terrain();
				terrainList[PALM_TREES].name = "Palm Trees";
				terrainList[PALM_TREES].type = PALM_TREES;
				terrainList[PALM_TREES].LOSObstacle = true;
				terrainList[PALM_TREES].lowerLOSHindrance = true;
				terrainList[PALM_TREES].split = (float) 0.5;
				terrainList[PALM_TREES].height = 1;
				terrainList[PALM_TREES].mapColor = new Color(51, 163, 51);

			terrainList[HUTS]  = new Terrain();
				terrainList[HUTS].name = "Huts";
				terrainList[HUTS].type = HUTS;
				terrainList[HUTS].LOSHindrance = true;
				terrainList[HUTS].lowerLOSHindrance = true;
				terrainList[HUTS].split = (float) 0.5;
				terrainList[HUTS].height = 1;
				terrainList[HUTS].mapColor = new Color(147, 95, 35);

			terrainList[COLLAPSED_HUTS]  = new Terrain();
				terrainList[COLLAPSED_HUTS].name = "Collapsed Huts";
				terrainList[COLLAPSED_HUTS].type = COLLAPSED_HUTS;
				terrainList[COLLAPSED_HUTS].LOSHindrance = true;
				terrainList[COLLAPSED_HUTS].lowerLOSHindrance = true;
				terrainList[COLLAPSED_HUTS].height = 1;
				terrainList[COLLAPSED_HUTS].mapColor = new Color(147, 95, 35);

			terrainList[KUNAI]  = new Terrain();
				terrainList[KUNAI].name = "Kunai";
				terrainList[KUNAI].type = KUNAI;
				terrainList[KUNAI].LOSHindrance = true;
				terrainList[KUNAI].lowerLOSHindrance = true;
				terrainList[KUNAI].halfLevelHeight = true;
				terrainList[KUNAI].mapColor = new Color(255, 255, 127);

			terrainList[SWAMP]  = new Terrain();
				terrainList[SWAMP].name = "Swamp";
				terrainList[SWAMP].type = SWAMP;
				terrainList[SWAMP].LOSObstacle = true;
				terrainList[SWAMP].lowerLOSObstacle = true;
				terrainList[SWAMP].height = 2;
				terrainList[SWAMP].mapColor = new Color(47, 99, 36);

			terrainList[RICE_PADDY_DRAINED]  = new Terrain();
				terrainList[RICE_PADDY_DRAINED].name = "Rice Paddy, Drained";
				terrainList[RICE_PADDY_DRAINED].type = RICE_PADDY_DRAINED;
				terrainList[RICE_PADDY_DRAINED].mapColor = new Color(47, 99, 36);

			terrainList[RICE_PADDY_IRRIGATED]  = new Terrain();
				terrainList[RICE_PADDY_IRRIGATED].name = "Rice Paddy, Irrigated";
				terrainList[RICE_PADDY_IRRIGATED].type = RICE_PADDY_IRRIGATED;
				terrainList[RICE_PADDY_IRRIGATED].mapColor = new Color(47, 99, 36);

			terrainList[RICE_PADDY_IN_SEASON]  = new Terrain();
				terrainList[RICE_PADDY_IN_SEASON].name = "Rice Paddy, In Season";
				terrainList[RICE_PADDY_IN_SEASON].type = RICE_PADDY_IN_SEASON;
				terrainList[RICE_PADDY_IN_SEASON].mapColor = new Color(47, 99, 36);

			terrainList[RICE_PADDY_BANK]  = new Terrain();
				terrainList[RICE_PADDY_BANK].name = "Rice Paddy Bank";
				terrainList[RICE_PADDY_BANK].type = RICE_PADDY_BANK;
				terrainList[RICE_PADDY_BANK].mapColor = new Color(205, 175, 84);

			terrainList[PANJI]  = new Terrain();
				terrainList[PANJI].name = "Panji";
				terrainList[PANJI].type = PANJI;
				terrainList[PANJI].mapColor = new Color(0, 0, 0);

			terrainList[OCEAN]  = new Terrain();
				terrainList[OCEAN].name = "Ocean";
				terrainList[OCEAN].type = OCEAN;
				terrainList[OCEAN].mapColor = new Color(0, 128, 255);

			terrainList[SHALLOW_OCEAN]  = new Terrain();
				terrainList[SHALLOW_OCEAN].name = "Shallow Ocean";
				terrainList[SHALLOW_OCEAN].type = SHALLOW_OCEAN;
				terrainList[SHALLOW_OCEAN].mapColor = new Color(171, 231, 255);

			terrainList[EFFLUENT]  = new Terrain();
				terrainList[EFFLUENT].name = "Effluent";
				terrainList[EFFLUENT].type = EFFLUENT;
				terrainList[EFFLUENT].mapColor = new Color(171, 231, 255);

			terrainList[SOFT_SAND]  = new Terrain();
				terrainList[SOFT_SAND].name = "Soft Sand";
				terrainList[SOFT_SAND].type = SOFT_SAND;
				terrainList[SOFT_SAND].mapColor = OPEN_GROUND_COLOR;

			terrainList[BEACH_SLIGHT_SLOPE]  = new Terrain();
				terrainList[BEACH_SLIGHT_SLOPE].name = "Beach, Slight Slope";
				terrainList[BEACH_SLIGHT_SLOPE].type = BEACH_SLIGHT_SLOPE;
				terrainList[BEACH_SLIGHT_SLOPE].mapColor = OPEN_GROUND_COLOR;

			terrainList[BEACH_MODERATE_SLOPE]  = new Terrain();
				terrainList[BEACH_MODERATE_SLOPE].name = "Beach, Moderate Slope";
				terrainList[BEACH_MODERATE_SLOPE].type = BEACH_MODERATE_SLOPE;
				terrainList[BEACH_MODERATE_SLOPE].mapColor = OPEN_GROUND_COLOR;

			terrainList[BEACH_STEEP_SLOPE]  = new Terrain();
				terrainList[BEACH_STEEP_SLOPE].name = "Beach, Steep Slope";
				terrainList[BEACH_STEEP_SLOPE].type = BEACH_STEEP_SLOPE;
				terrainList[BEACH_STEEP_SLOPE].mapColor = OPEN_GROUND_COLOR;

			terrainList[EXPOSED_REEF]  = new Terrain();
				terrainList[EXPOSED_REEF].name = "Exposed Reef";
				terrainList[EXPOSED_REEF].type = EXPOSED_REEF;
				terrainList[EXPOSED_REEF].mapColor = OPEN_GROUND_COLOR;

			terrainList[SUBMERGED_REEF]  = new Terrain();
				terrainList[SUBMERGED_REEF].name = "Submerged Reef";
				terrainList[SUBMERGED_REEF].type = SUBMERGED_REEF;
				terrainList[SUBMERGED_REEF].mapColor = OPEN_GROUND_COLOR;

			terrainList[GROUND_LEVEL_RAILROAD]  = new Terrain();
				terrainList[GROUND_LEVEL_RAILROAD].name = "Railroad, Ground Level";
				terrainList[GROUND_LEVEL_RAILROAD].type = GROUND_LEVEL_RAILROAD;
				terrainList[GROUND_LEVEL_RAILROAD].mapColor = OPEN_GROUND_COLOR;

			terrainList[EMBANKMENT_RAILROAD]  = new Terrain();
				terrainList[EMBANKMENT_RAILROAD].name = "Railroad, Embankment";
				terrainList[EMBANKMENT_RAILROAD].type = EMBANKMENT_RAILROAD;
				terrainList[EMBANKMENT_RAILROAD].mapColor = OPEN_GROUND_COLOR;

			terrainList[EMBANKMENT_RAILROAD]  = new Terrain();
				terrainList[EMBANKMENT_RAILROAD].name = "Railroad, Embankment";
				terrainList[EMBANKMENT_RAILROAD].type = EMBANKMENT_RAILROAD;
				terrainList[EMBANKMENT_RAILROAD].mapColor = OPEN_GROUND_COLOR;

			terrainList[TOWER_1_LEVEL_HINDRANCE]  = new Terrain();
				terrainList[TOWER_1_LEVEL_HINDRANCE].name = "Tower, 1 Level Hindrance";
				terrainList[TOWER_1_LEVEL_HINDRANCE].type = TOWER_1_LEVEL_HINDRANCE;
				terrainList[TOWER_1_LEVEL_HINDRANCE].LOSHindrance = true;
				terrainList[TOWER_1_LEVEL_HINDRANCE].lowerLOSHindrance = true;
				terrainList[TOWER_1_LEVEL_HINDRANCE].halfLevelHeight = true;
				terrainList[TOWER_1_LEVEL_HINDRANCE].height = 1;
				terrainList[TOWER_1_LEVEL_HINDRANCE].mapColor = new Color(115, 115, 115);

			terrainList[TOWER_2_LEVEL_HINDRANCE]  = new Terrain();
				terrainList[TOWER_2_LEVEL_HINDRANCE].name = "Tower, 2 Level Hindrance";
				terrainList[TOWER_2_LEVEL_HINDRANCE].type = TOWER_2_LEVEL_HINDRANCE;
				terrainList[TOWER_2_LEVEL_HINDRANCE].LOSHindrance = true;
				terrainList[TOWER_2_LEVEL_HINDRANCE].lowerLOSHindrance = true;
				terrainList[TOWER_2_LEVEL_HINDRANCE].halfLevelHeight = true;
				terrainList[TOWER_2_LEVEL_HINDRANCE].height = 2;
				terrainList[TOWER_2_LEVEL_HINDRANCE].mapColor = new Color(115, 115, 115);

			terrainList[TOWER_3_LEVEL_HINDRANCE]  = new Terrain();
				terrainList[TOWER_3_LEVEL_HINDRANCE].name = "Tower, 3 Level Hindrance";
				terrainList[TOWER_3_LEVEL_HINDRANCE].type = TOWER_3_LEVEL_HINDRANCE;
				terrainList[TOWER_3_LEVEL_HINDRANCE].LOSHindrance = true;
				terrainList[TOWER_3_LEVEL_HINDRANCE].lowerLOSHindrance = true;
				terrainList[TOWER_3_LEVEL_HINDRANCE].halfLevelHeight = true;
				terrainList[TOWER_3_LEVEL_HINDRANCE].height = 3;
				terrainList[TOWER_3_LEVEL_HINDRANCE].mapColor = new Color(115, 115, 115);

			terrainList[TOWER_1_LEVEL_OBSTACLE]  = new Terrain();
				terrainList[TOWER_1_LEVEL_OBSTACLE].name = "Tower, 1 Level Obstacle";
				terrainList[TOWER_1_LEVEL_OBSTACLE].type = TOWER_1_LEVEL_OBSTACLE;
				terrainList[TOWER_1_LEVEL_OBSTACLE].LOSObstacle = true;
				terrainList[TOWER_1_LEVEL_OBSTACLE].lowerLOSObstacle = true;
				terrainList[TOWER_1_LEVEL_OBSTACLE].halfLevelHeight = true;
				terrainList[TOWER_1_LEVEL_OBSTACLE].height = 1;
				terrainList[TOWER_1_LEVEL_OBSTACLE].mapColor = new Color(115, 115, 115);

			terrainList[TOWER_2_LEVEL_OBSTACLE]  = new Terrain();
				terrainList[TOWER_2_LEVEL_OBSTACLE].name = "Tower, 2 Level Obstacle";
				terrainList[TOWER_2_LEVEL_OBSTACLE].type = TOWER_2_LEVEL_OBSTACLE;
				terrainList[TOWER_2_LEVEL_OBSTACLE].LOSObstacle = true;
				terrainList[TOWER_2_LEVEL_OBSTACLE].lowerLOSObstacle = true;
				terrainList[TOWER_2_LEVEL_OBSTACLE].halfLevelHeight = true;
				terrainList[TOWER_2_LEVEL_OBSTACLE].height = 2;
				terrainList[TOWER_2_LEVEL_OBSTACLE].mapColor = new Color(115, 115, 115);

			terrainList[TOWER_3_LEVEL_OBSTACLE]  = new Terrain();
				terrainList[TOWER_3_LEVEL_OBSTACLE].name = "Tower, 3 Level Obstacle";
				terrainList[TOWER_3_LEVEL_OBSTACLE].type = TOWER_3_LEVEL_OBSTACLE;
				terrainList[TOWER_3_LEVEL_OBSTACLE].LOSObstacle = true;
				terrainList[TOWER_3_LEVEL_OBSTACLE].lowerLOSObstacle = true;
				terrainList[TOWER_3_LEVEL_OBSTACLE].halfLevelHeight = true;
				terrainList[TOWER_3_LEVEL_OBSTACLE].height = 3;
				terrainList[TOWER_3_LEVEL_OBSTACLE].mapColor = new Color(115, 115, 115);
		}
		return terrainList;
	}

	public boolean isEdgeTerrain() {
		if (type == WALL		||
			type == HEDGE		||
			type == BOCAGE		||
			type == ROAD_BLOCK	||
			type == CLIFF		||
			type == ROWHOUSE_WALL		||
			type == ROWHOUSE_WALL_1_LEVEL	||
			type == ROWHOUSE_WALL_2_LEVEL	||
			type == ROWHOUSE_WALL_3_LEVEL	||
			type == ROWHOUSE_WALL_4_LEVEL	||
			type == RICE_PADDY_BANK		||
			type == DEIR_LIP		||
			type == DUNE_CREST_LOW	||
			type == DUNE_CREST_HIGH	||
			type == CACTUS_HEDGE){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean isBuildingTerrain()	 {
		if (type == STONE_BUILDING			||
			type == STONE_BUILDING_1_LEVEL	||
			type == STONE_BUILDING_2_LEVEL	||
			type == STONE_BUILDING_3_LEVEL	||
			type == STONE_BUILDING_4_LEVEL	||
			type == STONE_FACTORY_1_5_LEVEL	||
			type == STONE_FACTORY_2_5_LEVEL	||
			type == STONE_MARKET_PLACE		||
			type == WOODEN_BUILDING			||
			type == WOODEN_BUILDING_1_LEVEL	||
			type == WOODEN_BUILDING_2_LEVEL	||
			type == WOODEN_BUILDING_3_LEVEL	||
			type == WOODEN_BUILDING_4_LEVEL	||
			type == ROWHOUSE_WALL		||
			type == ROWHOUSE_WALL_1_LEVEL	||
			type == ROWHOUSE_WALL_2_LEVEL	||
			type == ROWHOUSE_WALL_3_LEVEL	||
			type == ROWHOUSE_WALL_4_LEVEL	||
			type == WOODEN_FACTORY_1_5_LEVEL	||
			type == WOODEN_FACTORY_2_5_LEVEL	||
			type == WOODEN_MARKET_PLACE		||
			type == MAUSOLEUM			||
			type == BEDOUIN_CAMP		||
			type == BUILDING_CLUSTER	||
			type == HUTS			||
			type == TEMPLE			||
			type == TOWER_1_LEVEL_OBSTACLE ||
			type == TOWER_2_LEVEL_OBSTACLE ||
			type == TOWER_3_LEVEL_OBSTACLE ||
			type == TOWER_1_LEVEL_HINDRANCE ||
			type == TOWER_2_LEVEL_HINDRANCE ||
			type == TOWER_3_LEVEL_HINDRANCE){

			return true;
		}
		else{
			return false;
		}
	}

	public boolean isMarketPlaceTerrain(){
		if (type == STONE_MARKET_PLACE || type == WOODEN_MARKET_PLACE){

			return true;
		}
		else{
			return false;
		}
	}

	public boolean isFactoryTerrain()	 {
		if (type == STONE_FACTORY_1_5_LEVEL		||
			type == STONE_FACTORY_2_5_LEVEL		||
			type == WOODEN_FACTORY_1_5_LEVEL		||
			type == WOODEN_FACTORY_2_5_LEVEL		||
			type == STONE_FACTORY_WALL_1_5_LEVEL	||
			type == STONE_FACTORY_WALL_2_5_LEVEL	||
			type == WOODEN_FACTORY_WALL_1_5_LEVEL	||
			type == WOODEN_FACTORY_WALL_2_5_LEVEL){

			return true;
		}
		else{
			return false;
		}
	}

	public  boolean isOpenTerrain(){

		if (height == 0 && !halfLevelHeight) {
			return true;
		}
		else {
			return false;
		}
	}

	public  boolean isDepressionTerrain(){
		if (type == GULLY			||
			type == DRY_STREAM	||
			type == SHALLOW_STREAM	||
			type == DEEP_STREAM	||
			type == SUNKEN_ROAD	||
			type == WADI){

			return true;
		}
		else {
			return false;
		}
	}

	public  boolean isEntrenchmentTerrain(){
		if (type == FOX_HOLES 		||
			type == TRENCH 		||
			type == ANTITANK_TRENCH	||
			type == SANGAR){
			return true;
		}
		else {
			return false;
		}
	}

	public  boolean isBridge(){
		if (type ==  SINGLE_HEX_WOODEN_BRIDGE ||
			type == SINGLE_HEX_STONE_BRIDGE ||
			type ==  WOODEN_BRIDGE ||
			type == STONE_BRIDGE ||
			type == PONTOON_BRIDGE ||
			type == FOOT_BRIDGE){
			return true;
		}
		else {
			return false;
		}
	}

	public  boolean isTunnelTerrain(){
		if (type ==  TUNNEL || type == SEWER || type == CAVE){
			return true;
		}
		else {
			return false;
		}
	}

	public  boolean isRoadTerrain(){
		if (type ==  DIRT_ROAD ||
			type == PAVED_ROAD ||
			type == ELEVATED_ROAD ||
			type == SUNKEN_ROAD||
			type == RUNWAY){
			return true;
		}
		else {
			return false;
		}
	}

	public  boolean isWoodsTerrain(){
		if (type ==  WOODS ||
			type == PINE_WOODS ||
			type == LIGHT_JUNGLE ||
			type == DENSE_JUNGLE ){

			return true;
		}
		else {
			return false;
		}
	}

	public  boolean isWaterTerrain(){
		if (type ==  WATER 		||
			type == SHALLOW_WATER	||
			type == SHALLOW_STREAM	||
			type == DEEP_STREAM	||
			type == FLOODED_STREAM	||
			type == RIVER 		||
			type == SHALLOW_RIVER 	||
			type == FORD	 	||
			type == OCEAN 		||
			type == SHALLOW_OCEAN	||
			type == CANAL		||
			type == SUBMERGED_REEF){

			return true;
		}
		else {
			return false;
		}
	}

	public  boolean isStreamTerrain(){
		if (type == SHALLOW_STREAM	||
			type == DEEP_STREAM){

			return true;
		}
		else {
			return false;
		}
	}

	public static final Terrain getTerrain(String name) {

		// load terrain list if necessary
		if (terrainList == null){

			terrainList = getTerrainList();
		}

		for (int x = 0; x < terrainList.length; x++){
			if (terrainList[x] != null && terrainList[x].getName().equals(name)){

				return terrainList[x];
			}
		}
		return null;
	}
	
	/**
	 * Return the first Terrain object matching the specified color.
	 * @param color
	 * @return
	 */
	public static Terrain getTerrain(Color color) {
	  // load terrain list if necessary
	  if (terrainList == null){
		terrainList = getTerrainList();
      }
	  
	  Terrain terrain = terrainList[0];
	  
	  if (color.equals(OPEN_GROUND_COLOR)) {
	    return terrain;
	  } 
	  else if (color.equals(LEVEL1_COLOR)) {
	    terrain.height = 1;
	    return terrain;
	  } 
	  else if (color.equals(LEVEL2_COLOR)) {
	    terrain.height = 2;
	    return terrain;
	  } 
	  else if (color.equals(LEVEL3_COLOR)) {
	    terrain.height = 3;
	    return terrain;
	  } 
	  else if (color.equals(LEVEL4_COLOR)) {
	    terrain.height = 4;
	    return terrain;
	  } 
	  else if (color.equals(LEVEL5_COLOR)) {
	    terrain.height = 5;
	    return terrain;
	  } 
	  else if (color.equals(LEVEL6_COLOR)) {
	    terrain.height = 6;
	    return terrain;
	  } 
	  else if (color.equals(LEVEL7_COLOR)) {
	    terrain.height = 7;
	    return terrain;
	  } 
	  else if (color.equals(LEVEL8_COLOR)) {
	    terrain.height = 8;
	    return terrain;
	  } 
	  else if (color.equals(LEVEL9_COLOR)) {
	    terrain.height = 9;
	    return terrain;
	  } 
	  else if (color.equals(LEVEL10_COLOR)) {
	    terrain.height = 10;
	    return terrain;
	  } 
	    
	  for (int i = 0; i < terrainList.length; i++) {
	    if (terrainList[i] != null) {
	      if (terrainList[i].getMapColor().equals(color)) {
	        return terrainList[i];
	      }
	    }
	  }
	  return null;
	}

	public  boolean isInherentTerrain(){
		if (type == CRAGS					||
			type == GRAVE_YARD				||
			type == ORCHARD					||
			type == ORCHARD_OUT_OF_SEASON	||
			type == STONE_RUBBLE			||
			type == WOODEN_RUBBLE		||
			type == HILLOCK				||
			type == HILLOCK_SUMMIT		||
			type == CACTUS_PATCH		|
			type == OLIVE_GROVE			||
			type == VINEYARD	 		||
			type == BEDOUIN_CAMP 		||
			type == BUILDING_CLUSTER	||
			type == DENSE_JUNGLE		||
			type == BAMBOO				||
			type == PALM_TREES			||
			type == RICE_PADDY_IN_SEASON ||
			type == TOWER_1_LEVEL_HINDRANCE ||
			type == TOWER_2_LEVEL_HINDRANCE ||
			type == TOWER_3_LEVEL_HINDRANCE){

			return true;
		}
		else {
			return false;
		}
	}

	public  boolean isRicePaddy(){
		if (type == RICE_PADDY_DRAINED		||
			type == RICE_PADDY_IN_SEASON	||
			type == RICE_PADDY_IRRIGATED){

			return true;
		}
		else {
			return false;
		}
	}
	
public static void resetTerrain(){
		
		terrainList = null;
		Terrain terrainList[] = getTerrainList();
	}
}


