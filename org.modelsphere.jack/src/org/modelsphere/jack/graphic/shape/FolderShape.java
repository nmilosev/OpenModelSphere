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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicComponent;

public class FolderShape implements GraphicShape {

    public static final FolderShape singleton = new FolderShape();

    private static final int MAX_TAB_HEIGHT = (int) (5 * Diagram.PIXELS_PER_MM);
    private static final int MAX_TAB_WIDTH = (int) (15 * Diagram.PIXELS_PER_MM);

    //
    // public methods
    //

    public final void paint(Graphics g, DiagramView diagView, GraphicComponent gc,
            int renderingFlags) {
        Rectangle rect = gc.getRectangle();
        int tabWidth = getTabWidth(rect.width);
        int tabHeight = getTabHeight(rect.height);
        Graphics2D g2D = (Graphics2D) g;
        Stroke oldStroke = g2D.getStroke();
        g2D.setStroke(gc.getLineStroke());
        drawRect(g, diagView, gc, new Rectangle(rect.x, rect.y, tabWidth, tabHeight));
        drawRect(g, diagView, gc, new Rectangle(rect.x, rect.y + tabHeight, rect.width, rect.height
                - tabHeight));
        g2D.setStroke(oldStroke);
    }

    // Upon entry, we already checked that <x,y> is inside the rectangle.
    public final boolean contains(GraphicComponent gc, int x, int y) {
        Rectangle rect = gc.getRectangle();
        int tabWidth = getTabWidth(rect.width);
        int tabHeight = getTabHeight(rect.height);
        if (x > rect.x + tabWidth && y < rect.y + tabHeight)
            return false;
        return true;
    }

    public final Rectangle getContentRect(GraphicComponent gc) {
        Rectangle rect = gc.getRectangle();
        int tabHeight = getTabHeight(rect.height);
        int inset = (int) gc.getLineStroke().getLineWidth();
        return new Rectangle(rect.x + inset, rect.y + tabHeight + inset, Math.max(0, rect.width - 2
                * inset), Math.max(0, rect.height - tabHeight - 2 * inset));
    }

    public final Dimension getShapeSize(GraphicComponent gc, Dimension contentSize) {
        int inset = (int) gc.getLineStroke().getLineWidth();
        int height = contentSize.height + 2 * inset;
        return new Dimension(contentSize.width + 2 * inset, height
                + Math.min(height / 2, MAX_TAB_HEIGHT));
    }

    //
    // private methods
    // 

    private final void drawRect(Graphics g, DiagramView diagView, GraphicComponent gc,
            Rectangle rect) {
        if (diagView != null)
            rect = diagView.zoom(rect);
        g.setColor(gc.getFillColor());
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
        g.setColor(gc.getLineColor());
        g.drawRect(rect.x, rect.y, rect.width, rect.height);
    }

    private final int getTabWidth(int rectWidth) {
        return Math.min(rectWidth / 2, MAX_TAB_WIDTH);
    }

    private final int getTabHeight(int rectHeight) {
        return Math.min(rectHeight / 3, MAX_TAB_HEIGHT);
    }
}
