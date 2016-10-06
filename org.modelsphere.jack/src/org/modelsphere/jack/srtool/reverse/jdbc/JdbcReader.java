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

package org.modelsphere.jack.srtool.reverse.jdbc;

import java.sql.*;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.services.ConnectionService;

/*******************************************************************************
 * This service executes JDBC API calls
 * 
 * WARNING: Keep the call to the builder in the same order as the value jdbc column. The builder
 * count on this. 1- build occurrrence 2- jdbc column 1..2..3..4..5..6..etc
 ******************************************************************************/
public final class JdbcReader {
    // For debug use
    private static boolean PRINTINFO = false;

    private JdbcBuilder builder;
    private Connection connection;
    private DatabaseMetaData dbmd;

    public JdbcReader(JdbcBuilder aBuilder, int connectionId) throws SQLException {
        builder = aBuilder;
        connection = ConnectionService.getConnection(connectionId);
        dbmd = connection.getMetaData();
        Debug.trace("Driver version = " + dbmd.getDriverVersion());
        Debug.trace("Driver major version = " + dbmd.getDriverMajorVersion());
        Debug.trace("Driver minor version = " + dbmd.getDriverMinorVersion());
    }

    /*
     * Misc.
     */
    private void getMiscInfo() throws SQLException {
        String searchString = dbmd.getSearchStringEscape();
        String sqlKeywords = dbmd.getSQLKeywords();
        String userName = dbmd.getUserName();
        String dbURL = dbmd.getURL();
        printDebugInfo("Search String Escape = " + searchString); // NOT LOCALIZABLE
        printDebugInfo("SQL Keywords = " + sqlKeywords); // NOT LOCALIZABLE
        printDebugInfo("User Name = " + userName); // NOT LOCALIZABLE
        printDebugInfo("Database URL = " + dbURL); // NOT LOCALIZABLE
    }

    /*
     * JDBC Driver
     */
    private void getDriverInfo() throws SQLException {
        String drvName = dbmd.getDriverName();
        String drvVersion = dbmd.getDriverVersion();
        int drvMajorVersion = dbmd.getDriverMajorVersion();
        int drvMinorVersion = dbmd.getDriverMinorVersion();
        printDebugInfo("Driver Version = " + drvVersion); // NOT LOCALIZABLE
        printDebugInfo("Driver Name = " + drvName); // NOT LOCALIZABLE
        printDebugInfo("Driver Major Version = " + drvMajorVersion); // NOT LOCALIZABLE
        printDebugInfo("Driver Minor Version = " + drvMinorVersion); // NOT LOCALIZABLE
    }

    /*
     * Catalog (Database)
     */
    public void getCatalogInfo() throws SQLException, Exception {
        String catTerm = dbmd.getCatalogTerm();
        String catSep = dbmd.getCatalogSeparator();
        printDebugInfo("Catalog Term = " + catTerm); // NOT LOCALIZABLE
        printDebugInfo("Catalog Separator = " + catSep); // NOT LOCALIZABLE
        ResultSet rs = null;
        try {
            rs = dbmd.getCatalogs();
        } catch (Exception e) {
        };
        if (rs == null)
            return;

        while (rs.next()) {
            String name = getStringValue(rs, 1);
            builder.buildCatalogOccurrence();
            builder.buildCatalogName(name);
            printDebugInfo("Catalog Name = " + name); // NOT LOCALIZABLE
        }
    }

    /*
     * Schema (User)
     */
    public void getSchemaInfo() throws SQLException, Exception {
        String schemaTerm = dbmd.getSchemaTerm();
        // dbmd.getUserName()
        ResultSet rs = null;
        try {
            rs = dbmd.getSchemas();
        } catch (Exception e) {
        };
        if (rs == null)
            return;

        while (rs.next()) {
            String name = getStringValue(rs, 1);
            builder.buildSchemaOccurrence();
            builder.buildSchemaName(name);
            Debug.trace("Schema name = " + name); // NOT LOCALIZABLE
        }
    }

    /*
     * Table types
     */
    private void getTableTypeInfo() throws SQLException, Exception {
        ResultSet rsTabTypes = null;
        try {
            rsTabTypes = dbmd.getTableTypes();
        } catch (Exception e) {
        };
        if (rsTabTypes == null)
            return;

        while (rsTabTypes.next()) {
            String tabType = getStringValue(rsTabTypes, 1);
            builder.buildTableTypeName(tabType);
            printDebugInfo("Table Type = " + tabType); // NOT LOCALIZABLE
        }
    }

