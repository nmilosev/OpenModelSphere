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

package org.modelsphere.sms.be;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.db.event.DbUpdateListener;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseResource;
import org.modelsphere.sms.be.db.srtypes.BETimeUnit;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.db.DbSMSSemanticalObject;

public final class BESemanticalIntegrity implements DbUpdateListener {

    public static final int NA = 0;
    public static final int SMH = 1;

    public BESemanticalIntegrity() {
        MetaField.addDbUpdateListener(this, 0, new MetaField[] { DbObject.fComposite,
                DbObject.fComponents, DbBEDiagram.fNotation, DbBEUseCase.fFixedCost,
                DbBEUseCase.fFixedTime, DbBEUseCase.fFixedTimeUnit, DbBEUseCaseResource.fUsageRate,
                DbBEUseCaseResource.fUsageRateTimeUnit, DbBEResource.fCost,
                DbBEResource.fCostTimeUnit, DbSMSSemanticalObject.fName, DbBEFlow.fArrowFirstEnd,
                DbBEFlow.fArrowSecondEnd,
        // DbORPrimaryUnique.fColumns, DbORIndex.fConstraint,
                // DbORIndexKey.fIndexedElement,
                });
    }

    // //////////////////////////////
    // DbUpdateListener SUPPORT
    //

    public void dbUpdated(DbUpdateEvent event) throws DbException {

        if (event.metaField == DbObject.fComponents) {
            if ((event.dbo instanceof DbBEUseCase || event.dbo instanceof DbBEModel)
                    && (event.neighbor instanceof DbBEUseCase || event.neighbor instanceof DbBEDiagram)) {

                String term1 = null, term2 = null;
                boolean bApplyRule = true, isLeaf1 = false, isLeaf2 = false;
                DbObject eventDbo1 = event.dbo, eventNeighbor = event.neighbor;

                // 1) if the source and target models are not both Use Case
                // Models

                if (eventNeighbor instanceof DbBEUseCase)
                    term2 = ((DbBEUseCase) eventNeighbor).getTerminologyName();
                else
                    term2 = ((DbBEDiagram) eventNeighbor).findMasterNotation().getName();

                // //
                // when the target or source is a leaf, the term will be null
                // and we must go up to the model node
                // to identify the model kind

                if (eventDbo1 instanceof DbBEUseCase) {
                    DbObject dboComposite = eventDbo1.getComposite();
                    if (dboComposite instanceof DbBEUseCase) {
                        if (term1 == null)
                            term1 = ((DbBEUseCase) dboComposite).getTerminologyName();
                        else if (term1.equals(""))
                            term1 = ((DbBEUseCase) dboComposite).getTerminologyName();
                    }
                    if (term1 == null)
                        term1 = ((DbBEModel) ((DbBEUseCase) eventDbo1)
                                .getCompositeOfType(DbBEModel.metaClass)).getTerminologyName();
                    if (term1.equals(""))
                        term1 = ((DbBEModel) ((DbBEUseCase) eventDbo1)
                                .getCompositeOfType(DbBEModel.metaClass)).getTerminologyName();
                } else { // it's a model
                    if (term1 == null)
                        term1 = ((DbBEModel) eventDbo1).getTerminologyName();
                    if (term1.equals(""))
                        term1 = ((DbBEModel) eventDbo1).getTerminologyName();
                }

                boolean getCompositeTerm = false;
                if (term2 == null)
                    getCompositeTerm = true;
                else if (term2.equals(""))
                    getCompositeTerm = true;

                if (getCompositeTerm) {
                    DbObject dboComposite = eventNeighbor.getComposite();
                    if (dboComposite instanceof DbBEUseCase) {
                        if (term2 == null)
                            term2 = ((DbBEUseCase) dboComposite).getTerminologyName();
                        else if (term2.equals(""))
                            term2 = ((DbBEUseCase) dboComposite).getTerminologyName();
                    }
                    if (term2 == null)
                        term2 = ((DbBEModel) ((DbBEUseCase) eventNeighbor)
                                .getCompositeOfType(DbBEModel.metaClass)).getTerminologyName();
                    if (term2.equals(""))
                        term2 = ((DbBEModel) ((DbBEUseCase) eventNeighbor)
                                .getCompositeOfType(DbBEModel.metaClass)).getTerminologyName();
                }

                DbBENotation notation = (DbBENotation) eventNeighbor.getProject()
                        .findComponentByName(DbBENotation.metaClass, term2);
                int masterID = notation.getMasterNotationID().intValue();
                boolean isModel2UML = (masterID >= 13 && masterID <= 19) ? true : false;

                notation = (DbBENotation) eventDbo1.getProject().findComponentByName(
                        DbBENotation.metaClass, term1);

                masterID = notation.getMasterNotationID().intValue();
                boolean isModel1UML = (masterID >= 13 && masterID <= 19) ? true : false;

                String terminologyName = null;
                if (eventDbo1 instanceof DbBEModel)
                    terminologyName = ((DbBEModel) eventDbo1).getTerminologyName();
                else
                    terminologyName = ((DbBEUseCase) eventDbo1).getTerminologyName();

                if (terminologyName == null)
                    isLeaf1 = true;
                else if (terminologyName.equals(""))
                    isLeaf1 = true;

                if (eventNeighbor instanceof DbBEDiagram)
                    terminologyName = ((DbBEDiagram) eventNeighbor).findMasterNotation()
                            .getTerminologyName();
                else
                    terminologyName = ((DbBEUseCase) eventNeighbor).getTerminologyName();

                if (terminologyName == null)
                    isLeaf2 = true;
                else if (terminologyName.equals(""))
                    isLeaf2 = true;

                if ((term1.equals(DbBENotation.UML_USE_CASE) && isModel2UML)
                        || (isLeaf2 && isLeaf1))
                    bApplyRule = false;

                // //
                // if both models belong to process modeling or both models
                // belong to UML, do not process !!!

                if (isModel1UML == isModel2UML) {
                    if (false == isModel1UML)
                        bApplyRule = false;
                    else {
                        // if terminologies differ, apply rule ortherwise no
                        if (term1 != null)
                            if (term1.equals(term2))
                                bApplyRule = false;
                    }
                }
                if (isLeaf2 == true && isLeaf1 == false)
                    bApplyRule = false;
                else if (isLeaf2 == true && isLeaf1 == true)
                    bApplyRule = true;

                if (true == bApplyRule)
                    BEUtility.updateUseCaseTerminology(eventDbo1);
            }
        }

        // Attention la séquence des objets détruits est importante
        // Je considère ici que le premier trappé est l'objet détruit
        // directement suivi des autres
        // qui le sont en cascade.

        if (event.metaField == DbObject.fComposite) {

            if (event.dbo instanceof DbBEUseCaseResource
                    && event.dbo.getTransStatus() == Db.OBJ_REMOVED) {
                Object objComp = ((DbBEUseCaseResource) event.dbo)
                        .getOld(DbBEUseCaseResource.fComposite);
                if ((objComp instanceof DbBEUseCase)
                        && (((DbBEUseCase) objComp).getTransStatus() != Db.OBJ_REMOVED)) {
                    calculateCostAndTimeOfResource((DbBEUseCase) objComp);
                    calculatePartialCost((DbBEUseCase) objComp);
                    calculatePartialTime((DbBEUseCase) objComp);
                    calculateTotalCost((DbBEUseCase) objComp);
                    calculateTotalTime((DbBEUseCase) objComp);
                }
                Object objRsrc = ((DbBEUseCaseResource) event.dbo)
                        .getOld(DbBEUseCaseResource.fResource);
                // TODO Check this
                if (objRsrc == null)
                    return;
                if (((DbBEResource) objRsrc).getTransStatus() != Db.OBJ_REMOVED)
                    calculateWorkLoadOfResource((DbBEResource) objRsrc);
            }

            else if (event.dbo instanceof DbBEUseCase
                    && event.dbo.getTransStatus() == Db.OBJ_REMOVED) {
                Object objComp = ((DbBEUseCase) event.dbo).getOld(DbBEUseCase.fComposite);
                if ((objComp instanceof DbBEUseCase)
                        && (((DbBEUseCase) objComp).getTransStatus() != Db.OBJ_REMOVED)) {
                    calculateTotalCost((DbBEUseCase) objComp);
                    calculateTotalTime((DbBEUseCase) objComp);
                }
            }

            if (event.dbo instanceof DbBEDiagram && event.dbo.getTransStatus() == Db.OBJ_ADDED) {
                initDiagram(event);
            }

        } else if (event.metaField == DbObject.fComponents) {
            if (event.dbo instanceof DbBEUseCaseResource
                    && event.dbo.getTransStatus() == Db.OBJ_ADDED) {
                Object objComp = ((DbBEUseCaseResource) event.dbo).getComposite();
                if (objComp instanceof DbBEUseCase) {
                    calculateCostAndTimeOfResource((DbBEUseCase) objComp);
                    calculatePartialCost((DbBEUseCase) objComp);
                    calculatePartialTime((DbBEUseCase) objComp);
                    calculateTotalCost((DbBEUseCase) objComp);
                    calculateTotalTime((DbBEUseCase) objComp);
                }
                DbBEResource objRsrc = ((DbBEUseCaseResource) event.dbo).getResource();
                calculateWorkLoadOfResource(objRsrc);
            }
            if (event.dbo instanceof DbBEUseCase && event.dbo.getTransStatus() == Db.OBJ_ADDED) {
                Object objComp = ((DbBEUseCase) event.dbo).getComposite();
                if (objComp instanceof DbBEUseCase) {
                    calculateTotalCost((DbBEUseCase) objComp);
                    calculateTotalTime((DbBEUseCase) objComp);
                }
            }
        } else if ((event.metaField == DbBEResource.fCost)
                || (event.metaField == DbBEResource.fCostTimeUnit)) {
            DbEnumeration dbEnum = ((DbBEResource) event.dbo).getProcessUsages().elements();
            while (dbEnum.hasMoreElements()) {
                DbObject dboElem = ((DbBEUseCaseResource) dbEnum.nextElement()).getComposite();
                if (dboElem instanceof DbBEUseCase) {
                    calculateCostAndTimeOfResource((DbBEUseCase) dboElem);
                    calculatePartialCost((DbBEUseCase) dboElem);
                    calculatePartialTime((DbBEUseCase) dboElem);
                    calculateTotalCost((DbBEUseCase) dboElem);
                    calculateTotalTime((DbBEUseCase) dboElem);

                }
            }
            dbEnum.close();
        } else if ((event.metaField == DbBEUseCaseResource.fUsageRate)
                || (event.metaField == DbBEUseCaseResource.fUsageRateTimeUnit)) {
            DbObject dboComp = ((DbBEUseCaseResource) event.dbo).getComposite();
            if (dboComp instanceof DbBEUseCase) {
                calculateCostAndTimeOfResource((DbBEUseCase) dboComp);
                calculatePartialCost((DbBEUseCase) dboComp);
                calculatePartialTime((DbBEUseCase) dboComp);
                calculateTotalCost((DbBEUseCase) dboComp);
                calculateTotalTime((DbBEUseCase) dboComp);
            }
            DbBEResource resource = ((DbBEUseCaseResource) event.dbo).getResource();
            calculateWorkLoadOfResource(resource);
        } else if (event.metaField == DbBEUseCase.fFixedCost) {
            calculatePartialCost((DbBEUseCase) event.dbo);
            calculateTotalCost((DbBEUseCase) event.dbo);
        } else if ((event.metaField == DbBEUseCase.fFixedTime)
                || (event.metaField == DbBEUseCase.fFixedTimeUnit)) {
            calculatePartialTime((DbBEUseCase) event.dbo);
            calculateTotalTime((DbBEUseCase) event.dbo);
        } else if (event.dbo instanceof DbBEUseCase
                && event.metaField == DbSMSSemanticalObject.fName
                || event.dbo instanceof DbBEFlow
                && (event.metaField == DbBEFlow.fFirstEnd || event.metaField == DbBEFlow.fSecondEnd)) {
            DbBEUseCase useCase = (DbBEUseCase) event.dbo;
            DbEnumeration enumeration = useCase.getFirstEndFlows().elements(DbBEFlow.metaClass);
            while (enumeration.hasMoreElements()) {
                DbBEFlow flow = (DbBEFlow) enumeration.nextElement();
                DbUpdateEvent eventFlow = new DbUpdateEvent(flow, DbBEFlow.fName);
                ApplicationContext.getDefaultMainFrame().getExplorerPanel().getExplorer()
                        .refreshAfterDbUpdate(eventFlow);
            }
            enumeration.close();
            enumeration = useCase.getSecondEndFlows().elements(DbBEFlow.metaClass);
            while (enumeration.hasMoreElements()) {
                DbBEFlow flow = (DbBEFlow) enumeration.nextElement();
                DbUpdateEvent eventFlow = new DbUpdateEvent(flow, DbBEFlow.fName);
                ApplicationContext.getDefaultMainFrame().getExplorerPanel().getExplorer()
                        .refreshAfterDbUpdate(eventFlow);
            }
            enumeration.close();
        }

    } // end dbUpdated

