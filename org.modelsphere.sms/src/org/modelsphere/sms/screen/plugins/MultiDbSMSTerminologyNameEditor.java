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

/*
 * Created on Nov 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author nicolask
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package org.modelsphere.sms.screen.plugins;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.design.DesignTableModel;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.db.DbSMSNotation;
import org.modelsphere.sms.db.DbSMSProject;

public class MultiDbSMSTerminologyNameEditor implements TableCellEditor {

    protected EventListenerList listenerList = new EventListenerList();
    transient protected ChangeEvent changeEvent = null;
    protected Object oldValue;
    protected DbObject[] parentDbos;
    private Component comp = null;

    boolean noEditorOnNullValue = false;
    protected final ActionListener listener = new ComboActionListener();

    public MultiDbSMSTerminologyNameEditor() {
        setNoEditorOnNullValue(true);
    }

    public boolean isNoEditorOnNullValue() {
        return noEditorOnNullValue;
    }

    public void setNoEditorOnNullValue(boolean noEditorOnNullValue) {
        this.noEditorOnNullValue = noEditorOnNullValue;
    }

    public final Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {

        if (noEditorOnNullValue)
            if (value == null) {
                Toolkit.getDefaultToolkit().beep();
                return null;
            }

        oldValue = value;
        DesignTableModel model = (DesignTableModel) table.getModel();
        parentDbos = model.getDbObjects();
        // renderer = model.getRenderer(row, column);
        try {
            JComboBox combo = new JComboBox();
            parentDbos[0].getDb().beginTrans(Db.READ_TRANS);
            DbObject currentDbo = (DbObject) unwrapValue(value);
            int selInd = -1;
            Collection dbos = getSelectionSet(parentDbos);
            Iterator iter = dbos.iterator();
            while (iter.hasNext()) {
                DbObject dbo = (DbObject) iter.next();
                combo.addItem(new DefaultComparableElement(dbo, dbo.getName(), getUserObject(dbo)));
                if (dbo == currentDbo)
                    selInd = combo.getItemCount() - 1;
            }
            parentDbos[0].getDb().commitTrans();

            if (getStringForNullValue() != null)
                combo.addItem(new DefaultComparableElement(null, getStringForNullValue()));
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
            configureJComboBox(combo);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(table, ex);
        }
        return comp;
    }

    protected final String getStringForNullValue() {
        return null;
    }

    protected final Collection getSelectionSet(DbObject parentDbo) throws DbException {
        ArrayList dbos = new ArrayList();
        if (parentDbo instanceof DbBEUseCase) {
            DbBEUseCase useCase = (DbBEUseCase) parentDbo;
            if (useCase.getTerminologyName() == null)
                return dbos;
            if (useCase.getTerminologyName().length() == 0)
                return dbos;
        }
        DbEnumeration dbEnum = parentDbo.getProject().getComponents().elements(
                DbBENotation.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = dbEnum.nextElement();
            if (dbo instanceof DbBENotation) {
                if (((DbBENotation) dbo).isBuiltIn())
                    dbos.add(dbo);
            } else
                dbos.add(dbo);
        }
        dbEnum.close();
        return dbos;
    }

    protected DbEnumeration getSelectionEnum(DbObject parentDbo) throws DbException {
        return null;
    }

    protected final Collection getSelectionSet(DbObject[] parentDbos) throws DbException {
        ArrayList childs = new ArrayList();
        Collection tempchilds = null;
        for (int i = 0; i < parentDbos.length; i++) {
            tempchilds = getSelectionSet(parentDbos[i]);
            if (tempchilds == null) {
                childs.clear();
                break;
            }
            if (i == 0)
                childs.addAll(tempchilds);
            else {
                Iterator iter = childs.iterator();
                while (iter.hasNext()) {
                    Object element = iter.next();
                    if (tempchilds.contains(element))
                        continue;
                    iter.remove();
                }
            }
            if (childs.size() == 0)
                break;
        }
        return childs;
    }

    public final Object getCellEditorValue() {
        try {
            parentDbos[0].getDb().beginTrans(Db.READ_TRANS);
            Object value = ((DefaultComparableElement) ((JComboBox) comp).getSelectedItem()).object;
            value = wrapValue(value);
            parentDbos[0].getDb().commitTrans();
            return value;
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
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
            try {
                DbSMSProject project = (DbSMSProject) ApplicationContext.getFocusManager()
                        .getCurrentProject();
                project.getDb().beginReadTrans();
                DbBEModel model = (DbBEModel) parentDbos[0].getCompositeOfType(DbBEModel.metaClass);
                DbObject obj = project.findComponentByName(DbBENotation.metaClass, model
                        .getTerminologyName());
                project.getDb().commitTrans();
                if (obj instanceof DbBENotation) {
                    DbBENotation nota = (DbBENotation) obj;
                    nota.getDb().beginReadTrans();
                    int Id = nota.getNotationID().intValue();
                    nota.getDb().commitTrans();
                    if (Id >= 13 && Id <= 19) {
                        JOptionPane
                                .showMessageDialog(
                                        ApplicationContext.getDefaultMainFrame(),
                                        "Changing the terminology is not permitted for this type of model.",
                                        ApplicationContext.getApplicationName(),
                                        JOptionPane.INFORMATION_MESSAGE);

                        for (int i = 0; i < parentDbos.length; i++)
                            parentDbos[i].getDb().abortTrans();

                        cancelCellEditing();
                    } else
                        stopCellEditing();
                }
            } catch (DbException ex) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), ex);
            }
        }
    }

    public Object wrapValue(Object value) throws DbException {
        String name = (value == null ? getStringForNullValue() : ((DbObject) value).getName());
        return new DefaultComparableElement(value, name);
    }

    public final Object unwrapValue(Object value) {
        if (value == null)
            return null;
        else if (value instanceof DefaultComparableElement)
            return ((DefaultComparableElement) value).object;
        if (value instanceof String) {
            try {
                DbSMSProject project = (DbSMSProject) ApplicationContext.getFocusManager()
                        .getCurrentProject();
                project.getDb().beginReadTrans();
                DbBEModel model = (DbBEModel) parentDbos[0].getCompositeOfType(DbBEModel.metaClass);
                DbObject obj = project.findComponentByName(DbBENotation.metaClass, model
                        .getTerminologyName());
                project.getDb().commitTrans();
                return obj;
            } catch (DbException ex) {
                return null;
            }

        }
        return null;
    }

    protected Object getUserObject(DbObject dbo) throws DbException {
        return null;
    }

    protected void configureJComboBox(JComboBox combo) {
    }

}
