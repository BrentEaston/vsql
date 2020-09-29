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

/**
 * Title:        MovementResult.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class MovementResult{

	// constants
	public static final int NO_HEXSIDE_CROSSED 	= -1;
	public static final int ALL_MF 		= 99;
	public static final int ALL_MP 		= 99;

	// private variables
	private Location	startLocation;	// start of movement location
	private Location	exitLocation;	// bypass location used to exit hex
	private Location	endLocation;	// end of movement location
	private Location	enterLocation;	// bypass location used to enter new hex
	private int		exitHexside;		// hexside of exit hex that was crossed (-1=none)
	private int		enterHexside;		// hexside of enter hex that was crossed (-1=none)
	private boolean	legal;				// not legal: off map, no up/down location
	private String	errorMessage;		// Reason move not legal
	private String	movementMessage;	// Results of movement logic

	private float MP;	// movement points
	private float MF;	// movement factors

	private MovementResult prevMovementResult;

	// constructors
	public MovementResult(
		Location		startLocation,
		Location		exitLocation,
		Location		enterLocation,
		Location		endLocation,
		int				exitHexside,
		int				enterHexside,
		boolean			legal,
		String			errorMessage,
		MovementResult	prevMovementResult) {

		this.startLocation 		= startLocation;
		this.exitLocation  		= exitLocation;
		this.enterLocation 		= enterLocation;
		this.endLocation   	    = endLocation;
		this.exitHexside   		= exitHexside;
		this.enterHexside  	   	= enterHexside;
		this.legal		   	   	= legal;
		this.errorMessage  	   	= errorMessage;
		this.prevMovementResult	= prevMovementResult;
	}

	public Location	getStartLocation(){ return startLocation;}
	public Location	getEnterLocation(){ return enterLocation;}
	public Location	getExitLocation(){ return exitLocation;}

	public Location	getEndLocation(){ return endLocation;}
	public void		setEndLocation(Location l){ endLocation = l;}

	public float	getMP(){ return MP;}
	public void		setMP(float mp){MP = mp;}

	public float	getMF(){ return MF;}
	public void		setMF(float mf){MF = mf;}

	public int		getExitHexside(){ return exitHexside;}
	public int		getEnterHexside(){ return enterHexside;}

	public String	getErrorMessage(){ return errorMessage;}
	public void		setErrorMessage(String mess){ errorMessage = mess;}

	public String	getMovementMessage(){ return movementMessage;}
	public void		setMovementMessage(String mess){ movementMessage = mess;}

	public boolean	isLegal(){ return legal;}
	public void		setLegal(boolean flag){ legal = flag;}

	public MovementResult	getPrevMovementResult(){ return prevMovementResult;}
	public void				setPrevMovementResult(MovementResult mr){prevMovementResult = mr;}

	public boolean	crossedHexside(){

		if(exitHexside == -1 || enterHexside == -1){
			return false;
		}
		else {
			return true;
		}
	}
}
