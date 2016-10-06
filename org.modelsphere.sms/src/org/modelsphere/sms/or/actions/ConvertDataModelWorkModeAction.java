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

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.features.ConvertDataModelWorkModeDialog;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.plugins.TargetSystem;

final class ConvertDataModelWorkModeAction extends AbstractApplicationAction implements
        SelectionActionListener {

    private static final String kConvertDataModelWorkMode = LocaleMgr.action
            .getString("convertDataModelWorkMode");
    private static final String kConvertDataModelWorkModeDots = LocaleMgr.action
            .getString("convertDataModelWorkModeDots");
    private static final String kConvertToConceptual = LocaleMgr.action
            .getString("convertToConceptualDots");
    private static final String kConvertToRelationnel = LocaleMgr.action
            .getString("convertToRelationnelDots");
    private static final String kConvertToConceptualTitle = LocaleMgr.screen
            .getString("convertToConceptual");
    private static final String kConvertToRelationnelTitle = LocaleMgr.screen
            .getString("convertToRelationnel");
    private static final String kConvertOnlyLogicalModel = LocaleMgr.message
            .getString("convertOnlyLogicalModel");
    private TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    private int m_currentWorkMode = 0;

    ConvertDataModelWorkModeAction() {
        super(kConvertDataModelWorkModeDots);
        this.setMnemonic(LocaleMgr.action.getMnemonic("convertDataModelWorkModeDots"));
        setEnabled(true);
        setVisible(ScreenPerspective.isFullVersion());
    }

    public final void updateSelectionAction() throws DbException {
        DbObject[] activeObjs = ORActionFactory.getActiveObjects();
        boolean enabled = false;
        // int mode = 0;

        if ((activeObjs != null) && (activeObjs.length == 1)) {
            if (activeObjs[0] instanceof DbORDataModel) {
                setVisible(ScreenPerspective.isFullVersion());
                m_currentWorkMode = terminologyUtil.getModelLogicalMode(activeObjs[0]);
                enabled = true;
            } else if (activeObjs[0] instanceof DbORDiagram) {
                setVisible(ScreenPerspective.isFullVersion());
                activeObjs[0].getDb().beginReadTrans();
                DbObject model = ((DbORDiagram) activeObjs[0]).getComposite();
                activeObjs[0].getDb().commitTrans();
                if (model instanceof DbORDataModel) {
                    m_currentWorkMode = terminologyUtil.getModelLogicalMode(model);
                    enabled = true;
                }
            } else
                setVisible(false);
        }
        if (m_currentWorkMode != 0) {
            this
                    .setName(m_currentWorkMode == DbORDataModel.LOGICAL_MODE_OBJECT_RELATIONAL ? kConvertToConceptual
                            : kConvertToRelationnel);
        } else {
            this.setName(kConvertDataModelWorkModeDots);
        }

        setEnabled(enabled);
    } // end updateSelectionAction()

    protected final void doActionPerformed() {
        String title = null;
        try {
            // DbObject[] semObjs =
            // ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
            MainFrame frame = MainFrame.getSingleton();
            DbObject model = null;
            int targetID = 0;
            DbObject[] activeObjs = ORActionFactory.getActiveObjects();
            activeObjs[0].getDb().beginReadTrans();
            if (activeObjs[0] instanceof DbORDiagram) {
                model = ((DbORDiagram) activeObjs[0]).getComposite();
            } else
                model = activeObjs[0];

            DbSMSTargetSystem targetSys = ((DbORDataModel) model).getTargetSystem();
            targetID = targetSys.getID().intValue();
            activeObjs[0].getDb().commitTrans();

            if (targetID == TargetSystem.SGBD_LOGICAL) {
                title = m_currentWorkMode == DbORDataModel.LOGICAL_MODE_OBJECT_RELATIONAL ? kConvertToConceptualTitle
                        : kConvertToRelationnelTitle;
                JDialog dialog = new ConvertDataModelWorkModeDialog(frame, title, true, activeObjs,
                        m_currentWorkMode);
                AwtUtil.centerWindow(dialog);
                dialog.pack();
                dialog.setVisible(ScreenPerspective.isFullVersion());
            } else {
                JOptionPane.showMessageDialog(frame, kConvertOnlyLogicalModel,
                        kConvertToConceptualTitle, JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        } // end try

    } // end doActionPerformed()

    protected int getFeatureSet() {
        return SMSFilter.RELATIONAL;
    }
}
