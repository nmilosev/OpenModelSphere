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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import org.modelsphere.jack.baseDb.db.srtypes.SrFont;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MultiSrFontEditor implements ActionListener, TableCellEditor {
    public static final MultiSrFontEditor singleton = new MultiSrFontEditor();
    transient protected ChangeEvent changeEvent = null;
    protected EventListenerList listenerList = new EventListenerList();
    private JTable table;
    private JButton actionBtn = new JButton("");
    private Font m_font;
    private int row;

    // implements TableCellEditor
    public final Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        // this.editor = editor;
        this.table = table;
        this.row = row;

        if (value instanceof Font) {
            m_font = (Font) value;
            if (m_font != null) {
                SrFont srfont = new SrFont(m_font);
                String text = srfont.toString();
                actionBtn.setText(text);
            }
        } // end if
        actionBtn.addActionListener(this);
        actionBtn.setBackground(table.getSelectionBackground());
        return actionBtn;
    } // end getTableCellEditorComponent()

    // implements TableCellEditor
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    // implements TableCellEditor
    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    // implements TableCellEditor
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    // implements TableCellEditor
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    // implements TableCellEditor
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    // implements TableCellEditor
    public boolean isCellEditable(EventObject e) {
        if (e instanceof MouseEvent) {
            return (((MouseEvent) e)).getClickCount() >= 2;
        }
        return true;
    }

    // implements TableCellEditor
    public Object getCellEditorValue() {
        return m_font;
    }

    // implements ActionListener
    public final void actionPerformed(ActionEvent ev) {
        if (m_font == null) {
            TableModel model = table.getModel();
            Object obj = model.getValueAt(row, 1);
            if (obj instanceof Font) {
                m_font = (Font) obj;
            }
        }

        m_font = SrFontEditor.doActionPerformed(table, row, m_font, actionBtn);
    } // end actionPerformed()

    //
    // protected and private methods
    //

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

} // end MultiSrFontEditor
