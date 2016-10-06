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

package org.modelsphere.sms.actions;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbGraphicalObjectI;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.meta.MetaRelation1;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.graphic.Attachment;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.LineLabel;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.actions.ShowDiagramAction;
import org.modelsphere.jack.srtool.explorer.ExplorerView;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.jack.srtool.graphic.SrLine;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEActorGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEStoreGo;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseGo;
import org.modelsphere.sms.be.graphic.BEContext;
import org.modelsphere.sms.db.DbSMSAssociation;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSCommonItemGo;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSFreeLineGo;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSLineGo;
import org.modelsphere.sms.db.DbSMSNotice;
import org.modelsphere.sms.db.DbSMSNoticeGo;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSPackageGo;
import org.modelsphere.sms.db.util.AnyGo;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.DbOOAdtGo;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORChoiceOrSpecialization;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORTableGo;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.graphic.ORChoiceSpecialization;
import org.modelsphere.sms.or.oracle.db.DbORANestedTableStorage;

@SuppressWarnings("serial")
public class SendToDiagramAction extends AbstractDomainAction implements SelectionActionListener {

    private static final String kSendToDiagram = LocaleMgr.action.getString("sendToDiagram");
    private static final String kSendToDiagramExp = LocaleMgr.action.getString("sendToDiagramExp");
    private static final String kCreateGraphRep = LocaleMgr.action.getString("createGraphRep");
    private static final String kCurrentDiagram = LocaleMgr.action.getString("currentDiagram");
    private ApplicationDiagram m_matchingAppDiag = null;

    SendToDiagramAction() {
        super(kSendToDiagram, false);
        this.setMnemonic(LocaleMgr.action.getMnemonic("sendToDiagram"));
        this.setAccelerator(KeyStroke.getKeyStroke(
                LocaleMgr.action.getAccelerator("sendToDiagram"), ActionEvent.CTRL_MASK));
        
        setEnabled(false);
    }

    private class SourceObjectComparator implements Comparator<Object> {
        SourceObjectComparator() {
        }

        @Override
        public int compare(Object o1, Object o2) {
            if (o1 instanceof DbSMSClassifier && o2 instanceof DbSMSAssociation)
                return -1;
            if (o1 instanceof DbSMSAssociation && o2 instanceof DbSMSClassifier)
                return 1;
            else if (o1 instanceof DbSMSAssociation && o2 instanceof DbSMSAssociation)
                return 0;
            else if (o1 instanceof DbSMSAssociation)
                return 1;
            else if (o2 instanceof DbSMSAssociation)
                return -1;
            return 0;
        }

        public boolean equals(Object obj) {
            return obj == this;
        }
    }

