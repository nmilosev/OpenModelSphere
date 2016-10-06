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

/**
 * This service executes JDBC API calls
 **/
public abstract class JdbcBuilder {

    /* Catalog */
    protected void buildCatalogOccurrence() throws Exception {
    };

    protected void buildCatalogName(String catalogName) throws Exception {
    };

    /* Schema */
    protected void buildSchemaOccurrence() throws Exception {
    };

    protected void buildSchemaName(String schemaName) throws Exception {
    };

    /* Table Type */
    protected void buildTableTypeName(String typeName) throws Exception {
    };

    /* Table (5) */
    protected void buildTableOccurrence() throws Exception {
    };

    protected void buildTableCatalog(String catalogName) throws Exception {
    };

    protected void buildTableSchema(String schemaName) throws Exception {
    };

    protected void buildTableName(String tableName) throws Exception {
    };

    protected void buildTableType(String tableTypeName) throws Exception {
    };

    protected void buildTableRemark(String tableRemark) throws Exception {
    };

    /* View */
    protected void buildViewOccurrence() throws Exception {
    };

    protected void buildViewCatalog(String catalogName) throws Exception {
    };

    protected void buildViewSchema(String schemaName) throws Exception {
    };

    protected void buildViewName(String viewName) throws Exception {
    };

    protected void buildViewType(String viewTypeName) throws Exception {
    };

    protected void buildViewRemark(String viewRemark) throws Exception {
    };

    /* Table Column (18) */
    protected void buildTableColumnOccurrence() throws Exception {
    };

    protected void buildTableColumnOwner(String owner) throws Exception {
    };

    protected void buildTableColumnTableName(String tableName) throws Exception {
    };

    protected void buildTableColumnName(String columnName) throws Exception {
    };

    protected void buildTableColumnType(String typeName) throws Exception {
    };

    protected void buildTableColumnSize(int size) throws Exception {
    };

    protected void buildTableColumnDecimals(int decimals) throws Exception {
    };

    protected void buildTableColumnSQLDataType(short colJavaSQLDataType) throws Exception {
    };

    protected void buildTableColumnNull(int colNull) throws Exception {
    };

    protected void buildTableColumnDefValue(String defaultValue) throws Exception {
    };

    protected void buildTableColumnRemark(String columnRemark) throws Exception {
    };

    /* View Column (18) */
    protected void buildViewColumnOccurrence() throws Exception {
    };

    protected void buildViewColumnOwner(String owner) throws Exception {
    };

    protected void buildViewColumnTableName(String tableName) throws Exception {
    };

    protected void buildViewColumnName(String columnName) throws Exception {
    };

    protected void buildViewColumnType(String typeName) throws Exception {
    };

    protected void buildViewColumnSize(int size) throws Exception {
    };

    protected void buildViewColumnDecimals(int decimals) throws Exception {
    };

    protected void buildViewColumnSQLDataType(short colJavaSQLDataType) throws Exception {
    };

    protected void buildViewColumnNull(int colNull) throws Exception {
    };

    protected void buildViewColumnDefValue(String defaultValue) throws Exception {
    };

    protected void buildViewColumnRemark(String columnRemark) throws Exception {
    };

    /* Primary Key (6) */
    protected void buildPKOccurrence() throws Exception {
    };

    protected void buildPKTableCatalog(String pkTableCatalog) throws Exception {
    };

    protected void buildPKTableSchema(String pkTableSchema) throws Exception {
    };

    protected void buildPKTableName(String pkTableName) throws Exception {
    };

    protected void buildPKColumnName(String pkColumnName) throws Exception {
    };

    protected void buildPKKeySequence(short pkKeySequence) throws Exception {
    };

    protected void buildPKName(String pkName) throws Exception {
    };

    /* Foreign Key (14) */
    protected void buildFKOccurrence() throws Exception {
    };

    protected void buildFKPKTableCatalog(String fkPKTableCatalog) throws Exception {
    };

    protected void buildFKPKTableSchema(String fkPKTableSchema) throws Exception {
    };

    protected void buildFKPKTableName(String fkPKTableName) throws Exception {
    };

    protected void buildFKPKColumnName(String fkPKColumnName) throws Exception {
    };

    protected void buildFKTableCatalog(String fkTableCatalog) throws Exception {
    };

    protected void buildFKTableSchema(String fkTableSchema) throws Exception {
    };

    protected void buildFKTableName(String fkTableName) throws Exception {
    };

    protected void buildFKColumnName(String fkColumnName) throws Exception {
    };

    protected void buildFKSequence(String fkKeySequence) throws Exception {
    };

    protected void buildFKUpdateRule(String fkUpdateRule) throws Exception {
    };

    protected void buildFKDeleteRule(String fkDeleteRule) throws Exception {
    };

    protected void buildFKName(String fkName) throws Exception {
    };

    protected void buildFKPKName(String fkPKName) throws Exception {
    };

    protected void buildFKDefferrability(String fkDeferrability) throws Exception {
    };

