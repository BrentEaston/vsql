/*
 * $Id: VSQLFootprint.java 5546 2009-04-27 13:21:39Z swampwallaby $
 * 
 * Copyright (c) 2005 by Brent Easton
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License (LGPL) as published by
 * the Free Software Foundation.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, copies are available at
 * http://www.opensource.org.
 */
package VSQL;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.InputEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import VASL.counters.MarkMoved;
import VASSAL.build.module.Map;
import VASSAL.command.ChangeTracker;
import VASSAL.command.Command;
import VASSAL.counters.Decorator;
import VASSAL.counters.GamePiece;
import VASSAL.counters.KeyCommand;
import VASSAL.counters.KeySpecifier;
import VASSAL.counters.PieceEditor;
import VASSAL.counters.Properties;
import VASSAL.tools.SequenceEncoder;
import VASSAL.tools.image.LabelUtils;
import VASSAL.tools.imageop.ImageOp;
import VASSAL.tools.imageop.Op;

public class VSQLFootprint extends MarkMoved {

  public static final String ID = "footprint;";

  protected static ImageOp[] caImages = new ImageOp[6];

  private char trailKey;
  private KeyCommand[] commands;
  private String command = "Movement Trail";
  protected boolean visible = false;
  protected int currentCA = 1;
  protected int offboardCA = 1;
  protected boolean leader = false;
  protected int stackedCount = 0;

  protected int minX, minY, maxX, maxY;
  protected Rectangle myBoundingBox;

  // Color, size and font settings for the trail itself
  protected static final int CIRCLE_RADIUS = 10;
  protected static final Color CIRCLE_COLOR = Color.BLACK;
  protected static final Color FILL_COLOR = Color.WHITE;
  protected static final Color LINE_COLOR = Color.BLACK;
  protected static final float SELECTED_LINE_WIDTH = 2.0f;
  protected static final float UNSELECTED_LINE_WIDTH = 1.0f;
  protected static final int BASE_FONT_SIZE = 14;

  // Buffer around map for all drawing
  protected static final int EDGE_CLIP_LIMIT = 30;

  // Buffer around map for drawing circles
  protected static final int EDGE_POINT_LIMIT = 20;

  protected Vector<Point> pointList = new Vector<Point>();

  /**
   * Class CAPoint, a Point with a CA
   */
  public class CAPoint extends Point {
    private static final long serialVersionUID = 1L;
    protected int ca = 1;

    public CAPoint(int x, int y, int c) {
      super(x, y);
      ca = c;
    }

    public CAPoint(Point p, int c) {
      this(p.x, p.y, c);
    }
  }

  public VSQLFootprint() {
    this(ID + "fp;", null);
  }

  public VSQLFootprint(String type, GamePiece p) {
    mySetType(type);
    setInner(p);
  }

  protected Enumeration<Point> getPointList() {
    return pointList.elements();
  }

  /**
   * State a count of points, followed by a list of the points. Visibility
   * status of the trail is local to each player
   */
  public void mySetState(String newState) {
    pointList.clear();
    SequenceEncoder.Decoder ss = new SequenceEncoder.Decoder(newState, ';');
    int items = ss.nextInt(0);
    for (int i = 0; i < items; i++) {
      String point = ss.nextToken("");
      if (point.length() != 0) {
        SequenceEncoder.Decoder sp = new SequenceEncoder.Decoder(point, ',');
        int x = sp.nextInt(0);
        int y = sp.nextInt(0);
        int ca = sp.nextInt(1);
        pointList.addElement(new CAPoint(x, y, ca));
      }
    }
    if (ss.hasMoreTokens()) {
      visible = (ss.nextToken("false").equals("true"));
    }
    else
      visible = false;
    
    if (ss.hasMoreTokens()) {
      leader = (ss.nextToken("false").equals("true"));
    }
    else
      leader = false;
    
    if (ss.hasMoreTokens()) {
      stackedCount = ss.nextInt(0);
    }
  }

  public String myGetState() {
    String s = pointList.size() + "";
    Enumeration<Point> e = getPointList();
    while (e.hasMoreElements()) {
      CAPoint p = (CAPoint) e.nextElement();
      s += ";" + p.x + "," + p.y + "," + p.ca;
    }
    s += ";" + visible;
    s += ";" + leader;
    s += ";" + stackedCount;

    return s;
  }