    // Must be called within a write transaction.  This method is also used by paste (see SMSSemanticalModel).
    // ApplicationDiagram's corresponding InternalFrame must be visible.
    public final ArrayList<DbSMSGraphicalObject> sendToDiagram(ApplicationDiagram diagModel,
            Object[] objects) throws DbException {
        DbSMSDiagram diagGO = (DbSMSDiagram) diagModel.getDiagramGO();
        Rectangle viewRect = diagModel.getMainView().getViewRect();
        ArrayList<DbSMSGraphicalObject> graphicComponents = new ArrayList<DbSMSGraphicalObject>();
        if (isApplicationDiagramHaveFocus()
                && ApplicationContext.getFocusManager().getSelectedObjects().length > 0) {
            // Selection from a diagram: only graphical objects are selected
            // Copy to the target diagram the DbGraphicalObjects corresponding to the selection
            DbSMSGraphicalObject[] srcGos = getSourceGos(objects, diagGO);
            if (srcGos.length != 0) {
                DbObject[] dstGos = DbObject.deepCopy(srcGos, diagGO, null, false);

                //        PropertiesSet options = PropertiesManager.APPLICATION_PROPERTIES_SET;
                //        boolean stapTo = false;
                //        if (options != null)
                //            stapTo = options.getPropertyBoolean(Grid.class, Grid.PROPERTY_GRID_ACTIVE, Grid.PROPERTY_GRID_ACTIVE_DEFAULT);

                // Position the whole group of new graphical reps at the center of the visible area
                int i;
                Rectangle srcRect = new Rectangle(((GraphicComponent) srcGos[0].getGraphicPeer())
                        .getRectangle());
                for (i = 1; i < srcGos.length; i++) {
                    srcRect.add(((GraphicComponent) srcGos[i].getGraphicPeer()).getRectangle());
                }
                Rectangle dstRect = GraphicUtil.rectangleResize(viewRect, srcRect.width,
                        srcRect.height);
                GraphicUtil.confineToRect(dstRect, diagModel.getDrawingArea());

                // TODO We need to handle snapping on resize before enabling the snap.
                // Many components's size change on duplicate (source and target) if the duplicate is in a diagram
                // where the source is already present.
                //        Grid grid = diagModel.getMainView().getGrid();
                //        if (stapTo && grid != null){
                //            Rectangle snapToRect = grid.snapTo(diagModel.getMainView(), dstRect);
                //            dstRect.translate(snapToRect.x - dstRect.x, snapToRect.y - dstRect.y);
                //        }

                translateDestGos(dstGos, dstRect.x - srcRect.x, dstRect.y - srcRect.y);

                /*
                 * Look for unselected ClassifierGos that was copied as part of the copy of an
                 * association: for such a ClassifierGo, if there was already a duplicate in the
                 * target diagram, transfer all the association ends of the ClassifierGo to its
                 * duplicate, then delete the ClassifierGo. Straighten the polyline of the affected
                 * associations.
                 */
                for (i = 0; i < srcGos.length; i++) {
                    graphicComponents.add(srcGos[i]);
                    GraphicComponent gc = (GraphicComponent) srcGos[i].getGraphicPeer();
                    if (gc.isSelected())
                        continue;
                    if (!(dstGos[i] instanceof DbSMSClassifierGo))
                        continue;
                    DbSMSClassifierGo dstGo = (DbSMSClassifierGo) dstGos[i];
                    DbObject otherGo = DbGraphic.getFirstGraphicalObject(diagGO, dstGo
                            .getClassifier(), dstGo);
                    if (otherGo != null) {
                        transferLines(dstGo, otherGo, false);
                        transferLines(dstGo, otherGo, true);
                        dstGo.remove();
                    }
                }
            }
        }

        else {
            // Selection from the explorer or external call to sendToDiagram (paste in graphics)
            // Create in the target diagram new graphical reps for the selected semantical objects
            // Position each new graphical rep at the center of the visible area

            // sort Classifier first, others, and associations last (otherwise it may create more than one duplicate of the
            // same classifier during association duplication)
            Arrays.sort(objects, new SourceObjectComparator());

            Rectangle pos = GraphicUtil.rectangleResize(viewRect, 0, 0);
            for (int i = 0; i < objects.length; i++) {
                DbSMSGraphicalObject go = null;
                if (objects[i] instanceof DbORAbsTable || objects[i] instanceof DbORTypeClassifier)
                    go = new DbORTableGo(diagGO, (DbSMSClassifier) objects[i]);
                else if (objects[i] instanceof DbORCommonItem)
                    go = new DbSMSCommonItemGo(diagGO, (DbORCommonItem) objects[i]);
                else if (objects[i] instanceof DbOOAdt)
                    go = new DbOOAdtGo(diagGO, (DbOOAdt) objects[i]);
                else if (objects[i] instanceof DbSMSPackage)
                    go = new DbSMSPackageGo(diagGO, (DbSMSPackage) objects[i]);
                else if (objects[i] instanceof DbSMSNotice) {
                    DbSMSNotice notice = (DbSMSNotice) objects[i];
                    DbSMSNoticeGo noticeGo = new DbSMSNoticeGo(diagGO);
                    noticeGo.setNotice(notice);
                    go = noticeGo;
                } else if (objects[i] instanceof DbORAssociation) {
                    DbORAssociation assoc = (DbORAssociation) objects[i];
                    if (DbGraphic.getFirstGraphicalObject(diagGO, assoc) == null)
                        AnyGo.createORAssociationGo(diagGO, assoc, new Point(pos.x, pos.y));
                }
                //BE GO
                else if (objects[i] instanceof DbBEUseCase)
                    go = new DbBEUseCaseGo(diagGO, (DbSMSClassifier) objects[i]);
                else if (objects[i] instanceof DbBEStore)
                    go = new DbBEStoreGo(diagGO, (DbSMSClassifier) objects[i]);
                else if (objects[i] instanceof DbBEActor)
                    go = new DbBEActorGo(diagGO, (DbSMSClassifier) objects[i]);
                else if (objects[i] instanceof DbBEFlow) {
                    DbBEFlow flow = (DbBEFlow) objects[i];
                    AnyGo.createBEFlowGo(diagGO, flow, new Point(pos.x, pos.y));
                }

                if (go != null) {
                    if (go.isAutoFit()) {
                        Rectangle goRect = go.getRectangle();
                        go.setRectangle(new Rectangle(pos.x, pos.y, goRect.width, goRect.height));
                    } else {
                        Rectangle goRect = go.getRectangle();
                        if (goRect != null) {
                            go
                                    .setRectangle(new Rectangle(pos.x, pos.y, goRect.width,
                                            goRect.height));
                        } else
                            go.setRectangle(new Rectangle(pos.x, pos.y, 100, 70));

                    }
                }
                pos.x += 20;
                pos.y += 20;

                graphicComponents.add(go);
            }
        }
        return graphicComponents;
    }