    //
    // End of DbUpdateListener SUPPORT
    // ////////////////////////////////

    /**
     * Calculate the total time of Process
     */
    private void calculateTotalTime(DbBEUseCase process) throws DbException {
        // in
        double childTime = 0;
        int childUnit = NA;

        // work
        double childTotalTime = 0;
        double processPartialTime = 0;
        int processPartialUnit = NA;
        int childUnitSet = NA;
        int processUnitSet = NA;
        boolean firstChild = true;
        boolean isSameTotal = false;

        // out
        double processTotalTime = 0;
        int greaterTotalUnit = NA;

        // calculate total of child
        DbEnumeration dbEnum = process.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = (DbObject) dbEnum.nextElement();
            if (dbo instanceof DbBEUseCase) {
                DbBEUseCase childProcess = (DbBEUseCase) dbo;

                Double tmpTime = childProcess.getTotalTime();
                childTime = (tmpTime != null ? tmpTime.doubleValue() : 0.0);
                BETimeUnit tmpUnit = childProcess.getTotalTimeUnit();
                childUnit = (tmpUnit != null ? tmpUnit.getValue() : NA);

                if (Double.isNaN(childTime) || (childTime > 0.0)) {
                    if (firstChild == true) {
                        childUnitSet = (childUnit > 0 ? SMH : NA);
                        firstChild = false;
                    } else {
                        int tmpTimeSet = (childUnit > 0 ? SMH : NA);
                        if (childUnitSet != tmpTimeSet) {
                            childTotalTime = getUndefinedValue();
                            greaterTotalUnit = NA;
                            break;
                        }
                    }
                    if (childUnit > greaterTotalUnit) {
                        greaterTotalUnit = childUnit;
                    }
                    childTotalTime += getBaseTime(childTime, childUnit);
                }
            }
        }
        dbEnum.close();

