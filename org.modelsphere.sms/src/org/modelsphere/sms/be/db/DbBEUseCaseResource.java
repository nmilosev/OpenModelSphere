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
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEUseCase.html">DbBEUseCase</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbBEUseCaseResource extends DbObject {

    //Meta

    public static final MetaField fRole = new MetaField(LocaleMgr.db.getString("role"));
    public static final MetaField fUsageRate = new MetaField(LocaleMgr.db.getString("usageRate"));
    public static final MetaField fUsageRateTimeUnit = new MetaField(LocaleMgr.db
            .getString("usageRateTimeUnit"));
    public static final MetaField fDescription = new MetaField(LocaleMgr.db
            .getString("description"));
    public static final MetaRelation1 fResource = new MetaRelation1(LocaleMgr.db
            .getString("resource"), 1);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbBEUseCaseResource"), DbBEUseCaseResource.class, new MetaField[] { fRole,
            fUsageRate, fUsageRateTimeUnit, fDescription, fResource },
            MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbObject.metaClass);
            metaClass.setIcon("dbbeusecaseresource.gif");

            fRole.setJField(DbBEUseCaseResource.class.getDeclaredField("m_role"));
            fUsageRate.setJField(DbBEUseCaseResource.class.getDeclaredField("m_usageRate"));
            fUsageRateTimeUnit.setJField(DbBEUseCaseResource.class
                    .getDeclaredField("m_usageRateTimeUnit"));
            fDescription.setJField(DbBEUseCaseResource.class.getDeclaredField("m_description"));
            fDescription.setRendererPluginName("LookupDescription");
            fResource.setJField(DbBEUseCaseResource.class.getDeclaredField("m_resource"));
            fResource.setFlags(MetaField.COPY_REFS);
            fResource.setScreenOrder("<role");
            fResource.setRendererPluginName("DbBEResource");
            fResource.setEditable(false);

            fResource.setOppositeRel(DbBEResource.fProcessUsages);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    String m_role;
    SrDouble m_usageRate;
    BETimeUnit m_usageRateTimeUnit;
    String m_description;
    DbBEResource m_resource;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBEUseCaseResource() {
    }

    /**
     * Creates an instance of DbBEUseCaseResource.
     * 
     * @param composite
     *            org.modelsphere.jack.baseDb.db.DbObject
     * @param resource
     *            org.modelsphere.sms.be.db.DbBEResource
     **/
    public DbBEUseCaseResource(DbObject composite, DbBEResource resource) throws DbException {
        super(composite);
        basicSet(fResource, resource);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @param form
     *            int
     * @return string
     **/
    public final String getSemanticalName(int form) throws DbException {
        DbBEResource resource = getResource();
        return (resource != null ? resource.getName() : getRole());
    }

    /**
     * @return string
     **/
    public final String getName() throws DbException {
        DbBEResource resource = getResource();
        return (resource != null ? resource.getName() : getRole());
    }

    //Setters

    /**
     * Sets the "role" property of a DbBEUseCaseResource's instance.
     * 
     * @param value
     *            the "role" property
     **/
    public final void setRole(String value) throws DbException {
        basicSet(fRole, value);
    }

    /**
     * Sets the "usage rate" property of a DbBEUseCaseResource's instance.
     * 
     * @param value
     *            the "usage rate" property
     **/
    public final void setUsageRate(Double value) throws DbException {
        basicSet(fUsageRate, value);
    }

    /**
     * Sets the "usage rate time unit" property of a DbBEUseCaseResource's instance.
     * 
     * @param value
     *            the "usage rate time unit" property
     **/
    public final void setUsageRateTimeUnit(BETimeUnit value) throws DbException {
        basicSet(fUsageRateTimeUnit, value);
    }

    /**
     * Sets the "description" property of a DbBEUseCaseResource's instance.
     * 
     * @param value
     *            the "description" property
     **/
    public final void setDescription(String value) throws DbException {
        basicSet(fDescription, value);
    }

    /**
     * Sets the resource object associated to a DbBEUseCaseResource's instance.
     * 
     * @param value
     *            the resource object to be associated
     **/
    public final void setResource(DbBEResource value) throws DbException {
        basicSet(fResource, value);
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
     * Gets the "role" property of a DbBEUseCaseResource's instance.
     * 
     * @return the "role" property
     **/
    public final String getRole() throws DbException {
        return (String) get(fRole);
    }

    /**
     * Gets the "usage rate" of a DbBEUseCaseResource's instance.
     * 
     * @return the "usage rate"
     **/
    public final Double getUsageRate() throws DbException {
        return (Double) get(fUsageRate);
    }

    /**
     * Gets the "usage rate time unit" of a DbBEUseCaseResource's instance.
     * 
     * @return the "usage rate time unit"
     **/
    public final BETimeUnit getUsageRateTimeUnit() throws DbException {
        return (BETimeUnit) get(fUsageRateTimeUnit);
    }

    /**
     * Gets the "description" property of a DbBEUseCaseResource's instance.
     * 
     * @return the "description" property
     **/
    public final String getDescription() throws DbException {
        return (String) get(fDescription);
    }

    /**
     * Gets the resource object associated to a DbBEUseCaseResource's instance.
     * 
     * @return the resource object
     **/
    public final DbBEResource getResource() throws DbException {
        return (DbBEResource) get(fResource);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
