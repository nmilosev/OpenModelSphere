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

package org.modelsphere.sms.be.actions;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.be.db.DbBEActorGo;
import org.modelsphere.sms.be.db.DbBEStoreGo;
import org.modelsphere.sms.be.db.DbBEUseCaseGo;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.DbSMSClassifierGo;

public final class RepositionQualifiersAction extends AbstractApplicationAction {

    public static final String kRepositionQualifiers = LocaleMgr.action
            .getString("repositionQualifiers");
    public static final String kRepositionQualifiersAction = LocaleMgr.action
            .getString("repositionQualifiersAction");

    RepositionQualifiersAction() {
        super(kRepositionQualifiers);
    }

    protected final void doActionPerformed() {
        Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
        try {
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, objects, kRepositionQualifiersAction);
            for (int i = 0; i < objects.length; i++) {
                DbSMSClassifierGo go = (DbSMSClassifierGo) ((ActionInformation) objects[i])
                        .getGraphicalObject();
                if (go instanceof DbBEUseCaseGo)
                    ((DbBEUseCaseGo) go).setQualifiersOffset(null);
                else if (go instanceof DbBEActorGo)
                    ((DbBEActorGo) go).setQualifiersOffset(null);
                else if (go instanceof DbBEStoreGo)
                    ((DbBEStoreGo) go).setQualifiersOffset(null);
            }
            DbMultiTrans.commitTrans(objects);
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

}
