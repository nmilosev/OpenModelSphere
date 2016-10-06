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

package org.modelsphere.sms.or.features.extract;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORIndexKey;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORProcedure;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTrigger;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.db.DbORUserNode;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.db.srtypes.ORTriggerEvent;
import org.modelsphere.sms.or.db.srtypes.ORValidationRule;
import org.modelsphere.sms.or.oracle.db.DbORADatabase;
import org.modelsphere.sms.or.oracle.db.DbORAIndex;
import org.modelsphere.sms.or.oracle.db.DbORASequence;
import org.modelsphere.sms.or.oracle.db.DbORATable;
import org.modelsphere.sms.or.oracle.db.DbORATablespace;

/**
 * Writes an Oracle extract file for RDM
 * 
 * This class writes an extract file for Oracle, it is used to transfer SMS models into RDM using
 * its reverse engineering feature. Each Oracle extract file contain a comment header and several
 * occurence clauses. Each occurrence clause starts with the line header 'sr_occurrence_type
 * &lt;concept&gt;', where concept is one of {user, tablespace, table, view, column, pk_uk_cst,
 * fk_cst, check_cst, cst_col, index, index_col, trigger, procedure, sequence}. Each occurence
 * clause also contain several (key, value) pairs.
 * 
 * @author unascribed
 * @version 1.0
 */
class OracleExtractFileWriter extends ExtractFileWriter {

    protected OracleExtractFileWriter(Controller controller) {
        super(controller);
    }

    //
    // overrides ExtractFileWriter
    //

    // DBMSID: DBMS:
    // 71 Oracle 9i
    // 70 Oracle 8i
    // 52 Oracle 8
    // 23 Oracle 7.x
    protected void writeHeader(PrintWriter writer, DbORDataModel dataModel) throws DbException {
        DbSMSTargetSystem ts = dataModel.getTargetSystem();
        int smsID = ts.getID().intValue();
        RdmDbmsIdMapping mapping = RdmDbmsIdMapping.getSingleton();
        int rdmID = mapping.getRDMCounterPart(smsID);

        // Get DBMS name for RDM extract files
        String dbms = "Oracle";
        switch (rdmID) {
        case RdmDbmsIdMapping.RDM_ORACLE6:
            dbms = "Oracle 6";
            break;
        case RdmDbmsIdMapping.RDM_ORACLE7:
            dbms = "Oracle 7.x";
            break;
        case RdmDbmsIdMapping.RDM_ORACLE8:
            dbms = "Oracle 8";
            break;
        case RdmDbmsIdMapping.RDM_ORACLE8i:
            dbms = "Oracle 8i";
            break;
        case RdmDbmsIdMapping.RDM_ORACLE9i:
            dbms = "Oracle 9i";
            break;
        } // end switch

        writer.println("--***** DO NOT MODIFY HEADER *****"); // NOT LOCALIZABLE
        writer.println("--Interface: SMS2RDM Interface"); // NOT LOCALIZABLE
        writer.println("--Version:   " + RDM_VERSION); // NOT LOCALIZABLE
        writer.println("--Database:  <unspecified>"); // NOT LOCALIZABLE
        writer.println("--DBMSID:    " + rdmID); // NOT LOCALIZABLE
        writer.println("--DBMS:      " + dbms); // NOT LOCALIZABLE
        writer.println("--******** END OF HEADER *********"); // NOT LOCALIZABLE
    } // end writeHeader()

    protected void writeDataModel(DbORDataModel dataModel, File directory, ArrayList fileList)
            throws IOException, DbException {
        // create file and write file header
        File file = createFile(dataModel, directory, fileList);
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writeHeader(writer, dataModel);

        // get user node
        DbProject proj = dataModel.getProject();
        DbRelationN relN = proj.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORUserNode.metaClass);
        boolean userNodeEmpty = true;
        while (dbEnum.hasMoreElements()) {
            DbORUserNode userNode = (DbORUserNode) dbEnum.nextElement();
            userNodeEmpty &= writeUserNode(userNode, writer);
        } // end while
        dbEnum.close();

        // RDM for Oracle requires at least one user
        if (userNodeEmpty) {
            writer.println("sr_occurrence_type user");
            writer.println("name user");
        }

