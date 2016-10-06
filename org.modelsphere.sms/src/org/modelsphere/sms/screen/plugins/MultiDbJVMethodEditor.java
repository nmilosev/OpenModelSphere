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
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.DbTreeLookupDialog;
import org.modelsphere.jack.baseDb.screen.DefaultRenderer;
import org.modelsphere.jack.baseDb.screen.MultiDefaultRenderer;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.screen.design.DesignTableModel;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.sms.oo.java.db.DbJVMethod;

public class MultiDbJVMethodEditor implements TableCellEditor {

    protected EventListenerList listenerList = new EventListenerList();
    transient protected ChangeEvent changeEvent = null;

    private Object value;
    private Object oldValue;
    private DbObject[] parentDbos;
    private Component comp;

    protected final ActionListener listener = new DelegateActionListener();

    public final Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.value = value;
        this.oldValue = value;
        DesignTableModel model = (DesignTableModel) table.getModel();
        parentDbos = model.getDbObjects();
        String valuetext = null;
        if (value == null)
            valuetext = ""; // values differs in the selection, we should not
        // specify 'getStringForNullValue()'
        else {
            valuetext = value.toString();
        }
        JButton button = new JButton(valuetext);
        button.addActionListener(listener);
        comp = button;
        return comp;
    }

    protected String getStringForNullValue() {
        return MultiDefaultRenderer.kUnspecified;
    }

    public final Object getCellEditorValue() {
        return value;
    }

    protected boolean isDisplayFullName() {
        return true;
    }

    protected String getTitle() {
        return LocaleMgr.screen.getString("SelectNewCellValue");
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

    /*
     * DbObject currentDbo = (DbObject)renderer.unwrapValue(value); Object selItem =
     * DbTreeLookupDialog.selectOne(comp, LocaleMgr.screen.getString("SelectNewCellValue"), new
     * DbObject[] {parentDbo.getProject()}, new MetaClass[] {DbJVMethod.metaClass}, null,
     * DefaultRenderer.kUnspecified, currentDbo); if (selItem != null) {
     * parentDbo.getDb().beginTrans(Db.READ_TRANS); value = renderer.wrapValue(parentDbo, (selItem
     * instanceof DbObject ? selItem : null)); parentDbo.getDb().commitTrans(); }
     */
    class DelegateActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                DbObject currentDbo = (DbObject) unwrapValue(value);
                Object selItem = DbTreeLookupDialog.selectOne(comp, LocaleMgr.screen
                        .getString("SelectNewCellValue"), new DbObject[] { parentDbos[0]
                        .getProject() }, new MetaClass[] { DbJVMethod.metaClass }, null,
                        DefaultRenderer.kUnspecified, currentDbo);
                if (selItem != null) {
                    parentDbos[0].getDb().beginTrans(Db.READ_TRANS);
                    value = wrapValue((selItem instanceof DbObject ? selItem : null));
                    parentDbos[0].getDb().commitTrans();
                }
                /*
                 * Db db = parentDbos[0].getDb(); db.beginTrans(Db.READ_TRANS); Collection dbos =
                 * getSelectionSet(parentDbos); DbObject currentDbo = (DbObject)unwrapValue(value);
                 * // <selectDbo> takes care of closing the transaction. DefaultComparableElement
                 * item = DbLookupDialog.selectDbo(comp, getTitle(), null, db, dbos, currentDbo,
                 * getStringForNullValue(), isDisplayFullName()); if (item != null) {
                 * db.beginTrans(Db.READ_TRANS); value = wrapValue(item.object); db.commitTrans(); }
                 */
                else {
                    cancelCellEditing();
                    return;
                }
            } catch (Exception ex) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(comp, ex);
            }
            stopCellEditing();
        }
    }

    protected Collection getSelectionSet(DbObject parentDbo) throws DbException {
        ArrayList dbos = new ArrayList();
        DbEnumeration dbEnum = getSelectionEnum(parentDbo);
        if (dbEnum != null) {
            while (dbEnum.hasMoreElements())
                dbos.add(dbEnum.nextElement());
            dbEnum.close();
        }
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

    public Object wrapValue(Object value) throws DbException {
        String name = (value == null ? getStringForNullValue() : ((DbObject) value).getName());
        return new DefaultComparableElement(value, name);
    }

    public final Object unwrapValue(Object value) {
        return value == null ? null : ((DefaultComparableElement) value).object;
    }

}
