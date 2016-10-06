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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicComponent;

public class UmlComponentShape implements GraphicShape {

    public static final int NORMAL_FRAME = 0;

    public static final GraphicShape singleton = new UmlComponentShape(NORMAL_FRAME);

    private int frame;

    public UmlComponentShape(int frame) {
        this.frame = frame;
    }

    private static final int GAP = 15;

    private int getGap(DiagramView diagView) {
        double zoomFactor = diagView == null ? 1.0 : diagView.getZoomFactor();
        int gap = (int) (GAP * zoomFactor);
        return gap;
    } // getGap()

    public final void paint(Graphics g, DiagramView diagView, GraphicComponent gc,
            int renderingFlags) {
        Rectangle rect = gc.getRectangle();
        if (diagView != null)
            rect = diagView.zoom(rect);
        Graphics2D g2D = (Graphics2D) g;
        Stroke oldStroke = g2D.getStroke();

        // fill the main rectangle
        int gap = getGap(diagView);
        g2D.setColor(Color.WHITE);
        g2D.fillRect(rect.x + gap, rect.y, rect.width - gap, rect.height);

        // draw the border of the main rectangle
        g2D.setStroke(gc.getLineStroke());
        g2D.setColor(gc.getLineColor());
        g2D.drawRect(rect.x + gap, rect.y, rect.width - gap, rect.height);

        // fill the two small rectangles
        int y1 = (int) (rect.height * 0.3);
        int y2 = (int) (rect.height * 0.4);
        int y3 = (int) (rect.height * 0.6);
        int y4 = (int) (rect.height * 0.7);
        g2D.setColor(gc.getFillColor());
        g2D.fillRect(rect.x, rect.y + y1, gap * 2, y2 - y1);
        g2D.fillRect(rect.x, rect.y + y3, gap * 2, y4 - y3);

        // draw the border of the two small rectangles
        g2D.setColor(gc.getLineColor());
        g2D.drawRect(rect.x, rect.y + y1, gap * 2, y2 - y1);
        g2D.drawRect(rect.x, rect.y + y3, gap * 2, y4 - y3);

        g2D.setStroke(oldStroke);
    }

    // Upon entry, we already checked that <x,y> is inside the rectangle.
    public final boolean contains(GraphicComponent gc, int x, int y) {
        return true;
    }

    public final Rectangle getContentRect(GraphicComponent gc) {
        Rectangle rect = gc.getRectangle();
        int inset = (int) gc.getLineStroke().getLineWidth();
        int gap = GAP;
        return new Rectangle(rect.x + gap * 2 + inset, rect.y + inset, Math.max(0, rect.width - gap
                * 2 - 2 * inset), Math.max(0, rect.height - 2 * inset));
    }

    public final Dimension getShapeSize(GraphicComponent gc, Dimension contentSize) {
        int inset = (int) gc.getLineStroke().getLineWidth();
        return new Dimension(contentSize.width + 2 * inset, contentSize.height + 2 * inset);
    }
}
