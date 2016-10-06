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
import java.util.Iterator;
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
import org.modelsphere.sms.or.db.DbORFKeyColumn;
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
import org.modelsphere.sms.or.informix.db.DbINFDatabase;
import org.modelsphere.sms.or.informix.db.DbINFDbspace;
import org.modelsphere.sms.or.informix.db.DbINFTable;
import org.modelsphere.sms.or.informix.db.srtypes.INFLockMode;
import org.modelsphere.sms.or.informix.db.srtypes.INFLogMode;

/**
 * 
 * Writes an Informix extract file for RDM
 * 
 * This class writes an extract file for Informix, it is used to transfer SMS models into RDM using
 * its reverse engineering feature. Each Informix extract file contain a comment header and several
 * occurence clauses. Each occurrence clause starts with the line header 'occurrence
 * &lt;concept&gt;', where concept is one of {dbspace, database, user, table, view, column,
 * comb_pk_uk, comb_fk, index, action, constraint, procedure}. Each occurrence clause also contain
 * several (key, value) pairs.
 * 
 * In Informix, the value part must always be aligned on the 13th column : //GOOD: occurrence table
 * owner informix name customer
 * 
 * //WRONG: occurrence table owner informix name customer
 * 
 * @author unascribed
 * @version 1.0
 */
class InformixExtractFileWriter extends ExtractFileWriter {
    private static final String USER = "user";

    protected InformixExtractFileWriter(Controller controller) {
        super(controller);
    } // end InformixExtractFileWriter()

    //
    // PROTECTED METHODS
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
        String dbms = ts.getName();
        switch (rdmID) {
        case RdmDbmsIdMapping.RDM_INFORMIX:
            dbms = "Informix";
            break;
        case RdmDbmsIdMapping.RDM_INFORMIX_IDS92:
            dbms = "Informix Dynamic Server 92";
            break;
        case RdmDbmsIdMapping.RDM_INFORMIX_IDS93:
            dbms = "Informix Dynamic Server 93";
            break;
        case RdmDbmsIdMapping.RDM_INFORMIX_OL:
            dbms = "Informix OL";
            break;
        case RdmDbmsIdMapping.RDM_INFORMIX_SE:
            dbms = "Informix SE";
            break;
        case RdmDbmsIdMapping.RDM_INFORMIX_US:
            dbms = "Informix Universal Server";
            break;
        } // end switch

        DbINFDatabase database = (DbINFDatabase) dataModel.getDeploymentDatabase();
        String dbname = getPhysicalOrLogicalName(database, "<unspecified>");