        // get deployed database, if any
        DbORADatabase database = (DbORADatabase) dataModel.getDeploymentDatabase();
        if (database != null) {
            // For each tablespace
            relN = database.getComponents();
            dbEnum = relN.elements(DbORATablespace.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORATablespace tablespace = (DbORATablespace) dbEnum.nextElement();
                writeTablespace(tablespace, writer);
            } // end while
            dbEnum.close();
        } // end if

        // For each table, create table, columns and PKs
        relN = dataModel.getComponents();
        dbEnum = relN.elements(DbORTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORTable table = (DbORTable) dbEnum.nextElement();
            writeTable(table, writer);
        } // end while
        dbEnum.close();

        // For each view
        relN = dataModel.getComponents();
        dbEnum = relN.elements(DbORView.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORView view = (DbORView) dbEnum.nextElement();
            writeView(view, writer);
        } // end while
        dbEnum.close();

        // For each table, create FKs
        relN = dataModel.getComponents();
        dbEnum = relN.elements(DbORTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORTable table = (DbORTable) dbEnum.nextElement();
            DbORUser user = table.getUser();
            String owner = getPhysicalOrLogicalName(user, "user");
            writeForeignKeysOfTable(owner, table, writer);
        } // end while
        dbEnum.close();

        // For each operation
        if (database != null) {
            DbOROperationLibrary library = database.getOperationLibrary();
            relN = library.getComponents();
            dbEnum = relN.elements(DbORProcedure.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORProcedure proc = (DbORProcedure) dbEnum.nextElement();
                writeProcedure(proc, writer);
            } // end while
            dbEnum.close();
        } // end if

        // For each sequence
        relN = dataModel.getComponents();
        dbEnum = relN.elements(DbORASequence.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORASequence sequence = (DbORASequence) dbEnum.nextElement();
            writeSequence(sequence, writer);
        } // end while
        dbEnum.close();

