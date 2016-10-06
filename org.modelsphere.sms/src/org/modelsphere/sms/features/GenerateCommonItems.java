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

package org.modelsphere.sms.features;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.srtypes.OOTypeUseStyle;
import org.modelsphere.sms.oo.db.srtypes.OOVisibility;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.international.LocaleMgr;

final class GenerateCommonItems {
    static final int GENERATE_REPORT = 0;
    static final int GENERATE_COMMON_ITEMS = 1;

    static final int PARTIAL_OPTION = 0;
    static final int TOTAL_OPTION = 1;
    static final int USE_COLUMN_NAME = 2;
    static final int ONE_ITEM_PER_COLUMN_NAME = 4;
    static final int ONE_ITEM_PER_TYPE_PROFILE = 8;

    private static final String SUCCESS_MSG = LocaleMgr.message
            .getString("CommonItemsSuccessfullyGenerated");
    private static final String SET_A_COMMON_ITEM_PATN = LocaleMgr.message
            .getString("SetACommonItemForPattern");
    private static final String CREATION_OF_PATN = LocaleMgr.message.getString("CreationOfPattern");

    private int m_generateReportOrItems;
    private DbObject[] m_semObjs;
    private DbORCommonItemModel m_commonItemModel;
    private StringBuffer m_stringBuffer;
    private String m_message = SUCCESS_MSG;
    private int m_result = JOptionPane.INFORMATION_MESSAGE;
    private int m_counter = 0;
    private int m_options;
    private String m_title;

    // constructor (called by GenerateCommonItemsDialog)
    GenerateCommonItems(int generateReportOrItems, DbObject[] semObjs,
            DbORCommonItemModel commonItemModel, int options, String title) {
        m_generateReportOrItems = generateReportOrItems;
        m_semObjs = semObjs;
        m_commonItemModel = commonItemModel;
        m_stringBuffer = new StringBuffer();
        m_options = options;
        m_title = title;
    }

    // entry points (called by GenerateCommonItemsDialog)
    void execute() throws DbException {
        GenerateCommonItemsWorker worker = new GenerateCommonItemsWorker(m_generateReportOrItems,
                m_semObjs, m_commonItemModel, m_options, m_title, m_stringBuffer);

        if (m_generateReportOrItems == GENERATE_REPORT) {
            // Run outside a controller
            try {
                worker.runJob();
                m_counter = worker.getCounter();
            } catch (Exception ex) {
                m_stringBuffer.append(ex.toString());
                m_stringBuffer.append("\n");
            } // end try
        } else if (m_generateReportOrItems == GENERATE_COMMON_ITEMS) {
            // the controller spawns the worker
            DefaultController controller = new DefaultController(m_title, false, null);
            controller.start(worker);
            m_counter = worker.getCounter();
        } // end if
    } // end execute()

    String getReport() {
        String report = m_stringBuffer.toString();
        return report;
    } // end generateReport()

    String getMessage() {
        return m_message;
    } // end getMessage()

    int getResult() {
        return m_result;
    } // end getResult()

    int getCounter() {
        return m_counter;
    } // end getResult()

    //
    // INNER CLASSES
    //
    private static class CommonInfoStruct {
        // relational fields
        private DbORTypeClassifier m_type;
        private int m_len;
        private int m_dec;
        private boolean m_isNull;
        private DbORCommonItem m_item = null;

        // object fields
        private DbOOAdt m_ooType;
        private String m_typeUse;
        private OOTypeUseStyle m_style;
        private OOVisibility m_visib;
        private boolean m_isStatic;
        private boolean m_isFinal;
        private boolean m_isTransient;
        private boolean m_isVolatile;

        // relational constructor
        public CommonInfoStruct(DbORTypeClassifier type, Integer len, Integer dec, boolean isNull) {
            m_type = type;
            m_len = len == null ? 0 : len.intValue();
            m_dec = dec == null ? 0 : dec.intValue();
            m_isNull = isNull;
        } // end CommonInfoStruct()

        // object constructor
        public CommonInfoStruct(DbOOAdt type, String typeUse, OOTypeUseStyle style,
                OOVisibility visib, boolean isStatic, boolean isFinal, boolean isTransient,
                boolean isVolatile) {
            m_ooType = type;
            m_typeUse = typeUse;
            m_style = style;
            m_visib = visib;
            m_isStatic = isStatic;
            m_isFinal = isFinal;
            m_isTransient = isTransient;
            m_isVolatile = isVolatile;
        }

