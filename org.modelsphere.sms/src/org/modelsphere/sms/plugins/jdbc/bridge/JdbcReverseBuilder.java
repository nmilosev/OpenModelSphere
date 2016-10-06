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

import java.sql.Types;
import java.util.HashMap;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.reverse.engine.Constraint;
import org.modelsphere.jack.srtool.reverse.engine.DbElement;
import org.modelsphere.jack.srtool.reverse.engine.DomainMapping;
import org.modelsphere.jack.srtool.reverse.engine.SrxDbObjectElement;
import org.modelsphere.jack.srtool.reverse.engine.SrxDomainElement;
import org.modelsphere.jack.srtool.reverse.engine.SrxPrimitiveElement;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.db.srtypes.SMSPassingConvention;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAbstractMethod;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORIndexKey;
import org.modelsphere.sms.or.db.DbORParameter;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORProcedure;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.db.srtypes.ORDomainCategory;
import org.modelsphere.sms.or.db.srtypes.ORIndexKeySort;
import org.modelsphere.sms.or.db.srtypes.ORValidationRule;
import org.modelsphere.sms.or.features.dbms.DBMSReverseOptions;
import org.modelsphere.sms.or.features.dbms.GenericBuilder;
import org.modelsphere.sms.or.generic.db.DbGEColumn;
import org.modelsphere.sms.or.generic.db.DbGEForeign;
import org.modelsphere.sms.or.generic.db.DbGEIndex;
import org.modelsphere.sms.or.generic.db.DbGEParameter;
import org.modelsphere.sms.or.generic.db.DbGEPrimaryUnique;
import org.modelsphere.sms.or.generic.db.DbGEProcedure;
import org.modelsphere.sms.or.generic.db.DbGETable;
import org.modelsphere.sms.or.generic.db.DbGEView;
import org.modelsphere.sms.plugins.TargetSystemInfo;
import org.modelsphere.sms.plugins.TargetSystemManager;

public class JdbcReverseBuilder extends GenericBuilder {
    protected static final int UNKNOWN = 0;
    protected static final int PARAMIN = 1;
    protected static final int PARAMINOUT = 2;
    protected static final int PARAMOUT = 3;
    protected static final int RETURNTYPE = 4;
    protected static final int RESULTCOLUMN = 5;

    protected DomainMapping map_type_null = null;
    protected DomainMapping map_column_null = null;
    protected DomainMapping map_validation_rule = null;
    protected DomainMapping map_index_unique = null;
    protected DomainMapping map_index_sort = null;
    protected DomainMapping map_proc_fnc = null;
    protected DomainMapping map_proc_col_type = null;

    protected boolean addOwner = true;
    protected Constraint tableOwnerConstraint = null;
    protected Constraint tableOwnerConstraint2 = null;
    protected Constraint procOwnerConstraint = null;

    protected static HashMap typeMap = new HashMap(); // JDBC type definition

    public JdbcReverseBuilder() {
        initTypeMap();
    }

    protected void initDBMSSpecific() throws DbException {
        initDBMSSpecificOptions();
        initDBMSSpecificDomMapping();
        // Specific Concept Mapping
        add(JdbcReverseToolkitPlugin.USER, DbORUser.metaClass, new SrxDbObjectElement(userNode),
                addOwner);
        add(JdbcReverseToolkitPlugin.USER, new DbElement(DbORUser.fName), new SrxPrimitiveElement(
                "name", String.class), addOwner);

        add(JdbcReverseToolkitPlugin.TYPE, "hookType", true); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.UDT, "hookUDT", true); // NOT LOCALIZABLE

