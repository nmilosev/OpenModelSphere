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

import javax.swing.border.Border;

public final class LineBorder2 implements Border {
    private Color color;
    private int top = 0;
    private int left = 0;
    private int bottom = 0;
    private int right = 0;

    public LineBorder2(Color color, int top, int left, int bottom, int right) {
        this.color = color;
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Color oldColor = g.getColor();

        g.setColor(color);

        g.fillRect(x, y, x + width - 1, top);
        g.fillRect(x, y, left, y + height - 1);
        g.fillRect(x, y + height - 1 - bottom, x + width - 1, y + height - 1);
        g.fillRect(x + width - 1 - right, y, x + width - 1, y + height - 1);

        g.setColor(oldColor);
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(top, left, bottom, right);
    }

    public boolean isBorderOpaque() {
        return false;
    }

}
