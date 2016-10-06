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

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORIndexKey;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.db.srtypes.ORIndexKeySort;

/**
 *
 * This class writes an extract file for ANSI, it is used to transfer
 * SMS models into external tools using its reverse engineering feature.  Each ANSI
 * extract file contain a comment header and several occurence clauses.
 * Each occurrence clause starts with the line header 'sr_occurrence_type
 * &lt;concept&gt;', where concept is one of {table, column, comb_pk, comb_fk,
 * index}. Views, triggers, procedures and physical concepts are not transfered.
 * Each occurence clause also contain several (key, value)pairs.
 * <br><br>
 * Each SMS data model whose target system is different from Oracle or Informix
 * is tranfered using this ANSI extract format.
 * <pre>
 * @author unascribed
 * @version 1.0
 */

/**
 * Writes an ANSI extract file for an external tool
 */
class AnsiExtractFileWriter extends ExtractFileWriter {

    protected AnsiExtractFileWriter(Controller controller) {
        super(controller);
    }

    //
    // protected methods
    //
    protected void writeHeader(PrintWriter writer, DbORDataModel dataModel) throws DbException {
        DbSMSTargetSystem ts = dataModel.getTargetSystem();

        writer.println("--***** DO NOT MODIFY HEADER *****"); // NOT LOCALIZABLE
        writer.println("--Interface: SMS2RDM Interface"); // NOT LOCALIZABLE
        writer.println("--Version:   " + RDM_VERSION); // NOT LOCALIZABLE
        writer.println("--Database:  <unspecified>"); // NOT LOCALIZABLE
        writer.println("--DBMSID:    0"); // NOT LOCALIZABLE
        writer.println("--DBMS:      ANSI"); // NOT LOCALIZABLE
        writer.println("--******** END OF HEADER *********"); // NOT LOCALIZABLE
        writer.println();
    } // end writeHeader()

    protected void writeDataModel(DbORDataModel dataModel, File directory, ArrayList fileList)
            throws IOException, DbException {
        // create file and write file header
        File file = createFile(dataModel, directory, fileList);
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writeHeader(writer, dataModel);

        // For each table, create table, columns and PKs
        DbRelationN relN = dataModel.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORTable table = (DbORTable) dbEnum.nextElement();
            writeTable(table, writer);
        } // end while
        dbEnum.close();

        // For each table, create FKs
        dbEnum = relN.elements(DbORTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORTable table = (DbORTable) dbEnum.nextElement();
            DbORUser user = table.getUser();
            String owner = null;
            if (user != null) {
                owner = getPhysicalOrLogicalName(user);
            }
            writeForeignKeysOfTable(owner, table, writer);
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
     * sr_occurrence_type table   //header
     *   name MOVIE_STORE           //identifier
     *   &lt;/br&gt;
     * </pre>
     */
    private static final String TABLE_OCCURRENCE = "";

    protected void writeTable(DbORTable table, PrintWriter writer) throws DbException {
        String tableName = table.getPhysicalName();
        if ((tableName == null) || (tableName.equals("")))
            tableName = table.getName();

        m_controller.println(DbORTable.metaClass.getGUIName() + " " + tableName + "..");
        writer.println("sr_occurrence_type table");
        writer.println("name " + tableName);
        writer.println();

        // For each column
        DbRelationN relN = table.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORColumn.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORColumn column = (DbORColumn) dbEnum.nextElement();
            writeColumn(column, writer, tableName, 0);
        } // end while
        dbEnum.close();

        // For each PK/UK
        relN = table.getComponents();
        dbEnum = relN.elements(DbORPrimaryUnique.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORPrimaryUnique puKey = (DbORPrimaryUnique) dbEnum.nextElement();
            writePuKey(puKey, writer, null, tableName);
        } // end while
        dbEnum.close();

        // For each index
        relN = table.getComponents();
        dbEnum = relN.elements(DbORIndex.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORIndex index = (DbORIndex) dbEnum.nextElement();
            writeIndex(index, writer, null, tableName);
        } // end while
        dbEnum.close();
    } // end writeTable()

    /**
     * <pre>
     * sr_occurrence_type column        //header
     *   name movie_number                //identifier
     *   tab_name MOVIE_STORE             //table ID
     *   type INTEGER                     //type name
     *   precision 10                     //optional length, for type == CHAR, etc.
     *   scale 10                         //optional scale (number of decimals), for type == FLOAT, etc.
     *   null_poss 0                      //0 if NULL, 1 if NOT NULL
     *   &lt;/br&gt;
     * </pre>
     */
    private static final String COLUMN_OCCURRENCE = "";

    protected void writeColumn(DbORColumn column, PrintWriter writer, String tableName, int colID)
            throws DbException {
        String colName = column.getPhysicalName();
        if ((colName == null) || (colName.equals("")))
            colName = column.getName();

        writer.println("sr_occurrence_type column");
        writer.println("name " + colName);
        writer.println("tab_name " + tableName);

        // get column type
        int recursivityLevel = 0;
        String typename = getTypeName(column.getType(), recursivityLevel);
        writer.println("type " + typename);

        // get column length
        int len;
        Integer iLen = column.getLength();
        if (iLen != null) {
            len = iLen.intValue();
        } else {
            recursivityLevel = 0;
            len = getTypeLength(column.getType(), recursivityLevel);
        } // end if

        if (len > 0) {
            writer.println("precision " + len);
        } // end if

        // get column scale
        int scale;
        Integer iScale = column.getNbDecimal();
        if (iScale != null) {
            scale = iScale.intValue();
        } else {
            recursivityLevel = 0;
            scale = getTypeScale(column.getType(), recursivityLevel);
        }

        if (scale > 0) {
            writer.println("scale " + scale);
        } // end if

        // is null possible
        String isNull = column.isNull() ? "1" : "0";
        writer.println("null_poss " + isNull);

        writer.println();
    } // end writeColumn()

