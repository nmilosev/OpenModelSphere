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

import org.modelsphere.jack.actions.AbstractTriStatesAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.international.LocaleMgr;

public class NullModifierAction extends AbstractTriStatesAction implements SelectionActionListener,
        DbRefreshListener {

    NullModifierAction() {
        super(LocaleMgr.action.getString("Null"), LocaleMgr.action.getImageIcon("Null"));
        DbORColumn.fNull.addDbRefreshListener(this);
        DbORCommonItem.fNull.addDbRefreshListener(this);
        DbORDomain.fNull.addDbRefreshListener(this);
        setVisible(ScreenPerspective.isFullVersion());
    }

    protected final void doActionPerformed() {
        performTriState(ApplicationContext.getFocusManager().getSelectedSemanticalObjects(),
                LocaleMgr.action.getString("NullModifierUpdate"));
    }

    public final void updateSelectionAction() throws DbException {
        updateTriState(ApplicationContext.getFocusManager().getSelectedSemanticalObjects());
    }

    protected final Boolean getObjectValue(Object obj) throws DbException {
        DbObject semObj = (DbObject) obj;
        MetaField field = getMetaField(semObj);
        return (field != null ? (Boolean) semObj.get(field) : null);
    }

    protected final void setObjectValue(Object obj, Boolean value) throws DbException {
        DbObject semObj = (DbObject) obj;
        MetaField field = getMetaField(semObj);
        if (field != null)
            semObj.set(field, value);
    }

    private MetaField getMetaField(DbObject semObj) {
        MetaField field = null;
        if (semObj instanceof DbORColumn)
            field = DbORColumn.fNull;
        else if (semObj instanceof DbORCommonItem)
            field = DbORCommonItem.fNull;
        else if (semObj instanceof DbORDomain)
            field = DbORDomain.fNull;
        return field;
    }

    public void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        updateSelectionAction();
    }

    protected int getFeatureSet() {
        return SMSFilter.RELATIONAL;
    }
}
