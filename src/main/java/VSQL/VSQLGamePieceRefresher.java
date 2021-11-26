/*
 * $Id: VSQLGamePieceRefresher.java 5546 2009-04-27 13:21:39Z swampwallaby $
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import VASL.counters.Turreted;
import VASSAL.build.AbstractConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.Map;
import VASSAL.build.module.PieceWindow;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.build.widget.PieceSlot;
import VASSAL.command.Command;
import VASSAL.counters.BasicPiece;
import VASSAL.counters.Decorator;
import VASSAL.counters.Embellishment;
import VASSAL.counters.GamePiece;
import VASSAL.counters.PieceCloner;
import VASSAL.counters.Replace;
import VASSAL.counters.Stack;
import VASSAL.tools.LaunchButton;
import VASSAL.tools.SequenceEncoder;

public class VSQLGamePieceRefresher extends AbstractConfigurable {

  protected LaunchButton launch;
  protected Map map;

  public static final String BUTTON_TEXT = "text";
  public static final String NAME = "name";

  public VSQLGamePieceRefresher() {
    ActionListener refreshAction = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        refresh();
      }
    };
    launch = new LaunchButton(null, BUTTON_TEXT, null, null, refreshAction);
  }

  public static String getConfigureTypeName() {
    return "Gamepiece Refresher";
  }

  public String[] getAttributeNames() {
    String s[] = {NAME, BUTTON_TEXT};
    return s;
  }

  public String[] getAttributeDescriptions() {
    return new String[] {"Name", "Button text"};
  }

  public Class<?>[] getAttributeTypes() {
    return new Class[] {String.class, String.class};
  }

  public void addTo(Buildable parent) {
    GameModule.getGameModule().getToolBar().add(getComponent());
  }

  /**
   * The component to be added to the control window toolbar
   */
  protected java.awt.Component getComponent() {
    return launch;
  }

  public void setAttribute(String key, Object o) {
    if (NAME.equals(key)) {
      setConfigureName((String) o);
      launch.setToolTipText((String) o);
    }
    else {
      launch.setAttribute(key, o);
    }
  }

  public String getAttributeValueString(String key) {
    if (NAME.equals(key)) {
      return getConfigureName();
    }
    else {
      return launch.getAttributeValueString(key);
    }
  }

  public Class<?>[] getAllowableConfigureComponents() {
    return new Class[0];
  }

  public void removeFrom(Buildable b) {
    GameModule.getGameModule().getToolBar().remove(getComponent());
    GameModule.getGameModule().getToolBar().revalidate();
  }

  public HelpFile getHelpFile() {
    return null;
  }

  protected void refresh() {

    // First, Find all maps with pieces
    HashMap mapList = new HashMap();
    Enumeration e = GameModule.getGameModule().getGameState().getPieces();
    while (e.hasMoreElements()) {
      GamePiece pieceOrStack = (GamePiece) e.nextElement();
      if (pieceOrStack instanceof Stack) {
        Enumeration se = ((Stack) pieceOrStack).getPieces();
        while (se.hasMoreElements()) {
          map = ((GamePiece) se.nextElement()).getMap();
          mapList.put(map, map);
        }
      }
      else {
        map = pieceOrStack.getMap();
        mapList.put(map, map);
      }
    }

    // Now process the pieces on each map
    Iterator maps = mapList.values().iterator();
    while (maps.hasNext()) {
      Map map = (Map) maps.next();
      GamePiece pieces[] = map.getPieces();
      for (int i = 0; i < pieces.length; i++) {
        GamePiece pieceOrStack = pieces[i];
        if (pieceOrStack.getMap() == null) {
          pieceOrStack.setMap(map);
        }
        if (pieceOrStack instanceof Stack) {
          Enumeration se = ((Stack) pieceOrStack).getPieces();
          while (se.hasMoreElements()) {
            processPiece((GamePiece) se.nextElement());
          }
        }
        else {
          processPiece(pieceOrStack);
        }
      }
    }
  }

  protected void processPiece(GamePiece oldPiece) {

    // Handle pre v4.0 auto-generated Concealment counters
    if ("?".equals(oldPiece.getProperty(BasicPiece.BASIC_NAME))) {
      upgradeConcealment(oldPiece);
    }
    else {
      findNewPiece(oldPiece);
    }

   // if (newPiece != null) {
     //  Map map = oldPiece.getMap();
      // Point pos = oldPiece.getPosition();
       //map.placeOrMerge(newPiece, pos);
      // new RemovePiece(Decorator.getOutermost(oldPiece)).execute();
   // }
  }

  protected void upgradeConcealment(GamePiece oldPiece) {

    boolean large = oldPiece.boundingBox().height > 52;
    String counterName = "? (" + (large ? "lg" : "sm") + ")";
    BasicPiece p = (BasicPiece) Decorator.getInnermost(oldPiece);
    String type = p.getType();
    SequenceEncoder.Decoder st = new SequenceEncoder.Decoder(type, ';');
    st.nextToken();
    char cloneKey = st.nextChar('\0');
    char deleteKey = st.nextChar('\0');
    String imageName = st.nextToken();
    SequenceEncoder se = new SequenceEncoder(cloneKey > 0 ? "" + cloneKey : "", ';');
    String newType = BasicPiece.ID + se.append(deleteKey > 0 ? "" + deleteKey : "").append(imageName).append(counterName).getValue();
    p.mySetType(newType);
    findNewPiece(oldPiece);
  }
  
  // Find a new Piece matching the oldpiece
  protected void findNewPiece(GamePiece oldPiece) {
    for (final PieceWindow pw: GameModule.getGameModule().getComponentsOf(PieceWindow.class)) {
      if (checkBuildable(oldPiece, pw)) {
        break;
      }
    }
  }

  // Check for piece in a PieceWindow widget
  protected boolean checkBuildable(GamePiece oldPiece, AbstractConfigurable b) {
    boolean done = false;
    b.getConfigureComponents(); // Force widgets to rebuild
    Enumeration pwComponents = b.getBuildComponents();
    while (pwComponents.hasMoreElements() && ! done) {
      AbstractConfigurable bb = (AbstractConfigurable) pwComponents.nextElement();
      if (bb instanceof PieceSlot) {
        GamePiece p = PieceCloner.getInstance().clonePiece(((PieceSlot) bb).getPiece());
        done = checkNewPiece(oldPiece, p);
      }
      else {
        done = checkBuildable(oldPiece, bb);
      }
    }
    return done;
  }

  // Compare old Piece with a piece on the pallette
  protected boolean checkNewPiece(GamePiece oldPiece, GamePiece pallettePiece) {

    String oldPieceName = Decorator.getInnermost(oldPiece).getName();
    String newPieceName = Decorator.getInnermost(pallettePiece).getName();

    // Same BasicPiece name?
    if (oldPieceName.equals(newPieceName)) {
      if (embellishmentMatch(oldPiece, pallettePiece)) {
        ReplaceTrait r = new ReplaceTrait(oldPiece, pallettePiece);      
        r.replacePiece();
        return true;
      }
    }

    return false;
  }

  /**
   * Compare the name of the 1st image of the innermost Embellishment of 2
   * Decorators.
   */
  protected boolean embellishmentMatch(GamePiece piece1, GamePiece piece2) {

    String imageName2 = innerEmbImageName(piece2);
    String imageName1 = innerEmbImageName(piece1);

    // Override the embellishment check for some pieces
    if ("DM".equals(piece1.getProperty(BasicPiece.BASIC_NAME))) {
      return true;
    }
    if ("info-Smoke".equals(imageName1) && "info-Smoke-hex.png".equals(imageName2)) {
      return true;
    }
    if ("info-Building Rubble".equals(imageName1) && "info-Building-Rubble-hex.png".equals(imageName2)) {
      return true;
    }
    if (imageName1 == null) {
      return imageName2 == null;
    }
    else {
      if (imageName2 == null) {
        return imageName1 == null;
      }
      else {
        return imageName1.equals(imageName2);
      }
    }
  }

  /*
   * Find the name of the first image name of the innermost Embellishment
   */

  protected String innerEmbImageName(GamePiece piece) {

    GamePiece p = piece;
    String name = null;

    while (p != null) {
      p = Decorator.getDecorator(p, Embellishment.class);
      if (p != null) {
        if (!(p instanceof Turreted)) {
          if (p instanceof VSQLEmbellishment) {
            name = ((VSQLEmbellishment) p).getImageNames()[0];
          }
          else {
            name = (new VSQLEmbellishment((Embellishment) p)).getImageNames()[0];
          }
        }
        p = ((Decorator) p).getInner();
      }
    }

    // Check if this is an old-style concealment counter
    if (name == null) {
      p = Decorator.getDecorator(piece, VSQLConcealment.class);
      if (p != null) {
        return getInnermostImage(piece);
      }
    }
    return name;

  }

  public String getInnermostImage(GamePiece piece) {
    GamePiece p = Decorator.getInnermost(piece);
    if (p != null) {
      BasicPiece bp = (BasicPiece) Decorator.getInnermost(piece);
      String type = bp.getType();
      SequenceEncoder.Decoder st = new SequenceEncoder.Decoder(type, ';');
      st.nextToken();
      st.nextToken();
      st.nextToken();
      return st.nextToken("");
    }
    return null;
  }

