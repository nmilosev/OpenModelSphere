/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/
package org.modelsphere.sms.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.shape.GraphicShape;
import org.modelsphere.sms.db.DbSMSNoticeGo;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NoticeShape implements GraphicShape {

    public static final NoticeShape singleton = new NoticeShape();
    private static final int RATIO_FOR_CORNER = 5;
    private static final int CORNER_MAX_SIZE = 13;

    public final void paint(Graphics g, DiagramView diagView, GraphicComponent gc,
            int renderingFlags) {
        Rectangle rect = gc.getRectangle();
        Graphics2D g2D = (Graphics2D) g;
        Stroke oldStroke = g2D.getStroke();
        g2D.setStroke(gc.getLineStroke());
        drawRect(g, diagView, gc, new Rectangle(rect.x, rect.y, rect.width, rect.height));
        g2D.setStroke(oldStroke);
    } // end paint()

    public final Rectangle getContentRect(GraphicComponent gc) {
        Rectangle rect = gc.getRectangle();
        int inset = (int) gc.getLineStroke().getLineWidth();
        int margin = inset * 2;
        int xgap = (rect.width / RATIO_FOR_CORNER);
        int ygap = (rect.height / RATIO_FOR_CORNER);

        int gap = (xgap < ygap) ? xgap : ygap; // = min(xgap, ygap)

        if (gap > CORNER_MAX_SIZE)
            gap = CORNER_MAX_SIZE;

        Rectangle contentRect = new Rectangle(rect.x, rect.y + inset + gap, Math.max(0, rect.width
                - CORNER_MAX_SIZE), Math.max(0, rect.height - (inset * 2)));

        contentRect.height = rect.height;
        contentRect.y = rect.y;
        return contentRect;
    } // end getContentRect()

    // Upon entry, we already checked that <x,y> is inside the rectangle.
    public final boolean contains(GraphicComponent gc, int x, int y) {
        Rectangle rect = gc.getRectangle();
        if (x > rect.x && y < rect.y)
            return false;
        return true;
    }

    public final Dimension getShapeSize(GraphicComponent gc, Dimension contentSize) {
        int inset = (int) gc.getLineStroke().getLineWidth();
        int height = contentSize.height + 2 * inset;
        int xgap = (contentSize.width / RATIO_FOR_CORNER);
        int ygap = (contentSize.height / RATIO_FOR_CORNER);

        int gap = (xgap < ygap) ? xgap : ygap; // = min(xgap, ygap)

        if (gap > CORNER_MAX_SIZE)
            gap = CORNER_MAX_SIZE;

        int xdim = contentSize.width + 4 * inset + gap;
        int ydim = contentSize.height;

        if (xdim < DbSMSNoticeGo.DEFAULT_WIDTH)
            xdim = DbSMSNoticeGo.DEFAULT_WIDTH;

        if (ydim < DbSMSNoticeGo.DEFAULT_HEIGHT)
            ydim = DbSMSNoticeGo.DEFAULT_HEIGHT;

        return new Dimension(xdim, ydim);
    } // end getShapeSize()

    //
    // private methods
    //
    private final void drawRect(Graphics g, DiagramView diagView, GraphicComponent gc,
            Rectangle rect) {
        if (diagView != null)
            rect = diagView.zoom(rect);

        int xgap = rect.width / RATIO_FOR_CORNER;
        int ygap = rect.height / RATIO_FOR_CORNER;
        int gap = (xgap < ygap) ? xgap : ygap; // = min(xgap, ygap)

        if (gap > CORNER_MAX_SIZE)
            gap = CORNER_MAX_SIZE;

        int[] xs = new int[] { rect.x, rect.x, rect.x + rect.width, rect.x + rect.width,
                rect.x + rect.width - gap, rect.x + rect.width - gap };
        int[] ys = new int[] { rect.y, rect.y + rect.height, rect.y + rect.height, rect.y + gap,
                rect.y + gap, rect.y };

        g.setColor(gc.getFillColor());
        g.fillPolygon(xs, ys, xs.length);

        int x1 = rect.x + rect.width - gap; // top point
        int y1 = rect.y;

        int x2 = rect.x + rect.width; // right side
        int y2 = rect.y + gap;

        int x3 = rect.x + rect.width - gap;
        int y3 = rect.y + gap;

        g.setColor(Color.WHITE);
        g.fillPolygon(new int[] { x1, x2, x3 }, new int[] { y1, y2, y3 }, 3);

        g.setColor(gc.getLineColor());
        g.drawPolygon(xs, ys, xs.length);
        g.drawLine(x1, y1, x2, y2);

    } // end drawRect()

} // end NoticeShape
