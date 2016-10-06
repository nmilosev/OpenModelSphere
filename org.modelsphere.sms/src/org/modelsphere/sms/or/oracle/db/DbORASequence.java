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
package org.modelsphere.sms.or.oracle.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.oracle.db.srtypes.*;
import org.modelsphere.sms.or.oracle.international.LocaleMgr;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORADataModel.html">DbORADataModel</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORASequence extends DbSMSSemanticalObject {

    //Meta

    public static final MetaField fStartValue = new MetaField(LocaleMgr.db.getString("startValue"));
    public static final MetaField fMinValue = new MetaField(LocaleMgr.db.getString("minValue"));
    public static final MetaField fMaxValue = new MetaField(LocaleMgr.db.getString("maxValue"));
    public static final MetaField fIncrement = new MetaField(LocaleMgr.db.getString("increment"));
    public static final MetaField fCycle = new MetaField(LocaleMgr.db.getString("cycle"));
    public static final MetaField fCache = new MetaField(LocaleMgr.db.getString("cache"));
    public static final MetaField fCacheValue = new MetaField(LocaleMgr.db.getString("cacheValue"));
    public static final MetaField fOrder = new MetaField(LocaleMgr.db.getString("order"));
    public static final MetaRelation1 fUser = new MetaRelation1(LocaleMgr.db.getString("user"), 0);

    public static final MetaClass metaClass = new MetaClass(
            LocaleMgr.db.getString("DbORASequence"), DbORASequence.class, new MetaField[] {
                    fStartValue, fMinValue, fMaxValue, fIncrement, fCycle, fCache, fCacheValue,
                    fOrder, fUser }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);
            metaClass.setIcon("dborasequence.gif");

            fStartValue.setJField(DbORASequence.class.getDeclaredField("m_startValue"));
            fMinValue.setJField(DbORASequence.class.getDeclaredField("m_minValue"));
            fMaxValue.setJField(DbORASequence.class.getDeclaredField("m_maxValue"));
            fIncrement.setJField(DbORASequence.class.getDeclaredField("m_increment"));
            fCycle.setJField(DbORASequence.class.getDeclaredField("m_cycle"));
            fCache.setJField(DbORASequence.class.getDeclaredField("m_cache"));
            fCacheValue.setJField(DbORASequence.class.getDeclaredField("m_cacheValue"));
            fOrder.setJField(DbORASequence.class.getDeclaredField("m_order"));
            fUser.setJField(DbORASequence.class.getDeclaredField("m_user"));
            fUser.setFlags(MetaField.COPY_REFS);

            fUser.setOppositeRel(DbORUser.fSequences);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    String m_startValue;
    String m_minValue;
    String m_maxValue;
    String m_increment;
    boolean m_cycle;
    boolean m_cache;
    SrInteger m_cacheValue;
    boolean m_order;
    DbORUser m_user;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORASequence() {
    }

    /**
     * Creates an instance of DbORASequence.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORASequence(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setCycle(Boolean.FALSE);
        setCache(Boolean.FALSE);
        setOrder(Boolean.FALSE);
        setName(LocaleMgr.misc.getString("sequence"));
    }

    //Setters

    /**
     * Sets the "start value" property of a DbORASequence's instance.
     * 
     * @param value
     *            the "start value" property
     **/
    public final void setStartValue(String value) throws DbException {
        basicSet(fStartValue, value);
    }

    /**
     * Sets the "minimum value" property of a DbORASequence's instance.
     * 
     * @param value
     *            the "minimum value" property
     **/
    public final void setMinValue(String value) throws DbException {
        basicSet(fMinValue, value);
    }

    /**
     * Sets the "maximum value" property of a DbORASequence's instance.
     * 
     * @param value
     *            the "maximum value" property
     **/
    public final void setMaxValue(String value) throws DbException {
        basicSet(fMaxValue, value);
    }

    /**
     * Sets the "increment" property of a DbORASequence's instance.
     * 
     * @param value
     *            the "increment" property
     **/
    public final void setIncrement(String value) throws DbException {
        basicSet(fIncrement, value);
    }

    /**
     * Sets the "cycle" property of a DbORASequence's instance.
     * 
     * @param value
     *            the "cycle" property
     **/
    public final void setCycle(Boolean value) throws DbException {
        basicSet(fCycle, value);
    }

    /**
     * Sets the "cache" property of a DbORASequence's instance.
     * 
     * @param value
     *            the "cache" property
     **/
    public final void setCache(Boolean value) throws DbException {
        basicSet(fCache, value);
    }

    /**
     * Sets the "cache value" property of a DbORASequence's instance.
     * 
     * @param value
     *            the "cache value" property
     **/
    public final void setCacheValue(Integer value) throws DbException {
        basicSet(fCacheValue, value);
    }

    /**
     * Sets the "order" property of a DbORASequence's instance.
     * 
     * @param value
     *            the "order" property
     **/
    public final void setOrder(Boolean value) throws DbException {
        basicSet(fOrder, value);
    }

    /**
     * Sets the user object associated to a DbORASequence's instance.
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
     * Gets the "start value" property of a DbORASequence's instance.
     * 
     * @return the "start value" property
     **/
    public final String getStartValue() throws DbException {
        return (String) get(fStartValue);
    }

    /**
     * Gets the "minimum value" property of a DbORASequence's instance.
     * 
     * @return the "minimum value" property
     **/
    public final String getMinValue() throws DbException {
        return (String) get(fMinValue);
    }

    /**
     * Gets the "maximum value" property of a DbORASequence's instance.
     * 
     * @return the "maximum value" property
     **/
    public final String getMaxValue() throws DbException {
        return (String) get(fMaxValue);
    }

    /**
     * Gets the "increment" property of a DbORASequence's instance.
     * 
     * @return the "increment" property
     **/
    public final String getIncrement() throws DbException {
        return (String) get(fIncrement);
    }

    /**
     * Gets the "cycle" property's Boolean value of a DbORASequence's instance.
     * 
     * @return the "cycle" property's Boolean value
     * @deprecated use isCycle() method instead
     **/
    public final Boolean getCycle() throws DbException {
        return (Boolean) get(fCycle);
    }

    /**
     * Tells whether a DbORASequence's instance is cycle or not.
     * 
     * @return boolean
     **/
    public final boolean isCycle() throws DbException {
        return getCycle().booleanValue();
    }

    /**
     * Gets the "cache" property's Boolean value of a DbORASequence's instance.
     * 
     * @return the "cache" property's Boolean value
     * @deprecated use isCache() method instead
     **/
    public final Boolean getCache() throws DbException {
        return (Boolean) get(fCache);
    }

    /**
     * Tells whether a DbORASequence's instance is cache or not.
     * 
     * @return boolean
     **/
    public final boolean isCache() throws DbException {
        return getCache().booleanValue();
    }

    /**
     * Gets the "cache value" of a DbORASequence's instance.
     * 
     * @return the "cache value"
     **/
    public final Integer getCacheValue() throws DbException {
        return (Integer) get(fCacheValue);
    }

    /**
     * Gets the "order" property's Boolean value of a DbORASequence's instance.
     * 
     * @return the "order" property's Boolean value
     * @deprecated use isOrder() method instead
     **/
    public final Boolean getOrder() throws DbException {
        return (Boolean) get(fOrder);
    }

    /**
     * Tells whether a DbORASequence's instance is order or not.
     * 
     * @return boolean
     **/
    public final boolean isOrder() throws DbException {
        return getOrder().booleanValue();
    }

    /**
     * Gets the user object associated to a DbORASequence's instance.
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
