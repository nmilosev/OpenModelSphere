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
 * HREF="../../../../../org/modelsphere/sms/oo/java/db/DbJVInheritance.html">DbJVInheritance</A>.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/oo/db/DbOOClass.html">DbOOClass</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public abstract class DbOOInheritance extends DbSMSInheritance {

    //Meta

    public static final MetaRelation1 fSuperClass = new MetaRelation1(LocaleMgr.db
            .getString("superClass"), 1);
    public static final MetaRelation1 fSubClass = new MetaRelation1(LocaleMgr.db
            .getString("subClass"), 1);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbOOInheritance"), DbOOInheritance.class, new MetaField[] { fSuperClass,
            fSubClass }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSInheritance.metaClass);
            metaClass.setIcon("dbooinheritance.gif");

            fSuperClass.setJField(DbOOInheritance.class.getDeclaredField("m_superClass"));
            fSuperClass.setFlags(MetaField.COPY_REFS);
            fSuperClass.setEditable(false);
            fSubClass.setJField(DbOOInheritance.class.getDeclaredField("m_subClass"));
            fSubClass.setEditable(false);

            fSuperClass.setOppositeRel(DbOOClass.fSubInheritances);
            fSubClass.setOppositeRel(DbOOClass.fSuperInheritances);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbOOClass m_superClass;
    DbOOClass m_subClass;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbOOInheritance() {
    }

    /**
     * Creates an instance of DbOOInheritance.
     * 
     * @param subclass
     *            org.modelsphere.sms.oo.db.DbOOClass
     * @param superclass
     *            org.modelsphere.sms.oo.db.DbOOClass
     **/
    public DbOOInheritance(DbOOClass subClass, DbOOClass superClass) throws DbException {
        super(subClass);
        basicSet(fSubClass, subClass);
        basicSet(fSuperClass, superClass);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    //Setters

    /**
     * Sets the superclass object associated to a DbOOInheritance's instance.
     * 
     * @param value
     *            the superclass object to be associated
     **/
    public final void setSuperClass(DbOOClass value) throws DbException {
        basicSet(fSuperClass, value);
    }

    /**
     * Sets the subclass object associated to a DbOOInheritance's instance.
     * 
     * @param value
     *            the subclass object to be associated
     **/
    public final void setSubClass(DbOOClass value) throws DbException {
        basicSet(fSubClass, value);
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
     * Gets the superclass object associated to a DbOOInheritance's instance.
     * 
     * @return the superclass object
     **/
    public final DbOOClass getSuperClass() throws DbException {
        return (DbOOClass) get(fSuperClass);
    }

    /**
     * Gets the subclass object associated to a DbOOInheritance's instance.
     * 
     * @return the subclass object
     **/
    public final DbOOClass getSubClass() throws DbException {
        return (DbOOClass) get(fSubClass);
    }

}
