/*
 * $Id: ASLStackMetrics.java 611 2005-05-27 11:35:02Z swampwallaby $
 *
 * Copyright (c) 2000-2003 by Rodney Kinney
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
package VASL.build.module.map;

import java.awt.Component;
import java.awt.Graphics;

import VASL.counters.ASLProperties;
import VASSAL.build.module.map.StackMetrics;
import VASSAL.counters.GamePiece;
import VASSAL.counters.Properties;

public class ASLStackMetrics extends StackMetrics {
  protected void drawUnexpanded(GamePiece p, Graphics g,
                                int x, int y, Component obs, double zoom) {
    
    /*
     * vsql 3.0 - Modified so that HIP units (which are now non-meoveable) are not offset
     */
    if (p.getProperty(ASLProperties.LOCATION) != null && !isInvisibleToOthers(p)) {
      p.draw(g, x - (int) (zoom * 15), y, obs, zoom);
    }
    else {
      super.drawUnexpanded(p, g, x, y, obs, zoom);
    }
  }
  
  protected boolean isInvisibleToOthers(GamePiece p) {
    Boolean oto = (Boolean) p.getProperty(Properties.INVISIBLE_TO_OTHERS);
    return (oto != null && oto.booleanValue());
  }


}
