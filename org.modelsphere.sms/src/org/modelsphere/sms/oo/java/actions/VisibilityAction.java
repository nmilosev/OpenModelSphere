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

import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.oo.db.srtypes.OOVisibility;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVConstructor;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;
import org.modelsphere.sms.oo.java.international.LocaleMgr;

public class VisibilityAction extends AbstractDomainAction implements SelectionActionListener {

    public static final String kVisibility = LocaleMgr.action.getString("visibility");
    public static final String kVisibilityUpdate = LocaleMgr.action.getString("VisibilityUpdate");

    VisibilityAction() {
        super(kVisibility);
        setDomainValues(JVVisibility.stringPossibleValues);
        setEnabled(false);
        setVisible(ScreenPerspective.isFullVersion());
    }

    protected final void doActionPerformed() {
        int selvalue = getSelectedIndex();
        JVVisibility value = JVVisibility.objectPossibleValues[selvalue];
        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        try {
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, objects, kVisibilityUpdate);
            for (int i = 0; i < objects.length; i++) {
                setObjectValue(objects[i], value);
            }
            DbMultiTrans.commitTrans(objects);
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    public final void updateSelectionAction() throws DbException {
        OOVisibility value = null;
        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        boolean validobjfound = false;
        for (int i = 0; i < objects.length; i++) {
            Object obj = getObjectValue(objects[i]);
            OOVisibility objvalue = (OOVisibility) obj;
            if (objvalue == null) // invalid DbObject for visibility
                continue;
            validobjfound = true;
            if (!objvalue.equals(value)) {
                if (value == null) {
                    value = objvalue;
                    continue;
                } else {
                    value = null;
                    break;
                }
            }
        }

        setEnabled(validobjfound);
        if (value != null)
            setSelectedIndex(value.indexOf());
        else
            setSelectedIndex(-1);
    }

    private final Object getObjectValue(DbObject semObj) throws DbException {
        MetaField field = getMetaField(semObj);
        return (field != null ? semObj.get(field) : null);
    }

    private final void setObjectValue(DbObject semObj, Object value) throws DbException {
        MetaField field = getMetaField(semObj);
        if (field != null)
            semObj.set(field, value);
    }

    private MetaField getMetaField(DbObject semObj) {
        MetaField field = null;
        if (semObj instanceof DbJVClass)
            field = DbJVClass.fVisibility;
        else if (semObj instanceof DbJVDataMember)
            field = DbJVDataMember.fVisibility;
        else if (semObj instanceof DbJVMethod)
            field = DbJVMethod.fVisibility;
        else if (semObj instanceof DbJVConstructor)
            field = DbJVConstructor.fVisibility;
        return field;
    }

    protected int getFeatureSet() {
        return SMSFilter.JAVA;
    }
}
