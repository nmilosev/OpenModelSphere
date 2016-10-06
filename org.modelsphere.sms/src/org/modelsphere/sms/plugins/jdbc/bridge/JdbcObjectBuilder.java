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

package org.modelsphere.sms.plugins.jdbc.bridge;

import org.modelsphere.jack.srtool.reverse.Actions;
import org.modelsphere.jack.srtool.reverse.jdbc.JdbcBuilder;

/**
 * This service executes JDBC API calls
 **/
public final class JdbcObjectBuilder extends JdbcBuilder {

    private Actions actions;

    public JdbcObjectBuilder(Actions someActions) {
        actions = someActions;
    }

    /******************/
    // User Occurrence
    protected void buildSchemaOccurrence() throws Exception {
        actions.processOccurrenceId(-1, JdbcReverseToolkitPlugin.USER);
    };

    // User Name
    protected void buildSchemaName(String schemaName) throws Exception {
        actions.setAttribute("name", schemaName);
    };

    /*******************/
    // Table Occurrence
    protected void buildTableOccurrence() throws Exception {
        actions.processOccurrenceId(-1, JdbcReverseToolkitPlugin.TABLE);
    }

    // Table Owner
    protected void buildTableSchema(String schemaName) throws Exception {
        actions.setAttribute("owner", schemaName);
    };

    // Table Name
    protected void buildTableName(String tableName) throws Exception {
        actions.setAttribute("name", tableName);
    };

    // Table Comment
    protected void buildTableRemark(String tableRemark) throws Exception {
        actions.setAttribute("comments", tableRemark);
    };

    /*******************/
    // View Occurrence
    protected void buildViewOccurrence() throws Exception {
        actions.processOccurrenceId(-1, JdbcReverseToolkitPlugin.VIEW);
    }

    // View Owner
    protected void buildViewSchema(String schemaName) throws Exception {
        actions.setAttribute("owner", schemaName);
    };

    // View Name
    protected void buildViewName(String viewName) throws Exception {
        actions.setAttribute("name", viewName);
    };

    // View Comment
    protected void buildViewRemark(String viewRemark) throws Exception {
        actions.setAttribute("comments", viewRemark);
    };

    /*************************/
    // Table Column Occurrence
    protected void buildTableColumnOccurrence() throws Exception {
        actions.processOccurrenceId(-1, JdbcReverseToolkitPlugin.COLUMN);
    };

    // Table Column Owner
    protected void buildTableColumnOwner(String owner) throws Exception {
        actions.setAttribute("owner", owner);
    };

    // Table Column Table Name
    protected void buildTableColumnTableName(String tableName) throws Exception {
        actions.setAttribute("table_name", tableName);
    };

    // Table Column Name
    protected void buildTableColumnName(String columnName) throws Exception {
        actions.setAttribute("name", columnName);
    };

    // Table Column Type
    protected void buildTableColumnType(String typeName) throws Exception {
        actions.setAttribute("type", typeName);
    };

    // Table Column Size
    protected void buildTableColumnSize(int size) throws Exception {
        actions.setAttribute("size", new Integer(size).toString());
    };

    // Table Column Decimals
    protected void buildTableColumnDecimals(int decimals) throws Exception {
        actions.setAttribute("decimals", new Integer(decimals).toString());
    };

    // Table Column SQL Data Type
    protected void buildTableColumnSQLDataType(short colJavaSQLDataType) throws Exception {
        actions.setAttribute("sql_type", new Short(colJavaSQLDataType).toString());
    };

    // Table Column Null
    protected void buildTableColumnNull(int colNull) throws Exception {
        actions.setAttribute("null", new Integer(colNull).toString());
    };

    // Table Column Default Value
    protected void buildTableColumnDefValue(String defaultValue) throws Exception {
        actions.setAttribute("default_value", defaultValue);
    };

    // Table Column Comment
    protected void buildTableColumnRemark(String columnRemark) throws Exception {
        actions.setAttribute("comments", columnRemark);
    };

    /*************************/
    // View Column Occurrence
    protected void buildViewColumnOccurrence() throws Exception {
        actions.processOccurrenceId(-1, JdbcReverseToolkitPlugin.VIEW_COLUMN);
    };

    // View Column Owner
    protected void buildViewColumnOwner(String owner) throws Exception {
        actions.setAttribute("owner", owner);
    };

    // View Column View Name
    protected void buildViewColumnTableName(String viewName) throws Exception {
        actions.setAttribute("table_name", viewName);
    };

    // View Column Name
    protected void buildViewColumnName(String columnName) throws Exception {
        actions.setAttribute("name", columnName);
    };

    // View Column Type
    protected void buildViewColumnType(String typeName) throws Exception {
        actions.setAttribute("type", typeName);
    };

