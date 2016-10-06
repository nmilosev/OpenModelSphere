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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicComponent;

public class UmlActorShape implements GraphicShape {

    public static final UmlActorShape singleton = new UmlActorShape();

    public final void paint(Graphics g, DiagramView diagView, GraphicComponent gc,
            int renderingFlags) {
        Rectangle rect = gc.getRectangle();

        Dimension actorSize = getActorSize(rect.width, rect.height);
        if (diagView != null) {
            rect = diagView.zoom(rect);
            actorSize.width = (int) (actorSize.width * diagView.getZoomFactor() / 2);
            actorSize.height = (int) (actorSize.height * diagView.getZoomFactor());
        }
        Rectangle zoneRect = new Rectangle(rect.x, rect.y, rect.width + 1, rect.height + 1);

        Graphics2D g2D = (Graphics2D) g;
        Stroke oldStroke = g2D.getStroke();
        Color oldColor = g2D.getColor();

        BasicStroke strk = gc.getLineStroke();
        Color bgColor = new Color(255, 255, 255, Color.TRANSLUCENT);
        Color lineColor = gc.getLineColor();

        g2D.setStroke(strk);
        g2D.setColor(Color.WHITE);
        g2D.fillRect(zoneRect.x + zoneRect.width / 4, zoneRect.y, zoneRect.width / 2,
                (zoneRect.height - zoneRect.height / 4));

        g2D.setStroke(strk);
        g2D.setColor(bgColor);
        g2D.fillRect(zoneRect.x, zoneRect.y, zoneRect.width, zoneRect.height);
        g2D.setColor(lineColor);
        Color fillColor = Color.WHITE;// gc.getFillColor();
        drawStickMan(g2D, rect.x, rect.y, zoneRect.width, zoneRect.height / 2, lineColor, bgColor,
                fillColor);

        // restore old values
        g2D.setStroke(oldStroke);
        g2D.setColor(oldColor);
    }

    private void drawStickMan(Graphics2D g2D, int x, int y, int width, int height, Color lineColor,
            Color bgColor, Color fillColor) {
        int midX = x + (width / 2);
        int yHeaderPos = y + (int) (height * 0.05);
        int yArmPos = y + (int) (height * 0.4);
        int yBassinPos = y + (int) (height * 0.7);
        int radius = (int) (height * 0.1);

        // g2D.setColor(bgColor);
        // g2D.fillRect(x, y, width, height);
        g2D.setColor(fillColor);
        g2D.fillOval(midX - radius, y + 10, radius * 2, radius * 2); // the head
        g2D.setColor(lineColor);
        g2D.drawOval(midX - radius, y + 10, radius * 2, radius * 2); // the head
        g2D.drawLine(midX, y + (radius * 2) + 10, midX, yBassinPos); // the
        // trunk
        g2D.drawLine((int) (x + (width * 0.35)), yArmPos + 7, (int) (x + (width * 0.65)),
                yArmPos + 7); // the arms
        g2D.drawLine(midX, yBassinPos, (int) (x + (width * 0.35)), y + height); // the
        // left
        // leg
        g2D.drawLine(midX, yBassinPos, (int) (x + (width * 0.65)), y + height); // the
        // right
        // leg

    }

    // Upon entry, we already checked that <x,y> is inside the rectangle.
    public final boolean contains(GraphicComponent gc, int x, int y) {
        Rectangle rect = gc.getRectangle();
        Dimension actorSize = getActorSize(rect.width, rect.height);
        return (x >= rect.x + rect.width - actorSize.width || y >= rect.y + actorSize.height);
    }

    public final Rectangle getContentRect(GraphicComponent gc) {
        Rectangle rect = gc.getRectangle();
        Dimension actorSize = getActorSize(rect.width, rect.height);
        int inset = (int) gc.getLineStroke().getLineWidth();
        return new Rectangle(rect.x + inset, rect.y + actorSize.height + inset * 2, Math.max(0,
                rect.width - 2 * inset), Math.max(0, rect.height - actorSize.height - 2 * inset));
    }

    public final Dimension getShapeSize(GraphicComponent gc, Dimension contentSize) {
        int inset = (int) gc.getLineStroke().getLineWidth();
        int width = contentSize.width + 2 * inset;
        int height = contentSize.height + 2 * inset;
        Dimension actorSize = getActorSize(width, height);
        return new Dimension(width, height + actorSize.height * 2 + 2 * inset);
    }

    private Dimension getActorSize(int rectWidth, int rectHeight) {
        Dimension actorSize = new Dimension(rectWidth, rectHeight / 2);
        return actorSize;
    }

} // end UmlActorShape
