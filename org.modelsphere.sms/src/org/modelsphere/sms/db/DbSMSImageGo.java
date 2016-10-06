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
public final class DbSMSImageGo extends DbSMSGraphicalObject {

    //Meta

    public static final MetaField fDiagramImage = new MetaField(LocaleMgr.db
            .getString("diagramImage"));
    public static final MetaField fOpacityFactor = new MetaField(LocaleMgr.db
            .getString("opacityFactor"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbSMSImageGo"),
            DbSMSImageGo.class, new MetaField[] { fDiagramImage, fOpacityFactor }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSGraphicalObject.metaClass);

            fDiagramImage.setJField(DbSMSImageGo.class.getDeclaredField("m_diagramImage"));
            fOpacityFactor.setJField(DbSMSImageGo.class.getDeclaredField("m_opacityFactor"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 2766047372041724936L;

    //Instance variables
    SrImage m_diagramImage;
    float m_opacityFactor;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSImageGo() {
    }

    /**
     * Creates an instance of DbSMSImageGo.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSImageGo(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setOpacityFactor(new Float(1.0f));
    }

    /**
     * @return dbobject
     **/
    public DbObject getSO() throws DbException {
        return null;
    }

    //Setters

    /**
     * Sets the "diagram image" property of a DbSMSImageGo's instance.
     * 
     * @param value
     *            the "diagram image" property
     **/
    public final void setDiagramImage(Image value) throws DbException {
        basicSet(fDiagramImage, value);
    }

    /**
     * Sets the "opacity factor" property of a DbSMSImageGo's instance.
     * 
     * @param value
     *            the "opacity factor" property
     **/
    public final void setOpacityFactor(Float value) throws DbException {
        basicSet(fOpacityFactor, value);
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
     * Gets the "diagram image" of a DbSMSImageGo's instance.
     * 
     * @return the "diagram image"
     **/
    public final Image getDiagramImage() throws DbException {
        return (Image) get(fDiagramImage);
    }

    /**
     * Gets the "opacity factor" property of a DbSMSImageGo's instance.
     * 
     * @return the "opacity factor" property
     **/
    public final Float getOpacityFactor() throws DbException {
        return (Float) get(fOpacityFactor);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
