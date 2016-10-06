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
package org.modelsphere.sms.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.international.LocaleMgr;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSAssociationGo.html">DbSMSAssociationGo</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSInheritanceGo.html">DbSMSInheritanceGo</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSLinkGo.html">DbSMSLinkGo</A>, <A
 * HREF="../../../../org/modelsphere/sms/be/db/DbBEFlowGo.html">DbBEFlowGo</A>.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSDiagram.html">DbSMSDiagram</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public abstract class DbSMSLineGo extends DbSMSGraphicalObject {

    //Meta

    public static final MetaField fPolyline = new MetaField(LocaleMgr.db.getString("polyline"));
    public static final MetaField fRightAngle = new MetaField(LocaleMgr.db.getString("rightAngle"));
    public static final MetaRelation1 fFrontEndGo = new MetaRelation1(LocaleMgr.db
            .getString("frontEndGo"), 0);
    public static final MetaRelation1 fBackEndGo = new MetaRelation1(LocaleMgr.db
            .getString("backEndGo"), 0);
    public static final MetaField fDashStyle = new MetaField(LocaleMgr.db
            .getString("DbSMSLineGo.dashStyle"));
    public static final MetaField fHighlight = new MetaField(LocaleMgr.db
            .getString("DbSMSLineGo.highlight"));
    public static final MetaField fLineColor = new MetaField(LocaleMgr.db
            .getString("DbSMSLineGo.lineColor"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbSMSLineGo"),
            DbSMSLineGo.class, new MetaField[] { fPolyline, fRightAngle, fFrontEndGo, fBackEndGo,
                    fDashStyle, fHighlight, fLineColor }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSGraphicalObject.metaClass);

            fPolyline.setJField(DbSMSLineGo.class.getDeclaredField("m_polyline"));
            fRightAngle.setJField(DbSMSLineGo.class.getDeclaredField("m_rightAngle"));
            fFrontEndGo.setJField(DbSMSLineGo.class.getDeclaredField("m_frontEndGo"));
            fBackEndGo.setJField(DbSMSLineGo.class.getDeclaredField("m_backEndGo"));
            fDashStyle.setJField(DbSMSLineGo.class.getDeclaredField("m_dashStyle"));
            fHighlight.setJField(DbSMSLineGo.class.getDeclaredField("m_highlight"));
            fLineColor.setJField(DbSMSLineGo.class.getDeclaredField("m_lineColor"));

            fFrontEndGo.setOppositeRel(DbSMSGraphicalObject.fFrontEndLineGos);
            fBackEndGo.setOppositeRel(DbSMSGraphicalObject.fBackEndLineGos);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 3459917360015633627L;
    public static final Polygon DEFAULT_POLY = new Polygon(new int[] { 50, 300 }, new int[] { 150,
            150 }, 2);

    //Instance variables
    SrPolygon m_polyline;
    boolean m_rightAngle;
    DbSMSGraphicalObject m_frontEndGo;
    DbSMSGraphicalObject m_backEndGo;
    SrBoolean m_dashStyle;
    SrBoolean m_highlight;
    SrColor m_lineColor;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSLineGo() {
    }

    /**
     * Creates an instance of DbSMSLineGo.
     * 
     * @param composite
     *            org.modelsphere.sms.db.DbSMSDiagram
     * @param frontendgo
     *            org.modelsphere.sms.db.DbSMSGraphicalObject
     * @param backendgo
     *            org.modelsphere.sms.db.DbSMSGraphicalObject
     **/
    public DbSMSLineGo(DbSMSDiagram composite, DbSMSGraphicalObject frontEndGo,
            DbSMSGraphicalObject backEndGo) throws DbException {
        super(composite);
        basicSet(fFrontEndGo, frontEndGo);
        basicSet(fBackEndGo, backEndGo);
        setDefaultInitialValues();
        org.modelsphere.jack.srtool.graphic.DbGraphic.createPolyline(this);
    }

    private void setDefaultInitialValues() throws DbException {
        setRightAngle(Boolean.FALSE);
        setPolyline(DEFAULT_POLY);
    }

    /**

 **/
    public void resetLabelsPosition() throws DbException {
    }

    //Setters

    /**
     * Sets the "polygon" property of a DbSMSLineGo's instance.
     * 
     * @param value
     *            the "polygon" property
     **/
    public final void setPolyline(Polygon value) throws DbException {
        basicSet(fPolyline, value);
    }

    /**
     * Sets the "right angle?" property of a DbSMSLineGo's instance.
     * 
     * @param value
     *            the "right angle?" property
     **/
    public final void setRightAngle(Boolean value) throws DbException {
        basicSet(fRightAngle, value);
    }

    /**
     * Sets the front end graphical object object associated to a DbSMSLineGo's instance.
     * 
     * @param value
     *            the front end graphical object object to be associated
     **/
    public final void setFrontEndGo(DbSMSGraphicalObject value) throws DbException {
        basicSet(fFrontEndGo, value);
    }

    /**
     * Sets the back end graphical object object associated to a DbSMSLineGo's instance.
     * 
     * @param value
     *            the back end graphical object object to be associated
     **/
    public final void setBackEndGo(DbSMSGraphicalObject value) throws DbException {
        basicSet(fBackEndGo, value);
    }

    /**
     * Sets the "dash style" property of a DbSMSLineGo's instance.
     * 
     * @param value
     *            the "dash style" property
     **/
    public final void setDashStyle(Boolean value) throws DbException {
        basicSet(fDashStyle, value);
    }

    /**
     * Sets the "highlight" property of a DbSMSLineGo's instance.
     * 
     * @param value
     *            the "highlight" property
     **/
    public final void setHighlight(Boolean value) throws DbException {
        basicSet(fHighlight, value);
    }

    /**
     * Sets the "line color" property of a DbSMSLineGo's instance.
     * 
     * @param value
     *            the "line color" property
     **/
    public final void setLineColor(Color value) throws DbException {
        basicSet(fLineColor, value);
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
     * Gets the "polygon" of a DbSMSLineGo's instance.
     * 
     * @return the "polygon"
     **/
    public final Polygon getPolyline() throws DbException {
        return (Polygon) get(fPolyline);
    }

    /**
     * Gets the "right angle?" property's Boolean value of a DbSMSLineGo's instance.
     * 
     * @return the "right angle?" property's Boolean value
     * @deprecated use isRightAngle() method instead
     **/
    public final Boolean getRightAngle() throws DbException {
        return (Boolean) get(fRightAngle);
    }

    /**
     * Tells whether a DbSMSLineGo's instance is rightAngle or not.
     * 
     * @return boolean
     **/
    public final boolean isRightAngle() throws DbException {
        return getRightAngle().booleanValue();
    }

    /**
     * Gets the front end graphical object object associated to a DbSMSLineGo's instance.
     * 
     * @return the front end graphical object object
     **/
    public final DbSMSGraphicalObject getFrontEndGo() throws DbException {
        return (DbSMSGraphicalObject) get(fFrontEndGo);
    }

    /**
     * Gets the back end graphical object object associated to a DbSMSLineGo's instance.
     * 
     * @return the back end graphical object object
     **/
    public final DbSMSGraphicalObject getBackEndGo() throws DbException {
        return (DbSMSGraphicalObject) get(fBackEndGo);
    }

    /**
     * Gets the "dash style" of a DbSMSLineGo's instance.
     * 
     * @return the "dash style"
     **/
    public final Boolean getDashStyle() throws DbException {
        return (Boolean) get(fDashStyle);
    }

    /**
     * Gets the "highlight" of a DbSMSLineGo's instance.
     * 
     * @return the "highlight"
     **/
    public final Boolean getHighlight() throws DbException {
        return (Boolean) get(fHighlight);
    }

    /**
     * Gets the "line color" of a DbSMSLineGo's instance.
     * 
     * @return the "line color"
     **/
    public final Color getLineColor() throws DbException {
        return (Color) get(fLineColor);
    }

}
