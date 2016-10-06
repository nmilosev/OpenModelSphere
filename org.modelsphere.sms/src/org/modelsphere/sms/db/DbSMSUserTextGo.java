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
public final class DbSMSUserTextGo extends DbSMSGraphicalObject {

    //Meta

    public static final MetaField fText = new MetaField(LocaleMgr.db.getString("text"));
    public static final MetaField fFont = new MetaField(LocaleMgr.db.getString("font"));
    public static final MetaField fBackgroundColor = new MetaField(LocaleMgr.db
            .getString("backgroundColor"));
    public static final MetaField fTextColor = new MetaField(LocaleMgr.db.getString("textColor"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSUserTextGo"), DbSMSUserTextGo.class, new MetaField[] { fText, fFont,
            fBackgroundColor, fTextColor }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSGraphicalObject.metaClass);

            fText.setJField(DbSMSUserTextGo.class.getDeclaredField("m_text"));
            fFont.setJField(DbSMSUserTextGo.class.getDeclaredField("m_font"));
            fBackgroundColor.setJField(DbSMSUserTextGo.class.getDeclaredField("m_backgroundColor"));
            fTextColor.setJField(DbSMSUserTextGo.class.getDeclaredField("m_textColor"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 909859608814574085L;

    //Instance variables
    String m_text;
    SrFont m_font;
    SrColor m_backgroundColor;
    SrColor m_textColor;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSUserTextGo() {
    }

    /**
     * Creates an instance of DbSMSUserTextGo.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSUserTextGo(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setFont(new Font("SansSerif", Font.PLAIN, 8));
        setBackgroundColor(new Color(255, 255, 255, Color.TRANSLUCENT));
        setTextColor(Color.black);
        setAutoFit(Boolean.FALSE);

    }

    /**
     * @return dbobject
     **/
    public DbObject getSO() throws DbException {
        return null;
    }

    //Setters

    /**
     * Sets the "text" property of a DbSMSUserTextGo's instance.
     * 
     * @param value
     *            the "text" property
     **/
    public final void setText(String value) throws DbException {
        basicSet(fText, value);
    }

    /**
     * Sets the "free text" property of a DbSMSUserTextGo's instance.
     * 
     * @param value
     *            the "free text" property
     **/
    public final void setFont(Font value) throws DbException {
        basicSet(fFont, value);
    }

    /**
     * Sets the "backgroundcolor" property of a DbSMSUserTextGo's instance.
     * 
     * @param value
     *            the "backgroundcolor" property
     **/
    public final void setBackgroundColor(Color value) throws DbException {
        basicSet(fBackgroundColor, value);
    }

    /**
     * Sets the "textcolor" property of a DbSMSUserTextGo's instance.
     * 
     * @param value
     *            the "textcolor" property
     **/
    public final void setTextColor(Color value) throws DbException {
        basicSet(fTextColor, value);
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
     * Gets the "text" property of a DbSMSUserTextGo's instance.
     * 
     * @return the "text" property
     **/
    public final String getText() throws DbException {
        return (String) get(fText);
    }

    /**
     * Gets the "free text" of a DbSMSUserTextGo's instance.
     * 
     * @return the "free text"
     **/
    public final Font getFont() throws DbException {
        return (Font) get(fFont);
    }

    /**
     * Gets the "backgroundcolor" of a DbSMSUserTextGo's instance.
     * 
     * @return the "backgroundcolor"
     **/
    public final Color getBackgroundColor() throws DbException {
        return (Color) get(fBackgroundColor);
    }

    /**
     * Gets the "textcolor" of a DbSMSUserTextGo's instance.
     * 
     * @return the "textcolor"
     **/
    public final Color getTextColor() throws DbException {
        return (Color) get(fTextColor);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
