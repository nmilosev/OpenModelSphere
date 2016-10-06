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
 * HREF="../../../../org/modelsphere/sms/oo/db/DbOODataMember.html">DbOODataMember</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbORAttribute.html">DbORAttribute</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbSMSStructuralFeature extends DbSMSFeature {

    //Meta

    public static final MetaField fInitialValue = new MetaField(LocaleMgr.db
            .getString("initialValue"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSStructuralFeature"), DbSMSStructuralFeature.class,
            new MetaField[] { fInitialValue }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSFeature.metaClass);

            fInitialValue
                    .setJField(DbSMSStructuralFeature.class.getDeclaredField("m_initialValue"));
            fInitialValue.setRendererPluginName("LookupDescription");

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    String m_initialValue;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSStructuralFeature() {
    }

    /**
     * Creates an instance of DbSMSStructuralFeature.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSStructuralFeature(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    //Setters

    /**
     * Sets the "initial value" property of a DbSMSStructuralFeature's instance.
     * 
     * @param value
     *            the "initial value" property
     **/
    public final void setInitialValue(String value) throws DbException {
        basicSet(fInitialValue, value);
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
     * Gets the "initial value" property of a DbSMSStructuralFeature's instance.
     * 
     * @return the "initial value" property
     **/
    public final String getInitialValue() throws DbException {
        return (String) get(fInitialValue);
    }

}
