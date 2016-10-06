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
package org.modelsphere.sms.or.informix.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.informix.db.srtypes.*;
import org.modelsphere.sms.or.informix.international.LocaleMgr;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFTable.html">DbINFTable</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFView.html">DbINFView</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFFragment.html">DbINFFragment</A>,
 * <A HREF="../../../../../../org/modelsphere/sms/or/db/DbORIndexKey.html">DbORIndexKey</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbINFIndex extends DbORIndex {

    //Meta

    public static final MetaField fCluster = new MetaField(LocaleMgr.db.getString("cluster"));
    public static final MetaField fLockMode = new MetaField(LocaleMgr.db.getString("lockMode"));
    public static final MetaField fBitmapStorage = new MetaField(LocaleMgr.db
            .getString("bitmapStorage"));
    public static final MetaField fMode = new MetaField(LocaleMgr.db.getString("mode"));
    public static final MetaRelation1 fDbspace = new MetaRelation1(LocaleMgr.db
            .getString("dbspace"), 0);
    public static final MetaRelationN fFragmentationKeyColumns = new MetaRelationN(LocaleMgr.db
            .getString("fragmentationKeyColumns"), 0, MetaRelationN.cardN);
    public static final MetaField fFragmentationDistribScheme = new MetaField(LocaleMgr.db
            .getString("DbINFIndex.fragmentationDistribScheme"));
    public static final MetaField fFillFactor = new MetaField(LocaleMgr.db.getString("fillFactor"));
    public static final MetaField fOnline = new MetaField(LocaleMgr.db.getString("online"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbINFIndex"),
            DbINFIndex.class, new MetaField[] { fCluster, fLockMode, fBitmapStorage, fMode,
                    fDbspace, fFragmentationKeyColumns, fFragmentationDistribScheme, fFillFactor,
                    fOnline }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORIndex.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbINFFragment.metaClass });

            fCluster.setJField(DbINFIndex.class.getDeclaredField("m_cluster"));
            fLockMode.setJField(DbINFIndex.class.getDeclaredField("m_lockMode"));
            fBitmapStorage.setJField(DbINFIndex.class.getDeclaredField("m_bitmapStorage"));
            fMode.setJField(DbINFIndex.class.getDeclaredField("m_mode"));
            fDbspace.setJField(DbINFIndex.class.getDeclaredField("m_dbspace"));
            fDbspace.setFlags(MetaField.INTEGRABLE_BY_NAME);
            fDbspace.setRendererPluginName("DbINFDbspace");
            fFragmentationKeyColumns.setJField(DbINFIndex.class
                    .getDeclaredField("m_fragmentationKeyColumns"));
            fFragmentationKeyColumns.setFlags(MetaField.INTEGRABLE | MetaField.WRITE_CHECK);
            fFragmentationDistribScheme.setJField(DbINFIndex.class
                    .getDeclaredField("m_fragmentationDistribScheme"));
            fFillFactor.setJField(DbINFIndex.class.getDeclaredField("m_fillFactor"));
            fOnline.setJField(DbINFIndex.class.getDeclaredField("m_online"));

            fDbspace.setOppositeRel(DbINFDbspace.fIndexes);
            fFragmentationKeyColumns.setOppositeRel(DbINFColumn.fFragmentationKeyIndexes);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    boolean m_cluster;
    INFIndexLockMode m_lockMode;
    boolean m_bitmapStorage;
    INFConstraintMode m_mode;
    DbINFDbspace m_dbspace;
    DbRelationN m_fragmentationKeyColumns;
    INFDistScheme m_fragmentationDistribScheme;
    SrInteger m_fillFactor;
    boolean m_online;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbINFIndex() {
    }

    /**
     * Creates an instance of DbINFIndex.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbINFIndex(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setCluster(Boolean.FALSE);
        setBitmapStorage(Boolean.FALSE);
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));
    }

    //Setters

    /**
     * Sets the "cluster" property of a DbINFIndex's instance.
     * 
     * @param value
     *            the "cluster" property
     **/
    public final void setCluster(Boolean value) throws DbException {
        basicSet(fCluster, value);
    }

    /**
     * Sets the "lock mode" property of a DbINFIndex's instance.
     * 
     * @param value
     *            the "lock mode" property
     **/
    public final void setLockMode(INFIndexLockMode value) throws DbException {
        basicSet(fLockMode, value);
    }

    /**
     * Sets the "using bitmap" property of a DbINFIndex's instance.
     * 
     * @param value
     *            the "using bitmap" property
     **/
    public final void setBitmapStorage(Boolean value) throws DbException {
        basicSet(fBitmapStorage, value);
    }

    /**
     * Sets the "mode" property of a DbINFIndex's instance.
     * 
     * @param value
     *            the "mode" property
     **/
    public final void setMode(INFConstraintMode value) throws DbException {
        basicSet(fMode, value);
    }

    /**
     * Sets the dbspace object associated to a DbINFIndex's instance.
     * 
     * @param value
     *            the dbspace object to be associated
     **/
    public final void setDbspace(DbINFDbspace value) throws DbException {
        basicSet(fDbspace, value);
    }

    /**
     * Adds an element to or removes an element from the list of fragmentation key columns
     * associated to a DbINFIndex's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setFragmentationKeyColumns(DbINFColumn value, int op) throws DbException {
        setRelationNN(fFragmentationKeyColumns, value, op);
    }

    /**
     * Adds an element to the list of fragmentation key columns associated to a DbINFIndex's
     * instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToFragmentationKeyColumns(DbINFColumn value) throws DbException {
        setFragmentationKeyColumns(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of fragmentation key columns associated to a DbINFIndex's
     * instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromFragmentationKeyColumns(DbINFColumn value) throws DbException {
        setFragmentationKeyColumns(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Sets the "fragmentation distribution scheme" property of a DbINFIndex's instance.
     * 
     * @param value
     *            the "fragmentation distribution scheme" property
     **/
    public final void setFragmentationDistribScheme(INFDistScheme value) throws DbException {
        basicSet(fFragmentationDistribScheme, value);
    }

    /**
     * Sets the "fillfactor" property of a DbINFIndex's instance.
     * 
     * @param value
     *            the "fillfactor" property
     **/
    public final void setFillFactor(Integer value) throws DbException {
        basicSet(fFillFactor, value);
    }

    /**
     * Sets the "online" property of a DbINFIndex's instance.
     * 
     * @param value
     *            the "online" property
     **/
    public final void setOnline(Boolean value) throws DbException {
        basicSet(fOnline, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fFragmentationKeyColumns)
                setFragmentationKeyColumns((DbINFColumn) value, Db.ADD_TO_RELN);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fFragmentationKeyColumns)
            setFragmentationKeyColumns((DbINFColumn) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the "cluster" property's Boolean value of a DbINFIndex's instance.
     * 
     * @return the "cluster" property's Boolean value
     * @deprecated use isCluster() method instead
     **/
    public final Boolean getCluster() throws DbException {
        return (Boolean) get(fCluster);
    }

    /**
     * Tells whether a DbINFIndex's instance is cluster or not.
     * 
     * @return boolean
     **/
    public final boolean isCluster() throws DbException {
        return getCluster().booleanValue();
    }

    /**
     * Gets the "lock mode" of a DbINFIndex's instance.
     * 
     * @return the "lock mode"
     **/
    public final INFIndexLockMode getLockMode() throws DbException {
        return (INFIndexLockMode) get(fLockMode);
    }

    /**
     * Gets the "using bitmap" property's Boolean value of a DbINFIndex's instance.
     * 
     * @return the "using bitmap" property's Boolean value
     * @deprecated use isBitmapStorage() method instead
     **/
    public final Boolean getBitmapStorage() throws DbException {
        return (Boolean) get(fBitmapStorage);
    }

    /**
     * Tells whether a DbINFIndex's instance is bitmapStorage or not.
     * 
     * @return boolean
     **/
    public final boolean isBitmapStorage() throws DbException {
        return getBitmapStorage().booleanValue();
    }

    /**
     * Gets the "mode" of a DbINFIndex's instance.
     * 
     * @return the "mode"
     **/
    public final INFConstraintMode getMode() throws DbException {
        return (INFConstraintMode) get(fMode);
    }

    /**
     * Gets the dbspace object associated to a DbINFIndex's instance.
     * 
     * @return the dbspace object
     **/
    public final DbINFDbspace getDbspace() throws DbException {
        return (DbINFDbspace) get(fDbspace);
    }

    /**
     * Gets the list of fragmentation key columns associated to a DbINFIndex's instance.
     * 
     * @return the list of fragmentation key columns.
     **/
    public final DbRelationN getFragmentationKeyColumns() throws DbException {
        return (DbRelationN) get(fFragmentationKeyColumns);
    }

    /**
     * Gets the "fragmentation distribution scheme" of a DbINFIndex's instance.
     * 
     * @return the "fragmentation distribution scheme"
     **/
    public final INFDistScheme getFragmentationDistribScheme() throws DbException {
        return (INFDistScheme) get(fFragmentationDistribScheme);
    }

    /**
     * Gets the "fillfactor" of a DbINFIndex's instance.
     * 
     * @return the "fillfactor"
     **/
    public final Integer getFillFactor() throws DbException {
        return (Integer) get(fFillFactor);
    }

    /**
     * Gets the "online" property's Boolean value of a DbINFIndex's instance.
     * 
     * @return the "online" property's Boolean value
     * @deprecated use isOnline() method instead
     **/
    public final Boolean getOnline() throws DbException {
        return (Boolean) get(fOnline);
    }

    /**
     * Tells whether a DbINFIndex's instance is online or not.
     * 
     * @return boolean
     **/
    public final boolean isOnline() throws DbException {
        return getOnline().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
