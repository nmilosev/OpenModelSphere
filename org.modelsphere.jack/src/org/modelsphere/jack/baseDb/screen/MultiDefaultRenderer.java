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

package org.modelsphere.jack.baseDb.screen;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.screen.design.DesignPanel;
import org.modelsphere.jack.srtool.screen.design.DesignTableModel;
import org.modelsphere.jack.util.StringUtil;

public class MultiDefaultRenderer extends JLabel implements TableCellRenderer {

    public static final MultiDefaultRenderer singleton = new MultiDefaultRenderer();

    public final static String kUnspecified = LocaleMgr.misc.getString("unspecified");
    public final static String kNone = LocaleMgr.misc.getString("none");

    private static Border noFocusBorder = new EmptyBorder(1, 2, 1, 2);

    protected MultiDefaultRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

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
        if (hasFocus) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));// NOT LOCALIZABLE
        } else {
            setBorder(noFocusBorder);
        }
        setFont(table.getFont());
        setIcon(null);
        setText(getDisplayedValue(table, value, row, column));
        return this;
    }

    protected String getDisplayedValue(JTable table, Object value, int row, int col) {
        TableModel model = table.getModel();
        boolean valuesDiffer = model instanceof DesignTableModel ? ((DesignTableModel) model)
                .isValuesDiffer(row) : false;
        if (valuesDiffer) {
            return "";
        }

        String text = StringUtil.getDisplayString(value);

        return text;
    } // end getDisplayedValue()

    // protected String getStringForNullValue() {
    // return "";
    // }

} // end MultiDefaultRenderer
