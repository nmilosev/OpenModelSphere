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

import java.awt.*;

public class LineEnd {

    public static final int DEFAULT_WIDTH = 16;
    public static final int DEFAULT_HEIGHT = 12;

    protected int[] xs;
    protected int[] ys;
    protected int height; // distance for the next chained line end.
    protected Color fillColor; // null means open polygon
    protected Polygon poly = null;
    protected LineEnd nextEnd = null;

    // The coordinate values must represent the figure on an horizontal line
    // pointing to left and terminating at the origin.
    // The height is the extent of the figure on the line,
    // and the width is the extent of the figure perpendicularly to the line.
    public static LineEnd createArrowLineEnd(Color fillColor) {
        return new LineEnd(new int[] { DEFAULT_HEIGHT, 0, DEFAULT_HEIGHT }, new int[] {
                -DEFAULT_WIDTH / 2, 0, DEFAULT_WIDTH / 2 }, DEFAULT_HEIGHT, fillColor);
    }

    public static LineEnd createDoubleArrowLineEnd(Color fillColor) {
        LineEnd e = LineEnd.createArrowLineEnd(fillColor);
        e.pushNextEnd(LineEnd.createArrowLineEnd(fillColor));
        return e;
    }

    public static LineEnd createDiamondLineEnd(Color fillColor) {
        return new LineEnd(new int[] { 0, DEFAULT_HEIGHT, DEFAULT_HEIGHT * 2, DEFAULT_HEIGHT },
                new int[] { 0, -DEFAULT_WIDTH / 2, 0, DEFAULT_WIDTH / 2 }, DEFAULT_HEIGHT * 2,
                fillColor);
    }

    public static LineEnd createSmallDiamondLineEnd(Color fillColor) {
        return new LineEnd(new int[] { DEFAULT_HEIGHT / 2, DEFAULT_HEIGHT, DEFAULT_HEIGHT * 3 / 2,
                DEFAULT_HEIGHT }, new int[] { 0, -DEFAULT_WIDTH / 4, 0, DEFAULT_WIDTH / 4 },
                DEFAULT_HEIGHT * 2, fillColor);
    }

    public static LineEnd createHalfDiamondLineEnd(Color fillColor) {
        return new LineEnd(new int[] { -DEFAULT_HEIGHT * 3 / 2, 0, DEFAULT_HEIGHT * 3 / 2, 0,
                -DEFAULT_HEIGHT * 3 / 2 }, new int[] { 0, -DEFAULT_WIDTH / 2, 0, DEFAULT_WIDTH / 2,
                0 }, DEFAULT_HEIGHT * 3 / 2, fillColor);
    }

    public static LineEnd createSquareLineEnd(Color fillColor) {
        return new LineEnd(new int[] { DEFAULT_HEIGHT / 2, DEFAULT_HEIGHT / 2, DEFAULT_HEIGHT,
                DEFAULT_HEIGHT, DEFAULT_HEIGHT / 2, DEFAULT_HEIGHT / 2 }, new int[] { 0,
                -DEFAULT_WIDTH / 4, -DEFAULT_WIDTH / 4, DEFAULT_WIDTH / 4, DEFAULT_WIDTH / 4, 0 },
                DEFAULT_HEIGHT * 3 / 2, fillColor);
    }

    public static LineEnd createHalfSquareLineEnd(Color fillColor) {
        return new LineEnd(new int[] { -DEFAULT_HEIGHT, DEFAULT_HEIGHT, DEFAULT_HEIGHT,
                -DEFAULT_HEIGHT, -DEFAULT_HEIGHT }, new int[] { -DEFAULT_WIDTH * 4 / 8,
                -DEFAULT_WIDTH * 4 / 8, DEFAULT_WIDTH * 4 / 8, DEFAULT_WIDTH * 4 / 8,
                -DEFAULT_WIDTH * 4 / 8 }, DEFAULT_HEIGHT, fillColor);
    }

    public static LineEnd createCrossLineEnd() {
        return new LineEnd(new int[] { DEFAULT_HEIGHT * 2 / 6, DEFAULT_HEIGHT * 2 / 6 }, new int[] {
                -DEFAULT_WIDTH * 4 / 9, DEFAULT_WIDTH * 4 / 9 }, DEFAULT_HEIGHT * 3 / 6, null);
    }

