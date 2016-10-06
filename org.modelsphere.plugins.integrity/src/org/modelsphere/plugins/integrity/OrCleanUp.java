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

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORDomain;

/**
 * @author pierrem
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class OrCleanUp extends AbstractORIntegrity {

    public OrCleanUp() {
    }

    private String[] cleanUpSectionLinkStr;
    private String[] reportMessages;
    private Boolean[] errorFlags;
    private Boolean[] workErrorFlags;
    private StringBuffer warningStrBuffer = new StringBuffer();
    private StringBuffer errorStrBuffer = new StringBuffer();
    private int m_modelType = 0;

    //
    // public methods
    // 

    public void cleanUpOrModel(DbObject model, int modelType) throws DbException, IOException {
        m_modelType = modelType;
        initializeCleanUpUtilities();
        verifyCleanUpRules(model);
        modelIntegrityReport.showReport(model, modelErrorCount, modelWarningCount, CLEANUP);
        liberateCleanUpUtilities();
    }

    //
    // private methods
    //
    private void verifyCleanUpRules(DbObject model) throws DbException {
        getAllObjects(model, m_modelType);

        switch (m_modelType) {
        case AbstractIntegrity.OR_DATAMODEL:
            //    --- Errors ---
            verifyPrymaryUniquekeyWithoutColDir(primUniqKeyList,
                    modelIntegrityReport.errorStrBuffer, m_modelType, CLEANUP);
            verifyForeignkeyWithoutColumn(foreignKeyList, modelIntegrityReport.errorStrBuffer,
                    CLEANUP);
            verifyIndexWithoutIndexKey(indexList, modelIntegrityReport.errorStrBuffer, CLEANUP);

            //  --- Warning ---    
            verifyTableWithoutConnector(tableList, modelIntegrityReport.warningStrBuffer,
                    m_modelType, CLEANUP);
            verifyTableWithoutColumn(absTableList, modelIntegrityReport.warningStrBuffer,
                    m_modelType, CLEANUP);
            verifyCheckConstraintWithoutColumn(checkList, modelIntegrityReport.warningStrBuffer);

            verifyTriggerWithoutBodyAndCol(triggerList, modelIntegrityReport.warningStrBuffer,
                    CLEANUP);
            break;

        case AbstractIntegrity.ER_DATAMODEL:
            // --- Errors ---
            verifyPrymaryUniquekeyWithoutColDir(primUniqKeyList,
                    modelIntegrityReport.errorStrBuffer, m_modelType, CLEANUP);
            verifyRelationshipLessTwoArcs(erAssociationList, modelIntegrityReport.errorStrBuffer,
                    CLEANUP);

            // --- Warning ---
            verifyTableWithoutConnector(entityList, modelIntegrityReport.warningStrBuffer,
                    m_modelType, CLEANUP);
            verifyTableWithoutColumn(entityList, modelIntegrityReport.warningStrBuffer,
                    m_modelType, CLEANUP);

            break;

        case AbstractIntegrity.DOMAIN_MODEL:
            verifyDomainWithoutType(domainList, modelIntegrityReport.errorStrBuffer, CLEANUP);
            verifyDomainNotUsed(domainList, modelIntegrityReport.warningStrBuffer);
            break;

        case AbstractIntegrity.COMMONITEM_MODEL:
            verifyCommonItemWithoutType(commonItemList, modelIntegrityReport.errorStrBuffer,
                    CLEANUP);
            verifyCommonItemWithoutColumn(commonItemList, modelIntegrityReport.warningStrBuffer);
            break;

        case AbstractIntegrity.OPERATION_LIBRARY:
            verifyProcedureWithoutBody(procedureList, modelIntegrityReport.warningStrBuffer,
                    CLEANUP);
            verifyParameterWithoutType(parameterList, modelIntegrityReport.errorStrBuffer, CLEANUP);
            break;

        default:
            break;
        }
    }

    /**
     * Tables sans connecteurs
     */
    /*
     * private void verifyTableWithoutConnector(ArrayList aList, StringBuffer buffer)throws
     * DbException{ for (int i = 0; i < aList.size(); i++){ DbORTable semObj =
     * (DbORTable)(aList.get(i)); DbEnumeration connectors = semObj.getAssociationEnds().elements();
     * if (!connectors.hasMoreElements()){
     * modelIntegrityReport.printError(OR_CR_TABLE_WITHOUT_CONNECTOR, buffer, semObj , false);
     * occurencesToClean[OR_CR_TABLE_WITHOUT_CONNECTOR].add(semObj); modelWarningCount++; }
     * connectors.close(); } }
     */

    /**
     * Tables et vues sans colonnes
     */
    /*
     * private void verifyTableWithoutColumn(ArrayList aList, StringBuffer buffer)throws
     * DbException{ for (int i = 0; i < aList.size(); i++){ DbORAbsTable semObj =
     * (DbORAbsTable)(aList.get(i)); DbEnumeration columns =
     * semObj.getComponents().elements(DbORColumn.metaClass); if (!columns.hasMoreElements()){
     * modelIntegrityReport.printError(OR_CR_TABLE_WITHOUT_COLUMN, buffer, semObj, false, true);
     * occurencesToClean[OR_CR_TABLE_WITHOUT_COLUMN].add(semObj); modelWarningCount++; }
     * columns.close(); } }
     */

    /**
     *  
     */
    private void verifyCheckConstraintWithoutColumn(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORCheck semObj = (DbORCheck) (aList.get(i));
            if (semObj.getColumn() == null) {
                modelIntegrityReport.printError(OR_CR_CHECK_WITHOUT_COL, buffer, semObj, true);
                occurencesToClean[OR_CR_CHECK_WITHOUT_COL].add(semObj);
                modelWarningCount++;
            }
        }
    }

    /**
     * Domaines sans Type
     */
    /*
     * private void verifyDomainWithoutType(ArrayList aList, StringBuffer buffer)throws DbException{
     * for (int i = 0; i < aList.size(); i++){ DbORDomain semObj = (DbORDomain)(aList.get(i)); if
     * (semObj.getSourceType() == null){ modelIntegrityReport.printError(OR_CR_DOMAIN_WITHOUT_TYPE,
     * buffer, semObj , false); occurencesToClean[OR_CR_DOMAIN_WITHOUT_TYPE].add(semObj);
     * modelWarningCount++; } } }
     */

    /**
     * Domaines non utilisés
     */
    private void verifyDomainNotUsed(ArrayList aList, StringBuffer buffer) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORDomain semObj = (DbORDomain) (aList.get(i));
            DbEnumeration attributes = semObj.getTypedAttributes().elements();
            if (!attributes.hasMoreElements()) {
                modelIntegrityReport.printError(OR_CR_DOMAIN_NOT_USED, buffer, semObj, false);
                occurencesToClean[OR_CR_DOMAIN_NOT_USED].add(semObj);
                modelWarningCount++;
            }
            attributes.close();
        }
    }

    /**
     * Item commun sans colonne
     */
    private void verifyCommonItemWithoutColumn(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbORCommonItem semObj = (DbORCommonItem) (aList.get(i));
            DbEnumeration columns = semObj.getColumns().elements();
            if (!columns.hasMoreElements()) {
                modelIntegrityReport.printError(OR_CR_COMMON_ITEM_WITHOUT_COL, buffer, semObj,
                        false);
                occurencesToClean[OR_CR_COMMON_ITEM_WITHOUT_COL].add(semObj);
                modelWarningCount++;
            }
            columns.close();
        }
    }

    private String[] getCleanUpSectionLinkStr() {
        String[] result = null;
        if (m_modelType == ER_DATAMODEL)
            result = AbstractORIntegrity.erCleanUpSectionLinkStr;
        else
            result = AbstractORIntegrity.orCleanUpSectionLinkStr;
        return result;
    }

    private String[] getReportMessages() {
        String[] cleanMsgs = null;
        if (m_modelType == ER_DATAMODEL)
            cleanMsgs = AbstractORIntegrity.erCleanUpMessages;
        else
            cleanMsgs = AbstractORIntegrity.orCleanUpMessages;
        return cleanMsgs;
    }

    private Boolean[] initializeErrorFlags() {
        Boolean[] errorFlags = null;
        if (m_modelType == ER_DATAMODEL)
            errorFlags = AbstractORIntegrity.erCleanUpErrorFlags;
        else
            errorFlags = AbstractORIntegrity.orCleanUpErrorFlags;
        return errorFlags;
    }

    private void initializeCleanUpUtilities() throws DbException {
        cleanUpSectionLinkStr = getCleanUpSectionLinkStr();
        reportMessages = getReportMessages();
        errorFlags = initializeErrorFlags();
        workErrorFlags = (Boolean[]) errorFlags.clone();
        int nbCRRules = 0;
        if (m_modelType == ER_DATAMODEL) {
            modelIntegrityReport = new ModelIntegrityReport(reportMessages, workErrorFlags,
                    erCleanUpSectionLinkStr);
            nbCRRules = ER_CR_NB_RULES;
        } else {
            modelIntegrityReport = new ModelIntegrityReport(reportMessages, workErrorFlags,
                    orCleanUpSectionLinkStr);
            nbCRRules = OR_CR_NB_RULES;
        }
        modelIntegrityReport.setCleanUpMode(true);
        modelErrorCount = 0;
        modelWarningCount = 0;

        occurencesToClean = new ArrayList[nbCRRules];
        for (int i = 0; i < nbCRRules; i++) {
            occurencesToClean[i] = new ArrayList();
        }
    }

    private void liberateCleanUpUtilities() {
        modelIntegrityReport.setCleanUpMode(false);
        modelIntegrityReport = null;
    }

}
