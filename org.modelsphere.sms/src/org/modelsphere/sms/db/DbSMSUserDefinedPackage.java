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
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORUserNode;
import org.modelsphere.sms.be.db.DbBEModel;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSProject.html">DbSMSProject</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSUserDefinedPackage.html"
 * >DbSMSUserDefinedPackage</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../org/modelsphere/sms/oo/java/db/DbJVClassModel.html">DbJVClassModel</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbORDataModel.html">DbORDataModel</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSUserDefinedPackage.html"
 * >DbSMSUserDefinedPackage</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbOROperationLibrary.html">DbOROperationLibrary</A>,
 * <A HREF="../../../../org/modelsphere/sms/or/db/DbORDomainModel.html">DbORDomainModel</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbORCommonItemModel.html">DbORCommonItemModel</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbORDatabase.html">DbORDatabase</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSLinkModel.html">DbSMSLinkModel</A>, <A
 * HREF="../../../../org/modelsphere/sms/be/db/DbBEModel.html">DbBEModel</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSNotice.html">DbSMSNotice</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbSMSUserDefinedPackage extends DbSMSPackage {

    //Meta

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSUserDefinedPackage"), DbSMSUserDefinedPackage.class,
            new MetaField[] {}, MetaClass.ACCESS_CTL | MetaClass.CLUSTER_ROOT | MetaClass.MATCHABLE
                    | MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSPackage.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbJVClassModel.metaClass,
                    DbORDataModel.metaClass, DbSMSUserDefinedPackage.metaClass,
                    DbOROperationLibrary.metaClass, DbORDomainModel.metaClass,
                    DbORCommonItemModel.metaClass, DbORDatabase.metaClass,
                    DbSMSLinkModel.metaClass, DbBEModel.metaClass });
            metaClass.setIcon("dbsmsuserdefinedpackage.gif");

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSUserDefinedPackage() {
    }

    /**
     * Creates an instance of DbSMSUserDefinedPackage.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSUserDefinedPackage(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setName(LocaleMgr.misc.getString("userdefpackage"));
    }

    //Setters

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

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
