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

package org.modelsphere.jack.awt;

import java.awt.*;

import javax.swing.border.BevelBorder;

public class ThinBevelBorder extends BevelBorder {
    private Insets insets = new Insets(3, 3, 3, 3);

    public ThinBevelBorder(int bevelType) {
        super(bevelType);
    }

    public ThinBevelBorder(int bevelType, Color highlight, Color shadow) {
        super(bevelType, highlight, shadow);
    }

    public ThinBevelBorder(int bevelType, Color highlightOuter, Color highlightInner,
            Color shadowOuter, Color shadowInner) {
        super(bevelType, highlightOuter, highlightInner, shadowOuter, shadowInner);
    }

    public Insets getBorderInsets(Component c) {
        return insets;
    }

    protected void paintRaisedBevel(Component c, Graphics g, int x, int y, int width, int height) {
        Color oldColor = g.getColor();
        int h = height;
        int w = width;

        g.translate(x, y);

        g.setColor(getHighlightOuterColor(c));
        g.drawLine(0, 0, 0, h - 2);
        g.drawLine(1, 0, w - 2, 0);

        g.setColor(getShadowInnerColor(c));
        g.drawLine(w - 1, 0, w - 1, h - 1);
        g.drawLine(0, h - 1, w - 2, h - 1);

        g.translate(-x, -y);
        g.setColor(oldColor);

    }

    protected void paintLoweredBevel(Component c, Graphics g, int x, int y, int width, int height) {
        Color oldColor = g.getColor();
        int h = height;
        int w = width;

        g.translate(x, y);

        g.setColor(getShadowInnerColor(c));
        g.drawLine(0, 0, 0, h - 2);
        g.drawLine(1, 0, w - 2, 0);

        g.setColor(getHighlightOuterColor(c));
        g.drawLine(w - 1, 0, w - 1, h - 1);
        g.drawLine(0, h - 1, w - 2, h - 1);

        g.translate(-x, -y);
        g.setColor(oldColor);

    }

}