        public boolean equals(Object object) {
            CommonInfoStruct struct = (CommonInfoStruct) object;
            boolean sameOrType = m_type == null ? (struct.m_type == null) : (m_type
                    .equals(struct.m_type));
            boolean sameOoType = m_ooType == null ? (struct.m_ooType == null) : (m_ooType
                    .equals(struct.m_ooType));
            boolean sameType = sameOrType && sameOoType;
            boolean sameTypeUse = m_typeUse == null ? (struct.m_typeUse == null) : (m_typeUse
                    .equals(struct.m_typeUse));
            boolean sameStyle = m_style == null ? (struct.m_style == null) : (m_style
                    .equals(struct.m_style));
            boolean sameVisib = m_visib == null ? (struct.m_visib == null) : (m_visib
                    .equals(struct.m_visib));

            boolean isEqual = sameType && sameTypeUse && sameStyle && sameVisib
                    && (m_len == struct.m_len) && (m_dec == struct.m_dec)
                    && (m_isNull == struct.m_isNull) && (m_isStatic == struct.m_isStatic)
                    && (m_isFinal == struct.m_isFinal) && (m_isTransient == struct.m_isTransient)
                    && (m_isVolatile == struct.m_isVolatile);
            return isEqual;
        }
    } // end CommonInfoStruct

    //
    // GenerateCommonItemsWorker
    //
    private static class GenerateCommonItemsWorker extends Worker {
        private String m_title;
        private DbObject[] m_semObjs;
        private DbORCommonItemModel m_commonItemModel;
        private int m_options;
        private int m_generateReportOrItems;
        private StringBuffer m_stringBuffer;
        private int m_counter = 0;

        public GenerateCommonItemsWorker(int generateReportOrItems, DbObject[] semObjs,
                DbORCommonItemModel commonItemModel, int options, String title,
                StringBuffer stringBuffer) {
            m_generateReportOrItems = generateReportOrItems;
            m_semObjs = semObjs;
            m_commonItemModel = commonItemModel;
            m_options = options;
            m_title = title;
            m_stringBuffer = stringBuffer;
        }

        // Return this job's title
        protected String getJobTitle() {
            return m_title;
        }

        protected void runJob() throws Exception {
            ArrayList itemList = new ArrayList();
            HashMap itemMap = new HashMap();

            int nb = m_semObjs.length;
            for (int i = 0; i < nb; i++) {
                DbObject semObj = m_semObjs[i];
                if (semObj instanceof DbSMSProject) {
                    DbSMSProject proj = (DbSMSProject) semObj;
                    DbORCommonItemModel commonItemModel = (m_commonItemModel == null) ? getCommonItemModelFrom(proj)
                            : m_commonItemModel;
                    generateCommonItemsForProject(itemList, itemMap, proj, commonItemModel);
                } else if (semObj instanceof DbORDataModel) {
                    DbORDataModel dataModel = (DbORDataModel) semObj;
                    DbORCommonItemModel commonItemModel = (m_commonItemModel == null) ? getCommonItemModelFrom(dataModel)
                            : m_commonItemModel;
                    generateCommonItemsForDataModel(itemList, itemMap, dataModel, commonItemModel);
                } else if (semObj instanceof DbORTable) {
                    DbORTable table = (DbORTable) semObj;
                    DbORDataModel dataModel = (DbORDataModel) table
                            .getCompositeOfType(DbORDataModel.metaClass);
                    DbORCommonItemModel commonItemModel = getCommonItemModelFrom(dataModel);
                    generateCommonItemsForTable(itemList, itemMap, table, commonItemModel);
                } else if (semObj instanceof DbORColumn) {
                    DbORColumn column = (DbORColumn) semObj;
                    DbORDataModel dataModel = (DbORDataModel) column
                            .getCompositeOfType(DbORDataModel.metaClass);
                    DbORCommonItemModel commonItemModel = getCommonItemModelFrom(dataModel);
                    generateCommonItemsForColumn(column, commonItemModel);
                } else if (semObj instanceof DbJVClassModel) {
                    DbJVClassModel classModel = (DbJVClassModel) semObj;
                    DbORCommonItemModel commonItemModel = (m_commonItemModel == null) ? getCommonItemModelFrom(classModel)
                            : m_commonItemModel;
                    generateCommonItemsForClassModel(itemList, itemMap, classModel, commonItemModel);
                } else if (semObj instanceof DbJVClass) {
                    DbJVClass claz = (DbJVClass) semObj;
                    DbJVClassModel classModel = (DbJVClassModel) claz
                            .getCompositeOfType(DbJVClassModel.metaClass);
                    DbORCommonItemModel commonItemModel = (m_commonItemModel == null) ? getCommonItemModelFrom(classModel)
                            : m_commonItemModel;
                    generateCommonItemsForClass(itemList, itemMap, claz, commonItemModel);
                } else if (semObj instanceof DbJVDataMember) {
                    DbJVDataMember field = (DbJVDataMember) semObj;
                    DbJVClassModel classModel = (DbJVClassModel) field
                            .getCompositeOfType(DbJVClassModel.metaClass);
                    DbORCommonItemModel commonItemModel = getCommonItemModelFrom(classModel);
                    generateCommonItemsForField(field, commonItemModel);
                } // end if
            } // end for
        } // end runJob()

