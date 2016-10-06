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
public final class DbSMSInheritanceGo extends DbSMSLineGo {

    //Meta

    public static final MetaRelation1 fInheritance = new MetaRelation1(LocaleMgr.db
            .getString("inheritance"), 1);
    public static final MetaField fCenterOffset = new MetaField(LocaleMgr.db
            .getString("centerOffset"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSInheritanceGo"), DbSMSInheritanceGo.class, new MetaField[] {
            fInheritance, fCenterOffset }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSLineGo.metaClass);

            fInheritance.setJField(DbSMSInheritanceGo.class.getDeclaredField("m_inheritance"));
            fInheritance.setFlags(MetaField.COPY_REFS);
            fCenterOffset.setJField(DbSMSInheritanceGo.class.getDeclaredField("m_centerOffset"));

            fInheritance.setOppositeRel(DbSMSInheritance.fInheritanceGos);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 4251506749300249476L;

    //Instance variables
    DbSMSInheritance m_inheritance;
    SrPoint m_centerOffset;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSInheritanceGo() {
    }

    /**
     * Creates an instance of DbSMSInheritanceGo.
     * 
     * @param composite
     *            org.modelsphere.sms.db.DbSMSDiagram
     * @param frontendgo
     *            org.modelsphere.sms.db.DbSMSGraphicalObject
     * @param backendgo
     *            org.modelsphere.sms.db.DbSMSGraphicalObject
     * @param inheritance
     *            org.modelsphere.sms.db.DbSMSInheritance
     **/
    public DbSMSInheritanceGo(DbSMSDiagram composite, DbSMSGraphicalObject frontEndGo,
            DbSMSGraphicalObject backEndGo, DbSMSInheritance inheritance) throws DbException {
        super(composite, frontEndGo, backEndGo);
        basicSet(fInheritance, inheritance);
        setDefaultInitialValues();
        setRightAngle(Boolean.FALSE);
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @return dbobject
     **/
    public DbObject getSO() throws DbException {
        return this.getInheritance();
    }

    /**

 **/
    public void resetLabelsPosition() throws DbException {
        setCenterOffset(null);
    }

    //Setters

    /**
     * Sets the inheritance object associated to a DbSMSInheritanceGo's instance.
     * 
     * @param value
     *            the inheritance object to be associated
     **/
    public final void setInheritance(DbSMSInheritance value) throws DbException {
        basicSet(fInheritance, value);
    }

    /**
     * Sets the "center offset" property of a DbSMSInheritanceGo's instance.
     * 
     * @param value
     *            the "center offset" property
     **/
    public final void setCenterOffset(Point value) throws DbException {
        basicSet(fCenterOffset, value);
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
     * Gets the inheritance object associated to a DbSMSInheritanceGo's instance.
     * 
     * @return the inheritance object
     **/
    public final DbSMSInheritance getInheritance() throws DbException {
        return (DbSMSInheritance) get(fInheritance);
    }

    /**
     * Gets the "center offset" of a DbSMSInheritanceGo's instance.
     * 
     * @return the "center offset"
     **/
    public final Point getCenterOffset() throws DbException {
        return (Point) get(fCenterOffset);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
