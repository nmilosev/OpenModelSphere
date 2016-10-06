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

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.sms.be.db.*;
import org.modelsphere.sms.db.DbSMSClassifier;

/**
 * @author pierrem
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class BeIntegrity extends AbstractBEIntegrity {

    public BeIntegrity() {
    }

    private String[] reportMessages;
    private Boolean[] errorFlags;
    private Boolean[] workErrorFlags;
    private StringBuffer warningStrBuffer = new StringBuffer();
    private StringBuffer errorStrBuffer = new StringBuffer();

    //
    // public methods
    //      
    public void verifyBeModel(DbBEModel beModel) throws DbException, IOException {
        initializeIntegrityUtilities();

        verifyIntegrityRules(beModel);
        modelIntegrityReport.showReport(beModel, modelErrorCount, modelWarningCount, INTEGRITY);

        liberateIntegrityUtilities();
    }

    //
    // private methods
    //
    private void verifyIntegrityRules(DbBEModel beModel) throws DbException {
        getAllObjects(beModel);

        // --- Warnings ---
        verifyProcessSynchroRule(useCaseList, modelIntegrityReport.warningStrBuffer);
        verifyFlowEmissionCondition(flowList, modelIntegrityReport.warningStrBuffer);

        verifyObjectTypeLinked(useCaseList, modelIntegrityReport.warningStrBuffer);
        verifyObjectTypeLinked(actorList, modelIntegrityReport.warningStrBuffer);
        verifyObjectTypeLinked(storeList, modelIntegrityReport.warningStrBuffer);

        verifyOccurencesWithFlowError(useCaseList, modelIntegrityReport.warningStrBuffer);
        verifyOccurencesWithFlowError(actorList, modelIntegrityReport.warningStrBuffer);
        verifyOccurencesWithFlowError(storeList, modelIntegrityReport.warningStrBuffer);
        verifyFlowLinking(flowList, modelIntegrityReport.warningStrBuffer);

        verifyOccurenceWithoutGO(useCaseList, modelIntegrityReport.warningStrBuffer, INTEGRITY);
        verifyOccurenceWithoutGO(actorList, modelIntegrityReport.warningStrBuffer, INTEGRITY);
        verifyOccurenceWithoutGO(storeList, modelIntegrityReport.warningStrBuffer, INTEGRITY);

        verifyDefaultName(useCaseList, modelIntegrityReport.warningStrBuffer);
        verifyDefaultName(actorList, modelIntegrityReport.warningStrBuffer);
        verifyDefaultName(storeList, modelIntegrityReport.warningStrBuffer);
        verifyFlowDefaultName(flowList, modelIntegrityReport.warningStrBuffer);
    }

    /**
     * Processus sans regle de synchronisation
     */
    private void verifyProcessSynchroRule(ArrayList aList, StringBuffer buffer) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbBEUseCase semObj = (DbBEUseCase) (aList.get(i));
            if ((semObj.getSynchronizationRule() == null)
                    || (semObj.getSynchronizationRule().length() == 0)) {
                modelIntegrityReport.printError(BE_IR_PROCESS_SYNCHRO_RULE, buffer, semObj, false);
                modelWarningCount++;
            }
        }
    }

    /**
     *  
     */
    private void verifyDefaultName(ArrayList aList, StringBuffer buffer) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbSMSClassifier semObj = (DbSMSClassifier) (aList.get(i));
            boolean defaultnameFound = false;

            if (semObj instanceof DbBEUseCase) {
                if ((((DbBEUseCase) semObj).isContext())
                        && (semObj.getName()
                                .equals(org.modelsphere.sms.be.international.LocaleMgr.misc
                                        .getString("context"))))
                    defaultnameFound = true;
                else if (semObj.getName().equals(
                        org.modelsphere.sms.be.international.LocaleMgr.misc.getString("process"))) {
                    defaultnameFound = true;
                }
            } else if (semObj instanceof DbBEActor) {
                if (semObj.getName().equals(
                        org.modelsphere.sms.be.international.LocaleMgr.misc
                                .getString("externalentity"))) {
                    defaultnameFound = true;
                }
            } else if (semObj instanceof DbBEStore) {
                if (semObj.getName().equals(
                        org.modelsphere.sms.be.international.LocaleMgr.misc.getString("store"))) {
                    defaultnameFound = true;
                }
            }

            if (defaultnameFound) {
                modelIntegrityReport.printError(BE_IR_DEFAULT_NAME, buffer, semObj, false);
                modelWarningCount++;
            }
        }
    }

    /**
     *  
     */
    private void verifyFlowDefaultName(ArrayList aList, StringBuffer buffer) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbBEFlow semObj = (DbBEFlow) (aList.get(i));
            DbEnumeration flows = null;

            String name = (semObj).getName();
            if (name == null)
                return;
            if (name.equals(org.modelsphere.sms.be.international.LocaleMgr.misc.getString("flow"))) {
                modelIntegrityReport.printError(BE_IR_DEFAULT_NAME, buffer, semObj, false);
                modelWarningCount++;
            }
        }
    }

    /**
     * On vérifie si l'objet n'a aucun flux relié ou des flux seulement en entrée ou seulement en
     * sortie
     */
    private void verifyOccurencesWithFlowError(ArrayList aList, StringBuffer buffer)
            throws DbException {
        int nbFirstEnd;
        int nbSecondEnd;

        for (int i = 0; i < aList.size(); i++) {
            DbSMSClassifier semObj = (DbSMSClassifier) (aList.get(i));
            ArrayList ObjectLinkedList = new ArrayList();
            DbEnumeration flowsFirst = null;
            DbEnumeration flowsSecond = null;
            nbFirstEnd = 0;
            nbSecondEnd = 0;

            if (semObj instanceof DbBEUseCase) {
                if (((DbBEUseCase) semObj).isContext())
                    continue;
                flowsFirst = ((DbBEUseCase) semObj).getFirstEndFlows().elements();
                flowsSecond = ((DbBEUseCase) semObj).getSecondEndFlows().elements();
            } else if (semObj instanceof DbBEActor) {
                flowsFirst = ((DbBEActor) semObj).getFirstEndFlows().elements();
                flowsSecond = ((DbBEActor) semObj).getSecondEndFlows().elements();
            } else if (semObj instanceof DbBEStore) {
                flowsFirst = ((DbBEStore) semObj).getFirstEndFlows().elements();
                flowsSecond = ((DbBEStore) semObj).getSecondEndFlows().elements();
            }

            while (flowsFirst.hasMoreElements()) {
                DbBEFlow flow = (DbBEFlow) flowsFirst.nextElement();
                nbFirstEnd++;
            }
            flowsFirst.close();

            while (flowsSecond.hasMoreElements()) {
                DbBEFlow flow = (DbBEFlow) flowsSecond.nextElement();
                nbSecondEnd++;
            }
            flowsSecond.close();

            if (nbFirstEnd > 0 && nbSecondEnd == 0) {//pas d'entree
                modelIntegrityReport.printError(BE_IR_OBJECT_ERRORS_WITH_FLOW, buffer, semObj,
                        false, true, BE_IX_WITHOUT_FLOW_IN);
                modelWarningCount++;
            } else if (nbFirstEnd == 0 && nbSecondEnd > 0) {//pas de sortie
                modelIntegrityReport.printError(BE_IR_OBJECT_ERRORS_WITH_FLOW, buffer, semObj,
                        false, true, BE_IX_WITHOUT_FLOW_OUT);
                modelWarningCount++;
            } else if (nbFirstEnd == 0 && nbSecondEnd == 0) { //aucun flux rattaché
                modelIntegrityReport.printError(BE_IR_OBJECT_ERRORS_WITH_FLOW, buffer, semObj,
                        false, true, BE_IX_ORPHAN_OCCURENCE);
                modelWarningCount++;
            }
        }
    }

    /**
     *  
     */
    private void verifyObjectTypeLinked(ArrayList aList, StringBuffer buffer) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbSMSClassifier semObj = (DbSMSClassifier) (aList.get(i));
            ArrayList ObjectLinkedList = new ArrayList();
            DbEnumeration flows = null;
            boolean priorMsg = false;
            int MsgExtraIndex = -1;

            if (semObj instanceof DbBEUseCase) {
                if (((DbBEUseCase) semObj).isContext())
                    continue;
                flows = ((DbBEUseCase) semObj).getSecondEndFlows().elements();
            } else if (semObj instanceof DbBEActor) {
                flows = ((DbBEActor) semObj).getSecondEndFlows().elements();

            } else if (semObj instanceof DbBEStore) {
                flows = ((DbBEStore) semObj).getSecondEndFlows().elements();
            }

            if (flows != null) { //semble inutile mais je le laisse : un client aura l'application demain 
                while (flows.hasMoreElements()) {
                    DbBEFlow flow = (DbBEFlow) flows.nextElement();
                    DbSMSClassifier end1 = null;
                    end1 = flow.getFirstEnd();
                    MsgExtraIndex = -1;

                    if (end1 != null) {
                        if ((semObj instanceof DbBEUseCase) && (end1 instanceof DbBEUseCase)) {
                            MsgExtraIndex = BE_IX_PROC_TO_PROC;
                        }
                        if ((semObj instanceof DbBEActor) && (end1 instanceof DbBEActor)) {
                            if (!priorMsg)
                                MsgExtraIndex = BE_IX_ACTOR_TO_ACTOR;
                        }

                        if ((semObj instanceof DbBEStore) && (end1 instanceof DbBEStore)) {
                            if (!priorMsg)
                                MsgExtraIndex = BE_IX_STORE_TO_STORE;
                        }

                        if ((semObj instanceof DbBEStore) && (end1 instanceof DbBEActor)
                                || (semObj instanceof DbBEActor) && (end1 instanceof DbBEStore)) {
                            MsgExtraIndex = BE_IX_BAD_LINK_TO_OCCUR;
                            priorMsg = true;
                        }

                        if ((MsgExtraIndex >= 0) && (!ObjectLinkedList.contains(end1)))
                            ObjectLinkedList.add(end1);
                    }
                }
                flows.close();
            }

            if (ObjectLinkedList.size() == 0)
                continue;

            Iterator iter = ObjectLinkedList.iterator();
            String objectStrList = "";
            boolean first = true;
            while (iter.hasNext()) {
                if (!first)
                    objectStrList = objectStrList + ", "; // NOT LOCALIZABLE
                objectStrList = objectStrList
                        + modelIntegrityReport.getLinksForDbObject((DbSMSClassifier) iter.next(),
                                true, false);
                first = false;
            }
            modelIntegrityReport.printError(BE_IR_OBJECT_LINKED_ERRORS, buffer, semObj, false,
                    true, MsgExtraIndex, objectStrList);
            modelWarningCount++;
        }
    }

    /**
     * Flux sans condition d'émission
     */
    private void verifyFlowEmissionCondition(ArrayList aList, StringBuffer buffer)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbBEFlow semObj = (DbBEFlow) (aList.get(i));
            if ((semObj.getEmissionCondition() == null)
                    || (semObj.getEmissionCondition().length() == 0)) {
                modelIntegrityReport.printError(BE_IR_FLOW_EMISSION_CONDITION, buffer, semObj,
                        false);
                modelWarningCount++;
            }
        }
    }

    /**
     * Flux liés incorectement
     */
    private void verifyFlowLinking(ArrayList aList, StringBuffer buffer) throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbBEFlow semObj = (DbBEFlow) (aList.get(i));

            if ((semObj.getFirstEnd() == null) && (semObj.getSecondEnd() == null)) {
                modelIntegrityReport.printError(BE_IR_FLOW_ERRORS, buffer, semObj, false, false,
                        BE_IX_NO_OBJECT_LINKED);
                modelWarningCount++;
            } else if (((semObj.getFirstEnd() == null) && (semObj.getSecondEnd() != null))
                    || (semObj.getFirstEnd() != null) && (semObj.getSecondEnd() == null)) {
                modelIntegrityReport.printError(BE_IR_FLOW_ERRORS, buffer, semObj, false, false,
                        BE_IX_ONE_OBJECT_LINKED);
                modelWarningCount++;
            }
        }
    }

    private String[] getReportMessages() {
        return AbstractBEIntegrity.beIntegrityMessages;
    }

    private Boolean[] initializeErrorFlags() {
        return AbstractBEIntegrity.beIntegrityErrorFlags;
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
