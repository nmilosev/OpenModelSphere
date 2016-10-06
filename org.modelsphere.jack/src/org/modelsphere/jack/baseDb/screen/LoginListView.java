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

import javax.swing.JButton;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.DbtPassword;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.screen.model.DbListModel;
import org.modelsphere.jack.baseDb.screen.model.LoginListModel;
import org.modelsphere.jack.international.LocaleMgr;

public class LoginListView extends DbListView {

    public LoginListView(ScreenContext screenContext, DbLoginNode loginNode, MetaClass listClass)
            throws DbException {
        super(screenContext, loginNode, DbObject.fComponents, listClass, ADD_BTN | DELETE_BTN
                | PROPERTIES_BTN | APPLY_ACTION | SORTED_LIST
                | (listClass == DbLoginUser.metaClass ? EXTRA_BTN : 0));
    }

    public LoginListView(ScreenContext screenContext, DbLogin login, MetaRelationN listRelation,
            MetaClass listClass) throws DbException {
        super(screenContext, login, listRelation, listClass, LINK_BTN | UNLINK_BTN | SORTED_LIST);
    }

    protected final DbListModel createListModel() throws DbException {
        return new LoginListModel(this, semObj, listRelations, listClass);
    }

    protected final void initPropertiesButton(JButton button) {
        if (listClass == DbLoginGroup.metaClass)
            LocaleMgr.screen.initAbstractButton(button, "Members"); // NOT LOCALIZABLE
        else
            LocaleMgr.screen.initAbstractButton(button, "Groups"); // NOT LOCALIZABLE
    }

    protected final void initExtraButton(JButton button) {
        LocaleMgr.screen.initAbstractButton(button, "RemovePassword"); // NOT LOCALIZABLE
    }

    protected final void initLinkButton(JButton button) {
        LocaleMgr.screen.initAbstractButton(button, "Add"); // NOT LOCALIZABLE
    }

    protected final void initUnlinkButton(JButton button) {
        LocaleMgr.screen.initAbstractButton(button, "Remove"); // NOT LOCALIZABLE
    }

    public final void extraAction() {
        try {
            DbListModel model = (DbListModel) getModel();
            int[] selRows = getSelectedRows();
            semObj.getDb().beginWriteTrans("");
            for (int i = 0; i < selRows.length; i++) {
                DbLoginUser login = (DbLoginUser) model.getParentValue(selRows[i]);
                login.set(DbLoginUser.fPassword, new DbtPassword(""));
            }
            semObj.getDb().commitTrans();
        } catch (DbException ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(this, ex);
        }
    }

    protected final DbEnumeration getLinkableEnum() throws DbException {
        return semObj.getComposite().getComponents().elements(listClass);
    }

    protected final String getLinkableObjName(DbObject dbo) throws DbException {
        if (listRelations[0] == DbLoginGroup.fMembers) {
            if (dbo instanceof DbLoginGroup && isCircular((DbLoginGroup) dbo))
                return null;
        }
        return super.getLinkableObjName(dbo);
    }

    private boolean isCircular(DbLoginGroup group) throws DbException {
        if (group == semObj)
            return true;
        DbRelationN members = group.getMembers();
        for (int i = 0; i < members.size(); i++) {
            DbObject member = members.elementAt(i);
            if (member instanceof DbLoginGroup && isCircular((DbLoginGroup) member))
                return true;
        }
        return false;
    }
}
