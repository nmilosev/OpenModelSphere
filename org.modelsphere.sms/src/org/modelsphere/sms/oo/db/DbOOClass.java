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
package org.modelsphere.sms.oo.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.oo.db.srtypes.*;
import org.modelsphere.sms.oo.international.LocaleMgr;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../org/modelsphere/sms/oo/java/db/DbJVClass.html">DbJVClass</A>.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOOClass.html">DbOOClass</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOODataMember.html">DbOODataMember</A>, <A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOOClass.html">DbOOClass</A>, <A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOOOperation.html">DbOOOperation</A>, <A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOOInheritance.html">DbOOInheritance</A>, <A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOOAssociation.html">DbOOAssociation</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbOOClass extends DbOOAdt {

    //Meta

    public static final MetaRelationN fSubInheritances = new MetaRelationN(LocaleMgr.db
            .getString("subInheritances"), 0, MetaRelationN.cardN);
    public static final MetaField fAbstract = new MetaField(LocaleMgr.db.getString("abstract"));
    public static final MetaField fBaseType = new MetaField(LocaleMgr.db.getString("baseType"));
    public static final MetaRelationN fSuperInheritances = new MetaRelationN(LocaleMgr.db
            .getString("superInheritances"), 0, MetaRelationN.cardN);
    public static final MetaField fAssociationClass = new MetaField(LocaleMgr.db
            .getString("associationClass"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbOOClass"),
            DbOOClass.class, new MetaField[] { fSubInheritances, fAbstract, fBaseType,
                    fSuperInheritances, fAssociationClass }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbOOAdt.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbOODataMember.metaClass,
                    DbOOClass.metaClass, DbOOOperation.metaClass, DbOOInheritance.metaClass,
                    DbOOAssociation.metaClass });

            fSubInheritances.setJField(DbOOClass.class.getDeclaredField("m_subInheritances"));
            fAbstract.setJField(DbOOClass.class.getDeclaredField("m_abstract"));
            fBaseType.setJField(DbOOClass.class.getDeclaredField("m_baseType"));
            fSuperInheritances.setJField(DbOOClass.class.getDeclaredField("m_superInheritances"));
            fSuperInheritances.setFlags(MetaField.INTEGRABLE | MetaField.INTEGRABLE_BY_NAME);
            fAssociationClass.setJField(DbOOClass.class.getDeclaredField("m_associationClass"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbRelationN m_subInheritances;
    boolean m_abstract;
    boolean m_baseType;
    DbRelationN m_superInheritances;
    boolean m_associationClass;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbOOClass() {
    }

    /**
     * Creates an instance of DbOOClass.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbOOClass(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    //Setters

    /**
     * Sets the "abstract" property of a DbOOClass's instance.
     * 
     * @param value
     *            the "abstract" property
     **/
    public final void setAbstract(Boolean value) throws DbException {
        basicSet(fAbstract, value);
    }

    /**
     * Sets the "base type" property of a DbOOClass's instance.
     * 
     * @param value
     *            the "base type" property
     **/
    public final void setBaseType(Boolean value) throws DbException {
        basicSet(fBaseType, value);
    }

    /**
     * Sets the "association class" property of a DbOOClass's instance.
     * 
     * @param value
     *            the "association class" property
     **/
    public final void setAssociationClass(Boolean value) throws DbException {
        basicSet(fAssociationClass, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fSubInheritances)
                ((DbOOInheritance) value).setSuperClass(this);
            else if (metaField == fSuperInheritances)
                ((DbOOInheritance) value).setSubClass(this);
            else
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
     * Gets the list of subinheritances associated to a DbOOClass's instance.
     * 
     * @return the list of subinheritances.
     **/
    public final DbRelationN getSubInheritances() throws DbException {
        return (DbRelationN) get(fSubInheritances);
    }

    /**
     * Gets the "abstract" property's Boolean value of a DbOOClass's instance.
     * 
     * @return the "abstract" property's Boolean value
     * @deprecated use isAbstract() method instead
     **/
    public final Boolean getAbstract() throws DbException {
        return (Boolean) get(fAbstract);
    }

    /**
     * Tells whether a DbOOClass's instance is abstract or not.
     * 
     * @return boolean
     **/
    public final boolean isAbstract() throws DbException {
        return getAbstract().booleanValue();
    }

    /**
     * Gets the "base type" property's Boolean value of a DbOOClass's instance.
     * 
     * @return the "base type" property's Boolean value
     * @deprecated use isBaseType() method instead
     **/
    public final Boolean getBaseType() throws DbException {
        return (Boolean) get(fBaseType);
    }

    /**
     * Tells whether a DbOOClass's instance is baseType or not.
     * 
     * @return boolean
     **/
    public final boolean isBaseType() throws DbException {
        return getBaseType().booleanValue();
    }

    /**
     * Gets the list of superinheritances associated to a DbOOClass's instance.
     * 
     * @return the list of superinheritances.
     **/
    public final DbRelationN getSuperInheritances() throws DbException {
        return (DbRelationN) get(fSuperInheritances);
    }

    /**
     * Gets the "association class" property's Boolean value of a DbOOClass's instance.
     * 
     * @return the "association class" property's Boolean value
     * @deprecated use isAssociationClass() method instead
     **/
    public final Boolean getAssociationClass() throws DbException {
        return (Boolean) get(fAssociationClass);
    }

    /**
     * Tells whether a DbOOClass's instance is associationClass or not.
     * 
     * @return boolean
     **/
    public final boolean isAssociationClass() throws DbException {
        return getAssociationClass().booleanValue();
    }

}
