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
 * Defines display attributes of a zone for graphical objects. This is an element contained in a
 * notation.
 * 
 * See DbBESingleZoneDisplay in sms.be.db for a concrete class that extends a DbZoneDisplay.
 **/
public abstract class DbZoneDisplay extends DbObject {

    // Meta

    public static final MetaField fDisplayed = new MetaField(LocaleMgr.db.getString("displayed"));

    public static final MetaClass metaClass = new MetaClass(
            LocaleMgr.db.getString("DbZoneDisplay"), DbZoneDisplay.class,
            new MetaField[] { fDisplayed }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbObject.metaClass);

            fDisplayed.setJField(DbZoneDisplay.class.getDeclaredField("m_displayed"));
        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    // Instance variables
    boolean m_displayed;

    // Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbZoneDisplay() {
    }

    /**
     * Creates an instance of DbDisplayField.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbZoneDisplay(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    // Setters

    /**
     * Sets the "displayed" property of a DbZoneDisplay's instance.
     * 
     * @param value
     *            the "displayed" property
     **/
    public final void setDisplayed(Boolean value) throws DbException {
        basicSet(fDisplayed, value);
    }

    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        super.set(relation, neighbor, op);
    }

    // Getters

    /**
     * Gets the "displayed" property's Boolean value of a DbZoneDisplay's instance.
     * 
     * @return the "displayed" property's Boolean value
     * @deprecated use isDisplayed() method instead
     **/
    public final Boolean getDisplayed() throws DbException {
        return (Boolean) get(fDisplayed);
    }

    /**
     * Tells whether a DbZoneDisplay's instance is displayed or not.
     * 
     * @return boolean
     **/
    public final boolean isDisplayed() throws DbException {
        return getDisplayed().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
