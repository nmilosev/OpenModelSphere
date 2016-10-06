/*************************************************************************

Copyright (C) 2010 Grandite

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

package org.modelsphere.jack.actions.util;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;

public abstract class DbMultiTrans {
    public static final void beginTrans(int transactionStatement, Object[] objects,
            String transactionName) throws DbException {
        int i;
        Db db;

        for (i = 0; i < objects.length; i++) {
            if (objects[i] instanceof DbObject) {
                db = ((DbObject) objects[i]).getDb();
            } else if (objects[i] instanceof ActionInformation) {
                ActionInformation ai = (ActionInformation) objects[i];
                db = ai.getDb();
            } else {
                // explorer database node: RAM or Repository.
                continue;
            }

            db.beginTrans(transactionStatement, transactionName);
        } //end for
    } //end beginTrans()

    public static final void commitTrans(Object[] objects) throws DbException {
        int i;
        Db db;

        for (i = objects.length - 1; i >= 0; i--) {
            if (objects[i] instanceof DbObject)
                db = ((DbObject) objects[i]).getDb();
            else if (objects[i] instanceof ActionInformation)
                db = ((ActionInformation) objects[i]).getDb();
            else
                // explorer database node: RAM or Repository.
                continue;
            db.commitTrans();
        }
    }
}