  /**
   * Type is the character command that toggles footprint visiblity
   */
  public void mySetType(String type) {
    SequenceEncoder.Decoder st = new SequenceEncoder.Decoder(type, ';');
    st.nextToken();
    trailKey = st.nextChar('T');
    commands = null;
  }

  public String myGetType() {
    return ID + trailKey;
  }

  /**
   * setMoved is called with an argument of true each time the piece is moved.
   * The argiment is false when the unit is marked as not moved.
   */
  public void setMoved(boolean justMoved) {

    if (justMoved) {
      Point where = this.getPosition();
      if (pointList.size() == 0) {
        addPoint(where);
      }
      else {
        Point last = (Point) pointList.lastElement();
        if (!last.equals(where)) { // Don't add the same point twice
          addPoint(where);
        }
      }
    }
    else {
      pointList.clear();
      myBoundingBox = null;
      visible = false;
    }
    redraw();
  }

  protected void addPoint(Point p) {
    
    /**
     * For a vehicle, record the CA
     * For a leader, record the number of squads that moved with it
     */
    int value = 0;
    if (isVehicle()) value = currentCA;
    if (leader) value = stackedCount;
    
    pointList.addElement(new CAPoint(p, value));
 
    getMyBoundingBox();

    if (p.x + CIRCLE_RADIUS > maxX) maxX = p.x + CIRCLE_RADIUS;
    if (p.x - CIRCLE_RADIUS < minX) minX = p.x - CIRCLE_RADIUS;
    if (p.y + CIRCLE_RADIUS > maxY) maxY = p.y + CIRCLE_RADIUS;
    if (p.y - CIRCLE_RADIUS < minY) minY = p.y - CIRCLE_RADIUS;

    myBoundingBox = new Rectangle(minX, minY, maxX - minX, maxY - minY);

  }
  /**
   * Is this counter a vehicle?
   */
  protected String counterType = null;

  public boolean isVehicle() {
    if (counterType == null) {
      counterType = (String) Decorator.getOutermost(this).getProperty(VSQLProperties.UNIT_TYPE);
      if (counterType == null) counterType = "Unknown";
    }
    return counterType.equals(VSQLProperties.VEHICLE);
  }

  public void redraw() {
    piece.getMap().repaint();
  }

  /**
   * Called by VSQLEmbellishment with the new CA whenever a CA Layer rotation
   * command is executed
   */
  public void setProperty(Object key, Object val) {
    if (Properties.MOVED.equals(key)) {
      setMoved(Boolean.TRUE.equals(val));
      piece.setProperty(key, val); // Pass on to MarkMoved
    }
    if (VSQLProperties.VEHICLE_CA.equals(key)) {
      try {
        currentCA = Integer.parseInt((String) val);
      }
      catch (Exception e) {

      }
    }
    else  if (VSQLProperties.STACKED_COUNT.equals(key)) {
      try {
        stackedCount = Integer.parseInt((String) val);
        ((CAPoint) pointList.lastElement()).ca = stackedCount;
        leader = true;
      }
      catch (Exception e) {

      }
    }
    else {
      super.setProperty(key, val);
    }
  }

  public String getDescription() {
    return "Movement trail";
  }

