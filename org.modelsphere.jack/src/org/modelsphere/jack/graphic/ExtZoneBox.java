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

import java.awt.Graphics;
import java.awt.Rectangle;

import org.modelsphere.jack.graphic.shape.GraphicShape;
import org.modelsphere.jack.graphic.zone.Zone;

/**
 * @author nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class ExtZoneBox extends GraphicNode {

    public ExtZoneBox(Diagram diagram, GraphicShape shape) {
        super(diagram, shape);
    }

    protected void paintZonesSelection(Graphics g, DiagramView diagView) {
        paintZones(g, diagView, GraphicComponent.DRAW_WHOLE, false);
    }

    protected void paintZones(Graphics g, DiagramView diagView, int drawingMode) {
        paintZones(g, diagView, drawingMode, false);
    }

    protected void paintZones(Graphics g, DiagramView diagView, int drawingMode, boolean drawSel) {
        Rectangle savedClip = g.getClipBounds();
        Rectangle contentRect = getContentRect();
        int width = (leftColPct != 0 ? contentRect.width * leftColPct / 100 : contentRect.width);
        Rectangle rect = new Rectangle(contentRect.x, contentRect.y, width, contentRect.height);
        Rectangle zoomRect = (diagView == null ? rect : diagView.zoom(rect));
        Rectangle clipRect = savedClip.intersection(zoomRect);
        g.setClip(clipRect.x, clipRect.y, clipRect.width, clipRect.height);
        for (int i = 0; i < zones.size(); i++) {
            if (leftColPct != 0 && i == nbLeftZones) {
                rect.x += rect.width;
                rect.width = contentRect.width - rect.width;
                zoomRect = (diagView == null ? rect : diagView.zoom(rect));
                clipRect = savedClip.intersection(zoomRect);
                g.setClip(clipRect.x, clipRect.y, clipRect.width, clipRect.height);
                if (vertLine && !drawSel) {
                    g.setColor(lineColor);
                    g.drawLine(zoomRect.x, zoomRect.y, zoomRect.x, zoomRect.y + zoomRect.height);
                }
            } //end if

            Zone zone = (Zone) zones.get(i);
            if (!zone.isVisible())
                continue;

            Rectangle zRect = zone.getRectangle(rect); //zone.getRectangle(clipRect);
            if (zRect != null) {
                if (zRect.y >= rect.height)
                    continue; // skip the rest of the zones in this column, they are outside the frame

                Rectangle zoneRect = new Rectangle(rect.x, rect.y + zRect.y, rect.width,
                        rect.height - zRect.y);
                boolean bottomClipped = true; // on the printer, the clip rect clips only partially the bottom line
                if (zoneRect.height > zRect.height) {
                    zoneRect.height = zRect.height;
                    bottomClipped = false;
                }
                g.setPaintMode(); //this ensure that the clip rectangle is not drawn and text is original color ! 
                zone.paint(g, diagView, drawingMode, zoneRect, drawSel, bottomClipped);
            } //end if

        } //end for

        g.setClip(savedClip.x, savedClip.y, savedClip.width, savedClip.height);
    } //end paintZones()    

}
