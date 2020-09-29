/*
 * $Id: VSQLConcealment.java 2271 2007-07-08 02:39:45Z swampwallaby $
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

package VSQL;

import VASL.counters.ASLProperties;
import VASL.counters.Concealable;
import VASL.counters.Concealment;
import VASSAL.counters.Decorator;
import VASSAL.counters.GamePiece;

public class VSQLConcealment extends Concealment {
  
  public VSQLConcealment() {
    super();
  }

  public VSQLConcealment(String type, GamePiece p) {
    super(type, p);
  }
  
  public boolean canConceal(GamePiece p) {
    Concealable c = (Concealable) Decorator.getDecorator(p, Concealable.class);
    if (c == null
        || !c.isMaskable()) {
      return false;
    }
    else {
      String subType = (String) Decorator.getOutermost(this).getProperty(VSQLProperties.UNIT_SUB_TYPE);
      boolean isBunker = (subType == null) ? false : subType.equals(VSQLProperties.BUNKER);
      return getNationality().equals(c.getProperty(ASLProperties.NATIONALITY)) || isBunker;
    }
  }
  
}