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
 * HREF="../../../../org/modelsphere/sms/oo/db/DbOOAdtGo.html">DbOOAdtGo</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbORTableGo.html">DbORTableGo</A>, <A
 * HREF="../../../../org/modelsphere/sms/be/db/DbBEUseCaseGo.html">DbBEUseCaseGo</A>, <A
 * HREF="../../../../org/modelsphere/sms/be/db/DbBEActorGo.html">DbBEActorGo</A>, <A
 * HREF="../../../../org/modelsphere/sms/be/db/DbBEStoreGo.html">DbBEStoreGo</A>, <A
 * HREF="../../../../org/modelsphere/sms/be/db/DbBEContextGo.html">DbBEContextGo</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b>none.<br>
 **/
public abstract class DbSMSClassifierGo extends DbSMSGraphicalObject {

    //Meta

    public static final MetaRelation1 fClassifier = new MetaRelation1(LocaleMgr.db
            .getString("classifier"), 1);
    public static final MetaField fDashStyle = new MetaField(LocaleMgr.db.getString("dashStyle"));
    public static final MetaField fHighlight = new MetaField(LocaleMgr.db.getString("highlight"));
    public static final MetaField fLineColor = new MetaField(LocaleMgr.db.getString("lineColor"));
    public static final MetaField fTextColor = new MetaField(LocaleMgr.db.getString("textColor"));
    public static final MetaField fBackgroundColor = new MetaField(LocaleMgr.db
            .getString("backgroundColor"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSClassifierGo"), DbSMSClassifierGo.class, new MetaField[] {
            fClassifier, fDashStyle, fHighlight, fLineColor, fTextColor, fBackgroundColor }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSGraphicalObject.metaClass);

            fClassifier.setJField(DbSMSClassifierGo.class.getDeclaredField("m_classifier"));
            fClassifier.setFlags(MetaField.COPY_REFS);
            fClassifier.setVisibleInScreen(false);
            fDashStyle.setJField(DbSMSClassifierGo.class.getDeclaredField("m_dashStyle"));
            fDashStyle.setVisibleInScreen(false);
            fHighlight.setJField(DbSMSClassifierGo.class.getDeclaredField("m_highlight"));
            fHighlight.setVisibleInScreen(false);
            fLineColor.setJField(DbSMSClassifierGo.class.getDeclaredField("m_lineColor"));
            fLineColor.setVisibleInScreen(false);
            fTextColor.setJField(DbSMSClassifierGo.class.getDeclaredField("m_textColor"));
            fTextColor.setVisibleInScreen(false);
            fBackgroundColor.setJField(DbSMSClassifierGo.class
                    .getDeclaredField("m_backgroundColor"));
            fBackgroundColor.setVisibleInScreen(false);

            fClassifier.setOppositeRel(DbSMSClassifier.fClassifierGos);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbSMSClassifier m_classifier;
    SrBoolean m_dashStyle;
    SrBoolean m_highlight;
    SrColor m_lineColor;
    SrColor m_textColor;
    SrColor m_backgroundColor;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSClassifierGo() {
    }

    /**
     * Creates an instance of DbSMSClassifierGo.
     * 
     * @param composite
     *            org.modelsphere.sms.db.DbSMSDiagram
     * @param classifier
     *            org.modelsphere.sms.db.DbSMSClassifier
     **/
    public DbSMSClassifierGo(DbSMSDiagram composite, DbSMSClassifier classifier) throws DbException {
        super(composite);
        basicSet(fClassifier, classifier);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setRectangle(DEFAULT_RECT);
    }

    /**
     * @return boolean
     **/
    public final boolean isInSourceDiagram() throws DbException {
        return (getClassifier().getComposite() == getComposite().getComposite());
    }

    /**
     * @return dbobject
     **/
    public DbObject getSO() throws DbException {
        return this.getClassifier();
    }

    /**
     * Overriding methods must call super.remove()
     **/
    public void remove() throws DbException {
        DbObject so = this.getSO();
        boolean trueDelete = (so == null ? true : false);
        DbEnumeration dbEnum = getFrontEndLineGos().elements();
        while (dbEnum.hasMoreElements()) {
            DbSMSLineGo line = (DbSMSLineGo) dbEnum.nextElement();
            if (line.getTransStatus() != Db.OBJ_REMOVED && !trueDelete)
                line.remove();
        }
        dbEnum.close();
        dbEnum = getBackEndLineGos().elements();
        while (dbEnum.hasMoreElements()) {
            DbSMSLineGo line = (DbSMSLineGo) dbEnum.nextElement();
            if (line.getTransStatus() != Db.OBJ_REMOVED && !trueDelete)
                line.remove();
        }
        dbEnum.close();
        super.remove();
    }

    //Setters

    /**
     * Sets the classifier object associated to a DbSMSClassifierGo's instance.
     * 
     * @param value
     *            the classifier object to be associated
     **/
    public final void setClassifier(DbSMSClassifier value) throws DbException {
        basicSet(fClassifier, value);
    }

    /**
     * Sets the "dashstyle" property of a DbSMSClassifierGo's instance.
     * 
     * @param value
     *            the "dashstyle" property
     **/
    public final void setDashStyle(Boolean value) throws DbException {
        basicSet(fDashStyle, value);
    }

    /**
     * Sets the "highlight" property of a DbSMSClassifierGo's instance.
     * 
     * @param value
     *            the "highlight" property
     **/
    public final void setHighlight(Boolean value) throws DbException {
        basicSet(fHighlight, value);
    }

    /**
     * Sets the "linecolor" property of a DbSMSClassifierGo's instance.
     * 
     * @param value
     *            the "linecolor" property
     **/
    public final void setLineColor(Color value) throws DbException {
        basicSet(fLineColor, value);
    }

    /**
     * Sets the "textcolor" property of a DbSMSClassifierGo's instance.
     * 
     * @param value
     *            the "textcolor" property
     **/
    public final void setTextColor(Color value) throws DbException {
        basicSet(fTextColor, value);
    }

    /**
     * Sets the "backgroundcolor" property of a DbSMSClassifierGo's instance.
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
     * Gets the classifier object associated to a DbSMSClassifierGo's instance.
     * 
     * @return the classifier object
     **/
    public final DbSMSClassifier getClassifier() throws DbException {
        return (DbSMSClassifier) get(fClassifier);
    }

    /**
     * Gets the "dashstyle" of a DbSMSClassifierGo's instance.
     * 
     * @return the "dashstyle"
     **/
    public final Boolean getDashStyle() throws DbException {
        return (Boolean) get(fDashStyle);
    }

    /**
     * Gets the "highlight" of a DbSMSClassifierGo's instance.
     * 
     * @return the "highlight"
     **/
    public final Boolean getHighlight() throws DbException {
        return (Boolean) get(fHighlight);
    }

    /**
     * Gets the "linecolor" of a DbSMSClassifierGo's instance.
     * 
     * @return the "linecolor"
     **/
    public final Color getLineColor() throws DbException {
        return (Color) get(fLineColor);
    }

    /**
     * Gets the "textcolor" of a DbSMSClassifierGo's instance.
     * 
     * @return the "textcolor"
     **/
    public final Color getTextColor() throws DbException {
        return (Color) get(fTextColor);
    }

    /**
     * Gets the "backgroundcolor" of a DbSMSClassifierGo's instance.
     * 
     * @return the "backgroundcolor"
     **/
    public final Color getBackgroundColor() throws DbException {
        return (Color) get(fBackgroundColor);
    }

}
