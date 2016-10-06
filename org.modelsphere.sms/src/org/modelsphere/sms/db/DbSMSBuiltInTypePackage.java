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
import java.util.Iterator;
import org.modelsphere.sms.oo.db.DbOOPrimitiveType;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.oo.java.db.DbJVPrimitiveType;
import org.modelsphere.sms.plugins.TargetSystem;
import org.modelsphere.sms.plugins.TargetSystemManager;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSBuiltInTypeNode.html">DbSMSBuiltInTypeNode</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../org/modelsphere/sms/oo/db/DbOOPrimitiveType.html">DbOOPrimitiveType</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbORBuiltInType.html">DbORBuiltInType</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSNotice.html">DbSMSNotice</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbSMSBuiltInTypePackage extends DbSMSAbstractPackage {

    //Meta

    public static final MetaField fBuiltIn = new MetaField(LocaleMgr.db.getString("builtIn"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSBuiltInTypePackage"), DbSMSBuiltInTypePackage.class,
            new MetaField[] { fBuiltIn }, MetaClass.ACCESS_CTL | MetaClass.CLUSTER_ROOT
                    | MetaClass.MATCHABLE | MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSAbstractPackage.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbOOPrimitiveType.metaClass,
                    DbORBuiltInType.metaClass });

            fBuiltIn.setJField(DbSMSBuiltInTypePackage.class.getDeclaredField("m_builtIn"));
            fBuiltIn.setEditable(false);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    boolean m_builtIn;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSBuiltInTypePackage() {
    }

    /**
     * Creates an instance of DbSMSBuiltInTypePackage.
     * 
     * @param builtintypenode
     *            org.modelsphere.sms.db.DbSMSBuiltInTypeNode
     * @param name
     *            java.lang.String
     * @param targetsystem
     *            org.modelsphere.sms.db.DbSMSTargetSystem
     **/
    public DbSMSBuiltInTypePackage(DbSMSBuiltInTypeNode builtInTypeNode, String name,
            DbSMSTargetSystem targetSystem) throws DbException {
        super(builtInTypeNode);
        setDefaultInitialValues();
        setName(name);
        setTargetSystem(targetSystem);

        boolean isJava = (targetSystem.getRootID().intValue() == TargetSystem.SGBD_JAVA);
        Iterator iter = TargetSystemManager.getSingleton().getTargetSystemInfo(targetSystem)
                .getTypeSet().iterator();
        while (iter.hasNext()) {
            String typeName = (String) iter.next();
            if (isJava)
                new DbJVPrimitiveType(this, typeName);
            else
                new DbORBuiltInType(this, typeName);
        }
    }

    private void setDefaultInitialValues() throws DbException {
        setBuiltIn(Boolean.TRUE);
    }

    /**
     * @return boolean
     **/
    public final boolean isDeletable() throws DbException {
        return false;
    }

    /**
     * @param dbo
     *            org.modelsphere.jack.baseDb.db.DbObject
     * @return boolean
     **/
    public boolean matches(DbObject dbo) throws DbException {
        DbSMSBuiltInTypePackage that = (DbSMSBuiltInTypePackage) dbo;
        return getTargetSystem().matches(that.getTargetSystem());
    }

    //Setters

    /**
     * Sets the "built in?" property of a DbSMSBuiltInTypePackage's instance.
     * 
     * @param value
     *            the "built in?" property
     **/
    public final void setBuiltIn(Boolean value) throws DbException {
        basicSet(fBuiltIn, value);
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
     * Gets the "built in?" property's Boolean value of a DbSMSBuiltInTypePackage's instance.
     * 
     * @return the "built in?" property's Boolean value
     * @deprecated use isBuiltIn() method instead
     **/
    public final Boolean getBuiltIn() throws DbException {
        return (Boolean) get(fBuiltIn);
    }

    /**
     * Tells whether a DbSMSBuiltInTypePackage's instance is builtIn or not.
     * 
     * @return boolean
     **/
    public final boolean isBuiltIn() throws DbException {
        return getBuiltIn().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