//  public void updateState(GamePiece newPiece, GamePiece oldPiece) {
//
//    GamePiece p = Decorator.getOutermost(newPiece);
//    String type;
//    String newState;
//
//    // Special case for old-style MayRout counters has been converted to a
//    // multi-level DM counter
//    String image = getInnermostImage(oldPiece);
//    if ("mayrout".equals(image)) {
//      VSQLEmbellishment e = (VSQLEmbellishment) Decorator.getDecorator(p, VSQLEmbellishment.class);
//      e.setValue(2);
//      return;
//    }
//    while (p != null && !(p instanceof BasicPiece)) {
//      if (p instanceof Decorator) {
//        type = ((Decorator) p).myGetType();
//        newState = findState(oldPiece, type, p.getClass());
//        if (newState != null) {
//          ((Decorator) p).mySetState(newState);
//        }
//        p = ((Decorator) p).getInner();
//      }
//
//    }
//  }
//
//  public String findState(GamePiece piece, String typeToFind, Class findClass) {
//
//    GamePiece p = piece;
//    while (p != null) {
//      p = Decorator.getDecorator(p, findClass);
//      if (p != null) {
//
//        // if (p instanceof Labeler) {
//        // Labeler l = (Labeler) p;
//        String type = ((Decorator) p).myGetType();
//        if (type.equals(typeToFind)) {
//          return ((Decorator) p).myGetState();
//          // return l.myGetState();
//        }
//        else if (typeToFind.startsWith(Embellishment.ID)) {
//          if ((type.indexOf("Smoke") > 0 && typeToFind.indexOf("Smoke") > 0) || (type.indexOf("Rubble") > 0 && typeToFind.indexOf("Rubble") > 0)
//              || (type.indexOf("Fire") > 0 && typeToFind.indexOf("Fire") > 0)) {
//            return ((Decorator) p).myGetState();
//          }
//        }
//        // }
//        p = ((Decorator) p).getInner();
//      }
//    }
//    return null;
//  }

  protected static class ReplaceTrait extends Replace {
    protected GamePiece replacement;
    protected GamePiece original;

    public ReplaceTrait(GamePiece original, GamePiece replacement) {
      super(Replace.ID + "Replace;R;dummy;;0;0;true", original);
      setProperty(VASSAL.counters.Properties.OUTER, original);
      original.setProperty(VASSAL.counters.Properties.OUTER, null);
      this.replacement = replacement;
      this.original = original;
    }

    public GamePiece createMarker() {
      GamePiece marker = PieceCloner.getInstance().clonePiece(replacement);
      if (matchRotation) {
        matchTraits(getInner(), marker);
      }
      return marker;
    }

    public Command replacePiece() {
      return super.replacePiece();
    }

    protected void matchTraits(GamePiece base, GamePiece marker) {
      if (!(base instanceof Decorator) || !(marker instanceof Decorator)) {
        return;
      }
      Decorator currentTrait = (Decorator) base;
      Decorator lastMatch = (Decorator) marker;
      while (currentTrait != null) {
        Decorator candidate = lastMatch;
        while (candidate != null) {
          candidate = (Decorator) Decorator.getDecorator(candidate, currentTrait.getClass());
          if (candidate != null) {
            String candidateType = candidate.myGetType();
            String currentType = currentTrait.myGetType();
            if (candidateType.equals(currentType)
                || ((currentType.startsWith(Embellishment.ID) && 
                    ((currentType.indexOf("Smoke") > 0 && candidateType.indexOf("Smoke") > 0) || 
                    (currentType.indexOf("Rubble") > 0 && candidateType.indexOf("Rubble") > 0) || 
                    (currentType.indexOf("Fire") > 0 && candidateType.indexOf("Fire") > 0))))) {
              candidate.mySetState(currentTrait.myGetState());
              lastMatch = candidate;
              candidate = null;
            }
            else {
              GamePiece inner = candidate.getInner();
              if (inner instanceof Decorator) {
                candidate = (Decorator) inner;
              }
              else {
                candidate = null;
              }
            }
          }
        }
        if (currentTrait.getInner() instanceof Decorator) {
          currentTrait = (Decorator) currentTrait.getInner();
        }
        else {
          currentTrait = null;
        }
      }
    }
  }
}
