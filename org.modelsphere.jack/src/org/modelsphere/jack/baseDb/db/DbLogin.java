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

import org.modelsphere.jack.baseDb.db.srtypes.UserType;
import org.modelsphere.jack.baseDb.international.LocaleMgr;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;

/**
 * 
 * A common interface for DbLoginUser and DbLoginGroup
 * 
 * Only an ADMIN user can add, remove or modify DbLogin objects.
 * 
 */
// 
public abstract class DbLogin extends DbObject {

    static final long serialVersionUID = 0;

    public static final MetaField fName = new MetaField(LocaleMgr.db.getString("name"));
    public static final MetaField fNo = new MetaField(LocaleMgr.db.getString("no"));
    public static final MetaRelationN fGroups = new MetaRelationN(LocaleMgr.db.getString("groups"),
            0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbLogin"),
            DbLogin.class, new MetaField[] { fName, fNo, fGroups }, 0);

    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbObject.metaClass);

            fName.setJField(DbLogin.class.getDeclaredField("m_name")); // NOT LOCALIZABLE field name
            fNo.setJField(DbLogin.class.getDeclaredField("m_no")); // NOT LOCALIZABLE field name
            fNo.setVisibleInScreen(false);
            fGroups.setJField(DbLogin.class.getDeclaredField("m_groups")); // NOT LOCALIZABLE field name

            fGroups.setOppositeRel(DbLoginGroup.fMembers);
        } catch (Exception e) {
            throw new RuntimeException("Meta init"); // NOT LOCALIZABLE RuntimeException
        }
    }

    String m_name;
    int m_no;
    DbRelationN m_groups;

    public DbLogin() {
    }

    public DbLogin(DbObject composite) throws DbException {
        super(composite);
        checkAccess();
        basicSet(fNo, ((DbLoginNode) composite).nextNo());
    }

    public void remove() throws DbException {
        checkAccess();
        super.remove();
    }

    // We provide no individual set methods for this class; use the generic
    // <set>.
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            checkAccess();
            if (metaField == fNo)
                throw new RuntimeException(metaField.getGUIName() + " not settable"); // NOT LOCALIZABLE RuntimeException
            else if (metaField == fGroups)
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

    public final String getName() throws DbException {
        return (String) get(fName);
    }

    public final Integer getNo() throws DbException {
        return (Integer) get(fNo);
    }

    public final DbRelationN getGroups() throws DbException {
        return (DbRelationN) get(fGroups);
    }

    protected final void checkAccess() throws DbException {
        // Do not use db.getLoginType(); we want an up-to-date value
        if (db.getLogin() != null && db.getLogin().getUserType().getValue() != UserType.ADMIN)
            throw new RuntimeException("Unauthorized access"); // NOT LOCALIZABLE RuntimeException
    }
}
