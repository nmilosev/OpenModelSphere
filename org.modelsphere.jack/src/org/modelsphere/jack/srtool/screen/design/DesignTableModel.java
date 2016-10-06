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

package org.modelsphere.jack.srtool.screen.design;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.explorer.ExplorerView;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.ExceptionHandler;

public class DesignTableModel implements TableModel {
    private ArrayList<MetaField> fields = new ArrayList<MetaField>();
    private DbObject[] dbos = new DbObject[] {};
    private ArrayList<DbUDF> udfs = new ArrayList<DbUDF>();

    private ArrayList<RowData> data = new ArrayList<RowData>();// new
    // RowData[]{};

    private DesignTable table;

    DesignTableModel(DesignTable table) {
        this.table = table;
    }

    public int getRowCount() {
        return fields.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
        case 0:
            return DesignPanel.kProperties;
        default:
            return DesignPanel.kValues;
        }
    }

    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 0)
            return String.class;
        return Object.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0)
            return false;
        RowData row = (RowData) data.get(rowIndex);
        return row.editable;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            RowData row = (RowData) data.get(rowIndex);
            if (row == null)
                return null;
            if (row.udf == null) {
                boolean bMarkField = false;
                String sRetVal = null;
                try {
                    sRetVal = ApplicationContext.getSemanticalModel().getDisplayText(row.metaclass,
                            row.metafield, dbos[0], DesignTableModel.class, true);
                    if (sRetVal == null) {
                        sRetVal = ApplicationContext.getSemanticalModel().getDisplayText(
                                row.metaclass, row.metafield, dbos[0], DesignTableModel.class);
                        if (sRetVal == null)
                            sRetVal = row.metafield.getGUIName();
                        bMarkField = true;
                    }
                } catch (DbException dbe) {
                    sRetVal = ApplicationContext.getSemanticalModel().getDisplayText(row.metaclass,
                            row.metafield, DesignTableModel.class);
                    if (sRetVal == null)
                        sRetVal = row.metafield.getGUIName();
                }

                if (bMarkField) {
                    JLabel label = (JLabel) table.getCellRenderer(0, 0);
                    row.marked = true;
                    row.editable = false;
                    Font font = label.getFont();
                    font = new Font(font.getName(), font.getStyle() | Font.ITALIC, font.getSize());
                    label.setFont(font);
                }

                return sRetVal;
            }

            else
                return row.udfName;
        } else {
            if (data.size() == 0)
                return null;
            RowData row = (RowData) data.get(rowIndex);
            Object value = row.equalValues ? row.values[0] : null;
            if (value instanceof DbObject)
                value = row.sValue;
            return value;
        }
    }

    public MetaField getMetaFieldAt(int rowIndex) {
        RowData row = (RowData) data.get(rowIndex);
        return row.metafield;
    }

    public DbObject[] getDbObjects() {
        return dbos;
    }

    public String getDbValueFullQualifiedName(int row) {
        RowData rowd = (RowData) data.get(row);
        return rowd.dbValueFullQualifiedName;
    }

    public String getRendererAt(int row) {
        RowData rowd = (RowData) data.get(row);
        return rowd.renderer;
    }
    
    public RowData getRowData(int row) {
    	RowData rowData = (RowData) data.get(row);;
    	return rowData;
    }

    public String getEditorAt(int row) {
        RowData rowd = (RowData) data.get(row);
        return rowd.editor;
    }

    public void setValueAt(Object aValue, final int rowIndex, int columnIndex) {
        RowData row = (RowData) data.get(rowIndex);
        final MetaField field = row.metafield;
        if (aValue == null && field.getJField().getType().isPrimitive())
            return;
        if (aValue instanceof DefaultComparableElement) {
            aValue = ((DefaultComparableElement) aValue).object;
        }
        try {
            final String fieldname = row.udfName == null ? ApplicationContext.getSemanticalModel()
                    .getDisplayText(row.metaclass, field, DesignPanel.class) : row.udfName;
            final String transName = (field == DbSemanticalObject.fName ? DesignPanel.kRename
                    : MessageFormat.format(DesignPanel.kUpdate0, new Object[] { fieldname }));
            if (field == DbSemanticalObject.fName) {
                // ensure cancel editing on explorer
                Object focus = ApplicationContext.getFocusManager().getFocusObject();
                if (focus instanceof ExplorerView) {
                    ((ExplorerView) focus).cancelEditing();
                }
            }
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, dbos, transName);
            for (int i = 0; i < dbos.length; i++) {
                DbObject dbo = (DbObject) dbos[i];

                if (row.udf == null) {
                    dbo.set(field, aValue);
                } else {
                    dbo.set(row.udf, aValue);
                }
            } // end for
            DbMultiTrans.commitTrans(dbos);
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        }
    }

    public void addTableModelListener(TableModelListener l) {
    }

    public void removeTableModelListener(TableModelListener l) {
    }

    public boolean isValuesDiffer(int rowIndex) {
        RowData row = (RowData) data.get(rowIndex);
        return !row.equalValues;
    }

    ArrayList<MetaField> getFields() {
        return fields;
    }

    ArrayList<DbUDF> getUdfs() {
        return udfs;
    }

    ArrayList<RowData> getData() {
        return data;
    }

    void clear() {
        dbos = new DbObject[] {};
        // data = new RowData[]{};
        data = new ArrayList<RowData>();
    }

    boolean showPhysicalProperties() {
        return TerminologyUtil.getShowPhysicalConcepts(dbos);
    }

    void load() throws DbException {
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        int udfIndex = 0;
        data = new ArrayList<RowData>(fields.size());
        ArrayList<RowData> arrayList = null;
        boolean bRemoveMarkedFields = !showPhysicalProperties();
        if (bRemoveMarkedFields)
            arrayList = new ArrayList<RowData>();
        Iterator<MetaField> iter = fields.iterator();
        while (iter.hasNext()) {
            MetaField field = iter.next();
            RowData row = null;
            if (field == DbUDFValue.fValue) {
                row = new RowData((DbUDF) udfs.get(udfIndex));
                udfIndex++;
            } else
                row = new RowData(field);

            String val = ApplicationContext.getSemanticalModel().getDisplayText(
                    dbos[0].getMetaClass(), row.metafield, dbos[0], DesignTable.class, true);
            if (null == val) {
                data.add(row);
                if (bRemoveMarkedFields)
                    arrayList.add(row);
            } else if (!terminologyUtil.isPureERSet(dbos)) {
                data.add(row);
                if (bRemoveMarkedFields)
                    arrayList.add(row);
            } else {
                data.add(row);
            }
            row.load(dbos);
        }
        if (bRemoveMarkedFields) {
            for (int i = 0; i < arrayList.size(); i++) {
                RowData row = arrayList.get(i);
                fields.remove(row.metafield);
                data.remove(row);
            }
        }
        sortRows();
    }

    private void sortRows() {
        if (data == null)
            return;
        int count = data.size();
        RowData creationTimeRow = null;
        RowData updateTimeRow = null;

        // ensure creation time and update time are located after the UDF
        for (int i = 0; i < count; i++) {
            RowData row = data.get(i);
            if (row.metafield == DbObject.fCreationTime) {
                creationTimeRow = row;
            } else if (row.metafield == DbObject.fModificationTime) {
                updateTimeRow = row;
            }
        }
        if (creationTimeRow != null) {
            data.remove(creationTimeRow);
            data.add(creationTimeRow);
        }
        if (updateTimeRow != null) {
            data.remove(updateTimeRow);
            data.add(updateTimeRow);
        }
    }

    void init() {
        clear();
        DbObject[] dbos = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();

        // Check if Dead objects in the selection (Focus Manager may not have
        // been updated)
        boolean deadObjects = false;
        for (int i = 0; i < dbos.length; i++) {
            if (dbos[i].getTransStatus() == Db.OBJ_REMOVED) {
                deadObjects = true;
                break;
            }
        }
        if (deadObjects)
            dbos = new DbObject[] {};

        ArrayList<MetaField> allMetafields = new ArrayList<MetaField>();
        ArrayList<MetaField> tempMetafields = new ArrayList<MetaField>();
        ArrayList<MetaClass> metaClasses = new ArrayList<MetaClass>();

        Db db = null;
        if (dbos.length > 0) {
            db = dbos[0].getDb();
            for (int i = 1; i < dbos.length; i++) {
                if (dbos[i].getDb() != db) {
                    db = null;
                    break;
                }
            }
        }

        if (db != null && (db.isTerminating() || !db.isValid()))
            dbos = new DbObject[] {};

        for (int i = 0; i < dbos.length; i++) {
            MetaClass metaClass = dbos[i].getMetaClass();
            if (metaClasses.contains(metaClass))
                continue;
            metaClasses.add(metaClass);
            ArrayList<MetaField> metafields = metaClass.getScreenMetaFields();
            if (i == 0) {
                Iterator<MetaField> iter = metafields.iterator();
                while (iter.hasNext()) {
                    MetaField metafield = iter.next();
                    if (metafield.isEditable() && !(metafield instanceof MetaRelationN)) {
                        // must be excluded if multiple dbs in the selection
                        if ((metafield instanceof MetaRelationship) && db == null)
                            continue;
                        allMetafields.add(metafield);
                    }
                }
                continue;
            }
            Iterator<MetaField> iter = metafields.iterator();
            tempMetafields.clear();
            while (iter.hasNext()) {
                MetaField metaField = iter.next();
                if (allMetafields.contains(metaField))
                    tempMetafields.add(metaField);
            }
            allMetafields.clear();
            if (tempMetafields.size() == 0)
                break;
            allMetafields.addAll(tempMetafields);
        }
        this.dbos = dbos;

        // For each dbo, check if metafields should be visible or not. If not,
        // remove the metafield
        // from list of visible metafields
        // ArrayList udfFields = new ArrayList();
        udfs.clear();
        try {
            DbMultiTrans.beginTrans(Db.READ_TRANS, dbos, null);
            for (int i = 0; i < dbos.length; i++) {
                Iterator<MetaField> iter = allMetafields.iterator();
                while (iter.hasNext()) {
                    MetaField field = iter.next();
                    if (!ApplicationContext.getSemanticalModel().isVisibleOnScreen(
                            dbos[i].getMetaClass(), field, dbos[i], dbos.length > 1,
                            DesignPanel.class))
                        iter.remove();
                }
            }

            // Append the udfs if selection on one metaclass and one db
            if (db != null && metaClasses.size() == 1) {
                MetaClass metaclass = (MetaClass) metaClasses.get(0);
                DbProject project = dbos[0].getProject();
                if (project != null) {
                    DbEnumeration dbEnum = project.getComponents().elements(DbUDF.metaClass);
                    while (dbEnum.hasMoreElements()) {
                        DbUDF udf = (DbUDF) dbEnum.nextElement();
                        if (metaclass == udf.getUDFMetaClass()) {
                            udfs.add(udf);
                            allMetafields.add(DbUDFValue.fValue);
                        }
                    }
                    dbEnum.close();
                }
            }
            DbMultiTrans.commitTrans(dbos);
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(table.getDesignPanel(), e);
        }
        fields.clear();
        fields.addAll(allMetafields);
    }
}