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

package org.modelsphere.sms.graphic.tool;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JInternalFrame;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.graphic.zone.MatrixZone;
import org.modelsphere.jack.graphic.zone.ZoneCell;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.FocusManager;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEFlowGo;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.db.DbSMSAssociation;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.DbSMSStructuralFeature;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOAssociation;
import org.modelsphere.sms.oo.db.DbOOAssociationEnd;
import org.modelsphere.sms.oo.db.DbOOClass;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.DbOOInheritance;
import org.modelsphere.sms.oo.java.graphic.AdtBox;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORChoiceOrSpecialization;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.graphic.ORTable;

public class SMSSelectNeighborhoodTool extends SMSSelectTool {
    private static String g_tooltext = LocaleMgr.screen.getString("SelectNeighborhoodTool");
    private static Image g_toolImage = GraphicUtil.loadImage(Tool.class,
            "resources/selectionNeighborhoodTool.gif"); // NOT LOCALIZABLE - tool image

    private ApplicationDiagram m_currentActiveDiagram = null;

    public SMSSelectNeighborhoodTool() {
        super(g_tooltext, g_toolImage);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);

        FocusManager fm = ApplicationContext.getFocusManager();
        m_currentActiveDiagram = fm.getActiveDiagram();

        unselectOpenDiagrams();
        Object[] objs = fm.getSelectedObjects();

        for (Object obj : objs) {
            if (obj instanceof ActionInformation) {
                ActionInformation info = (ActionInformation) obj;
                DbObject dbo = info.getSemanticalObject();
                selectObjectNeighbors(dbo);
            } else if (obj instanceof DbSMSSemanticalObject) {
                DbSMSSemanticalObject so = (DbSMSSemanticalObject) obj;
                selectObjectNeighbors(so);
            } //end if
        } //end for

