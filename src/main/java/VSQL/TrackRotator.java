/*
 * $Id: TrackRotator.java 5546 2009-04-27 13:21:39Z swampwallaby $
 *
 * Copyright (c) 2000-2005 by Rodney Kinney, Brent Easton
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

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import VASSAL.build.GameModule;
import VASSAL.build.module.Map;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.build.module.map.Drawable;
import VASSAL.command.ChangeTracker;
import VASSAL.command.Command;
import VASSAL.configure.BooleanConfigurer;
import VASSAL.configure.IntConfigurer;
import VASSAL.counters.Decorator;
import VASSAL.counters.EditablePiece;
import VASSAL.counters.GamePiece;
import VASSAL.counters.KeyCommand;
import VASSAL.counters.KeySpecifier;
import VASSAL.counters.PieceEditor;
import VASSAL.counters.PieceImage;
import VASSAL.tools.SequenceEncoder;
import VASSAL.tools.imageop.GamePieceOp;
import VASSAL.tools.imageop.ImageOp;
import VASSAL.tools.imageop.Op;

/**
 * A Decorator that rotates a GamePiece to an arbitrary angle
 * No longer used in current VSQL, maintained for compatibility
 */
@Deprecated public class TrackRotator extends Decorator implements EditablePiece, MouseListener, MouseMotionListener, Drawable {
  public static final String ID = "trotate;";

  private KeyCommand setAngleCommand;
  private KeyCommand rotateCWCommand;
  private KeyCommand rotateCCWCommand;
  private KeyCommand[] commands;
  private char setAngleKey = 'R';
  private String setAngleText = "Rotate";
  private char rotateCWKey = ']';
  private String rotateCWText = "Rotate CW";
  private char rotateCCWKey = '[';
  private String rotateCCWText = "Rotate CCW";

  private double[] validAngles = new double[]{0.0};
  private int angleIndex = 0;

  private HashMap<Double, Rectangle> bounds = new HashMap<Double, Rectangle>();
  private GamePieceOp unrotated;

  private double tempAngle, startAngle;
  private boolean drawGhost;

  public TrackRotator() {
    this(ID + "6;];[", null);
  }

  public TrackRotator(String type, GamePiece inner) {
    mySetType(type);
    setInner(inner);
  }

  public String getName() {
    return piece.getName();
  }

  public void setInner(GamePiece p) {
    unrotated = Op.piece(p);
    super.setInner(p);
  }

  public Rectangle boundingBox() {
    if (getAngle() == 0.0) {
      return piece.boundingBox();
    }
    else {
      return AffineTransform.getRotateInstance(-Math.PI * getAngle() / 180.0).createTransformedShape(piece.boundingBox()).getBounds();
    }
  }

  public double getAngle() {
    if (getMap() == null) {
      return 0.0;
    }
    return validAngles[angleIndex];
  }

  public void setAngle(double angle) {
    if (validAngles.length == 1) {
      validAngles[angleIndex] = angle;
    }
    else {
      // Find nearest valid angle
      int newIndex = angleIndex;
      double minDist = Math.abs(validAngles[angleIndex]-angle);
      for (int i=0;i<validAngles.length;++i) {
        if (minDist > Math.abs(validAngles[i]-angle)) {
          newIndex = i;
          minDist = Math.abs(validAngles[i]-angle);
        }
      }
      angleIndex = newIndex;
    }
  }

  public Rectangle getRotatedBounds() {
    if (unrotated.isChanged()) {
      bounds.clear();
    }

    return bounds.computeIfAbsent(getAngle(), k -> piece.boundingBox());
  }

  public Shape getShape() {
    if (getAngle() == 0.0) {
      return piece.getShape();
    }
    else  {
      return AffineTransform.getRotateInstance(getAngleInRadians()).createTransformedShape(piece.getShape());
    }
  }

  public double getAngleInRadians() {
    return -Math.PI * getAngle() / 180.0;
  }

