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

package org.modelsphere.jack.graphic.tool;

import java.awt.*;
import javax.swing.*;

import org.modelsphere.jack.util.DefaultComparableElement;

public class DefaultLinkUnlinkListPanel extends JPanel {
    private JList list = new JList();
    private JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

    public DefaultLinkUnlinkListPanel() {
        super(new GridBagLayout());
        initGUI();
        list.setCellRenderer(new LinkUnlinkListCellRenderer());
    }

    private void initGUI() {
        add(new JScrollPane(list),
                new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                        GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        add(toolPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 3, 3, 3), 0, 0));
    }

    public JList getJList() {
        return list;
    }

    public void addToolComponent(JComponent component) {
        toolPanel.add(component);
        toolPanel.revalidate();
        invalidate();
        revalidate();
        this.doLayout();
    }

    private class LinkUnlinkListCellRenderer extends DefaultListCellRenderer {
        LinkUnlinkListCellRenderer() {
            super();
        }

        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            Component renderer = super.getListCellRendererComponent(list, value, index, isSelected,
                    cellHasFocus);
            if (value instanceof DefaultComparableElement) {
                if (((DefaultComparableElement) value).object2 instanceof Image) {
                    setIcon(new ImageIcon((Image) ((DefaultComparableElement) value).object2) {
                        /*
                         * public int getIconHeight() { int height = super.getIconHeight(); return
                         * height > 30 ? 30 : height; } public int getIconWidth() { int width =
                         * super.getIconWidth(); return width > 30 ? 30 : width; } public
                         * synchronized void paintIcon(Component c, Graphics g, int x, int y) {
                         * Rectangle oldClip = g.getClipBounds(); // avoid big images to paint over
                         * x > 30 and y > 30 int width = getIconWidth(); int height =
                         * getIconHeight(); g.setClip(oldClip.x, oldClip.y, Math.min(width,
                         * oldClip.width) , Math.min(height, oldClip.height)); super.paintIcon(c, g,
                         * x, y); g.setClip(oldClip.x, oldClip.y, oldClip.width, oldClip.height); }
                         */
                    });
                    setHorizontalTextPosition(SwingConstants.RIGHT);
                } else {
                    setIcon(null);
                }
                setText(value.toString());
            }
            return renderer;
        }
    }
}