    /* Table (5) */
    public void getTableInfo(String aCatalog, String schemaPattern, String namePattern,
            String[] types) throws SQLException, Exception {
        Debug.trace("All table selectable = " + dbmd.allTablesAreSelectable());
        ResultSet rsTables = null;
        try {
            rsTables = dbmd.getTables(aCatalog, schemaPattern, namePattern, types);
        } catch (Exception e) {
        };
        if (rsTables == null)
            return;

        while (rsTables.next()) {
            String tableCatalog = getStringValue(rsTables, 1);
            String tableSchema = getStringValue(rsTables, 2);
            String tableName = getStringValue(rsTables, 3);
            String tableType = getStringValue(rsTables, 4);
            String tableRem = getStringValue(rsTables, 5);
            builder.buildTableOccurrence();
            builder.buildTableCatalog(tableCatalog);
            builder.buildTableSchema(tableSchema);
            builder.buildTableName(tableName);
            builder.buildTableType(tableType);
            builder.buildTableRemark(tableRem);
            printDebugInfo("Table Catalog = " + tableCatalog); // NOT LOCALIZABLE
            printDebugInfo("Table Schema = " + tableSchema); // NOT LOCALIZABLE
            printDebugInfo("Table Name = " + tableName); // NOT LOCALIZABLE
            printDebugInfo("Table Type = " + tableType); // NOT LOCALIZABLE
            printDebugInfo("Table Remark = " + tableRem); // NOT LOCALIZABLE
        }
    }

    /* View */
    public void getViewInfo(String aCatalog, String schemaPattern, String namePattern,
            String[] types) throws SQLException, Exception {
        Debug.trace("All view selectable = " + dbmd.allTablesAreSelectable());
        ResultSet rsTables = null;
        try {
            rsTables = dbmd.getTables(aCatalog, schemaPattern, namePattern, types);
        } catch (Exception e) {
        };
        if (rsTables == null)
            return;

        while (rsTables.next()) {
            String tableCatalog = getStringValue(rsTables, 1);
            String tableSchema = getStringValue(rsTables, 2);
            String tableName = getStringValue(rsTables, 3);
            String tableType = getStringValue(rsTables, 4);
            String tableRem = getStringValue(rsTables, 5);
            builder.buildViewOccurrence();
            builder.buildViewCatalog(tableCatalog);
            builder.buildViewSchema(tableSchema);
            builder.buildViewName(tableName);
            builder.buildViewType(tableType);
            builder.buildViewRemark(tableRem);
            printDebugInfo("View Catalog = " + tableCatalog); // NOT LOCALIZABLE
            printDebugInfo("View Schema = " + tableSchema); // NOT LOCALIZABLE
            printDebugInfo("View Name = " + tableName); // NOT LOCALIZABLE
            printDebugInfo("View Type = " + tableType); // NOT LOCALIZABLE
            printDebugInfo("View Remark = " + tableRem); // NOT LOCALIZABLE
        }
    }