    /**
     * <pre>
     * sr_occurrence_type comb_pk            //header
     *   tab_name MOVIE_COPY                   //table ID
     *   name Primary_Key                      //identifier
     *   col_name movie_copy_number            //column ID
     *   seq 1                                 //order of the column within the key
     *   &lt;/br&gt;
     *   one 'sr_occurrence_type comb_pk' per each column within the primary key.
     * </pre>
     */
    private static final String PKEY_OCCURRENCE = "";

    protected void writePuKey(DbORPrimaryUnique key, PrintWriter writer, String owner,
            String tableName) throws DbException {
        if (!key.isPrimary()) {
            return;
        }

        int seq = 0;
        DbRelationN relN = key.getColumns();
        DbEnumeration dbEnum = relN.elements(DbORColumn.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORColumn col = (DbORColumn) dbEnum.nextElement();
            writer.println("sr_occurrence_type comb_pk");
            writer.println("tab_name " + tableName);

            String name = key.getPhysicalName();
            if ((name == null) || (name.equals(""))) {
                name = key.getName();
            }
            writer.println("name " + name);

            String colname = col.getPhysicalName();
            if ((colname == null) || (colname.equals(""))) {
                colname = col.getName();
            }
            writer.println("col_name " + colname);
            writer.println("seq " + ++seq);

            writer.println();
        } // end while
        dbEnum.close();
    } // end writePuKey()

    /**
     * <pre>
     * sr_occurrence_type index      //header
     *   tab_name MOVIE_COPY           //table ID
     *   name XPKMOVIE_COPY            //identifier
     *   col_name movie_copy_number    //column ID
     *   sort DESC                     //optional field with value ASC or DESC
     *   seq 1                         //order of the column within the index
     *   &lt;/br&gt;
     *   one 'sr_occurrence_type comb_pk' per each column within the index.
     * </pre>
     */
    private static final String INDEX_OCCURRENCE = "";

    protected void writeIndex(DbORIndex index, PrintWriter writer, String owner, String tableName)
            throws DbException {
        int seq = 0;
        String name = index.getPhysicalName();
        if ((name == null) || (name.equals(""))) {
            name = index.getName();
        }

        DbRelationN relN = index.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORIndexKey.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORIndexKey key = (DbORIndexKey) dbEnum.nextElement();
            writer.println("sr_occurrence_type index");
            writer.println("tab_name " + tableName);
            writer.println("name " + name); // index name

            DbORColumn col = key.getIndexedElement();
            String colname = col.getPhysicalName();
            if ((colname == null) || (colname.equals(""))) {
                colname = col.getName();
            }
            writer.println("col_name " + colname);

            ORIndexKeySort sort = key.getSortOption();
            int value = (sort == null) ? ORIndexKeySort.ASC : sort.getValue();

            switch (value) {
            case ORIndexKeySort.ASC:
                writer.println("sort ASC");
                break;
            case ORIndexKeySort.DESC:
                writer.println("sort DESC");
                break;
            } // end switch()

            writer.println("seq " + ++seq);
            writer.println();
        } // end while
        dbEnum.close();
    } // end writeIndex()

    /**
     * <pre>
     * sr_occurrence_type comb_fk    //header
     *   tab_name MOVIE_STORE          //table ID
     *   name Foreign_Key2             //identifier
     *   col_name store_number         //column ID
     *   ptab_name STORE               //opposite table ID
     *   seq 1                         //order of the column within the foreign key
     *   &lt;/br&gt;
     *   one 'sr_occurrence_type comb_fk' per each column within the foreign key.
     * </pre>
     */
    private static final String FOREIGN_KEY_OCCURRENCE = "";

    protected void writeForeign(DbORForeign fKey, PrintWriter writer, String owner, String tableName)
            throws DbException {
        int seq = 0;
        String name = fKey.getPhysicalName();
        if ((name == null) || (name.equals(""))) {
            name = fKey.getName();
        }

        DbRelationN relN = fKey.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORFKeyColumn.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORFKeyColumn keyColumn = (DbORFKeyColumn) dbEnum.nextElement();
            DbORColumn col = keyColumn.getColumn();

            writer.println("sr_occurrence_type comb_fk");
            // owner name
            writer.println("tab_name " + tableName);
            writer.println("name " + name);

            String colName = col.getPhysicalName();
            if ((colName == null) || (colName.equals(""))) {
                colName = col.getName();
            }

            writer.println("col_name " + colName);

            // get opposite table name
            DbORAssociationEnd end = fKey.getAssociationEnd();
            DbORAssociationEnd oppEnd = end.getOppositeEnd();
            DbORAbsTable oppTable = oppEnd.getClassifier();
            String pName = oppTable.getPhysicalName();
            writer.println("ptab_name " + pName);

            writer.println("seq " + ++seq);
            writer.println("");

        } // end while
        dbEnum.close();
    } // end writeForeign()

}// end AnsiExtractFileWriter
