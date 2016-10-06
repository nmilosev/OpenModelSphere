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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.db.util.AnyORObject;

/**
 * @author pierrem
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class OrIntegrity extends AbstractORIntegrity {

    public OrIntegrity() {
    }

    private String[] reportMessages;
    private Boolean[] errorFlags;
    private Boolean[] workErrorFlags;
    private StringBuffer warningStrBuffer = new StringBuffer();
    private StringBuffer errorStrBuffer = new StringBuffer();
    private int m_modelType = 0;

    //
    // public methods
    // 

    public int verifyIntegrityOrModel(DbObject model, int modelType) throws DbException,
            IOException {
        m_modelType = modelType;
        initializeIntegrityUtilities();
        verifyIntegrityRules(model);
        modelIntegrityReport.showReport(model, modelErrorCount, modelWarningCount, INTEGRITY);
        int errorFound = modelErrorCount;
        liberateIntegrityUtilities();
        return errorFound;
    } //end verifyIntegrityOrModel()

    //
    // private methods
    //
    private void verifyIntegrityRules(DbObject model) throws DbException {
        getAllObjects(model, m_modelType);

        switch (m_modelType) {
        case AbstractIntegrity.OR_DATAMODEL:
            // --- Errors ---
            verifyPKUKColumnsNullity(primUniqKeyList, modelIntegrityReport.errorStrBuffer);
            verifyChildMinConnectivity0(foreignKeyList, modelIntegrityReport.errorStrBuffer);
            verifyChildMinConnectivity1(foreignKeyList, modelIntegrityReport.errorStrBuffer);
            verifyParentMaxConnectivity1(foreignKeyList, modelIntegrityReport.errorStrBuffer);
            verifyParentMaxConnectivityN(foreignKeyList, modelIntegrityReport.errorStrBuffer);
            verifyMultiplicityUndefined(roleList, modelIntegrityReport.errorStrBuffer);
            verifyPrymaryUniquekeyWithoutColDir(primUniqKeyList,
                    modelIntegrityReport.errorStrBuffer, m_modelType, INTEGRITY);
            verifyForeignkeyWithoutColumn(foreignKeyList, modelIntegrityReport.errorStrBuffer,
                    INTEGRITY);
            verifyIndexWithoutIndexKey(indexList, modelIntegrityReport.errorStrBuffer, INTEGRITY);

            // --- Warning ---
            verifyTableWithoutConnector(tableList, modelIntegrityReport.warningStrBuffer,
                    m_modelType, INTEGRITY);
            verifyTableWithoutColumn(absTableList, modelIntegrityReport.warningStrBuffer,
                    m_modelType, INTEGRITY);
            verifyTriggerWithoutBodyAndCol(triggerList, modelIntegrityReport.warningStrBuffer,
                    INTEGRITY);
            verifyRoleUpdateRule(roleList, modelIntegrityReport.warningStrBuffer);
            verifyBadAssociation(associationList, modelIntegrityReport.warningStrBuffer);
            verifyForeignkeyColumnIntegrity(fKeyColumnList, modelIntegrityReport.warningStrBuffer);

            break;

        case AbstractIntegrity.ER_DATAMODEL:
            // --- Errors ---
            verifyPKUKColumnsNullity(primUniqKeyList, modelIntegrityReport.errorStrBuffer);
            verifyMultiplicityUndefined(erRoleList, modelIntegrityReport.errorStrBuffer);
            verifyPrymaryUniquekeyWithoutColDir(primUniqKeyList,
                    modelIntegrityReport.errorStrBuffer, m_modelType, INTEGRITY);
            verifyEntityWithoutKeys(entityList, modelIntegrityReport.errorStrBuffer);
            verifyTernaryRelationWithDependence(erAssociationList,
                    modelIntegrityReport.errorStrBuffer);
            verifyBinaryRelationWithTooManyDependence(erAssociationList,
                    modelIntegrityReport.errorStrBuffer);
            verifyRelationshipLessTwoArcs(erAssociationList, modelIntegrityReport.errorStrBuffer,
                    INTEGRITY);
            verifyBinaryRecursiveWithDependence(erAssociationList,
                    modelIntegrityReport.errorStrBuffer);
            verifyArcWithDependenceNotAloneOnKey(erAssociationList,
                    modelIntegrityReport.errorStrBuffer);
            verifyArcWithDependenceNotExactlyOne(arcList, modelIntegrityReport.errorStrBuffer);
            //BUILD 507 on retire cette règle qui selon Axel ne serait pas une erreur
            //verifyArcWithDependenceAloneOnKey(erAssociationList, modelIntegrityReport.errorStrBuffer);

            // --- Warning ---
            verifyTableWithoutColumn(entityList, modelIntegrityReport.warningStrBuffer,
                    m_modelType, INTEGRITY);
            verifyTableWithoutConnector(entityList, modelIntegrityReport.warningStrBuffer,
                    m_modelType, INTEGRITY);
            verifyRelationshipWithAttributeAndExactlyOneMult(erAssociationList,
                    modelIntegrityReport.warningStrBuffer);
            verifyNavigability(erAssociationList, modelIntegrityReport.warningStrBuffer);

            break;

        case AbstractIntegrity.DOMAIN_MODEL:
            verifyDomainWithoutType(domainList, modelIntegrityReport.errorStrBuffer, INTEGRITY);
            break;

        case AbstractIntegrity.COMMONITEM_MODEL:
            verifyCommonItemWithoutType(commonItemList, modelIntegrityReport.errorStrBuffer,
                    INTEGRITY);
            break;

        case AbstractIntegrity.OPERATION_LIBRARY:
            verifyProcedureWithoutBody(procedureList, modelIntegrityReport.warningStrBuffer,
                    INTEGRITY);
            verifyParameterWithoutType(parameterList, modelIntegrityReport.errorStrBuffer,
                    INTEGRITY);
            break;

        default:
            break;
        }
    }

    /**********************************************************************************
     * MÉTHODES UTILISÉES POUR LES MODÈLES RELATIONNELS
     **********************************************************************************/

    /**
     * Règle :
     */
    private void verifyForeignkeyColumnIntegrity(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORFKeyColumn semObj = (DbORFKeyColumn) (aList.get(i));
            DbORColumn fKeyColumn = semObj.getColumn();
            DbORColumn sourceColumn = semObj.getSourceColumn();
            DbORForeign fkKey = (DbORForeign) semObj.getComposite();
            if ((sourceColumn != null) && (fKeyColumn != null)) {
                DbORTypeClassifier sourceType = sourceColumn.getType();
                DbORTypeClassifier fKeytype = fKeyColumn.getType();
                if ((sourceType != null) && (fKeytype != null)) {
                    if ((!sourceType.equals(fKeytype))) {
                        modelIntegrityReport.printError(OR_IR_FK_COL_DIFFERENT, buffer, fkKey,
                                true, false, OR_IX_DIFFERENT_DOMAIN, sourceColumn);
                        reportWarning(fkKey);
                    }
                }
                Integer sourceLength = sourceColumn.getLength();
                Integer fKeyLength = fKeyColumn.getLength();
                if ((sourceLength != null) && (fKeyLength != null)) {
                    if (!sourceLength.equals(fKeyLength)) {
                        modelIntegrityReport.printError(OR_IR_FK_COL_DIFFERENT, buffer, fkKey,
                                true, false, OR_IX_DIFFERENT_LENGTH, sourceColumn);
                        reportWarning(fkKey);
                    }
                }
                String sourceNbDec = sourceColumn.getLengthNbDecimal();
                String fKeyNbDec = fKeyColumn.getLengthNbDecimal();
                if ((sourceNbDec != null) && (fKeyNbDec != null)) {
                    if (!sourceNbDec.equals(fKeyNbDec)) {
                        modelIntegrityReport.printError(OR_IR_FK_COL_DIFFERENT, buffer, fkKey,
                                true, false, OR_IX_DIFFERENT_NB_DEC, sourceColumn);
                        reportWarning(fkKey);
                    }
                }
            }
        }
    }

    /**
     * 
     */
    private void verifyRoleUpdateRule(ArrayList aList, StringBuffer buffer) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORAssociationEnd semObj = (DbORAssociationEnd) (aList.get(i));
            if (semObj.getUpdateRule() == null) {
                modelIntegrityReport.printError(OR_IR_NO_UPDATE_RULE, buffer, semObj, true, false);
                reportWarning(semObj);
            }
        }
    }

    /**
     * 
     */
    private void verifyBadAssociation(ArrayList aList, StringBuffer buffer) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORAssociation semObj = (DbORAssociation) (aList.get(i));
            DbORAssociationEnd backEnd = semObj.getBackEnd();
            DbORAssociationEnd frontEnd = semObj.getFrontEnd();
            SMSMultiplicity backCard = backEnd.getMultiplicity();
            SMSMultiplicity frontCard = frontEnd.getMultiplicity();

            if ((backCard.getValue() == SMSMultiplicity.ONE_OR_MORE || backCard.getValue() == SMSMultiplicity.MANY)
                    && (frontCard.getValue() == SMSMultiplicity.ONE_OR_MORE || frontCard.getValue() == SMSMultiplicity.MANY)) {

                modelIntegrityReport.printError(OR_IR_BAD_ASSOCIATION, buffer, semObj, true, false);
                reportWarning(semObj);
            }

        }
    }

    /**
     * Règle : Lors d'une connectivité minimale 0, les colonnes de clés étrangères correspondantes
     * doivent être valeur nulle possible.
     * 
     * check minimum connectivity - if == 0 - fk columns should be null possible
     */
    private void verifyChildMinConnectivity0(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORForeign semObj = (DbORForeign) (aList.get(i));
            DbORAssociationEnd assoc_end = semObj.getAssociationEnd();
            SMSMultiplicity multiplicity = assoc_end.getMultiplicity();
            DbORAbsTable table = (DbORAbsTable) semObj.getComposite();
            ArrayList badColumns = new ArrayList(5);
            DbEnumeration columns = semObj.getComponents().elements(DbORFKeyColumn.metaClass);
            while (columns.hasMoreElements()) {
                DbORColumn column = ((DbORFKeyColumn) columns.nextElement()).getColumn();
                if (!column.isNull()
                        && (multiplicity.getValue() == SMSMultiplicity.ONE_OR_MORE || multiplicity
                                .getValue() == SMSMultiplicity.EXACTLY_ONE))
                    continue;
                if (!column.isNull())
                    badColumns.add(column);
            }
            columns.close();
            if (badColumns.size() == 0)
                continue;
            Iterator iter = badColumns.iterator();
            String colList = "";
            boolean first = true;
            while (iter.hasNext()) {
                if (!first)
                    colList = colList + ", "; // NOT LOCALIZABLE
                colList = colList
                        + modelIntegrityReport.getLinksForDbObject((DbORColumn) iter.next(), true,
                                false);
                first = false;
            }
            //String linkfk = modelIntegrityReport.getLinksForDbObject(semObj, true, true);

            String solution1 = modelIntegrityReport.getSolutionLink(semObj, FK_COLUMNS_SET_NULL,
                    kSetFKColNull);
            SMSMultiplicity newmultiplicity = null;
            if (multiplicity.getValue() == SMSMultiplicity.OPTIONAL)
                newmultiplicity = SMSMultiplicity.getInstance(SMSMultiplicity.EXACTLY_ONE);
            else if (multiplicity.getValue() == SMSMultiplicity.MANY)
                newmultiplicity = SMSMultiplicity.getInstance(SMSMultiplicity.ONE_OR_MORE);

            String sol2message = MessageFormat.format(kSetMulti01, new Object[] { multiplicity,
                    newmultiplicity });
            String solution2 = modelIntegrityReport.getSolutionLink(assoc_end,
                    FK_MULTIPLICITY_SET_MIN_1, sol2message);

            modelIntegrityReport.printError(OR_IR_CHILD_MIN_0123, buffer, semObj, true, true,
                    OR_IX_CHILD_MIN_0123, null, colList, true, solution1, solution2);//MSG-7b
            reportError(semObj);
        }//fin for    
    } //verifyChildMinConnectivity0()

    /**
     * Règle : Lors d'une connectivité minimale 1, les colonnes de clés étrangères correspondantes
     * NE doivent PAS être valeur nulle possible.
     */
    private void verifyChildMinConnectivity1(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORForeign semObj = (DbORForeign) (aList.get(i));
            DbORAssociationEnd assoc_end = semObj.getAssociationEnd();
            SMSMultiplicity multiplicity = assoc_end.getMultiplicity();
            DbORAbsTable table = (DbORAbsTable) semObj.getComposite();
            ArrayList badColumns = new ArrayList(5);
            DbEnumeration columns = semObj.getComponents().elements(DbORFKeyColumn.metaClass);
            while (columns.hasMoreElements()) {
                DbORColumn column = ((DbORFKeyColumn) columns.nextElement()).getColumn();
                if (!column.isNull()
                        && (multiplicity.getValue() == SMSMultiplicity.EXACTLY_ONE || multiplicity
                                .getValue() == SMSMultiplicity.ONE_OR_MORE))
                    continue;
                if (column.isNull()
                        && (multiplicity.getValue() == SMSMultiplicity.EXACTLY_ONE || multiplicity
                                .getValue() == SMSMultiplicity.ONE_OR_MORE))
                    badColumns.add(column);
            }
            columns.close();
            if (badColumns.size() == 0)
                continue;
            Iterator iter = badColumns.iterator();
            String colList = "";
            boolean first = true;
            while (iter.hasNext()) {
                if (!first)
                    colList = colList + ", "; // NOT LOCALIZABLE
                colList = colList
                        + modelIntegrityReport.getLinksForDbObject((DbORColumn) iter.next(), true,
                                false);
                first = false;
            }

            //String linkfk = modelIntegrityReport.getLinksForDbObject(semObj, true, true);

            String solution1 = modelIntegrityReport.getSolutionLink(semObj,
                    FK_COLUMNS_SET_NOT_NULL, kSetFKColNotNull);

            SMSMultiplicity newmultiplicity = null;
            if (multiplicity.getValue() == SMSMultiplicity.EXACTLY_ONE)
                newmultiplicity = SMSMultiplicity.getInstance(SMSMultiplicity.OPTIONAL);
            else if (multiplicity.getValue() == SMSMultiplicity.ONE_OR_MORE)
                newmultiplicity = SMSMultiplicity.getInstance(SMSMultiplicity.MANY);

            String sol2message = MessageFormat.format(kSetMulti01, new Object[] { multiplicity,
                    newmultiplicity });
            String solution2 = modelIntegrityReport.getSolutionLink(assoc_end,
                    FK_MULTIPLICITY_SET_MIN_0, sol2message);

            modelIntegrityReport.printError(OR_IR_CHILD_MIN2_0123, buffer, semObj, true, true,
                    OR_IX_CHILD_MIN_0123, null, colList, true, solution1, solution2);//MSG-7b
            reportError(semObj);
        }//fin for   
    }// fin verifyChildMinConnectivity1

    private void verifyParentMaxConnectivity1(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORForeign semObj = (DbORForeign) (aList.get(i));
            DbORAssociationEnd parentend = semObj.getAssociationEnd().getOppositeEnd();
            SMSMultiplicity parentmultiplicity = parentend.getMultiplicity();
            DbORAbsTable table = (DbORAbsTable) semObj.getComposite();
            if (parentmultiplicity.getValue() != SMSMultiplicity.EXACTLY_ONE
                    && parentmultiplicity.getValue() != SMSMultiplicity.OPTIONAL)
                continue;

            // find a unique contraint or index in the child table (physical representation of a parent maximal of 1)
            int fkcolcount = 0;
            DbEnumeration fkcols = semObj.getComponents().elements(DbORFKeyColumn.metaClass);
            while (fkcols.hasMoreElements()) {
                fkcols.nextElement();
                fkcolcount++;
            }
            fkcols.close();

            boolean uniquefound = false;
            DbEnumeration uniquekeys = table.getComponents().elements(DbORPrimaryUnique.metaClass);
            while (uniquekeys.hasMoreElements()) {
                DbORPrimaryUnique unique = (DbORPrimaryUnique) uniquekeys.nextElement();
                DbRelationN uniquecols = unique.getColumns();
                int colcounts = unique.getColumns().size();
                if (colcounts != fkcolcount)
                    continue;
                boolean sameCols = true;
                DbEnumeration fkcols2 = semObj.getComponents().elements(DbORFKeyColumn.metaClass);
                while (fkcols2.hasMoreElements()) {
                    DbORColumn fkcol = ((DbORFKeyColumn) fkcols2.nextElement()).getColumn();
                    if (uniquecols.indexOf(fkcol) == -1) {
                        sameCols = false;
                        break;
                    }
                }
                fkcols2.close();
                if (sameCols) {
                    uniquefound = true;
                    break;
                }
            }
            uniquekeys.close();
            if (uniquefound)
                continue;

            // No unique found, check for indexes
            DbEnumeration indexes = table.getComponents().elements(DbORIndex.metaClass);
            while (indexes.hasMoreElements()) {
                DbORIndex index = (DbORIndex) indexes.nextElement();
                if (!index.isUnique())
                    continue;
                DbEnumeration indexcomp = index.getComponents().elements(DbORIndexKey.metaClass);
                ArrayList indexCols = new ArrayList(5);
                while (indexcomp.hasMoreElements()) {
                    indexCols.add(((DbORIndexKey) indexcomp.nextElement()).getIndexedElement());
                }
                indexcomp.close();

                int colcounts = indexCols.size();
                if (colcounts != fkcolcount)
                    continue;
                boolean sameCols = true;
                DbEnumeration fkcols3 = semObj.getComponents().elements(DbORFKeyColumn.metaClass);
                while (fkcols3.hasMoreElements()) {
                    DbORColumn fkcol = ((DbORFKeyColumn) fkcols3.nextElement()).getColumn();
                    if (indexCols.indexOf(fkcol) == -1) {
                        sameCols = false;
                        break;
                    }
                }
                fkcols3.close();
                if (sameCols) {
                    uniquefound = true;
                    break;
                }
            }
            indexes.close();
            if (uniquefound)
                continue;

            //String linkfk = modelIntegrityReport.getLinksForDbObject(semObj, true, true);
            //String linkparentend = modelIntegrityReport.getLinksForDbObject(parentend, true, false);

            SMSMultiplicity newmultiplicity = null;
            if (parentend.getMultiplicity().getValue() == SMSMultiplicity.EXACTLY_ONE)
                newmultiplicity = SMSMultiplicity.getInstance(SMSMultiplicity.ONE_OR_MORE);
            else if (parentend.getMultiplicity().getValue() == SMSMultiplicity.OPTIONAL)
                newmultiplicity = SMSMultiplicity.getInstance(SMSMultiplicity.MANY);

            String sol1message = MessageFormat.format(kSetMulti01, new Object[] {
                    parentend.getMultiplicity(), newmultiplicity });
            String solution1 = modelIntegrityReport.getSolutionLink(parentend,
                    FK_MULTIPLICITY_SET_MAX_N, sol1message);

            DbORIndex fkindex = semObj.getIndex();
            String solution2message = "";
            String solution2 = "";

            if (fkindex == null) {
                solution2message = MessageFormat.format(kCreateIndex0, new Object[] { semObj
                        .getName() });
                solution2 = modelIntegrityReport.getSolutionLink(semObj, FK_CREATE_UNIQUE_INDEX,
                        solution2message);
            } else {
                solution2message = MessageFormat.format(kUpdateUnicityIndex01, new Object[] {
                        semObj.getName(), fkindex.getName() });
                solution2 = modelIntegrityReport.getSolutionLink(fkindex, FK_CREATE_UNIQUE_INDEX,
                        solution2message);
            }

            modelIntegrityReport.printError(OR_IR_PARENT_MAX1012, buffer, semObj, true, true,
                    OR_IX_PARENT_MAX, parentend, null, true, solution1, solution2);
            reportError(semObj);
        }
    }

    private void verifyParentMaxConnectivityN(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORForeign semObj = (DbORForeign) (aList.get(i));
            DbORAssociationEnd parentend = semObj.getAssociationEnd().getOppositeEnd();
            SMSMultiplicity parentmultiplicity = parentend.getMultiplicity();
            DbORAbsTable table = (DbORAbsTable) semObj.getComposite();
            if (parentmultiplicity.getValue() != SMSMultiplicity.ONE_OR_MORE
                    && parentmultiplicity.getValue() != SMSMultiplicity.MANY)
                continue;

            // find a non unique constraint or index in the child table (physical representation of a parent maximal of N)
            int fkcolcount = 0;
            DbEnumeration fkcols = semObj.getComponents().elements(DbORFKeyColumn.metaClass);
            while (fkcols.hasMoreElements()) {
                fkcols.nextElement();
                fkcolcount++;
            }
            fkcols.close();

            boolean uniquefound = false;
            DbEnumeration uniquekeys = table.getComponents().elements(DbORPrimaryUnique.metaClass);
            while (uniquekeys.hasMoreElements()) {
                DbORPrimaryUnique unique = (DbORPrimaryUnique) uniquekeys.nextElement();
                DbRelationN uniquecols = unique.getColumns();
                int colcounts = unique.getColumns().size();
                if (colcounts != fkcolcount)
                    continue;
                boolean sameCols = true;
                DbEnumeration fkcols2 = semObj.getComponents().elements(DbORFKeyColumn.metaClass);
                while (fkcols2.hasMoreElements()) {
                    DbORColumn fkcol = ((DbORFKeyColumn) fkcols2.nextElement()).getColumn();
                    if (uniquecols.indexOf(fkcol) == -1) {
                        sameCols = false;
                        break;
                    }
                }
                fkcols2.close();
                if (sameCols) {
                    uniquefound = true;
                    break;
                }
            }
            uniquekeys.close();
            if (!uniquefound) {
                // No Unique found, check for unique indexes
                DbEnumeration indexes = table.getComponents().elements(DbORIndex.metaClass);
                while (indexes.hasMoreElements()) {
                    DbORIndex index = (DbORIndex) indexes.nextElement();
                    if (!index.isUnique())
                        continue;
                    DbEnumeration indexcomp = index.getComponents()
                            .elements(DbORIndexKey.metaClass);
                    ArrayList indexCols = new ArrayList(5);
                    while (indexcomp.hasMoreElements()) {
                        indexCols.add(((DbORIndexKey) indexcomp.nextElement()).getIndexedElement());
                    }
                    indexcomp.close();

                    int colcounts = indexCols.size();
                    if (colcounts != fkcolcount)
                        continue;
                    boolean sameCols = true;
                    DbEnumeration fkcols3 = semObj.getComponents().elements(
                            DbORFKeyColumn.metaClass);
                    while (fkcols3.hasMoreElements()) {
                        DbORColumn fkcol = ((DbORFKeyColumn) fkcols3.nextElement()).getColumn();
                        if (indexCols.indexOf(fkcol) == -1) {
                            sameCols = false;
                            break;
                        }
                    }
                    fkcols3.close();
                    if (sameCols) {
                        uniquefound = true;
                        break;
                    }
                }
                indexes.close();
            }
            if (!uniquefound)
                continue;

            // Nothing found, add error
            //String linkfk = getLinksForDbObject(semObj, false, links, showPropertyImageURL);
            //String linkparentend = getLinksForDbObject(parentend, false, links, showPropertyImageURL);

            SMSMultiplicity newmultiplicity = null;
            if (parentend.getMultiplicity().getValue() == SMSMultiplicity.ONE_OR_MORE)
                newmultiplicity = SMSMultiplicity.getInstance(SMSMultiplicity.EXACTLY_ONE);
            else if (parentend.getMultiplicity().getValue() == SMSMultiplicity.MANY)
                newmultiplicity = SMSMultiplicity.getInstance(SMSMultiplicity.OPTIONAL);

            String sol1message = MessageFormat.format(kSetMulti01, new Object[] {
                    parentend.getMultiplicity(), newmultiplicity });
            String solution1 = modelIntegrityReport.getSolutionLink(parentend,
                    FK_MULTIPLICITY_SET_MAX_1, sol1message);

            DbORIndex fkindex = semObj.getIndex();
            String solution2message = "";
            String solution2 = "";

            if (fkindex != null) {
                solution2message = MessageFormat.format(kUpdateUnicityIndex01, new Object[] {
                        semObj.getName(), fkindex.getName() });
                solution2 = modelIntegrityReport.getSolutionLink(fkindex,
                        FK_CREATE_NON_UNIQUE_INDEX, solution2message);
                modelIntegrityReport.printError(OR_IR_PARENT_MAXN012, buffer, semObj, true, true,
                        OR_IX_PARENT_MAX, parentend, null, true, solution1, solution2);
            } else {
                modelIntegrityReport.printError(OR_IR_PARENT_MAXN012, buffer, semObj, true, true,
                        OR_IX_PARENT_MAX, parentend, null, true, solution1, null);
            }
            reportError(semObj);

        }
    }

    /**********************************************************************************
     * MÉTHODES UTILISÉES POUR LES MODÈLES CONCEPTUELS ET RELATIONNELS
     **********************************************************************************/

    /**
     * 
     */
    private void verifyMultiplicityUndefined(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORAssociationEnd semObj = (DbORAssociationEnd) (aList.get(i));
            SMSMultiplicity roleMultiplicity = null;
            roleMultiplicity = semObj.getMultiplicity();
            if (roleMultiplicity.getValue() == SMSMultiplicity.UNDEFINED) {
                if (m_modelType == ER_DATAMODEL)
                    modelIntegrityReport.printError(ER_IR_MULTIPLICITY_UNDEFINED, buffer, semObj,
                            true, false);
                else
                    modelIntegrityReport.printError(OR_IR_MULTIPLICITY_UNDEFINED, buffer, semObj,
                            true, false);

                reportError(semObj);
            }
        }
    }

    /**
     * Règle : Une colonne faisant partie d'une clé primaire ou unique doit être NON NUL
     */
    private void verifyPKUKColumnsNullity(ArrayList aList, StringBuffer buffer) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORPrimaryUnique semObj = (DbORPrimaryUnique) (aList.get(i));
            DbEnumeration columns = semObj.getColumns().elements();
            while (columns.hasMoreElements()) {
                DbORColumn column = (DbORColumn) columns.nextElement();
                if (column.isNull()) {
                    if (m_modelType == ER_DATAMODEL) {
                        String solution1 = modelIntegrityReport.getSolutionLink(column,
                                COLUMN_SET_NOT_NULL, kSetColNotNull);
                        modelIntegrityReport.printError(ER_IR_COLUMN_NOT_NULL, buffer, column,
                                true, true, true, solution1);//MSG-7b    
                    } else {
                        String solution1 = modelIntegrityReport.getSolutionLink(column,
                                COLUMN_SET_NOT_NULL, kSetColNotNull);
                        modelIntegrityReport.printError(OR_IR_COLUMN_NOT_NULL, buffer, column,
                                true, true, true, solution1);//MSG-7b
                    }

                    reportError(column);
                }
            }
            columns.close();
        }
    }

    /**********************************************************************************
     * MÉTHODES UTILISÉES POUR LES MODÈLES CONCEPTUELS
     **********************************************************************************/

    /**
     * Entités sans clés
     */
    protected void verifyEntityWithoutKeys(ArrayList aList, StringBuffer buffer) throws DbException {
        ArrayList keyList = new ArrayList();

        for (int i = 0; i < aList.size(); i++) {
            DbORTable semObj = (DbORTable) (aList.get(i));
            DbEnumeration components = semObj.getComponents().elements();
            while (components.hasMoreElements()) {
                DbObject dbo = components.nextElement();
                if (dbo instanceof DbORPrimaryUnique)
                    keyList.add(dbo);
            }
            components.close();

            if (keyList.size() == 0) {
                modelIntegrityReport.printError(ER_IR_ENTITY_WITHOUT_KEY, buffer, semObj, true);
                reportError(semObj);
            }
            keyList.clear();
        }
    }

    /**
     * Association ternaire avec une dépendance
     */
    protected void verifyTernaryRelationWithDependence(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            int nbDependenceFound = 0;
            DbORTable assocTable = (DbORTable) (aList.get(i));
            int nbArcs = assocTable.getAssociationEnds().size();
            if (nbArcs > 2) {
                DbEnumeration rolesEnum = assocTable.getAssociationEnds().elements();// toujours des backEnds
                while (rolesEnum.hasMoreElements()) {
                    DbORAssociationEnd assocEnd = (DbORAssociationEnd) rolesEnum.nextElement();
                    nbDependenceFound += assocEnd.getDependentConstraints().size();
                }
                rolesEnum.close();

                if (nbDependenceFound > 0) {
                    modelIntegrityReport.printError(ER_IR_TERNARY_RELATION_WITH_DEP, buffer,
                            assocTable, true);
                    reportError(assocTable);
                }
            }
        }
    }

    /**
     * Association binaire avec dépendance sur plus d'un arc
     */
    protected void verifyBinaryRelationWithTooManyDependence(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            int nbArcWithDependence = 0;
            DbORTable assocTable = (DbORTable) (aList.get(i));
            int nbArcs = assocTable.getAssociationEnds().size();
            if (nbArcs == 2) {
                DbEnumeration rolesEnum = assocTable.getAssociationEnds().elements();// toujours des backEnds
                while (rolesEnum.hasMoreElements()) {
                    DbORAssociationEnd assocEnd = (DbORAssociationEnd) rolesEnum.nextElement();
                    if (assocEnd.getDependentConstraints().size() > 0)
                        nbArcWithDependence++;
                }
                rolesEnum.close();

                if (nbArcWithDependence > 1) {
                    modelIntegrityReport.printError(ER_IR_BINARY_WITH_TOO_MANY_DEP, buffer,
                            assocTable, true);
                    reportError(assocTable);
                }
            }
        }
    }

    /**
     * Association binaire avec dépendance sur une relation récursive
     */
    protected void verifyBinaryRecursiveWithDependence(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            int nbArcWithDependence = 0;
            DbORTable assocTable = (DbORTable) (aList.get(i));
            DbORAbsTable firstEntity = null;
            DbORAbsTable secondEntity = null;
            int noArc = 0;

            int nbArcs = assocTable.getAssociationEnds().size();
            if (nbArcs == 2) {
                DbEnumeration rolesEnum = assocTable.getAssociationEnds().elements();// toujours des backEnds
                while (rolesEnum.hasMoreElements()) {
                    DbORAssociationEnd assocEnd = (DbORAssociationEnd) rolesEnum.nextElement();
                    if (assocEnd.getDependentConstraints().size() > 0)
                        nbArcWithDependence++;
                    noArc++;
                    if (noArc == 1)
                        firstEntity = assocEnd.getOppositeEnd().getClassifier();
                    else
                        secondEntity = assocEnd.getOppositeEnd().getClassifier();
                }
                rolesEnum.close();

                if ((nbArcWithDependence > 0) && (firstEntity.equals(secondEntity))) {
                    modelIntegrityReport.printError(ER_IR_RECURSIVE_WITH_DEPENDENCE, buffer,
                            assocTable, true);
                    reportError(assocTable);
                }
            }
        }
    }

    /**
     * Association contenant un attribut et un arc avec une multiplicité 1,1
     */
    protected void verifyRelationshipWithAttributeAndExactlyOneMult(ArrayList aList,
            StringBuffer buffer) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            boolean exactlyOneExist = false;
            DbORTable assocTable = (DbORTable) (aList.get(i));
            DbEnumeration attributesEnum = assocTable.getComponents()
                    .elements(DbORColumn.metaClass);
            if (attributesEnum.hasMoreElements()) {
                DbEnumeration rolesEnum = assocTable.getAssociationEnds().elements();// toujours des backEnds
                while (rolesEnum.hasMoreElements()) {
                    DbORAssociationEnd assocEnd = (DbORAssociationEnd) rolesEnum.nextElement();
                    SMSMultiplicity multiplicity = assocEnd.getMultiplicity();
                    if (multiplicity.getValue() == SMSMultiplicity.EXACTLY_ONE) {
                        exactlyOneExist = true;
                    }
                }
                rolesEnum.close();
            }
            attributesEnum.close();

            if (exactlyOneExist) {
                modelIntegrityReport.printError(ER_IR_RELATIONSHIP_WITH_ATTR_AND_1_1, buffer,
                        assocTable, true);
                reportWarning(assocTable);
            }
        }
    }

    /**
     * Arc avec une dépendance et dont la multiplicité n'est pas 1,1
     */
    protected void verifyArcWithDependenceNotExactlyOne(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORAssociation arc = (DbORAssociation) (aList.get(i));
            DbORAssociationEnd assocEnd = arc.getArcEnd();
            SMSMultiplicity multiplicity = assocEnd.getMultiplicity();
            if ((assocEnd.getDependentConstraints().size() > 0)
                    && !(multiplicity.getValue() == SMSMultiplicity.EXACTLY_ONE)) {
                modelIntegrityReport.printError(ER_IR_DEPENDENCE_ON_ARC_NOT_1_1, buffer, arc, true);
                reportError(arc);
            }
        }
    }

    /**
     * Règle : Vérification de la navigabilité des rôles de relations binaires
     */
    private void verifyNavigability(ArrayList aList, StringBuffer buffer) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORTable assocTable = (DbORTable) (aList.get(i));
            int nbArcs = assocTable.getAssociationEnds().size();
            if (nbArcs == 2) {
                int nbNavigable = 0;
                int maxNIsNavigable = 0;
                int nbMaxN = 0;
                DbEnumeration rolesEnum = assocTable.getAssociationEnds().elements();// toujours des backEnds
                while (rolesEnum.hasMoreElements()) {
                    DbORAssociationEnd assocEnd = (DbORAssociationEnd) rolesEnum.nextElement();
                    SMSMultiplicity multiplicity = assocEnd.getMultiplicity();
                    if (assocEnd.isNavigable()) {
                        nbNavigable++;
                        if (multiplicity.isMaxN())
                            maxNIsNavigable++;
                    }
                    if (multiplicity.isMaxN())
                        nbMaxN++;
                }
                rolesEnum.close();

                if (nbNavigable == 0) {
                    modelIntegrityReport.printError(ER_IR_RELATIONSHIP_BAD_NAVIGABILITY, buffer,
                            assocTable, false, false, ER_IX_NO_ROLE_NAVIGABLE);
                    reportWarning(assocTable);
                } else if (nbNavigable == 2) {
                    modelIntegrityReport.printError(ER_IR_RELATIONSHIP_BAD_NAVIGABILITY, buffer,
                            assocTable, false, false, ER_IX_BOTH_ROLE_NAVIGABLE);
                    reportWarning(assocTable);
                } else if ((maxNIsNavigable == 1) && (nbMaxN == 1)) {
                    modelIntegrityReport.printError(ER_IR_RELATIONSHIP_BAD_NAVIGABILITY, buffer,
                            assocTable, false, false, ER_IX_MAXN_ROLE_NAVIGABLE);
                    reportWarning(assocTable);
                }
            }
        }
    }

    /**
     * Les arcs ayant une dépendance sur le rôle qui est le seul membre de la clé et le rôle opposé
     * a une multiplicité maximale différente de 1
     * 
     * BUILD 507 on retire cette règle qui selon Axel ne serait pas une erreur
     */
    protected void verifyArcWithDependenceAloneOnKey(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORTable assocTable = (DbORTable) (aList.get(i));
            int nbArcs = assocTable.getAssociationEnds().size();
            if (nbArcs == 2) {
                boolean first = true;
                DbORAssociationEnd firstEnd = null;
                DbORAssociationEnd secondEnd = null;
                DbEnumeration rolesEnum = assocTable.getAssociationEnds().elements();
                while (rolesEnum.hasMoreElements()) {
                    if (first) {
                        firstEnd = (DbORAssociationEnd) rolesEnum.nextElement();
                        first = false;
                    } else
                        secondEnd = (DbORAssociationEnd) rolesEnum.nextElement();
                }
                rolesEnum.close();

                if (firstEnd.getDependentConstraints().size() > 0) {
                    SMSMultiplicity oppositeEndMult = secondEnd.getMultiplicity();
                    DbORAssociation arc = (DbORAssociation) firstEnd
                            .getCompositeOfType(DbORAssociation.metaClass);
                    DbEnumeration depEnum = firstEnd.getDependentConstraints().elements();
                    while (depEnum.hasMoreElements()) {
                        DbORPrimaryUnique key = (DbORPrimaryUnique) depEnum.nextElement();
                        //  pas de colonne et opposé Max N
                        if ((key.getColumns().size() == 0) && (oppositeEndMult.isMaxN())) {
                            modelIntegrityReport.printError(ER_IX_DEP_ALONE_WITH_OPPOSITE_MAXN,
                                    buffer, arc, true);
                            reportError(arc);
                        }
                    }
                    depEnum.close();
                } else if (secondEnd.getDependentConstraints().size() > 0) {
                    SMSMultiplicity oppositeEndMult = firstEnd.getMultiplicity();
                    DbORAssociation arc = (DbORAssociation) secondEnd
                            .getCompositeOfType(DbORAssociation.metaClass);
                    DbEnumeration depEnum = secondEnd.getDependentConstraints().elements();
                    while (depEnum.hasMoreElements()) {
                        DbORPrimaryUnique key = (DbORPrimaryUnique) depEnum.nextElement();
                        // pas de colonne et opposé Max N

                        if ((key.getColumns().size() == 0) && (oppositeEndMult.isMaxN())) {
                            modelIntegrityReport.printError(ER_IX_DEP_ALONE_WITH_OPPOSITE_MAXN,
                                    buffer, arc, true);
                            reportError(arc);
                        }
                    }
                    depEnum.close();
                }
            }
        }
    }

    /**
     * Les arcs ayant une dépendance sur le rôle qui n'est pas le seul membre de la clé et le rôle
     * opposé a une multiplicité maximale égale à 1
     */
    protected void verifyArcWithDependenceNotAloneOnKey(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORTable assocTable = (DbORTable) (aList.get(i));
            int nbArcs = assocTable.getAssociationEnds().size();
            if (nbArcs == 2) {
                boolean first = true;
                DbORAssociationEnd firstEnd = null;
                DbORAssociationEnd secondEnd = null;
                DbEnumeration rolesEnum = assocTable.getAssociationEnds().elements();
                while (rolesEnum.hasMoreElements()) {
                    if (first) {
                        firstEnd = (DbORAssociationEnd) rolesEnum.nextElement();
                        first = false;
                    } else
                        secondEnd = (DbORAssociationEnd) rolesEnum.nextElement();
                }
                rolesEnum.close();

                if (firstEnd.getDependentConstraints().size() > 0) {
                    SMSMultiplicity oppositeEndMult = secondEnd.getMultiplicity();
                    DbORAssociation arc = (DbORAssociation) firstEnd
                            .getCompositeOfType(DbORAssociation.metaClass);
                    DbEnumeration depEnum = firstEnd.getDependentConstraints().elements();
                    while (depEnum.hasMoreElements()) {
                        DbORPrimaryUnique key = (DbORPrimaryUnique) depEnum.nextElement();
                        //  avec colonne et opposé Max 1
                        if ((key.getColumns().size() > 0) && (!oppositeEndMult.isMaxN())) {
                            modelIntegrityReport.printError(ER_IX_DEP_NOT_ALONE_WITH_OPPOSITE_MAX1,
                                    buffer, arc, true);
                            reportError(arc);
                        }
                    }
                    depEnum.close();
                } else if (secondEnd.getDependentConstraints().size() > 0) {
                    SMSMultiplicity oppositeEndMult = firstEnd.getMultiplicity();
                    DbORAssociation arc = (DbORAssociation) secondEnd
                            .getCompositeOfType(DbORAssociation.metaClass);
                    DbEnumeration depEnum = secondEnd.getDependentConstraints().elements();
                    while (depEnum.hasMoreElements()) {
                        DbORPrimaryUnique key = (DbORPrimaryUnique) depEnum.nextElement();
                        //  avec colonne et opposé Max 1                          
                        if ((key.getColumns().size() > 0) && (!oppositeEndMult.isMaxN())) {
                            modelIntegrityReport.printError(ER_IX_DEP_NOT_ALONE_WITH_OPPOSITE_MAX1,
                                    buffer, arc, true);
                            reportError(arc);
                        }
                    }
                    depEnum.close();
                }
            }
        }
    }

    /**************************************************************************************
     * MÉTHODES DE CORRECTION DE DATAMODEL
     **************************************************************************************/

    protected static boolean correctPKUKColumnsNullity(DbORColumn column) throws DbException {
        if (column.isNull()) {
            column.setNull(Boolean.FALSE);
            return true;
        }
        return false;
    }

    protected static boolean correctChildMinConnectivity0(DbObject dbo) throws DbException {
        if (dbo instanceof DbORAssociationEnd) {
            DbORAssociationEnd end = (DbORAssociationEnd) dbo;
            SMSMultiplicity multiplicity = end.getMultiplicity();
            SMSMultiplicity newmultiplicity = null;
            if (multiplicity.getValue() == SMSMultiplicity.OPTIONAL)
                newmultiplicity = SMSMultiplicity.getInstance(SMSMultiplicity.EXACTLY_ONE);
            else if (multiplicity.getValue() == SMSMultiplicity.MANY)
                newmultiplicity = SMSMultiplicity.getInstance(SMSMultiplicity.ONE_OR_MORE);
            else
                return false;
            end.setMultiplicity(newmultiplicity);
            return true;
        }
        if (dbo instanceof DbORForeign) {
            DbORForeign key = (DbORForeign) dbo;
            boolean updated = false;
            DbEnumeration columns = key.getComponents().elements(DbORFKeyColumn.metaClass);
            while (columns.hasMoreElements()) {
                DbORColumn column = ((DbORFKeyColumn) columns.nextElement()).getColumn();
                if (column.isNull())
                    continue;
                column.setNull(Boolean.TRUE);
                updated = true;
            }
            columns.close();
            return updated;
        }
        return false;
    }

    protected static boolean correctChildMinConnectivity1(DbObject dbo) throws DbException {
        if (dbo instanceof DbORAssociationEnd) {
            DbORAssociationEnd end = (DbORAssociationEnd) dbo;
            SMSMultiplicity multiplicity = end.getMultiplicity();
            SMSMultiplicity newmultiplicity = null;
            if (multiplicity.getValue() == SMSMultiplicity.EXACTLY_ONE)
                newmultiplicity = SMSMultiplicity.getInstance(SMSMultiplicity.OPTIONAL);
            else if (multiplicity.getValue() == SMSMultiplicity.ONE_OR_MORE)
                newmultiplicity = SMSMultiplicity.getInstance(SMSMultiplicity.MANY);
            else
                return false;
            end.setMultiplicity(newmultiplicity);
            return true;
        }
        if (dbo instanceof DbORForeign) {
            DbORForeign key = (DbORForeign) dbo;
            boolean updated = false;
            DbEnumeration columns = key.getComponents().elements(DbORFKeyColumn.metaClass);
            while (columns.hasMoreElements()) {
                DbORColumn column = ((DbORFKeyColumn) columns.nextElement()).getColumn();
                if (!column.isNull())
                    continue;
                column.setNull(Boolean.FALSE);
                updated = true;
            }
            columns.close();
            return updated;
        }
        return false;
    }

    protected static boolean correctParentMaxConnectivity1(DbObject dbo) throws DbException {
        if (dbo instanceof DbORAssociationEnd) {
            DbORAssociationEnd end = (DbORAssociationEnd) dbo;
            SMSMultiplicity multiplicity = end.getMultiplicity();
            SMSMultiplicity newmultiplicity = null;
            if (multiplicity.getValue() == SMSMultiplicity.EXACTLY_ONE)
                newmultiplicity = SMSMultiplicity.getInstance(SMSMultiplicity.ONE_OR_MORE);
            else if (multiplicity.getValue() == SMSMultiplicity.OPTIONAL)
                newmultiplicity = SMSMultiplicity.getInstance(SMSMultiplicity.MANY);
            else
                return false;
            end.setMultiplicity(newmultiplicity);
            return true;
        }
        if (dbo instanceof DbORForeign) {
            DbORForeign key = (DbORForeign) dbo;
            DbORIndex index = key.getIndex();
            if (index == null) {
                MetaClass metaclass = AnyORObject.getTargetMetaClasses(key)[AnyORObject.I_INDEX];
                index = (DbORIndex) key.getCompositeOfType(DbORAbsTable.metaClass).createComponent(
                        metaclass);
                index.setUnique(Boolean.TRUE);
                key.setIndex(index);
                ApplicationContext.getDefaultMainFrame().findInExplorer(index);
                return true;
            }
            return false;
        }
        if (dbo instanceof DbORIndex) {
            DbORIndex index = (DbORIndex) dbo;
            if (index.isUnique())
                return false;
            index.setUnique(Boolean.TRUE);
            return true;
        }
        return false;
    } //correctParentMaxConnectivity1()

    protected static boolean correctParentMaxConnectivityN(DbObject dbo) throws DbException {
        if (dbo instanceof DbORAssociationEnd) {
            DbORAssociationEnd end = (DbORAssociationEnd) dbo;
            SMSMultiplicity multiplicity = end.getMultiplicity();
            SMSMultiplicity newmultiplicity = null;
            if (multiplicity.getValue() == SMSMultiplicity.ONE_OR_MORE)
                newmultiplicity = SMSMultiplicity.getInstance(SMSMultiplicity.EXACTLY_ONE);
            else if (multiplicity.getValue() == SMSMultiplicity.MANY)
                newmultiplicity = SMSMultiplicity.getInstance(SMSMultiplicity.OPTIONAL);
            else
                return false;
            end.setMultiplicity(newmultiplicity);
            return true;
        }
        if (dbo instanceof DbORIndex) {
            DbORIndex index = (DbORIndex) dbo;
            if (!index.isUnique())
                return false;
            index.setUnique(Boolean.FALSE);
            return true;
        }
        return false;
    }

    //fin Méthodes de corrections des DataModel

    private String[] getReportMessages() {
        String[] integMsgs = null;
        if (m_modelType == ER_DATAMODEL)
            integMsgs = AbstractORIntegrity.erIntegrityMessages;
        else
            integMsgs = AbstractORIntegrity.orIntegrityMessages;
        return integMsgs;
    }

    private Boolean[] initializeErrorFlags() {
        Boolean[] errorFlags = null;
        if (m_modelType == ER_DATAMODEL)
            errorFlags = AbstractORIntegrity.erIntegrityErrorFlags;
        else
            errorFlags = AbstractORIntegrity.orIntegrityErrorFlags;
        return errorFlags;
    }

    private void initializeIntegrityUtilities() throws DbException {
        reportMessages = getReportMessages();
        errorFlags = initializeErrorFlags();
        workErrorFlags = (Boolean[]) errorFlags.clone();
        modelIntegrityReport = new ModelIntegrityReport(reportMessages, workErrorFlags);
        modelErrorCount = 0;
        modelWarningCount = 0;
    }

    private void liberateIntegrityUtilities() {
        modelIntegrityReport = null;
    }
}
