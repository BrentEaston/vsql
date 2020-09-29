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

import java.util.Iterator;

import CASL.Unit.Unit;

/**
 * Title:        MovementLogic.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class MovementLogic {

	public class EnterHexResult {

     	public String 	message;
		public float    MF		= 0;
		public boolean  isLegal = true;
	}

	public MovementResult moveStack(CASL.Unit.Stack stack, MovementResult results){

		Unit	currentUnit = null;

		// step through each unit in the stack
		currentUnit = stack.getFirstUnit();
		while (currentUnit != null){

			currentUnit = stack.getNextUnit();
		}

		return results;
	}

	private EnterHexResult enterViaRoad(EnterHexResult results){

		results.message = "\nB3.4     Enter hex via road hexside: 1 MF";
		results.MF = 1;

		return results;
	}

	private EnterHexResult enterViaPath(EnterHexResult results){

		results.message = "\nB13.6    Enter hex via path hexside: 1 MF";
		results.MF = 1;

		return results;
	}

	private EnterHexResult getEnterHexMF(MovementResult results){

		EnterHexResult enterResults = new EnterHexResult();
		boolean enterViaRoad      = false;
		boolean enterViaPath      = false;
		boolean enterViaPaddyBank = false;

		// see if terrain on either side of hexside is non-enterable
		if (results.getEnterLocation() != null){

			switch(results.getEnterLocation().getTerrain().getType()){

				// water
				case Terrain.WATER:
				case Terrain.RIVER:
				case Terrain.OCEAN:
				case Terrain.CANAL:
					enterResults.message = "B21.4 Infantry may not enter water unless swimming";
					enterResults.isLegal = false;
					return enterResults;
			}
		}
		if (results.getExitLocation() != null){

			switch(results.getExitLocation().getTerrain().getType()){

				// water
				case Terrain.WATER:
				case Terrain.RIVER:
				case Terrain.OCEAN:
				case Terrain.CANAL:
					enterResults.message = "B21.4 Infantry may not enter water unless swimming";
					enterResults.isLegal = false;
					return enterResults;
			}
		}

		// entered via a road?
		if (results.crossedHexside() && results.getEnterLocation().getTerrain().isRoadTerrain()){

				enterViaRoad = true;
		}

		// entered via a path?
		if (results.crossedHexside() && results.getEnterLocation().getTerrain().getType() == Terrain.PATH){

				enterViaPath = true;
		}

		// entered via a paddy bank?
		if (results.crossedHexside() &&
			results.getEndLocation().getHex().getEdgeTerrain(results.getEnterHexside()) != null &&
			results.getEndLocation().getHex().getEdgeTerrain(results.getEnterHexside()).getType() == Terrain.RICE_PADDY_BANK){

				enterViaPaddyBank = true;
		}

		// move onto bridge?
		if (results.getEndLocation().getTerrain().isBridge()){

				enterResults.message = "\nB6.4     Enter bridge: 1 MF";
				enterResults.MF = 1;

				return enterResults;
		}

		switch(results.getEndLocation().getTerrain().getType()){

			// open ground
			case Terrain.OPEN_GROUND:
				enterResults.message = "\nB1       Enter open ground: 1 MF";
				enterResults.MF = 1;
				break;

			// shellholes
			case Terrain.SHELL_HOLES:
				enterResults.message = "\nB2.4     Enter INTO shellholes: 2 MF";
				enterResults.MF = 2;
				break;

			// brush
			case Terrain.BRUSH:
				if (enterViaRoad) return enterViaRoad(enterResults);
				if (enterViaPath) return enterViaPath(enterResults);
				enterResults.message = "\nB12.4     Enter brush: 2 MF";
				enterResults.MF = 2;
				break;

			// kunai
			case Terrain.KUNAI:
				if (enterViaRoad) return enterViaRoad(enterResults);
				if (enterViaPath) return enterViaPath(enterResults);
				enterResults.message = "\nG6.1      Enter kunai: 2 MF";
				enterResults.MF = 2;
				break;

			// woods
			case Terrain.WOODS:
				if (enterViaRoad) return enterViaRoad(enterResults);
				if (enterViaPath) return enterViaPath(enterResults);
				enterResults.message = "\nB13.4     Enter woods: 2 MF";
				enterResults.MF = 2;
				break;

			// pine woods
			case Terrain.PINE_WOODS:
				if (enterViaRoad) return enterViaRoad(enterResults);
				if (enterViaPath) return enterViaPath(enterResults);
				enterResults.message = "\nB13.82    Enter pine woods: 1.5 MF";
				enterResults.MF = (float) 1.5;
				break;

			// forest
			case Terrain.FOREST:
				if (enterViaRoad) return enterViaRoad(enterResults);
				if (enterViaPath) return enterViaPath(enterResults);
				enterResults.message = "\nB13.7     Enter forest: 2 MF";
				enterResults.MF = 2;
				break;

			// light jungle
			case Terrain.LIGHT_JUNGLE:
				if (enterViaRoad) return enterViaRoad(enterResults);
				if (enterViaPath) return enterViaPath(enterResults);
				enterResults.message = "\nG2.1      Enter light jungle: 2 MF";
				enterResults.MF = 2;
				break;

			// dense jungle
			case Terrain.DENSE_JUNGLE:
				if (enterViaRoad) return enterViaRoad(enterResults);
				if (enterViaPath) return enterViaPath(enterResults);
				enterResults.message = "\nG2.21     Enter dense jungle: 2 MF";
				enterResults.MF = 2;
				break;

			// Orchard
			case Terrain.ORCHARD:
			case Terrain.ORCHARD_OUT_OF_SEASON:
				if (enterViaRoad) return enterViaRoad(enterResults);
				if (enterViaPath) return enterViaPath(enterResults);
				enterResults.message = "\nB14.4     Enter orchard: 1 MF";
				enterResults.MF = 1;
				break;

			// Palm trees
			case Terrain.PALM_TREES:
				if (enterViaRoad) return enterViaRoad(enterResults);
				if (enterViaPath) return enterViaPath(enterResults);
				enterResults.message = "\nG4.1      Enter Palm trees: 1 MF";
				enterResults.MF = 1;
				break;

			// grain
			case Terrain.GRAIN:
				if (enterViaRoad) return enterViaRoad(enterResults);
				if (enterViaPath) return enterViaPath(enterResults);
				enterResults.message = "\nB15.4     Enter grain: 1.5 MF";
				enterResults.MF = (float) 1.5;
				break;

			// plowed fields
			case Terrain.PLOWED_FIELD:
				if (enterViaRoad) return enterViaRoad(enterResults);
				if (enterViaPath) return enterViaPath(enterResults);
				enterResults.message = "\nB15.6     Enter plowed field: 1.5 MF";
				enterResults.MF = (float) 1.5;
				break;

			// marsh
			case Terrain.MARSH:
				enterResults.message = "\nB16.4     Enter marsh: all MF";
				enterResults.MF = MovementResult.ALL_MF;
				break;

			// swamp
			case Terrain.SWAMP:
				enterResults.message = "\nG7.1      Enter swamp: all MF";
				enterResults.MF = MovementResult.ALL_MF;
				break;

			// bamboo
			case Terrain.BAMBOO:
				if (enterViaRoad) return enterViaRoad(enterResults);
				if (enterViaPath) return enterViaPath(enterResults);
				enterResults.message = "\nG3.2      Enter bamboo: all MF";
				enterResults.MF = MovementResult.ALL_MF;
				break;

			// crag
			case Terrain.CRAGS:
				if (enterViaRoad) return enterViaRoad(enterResults);
				if (enterViaPath) return enterViaPath(enterResults);
				enterResults.message = "\nB17.4     Enter crag: 2 MF";
				enterResults.MF = 2;
				break;

			// graveyard
			case Terrain.GRAVE_YARD:
				if (enterViaRoad) return enterViaRoad(enterResults);
				if (enterViaPath) return enterViaPath(enterResults);
				enterResults.message = "\nB18.41    Enter graveyard: 1 MF";
				enterResults.MF = 1;
				break;

			// rice paddy
			case Terrain.RICE_PADDY_DRAINED:

				// may not enter from paddy bank if no bank hexside
				if (results.getStartLocation().getTerrain().getType() == Terrain.RICE_PADDY_BANK && !enterViaPaddyBank && results.crossedHexside()){

					enterResults.message = "\nG8.210    Unit may not move from a rice paddy bank INTO a rice paddy across a non-bank hexside";
					enterResults.isLegal = false;
					return enterResults;
				}

				// non-paddy or IN paddy INTO paddy
				if (results.getStartLocation().getTerrain().getType() != Terrain.RICE_PADDY_BANK){

					enterResults.message = "\nG8.2102   Enter INTO drained rice paddy from IN paddy or non-paddy: 1 MF";
					enterResults.MF = 1;
				}

				// paddy bank INTO paddy
				else if (results.getStartLocation().getTerrain().getType() == Terrain.RICE_PADDY_BANK  && enterViaPaddyBank){

					enterResults.message = "\nG8.2106   Enter INTO drained rice paddy from paddy bank: 1 MF";
					enterResults.MF = 1;
				}
				break;

			case Terrain.RICE_PADDY_IRRIGATED:

				// may not enter from paddy bank if no bank hexside
				if (results.getStartLocation().getTerrain().getType() == Terrain.RICE_PADDY_BANK && !enterViaPaddyBank && results.crossedHexside()){

					enterResults.message = "\nG8.210    Unit may not move from a rice paddy bank INTO a rice paddy across a non-bank hexside";
					enterResults.isLegal = false;
					return enterResults;
				}

				// non-paddy or IN paddy INTO paddy
				if (results.getStartLocation().getTerrain().getType() != Terrain.RICE_PADDY_BANK){

					enterResults.message = "\nG8.2102   Enter INTO irrigated rice paddy from IN paddy or non-paddy: 3 MF";
					enterResults.MF = 3;
				}
				// paddy bank INTO paddy
				else if (results.getStartLocation().getTerrain().getType() == Terrain.RICE_PADDY_BANK && enterViaPaddyBank){

					enterResults.message = "\nG8.2106   Enter INTO irrigated rice paddy from paddy bank: 3 MF";
					enterResults.MF = 3;
				}
				break;

			case Terrain.RICE_PADDY_IN_SEASON:

				// may not enter from paddy bank if no bank hexside
				if (results.getStartLocation().getTerrain().getType() == Terrain.RICE_PADDY_BANK && !enterViaPaddyBank && results.crossedHexside()){

					enterResults.message = "\nG8.210    Unit may not move from a rice paddy bank INTO a rice paddy across a non-bank hexside";
					enterResults.isLegal = false;
					return enterResults;
				}

				// non-paddy or IN paddy INTO paddy
				if (results.getStartLocation().getTerrain().getType() != Terrain.RICE_PADDY_BANK){

					enterResults.message = "\nG8.2102   Enter INTO in-season rice paddy from IN paddy or non-paddy: 1.5 MF";
					enterResults.MF = (float) 1.5;
				}
				// paddy bank INTO paddy
				else if (results.getStartLocation().getTerrain().getType() == Terrain.RICE_PADDY_BANK && enterViaPaddyBank){

					enterResults.message = "\nG8.2106   Enter INTO in-season rice paddy from paddy bank: 1.5 MF";
					enterResults.MF = (float) 1.5;
				}
				break;

			// rice paddy bank
			case Terrain.RICE_PADDY_BANK:

				// must be across paddy bank
				if (results.crossedHexside() && !enterViaPaddyBank){

					enterResults.message = "\nG8.210    Unit may not move directly onto rice paddy bank across a non-bank hexside";
					enterResults.isLegal = false;
					return enterResults;
				}

				// non-paddy or IN paddy to bank
				if (results.getStartLocation().getTerrain().getType() != Terrain.RICE_PADDY_BANK && enterViaPaddyBank){

					enterResults.message = "\nG8.2101   Enter onto rice paddy bank from IN paddy or non-paddy: 1 MF";
					enterResults.MF = 1;
				}
				// bank to bank
				else if (results.getStartLocation().getTerrain().getType() == Terrain.RICE_PADDY_BANK && enterViaPaddyBank){

					enterResults.message = "\nG8.2101   Enter onto rice paddy bank from rice paddy bank: Bank cost only";
					enterResults.MF = 0;
				}

				break;

			// water
			case Terrain.WATER:
			case Terrain.RIVER:
			case Terrain.OCEAN:
			case Terrain.CANAL:
				enterResults.message = "B21.4 Infantry may not enter water unless swimming";
				enterResults.isLegal = false;
				break;

			// shallow water
			case Terrain.SHALLOW_RIVER:
			case Terrain.SHALLOW_OCEAN:
			case Terrain.SHALLOW_WATER:
				enterResults.message = "\nB21.41    Enter shallow water: all MF";
				enterResults.MF = MovementResult.ALL_MF;
				break;

			// roads
			case Terrain.DIRT_ROAD:
				enterResults.message = "\nB23.4    Enter dirt road via non-road hexside: 1 MF";
				enterResults.MF = 1;
				break;
			case Terrain.PAVED_ROAD:
				enterResults.message = "\nB23.4    Enter paved road via non-road hexside: 1 MF";
				enterResults.MF = 1;
				break;
			case Terrain.SUNKEN_ROAD:
				enterResults.message = "\nB23.4    Enter sunken road via non-road hexside: 2 MF";
				enterResults.MF = 2;
				break;
			case Terrain.ELEVATED_ROAD:
				enterResults.message = "\nB23.4    Enter elevated road via non-road hexside: 1 MF";
				enterResults.MF = 1;
				break;

			// lumberyard
			case Terrain.LUMBER_YARD:
				enterResults.message = "\nB23.211  Enter lumberyard: 2 MF";
				enterResults.MF = 2;
				break;

			// building
			case Terrain.STONE_BUILDING:
			case Terrain.STONE_BUILDING_1_LEVEL:
			case Terrain.STONE_BUILDING_2_LEVEL:
			case Terrain.STONE_BUILDING_3_LEVEL:
			case Terrain.STONE_BUILDING_4_LEVEL:
			case Terrain.WOODEN_MARKET_PLACE:
			case Terrain.WOODEN_BUILDING:
			case Terrain.WOODEN_BUILDING_1_LEVEL:
			case Terrain.WOODEN_BUILDING_2_LEVEL:
			case Terrain.WOODEN_BUILDING_3_LEVEL:
			case Terrain.WOODEN_BUILDING_4_LEVEL:
			case Terrain.STONE_MARKET_PLACE:
			case Terrain.TEMPLE:
			case Terrain.HUTS:
			case Terrain.COLLAPSED_HUTS:
				if (enterViaRoad) return enterViaRoad(enterResults);
				enterResults.message = "\nB23.4    Enter building: 2 MF";
				enterResults.MF = 2;
				break;

			// factory
			case Terrain.WOODEN_FACTORY_1_5_LEVEL:
			case Terrain.WOODEN_FACTORY_2_5_LEVEL:
			case Terrain.STONE_FACTORY_1_5_LEVEL:
			case Terrain.STONE_FACTORY_2_5_LEVEL:

				// enter from another factory hex?
				if (results.getEnterLocation().getTerrain().isFactoryTerrain()){

					enterResults.message = "\nB23.743   Enter factory: 1 MF";
					enterResults.MF = 1;
				}
				else {
					enterResults.message = "\nB23.743   Enter factory: 2 MF";
					enterResults.MF = 2;
				}
				break;

			// rubble
			case Terrain.WOODEN_RUBBLE:
			case Terrain.STONE_RUBBLE:
				enterResults.message = "\nB24.4     Enter rubble: 3 MF";
				enterResults.MF = 3;
				break;


		}

		return enterResults;
	}

	public MovementResult moveUnit(Unit unit, MovementResult results){

		// movement type flags
		boolean infantryMovement 	= false;
		boolean trackedMovement 	= false;
		boolean halftrackMovement 	= false;
		boolean ACMovement 			= false;
		boolean truckMovement 		= false;
		boolean wagonMovement 		= false;
		boolean calvaryMovement 	= false;

		float	movementCost = 0;
		float	edgeMovementCost = 0;
		String	movementMessages = "";

		// if results is a bad move, return it
		if (!results.isLegal()) return results;

		// determine unit movement type
		switch(unit.getMovementType()){

			case Unit.INFANTRY_MOVEMENT:
				infantryMovement 	= true;
				break;
			case Unit.TRACKED_MOVEMENT:
				trackedMovement 	= true;
				break;
			case Unit.HALFTRACK_MOVEMENT:
				halftrackMovement	= true;
				break;
			case Unit.AC_MOVEMENT:
				ACMovement 		= true;
				break;
			case Unit.TRUCK_MOVEMENT:
				truckMovement 	= true;
				break;
			case Unit.WAGON_MOVEMENT:
				wagonMovement 	= true;
				break;
			case Unit.CALVARY_MOVEMENT:
				calvaryMovement = true;
				break;
		}

		// infanty movement?
		if (infantryMovement){

			// setup variables
			int levelsChanged = results.getEndLocation().getAbsoluteHeight() - results.getStartLocation().getAbsoluteHeight();

			Terrain depressionTerrain 	= results.getEndLocation().getDepressionTerrain();
			Terrain endTerr 			= results.getEndLocation().getTerrain();
			Terrain startTerr 			= results.getStartLocation().getTerrain();

			// moving within the same hex?
			if (!results.crossedHexside()){

				EnterHexResult enterHexResults = getEnterHexMF(results);

				// legal?
				if (!enterHexResults.isLegal) {

					results.setErrorMessage(enterHexResults.message);
					results.setLegal(false);
   					return results;
				}

				// all MF?
				if (enterHexResults.MF == MovementResult.ALL_MF){

					results.setMovementMessage(enterHexResults.message);
					results.setMF(MovementResult.ALL_MF);
					return results;
				}

				// if unit entered the hex three moves ago and remainded in bypass, double bypass entry cost
				if (// the previous move was a continuation of bypass in the current hex
					!results.getEndLocation().isCenterLocation() &&
					results.getPrevMovementResult() != null &&
					results.getPrevMovementResult().getEndLocation().getHex() == results.getEndLocation().getHex() &&
					results.getPrevMovementResult().getStartLocation().getHex() == results.getEndLocation().getHex() &&
					!results.getPrevMovementResult().getEndLocation().isCenterLocation() &&
					!results.getPrevMovementResult().getStartLocation().isCenterLocation() &&
					// the move before that entered the current hex in bypass
					results.getPrevMovementResult().getPrevMovementResult() != null &&
					results.getPrevMovementResult().getPrevMovementResult().crossedHexside() &&
					results.getPrevMovementResult().getPrevMovementResult().getEndLocation().getHex() == results.getEndLocation().getHex() &&
					!results.getPrevMovementResult().getPrevMovementResult().getEndLocation().isCenterLocation()
					){

					movementMessages += "\nA4.31    Bypassing more than two hexsides in the same hex: bypass cost doubled";
					movementMessages += enterHexResults.message;
					movementCost 	 +=
						2 * Math.max(enterHexResults.MF, results.getPrevMovementResult().getMF() + results.getPrevMovementResult().getPrevMovementResult().getMF())
							- results.getPrevMovementResult().getPrevMovementResult().getMF()
							- results.getPrevMovementResult().getMF();
				}

				// if the unit entered the current hex in bypass but the terrain has changed
				else if (
					results.getPrevMovementResult() != null &&
					results.getPrevMovementResult().crossedHexside() &&
					results.getPrevMovementResult().getEndLocation().getHex() == results.getEndLocation().getHex() &&
					!results.getPrevMovementResult().getEndLocation().isCenterLocation()
				) {

					if (enterHexResults.MF > results.getPrevMovementResult().getMF()){
						movementMessages += "\nA4.31    Bypassing second hexside with higher bypass cost";
						movementMessages += enterHexResults.message;
						movementCost 	 += enterHexResults.MF - results.getPrevMovementResult().getMF();
					}
					else {
						movementMessages += "\nA4.31    Bypassing second hexside: no cost";
					}
				}

				// enter hex center from bypass?
				else if (results.getEndLocation().isCenterLocation() && !results.getStartLocation().isCenterLocation()){

					movementMessages += enterHexResults.message;
					movementCost 	 += enterHexResults.MF;
				}

				// moving vertically
				else if (results.getEndLocation().getHex().isCenterLocation(results.getEndLocation()) &&
					results.getStartLocation().getHex().isCenterLocation(results.getStartLocation())){

					// can't change levels on a bridge
                    if (results.getEndLocation().getTerrain().isBridge()){

						results.setErrorMessage("B6.4     May not move onto a bridge unless climbing");
						results.setLegal(false);
						return results;
					}
                    else if (results.getStartLocation().getTerrain().isBridge()){

						results.setErrorMessage("B6.4     May not move beneath a bridge unless climbing");
						results.setLegal(false);
						return results;
					}

					// exit/enter entrenchment
					if (endTerr.getType() == Terrain.FOX_HOLES && startTerr.getType() != Terrain.FOX_HOLES){

						movementMessages += "\nB27.4    Enter foxholes: 1 MF";
						movementCost += 1;
					}
					else if (endTerr.getType() != Terrain.FOX_HOLES && startTerr.getType() == Terrain.FOX_HOLES){

						movementMessages += "\nB27.4    Exit foxholes: 1 MF";
						movementCost += 1;
					}
					else if (endTerr.getType() == Terrain.PILLBOX && startTerr.getType() != Terrain.PILLBOX){

						movementMessages += "\nB27.4    Enter pillbox: 1 MF";
						movementCost += 1;
					}
					else if (endTerr.getType() != Terrain.PILLBOX && startTerr.getType() == Terrain.PILLBOX){

						movementMessages += "\nB27.4    Exit pillbox: 1 MF";
						movementCost += 1;
					}
					else if (endTerr.getType() == Terrain.TRENCH && startTerr.getType() != Terrain.TRENCH){

						movementMessages += "\nB27.4    Enter trench: 1 MF";
						movementCost += 1;
					}
					else if (endTerr.getType() != Terrain.TRENCH && startTerr.getType() == Terrain.TRENCH){

						movementMessages += "\nB27.4    Exit trench: 1 MF";
						movementCost += 1;
					}
					else if (endTerr.getType() == Terrain.RICE_PADDY_DRAINED && startTerr.getType() == Terrain.RICE_PADDY_BANK){
						movementMessages += "\nG8.2108  Enter drained rice paddy: 1 MF";
						movementCost += 1;
					}
					else if (endTerr.getType() == Terrain.RICE_PADDY_IRRIGATED && startTerr.getType() == Terrain.RICE_PADDY_BANK){

						movementMessages += "\nG8.2108  Enter irrigated rice paddy: 3 MF";
						movementCost += 3;
					}
					else if (endTerr.getType() == Terrain.RICE_PADDY_IN_SEASON && startTerr.getType() == Terrain.RICE_PADDY_BANK){

						movementMessages += "\nG8.2108  Enter in-season rice paddy: 1.5 MF";
						movementCost += (float) 1.5;
					}
					else if (endTerr.getType() == Terrain.RICE_PADDY_BANK && startTerr.isRicePaddy()){

						movementMessages += "\nG8.2108  Enter rice paddy bank: 1 MF";
						movementCost += 1;
					}

					// exit/enter tunnel

					// change level in building
					if (endTerr.isBuildingTerrain() && startTerr.isBuildingTerrain() ||
						(endTerr.isMarketPlaceTerrain() || startTerr.isMarketPlaceTerrain())){

						if (!results.getEndLocation().getHex().hasStairway()) {

							results.setErrorMessage("\nB23.4??  Cannot change levels within a building without a stairway");
							results.setLegal(false);
							return results;
						}
						else {
							movementMessages += "\nB23.4    Changing levels within a building: 1 MF";
							movementCost += 1;
						}
					}
				}

				// catch degenerate cases
				else {
					results.setErrorMessage("Cannot determine the movement cost of this move");
					results.setLegal(false);
   					return results;
				}

				// smoke in end location?
				Iterator iter = results.getEndLocation().getHex().getSmoke().iterator();
				boolean inSmoke = false;
				Smoke	s		= null;
				while (iter.hasNext()){

					s = (Smoke) iter.next();
					if (s.getLocation().getAbsoluteHeight() 				<= results.getEndLocation().getAbsoluteHeight() &&
						s.getLocation().getAbsoluteHeight() + s.getHeight() >= results.getEndLocation().getAbsoluteHeight()){

						inSmoke = true;
					}
				}
				if (inSmoke){

					movementMessages += "\nA24.7    Movement through smoke: 1MF";
					movementCost++;
				}

				// set results and return
				results.setMovementMessage(movementMessages);
				results.setMF(movementCost);
				return results;
			}

			// crossed a hexside
			else {

				// get edge terrain
				Terrain edgeTerrain = results.getEndLocation().getHex().getEdgeTerrain(results.getEnterHexside());
				boolean hasCliff    = results.getEndLocation().getHex().hasCliff(results.getEnterHexside());

				// leaving an entrenchment?
				if (startTerr.isEntrenchmentTerrain()){

					results.setErrorMessage("B27.4    Must exit entrenchment before leaving hex");
					results.setLegal(false);
					return results;
				}

				// leaving an tunnel?
				if (startTerr.isTunnelTerrain()){

					results.setErrorMessage("B8.41    May not exit a sewer/tunnel during the movement phase");
					results.setLegal(false);
					return results;
				}

				// exit building
				if (startTerr.isBuildingTerrain()){

					// Moving building to building?
					if ((endTerr.isBuildingTerrain() ||
						    (results.getEndLocation().getUpLocation() != null && results.getEndLocation().getUpLocation().getTerrain().isMarketPlaceTerrain())) &&
						results.getEnterLocation().getTerrain().isBuildingTerrain() &&
						results.getExitLocation().getTerrain().isBuildingTerrain()){

						// try to move into the correct building level
						if (results.getStartLocation().getAbsoluteHeight()!= results.getEndLocation().getAbsoluteHeight()){

							Hex 	endHex 	 	= results.getEndLocation().getHex();
							Hex 	startHex 	= results.getStartLocation().getHex();
							int 	startLevel	= results.getStartLocation().getAbsoluteHeight();
							boolean levelFound	= false;

							// try to move into the correct level
							if ( startLevel != results.getEndLocation().getAbsoluteHeight()){

								Location currentLoc = endHex.getCenterLocation();
								while (currentLoc != null){

									// is this the right level?
									if (currentLoc.getAbsoluteHeight() == startLevel){

										results.setEndLocation(currentLoc);
										levelsChanged = 0;
										levelFound = true;
										break;
									}
									else {

										currentLoc = currentLoc.getUpLocation();
									}
								}

								if (!levelFound){
									results.setErrorMessage("B23.4??? Cannot enter the building from the current level");
									results.setLegal(false);
									return results;
								}
							}
						}
					}

					//Must exit via hex base level
					else if (results.getStartLocation().getBaseHeight()!= 0){

						results.setErrorMessage("B23.4??? Cannot exit a building via an upper level");
						results.setLegal(false);
						return results;

					}
				}

				// exit bridge via road?
				if (startTerr.isBridge() &&
					!(results.getEnterLocation().getTerrain().isRoadTerrain() ||
					  results.getEndLocation().getTerrain().isBridge())){

					results.setErrorMessage("B6.4  Must exit a bridge via a road hexside unless climbing");
					results.setLegal(false);
					return results;
				}

				// edge terrain crossed?
				if (hasCliff) {

					results.setErrorMessage("B11.4 May not cross a cliff hexside unless climbing");
					results.setLegal(false);
					return results;
				}

				if (edgeTerrain != null){

					switch(edgeTerrain.getType()){

						// wall
						case Terrain.WALL:
							movementMessages += "\nB9.4     Crossing wall: 1 MF";
							edgeMovementCost += 1;
							break;

						// hedge
						case Terrain.HEDGE:
							movementMessages += "\nB9.4     Crossing hedge: 1 MF";
							edgeMovementCost += 1;
							break;

						// bocage
						case Terrain.BOCAGE:
							movementMessages += "\nB9.54    Crossing bocage: 2 MF";
							edgeMovementCost += 2;
							break;

						// roadblock
						case Terrain.ROAD_BLOCK:
							movementMessages += "\nB29.4    Crossing roadblock: 1 MF";
							edgeMovementCost += 1;
							break;

						// rice paddy bank
						case Terrain.RICE_PADDY_BANK:
							movementMessages += "\nG8.210    Crossing rice paddy bank: 1 MF";
							edgeMovementCost += 1;
							break;

						// rowhouse walls
						case Terrain.ROWHOUSE_WALL:
						case Terrain.ROWHOUSE_WALL_1_LEVEL:
						case Terrain.ROWHOUSE_WALL_2_LEVEL:
						case Terrain.ROWHOUSE_WALL_3_LEVEL:

							// must have bypassed around the wall
							if (results.getEndLocation().getHex().isCenterLocation(results.getEndLocation())){

								results.setErrorMessage("B23.71 Infantry may not cross a Rowhouse wall");
								results.setLegal(false);
								return results;
							}

							break;
					}
				}

				//standard movement logic
				EnterHexResult enterHexResults = getEnterHexMF(results);

				// legal?
				if (!enterHexResults.isLegal) {

					results.setErrorMessage(enterHexResults.message);
					results.setLegal(false);
    				return results;
				}

				// all MF?
				if (enterHexResults.MF == MovementResult.ALL_MF){

					results.setMovementMessage(enterHexResults.message);
					results.setMF(MovementResult.ALL_MF);
					return results;
				}
				else {

					movementMessages += enterHexResults.message;
					movementCost 	 += enterHexResults.MF;
				}

				// check depression terrain
				if (depressionTerrain != null){

					switch(depressionTerrain.getType()){

						// enter gully
						case Terrain.GULLY:
							if (endTerr.isOpenTerrain()){
								movementMessages = "\nB19.4    Enter gully: 2 MF";
								movementCost = 2;
							}
							else {
								movementMessages += "\nB19.4    Enter gully: 2 MF";
								movementCost += 2;
							}
							break;

						// enter dry stream
						case Terrain.DRY_STREAM:
							if (endTerr.isOpenTerrain()){
								movementMessages = "\nB20.41   Enter dry stream: 2 MF";
								movementCost = 2;
							}
							else {
								movementMessages += "\nB20.41   Enter dry stream: 2 MF";
								movementCost += 2;
							}
							break;

						// enter shallow stream
						case Terrain.SHALLOW_STREAM:
							if (endTerr.isOpenTerrain()){
								movementMessages = "\nB20.42   Enter shallow stream: 3 MF";
								movementCost = 3;
							}
							else {
								movementMessages += "\nB20.42   Enter shallow stream: 3 MF";
								movementCost += 3;
							}
							break;

						// enter deep stream
						case Terrain.DEEP_STREAM:
							if (endTerr.isOpenTerrain()){
								movementMessages = "\nB20.43   Enter deep stream: 4 MF";
								movementCost = 4;
							}
							else {
								movementMessages += "\nB20.43   Enter deep stream: 4 MF";
								movementCost += 4;
							}
							break;

						// sunken road
						case Terrain.SUNKEN_ROAD:
							if (results.getEnterLocation().getTerrain().isRoadTerrain()){
								movementMessages = "\nB4.4     Enter sunken road hex via road hexside: 1 MF";
								movementCost = 1;
							}
							else {
								movementMessages += "\nB4.41    Enter sunken road hex via non-road hexside: 2 MF";
								movementCost = 2;
							}
							break;
	    				}
				}

				// double the enter cost if base elevation is higher
				if (levelsChanged == 1){

					movementMessages += "\nB10.4    Crosses a Crest Line into higher terrain: 2 * COT";
					movementCost *= 2;
				}

				// Abrupt elevation change: going up
				else if (levelsChanged > 1){

					movementMessages += "\nB10.51   Abrupt Elevation Change into higher terrain: 2 * COT * # levels crossed";
					movementCost *= 2 * Math.abs(levelsChanged);
				}

				// Abrupt elevation change: going down
				else if (levelsChanged < -1){

					movementMessages += "\nB10.51   Abrupt Elevation Change into lower terrain: COT * # levels crossed";
					movementCost *= Math.abs(levelsChanged);
				}
			}

			// smoke in end location? (This is redundant code)
			Iterator iter = results.getEndLocation().getHex().getSmoke().iterator();
			boolean inSmoke = false;
			Smoke	s		= null;
			while (iter.hasNext()){

				s = (Smoke) iter.next();
				if (s.getLocation().getAbsoluteHeight() 				<= results.getEndLocation().getAbsoluteHeight() &&
					s.getLocation().getAbsoluteHeight() + s.getHeight() >  results.getEndLocation().getAbsoluteHeight()){

					inSmoke = true;
				}
			}
			if (inSmoke){

				movementMessages += "\nA24.7    Movement through smoke: 1MF";
				movementCost++;
			}

			// set results and return
			results.setMovementMessage(movementMessages);
			results.setMF(movementCost + edgeMovementCost);
			return results;
		}

		// set results and return
		results.setMovementMessage(movementMessages);
		results.setMF(movementCost + edgeMovementCost);
		return results;
	}

	public MovementResult advanceUnit(Unit unit, MovementResult results){
		return results;
	}

	public MovementResult routUnit(Unit unit, MovementResult results){
		return results;
	}
}