        writer.println("--***** DO NOT MODIFY HEADER *****"); // NOT LOCALIZABLE
        writer.println("--Interface: SMS2RDM Interface"); // NOT LOCALIZABLE
        writer.println("--Version:   " + RDM_VERSION); // NOT LOCALIZABLE
        writer.println("--Database:  " + dbname); // NOT LOCALIZABLE
        writer.println("--DBMSID:    " + rdmID); // NOT LOCALIZABLE
        writer.println("--DBMS:      " + dbms); // NOT LOCALIZABLE
        writer.println("--******** END OF HEADER *********"); // NOT LOCALIZABLE
        writer.println();
    } // end writeHeader()

    protected void writeDataModel(DbORDataModel dataModel, File directory, ArrayList fileList)
            throws IOException, DbException {
        // create file and write file header
        File file = createFile(dataModel, directory, fileList);
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writeHeader(writer, dataModel);

        // get deployed database, if any
        DbINFDatabase database = (DbINFDatabase) dataModel.getDeploymentDatabase();
        if (database != null) {
            // For each dbspace
            DbRelationN relN = database.getComponents();
            DbEnumeration dbEnum = relN.elements(DbINFDbspace.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbINFDbspace dbspace = (DbINFDbspace) dbEnum.nextElement();
                writeDbspace(dbspace, writer);
            } // end while
            dbEnum.close();

            writeDatabase(database, writer);
        } // end if
        writer.println();

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
        writer.println();

        // For each table
        relN = dataModel.getComponents();
        dbEnum = relN.elements(DbORTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORTable table = (DbORTable) dbEnum.nextElement();
            writeTable(table, writer);
        } // end while
        dbEnum.close();
        writer.println();

        // For each view
        relN = dataModel.getComponents();
        dbEnum = relN.elements(DbORView.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORView view = (DbORView) dbEnum.nextElement();
            writeView(view, writer);
        } // end while
        dbEnum.close();
        writer.println();

        // For each table column
        relN = dataModel.getComponents();
        dbEnum = relN.elements(DbORTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORTable table = (DbORTable) dbEnum.nextElement();
            writeTableColumns(table, writer);
        } // end while
        dbEnum.close();

        // For each view column
        relN = dataModel.getComponents();
        dbEnum = relN.elements(DbORView.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORView view = (DbORView) dbEnum.nextElement();
            writeViewColumns(view, writer);
        } // end while
        dbEnum.close();
        writer.println();

        // For each PK
        relN = dataModel.getComponents();
        dbEnum = relN.elements(DbORTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORTable table = (DbORTable) dbEnum.nextElement();
            writeTablePKs(table, writer);
        } // end while
        dbEnum.close();
        writer.println();

        // For each FK
        relN = dataModel.getComponents();
        dbEnum = relN.elements(DbORTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORTable table = (DbORTable) dbEnum.nextElement();
            DbORUser user = table.getUser();
            String owner = getPhysicalOrLogicalName(user, USER);
            writeForeignKeysOfTable(owner, table, writer);
        } // end while
        dbEnum.close();
        writer.println();

        // For each index
        relN = dataModel.getComponents();
        dbEnum = relN.elements(DbORTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORTable table = (DbORTable) dbEnum.nextElement();
            writeTableIndexes(table, writer);
        } // end while
        dbEnum.close();
        writer.println();

        // For each PK, create an index
        relN = dataModel.getComponents();
        dbEnum = relN.elements(DbORTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORTable table = (DbORTable) dbEnum.nextElement();
            writeTablePKIndexes(table, writer);
        } // end while
        dbEnum.close();
        writer.println();

        // For each FK, create an index
        relN = dataModel.getComponents();
        dbEnum = relN.elements(DbORTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORTable table = (DbORTable) dbEnum.nextElement();
            writeTableFKIndexes(table, writer);
        } // end while
        dbEnum.close();
        writer.println();

        // For each trigger
        relN = dataModel.getComponents();
        dbEnum = relN.elements(DbORTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORTable table = (DbORTable) dbEnum.nextElement();
            writeTableTriggers(table, writer);
        } // end while
        dbEnum.close();
        writer.println();

        // For each check constraints
        relN = dataModel.getComponents();
        dbEnum = relN.elements(DbORTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORTable table = (DbORTable) dbEnum.nextElement();
            writeTableChecks(table, writer);
        } // end while
        dbEnum.close();
        writer.println();

        // For each operation
        if (database != null) {
            DbOROperationLibrary library = database.getOperationLibrary();
            if (library != null) {
                relN = library.getComponents();
                dbEnum = relN.elements(DbORProcedure.metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbORProcedure proc = (DbORProcedure) dbEnum.nextElement();
                    writeProcedure(proc, writer);
                } // end while
                dbEnum.close();
                writer.println();
            }
        } // end if

        String msg = MessageFormat.format(MSG_PATTERN, new Object[] { file.getName(),
                file.getParent() });
        m_controller.println(msg);
        m_controller.println();
        writer.close();
    } // end writeDataModel()

    /**
     * <pre>
     * occurrence  dbspace   //header
     * name        rootdbs   //identifier
     * dummy       dummy
     * &lt;/br&gt;
     * One per dbspace in the database
     * </pre>
     */
    private static final String DBSPACE_OCCURENCE = "";

    private void writeDbspace(DbINFDbspace dbspace, PrintWriter writer) throws DbException {
        String name = getPhysicalOrLogicalName(dbspace);
        write(writer, "occurrence", "dbspace");
        write(writer, "name", name);
        write(writer, "dummy", "dummy");
        writer.println();
    } // end writeDbspace()

    /**
     * <pre>
     * occurrence  database  //header
     *   name        stores7   //identifier
     *   dbs_name    rootdbs   //the dbsspace ID on which the database is stored
     *   log         1         //integer
     * &lt;/br&gt;
     *   one per data model
     *   log : 1 for LOG, 2 for BUFFERED_LOG, 3 for LOG_MODE_ANSI.
     * </pre>
     */
    private static final String DATABASE_OCCURRENCE = "";

    private void writeDatabase(DbINFDatabase database, PrintWriter writer) throws DbException {
        String name = getPhysicalOrLogicalName(database);
        write(writer, "occurrence", "database");
        write(writer, "name", name);

        DbINFDbspace dbspace = database.getDbspace();
        if (dbspace != null) {
            String dbsName = getPhysicalOrLogicalName(dbspace);
            write(writer, "dbs_name", dbsName);
        }

        INFLogMode mode = database.getLogging();
        if (mode != null) {
            write(writer, "log", mode.getValue());
        }

        writer.println();
    } // end writeDatabase()

    /**
     * <pre>
     * occurrence  user      //header
     *   name        informix  //identifier
     *   dummy       dummy
     * &lt;/br&gt;
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
            write(writer, "occurrence", "user");
            write(writer, "name", username);
            write(writer, "dummy", "dummy");
            writer.println();
            userNodeEmpty = false;
        } // end while
        dbEnum.close();
        return userNodeEmpty;
    } // end writeUserNode()

    /**
     * <pre>
     * occurrence  table     //header
     * owner       informix  //user ID
     * name        customer  //identifier
     * dbs_name    rootdbs   //the dbspace ID on which the table is stored
     * lock_mode   P         //one of {'P', 'R', 'T'} for page, row or table
     * init_size   16        //integer, in bytes
     * next_size   16        //integer, in bytes
     * &lt;/br&gt;
     *   One 'occurence table' per actual table. Of course, we can have several
     *   tables in the data model.
     * </pre>
     */
    private static final String TABLE_OCCURRENCE = "";

    protected void writeTable(DbORTable table, PrintWriter writer) throws DbException {
        String tableName = getPhysicalOrLogicalName(table);
        m_controller.println(DbORTable.metaClass.getGUIName() + " " + tableName + "..");
        write(writer, "occurrence", "table");

        DbORUser user = table.getUser();
        String owner = getPhysicalOrLogicalName(user, USER);
        write(writer, "owner", owner);
        write(writer, "name", tableName);

        // specific values
        DbINFTable tab = (DbINFTable) table;
        DbINFDbspace dbspace = tab.getDbspace();
        if (dbspace != null) {
            String dbsName = getPhysicalOrLogicalName(dbspace, "");
            write(writer, "dbs_name", dbsName);
        }

        INFLockMode mode = tab.getLockMode();
        if (mode != null) {
            int value = mode.getValue();
            char ch = 'P';
            switch (value) {
            case INFLockMode.PAGE:
                ch = 'P';
                break;
            case INFLockMode.ROW:
                ch = 'R';
                break;
            case INFLockMode.TABLE:
                ch = 'T';
                break;
            } // end case

            write(writer, "lock_mode", ch);
        } // end if

        int init_size = computeActualValue(tab.getExtentSize());
        int next_size = computeActualValue(tab.getNextExtentSize());
        write(writer, "init_size", init_size);
        write(writer, "next_size", next_size);
        writer.println();
    } // end writeTable()

    /**
     * <pre>
     * occurrence  view        //header
     * owner       informix    //user ID
     * name        custview    //view name
     * seq         0           //integer
     * select      create view &quot;informix&quot;.custview (firstname,lastname,company,city
     * _sr_e_o_t_
     * &lt;br&gt;
     * occurrence  view
     * owner       informix
     * name        custview
     * seq         1
     * select      ) as select x0.fname ,x0.lname ,x0.company ,x0.city from &quot;inform
     * _sr_e_o_t_
     * &lt;/br&gt;
     *   We can have several 'occurrence view' per actual view.  In this case, their 'name'
     * stays the same, but we have 'seq   0', 'seq  1', etc.  The various 'select' fields
     * are concatenated to retrieve the actual select rule of the view.  Of course, we
     * can have several views in the data model.
     * </pre>
     */
    private static final String VIEW_OCCURRENCE = "";

    private void writeViewLine(String viewName, String owner, int seq, String line,
            PrintWriter writer) {
        write(writer, "occurrence", "view");
        write(writer, "owner", owner);
        write(writer, "name", viewName);
        write(writer, "seq", seq);
        write(writer, "select", line);

        writer.println("_sr_e_o_t_");
        writer.println();
    } // end writeViewLine()

    /**
     * <pre>
     * occurrence  column         //header
     * owner       informix       //user ID
     * tab_name    customer       //table ID or view ID
     * name        customer_num   //identifier
     * seq         10             //order of the column in its table
     * type        262            //data type and null option
     * length      0              //length and scale, if applicable
     * &lt;/br&gt;
     *   One 'occurrence column' per actual column.  'seq' values are normally 10, 20,
     * 30, etc.  (type % 256) indicates the actual type (here, (262 % 256) gives 6,
     * indicating a SERIAL type.  A 'type' value &gt; 256 indicates the NOT NULL option.
     * length is computed differently according the 'type' value.
     * </pre>
     * 
     * @see method getTypeID() to get the list of type IDs (0 = CHAR, 6 = SERIAL, etc.)
     * @see method computeLength() to compute length value according the column type, precision and
     *      scale.
     */
    private static final String COLUMN_OCCURRENCE = "";

    protected void writeColumn(DbORColumn column, PrintWriter writer, String tableName, int colID)
            throws DbException {
        write(writer, "occurrence", "column");
        DbORAbsTable table = (DbORAbsTable) column.getCompositeOfType(DbORAbsTable.metaClass);
        String tabName = getPhysicalOrLogicalName(table);
        String colName = getPhysicalOrLogicalName(column);
        DbORUser user = table.getUser();
        String owner = getPhysicalOrLogicalName(user, USER);
        write(writer, "owner", owner);
        write(writer, "tab_name", tabName);
        write(writer, "name", colName);
        write(writer, "seq", colID);

        DbORTypeClassifier type = column.getType();
        if (type != null) {
            int typeID = getTypeID(type);
            int len = computeLength(column, typeID);

            // value > 256 to indicate NOT NULL columns
            if (!column.isNull()) {
                typeID += 256;
            }

            write(writer, "type", typeID);
            write(writer, "length", len);
        } // end if

        /*
         * def_type def_val _sr_e_o_t_ def_val _sr_e_o_t_ def_val _sr_e_o_t_ def_val _sr_e_o_t_
         */
        writer.println();
    } // end writeColumn

    /**
     * <pre>
     * occurrence  comb_pk_uk    //header
     *   owner       informix      //user ID
     *   tab_name    customer      //table ID
     *   name        u100_1        //identifier
     *   type        P             //one of {'P', 'U'}, for primary, unique
     *   &lt;/br&gt;
     *     One 'occurrence comb_pk_uk' per actual primary/unique key.  For each key,
     * an 'occurrence index' is also generated.  See the index to get the key columns.
     * </pre>
     */
    private static final String PUKEY_OCCURRENCE = "";

    protected void writePuKey(DbORPrimaryUnique key, PrintWriter writer, String owner,
            String tableName) throws DbException {
        write(writer, "occurrence", "comb_pk_uk");
        String keyName = getPhysicalOrLogicalName(key);
        write(writer, "owner", owner);
        write(writer, "tab_name", tableName);
        write(writer, "name", keyName);

        char type = key.isPrimary() ? 'P' : 'U';
        write(writer, "type", type);
        writer.println();
    } // end writePuKey()

    /**
     * <pre>
     * occurrence  comb_fk     //header
     *   owner       informix    //user ID
     *   tab_name    orders      //table ID
     *   name        r101_4      //identifier
     *   powner      informix    //user ID of the opposite table
     *   ptab_name   customer    //table ID of the opposite table
     *   del_rule    R           //always 'R'
     *   &lt;/br&gt;
     *     One 'occurrence comb_fk' per actual foreign key.  For each foreign key,
     *     an 'occurrence index' is also generated.  See the index to get the key columns.
     * </pre>
     */
    private static final String FKEY_OCCURRENCE = "";

    void writeForeign(DbORForeign fKey, PrintWriter writer, String owner, String tableName)
            throws DbException {
        write(writer, "occurrence", "comb_fk");

        write(writer, "owner", owner);
        write(writer, "tab_name", tableName);
        String keyName = getPhysicalOrLogicalName(fKey);
        write(writer, "name", keyName);

        // get opposite table
        DbORAssociationEnd end = fKey.getAssociationEnd();
        DbORAssociationEnd oppEnd = end.getOppositeEnd();
        DbORAbsTable oppTable = oppEnd.getClassifier();
        DbORUser oppUser = oppTable.getUser();
        String oppTableName = getPhysicalOrLogicalName(oppTable);
        String oppUserName = getPhysicalOrLogicalName(oppUser, USER);
        write(writer, "powner", oppUserName);
        write(writer, "ptab_name", oppTableName);

        // get delete rule
        char ruleCat = 'R';
        ORValidationRule rule = end.getDeleteRule();
        if (rule != null) {
            int value = rule.getValue();
            switch (value) {
            case ORValidationRule.NONE:
                // writer.println("del_rule NO ACTION");
                break;
            } // end switch
        } // end if

        write(writer, "del_rule", ruleCat);
        writer.println();
    } // end writeForeign()

    //
    // PRIVATE METHODS
    //

    private void writeTableColumns(DbORTable table, PrintWriter writer) throws DbException {
        String tableName = getPhysicalOrLogicalName(table);

        // For each column
        DbRelationN relN = table.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORColumn.metaClass);
        int colID = 0;
        while (dbEnum.hasMoreElements()) {
            colID += 10;
            DbORColumn column = (DbORColumn) dbEnum.nextElement();
            writeColumn(column, writer, tableName, colID);
        } // end while
        dbEnum.close();
    } // end writeTableColumns

    private void writeViewColumns(DbORView view, PrintWriter writer) throws DbException {
        String viewName = getPhysicalOrLogicalName(view);

        // For each column
        DbRelationN relN = view.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORColumn.metaClass);
        int colID = 0;
        while (dbEnum.hasMoreElements()) {
            colID += 10;
            DbORColumn column = (DbORColumn) dbEnum.nextElement();
            writeColumn(column, writer, viewName, colID);
        } // end while
        dbEnum.close();

    } // end writeViewColumns()

    private void writeTablePKs(DbORTable table, PrintWriter writer) throws DbException {
        String tableName = getPhysicalOrLogicalName(table);
        DbORUser user = table.getUser();
        String owner = getPhysicalOrLogicalName(user, USER);

        // For each PK/UK
        DbRelationN relN = table.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORPrimaryUnique.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORPrimaryUnique puKey = (DbORPrimaryUnique) dbEnum.nextElement();
            writePuKey(puKey, writer, owner, tableName);
        } // end while
        dbEnum.close();
    } // end writeTablePKs

    private void writeTablePKIndexes(DbORTable table, PrintWriter writer) throws DbException {
        String tableName = getPhysicalOrLogicalName(table);
        DbORUser user = table.getUser();
        String owner = getPhysicalOrLogicalName(user, USER);

        // For each PK/UK
        DbRelationN relN = table.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORPrimaryUnique.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORPrimaryUnique puKey = (DbORPrimaryUnique) dbEnum.nextElement();
            writePKIndex(puKey, writer, owner, tableName);
        } // end while
        dbEnum.close();
    } // end writeTablePKIndexes

    private void writeTableFKIndexes(DbORTable table, PrintWriter writer) throws DbException {
        String tableName = getPhysicalOrLogicalName(table);
        DbORUser user = table.getUser();
        String owner = getPhysicalOrLogicalName(user, USER);

        // For each FK
        DbRelationN relN = table.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORForeign.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORForeign fKey = (DbORForeign) dbEnum.nextElement();
            writeFKIndex(fKey, writer, owner, tableName);
        } // end while
        dbEnum.close();
    } // end writeTablePKIndexes

    private void writeTableChecks(DbORTable table, PrintWriter writer) throws DbException {
        String tableName = getPhysicalOrLogicalName(table);
        DbORUser user = table.getUser();
        String owner = getPhysicalOrLogicalName(user, USER);

        // For each check constraint
        DbRelationN relN = table.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORCheck.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORCheck check = (DbORCheck) dbEnum.nextElement();
            writeCheckConstraint(check, writer, owner, tableName);
        } // end while
        dbEnum.close();
    } // end writeTableChecks()

    private void writeTableIndexes(DbORTable table, PrintWriter writer) throws DbException {
        String tableName = getPhysicalOrLogicalName(table);
        DbORUser user = table.getUser();
        String owner = getPhysicalOrLogicalName(user, USER);

        // For each index
        DbRelationN relN = table.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORIndex.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORIndex index = (DbORIndex) dbEnum.nextElement();
            writeIndex(index, writer, owner, tableName);
        } // end while
        dbEnum.close();
    } // end writeTableIndexes()

    private void writeTableTriggers(DbORTable table, PrintWriter writer) throws DbException {
        String tableName = getPhysicalOrLogicalName(table);
        DbORUser user = table.getUser();
        String owner = getPhysicalOrLogicalName(user, USER);

        // For each trigger
        DbRelationN relN = table.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORTrigger.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORTrigger trigger = (DbORTrigger) dbEnum.nextElement();
            writeTrigger(trigger, writer, owner, tableName);
        } // end while
        dbEnum.close();
    } // end writeTableTriggers()

    /**
     * <pre>
     * occurrence  index      //header
     *   owner       informix   //user ID
     *   name        100_1      //identifier
     *   tab_owner   informix   //user ID of its table
     *   tab_name    customer   //table ID
     *   comb_name   u100_1     //may refer to comb_pk_uk or comb_fk
     *   unique      U          //'U'nique or 'D'uplicated
     *   col_no1     1          //the 1st column in the index refers to the 1st column in tab_name
     *   col_no2     4          //the 2nd column in the index refers to the 4th column in tab_name
     *   &lt;/br&gt;
     *     One 'occurrence index' per actual index, and per each PK, UK and FK.  We
     *   can have col_no1, col_no2, col_no3, up to col_no116 (maximun of 16 columns
     *   in a combination).
     * </pre>
     */
    private static final String INDEX_OCCURRENCE = "";

    private void writePKIndex(DbORPrimaryUnique puKey, PrintWriter writer, String owner,
            String tableName) throws DbException {
        write(writer, "occurrence", "index");
        write(writer, "owner", owner);
        String name = getPhysicalOrLogicalName(puKey);
        write(writer, "name", name.substring(1));
        write(writer, "tab_owner", owner);
        write(writer, "tab_name", tableName);

        write(writer, "comb_name", name);
        write(writer, "unique", 'U');

        DbRelationN relN = puKey.getColumns();
        DbEnumeration dbEnum = relN.elements(DbORColumn.metaClass);
        int cnt = 0;
        while (dbEnum.hasMoreElements()) {
            DbORColumn column = (DbORColumn) dbEnum.nextElement();
            writeIndexCol(writer, column, ++cnt);
        } // end while
        dbEnum.close();

        writer.println();
    } // end writePKIndex()

    private void writeFKIndex(DbORForeign fKey, PrintWriter writer, String owner, String tableName)
            throws DbException {
        write(writer, "occurrence", "index");
        write(writer, "owner", owner);
        String name = getPhysicalOrLogicalName(fKey);
        write(writer, "name", name.substring(1));
        write(writer, "tab_owner", owner);
        write(writer, "tab_name", tableName);

        write(writer, "comb_name", name);
        write(writer, "unique", 'D');

        DbRelationN relN = fKey.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORFKeyColumn.metaClass);
        int cnt = 0;
        while (dbEnum.hasMoreElements()) {
            DbORFKeyColumn key = (DbORFKeyColumn) dbEnum.nextElement();
            DbORColumn column = key.getColumn();
            writeIndexCol(writer, column, ++cnt);
        } // end while
        dbEnum.close();

        writer.println();
    } // end writeFKIndex()

    void writeIndex(DbORIndex index, PrintWriter writer, String owner, String tableName)
            throws DbException {
        write(writer, "occurrence", "index");
        write(writer, "owner", owner);
        String name = getPhysicalOrLogicalName(index);
        write(writer, "name", name.substring(1));
        write(writer, "tab_owner", owner);
        write(writer, "tab_name", tableName);
        write(writer, "comb_name", name);

        char unique = index.isUnique() ? 'U' : 'D';
        write(writer, "unique", unique);

        DbRelationN relN = index.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORIndexKey.metaClass);
        int cnt = 0;
        while (dbEnum.hasMoreElements()) {
            DbORIndexKey key = (DbORIndexKey) dbEnum.nextElement();
            DbORColumn column = key.getIndexedElement();
            writeIndexCol(writer, column, ++cnt);
        } // end while
        dbEnum.close();

        /*
         * occurrence index owner informix name 100_1 tab_owner informix tab_name customer comb_name
         * u100_1 unique U cluster col_no1 1 col_no2 0 col_no3 0 col_no4 0 col_no5 0 col_no6 0
         * col_no7 0 col_no8 0 col_no9 0 col_no10 0 col_no11 0 col_no12 0 col_no13 0 col_no14 0
         * col_no15 0 col_no16 0
         */

        writer.println();
    } // end writeIndex()

    private void writeIndexCol(PrintWriter writer, DbORColumn column, int cnt) throws DbException {
        // compute position of column
        int pos = getPositionOf(column);

        String key = "col_no" + cnt;
        write(writer, key, pos);
    } // end writeIndexCol()

    private int getPositionOf(DbORColumn column) throws DbException {
        int pos = 0;
        DbORAbsTable table = (DbORAbsTable) column.getCompositeOfType(DbORAbsTable.metaClass);
        DbRelationN relN = table.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORColumn.metaClass);
        int i = 1;
        while (dbEnum.hasMoreElements()) {
            DbORColumn col = (DbORColumn) dbEnum.nextElement();
            if (col.equals(column)) {
                pos = i;
                break;
            }

            i++;
        } // end while
        dbEnum.close();

        return pos;
    }

    private void writeView(DbORView view, PrintWriter writer) throws DbException {
        String viewName = getPhysicalOrLogicalName(view);
        m_controller.println(DbORView.metaClass.getGUIName() + " " + viewName + "..");
        DbORUser user = view.getUser();
        String owner = getPhysicalOrLogicalName(user, USER);

        // get selection rule
        int seq = 0;
        String pattern = "create view \"{0}\".{1} {2};";
        String rule = view.getSelectionRule();
        String text = MessageFormat.format(pattern, new Object[] { owner, viewName, rule });

        SplitIterator iter = new SplitIterator(text, 64);
        while (iter.hasNext()) {
            String line = (String) iter.next();
            writeViewLine(viewName, owner, seq++, line, writer);
        } // end while
    } // end writeView()

    private void writeTrigger(DbORTrigger trigger, PrintWriter writer, String owner,
            String tableName) throws DbException {
        String name = getPhysicalOrLogicalName(trigger);
        String body = trigger.getBody();
        TriggerBodyStructure struct = new TriggerBodyStructure(body);

        SplitIterator iter = new SplitIterator(struct.body, 256);
        int cnt = 0;
        while (iter.hasNext()) {
            String part = (String) iter.next();
            writeAction(trigger, writer, owner, tableName, name, part, ++cnt, struct);
        } // end while

        // writeAction2(trigger, writer, owner, tableName, name);
    } // end writeTrigger()

    /**
     * <pre>
     * occurrence  action       //header
     *   owner       informix     //user ID
     *   tab_name    items        //table ID
     *   name        upqty_i      //trigger name
     *   seq         1            //integer
     *   act_cat     I            //'I'nsert, 'D'elete or 'U'pdate
     *   old         pre_upd      //old name
     *   new         post_upd     //new name
     *   text        for each row ( insert into &quot;informix&quot;.lo
     *   _sr_e_o_t_
     *   text        g_record (item_num,ord_num,username,update_time,old_qty,new_qty)
     *   _sr_e_o_t_
     *   text         values (pre_upd.item_num ,pre_upd.order_num ,USER ,CURRENT yea
     *   _sr_e_o_t_
     *   text        r to fraction(3) ,pre_upd.quantity ,post_upd.quantity ))
     *   _sr_e_o_t_
     *   &lt;/br&gt;
     *     One or several 'occurrence action' per actual trigger, depending of the
     *   length of the action 'text'.  There are four fields 'text', each of them
     *   can contain up to 64 characters.  If the trigger body exceeds 256 characters
     *   (four times 64), them another 'occurrence  action' is generated with seq == 2,
     *   and so on until the whole body is generated. The trigger body in SMS always
     *   starts with 'REFERENCING OLD AS &lt;oldName&gt; NEW AS &lt;newName&gt;', this header
     *   is removed from the action 'text' and &lt;oldName&gt;, &lt;newName&gt; are kept in the
     *   'old' and 'new' fields.
     * </pre>
     */
    private static final String ACTION_OCCURRENCE = "";

    private void writeAction(DbORTrigger trigger, PrintWriter writer, String owner,
            String tableName, String name, String bodypart, int seq, TriggerBodyStructure struct)
            throws DbException {
        write(writer, "occurrence", "action");
        write(writer, "owner", owner);
        write(writer, "tab_name", tableName);
        write(writer, "name", name);
        write(writer, "seq", seq);

        // get event
        ORTriggerEvent event = trigger.getEvent();
        if (event != null) {
            int value = event.getValue();
            switch (value) {
            case ORTriggerEvent.DELETE:
                write(writer, "act_cat", 'D');
                break;
            case ORTriggerEvent.INSERT:
                write(writer, "act_cat", 'I');
                break;
            case ORTriggerEvent.UPDATE:
                write(writer, "act_cat", 'U');
                break;
            // TODO other cases?
            } // end switch
        } // end if

        write(writer, "old", struct.old);
        write(writer, "new", struct.niu);

        // print body
        int len = bodypart.length();
        for (int i = 0; i < 4; i++) {
            String text = "";
            int start = i * 64;
            int end = (i + 1) * 64;

            if (start <= len) {
                if (end > len) {
                    text = bodypart.substring(start);
                } else {
                    text = bodypart.substring(start, end);
                }
            } // end if

            write(writer, "text", text);
            writer.println("_sr_e_o_t_");
        } // end for

        writer.println();
    } // end writeAction

    private void writeAction2(DbORTrigger trigger, PrintWriter writer, String owner,
            String tableName, String name) throws DbException {
        write(writer, "occurrence", "action2");
        write(writer, "owner", owner);
        write(writer, "tab_name", tableName);
        write(writer, "name", name);
        // seq 0

        /*
         * //act_cols //get action cols String body = trigger.getWhenCondition(); SplitIterator iter
         * = new SplitIterator(body, 64); while (iter.hasNext()) { String line =
         * (String)iter.next(); writeBodyLine(line, writer); } //end while
         */
        writer.println();
    } // end writeAction2()

    private void writeBodyLine(String line, PrintWriter writer) {
        write(writer, "text", line);
        writer.println("_sr_e_o_t_");
    } // end writeBodyLine()

    /**
     * <pre>
     * occurrence  constraint       //header
     *   owner       informix         //user ID
     *   tab_name    items            //table ID
     *   name        c104_15          //identifier
     *   text        (quantity &gt;= 1 ) //check constraint body
     *   _sr_e_o_t_
     *   &lt;/br&gt;
     *   One 'occurrence  constraint' per check constraint.
     * </pre>
     */
    private static final String CHECK_OCCURRENCE = "";

    private void writeCheckConstraint(DbORCheck check, PrintWriter writer, String owner,
            String tableName) throws DbException {
        String name = getPhysicalOrLogicalName(check);

        write(writer, "occurrence", "constraint");
        write(writer, "owner", owner);
        write(writer, "tab_name", tableName);
        write(writer, "name", name);

        // seq 0
        String cond = check.getCondition();
        write(writer, "text", cond);
        writer.println("_sr_e_o_t_");
        writer.println();

    } // end writeCheckConstraint()

    // protected visibility to be JAVADOCumented
    protected static final int CHAR = 0;
    protected static final int SMALLINT = 1;
    protected static final int INT = 2;
    protected static final int FLOAT = 3;
    protected static final int SMALLFLOAT = 4;
    protected static final int DEC = 5;
    protected static final int SERIAL = 6;
    protected static final int DATE = 7;
    protected static final int MONEY = 8;
    protected static final int DATETIME = 10;
    protected static final int BYTE = 11;
    protected static final int TEXT = 12;
    protected static final int VARCHAR = 13;
    protected static final int INTERVAL = 14;
    protected static final int NCHAR = 15;
    protected static final int NVARCHAR = 16;
    protected static final int INT8 = 17;
    protected static final int SERIAL8 = 18;
    protected static final int VARLEN_OPAQUE = 40;
    protected static final int FIXEDLEN_OPAQUE = 41;

    /**
     * For a 'type', returns its Informix type ID. For instance, if type == CHAR, return 0; if type
     * == SERIAL, return 6, etc. Informix Type IDs are documented in : IBM Informix Guide to SQL:
     * Reference (IDS-SQL-Reference-v9.3-8338.pdf) System Catalog Tables, page 1-29 <br>
     * <br>
     * for CHAR, CHARACTER : returns 0.<br>
     * for SMALLINT : returns 1.<br>
     * for INT : returns 2.<br>
     * for FLOAT : returns 3.<br>
     * for SMALLFLOAT, REAL : returns 4.<br>
     * for DEC, NUMERIC : returns 5.<br>
     * for SERIAL : returns 6.<br>
     * etc..
     */
    private int getTypeID(DbORTypeClassifier type) throws DbException {
        int typeID = 0;

        String name = type.getName();

        if (name.equals("BLOB"))
            typeID = FIXEDLEN_OPAQUE;
        else if (name.equals("BOOLEAN"))
            typeID = FIXEDLEN_OPAQUE;
        else if (name.equals("BYTE"))
            typeID = BYTE;
        else if ((name.equals("CHAR") || name.equals("CHARACTER")))
            typeID = CHAR;
        else if (name.equals("CLOB"))
            typeID = FIXEDLEN_OPAQUE;
        else if (name.equals("DATE"))
            typeID = DATE;
        else if (name.startsWith("DATETIME"))
            typeID = DATETIME;
        else if ((name.equals("DEC") || name.equals("DECIMAL")))
            typeID = DEC;
        else if ((name.equals("DOUBLE PRECISION") || name.equals("FLOAT")))
            typeID = FLOAT;
        else if ((name.equals("INT") || name.equals("INTEGER")))
            typeID = INT;
        else if (name.equals("INT8"))
            typeID = INT8;
        else if (name.startsWith("INTERVAL"))
            typeID = INTERVAL;
        else if (name.equals("LVARCHAR"))
            typeID = VARLEN_OPAQUE;
        else if (name.equals("MONEY"))
            typeID = MONEY;
        else if (name.equals("NCHAR"))
            typeID = NCHAR;
        else if (name.equals("NVARCHAR"))
            typeID = NVARCHAR;
        else if (name.equals("NUMERIC"))
            typeID = DEC;
        else if (name.equals("REAL"))
            typeID = SMALLFLOAT;
        else if (name.equals("SERIAL"))
            typeID = SERIAL;
        else if (name.equals("SERIAL8"))
            typeID = SERIAL8;
        else if (name.equals("SMALLFLOAT"))
            typeID = SMALLFLOAT;
        else if (name.equals("SMALLINT"))
            typeID = SMALLINT;
        else if (name.equals("TEXT"))
            typeID = TEXT;
        else if ((name.equals("VARCHAR") || name.equals("CHARACTER VARYING")))
            typeID = VARCHAR;

        return typeID;
    } // end getTypeID()

    /**
     * Compute 'length' value in 'occurrence column' according the column type, precision and scale.
     * Length formulae are taken from IBM Informix Guide to SQL: page 1-30. <br>
     * <br>
     * for DEC, MONEY : returns (len * 256) + scale.<br>
     * for DATETIME, INTERVAL : returns (len * 256) + scale.<br>
     * for VARCHAR, NVARCHAR : returns (scale * 256) + len.<br>
     * for BYTE, CHAR, NCHAR, TEXT : returns len.<br>
     * for all others, return 0<br>
     */
    private int computeLength(DbORColumn column, int typeID) throws DbException {
        int actualLength = 0;

        int len = computeActualValue(column.getLength());
        int scale = computeActualValue(column.getNbDecimal());

        switch (typeID) {
        case DEC:
        case MONEY:
            actualLength = (len * 256) + scale;
            break;
        case VARCHAR:
        case NVARCHAR:
            actualLength = (scale * 256) + len; // special case
            break;
        case DATETIME:
        case INTERVAL:
            actualLength = (len * 256) + scale;
            break;
        case BYTE:
        case CHAR:
        case NCHAR:
        case TEXT:
            actualLength = len;
            break;
        } // end switch

        return actualLength;
    } // end computLength()

    private void writeProcedure(DbORProcedure proc, PrintWriter writer) throws DbException {
        String body = proc.getBody();

        SplitIterator iter = new SplitIterator(body, 256);
        int cnt = 0;
        while (iter.hasNext()) {
            String part = (String) iter.next();
            writeProcedurePart(proc, writer, part, ++cnt);
        } // end while

    } // end writeProcedure()

    /**
     * <pre>
     * occurrence  procedure     //header
     *   owner       informix      //user ID
     *   name_only   Procedure     //procedure name
     *   procid      0             //procedure number
     *   name        Procedure.0   //concatenation of name and number
     *   dba         n             //'y'es or 'n'o
     *   is_proc     t             //'t'rue for procedure, 'f'alse for function
     *   seq         1             //integer
     *   text        select column01, column02, column03,  column04,  column05,  colu
     *   _sr_e_o_t_
     *   text        mn06, column07,  column08,  column09,  column10,  column11,
     *   _sr_e_o_t_
     *   text        column12,  column13, column14,  column15, column16, column17
     *   _sr_e_o_t_
     *   text        , column18, column19, column20 from table01, table02, table03 wh
     *   _sr_e_o_t_
     *   &lt;/br&gt;
     *       One or several 'occurrence procedure' per actual procedure, depending of the
     *   length of the procedure 'text'.  There are four fields 'text', each of them
     *   can contain up to 64 characters.  If the procedure body exceeds 256 characters
     *   (four times 64), them another 'occurrence procedure' is generated with seq == 2,
     *   and so on until the whole body is generated.
     * </pre>
     */
    private static final String PROCEDURE_OCCURRENCE = "";

    private void writeProcedurePart(DbORProcedure proc, PrintWriter writer, String part, int seq)
            throws DbException {
        DbORUser user = proc.getUser();
        String owner = getPhysicalOrLogicalName(user);
        String name_only = getPhysicalOrLogicalName(proc);
        String id = proc.getId();
        String name = name_only + "." + id;
        char isProc = proc.isFunction() ? 'f' : 't';

        write(writer, "occurrence", "procedure");
        write(writer, "owner", owner);
        write(writer, "name_only", name_only);
        write(writer, "procid", id);
        write(writer, "name", name);
        write(writer, "dba", "n");
        write(writer, "is_proc", isProc);
        write(writer, "seq", seq);

        int len = part.length();
        for (int i = 0; i < 4; i++) {
            String text = "";
            int start = i * 64;
            int end = (i + 1) * 64;

            if (start <= len) {
                if (end > len) {
                    text = part.substring(start);
                } else {
                    text = part.substring(start, end);
                }
            } // end if

            write(writer, "text", text);
            writer.println("_sr_e_o_t_");
        } // end for

        writer.println();
    } // end writeProcedurePart

    private void write(PrintWriter writer, String key, int value) {
        write(writer, key, Integer.toString(value));
    }

    private void write(PrintWriter writer, String key, char value) {
        write(writer, key, Character.toString(value));
    }

    private static final int KEY_MARGIN = 12;

    private void write(PrintWriter writer, String key, String value) {
        int filler = KEY_MARGIN - key.length();
        if (filler < 1) {
            filler = 1;
        }

        writer.print(key);
        for (int i = 0; i < filler; i++) {
            writer.print(' ');
        }

        writer.println(value);
    } // end write

    //
    // INNER CLASS
    //
    private static class SplitIterator implements Iterator {
        private String m_text;
        private int m_size;
        private boolean m_hasNext = true;
        private int m_len = 0;
        private int m_start = 0;

        SplitIterator(String text, int size) {
            m_text = text;
            m_size = size;
            if (text == null) {
                m_hasNext = false;
            } else {
                m_len = text.length();
            }
        } // end StringSplitter()

        public boolean hasNext() {
            return m_hasNext;
        }

        public Object next() {
            String line = null;
            if (m_len > m_size) {
                line = m_text.substring(m_start, m_start + m_size);
                m_start += m_size;
                m_len -= m_size;
            } else {
                line = (m_text == null) ? null : m_text.substring(m_start);
                m_hasNext = false;
            }

            return line;
        } // end next()

        public void remove() {
        }
    } // end StringSplitter

    private static class TriggerBodyStructure {
        String old = null;
        String niu = null;
        String body = null;

        TriggerBodyStructure(String rawBody) {
            if (rawBody != null) {
                parse(rawBody);
            }
        }

        private void parse(String rawBody) {
            int wordCnt = 0;
            StringTokenizer st = new StringTokenizer(rawBody);
            while (st.hasMoreTokens()) {
                String token = st.nextToken();

                if (wordCnt > 6)
                    break;

                switch (wordCnt) {
                case 0:
                    // REFERENCING
                    break;
                case 1:
                    // OLD
                    break;
                case 2:
                case 5:
                    // AS
                    break;
                case 3:
                    old = token;
                    break;
                case 4:
                    // NEW
                    break;
                case 6:
                    niu = token;
                    break;
                } // end switch

                wordCnt++;
            } // end while

            String end = "NEW AS " + niu;
            int idx = rawBody.indexOf(end);
            if (idx > 0) {
                idx += end.length();
                body = rawBody.substring(idx);
            }
        } // end parse()
    } // end TriggerBodyStructure

} // end InformixExtractFileWriter
