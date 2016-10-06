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
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMTable.html">DbIBMTable</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMView.html">DbIBMView</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/db/DbORIndexKey.html">DbORIndexKey</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbIBMIndex extends DbORIndex {

    //Meta

    public static final MetaField fPctFree = new MetaField(LocaleMgr.db.getString("pctFree"));
    public static final MetaField fMinPctUsed = new MetaField(LocaleMgr.db.getString("minPctUsed"));
    public static final MetaField fAllowReverseScans = new MetaField(LocaleMgr.db
            .getString("allowReverseScans"));
    public static final MetaField fCollectStatistics = new MetaField(LocaleMgr.db
            .getString("collectStatistics"));
    public static final MetaField fSpecificationOnly = new MetaField(LocaleMgr.db
            .getString("specificationOnly"));
    public static final MetaField fCluster = new MetaField(LocaleMgr.db.getString("cluster"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbIBMIndex"),
            DbIBMIndex.class, new MetaField[] { fPctFree, fMinPctUsed, fAllowReverseScans,
                    fCollectStatistics, fSpecificationOnly, fCluster }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORIndex.metaClass);

            fPctFree.setJField(DbIBMIndex.class.getDeclaredField("m_pctFree"));
            fMinPctUsed.setJField(DbIBMIndex.class.getDeclaredField("m_minPctUsed"));
            fAllowReverseScans.setJField(DbIBMIndex.class.getDeclaredField("m_allowReverseScans"));
            fCollectStatistics.setJField(DbIBMIndex.class.getDeclaredField("m_collectStatistics"));
            fSpecificationOnly.setJField(DbIBMIndex.class.getDeclaredField("m_specificationOnly"));
            fSpecificationOnly.setVisibleInScreen(false);
            fCluster.setJField(DbIBMIndex.class.getDeclaredField("m_cluster"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    int m_pctFree;
    int m_minPctUsed;
    boolean m_allowReverseScans;
    IBMIndexCollectStatsType m_collectStatistics;
    boolean m_specificationOnly;
    boolean m_cluster;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbIBMIndex() {
    }

    /**
     * Creates an instance of DbIBMIndex.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbIBMIndex(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));
    }

    //Setters

    /**
     * Sets the "percentage free" property of a DbIBMIndex's instance.
     * 
     * @param value
     *            the "percentage free" property
     **/
    public final void setPctFree(Integer value) throws DbException {
        basicSet(fPctFree, value);
    }

    /**
     * Sets the "minimum percentage used" property of a DbIBMIndex's instance.
     * 
     * @param value
     *            the "minimum percentage used" property
     **/
    public final void setMinPctUsed(Integer value) throws DbException {
        basicSet(fMinPctUsed, value);
    }

    /**
     * Sets the "allow reverse scans" property of a DbIBMIndex's instance.
     * 
     * @param value
     *            the "allow reverse scans" property
     **/
    public final void setAllowReverseScans(Boolean value) throws DbException {
        basicSet(fAllowReverseScans, value);
    }

    /**
     * Sets the "collect statistics" property of a DbIBMIndex's instance.
     * 
     * @param value
     *            the "collect statistics" property
     **/
    public final void setCollectStatistics(IBMIndexCollectStatsType value) throws DbException {
        basicSet(fCollectStatistics, value);
    }

    /**
     * Sets the "specification only" property of a DbIBMIndex's instance.
     * 
     * @param value
     *            the "specification only" property
     **/
    public final void setSpecificationOnly(Boolean value) throws DbException {
        basicSet(fSpecificationOnly, value);
    }

    /**
     * Sets the "cluster" property of a DbIBMIndex's instance.
     * 
     * @param value
     *            the "cluster" property
     **/
    public final void setCluster(Boolean value) throws DbException {
        basicSet(fCluster, value);
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
     * Gets the "percentage free" property of a DbIBMIndex's instance.
     * 
     * @return the "percentage free" property
     **/
    public final Integer getPctFree() throws DbException {
        return (Integer) get(fPctFree);
    }

    /**
     * Gets the "minimum percentage used" property of a DbIBMIndex's instance.
     * 
     * @return the "minimum percentage used" property
     **/
    public final Integer getMinPctUsed() throws DbException {
        return (Integer) get(fMinPctUsed);
    }

    /**
     * Gets the "allow reverse scans" property's Boolean value of a DbIBMIndex's instance.
     * 
     * @return the "allow reverse scans" property's Boolean value
     * @deprecated use isAllowReverseScans() method instead
     **/
    public final Boolean getAllowReverseScans() throws DbException {
        return (Boolean) get(fAllowReverseScans);
    }

    /**
     * Tells whether a DbIBMIndex's instance is allowReverseScans or not.
     * 
     * @return boolean
     **/
    public final boolean isAllowReverseScans() throws DbException {
        return getAllowReverseScans().booleanValue();
    }

    /**
     * Gets the "collect statistics" of a DbIBMIndex's instance.
     * 
     * @return the "collect statistics"
     **/
    public final IBMIndexCollectStatsType getCollectStatistics() throws DbException {
        return (IBMIndexCollectStatsType) get(fCollectStatistics);
    }

    /**
     * Gets the "specification only" property's Boolean value of a DbIBMIndex's instance.
     * 
     * @return the "specification only" property's Boolean value
     * @deprecated use isSpecificationOnly() method instead
     **/
    public final Boolean getSpecificationOnly() throws DbException {
        return (Boolean) get(fSpecificationOnly);
    }

    /**
     * Tells whether a DbIBMIndex's instance is specificationOnly or not.
     * 
     * @return boolean
     **/
    public final boolean isSpecificationOnly() throws DbException {
        return getSpecificationOnly().booleanValue();
    }

    /**
     * Gets the "cluster" property's Boolean value of a DbIBMIndex's instance.
     * 
     * @return the "cluster" property's Boolean value
     * @deprecated use isCluster() method instead
     **/
    public final Boolean getCluster() throws DbException {
        return (Boolean) get(fCluster);
    }

    /**
     * Tells whether a DbIBMIndex's instance is cluster or not.
     * 
     * @return boolean
     **/
    public final boolean isCluster() throws DbException {
        return getCluster().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
