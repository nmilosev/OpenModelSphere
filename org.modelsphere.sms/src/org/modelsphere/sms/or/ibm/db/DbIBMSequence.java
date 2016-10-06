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
package org.modelsphere.sms.or.ibm.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.ibm.db.srtypes.*;
import org.modelsphere.sms.or.ibm.international.LocaleMgr;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMDataModel.html">DbIBMDataModel</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbIBMSequence extends DbSMSSemanticalObject {

    //Meta

    public static final MetaField fAs = new MetaField(LocaleMgr.db.getString("as"));
    public static final MetaField fStartWith = new MetaField(LocaleMgr.db.getString("startWith"));
    public static final MetaField fIncrementBy = new MetaField(LocaleMgr.db
            .getString("incrementBy"));
    public static final MetaField fMinValue = new MetaField(LocaleMgr.db.getString("minValue"));
    public static final MetaField fMaxValue = new MetaField(LocaleMgr.db.getString("maxValue"));
    public static final MetaField fCycle = new MetaField(LocaleMgr.db.getString("cycle"));
    public static final MetaField fOrder = new MetaField(LocaleMgr.db.getString("order"));
    public static final MetaField fCache = new MetaField(LocaleMgr.db.getString("cache"));
    public static final MetaRelation1 fUser = new MetaRelation1(LocaleMgr.db.getString("user"), 0);

    public static final MetaClass metaClass = new MetaClass(
            LocaleMgr.db.getString("DbIBMSequence"), DbIBMSequence.class,
            new MetaField[] { fAs, fStartWith, fIncrementBy, fMinValue, fMaxValue, fCycle, fOrder,
                    fCache, fUser }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);
            metaClass.setIcon("dbibmsequebce.gif");

            fAs.setJField(DbIBMSequence.class.getDeclaredField("m_as"));
            fStartWith.setJField(DbIBMSequence.class.getDeclaredField("m_startWith"));
            fIncrementBy.setJField(DbIBMSequence.class.getDeclaredField("m_incrementBy"));
            fMinValue.setJField(DbIBMSequence.class.getDeclaredField("m_minValue"));
            fMaxValue.setJField(DbIBMSequence.class.getDeclaredField("m_maxValue"));
            fCycle.setJField(DbIBMSequence.class.getDeclaredField("m_cycle"));
            fOrder.setJField(DbIBMSequence.class.getDeclaredField("m_order"));
            fCache.setJField(DbIBMSequence.class.getDeclaredField("m_cache"));
            fUser.setJField(DbIBMSequence.class.getDeclaredField("m_user"));

            fUser.setOppositeRel(DbORUser.fIbmSequences);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    IBMSequenceType m_as;
    int m_startWith;
    int m_incrementBy;
    int m_minValue;
    int m_maxValue;
    boolean m_cycle;
    boolean m_order;
    int m_cache;
    DbORUser m_user;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbIBMSequence() {
    }

    /**
     * Creates an instance of DbIBMSequence.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbIBMSequence(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        org.modelsphere.sms.or.ibm.db.util.IBMUtility.getInstance().setInitialValues(this);
    }

    //Setters

    /**
     * Sets the "as" property of a DbIBMSequence's instance.
     * 
     * @param value
     *            the "as" property
     **/
    public final void setAs(IBMSequenceType value) throws DbException {
        basicSet(fAs, value);
    }

    /**
     * Sets the "start with" property of a DbIBMSequence's instance.
     * 
     * @param value
     *            the "start with" property
     **/
    public final void setStartWith(Integer value) throws DbException {
        basicSet(fStartWith, value);
    }

    /**
     * Sets the "increment by" property of a DbIBMSequence's instance.
     * 
     * @param value
     *            the "increment by" property
     **/
    public final void setIncrementBy(Integer value) throws DbException {
        basicSet(fIncrementBy, value);
    }

    /**
     * Sets the "minimum value" property of a DbIBMSequence's instance.
     * 
     * @param value
     *            the "minimum value" property
     **/
    public final void setMinValue(Integer value) throws DbException {
        basicSet(fMinValue, value);
    }

    /**
     * Sets the "maximum value" property of a DbIBMSequence's instance.
     * 
     * @param value
     *            the "maximum value" property
     **/
    public final void setMaxValue(Integer value) throws DbException {
        basicSet(fMaxValue, value);
    }

    /**
     * Sets the "cycle" property of a DbIBMSequence's instance.
     * 
     * @param value
     *            the "cycle" property
     **/
    public final void setCycle(Boolean value) throws DbException {
        basicSet(fCycle, value);
    }

    /**
     * Sets the "order" property of a DbIBMSequence's instance.
     * 
     * @param value
     *            the "order" property
     **/
    public final void setOrder(Boolean value) throws DbException {
        basicSet(fOrder, value);
    }

    /**
     * Sets the "cache" property of a DbIBMSequence's instance.
     * 
     * @param value
     *            the "cache" property
     **/
    public final void setCache(Integer value) throws DbException {
        basicSet(fCache, value);
    }

    /**
     * Sets the user object associated to a DbIBMSequence's instance.
     * 
     * @param value
     *            the user object to be associated
     **/
    public final void setUser(DbORUser value) throws DbException {
        basicSet(fUser, value);
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
     * Gets the "as" of a DbIBMSequence's instance.
     * 
     * @return the "as"
     **/
    public final IBMSequenceType getAs() throws DbException {
        return (IBMSequenceType) get(fAs);
    }

    /**
     * Gets the "start with" property of a DbIBMSequence's instance.
     * 
     * @return the "start with" property
     **/
    public final Integer getStartWith() throws DbException {
        return (Integer) get(fStartWith);
    }

    /**
     * Gets the "increment by" property of a DbIBMSequence's instance.
     * 
     * @return the "increment by" property
     **/
    public final Integer getIncrementBy() throws DbException {
        return (Integer) get(fIncrementBy);
    }

    /**
     * Gets the "minimum value" property of a DbIBMSequence's instance.
     * 
     * @return the "minimum value" property
     **/
    public final Integer getMinValue() throws DbException {
        return (Integer) get(fMinValue);
    }

    /**
     * Gets the "maximum value" property of a DbIBMSequence's instance.
     * 
     * @return the "maximum value" property
     **/
    public final Integer getMaxValue() throws DbException {
        return (Integer) get(fMaxValue);
    }

    /**
     * Gets the "cycle" property's Boolean value of a DbIBMSequence's instance.
     * 
     * @return the "cycle" property's Boolean value
     * @deprecated use isCycle() method instead
     **/
    public final Boolean getCycle() throws DbException {
        return (Boolean) get(fCycle);
    }

    /**
     * Tells whether a DbIBMSequence's instance is cycle or not.
     * 
     * @return boolean
     **/
    public final boolean isCycle() throws DbException {
        return getCycle().booleanValue();
    }

    /**
     * Gets the "order" property's Boolean value of a DbIBMSequence's instance.
     * 
     * @return the "order" property's Boolean value
     * @deprecated use isOrder() method instead
     **/
    public final Boolean getOrder() throws DbException {
        return (Boolean) get(fOrder);
    }

    /**
     * Tells whether a DbIBMSequence's instance is order or not.
     * 
     * @return boolean
     **/
    public final boolean isOrder() throws DbException {
        return getOrder().booleanValue();
    }

    /**
     * Gets the "cache" property of a DbIBMSequence's instance.
     * 
     * @return the "cache" property
     **/
    public final Integer getCache() throws DbException {
        return (Integer) get(fCache);
    }

    /**
     * Gets the user object associated to a DbIBMSequence's instance.
     * 
     * @return the user object
     **/
    public final DbORUser getUser() throws DbException {
        return (DbORUser) get(fUser);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
