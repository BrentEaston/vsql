package VSQL;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import VASL.build.module.map.HindranceKeeper;
import VASSAL.build.Buildable;
import VASSAL.build.module.Map;
import VASSAL.counters.GamePiece;
import VASSAL.counters.Stack;

public class VSQLHindranceKeeper extends HindranceKeeper  {

  protected VSQLThread los;
  
  public VSQLHindranceKeeper () {
    super();
  }
  
  /* 
   * Don't show hindrance counters unless LOS is activated
   */
  public void draw(Graphics g, Map m) {
    
    if (isLOSactivated(m)) {
      // Use existing Hindrance keeper to draw Hindrance counters
      // at map specified transparency
      Graphics2D g2d = (Graphics2D) g;
      Composite oldComposite = g2d.getComposite();
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, map.getPieceOpacity()));
      super.draw(g, m);
      g2d.setComposite(oldComposite);
    }
    
    if (isLOSvisible(m)) {
      // Find and draw any Overlay counters at full strength
      GamePiece[] pieces = m.getPieces();
      for (int i = 0; i < pieces.length; ++i) {
        if (pieces[i] instanceof Stack) {
          Stack s = (Stack) pieces[i];
          for (int j = 0; j < s.getPieceCount(); j++) {
            apply(s.getPieceAt(j), g, m);
          }
        }
        else {
          apply(pieces[i], g, m);
        }          
      }
    }
  }

  
  protected boolean isLOSactivated(Map map) {
    findLOSThread();
    if (los != null) {
      return los.isActive();
    }
    return false;
  }
  
  protected boolean isLOSvisible(Map map) {
    findLOSThread();
    if (los != null) {
      return los.isVisible();
    }
    return false;
  }
  
  protected void findLOSThread() {
    if (los == null) {
      for (Buildable c : this.buildComponents) {
        if (c instanceof VSQLThread) {
          los = (VSQLThread) c;
          return;
        }
      }
    }
  }
  
  
  protected void apply(GamePiece p, Graphics g, Map m) {
    if ("Overlay".equals(p.getProperty("Level"))) {
      Point pt = m.mapToComponent(p.getPosition());
      p.draw(g, pt.x, pt.y, m.getView(), m.getZoom());
    }
    
  }
}
