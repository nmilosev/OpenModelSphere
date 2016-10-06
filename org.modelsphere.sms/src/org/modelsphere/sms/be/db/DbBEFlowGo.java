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
package org.modelsphere.sms.be.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.be.db.srtypes.*;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEDiagram.html">DbBEDiagram</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSDiagram.html">DbSMSDiagram</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbBEFlowGo extends DbSMSLineGo {

    //Meta

    public static final MetaField fQualifiersOffset = new MetaField(LocaleMgr.db
            .getString("qualifiersOffset"));
    public static final MetaField fZone1Offset = new MetaField(LocaleMgr.db
            .getString("zone1Offset"));
    public static final MetaField fZone2Offset = new MetaField(LocaleMgr.db
            .getString("zone2Offset"));
    public static final MetaRelation1 fFlow = new MetaRelation1(LocaleMgr.db.getString("flow"), 1);
    public static final MetaField fCenterOffset = new MetaField(LocaleMgr.db
            .getString("centerOffset"));
    public static final MetaField fZone3Offset = new MetaField(LocaleMgr.db
            .getString("zone3Offset"));
    public static final MetaField fZone4Offset = new MetaField(LocaleMgr.db
            .getString("zone4Offset"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbBEFlowGo"),
            DbBEFlowGo.class, new MetaField[] { fQualifiersOffset, fZone1Offset, fZone2Offset,
                    fFlow, fCenterOffset, fZone3Offset, fZone4Offset }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSLineGo.metaClass);

            fQualifiersOffset.setJField(DbBEFlowGo.class.getDeclaredField("m_qualifiersOffset"));
            fZone1Offset.setJField(DbBEFlowGo.class.getDeclaredField("m_zone1Offset"));
            fZone2Offset.setJField(DbBEFlowGo.class.getDeclaredField("m_zone2Offset"));
            fFlow.setJField(DbBEFlowGo.class.getDeclaredField("m_flow"));
            fFlow.setFlags(MetaField.COPY_REFS);
            fCenterOffset.setJField(DbBEFlowGo.class.getDeclaredField("m_centerOffset"));
            fZone3Offset.setJField(DbBEFlowGo.class.getDeclaredField("m_zone3Offset"));
            fZone4Offset.setJField(DbBEFlowGo.class.getDeclaredField("m_zone4Offset"));

            fFlow.setOppositeRel(DbBEFlow.fFlowGos);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    SrPoint m_qualifiersOffset;
    SrPoint m_zone1Offset;
    SrPoint m_zone2Offset;
    DbBEFlow m_flow;
    SrPoint m_centerOffset;
    SrPoint m_zone3Offset;
    SrPoint m_zone4Offset;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBEFlowGo() {
    }

    /**
     * Creates an instance of DbBEFlowGo.
     * 
     * @param composite
     *            org.modelsphere.sms.db.DbSMSDiagram
     * @param frontendgo
     *            org.modelsphere.sms.db.DbSMSGraphicalObject
     * @param backendgo
     *            org.modelsphere.sms.db.DbSMSGraphicalObject
     * @param flow
     *            org.modelsphere.sms.be.db.DbBEFlow
     **/
    public DbBEFlowGo(DbSMSDiagram composite, DbSMSGraphicalObject frontEndGo,
            DbSMSGraphicalObject backEndGo, DbBEFlow flow) throws DbException {
        super(composite, frontEndGo, backEndGo);
        basicSet(fFlow, flow);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @return dbobject
     **/
    public DbObject getSO() throws DbException {
        return this.getFlow();
    }

    /**

 **/
    public void resetLabelsPosition() throws DbException {
        setQualifiersOffset(null);
        setZone1Offset(null);
        setZone2Offset(null);
        setCenterOffset(null);

    }

    //Setters

    /**
     * Sets the "qualifiers offset" property of a DbBEFlowGo's instance.
     * 
     * @param value
     *            the "qualifiers offset" property
     **/
    public final void setQualifiersOffset(Point value) throws DbException {
        basicSet(fQualifiersOffset, value);
    }

    /**
     * Sets the "label 1 offset" property of a DbBEFlowGo's instance.
     * 
     * @param value
     *            the "label 1 offset" property
     **/
    public final void setZone1Offset(Point value) throws DbException {
        basicSet(fZone1Offset, value);
    }

    /**
     * Sets the "label 2 offset" property of a DbBEFlowGo's instance.
     * 
     * @param value
     *            the "label 2 offset" property
     **/
    public final void setZone2Offset(Point value) throws DbException {
        basicSet(fZone2Offset, value);
    }

    /**
     * Sets the flow object associated to a DbBEFlowGo's instance.
     * 
     * @param value
     *            the flow object to be associated
     **/
    public final void setFlow(DbBEFlow value) throws DbException {
        basicSet(fFlow, value);
    }

    /**
     * Sets the "center offset" property of a DbBEFlowGo's instance.
     * 
     * @param value
     *            the "center offset" property
     **/
    public final void setCenterOffset(Point value) throws DbException {
        basicSet(fCenterOffset, value);
    }

    /**
     * Sets the "label 3 offset" property of a DbBEFlowGo's instance.
     * 
     * @param value
     *            the "label 3 offset" property
     **/
    public final void setZone3Offset(Point value) throws DbException {
        basicSet(fZone3Offset, value);
    }

    /**
     * Sets the "label 4 offset" property of a DbBEFlowGo's instance.
     * 
     * @param value
     *            the "label 4 offset" property
     **/
    public final void setZone4Offset(Point value) throws DbException {
        basicSet(fZone4Offset, value);
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
     * Gets the "qualifiers offset" of a DbBEFlowGo's instance.
     * 
     * @return the "qualifiers offset"
     **/
    public final Point getQualifiersOffset() throws DbException {
        return (Point) get(fQualifiersOffset);
    }

    /**
     * Gets the "label 1 offset" of a DbBEFlowGo's instance.
     * 
     * @return the "label 1 offset"
     **/
    public final Point getZone1Offset() throws DbException {
        return (Point) get(fZone1Offset);
    }

    /**
     * Gets the "label 2 offset" of a DbBEFlowGo's instance.
     * 
     * @return the "label 2 offset"
     **/
    public final Point getZone2Offset() throws DbException {
        return (Point) get(fZone2Offset);
    }

    /**
     * Gets the flow object associated to a DbBEFlowGo's instance.
     * 
     * @return the flow object
     **/
    public final DbBEFlow getFlow() throws DbException {
        return (DbBEFlow) get(fFlow);
    }

    /**
     * Gets the "center offset" of a DbBEFlowGo's instance.
     * 
     * @return the "center offset"
     **/
    public final Point getCenterOffset() throws DbException {
        return (Point) get(fCenterOffset);
    }

    /**
     * Gets the "label 3 offset" of a DbBEFlowGo's instance.
     * 
     * @return the "label 3 offset"
     **/
    public final Point getZone3Offset() throws DbException {
        return (Point) get(fZone3Offset);
    }

    /**
     * Gets the "label 4 offset" of a DbBEFlowGo's instance.
     * 
     * @return the "label 4 offset"
     **/
    public final Point getZone4Offset() throws DbException {
        return (Point) get(fZone4Offset);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
