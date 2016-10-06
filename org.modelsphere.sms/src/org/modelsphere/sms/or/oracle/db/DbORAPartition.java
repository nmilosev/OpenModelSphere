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
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORATable.html">DbORATable</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAIndex.html">DbORAIndex</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORASubPartition.html"
 * >DbORASubPartition</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORAPartition extends DbORAAbsPartition {

    //Meta

    public static final MetaField fValueList = new MetaField(LocaleMgr.db.getString("valueList"));
    public static final MetaField fPctfree = new MetaField(LocaleMgr.db.getString("pctfree"));
    public static final MetaField fPctused = new MetaField(LocaleMgr.db.getString("pctused"));
    public static final MetaField fInitrans = new MetaField(LocaleMgr.db.getString("initrans"));
    public static final MetaField fMaxtrans = new MetaField(LocaleMgr.db.getString("maxtrans"));
    public static final MetaField fInitialExtent = new MetaField(LocaleMgr.db
            .getString("initialExtent"));
    public static final MetaField fInitialExtentSizeUnit = new MetaField(LocaleMgr.db
            .getString("initialExtentSizeUnit"));
    public static final MetaField fNextExtent = new MetaField(LocaleMgr.db.getString("nextExtent"));
    public static final MetaField fNextExtentSizeUnit = new MetaField(LocaleMgr.db
            .getString("nextExtentSizeUnit"));
    public static final MetaField fMinExtents = new MetaField(LocaleMgr.db.getString("minExtents"));
    public static final MetaField fMaxExtents = new MetaField(LocaleMgr.db.getString("maxExtents"));
    public static final MetaField fUnlimitedExtents = new MetaField(LocaleMgr.db
            .getString("unlimitedExtents"));
    public static final MetaField fPctIncrease = new MetaField(LocaleMgr.db
            .getString("pctIncrease"));
    public static final MetaField fFreelists = new MetaField(LocaleMgr.db.getString("freelists"));
    public static final MetaField fFreelistGroups = new MetaField(LocaleMgr.db
            .getString("freelistGroups"));
    public static final MetaField fBufferPool = new MetaField(LocaleMgr.db.getString("bufferPool"));
    public static final MetaField fLog = new MetaField(LocaleMgr.db.getString("log"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORAPartition"), DbORAPartition.class, new MetaField[] { fValueList,
            fPctfree, fPctused, fInitrans, fMaxtrans, fInitialExtent, fInitialExtentSizeUnit,
            fNextExtent, fNextExtentSizeUnit, fMinExtents, fMaxExtents, fUnlimitedExtents,
            fPctIncrease, fFreelists, fFreelistGroups, fBufferPool, fLog }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORAAbsPartition.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbORASubPartition.metaClass });

            fValueList.setJField(DbORAPartition.class.getDeclaredField("m_valueList"));
            fPctfree.setJField(DbORAPartition.class.getDeclaredField("m_pctfree"));
            fPctused.setJField(DbORAPartition.class.getDeclaredField("m_pctused"));
            fInitrans.setJField(DbORAPartition.class.getDeclaredField("m_initrans"));
            fMaxtrans.setJField(DbORAPartition.class.getDeclaredField("m_maxtrans"));
            fInitialExtent.setJField(DbORAPartition.class.getDeclaredField("m_initialExtent"));
            fInitialExtentSizeUnit.setJField(DbORAPartition.class
                    .getDeclaredField("m_initialExtentSizeUnit"));
            fNextExtent.setJField(DbORAPartition.class.getDeclaredField("m_nextExtent"));
            fNextExtentSizeUnit.setJField(DbORAPartition.class
                    .getDeclaredField("m_nextExtentSizeUnit"));
            fMinExtents.setJField(DbORAPartition.class.getDeclaredField("m_minExtents"));
            fMaxExtents.setJField(DbORAPartition.class.getDeclaredField("m_maxExtents"));
            fUnlimitedExtents
                    .setJField(DbORAPartition.class.getDeclaredField("m_unlimitedExtents"));
            fPctIncrease.setJField(DbORAPartition.class.getDeclaredField("m_pctIncrease"));
            fFreelists.setJField(DbORAPartition.class.getDeclaredField("m_freelists"));
            fFreelistGroups.setJField(DbORAPartition.class.getDeclaredField("m_freelistGroups"));
            fBufferPool.setJField(DbORAPartition.class.getDeclaredField("m_bufferPool"));
            fLog.setJField(DbORAPartition.class.getDeclaredField("m_log"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    String m_valueList;
    SrInteger m_pctfree;
    SrInteger m_pctused;
    SrInteger m_initrans;
    SrInteger m_maxtrans;
    SrInteger m_initialExtent;
    ORASizeUnit m_initialExtentSizeUnit;
    SrInteger m_nextExtent;
    ORASizeUnit m_nextExtentSizeUnit;
    SrInteger m_minExtents;
    SrInteger m_maxExtents;
    boolean m_unlimitedExtents;
    SrInteger m_pctIncrease;
    SrInteger m_freelists;
    SrInteger m_freelistGroups;
    ORABufferPool m_bufferPool;
    ORALog m_log;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORAPartition() {
    }

    /**
     * Creates an instance of DbORAPartition.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORAPartition(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setUnlimitedExtents(Boolean.FALSE);
        setName(LocaleMgr.misc.getString("partition"));
    }

    //Setters

    /**
     * Sets the "value list" property of a DbORAPartition's instance.
     * 
     * @param value
     *            the "value list" property
     **/
    public final void setValueList(String value) throws DbException {
        basicSet(fValueList, value);
    }

    /**
     * Sets the "pctfree" property of a DbORAPartition's instance.
     * 
     * @param value
     *            the "pctfree" property
     **/
    public final void setPctfree(Integer value) throws DbException {
        basicSet(fPctfree, value);
    }

    /**
     * Sets the "pctused" property of a DbORAPartition's instance.
     * 
     * @param value
     *            the "pctused" property
     **/
    public final void setPctused(Integer value) throws DbException {
        basicSet(fPctused, value);
    }

    /**
     * Sets the "initrans" property of a DbORAPartition's instance.
     * 
     * @param value
     *            the "initrans" property
     **/
    public final void setInitrans(Integer value) throws DbException {
        basicSet(fInitrans, value);
    }

    /**
     * Sets the "maxtrans" property of a DbORAPartition's instance.
     * 
     * @param value
     *            the "maxtrans" property
     **/
    public final void setMaxtrans(Integer value) throws DbException {
        basicSet(fMaxtrans, value);
    }

    /**
     * Sets the "initial extent" property of a DbORAPartition's instance.
     * 
     * @param value
     *            the "initial extent" property
     **/
    public final void setInitialExtent(Integer value) throws DbException {
        basicSet(fInitialExtent, value);
    }

    /**
     * Sets the "initial extent size unit" property of a DbORAPartition's instance.
     * 
     * @param value
     *            the "initial extent size unit" property
     **/
    public final void setInitialExtentSizeUnit(ORASizeUnit value) throws DbException {
        basicSet(fInitialExtentSizeUnit, value);
    }

    /**
     * Sets the "next extent" property of a DbORAPartition's instance.
     * 
     * @param value
     *            the "next extent" property
     **/
    public final void setNextExtent(Integer value) throws DbException {
        basicSet(fNextExtent, value);
    }

    /**
     * Sets the "next extent size unit" property of a DbORAPartition's instance.
     * 
     * @param value
     *            the "next extent size unit" property
     **/
    public final void setNextExtentSizeUnit(ORASizeUnit value) throws DbException {
        basicSet(fNextExtentSizeUnit, value);
    }

    /**
     * Sets the "minextents" property of a DbORAPartition's instance.
     * 
     * @param value
     *            the "minextents" property
     **/
    public final void setMinExtents(Integer value) throws DbException {
        basicSet(fMinExtents, value);
    }

    /**
     * Sets the "maxextents" property of a DbORAPartition's instance.
     * 
     * @param value
     *            the "maxextents" property
     **/
    public final void setMaxExtents(Integer value) throws DbException {
        basicSet(fMaxExtents, value);
    }

    /**
     * Sets the "unlimited extents" property of a DbORAPartition's instance.
     * 
     * @param value
     *            the "unlimited extents" property
     **/
    public final void setUnlimitedExtents(Boolean value) throws DbException {
        basicSet(fUnlimitedExtents, value);
    }

    /**
     * Sets the "pctincrease" property of a DbORAPartition's instance.
     * 
     * @param value
     *            the "pctincrease" property
     **/
    public final void setPctIncrease(Integer value) throws DbException {
        basicSet(fPctIncrease, value);
    }

    /**
     * Sets the "freelists" property of a DbORAPartition's instance.
     * 
     * @param value
     *            the "freelists" property
     **/
    public final void setFreelists(Integer value) throws DbException {
        basicSet(fFreelists, value);
    }

    /**
     * Sets the "freelist groups" property of a DbORAPartition's instance.
     * 
     * @param value
     *            the "freelist groups" property
     **/
    public final void setFreelistGroups(Integer value) throws DbException {
        basicSet(fFreelistGroups, value);
    }

    /**
     * Sets the "buffer pool" property of a DbORAPartition's instance.
     * 
     * @param value
     *            the "buffer pool" property
     **/
    public final void setBufferPool(ORABufferPool value) throws DbException {
        basicSet(fBufferPool, value);
    }

    /**
     * Sets the "log" property of a DbORAPartition's instance.
     * 
     * @param value
     *            the "log" property
     **/
    public final void setLog(ORALog value) throws DbException {
        basicSet(fLog, value);
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
     * Gets the "value list" property of a DbORAPartition's instance.
     * 
     * @return the "value list" property
     **/
    public final String getValueList() throws DbException {
        return (String) get(fValueList);
    }

    /**
     * Gets the "pctfree" of a DbORAPartition's instance.
     * 
     * @return the "pctfree"
     **/
    public final Integer getPctfree() throws DbException {
        return (Integer) get(fPctfree);
    }

    /**
     * Gets the "pctused" of a DbORAPartition's instance.
     * 
     * @return the "pctused"
     **/
    public final Integer getPctused() throws DbException {
        return (Integer) get(fPctused);
    }

    /**
     * Gets the "initrans" of a DbORAPartition's instance.
     * 
     * @return the "initrans"
     **/
    public final Integer getInitrans() throws DbException {
        return (Integer) get(fInitrans);
    }

    /**
     * Gets the "maxtrans" of a DbORAPartition's instance.
     * 
     * @return the "maxtrans"
     **/
    public final Integer getMaxtrans() throws DbException {
        return (Integer) get(fMaxtrans);
    }

    /**
     * Gets the "initial extent" of a DbORAPartition's instance.
     * 
     * @return the "initial extent"
     **/
    public final Integer getInitialExtent() throws DbException {
        return (Integer) get(fInitialExtent);
    }

    /**
     * Gets the "initial extent size unit" of a DbORAPartition's instance.
     * 
     * @return the "initial extent size unit"
     **/
    public final ORASizeUnit getInitialExtentSizeUnit() throws DbException {
        return (ORASizeUnit) get(fInitialExtentSizeUnit);
    }

    /**
     * Gets the "next extent" of a DbORAPartition's instance.
     * 
     * @return the "next extent"
     **/
    public final Integer getNextExtent() throws DbException {
        return (Integer) get(fNextExtent);
    }

    /**
     * Gets the "next extent size unit" of a DbORAPartition's instance.
     * 
     * @return the "next extent size unit"
     **/
    public final ORASizeUnit getNextExtentSizeUnit() throws DbException {
        return (ORASizeUnit) get(fNextExtentSizeUnit);
    }

    /**
     * Gets the "minextents" of a DbORAPartition's instance.
     * 
     * @return the "minextents"
     **/
    public final Integer getMinExtents() throws DbException {
        return (Integer) get(fMinExtents);
    }

    /**
     * Gets the "maxextents" of a DbORAPartition's instance.
     * 
     * @return the "maxextents"
     **/
    public final Integer getMaxExtents() throws DbException {
        return (Integer) get(fMaxExtents);
    }

    /**
     * Gets the "unlimited extents" property's Boolean value of a DbORAPartition's instance.
     * 
     * @return the "unlimited extents" property's Boolean value
     * @deprecated use isUnlimitedExtents() method instead
     **/
    public final Boolean getUnlimitedExtents() throws DbException {
        return (Boolean) get(fUnlimitedExtents);
    }

    /**
     * Tells whether a DbORAPartition's instance is unlimitedExtents or not.
     * 
     * @return boolean
     **/
    public final boolean isUnlimitedExtents() throws DbException {
        return getUnlimitedExtents().booleanValue();
    }

    /**
     * Gets the "pctincrease" of a DbORAPartition's instance.
     * 
     * @return the "pctincrease"
     **/
    public final Integer getPctIncrease() throws DbException {
        return (Integer) get(fPctIncrease);
    }

    /**
     * Gets the "freelists" of a DbORAPartition's instance.
     * 
     * @return the "freelists"
     **/
    public final Integer getFreelists() throws DbException {
        return (Integer) get(fFreelists);
    }

    /**
     * Gets the "freelist groups" of a DbORAPartition's instance.
     * 
     * @return the "freelist groups"
     **/
    public final Integer getFreelistGroups() throws DbException {
        return (Integer) get(fFreelistGroups);
    }

    /**
     * Gets the "buffer pool" of a DbORAPartition's instance.
     * 
     * @return the "buffer pool"
     **/
    public final ORABufferPool getBufferPool() throws DbException {
        return (ORABufferPool) get(fBufferPool);
    }

    /**
     * Gets the "log" of a DbORAPartition's instance.
     * 
     * @return the "log"
     **/
    public final ORALog getLog() throws DbException {
        return (ORALog) get(fLog);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
