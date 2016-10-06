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

public class ToolBarGroupBorder extends BevelBorder {
    private boolean paintLeft = true;
    private boolean paintRight = true;

    public ToolBarGroupBorder(boolean left, boolean right) {
        super(BevelBorder.LOWERED);
        paintLeft = left;
        paintRight = right;
    }

    public ToolBarGroupBorder(boolean left, boolean right, Color highlight, Color shadow) {
        super(BevelBorder.LOWERED, highlight, shadow);
        paintLeft = left;
        paintRight = right;
    }

    public ToolBarGroupBorder(boolean left, boolean right, Color highlightOuter,
            Color highlightInner, Color shadowOuter, Color shadowInner) {
        super(BevelBorder.LOWERED, highlightOuter, highlightInner, shadowOuter, shadowInner);
        paintLeft = left;
        paintRight = right;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(0, paintLeft ? 5 : 0, 0, paintRight ? 5 : 0);
    }

    protected void paintLoweredBevel(Component c, Graphics g, int x, int y, int width, int height) {
        Color oldColor = g.getColor();
        int h = height;
        int w = width;

        g.translate(x, y);

        g.setColor(getShadowInnerColor(c));
        if (paintLeft)
            g.drawLine(2, 3, 2, h - 4);
        if (paintRight)
            g.drawLine(w - 4, 3, w - 4, h - 4);

        g.setColor(getHighlightOuterColor(c));
        if (paintLeft)
            g.drawLine(3, 3, 3, h - 4);
        if (paintRight)
            g.drawLine(w - 3, 3, w - 3, h - 4);

        g.translate(-x, -y);
        g.setColor(oldColor);

    }

}
