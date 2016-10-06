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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public final class CircleLineEnd extends LineEnd {

    private int radius;

    // height is the space reserved for the line end along the line; it may be greater
    // than radius*2, if we want to leave space between line ends.
    public CircleLineEnd(int center, int radius, int height, Color fillColor) {
        super(new int[] { center }, new int[] { 0 }, height, fillColor);
        this.radius = radius;
    }

    public static CircleLineEnd createCircleLineEnd(Color fillColor) {
        return new CircleLineEnd(DEFAULT_HEIGHT, DEFAULT_HEIGHT / 2, DEFAULT_HEIGHT * 2, fillColor);
    };

    public static CircleLineEnd createSmallCircleLineEnd(Color fillColor) {
        return new CircleLineEnd(DEFAULT_HEIGHT * 5 / 9, DEFAULT_HEIGHT * 4 / 9,
                DEFAULT_HEIGHT * 10 / 9, fillColor);
    };

    public static CircleLineEnd createHalfCircleLineEnd(Color fillColor) {
        return new CircleLineEnd(0, DEFAULT_HEIGHT * 6 / 7, DEFAULT_HEIGHT * 6 / 7, fillColor);
    };

    protected final Rectangle getRect() {
        return new Rectangle(poly.xpoints[0] - radius, poly.ypoints[0] - radius, radius * 2,
                radius * 2);
    }

    protected final void draw(Graphics g, DiagramView diagView) {
        Rectangle rect = getRect();
        if (diagView != null)
            rect = diagView.zoom(rect);
        if (fillColor != null) {
            Color lineColor = g.getColor();
            g.setColor(fillColor);
            g.fillOval(rect.x, rect.y, rect.width, rect.height);
            g.setColor(lineColor);
        }
        g.drawOval(rect.x, rect.y, rect.width, rect.height);
    }
}