        String msg = MessageFormat.format(MSG_PATTERN, new Object[] { file.getName(),
                file.getParent() });
        m_controller.println(msg);
        m_controller.println();
        writer.close();
    } // end writeDataModel()

    /**
     * <pre>
     * sr_occurrence_type user    //header
     *   name STEPHANE              //identifier
     *   &lt;/br&gt;
     *   Several users can be specified
     * </pre>
     */
    private static final String USER_OCCURRENCE = "";

    private boolean writeUserNode(DbORUserNode userNode, PrintWriter writer) throws DbException {
        DbRelationN relN = userNode.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORUser.metaClass);
        boolean userNodeEmpty = true;
        while (dbEnum.hasMoreElements()) {
            DbORUser user = (DbORUser) dbEnum.nextElement();
            String username = getPhysicalOrLogicalName(user);
            writer.println("sr_occurrence_type user");
            writer.println("name " + username);
            userNodeEmpty = false;
        } // end while
        dbEnum.close();
        return userNodeEmpty;
    } // end writeUserNode()

    /**
     * <pre>
     * sr_occurrence_type tablespace   //header
     *   name SYSTEM                     //identifier
     *   init_extent 10485760            //integer, in bytes
     *   next_extent 1048576             //integer, in bytes
     *   min_extents 1                   //integer
     *   max_extents 110                 //integer
     *   pct_increase 50                 //integer
     * </pre>
     */
    private static final String TABLESPACE_OCCURRENCE = "";

    private void writeTablespace(DbORATablespace tablespace, PrintWriter writer) throws DbException {
        String tablespaceName = getPhysicalOrLogicalName(tablespace);
        m_controller.println(DbORATablespace.metaClass.getGUIName() + " " + tablespaceName + "..");
        writer.println("sr_occurrence_type tablespace");
        writer.println("name " + tablespaceName);

        int initExtent = computeActualValue(tablespace.getDefObjInitialExtent(), tablespace
                .getDefObjInitialExtentSizeUnit());
        int nextExtent = computeActualValue(tablespace.getDefObjNextExtent(), tablespace
                .getDefObjNextExtentSizeUnit());
        int minExtents = computeActualValue(tablespace.getDefObjMinExtents());
        int maxExtents = computeActualValue(tablespace.getDefObjMaxExtents());
        int pctIncrease = computeActualValue(tablespace.getDefObjPctIncrease());

        writer.println("init_extent " + initExtent);
        writer.println("next_extent " + nextExtent);
        writer.println("min_extents " + minExtents);
        writer.println("max_extents " + maxExtents);
        writer.println("pct_increase " + pctIncrease);
    } // end writeTablespace()

    /**
     * <pre>
     * sr_occurrence_type table   //header
     *   owner STEPHANE             //user ID
     *   name CLIENT                //identifier
     *   init_extent 10240          //integer
     *   next_extent 10240          //integer
     *   min_extents 1              //integer
     *   max_extents 121            //integer
     *   pct_free 10                //integer
     *   pct_used 40                //integer
     *   pct_increase 50            //integer
     *   ini_trans 1                //integer
     *   max_trans 255              //integer
     *   tbsp_name TB_AQ            //tablespace ID
     *   comment free text          //optional table description
     *   _sr_e_o_t_
     * </pre>
     */
    private static final String TABLE_OCCURRENCE = "";

    protected void writeTable(DbORTable table, PrintWriter writer) throws DbException {
        String tableName = getPhysicalOrLogicalName(table);
        m_controller.println(DbORTable.metaClass.getGUIName() + " " + tableName + "..");
        writer.println("sr_occurrence_type table");

        DbORUser user = table.getUser();
        String owner = getPhysicalOrLogicalName(user, "user");
        writer.println("owner " + owner);
        writer.println("name " + tableName);

        // specific values
        DbORATable tab = (DbORATable) table;
        int initExtent = computeActualValue(tab.getInitialExtent(), tab.getInitialExtentSizeUnit());
        int nextExtent = computeActualValue(tab.getNextExtent(), tab.getNextExtentSizeUnit());
        int minExtents = computeActualValue(tab.getMinExtents());
        int maxExtents = computeActualValue(tab.getMaxExtents());
        int pctFree = computeActualValue(tab.getPctfree());
        int pctUsed = computeActualValue(tab.getPctused());
        int pctIncrease = computeActualValue(tab.getPctIncrease());
        int iniTrans = computeActualValue(tab.getInitrans());
        int maxTrans = computeActualValue(tab.getMaxtrans());

        writer.println("init_extent " + initExtent);
        writer.println("next_extent " + nextExtent);
        writer.println("min_extents " + minExtents);
        writer.println("max_extents " + maxExtents);
        writer.println("pct_free " + pctFree);
        writer.println("pct_used " + pctUsed);
        writer.println("pct_increase " + pctIncrease);
        writer.println("ini_trans " + iniTrans);
        writer.println("max_trans " + maxTrans);

        DbRelationN relN = tab.getTablespaces();
        DbEnumeration dbEnum = relN.elements(DbORATablespace.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORATablespace tablespace = (DbORATablespace) dbEnum.nextElement();
            String tablespaceName = getPhysicalOrLogicalName(tablespace);
            writer.println("tbsp_name " + tablespaceName);
        } // end while
        dbEnum.close();

        // degree
        // instances

        String comments = tab.getDescription();
        if (comments != null) {
            writer.println("comments " + comments);
            writer.println("_sr_e_o_t_");
        }

        // For each column
        relN = table.getComponents();
        dbEnum = relN.elements(DbORColumn.metaClass);
        int colID = 0;
        while (dbEnum.hasMoreElements()) {
            colID += 10;
            DbORColumn column = (DbORColumn) dbEnum.nextElement();
            writeColumn(column, writer, tableName, colID);
        } // end while
        dbEnum.close();

        // For each PK/UK
        relN = table.getComponents();
        dbEnum = relN.elements(DbORPrimaryUnique.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORPrimaryUnique puKey = (DbORPrimaryUnique) dbEnum.nextElement();
            writePuKey(puKey, writer, owner, tableName);
        } // end while
        dbEnum.close();

        // For each check constraint
        relN = table.getComponents();
        dbEnum = relN.elements(DbORCheck.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORCheck check = (DbORCheck) dbEnum.nextElement();
            writeCheckConstraint(check, writer, owner, tableName);
        } // end while
        dbEnum.close();

        // For each index
        relN = table.getComponents();
        dbEnum = relN.elements(DbORIndex.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORIndex index = (DbORIndex) dbEnum.nextElement();
            writeIndex(index, writer, owner, tableName);
        } // end while
        dbEnum.close();

        // For each trigger
        relN = table.getComponents();
        dbEnum = relN.elements(DbORTrigger.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORTrigger trigger = (DbORTrigger) dbEnum.nextElement();
            writeTrigger(trigger, writer, owner, tableName);
        } // end while
        dbEnum.close();
    } // end writeTable()

    /**
     * <pre>
     * sr_occurrence_type view        //header
     *   owner STEPHANE                 //user ID
     *   view_name COMPTE_CLIENT        //identifier
     *   comments                       //free text description
     *   _sr_e_o_t_
     *   view_text SELECT client.numero, client.nom, FROM stephane.client
     *   _sr_e_o_t_                     //view body
     * </pre>
     */
    private static final String VIEW_OCCURRENCE = "";

    private void writeView(DbORView view, PrintWriter writer) throws DbException {
        String viewName = getPhysicalOrLogicalName(view);
        m_controller.println(DbORView.metaClass.getGUIName() + " " + viewName + "..");
        writer.println("sr_occurrence_type view");

        DbORUser user = view.getUser();
        String owner = getPhysicalOrLogicalName(user, "user");
        writer.println("owner " + owner);
        writer.println("view_name " + viewName);

        String desc = view.getDescription();
        writer.println("comments");
        if (desc != null) {
            writer.println(desc);
        }
        writer.println("_sr_e_o_t_");

        // view_type_owner
        // view_type
        // oid_text

        String rule = view.getSelectionRule();
        writer.println("view_text");
        if (rule != null) {
            writer.println(rule);
        }
        writer.println("_sr_e_o_t_");
    } // end writeView()

    /**
     * <pre>
     * sr_occurrence_type column      //header
     *   owner STEPHANE                 //user ID
     *   table_name CLIENT              //table or view ID
     *   name ADDRESSE1                 //identifier
     *   column_id 40                   //position of column into table
     *   data_type VARCHAR2             //name of the data type
     *   col_len 60                     //length or size (if type == CHAR, etc.)
     *   data_prec 10                   //precision, or nbDecimals (if type == FLOAT, etc.)
     *   data_scale 15                  //scale (if type == FLOAT, etc.)
     *   col_null Y                     //'Y' for NULL, 'N' for NOT NULL
     *   comments                       //optional free text description
     *   _sr_e_o_t_
     *   data_default                   //optional default value of the column
     *   _sr_e_o_t_
     * </pre>
     */
    private static final String COLUMN_OCCURRENCE = "";

    protected void writeColumn(DbORColumn column, PrintWriter writer, String tableName, int colID)
            throws DbException {
        writer.println("sr_occurrence_type column");
        DbORTable table = (DbORTable) column.getCompositeOfType(DbORTable.metaClass);
        DbORUser user = table.getUser();
        String owner = getPhysicalOrLogicalName(user, "user");
        writer.println("owner " + owner);
        writer.println("table_name " + tableName);

        String colName = getPhysicalOrLogicalName(column);
        writer.println("name " + colName);
        writer.println("column_id " + colID);

        // get column type
        DbORTypeClassifier type = column.getType();
        int recursivityLevel = 0;
        String typename = getTypeName(type, recursivityLevel);
        writer.println("data_type " + typename);

        // get column length
        Integer len = column.getLength();
        int i = (len == null) ? 0 : len.intValue();
        writer.println("col_len " + i);

        Integer nbDecs = column.getNbDecimal();
        i = (nbDecs == null) ? 0 : nbDecs.intValue();
        writer.println("data_prec " + i);

        // data_scale
        // char_used
        // char_length

        // isNull
        boolean isNull = column.isNull();
        char yn = isNull ? 'Y' : 'N';
        writer.println("col_null " + yn);

        // comments
        String comments = column.getDescription();
        writer.println("comments");
        if (comments != null) {
            writer.println(comments);
        }
        writer.println("_sr_e_o_t_");

        // comments
        String value = column.getInitialValue();
        writer.println("data_default");
        if (value != null) {
            writer.println(value);
        }
        writer.println("_sr_e_o_t_");
    } // end writeColumn()

    /**
     * <pre>
     * sr_occurrence_type pk_uk_cst   //header
     *   owner STEPHANE                 //user ID
     *   table_name CLIENT              //table ID
     *   cst_name PK_CLIENT             //identifier
     *   type P                         //'P'rimary or 'U'nique
     * </pre>
     */
    private static final String PUKEY_OCCURRENCE = "";

    protected void writePuKey(DbORPrimaryUnique key, PrintWriter writer, String owner,
            String tableName) throws DbException {
        writer.println("sr_occurrence_type pk_uk_cst");
        writer.println("owner " + owner);

        writer.println("table_name " + tableName);
        String name = getPhysicalOrLogicalName(key);
        writer.println("cst_name " + name);

        char type = key.isPrimary() ? 'P' : 'U';
        writer.println("type " + type);

        // deferrable
        // deferred
    } // end writePuKey()

    /**
     * <pre>
     * sr_occurrence_type check_cst      //header
     *   owner STEPHANE                    //user ID
     *   table_name CLIENT                 //table ID
     *   cst_name FORMAT_CODE_POST         //identifier
     *   col_name CODE_POSTAL              //column ID
     *   search CODE_POSTAL LIKE 'A9A9A9'  //search condition
     *   _sr_e_o_t_
     * </pre>
     */
    private static final String CHECK_OCCURRENCE = "";

    private void writeCheckConstraint(DbORCheck check, PrintWriter writer, String owner,
            String tableName) throws DbException {
        writer.println("sr_occurrence_type check_cst");
        writer.println("owner " + owner);

        writer.println("table_name " + tableName);
        String name = getPhysicalOrLogicalName(check);
        writer.println("cst_name " + name);

        // get column
        DbORColumn column = check.getColumn();
        String colName = getPhysicalOrLogicalName(column);
        writer.println("col_name " + colName);

        // deferrable
        // deferred

        // get condition
        String condition = check.getCondition();
        if (condition != null) {
            writer.println("search");
            writer.println(condition);
            writer.println("_sr_e_o_t_");
        } // end if
    } // end writeCheckConstraint()

    /**
     * <pre>
     * sr_occurrence_type index    //header
     *   owner STEPHANE              //user ID
     *   index_name AK_CLIENT        //identifier
     *   table_owner STEPHANE        //user ID of the table
     *   table_name CLIENT           //table ID
     *   idx_unique UNIQUE           //optinal field to indicate uniqueness
     *   init_extent 10240           //integer
     *   next_extent 10240           //integer
     *   min_extents 1               //integer
     *   max_extents 121             //integer
     *   pct_free 10                 //integer
     *   pct_increase 50             //integer
     *   ini_trans 2                 //integer
     *   max_trans 255               //integer
     *   tbsp_name TB_AQ             //tablespace ID
     * </pre>
     */
    private static final String INDEX_OCCURRENCE = "";

    protected void writeIndex(DbORIndex index, PrintWriter writer, String owner, String tableName)
            throws DbException {
        writer.println("sr_occurrence_type index");
        // owner
        String name = getPhysicalOrLogicalName(index);
        writer.println("index_name " + name);
        writer.println("table_owner " + owner);
        writer.println("table_name " + tableName);

        // is unique
        boolean isUnique = index.isUnique();
        if (isUnique) {
            writer.println("idx_unique UNIQUE");
        }

        // idx_bitmap

        // physical attributes
        DbORAIndex oraIndex = (DbORAIndex) index;
        int initExtent = computeActualValue(oraIndex.getInitialExtent(), oraIndex
                .getInitialExtentSizeUnit());
        int nextExtent = computeActualValue(oraIndex.getNextExtent(), oraIndex
                .getNextExtentSizeUnit());
        int minExtents = computeActualValue(oraIndex.getMinExtent());
        int maxExtents = computeActualValue(oraIndex.getMaxExtent());
        int pctFree = computeActualValue(oraIndex.getPctfree());
        int pctIncrease = computeActualValue(oraIndex.getPctIncrease());
        int iniTrans = computeActualValue(oraIndex.getInitrans());
        int maxTrans = computeActualValue(oraIndex.getMaxtrans());

        writer.println("init_extent " + initExtent);
        writer.println("next_extent " + nextExtent);
        writer.println("min_extents " + minExtents);
        writer.println("max_extents " + maxExtents);
        writer.println("pct_free " + pctFree);
        writer.println("pct_increase " + pctIncrease);
        writer.println("ini_trans " + iniTrans);
        writer.println("max_trans " + maxTrans);

        // write table space
        DbORATablespace tablespace = oraIndex.getTablespace();
        if (tablespace != null) {
            String tablespaceName = getPhysicalOrLogicalName(tablespace);
            writer.println("tbsp_name " + tablespaceName);
        } // end if

        // locality

        // write index keys
        DbRelationN relN = index.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORIndexKey.metaClass);
        int pos = 0;
        while (dbEnum.hasMoreElements()) {
            pos++;
            DbORIndexKey key = (DbORIndexKey) dbEnum.nextElement();
            writeIndexKey(key, writer, owner, name, pos);
        } // end while
        dbEnum.close();
    } // end writeIndex()

    /**
     * <pre>
     * sr_occurrence_type index_col    //header
     *   index_owner STEPHANE            //user ID
     *   index_name AK_CLIENT            //index ID
     *   col_name CODE_POSTAL            //column ID
     *   pos 1                           //position of column within the key
     * </pre>
     */
    private static final String INDEX_KEY_OCCURRENCE = "";

    private void writeIndexKey(DbORIndexKey key, PrintWriter writer, String owner,
            String indexName, int pos) throws DbException {
        writer.println("sr_occurrence_type index_col");
        writer.println("index_owner " + owner);
        writer.println("index_name " + indexName);

        // write column ans pos
        DbORColumn col = key.getIndexedElement();
        String colName = getPhysicalOrLogicalName(col);
        writer.println("col_name " + colName);
        writer.println("pos " + pos);
    } // end writeIndexKey()

    /**
     * <pre>
     * sr_occurrence_type fk_cst     //header
     *   owner STEPHANE                //user ID
     *   table_name COMMANDE           //table ID
     *   cst_name FK_COMMANDE          //identifier
     *   ref_owner STEPHANE            //user ID of the opposite table
     *   ref_table_name CLIENT         //opposite table ID
     *   del_rule NO ACTION            //always NO ACTION
     * </pre>
     */
    private static final String FOREIGN_KEY_OCCURRENCE = "";

    protected void writeForeign(DbORForeign fKey, PrintWriter writer, String owner, String tableName)
            throws DbException {
        writer.println("sr_occurrence_type fk_cst");
        writer.println("owner " + owner);
        writer.println("table_name " + tableName);

        String name = getPhysicalOrLogicalName(fKey);
        writer.println("cst_name " + name);

        // get opposite table
        DbORAssociationEnd end = fKey.getAssociationEnd();
        DbORAssociationEnd oppEnd = end.getOppositeEnd();
        DbORAbsTable oppTable = oppEnd.getClassifier();
        DbORUser oppUser = oppTable.getUser();
        String oppTableName = getPhysicalOrLogicalName(oppTable);
        String oppUserName = getPhysicalOrLogicalName(oppUser, "user");
        writer.println("ref_owner " + oppUserName);
        writer.println("ref_table_name " + oppTableName);

        // get delete rule
        ORValidationRule rule = end.getDeleteRule();
        int value = rule.getValue();
        switch (value) {
        case ORValidationRule.NONE:
            writer.println("del_rule NO ACTION");
            break;
        } // end switch

        // deferrable
        // deferred
    } // end writeForeign()

    /**
     * <pre>
     * sr_occurrence_type trigger              //header
     *   owner STEPHANE                          //user ID
     *   table_name ACCOUNT                      //table ID
     *   trig_name I_COMPTE                      //identifier
     *   event INSERT                            //INSERT, DELETE or UPDATE
     *   description                             //optional description
     *   _sr_e_o_t_
     *   when_clause                             //optional WHEN condition
     *   _sr_e_o_t_
     *   trig_body                               //trigger's statements
     *   DECLARE nb_valid_rows   INTEGER;
     *   _sr_e_o_t_
     * </pre>
     */
    private static final String TRIGGER_OCCURRENCE = "";

    private void writeTrigger(DbORTrigger trigger, PrintWriter writer, String owner,
            String tableName) throws DbException {
        writer.println("sr_occurrence_type trigger");
        writer.println("owner " + owner);
        writer.println("table_name " + tableName);

        String name = getPhysicalOrLogicalName(trigger);
        writer.println("trig_name " + name);

        // get event
        ORTriggerEvent event = trigger.getEvent();
        if (event != null) {
            int value = event.getValue();
            switch (value) {
            case ORTriggerEvent.DELETE:
                writer.println("event DELETE");
                break;
            case ORTriggerEvent.INSERT:
                writer.println("event INSERT");
                break;
            case ORTriggerEvent.UPDATE:
                writer.println("event UPDATE");
                break;
            // TODO other cases?
            } // end switch
        } // end if

        // description
        String desc = trigger.getDescription();
        if (desc != null) {
            writer.println("description " + desc);
            writer.println("_sr_e_o_t_");
        } // end if

        // when clause
        String whenCond = trigger.getWhenCondition();
        if (whenCond != null) {
            writer.println("when_clause " + whenCond);
            writer.println("_sr_e_o_t_");
        } // end if

        // body
        String body = trigger.getBody();
        if (body != null) {
            writer.println("trig_body " + body);
            writer.println("_sr_e_o_t_");
        } // end if
    } // end writeTrigger()

    private void writeProcedure(DbORProcedure proc, PrintWriter writer) throws DbException {
        DbORUser user = proc.getUser();
        String owner = getPhysicalOrLogicalName(user, "user");
        String procName = getPhysicalOrLogicalName(proc);
        String isFunction = proc.isFunction() ? "FUNCTION" : "PROCEDURE";

        // for each line
        String body = proc.getBody();
        StringTokenizer st = new StringTokenizer(body, "\n");
        while (st.hasMoreTokens()) {
            String line = st.nextToken();
            writeProcedureLine(owner, procName, isFunction, line, writer);
        } // end while
    } // end writeProcedure()

    /**
     * <pre>
     * sr_occurrence_type procedure     //header
     *   owner STEPHANE                   //user ID
     *   rdm_proc_name PROCA              //name
     *   proc_name PROCA                  //identifier
     *   is_fn FUNCTION                   //FUNCTION or PROCEDURE
     *   text    TOTAL                    //one line of procedure's body
     *   _sr_e_o_t_
     * </pre>
     * 
     * One 'sr_occurrence_type procedure' per procedure line, so several 'sr_occurrence_type
     * procedure' per actual procedure.
     */
    private static final String PROCEDURE_OCCURRENCE = "";

    private void writeProcedureLine(String owner, String procName, String isFunction, String line,
            PrintWriter writer) throws DbException {
        writer.println("sr_occurrence_type procedure");
        writer.println("owner " + owner);

        writer.println("rdm_proc_name " + procName);
        writer.println("proc_name " + procName);
        writer.println("is_fn " + isFunction);

        writer.println("text " + line);
        writer.println("_sr_e_o_t_");
    } // end writeProcedureLine()

    /**
     * <pre>
     * sr_occurrence_type sequence    //header
     *   owner STEPHANE                 //user ID
     *   name SEQ1                      //identifier
     *   min_value 1                    //integer
     *   max_value 10                   //integer
     *   increment 1                    //integer
     *   cycle Y                        //'Y' or 'N'
     *   order Y                        //'Y' or 'N'
     *   cache 2                        //integer
     * </pre>
     */
    private static final String SEQUENCE_OCCURRENCE = "";

    private void writeSequence(DbORASequence sequence, PrintWriter writer) throws DbException {
        writer.println("sr_occurrence_type sequence");

        DbORUser user = sequence.getUser();
        String owner = getPhysicalOrLogicalName(user, "user");
        writer.println("owner " + owner);

        String name = getPhysicalOrLogicalName(sequence);
        writer.println("name " + name);

        // minimun
        String min = sequence.getMinValue();
        if (min != null) {
            writer.println("min_value " + min);
        } // end if

        // maximun
        String max = sequence.getMaxValue();
        if (max != null) {
            writer.println("max_value " + max);
        } // end if

        // increment
        String inc = sequence.getIncrement();
        if (inc != null) {
            writer.println("increment " + inc);
        } // end if

        // cycle
        boolean cycle = sequence.isCycle();
        writer.println("cycle " + (cycle ? 'Y' : 'N'));

        // increment
        boolean order = sequence.isOrder();
        writer.println("order " + (order ? 'Y' : 'N'));

        // increment
        Integer cache = sequence.getCacheValue();
        if (cache != null) {
            writer.println("cache " + cache.intValue());
        } // end if
    } // end writeSequence()
} // end OracleExtractFileWriter
