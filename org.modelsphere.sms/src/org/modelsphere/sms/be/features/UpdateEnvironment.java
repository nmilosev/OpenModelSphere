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

package org.modelsphere.sms.be.features;

import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DeepCopyCustomizer;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.sms.be.db.DbBEActorGo;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEFlowGo;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseGo;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.util.AnyGo;
import org.modelsphere.sms.db.util.AnySemObject;
import org.modelsphere.sms.db.util.DbInitialization;

public final class UpdateEnvironment {
    private static final int MAX_DIAGRAM_INTERNAL_FRAME = 5;

    private DbBEUseCase useCase;
    private boolean addGo = true;
    private ArrayList useCaseDiags = new ArrayList();
    private HashMap flowSemObjectMap = new HashMap();
    private DeepCopyCustomizer copyCustomizer = new CopyCustomizer();
    private ArrayList addedClassifierGos = new ArrayList();
    private boolean m_updatedEnvironment = false;

    private int m_leftCounter, m_rightCounter;
    private int m_leftMargin = 0;
    private int m_rightMargin = Integer.MAX_VALUE;

    public UpdateEnvironment() {
    }

    public boolean execute(DbBEUseCase useCase) throws DbException {
        m_updatedEnvironment = false;
        execute(useCase, null, null);
        return m_updatedEnvironment;

    }

    // A write transaction must be opened on the usecase's Db.
    public void execute(DbBEUseCase useCase, DbBEDiagram parentDiagram, DbBEContextGo frame)
            throws DbException {
        if (useCase == null)
            return;
        this.useCase = useCase;
        initDiagrams();
        doUpdate(useCase, parentDiagram, frame);

        // display up to MAX_DIAGRAM_INTERNAL_FRAME updated diag
        int diagcount = 0;
        for (int i = 0; i < useCaseDiags.size() && diagcount < MAX_DIAGRAM_INTERNAL_FRAME; i++) {
            if (!(useCaseDiags.get(i) instanceof DbBEDiagram))
                continue;
            ApplicationContext.getDefaultMainFrame().addDiagramInternalFrame(
                    (DbBEDiagram) useCaseDiags.get(i));
            diagcount++;
        }
    } // end execute()

    private void initDiagrams() throws DbException {

        DbEnumeration diagEnum = useCase.getComponents().elements(DbBEDiagram.metaClass);
        while (diagEnum.hasMoreElements()) {
            useCaseDiags.add((DbBEDiagram) diagEnum.nextElement());
        }
        diagEnum.close();
    }

