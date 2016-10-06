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
 * HREF="../../../../org/modelsphere/sms/db/DbSMSFreeLineGo.html">DbSMSFreeLineGo</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSFreeFormGo.html">DbSMSFreeFormGo</A>.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSDiagram.html">DbSMSDiagram</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public abstract class DbSMSFreeGraphicGo extends DbSMSGraphicalObject {

    //Meta

    public static final MetaField fDashStyle = new MetaField(LocaleMgr.db.getString("dashStyle"));
    public static final MetaField fHighlight = new MetaField(LocaleMgr.db.getString("highlight"));
    public static final MetaField fLineColor = new MetaField(LocaleMgr.db.getString("lineColor"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSFreeGraphicGo"), DbSMSFreeGraphicGo.class, new MetaField[] {
            fDashStyle, fHighlight, fLineColor }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSGraphicalObject.metaClass);

            fDashStyle.setJField(DbSMSFreeGraphicGo.class.getDeclaredField("m_dashStyle"));
            fHighlight.setJField(DbSMSFreeGraphicGo.class.getDeclaredField("m_highlight"));
            fLineColor.setJField(DbSMSFreeGraphicGo.class.getDeclaredField("m_lineColor"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    SrBoolean m_dashStyle;
    SrBoolean m_highlight;
    SrColor m_lineColor;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSFreeGraphicGo() {
    }

    /**
     * Creates an instance of DbSMSFreeGraphicGo.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSFreeGraphicGo(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setDashStyle(Boolean.FALSE);
        setHighlight(Boolean.FALSE);
        setLineColor(Color.black);
    }

    /**
     * @return dbobject
     **/
    public DbObject getSO() throws DbException {
        return null;
    }

    //Setters

    /**
     * Sets the "dashstyle" property of a DbSMSFreeGraphicGo's instance.
     * 
     * @param value
     *            the "dashstyle" property
     **/
    public final void setDashStyle(Boolean value) throws DbException {
        basicSet(fDashStyle, value);
    }

    /**
     * Sets the "highlight" property of a DbSMSFreeGraphicGo's instance.
     * 
     * @param value
     *            the "highlight" property
     **/
    public final void setHighlight(Boolean value) throws DbException {
        basicSet(fHighlight, value);
    }

    /**
     * Sets the "linecolor" property of a DbSMSFreeGraphicGo's instance.
     * 
     * @param value
     *            the "linecolor" property
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
     * Gets the "dashstyle" of a DbSMSFreeGraphicGo's instance.
     * 
     * @return the "dashstyle"
     **/
    public final Boolean getDashStyle() throws DbException {
        return (Boolean) get(fDashStyle);
    }

    /**
     * Gets the "highlight" of a DbSMSFreeGraphicGo's instance.
     * 
     * @return the "highlight"
     **/
    public final Boolean getHighlight() throws DbException {
        return (Boolean) get(fHighlight);
    }

    /**
     * Gets the "linecolor" of a DbSMSFreeGraphicGo's instance.
     * 
     * @return the "linecolor"
     **/
    public final Color getLineColor() throws DbException {
        return (Color) get(fLineColor);
    }

}
