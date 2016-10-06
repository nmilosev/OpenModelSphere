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

package org.modelsphere.sms.or;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.plugins.Plugin;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORModel;
import org.modelsphere.sms.or.db.DbORPackage;
import org.modelsphere.sms.or.db.DbORParameter;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORProcedure;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTrigger;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.db.srtypes.ORDomainCategory;
import org.modelsphere.sms.or.international.LocaleMgr;

/**
 * 
 * Common services for plug-ins that perform validation of relational rules and clean-up of models.
 * 
 */

public abstract class ORValidationPlugin implements Plugin {
    private static final String IS_VALIDATED = org.modelsphere.sms.international.LocaleMgr.db
            .getString("isValidated");

    public ORValidationPlugin() {
    }

    public static final int NO_PRECISION = 0x00000001; // no precision allowed
    public static final int PRECISION_REQUIRED = 0x00000002; // a precision value is required for the column in the CREATE TABLE statement
    public static final int HAS_SCALE_IF = 0x00000004; // has a scale attribute if precision is specified
    public static final int NO_SCALE = 0x00000008; // no scale allowed
    public static final int NO_DEFAULT_VALUE = 0x00000010; // cannot specify a default value in the CREATE TABLE statement

    public static final int NOT_ALLOWED_IN_PRIMARY = 0x00000020; // cannot be indexed in a primary key
    public static final int NOT_ALLOWED_IN_UNIQUE = 0x00000040; // cannot be indexed in an unique key
    public static final int NOT_ALLOWED_IN_FOREIGN = 0x00000080; // cannot be indexed in a foreign key
    public static final int NOT_ALLOWED_IN_INDEX = 0x00000100; // cannot be indexed in a foreign key

    public static final int ONE_PER_TABLE = 0x00000200; // one column of this type per table only
    public static final int NOT_FOR_PARAMETER = 0x00000400; // cannot be associated to Informix parameter
    public static final int LOB_ITEM_REQUIRED = 0x00000800; // must have a lob item associated to Oracle LobStorage. Others cannot
    public static final int AS_LOCATOR_REQUIRED = 0x00001000; // must be setted as locator
    public static final int NO_RETURN_TYPE = 0x00002000; // cannot be a return type for Oracle function
    public static final int FOR_BIT_DATA_REQUIRED = 0x00004000; // must be setted for bit data

    public static final int NOT_INDEXABLE = NOT_ALLOWED_IN_PRIMARY + NOT_ALLOWED_IN_UNIQUE
            + NOT_ALLOWED_IN_FOREIGN + NOT_ALLOWED_IN_INDEX;

    // Messages index
    // CAUTION : Use the printGenericError() method with that following constants.
    public static final int G_INVALID_PHYS_NAME = 4;
    public static final int G_NO_PHYS_NAME = 5;
    public static final int G_PHYS_NAME_TOO_LONG = 6;
    public static final int G_ILLEGAL_CAR = 7;
    public static final int G_RESERVED_WORD_IN = 8;
    public static final int G_ALREADY_USED = 9;
    public static final int G_DOMAIN_INCORRECT = 10;
    public static final int G_NO_DOMAIN_LINKED = 11;
    public static final int G_COLUMN_LENGTH = 12;
    public static final int G_TOO_MANY_COLUMNS = 13;
    public static final int G_COL_IN_CONSTRAINT = 14;
    public static final int G_TRIGGER_NO_BODY = 15;
    public static final int G_PROCEDURE_NO_BODY = 16;
    public static final int G_CHECK_NO_CONDITION = 17;
    public static final int G_VIEW_NO_SELECT_RULE = 18;
    public static final int G_NO_COLUMN = 19;
    public static final int G_ALREADY_USED_CASE = 20;
    public static final int G_DOMAIN_ITSELF = 21;
    public static final int G_NO_VALUE = 22;
    public static final int G_VALUE_ZERO = 23;
    public static final int G_VALUE_FORBIDDEN = 24;
    public static final int G_COLUMN_SCALE = 25;
    public static final int G_NO_GOOD_INTEGER = 26;
    public static final int G_NO_INTEGER = 27;
    public static final int G_PARAMETER_TYPE = 28;

