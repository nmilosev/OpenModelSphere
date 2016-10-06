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

import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.RenderingFlags;

public class RectangleShape implements GraphicShape, RenderingFlags {
    private static final Color TRANSPARENT = new Color(255, 255, 255, 0);

    public static final int NORMAL_FRAME = 0;
    public static final int TLB_FRAME = 1; // top-left-bottom
    public static final int TB_FRAME = 2; // top-bottom

    public static final RectangleShape singleton = new RectangleShape(NORMAL_FRAME);
    public static final RectangleShape tlb_singleton = new RectangleShape(TLB_FRAME);
    public static final RectangleShape tb_singleton = new RectangleShape(TB_FRAME);
    public static final RectangleShape singleton_erasebackground = new RectangleShape(TB_FRAME,
            true);

    private int frame;
    // Indicate if this shape should be used to clear the shape area
    private boolean eraseBackground = false;

    public RectangleShape(int frame, boolean eraseBackground) {
        this.frame = frame;
        this.eraseBackground = eraseBackground;
    }

    public RectangleShape(int frame) {
        this.frame = frame;
    }

    public final void paint(Graphics g, DiagramView diagView, GraphicComponent gc,
            int renderingFlags) {
        Rectangle rect = gc.getRectangle();
        if (diagView != null)
            rect = diagView.zoom(rect);
        Graphics2D g2D = (Graphics2D) g;
        Stroke oldStroke = g2D.getStroke();
        g2D.setStroke(gc.getLineStroke());

        Color fillColor = gc.getFillColor();
        Color lineColor = gc.getLineColor();
        Composite composite = g2D.getComposite();
        if (eraseBackground) {
            if (fillColor != null) {
                if ((renderingFlags & ERASE_BACKGROUND) != 0) {
                    // This ensure that the components are transparent during a
                    // copy image
                    // operation if this shape is used for clearing purposes
                    // (for example a line label).
                    // Erase the content inside the fillRect area.
                    g2D.setComposite(AlphaComposite.Clear);
                    g2D.fillRect(rect.x, rect.y, rect.width, rect.height);
                } else {
                    // Erase the content inside the fillRect area only for the
                    // existing drawn portions.
                    g2D.setColor(Color.WHITE);
                    g2D.setComposite(AlphaComposite.SrcAtop);
                    g2D.fillRect(rect.x, rect.y, rect.width, rect.height);
                }
            }
        } else {
            if (fillColor != null) {
                g2D.setColor(fillColor);
                g2D.fillRect(rect.x, rect.y, rect.width, rect.height);
            }
        }

        if (lineColor != null) {
            g2D.setColor(lineColor);
            switch (frame) {
            case RectangleShape.TLB_FRAME:
                g2D.drawPolyline(new int[] { rect.x + rect.width, rect.x, rect.x,
                        rect.x + rect.width }, new int[] { rect.y, rect.y, rect.y + rect.height,
                        rect.y + rect.height }, 4);
                break;
            case RectangleShape.TB_FRAME:
                g2D.drawLine(rect.x, rect.y, rect.x + rect.width, rect.y);
                g2D.drawLine(rect.x, rect.y + rect.height, rect.x + rect.width, rect.y
                        + rect.height);
                break;
            default:
                g2D.drawRect(rect.x, rect.y, rect.width, rect.height);
                break;
            }
        }

        g2D.setComposite(composite);

        g2D.setStroke(oldStroke);
    }

    // Upon entry, we already checked that <x,y> is inside the rectangle.
    public final boolean contains(GraphicComponent gc, int x, int y) {
        return true;
    }

    public final Rectangle getContentRect(GraphicComponent gc) {
        Rectangle rect = gc.getRectangle();
        int inset = (int) gc.getLineStroke().getLineWidth();
        return new Rectangle(rect.x + inset, rect.y + inset, Math.max(0, rect.width - 2 * inset),
                Math.max(0, rect.height - 2 * inset));
    }

    public final Dimension getShapeSize(GraphicComponent gc, Dimension contentSize) {
        int inset = (int) gc.getLineStroke().getLineWidth();
        return new Dimension(contentSize.width + 2 * inset, contentSize.height + 2 * inset);
    }
}
