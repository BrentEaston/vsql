/*
 * $Id: SQLLOSResult.java 5546 2009-04-27 13:21:39Z swampwallaby $
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

import java.awt.Point;
import java.util.Iterator;

import CASL.Map.Hex;
import CASL.Map.LOSResult;

public class SQLLOSResult extends LOSResult {
	
	/*
	 *  Over-ride addMapHindrance to generate a LOS blockage, not a hindrance.
	 */
	 public void addMapHindrance(Hex h, int x, int y){

		 // point already in the extended border of a hindrance hex?
		 Iterator<Hex> iter = mapHindranceHexes.iterator();
		 boolean	found = false;
		 Hex temp = null;
		 while(iter.hasNext() && !found){

			 temp = (Hex) iter.next();
			 if(temp.getExtendedHexBorder().contains(x, y)){
				 found = true;
			 }
			 // don't add if LOSis60Degree and the appropriate adjacent hex already has been added
			 if (LOSis60Degree) {

				 if (sourceExitHexspine == 0 || sourceExitHexspine == 3){

					 if (h.getMap().getAdjacentHex(temp, 1) == h || h.getMap().getAdjacentHex(temp, 4) == h){

						 found = true;
					 }
				 }
				 else if (sourceExitHexspine == 1 || sourceExitHexspine == 4){

					 if (h.getMap().getAdjacentHex(temp, 2) == h || h.getMap().getAdjacentHex(temp, 5) == h){

						 found = true;
					 }
				 }
			 }

			 // don't add another hex having the same range to the target is already present
			 if(sourceLocation.getHex().getMap().range(sourceLocation.getHex(), h) ==
				sourceLocation.getHex().getMap().range(sourceLocation.getHex(), temp)){

				 found = true;
			 }
		 }

		 // add hex if necessary
		 if(!found){
			 mapHindranceHexes.add(h);
			  
			 // set first hindrance point, if necesary
			 if (firstHindranceAt == null){

				 firstHindranceAt = new Point(x, y);
				 setBlocked(x,y, "LOS blocked by " + h.getTerrain().getName() + " (44.21)");
			 }

			 // blocked if hindrances >= 6
//			 if (getHindrance() >= 6) {
//
//				 setBlocked(x, y, "Hindrance total of six or more (B.10)");
//			 }
		 }
	 }
}
