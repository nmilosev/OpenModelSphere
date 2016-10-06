/*************************************************************************

Copyright (C) 2009 Grandite

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

You can redistribute and/or modify this particular file even under the
terms of the GNU Lesser General Public License (LGPL) as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License 
(LGPL) along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.jack.baseDb.screen;

import javax.swing.JOptionPane;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.model.DbListModel;
import org.modelsphere.jack.baseDb.screen.model.UDFListModel;
import org.modelsphere.jack.international.LocaleMgr;

public class UDFListView extends DbListView {

    public UDFListView(ScreenContext screenContext, DbProject project) throws DbException {
        super(screenContext, project, DbObject.fComponents, DbUDF.metaClass, ADD_DEL_ACTIONS
                | REINSERT_ACTION);
    }

    protected final DbListModel createListModel() throws DbException {
        return new UDFListModel(this, (DbProject) semObj);
    }

    protected final void createComponent() throws DbException {
        DbListModel model = (DbListModel) getModel();
        DbListModel.Partition part = (DbListModel.Partition) model.getPartitions()[model
                .getPartitionIndex()];
        new DbUDF((DbProject) semObj, (MetaClass) part.partId);
    }

    public final void deleteAction() {
        try {
            DbListModel model = (DbListModel) getModel();
            int[] selRows = getSelectedRows();
            boolean used = false;
            semObj.getDb().beginTrans(Db.READ_TRANS);
            for (int i = 0; i < selRows.length; i++) {
                DbUDF udf = (DbUDF) model.getParentValue(selRows[i]);
                if (udf.getNbNeighbors(DbObject.fComponents) != 0)
                    used = true;
            }
            semObj.getDb().commitTrans();

            if (used) {
                if (JOptionPane.showConfirmDialog(this, LocaleMgr.message
                        .getString(selRows.length == 1 ? "deleteUDF" : "deleteUDFs"), "",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
                    return;
            }
            super.deleteAction();
        } catch (DbException ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(this, ex);
        }
    }
}
