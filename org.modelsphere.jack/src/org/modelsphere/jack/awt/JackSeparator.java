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
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

public class JackSeparator extends JComponent {
    private static final int SEP_WIDTH = 4;
    private static final int SEP_HEIGHT = 22;

    public JackSeparator() {
        setToolTipText(null);
        setBorder(null);
    }

    protected void paintComponent(Graphics g) {
        int h = getHeight();
        int w = getWidth();
        if (w >= 2) {
            int x = (w / 2) - 1;

            Color c = getBackground();
            g.setColor(c);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(c.darker());
            g.drawLine(x, 0, x, h);

            g.setColor(c.brighter());
            g.drawLine(x + 1, 0, x + 1, h);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(SEP_WIDTH, SEP_HEIGHT);
    }

    public Dimension getMinimumSize() {
        return new Dimension(SEP_WIDTH, SEP_HEIGHT);
    }

    public Dimension getMaximumSize() {
        return new Dimension(SEP_WIDTH, SEP_HEIGHT);
    }

}
