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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.ImageIcon;

public class SeparatorIcon extends ImageIcon {
    private static final int HEIGHT = 3;
    private static final int HEIGHT_MARGINS = 1;

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Color oldColor = g.getColor();
        int w = c.getWidth();

        g.setColor(c.getBackground());
        g.fillRect(x, y, w, HEIGHT);
        g.setColor(c.getForeground());
        g.fillRect(x, y + HEIGHT_MARGINS, w, HEIGHT - (HEIGHT_MARGINS * 2));

        g.setColor(oldColor); // restore old graphic color
    }

    public int getIconWidth() {
        return 0;
    }

    public int getIconHeight() {
        return HEIGHT;
    }
}