  public void mySetType(String type) {
    type = type.substring(ID.length());
    SequenceEncoder.Decoder st = new SequenceEncoder.Decoder(type, ';');
    validAngles = new double[Integer.parseInt(st.nextToken())];
    for (int i = 0; i < validAngles.length; ++i) {
      validAngles[i] = -i * (360.0 / validAngles.length);
    }
    if (validAngles.length == 1) {
      setAngleKey = st.nextChar('\0');
      if (st.hasMoreTokens()) {
        setAngleText = st.nextToken();
      }
    }
    else {
      String s = st.nextToken();
      rotateCWKey = s.length() > 0 ? s.charAt(0) : '\0';
      s = st.nextToken();
      rotateCCWKey = s.length() > 0 ? s.charAt(0) : '\0';
      if (st.hasMoreTokens()) {
        rotateCWText = st.nextToken();
        rotateCCWText = st.nextToken();
      }
    }
    commands = null;
  }

  public void draw(final Graphics g, final int x, final int y, final Component obs, final double zoom) {
    if (getAngle() == 0.0) {
      piece.draw(g, x, y, obs, zoom);
    }
    else {
      final ImageOp op = Op.rotateScale(unrotated, getAngle(), zoom);
      final Rectangle r = getRotatedBounds();

      g.drawImage(op.getImage(),
                  x + (int) (zoom * r.x),
                  y + (int) (zoom * r.y), obs);
    }
  }

