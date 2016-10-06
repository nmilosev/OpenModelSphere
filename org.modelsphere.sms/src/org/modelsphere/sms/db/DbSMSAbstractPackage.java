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
package org.modelsphere.sms.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.international.LocaleMgr;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSBuiltInTypePackage.html"
 * >DbSMSBuiltInTypePackage</A>, <A
 * HREF="../../../../org/modelsphere/sms/oo/db/DbOOAbsPackage.html">DbOOAbsPackage</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbORModel.html">DbORModel</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSNotice.html">DbSMSNotice</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbSMSAbstractPackage extends DbSMSPackage {

    //Meta

    public static final MetaRelation1 fTargetSystem = new MetaRelation1(LocaleMgr.db
            .getString("targetSystem"), 0);
    public static final MetaField fIsValidated = new MetaField(LocaleMgr.db
            .getString("isValidated"));
    public static final MetaField fIsLocked = new MetaField(LocaleMgr.db.getString("isLocked"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSAbstractPackage"), DbSMSAbstractPackage.class, new MetaField[] {
            fTargetSystem, fIsValidated, fIsLocked, }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSPackage.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbSMSLinkModel.metaClass });

            fTargetSystem.setJField(DbSMSAbstractPackage.class.getDeclaredField("m_targetSystem"));
            fTargetSystem.setFlags(MetaField.COPY_REFS);
            fIsValidated.setJField(DbSMSAbstractPackage.class.getDeclaredField("m_isValidated"));
            fIsLocked.setJField(DbSMSAbstractPackage.class.getDeclaredField("m_isLocked"));
            fIsLocked.setVisibleInScreen(false);

            fTargetSystem.setOppositeRel(DbSMSTargetSystem.fPackages);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbSMSTargetSystem m_targetSystem;
    boolean m_isValidated;
    boolean m_isLocked;
    DbSMSLinkModel[] m_ownedLinkModels;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSAbstractPackage() {
    }

    /**
     * Creates an instance of DbSMSAbstractPackage.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSAbstractPackage(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @param dbo
     *            org.modelsphere.jack.baseDb.db.DbObject
     * @return boolean
     **/
    public boolean matches(DbObject dbo) throws DbException {
        DbSMSAbstractPackage that = (DbSMSAbstractPackage) dbo;
        return (DbObject.valuesAreEqual(getName(), that.getName()) && getTargetSystem().matches(
                that.getTargetSystem()));
    }

    //Setters

    /**
     * Sets the target system object associated to a DbSMSAbstractPackage's instance.
     * 
     * @param value
     *            the target system object to be associated
     **/
    public final void setTargetSystem(DbSMSTargetSystem value) throws DbException {
        basicSet(fTargetSystem, value);
    }

    /**
     * Sets the "is validated" property of a DbSMSAbstractPackage's instance.
     * 
     * @param value
     *            the "is validated" property
     **/
    public final void setIsValidated(Boolean value) throws DbException {
        basicSet(fIsValidated, value);
    }

    /**
     * Sets the "locked" property of a DbSMSAbstractPackage's instance.
     * 
     * @param value
     *            the "locked" property
     **/
    public final void setIsLocked(Boolean value) throws DbException {
        basicSet(fIsLocked, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the target system object associated to a DbSMSAbstractPackage's instance.
     * 
     * @return the target system object
     **/
    public final DbSMSTargetSystem getTargetSystem() throws DbException {
        return (DbSMSTargetSystem) get(fTargetSystem);
    }

    /**
     * Gets the "is validated" property's Boolean value of a DbSMSAbstractPackage's instance.
     * 
     * @return the "is validated" property's Boolean value
     * @deprecated use isIsValidated() method instead
     **/
    public final Boolean getIsValidated() throws DbException {
        return (Boolean) get(fIsValidated);
    }

    /**
     * Tells whether a DbSMSAbstractPackage's instance is isValidated or not.
     * 
     * @return boolean
     **/
    public final boolean isIsValidated() throws DbException {
        return getIsValidated().booleanValue();
    }

    /**
     * Gets the "locked" property's Boolean value of a DbSMSAbstractPackage's instance.
     * 
     * @return the "locked" property's Boolean value
     * @deprecated use isIsLocked() method instead
     **/
    public final Boolean getIsLocked() throws DbException {
        return (Boolean) get(fIsLocked);
    }

    /**
     * Tells whether a DbSMSAbstractPackage's instance is isLocked or not.
     * 
     * @return boolean
     **/
    public final boolean isIsLocked() throws DbException {
        return getIsLocked().booleanValue();
    }

}
