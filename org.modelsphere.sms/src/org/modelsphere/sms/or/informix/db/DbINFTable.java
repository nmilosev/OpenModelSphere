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
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFDataModel.html"
 * >DbINFDataModel</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFColumn.html">DbINFColumn</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFForeign.html">DbINFForeign</A>,
 * <A HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFPrimaryUnique.html">
 * DbINFPrimaryUnique</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFCheck.html">DbINFCheck</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFIndex.html">DbINFIndex</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFTrigger.html">DbINFTrigger</A>,
 * <A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFFragment.html">DbINFFragment</A>,
 * <A HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbINFTable extends DbORTable {

    //Meta

    public static final MetaField fCategory = new MetaField(LocaleMgr.db.getString("category"));
    public static final MetaRelation1 fDbspace = new MetaRelation1(LocaleMgr.db
            .getString("dbspace"), 0);
    public static final MetaField fFragmentationDistribScheme = new MetaField(LocaleMgr.db
            .getString("fragmentationDistribScheme"));
    public static final MetaField fWithRowIDs = new MetaField(LocaleMgr.db.getString("withRowIDs"));
    public static final MetaField fFragmentationMinRangeValue = new MetaField(LocaleMgr.db
            .getString("fragmentationMinRangeValue"));
    public static final MetaField fFragmentationMaxRangeValue = new MetaField(LocaleMgr.db
            .getString("fragmentationMaxRangeValue"));
    public static final MetaField fExtentSize = new MetaField(LocaleMgr.db.getString("extentSize"));
    public static final MetaField fNextExtentSize = new MetaField(LocaleMgr.db
            .getString("nextExtentSize"));
    public static final MetaField fLockMode = new MetaField(LocaleMgr.db.getString("lockMode"));
    public static final MetaRelationN fFragmentationKeyColumns = new MetaRelationN(LocaleMgr.db
            .getString("fragmentationKeyColumns"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbINFTable"),
            DbINFTable.class, new MetaField[] { fCategory, fDbspace, fFragmentationDistribScheme,
                    fWithRowIDs, fFragmentationMinRangeValue, fFragmentationMaxRangeValue,
                    fExtentSize, fNextExtentSize, fLockMode, fFragmentationKeyColumns },
            MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORTable.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbINFColumn.metaClass,
                    DbINFForeign.metaClass, DbINFPrimaryUnique.metaClass, DbINFCheck.metaClass,
                    DbINFIndex.metaClass, DbINFTrigger.metaClass, DbINFFragment.metaClass });

            fCategory.setJField(DbINFTable.class.getDeclaredField("m_category"));
            fDbspace.setJField(DbINFTable.class.getDeclaredField("m_dbspace"));
            fDbspace.setFlags(MetaField.INTEGRABLE_BY_NAME);
            fDbspace.setRendererPluginName("DbINFDbspace");
            fFragmentationDistribScheme.setJField(DbINFTable.class
                    .getDeclaredField("m_fragmentationDistribScheme"));
            fWithRowIDs.setJField(DbINFTable.class.getDeclaredField("m_withRowIDs"));
            fFragmentationMinRangeValue.setJField(DbINFTable.class
                    .getDeclaredField("m_fragmentationMinRangeValue"));
            fFragmentationMaxRangeValue.setJField(DbINFTable.class
                    .getDeclaredField("m_fragmentationMaxRangeValue"));
            fExtentSize.setJField(DbINFTable.class.getDeclaredField("m_extentSize"));
            fNextExtentSize.setJField(DbINFTable.class.getDeclaredField("m_nextExtentSize"));
            fLockMode.setJField(DbINFTable.class.getDeclaredField("m_lockMode"));
            fFragmentationKeyColumns.setJField(DbINFTable.class
                    .getDeclaredField("m_fragmentationKeyColumns"));
            fFragmentationKeyColumns.setFlags(MetaField.INTEGRABLE);

            fDbspace.setOppositeRel(DbINFDbspace.fTables);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    INFTableCategory m_category;
    DbINFDbspace m_dbspace;
    INFTableDistScheme m_fragmentationDistribScheme;
    boolean m_withRowIDs;
    SrInteger m_fragmentationMinRangeValue;
    SrInteger m_fragmentationMaxRangeValue;
    SrInteger m_extentSize;
    SrInteger m_nextExtentSize;
    INFLockMode m_lockMode;
    DbRelationN m_fragmentationKeyColumns;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbINFTable() {
    }

    /**
     * Creates an instance of DbINFTable.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbINFTable(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setWithRowIDs(Boolean.FALSE);
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));
    }

    //Setters

    /**
     * Sets the "category" property of a DbINFTable's instance.
     * 
     * @param value
     *            the "category" property
     **/
    public final void setCategory(INFTableCategory value) throws DbException {
        basicSet(fCategory, value);
    }

    /**
     * Sets the dbspace object associated to a DbINFTable's instance.
     * 
     * @param value
     *            the dbspace object to be associated
     **/
    public final void setDbspace(DbINFDbspace value) throws DbException {
        basicSet(fDbspace, value);
    }

    /**
     * Sets the "distribution scheme" property of a DbINFTable's instance.
     * 
     * @param value
     *            the "distribution scheme" property
     **/
    public final void setFragmentationDistribScheme(INFTableDistScheme value) throws DbException {
        basicSet(fFragmentationDistribScheme, value);
    }

    /**
     * Sets the "with rowids" property of a DbINFTable's instance.
     * 
     * @param value
     *            the "with rowids" property
     **/
    public final void setWithRowIDs(Boolean value) throws DbException {
        basicSet(fWithRowIDs, value);
    }

    /**
     * Sets the "minimum range value" property of a DbINFTable's instance.
     * 
     * @param value
     *            the "minimum range value" property
     **/
    public final void setFragmentationMinRangeValue(Integer value) throws DbException {
        basicSet(fFragmentationMinRangeValue, value);
    }

    /**
     * Sets the "maximum range value" property of a DbINFTable's instance.
     * 
     * @param value
     *            the "maximum range value" property
     **/
    public final void setFragmentationMaxRangeValue(Integer value) throws DbException {
        basicSet(fFragmentationMaxRangeValue, value);
    }

    /**
     * Sets the "extent size" property of a DbINFTable's instance.
     * 
     * @param value
     *            the "extent size" property
     **/
    public final void setExtentSize(Integer value) throws DbException {
        basicSet(fExtentSize, value);
    }

    /**
     * Sets the "next extent size" property of a DbINFTable's instance.
     * 
     * @param value
     *            the "next extent size" property
     **/
    public final void setNextExtentSize(Integer value) throws DbException {
        basicSet(fNextExtentSize, value);
    }

    /**
     * Sets the "lock mode" property of a DbINFTable's instance.
     * 
     * @param value
     *            the "lock mode" property
     **/
    public final void setLockMode(INFLockMode value) throws DbException {
        basicSet(fLockMode, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fFragmentationKeyColumns)
                ((DbINFColumn) value).setFragmentationKeyTable(this);
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
     * Gets the "category" of a DbINFTable's instance.
     * 
     * @return the "category"
     **/
    public final INFTableCategory getCategory() throws DbException {
        return (INFTableCategory) get(fCategory);
    }

    /**
     * Gets the dbspace object associated to a DbINFTable's instance.
     * 
     * @return the dbspace object
     **/
    public final DbINFDbspace getDbspace() throws DbException {
        return (DbINFDbspace) get(fDbspace);
    }

    /**
     * Gets the "distribution scheme" of a DbINFTable's instance.
     * 
     * @return the "distribution scheme"
     **/
    public final INFTableDistScheme getFragmentationDistribScheme() throws DbException {
        return (INFTableDistScheme) get(fFragmentationDistribScheme);
    }

    /**
     * Gets the "with rowids" property's Boolean value of a DbINFTable's instance.
     * 
     * @return the "with rowids" property's Boolean value
     * @deprecated use isWithRowIDs() method instead
     **/
    public final Boolean getWithRowIDs() throws DbException {
        return (Boolean) get(fWithRowIDs);
    }

    /**
     * Tells whether a DbINFTable's instance is withRowIDs or not.
     * 
     * @return boolean
     **/
    public final boolean isWithRowIDs() throws DbException {
        return getWithRowIDs().booleanValue();
    }

    /**
     * Gets the "minimum range value" of a DbINFTable's instance.
     * 
     * @return the "minimum range value"
     **/
    public final Integer getFragmentationMinRangeValue() throws DbException {
        return (Integer) get(fFragmentationMinRangeValue);
    }

    /**
     * Gets the "maximum range value" of a DbINFTable's instance.
     * 
     * @return the "maximum range value"
     **/
    public final Integer getFragmentationMaxRangeValue() throws DbException {
        return (Integer) get(fFragmentationMaxRangeValue);
    }

    /**
     * Gets the "extent size" of a DbINFTable's instance.
     * 
     * @return the "extent size"
     **/
    public final Integer getExtentSize() throws DbException {
        return (Integer) get(fExtentSize);
    }

    /**
     * Gets the "next extent size" of a DbINFTable's instance.
     * 
     * @return the "next extent size"
     **/
    public final Integer getNextExtentSize() throws DbException {
        return (Integer) get(fNextExtentSize);
    }

    /**
     * Gets the "lock mode" of a DbINFTable's instance.
     * 
     * @return the "lock mode"
     **/
    public final INFLockMode getLockMode() throws DbException {
        return (INFLockMode) get(fLockMode);
    }

    /**
     * Gets the list of fragmentation key columns associated to a DbINFTable's instance.
     * 
     * @return the list of fragmentation key columns.
     **/
    public final DbRelationN getFragmentationKeyColumns() throws DbException {
        return (DbRelationN) get(fFragmentationKeyColumns);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