    private void doUpdate(DbBEUseCase useCase, DbBEDiagram parentDiagram, DbBEContextGo frame)
            throws DbException {
        m_leftCounter = 0;
        m_rightCounter = 0;

        DbEnumeration flows = useCase.getFirstEndFlows().elements();
        while (flows.hasMoreElements()) {
            DbBEFlow flow = (DbBEFlow) flows.nextElement();
            if (useCase.getComposite() != flow.getComposite())
                continue;
            DbSMSClassifier classifier = flow.getSecondEnd();
            flowSemObjectMap.put(flow, classifier);
        }
        flows.close();

        flows = useCase.getSecondEndFlows().elements();
        while (flows.hasMoreElements()) {
            DbBEFlow flow = (DbBEFlow) flows.nextElement();
            if (useCase.getComposite() != flow.getComposite())
                continue;
            DbSMSClassifier classifier = flow.getFirstEnd();
            flowSemObjectMap.put(flow, classifier);
        }
        flows.close();

        if (flowSemObjectMap.isEmpty())
            return;
        Iterator iter = flowSemObjectMap.keySet().iterator();
        while (iter.hasNext()) {
            ArrayList matchedFlowsList = new ArrayList();
            DbSMSClassifier end1 = null;
            DbSMSClassifier end2 = null;
            DbBEFlow flow = (DbBEFlow) iter.next();
            end1 = flow.getFirstEnd();
            end2 = flow.getSecondEnd();
            DbSMSClassifier classifier = (DbSMSClassifier) flowSemObjectMap.get(flow);
            boolean recursive = DbObject.valuesAreEqual(useCase, classifier);
            boolean addEnd1 = true;
            boolean addEnd2 = true;
            DbEnumeration subFlows = flow.getSubCopies().elements(DbBEFlow.metaClass);
            while (subFlows.hasMoreElements()) {
                DbBEFlow subFlow = (DbBEFlow) subFlows.nextElement();
                DbSMSClassifier subEnd1 = null;
                DbSMSClassifier subEnd2 = null;
                subEnd1 = subFlow.getFirstEnd();
                subEnd2 = subFlow.getSecondEnd();
                if (recursive) {
                    if (DbObject.valuesAreEqual(classifier, subEnd1) && addEnd1) {
                        matchedFlowsList.add(subFlow);
                        addEnd1 = false;
                    } else if (DbObject.valuesAreEqual(classifier, subEnd2) && addEnd2) {
                        matchedFlowsList.add(subFlow);
                        addEnd2 = false;
                    }
                } else {
                    if (end1 != null && DbObject.valuesAreEqual(classifier, end1)
                            && DbObject.valuesAreEqual(classifier, subEnd1))
                        matchedFlowsList.add(subFlow);
                    if (end2 != null && DbObject.valuesAreEqual(classifier, end2)
                            && DbObject.valuesAreEqual(classifier, subEnd2))
                        matchedFlowsList.add(subFlow);
                }
            }
            subFlows.close();

            // Create flows if required
            int nbAdd = 1;
            if (recursive)
                nbAdd = 2;
            if (matchedFlowsList.size() < nbAdd) { // need to create
                int count = nbAdd - matchedFlowsList.size();
                for (int i = 0; i < count; i++) {
                    DbObject[] newFlows = new DbObject[] {};
                    newFlows = DbObject.deepCopy(new DbObject[] { flow }, this.useCase,
                            this.copyCustomizer, false);
                    DbBEFlow newFlow = (DbBEFlow) newFlows[0];
                    if (recursive) {
                        if (addEnd1 && newFlow.getFirstEnd() != null) {
                            newFlow.setSecondEnd(null);
                            addEnd1 = false;
                        } else if (addEnd2 && newFlow.getSecondEnd() != null) {
                            newFlow.setFirstEnd(null);
                            addEnd2 = false;
                        }
                    }
                    matchedFlowsList.add((DbBEFlow) newFlows[0]);
                }
            }
            updateFlowDiagrams(flow, matchedFlowsList, classifier, parentDiagram, frame);
        } // end while flows

        resetFrameMargins();
    } // end doUpdate()

    private void updateFlowDiagrams(DbBEFlow flow, List matchedFlows, DbSMSClassifier classifier,
            DbBEDiagram parentDiagram, DbBEContextGo frame) throws DbException {
        for (int i = 0; i < useCaseDiags.size(); i++) {
            DbBEDiagram diagram = (DbBEDiagram) useCaseDiags.get(i);
            for (int j = 0; j < matchedFlows.size(); j++) {
                DbBEFlow matchedFlow = (DbBEFlow) matchedFlows.get(j);
                DbRelationN flowGos = ApplicationContext.getSemanticalModel().getGos(matchedFlow);
                boolean addGos = true;
                if (flowGos != null) {
                    DbEnumeration flowGoEnum = flowGos.elements(DbBEFlowGo.metaClass);
                    while (flowGoEnum.hasMoreElements()) {
                        DbBEFlowGo flowGo = (DbBEFlowGo) flowGoEnum.nextElement();
                        // In same diagram
                        if (!DbObject.valuesAreEqual(diagram, flowGo
                                .getCompositeOfType(DbBEDiagram.metaClass)))
                            continue;
                        if (classifier == null) {
                            addGos = false;
                            break;
                        }
                        // Look for matching classifier
                        if (findMatchingFlowGoClassifier(flowGo, classifier)) {
                            addGos = false;
                            break;
                        }
                    } // end while flowGo enum
                    flowGoEnum.close();
                } // end if flowGos != null
                if (addGos) // Add the flow and the classifier
                    createGos(diagram, matchedFlow, classifier, parentDiagram, frame);

            } // End matchedflow
        } // End each diagram
    } // end updateFlowDiagrams()

