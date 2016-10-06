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

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import org.modelsphere.jack.baseDb.screen.MultiDefaultRenderer;
import org.modelsphere.jack.srtool.list.ListTable;
import org.modelsphere.jack.srtool.screen.design.DesignPanel;
import org.modelsphere.jack.srtool.screen.design.DesignTableModel;

public class MultiLookupDescriptionRenderer extends MultiDefaultRenderer {
    private static JPanel panel;
    private static JLabel label;
    private static JButton textEditorBtn = new JButton();

    public static final Icon kTextToWrite = org.modelsphere.jack.srtool.international.LocaleMgr.screen
            .getImageIcon("TextToWrite");
    public static final Icon kTextWritten = org.modelsphere.jack.srtool.international.LocaleMgr.screen
            .getImageIcon("TextWritten");

    private static Border noFocusBorder = new EmptyBorder(1, 2, 1, 2);

    public static final MultiLookupDescriptionRenderer singleton = new MultiLookupDescriptionRenderer();

    protected MultiLookupDescriptionRenderer() {
    }

    public final Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        if (panel == null) {
            panel = new JPanel(new BorderLayout());
            label = new JLabel();
            textEditorBtn.setName("TextEditorBtn"); // NOT LOCALIZABLE - For QA
        }
        panel.add(label, BorderLayout.CENTER);
        panel.add(textEditorBtn, BorderLayout.EAST);
        label.setText((String) value);

        if (table instanceof ListTable)
            textEditorBtn.setVisible(false);
        else {
            textEditorBtn.setVisible(true);
            if (value == null) {
                textEditorBtn.setIcon(kTextToWrite);
            } else {
                textEditorBtn.setIcon(((String) value).length() > 0 ? kTextWritten : kTextToWrite);
            }
        }
        TableModel model = table.getModel();
        boolean valuesDiffer = model instanceof DesignTableModel ? ((DesignTableModel) model)
                .isValuesDiffer(row) : false;
        if (valuesDiffer && !isSelected) {
            label.setForeground(table.getForeground());
            label.setBackground(DesignPanel.DIFFER_NO_FOCUS_COLOR);
            panel.setBackground(DesignPanel.DIFFER_NO_FOCUS_COLOR);
            textEditorBtn.setBackground(DesignPanel.DIFFER_NO_FOCUS_COLOR);
        } else if (isSelected) {
            label.setBackground(table.getBackground());
            label.setForeground(table.getSelectionForeground());
            panel.setBackground(table.getSelectionBackground());
            textEditorBtn.setBackground(table.getSelectionBackground());
        } else {
            label.setBackground(table.getBackground());
            label.setForeground(table.getForeground());
            panel.setBackground(table.getBackground());
            textEditorBtn.setBackground(table.getBackground());
        }
        label.setBorder(null);
        label.setFont(table.getFont());
        if (hasFocus) {
            panel.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));// NOT
            // LOCALIZABLE
        } else {
            panel.setBorder(noFocusBorder);
        }
        panel.doLayout();
        return panel;
    }

    public void updateUI() {
        super.updateUI();
        if (panel != null)
            panel.updateUI();
        if (label != null)
            label.updateUI();
        if (textEditorBtn != null)
            textEditorBtn.updateUI();
    }

}
