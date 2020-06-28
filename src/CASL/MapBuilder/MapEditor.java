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

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.Scrollable;

import Acme.JPM.Encoders.GifEncoder;
import CASL.Map.Bridge;
import CASL.Map.GameMap;
import CASL.Map.Hex;
import CASL.Map.LOSResult;
import CASL.Map.Location;
import CASL.Map.MovementLogic;
import CASL.Map.MovementResult;
import CASL.Map.PrefabBuilding;
import CASL.Map.Smoke;
import CASL.Map.Terrain;
import CASL.MapBuilder.Utility.CASLProperties;
import CASL.Scenario.Scenario;
import CASL.Unit.Infantry;
import CASL.Unit.Unit;

/**
 * Title:        MapEditor.java
 * Copyright:    Copyright (c) 2001 David Sullivan Zuericher Strasse 6 12205 Berlin Germany. All rights reserved.
 * @author       David Sullivan
 * @version      1.0
 */
public class 	MapEditor
	extends 	JComponent
	implements  MouseListener,
				MouseMotionListener,
				Scrollable,
				KeyListener
	{
	  
	// synthetic field used to access text files in jar
    static Class thisClass;

	// status variables
	private boolean mapChanged		= false;
	private boolean mapOpen			= false;

	// file variables
	private File 	file;
	private String	fileName;
	private String	homeDirectory;

	// the map
	public GameMap 			map			= null;
	public BufferedImage	mapImage	= null;
	private int minDirtyX = -1;
	private int minDirtyY = -1;
	private int maxDirtyX = -1;
	private int maxDirtyY = -1;
	private BufferedImage terrainImages[] = new BufferedImage[256];
	private Image singleHexWoodenBridgeImage;
	private Image singleHexStoneBridgeImage;

	// scenario/unit stuff
	private Scenario	scenario 	= new Scenario();
	private Image 		unitImage;
	private Image 		vehImage;
	private int			unitSize	= 40;
	private MovementLogic	moveLogic	= new MovementLogic();
	private Unit			unit		= new Infantry((Location) null);
	private MovementResult 	moveResult	= null;

	// function variables
	private String		currentFunctionName	= "LOS";
	private String		currentTerrainName;
	private Terrain		currentTerrain;
	private String		currentToTerrainName;
	private Terrain		currentToTerrain;
	private String		currentBrush;
	private int			currentBrushSize;
	private int			currentGroundLevel;
	private int			currentToGroundLevel;
	private boolean		roundBrush		= false;
	private boolean		pointer			= false;
	private String		zoomSetting		= "100%";
	private int			rotation;

	// pseudo mouse cursors
	private Shape		cursorShape;

	private boolean useAuxTargetLOSPoint = false;

	// custom building variables
	private int	customBuildingWidth			= 32;
	private int	customBuildingHeight		= 32;
	private boolean customBuildingOn	= false;

	// prefab building variables
	private boolean prefabBuildingOn	= false;
	private int currentPrefabBuilding	= 0;
	private int MAX_PREFAB_BUILDINGS	= 500;
	private int MAX_BUILDING_POINTS		= 500;
	private PrefabBuilding[] prefabBuildingList = new PrefabBuilding[MAX_PREFAB_BUILDINGS];
	private Image[] prefabBuildingImageList = new Image[MAX_PREFAB_BUILDINGS];

	// custom bridge variables
	private Bridge currentBridge;
	private int	customBridgeRoadElevation	= 0;

	// road variables
	private int	roadWidth	= (int) Hex.WIDTH/6 + 1;
	private int	roadHeight	= (int) Hex.HEIGHT/2;
	private int	roadOffset	= 4;

	// selection list
	private LinkedList allSelections	= new LinkedList();

	// ZIP file archive
	private ZipFile archive;

	private boolean		doingLOS = false;
	private	int			targetX;
	private	int			targetY;
	private	LOSResult   result = new LOSResult();
	private	Location	targetLocation;

	private	MapEditFrame 	frame;

	private Dimension	   dim;

	public MapEditor() {

		enableEvents(AWTEvent.WINDOW_EVENT_MASK);

		try  {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void setFrame(MapEditFrame newFrame) {

		frame = newFrame;
	}


	// load the terrain graphics
	public void loadTerrainGraphics(){

		String sbuf[] = new String[256];
		String fileName = "CASL/init/TerrainImages.txt";

		String s;
		int 		current 	= 0;
		Graphics 	g;
		Image 		tempImage	= null;

		// open and read the file
		try  {

		    BufferedReader r = new BufferedReader(new InputStreamReader(getTextFile(fileName)));

			// read in the text line for each building
			while ((s = r.readLine( ) ) != null && current < 256) {

				sbuf[current++] = s;
			}
			r.close();
		}
		catch (IOException e)  {

			// handle error
			MapBuilder.writeError("Error reading the terrain images file " + fileName);
		}

		// get the images
		current = 0;
		while (sbuf[current] != null && current < 256) {

			String terrainName		= "";
			String imageFileName	= "";

			// get the terrain and image names
			try {
				terrainName		= sbuf[current].substring(0, sbuf[current].indexOf('|'));
				imageFileName	= sbuf[current].substring(sbuf[current].indexOf('|') + 1);
			}
			catch(Exception e){

				MapBuilder.writeError("Line " + (current + 1) + ": Cannot read the line... " + sbuf[current]);
			}

			// find the terrain
			Terrain t = Terrain.getTerrain(terrainName);
			if (t == null) {

				MapBuilder.writeError("Line " + (current + 1) + ": Terrain not found - " + terrainName);
			}

			// load the graphic
			else {

				// get the image from the file
				try{

					tempImage = getImage("CASL/images/terrain/" + imageFileName);

				    // no buffered image for bridges (allow for transparency)
				    if (t.getType() == Terrain.SINGLE_HEX_WOODEN_BRIDGE){

						singleHexWoodenBridgeImage = tempImage;
				    }
				    else if (t.getType() == Terrain.SINGLE_HEX_STONE_BRIDGE ){

						singleHexStoneBridgeImage = tempImage;
				    }
				    else {

						// draw it into the buffered image
						terrainImages[t.getType()] = new BufferedImage(tempImage.getWidth(this), tempImage.getHeight(this), BufferedImage.TYPE_3BYTE_BGR);
						g = terrainImages[t.getType()].getGraphics();
						g.drawImage(tempImage, 0, 0, this);

						// free up resources
						g.dispose();
				    }

				}
				catch (Exception e){
					terrainImages[t.getType()] = null;
					MapBuilder.writeError("Line " + (current + 1) + ": Cannot find terrain image file " + imageFileName);
				}
			}

			current++;
		}
	}

	// load the custom buildings
	public void loadPrefabBuildings(){

		String sbuf[] = new String[MAX_PREFAB_BUILDINGS];
		String fileName = "CASL/init/PrefabBuildings.txt";

		String s;
		int current = 0;

		// open and read the file
		try  {

		    BufferedReader r = new BufferedReader(new InputStreamReader(getTextFile(fileName)));

			// read in the text line for each building
			while ((s = r.readLine( ) ) != null && current < MAX_PREFAB_BUILDINGS) {

				sbuf[current++] = s;
			}
			r.close();
		}
		catch (IOException e)  {

			// handle error
			MapBuilder.writeError("Error reading the prefab building file " + fileName);
		}

		// create the custom buildings
		current = 0;
		PrefabBuilding building;
		while (sbuf[current] != null && current < MAX_PREFAB_BUILDINGS) {

			prefabBuildingList[current] = createBuildingFromText(sbuf[current], current);

			if (prefabBuildingList[current] != null) {

				// get the image
				try{
					prefabBuildingImageList[current] = getImage("CASL/images/terrain/" + prefabBuildingList[current].getImageName());
					prefabBuildingList[current].setFileIndex(current);
				}
				catch (Exception e){
					MapBuilder.writeError("Cannot find the image file for prefab building " + prefabBuildingList[current].getImageName());
				}
			}
			current++;
		}
	}

	private PrefabBuilding createBuildingFromText(String line, int lineNumber){

		String 	imageName;
		String[]	pointTokens = new String[256];
		Terrain	terrain;
		String 	buildingName = null;
		int		terr 		= 0;
		int[]		xPoints	= new int[MAX_BUILDING_POINTS];
		int[]		yPoints	= new int[MAX_BUILDING_POINTS];

		// get the building name
		try {
			buildingName = line.substring(0, line.indexOf('|'));
			line = line.substring(line.indexOf('|') + 1);
		}
		catch(Exception e){

			MapBuilder.writeError("Line " + (lineNumber + 1) + ": Cannot read the file name ");
			return null;
		}

		// read the terrain type
		try {
			char stoneWooden = line.charAt(0);
			line = line.substring(line.indexOf('|') + 1);
			char levels = line.charAt(0);
			line = line.substring(line.indexOf('|') + 1);

			if (stoneWooden == 'W' || stoneWooden == 'w') {

				switch(levels) {

					case '0' :
						terr = Terrain.WOODEN_BUILDING; break;

					case '1' :
						terr = Terrain.WOODEN_BUILDING_1_LEVEL; break;

					case '2' :
						terr = Terrain.WOODEN_BUILDING_2_LEVEL; break;

					case '3' :
						terr = Terrain.WOODEN_BUILDING_3_LEVEL; break;

					case '4' :
						terr = Terrain.WOODEN_BUILDING_4_LEVEL; break;

					default:
						MapBuilder.writeError("Line " + (lineNumber + 1) + ": Invalid levels code (" + levels + ")");
						return null;
				}
			}
			else if (stoneWooden == 'S' || stoneWooden == 's') {

				switch(levels) {

					case '0' :
						terr = Terrain.STONE_BUILDING; break;

					case '1' :
						terr = Terrain.STONE_BUILDING_1_LEVEL; break;

					case '2' :
						terr = Terrain.STONE_BUILDING_2_LEVEL; break;

					case '3' :
						terr = Terrain.STONE_BUILDING_3_LEVEL; break;

					case '4' :
						terr = Terrain.STONE_BUILDING_4_LEVEL; break;

					default:
						MapBuilder.writeError("Line " + (lineNumber + 1) + ": Invalid levels code (" + levels + ")");
						return null;
				}
			}
			else if (stoneWooden == 'F' || stoneWooden == 'f') {

				switch(levels) {

					case '1' :
						terr = Terrain.STONE_FACTORY_1_5_LEVEL; break;

					case '2' :
						terr = Terrain.STONE_FACTORY_2_5_LEVEL; break;

					default:
						MapBuilder.writeError("Line " + (lineNumber + 1) + ": Invalid levels code (" + levels + ")");
						return null;
				}
			}
			else if (stoneWooden == 'C' || stoneWooden == 'c') {

				switch(levels) {

					case '1' :
						terr = Terrain.WOODEN_FACTORY_1_5_LEVEL; break;

					case '2' :
						terr = Terrain.WOODEN_FACTORY_2_5_LEVEL; break;

					default:
						MapBuilder.writeError("Line " + (lineNumber + 1) + ": Invalid levels code (" + levels + ")");
						return null;
				}
			}
			else {
				MapBuilder.writeError("Line " + (lineNumber + 1) + ": Invalid terrain type code (" + stoneWooden + ")");
				return null;
			}
		}
		catch(Exception e){

			MapBuilder.writeError("Line " + (lineNumber + 1) + ": Cannot determine the terrain type");
			MapBuilder.writeException(e);
			return null;
		}

		// get the building points
		int currentPoint = 0;
		try {
			while (!line.equals("")) {

				xPoints[currentPoint] = Integer.parseInt(line.substring(0, line.indexOf(',')));
				yPoints[currentPoint] = Integer.parseInt(line.substring(line.indexOf(',') + 1, line.indexOf('|')));

				line = line.substring(line.indexOf('|') + 1);
				currentPoint++;
			}
		}
		catch(Exception e){

			MapBuilder.writeError("Line " + (lineNumber + 1) + ": Cannot read point " + (currentPoint + 1));
			MapBuilder.writeException(e);
			return null;
		}

		// create the building
		return new PrefabBuilding(terr, new Polygon(xPoints, yPoints, currentPoint), buildingName);
	}

	//Component initialization
	private void jbInit() throws Exception  {

		// set the home directory
		homeDirectory = CASLProperties.getCASLHome();

		this.setMinimumSize(new Dimension(100, 100));
		this.setEnabled(true);
		adjustMapViewSize();
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		// set up the image archive
		try {
//		    String s = CASLProperties.getCASLHome() + "/CASLData.zip";
//		    s = "D:/VASSAL/CASL/CASLData.zip";
//			archive = new ZipFile(new File(s));
			archive = new ZipFile(CASLProperties.getCASLHome() + System.getProperty("file.separator","\\") + "CASLData.zip");

		} catch (IOException e) {

		    MapBuilder.writeError("Cannot read the archive file CASLData.zip");
		}

		// set the images
		unitImage = getImage("CASL/images/6_6_7.jpg");
		vehImage = getImage("CASL/images/M4.jpg");

	}

	public void 	setFileName(String newFileName)	{fileName = newFileName;}
	public String	getFileName()					{return fileName;}
	public boolean	isMapOpen()						{return mapOpen;}
	public boolean	isMapChanged()					{return mapChanged;}

	public void adjustMapViewSize() {

		// adjust window to map size
		if (map == null) {

			this.setPreferredSize(new Dimension(0, 0));
			this.revalidate();
		}
		else {
			dim = new Dimension(map.getImageWidth(), map.getImageHeight());
   			this.setPreferredSize(dim);
			this.revalidate();
		}
	}

	public void paint(Graphics g) {

		// is the map open?
		if (!mapOpen) return;

		// paint the map
		Graphics2D  screen2D = (Graphics2D) g;
		if (zoomSetting.equals("100%")) {

			screen2D.drawImage(mapImage, 0, 0, this);
		}
		else if (zoomSetting.equals("200%")) {

                        screen2D.drawImage(mapImage, 0, 0, mapImage.getWidth(), mapImage.getHeight(), 0, 0, mapImage.getWidth()/2, mapImage.getHeight()/2, this);
		}
		else if (zoomSetting.equals("50%")) {

			screen2D.drawImage(mapImage, 0, 0, mapImage.getWidth()/2, mapImage.getHeight()/2, this);
		}

		// paint the scenario units
		screen2D.setColor(Color.white);
		Iterator units = scenario.getUnits().iterator();
		while(units.hasNext()){

			Unit u = (Unit) units.next();
			screen2D.drawImage(
				vehImage,
				(int)u.getLocation().getUnitLocationPoint().getX() - 24,
				(int)u.getLocation().getUnitLocationPoint().getY() - 24,
				this);
		}

		if(currentFunctionName.equals("LOS")){

			screen2D.drawImage(
				unitImage,
				(int)unit.getLocation().getUnitLocationPoint().getX() - unitSize/2,
				(int)unit.getLocation().getUnitLocationPoint().getY() - unitSize/2,
				this);

				// set level color
				switch(unit.getLocation().getBaseHeight() + unit.getLocation().getHex().getBaseHeight()){

					case  -1:
					case  -2:
						screen2D.setColor(Color.red);
						break;
					case  0:
						screen2D.setColor(Color.gray);
						break;
					case 1:
						screen2D.setColor(Color.darkGray);
						break;
					case 2:
						screen2D.setColor(Color.black);
						break;
					default:
						screen2D.setColor(Color.white);
				}
				screen2D.drawString(
					"Level " + ((int)unit.getLocation().getBaseHeight() + unit.getLocation().getHex().getBaseHeight()),
					(int) unit.getLocation().getUnitLocationPoint().getX() - unitSize/2,
					(int) unit.getLocation().getUnitLocationPoint().getY() + unitSize/2 + 15
				);


			if(doingLOS) {

			if (result.isBlocked()) {
				if (result.hasHindrance()){

					screen2D.setColor(Color.white);
					screen2D.drawLine(
						(int) unit.getLocation().getLOSPoint().getX(),
						(int) unit.getLocation().getLOSPoint().getY(),
						(int) result.firstHindranceAt().getX(),
						(int) result.firstHindranceAt().getY());

					screen2D.setColor(Color.red);
					screen2D.drawLine(
						(int) result.firstHindranceAt().getX(),
						(int) result.firstHindranceAt().getY(),
						(int) result.getBlockedAtPoint().getX(),
						(int) result.getBlockedAtPoint().getY());

					screen2D.setColor(Color.black);
					screen2D.drawLine(
						(int) result.getBlockedAtPoint().getX(),
						(int) result.getBlockedAtPoint().getY(),
						targetX,
						targetY);
				}
				else {
					screen2D.setColor(Color.white);
					screen2D.drawLine(
						(int) unit.getLocation().getLOSPoint().getX(),
						(int) unit.getLocation().getLOSPoint().getY(),
						(int) result.getBlockedAtPoint().getX(),
						(int) result.getBlockedAtPoint().getY());

					screen2D.setColor(Color.black);
					screen2D.drawLine(
						(int) result.getBlockedAtPoint().getX(),
						(int) result.getBlockedAtPoint().getY(),
						targetX,
						targetY);
				}
			}
			else if (result.hasHindrance()) {

				screen2D.setColor(Color.white);
				screen2D.drawLine(
					(int) unit.getLocation().getLOSPoint().getX(),
					(int) unit.getLocation().getLOSPoint().getY(),
					(int) result.firstHindranceAt().getX(),
					(int) result.firstHindranceAt().getY());

				screen2D.setColor(Color.red);
				screen2D.drawLine(
					(int) result.firstHindranceAt().getX(),
					(int) result.firstHindranceAt().getY(),
					targetX,
					targetY);
			}
			else {

				screen2D.setColor(Color.white);
				screen2D.drawLine(
					(int) unit.getLocation().getLOSPoint().getX(),
					(int) unit.getLocation().getLOSPoint().getY(),
					targetX,
					targetY);
			}
			}
		}

		else {
			Iterator hiter = allSelections.iterator();
			while(hiter.hasNext()){

				((Selection) hiter.next()).paint(screen2D);
			}
		}

		// show mouse pseudo cursor
		if (pointer){
			if(cursorShape != null){

				// red square
				screen2D.setColor(Color.white);
				screen2D.draw(cursorShape);
			}
		}

		// free resources
		screen2D.dispose();
	}

	public void update(Graphics screen) {
		paint(screen);
	}

	/******************************
	Mouse methods
	******************************/
	public void mouseReleased(MouseEvent e) {

		// is the map open?
		if (!mapOpen) return;

		// right mouse button for menu?
		if (e.isPopupTrigger()){

			// create the menu
			JPopupMenu	menu = new JPopupMenu();
			menu.setFont(new java.awt.Font("Dialog", 0, 11));
			menu.add(new PopupMenuAction("Flip bypass flag", this, e.getX(), e.getY()));
			menu.addSeparator();

			// show menu
			menu.show(e.getComponent(), e.getX(), e.getY());
			return;
		}

		if (map.onMap(e.getX(), e.getY())) {
			if(currentFunctionName.equals("LOS")){

					doingLOS	= false;
			}
			else if(	currentFunctionName.equals("Set ground level") ||
					currentFunctionName.equals("Add terrain")){

				// custom building?
				if(customBuildingOn){

					// create, rotate and add the rectangle
					AffineTransform at = AffineTransform.getRotateInstance(
						Math.toRadians(rotation),
						(int) e.getX(),
						(int) e.getY()
					);
					allSelections.add(new RectangularSelection(
						at.createTransformedShape(new Rectangle(
						(int) e.getX() - customBuildingWidth/2,
						(int) e.getY() - customBuildingHeight/2,
						customBuildingWidth,
						customBuildingHeight)),
						false,
						false
					));
				}

				// prefab building?
				else if(prefabBuildingOn){

					// create and initialize the new building
					prefabBuildingList[currentPrefabBuilding].setCenter((int) e.getX(), (int) e.getY());
					Shape s  = prefabBuildingList[currentPrefabBuilding].getShape();
					PrefabBuilding building = new PrefabBuilding(
						prefabBuildingList[currentPrefabBuilding].getTerrainType(),
						prefabBuildingList[currentPrefabBuilding].getShapePolygon(),
						prefabBuildingList[currentPrefabBuilding].getImageName());
					building.setRotation(rotation);
					building.setCenter((int) e.getX(), (int) e.getY());
					building.setFileIndex(prefabBuildingList[currentPrefabBuilding].getFileIndex());

					allSelections.add(new PrefabBuildingSelection(s, building));
				}

				// full hex selected?
				else if(currentBrush.equals("Hex")){

					Hex h = map.gridToHex(e.getX(), e.getY());

					// mark the hex
					allSelections.add(new HexSelection(h.getExtendedHexBorder(), h));

				}
				else{
					int currentX	= (int) e.getX() - currentBrushSize/2;
					int currentY	= (int) e.getY() - currentBrushSize/2;

					// need to rotate?
					if (rotation != 0 && ! roundBrush){

						// create, rotate and add the rectangle
						AffineTransform at = AffineTransform.getRotateInstance(
							Math.toRadians(rotation),
							(int) e.getX(),
							(int) e.getY()
						);
						allSelections.add(new RectangularSelection(
							at.createTransformedShape(new Rectangle(
							(int) e.getX() - currentBrushSize/2,
							(int) e.getY() - currentBrushSize/2,
							currentBrushSize,
							currentBrushSize)),
							roundBrush,
							false
						));
					}
					else {

						// create the rectangle and add
						Rectangle rect = new Rectangle(currentX, currentY, currentBrushSize, currentBrushSize);
						allSelections.add(new RectangularSelection(rect, roundBrush, true));
					}
				}
			}
			else if(currentFunctionName.equals("Add hexside terrain")){

				Location sourceLocation = map.gridToHex(e.getX(), e.getY()).nearestLocation(e.getX(), e.getY());
				Hex hex = sourceLocation.getHex();

				//ignore the center location
				if(hex.isHexsideLocation(sourceLocation)){

					// create a hexside rectangles
					Rectangle paintRect = new Rectangle(
			 			(int) sourceLocation.getEdgeCenterPoint().getX() - Terrain.EDGE_TERRAIN_WIDTH/2,
			 			(int) sourceLocation.getEdgeCenterPoint().getY() - Terrain.EDGE_TERRAIN_HEIGHT/2,
			 			Terrain.EDGE_TERRAIN_WIDTH,
			 			Terrain.EDGE_TERRAIN_HEIGHT
					);
					Rectangle gridRect = new Rectangle(
			 			(int) sourceLocation.getEdgeCenterPoint().getX() - Terrain.EDGE_TERRAIN_WIDTH/2 - 1,
			 			(int) sourceLocation.getEdgeCenterPoint().getY() - Terrain.EDGE_TERRAIN_HEIGHT/2 -1,
			 			Terrain.EDGE_TERRAIN_WIDTH + 1,
			 			Terrain.EDGE_TERRAIN_HEIGHT + 1
					);

					// need to rotate?
					int degrees	= 0;
					int side 	= hex.getLocationHexside(sourceLocation);
					switch(side) {
						case 1:
						case 4:
							degrees =  60; break;
						case 2:
						case 5:
							degrees = -60; break;
					}

					// rotate the rectangle
					AffineTransform at = AffineTransform.getRotateInstance(
						Math.toRadians(degrees),
						sourceLocation.getEdgeCenterPoint().getX(),
						sourceLocation.getEdgeCenterPoint().getY()
					);
					allSelections.add(new HexsideSelection(
						at.createTransformedShape(paintRect),
						at.createTransformedShape(gridRect),
						sourceLocation
					));
				}
			}

			else if(currentFunctionName.equals("Add bridge")){

				Hex h = map.gridToHex(e.getX(), e.getY());
				// remove?
				if (currentTerrain == null){

					allSelections.add(new HexSelection(
						new Rectangle((int) h.getCenterLocation().getLOSPoint().getX() - 8, (int) h.getCenterLocation().getLOSPoint().getY() - 8, 16, 16),
						h
					));
				}
				else {
					allSelections.add(new BridgeSelection(new Bridge(
					    currentBridge.getTerrain(),
						this.customBridgeRoadElevation,
					    currentBridge.getRotation(),
					    currentBridge.getLocation(),
					    currentBridge.isSingleHex(),
					    currentBridge.getCenter()
					)));
				}
			}

			else if(currentFunctionName.equals("Add road")){

				Location sourceLocation = map.gridToHex(e.getX(), e.getY()).nearestLocation(e.getX(), e.getY());
				Hex hex = sourceLocation.getHex();

				// only place elevated roads on level 0
				if (hex.getCenterLocation().getTerrain().getType() != Terrain.ELEVATED_ROAD &&
					currentTerrain.getType() == Terrain.ELEVATED_ROAD &&
					hex.getBaseHeight() != 0)
					return;

				//ignore the center location
				if(hex.isHexsideLocation(sourceLocation)){

					// create the road rectangle
					Rectangle roadRect = new Rectangle(
					 			(int) hex.getCenterLocation().getLOSPoint().getX() - roadWidth/2,
					 			(int) hex.getCenterLocation().getLOSPoint().getY() - roadHeight - 1,
					 			roadWidth,
			 					roadHeight + roadOffset
							);

					Rectangle elevationRect = new Rectangle(
					 			(int) hex.getCenterLocation().getLOSPoint().getX() - roadWidth/2 - 4,
					 			(int) hex.getCenterLocation().getLOSPoint().getY() - roadHeight - 1,
					 			roadWidth + 8,
			 					roadHeight + roadOffset
							);

					// need to rotate?
					int degrees	= 0;
					int side 	= hex.getLocationHexside(sourceLocation);
					switch(side) {
						case 1:
							degrees =  60; break;
						case 2:
							degrees =  120; break;
						case 3:
							degrees =  180; break;
						case 4:
							degrees =  -120; break;
						case 5:
							degrees = -60; break;
					}

					// rotate the rectangle
					AffineTransform at = AffineTransform.getRotateInstance(
						Math.toRadians(degrees),
						hex.getCenterLocation().getLOSPoint().getX(),
						hex.getCenterLocation().getLOSPoint().getY()
					);
					allSelections.add(new HexsideSelection(
						at.createTransformedShape(roadRect),
						at.createTransformedShape(elevationRect),
						sourceLocation
					));
				}
			}
			else if(currentFunctionName.equals("Add objects")){

				// mark the hex
				Hex h = map.gridToHex(e.getX(), e.getY());
				allSelections.add(new HexSelection(
					new Rectangle((int) h.getCenterLocation().getLOSPoint().getX() - 8, (int) h.getCenterLocation().getLOSPoint().getY() - 8, 16, 16),
					h
				));
			}

			repaint();
		}
	}

	public void mousePressed(MouseEvent e) {
		if(currentFunctionName.equals("LOS")){

			doingLOS	= true;
			mouseDragged(e);

		}
		requestFocus();
	}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {

		int mouseX = e.getX();
		int mouseY = e.getY();

		// is the map open?
		if (!mapOpen) return;
		if (!map.onMap(mouseX, mouseY)) return;

		Location newLocation = map.gridToHex(mouseX, mouseY).nearestLocation(mouseX, mouseY);

		// set the pseudo mouse cursor
		if(	currentFunctionName.equals("Set ground level") ||
			currentFunctionName.equals("Add terrain")){

			if (customBuildingOn){

				// create and rotate the rectangle
				AffineTransform at = AffineTransform.getRotateInstance(
					Math.toRadians(rotation),
					(int) e.getX(),
					(int) e.getY()
				);
				cursorShape = at.createTransformedShape(new Rectangle(
					(int) e.getX() - customBuildingWidth/2,
					(int) e.getY() - customBuildingHeight/2,
					customBuildingWidth,
					customBuildingHeight));

			}
			else if (prefabBuildingOn){

				prefabBuildingList[currentPrefabBuilding].setCenter((int) e.getX(), (int) e.getY());
				cursorShape = prefabBuildingList[currentPrefabBuilding].getShape();

			}
			else if (roundBrush){

				// set the cursor rectangle
				cursorShape = new Ellipse2D.Float(
					(float) mouseX - currentBrushSize/2,
					(float) mouseY - currentBrushSize/2,
					(float) currentBrushSize,
					(float) currentBrushSize
				);

			}
			else {
				// create and rotate the rectangle
				AffineTransform at = AffineTransform.getRotateInstance(
					Math.toRadians(rotation),
					(int) e.getX(),
					(int) e.getY()
				);

				// set the cursor rectangle
				cursorShape = at.createTransformedShape(new Rectangle (
					((int) mouseX - currentBrushSize/2),
					((int) mouseY - currentBrushSize/2),
					currentBrushSize,
					currentBrushSize
				));
			}
			repaint();
		}
		else if(currentFunctionName.equals("Add bridge")){

		    if (currentBridge != null) {

				currentBridge.setCenter(e.getPoint());
				currentBridge.setRotation(rotation);
				currentBridge.setLocation(map.gridToHex((int) e.getX(), (int) e.getY()).getCenterLocation());

				cursorShape = currentBridge.getShape();
				repaint();
		    }
		}
		else {
			cursorShape = null;
		}

		targetLocation = newLocation;
		String cursorString =
			" X: " + mouseX 	+
			" Y: " + mouseY 	+
			" Z: "	+ ( map.getGridGroundLevel(mouseX,mouseY) +
									map.getGridTerrain(mouseX,mouseY).getHeight()) +
			" ("	+ map.getGridTerrain(mouseX,mouseY).getName() +
			")";

		String locationString = " | Location: "	+ " " + targetLocation.getName();

		String heightString =
			" - Height: "		+ (targetLocation.getHex().getBaseHeight() + targetLocation.getBaseHeight());

		String terrainString =  " - Terrain:  "	+ targetLocation.getTerrain().getName();

		// depression terrain?
		if(targetLocation.getDepressionTerrain() != null){
			terrainString += "/" + targetLocation.getDepressionTerrain().getName();
		}

		// edge/cliff terrain?
		if(!targetLocation.getHex().isCenterLocation(targetLocation)){
			if(targetLocation.getHex().getEdgeTerrain(targetLocation.getHex().getLocationHexside(targetLocation)) != null){
				terrainString += "/" + targetLocation.getHex().getEdgeTerrain(targetLocation.getHex().getLocationHexside(targetLocation)).getName();
			}
			if(targetLocation.getHex().hasCliff(targetLocation.getHex().getLocationHexside(targetLocation))){
				terrainString += "/" + Terrain.getTerrainList()[Terrain.CLIFF].getName();
			}
		}

		//Bridge?
		if(targetLocation.getHex().hasBridge()){
			terrainString += "/" + targetLocation.getHex().getBridge().getTerrain().getName();
		}
		String bypassString =  " | Bypass:  "	+ targetLocation.isBypassAllowed();

		String tempString = " | isAdjacent to F2:" + map.isAdjacentHexside(map.getHex("F2"), targetLocation) + " | isHexspine to F2:" + map.isHexspine(map.getHex("F2"), targetLocation);
		frame.setStatusBarText(cursorString + locationString + heightString + terrainString + bypassString + tempString);
	}

	public void mouseDragged(MouseEvent e) {

		int mouseX = e.getX();
		int mouseY = e.getY();

		// is the map open?
		if (!mapOpen) return;
		if (!map.onMap(mouseX, mouseY)) return;

		Location newLocation = map.gridToHex(mouseX, mouseY).nearestLocation(mouseX, mouseY);

		if(currentFunctionName.equals("LOS")){

			if (doingLOS) {

				useAuxTargetLOSPoint = false;
				Point LOSPoint = newLocation.getLOSPoint();

				// ensure the LOS point is on the map
				if (!map.onMap((int) newLocation.getLOSPoint().getX(), (int) newLocation.getLOSPoint().getY())){

					LOSPoint = newLocation.getAuxLOSPoint();
					useAuxTargetLOSPoint = true;
				}
				// use the closest LOS point
				else if (Point.distance(mouseX, mouseY, (int) newLocation.getLOSPoint().getX(), (int) newLocation.getLOSPoint().getY()) >
				    	 Point.distance(mouseX, mouseY, (int) newLocation.getAuxLOSPoint().getX(), (int) newLocation.getAuxLOSPoint().getY())){

					LOSPoint = newLocation.getAuxLOSPoint();
					useAuxTargetLOSPoint = true;

				}

				// are we really in a new location?
				if (targetLocation == newLocation && targetX == (int) LOSPoint.getX() && targetY == (int) LOSPoint.getY()) {
					return;
				}

				targetLocation = newLocation;

           		// if Ctrl click, use upper location
                if (e.isControlDown()){
                    while(targetLocation.getUpLocation() != null){
                        targetLocation = targetLocation.getUpLocation();
                    }
                }

				targetX = (int) LOSPoint.getX();
				targetY = (int) LOSPoint.getY();

				map.LOS(unit.getLocation(), false, targetLocation, useAuxTargetLOSPoint, result, scenario);

				if (result.isBlocked()){
					frame.setStatusBarText(
						"Blocked at "	+ (int) result.getBlockedAtPoint().getX() + ", "
										+ (int) result.getBlockedAtPoint().getY() +
						" Reason: " 	+ result.getReason()
					);
				}
				else {
					frame.setStatusBarText(
						" Hindrances: " 		+ result.getHindrance() +
						" Continuous slope: " 	+ result.isContinuousSlope() +
						" Range: " 				+ result.getRange());
				}

				repaint();
				return;
			}
		}
		else {

			mouseReleased(e);
		}
	}

	public Dimension getPreferredScrollableViewportSize(){
		return dim;
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		if (map == null) {
			return 0;
		}
		else {
			return (int) Hex.WIDTH;
		}
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction){
		if (map == null) {
			return 0;
		}
		else {
			return 200;
		}
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public	 boolean getScrollableTracksViewportHeight(){
		return false;
	}

	public void setCurrentFunction(String newCurrentFunction){

		clearSelections();
		frame.setStatusBarText("  ");

		if (newCurrentFunction.equals("LOS")) {
				if (!currentFunctionName.equals("LOS")){

				requestFocus();
				doingLOS = false;
				currentFunctionName = newCurrentFunction;
			}
		}
		else if (newCurrentFunction.equals("Set ground level")) {
			if (!currentFunctionName.equals("Set ground level")) {

				currentFunctionName 	= newCurrentFunction;
				currentGroundLevel	= 0;
				currentToGroundLevel	= 0;
			}
		}
		else if (newCurrentFunction.equals("Add hexside terrain")) {
			if (!currentFunctionName.equals("Add hexside terrain")) {
				currentFunctionName 	= newCurrentFunction;

				// start with wall terrain
				currentTerrain			= map.getTerrain(Terrain.WALL);
				currentTerrainName		= "Wall";
				currentToTerrain			= map.getTerrain(Terrain.WALL);
				currentToTerrainName		= "Wall";
			}
		}
		else if (newCurrentFunction.equals("Add terrain")) {
			if (!currentFunctionName.equals("Add terrain")) {
				currentFunctionName 	= newCurrentFunction;

				// start with open ground
				currentTerrain			= map.getTerrain(Terrain.OPEN_GROUND);
				currentTerrainName		= "Open ground";
				currentToTerrain			= map.getTerrain(Terrain.OPEN_GROUND);
				currentToTerrainName		= "Open ground";
				customBuildingOn			= false;
				prefabBuildingOn			= false;
			}
		}
		else if (newCurrentFunction.equals("Add bridge")) {
			if (!currentFunctionName.equals("Add bridge")) {
				currentFunctionName 	= newCurrentFunction;
				currentTerrain 		= map.getTerrain(Terrain.SINGLE_HEX_WOODEN_BRIDGE);
				currentTerrainName	= "Wooden Building, One level";
			}
		}
		else if (newCurrentFunction.equals("Add road")) {
			if (!currentFunctionName.equals("Add road")) {
				currentFunctionName 	= newCurrentFunction;
				currentTerrain 		= map.getTerrain(Terrain.DIRT_ROAD);
				currentTerrainName	= "Dirt Road";
				currentToTerrain 		= map.getTerrain(Terrain.DIRT_ROAD);
				currentToTerrainName	= "Dirt Road";
			}
		}
		else if (newCurrentFunction.equals("Add objects")) {
			if (!currentFunctionName.equals("Add objects")) {
				currentFunctionName 	= newCurrentFunction;
				currentTerrain 		= map.getTerrain(Terrain.FOX_HOLES);
				currentTerrainName	= "Foxholes";
			}
		}

		repaint();
	}

	public String getCurrentFunctionName(){

		return currentFunctionName;
	}

	public void setCurrentTerrain(String newCurrentTerrain){

		currentTerrainName = newCurrentTerrain;

		if (currentFunctionName.equals("Set ground level")) {

			// set the current ground level
			if(currentTerrainName.equals("Hill Level 0")) {
				currentTerrain = null;
				currentGroundLevel = 0;
			}
			else if(currentTerrainName.equals("Hill Level 1")) {
				currentTerrain = null;
				currentGroundLevel = 1;
			}
			else if(currentTerrainName.equals("Hill Level 2")) {
				currentTerrain = null;
				currentGroundLevel = 2;
			}
			else if(currentTerrainName.equals("Hill Level 3")) {
				currentTerrain = null;
				currentGroundLevel = 3;
			}
			else if(currentTerrainName.equals("Hill Level 4")) {
				currentTerrain = null;
				currentGroundLevel = 4;
			}
			else if(currentTerrainName.equals("Hill Level 5")) {
				currentTerrain = null;
				currentGroundLevel = 5;
			}
			else if(currentTerrainName.equals("Hill Level 6")) {
				currentTerrain = null;
				currentGroundLevel = 6;
			}
			else if(currentTerrainName.equals("Hill Level 7")) {
				currentTerrain = null;
				currentGroundLevel = 7;
			}
			else if(currentTerrainName.equals("Hill Level 8")) {
				currentTerrain = null;
				currentGroundLevel = 8;
			}
			else if(currentTerrainName.equals("Hill Level 9")) {
				currentTerrain = null;
				currentGroundLevel = 9;
			}
			else if(currentTerrainName.equals("Hill Level 10")) {
				currentTerrain = null;
				currentGroundLevel = 10;
			}
			else if(currentTerrainName.equals("Valley -1")) {
				currentTerrain = null;
				currentGroundLevel = -1;
			}
			else if(currentTerrainName.equals("Valley -2")) {
				currentTerrain = null;
				currentGroundLevel = -2;
			}
			else if(currentTerrainName.equals("Gully")) {
				currentTerrain = map.getTerrain(Terrain.GULLY);
				currentGroundLevel = -1;
			}
			else if(currentTerrainName.equals("Dry Stream")) {
				currentTerrain = map.getTerrain(Terrain.DRY_STREAM);
				currentGroundLevel = -1;
			}
			else if(currentTerrainName.equals("Shallow Stream")) {
				currentTerrain = map.getTerrain(Terrain.SHALLOW_STREAM);
				currentGroundLevel = -1;
			}
			else if(currentTerrainName.equals("Deep Stream")) {
				currentTerrain = map.getTerrain(Terrain.DEEP_STREAM);
				currentGroundLevel = -1;
			}
			else if(currentTerrainName.equals("Wadi")) {
				currentTerrain = map.getTerrain(Terrain.WADI);
				currentGroundLevel = -1;
			}
		}
		else if (currentFunctionName.equals("Add hexside terrain")) {

			Terrain t = map.getTerrain(currentTerrainName);

			if(currentTerrainName.equals("Remove")){
				currentTerrain = null;
			}
			else if (t == null){

				frame.setStatusBarText("Terrain " + currentTerrainName + " not found. Terrain set to 'Wall'.");
				currentTerrain = map.getTerrain(Terrain.WALL);
				currentTerrainName = "Wall";
			}
			else {

				currentTerrain = t;
			}
		}
		else if (currentFunctionName.equals("Add terrain")) {

			Terrain t = map.getTerrain(currentTerrainName);

			if(currentTerrainName.equals("Prefab Building")){

				// ensure the current prefab building is valid
				if (prefabBuildingList[currentPrefabBuilding] == null) {

					frame.setStatusBarText("No prefab building defined. Ensure file definitions where loaded.");
				}
				else {
					currentTerrain = map.getTerrain(prefabBuildingList[currentPrefabBuilding].getTerrainType());
				}
			}
			else if (t == null){

				frame.setStatusBarText("Terrain " + currentTerrainName + " not found. Terrain set to 'Open Ground'.");
				currentTerrain = map.getTerrain(Terrain.OPEN_GROUND);
				currentTerrainName = "Open Ground";
			}
			else {

				currentTerrain = t;
			}

		}

		else if (currentFunctionName.equals("Add bridge")) {


			if(currentTerrainName.equals("Remove")){
				currentTerrain = null;
				currentBridge = null;
			}
			else if(currentTerrainName.equals("Single Hex Wooden Bridge")){
				currentTerrain = map.getTerrain(Terrain.SINGLE_HEX_WOODEN_BRIDGE);
				currentBridge  = new Bridge(currentTerrain, customBridgeRoadElevation, rotation, null, true);
			}
			else if(currentTerrainName.equals("Single Hex Stone Bridge")){
				currentTerrain = map.getTerrain(Terrain.SINGLE_HEX_STONE_BRIDGE);
				currentBridge  = new Bridge(currentTerrain, customBridgeRoadElevation, rotation, null, true);
			}
			else if(currentTerrainName.equals("Wooden Bridge")){
				currentTerrain = map.getTerrain(Terrain.WOODEN_BRIDGE);
				currentBridge  = new Bridge(currentTerrain, customBridgeRoadElevation, rotation, null, true);
			}
			else if(currentTerrainName.equals("Stone Bridge")){
				currentTerrain = map.getTerrain(Terrain.STONE_BRIDGE);
				currentBridge  = new Bridge(currentTerrain, customBridgeRoadElevation, rotation, null, true);
			}
		}
		else if (currentFunctionName.equals("Add road")) {

			if(currentTerrainName.equals("Dirt Road")){
				currentTerrain = map.getTerrain(Terrain.DIRT_ROAD);
			}
			else if(currentTerrainName.equals("Paved Road")){
				currentTerrain = map.getTerrain(Terrain.PAVED_ROAD);
			}
			else if(currentTerrainName.equals("Elevated Road")){
				currentTerrain = map.getTerrain(Terrain.ELEVATED_ROAD);
			}
			else if(currentTerrainName.equals("Sunken Road")){
				currentTerrain = map.getTerrain(Terrain.SUNKEN_ROAD);
			}
			else if(currentTerrainName.equals("Runway")){
				currentTerrain = map.getTerrain(Terrain.RUNWAY);
			}
		}
		else if (currentFunctionName.equals("Add objects")) {

			if(currentTerrainName.equals("Foxholes")){
				currentTerrain = map.getTerrain(Terrain.FOX_HOLES);
			}
			else if(currentTerrainName.equals("Trench")){
				currentTerrain = map.getTerrain(Terrain.TRENCH);
			}
			else if(currentTerrainName.equals("Tunnel")){
				currentTerrain = map.getTerrain(Terrain.TUNNEL);
			}
			else if(currentTerrainName.equals("Sewer")){
				currentTerrain = map.getTerrain(Terrain.SEWER);
			}
			else if(currentTerrainName.equals("Stairway")){
				currentTerrain = null;
			}
			else if(currentTerrainName.equals("Smoke")){
				currentTerrain = null;
			}
			else if(currentTerrainName.equals("Vehicle")){
				currentTerrain = null;
			}
			else if(currentTerrainName.equals("Remove Stairway")){
				currentTerrain = null;
			}
			else if(currentTerrainName.equals("Remove Tunnel/Sewer")){
				currentTerrain = null;
			}
			else if(currentTerrainName.equals("Remove Entrenchment")){
				currentTerrain = null;
			}
			else if(currentTerrainName.equals("Remove Smoke")){
				currentTerrain = null;
			}
			else if(currentTerrainName.equals("Remove Vehicle")){
				currentTerrain = null;
			}
		}

		frame.setStatusBarText("  ");
		repaint();
	}

	public String getCurrentTerrain(){

		return currentTerrainName;
	}

	public void setCurrentToTerrain(String newCurrentToTerrain){

		currentToTerrainName = newCurrentToTerrain;

		if (currentFunctionName.equals("Set ground level")) {

			// set the current ground level
			if(currentToTerrainName.equals("Hill Level 0")) {
				currentToTerrain = null;
				currentToGroundLevel = 0;
			}
			else if(currentToTerrainName.equals("Hill Level 1")) {
				currentToTerrain = null;
				currentToGroundLevel = 1;
			}
			else if(currentToTerrainName.equals("Hill Level 2")) {
				currentToTerrain = null;
				currentToGroundLevel = 2;
			}
			else if(currentToTerrainName.equals("Hill Level 3")) {
				currentToTerrain = null;
				currentToGroundLevel = 3;
			}
			else if(currentToTerrainName.equals("Hill Level 4")) {
				currentToTerrain = null;
				currentToGroundLevel = 4;
			}
			else if(currentToTerrainName.equals("Hill Level 5")) {
				currentToTerrain = null;
				currentToGroundLevel = 5;
			}
			else if(currentToTerrainName.equals("Hill Level 6")) {
				currentToTerrain = null;
				currentToGroundLevel = 6;
			}
			else if(currentToTerrainName.equals("Hill Level 7")) {
				currentToTerrain = null;
				currentToGroundLevel = 7;
			}
			else if(currentToTerrainName.equals("Hill Level 8")) {
				currentToTerrain = null;
				currentToGroundLevel = 8;
			}
			else if(currentToTerrainName.equals("Hill Level 9")) {
				currentToTerrain = null;
				currentToGroundLevel = 9;
			}
			else if(currentToTerrainName.equals("Hill Level 10")) {
				currentToTerrain = null;
				currentToGroundLevel = 10;
			}
			else if(currentToTerrainName.equals("Valley -1")) {
				currentToTerrain = null;
				currentToGroundLevel = -1;
			}
			else if(currentToTerrainName.equals("Valley -2")) {
				currentToTerrain = null;
				currentToGroundLevel = -2;
			}
			else if(currentToTerrainName.equals("Gully")) {
				currentToTerrain = map.getTerrain(Terrain.GULLY);
				currentToGroundLevel = -1;
			}
			else if(currentToTerrainName.equals("Dry Stream")) {
				currentToTerrain = map.getTerrain(Terrain.DRY_STREAM);
				currentToGroundLevel = -1;
			}
			else if(currentToTerrainName.equals("Shallow Stream")) {
				currentToTerrain = map.getTerrain(Terrain.SHALLOW_STREAM);
				currentToGroundLevel = -1;
			}
			else if(currentToTerrainName.equals("Deep Stream")) {
				currentToTerrain = map.getTerrain(Terrain.DEEP_STREAM);
				currentToGroundLevel = -1;
			}
		}
		else if (currentFunctionName.equals("Add hexside terrain")) {

			Terrain t = map.getTerrain(currentToTerrainName);

			if (t == null){

				frame.setStatusBarText("Terrain " + currentToTerrainName + " not found. Terrain set to 'Wall'.");
				currentToTerrain = map.getTerrain(Terrain.WALL);
				currentToTerrainName = "Wall";
			}
			else {

				currentToTerrain = t;
			}
		}
		else if (currentFunctionName.equals("Add terrain")) {

			Terrain t = map.getTerrain(currentToTerrainName);

			if (t == null){

				frame.setStatusBarText("Terrain " + currentToTerrainName + " not found. Terrain set to 'Open Ground'.");
				currentToTerrain = map.getTerrain(Terrain.OPEN_GROUND);
				currentToTerrainName = "Open Ground";
			}
			else {

				currentToTerrain = t;
			}
		}
		else if (currentFunctionName.equals("Add road")) {

			if(currentToTerrainName.equals("Dirt Road")){
				currentToTerrain = map.getTerrain(Terrain.DIRT_ROAD);
			}
			else if(currentToTerrainName.equals("Paved Road")){
				currentToTerrain = map.getTerrain(Terrain.PAVED_ROAD);
			}
			else if(currentToTerrainName.equals("Elevated Road")){
				currentToTerrain = map.getTerrain(Terrain.ELEVATED_ROAD);
			}
			else if(currentToTerrainName.equals("Sunken Road")){
				currentToTerrain = map.getTerrain(Terrain.SUNKEN_ROAD);
			}
		}

		frame.setStatusBarText("  ");
		repaint();
	}

	public String getCurrentToTerrain(){

		return currentToTerrainName;
	}

	public void setCurrentBrush(String newCurrentBrush){

		if (newCurrentBrush == null) {
			currentBrush = "";
		}
		else {
			currentBrush = newCurrentBrush;
		}

		if (currentBrush == "") {
			currentBrushSize = 0;
		}
		else if(currentBrush.equals("1  Pixel")) {
			currentBrushSize = 1;
		}
		else if(currentBrush.equals("2  Pixel")) {
			currentBrushSize = 2;
		}
		else if(currentBrush.equals("4  Pixel")) {
			currentBrushSize = 4;
		}
		else if(currentBrush.equals("8  Pixel")) {
			currentBrushSize = 8;
		}
		else if(currentBrush.equals("16 Pixel")) {
			currentBrushSize = 16;
		}
		else if(currentBrush.equals("32 Pixel")) {
			currentBrushSize = 32;
		}
		else if(currentBrush.equals("64 Pixel")) {
			currentBrushSize = 64;
		}
		else if(currentBrush.equals("Hex")) {
			currentBrushSize = -1;
		}
		repaint();
	}

	public String getCurrentBrush(){

		return currentBrush;
	}

	public void updateMap(){

		// is the map open?
		if (!mapOpen) return;

		if (currentFunctionName.equals("LOS")) {

		}
		else if (currentFunctionName.equals("Set ground level")) {

			// set the map grid first...
			Iterator iter = allSelections.iterator();
			while(iter.hasNext()){

				Shape s = ((Selection) iter.next()).getUpdateShape();

				map.setGridGroundLevel(s, currentTerrain, currentGroundLevel);
				mapChanged = true;
				setDirtyArea(s.getBounds());
			}

			// ...then set the hex elevation/depression info
			iter = allSelections.iterator();
			while(iter.hasNext()){

				map.setHexGroundLevel(((Selection) iter.next()).getUpdateShape(), currentTerrain, currentGroundLevel);
			}
		}
		else if (currentFunctionName.equals("Add hexside terrain")) {

	  		HexsideSelection selectedHexside;
	  		int			 hexside;
  			Hex				hex;

			Iterator iter = allSelections.iterator();
			while(iter.hasNext()){

	  			selectedHexside = (HexsideSelection) iter.next();
	  			hex = selectedHexside.getLocation().getHex();

  				// set the edge terrain in the location hex
  				hexside = hex.getLocationHexside(selectedHexside.getLocation());
 				hex.setEdgeTerrain(hexside, currentTerrain);

	  			// set the edge terrain in the adjacent hex
	  			hex =  map.getAdjacentHex(hex, hexside);
				if (hex != null){
	  				hex.setEdgeTerrain(hex.getOppositeHexside(hexside), currentTerrain);
				}

	  			// set the grid map
				// use open ground when removing terrain
				if (currentTerrain == null) {
					map.setGridTerrain(selectedHexside.getUpdateShape(), map.getTerrain(Terrain.OPEN_GROUND));
				}
				else {
					map.setGridTerrain(selectedHexside.getUpdateShape(), currentTerrain);
				}
				setDirtyArea(selectedHexside.getUpdateShape().getBounds());
				mapChanged = true;
			}
		}
		else if (currentFunctionName.equals("Add terrain")) {

			Iterator iter = allSelections.iterator();
			Selection sel = null;

			while(iter.hasNext()){

				sel = (Selection) iter.next();
				Shape s = sel.getUpdateShape();

				map.setGridTerrain(s, currentTerrain);
				map.setHexTerrain(s, currentTerrain);
				mapChanged = true;
				setDirtyArea(s.getBounds());

				// add the prefab buildings
				if (currentTerrainName.equals("Prefab Building")) {

					map.addPrefabBuilding(((PrefabBuildingSelection) sel).getBuilding());
				}
			}
		}
		else if (currentFunctionName.equals("Add bridge")) {
			if (allSelections.size() > 0) {

				// selected bridge
				Iterator iter = allSelections.iterator();
				while(iter.hasNext()){

					// remove?
					if(currentTerrain == null){


						Hex h = ((HexSelection) iter.next()).getHex();
						h.removeBridge();

						// update the map
						setDirtyArea(h.getHexBorder().getBounds());
						mapChanged = true;
					}

					// add the bridge
					else {

						BridgeSelection sel = (BridgeSelection) iter.next();

						// Bridge location is currently the center of the hex
						// need to create the new
						sel.getHex().setBridge(sel.getBridge());

						// update the map
						setDirtyArea(sel.getUpdateShape().getBounds());
						mapChanged = true;
					}
				}
			}
		}
		else if (currentFunctionName.equals("Add road")) {
	  		Iterator 		iter;
	  		HexsideSelection  selectedHexside;
	  		int			hexside;
	  		Hex			hex;
			Terrain		tempTerrain = currentTerrain;

			// set depression/groundlevel for sunken/elevated road
			if(currentTerrain.getType() == Terrain.ELEVATED_ROAD || currentTerrain.getType() == Terrain.SUNKEN_ROAD){

				// convert sunken road to dirt roads for non-depression terrain
				if (currentTerrain.getType() == Terrain.SUNKEN_ROAD){
					tempTerrain = map.getTerrain(Terrain.DIRT_ROAD);
				}

				// set the map grid first...
				iter = allSelections.iterator();
		  		while(iter.hasNext()){

	  				selectedHexside = (HexsideSelection) iter.next();
	  				hex = selectedHexside.getLocation().getHex();

					if (currentTerrain.getType() == Terrain.ELEVATED_ROAD){

						map.setGridGroundLevel(
							selectedHexside.getUpdateShape(),
							null,
							1);
					}
					else {
						map.setGridGroundLevel(
							selectedHexside.getUpdateShape(),
							currentTerrain,
							0);
					}
				}

				// ...then set the hex elevation/depression info
				iter = allSelections.iterator();
				while(iter.hasNext()){

	  				selectedHexside = (HexsideSelection) iter.next();

					if (currentTerrain.getType() == Terrain.ELEVATED_ROAD){

						map.setHexGroundLevel(
							selectedHexside.getUpdateShape(),
							null,
							1);
					}
					else {
						map.setHexGroundLevel(
							selectedHexside.getUpdateShape(),
							currentTerrain,
							0);
					}
				}
			}

			iter = allSelections.iterator();
	  		while(iter.hasNext()){

  				selectedHexside = (HexsideSelection) iter.next();
  				hex = selectedHexside.getLocation().getHex();

				map.setGridTerrain(selectedHexside.getPaintShape(), tempTerrain);
				map.setHexTerrain( selectedHexside.getPaintShape(), tempTerrain);

				setDirtyArea(selectedHexside.getUpdateShape().getBounds());
				mapChanged = true;
	  		}
		}
		else if (currentFunctionName.equals("Add objects")) {
			if (allSelections.size() > 0) {

				Iterator iter = allSelections.iterator();
				Hex h = null;
				while(iter.hasNext()){

					h = ((HexSelection) iter.next()).getHex();

					if(currentTerrain != null &&
						(currentTerrain.getType() == Terrain.TRENCH || currentTerrain.getType() == Terrain.FOX_HOLES)){

						h.addEntrenchment(currentTerrain);
					}
					else if(currentTerrain != null &&
						(currentTerrain.getType() == Terrain.SEWER || currentTerrain.getType() == Terrain.TUNNEL)){

						h.addTunnel(currentTerrain);
					}
					else if(currentTerrainName.equals("Stairway")){

						h.setStairway(true);
					}
					else if(currentTerrainName.equals("Smoke")){

						map.addSmoke(new Smoke(Smoke.SMOKE, h.getCenterLocation()));
					}
					else if(currentTerrainName.equals("Vehicle")){

						scenario.addUnit((CASL.Unit.Unit) new CASL.Unit.Vehicle(h.getCenterLocation()), Scenario.ALLIES);
					}
					else if(currentTerrainName.equals("Remove Stairway")){

						h.setStairway(false);
					}
					else if(currentTerrainName.equals("Remove Tunnel/Sewer")){

						h.removeTunnel();
					}
					else if(currentTerrainName.equals("Remove Entrenchment")){

						h.removeEntrenchment();
					}
					else if(currentTerrainName.equals("Remove Smoke")){

						map.removeSmoke(h.getCenterLocation());
					}
					else if(currentTerrainName.equals("Remove Vehicle")){

						Iterator viter = scenario.getVehicles().iterator();
						Hex vh;
						while (viter.hasNext()){

							vh = (Hex) viter.next();
							if (vh == h){

								viter.remove();
							}
						}
					}

					// adjust "dirty" area of map
					setDirtyArea(h.getExtendedHexBorder().getBounds());
					mapChanged = true;
				}
			}
		}

		// rebuild the map image
		paintMapImage(true);
		clearSelections();
		repaint();
	}

	public void setRoundBrush(boolean isRoundBrush){

		roundBrush = isRoundBrush;
	}

	public void setContours(boolean contours){

		if (map != null) {

			map.setShowContours(contours);
		}
	}

	public void setShadows(boolean shadows){

		if (map != null) {

			map.setShowShadows(shadows);
		}
	}

	public void clearSelections(){

		if (allSelections.size() > 0){
			allSelections.clear();
		}
		// reset the dirty area
		minDirtyX = -1;
		minDirtyY = -1;
		maxDirtyX = -1;
		maxDirtyY = -1;
	}

	public void createNewMap(int width, int height){

		// create the map
		frame.setStatusBarText("Creating the map...");
		frame.paintImmediately();
		map   = new GameMap(width, height);

		// create the map image
		if (map != null) {

			frame.setStatusBarText("Creating the map image...");
			frame.paintImmediately();
			mapImage  = new BufferedImage((int) map.getImageWidth(), (int) map.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
			paintMapImage(false);
			frame.setStatusBarText("  ");
			adjustMapViewSize();
			mapOpen 	= true;
			mapChanged	= false;
		}
		frame.setStatusBarText("  ");
		unit.setLocation(map.getHex(map.getWidth()/2, 1).getCenterLocation());
		targetLocation = unit.getLocation();
	}

	public void saveMap(){

		frame.setStatusBarText("Saving the map...");
		frame.paintImmediately();
		map.writeMap(fileName);
		mapChanged 	= false;
		frame.setStatusBarText("");
		frame.paintImmediately();

	}

	public void saveMapImage(){

		// save the map image a gif file
		try{
			frame.setStatusBarText("Saving the Gif map image...");
			frame.paintImmediately();
			FileOutputStream os = new FileOutputStream(fileName.substring(0, fileName.indexOf('.')) + ".gif");

			GifEncoder encoder = new GifEncoder((Image) mapImage, os);
			encoder.encode();

			os.close();

			frame.setStatusBarText("");
			frame.paintImmediately();
		} catch (Exception e) {

			e.printStackTrace();
			frame.setStatusBarText("Error saving the Gif file");
		}
	}

	public void openMap(){

		frame.setStatusBarText("Loading the map...");
		frame.paintImmediately();
		map = GameMap.readMap(fileName);
		frame.setStatusBarText("");

		// create the map image
		if (map != null) {

			mapImage  = new BufferedImage(map.getImageWidth(), map.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);

			if(!(new File(fileName.substring(0, fileName.indexOf('.')) + ".gif")).exists()) {

				// rebuild image
				frame.setStatusBarText("Rebuilding the map image...");
				frame.paintImmediately();
				paintMapImage(false);
				frame.setStatusBarText("");
			}
			else {

				Image tempImage = Toolkit.getDefaultToolkit().getImage(fileName.substring(0, fileName.indexOf('.')) + ".gif");

				// ensure the image is loaded
				try{
					MediaTracker mediatracker = new MediaTracker(this);
					mediatracker.addImage(tempImage, 0);
					mediatracker.waitForAll();
				} catch (Exception e){

					MapBuilder.writeError("Cannot loaded the map image");
				}

				// draw it into the buffered image
				Graphics2D g = (Graphics2D) mapImage.getGraphics();
				g.drawImage(tempImage, 0, 0, this);

				// free up resources
				g.dispose();
			}

			frame.setStatusBarText("");
			frame.paintImmediately();
			unit.setLocation(map.getHex(map.getWidth()/2, 1).getCenterLocation());
			targetLocation = unit.getLocation();
			adjustMapViewSize();
			mapOpen 	= true;
			mapChanged	= false;
		}
		else {
			closeMap();
			frame.setStatusBarText("Cannot open the map file" + fileName);
		}
	}

	public void closeMap(){

		// reset the map
		mapChanged 	= false;
		mapOpen 	= false;
		map 		= null;
		mapImage 	= null;
		fileName	= "";
		System.gc();		// recover space
		frame.setStatusBarText("  ");
		adjustMapViewSize();
		repaint();

	}

	public void undoSelections(){

		clearSelections();
		repaint();
		mapChanged = false;
	}

	public void setCustomBuildingParameters(String terr, int width, int height){

		// set the current terrain
		setCurrentTerrain(terr);

		// set the custom building parameters
		customBuildingWidth		= width;
		customBuildingHeight	= height;
	}

	public void setCustomBuildingOn(boolean newCustomBuildingOn){

		customBuildingOn = newCustomBuildingOn;
		if(prefabBuildingOn && customBuildingOn){

			prefabBuildingOn = false;
		}
	}

	public void setPrefabBuildingParameters(String buildingName){

		// find the correct building
		int current = 0;
		while (prefabBuildingList[current] != null) {

			if (prefabBuildingList[current].getImageName().equals(buildingName)) {

				currentPrefabBuilding = current;
				break;
			}
			else {
				current++;
			}
		}

		// set the current terrain
		currentTerrain = map.getTerrain(prefabBuildingList[currentPrefabBuilding].getTerrainType());
	}

	public void setPrefabBuildingOn(boolean newPrefabBuildingOn){

		prefabBuildingOn = newPrefabBuildingOn;
		if(prefabBuildingOn && customBuildingOn){

			customBuildingOn = false;
		}
	}

	public void setCustomBridgeParameters(String terr, int roadElevation){

		// set the current terrain
		setCurrentTerrain(terr);

		// set the custom bridge parameters
		customBridgeRoadElevation	= roadElevation;
	}

	public boolean isCustomBuildingOn(){

		return customBuildingOn;
	}

	public int getCustomBuildingWidth()		{ return customBuildingWidth;}
	public int getCustomBuildingHeight()	{ return customBuildingHeight;}

	public int getCustomBridgeRoadElevation()	{ return customBridgeRoadElevation;}

	public PrefabBuilding[] getPrefabBuildingList()		{ return prefabBuildingList;}
	public int getCurrentPrefabBuilding()	{ return currentPrefabBuilding;}

	public boolean isFocusTraversable() {return true;}

	public int 	getRotation() { return rotation;}
	public void	setRotation(int rotation) {

		this.rotation = rotation;

		// set the prefab building rotation
		if (prefabBuildingList[currentPrefabBuilding] != null){
			prefabBuildingList[currentPrefabBuilding].setRotation(rotation);
		}
	}

	/******************************
	Keyboard methods
	******************************/
	public void keyTyped(KeyEvent e) {

		int code = e.getKeyCode();
		String modifiers = KeyEvent.getKeyModifiersText(e.getModifiers());

	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {

		int code = e.getKeyCode();
		String modifiers = KeyEvent.getKeyModifiersText(e.getModifiers());

		// is the map open?
		if (!mapOpen) return;

		// Not doing LOS?
		if (!currentFunctionName.equals("LOS")) {

			// undo
			if(code ==	KeyEvent.VK_Z && modifiers.equals("Ctrl")){

				if (allSelections.size() > 0) {

					allSelections.remove(allSelections.getLast());
					repaint();
				}
			}
			// clear selections
			else if(code ==	KeyEvent.VK_ESCAPE){

				clearSelections();
			}

			// update
			else if(code ==	KeyEvent.VK_U){

				updateMap();
			}
			return;
		}

		// north
		if(code ==	KeyEvent.VK_NUMPAD8){

			if (modifiers.equals("")){

				moveResult = map.getMovementResult(unit.getLocation(), 0, 0, moveResult);
			}
			else if (modifiers.equals("Alt")){

				moveResult = map.getMovementResult(unit.getLocation(), 0, 2, moveResult);
			}
			else if (modifiers.equals("Ctrl")){

				moveResult = map.getMovementResult(unit.getLocation(), 0, 1, moveResult);
			}

			// bad modifier string, ignore
			else {
				return;
			}

		}

		// south
		else if (code == KeyEvent.VK_NUMPAD5){

			if (modifiers.equals("")){

				moveResult = map.getMovementResult(unit.getLocation(), 3, 0, moveResult);
			}
			else if (modifiers.equals("Alt")){

				moveResult = map.getMovementResult(unit.getLocation(), 3, 2, moveResult);
			}
			else if (modifiers.equals("Ctrl")){

				moveResult = map.getMovementResult(unit.getLocation(), 3, 1, moveResult);
			}

			// bad modifier string, ignore
			else {
				return;
			}
		}

	   	// northwest
		else if (code == KeyEvent.VK_NUMPAD7){

			if (modifiers.equals("")){

				moveResult = map.getMovementResult(unit.getLocation(), 5, 0, moveResult);
			}
			else if (modifiers.equals("Alt")){

				moveResult = map.getMovementResult(unit.getLocation(), 5, 2, moveResult);
			}
			else if (modifiers.equals("Ctrl")){

				moveResult = map.getMovementResult(unit.getLocation(), 5, 1, moveResult);
			}

			// bad modifier string, ignore
			else {
				return;
			}
		}

		// northeast
		else if (code == KeyEvent.VK_NUMPAD9){

			if (modifiers.equals("")){

				moveResult = map.getMovementResult(unit.getLocation(), 1, 0, moveResult);
			}
			else if (modifiers.equals("Alt")){

				moveResult = map.getMovementResult(unit.getLocation(), 1, 2, moveResult);
			}
			else if (modifiers.equals("Ctrl")){

				moveResult = map.getMovementResult(unit.getLocation(), 1, 1, moveResult);
			}

			// bad modifier string, ignore
			else {
				return;
			}
		}

		// southwest
		else if (code == KeyEvent.VK_NUMPAD4){

			if (modifiers.equals("")){

				moveResult = map.getMovementResult(unit.getLocation(), 4, 0, moveResult);
			}
			else if (modifiers.equals("Alt")){

				moveResult = map.getMovementResult(unit.getLocation(), 4, 2, moveResult);
			}
			else if (modifiers.equals("Ctrl")){

				moveResult = map.getMovementResult(unit.getLocation(), 4, 1, moveResult);
			}

			// bad modifier string, ignore
			else {
				return;
			}
		}

		// southeast
		else if (code == KeyEvent.VK_NUMPAD6){

			if (modifiers.equals("")){

				moveResult = map.getMovementResult(unit.getLocation(), 2, 0, moveResult);
			}
			else if (modifiers.equals("Alt")){

				moveResult = map.getMovementResult(unit.getLocation(), 2, 2, moveResult);
			}
			else if (modifiers.equals("Ctrl")){

				moveResult = map.getMovementResult(unit.getLocation(), 2, 1, moveResult);
			}

			// bad modifier string, ignore
			else {
				return;
			}

		}

		// up/down
		else if (code == KeyEvent.VK_NUMPAD2){

			// up or down?
			if (modifiers.equals("Alt")) {

				moveResult = map.getMovementResult(unit.getLocation(), 7, 0, moveResult);
			}
			else {
				moveResult = map.getMovementResult(unit.getLocation(), 6, 0, moveResult);
			}
		}

		else if (code == KeyEvent.VK_D){

			return;
		}
		// ignore other keys
		else {
			return;
		}

		// error?
		if (!moveResult.isLegal()){

			frame.setStatusBarText("Illegal move: " + moveResult.getErrorMessage());
			moveResult = moveResult.getPrevMovementResult();
			repaint();
		}

		// set new location, if necessary
		else if (moveResult.getEndLocation() != null){

			// if unit has moved into the center of a new hex containing a shellhole,
			// ask if they want to move directly into that shellhole
			if (moveResult.crossedHexside() &&
				moveResult.getEndLocation().isCenterLocation() &&
				moveResult.getEndLocation().getDownLocation() != null &&
				moveResult.getEndLocation().getDownLocation().getTerrain().getType() == Terrain.SHELL_HOLES){

				int response = frame.AskYesNo("Move directly INTO the shellholes?");

				if (response == JOptionPane.YES_OPTION){

					moveResult.setEndLocation(moveResult.getEndLocation().getDownLocation());
				}
				else if (response == JOptionPane.NO_OPTION){
				}
				else if (response == JOptionPane.CANCEL_OPTION){

					return;
				}
			}
			// if unit has moved into the center of a new hex containing rice paddies,
			// ask if they want to move directly into the rice paddy
			else if (moveResult.crossedHexside() &&
				moveResult.getEndLocation().isCenterLocation() &&
				moveResult.getEndLocation().getTerrain().isRicePaddy()){

				int response = frame.AskYesNo("Move directly INTO the rice paddy?");

				if (response == JOptionPane.YES_OPTION){
				}
				else if (response == JOptionPane.NO_OPTION){

					moveResult.setEndLocation(moveResult.getEndLocation().getUpLocation());
				}
				else if (response == JOptionPane.CANCEL_OPTION){

					return;
				}
			}

			// apply movement logic
			moveLogic.moveUnit(unit, moveResult);

			// error?
			if (!moveResult.isLegal()){

				frame.setStatusBarText("Illegal move: " + moveResult.getErrorMessage());
				if (moveResult.getEnterLocation() != null) {

					unit.setLocation(moveResult.getEndLocation());
	    			map.LOS(unit.getLocation(), false, targetLocation, useAuxTargetLOSPoint, result, scenario);
				}
				else {
					moveResult = moveResult.getPrevMovementResult();
				}
				repaint();
			}
			else {

				// move the unit
				unit.setLocation(moveResult.getEndLocation());
				map.LOS(unit.getLocation(), false, targetLocation, useAuxTargetLOSPoint, result, scenario);

				// show some interesting information
				String s = "Location: " + " " + unit.getLocation().getName() +
					" - Height: " + (unit.getLocation().getHex().getBaseHeight() + unit.getLocation().getBaseHeight()) +
					" - Terrain:  "	+ unit.getLocation().getTerrain().getName();

				// depression terrain?
				if(unit.getLocation().getDepressionTerrain() != null){
					s += "/" + unit.getLocation().getDepressionTerrain().getName();
				}

				// interim locations
				if (moveResult.getExitLocation() != null || moveResult.getEnterLocation() != null){

					s += " (via:";

					if (moveResult.getExitLocation() != null){
						s += " " + moveResult.getExitLocation().getName();
					}
					if (moveResult.getEnterLocation() != null){
						s += " " + moveResult.getEnterLocation().getName();
					}
					s += ")";
				}

				// MP/MF
				s +=	" - MF:  "	+ moveResult.getMF();

				// print the movement message
				frame.addMessage("\n" + moveResult.getMovementMessage());

				frame.setStatusBarText(s);
				repaint();
			}
		}
	}

	public void paintMapImage(boolean askToPaint){

		// map not dirty? ask if we should paint the whole thing
		if(minDirtyX == -1) {

			if (askToPaint){

				int response = frame.AskYesNo("Do you want to recreate the entire map image?");

				if (response == JOptionPane.YES_OPTION){

					map.paintMapArea	(0, 0, map.getImageWidth(), map.getImageHeight(), mapImage, terrainImages, prefabBuildingImageList, singleHexWoodenBridgeImage,	singleHexStoneBridgeImage);
					map.paintMapShadows (0, 0, map.getImageWidth(), map.getImageHeight(), mapImage);
					map.paintMapContours(0, 0, map.getImageWidth(), map.getImageHeight(), mapImage);
				}
				else if (response == JOptionPane.NO_OPTION){
					return;
				}
				else if (response == JOptionPane.CANCEL_OPTION){
					return;
				}
			}
			else {
				map.paintMapArea	(0, 0, map.getImageWidth(), map.getImageHeight(), mapImage, terrainImages, prefabBuildingImageList, singleHexWoodenBridgeImage,	singleHexStoneBridgeImage);
				map.paintMapShadows (0, 0, map.getImageWidth(), map.getImageHeight(), mapImage);
				map.paintMapContours(0, 0, map.getImageWidth(), map.getImageHeight(), mapImage);
			}
		}
		else {
			map.paintMapArea(
				minDirtyX,
				minDirtyY,
				maxDirtyX - minDirtyX + 1,
				maxDirtyY - minDirtyY + 1,
				mapImage,
				terrainImages,
				prefabBuildingImageList,
				singleHexWoodenBridgeImage,
				singleHexStoneBridgeImage
			);
			map.paintMapShadows(
				minDirtyX,
				minDirtyY,
				maxDirtyX - minDirtyX + 1,
				maxDirtyY - minDirtyY + 1,
				mapImage
			);
			map.paintMapContours(
				minDirtyX,
				minDirtyY,
				maxDirtyX - minDirtyX + 1,
				maxDirtyY - minDirtyY + 1,
				mapImage
			);
		}
		map.paintMapHexes(mapImage);
	}

	// adjust "dirty" area of map
	private void setDirtyArea(Rectangle rect){

		//first time?
		if(minDirtyX == -1){

			minDirtyX = (int) rect.getX();
			minDirtyY = (int) rect.getY();
			maxDirtyX = (int) (rect.getX() + rect.getWidth());
			maxDirtyY = (int) (rect.getY() + rect.getHeight());
		}
		else {

			minDirtyX = (int) Math.min(rect.getX(), minDirtyX);
			minDirtyY = (int) Math.min(rect.getY(), minDirtyY);
			maxDirtyX = (int) Math.max(rect.getX() + rect.getWidth(), maxDirtyX);
			maxDirtyY = (int) Math.max(rect.getY() + rect.getHeight(), maxDirtyY);
		}
	}

	public void mousePopupMenuEvent(String event, ActionEvent e, int x, int y){

		if (event.equals("Flip bypass flag")) {

			Location l = map.gridToHex(x, y).nearestLocation(x, y);

			//	ignore center locations
			if (!l.isCenterLocation()){

				l.setBypassAllowed(!l.isBypassAllowed());
			}
		}
	}

      public void setZoom(String zoom){

		zoomSetting = zoom;
      }

      public void setPointer(boolean newPointer){

		pointer = newPointer;
      }

	public void changeAllTerrain(){

		boolean changed = false;

		frame.setStatusBarText("Changing the map...");
		frame.paintImmediately();

		// just the current selections?
		if (allSelections.size() > 0) {

			// update the map
			if (currentTerrain != null && currentToTerrain != null){

				Iterator i = allSelections.iterator();
				while (i.hasNext()){

					Selection s = (Selection) i.next();
					boolean selectionChanged = map.changeAllTerrain(currentTerrain, currentToTerrain, s.getUpdateShape());

					if (selectionChanged) {

						this.setDirtyArea(s.getUpdateShape().getBounds());
						changed = true;
		    		    mapChanged = true;
					}

				}
			}
			else if (currentTerrain == null && currentToTerrain == null){

				Iterator i = allSelections.iterator();
				while (i.hasNext()){

					Selection s = (Selection) i.next();
					boolean selectionChanged = map.changeAllGroundLevel(currentGroundLevel, currentToGroundLevel, s.getUpdateShape());

					if (selectionChanged) {

						this.setDirtyArea(s.getUpdateShape().getBounds());
						changed = true;
		    		    mapChanged = true;
					}

				}
			}
			else {
				frame.setStatusBarText("Illegal terrain mapping");
				return;
			}

		    // clear the selections, set changed flag
		    allSelections.clear();
		}

		// the whole map
		else {

			// update the map
			if (currentTerrain != null && currentToTerrain != null){
				changed = map.changeAllTerrain(currentTerrain, currentToTerrain);
			}
			else if (currentTerrain == null && currentToTerrain == null){
				changed = map.changeAllGroundLevel(currentGroundLevel, currentToGroundLevel);
			}
			else {
				frame.setStatusBarText("Illegal terrain mapping");
				return;
			}

			if (!changed){

				frame.setStatusBarText("Nothing changed");
				return;

			}

			// mark the whole map as changed and recreate
			minDirtyX = -1;

		}

		frame.setStatusBarText("Recreating the map image...");
		frame.paintImmediately();
		paintMapImage(false);
		frame.setStatusBarText("");
		repaint();
	}

	public void exportMap(String filename){

		frame.setStatusBarText("Exporting the map...");
		frame.paintImmediately();
		map.exportMap(filename);
		frame.setStatusBarText("");
		frame.paintImmediately();
	}

	public void importMap(String filename){

		frame.setStatusBarText("Importing the map...");
		frame.paintImmediately();
		map = GameMap.importMap(filename);
		frame.setStatusBarText("");

		// reset shadows/contours settings
		map.setShowContours(frame.contoursSelected());
		map.setShowShadows(frame.shadowsSelected());

		// create the map image
		if (map != null) {

			mapImage  = new BufferedImage(map.getImageWidth(), map.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);

			// rebuild image
			frame.setStatusBarText("Rebuilding the map image...");
			frame.paintImmediately();
			paintMapImage(false);
			frame.setStatusBarText("");
			frame.paintImmediately();

			unit.setLocation(map.getHex(map.getWidth()/2, 1).getCenterLocation());
			adjustMapViewSize();
			mapOpen 	= true;
			mapChanged	= true;
		}
		else {
			closeMap();
			frame.setStatusBarText("Cannot import the map file " + fileName);
		}
	}

	public void convertVASLImage(int board){

		if (map == null) return;

		// convert the image
		VASLMapConverter converter = new VASLMapConverter(map, frame);
		converter.setImage(mapImage);
		converter.convertMap(board);

		// rebuild image
		frame.setStatusBarText("Rebuilding the map image...");
		frame.paintImmediately();
		paintMapImage(false);
		frame.setStatusBarText("");
		frame.paintImmediately();

		mapChanged	= true;
		frame.setStatusBarText("");
	}

	public Image getImage(String imageName){

		try {

		    ZipEntry    e  = archive.getEntry(imageName);
			InputStream ip = archive.getInputStream(e);

			if (ip == null) {

				return null;
		    }
		    else {

				byte bytes[] = new byte[ip.available()];
				ip.read(bytes);
				Image temp = Toolkit.getDefaultToolkit().createImage(bytes);

				MediaTracker m = new MediaTracker(this);
				m.addImage(temp, 0);
				m.waitForID(0);

				return temp;
		    }

		} catch (IOException e){

		    return null;

		} catch (InterruptedException e){

		    return null;

		} catch (Exception e){

		    return null;
		}
	}
	public InputStream getTextFile(String imageName){

		try {

		    ZipEntry    e  = archive.getEntry(imageName);
			return      archive.getInputStream(e);


		} catch (Exception e){

		    return null;
		}
	}

	public void flipMap(){

		frame.setStatusBarText("Flipping the map...");
		frame.paintImmediately();
		map.flip();
		frame.setStatusBarText("Rebuilding the map image...");
		frame.paintImmediately();
		paintMapImage(false);
		mapChanged	= true;
		frame.setStatusBarText("");
	}

	public void runLosTest(){

		// zap a location
//		Hex h = map.getHex("J2");
//		h.setBaseHeight(2);
//		frame.setStatusBarText("J2 zapped");

		int width    = map.getWidth();
		int height   = map.getHeight();
		int count    = 0;
		int blocked  = 0;

		LOSResult result = new LOSResult();

		// create a new location in the middle of the board
		Hex      hex = map.getHex((int) map.getWidth()/2 , (int) map.getHeight()/2);
		Location l = hex.getCenterLocation();

		frame.setStatusBarText("Starting the LOS test...");
		frame.paintImmediately();

		// set the start time and save the base height
		int baseHeight = l.getBaseHeight();
		long startTime = System.currentTimeMillis();

		// check LOS at level zero
		for (int col = 0; col < width; col++) {
			for(int row = 0; row < height + (col%2); row++) {

				result.setClear();
				map.LOS(l, false, map.getHex(col, row).getCenterLocation(), false, result, scenario);

				// increment counters
				count++;
				if (result.isBlocked()) {

				    blocked++;
				}
			}
		}

		// check LOS at level two
		l.setBaseHeight(2);
		for (int col = 0; col < width; col++) {
			for(int row = 0; row < height + (col%2); row++) {

				result.setClear();
				map.LOS(l, false, map.getHex(col, row).getCenterLocation(), false, result, scenario);

				// increment counters
				count++;
				if (result.isBlocked()) {

				    blocked++;
				}
			}
		}

		// check LOS at level four
		l.setBaseHeight(4);
		for (int col = 0; col < width; col++) {
			for(int row = 0; row < height + (col%2); row++) {

				result.setClear();
				map.LOS(l, false, map.getHex(col, row).getCenterLocation(), false, result, scenario);

				// increment counters
				count++;
				if (result.isBlocked()) {

				    blocked++;
				}
			}
		}

		frame.setStatusBarText(
		      "LOS test complete. Total checks: " + count +
		    "  Blocked: " + (int) ((float)blocked/(float)count*100) + "%" +
		    "  Time elapsed: " +  (((double) System.currentTimeMillis() - (double)startTime)/1000));
		frame.paintImmediately();

		// restore base height
		l.setBaseHeight(baseHeight);
	}

	public void insertGEOMap(GameMap insertMap, String upperLeftHex){

		if (map.insertGEOMap(insertMap, map.getHex(upperLeftHex.toUpperCase()))){

            frame.setStatusBarText("Rebuilding the map image...");
            frame.paintImmediately();

			paintMapImage(false);
			mapChanged	= true;
			frame.setStatusBarText("");
		}
	}
	
}

