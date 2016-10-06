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

package org.modelsphere.jack.srtool.actions;

import java.util.ArrayList;

import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.graphic.AssociationRoles;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.LineLabel;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.SrVector;

@SuppressWarnings("serial")
public class FindInDiagramAction extends AbstractDomainAction implements SelectionActionListener {

    private static final String kFindInDiagram = LocaleMgr.action.getString("FindInDiagram");
    private static final String kDiagTitle = LocaleMgr.screen.getString("diagTitle3");
    //    private static final String kNbFound = LocaleMgr.message.getString("nInstancesFound");

    SrVector vecGos = null;

    public FindInDiagramAction() {
        super(kFindInDiagram, false);
    }

    protected final void doActionPerformed() {
        DefaultMainFrame mf = ApplicationContext.getDefaultMainFrame();
        try {
            Object objRetVal = ((DefaultComparableElement) getSelectedObject()).object;
            if (vecGos.get(0) instanceof ArrayList) {

                DbObject associationGo = (DbObject) objRetVal;
                associationGo.getDb().beginReadTrans();
                DiagramInternalFrame diagFrame = mf.addDiagramInternalFrame(associationGo
                        .getComposite());
                associationGo.getDb().commitTrans();
                ApplicationDiagram diagModel = diagFrame.getDiagram();
                diagModel.deselectAll();

                //find the graphical object that was selected in the array,
                //then connect to the line label that interests us
                AssociationRoles ar = null;
                ArrayList arrayOfLabels = (ArrayList) vecGos.get(0);
                for (int i = 0; i < arrayOfLabels.size(); i++) {
                    ar = (AssociationRoles) arrayOfLabels.get(i);
                    if (ar.getAssociationGo() == objRetVal)
                        break;
                }
                if (ar != null) {

                    //locate the non-null role
                    LineLabel lineLabel = ar.getLineLabel1();
                    if (lineLabel == null)
                        lineLabel = ar.getLineLabel2();

                    GraphicComponent gc = lineLabel;
                    if (gc == null) {
                        DbObject semObj = ApplicationContext.getFocusManager()
                                .getSelectedSemanticalObjects()[0];
                        ArrayList listOfLabels = ApplicationContext.getSemanticalModel()
                                .getLineLabels(semObj);
                        if (listOfLabels != null) {
                            for (int i = 0; i < listOfLabels.size(); i++) {
                                ar = (AssociationRoles) listOfLabels.get(i);
                                if (ar.getAssociationGo() == objRetVal)
                                    break;
                            }
                            if (ar != null) {
                                //locate the non-null role
                                lineLabel = ar.getLineLabel1();
                                if (lineLabel == null)
                                    lineLabel = ar.getLineLabel2();
                            }
                        }
                    }
                    gc = lineLabel;
                    if (gc != null) {
                        gc.setSelected(true);
                        diagModel.getMainView().scrollRectToCenter(gc.getRectangle());
                        diagModel.getMainView().repaint();
                    }
                }
            } else {
                DbObject go = (DbObject) objRetVal;
                go.getDb().beginTrans(Db.READ_TRANS);
                DiagramInternalFrame diagFrame = mf.addDiagramInternalFrame(go.getComposite());
                ApplicationDiagram diagModel = diagFrame.getDiagram();
                diagModel.deselectAll();
                GraphicComponent gc = (GraphicComponent) ((DbGraphicalObjectI) go).getGraphicPeer();
                if (gc != null) {
                    gc.setSelected(true);
                    diagModel.getMainView().scrollRectToCenter(gc.getRectangle());
                    diagModel.getMainView().repaint();
                }
                go.getDb().commitTrans();
            }
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(mf, e);
        }
    }