    private void resetFrameMargins() throws DbException {
        m_leftMargin += 20;
        m_rightMargin -= 20;

        for (int i = 0; i < useCaseDiags.size(); i++) {
            DbBEDiagram diagram = (DbBEDiagram) useCaseDiags.get(i);
            DbRelationN relN = diagram.getComponents();
            DbEnumeration dbEnum = relN.elements(DbBEContextGo.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbBEContextGo frame = (DbBEContextGo) dbEnum.nextElement();
                Rectangle rect = frame.getRectangle();
                if (rect.x < m_leftMargin) {
                    rect.x = m_leftMargin;
                }

                if ((rect.x + rect.width) > m_rightMargin) {
                    rect.width = m_rightMargin - rect.x;
                }

                frame.setRectangle(rect);
            } // end while
            dbEnum.close();
        } // end for

    } // end resetFrameMargins()

    private boolean findMatchingFlowGoClassifier(DbBEFlowGo flowGo, DbSMSClassifier classifier)
            throws DbException {
        boolean match = false;
        if ((flowGo == null) || (classifier == null))
            return match;
        DbSMSGraphicalObject frontEndGo = flowGo.getFrontEndGo();
        DbSMSGraphicalObject backEndGo = flowGo.getBackEndGo();
        DbSMSClassifier goClassifier = null;
        // Front end
        if (frontEndGo != null) {
            if (frontEndGo instanceof DbSMSClassifierGo) {
                goClassifier = ((DbSMSClassifierGo) frontEndGo).getClassifier();
                if (DbObject.valuesAreEqual(classifier, goClassifier))
                    return true;
            }
        }
        // Back end
        if (backEndGo != null) {
            if (backEndGo instanceof DbSMSClassifierGo) {
                goClassifier = ((DbSMSClassifierGo) backEndGo).getClassifier();
                if (DbObject.valuesAreEqual(classifier, goClassifier))
                    return true;
            }
        }
        return match;
    }

    private void createGos(DbBEDiagram diagram, DbBEFlow flow, DbSMSClassifier classifier)
            throws DbException {
        createGos(diagram, flow, classifier, null, null);
    }

    private void createGos(DbBEDiagram diagram, DbBEFlow flow, DbSMSClassifier classifier,
            DbBEDiagram parentDiagram, DbBEContextGo frame) throws DbException {

        m_updatedEnvironment = true;

        DbSMSClassifierGo classGo = null;
        boolean inComing = flow.getFirstEnd() != null;
        int masterNotationID = diagram.getNotation().getMasterNotationID().intValue();
        DbBENotation parentNotation = (parentDiagram == null) ? null : parentDiagram.getNotation();
        int parentMasterNotationID = (parentNotation == null) ? 0 : parentNotation
                .getMasterNotationID().intValue();
        // String parentName = (parentNotation == null) ? "" :
        // parentNotation.getName();

        // Watch out for the context!
        if (classifier != null) {
            if (classifier instanceof DbBEUseCase)
                classGo = (DbSMSClassifierGo) DbGraphic.getFirstGraphicalObject(diagram,
                        classifier, null, DbBEUseCaseGo.metaClass);
            else
                classGo = (DbSMSClassifierGo) DbGraphic
                        .getFirstGraphicalObject(diagram, classifier);

            // sequence diagram : do not recover anything from the parent
            // diagram
            if (masterNotationID == DbInitialization.UML_SEQUENCE_DIAGRAM) {
                classGo = null;
                // collaboration diagram : recover only actors from the parent
                // diagram
            } else if (masterNotationID == DbInitialization.UML_COLLABORATION_DIAGRAM
                    && parentMasterNotationID == DbInitialization.UML_COLLABORATION_DIAGRAM) {
                if (!(classGo instanceof DbBEActorGo)) {
                    classGo = null;
                }
                // state diagram : do not recover anything from the parent
                // diagram if diagram type is different
            } else if (masterNotationID == DbInitialization.UML_STATE_DIAGRAM
                    && parentMasterNotationID == DbInitialization.UML_STATE_DIAGRAM) {
                classGo = null;
                // activity diagram : do not recover anything from the parent
                // diagram if diagram type is different
            } else if (masterNotationID == DbInitialization.UML_ACTIVITY_DIAGRAM
                    && parentMasterNotationID == DbInitialization.UML_ACTIVITY_DIAGRAM) {
                classGo = null;
            } else { // other than UML_SEQUENCE_DIAGRAM
                if (classGo == null) {
                    classGo = AnyGo.createClassifierGo(diagram, classifier, null);
                    setPosition(diagram, classGo, inComing);
                } // end if
            } // end if
        } // end if

        if (classGo != null) {
            DbBEFlowGo flowGo = null;
            if (inComing) {
                flowGo = new DbBEFlowGo(diagram, classGo, null, flow);
                if (masterNotationID == DbInitialization.UML_STATE_DIAGRAM) {
                    initFlowGo(flowGo, frame, true);
                }
            } else {
                flowGo = new DbBEFlowGo(diagram, null, classGo, flow);
                if (masterNotationID == DbInitialization.UML_STATE_DIAGRAM) {
                    initFlowGo(flowGo, frame, false);
                }
            } // end if
        } // end if
    } // end createGos()

