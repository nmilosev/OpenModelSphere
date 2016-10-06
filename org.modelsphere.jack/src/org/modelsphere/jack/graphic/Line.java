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

package org.modelsphere.jack.graphic;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;

public class Line extends GraphicComponent {

    // possible positions of line labels
    public static final int AT_END1 = 0;
    public static final int AT_END2 = 1;
    public static final int AT_CENTER = 2;

    protected Polygon poly;
    protected GraphicNode node1 = null; // node at first point of this line
    protected GraphicNode node2 = null; // node at last point of this line
    protected LineEnd end1 = null; // line end chain at node1 extremity
    protected LineEnd end2 = null; // line end chain at node2 extremity
    protected LineEndZone lineEndZone1 = null; // zone over LineEnd
    protected LineEndZone lineEndZone2 = null; // zone over LineEnd
    protected LineLabel label1 = null; // line label chain at node1 extremity
    protected LineLabel label2 = null; // line label chain at node2 extremity
    protected LineLabel centerLabel = null; // line label chain at center
    protected boolean rightAngle = false;
    protected boolean freeLine;

    public Line(Diagram diagram, GraphicNode node1, GraphicNode node2, Polygon poly) {
        super(diagram, null);
        this.node1 = node1;
        if (node1 != null)
            node1.addLine1(this);
        this.node2 = node2;
        if (node2 != null)
            node2.addLine2(this);
        setPoly(poly);
        freeLine = false;
    }

    public Line(Diagram diagram, Polygon poly) {
        super(diagram, null);
        setPoly(poly);
        freeLine = true;
    }

    // BEWARE: overriding methods must call super.delete as last action
    public void delete(boolean all) {
        if (!all) {
            LineLabel label;
            for (label = label1; label != null; label = label.getNextLabel())
                label.delete(all);
            for (label = label2; label != null; label = label.getNextLabel())
                label.delete(all);
            for (label = centerLabel; label != null; label = label.getNextLabel())
                label.delete(all);
            if (lineEndZone1 != null)
                lineEndZone1.delete(all);
            if (lineEndZone2 != null)
                lineEndZone2.delete(all);
            if (node1 != null)
                node1.removeLine1(this);
            if (node2 != null)
                node2.removeLine2(this);
        }
        node1 = node2 = null;
        super.delete(all);
    }

    public final Polygon getPoly() {
        return poly;
    }

    public final void setPoly(Polygon poly) {
        this.poly = poly;
        diagram.setComputePos(this);
    }

    public final boolean isRightAngle() {
        return rightAngle;
    }

    public final void setRightAngle(boolean state) {
        rightAngle = state;
    }

    public final boolean isFreeLine() {
        return freeLine;
    }

    // overriden
    public boolean isStandAloneSupported() {
        return false;
    }

    public final GraphicNode getNode1() {
        return node1;
    }

    public final void setNode1(GraphicNode node) {
        if (node1 != null)
            node1.removeLine1(this);
        node1 = node;
        if (node1 != null)
            node1.addLine1(this);
        diagram.setComputePos(this);
    }

    public final GraphicNode getNode2() {
        return node2;
    }

    public final void setNode2(GraphicNode node) {
        if (node2 != null)
            node2.removeLine2(this);
        node2 = node;
        if (node2 != null)
            node2.addLine2(this);
        diagram.setComputePos(this);
    }

    public final LineEnd getEnd1() {
        return end1;
    }

    public final void setEnd1(LineEnd end) {
        end1 = end;
    }

    public final LineEnd getEnd2() {
        return end2;
    }

    public final void setEnd2(LineEnd end) {
        end2 = end;
    }

    public final LineLabel getLabel1() {
        return label1;
    }

    public final void setLabel1(LineLabel label) {
        label1 = label;
    }

    public final LineLabel getLabel2() {
        return label2;
    }

    public final void setLabel2(LineLabel label) {
        label2 = label;
    }

    public final LineLabel getCenterLabel() {
        return centerLabel;
    }

    public final void setCenterLabel(LineLabel label) {
        centerLabel = label;
    }

    public final LineEndZone getLineEndZone1() {
        return lineEndZone1;
    }

    public final void setLineEndZone1(LineEndZone zone) {
        lineEndZone1 = zone;
    }

