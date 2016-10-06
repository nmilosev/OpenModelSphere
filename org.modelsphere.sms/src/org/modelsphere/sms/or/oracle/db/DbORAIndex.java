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
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAAbsTable.html">DbORAAbsTable</A>,
 * <A HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAView.html">DbORAView</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAPartition.html">DbORAPartition</A>,
 * <A HREF="../../../../../../org/modelsphere/sms/or/db/DbORIndexKey.html">DbORIndexKey</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORAIndex extends DbORIndex {

    //Meta

    public static final MetaField fBitmap = new MetaField(LocaleMgr.db.getString("bitmap"));
    public static final MetaField fCategory = new MetaField(LocaleMgr.db.getString("category"));
    public static final MetaField fPctfree = new MetaField(LocaleMgr.db.getString("pctfree"));
    public static final MetaField fInitrans = new MetaField(LocaleMgr.db.getString("initrans"));
    public static final MetaField fMaxtrans = new MetaField(LocaleMgr.db.getString("maxtrans"));
    public static final MetaField fInitialExtent = new MetaField(LocaleMgr.db
            .getString("initialExtent"));
    public static final MetaField fInitialExtentSizeUnit = new MetaField(LocaleMgr.db
            .getString("initialExtentSizeUnit"));
    public static final MetaField fNextExtent = new MetaField(LocaleMgr.db.getString("nextExtent"));
    public static final MetaField fNextExtentSizeUnit = new MetaField(LocaleMgr.db
            .getString("nextExtentSizeUnit"));
    public static final MetaField fMinExtent = new MetaField(LocaleMgr.db.getString("minExtent"));
    public static final MetaField fMaxExtent = new MetaField(LocaleMgr.db.getString("maxExtent"));
    public static final MetaField fUnlimitedExtents = new MetaField(LocaleMgr.db
            .getString("unlimitedExtents"));
    public static final MetaField fPctIncrease = new MetaField(LocaleMgr.db
            .getString("pctIncrease"));
    public static final MetaField fFreelists = new MetaField(LocaleMgr.db.getString("freelists"));
    public static final MetaField fFreelistGroups = new MetaField(LocaleMgr.db
            .getString("freelistGroups"));
    public static final MetaField fBufferPool = new MetaField(LocaleMgr.db.getString("bufferPool"));
    public static final MetaField fLog = new MetaField(LocaleMgr.db.getString("log"));
    public static final MetaRelation1 fTablespace = new MetaRelation1(LocaleMgr.db
            .getString("tablespace"), 0);
    public static final MetaField fSorting = new MetaField(LocaleMgr.db.getString("sorting"));
    public static final MetaField fParallel = new MetaField(LocaleMgr.db.getString("parallel"));
    public static final MetaField fParallelDegree = new MetaField(LocaleMgr.db
            .getString("parallelDegree"));
    public static final MetaField fPartitioningMethod = new MetaField(LocaleMgr.db
            .getString("partitioningMethod"));
    public static final MetaRelationN fPartitionKeyColumns = new MetaRelationN(LocaleMgr.db
            .getString("partitionKeyColumns"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fSubpartitionKeyColumns = new MetaRelationN(LocaleMgr.db
            .getString("subpartitionKeyColumns"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbORAIndex"),
            DbORAIndex.class, new MetaField[] { fBitmap, fCategory, fPctfree, fInitrans, fMaxtrans,
                    fInitialExtent, fInitialExtentSizeUnit, fNextExtent, fNextExtentSizeUnit,
                    fMinExtent, fMaxExtent, fUnlimitedExtents, fPctIncrease, fFreelists,
                    fFreelistGroups, fBufferPool, fLog, fTablespace, fSorting, fParallel,
                    fParallelDegree, fPartitioningMethod, fPartitionKeyColumns,
                    fSubpartitionKeyColumns }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORIndex.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbORAPartition.metaClass });

            fBitmap.setJField(DbORAIndex.class.getDeclaredField("m_bitmap"));
            fCategory.setJField(DbORAIndex.class.getDeclaredField("m_category"));
            fPctfree.setJField(DbORAIndex.class.getDeclaredField("m_pctfree"));
            fInitrans.setJField(DbORAIndex.class.getDeclaredField("m_initrans"));
            fMaxtrans.setJField(DbORAIndex.class.getDeclaredField("m_maxtrans"));
            fInitialExtent.setJField(DbORAIndex.class.getDeclaredField("m_initialExtent"));
            fInitialExtentSizeUnit.setJField(DbORAIndex.class
                    .getDeclaredField("m_initialExtentSizeUnit"));
            fNextExtent.setJField(DbORAIndex.class.getDeclaredField("m_nextExtent"));
            fNextExtentSizeUnit
                    .setJField(DbORAIndex.class.getDeclaredField("m_nextExtentSizeUnit"));
            fMinExtent.setJField(DbORAIndex.class.getDeclaredField("m_minExtent"));
            fMaxExtent.setJField(DbORAIndex.class.getDeclaredField("m_maxExtent"));
            fUnlimitedExtents.setJField(DbORAIndex.class.getDeclaredField("m_unlimitedExtents"));
            fPctIncrease.setJField(DbORAIndex.class.getDeclaredField("m_pctIncrease"));
            fFreelists.setJField(DbORAIndex.class.getDeclaredField("m_freelists"));
            fFreelistGroups.setJField(DbORAIndex.class.getDeclaredField("m_freelistGroups"));
            fBufferPool.setJField(DbORAIndex.class.getDeclaredField("m_bufferPool"));
            fLog.setJField(DbORAIndex.class.getDeclaredField("m_log"));
            fTablespace.setJField(DbORAIndex.class.getDeclaredField("m_tablespace"));
            fTablespace.setFlags(MetaField.INTEGRABLE_BY_NAME);
            fTablespace.setRendererPluginName("DbORATablespace");
            fSorting.setJField(DbORAIndex.class.getDeclaredField("m_sorting"));
            fParallel.setJField(DbORAIndex.class.getDeclaredField("m_parallel"));
            fParallelDegree.setJField(DbORAIndex.class.getDeclaredField("m_parallelDegree"));
            fPartitioningMethod
                    .setJField(DbORAIndex.class.getDeclaredField("m_partitioningMethod"));
            fPartitionKeyColumns.setJField(DbORAIndex.class
                    .getDeclaredField("m_partitionKeyColumns"));
            fPartitionKeyColumns.setFlags(MetaField.INTEGRABLE | MetaField.WRITE_CHECK);
            fSubpartitionKeyColumns.setJField(DbORAIndex.class
                    .getDeclaredField("m_subpartitionKeyColumns"));
            fSubpartitionKeyColumns.setFlags(MetaField.INTEGRABLE | MetaField.WRITE_CHECK);

            fTablespace.setOppositeRel(DbORATablespace.fIndexes);
            fPartitionKeyColumns.setOppositeRel(DbORAColumn.fPartitionKeyIndexes);
            fSubpartitionKeyColumns.setOppositeRel(DbORAColumn.fSubpartitionKeyIndexes);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    boolean m_bitmap;
    ORACategory m_category;
    SrInteger m_pctfree;
    SrInteger m_initrans;
    SrInteger m_maxtrans;
    SrInteger m_initialExtent;
    ORASizeUnit m_initialExtentSizeUnit;
    SrInteger m_nextExtent;
    ORASizeUnit m_nextExtentSizeUnit;
    SrInteger m_minExtent;
    SrInteger m_maxExtent;
    boolean m_unlimitedExtents;
    SrInteger m_pctIncrease;
    SrInteger m_freelists;
    SrInteger m_freelistGroups;
    ORABufferPool m_bufferPool;
    ORALog m_log;
    DbORATablespace m_tablespace;
    ORASort m_sorting;
    ORAParallel m_parallel;
    SrInteger m_parallelDegree;
    ORAIndexPartitioningMethod m_partitioningMethod;
    DbRelationN m_partitionKeyColumns;
    DbRelationN m_subpartitionKeyColumns;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORAIndex() {
    }

    /**
     * Creates an instance of DbORAIndex.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORAIndex(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setBitmap(Boolean.FALSE);
        setUnlimitedExtents(Boolean.FALSE);
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));
    }

    //Setters

    /**
     * Sets the "bitmap" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "bitmap" property
     **/
    public final void setBitmap(Boolean value) throws DbException {
        basicSet(fBitmap, value);
    }

    /**
     * Sets the "category" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "category" property
     **/
    public final void setCategory(ORACategory value) throws DbException {
        basicSet(fCategory, value);
    }

    /**
     * Sets the "pctfree" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "pctfree" property
     **/
    public final void setPctfree(Integer value) throws DbException {
        basicSet(fPctfree, value);
    }

    /**
     * Sets the "initrans" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "initrans" property
     **/
    public final void setInitrans(Integer value) throws DbException {
        basicSet(fInitrans, value);
    }

    /**
     * Sets the "maxtrans" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "maxtrans" property
     **/
    public final void setMaxtrans(Integer value) throws DbException {
        basicSet(fMaxtrans, value);
    }

    /**
     * Sets the "initial extent" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "initial extent" property
     **/
    public final void setInitialExtent(Integer value) throws DbException {
        basicSet(fInitialExtent, value);
    }

    /**
     * Sets the "initial extent size unit" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "initial extent size unit" property
     **/
    public final void setInitialExtentSizeUnit(ORASizeUnit value) throws DbException {
        basicSet(fInitialExtentSizeUnit, value);
    }

    /**
     * Sets the "next extent" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "next extent" property
     **/
    public final void setNextExtent(Integer value) throws DbException {
        basicSet(fNextExtent, value);
    }

    /**
     * Sets the "next extent size unit" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "next extent size unit" property
     **/
    public final void setNextExtentSizeUnit(ORASizeUnit value) throws DbException {
        basicSet(fNextExtentSizeUnit, value);
    }

    /**
     * Sets the "minextents" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "minextents" property
     **/
    public final void setMinExtent(Integer value) throws DbException {
        basicSet(fMinExtent, value);
    }

    /**
     * Sets the "maxextents" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "maxextents" property
     **/
    public final void setMaxExtent(Integer value) throws DbException {
        basicSet(fMaxExtent, value);
    }

    /**
     * Sets the "unlimited extents" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "unlimited extents" property
     **/
    public final void setUnlimitedExtents(Boolean value) throws DbException {
        basicSet(fUnlimitedExtents, value);
    }

    /**
     * Sets the "pctincrease" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "pctincrease" property
     **/
    public final void setPctIncrease(Integer value) throws DbException {
        basicSet(fPctIncrease, value);
    }

    /**
     * Sets the "freelists" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "freelists" property
     **/
    public final void setFreelists(Integer value) throws DbException {
        basicSet(fFreelists, value);
    }

    /**
     * Sets the "freelist groups" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "freelist groups" property
     **/
    public final void setFreelistGroups(Integer value) throws DbException {
        basicSet(fFreelistGroups, value);
    }

    /**
     * Sets the "buffer pool" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "buffer pool" property
     **/
    public final void setBufferPool(ORABufferPool value) throws DbException {
        basicSet(fBufferPool, value);
    }

    /**
     * Sets the "log" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "log" property
     **/
    public final void setLog(ORALog value) throws DbException {
        basicSet(fLog, value);
    }

    /**
     * Sets the tablespace object associated to a DbORAIndex's instance.
     * 
     * @param value
     *            the tablespace object to be associated
     **/
    public final void setTablespace(DbORATablespace value) throws DbException {
        basicSet(fTablespace, value);
    }

    /**
     * Sets the "sorting" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "sorting" property
     **/
    public final void setSorting(ORASort value) throws DbException {
        basicSet(fSorting, value);
    }

    /**
     * Sets the "parallel" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "parallel" property
     **/
    public final void setParallel(ORAParallel value) throws DbException {
        basicSet(fParallel, value);
    }

    /**
     * Sets the "parallel degree" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "parallel degree" property
     **/
    public final void setParallelDegree(Integer value) throws DbException {
        basicSet(fParallelDegree, value);
    }

    /**
     * Sets the "partitioning method" property of a DbORAIndex's instance.
     * 
     * @param value
     *            the "partitioning method" property
     **/
    public final void setPartitioningMethod(ORAIndexPartitioningMethod value) throws DbException {
        basicSet(fPartitioningMethod, value);
    }

    /**
     * Adds an element to or removes an element from the list of partition key columns associated to
     * a DbORAIndex's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setPartitionKeyColumns(DbORAColumn value, int op) throws DbException {
        setRelationNN(fPartitionKeyColumns, value, op);
    }

    /**
     * Adds an element to the list of partition key columns associated to a DbORAIndex's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToPartitionKeyColumns(DbORAColumn value) throws DbException {
        setPartitionKeyColumns(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of partition key columns associated to a DbORAIndex's
     * instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromPartitionKeyColumns(DbORAColumn value) throws DbException {
        setPartitionKeyColumns(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Adds an element to or removes an element from the list of subpartition key columns associated
     * to a DbORAIndex's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setSubpartitionKeyColumns(DbORAColumn value, int op) throws DbException {
        setRelationNN(fSubpartitionKeyColumns, value, op);
    }

    /**
     * Adds an element to the list of subpartition key columns associated to a DbORAIndex's
     * instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToSubpartitionKeyColumns(DbORAColumn value) throws DbException {
        setSubpartitionKeyColumns(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of subpartition key columns associated to a DbORAIndex's
     * instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromSubpartitionKeyColumns(DbORAColumn value) throws DbException {
        setSubpartitionKeyColumns(value, Db.REMOVE_FROM_RELN);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fPartitionKeyColumns)
                setPartitionKeyColumns((DbORAColumn) value, Db.ADD_TO_RELN);
            else if (metaField == fSubpartitionKeyColumns)
                setSubpartitionKeyColumns((DbORAColumn) value, Db.ADD_TO_RELN);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fPartitionKeyColumns)
            setPartitionKeyColumns((DbORAColumn) neighbor, op);
        else if (relation == fSubpartitionKeyColumns)
            setSubpartitionKeyColumns((DbORAColumn) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the "bitmap" property's Boolean value of a DbORAIndex's instance.
     * 
     * @return the "bitmap" property's Boolean value
     * @deprecated use isBitmap() method instead
     **/
    public final Boolean getBitmap() throws DbException {
        return (Boolean) get(fBitmap);
    }

    /**
     * Tells whether a DbORAIndex's instance is bitmap or not.
     * 
     * @return boolean
     **/
    public final boolean isBitmap() throws DbException {
        return getBitmap().booleanValue();
    }

    /**
     * Gets the "category" of a DbORAIndex's instance.
     * 
     * @return the "category"
     **/
    public final ORACategory getCategory() throws DbException {
        return (ORACategory) get(fCategory);
    }

    /**
     * Gets the "pctfree" of a DbORAIndex's instance.
     * 
     * @return the "pctfree"
     **/
    public final Integer getPctfree() throws DbException {
        return (Integer) get(fPctfree);
    }

    /**
     * Gets the "initrans" of a DbORAIndex's instance.
     * 
     * @return the "initrans"
     **/
    public final Integer getInitrans() throws DbException {
        return (Integer) get(fInitrans);
    }

    /**
     * Gets the "maxtrans" of a DbORAIndex's instance.
     * 
     * @return the "maxtrans"
     **/
    public final Integer getMaxtrans() throws DbException {
        return (Integer) get(fMaxtrans);
    }

    /**
     * Gets the "initial extent" of a DbORAIndex's instance.
     * 
     * @return the "initial extent"
     **/
    public final Integer getInitialExtent() throws DbException {
        return (Integer) get(fInitialExtent);
    }

    /**
     * Gets the "initial extent size unit" of a DbORAIndex's instance.
     * 
     * @return the "initial extent size unit"
     **/
    public final ORASizeUnit getInitialExtentSizeUnit() throws DbException {
        return (ORASizeUnit) get(fInitialExtentSizeUnit);
    }

    /**
     * Gets the "next extent" of a DbORAIndex's instance.
     * 
     * @return the "next extent"
     **/
    public final Integer getNextExtent() throws DbException {
        return (Integer) get(fNextExtent);
    }

    /**
     * Gets the "next extent size unit" of a DbORAIndex's instance.
     * 
     * @return the "next extent size unit"
     **/
    public final ORASizeUnit getNextExtentSizeUnit() throws DbException {
        return (ORASizeUnit) get(fNextExtentSizeUnit);
    }

    /**
     * Gets the "minextents" of a DbORAIndex's instance.
     * 
     * @return the "minextents"
     **/
    public final Integer getMinExtent() throws DbException {
        return (Integer) get(fMinExtent);
    }

    /**
     * Gets the "maxextents" of a DbORAIndex's instance.
     * 
     * @return the "maxextents"
     **/
    public final Integer getMaxExtent() throws DbException {
        return (Integer) get(fMaxExtent);
    }

    /**
     * Gets the "unlimited extents" property's Boolean value of a DbORAIndex's instance.
     * 
     * @return the "unlimited extents" property's Boolean value
     * @deprecated use isUnlimitedExtents() method instead
     **/
    public final Boolean getUnlimitedExtents() throws DbException {
        return (Boolean) get(fUnlimitedExtents);
    }

    /**
     * Tells whether a DbORAIndex's instance is unlimitedExtents or not.
     * 
     * @return boolean
     **/
    public final boolean isUnlimitedExtents() throws DbException {
        return getUnlimitedExtents().booleanValue();
    }

    /**
     * Gets the "pctincrease" of a DbORAIndex's instance.
     * 
     * @return the "pctincrease"
     **/
    public final Integer getPctIncrease() throws DbException {
        return (Integer) get(fPctIncrease);
    }

    /**
     * Gets the "freelists" of a DbORAIndex's instance.
     * 
     * @return the "freelists"
     **/
    public final Integer getFreelists() throws DbException {
        return (Integer) get(fFreelists);
    }

    /**
     * Gets the "freelist groups" of a DbORAIndex's instance.
     * 
     * @return the "freelist groups"
     **/
    public final Integer getFreelistGroups() throws DbException {
        return (Integer) get(fFreelistGroups);
    }

    /**
     * Gets the "buffer pool" of a DbORAIndex's instance.
     * 
     * @return the "buffer pool"
     **/
    public final ORABufferPool getBufferPool() throws DbException {
        return (ORABufferPool) get(fBufferPool);
    }

    /**
     * Gets the "log" of a DbORAIndex's instance.
     * 
     * @return the "log"
     **/
    public final ORALog getLog() throws DbException {
        return (ORALog) get(fLog);
    }

    /**
     * Gets the tablespace object associated to a DbORAIndex's instance.
     * 
     * @return the tablespace object
     **/
    public final DbORATablespace getTablespace() throws DbException {
        return (DbORATablespace) get(fTablespace);
    }

    /**
     * Gets the "sorting" of a DbORAIndex's instance.
     * 
     * @return the "sorting"
     **/
    public final ORASort getSorting() throws DbException {
        return (ORASort) get(fSorting);
    }

    /**
     * Gets the "parallel" of a DbORAIndex's instance.
     * 
     * @return the "parallel"
     **/
    public final ORAParallel getParallel() throws DbException {
        return (ORAParallel) get(fParallel);
    }

    /**
     * Gets the "parallel degree" of a DbORAIndex's instance.
     * 
     * @return the "parallel degree"
     **/
    public final Integer getParallelDegree() throws DbException {
        return (Integer) get(fParallelDegree);
    }

    /**
     * Gets the "partitioning method" of a DbORAIndex's instance.
     * 
     * @return the "partitioning method"
     **/
    public final ORAIndexPartitioningMethod getPartitioningMethod() throws DbException {
        return (ORAIndexPartitioningMethod) get(fPartitioningMethod);
    }

    /**
     * Gets the list of partition key columns associated to a DbORAIndex's instance.
     * 
     * @return the list of partition key columns.
     **/
    public final DbRelationN getPartitionKeyColumns() throws DbException {
        return (DbRelationN) get(fPartitionKeyColumns);
    }

    /**
     * Gets the list of subpartition key columns associated to a DbORAIndex's instance.
     * 
     * @return the list of subpartition key columns.
     **/
    public final DbRelationN getSubpartitionKeyColumns() throws DbException {
        return (DbRelationN) get(fSubpartitionKeyColumns);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
