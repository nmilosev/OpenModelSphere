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

import java.util.ArrayList;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.screen.DbLookupDialog;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.util.ForeignKey;
import org.modelsphere.sms.or.international.LocaleMgr;

final class MergeColumnAction extends AbstractApplicationAction implements SelectionActionListener {

    private static final String kMergeColumn = LocaleMgr.action.getString("mergeColumn");
    private static final String kMergeColumnDots = LocaleMgr.action.getString("mergeColumnDots");
    private static final String kSelectTargetCol = LocaleMgr.screen.getString("selectTargetCol");

    MergeColumnAction() {
        super(kMergeColumnDots);
        setVisible(ScreenPerspective.isFullVersion());
    }

    // Two columns can be merged if they have the same type, or if they are both
    // foreign,
    // if they have the same ultimate source.
    protected final void doActionPerformed() {
        try {
            DbObject[] semObjs = ApplicationContext.getFocusManager()
                    .getSelectedSemanticalObjects();
            DbORColumn mergeCol = (DbORColumn) semObjs[0];
            Db db = mergeCol.getDb();
            db.beginTrans(Db.READ_TRANS);
            DbORTypeClassifier type = mergeCol.getType();
            DbORColumn source = (mergeCol.getFKeyColumns().size() != 0 ? ForeignKey
                    .getUltimateSource(mergeCol) : null);
            ArrayList cols = new ArrayList();
            DbEnumeration dbEnum = mergeCol.getComposite().getComponents().elements(
                    DbORColumn.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORColumn col = (DbORColumn) dbEnum.nextElement();
                if (col == mergeCol)
                    continue;
                if (mergeCol.getFKeyColumns().size() != 0 && col.getFKeyColumns().size() != 0 ? source == ForeignKey
                        .getUltimateSource(col)
                        : type == col.getType()) {
                    cols.add(col);
                }
            }
            dbEnum.close();

            // DbLookupDialog closes the transaction.
            DefaultComparableElement targetCol = DbLookupDialog.selectDbo(ApplicationContext
                    .getDefaultMainFrame(), kSelectTargetCol, null, db, cols, null, null, false);
            if (targetCol == null)
                return;
            db.beginTrans(Db.WRITE_TRANS, kMergeColumn);
            ForeignKey.mergeColumns(mergeCol, (DbORColumn) targetCol.object);
            db.commitTrans();
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    public final void updateSelectionAction() throws DbException {
        DbObject[] semObjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        setEnabled(semObjs.length == 1 && semObjs[0] instanceof DbORColumn);
    }
}
