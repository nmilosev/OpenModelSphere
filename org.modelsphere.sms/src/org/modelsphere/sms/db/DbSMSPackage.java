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
 * HREF="../../../../org/modelsphere/sms/db/DbSMSUserDefinedPackage.html"
 * >DbSMSUserDefinedPackage</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSAbstractPackage.html">DbSMSAbstractPackage</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSBuiltInTypeNode.html">DbSMSBuiltInTypeNode</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSLinkModel.html">DbSMSLinkModel</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSUmlExtensibility.html">DbSMSUmlExtensibility</A>,
 * <A HREF="../../../../org/modelsphere/sms/or/db/DbORCommonItemModel.html">DbORCommonItemModel</A>,
 * <A HREF="../../../../org/modelsphere/sms/or/db/DbORUserNode.html">DbORUserNode</A>, <A
 * HREF="../../../../org/modelsphere/sms/be/db/DbBEModel.html">DbBEModel</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSNotice.html">DbSMSNotice</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbSMSPackage extends DbSMSSemanticalObject {

    //Meta

    public static final MetaRelationN fPackageGos = new MetaRelationN(LocaleMgr.db
            .getString("packageGos"), 0, MetaRelationN.cardN);
    public static final MetaField fVersion = new MetaField(LocaleMgr.db.getString("version"));
    public static final MetaField fAuthor = new MetaField(LocaleMgr.db.getString("author"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbSMSPackage"),
            DbSMSPackage.class, new MetaField[] { fPackageGos, fVersion, fAuthor },
            MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbSMSNotice.metaClass });

            fPackageGos.setJField(DbSMSPackage.class.getDeclaredField("m_packageGos"));
            fVersion.setJField(DbSMSPackage.class.getDeclaredField("m_version"));
            fAuthor.setJField(DbSMSPackage.class.getDeclaredField("m_author"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbRelationN m_packageGos;
    String m_version;
    String m_author;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSPackage() {
    }

    /**
     * Creates an instance of DbSMSPackage.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSPackage(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    //Setters

    /**
     * Sets the "version" property of a DbSMSPackage's instance.
     * 
     * @param value
     *            the "version" property
     **/
    public final void setVersion(String value) throws DbException {
        basicSet(fVersion, value);
    }

    /**
     * Sets the "author" property of a DbSMSPackage's instance.
     * 
     * @param value
     *            the "author" property
     **/
    public final void setAuthor(String value) throws DbException {
        basicSet(fAuthor, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fPackageGos)
                ((DbSMSPackageGo) value).setPackage(this);
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
     * Gets the list of packages graphical objects associated to a DbSMSPackage's instance.
     * 
     * @return the list of packages graphical objects.
     **/
    public final DbRelationN getPackageGos() throws DbException {
        return (DbRelationN) get(fPackageGos);
    }

    /**
     * Gets the "version" property of a DbSMSPackage's instance.
     * 
     * @return the "version" property
     **/
    public final String getVersion() throws DbException {
        return (String) get(fVersion);
    }

    /**
     * Gets the "author" property of a DbSMSPackage's instance.
     * 
     * @return the "author" property
     **/
    public final String getAuthor() throws DbException {
        return (String) get(fAuthor);
    }

}
