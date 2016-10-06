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

package org.modelsphere.sms.plugins.generic.validation;

import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginSelectionListener;
import org.modelsphere.jack.plugins.PluginServices;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.FocusManager;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.or.ORValidationPlugin;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.generic.db.*;

public class GenericValidationPlugin extends ORValidationPlugin implements Plugin2, PluginSelectionListener {
    private static final String GENERIC_VALIDATION = LocaleMgr.misc.getString("GenericValidation");

    private static final String[] reservedWords = new String[] {
            "ABSOLUTE",
            "ACTION",
            "ADD",
            "ADMIN",
            "AFTER",
            "AGGREGATE",
            "ALIAS",
            "ALL",
            "ALLOCATE",
            "ALTER", //NOT LOCALIZABLE, keywords
            "AND",
            "ANY",
            "ARE",
            "ARRAY",
            "AS",
            "ASC",
            "ASSERTION",
            "AT",
            "AUTHORIZATION", //NOT LOCALIZABLE, keywords
            "BEFORE",
            "BEGIN",
            "BINARY",
            "BIT",
            "BLOB",
            "BOOLEAN",
            "BOTH",
            "BREADTH",
            "BY", //NOT LOCALIZABLE, keywords
            "CALL",
            "CASCADE",
            "CASCADED",
            "CASE",
            "CAST",
            "CATALOG",
            "CHAR",
            "CHARACTER",
            "CHECK",
            "CLASS", //NOT LOCALIZABLE, keywords
            "CLOB",
            "CLOSE",
            "COLLATE",
            "COLLATION",
            "COLUMN",
            "COMMIT",
            "COMPLETION",
            "CONNECT", //NOT LOCALIZABLE, keywords
            "CONNECTION",
            "CONSTRAINT",
            "CONSTRAINTS",
            "CONSTRUCTOR",
            "CONTINUE",
            "CORRESPONDING",
            "CREATE", //NOT LOCALIZABLE, keywords
            "CROSS",
            "CUBE",
            "CURRENT",
            "CURRENT_DATE",
            "CURRENT_PATH",
            "CURRENT_ROLE",
            "CURRENT_TIME", //NOT LOCALIZABLE, keywords
            "CURRENT_TIMESTAMP",
            "CURRENT_USER",
            "CURSOR",
            "CYCLE", //NOT LOCALIZABLE, keywords
            "DATA",
            "DATE",
            "DAY",
            "DEALLOCATE",
            "DEC",
            "DECIMAL",
            "DECLARE",
            "DEFAULT",
            "DEFERRABLE", //NOT LOCALIZABLE, keywords
            "DEFERRED",
            "DELETE",
            "DEPTH",
            "DEREF",
            "DESC",
            "DESCRIBE",
            "DESCRIPTOR",
            "DESTROY",
            "DESTRUCTOR",//NOT LOCALIZABLE, keywords
            "DETERMINISTIC",
            "DICTIONARY",
            "DIAGNOSTICS",
            "DISCONNECT",
            "DISTINCT",
            "DOMAIN",
            "DOUBLE",
            "DROP",
            "DYNAMIC", //NOT LOCALIZABLE, keywords
            "EACH",
            "ELSE",
            "END",
            "END-EXEC",
            "EQUALS",
            "ESCAPE",
            "EVERY",
            "EXCEPT",
            "EXCEPTION",
            "EXEC",
            "EXECUTE",
            "EXTERNAL", //NOT LOCALIZABLE, keywords
            "FALSE",
            "FETCH",
            "FIRST",
            "FLOAT",
            "FOR",
            "FOREIGN",
            "FOUND",
            "FROM",
            "FREE",
            "FULL",
            "FUNCTION",//NOT LOCALIZABLE, keywords
            "GENERAL",
            "GET",
            "GLOBAL",
            "GO",
            "GOTO",
            "GRANT",
            "GROUP",
            "GROUPING", //NOT LOCALIZABLE, keywords
            "HAVING",
            "HOST",
            "HOUR", //NOT LOCALIZABLE, keywords
            "IDENTITY",
            "IGNORE",
            "IMMEDIATE",
            "IN",
            "INDICATOR",
            "INITIALIZE",
            "INITIALLY",
            "INNER",
            "INOUT", //NOT LOCALIZABLE, keywords
            "INPUT",
            "INSERT",
            "INT",
            "INTEGER",
            "INTERSECT",
            "INTERVAL",
            "INTO",
            "IS",
            "ISOLATION",
            "ITERATE",//NOT LOCALIZABLE, keywords
            "JOIN", //NOT LOCALIZABLE, keywords
            "KEY", //NOT LOCALIZABLE, keywords
            "LANGUAGE", "LARGE",
            "LAST",
            "LATERAL",
            "LEADING",
            "LEFT",
            "LESS",
            "LEVEL",
            "LIKE",
            "LIMIT",
            "LOCAL",
            "LOCALTIME",
            "LOCALTIMESTAMP",
            "LOCATOR",//NOT LOCALIZABLE, keywords
            "MAP",
            "MATCH",
            "MINUTE",
            "MODIFIES",
            "MODIFY",
            "MODULE",
            "MONTH", //NOT LOCALIZABLE, keywords
            "NAMES", "NATIONAL", "NATURAL",
            "NCHAR",
            "NCLOB",
            "NEW",
            "NEXT",
            "NO",
            "NONE",
            "NOT",
            "NULL",
            "NUMERIC", //NOT LOCALIZABLE, keywords
            "OBJECT", "OF", "OFF", "OLD", "ON", "ONLY", "OPEN",
            "OPERATION",
            "OPTION",
            "OR",
            "ORDER",
            "ORDINALITY",
            "OUT",
            "OUTER",
            "OUTPUT", //NOT LOCALIZABLE, keywords
            "PAD", "PARAMETER", "PARAMETERS",
            "PARTIAL",
            "PATH",
            "POSTFIX",
            "PRECISION",
            "PREFIX",
            "PREORDER",
            "PREPARE", //NOT LOCALIZABLE, keywords
            "PRESERVE",
            "PRIMARY",
            "PRIOR",
            "PRIVILEGES",
            "PROCEDURE",
            "PUBLIC", //NOT LOCALIZABLE, keywords
            "READ", "READS", "REAL", "RECURSIVE",
            "REF",
            "REFERENCES",
            "REFERENCING",
            "RELATIVE",
            "RESTRICT",
            "RESULT", //NOT LOCALIZABLE, keywords
            "RETURN", "RETURNS", "REVOKE", "RIGHT", "ROLE",
            "ROLLBACK",
            "ROLLUP",
            "ROUTINE",
            "ROW",
            "ROWS", //NOT LOCALIZABLE, keywords
            "SAVEPOINT", "SCHEMA", "SCROLL", "SCOPE", "SEARCH", "SECOND",
            "SECTION",
            "SELECT",
            "SEQUENCE",
            "SESSION", //NOT LOCALIZABLE, keywords
            "SESSION_USER", "SET", "SETS", "SIZE", "SMALLINT", "SOME", "SPACE",
            "SPECIFIC",
            "SPECIFICTYPE",
            "SQL", //NOT LOCALIZABLE, keywords
            "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "START", "STATE", "STATEMENT", "STATIC",
            "STRUCTURE",
            "SYSTEM_USER", //NOT LOCALIZABLE, keywords
            "TABLE", "TEMPORARY", "TERMINATE", "THAN", "THEN", "TIME", "TIMESTAMP",
            "TIMEZONE_HOUR", "TIMEZONE_MINUTE", //NOT LOCALIZABLE, keywords
            "TO", "TRAILING", "TRANSACTION", "TRANSLATION", "TREAT", "TRIGGER", "TRUE", //NOT LOCALIZABLE, keywords
            "UNDER", "UNION", "UNIQUE", "UNKNOWN", "UNNEST", "UPDATE", "USAGE", "USER", "USING", //NOT LOCALIZABLE, keywords
            "VALUE", "VALUES", "VARCHAR", "VARIABLE", "VARYING", "VIEW", //NOT LOCALIZABLE, keywords
            "WHEN", "WHENEVER", "WHERE", "WITH", "WITHOUT", "WORK", "WRITE", //NOT LOCALIZABLE, keywords
            "YEAR", //NOT LOCALIZABLE, keywords
            "ZONE", //NOT LOCALIZABLE, keywords
    };

