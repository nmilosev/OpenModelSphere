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

import org.modelsphere.jack.baseDb.db.srtypes.DbtPassword;
import org.modelsphere.jack.baseDb.db.srtypes.UserType;
import org.modelsphere.jack.baseDb.international.LocaleMgr;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;

/**
 * 
 * Define a user. DbLoginUsers are components of DbLoginNode (a group of users). Internal to the Db
 * framework.
 * 
 * Only an ADMIN user can add, remove or modify DbLoginUser objects.
 */
public final class DbLoginUser extends DbLogin {

    static final long serialVersionUID = 0;

    public static final MetaField fPassword = new MetaField(LocaleMgr.db.getString("password"));
    public static final MetaField fUserType = new MetaField(LocaleMgr.db.getString("usertype"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbLoginUser"),
            DbLoginUser.class, new MetaField[] { fPassword, fUserType }, MetaClass.NO_UDF);

    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbLogin.metaClass);

            fPassword.setJField(DbLoginUser.class.getDeclaredField("m_password")); // NOT LOCALIZABLE field name
            fPassword.setVisibleInScreen(false);
            fUserType.setJField(DbLoginUser.class.getDeclaredField("m_usertype")); // NOT LOCALIZABLE field name
        } catch (Exception e) {
            throw new RuntimeException("Meta init"); // NOT LOCALIZABLE RuntimeException
        }
    }

    DbtPassword m_password;
    UserType m_usertype;

    public DbLoginUser() {
    }

    public DbLoginUser(DbObject composite) throws DbException {
        this(composite, false);
    }

    DbLoginUser(DbObject composite, boolean isInitial) throws DbException {
        super(composite);
        if (isInitial) {
            basicSet(DbLogin.fName, "Admin"); // NOT LOCALIZABLE
            basicSet(fUserType, UserType.getInstance(UserType.ADMIN));
        } else {
            basicSet(DbLogin.fName, LocaleMgr.db.getString("user"));
            basicSet(fUserType, UserType.getInstance(UserType.USER));
        }
        basicSet(fPassword, new DbtPassword(""));
    }

    public final boolean isDeletable() throws DbException {
        return (this != db.getLogin());
    }

    // We provide no individual set methods for this class; use the generic
    // <set>.
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            checkAccess();
            if (metaField == fUserType) {
                if (this != db.getLogin()) // do not let the admin change its
                    // own access
                    basicSet(metaField, value);
            } else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    public final DbtPassword getPassword() throws DbException {
        return (DbtPassword) get(fPassword);
    }

    public final UserType getUserType() throws DbException {
        return (UserType) get(fUserType);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }
}
