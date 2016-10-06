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

import org.modelsphere.jack.baseDb.db.srtypes.DbtLoginList;
import org.modelsphere.jack.baseDb.international.LocaleMgr;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;

/**
 * The container of model elements. DbProject and all its components are persistent in a .sms file.
 * DbProject are themselves components of DbRoot, which allows ModelSphere to open several projects
 * at the same time in the same session.
 * 
 */
public abstract class DbProject extends DbSemanticalObject {

    static final long serialVersionUID = 6464396599068739173L;
    private transient String ramFileName = null;
    private transient int lastSaveTrans = 0;

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbProject"),
            DbProject.class, new MetaField[0], 0);

    public static void initMeta() {
        metaClass.setSuperMetaClass(DbSemanticalObject.metaClass);
        metaClass.setComponentMetaClasses(new MetaClass[] { DbUDF.metaClass });
    }

    public DbProject() {
    }

    public abstract boolean isIsLocked() throws DbException;

    public DbProject(DbObject composite) throws DbException {
        super(composite);
        setName(LocaleMgr.db.getString("project"));
        if (!(db instanceof DbRAM))
            basicSet(DbSemanticalObject.fAdminAccessList, new DbtLoginList(new DbLogin[] { db
                    .getLogin() }));
    }

    public final boolean isDeletable() throws DbException {
        return !(getDb() instanceof DbRAM);
    }

    public final String getRamFileName() {
        return ramFileName;
    }

    public final void setRamFileName(String newRamFileName) {
        ramFileName = newRamFileName;
    }

    public final int getLastSaveTrans() {
        return lastSaveTrans;
    }

    public final void setLastSaveTrans(int lastSaveTrans) {
        this.lastSaveTrans = lastSaveTrans;
    }

    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }
}