    public static LineEnd createCursorLineEnd() {
        return new LineEnd(new int[] { DEFAULT_HEIGHT * 3 / 4, DEFAULT_HEIGHT * 3 / 4,
                DEFAULT_HEIGHT / 2, DEFAULT_HEIGHT, DEFAULT_HEIGHT * 3 / 4, DEFAULT_HEIGHT * 3 / 4,
                DEFAULT_HEIGHT / 2, DEFAULT_HEIGHT }, new int[] { 0, -DEFAULT_WIDTH / 2,
                -DEFAULT_WIDTH / 2, -DEFAULT_WIDTH / 2, -DEFAULT_WIDTH / 2, DEFAULT_WIDTH / 2,
                DEFAULT_WIDTH / 2, DEFAULT_WIDTH / 2 }, DEFAULT_HEIGHT * 3 / 2, null);
    }

    public LineEnd(int[] xs, int[] ys, int height, Color fillColor) {
        this.xs = xs;
        this.ys = ys;
        this.height = height;
        this.fillColor = fillColor;
    }

    public final void pushNextEnd(LineEnd nextEnd) {
        if (this.nextEnd == null)
            this.nextEnd = nextEnd;
        else
            this.nextEnd.pushNextEnd(nextEnd);
    }

    // Perform a rotation and a translation on all the line ends of the chain,
    // to position them at the connection point of the Line with the Node.
    // Must be called before getRectangle() and draw()
    public final void setPosition(Line line, int where) {
        poly = null;
        Polygon linePoly = line.getPoly();
        int inInd, outInd;
        Point pos;
        if (where == Line.AT_END1) {
            if (line.getNode1() == null) {
                inInd = 0;
                outInd = 1;
                pos = new Point(linePoly.xpoints[0], linePoly.ypoints[0]);
            } else {
                Rectangle nodeRect = line.getNode1().getRectangle();
                outInd = GraphicUtil.getFirstOutPointIndex(linePoly, nodeRect, false);
                if (outInd == -1)
                    return;
                inInd = outInd - 1;
                pos = GraphicUtil.getIntersectionPoint(linePoly.xpoints[inInd],
                        linePoly.ypoints[inInd], linePoly.xpoints[outInd],
                        linePoly.ypoints[outInd], nodeRect);
            }
        } else {
            if (line.getNode2() == null) {
                inInd = linePoly.npoints - 1;
                outInd = inInd - 1;
                pos = new Point(linePoly.xpoints[inInd], linePoly.ypoints[inInd]);
            } else {
                Rectangle nodeRect = line.getNode2().getRectangle();
                outInd = GraphicUtil.getFirstOutPointIndex(linePoly, nodeRect, true);
                if (outInd == -1)
                    return;
                inInd = outInd + 1;
                pos = GraphicUtil.getIntersectionPoint(linePoly.xpoints[inInd],
                        linePoly.ypoints[inInd], linePoly.xpoints[outInd],
                        linePoly.ypoints[outInd], nodeRect);
            }
        }
        double theta = Math.atan2(linePoly.ypoints[outInd] - linePoly.ypoints[inInd],
                linePoly.xpoints[outInd] - linePoly.xpoints[inInd]);
        setPosition(theta, 0, pos.x, pos.y);
    }

    private final void setPosition(double theta, int distance, int x, int y) {
        poly = new Polygon(xs, ys, xs.length);
        poly.translate(distance, 0);
        GraphicUtil.rotate(poly, theta);
        poly.translate(x, y);
        if (nextEnd != null)
            nextEnd.setPosition(theta, distance + height, x, y);
    }

    // Get the bounding rectangle of all the line ends of the chain.
    public final Rectangle getRectangle() {
        if (poly == null)
            return null;
        return addRectangle(null);
    }

    private final Rectangle addRectangle(Rectangle rect) {
        if (rect == null)
            rect = getRect();
        else
            rect.add(getRect());
        if (nextEnd != null)
            rect = nextEnd.addRectangle(rect);
        return rect;
    }

    // Draw all the line ends of the chain.
    // BEWARE: diagView is null for printing
    public final void paint(Graphics g, DiagramView diagView) {
        if (poly == null)
            return;
        draw(g, diagView);
        if (nextEnd != null)
            nextEnd.paint(g, diagView);
    }

    // Overridden
    protected Rectangle getRect() {
        return GraphicUtil.getBounds(poly);
    }

    // Overridden
    protected void draw(Graphics g, DiagramView diagView) {
        Polygon zoomPoly = (diagView == null ? poly : diagView.zoom(poly));
        if (fillColor != null) {
            Color lineColor = g.getColor();
            g.setColor(fillColor);
            g.fillPolygon(zoomPoly);
            g.setColor(lineColor);
            g.drawPolygon(zoomPoly);
        } else {
            g.drawPolyline(zoomPoly.xpoints, zoomPoly.ypoints, zoomPoly.npoints);
        }
    }
}
