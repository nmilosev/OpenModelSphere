/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.plugins.report.screen;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class BooleanRenderer extends JCheckBox implements Renderer {
    private static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    public static final BooleanRenderer singleton = new BooleanRenderer();

    protected BooleanRenderer() {
    }

    public final Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setSelected(value != null && ((Boolean) value).booleanValue());
        setHorizontalAlignment(javax.swing.JLabel.CENTER);
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(Color.white);
            setForeground(table.getForeground());
        }
        if (hasFocus)
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder")); // NOT LOCALIZABLE
        else
            setBorder(noFocusBorder);
        return this;
    }

    public int getDisplayWidth() {
        return 40;
    }
}
