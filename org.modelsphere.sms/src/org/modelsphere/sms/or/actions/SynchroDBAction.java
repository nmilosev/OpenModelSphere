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

package org.modelsphere.sms.or.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.gui.wizard.Wizard;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORModel;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.features.dbms.DBMSDefaultSynchroWizardModel;
import org.modelsphere.sms.or.features.dbms.DBMSReverseOptions;
import org.modelsphere.sms.or.features.dbms.ReverseToolkitPlugin;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.plugins.TargetSystem;

public class SynchroDBAction extends AbstractApplicationAction implements SelectionActionListener {
    private static final String kSynchroDB = LocaleMgr.action.getString("Synchronization_");
    private static final String kProceed = LocaleMgr.screen.getString("Proceed");
    private static final String SYNCHRO_NOT_SUPPORTED = LocaleMgr.message
            .getString("SynchroNotSupported");

    SynchroDBAction() {
        super(kSynchroDB);
        this.setMnemonic(LocaleMgr.action.getMnemonic("Synchronization_"));
        setEnabled(false);
    }

    public void updateSelectionAction() throws DbException {
        //Only one object selected
        DbObject[] selObjs = ORActionFactory.getActiveObjects();
        if (selObjs == null || selObjs.length > 1 || selObjs.length == 0) {
            setEnabled(false);
            return;
        }

        DbORDatabase db = null;
        if (selObjs[0] instanceof DbORDatabase) {
            db = (DbORDatabase) selObjs[0];
        } else {
            DbORModel model = null;
            if (selObjs[0] instanceof DbORModel) {
                model = (DbORModel) selObjs[0];
            } else {
                selObjs[0].getDb().beginReadTrans();
                model = (DbORModel) selObjs[0].getCompositeOfType(DbORModel.metaClass);
                selObjs[0].getDb().commitTrans();
            }

            if (model != null) {
                if (model instanceof DbORDataModel) {
                    db = ((DbORDataModel) model).getDeploymentDatabase();
                } else if (model instanceof DbORDomainModel) {
                    db = ((DbORDomainModel) model).getDeploymentDatabase();
                } else if (model instanceof DbOROperationLibrary) {
                    db = ((DbOROperationLibrary) model).getDeploymentDatabase();
                }
            }
        } //end if

        //Enable if connected to a database (doActionPerfomed() will generate an error if database does not support synchro)
        setEnabled(db != null);
    } //end updateSelectionAction()

    protected final void doActionPerformed(ActionEvent e) {
        doActionPerformed();
    }

    protected final void doActionPerformed() {
        DbObject[] selObjs = ORActionFactory.getActiveObjects();
        DBMSDefaultSynchroWizardModel model = new DBMSDefaultSynchroWizardModel(selObjs);
        DBMSReverseOptions options = model.getOptions();
        boolean synchroSupported = ReverseToolkitPlugin.getToolkitForTargetSystem(
                options.targetSystemId).isSynchroSupported();
        if (synchroSupported) {
            Wizard wizard = new Wizard(model, kProceed);
            wizard.setVisible(true);
        } else {
            DbProject project = selObjs[0].getProject();
            String msg;

            try {
                project.getDb().beginReadTrans();
                DbSMSTargetSystem ts = TargetSystem.getSpecificTargetSystem(project,
                        options.targetSystemId);
                msg = MessageFormat.format(SYNCHRO_NOT_SUPPORTED, new Object[] { ts.getName(),
                        ts.getVersion() });
                project.getDb().commitTrans();
            } catch (DbException ex) {
                msg = ex.toString();
            } //end try

            //Display an error message
            MainFrame parent = MainFrame.getSingleton();
            JOptionPane.showMessageDialog(parent, msg);
        } //end if

    } //end doActionPerformed()

    protected int getFeatureSet() {
        return SMSFilter.RELATIONAL;
    }

}