        int getCounter() {
            return m_counter;
        } // end getCounter()

        //
        // Private methods
        //
        private DbORCommonItemModel getCommonItemModelFrom(DbSMSAbstractPackage dataModel)
                throws DbException {
            DbORCommonItemModel commonItemModel = null;
            DbObject composite = dataModel.getProject();
            DbRelationN relN = composite.getComponents();
            DbEnumeration dbEnum = relN.elements(DbORCommonItemModel.metaClass);
            while (dbEnum.hasMoreElements()) {
                commonItemModel = (DbORCommonItemModel) dbEnum.nextElement();
                break;
            } // end while
            dbEnum.close();

            if (commonItemModel == null) {
                commonItemModel = new DbORCommonItemModel(composite);
            }

            return commonItemModel;
        } // end getCommonItemModelFrom()

        private DbORCommonItemModel getCommonItemModelFrom(DbSMSProject proj) throws DbException {
            DbORCommonItemModel commonItemModel = null;
            DbRelationN relN = proj.getComponents();
            DbEnumeration dbEnum = relN.elements(DbORCommonItemModel.metaClass);
            while (dbEnum.hasMoreElements()) {
                commonItemModel = (DbORCommonItemModel) dbEnum.nextElement();
                break;
            } // end while
            dbEnum.close();

            if (commonItemModel == null) {
                commonItemModel = new DbORCommonItemModel(proj);
            }

            return commonItemModel;
        } // end getCommonItemModelFrom()

        private void generateCommonItemsForProject(ArrayList itemList, HashMap itemMap,
                DbSMSProject proj, DbORCommonItemModel commonItemModel) throws DbException {
            // for each data model
            DbRelationN relN = proj.getComponents();
            DbEnumeration dbEnum = relN.elements(DbORDataModel.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORDataModel dataModel = (DbORDataModel) dbEnum.nextElement();
                generateCommonItemsForDataModel(itemList, itemMap, dataModel, commonItemModel);
            } // end while
            dbEnum.close();

            // for each class model
            dbEnum = relN.elements(DbJVClassModel.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbJVClassModel classModel = (DbJVClassModel) dbEnum.nextElement();
                generateCommonItemsForClassModel(itemList, itemMap, classModel, commonItemModel);
            } // end while
            dbEnum.close();

        } // end generateCommonItemsForProject

        private void generateCommonItemsForDataModel(ArrayList itemList, HashMap itemMap,
                DbORDataModel dataModel, DbORCommonItemModel commonItemModel) throws DbException {
            DbRelationN relN = dataModel.getComponents();
            DbEnumeration dbEnum = relN.elements(DbORTable.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORTable table = (DbORTable) dbEnum.nextElement();
                generateCommonItemsForTable(itemList, itemMap, table, commonItemModel);
            } // end while
            dbEnum.close();

            dbEnum = relN.elements(DbORDataModel.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORDataModel submodel = (DbORDataModel) dbEnum.nextElement();
                generateCommonItemsForDataModel(itemList, itemMap, submodel, commonItemModel);
            } // end while
            dbEnum.close();
        } // end generateCommonItemsForDataModel()

