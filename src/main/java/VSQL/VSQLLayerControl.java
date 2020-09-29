/*
 * $Id: VSQLLayerControl.java 5546 2009-04-27 13:21:39Z swampwallaby $
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

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenuItem;

import VASSAL.build.Buildable;
import VASSAL.build.module.map.LayerControl;

public class VSQLLayerControl extends LayerControl {

  // menuitem in the popup menu
  JMenuItem m_MenuItem = null;

  public VSQLLayerControl() {
    super();

  }

  public JMenuItem getQCMenuItem() {
    return m_MenuItem;
  }

  public void addTo(Buildable parent) {
    super.addTo(parent);

    m_MenuItem = new JMenuItem(launch.getText());
    m_MenuItem.setIcon(launch.getIcon());
    m_MenuItem.addActionListener(((JButton) launch).getListeners(ActionListener.class)[0]);

    pieceLayers.getToolBar().remove(launch);

  }
}
