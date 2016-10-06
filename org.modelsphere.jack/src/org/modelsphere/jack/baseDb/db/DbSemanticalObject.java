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
import org.modelsphere.jack.baseDb.db.srtypes.UserType;
import org.modelsphere.jack.baseDb.international.LocaleMgr;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;

/**
 * Defines model objects that can be named (semantic object).
 * <p>
 * This class defines generic methods to access name, physical name, alias and description.
 */
public abstract class DbSemanticalObject extends DbObject {

    static final long serialVersionUID = -7285258563960204426L;

    public static final MetaField fName = new MetaField(LocaleMgr.db.getString("name"));

    public static final MetaField fPhysicalName = new MetaField(LocaleMgr.db
            .getString("physicalName"));

    public static final MetaField fAlias = new MetaField(LocaleMgr.db.getString("alias"));

    public static final MetaField fDescription = new MetaField(LocaleMgr.db
            .getString("description"));

    public static final MetaField fWriteAccessList = new MetaField(LocaleMgr.db
            .getString("writeAccessList"));

    public static final MetaField fAdminAccessList = new MetaField(LocaleMgr.db
            .getString("adminAccessList"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSemanticalObject"), DbSemanticalObject.class, new MetaField[] {
            fDescription, // see below
            // fName.setScreenOrder("<description")
            fName, fPhysicalName, fAlias, fWriteAccessList, fAdminAccessList }, MetaClass.MATCHABLE);

    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbObject.metaClass);

            fName.setJField(DbSemanticalObject.class.getDeclaredField("m_name")); // NOT LOCALIZABLE
            // field name
            fName.setScreenOrder("<description"); // trick to have <description>
            // as last field in screens //
            // NOT LOCALIZABLE
            fPhysicalName.setJField(DbSemanticalObject.class.getDeclaredField("m_physicalName")); // NOT LOCALIZABLE
            // field name
            fAlias.setJField(DbSemanticalObject.class.getDeclaredField("m_alias")); // NOT LOCALIZABLE field name
            fDescription.setJField(DbSemanticalObject.class.getDeclaredField("m_description")); // NOT LOCALIZABLE
            // field name
            fDescription.setRendererPluginName("LookupDescription"); // NOT
            // LOCALIZABLE
            // renderer
            // name
            fDescription.setScreenOrder("<creationTime");
            fWriteAccessList.setJField(DbSemanticalObject.class
                    .getDeclaredField("m_writeAccessList")); // NOT LOCALIZABLE
            // field name
            fWriteAccessList.setVisibleInScreen(false);
            fWriteAccessList.setFlags(MetaField.NO_WRITE_CHECK);
            fAdminAccessList.setJField(DbSemanticalObject.class
                    .getDeclaredField("m_adminAccessList")); // NOT LOCALIZABLE
            // field name
            fAdminAccessList.setVisibleInScreen(false);
            fAdminAccessList.setFlags(MetaField.NO_WRITE_CHECK);
        } catch (Exception e) {
            throw new RuntimeException("Meta init"); // NOT LOCALIZABLE
            // RuntimeException
        }
    }

    String m_name;

    String m_physicalName;

    String m_alias;

    String m_description;

    DbtLoginList m_writeAccessList;

    DbtLoginList m_adminAccessList;

    public DbSemanticalObject() {
    }

    public DbSemanticalObject(DbObject composite) throws DbException {
        super(composite);
    }

    public boolean matches(DbObject dbo) throws DbException {
        return DbObject.valuesAreEqual(getName(), ((DbSemanticalObject) dbo).getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.baseDb.db.DbObject#setName(java.lang.String)
     */
    public void setName(String value) throws DbException {
        basicSet(fName, value);
    }

    /**
     * Change the alias. Aliases are alternate display value to identify objects.
     * 
     * @param value
     * @throws DbException
     */
    public final void setAlias(String value) throws DbException {
        basicSet(fAlias, value);
    }

    /**
     * Change the physical name. Physical names are used to allow user to specify a non limitative
     * name to identify objects. The physical name should be used (instead of name) for code, SQL or
     * script generation.
     * 
     * @param value
     * @throws DbException
     */
    public final void setPhysicalName(String value) throws DbException {
        basicSet(fPhysicalName, value);
    }

    /**
     * Change the description for this object.
     * 
     * @param value
     * @throws DbException
     */
    public final void setDescription(String value) throws DbException {
        basicSet(fDescription, value);
    }

    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fName) {
                setName((String) value);
            }
            // No setWriteAccessList/setAdminAccessList methods; must use
            // generic set.
            else if (metaField == fWriteAccessList || metaField == fAdminAccessList) {
                if (!(db.getLoginType() == UserType.ADMIN || hasAdminAccess()))
                    throw new RuntimeException("Unauthorized access"); // NOT
                // LOCALIZABLE
                // RuntimeException
                basicSet(metaField, value);
            } else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.baseDb.db.DbObject#getName()
     */
    public final String getName() throws DbException {
        return (String) get(fName);
    }

    /**
     * @return
     * @throws DbException
     */
    public final String getAlias() throws DbException {
        return (String) get(fAlias);
    }

    /**
     * @return
     * @throws DbException
     * @see #setPhysicalName(String)
     */
    public final String getPhysicalName() throws DbException {
        return (String) get(fPhysicalName);
    }

    /**
     * @return
     * @throws DbException
     * @see #setDescription(String)
     */
    public final String getDescription() throws DbException {
        return (String) get(fDescription);
    }

    public final DbtLoginList getWriteAccessList() throws DbException {
        return (DbtLoginList) get(fWriteAccessList);
    }

    public final DbtLoginList getAdminAccessList() throws DbException {
        return (DbtLoginList) get(fAdminAccessList);
    }

    /**
     * Returns the component of the specified class with the specified physical name.
     * <p>
     * Note: This method is not recursive. Only direct components of this semantical object are
     * checked.
     * 
     * @param metaClass
     *            If null, search for a match in any components.
     * @param physicalName
     * @return
     * @throws DbException
     * 
     * @see DbObject#findComponentByName(MetaClass, String)
     */
    public final DbObject findComponentByPhysicalName(MetaClass metaClass, String physicalName)
            throws DbException {
        DbObject childFound = null;
        DbEnumeration dbEnum = getComponents().elements(metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSemanticalObject child = (DbSemanticalObject) dbEnum.nextElement();
            if (physicalName.equals(child.getPhysicalName())) {
                childFound = child;
                break;
            }
        }
        dbEnum.close();
        return childFound;
    }
}
