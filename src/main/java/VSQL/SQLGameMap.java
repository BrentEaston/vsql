/*
 * $Id: SQLGameMap.java 2271 2007-07-08 02:39:45Z swampwallaby $
 *
 * Copyright (c) 2000-2004 by Brent Easton and Rodney Kinney
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
package VSQL;

import CASL.Map.GameMap;
import CASL.Map.Hex;
import CASL.Map.Terrain;


public class SQLGameMap extends GameMap {

  private static final long serialVersionUID = 1L;
    protected static SQLGameMap theMap;
    
	public SQLGameMap(int w, int h) {
		super(w, h);
		LOS_err_A6_3_1  = "Exits depression before range/elevation restictions are satisfied (A6.3)";
		LOS_err_A6_3_2  = "Does not enter depression while range/elevation restictions are satisfied (A6.3)";
		LOS_err_A6_8    = "LOS must leave the building before leaving the source hex to see a location with a different elevation (A6.8 Example 2)";
		LOS_err_B23_71  = "Cannot see through rowhouse wall (B23.71)";
		LOS_err_B27_2_1 = "Unit in entrenchment cannot see over hexside terrain to non-adjacent lower target (B27.2)";
		LOS_err_B27_2_2 = "Cannot see non-adjacent unit in higher elevation entrenchment over hexside terrain (B27.2)";
		LOS_err_B9_52_1 = "Cannot see through/over bocage (B9.52)";
		LOS_err_B9_52_2 = "Source or Target location is in a blind hex (B9.52)";
		LOS_err_B9_2    = "Intervening hexside terrain (11.51)";
		LOS_err_A6_2_1  = "Ground level is higher than both the source and target (43.4)";
		LOS_err_A6_2_2  = "Half level terrain is higher than both the source and target (43.4)";
		LOS_err_A6_2_3  = "Terrain is higher than both the source and target (43.4)";
		LOS_err_A6_2_4  = "Must have a height advantage to see over this terrain (43.4)";
		LOS_err_A6_4_1  = "Source or Target location is in a blind hex (7.4, 43.62)";
		LOS_err_B10_23  = "Source or Target location is in a blind hex (B10.23)";
		
		theMap = this;
	
	}
	
	public static SQLGameMap getMap() {
	  return theMap;
	}
	
	public Hex getGridHex(int x, int y) {
	  return gridToHex(x, y);
	}

	/*
	 * Replace Blind Hex calculations with VSQL rules.
	 */
	protected boolean isBlindHex(
		int sourceElevation,
		int targetElevation,
		int rangeToSource,
		int rangeToTarget,
		int groundLevel,
		int currentTerrainHgt,
		Terrain currentTerrain,
		Hex currentHex,
		boolean isCliffHexside) {

		int temp = 0;

		boolean result = false;

		// if LOS raising, swap source/target and use the same logic as LOS falling
		if (sourceElevation < targetElevation) {

			// swap elevations
			temp = sourceElevation;
			sourceElevation = targetElevation;
			targetElevation = temp;

			// swap range
			temp = rangeToSource;
			rangeToSource = rangeToTarget;
			rangeToTarget = temp;
		}

		// 2 blind hexes behind multi-story buildings, otherwise 1.
		if (currentTerrainHgt == 0 && !isCliffHexside) {
		    return rangeToTarget <= Math.max(2 * (groundLevel + currentTerrainHgt) + Math.min((rangeToSource / 5), 1)
	          - sourceElevation - targetElevation, 0);
		}
		else if (currentTerrain.isBuildingTerrain() && currentTerrain.getHeight() > 1 ) {
			result = (rangeToTarget <= 2);
		}
		else if (!currentTerrain.isOpenTerrain()) {        
			result = (rangeToTarget <= 1);
		}
		return result;
	}
}