    protected final void doActionPerformed() {
        DefaultMainFrame mf = ApplicationContext.getDefaultMainFrame();

        try {
            DiagramInternalFrame diagFrame = (DiagramInternalFrame) ((DefaultComparableElement) getSelectedObject()).object;
            // Instead of listening for internal frame closing, verify if the frame is still visible.
            // This is a side effect of accelerator.  The action may not have been updated if a diag internal frame
            // has been close.
            if (!diagFrame.isVisible()) {
                return;
            }

            Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
            objects = findNbDuplicableObjects(objects);

            if (objects.length == 0) {
                String key = (objects.length > 1) ? "CannotDuplicateSelectedObjects"
                        : "CannotDuplicateSelectedObject";
                String msg = LocaleMgr.message.getString(key);
                String title = ApplicationContext.getApplicationName();
                JOptionPane.showMessageDialog(mf, msg, title, JOptionPane.ERROR_MESSAGE);
            } else {
                duplicateObjects(diagFrame, objects);
            } //end if

        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(mf, e);
        }
    }

    private void duplicateObjects(DiagramInternalFrame diagFrame, Object[] objects)
            throws DbException, PropertyVetoException {
        ApplicationDiagram diagModel = diagFrame.getDiagram();
        DbSMSDiagram diagGO = (DbSMSDiagram) diagModel.getDiagramGO();
        diagGO.getDb().beginTrans(Db.WRITE_TRANS, kCreateGraphRep);
        ArrayList<DbSMSGraphicalObject> graphicComponents = sendToDiagram(diagModel, objects);

        // select the new graphical objects in the diagram
        diagModel.deselectAll();
        for (int i = 0; i < graphicComponents.size(); i++) {

            if (isDuplicableObject(objects[i])) {
                DbSMSGraphicalObject go = graphicComponents.get(i);
                if (go != null) {
                    GraphicComponent gc = (GraphicComponent) ((DbGraphicalObjectI) go)
                            .getGraphicPeer();
                    if (gc != null)
                        gc.setSelected(true);
                }
            }
        }
        diagGO.getDb().commitTrans();

        diagFrame.setVisible(true);
        diagFrame.setIcon(false);
        diagFrame.setSelected(true);
    }

    private Object[] findNbDuplicableObjects(Object[] objects) {
        List<Object> list = new ArrayList<Object>();

        for (int i = 0; i < objects.length; i++) {
            if (isDuplicableObject(objects[i])) {
                list.add(objects[i]);
            }
        } //end for

        Object[] duplicableObjects = new Object[list.size()];
        list.toArray(duplicableObjects);
        return duplicableObjects;
    }

