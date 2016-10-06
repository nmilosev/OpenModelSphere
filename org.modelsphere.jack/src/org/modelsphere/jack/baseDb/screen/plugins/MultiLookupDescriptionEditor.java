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
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.screen.*;
import org.modelsphere.jack.srtool.screen.design.DesignTableModel;

import java.util.EventObject;

public class MultiLookupDescriptionEditor implements TableCellEditor, ActionListener {

    protected EventListenerList listenerList = new EventListenerList();
    transient protected ChangeEvent changeEvent = null;

    public static final Icon kTextToWrite = org.modelsphere.jack.srtool.international.LocaleMgr.screen
            .getImageIcon("TextToWrite");
    public static final Icon kTextWritten = org.modelsphere.jack.srtool.international.LocaleMgr.screen
            .getImageIcon("TextWritten");

    private String title = "";
    private MetaField mf;
    private String text;
    private JPanel panel;
    private JLabel label = new JLabel();
    private JButton textEditorBtn = new JButton();
    private JTable table;

    public final Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        text = (value == null ? "" : (String) value);
        this.table = table;
        panel = new JPanel(new BorderLayout());
        textEditorBtn.setName("TextEditorBtn"); // NOT LOCALIZABLE - For QA
        panel.add(label, BorderLayout.CENTER);
        panel.add(textEditorBtn, BorderLayout.EAST);
        DesignTableModel designTable = (DesignTableModel) table.getModel();
        mf = designTable.getMetaFieldAt(row);
        title = mf.getGUIName();
        textEditorBtn.addActionListener(this);
        label.setText(text);
        label.setBorder(null);
        if (text == null) {
            textEditorBtn.setIcon(kTextToWrite);
        } else {
            textEditorBtn.setIcon(text.length() > 0 ? kTextWritten : kTextToWrite);
        }
        // if (isSelected) {
        // assume editor is always selected - workaround to have the selection
        // background for editors with edit click count = 1
        label.setBackground(table.getSelectionBackground());
        label.setForeground(table.getSelectionForeground());
        panel.setBackground(table.getSelectionBackground());
        textEditorBtn.setBackground(table.getSelectionBackground());
        // } else {
        // label.setBackground(table.getBackground());
        // label.setForeground(table.getForeground());
        // panel.setBackground(table.getBackground());
        // textEditorBtn.setBackground(table.getBackground());
        // }
        label.setFont(table.getFont());
        panel.doLayout();
        return panel;
    }

    public final void actionPerformed(ActionEvent ev) {
        String mfName = (mf == null) ? null : mf.getJName();
        boolean doCheckSpell = "m_description".equals(mfName) || "Description".equals(title);
        TextEditorDialog descrDialog = new TextEditorDialog(table, text, title, doCheckSpell);

        descrDialog.setVisible(true);

        if (descrDialog.isTextModified())
            text = descrDialog.getText();

        stopCellEditing();
    }

    public Object getCellEditorValue() {
        return text;
    }

    public boolean isCellEditable(EventObject e) {
        if (e instanceof MouseEvent) {
            return (((MouseEvent) e)).getClickCount() >= 1;
        }
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
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
