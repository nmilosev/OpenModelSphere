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

package org.modelsphere.jack.baseDb.screen.plugins;

import java.awt.Component;

import javax.swing.*;
import javax.swing.table.*;

import org.modelsphere.jack.srtool.screen.design.DesignPanel;
import org.modelsphere.jack.srtool.screen.design.DesignTableModel;

public class MultiBooleanRenderer extends JCheckBox implements TableCellRenderer {

    public static final MultiBooleanRenderer singleton = new MultiBooleanRenderer();

    protected MultiBooleanRenderer() {
    }

    public final Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setSelected(value != null && ((Boolean) value).booleanValue());
        setHorizontalAlignment(JLabel.CENTER);
        TableModel model = table.getModel();
        boolean valuesDiffer = model instanceof DesignTableModel ? ((DesignTableModel) model)
                .isValuesDiffer(row) : false;
        if (valuesDiffer && !isSelected) {
            setForeground(table.getForeground());
            setBackground(DesignPanel.DIFFER_NO_FOCUS_COLOR);
        } else if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        return this;
    }

}