    public final void updateSelectionAction() throws DbException {
        vecGos = getValidGos();

        int nb = vecGos.size();
        if (nb == 0) {
            setEnabled(false);
            return;
        } else if (nb == 1 && vecGos.get(0) instanceof ArrayList) {
            ArrayList listOfLabels = (ArrayList) vecGos.get(0);

            // Regroup duplicate elements for each diagrams - needed for displaying the duplicate number
            ArrayList diagList = new ArrayList();
            ArrayList diagObjectsList = new ArrayList();
            if (listOfLabels.size() == 0) {
                setEnabled(false);
                return;
            }
            for (int i = 0; i < listOfLabels.size(); i++) {
                AssociationRoles ar = (AssociationRoles) listOfLabels.get(i);
                DbObject go = ar.getAssociationGo();
                Object diag = go.getComposite();
                if (diagList.contains(diag)) {
                    ArrayList diagObjects = (ArrayList) diagObjectsList.get(diagList.indexOf(diag));
                    diagObjects.add(go);
                } else {
                    diagList.add(diag);
                    ArrayList diagObjects = new ArrayList();
                    diagObjects.add(go);
                    diagObjectsList.add(diagObjects);
                }
            }

            // Build the list of values for this action
            DefaultComparableElement[] values = new DefaultComparableElement[listOfLabels.size()];
            int valuesCursor = 0;
            int diagcount = diagList.size();
            for (int i = 0; i < diagcount; i++) {
                ArrayList diagObjects = (ArrayList) diagObjectsList.get(i);
                int diagGosCount = diagObjects.size();
                for (int j = 0; j < diagGosCount; j++) {
                    DbObject go = (DbObject) diagObjects.get(j);
                    DbObject diag = go.getComposite();
                    String packName = diag.getComposite().getSemanticalName(DbObject.LONG_FORM);
                    String title = MessageFormat.format(kDiagTitle, new Object[] { packName,
                            diag.getName(), new Integer(j + 1), new Integer(diagGosCount) });
                    values[valuesCursor] = new DefaultComparableElement(go, title);
                    valuesCursor++;
                }
            }
            setDomainValues(values);
            setEnabled(true);
        } else {
            // Regroup duplicate elements for each diagrams - needed for displaying the duplicate number
            ArrayList diagList = new ArrayList();
            ArrayList diagObjectsList = new ArrayList();
            for (int i = 0; i < vecGos.size(); i++) {
                DbObject go = (DbObject) vecGos.get(i);
                Object diag = go.getComposite();
                if (diagList.contains(diag)) {
                    ArrayList diagObjects = (ArrayList) diagObjectsList.get(diagList.indexOf(diag));
                    diagObjects.add(go);
                } else {
                    diagList.add(diag);
                    ArrayList diagObjects = new ArrayList();
                    diagObjects.add(go);
                    diagObjectsList.add(diagObjects);
                }
            }

            // Build the list of values for this action
            DefaultComparableElement[] values = new DefaultComparableElement[nb];
            int valuesCursor = 0;
            int diagcount = diagList.size();
            for (int i = 0; i < diagcount; i++) {
                ArrayList diagObjects = (ArrayList) diagObjectsList.get(i);
                int diagGosCount = diagObjects.size();
                for (int j = 0; j < diagGosCount; j++) {
                    DbObject go = (DbObject) diagObjects.get(j);
                    DbObject diag = go.getComposite();
                    String packName = diag.getComposite().getSemanticalName(DbObject.LONG_FORM);
                    String title = MessageFormat.format(kDiagTitle, new Object[] { packName,
                            diag.getName(), new Integer(j + 1), new Integer(diagGosCount) });
                    values[valuesCursor] = new DefaultComparableElement(go, title);
                    valuesCursor++;
                }
            }
            setDomainValues(values);
            setEnabled(true);
        }
    }

    private SrVector getValidGos() throws DbException {
        SrVector vecGos = new SrVector();
        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        if (objects.length != 1) {
            return vecGos;
        }

        DbRelationN gos = ApplicationContext.getSemanticalModel().getGos(objects[0]);
        if (gos == null) {
            ArrayList listOfLabels = ApplicationContext.getSemanticalModel().getLineLabels(
                    objects[0]);
            if (listOfLabels != null) {
                vecGos.add(listOfLabels);
                return vecGos;
            } else {
                gos = ApplicationContext.getSemanticalModel().getGos(objects[0].getComposite());
                if (gos == null)
                    return vecGos;
            }
        }
        DbEnumeration dbEnum = gos.elements();
        while (dbEnum.hasMoreElements()) {
            DbObject go = dbEnum.nextElement();
            vecGos.add(go);
        }
        dbEnum.close();
        return vecGos;
    }

}
