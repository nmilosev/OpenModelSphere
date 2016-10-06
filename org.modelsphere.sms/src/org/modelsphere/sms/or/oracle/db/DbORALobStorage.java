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
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORATable.html">DbORATable</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORALobStorage extends DbSMSSemanticalObject {

    //Meta

    public static final MetaField fStorageInRow = new MetaField(LocaleMgr.db
            .getString("storageInRow"));
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
    public static final MetaField fChunkSize = new MetaField(LocaleMgr.db.getString("chunkSize"));
    public static final MetaField fPctversion = new MetaField(LocaleMgr.db.getString("pctversion"));
    public static final MetaField fCache = new MetaField(LocaleMgr.db.getString("cache"));
    public static final MetaField fLog = new MetaField(LocaleMgr.db.getString("log"));
    public static final MetaRelation1 fTablespace = new MetaRelation1(LocaleMgr.db
            .getString("tablespace"), 0);
    public static final MetaRelationN fLobItems = new MetaRelationN(LocaleMgr.db
            .getString("lobItems"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORALobStorage"), DbORALobStorage.class, new MetaField[] { fStorageInRow,
            fInitialExtent, fInitialExtentSizeUnit, fNextExtent, fNextExtentSizeUnit, fMinExtent,
            fMaxExtent, fUnlimitedExtents, fPctIncrease, fFreelists, fFreelistGroups, fBufferPool,
            fChunkSize, fPctversion, fCache, fLog, fTablespace, fLobItems },
            MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);
            metaClass.setIcon("dboralobstorage.gif");

            fStorageInRow.setJField(DbORALobStorage.class.getDeclaredField("m_storageInRow"));
            fInitialExtent.setJField(DbORALobStorage.class.getDeclaredField("m_initialExtent"));
            fInitialExtentSizeUnit.setJField(DbORALobStorage.class
                    .getDeclaredField("m_initialExtentSizeUnit"));
            fNextExtent.setJField(DbORALobStorage.class.getDeclaredField("m_nextExtent"));
            fNextExtentSizeUnit.setJField(DbORALobStorage.class
                    .getDeclaredField("m_nextExtentSizeUnit"));
            fMinExtent.setJField(DbORALobStorage.class.getDeclaredField("m_minExtent"));
            fMaxExtent.setJField(DbORALobStorage.class.getDeclaredField("m_maxExtent"));
            fUnlimitedExtents.setJField(DbORALobStorage.class
                    .getDeclaredField("m_unlimitedExtents"));
            fPctIncrease.setJField(DbORALobStorage.class.getDeclaredField("m_pctIncrease"));
            fFreelists.setJField(DbORALobStorage.class.getDeclaredField("m_freelists"));
            fFreelistGroups.setJField(DbORALobStorage.class.getDeclaredField("m_freelistGroups"));
            fBufferPool.setJField(DbORALobStorage.class.getDeclaredField("m_bufferPool"));
            fChunkSize.setJField(DbORALobStorage.class.getDeclaredField("m_chunkSize"));
            fPctversion.setJField(DbORALobStorage.class.getDeclaredField("m_pctversion"));
            fCache.setJField(DbORALobStorage.class.getDeclaredField("m_cache"));
            fLog.setJField(DbORALobStorage.class.getDeclaredField("m_log"));
            fTablespace.setJField(DbORALobStorage.class.getDeclaredField("m_tablespace"));
            fTablespace.setFlags(MetaField.INTEGRABLE_BY_NAME);
            fTablespace.setRendererPluginName("DbORATablespace");
            fLobItems.setJField(DbORALobStorage.class.getDeclaredField("m_lobItems"));

            fTablespace.setOppositeRel(DbORATablespace.fLobs);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    ORAEnableDisable m_storageInRow;
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
    SrInteger m_chunkSize;
    SrInteger m_pctversion;
    ORACache m_cache;
    ORALog m_log;
    DbORATablespace m_tablespace;
    DbRelationN m_lobItems;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORALobStorage() {
    }

    /**
     * Creates an instance of DbORALobStorage.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORALobStorage(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setUnlimitedExtents(Boolean.FALSE);
        setName(LocaleMgr.misc.getString("lobstorage"));
    }

    //Setters

    /**
     * Sets the "storage in row" property of a DbORALobStorage's instance.
     * 
     * @param value
     *            the "storage in row" property
     **/
    public final void setStorageInRow(ORAEnableDisable value) throws DbException {
        basicSet(fStorageInRow, value);
    }

    /**
     * Sets the "initial extent" property of a DbORALobStorage's instance.
     * 
     * @param value
     *            the "initial extent" property
     **/
    public final void setInitialExtent(Integer value) throws DbException {
        basicSet(fInitialExtent, value);
    }

    /**
     * Sets the "initial extent size unit" property of a DbORALobStorage's instance.
     * 
     * @param value
     *            the "initial extent size unit" property
     **/
    public final void setInitialExtentSizeUnit(ORASizeUnit value) throws DbException {
        basicSet(fInitialExtentSizeUnit, value);
    }

    /**
     * Sets the "next extent" property of a DbORALobStorage's instance.
     * 
     * @param value
     *            the "next extent" property
     **/
    public final void setNextExtent(Integer value) throws DbException {
        basicSet(fNextExtent, value);
    }

    /**
     * Sets the "next extent size unit" property of a DbORALobStorage's instance.
     * 
     * @param value
     *            the "next extent size unit" property
     **/
    public final void setNextExtentSizeUnit(ORASizeUnit value) throws DbException {
        basicSet(fNextExtentSizeUnit, value);
    }

    /**
     * Sets the "minextents" property of a DbORALobStorage's instance.
     * 
     * @param value
     *            the "minextents" property
     **/
    public final void setMinExtent(Integer value) throws DbException {
        basicSet(fMinExtent, value);
    }

    /**
     * Sets the "maxextents" property of a DbORALobStorage's instance.
     * 
     * @param value
     *            the "maxextents" property
     **/
    public final void setMaxExtent(Integer value) throws DbException {
        basicSet(fMaxExtent, value);
    }

    /**
     * Sets the "unlimited extents" property of a DbORALobStorage's instance.
     * 
     * @param value
     *            the "unlimited extents" property
     **/
    public final void setUnlimitedExtents(Boolean value) throws DbException {
        basicSet(fUnlimitedExtents, value);
    }

    /**
     * Sets the "pctincrease" property of a DbORALobStorage's instance.
     * 
     * @param value
     *            the "pctincrease" property
     **/
    public final void setPctIncrease(Integer value) throws DbException {
        basicSet(fPctIncrease, value);
    }

    /**
     * Sets the "freelists" property of a DbORALobStorage's instance.
     * 
     * @param value
     *            the "freelists" property
     **/
    public final void setFreelists(Integer value) throws DbException {
        basicSet(fFreelists, value);
    }

    /**
     * Sets the "freelist groups" property of a DbORALobStorage's instance.
     * 
     * @param value
     *            the "freelist groups" property
     **/
    public final void setFreelistGroups(Integer value) throws DbException {
        basicSet(fFreelistGroups, value);
    }

    /**
     * Sets the "buffer pool" property of a DbORALobStorage's instance.
     * 
     * @param value
     *            the "buffer pool" property
     **/
    public final void setBufferPool(ORABufferPool value) throws DbException {
        basicSet(fBufferPool, value);
    }

    /**
     * Sets the "chunk size" property of a DbORALobStorage's instance.
     * 
     * @param value
     *            the "chunk size" property
     **/
    public final void setChunkSize(Integer value) throws DbException {
        basicSet(fChunkSize, value);
    }

    /**
     * Sets the "pctversion" property of a DbORALobStorage's instance.
     * 
     * @param value
     *            the "pctversion" property
     **/
    public final void setPctversion(Integer value) throws DbException {
        basicSet(fPctversion, value);
    }

    /**
     * Sets the "cache" property of a DbORALobStorage's instance.
     * 
     * @param value
     *            the "cache" property
     **/
    public final void setCache(ORACache value) throws DbException {
        basicSet(fCache, value);
    }

    /**
     * Sets the "log" property of a DbORALobStorage's instance.
     * 
     * @param value
     *            the "log" property
     **/
    public final void setLog(ORALog value) throws DbException {
        basicSet(fLog, value);
    }

    /**
     * Sets the tablespace object associated to a DbORALobStorage's instance.
     * 
     * @param value
     *            the tablespace object to be associated
     **/
    public final void setTablespace(DbORATablespace value) throws DbException {
        basicSet(fTablespace, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fLobItems)
                ((DbORAColumn) value).setLobStorage(this);
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
     * Gets the "storage in row" of a DbORALobStorage's instance.
     * 
     * @return the "storage in row"
     **/
    public final ORAEnableDisable getStorageInRow() throws DbException {
        return (ORAEnableDisable) get(fStorageInRow);
    }

    /**
     * Gets the "initial extent" of a DbORALobStorage's instance.
     * 
     * @return the "initial extent"
     **/
    public final Integer getInitialExtent() throws DbException {
        return (Integer) get(fInitialExtent);
    }

    /**
     * Gets the "initial extent size unit" of a DbORALobStorage's instance.
     * 
     * @return the "initial extent size unit"
     **/
    public final ORASizeUnit getInitialExtentSizeUnit() throws DbException {
        return (ORASizeUnit) get(fInitialExtentSizeUnit);
    }

    /**
     * Gets the "next extent" of a DbORALobStorage's instance.
     * 
     * @return the "next extent"
     **/
    public final Integer getNextExtent() throws DbException {
        return (Integer) get(fNextExtent);
    }

    /**
     * Gets the "next extent size unit" of a DbORALobStorage's instance.
     * 
     * @return the "next extent size unit"
     **/
    public final ORASizeUnit getNextExtentSizeUnit() throws DbException {
        return (ORASizeUnit) get(fNextExtentSizeUnit);
    }

    /**
     * Gets the "minextents" of a DbORALobStorage's instance.
     * 
     * @return the "minextents"
     **/
    public final Integer getMinExtent() throws DbException {
        return (Integer) get(fMinExtent);
    }

    /**
     * Gets the "maxextents" of a DbORALobStorage's instance.
     * 
     * @return the "maxextents"
     **/
    public final Integer getMaxExtent() throws DbException {
        return (Integer) get(fMaxExtent);
    }

    /**
     * Gets the "unlimited extents" property's Boolean value of a DbORALobStorage's instance.
     * 
     * @return the "unlimited extents" property's Boolean value
     * @deprecated use isUnlimitedExtents() method instead
     **/
    public final Boolean getUnlimitedExtents() throws DbException {
        return (Boolean) get(fUnlimitedExtents);
    }

    /**
     * Tells whether a DbORALobStorage's instance is unlimitedExtents or not.
     * 
     * @return boolean
     **/
    public final boolean isUnlimitedExtents() throws DbException {
        return getUnlimitedExtents().booleanValue();
    }

    /**
     * Gets the "pctincrease" of a DbORALobStorage's instance.
     * 
     * @return the "pctincrease"
     **/
    public final Integer getPctIncrease() throws DbException {
        return (Integer) get(fPctIncrease);
    }

    /**
     * Gets the "freelists" of a DbORALobStorage's instance.
     * 
     * @return the "freelists"
     **/
    public final Integer getFreelists() throws DbException {
        return (Integer) get(fFreelists);
    }

    /**
     * Gets the "freelist groups" of a DbORALobStorage's instance.
     * 
     * @return the "freelist groups"
     **/
    public final Integer getFreelistGroups() throws DbException {
        return (Integer) get(fFreelistGroups);
    }

    /**
     * Gets the "buffer pool" of a DbORALobStorage's instance.
     * 
     * @return the "buffer pool"
     **/
    public final ORABufferPool getBufferPool() throws DbException {
        return (ORABufferPool) get(fBufferPool);
    }

    /**
     * Gets the "chunk size" of a DbORALobStorage's instance.
     * 
     * @return the "chunk size"
     **/
    public final Integer getChunkSize() throws DbException {
        return (Integer) get(fChunkSize);
    }

    /**
     * Gets the "pctversion" of a DbORALobStorage's instance.
     * 
     * @return the "pctversion"
     **/
    public final Integer getPctversion() throws DbException {
        return (Integer) get(fPctversion);
    }

    /**
     * Gets the "cache" of a DbORALobStorage's instance.
     * 
     * @return the "cache"
     **/
    public final ORACache getCache() throws DbException {
        return (ORACache) get(fCache);
    }

    /**
     * Gets the "log" of a DbORALobStorage's instance.
     * 
     * @return the "log"
     **/
    public final ORALog getLog() throws DbException {
        return (ORALog) get(fLog);
    }

    /**
     * Gets the tablespace object associated to a DbORALobStorage's instance.
     * 
     * @return the tablespace object
     **/
    public final DbORATablespace getTablespace() throws DbException {
        return (DbORATablespace) get(fTablespace);
    }

    /**
     * Gets the list of lob items associated to a DbORALobStorage's instance.
     * 
     * @return the list of lob items.
     **/
    public final DbRelationN getLobItems() throws DbException {
        return (DbRelationN) get(fLobItems);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
