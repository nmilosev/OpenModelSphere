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

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;

abstract class AbstractViewer {

    protected AbstractViewer() {
    }

    protected void setViewers(TableCellRenderer renderer, TableCellEditor editor) {
        setTableCellRenderer(renderer);
        setTableCellEditor(editor);
    }

    // table
    private JTable m_table;

    private void setTable(JTable table) {
        m_table = table;
    }

    // property's row
    private int m_row = -1;

    private void setRow(int row) {
        m_row = row;
    }

    private int getRow() {
        return m_row;
    }

    // table cell renderer
    private TableCellRenderer m_tableCellRenderer;

    private void setTableCellRenderer(TableCellRenderer tableCellRenderer) {
        m_tableCellRenderer = tableCellRenderer;
    }

    TableCellRenderer getTableCellRenderer() {
        return m_tableCellRenderer;
    }

    // table cell editor
    private TableCellEditor m_tableCellEditor;

    private void setTableCellEditor(TableCellEditor tableCellEditor) {
        m_tableCellEditor = tableCellEditor;
    }

    TableCellEditor getTableCellEditor() {
        return m_tableCellEditor;
    }

    void setEditorInfo(JTable table, int row) {
        setTable(table);
        setRow(row);
    }

    final void setValueInModel(Object value) {
        if (m_table != null) {
            AbstractTableModel model = (AbstractTableModel) m_table.getModel();
            int row = getRow();
            if (row != -1) {
                model.setValueAt(value, row, 1);
            }
        } // end if
    } // end setValueInModel()

    final Object getValueInModel() {
        Object value = null;

        if (m_table != null) {
            AbstractTableModel model = (AbstractTableModel) m_table.getModel();
            int row = getRow();
            if (row != -1) {
                value = model.getValueAt(row, 1);
            }
        } // end if

        return value;
    } // end getValueInModel()
}
