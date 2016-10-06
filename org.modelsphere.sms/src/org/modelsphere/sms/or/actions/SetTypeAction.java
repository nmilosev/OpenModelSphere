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

import java.awt.Component;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.baseDb.screen.DbLookupDialog;
import org.modelsphere.jack.baseDb.screen.DefaultRenderer;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.util.AnyORObject;

@SuppressWarnings("serial")
public final class SetTypeAction extends AbstractApplicationAction implements
        SelectionActionListener {

    private static final String kSetType = LocaleMgr.action.getString("setType");
    private static final String kSetTypeDots = LocaleMgr.action.getString("setType...");
    private static final String kSelectNewType = LocaleMgr.screen.getString("SelectNewType");

    SetTypeAction() {
        super(kSetTypeDots);
        setVisible(ScreenPerspective.isFullVersion());
    }
    

    protected final void doActionPerformed() {
        setType(ApplicationContext.getDefaultMainFrame(), ApplicationContext.getFocusManager()
                .getSelectedSemanticalObjects(), null);
    }

    // Check that all objects in the selection are typeable, and belong to the
    // same package.
    public final void updateSelectionAction() throws DbException {
        DbObject[] semObjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        boolean state = (semObjs.length > 0);
        if (state) {
            DbSMSPackage pack = (DbSMSPackage) semObjs[0]
                    .getCompositeOfType(DbSMSPackage.metaClass);
            for (int i = 0; i < semObjs.length; i++) {
                MetaRelationship field = AnyORObject.getTypeField(semObjs[i]);
                if (field == null || field instanceof MetaRelationN
                        || pack != semObjs[i].getCompositeOfType(DbSMSPackage.metaClass)) {
                    state = false;
                    break;
                }
            }
        }
        setEnabled(state);
    }

    // All objects must be typeable, and belong to the same package.
    public static void setType(Component comp, DbObject[] dbos, Point center) {
        try {
            Db db = dbos[0].getDb();
            db.beginTrans(Db.READ_TRANS);
            DbObject currentType = (DbObject) dbos[0].get(AnyORObject.getTypeField(dbos[0]));
            List<DbObject> types = new ArrayList<DbObject>();
            if (dbos[0] instanceof DbORCommonItem)
                AnyORObject.getAllTypes(types, (DbSMSProject) dbos[0].getProject());
            else
                AnyORObject.getAllTypes(types, AnyORObject.getTargetSystem(dbos[0]));
            // <selectDbo> takes care of closing the transaction.
            DefaultComparableElement item = DbLookupDialog.selectDbo(comp, kSelectNewType, center,
                    db, types, currentType, DefaultRenderer.kUnspecified, true);
            if (item == null)
                return;
            db.beginTrans(Db.WRITE_TRANS, kSetType);
            for (int i = 0; i < dbos.length; i++) {
                dbos[i].set(AnyORObject.getTypeField(dbos[i]), item.object);
            }
            db.commitTrans();
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(comp, e);
        }
    }
}
