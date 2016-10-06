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

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.international.LocaleMgr;

public final class DeleteOrphanCommonItemsAction extends AbstractApplicationAction implements
        SelectionActionListener {

    private static final String DELETE_ORPHAN_ITEMS = LocaleMgr.action
            .getString("DeleteOrphanCommonItems");

    public DeleteOrphanCommonItemsAction() {
        super(DELETE_ORPHAN_ITEMS);
        setEnabled(true);
    }

    protected final void doActionPerformed() {
        try {
            DbObject[] semObjs = ApplicationContext.getFocusManager()
                    .getSelectedSemanticalObjects();
            semObjs[0].getDb().beginWriteTrans(DELETE_ORPHAN_ITEMS);
            int nb = semObjs.length;
            for (int i = 0; i < nb; i++) {
                DbObject semObj = semObjs[i];
                if (semObj instanceof DbORCommonItemModel) {
                    DbORCommonItemModel model = (DbORCommonItemModel) semObj;
                    deleteOrphanCommonItems(model);
                } else if (semObj instanceof DbORCommonItem) {
                    DbORCommonItem item = (DbORCommonItem) semObj;
                    deleteOrphanCommonItems(item);
                } // end if
            } // end for
            semObjs[0].getDb().commitTrans();

        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    public final void updateSelectionAction() throws DbException {
        DbObject[] semObjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        // setEnabled((semObjs[0] instanceof DbORDataModel || semObjs[0]
        // instanceof DbORTable || semObjs[0] instanceof DbORColumn));
    }

    protected int getFeatureSet() {
        return SMSFilter.RELATIONAL;
    }

    //
    // PRIVATE METHODS
    //
    private void deleteOrphanCommonItems(DbORCommonItemModel model) throws DbException {
        DbRelationN relN = model.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORCommonItem.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORCommonItem item = (DbORCommonItem) dbEnum.nextElement();
            deleteOrphanCommonItems(item);
        } // end while
        dbEnum.close();
    } // end propagateCommonItemValues()

    private void deleteOrphanCommonItems(DbORCommonItem item) throws DbException {
        boolean doDelete = true;

        DbRelationN relN = item.getColumns();
        DbEnumeration dbEnum = relN.elements(DbORColumn.metaClass);
        if (dbEnum.hasMoreElements()) {
            doDelete = false;
        } // end if
        dbEnum.close();

        relN = item.getFields();
        dbEnum = relN.elements(DbJVDataMember.metaClass);
        if (dbEnum.hasMoreElements()) {
            doDelete = false;
        } // end if
        dbEnum.close();

        if (doDelete) {
            item.remove();
        }
    } // end propagateCommonItemValues()

} // end GenerateCommonItemsAction

