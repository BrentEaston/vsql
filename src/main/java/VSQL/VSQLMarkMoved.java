/*
 * $Id: VSQLMarkMoved.java 5546 2009-04-27 13:21:39Z swampwallaby $
 *
 * Copyright (c) 2000-2005 by Rodney Kinney and Brent Easton
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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.KeyStroke;

import VASSAL.command.ChangeTracker;
import VASSAL.command.Command;
import VASSAL.counters.Decorator;
import VASSAL.counters.EditablePiece;
import VASSAL.counters.GamePiece;
import VASSAL.counters.KeyCommand;
import VASSAL.counters.PieceEditor;
import VASSAL.counters.Properties;
import VASSAL.counters.SimplePieceEditor;
import VASSAL.tools.SequenceEncoder;
import VASSAL.tools.imageop.ImageOp;
import VASSAL.tools.imageop.Op;

/**
 * Allows a piece to be marked as having moved
 */
public class VSQLMarkMoved extends Decorator implements EditablePiece {
  public static final String ID = "moved;";

  public static final KeyStroke markStroke = KeyStroke.getKeyStroke('M', java.awt.event.InputEvent.CTRL_DOWN_MASK);
  private String markImage;
  private boolean hasMoved = false;

  public VSQLMarkMoved() {
    this(ID + "moved", null);
  }

  public VSQLMarkMoved(String type, GamePiece p) {
    mySetType(type);
    setInner(p);
  }

  public boolean isMoved() {
    return hasMoved;
  }

  public void setMoved(boolean b) {
    hasMoved = b;
  }

  public Object getProperty(Object key) {
    if (Properties.MOVED.equals(key)) {
      return isMoved();
    }
    else {
      return super.getProperty(key);
    }
  }

  public void setProperty(Object key, Object val) {
    if (Properties.MOVED.equals(key)) {
      setMoved(Boolean.TRUE.equals(val));
      super.setProperty(key, val);  // Pass onto Footprint
    }
    else {
      super.setProperty(key, val);
    }
  }

  public void mySetType(String type) {
    SequenceEncoder.Decoder st = new SequenceEncoder.Decoder(type, ';');
    st.nextToken();
    markImage = st.nextToken();
  }

  public void mySetState(String newState) {
    hasMoved = "true".equals(newState);
  }

  public String myGetState() {
    return "" + hasMoved;
  }

  public String myGetType() {
    return ID + markImage;
  }

  protected KeyCommand[] myGetKeyCommands() {
    return new KeyCommand[]{new KeyCommand("Mark Moved", markStroke, Decorator.getOutermost(this))};
  }

  public Command myKeyEvent(javax.swing.KeyStroke stroke) {
    if (stroke.equals(markStroke)) {
      ChangeTracker c = new ChangeTracker(this);
      Decorator.getOutermost(this).setProperty(Properties.MOVED, !hasMoved);
      //hasMoved = !hasMoved;
      return c.getChangeCommand();
    }
    else {
      return null;
    }
  }

  public Shape getShape() {
    return piece.getShape();
  }

  public Rectangle boundingBox() {
    Rectangle r = piece.boundingBox();
    Rectangle r2 = piece.getShape().getBounds();
    r2.width += 20;
    return r.union(r2);
  }

  public String getName() {
    return piece.getName();
  }

  public void draw(Graphics g, int x, int y, Component obs, double zoom) {
    piece.draw(g, x, y, obs, zoom);
    if (hasMoved) {
      Rectangle r = piece.getShape().getBounds();
      ImageOp op = Op.load(markImage + ".gif");
      if (zoom != 1.0) {
        op = Op.scale(op, zoom);
      }      
      g.drawImage(op.getImage(),
                  x + (int) (zoom * (r.x + r.width)),
                  y + (int) (zoom * r.y),obs);
    }
  }

  public String getDescription() {
    return "Can be marked moved";
  }

  public VASSAL.build.module.documentation.HelpFile getHelpFile() {
    return null;
  }

  public PieceEditor getEditor() {
    return new SimplePieceEditor(this);
  }
}
