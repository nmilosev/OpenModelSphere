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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.srtool.screen.design.DesignTableModel;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.sms.db.DbSMSAssociation;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORColumn;

/**
 * 
 * editor for fields: DbORIndex.fConstraint, DbORPrimaryUnique.fIndex, DbORForeign.fIndex
 * 
 */
public final class MultiSMSSemanticalMetaClassEditor implements TableCellEditor {
    protected EventListenerList listenerList = new EventListenerList();
    transient protected ChangeEvent changeEvent = null;
    private Object oldValue;
    private DbObject[] parentDbos;
    private Component comp = null;

    private static final String kTableOrEntity = org.modelsphere.sms.international.LocaleMgr.screen
            .getString("TableOrEntity");
    private static final String kAssociationOrArc = org.modelsphere.sms.international.LocaleMgr.screen
            .getString("AssociationOrArc");
    private static final String kColumnOrAttribute = org.modelsphere.sms.international.LocaleMgr.screen
            .getString("ColumnOrAttribute");

    private ArrayList semanticalMetaClasses = null;

    protected final ActionListener listener = new ComboActionListener();

    public final Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {

        oldValue = value;
        DesignTableModel model = (DesignTableModel) table.getModel();
        parentDbos = model.getDbObjects();

        try {
            if (semanticalMetaClasses == null) {
                semanticalMetaClasses = new ArrayList();
                Enumeration enumeration = MetaClass.enumMetaClasses();
                while (enumeration.hasMoreElements()) {
                    MetaClass metaclass = (MetaClass) enumeration.nextElement();
                    if (((metaclass.getFlags() & MetaClass.UML_EXTENSIBILITY_FILTER) != 0)
                            && DbSMSSemanticalObject.metaClass.isAssignableFrom(metaclass)) {
                        if (metaclass == DbORAbsTable.metaClass)
                            semanticalMetaClasses.add(new DefaultComparableElement(metaclass,
                                    kTableOrEntity));
                        else if (metaclass == DbSMSAssociation.metaClass)
                            semanticalMetaClasses.add(new DefaultComparableElement(metaclass,
                                    kAssociationOrArc));
                        else if (metaclass == DbORColumn.metaClass)
                            semanticalMetaClasses.add(new DefaultComparableElement(metaclass,
                                    kColumnOrAttribute));
                        else
                            semanticalMetaClasses.add(new DefaultComparableElement(metaclass,
                                    metaclass.getGUIName(false, true)));
                    }
                }
                Collections.sort(semanticalMetaClasses);
            }

            JComboBox combo = new JComboBox();
            int selInd = -1;

            String currentMetaclassName = (String) value;
            if (currentMetaclassName == null)
                currentMetaclassName = DbSMSSemanticalObject.metaClass.getJClass().getName();
            MetaClass currentMetaclass = MetaClass.find(currentMetaclassName);
            if (currentMetaclass == null)
                currentMetaclass = DbSMSSemanticalObject.metaClass;

            parentDbos[0].getDb().beginTrans(Db.READ_TRANS);
            Iterator iter = semanticalMetaClasses.iterator();
            while (iter.hasNext()) {
                MetaClass metaclass = (MetaClass) ((DefaultComparableElement) iter.next()).object;
                if (metaclass == DbORAbsTable.metaClass)
                    combo.addItem(new DefaultComparableElement(metaclass, kTableOrEntity));
                else if (metaclass == DbSMSAssociation.metaClass)
                    combo.addItem(new DefaultComparableElement(metaclass, kAssociationOrArc));
                else if (metaclass == DbORColumn.metaClass)
                    combo.addItem(new DefaultComparableElement(metaclass, kColumnOrAttribute));
                else
                    combo.addItem(new DefaultComparableElement(metaclass, metaclass.getGUIName(
                            false, true)));
                if (metaclass == currentMetaclass)
                    selInd = combo.getItemCount() - 1;
            }
            parentDbos[0].getDb().commitTrans();

            // combo.addItem(new DefaultComparableElement(null,
            // getStringForNullValue()));
            combo.setMaximumRowCount(10);
            combo.setSelectedIndex(selInd != -1 ? selInd : combo.getItemCount() - 1);
            combo.addActionListener(listener);
            comp = combo;
            if (isSelected) {
                comp.setBackground(table.getBackground());
                comp.setForeground(table.getForeground());
            } else {
                comp.setBackground(table.getBackground());
                comp.setForeground(table.getForeground());
            }
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(table, ex);
        }
        return comp;
    }

    public final Object getCellEditorValue() {
        Object value = ((DefaultComparableElement) ((JComboBox) comp).getSelectedItem()).object;
        return ((MetaClass) value).getJClass().getName();
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

    class ComboActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            stopCellEditing();
        }
    }

    public Object wrapValue(Object value) {
        if (value != null)
            value = MetaClass.find((String) value);
        if (value == null)
            value = DbSMSSemanticalObject.metaClass;
        String name = ((MetaClass) value).getGUIName(false, true);
        value = ((MetaClass) value).getJClass().getName();
        return new DefaultComparableElement(value, name);
    }

    public final Object unwrapValue(Object value) {
        return value == null ? DbSMSSemanticalObject.metaClass.getJClass().getName()
                : ((DefaultComparableElement) value).object;
    }

}