        private void generateCommonItemsForClassModel(ArrayList itemList, HashMap itemMap,
                DbJVClassModel classModel, DbORCommonItemModel commonItemModel) throws DbException {
            DbRelationN relN = classModel.getComponents();
            DbEnumeration dbEnum = relN.elements(DbJVClass.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbJVClass claz = (DbJVClass) dbEnum.nextElement();
                generateCommonItemsForClass(itemList, itemMap, claz, commonItemModel);
            } // end while
            dbEnum.close();

            dbEnum = relN.elements(DbJVPackage.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbJVPackage pack = (DbJVPackage) dbEnum.nextElement();
                generateCommonItemsForPackage(itemList, itemMap, pack, commonItemModel);
            } // end while
            dbEnum.close();
        } // end generateCommonItemsForClassModel()

        private void generateCommonItemsForPackage(ArrayList itemList, HashMap itemMap,
                DbJVPackage pack, DbORCommonItemModel commonItemModel) throws DbException {
            DbRelationN relN = pack.getComponents();
            DbEnumeration dbEnum = relN.elements(DbJVClass.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbJVClass claz = (DbJVClass) dbEnum.nextElement();
                generateCommonItemsForClass(itemList, itemMap, claz, commonItemModel);
            } // end while
            dbEnum.close();

            dbEnum = relN.elements(DbJVPackage.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbJVPackage subpack = (DbJVPackage) dbEnum.nextElement();
                generateCommonItemsForPackage(itemList, itemMap, subpack, commonItemModel);
            } // end while
            dbEnum.close();
        } // end generateCommonItemsForClassModel()

        private void generateCommonItemsForTable(ArrayList itemList, HashMap itemMap,
                DbORTable table, DbORCommonItemModel commonItemModel) throws DbException {
            DbRelationN relN = table.getComponents();
            DbEnumeration dbEnum = relN.elements(DbORColumn.metaClass);
            boolean createOneItemPerColumn = ((m_options & ONE_ITEM_PER_COLUMN_NAME) == 0)
                    && ((m_options & ONE_ITEM_PER_TYPE_PROFILE) == 0);
            while (dbEnum.hasMoreElements()) {
                DbORColumn column = (DbORColumn) dbEnum.nextElement();
                DbORCommonItem item = column.getCommonItem();
                // Generate if total generation, or if partiel generation for
                // itemless columns
                if (((m_options & TOTAL_OPTION) != 0) || (item == null)) {
                    CommonInfoStruct newStruct = new CommonInfoStruct(column.getType(), column
                            .getLength(), column.getNbDecimal(), column.isNull());
                    if (createOneItemPerColumn) {
                        item = createNewCommonItem(commonItemModel, column);
                        newStruct.m_item = item;
                        setCommonItemToColumn(column, newStruct);
                    } else if ((m_options & ONE_ITEM_PER_TYPE_PROFILE) != 0) {
                        if (itemList.contains(newStruct)) {
                            CommonInfoStruct struct = (CommonInfoStruct) itemList.get(itemList
                                    .indexOf(newStruct));
                            setCommonItemToColumn(column, struct);
                        } else {
                            item = createNewCommonItem(commonItemModel, column);
                            newStruct.m_item = item;
                            itemList.add(newStruct);
                        } // end if
                    } else if ((m_options & ONE_ITEM_PER_COLUMN_NAME) != 0) {
                        String colName = column.getName();
                        colName = (colName == null) ? "" : colName;
                        if (itemMap.containsKey(colName)) {
                            item = (DbORCommonItem) itemMap.get(colName);
                            assignItemToColumn(column, item);
                        } else {
                            item = createNewCommonItem(commonItemModel, column);
                            itemMap.put(colName, item);
                        } // end if
                    } // end if
                } // end if
            } // end while
            dbEnum.close();
        } // end generateCommonItemsForDataModel()

