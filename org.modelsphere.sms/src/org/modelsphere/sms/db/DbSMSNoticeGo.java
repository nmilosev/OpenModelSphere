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
public final class DbSMSNoticeGo extends DbSMSGraphicalObject {

    //Meta

    public static final MetaField fDashStyle = new MetaField(LocaleMgr.db.getString("dashStyle"));
    public static final MetaField fHighlight = new MetaField(LocaleMgr.db.getString("highlight"));
    public static final MetaField fLineColor = new MetaField(LocaleMgr.db.getString("lineColor"));
    public static final MetaField fTextColor = new MetaField(LocaleMgr.db.getString("textColor"));
    public static final MetaField fBackgroundColor = new MetaField(LocaleMgr.db
            .getString("backgroundColor"));
    public static final MetaRelation1 fNotice = new MetaRelation1(LocaleMgr.db.getString("notice"),
            1);

    public static final MetaClass metaClass = new MetaClass(
            LocaleMgr.db.getString("DbSMSNoticeGo"), DbSMSNoticeGo.class, new MetaField[] {
                    fDashStyle, fHighlight, fLineColor, fTextColor, fBackgroundColor, fNotice },
            MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSGraphicalObject.metaClass);

            fDashStyle.setJField(DbSMSNoticeGo.class.getDeclaredField("m_dashStyle"));
            fHighlight.setJField(DbSMSNoticeGo.class.getDeclaredField("m_highlight"));
            fLineColor.setJField(DbSMSNoticeGo.class.getDeclaredField("m_lineColor"));
            fTextColor.setJField(DbSMSNoticeGo.class.getDeclaredField("m_textColor"));
            fBackgroundColor.setJField(DbSMSNoticeGo.class.getDeclaredField("m_backgroundColor"));
            fNotice.setJField(DbSMSNoticeGo.class.getDeclaredField("m_notice"));
            fNotice.setFlags(MetaField.COPY_REFS);

            fNotice.setOppositeRel(DbSMSNotice.fNoticeGos);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    public static final int DEFAULT_HEIGHT = 70;
    public static final int DEFAULT_WIDTH = 100;

    //Instance variables
    SrBoolean m_dashStyle;
    SrBoolean m_highlight;
    SrColor m_lineColor;
    SrColor m_textColor;
    SrColor m_backgroundColor;
    DbSMSNotice m_notice;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSNoticeGo() {
    }

    /**
     * Creates an instance of DbSMSNoticeGo.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSNoticeGo(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setAutoFit(Boolean.FALSE);
    }

    /**
     * @return dbobject
     **/
    public DbObject getSO() throws DbException {
        return this.getNotice();
    }

    //Setters

    /**
     * Sets the "dashstyle" property of a DbSMSNoticeGo's instance.
     * 
     * @param value
     *            the "dashstyle" property
     **/
    public final void setDashStyle(Boolean value) throws DbException {
        basicSet(fDashStyle, value);
    }

    /**
     * Sets the "highlight" property of a DbSMSNoticeGo's instance.
     * 
     * @param value
     *            the "highlight" property
     **/
    public final void setHighlight(Boolean value) throws DbException {
        basicSet(fHighlight, value);
    }

    /**
     * Sets the "linecolor" property of a DbSMSNoticeGo's instance.
     * 
     * @param value
     *            the "linecolor" property
     **/
    public final void setLineColor(Color value) throws DbException {
        basicSet(fLineColor, value);
    }

    /**
     * Sets the "textcolor" property of a DbSMSNoticeGo's instance.
     * 
     * @param value
     *            the "textcolor" property
     **/
    public final void setTextColor(Color value) throws DbException {
        basicSet(fTextColor, value);
    }

    /**
     * Sets the "backgroundcolor" property of a DbSMSNoticeGo's instance.
     * 
     * @param value
     *            the "backgroundcolor" property
     **/
    public final void setBackgroundColor(Color value) throws DbException {
        basicSet(fBackgroundColor, value);
    }

    /**
     * Sets the notice object associated to a DbSMSNoticeGo's instance.
     * 
     * @param value
     *            the notice object to be associated
     **/
    public final void setNotice(DbSMSNotice value) throws DbException {
        basicSet(fNotice, value);
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
     * Gets the "dashstyle" of a DbSMSNoticeGo's instance.
     * 
     * @return the "dashstyle"
     **/
    public final Boolean getDashStyle() throws DbException {
        return (Boolean) get(fDashStyle);
    }

    /**
     * Gets the "highlight" of a DbSMSNoticeGo's instance.
     * 
     * @return the "highlight"
     **/
    public final Boolean getHighlight() throws DbException {
        return (Boolean) get(fHighlight);
    }

    /**
     * Gets the "linecolor" of a DbSMSNoticeGo's instance.
     * 
     * @return the "linecolor"
     **/
    public final Color getLineColor() throws DbException {
        return (Color) get(fLineColor);
    }

    /**
     * Gets the "textcolor" of a DbSMSNoticeGo's instance.
     * 
     * @return the "textcolor"
     **/
    public final Color getTextColor() throws DbException {
        return (Color) get(fTextColor);
    }

    /**
     * Gets the "backgroundcolor" of a DbSMSNoticeGo's instance.
     * 
     * @return the "backgroundcolor"
     **/
    public final Color getBackgroundColor() throws DbException {
        return (Color) get(fBackgroundColor);
    }

    /**
     * Gets the notice object associated to a DbSMSNoticeGo's instance.
     * 
     * @return the notice object
     **/
    public final DbSMSNotice getNotice() throws DbException {
        return (DbSMSNotice) get(fNotice);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
