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

package org.modelsphere.jack.graphic.shape;

import java.awt.*;

import org.modelsphere.jack.graphic.*;

public class MeriseProcessShape implements GraphicShape {

    public static final MeriseProcessShape singleton = new MeriseProcessShape();

    public final void paint(Graphics g, DiagramView diagView, GraphicComponent gc,
            int renderingFlags) {
        Rectangle rect = new Rectangle(gc.getRectangle());
        if (diagView != null)
            rect = diagView.zoom(rect);
        int arrowHeight = getArrowHeight(rect);
        Polygon poly = getArrowPolygon(rect, arrowHeight);
        rect.y += arrowHeight;
        rect.height -= arrowHeight;

        Graphics2D g2D = (Graphics2D) g;
        Stroke oldStroke = g2D.getStroke();
        g2D.setStroke(gc.getLineStroke());
        g2D.setColor(gc.getFillColor());
        g2D.fillPolygon(poly);
        g2D.fillRect(rect.x, rect.y, rect.width, rect.height);
        g2D.setColor(gc.getLineColor());
        g2D.drawPolygon(poly);
        g2D.drawRect(rect.x, rect.y, rect.width, rect.height);
        g2D.setStroke(oldStroke);
    }

    // Upon entry, we already checked that <x,y> is inside the rectangle.
    public final boolean contains(GraphicComponent gc, int x, int y) {
        Rectangle rect = gc.getRectangle();
        int arrowHeight = getArrowHeight(rect);
        if (y >= rect.y + arrowHeight)
            return true;
        Polygon poly = getArrowPolygon(rect, arrowHeight);
        return poly.contains(x, y);
    }

    public final Rectangle getContentRect(GraphicComponent gc) {
        Rectangle rect = gc.getRectangle();
        int arrowHeight = getArrowHeight(rect);
        int inset = (int) gc.getLineStroke().getLineWidth();
        return new Rectangle(rect.x + inset, rect.y + arrowHeight + inset, Math.max(0, rect.width
                - 2 * inset), Math.max(0, rect.height - arrowHeight - 2 * inset));
    }

    public final Dimension getShapeSize(GraphicComponent gc, Dimension contentSize) {
        int inset = (int) gc.getLineStroke().getLineWidth();
        int width = contentSize.width + 2 * inset;
        int height = contentSize.height + 2 * inset;
        int arrowHeight = Math.min(width / 3, height / 2);
        return new Dimension(width, height + arrowHeight);
    }

    private int getArrowHeight(Rectangle rect) {
        return Math.min(rect.width / 3, rect.height / 3);
    }

    private Polygon getArrowPolygon(Rectangle rect, int arrowHeight) {
        Polygon poly = new Polygon();
        int midX = rect.x + rect.width / 2;
        poly.addPoint(midX - rect.width / 3, rect.y);
        poly.addPoint(midX, rect.y + arrowHeight);
        poly.addPoint(midX + rect.width / 3, rect.y);
        return poly;
    }
}
