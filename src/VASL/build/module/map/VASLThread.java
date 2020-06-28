/*
 * $Id: VASLThread 11/25/13 davidsullivan1 $
 *
 * Copyright (c) 2013 by David Sullivan
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

import static VASSAL.build.GameModule.getGameModule;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.JCheckBox;

import VASL.LOS.Map.LOSResult;
import VASL.LOS.Map.Location;
import VASL.LOS.Map.VASLGameInterface;
import VASL.build.module.ASLMap;
import VASL.build.module.map.boardPicker.ASLBoard;
import VASSAL.build.Buildable;
import VASSAL.build.module.GameComponent;
import VASSAL.build.module.map.LOS_Thread;
import VASSAL.build.module.map.boardPicker.Board;
import VASSAL.build.module.map.boardPicker.board.HexGrid;
import VASSAL.command.Command;
import VASSAL.configure.BooleanConfigurer;
import VASSAL.configure.ColorConfigurer;

public class VASLThread extends LOS_Thread implements KeyListener, GameComponent {

    public static final String ENABLED = "LosCheckEnabled";
    public static final String HINDRANCE_THREAD_COLOR = "hindranceThreadColor";
    public static final String BLOCKED_THREAD_COLOR = "blockedThreadColor";
    private boolean legacyMode;
    private boolean initialized; // LOS has been initialized?
    private static final String preferenceTabName = "LOS";
    private VASL.LOS.Map.Map LOSMap;

    // LOS stuff
    private LOSResult result;
    private Location source;
    private Location target;
    private VASLGameInterface VASLGameInterface;
    private ASLBoard upperLeftBoard;
    private boolean useAuxSourceLOSPoint;
    private boolean useAuxTargetLOSPoint;
    private String resultsString = "";

    // LOS colors
    private Color LOSColor;
    private Color hindranceColor;
    private Color blockedColor;

    private void setGridSnapToVertex(boolean toVertex) {
        for (Board b : map.getBoards()) {
            HexGrid grid =
                    (HexGrid)b.getGrid();
            grid.setCornersLegal(toVertex);
            grid.setEdgesLegal(!toVertex);
        }
    }

    /**
     * Called when the LOS check is started
     */
    @Override
	protected void launch() {

        super.launch();
        setGridSnapToVertex(true);
        initializeMap();

    }

    private void initializeMap(){

        // make sure we have a map otherwise disable LOS
        final ASLMap theMap = (ASLMap) map;
        if(theMap == null || theMap.isLegacyMode()) {
            legacyMode = true;
        }
        else {
            legacyMode = false;
            LOSMap = theMap.getVASLMap();

            // initialize LOS
            result = new LOSResult();
            VASLGameInterface = new VASLGameInterface(theMap, LOSMap);
            VASLGameInterface.updatePieces();

            // setting these to null prevents the last LOS from being shown when launched
            source = null;
            target = null;

        }

        // grab the user preferences for the thread colors
        super.threadColor = (Color) getGameModule().getPrefs().getValue(LOS_COLOR);
        LOSColor = (Color) getGameModule().getPrefs().getValue(LOS_COLOR);
        if (LOSPrefActive()) {

            LOSColor = (Color) getGameModule().getPrefs().getValue(LOS_COLOR);
            hindranceColor = (Color) getGameModule().getPrefs().getValue(HINDRANCE_THREAD_COLOR);
            blockedColor = (Color) getGameModule().getPrefs().getValue(BLOCKED_THREAD_COLOR);
            map.getView().requestFocus();
        }

        initialized = true;
    }

    @Override
	public void addTo(Buildable buildable) {

        super.addTo(buildable);

        // add the key listener
        map.getView().addKeyListener(this);

        // add additional thread colors
        final BooleanConfigurer enable = new BooleanConfigurer(ENABLED, "Enable LOS checking", Boolean.TRUE);
        final JCheckBox enableBox = findBox(enable.getControls());

        final ColorConfigurer thread = new ColorConfigurer(LOS_COLOR, "Thread Color", Color.red);
        final ColorConfigurer hindrance = new ColorConfigurer(HINDRANCE_THREAD_COLOR, "Hindrance Thread Color", Color.red);
        final ColorConfigurer blocked = new ColorConfigurer(BLOCKED_THREAD_COLOR, "Blocked Thread Color", Color.blue);
        final BooleanConfigurer verbose = new BooleanConfigurer("verboseLOS", "Verbose LOS mode");
        getGameModule().getPrefs().addOption(preferenceTabName, thread);
        getGameModule().getPrefs().addOption(preferenceTabName, enable);
        getGameModule().getPrefs().addOption(preferenceTabName, hindrance);
        getGameModule().getPrefs().addOption(preferenceTabName, blocked);
        getGameModule().getPrefs().addOption(preferenceTabName, verbose);
        final ItemListener l = new ItemListener() {

			public void itemStateChanged(ItemEvent evt) {
                enableAll(hindrance.getControls(), enableBox.isSelected());
                enableAll(blocked.getControls(), enableBox.isSelected());
                enableAll(verbose.getControls(), enableBox.isSelected());
            }
        };
        enableBox.addItemListener(l);
        enableAll(hindrance.getControls(), Boolean.TRUE.equals(enable.getValue()));
        enableAll(blocked.getControls(), Boolean.TRUE.equals(enable.getValue()));
        enableAll(verbose.getControls(), Boolean.TRUE.equals(enable.getValue()));

        // hook for game opening/closing
        getGameModule().getGameState().addGameComponent(this);
    }

    protected static JCheckBox findBox(Component c) {
        JCheckBox val = null;
        if (c instanceof JCheckBox) {
            val = (JCheckBox) c;
        }
        for (int i = 0; i < ((Container) c).getComponentCount(); ++i) {
            val = findBox(((Container) c).getComponent(i));
            if (val != null) {
                break;
            }
        }
        return val;
    }

    private static void enableAll(Component c, boolean enable) {
        c.setEnabled(enable);
        if (c instanceof Container) {
            for (int i = 0; i < ((Container) c).getComponentCount(); ++i) {
                enableAll(((Container) c).getComponent(i), enable);
            }
        }
    }

    @Override
	public void mousePressed(MouseEvent e) {

        super.mousePressed(e);
        if (!isEnabled() || legacyMode) {
            return;
        }

        setSourceFromMousePressedEvent(new Point(e.getPoint()));

        if(source == null) {
            return;
        }

        // if Ctrl click, use upper location
        if (e.isControlDown()) {
            while (source.getUpLocation() != null) {
                source = source.getUpLocation();
            }
        }

        // make the source and the target the same
        target = source;
        useAuxTargetLOSPoint = useAuxSourceLOSPoint;
    }

    /**
     * Sets the source location using a mouse-pressed event point
     * @param eventPoint the point in mouse pressed coordinates
     */
    private void setSourceFromMousePressedEvent(Point eventPoint) {

        final Point p = mapMouseToMapCoordinates(eventPoint);
        if (p == null || !LOSMap.onMap(p.x, p.y)) return;
        source = LOSMap.gridToHex(p.x, p.y).getNearestLocation(p.x, p.y);
        useAuxSourceLOSPoint = useAuxLOSPoint(source, p.x, p.y);
    }

    @Override
	public void mouseReleased(MouseEvent e) {

        super.mouseReleased(e);
        if (!isVisible()) {
            setGridSnapToVertex(false);
        }
    }

    @Override
	public void mouseDragged(MouseEvent e) {

        if (!legacyMode && source != null && isEnabled()) {

            final Location oldLocation = target;
            final boolean oldAuxFlag = useAuxTargetLOSPoint;

            setTargetFromMouseDraggedEvent(new Point(e.getPoint()));

            // are we really in a new location?
            if (target == null || (target.equals(oldLocation) && useAuxTargetLOSPoint == oldAuxFlag)) {
                return;
            }

            // if Ctrl click, use upper location
            if (e.isControlDown()) {
                while (target.getUpLocation() != null) {
                    target = target.getUpLocation();
                }
            }
            doLOS();
        }
        super.mouseDragged(e);
        map.repaint();
    }

    /**
     * Sets the target using a mouse-dragged event point
     * @param eventPoint the point in mouse dragged coordinates
     */
    private void setTargetFromMouseDraggedEvent(Point eventPoint) {

        final Point p = map.mapCoordinates(eventPoint);
        p.translate(-map.getEdgeBuffer().width, -map.getEdgeBuffer().height);
        if (p == null || !LOSMap.onMap(p.x, p.y)) return;
        target = LOSMap.gridToHex(p.x, p.y).getNearestLocation(p.x, p.y);
        useAuxTargetLOSPoint = useAuxLOSPoint(target, p.x, p.y);
    }

    /**
     * Sets the target using a remote event point
     * @param eventPoint the point in remote event coordinates
     */
    private void setTargetFromRemoteEvent(Point eventPoint) {

         final Point p = mapMouseToMapCoordinates(eventPoint);
        if (p == null || !LOSMap.onMap(p.x, p.y)) return;
        target = LOSMap.gridToHex(p.x, p.y).getNearestLocation(p.x, p.y);
        useAuxTargetLOSPoint = useAuxLOSPoint(target, p.x, p.y);

    }

    public boolean isEnabled() {
        return visible && LOSPrefActive();
    }

    private static boolean LOSPrefActive() {
        return Boolean.TRUE.equals(getGameModule().getPrefs().getValue(ENABLED));
    }

    @Override
	public void draw(Graphics g, VASSAL.build.module.Map m) {

        if (!LOSPrefActive() || legacyMode) {
            super.draw(g, m);
        }
        else if (visible) {

            lastAnchor = map.componentCoordinates(anchor);
            lastArrow = map.componentCoordinates(arrow);

            if (source != null && target != null) {
                // source LOS point
                Point sourceLOSPoint;
                if (useAuxSourceLOSPoint) {
                    sourceLOSPoint = new Point(source.getAuxLOSPoint());
                }
                else {
                    sourceLOSPoint = new Point(source.getLOSPoint());
                }
                sourceLOSPoint = mapPointToScreen(sourceLOSPoint);

                // target LOS point
                Point targetLOSPoint;
                if (useAuxTargetLOSPoint) {
                    targetLOSPoint = new Point(target.getAuxLOSPoint());
                }
                else {
                    targetLOSPoint = new Point(target.getLOSPoint());
                }
                targetLOSPoint = mapPointToScreen(targetLOSPoint);

                // transform the blocked-at point
                Point b = null;
                if (result.isBlocked()) {
                    b = new Point(result.getBlockedAtPoint());
                    b = mapPointToScreen(b);
                }
                // transform the hindrance point
                Point h = null;
                if (result.hasHindrance()) {
                    h = new Point(result.firstHindranceAt());
                    h = mapPointToScreen(h);
                }
                // draw the LOS thread
                if (result.isBlocked()) {
                    if (result.hasHindrance()) {
                        g.setColor(LOSColor);
                        g.drawLine(
                                sourceLOSPoint.x,
                                sourceLOSPoint.y,
                                h.x,
                                h.y);
                        g.setColor(hindranceColor);
                        g.drawLine(
                                h.x,
                                h.y,
                                b.x,
                                b.y);
                        g.setColor(blockedColor);
                        g.drawLine(
                                b.x,
                                b.y,
                                targetLOSPoint.x,
                                targetLOSPoint.y);
                    }
                    else {
                        g.setColor(LOSColor);
                        g.drawLine(
                                sourceLOSPoint.x,
                                sourceLOSPoint.y,
                                b.x,
                                b.y);
                        g.setColor(blockedColor);
                        g.drawLine(
                                b.x,
                                b.y,
                                targetLOSPoint.x,
                                targetLOSPoint.y);
                    }
                }
                else if (result.hasHindrance()) {
                    g.setColor(LOSColor);
                    g.drawLine(
                            sourceLOSPoint.x,
                            sourceLOSPoint.y,
                            h.x,
                            h.y);
                    g.setColor(hindranceColor);
                    g.drawLine(
                            h.x,
                            h.y,
                            targetLOSPoint.x,
                            targetLOSPoint.y);
                }
                else {
                    g.setColor(LOSColor);
                    g.drawLine(
                            sourceLOSPoint.x,
                            sourceLOSPoint.y,
                            targetLOSPoint.x,
                            targetLOSPoint.y);
                }

                // use the draw range property to turn all text on/off
                if (drawRange) {
                    // determine if the text should be above or below the location
                    final boolean shiftSourceText = sourceLOSPoint.y > targetLOSPoint.y;
                    final int shift = g.getFontMetrics().getHeight();

                    // draw the source elevation
                    switch (source.getBaseHeight() + source.getHex().getBaseHeight()) {
                        case -1:
                        case -2:
                            g.setColor(Color.red);
                            break;
                        case 0:
                            g.setColor(Color.gray);
                            break;
                        case 1:
                            g.setColor(Color.darkGray);
                            break;
                        case 2:
                            g.setColor(Color.black);
                            break;
                        default:
                            g.setColor(Color.white);
                    }
                    g.setFont(RANGE_FONT);
                    if (isVerbose()) {
                        lastRangeRect = drawText(g,
                                sourceLOSPoint.x - 20,
                                sourceLOSPoint.y + (shiftSourceText ? shift : 0) - g.getFontMetrics().getDescent(),
                                source.getName() + "  (Level " + (source.getBaseHeight() + source.getHex().getBaseHeight() + ")"));
                    }
                    else if (source.getBaseHeight() != 0) {
                        lastRangeRect = drawText(g,
                                sourceLOSPoint.x - 20,
                                sourceLOSPoint.y + (shiftSourceText ? shift : 0) - g.getFontMetrics().getDescent(),
                                "Level " + (source.getBaseHeight() + source.getHex().getBaseHeight()));
                    }

                    // draw the target elevation
                    switch (target.getBaseHeight() + target.getHex().getBaseHeight()) {
                        case -1:
                        case -2:
                            g.setColor(Color.red);
                            break;
                        case 0:
                            g.setColor(Color.gray);
                            break;
                        case 1:
                            g.setColor(Color.darkGray);
                            break;
                        case 2:
                            g.setColor(Color.black);
                            break;
                        default:
                            g.setColor(Color.white);
                    }
                    if (isVerbose()) {
                        lastRangeRect.add(drawText(g,
                                targetLOSPoint.x - 20,
                                targetLOSPoint.y + (shiftSourceText ? 0 : shift) - g.getFontMetrics().getDescent(),
                                target.getName() + "  (Level " + (target.getBaseHeight() + target.getHex().getBaseHeight() + ")")));
                    }
                    else if (target.getBaseHeight() != 0) {
                        lastRangeRect.add(drawText(g,
                                targetLOSPoint.x - 20,
                                targetLOSPoint.y + (shiftSourceText ? 0 : shift) - g.getFontMetrics().getDescent(),
                                "Level " + (target.getBaseHeight() + target.getHex().getBaseHeight())));
                    }

                    // draw the verbose text
                    g.setColor(Color.black);
                    if (shiftSourceText) {
                        lastRangeRect.add(drawText(g, targetLOSPoint.x - 20, targetLOSPoint.y - shift, resultsString));
                    }
                    else {
                        lastRangeRect.add(drawText(g, targetLOSPoint.x - 20, targetLOSPoint.y + shift * 2 - 2, resultsString));
                    }
                }
            }
        }
        else {
            super.draw(g, m);
        }
    }

    private static boolean isVerbose() {
        return Boolean.TRUE.equals(getGameModule().getPrefs().getValue("verboseLOS"));
    }

	public void keyTyped(KeyEvent e) {
    }

	public void keyReleased(KeyEvent e) {
    }

	public void keyPressed(KeyEvent e) {

        if (!isEnabled() && legacyMode) {
            return;
        }
        final int code = e.getKeyCode();
        // move up
        if (code == KeyEvent.VK_KP_UP || code == KeyEvent.VK_UP) {

            e.consume(); // prevents the map from scrolling when trying to move end point

            // move the source up
            if (e.isControlDown() && source != null) {
                if (source.getUpLocation() != null) {
                    source = source.getUpLocation();
                    doLOS();
                    map.repaint();
                }
            }
            // move the target up
            else if (target != null) {
                if (target.getUpLocation() != null) {
                    target = target.getUpLocation();
                    doLOS();
                    map.repaint();
                }
            }
        }

        // move down
        else if (code == KeyEvent.VK_KP_DOWN || code == KeyEvent.VK_DOWN) {

            e.consume();

            // move the source down
            if (e.isControlDown() && source != null) {
                if (source.getDownLocation() != null) {
                    source = source.getDownLocation();
                    doLOS();
                    map.repaint();
                }
            }
            // move the target down
            else if (target != null) {
                if (target.getDownLocation() != null) {
                    target = target.getDownLocation();
                    doLOS();
                    map.repaint();

                }
            }
        }
    }

    private static boolean useAuxLOSPoint(Location l, int x, int y) {

        final Point LOSPoint = l.getLOSPoint();
        final Point AuxLOSPoint = l.getAuxLOSPoint();

        // use the closest LOS point
		return Point2D.distance((double)x, (double)y, (double)LOSPoint.x, (double)LOSPoint.y) > Point2D.distance((double)x, (double)y, (double)AuxLOSPoint.x, (double)AuxLOSPoint.y);
	}

	public Command getRestoreCommand() {
        return null;
    }

    private Point mapPointToScreen(Point p) {

        final Point temp = map.componentCoordinates(new Point(p));
        final double scale = upperLeftBoard == null ? 1.0 : upperLeftBoard.getMagnification() * ((HexGrid)upperLeftBoard.getGrid()).getHexSize()/ASLBoard.DEFAULT_HEX_HEIGHT;
        if (upperLeftBoard != null) {
            temp.x = (int)Math.round((double)temp.x *scale);
            temp.y = (int)Math.round((double)temp.y *scale);
        }
        temp.translate((int) ((double)map.getEdgeBuffer().width * map.getZoom()), (int) ((double)map.getEdgeBuffer().height * map.getZoom()));

        return temp;
    }

    private Point mapMouseToMapCoordinates(Point p) {

        // just remove edge buffer
        final Point temp = new Point(p);
        temp.translate(-map.getEdgeBuffer().width, -map.getEdgeBuffer().height);
        return temp;
    }

    /**
     * Draws some text on the map
     * @param g the map graphics
     * @param x upper left point
     * @param y upper left point
     * @param s the text
     * @return a bounding region of the text drawn
     */
    private static Rectangle drawText(Graphics g, int x, int y, String s) {

        // paint the background
        final int border = 1;
        g.setColor(Color.black);
        final Rectangle region = new Rectangle(
                x - border,
                y - border - g.getFontMetrics().getHeight() + g.getFontMetrics().getDescent(),
                g.getFontMetrics().stringWidth(s) + 2,
                g.getFontMetrics().getHeight() + 2);
        g.fillRect(region.x, region.y, region.width, region.height);

        // draw the text
        g.setColor(Color.white);
        g.drawString(s, x, y);
        return region;
    }

    /**
     * Execute the LOS
     */
    private void doLOS() {

        // silently ignore invalid LOS checks
        if(source == null || target == null || result == null || VASLGameInterface == null)
        {
            return;
        }

        // do the LOS
        result = new LOSResult();
        LOSMap.LOS(source, useAuxSourceLOSPoint, target, useAuxTargetLOSPoint, result, VASLGameInterface);

        // set the result string
        resultsString =
                "Range: " + result.getRange();
        lastRange = String.valueOf(result.getRange());
        if (isVerbose()) {
            if (result.isBlocked()) {
                resultsString += "  Blocked in " + LOSMap.gridToHex(result.getBlockedAtPoint().x, result.getBlockedAtPoint().y).getName() +
                        " ( " + result.getReason() + ")";
            }
            else {
                resultsString += (result.getHindrance() > 0 ? ("  Hindrances: " + result.getHindrance()) : "");
            }
        }
    }

	// force a paint when remote LOS command received
	@Override
	public Command decode(String command) {
		map.repaint();
		return super.decode(command);
	}

    @Override
    protected void setEndPoints(Point newAnchor, Point newArrow) {
        anchor.x = newAnchor.x;
        anchor.y = newAnchor.y;
        arrow.x = newArrow.x;
        arrow.y = newArrow.y;

        initializeMap();

        if (!legacyMode) {
            setSourceFromMousePressedEvent(newAnchor);
            setTargetFromRemoteEvent(newArrow);
            doLOS();
        }

        map.repaint();
    }
}
