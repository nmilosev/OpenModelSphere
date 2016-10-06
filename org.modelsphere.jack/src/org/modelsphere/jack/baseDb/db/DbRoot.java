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

import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;

/**
 * The ultimate container of all model elements. Root objects are managed by their corresponding Db
 * instance.
 * 
 * @see org.modelsphere.jack.baseDb.db.Db#getRoot()
 */
public final class DbRoot extends DbObject {

    static final long serialVersionUID = -7717910379514949485L;

    int version;

    public final static MetaClass metaClass = new MetaClass("DbRoot", DbRoot.class, // NOT LOCALIZABLE Class name
            new MetaField[0], MetaClass.NO_UDF);

    public static void initMeta() {
        metaClass.setSuperMetaClass(DbObject.metaClass);
        metaClass.setComponentMetaClasses(new MetaClass[] { DbProject.metaClass,
                DbLoginNode.metaClass });
    }

    public DbRoot() {
    }

    DbRoot(Db db, int version) {
        super(db);
        this.version = version;
    }

    final void initTransientFields(Db db) {
        if (this.db == null) {
            this.db = db;
            this.project = null;
            this.transStatus = Db.OBJ_UNTOUCHED;
        }
    }

    public final int getVersion() throws DbException {
        db.fetch(this);
        return version;
    }

    public final void setVersion(int version) throws DbException {
        db.dirty(this);
        this.version = version;
    }

    public final MetaClass getMetaClass() {
        return metaClass;
    }
}