  public void draw(Graphics g, int x, int y, Component obs, double zoom) {

    int x1, y1, x2, y2;
    int lastValue = 0;

    piece.draw(g, x, y, obs, zoom);

    final Graphics2D g2d = (Graphics2D) g;
    final double os_scale = g2d.getDeviceConfiguration().getDefaultTransform().getScaleX();

    /*
     * If we are asked to be drawn at a different zoom from the current map zoom
     * setting, then don't draw the trail as it will be in the wrong place.
     * (i.e. Mouse-over viewer)
     */
    double mapZoom = zoom;
    if (this.getMap() != null) {
      mapZoom = this.getMap().getZoom() * os_scale;
    }

    if (visible && (zoom == mapZoom)) {
      Font f = new Font("Dialog", Font.PLAIN, (int) (BASE_FONT_SIZE * zoom));
      boolean selected = Boolean.TRUE.equals(getProperty(Properties.SELECTED));
      Composite oldComposite = g2d.getComposite();

      /**
       * newClip is an overall clipping region made up of the Map itself and a
       * border of 20 pixels. No drawing at all outside this area. mapRect is
       * made of the Map and a 10 pixel border. Circles are not drawn outside
       * this area.
       */
      int mapHeight = getMap().mapSize().height;
      int mapWidth = getMap().mapSize().width;
      int edgeHeight = Integer.parseInt(getMap().getAttributeValueString(Map.EDGE_HEIGHT));
      int edgeWidth = Integer.parseInt(getMap().getAttributeValueString(Map.EDGE_WIDTH));

      int edgeClipHeight = (edgeHeight < EDGE_CLIP_LIMIT) ? edgeHeight : EDGE_CLIP_LIMIT;
      int edgeClipWidth = (edgeWidth < EDGE_CLIP_LIMIT) ? edgeWidth : EDGE_CLIP_LIMIT;

      int clipX = edgeWidth - edgeClipWidth;
      int clipY = edgeHeight - edgeClipHeight;
      int width = mapWidth - (2 * edgeWidth) + 2 * edgeClipWidth;
      int height = mapHeight - (2 * edgeHeight) + 2 * edgeClipHeight;

      Rectangle newClip = new Rectangle((int) (clipX * zoom), (int) (clipY * zoom), (int) (width * zoom),
          (int) (height * zoom));
      Rectangle circleRect = new Rectangle(edgeWidth - EDGE_POINT_LIMIT, edgeHeight - EDGE_POINT_LIMIT, mapWidth + 2
          * EDGE_POINT_LIMIT, mapHeight + 2 * EDGE_POINT_LIMIT);
      Rectangle visibleRect = getMap().getView().getVisibleRect();

      Shape oldClip = g.getClip();
      g.setClip(newClip.intersection(visibleRect));

      float lineWidth = SELECTED_LINE_WIDTH;
      if (!selected) {         
         g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5F));
         lineWidth = UNSELECTED_LINE_WIDTH;
      }
      g2d.setStroke(new BasicStroke(lineWidth));
      g2d.setColor(LINE_COLOR);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      Enumeration<Point> e = getPointList();
      Point lastP = null;
      Point here = getPosition();
      while (e.hasMoreElements()) {
        CAPoint cap = (CAPoint) e.nextElement();
        Point p = cap.getLocation();

        if (lastP != null) {
          x1 = (int) (lastP.x * zoom);
          y1 = (int) (lastP.y * zoom);
          x2 = (int) (p.x * zoom);
          y2 = (int) (p.y * zoom);     
          
          double dist = getDistance(x1, y1, x2, y2);
          int xDiff = (int) ((CIRCLE_RADIUS * zoom * (x2 - x1)) / dist);
          int yDiff = (int) ((CIRCLE_RADIUS * zoom * (y2 - y1)) / dist);
          
          g.drawLine(x1+xDiff, y1+yDiff, x2-xDiff, y2-yDiff);
        }
        lastP = p;
      }
      if (lastP != null) {
        x1 = (int) (lastP.x * zoom);
        y1 = (int) (lastP.y * zoom);
        x2 = (int) (here.x * zoom);
        y2 = (int) (here.y * zoom);
        
        double dist = getDistance(x1, y1, x2, y2);
        int xDiff = (int) ((CIRCLE_RADIUS * zoom * (x2 - x1)) / dist);
        int yDiff = (int) ((CIRCLE_RADIUS * zoom * (y2 - y1)) / dist);
        
        g.drawLine(x1+xDiff, y1+yDiff, x2-xDiff, y2-yDiff);
      }

