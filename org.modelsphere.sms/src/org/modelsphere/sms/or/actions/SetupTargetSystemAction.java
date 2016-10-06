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

import java.beans.PropertyVetoException;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRAM;
import org.modelsphere.jack.baseDb.screen.PropertiesFrame;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.CurrentProjectEvent;
import org.modelsphere.jack.srtool.CurrentProjectListener;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.SrScreenContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.screen.SMSListView;
import org.modelsphere.sms.screen.SMSPropertiesFrame;

public final class SetupTargetSystemAction extends AbstractApplicationAction implements
        CurrentProjectListener, SelectionActionListener {
    private static final long serialVersionUID = 1L;

    public SetupTargetSystemAction() {
        super(LocaleMgr.action.getString("setupTarget"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("setupTarget"));
        setEnabled(false);
        setVisible(ScreenPerspective.isFullVersion());
        ApplicationContext.getFocusManager().addCurrentProjectListener(this);
    }

    public final void updateSelectionAction() throws DbException {
        DbObject[] activeObjs = ORActionFactory.getActiveObjects();
        boolean enabled = false;

        if (activeObjs != null) {
            TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

            if (activeObjs[0] instanceof DbORDataModel) {
                enabled = true;
                DbORDataModel dataModel = (DbORDataModel) activeObjs[0];
                if (terminologyUtil.getModelLogicalMode(dataModel) == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                    enabled = false;
            } else {
                DbObject dataModel = terminologyUtil.isCompositeDataModel(activeObjs[0]);
                enabled = true;

                if (dataModel == null)
                    enabled = false;
                else if (terminologyUtil.getModelLogicalMode(dataModel) == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                    enabled = false;
            }
        }
        setEnabled(enabled);
    }

    protected final void doActionPerformed() {
        DbSMSProject project = (DbSMSProject) ApplicationContext.getFocusManager()
                .getCurrentProject();
        DefaultMainFrame mf = ApplicationContext.getDefaultMainFrame();
        try {
            PropertiesFrame frame = mf.getPropertyInternalFrame(project,
                    SMSPropertiesFrame.TYPE_TARGET_SYSTEM);
            if (frame == null) {
                project.getDb().beginTrans(Db.READ_TRANS);
                String name = "{0}"; // placeholder for project name
                // (automatically refreeshed when it
                // changes)
                if (project.getDb() instanceof DbRAM && project.getRamFileName() != null)
                    name = StringUtil.getFileName(project.getRamFileName()); // fixed
                // (not
                // refreshed)
                // RAM
                // file
                // name
                String titlePattern = MessageFormat.format(LocaleMgr.screen
                        .getString("0TargetSystem"), new Object[] { name });
                frame = new PropertiesFrame(SrScreenContext.singleton, project,
                        new ScreenView[] { new SMSListView(project, DbObject.fComponents,
                                DbSMSTargetSystem.metaClass, ScreenView.ADD_BTN
                                        | ScreenView.DELETE_BTN | ScreenView.APPLY_ACTION
                                        | ScreenView.REINSERT_ACTION) }, titlePattern);
                project.getDb().commitTrans();

                frame.setType(SMSPropertiesFrame.TYPE_TARGET_SYSTEM);
                mf.getJDesktopPane().add(frame, DefaultMainFrame.PROPERTY_LAYER);
                frame.setVisible(true);
            }
            try {
                frame.setIcon(false);
                frame.setSelected(true);
            } catch (PropertyVetoException e) {
            }
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(mf, e);
        }
    }

    public final void currentProjectChanged(CurrentProjectEvent cpe) throws DbException {
        setEnabled(cpe.getProject() != null);
    }

    protected int getFeatureSet() {
        return SMSFilter.RELATIONAL;
    }

}