    /* Table Columns (18) */
    public void getColumnInfo(String aCatalog, String schemaPattern, String tableNameParttern,
            String columnNamePattern) throws SQLException, Exception {
        ResultSet rsColumns = null;
        try {
            rsColumns = dbmd.getColumns(aCatalog, schemaPattern, tableNameParttern,
                    columnNamePattern);
        } catch (Exception e) {
        };
        if (rsColumns == null)
            return;

        while (rsColumns.next()) {
            String colCatalog = getStringValue(rsColumns, 1);
            String colSchema = getStringValue(rsColumns, 2);
            String colTableName = getStringValue(rsColumns, 3);
            String colName = getStringValue(rsColumns, 4);
            short colJavaSQLDataType = getShortValue(rsColumns, 5); // From java.sql.Types
            String colTypeName = getStringValue(rsColumns, 6);
            int colSize = getIntValue(rsColumns, 7);
            int colBufferLength = getIntValue(rsColumns, 8); // Do not use
            int colDecimals = getIntValue(rsColumns, 9);
            int colNbPrecRadix = getIntValue(rsColumns, 10);
            int colNull = getIntValue(rsColumns, 11); // 0=Not Null,1=Null,2=Unknown
            String colRem = getStringValue(rsColumns, 12);
            String colDefValue = getStringValue(rsColumns, 13);
            int colSQLDataType = getIntValue(rsColumns, 14); // Do Not Use
            int colSQLDatetimeSub = getIntValue(rsColumns, 15); // Do Not Use
            int colCharOctetMaxLength = getIntValue(rsColumns, 16);
            int colSequence = getIntValue(rsColumns, 17); // The order of the column in the table
            String colIsnull = getStringValue(rsColumns, 18);
            builder.buildTableColumnOccurrence();
            builder.buildTableColumnOwner(colSchema);
            builder.buildTableColumnTableName(colTableName);
            builder.buildTableColumnName(colName);
            builder.buildTableColumnType(colTypeName);
            builder.buildTableColumnSize(colSize);
            builder.buildTableColumnDecimals(colDecimals);
            builder.buildTableColumnSQLDataType(colJavaSQLDataType);
            builder.buildTableColumnNull(colNull);
            builder.buildTableColumnDefValue(colDefValue);
            builder.buildTableColumnRemark(colRem);
            printDebugInfo("Column Catalog = " + colCatalog); // NOT LOCALIZABLE
            printDebugInfo("Column Schema = " + colSchema); // NOT LOCALIZABLE
            printDebugInfo("Column Table = " + colTableName); // NOT LOCALIZABLE
            printDebugInfo("Column Name = " + colName); // NOT LOCALIZABLE
            printDebugInfo("Column SQL Data Type = " + colJavaSQLDataType); // NOT LOCALIZABLE
            printDebugInfo("Column Type = " + colTypeName); // NOT LOCALIZABLE
            printDebugInfo("Column Size = " + colSize); // NOT LOCALIZABLE
            printDebugInfo("Column Buffer = " + colBufferLength); // NOT LOCALIZABLE
            printDebugInfo("Column Decimals = " + colDecimals); // NOT LOCALIZABLE
            printDebugInfo("Column Prec Radix = " + colNbPrecRadix); // NOT LOCALIZABLE
            printDebugInfo("Column Null = " + colNull); // NOT LOCALIZABLE
            printDebugInfo("Column Remarks = " + colRem); // NOT LOCALIZABLE
            printDebugInfo("Column Default Value = " + colDefValue); // NOT LOCALIZABLE
            printDebugInfo("Column SQL Data Type2 = " + colSQLDataType); // NOT LOCALIZABLE
            printDebugInfo("Column DatetimeSub = " + colSQLDatetimeSub); // NOT LOCALIZABLE
            printDebugInfo("Column Octet Max Length = " + colCharOctetMaxLength);// NOT LOCALIZABLE
            printDebugInfo("Column Sequence = " + colSequence); // NOT LOCALIZABLE
            printDebugInfo("Column Null2 = " + colIsnull); // NOT LOCALIZABLE
        }
    }

    /* View Columns (18) */
    public void getViewColumnInfo(String aCatalog, String schemaPattern, String viewNameParttern,
            String columnNamePattern) throws SQLException, Exception {
        ResultSet rsColumns = null;
        try {
            rsColumns = dbmd.getColumns(aCatalog, schemaPattern, viewNameParttern,
                    columnNamePattern);
        } catch (Exception e) {
        };
        if (rsColumns == null)
            return;

        while (rsColumns.next()) {
            String colCatalog = getStringValue(rsColumns, 1);
            String colSchema = getStringValue(rsColumns, 2);
            String colTableName = getStringValue(rsColumns, 3);
            String colName = getStringValue(rsColumns, 4);
            short colJavaSQLDataType = getShortValue(rsColumns, 5); // From java.sql.Types
            String colTypeName = getStringValue(rsColumns, 6);
            int colSize = getIntValue(rsColumns, 7);
            int colBufferLength = getIntValue(rsColumns, 8); // Do not use
            int colDecimals = getIntValue(rsColumns, 9);
            int colNbPrecRadix = getIntValue(rsColumns, 10);
            int colNull = getIntValue(rsColumns, 11); // 0=Not Null,1=Null,2=Unknown
            String colRem = getStringValue(rsColumns, 12);
            String colDefValue = getStringValue(rsColumns, 13);
            int colSQLDataType = getIntValue(rsColumns, 14); // Do Not Use
            int colSQLDatetimeSub = getIntValue(rsColumns, 15); // Do Not Use
            int colCharOctetMaxLength = getIntValue(rsColumns, 16);
            int colSequence = getIntValue(rsColumns, 17); // The order of the column in the table
            String colIsnull = getStringValue(rsColumns, 18);
            builder.buildViewColumnOccurrence();
            builder.buildViewColumnOwner(colSchema);
            builder.buildViewColumnTableName(colTableName);
            builder.buildViewColumnName(colName);
            builder.buildViewColumnType(colTypeName);
            builder.buildViewColumnSize(colSize);
            builder.buildViewColumnDecimals(colDecimals);
            builder.buildViewColumnSQLDataType(colJavaSQLDataType);
            builder.buildViewColumnNull(colNull);
            builder.buildViewColumnDefValue(colDefValue);
            builder.buildViewColumnRemark(colRem);
            printDebugInfo("Column Catalog = " + colCatalog); // NOT LOCALIZABLE
            printDebugInfo("Column Schema = " + colSchema); // NOT LOCALIZABLE
            printDebugInfo("Column Table = " + colTableName); // NOT LOCALIZABLE
            printDebugInfo("Column Name = " + colName); // NOT LOCALIZABLE
            printDebugInfo("Column SQL Data Type = " + colJavaSQLDataType); // NOT LOCALIZABLE
            printDebugInfo("Column Type = " + colTypeName); // NOT LOCALIZABLE
            printDebugInfo("Column Size = " + colSize); // NOT LOCALIZABLE
            printDebugInfo("Column Buffer = " + colBufferLength); // NOT LOCALIZABLE
            printDebugInfo("Column Decimals = " + colDecimals); // NOT LOCALIZABLE
            printDebugInfo("Column Prec Radix = " + colNbPrecRadix); // NOT LOCALIZABLE
            printDebugInfo("Column Null = " + colNull); // NOT LOCALIZABLE
            printDebugInfo("Column Remarks = " + colRem); // NOT LOCALIZABLE
            printDebugInfo("Column Default Value = " + colDefValue); // NOT LOCALIZABLE
            printDebugInfo("Column SQL Data Type2 = " + colSQLDataType); // NOT LOCALIZABLE
            printDebugInfo("Column DatetimeSub = " + colSQLDatetimeSub); // NOT LOCALIZABLE
            printDebugInfo("Column Octet Max Length = " + colCharOctetMaxLength);// NOT LOCALIZABLE
            printDebugInfo("Column Sequence = " + colSequence); // NOT LOCALIZABLE
            printDebugInfo("Column Null2 = " + colIsnull); // NOT LOCALIZABLE
        }
    }