      int step = 0;
      e = getPointList();
      while (e.hasMoreElements()) {
        step += 1;
        CAPoint cap = (CAPoint) e.nextElement();
        Point p = cap.getLocation();

        if (circleRect.contains(p)) {
          x1 = (int) (p.x * zoom);
          y1 = (int) (p.y * zoom);
          x2 = (int) ((p.x - CIRCLE_RADIUS) * zoom);
          y2 = (int) ((p.y - CIRCLE_RADIUS) * zoom);
          int radius = (int) (CIRCLE_RADIUS * 2 * zoom);
          g.setColor(FILL_COLOR);
          g.fillOval(x2, y2, radius, radius);
          g.setColor(CIRCLE_COLOR);
          g.drawOval(x2, y2, radius, radius);
          
          /**
           * For a vehicled, draw an arrow showing the CA of the vehicle
           * as it entered the hex.
           */
          if (isVehicle() && lastValue > 0 && selected) {
            
            try {
              ImageOp im = getCAImage(lastValue);
              if (zoom == 1.0) {
                g.drawImage(im.getImage(), x2, y2, obs);
              }
              else {
                ImageOp scaled = Op.scale(im, zoom);
                g.drawImage(scaled.getImage(), x2, y2, obs);
              }
            }
            catch (Exception ex) {

            }
          }
          
          /**
           * For a leader counter, display the number of squads or crews
           * that moved into the hex stacked with the leader. (+1 for the leader)
           */
          else if (leader && lastValue > 0 && selected) {
           
            LabelUtils.drawLabel(g, lastValue + 1 + "", x1, y1, f, LabelUtils.CENTER,
               LabelUtils.CENTER, CIRCLE_COLOR, null, null);
            
          }
        }
        lastValue = cap.ca;
      }
      if (! selected) {
         g2d.setComposite(oldComposite);
      }
      g.setClip(oldClip);

    }
  }

  protected double getDistance(int x1, int y1, int x2, int y2) {
    double lastSqrt = -1;
    int lastDistSq = -1;
    
    int distSq = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
    if (distSq == lastDistSq) {
       return lastSqrt;
    }
    
    lastDistSq = distSq;
    lastSqrt = Math.sqrt(distSq);
    return lastSqrt;
    
  }
  
  protected ImageOp getCAImage(int ca) throws java.io.IOException {

    if (caImages[ca - 1] == null) {
      //caImages[ca - 1] = GameModule.getGameModule().getDataArchive().getCachedImage("ca_" + ca + ".gif");
      caImages[ca - 1] = Op.load("ca_" + ca + ".gif");
    }
    return caImages[ca - 1];

  }

  public Rectangle boundingBox() {
    if (visible) {
      return piece.boundingBox().union(getMyBoundingBox());
    }
    else
      return piece.boundingBox();
  }

  public Rectangle getMyBoundingBox() {
    if (myBoundingBox == null) {

      Point p = piece.getPosition();
      myBoundingBox = new Rectangle(p.x, p.y, 60, 60);
      minX = myBoundingBox.x;
      minY = myBoundingBox.y;
      maxX = myBoundingBox.x + myBoundingBox.width;
      maxY = myBoundingBox.y + myBoundingBox.height;
    }
    return myBoundingBox;
  }

  public Shape getShape() {
    return piece.getShape();
  }

  public String getName() {
    return piece.getName();
  }

  public KeyCommand[] myGetKeyCommands() {
    if (commands == null) {
      commands = new KeyCommand[1];
      commands[0] = new KeyCommand(command, KeyStroke.getKeyStroke(trailKey, InputEvent.CTRL_DOWN_MASK), Decorator
          .getOutermost(this));
      commands[0].setEnabled(getMap() != null);
    }
    return commands;
  }

  public Command myKeyEvent(KeyStroke stroke) {
    if (KeyStroke.getKeyStroke(trailKey, InputEvent.CTRL_DOWN_MASK) == stroke) {
      ChangeTracker tracker = new ChangeTracker(this);
      visible = !visible;
      redraw();
      return tracker.getChangeCommand();
      //GameModule.getGameModule().sendAndLog(c);
      //return c;
    }
    return null;
  }

  public PieceEditor getEditor() {
    return new Ed(this);
  }

  private static class Ed implements PieceEditor {
    private KeySpecifier trailKeyInput;
    private JPanel controls;

    public Ed(VSQLFootprint p) {
      controls = new JPanel();
      controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));

      Box b = Box.createHorizontalBox();
      trailKeyInput = new KeySpecifier('T');
      trailKeyInput.setKey(p.trailKey);
      b.add(new JLabel("Key command:  "));
      b.add(trailKeyInput);
      controls.add(b);

    }

    public String getState() {
      return "null";
    }

    public String getType() {
      return ID + trailKeyInput.getKey();
    }

    public Component getControls() {
      return controls;
    }
  }
}
