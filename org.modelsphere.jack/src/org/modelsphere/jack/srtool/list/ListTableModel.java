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

package org.modelsphere.jack.srtool.list;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.Comparable;
import javax.swing.*;
import javax.swing.table.*;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.event.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.preference.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.util.*;

public/* final */class ListTableModel extends DefaultTableModel implements Comparator,
        DbRefreshListener {
    private static final String kSequence = LocaleMgr.screen.getString("Sequence");
    private static final String kComposite = LocaleMgr.screen.getString("Composite");
    private static final String kObjectType = LocaleMgr.screen.getString("ObjectType");

    private static final String LIST_PROPERTIES = "list"; // NOT LOCALIZABLE
    private static final String LIST_PROPERTIES_KEY = "list properties"; // NOT LOCALIZABLE
    private static final PropertiesSet LIST_PROPERTIES_SET;

    private static final String LAZY_STRING = "skdfiwlwefjweorkerjwsdofi"; // NOT LOCALIZABLE - Used for column width evaluation

    // TODO test this!
    static final int MAX_COLUMNS_COUNT = 200;

    private static final int MIN_COLUMN_WIDTH = 4;

    private TerminologyUtil m_terminologyUtil = TerminologyUtil.getInstance();

    //  the current column for sorting rows
    int sortedColumn;

    // The available metafields and udfs for this model (displayed or not)
    // Contains a ColumnDescriptor object for each possible columns.  This contains the title for all columns.
    private ArrayList columnDescriptors = new ArrayList();

    // The root composite
    DbObject root;

    // The metaclass of root
    private MetaClass metaclass;
    // The metaclass filter to get the list of neighbors objects
    MetaClass neighborsMetaClass;

    // The MetaRelationship to populate the list from the root object
    MetaRelationship association;

    // The metaclass limits on the components tree
    private MetaClass[] boundsMetaClasses;

    // Contains a RowData object for each neighbors of the root object
    protected ArrayList rows = new ArrayList();
    protected ArrayList columns = new ArrayList();

    private ListTable list;
    ListDescriptor descriptor;

    // Optimization
    private ArrayList addedObjects = new ArrayList();

    // Optimization
    //private ArrayList     updatedObjects      = new ArrayList();

    static {
        PropertiesManager.installPropertiesSet(LIST_PROPERTIES_KEY, LIST_PROPERTIES);
        LIST_PROPERTIES_SET = PropertiesManager.getPropertiesSet(LIST_PROPERTIES_KEY);
    }

    public boolean isColumnEditable(int column) {
        return !((ColumnDescriptor) columnDescriptors.get(column)).bDisabled;
    }

    public ListTableModel(DbObject root, ListDescriptor descriptor) throws DbException {
        super();
        this.root = root;
        this.boundsMetaClasses = descriptor.boundsMetaClasses;
        this.neighborsMetaClass = descriptor.neighborsMetaClass;
        this.association = descriptor.association;
        this.descriptor = descriptor;

        loadData();
        RowData rowData = null;
        if (rows.size() != 0)
            rowData = (RowData) rows.get(0);

        boolean bShowFields = TerminologyUtil.getShowPhysicalConcepts(new DbObject[] { root });

        // init all available columns ... if not specified within the descriptor, get the default from meta
        if (descriptor.defaultColumns == null) {
            ArrayList fields = neighborsMetaClass.getScreenMetaFields();
            for (int i = 0; i < fields.size(); i++) {
                MetaField field = (MetaField) fields.get(i);
                if (field == association)
                    continue;
                if (!ApplicationContext.getSemanticalModel().isVisibleOnScreen(neighborsMetaClass,
                        field, null, false, ListTableModel.class))
                    continue;
                if (rows.size() != 0) {
                    ColumnDescriptor cd = new ColumnDescriptor(this, rowData.neighbor, fields
                            .get(i));
                    if (!(cd.bDisabled && !bShowFields))
                        columnDescriptors.add(cd);
                } else {
                    ColumnDescriptor cd = new ColumnDescriptor(this, null, fields.get(i));
                    if (!(cd.bDisabled && !bShowFields))
                        columnDescriptors.add(cd);
                }
            }
        } else {
            for (int i = 0; i < descriptor.defaultColumns.length; i++) {
                if (descriptor.defaultColumns[i] instanceof MetaField) {
                    MetaField field = (MetaField) descriptor.defaultColumns[i];
                    if (field == association)
                        continue;
                    if (!ApplicationContext.getSemanticalModel().isVisibleOnScreen(
                            neighborsMetaClass, field, null, false, ListTableModel.class))
                        continue;
                }
                if (rows.size() != 0)
                    columnDescriptors.add(new ColumnDescriptor(this, rowData.neighbor,
                            descriptor.defaultColumns[i]));
                else
                    columnDescriptors.add(new ColumnDescriptor(this, null,
                            descriptor.defaultColumns[i]));
            }
        }

        // include the composite
        boolean includeComposite = descriptor.compositeVisible /*
                                                                * ||
                                                                * !neighborsMetaClass.compositeIsAllowed
                                                                * (root.getMetaClass())
                                                                */;
        if (includeComposite) {
            // Find a super common name for all possible composite metaclasses of the component metaclasses
            boolean[] compositePath = neighborsMetaClass.markCompositePaths();
            ArrayList compositeMetaClasses = new ArrayList();
            Enumeration enumeration = MetaClass.enumMetaClasses();
            while (enumeration.hasMoreElements()) {
                MetaClass metaclass = (MetaClass) enumeration.nextElement();
                if (compositePath[metaclass.getSeqNo()]
                        && (metaclass.getSubMetaClasses().length == 0)
                        && neighborsMetaClass.compositeIsAllowed(metaclass))
                    compositeMetaClasses.add(metaclass);
            }

            String displayText = null;
            if (descriptor.compositeName == null) {
                displayText = kComposite;
                MetaClass metaclass0 = null;
                Iterator iter = compositeMetaClasses.iterator();
                while (iter.hasNext()) {
                    MetaClass metaclass1 = (MetaClass) iter.next();
                    if (metaclass0 == null) {
                        metaclass0 = metaclass1;
                        continue;
                    }
                    metaclass0 = metaclass0.getCommonSuperMetaClass(metaclass1);
                }
                if (metaclass0 != null) // should not be null (DbObject metaclass is super of all metaclasses
                    displayText = ApplicationContext.getSemanticalModel().getDisplayText(
                            metaclass0, null, ListTableModel.class);
            } else {
                displayText = descriptor.compositeName;
            }

            columnDescriptors.add(0, new ColumnDescriptor(this, DbObject.fComposite, displayText));
        }

        if (descriptor.includeSequences) {
            columnDescriptors.add(0, new ColumnDescriptor(this, DbObject.fComponents, kSequence));
        }

        if (descriptor.includeObjectType) {
            columnDescriptors.add(0, new ColumnDescriptor(this, MetaClass.class, kObjectType));
        }

        metaclass = root.getMetaClass();

        rows.clear();
        initStructure(true);
        loadData();
        sortedColumn = loadOrderringColumn();
        sort();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                loadColumnsWidths(false);
            }
        });

    }

    void setListTable(ListTable listtable) {
        this.list = listtable;
    }

    private void initStructure(boolean load) throws DbException {
        columns.clear();
        // remove all udfs ColumnDescriptor - must be refrehed
        Iterator iter = columnDescriptors.iterator();
        while (iter.hasNext()) {
            ColumnDescriptor descriptor = (ColumnDescriptor) iter.next();
            if (descriptor.object instanceof DbUDF)
                iter.remove();
        }

        // include udfs - This is done here to allow refresh of udfs (can be modified externally)
        DbEnumeration dbEnum = root.getProject().getComponents().elements(DbUDF.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbUDF udf = (DbUDF) dbEnum.nextElement();
            if (udf.getUDFMetaClass() == neighborsMetaClass)
                columnDescriptors.add(new ColumnDescriptor(this, null, udf));
        }
        dbEnum.close();

        // Define the columns model contents (visible columns)
        iter = columnDescriptors.iterator();
        while (iter.hasNext()) {
            ColumnDescriptor descriptor = (ColumnDescriptor) iter.next();
            int index = loadIndex(descriptor);
            if (index == -1 && load)
                continue;
            ColumnInfo colInfo = createColumnInfo(descriptor, load);
            if (!columns.contains(colInfo))
                if (!colInfo.bDisabled)
                    columns.add(colInfo);
        }

        // This will sort columns regarding Preferences and GUI order specified in meta (if default)
        Collections.sort(columns);
    }

    private ColumnInfo createColumnInfo(ColumnDescriptor descriptor, boolean load)
            throws DbException {
        ColumnInfo colInfo = new ColumnInfo(descriptor, load ? loadIndex(descriptor) : 1,
                load ? loadOrderring(descriptor) : ListTable.ASC);
        colInfo.width = loadWidth(descriptor);
        return colInfo;
    }

    private void loadData() throws DbException {
        removeDbListeners();
        rows.clear();
        DbEnumeration neighbors = null;
        if (association == DbObject.fComponents)
            neighbors = root.componentTree(neighborsMetaClass, boundsMetaClasses);
        else {
            if (Debug.isDebug())
                Debug
                        .assert2(root.hasField(association),
                                "Invalid List Descriptor.  Root object does not support the specified association.");
            Object value = root.get(association);
            if (value instanceof DbObject)
                addRow((DbObject) value);
            else if (value instanceof DbRelationN)
                neighbors = ((DbRelationN) value).elements(neighborsMetaClass);
        }
        while (neighbors != null && neighbors.hasMoreElements()) {
            addRow(neighbors.nextElement());
        }
        if (neighbors != null)
            neighbors.close();
        installDbListeners();
    }

    private Object getColumValueByName(String name, RowData row) {
        int size = columns.size();
        for (int i = 0; i < size; i++) {
            ColumnInfo ci = (ColumnInfo) columns.get(i);
            if (ci.metafield.getJName().compareTo(name) == 0) {
                return row.getValue(i);
            }
        }
        return null;
    }

    private RowData addRow(DbObject neighbor) throws DbException {
        SrVector values = new SrVector();
        RowData row = new RowData(this, neighbor, new SrVector(), columns, true);

        boolean bRemoveERObjects = false;
        DbObject parent = m_terminologyUtil.isCompositeDataModel(neighbor);
        if (parent == null)
            parent = m_terminologyUtil.isDataModel(neighbor) ? neighbor : null;

        if (parent != null)
            if (m_terminologyUtil.getModelLogicalMode(parent) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                bRemoveERObjects = true;

        //if this list has a relationship
        Boolean hasrel = descriptor.HasRelationship();
        if (hasrel != null) {
            Boolean bValue = new Boolean(false);
            if (bRemoveERObjects
                    && hasrel.booleanValue() == m_terminologyUtil.isObjectAssociation(neighbor))
                rows.add(row);
        } else // not an entity or relationship list
        if (bRemoveERObjects && m_terminologyUtil.isObjectRole(neighbor)) {
            if (m_terminologyUtil.isObjectArcEndRole(neighbor))
                rows.add(row);
        } else
            rows.add(row);

        return row;
    }

    public void refresh() throws DbException {
        DbObject[] selectionBackup = list.getSelectedDbObjects();
        loadData();
        sort();
        fireTableDataChanged();
        list.setSelectedDbObjects(selectionBackup);
    }

    private void installDbListeners() {
        root.addDbRefreshListener(this);
        //DbSemanticalObject.fName.addDbRefreshListener(this);
        Iterator iter = rows.iterator();
        while (iter.hasNext()) {
            RowData row = (RowData) iter.next();
            installDbListeners(row.neighbor, row.composite);
        }
        DbUDFValue.fValue.addDbRefreshListener(this);
    }

    void installDbListeners(boolean install) {
        if (install)
            installDbListeners();
        else
            removeDbListeners();
    }

    private void installDbListeners(DbObject neighbor, DbObject neighborcomposite) {
        neighbor.addDbRefreshListener(this);
        if (neighborcomposite != root)
            neighborcomposite.addDbRefreshListener(this);
    }

    void removeDbListeners() {
        root.removeDbRefreshListener(this);
        //DbSemanticalObject.fName.removeDbRefreshListener(this);
        Iterator iter = rows.iterator();
        while (iter.hasNext()) {
            RowData row = (RowData) iter.next();
            removeDbListeners(row.neighbor, row.composite);
        }
        DbUDFValue.fValue.removeDbRefreshListener(this);
    }

    private void removeDbListeners(DbObject neighbor, DbObject neighborcomposite) {
        if (neighbor != null)
            neighbor.removeDbRefreshListener(this);
        if (neighborcomposite != null && neighborcomposite != root)
            neighborcomposite.removeDbRefreshListener(this);
    }

    public void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (event.metaField == DbObject.fComponents && this.association == DbObject.fComponents
                && event.neighbor != null && event.neighbor.getTransStatus() == Db.OBJ_ADDED
                && neighborsMetaClass.isAssignableFrom(event.neighbor.getMetaClass())) {
            // a component has been added in a sub component of the composite
            if (event.dbo == this.root || descriptor.boundsMetaClasses == null)
                insertDbObject(event.neighbor);
            else if (descriptor.boundsMetaClasses != null) {
                // revalidate with the component tree using the boundsMetaClasses (it could be a sub component blocked with boundsMetaClasses)
                // boolean found = root.componentTree(descriptor.neighborsMetaClass, descriptor.boundsMetaClasses);
                DbObject tempdbo = event.neighbor;
                boolean firstLevel = true;
                while (tempdbo != root) {
                    for (int i = 0; i < descriptor.boundsMetaClasses.length; i++) {
                        if (descriptor.boundsMetaClasses[i]
                                .isAssignableFrom(tempdbo.getMetaClass())) {
                            // If a boundsMetaClass is also this list components metaclass, we must allow the first components of this type to be
                            // added.
                            if (descriptor.neighborsMetaClass.isAssignableFrom(tempdbo
                                    .getMetaClass())) {
                                if (firstLevel) {
                                    firstLevel = false;
                                    break;
                                }
                                return;
                            }
                            return;
                        }
                    }
                    tempdbo = tempdbo.getComposite();
                }
                insertDbObject(event.neighbor);
            }
        } else if (event.dbo != null && event.dbo.getTransStatus() == Db.OBJ_REMOVED) {
            removeDbObject(event.dbo);
        } else if (event.metaField == this.association && this.association != DbObject.fComponents
                && event.neighbor != null && event.op == Db.ADD_TO_RELN
                && neighborsMetaClass.isAssignableFrom(event.neighbor.getMetaClass())) {
            // an object has been added to this model association
            insertDbObject(event.neighbor);
        } else if (event.metaField == this.association && this.association != DbObject.fComponents
                && event.neighbor != null && event.op == Db.REMOVE_FROM_RELN) {
            // an object has been removed from this model association
            removeDbObject(event.neighbor);
        } else if (event.metaField == this.association && this.association != DbObject.fComponents
                && event.dbo != root && event.neighbor != null && event.op == Db.REINSERT_IN_RELN
                && neighborsMetaClass.isAssignableFrom(event.neighbor.getMetaClass())) {
            // an object has been moved within this model association -- do nothing
            return;
        } else if (event.metaField == association) {
            // ordering or ???
            // use refresh -- may be too slow with a repository
            return;
        } else if (event.metaField == DbUDFValue.fValue) { // UDF Value updated
            updateDbObject(event.dbo, null);
        } else if (event.metaField == DbSemanticalObject.fName) { // may be the name of an object in a cell value
            // Optimization: Ensure that event.metaField is visible before updating
            if (indexOf(event.metaField) > -1)
                updateDbObject(event.dbo, event.metaField);
            Iterator iter = rows.iterator();
            while (iter.hasNext()) {
                RowData row = (RowData) iter.next();
                for (int i = 0; i < row.values.size(); i++) {
                    Object value = row.values.get(i);
                    if ((value instanceof DefaultComparableElement)
                            && ((DefaultComparableElement) value).object == event.dbo) {
                        updateDbObject(row.neighbor, getMetaFieldAt(i));
                        break;
                    }
                }
            }
        } else { // other metafields
            // Optimization: Ensure that event.metaField is visible before updating
            if (indexOf(event.metaField) > -1)
                updateDbObject(event.dbo, event.metaField);
        }
    }

    private void insertDbObject(DbObject dbo) throws DbException {
        addRow(dbo);
        int idx = indexOf(dbo);
        installDbListeners(dbo, dbo.getComposite());
        postDbObjectAdded(idx);
    }

    private void removeDbObject(DbObject dbo) throws DbException {
        int idx = indexOf(dbo);
        if (idx < 0 || list == null)
            return;
        DbObject[] selectionBackup = list.getSelectedDbObjects();
        RowData row = (RowData) rows.get(idx);
        removeDbListeners(row.neighbor, row.composite);
        rows.remove(idx);
        fireTableRowsDeleted(idx, idx);
        validateOrdering(association);
        list.setSelectedDbObjects(selectionBackup);
    }

    private void updateDbObject(DbObject dbo, MetaField updatedField) throws DbException {
        int idx = indexOf(dbo);
        if (idx < 0)
            return;
        DbObject[] selectionBackup = list.getSelectedDbObjects();
        ((RowData) rows.get(idx)).loadData(columns);
        //loadData((RowData)rows.get(idx));
        validateOrdering(updatedField);
        int newindex = indexOf(dbo);
        // rows between the old index and new index (may be different if rows have been sorted) need repaint.
        if (idx > newindex)
            fireTableRowsUpdated(newindex, idx);
        else
            fireTableRowsUpdated(idx, newindex);
        list.setSelectedDbObjects(selectionBackup);
    }

    // Postpone update of the list - May avoid many repaint, selection and sort()
    private void postDbObjectAdded(int idx) {
        if (idx == -1)
            return;
        // On the first added row, post a runnable on the event queue to update the list
        // For other added idx, only add the idx to addedObjects
        if (addedObjects.size() == 0) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    DbObject[] selectionBackup = list.getSelectedDbObjects();
                    int firstidx = ((Integer) addedObjects.get(0)).intValue();
                    int lastidx = ((Integer) addedObjects.get(addedObjects.size() - 1)).intValue();
                    // flush the list of added idx to process
                    addedObjects.clear();
                    fireTableRowsInserted(firstidx, lastidx);
                    validateOrdering(null);
                    list.setSelectedDbObjects(selectionBackup);
                }
            });
        }
        addedObjects.add(new Integer(idx));
    }

    int indexOf(DbObject dbo) {
        Iterator iter = rows.iterator();
        int index = -1;
        int count = 0;
        while (iter.hasNext()) {
            if (((RowData) iter.next()).neighbor == dbo) {
                index = count;
                break;
            }
            count++;
        }
        return index;
    }

    private int indexOf(MetaField metafield) {
        if (metafield == null)
            return -1;
        Iterator iter = columns.iterator();
        int index = -1;
        int count = 0;
        while (iter.hasNext()) {
            ColumnInfo column = (ColumnInfo) iter.next();
            if (column.metafield == metafield) {
                index = count;
                break;
            }
            count++;
        }
        return index;
    }

    private void validateOrdering(MetaField metafield) {
        if (metafield == null) {
            sort();
            //list.repaint();
        }
        // if the specified metafield correspond to the sorted column metafield, sort and repaint the table
        else if (sortedColumn != -1 && sortedColumn <= columns.size()
                && metafield == ((ColumnInfo) columns.get(sortedColumn)).metafield) {
            sort();
            //list.repaint();
        }
    }

    protected String buildFullQualifiedName(DbObject dbo) throws DbException {
        return dbo == null ? "" : dbo.getName();
    }

    public int getRowCount() {
        if (rows == null)
            return 0;
        return rows.size();
    }

    public int getColumnCount() {
        if (columns == null)
            return 0;
        return columns.size();
    }

    public String getColumnName(int columnIndex) {
        if (columns == null)
            return "";
        return ((ColumnInfo) columns.get(columnIndex)).title;
    }

    public Class getColumnClass(int columnIndex) {
        return Object.class;
    }

    ColumnDescriptor[] getColumnDescriptors() {
        ColumnDescriptor[] descriptors = new ColumnDescriptor[columnDescriptors.size()];
        for (int i = 0; i < descriptors.length; i++)
            descriptors[i] = (ColumnDescriptor) columnDescriptors.get(i);
        return descriptors;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        RowData row = (RowData) rows.get(rowIndex);
        Object value = row.values.get(columnIndex);
        return value;
    }

    // This list is readonly
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    // within the visible columns only - correspond to the table model index
    int indexInModelOf(ColumnDescriptor descriptor) {
        if (columns == null || descriptor == null)
            return -1;

        int size = columns.size();
        for (int i = 0; i < size; i++) {
            ColumnInfo column = (ColumnInfo) columns.get(i);
            if (column.metafield != null && column.metafield == descriptor.object)
                return i;
            if (column.udf != null && column.udf == descriptor.object)
                return i;
            if (column.customColumn != null && column.customColumn == descriptor.object)
                return i;
            if (column.path != null && column.path == descriptor.object)
                return i;
            if (column.objectType && descriptor.object == MetaClass.class)
                return i;
        }
        return -1;
    }

    public MetaField getMetaFieldAt(int columnIndex) {
        if (columns == null)
            return null;
        return ((ColumnInfo) columns.get(columnIndex)).metafield;
    }

    public DbUDF getUDFAt(int columnIndex) {
        if (columns == null)
            return null;
        return ((ColumnInfo) columns.get(columnIndex)).udf;
    }

    public String getRendererAt(int columnIndex) {
        if (columns == null)
            return null;
        return ((ColumnInfo) columns.get(columnIndex)).renderer;
    }

    public Integer getColumnOrdering(int columnIndex) {
        if (columns == null)
            return null;
        return ((ColumnInfo) columns.get(columnIndex)).ordering;
    }

    public String getDbValueFullQualifiedName(int row, int col) {
        return (String) ((RowData) rows.get(row)).semObjectFullNameValues.get(col);
    }

    public DbObject getDbObjectAt(int rowIndex) {
        RowData row = (RowData) rows.get(rowIndex);
        return row.neighbor;
    }

    void insertColumn(ColumnDescriptor descriptor, int insertAt) throws DbException {
        if (indexInModelOf(descriptor) != -1)
            return;

        if (insertAt < 0)
            insertAt = 0;

        // Backup each column state
        saveColumnsPreferences();

        if (sortedColumn >= insertAt) {
            sortedColumn++;
        }

        // Rearrange columns
        ColumnInfo colinfo = createColumnInfo(descriptor, true);
        colinfo.preferredIndex = insertAt - 1;
        colinfo.index = insertAt - 1;
        columns.add(colinfo);
        Collections.sort(columns);

        // backup selection
        DbObject[] selObjs = list.getSelectedDbObjects();

        root.getDb().beginReadTrans();
        loadData();
        root.getDb().commitTrans();

        fireTableStructureChanged();

        // restore columns state
        loadColumnsPreferences();

        sortByColumn(sortedColumn);
        // restore selection
        list.setSelectedDbObjects(selObjs);
    }

    void setColumns(ColumnDescriptor[] descriptors, int insertAt) throws DbException {
        saveColumnsPreferences();

        if (insertAt < 0)
            insertAt = 0;

        // Backup each column state
        saveColumnsPreferences();
        // backup selection
        DbObject[] selObjs = list.getSelectedDbObjects();

        java.util.List newVisibleDescriptors = Arrays.asList(descriptors);
        Iterator iter = columnDescriptors.iterator();
        while (iter.hasNext()) {
            ColumnDescriptor descriptor = (ColumnDescriptor) iter.next();
            int index = indexInModelOf(descriptor); // index in visible columns
            // already shown
            if ((index > -1) && newVisibleDescriptors.contains(descriptor)) {
                continue;
            }
            // already hiden
            if ((index == -1) && !newVisibleDescriptors.contains(descriptor)) {
                continue;
            }
            // remove column
            if (index > -1) {
                if (sortedColumn == index) {
                    sortedColumn = 0;
                } else if (sortedColumn > index) {
                    sortedColumn = sortedColumn - 1;
                }
                columns.remove(index);
            }
            // add column
            else {
                ColumnInfo colinfo = createColumnInfo(descriptor, true);
                colinfo.preferredIndex = insertAt - 1;
                colinfo.index = insertAt - 1;
                columns.add(colinfo);
            }
        }

        Collections.sort(columns);

        root.getDb().beginReadTrans();
        loadData();
        root.getDb().commitTrans();

        fireTableStructureChanged();

        // restore columns state
        loadColumnsPreferences();

        sortByColumn(sortedColumn);
        // restore selection
        list.setSelectedDbObjects(selObjs);
    }

    void removeColumn(ColumnDescriptor descriptor) throws DbException {
        // Backup each column state
        saveColumnsPreferences();

        int index = indexInModelOf(descriptor);
        if (index == -1)
            return;
        if (sortedColumn == index) {
            sortedColumn = 0;
        } else if (sortedColumn > index) {
            sortedColumn = sortedColumn - 1;
        }
        // backup selection
        DbObject[] selObjs = list.getSelectedDbObjects();

        // rearrange columns
        columns.remove(index);
        Collections.sort(columns);

        root.getDb().beginReadTrans();
        loadData();
        root.getDb().commitTrans();

        fireTableStructureChanged();

        // restore columns state
        loadColumnsPreferences();

        sortByColumn(sortedColumn);
        // restore selections
        list.setSelectedDbObjects(selObjs);
    }

    private void restructureData(int oldindex, int newindex) {
        if (oldindex == -1 || newindex == -1)
            throw new RuntimeException("Invalid indexes"); // NOT LOCALIZABLE
        if (oldindex == newindex)
            return;
        Iterator iter = rows.iterator();
        while (iter.hasNext()) {
            RowData row = (RowData) iter.next();
            row.restructurate(oldindex, newindex);
        }
    }

    // Apply current column orderring to the model
    private void saveColumnsPreferences() {
        Iterator iter = columns.iterator();
        int index = -1;
        ArrayList rowsConverted = new ArrayList();
        while (iter.hasNext()) {
            index++;
            ColumnInfo col = (ColumnInfo) iter.next();
            col.index = list.convertColumnIndexToView(index);
            // ensure index switch is done once
            if (!rowsConverted.contains(new Integer(index))) {
                restructureData(index, col.index);
                rowsConverted.add(new Integer(col.index));
            }
            TableColumn column = list.getColumnModel().getColumn(
                    list.convertColumnIndexToView(index));
            if (column != null)
                col.width = column.getWidth();
        }
        // reavaluate sorted column index
        sortedColumn = list.convertColumnIndexToView(sortedColumn);
    }

    private void loadColumnsPreferences() {
        loadColumnsIndexes();
        loadColumnsWidths(false);
    }

    private void loadColumnsWidths(boolean useDefault) {
        TableColumnModel colModel = list.getColumnModel();
        if (colModel == null)
            return;
        Iterator iter = columns.iterator();
        int index = -1;
        while (iter.hasNext()) {
            index++;
            ColumnInfo col = (ColumnInfo) iter.next();
            TableColumn column = list.getColumnModel().getColumn(index);
            if (column == null)
                continue;
            if (!useDefault && col.width > MIN_COLUMN_WIDTH) {
                column.setPreferredWidth(col.width);
            } else if (list != null) {
                // Try evaluation a proper width
                Font font = list.getTableHeader().getFont();
                FontMetrics fm = list.getTableHeader().getFontMetrics(font);
                col.width = SwingUtilities.computeStringWidth(fm, col.title);
                boolean stringcolumn = false;
                if (col.metafield != null) {
                    Class type = col.metafield.getJField().getType();
                    if (String.class.isAssignableFrom(type))
                        stringcolumn = true;
                    else if (DbObject.class.isAssignableFrom(type))
                        stringcolumn = true;
                    else if (Domain.class.isAssignableFrom(type))
                        stringcolumn = true;
                } else if (col.objectType || col.stringUdf)
                    stringcolumn = true;
                else if (col.customColumn != null) {
                    int defwidth = col.customColumn.getDefaultWidth();
                    if (defwidth < 5)
                        stringcolumn = true;
                    else
                        col.width = defwidth;
                }
                //else if (col.udf)
                if (stringcolumn)
                    col.width = Math.max(SwingUtilities.computeStringWidth(fm, LAZY_STRING),
                            col.width);
                col.width = Math.max(MIN_COLUMN_WIDTH, col.width);
                column.setPreferredWidth(col.width + 25); // 25 is an approximate of the arrow icon size added with some extra margins
            }
        }
    }

    private void loadColumnsIndexes() {
        Iterator iter = columns.iterator();
        int index = -1;
        while (iter.hasNext()) {
            index++;
            ColumnInfo col = (ColumnInfo) iter.next();
            col.index = list.convertColumnIndexToView(index);
        }
    }

    public int compare(Object v1, Object v2) {
        if (getColumnOrdering(sortedColumn) == null)
            return 0;
        int ascending = (getColumnOrdering(sortedColumn)).intValue();
        if (v1 == null && v2 == null) {
            return 0;
        } else if (v2 == null) {
            return 1 * ascending;
        } else if (v1 == null) {
            return -1 * ascending;
        }

        Object o1 = ((RowData) v1).values.get(sortedColumn);
        Object o2 = ((RowData) v2).values.get(sortedColumn);

        // If both values are null, return 0.
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o2 == null) { // Define null less than everything.
            return 1 * ascending;
        } else if (o1 == null) {
            return -1 * ascending;
        }

        if (o1 instanceof Number && o2 instanceof Number) {
            Number n1 = (Number) o1;
            double d1 = n1.doubleValue();
            Number n2 = (Number) o2;
            double d2 = n2.doubleValue();

            if (d1 == d2) {
                return 0;
            } else if (d1 > d2) {
                return 1 * ascending;
            } else {
                return -1 * ascending;
            }

        } else if (o1 instanceof Boolean && o2 instanceof Boolean) {
            Boolean bool1 = (Boolean) o1;
            boolean b1 = bool1.booleanValue();
            Boolean bool2 = (Boolean) o2;
            boolean b2 = bool2.booleanValue();

            if (b1 == b2) {
                return 0;
            } else if (b1) {
                return 1 * ascending;
            } else {
                return -1 * ascending;
            }

        } else if (o1 instanceof Comparable && o2 instanceof Comparable) {
            Comparable c1 = (Comparable) o1;
            Comparable c2 = (Comparable) o2; // superflous cast, no need for it!

            int compare = c1.compareTo(c2) * ascending;
            if (!(o1 instanceof DefaultComparableElement)
                    || !(o2 instanceof DefaultComparableElement))
                return compare;
            if (compare != 0)
                return compare;

            // dbos may have the same names ... make sure this is not the same object
            if ((compare != 0)
                    || (((DefaultComparableElement) o1).object != ((DefaultComparableElement) o2).object))
                return compare;

            // If sorted column is the composite column and if the sequence is displayed, we must use the sequence as
            // a second sorted column.
            int sequenceIndex = indexInModelOf(new ColumnDescriptor(this, DbObject.fComponents, ""));
            int compositeIndex = indexInModelOf(new ColumnDescriptor(this, DbObject.fComposite, ""));
            boolean includesequence = sequenceIndex > -1 && compositeIndex > -1
                    && compositeIndex == sortedColumn;

            if (!includesequence)
                return 0;

            Integer seq1 = (Integer) ((RowData) v1).values.get(sequenceIndex);
            Integer seq2 = (Integer) ((RowData) v2).values.get(sequenceIndex);

            return seq1.compareTo(seq2);
        } else { // default
            String s1 = o1.toString();
            String s2 = o2.toString();
            return s1.compareTo(s2) * ascending;
        }
    }

    public void sort() {
        Collections.sort(rows, this);
    }

    public void sortByColumn(int column) {
        this.sortedColumn = column;
        sort();
    }

    // Add a mouse listener to the Table to trigger a table sort
    // when a column heading is clicked in the JTable.
    public void addMouseListenerToHeaderInTable(final JTable table) {
        final ListTableModel sorter = this;
        final JTable tableView = table;
        MouseAdapter listMouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                TableColumnModel columnModel = tableView.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int column = tableView.convertColumnIndexToModel(viewColumn);
                if (/* e.getClickCount() == 1 && */column != -1
                        && !SwingUtilities.isRightMouseButton(e) && !e.isPopupTrigger()) {
                    int shiftPressed = e.getModifiers() & InputEvent.SHIFT_MASK;
                    boolean ascending = (shiftPressed == 0);
                    Integer val = getColumnOrdering(column);
                    if (val == null)
                        return;
                    if (column == sortedColumn) {
                        // change the state of the column
                        if (val.equals(ListTable.ASC))
                            ((ColumnInfo) columns.get(column)).ordering = ListTable.DESC;
                        else
                            ((ColumnInfo) columns.get(column)).ordering = ListTable.ASC;
                    }
                    DbObject[] selectionBackup = list.getSelectedDbObjects();
                    sorter.sortByColumn(column);
                    list.setSelectedDbObjects(selectionBackup);
                    // TODO:  I should not have to repaint???!
                    list.repaint();
                    list.getTableHeader().repaint();
                }
            }
        };
        JTableHeader th = tableView.getTableHeader();
        th.addMouseListener(listMouseListener);
    }

    private int loadIndex(ColumnDescriptor descriptor) {
        if (descriptor.object == null || LIST_PROPERTIES == null || root == null
                || neighborsMetaClass == null)
            return -1;
        String key = neighborsMetaClass.getJClass().getName() + "_"; // NOT LOCALIZABLE
        key += descriptor.getID();
        key += "_index"; // NOT LOCALIZABLE
        int index = LIST_PROPERTIES_SET.getPropertyInteger(root.getMetaClass().getJClass(), key,
                new Integer(1)).intValue();
        return index;
    }

    private void saveIndex(ColumnDescriptor descriptor, int index) {
        if (descriptor.object == null || LIST_PROPERTIES == null || root == null
                || neighborsMetaClass == null)
            return;
        String key = neighborsMetaClass.getJClass().getName() + "_"; // NOT LOCALIZABLE
        key += descriptor.getID();
        key += "_index"; // NOT LOCALIZABLE
        LIST_PROPERTIES_SET.setProperty(root.getMetaClass().getJClass(), key, index);
    }

    private int loadWidth(ColumnDescriptor descriptor) {
        if (descriptor.object == null || LIST_PROPERTIES == null || root == null
                || neighborsMetaClass == null)
            return -1;
        String key = neighborsMetaClass.getJClass().getName() + "_"; // NOT LOCALIZABLE
        key += descriptor.getID();
        key += "_width"; // NOT LOCALIZABLE
        int width = LIST_PROPERTIES_SET.getPropertyInteger(root.getMetaClass().getJClass(), key,
                new Integer(-1)).intValue();
        return width;
    }

    private void saveWidth(ColumnDescriptor descriptor, int width) {
        if (descriptor.object == null || LIST_PROPERTIES == null || root == null
                || neighborsMetaClass == null)
            return;
        String key = neighborsMetaClass.getJClass().getName() + "_"; // NOT LOCALIZABLE
        key += descriptor.getID();
        key += "_width"; // NOT LOCALIZABLE
        LIST_PROPERTIES_SET.setProperty(root.getMetaClass().getJClass(), key, width);
    }

    private Integer loadOrderring(ColumnDescriptor descriptor) {
        if (descriptor.object == null || LIST_PROPERTIES == null || root == null
                || neighborsMetaClass == null)
            return ListTable.ASC;
        String key = neighborsMetaClass.getJClass().getName() + "_"; // NOT LOCALIZABLE
        key += descriptor.getID();
        key += "_sort"; // NOT LOCALIZABLE
        int orderring = LIST_PROPERTIES_SET.getPropertyInteger(root.getMetaClass().getJClass(),
                key, ListTable.ASC).intValue();
        return orderring == -1 ? ListTable.DESC : ListTable.ASC;
    }

    private void saveOrderring(ColumnDescriptor descriptor, Integer ordering) {
        if (descriptor.object == null || LIST_PROPERTIES == null || root == null
                || neighborsMetaClass == null)
            return;
        String key = neighborsMetaClass.getJClass().getName() + "_"; // NOT LOCALIZABLE
        key += descriptor.getID();
        key += "_sort"; // NOT LOCALIZABLE
        LIST_PROPERTIES_SET.setProperty(root.getMetaClass().getJClass(), key,
                (ordering == null ? ListTable.ASC : ordering).intValue());
    }

    private int loadOrderringColumn() {
        if (LIST_PROPERTIES == null || root == null || neighborsMetaClass == null)
            return 0;
        String key = neighborsMetaClass.getJClass().getName() + "_sort"; // NOT LOCALIZABLE
        int orderring = LIST_PROPERTIES_SET.getPropertyInteger(root.getMetaClass().getJClass(),
                key, new Integer(0)).intValue();
        return orderring;
    }

    private void saveOrderringColumn() {
        if (LIST_PROPERTIES == null || root == null || neighborsMetaClass == null)
            return;
        String key = neighborsMetaClass.getJClass().getName() + "_sort"; // NOT LOCALIZABLE
        LIST_PROPERTIES_SET.setProperty(root.getMetaClass().getJClass(), key, sortedColumn);
    }

    void saveStucture() {
        if (columns == null || columnDescriptors == null)
            return;
        final DbObject[] selection = list.getSelectedDbObjects();
        saveColumnsPreferences();
        Collections.sort(columns);
        Iterator iter = columnDescriptors.iterator();
        while (iter.hasNext()) {
            ColumnDescriptor descriptor = (ColumnDescriptor) iter.next();
            int index = indexInModelOf(descriptor);
            if (index == -1) {
                saveIndex(descriptor, index);
                continue;
            }
            // get the screen order
            ColumnInfo column = (ColumnInfo) columns.get(index);
            saveIndex(descriptor, column.index);
            saveOrderring(descriptor, column.ordering);
            saveWidth(descriptor, column.width);
        }
        saveOrderringColumn();
        sort();
        fireTableStructureChanged();
        fireTableDataChanged();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                loadColumnsWidths(false);
                list.setSelectedDbObjects(selection);
                list.repaint();
            }
        });
    }

    void ressetStucture() throws DbException {
        root.getDb().beginReadTrans();
        initStructure(false);
        loadData();
        root.getDb().commitTrans();
        sortedColumn = 0;
        sort();
        fireTableStructureChanged();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                loadColumnsWidths(true);
                list.repaint();
            }
        });
    }

    private String getKey(String objectname) {
        if (objectname == null)
            return "";
        if (objectname.indexOf("org.modelsphere.jack.baseDb.db.") == 0) // NOT LOCALIZABLE
            return objectname.substring("org.modelsphere.jack.baseDb.db.".length(), objectname
                    .length()); // NOT LOCALIZABLE
        if (objectname.indexOf("org.modelsphere.") == 0) // NOT LOCALIZABLE
            return objectname.substring("org.modelsphere.".length(), objectname.length()); // NOT LOCALIZABLE
        return objectname;
    }

    void lockGUI() {
        installDbListeners(false);
    }

    void unlockGUI() {
        installDbListeners(true);
        try {
            root.getDb().beginReadTrans();
            refresh();
            root.getDb().commitTrans();
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        }
    }

}
