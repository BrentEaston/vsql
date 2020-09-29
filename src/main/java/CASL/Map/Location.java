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
import java.io.Serializable;

/**
 * Title:        Location.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class Location
	implements Serializable {

	// serial ID: version 1.0.0
	static final long serialVersionUID = 000100L;

	// property variables
	private String  	name;
	private int			baseHeight;
	private Point		LOSPoint;
	private Point		auxLOSPoint;	//auxillary LOS point for bypass locations
	private Point		edgeCenterPoint;
	private Hex			hex;
	private boolean 	bypassAllowed	= false;
	private boolean 	trailbreak		= false;
	private boolean 	path			= false;
	private boolean 	breach			= false;
	private boolean 	wire			= false;
	private boolean 	panji			= false;
	private boolean 	cave			= false;
	private boolean 	fire			= false;
	private boolean 	blaze			= false;
	private Terrain 	terrain;
	private Terrain 	depressionTerrain;
	private Location 	upLocation;
	private Location 	downLocation;

	// property methods
	public	String  getName(){return name;}
	public	void 	setName(String newName) {name = newName;}

	// resets the location name when the location is moved (i.e. map flipped, etc)
	public void resetName(){

		name = hex.getName() + name.substring(name.indexOf(':'));
	}

	public	int	 	getBaseHeight() {return baseHeight;}
	public	void	setBaseHeight(int newBaseHeight) {baseHeight = newBaseHeight;}
	public	int	 	getAbsoluteHeight() {return baseHeight + hex.getBaseHeight();}

	public	Terrain getTerrain() {return terrain;}
	public	void 	setTerrain(Terrain newTerrain) {terrain = newTerrain;}

	public	Hex		getHex() {return hex;}
	public	void	setHex(Hex newHex) {hex = newHex;}

	public	Point	getLOSPoint() {return LOSPoint;}
	public	void	setLOSPoint(Point newLOSPoint) {LOSPoint = newLOSPoint;}
	public	Point	getAuxLOSPoint() {return auxLOSPoint;}
	public	void	setAuxLOSPoint(Point newAuxLOSPoint) {auxLOSPoint = newAuxLOSPoint;}
	public	Point	getEdgeCenterPoint() {return edgeCenterPoint;}
	public	void	setEdgeCenterPoint(Point newEdgeCenterPoint) {edgeCenterPoint = newEdgeCenterPoint;}
	public	Point	getUnitLocationPoint() {
		if (edgeCenterPoint == null){
			return LOSPoint;
		}
		else {
			return edgeCenterPoint;
		}
	}

	public	boolean	isBypassAllowed() {return bypassAllowed;}
	public	void 	setBypassAllowed(boolean newBypassAllowed) {bypassAllowed = newBypassAllowed;}

	public	boolean	hasPath() {return path;}
	public	void 	setPath(boolean newPath) {path = newPath;}

	public	boolean	hasBreach() {return breach;}
	public	void 	setBreach(boolean newBreach) {breach = newBreach;}

	public	boolean	hasWire() {return wire;}
	public	void 	setWire(boolean newWire) {wire = newWire;}

	public	boolean	hasPanji() {return panji;}
	public	void 	setPanji(boolean newPanji) {panji = newPanji;}

	public	boolean	hasTrailbreak() {return trailbreak;}
	public	void 	setTrailbreak(boolean newTrailbreak) {trailbreak = newTrailbreak;}

	public	boolean	hasCave() {return cave;}
	public	void 	setCave(boolean newCave) {cave = newCave;}

	public	boolean	hasFire() {return fire;}
	public	void 	setFire(boolean newFire) {fire = newFire;}

	public	boolean	hasBlaze() {return blaze;}
	public	void 	setBlaze(boolean newBlaze) {blaze = newBlaze;}

	public	Terrain	getDepressionTerrain(){ return depressionTerrain;}
	public	void	setDepressionTerrain(Terrain newDepressionTerrain){

		// removing depression terrain?
		if (newDepressionTerrain == null){

			// ensure the location base elevation is the same as the center
			if(hex.getCenterLocation().isDepressionTerrain()){
				baseHeight = 1;
			}
			else {
				baseHeight = 0;
			}
		}

		// adding depression terrain?
		else if (depressionTerrain == null) {

			// set the location height same as center
			baseHeight = 0;

		}

		depressionTerrain = newDepressionTerrain;
	}

	public	boolean	isDepressionTerrain() {
		if(depressionTerrain != null) {
			return true;
		}
		else {
			return false;
		}
	}

	public	boolean	isCenterLocation() {

			return hex.isCenterLocation(this);
	}

	public	Location	getUpLocation() {return upLocation;}
	public	void		setUpLocation(Location newUpLocation) {upLocation = newUpLocation;}

	public	Location	getDownLocation() {return downLocation;}
	public	void		setDownLocation(Location newDownLocation) {downLocation = newDownLocation;}


	// constructors
	public Location(){};

	public Location(
		String  n,
		int	 hgt,
		Point   LOSpt,
		Point   auxLOSpt,
		Point   edgept,
		Hex	 hex,
		boolean bypass,
		Terrain terr) {

		name			= n;
		baseHeight		= hgt;
		LOSPoint		= LOSpt;
		auxLOSPoint		= auxLOSpt;
		edgeCenterPoint	= edgept;
		this.hex		= hex;
		bypassAllowed	= bypass;
		terrain			= terr;
	}

	public Location(Location l) {

		// use the same points
		LOSPoint		= (Point) l.getLOSPoint().clone();
		auxLOSPoint		= (Point) l.getAuxLOSPoint().clone();
		edgeCenterPoint	= (Point) l.getEdgeCenterPoint().clone();

		hex				= l.getHex();

		copyLocationValues(l);
	}

	public void copyLocationValues(Location l) {

		// copy the flags
		baseHeight 		= l.getBaseHeight();
		bypassAllowed	= l.isBypassAllowed();
		trailbreak		= l.hasTrailbreak();
		path			= l.hasPath();
		breach			= l.hasBreach();
		wire			= l.hasWire();
		panji			= l.hasPanji();
		cave			= l.hasCave();
		fire			= l.hasFire();
		blaze			= l.hasBlaze();

		// copy name, terrain values
		name				= l.getName();
		baseHeight			= l.getBaseHeight();
		terrain 			= l.getTerrain();
		depressionTerrain 	= l.getDepressionTerrain();
	}
}

