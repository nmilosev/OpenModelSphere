/*************************************************************************

Copyright (C) 2009 Grandite

This file is part of Open ModelSphere.

Open ModelSphere is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can redistribute and/or modify this particular file even under the
terms of the GNU Lesser General Public License (LGPL) as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License 
(LGPL) along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.jack.graphic.tool;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;

import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.Line;
import org.modelsphere.jack.international.LocaleMgr;

public abstract class LineTool extends ButtonSelectionPanelTool {
    private static final String HELP_STEP0 = LocaleMgr.misc.getString("HelpLine0");
    private static final String HELP_STEP1 = LocaleMgr.misc.getString("HelpLine1");

    public static final String kAngularLineTooltips = LocaleMgr.screen.getString("AngularLine");
    public static final String kRightAngleTooltips = LocaleMgr.screen.getString("RightAngle");

    protected boolean rightAngle;
    protected boolean freeLine;

    private Cursor cursor;
    private GraphicNode sourceNode; // may be null
    private Polygon poly = null; // if not null, indicates a line under
    // construction
    private int nbPressed;
    private int xDragged, yDragged;
    private int xFloat, yFloat; // terminal point of the floating segment
    private boolean floatSegDrawn; // indicates that a floating segment (not in

    // poly) is drawn

    public LineTool(int userId, String text, String[] tooltips, Image image, Image[] secondaryimages) {
        this(userId, text, tooltips, image, secondaryimages, false, -1);
    }

    public LineTool(int userId, String text, String[] tooltips, Image image,
            Image[] secondaryimages, boolean freeLine) {
        this(userId, text, tooltips, image, secondaryimages, freeLine, -1);
    }

    public LineTool(int userId, String text, String[] tooltips, Image image,
            Image[] secondaryimages, int defaultIndex) {
        this(userId, text, tooltips, image, secondaryimages, false, defaultIndex);
    }

    public LineTool(int userId, String text, String[] tooltips, Image image,
            Image[] secondaryimages, boolean freeLine, int defaultIndex) {
        super(userId, text, tooltips, image, secondaryimages, defaultIndex);
        this.freeLine = freeLine;
        cursor = loadDefaultCursor();
    }

    protected Cursor loadDefaultCursor() {
        return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    }

    /*
     * Methods to be overridden by subclass: if normal line, override: isSourceAcceptable,
     * isDestAcceptable, createLine (sourceNode and/or destNode may be null). if free line,
     * override: createFreeLine.
     */
    protected boolean isSourceAcceptable(GraphicNode source) {
        return false;
    }

    protected boolean isDestAcceptable(GraphicNode source, GraphicNode dest) {
        return false;
    }

    protected void createLine(GraphicNode source, GraphicNode dest, Polygon poly) {
    }

    protected void createFreeLine(Polygon poly) {
    }

    public final void mousePressed(MouseEvent e) {
        if (poly == null) {
            sourceNode = null;
            Point pt = e.getPoint();
            if (!freeLine) {
                GraphicComponent gc = model.graphicAt(view, pt.x, pt.y, 1 << Diagram.LAYER_GRAPHIC,
                        false);
                if (!(gc instanceof GraphicNode))
                    gc = null;
                if (isSourceAcceptable((GraphicNode) gc)) {
                    sourceNode = (GraphicNode) gc;
                    if (sourceNode != null)
                        pt = GraphicUtil.rectangleGetCenter(sourceNode.getRectangle());
                    updateHelp();

                } else {
                    view.toolCompleted();
                    return;
                }
            }
            rightAngle = (auxiliaryIndex == 0);
            poly = new Polygon();
            poly.addPoint(pt.x, pt.y);
            nbPressed = 1;
            floatSegDrawn = false;
            Graphics g = getGraphics();
            if (g != null) {
                paint(g); // show the initial drawing: a small rectangle in the
                // center of the source node
                g.dispose();
            }
            view.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } else {
            nbPressed++;
            mouseDragged(e);
        }
        updateHelp();
    }

    public final void mouseDragged(MouseEvent e) {
        Graphics g = getGraphics();
        if (g == null)
            return;
        paintFloatSeg(g); // hide the floating segment
        floatSegDrawn = false;
        Rectangle drawingArea = model.getDrawingArea();
        xDragged = GraphicUtil.confineXToRect(e.getX(), drawingArea);
        yDragged = GraphicUtil.confineYToRect(e.getY(), drawingArea);
        if (!scrollToVisible(xDragged, yDragged)) {
            if (sourceNode == null || poly.npoints > 1
                    || !sourceNode.getRectangle().contains(xDragged, yDragged)) {
                floatSegDrawn = true; // show the floating segment unless it is
                // the first segment and is inside the
                // source node
                xFloat = xDragged;
                yFloat = yDragged;
                if (rightAngle) { // alternate horizontal and vertical segments.
                    int i = poly.npoints - 1;
                    boolean horz = (i == 0 ? Math.abs(xDragged - poly.xpoints[i]) > Math
                            .abs(yDragged - poly.ypoints[i])
                            : poly.xpoints[i] == poly.xpoints[i - 1]);
                    if (horz)
                        yFloat = poly.ypoints[i];
                    else
                        xFloat = poly.xpoints[i];
                }
                paintFloatSeg(g);
            }
        }
        g.dispose();
    }

    public final void mouseReleased(MouseEvent e) {
        // Case 1: cursor inside the source node with an empty polygon, or
        // scrolling mode
        if (!floatSegDrawn) {
            if (poly.npoints == 1 && nbPressed > 1) {
                reset(); // cancel if 2 clicks on the same node without dragging
                view.toolCompleted();
            }
            return;
        }
        // Case 2: cursor on a graphic (may be the source node, but in this
        // case,
        // we have at least one point outside the node).
        if (!freeLine) {
            GraphicComponent gc = model.graphicAt(view, xDragged, yDragged,
                    1 << Diagram.LAYER_GRAPHIC, false);
            if (gc instanceof GraphicNode) {
                Polygon newPoly = poly; // keep a reference, reset() sets poly =
                // null
                reset();
                view.toolCompleted();
                if (!isDestAcceptable(sourceNode, (GraphicNode) gc))
                    return;
                Point pt = GraphicUtil.rectangleGetCenter(gc.getRectangle());
                if (rightAngle) {
                    int i = newPoly.npoints - 1;
                    if (sourceNode == gc) { // if recursive connector, user must
                        // draw at least 2 segments to allow
                        // us to close the figure
                        if (i < 2)
                            return;
                        if (i == 2) {
                            if (newPoly.xpoints[i] == newPoly.xpoints[i - 1])
                                newPoly.addPoint(pt.x, newPoly.ypoints[i]);
                            else
                                newPoly.addPoint(newPoly.xpoints[i], pt.y);
                            i++;
                        }
                    }
                    if (i == 0) { // add a vertex, a right angle connector has
                        // at least 2 segments.
                        newPoly.addPoint(newPoly.xpoints[i], pt.y);
                    } else {
                        if (newPoly.xpoints[i] == newPoly.xpoints[i - 1])
                            newPoly.ypoints[i] = pt.y;
                        else
                            newPoly.xpoints[i] = pt.x;
                    }
                }
                newPoly.addPoint(pt.x, pt.y);
                createLine(sourceNode, (GraphicNode) gc, newPoly);
                return;
            }
        }
        // Case 3: cursor not on a graphic: add a point to the polygon.
        int delta = view.getHandleSize();
        int i = poly.npoints - 1;
        boolean newPoint = (Math.abs(xFloat - poly.xpoints[i]) > delta || Math.abs(yFloat
                - poly.ypoints[i]) > delta);
        if (!newPoint) { // If double-click on the same point, close the line.
            Polygon newPoly = poly; // keep a reference, reset() sets poly =
            // null
            reset();
            view.toolCompleted();
            if (newPoly.npoints > 1) {
                if (freeLine)
                    createFreeLine(newPoly);
                else if (isDestAcceptable(sourceNode, null))
                    createLine(sourceNode, null, newPoly);
            }
            return;
        }
        Graphics g = getGraphics();
        if (g != null) {
            paint(g); // hide the previous drawing
            floatSegDrawn = false;
            poly.addPoint(xFloat, yFloat);
            paint(g); // show the new polygon
            g.dispose();
        }
    }

    public final void paint(Graphics g) {
        if (poly == null)
            return;
        Line.drawSelectedLine(g, view, poly, true);
        paintFloatSeg(g);
    }

    public final void reset() {
        if (poly == null)
            return;
        Graphics g = getGraphics();
        if (g != null) {
            getDiagramView().areaDamaged(poly.getBounds());
            paint(g);
            g.dispose();
        }
        view.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        poly = null;
        updateHelp();
    }

    public final void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            reset();
            view.toolCompleted();
        }
    }

    private final void paintFloatSeg(Graphics g) {
        if (!floatSegDrawn)
            return;
        g.setColor(Color.red);
        float zoomFactor = view.getZoomFactor();
        int i = poly.npoints - 1;
        g.drawLine((int) (poly.xpoints[i] * zoomFactor), (int) (poly.ypoints[i] * zoomFactor),
                (int) (xFloat * zoomFactor), (int) (yFloat * zoomFactor));
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void updateHelp() {
        String help = null;
        if (poly != null) {
            String escape = KeyEvent.getKeyText(KeyEvent.VK_ESCAPE);
            help = MessageFormat.format(HELP_STEP1, escape);
        }
        if (help == null) {
            help = HELP_STEP0;
        }
        setHelpText(help);
    }

}