    // View Column Size
    protected void buildViewColumnSize(int size) throws Exception {
        actions.setAttribute("size", new Integer(size).toString());
    };

    // View Column Decimals
    protected void buildViewColumnDecimals(int decimals) throws Exception {
        actions.setAttribute("decimals", new Integer(decimals).toString());
    };

    // View Column SQL Data Type
    protected void buildViewColumnSQLDataType(short colJavaSQLDataType) throws Exception {
        actions.setAttribute("sql_type", new Short(colJavaSQLDataType).toString());
    };

    // View Column Null
    protected void buildViewColumnNull(int colNull) throws Exception {
        actions.setAttribute("null", new Integer(colNull).toString());
    };

    // View Column Default Value
    protected void buildViewColumnDefValue(String defaultValue) throws Exception {
        actions.setAttribute("default_value", defaultValue);
    };

    // View Column Comment
    protected void buildViewColumnRemark(String columnRemark) throws Exception {
        actions.setAttribute("comments", columnRemark);
    };

    /*************************/
    // Primary Key Occurrence
    protected void buildPKOccurrence() throws Exception {
        actions.processOccurrenceId(-1, JdbcReverseToolkitPlugin.PK);
    };

    // Prinary Key Table Owner
    protected void buildPKTableSchema(String pkTableSchema) throws Exception {
        actions.setAttribute("owner", pkTableSchema);
    };

    // Primary Key Table Name
    protected void buildPKTableName(String pkTableName) throws Exception {
        actions.setAttribute("table_name", pkTableName);
    };

    // Primary Key Column Name
    protected void buildPKColumnName(String pkColumnName) throws Exception {
        actions.setAttribute("column_name", pkColumnName);
    };

    // Primary Key Name
    protected void buildPKName(String pkName) throws Exception {
        actions.setAttribute("name", pkName);
    };

    /*************************/
    // Foreign Key Occurrence
    protected void buildFKOccurrence() throws Exception {
        actions.processOccurrenceId(-1, JdbcReverseToolkitPlugin.FK);
    };

    // Foreign Key Primary Key Table Owner
    protected void buildFKPKTableSchema(String fkPKTableSchema) throws Exception {
        actions.setAttribute("owner2", fkPKTableSchema);
    };

    // Foreign Key Primary Key Table
    protected void buildFKPKTableName(String fkPKTableName) throws Exception {
        actions.setAttribute("fk_pk_table", fkPKTableName);
    };

    // Foreign Key Primary Key Column
    protected void buildFKPKColumnName(String fkPKColumnName) throws Exception {
        actions.setAttribute("fk_pk_column", fkPKColumnName);
    };

    // Foreign Key Table Owner
    protected void buildFKTableSchema(String fkTableSchema) throws Exception {
        actions.setAttribute("owner", fkTableSchema);
    };

    // Foreign Key Table
    protected void buildFKTableName(String fkTableName) throws Exception {
        actions.setAttribute("fk_table", fkTableName);
    };

    // Foreign Key Column
    protected void buildFKColumnName(String fkColumnName) throws Exception {
        actions.setAttribute("fk_column", fkColumnName);
    };

    // Foreign Key Update Rule
    protected void buildFKUpdateRule(String fkUpdateRule) throws Exception {
        actions.setAttribute("upd_rule", fkUpdateRule);
    };

    // Foreign Key Delete Rule
    protected void buildFKDeleteRule(String fkDeleteRule) throws Exception {
        actions.setAttribute("del_rule", fkDeleteRule);
    };

    // Foreign Key Name
    protected void buildFKName(String fkName) throws Exception {
        actions.setAttribute("name", fkName);
    };

    // Foreign Key Primary Key Name
    protected void buildFKPKName(String fkPKName) throws Exception {
        actions.setAttribute("fk_pk_name", fkPKName);
    };

    /********/
    // Index Occurrence
    protected void buildIndexOccurrence() throws Exception {
        actions.processOccurrenceId(-1, JdbcReverseToolkitPlugin.INDEX);
    };

    // Index Table Owner
    protected void buildIndexTableSchema(String indexTableSchema) throws Exception {
        actions.setAttribute("owner", indexTableSchema);
    };

    // Index Table Name
    protected void buildIndexTableName(String indexTableName) throws Exception {
        actions.setAttribute("table_name", indexTableName);
    };

    // Index Unique
    protected void buildIndexNonUnique(String indexNonUnique) throws Exception {
        actions.setAttribute("unique", indexNonUnique);
    };

    // Index Name
    protected void buildIndexName(String indexName) throws Exception {
        actions.setAttribute("name", indexName);
    };

    // Index Column Name
    protected void buildIndexColumnName(String indexColumnName) throws Exception {
        actions.setAttribute("column", indexColumnName);
    };

