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

package org.modelsphere.plugins.integrity;

import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.sms.or.db.*;

/**
 * @author pierrem
 * 
 */
public abstract class AbstractORIntegrity extends AbstractIntegrity {

    static final String kDataModel = LocaleMgr.misc.getString("DM-DataModel");

    /*
     * ORIntegrity
     */

    //identifiants d'action des solutions
    static final String COLUMN_SET_NOT_NULL = "COLUMN_SET_NOT_NULL"; // NOT LOCALIZABLE 
    static final String FK_COLUMNS_SET_NULL = "FK_COLUMNS_SET_NULL"; // NOT LOCALIZABLE 
    static final String FK_COLUMNS_SET_NOT_NULL = "FK_COLUMNS_SET_NOT_NULL"; // NOT LOCALIZABLE
    static final String FK_MULTIPLICITY_SET_MIN_1 = "FK_MULTIPLICITY_SET_MIN_1"; // NOT LOCALIZABLE  
    static final String FK_MULTIPLICITY_SET_MIN_0 = "FK_MULTIPLICITY_SET_MIN_0"; // NOT LOCALIZABLE  
    static final String FK_MULTIPLICITY_SET_MAX_N = "FK_MULTIPLICITY_SET_MAX_N"; // NOT LOCALIZABLE  
    static final String FK_CREATE_UNIQUE_INDEX = "FK_CREATE_UNIQUE_INDEX"; // NOT LOCALIZABLE  
    static final String FK_MULTIPLICITY_SET_MAX_1 = "FK_MULTIPLICITY_SET_MAX_1"; // NOT LOCALIZABLE  
    static final String FK_CREATE_NON_UNIQUE_INDEX = "FK_CREATE_NON_UNIQUE_INDEX"; // NOT LOCALIZABLE  

    //Messages des solutions d'intégrité
    static final String kSetColNotNull = LocaleMgr.misc.getString("DM-IS_SetColNotNull");
    static final String kSetFKColNotNull = LocaleMgr.misc.getString("DM-IS_SetFKColNotNull");
    static final String kSetFKColNull = LocaleMgr.misc.getString("DM-IS_SetFKColNull");
    static final String kSetMulti01 = LocaleMgr.misc.getString("DM-IS_SetMulti01");
    static final String kCreateIndex0 = LocaleMgr.misc.getString("DM-IS_CreateIndex0");
    static final String kUpdateUnicityIndex01 = LocaleMgr.misc
            .getString("DM-IS_UpdateUnicityIndex01");

    //Messages des règles d'intégrité et messages extra
    public static final int OR_IR_COLUMN_NOT_NULL = 0;
    public static final int OR_IR_CHILD_MIN_0123 = 1;
    public static final int OR_IX_CHILD_MIN_0123 = 2;
    public static final int OR_IR_CHILD_MIN2_0123 = 3;
    public static final int OR_IR_PARENT_MAX1012 = 4;
    public static final int OR_IX_PARENT_MAX = 5;
    public static final int OR_IR_PARENT_MAXN012 = 6;
    public static final int OR_IR_TRIGGER_WITHOUT_COL_BODY = 7;
    public static final int OR_IR_NO_UPDATE_RULE = 8;
    public static final int OR_IR_MULTIPLICITY_UNDEFINED = 9;
    public static final int OR_IR_BAD_ASSOCIATION = 10;
    public static final int OR_IR_FK_WITHOUT_COL = 11;
    public static final int OR_IR_PK_WITHOUT_DIR_COL = 12;
    public static final int OR_IR_INDEX_WITHOUT_ELEMENTS = 13;
    public static final int OR_IR_FK_COL_DIFFERENT = 14;
    public static final int OR_IX_DIFFERENT_DOMAIN = 15;
    public static final int OR_IX_DIFFERENT_LENGTH = 16;
    public static final int OR_IX_DIFFERENT_NB_DEC = 17;
    public static final int OR_IR_DOMAIN_WITHOUT_TYPE = 18;
    public static final int OR_IR_COMMON_ITEM_WITHOUT_TYPE = 19;
    public static final int OR_IR_PARAMETER_WITHOUT_TYPE = 20;
    public static final int OR_IR_PROCEDURE_WITHOUT_BODY = 21;
    public static final int OR_IR_TABLE_WITHOUT_CONNECTOR = 22;
    public static final int OR_IR_TABLE_WITHOUT_COLUMN = 23;

