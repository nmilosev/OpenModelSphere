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
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORARollbackSegment extends DbSMSSemanticalObject {

    //Meta

    public static final MetaField fPublic = new MetaField(LocaleMgr.db.getString("public"));
    public static final MetaField fInitialExtent = new MetaField(LocaleMgr.db
            .getString("initialExtent"));
    public static final MetaField fInitialExtentSizeUnit = new MetaField(LocaleMgr.db
            .getString("initialExtentSizeUnit"));
    public static final MetaField fNextExtent = new MetaField(LocaleMgr.db.getString("nextExtent"));
    public static final MetaField fNextExtentSizeUnit = new MetaField(LocaleMgr.db
            .getString("nextExtentSizeUnit"));
    public static final MetaField fMinExtents = new MetaField(LocaleMgr.db.getString("minExtents"));
    public static final MetaField fMaxExtents = new MetaField(LocaleMgr.db.getString("maxExtents"));
    public static final MetaField fOptimalSize = new MetaField(LocaleMgr.db
            .getString("optimalSize"));
    public static final MetaField fOptimalSizeUnit = new MetaField(LocaleMgr.db
            .getString("optimalSizeUnit"));
    public static final MetaRelation1 fTablespace = new MetaRelation1(LocaleMgr.db
            .getString("tablespace"), 0);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORARollbackSegment"), DbORARollbackSegment.class, new MetaField[] {
            fPublic, fInitialExtent, fInitialExtentSizeUnit, fNextExtent, fNextExtentSizeUnit,
            fMinExtents, fMaxExtents, fOptimalSize, fOptimalSizeUnit, fTablespace },
            MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);
            metaClass.setIcon("dborarollbackseg.gif");

            fPublic.setJField(DbORARollbackSegment.class.getDeclaredField("m_public"));
            fInitialExtent
                    .setJField(DbORARollbackSegment.class.getDeclaredField("m_initialExtent"));
            fInitialExtentSizeUnit.setJField(DbORARollbackSegment.class
                    .getDeclaredField("m_initialExtentSizeUnit"));
            fNextExtent.setJField(DbORARollbackSegment.class.getDeclaredField("m_nextExtent"));
            fNextExtentSizeUnit.setJField(DbORARollbackSegment.class
                    .getDeclaredField("m_nextExtentSizeUnit"));
            fMinExtents.setJField(DbORARollbackSegment.class.getDeclaredField("m_minExtents"));
            fMaxExtents.setJField(DbORARollbackSegment.class.getDeclaredField("m_maxExtents"));
            fOptimalSize.setJField(DbORARollbackSegment.class.getDeclaredField("m_optimalSize"));
            fOptimalSizeUnit.setJField(DbORARollbackSegment.class
                    .getDeclaredField("m_optimalSizeUnit"));
            fTablespace.setJField(DbORARollbackSegment.class.getDeclaredField("m_tablespace"));
            fTablespace.setRendererPluginName("DbORATablespace");

            fTablespace.setOppositeRel(DbORATablespace.fRollbackSegments);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    boolean m_public;
    SrInteger m_initialExtent;
    ORASizeUnit m_initialExtentSizeUnit;
    SrInteger m_nextExtent;
    ORASizeUnit m_nextExtentSizeUnit;
    SrInteger m_minExtents;
    SrInteger m_maxExtents;
    SrInteger m_optimalSize;
    ORASizeUnit m_optimalSizeUnit;
    DbORATablespace m_tablespace;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORARollbackSegment() {
    }

    /**
     * Creates an instance of DbORARollbackSegment.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORARollbackSegment(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setPublic(Boolean.FALSE);
        setName(LocaleMgr.misc.getString("rollbackSegment"));
    }

    //Setters

    /**
     * Sets the "public" property of a DbORARollbackSegment's instance.
     * 
     * @param value
     *            the "public" property
     **/
    public final void setPublic(Boolean value) throws DbException {
        basicSet(fPublic, value);
    }

    /**
     * Sets the "initial extent" property of a DbORARollbackSegment's instance.
     * 
     * @param value
     *            the "initial extent" property
     **/
    public final void setInitialExtent(Integer value) throws DbException {
        basicSet(fInitialExtent, value);
    }

    /**
     * Sets the "initial extent size unit" property of a DbORARollbackSegment's instance.
     * 
     * @param value
     *            the "initial extent size unit" property
     **/
    public final void setInitialExtentSizeUnit(ORASizeUnit value) throws DbException {
        basicSet(fInitialExtentSizeUnit, value);
    }

    /**
     * Sets the "next extent" property of a DbORARollbackSegment's instance.
     * 
     * @param value
     *            the "next extent" property
     **/
    public final void setNextExtent(Integer value) throws DbException {
        basicSet(fNextExtent, value);
    }

    /**
     * Sets the "next extent size unit" property of a DbORARollbackSegment's instance.
     * 
     * @param value
     *            the "next extent size unit" property
     **/
    public final void setNextExtentSizeUnit(ORASizeUnit value) throws DbException {
        basicSet(fNextExtentSizeUnit, value);
    }

    /**
     * Sets the "minextents" property of a DbORARollbackSegment's instance.
     * 
     * @param value
     *            the "minextents" property
     **/
    public final void setMinExtents(Integer value) throws DbException {
        basicSet(fMinExtents, value);
    }

    /**
     * Sets the "maxextents" property of a DbORARollbackSegment's instance.
     * 
     * @param value
     *            the "maxextents" property
     **/
    public final void setMaxExtents(Integer value) throws DbException {
        basicSet(fMaxExtents, value);
    }

    /**
     * Sets the "optimal size" property of a DbORARollbackSegment's instance.
     * 
     * @param value
     *            the "optimal size" property
     **/
    public final void setOptimalSize(Integer value) throws DbException {
        basicSet(fOptimalSize, value);
    }

    /**
     * Sets the "optimal size unit" property of a DbORARollbackSegment's instance.
     * 
     * @param value
     *            the "optimal size unit" property
     **/
    public final void setOptimalSizeUnit(ORASizeUnit value) throws DbException {
        basicSet(fOptimalSizeUnit, value);
    }

    /**
     * Sets the tablespace object associated to a DbORARollbackSegment's instance.
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
     * Gets the "public" property's Boolean value of a DbORARollbackSegment's instance.
     * 
     * @return the "public" property's Boolean value
     * @deprecated use isPublic() method instead
     **/
    public final Boolean getPublic() throws DbException {
        return (Boolean) get(fPublic);
    }

    /**
     * Tells whether a DbORARollbackSegment's instance is public or not.
     * 
     * @return boolean
     **/
    public final boolean isPublic() throws DbException {
        return getPublic().booleanValue();
    }

    /**
     * Gets the "initial extent" of a DbORARollbackSegment's instance.
     * 
     * @return the "initial extent"
     **/
    public final Integer getInitialExtent() throws DbException {
        return (Integer) get(fInitialExtent);
    }

    /**
     * Gets the "initial extent size unit" of a DbORARollbackSegment's instance.
     * 
     * @return the "initial extent size unit"
     **/
    public final ORASizeUnit getInitialExtentSizeUnit() throws DbException {
        return (ORASizeUnit) get(fInitialExtentSizeUnit);
    }

    /**
     * Gets the "next extent" of a DbORARollbackSegment's instance.
     * 
     * @return the "next extent"
     **/
    public final Integer getNextExtent() throws DbException {
        return (Integer) get(fNextExtent);
    }

    /**
     * Gets the "next extent size unit" of a DbORARollbackSegment's instance.
     * 
     * @return the "next extent size unit"
     **/
    public final ORASizeUnit getNextExtentSizeUnit() throws DbException {
        return (ORASizeUnit) get(fNextExtentSizeUnit);
    }

    /**
     * Gets the "minextents" of a DbORARollbackSegment's instance.
     * 
     * @return the "minextents"
     **/
    public final Integer getMinExtents() throws DbException {
        return (Integer) get(fMinExtents);
    }

    /**
     * Gets the "maxextents" of a DbORARollbackSegment's instance.
     * 
     * @return the "maxextents"
     **/
    public final Integer getMaxExtents() throws DbException {
        return (Integer) get(fMaxExtents);
    }

    /**
     * Gets the "optimal size" of a DbORARollbackSegment's instance.
     * 
     * @return the "optimal size"
     **/
    public final Integer getOptimalSize() throws DbException {
        return (Integer) get(fOptimalSize);
    }

    /**
     * Gets the "optimal size unit" of a DbORARollbackSegment's instance.
     * 
     * @return the "optimal size unit"
     **/
    public final ORASizeUnit getOptimalSizeUnit() throws DbException {
        return (ORASizeUnit) get(fOptimalSizeUnit);
    }

    /**
     * Gets the tablespace object associated to a DbORARollbackSegment's instance.
     * 
     * @return the tablespace object
     **/
    public final DbORATablespace getTablespace() throws DbException {
        return (DbORATablespace) get(fTablespace);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
