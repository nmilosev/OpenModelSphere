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
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORADataModel.html">DbORADataModel</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORANestedTableStorage.html"
 * >DbORANestedTableStorage</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORALobStorage.html"
 * >DbORALobStorage</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAPartition.html">DbORAPartition</A>,
 * <A HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAColumn.html">DbORAColumn</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAForeign.html">DbORAForeign</A>, <A
 * HREF
 * ="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAPrimaryUnique.html">DbORAPrimaryUnique
 * </A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORACheck.html">DbORACheck</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAIndex.html">DbORAIndex</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORATrigger.html">DbORATrigger</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORATable extends DbORAAbsTable {

    //Meta

    public static final MetaField fCache = new MetaField(LocaleMgr.db.getString("cache"));
    public static final MetaField fMonitoring = new MetaField(LocaleMgr.db.getString("monitoring"));
    public static final MetaField fRowMovement = new MetaField(LocaleMgr.db
            .getString("rowMovement"));
    public static final MetaField fParallel = new MetaField(LocaleMgr.db.getString("parallel"));
    public static final MetaField fParallelDegree = new MetaField(LocaleMgr.db
            .getString("parallelDegree"));
    public static final MetaField fTemporary = new MetaField(LocaleMgr.db.getString("temporary"));
    public static final MetaField fCommitAction = new MetaField(LocaleMgr.db
            .getString("commitAction"));
    public static final MetaField fLog = new MetaField(LocaleMgr.db.getString("log"));
    public static final MetaRelationN fPrimaryUniqueConstraints = new MetaRelationN(LocaleMgr.db
            .getString("primaryUniqueConstraints"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fCheckConstraints = new MetaRelationN(LocaleMgr.db
            .getString("checkConstraints"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fForeignConstraints = new MetaRelationN(LocaleMgr.db
            .getString("foreignConstraints"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fTablespaces = new MetaRelationN(LocaleMgr.db
            .getString("tablespaces"), 0, MetaRelationN.cardN);
    public static final MetaField fPartitioningMethod = new MetaField(LocaleMgr.db
            .getString("partitioningMethod"));
    public static final MetaRelationN fPartitionKeyColumns = new MetaRelationN(LocaleMgr.db
            .getString("partitionKeyColumns"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fSubpartitionKeyColumns = new MetaRelationN(LocaleMgr.db
            .getString("subpartitionKeyColumns"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbORATable"),
            DbORATable.class, new MetaField[] { fCache, fMonitoring, fRowMovement, fParallel,
                    fParallelDegree, fTemporary, fCommitAction, fLog, fPrimaryUniqueConstraints,
                    fCheckConstraints, fForeignConstraints, fTablespaces, fPartitioningMethod,
                    fPartitionKeyColumns, fSubpartitionKeyColumns }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORAAbsTable.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbORANestedTableStorage.metaClass,
                    DbORALobStorage.metaClass, DbORAPartition.metaClass });

            fCache.setJField(DbORATable.class.getDeclaredField("m_cache"));
            fMonitoring.setJField(DbORATable.class.getDeclaredField("m_monitoring"));
            fRowMovement.setJField(DbORATable.class.getDeclaredField("m_rowMovement"));
            fParallel.setJField(DbORATable.class.getDeclaredField("m_parallel"));
            fParallelDegree.setJField(DbORATable.class.getDeclaredField("m_parallelDegree"));
            fTemporary.setJField(DbORATable.class.getDeclaredField("m_temporary"));
            fTemporary.setScreenOrder("<pctfree");
            fCommitAction.setJField(DbORATable.class.getDeclaredField("m_commitAction"));
            fLog.setJField(DbORATable.class.getDeclaredField("m_log"));
            fPrimaryUniqueConstraints.setJField(DbORATable.class
                    .getDeclaredField("m_primaryUniqueConstraints"));
            fCheckConstraints.setJField(DbORATable.class.getDeclaredField("m_checkConstraints"));
            fForeignConstraints
                    .setJField(DbORATable.class.getDeclaredField("m_foreignConstraints"));
            fTablespaces.setJField(DbORATable.class.getDeclaredField("m_tablespaces"));
            fTablespaces.setFlags(MetaField.INTEGRABLE | MetaField.WRITE_CHECK
                    | MetaField.INTEGRABLE_BY_NAME);
            fPartitioningMethod
                    .setJField(DbORATable.class.getDeclaredField("m_partitioningMethod"));
            fPartitionKeyColumns.setJField(DbORATable.class
                    .getDeclaredField("m_partitionKeyColumns"));
            fPartitionKeyColumns.setFlags(MetaField.INTEGRABLE);
            fSubpartitionKeyColumns.setJField(DbORATable.class
                    .getDeclaredField("m_subpartitionKeyColumns"));
            fSubpartitionKeyColumns.setFlags(MetaField.INTEGRABLE);

            fTablespaces.setOppositeRel(DbORATablespace.fTables);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    ORACache m_cache;
    ORAMonitoring m_monitoring;
    ORAEnableDisable m_rowMovement;
    ORAParallel m_parallel;
    SrInteger m_parallelDegree;
    boolean m_temporary;
    ORACommitAction m_commitAction;
    ORALog m_log;
    DbRelationN m_primaryUniqueConstraints;
    DbRelationN m_checkConstraints;
    DbRelationN m_foreignConstraints;
    DbRelationN m_tablespaces;
    ORAPartitioningMethod m_partitioningMethod;
    DbRelationN m_partitionKeyColumns;
    DbRelationN m_subpartitionKeyColumns;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORATable() {
    }

    /**
     * Creates an instance of DbORATable.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORATable(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setTemporary(Boolean.FALSE);
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));
    }

    //Setters

    /**
     * Sets the "cache" property of a DbORATable's instance.
     * 
     * @param value
     *            the "cache" property
     **/
    public final void setCache(ORACache value) throws DbException {
        basicSet(fCache, value);
    }

    /**
     * Sets the "monitoring" property of a DbORATable's instance.
     * 
     * @param value
     *            the "monitoring" property
     **/
    public final void setMonitoring(ORAMonitoring value) throws DbException {
        basicSet(fMonitoring, value);
    }

    /**
     * Sets the "row movement" property of a DbORATable's instance.
     * 
     * @param value
     *            the "row movement" property
     **/
    public final void setRowMovement(ORAEnableDisable value) throws DbException {
        basicSet(fRowMovement, value);
    }

    /**
     * Sets the "parallel" property of a DbORATable's instance.
     * 
     * @param value
     *            the "parallel" property
     **/
    public final void setParallel(ORAParallel value) throws DbException {
        basicSet(fParallel, value);
    }

    /**
     * Sets the "parallel degree" property of a DbORATable's instance.
     * 
     * @param value
     *            the "parallel degree" property
     **/
    public final void setParallelDegree(Integer value) throws DbException {
        basicSet(fParallelDegree, value);
    }

    /**
     * Sets the "temporary" property of a DbORATable's instance.
     * 
     * @param value
     *            the "temporary" property
     **/
    public final void setTemporary(Boolean value) throws DbException {
        basicSet(fTemporary, value);
    }

    /**
     * Sets the "commit action" property of a DbORATable's instance.
     * 
     * @param value
     *            the "commit action" property
     **/
    public final void setCommitAction(ORACommitAction value) throws DbException {
        basicSet(fCommitAction, value);
    }

    /**
     * Sets the "log" property of a DbORATable's instance.
     * 
     * @param value
     *            the "log" property
     **/
    public final void setLog(ORALog value) throws DbException {
        basicSet(fLog, value);
    }

    /**
     * Adds an element to or removes an element from the list of tablespaces associated to a
     * DbORATable's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setTablespaces(DbORATablespace value, int op) throws DbException {
        setRelationNN(fTablespaces, value, op);
    }

    /**
     * Adds an element to the list of tablespaces associated to a DbORATable's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToTablespaces(DbORATablespace value) throws DbException {
        setTablespaces(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of tablespaces associated to a DbORATable's instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromTablespaces(DbORATablespace value) throws DbException {
        setTablespaces(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Sets the "partitioning method" property of a DbORATable's instance.
     * 
     * @param value
     *            the "partitioning method" property
     **/
    public final void setPartitioningMethod(ORAPartitioningMethod value) throws DbException {
        basicSet(fPartitioningMethod, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fPrimaryUniqueConstraints)
                ((DbORAPrimaryUnique) value).setExceptionTable(this);
            else if (metaField == fCheckConstraints)
                ((DbORACheck) value).setExceptionTable(this);
            else if (metaField == fForeignConstraints)
                ((DbORAForeign) value).setExceptionTable(this);
            else if (metaField == fTablespaces)
                setTablespaces((DbORATablespace) value, Db.ADD_TO_RELN);
            else if (metaField == fPartitionKeyColumns)
                ((DbORAColumn) value).setPartitionKeyTable(this);
            else if (metaField == fSubpartitionKeyColumns)
                ((DbORAColumn) value).setSubpartitionKeyTable(this);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fTablespaces)
            setTablespaces((DbORATablespace) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the "cache" of a DbORATable's instance.
     * 
     * @return the "cache"
     **/
    public final ORACache getCache() throws DbException {
        return (ORACache) get(fCache);
    }

    /**
     * Gets the "monitoring" of a DbORATable's instance.
     * 
     * @return the "monitoring"
     **/
    public final ORAMonitoring getMonitoring() throws DbException {
        return (ORAMonitoring) get(fMonitoring);
    }

    /**
     * Gets the "row movement" of a DbORATable's instance.
     * 
     * @return the "row movement"
     **/
    public final ORAEnableDisable getRowMovement() throws DbException {
        return (ORAEnableDisable) get(fRowMovement);
    }

    /**
     * Gets the "parallel" of a DbORATable's instance.
     * 
     * @return the "parallel"
     **/
    public final ORAParallel getParallel() throws DbException {
        return (ORAParallel) get(fParallel);
    }

    /**
     * Gets the "parallel degree" of a DbORATable's instance.
     * 
     * @return the "parallel degree"
     **/
    public final Integer getParallelDegree() throws DbException {
        return (Integer) get(fParallelDegree);
    }

    /**
     * Gets the "temporary" property's Boolean value of a DbORATable's instance.
     * 
     * @return the "temporary" property's Boolean value
     * @deprecated use isTemporary() method instead
     **/
    public final Boolean getTemporary() throws DbException {
        return (Boolean) get(fTemporary);
    }

    /**
     * Tells whether a DbORATable's instance is temporary or not.
     * 
     * @return boolean
     **/
    public final boolean isTemporary() throws DbException {
        return getTemporary().booleanValue();
    }

    /**
     * Gets the "commit action" of a DbORATable's instance.
     * 
     * @return the "commit action"
     **/
    public final ORACommitAction getCommitAction() throws DbException {
        return (ORACommitAction) get(fCommitAction);
    }

    /**
     * Gets the "log" of a DbORATable's instance.
     * 
     * @return the "log"
     **/
    public final ORALog getLog() throws DbException {
        return (ORALog) get(fLog);
    }

    /**
     * Gets the list of exception usage (primary/unique)s associated to a DbORATable's instance.
     * 
     * @return the list of exception usage (primary/unique)s.
     **/
    public final DbRelationN getPrimaryUniqueConstraints() throws DbException {
        return (DbRelationN) get(fPrimaryUniqueConstraints);
    }

    /**
     * Gets the list of exception usage (check)s associated to a DbORATable's instance.
     * 
     * @return the list of exception usage (check)s.
     **/
    public final DbRelationN getCheckConstraints() throws DbException {
        return (DbRelationN) get(fCheckConstraints);
    }

    /**
     * Gets the list of exception usage (foreign)s associated to a DbORATable's instance.
     * 
     * @return the list of exception usage (foreign)s.
     **/
    public final DbRelationN getForeignConstraints() throws DbException {
        return (DbRelationN) get(fForeignConstraints);
    }

    /**
     * Gets the list of tablespaces associated to a DbORATable's instance.
     * 
     * @return the list of tablespaces.
     **/
    public final DbRelationN getTablespaces() throws DbException {
        return (DbRelationN) get(fTablespaces);
    }

    /**
     * Gets the "partitioning method" of a DbORATable's instance.
     * 
     * @return the "partitioning method"
     **/
    public final ORAPartitioningMethod getPartitioningMethod() throws DbException {
        return (ORAPartitioningMethod) get(fPartitioningMethod);
    }

    /**
     * Gets the list of partition key columns associated to a DbORATable's instance.
     * 
     * @return the list of partition key columns.
     **/
    public final DbRelationN getPartitionKeyColumns() throws DbException {
        return (DbRelationN) get(fPartitionKeyColumns);
    }

    /**
     * Gets the list of subpartition key columns associated to a DbORATable's instance.
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