    final static String[] orIntegrityMessages = {
            //LocaleMgr.misc.getString("Les colonnes suivantes ne devraient pas être nulles. --> font partie d'une clé primaire/unique"),
            LocaleMgr.misc.getString("DM-IR_PKUKColNotNull012"),//0
            LocaleMgr.misc.getString("DM-IR_ChildMin0123"),
            LocaleMgr.misc.getString("DM-IX_ChildMin0123"),
            LocaleMgr.misc.getString("DM-IR_ChildMin20123"),
            LocaleMgr.misc.getString("DM-IR_ParentMax1012"),
            LocaleMgr.misc.getString("DM-IX_ParentMax"), //5
            LocaleMgr.misc.getString("DM-IR_ParentMaxN012"),
            LocaleMgr.misc.getString("DM-GR_TriggerWithoutBodyAndCol"),
            LocaleMgr.misc.getString("DM-IR_NoUpdateRule"),
            LocaleMgr.misc.getString("DM-IR_MultiplicityUndefined"),
            LocaleMgr.misc.getString("DM-IR_BadAssociation"), //10
            LocaleMgr.misc.getString("DM-GR_ForeignWithoutCol"),
            LocaleMgr.misc.getString("DM-GR_PrimaryWithoutDirCol"),
            LocaleMgr.misc.getString("DM-GR_IndexWithoutKey"),
            LocaleMgr.misc.getString("DM-IR_FKColDiffer"),
            LocaleMgr.misc.getString("DM-IX_DifferentDomain"), //15
            LocaleMgr.misc.getString("DM-IX_DifferentLength"),
            LocaleMgr.misc.getString("DM-IX_DifferentnbDec"),
            LocaleMgr.misc.getString("DM-GR_DomainWithoutType"),
            LocaleMgr.misc.getString("DM-GR_CommonItemWithoutType"),
            LocaleMgr.misc.getString("DM-GR_ParameterWithoutType"), //20 
            LocaleMgr.misc.getString("DM-GR_ProcedureWithoutBody"),
            LocaleMgr.misc.getString("DM-GR_TableWithoutConnector"),
            LocaleMgr.misc.getString("DM-GR_TableWithoutColumn"), };

    final static Boolean[] orIntegrityErrorFlags = { Boolean.FALSE,//0
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,//5
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,//10
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,//15
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,//20
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, };

    /*
     * ORCleanup
     */
    //ruleIndex spécifiques à ORCleanup 

    public static final int OR_CR_TABLE_WITHOUT_CONNECTOR = 0;
    public static final int OR_CR_TABLE_WITHOUT_COLUMN = 1;
    public static final int OR_CR_PK_WITHOUT_DIR_COL = 2;
    public static final int OR_CR_FK_WITHOUT_COL = 3;
    public static final int OR_CR_CHECK_WITHOUT_COL = 4;
    public static final int OR_CR_INDEX_WITHOUT_ELEMENTS = 5;
    public static final int OR_CR_TRIGGER_WITHOUT_COL_BODY = 6;
    public static final int OR_CR_DOMAIN_WITHOUT_TYPE = 7;
    public static final int OR_CR_DOMAIN_NOT_USED = 8;
    public static final int OR_CR_COMMON_ITEM_WITHOUT_TYPE = 9;
    public static final int OR_CR_COMMON_ITEM_WITHOUT_COL = 10;
    public static final int OR_CR_PROCEDURE_WITHOUT_BODY = 11;
    public static final int OR_CR_PARAMETER_WITHOUT_TYPE = 12;

