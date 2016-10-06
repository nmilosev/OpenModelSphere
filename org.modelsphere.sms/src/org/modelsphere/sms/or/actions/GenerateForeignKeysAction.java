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
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.util.ForeignKey;
import org.modelsphere.sms.or.db.util.GenForeignKeyDialog;
import org.modelsphere.sms.or.international.LocaleMgr;

@SuppressWarnings("serial")
final class GenerateForeignKeysAction extends AbstractApplicationAction implements
        SelectionActionListener {

    //private static final String kGenForeignKeys = LocaleMgr.action.getString("genForeignKeys");
    private static final String kGenForeignKeysDots = LocaleMgr.action
            .getString("genForeignKeysDots");

    private TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    GenerateForeignKeysAction() {
        super(kGenForeignKeysDots);
        this.setMnemonic(LocaleMgr.action.getMnemonic("genForeignKeysDots"));
        setEnabled(true);
        setVisible(ScreenPerspective.isFullVersion());
    }

    public final void updateSelectionAction() throws DbException {
        DbObject[] activeObjs = ORActionFactory.getActiveObjects();

        boolean enabled = false;

        if (activeObjs != null) {
            enabled = true;

            if (activeObjs[0] instanceof DbORDataModel) {
                DbORDataModel dataModel = (DbORDataModel) activeObjs[0];
                if (terminologyUtil.getModelLogicalMode(dataModel) == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                    enabled = false;
            } else {
                DbObject dataModel = terminologyUtil.isCompositeDataModel(activeObjs[0]);
                if (dataModel == null)
                    enabled = false;
                else if (terminologyUtil.getModelLogicalMode(dataModel) == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                    enabled = false;
            }
        }
        setEnabled(enabled);
    } // end updateSelectionAction()

    protected final void doActionPerformed() {
        try {
            DbORDataModel dataModel;
            DbObject[] activeObjs = ORActionFactory.getActiveObjects();
            activeObjs[0].getDb().beginReadTrans();
            if (activeObjs[0] instanceof DbORDataModel) {
                dataModel = ((DbORDataModel) activeObjs[0]);
            } else {
                dataModel = (DbORDataModel) activeObjs[0]
                        .getCompositeOfType(DbORDataModel.metaClass);
            }
            activeObjs[0].getDb().commitTrans();

            if (dataModel == null)
                return;

            // Dialog
            dataModel.getDb().beginReadTrans();
            String dataModelName = dataModel.getName();
            dataModel.getDb().commitTrans();
            String title = GenForeignKeyDialog.buildTitle(dataModelName);
            GenForeignKeyDialog genFKOptionDialog = new GenForeignKeyDialog(ApplicationContext
                    .getDefaultMainFrame(), title, true, ForeignKey.copyFlagsList, dataModel);
            genFKOptionDialog.setVisible(true);

            if (genFKOptionDialog.getButtonSelect() == ForeignKey.GENERATE) {
                // Generation
                ForeignKey genFKey = new ForeignKey();
                genFKey.generate(dataModel, title, genFKOptionDialog.getSelectedFlags(),
                        genFKOptionDialog.getOrphanOptionSelect(), genFKOptionDialog
                                .isReorderPuCols(),
                        (genFKOptionDialog.getButtonSelect() == ForeignKey.REPORTONLY));
            } // end if
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        } // end try
    } // end doActionPerformed()

    protected int getFeatureSet() {
        return SMSFilter.RELATIONAL;
    }
} // GenerateForeignKeysAction

