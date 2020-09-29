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

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Title:        Hex.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class Hex
	implements Serializable {

	// serial ID: version 1.0.0
	static final long serialVersionUID = 000100L;

	// static constant variables
	public 		static double HEIGHT	= 64.5;
	private		static double y			= HEIGHT/2;
	private		static double h			= y/Math.cos(Math.toRadians(30)) * 1.00727476217123670956911024062675;
	private		static double x			= h/2;
	public		static double WIDTH		= h + x;

	// Property variables
	private	String	name			= null;
	private	int		columnNumber	= 0;
	private	int		rowNumber		= 0;
	private	int		baseHeight		= 0;
	private	boolean northOnMap		= true;
	private	boolean northEastOnMap  = true;
	private	boolean southEastOnMap  = true;
	private	boolean southOnMap		= true;
	private	boolean southWestOnMap  = true;
	private	boolean northWestOnMap  = true;

	// the map
	GameMap	map;

	// geometeric variables
	private	Point   center;
	private	Polygon hexBorder 		= new Polygon();
	private	Polygon extendedHexBorder	= new Polygon();

	// location variables (center and each hexside)
	private	Location hexsideLocations[] = new Location[6];
	private	Location centerLocation;

	// terrain variables
	private	Terrain edgeTerrain[]  = new Terrain[6];
	private	boolean edgeHasCliff[]  = new boolean[6];

	// Bridge
	private	Bridge	bridge;

	// other stuff
	private	boolean stairway = false;
	private	boolean ford = false;

	// constructors
	public Hex(
		int	col,
		int   row,
		GameMap map,
		int	 baseHgt,
		Terrain terr) {

		baseHeight   = baseHgt;
		columnNumber = col;
		rowNumber	= row;
		this.map	= map;
		initHex(terr, map);
	}

	// initialize the hex
	public void initHex(
		Terrain terr,
		GameMap map){

		boolean lastCol	= false;
		boolean lastRow	= false;

		double[] vertexPointsX  =	{-h/2, h/2, h/2+x, h/2, -h/2, -h/2-x};
		double[] vertexPointsY  =	{0,    0,   y,     2*y, 2*y,	y};

		double[] extendedVertexPointsX  =	{-h/2-1, h/2+1, h/2+x+1, h/2+1, -h/2-1, -h/2-x-1};
		double[] extendedVertexPointsY  =	{-1,     -1,    y,       2*y+1, 2*y+1,  y};

		double[] hexsidePointsX  =	{0, (h+x)/2-1, (h+x)/2-1, 0,     -(h+x)/2, -(h+x)/2};
		double[] hexsidePointsY  =	{1, y/2+1,      y/2+y-1,  2*y-1, y/2+y-1, y/2+1};

		// set name
		int  rowName = rowNumber + (columnNumber%2 == 0 ? 1 : 0);
		if (columnNumber < 26) {

			char chars[] = {' '};

			// geomorphic board numbering
			chars[0]	 =  (char) ('A' + columnNumber%26);
			name		 =  new String(chars);
			name		 += rowName;
		}
		else {
			char chars[] = {' ', ' '};

			// geomorphic board numbering
			chars[0] =  (char) ('A' + (columnNumber)%26);
			chars[1] =  chars[0];
			name		 =  new String(chars);
			name		 += rowName;
		}

		// set "on map" flags
		// first column?
		if (columnNumber == 0) {

			southWestOnMap  = false;
			northWestOnMap  = false;
		}

		// last column?
		if (columnNumber + 1 == map.getWidth()) {

			southEastOnMap  = false;
			northEastOnMap  = false;
			lastCol = true;
		}

		// first hex in odd column?
		if ((columnNumber%2 == 1) &&  (rowNumber == 0)){

			northOnMap  = false;
			northEastOnMap  = false;
			northWestOnMap  = false;
		}

		// last hex in odd column?
		if ((columnNumber%2 == 1) &&  (rowNumber == map.getHeight())){

			southOnMap  = false;
			southEastOnMap  = false;
			southWestOnMap  = false;
			lastRow = true;
		}

		// set last row for even columns
		else if ((columnNumber%2 == 0) &&  (rowNumber == map.getHeight() - 1)){

			lastRow = true;
		}

		// translate and create the hex borders, center, hexsides (ensure they are on the map)
		double deltaX = (columnNumber * WIDTH);
		double deltaY = (rowNumber	* HEIGHT) - (columnNumber%2 == 1 ? HEIGHT/2 : 0);

		Point hexsidePoints[]		= new Point[6];
		Point vertexPoints[] 		= new Point[6];
		Point extendedVertexPoints[]	= new Point[6];

		center 	= new Point((int)deltaX + (lastCol ? -1 : 0), (int)(deltaY + HEIGHT/2) + (lastRow ? -1 : 0));
		int verticleOffset 	= 0;
		for (int i = 0; i < 6; i++){

			// do we need to tweek points to keep them on the map?
			if (lastRow && columnNumber%2 == 0 && (i == 3 || i == 4)) {

				verticleOffset = -1;
			}
			else if (lastRow && columnNumber%2 == 1 && i != 0 && i != 1) {

				verticleOffset = -1;
			}
			else {
				verticleOffset = 0;
			}

			vertexPoints[i] = new Point(
				(int)(vertexPointsX[i] + deltaX),
				(int)(vertexPointsY[i] + deltaY + verticleOffset));
			extendedVertexPoints[i]	= new Point(
				(int)(extendedVertexPointsX[i] + deltaX),
				(int)(extendedVertexPointsY[i] + deltaY + verticleOffset));
			hexsidePoints[i] = new Point(
				(int)(hexsidePointsX[i] + deltaX + (lastCol ? -1 : 0)),
				(int)(hexsidePointsY[i] + deltaY));

			hexBorder.addPoint((int)vertexPoints[i].getX(), (int)vertexPoints[i].getY());
			extendedHexBorder.addPoint((int)extendedVertexPoints[i].getX(), (int)extendedVertexPoints[i].getY());
		}

		// create center and hexside locations
		centerLocation = new Location(
			name,
			baseHeight,
			center,
			center,
			center,
			this,
			false,
			terr
		);

		hexsideLocations[0] = new Location(
			name + ":North",
			baseHeight,
			new Point((int)vertexPoints[0].getX(),
					  (int)vertexPoints[0].getY()),
			new Point((int)vertexPoints[1].getX(),
					  (int)vertexPoints[1].getY()),
			new Point((int)hexsidePoints[0].getX(),
					  (int)hexsidePoints[0].getY()),
			this,
			false,
			terr
		);

		hexsideLocations[1] = new Location(
			name +  ":NorthEast",
			baseHeight,
			new Point((int)vertexPoints[1].getX(),
					  (int)vertexPoints[1].getY()),
			new Point((int)vertexPoints[2].getX(),
					  (int)vertexPoints[2].getY()),
			new Point((int)hexsidePoints[1].getX(),
					  (int)hexsidePoints[1].getY()),
			this,
			false,
			terr
		);

		hexsideLocations[2] = new Location(
			name +  ":SouthEast",
			baseHeight,
			new Point((int)vertexPoints[2].getX(),
					  (int)vertexPoints[2].getY()),
			new Point((int)vertexPoints[3].getX(),
					  (int)vertexPoints[3].getY()),
			new Point((int)hexsidePoints[2].getX(),
					  (int)hexsidePoints[2].getY()),
			this,
			false,
			terr
		);

		hexsideLocations[3] = new Location(
			name +  ":South",
			baseHeight,
			new Point((int)vertexPoints[3].getX(),
					  (int)vertexPoints[3].getY()),
			new Point((int)vertexPoints[4].getX(),
					  (int)vertexPoints[4].getY()),
			new Point((int)hexsidePoints[3].getX(),
					  (int)hexsidePoints[3].getY()),
			this,
			false,
			terr
		);

		hexsideLocations[4] = new Location(
			name +  ":SouthWest",
			baseHeight,
			new Point((int)vertexPoints[4].getX(),
					  (int)vertexPoints[4].getY()),
			new Point((int)vertexPoints[5].getX(),
					  (int)vertexPoints[5].getY()),
			new Point((int)hexsidePoints[4].getX(),
					  (int)hexsidePoints[4].getY()),
			this,
			false,
			terr
		);

		hexsideLocations[5] = new Location(
			name +  ":NorthWest",
			baseHeight,
			new Point((int)vertexPoints[5].getX(),
					  (int)vertexPoints[5].getY()),
			new Point((int)vertexPoints[0].getX(),
					  (int)vertexPoints[0].getY()),
			new Point((int)hexsidePoints[5].getX(),
					  (int)hexsidePoints[5].getY()),
			this,
			false,
			terr
		);
	}

	// used to update the hexside location once the map has been fully initialized
	public void resetHexsideLocationNames(){

		if (map.getAdjacentHex(this, 0) != null) hexsideLocations[0].setName(name + "/" + map.getAdjacentHex(this, 0).getName());
		if (map.getAdjacentHex(this, 1) != null) hexsideLocations[1].setName(name + "/" + map.getAdjacentHex(this, 1).getName());
		if (map.getAdjacentHex(this, 2) != null) hexsideLocations[2].setName(name + "/" + map.getAdjacentHex(this, 2).getName());
		if (map.getAdjacentHex(this, 3) != null) hexsideLocations[3].setName(name + "/" + map.getAdjacentHex(this, 3).getName());
		if (map.getAdjacentHex(this, 4) != null) hexsideLocations[4].setName(name + "/" + map.getAdjacentHex(this, 4).getName());
		if (map.getAdjacentHex(this, 5) != null) hexsideLocations[5].setName(name + "/" + map.getAdjacentHex(this, 5).getName());
	}

	// get the map
	public GameMap getMap() { return map;}

	// bridge methods
	public Bridge  getBridge(){ return bridge;}
	public void    removeBridge(){ bridge = null;}
	public void    setBridge(Bridge bridge){

		this.bridge = bridge;

		// create the new bridge location
		Location l = new Location(
			name + ":Bridge",
			bridge.getRoadLevel() - baseHeight,
			new Point((int) center.getX(), (int)center.getY()),
			new Point((int) center.getX(), (int)center.getY()),
			new Point((int) center.getX(), (int)center.getY()),
			this,
			false,
			bridge.getTerrain()
		);
		bridge.setLocation(l);

		// set the location up/down pointers
		l.setDownLocation(centerLocation);
		centerLocation.setUpLocation(l);
	}

	public boolean hasBridge(){
		if (bridge != null){
			return true;
		}
		else {
			return false;
		}
	}

	// Property methods
	public boolean isSouthEastOnMap()   {return southEastOnMap;}
	public void 	setSouthEastOnMap(boolean newSouthEastOnMap) {
		southEastOnMap = newSouthEastOnMap;
	}

	public boolean isSouthOnMap()	   {return southOnMap;}
	public void 	setSouthOnMap(boolean newSouthOnMap) {
		southOnMap = newSouthOnMap;
	}

	public boolean isSouthWestOnMap()   {return southWestOnMap;}
	public void 	setSouthWestOnMap(boolean newSouthWestOnMap) {
		southWestOnMap = newSouthWestOnMap;
	}

	public boolean 	isNorthEastOnMap() {return northEastOnMap;}
	public void 	setNorthEastOnMap(boolean newNorthEastOnMap) {
		northEastOnMap = newNorthEastOnMap;
	}

	public boolean	isNorthOnMap(){return northOnMap;}
	public void 	setNorthOnMap(boolean newNorthOnMap) {
		northOnMap = newNorthOnMap;
	}

	public boolean 	isNorthWestOnMap()   {return northWestOnMap;}
	public void 	setNorthWestOnMap(boolean newNorthWestOnMap) {
		northWestOnMap = newNorthWestOnMap;
	}

	public boolean 	isHexsideOnMap(int hexside)   {

		switch (hexside){

		    case 0 : return isNorthOnMap();
		    case 1 : return isNorthEastOnMap();
		    case 2 : return isSouthEastOnMap();
		    case 3 : return isSouthOnMap();
		    case 4 : return isSouthWestOnMap();
		    case 5 : return isNorthWestOnMap();

		    default: return false;
		}
	}


	public int		getColumnNumber() {return columnNumber;}
	public void 	setColumnNumber(int newColumnNumber) {columnNumber = newColumnNumber;
	}

	public void		setStairway(boolean stairway){

		// ignore non-building terrains
		if(centerLocation.getTerrain().isBuildingTerrain()){
			this.stairway = stairway;
		}
	}
	public boolean hasStairway(){ return stairway;}

	public void		setFord(boolean ford){this.ford = ford;}
	public boolean	hasFord(){ return ford;}

	public boolean hasTunnel(){
		if (getTunnelLocation() == null){

			return false;
		}
		else {
			return true;
		}
	}

	public Location getTunnelLocation(){

		Location l = centerLocation;

		// check the location below the center location
		if (l.getDownLocation() != null){

			l = centerLocation.getDownLocation();
			if (l.getTerrain().isTunnelTerrain()){

				return l;
			}

			// check the next location down
			if (l.getDownLocation() != null){

				l = l.getDownLocation();
				if (l.getTerrain().isTunnelTerrain()){

					return l;
				}
			}
		}

		return null;
	}

	public void	addTunnel(Terrain terr){

		// create location
		Location l = new Location(
			centerLocation.getName() + " " + terr.getName(),
			-1,
			centerLocation.getLOSPoint(),
			centerLocation.getLOSPoint(),
			null,
			this,
			false,
			terr
		);

		// set links
		if (this.hasEntrenchment()){

			centerLocation.getDownLocation().setDownLocation(l);
			l.setUpLocation(centerLocation.getDownLocation());

		}
		else {
			centerLocation.setDownLocation(l);
			l.setUpLocation(centerLocation);
		}
	}

	public void	removeTunnel(){

		Location l = getTunnelLocation();

		if (l != null){

			l.getUpLocation().setDownLocation(null);
		}
	}

	public boolean hasEntrenchment(){
		if (getEntrenchmentLocation() == null){

			return false;
		}
		else {
			return true;
		}
	}

	public Location getEntrenchmentLocation(){

		Location l = centerLocation.getDownLocation();
		if (l != null && (l.getTerrain().isEntrenchmentTerrain())){

			return l;
		}

		return null;
	}

	public void	addEntrenchment(Terrain terr){

		// create location
		Location l = new Location(
			centerLocation.getName() + " " + terr.getName(),
			0,
			centerLocation.getLOSPoint(),
			centerLocation.getLOSPoint(),
			null,
			this,
			false,
			terr
		);

		// get tunnel location
		Location tunnel = getTunnelLocation();

		// set links
		centerLocation.setDownLocation(l);
		l.setUpLocation(centerLocation);

		// set the links to tunnel, if necessary
		if (tunnel != null){

			tunnel.setUpLocation(l);
			l.setDownLocation(tunnel);
		}
	}

	public void	removeEntrenchment(){

		// get tunnel location
		Location tunnel = getTunnelLocation();

		if(tunnel != null){

			centerLocation.setDownLocation(tunnel);
			tunnel.setUpLocation(centerLocation);
		}
		else {
			centerLocation.setDownLocation(null);
		}
	}

	public String   getName(){return name;}
	public void 	setName(String newName) {name = newName;}

	public int	  getRowNumber()			{return rowNumber;}
	public Polygon  getHexBorder()			{return hexBorder;}
	public Polygon  getExtendedHexBorder()	{return extendedHexBorder;}
	public Point	getHexCenter()			{return center;}
	public int	  getXOrigin()			{return hexBorder.xpoints[0];}
	public int	  getYOrigin()			{return hexBorder.ypoints[0];}
	public int	  getBaseHeight()			{return baseHeight;}

	public Location getCenterLocation() { return centerLocation;}
	public void 	setCenterLocation(Location newCenterLocation) {
		centerLocation = newCenterLocation;
	}

	public void setHexBorder(Polygon newHexBorder) {
		hexBorder = newHexBorder;
	}

	public void setExtendedHexBorder(Polygon newHexBorder) {
		extendedHexBorder = newHexBorder;
	}

	public void setRowNumber(int newRowNumber) {
		rowNumber = newRowNumber;
	}

	public int getOppositeHexside(int hexside){

		switch (hexside){
			case 0: return 3;
			case 1: return 4;
			case 2: return 5;
			case 3: return 0;
			case 4: return 1;
			case 5: return 2;
		}
		return -1;
	}

	public int getClockwiseHexside(int hexside){

		switch (hexside){
			case 0: return 1;
			case 1: return 2;
			case 2: return 3;
			case 3: return 4;
			case 4: return 5;
			case 5: return 0;
		}
		return -1;
	}

	public int getCounterClockwiseHexside(int hexside){

		switch (hexside){
			case 0: return 5;
			case 1: return 0;
			case 2: return 1;
			case 3: return 2;
			case 4: return 3;
			case 5: return 4;
		}
		return -1;
	}

	// location methods
	public boolean isCenterLocation(Location l) {

		Location cent = centerLocation;

		// center, up locations
		while(cent != null){
			if (l == cent){
				return true;
			}
			else {
				cent = cent.getUpLocation();
			}
		}

		// down locations
		cent = centerLocation.getDownLocation();
		while(cent != null){
			if (l == cent){
				return true;
			}
			else {
				cent = cent.getDownLocation();
			}
		}
		return false;
	}

	public int getLocationHexside(Location l){

		for(int x = 0; x < 6; x++) {
			if (l == hexsideLocations[x])
				return x;
		}
		return -1;
	}

	public int getHexspine(Point LOSPoint){

		for(int x = 0; x < 6; x++) {
			if (hexsideLocations[x].getLOSPoint().getX() == LOSPoint.getX() &&
				hexsideLocations[x].getLOSPoint().getY() == LOSPoint.getY()) {

				return x;
			}
		}
		return -1;
	}

	public boolean isHexsideLocation(Location l) {

		return !isCenterLocation(l);
	}

	public boolean isGroundlevelLocation(Location l){

		if (l == centerLocation) return true;

		for(int x = 0; x < 6; x++) {
			if (l == hexsideLocations[x])	return true;
		}

		return false;
	}

	public void setDepressionTerrain(Terrain terr) {

		// change the depression terrain in the center location
		centerLocation.setDepressionTerrain(terr);

		// if were removing the depression terrain, ensure all hexside
		// depression terrain is also removed
		if (terr == null) {
			for(int x = 0; x < 6; x++){
				hexsideLocations[x].setDepressionTerrain(terr);
			}
		}
	}

	public void setHexsideDepressionTerrain(int side) {

		// change the depression terrain in the hexside location
		hexsideLocations[side].setDepressionTerrain(centerLocation.getDepressionTerrain());
	}

	public boolean isDepressionTerrain() {

		return centerLocation.isDepressionTerrain();
	}

	public Terrain getTerrain() {

		return centerLocation.getTerrain();
	}

	public void setTerrain(Terrain terr) {

		// location variables
		Location	l = null, l2 = null, l3 = null, l4 = null;

		// save/set the center location
		Terrain oldCenterTerrain = centerLocation.getTerrain();
		centerLocation.setBypassAllowed(false);

		// If adding road or path, leave center terrain as is
		if (!((terr.isRoadTerrain() || terr.getType() == Terrain.PATH) && !oldCenterTerrain.isOpenTerrain())){

			centerLocation.setTerrain(terr);
		}

		// setup market place locations
		if (terr.getType() == Terrain.WOODEN_MARKET_PLACE ||
			terr.getType() == Terrain.STONE_MARKET_PLACE){

			// set center location to open ground
			centerLocation.setTerrain(Terrain.getTerrainList()[Terrain.OPEN_GROUND]);

			l = new Location(
				centerLocation.getName() + " Level 1",
				1,
				centerLocation.getLOSPoint(),
				centerLocation.getLOSPoint(),
				null,
				this,
				false,
				terr
			);

			centerLocation.setUpLocation(l);
			l.setDownLocation(centerLocation);
		}

		// add/remove vertical building locations
		if (terr.getType() == Terrain.WOODEN_BUILDING_1_LEVEL ||
		   	terr.getType() == Terrain.STONE_BUILDING_1_LEVEL  ||
		   	terr.getType() == Terrain.WOODEN_BUILDING_2_LEVEL ||
		   	terr.getType() == Terrain.STONE_BUILDING_2_LEVEL  ||
		   	terr.getType() == Terrain.WOODEN_BUILDING_3_LEVEL ||
		   	terr.getType() == Terrain.WOODEN_BUILDING_4_LEVEL ||
		   	terr.getType() == Terrain.STONE_BUILDING_4_LEVEL ||
		   	terr.getType() == Terrain.STONE_BUILDING_3_LEVEL){

			l = new Location(
				centerLocation.getName() + " Level 1",
				1,
				centerLocation.getLOSPoint(),
				centerLocation.getLOSPoint(),
				null,
				this,
				false,
				terr
			);
			centerLocation.setUpLocation(l);
			l.setDownLocation(centerLocation);
		}
		if(	terr.getType() == Terrain.WOODEN_BUILDING_2_LEVEL ||
		   	terr.getType() == Terrain.STONE_BUILDING_2_LEVEL  ||
		   	terr.getType() == Terrain.WOODEN_BUILDING_4_LEVEL ||
		   	terr.getType() == Terrain.STONE_BUILDING_4_LEVEL ||
		   	terr.getType() == Terrain.WOODEN_BUILDING_3_LEVEL ||
		   	terr.getType() == Terrain.STONE_BUILDING_3_LEVEL){

			l2 = new Location(
				centerLocation.getName() + " Level 2",
				2,
				centerLocation.getLOSPoint(),
				centerLocation.getLOSPoint(),
				null,
				this,
				false,
				terr
			);

			l.setUpLocation(l2);
			l2.setDownLocation(l);
		}
		if ( 	terr.getType() == Terrain.WOODEN_BUILDING_3_LEVEL ||
		   	terr.getType() == Terrain.WOODEN_BUILDING_4_LEVEL ||
		   	terr.getType() == Terrain.STONE_BUILDING_4_LEVEL ||
		   	terr.getType() == Terrain.STONE_BUILDING_3_LEVEL){

			l3 = new Location(
				centerLocation.getName() + " Level 3",
				3,
				centerLocation.getLOSPoint(),
				centerLocation.getLOSPoint(),
				null,
				this,
				false,
				terr
			);

			l2.setUpLocation(l3);
			l3.setDownLocation(l2);
		}

		if ( 	terr.getType() == Terrain.WOODEN_BUILDING_4_LEVEL ||
		   	terr.getType() == Terrain.STONE_BUILDING_4_LEVEL){

			l4 = new Location(
				centerLocation.getName() + " Level 4",
				4,
				centerLocation.getLOSPoint(),
				centerLocation.getLOSPoint(),
				null,
				this,
				false,
				terr
			);

			l3.setUpLocation(l4);
			l4.setDownLocation(l3);
		}

		// set inherent stairway
		if (terr.getType() == Terrain.WOODEN_BUILDING_1_LEVEL ||
		   	terr.getType() == Terrain.STONE_BUILDING_1_LEVEL){

			stairway = true;
		}
		else {
			stairway = false;
		}

		// set shellhole location
		if (terr.getType() == Terrain.SHELL_HOLES){

			l = new Location(
				centerLocation.getName() + " " + terr.getName(),
				0,
				centerLocation.getLOSPoint(),
				centerLocation.getLOSPoint(),
				null,
				this,
				false,
				terr
			);
			centerLocation.setDownLocation(l);
			centerLocation.setTerrain(oldCenterTerrain);
			l.setUpLocation(centerLocation);
		}

		// add rice paddy bank location
		if (terr.isRicePaddy()){

			l = new Location(
				centerLocation.getName() + " Paddy Bank",
				0,
				centerLocation.getLOSPoint(),
				centerLocation.getLOSPoint(),
				null,
				this,
				false,
				Terrain.getTerrainList()[Terrain.RICE_PADDY_BANK]
			);

			centerLocation.setUpLocation(l);
			l.setDownLocation(centerLocation);
		}

		// reset the bypass flags
		setBypassFlags();
	}

	public void setBypassFlags() {

		Terrain terr = centerLocation.getTerrain();

		// set the bypass flags
		if(terr.isBuildingTerrain() || terr.isWoodsTerrain()){
			for(int x = 0; x < 6; x++){

				if(hexsideLocations[x].getTerrain() != terr){
					hexsideLocations[x].setBypassAllowed(true);
				}
				else {
					hexsideLocations[x].setBypassAllowed(false);
				}
			}
		}
		else {
			for(int x = 0; x < 6; x++){
				hexsideLocations[x].setBypassAllowed(false);
			}
		}
	}

	public Terrain getEdgeTerrain(int side) {

		return edgeTerrain[side];
	}

	public boolean hasCliff(int side) {

		return edgeHasCliff[side];
	}

	public void setEdgeTerrain(int side, Terrain terr) {

		// removing?
		if (terr == null){
			edgeTerrain[side] = null;
			edgeHasCliff[side] = false;
			return;
		}

		int terrType = terr.getType();
		Terrain[] terrainList = Terrain.getTerrainList();

		// set cliff
		if (terrType == Terrain.CLIFF){

			edgeHasCliff[side] = true;
		}

		// map rowhouse wall to appropriate terrain, if possible
		else if(terrType == Terrain.ROWHOUSE_WALL ||
			terrType == Terrain.ROWHOUSE_WALL_1_LEVEL ||
			terrType == Terrain.ROWHOUSE_WALL_2_LEVEL ||
			terrType == Terrain.ROWHOUSE_WALL_3_LEVEL){

			switch(hexsideLocations[side].getTerrain().getType()){

				case Terrain.WOODEN_BUILDING:
				case Terrain.STONE_BUILDING:

					edgeTerrain[side] = terrainList[Terrain.ROWHOUSE_WALL];
					break;

				case Terrain.WOODEN_BUILDING_1_LEVEL:
				case Terrain.STONE_BUILDING_1_LEVEL:

					edgeTerrain[side] = terrainList[Terrain.ROWHOUSE_WALL_1_LEVEL];
					break;

				case Terrain.WOODEN_BUILDING_2_LEVEL:
				case Terrain.STONE_BUILDING_2_LEVEL:

					edgeTerrain[side] = terrainList[Terrain.ROWHOUSE_WALL_2_LEVEL];
					break;

				case Terrain.WOODEN_BUILDING_3_LEVEL:
				case Terrain.STONE_BUILDING_3_LEVEL:

					edgeTerrain[side] = terrainList[Terrain.ROWHOUSE_WALL_3_LEVEL];
					break;

				default:
					edgeTerrain[side] = terr;
			}
		}
		else {
			edgeTerrain[side] = terr;
		}
	}

	public Terrain getHexsideTerrain(int hexside){

		return hexsideLocations[hexside].getTerrain();
	}

	public Location getHexsideLocation(int hexside){

		return hexsideLocations[hexside];
	}

	public void setHexsideTerrain(int hexside, Terrain terr){

		hexsideLocations[hexside].setTerrain(terr);

		// set the bypass flag
		if((centerLocation.getTerrain().isBuildingTerrain() ||
			centerLocation.getTerrain().isWoodsTerrain()) &&
			centerLocation.getTerrain() != terr){

			hexsideLocations[hexside].setBypassAllowed(true);
		}
		else {
			hexsideLocations[hexside].setBypassAllowed(false);
		}
	}

	public void setBaseHeight(int hgt) {

		baseHeight = hgt;
	}

	// geometric methods
	public boolean  contains(int x, int y)			{return hexBorder.contains(x, y);}
	public boolean  containsExtended(int x, int y)	{return extendedHexBorder.contains(x, y);}
	public boolean  contains(Point p)				{return hexBorder.contains(p);}
	public boolean  containsExtended(Point p)		{return extendedHexBorder.contains(p);}

	// nearest vertex point
	public Point nearestVertex(int x, int y) {

		// get distance to center
		double  distance	 = Point.distance(x, y, center.getX(), center.getY());
		Point   currentPoint = new Point(
				(int) center.getX(),
				(int) center.getY());
		double  nextDistance;

		// compare distance to center to distances to vertixes
		for(int vert = 0; vert < 6; vert++) {

			nextDistance = Point.distance(
				x,
				y,
				hexBorder.xpoints[vert],
				hexBorder.ypoints[vert]
			 );

			// vertex is closer?
			if (nextDistance < distance) {

				distance = nextDistance;
				currentPoint.setLocation(
					hexBorder.xpoints[vert],
					hexBorder.ypoints[vert]
				);
			}
		}

		return currentPoint;
	}

	// nearest Hexside aiming point
	public Location nearestLocation(int x, int y) {

		// get distance to center
		double  distance	 = Point.distance(
				x,
				y,
				(int) centerLocation.getLOSPoint().getX(),
				(int) centerLocation.getLOSPoint().getY());
		Location currentLocation = centerLocation;
		double   nextDistance;

		// compare distance to center to distances to vertixes
		for(int side = 0; side < 6; side++) {

			// screen out locations off the map
			if (side == 0 && northOnMap 	||
				side == 1 && northEastOnMap 	||
				side == 2 && southEastOnMap 	||
				side == 3 && southOnMap	 	||
				side == 4 && southWestOnMap 	||
				side == 5 && northWestOnMap){

				nextDistance = Point.distance(
					x,
					y,
					(int) hexsideLocations[side].getEdgeCenterPoint().getX(),
					(int) hexsideLocations[side].getEdgeCenterPoint().getY()
				 );

				// side is closer?
				if (nextDistance < distance) {

					distance = nextDistance;
		  			currentLocation = hexsideLocations[side];
				}
			}
		}

		return currentLocation;
	}

	// is a point on the map edge terrain in an adjacent hex?
	public boolean isAdjacentEdgeTerrain(int x, int y, GameMap map) {

		//find the location
		Location loc = map.gridToHex(x, y).nearestLocation(x, y);

		if (1 ==1) {

			return true;
		}
		else {
			return false;
		}
	}

	// is the hex touched by the given rectangle
	public boolean isTouchedBy(Rectangle rect) {

		if (hexBorder.intersects(rect)) {

			return true;
		}
		else {
			return false;
		}
	}

	// get smoke in hex
	public HashSet<Smoke> getSmoke(){

		return map.getAllSmoke(this);
	}

	// get smoke in hex
	public int getSmokeHindrance(){

		HashSet<Smoke> 	hind 	= map.getAllSmoke(this);
		Iterator<Smoke> 	iter 	= hind.iterator();
		Smoke 		s	 	= null;
		int			total	= 0;

		while(iter.hasNext()){

			s = (Smoke) iter.next();
			total += s.getHindrance();
		}

		return total;
	}

	// change all terrain within hex
	public void changeAllTerrain(Terrain fromTerrain, Terrain toTerrain, Shape s){

		boolean containsCenter =  s.contains(centerLocation.getLOSPoint());

		// change the center location
		if (centerLocation.getTerrain().getType() == fromTerrain.getType() && containsCenter){

			setTerrain(toTerrain);
		}
		if (centerLocation.getDepressionTerrain() != null && centerLocation.getDepressionTerrain().getType() == fromTerrain.getType()  && containsCenter){

			setDepressionTerrain(toTerrain);
		}

		// change the hexside locations
		for (int x = 0; x < 6; x++){

			if (hexsideLocations[x].getTerrain().getType() == fromTerrain.getType() && s.contains(hexsideLocations[x].getEdgeCenterPoint())){

				hexsideLocations[x].setTerrain(toTerrain);
			}
			if (hexsideLocations[x].getDepressionTerrain() != null && hexsideLocations[x].getDepressionTerrain().getType() == fromTerrain.getType() && s.contains(hexsideLocations[x].getEdgeCenterPoint())){

				hexsideLocations[x].setDepressionTerrain(toTerrain);
			}
		}

		// change the edge terrain locations
		for (int x = 0; x < 6; x++){

			if (edgeTerrain[x] != null && edgeTerrain[x].getType() == fromTerrain.getType() && s.contains(hexsideLocations[x].getEdgeCenterPoint())){

				edgeTerrain[x] = toTerrain;
			}
		}

		// reset the bypass flags
		setBypassFlags();
	}

	public void flip(){

		// trasform the hex polygons

		// flip the points in the center location
		flipHexPoint(centerLocation.getEdgeCenterPoint());
		flipHexPoint(centerLocation.getLOSPoint());
		flipHexPoint(centerLocation.getAuxLOSPoint());

		// flip the points in the hexside locations
		for (int x = 0; x < 6; x++){

			flipHexPoint(hexsideLocations[x].getEdgeCenterPoint());
			flipHexPoint(hexsideLocations[x].getLOSPoint());
			flipHexPoint(hexsideLocations[x].getAuxLOSPoint());
		}

		// shuffle the indexes of the hexside variables
		boolean  btemp;
		Location ltemp;
		Terrain  ttemp;
		for (int x = 0; x < 3; x++){

			ltemp = hexsideLocations[x];
			btemp = edgeHasCliff[x];
			ttemp = edgeTerrain[x];
			hexsideLocations[x]     = hexsideLocations[x + 3];
			edgeHasCliff[x]         = edgeHasCliff[x + 3];
			edgeTerrain[x]          = edgeTerrain[x + 3];
			hexsideLocations[x + 3] = ltemp;
			edgeHasCliff[x + 3]     = btemp;
			edgeTerrain[x + 3]      = ttemp;
		}

		// shuffle the "on map" flags
		btemp       = northOnMap;
		northOnMap  = southOnMap;
		southOnMap  = btemp;
		btemp       = northEastOnMap;
		northEastOnMap  = southWestOnMap;
		southWestOnMap  = btemp;
		btemp           = southEastOnMap;
		southEastOnMap  = northWestOnMap;
		northWestOnMap   = btemp;

		// up and down locations
		Location l = centerLocation.getUpLocation();
		while (l != null){

			flipHexPoint(l.getLOSPoint());
			flipHexPoint(l.getAuxLOSPoint());
			l = l.getUpLocation();
		}

		l = centerLocation.getDownLocation();
		while (l != null){

			flipHexPoint(l.getLOSPoint());
			flipHexPoint(l.getAuxLOSPoint());
			l = l.getDownLocation();
		}

		// flip bridge
		if (bridge != null){

			flipHexPoint(bridge.getCenter());
			bridge.setRotation(bridge.getRotation() >= 180 ? bridge.getRotation() - 180 : bridge.getRotation() + 180);
		}
	}

	public void flipHexPoint(Point p){

		p.x = map.gridWidth  - p.x - 1;
		p.y = map.gridHeight - p.y - 1;
	}

	public void copy(Hex h){

/*
	Note: When a "half hex" is being copied, no attempt is made to resolve
	conflicting terrain types or ground level elevations. It is assumed the
	terrain types of the center location are the same and upper/lower levels
	will never be present.



	// location variables (center and each hexside)
	protected Location hexsideLocations[] = new Location[6];
	protected Location centerLocation;

	// terrain variables
	protected Terrain edgeTerrain[]  = new Terrain[6];
	protected boolean edgeHasCliff[]  = new boolean[6];

*/
		// copy hex values
		name 		= h.getName();
		baseHeight	= h.getBaseHeight();

		// copy the center location
		centerLocation.copyLocationValues(h.getCenterLocation());

		// copy upper/lower level locations
		Location current = centerLocation;
		Location source  = h.getCenterLocation().getUpLocation();
		while (source != null){

			// create a new location
			Location temp = new Location (
				source.getName(),
				source.getBaseHeight(),
				(Point) current.getLOSPoint().clone(),
				(Point) current.getAuxLOSPoint().clone(),
				(Point) current.getEdgeCenterPoint().clone(),
				this,
				source.isBypassAllowed(),
				source.getTerrain()
			);
			temp.copyLocationValues(source);

			// set up/down links
			current.setUpLocation(temp);
			temp.setDownLocation(current);

			// increment the pointers
			current = current.getUpLocation();
			source  = source.getUpLocation();
		}
		current = centerLocation;
		source  = h.getCenterLocation().getDownLocation();
		while (source != null){

			// create a new location
			Location temp = new Location (
				source.getName(),
				source.getBaseHeight(),
				(Point) current.getLOSPoint().clone(),
				(Point) current.getAuxLOSPoint().clone(),
				(Point) current.getEdgeCenterPoint().clone(),
				this,
				source.isBypassAllowed(),
				source.getTerrain()
			);
			temp.copyLocationValues(source);

			// set up/down links
			current.setDownLocation(temp);
			temp.setUpLocation(current);

			// increment the pointers
			current = current.getDownLocation();
			source  = source.getDownLocation();
		}

		// set the hexside locations
		if (northOnMap     && h.isNorthOnMap())     {
            hexsideLocations[0].copyLocationValues(h.getHexsideLocation(0));
            edgeTerrain[0]  = h.getEdgeTerrain(0);
	        edgeHasCliff[0] = h.hasCliff(0);
        }
		if (northEastOnMap && h.isNorthEastOnMap()) {
            hexsideLocations[1].copyLocationValues(h.getHexsideLocation(1));
            edgeTerrain[1]  = h.getEdgeTerrain(1);
	        edgeHasCliff[1] = h.hasCliff(1);
        }
		if (southEastOnMap && h.isSouthEastOnMap()) {
            hexsideLocations[2].copyLocationValues(h.getHexsideLocation(2));
            edgeTerrain[2]  = h.getEdgeTerrain(2);
	        edgeHasCliff[2] = h.hasCliff(2);
        }
		if (southOnMap     && h.isSouthOnMap()) {
            hexsideLocations[3].copyLocationValues(h.getHexsideLocation(3));
            edgeTerrain[3]  = h.getEdgeTerrain(3);
	        edgeHasCliff[3] = h.hasCliff(3);
        }
		if (southWestOnMap && h.isSouthWestOnMap()) {
            hexsideLocations[4].copyLocationValues(h.getHexsideLocation(4));
            edgeTerrain[4]  = h.getEdgeTerrain(4);
	        edgeHasCliff[4] = h.hasCliff(4);
        }
		if (northWestOnMap && h.isNorthWestOnMap()) {
            hexsideLocations[5].copyLocationValues(h.getHexsideLocation(5));
            edgeTerrain[5]  = h.getEdgeTerrain(5);
	        edgeHasCliff[5] = h.hasCliff(5);
        }

		// bridges
		if (bridge == null && h.getBridge() != null){

			setBridge(new Bridge(
				h.getBridge().getTerrain(),
				h.getBridge().getRoadLevel(),
				h.getBridge().getRotation(),
				new Location(),
				h.getBridge().isSingleHex(),
				(Point) h.getBridge().getCenter().clone()
			));
		}

		// other stuff
		stairway 	= h.hasStairway();
		ford 		= h.hasFord();
	}
}

