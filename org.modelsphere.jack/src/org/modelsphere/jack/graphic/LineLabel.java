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

import org.modelsphere.jack.graphic.shape.GraphicShape;

public class LineLabel extends ZoneBox {

    private static final int LABEL_MARGIN = 10;

    protected int xOffset = 0;
    protected int yOffset = 0;
    protected int maxWidth = 0;
    protected Line line;
    protected LineLabel nextLabel = null;

    public LineLabel(Diagram diagram, Line line, GraphicShape shape) {
        super(diagram, shape);
        setLineColor(Color.white);
        this.line = line;
    }

    public final void setOffset(Point pt) {
        if (pt == null) {
            xOffset = yOffset = 0;
        } else {
            xOffset = pt.x;
            yOffset = pt.y;
        }
    }

    public final int getMaxWidth() {
        return maxWidth;
    }

    public final void setMaxWidth(int width) {
        maxWidth = width;
    }

    public final Line getLine() {
        return line;
    }

    public final LineLabel getNextLabel() {
        return nextLabel;
    }

    public final void setNextLabel(LineLabel nextLabel) {
        this.nextLabel = nextLabel;
    }

    public final void pushNextLabel(LineLabel nextLabel) {
        if (this.nextLabel == null)
            this.nextLabel = nextLabel;
        else
            this.nextLabel.pushNextLabel(nextLabel);
    }

    public void paint(Graphics g, DiagramView diagView, int drawingMode, int renderingFlags) {
        if (drawingMode != GraphicComponent.DRAW_FRAME)
            super.paint(g, diagView, drawingMode, renderingFlags);
    }

    public final void paintSelHandles(Graphics g, DiagramView diagView) {
        Rectangle rectangle = getRectangle();
        Rectangle rect = diagView.zoom(rectangle);
        g.setColor(Color.gray);
        g.drawRect(rect.x, rect.y, rect.width, rect.height);
        if (maxWidth != 0) {
            g.setColor(Color.black);
            drawHandle(g, rect.x + rect.width + GraphicComponent.HANDLE_SIZE / 2, rect.y
                    + rect.height / 2);
        }
        paintZonesSelection(g, diagView);
    }

    public final int handleAt(DiagramView diagView, int x, int y) {
        if (maxWidth != 0) {
            Rectangle rectangle = getRectangle();
            int d = diagView.getHandleSize();
            if (x >= rectangle.x + rectangle.width && x <= rectangle.x + rectangle.width + d) {
                int mid = rectangle.y + (rectangle.height - d) / 2;
                if (y >= mid && y <= mid + d)
                    return GraphicComponent.CENTER_RIGHT;
            }
        }
        return -1;
    }

    // Compute the position of all the line labels of the chain.
    // Assume line end positions already computed.
    public final void setPosition(Graphics g, int where) {
        Polygon poly = line.getPoly();
        if (where == Line.AT_CENTER) {
            int i1 = 0;
            int i2 = poly.npoints - 1;
            Point pt1 = new Point(poly.xpoints[i1], poly.ypoints[i1]);
            Point pt2 = new Point(poly.xpoints[i2], poly.ypoints[i2]);
            if (line.getNode1() != null) {
                Rectangle rect = line.getNode1().getRectangle();
                int ind = GraphicUtil.getFirstOutPointIndex(poly, rect, false);
                if (ind != -1) {
                    i1 = ind - 1;
                    pt1 = GraphicUtil.getIntersectionPoint(poly.xpoints[i1], poly.ypoints[i1],
                            poly.xpoints[ind], poly.ypoints[ind], rect);
                }
            }
            if (line.getNode2() != null) {
                Rectangle rect = line.getNode2().getRectangle();
                int ind = GraphicUtil.getFirstOutPointIndex(poly, rect, true);
                if (ind >= i1) {
                    i2 = ind + 1;
                    pt2 = GraphicUtil.getIntersectionPoint(poly.xpoints[i2], poly.ypoints[i2],
                            poly.xpoints[ind], poly.ypoints[ind], rect);
                }
            }
            Polygon newPoly = new Polygon();
            newPoly.addPoint(pt1.x, pt1.y);
            while (++i1 < i2)
                newPoly.addPoint(poly.xpoints[i1], poly.ypoints[i1]);
            newPoly.addPoint(pt2.x, pt2.y);
            Point pos = GraphicUtil.getLineCenter(newPoly);
            setAtCenter(g, pos.x, pos.y, true);
        } else {
            boolean reverse = (where == Line.AT_END2);
            GraphicNode node = (reverse ? line.getNode2() : line.getNode1());
            Rectangle growingRect;
            if (node == null) {
                int ind = (reverse ? poly.npoints - 1 : 0);
                growingRect = new Rectangle(poly.xpoints[ind], poly.ypoints[ind], 0, 0);
            } else {
                growingRect = new Rectangle(node.getRectangle());
            }
            // outInd is the first point outside the rectangle before growing;
            // if growing the rectangle swallows the entire connector (frequent
            // case with reflexive connector)
            // use this point to calculate the label position.
            int outInd = GraphicUtil.getFirstOutPointIndex(poly, growingRect, reverse);
            LineEnd end = (reverse ? line.getEnd2() : line.getEnd1());
            if (end != null) {
                Rectangle rect = end.getRectangle();
                if (rect != null)
                    growingRect.add(rect);
            }
            setAtEnd(g, growingRect, poly, outInd, reverse);
        }
    }

    private void setAtCenter(Graphics g, int x, int y, boolean first) {
        computePositionData(g);
        Dimension dim = getPreferredSize();
        if (first)
            y -= (nextLabel != null ? dim.height + LABEL_MARGIN / 2 : dim.height / 2);
        setRectangle(new Point(x, y + dim.height / 2), dim);
        if (nextLabel != null)
            nextLabel.setAtCenter(g, x, y + dim.height + LABEL_MARGIN, false);
    }

    private void setAtEnd(Graphics g, Rectangle growingRect, Polygon poly, int outInd,
            boolean reverse) {
        computePositionData(g);
        Dimension dim = getPreferredSize();
        Point pos;
        if (outInd == -1) {
            pos = GraphicUtil.rectangleGetCenter(growingRect);
        } else {
            growingRect.grow(LABEL_MARGIN + dim.width / 2, LABEL_MARGIN + dim.height / 2);
            int i = GraphicUtil.getFirstOutPointIndex(poly, growingRect, reverse);
            if (i != -1)
                outInd = i;
            int inInd = (reverse ? outInd + 1 : outInd - 1);
            pos = GraphicUtil.getIntersectionPoint(poly.xpoints[inInd], poly.ypoints[inInd],
                    poly.xpoints[outInd], poly.ypoints[outInd], growingRect);
            growingRect.grow(dim.width / 2, dim.height / 2);
        }
        setRectangle(pos, dim);
        if (nextLabel != null)
            nextLabel.setAtEnd(g, growingRect, poly, outInd, reverse);
    }

    protected void setRectangle(Point pos, Dimension dim) {
        Rectangle drawingArea = diagram.getDrawingArea();
        int x = GraphicUtil.confineXToRect(pos.x + xOffset, drawingArea);
        int y = GraphicUtil.confineYToRect(pos.y + yOffset, drawingArea);
        setRectangle(new Rectangle(x - dim.width / 2, y - dim.height / 2, dim.width, dim.height));
    }

    public final int getLayer() {
        return Diagram.LAYER_LINE_LABEL;
    }
}
