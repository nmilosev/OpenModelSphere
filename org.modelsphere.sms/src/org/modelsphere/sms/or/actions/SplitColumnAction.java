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
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.util.ForeignKey;
import org.modelsphere.sms.or.international.LocaleMgr;

@SuppressWarnings("serial")
final class SplitColumnAction extends AbstractApplicationAction implements SelectionActionListener {

    private static final String kSplitColumn = LocaleMgr.action.getString("splitColumn");

    SplitColumnAction() {
        super(kSplitColumn);
        setVisible(ScreenPerspective.isFullVersion());
    }

    protected final void doActionPerformed() {
        try {
            DbObject[] semObjs = ApplicationContext.getFocusManager()
                    .getSelectedSemanticalObjects();
            semObjs[0].getDb().beginTrans(Db.WRITE_TRANS, kSplitColumn);
            ForeignKey.splitMergedColumn((DbORColumn) semObjs[0]);
            semObjs[0].getDb().commitTrans();
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    public final void updateSelectionAction() throws DbException {
        DbObject[] semObjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        boolean state = (semObjs.length == 1 && semObjs[0] instanceof DbORColumn && semObjs[0]
                .getNbNeighbors(DbORColumn.fFKeyColumns) > 1);
        setEnabled(state);
    }
}