    public static final int OR_CR_NB_RULES = 13;//pour arrayList 

    final static String[] orCleanUpMessages = {
            LocaleMgr.misc.getString("DM-GR_TableWithoutConnector"),
            LocaleMgr.misc.getString("DM-GR_TableWithoutColumn"),
            LocaleMgr.misc.getString("DM-GR_PrimaryWithoutDirCol"),
            LocaleMgr.misc.getString("DM-GR_ForeignWithoutCol"),
            LocaleMgr.misc.getString("DM-CR_CheckWithoutCol"),
            LocaleMgr.misc.getString("DM-GR_IndexWithoutKey"),
            LocaleMgr.misc.getString("DM-GR_TriggerWithoutBodyAndCol"),
            LocaleMgr.misc.getString("DM-GR_DomainWithoutType"),
            LocaleMgr.misc.getString("DM-CR_DomainNotUsed"),
            LocaleMgr.misc.getString("DM-GR_CommonItemWithoutType"),
            LocaleMgr.misc.getString("DM-CR_CommonItemWithoutColumn"),
            LocaleMgr.misc.getString("DM-GR_ProcedureWithoutBody"),
            LocaleMgr.misc.getString("DM-GR_ParameterWithoutType") };

    final static Boolean[] orCleanUpErrorFlags = { Boolean.FALSE,//0
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,//5
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,//10
            Boolean.FALSE, Boolean.FALSE, };

    final static String[] orCleanUpSectionLinkStr = { "DELETE_TABLE_WITHOUT_CONNECTOR", // NOT LOCALIZABLE
            "DELETE_TABLE_WITHOUT_COLUMN", // NOT LOCALIZABLE
            "DELETE_PK_WITHOUT_DIR_COL", // NOT LOCALIZABLE
            "DELETE_FK_WITHOUT_COL", // NOT LOCALIZABLE    
            "DELETE_CHECK_WITHOUT_COL", // NOT LOCALIZABLE 
            "DELETE_INDEX_WITHOUT_KEY", // NOT LOCALIZABLE
            "DELETE_TRIGGER_WITHOUT_COL_BODY", // NOT LOCALIZABLE
            "DELETE_DOMAIN_WITHOUT_TYPE", // NOT LOCALIZABLE
            "DELETE_DOMAIN_NOT_USED", // NOT LOCALIZABLE
            "DELETE_COMMON_ITEM_WITHOUT_TYPE", // NOT LOCALIZABLE
            "DELETE_COMMON_ITEM_WITHOUT_COL", // NOT LOCALIZABLE
            "DELETE_PROCEDURE_WITHOUT_BODY", // NOT LOCALIZABLE 
            "DELETE_PARAMETER_WITHOUT_TYPE", // NOT LOCALIZABLE 
    };

    /*
     * ER Integrity
     */

    //Messages des règles d'intégrité et messages extra
    public static final int ER_IR_COLUMN_NOT_NULL = 0;
    public static final int ER_IR_MULTIPLICITY_UNDEFINED = 1;
    public static final int ER_IR_PK_WITHOUT_DIR_COL = 2;
    public static final int ER_IR_ENTITY_WITHOUT_ARC = 3;
    public static final int ER_IR_ENTITY_WITHOUT_ATTRIBUTE = 4;
    public static final int ER_IR_ENTITY_WITHOUT_KEY = 5;
    public static final int ER_IR_TERNARY_RELATION_WITH_DEP = 6;
    public static final int ER_IR_BINARY_WITH_TOO_MANY_DEP = 7;
    public static final int ER_IR_RELATIONSHIP_LESS_2ARCS = 8;
    public static final int ER_IR_RECURSIVE_WITH_DEPENDENCE = 9;
    public static final int ER_IR_RELATIONSHIP_WITH_ATTR_AND_1_1 = 10;
    public static final int ER_IR_DEPENDENCE_ON_ARC_NOT_1_1 = 11;
    public static final int ER_IR_RELATIONSHIP_BAD_NAVIGABILITY = 12;
    public static final int ER_IX_NO_ROLE_NAVIGABLE = 13;
    public static final int ER_IX_BOTH_ROLE_NAVIGABLE = 14;
    public static final int ER_IX_MAXN_ROLE_NAVIGABLE = 15;
    public static final int ER_IX_DEP_ALONE_WITH_OPPOSITE_MAXN = 16;
    public static final int ER_IX_DEP_NOT_ALONE_WITH_OPPOSITE_MAX1 = 17;

