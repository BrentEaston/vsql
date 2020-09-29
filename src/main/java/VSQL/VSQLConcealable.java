/*
 * $Id: VSQLConcealable.java 5884 2009-08-01 10:26:30Z swampwallaby $
 *
 * Copyright (c) 2005 by Rodney Kinney, Brent Easton
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import VASL.counters.Concealable;
import VASL.counters.Concealment;
import VASL.counters.MarkMoved;
import VASSAL.build.GameModule;
import VASSAL.configure.ColorConfigurer;
import VASSAL.counters.AreaOfEffect;
import VASSAL.counters.BasicPiece;
import VASSAL.counters.Clone;
import VASSAL.counters.Decorator;
import VASSAL.counters.Delete;
import VASSAL.counters.GamePiece;
import VASSAL.counters.KeyCommand;
import VASSAL.counters.Labeler;
import VASSAL.counters.Marker;
import VASSAL.counters.Properties;

/**
 *  
 */
public class VSQLConcealable extends Concealable {

  public VSQLConcealable() {
    super();
  }

  public VSQLConcealable(String type, GamePiece inner) {
    super(type, inner);
  }

  /*
   * @return a new GamePiece that is a concealment counter appropriate for this
   *         unit that is indistinguishable from a standard ? counter.
   */
  public GamePiece createConcealment() {

    resetPLCNationality();
    
    boolean large = boundingBox().height > 52;
    String counterName = "? (" + (large ? "lg" : "sm") + ")";
    
    GamePiece p = new BasicPiece(BasicPiece.ID + ";;" + imageName + ";" + counterName);
    p = new Marker (Marker.ID + "Level", p);
    ((Marker) p).mySetState("Ground");
    p = new Delete(Delete.ID + "Delete;D", p);
    p = new Clone(Clone.ID + "Clone;K", p);
    p = new VSQLConcealment(Concealment.ID + GameModule.getUserId() + ";" + nation, p);
    p = new Labeler(Labeler.ID + "L;Change Label;10;0,0,0;255,255,255;t;0;c;0;b;c;$pieceName$ ($label$)", p);
    p = new VSQLHideable("hide;H;HIP;"+getHiddenColor(), p);
    p = new VSQLConcealable(Concealable.ID + "C;" + imageName + ";" + nation, p);
    p = new MarkMoved(MarkMoved.ID + (large ? "moved58" : "moved"), p);
    p = new Marker (Marker.ID + VSQLProperties.SIDE, p);
    ((Marker) p).mySetState(getSide(nation));
    p = new AreaOfEffect(AreaOfEffect.ID+"128,128,128;30;1;true;Show Area;;Night;false;NVR;", p);
    p.setProperty(Properties.OBSCURED_TO_OTHERS, new Boolean(true));
    p.setProperty(Properties.OBSCURED_BY, GameModule.getUserId());

    return p;
  }
  
  
  protected String getHiddenColor() {
    Color c = Color.WHITE;
    if (nation.equals("ge")) {
      c = ColorConfigurer.stringToColor("194,246,255");
    }
    else if (nation.equals("ru")) {
      c = ColorConfigurer.stringToColor("206,156,74");
    }
    else if (nation.equals("am")) {
      c = ColorConfigurer.stringToColor("181,222,90");
    }
    else if (nation.equals("ax")) {
      c = ColorConfigurer.stringToColor("156,206,156");
    }
    else if (nation.equals("pl")) {
      c = ColorConfigurer.stringToColor("239,239,170");
    }
    else {
      c = (Color) GameModule.getGameModule().getPrefs().getValue(nation);
    }

    return ColorConfigurer.colorToString(c);
  }

  public Color getColor(String nation) {
    Color c = Color.WHITE;
    if (nation.equals("ge")) {
      c = ColorConfigurer.stringToColor(VSQLProperties.GERMAN_COLOR);
    }
    else if (nation.equals("ru")) {
      c = ColorConfigurer.stringToColor(VSQLProperties.RUSSIAN_COLOR);
    }
    else if (nation.equals("am")) {
      c = ColorConfigurer.stringToColor(VSQLProperties.AMERICAN_COLOR);
    }
    else if (nation.equals("ax") || nation.equals("al")) {
      c = ColorConfigurer.stringToColor(VSQLProperties.NEUTRAL_COLOR);
    }
    else if (nation.equals("pl")) {
      c = ColorConfigurer.stringToColor(VSQLProperties.PLC_COLOR);
    }
    else if (nation.equals("br")) {
      c = ColorConfigurer.stringToColor(VSQLProperties.BRITISH_COLOR);
    }
    else if (nation.equals("fr")) {
      c = ColorConfigurer.stringToColor(VSQLProperties.FRENCH_COLOR);
    }
    else {
      c = super.getColor(nation);
    }

    return c;
  }
  
