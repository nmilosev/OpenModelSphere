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
import java.io.File;
import java.net.*;
import org.modelsphere.sms.preference.DiagramStampOptionGroup;
import org.modelsphere.jack.graphic.GraphicUtil;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSDiagram.html">DbSMSDiagram</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbSMSStampGo extends DbSMSGraphicalObject {

    //Meta

    public static final MetaField fCompanyLogo = new MetaField(LocaleMgr.db
            .getString("companyLogo"));
    public static final MetaField fDescription = new MetaField(LocaleMgr.db
            .getString("description"));
    public static final MetaField fDashStyle = new MetaField(LocaleMgr.db
            .getString("DbSMSStampGo.dashStyle"));
    public static final MetaField fHighlight = new MetaField(LocaleMgr.db
            .getString("DbSMSStampGo.highlight"));
    public static final MetaField fLineColor = new MetaField(LocaleMgr.db
            .getString("DbSMSStampGo.lineColor"));
    public static final MetaField fTextColor = new MetaField(LocaleMgr.db
            .getString("DbSMSStampGo.textColor"));
    public static final MetaField fBackgroundColor = new MetaField(LocaleMgr.db
            .getString("DbSMSStampGo.backgroundColor"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbSMSStampGo"),
            DbSMSStampGo.class, new MetaField[] { fCompanyLogo, fDescription, fDashStyle,
                    fHighlight, fLineColor, fTextColor, fBackgroundColor }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSGraphicalObject.metaClass);

            fCompanyLogo.setJField(DbSMSStampGo.class.getDeclaredField("m_companyLogo"));
            fDescription.setJField(DbSMSStampGo.class.getDeclaredField("m_description"));
            fDashStyle.setJField(DbSMSStampGo.class.getDeclaredField("m_dashStyle"));
            fHighlight.setJField(DbSMSStampGo.class.getDeclaredField("m_highlight"));
            fLineColor.setJField(DbSMSStampGo.class.getDeclaredField("m_lineColor"));
            fTextColor.setJField(DbSMSStampGo.class.getDeclaredField("m_textColor"));
            fBackgroundColor.setJField(DbSMSStampGo.class.getDeclaredField("m_backgroundColor"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 2766047372041724936L;

    //Instance variables
    SrImage m_companyLogo;
    String m_description;
    SrBoolean m_dashStyle;
    SrBoolean m_highlight;
    SrColor m_lineColor;
    SrColor m_textColor;
    SrColor m_backgroundColor;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSStampGo() {
    }

    /**
     * Creates an instance of DbSMSStampGo.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSStampGo(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setDashStyle(Boolean.FALSE);
        setHighlight(Boolean.TRUE);
        setLineColor(Color.black);
        setTextColor(Color.black);
        setBackgroundColor(Color.white);
        setDashStyle(Boolean.FALSE);
        setHighlight(Boolean.TRUE);
        setLineColor(Color.black);
        setTextColor(Color.black);
        setBackgroundColor(Color.white);
        Image image = null;
        boolean stampFound = false;
        DbSMSDiagram diag = (DbSMSDiagram) this.getComposite();
        DbEnumeration dbEnum = diag.getComponents().elements(getMetaClass());
        if (dbEnum.hasMoreElements()) {
            DbObject dbo = dbEnum.nextElement();
            if (!dbo.equals(this)) {
                image = ((DbSMSStampGo) dbo).getCompanyLogo();
                stampFound = true;
                setCompanyLogo(image);
            }
        }
        dbEnum.close();
        if (image == null && !stampFound && DiagramStampOptionGroup.isUseStampImage()) {
            image = GraphicUtil.loadImage(org.modelsphere.sms.Application.class,
                    "international/resources/sms_stamp.jpg");
            org.modelsphere.jack.graphic.GraphicUtil.waitForImage(image);
            setCompanyLogo(image);
        }
    }

    /**
     * @return dbobject
     **/
    public DbObject getSO() throws DbException {
        return null;
    }

    //Setters

    /**
     * Sets the "company logo" property of a DbSMSStampGo's instance.
     * 
     * @param value
     *            the "company logo" property
     **/
    public final void setCompanyLogo(Image value) throws DbException {
        basicSet(fCompanyLogo, value);
    }

    /**
     * Sets the "description" property of a DbSMSStampGo's instance.
     * 
     * @param value
     *            the "description" property
     **/
    public final void setDescription(String value) throws DbException {
        basicSet(fDescription, value);
    }

    /**
     * Sets the "dash style" property of a DbSMSStampGo's instance.
     * 
     * @param value
     *            the "dash style" property
     **/
    public final void setDashStyle(Boolean value) throws DbException {
        basicSet(fDashStyle, value);
    }

    /**
     * Sets the "highlight" property of a DbSMSStampGo's instance.
     * 
     * @param value
     *            the "highlight" property
     **/
    public final void setHighlight(Boolean value) throws DbException {
        basicSet(fHighlight, value);
    }

    /**
     * Sets the "line color" property of a DbSMSStampGo's instance.
     * 
     * @param value
     *            the "line color" property
     **/
    public final void setLineColor(Color value) throws DbException {
        basicSet(fLineColor, value);
    }

    /**
     * Sets the "text color" property of a DbSMSStampGo's instance.
     * 
     * @param value
     *            the "text color" property
     **/
    public final void setTextColor(Color value) throws DbException {
        basicSet(fTextColor, value);
    }

    /**
     * Sets the "background color" property of a DbSMSStampGo's instance.
     * 
     * @param value
     *            the "background color" property
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
     * Gets the "company logo" of a DbSMSStampGo's instance.
     * 
     * @return the "company logo"
     **/
    public final Image getCompanyLogo() throws DbException {
        return (Image) get(fCompanyLogo);
    }

    /**
     * Gets the "description" property of a DbSMSStampGo's instance.
     * 
     * @return the "description" property
     **/
    public final String getDescription() throws DbException {
        return (String) get(fDescription);
    }

    /**
     * Gets the "dash style" of a DbSMSStampGo's instance.
     * 
     * @return the "dash style"
     **/
    public final Boolean getDashStyle() throws DbException {
        return (Boolean) get(fDashStyle);
    }

    /**
     * Gets the "highlight" of a DbSMSStampGo's instance.
     * 
     * @return the "highlight"
     **/
    public final Boolean getHighlight() throws DbException {
        return (Boolean) get(fHighlight);
    }

    /**
     * Gets the "line color" of a DbSMSStampGo's instance.
     * 
     * @return the "line color"
     **/
    public final Color getLineColor() throws DbException {
        return (Color) get(fLineColor);
    }

    /**
     * Gets the "text color" of a DbSMSStampGo's instance.
     * 
     * @return the "text color"
     **/
    public final Color getTextColor() throws DbException {
        return (Color) get(fTextColor);
    }

    /**
     * Gets the "background color" of a DbSMSStampGo's instance.
     * 
     * @return the "background color"
     **/
    public final Color getBackgroundColor() throws DbException {
        return (Color) get(fBackgroundColor);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
