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

import java.awt.event.ActionEvent;
import java.util.Enumeration;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.explorer.ExplorerView;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.jack.util.SrVector;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.features.SaveDiagramDialog;
import org.modelsphere.sms.international.LocaleMgr;

public class SaveDiagramAction extends AbstractApplicationAction implements SelectionActionListener {

    // +
    public static final String kAddMissingGraphics = LocaleMgr.action
            .getString("addMissingGraphics");

    public SaveDiagramAction() {
        super(LocaleMgr.action.getString("SaveDiagram"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("SaveDiagram"));
        this.setEnabled(false);
    }

    protected final void doActionPerformed(ActionEvent e) {
        doActionPerformed();
    }

    protected final void doActionPerformed() {

        Object focusObject = ApplicationContext.getFocusManager().getFocusObject();

        if (focusObject instanceof ExplorerView) {

            MainFrame mainFrame = MainFrame.getSingleton();
            DiagramInternalFrame diagramInternalFrame;
            DbObject[] dbObjects;

            dbObjects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
            // for each selected node in the explorer ...
            for (int i = 0; i < dbObjects.length; i++) {

                diagramInternalFrame = mainFrame.getDiagramInternalFrame(dbObjects[i]);
                if (diagramInternalFrame == null) {
                    /*
                     * ApplicationDiagram appDiagram =
                     * createApplicationDiagramFromDbObject(dbObjects[i]); showDialog(appDiagram,
                     * true);
                     */
                    showDialog((DbSMSDiagram) dbObjects[i]);
                } else {
                    showDialog(diagramInternalFrame.getDiagram(), false);
                }
            }
        } else if (focusObject instanceof ApplicationDiagram) {
            showDialog((ApplicationDiagram) focusObject, false);
        }
    }

    private void showDialog(ApplicationDiagram appDiagram, boolean hasToBeDeleted) {
        try {
            SaveDiagramDialog dialog = new SaveDiagramDialog(appDiagram, MainFrame.getSingleton(),
                    true, hasToBeDeleted);
            AwtUtil.centerWindow(dialog);
            dialog.setVisible(true);
        } catch (DbException ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }

    private void showDialog(DbSMSDiagram diagram) {
        try {
            SaveDiagramDialog dialog = new SaveDiagramDialog(diagram, MainFrame.getSingleton(),
                    true);
            AwtUtil.centerWindow(dialog);
            dialog.setVisible(true);
        } catch (DbException ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }

    // disable item (in popup) when an object is selected but is not a diagram
    // or when a diagram is selected and is empty
    public void updateSelectionAction() throws DbException {
        SrVector selectedObjects = getValidObjects();
        boolean enabled = true;

        if (selectedObjects.size() == 0) {
            enabled = false;
        } else {
            // setEnabled(true);
            Enumeration enumeration = selectedObjects.elements();
            while (enumeration.hasMoreElements()) {
                Object element = enumeration.nextElement();
                if (element instanceof ApplicationDiagram) {
                    ApplicationDiagram appDiagram = (ApplicationDiagram) element;
                    if (appDiagram.getContentRect() == null)
                        enabled = false;
                } else if (element instanceof DbSMSDiagram) {
                    DbSMSDiagram diagram = (DbSMSDiagram) element;
                    if (diagram.getComponents().size() == 0)
                        enabled = false;
                }
            }
        }

        setEnabled(enabled);
    }

    private SrVector getValidObjects() {
        SrVector srVector = new SrVector();
        Object focusObject = ApplicationContext.getFocusManager().getFocusObject();

        if (focusObject instanceof ApplicationDiagram)
            srVector.add(focusObject);
        else {
            Object[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
            for (int i = 0; i < objects.length; i++)
                if (objects[i] instanceof DbSMSDiagram)
                    srVector.add(objects[i]);
        }

        return srVector;
    }

    // DbOject being a DbSMSDiagram
    private ApplicationDiagram createApplicationDiagramFromDbObject(DbObject diag) {
        ApplicationDiagram appDiagram = null;

        try {
            Db db = diag.getDb();
            db.beginTrans(Db.READ_TRANS);
            DbSemanticalObject so = (DbSemanticalObject) diag.getComposite();
            SMSToolkit kit = SMSToolkit.getToolkit(so);

            appDiagram = new ApplicationDiagram(so, diag, kit.createGraphicalComponentFactory(),
                    ApplicationContext.getDefaultMainFrame().getDiagramsToolGroup());
            db.commitTrans();
        } catch (DbException e) {
        }

        return appDiagram;
    }
}
