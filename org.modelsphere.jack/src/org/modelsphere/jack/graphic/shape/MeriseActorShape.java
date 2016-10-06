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

public class MeriseActorShape implements GraphicShape {

    public static final MeriseActorShape singleton = new MeriseActorShape();

    private static final Image actor = GraphicUtil.loadImage(MeriseActorShape.class,
            "meriseactor.gif");// NOT LOCALIZABLE
    private boolean imageLoaded = false;

    public final void paint(Graphics g, DiagramView diagView, GraphicComponent gc,
            int renderingFlags) {
        Rectangle rect = gc.getRectangle();
        if (!imageLoaded) {
            GraphicUtil.waitForImage(actor);
            imageLoaded = true;
        }
        Dimension actorSize = getActorSize(rect.width, rect.height);
        if (diagView != null) {
            rect = diagView.zoom(rect);
            actorSize.width = (int) (actorSize.width * diagView.getZoomFactor());
            actorSize.height = (int) (actorSize.height * diagView.getZoomFactor());
        }
        Rectangle zoneRect = new Rectangle(rect.x + actorSize.width - 1, rect.y, rect.width
                - actorSize.width + 1, rect.height);

        Graphics2D g2D = (Graphics2D) g;
        Stroke oldStroke = g2D.getStroke();
        g2D.setStroke(gc.getLineStroke());
        g2D.setColor(gc.getFillColor());
        g2D.fillRect(zoneRect.x, zoneRect.y, zoneRect.width, zoneRect.height);
        g2D.setColor(gc.getLineColor());
        g2D.drawRect(zoneRect.x, zoneRect.y, zoneRect.width, zoneRect.height);
        g2D.drawImage(actor, rect.x, rect.y + rect.height - actorSize.height, actorSize.width,
                actorSize.height, null);
        g2D.setStroke(oldStroke);
    }

    // Upon entry, we already checked that <x,y> is inside the rectangle.
    public final boolean contains(GraphicComponent gc, int x, int y) {
        Rectangle rect = gc.getRectangle();
        Dimension actorSize = getActorSize(rect.width, rect.height);
        return (x >= rect.x + actorSize.width || y >= rect.y + rect.height - actorSize.height);
    }

    public final Rectangle getContentRect(GraphicComponent gc) {
        Rectangle rect = gc.getRectangle();
        Dimension actorSize = getActorSize(rect.width, rect.height);
        int inset = (int) gc.getLineStroke().getLineWidth();
        return new Rectangle(rect.x + actorSize.width + inset, rect.y + inset, Math.max(0,
                rect.width - actorSize.width - 2 * inset), Math.max(0, rect.height - 2 * inset));
    }

    public final Dimension getShapeSize(GraphicComponent gc, Dimension contentSize) {
        int inset = (int) gc.getLineStroke().getLineWidth();
        int width = contentSize.width + 2 * inset;
        int height = contentSize.height + 2 * inset;
        Dimension actorSize = getActorSize(Integer.MAX_VALUE, height);
        return new Dimension(width + actorSize.width, height);
    }

    private Dimension getActorSize(int rectWidth, int rectHeight) {
        int width = actor.getWidth(null);
        int height = actor.getHeight(null);
        if (height > rectHeight) {
            width = width * rectHeight / height; // scale proportionnaly
            height = rectHeight;
        }
        return new Dimension(Math.min(width, rectWidth), height);
    }
}
