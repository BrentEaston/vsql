package VSQL;

import java.awt.geom.Area;

import VASSAL.build.GameModule;
import VASSAL.build.module.map.MapShader;
import VASSAL.counters.GamePiece;
import VASSAL.counters.Stack;

public class VSQLMapShader extends MapShader {

  public VSQLMapShader() {
      super();
  }
  
  protected void checkPiece(Area area, GamePiece piece) {
    if (piece instanceof Stack) {
      super.checkPiece(area, piece);
    }
    else {
      String nvs = (String) GameModule.getGameModule().getProperty(VSQLProperties.NV_SIDE) + "";
      String side = (String) piece.getProperty(VSQLProperties.SIDE) + "";
      String light = (String) piece.getProperty(VSQLProperties.LIGHT) + "";
      
      if (nvs.equals(side) || light.equals("true")) {
        super.checkPiece(area, piece);;
      }      
    }
    return;
  }
  
}
