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
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMContainerClause.html"
 * >DbIBMContainerClause</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbIBMTablespace extends DbSMSSemanticalObject {

    //Meta

    public static final MetaField fType = new MetaField(LocaleMgr.db.getString("type"));
    public static final MetaRelation1 fDbPartitionGroup = new MetaRelation1(LocaleMgr.db
            .getString("dbPartitionGroup"), 0);
    public static final MetaField fPageSize = new MetaField(LocaleMgr.db.getString("pageSize"));
    public static final MetaField fPageSizeUnit = new MetaField(LocaleMgr.db
            .getString("pageSizeUnit"));
    public static final MetaField fManagedBy = new MetaField(LocaleMgr.db.getString("managedBy"));
    public static final MetaField fManagedBySystemClause = new MetaField(LocaleMgr.db
            .getString("managedBySystemClause"));
    public static final MetaField fExtentSize = new MetaField(LocaleMgr.db.getString("extentSize"));
    public static final MetaField fExtentSizeUnit = new MetaField(LocaleMgr.db
            .getString("extentSizeUnit"));
    public static final MetaField fPrefetchSize = new MetaField(LocaleMgr.db
            .getString("prefetchSize"));
    public static final MetaField fPrefetchSizeUnit = new MetaField(LocaleMgr.db
            .getString("prefetchSizeUnit"));
    public static final MetaRelation1 fBufferPool = new MetaRelation1(LocaleMgr.db
            .getString("bufferPool"), 0);
    public static final MetaField fOverhead = new MetaField(LocaleMgr.db.getString("overhead"));
    public static final MetaField fTransferRate = new MetaField(LocaleMgr.db
            .getString("transferRate"));
    public static final MetaField fDropTableRecovery = new MetaField(LocaleMgr.db
            .getString("dropTableRecovery"));
    public static final MetaRelationN fTables = new MetaRelationN(LocaleMgr.db.getString("tables"),
            0, MetaRelationN.cardN);
    public static final MetaRelationN fTablesForLOB = new MetaRelationN(LocaleMgr.db
            .getString("tablesForLOB"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fTablesForIndexes = new MetaRelationN(LocaleMgr.db
            .getString("tablesForIndexes"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbIBMTablespace"), DbIBMTablespace.class, new MetaField[] { fType,
            fDbPartitionGroup, fPageSize, fPageSizeUnit, fManagedBy, fManagedBySystemClause,
            fExtentSize, fExtentSizeUnit, fPrefetchSize, fPrefetchSizeUnit, fBufferPool, fOverhead,
            fTransferRate, fDropTableRecovery, fTables, fTablesForLOB, fTablesForIndexes }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbIBMContainerClause.metaClass });
            metaClass.setIcon("dbibmtablespace.gif");

            fType.setJField(DbIBMTablespace.class.getDeclaredField("m_type"));
            fDbPartitionGroup.setJField(DbIBMTablespace.class
                    .getDeclaredField("m_dbPartitionGroup"));
            fPageSize.setJField(DbIBMTablespace.class.getDeclaredField("m_pageSize"));
            fPageSizeUnit.setJField(DbIBMTablespace.class.getDeclaredField("m_pageSizeUnit"));
            fManagedBy.setJField(DbIBMTablespace.class.getDeclaredField("m_managedBy"));
            fManagedBySystemClause.setJField(DbIBMTablespace.class
                    .getDeclaredField("m_managedBySystemClause"));
            fManagedBySystemClause.setVisibleInScreen(false);
            fExtentSize.setJField(DbIBMTablespace.class.getDeclaredField("m_extentSize"));
            fExtentSizeUnit.setJField(DbIBMTablespace.class.getDeclaredField("m_extentSizeUnit"));
            fPrefetchSize.setJField(DbIBMTablespace.class.getDeclaredField("m_prefetchSize"));
            fPrefetchSizeUnit.setJField(DbIBMTablespace.class
                    .getDeclaredField("m_prefetchSizeUnit"));
            fBufferPool.setJField(DbIBMTablespace.class.getDeclaredField("m_bufferPool"));
            fOverhead.setJField(DbIBMTablespace.class.getDeclaredField("m_overhead"));
            fTransferRate.setJField(DbIBMTablespace.class.getDeclaredField("m_transferRate"));
            fDropTableRecovery.setJField(DbIBMTablespace.class
                    .getDeclaredField("m_dropTableRecovery"));
            fTables.setJField(DbIBMTablespace.class.getDeclaredField("m_tables"));
            fTablesForLOB.setJField(DbIBMTablespace.class.getDeclaredField("m_tablesForLOB"));
            fTablesForIndexes.setJField(DbIBMTablespace.class
                    .getDeclaredField("m_tablesForIndexes"));

            fDbPartitionGroup.setOppositeRel(DbIBMDbPartitionGroup.fTablespaces);
            fBufferPool.setOppositeRel(DbIBMBufferPool.fTablespaces);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    IBMTablespaceType m_type;
    DbIBMDbPartitionGroup m_dbPartitionGroup;
    int m_pageSize;
    IBMSizeUnit m_pageSizeUnit;
    IBMTablespaceManagedBy m_managedBy;
    String m_managedBySystemClause;
    int m_extentSize;
    IBMSizeUnit m_extentSizeUnit;
    int m_prefetchSize;
    IBMSizeUnit m_prefetchSizeUnit;
    DbIBMBufferPool m_bufferPool;
    double m_overhead;
    double m_transferRate;
    IBMDropTableRecovery m_dropTableRecovery;
    DbRelationN m_tables;
    DbRelationN m_tablesForLOB;
    DbRelationN m_tablesForIndexes;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbIBMTablespace() {
    }

    /**
     * Creates an instance of DbIBMTablespace.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbIBMTablespace(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        org.modelsphere.sms.or.ibm.db.util.IBMUtility.getInstance().setInitialValues(this);
    }

    //Setters

    /**
     * Sets the "type" property of a DbIBMTablespace's instance.
     * 
     * @param value
     *            the "type" property
     **/
    public final void setType(IBMTablespaceType value) throws DbException {
        basicSet(fType, value);
    }

    /**
     * Sets the database partition group object associated to a DbIBMTablespace's instance.
     * 
     * @param value
     *            the database partition group object to be associated
     **/
    public final void setDbPartitionGroup(DbIBMDbPartitionGroup value) throws DbException {
        basicSet(fDbPartitionGroup, value);
    }

    /**
     * Sets the "page size" property of a DbIBMTablespace's instance.
     * 
     * @param value
     *            the "page size" property
     **/
    public final void setPageSize(Integer value) throws DbException {
        basicSet(fPageSize, value);
    }

    /**
     * Sets the "page size units" property of a DbIBMTablespace's instance.
     * 
     * @param value
     *            the "page size units" property
     **/
    public final void setPageSizeUnit(IBMSizeUnit value) throws DbException {
        basicSet(fPageSizeUnit, value);
    }

    /**
     * Sets the "managed by" property of a DbIBMTablespace's instance.
     * 
     * @param value
     *            the "managed by" property
     **/
    public final void setManagedBy(IBMTablespaceManagedBy value) throws DbException {
        basicSet(fManagedBy, value);
    }

    /**
     * Sets the "clause managed by" property of a DbIBMTablespace's instance.
     * 
     * @param value
     *            the "clause managed by" property
     **/
    public final void setManagedBySystemClause(String value) throws DbException {
        basicSet(fManagedBySystemClause, value);
    }

    /**
     * Sets the "extent size" property of a DbIBMTablespace's instance.
     * 
     * @param value
     *            the "extent size" property
     **/
    public final void setExtentSize(Integer value) throws DbException {
        basicSet(fExtentSize, value);
    }

    /**
     * Sets the "extent size units" property of a DbIBMTablespace's instance.
     * 
     * @param value
     *            the "extent size units" property
     **/
    public final void setExtentSizeUnit(IBMSizeUnit value) throws DbException {
        basicSet(fExtentSizeUnit, value);
    }

    /**
     * Sets the "prefetch size" property of a DbIBMTablespace's instance.
     * 
     * @param value
     *            the "prefetch size" property
     **/
    public final void setPrefetchSize(Integer value) throws DbException {
        basicSet(fPrefetchSize, value);
    }

    /**
     * Sets the "prefetch size units" property of a DbIBMTablespace's instance.
     * 
     * @param value
     *            the "prefetch size units" property
     **/
    public final void setPrefetchSizeUnit(IBMSizeUnit value) throws DbException {
        basicSet(fPrefetchSizeUnit, value);
    }

    /**
     * Sets the buffer pool object associated to a DbIBMTablespace's instance.
     * 
     * @param value
     *            the buffer pool object to be associated
     **/
    public final void setBufferPool(DbIBMBufferPool value) throws DbException {
        basicSet(fBufferPool, value);
    }

    /**
     * Sets the "overhead" property of a DbIBMTablespace's instance.
     * 
     * @param value
     *            the "overhead" property
     **/
    public final void setOverhead(Double value) throws DbException {
        basicSet(fOverhead, value);
    }

    /**
     * Sets the "transfer rate" property of a DbIBMTablespace's instance.
     * 
     * @param value
     *            the "transfer rate" property
     **/
    public final void setTransferRate(Double value) throws DbException {
        basicSet(fTransferRate, value);
    }

    /**
     * Sets the "drop table recovery" property of a DbIBMTablespace's instance.
     * 
     * @param value
     *            the "drop table recovery" property
     **/
    public final void setDropTableRecovery(IBMDropTableRecovery value) throws DbException {
        basicSet(fDropTableRecovery, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fTables)
                ((DbIBMTable) value).setInTablespace(this);
            else if (metaField == fTablesForLOB)
                ((DbIBMTable) value).setLongInTablespace(this);
            else if (metaField == fTablesForIndexes)
                ((DbIBMTable) value).setIndexInTablespace(this);
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
     * Gets the "type" of a DbIBMTablespace's instance.
     * 
     * @return the "type"
     **/
    public final IBMTablespaceType getType() throws DbException {
        return (IBMTablespaceType) get(fType);
    }

    /**
     * Gets the database partition group object associated to a DbIBMTablespace's instance.
     * 
     * @return the database partition group object
     **/
    public final DbIBMDbPartitionGroup getDbPartitionGroup() throws DbException {
        return (DbIBMDbPartitionGroup) get(fDbPartitionGroup);
    }

    /**
     * Gets the "page size" property of a DbIBMTablespace's instance.
     * 
     * @return the "page size" property
     **/
    public final Integer getPageSize() throws DbException {
        return (Integer) get(fPageSize);
    }

    /**
     * Gets the "page size units" of a DbIBMTablespace's instance.
     * 
     * @return the "page size units"
     **/
    public final IBMSizeUnit getPageSizeUnit() throws DbException {
        return (IBMSizeUnit) get(fPageSizeUnit);
    }

    /**
     * Gets the "managed by" of a DbIBMTablespace's instance.
     * 
     * @return the "managed by"
     **/
    public final IBMTablespaceManagedBy getManagedBy() throws DbException {
        return (IBMTablespaceManagedBy) get(fManagedBy);
    }

    /**
     * Gets the "clause managed by" property of a DbIBMTablespace's instance.
     * 
     * @return the "clause managed by" property
     **/
    public final String getManagedBySystemClause() throws DbException {
        return (String) get(fManagedBySystemClause);
    }

    /**
     * Gets the "extent size" property of a DbIBMTablespace's instance.
     * 
     * @return the "extent size" property
     **/
    public final Integer getExtentSize() throws DbException {
        return (Integer) get(fExtentSize);
    }

    /**
     * Gets the "extent size units" of a DbIBMTablespace's instance.
     * 
     * @return the "extent size units"
     **/
    public final IBMSizeUnit getExtentSizeUnit() throws DbException {
        return (IBMSizeUnit) get(fExtentSizeUnit);
    }

    /**
     * Gets the "prefetch size" property of a DbIBMTablespace's instance.
     * 
     * @return the "prefetch size" property
     **/
    public final Integer getPrefetchSize() throws DbException {
        return (Integer) get(fPrefetchSize);
    }

    /**
     * Gets the "prefetch size units" of a DbIBMTablespace's instance.
     * 
     * @return the "prefetch size units"
     **/
    public final IBMSizeUnit getPrefetchSizeUnit() throws DbException {
        return (IBMSizeUnit) get(fPrefetchSizeUnit);
    }

    /**
     * Gets the buffer pool object associated to a DbIBMTablespace's instance.
     * 
     * @return the buffer pool object
     **/
    public final DbIBMBufferPool getBufferPool() throws DbException {
        return (DbIBMBufferPool) get(fBufferPool);
    }

    /**
     * Gets the "overhead" property of a DbIBMTablespace's instance.
     * 
     * @return the "overhead" property
     **/
    public final Double getOverhead() throws DbException {
        return (Double) get(fOverhead);
    }

    /**
     * Gets the "transfer rate" property of a DbIBMTablespace's instance.
     * 
     * @return the "transfer rate" property
     **/
    public final Double getTransferRate() throws DbException {
        return (Double) get(fTransferRate);
    }

    /**
     * Gets the "drop table recovery" of a DbIBMTablespace's instance.
     * 
     * @return the "drop table recovery"
     **/
    public final IBMDropTableRecovery getDropTableRecovery() throws DbException {
        return (IBMDropTableRecovery) get(fDropTableRecovery);
    }

    /**
     * Gets the list of linked tables associated to a DbIBMTablespace's instance.
     * 
     * @return the list of linked tables.
     **/
    public final DbRelationN getTables() throws DbException {
        return (DbRelationN) get(fTables);
    }

    /**
     * Gets the list of linked tables for long objects associated to a DbIBMTablespace's instance.
     * 
     * @return the list of linked tables for long objects.
     **/
    public final DbRelationN getTablesForLOB() throws DbException {
        return (DbRelationN) get(fTablesForLOB);
    }

    /**
     * Gets the list of linked tables for indexes associated to a DbIBMTablespace's instance.
     * 
     * @return the list of linked tables for indexes.
     **/
    public final DbRelationN getTablesForIndexes() throws DbException {
        return (DbRelationN) get(fTablesForIndexes);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
