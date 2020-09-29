/*
 * $Id: VSQLLayeredPieceCollection.java 5546 2009-04-27 13:21:39Z swampwallaby $
 *
 * Copyright (c) 2015 by Brent Easton
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

import javax.swing.JMenuItem;

import VASL.build.module.ASLMap;
import VASSAL.build.Buildable;
import VASSAL.build.module.map.LayeredPieceCollection;

public class VSQLLayeredPieceCollection extends LayeredPieceCollection {
  
  public VSQLLayeredPieceCollection () {
    super();
  }
  
  public void addTo(Buildable parent) {
    super.addTo(parent);
    
    // Run though the children and add move any Launch buttons to the QC drop-down menu
    
    for (Buildable b : getBuildables()) {
      if (b instanceof VSQLLayerControl) {
        final VSQLLayerControl layer = (VSQLLayerControl) b;        
        final JMenuItem item = layer.getQCMenuItem();
        
        // getToolBar().remove(item);
        ((ASLMap) map).getPopupMenu().add(item);
      }
    }
  }
}