    private void initFlowGo(DbBEFlowGo flowGo, DbBEContextGo frame, boolean inComing)
            throws DbException {
        DbBEFlow flow = flowGo.getFlow();
        flow.setName(null);
        // flow.

        if (frame == null)
            return;

        Rectangle frameRect = frame.getRectangle();
        Polygon poly = flowGo.getPolyline();
        int nb = poly.npoints;

        if (inComing) {
            poly.xpoints[nb - 1] = frameRect.x + 20;
            flowGo.setPolyline(poly);
        } else { // outgoing
            poly.xpoints[0] = frameRect.x + frameRect.width - 20;
            flowGo.setPolyline(poly);
        } // end if

    } // end initFlowGo()

    private void setPosition(DbBEDiagram diagram, DbSMSClassifierGo classGo, boolean inComing)
            throws DbException {
        // get diagram dimension
        Dimension dim = ApplicationDiagram.getPageSize(diagram.getPageFormat(), diagram
                .getPrintScale().intValue());
        Rectangle rect = classGo.getRectangle();
        int offset = 120; // distance between frame and top
        int gap = 100; // process height

        if (inComing) {
            // put at the left of the frame
            int x1 = (int) (dim.width * 0.05);
            int y1 = offset + (m_leftCounter++) * gap;
            rect.x = x1;
            rect.y = y1;

            if (m_leftMargin < (rect.x + rect.width))
                m_leftMargin = rect.x + rect.width;

        } else { // outGoing
            // put at the right of the frame
            int x2 = (int) (dim.width * 0.95);
            int y1 = offset + (m_rightCounter++) * gap;
            rect.x = x2 - rect.width;
            rect.y = y1;

            if (m_rightMargin > rect.x)
                m_rightMargin = rect.x;
        } // end if

        classGo.setRectangle(rect);
    } // end setPosition()

    private class CopyCustomizer implements DeepCopyCustomizer {
        CopyCustomizer() {
        }

        public MetaClass getDestMetaClass(DbObject srcObj, DbObject destComposite)
                throws DbException {
            return srcObj.getMetaClass();
        }

        public void initFields(DbObject srcObj, DbObject destObj, boolean namePrefixedWithCopyOf)
                throws DbException {
        }

        public DbObject resolveLink(DbObject srcObj, MetaRelationship metaRel, DbObject srcNeighbor)
                throws DbException {
            /* Keep only the destination object */
            if ((metaRel == DbBEFlow.fFirstEnd) || (metaRel == DbBEFlow.fSecondEnd)) {
                DbSMSClassifier classifier = (DbSMSClassifier) flowSemObjectMap.get(srcObj);
                if (DbObject.valuesAreEqual(classifier, srcNeighbor))
                    return srcNeighbor;
            }

            if ((metaRel.getFlags() & MetaField.COPY_REFS) == 0)
                return null;

            return srcNeighbor.findMatchingObject();
        }

        public void endCopy(ArrayList srcRoots) throws DbException {
            if (srcRoots.size() != 1)
                return;

            DbObject dbo = (DbObject) srcRoots.get(0);
            if (!AnySemObject.supportsSuper(dbo.getMetaClass()))
                return;
            if (!(dbo instanceof DbBEFlow))
                return;

            DbBEFlow srcFlow = (DbBEFlow) dbo;
            DbBEFlow destFlow = (DbBEFlow) srcFlow.getMatchingObject();
            if (destFlow == null)
                return;

            destFlow.setSuperCopy(srcFlow);
        }
    }
}
