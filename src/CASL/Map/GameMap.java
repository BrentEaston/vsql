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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import CASL.MapBuilder.VASLMultilevelBuildings;
import CASL.Scenario.Scenario;
import CASL.Unit.Vehicle;

/**
 * The <code>GameMap</code> class is the map API. The map uses two data
 * structures to represent the logical map: a terrain grid and a hex grid.
 * <p>
 * The terrain grid contains a physical representation of the image terrain
 * where one point in the grid corresponds to one pixel in the map image. The
 * upper-left pixel is (0,0).
 * <p>
 * The hex grid contains the information that is specific to each hex and uses
 * the following coordinate system: the upper-left most hex (A1) is (0,0), A2
 * would be (0,1), B0 would be (1,0), and so on. Note that the number of hexes
 * in each column will depend upon whether the column is odd or even.
 * 
 * @version 1.00 10/01/00
 * @author David Sullivan
 */
public class GameMap extends Map implements Serializable {

  // serial ID: version 1.0.0
  static final long serialVersionUID = 000100L;

  /**
   * Constructs a new <code>GameMap</code> object. A standard geomorphic
   * mapboard is 10 x 33 hexes.
   * 
   * @param w
   *          the width of the map in hexes
   * @param h
   *          the height of the map in hexes
   */
  // constructor
  public GameMap(int w, int h) {

    width = w;
    height = h;
    gridWidth = (int) ((width - 1) * Hex.WIDTH);
    gridHeight = (int) (height * Hex.HEIGHT);

    // get the terrain lists
    terrainList = Terrain.getTerrainList();

    // create the location grid
    terrainGrid = new char[gridWidth][gridHeight];
    elevationGrid = new byte[gridWidth][gridHeight];

    // create the hex grid
    hexGrid = new Hex[width][];
    for (int col = 0; col < width; col++) {

      hexGrid[col] = new Hex[height + (col % 2)]; // add 1 if odd
      for (int row = 0; row < height + (col % 2); row++) {
        hexGrid[col][row] = new Hex(col, row, this, 0, terrainList[Terrain.OPEN_GROUND]);
      }
    }

    // create the hex locations
    for (int col = 0; col < width; col++) {
      for (int row = 0; row < height + (col % 2); row++) {
        hexGrid[col][row].resetHexsideLocationNames();
      }
    }
  }

  /**
   * Returns the <code>Terrain</code> type for the pixel at x, y of the map
   * image.
   * 
   * @param x
   *          the x coordinate of the map image pixel
   * @param y
   *          the y coordinate of the map image pixel
   * @return <code>Terrain</code> the terrain type at (col, row)
   */
  public Terrain getGridTerrain(int x, int y) {

    return terrainList[(int) terrainGrid[x][y]];
  }

  /**
   * Returns the ground level for the pixel at x, y of the map image.
   * 
   * @param x
   *          the x coordinate of the map image pixel
   * @param y
   *          the y coordinate of the map image pixel
   * @return the ground level at (x, y)
   */
  public int getGridGroundLevel(int x, int y) {

    // ground level
    return (int) elevationGrid[x][y];
  }

  public void setGridGroundLevel(int x, int y, byte level) {
    elevationGrid[x][y] = level;
  }
  
  /**
   * Reads the map from disk. File must be in the CASL/Maps directory.
   * 
   * @param filename
   *          name of the file (only)
   * @return <code>GameMap</code> GameMap object. Null if an error occured.
   */
  public static GameMap readMap(String filename) {

    return Map.readMap(filename);
  }

  /**
   * Writes the map to disk. File is written to the CASL/Maps directory.
   * 
   * @param filename
   *          name of the file (only)
   */
  public void writeMap(String filename) {

    super.writeMap(filename);
  }

  /**
   * Turns map shadows option on or off for paint map functions.
   * 
   * @param shadows
   *          <code>true</code> to add shadows
   */
  public void setShowShadows(boolean shadows) {

    showShadows = shadows;
  }

  /**
   * Turns map contour lines option on or off for paint map functions.
   * 
   * @param contours
   *          <code>true</code> to add contour lines
   */
  public void setShowContours(boolean contours) {

    showContours = contours;
  }

  /**
   * Turns hex grid option on or off for paint map functions.
   * 
   * @param hexes
   *          <code>true</code> to show hex grid
   */
  public void setShowHexes(boolean hexes) {

    showHexes = hexes;
  }

  /**
   * Returns the hex height. Hex height is the number of pixels from the top of
   * the hex to the bottom of the hex.
   * 
   * @return the hex height.
   */
  public static double getHexHeight() {
    return Hex.WIDTH;
  }

  /**
   * Returns the hex width. The hex width is the distance beween the hex column
   * centers.
   * 
   * @return the hex width
   */
  public static double getHexWidth() {
    return Hex.HEIGHT;
  }

  /**
   * Returns the width of the map in hexes.
   * 
   * @return the map width in hexes
   */
  public int getWidth() {
    return width;
  }

  /**
   * Returns the height of the map in hexes for the even hex columns.
   * 
   * @return the map height in hexes
   */
  public int getHeight() {
    return height;
  }

  /**
   * Returns the width of the map image in pixels.
   * 
   * @return the map width in pixels
   */
  public int getImageWidth() {
    return gridWidth;
  }

  /**
   * Returns the height of the map image in pixels.
   * 
   * @return the map height in pixels
   */
  public int getImageHeight() {
    return gridHeight;
  }

  /**
   * Returns the width of the map image in pixels.
   * 
   * @return the map width in pixels
   */

  /**
   * Return the <code>Terrain</code> type for the given terrain code.
   * 
   * @param t
   *          one of the terrain codes defined in the <code>Terrain</code>
   *          class
   * @return <code>Terrain</code> Terrain type.
   */
  public Terrain getTerrain(int t) {

    return terrainList[t];
  }

  public Terrain getTerrain(String name) {

    return Terrain.getTerrain(name);
  }