    /* Primary Keys (6) */

    public void getPrimaryKeyInfo(String aCatalog, String aSchema, String aTable)
            throws SQLException, Exception {
        ResultSet rsPK = null;
        try {
            rsPK = dbmd.getPrimaryKeys(aCatalog, aSchema, aTable);
        } catch (Exception e) {
        };
        if (rsPK == null)
            return;

        while (rsPK.next()) {
            String pkTableCatalog = getStringValue(rsPK, 1);
            String pkTableSchema = getStringValue(rsPK, 2);
            String pkTableName = getStringValue(rsPK, 3);
            String pkColumnName = getStringValue(rsPK, 4);
            short pkKeySequence = getShortValue(rsPK, 5);
            String pkName = getStringValue(rsPK, 6);
            builder.buildPKOccurrence();
            builder.buildPKTableCatalog(pkTableCatalog);
            builder.buildPKTableSchema(pkTableSchema);
            builder.buildPKTableName(pkTableName);
            builder.buildPKColumnName(pkColumnName);
            builder.buildPKKeySequence(pkKeySequence);
            builder.buildPKName(pkName);
            printDebugInfo("PK Table Catalog = " + pkTableCatalog); // NOT LOCALIZABLE
            printDebugInfo("PK Table Schema = " + pkTableSchema); // NOT LOCALIZABLE
            printDebugInfo("PK Table Name = " + pkTableName); // NOT LOCALIZABLE
            printDebugInfo("PK Column Name = " + pkColumnName); // NOT LOCALIZABLE
            printDebugInfo("PK Key Sequence = " + pkKeySequence); // NOT LOCALIZABLE
            printDebugInfo("PK Name = " + pkName); // NOT LOCALIZABLE
        }
    }