    /* Indexes (13) */
    protected void buildIndexOccurrence() throws Exception {
    };

    protected void buildIndexTableCatalog(String indexTableCatalog) throws Exception {
    };

    protected void buildIndexTableSchema(String indexTableSchema) throws Exception {
    };

    protected void buildIndexTableName(String indexTableName) throws Exception {
    };

    protected void buildIndexNonUnique(String indexNonUnique) throws Exception {
    };

    protected void buildIndexQualifier(String indexQualifier) throws Exception {
    };

    protected void buildIndexName(String indexName) throws Exception {
    };

    protected void buildIndexType(String indexType) throws Exception {
    };

    protected void buildIndexOrdinalPosition(String indexOrdinalPosition) throws Exception {
    };

    protected void buildIndexColumnName(String indexColumnName) throws Exception {
    };

    protected void buildIndexAscDesc(String indexAscDesc) throws Exception {
    };

    protected void buildIndexCardinality(int indexCardinality) throws Exception {
    };

    protected void buildIndexPages(int indexPages) throws Exception {
    };

    protected void buildIndexFilter(String indexFilter) throws Exception {
    };

    /* Procedures (8) */
    protected void buildProcOccurrence() throws Exception {
    };

    protected void buildProcCatalog(String tokenProcCatalog) throws Exception {
    };

    protected void buildProcSchema(String tokenProcSchema) throws Exception {
    };

    protected void buildProcName(String tokenProcName) throws Exception {
    };

    protected void buildProcReserved1(String tokenProcReserved1) throws Exception {
    };

    protected void buildProcReserved2(String tokenProcReserved2) throws Exception {
    };

    protected void buildProcReserved3(String tokenProcReserved3) throws Exception {
    };

    protected void buildProcRemark(String tokenProcRem) throws Exception {
    };

    protected void buildProcType(String tokenProcType) throws Exception {
    };

    /* Procedure Columns (13) */
    protected void buildProcColOccurrence() throws Exception {
    };

    protected void buildProcColProcCatalog(String procCatalog) throws Exception {
    };

    protected void buildProcColProcSchema(String procSchema) throws Exception {
    };

    protected void buildProcColProcName(String procName) throws Exception {
    };

    protected void buildProcColName(String procColName) throws Exception {
    };

    protected void buildProcColType(short procColType) throws Exception {
    };

    protected void buildProcColSQLType(short procColSQLDataType) throws Exception {
    };

    protected void buildProcColDataType(String procColTypeName) throws Exception {
    };

    protected void buildProcColPrec(int procColPrecision) throws Exception {
    };

    protected void buildProcColLength(int procColLength) throws Exception {
    };

    protected void buildProcColScale(short procColScale) throws Exception {
    };

    protected void buildProcColRadix(int procColRadix) throws Exception {
    };

    protected void buildProcColNull(short procColNull) throws Exception {
    };

    protected void buildProcColRemark(String procColRemarks) throws Exception {
    };

    /* Types */
    protected void buildTypeOccurrence() throws Exception {
    };

    protected void buildTypeName(String typeName) throws Exception {
    };

    protected void buildTypeDataType(short dataType) throws Exception {
    };

    protected void buildTypePrecision(int precision) throws Exception {
    };

    protected void buildTypePrefix(String prefix) throws Exception {
    };

    protected void buildTypeSuffix(String suffix) throws Exception {
    };

    protected void buildTypeCreateParams(String createParams) throws Exception {
    };

    protected void buildTypeNullable(int nullable) throws Exception {
    };

    protected void buildTypeCaseSensitive(String caseSensitive) throws Exception {
    };

    protected void buildTypeSearchable(short searchable) throws Exception {
    };

    protected void buildTypeUnsignedAttr(String unsignedAttr) throws Exception {
    };

    protected void buildTypeFixedPrecScale(String fixedPrecScale) throws Exception {
    };

    protected void buildTypeAutoIncrement(String autoIncrement) throws Exception {
    };

    protected void buildTypeLocalTypeName(String localTypeName) throws Exception {
    };

    protected void buildTypeMinScale(short minScale) throws Exception {
    };

    protected void buildTypeMaxScale(short maxScale) throws Exception {
    };

    protected void buildTypeSQLDataType(int sqlDataType) throws Exception {
    };

    protected void buildTypeSQLDatetimeSub(int sqlDatatimeSub) throws Exception {
    };

    protected void buildTypePrecRadix(int precRadix) throws Exception {
    };

    /* User Defined Types */
    protected void buildUDTOccurrence() throws Exception {
    };

    protected void buildUDTCatalog(String uDTCatalog) throws Exception {
    };

    protected void buildUDTSchema(String uDTSchema) throws Exception {
    };

    protected void buildUDTName(String uDTName) throws Exception {
    };

    protected void buildUDTClassName(String uDTClassName) throws Exception {
    };

    protected void buildUDTType(short uDTType) throws Exception {
    };

    protected void buildUDTRemark(String uDTRem) throws Exception {
    };

}