        add(JdbcReverseToolkitPlugin.TABLE, DbGETable.metaClass, new SrxDbObjectElement(dataModel),
                true);
        add(JdbcReverseToolkitPlugin.TABLE, new DbElement(DbGETable.fUser), new SrxDbObjectElement(
                userNode, DbORUser.metaClass, "owner", null), addOwner); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.TABLE, new DbElement(DbGETable.fName),
                new SrxPrimitiveElement("name", String.class), true); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.TABLE, new DbElement(DbGETable.fDescription),
                new SrxPrimitiveElement("comments", String.class), true); // NOT LOCALIZABLE

        add(JdbcReverseToolkitPlugin.VIEW, DbGEView.metaClass, new SrxDbObjectElement(dataModel),
                true);
        add(JdbcReverseToolkitPlugin.VIEW, new DbElement(DbGEView.fUser), new SrxDbObjectElement(
                userNode, DbORUser.metaClass, "owner", null), addOwner); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.VIEW, new DbElement(DbGEView.fName), new SrxPrimitiveElement(
                "name", String.class), true); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.VIEW, new DbElement(DbGEView.fDescription),
                new SrxPrimitiveElement("comments", String.class), true); // NOT LOCALIZABLE

        add(JdbcReverseToolkitPlugin.COLUMN, DbGEColumn.metaClass, new SrxDbObjectElement(
                dataModel, DbORAbsTable.metaClass, "table_name", // NOT LOCALIZABLE
                new Constraint[] { tableOwnerConstraint }), true);
        add(JdbcReverseToolkitPlugin.COLUMN, new DbElement(DbGEColumn.fName),
                new SrxPrimitiveElement("name", String.class), true); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.COLUMN, new DbElement(DbGEColumn.fNull), new SrxDomainElement(
                "null", map_column_null), true); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.COLUMN, new DbElement(DbGEColumn.fInitialValue),
                new SrxPrimitiveElement("default_value", String.class), true); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.COLUMN, new DbElement(DbGEColumn.fDescription),
                new SrxPrimitiveElement("comments", String.class), true); // NOT LOCALIZABLE
        addPostProcessOccurrenceHook(JdbcReverseToolkitPlugin.COLUMN,
                "hookColumnTypeLengthDecimals", true); // NOT LOCALIZABLE

        add(JdbcReverseToolkitPlugin.VIEW_COLUMN, DbGEColumn.metaClass, new SrxDbObjectElement(
                dataModel, DbORAbsTable.metaClass, "table_name", // NOT LOCALIZABLE
                new Constraint[] { tableOwnerConstraint }), true);
        add(JdbcReverseToolkitPlugin.VIEW_COLUMN, new DbElement(DbGEColumn.fName),
                new SrxPrimitiveElement("name", String.class), true); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.VIEW_COLUMN, new DbElement(DbGEColumn.fNull),
                new SrxDomainElement("null", map_column_null), true); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.VIEW_COLUMN, new DbElement(DbGEColumn.fInitialValue),
                new SrxPrimitiveElement("default_value", String.class), true); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.VIEW_COLUMN, new DbElement(DbGEColumn.fDescription),
                new SrxPrimitiveElement("comments", String.class), true); // NOT LOCALIZABLE
        addPostProcessOccurrenceHook(JdbcReverseToolkitPlugin.VIEW_COLUMN,
                "hookColumnTypeLengthDecimals", true); // NOT LOCALIZABLE

        add(JdbcReverseToolkitPlugin.PK, new SrxDbObjectElement(dataModel, DbGETable.metaClass,
                "table_name",
                new Constraint[] { tableOwnerConstraint }, // NOT LOCALIZABLE
                DbGEPrimaryUnique.metaClass, "name", null, true,
                SrxDbObjectElement.CREATE_IF_NOT_FOUND), true); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.PK, new DbElement(DbGEPrimaryUnique.fName),
                new SrxPrimitiveElement("name", String.class), true); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.PK, new DbElement(DbGEPrimaryUnique.fColumns),
                new SrxDbObjectElement(dataModel, DbGETable.metaClass,
                        "table_name", // NOT LOCALIZABLE
                        new Constraint[] { tableOwnerConstraint }, DbGEColumn.metaClass,
                        "column_name", null), true); // NOT LOCALIZABLE

        add(JdbcReverseToolkitPlugin.FK, "hookForeignKey", true); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.FK, new DbElement(DbGEForeign.fName), new SrxPrimitiveElement(
                "name", String.class), true); // NOT LOCALIZABLE

        add(JdbcReverseToolkitPlugin.INDEX, "hookIndex", true); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.INDEX, new DbElement(DbGEIndex.fName),
                new SrxPrimitiveElement("name", String.class), true); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.INDEX, new DbElement(DbGEIndex.fUnique), new SrxDomainElement(
                "unique", map_index_unique), true); // NOT LOCALIZABLE

        add(JdbcReverseToolkitPlugin.PROC, DbGEProcedure.metaClass, new SrxDbObjectElement(
                operationLibrary), true); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.PROC, new DbElement(DbGEProcedure.fUser),
                new SrxDbObjectElement(userNode, DbORUser.metaClass, "owner", null), addOwner); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.PROC, new DbElement(DbGEProcedure.fName),
                new SrxPrimitiveElement("name", String.class), true); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.PROC, new DbElement(DbGEProcedure.fDescription),
                new SrxPrimitiveElement("comments", String.class), true); // NOT LOCALIZABLE
        add(JdbcReverseToolkitPlugin.PROC, new DbElement(DbGEProcedure.fFunction),
                new SrxDomainElement("type", map_proc_fnc), true); // NOT LOCALIZABLE

        add(JdbcReverseToolkitPlugin.PROCCOL, "hookProcCol", true); // NOT LOCALIZABLE

        registerForOutput();
    }

    protected void initDBMSSpecificOptions() throws DbException {
        DBMSReverseOptions options = getDBMSReverseOptions();
        addOwner = options.reverseObjectUser;
        tableOwnerConstraint = (addOwner ? new Constraint(new DbElement(DbORAbsTable.fUser),
                new SrxDbObjectElement(userNode, DbORUser.metaClass, "owner", null)) : null); // NOT LOCALIZABLE
        tableOwnerConstraint2 = (addOwner ? new Constraint(new DbElement(DbORAbsTable.fUser),
                new SrxDbObjectElement(userNode, DbORUser.metaClass, "owner2", null)) : null); // NOT LOCALIZABLE
        procOwnerConstraint = (addOwner ? new Constraint(new DbElement(DbORAbstractMethod.fUser),
                new SrxDbObjectElement(userNode, DbORUser.metaClass, "owner", null)) : null); // NOT LOCALIZABLE
    }

    protected DbORDatabase createORDatabase(DbObject root, DbSMSTargetSystem ts) throws DbException {
        DbORDatabase database = super.createORDatabase(root, ts);
        Object config = this.getDBMSReverseOptions().getSpecificDBMSOptions();

        if (database != null && (config instanceof JdbcReverseOptions)) {
            String catalog = ((JdbcReverseOptions) config).getCatalogOption();
            if (catalog != null) {
                database.setName(catalog);
                database.setPhysicalName(catalog);
            }
        }
        return database;
    }

    protected void initDBMSSpecificDomMapping() throws DbException {
        map_type_null = new DomainMapping(Boolean.TRUE);
        map_type_null.put("0", Boolean.FALSE);
        map_type_null.put("1", Boolean.TRUE);
        map_type_null.put("2", Boolean.TRUE);

        map_column_null = new DomainMapping(Boolean.TRUE);
        map_column_null.put("0", Boolean.FALSE);
        map_column_null.put("1", Boolean.TRUE);
        map_column_null.put("2", Boolean.TRUE);

        map_validation_rule = new DomainMapping(null);
        map_column_null.put("1", null); // No action = restrict
        map_column_null.put("2", ORValidationRule.getInstance(ORValidationRule.CASCADE)); // Cascade
        map_column_null.put("3", ORValidationRule.getInstance(ORValidationRule.SETNULL)); // Set Null
        map_column_null.put("4", ORValidationRule.getInstance(ORValidationRule.SETDEFAULT)); // Set Default
        map_column_null.put("5", null); // Restrict

        map_index_unique = new DomainMapping(Boolean.FALSE);
        map_index_unique.put("0", Boolean.TRUE);
        map_index_unique.put("1", Boolean.FALSE);

        map_index_sort = new DomainMapping(null);
        map_index_sort.put("A", ORIndexKeySort.getInstance(ORIndexKeySort.ASC));
        map_index_sort.put("D", ORIndexKeySort.getInstance(ORIndexKeySort.DESC));

        map_proc_fnc = new DomainMapping(Boolean.FALSE);
        map_proc_fnc.put("1", Boolean.FALSE);
        map_proc_fnc.put("2", Boolean.FALSE);
        map_proc_fnc.put("3", Boolean.TRUE);

        map_proc_col_type = new DomainMapping(null);
        map_proc_col_type.put("0", new Integer(UNKNOWN));
        map_proc_col_type.put("1", new Integer(PARAMIN));
        map_proc_col_type.put("2", new Integer(PARAMINOUT));
        map_proc_col_type.put("3", new Integer(PARAMOUT));
        map_proc_col_type.put("4", new Integer(RETURNTYPE));
        map_proc_col_type.put("5", new Integer(RESULTCOLUMN));
    }

    protected void registerForOutput() throws DbException {
        // Build list of concept available in the repport
        registerConceptForOutputReport(JdbcReverseToolkitPlugin.TYPE, true);
        registerConceptForOutputReport(JdbcReverseToolkitPlugin.TABLE, true);
        registerConceptForOutputReport(JdbcReverseToolkitPlugin.COLUMN, true);
        registerConceptForOutputReport(JdbcReverseToolkitPlugin.PK, true);
        registerConceptForOutputReport(JdbcReverseToolkitPlugin.FK, true);
        registerConceptForOutputReport(JdbcReverseToolkitPlugin.INDEX, true);
        registerConceptForOutputReport(JdbcReverseToolkitPlugin.PROC, true);
        registerConceptForOutputReport(JdbcReverseToolkitPlugin.VIEW, true);
        registerConceptForOutputReport(JdbcReverseToolkitPlugin.VIEW_COLUMN, true);
    }

    protected void exitDBMSSpecific() throws DbException {
    }

    protected void abortDBMSSpecific() throws DbException {
    }

    protected void initTypeMap() {
        typeMap.put(new Integer(Types.ARRAY), new JdbcReverseTypeDef("ARRAY", false, false, false,
                false, ORDomainCategory.getInstance(ORDomainCategory.COLLECTION))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.BIGINT), new JdbcReverseTypeDef("LONG INTEGER", false, false,
                false, true, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.BINARY), new JdbcReverseTypeDef("BINARY", true, false, false,
                false, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.BIT), new JdbcReverseTypeDef("BIT", false, false, false,
                true, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.BLOB), new JdbcReverseTypeDef("BLOB", false, false, false,
                false, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.BOOLEAN), new JdbcReverseTypeDef("BOOLEAN", false, false,
                false, true, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.CHAR), new JdbcReverseTypeDef("CHARACTER", true, false,
                false, false, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.CLOB), new JdbcReverseTypeDef("CLOB", false, false, false,
                false, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.DATALINK), new JdbcReverseTypeDef("DATALINK", false, false,
                false, false, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.DATE), new JdbcReverseTypeDef("DATE", false, false, false,
                false, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.DECIMAL), new JdbcReverseTypeDef("DECIMAL", true, true,
                false, true, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.DISTINCT), new JdbcReverseTypeDef("DISTINCT", true, true,
                false, false, ORDomainCategory.getInstance(ORDomainCategory.DISTINCT))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.DOUBLE), new JdbcReverseTypeDef("DOUBLE PRECISION", false,
                false, false, true, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.FLOAT), new JdbcReverseTypeDef("FLOAT", false, false, false,
                true, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.INTEGER), new JdbcReverseTypeDef("INTEGER", false, false,
                false, true, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.JAVA_OBJECT), new JdbcReverseTypeDef("JAVA OBJECT", false,
                false, false, false, ORDomainCategory.getInstance(ORDomainCategory.OPAQUE))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.LONGVARBINARY), new JdbcReverseTypeDef("LARGE VARBINARY",
                false, false, false, false, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.LONGVARCHAR), new JdbcReverseTypeDef("LONG VARCHAR", false,
                false, false, false, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.NULL), new JdbcReverseTypeDef("NULL", true, true, false,
                false, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.NUMERIC), new JdbcReverseTypeDef("NUMERIC", true, true,
                false, true, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.OTHER), new JdbcReverseTypeDef("OTHER", true, true, false,
                false, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.REAL), new JdbcReverseTypeDef("REAL", false, false, false,
                true, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.REF), new JdbcReverseTypeDef("REF", false, false, true,
                false, ORDomainCategory.getInstance(ORDomainCategory.STRUCTURED))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.SMALLINT), new JdbcReverseTypeDef("SMALL INTEGER", false,
                false, false, true, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.STRUCT), new JdbcReverseTypeDef("STRUCTURED", false, false,
                false, false, ORDomainCategory.getInstance(ORDomainCategory.STRUCTURED))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.TIME), new JdbcReverseTypeDef("TIME", false, false, false,
                false, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.TIMESTAMP), new JdbcReverseTypeDef("TIMESTAMP", false, false,
                false, false, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.TINYINT), new JdbcReverseTypeDef("SMALL INTEGER", false,
                false, false, true, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.VARBINARY), new JdbcReverseTypeDef("VARIABLE BINARY", true,
                false, false, false, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
        typeMap.put(new Integer(Types.VARCHAR), new JdbcReverseTypeDef("VARIABLE CHARACTER", true,
                false, false, false, ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))); // NOT LOCALIZABLE
    }

    /****************/
    /* HOOK SECTION */
    /****************/

    public Object hookType() throws Exception {
        DbObject domain = null;
        DbORBuiltInType type = null;
        JdbcReverseTypeDef typeDef = null;
        ORDomainCategory category = ORDomainCategory.getInstance(ORDomainCategory.DOMAIN);
        boolean length = false;
        boolean isRef = false;
        String targetLogicalName = null;

        String sName = (String) currentObject.get("name"); // NOT LOCALIZABLE
        String sSQLType = (String) currentObject.get("sql_type"); // NOT LOCALIZABLE
        String sLength = (String) currentObject.get("length"); // NOT LOCALIZABLE
        String sNullable = (String) currentObject.get("nullable"); // NOT LOCALIZABLE
        String sCaseSens = (String) currentObject.get("case_sensitive"); // NOT LOCALIZABLE

        if (sName == null)
            return null;

        // Try to find the type in the mapping table.
        if (sSQLType != null) {
            typeDef = (JdbcReverseTypeDef) typeMap.get(new Integer(sSQLType));
            if (typeDef != null) {
                targetLogicalName = typeDef.getName();
                category = typeDef.getCategory();
                isRef = typeDef.getRef();
                length = typeDef.getLength();
            }
        }

        if (targetLogicalName != null) {
            type = getDomainSourceType(targetLogicalName);
        }
        // Will process the type correctly
        domain = getDbORDomain(sName, type, category, true);
        if (length)
            ((DbORDomain) domain).setLength(new Integer(sLength));
        if (isRef)
            ((DbORDomain) domain).setReference(new Boolean(isRef));
        ((DbORDomain) domain).setNull((Boolean) (map_type_null.get(sNullable)));
        ((DbORDomain) domain).setCaseSensitive((Boolean) (map_type_null.get(sCaseSens)));

        return domain;
    }

    /* This method gets the proper builtin type */
    private DbORBuiltInType getDomainSourceType(String typeName) throws DbException {
        DbORBuiltInType type = null;
        if (typeName == null)
            return null;

        DbSMSTargetSystem ts = domainModel.getTargetSystem().getBuiltInTypePackage()
                .getTargetSystem();
        TargetSystemInfo tsInfo = TargetSystemManager.getSingleton().getTargetSystemInfo(ts);
        String targetName = tsInfo.logicalToType(typeName);
        if (targetName != null) {
            type = (DbORBuiltInType) domainModel.getTargetSystem().getBuiltInTypePackage()
                    .findComponentByName(DbORBuiltInType.metaClass, targetName.toUpperCase());
        }
        return type;
    }

    /*
     * This method only deal with domain. It should not create new ones as they were previously
     * created during the type reverse. It should only complete the domain information.
     */
    public Object hookUDT() throws Exception {
        String sOwner = (String) currentObject.get("owner"); // NOT LOCALIZABLE
        String sName = (String) currentObject.get("name"); // NOT LOCALIZABLE
        DbORUser user = null;

        if (sName == null)
            return null;
        // Search with owner
        DbORDomain domain = (DbORDomain) SrxDbObjectElement.findComponentByName(this,
                currentObject, udtModel, DbORDomain.metaClass, sName,
                new Constraint[] { tableOwnerConstraint });
        // Complete the domain
        if ((domain != null) && (user != null)) {
            domain.setUser(user);
        }
        return null;
    }

    public Object hookColumnTypeLengthDecimals() throws DbException {
        DbORColumn column = (DbORColumn) currentObject.get(CURRENT_OBJECT);
        String sType = (String) currentObject.get("type"); // NOT LOCALIZABLE
        boolean isRef = false;
        DbObject type = null;

        if (sType == null)
            return null;

        String sSQLType = (String) currentObject.get("sql_type"); // NOT LOCALIZABLE
        JdbcReverseTypeDef typeDef = null;
        ORDomainCategory category = null;

        if (sSQLType != null) {
            typeDef = (JdbcReverseTypeDef) typeMap.get(new Integer(sSQLType));
            if (typeDef != null) {
                String length = (String) currentObject.get("size"); // NOT LOCALIZABLE
                String decimal = (String) currentObject.get("decimals"); // NOT LOCALIZABLE
                category = typeDef.getCategory();
                isRef = typeDef.getRef();
                if ((length != null) && (typeDef.getLength()))
                    column.setLength(new Integer(length));
                if ((decimal != null) && (typeDef.getDecimal()))
                    column.setNbDecimal(new Integer(decimal));
                column.setReference(new Boolean(isRef));
                String defValue = column.getInitialValue();
                if (defValue != null) {
                    column.setInitialValue(getDefaultValue(defValue, typeDef));
                }
            }
        }

        type = getDbORDomain(sType, null, category, true);
        if (type != null)
            column.setType((DbORTypeClassifier) (type));

        return type;
    }

    // Override for specific parsing for default value
    protected String getDefaultValue(String s, JdbcReverseTypeDef typeDef) {
        if (s == null)
            return null;
        s = s.trim();
        if (s.length() == 0)
            return s;
        // check for parenthesis at beginning and end;
        if (s.indexOf("(") == 0 && s.lastIndexOf(")") == (s.length() - 1)) {
            s = s.substring(1, s.length() - 1);
        }
        // remove single or double quote
        s = s.trim();
        if (s.indexOf("'") == 0 && s.lastIndexOf("'") == (s.length() - 1)) {
            s = s.substring(1, s.length() - 1);
        } else if (s.indexOf("\"") == 0 && s.lastIndexOf("\"") == (s.length() - 1)) {
            s = s.substring(1, s.length() - 1);
        }
        return s;
    }

    public Object hookForeignKey() throws Exception {
        // Try to find the foreign key first
        String sPkTable = (String) currentObject.get("fk_pk_table"); // NOT LOCALIZABLE
        String sPkColumn = (String) currentObject.get("fk_pk_column"); // NOT LOCALIZABLE
        String sPkName = (String) currentObject.get("fk_pk_name"); // NOT LOCALIZABLE
        String sFkTable = (String) currentObject.get("fk_table"); // NOT LOCALIZABLE
        String sFkColumn = (String) currentObject.get("fk_column"); // NOT LOCALIZABLE
        String sFKName = (String) currentObject.get("name"); // NOT LOCALIZABLE
        String sUpdRule = (String) currentObject.get("upd_rule"); // NOT LOCALIZABLE
        String sDelRule = (String) currentObject.get("del_rule"); // NOT LOCALIZABLE

        boolean keyColsCreated = false;

        DbORTable table = (DbORTable) SrxDbObjectElement
                .findComponentByName(this, currentObject, dataModel, DbORTable.metaClass, sFkTable,
                        new Constraint[] { tableOwnerConstraint });
        DbORTable srcTable = (DbORTable) SrxDbObjectElement.findComponentByName(this,
                currentObject, dataModel, DbORTable.metaClass, sPkTable,
                new Constraint[] { tableOwnerConstraint2 });

        if ((table == null) || (srcTable == null))
            return null;

        DbORForeign fKey = (DbORForeign) SrxDbObjectElement.findComponentByName(this,
                currentObject, table, DbORForeign.metaClass, sFKName, null);

        // If exist, just create the key column object
        if (fKey != null) {
            keyColsCreated = createForeignKeyColumns(fKey, sFkTable, sFkColumn, sPkTable, sPkColumn);
            return null;
        }

        DbORPrimaryUnique pKey = (DbORPrimaryUnique) SrxDbObjectElement.findComponentByName(this,
                currentObject, srcTable, DbORPrimaryUnique.metaClass, sPkName, null);

        // If not found create it
        SMSMultiplicity mult1 = SMSMultiplicity.getInstance(SMSMultiplicity.OPTIONAL);
        SMSMultiplicity mult2 = SMSMultiplicity.getInstance(SMSMultiplicity.MANY);
        DbORAssociation assoc = new DbORAssociation(table, mult1, srcTable, mult2);
        DbORAssociationEnd frontEnd = assoc.getFrontEnd();

        if (map_validation_rule != null) {
            ORValidationRule updateRule = (ORValidationRule) map_validation_rule.get(sUpdRule);
            ORValidationRule deleteRule = (ORValidationRule) map_validation_rule.get(sDelRule);
            frontEnd.setUpdateRule(updateRule);
            frontEnd.setDeleteRule(deleteRule);
        }
        fKey = createForeignAux(frontEnd);
        if (pKey != null)
            frontEnd.setReferencedConstraint(pKey);
        if (keyColsCreated == false)
            keyColsCreated = createForeignKeyColumns(fKey, sFkTable, sFkColumn, sPkTable, sPkColumn);
        return fKey;
    }

    protected DbORForeign createForeignAux(DbORAssociationEnd frontEnd) throws DbException {
        return new DbGEForeign(frontEnd);
    }

    private boolean createForeignKeyColumns(DbORForeign fk, String fkTable, String fkColumn,
            String pkTable, String pkColumn) throws Exception {

        DbORTable table = (DbORTable) SrxDbObjectElement.findComponentByName(this, currentObject,
                dataModel, DbORTable.metaClass, fkTable, new Constraint[] { tableOwnerConstraint });
        DbORTable srcTable = (DbORTable) SrxDbObjectElement.findComponentByName(this,
                currentObject, dataModel, DbORTable.metaClass, pkTable,
                new Constraint[] { tableOwnerConstraint2 });

        if ((table == null) || (srcTable == null) || (fkColumn == null) || (pkColumn == null))
            return false;

        DbORColumn column = (DbORColumn) table.findComponentByName(DbORColumn.metaClass, fkColumn);
        DbORColumn srcColumn = (DbORColumn) srcTable.findComponentByName(DbORColumn.metaClass,
                pkColumn);

        if ((column == null) || (srcColumn == null))
            return false;

        DbORFKeyColumn fkCol = new DbORFKeyColumn(fk, column, srcColumn);
        if (fkCol == null)
            return false;
        return true;
    }

    public Object hookIndex() throws Exception {
        String sTable = (String) currentObject.get("table_name"); // NOT LOCALIZABLE
        String sName = (String) currentObject.get("name"); // NOT LOCALIZABLE

        if ((sTable == null) || (sName == null))
            return null;

        DbORTable table = (DbORTable) SrxDbObjectElement.findComponentByName(this, currentObject,
                dataModel, DbORTable.metaClass, sTable, new Constraint[] { tableOwnerConstraint });

        if (table == null)
            return null;

        DbORIndex index = (DbORIndex) table.findComponentByName(DbORIndex.metaClass, sName);

        if (index != null) {
            createIndexKeys(table, index);
            return null;
        }

        index = createIndexAux(table);
        createIndexKeys(table, index);
        return index;
    }

    protected DbORIndex createIndexAux(DbORTable table) throws DbException {
        return new DbGEIndex(table);
    }

    private void createIndexKeys(DbORTable table, DbORIndex anIndex) throws DbException {
        String sColumn = (String) currentObject.get("column"); // NOT LOCALIZABLE
        String sSort = (String) currentObject.get("sort"); // NOT LOCALIZABLE
        String sExpression = (String) currentObject.get("expression"); // NOT LOCALIZABLE

        if (sColumn == null)
            return;

        DbORColumn column = (DbORColumn) table.findComponentByName(DbORColumn.metaClass, sColumn);

        if (column == null)
            return;

        DbORIndexKey indexKey = new DbORIndexKey(anIndex);
        indexKey.setIndexedElement(column);
        indexKey.setExpression(sExpression);
        if (map_index_sort != null) {
            ORIndexKeySort sort = (ORIndexKeySort) map_index_sort.get(sSort);
            indexKey.setSortOption(sort);
        }
    }

    /* Special case: we have to determine what type of object it is */
    public Object hookProcCol() throws Exception {
        Object procCol = null;
        String sType = (String) currentObject.get("type"); // NOT LOCALIZABLE
        String sProcName = (String) currentObject.get("proc_name"); // NOT LOCALIZABLE

        Integer type = (Integer) map_proc_col_type.get(sType);

        if (type == null)
            return null;

        DbORProcedure proc = (DbORProcedure) SrxDbObjectElement.findComponentByName(this,
                currentObject, operationLibrary, DbORProcedure.metaClass, sProcName,
                new Constraint[] { procOwnerConstraint });

        if (proc == null)
            return null;

        SMSPassingConvention passBy = null;
        switch (type.intValue()) {
        case PARAMIN:
            passBy = SMSPassingConvention.getInstance(SMSPassingConvention.IN);
            procCol = createParam(proc, passBy);
            break;
        case PARAMINOUT:
            passBy = SMSPassingConvention.getInstance(SMSPassingConvention.INOUT);
            procCol = createParam(proc, passBy);
            break;
        case PARAMOUT:
            passBy = SMSPassingConvention.getInstance(SMSPassingConvention.OUT);
            procCol = createParam(proc, passBy);
            break;
        case RETURNTYPE:
            setReturnType(proc);
            break;
        case RESULTCOLUMN:
            break;
        }

        return procCol;
    }

    private Object createParam(DbORProcedure aProc, SMSPassingConvention passBy) throws DbException {
        String sSQLType = (String) currentObject.get("sql_type"); // NOT LOCALIZABLE
        String sTypeName = (String) currentObject.get("type_name"); // NOT LOCALIZABLE
        String sComments = (String) currentObject.get("comments"); // NOT LOCALIZABLE
        String sName = (String) currentObject.get("name"); // NOT LOCALIZABLE
        boolean isRef = false;

        DbORParameter param = createParamAux(aProc);
        param.setName(sName);
        param.setPassingConvention(passBy);
        param.setDescription(sComments);

        JdbcReverseTypeDef typeDef = null;
        ORDomainCategory category = null;
        if (sSQLType != null) {
            typeDef = (JdbcReverseTypeDef) typeMap.get(new Integer(sSQLType));
            if (typeDef != null) {
                String sLength = (String) currentObject.get("length"); // NOT LOCALIZABLE
                String sNbDecimal = (String) currentObject.get("nb_decimals"); // NOT LOCALIZABLE
                category = typeDef.getCategory();
                isRef = typeDef.getRef();
                if ((sLength != null) && (typeDef.getLength()))
                    param.setLength(new Integer(sLength));
                if ((sNbDecimal != null) && (typeDef.getDecimal()))
                    param.setNbDecimal(new Integer(sNbDecimal));
                param.setReference(new Boolean(isRef));
            }
        }

        DbObject type = null;
        if (sTypeName != null)
            type = getDbORDomain(sTypeName, null, category, true);

        if (type != null) {
            param.setType((DbORTypeClassifier) (type));
        }

        return param;
    }

    protected DbORParameter createParamAux(DbORProcedure aProc) throws DbException {
        return new DbGEParameter(aProc);
    }

    private void setReturnType(DbORProcedure aProc) throws DbException {
        String sSQLType = (String) currentObject.get("sql_type"); // NOT LOCALIZABLE
        String sTypeName = (String) currentObject.get("type_name"); // NOT LOCALIZABLE
        boolean isRef = false;
        DbObject type = null;

        if (sTypeName == null)
            return;

        JdbcReverseTypeDef typeDef = null;
        ORDomainCategory category = null;
        if (sSQLType != null) {
            typeDef = (JdbcReverseTypeDef) typeMap.get(new Integer(sSQLType));
            if (typeDef != null) {
                category = typeDef.getCategory();
                isRef = typeDef.getRef();
                aProc.setReturnTypeReference(new Boolean(isRef));
            }
        }

        type = getDbORDomain(sTypeName, null, category, true);
        if (type != null)
            aProc.setReturnType((DbORTypeClassifier) (type));
    }
}
