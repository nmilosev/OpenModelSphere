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
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORADatabase.html">DbORADatabase</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORADataFile.html">DbORADataFile</A>,
 * <A HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORATablespace extends DbSMSSemanticalObject {

    //Meta

    public static final MetaRelationN fLobs = new MetaRelationN(LocaleMgr.db.getString("lobs"), 0,
            MetaRelationN.cardN);
    public static final MetaRelationN fAbsPartitions = new MetaRelationN(LocaleMgr.db
            .getString("absPartitions"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fIndexes = new MetaRelationN(LocaleMgr.db
            .getString("indexes"), 0, MetaRelationN.cardN);
    public static final MetaField fMinExtent = new MetaField(LocaleMgr.db
            .getString("DbORATablespace.minExtent"));
    public static final MetaField fMinExtentSizeUnit = new MetaField(LocaleMgr.db
            .getString("minExtentSizeUnit"));
    public static final MetaField fLog = new MetaField(LocaleMgr.db.getString("log"));
    public static final MetaField fOnline = new MetaField(LocaleMgr.db.getString("online"));
    public static final MetaField fTemporary = new MetaField(LocaleMgr.db.getString("temporary"));
    public static final MetaField fDefObjInitialExtent = new MetaField(LocaleMgr.db
            .getString("defObjInitialExtent"));
    public static final MetaField fDefObjInitialExtentSizeUnit = new MetaField(LocaleMgr.db
            .getString("defObjInitialExtentSizeUnit"));
    public static final MetaField fDefObjNextExtent = new MetaField(LocaleMgr.db
            .getString("defObjNextExtent"));
    public static final MetaField fDefObjNextExtentSizeUnit = new MetaField(LocaleMgr.db
            .getString("defObjNextExtentSizeUnit"));
    public static final MetaField fDefObjMinExtents = new MetaField(LocaleMgr.db
            .getString("defObjMinExtents"));
    public static final MetaField fDefObjMaxExtents = new MetaField(LocaleMgr.db
            .getString("defObjMaxExtents"));
    public static final MetaField fDefObjUnlimitedExtents = new MetaField(LocaleMgr.db
            .getString("defObjUnlimitedExtents"));
    public static final MetaField fDefObjPctIncrease = new MetaField(LocaleMgr.db
            .getString("defObjPctIncrease"));
    public static final MetaField fExtentManagement = new MetaField(LocaleMgr.db
            .getString("extentManagement"));
    public static final MetaField fUniformExtentSize = new MetaField(LocaleMgr.db
            .getString("uniformExtentSize"));
    public static final MetaField fUniformExtentSizeUnit = new MetaField(LocaleMgr.db
            .getString("uniformExtentSizeUnit"));
    public static final MetaRelationN fRollbackSegments = new MetaRelationN(LocaleMgr.db
            .getString("rollbackSegments"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fTables = new MetaRelationN(LocaleMgr.db.getString("tables"),
            0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORATablespace"), DbORATablespace.class, new MetaField[] { fLobs,
            fAbsPartitions, fIndexes, fMinExtent, fMinExtentSizeUnit, fLog, fOnline, fTemporary,
            fDefObjInitialExtent, fDefObjInitialExtentSizeUnit, fDefObjNextExtent,
            fDefObjNextExtentSizeUnit, fDefObjMinExtents, fDefObjMaxExtents,
            fDefObjUnlimitedExtents, fDefObjPctIncrease, fExtentManagement, fUniformExtentSize,
            fUniformExtentSizeUnit, fRollbackSegments, fTables },
            MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbORADataFile.metaClass });
            metaClass.setIcon("dboratablespace.gif");

            fLobs.setJField(DbORATablespace.class.getDeclaredField("m_lobs"));
            fAbsPartitions.setJField(DbORATablespace.class.getDeclaredField("m_absPartitions"));
            fIndexes.setJField(DbORATablespace.class.getDeclaredField("m_indexes"));
            fMinExtent.setJField(DbORATablespace.class.getDeclaredField("m_minExtent"));
            fMinExtentSizeUnit.setJField(DbORATablespace.class
                    .getDeclaredField("m_minExtentSizeUnit"));
            fLog.setJField(DbORATablespace.class.getDeclaredField("m_log"));
            fOnline.setJField(DbORATablespace.class.getDeclaredField("m_online"));
            fTemporary.setJField(DbORATablespace.class.getDeclaredField("m_temporary"));
            fDefObjInitialExtent.setJField(DbORATablespace.class
                    .getDeclaredField("m_defObjInitialExtent"));
            fDefObjInitialExtentSizeUnit.setJField(DbORATablespace.class
                    .getDeclaredField("m_defObjInitialExtentSizeUnit"));
            fDefObjNextExtent.setJField(DbORATablespace.class
                    .getDeclaredField("m_defObjNextExtent"));
            fDefObjNextExtentSizeUnit.setJField(DbORATablespace.class
                    .getDeclaredField("m_defObjNextExtentSizeUnit"));
            fDefObjMinExtents.setJField(DbORATablespace.class
                    .getDeclaredField("m_defObjMinExtents"));
            fDefObjMaxExtents.setJField(DbORATablespace.class
                    .getDeclaredField("m_defObjMaxExtents"));
            fDefObjUnlimitedExtents.setJField(DbORATablespace.class
                    .getDeclaredField("m_defObjUnlimitedExtents"));
            fDefObjPctIncrease.setJField(DbORATablespace.class
                    .getDeclaredField("m_defObjPctIncrease"));
            fExtentManagement.setJField(DbORATablespace.class
                    .getDeclaredField("m_extentManagement"));
            fUniformExtentSize.setJField(DbORATablespace.class
                    .getDeclaredField("m_uniformExtentSize"));
            fUniformExtentSizeUnit.setJField(DbORATablespace.class
                    .getDeclaredField("m_uniformExtentSizeUnit"));
            fRollbackSegments.setJField(DbORATablespace.class
                    .getDeclaredField("m_rollbackSegments"));
            fTables.setJField(DbORATablespace.class.getDeclaredField("m_tables"));

            fTables.setOppositeRel(DbORATable.fTablespaces);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbRelationN m_lobs;
    DbRelationN m_absPartitions;
    DbRelationN m_indexes;
    SrInteger m_minExtent;
    ORASizeUnit m_minExtentSizeUnit;
    ORALog m_log;
    ORAOnline m_online;
    boolean m_temporary;
    SrInteger m_defObjInitialExtent;
    ORASizeUnit m_defObjInitialExtentSizeUnit;
    SrInteger m_defObjNextExtent;
    ORASizeUnit m_defObjNextExtentSizeUnit;
    SrInteger m_defObjMinExtents;
    SrInteger m_defObjMaxExtents;
    boolean m_defObjUnlimitedExtents;
    SrInteger m_defObjPctIncrease;
    ORAExtentManagement m_extentManagement;
    SrInteger m_uniformExtentSize;
    ORASizeUnit m_uniformExtentSizeUnit;
    DbRelationN m_rollbackSegments;
    DbRelationN m_tables;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORATablespace() {
    }

    /**
     * Creates an instance of DbORATablespace.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORATablespace(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setTemporary(Boolean.FALSE);
        setDefObjUnlimitedExtents(Boolean.FALSE);
        setName(LocaleMgr.misc.getString("tablespace"));
    }

    //Setters

    /**
     * Sets the "minimum extent size" property of a DbORATablespace's instance.
     * 
     * @param value
     *            the "minimum extent size" property
     **/
    public final void setMinExtent(Integer value) throws DbException {
        basicSet(fMinExtent, value);
    }

    /**
     * Sets the "minimum extent size unit" property of a DbORATablespace's instance.
     * 
     * @param value
     *            the "minimum extent size unit" property
     **/
    public final void setMinExtentSizeUnit(ORASizeUnit value) throws DbException {
        basicSet(fMinExtentSizeUnit, value);
    }

    /**
     * Sets the "log" property of a DbORATablespace's instance.
     * 
     * @param value
     *            the "log" property
     **/
    public final void setLog(ORALog value) throws DbException {
        basicSet(fLog, value);
    }

    /**
     * Sets the "online" property of a DbORATablespace's instance.
     * 
     * @param value
     *            the "online" property
     **/
    public final void setOnline(ORAOnline value) throws DbException {
        basicSet(fOnline, value);
    }

    /**
     * Sets the "temporary" property of a DbORATablespace's instance.
     * 
     * @param value
     *            the "temporary" property
     **/
    public final void setTemporary(Boolean value) throws DbException {
        basicSet(fTemporary, value);
    }

    /**
     * Sets the "default object initial extent size" property of a DbORATablespace's instance.
     * 
     * @param value
     *            the "default object initial extent size" property
     **/
    public final void setDefObjInitialExtent(Integer value) throws DbException {
        basicSet(fDefObjInitialExtent, value);
    }

    /**
     * Sets the "default object initial extent size unit" property of a DbORATablespace's instance.
     * 
     * @param value
     *            the "default object initial extent size unit" property
     **/
    public final void setDefObjInitialExtentSizeUnit(ORASizeUnit value) throws DbException {
        basicSet(fDefObjInitialExtentSizeUnit, value);
    }

    /**
     * Sets the "default object next extent size" property of a DbORATablespace's instance.
     * 
     * @param value
     *            the "default object next extent size" property
     **/
    public final void setDefObjNextExtent(Integer value) throws DbException {
        basicSet(fDefObjNextExtent, value);
    }

    /**
     * Sets the "default object next extent size unit" property of a DbORATablespace's instance.
     * 
     * @param value
     *            the "default object next extent size unit" property
     **/
    public final void setDefObjNextExtentSizeUnit(ORASizeUnit value) throws DbException {
        basicSet(fDefObjNextExtentSizeUnit, value);
    }

    /**
     * Sets the "default object minextents" property of a DbORATablespace's instance.
     * 
     * @param value
     *            the "default object minextents" property
     **/
    public final void setDefObjMinExtents(Integer value) throws DbException {
        basicSet(fDefObjMinExtents, value);
    }

    /**
     * Sets the "default object maxextents" property of a DbORATablespace's instance.
     * 
     * @param value
     *            the "default object maxextents" property
     **/
    public final void setDefObjMaxExtents(Integer value) throws DbException {
        basicSet(fDefObjMaxExtents, value);
    }

    /**
     * Sets the "default object unlimited extents" property of a DbORATablespace's instance.
     * 
     * @param value
     *            the "default object unlimited extents" property
     **/
    public final void setDefObjUnlimitedExtents(Boolean value) throws DbException {
        basicSet(fDefObjUnlimitedExtents, value);
    }

    /**
     * Sets the "default object pctincrease" property of a DbORATablespace's instance.
     * 
     * @param value
     *            the "default object pctincrease" property
     **/
    public final void setDefObjPctIncrease(Integer value) throws DbException {
        basicSet(fDefObjPctIncrease, value);
    }

    /**
     * Sets the "extent management" property of a DbORATablespace's instance.
     * 
     * @param value
     *            the "extent management" property
     **/
    public final void setExtentManagement(ORAExtentManagement value) throws DbException {
        basicSet(fExtentManagement, value);
    }

    /**
     * Sets the "uniform extent size" property of a DbORATablespace's instance.
     * 
     * @param value
     *            the "uniform extent size" property
     **/
    public final void setUniformExtentSize(Integer value) throws DbException {
        basicSet(fUniformExtentSize, value);
    }

    /**
     * Sets the "uniform extent size unit" property of a DbORATablespace's instance.
     * 
     * @param value
     *            the "uniform extent size unit" property
     **/
    public final void setUniformExtentSizeUnit(ORASizeUnit value) throws DbException {
        basicSet(fUniformExtentSizeUnit, value);
    }

    /**
     * Adds an element to or removes an element from the list of tables associated to a
     * DbORATablespace's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setTables(DbORATable value, int op) throws DbException {
        setRelationNN(fTables, value, op);
    }

    /**
     * Adds an element to the list of tables associated to a DbORATablespace's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToTables(DbORATable value) throws DbException {
        setTables(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of tables associated to a DbORATablespace's instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromTables(DbORATable value) throws DbException {
        setTables(value, Db.REMOVE_FROM_RELN);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fLobs)
                ((DbORALobStorage) value).setTablespace(this);
            else if (metaField == fAbsPartitions)
                ((DbORAAbsPartition) value).setTablespace(this);
            else if (metaField == fIndexes)
                ((DbORAIndex) value).setTablespace(this);
            else if (metaField == fRollbackSegments)
                ((DbORARollbackSegment) value).setTablespace(this);
            else if (metaField == fTables)
                setTables((DbORATable) value, Db.ADD_TO_RELN);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fTables)
            setTables((DbORATable) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the list of lobs associated to a DbORATablespace's instance.
     * 
     * @return the list of lobs.
     **/
    public final DbRelationN getLobs() throws DbException {
        return (DbRelationN) get(fLobs);
    }

    /**
     * Gets the list of partitions associated to a DbORATablespace's instance.
     * 
     * @return the list of partitions.
     **/
    public final DbRelationN getAbsPartitions() throws DbException {
        return (DbRelationN) get(fAbsPartitions);
    }

    /**
     * Gets the list of indexes associated to a DbORATablespace's instance.
     * 
     * @return the list of indexes.
     **/
    public final DbRelationN getIndexes() throws DbException {
        return (DbRelationN) get(fIndexes);
    }

    /**
     * Gets the "minimum extent size" of a DbORATablespace's instance.
     * 
     * @return the "minimum extent size"
     **/
    public final Integer getMinExtent() throws DbException {
        return (Integer) get(fMinExtent);
    }

    /**
     * Gets the "minimum extent size unit" of a DbORATablespace's instance.
     * 
     * @return the "minimum extent size unit"
     **/
    public final ORASizeUnit getMinExtentSizeUnit() throws DbException {
        return (ORASizeUnit) get(fMinExtentSizeUnit);
    }

    /**
     * Gets the "log" of a DbORATablespace's instance.
     * 
     * @return the "log"
     **/
    public final ORALog getLog() throws DbException {
        return (ORALog) get(fLog);
    }

    /**
     * Gets the "online" of a DbORATablespace's instance.
     * 
     * @return the "online"
     **/
    public final ORAOnline getOnline() throws DbException {
        return (ORAOnline) get(fOnline);
    }

    /**
     * Gets the "temporary" property's Boolean value of a DbORATablespace's instance.
     * 
     * @return the "temporary" property's Boolean value
     * @deprecated use isTemporary() method instead
     **/
    public final Boolean getTemporary() throws DbException {
        return (Boolean) get(fTemporary);
    }

    /**
     * Tells whether a DbORATablespace's instance is temporary or not.
     * 
     * @return boolean
     **/
    public final boolean isTemporary() throws DbException {
        return getTemporary().booleanValue();
    }

    /**
     * Gets the "default object initial extent size" of a DbORATablespace's instance.
     * 
     * @return the "default object initial extent size"
     **/
    public final Integer getDefObjInitialExtent() throws DbException {
        return (Integer) get(fDefObjInitialExtent);
    }

    /**
     * Gets the "default object initial extent size unit" of a DbORATablespace's instance.
     * 
     * @return the "default object initial extent size unit"
     **/
    public final ORASizeUnit getDefObjInitialExtentSizeUnit() throws DbException {
        return (ORASizeUnit) get(fDefObjInitialExtentSizeUnit);
    }

    /**
     * Gets the "default object next extent size" of a DbORATablespace's instance.
     * 
     * @return the "default object next extent size"
     **/
    public final Integer getDefObjNextExtent() throws DbException {
        return (Integer) get(fDefObjNextExtent);
    }

    /**
     * Gets the "default object next extent size unit" of a DbORATablespace's instance.
     * 
     * @return the "default object next extent size unit"
     **/
    public final ORASizeUnit getDefObjNextExtentSizeUnit() throws DbException {
        return (ORASizeUnit) get(fDefObjNextExtentSizeUnit);
    }

    /**
     * Gets the "default object minextents" of a DbORATablespace's instance.
     * 
     * @return the "default object minextents"
     **/
    public final Integer getDefObjMinExtents() throws DbException {
        return (Integer) get(fDefObjMinExtents);
    }

    /**
     * Gets the "default object maxextents" of a DbORATablespace's instance.
     * 
     * @return the "default object maxextents"
     **/
    public final Integer getDefObjMaxExtents() throws DbException {
        return (Integer) get(fDefObjMaxExtents);
    }

    /**
     * Gets the "default object unlimited extents" property's Boolean value of a DbORATablespace's
     * instance.
     * 
     * @return the "default object unlimited extents" property's Boolean value
     * @deprecated use isDefObjUnlimitedExtents() method instead
     **/
    public final Boolean getDefObjUnlimitedExtents() throws DbException {
        return (Boolean) get(fDefObjUnlimitedExtents);
    }

    /**
     * Tells whether a DbORATablespace's instance is defObjUnlimitedExtents or not.
     * 
     * @return boolean
     **/
    public final boolean isDefObjUnlimitedExtents() throws DbException {
        return getDefObjUnlimitedExtents().booleanValue();
    }

    /**
     * Gets the "default object pctincrease" of a DbORATablespace's instance.
     * 
     * @return the "default object pctincrease"
     **/
    public final Integer getDefObjPctIncrease() throws DbException {
        return (Integer) get(fDefObjPctIncrease);
    }

    /**
     * Gets the "extent management" of a DbORATablespace's instance.
     * 
     * @return the "extent management"
     **/
    public final ORAExtentManagement getExtentManagement() throws DbException {
        return (ORAExtentManagement) get(fExtentManagement);
    }

    /**
     * Gets the "uniform extent size" of a DbORATablespace's instance.
     * 
     * @return the "uniform extent size"
     **/
    public final Integer getUniformExtentSize() throws DbException {
        return (Integer) get(fUniformExtentSize);
    }

    /**
     * Gets the "uniform extent size unit" of a DbORATablespace's instance.
     * 
     * @return the "uniform extent size unit"
     **/
    public final ORASizeUnit getUniformExtentSizeUnit() throws DbException {
        return (ORASizeUnit) get(fUniformExtentSizeUnit);
    }

    /**
     * Gets the list of rollback segments associated to a DbORATablespace's instance.
     * 
     * @return the list of rollback segments.
     **/
    public final DbRelationN getRollbackSegments() throws DbException {
        return (DbRelationN) get(fRollbackSegments);
    }

    /**
     * Gets the list of tables associated to a DbORATablespace's instance.
     * 
     * @return the list of tables.
     **/
    public final DbRelationN getTables() throws DbException {
        return (DbRelationN) get(fTables);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