    /* Indexes (13) */
    public void getIndexInfo(String aCatalog, String aSchema, String aTable, boolean unique,
            boolean approximate) throws SQLException, Exception {
        ResultSet rsIndexes = null;
        try {
            rsIndexes = dbmd.getIndexInfo(aCatalog, aSchema, aTable, unique, approximate);
        } catch (Exception e) {
        };
        if (rsIndexes == null)
            return;

        while (rsIndexes.next()) {
            String indexTableCatalog = getStringValue(rsIndexes, 1);
            String indexTableSchema = getStringValue(rsIndexes, 2);
            String indexTableName = getStringValue(rsIndexes, 3);
            String indexNonUnique = getStringValue(rsIndexes, 4);
            String indexQualifier = getStringValue(rsIndexes, 5);
            String indexName = getStringValue(rsIndexes, 6);
            String indexType = getStringValue(rsIndexes, 7); // 0-Statistic,1-Clustered,2-Hash,3-Other
            String indexOrdinalPosition = getStringValue(rsIndexes, 8);
            String indexColumnName = getStringValue(rsIndexes, 9);
            String indexAscDesc = getStringValue(rsIndexes, 10); // 1-A,2-D
            int indexCardinality = getIntValue(rsIndexes, 11);
            int indexPages = getIntValue(rsIndexes, 12);
            String indexFilter = getStringValue(rsIndexes, 13);
            builder.buildIndexOccurrence();
            builder.buildIndexTableCatalog(indexTableCatalog);
            builder.buildIndexTableSchema(indexTableSchema);
            builder.buildIndexTableName(indexTableName);
            builder.buildIndexNonUnique(indexNonUnique);
            builder.buildIndexQualifier(indexQualifier);
            builder.buildIndexName(indexName);
            builder.buildIndexType(indexType);
            builder.buildIndexOrdinalPosition(indexOrdinalPosition);
            builder.buildIndexColumnName(indexColumnName);
            builder.buildIndexAscDesc(indexAscDesc);
            builder.buildIndexCardinality(indexCardinality);
            builder.buildIndexPages(indexPages);
            builder.buildIndexFilter(indexFilter);
            printDebugInfo("Index Table Catalog = " + indexTableCatalog); // NOT LOCALIZABLE
            printDebugInfo("Index Table Schema = " + indexTableSchema); // NOT LOCALIZABLE
            printDebugInfo("Index Table Name = " + indexTableName); // NOT LOCALIZABLE
            printDebugInfo("Index Non Unique = " + indexNonUnique); // NOT LOCALIZABLE
            printDebugInfo("Index Qualifier = " + indexQualifier); // NOT LOCALIZABLE
            printDebugInfo("Index Name = " + indexName); // NOT LOCALIZABLE
            printDebugInfo("Index Type = " + indexType); // NOT LOCALIZABLE
            printDebugInfo("Index Ordinal Position = " + indexOrdinalPosition); // NOT LOCALIZABLE
            printDebugInfo("Index Column Name = " + indexColumnName); // NOT LOCALIZABLE
            printDebugInfo("Index Asc-Desc = " + indexAscDesc); // NOT LOCALIZABLE
            printDebugInfo("Index Cardinality = " + indexCardinality); // NOT LOCALIZABLE
            printDebugInfo("Index Pages = " + indexPages); // NOT LOCALIZABLE
            printDebugInfo("Index Filter = " + indexFilter); // NOT LOCALIZABLE
        }
    }

    /* Foreign Keys (14) */
    public void getForeignKeyInfo(String aCatalog, String aSchema, String aTable)
            throws SQLException, Exception {
        ResultSet rsFKs = null;
        try {
            rsFKs = dbmd.getImportedKeys(aCatalog, aSchema, aTable);
        } catch (Exception e) {
        };
        if (rsFKs == null)
            return;

        while (rsFKs.next()) {
            String fkPKTableCatalog = getStringValue(rsFKs, 1);
            String fkPKTableSchema = getStringValue(rsFKs, 2);
            String fkPKTableName = getStringValue(rsFKs, 3);
            String fkPKColumnName = getStringValue(rsFKs, 4);
            String fkTableCatalog = getStringValue(rsFKs, 5);
            String fkTableSchema = getStringValue(rsFKs, 6);
            String fkTableName = getStringValue(rsFKs, 7);
            String fkColumnName = getStringValue(rsFKs, 8);
            String fkKeySequence = getStringValue(rsFKs, 9); // There is a ";1"
            // at the end!
            String fkUpdateRule = getStringValue(rsFKs, 10); // 1-NoAction,2-Cascade,3-SetNull,4-SetDefault,5-Restrict
            // (1=5)
            String fkDeleteRule = getStringValue(rsFKs, 11); // 1-NoAction,2-Cascade,3-SetNull,4-SetDefault,5-Restrict
            // (1=5)
            String fkName = getStringValue(rsFKs, 12);
            String fkPKName = getStringValue(rsFKs, 13);
            String fkDeferrability = getStringValue(rsFKs, 14); // 1-InitiallyDeferred,2-InitiallyImmediate,3-NotDefferable
            builder.buildFKOccurrence();
            builder.buildFKPKTableCatalog(fkPKTableCatalog);
            builder.buildFKPKTableSchema(fkPKTableSchema);
            builder.buildFKPKTableName(fkPKTableName);
            builder.buildFKPKColumnName(fkPKColumnName);
            builder.buildFKTableCatalog(fkTableCatalog);
            builder.buildFKTableSchema(fkTableSchema);
            builder.buildFKTableName(fkTableName);
            builder.buildFKColumnName(fkColumnName);
            builder.buildFKSequence(fkKeySequence);
            builder.buildFKUpdateRule(fkUpdateRule);
            builder.buildFKDeleteRule(fkDeleteRule);
            builder.buildFKName(fkName);
            builder.buildFKPKName(fkPKName);
            builder.buildFKDefferrability(fkDeferrability);
            printDebugInfo("FK Table Catalog = " + fkPKTableCatalog); // NOT LOCALIZABLE
            printDebugInfo("FK Table Schema = " + fkPKTableSchema); // NOT LOCALIZABLE
            printDebugInfo("FK Table Name = " + fkPKTableName); // NOT LOCALIZABLE
            printDebugInfo("FK PK Column Name = " + fkPKColumnName); // NOT LOCALIZABLE
            printDebugInfo("FK Table Catalog = " + fkTableCatalog); // NOT LOCALIZABLE
            printDebugInfo("FK Table Schema = " + fkTableSchema); // NOT LOCALIZABLE
            printDebugInfo("FK Table Name = " + fkTableName); // NOT LOCALIZABLE
            printDebugInfo("FK Column Name = " + fkColumnName); // NOT LOCALIZABLE
            printDebugInfo("FK Key Sequence = " + fkKeySequence); // NOT LOCALIZABLE
            printDebugInfo("FK Update Rule = " + fkUpdateRule); // NOT LOCALIZABLE
            printDebugInfo("FK Delete Rule = " + fkDeleteRule); // NOT LOCALIZABLE
            printDebugInfo("FK Name = " + fkName); // NOT LOCALIZABLE
            printDebugInfo("FK PK Name = " + fkPKName); // NOT LOCALIZABLE
            printDebugInfo("FK Deferrability = " + fkDeferrability); // NOT LOCALIZABLE
        }
    }