    final static String[] erIntegrityMessages = {
            LocaleMgr.misc.getString("ER-IR_PKUKColNotNull012"),//0
            LocaleMgr.misc.getString("ER-IR_MultiplicityUndefined"),
            LocaleMgr.misc.getString("ER-GR_PrimaryWithoutDirCol"),
            LocaleMgr.misc.getString("ER-GR_EntityWithoutArc"),
            LocaleMgr.misc.getString("ER-GR_EntityWithoutAttribute"),
            LocaleMgr.misc.getString("ER-IR_EntityWithoutKey"),//5
            LocaleMgr.misc.getString("ER-IR_TernaryRelationWithDep"),
            LocaleMgr.misc.getString("ER-IR_BinaryRelationWithTooManyDep"),
            LocaleMgr.misc.getString("ER-IR_RelationshipLessTwoArcs"),
            LocaleMgr.misc.getString("ER-IR_RecursiveRelationWithDep"),
            LocaleMgr.misc.getString("ER-IR_RelationshipWithAttrAnd11Mult"),//10
            LocaleMgr.misc.getString("ER-IR_DependenceOnArcNotExactlyOne"),
            LocaleMgr.misc.getString("ER-IR_RelationWithBadNavigability"),
            LocaleMgr.misc.getString("ER-IX_NoRoleNavigable"),
            LocaleMgr.misc.getString("ER-IX_BothRoleNavigable"),
            LocaleMgr.misc.getString("ER-IX_MaxNRoleNavigable"),//15
            LocaleMgr.misc.getString("ER-IR_DepAloneWithOppositeMaxN"),
            LocaleMgr.misc.getString("ER-IR_DepNotAloneWithOppositeMax1"),

    };

    final static Boolean[] erIntegrityErrorFlags = { Boolean.FALSE,//0
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,//5
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,//10
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,//15
            Boolean.FALSE, Boolean.FALSE,

    };

    /*
     * ERCleanup
     */
    //ruleIndex spécifiques à ERCleanup 

    public static final int ER_CR_ENTITY_WITHOUT_ATTRIBUTE = 0;
    public static final int ER_CR_ENTITY_WITHOUT_ARC = 1;
    public static final int ER_CR_PK_WITHOUT_DIR_COL = 2;
    public static final int ER_CR_RELATIONSHIP_LESS_2_ARCS = 3;

    public static final int ER_CR_NB_RULES = 4;//pour arrayList 

    final static String[] erCleanUpMessages = {
            LocaleMgr.misc.getString("ER-GR_EntityWithoutAttribute"),
            LocaleMgr.misc.getString("ER-GR_EntityWithoutArc"),
            LocaleMgr.misc.getString("ER-GR_PrimaryWithoutDirCol"),
            LocaleMgr.misc.getString("ER-IR_RelationshipLessTwoArcs"), };

    final static Boolean[] erCleanUpErrorFlags = { Boolean.FALSE,//0
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, };

    final static String[] erCleanUpSectionLinkStr = { "DELETE_ENTITY_WITHOUT_ATTRIBUTE", // NOT LOCALIZABLE
            "DELETE_ENTITY_WITHOUT_ARC", // NOT LOCALIZABLE
            "DELETE_PK_WITHOUT_DIR_COL", // NOT LOCALIZABLE
            "DELETE_RELATION_LESS_2ARC", // NOT LOCALIZABLE    
    };

