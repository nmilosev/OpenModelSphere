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

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORModel;
import org.modelsphere.sms.or.features.ChangingTargetSystemWizard;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.plugins.TargetSystem;
import org.modelsphere.sms.plugins.TargetSystemManager;

public final class ChangeTargetSystemAction extends AbstractApplicationAction implements
        SelectionActionListener {

    private static final String CHANGE_TARGET_SYSTEM = LocaleMgr.action
            .getString("ChangeTargetSystem");

    public ChangeTargetSystemAction() {
        super(CHANGE_TARGET_SYSTEM + "...");
        setEnabled(true);
        setVisible(ScreenPerspective.isFullVersion());
    }

    protected final void doActionPerformed() {
        try {
            DbObject[] semObjs = ApplicationContext.getFocusManager()
                    .getSelectedSemanticalObjects();
            DefaultMainFrame mainframe = ApplicationContext.getDefaultMainFrame();
            DbProject proj = semObjs[0].getProject();
            proj.getDb().beginWriteTrans(CHANGE_TARGET_SYSTEM);
            ChangingTargetSystemWizard wizard = new ChangingTargetSystemWizard(mainframe,
                    CHANGE_TARGET_SYSTEM, true, (DbORModel) semObjs[0]);
            AwtUtil.centerWindow(wizard);
            wizard.setVisible(true);
            proj.getDb().commitTrans();

        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    } // end doActionPerformed()

    public final void updateSelectionAction() throws DbException {
        DbObject[] activeObjs = ORActionFactory.getActiveObjects();
        boolean enabled = false;

        if ((activeObjs != null) && (activeObjs.length == 1)) {
            if (activeObjs[0] instanceof DbORModel) {
                if (activeObjs[0] instanceof DbORDataModel) {
                    int mode = ((DbORDataModel) activeObjs[0]).getLogicalMode().intValue();
                    if (mode == DbORDataModel.LOGICAL_MODE_OBJECT_RELATIONAL)
                        enabled = true;
                } else {
                    enabled = true;
                }
            }
        }
        setEnabled(enabled);
    }// end updateSelectionAction()

    protected int getFeatureSet() {
        return SMSFilter.RELATIONAL;
    }

    //
    // PRIVATE METHODS
    //
    private DbSMSTargetSystem selectDestinationTargetSystem(DbProject project) throws DbException {
        TargetSystem tsMgr = TargetSystemManager.getSingleton();
        DbSMSTargetSystem ts = TargetSystem.getSpecificTargetSystem(project,
                TargetSystem.SGBD_ORACLE90);
        if (ts == null) {
            ts = tsMgr.createTargetSystem(project, TargetSystem.SGBD_ORACLE90);
        } // end if

        return ts;
    } // end selectDestinationTargetSystem()

    //
    // PART 2 : PERFORM CONVERSION
    //
} // end ChangeTargetSystemAction

