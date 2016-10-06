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
import javax.swing.UIManager;

// This class provide a way to display a color value as an image icon

@SuppressWarnings("serial")
public final class ColorIcon extends ImageIcon {
    public static final int DEFAULT_WIDTH = 16;
    public static final int DEFAULT_HEIGHT = 16;

    private Color color;
    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;
    private boolean paintBorder = true;

    ColorIcon(Color c) {
        color = c;
    }

    public ColorIcon(Color c, int width, int height) {
        this(c);
        this.width = width;
        this.height = height;
    }

    public ColorIcon(Color c, int width, int height, boolean paintBorder) {
        this(c);
        this.width = width;
        this.height = height;
        this.paintBorder = paintBorder;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Color oldColor = g.getColor();

        if (paintBorder) {
            g.setColor(UIManager.getColor("Button.background").darker()); // NOT LOCALIZABLE
            g.fillRect(x, y, width, height);
            g.setColor(color);
            g.fillRect(x + 1, y + 1, width - 2, height - 2);
        } else {
            g.setColor(color);
            g.fillRect(x, y, width, height);
        }

        g.setColor(oldColor); // restore old graphic color
    }

    public int getIconWidth() {
        return width;
    }

    public int getIconHeight() {
        return height;
    }

    public void setIconWidth(int w) {
        width = w;
    }

    public void setIconHeight(int h) {
        height = h;
    }
}