    public final LineEndZone getLineEndZone2() {
        return lineEndZone2;
    }

    public final void setLineEndZone2(LineEndZone zone) {
        lineEndZone2 = zone;
    }

    // BEWARE: diagView is null for printing
    public final void paint(Graphics g, DiagramView diagView, int drawingMode, int renderingFlags) {
        Polygon zoomPoly = (diagView == null ? poly : diagView.zoom(poly));
        Graphics2D g2D = (Graphics2D) g;
        Stroke oldStroke = g2D.getStroke();
        BasicStroke newStroke = getLineStroke();

        g2D.setStroke(newStroke);
        g2D.setColor(lineColor);
        g2D.drawPolyline(zoomPoly.xpoints, zoomPoly.ypoints, zoomPoly.npoints);

        if (end1 != null || end2 != null) { // do not dash line end figures
            if (lineStyle == LINE_DASHED || lineStyle == LINE_DASHED_BOLD)
                g2D.setStroke(new BasicStroke(newStroke.getLineWidth()));
            if (end1 != null)
                end1.paint(g2D, diagView);
            if (end2 != null)
                end2.paint(g2D, diagView);
        }
        g2D.setStroke(oldStroke);

        // clear area under the node's shape
        Composite composite = g2D.getComposite();
        if ((renderingFlags & RenderingFlags.ERASE_BACKGROUND) == 0) {
            g2D.setColor(Color.WHITE);
        } else {
            g2D.setComposite(AlphaComposite.Clear);
        }
        // Clear the area under the nodes. This solution is temporary.
        // TODO: Ideally, this should be delegated to the specific nodes or
        // if we could determine the intersections with the shapes, we could
        // restrict
        // the drawing of the line outside the shapes. This would require
        // specific
        // new methods on the GraphicShape API.
        if (node1 != null) {
            Rectangle rect = node1.getRectangle();
            if (rect != null) {
                rect = diagView == null ? rect : diagView.zoom(rect);
                rect = new Rectangle(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
                g2D.fillRect(rect.x, rect.y, rect.width, rect.height);
            }
        }
        if (node2 != null) {
            Rectangle rect = node2.getRectangle();
            if (rect != null) {
                rect = diagView == null ? rect : diagView.zoom(rect);
                rect = new Rectangle(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
                g2D.fillRect(rect.x, rect.y, rect.width, rect.height);
            }
        }

        g2D.setComposite(composite);
    }

    public final void paintSelHandles(Graphics g, DiagramView diagView) {
        drawSelectedLine(g, diagView, poly, false);
    }

    public static void drawSelectedLine(Graphics g, DiagramView diagView, Polygon poly,
            boolean constructing) {
        int overlapHandleGrow = 2;
        g.setColor(constructing ? Color.red : Color.gray);
        Polygon zoomPoly = diagView.zoom(poly);
        if (zoomPoly.npoints > 1)
            g.drawPolyline(zoomPoly.xpoints, zoomPoly.ypoints, zoomPoly.npoints);
        // Draw a little rectangle on each point of the line; rectangle size is not affected by zoom
        int delta = GraphicComponent.HANDLE_SIZE / 2;
        if (!constructing)
            g.setColor(Color.black);
        ArrayList<Point> pts = new ArrayList<Point>();
        for (int i = 0; i < zoomPoly.npoints; i++) {
            int x = zoomPoly.xpoints[i];
            int y = zoomPoly.ypoints[i];
            if (pts.contains(new Point(x, y)))
                g.fillRect(x - (delta + overlapHandleGrow / 2),
                        y - (delta + overlapHandleGrow / 2), GraphicComponent.HANDLE_SIZE
                                + overlapHandleGrow, GraphicComponent.HANDLE_SIZE
                                + overlapHandleGrow);
            else
                g.fillRect(x - delta, y - delta, GraphicComponent.HANDLE_SIZE,
                        GraphicComponent.HANDLE_SIZE);
            pts.add(new Point(x, y));
        }
    }

    public final boolean contains(DiagramView diagView, int x, int y) {
        return (segmentAt(diagView, x, y) != -1);
    }

    // return -1 if point <x,y> not on the line
    public final int segmentAt(DiagramView diagView, int x, int y) {
        int delta = diagView.getHandleSize() / 2;
        Rectangle rectangle = getRectangle();

        if (rectangle == null)
            return -1;

        // Quick test: is the point in the bounding rectangle of the line ?
        if (x < rectangle.x - delta || x > rectangle.x + rectangle.width + delta
                || y < rectangle.y - delta || y > rectangle.y + rectangle.height + delta)
            return -1;
        for (int i = 0; i < poly.npoints - 1; i++) {
            int dx = poly.xpoints[i + 1] - poly.xpoints[i];
            int dy = poly.ypoints[i + 1] - poly.ypoints[i];
            // Is the point in the bounding rectangle of this segment ?
            if (!(dx > 0 ? x >= poly.xpoints[i] - delta && x <= poly.xpoints[i + 1] + delta
                    : x >= poly.xpoints[i + 1] - delta && x <= poly.xpoints[i] + delta))
                continue;
            if (!(dy > 0 ? y >= poly.ypoints[i] - delta && y <= poly.ypoints[i + 1] + delta
                    : y >= poly.ypoints[i + 1] - delta && y <= poly.ypoints[i] + delta))
                continue;
            if (dx == 0 || dy == 0) // If horz or vert, stop here.
                return i;
            int d;
            if (Math.abs(dx) > Math.abs(dy))
                d = poly.ypoints[i] + (int) ((x - poly.xpoints[i]) * ((float) dy / dx)) - y;
            else
                d = poly.xpoints[i] + (int) ((y - poly.ypoints[i]) * ((float) dx / dy)) - x;
            if (Math.abs(d) <= delta)
                return i;
        }
        return -1;
    }

    // If <x,y> is on a vertex point of the line, return the index of this
    // point; otherwise return -1.
    // Parm <iseg> is the result of <segmentAt>.
    public final int pointAt(DiagramView diagView, int x, int y, int iseg) {
        if (iseg == -1)
            return -1;
        int delta = diagView.getHandleSize() / 2;
        if (Math.abs(poly.xpoints[iseg] - x) <= delta && Math.abs(poly.ypoints[iseg] - y) <= delta)
            return iseg;
        iseg++;
        if (Math.abs(poly.xpoints[iseg] - x) <= delta && Math.abs(poly.ypoints[iseg] - y) <= delta)
            return iseg;
        return -1;
    }

    public final int handleAt(DiagramView diagView, int x, int y) {
        return -1;
    }

    // Return true if the line begins with a horizontal segment, false if
    // vertical segment
    // Works with any line (right-angle or not); skips zero-length segments.
    public static boolean startHorz(Polygon poly) {
        for (int i = 1; i < poly.npoints; i++) {
            int dx = Math.abs(poly.xpoints[i] - poly.xpoints[i - 1]);
            int dy = Math.abs(poly.ypoints[i] - poly.ypoints[i - 1]);
            if (dx != dy)
                return ((i & 1) == 0 ? dx < dy : dx > dy);
        }
        return true;
    }

    final void computePositionData(Graphics g) {
        Rectangle rect = GraphicUtil.getBounds(poly);
        Rectangle end1Rect = null;
        Rectangle end2Rect = null;
        if (end1 != null) {
            end1.setPosition(this, AT_END1);
            end1Rect = end1.getRectangle();
            if (end1Rect != null)
                rect.add(end1Rect);
        }
        if (end2 != null) {
            end2.setPosition(this, AT_END2);
            end2Rect = end2.getRectangle();
            if (end2Rect != null)
                rect.add(end2Rect);
        }
        setRectangle(rect);

        if (lineEndZone1 != null) {
            if (end1Rect == null)
                end1Rect = new Rectangle(poly.xpoints[0], poly.ypoints[0], 0, 0);
            lineEndZone1.setRectangle(end1Rect);
        }
        if (lineEndZone2 != null) {
            if (end2Rect == null)
                end2Rect = new Rectangle(poly.xpoints[poly.npoints - 1],
                        poly.ypoints[poly.npoints - 1], 0, 0);
            lineEndZone2.setRectangle(end2Rect);
        }

        if (label1 != null)
            label1.setPosition(g, AT_END1);
        if (label2 != null)
            label2.setPosition(g, AT_END2);
        if (centerLabel != null)
            centerLabel.setPosition(g, AT_CENTER);
    }

    public final int getLayer() {
        return Diagram.LAYER_LINE;
    }
}
