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
public final class DbSMSFreeFormGo extends DbSMSFreeGraphicGo {

    //Meta

    public static final MetaField fBackgroundColor = new MetaField(LocaleMgr.db
            .getString("backgroundColor"));
    public static final MetaField fCategory = new MetaField(LocaleMgr.db.getString("category"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSFreeFormGo"), DbSMSFreeFormGo.class, new MetaField[] {
            fBackgroundColor, fCategory }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSFreeGraphicGo.metaClass);

            fBackgroundColor.setJField(DbSMSFreeFormGo.class.getDeclaredField("m_backgroundColor"));
            fCategory.setJField(DbSMSFreeFormGo.class.getDeclaredField("m_category"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    SrColor m_backgroundColor;
    SMSFreeForm m_category;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSFreeFormGo() {
    }

    /**
     * Creates an instance of DbSMSFreeFormGo.
     * 
     * @param composite
     *            org.modelsphere.jack.baseDb.db.DbObject
     * @param category
     *            org.modelsphere.sms.db.srtypes.SMSFreeForm
     **/
    public DbSMSFreeFormGo(DbObject composite, SMSFreeForm category) throws DbException {

        super(composite);
        setDefaultInitialValues();
        setCategory(category);
    }

    private void setDefaultInitialValues() throws DbException {
        setBackgroundColor(Color.white);

        setAutoFit(Boolean.FALSE);

    }

    //Setters

    /**
     * Sets the "backgroundcolor" property of a DbSMSFreeFormGo's instance.
     * 
     * @param value
     *            the "backgroundcolor" property
     **/
    public final void setBackgroundColor(Color value) throws DbException {
        basicSet(fBackgroundColor, value);
    }

    /**
     * Sets the "category" property of a DbSMSFreeFormGo's instance.
     * 
     * @param value
     *            the "category" property
     **/
    public final void setCategory(SMSFreeForm value) throws DbException {
        basicSet(fCategory, value);
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
     * Gets the "backgroundcolor" of a DbSMSFreeFormGo's instance.
     * 
     * @return the "backgroundcolor"
     **/
    public final Color getBackgroundColor() throws DbException {
        return (Color) get(fBackgroundColor);
    }

    /**
     * Gets the "category" of a DbSMSFreeFormGo's instance.
     * 
     * @return the "category"
     **/
    public final SMSFreeForm getCategory() throws DbException {
        return (SMSFreeForm) get(fCategory);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
