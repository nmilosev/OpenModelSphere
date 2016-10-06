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
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMExceptClause.html"
 * >DbIBMExceptClause</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbIBMBufferPool extends DbSMSSemanticalObject {

    //Meta

    public static final MetaField fDeferredClause = new MetaField(LocaleMgr.db
            .getString("deferredClause"));
    public static final MetaField fOnAllDbPartitionNums = new MetaField(LocaleMgr.db
            .getString("DbIBMBufferPool.onAllDbPartitionNums"));
    public static final MetaField fSize = new MetaField(LocaleMgr.db.getString("size"));
    public static final MetaField fNbBlockPages = new MetaField(LocaleMgr.db
            .getString("nbBlockPages"));
    public static final MetaField fBlockSize = new MetaField(LocaleMgr.db.getString("blockSize"));
    public static final MetaField fPageSize = new MetaField(LocaleMgr.db.getString("pageSize"));
    public static final MetaField fPageSizeUnit = new MetaField(LocaleMgr.db
            .getString("DbIBMBufferPool.pageSizeUnit"));
    public static final MetaField fExtendedStorage = new MetaField(LocaleMgr.db
            .getString("extendedStorage"));
    public static final MetaRelationN fTablespaces = new MetaRelationN(LocaleMgr.db
            .getString("tablespaces"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fDbIBMDbPartitionGroups = new MetaRelationN(LocaleMgr.db
            .getString("dbIBMDbPartitionGroups"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbIBMBufferPool"), DbIBMBufferPool.class, new MetaField[] {
            fDeferredClause, fOnAllDbPartitionNums, fSize, fNbBlockPages, fBlockSize, fPageSize,
            fPageSizeUnit, fExtendedStorage, fTablespaces, fDbIBMDbPartitionGroups }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbIBMExceptClause.metaClass });
            metaClass.setIcon("dbibmbufferpool.gif");

            fDeferredClause.setJField(DbIBMBufferPool.class.getDeclaredField("m_deferredClause"));
            fOnAllDbPartitionNums.setJField(DbIBMBufferPool.class
                    .getDeclaredField("m_onAllDbPartitionNums"));
            fSize.setJField(DbIBMBufferPool.class.getDeclaredField("m_size"));
            fNbBlockPages.setJField(DbIBMBufferPool.class.getDeclaredField("m_nbBlockPages"));
            fBlockSize.setJField(DbIBMBufferPool.class.getDeclaredField("m_blockSize"));
            fPageSize.setJField(DbIBMBufferPool.class.getDeclaredField("m_pageSize"));
            fPageSizeUnit.setJField(DbIBMBufferPool.class.getDeclaredField("m_pageSizeUnit"));
            fPageSizeUnit.setVisibleInScreen(false);
            fExtendedStorage.setJField(DbIBMBufferPool.class.getDeclaredField("m_extendedStorage"));
            fTablespaces.setJField(DbIBMBufferPool.class.getDeclaredField("m_tablespaces"));
            fDbIBMDbPartitionGroups.setJField(DbIBMBufferPool.class
                    .getDeclaredField("m_dbIBMDbPartitionGroups"));

            fDbIBMDbPartitionGroups.setOppositeRel(DbIBMDbPartitionGroup.fDbIBMBufferPools);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    IBMBufferPoolDeferred m_deferredClause;
    boolean m_onAllDbPartitionNums;
    int m_size;
    int m_nbBlockPages;
    int m_blockSize;
    int m_pageSize;
    IBMSizeUnit m_pageSizeUnit;
    IBMBufferPoolExtendedStorage m_extendedStorage;
    DbRelationN m_tablespaces;
    DbRelationN m_dbIBMDbPartitionGroups;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbIBMBufferPool() {
    }

    /**
     * Creates an instance of DbIBMBufferPool.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbIBMBufferPool(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        org.modelsphere.sms.or.ibm.db.util.IBMUtility.getInstance().setInitialValues(this);
    }

    //Setters

    /**
     * Sets the "deferrable" property of a DbIBMBufferPool's instance.
     * 
     * @param value
     *            the "deferrable" property
     **/
    public final void setDeferredClause(IBMBufferPoolDeferred value) throws DbException {
        basicSet(fDeferredClause, value);
    }

    /**
     * Sets the "on all database partition numbers" property of a DbIBMBufferPool's instance.
     * 
     * @param value
     *            the "on all database partition numbers" property
     **/
    public final void setOnAllDbPartitionNums(Boolean value) throws DbException {
        basicSet(fOnAllDbPartitionNums, value);
    }

    /**
     * Sets the "size" property of a DbIBMBufferPool's instance.
     * 
     * @param value
     *            the "size" property
     **/
    public final void setSize(Integer value) throws DbException {
        basicSet(fSize, value);
    }

    /**
     * Sets the "number of block pages" property of a DbIBMBufferPool's instance.
     * 
     * @param value
     *            the "number of block pages" property
     **/
    public final void setNbBlockPages(Integer value) throws DbException {
        basicSet(fNbBlockPages, value);
    }

    /**
     * Sets the "block size" property of a DbIBMBufferPool's instance.
     * 
     * @param value
     *            the "block size" property
     **/
    public final void setBlockSize(Integer value) throws DbException {
        basicSet(fBlockSize, value);
    }

    /**
     * Sets the "page size" property of a DbIBMBufferPool's instance.
     * 
     * @param value
     *            the "page size" property
     **/
    public final void setPageSize(Integer value) throws DbException {
        basicSet(fPageSize, value);
    }

    /**
     * Sets the "page size unit" property of a DbIBMBufferPool's instance.
     * 
     * @param value
     *            the "page size unit" property
     **/
    public final void setPageSizeUnit(IBMSizeUnit value) throws DbException {
        basicSet(fPageSizeUnit, value);
    }

    /**
     * Sets the "extended storage" property of a DbIBMBufferPool's instance.
     * 
     * @param value
     *            the "extended storage" property
     **/
    public final void setExtendedStorage(IBMBufferPoolExtendedStorage value) throws DbException {
        basicSet(fExtendedStorage, value);
    }

    /**
     * Adds an element to or removes an element from the list of database partition groups
     * associated to a DbIBMBufferPool's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setDbIBMDbPartitionGroups(DbIBMDbPartitionGroup value, int op)
            throws DbException {
        setRelationNN(fDbIBMDbPartitionGroups, value, op);
    }

    /**
     * Adds an element to the list of database partition groups associated to a DbIBMBufferPool's
     * instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToDbIBMDbPartitionGroups(DbIBMDbPartitionGroup value) throws DbException {
        setDbIBMDbPartitionGroups(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of database partition groups associated to a
     * DbIBMBufferPool's instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromDbIBMDbPartitionGroups(DbIBMDbPartitionGroup value)
            throws DbException {
        setDbIBMDbPartitionGroups(value, Db.REMOVE_FROM_RELN);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fTablespaces)
                ((DbIBMTablespace) value).setBufferPool(this);
            else if (metaField == fDbIBMDbPartitionGroups)
                setDbIBMDbPartitionGroups((DbIBMDbPartitionGroup) value, Db.ADD_TO_RELN);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fDbIBMDbPartitionGroups)
            setDbIBMDbPartitionGroups((DbIBMDbPartitionGroup) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the "deferrable" of a DbIBMBufferPool's instance.
     * 
     * @return the "deferrable"
     **/
    public final IBMBufferPoolDeferred getDeferredClause() throws DbException {
        return (IBMBufferPoolDeferred) get(fDeferredClause);
    }

    /**
     * Gets the "on all database partition numbers" property's Boolean value of a DbIBMBufferPool's
     * instance.
     * 
     * @return the "on all database partition numbers" property's Boolean value
     * @deprecated use isOnAllDbPartitionNums() method instead
     **/
    public final Boolean getOnAllDbPartitionNums() throws DbException {
        return (Boolean) get(fOnAllDbPartitionNums);
    }

    /**
     * Tells whether a DbIBMBufferPool's instance is onAllDbPartitionNums or not.
     * 
     * @return boolean
     **/
    public final boolean isOnAllDbPartitionNums() throws DbException {
        return getOnAllDbPartitionNums().booleanValue();
    }

    /**
     * Gets the "size" property of a DbIBMBufferPool's instance.
     * 
     * @return the "size" property
     **/
    public final Integer getSize() throws DbException {
        return (Integer) get(fSize);
    }

    /**
     * Gets the "number of block pages" property of a DbIBMBufferPool's instance.
     * 
     * @return the "number of block pages" property
     **/
    public final Integer getNbBlockPages() throws DbException {
        return (Integer) get(fNbBlockPages);
    }

    /**
     * Gets the "block size" property of a DbIBMBufferPool's instance.
     * 
     * @return the "block size" property
     **/
    public final Integer getBlockSize() throws DbException {
        return (Integer) get(fBlockSize);
    }

    /**
     * Gets the "page size" property of a DbIBMBufferPool's instance.
     * 
     * @return the "page size" property
     **/
    public final Integer getPageSize() throws DbException {
        return (Integer) get(fPageSize);
    }

    /**
     * Gets the "page size unit" of a DbIBMBufferPool's instance.
     * 
     * @return the "page size unit"
     **/
    public final IBMSizeUnit getPageSizeUnit() throws DbException {
        return (IBMSizeUnit) get(fPageSizeUnit);
    }

    /**
     * Gets the "extended storage" of a DbIBMBufferPool's instance.
     * 
     * @return the "extended storage"
     **/
    public final IBMBufferPoolExtendedStorage getExtendedStorage() throws DbException {
        return (IBMBufferPoolExtendedStorage) get(fExtendedStorage);
    }

    /**
     * Gets the list of tablespaces associated to a DbIBMBufferPool's instance.
     * 
     * @return the list of tablespaces.
     **/
    public final DbRelationN getTablespaces() throws DbException {
        return (DbRelationN) get(fTablespaces);
    }

    /**
     * Gets the list of database partition groups associated to a DbIBMBufferPool's instance.
     * 
     * @return the list of database partition groups.
     **/
    public final DbRelationN getDbIBMDbPartitionGroups() throws DbException {
        return (DbRelationN) get(fDbIBMDbPartitionGroups);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
