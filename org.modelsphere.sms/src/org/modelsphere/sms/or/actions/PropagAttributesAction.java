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

import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.util.ForeignKey;
import org.modelsphere.sms.or.international.LocaleMgr;

final class PropagAttributesAction extends AbstractDomainAction implements SelectionActionListener {

    private static final String kPropagToForeign = LocaleMgr.action.getString("propagToForeign");

    private TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    PropagAttributesAction() {
        super(kPropagToForeign, false);
        String[] values = new String[ForeignKey.copyFlagsList.length];
        for (int i = 0; i < ForeignKey.copyFlagsList.length; i++)
            values[i] = ForeignKey.getAttrLabel(ForeignKey.copyFlagsList[i]);
        setDomainValues(values);
        setVisible(ScreenPerspective.isFullVersion());
    }

    protected final void doActionPerformed() {
        int copyFlags = ForeignKey.copyFlagsList[getSelectedIndex()];
        DbObject[] semObjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        try {
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, semObjs, kPropagToForeign);
            for (int i = 0; i < semObjs.length; i++)
                ForeignKey.propagAttributes((DbORColumn) semObjs[i], copyFlags);
            DbMultiTrans.commitTrans(semObjs);
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    public final void updateSelectionAction() throws DbException {
        DbObject[] semObjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();

        boolean bEnable = false;
        DbORDataModel datamodel = (DbORDataModel) semObjs[0]
                .getCompositeOfType(DbORDataModel.metaClass);
        if (datamodel != null)
            if (terminologyUtil.getModelLogicalMode(datamodel) != DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                if (semObjs.length == 1 && semObjs[0] instanceof DbORColumn)
                    bEnable = true;

        boolean state = false;
        for (int i = 0; i < semObjs.length; i++) {
            if (((DbORColumn) semObjs[i]).getDestFKeyColumns().size() != 0) {
                state = true;
                break;
            }
        }

        setEnabled(bEnable && state);
    }
}
