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

import java.awt.Component;

import javax.swing.JOptionPane;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.gui.wizard.Wizard;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORModel;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.or.features.dbms.DBMSDefaultForwardWizardModel;
import org.modelsphere.sms.or.features.dbms.ForwardToolkitPlugin;
import org.modelsphere.sms.or.international.LocaleMgr;

public class ForwardDataModelAction extends AbstractApplicationAction implements
        SelectionActionListener {
    private static final String kForwardDataModel = LocaleMgr.action.getString("ForwardEngineer");
    private static final String kPattern = LocaleMgr.message
            .getString("NoForwardEnginneringForTargetSystem");
    private static final String kUnknown = LocaleMgr.message.getString("Unknown");
    private static final String kError = LocaleMgr.message.getString("Error");
    private static final String kWarning = LocaleMgr.message.getString("Warning");
    private static final String kNoActiveDatabase = LocaleMgr.message.getString("NoActiveDatabase");
    private static final String kCancel = LocaleMgr.screen.getString("Cancel");
    private static final String kCreation = LocaleMgr.message.getString("0Creation");
    private static final String kCreateDeploymentDB = LocaleMgr.message
            .getString("CreateADeploymentDatabase");

    ForwardDataModelAction() {
        super(kForwardDataModel);
        this.setMnemonic(LocaleMgr.action.getMnemonic("ForwardEngineer"));
        setEnabled(false);
        ApplicationContext.getFocusManager().addSelectionListener(this);
    }

    public final void updateSelectionAction() throws DbException {

        //Disable action not exactly one object is selected
        DbObject[] selObjs = ORActionFactory.getActiveObjects();
        if (selObjs == null || selObjs.length > 1 || selObjs.length == 0) {
            setEnabled(false);
            return;
        }

        //Get db
        boolean forwardEnabled = true;
        DbORDatabase db = null;
        if (selObjs[0] instanceof DbORDatabase) {
            db = (DbORDatabase) selObjs[0];
        } else {
            selObjs[0].getDb().beginReadTrans();
            DbORModel model = (selObjs[0] instanceof DbORModel) ? (DbORModel) selObjs[0]
                    : (DbORModel) selObjs[0].getCompositeOfType(DbORModel.metaClass);
            selObjs[0].getDb().commitTrans();
            if (model == null) {
                forwardEnabled = false;
            } else {
                model.getDb().beginReadTrans();
                if (model instanceof DbORDataModel) {
                    db = ((DbORDataModel) model).getDeploymentDatabase();
                } else if (model instanceof DbORDomainModel) {
                    db = ((DbORDomainModel) model).getDeploymentDatabase();
                } else if (model instanceof DbOROperationLibrary) {
                    db = ((DbOROperationLibrary) model).getDeploymentDatabase();
                } else {
                    forwardEnabled = false;
                } //end if
                model.getDb().commitTrans();
            } //end if
        } //end if

        if (db == null) {
            setEnabled(forwardEnabled); //let it enable to allow doActionPerformed() to inform user of why the operation cannot be archieved
        } else {
            db.getDb().beginReadTrans();
            ForwardToolkitPlugin.setActiveDiagramTarget(db.getTargetSystem().getID().intValue());
            forwardEnabled = ForwardToolkitPlugin.getToolkit().forwardEnabled();
            setEnabled(forwardEnabled);
            db.getDb().commitTrans();
        } //end if

        //Disable for ER module
        if (true == forwardEnabled) {
            TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
            Object obj = ApplicationContext.getFocusManager().getFocusObject();
            if (obj instanceof ApplicationDiagram) {
                if (obj != null) {
                    DbObject semObj = ((ApplicationDiagram) obj).getSemanticalObject();
                    if (semObj != null) {
                        if (semObj instanceof DbORDataModel) {
                            DbORDataModel model = (DbORDataModel) semObj;
                            model.getDb().beginReadTrans();
                            if (terminologyUtil.getModelLogicalMode(model) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                                setEnabled(false);
                            model.getDb().commitTrans();
                        }
                    } else
                        setEnabled(false);
                }
            } else if (terminologyUtil.validateSelectionModel() == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                setEnabled(false);
        }
    } //end updateSelectionAction()

    protected final void doActionPerformed(java.awt.event.ActionEvent e) {
        doActionPerformed();
    }

    protected final void doActionPerformed() {
        DbObject[] selObjs = ORActionFactory.getActiveObjects();
        if (selObjs == null || selObjs.length > 1 || selObjs.length == 0) {
            throw new RuntimeException("Expecting selection of one database");
        }
        boolean userHasCancelled = false;
        DbORDatabase database = null;
        try {
            selObjs[0].getDb().beginTrans(Db.READ_TRANS);
            DbORModel model = null;

            if (selObjs[0] instanceof DbORDatabase) {
                database = (DbORDatabase) selObjs[0];
            } else {
                model = (selObjs[0] instanceof DbORModel) ? (DbORModel) selObjs[0]
                        : (DbORModel) selObjs[0].getCompositeOfType(DbORModel.metaClass);
                if (model != null) {
                    if (model instanceof DbORDataModel) {
                        database = ((DbORDataModel) model).getDeploymentDatabase();
                    } else if (model instanceof DbORDomainModel) {
                        database = ((DbORDomainModel) model).getDeploymentDatabase();
                    } else if (model instanceof DbOROperationLibrary) {
                        database = ((DbOROperationLibrary) model).getDeploymentDatabase();
                    } //end if
                } //end if
            } //end if
            selObjs[0].getDb().commitTrans();

            if ((model != null) && (database == null)) {
                String transName = MessageFormat.format(kCreation,
                        new Object[] { DbORDataModel.fDeploymentDatabase.getGUIName() });
                selObjs[0].getDb().beginWriteTrans(transName);
                int option = JOptionPane.showConfirmDialog(
                        ApplicationContext.getDefaultMainFrame(), kNoActiveDatabase,
                        ApplicationContext.getApplicationName(), JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE);
                if (option == 0) {
                    DbObject composite = model.getProject();
                    DbSMSTargetSystem ts = model.getTargetSystem();
                    database = AnyORObject.createDatabase(composite, ts);
                    if (model instanceof DbORDataModel) {
                        DbORDataModel dm = (DbORDataModel) model;
                        dm.setDeploymentDatabase(database);
                    } else if (model instanceof DbOROperationLibrary) {
                        DbOROperationLibrary ol = (DbOROperationLibrary) model;
                        ol.setDeploymentDatabase(database);
                    } else if (model instanceof DbORDomainModel) {
                        DbORDomainModel dm = (DbORDomainModel) model;
                        dm.setDeploymentDatabase(database);
                    } //end if
                } else if (option == 1) {
                    userHasCancelled = true;
                } //end if

                selObjs[0].getDb().commitTrans();
            } //end if
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        } //end try

        if (userHasCancelled) {
            return;
        }

        DbSMSTargetSystem target = null;
        try {
            Db db = database.getDb();
            db.beginTrans(Db.READ_TRANS);
            target = database.getTargetSystem();
            DBMSDefaultForwardWizardModel model = new DBMSDefaultForwardWizardModel(target.getID()
                    .intValue());
            model.setAbstractPackage(database);
            model.setDatabaseName(database.getName());
            db.commitTrans();
            Wizard wizard = new Wizard(model);
            wizard.setVisible(true);
        } catch (InstantiationException e) {
            Component parent = ApplicationContext.getDefaultMainFrame();
            Db.abortAllTrans();
            ExceptionHandler.showErrorMessage(parent, e.getMessage());
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        } catch (RuntimeException e) {
            Component parent = ApplicationContext.getDefaultMainFrame();
            String pattern = kPattern;
            String targetName = kUnknown;

            try {
                Db db = target.getDb();
                db.beginReadTrans();
                targetName = target.getName() + " " + target.getVersion();
                db.commitTrans();
            } catch (DbException ex) {
                //leave targetName's value to unknown
            }

            String msg = MessageFormat.format(pattern, new Object[] { targetName });
            Db.abortAllTrans();
            ExceptionHandler.showErrorMessage(parent, msg);
        }
    }
} //end ForwardDataModelAction