    final String[] genericValidationMessages = {
            LocaleMgr.message.getString("genericValidationMsg0"),
            LocaleMgr.message.getString("genericValidationMsg1"),
            LocaleMgr.message.getString("genericValidationMsg2"),
            LocaleMgr.message.getString("genericValidationMsg3"),
            LocaleMgr.message.getString("genericValidationMsg4"),
            LocaleMgr.message.getString("genericValidationMsg5"),
            LocaleMgr.message.getString("genericValidationMsg6"),
            LocaleMgr.message.getString("genericValidationMsg7"),
            LocaleMgr.message.getString("genericValidationMsg8"),
            LocaleMgr.message.getString("genericValidationMsg9"),
            LocaleMgr.message.getString("genericValidationMsg10"),
            LocaleMgr.message.getString("genericValidationMsg11"),
            LocaleMgr.message.getString("genericValidationMsg12"),
            LocaleMgr.message.getString("genericValidationMsg13"),
            LocaleMgr.message.getString("genericValidationMsg14"),
            LocaleMgr.message.getString("genericValidationMsg15"),
            LocaleMgr.message.getString("genericValidationMsg16"),
            LocaleMgr.message.getString("genericValidationMsg17"),
            LocaleMgr.message.getString("genericValidationMsg18"),
            LocaleMgr.message.getString("genericValidationMsg19"),
            LocaleMgr.message.getString("genericValidationMsg20"),
            LocaleMgr.message.getString("genericValidationMsg21"),
            LocaleMgr.message.getString("genericValidationMsg22"),
            LocaleMgr.message.getString("genericValidationMsg23"),
            LocaleMgr.message.getString("genericValidationMsg24"),
            LocaleMgr.message.getString("genericValidationMsg25"),
            LocaleMgr.message.getString("genericValidationMsg26"),
            LocaleMgr.message.getString("genericValidationMsg27"),
            LocaleMgr.message.getString("genericValidationMsg28") };

    final static Boolean[] genericErrorFlags = { null,//0
            null, null, null, Boolean.FALSE, null,//5
            null, null, null, Boolean.FALSE, Boolean.FALSE,//10
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,//15
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,//20
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,//25
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE };

    public ValidationReport validationReport;
    public static int maxColInConstraint;
    public static String specialChars;
    public HashSet reservedWordsSet;
    public HashMap physicalNameMaxLenMap;
    public HashMap dataTypesMap;
    protected String[] validationMessages;
    protected Boolean[] errorFlags;
    protected int errorCount;
    protected int warningCount;

    public ArrayList databaseList;
    public ArrayList tableList;
    public ArrayList viewList;
    public ArrayList columnList;
    public ArrayList primUniqKeyList;
    public ArrayList foreignKeyList;
    public ArrayList udtDomainList;
    public ArrayList defaultDomainList;
    public ArrayList triggerList;
    public ArrayList checkList;
    public ArrayList indexList;
    public ArrayList procedureList;
    public ArrayList parameterList;

    // Abstract Methods
    public abstract void initializeDBMSInfo();

    public abstract Class[] getDatabaseClass() throws DbException;

    public abstract void getAllObjects(DbObject database) throws DbException;

    public abstract void validateConstraintPhysicalNames(StringBuffer errorString)
            throws DbException;

    public abstract void validatePhysNameOfSpecificConcepts(StringBuffer errorString)
            throws DbException;

    public abstract void validateForSpecificDBMS(StringBuffer warningString,
            StringBuffer errorString) throws DbException;

    public abstract String[] getValidationMessages();

    public abstract Boolean[] createErrorFlags();

