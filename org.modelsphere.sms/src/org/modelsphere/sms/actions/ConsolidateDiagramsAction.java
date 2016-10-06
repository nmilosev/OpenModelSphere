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

/*
 * Created on Oct 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.modelsphere.sms.actions;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.awt.SelectDialog;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.actions.RefreshAllAction;
import org.modelsphere.jack.srtool.actions.ShowDiagramAction;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEStyle;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.db.DbSMSBuiltInTypeNode;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSNotation;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStyle;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.db.DbSMSUserDefinedPackage;
import org.modelsphere.sms.db.util.DbInitialization;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORNotation;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORStyle;
import org.modelsphere.sms.or.db.DbORUserNode;

/**
 * @author nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class ConsolidateDiagramsAction extends AbstractApplicationAction implements
        SelectionActionListener {
    private static final String SELECT_NOTATION = LocaleMgr.misc
            .getString("SelectWhichKindOfDiagram");

    public void updateSelectionAction() throws DbException {
        boolean enabled = false;
        Object focusObject = ApplicationContext.getFocusManager().getFocusObject();
        if (focusObject instanceof ApplicationDiagram) {

            DbObject dbo = ((ApplicationDiagram) focusObject).getDiagramGO();
            dbo.getDb().beginReadTrans();
            DbObject referenceComposite = dbo.getComposite();

            DbEnumeration enumer = referenceComposite.getComponents().elements(
                    DbSMSDiagram.metaClass);
            int count = 0;
            while (enumer.hasMoreElements()) {
                enumer.nextElement();
                count++;
                if (count == 2)
                    break;
            }
            enumer.close();
            dbo.getDb().commitTrans();

            setEnabled(count == 2);
            return;

        }

        DbObject[] semanticalObjects = ApplicationContext.getFocusManager()
                .getSelectedSemanticalObjects();
        if (semanticalObjects.length == 1) { // this is a model
            if (semanticalObjects[0] instanceof DbSMSPackage
                    && !(semanticalObjects[0] instanceof DbBEModel)) {
                if (!(semanticalObjects[0] instanceof DbSMSBuiltInTypeNode
                        || semanticalObjects[0] instanceof DbSMSUmlExtensibility
                        || semanticalObjects[0] instanceof DbORUserNode
                        || semanticalObjects[0] instanceof DbSMSUserDefinedPackage
                        || semanticalObjects[0] instanceof DbSMSBuiltInTypePackage
                        || semanticalObjects[0] instanceof DbOROperationLibrary
                        || semanticalObjects[0] instanceof DbSMSLinkModel || semanticalObjects[0] instanceof DbORDatabase)) {
                    semanticalObjects[0].getDb().beginReadTrans();
                    boolean hasOne = false;
                    DbEnumeration dbEnum = semanticalObjects[0].getComponents().elements(
                            DbSMSDiagram.metaClass);
                    if (dbEnum.hasMoreElements())
                        dbEnum.nextElement();
                    if (dbEnum.hasMoreElements() == false)
                        hasOne = true;
                    dbEnum.close();
                    semanticalObjects[0].getDb().commitTrans();
                    enabled = !hasOne;
                }
            } else if (semanticalObjects[0] instanceof DbBEUseCase) {
                DbBEUseCase useCase = (DbBEUseCase) semanticalObjects[0];
                useCase.getDb().beginReadTrans();
                if (true/* useCase.isContext() */) {
                    boolean hasOne = false;
                    DbEnumeration dbEnum = semanticalObjects[0].getComponents().elements(
                            DbSMSDiagram.metaClass);
                    if (dbEnum.hasMoreElements())
                        dbEnum.nextElement();
                    if (dbEnum.hasMoreElements() == false)
                        hasOne = true;
                    dbEnum.close();
                    semanticalObjects[0].getDb().commitTrans();
                    enabled = !hasOne;
                }
                useCase.getDb().commitTrans();
            }
        } else if (semanticalObjects.length > 1) {
            enabled = true;
            DbMultiTrans.beginTrans(Db.READ_TRANS, semanticalObjects, "");
            DbObject referenceComposite = semanticalObjects[0].getComposite();
            for (int i = 0; i < semanticalObjects.length; i++) {
                if (!(semanticalObjects[i] instanceof DbSMSDiagram)
                        || !referenceComposite.equals(semanticalObjects[i].getComposite())) {
                    enabled = false;
                    break;
                }
            }
            DbMultiTrans.commitTrans(semanticalObjects);
        }
        setEnabled(enabled);
    }

    public static final String kConsolidateDiagrams = LocaleMgr.action
            .getString("consolidateDiagrams");

    ConsolidateDiagramsAction() {
        super(kConsolidateDiagrams);
        setVisible(ScreenPerspective.isFullVersion());
    }

    public final DbSMSDiagram performAction(DbObject modelOrUseCase, boolean keepSourceDiagrams) {
        DbObject[] diagrams = null;
        try {
            if (modelOrUseCase != null) {
                ArrayList<DbObject> arrayDiagrams = new ArrayList<DbObject>();
                modelOrUseCase.getDb().beginReadTrans();
                DbEnumeration dbEnum = modelOrUseCase.getComponents().elements(
                        DbSMSDiagram.metaClass);
                while (dbEnum.hasMoreElements())
                    arrayDiagrams.add(dbEnum.nextElement());
                dbEnum.close();
                modelOrUseCase.getDb().commitTrans();
                diagrams = new DbObject[arrayDiagrams.size()];
                arrayDiagrams.toArray(diagrams);

                return consolidateDiagrams(diagrams, keepSourceDiagrams);
            }
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
        return null;
    }

    public final DbSMSDiagram performAction(DbObject[] diagrams, boolean keepSourceDiagrams) {
        try {
            if (diagrams != null) {
                return consolidateDiagrams(diagrams, keepSourceDiagrams);
            }
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
        return null;
    }

    protected final void doActionPerformed(ActionEvent e) {
        try {
            ArrayList<DbObject> al = new ArrayList<DbObject>();
            Point pos = getDiagramLocation(e); // if triggered from the diagram, create all graphics at the click position
            DbObject[] diagrams;
            Object focusObject = ApplicationContext.getFocusManager().getFocusObject();
            if (focusObject instanceof ApplicationDiagram) {
                DbObject object = ((ApplicationDiagram) focusObject).getSemObj();
                performAction(object, false);
            } else {
                diagrams = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
                if (diagrams.length > 0 && !(diagrams[0] instanceof DbSMSDiagram)) {
                    diagrams[0].getDb().beginReadTrans();
                    DbEnumeration dbEnum = diagrams[0].getComponents().elements(
                            DbSMSDiagram.metaClass);
                    while (dbEnum.hasMoreElements())
                        al.add(dbEnum.nextElement());
                    dbEnum.close();
                    diagrams[0].getDb().commitTrans();
                    diagrams = new DbObject[al.size()];
                    al.toArray(diagrams);
                }
                consolidateDiagrams(diagrams, false);
            }
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    } //end createOtherGraphics()

    private DbSMSDiagram consolidateDiagrams(DbObject[] diagrams, boolean keepDiagrams) {
        DbProject project = (DbProject) diagrams[0].getProject();
        DbSMSDiagram targetDiagram = null;
        try {
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, diagrams, kConsolidateDiagrams);

            // collect the total width and total height for the new diagram.
            double totalWidth = 0, totalHeight = 1;
            for (int i = 0; i < diagrams.length; i++) {
                Dimension dm = ((DbSMSDiagram) diagrams[i]).getNbPages();
                totalWidth += dm.getWidth();
                totalHeight = Math.max(totalHeight, dm.getHeight());
            }
            if (diagrams.length > 0) {
                if (diagrams[0] instanceof DbORDiagram)
                    targetDiagram = new DbORDiagram(diagrams[0].getComposite());
                else if (diagrams[0] instanceof DbOODiagram)
                    targetDiagram = new DbOODiagram(diagrams[0].getComposite());
                else if (diagrams[0] instanceof DbBEDiagram)
                    targetDiagram = new DbBEDiagram(diagrams[0].getComposite());

                Integer printScale = ((DbSMSDiagram) diagrams[0]).getPrintScale();
                if (targetDiagram != null) {
                    targetDiagram.getDb().beginReadTrans();
                    targetDiagram.set(DbGraphic.fDiagramNbPages, new Dimension((int) totalWidth,
                            (int) totalHeight));
                    targetDiagram.set(DbGraphic.fDiagramPrintScale, printScale);
                    targetDiagram.getDb().commitTrans();
                }
            }

            // //
            // for each diagram, for all components, set the composite to the
            // new "consolidated" diagram
            // and set the according go translation based on the width offset

            if (targetDiagram != null) {

                // //
                // select the notation for the target diagram

                // collect notations in an array
                Object[] dboNotations = new DbObject[diagrams.length];
                DbObject[] dboStyles = new DbObject[diagrams.length];
                for (int i = 0; i < diagrams.length; i++) {
                    if (diagrams[i] instanceof DbORDiagram) {
                        dboNotations[i] = ((DbORDiagram) diagrams[i]).findNotation();
                        dboStyles[i] = ((DbORDiagram) diagrams[i]).findStyle();
                    } else if (diagrams[i] instanceof DbBEDiagram) {
                        dboNotations[i] = ((DbBEDiagram) diagrams[i]).findNotation();
                        dboStyles[i] = ((DbBEDiagram) diagrams[i]).findStyle();
                    }
                }

                // check if all notations are the same
                boolean bAllTheSame = true;
                DbObject referenceNotation = (DbObject) dboNotations[0];
                for (int i = 0; i < diagrams.length; i++) {
                    if (dboNotations[i] != null)
                        if (false == dboNotations[i].equals(referenceNotation)) {
                            bAllTheSame = false;
                            break;
                        }
                }

                DbSMSStyle style = null;
                DbSMSNotation notation = null;
                if (false == bAllTheSame) { // among the used notations
                    JFrame frame = MainFrame.getSingleton();
                    String[] strNotationNames = new String[dboNotations.length];
                    for (int i = 0; i < dboNotations.length; i++)
                        strNotationNames[i] = ((DbObject) dboNotations[i]).getName();

                    // remove duplicates
                    int countNulls = 0;
                    for (int i = 0; i < strNotationNames.length; i++)
                        for (int j = i + 1; j < strNotationNames.length; j++)
                            if (strNotationNames[i] != null)
                                if (strNotationNames[i].equals(strNotationNames[j])) {
                                    strNotationNames[j] = null;
                                    countNulls++;
                                }

                    String[] uniqueNames = new String[strNotationNames.length - countNulls];
                    for (int i = 0; i < strNotationNames.length; i++)
                        if (strNotationNames[i] != null)
                            uniqueNames[i] = strNotationNames[i];

                    String sname = uniqueNames[0];
                    if (!keepDiagrams) {
                        int idx = SelectDialog.select(frame, SELECT_NOTATION, uniqueNames,
                                (Icon[]) null, 0, true, true);
                        if (idx == -1) {
                            for (int i = 0; i < diagrams.length; i++) {
                                diagrams[i].getDb().abortTrans();
                            }
                            return null;
                        }
                        sname = uniqueNames[idx];
                    }

                    // //
                    // find the notation and style by name in the project node

                    DbObject retval = project.findComponentByName(DbBENotation.metaClass, sname);
                    if (retval != null) {
                        notation = (DbBENotation) retval;
                        style = getStyleForBENotation((DbSMSProject) project,
                                (DbBENotation) notation);
                    } else {
                        retval = project.findComponentByName(DbORNotation.metaClass, sname);
                        if (retval != null) {
                            notation = (DbORNotation) retval;
                            style = getStyleForORNotation((DbSMSProject) project,
                                    (DbORNotation) notation);
                        }
                    }
                } else { // automatically select notation and style for the new
                    // diagram
                    notation = (DbSMSNotation) dboNotations[0];
                    style = (DbSMSStyle) dboStyles[0];
                }

                double xOffset = 0;
                if (notation != null) {
                    if (targetDiagram instanceof DbORDiagram) {
                        DbORDiagram targetDgm = ((DbORDiagram) targetDiagram);
                        targetDgm.setNotation((DbORNotation) notation);
                        if (targetDiagram.getComposite() instanceof DbORDataModel)
                            targetDgm.setStyle(style);
                    } else if (targetDiagram instanceof DbBEDiagram) {
                        DbBEDiagram targetDgm = ((DbBEDiagram) targetDiagram);
                        targetDgm.setNotation((DbBENotation) notation);
                        targetDgm.setStyle(style);
                    }
                    targetDiagram.setName(notation.getName() + " ("
                            + LocaleMgr.action.getString("consolidatedDiagram") + ")");
                }
                targetDiagram.getDb().beginWriteTrans(kConsolidateDiagrams);
                for (int i = 0; i < diagrams.length; i++) {
                    if (i == 0) { // no offset to compute
                        // take all components from the diagram and reconnect
                        // them with the new composite
                        DbEnumeration dbEnum = diagrams[0].getComponents().elements();
                        while (dbEnum.hasMoreElements()) {
                            DbObject dbo = dbEnum.nextElement();
                            dbo.getDb().beginWriteTrans("");
                            dbo.setComposite(targetDiagram);
                            dbo.getDb().commitTrans();
                        }
                        dbEnum.close();
                    } else { // take all components from the diagram and
                        // reconnect them with the new composite
                        DbSMSDiagram dgm = ((DbSMSDiagram) diagrams[i - 1]);
                        double nbPages = dgm.getNbPages().getWidth();
                        int printScale = dgm.getPrintScale().intValue();
                        xOffset += nbPages * dgm.getPageFormat().getImageableWidth() * 100.0
                                / (double) printScale;
                        DbEnumeration dbEnum = diagrams[i].getComponents().elements();
                        while (dbEnum.hasMoreElements()) {
                            DbObject dbo = dbEnum.nextElement();
                            dbo.getDb().beginWriteTrans("");
                            dbo.setComposite(targetDiagram);
                            if (dbo instanceof DbSMSGraphicalObject) {
                                DbSMSGraphicalObject go = (DbSMSGraphicalObject) dbo;
                                Rectangle r = go.getRectangle();
                                if (r != null) {
                                    r.x += xOffset;
                                    go.setRectangle(r);
                                }
                            }
                            dbo.getDb().commitTrans();
                        }
                        dbEnum.close();
                    }
                }
                targetDiagram.getDb().commitTrans();

                DiagramInternalFrame dif = null;
                if (!keepDiagrams) {
                    for (int i = 0; i < diagrams.length; i++)
                        diagrams[i].doDeleteAction();

                    ShowDiagramAction action = (ShowDiagramAction) ApplicationContext
                            .getActionStore().get(AbstractActionsStore.SHOW_DIAGRAM);
                    dif = action.performAction(targetDiagram);
                    if (dif != null) {
                        DiagramView dv = dif.getDiagram().getMainView();
                        dv.setZoomFactor(dv.getPanoramaZoomFactor());
                    }
                }
                DbMultiTrans.commitTrans(diagrams);
                RefreshAllAction actionRefresh = (RefreshAllAction) ApplicationContext
                        .getActionStore().get(AbstractActionsStore.REFRESH_ALL);
                actionRefresh.doActionPerformed();

            }
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
        return targetDiagram;
    }

    public DbSMSStyle getStyleForORNotation(DbSMSProject project, DbORNotation notation)
            throws DbException {
        DbSMSStyle style = null;
        int notationId = notation.getNotationID().intValue();
        switch (notationId) {
        case DbInitialization.ENTITY_RELATIONSHIP: {
            style = (DbSMSStyle) project.findComponentByName(DbORStyle.metaClass,
                    DbORStyle.ENTITY_RELATIONSHIP_STYLE_NAME);
        }
            break;
        default: {
            style = (DbSMSStyle) project.findComponentByName(DbORStyle.metaClass,
                    DbORStyle.DEFAULT_STYLE_NAME);
        }
            break;
        }
        return style;

    }

    public DbSMSStyle getStyleForBENotation(DbSMSProject project, DbBENotation notation)
            throws DbException {

        DbSMSStyle style = null;
        int notationId = notation.getNotationID().intValue();
        switch (notationId) {
        case DbInitialization.UML_USE_CASE: {
            style = (DbSMSStyle) project.findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_USE_CASE_STYLE_NAME);
        }
            break;
        case DbInitialization.UML_ACTIVITY_DIAGRAM: {
            style = (DbSMSStyle) project.findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_ACTIVITY_STYLE_NAME);
        }
            break;
        case DbInitialization.UML_COLLABORATION_DIAGRAM: {
            style = (DbSMSStyle) project.findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_COLLABORATION_STYLE_NAME);
        }
            break;
        case DbInitialization.UML_COMPONENT_DIAGRAM: {
            style = (DbSMSStyle) project.findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_COMPONENT_STYLE_NAME);
        }
            break;
        case DbInitialization.UML_DEPLOYMENT_DIAGRAM: {
            style = (DbSMSStyle) project.findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_DEPLOYMENT_STYLE_NAME);
        }
            break;
        case DbInitialization.UML_SEQUENCE_DIAGRAM: {
            style = (DbSMSStyle) project.findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_SEQUENCE_STYLE_NAME);
        }
            break;
        case DbInitialization.UML_STATE_DIAGRAM: {
            style = (DbSMSStyle) project.findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_STATE_STYLE_NAME);
        }
            break;
        default: {
            style = (DbSMSStyle) project.findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.DEFAULT_STYLE_NAME);
        }
            break;
        }
        return style;
    }
}
