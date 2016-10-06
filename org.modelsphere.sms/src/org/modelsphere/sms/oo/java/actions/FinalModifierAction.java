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
package org.modelsphere.sms.oo.java.actions;

import org.modelsphere.jack.actions.AbstractTriStatesAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVParameter;
import org.modelsphere.sms.oo.java.international.LocaleMgr;

public class FinalModifierAction extends AbstractTriStatesAction implements SelectionActionListener {

    FinalModifierAction() {
        super(LocaleMgr.action.getString("Final"), LocaleMgr.action.getImageIcon("Final"));
        setVisible(ScreenPerspective.isFullVersion());
    }

    protected final void doActionPerformed() {
        performTriState(ApplicationContext.getFocusManager().getSelectedSemanticalObjects(),
                LocaleMgr.action.getString("FinalModifierUpdate"));
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
        if (semObj instanceof DbJVClass)
            field = DbJVClass.fFinal;
        else if (semObj instanceof DbJVDataMember)
            field = DbJVDataMember.fFinal;
        else if (semObj instanceof DbJVMethod)
            field = DbJVMethod.fFinal;
        else if (semObj instanceof DbJVParameter)
            field = DbJVParameter.fFinal;
        return field;
    }

    protected int getFeatureSet() {
        return SMSFilter.JAVA;
    }
}