    private boolean isDuplicableObject(Object object) {
        boolean duplicable = true;

        if (object instanceof ORChoiceSpecialization) {
            duplicable = false;
        } else if (object instanceof DbORChoiceOrSpecialization) {
            duplicable = false;
        } else if (object instanceof DbORANestedTableStorage) {
            duplicable = false;
        } else if (object instanceof DbORAssociation) {
            duplicable = false;
        } //end if

        return duplicable;
    }

    public void performAction(DbObject[] dropObjects, DbObject targetObjectContainer, Point location)
            throws DbException {
        if (!(targetObjectContainer instanceof DbSMSDiagram))
            return;

        DbSMSDiagram diagGO = (DbSMSDiagram) targetObjectContainer;
        diagGO.getDb().beginTrans(Db.WRITE_TRANS, kCreateGraphRep);
        ArrayList<DbSMSGraphicalObject> graphicComponents = sendToDiagram(diagGO, dropObjects,
                location);

        diagGO.getDb().commitTrans();

        /*
         * int arraySize = graphicComponents.size(); GraphicComponent[] gpArray = new
         * GraphicComponent[arraySize]; for(int i = 0; i < arraySize; i++){ gpArray[i] =
         * (GraphicComponent)graphicComponents.get(i).getGraphicPeer(); }
         */

        ////
        // Take all newly created graphical objects and perform a layout on them
        /*
         * if(gpArray.length > 0){ LayoutSelectionAction smsAction =
         * (LayoutSelectionAction)ApplicationContext
         * .getActionStore().get(SMSActionsStore.LAYOUT_SELECTION); smsAction.performAction(gpArray,
         * m_matchingAppDiag); }
         */

        ////
        // select the new graphical objects in the diagram
        diagGO.getDb().beginTrans(Db.WRITE_TRANS, kCreateGraphRep);
        m_matchingAppDiag.deselectAll();
        for (int i = 0; i < graphicComponents.size(); i++) {
            DbSMSGraphicalObject go = graphicComponents.get(i);
            if (go != null) {
                GraphicComponent gc = (GraphicComponent) ((DbGraphicalObjectI) go).getGraphicPeer();
                if (gc != null)
                    gc.setSelected(true);
            }
        }
        diagGO.getDb().commitTrans();
        m_matchingAppDiag = null;

    }