    // Index Column Asc/Desc
    protected void buildIndexAscDesc(String indexAscDesc) throws Exception {
        actions.setAttribute("sort", indexAscDesc);
    };

    /**********************/
    // Procedure Occurrence
    protected void buildProcOccurrence() throws Exception {
        actions.processOccurrenceId(-1, JdbcReverseToolkitPlugin.PROC);
    };

    // Procedure Owner
    protected void buildProcSchema(String tokenProcSchema) throws Exception {
        actions.setAttribute("owner", tokenProcSchema);
    };

    // Procedure Name
    protected void buildProcName(String tokenProcName) throws Exception {
        actions.setAttribute("name", tokenProcName);
    };

    // Procedure Comments
    protected void buildProcRemark(String tokenProcRem) throws Exception {
        actions.setAttribute("comments", tokenProcRem);
    };

    // Procedure Type
    protected void buildProcType(String tokenProcType) throws Exception {
        actions.setAttribute("type", tokenProcType);
    };

    /*****************************/
    // Procedure Column Occurrence
    protected void buildProcColOccurrence() throws Exception {
        actions.processOccurrenceId(-1, JdbcReverseToolkitPlugin.PROCCOL);
    };

    // Procedure Column Procedure Owner
    protected void buildProcColProcSchema(String procSchema) throws Exception {
        actions.setAttribute("owner", procSchema);
    };

    // Procedure Column Procedure Name
    protected void buildProcColProcName(String procName) throws Exception {
        actions.setAttribute("proc_name", procName);
    };

    // Procedure Column Name
    protected void buildProcColName(String procColName) throws Exception {
        actions.setAttribute("name", procColName);
    };

    // Procedure Column Type (category)
    protected void buildProcColType(short procColType) throws Exception {
        actions.setAttribute("type", new Short(procColType).toString());
    };

    // Procedure Column SQL Type
    protected void buildProcColSQLType(short procColSQLDataType) throws Exception {
        actions.setAttribute("sql_type", new Short(procColSQLDataType).toString());
    };

    // Procedure Column Data Type
    protected void buildProcColDataType(String procColTypeName) throws Exception {
        actions.setAttribute("type_name", procColTypeName);
    };

    // Procedure Column Length
    protected void buildProcColPrec(int procColPrecision) throws Exception {
        actions.setAttribute("length", new Integer(procColPrecision).toString());
    };

    // Procedure Column Nb Decimals
    protected void buildProcColScale(short procColScale) throws Exception {
        actions.setAttribute("nb_decimals", new Short(procColScale).toString());
    };

    // Procedure Column Comments
    protected void buildProcColRemark(String procColRemarks) throws Exception {
        actions.setAttribute("comments", procColRemarks);
    };

    /*****************/
    // Type Occurrence
    protected void buildTypeOccurrence() throws Exception {
        actions.processOccurrenceId(-1, JdbcReverseToolkitPlugin.TYPE);
    };

    // Type Name
    protected void buildTypeName(String typeName) throws Exception {
        actions.setAttribute("name", typeName);
    };

    // Type SQL Data Type (java.sql.Types)
    protected void buildTypeDataType(short dataType) throws Exception {
        actions.setAttribute("sql_type", new Short(dataType).toString());
    };

    // Type Length
    protected void buildTypePrecision(int precision) throws Exception {
        actions.setAttribute("length", new Integer(precision).toString());
    };

    // Type Nullable
    protected void buildTypeNullable(int nullable) throws Exception {
        actions.setAttribute("nullable", new Integer(nullable).toString());
    };

    // Type Case Sensitive
    protected void buildTypeCaseSensitive(String caseSensitive) throws Exception {
        actions.setAttribute("case_sensitive", caseSensitive);
    };

    /******************************/
    // User-defined Type Occurrence
    protected void buildUDTOccurrence() throws Exception {
        actions.processOccurrenceId(-1, JdbcReverseToolkitPlugin.UDT);
    };

    // UDT User
    protected void buildUDTSchema(String uDTSchema) throws Exception {
        actions.setAttribute("owner", uDTSchema);
    };

    // UDT Name
    protected void buildUDTName(String uDTName) throws Exception {
        actions.setAttribute("name", uDTName);
    };

    // UDT Class
    protected void buildUDTClassName(String uDTClassName) throws Exception {
        actions.setAttribute("class_name", uDTClassName);
    };

    // UDT SQL Type
    protected void buildUDTType(short uDTType) throws Exception {
        actions.setAttribute("sql_type", new Short(uDTType).toString());
    };

    // UDT Comments
    protected void buildUDTRemark(String uDTRem) throws Exception {
        actions.setAttribute("comments", uDTRem);
    };

}