  public void draw(Graphics g, Map map) {
    if (drawGhost) {
      final Graphics2D g2d = (Graphics2D) g;
      final double os_scale = g2d.getDeviceConfiguration().getDefaultTransform().getScaleX();
      final double zoom = map.getZoom() * os_scale;

      Point p = map.mapToDrawing(getPosition(), os_scale);

      AffineTransform t = g2d.getTransform();
      g2d.transform(AffineTransform.getRotateInstance(-Math.PI * tempAngle / 180., p.x, p.y));
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5F));
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      piece.draw(g, p.x, p.y, map.getView(), zoom);
      g2d.setTransform(t);
    }
  }

  public String myGetType() {
    SequenceEncoder se = new SequenceEncoder(';');
    se.append("" + validAngles.length);
    if (validAngles.length == 1) {
      se.append("" + setAngleKey);
      se.append(setAngleText);
    }
    else {
      se.append(rotateCWKey == 0 ? "" : "" + rotateCWKey)
          .append(rotateCCWKey == 0 ? "" : "" + rotateCCWKey)
          .append(rotateCWText)
          .append(rotateCCWText);
    }
    return ID + se.getValue();
  }

  public String myGetState() {
    if (validAngles.length == 1) {
      return "" + validAngles[0];
    }
    else {
      return "" + angleIndex;
    }
  }

  public void mySetState(String state) {
    if (validAngles.length == 1) {
      validAngles[0] = Double.valueOf(state).doubleValue();
    }
    else {
      angleIndex = Integer.parseInt(state);
    }
  }

  public KeyCommand[] myGetKeyCommands() {
    if (commands == null) {
      setAngleCommand = new KeyCommand
          (setAngleText,
           KeyStroke.getKeyStroke(setAngleKey,
                                  java.awt.event.InputEvent.CTRL_DOWN_MASK),
           Decorator.getOutermost(this));
      rotateCWCommand = new KeyCommand
          (rotateCWText,
           KeyStroke.getKeyStroke(rotateCWKey,
                                  java.awt.event.InputEvent.CTRL_DOWN_MASK),
           Decorator.getOutermost(this));

      rotateCCWCommand = new KeyCommand
          (rotateCCWText,
           KeyStroke.getKeyStroke(rotateCCWKey,
                                  java.awt.event.InputEvent.CTRL_DOWN_MASK),
           Decorator.getOutermost(this));

      if (validAngles.length == 1) {
        if (setAngleText.length() > 0) {
          commands = new KeyCommand[]{setAngleCommand};
        }
        else {
          commands = new KeyCommand[0];
          setAngleCommand.setEnabled(false);
        }
        rotateCWCommand.setEnabled(false);
        rotateCCWCommand.setEnabled(false);
      }
      else {
        if (rotateCWText.length() > 0
            && rotateCCWText.length() > 0) {
          commands = new KeyCommand[]{rotateCWCommand, rotateCCWCommand};
        }
        else if (rotateCWText.length() > 0) {
          commands = new KeyCommand[]{rotateCWCommand};
          rotateCCWCommand.setEnabled(false);
        }
        else if (rotateCCWText.length() > 0) {
          commands = new KeyCommand[]{rotateCCWCommand};
          rotateCWCommand.setEnabled(false);
        }
        setAngleCommand.setEnabled(false);
      }
    }
    setAngleCommand.setEnabled(getMap() != null && validAngles.length == 1 && setAngleText.length() > 0);
    return commands;
  }

  public Command myKeyEvent(KeyStroke stroke) {
    myGetKeyCommands();
    Command c = null;
    if (setAngleCommand.matches(stroke)) {
      getMap().pushMouseListener(this);
      getMap().addDrawComponent(this);
      getMap().getView().addMouseMotionListener(this);
      getMap().getView().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }
    else if (rotateCWCommand.matches(stroke)) {
      ChangeTracker tracker = new ChangeTracker(this);
      angleIndex = (angleIndex + 1) % validAngles.length;
      c = tracker.getChangeCommand();
    }
    else if (rotateCCWCommand.matches(stroke)) {
      ChangeTracker tracker = new ChangeTracker(this);
      angleIndex = (angleIndex - 1 + validAngles.length) % validAngles.length;
      c = tracker.getChangeCommand();
    }
    return c;
  }

  public void mouseClicked(MouseEvent e) {
  }

  public void mouseEntered(MouseEvent e) {
  }

  public void mouseExited(MouseEvent e) {
  }

  public void mousePressed(MouseEvent e) {
    drawGhost = true;
    startAngle = getRelativeAngle(e.getPoint(),getPosition());
  }

  public void mouseReleased(MouseEvent e) {
    try {
      ChangeTracker tracker = new ChangeTracker(this);
      setAngle(tempAngle);
      GameModule.getGameModule().sendAndLog(tracker.getChangeCommand());
    }
    finally {
      getMap().getView().setCursor(null);
      getMap().removeDrawComponent(this);
      getMap().popMouseListener();
      getMap().getView().removeMouseMotionListener(this);
    }
  }

  public void mouseDragged(MouseEvent e) {
    if (drawGhost) {
      Point mousePos = getMap().componentToMap(e.getPoint());
      Point origin = getPosition();
      double myAngle = getRelativeAngle(mousePos, origin);
      tempAngle = getAngle() + -180. * (myAngle-startAngle) / Math.PI;
    }
    getMap().repaint();
  }

  private double getRelativeAngle(Point p, Point origin) {
    double myAngle;
    if (p.y == origin.y) {
      myAngle = p.x < origin.x ? Math.PI / 2 : -Math.PI / 2;
    }
    else {
      myAngle = Math.atan((float) (p.x - origin.x) / (float) (origin.y - p.y));
      if (origin.y < p.y) {
        myAngle += Math.PI;
      }
    }
    return myAngle;
  }

  public void mouseMoved(MouseEvent e) {
  }

  public String getDescription() {
    return "Can Rotate";
  }

  public VASSAL.build.module.documentation.HelpFile getHelpFile() {
    File dir = VASSAL.build.module.Documentation.getDocumentationBaseDir();
    dir = new File(dir, "ReferenceManual");
    try {
      return new HelpFile(null, new File(dir, "Rotate.htm"));
    }
    catch (MalformedURLException ex) {
      return null;
    }
  }

  public PieceEditor getEditor() {
    return new Ed(this);
  }

  private static class Ed implements PieceEditor,
      java.beans.PropertyChangeListener {
    private BooleanConfigurer anyConfig;
    private KeySpecifier anyKeyConfig;
    private IntConfigurer facingsConfig;
    private KeySpecifier cwKeyConfig;
    private KeySpecifier ccwKeyConfig;
    private JTextField anyCommand;
    private JTextField cwCommand;
    private JTextField ccwCommand;
    private Box anyControls;
    private Box cwControls;
    private Box ccwControls;

    private JPanel panel;

    public Ed(TrackRotator p) {
      cwKeyConfig = new KeySpecifier(p.rotateCWKey);
      ccwKeyConfig = new KeySpecifier(p.rotateCCWKey);
      anyConfig = new BooleanConfigurer
          (null, "Allow arbitrary rotations",
           p.validAngles.length == 1);
      anyKeyConfig = new KeySpecifier(p.setAngleKey);
      facingsConfig = new IntConfigurer
          (null, "Number of allowed facings :",
           p.validAngles.length == 1 ? 6 : p.validAngles.length);

      panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

      panel.add(facingsConfig.getControls());
      cwControls = Box.createHorizontalBox();
      cwControls.add(new JLabel("Command to rotate clockwise:"));
      cwControls.add(cwKeyConfig);
      cwControls.add(new JLabel("Menu text:"));
      cwCommand = new JTextField(12);
      cwCommand.setMaximumSize(cwCommand.getPreferredSize());
      cwCommand.setText(p.rotateCWText);
      cwControls.add(cwCommand);
      panel.add(cwControls);

      ccwControls = Box.createHorizontalBox();
      ccwControls.add(new JLabel("Command to rotate counterclockwise:"));
      ccwControls.add(ccwKeyConfig);
      ccwControls.add(new JLabel("Menu text:"));
      ccwCommand = new JTextField(12);
      ccwCommand.setMaximumSize(ccwCommand.getPreferredSize());
      ccwCommand.setText(p.rotateCCWText);
      ccwControls.add(ccwCommand);
      panel.add(ccwControls);

      panel.add(anyConfig.getControls());
      anyControls = Box.createHorizontalBox();
      anyControls.add(new JLabel("Command to rotate :"));
      anyControls.add(anyKeyConfig);
      anyControls.add(new JLabel("Menu text:"));
      anyCommand = new JTextField(12);
      anyCommand.setMaximumSize(anyCommand.getPreferredSize());
      anyCommand.setText(p.setAngleText);
      anyControls.add(anyCommand);
      panel.add(anyControls);

      anyConfig.addPropertyChangeListener(this);
      propertyChange(null);
    }

    public void propertyChange(java.beans.PropertyChangeEvent evt) {
      boolean any = Boolean.TRUE.equals(anyConfig.getValue());
      anyControls.setVisible(any);
      facingsConfig.getControls().setVisible(!any);
      cwControls.setVisible(!any);
      ccwControls.setVisible(!any);
      panel.revalidate();
    }

    public Component getControls() {
      return panel;
    }

    public String getType() {
      SequenceEncoder se = new SequenceEncoder(';');
      if (Boolean.TRUE.equals(anyConfig.getValue())) {
        se.append("1");
        se.append(anyKeyConfig.getKey());
        se.append(anyCommand.getText() == null ?
                  "" : anyCommand.getText().trim());
      }
      else {
        se.append(facingsConfig.getValueString());
        se.append(cwKeyConfig.getKey());
        se.append(ccwKeyConfig.getKey());
        se.append(cwCommand.getText() == null ?
                  "" : cwCommand.getText().trim());
        se.append(ccwCommand.getText() == null ?
                  "" : ccwCommand.getText().trim());
      }
      return ID + se.getValue();
    }

    public String getState() {
      return "0";
    }
  }

  /* (non-Javadoc)
   * @see VASSAL.build.module.map.Drawable#drawAboveCounters()
   */
  public boolean drawAboveCounters() {
    // TODO Auto-generated method stub
    return false;
  }
}
