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
public final class DbSMSLinkGo extends DbSMSLineGo {

    //Meta

    public static final MetaField fCenterOffset = new MetaField(LocaleMgr.db
            .getString("centerOffset"));
    public static final MetaRelation1 fLink = new MetaRelation1(LocaleMgr.db.getString("link"), 1);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbSMSLinkGo"),
            DbSMSLinkGo.class, new MetaField[] { fCenterOffset, fLink }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSLineGo.metaClass);

            fCenterOffset.setJField(DbSMSLinkGo.class.getDeclaredField("m_centerOffset"));
            fLink.setJField(DbSMSLinkGo.class.getDeclaredField("m_link"));
            fLink.setFlags(MetaField.COPY_REFS);

            fLink.setOppositeRel(DbSMSLink.fLinkGos);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    private static final long serialVersionUID = 0L;

    //Instance variables
    SrPoint m_centerOffset;
    DbSMSLink m_link;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSLinkGo() {
    }

    /**
     * Creates an instance of DbSMSLinkGo.
     * 
     * @param composite
     *            org.modelsphere.sms.db.DbSMSDiagram
     * @param frontendgo
     *            org.modelsphere.sms.db.DbSMSGraphicalObject
     * @param backendgo
     *            org.modelsphere.sms.db.DbSMSGraphicalObject
     * @param link
     *            org.modelsphere.sms.db.DbSMSLink
     **/
    public DbSMSLinkGo(DbSMSDiagram composite, DbSMSGraphicalObject frontEndGo,
            DbSMSGraphicalObject backEndGo, DbSMSLink link) throws DbException {
        super(composite, frontEndGo, backEndGo);
        basicSet(fLink, link);
        setDefaultInitialValues();
        setRightAngle(Boolean.FALSE);
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @return dbobject
     **/
    public DbObject getSO() throws DbException {
        return this.getLink();
    }

    /**

 **/
    public void resetLabelsPosition() throws DbException {
        setCenterOffset(null);
    }

    //Setters

    /**
     * Sets the "centeroffset" property of a DbSMSLinkGo's instance.
     * 
     * @param value
     *            the "centeroffset" property
     **/
    public final void setCenterOffset(Point value) throws DbException {
        basicSet(fCenterOffset, value);
    }

    /**
     * Sets the link object associated to a DbSMSLinkGo's instance.
     * 
     * @param value
     *            the link object to be associated
     **/
    public final void setLink(DbSMSLink value) throws DbException {
        basicSet(fLink, value);
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
     * Gets the "centeroffset" of a DbSMSLinkGo's instance.
     * 
     * @return the "centeroffset"
     **/
    public final Point getCenterOffset() throws DbException {
        return (Point) get(fCenterOffset);
    }

    /**
     * Gets the link object associated to a DbSMSLinkGo's instance.
     * 
     * @return the link object
     **/
    public final DbSMSLink getLink() throws DbException {
        return (DbSMSLink) get(fLink);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