  public String getSide(String prefix) {
    String side = VSQLProperties.NEUTRAL;
    
    if (prefix.equals("ge")) {
      side = VSQLProperties.AXIS;
    }
    else if (prefix.equals("ru")) {
      side = VSQLProperties.ALLIED;
    }
    else if (prefix.equals("am")) {
      side = VSQLProperties.ALLIED;
    }
    else if (prefix.equals("ax")) {
      side = VSQLProperties.AXIS;
    }
    else if (prefix.equals("al")) {
      side = VSQLProperties.ALLIED;
    }
    else if (prefix.equals("pl")) {
      side = VSQLProperties.ALLIED;
    }
    else if (prefix.equals("br")) {
      side = VSQLProperties.ALLIED;
    }
    else if (prefix.equals("fr")) {
      side = VSQLProperties.ALLIED;
    }
    return side;
  }
  
  public Object getProperty(Object key) {
    if (((String) key).equals(VSQLProperties.NATION) && "true".equals(Decorator.getOutermost(this).getProperty(VSQLProperties.PLC))) {
      return getSide(getPrefix(nation));
    }
    else {
      return super.getProperty(key);
    }      
  }
  
  protected void resetPLCNationality() {
    GamePiece outer = Decorator.getOutermost(this);
    if ("true".equals(outer.getProperty(VSQLProperties.PLC))) {
      String s = Decorator.getOutermost(this).getProperty(VSQLProperties.PLC_NATIONALITY) + "";
      nation = getPrefix(s);
      imageName = nation + "-conceal";
    }
  }
  
  protected String getPrefix(String nation) {
    String prefix = "pl";
    if (VSQLProperties.GERMAN.equals(nation)) {
      prefix = "ge";
    }
    else if (VSQLProperties.AMERICAN.equals(nation)) {
      prefix = "am";
    }
    else if (VSQLProperties.RUSSIAN.equals(nation)) {
      prefix = "ru";
    }
    else if (VSQLProperties.AXIS_MINOR.equals(nation)) {
      prefix = "ax";
    }
    else if (VSQLProperties.ALLIED_MINOR.equals(nation)) {
      prefix = "al";
    }
    else if (VSQLProperties.BRITISH.equals(nation)) {
      prefix = "br";
    }
    else if (VSQLProperties.FRENCH.equals(nation)) {
      prefix = "fr";
    } 
    return prefix;
  }

  /*
   * If this unit is obscured to Me,then make sure I can't see any Control commands from
   * inner pieces
   * For compatibility vsql 2.5.8 to  3.0
   */
  
  public KeyCommand[] getKeyCommands() {
    KeyCommand[] commands = super.getKeyCommands();
    if (obscuredToMe() && commands.length > 0) {
      commands = new KeyCommand[] { commands[0] };
    }
    return commands;
  }

  /*
   * Don't draw the little question mark on concealment counters.
   * From vsql 3.0. Concealment counters are now concealable also
   */
  protected void drawObscuredToOthers(Graphics g, int x, int y, Component obs, double zoom) {
    
    boolean isConcealment = (Decorator.getDecorator(Decorator.getOutermost(this), VSQLConcealment.class) != null
        || Decorator.getDecorator(Decorator.getOutermost(this), Concealment.class) != null);
    
    if (isConcealment) {
      loadImages(obs);
      piece.draw(g, x, y, obs, zoom);      
    }
    else {
      super.drawObscuredToOthers(g, x, y, obs, zoom);
    }
    
  }
  
  /*
   * Ensure Hidden counters have no name
   */
  public String getName() {
    if (isInvisibleToOthers()) {
      return "";
    }
    else {
      return super.getName();
    }
  }

  protected boolean isInvisibleToOthers() {
    Boolean oto = (Boolean) Decorator.getOutermost(this).getProperty(Properties.INVISIBLE_TO_OTHERS);
    return (oto != null && oto.booleanValue());
  }
}
