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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Title:        Map.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * The <code>Map</code> class maintains the map data information.
 * It is used to control map versioning.
 *
 * @author       David Sullivan
 * @version      1.0
 */
public class Map
	implements Serializable {

	// serial ID: version 1.0.0
	static final long serialVersionUID = 000100L;

	// private variables
	protected int		 width;  		// width of map in hexes
	protected int		 height; 		// height of map in hexes
	protected int		 gridWidth;  	// width of map grid
	protected int		 gridHeight; 	// height of map grid

	protected char		 terrainGrid[][];  		// terrain for each pixel
	protected byte		 elevationGrid[][];  	// height for each pixel
	protected Hex		 hexGrid[][];			// hex array
	protected Terrain	 terrainList[];  		// all terrain types

	// smoke list
	protected HashSet smokeList	= new HashSet(20);

	// prefab building list
	protected HashSet prefabBuildingList = new HashSet(20);

	// image options
	protected boolean	showShadows		= false;
	protected boolean	showContours	= false;
	protected boolean	showHexes		= true;

	/**
	* Reads the map from disk. File name must be fully qualified.
	* @param filename name of the file
	* @return <code>GameMap</code> GameMap object. Null if an error occured.
	*/
	public static GameMap readMap(String filename){

		ObjectInputStream   infile;
		GameMap map;

		try {
			infile =
			new ObjectInputStream(
			new BufferedInputStream(
			new GZIPInputStream(
			new FileInputStream(filename))));

			map  = (GameMap) infile.readObject();
			infile.close();

		} catch(Exception e) {
			System.err.println("Cannot open the map file: " + filename);
			e.printStackTrace(System.err);
			return null;
		}
		
		// reset the terrain to pick up current values
		map.terrainList = Terrain.getTerrainList();

		return map;
	}

	/**
	* Reads the map from an input stream.
	* @param instream input stream
	* @return <code>GameMap</code> GameMap object. Null if an error occured.
	*/
	public static GameMap readMap(InputStream instream){

		ObjectInputStream   infile;
		GameMap map;

		try {
			infile =
			new ObjectInputStream(
			new BufferedInputStream(
			new GZIPInputStream(instream)));

			map  = (GameMap) infile.readObject();
			infile.close();

		} catch(Exception e) {
			System.err.println("Cannot open the map file from input stream");
			e.printStackTrace(System.err);
			return null;
		}
		
		// reset the terrain to pick up current values
		map.terrainList = Terrain.getTerrainList();

		return map;
	}

	/**
	* Writes the map to disk. File name must be fully qualified.
	* @param filename name of the file
	*/
	public void writeMap(String filename){

		ObjectOutputStream  outfile;

		// open output file and save map
		try {
			outfile =
			new ObjectOutputStream(
			new BufferedOutputStream(
			new GZIPOutputStream(
			new FileOutputStream(filename))));

			outfile.writeObject(this);
			outfile.close();
		} catch(Exception e) {
			System.err.println("Cannot save the map file: " + filename);
			e.printStackTrace(System.err);
		}
	}

	/**
	* Writes a version independant version of the map to disk.
	* File is written to the CASL/Maps directory.
	* @param filename name of the file (only)
	*/
	public void exportMap(String filename){

		ObjectOutputStream  outfile;

		// open output file and save map
		try {
			outfile =
			new ObjectOutputStream(
			new BufferedOutputStream(
			new GZIPOutputStream(
			new FileOutputStream(filename))));

			// export the size
			outfile.writeInt(width);
			outfile.writeInt(height);
			outfile.writeInt(gridWidth);
			outfile.writeInt(gridHeight);

			// export the terrain grids
			for (int x = 0; x < gridWidth; x++){
				for (int y = 0; y < gridHeight; y++){
					outfile.write(terrainGrid[x][y]);
					outfile.write(elevationGrid[x][y]);
				}
			}

			// export the hex grid
			Location l = null;
			for (int x = 0; x < hexGrid.length; x++) {
				for(int y = 0; y < hexGrid[x].length; y++) {

					// save misc hex information
					l = hexGrid[x][y].getCenterLocation();
					outfile.write(l.getHex().hasStairway() ? 1 : 0);
					outfile.write(l.getHex().hasTunnel() ? l.getHex().getTunnelLocation().getTerrain().getType() : 0);
					outfile.write(l.getHex().getBaseHeight());

					// save the center location
					outfile.write(l.getTerrain().getType());
					outfile.write(l.getDepressionTerrain() == null ? 0 : l.getDepressionTerrain().getType());
					outfile.write(l.isBypassAllowed() ? 1 : 0);
					outfile.write(l.hasTrailbreak() ? 1 : 0);
					outfile.write(l.hasPath() ? 1 : 0);
					outfile.write(l.hasBreach() ? 1 : 0);

					// save the hexside locations
					for (int s = 0; s < 6; s++) {

						l = hexGrid[x][y].getHexsideLocation(s);

						outfile.write(l.getHex().getEdgeTerrain(s) == null ? 0 : l.getHex().getEdgeTerrain(s).getType());
						outfile.write(l.getTerrain().getType());
						outfile.write(l.getDepressionTerrain() == null ? 0 : l.getDepressionTerrain().getType());
						outfile.write(l.isBypassAllowed() ? 1 : 0);
						outfile.write(l.hasTrailbreak() ? 1 : 0);
						outfile.write(l.hasPath() ? 1 : 0);
						outfile.write(l.hasBreach() ? 1 : 0);
					}
				}
			}

			outfile.close();

		} catch(Exception e) {
			System.err.println("Cannot export the map file: " + filename);
			e.printStackTrace(System.err);
		}
	}

	/**
	* Reads a version independant version of the map from disk.
	* File must be in the CASL/Maps directory.
	* @param filename name of the file (only)
	* @return <code>GameMap</code> GameMap object. Null if an error occured.
	*/
	public static GameMap importMap(String filename){

		ObjectInputStream   infile;
		GameMap map = null;

		// get the terrain array
		Terrain[] terrainList = Terrain.getTerrainList();
		try {
			infile =
			new ObjectInputStream(
			new BufferedInputStream(
			new GZIPInputStream(
			new FileInputStream(filename))));

			// get the size variables
			int w		= infile.readInt();
			int h		= infile.readInt();
			int gw	= infile.readInt();
			int gh	= infile.readInt();

			// create a new map
			map 		= new GameMap(w, h);

			// import the terrain grid
			for (int x = 0; x < gw; x++){
				for (int y = 0; y < gh; y++){
					map.terrainGrid[x][y] = (char) infile.read();
					map.elevationGrid[x][y] = (byte) infile.read();
				}
			}

			// import the hex grid
			Location l = null;
			for (int x = 0; x < map.hexGrid.length; x++) {
				for(int y = 0; y < map.hexGrid[x].length; y++) {

					// set misc hex information
					l = map.hexGrid[x][y].getCenterLocation();
					boolean stairway = infile.read() == 1 ? true : false;
//					if (infile.read() == 1) l.getHex().setStairway(true);
					byte b = infile.readByte();
					if (b != 0) l.getHex().addTunnel(terrainList[b]);

					l.getHex().setBaseHeight(infile.readByte());

					// need to rebuild upper building levels
					b = infile.readByte();
					if (terrainList[b].isBuildingTerrain()){

						l.getHex().setTerrain(terrainList[b]);
						l.getHex().setStairway(stairway);
					}
					// rebuild locations for shellholes and rice paddys
                    else if (map.terrainGrid[l.getLOSPoint().x][l.getLOSPoint().y] == Terrain.SHELL_HOLES ||
                             terrainList[map.terrainGrid[l.getLOSPoint().x][l.getLOSPoint().y]].isRicePaddy()){

                        l.getHex().setTerrain(terrainList[map.terrainGrid[l.getLOSPoint().x][l.getLOSPoint().y]]);
                    }
					else {

						l.setTerrain(terrainList[b]);
					}

					b = infile.readByte();
					if (b != 0) l.getHex().setDepressionTerrain(terrainList[b]);

					// set flags
					l.setBypassAllowed(infile.readByte() == 0 ? false : true);
					l.setTrailbreak(infile.readByte() == 0 ? false : true);
					l.setPath(infile.readByte() == 0 ? false : true);
					l.setBreach(infile.readByte() == 0 ? false : true);

					// read the hexside locations
					for (int s = 0; s < 6; s++) {

						l = map.hexGrid[x][y].getHexsideLocation(s);

						b = infile.readByte();
						if (b != 0) l.getHex().setEdgeTerrain(s, terrainList[b]);

						l.setTerrain(terrainList[infile.readByte()]);

						b = infile.readByte();
						if (b != 0) l.getHex().setHexsideDepressionTerrain(s);

						// set flags
						l.setBypassAllowed(infile.readByte() == 0 ? false : true);
						l.setTrailbreak(infile.readByte() == 0 ? false : true);
						l.setPath(infile.readByte() == 0 ? false : true);
						l.setBreach(infile.readByte() == 0 ? false : true);
					}
				}
			}

			infile.close();

		} catch(Exception e) {
			System.err.println("Cannot import the map file: " + filename);
			e.printStackTrace(System.err);
			return null;
		}
		return map;
	}
}