    /*****************************************************************************
     * Entry point
     ****************************************************************************/
    public final void execute(ActionEvent ev) throws Exception {
        DbObject[] objects = null;
        try {
            Class claz = Class.forName("org.modelsphere.sms.or.actions.ORActionFactory"); //NOT LOCALIZABLE, unit test
            java.lang.reflect.Method method = claz.getDeclaredMethod("getActiveObjects",
                    new Class[] {}); //NOT LOCALIZABLE, unit test
            objects = (DbObject[]) method.invoke(null, new Object[] {});
            int nbErrors = execute(ev, objects);
            validateObjects(objects, nbErrors);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
        return;
    }

    private int execute(ActionEvent actEvent, DbObject[] objects) throws Exception {
        //DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        objects[0].getDb().beginTrans(Db.READ_TRANS);

        //System.out.println(getSignature());
        initializeValidationUtilities();
        initializeDBMSInfo();
        validationMessages = getValidationMessages();
        errorFlags = createErrorFlags();
        //Object[] clone = null;
        //clone = (Object[])genericErrorFlags.clone();
        Boolean[] errorflags = new Boolean[genericErrorFlags == null ? 0 : genericErrorFlags.length];
        for (int i = 0; i < errorflags.length; i++)
            errorflags[i] = genericErrorFlags[i];
        validationReport = new ValidationReport(validationMessages, errorFlags,
                genericValidationMessages, errorflags);

        validate(objects[0]);

        validationReport.showReport(errorCount, warningCount);
        int nbErrors = errorCount;
        validationReport = null;
        liberateValidationUtilities();
        objects[0].getDb().commitTrans();
        return errorCount;
    }

    private void validateObjects(DbObject[] objects, int nbErrors) {
        try {
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] instanceof DbORModel) {
                    validateModel((DbORModel) objects[i], nbErrors);
                } else {
                    //get the isValidated flag of its class model composite
                    objects[i].getDb().beginReadTrans();
                    DbORModel orModel = (DbORModel) objects[i]
                            .getCompositeOfType(DbORModel.metaClass);
                    String modelName = (orModel == null) ? null : orModel.getName();
                    boolean isValidated = (orModel == null) ? false : orModel.isIsValidated();
                    objects[0].getDb().commitTrans();

                    if (orModel != null) {
                        if (isValidated && (nbErrors > 0)) {
                            validateModel((DbORModel) objects[i], nbErrors);
                        } //end if
                    } //end if
                } //end if
            } //end for
        } catch (DbException ex) {
            //ignore
        } //end try
    } //end validateObjects()

    private void validateModel(DbORModel classModel, int nbErrors) {
        try {
            //get model name
            classModel.getDb().beginReadTrans();
            String modelName = classModel.getName();
            String transName = modelName + ": " + IS_VALIDATED;
            classModel.getDb().commitTrans();

            //set its isValidated flag
            classModel.getDb().beginWriteTrans(transName);
            classModel.setIsValidated((nbErrors > 0) ? Boolean.FALSE : Boolean.TRUE);
            classModel.getDb().commitTrans();
        } catch (DbException ex) {
            //ignore
        } //end try
    } //end validateModel()

    /*****************************************************************************
     * Validation Utility Methods
     ****************************************************************************/
    /**
     * Get the first source of a domain Return itself if their is a recursive source
     */
    public DbORTypeClassifier getOriginType(DbORTypeClassifier type) throws DbException {
        DbORTypeClassifier startType = type;
        DbORTypeClassifier sourceType;
        ArrayList domList = new ArrayList();

        while ((type != null) && (type instanceof DbORDomain)) {
            sourceType = ((DbORDomain) type).getSourceType();
            if ((type == sourceType) || (domList.contains(sourceType)))
                return startType;
            type = ((DbORDomain) type).getSourceType();
            domList.add(type);
        }
        return type;
    }

    /**
     * Because a new instance of XXXvalidation is not create for each execution of validation
     */
    public void initializeValidationUtilities() {
        databaseList = new ArrayList();
        reservedWordsSet = new HashSet();
        physicalNameMaxLenMap = new HashMap();
        dataTypesMap = new HashMap();
        errorCount = 0;
        warningCount = 0;
    }

    public void liberateValidationUtilities() {
        databaseList = null;
        reservedWordsSet = null;
        physicalNameMaxLenMap = null;
        dataTypesMap = null;
    }

    public boolean containAllValidCharacter(String str) {
        boolean result = false;

        for (int i = 0; i < str.length(); i++) {
            result = false;
            char ch = str.charAt(i);

            if (Character.isLowerCase(ch))
                result = true;
            else if (Character.isUpperCase(ch))
                result = true;
            else if (Character.isDigit(ch)) {
                if (i == 0)
                    return false;
                else
                    result = true;
            } else if (ch == '_')
                result = true;
            else if (isSpecialChar(ch))
                result = true;
            else
                return false;
        }
        return result;
    }

    public boolean isSpecialChar(char ch) {
        boolean found = false;

        for (int i = 0; i < specialChars.length(); i++) {
            char spcChar = specialChars.charAt(i);
            if (spcChar == ch)
                return true;
        }
        return found;
    }

    public DbSemanticalObject containsCaseInsensitive(HashMap map, String name) {
        if (name == null)
            return null;
        if (map.containsKey(name))
            return null;
        Iterator iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            if (key.equalsIgnoreCase(name)) {
                DbSemanticalObject semObjFound = (DbSemanticalObject) (map.get(key));
                return semObjFound;
            }
        }
        return null;
    }

    public void initReservedWords(String[] reservedWords) {
        for (int i = 0; i < reservedWords.length; i++) {
            reservedWordsSet.add(reservedWords[i]);
        }
    }

    public void initPhysNameMaxLen(Object[][] physNameMaxLen) {
        for (int i = 0; i < physNameMaxLen.length; i++) {
            physicalNameMaxLenMap.put(physNameMaxLen[i][0], physNameMaxLen[i][1]);
        }
    }

    public void initDataTypes(Object[][] dataTypes) {
        for (int i = 0; i < dataTypes.length; i++) {
            dataTypesMap.put(dataTypes[i][0], dataTypes[i][1]);
        }
    }

    public boolean isTooLong(DbObject dbo) throws DbException {
        DbSemanticalObject semObj = (DbSemanticalObject) dbo;
        Integer maxLen = (Integer) physicalNameMaxLenMap.get(semObj.getMetaClass().getGUIName(
                false, false));
        return (semObj.getPhysicalName().length() > maxLen.intValue());
    }

    public static final DbEnumeration getDbObjectsToValidate(DbObject composite, MetaClass metaclass)
            throws DbException {
        return composite.componentTree(metaclass,
                new MetaClass[] { DbSMSAbstractPackage.metaClass });
    }

    public ArrayList getOccurrences(DbORModel model, MetaClass metaclass) throws DbException {
        ArrayList occurences = new ArrayList();

        if (model == null || metaclass == null)
            return occurences;

        DbEnumeration dbEnum = getDbObjectsToValidate(model, metaclass);
        while (dbEnum.hasMoreElements()) {
            occurences.add(dbEnum.nextElement());
        }
        dbEnum.close();
        return occurences;
    }

    /*****************************************************************************
     * Validations
     ****************************************************************************/

    /**
     * Validation of Physical names
     */
    public void validatePhysicalNamesAux(DbSemanticalObject semObj, StringBuffer buffer,
            HashMap physicalNameMap) throws DbException {
        //HashMap physicalNameMap = new HashMap();
        DbSemanticalObject semObjFound;
        boolean mustBeValidate = true;

        //for (int i = 0;  i < aList.size();  i++){
        //  DbSemanticalObject semObj = (DbSemanticalObject)(aList.get(i));
        String physicalName = semObj.getPhysicalName();
        boolean objectValid = true;
        if (semObj.getMetaClass() == DbORDomain.metaClass) {
            if (((DbORDomain) semObj).getCategory().getValue() == ORDomainCategory.DOMAIN) {
                mustBeValidate = false;
                objectValid = false;
            }
        }

        if (physicalName == null || physicalName.length() == 0) {
            validationReport.printGenericError(G_INVALID_PHYS_NAME, buffer, semObj, true, true,
                    G_NO_PHYS_NAME);
            objectValid = false;
            errorCount++;
        } else if (mustBeValidate == true) {
            if (isTooLong(semObj)) {
                validationReport.printGenericError(G_INVALID_PHYS_NAME, buffer, semObj, true, true,
                        G_PHYS_NAME_TOO_LONG);
                objectValid = false;
                errorCount++;
            }

            if (reservedWordsSet.contains(physicalName)) {
                validationReport.printGenericError(G_INVALID_PHYS_NAME, buffer, semObj, true, true,
                        G_RESERVED_WORD_IN);
                objectValid = false;
                errorCount++;
            }

            if (!containAllValidCharacter(physicalName)) {
                validationReport.printGenericError(G_INVALID_PHYS_NAME, buffer, semObj, true, true,
                        G_ILLEGAL_CAR);
                objectValid = false;
                errorCount++;
            }

            if (physicalNameMap.containsKey(physicalName)) {
                semObjFound = (DbSemanticalObject) (physicalNameMap.get(physicalName));
                if (isQualifiedDuplicateName(semObj, semObjFound)) {
                    validationReport.printGenericError(G_INVALID_PHYS_NAME, buffer, semObj, true,
                            true, G_ALREADY_USED, semObjFound);
                    objectValid = false;
                    errorCount++;
                }
            } else if ((semObjFound = containsCaseInsensitive(physicalNameMap, physicalName)) != null) {
                if (isQualifiedDuplicateName(semObj, semObjFound)) {
                    validationReport.printGenericError(G_INVALID_PHYS_NAME, buffer, semObj, true,
                            true, G_ALREADY_USED_CASE, semObjFound);
                    objectValid = false;
                    errorCount++;
                }
            }
        }
        if (objectValid)
            physicalNameMap.put(physicalName, semObj);
        //}
    }

    public boolean isQualifiedDuplicateName(DbSemanticalObject semObj,
            DbSemanticalObject semObjFound) throws DbException {
        DbORUser userObj = null;
        DbORUser userObjFound = null;

        //DbORAbsTable
        if (semObj instanceof DbORAbsTable && semObjFound instanceof DbORAbsTable) {
            userObj = ((DbORAbsTable) semObj).getUser();
            userObjFound = ((DbORAbsTable) semObjFound).getUser();
        }

        //DbORIndex
        if (semObj instanceof DbORIndex && semObjFound instanceof DbORIndex) {
            userObj = ((DbORIndex) semObj).getUser();
            userObjFound = ((DbORIndex) semObjFound).getUser();
        }

        //DbORDomain
        if (semObj instanceof DbORDomain && semObjFound instanceof DbORDomain) {
            userObj = ((DbORDomain) semObj).getUser();
            userObjFound = ((DbORDomain) semObjFound).getUser();
        }

        //DbORPackage
        if (semObj instanceof DbORPackage && semObjFound instanceof DbORPackage) {
            userObj = ((DbORPackage) semObj).getUser();
            userObjFound = ((DbORPackage) semObjFound).getUser();
        }

        //DbORTrigger
        if (semObj instanceof DbORTrigger && semObjFound instanceof DbORTrigger) {
            userObj = ((DbORTrigger) semObj).getUser();
            userObjFound = ((DbORTrigger) semObjFound).getUser();
        }

        //DbORProcedure
        if (semObj instanceof DbORProcedure && semObjFound instanceof DbORProcedure) {
            userObj = ((DbORProcedure) semObj).getUser();
            userObjFound = ((DbORProcedure) semObjFound).getUser();
        }// end if DbORAbsTable

        // Same name without user
        if (userObj == null && userObjFound == null)
            return true;

        // Same name with same user
        if (userObj != null) {
            String userName = userObj.getName();
            if (userObjFound != null) {
                String userNameFound = userObjFound.getName();
                if (userNameFound.equalsIgnoreCase(userName)) {
                    return true;
                }
            }
        }

        // by default no error with qualified name
        return false;
    }

    /**
     * Validation of Physical names depend on Namespace
     */
    public void validatePhysicalNames(ArrayList aList, StringBuffer buffer, MetaClass metaClass)
            throws DbException {
        HashMap physicalNameMap = new HashMap();

        if (metaClass != null) {
            for (int i = 0; i < aList.size(); i++) {
                DbSemanticalObject semObj = (DbSemanticalObject) (aList.get(i));

                physicalNameMap.clear();
                DbEnumeration dbEnum = semObj.getComponents().elements(metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbSemanticalObject elemSemObj = (DbSemanticalObject) dbEnum.nextElement();
                    validatePhysicalNamesAux(elemSemObj, buffer, physicalNameMap);
                }
                dbEnum.close();
            }
        } else {
            for (int i = 0; i < aList.size(); i++) {
                DbSemanticalObject semObj = (DbSemanticalObject) (aList.get(i));
                validatePhysicalNamesAux(semObj, buffer, physicalNameMap);
            }
        }
    }

    /**
     * Validation of Domain of columns Each column must have a domain
     */
    public void validateDomainOfColumns(ArrayList aList, StringBuffer buffer) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORColumn semObj = (DbORColumn) (aList.get(i));
            if (semObj.getType() == null) {
                validationReport.printGenericError(G_NO_DOMAIN_LINKED, buffer, semObj, true);//printError(11, buffer, composite+getName(column))
                errorCount++;
            }
        }
    }

    /**
     * Validation of length of columns Verify if columns who required length have one. Verify if
     * columns who length is forbiden don't have one. Verify for all if the value is not 0.
     */
    public void validateLengthOfColumns(ArrayList aList, StringBuffer buffer) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORColumn semObj = (DbORColumn) (aList.get(i));
            DbORTypeClassifier type = semObj.getType();
            boolean valueTrapped = false;

            if (type instanceof DbORDomain)
                type = getOriginType(type);

            if ((type != null) && (dataTypesMap.containsKey(type.getName()))) {
                Integer flag = (Integer) dataTypesMap.get(type.getName());
                if (((flag.intValue() & PRECISION_REQUIRED) == PRECISION_REQUIRED)
                        && (semObj.getLength() == null)) {
                    validationReport.printGenericError(G_COLUMN_LENGTH, buffer, semObj, true, true,
                            G_NO_VALUE);
                    errorCount++;
                } else if (((flag.intValue() & NO_PRECISION) == NO_PRECISION)
                        && (semObj.getLength() != null)) {
                    validationReport.printGenericError(G_COLUMN_LENGTH, buffer, semObj, true, true,
                            G_VALUE_FORBIDDEN);
                    errorCount++;
                    valueTrapped = true;
                }
            }
            if (!valueTrapped) {
                if ((semObj.getLength() != null) && (semObj.getLength().intValue() == 0)) {
                    validationReport.printGenericError(G_COLUMN_LENGTH, buffer, semObj, true, true,
                            G_VALUE_ZERO);
                    errorCount++;
                }
            }
        }
    }

    /**
     * Validation of scale of columns (Nbr. decimal) Verify if columns who have scale required
     * length have one. Verify if columns who scale is forbiden don't have one. Verify for all if
     * the value is not 0.
     */
    public void validateScaleOfColumns(ArrayList aList, StringBuffer buffer) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORColumn semObj = (DbORColumn) (aList.get(i));
            DbORTypeClassifier type = semObj.getType();
            boolean valueTrapped = false;
            boolean hasValidLength = false;

            if ((semObj.getLength() != null) && (semObj.getLength().intValue() != 0)) {
                hasValidLength = true;
            }

            if (type instanceof DbORDomain)
                type = getOriginType(type);

            if ((type != null) && (dataTypesMap.containsKey(type.getName()))) {
                Integer flag = (Integer) dataTypesMap.get(type.getName());
                if (semObj.getNbDecimal() != null) {
                    if ((((flag.intValue() & HAS_SCALE_IF) == HAS_SCALE_IF) && (!hasValidLength))
                            || ((flag.intValue() & NO_SCALE) == NO_SCALE)) {
                        validationReport.printGenericError(G_COLUMN_SCALE, buffer, semObj, true,
                                true, G_VALUE_FORBIDDEN);
                        errorCount++;
                        valueTrapped = true;
                    }
                }
            }
            if (!valueTrapped) {
                // GP Disabled:  0 is permitted in most DBMS for most types
                //if ((semObj.getNbDecimal() != null) && (semObj.getNbDecimal().intValue() == 0)){
                //  validationReport.printError(COLUMN_SCALE, buffer, semObj , true, true, VALUE_ZERO);
                //  errorCount++;
                //}
            }
        }
    }

    /**
     * Validation of Columns of tables Each table must have at least one column
     */
    public void validateColumnsOfTables(ArrayList aList, StringBuffer buffer) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORTable semObj = (DbORTable) (aList.get(i));
            int nbCol = 0;

            DbEnumeration dbEnum = semObj.getComponents().elements();
            while (dbEnum.hasMoreElements()) {
                if (dbEnum.nextElement() instanceof DbORColumn)
                    nbCol++;
            }
            dbEnum.close();

            if (nbCol == 0) {
                validationReport.printGenericError(G_NO_COLUMN, buffer, semObj, false);//printError(23, buffer, getName(table))
                errorCount++;
            }
        }
    }

    /**
     * Validation of Columns of combinations Verify if each combinaison doesnt have more columns
     * than the maximum allowed
     */
    public void validateColumnsOfCombinations(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbSemanticalObject semObj = (DbSemanticalObject) (aList.get(i));
            int size = 0;

            if (semObj instanceof DbORCheck)
                size = 1;
            else if (semObj instanceof DbORPrimaryUnique)
                size = ((DbORPrimaryUnique) semObj).getColumns().size();
            else
                // DbORIndex and DbORForeign
                size = semObj.getComponents().size();

            if (size > maxColInConstraint) {
                validationReport.printGenericError(G_TOO_MANY_COLUMNS, buffer, semObj, true);//printError(15, buffer, composite+comb.getName())
                errorCount++;
            }
        }
    }

    /**
     * Validation of source type of domains Verify if the source type of each domain is valid
     */
    public void validateSourceTypeOfDomains(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORDomain semObj = (DbORDomain) (aList.get(i));
            if (semObj.getSourceType() == null) {
                if ((semObj.getCategory() == null)
                        || (semObj.getCategory().getValue() == ORDomainCategory.DOMAIN)) {
                    validationReport.printError(G_DOMAIN_INCORRECT, buffer, semObj, false);//printError(10, buffer, getName(domain))
                    errorCount++;
                }
            } else if (this.getOriginType(semObj) == semObj) {
                validationReport.printGenericError(G_DOMAIN_INCORRECT, buffer, semObj, true, true,
                        G_DOMAIN_ITSELF);
                errorCount++;
            }
        }
    }

    /**
     * Validation of columns allowed in constraints
     */
    public void validateColumnsAllowedInConstraints(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORColumn semObj = (DbORColumn) (aList.get(i));
            DbORTypeClassifier type = semObj.getType();

            if (type instanceof DbORDomain)
                type = getOriginType(type);

            /*
             * while ((type != null) && (type instanceof DbORDomain)){ if (type ==
             * ((DbORDomain)type).getSourceType()) break; type = ((DbORDomain)type).getSourceType();
             * }
             */

            /* ATTENTION----> pas d'index sur les colonnes liées à un domaine pour ORACLE */

            if ((type != null) && (dataTypesMap.containsKey(type.getName()))) {
                Integer flag = (Integer) (dataTypesMap.get(type.getName()));
                if ((semObj.getPrimaryUniques().size() > 0)
                        && (((flag.intValue() & NOT_ALLOWED_IN_PRIMARY) == NOT_ALLOWED_IN_PRIMARY)
                                || ((flag.intValue() & NOT_ALLOWED_IN_UNIQUE) == NOT_ALLOWED_IN_UNIQUE) || ((flag
                                .intValue() & NOT_INDEXABLE) == NOT_INDEXABLE))) {
                    validationReport.printGenericError(G_COL_IN_CONSTRAINT, buffer, semObj, true);//printError(16, buffer, composite+getName(column))
                    errorCount++;
                } else if (((semObj.getFKeyColumns().size() > 0) || (semObj.getDestFKeyColumns()
                        .size() > 0))
                        && (((flag.intValue() & NOT_ALLOWED_IN_FOREIGN) == NOT_ALLOWED_IN_FOREIGN) || ((flag
                                .intValue() & NOT_INDEXABLE) == NOT_INDEXABLE))) {
                    validationReport.printGenericError(G_COL_IN_CONSTRAINT, buffer, semObj, true);//printError(16, buffer, composite+getName(column))
                    errorCount++;
                } else if ((semObj.getIndexKeys().size() > 0)
                        && (((flag.intValue() & NOT_ALLOWED_IN_INDEX) == NOT_ALLOWED_IN_INDEX) || ((flag
                                .intValue() & NOT_INDEXABLE) == NOT_INDEXABLE))) {
                    validationReport.printGenericError(G_COL_IN_CONSTRAINT, buffer, semObj, true);//printError(16, buffer, composite+getName(column))
                    errorCount++;
                }
            }
        }
    }

    /**
     * Validation of intructions of Triggers Each trigger must have instructions
     */
    public void validateIntructionsOfTriggers(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORTrigger semObj = (DbORTrigger) (aList.get(i));
            if ((semObj.getBody() == null) || (semObj.getBody().length() == 0)) {
                validationReport.printGenericError(G_TRIGGER_NO_BODY, buffer, semObj, true);//printError(17, buffer, composite+getName(trigger))
                warningCount++;
            }
        }
    }

    /**
     * Validation of intructions of Procedure Each Procedure must have instructions
     */
    public void validateInstructionsOfProcedures(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORProcedure semObj = (DbORProcedure) (aList.get(i));
            if ((semObj.getBody() == null) || (semObj.getBody().length() == 0)) {
                validationReport.printGenericError(G_PROCEDURE_NO_BODY, buffer, semObj, true);//printError(18, buffer, composite+getName(procedure))
                warningCount++;
            }
        }
    }

    /**
     * Validation of parameters Each parameter must have linked to a type
     */
    public void validateParameterWithoutType(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORParameter semObj = (DbORParameter) (aList.get(i));
            if (semObj.getType() == null) {
                validationReport.printGenericError(G_PARAMETER_TYPE, buffer, semObj, true);//printError(18, buffer, composite+getName(procedure))
                //validationReport.printGenericError(G_NO_DOMAIN_LINKED, buffer, semObj , true);//printError(11, buffer, composite+getName(column))
                errorCount++;
            }
        }
    }

    /**
     * Validation of condition of Check constraint Each check constraint must have a condition
     */
    public void validateInstructionsOfCheckConstraints(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORCheck semObj = (DbORCheck) (aList.get(i));
            if ((semObj.getCondition() == null) || (semObj.getCondition().length() == 0)) {
                validationReport.printGenericError(G_CHECK_NO_CONDITION, buffer, semObj, true);//printError(19, buffer, composite+getName(check))
                warningCount++;
            }
        }
    }

    /**
     * Validation of Selection rule of View Each view must have a Selection rule
     */
    public void validateSelectionRuleOfViews(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORView semObj = (DbORView) (aList.get(i));
            if ((semObj.getSelectionRule() == null) || (semObj.getSelectionRule().length() == 0)) {
                validationReport.printGenericError(G_VIEW_NO_SELECT_RULE, buffer, semObj, true);//printError(20, buffer, composite+getName(view))
                warningCount++;
            }
        }
    }

    public final String installAction(DefaultMainFrame frame, MainFrameMenu menuManager) {
        return null;
    }



    public final void validate(DbObject database) throws DbException {

        getAllObjects(database);
        validateForAllDBMS(validationReport.warningString, validationReport.errorString);
        validateForSpecificDBMS(validationReport.warningString, validationReport.errorString);
    }

    public void validateForAllDBMS(StringBuffer warningString, StringBuffer errorString)
            throws DbException {
        ArrayList tableAndViews = new ArrayList(tableList);
        tableAndViews.addAll(viewList);

        ArrayList constraintList = new ArrayList(primUniqKeyList);
        constraintList.addAll(foreignKeyList);
        constraintList.addAll(checkList);

        ArrayList constraintAndIndexes = new ArrayList(constraintList);
        constraintAndIndexes.addAll(indexList);

        ArrayList domainList = new ArrayList(udtDomainList);
        domainList.addAll(defaultDomainList);

        // --- Errors ---
        validatePhysicalNames(tableAndViews, errorString, null);
        validatePhysicalNames(tableList, errorString, DbORColumn.metaClass);
        validatePhysicalNames(viewList, errorString, DbORColumn.metaClass);
        validatePhysicalNames(triggerList, errorString, null);
        validatePhysicalNames(indexList, errorString, null);
        validatePhysicalNames(procedureList, errorString, null);
        validatePhysicalNames(procedureList, errorString, DbORParameter.metaClass);
        validatePhysicalNames(domainList, errorString, null);
        validatePhysicalNames(databaseList, errorString, null);
        validateConstraintPhysicalNames(errorString);
        validatePhysNameOfSpecificConcepts(errorString);

        validateDomainOfColumns(columnList, errorString);
        validateColumnsOfTables(tableList, errorString);
        validateColumnsOfCombinations(constraintAndIndexes, errorString);
        validateSourceTypeOfDomains(domainList, errorString);
        validateColumnsAllowedInConstraints(columnList, errorString);
        validateLengthOfColumns(columnList, errorString);
        validateScaleOfColumns(columnList, errorString);
        validateParameterWithoutType(parameterList, errorString);

        // --- Warning ---
        validateIntructionsOfTriggers(triggerList, warningString);
        validateInstructionsOfCheckConstraints(checkList, warningString);
        validateInstructionsOfProcedures(procedureList, warningString);
        validateSelectionRuleOfViews(viewList, warningString);
    }
    
    @Override
    public boolean doListenSelection() {
        return false;
    }
}
