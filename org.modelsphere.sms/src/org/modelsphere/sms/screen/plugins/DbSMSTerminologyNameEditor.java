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
 * Created on Nov 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.modelsphere.sms.screen.plugins;

import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;

import org.modelsphere.jack.awt.AbstractTableCellEditor;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.screen.DefaultRenderer;
import org.modelsphere.jack.baseDb.screen.Editor;
import org.modelsphere.jack.baseDb.screen.Renderer;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.screen.model.ScreenModel;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.design.DesignPanel;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.screen.plugins.MultiDbSMSTerminologyNameEditor.ComboActionListener;

/**
 * @author Nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class DbSMSTerminologyNameEditor implements Editor {

    protected EventListenerList listenerList = new EventListenerList();
    transient protected ChangeEvent changeEvent = null;
    private Object oldValue;
    private DbObject parentDbo;
    private Renderer renderer;
    private Component comp = null;

    boolean noEditorOnNullValue = false;
    protected final ActionListener listener = new ComboActionListener();

    public DbSMSTerminologyNameEditor() {
        setNoEditorOnNullValue(true);
    }

    public boolean isNoEditorOnNullValue() {
        return noEditorOnNullValue;
    }

    public void setNoEditorOnNullValue(boolean noEditorOnNullValue) {
        this.noEditorOnNullValue = noEditorOnNullValue;
    }

    public Component getTableCellEditorComponent(ScreenView screenView,
            AbstractTableCellEditor tableCellEditorListener, Object value, boolean isSelected,
            int row, int column) {

        if (noEditorOnNullValue)
            if (value == null) {
                Toolkit.getDefaultToolkit().beep();
                return null;
            }

        oldValue = value;
        ScreenModel model = screenView.getModel();
        parentDbo = (DbObject) model.getParentValue(row);
        try {
            JComboBox combo = new JComboBox();
            parentDbo.getDb().beginTrans(Db.READ_TRANS);
            DbObject currentDbo = (DbObject) unwrapValue(value);
            int selInd = -1;
            Collection dbos = getSelectionSet(parentDbo);
            Iterator iter = dbos.iterator();
            while (iter.hasNext()) {
                DbObject dbo = (DbObject) iter.next();
                combo.addItem(new DefaultComparableElement(dbo, dbo.getName(), getUserObject(dbo)));
                if (dbo == currentDbo)
                    selInd = combo.getItemCount() - 1;
            }
            parentDbo.getDb().commitTrans();

            if (getStringForNullValue() != null)
                combo.addItem(new DefaultComparableElement(null, getStringForNullValue()));
            combo.setMaximumRowCount(10);
            combo.setSelectedIndex(selInd != -1 ? selInd : combo.getItemCount() - 1);
            combo.addActionListener(listener);
            comp = combo;
            if (isSelected) {
                comp.setBackground(screenView.getBackground());
                comp.setForeground(screenView.getForeground());
            } else {
                comp.setBackground(screenView.getBackground());
                comp.setForeground(screenView.getForeground());
            }
            configureJComboBox(combo);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(screenView, ex);
        }
        return comp;
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
            dbos.add(dbo);
        }
        dbEnum.close();
        return dbos;
    }

    protected DbEnumeration getSelectionEnum(DbObject parentDbo) throws DbException {
        return null;
    }

    public final Object getCellEditorValue() {
        try {
            parentDbo.getDb().beginTrans(Db.READ_TRANS);
            Object value = ((DefaultComparableElement) ((JComboBox) comp).getSelectedItem()).object;
            value = wrapValue(value);
            parentDbo.getDb().commitTrans();
            return value;
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
            return null;
        }
    }

    public boolean isCellEditable(EventObject e) {
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
                DbBEModel model = (DbBEModel) parentDbo.getCompositeOfType(DbBEModel.metaClass);
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

                        parentDbo.getDb().abortTrans();

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
                DbBEModel model = (DbBEModel) parentDbo.getCompositeOfType(DbBEModel.metaClass);
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

    protected final String getStringForNullValue() {
        return null;
    }

    protected Object getUserObject(DbObject dbo) throws DbException {
        if (dbo == null || !(dbo instanceof DbSMSStereotype))
            return null;
        return ((DbSMSStereotype) dbo).getIcon();
    }

    protected void configureJComboBox(JComboBox combo) {
        combo.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (!(value instanceof DefaultComparableElement))
                    return this;
                DefaultComparableElement element = (DefaultComparableElement) value;
                if (element.object2 != null) {
                    setIcon(new ImageIcon((Image) element.object2) {
                        public int getIconHeight() {
                            int height = super.getIconHeight();
                            return height > 30 ? 30 : height;
                        }
                    });
                    setHorizontalTextPosition(SwingConstants.LEFT);
                }
                return this;
            }
        });
    }

    @Override
    public int getClickCountForEditing() {
        return 2;
    }
}
