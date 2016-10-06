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

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.sms.be.db.*;
import org.modelsphere.sms.db.DbSMSClassifier;

/**
 * @author pierrem
 * 
 */
public abstract class AbstractBEIntegrity extends AbstractIntegrity {

    protected static final String kBeModel = LocaleMgr.misc.getString("BE-BeModel");

    //spécifiques à BeIntegrity
    public static final int BE_IR_PROCESS_SYNCHRO_RULE = 0;
    public static final int BE_IR_FLOW_EMISSION_CONDITION = 1;
    public static final int BE_IR_OBJECT_ERRORS_WITH_FLOW = 2;
    public static final int BE_IX_WITHOUT_FLOW_IN = 3;
    public static final int BE_IX_WITHOUT_FLOW_OUT = 4;
    public static final int BE_IX_ORPHAN_OCCURENCE = 5;
    public static final int BE_IR_OBJECT_LINKED_ERRORS = 6;
    public static final int BE_IX_PROC_TO_PROC = 7;
    public static final int BE_IX_STORE_TO_STORE = 8;
    public static final int BE_IX_ACTOR_TO_ACTOR = 9;
    public static final int BE_IX_BAD_LINK_TO_OCCUR = 10;
    public static final int BE_IR_WITHOUT_GO = 11;
    public static final int BE_IR_FLOW_ERRORS = 12;
    public static final int BE_IX_ONE_OBJECT_LINKED = 13;
    public static final int BE_IX_NO_OBJECT_LINKED = 14;
    public static final int BE_IR_DEFAULT_NAME = 15;

    final static String[] beIntegrityMessages = {
            LocaleMgr.misc.getString("BE-IR_ProcessSynchroRule"),//0
            LocaleMgr.misc.getString("BE-IR_FlowEmissionCondition"),
            LocaleMgr.misc.getString("BE-IR_ObjectErrorsWithFlowLink"),
            LocaleMgr.misc.getString("BE-IX_WithoutFlowIn"),
            LocaleMgr.misc.getString("BE-IX_WithoutFlowOut"),
            LocaleMgr.misc.getString("BE-IX_OrphanOccurence"),//5
            LocaleMgr.misc.getString("BE-IR_ObjectLinkedErrors"),
            LocaleMgr.misc.getString("BE-IX_ProcessLinkToProcess"),
            LocaleMgr.misc.getString("BE-IX_StoreToStore"),
            LocaleMgr.misc.getString("BE-IX_ActorToActor"),
            LocaleMgr.misc.getString("BE-IX_BadLinkToOccurence"),//10  
            LocaleMgr.misc.getString("BE-GR_WithoutGO"),
            LocaleMgr.misc.getString("BE-IR_FlowErrors"),
            LocaleMgr.misc.getString("BE-IX_OneObjectLinked"),
            LocaleMgr.misc.getString("BE-IX_NoObjectLinked"),
            LocaleMgr.misc.getString("BE-IR_DefaultName"),//15
    };

    final static Boolean[] beIntegrityErrorFlags = { Boolean.FALSE,//0
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,//5
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,//10
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,//15
    };

    /*
     * BeCleanup
     */
    //ruleIndex spécifiques à BeCleanup 

    public static final int BE_CR_RESOURCE_NOT_USED = 0;
    public static final int BE_CR_WITHOUT_GO = 1;
    public static final int BE_CR_NB_INDEX = 2;//pour arrayList 

    final static String[] beCleanUpMessages = { LocaleMgr.misc.getString("BE-CR_ResourceNotUsed"),
            LocaleMgr.misc.getString("BE-GR_WithoutGO"), };

    final static Boolean[] beCleanUpErrorFlags = { Boolean.FALSE,//0
            Boolean.FALSE, };

    final static String[] cleanUpSectionLinkStr = { "DELETE_RESOURCES_NOT_USED", // NOT LOCALIZABLE
            "DELETE_OCCURENCE_WITHOUT_GO" // NOT LOCALIZABLE  
    };

    /*
     * public static final int PROCESS = 0; public static final int ACTOR = 1; public static final
     * int STORE = 2;
     */

    public ArrayList useCaseList;
    public ArrayList actorList;
    public ArrayList storeList;
    public ArrayList flowList;
    public ArrayList resourceList;

    //méthodes 
    public void getAllObjects(DbObject beModel) throws DbException {

        useCaseList = getOccurrences(beModel, DbBEUseCase.metaClass);
        actorList = getOccurrences(beModel, DbBEActor.metaClass);
        storeList = getOccurrences(beModel, DbBEStore.metaClass);
        flowList = getOccurrences(beModel, DbBEFlow.metaClass);
        resourceList = getOccurrences(beModel, DbBEResource.metaClass);
    }

    /**
     * Occurence sans représentation graphique
     */
    public void verifyOccurenceWithoutGO(ArrayList aList, StringBuffer buffer, int operation)
            throws DbException {
        for (int i = 0; i < aList.size(); i++) {
            DbSMSClassifier semObj = (DbSMSClassifier) (aList.get(i));
            DbRelationN relN = semObj.getClassifierGos();
            if (relN.size() == 0) {
                if (operation == INTEGRITY)
                    modelIntegrityReport.printError(BE_IR_WITHOUT_GO, buffer, semObj, false, true);
                else {
                    if (semObj instanceof DbBEUseCase) {
                        boolean isExtern = ((DbBEUseCase) semObj).isExternal();
                        if (isExtern)
                            modelIntegrityReport.printError(BE_CR_WITHOUT_GO, buffer, semObj,
                                    false, true);
                    } else {
                        modelIntegrityReport.printError(BE_CR_WITHOUT_GO, buffer, semObj, false,
                                true);
                    }
                    occurencesToClean[BE_CR_WITHOUT_GO].add(semObj);
                }
                modelWarningCount++;
            }
        }
    }
}
