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
public final class DbSMSPackageGo extends DbSMSGraphicalObject {

    //Meta

    public static final MetaRelation1 fPackage = new MetaRelation1(LocaleMgr.db
            .getString("package"), 1);
    public static final MetaField fDashStyle = new MetaField(LocaleMgr.db.getString("dashStyle"));
    public static final MetaField fHighlight = new MetaField(LocaleMgr.db.getString("highlight"));
    public static final MetaField fTextColor = new MetaField(LocaleMgr.db.getString("textColor"));
    public static final MetaField fLineColor = new MetaField(LocaleMgr.db.getString("lineColor"));
    public static final MetaField fBackgroundColor = new MetaField(LocaleMgr.db
            .getString("backgroundColor"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSPackageGo"), DbSMSPackageGo.class, new MetaField[] { fPackage,
            fDashStyle, fHighlight, fTextColor, fLineColor, fBackgroundColor }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSGraphicalObject.metaClass);

            fPackage.setJField(DbSMSPackageGo.class.getDeclaredField("m_package"));
            fPackage.setFlags(MetaField.COPY_REFS);
            fDashStyle.setJField(DbSMSPackageGo.class.getDeclaredField("m_dashStyle"));
            fHighlight.setJField(DbSMSPackageGo.class.getDeclaredField("m_highlight"));
            fTextColor.setJField(DbSMSPackageGo.class.getDeclaredField("m_textColor"));
            fLineColor.setJField(DbSMSPackageGo.class.getDeclaredField("m_lineColor"));
            fBackgroundColor.setJField(DbSMSPackageGo.class.getDeclaredField("m_backgroundColor"));

            fPackage.setOppositeRel(DbSMSPackage.fPackageGos);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = -6610694440517043431L;

    //Instance variables
    DbSMSPackage m_package;
    SrBoolean m_dashStyle;
    SrBoolean m_highlight;
    SrColor m_textColor;
    SrColor m_lineColor;
    SrColor m_backgroundColor;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSPackageGo() {
    }

    /**
     * Creates an instance of DbSMSPackageGo.
     * 
     * @param composite
     *            org.modelsphere.sms.db.DbSMSDiagram
     * @param pack
     *            org.modelsphere.sms.db.DbSMSPackage
     **/
    public DbSMSPackageGo(DbSMSDiagram composite, DbSMSPackage pack) throws DbException {
        super(composite);
        basicSet(fPackage, pack);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setRectangle(DEFAULT_RECT);
    }

    /**
     * @return dbobject
     **/
    public DbObject getSO() throws DbException {
        return this.getPackage();
    }

    //Setters

    /**
     * Sets the package object associated to a DbSMSPackageGo's instance.
     * 
     * @param value
     *            the package object to be associated
     **/
    public final void setPackage(DbSMSPackage value) throws DbException {
        basicSet(fPackage, value);
    }

    /**
     * Sets the "dashstyle" property of a DbSMSPackageGo's instance.
     * 
     * @param value
     *            the "dashstyle" property
     **/
    public final void setDashStyle(Boolean value) throws DbException {
        basicSet(fDashStyle, value);
    }

    /**
     * Sets the "highlight" property of a DbSMSPackageGo's instance.
     * 
     * @param value
     *            the "highlight" property
     **/
    public final void setHighlight(Boolean value) throws DbException {
        basicSet(fHighlight, value);
    }

    /**
     * Sets the "textcolor" property of a DbSMSPackageGo's instance.
     * 
     * @param value
     *            the "textcolor" property
     **/
    public final void setTextColor(Color value) throws DbException {
        basicSet(fTextColor, value);
    }

    /**
     * Sets the "linecolor" property of a DbSMSPackageGo's instance.
     * 
     * @param value
     *            the "linecolor" property
     **/
    public final void setLineColor(Color value) throws DbException {
        basicSet(fLineColor, value);
    }

    /**
     * Sets the "backgroundcolor" property of a DbSMSPackageGo's instance.
     * 
     * @param value
     *            the "backgroundcolor" property
     **/
    public final void setBackgroundColor(Color value) throws DbException {
        basicSet(fBackgroundColor, value);
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
     * Gets the package object associated to a DbSMSPackageGo's instance.
     * 
     * @return the package object
     **/
    public final DbSMSPackage getPackage() throws DbException {
        return (DbSMSPackage) get(fPackage);
    }

    /**
     * Gets the "dashstyle" of a DbSMSPackageGo's instance.
     * 
     * @return the "dashstyle"
     **/
    public final Boolean getDashStyle() throws DbException {
        return (Boolean) get(fDashStyle);
    }

    /**
     * Gets the "highlight" of a DbSMSPackageGo's instance.
     * 
     * @return the "highlight"
     **/
    public final Boolean getHighlight() throws DbException {
        return (Boolean) get(fHighlight);
    }

    /**
     * Gets the "textcolor" of a DbSMSPackageGo's instance.
     * 
     * @return the "textcolor"
     **/
    public final Color getTextColor() throws DbException {
        return (Color) get(fTextColor);
    }

    /**
     * Gets the "linecolor" of a DbSMSPackageGo's instance.
     * 
     * @return the "linecolor"
     **/
    public final Color getLineColor() throws DbException {
        return (Color) get(fLineColor);
    }

    /**
     * Gets the "backgroundcolor" of a DbSMSPackageGo's instance.
     * 
     * @return the "backgroundcolor"
     **/
    public final Color getBackgroundColor() throws DbException {
        return (Color) get(fBackgroundColor);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
