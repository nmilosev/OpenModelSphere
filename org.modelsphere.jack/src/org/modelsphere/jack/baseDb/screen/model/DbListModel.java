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

package org.modelsphere.jack.baseDb.screen.model;

import java.util.Enumeration;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.baseDb.screen.DbListView;
import org.modelsphere.jack.baseDb.screen.Renderer;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.SrVector;

public class DbListModel extends javax.swing.table.AbstractTableModel implements ListModel,
        DbRefreshListener, ListDataListener {

    protected ScreenView screenView;
    protected MetaClass listClass;
    protected MetaRelationN[] listRelations;
    protected DbObject semObj;
    protected boolean withApply;
    protected boolean sorted;
    protected ReflectionDescriptionModel columnModel;
    protected Partition[] partitions;
    private static int partIndex = 0;
    private SrVector rows;
    private boolean hasChanged = false;
    private boolean bColumnModelLocked = false;

    public void setColumnLocked(boolean bLocked) {
        bColumnModelLocked = bLocked;
    }

    public boolean isColumnLocked() {
        return bColumnModelLocked;
    }

    public DbListModel(ScreenView screenView, DbObject semObj, MetaRelationN listRelation,
            MetaClass listClass) throws DbException {
        this(screenView, semObj, new MetaRelationN[] { listRelation }, listClass);
    }

    public DbListModel(ScreenView screenView, DbObject semObj, MetaRelationN[] listRelations,
            MetaClass listClass) throws DbException {
        this.screenView = screenView;
        this.semObj = semObj;
        this.listClass = listClass;
        this.listRelations = listRelations;
        int actionMode = screenView.getActionMode();
        withApply = (actionMode & ScreenView.APPLY_ACTION) != 0;
        sorted = ((actionMode & ScreenView.SORTED_LIST) != 0 || listRelations.length > 1);

        columnModel = createColumnDescriptionModel();
        loadPartitions();
        loadRows();
        installTriggers();
    }

    public final ScreenView getScreenView() {
        return screenView;
    }

    protected ReflectionDescriptionModel createColumnDescriptionModel() throws DbException {
        return new ReflectionDescriptionModel(screenView, semObj, listClass, listRelations);
    }

    public final String getColumnName(int column) {
        return columnModel.getGUIName(column);
    }

    public final String getEditorName(int column) {
        return columnModel.getEditorName(column);
    }

    public final Renderer getRenderer(int row, int col) {
        return columnModel.getRenderer(col, 0);
    }

    public final Object getParentValue(int row) {
        return ((DbDescriptionModel) rows.elementAt(row)).getDbObject();
    }

    public final DbDescriptionModel getDbDescriptionModelAt(int row) {
        return (DbDescriptionModel) rows.elementAt(row);
    }

    public final Object[] getPartitions() {
        return partitions;
    }

    public final int getPartitionIndex() {
        return partIndex;
    }

    public final void setPartitionIndex(int index) {
        if (index == -1)
            return;
        partIndex = index;
        rows = partitions[partIndex].rows;
        if (rows == null) {
            try {
                semObj.getDb().beginTrans(Db.READ_TRANS);
                loadRows();
                semObj.getDb().commitTrans();
            } catch (Exception ex) {
                org.modelsphere.jack.util.ExceptionHandler
                        .processUncatchedException(screenView, ex);
            }
        }
        fireTableDataChanged();
    }

    protected void loadPartitions() throws DbException {
        partitions = null;
    }

    private void loadRows() throws DbException {
        rows = loadChildren(partitions == null ? null : partitions[partIndex].partId);
        int nb = rows.size();
        DescriptionModel rowModel = null;

        for (int i = 0; i < nb; i++) {
            DbObject dbObj = (DbObject) rows.elementAt(i);
            rowModel = createRowModel(dbObj);
            rows.setElementAt(rowModel, i);
            rowModel.addListDataListener(this);
        }

        if (partitions != null)
            partitions[partIndex].rows = rows;
    }

    private SrVector loadChildren(Object partId) throws DbException {

        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        SrVector children = new SrVector(10);
        int i, nb;
        for (i = 0; i < listRelations.length; i++) {

            boolean bIsERModel = false;
            DbObject compositeModel = terminologyUtil.isCompositeDataModel(semObj);
            if (compositeModel != null)
                if (terminologyUtil.getModelLogicalMode(compositeModel) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                    bIsERModel = true;

            DbEnumeration comps = ((DbRelationN) semObj.get(listRelations[i])).elements();
            while (comps.hasMoreElements()) {
                DbObject dbo = comps.nextElement();
                if (bIsERModel) // skip if ER model
                    if (terminologyUtil.isObjectRole(dbo))
                        if (!terminologyUtil.isObjectArcEndRole(dbo))
                            continue;

                if (!dbObjectIsAllowed(dbo))
                    continue;

                if (partitions == null || partId.equals(getPartId(dbo))) {
                    if (sorted) {
                        int visibility = dbo.getHideFlags();
                        if ((visibility & DbObject.HIDE_IN_DB_LIST) == 0) {
                            String str = getSortString(dbo);
                            DefaultComparableElement elem = new DefaultComparableElement(dbo, str);
                            children.addElement(elem);
                        }
                    } else {
                        children.addElement(dbo);
                    }
                }
            }
            comps.close();
        }

        if (sorted) {
            children.sort(new CollationComparator());
            for (i = 0, nb = children.size(); i < nb; i++)
                children.setElementAt(((DefaultComparableElement) children.elementAt(i)).object, i);
        }
        return children;
    }

    // Overridden
    protected String getSortString(DbObject dbo) throws DbException {
        MetaRelationship parentRel = ((DbListView) screenView).getParentRel();
        if (parentRel != null)
            dbo = (DbObject) dbo.get(parentRel);
        return dbo.getName();
    }

    public final Enumeration enumRows() {
        if (partitions == null)
            return rows.elements();

        return new Enumeration() {
            private int index = -1;
            private Enumeration enumeration = null;

            public final boolean hasMoreElements() {
                while (true) {
                    if (enumeration != null && enumeration.hasMoreElements())
                        return true;
                    enumeration = null;
                    if (++index >= partitions.length)
                        return false;
                    SrVector rows = partitions[index].rows;
                    if (rows != null)
                        enumeration = rows.elements();
                }
            }

            public final Object nextElement() {
                return enumeration.nextElement();
            }
        };
    }

    private void installTriggers() {
        semObj.addDbRefreshListener(this);
    }

    private void removeTriggers() {
        Enumeration enumeration = enumRows();
        while (enumeration.hasMoreElements()) {
            DbDescriptionModel rowModel = (DbDescriptionModel) enumeration.nextElement();
            rowModel.removeListDataListener(this);
            rowModel.dispose();
        }
        semObj.removeDbRefreshListener(this);
    }

    public void dispose() {
        removeTriggers();
    }

    // //////////////////////////////////////
    // DbRefreshListener SUPPORT
    //
    // Overridden
    public void refreshAfterDbUpdate(DbUpdateEvent evt) throws DbException {
        int i = 0;
        while (evt.metaField != listRelations[i]) {
            if (++i == listRelations.length)
                return;
        }
        if (dbObjectIsAllowed(evt.neighbor))
            refreshAfterDbUpdateAux(evt.neighbor, evt.op);
    }

    protected final void refreshAfterDbUpdateAux(DbObject neighbor, int op) throws DbException {
        SrVector dboRows = rows;
        Object partId = null;
        if (partitions != null) {
            partId = getPartId(neighbor);
            int i = getPartIndex(partId);
            if (i == -1 || partitions[i].rows == null)
                return;
            dboRows = partitions[i].rows;
        }
        if (op == Db.REMOVE_FROM_RELN)
            removeDbObject(dboRows, neighbor);
        else {
            if (sorted) {
                if (op == Db.ADD_TO_RELN)
                    addDbObject(dboRows, neighbor);
            } else {
                if (dboRows.isEmpty()) {
                    ((DbListView) screenView).setModel(null);
                    ((DbListView) screenView).activateTab();
                } else
                    reorder(partId, dboRows, false);
            }
        }
    }

    //
    // End of DbRefreshListener SUPPORT
    // //////////////////////////////////////

    // ////////////////////////////////////
    // ListDataListener SUPPORT
    //
    public final void intervalAdded(ListDataEvent e) {
        // should never be called
    }

    public final void intervalRemoved(ListDataEvent e) {
        // should never be called
    }

    public final void contentsChanged(ListDataEvent e) {
        DbDescriptionModel rowModel = (DbDescriptionModel) e.getSource();
        if (rowModel.hasChanged()) // user change, not db refresh
            hasChanged = true; // set hasChanged before firing tableCellUpdated
        int row = rows.indexOf(rowModel);
        if (row != -1)
            fireTableCellUpdated(row, e.getIndex0());
    }

    //
    // End of ListDataListener SUPPORT
    // /////////////////////////////////////////

    public final int getColumnIndex(MetaField metaField) {
        return columnModel.getPropertyRowIndex(metaField);
    }

    public final int getRowIndex(DbObject dbObj) {
        return getRowIndex(rows, dbObj, 0);
    }

    public final int getRowIndex(SrVector dboRows, DbObject dbObj, int iStart) {
        int index = -1;
        int size = dboRows.size();
        for (int i = iStart; i < size; i++) {
            DbDescriptionModel rowModel = (DbDescriptionModel) dboRows.elementAt(i);
            if (rowModel.getDbObject() == dbObj) {
                index = i;
                break;
            }
        }
        return index;
    }

    private int getPartIndex(Object partId) {
        for (int i = 0; i < partitions.length; i++) {
            if (partId.equals(partitions[i].partId))
                return i;
        }
        return -1;
    }

    private void removeDbObject(SrVector dboRows, DbObject dbObj) throws DbException {
        int index = getRowIndex(dboRows, dbObj, 0);
        if (index != -1) {
            DbDescriptionModel rowModel = (DbDescriptionModel) dboRows.elementAt(index);
            rowModel.removeListDataListener(this);
            rowModel.dispose();
            dboRows.removeElementAt(index);
            if (dboRows == rows)
                fireTableRowsDeleted(index, index);
        }
    }

    private void addDbObject(SrVector dboRows, DbObject dbObj) throws DbException {
        DescriptionModel rowModel = createRowModel(dbObj);
        dboRows.addElement(rowModel);
        rowModel.addListDataListener(this);
        if (dboRows == rows) {
            int i = rows.size() - 1;
            fireTableRowsInserted(i, i);
        }
    }

    protected DescriptionModel createRowModel(DbObject dbObj) throws DbException {
        return new ReflectionDescriptionModel(screenView, dbObj, columnModel);
    }

    /*
     * Rebuilds the model if rows added/removed/reordered in the database. We keep the existing
     * rows, we just move them to the right place in the vector.
     */
    private void reorder(Object partId, SrVector dboRows, boolean refresh) throws DbException {

        SrVector children = loadChildren(partId);
        int nbChanges = 0;
        int i, nb;
        for (i = 0, nb = children.size(); i < nb; i++) {
            DbObject dbo = (DbObject) children.elementAt(i);
            if (i < dboRows.size()
                    && dbo == ((DbDescriptionModel) dboRows.elementAt(i)).getDbObject()) {
                if (refresh)
                    ((DbDescriptionModel) dboRows.elementAt(i)).refresh();
            } else {
                int iFound = getRowIndex(dboRows, dbo, i + 1);
                if (iFound == -1) {

                    DescriptionModel rowModel = createRowModel(dbo);
                    dboRows.insertElementAt(rowModel, i);
                    rowModel.addListDataListener(this);

                    if (dboRows == rows && nbChanges == 0)
                        fireTableRowsInserted(i, i);
                    nbChanges++;
                } else {
                    DbDescriptionModel rowModel = (DbDescriptionModel) dboRows.elementAt(iFound);
                    if (refresh)
                        rowModel.refresh();
                    dboRows.removeElementAt(iFound);
                    dboRows.insertElementAt(rowModel, i);
                    nbChanges += 2;
                }
            }
        }
        while (i < dboRows.size()) {
            DbDescriptionModel rowModel = (DbDescriptionModel) dboRows.elementAt(i);
            rowModel.removeListDataListener(this);
            rowModel.dispose();
            dboRows.removeElementAt(i);
            if (dboRows == rows && nbChanges == 0)
                fireTableRowsDeleted(i, i);
            nbChanges++;
        }
        if (dboRows == rows && nbChanges > 1)
            fireTableDataChanged(); // invalidate all the data if more than one
        // change.
    }

    protected boolean dbObjectIsAllowed(DbObject dbObj) throws DbException {
        return listClass.getJClass().isInstance(dbObj);
    }

    // must be overridden if partitionned list
    protected Object getPartId(DbObject dbObj) throws DbException {
        return null;
    }

    // Overridden
    public int getFixedColumnCount() {
        return Math.min(2, columnModel.getSize() - 1);
    }

    public final int getDisplayWidth(int column) {
        return columnModel.getDescriptionFieldAt(column).getDisplayWidth();
    }

    public final Class getColumnClass(int column) {
        return columnModel.getPropertyClass(column);
    }

    public final boolean isCellEditable(int row, int column) {
        if (!withApply)
            return false;
        return getDbDescriptionModelAt(row).isEditable(column);
    }

    public final boolean isCellEnabled(int row, int column) {
        return getDbDescriptionModelAt(row).isEnabled(column);
    }

    public final void setValueAt(Object value, int row, int column) {
        DbDescriptionModel rowModel = getDbDescriptionModelAt(row);
        rowModel.setElementAt(value, column);
    }

    public final int getColumnCount() {
        return columnModel.getSize();
    }

    public final int getRowCount() {
        return rows.size();
    }

    public final Object getValueAt(int row, int column) {
        DescriptionModel rowModel = getDbDescriptionModelAt(row);
        return rowModel.getElementAt(column);
    }

    public final DescriptionField getDescriptionFieldAt(int row, int column) {
        DescriptionModel rowModel = getDbDescriptionModelAt(row);
        return ((DbDescriptionModel) rowModel).getDescriptionField(column);
    }

    public final boolean hasChanged() {
        return hasChanged;
    }

    public final void commit() throws DbException {
        if (!hasChanged)
            return;
        Enumeration enumeration = enumRows();
        while (enumeration.hasMoreElements()) {
            DbDescriptionModel rowModel = (DbDescriptionModel) enumeration.nextElement();
            rowModel.commit();
        }
    }

    public final void resetHasChanged() {
        if (!hasChanged)
            return;
        hasChanged = false;
        Enumeration enumeration = enumRows();
        while (enumeration.hasMoreElements()) {
            DbDescriptionModel rowModel = (DbDescriptionModel) enumeration.nextElement();
            rowModel.resetHasChanged();
        }
    }

    public final void refresh() throws DbException {
        if (partitions == null)
            reorder(null, rows, true);
        else {
            for (int i = 0; i < partitions.length; i++) {
                if (partitions[i].rows != null)
                    reorder(partitions[i].partId, partitions[i].rows, true);
            }
        }
    }

    public static class Partition implements Comparable {
        public Object partId;
        public String GUIname;
        public SrVector rows;

        public Partition(Object partId, String GUIname) {
            this.partId = partId;
            this.GUIname = GUIname;
            rows = null;
        }

        public final String toString() {
            return GUIname;
        }

        public final int compareTo(Object obj) {
            return GUIname.compareTo(((Partition) obj).GUIname);
        }
    }
}
