/*
 * $Id: VersionChecker.java 5546 2009-04-27 13:21:39Z swampwallaby $
 *
 * Copyright (c) 2005 by Brent Easton
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

import javax.swing.JOptionPane;

import VASSAL.Info;
import VASSAL.build.AbstractConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.GameComponent;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.command.Command;

/**
 * @author Brent Easton
 */
public class VersionChecker extends AbstractConfigurable implements GameComponent {

  protected static final String VASSAL_VERSION = "vassalVersion";
  protected String vassalVersion;

  public String[] getAttributeDescriptions() {
    return new String[] { "Approved VASSAL Version" };
  }

  public Class<?>[] getAttributeTypes() {
    return new Class[] { String.class };
  }

  public String[] getAttributeNames() {
    return new String[] { VASSAL_VERSION };
  }

  public void setAttribute(String key, Object value) {
    if (key.equals(VASSAL_VERSION)) {
      vassalVersion = (String) value;
    }
  }

  public String getAttributeValueString(String key) {
    String s = null;

    if (key.equals(VASSAL_VERSION)) {
      s = vassalVersion;
    }
    return s;
  }

  public void removeFrom(Buildable parent) {
    GameModule.getGameModule().getGameState().removeGameComponent(this);
  }

  public HelpFile getHelpFile() {
    return null;
  }

  public Class<?>[] getAllowableConfigureComponents() {
    return new Class[0];
  }

  public void addTo(Buildable parent) {
    GameModule.getGameModule().getGameState().addGameComponent(this);
  }

  public void setup(boolean gameStarting) {
    if (gameStarting) {
      if (!vassalVersion.equals(Info.getVersion())) {
        JOptionPane.showMessageDialog(null, "*** WARNING *** \nThe approved version of VASSAL for VSQL use is v"
            + vassalVersion + ".\nYou are currently running VASSAL version v" + Info.getVersion()
            + ".\nYour saved games may not be readable by other players.", "Warning", JOptionPane.ERROR_MESSAGE);
      }
    }

  }

  public Command getRestoreCommand() {
    return null;
  }

}