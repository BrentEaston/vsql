/*
 * $Id: ASLPieceMover.java 5078 2009-02-09 05:40:45Z swampwallaby $
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import VASL.counters.ASLHighlighter;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.Map;
import VASSAL.build.module.map.PieceMover;
import VASSAL.command.Command;
import VASSAL.command.NullCommand;
import VASSAL.counters.GamePiece;
import VASSAL.tools.LaunchButton;

public class ASLPieceMover extends PieceMover {
  /** Preferences key for whether to mark units as having moved */
  public static final String MARK_MOVED = "MarkMoved";
  public static final String HOTKEY = "hotkey";

  private LaunchButton clear;

  public ASLPieceMover() {
    ActionListener al = new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        GamePiece[] p = getMap().getPieces();
        Command c = new NullCommand();
        for (int i = 0; i < p.length; ++i) {
          c.append(markMoved(p[i], false));
        }
        GameModule.getGameModule().sendAndLog(c);
        getMap().repaint();
      }
    };
    clear = new LaunchButton("Mark unmoved", null, HOTKEY, al);
  }

  public Map getMap() {
    return map;
  }

  public String[] getAttributeNames() {
    String[] s = super.getAttributeNames();
    String[] all = new String[s.length + 1];
    System.arraycopy(s, 0, all, 0, s.length);
    all[all.length - 1] = HOTKEY;
    return all;
  }

  public String getAttributeValueString(String key) {
    if (HOTKEY.equals(key)) {
      return clear.getAttributeValueString(key);
    }
    else {
      return super.getAttributeValueString(key);
    }
  }

  public void setAttribute(String key, Object value) {
    if (HOTKEY.equals(key)) {
      clear.setAttribute(key, value);
    }
    else {
      super.setAttribute(key, value);
    }
  }

  public void addTo(Buildable b) {
    super.addTo(b);

    map.setHighlighter(new ASLHighlighter());
/*
    map.getToolBar().add(clear);
    BooleanConfigurer option = new BooleanConfigurer(MARK_MOVED, "Mark moved units");
    GameModule.getGameModule().getPrefs().addOption(option);
*/
  }
}
