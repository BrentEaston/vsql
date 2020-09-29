/*
 * $Id: VSQLEmbellishment.java 920 2006-06-05 13:02:15Z swampwallaby $
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

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.KeyStroke;

import VASSAL.command.Command;
import VASSAL.counters.Embellishment;
import VASSAL.counters.GamePiece;

public class VSQLEmbellishment extends Embellishment {

  protected static final String CA_PREFIX = "+ CA = ";
  protected boolean first = true;

  public VSQLEmbellishment() {
    super();
  }

  public VSQLEmbellishment(String type, GamePiece d) {
    super(type, d);
  }

  public VSQLEmbellishment(Embellishment e) {
    this();
    mySetType(e.myGetType());
    mySetState(e.myGetState());
  }
  
  public String[] getImageNames() {
    return imageName;
  }
  
  /**
   * Record the CA each time we rotate a vehicle
   */
  public Command myKeyEvent(KeyStroke stroke) {

    int oldValue = value;

    Command c = super.myKeyEvent(stroke);

    String name = commonName[Math.abs(value) - 1];
    if (value != oldValue && name != null && name.startsWith(CA_PREFIX)) {
      String ca = name.substring(CA_PREFIX.length());
      setProperty(VSQLProperties.VEHICLE_CA, ca);
    }
    return c;
  }

  /**
   * Set the initial CA the first time a vehicle is drawn. Required because
   * SL vehicles have a different initial CA to COI vehicles.
   */
  public void draw(Graphics g, int x, int y, Component obs, double zoom) {

    super.draw(g, x, y, obs, zoom);

    if (first) {
      first = false;
      String name = commonName[Math.abs(value) - 1];
      if (name != null && name.startsWith(CA_PREFIX)) {
        String ca = name.substring(CA_PREFIX.length());
        setProperty(VSQLProperties.VEHICLE_CA, ca);
      }
    }
  }
}