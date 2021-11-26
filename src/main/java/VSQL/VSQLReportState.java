/*
 * $Id: VSQLReportState.java 617 2005-06-03 22:01:14Z swampwallaby $
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
/*
 * Created by IntelliJ IDEA.
 * User: rkinney
 * Date: Oct 2, 2002
 * Time: 6:30:35 AM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package VSQL;

import java.awt.event.InputEvent;

import javax.swing.KeyStroke;

import VASSAL.command.Command;
import VASSAL.command.NullCommand;
import VASSAL.counters.Decorator;
import VASSAL.counters.GamePiece;
import VASSAL.counters.Properties;
import VASSAL.counters.ReportState;

/**
 * A GamePiece with this trait will echo the piece's current name when any of a given key commands are pressed
 * (and after they take effect)
 */
public class VSQLReportState extends ReportState  {

  public VSQLReportState() {
    super();
  }

  public VSQLReportState(String type, GamePiece inner) {
   super(type, inner);
  }

  /*
   * Vsql 2.5.8 to 3.0 conversion
   * Old counters may have ReportAction commands that need suppressing
   * when counter is concealed. Need to let Mark Moved command through.
   */
  public Command keyEvent(KeyStroke stroke) {
    if (obscuredToMe()) {
      if (stroke.equals(VSQLMarkMoved.markStroke)) {
        return super.keyEvent(stroke);
      }
      else {
        return new NullCommand();
      }
    }
    else {
      return super.keyEvent(stroke);
    }
  }
  
  /*
   * Don't display reports for Units in toolbox
   */
  public Command myKeyEvent(KeyStroke stroke) {
  
    if (getMap() == null && stroke != KeyStroke.getKeyStroke('D', InputEvent.CTRL_DOWN_MASK)) {
      return null;
    }
    else {
      return super.myKeyEvent(stroke);
    }
  }
  
  protected boolean obscuredToMe() {
    Boolean otm = (Boolean) Decorator.getOutermost(this).getProperty(Properties.OBSCURED_TO_ME);
    if (otm == null || !otm.booleanValue()) {
      return false;
    }
    else {
      return true;
    }
  }
}
