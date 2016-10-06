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
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMDatabase.html">DbIBMDatabase</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbIBMDbPartitionGroup extends DbSMSSemanticalObject {

    //Meta

    public static final MetaRelationN fTablespaces = new MetaRelationN(LocaleMgr.db
            .getString("tablespaces"), 0, MetaRelationN.cardN);
    public static final MetaField fOnAllDbPartitionNums = new MetaField(LocaleMgr.db
            .getString("onAllDbPartitionNums"));
    public static final MetaField fDbPartitionNums = new MetaField(LocaleMgr.db
            .getString("dbPartitionNums"));
    public static final MetaRelationN fDbIBMBufferPools = new MetaRelationN(LocaleMgr.db
            .getString("dbIBMBufferPools"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbIBMDbPartitionGroup"), DbIBMDbPartitionGroup.class, new MetaField[] {
            fTablespaces, fOnAllDbPartitionNums, fDbPartitionNums, fDbIBMBufferPools }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);
            metaClass.setIcon("dbibmpartitiongroup.gif");

            fTablespaces.setJField(DbIBMDbPartitionGroup.class.getDeclaredField("m_tablespaces"));
            fOnAllDbPartitionNums.setJField(DbIBMDbPartitionGroup.class
                    .getDeclaredField("m_onAllDbPartitionNums"));
            fDbPartitionNums.setJField(DbIBMDbPartitionGroup.class
                    .getDeclaredField("m_dbPartitionNums"));
            fDbIBMBufferPools.setJField(DbIBMDbPartitionGroup.class
                    .getDeclaredField("m_dbIBMBufferPools"));

            fDbIBMBufferPools.setOppositeRel(DbIBMBufferPool.fDbIBMDbPartitionGroups);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbRelationN m_tablespaces;
    boolean m_onAllDbPartitionNums;
    String m_dbPartitionNums;
    DbRelationN m_dbIBMBufferPools;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbIBMDbPartitionGroup() {
    }

    /**
     * Creates an instance of DbIBMDbPartitionGroup.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbIBMDbPartitionGroup(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        org.modelsphere.sms.or.ibm.db.util.IBMUtility.getInstance().setInitialValues(this);
    }

    //Setters

    /**
     * Sets the "on all database partition numbers" property of a DbIBMDbPartitionGroup's instance.
     * 
     * @param value
     *            the "on all database partition numbers" property
     **/
    public final void setOnAllDbPartitionNums(Boolean value) throws DbException {
        basicSet(fOnAllDbPartitionNums, value);
    }

    /**
     * Sets the "database partition numbers" property of a DbIBMDbPartitionGroup's instance.
     * 
     * @param value
     *            the "database partition numbers" property
     **/
    public final void setDbPartitionNums(String value) throws DbException {
        basicSet(fDbPartitionNums, value);
    }

    /**
     * Adds an element to or removes an element from the list of buffer pools associated to a
     * DbIBMDbPartitionGroup's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setDbIBMBufferPools(DbIBMBufferPool value, int op) throws DbException {
        setRelationNN(fDbIBMBufferPools, value, op);
    }

    /**
     * Adds an element to the list of buffer pools associated to a DbIBMDbPartitionGroup's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToDbIBMBufferPools(DbIBMBufferPool value) throws DbException {
        setDbIBMBufferPools(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of buffer pools associated to a DbIBMDbPartitionGroup's
     * instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromDbIBMBufferPools(DbIBMBufferPool value) throws DbException {
        setDbIBMBufferPools(value, Db.REMOVE_FROM_RELN);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fTablespaces)
                ((DbIBMTablespace) value).setDbPartitionGroup(this);
            else if (metaField == fDbIBMBufferPools)
                setDbIBMBufferPools((DbIBMBufferPool) value, Db.ADD_TO_RELN);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fDbIBMBufferPools)
            setDbIBMBufferPools((DbIBMBufferPool) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the list of tablespaces associated to a DbIBMDbPartitionGroup's instance.
     * 
     * @return the list of tablespaces.
     **/
    public final DbRelationN getTablespaces() throws DbException {
        return (DbRelationN) get(fTablespaces);
    }

    /**
     * Gets the "on all database partition numbers" property's Boolean value of a
     * DbIBMDbPartitionGroup's instance.
     * 
     * @return the "on all database partition numbers" property's Boolean value
     * @deprecated use isOnAllDbPartitionNums() method instead
     **/
    public final Boolean getOnAllDbPartitionNums() throws DbException {
        return (Boolean) get(fOnAllDbPartitionNums);
    }

    /**
     * Tells whether a DbIBMDbPartitionGroup's instance is onAllDbPartitionNums or not.
     * 
     * @return boolean
     **/
    public final boolean isOnAllDbPartitionNums() throws DbException {
        return getOnAllDbPartitionNums().booleanValue();
    }

    /**
     * Gets the "database partition numbers" property of a DbIBMDbPartitionGroup's instance.
     * 
     * @return the "database partition numbers" property
     **/
    public final String getDbPartitionNums() throws DbException {
        return (String) get(fDbPartitionNums);
    }

    /**
     * Gets the list of buffer pools associated to a DbIBMDbPartitionGroup's instance.
     * 
     * @return the list of buffer pools.
     **/
    public final DbRelationN getDbIBMBufferPools() throws DbException {
        return (DbRelationN) get(fDbIBMBufferPools);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