    /* Procedures */
    public void getProcedureInfo(String aCatalog, String aSchema, String aProc)
            throws SQLException, Exception {
        String procedureTerm = dbmd.getProcedureTerm();
        printDebugInfo("Procedure Term = " + procedureTerm); // NOT LOCALIZABLE
        ResultSet rsProcedures = null;
        try {
            rsProcedures = dbmd.getProcedures(aCatalog, aSchema, aProc);
        } catch (Exception e) {
        };
        if (rsProcedures == null)
            return;

        while (rsProcedures.next()) {
            String tokenProcCatalog = getStringValue(rsProcedures, 1);
            String tokenProcSchema = getStringValue(rsProcedures, 2);
            String tokenProcName = getStringValue(rsProcedures, 3);
            String tokenProcReserved1 = getStringValue(rsProcedures, 4); // Not used
            String tokenProcReserved2 = getStringValue(rsProcedures, 5); // Not used
            String tokenProcReserved3 = getStringValue(rsProcedures, 6); // Not used
            String tokenProcRem = getStringValue(rsProcedures, 7);
            String tokenProcType = getStringValue(rsProcedures, 8);
            builder.buildProcOccurrence();
            builder.buildProcCatalog(tokenProcCatalog);
            builder.buildProcSchema(tokenProcSchema);
            builder.buildProcName(tokenProcName);
            builder.buildProcReserved1(tokenProcReserved1);
            builder.buildProcReserved2(tokenProcReserved2);
            builder.buildProcReserved3(tokenProcReserved3);
            builder.buildProcRemark(tokenProcRem);
            builder.buildProcType(tokenProcType);
            printDebugInfo("Procedure Catalog = " + tokenProcCatalog); // NOT LOCALIZABLE
            printDebugInfo("Procedure Schema = " + tokenProcSchema); // NOT LOCALIZABLE
            printDebugInfo("Procedure Name = " + tokenProcName); // NOT LOCALIZABLE
            printDebugInfo("Procedure Reserved 1 = " + tokenProcReserved1); // NOT LOCALIZABLE
            printDebugInfo("Procedure Reserved 2 = " + tokenProcReserved2); // NOT LOCALIZABLE
            printDebugInfo("Procedure Reserved 3 = " + tokenProcReserved3); // NOT LOCALIZABLE
            printDebugInfo("Procedure Remarks = " + tokenProcRem); // NOT LOCALIZABLE
            printDebugInfo("Procedure Type = " + tokenProcType); // NOT LOCALIZABLE
        }
    }

