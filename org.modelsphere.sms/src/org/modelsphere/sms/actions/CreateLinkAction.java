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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.util.AnySemObject;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.screen.SMSLinkFrameExt;

public final class CreateLinkAction extends AbstractApplicationAction implements
        SelectionActionListener {

    public CreateLinkAction() {
        super(LocaleMgr.action.getString("createLink") + "...", LocaleMgr.action
                .getImageIcon("createLink"));
        setEnabled(false);
        setDefaultToolBarVisibility(false);
        setVisible(ScreenPerspective.isFullVersion());
    }

    protected final void doActionPerformed() {
        List<DbObject> selection = getInterDiagramSelection();
        int length = selection.size();
        DefaultMainFrame dmf = ApplicationContext.getDefaultMainFrame();

        //Link only one source and one target
        if (length != 2) {
            JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(),
                    LocaleMgr.action.getString("Exactly2ObjectsMessage"),
                    ApplicationContext.getApplicationName(), JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        //TODO Linked elements must be in the same project 
        DbObject dbo1 = selection.get(0);
        DbObject dbo2 = selection.get(1);
        DbSMSProject project = (DbSMSProject) dbo1.getProject();

        try {
            project.getDb().beginWriteTrans(LocaleMgr.action.getString("newLinkModel"));
            List<DbSMSLinkModel> list = AnySemObject.getAllDbSMSLinkModel(project);
            if (list == null || list.size() == 0)
                createDefaultLinkModel(dbo1, dbo2);

            project.getDb().commitTrans();

            dbo1.getDb().beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("createLink"));
            SMSLinkFrameExt linkFrame = new SMSLinkFrameExt(dmf, selection, true);
            linkFrame.setLocationRelativeTo(dmf);
            linkFrame.setVisible(true);

            if (linkFrame.cancel == false) {
                DbSMSSemanticalObject src = (DbSMSSemanticalObject) linkFrame.sourceObject;
                DbSMSSemanticalObject dest = (DbSMSSemanticalObject) linkFrame.targetObject;
                createLink(linkFrame.linkModel, src, dest);
            }

            dbo1.getDb().commitTrans();
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        } //end try
    }

    private DbSMSLink createLink(DbSMSLinkModel linkModel, DbSMSSemanticalObject src,
            DbSMSSemanticalObject dest) throws DbException {
        DbSMSLink link = new DbSMSLink(linkModel);
        link.addToSourceObjects(src);
        link.addToTargetObjects(dest);

        DbSMSSemanticalObject composite = (DbSMSSemanticalObject) linkModel
                .getCompositeOfType(DbSMSSemanticalObject.metaClass);
        String compositeName = (composite == null) ? "" : composite.getName();
        DbRelationN relN = linkModel.getComponents();
        int noLink = relN.size();

        String name = src.getName() + " -> " + dest.getName(); //NOT LOCALIZABLE
        String alias = Integer.toString(noLink);

        link.setName(name);
        link.setAlias(alias);

        return link;
    }

    protected final void doActionPerformedNew() {
        List<DbObject> selection = getInterDiagramSelection();
        int length = selection.size();
        DefaultMainFrame dmf = ApplicationContext.getDefaultMainFrame();

        //Link only two elements
        if (length != 2) {
            String msg = LocaleMgr.action.getString("Exactly2ObjectsMessage");
            JOptionPane.showMessageDialog(dmf, msg, ApplicationContext.getApplicationName(),
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        //Linked elements must be in the same project
        DbObject dbo1 = selection.get(0);
        DbObject dbo2 = selection.get(1);
        DbSMSProject project1 = (DbSMSProject) dbo1.getProject();
        DbSMSProject project2 = (DbSMSProject) dbo2.getProject();
        if (!project1.equals(project2)) {
            String msg = "Must be in the same project"; //TODO localise
            JOptionPane.showMessageDialog(dmf, msg, ApplicationContext.getApplicationName(),
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        //Show dialog
        SMSLinkFrameExt linkFrame = new SMSLinkFrameExt(dmf, selection, true);
        linkFrame.setLocationRelativeTo(dmf);
        linkFrame.setVisible(true);

        //for next version
        //CreateLinkDialog dialog = new CreateLinkDialog(dmf);
        //dialog.setVisible(true); 

    } //end doActionPerformed()

    //create the appropriate link model 
    private void createDefaultLinkModel(DbObject dbo1, DbObject dbo2) throws DbException {
        DbSMSAbstractPackage pack1 = (DbSMSAbstractPackage) dbo1
                .getCompositeOfType(DbSMSAbstractPackage.metaClass);
        DbSMSAbstractPackage pack2 = (DbSMSAbstractPackage) dbo2
                .getCompositeOfType(DbSMSAbstractPackage.metaClass);
        boolean commonPackage = ((pack1 != null) && (pack2 != null) && (pack1.equals(pack2)));

        if (commonPackage) {
            DbSMSLinkModel model = new DbSMSLinkModel(pack1);
            String defaultName = model.getName();
            model.setName(defaultName);
        } else {
            DbSMSProject project = (DbSMSProject) dbo1.getProject();
            new DbSMSLinkModel(project);
        }
    } //end createDefaultLinkModel()

    private List<DbObject> getInterDiagramSelection() {
        List<DbObject> selection = new ArrayList<DbObject>();
        DefaultMainFrame mf = ApplicationContext.getDefaultMainFrame();
        JInternalFrame[] frames = mf.getDiagramInternalFrames();
        for (JInternalFrame frame : frames) {
            if (frame instanceof DiagramInternalFrame) {
                DiagramInternalFrame df = (DiagramInternalFrame) frame;
                ApplicationDiagram d = df.getDiagram();
                Object[] selectedObjects = d.getSelectedObjects();
                for (Object o : selectedObjects) {
                    if (o instanceof DbObject) {
                        selection.add((DbObject) o);
                    }
                }
            }
        } //end for

        return selection;
    } //end getSelection

    public final void updateSelectionAction() throws DbException {
        setEnabled(false);
        int length = ApplicationContext.getFocusManager().getSelectedSemanticalObjects().length;
        if (length == 1) {
            DbObject[] objects = ApplicationContext.getFocusManager()
                    .getSelectedSemanticalObjects();
            if (AnySemObject.supportsLinks(objects[0].getMetaClass()))
                setEnabled(true);
            return;
        }
        if (length != 2) {
            return;
        }

        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        if (ApplicationContext.getFocusManager().getCurrentProject() == null)
            return;
        if (!AnySemObject.supportsLinks(objects[0].getMetaClass())
                || !AnySemObject.supportsLinks(objects[1].getMetaClass()))
            return;
        setEnabled(true);
    }

    //
    // private class (future extension)
    //
    /*
     * private static class CreateLinkDialog extends JDialog { private static final boolean MODAL =
     * true;
     * 
     * public CreateLinkDialog(JFrame parent) { super(parent, "Semantic Link Creation", MODAL);
     * SMSLinkFrameExt2 panel = new SMSLinkFrameExt2(); panel.initContents();
     * this.setContentPane(panel); }
     * 
     * @Override public void setVisible(boolean visible) { super.pack(); AwtUtil.centerWindow(this);
     * super.setVisible(visible); } }
     */
}
