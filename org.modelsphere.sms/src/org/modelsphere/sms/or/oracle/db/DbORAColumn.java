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
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORAColumn extends DbORColumn {

    //Meta

    public static final MetaRelation1 fStorageTable = new MetaRelation1(LocaleMgr.db
            .getString("storageTable"), 0);
    public static final MetaRelation1 fLobStorage = new MetaRelation1(LocaleMgr.db
            .getString("lobStorage"), 0);
    public static final MetaRelation1 fPartitionKeyTable = new MetaRelation1(LocaleMgr.db
            .getString("partitionKeyTable"), 0);
    public static final MetaRelationN fPartitionKeyIndexes = new MetaRelationN(LocaleMgr.db
            .getString("partitionKeyIndexes"), 0, MetaRelationN.cardN);
    public static final MetaField fWithRowid = new MetaField(LocaleMgr.db.getString("withRowid"));
    public static final MetaRelationN fSubpartitionKeyIndexes = new MetaRelationN(LocaleMgr.db
            .getString("subpartitionKeyIndexes"), 0, MetaRelationN.cardN);
    public static final MetaRelation1 fSubpartitionKeyTable = new MetaRelation1(LocaleMgr.db
            .getString("subpartitionKeyTable"), 0);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbORAColumn"),
            DbORAColumn.class, new MetaField[] { fStorageTable, fLobStorage, fPartitionKeyTable,
                    fPartitionKeyIndexes, fWithRowid, fSubpartitionKeyIndexes,
                    fSubpartitionKeyTable }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORColumn.metaClass);

            fStorageTable.setJField(DbORAColumn.class.getDeclaredField("m_storageTable"));
            fStorageTable.setRendererPluginName("DbORANestedTableStorage");
            fLobStorage.setJField(DbORAColumn.class.getDeclaredField("m_lobStorage"));
            fLobStorage.setRendererPluginName("DbORALobStorage");
            fPartitionKeyTable.setJField(DbORAColumn.class.getDeclaredField("m_partitionKeyTable"));
            fPartitionKeyTable.setVisibleInScreen(false);
            fPartitionKeyIndexes.setJField(DbORAColumn.class
                    .getDeclaredField("m_partitionKeyIndexes"));
            fWithRowid.setJField(DbORAColumn.class.getDeclaredField("m_withRowid"));
            fSubpartitionKeyIndexes.setJField(DbORAColumn.class
                    .getDeclaredField("m_subpartitionKeyIndexes"));
            fSubpartitionKeyTable.setJField(DbORAColumn.class
                    .getDeclaredField("m_subpartitionKeyTable"));
            fSubpartitionKeyTable.setVisibleInScreen(false);

            fStorageTable.setOppositeRel(DbORANestedTableStorage.fNestedItem);
            fLobStorage.setOppositeRel(DbORALobStorage.fLobItems);
            fPartitionKeyTable.setOppositeRel(DbORATable.fPartitionKeyColumns);
            fPartitionKeyIndexes.setOppositeRel(DbORAIndex.fPartitionKeyColumns);
            fSubpartitionKeyIndexes.setOppositeRel(DbORAIndex.fSubpartitionKeyColumns);
            fSubpartitionKeyTable.setOppositeRel(DbORATable.fSubpartitionKeyColumns);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbORANestedTableStorage m_storageTable;
    DbORALobStorage m_lobStorage;
    DbORATable m_partitionKeyTable;
    DbRelationN m_partitionKeyIndexes;
    boolean m_withRowid;
    DbRelationN m_subpartitionKeyIndexes;
    DbORATable m_subpartitionKeyTable;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORAColumn() {
    }

    /**
     * Creates an instance of DbORAColumn.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORAColumn(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setWithRowid(Boolean.FALSE);
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));
    }

    //Setters

    /**
     * Sets the storage table object associated to a DbORAColumn's instance.
     * 
     * @param value
     *            the storage table object to be associated
     **/
    public final void setStorageTable(DbORANestedTableStorage value) throws DbException {
        basicSet(fStorageTable, value);
    }

    /**
     * Sets the lob storage object associated to a DbORAColumn's instance.
     * 
     * @param value
     *            the lob storage object to be associated
     **/
    public final void setLobStorage(DbORALobStorage value) throws DbException {
        basicSet(fLobStorage, value);
    }

    /**
     * Sets the partition key table object associated to a DbORAColumn's instance.
     * 
     * @param value
     *            the partition key table object to be associated
     **/
    public final void setPartitionKeyTable(DbORATable value) throws DbException {
        basicSet(fPartitionKeyTable, value);
    }

    /**
     * Adds an element to or removes an element from the list of indexes associated to a
     * DbORAColumn's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setPartitionKeyIndexes(DbORAIndex value, int op) throws DbException {
        setRelationNN(fPartitionKeyIndexes, value, op);
    }

    /**
     * Adds an element to the list of indexes associated to a DbORAColumn's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToPartitionKeyIndexes(DbORAIndex value) throws DbException {
        setPartitionKeyIndexes(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of indexes associated to a DbORAColumn's instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromPartitionKeyIndexes(DbORAIndex value) throws DbException {
        setPartitionKeyIndexes(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Sets the "with rowid" property of a DbORAColumn's instance.
     * 
     * @param value
     *            the "with rowid" property
     **/
    public final void setWithRowid(Boolean value) throws DbException {
        basicSet(fWithRowid, value);
    }

    /**
     * Adds an element to or removes an element from the list of indexes associated to a
     * DbORAColumn's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setSubpartitionKeyIndexes(DbORAIndex value, int op) throws DbException {
        setRelationNN(fSubpartitionKeyIndexes, value, op);
    }

    /**
     * Adds an element to the list of indexes associated to a DbORAColumn's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToSubpartitionKeyIndexes(DbORAIndex value) throws DbException {
        setSubpartitionKeyIndexes(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of indexes associated to a DbORAColumn's instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromSubpartitionKeyIndexes(DbORAIndex value) throws DbException {
        setSubpartitionKeyIndexes(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Sets the subpartition key table object associated to a DbORAColumn's instance.
     * 
     * @param value
     *            the subpartition key table object to be associated
     **/
    public final void setSubpartitionKeyTable(DbORATable value) throws DbException {
        basicSet(fSubpartitionKeyTable, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fPartitionKeyIndexes)
                setPartitionKeyIndexes((DbORAIndex) value, Db.ADD_TO_RELN);
            else if (metaField == fSubpartitionKeyIndexes)
                setSubpartitionKeyIndexes((DbORAIndex) value, Db.ADD_TO_RELN);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fPartitionKeyIndexes)
            setPartitionKeyIndexes((DbORAIndex) neighbor, op);
        else if (relation == fSubpartitionKeyIndexes)
            setSubpartitionKeyIndexes((DbORAIndex) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the storage table object associated to a DbORAColumn's instance.
     * 
     * @return the storage table object
     **/
    public final DbORANestedTableStorage getStorageTable() throws DbException {
        return (DbORANestedTableStorage) get(fStorageTable);
    }

    /**
     * Gets the lob storage object associated to a DbORAColumn's instance.
     * 
     * @return the lob storage object
     **/
    public final DbORALobStorage getLobStorage() throws DbException {
        return (DbORALobStorage) get(fLobStorage);
    }

    /**
     * Gets the partition key table object associated to a DbORAColumn's instance.
     * 
     * @return the partition key table object
     **/
    public final DbORATable getPartitionKeyTable() throws DbException {
        return (DbORATable) get(fPartitionKeyTable);
    }

    /**
     * Gets the list of indexes associated to a DbORAColumn's instance.
     * 
     * @return the list of indexes.
     **/
    public final DbRelationN getPartitionKeyIndexes() throws DbException {
        return (DbRelationN) get(fPartitionKeyIndexes);
    }

    /**
     * Gets the "with rowid" property's Boolean value of a DbORAColumn's instance.
     * 
     * @return the "with rowid" property's Boolean value
     * @deprecated use isWithRowid() method instead
     **/
    public final Boolean getWithRowid() throws DbException {
        return (Boolean) get(fWithRowid);
    }

    /**
     * Tells whether a DbORAColumn's instance is withRowid or not.
     * 
     * @return boolean
     **/
    public final boolean isWithRowid() throws DbException {
        return getWithRowid().booleanValue();
    }

    /**
     * Gets the list of indexes associated to a DbORAColumn's instance.
     * 
     * @return the list of indexes.
     **/
    public final DbRelationN getSubpartitionKeyIndexes() throws DbException {
        return (DbRelationN) get(fSubpartitionKeyIndexes);
    }

    /**
     * Gets the subpartition key table object associated to a DbORAColumn's instance.
     * 
     * @return the subpartition key table object
     **/
    public final DbORATable getSubpartitionKeyTable() throws DbException {
        return (DbORATable) get(fSubpartitionKeyTable);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
