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

package org.modelsphere.jack.awt;

import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

public abstract class AbstractTableCellEditor implements javax.swing.table.TableCellEditor,
        java.awt.event.ActionListener, java.awt.event.ItemListener {
    protected EventListenerList listenerList = new EventListenerList();
    transient protected ChangeEvent changeEvent = null;

    private int clickCountForEditing = 1;

    abstract public java.awt.Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column);

    /**
     * Add a listener to the list that's notified when the editor starts, stops, or cancels editing.
     */
    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    /**
     * Remove a listener from the list that's notified
     */
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    /*
     * Notify all listeners that have registered interest for notification on this event type. The
     * event instance is lazily created using the parameters passed into the fire method.
     * 
     * @see EventListenerList
     */
    protected void fireEditingStopped() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
            }
        }
    }

    /*
     * Notify all listeners that have registered interest for notification on this event type. The
     * event instance is lazily created using the parameters passed into the fire method.
     * 
     * @see EventListenerList
     */
    protected void fireEditingCanceled() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
            }
        }
    }

    /**
     * Tell the editor to cancel editing and not accept any partially edited value.
     */
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    /**
     * Ask the editor if it can start editing using anEvent.
     */
    public boolean isCellEditable(java.util.EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent) anEvent).getClickCount() >= clickCountForEditing;
        }
        return true;
    }

    /**
     * Tell the editor to start editing using anEvent.
     */
    public boolean shouldSelectCell(java.util.EventObject anEvent) {
        return true;
    }

    /**
     * Tell the editor to stop editing and accept any partially edited value as the value of the
     * editor.
     */
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;// done by Editor directly, to be update to use this
        // interface
    }

    // Implementing ActionListener interface, it's the resposability of the
    // sub-classes to
    // call the addActionListener method to its component
    public void actionPerformed(java.awt.event.ActionEvent e) {
        stopCellEditing();
    }

    // Implementing ItemListener interface, it's the resposability of the
    // sub-classes to
    // call the addItemListener method to its component
    public void itemStateChanged(java.awt.event.ItemEvent e) {
        stopCellEditing();
    }

    public Object getCellEditorValue() {
        return null;
    }

    public int getClickCountForEditing() {
        return clickCountForEditing;
    }

    public void setClickCountForEditing(int clickCountForEditing) {
        this.clickCountForEditing = clickCountForEditing;
    }

}
