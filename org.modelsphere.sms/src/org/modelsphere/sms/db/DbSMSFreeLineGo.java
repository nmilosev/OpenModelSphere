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
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSDiagram.html">DbSMSDiagram</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbSMSFreeLineGo extends DbSMSFreeGraphicGo {

    //Meta

    public static final MetaField fRightAngle = new MetaField(LocaleMgr.db.getString("rightAngle"));
    public static final MetaField fPolyline = new MetaField(LocaleMgr.db.getString("polyline"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSFreeLineGo"), DbSMSFreeLineGo.class, new MetaField[] { fRightAngle,
            fPolyline }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSFreeGraphicGo.metaClass);

            fRightAngle.setJField(DbSMSFreeLineGo.class.getDeclaredField("m_rightAngle"));
            fPolyline.setJField(DbSMSFreeLineGo.class.getDeclaredField("m_polyline"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    boolean m_rightAngle;
    SrPolygon m_polyline;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSFreeLineGo() {
    }

    /**
     * Creates an instance of DbSMSFreeLineGo.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSFreeLineGo(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setRightAngle(Boolean.FALSE);
    }

    //Setters

    /**
     * Sets the "right angle?" property of a DbSMSFreeLineGo's instance.
     * 
     * @param value
     *            the "right angle?" property
     **/
    public final void setRightAngle(Boolean value) throws DbException {
        basicSet(fRightAngle, value);
    }

    /**
     * Sets the "polygon" property of a DbSMSFreeLineGo's instance.
     * 
     * @param value
     *            the "polygon" property
     **/
    public final void setPolyline(Polygon value) throws DbException {
        basicSet(fPolyline, value);
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
     * Gets the "right angle?" property's Boolean value of a DbSMSFreeLineGo's instance.
     * 
     * @return the "right angle?" property's Boolean value
     * @deprecated use isRightAngle() method instead
     **/
    public final Boolean getRightAngle() throws DbException {
        return (Boolean) get(fRightAngle);
    }

    /**
     * Tells whether a DbSMSFreeLineGo's instance is rightAngle or not.
     * 
     * @return boolean
     **/
    public final boolean isRightAngle() throws DbException {
        return getRightAngle().booleanValue();
    }

    /**
     * Gets the "polygon" of a DbSMSFreeLineGo's instance.
     * 
     * @return the "polygon"
     **/
    public final Polygon getPolyline() throws DbException {
        return (Polygon) get(fPolyline);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
