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
package org.modelsphere.sms.be.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.be.db.srtypes.*;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEUseCaseQualifier.html"
 * >DbBEUseCaseQualifier</A>, <A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEActorQualifier.html">DbBEActorQualifier</A>,
 * <A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEStoreQualifier.html">DbBEStoreQualifier</A>,
 * <A HREF="../../../../../org/modelsphere/sms/be/db/DbBEFlowQualifier.html">DbBEFlowQualifier</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b>none.<br>
 **/
public abstract class DbBEQualifierLink extends DbObject {

    //Meta

    public static final MetaField fRate2 = new MetaField(LocaleMgr.db.getString("rate2"));
    public static final MetaField fRateUnit = new MetaField(LocaleMgr.db.getString("rateUnit"));
    public static final MetaField fDescription = new MetaField(LocaleMgr.db
            .getString("description"));
    public static final MetaField fRate = new MetaField(LocaleMgr.db.getString("rate"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbBEQualifierLink"), DbBEQualifierLink.class, new MetaField[] { fRate2,
            fRateUnit, fDescription, fRate }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbObject.metaClass);

            fRate2.setJField(DbBEQualifierLink.class.getDeclaredField("m_rate2"));
            fRateUnit.setJField(DbBEQualifierLink.class.getDeclaredField("m_rateUnit"));
            fDescription.setJField(DbBEQualifierLink.class.getDeclaredField("m_description"));
            fDescription.setRendererPluginName("LookupDescription");
            fRate.setJField(DbBEQualifierLink.class.getDeclaredField("m_rate"));
            fRate.setVisibleInScreen(false);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    double m_rate2;
    String m_rateUnit;
    String m_description;
    String m_rate;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBEQualifierLink() {
    }

    /**
     * Creates an instance of DbBEQualifierLink.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbBEQualifierLink(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    //Setters

    /**
     * Sets the "rate" property of a DbBEQualifierLink's instance.
     * 
     * @param value
     *            the "rate" property
     **/
    public final void setRate2(Double value) throws DbException {
        basicSet(fRate2, value);
    }

    /**
     * Sets the "rate unit" property of a DbBEQualifierLink's instance.
     * 
     * @param value
     *            the "rate unit" property
     **/
    public final void setRateUnit(String value) throws DbException {
        basicSet(fRateUnit, value);
    }

    /**
     * Sets the "description" property of a DbBEQualifierLink's instance.
     * 
     * @param value
     *            the "description" property
     **/
    public final void setDescription(String value) throws DbException {
        basicSet(fDescription, value);
    }

    /**
     * Sets the "rate (obsolete)" property of a DbBEQualifierLink's instance.
     * 
     * @param value
     *            the "rate (obsolete)" property
     **/
    public final void setRate(String value) throws DbException {
        basicSet(fRate, value);
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
     * Gets the "rate" property of a DbBEQualifierLink's instance.
     * 
     * @return the "rate" property
     **/
    public final Double getRate2() throws DbException {
        return (Double) get(fRate2);
    }

    /**
     * Gets the "rate unit" property of a DbBEQualifierLink's instance.
     * 
     * @return the "rate unit" property
     **/
    public final String getRateUnit() throws DbException {
        return (String) get(fRateUnit);
    }

    /**
     * Gets the "description" property of a DbBEQualifierLink's instance.
     * 
     * @return the "description" property
     **/
    public final String getDescription() throws DbException {
        return (String) get(fDescription);
    }

    /**
     * Gets the "rate (obsolete)" property of a DbBEQualifierLink's instance.
     * 
     * @return the "rate (obsolete)" property
     **/
    public final String getRate() throws DbException {
        return (String) get(fRate);
    }

}
