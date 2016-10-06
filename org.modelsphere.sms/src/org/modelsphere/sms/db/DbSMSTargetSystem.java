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
import org.modelsphere.sms.or.generic.db.*;
import org.modelsphere.sms.plugins.TargetSystem;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSProject.html">DbSMSProject</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbSMSTargetSystem extends DbSMSSemanticalObject {

    //Meta

    public static final MetaField fVersion = new MetaField(LocaleMgr.db.getString("version"));
    public static final MetaField fID = new MetaField(LocaleMgr.db.getString("ID"));
    public static final MetaField fRootID = new MetaField(LocaleMgr.db.getString("rootID"));
    public static final MetaRelationN fPackages = new MetaRelationN(LocaleMgr.db
            .getString("packages"), 0, MetaRelationN.cardN);
    public static final MetaField fBuiltIn = new MetaField(LocaleMgr.db.getString("builtIn"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSTargetSystem"), DbSMSTargetSystem.class, new MetaField[] { fVersion,
            fID, fRootID, fPackages, fBuiltIn }, MetaClass.MATCHABLE | MetaClass.NO_UDF
            | MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);
            metaClass.setIcon("dbsmstargetsystem.gif");

            fVersion.setJField(DbSMSTargetSystem.class.getDeclaredField("m_version"));
            fVersion.setScreenOrder(">name");
            fID.setJField(DbSMSTargetSystem.class.getDeclaredField("m_ID"));
            fID.setVisibleInScreen(false);
            fRootID.setJField(DbSMSTargetSystem.class.getDeclaredField("m_rootID"));
            fRootID.setVisibleInScreen(false);
            fPackages.setJField(DbSMSTargetSystem.class.getDeclaredField("m_packages"));
            fBuiltIn.setJField(DbSMSTargetSystem.class.getDeclaredField("m_builtIn"));
            fBuiltIn.setEditable(false);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    private transient DbSMSBuiltInTypePackage builtInTypePackage;

    //Instance variables
    String m_version;
    int m_ID;
    int m_rootID;
    DbRelationN m_packages;
    boolean m_builtIn;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSTargetSystem() {
    }

    /**
     * Creates an instance of DbSMSTargetSystem.
     * 
     * @param project
     *            org.modelsphere.sms.db.DbSMSProject
     * @param name
     *            java.lang.String
     * @param version
     *            java.lang.String
     * @param id
     *            java.lang.Integer
     * @param rootid
     *            java.lang.Integer
     **/
    public DbSMSTargetSystem(DbSMSProject project, String name, String version, Integer id,
            Integer rootId) throws DbException {
        super(project);
        setDefaultInitialValues();
        setName(name);
        setVersion(version);
        setID(id);
        setRootID(rootId);
        builtInTypePackage = new DbSMSBuiltInTypePackage(project.getBuiltInTypeNode(), name + ' '
                + version, this);
        if (m_rootID > 999 && m_rootID < 2000) {
            setBuiltIn(Boolean.FALSE);
            builtInTypePackage.setBuiltIn(Boolean.FALSE);
        }
    }

    private void setDefaultInitialValues() throws DbException {
        setBuiltIn(Boolean.TRUE);
    }

    /**
     * @return built in type package
     **/
    public final DbSMSBuiltInTypePackage getBuiltInTypePackage() throws DbException {
        if (builtInTypePackage == null) {
            DbEnumeration dbEnum = ((DbSMSProject) getProject()).getBuiltInTypeNode()
                    .getComponents().elements(DbSMSBuiltInTypePackage.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbSMSBuiltInTypePackage pack = (DbSMSBuiltInTypePackage) dbEnum.nextElement();
                if (pack.getTargetSystem() == this) {
                    builtInTypePackage = pack;
                    break;
                }
            }
            dbEnum.close();
        }
        return builtInTypePackage;
    }

    /**

 **/
    public final void remove() throws DbException {
        if (getProject().getTransStatus() != Db.OBJ_REMOVED) {
            DbSMSBuiltInTypePackage pack = getBuiltInTypePackage();
            if (pack != null)
                pack.remove();
        }
        super.remove();
    }

    /**
     * @param dbo
     *            org.modelsphere.jack.baseDb.db.DbObject
     * @return boolean
     **/
    public boolean matches(DbObject dbo) throws DbException {
        DbSMSTargetSystem that = (DbSMSTargetSystem) dbo;
        return getID().equals(that.getID());
    }

    /**
     * @return boolean
     **/
    public final boolean isDeletable() throws DbException {
        return (getID().intValue() != TargetSystem.SGBD_LOGICAL && getPackages().size() <= 1);
    }

    //Setters

    /**
     * Sets the "version" property of a DbSMSTargetSystem's instance.
     * 
     * @param value
     *            the "version" property
     **/
    public final void setVersion(String value) throws DbException {
        basicSet(fVersion, value);
    }

    /**
     * Sets the "id" property of a DbSMSTargetSystem's instance.
     * 
     * @param value
     *            the "id" property
     **/
    public final void setID(Integer value) throws DbException {
        basicSet(fID, value);
    }

    /**
     * Sets the "root id" property of a DbSMSTargetSystem's instance.
     * 
     * @param value
     *            the "root id" property
     **/
    public final void setRootID(Integer value) throws DbException {
        basicSet(fRootID, value);
    }

    /**
     * Sets the "built in?" property of a DbSMSTargetSystem's instance.
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
            if (metaField == fPackages)
                ((DbSMSAbstractPackage) value).setTargetSystem(this);
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
     * Gets the "version" property of a DbSMSTargetSystem's instance.
     * 
     * @return the "version" property
     **/
    public final String getVersion() throws DbException {
        return (String) get(fVersion);
    }

    /**
     * Gets the "id" property of a DbSMSTargetSystem's instance.
     * 
     * @return the "id" property
     **/
    public final Integer getID() throws DbException {
        return (Integer) get(fID);
    }

    /**
     * Gets the "root id" property of a DbSMSTargetSystem's instance.
     * 
     * @return the "root id" property
     **/
    public final Integer getRootID() throws DbException {
        return (Integer) get(fRootID);
    }

    /**
     * Gets the list of packages associated to a DbSMSTargetSystem's instance.
     * 
     * @return the list of packages.
     **/
    public final DbRelationN getPackages() throws DbException {
        return (DbRelationN) get(fPackages);
    }

    /**
     * Gets the "built in?" property's Boolean value of a DbSMSTargetSystem's instance.
     * 
     * @return the "built in?" property's Boolean value
     * @deprecated use isBuiltIn() method instead
     **/
    public final Boolean getBuiltIn() throws DbException {
        return (Boolean) get(fBuiltIn);
    }

    /**
     * Tells whether a DbSMSTargetSystem's instance is builtIn or not.
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
