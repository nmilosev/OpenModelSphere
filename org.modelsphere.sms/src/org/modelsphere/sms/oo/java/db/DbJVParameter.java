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
package org.modelsphere.sms.oo.java.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.oo.java.db.srtypes.*;
import org.modelsphere.sms.oo.java.international.LocaleMgr;
import org.modelsphere.sms.oo.db.*;
import org.modelsphere.sms.oo.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.SMSFilter;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/oo/db/DbOOAbstractMethod.html">
 * DbOOAbstractMethod</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbJVParameter extends DbOOParameter {

    //Meta

    public static final MetaField fFinal = new MetaField(LocaleMgr.db.getString("final"));

    public static final MetaClass metaClass = new MetaClass(
            LocaleMgr.db.getString("DbJVParameter"), DbJVParameter.class,
            new MetaField[] { fFinal }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbOOParameter.metaClass);

            fFinal.setJField(DbJVParameter.class.getDeclaredField("m_final"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    public static final String ARRAY_TYPE_USE = "[]";

    //Instance variables
    boolean m_final;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbJVParameter() {
    }

    /**
     * Creates an instance of DbJVParameter.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbJVParameter(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setName(LocaleMgr.misc.getString("parameter"));
    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.JAVA;

    }

    //Setters

    /**
     * Sets the "final" property of a DbJVParameter's instance.
     * 
     * @param value
     *            the "final" property
     **/
    public final void setFinal(Boolean value) throws DbException {
        basicSet(fFinal, value);
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
     * Gets the "final" property's Boolean value of a DbJVParameter's instance.
     * 
     * @return the "final" property's Boolean value
     * @deprecated use isFinal() method instead
     **/
    public final Boolean getFinal() throws DbException {
        return (Boolean) get(fFinal);
    }

    /**
     * Tells whether a DbJVParameter's instance is final or not.
     * 
     * @return boolean
     **/
    public final boolean isFinal() throws DbException {
        return getFinal().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