    protected ArrayList databaseList;
    protected ArrayList absTableList;
    protected ArrayList tableList;
    protected ArrayList entityList;
    protected ArrayList erAssociationList;
    protected ArrayList entityAssociationList;
    protected ArrayList arcList;
    protected ArrayList erRoleList;
    protected ArrayList viewList;
    protected ArrayList columnList;
    protected ArrayList primUniqKeyList;
    protected ArrayList foreignKeyList;
    protected ArrayList udtDomainList;
    protected ArrayList defaultDomainList;
    protected ArrayList triggerList;
    protected ArrayList checkList;
    protected ArrayList indexList;
    protected ArrayList procedureList;
    protected ArrayList parameterList;
    protected ArrayList domainList;
    protected ArrayList commonItemList;
    protected ArrayList associationList;
    protected ArrayList roleList;
    protected ArrayList fKeyColumnList;

    public void getAllObjects(DbObject model, int modelType) throws DbException {
        switch (modelType) {
        case AbstractIntegrity.OR_DATAMODEL:
            tableList = getOccurrences(model, DbORTable.metaClass);
            viewList = getOccurrences(model, DbORView.metaClass);
            columnList = getOccurrences(model, DbORColumn.metaClass);
            primUniqKeyList = getOccurrences(model, DbORPrimaryUnique.metaClass);
            foreignKeyList = getOccurrences(model, DbORForeign.metaClass);
            triggerList = getOccurrences(model, DbORTrigger.metaClass);
            checkList = getOccurrences(model, DbORCheck.metaClass);
            indexList = getOccurrences(model, DbORIndex.metaClass);
            associationList = getOccurrences(model, DbORAssociation.metaClass);
            roleList = getOccurrences(model, DbORAssociationEnd.metaClass);
            fKeyColumnList = getOccurrences(model, DbORFKeyColumn.metaClass);
            //fusions
            absTableList = new ArrayList(tableList);
            absTableList.addAll(viewList);
            break;

        case AbstractIntegrity.DOMAIN_MODEL:
            domainList = getOccurrences(model, DbORDomain.metaClass);
            break;

        case AbstractIntegrity.COMMONITEM_MODEL:
            commonItemList = getOccurrences(model, DbORCommonItem.metaClass);
            break;

        case AbstractIntegrity.OPERATION_LIBRARY:
            procedureList = getOccurrences(model, DbORProcedure.metaClass);
            parameterList = getOccurrences(model, DbORParameter.metaClass);
            break;

        case AbstractIntegrity.ER_DATAMODEL:
            entityList = getEntitiesAssociations(model, DbORTable.metaClass, false);
            erAssociationList = getEntitiesAssociations(model, DbORTable.metaClass, true);
            columnList = getOccurrences(model, DbORColumn.metaClass);
            primUniqKeyList = getOccurrences(model, DbORPrimaryUnique.metaClass);
            arcList = getOccurrences(model, DbORAssociation.metaClass);
            erRoleList = getErRoles(model, DbORAssociationEnd.metaClass);
            //fusions
            entityAssociationList = new ArrayList(entityList);
            entityAssociationList.addAll(erAssociationList);
            break;

        default:
            break;
        }
    }

    /**********************************************************************************
     * MÉTHODES UTILISÉES POUR LES MODÈLES RELATIONNELS (INTÉGRITÉ ET ÉPURATION)
     **********************************************************************************/

    /**
     *  
     */
    protected void verifyTriggerWithoutBodyAndCol(ArrayList aList, StringBuffer buffer,
            int operation) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            boolean haveColumnLinked = true;
            DbORTrigger semObj = (DbORTrigger) (aList.get(i));
            DbEnumeration columns = semObj.getColumns().elements();
            if (!columns.hasMoreElements()) {
                haveColumnLinked = false;
            }
            columns.close();

