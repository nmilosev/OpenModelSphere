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

public class ShadowRectShape implements GraphicShape {
    private boolean m_shadowEast = true;
    private boolean m_shadowNorth = true;

    private ShadowRectShape(boolean shadowEast, boolean shadowNorth) {
        m_shadowEast = shadowEast;
        m_shadowNorth = shadowNorth;
    }

    public static final ShadowRectShape singleton = new ShadowRectShape(true, true);
    public static final ShadowRectShape southWestSingleton = new ShadowRectShape(false, false);

    private static final int SHADOW_WIDTH = 5; // must be odd

    /*
     * Paint the Showdow Rectangle. The outer rectangle is the rectangle holding the box and its
     * shadow. The inner rectangle is the rectangle holding the box itself.
     */
    public final void paint(Graphics g, DiagramView diagView, GraphicComponent gc,
            int renderingFlags) {
        Rectangle outerRect = gc.getRectangle();
        int shadowWidth = SHADOW_WIDTH;
        if (diagView != null) {
            outerRect = diagView.zoom(outerRect);
            shadowWidth = 1 | (int) (shadowWidth * diagView.getZoomFactor()); // keep it odd (insure min 1)
        }

        // compute inner rectangle
        int innerXPos = outerRect.x + (m_shadowEast ? shadowWidth : 0);
        int innerYPos = outerRect.y + (m_shadowNorth ? shadowWidth : 0);
        Rectangle innerRect = new Rectangle(innerXPos, innerYPos, outerRect.width - shadowWidth,
                outerRect.height - shadowWidth);

        if (innerRect.width < 2 || innerRect.height < 2)
            return;

        int halfWidth = (shadowWidth + 1) / 2;

        // draw the shadow (a two-segment line with stoke == shadowWidth)
        Graphics2D g2D = (Graphics2D) g;
        Stroke oldStroke = g2D.getStroke();
        g2D.setStroke(new BasicStroke((float) shadowWidth));
        g2D.setColor(Color.black);
        int[] xPoints = computeXPoints(innerRect, shadowWidth, halfWidth);
        int[] yPoints = computeYPoints(innerRect, shadowWidth, halfWidth);
        g2D.drawPolyline(xPoints, yPoints, 3);
        g2D.setStroke(gc.getLineStroke());
        g2D.setColor(gc.getFillColor());
        g2D.fillRect(innerRect.x, innerRect.y, innerRect.width, innerRect.height);

        // draw rectangle
        g2D.setColor(gc.getLineColor());
        g2D.drawRect(innerRect.x, innerRect.y, innerRect.width, innerRect.height);

        // restore stroke
        g2D.setStroke(oldStroke);
    }

    // Upon entry, we already checked that <x,y> is inside the rectangle.
    public final boolean contains(GraphicComponent gc, int x, int y) {
        return true;
    }

    public final Rectangle getContentRect(GraphicComponent gc) {
        Rectangle outerRect = gc.getRectangle();
        int inset = (int) gc.getLineStroke().getLineWidth();
        int width = Math.max(0, outerRect.width - SHADOW_WIDTH - 2 * inset);
        int height = Math.max(0, outerRect.height - SHADOW_WIDTH - 2 * inset);

        int innerXPos = outerRect.x + (m_shadowEast ? SHADOW_WIDTH : 0) + inset;
        int innerYPos = outerRect.y + (m_shadowNorth ? SHADOW_WIDTH : 0) + inset;
        Rectangle contectRect = new Rectangle(innerXPos, innerYPos, width, height);

        return contectRect;
    }

    public final Dimension getShapeSize(GraphicComponent gc, Dimension contentSize) {
        int inset = (int) gc.getLineStroke().getLineWidth();
        return new Dimension(contentSize.width + SHADOW_WIDTH + 2 * inset, contentSize.height
                + SHADOW_WIDTH + 2 * inset);
    }

    private int[] computeXPoints(Rectangle innerRect, int shadowWidth, int halfWidth) {
        int[] xPoints;

        if (m_shadowEast) {
            xPoints = new int[] { innerRect.x + innerRect.width - shadowWidth,
                    innerRect.x - halfWidth, innerRect.x - halfWidth };
        } else {
            xPoints = new int[] { innerRect.x + shadowWidth,
                    innerRect.x + innerRect.width + halfWidth,
                    innerRect.x + innerRect.width + halfWidth };
        }

        return xPoints;
    } // end computeXPoints()

    private int[] computeYPoints(Rectangle innerRect, int shadowWidth, int halfWidth) {
        int[] yPoints;

        if (m_shadowNorth) {
            yPoints = new int[] { innerRect.y - halfWidth, innerRect.y - halfWidth,
                    innerRect.y + innerRect.height - shadowWidth };
        } else {
            yPoints = new int[] { innerRect.y + innerRect.height + halfWidth,
                    innerRect.y + innerRect.height + halfWidth, innerRect.y + shadowWidth };
        }

        return yPoints;
    } // end computeYPoints()

} // end ShadowRectShape

