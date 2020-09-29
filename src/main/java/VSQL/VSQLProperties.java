/*
 * $Id: VSQLProperties.java 9198 2015-05-27 01:59:44Z swampwallaby $
 * 
 * Copyright (c) 2005 by Brent Easton
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License (LGPL) as published by
 * the Free Software Foundation.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, copies are available at
 * http://www.opensource.org.
 */

package VSQL;

public class VSQLProperties {
  
  public static final String VSQL = "VSQL";
  
  // Preferences
  public static final String SNAP_OPTION = "snapoption";
  public static final String ZOOM_LEVELS = "zoomLevels";
  public static final String ZOOM_START = "zoomStart";
  public static final String ZOOM_FACTOR = "zoomFactor";
  public static final String RULE_LEVEL = "ruleLevel";  
  public static final String SL = "SL";
  public static final String COI = "COI";
  public static final String COD = "COD";
  public static final String GI = "GI";
  public static final String[] RULE_LEVELS = new String[] { SL, COI, COD, GI };
  public static final String SHOWQC = "showQc";
  
  // Political
  public static final String SIDE = "Side";
  public static final String AXIS = "Axis";
  public static final String ALLIED = "Allied";
  public static final String NEUTRAL = "Neutral";
  
  public static final String NATION = "Nation";
  public static final String GERMAN = "German";
  public static final String AMERICAN = "American";
  public static final String RUSSIAN = "Russian";
  public static final String AXIS_MINOR = "Axis Minor";
  public static final String ALLIED_MINOR = "Allied Minor";
  public static final String BRITISH = "British";
  public static final String FRENCH = "French";
  
  public static final String GERMAN_COLOR = "194,246,255";
  public static final String AMERICAN_COLOR = "181,222,90";
  public static final String RUSSIAN_COLOR = "206,156,74";
  public static final String NEUTRAL_COLOR = "156,206,156";
  public static final String AXIS_MINOR_COLOR = NEUTRAL_COLOR;
  public static final String ALLIED_MINOR_COLOR = NEUTRAL_COLOR;
  public static final String BRITISH_COLOR = "253,198,137";
  public static final String FRENCH_COLOR = "0,113,235";
  public static final String PLC_COLOR = "239,239,170";
  
  // Night Vision public properties
  public static final String NV_SIDE = "NVS";
  public static final String NV_RANGE = "NVR";
  public static final String LIGHT = "LIGHT";
  
  // Vehicle CA
  public static final String VEHICLE_CA = "VEHICLE_CA";
  
  // Number of Squads/Crews stacked with a leader
  public static final String STACKED_COUNT = "STACKED_COUNT";
  
  // Unit Types
  public static final String UNIT_TYPE = "Type";
  public static final String VEHICLE = "Vehicle";
  public static final String INFANTRY = "Infantry";
  public static final String GUN = "Gun";
  public static final String SW = "SW";
  
  // Unit Sub-types
  public static final String UNIT_SUB_TYPE = "Stype";
  public static final String LEADER = "Leader";
  public static final String SQUAD = "Squad";
  public static final String CREW = "Crew";
  public static final String SNIPER = "Sniper";
  public static final String TRUCK_OR_JEEP = "Truck-Jeep";
  public static final String ARMORED_CAR = "Armored Car";
  public static final String MOTOR_CYCLE = "Motor Cycle";
  public static final String BUNKER = "Bunker";
  
  // PLC's
  public static final String PLC = "PLC";
  public static final String PLC_NATIONALITY = "PlcNationality";
}