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

package org.modelsphere.sms.oo.actions;

import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.srtypes.OOAggregation;
import org.modelsphere.sms.oo.international.LocaleMgr;

public final class SetAggregationAction extends AbstractDomainAction implements
        SelectionActionListener {

    public static final String kAggregation = LocaleMgr.action.getString("aggregation");

    SetAggregationAction() {
        super(kAggregation);
        setDomainValues(OOAggregation.stringPossibleValues);
    }

    protected final void doActionPerformed() {
        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        OOAggregation selValue = OOAggregation.objectPossibleValues[getSelectedIndex()];
        try {
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, objects, LocaleMgr.action
                    .getString("setAggregation"));
            for (int i = 0; i < objects.length; i++) {
                ((DbOODataMember) objects[i]).getAssociationEnd().setAggregation(selValue);
            }
            DbMultiTrans.commitTrans(objects);
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    public final void updateSelectionAction() throws DbException {
        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        OOAggregation selValue = null;
        for (int i = 0; i < objects.length; i++) {
            OOAggregation value = ((DbOODataMember) objects[i]).getAssociationEnd()
                    .getAggregation();
            if (selValue == null)
                selValue = value;
            else if (!selValue.equals(value)) {
                selValue = null;
                break;
            }
        }
        setSelectedIndex(selValue != null ? selValue.indexOf() : -1);
    }

    protected int getFeatureSet() {
        return SMSFilter.JAVA;
    }
}