        m_currentActiveDiagram = null;
    } //end mousePressed()

    private void unselectOpenDiagrams() {
        MainFrame mf = MainFrame.getSingleton();
        JInternalFrame[] frames = mf.getDiagramInternalFrames();
        for (JInternalFrame frame : frames) {
            if (frame instanceof DiagramInternalFrame) {
                DiagramInternalFrame diagramFrame = (DiagramInternalFrame) frame;
                ApplicationDiagram diagram = diagramFrame.getDiagram();
                if (diagram != m_currentActiveDiagram) {
                    diagram.deselectAll();
                }
            } //end if
        } //end for
    } //end unselectOpenDiagrams()

    private void selectObjectNeighbors(DbObject dbo) {
        if (dbo == null)
            return;

        try {
            dbo.getDb().beginReadTrans();
            Set<GraphicComponent> neighborhood = new HashSet<GraphicComponent>();

            //part 1 : find neighborhood

            //find neighbors of generic objects
            if (dbo instanceof DbSMSSemanticalObject) {
                DbSMSSemanticalObject so = (DbSMSSemanticalObject) dbo;
                addLinkedNeighors(neighborhood, so);
                addSuperCopy(neighborhood, so);
                addSubCopy(neighborhood, so);
            }

            //find neighbors for specific objects
            if (dbo instanceof DbORAbsTable) {
                DbORAbsTable table = (DbORAbsTable) dbo;
                addTableNeighbors(neighborhood, table);
            } else if (dbo instanceof DbORAssociation) {
                DbORAssociation assoc = (DbORAssociation) dbo;
                addAssociationNeighbors(neighborhood, assoc);
            } else if (dbo instanceof DbOOClass) {
                DbOOClass claz = (DbOOClass) dbo;
                addClassNeighbors(neighborhood, claz);
            } else if (dbo instanceof DbOOAssociation) {
                DbOOAssociation assoc = (DbOOAssociation) dbo;
                addAssociationNeighbors(neighborhood, assoc);
            } else if (dbo instanceof DbBEUseCase) {
                DbBEUseCase process = (DbBEUseCase) dbo;
                addProcessNeighbors(neighborhood, process);
            } else if (dbo instanceof DbBEActor) {
                DbBEActor actor = (DbBEActor) dbo;
                addActorNeighbors(neighborhood, actor);
            } else if (dbo instanceof DbBEStore) {
                DbBEStore store = (DbBEStore) dbo;
                addStoreNeighbors(neighborhood, store);
            } else if (dbo instanceof DbBEFlow) {
                DbBEFlow flow = (DbBEFlow) dbo;
                addFlowNeighbors(neighborhood, flow);
            } //end if

            //part 2 : select neighborhood

            //select neighbors
            for (GraphicComponent neighbor : neighborhood) {
                neighbor.setSelected(true);
            } //end for

            //refresh current diagram
            refreshDiagram(m_currentActiveDiagram);

            dbo.getDb().commitTrans();

        } catch (DbException ex) {
            //do nothing
        }
    } //end selectObjectNeighbors()

    private void refreshDiagram(ApplicationDiagram diagram) {
        if (diagram != null) {
            DiagramInternalFrame frame = diagram.getDiagramInternalFrame();
            try {
                frame.setSelected(true);
            } catch (PropertyVetoException ex) {
                //ignore exception
            }
        } //end if
    } //end refreshDiagram()

    private void addSuperCopy(Set<GraphicComponent> neighborhood, DbSMSSemanticalObject so)
            throws DbException {
        DbSMSSemanticalObject superCopy = so.getSuperCopy();
        addSemanticObjectGOs(neighborhood, superCopy);
    }

    private void addSubCopy(Set<GraphicComponent> neighborhood, DbSMSSemanticalObject so)
            throws DbException {
        DbRelationN relN = so.getSubCopies();
        DbEnumeration enu = relN.elements(DbSMSSemanticalObject.metaClass);
        while (enu.hasMoreElements()) {
            DbSMSSemanticalObject subCopy = (DbSMSSemanticalObject) enu.nextElement();
            addSemanticObjectGOs(neighborhood, subCopy);
        }
        enu.close();
    }

    private void addLinkedNeighors(Set<GraphicComponent> neighborhood, DbSMSSemanticalObject so)
            throws DbException {
        //for each outgoing link
        DbRelationN relN = so.getSourceLinks();
        DbEnumeration enu = relN.elements(DbSMSLink.metaClass);
        while (enu.hasMoreElements()) {

            //for each target object
            DbSMSLink link = (DbSMSLink) enu.nextElement();
            DbRelationN relN2 = link.getTargetObjects();
            DbEnumeration enu2 = relN2.elements(DbSMSSemanticalObject.metaClass);
            while (enu2.hasMoreElements()) {
                DbSMSSemanticalObject target = (DbSMSSemanticalObject) enu2.nextElement();
                addSemanticObjectGOs(neighborhood, target);
            } //end while
            enu2.close();
        } //end while
        enu.close();

        //for each incoming link
        relN = so.getTargetLinks();
        enu = relN.elements(DbSMSLink.metaClass);
        while (enu.hasMoreElements()) {

            //for each source object
            DbSMSLink link = (DbSMSLink) enu.nextElement();
            DbRelationN relN2 = link.getSourceObjects();
            DbEnumeration enu2 = relN2.elements(DbSMSSemanticalObject.metaClass);
            while (enu2.hasMoreElements()) {
                DbSMSSemanticalObject src = (DbSMSSemanticalObject) enu2.nextElement();
                addSemanticObjectGOs(neighborhood, src);
            } //end while
            enu2.close();
        } //end while
        enu.close();
    } //end addLinkedNeighors()

    private void addSemanticObjectGOs(Set<GraphicComponent> neighborhood, DbSMSSemanticalObject so)
            throws DbException {

        if (so instanceof DbSMSClassifier) {
            DbSMSClassifier classifier = (DbSMSClassifier) so;

            //for each graphical representation of the opposite table/class
            DbRelationN relN = classifier.getClassifierGos();
            DbEnumeration enu = relN.elements();
            while (enu.hasMoreElements()) {
                DbSMSGraphicalObject go = (DbSMSGraphicalObject) enu.nextElement();
                Object peer = go.getGraphicPeer();
                if (peer instanceof ORTable) {
                    ORTable srcTable = (ORTable) peer;
                    neighborhood.add(srcTable);
                } else if (peer instanceof AdtBox) {
                    AdtBox srcAdt = (AdtBox) peer;
                    neighborhood.add(srcAdt);
                }
            } //end while
            enu.close();

        } else if (so instanceof DbSMSStructuralFeature) {
            DbSMSStructuralFeature feature = (DbSMSStructuralFeature) so;
            DbSMSClassifier classifier = (DbSMSClassifier) feature
                    .getCompositeOfType(DbSMSClassifier.metaClass);

            //for each graphical representation of the opposite table/class
            DbRelationN relN = classifier.getClassifierGos();
            DbEnumeration enu = relN.elements();
            while (enu.hasMoreElements()) {
                DbSMSGraphicalObject go = (DbSMSGraphicalObject) enu.nextElement();
                Object peer = go.getGraphicPeer();
                if (peer instanceof ORTable) {
                    ORTable srcTable = (ORTable) peer;
                    MatrixZone zone = srcTable.getColumnZone();
                    selectCell(srcTable, zone, so);
                    neighborhood.add(srcTable);
                } else if (peer instanceof AdtBox) {
                    AdtBox srcAdt = (AdtBox) peer;
                    MatrixZone zone = srcAdt.getFieldZone();
                    selectCell(srcAdt, zone, so);
                    neighborhood.add(srcAdt);
                }
            } //end while
            enu.close();
        }

    }

    //select the cell holding 'so'
    private void selectCell(GraphicComponent gc, MatrixZone zone, DbSMSSemanticalObject so) {
        int rows = zone.getRowCount();
        int cols = zone.getColumnCount();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                ZoneCell cell = zone.get(row, col);
                Object o = cell.getObject();

                boolean isSame = so.equals(o);
                if (isSame) {
                    cell.setSelected(true);
                }
            }
        } //end for 
    }

    //
    // object neighborhood
    //

    private void addClassNeighbors(Set<GraphicComponent> neighborhood, DbOOClass claz)
            throws DbException {
        //for each field
        DbRelationN relN = claz.getComponents();
        DbEnumeration enu = relN.elements(DbOODataMember.metaClass);
        while (enu.hasMoreElements()) {
            DbOODataMember field = (DbOODataMember) enu.nextElement();
            DbOOAssociationEnd end = field.getAssociationEnd();
            if (end != null) {
                DbOOAssociation assoc = (DbOOAssociation) end
                        .getCompositeOfType(DbOOAssociation.metaClass);
                addAssociationGos(neighborhood, assoc);
                //add opposite class
                DbOOAssociationEnd opposite = end.getOppositeEnd();
                DbOODataMember oppField = opposite.getAssociationMember();
                DbSMSClassifier classifier = (DbOOClass) oppField
                        .getCompositeOfType(DbOOClass.metaClass);
                addClassifierGOs(neighborhood, classifier);
            }
        } //end while
        enu.close();

        //for each super class
        relN = claz.getSuperInheritances();
        enu = relN.elements(DbOOInheritance.metaClass);
        while (enu.hasMoreElements()) {
            DbOOInheritance inher = (DbOOInheritance) enu.nextElement();
            DbSMSClassifier classifier = (DbOOClass) inher.getSuperClass();
            addClassifierGOs(neighborhood, classifier);
        }
        enu.close();

        //for each sub class
        relN = claz.getSubInheritances();
        enu = relN.elements(DbOOInheritance.metaClass);
        while (enu.hasMoreElements()) {
            DbOOInheritance inher = (DbOOInheritance) enu.nextElement();
            DbSMSClassifier classifier = (DbOOClass) inher.getSubClass();
            addClassifierGOs(neighborhood, classifier);
        }
        enu.close();
    }

    private void addAssociationNeighbors(Set<GraphicComponent> neighborhood, DbOOAssociation assoc)
            throws DbException {
        DbOOAssociationEnd backEnd = assoc.getBackEnd();
        DbOOAssociationEnd frontEnd = assoc.getFrontEnd();
        DbSMSClassifier backClass = (DbSMSClassifier) backEnd.getAssociationMember()
                .getCompositeOfType(DbSMSClassifier.metaClass);
        DbSMSClassifier frontClass = (DbSMSClassifier) frontEnd.getAssociationMember()
                .getCompositeOfType(DbSMSClassifier.metaClass);
        addClassifierGOs(neighborhood, backClass);
        addClassifierGOs(neighborhood, frontClass);
    }

    //
    // relational neighborhood
    //

    private void addTableNeighbors(Set<GraphicComponent> neighborhood, DbORAbsTable table)
            throws DbException {
        DbRelationN relN = table.getAssociationEnds();
        DbEnumeration enu = relN.elements(DbORAssociationEnd.metaClass);

        //for each association end
        while (enu.hasMoreElements()) {
            DbORAssociationEnd end = (DbORAssociationEnd) enu.nextElement();
            DbORAssociation assoc = (DbORAssociation) end
                    .getCompositeOfType(DbORAssociation.metaClass);
            addAssociationGos(neighborhood, assoc);

            //add opposite table
            DbORAssociationEnd opposite = end.getOppositeEnd();
            DbORAbsTable oppTable = opposite.getClassifier();
            addClassifierGOs(neighborhood, oppTable);

            //add parent table of a choice/spec
            if (oppTable instanceof DbORChoiceOrSpecialization) {
                DbORChoiceOrSpecialization choiceSpec = (DbORChoiceOrSpecialization) oppTable;
                DbORTable parentTable = choiceSpec.getParentTable();

                if (table.equals(parentTable)) {
                    addChoiceSpecChildrenGos(neighborhood, choiceSpec);
                } else {
                    addClassifierGOs(neighborhood, parentTable);
                } //end if
            } //end if
        } //end while
        enu.close();
        
        if (table instanceof DbORChoiceOrSpecialization) {
        	DbORChoiceOrSpecialization choiceSpec = (DbORChoiceOrSpecialization)table;
        	addChoiceSpecNeighbors(neighborhood, choiceSpec);
        }

    } //end addTableNeighbors()

    private void addChoiceSpecNeighbors(Set<GraphicComponent> neighborhood, DbORChoiceOrSpecialization choiceSpec) throws DbException {
    	DbEnumeration enu = choiceSpec.getAssociations().elements(DbORAssociation.metaClass);
    	while (enu.hasMoreElements()) { 
    		DbORAssociation assoc = (DbORAssociation) enu.nextElement();
    		addClassifierGOs(neighborhood, assoc.getBackEnd().getClassifier());
    		addClassifierGOs(neighborhood, assoc.getFrontEnd().getClassifier());
    	} //end while
        enu.close();
	} //end addChoiceSpecNeighbors()

	private void addChoiceSpecChildrenGos(Set<GraphicComponent> neighborhood,
            DbORChoiceOrSpecialization choiceSpec) throws DbException {
        DbORTable parentTable = choiceSpec.getParentTable();

        DbEnumeration enu = choiceSpec.getAssociationEnds().elements(DbORAssociationEnd.metaClass);
        while (enu.hasMoreElements()) {
            DbORAssociationEnd end = (DbORAssociationEnd) enu.nextElement();
            DbORAssociationEnd oppEnd = end.getOppositeEnd();
            DbORAbsTable oppTable = oppEnd.getClassifier();

            if (!oppTable.equals(parentTable)) {
                addClassifierGOs(neighborhood, oppTable);
            }
        } //end while
        enu.close();
    }

    private void addAssociationGos(Set<GraphicComponent> neighborhood, DbSMSAssociation assoc)
            throws DbException {

        //for each graphical representation of classifier
        DbRelationN relN = assoc.getAssociationGos();
        DbEnumeration enu = relN.elements();
        while (enu.hasMoreElements()) {
            DbSMSGraphicalObject go = (DbSMSGraphicalObject) enu.nextElement();
            Object peer = go.getGraphicPeer();
            if (peer instanceof GraphicComponent) {
                GraphicComponent comp = (GraphicComponent) peer;
                neighborhood.add(comp);
            }
        }
        enu.close();
    }

    private void addClassifierGOs(Set<GraphicComponent> neighborhood, DbSMSClassifier classifier)
            throws DbException {
        if (classifier == null)
            return;

        //for each graphical representation of classifier
        DbRelationN relN = classifier.getClassifierGos();
        DbEnumeration enu = relN.elements();
        while (enu.hasMoreElements()) {
            DbSMSGraphicalObject go = (DbSMSGraphicalObject) enu.nextElement();
            Object peer = go.getGraphicPeer();
            if (peer instanceof GraphicComponent) {
                GraphicComponent comp = (GraphicComponent) peer;
                neighborhood.add(comp);
            }
        } //end while
        enu.close();
    }

    private void addAssociationNeighbors(Set<GraphicComponent> neighborhood, DbORAssociation assoc)
            throws DbException {
        DbORAssociationEnd backEnd = (DbORAssociationEnd) assoc.getBackEnd();
        DbORAssociationEnd frontEnd = (DbORAssociationEnd) assoc.getFrontEnd();

        addClassifierGOs(neighborhood, backEnd.getClassifier());
        addClassifierGOs(neighborhood, frontEnd.getClassifier());
    }

    //
    // process neighborhood
    //

    private void addProcessNeighbors(Set<GraphicComponent> neighborhood, DbBEUseCase process)
            throws DbException {
        DbRelationN relN = process.getFirstEndFlows();
        DbEnumeration enu = relN.elements(DbBEFlow.metaClass);

        //for each association end
        while (enu.hasMoreElements()) {
            DbBEFlow flow = (DbBEFlow) enu.nextElement();
            addFlowGOs(neighborhood, flow);

            DbSMSClassifier classifier = flow.getSecondEnd();
            addClassifierGOs(neighborhood, classifier);
        } //end while
        enu.close();

        relN = process.getSecondEndFlows();
        enu = relN.elements(DbBEFlow.metaClass);

        //for each association end
        while (enu.hasMoreElements()) {
            DbBEFlow flow = (DbBEFlow) enu.nextElement();
            addFlowGOs(neighborhood, flow);

            DbSMSClassifier classifier = flow.getFirstEnd();
            addClassifierGOs(neighborhood, classifier);
        } //end while
        enu.close();
    } //end addProcessNeighbors()

    private void addActorNeighbors(Set<GraphicComponent> neighborhood, DbBEActor actor)
            throws DbException {
        DbRelationN relN = actor.getFirstEndFlows();
        DbEnumeration enu = relN.elements(DbBEFlow.metaClass);

        //for each association end
        while (enu.hasMoreElements()) {
            DbBEFlow flow = (DbBEFlow) enu.nextElement();
            addFlowGOs(neighborhood, flow);

            DbSMSClassifier classifier = flow.getSecondEnd();
            addClassifierGOs(neighborhood, classifier);
        } //end while
        enu.close();

        relN = actor.getSecondEndFlows();
        enu = relN.elements(DbBEFlow.metaClass);

        //for each association end
        while (enu.hasMoreElements()) {
            DbBEFlow flow = (DbBEFlow) enu.nextElement();
            addFlowGOs(neighborhood, flow);

            DbSMSClassifier classifier = flow.getFirstEnd();
            addClassifierGOs(neighborhood, classifier);
        } //end while
        enu.close();
    } //end addActorNeighbors()

    private void addStoreNeighbors(Set<GraphicComponent> neighborhood, DbBEStore store)
            throws DbException {
        DbRelationN relN = store.getFirstEndFlows();
        DbEnumeration enu = relN.elements(DbBEFlow.metaClass);

        //for each association end
        while (enu.hasMoreElements()) {
            DbBEFlow flow = (DbBEFlow) enu.nextElement();
            addFlowGOs(neighborhood, flow);

            DbSMSClassifier classifier = flow.getSecondEnd();
            addClassifierGOs(neighborhood, classifier);
        } //end while
        enu.close();

        relN = store.getSecondEndFlows();
        enu = relN.elements(DbBEFlow.metaClass);

        //for each association end
        while (enu.hasMoreElements()) {
            DbBEFlow flow = (DbBEFlow) enu.nextElement();
            addFlowGOs(neighborhood, flow);

            DbSMSClassifier classifier = flow.getFirstEnd();
            addClassifierGOs(neighborhood, classifier);
        } //end while
        enu.close();
    } //end addStoreNeighbors()

    private void addFlowGOs(Set<GraphicComponent> neighborhood, DbBEFlow flow) throws DbException {
        //for each graphical representation of classifier
        DbRelationN relN = flow.getFlowGos();
        DbEnumeration enu = relN.elements();
        while (enu.hasMoreElements()) {
            DbBEFlowGo go = (DbBEFlowGo) enu.nextElement();
            Object peer = go.getGraphicPeer();
            if (peer instanceof GraphicComponent) {
                GraphicComponent comp = (GraphicComponent) peer;
                neighborhood.add(comp);
            }
        } //end while
        enu.close();
    }

    private void addFlowNeighbors(Set<GraphicComponent> neighborhood, DbBEFlow flow)
            throws DbException {
        addClassifierGOs(neighborhood, flow.getFirstEnd());
        addClassifierGOs(neighborhood, flow.getSecondEnd());
    }

} //end SMSSelectTool