    // Must be called within a write transaction.  This method is also used by paste (see SMSSemanticalModel).
    // ApplicationDiagram's corresponding InternalFrame must be visible.
    public final ArrayList<DbSMSGraphicalObject> sendToDiagram(DbSMSDiagram diagGO,
            Object[] objects, Point location) throws DbException {

        ////
        // find the application diagram that matches this graphical object

        m_matchingAppDiag = null;
        ApplicationDiagram appDiag = null;
        JInternalFrame[] frames = ApplicationContext.getDefaultMainFrame()
                .getDiagramInternalFrames();
        int i, nb;
        for (i = nb = 0; i < frames.length; i++) {
            appDiag = (ApplicationDiagram) ((DiagramInternalFrame) frames[i]).getDiagram();
            DbSMSDiagram matchingDiagGo = (DbSMSDiagram) appDiag.getDiagramGO();
            if (diagGO.equals(matchingDiagGo)) {
                m_matchingAppDiag = appDiag;
                break;
            }
        }

        if (m_matchingAppDiag == null) {
            ShowDiagramAction smsAction = (ShowDiagramAction) ApplicationContext.getActionStore()
                    .get(SMSActionsStore.SHOW_DIAGRAM);
            smsAction.performAction(diagGO);

            frames = ApplicationContext.getDefaultMainFrame().getDiagramInternalFrames();
            for (i = nb = 0; i < frames.length; i++) {
                appDiag = (ApplicationDiagram) ((DiagramInternalFrame) frames[i]).getDiagram();
                DbSMSDiagram matchingDiagGo = (DbSMSDiagram) appDiag.getDiagramGO();
                if (diagGO.equals(matchingDiagGo)) {
                    m_matchingAppDiag = appDiag;
                    break;
                }
            }
        } else { //give focus to the diagram frame
            try {
                DiagramInternalFrame diagFrame = ((DiagramInternalFrame) frames[i]);
                diagFrame.setVisible(true);
                diagFrame.setIcon(false);
                diagFrame.setSelected(true);
            } catch (PropertyVetoException pv) {

            }
        }

        Arrays.sort(objects, new SourceObjectComparator());
        ArrayList<DbSMSGraphicalObject> graphicComponents = new ArrayList<DbSMSGraphicalObject>();
        Rectangle viewRect = m_matchingAppDiag.getMainView().getViewRect();

        location.x = (int) ((float) location.x / m_matchingAppDiag.getMainView().getZoomFactor());
        location.y = (int) ((float) location.y / m_matchingAppDiag.getMainView().getZoomFactor());

        m_matchingAppDiag.deselectAll();

        for (i = 0; i < objects.length; i++) {

            viewRect.setRect(location.x, location.y, 0, 0);

            DbSMSGraphicalObject go = null;
            if (objects[i] instanceof DbORAbsTable || objects[i] instanceof DbORTypeClassifier)
                go = new DbORTableGo(diagGO, (DbSMSClassifier) objects[i]);
            else if (objects[i] instanceof DbORCommonItem)
                go = new DbSMSCommonItemGo(diagGO, (DbORCommonItem) objects[i]);
            else if (objects[i] instanceof DbOOAdt) {
                go = new DbOOAdtGo(diagGO, (DbOOAdt) objects[i]);
                viewRect = m_matchingAppDiag.getMainView().getViewRect();
                viewRect.setRect(viewRect.x + location.x, viewRect.y + location.y, 0, 0);
            } else if (objects[i] instanceof DbSMSPackage)
                go = new DbSMSPackageGo(diagGO, (DbSMSPackage) objects[i]);
            else if (objects[i] instanceof DbSMSNotice) {
                DbSMSNotice notice = (DbSMSNotice) objects[i];
                DbSMSNoticeGo noticeGo = new DbSMSNoticeGo(diagGO);
                noticeGo.setNotice(notice);
                go = noticeGo;
            } else if (objects[i] instanceof DbORAssociation) {
                DbORAssociation assoc = (DbORAssociation) objects[i];
                if (DbGraphic.getFirstGraphicalObject(diagGO, assoc) == null)
                    AnyGo.createORAssociationGo(diagGO, assoc, location);
            }
            //BE GO
            else if (objects[i] instanceof DbBEUseCase)
                go = new DbBEUseCaseGo(diagGO, (DbSMSClassifier) objects[i]);
            else if (objects[i] instanceof DbBEStore)
                go = new DbBEStoreGo(diagGO, (DbSMSClassifier) objects[i]);
            else if (objects[i] instanceof DbBEActor)
                go = new DbBEActorGo(diagGO, (DbSMSClassifier) objects[i]);
            else if (objects[i] instanceof DbBEFlow) {
                DbBEFlow flow = (DbBEFlow) objects[i];
                AnyGo.createBEFlowGo(diagGO, flow, location);
            }

            if (go != null) {
                if (go.isAutoFit()) {
                    Rectangle goRect = go.getRectangle();
                    go.setRectangle(new Rectangle(viewRect.x, viewRect.y, goRect.width,
                            goRect.height));
                } else {
                    Rectangle goRect = go.getRectangle();
                    if (goRect != null) {
                        go.setRectangle(new Rectangle(viewRect.x, viewRect.y, goRect.width,
                                goRect.height));
                    } else
                        go.setRectangle(new Rectangle(location.x, location.y, 100, 70));
                }
            }
            if (objects.length > 1) {
                /*
                 * if(singletonGrid.isActive()){ location.x += lateralOffset; } else{
                 */
                location.x += 20;
                location.y += 20;
                //}
            }
            graphicComponents.add(go);
        }

        return graphicComponents;
    }

