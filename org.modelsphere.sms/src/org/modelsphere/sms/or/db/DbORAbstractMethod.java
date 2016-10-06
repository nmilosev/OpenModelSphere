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
package org.modelsphere.sms.or.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.SMSFilter;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORTrigger.html">DbORTrigger</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORProcedure.html">DbORProcedure</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORPackage.html">DbORPackage</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbORAbstractMethod extends DbSMSBehavioralFeature {

    //Meta

    public static final MetaRelation1 fUser = new MetaRelation1(LocaleMgr.db.getString("user"), 0);
    public static final MetaField fBody = new MetaField(LocaleMgr.db.getString("body"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORAbstractMethod"), DbORAbstractMethod.class, new MetaField[] { fUser,
            fBody }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSBehavioralFeature.metaClass);

            fUser.setJField(DbORAbstractMethod.class.getDeclaredField("m_user"));
            fUser.setFlags(MetaField.COPY_REFS);
            fBody.setJField(DbORAbstractMethod.class.getDeclaredField("m_body"));
            fBody.setScreenOrder("<description");
            fBody.setRendererPluginName("LookupDescription");

            fUser.setOppositeRel(DbORUser.fOperations);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbORUser m_user;
    String m_body;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORAbstractMethod() {
    }

    /**
     * Creates an instance of DbORAbstractMethod.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORAbstractMethod(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @return int
     **/
    protected final int getSecurityMask() {
        return SMSFilter.RELATIONAL;

    }

    //Setters

    /**
     * Sets the user object associated to a DbORAbstractMethod's instance.
     * 
     * @param value
     *            the user object to be associated
     **/
    public final void setUser(DbORUser value) throws DbException {
        basicSet(fUser, value);
    }

    /**
     * Sets the "body" property of a DbORAbstractMethod's instance.
     * 
     * @param value
     *            the "body" property
     **/
    public final void setBody(String value) throws DbException {
        basicSet(fBody, value);
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
     * Gets the user object associated to a DbORAbstractMethod's instance.
     * 
     * @return the user object
     **/
    public final DbORUser getUser() throws DbException {
        return (DbORUser) get(fUser);
    }

    /**
     * Gets the "body" property of a DbORAbstractMethod's instance.
     * 
     * @return the "body" property
     **/
    public final String getBody() throws DbException {
        return (String) get(fBody);
    }

}
