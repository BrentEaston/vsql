/*
 * $Id: VSQLPieceMover.java 5546 2009-04-27 13:21:39Z swampwallaby $
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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import VASL.build.module.map.ASLPieceMover;
import VASL.counters.ASLProperties;
import VASL.counters.Concealable;
import VASL.counters.Concealment;
import VASSAL.build.module.Map;
import VASSAL.command.Command;
import VASSAL.counters.Decorator;
import VASSAL.counters.DragBuffer;
import VASSAL.counters.GamePiece;
import VASSAL.counters.PieceIterator;
import VASSAL.counters.Stack;

public class VSQLPieceMover extends ASLPieceMover {

  public VSQLPieceMover() {
    super();
  }

  /**
   * Notify any leaders of the number of squads stacked with them
   */
  protected void processStack(Stack stack) {

    ArrayList<GamePiece> leaders = new ArrayList<GamePiece>(3);
    int squadCount = 0;

    Iterator<GamePiece> i = stack.getPiecesIterator();
    while (i.hasNext()) {
      GamePiece piece = i.next();
    
      String type = (String) piece.getProperty(VSQLProperties.UNIT_TYPE);
      if (type != null && type.equals(VSQLProperties.INFANTRY)) {
        String subType = (String) piece.getProperty(VSQLProperties.UNIT_SUB_TYPE);
        if (subType != null) {
          if (subType.equals(VSQLProperties.LEADER)) {
            leaders.add(piece);
          }
          else if (subType.equals(VSQLProperties.SQUAD) || subType.equals(VSQLProperties.CREW)) {
            squadCount++;
          }
        }
      }
    }

    for (GamePiece leader : leaders) {
      leader.setProperty(VSQLProperties.STACKED_COUNT, squadCount + "");
    }
  }
  
  /**
   * In addition to moving pieces normally, we mark units that have moved
   * and adjust the concealment status of units
   */
  public Command movePieces(Map m, java.awt.Point p) {
    extractMovable();

    GamePiece movingConcealment = null;
    Stack formerParent = null;
    PieceIterator it = DragBuffer.getBuffer().getIterator();
    if (it.hasMoreElements()) {
      GamePiece moving = it.nextPiece();
      if (moving instanceof Stack) {
        Stack s = (Stack) moving;
        moving = s.topPiece();
        if (moving != s.bottomPiece()) {
          moving = null;
        }
      }
      if (Decorator.getDecorator(moving, Concealment.class) != null
          && !it.hasMoreElements()) {
        movingConcealment = moving;
        formerParent = movingConcealment.getParent();
      }
    }
    Command c = super.movePieces(m, p);
    if (c == null || c.isNull()) {
      return c;
    }
    if (movingConcealment != null) {
      if (movingConcealment.getParent() != null) {
        c.append(Concealable.adjustConcealment(movingConcealment.getParent()));
      }
      if (formerParent != null) {
        c.append(Concealable.adjustConcealment(formerParent));
      }
    } else 
    {
    	it = DragBuffer.getBuffer().getIterator();
        if (it.hasMoreElements()) {
        	GamePiece moving = it.nextPiece();
            if (moving instanceof Stack) {
            	c.append(Concealable.adjustConcealment((Stack) moving));
            } else {
            	Concealable con = (Concealable) Decorator.getDecorator(moving, Concealable.class);
            	if (con != null) {
            		c.append(con.adjustConcealment());
            	}
            }
        }
    }
    
    /* Reset stack count of leaders */
    
    PieceIterator it2 = DragBuffer.getBuffer().getIterator();

    while (it2.hasMoreElements()) {
      GamePiece piece = it2.nextPiece();
      if (piece instanceof Stack) {
        processStack((Stack) piece);
      }
      else {
        String subType = (String) piece.getProperty(VSQLProperties.UNIT_SUB_TYPE);
        if (subType != null && subType.equals(VSQLProperties.LEADER)) {
          piece.setProperty(VSQLProperties.STACKED_COUNT, "0");
        }
      }

    }
    
    return c;
  }
  
  /**
   * Remove all un-movable pieces from the DragBuffer.  Un-movable pieces
   * are those with the ASLProperties.LOCATION property set, or those that
   * are underneath another un-movable piece.
   */
  public void extractMovable() {
    Vector<GamePiece> movable = new Vector<GamePiece>();
    for (PieceIterator it = DragBuffer.getBuffer().getIterator();
         it.hasMoreElements();) {
      GamePiece p = it.nextPiece();
      if (p instanceof Stack) {
        Vector<GamePiece> toMove = new Vector<GamePiece>();
        int pos = 0;
        for (PieceIterator pi = new PieceIterator(((Stack) p).getPieces());
             pi.hasMoreElements();) {
          GamePiece p1 = pi.nextPiece();
          if (p1.getProperty(ASLProperties.LOCATION) == null && !isBelowNonMoveable((Stack) p, pos)) {
            toMove.addElement(p1);
          }
          pos++;		
        }
        if (toMove.size() == ((Stack) p).getPieceCount()
            || toMove.size() == 0) {
          movable.addElement(p);
        }
        else {
          for (int i = 0; i < toMove.size(); ++i) {
            movable.addElement(toMove.elementAt(i));
          }
        }
      }
      else {
        movable.addElement(p);
      }
    }
    DragBuffer.getBuffer().clear();
    for (Enumeration<GamePiece> e = movable.elements();
         e.hasMoreElements();) {
      DragBuffer.getBuffer().add((GamePiece) e.nextElement());
    }
  }
  
  protected boolean isBelowNonMoveable(Stack s, int pos) {
  	
  	if (pos >= s.getPieceCount()) {
  		return false;
  	}
  	
  	for (int i = pos+1; i < s.getPieceCount(); i++) {
  		GamePiece p = s.getPieceAt(i);
  		if (p.getProperty(ASLProperties.LOCATION) != null) {
  			return true;
  		}
  	}
  	
  	return false;
  }
}