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

import java.text.MessageFormat;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.integrate.IntegrateFrame;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.SMSIntegrateModel;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.features.IntegrateEntryDialog;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.or.actions.ORActionFactory;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORModel;

final class IntegrateAction extends AbstractApplicationAction implements SelectionActionListener {
    private static final String kIntegrateDots = LocaleMgr.action.getString("integrateDots");
    private static final String kDialogPattn = LocaleMgr.screen.getString("Integrating0");

    IntegrateAction() {
        super(kIntegrateDots);
        this.setMnemonic(LocaleMgr.action.getMnemonic("integrateDots"));
        this.setVisible(ScreenPerspective.isFullVersion());
    }

    public final int getFeatureSet() {
        return SMSFilter.JAVA | SMSFilter.RELATIONAL | SMSFilter.BPM;
    }

    public final void updateSelectionAction() throws DbException {
        DbObject[] semObjs = ORActionFactory.getActiveObjects();
        boolean state = false;

        if (semObjs != null) {
            if (semObjs.length == 1) {
                if (semObjs[0] instanceof DbORModel || semObjs[0] instanceof DbORCommonItemModel
                        || semObjs[0] instanceof DbORDiagram
                        || semObjs[0] instanceof DbOOAbsPackage
                        || semObjs[0] instanceof DbOODiagram || semObjs[0] instanceof DbBEModel
                        || semObjs[0] instanceof DbBEDiagram) {
                    state = true;
                }
            }
        } // end if

        setEnabled(state);
    } // end updateSelectionAction()

    //
    // Protected and private methods
    // 

    protected final void doActionPerformed() {
        DbObject[] semObjs = ORActionFactory.getActiveObjects();

        try {
            semObjs[0].getDb().beginReadTrans();
            DbObject obj = semObjs[0];
            if (obj instanceof DbBEDiagram) {
                obj = (DbBEModel) semObjs[0].getCompositeOfType(DbBEModel.metaClass);
            } else if (obj instanceof DbOODiagram) {
                obj = (DbOOAbsPackage) semObjs[0].getCompositeOfType(DbOOAbsPackage.metaClass);
            } else if (obj instanceof DbORDiagram) {
                obj = (DbORModel) semObjs[0].getCompositeOfType(DbORModel.metaClass);
                if (obj == null) {
                    obj = (DbORCommonItemModel) semObjs[0]
                            .getCompositeOfType(DbORCommonItemModel.metaClass);
                }
            } // end if
            semObjs[0].getDb().commitTrans();

            if (obj == null) {
                return;
            }

            obj.getDb().beginReadTrans();
            String title = MessageFormat.format(kDialogPattn, new Object[] { obj.getName() });
            obj.getDb().commitTrans();
            IntegrateEntryDialog EntryDlg = new IntegrateEntryDialog(obj, title);
            EntryDlg.setVisible(true);

            if (!EntryDlg.isCanceled()) {
                Db.beginMatching();
                DbObject semObj2 = EntryDlg.getSecondSemObj();

                obj.getDb().beginTrans(Db.READ_TRANS);
                semObj2.getDb().beginTrans(Db.READ_TRANS);
                SMSIntegrateModel model = new SMSIntegrateModel(obj, semObj2, EntryDlg
                        .getFieldTree(), EntryDlg.isExternalMatchByName(), EntryDlg
                        .isPhysNameSelected(), false, true);
                semObj2.getDb().commitTrans();
                obj.getDb().commitTrans();

                IntegrateFrame frame = new IntegrateFrame(model);
                frame.setVisible(true);

                Db.endMatching();
            }
        } catch (Exception e) {
            Db.endMatching();
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        } // end try
    } // end doActionPerformed()
} // end IntegrateAction

