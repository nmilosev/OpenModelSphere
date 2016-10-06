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

package org.modelsphere.jack.baseDb.db;

import org.modelsphere.jack.baseDb.meta.MetaField;

/**
 * Provides services on DB that usually require package-level access.
 */
public class DbServices {

    // IMPLEMENTS Singleton Pattern
    private static DbServices g_singleInstance = null;

    public static DbServices getSingleton() {
        if (g_singleInstance == null) {
            g_singleInstance = new DbServices();
        }
        return g_singleInstance;
    }

    private DbServices() {
    }

    //
    // Public methods
    //

    // Set the object's db (this field has a package visibility)
    public void setDbOfObject(DbObject object, Db db) {
        object.db = db;
    } // end setDbOfObject()

    // Set the object's transaction status (this field has a package visibility)
    public void setTransStatus(DbObject obj, int status) {
        obj.setTransStatus(status);
    } // end setTransStatus()

    // Set the object's value (this field has a package visibility)
    public void setValueOfObject(DbObject obj, MetaField mf, Object value) throws DbException {
        obj.basicSet(mf, value);
    } // end setValueOfObject()

    // Get the object's value (this field has a package visibility)
    public Object getValueOfObject(DbObject obj, MetaField mf) throws DbException {
        return obj.basicGet(mf);
    } // end getValueOfObject()

    public void setProject(DbObject obj, DbProject project) throws DbException {
        obj.project = (obj instanceof DbProject ? (DbProject) obj : obj.getComposite().getProject());
    } // end setProject()

} // end DbServices