        // Set current total (child + current)
        Double tmpTime = process.getPartialTime();
        processPartialTime = (tmpTime != null ? tmpTime.doubleValue() : 0.0);
        BETimeUnit tmpUnit = process.getPartialTimeUnit();
        processPartialUnit = (tmpUnit != null ? tmpUnit.getValue() : NA);
        processUnitSet = (processPartialUnit > 0 ? SMH : NA);
        processPartialTime = getBaseTime(processPartialTime, processPartialUnit);

        if (Double.isNaN(childTotalTime) || Double.isNaN(childTotalTime)) {
            // if one of them is NaN
            processTotalTime = getUndefinedValue();
            greaterTotalUnit = NA;
        } else if ((childTotalTime > 0.0) && (processPartialTime > 0.0)) {
            // if there are two values to add
            if (processUnitSet == childUnitSet) {
                greaterTotalUnit = (processPartialUnit > greaterTotalUnit) ? processPartialUnit
                        : greaterTotalUnit;
                processTotalTime = getConvertValue((childTotalTime + processPartialTime),
                        greaterTotalUnit);
            } else { // incompatible time unit
                processTotalTime = getUndefinedValue();
                greaterTotalUnit = NA;
            } // end if
        } else {
            // if only one value or two null
            greaterTotalUnit = ((processPartialTime > 0.0) ? processPartialUnit : greaterTotalUnit);
            processTotalTime = getConvertValue((childTotalTime + processPartialTime),
                    greaterTotalUnit);
        } // end if