    /* Procedures Columns */
    public void getProcedureColumns(String aCatalog, String aSchema, String aProc, String aColumn)
            throws SQLException, Exception {
        ResultSet rsProceduresCols = null;
        try {
            rsProceduresCols = dbmd.getProcedureColumns(aCatalog, aSchema, aProc, aColumn);
        } catch (Exception e) {
        };
        if (rsProceduresCols == null)
            return;

        while (rsProceduresCols.next()) {
            String procCatalog = getStringValue(rsProceduresCols, 1);
            String procSchema = getStringValue(rsProceduresCols, 2);
            String procName = getStringValue(rsProceduresCols, 3);
            String procColName = getStringValue(rsProceduresCols, 4);
            short procColType = getShortValue(rsProceduresCols, 5);
            short procColSQLDataType = getShortValue(rsProceduresCols, 6);
            String procColTypeName = getStringValue(rsProceduresCols, 7);
            int procColPrecision = getIntValue(rsProceduresCols, 8);
            int procColLength = getIntValue(rsProceduresCols, 9);
            short procColScale = getShortValue(rsProceduresCols, 10);
            int procColRadix = getIntValue(rsProceduresCols, 11);
            short procColNull = getShortValue(rsProceduresCols, 12);
            String procColRemarks = getStringValue(rsProceduresCols, 13);
            builder.buildProcColOccurrence();
            builder.buildProcColProcCatalog(procCatalog);
            builder.buildProcColProcSchema(procSchema);
            builder.buildProcColProcName(procName);
            builder.buildProcColName(procColName);
            builder.buildProcColType(procColType);
            builder.buildProcColSQLType(procColSQLDataType);
            builder.buildProcColDataType(procColTypeName);
            builder.buildProcColPrec(procColPrecision);
            builder.buildProcColLength(procColLength);
            builder.buildProcColScale(procColScale);
            builder.buildProcColRadix(procColRadix);
            builder.buildProcColNull(procColNull);
            builder.buildProcColRemark(procColRemarks);
            printDebugInfo("Procedure Catalog = " + procCatalog); // NOT LOCALIZABLE
            printDebugInfo("Procedure Schema = " + procSchema); // NOT LOCALIZABLE
            printDebugInfo("Procedure Name = " + procName); // NOT LOCALIZABLE
            printDebugInfo("Procedure Column Name = " + procColName); // NOT LOCALIZABLE
            printDebugInfo("Procedure Column Type = " + procColType); // NOT LOCALIZABLE
            printDebugInfo("Procedure Column SQL Type = " + procColSQLDataType); // NOT LOCALIZABLE
            printDebugInfo("Procedure Column Data Type = " + procColTypeName);// NOT LOCALIZABLE
            printDebugInfo("Procedure Column Precision = " + procColPrecision); // NOT LOCALIZABLE
            printDebugInfo("Procedure Column Length = " + procColLength); // NOT LOCALIZABLE
            printDebugInfo("Procedure Column Scale = " + procColScale); // NOT LOCALIZABLE
            printDebugInfo("Procedure Column Radix = " + procColRadix); // NOT LOCALIZABLE
            printDebugInfo("Procedure Column Null = " + procColNull); // NOT LOCALIZABLE
            printDebugInfo("Procedure Column Remark = " + procColRemarks); // NOT LOCALIZABLE
        }
    }

    /* Type Information */
    public void getTypeInfo() throws SQLException, Exception {
        ResultSet rsTypes = null;
        try {
            rsTypes = dbmd.getTypeInfo();
        } catch (Exception e) {
        };
        if (rsTypes == null)
            return;

        while (rsTypes.next()) {
            String typeName = getStringValue(rsTypes, 1);
            short dataType = getShortValue(rsTypes, 2); // java.sql.Types
            int precision = getIntValue(rsTypes, 3);
            String prefix = getStringValue(rsTypes, 4);
            String suffix = getStringValue(rsTypes, 5);
            String createParams = getStringValue(rsTypes, 6);
            int nullable = getIntValue(rsTypes, 7);
            String caseSensitive = getStringValue(rsTypes, 8);
            short searchable = getShortValue(rsTypes, 9);
            String unsignedAttr = getStringValue(rsTypes, 10);
            String fixedPrecScale = getStringValue(rsTypes, 11);
            String autoIncrement = getStringValue(rsTypes, 12);
            String localTypeName = getStringValue(rsTypes, 13);
            short minScale = getShortValue(rsTypes, 14);
            short maxScale = getShortValue(rsTypes, 15);
            int sqlDataType = getIntValue(rsTypes, 16);
            int sqlDatatimeSub = getIntValue(rsTypes, 17);
            int precRadix = getIntValue(rsTypes, 18);
            builder.buildTypeOccurrence();
            builder.buildTypeName(typeName);
            builder.buildTypeDataType(dataType);
            builder.buildTypePrecision(precision);
            builder.buildTypePrefix(prefix);
            builder.buildTypeSuffix(suffix);
            builder.buildTypeCreateParams(createParams);
            builder.buildTypeNullable(nullable);
            builder.buildTypeCaseSensitive(caseSensitive);
            builder.buildTypeSearchable(searchable);
            builder.buildTypeUnsignedAttr(unsignedAttr);
            builder.buildTypeFixedPrecScale(fixedPrecScale);
            builder.buildTypeAutoIncrement(autoIncrement);
            builder.buildTypeLocalTypeName(localTypeName);
            builder.buildTypeMinScale(minScale);
            builder.buildTypeMaxScale(maxScale);
            builder.buildTypeSQLDataType(sqlDataType);
            builder.buildTypeSQLDatetimeSub(sqlDatatimeSub);
            builder.buildTypePrecRadix(precRadix);
            printDebugInfo("Type Name = " + typeName); // NOT LOCALIZABLE
            printDebugInfo("Type Data Type = " + dataType); // NOT LOCALIZABLE
            printDebugInfo("Type Precision = " + precision); // NOT LOCALIZABLE
            printDebugInfo("Type Prefix = " + prefix); // NOT LOCALIZABLE
            printDebugInfo("Type Suffix = " + suffix); // NOT LOCALIZABLE
            printDebugInfo("Type Create Params = " + createParams); // NOT LOCALIZABLE
            printDebugInfo("Type Nullable = " + nullable); // NOT LOCALIZABLE
            printDebugInfo("Type Case Sensitive = " + caseSensitive); // NOT LOCALIZABLE
            printDebugInfo("Type Searchable = " + searchable); // NOT LOCALIZABLE
            printDebugInfo("Type Unsigned Attr = " + unsignedAttr); // NOT LOCALIZABLE
            printDebugInfo("Type Fixed Prec Scale = " + fixedPrecScale); // NOT LOCALIZABLE
            printDebugInfo("Type Auto Increment = " + autoIncrement); // NOT LOCALIZABLE
            printDebugInfo("Type Local Type Name = " + localTypeName); // NOT LOCALIZABLE
            printDebugInfo("Type Min Scale = " + minScale); // NOT LOCALIZABLE
            printDebugInfo("Type Max Scale = " + maxScale); // NOT LOCALIZABLE
            printDebugInfo("Type SQL Data Type = " + sqlDataType); // NOT LOCALIZABLE
            printDebugInfo("Type SQL Datatime Sub = " + sqlDatatimeSub); // NOT LOCALIZABLE
            printDebugInfo("Type Prec Radix = " + precRadix); // NOT LOCALIZABLE
        }
    }

