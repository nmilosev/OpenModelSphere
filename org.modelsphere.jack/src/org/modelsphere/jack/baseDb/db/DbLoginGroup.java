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

import org.modelsphere.jack.baseDb.international.LocaleMgr;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;

/**
 * Define a group of DbLogin (DbLogin can be user or group). Internal to the Db framework. A
 * DbLoginGroup is an aggregation of DbLogin (a login may belong to several groups), while a
 * DbLoginNode is a composition of DbLoginUser.
 * 
 * Only an ADMIN user can add, remove or modify DbLogin objects.
 */
public final class DbLoginGroup extends DbLogin {

    static final long serialVersionUID = 0;

    public static final MetaRelationN fMembers = new MetaRelationN(LocaleMgr.db
            .getString("members"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbLoginGroup"),
            DbLoginGroup.class, new MetaField[] { fMembers }, MetaClass.NO_UDF);

    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbLogin.metaClass);

            fMembers.setJField(DbLoginGroup.class.getDeclaredField("m_members")); // NOT
            // LOCALIZABLE
            // field
            // name
            fMembers.setOppositeRel(DbLogin.fGroups);
        } catch (Exception e) {
            throw new RuntimeException("Meta init"); // NOT LOCALIZABLE
            // RuntimeException
        }
    }

    DbRelationN m_members;

    public DbLoginGroup() {
    }

    public DbLoginGroup(DbObject composite) throws DbException {
        super(composite);
        basicSet(DbLogin.fName, LocaleMgr.db.getString("group"));
    }

    // We provide no individual set methods for this class; use the generic
    // <set>.
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            checkAccess();
            if (metaField == fMembers)
                setRelationNN((MetaRelationN) metaField, (DbObject) value, Db.ADD_TO_RELN);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation.getMetaClass() == metaClass) {
            checkAccess();
            setRelationNN(relation, neighbor, op);
        } else
            super.set(relation, neighbor, op);
    }

    public final DbRelationN getMembers() throws DbException {
        return (DbRelationN) get(fMembers);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }
}
