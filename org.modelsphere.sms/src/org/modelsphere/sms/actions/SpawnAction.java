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
package org.modelsphere.sms.actions;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.sms.or.actions.ORActionFactory;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SpawnAction extends AbstractApplicationAction {
    private static final String SPAWN = "Spawn"; // LocaleMgr.action.getString("About")

    public SpawnAction() {
        super(SPAWN);
        // this.setMnemonic(LocaleMgr.action.getMnemonic("About"));
    } // end SpawnAction

    protected final void doActionPerformed() {
        DbObject[] semObjs = ORActionFactory.getActiveObjects();
        if (semObjs == null)
            return;

        try {
            if (semObjs[0] instanceof DbSemanticalObject) {
                DbSemanticalObject so = (DbSemanticalObject) semObjs[0];
                so.getDb().beginReadTrans();
                String desc = so.getDescription();
                so.getDb().commitTrans();

                Runtime runtime = Runtime.getRuntime();
                Process proc = runtime.exec(desc);
            }

        } catch (Exception ex) {

        }
    } // end doActionPerformed()

} // end SpawnAction
