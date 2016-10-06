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

/**
 * Define a group of users.
 * 
 * A DbLoginNode is contained in a DbRoot, and DbLoginUser are themselves components of DbLoginNode
 * 
 */
public final class DbLoginNode extends DbObject {

    static final long serialVersionUID = 0;

    public static final MetaField fLastNo = new MetaField(LocaleMgr.db.getString("lastNo"));

    public final static MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbLoginNode"),
            DbLoginNode.class, new MetaField[] { fLastNo }, MetaClass.NAMING_ROOT
                    | MetaClass.NO_UDF);

    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbObject.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbLogin.metaClass });

            fLastNo.setJField(DbLoginNode.class.getDeclaredField("m_lastNo")); // NOT
            // LOCALIZABLE
            // field
            // name
        } catch (Exception e) {
            throw new RuntimeException("Meta init"); // NOT LOCALIZABLE
            // RuntimeException
        }
    }

    int m_lastNo;

    public DbLoginNode() {
    }

    public DbLoginNode(DbRoot dbRoot) throws DbException {
        super(dbRoot);
    }

    // Called in DbLogin constructor
    final Integer nextNo() throws DbException {
        Integer next = (Integer) get(fLastNo);
        next = new Integer(next.intValue() + 1);
        basicSet(fLastNo, next);
        return next;
    }

    public final DbLogin getLogin(int no) throws DbException {
        DbRelationN logins = getComponents();
        for (int i = 0; i < logins.size(); i++) {
            DbLogin login = (DbLogin) logins.elementAt(i);
            if (no == login.getNo().intValue())
                return login;
        }
        return null;
    }

    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fLastNo)
                setLastNo((Integer) value);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    public final Integer getLastNo() throws DbException {
        return (Integer) get(fLastNo);
    }

    public final void setLastNo(Integer value) throws DbException {
        throw new RuntimeException(fLastNo.getGUIName() + " not settable"); // NOT
        // LOCALIZABLE
        // RuntimeException
    }

    public final MetaClass getMetaClass() {
        return metaClass;
    }
}
