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

// JDK
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ColorEditor extends JPanel implements Editor, ActionListener {

    private org.modelsphere.jack.awt.AbstractTableCellEditor editor;
    private JLabel label;
    private JButton button;

    private ReportPropertiesFrame parent;

    public ColorEditor(ReportPropertiesFrame parent) {
        this.parent = parent;
    }

    public final Component getTableCellEditorComponent(JTable table,
            org.modelsphere.jack.awt.AbstractTableCellEditor tableCellEditorListener, Object value,
            boolean isSelected, int row, int column) {

        this.editor = tableCellEditorListener;
        label = new JLabel();
        setBackground((Color) value);
        button = new JButton("..."); // NOT LOCALIZABLE
        button.setPreferredSize(new Dimension(20, 20));
        button.addActionListener(this);
        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);
        setBorder(UIManager.getBorder("Table.focusCellHighlightBorder")); // NOT LOCALIZABLE
        return this;
    }

    public final boolean stopCellEditing() {
        return true;
    }

    public final Object getCellEditorValue() {
        return getBackground();
    }

    public final void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            Color chosenColor = JColorChooser.showDialog(parent, "Choose a color", button
                    .getParent().getBackground());
            if (chosenColor != null)
                button.getParent().setBackground(chosenColor);
        }

        editor.stopCellEditing();
    }
}