            if ((semObj.getBody() == null) && (!haveColumnLinked)) {
                if (operation == INTEGRITY) {
                    modelIntegrityReport.printError(OR_IR_TRIGGER_WITHOUT_COL_BODY, buffer, semObj,
                            true);
                } else {
                    modelIntegrityReport.printError(OR_CR_TRIGGER_WITHOUT_COL_BODY, buffer, semObj,
                            true);
                    occurencesToClean[OR_CR_TRIGGER_WITHOUT_COL_BODY].add(semObj);
                }
                modelWarningCount++;
            }
        }
    }

    /**
     *  
     */
    protected void verifyForeignkeyWithoutColumn(ArrayList aList, StringBuffer buffer, int operation)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORForeign semObj = (DbORForeign) (aList.get(i));
            DbEnumeration foreignKeyCols = semObj.getComponents()
                    .elements(DbORFKeyColumn.metaClass);
            if (!foreignKeyCols.hasMoreElements()) {
                if (operation == INTEGRITY) {
                    modelIntegrityReport.printError(OR_IR_FK_WITHOUT_COL, buffer, semObj, true);
                } else {
                    modelIntegrityReport.printError(OR_CR_FK_WITHOUT_COL, buffer, semObj, true);
                    occurencesToClean[OR_CR_FK_WITHOUT_COL].add(semObj);
                }
                reportError(semObj);
            }
            foreignKeyCols.close();
        }
    }

    /**
     *  
     */
    protected void verifyIndexWithoutIndexKey(ArrayList aList, StringBuffer buffer, int operation)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            int nbIndexKeys = 0;
            boolean noElementAndNoExpression = false;
            DbORIndex semObj = (DbORIndex) (aList.get(i));
            DbEnumeration indexKeys = semObj.getComponents().elements(DbORIndexKey.metaClass);
            while (indexKeys.hasMoreElements()) {
                DbORIndexKey indexKey = (DbORIndexKey) indexKeys.nextElement();
                if (indexKey.getIndexedElement() == null && indexKey.getExpression() == null) {
                    noElementAndNoExpression = true;
                }
                nbIndexKeys++;
            }
            indexKeys.close();

            if (nbIndexKeys == 0 || noElementAndNoExpression) {
                if (operation == INTEGRITY) {
                    modelIntegrityReport.printError(OR_IR_INDEX_WITHOUT_ELEMENTS, buffer, semObj,
                            true);
                } else {
                    modelIntegrityReport.printError(OR_CR_INDEX_WITHOUT_ELEMENTS, buffer, semObj,
                            true);
                    occurencesToClean[OR_CR_INDEX_WITHOUT_ELEMENTS].add(semObj);
                }

                reportError(semObj);
            }
        }
    }

    /**
     * Domaines sans Type
     */
    protected void verifyDomainWithoutType(ArrayList aList, StringBuffer buffer, int operation)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORDomain semObj = (DbORDomain) (aList.get(i));
            if (semObj.getSourceType() == null) {
                if (operation == INTEGRITY) {
                    modelIntegrityReport.printError(OR_IR_DOMAIN_WITHOUT_TYPE, buffer, semObj,
                            false);
                } else {
                    modelIntegrityReport.printError(OR_CR_DOMAIN_WITHOUT_TYPE, buffer, semObj,
                            false);
                    occurencesToClean[OR_CR_DOMAIN_WITHOUT_TYPE].add(semObj);
                }

                reportError(semObj);
            }
        }
    }

    /**
     * Item commun sans type
     */
    protected void verifyCommonItemWithoutType(ArrayList aList, StringBuffer buffer, int operation)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORCommonItem semObj = (DbORCommonItem) (aList.get(i));
            if (semObj.getType() == null) {
                if (operation == INTEGRITY) {
                    modelIntegrityReport.printError(OR_IR_COMMON_ITEM_WITHOUT_TYPE, buffer, semObj,
                            false);
                } else {
                    modelIntegrityReport.printError(OR_CR_COMMON_ITEM_WITHOUT_TYPE, buffer, semObj,
                            false);
                    occurencesToClean[OR_CR_COMMON_ITEM_WITHOUT_TYPE].add(semObj);
                }

                reportError(semObj);
            }
        }
    }

    /**
     * Parametres sans type
     */
    protected void verifyParameterWithoutType(ArrayList aList, StringBuffer buffer, int operation)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORParameter semObj = (DbORParameter) (aList.get(i));
            if (semObj.getType() == null) {
                if (operation == INTEGRITY) {
                    modelIntegrityReport.printError(OR_IR_PARAMETER_WITHOUT_TYPE, buffer, semObj,
                            false);
                } else {
                    modelIntegrityReport.printError(OR_CR_PARAMETER_WITHOUT_TYPE, buffer, semObj,
                            false);
                    occurencesToClean[OR_CR_PARAMETER_WITHOUT_TYPE].add(semObj);
                }

                reportError(semObj);
            }
        }
    }

    /**
     * Parametres sans instructions
     */
    protected void verifyProcedureWithoutBody(ArrayList aList, StringBuffer buffer, int operation)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            boolean haveColumnLinked = true;
            DbORProcedure semObj = (DbORProcedure) (aList.get(i));
            if (semObj.getBody() == null) {
                if (operation == INTEGRITY) {
                    modelIntegrityReport.printError(OR_IR_PROCEDURE_WITHOUT_BODY, buffer, semObj,
                            false);
                } else {
                    modelIntegrityReport.printError(OR_CR_PROCEDURE_WITHOUT_BODY, buffer, semObj,
                            false);
                    occurencesToClean[OR_CR_PROCEDURE_WITHOUT_BODY].add(semObj);
                }

                reportWarning(semObj);

                /*
                 * modelIntegrityReport.printError(OR_CR_PROCEDURE_WITHOUT_BODY, buffer, semObj ,
                 * true); occurencesToClean[OR_CR_PROCEDURE_WITHOUT_BODY].add(semObj);
                 */

            }
        }
    }

    /*******************************************************************************************
     * MÉTHODES UTILISÉES POUR LES MODÈLES CONCEPTUELS ET RELATIONNELS (INTÉGRITÉ ET ÉPURATION)
     *******************************************************************************************/
    /*
     * public static final int = 3;
     */

    /**
     * Clés sans colonnes/attributs ni dépendances
     */
    protected void verifyPrymaryUniquekeyWithoutColDir(ArrayList aList, StringBuffer buffer,
            int modelType, int operation) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORPrimaryUnique semObj = (DbORPrimaryUnique) (aList.get(i));
            DbEnumeration primaryKeyCols = semObj.getColumns().elements();
            DbEnumeration primaryKeyDirs = semObj.getAssociationDependencies().elements();
            if (!primaryKeyCols.hasMoreElements() && !primaryKeyDirs.hasMoreElements()) {
                if (operation == INTEGRITY) {
                    if (modelType == ER_DATAMODEL)
                        modelIntegrityReport.printError(ER_IR_PK_WITHOUT_DIR_COL, buffer, semObj,
                                true);
                    else
                        modelIntegrityReport.printError(OR_IR_PK_WITHOUT_DIR_COL, buffer, semObj,
                                true);
                } else {//CLEANUP
                    if (modelType == ER_DATAMODEL) {
                        modelIntegrityReport.printError(ER_CR_PK_WITHOUT_DIR_COL, buffer, semObj,
                                true);
                        occurencesToClean[ER_CR_PK_WITHOUT_DIR_COL].add(semObj);
                    } else {
                        modelIntegrityReport.printError(OR_CR_PK_WITHOUT_DIR_COL, buffer, semObj,
                                true);
                        occurencesToClean[OR_CR_PK_WITHOUT_DIR_COL].add(semObj);
                    }
                }

                reportError(semObj);
            }
            primaryKeyCols.close();
            primaryKeyDirs.close();
        }
    }

    /**
     * Tables sans connecteurs / Entités ou Associations sans arc
     */
    protected void verifyTableWithoutConnector(ArrayList aList, StringBuffer buffer, int modelType,
            int operation) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORTable semObj = (DbORTable) (aList.get(i));
            DbEnumeration connectors = semObj.getAssociationEnds().elements();
            if (!connectors.hasMoreElements()) {
                if (operation == INTEGRITY) {
                    if (modelType == ER_DATAMODEL)
                        modelIntegrityReport.printError(ER_IR_ENTITY_WITHOUT_ARC, buffer, semObj,
                                true);
                    else
                        modelIntegrityReport.printError(OR_IR_TABLE_WITHOUT_CONNECTOR, buffer,
                                semObj, true);
                } else {
                    if (modelType == ER_DATAMODEL) {
                        modelIntegrityReport.printError(ER_CR_ENTITY_WITHOUT_ARC, buffer, semObj,
                                false);
                        occurencesToClean[ER_CR_ENTITY_WITHOUT_ARC].add(semObj);
                    } else {
                        modelIntegrityReport.printError(OR_CR_TABLE_WITHOUT_CONNECTOR, buffer,
                                semObj, false);
                        occurencesToClean[OR_CR_TABLE_WITHOUT_CONNECTOR].add(semObj);
                    }
                }

                reportWarning(semObj);
            }
            connectors.close();
        }
    }

    /**
     * Tables et vues sans colonnes / Entités sans attributs
     */
    protected void verifyTableWithoutColumn(ArrayList aList, StringBuffer buffer, int modelType,
            int operation) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORAbsTable semObj = (DbORAbsTable) (aList.get(i));
            DbEnumeration columns = semObj.getComponents().elements(DbORColumn.metaClass);
            if (!columns.hasMoreElements()) {
                if (operation == INTEGRITY) {
                    if (modelType == ER_DATAMODEL)
                        modelIntegrityReport.printError(ER_IR_ENTITY_WITHOUT_ATTRIBUTE, buffer,
                                semObj, true);
                    else
                        modelIntegrityReport.printError(OR_IR_TABLE_WITHOUT_COLUMN, buffer, semObj,
                                true);
                } else {//CLEANUP
                    if (modelType == ER_DATAMODEL) {

                        modelIntegrityReport.printError(ER_CR_ENTITY_WITHOUT_ATTRIBUTE, buffer,
                                semObj, false, true);
                        occurencesToClean[ER_CR_ENTITY_WITHOUT_ATTRIBUTE].add(semObj);
                    } else {
                        modelIntegrityReport.printError(OR_CR_TABLE_WITHOUT_COLUMN, buffer, semObj,
                                false, true);
                        occurencesToClean[OR_CR_TABLE_WITHOUT_COLUMN].add(semObj);
                    }
                }

                reportWarning(semObj);
            }
            columns.close();
        }
    }

    /*******************************************************************************************
     * MÉTHODES UTILISÉES POUR LES MODÈLES CONCEPTUELS (INTÉGRITÉ ET ÉPURATION)
     *******************************************************************************************/
    /**
     * Association avec moins de 2 arcs
     */
    protected void verifyRelationshipLessTwoArcs(ArrayList aList, StringBuffer buffer, int operation)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORTable assocTable = (DbORTable) (aList.get(i));
            int nbArcs = assocTable.getAssociationEnds().size();
            if (nbArcs < 2) {
                if (operation == INTEGRITY)
                    modelIntegrityReport.printError(ER_IR_RELATIONSHIP_LESS_2ARCS, buffer,
                            assocTable, true);
                else {
                    modelIntegrityReport.printError(ER_CR_RELATIONSHIP_LESS_2_ARCS, buffer,
                            assocTable, true);
                    occurencesToClean[ER_CR_RELATIONSHIP_LESS_2_ARCS].add(assocTable);
                }

                reportError(assocTable);
            }
        }
    }

    protected void reportWarning(DbObject dbo) throws DbException {
        dbo.setValidationStatus(DbObject.VALIDATION_WARNING);
        modelWarningCount++;
    }

    protected void reportError(DbObject dbo) throws DbException {
        dbo.setValidationStatus(DbObject.VALIDATION_ERROR);
        modelErrorCount++;
    }

}
