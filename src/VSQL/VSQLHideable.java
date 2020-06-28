/*
 * $Id: VSQLHideable.java 609 2005-05-24 03:11:35Z swampwallaby $
 *
 * Copyright (c) 2000-2005 by Rodney Kinney, Brent Easton
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

import javax.swing.KeyStroke;

import VASSAL.command.Command;
import VASSAL.command.NullCommand;
import VASSAL.counters.Decorator;
import VASSAL.counters.GamePiece;
import VASSAL.counters.Hideable;
import VASSAL.counters.KeyCommand;
import VASSAL.counters.Properties;
 
 public class VSQLHideable extends Hideable {
   
   public VSQLHideable() {
     super();
   }
   
   public VSQLHideable(String type, GamePiece p) {
     super(type, p);
   }
   
   /*
    * Don't return a position name if hidden 
    */
   public Object getProperty(Object key) {
     if ("Location".equals(key)) {
       if (invisibleToMe() || invisibleToOthers()) {
         return new Boolean(true);
       }
       else {
         return piece.getProperty(key);
       }
     }
     else {
       return super.getProperty(key);
     }
   }
   
   /*
    * If this unit is concealed, then do not display or enact the HIP command
    * For compatibility vsql 2.5.8 to 3.0
    * Old counters have HIP outside of Conceal so will show up differently when
    * concealed
    */
   public KeyCommand[] myGetKeyCommands() {

     if (obscuredToMe()) {
       return new KeyCommand[0];
     }
     else {
       return super.myGetKeyCommands();
     }
   }
      
   public Command myKeyEvent(KeyStroke stroke) {
     if (obscuredToMe()) {
       return new NullCommand(); 
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