    /***************
     * JDBC 2.0 API *
     ***************/

    /* User-Defined Types */
    public void getUDTInfo(String aCatalog, String aSchema, String aName, int[] types)
            throws SQLException, Exception {
        // Class myClass = dbmd.getClass();
        // Method mth = myClass.getMethod("getUDTs",null); //NOT LOCALIZABLE
        ResultSet rsUDTs = null;
        try {
            rsUDTs = dbmd.getUDTs(aCatalog, aSchema, aName, types);
        } catch (Exception e) {
        };

        if (rsUDTs == null)
            return;
        while (rsUDTs.next()) {
            String uDTCatalog = getStringValue(rsUDTs, 1);
            String uDTSchema = getStringValue(rsUDTs, 2);
            String uDTName = getStringValue(rsUDTs, 3); // There is a ";1" at
            // the end!
            String uDTClassName = getStringValue(rsUDTs, 4);
            short uDTType = getShortValue(rsUDTs, 5);
            String uDTRem = getStringValue(rsUDTs, 6);
            builder.buildUDTOccurrence();
            builder.buildUDTCatalog(uDTCatalog);
            builder.buildUDTSchema(uDTSchema);
            builder.buildUDTName(uDTName);
            builder.buildUDTClassName(uDTClassName);
            builder.buildUDTType(uDTType);
            builder.buildUDTRemark(uDTRem);
            printDebugInfo("UDT Catalog = " + uDTCatalog); // NOT LOCALIZABLE
            printDebugInfo("UDT Schema = " + uDTSchema); // NOT LOCALIZABLE
            printDebugInfo("UDT Name = " + uDTName); // NOT LOCALIZABLE
            printDebugInfo("UDT Class Name = " + uDTClassName); // NOT LOCALIZABLE
            printDebugInfo("UDT Type = " + uDTType); // NOT LOCALIZABLE
            printDebugInfo("UDT Remarks = " + uDTRem); // NOT LOCALIZABLE
        }
    }

    /************
     * Utilities *
     ************/
    private void printDebugInfo(String output) {
        if (PRINTINFO)
            Debug.trace(output);
    }

    private String getStringValue(ResultSet rs, int index) {
        try {
            return rs.getString(index);
        } catch (Exception e) {
        };
        return null;
    }

    private int getIntValue(ResultSet rs, int index) {
        try {
            return rs.getInt(index);
        } catch (Exception e) {
        };
        return 0;
    }

    private short getShortValue(ResultSet rs, int index) {
        try {
            return rs.getShort(index);
        } catch (Exception e) {
        };
        return 0;
    }

    /*****************
     * Helper Classes *
     *****************/
    /*
     * private class TableId{ private String tabCatalogue = null; private String tabSchema = null;
     * private String tabName = null;
     * 
     * public TableId(String aCatalog, String aSchema, String aName){ tabCatalogue = aCatalog;
     * tabSchema = aSchema; tabName = aName; } public String getTabCatalog(){return tabCatalogue;}
     * public String getTabSchema(){return tabSchema;} public String getTabName(){return tabName;} }
     */
}