        Double tmpTime1 = process.getTotalTime();
        double currentTotalTime = (tmpTime1 != null ? tmpTime1.doubleValue() : 0.0);
        BETimeUnit tmpUnit1 = process.getTotalTimeUnit();
        double currentTotalUnit = (tmpUnit1 != null ? tmpUnit1.getValue() : NA);
        if ((currentTotalTime == processTotalTime) && (currentTotalUnit == greaterTotalUnit))
            isSameTotal = true;
        else {
            process.setTotalTime(new Double(processTotalTime));
            process.setTotalTimeUnit((greaterTotalUnit > 0) ? BETimeUnit
                    .getInstance(greaterTotalUnit) : null);
        }
        // While process has a composite (until context)
        DbObject dbo = process.getComposite();
        if ((dbo != null) && (dbo instanceof DbBEUseCase) && (!isSameTotal)) {
            DbBEUseCase parentProcess = (DbBEUseCase) dbo;
            calculateTotalTime(parentProcess);
        }
    }

    /**
     * Calculate the total cost of Process
     */
    private void calculateTotalCost(DbBEUseCase process) throws DbException {
        // in
        double childCost = 0;
        // work
        double childTotalCost = 0;
        double processPartialCost = 0;
        boolean isSameTotal = false;
        // out
        double processTotalCost = 0;

        // calculate total of child
        DbEnumeration dbEnum = process.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = (DbObject) dbEnum.nextElement();
            if (dbo instanceof DbBEUseCase) {
                DbBEUseCase childProcess = (DbBEUseCase) dbo;
                Double totalCost = childProcess.getTotalCost();
                childCost = (totalCost != null ? totalCost.doubleValue() : 0.0);
                childTotalCost += childCost;
            }
        }
        dbEnum.close();

        // Set current total (child + current)
        Double partialCost = process.getPartialCost();
        processPartialCost = (partialCost != null ? partialCost.doubleValue() : 0.0);
        processTotalCost = childTotalCost + processPartialCost;

        Double tmpCost = process.getTotalCost();
        double currentTotalCost = (tmpCost != null ? tmpCost.doubleValue() : 0.0);

        if (currentTotalCost == processTotalCost)
            isSameTotal = true;
        else
            process.setTotalCost(new Double(processTotalCost));

        // While process has a composite (until context)
        DbObject dbo = process.getComposite();
        if ((dbo != null) && (dbo instanceof DbBEUseCase) && (!isSameTotal)) {
            DbBEUseCase parentProcess = (DbBEUseCase) dbo;
            calculateTotalCost(parentProcess);
        }
    }

    /**
     * Calculate the partial cost of Process
     */
    private void calculatePartialCost(DbBEUseCase process) throws DbException {
        // in
        double procFixedCost;
        double procResourceCost;

        // out
        double partialCost = 0;

        Double fixedCost = process.getFixedCost();
        procFixedCost = (fixedCost != null ? fixedCost.doubleValue() : 0.0);
        Double rsrcCost = process.getResourceCost();
        procResourceCost = (rsrcCost != null ? rsrcCost.doubleValue() : 0.0);
        partialCost = procFixedCost + procResourceCost;
        process.setPartialCost(new Double(partialCost));
    }

    /**
     * Calculate the partial time of Process
     */
    private void calculatePartialTime(DbBEUseCase process) throws DbException {
        // in
        double procFixedTime = 0;
        double procResourceTime = 0;
        int procFixedUnit = NA;
        int procResourceUnit = NA;

        // work
        int rsrcUnitSet = 0;
        int fixedUnitSet = NA;
        // out
        double partialTime = 0;
        int partialUnit = NA;

        Double fixedTime = process.getFixedTime();
        procFixedTime = (fixedTime != null ? fixedTime.doubleValue() : 0.0);
        Double rsrcTime = process.getResourceTime();
        procResourceTime = (rsrcTime != null ? rsrcTime.doubleValue() : 0.0);
        BETimeUnit fixedUnit = process.getFixedTimeUnit();
        procFixedUnit = (fixedUnit != null ? fixedUnit.getValue() : NA);
        BETimeUnit resourceUnit = process.getResourceTimeUnit();
        procResourceUnit = (resourceUnit != null ? resourceUnit.getValue() : NA);
        fixedUnitSet = (procFixedUnit > 0 ? SMH : NA);
        rsrcUnitSet = (procResourceUnit > 0 ? SMH : NA);

        if (Double.isNaN(procFixedTime) || Double.isNaN(procResourceTime)) {
            // if one of them is NaN
            partialTime = getUndefinedValue();
            partialUnit = NA;
        } else if ((procFixedTime > 0.0) && (procResourceTime > 0.0)) {
            // if there are two values to add
            if (fixedUnitSet == rsrcUnitSet) {
                procFixedTime = getBaseTime(procFixedTime, procFixedUnit);
                procResourceTime = getBaseTime(procResourceTime, procResourceUnit);
                partialUnit = (procFixedUnit > procResourceUnit ? procFixedUnit : procResourceUnit);
                partialTime = getConvertValue((procFixedTime + procResourceTime), partialUnit);
            } else { // incompatible time unit
                partialTime = getUndefinedValue();
                partialUnit = NA;
            }
        } else {
            // if only one value or two null
            partialTime = procFixedTime + procResourceTime;
            partialUnit = ((procFixedTime > 0.0) ? procFixedUnit : procResourceUnit);
        }

        process.setPartialTime(new Double(partialTime));
        process.setPartialTimeUnit((partialUnit > 0) ? BETimeUnit.getInstance(partialUnit) : null);
    }

    /**
     * Calculate the worload of Resource
     */
    private void calculateWorkLoadOfResource(DbBEResource resource) throws DbException {
        // in
        double rsrcUsageRate;
        int rscrUsageRateUnit;
        // work
        int timeUnitSet = 0;
        boolean firstLink = true;
        // out
        double workLoad = 0;
        int workLoadUnit = NA;

        DbEnumeration dbEnum = resource.getProcessUsages().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = ((DbBEUseCaseResource) dbEnum.nextElement());
            DbBEUseCaseResource useCaseRsrc = ((DbBEUseCaseResource) dbo);

            Double usageRate = useCaseRsrc.getUsageRate();
            rsrcUsageRate = (usageRate != null ? usageRate.doubleValue() : 0.0);
            BETimeUnit usageRateUnit = useCaseRsrc.getUsageRateTimeUnit();
            rscrUsageRateUnit = (usageRateUnit != null ? usageRateUnit.getValue() : NA);

            if (Double.isNaN(rsrcUsageRate)) {
                workLoad = getUndefinedValue();
                workLoadUnit = NA;
            } else if (rsrcUsageRate > 0.0) {
                if (firstLink == true) {
                    timeUnitSet = (rscrUsageRateUnit > 0 ? SMH : NA);
                    firstLink = false;
                } else {
                    int tmpTimeSet = (rscrUsageRateUnit > 0 ? SMH : NA);
                    if (timeUnitSet != tmpTimeSet) { // incompatible time units
                        workLoad = getUndefinedValue();
                        workLoadUnit = NA;
                        break;
                    }
                }

                if (rscrUsageRateUnit > workLoadUnit) {
                    workLoadUnit = rscrUsageRateUnit;
                }
                workLoad += getBaseTime(rsrcUsageRate, rscrUsageRateUnit);
            }
        }
        dbEnum.close();

        workLoad = getConvertValue(workLoad, workLoadUnit);
        resource.setWorkLoad(new Double(workLoad));
        resource.setWorkLoadTimeUnit((workLoadUnit > 0) ? BETimeUnit.getInstance(workLoadUnit)
                : null);
    }

    /**
     * Calculate the cost and the time of all resources linked to a Process
     */
    private void calculateCostAndTimeOfResource(DbBEUseCase process) throws DbException {
        // in
        double rsrcUsageRate;
        int rscrUsageRateUnit;
        double rsrcCost;
        int rsrcCostUnit;
        // work
        double resultTime = 0;
        double tmpCost;
        double tmpUsageRate;
        int tmpTimeUnit;
        int timeUnitSet = 0;
        int useCaseRsrcUnitDiff = 0;
        boolean firstLink = true;
        boolean isIntraLinkUnitValid = true;
        boolean isRsrcFixedCost;

        // out
        double processRsrcTime = 0;
        double processRsrcCost = 0;
        int commonTimeUnit = 0;

        DbEnumeration dbEnum = process.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = (DbObject) dbEnum.nextElement();
            if (dbo instanceof DbBEUseCaseResource) {
                DbBEUseCaseResource useCaseRsrc = ((DbBEUseCaseResource) dbo);
                DbBEResource resource = useCaseRsrc.getResource();

                Double usageRate = useCaseRsrc.getUsageRate();
                rsrcUsageRate = (usageRate != null ? usageRate.doubleValue() : 0.0);
                Double cost = resource.getCost();
                rsrcCost = (cost != null ? cost.doubleValue() : 0.0);
                BETimeUnit usageRateUnit = useCaseRsrc.getUsageRateTimeUnit();
                rscrUsageRateUnit = (usageRateUnit != null ? usageRateUnit.getValue() : NA);
                BETimeUnit costUnit = resource.getCostTimeUnit();
                rsrcCostUnit = (costUnit != null ? costUnit.getValue() : NA);

                if (rsrcUsageRate > 0.0) { // Note : rsrcUsageRate cannot be
                    // Double.NaN, not a computed value
                    tmpCost = tmpUsageRate = 0.0;
                    tmpTimeUnit = NA;

                    isIntraLinkUnitValid = true;

                    // fixed cost only if rscrUsageRateUnit has units
                    isRsrcFixedCost = (rsrcCostUnit == NA) && (rscrUsageRateUnit != NA);
                    isIntraLinkUnitValid = (rsrcCostUnit == NA) || (rscrUsageRateUnit != NA);

                    if (firstLink) {
                        timeUnitSet = (rscrUsageRateUnit > NA ? SMH : NA);
                        firstLink = false;
                    } else {
                        int tmpTimeSet = (rscrUsageRateUnit > 0 ? SMH : NA);
                        if (timeUnitSet != tmpTimeSet) {
                            useCaseRsrcUnitDiff++;
                        }
                    } // end if

                    if (rscrUsageRateUnit > commonTimeUnit) {
                        commonTimeUnit = rscrUsageRateUnit;
                    }

                    // Resource Cost
                    if (!isIntraLinkUnitValid) {
                        processRsrcCost += getUndefinedValue();
                    } else {

                        if (rsrcCost > 0) {
                            tmpTimeUnit = (rsrcCostUnit > 0 ? rsrcCostUnit : NA);
                            tmpCost = getConvertValue(rsrcCost, tmpTimeUnit);

                            tmpTimeUnit = (rscrUsageRateUnit > 0 ? rscrUsageRateUnit : NA);
                            tmpUsageRate = getBaseTime(rsrcUsageRate, tmpTimeUnit);
                        } else {
                            tmpCost = 0.0;
                        }

                        if (isRsrcFixedCost)
                            processRsrcCost += tmpCost;
                        else
                            processRsrcCost += tmpCost * tmpUsageRate;
                    } // end if

                    // Resource Time
                    if (useCaseRsrcUnitDiff == 0) {
                        tmpTimeUnit = (rscrUsageRateUnit > 0 ? rscrUsageRateUnit : NA);
                        tmpUsageRate = getBaseTime(rsrcUsageRate, tmpTimeUnit);
                        resultTime += tmpUsageRate;
                    } else {
                        resultTime = getUndefinedValue(); // incompatible time
                        // units
                    } // end if
                } // end if
            } // end if
        } // end while
        dbEnum.close();

        if (Double.isNaN(resultTime)) {
            processRsrcTime = getUndefinedValue();
        } else if (resultTime > 0.0) {
            processRsrcTime = getConvertValue(resultTime, commonTimeUnit);
        }

        process.setResourceCost(new Double(processRsrcCost));
        process.setResourceTime(useCaseRsrcUnitDiff == 0 ? new Double(processRsrcTime)
                : new Double(getUndefinedValue()));
        if ((useCaseRsrcUnitDiff == 0) && (commonTimeUnit > 0))
            process.setResourceTimeUnit(BETimeUnit.getInstance(commonTimeUnit));
        else
            process.setResourceTimeUnit(null);
    }

    private double getConvertValue(double value, int timeUnit) {
        switch (timeUnit) {
        case BETimeUnit.MINUTE:
            return (value / 60.0); // 60.0 for floating-point division
        case BETimeUnit.HOUR:
            return (value / 3600.0); // 3600.0 for floating-point division
            // NA or BETimeUnit.SECOND
        default:
            return value;
        }
    }

    private double getBaseTime(double time, int timeUnit) {
        switch (timeUnit) {
        case BETimeUnit.MINUTE:
            return (time * 60.0); // 60.0 for floating-point division
        case BETimeUnit.HOUR:
            return (time * 3600.0); // 3600.0 for floating-point division
            // NA or BETimeUnit.SECOND
        default:
            return time;
        }
    }

    private double getUndefinedValue() {
        return Double.NaN;
    }

    // If we change (or create) the notation of a diagram and
    // its model has no terminoloy, set it to the notation's terminology
    private void initDiagram(DbUpdateEvent event) {
        DbBEDiagram diagram = (DbBEDiagram) event.dbo;
        try {
            diagram.getDb().beginReadTrans();
            DbBENotation notation = diagram.findMasterNotation();
            DbBEUseCase useCase = (DbBEUseCase) diagram.getComposite();
            useCase.setTerminologyName(notation.getTerminologyName());
            diagram.getDb().commitTrans();
        } catch (DbException ex) {
            DefaultMainFrame mf = ApplicationContext.getDefaultMainFrame();
            ExceptionHandler.processUncatchedException(mf, ex);
        } // end try
    } // end changeNotation
} // end BESemanticalIntegrity
