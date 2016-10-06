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

package org.modelsphere.sms.screen.plugins;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.screen.design.DesignTableModel;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEUseCase;

public class MultiDbBEUseCaseExternalEditor extends JCheckBox implements TableCellEditor {
    private JTable focusTable = null;

    public final Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        setHorizontalAlignment(javax.swing.JLabel.CENTER);
        setSelected(value != null && ((Boolean) value).booleanValue());
        if (isSelected) {
            setBackground(table.getBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        focusTable = table;
        return this;
    }

    public Object getCellEditorValue() {
        return (isSelected() ? Boolean.TRUE : Boolean.FALSE);
    }

    public boolean isCellEditable(EventObject e) {
        if (e instanceof MouseEvent) {
            return (((MouseEvent) e)).getClickCount() >= 2;
        }
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        boolean shouldSelect = true;
        TableModel model = focusTable.getModel();
        if (!(model instanceof DesignTableModel))
            return false;

        DbBEDiagram explodedDiag = null;
        boolean isActionPossible = true;

        DesignTableModel tableModel = (DesignTableModel) model;
        boolean selected = isSelected();
        DbObject[] objects = tableModel.getDbObjects();
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof DbBEUseCase) {
                DbBEUseCase process = (DbBEUseCase) objects[i];
                isActionPossible &= DbBEUseCaseExternalEditor.verifyAction(process, selected);
                if (!isActionPossible) {
                    break;
                }
            } // end if
        } // end for

        if (isActionPossible) {
            setSelected(!selected); // toggle value
            shouldSelect = false;
        } else {
            setSelected(false);
            focusTable.getCellEditor().cancelCellEditing();
            shouldSelect = false;
        } // end if

        return shouldSelect;
    }

    public boolean stopCellEditing() {
        focusTable = null;
        return true;
    }

    public void cancelCellEditing() {
        focusTable = null;
    }

    public void addCellEditorListener(CellEditorListener l) {
    }

    public void removeCellEditorListener(CellEditorListener l) {
    }

    protected void fireEditingStopped() {
    }

    protected void fireEditingCanceled() {
    }

}
