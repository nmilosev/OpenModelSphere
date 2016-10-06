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
import org.modelsphere.sms.SMSFilter;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEModel.html">DbBEModel</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbBEResource extends DbSMSSemanticalObject {

    //Meta

    public static final MetaRelationN fProcessUsages = new MetaRelationN(LocaleMgr.db
            .getString("processUsages"), 0, MetaRelationN.cardN);
    public static final MetaField fIdentifier = new MetaField(LocaleMgr.db.getString("identifier"));
    public static final MetaField fCategory = new MetaField(LocaleMgr.db.getString("category"));
    public static final MetaField fUser = new MetaField(LocaleMgr.db.getString("user"));
    public static final MetaField fCost = new MetaField(LocaleMgr.db.getString("cost"));
    public static final MetaField fCostTimeUnit = new MetaField(LocaleMgr.db
            .getString("costTimeUnit"));
    public static final MetaField fWorkLoad = new MetaField(LocaleMgr.db.getString("workLoad"));
    public static final MetaField fWorkLoadTimeUnit = new MetaField(LocaleMgr.db
            .getString("workLoadTimeUnit"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbBEResource"),
            DbBEResource.class, new MetaField[] { fProcessUsages, fIdentifier, fCategory, fUser,
                    fCost, fCostTimeUnit, fWorkLoad, fWorkLoadTimeUnit },
            MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);
            metaClass.setIcon("dbberesource.gif");

            fProcessUsages.setJField(DbBEResource.class.getDeclaredField("m_processUsages"));
            fIdentifier.setJField(DbBEResource.class.getDeclaredField("m_identifier"));
            fCategory.setJField(DbBEResource.class.getDeclaredField("m_category"));
            fUser.setJField(DbBEResource.class.getDeclaredField("m_user"));
            fCost.setJField(DbBEResource.class.getDeclaredField("m_cost"));
            fCostTimeUnit.setJField(DbBEResource.class.getDeclaredField("m_costTimeUnit"));
            fWorkLoad.setJField(DbBEResource.class.getDeclaredField("m_workLoad"));
            fWorkLoad.setEditable(false);
            fWorkLoadTimeUnit.setJField(DbBEResource.class.getDeclaredField("m_workLoadTimeUnit"));
            fWorkLoadTimeUnit.setEditable(false);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbRelationN m_processUsages;
    String m_identifier;
    String m_category;
    String m_user;
    SrDouble m_cost;
    BETimeUnit m_costTimeUnit;
    SrDouble m_workLoad;
    BETimeUnit m_workLoadTimeUnit;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBEResource() {
    }

    /**
     * Creates an instance of DbBEResource.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbBEResource(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setName(LocaleMgr.misc.getString("resource"));

    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.BPM;

    }

    //Setters

    /**
     * Sets the "identifier" property of a DbBEResource's instance.
     * 
     * @param value
     *            the "identifier" property
     **/
    public final void setIdentifier(String value) throws DbException {
        basicSet(fIdentifier, value);
    }

    /**
     * Sets the "category" property of a DbBEResource's instance.
     * 
     * @param value
     *            the "category" property
     **/
    public final void setCategory(String value) throws DbException {
        basicSet(fCategory, value);
    }

    /**
     * Sets the "user" property of a DbBEResource's instance.
     * 
     * @param value
     *            the "user" property
     **/
    public final void setUser(String value) throws DbException {
        basicSet(fUser, value);
    }

    /**
     * Sets the "cost" property of a DbBEResource's instance.
     * 
     * @param value
     *            the "cost" property
     **/
    public final void setCost(Double value) throws DbException {
        basicSet(fCost, value);
    }

    /**
     * Sets the "cost time unit" property of a DbBEResource's instance.
     * 
     * @param value
     *            the "cost time unit" property
     **/
    public final void setCostTimeUnit(BETimeUnit value) throws DbException {
        basicSet(fCostTimeUnit, value);
    }

    /**
     * Sets the "work load" property of a DbBEResource's instance.
     * 
     * @param value
     *            the "work load" property
     **/
    public final void setWorkLoad(Double value) throws DbException {
        basicSet(fWorkLoad, value);
    }

    /**
     * Sets the "work load time unit" property of a DbBEResource's instance.
     * 
     * @param value
     *            the "work load time unit" property
     **/
    public final void setWorkLoadTimeUnit(BETimeUnit value) throws DbException {
        basicSet(fWorkLoadTimeUnit, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fProcessUsages)
                ((DbBEUseCaseResource) value).setResource(this);
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
     * Gets the list of processes associated to a DbBEResource's instance.
     * 
     * @return the list of processes.
     **/
    public final DbRelationN getProcessUsages() throws DbException {
        return (DbRelationN) get(fProcessUsages);
    }

    /**
     * Gets the "identifier" property of a DbBEResource's instance.
     * 
     * @return the "identifier" property
     **/
    public final String getIdentifier() throws DbException {
        return (String) get(fIdentifier);
    }

    /**
     * Gets the "category" property of a DbBEResource's instance.
     * 
     * @return the "category" property
     **/
    public final String getCategory() throws DbException {
        return (String) get(fCategory);
    }

    /**
     * Gets the "user" property of a DbBEResource's instance.
     * 
     * @return the "user" property
     **/
    public final String getUser() throws DbException {
        return (String) get(fUser);
    }

    /**
     * Gets the "cost" of a DbBEResource's instance.
     * 
     * @return the "cost"
     **/
    public final Double getCost() throws DbException {
        return (Double) get(fCost);
    }

    /**
     * Gets the "cost time unit" of a DbBEResource's instance.
     * 
     * @return the "cost time unit"
     **/
    public final BETimeUnit getCostTimeUnit() throws DbException {
        return (BETimeUnit) get(fCostTimeUnit);
    }

    /**
     * Gets the "work load" of a DbBEResource's instance.
     * 
     * @return the "work load"
     **/
    public final Double getWorkLoad() throws DbException {
        return (Double) get(fWorkLoad);
    }

    /**
     * Gets the "work load time unit" of a DbBEResource's instance.
     * 
     * @return the "work load time unit"
     **/
    public final BETimeUnit getWorkLoadTimeUnit() throws DbException {
        return (BETimeUnit) get(fWorkLoadTimeUnit);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