    private static Object[][] physNameMaxLen = new Object[][] {
            new Object[] { DbGETable.metaClass.getGUIName(false, false), new Integer(30) },
            new Object[] { DbGEColumn.metaClass.getGUIName(false, false), new Integer(30) },
            new Object[] { DbGEView.metaClass.getGUIName(false, false), new Integer(30) },
            new Object[] { DbGEPrimaryUnique.metaClass.getGUIName(false, false), new Integer(30) },
            new Object[] { DbGEForeign.metaClass.getGUIName(false, false), new Integer(30) },
            new Object[] { DbGECheck.metaClass.getGUIName(false, false), new Integer(30) },
            new Object[] { DbGEIndex.metaClass.getGUIName(false, false), new Integer(30) },
            new Object[] { DbORDomain.metaClass.getGUIName(false, false), new Integer(30) },
            new Object[] { DbGETrigger.metaClass.getGUIName(false, false), new Integer(30) },
            new Object[] { DbGEProcedure.metaClass.getGUIName(false, false), new Integer(30) },
            new Object[] { DbGEParameter.metaClass.getGUIName(false, false), new Integer(30) },
            new Object[] { DbGEDatabase.metaClass.getGUIName(false, false), new Integer(30) }, };

    private static Object[][] dataTypes = new Object[][] {
            new Object[] { "BINARY", new Integer(NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "BIT", new Integer(NO_PRECISION | NO_SCALE | NOT_INDEXABLE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "BLOB", new Integer(NO_PRECISION | NO_SCALE | NOT_INDEXABLE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "BOOLEAN", new Integer(NO_PRECISION | NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "BYTE", new Integer(NO_SCALE | NOT_INDEXABLE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "CHAR", new Integer(NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "CHAR VARYING", new Integer(PRECISION_REQUIRED | NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "CHARACTER", new Integer(NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "CHARACTER VARYING", new Integer(PRECISION_REQUIRED | NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "CLOB", new Integer(NO_PRECISION | NO_SCALE | NOT_INDEXABLE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "DATE", new Integer(NO_PRECISION | NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "DATE TIME", new Integer(NO_PRECISION | NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "DEC", new Integer(HAS_SCALE_IF) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "DECIMAL", new Integer(HAS_SCALE_IF) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "DOUBLE PRECISION", new Integer(NO_PRECISION | NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "FLOAT", new Integer(NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "INT", new Integer(NO_PRECISION | NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "INTEGER", new Integer(NO_PRECISION | NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "INTERVAL", new Integer(0) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "LARGE VARBINARY", new Integer(NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "LONG INTEGER", new Integer(NO_PRECISION | NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "LONG VARCHAR", new Integer(NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "MONEY", new Integer(HAS_SCALE_IF) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "NCHAR", new Integer(NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "NUMERIC", new Integer(HAS_SCALE_IF) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "NVARCHAR", new Integer(PRECISION_REQUIRED | NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "REAL", new Integer(NO_PRECISION | NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "ROWID", new Integer(NO_PRECISION | NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "SMALL DATE TIME", new Integer(NO_PRECISION | NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "SMALL FLOAT", new Integer(NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "SMALLINT", new Integer(NO_PRECISION | NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "SMALL INTEGER", new Integer(NO_PRECISION | NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "SMALL MONEY", new Integer(HAS_SCALE_IF) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "TEXT", new Integer(NO_PRECISION | NO_SCALE | NOT_INDEXABLE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "TIME", new Integer(NO_PRECISION | NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "TIMESTAMP", new Integer(NO_PRECISION | NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "VARCHAR", new Integer(NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "VARIABLE BINARY", new Integer(NO_SCALE) }, //NOT LOCALIZABLE, datatypes
            new Object[] { "VARIABLE CHARACTER", new Integer(NO_SCALE) }, //NOT LOCALIZABLE, datatypes//NOT LOCALIZABLE, datatypes
    };

    final String[] validationMessages = { null // any for moment
    };

    final static Boolean[] errorFlags = { null // any for moment
    };
	
    public GenericValidationPlugin() {
    }

    public void initializeDBMSInfo() {
        maxColInConstraint = 32;
        // MS SQL Server allow @ for parameters name
        specialChars = "@"; // NOT LOCALIZABLE
        initReservedWords(reservedWords);
        initPhysNameMaxLen(physNameMaxLen);
        initDataTypes(dataTypes);
    }

    public String[] getValidationMessages() {
        return validationMessages;
    }

    public Boolean[] createErrorFlags() {
        return (Boolean[]) (errorFlags.clone());
    }

    public void getAllObjects(DbObject database) throws DbException {
        databaseList.add(database);

        // --- DataModels ---
        DbORDataModel dataModel = ((DbORDatabase) database).getSchema();
        tableList = getOccurrences(dataModel, DbGETable.metaClass);
        viewList = getOccurrences(dataModel, DbGEView.metaClass);
        columnList = getOccurrences(dataModel, DbGEColumn.metaClass);
        primUniqKeyList = getOccurrences(dataModel, DbGEPrimaryUnique.metaClass);
        foreignKeyList = getOccurrences(dataModel, DbGEForeign.metaClass);
        triggerList = getOccurrences(dataModel, DbGETrigger.metaClass);
        checkList = getOccurrences(dataModel, DbGECheck.metaClass);
        indexList = getOccurrences(dataModel, DbGEIndex.metaClass);

        // --- Procedures ---
        DbOROperationLibrary operationLibrary = ((DbORDatabase) database).getOperationLibrary();
        procedureList = getOccurrences(operationLibrary, DbGEProcedure.metaClass);
        parameterList = getOccurrences(operationLibrary, DbGEParameter.metaClass);

        // --- Domains ---
        DbORDomainModel udtDomainModel = ((DbORDatabase) database).getUdtModel();
        udtDomainList = getOccurrences(udtDomainModel, DbORDomain.metaClass);
        DbORDomainModel defaultDomainModel = ((DbORDatabase) database).getDefaultDomainModel();
        defaultDomainList = getOccurrences(defaultDomainModel, DbORDomain.metaClass);
    }

    public void validatePhysNameOfSpecificConcepts(StringBuffer errorString) throws DbException {
        // no specific method for this moment
    }

    /**
     * Validation of Constraint Physical names Except DB2, the namespace is the database
     */
    public void validateConstraintPhysicalNames(StringBuffer errorString) throws DbException {
        ArrayList constraintList = new ArrayList(primUniqKeyList);
        constraintList.addAll(foreignKeyList);
        constraintList.addAll(checkList);

        validatePhysicalNames(constraintList, errorString, null);
    }

    public void validateForSpecificDBMS(StringBuffer warningString, StringBuffer errorString)
            throws DbException {
        // no specific method for this moment
    }

    //////////////////
    //OVERRIDES Plugin
    public PluginSignature getSignature() {
        return null;
    }

    public Class[] getDatabaseClass() {
        return new Class[] { DbGEDatabase.class };
        
    }
    public final Class[] getSupportedClasses() {
    	return new Class[] { DbGEDatabase.class };
    }
    
	@Override
	public PluginAction getPluginAction() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public OptionGroup getOptionGroup() {
		// TODO Auto-generated method stub
		return null;
	}
    public boolean doListenSelection() { return (this instanceof PluginSelectionListener); }
    
	@Override
	public boolean selectionChanged() throws DbException {
		boolean enabled = false;
        final DbObject[] selectedobjs = PluginServices.getSelectedSemanticalObjects();
        if (selectedobjs != null && selectedobjs.length == 1) {
            	enabled = selectedobjs[0] instanceof DbGEDatabase;
        }
		return enabled;
	}
	
}
