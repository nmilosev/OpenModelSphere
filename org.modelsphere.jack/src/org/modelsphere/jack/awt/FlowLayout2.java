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

/**
 * This layout work exactly like the java.awt.FlowLayout except that on the java FlowLayout,
 * preferredLayoutSize is calculated to have all components on the same line. Since the ToolBarPanel
 * will never get this preferred size (width > application_width), I override the
 * preferredLayoutSize method to evaluate the preferred size using the actual available width for
 * the container. If we don't override, some components will never become visible on the screen if
 * the parent of the target container use a BorderLayout (maybe others) with a NORTH, SOUTH, EAST or
 * WEST constraint. The FlowLayout correctly layout the components on multiple rows. DO NOT USE THIS
 * CLASS unless you want that behavior.
 */

class FlowLayout2 extends FlowLayout {

    public FlowLayout2() {
        super(CENTER, 5, 5);
    }

    public FlowLayout2(int align) {
        super(align, 5, 5);
    }

    public FlowLayout2(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    public Dimension preferredLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            int vgap = getVgap();
            int hgap = getHgap();
            Insets insets = target.getInsets();
            Dimension dim = new Dimension(0, 0);
            int maxwidth = target.getWidth() - (insets.left + insets.right + hgap * 2);
            int nmembers = target.getComponentCount();
            int roww = 0;
            int rowh = 0;

            for (int i = 0; i < nmembers; i++) {
                Component m = target.getComponent(i);
                if (m.isVisible()) {
                    Dimension d = m.getPreferredSize();
                    if (d.width > (maxwidth - roww)) {
                        dim.height += rowh + hgap;
                        rowh = 0;
                        dim.width = Math.max(roww, dim.width);
                        roww = 0;
                    }
                    rowh = Math.max(rowh, d.height);
                    if (roww > 0) {
                        roww += hgap;
                    }
                    roww += d.width;
                }
            }
            dim.height += rowh + hgap;
            dim.width = Math.max(roww, dim.width);

            dim.width += insets.left + insets.right + hgap * 2;
            dim.height += insets.top + insets.bottom + vgap * 2;
            return dim;
        }
    }

}