        private void generateCommonItemsForClass(ArrayList itemList, HashMap itemMap,
                DbJVClass claz, DbORCommonItemModel commonItemModel) throws DbException {
            DbRelationN relN = claz.getComponents();
            DbEnumeration dbEnum = relN.elements(DbJVDataMember.metaClass);
            boolean createOneItemPerField = ((m_options & ONE_ITEM_PER_COLUMN_NAME) == 0)
                    && ((m_options & ONE_ITEM_PER_TYPE_PROFILE) == 0);
            while (dbEnum.hasMoreElements()) {
                DbJVDataMember field = (DbJVDataMember) dbEnum.nextElement();
                DbORCommonItem item = field.getCommonItem();
                // Generate if total generation, or if partiel generation for
                // itemless columns
                if (((m_options & TOTAL_OPTION) != 0) || (item == null)) {
                    DbOOAdt type = field.getType();
                    String typeUse = field.getTypeUse();
                    OOTypeUseStyle style = field.getTypeUseStyle();
                    OOVisibility visib = (OOVisibility) field.getVisibility();
                    boolean isStatic = field.isStatic();
                    boolean isFinal = field.isFinal();
                    boolean isTransient = field.isTransient();
                    boolean isVolatile = field.isVolatile();
                    CommonInfoStruct newStruct = new CommonInfoStruct(type, typeUse, style, visib,
                            isStatic, isFinal, isTransient, isVolatile);
                    if (createOneItemPerField) {
                        item = createNewCommonItem(commonItemModel, field);
                        newStruct.m_item = item;
                        setCommonItemToField(field, newStruct);
                    } else if ((m_options & ONE_ITEM_PER_TYPE_PROFILE) != 0) {
                        if (itemList.contains(newStruct)) {
                            CommonInfoStruct struct = (CommonInfoStruct) itemList.get(itemList
                                    .indexOf(newStruct));
                            setCommonItemToField(field, struct);
                        } else {
                            item = createNewCommonItem(commonItemModel, field);
                            newStruct.m_item = item;
                            itemList.add(newStruct);
                        } // end if
                    } else if ((m_options & ONE_ITEM_PER_COLUMN_NAME) != 0) {
                        String colName = field.getName();
                        colName = (colName == null) ? "" : colName;
                        if (itemMap.containsKey(colName)) {
                            item = (DbORCommonItem) itemMap.get(colName);
                            assignItemToField(field, item);
                        } else {
                            item = createNewCommonItem(commonItemModel, field);
                            itemMap.put(colName, item);
                        } // end if
                    } // end if
                } // end if
            } // end while
            dbEnum.close();
        } // end generateCommonItemsForClass()

        private void generateCommonItemsForColumn(DbORColumn column,
                DbORCommonItemModel commonItemModel) throws DbException {

            DbORCommonItem item = column.getCommonItem();

            // Generate if total generation, or if partiel generation for
            // itemless columns
            if (((m_options & TOTAL_OPTION) != 0) || (item == null)) {
                item = createNewCommonItem(commonItemModel, column);
            }
        } // end generateCommonItemsForColumn()

        private void generateCommonItemsForField(DbJVDataMember field,
                DbORCommonItemModel commonItemModel) throws DbException {
            DbORCommonItem item = field.getCommonItem();
            // Generate if total generation, or if partiel generation for
            // itemless fields
            if (((m_options & TOTAL_OPTION) != 0) || (item == null)) {
                item = createNewCommonItem(commonItemModel, field);
            }
        } // end generateCommonItemsForField()

        private DbORCommonItem createNewCommonItem(DbORCommonItemModel commonItemModel,
                DbORColumn column) throws DbException {
            DbORCommonItem item = null;
            DbORTypeClassifier type = column.getType();
            Integer len = column.getLength();
            Integer dec = column.getNbDecimal();
            boolean b = column.isNull();
            DbSMSStereotype stereotype = column.getUmlStereotype();
            String desc = column.getDescription();

            String message = MessageFormat.format(CREATION_OF_PATN, new Object[] {
                    commonItemModel.getName(), DbORCommonItem.metaClass.getGUIName() });

            if (m_generateReportOrItems == GENERATE_COMMON_ITEMS) {
                item = new DbORCommonItem(commonItemModel);
                item.setType(type);
                item.setLength(len);
                item.setNbDecimal(dec);
                item.setNull(Boolean.valueOf(b));
                item.setUmlStereotype(stereotype);
                item.setDescription(desc);
                column.setCommonItem(item);
                getController().println(message);

                if ((m_options & USE_COLUMN_NAME) != 0) {
                    item.setName(column.getName());
                    item.setPhysicalName(column.getPhysicalName());
                    item.setAlias(column.getAlias());
                }
            } else if (m_generateReportOrItems == GENERATE_REPORT) {
                m_stringBuffer.append(message);
                m_counter++;
            } // end if

            return item;
        } // end createNewCommonItem()

