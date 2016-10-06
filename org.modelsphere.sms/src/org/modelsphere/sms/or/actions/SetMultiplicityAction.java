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

import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.international.LocaleMgr;

public final class SetMultiplicityAction extends AbstractDomainAction implements
        SelectionActionListener {

    public static final String kMultiplicity = LocaleMgr.action.getString("multiplicity");
    public static final String kSetMultiplicity = LocaleMgr.action.getString("setMultiplicity");

    SetMultiplicityAction() {
        super(kMultiplicity);
        setDomainValues(SMSMultiplicity.stringPossibleValues);
        setVisible(ScreenPerspective.isFullVersion());
    }

    protected final void doActionPerformed() {
        int selvalue = getSelectedIndex();
        SMSMultiplicity value = SMSMultiplicity.objectPossibleValues[selvalue];
        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        try {
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, objects, kSetMultiplicity);
            for (int i = 0; i < objects.length; i++) {
                ((DbORAssociationEnd) objects[i]).setMultiplicity(value);
            }
            DbMultiTrans.commitTrans(objects);
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    // This update supposed that the selection is composed uniquely of
    // LineLabels
    public final void updateSelectionAction() throws DbException {
        SMSMultiplicity multi = SMSMultiplicity.getInstance(0);
        String[] strings = multi.getUIStringPossibleValues();
        setDomainValues(strings);

        SMSMultiplicity value = null;
        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        for (int i = 0; i < objects.length; i++) {
            SMSMultiplicity objvalue = ((DbORAssociationEnd) objects[i]).getMultiplicity();
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

        if (value != null)
            setSelectedIndex(value.indexOf());
        else
            setSelectedIndex(-1);
    }
}