    // Return the DbGraphicalObjects corresponding to the selection
    // If a line is selected, return also the DbGraphicalObjects corresponding to the nodes of the line
    private DbSMSGraphicalObject[] getSourceGos(Object[] objects, DbSMSDiagram diagGO)
            throws DbException {
        HashSet srcGoSet = new HashSet(objects.length * 2);
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof LineLabel || objects[i] instanceof Attachment)
                continue;
            if (objects[i] instanceof SrLine) {
                // Do not copy the line if selected alone (without its nodes, minimum one node)
                SrLine line = (SrLine) objects[i];
                GraphicNode node1 = line.getNode1();
                GraphicNode node2 = line.getNode2();
                if ((node1 != null && node2 != null) && !(node1.isSelected() || node2.isSelected())) {
                    continue;
                } else if (node1 != null && node2 != null) {
                    srcGoSet.add(((ActionInformation) node1).getGraphicalObject());
                    srcGoSet.add(((ActionInformation) node2).getGraphicalObject());
                } else if (node1 != null)
                    if (node1.isSelected())
                        srcGoSet.add(((ActionInformation) node1).getGraphicalObject());
                    else
                        continue;
                else if (node2 != null)
                    if (node2.isSelected())
                        srcGoSet.add(((ActionInformation) node2).getGraphicalObject());
                    else
                        continue;
            }
            if (objects[i] instanceof ActionInformation)
                srcGoSet.add(((ActionInformation) objects[i]).getGraphicalObject());
        }
        DbSMSGraphicalObject[] srcGos = new DbSMSGraphicalObject[srcGoSet.size()];
        return (DbSMSGraphicalObject[]) srcGoSet.toArray(srcGos);
    }

    private void translateDestGos(DbObject[] dstGos, int x, int y) throws DbException {
        for (int i = 0; i < dstGos.length; i++) {
            if (dstGos[i] instanceof DbSMSLineGo) {
                Polygon poly = ((DbSMSLineGo) dstGos[i]).getPolyline();
                poly.translate(x, y);
                ((DbSMSLineGo) dstGos[i]).setPolyline(poly);
            } else if (dstGos[i] instanceof DbSMSFreeLineGo) {
                Polygon poly = ((DbSMSFreeLineGo) dstGos[i]).getPolyline();
                poly.translate(x, y);
                ((DbSMSFreeLineGo) dstGos[i]).setPolyline(poly);
            } else if (dstGos[i] != null) {
                Rectangle rect = ((DbSMSGraphicalObject) dstGos[i]).getRectangle();
                rect.translate(x, y);
                ((DbSMSGraphicalObject) dstGos[i]).setRectangle(rect);
            } //end if
        } //end for
    } //end translateDestGos()

    private void transferLines(DbObject srcGo, DbObject dstGo, boolean front) throws DbException {
        MetaRelationN linesRel = (front ? DbSMSGraphicalObject.fFrontEndLineGos
                : DbSMSGraphicalObject.fBackEndLineGos);
        MetaRelation1 goRel = (front ? DbSMSLineGo.fFrontEndGo : DbSMSLineGo.fBackEndGo);
        DbEnumeration dbEnum = ((DbRelationN) srcGo.get(linesRel)).elements();
        while (dbEnum.hasMoreElements()) {
            DbObject lineGo = dbEnum.nextElement();
            lineGo.set(goRel, dstGo);
            DbGraphic.createPolyline(lineGo);
        }
        dbEnum.close();
    }

    public final void updateSelectionAction() throws DbException {

        Object object = ApplicationContext.getFocusManager().getFocusObject();
        if (object instanceof ExplorerView)
            this.setName(kSendToDiagramExp);
        else
            this.setName(kSendToDiagram);

        DbSMSPackage smsPackage = null;
        DbObject[] dbos = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();

        if (dbos.length >= 1) {

            //if all objects do not belong to the same model node, reject the selection
            smsPackage = (DbSMSPackage) dbos[0].getCompositeOfType(DbBEModel.metaClass);
            if (smsPackage instanceof DbBEModel) {
                for (int i = 0; i < dbos.length; i++) {
                    if (!smsPackage.equals(dbos[i].getCompositeOfType(DbBEModel.metaClass))) {
                        setDomainValues(null);
                        setEnabled(false);
                        return;
                    }
                }
            } else {
                Object[] objects = new Object[dbos.length];
                for (int i = 0; i < objects.length; i++) {
                    objects[i] = dbos[i];
                }
                objects = findNbDuplicableObjects(objects);

                if (objects.length == 0) {
                    setDomainValues(null);
                    setEnabled(false);
                    return;
                }
                /*
                 * //jjj for (int i = 0; i < dbos.length; i++) { if (dbos[i] instanceof ) {
                 * 
                 * } }
                 */
            } //end if
        } //end if

        DefaultComparableElement[] frames = getValidFrames(smsPackage);
        if (frames == null || frames.length == 0) {
            setDomainValues(null);
            setEnabled(false);
        } else {
            DefaultComparableElement[] newframes = new DefaultComparableElement[frames.length + 2];
            // the top internal frame is the first element return by getValidFrames()
            newframes[0] = new DefaultComparableElement(frames[0].object, kCurrentDiagram);
            newframes[1] = null; // separator
            Arrays.sort(frames);
            System.arraycopy(frames, 0, newframes, 2, frames.length);
            setDomainValues(newframes);
            setEnabled(newframes.length > 0);
        }
    }

    private DefaultComparableElement[] getValidFrames(DbObject smspackage) throws DbException {
        Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
        if (objects.length == 0)
            return null;
        boolean beContextInSelection = false;
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof BEContext) {
                beContextInSelection = true;
                break;
            }
        }
        DbObject pack = null;
        ApplicationDiagram curDiag = ApplicationContext.getFocusManager().getActiveDiagram();
        if (curDiag != null) { // diagram selection
            if (!isValidDiagramSelection(objects))
                return null;
            pack = curDiag.getSemanticalObject();
        } else if (objects[0] instanceof DbObject) { // explorer selection, all objects must belong to the same package
            pack = ((DbObject) objects[0]).getCompositeOfType(DbBEUseCase.metaClass);
            if (pack == null)
                pack = ((DbObject) objects[0]).getCompositeOfType(DbSMSPackage.metaClass);
            for (int i = 1; i < objects.length; i++) {
                if (!(objects[i] instanceof DbObject)
                        || ((pack != ((DbObject) objects[i])
                                .getCompositeOfType(DbSMSPackage.metaClass)) && (pack != ((DbObject) objects[i])
                                .getCompositeOfType(DbBEUseCase.metaClass))))
                    return null;
            }
        } else {
            return null; // In explorer, Db instances may be selected (if repository)
        }

        // Keep only the diagrams belonging to the common package of the selection.
        // For OO, acccept any OO diagram of the same project.
        JInternalFrame[] frames = ApplicationContext.getDefaultMainFrame()
                .getDiagramInternalFrames();
        int i, nb;
        for (i = nb = 0; i < frames.length; i++) {
            ApplicationDiagram diag = ((DiagramInternalFrame) frames[i]).getDiagram();
            if (pack instanceof DbOOAbsPackage) {
                if (!(diag.getSemanticalObject() instanceof DbOOAbsPackage))
                    continue;
                if (diag.getProject() != pack.getProject())
                    continue;
            } else if (pack instanceof DbBEUseCase || pack instanceof DbBEModel) {
                if (!(diag.getSemanticalObject() instanceof DbBEUseCase))
                    continue;
                if (diag.getProject() != pack.getProject())
                    continue;
                if (beContextInSelection && nb > 0)
                    continue;
            } else {
                if (diag.getSemanticalObject() != pack)
                    continue;
            }
            DbSMSDiagram diagram = (DbSMSDiagram) diag.getDiagramGO();
            if (diagram instanceof DbBEDiagram)//if the diagram is a BE diagram and the diagram doe snot belong to the right model, skip it
                if (!diagram.getCompositeOfType(DbBEModel.metaClass).equals(smspackage))
                    continue;

            frames[nb++] = frames[i];
        }
        DefaultComparableElement[] validFrames = new DefaultComparableElement[nb];
        for (i = 0; i < nb; i++)
            validFrames[i] = new DefaultComparableElement(frames[i], frames[i].getTitle());
        return validFrames;
    }

    // All selected objects must be GraphicComponents; there must not be only LineLabels
    private boolean isValidDiagramSelection(Object[] objects) {
        int nb = 0;
        for (int i = 0; i < objects.length; i++) {
            if (!(objects[i] instanceof GraphicComponent))
                return false;
            if (!(objects[i] instanceof LineLabel || objects[i] instanceof Attachment))
                nb++;
        }
        return (nb > 0);
    }
}
