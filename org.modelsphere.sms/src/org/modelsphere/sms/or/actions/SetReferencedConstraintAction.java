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

import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.international.LocaleMgr;

public final class SetReferencedConstraintAction extends AbstractDomainAction implements
        SelectionActionListener {

    private static final String kReferencedConstraint = LocaleMgr.action.getString("refConstraint");
    private static final String kSetReferencedConstraint = LocaleMgr.action
            .getString("setRefConstraint");
    private static final String kNoReferencedConstraint = LocaleMgr.action
            .getString("noRefConstraint");
    private static final String kKeyNamePatern = LocaleMgr.action.getString("keyName01");

    private ArrayList dbos = new ArrayList(0);

    SetReferencedConstraintAction() {
        super(kReferencedConstraint);
        setEnabled(false);
        setVisible(ScreenPerspective.isFullVersion());
    }

    protected final void doActionPerformed() {
        int selidx = getSelectedIndex();
        DbORPrimaryUnique key = null;
        if (selidx != 0) {
            key = (DbORPrimaryUnique) dbos.get(selidx - 2);
        }
        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        if (objects.length != 1)
            return;
        try {
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, objects, kSetReferencedConstraint);
            for (int i = 0; i < objects.length; i++) {
                ((DbORAssociationEnd) objects[i]).setReferencedConstraint(key);
            }
            DbMultiTrans.commitTrans(objects);
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    // this update is always safe
    public final void updateSelectionAction() throws DbException {
        ApplicationDiagram diag = ApplicationContext.getFocusManager().getActiveDiagram();
        Object[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        if ((diag == null) || (objects.length != 1) || !(objects[0] instanceof DbORAssociationEnd)) {
            setSelectedIndex(-1);
            setEnabled(false);
            return;
        }

        // update values
        dbos.clear();

        DbORAssociationEnd end = (DbORAssociationEnd) objects[0];
        DbORPrimaryUnique selKey = end.getReferencedConstraint();

        DbORAbsTable tableSo = (DbORAbsTable) end.getOppositeEnd().getClassifier();
        DbEnumeration dbEnum = tableSo.getComponents().elements(DbORPrimaryUnique.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORPrimaryUnique key = (DbORPrimaryUnique) dbEnum.nextElement();
            dbos.add(key);
        }
        dbEnum.close();

        int sizeadjustment = (dbos.size() == 0) ? 1 : 2; // adding a separator
        // and an item for
        // removing the
        // referenced
        // constraint
        // if no keys, do not add a separator
        Object[] items = new Object[dbos.size() + sizeadjustment];

        items[0] = kNoReferencedConstraint;
        if (sizeadjustment == 2) {
            items[1] = null;
        }

        String tableName = tableSo.getName();
        for (int i = 0; i < dbos.size(); i++) {
            String name = ((DbORPrimaryUnique) dbos.get(i)).getName();
            items[i + sizeadjustment] = MessageFormat.format(kKeyNamePatern, new Object[] {
                    tableName, name });
        }
        setDomainValues(items);

        if (selKey == null)
            setSelectedIndex(0);
        else
            setSelectedIndex(dbos.indexOf(selKey) + sizeadjustment);

        setEnabled(true);
    }
}
