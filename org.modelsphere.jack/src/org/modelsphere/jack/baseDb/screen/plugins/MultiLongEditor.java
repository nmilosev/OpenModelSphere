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
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.*;
import javax.swing.event.*;

import org.modelsphere.jack.awt.NumericTextField;

import java.util.EventObject;

public class MultiLongEditor extends NumericTextField implements TableCellEditor {

    protected EventListenerList listenerList = new EventListenerList();
    transient protected ChangeEvent changeEvent = null;

    public MultiLongEditor() {
        super(NumericTextField.LONG);
    }

    public final Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        if (value != null) {
            setText(value.toString());
        }
        if (isSelected) {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        return this;
    }

    public Object getCellEditorValue() {
        String text = getText();
        if (text.length() == 0)
            return null;
        try {
            return new Long(Long.parseLong(text));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public boolean isCellEditable(EventObject e) {
        if (e instanceof MouseEvent) {
            return (((MouseEvent) e)).getClickCount() >= 2;
        }
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    public boolean stopCellEditing() {
        String text = getText();
        if (text.length() == 0) {
            fireEditingStopped();
            return true; // must return false on primitive int to avoid
            // set(null)
        }
        try {
            Long.parseLong(text);
            fireEditingStopped();
            return true;
        } catch (NumberFormatException ex) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            fireEditingStopped();
            return false;
        }
    }

    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    protected void fireEditingStopped() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
            }
        }
    }

    protected void fireEditingCanceled() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
            }
        }
    }

}