        private DbORCommonItem createNewCommonItem(DbORCommonItemModel commonItemModel,
                DbJVDataMember field) throws DbException {
            DbORCommonItem item = null;
            DbOOAdt type = field.getType();
            String typeUse = field.getTypeUse();
            OOTypeUseStyle style = field.getTypeUseStyle();
            OOVisibility visib = (OOVisibility) field.getVisibility();
            boolean isStatic = field.isStatic();
            boolean isFinal = field.isFinal();
            boolean isTransient = field.isTransient();
            boolean isVolatile = field.isVolatile();
            DbSMSStereotype stereotype = field.getUmlStereotype();
            String desc = field.getDescription();

            String message = MessageFormat.format(CREATION_OF_PATN, new Object[] {
                    commonItemModel.getName(), DbORCommonItem.metaClass.getGUIName() });

            if (m_generateReportOrItems == GENERATE_COMMON_ITEMS) {
                item = new DbORCommonItem(commonItemModel);
                item.setOoType(type);
                item.setTypeUse(typeUse);
                item.setTypeUseStyle(style);
                item.setVisibility(JVVisibility.getInstance(visib.getValue()));
                item.setStatic(Boolean.valueOf(isStatic));
                item.setFinal(new Boolean(isFinal));
                item.setTransient(new Boolean(isTransient));
                item.setVolatile(new Boolean(isVolatile));
                item.setUmlStereotype(stereotype);
                item.setDescription(desc);

                field.setCommonItem(item);
                getController().println(message);

                if ((m_options & USE_COLUMN_NAME) != 0) {
                    item.setName(field.getName());
                    item.setPhysicalName(field.getPhysicalName());
                    item.setAlias(field.getAlias());
                }
            } else if (m_generateReportOrItems == GENERATE_REPORT) {
                m_stringBuffer.append(message);
                m_counter++;
            } // end if

            return item;
        } // end createNewCommonItem()

        private void setCommonItemToColumn(DbORColumn column, CommonInfoStruct struct)
                throws DbException {
            DbORTable table = (DbORTable) column.getCompositeOfType(DbORTable.metaClass);
            String message = MessageFormat.format(SET_A_COMMON_ITEM_PATN, new Object[] {
                    table.getName(), column.getName() });

            if (m_generateReportOrItems == GENERATE_COMMON_ITEMS) {
                DbORCommonItem item = struct.m_item;
                assignItemToColumn(column, item);
                column.setCommonItem(item);
                getController().println(message);
            } else if (m_generateReportOrItems == GENERATE_REPORT) {
                m_stringBuffer.append(message);
            } // end if
        } // end setCommonItemToColumn()

        private void setCommonItemToField(DbJVDataMember field, CommonInfoStruct struct)
                throws DbException {
            DbJVClass claz = (DbJVClass) field.getCompositeOfType(DbJVClass.metaClass);
            String message = MessageFormat.format(SET_A_COMMON_ITEM_PATN, new Object[] {
                    claz.getName(), field.getName() });

            if (m_generateReportOrItems == GENERATE_COMMON_ITEMS) {
                DbORCommonItem item = struct.m_item;
                assignItemToField(field, item);
                getController().println(message);
            } else if (m_generateReportOrItems == GENERATE_REPORT) {
                m_stringBuffer.append(message);
            } // end if
        } // end setCommonItemToField()

        private void assignItemToColumn(DbORColumn column, DbORCommonItem item) throws DbException {
            if (item != null) {
                DbSMSStereotype stereotype = column.getUmlStereotype();
                String desc = column.getDescription();
                if ((stereotype == null) || (!stereotype.equals(item.getUmlStereotype()))) {
                    item.setUmlStereotype(null);
                }
                if ((desc == null) || (!desc.equals(item.getDescription()))) {
                    item.setDescription(null);
                }
            } // end if
            column.setCommonItem(item);
        } // end assignItemToColumn()

        private void assignItemToField(DbJVDataMember field, DbORCommonItem item)
                throws DbException {
            if (item != null) {
                DbSMSStereotype stereotype = field.getUmlStereotype();
                String desc = field.getDescription();
                if ((stereotype == null) || (!stereotype.equals(item.getUmlStereotype()))) {
                    item.setUmlStereotype(null);
                }
                if ((desc == null) || (!desc.equals(item.getDescription()))) {
                    item.setDescription(null);
                }
                field.setCommonItem(item);
            } // end if
        } // end assignItemToField()
    } // end GenerateCommonItemsWorker

    //
    // UNIT TEST
    //
    public static void main(String args[]) {

    } // end main()
} // end GenerateCommonItems()
