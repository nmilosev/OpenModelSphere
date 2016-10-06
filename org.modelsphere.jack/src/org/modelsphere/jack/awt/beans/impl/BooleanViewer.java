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

package org.modelsphere.jack.awt.beans.impl;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.EventObject;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.event.CellEditorListener;

final class BooleanViewer extends AbstractViewer {

    private JCheckBox m_checkbox = new JCheckBox(); // boolean viewed as a
    // checkbox

    private TableCellRenderer renderer = new TableCellRenderer() {
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Boolean booleanValue = (Boolean) value;
            m_checkbox.setSelected(booleanValue.booleanValue());
            return m_checkbox;
        }
    };

    private TableCellEditor editor = new TableCellEditor() {
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            setEditorInfo(table, row);
            Boolean booleanValue = (Boolean) value;
            m_checkbox.setSelected(booleanValue.booleanValue());
            return m_checkbox;
        }

        public Object getCellEditorValue() {
            return Boolean.TRUE;
        }

        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }

        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        public void removeCellEditorListener(CellEditorListener l) {
        }

        public void addCellEditorListener(CellEditorListener l) {
        }

        public void cancelCellEditing() {
        }

        public boolean stopCellEditing() {
            return true;
        }
    };

    private ActionListener listener = new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            boolean selected = m_checkbox.isSelected();
            setValueInModel(new Boolean(selected));
        }
    };

    BooleanViewer() {
        super();
        super.setViewers(renderer, editor);
        m_checkbox.addActionListener(listener);
    }
} // end BooleanViewer
