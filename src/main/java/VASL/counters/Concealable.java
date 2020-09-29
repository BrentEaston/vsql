/*
 * $Id: Concealable.java 5884 2009-08-01 10:26:30Z swampwallaby $
 * 
 * Copyright (c) 2000-2003 by Rodney Kinney
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
package VASL.counters;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import VASSAL.build.GameModule;
import VASSAL.command.ChangePiece;
import VASSAL.command.Command;
import VASSAL.command.NullCommand;
import VASSAL.configure.ColorConfigurer;
import VASSAL.counters.BasicPiece;
import VASSAL.counters.Decorator;
import VASSAL.counters.EditablePiece;
import VASSAL.counters.Embellishment;
import VASSAL.counters.GamePiece;
import VASSAL.counters.Hideable;
import VASSAL.counters.Obscurable;
import VASSAL.counters.PieceEditor;
import VASSAL.counters.Properties;
import VASSAL.counters.SimplePieceEditor;
import VASSAL.counters.Stack;
import VASSAL.tools.SequenceEncoder;
import VASSAL.tools.imageop.Op;

public class Concealable extends Obscurable implements EditablePiece {
  public static final String ID = "conceal;";

  protected String nation;
  protected String nation2;
  private Image concealedToMe;
  private Image concealedToOthers;
  private Color concealedToOthersColor;
  private Dimension imageSize = new Dimension(-1, -1);

  public Concealable() {
    this(ID + "C;Qmark58;ge", null);
  }

  public Concealable(String type, GamePiece inner) {
    super(type, inner);
    mySetType(type);
    hideCommand = "Conceal";
  }

  public void mySetType(String in) {
    SequenceEncoder.Decoder st = new SequenceEncoder.Decoder(in, ';');
    st.nextToken();
    obscureKey = st.nextToken().toUpperCase().charAt(0);
    imageName = st.nextToken();
    nation = st.nextToken();
    if (st.hasMoreTokens()) {
      nation2 = st.nextToken();
    }
  }

  public String myGetType() {
    String s = ID + obscureKey + ";" + imageName + ";" + nation;
    if (nation2 != null) {
      s += ";" + nation2;
    }
    return s;
  }

  protected void drawObscuredToMe(Graphics g, int x, int y, Component obs, double zoom) {
    loadImages(obs);
    int size = (int) (zoom * imageSize.width);
    g.setColor(getColor(nation));
    g.fillRect(x - size / 2, y - size / 2, size, size);
    if (nation2 != null) {
      g.setColor(getColor(nation2));
      g.fillRect(x - size / 2 + size / 8, y - size / 2 + size / 8, size - size / 4, size - size / 4);
    }
    g.drawImage(concealedToMe, x - size / 2, y - size / 2, size, size, obs);
  }

  protected void drawObscuredToOthers(Graphics g, int x, int y, Component obs, double zoom) {
    loadImages(obs);
    piece.draw(g, x, y, obs, zoom);
    int size = (int) (zoom * imageSize.width);
    try {
      g.drawImage(concealedToOthers, x - size / 2, y - size / 2, size, size, obs);
    }
    catch (Exception e) {
    }
  }

  public Command myKeyEvent(KeyStroke stroke) {
    myGetKeyCommands();
    Command c = null;
    if (hide.matches(stroke)
        && getMap() != null
        && !obscuredToOthers()
        && !obscuredToMe()) {
      c = super.myKeyEvent(stroke);
      boolean concealmentExists = false;
      GamePiece outer = Decorator.getOutermost(this);
      if (getParent() != null) {
        for (int i = getParent().indexOf(outer),j = getParent().getPieceCount(); i < j; ++i) {
          Concealment conceal = (Concealment) Decorator.getDecorator(getParent().getPieceAt(i), Concealment.class);
          if (conceal != null
              && conceal.canConceal(outer)) {
            concealmentExists = true;
            break;
          }
        }
      }
      if (!concealmentExists) {
        GamePiece concealOuter = createConcealment();
        Concealment conceal = (Concealment) Decorator.getDecorator(concealOuter, Concealment.class);
        c.append
            (getMap().getStackMetrics().merge
             (outer, concealOuter));
        for (int i = 0,j = getParent().indexOf(outer);
             i < j; ++i) {
          c.append(conceal.setConcealed(getParent().getPieceAt(i), true));
        }
      }
    }
    else {
      c = super.myKeyEvent(stroke);
    }
    return c;
  }

  public Command keyEvent(javax.swing.KeyStroke stroke) {
    if (!obscuredToMe()) {
      Stack parent = getParent();
      if (parent != null) {
        int lastIndex = getParent().indexOf(Decorator.getOutermost(this));
        Command c = super.keyEvent(stroke);
        if (getParent() != null) {
          int newIndex = getParent().indexOf(Decorator.getOutermost(this));
          if (newIndex != lastIndex) {
            c.append(adjustConcealment());
          }
        }
        return c;
      }
      else {
        return super.keyEvent(stroke);
      }
    }
    else {
      return myKeyEvent(stroke);
    }

  }

  /**
   * Conceal/unconceal this unit according to whether a concealment counter is
   * on top of it in a stack
   */
  public Command adjustConcealment() {
    if (isMaskable()) {
      GamePiece outer = Decorator.getOutermost(this);
      String state = outer.getState();
      setProperty(Properties.OBSCURED_BY, null);
      if (getParent() != null) {
        for (int i = getParent().indexOf(outer), j = getParent().getPieceCount(); i < j; ++i) {
          Concealment p = (Concealment) Decorator.getDecorator(getParent().getPieceAt(i), Concealment.class);
          if (p != null && p.canConceal(this)) {
            setProperty(Properties.OBSCURED_BY, GameModule.getUserId());
            break;
          }
        }
        getMap().repaint(getMap().boundingBoxOf(getParent()));
      }
      return outer.getState().equals(state) ? null : new ChangePiece(outer.getId(), state, outer.getState());
    }
    else {
      return null;
    }
  }

  /**
   * Conceal/unconceal units in this stack according to positions of concealment
   * counters in the stack
   */
  public static Command adjustConcealment(Stack s) {
    if (s == null || s.getMap() == null) {
      return null;
    }
    Command c = new NullCommand();
    for (int i = 0, j = s.getPieceCount(); i < j; ++i) {
      Concealable p = (Concealable) Decorator.getDecorator(s.getPieceAt(i), Concealable.class);
      if (p != null) {
        c = c.append(p.adjustConcealment());
      }
    }
    s.getMap().repaint(s.getMap().boundingBoxOf(s));
    return c;
  }

  /**
   * @return a new GamePiece that is a concealment counter appropriate for this
   *         unit
   */
  public GamePiece createConcealment() {
    GamePiece p = new BasicPiece(BasicPiece.ID + "K;D;" + imageName + ";?");
    boolean large = imageName.indexOf("58") > 0;
    if (!imageName.startsWith(nation)) { // Backward compatibility with generic
                                         // concealment markers
      large = imageName.substring(0, 1).toUpperCase().equals(imageName.substring(0, 1));
      String size = large ? "60;60" : "48;48";
      if (nation2 != null) {
        p = new ColoredBox(ColoredBox.ID + "ru" + ";" + size, p);
        p = new ColoredBox(ColoredBox.ID + "ge" + ";" + (large ? "48;48" : "36;36"), p);
      }
      else {
        p = new ColoredBox(ColoredBox.ID + nation + ";" + size, p);
      }
      p = new Embellishment(Embellishment.ID + ";;;;;;0;0;" + imageName + ",?", p);
    }
    p = new Concealment(Concealment.ID + GameModule.getUserId() + ";" + nation, p);
    p = new MarkMoved(MarkMoved.ID + (large ? "moved58" : "moved"), p);
    Color c = (Color) GameModule.getGameModule().getPrefs().getValue(nation);
    if (c == null) c = Color.WHITE;
    p = new Hideable("hide;H;HIP;"+ColorConfigurer.colorToString(c), p);
    return p;
  }

  public Rectangle boundingBox() {
    return piece.boundingBox();
  }

  public Shape getShape() {
    return piece.getShape();
  }

  protected void loadImages(Component obs) {
    if (concealedToMe == null) {
      concealedToMe = Op.load(imageName + ".gif").getImage();
      if (concealedToMe != null) {
        JLabel l = new JLabel(new ImageIcon(concealedToMe));
        imageSize = l.getPreferredSize();
      }
      else {
        imageSize.setSize(0, 0);
      }
    }
    if (concealedToOthers == null || !getColor(nation).equals(concealedToOthersColor)) {
      concealedToOthersColor = getColor(nation);
      //try {
      //  concealedToOthers = GameModule.getGameModule().getDataArchive().getCachedImage(
      //      nation + "/" + nation + "qmarkme.gif");
      //}
      //catch (java.io.IOException ex) {
        // Using generic qmarkme.gif image and prefs-specified colors
        int size = imageSize.width;
        BufferedImage rev = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = rev.createGraphics();
        g.setColor(concealedToOthersColor);
        g.fillRect(0, 0, 24, 32);
        if (nation2 != null) {
          g.setColor(getColor(nation2));
          g.fillRect(6, 6, 18, 26);
        }
        Image generic = Op.load("qmarkme.gif").getImage();
        MediaTracker tracker = new MediaTracker(obs);
        tracker.addImage(generic, 0);
        try {
          tracker.waitForAll();
        }
        catch (InterruptedException e) {
        }
        g.drawImage(generic, 0, 0, obs);
        concealedToOthers = rev;
    }
  }

  public String getName() {
    if (obscuredToMe()) {
      return "?";
    }
    else if (obscuredToOthers()) {
      return piece.getName() + "(?)";
    }
    else {
      return piece.getName();
    }
  }

  public Object getProperty(Object key) {
    if (ASLProperties.HINDRANCE.equals(key) && obscuredToMe()) {
      return null;
    }
    else if (ASLProperties.NATIONALITY.equals(key)) {
      return nation;
    }
    else {
      return super.getProperty(key);
    }
  }

  public Color getColor(String s) {
    return (Color) GameModule.getGameModule().getPrefs().getValue(s);
  }

  public String getDescription() {
    return "Can be concealed";
  }

  public VASSAL.build.module.documentation.HelpFile getHelpFile() {
    return null;
  }

  public PieceEditor getEditor() {
    return new SimplePieceEditor(this);
  }
}