  /**
   * Repaints an area of the map image. If a bridge or prefab building touches
   * the paint area, the entire bridge/building is repainted as well. This
   * should be the first paint routine called when recreating an area of the map
   * image.
   * 
   * @param x
   *          left-most pixel column
   * @param y
   *          right-most pixel column
   * @param width
   *          width of the paint area
   * @param height
   *          height of the paint area
   * @param mapImage
   *          the map image
   * @param imageList
   *          list of the terrain images to use
   * @param prefabBuildingImageList
   *          list of the prefab building images to use
   */
  public void paintMapArea(int x, int y, int width, int height, BufferedImage mapImage, BufferedImage[] imageList,
      Image[] prefabBuildingImageList, Image singleHexWoodenBridgeImage, Image singleHexStoneBridgeImage) {

    Hex currentHex = null;
    Terrain depressionTerrain = null;
    int terrType = 0;
    Rectangle paintArea = new Rectangle(x, y, width, height);

    try {

      // step through each pixel
      for (int i = Math.max(x, 0); i < Math.min(x + width + 1, gridWidth); i++) {
        for (int j = Math.max(y, 0); j < Math.min(y + height + 1, gridHeight); j++) {

          // should we use the depression terrain? (ignore switch for sunken
          // roads,
          // non open ground terrain)
          currentHex = gridToHex(i, j);
          depressionTerrain = currentHex.getCenterLocation().getDepressionTerrain();

          if (depressionTerrain != null && terrainList[terrainGrid[i][j]].isOpenTerrain()
              && depressionTerrain.getType() != Terrain.SUNKEN_ROAD
              && elevationGrid[i][j] == currentHex.getBaseHeight()) {

            terrType = depressionTerrain.getType();
          }
          else {

            terrType = terrainGrid[i][j];
          }

          // get color for non-ground level open ground
          Color c = null;
          switch ((int) elevationGrid[i][j]) {

          case -2:
            c = Terrain.VALLEY2_COLOR;
            break;
          case -1:
            c = Terrain.VALLEY1_COLOR;
            break;
          case 1:
            c = Terrain.LEVEL1_COLOR;
            break;
          case 2:
            c = Terrain.LEVEL2_COLOR;
            break;
          case 3:
            c = Terrain.LEVEL3_COLOR;
            break;
          case 4:
            c = Terrain.LEVEL4_COLOR;
            break;
          case 5:
            c = Terrain.LEVEL5_COLOR;
            break;
          case 6:
            c = Terrain.LEVEL6_COLOR;
            break;
          case 7:
            c = Terrain.LEVEL7_COLOR;
            break;
          case 8:
            c = Terrain.LEVEL8_COLOR;
            break;
          case 9:
            c = Terrain.LEVEL9_COLOR;
            break;
          case 10:
            c = Terrain.LEVEL10_COLOR;
            break;
          }

          // create the two-tone colors for depression terrain
          boolean overridePixelColor = false;
          if (terrainList[terrType].isWaterTerrain() || terrType == Terrain.GULLY) {

            // get the distance to the nearest non-water terrain
            double dist = 10.0;
            double currDist = 0.0;
            Point p = new Point(0, 0);
            for (int a = -6; a <= 6; a++) {
              for (int b = -6; b <= 6; b++) {

                if (onMap(i + a, j + b)
                    && (((terrType == Terrain.GULLY || terrType == Terrain.SHALLOW_STREAM || terrType == Terrain.DEEP_STREAM) && elevationGrid[i][j] != elevationGrid[i
                        + a][j + b]) || (!(terrType == Terrain.GULLY || terrType == Terrain.SHALLOW_STREAM || terrType == Terrain.DEEP_STREAM) && !terrainList[terrainGrid[i
                        + a][j + b]].isWaterTerrain()))) {

                  currDist = p.distance((double) a, (double) b);
                  if (currDist < dist) {

                    dist = currDist;
                  }
                }
              }
            }

            // set the outer-color pixel
            if (terrType == Terrain.GULLY) {

              // brown middle for Gullies
              if (5.0 < dist) {

                mapImage.setRGB(i, j, Terrain.GULLY_INTERIOR_COLOR.getRGB());
                overridePixelColor = true;
              }
            }
            else if (terrainList[terrType].isStreamTerrain()) {

              if (0.0 < dist && dist <= 5.0) {

                mapImage.setRGB(i, j, Terrain.VALLEY1_COLOR.getRGB());
                overridePixelColor = true;
              }
              else if (5.0 < dist && dist < 10.0) {

                mapImage.setRGB(i, j, Terrain.WATER_EDGE_COLOR.getRGB());
                overridePixelColor = true;
              }
            }
            else {

              if (0.0 < dist && dist <= 5.0) {

                mapImage.setRGB(i, j, Terrain.WATER_EDGE_COLOR.getRGB());
                overridePixelColor = true;
              }

            }
          }

          // image exist for this terrain?
          if (!overridePixelColor && imageList[terrType] == null) {

            // open ground color on an elevation?
            if (terrainList[terrType].getMapColor().equals(Terrain.OPEN_GROUND_COLOR) && elevationGrid[i][j] != 0) {

              mapImage.setRGB(i, j, c.getRGB());
            }
            else {
              mapImage.setRGB(i, j, terrainList[terrType].getMapColor().getRGB());
            }
          }
          else if (!overridePixelColor) {

            // open ground color on an elevation?
            if (imageList[terrType].getRGB(i % imageList[terrType].getWidth(), j % imageList[terrType].getHeight()) == Terrain.OPEN_GROUND_COLOR
                .getRGB()
                && elevationGrid[i][j] != 0) {

              mapImage.setRGB(i, j, c.getRGB());
            }
            else {
              mapImage.setRGB(i, j, imageList[terrType].getRGB(i % imageList[terrType].getWidth(), j
                  % imageList[terrType].getHeight()));
            }
          }
        }
      }

    }
    catch (Exception e) {

      System.out.println("Exception...");
    }
    // paint the bridges...
    // create a temp image for translated bridge image
    Bridge bridge = null;
    Image bridgeImage = null;
    int centerX = 0;
    int centerY = 0;

    for (int col = 0; col < hexGrid.length; col++) {
      for (int row = 0; row < hexGrid[col].length; row++) {

        currentHex = hexGrid[col][row];

        // has a bridge?
        if (currentHex.hasBridge() && currentHex.getExtendedHexBorder().getBounds().intersects(paintArea)) {

          // set the bridge, etc.
          bridge = currentHex.getBridge();
          centerX = (int) currentHex.getCenterLocation().getLOSPoint().getX();
          centerY = (int) currentHex.getCenterLocation().getLOSPoint().getY();

          // set the image
          if (bridge.getTerrain().getType() == Terrain.SINGLE_HEX_STONE_BRIDGE) {

            bridgeImage = singleHexStoneBridgeImage;
          }
          else if (bridge.getTerrain().getType() == Terrain.SINGLE_HEX_WOODEN_BRIDGE) {

            bridgeImage = singleHexWoodenBridgeImage;
          }
          else {

            bridgeImage = imageList[bridge.getTerrain().getType()];
          }

          // has image?
          if (bridgeImage == null) {

            System.err.println("No image found for bridge " + bridge.getTerrain().getName() + " in hex "
                + currentHex.getName());

          }
          else {

            Graphics2D g = (Graphics2D) mapImage.getGraphics();

            // need to translate?
            if (bridge.getRotation() != 0) {
              g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(bridge.getRotation()), (int) bridge
                  .getCenter().getX(), (int) bridge.getCenter().getY()));
            }

            g.drawImage(bridgeImage, (int) bridge.getCenter().getX() - bridgeImage.getWidth(null) / 2, (int) bridge
                .getCenter().getY()
                - bridgeImage.getHeight(null) / 2, null);

            // free resources
            g.dispose();
          }
        }
      }
    }

    // paint the prefab buildings
    if (prefabBuildingList.size() > 0) {

      Graphics2D g = (Graphics2D) mapImage.getGraphics();

      Iterator iter = prefabBuildingList.iterator();
      while (iter.hasNext()) {

        PrefabBuilding building = (PrefabBuilding) iter.next();
        Rectangle buildingArea = building.getShape().getBounds();
        Rectangle buildingPolygonArea = building.getShapePolygon().getBounds();

        // in paint area?
        if (buildingArea.intersects(paintArea) && building.getFileIndex() != PrefabBuilding.INVALID_FILE_INDEX) {

          g.setTransform(building.getAffineTransform());
          g.drawImage(prefabBuildingImageList[building.getFileIndex()], (int) 0, (int) 0, null);
        }
      }

      // free resources
      g.dispose();
    }
  }

  /**
   * Paints the contour lines into an area of the map image. This should be
   * called after paintMapArea when recreating the map image.
   * 
   * @param x
   *          left-most pixel column
   * @param y
   *          right-most pixel column
   * @param width
   *          width of the paint area
   * @param height
   *          height of the paint area
   * @param img
   *          the map image
   */
  public void paintMapContours(int x, int y, int width, int height, BufferedImage img) {

    // option turned on?
    if (!showContours) return;

    // create map in image
    for (int col = Math.max(x, 0); col < Math.min(x + width, gridWidth); col++) {
      for (int row = Math.max(y, 0); row < Math.min(y + height, gridHeight); row++) {

        // grid adjacent to lower ground level?
        if (((col > 0 && elevationGrid[col][row] > elevationGrid[col - 1][row])
            || (row > 0 && elevationGrid[col][row] > elevationGrid[col][row - 1])
            || (col < gridWidth - 1 && elevationGrid[col][row] > elevationGrid[col + 1][row]) || (row < gridHeight - 1 && elevationGrid[col][row] > elevationGrid[col][row + 1]))
        // don't show in depressions
        //					&& elevationGrid[col][row] > 0
        ) {
          img.setRGB(col, row, 0xFFFF0F0F);
        }
      }
    }
  }

  /**
   * Paints the shadows into an area of the map image. This should be called
   * after paintMapArea when recreating the map image.
   * 
   * @param x
   *          left-most pixel column
   * @param y
   *          right-most pixel column
   * @param width
   *          width of the paint area
   * @param height
   *          height of the paint area
   * @param img
   *          the map image
   */
  public void paintMapShadows(int x, int y, int width, int height, BufferedImage img) {

    int startRow;
    int currentHeight;
    int currentTerrainHeight;
    int groundLevel;
    Terrain currentTerrain;
    Hex currentHex = null;
    Hex tempHex = null;

    // Bridge stuff
    AffineTransform at;
    Bridge bridge = null;

    // should we bother?
    if (!showShadows) return;

    // paint the map shadows in the image
    for (int col = Math.max(x, 0); col < Math.min(x + width - 1, gridWidth); col++) {

      // set the height of the first location in the grid column
      currentTerrain = getGridTerrain(col, Math.max(y - 1, 0));

      // ignore the following terrains for shadowing
      if (currentTerrain.getType() == Terrain.WOODEN_RUBBLE || currentTerrain.getType() == Terrain.STONE_RUBBLE
          || currentTerrain.getType() == Terrain.CRAGS || currentTerrain.getType() == Terrain.ORCHARD
          || currentTerrain.getType() == Terrain.ORCHARD_OUT_OF_SEASON) {

        currentHeight = Terrain.HEIGHT_PER_LEVEL * getGridGroundLevel(col, Math.max(y - 1, 0));
      }
      else {

        currentHeight = Terrain.HEIGHT_PER_LEVEL
            * (currentTerrain.getHeight() + getGridGroundLevel(col, Math.max(y - 1, 0)));
      }

      // add half level height
      if (currentTerrain.isHalfLevelHeight() && currentTerrain.getType() != Terrain.CRAGS) {
        currentHeight += Terrain.HEIGHT_PER_HALF_LEVEL;

      }

      for (int row = Math.max(y - 1, 0); row < Math.min(y + height + Terrain.HEIGHT_PER_LEVEL * 3, gridHeight); row++) {

        // set the current hex
        tempHex = gridToHex(col, row);
        if (tempHex != currentHex) {

          // set the bridge
          currentHex = tempHex;
          bridge = currentHex.getBridge();
        }

        if (bridge != null && bridge.getShape().contains(col, row)) {

          groundLevel = bridge.getRoadLevel() * Terrain.HEIGHT_PER_LEVEL;
          currentTerrainHeight = Terrain.HEIGHT_PER_HALF_LEVEL;

        }
        else {

          currentTerrain = getGridTerrain(col, row);

          // ignore the following terrains for shadowing
          if (currentTerrain.getType() == Terrain.WOODEN_RUBBLE || currentTerrain.getType() == Terrain.STONE_RUBBLE
              || currentTerrain.getType() == Terrain.CRAGS || currentTerrain.getType() == Terrain.ORCHARD
              || currentTerrain.getType() == Terrain.ORCHARD_OUT_OF_SEASON) {

            currentTerrainHeight = 0;
          }
          else {
            currentTerrainHeight = currentTerrain.getHeight() * Terrain.HEIGHT_PER_LEVEL;
          }

          if (currentTerrain.isHalfLevelHeight() && currentTerrain.getType() != Terrain.CRAGS) {

            currentTerrainHeight += Terrain.HEIGHT_PER_HALF_LEVEL;
          }
          groundLevel = getGridGroundLevel(col, row) * Terrain.HEIGHT_PER_LEVEL;
        }

        // darken pixels in shadow
        if (currentTerrainHeight + groundLevel < currentHeight) {

          // parse the pixel
          int pixel = img.getRGB(col, row);
          int alpha = pixel & 0xFF000000;
          int red = pixel & 0x000000FF;
          int green = (pixel & 0x0000FF00) >> 8;
          int blue = (pixel & 0x00FF0000) >> 16;

          // apply shadow
          red = (int) ((float) red * 0.7);
          green = (int) ((float) green * 0.7);
          blue = (int) ((float) blue * 0.7);

          // re-assemble and paint
          pixel = alpha | red | (green << 8) | (blue << 16);
          img.setRGB(col, row, pixel);

          currentHeight -= 1;
        }
        else if (currentTerrainHeight + groundLevel > currentHeight) {

          // parse the pixel
          int pixel = img.getRGB(col, row);
          int alpha = pixel & 0xFF000000;
          int red = pixel & 0x000000FF;
          int green = (pixel & 0x0000FF00) >> 8;
          int blue = (pixel & 0x00FF0000) >> 16;

          // apply shadow
          red = (int) Math.min(255, (float) red * 2);
          green = (int) Math.min(255, (float) green * 2);
          blue = (int) Math.min(255, (float) blue * 2);

          // re-assemble and paint
          pixel = alpha | red | (green << 8) | (blue << 16);
          img.setRGB(col, row, pixel);

          // set the current height
          currentHeight = currentTerrainHeight + groundLevel;
        }
      }
    }
  }

  /**
   * Paints the hex grid into the map image. Also paints the hex centers mark
   * (including tunnel/sewer, stairway symbols). Shows if smoke and
   * entrenchments exist in hex (for now). This should be called after all other
   * map painting routines when recreating the map image.
   * 
   * @param img
   *          the map image
   */
  // create hex outlines in image
  public void paintMapHexes(Image img) {

    paintMapHexes(img, Color.black);

  }

  // create hex outlines in image
  private void paintMapHexes(Image img, Color c) {

    // should we bother?
    if (!showHexes) return;

    // paint each hex
    for (int col = 0; col < hexGrid.length; col++) {
      for (int row = 0; row < hexGrid[col].length; row++) {

        paintMapHex(img, hexGrid[col][row], true, c);
      }
    }
  }

  // paint a single hex
  private void paintMapHex(Image img, Hex h, boolean paintName, Color c) {

    // get graphics handle
    Graphics2D workSpace = (Graphics2D) img.getGraphics();
    workSpace.setColor(c);

    // draw hex border
    workSpace.drawPolygon(h.getHexBorder());

    // draw hex name
    if (paintName) {
      workSpace.setFont(new Font("Arial", Font.PLAIN, 10));

      workSpace.drawString(h.getName(), h.getCenterLocation().getLOSPoint().x
          - (int) workSpace.getFontMetrics().stringWidth(h.getName()) / 2 + (h.getColumnNumber() == 0 ? 6 : 0)
          + (h.getColumnNumber() == hexGrid.length - 1 ? -7 : 0), h.getYOrigin() + 10);

      // draw hex center
      workSpace.setColor(Color.white);
      if (h.hasStairway()
          && !(h.getTerrain().getType() == Terrain.STONE_BUILDING_1_LEVEL || h.getTerrain().getType() == Terrain.WOODEN_BUILDING_1_LEVEL)) {

        workSpace.fillRect(h.getHexCenter().x - 3, h.getHexCenter().y - 3, 6, 6);
      }
      else {
        workSpace.fillRect(h.getHexCenter().x - 1, h.getHexCenter().y - 1, 2, 2);
      }

      // draw tunnel indicator
      if (h.hasTunnel()) {

        Stroke oldStroke = workSpace.getStroke();
        workSpace.setColor(Color.black);
        workSpace.setStroke(new BasicStroke(2));
        workSpace.draw(new Ellipse2D.Float((float) h.getHexCenter().x - 6, (float) h.getHexCenter().y - 6, (float) 12,
            (float) 12));
        workSpace.setStroke(oldStroke);
      }

      // show entrechments
      if (h.hasEntrenchment()) {

        workSpace.setFont(new Font("Courier", Font.PLAIN, 12));
        workSpace.setColor(Color.white);
        workSpace.drawString(h.getCenterLocation().getDownLocation().getTerrain().getName(), h.getHexCenter().x - 25, h
            .getHexCenter().y + 4);
      }

      // show smoke
      HashSet smoke = h.getSmoke();
      Smoke sm = null;
      int offset = 0;
      if (smoke.size() > 0) {

        Iterator iter = smoke.iterator();
        while (iter.hasNext()) {

          sm = (Smoke) iter.next();
          workSpace.setFont(new Font("Courier", Font.PLAIN, 12));
          workSpace.setColor(Color.white);
          workSpace.drawString(sm.getName(), h.getHexCenter().x - 25, h.getHexCenter().y + 4 + offset);

          offset += 15;
        }
      }
    }
  }

  /**
   * Sets all pixels within the given rectangle to the new terrain type.
   * 
   * @param rect
   *          map area to update
   * @param terr
   *          new terrain type
   */
  public void setGridTerrain(Rectangle rect, Terrain terr) {

    int startx = (int) rect.getX();
    int starty = (int) rect.getY();

    // set the terrain in the map grid
    for (int x = Math.max(startx, 0); x < Math.min(startx + rect.getWidth(), gridWidth); x++) {
      for (int y = Math.max(starty, 0); y < Math.min(starty + rect.getHeight(), gridHeight); y++) {

        terrainGrid[x][y] = (char) terr.getType();
      }
    }

    // set the factory walls, if necesary
    if (terr.isFactoryTerrain()) {
      setFactoryWalls(rect, terr);
    }
  }

  /**
   * Sets the terrain for a pixel as unknown. Used only by the map convertion
   * program.
   * 
   * @param x
   *          coordinate of pixel
   * @param y
   *          coordinate of pixel
   */
  public void setGridTerrainUnknown(int x, int y) {

    terrainGrid[x][y] = (char) Terrain.UNKNOWN;
  }

  /**
   * Determin if the terrain for a pixel is known. Used only by the map
   * convertion program.
   * 
   * @param x
   *          coordinate of pixel
   * @param y
   *          coordinate of pixel
   */
  public boolean isUnknownTerrain(int x, int y) {

    return terrainGrid[x][y] == (char) Terrain.UNKNOWN;
  }

  /**
   * Sets a raw terrain for a pixel as unknown. Used only by the map convertion
   * program.
   * 
   * @param x
   *          coordinate of pixel
   * @param y
   *          coordinate of pixel
   */
  public void setRawTerrain(int x, int y, int terr) {

    terrainGrid[x][y] = (char) terr;
  }

  /**
   * Gets a raw terrain for a pixel. Used only by the map convertion program.
   * 
   * @param x
   *          coordinate of pixel
   * @param y
   *          coordinate of pixel
   */
  public int getRawTerrain(int x, int y) {

    return terrainGrid[x][y];
  }

  /**
   * Sets all pixels within the given shape to the new terrain type.
   * 
   * @param s
   *          map area to update
   * @param terr
   *          new terrain type
   */
  // set the grid terrain for an arbitrary shape
  public void setGridTerrain(Shape s, Terrain terr) {

    Rectangle rect = s.getBounds();
    int startx = (int) rect.getX();
    int starty = (int) rect.getY();
    int terrType = terr.getType();

    // set the terrain in the map grid
    for (int x = Math.max(startx, 0); x < Math.min(startx + rect.getWidth(), gridWidth); x++) {
      for (int y = Math.max(starty, 0); y < Math.min(starty + rect.getHeight(), gridHeight); y++) {

        if (s.contains(x, y)) {

          // only apply rowhouse walls to buildings
          if (terrType == Terrain.ROWHOUSE_WALL || terrType == Terrain.ROWHOUSE_WALL_1_LEVEL
              || terrType == Terrain.ROWHOUSE_WALL_2_LEVEL || terrType == Terrain.ROWHOUSE_WALL_3_LEVEL) {

            //map rowhouse height to current building height
            switch ((int) terrainGrid[x][y]) {

            case Terrain.WOODEN_BUILDING:
            case Terrain.STONE_BUILDING:

              terrainGrid[x][y] = (char) Terrain.ROWHOUSE_WALL;
              break;

            case Terrain.WOODEN_BUILDING_1_LEVEL:
            case Terrain.STONE_BUILDING_1_LEVEL:

              terrainGrid[x][y] = (char) Terrain.ROWHOUSE_WALL_1_LEVEL;
              break;

            case Terrain.WOODEN_BUILDING_2_LEVEL:
            case Terrain.STONE_BUILDING_2_LEVEL:

              terrainGrid[x][y] = (char) Terrain.ROWHOUSE_WALL_2_LEVEL;
              break;

            case Terrain.WOODEN_BUILDING_3_LEVEL:
            case Terrain.STONE_BUILDING_3_LEVEL:

              terrainGrid[x][y] = (char) Terrain.ROWHOUSE_WALL_3_LEVEL;
              break;
            }
          }
          else {
            terrainGrid[x][y] = (char) terrType;
          }
        }
      }
    }

    // set the factory walls, if necesary
    if (terr.isFactoryTerrain()) {
      setFactoryWalls(s.getBounds(), terr);
    }
  }

  /**
   * Determine where factory wall exist for the given terrain type within a
   * rectangular area. For factories, we need to set where the "boundry" of the
   * factory is, replacing it with the appropriate factory wall terrian.
   * 
   * @param rect
   *          map area to update
   * @param terr
   *          building terrain type
   */
  private void setFactoryWalls(Rectangle rect, Terrain terr) {

    int startx = (int) rect.getX();
    int starty = (int) rect.getY();
    int terrType = terr.getType();

    // map the terrain
    int newTerr = terrType;
    switch (terrType) {

    case Terrain.WOODEN_FACTORY_1_5_LEVEL:
      newTerr = Terrain.WOODEN_FACTORY_WALL_1_5_LEVEL;
      break;
    case Terrain.WOODEN_FACTORY_2_5_LEVEL:
      newTerr = Terrain.WOODEN_FACTORY_WALL_2_5_LEVEL;
      break;
    case Terrain.STONE_FACTORY_1_5_LEVEL:
      newTerr = Terrain.STONE_FACTORY_WALL_1_5_LEVEL;
      break;
    case Terrain.STONE_FACTORY_2_5_LEVEL:
      newTerr = Terrain.STONE_FACTORY_WALL_2_5_LEVEL;
      break;
    }

    // set the walls
    for (int x = Math.max(startx, 0); x < Math.min(startx + rect.getWidth(), gridWidth); x++) {
      for (int y = Math.max(starty, 0); y < Math.min(starty + rect.getHeight(), gridHeight); y++) {

        if (terrainList[terrainGrid[x][y]].isFactoryTerrain()
            && (!terrainList[terrainGrid[Math.max(x - 1, 0)][y]].isFactoryTerrain()
                || !terrainList[terrainGrid[Math.min(x + 1, gridWidth)][y]].isFactoryTerrain()
                || !terrainList[terrainGrid[x][Math.max(y - 1, 0)]].isFactoryTerrain() || !terrainList[terrainGrid[x][Math
                .min(y + 1, gridHeight)]].isFactoryTerrain())) {
          terrainGrid[x][y] = (char) newTerr;
        }
      }
    }
  }

  /**
   * Sets the hex terrain for a single hex. All locations in the hex are
   * changed. Any prefab buildings that are in the hex are removed.
   * 
   * @param h
   *          hex to change
   * @param terr
   *          new terrain type
   */
  public void setHexTerrain(Hex h, Terrain terr) {

    // set the center location
    h.setTerrain(terr);

    // set the terrain on the hexsides
    for (int x = 0; x < 6; x++) {

      h.setHexsideTerrain(x, terr);
    }

    // remove any prefab buildings in the hex
    removePrefabBuilding(h);
  }

  /**
   * Sets the hex terrain for an area of the map. All locations within the given
   * shape are changed. If the area contains a hex center location, any prefab
   * buildings in that hex are removed.
   * 
   * @param s
   *          map area to change
   * @param terr
   *          new terrain type
   */
  // set the hex terrain for a section of map
  public void setHexTerrain(Shape s, Terrain terr) {

    // get the affected hexes
    Vector v = intersectedHexes(s.getBounds());
    Iterator iter = v.iterator();
    Hex currentHex = null;
    while (iter.hasNext()) {

      currentHex = (Hex) iter.next();

      // set the center location
      if (s.contains(currentHex.getCenterLocation().getLOSPoint())) {

        currentHex.setTerrain(terr);

        // remove any prefab buildings in the hex
        removePrefabBuilding(currentHex);
      }

      // set the terrain on the hexsides
      for (int x = 0; x < 6; x++) {
        if (s.contains(currentHex.getHexsideLocation(x).getEdgeCenterPoint())) {

          currentHex.setHexsideTerrain(x, terr);
        }
      }
    }
  }

  /**
   * Sets the ground level of all pixels within the given shape to the new
   * terrain height.
   * 
   * @param s
   *          map area to update
   * @param terr
   *          applicable depression terrain
   * @param level
   *          new ground level
   */
  public void setGridGroundLevel(Shape s, Terrain terr, int level) {

    setGridGroundLevel(s, s.getBounds(), terr, level);
  }

  /**
   * Sets the ground level of all pixels within the given rectangle to the new
   * terrain height.
   * 
   * @param s
   *          map area to update
   * @param terr
   *          applicable depression terrain
   * @param level
   *          new ground level
   */
  public void setGridGroundLevel(Rectangle area, Terrain terr, int newLevel) {

    setGridGroundLevel(null, area, terr, newLevel);
  }

  // set the grid map ground level for a section of map
  private void setGridGroundLevel(Shape s, Rectangle area, Terrain terr, int newLevel) {

    // get the rectangle variables once
    int locX = (int) area.getX();
    int locY = (int) area.getY();
    int width = (int) area.getWidth();
    int height = (int) area.getHeight();

    Hex currentHex = null;

    // step through each pixel in the rectangle
    for (int x = Math.max(locX, 0); x <= Math.min(locX + width, gridWidth - 1); x++) {
      for (int y = Math.max(locY, 0); y <= Math.min(locY + height, gridHeight - 1); y++) {

        // point in brush?
        if (s == null || s.contains(x, y)) {

          // setting to depression hex?
          if (terr != null) {

            // set the current hex
            currentHex = gridToHex(x, y);

            // if we're already a depression, use the current elevation
            if (currentHex.isDepressionTerrain()) {
              elevationGrid[x][y] = (byte) (currentHex.getBaseHeight());
            }
            else {
              elevationGrid[x][y] = (byte) (currentHex.getBaseHeight() - 1);
            }
          }
          else {
            elevationGrid[x][y] = (byte) newLevel;
          }
        }
      }
    }
  }

  /**
   * Sets the hex ground level/depression terrain for a section of map. Should
   * be called after setGridGroundLevel.
   * 
   * @param s
   *          map area to update
   * @param terr
   *          applicable depression terrain
   * @param level
   *          new ground level
   */
  public void setHexGroundLevel(Shape s, Terrain terr, int newLevel) {

    // set the hex base elevation and depression terrain
    Vector v = intersectedHexes(s.getBounds());
    Iterator iter = v.iterator();
    Hex currentHex = null;
    Location center = null;
    while (iter.hasNext()) {

      currentHex = (Hex) iter.next();
      center = currentHex.getCenterLocation();

      // set the center location
      if (s.contains(center.getLOSPoint())) {

        if (terr != null) {

          // hex being set to depression terrain?
          if (!center.isDepressionTerrain()) {

            currentHex.setBaseHeight(getGridGroundLevel((int) center.getLOSPoint().getX(), (int) center.getLOSPoint()
                .getY()));

            //set the depression terrain, base level of the six hexsides
            for (int x = 0; x < 6; x++) {

              // on map?
              if ((x == 0 && currentHex.isNorthOnMap()) || (x == 1 && currentHex.isNorthEastOnMap())
                  || (x == 2 && currentHex.isSouthEastOnMap()) || (x == 3 && currentHex.isSouthOnMap())
                  || (x == 4 && currentHex.isSouthWestOnMap()) || (x == 5 && currentHex.isNorthWestOnMap())

              ) {

                // if the hexside location has the same elevation as the center
                // (on the grid), then make it a depression location
                if (getGridGroundLevel((int) center.getLOSPoint().getX(), (int) center.getLOSPoint().getY()) == getGridGroundLevel(
                    (int) currentHex.getHexsideLocation(x).getEdgeCenterPoint().getX(), (int) currentHex
                        .getHexsideLocation(x).getEdgeCenterPoint().getY())) {
                  currentHex.getHexsideLocation(x).setBaseHeight(0);
                  currentHex.getHexsideLocation(x).setDepressionTerrain(terr);
                }
                else {
                  // non-depression hexside locations are one level higher
                  currentHex.getHexsideLocation(x).setBaseHeight(1);
                }
              }
            }
          }
        }
        else {
          currentHex.setBaseHeight(newLevel);
        }

        // update the depression terrain for the hex
        currentHex.setDepressionTerrain(terr);
      }

      // set the depression terrain on the hexsides
      if (terr != null) {
        for (int x = 0; x < 6; x++) {
          if (s.contains(currentHex.getHexsideLocation(x).getEdgeCenterPoint())) {

            currentHex.setHexsideDepressionTerrain(x);

            // if center is depression, ensure base level is reset
            if (center.isDepressionTerrain()) {
              currentHex.getHexsideLocation(x).setBaseHeight(0);
            }
          }
        }
      }
    }
  }

  /**
   * Find the hex at hex grid location (col, row)
   * 
   * @param col
   *          hex column ('A' is 0)
   * @param row
   *          offset of hex in column (first hex in column is 0)
   * @return the Hex at (col, row)
   */
  // find a hex in the array
  public Hex getHex(int col, int row) {

    return hexGrid[col][row];
  }

  /**
   * Find a hex by name
   * 
   * @param name
   *          hex name
   * @return the Hex at (col, row)
   */
  public Hex getHex(String name) {

    Hex h;
    for (int col = 0; col < hexGrid.length; col++) {
      for (int row = 0; row < hexGrid[col].length; row++) {

        h = getHex(col, row);

        if (h.getName().equalsIgnoreCase(name)) {

          return h;
        }
      }
    }

    return null;
  }

  /**
   * Returns a set of hexes that intersect ("touch") the given rectangle.
   * 
   * @param rect
   *          map area
   * @return a Vector containing the intersecting hexes
   */
  public Vector intersectedHexes(Rectangle rect) {

    Vector hexes = new Vector(5, 5);
    Hex currentHex = null;

    // find the hexes in the corner of the rectangle, clip to map boundry
    Hex upperLeft = gridToHex(Math.max((int) rect.getX(), 0), Math.max((int) rect.getY(), 0));
    Hex lowerRight = gridToHex(Math.min((int) (rect.getX() + rect.getWidth()), gridWidth - 1), Math.min((int) (rect
        .getY() + rect.getHeight()), gridHeight - 1));

    // Rectangle completely in a single hex? Add the hex and quit
    if (upperLeft == lowerRight) {

      hexes.addElement(upperLeft);
      return hexes;
    }

    // our desired bounds
    int minX = Math.max(upperLeft.getColumnNumber() - 1, 0);
    int minY = Math.max(upperLeft.getRowNumber() - 1, 0);
    int maxX = Math.min(lowerRight.getColumnNumber() + 1, width - 1);
    int maxY = Math.min(lowerRight.getRowNumber() + 1, height);

    // check all hexes bound by the corners to the vector
    for (int x = minX; x <= maxX; x++) {
      for (int y = minY; y <= Math.min(maxY, hexGrid[x].length - 1); y++) {
        currentHex = getHex(x, y);

        // add hexes that touch
        if (currentHex.isTouchedBy(rect)) {
          hexes.addElement(getHex(x, y));
        }
      }
    }
    return hexes;
  }

  // find a hex in the array
  /**
   * Determines if the hex at (col, row) in the hex grid is on the map
   * 
   * @param col
   *          hex column ('A' is 0)
   * @param row
   *          offset of hex in column (first hex in column is 0)
   * @return true if on map, otherwise false
   */
  public boolean hexOnMap(int col, int row) {

    try {
      Hex temp = hexGrid[col][row];
      return true;
    }
    catch (Exception e) {

      return false;
    }
  }

  /**
   * Find an adjacent hex.
   * 
   * @param h
   *          starting hex
   * @param hexside
   *          hexside shared with the adjacent hex. 0-5 where 0 is top of hex,
   *          continuing clockwise through 5
   * @return adjacent hex, null if hex is not on the map
   */
  public Hex getAdjacentHex(Hex h, int hexside) {

    int col = h.getColumnNumber();
    int row = h.getRowNumber();
    boolean colIsEven = (col % 2 == 0);

    switch (hexside) {
    case 0:
      row -= 1;
      break;
    case 1:
      col += 1;
      row += colIsEven ? 0 : -1;
      break;
    case 2:
      col += 1;
      row += colIsEven ? 1 : 0;
      break;
    case 3:
      row += 1;
      break;
    case 4:
      col -= 1;
      row += colIsEven ? 1 : 0;
      break;
    case 5:
      col -= 1;
      row += colIsEven ? 0 : -1;
      break;
    }

    if (hexOnMap(col, row)) {
      return getHex(col, row);
    }
    else {
      return null;
    }
  }

  /**
   * Finds the hex that contains an image pixel.
   * 
   * @param x
   *          the x coordinate of the map image pixel
   * @param y
   *          the y coordinate of the map image pixel
   * @return the Hex containing the pixel
   */
  public Hex gridToHex(int x, int y) {

    // enure the point is on the map
    x = Math.max(x, 0);
    x = Math.min(x, gridWidth - 1);
    y = Math.max(y, 0);
    y = Math.min(y, gridHeight - 1);

    try {

      int z = (int) (x / (Hex.WIDTH / 3));
      int row;
      int col;

      // in "grey area" between columns?
      if ((z - 1) % 3 == 0) {

        col = (int) Math.ceil(((double) z - 1) / 3);
        row = (int) ((col % 2 == 0) ? y / Hex.HEIGHT : (y + Hex.HEIGHT / 2) / Hex.HEIGHT);

        if (hexGrid[col][row].contains(x, y)) {

          return hexGrid[col][row];
        }
        else if (col % 2 == 0) {

          if (hexGrid[col + 1][row + 1].contains(x, y)) {

            return hexGrid[col + 1][row + 1];
          }
          else {
            return hexGrid[col + 1][row];
          }
        }
        else {
          if ((row - 1 >= 0 && hexGrid[col + 1][row - 1].contains(x, y)) || (row == height)) {

            return hexGrid[col + 1][row - 1];
          }
          else {
            return hexGrid[col + 1][row];
          }
        }
      }
      else {

        col = (int) Math.ceil((double) z / 3);
        row = (int) ((col % 2 == 0) ? y / Hex.HEIGHT : (y + Hex.HEIGHT / 2) / Hex.HEIGHT);
        return hexGrid[col][row];
      }
    }
    catch (Exception e) {

      System.err.println("gridToHex error at X: " + x + " Y: " + y);
      return null;
    }
  }

  /**
   * Determines if a pixel is within the map image.
   * 
   * @param x
   *          the x coordinate of the map image pixel
   * @param y
   *          the y coordinate of the map image pixel
   * @return returns true if within the image, else false
   */
  public boolean onMap(int c, int r) {

    if (r < 0 || c < 0 || c >= gridWidth || r >= gridHeight) return false;
    else
      return true;
  }

  // ignore edge terrain in adjacent hex?
  protected boolean ignoreEdgeTerrain(int referenceHexside, int adjacentHexside) {

    switch (referenceHexside) {
    case 0:
      if (adjacentHexside == 2 || adjacentHexside == 3 || adjacentHexside == 4) {

        return true;
      }
      break;
    case 1:
      if (adjacentHexside == 3 || adjacentHexside == 4 || adjacentHexside == 5) {

        return true;
      }
      break;
    case 2:
      if (adjacentHexside == 4 || adjacentHexside == 5 || adjacentHexside == 0) {

        return true;
      }
      break;
    case 3:
      if (adjacentHexside == 5 || adjacentHexside == 0 || adjacentHexside == 1) {

        return true;
      }
      break;
    case 4:
      if (adjacentHexside == 0 || adjacentHexside == 1 || adjacentHexside == 2) {

        return true;
      }
      break;
    case 5:
      if (adjacentHexside == 1 || adjacentHexside == 2 || adjacentHexside == 3) {

        return true;
      }
      break;
    }

    return false;
  }

  /**
   * Returns the range between two hexes.
   * 
   * @param source
   *          source hex
   * @param target
   *          "target" hex
   * @return the range
   */
  public int range(Hex source, Hex target) {

    int dirX = target.getColumnNumber() > source.getColumnNumber() ? 1 : -1;
    int dirY = target.getRowNumber() > source.getRowNumber() ? 1 : -1;

    int rng = 0;

    int currentRow = source.getRowNumber();
    int currentCol = source.getColumnNumber();

    // step through each row, adjusting the current row as necessary
    while (currentCol != target.getColumnNumber()) {

      // adjust the row as we step through the columns
      if ((currentRow != target.getRowNumber())
          && ((currentCol % 2 == 0 && dirY == 1) || (currentCol % 2 == 1 && dirY == -1))) {

        currentRow += dirY;
      }

      currentCol += dirX;
      rng += 1;
    }

    // we're in the target col: if not in target hex, compute distance
    if (currentRow != target.getRowNumber()) {

      rng += Math.abs(target.getRowNumber() - currentRow);
    }

    return rng;
  }

  /**
   * Determines if a line-of-sight exists between two locations. The auxillary
   * LOS points are used for bypass locations. Standard LOS is drawn to the
   * counterclockwise-most hexspine on the bypassed hexside. The auxillary LOS
   * point is the clockwise-most hexspine on the bypassed hexside.
   * 
   * @param source
   *          source hex
   * @param useAuxSourceLOSPoint
   *          use auxillary bypass aiming point for source location
   * @param target
   *          "target" hex
   * @param useAuxTargetLOSPoint
   *          use auxillary bypass aiming point for target location
   * @param result
   *          <code>LOSResult</code> that will contain all of the LOS
   *          information
   * @param scenario
   *          <code>Scenario</code> that contains all scenario-dependent LOS
   *          information
   */
  public void LOS(Location source, boolean useAuxSourceLOSPoint, Location target, boolean useAuxTargetLOSPoint,
      LOSResult result, Scenario scenario) {

    // reset the results
    result.setClear();
    result.setSourceLocation(source);
    result.setTargetLocation(target);
    result.setUseAuxSourceLOSPoint(useAuxSourceLOSPoint);
    result.setUseAuxTargetLOSPoint(useAuxTargetLOSPoint);

    // add the first hex
    result.addHex(source.getHex());

    // in the same location?
    if (source == target) {

      result.setRange(0);
      return;
    }

    // in tunnel location?
    if (source.getTerrain().isTunnelTerrain() && source.getUpLocation() != target) {

      result.setBlocked((int) source.getLOSPoint().getX(), (int) source.getLOSPoint().getY(),
          "Units in tunnel/sewer can only see the exit location");
      return;
    }

    // check for smoke in source hex here
    HashSet hexSmoke = getAllSmoke(source.getHex());
    if (hexSmoke != null && hexSmoke.size() > 0) {

      Smoke s = null;

      // step through all smoke counters in the source hex
      Iterator iter = hexSmoke.iterator();
      while (iter.hasNext()) {

        s = (Smoke) iter.next();

        if ((source.getAbsoluteHeight() >= s.getLocation().getAbsoluteHeight() && source.getAbsoluteHeight() < s
            .getLocation().getAbsoluteHeight()
            + s.getHeight())
            ||
            // shooting down through smoke
            (source.getAbsoluteHeight() == s.getLocation().getAbsoluteHeight() + s.getHeight() && target
                .getAbsoluteHeight() < source.getAbsoluteHeight())
            ||
            // shooting down through smoke in the same hex
            (source.getHex() == target.getHex()
                && source.getAbsoluteHeight() >= s.getLocation().getAbsoluteHeight() + s.getHeight() && target
                .getAbsoluteHeight() < source.getAbsoluteHeight() + s.getHeight())
            ||
            // source below and target above
            (source.getAbsoluteHeight() < s.getLocation().getAbsoluteHeight() && target.getAbsoluteHeight() > s
                .getLocation().getAbsoluteHeight()
                + s.getHeight())) {

          // add hindrance
          result.addSmokeHindrance(s, (int) source.getLOSPoint().getX(), (int) source.getLOSPoint().getY());
          if (result.isBlocked()) return;
        }
      }
    }

    // in same hex?
    if (source.getHex() == target.getHex()) {

      // Set the range
      result.setRange(0);

      // either unit in the hex center?
      if (source.isCenterLocation() || target.isCenterLocation()) {

        // if both locations are building, ensure difference in levels is <= 1
        // and stairway
        if (source.getTerrain().isBuildingTerrain() && target.getTerrain().isBuildingTerrain()) {
          if (Math.abs(source.getBaseHeight() - target.getBaseHeight()) > 1 || !source.getHex().hasStairway()) {

            result.setBlocked((int) source.getLOSPoint().getX(), (int) source.getLOSPoint().getY(),
                "Crosses building level or no stairway");
            return;
          }
        }

        // source on a bridge and target under bridge, etc?
        if ((source.getTerrain().isBridge() && target.isCenterLocation())
            || (target.getTerrain().isBridge() && source.isCenterLocation())) {

          result.setBlocked((int) source.getLOSPoint().getX(), (int) source.getLOSPoint().getY(),
              "Cannot see location under the bridge");
          return;
        }

        // Out of bunker cover arc?

        // otherwise clear
        return;
      }
    }

    // Otherwise, standard LOS check
    Pt2PtLOS(source, useAuxSourceLOSPoint, target, useAuxTargetLOSPoint, result, scenario);
  }

  protected static String LOS_err_A6_3_1 = "Exits depression before range/elevation restictions are satisfied (A6.3)";
  protected static String LOS_err_A6_3_2 = "Does not enter depression while range/elevation restictions are satisfied (A6.3)";
  protected static String LOS_err_A6_8 = "LOS must leave the building before leaving the source hex to see a location with a different elevation (A6.8 Example 2)";
  protected static String LOS_err_B23_71 = "Cannot see through rowhouse wall (B23.71)";
  protected static String LOS_err_B27_2_1 = "Unit in entrenchment cannot see over hexside terrain to non-adjacent lower target (B27.2)";
  protected static String LOS_err_B27_2_2 = "Cannot see non-adjacent unit in higher elevation entrenchment over hexside terrain (B27.2)";
  protected static String LOS_err_B9_52_1 = "Cannot see through/over bocage (B9.52)";
  protected static String LOS_err_B9_52_2 = "Source or Target location is in a blind hex (B9.52)";
  protected static String LOS_err_B9_2 = "Intervening hexside terrain (B9.2)";
  protected static String LOS_err_A6_2_1 = "Ground level is higher than both the source and target (A6.2)";
  protected static String LOS_err_A6_2_2 = "Half level terrain is higher than both the source and target (A6.2)";
  protected static String LOS_err_A6_2_3 = "Terrain is higher than both the source and target (A6.2)";
  protected static String LOS_err_A6_2_4 = "Must have a height advantage to see over this terrain (A6.2)";
  protected static String LOS_err_A6_4_1 = "Source or Target location is in a blind hex (A6.4)";
  protected static String LOS_err_B10_23 = "Source or Target location is in a blind hex (B10.23)";

  // point to point LOS
  protected void Pt2PtLOS(Location source, boolean useAuxSourceLOSPoint, Location target, boolean useAuxTargetLOSPoint,
      LOSResult result, Scenario scenario) {

    // location variables
    int sourceX = useAuxSourceLOSPoint ? (int) source.getAuxLOSPoint().getX() : (int) source.getLOSPoint().getX();
    int sourceY = useAuxSourceLOSPoint ? (int) source.getAuxLOSPoint().getY() : (int) source.getLOSPoint().getY();
    int targetX = useAuxTargetLOSPoint ? (int) target.getAuxLOSPoint().getX() : (int) target.getLOSPoint().getX();
    int targetY = useAuxTargetLOSPoint ? (int) target.getAuxLOSPoint().getY() : (int) target.getLOSPoint().getY();

    // direction variables
    int colDir = targetX - sourceX < 0 ? -1 : 1;
    int rowDir = targetY - sourceY < 0 ? -1 : 1;
    int numCols = Math.abs(targetX - sourceX) + 1;
    double deltaY;

    // hindrance, etc. variables
    boolean blocked = false;
    String reason = "";

    Terrain currentTerrain = null;
    int currentTerrainHgt = 0;
    int groundLevel = -9999;
    Terrain previousTerrain;
    int previousGroundLevel;

    // hex data variables
    Hex sourceHex = source.getHex();
    Hex targetHex = target.getHex();
    Hex currentHex = sourceHex;
    Hex smokeHex = sourceHex;
    Hex vehicleHex = sourceHex;
    Hex hindranceHex = sourceHex;
    Hex tempHex = null;
    int sourceElevation = sourceHex.getBaseHeight() + source.getBaseHeight();
    int targetElevation = targetHex.getBaseHeight() + target.getBaseHeight();
    int sourceEnterHexside = result.getSourceExitHexside();
    int targetEnterHexside = result.getTargetEnterHexside();
    int range = range(sourceHex, targetHex);
    int rangeToSource = 0;
    int rangeToTarget = range;

    // bridge stuff
    AffineTransform at;
    Shape bridgeArea = null;
    Shape bridgeRoadArea = null;
    Bridge bridge = null;

    boolean continuousSlope = true;
    boolean LOSLeavesBuilding = !sourceHex.getTerrain().isBuildingTerrain();

    // "rise" per grid column
    deltaY = ((double) targetY - (double) sourceY) / (double) numCols;

    // Exiting depression restriction placed when looking out of a depression
    // to a higher elevation where the "elevation difference <= range"
    // restriction has not
    // been satisfied. Must be satisfied before leaving the depression.
    boolean exitsSourceDepression = source.isDepressionTerrain()
        && (targetElevation < sourceElevation
            || (targetElevation - sourceElevation > 0 && targetElevation - sourceElevation < range) || (source
            .isDepressionTerrain()
            && target.isDepressionTerrain() && targetElevation == sourceElevation));
    Hex ignoreGroundLevelHex = null;

    // Entering depression retriction placed when looking into a depression
    // reverse of above
    boolean entersTargetDepression = target.isDepressionTerrain() && sourceElevation - targetElevation > 0
        && sourceElevation - targetElevation < range;

    // grid column entery/exit points
    double enter = sourceY;
    double exit = enter + deltaY;

    // initialize some result variables
    result.setRange(rangeToTarget);
    result.setSourceExitHexside(LOSResult.UNKNOWN);
    result.setTargetEnterHexside(LOSResult.UNKNOWN);

    // set the adjacent hexes
    Hex sourceAdjacentHex = getAdjacentHex(sourceHex, result.getSourceExitHexside());
    Hex targetAdjacentHex = getAdjacentHex(targetHex, result.getTargetEnterHexside());

    // LOS slope variables
    boolean LOSisHorizontal = (sourceY == targetY);
    double doubleSourceX = useAuxSourceLOSPoint ? source.getAuxLOSPoint().getX() : source.getLOSPoint().getX();
    double doubleSourceY = useAuxSourceLOSPoint ? source.getAuxLOSPoint().getY() : source.getLOSPoint().getY();
    double doubleTargetX = useAuxTargetLOSPoint ? target.getAuxLOSPoint().getX() : target.getLOSPoint().getX();
    double doubleTargetY = useAuxTargetLOSPoint ? target.getAuxLOSPoint().getY() : target.getLOSPoint().getY();
    double slope = Math.abs((doubleSourceY - doubleTargetY) / (doubleSourceX - doubleTargetX));

    // set the tolerance to compensate for "fuzzy" geometry of VASL boards
    double tolerance = 0.05;
    if (range == 3 || range == 4) {

      tolerance = 0.028;
    }
    else if (5 <= range && range <= 10) {

      tolerance = 0.02;
    }
    else if (range > 10) {

      tolerance = 0.015;
    }

    boolean LOSis60Degree = Math.abs(slope - Math.tan(Math.toRadians(60))) < tolerance;
    int sourceHexspine = useAuxSourceLOSPoint ? sourceHex.getHexspine(source.getAuxLOSPoint()) : sourceHex
        .getHexspine(source.getLOSPoint());
    int targetHexspine = useAuxTargetLOSPoint ? targetHex.getHexspine(target.getAuxLOSPoint()) : targetHex
        .getHexspine(target.getLOSPoint());

    // set the result with the slope information
    result.setLOSis60Degree(LOSis60Degree);
    if (LOSis60Degree) {
      if (colDir == 1) {
        if (rowDir == 1) {
          result.setSourceExitHexspine(3);
          result.setTargetEnterHexspine(0);
        }
        else {
          result.setSourceExitHexspine(1);
          result.setTargetEnterHexspine(4);
        }
      }
      else {
        if (rowDir == 1) {
          result.setSourceExitHexspine(4);
          result.setTargetEnterHexspine(1);
        }
        else {
          result.setSourceExitHexspine(0);
          result.setTargetEnterHexspine(3);
        }
      }
    }
    else if (slope == 0.0) {
      if (colDir == 1) {
        result.setSourceExitHexspine(2);
        result.setTargetEnterHexspine(5);
      }
      else {
        result.setSourceExitHexspine(5);
        result.setTargetEnterHexspine(2);
      }
    }

    int counter = 0;

    // step through each pixel column
    int currentCol = sourceX;
    for (int col = 0; col < numCols; col++) {

      // set row variables
      int currentRow = (int) enter;
      int numRows = Math.abs((int) exit - (int) enter) + 1;

      // step through each pixel in the current row
      for (int row = 0; row < numRows; row++) {

        // adjust our variables for the new spot
        previousTerrain = currentTerrain;
        previousGroundLevel = groundLevel;
        try {
          currentTerrain = getGridTerrain(currentCol, currentRow);
        }
        catch (Exception e) {
          System.err.println("LOS is off the map at " + currentCol + ", " + currentRow);
          if (useAuxTargetLOSPoint) {
            System.err.println("Target is " + target.getName() + " at " + target.getLOSPoint().getX() + ", "
                + target.getLOSPoint().getY());
          }
          else {
            System.err.println("Target is " + target.getName() + " at " + target.getAuxLOSPoint().getX() + ", "
                + target.getAuxLOSPoint().getY());
          }
          return;
        }

        // set the temp hex
        if (sourceHex.getExtendedHexBorder().contains(currentCol, currentRow)) {

          tempHex = sourceHex;
        }
        else if (targetHex.getExtendedHexBorder().contains(currentCol, currentRow)) {

          tempHex = targetHex;
        }
        else {

          tempHex = gridToHex(currentCol, currentRow);
        }

        // set the other "temp hexes"
        vehicleHex = tempHex;
        smokeHex = tempHex;
        hindranceHex = tempHex;

        // need to do some 'tweeking' to properly handle inherent terrain
        // hindrances, vehicles, and smoke
        // 1) skip inherent terrain that 'spills' into adjacent hex
        // 2) LOS along a hexside misses inherent terrain in adjacent hex
        if (LOSis60Degree || LOSisHorizontal) {

          if (LOSisHorizontal) {

            if (currentTerrain.isInherentTerrain() && tempHex.getCenterLocation().getTerrain().isInherentTerrain()) {

            }
            else if (currentRow != 0
                && gridToHex(currentCol, currentRow - 1).getCenterLocation().getTerrain().isInherentTerrain()) {

              hindranceHex = gridToHex(currentCol, currentRow - 1);
              currentTerrain = terrainList[hindranceHex.getCenterLocation().getTerrain().getType()];
            }
            else if (currentRow != gridHeight
                && gridToHex(currentCol, currentRow + 1).getCenterLocation().getTerrain().isInherentTerrain()) {

              hindranceHex = gridToHex(currentCol, currentRow + 1);
              currentTerrain = terrainList[hindranceHex.getCenterLocation().getTerrain().getType()];
            }
            else if (currentTerrain.isInherentTerrain()
                && !tempHex.getCenterLocation().getTerrain().isInherentTerrain()) {
              if (tempHex.getCenterLocation().getTerrain().isInherentTerrain()) {

                currentTerrain = terrainList[tempHex.getCenterLocation().getTerrain().getType()];
              }
              else {
                currentTerrain = terrainList[Terrain.OPEN_GROUND];
              }
            }
          }
          else if (LOSis60Degree) {

            if (currentTerrain.isInherentTerrain() && tempHex.getCenterLocation().getTerrain().isInherentTerrain()) {

            }
            else if (currentCol != 0 && currentRow != 0
                && gridToHex(currentCol - 1, currentRow - 1).getCenterLocation().getTerrain().isInherentTerrain()) {

              hindranceHex = gridToHex(currentCol - 1, currentRow - 1);
              currentTerrain = terrainList[hindranceHex.getCenterLocation().getTerrain().getType()];
            }
            else if (currentCol != gridWidth && currentRow != gridHeight
                && gridToHex(currentCol + 1, currentRow + 1).getCenterLocation().getTerrain().isInherentTerrain()) {

              hindranceHex = gridToHex(currentCol + 1, currentRow + 1);
              currentTerrain = terrainList[hindranceHex.getCenterLocation().getTerrain().getType()];
            }
            else if (currentCol != 0 && currentRow != gridHeight
                && gridToHex(currentCol - 1, currentRow + 1).getCenterLocation().getTerrain().isInherentTerrain()) {

              hindranceHex = gridToHex(currentCol - 1, currentRow + 1);
              currentTerrain = terrainList[hindranceHex.getCenterLocation().getTerrain().getType()];
            }
            else if (currentCol != gridWidth && currentRow != 0
                && gridToHex(currentCol + 1, currentRow - 1).getCenterLocation().getTerrain().isInherentTerrain()) {

              hindranceHex = gridToHex(currentCol + 1, currentRow - 1);
              currentTerrain = terrainList[hindranceHex.getCenterLocation().getTerrain().getType()];
            }
            else if (currentTerrain.isInherentTerrain()
                && !tempHex.getCenterLocation().getTerrain().isInherentTerrain()) {
              if (tempHex.getCenterLocation().getTerrain().isInherentTerrain()) {

                currentTerrain = terrainList[tempHex.getCenterLocation().getTerrain().getType()];
              }
              else {
                currentTerrain = terrainList[Terrain.OPEN_GROUND];
              }
            }
          }
        }
        else if (currentTerrain.isInherentTerrain() && !tempHex.getCenterLocation().getTerrain().isInherentTerrain()) {
          if (tempHex.getCenterLocation().getTerrain().isInherentTerrain()) {

            currentTerrain = terrainList[tempHex.getCenterLocation().getTerrain().getType()];
          }
          else {

            currentTerrain = terrainList[Terrain.OPEN_GROUND];
          }
        }

        currentTerrainHgt = currentTerrain.getHeight();
        groundLevel = (int) elevationGrid[currentCol][currentRow];

        counter += 1;

        /***********************************************************************
         * Set the current hex
         **********************************************************************/
        if (tempHex != currentHex) {

          currentHex = tempHex;
          rangeToSource = range(currentHex, sourceHex);
          rangeToTarget = range(currentHex, targetHex);

          // add the current hex
          result.addHex(currentHex);

          // set the bridge variables
          bridge = currentHex.getBridge();

          // get the vehicle hindrances
          //if LOS on hex side, use the hex that has the most vehicles
          if ((LOSisHorizontal || LOSis60Degree) && sourceElevation == targetElevation) {
            int numVeh = scenario.getVehicles(vehicleHex, sourceElevation).size();
            if (currentRow != 0
                && scenario.getVehicles(gridToHex(currentCol, currentRow - 1), sourceElevation).size() > numVeh) {

              vehicleHex = gridToHex(currentCol, currentRow - 1);
              numVeh = scenario.getVehicles(vehicleHex).size();
            }
            if (currentRow != gridHeight
                && scenario.getVehicles(gridToHex(currentCol, currentRow + 1), sourceElevation).size() > numVeh) {

              vehicleHex = gridToHex(currentCol, currentRow + 1);
              numVeh = scenario.getVehicles(vehicleHex).size();
            }
            if (LOSis60Degree && currentCol != 0 && currentRow != gridHeight
                && scenario.getVehicles(gridToHex(currentCol - 1, currentRow + 1), sourceElevation).size() > numVeh) {

              vehicleHex = gridToHex(currentCol - 1, currentRow + 1);
              numVeh = scenario.getVehicles(vehicleHex).size();
            }
            if (LOSis60Degree && currentCol != gridWidth && currentRow != 0
                && scenario.getVehicles(gridToHex(currentCol + 1, currentRow - 1), sourceElevation).size() > numVeh) {

              vehicleHex = gridToHex(currentCol + 1, currentRow - 1);
            }
          }

          // vehicle hindrance?
          if (vehicleHex != sourceHex && vehicleHex != targetHex
              && source.getAbsoluteHeight() == target.getAbsoluteHeight()) {

            Iterator vehicles = scenario.getVehicles(vehicleHex).iterator();
            Vehicle v;
            while (vehicles.hasNext()) {

              v = (Vehicle) vehicles.next();

              // see if a LOS exists to the vehicle
              LOSResult res1 = new LOSResult();
              LOSResult res2 = new LOSResult();
              LOS(source, useAuxSourceLOSPoint, v.getLocation(), false, res1, scenario);
              LOS(target, useAuxTargetLOSPoint, v.getLocation(), false, res2, scenario);

              if (!res1.isBlocked() && !res2.isBlocked()) {

                // add vehicle hindrance
                result.addVehicleHindrance(v, currentCol, currentRow, scenario);
                if (result.isBlocked()) return;
              }
            }
          }

          if (bridge != null) {

            // set bridge area
            bridgeArea = bridge.getShape();
            bridgeRoadArea = bridge.getRoadShape();
          }

          // still continuous slope?
          if (Math.abs(sourceElevation - currentHex.getBaseHeight()) != rangeToSource) {

            continuousSlope = false;
          }

          // lift the depression exit restriction?
          if (exitsSourceDepression) {

            if ((currentHex.isDepressionTerrain() && targetElevation - currentHex.getBaseHeight() >= rangeToTarget) ||
            // LOS leaves gully because hex elevation is <= the elevation of the
            // gully
                (!currentHex.isDepressionTerrain() && currentHex.getBaseHeight() <= sourceElevation)) {

              ignoreGroundLevelHex = currentHex;
              exitsSourceDepression = false;
            }
          }

          // hex has smoke, or LOS on hexside and adjacent hex has smoke?
          HashSet hexSmoke = getAllSmoke(currentHex);
          if (hexSmoke.size() == 0) {

            if (LOSisHorizontal) {

              if (currentRow != 0) {

                hexSmoke = getAllSmoke(gridToHex(currentCol, currentRow - 1));
              }
              if (hexSmoke.size() == 0 && currentRow != gridHeight) {

                hexSmoke = getAllSmoke(gridToHex(currentCol, currentRow + 1));
              }
            }

            else if (LOSis60Degree) {

              if (currentCol != 0 && currentRow != 0) {

                hexSmoke = getAllSmoke(gridToHex(currentCol - 1, currentRow - 1));
              }
              if (hexSmoke.size() == 0 && currentCol != gridWidth && currentRow != gridHeight) {

                hexSmoke = getAllSmoke(gridToHex(currentCol + 1, currentRow + 1));
              }
              if (hexSmoke.size() == 0 && currentCol != 0 && currentRow != gridHeight
                  && gridToHex(currentCol - 1, currentRow + 1).getCenterLocation().getTerrain().isInherentTerrain()) {

                hexSmoke = getAllSmoke(gridToHex(currentCol - 1, currentRow + 1));
              }
              if (hexSmoke.size() == 0 && currentCol != gridWidth && currentRow != 0
                  && gridToHex(currentCol + 1, currentRow - 1).getCenterLocation().getTerrain().isInherentTerrain()) {

                hexSmoke = getAllSmoke(gridToHex(currentCol + 1, currentRow - 1));
              }
            }
          }

          if (hexSmoke != null && hexSmoke.size() > 0) {

            Smoke s = null;

            // step through all smoke counters in the hex
            Iterator iter = hexSmoke.iterator();
            while (iter.hasNext()) {

              s = (Smoke) iter.next();

              // in target hex
              if ((currentHex == targetHex && target.getAbsoluteHeight() >= s.getLocation().getAbsoluteHeight() && target
                  .getAbsoluteHeight() < s.getLocation().getAbsoluteHeight() + s.getHeight())
                  || (target.getAbsoluteHeight() == s.getLocation().getAbsoluteHeight() + s.getHeight() && source
                      .getAbsoluteHeight() < target.getAbsoluteHeight())) {

                // add hindrance
                result.addSmokeHindrance(s, currentCol, currentRow);
                if (result.isBlocked()) return;
              }
              // between source and target
              else if (Math.max(source.getAbsoluteHeight(), target.getAbsoluteHeight()) <= s.getLocation()
                  .getAbsoluteHeight()
                  + s.getHeight()
                  && Math.min(source.getAbsoluteHeight(), target.getAbsoluteHeight()) >= s.getLocation()
                      .getAbsoluteHeight()) {

                if (// source and target under the smoke? Ignore
                //									!(target.getAbsoluteHeight() <
                // s.getLocation().getAbsoluteHeight() &&
                // source.getAbsoluteHeight() <
                // s.getLocation().getAbsoluteHeight()) &&
                // source and target above smoke? Ignore
                !(source.getAbsoluteHeight() == s.getLocation().getAbsoluteHeight() + s.getHeight() && target
                    .getAbsoluteHeight() == s.getLocation().getAbsoluteHeight() + s.getHeight())) {
                  // add hindrance
                  result.addSmokeHindrance(s, currentCol, currentRow);
                  if (result.isBlocked()) return;
                }
              }
              // creates "blind hex"
              else if (isBlindHex(sourceElevation, targetElevation, rangeToSource, rangeToTarget, groundLevel, s
                  .getHeight(), currentTerrain, currentHex)) {

                // add hindrance
                result.addSmokeHindrance(s, currentCol, currentRow);
                if (result.isBlocked()) return;
              }
            }
          }
        }

        // LOS leaves the source building?
        if (!LOSLeavesBuilding) {
          if (!currentTerrain.isBuildingTerrain()) {
            LOSLeavesBuilding = true;
          }
        }

        /***********************************************************************
         * Depression terrain
         **********************************************************************/
        // restricted by exiting a depression? (checked in all hexes)
        if (exitsSourceDepression) {

          // LOS still in the depression?
          if (groundLevel > currentHex.getBaseHeight()) {

            blocked = true;
            reason = LOS_err_A6_3_1;
          }
        }

        // LOS must enter a depression?
        // range must be <= elevation difference or be in the depression
        if (entersTargetDepression) {
          if (rangeToSource > (sourceElevation - targetElevation)
              && !(currentHex.isDepressionTerrain() && groundLevel == currentHex.getBaseHeight())) {

            blocked = true;
            reason = LOS_err_A6_3_2;
          }
        }

        /***********************************************************************
         * Leaving buildings
         **********************************************************************/
        // blocked LOS leaving a building?
        if (!LOSLeavesBuilding && currentHex != sourceHex && currentTerrain.isBuildingTerrain()
            && target.getTerrain().isBuildingTerrain() && sourceElevation != targetElevation
            && groundLevel + currentTerrainHgt >= sourceElevation) {
          reason = LOS_err_A6_8;
          blocked = true;
        }

        /***********************************************************************
         * Edge terrain
         **********************************************************************/
        if (currentTerrain.isEdgeTerrain() && currentTerrain.getType() != Terrain.CLIFF) {

          // rowhouse wall?
          if (currentTerrain.getType() == Terrain.ROWHOUSE_WALL
              || currentTerrain.getType() == Terrain.ROWHOUSE_WALL_1_LEVEL
              || currentTerrain.getType() == Terrain.ROWHOUSE_WALL_2_LEVEL
              || currentTerrain.getType() == Terrain.ROWHOUSE_WALL_3_LEVEL) {

            // always blocks if...
            if ( //higher than both source/target
            (groundLevel + currentTerrainHgt > sourceElevation && groundLevel + currentTerrainHgt > targetElevation)
                ||
                //same height as both source/target, but 1/2 level
                (groundLevel + currentTerrainHgt == sourceElevation
                    && groundLevel + currentTerrainHgt == targetElevation && currentTerrain.isHalfLevelHeight())
                ||
                //same height as higher source/target, but other is lower
                (groundLevel + currentTerrainHgt == Math.max(sourceElevation, targetElevation) && groundLevel
                    + currentTerrainHgt > Math.min(sourceElevation, targetElevation))) {

              reason = LOS_err_B23_71;
              blocked = true;
            }

            // otherwise check for blind hexes
            else if (isBlindHex(sourceElevation, targetElevation, rangeToSource, rangeToTarget, groundLevel,
                currentTerrainHgt, currentTerrain, currentHex)) {

              reason = "Source or Target location is in a blind hex";
              blocked = true;
            }
          }

          else {

            // target elevation must > source if in entrenchment
            if (source.getTerrain().isEntrenchmentTerrain()) {

              if (range > 1 && targetElevation <= sourceElevation) {

                blocked = true;
                reason = LOS_err_B27_2_1;
              }
            }
            else if (target.getTerrain().isEntrenchmentTerrain()) {

              if (range > 1 && targetElevation >= sourceElevation) {

                blocked = true;
                reason = LOS_err_B27_2_2;
              }
            }
            else {

              // should we ignore the edge terrain?

              boolean ignore = isIgnorableHexsideTerrain(sourceHex, currentHex.nearestLocation(currentCol, currentRow),
                  result.getSourceExitHexspine(), currentCol, currentRow)
                  || isIgnorableHexsideTerrain(targetHex, currentHex.nearestLocation(currentCol, currentRow), result
                      .getTargetEnterHexspine(), currentCol, currentRow);

              if (!ignore) {

                // check bocage
                if (currentTerrain.getType() == Terrain.BOCAGE) {

                  // always blocks if...
                  if ( //higher than both source/target
                  (groundLevel + currentTerrainHgt > sourceElevation && groundLevel + currentTerrainHgt > targetElevation)
                      ||
                      //same height as both source/target, but 1/2 level
                      (groundLevel + currentTerrainHgt == sourceElevation
                          && groundLevel + currentTerrainHgt == targetElevation && currentTerrain.isHalfLevelHeight())
                      ||
                      //same height as higher source/target, but other is lower
                      (groundLevel + currentTerrainHgt == Math.max(sourceElevation, targetElevation) && groundLevel
                          + currentTerrainHgt > Math.min(sourceElevation, targetElevation))) {

                    reason = LOS_err_B9_52_1;
                    blocked = true;
                  }

                  // otherwise check for blind hexes
                  else if (isBlindHex(sourceElevation, targetElevation, rangeToSource, rangeToTarget, groundLevel,
                      currentTerrainHgt, currentTerrain, currentHex)) {

                    reason = LOS_err_B9_52_2;
                    blocked = true;
                  }
                }

                // on the same level?
                else if (groundLevel == sourceElevation && groundLevel == targetElevation) {

                  blocked = true;
                  reason = LOS_err_B9_2;
                }
              }
            }
          }
        }

        // Can we ignore the current hex?
        else if ((currentHex != sourceHex && currentHex != targetHex)
            || (currentHex == sourceHex && !currentTerrain.isOpenTerrain() && !source.isCenterLocation())
            || (currentHex == targetHex && !currentTerrain.isOpenTerrain() && !target.isCenterLocation())) {

          /*********************************************************************
           * Bridge causes hindrance?
           ********************************************************************/
          if (currentHex.hasBridge()) {

            if (sourceElevation == targetElevation && sourceElevation == bridge.getRoadLevel()) {

              // on bridge but not on road?
              if (bridgeArea.contains(currentCol, currentRow) && !bridgeRoadArea.contains(currentCol, currentRow)) {

                // add hindrance
                if (addHindranceHex(currentHex, sourceHex, targetHex, currentCol, currentRow, result)) return;
              }
            }
          }

          /*********************************************************************
           * Ground level higher than both source and target?
           ********************************************************************/
          if (groundLevel > sourceElevation && groundLevel > targetElevation) {

            reason = LOS_err_A6_2_1;
            blocked = true;
          }

          /*********************************************************************
           * Lower level of split terrain
           ********************************************************************/
          else if (currentTerrain.hasSplit() && groundLevel == sourceElevation && groundLevel == targetElevation) {

            if (currentTerrain.isLowerLOSObstacle()) {

              reason = "This terrain blocks LOS to same same elevation Source and Target";
              blocked = true;
            }
            else if (currentTerrain.isLowerLOSHindrance()) {

              // add hindrance
              if (addHindranceHex(hindranceHex, sourceHex, targetHex, currentCol, currentRow, result)) return;
            }
          }

          /*********************************************************************
           * Half level terrain on same elevation
           ********************************************************************/
          else if (currentTerrain.isHalfLevelHeight() && groundLevel + currentTerrainHgt == sourceElevation
              && groundLevel + currentTerrainHgt == targetElevation) {

            // terrain blocks LOS?
            if (currentTerrain.isLOSObstacle()) {
              reason = LOS_err_A6_2_2;
              blocked = true;
            }
            // must be hindrance
            else {

              // add hindrance
              if (addHindranceHex(hindranceHex, sourceHex, targetHex, currentCol, currentRow, result)) return;
            }
          }

          /*********************************************************************
           * Higher than both source and target
           ********************************************************************/
          else if (groundLevel + currentTerrainHgt > sourceElevation
              && groundLevel + currentTerrainHgt > targetElevation) {

            // terrain blocks LOS?
            if (currentTerrain.isLOSObstacle()) {
              reason = LOS_err_A6_2_3;
              blocked = true;
            }
            // must be hindrance
            else {

              // add hindrance
              if (addHindranceHex(hindranceHex, sourceHex, targetHex, currentCol, currentRow, result)) return;
            }
          }

          /*********************************************************************
           * Blocked if equal to the higher of either location when...
           ********************************************************************/
          else if (groundLevel + currentTerrainHgt == Math.max(sourceElevation, targetElevation)
              && groundLevel + currentTerrainHgt > Math.min(sourceElevation, targetElevation) &&
              // are exiting gully restrictions satisfied?
              !(ignoreGroundLevelHex != null && ignoreGroundLevelHex.containsExtended(currentCol, currentRow)) &&
              // are entering gully restrictions satisfied?
              !(entersTargetDepression && currentHex.isDepressionTerrain())
              && !(exitsSourceDepression && currentHex.isDepressionTerrain())) {

            // Need to handle special case where source unit is adjacent to a
            // water obstacle looking
            // at a target in the water obstacle. We can ignore the bit of open
            // ground that extends into
            // the first water hex.
            if (!(currentHex.getCenterLocation().getTerrain().isWaterTerrain() && currentTerrain.getHeight() < 1 && ((rangeToSource == 1
                && sourceElevation > targetElevation && target.getHex().getCenterLocation().getTerrain()
                .isWaterTerrain()) || (rangeToTarget == 1 && targetElevation > sourceElevation && source.getHex()
                .getCenterLocation().getTerrain().isWaterTerrain())))) {

              // if orchard, then hindrance
              if (currentTerrain.getType() == Terrain.ORCHARD_OUT_OF_SEASON) {

                if (addHindranceHex(hindranceHex, sourceHex, targetHex, currentCol, currentRow, result)) return;

              }
              else {
                reason = LOS_err_A6_2_4;
                blocked = true;
              }

            }
          }
          /*
           * // terrain blocks LOS? if(currentTerrain.isLOSObstacle()){ reason =
           * "Must have a height advantage to see over this terrain (A6.2)";
           * blocked = true; } // must be hindrance else {
           *  // add hindrance if (addHindranceHex(hindranceHex, sourceHex,
           * targetHex, currentCol, currentRow, result)) return; }
           *  
           */

          /*********************************************************************
           * Check for blind hexes
           ********************************************************************/
          else if (groundLevel + currentTerrainHgt > Math.min(sourceElevation, targetElevation)
              && groundLevel + currentTerrainHgt < Math.max(sourceElevation, targetElevation)) {

            if (isBlindHex(sourceElevation, targetElevation, rangeToSource, rangeToTarget, groundLevel,
                currentTerrainHgt, currentTerrain, currentHex, nearestHexsideIsCliff(currentCol, currentRow))) {
              // blocked if terrain is obstacle
              if (currentTerrain.isLOSObstacle()) {
                reason = LOS_err_A6_4_1;
                blocked = true;
              }

              // see if ground level alone creates blind hex
              else if (groundLevel > Math.min(sourceElevation, targetElevation)
                  && groundLevel < Math.max(sourceElevation, targetElevation)
                  && isBlindHex(sourceElevation, targetElevation, rangeToSource, rangeToTarget, groundLevel, 0,
                      currentTerrain, currentHex, nearestHexsideIsCliff(currentCol, currentRow))) {
                reason = LOS_err_B10_23;
                blocked = true;
              }

              // hindrance creates "blind hex", if not target/source hex
              else if (currentHex != targetHex && currentHex != sourceHex) {

                // only one hindrance for out-of-season orchard
                if (currentTerrain.getType() == Terrain.ORCHARD_OUT_OF_SEASON) {

                  if (rangeToTarget == 1) {

                    if (addHindranceHex(hindranceHex, sourceHex, targetHex, currentCol, currentRow, result)) return;
                  }
                }
                else {
                  // add hindrance
                  if (addHindranceHex(hindranceHex, sourceHex, targetHex, currentCol, currentRow, result)) return;
                }
              }
            }
          }
        }

        // set results if blocked
        if (blocked == true) {
          result.setBlocked(currentCol, currentRow, reason);
          return;
        }

        // next row
        currentRow += rowDir;
      }

      // adjust variables for next column
      enter = exit;
      currentCol += colDir;

      // adjust variables for last column
      if (col + 1 == numCols) {
        exit = targetY;
      }
      else {
        exit += deltaY;
      }
    }

    // set continuous slope result
    result.setContinuousSlope(continuousSlope);
  }

  // nearest location is a cliff?
  private boolean nearestHexsideIsCliff(int x, int y) {

    Hex h = gridToHex(x, y);
    Location l = gridToHex(x, y).nearestLocation(x, y);

    if (l.isCenterLocation()) {

      return false;
    }
    else {

      return h.hasCliff(h.getLocationHexside(l));
    }
  }

  // add hindrance and return true if LOS blocked
  private boolean addHindranceHex(Hex currentHex, Hex sourceHex, Hex targetHex, int currentCol, int currentRow,
      LOSResult result) {

    // add hex if necessary
    if (currentHex != sourceHex && currentHex != targetHex) {

      result.addMapHindrance(currentHex, currentCol, currentRow);

      // see if hindrance caused LOS to be blocked
      if (result.isBlocked()) {
        return true;
      }
      else {
        return false;
      }
    }
    return false;
  }

  private boolean isBlindHex(int sourceElevation, int targetElevation, int rangeToSource, int rangeToTarget,
      int groundLevel, int currentTerrainHgt, Terrain currentTerrain, Hex currentHex) {

    return isBlindHex(sourceElevation, targetElevation, rangeToSource, rangeToTarget, groundLevel, currentTerrainHgt,
        currentTerrain, currentHex, false);
  }

  protected boolean isBlindHex(int sourceElevation, int targetElevation, int rangeToSource, int rangeToTarget,
      int groundLevel, int currentTerrainHgt, Terrain currentTerrain, Hex currentHex, boolean isCliffHexside) {

    int numBlindHexes = 0;
    int temp = 0;

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

    // is the obstacle a non-cliff crestline?
    if (currentTerrainHgt == 0 && !isCliffHexside) {

      return rangeToTarget <= Math.max(2 * (groundLevel + currentTerrainHgt) + ((int) rangeToSource / 5)
          - sourceElevation - targetElevation, 0);
    }
    else {

      return rangeToTarget <= Math.max(2 * (groundLevel + currentTerrainHgt) + ((int) rangeToSource / 5)
          - sourceElevation - targetElevation + 1, 1);
    }
  }

  /**
   * Creates a "movement path" that represents the movement of a unit between
   * two locations.
   * 
   * @param start
   *          current unit location
   * @param end
   *          destinationlocation
   * @param prevMovementResults
   *          results for this units previous movement. Needed to compute
   *          certain movement costs.
   * @return MovementResult containing all movement information
   */
  public MovementResult getMovementResult(Location start, // starting location
      Location destination, // ending location
      MovementResult prevMovementResults) {

    //convert location-to-location to vector

    // same location?
    if (start == destination) {

      return new MovementResult(null, null, null, null, MovementResult.NO_HEXSIDE_CROSSED,
          MovementResult.NO_HEXSIDE_CROSSED, false, "Start and destination locations are the same", prevMovementResults);

    }

    // same hex?
    if (start.getHex() == destination.getHex()) {

      Hex h = start.getHex();

      // move up and down?
      if (start.getUpLocation() == destination) {
        return getMovementResult(start, 6, 0, prevMovementResults);
      }
      if (start.getDownLocation() == destination) {
        return getMovementResult(start, 7, 0, prevMovementResults);
      }

      // moving from center to hexside?
      if (start.isCenterLocation() && h.isHexsideLocation(destination)) {

        return new MovementResult(null, null, null, null, MovementResult.NO_HEXSIDE_CROSSED,
            MovementResult.NO_HEXSIDE_CROSSED, false, "Cannot move from the center of a hex to a hexside location",
            prevMovementResults);

      }

      // moving from hexside to center?
      if (destination.isCenterLocation() && h.isHexsideLocation(start)) {

        return getMovementResult(start, h.getOppositeHexside(h.getLocationHexside(start)), 0, prevMovementResults);
      }

      // continuing in bypass?
      if (h.isHexsideLocation(destination) && h.isHexsideLocation(start)) {

        int startHexside = h.getLocationHexside(start);
        int destHexside = h.getLocationHexside(destination);

        // moving clockwise to adjacent hexside
        if (startHexside == 0 && destHexside == 1) return getMovementResult(start, 2, 0, prevMovementResults);
        if (startHexside == 1 && destHexside == 2) return getMovementResult(start, 3, 0, prevMovementResults);
        if (startHexside == 2 && destHexside == 3) return getMovementResult(start, 4, 0, prevMovementResults);
        if (startHexside == 3 && destHexside == 4) return getMovementResult(start, 5, 0, prevMovementResults);
        if (startHexside == 4 && destHexside == 5) return getMovementResult(start, 0, 0, prevMovementResults);
        if (startHexside == 5 && destHexside == 0) return getMovementResult(start, 1, 0, prevMovementResults);

        // moving counter-clockwise to adjacent hexside
        if (startHexside == 0 && destHexside == 5) return getMovementResult(start, 4, 0, prevMovementResults);
        if (startHexside == 1 && destHexside == 0) return getMovementResult(start, 5, 0, prevMovementResults);
        if (startHexside == 2 && destHexside == 1) return getMovementResult(start, 0, 0, prevMovementResults);
        if (startHexside == 3 && destHexside == 2) return getMovementResult(start, 1, 0, prevMovementResults);
        if (startHexside == 4 && destHexside == 3) return getMovementResult(start, 2, 0, prevMovementResults);
        if (startHexside == 5 && destHexside == 4) return getMovementResult(start, 3, 0, prevMovementResults);
      }
    }

    // adjacent hex?
    if (range(start.getHex(), destination.getHex()) == 1) {

      // determine across which hexside the destination hex lies
      int destHexside = 0;
      if (getAdjacentHex(start.getHex(), 1) == destination.getHex()) {
        destHexside = 1;
      }
      if (getAdjacentHex(start.getHex(), 2) == destination.getHex()) {
        destHexside = 2;
      }
      if (getAdjacentHex(start.getHex(), 3) == destination.getHex()) {
        destHexside = 3;
      }
      if (getAdjacentHex(start.getHex(), 4) == destination.getHex()) {
        destHexside = 4;
      }
      if (getAdjacentHex(start.getHex(), 5) == destination.getHex()) {
        destHexside = 5;
      }

      // hex center to hex center?
      if (start.isCenterLocation() && destination.isCenterLocation()) {

        return getMovementResult(start, destHexside, 0, prevMovementResults);
      }

      // hex center to bypass?
      if (start.isCenterLocation() && destination.getHex().isHexsideLocation(destination)) {

      }

      // bypass to hex center?
      if (start.getHex().isHexsideLocation(start) && destination.isCenterLocation()) {

        int startHexside = start.getHex().getLocationHexside(start);
        if (startHexside == 0 && (destHexside == 5 || destHexside == 0 || destHexside == 1)) return getMovementResult(
            start, destHexside, 0, prevMovementResults);
        if (startHexside == 1 && (destHexside == 0 || destHexside == 1 || destHexside == 2)) return getMovementResult(
            start, destHexside, 0, prevMovementResults);
        if (startHexside == 2 && (destHexside == 1 || destHexside == 2 || destHexside == 3)) return getMovementResult(
            start, destHexside, 0, prevMovementResults);
        if (startHexside == 3 && (destHexside == 2 || destHexside == 3 || destHexside == 4)) return getMovementResult(
            start, destHexside, 0, prevMovementResults);
        if (startHexside == 4 && (destHexside == 3 || destHexside == 4 || destHexside == 5)) return getMovementResult(
            start, destHexside, 0, prevMovementResults);
        if (startHexside == 5 && (destHexside == 4 || destHexside == 5 || destHexside == 0)) return getMovementResult(
            start, destHexside, 0, prevMovementResults);
      }

    }

    // bad movement
    return new MovementResult(null, null, null, null, MovementResult.NO_HEXSIDE_CROSSED,
        MovementResult.NO_HEXSIDE_CROSSED, false, "Invalid movement", prevMovementResults);
  }

  /**
   * Creates a "movement path" that represents the movement of a unit between
   * two locations.
   * 
   * @param loc
   *          current unit location
   * @param direction
   *          which direction the unit is moving: 0..5 for north..northwest,
   *          6=up, 7=down
   * @param bypass
   *          using bypass movement:0=none, 1=clockwise, 2=counterclockwise
   * @param prevMovementResults
   *          results for this units previous movement. Needed to compute
   *          certain movement costs.
   * @return MovementResult containing all movement information
   */
  public MovementResult getMovementResult(Location loc, // starting location
      int direction, // 0..5 for north..northwest, 6=up, 7=down
      int bypass, // 0=none, 1=cw, 2=ccw
      MovementResult prevMovementResults) {

    Hex startHex = loc.getHex();
    Hex enterHex = null;

    // horizontal movement?
    if (0 <= direction && direction <= 5) {

      // starting movement in bypass location?
      if (!startHex.isCenterLocation(loc)) {

        int currentHexside = startHex.getLocationHexside(loc);

        // moving out of hex
        if (direction == currentHexside) {

          enterHex = getAdjacentHex(startHex, direction);
          if (enterHex == null) {

            return new MovementResult(null, null, null, null, MovementResult.NO_HEXSIDE_CROSSED,
                MovementResult.NO_HEXSIDE_CROSSED, false, "Destination hex is off of the map", prevMovementResults);
          }
          else {

            // enter new hex with cw bypass
            if (bypass == 1) {

              Location endLoc = enterHex.getHexsideLocation(enterHex.getClockwiseHexside(enterHex
                  .getOppositeHexside(direction)));
              ;

              // bypass allowed?
              if (endLoc.isBypassAllowed()) {

                return new MovementResult(loc, startHex.getHexsideLocation(direction), enterHex
                    .getHexsideLocation(enterHex.getOppositeHexside(direction)), endLoc, direction, enterHex
                    .getOppositeHexside(direction), true, "", prevMovementResults);
              }
              else {
                return new MovementResult(null, null, null, null, MovementResult.NO_HEXSIDE_CROSSED,
                    MovementResult.NO_HEXSIDE_CROSSED, false, "Bypass of " + endLoc.getName() + " is not allowed",
                    prevMovementResults);
              }
            }

            // enter new hex with ccw bypass
            else if (bypass == 2) {
              Location endLoc = enterHex.getHexsideLocation(enterHex.getCounterClockwiseHexside(enterHex
                  .getOppositeHexside(direction)));
              ;

              // bypass allowed?
              if (endLoc.isBypassAllowed()) {

                return new MovementResult(loc, startHex.getHexsideLocation(direction), enterHex
                    .getHexsideLocation(enterHex.getOppositeHexside(direction)), endLoc, direction, enterHex
                    .getOppositeHexside(direction), true, "", prevMovementResults);
              }
              else {
                return new MovementResult(null, null, null, null, MovementResult.NO_HEXSIDE_CROSSED,
                    MovementResult.NO_HEXSIDE_CROSSED, false, "Bypass of " + endLoc.getName() + " is not allowed",
                    prevMovementResults);
              }
            }

            // no bypass
            else {
              return new MovementResult(loc, null, enterHex.getHexsideLocation(enterHex.getOppositeHexside(direction)),
                  enterHex.getCenterLocation(), direction, enterHex.getOppositeHexside(direction), true, "",
                  prevMovementResults);
            }
          }
        }
        else if (direction == startHex.getClockwiseHexside(currentHexside)
            || direction == startHex.getCounterClockwiseHexside(currentHexside)) {

          enterHex = getAdjacentHex(startHex, direction);
          if (enterHex == null) {

            return new MovementResult(null, null, null, null, MovementResult.NO_HEXSIDE_CROSSED,
                MovementResult.NO_HEXSIDE_CROSSED, false, "Destination hex is off of the map", prevMovementResults);
          }
          else {

            // enter new hex with cw bypass
            if (bypass == 1) {

              Location endLoc = enterHex.getHexsideLocation(enterHex.getClockwiseHexside(enterHex
                  .getOppositeHexside(direction)));
              ;

              // bypass allowed?
              if (endLoc.isBypassAllowed()) {

                return new MovementResult(loc, startHex.getHexsideLocation(direction), enterHex
                    .getHexsideLocation(enterHex.getOppositeHexside(direction)), endLoc, direction, enterHex
                    .getOppositeHexside(direction), true, "", prevMovementResults);
              }
              else {
                return new MovementResult(null, null, null, null, MovementResult.NO_HEXSIDE_CROSSED,
                    MovementResult.NO_HEXSIDE_CROSSED, false, "Bypass of " + endLoc.getName() + " is not allowed",
                    prevMovementResults);
              }
            }

            // enter new hex with ccw bypass
            else if (bypass == 2) {
              Location endLoc = enterHex.getHexsideLocation(enterHex.getCounterClockwiseHexside(enterHex
                  .getOppositeHexside(direction)));
              ;

              // bypass allowed?
              if (endLoc.isBypassAllowed()) {

                return new MovementResult(loc, startHex.getHexsideLocation(direction), enterHex
                    .getHexsideLocation(enterHex.getOppositeHexside(direction)), endLoc, direction, enterHex
                    .getOppositeHexside(direction), true, "", prevMovementResults);
              }
              else {
                return new MovementResult(null, null, null, null, MovementResult.NO_HEXSIDE_CROSSED,
                    MovementResult.NO_HEXSIDE_CROSSED, false, "Bypass of " + endLoc.getName() + " is not allowed",
                    prevMovementResults);
              }
            }

            // no bypass?
            else {
              return new MovementResult(loc, startHex.getHexsideLocation(direction), enterHex
                  .getHexsideLocation(enterHex.getOppositeHexside(direction)), enterHex.getCenterLocation(), direction,
                  enterHex.getOppositeHexside(direction), true, "", prevMovementResults);
            }
          }
        }

        // moving into center of current hex?
        else if (direction == startHex.getOppositeHexside(currentHexside) && bypass == 0) {

          return new MovementResult(loc, null, null, startHex.getCenterLocation(), MovementResult.NO_HEXSIDE_CROSSED,
              MovementResult.NO_HEXSIDE_CROSSED, true, "", prevMovementResults);
        }

        // continue bypass clockwise
        else if ((direction == startHex.getOppositeHexside(currentHexside) && bypass == 1)
            || (direction == startHex.getOppositeHexside(startHex.getCounterClockwiseHexside(currentHexside)) && bypass == 0)) {

          return new MovementResult(loc, null, null, startHex.getHexsideLocation(startHex
              .getClockwiseHexside(currentHexside)), MovementResult.NO_HEXSIDE_CROSSED,
              MovementResult.NO_HEXSIDE_CROSSED, true, "", prevMovementResults);
        }

        // continue bypass counter clockwise
        else if ((direction == startHex.getOppositeHexside(currentHexside) && bypass == 2)
            || (direction == startHex.getOppositeHexside(startHex.getClockwiseHexside(currentHexside)) && bypass == 0)) {

          return new MovementResult(loc, null, null, startHex.getHexsideLocation(startHex
              .getCounterClockwiseHexside(currentHexside)), MovementResult.NO_HEXSIDE_CROSSED,
              MovementResult.NO_HEXSIDE_CROSSED, true, "", prevMovementResults);
        }

        else {
          return new MovementResult(null, null, null, null, MovementResult.NO_HEXSIDE_CROSSED,
              MovementResult.NO_HEXSIDE_CROSSED, false, "Cannot move that direction from the current bypass location",
              prevMovementResults);
        }
      }
      else {

        // get new hex and see if it is off map
        enterHex = getAdjacentHex(startHex, direction);
        if (enterHex == null) {

          return new MovementResult(null, null, null, null, MovementResult.NO_HEXSIDE_CROSSED,
              MovementResult.NO_HEXSIDE_CROSSED, false, "Destination hex is off of the map", prevMovementResults);
        }

        // entering bypass?
        if (bypass != 0) {

          Location endLoc = null;

          if (bypass == 1) {

            endLoc = enterHex.getHexsideLocation(enterHex.getClockwiseHexside(enterHex.getOppositeHexside(direction)));

            // bypass allowed?
            if (endLoc.isBypassAllowed()) {

              return new MovementResult(loc, startHex.getHexsideLocation(direction), enterHex
                  .getHexsideLocation(enterHex.getOppositeHexside(direction)), endLoc, direction, enterHex
                  .getOppositeHexside(direction), true, "", prevMovementResults);
            }
            else {
              return new MovementResult(null, null, null, null, MovementResult.NO_HEXSIDE_CROSSED,
                  MovementResult.NO_HEXSIDE_CROSSED, false, "Bypass of " + endLoc.getName() + " is not allowed",
                  prevMovementResults);
            }
          }
          else if (bypass == 2) {

            endLoc = enterHex.getHexsideLocation(enterHex.getCounterClockwiseHexside(enterHex
                .getOppositeHexside(direction)));

            // bypass allowed?
            if (endLoc.isBypassAllowed()) {

              return new MovementResult(loc, startHex.getHexsideLocation(direction), enterHex
                  .getHexsideLocation(enterHex.getOppositeHexside(direction)), endLoc, direction, enterHex
                  .getOppositeHexside(direction), true, "", prevMovementResults);
            }
            else {
              return new MovementResult(null, null, null, null, MovementResult.NO_HEXSIDE_CROSSED,
                  MovementResult.NO_HEXSIDE_CROSSED, false, "Bypass of " + endLoc.getName() + " is not allowed",
                  prevMovementResults);
            }
          }

          else {
            return new MovementResult(null, null, null, null, MovementResult.NO_HEXSIDE_CROSSED,
                MovementResult.NO_HEXSIDE_CROSSED, false, "Invalid bypass option", prevMovementResults);
          }
        }
        else {

          // if moving into bridge hex via road, bridge, move onto bridge
          if (enterHex.hasBridge()
              && (startHex.getHexsideLocation(direction).getTerrain().isRoadTerrain() || loc.getTerrain().isBridge())) {

            return new MovementResult(loc, startHex.getHexsideLocation(direction), enterHex.getHexsideLocation(enterHex
                .getOppositeHexside(direction)), enterHex.getBridge().getLocation(), direction, enterHex
                .getOppositeHexside(direction), true, "", prevMovementResults);
          }
          else {

            return new MovementResult(loc, startHex.getHexsideLocation(direction), enterHex.getHexsideLocation(enterHex
                .getOppositeHexside(direction)), enterHex.getCenterLocation(), direction, enterHex
                .getOppositeHexside(direction), true, "", prevMovementResults);
          }
        }
      }
    }

    // up
    else if (direction == 6) {

      // up location exists?
      if (loc.getUpLocation() != null) {

        return new MovementResult(loc, null, null, loc.getUpLocation(), MovementResult.NO_HEXSIDE_CROSSED,
            MovementResult.NO_HEXSIDE_CROSSED, true, null, prevMovementResults);
      }
      else {

        return new MovementResult(null, null, null, null, MovementResult.NO_HEXSIDE_CROSSED,
            MovementResult.NO_HEXSIDE_CROSSED, false, "There is no location above to move into", prevMovementResults);
      }
    }

    // down
    else if (direction == 7) {

      // down location exists?
      if (loc.getDownLocation() != null) {

        return new MovementResult(loc, null, null, loc.getDownLocation(), MovementResult.NO_HEXSIDE_CROSSED,
            MovementResult.NO_HEXSIDE_CROSSED, true, null, prevMovementResults);
      }
      else {

        return new MovementResult(null, null, null, null, MovementResult.NO_HEXSIDE_CROSSED,
            MovementResult.NO_HEXSIDE_CROSSED, false, "There is no location below to move into", prevMovementResults);
      }
    }

    // bad direction
    return new MovementResult(null, null, null, null, MovementResult.NO_HEXSIDE_CROSSED,
        MovementResult.NO_HEXSIDE_CROSSED, false, "Invalid direction (" + direction + ")", prevMovementResults);
  }

  /**
   * Adds a smoke object to the map.
   * 
   * @param s
   *          new smoke object
   */
  public void addSmoke(Smoke s) {

    smokeList.add(s);
  }

  /**
   * Removes all smoke from the given location.
   * 
   * @param l
   *          location
   */
  public void removeSmoke(Location l) {

    Smoke s = null;

    // step through all locations
    Iterator iter = smokeList.iterator();
    while (iter.hasNext()) {

      s = (Smoke) iter.next();

      if (s.getLocation() == l) {

        iter.remove();
      }
    }
  }

  /**
   * Removes all smoke from the given hex.
   * 
   * @param h
   *          hex
   */
  public void removeSmoke(Hex h) {

    Smoke s = null;

    // step through all locations
    Iterator iter = smokeList.iterator();
    while (iter.hasNext()) {

      s = (Smoke) iter.next();

      if (s.getLocation().getHex() == h) {

        iter.remove();
      }
    }
  }

  /**
   * Removes an individual smoke object from the map.
   * 
   * @param s
   *          smoke object
   */
  public void removeSmoke(Smoke s) {

    Smoke sl = null;

    // step through all smoke objects
    Iterator iter = smokeList.iterator();
    while (iter.hasNext()) {

      sl = (Smoke) iter.next();

      if (sl == s) {

        iter.remove();
      }
    }
  }

  /**
   * Removes all smoke from the map.
   */
  public void removeAllSmoke() {

    // step through all locations
    Iterator iter = smokeList.iterator();
    while (iter.hasNext()) {

      Smoke s = (Smoke) iter.next();
      iter.remove();
    }
  }

  /**
   * Returns the set of all smoke objects currently on the map.
   * 
   * @return HashSet containing all smoke objects
   */
  public HashSet getAllSmoke() {

    return smokeList;
  }

  /**
   * Returns the set of all smoke objects in a hex.
   * 
   * @return HashSet containing all smoke objects
   */
  public HashSet<Smoke> getAllSmoke(Hex h) {

    // step through all smoke locations
    HashSet<Smoke> allSmoke = new HashSet<Smoke>(smokeList.size());
    Iterator<Smoke> iter = smokeList.iterator();
    Smoke sl = null;
    while (iter.hasNext()) {

      sl = (Smoke) iter.next();

      if (sl.getLocation().getHex() == h) {
        allSmoke.add(sl);
      }
    }

    return allSmoke;
  }

  /**
   * Adds a prefab building to the map.
   * 
   * @parameter building new prefab building
   */
  public void addPrefabBuilding(PrefabBuilding building) {

    prefabBuildingList.add(building);
  }

  /**
   * Removes a prefab building from the map.
   * 
   * @parameter building prefab building to remove
   */
  public void removePrefabBuilding(PrefabBuilding building) {

    prefabBuildingList.remove(building);
  }

  /**
   * Removes all prefab buildings that "touch" the center of a hex.
   * 
   * @parameter h hex
   */
  public void removePrefabBuilding(Hex h) {

    HashSet temp = new HashSet(5);

    if (prefabBuildingList.size() > 0) {

      // find the prefab buildings to remove
      Iterator iter = prefabBuildingList.iterator();
      while (iter.hasNext()) {

        PrefabBuilding building = (PrefabBuilding) iter.next();
        Rectangle buildingArea = building.getShape().getBounds();

        if (buildingArea.contains(h.getCenterLocation().getLOSPoint())) {

          temp.add(building);
        }
      }

      // remove them
      iter = temp.iterator();
      while (iter.hasNext()) {

        prefabBuildingList.remove(iter.next());
      }
    }
  }

  /**
   * Maps all terrain from one type to another in the whole map.
   * 
   * @parameter fromTerrain original terrain to replace
   * @parameter toTerrain new terrain type
   */
  public boolean changeAllTerrain(Terrain fromTerrain, Terrain toTerrain) {

    return changeAllTerrain(fromTerrain, toTerrain, new Rectangle(0, 0, gridWidth, gridHeight));
  }

  /**
   * Maps all terrain from one type to another within a given shape.
   * 
   * @parameter fromTerrain original terrain to replace
   * @parameter toTerrain new terrain type
   * @parameter s area of the map to change
   */
  public boolean changeAllTerrain(Terrain fromTerrain, Terrain toTerrain, Shape s) {

    char fromTerrainType = (char) fromTerrain.getType();
    char toTerrainType = (char) toTerrain.getType();

    boolean changed = false;

    // change the map grid
    for (int i = 0; i < gridWidth; i++) {
      for (int j = 0; j < gridHeight; j++) {
        if (terrainGrid[i][j] == fromTerrainType && s.contains((double) i, (double) j)) {

          terrainGrid[i][j] = toTerrainType;
          changed = true;
        }
      }
    }

    // change the hex grid
    for (int col = 0; col < width; col++) {
      for (int row = 0; row < height + (col % 2); row++) {

        if (hexGrid[col][row].getHexBorder().intersects(s.getBounds())) {

          hexGrid[col][row].changeAllTerrain(fromTerrain, toTerrain, s);
          changed = true;
        }
      }
    }

    return changed;
  }

  /**
   * Maps all ground level elevation from one level to another in the whole map.
   * 
   * @parameter fromElevation original ground level elevation to replace
   * @parameter toTerrain new ground level elevation
   */
  public boolean changeAllGroundLevel(int fromElevation, int toElevation) {

    return changeAllGroundLevel(fromElevation, toElevation, new Rectangle(0, 0, gridWidth, gridHeight));
  }

  /**
   * Maps all ground level elevation from one level to another within a given
   * shape.
   * 
   * @parameter fromElevation original ground level elevation to replace
   * @parameter toTerrain new ground level elevation
   */
  public boolean changeAllGroundLevel(int fromElevation, int toElevation, Shape s) {

    boolean changed = false;

    // change the map grid
    for (int i = 0; i < gridWidth; i++) {
      for (int j = 0; j < gridHeight; j++) {
        if (elevationGrid[i][j] == (byte) fromElevation && s.contains((double) i, (double) j)) {

          elevationGrid[i][j] = (byte) toElevation;
          changed = true;
        }
      }
    }

    // change the base height of the hex grid
    for (int col = 0; col < width; col++) {
      for (int row = 0; row < height + (col % 2); row++) {

        if (hexGrid[col][row].getBaseHeight() == fromElevation
            && hexGrid[col][row].getHexBorder().intersects(s.getBounds())) {

          hexGrid[col][row].setBaseHeight(toElevation);
          changed = true;
        }
      }
    }
    return changed;
  }

  /**
   * Resets the hexside and depression terrain in all hexes to what is in the
   * terrain grid.
   */
  public void resetTerrain() {

    Hex h = null;
    Location l = null;
    Terrain t = null;
    for (int x = 0; x < hexGrid.length; x++) {
      for (int y = 0; y < hexGrid[x].length; y++) {

        h = hexGrid[x][y];

        // set the center location for depression terrain
        t = terrainList[(getGridTerrain((int) h.getCenterLocation().getEdgeCenterPoint().getX(), (int) h
            .getCenterLocation().getEdgeCenterPoint().getY())).getType()];
        if (t.isDepressionTerrain()) {

          h.setDepressionTerrain(t);
        }

        // set each hexside
        for (int z = 0; z < 6; z++) {

          if (h.isHexsideOnMap(z)) {

            // this hexside on the map?
            l = h.getHexsideLocation(z);
            try {
              t = terrainList[(getGridTerrain((int) l.getEdgeCenterPoint().getX(), (int) l.getEdgeCenterPoint().getY()))
                  .getType()];

              if (t.isEdgeTerrain()) {

                h.setEdgeTerrain(z, t);
              }
              else if (t.isDepressionTerrain()) {

                l.setDepressionTerrain(t);
              }

              // if adjacent to hexside terrain, make it the same
              Hex h2 = getAdjacentHex(h, z);
              if (h2 != null && h2.getEdgeTerrain(h.getOppositeHexside(z)) != null) {

                h.setEdgeTerrain(z, h2.getEdgeTerrain(h.getOppositeHexside(z)));
              }

            }
            catch (Exception e) {

              System.err.println(l.getName() + ": ");
              System.err.println((int) l.getEdgeCenterPoint().getX() + ", ");
              System.err.println((int) l.getEdgeCenterPoint().getY() + "");
            }
          }
        }
      }
    }
  }

  /**
   * Converts buildings to the correct type when converting a VASL image.
   * 
   * @parameter buildings correct building types for upper level buildings
   */
  public void convertMultiLevelBuildings(VASLMultilevelBuildings buildings) {

    Hex h = null;
    Location l = null;
    Terrain t = null;
    for (int x = 0; x < hexGrid.length; x++) {
      for (int y = 0; y < hexGrid[x].length; y++) {

        h = hexGrid[x][y];

        // building need to be changed?
        t = buildings.getTerrain(h.getName());
        if (t != null) {

          Polygon b = h.getExtendedHexBorder();
          Rectangle r = b.getBounds();

          // convert the terrain
          for (int i = (int) r.getX(); i < (int) r.getX() + (int) r.getWidth() - 1; i++) {
            for (int j = (int) r.getY(); j < (int) r.getY() + (int) r.getHeight() - 1; j++) {

              if (b.contains(i, j) && onMap(i, j) && terrainList[(int) terrainGrid[i][j]].isBuildingTerrain()) {

                terrainGrid[i][j] = (char) t.getType();
                setHexTerrain(new Rectangle(i, j, 1, 1), t);
              }
            }
          }
        }
      }
    }
  }

  /**
   * Rotates the map 180 degrees. Should only be used for geomorphic mapboards
   */
  public void flip() {

    char tchar;
    byte tbyte;
    Location loc1, loc2;
    Hex h1, h2;
    Point p;

    // flip the terrain and elevation grids
    for (int x = 0; x < gridWidth / 2; x++) {
      for (int y = 0; y < gridHeight; y++) {

        tchar = terrainGrid[x][y];
        terrainGrid[x][y] = terrainGrid[gridWidth - x - 1][gridHeight - y - 1];
        terrainGrid[gridWidth - x - 1][gridHeight - y - 1] = tchar;

        tbyte = elevationGrid[x][y];
        elevationGrid[x][y] = elevationGrid[gridWidth - x - 1][gridHeight - y - 1];
        elevationGrid[gridWidth - x - 1][gridHeight - y - 1] = tbyte;
      }
    }

    // flip the hex grid
    for (int x = 0; x < hexGrid.length / 2 + 1; x++) {
      for (int y = 0; y < (x == hexGrid.length / 2 ? (hexGrid[x].length - 1) / 2 + 1 : hexGrid[x].length); y++) {

        // get the next two hexes
        h1 = hexGrid[x][y];
        h2 = hexGrid[width - x - 1][hexGrid[width - x - 1].length - y - 1];

        // swap the hexes in the grid
        hexGrid[x][y] = h2;
        hexGrid[width - x - 1][hexGrid[width - x - 1].length - y - 1] = h1;

        // flip the hexes themselves
        h1.flip();
        h2.flip();

        // swap the column/row numbers
        int temp = h1.getColumnNumber();
        h1.setColumnNumber(h2.getColumnNumber());
        h2.setColumnNumber(temp);

        temp = h1.getRowNumber();
        h1.setRowNumber(h2.getRowNumber());
        h2.setRowNumber(temp);

        // swap the hex polygons
        Polygon poly = h1.getHexBorder();
        h1.setHexBorder(h2.getHexBorder());
        h2.setHexBorder(poly);

        poly = h1.getExtendedHexBorder();
        h1.setExtendedHexBorder(h2.getExtendedHexBorder());
        h2.setExtendedHexBorder(poly);
      }
    }

    // flip all of the prefab buildings
    Iterator i = prefabBuildingList.iterator();
    while (i.hasNext()) {

      PrefabBuilding b = (PrefabBuilding) i.next();
      b.setCenter(gridWidth - b.getCenterX() - 1, gridHeight - b.getCenterY() - 1);
      b.setRotation(b.getRotation() >= 180 ? b.getRotation() - 180 : b.getRotation() + 180);
    }
  }

  /**
   * This method is intended to be used only to copy geomorphic maps into a
   * larger map "grid" for VASL. As such, 1) it is assumed the half hex along
   * board edges are compatable, and 2) the hex/location names from the map that
   * is being inserted should be used. Other uses will produce unexpected
   * results.
   */
  public boolean insertGEOMap(GameMap insertMap, Hex upperLeft) {

    // determine where the upper-left pixel of the inserted map will be
    int left = upperLeft.getCenterLocation().getLOSPoint().x;
    int upper = upperLeft.getCenterLocation().getLOSPoint().y - (int) Hex.HEIGHT / 2;

    // ensure the map will fit
    if (!onMap(left, upper)) {

      JOptionPane.showMessageDialog(null, "The left-most corner is off the map", "Map placement error",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }

    // ensure the map will fit
    if (left + insertMap.getImageWidth() > this.gridWidth || upper + insertMap.getImageHeight() > this.gridHeight) {

      JOptionPane.showMessageDialog(null, "The map is too big to place at " + upperLeft.getName(),
          "Map placement error", JOptionPane.ERROR_MESSAGE);
      return false;
    }

    // copy the terrain and elevation grids
    for (int x = 0; x < insertMap.gridWidth; x++) {
      for (int y = 0; y < insertMap.gridHeight; y++) {

        terrainGrid[left + x][upper + y] = (char) insertMap.getGridTerrain(x, y).getType();
        elevationGrid[left + x][upper + y] = (byte) insertMap.getGridGroundLevel(x, y);
      }
    }

    // copy the hex grid
    int hexRow = upperLeft.getRowNumber();
    int hexCol = upperLeft.getColumnNumber();
    for (int x = 0; x < insertMap.hexGrid.length; x++) {
      for (int y = 0; y < insertMap.hexGrid[x].length; y++) {

        hexGrid[x + hexCol][y + hexRow].copy(insertMap.getHex(x, y));

      }
    }

    // add the pre-fab buildings???

    return true;
  }

  /**
   * Determines if a location is on one of the hexsides for a hex.
   * 
   * @parameter h the hex
   * @parameter l the hexside location
   */
  public boolean isAdjacentHexside(Hex h, Location l) {

    // ignore center locations
    if (l.isCenterLocation()) {

      return false;
    }
    // same hex?
    else if (l.getHex() == h) {

      return true;
    }

    // opposite hexside?
    for (int x = 0; x < 6; x++) {

      // get the adjacent hex for this hexside
      Hex h2 = getAdjacentHex(h, x);
      if (h2 != null) {

        // adjacent to this hexside?
        if (h2.getHexsideLocation(h2.getOppositeHexside(x)) == l) {

          return true;
        }
      }
    }

    return false;
  }

  /**
   * Determines if a location is one of the hexspines for a hex.
   * 
   * @parameter h the hex
   * @parameter l the hexside location
   */
  public boolean isHexspine(Hex h, Location l) {

    // check each hexspine
    for (int x = 0; x < 6; x++) {

      // get the adjacent hex for this hexside
      Hex h2 = getAdjacentHex(h, x);
      if (h2 != null) {

        // hexspine location?
        if (x == 0 && (h2.getHexsideLocation(2) == l || h2.getHexsideLocation(4) == l)) return true;
        if (x == 1 && (h2.getHexsideLocation(3) == l || h2.getHexsideLocation(5) == l)) return true;
        if (x == 2 && (h2.getHexsideLocation(4) == l || h2.getHexsideLocation(0) == l)) return true;
        if (x == 3 && (h2.getHexsideLocation(5) == l || h2.getHexsideLocation(1) == l)) return true;
        if (x == 4 && (h2.getHexsideLocation(0) == l || h2.getHexsideLocation(2) == l)) return true;
        if (x == 5 && (h2.getHexsideLocation(1) == l || h2.getHexsideLocation(3) == l)) return true;
      }
    }

    return false;
  }

  /**
   * Determines if a hexside terrain can be ignored for a hex.
   * 
   * @parameter h the hex. LOS assumed to start or end in this hex
   * @parameter l the hexside location
   * @parameter LOSHexspine hexspine the LOS enters/leaves hex h. -1 if LOS not
   *            on hexspine.
   */
  public boolean isIgnorableHexsideTerrain(Hex h, Location l, int LOSHexspine, int x, int y) {

    // some useful variables
    Hex locationHex = l.getHex();
    int locationHexside = locationHex.getLocationHexside(l);
    Terrain locationHexsideTerrain = locationHex.getEdgeTerrain(locationHexside);

    // too far away?
    if (range(h, locationHex) > 2) {

      return false;
    }
    // always ignore if adjacent
    if (isAdjacentHexside(h, l)) {

      return true;
    }
    // ignore hexspines if not bocage
    if (isHexspine(h, l) && locationHexsideTerrain != null && locationHexsideTerrain.getType() != Terrain.BOCAGE) {

      return true;
    }
    // ignore any hexside terrain that spilled into an adjacent hex
    if (locationHexsideTerrain == null) {

      return true;
    }

    // for LOS along a hexspine, check hexside terrain at the far end of the
    // hexspine
    if (LOSHexspine >= 0) {

      // for locations that are 2 hexes away, let's use the corresponding
      // location in the adjacent hex
      if (range(h, locationHex) == 2) {

        // find the hex across the location hexside
        Hex oppositeHex = getAdjacentHex(locationHex, locationHexside);

        if (oppositeHex != null && range(h, oppositeHex) > 1) {

          return false;
        }

        // change the location values
        locationHex = oppositeHex;
        locationHexside = oppositeHex.getOppositeHexside(locationHexside);
        l = locationHex.getHexsideLocation(locationHexside);
      }

      // The following code check the hex/hexsides listing in this table:
      //
      //	LOS Hexside Hexspine Hexside 1 Hexside 2
      //	----------- -------- --------- ---------
      //	0 0, 4 5, 0 0, 5
      //	1 1, 5 0, 1 1, 0
      //	2 2, 0 1, 2 2, 1
      //	3 3, 1 2, 3 3, 2
      //	4 4, 2 3, 4 4, 3
      //	5 5, 3 4, 5 5, 4
      //
      //	where x is the adjacent hex, y is the hexside

      int hexside = LOSHexspine == 0 ? 5 : LOSHexspine - 1;
      int hexspine = LOSHexspine < 2 ? LOSHexspine + 4 : LOSHexspine - 2;
      Hex hex1 = getAdjacentHex(h, hexside);
      Hex hex2 = getAdjacentHex(h, LOSHexspine);
      Location l1 = hex2.getHexsideLocation(hexspine);
      Location l2 = hex1.getHexsideLocation(LOSHexspine);
      Location l3 = hex2.getHexsideLocation(hexside);
      Terrain t1 = hex2.getEdgeTerrain(hexspine);
      Terrain t2 = hex1.getEdgeTerrain(LOSHexspine);
      Terrain t3 = hex2.getEdgeTerrain(hexside);

      if (t1 != null && (l == l2 || l == l3) && (t2 == null || t3 == null)) {

        return true;
      }
    }
    return false;
  }

}

