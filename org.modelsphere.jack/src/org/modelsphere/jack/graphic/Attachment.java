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

import org.modelsphere.jack.graphic.shape.RectangleShape;

public class Attachment extends ZoneBox {

    private static final int X_DEFAULT = -100; // not a linear coordinate
    private static final int Y_DEFAULT = 104; // not a linear coordinate

    protected int xOffset = X_DEFAULT;
    protected int yOffset = Y_DEFAULT;
    protected int maxWidth = 0;
    protected GraphicNode node;

    public Attachment(Diagram diagram, GraphicNode node) {
        super(diagram, RectangleShape.singleton);
        setLineColor(Color.white);
        this.node = node;
        node.addAttachment(this);
    }

    // BEWARE: overriding methods must call super.delete as last action
    public void delete(boolean all) {
        if (!all)
            node.removeAttachment(this);
        node = null;
        super.delete(all);
    }

    public final void setOffset(Point pt) {
        if (pt == null) {
            xOffset = X_DEFAULT;
            yOffset = Y_DEFAULT;
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

    public final void paint(Graphics g, DiagramView diagView, int drawingMode, int renderingFlags) {
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
        Rectangle rectangle = getRectangle();
        if (maxWidth != 0) {
            int d = diagView.getHandleSize();
            if (x >= rectangle.x + rectangle.width && x <= rectangle.x + rectangle.width + d) {
                int mid = rectangle.y + (rectangle.height - d) / 2;
                if (y >= mid && y <= mid + d)
                    return GraphicComponent.CENTER_RIGHT;
            }
        }
        return -1;
    }

    final void computePositionData(Graphics g) {
        super.computePositionData(g);
        Dimension dim = getPreferredSize();
        Rectangle nodeRect = node.getRectangle();
        int x = offsetToCoord(xOffset, nodeRect.x, nodeRect.x + nodeRect.width);
        int y = offsetToCoord(yOffset, nodeRect.y, nodeRect.y + nodeRect.height);
        Rectangle rect = new Rectangle(x, y, dim.width, dim.height);
        GraphicUtil.confineCenterToRect(rect, diagram.getDrawingArea());
        setRectangle(rect);
    }

    protected final int coordToOffset(int coord, int low, int high) {
        if (coord <= low)
            return coord - low - 100;
        if (coord >= high)
            return coord - high + 100;
        int mid = (low + high) / 2;
        return (coord - mid) * 100 / (high - mid);
    }

    private int offsetToCoord(int offset, int low, int high) {
        if (offset >= 100)
            return high + offset - 100;
        if (offset <= -100)
            return low + offset + 100;
        int mid = (low + high) / 2;
        return mid + (high - mid) * offset / 100;
    }
}
