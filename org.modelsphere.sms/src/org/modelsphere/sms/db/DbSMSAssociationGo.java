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
 * HREF="../../../../org/modelsphere/sms/oo/db/DbOOAssociationGo.html">DbOOAssociationGo</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbORAssociationGo.html">DbORAssociationGo</A>.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSDiagram.html">DbSMSDiagram</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public abstract class DbSMSAssociationGo extends DbSMSLineGo {

    //Meta

    public static final MetaRelation1 fAssociation = new MetaRelation1(LocaleMgr.db
            .getString("association"), 1);
    public static final MetaField fMulti1Offset = new MetaField(LocaleMgr.db
            .getString("multi1Offset"));
    public static final MetaField fMulti2Offset = new MetaField(LocaleMgr.db
            .getString("multi2Offset"));
    public static final MetaField fRole1Offset = new MetaField(LocaleMgr.db
            .getString("role1Offset"));
    public static final MetaField fRole2Offset = new MetaField(LocaleMgr.db
            .getString("role2Offset"));
    public static final MetaField fCenterOffset = new MetaField(LocaleMgr.db
            .getString("centerOffset"));
    public static final MetaField fUml1Offset = new MetaField(LocaleMgr.db.getString("uml1Offset"));
    public static final MetaField fUml2Offset = new MetaField(LocaleMgr.db.getString("uml2Offset"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSAssociationGo"), DbSMSAssociationGo.class, new MetaField[] {
            fAssociation, fMulti1Offset, fMulti2Offset, fRole1Offset, fRole2Offset, fCenterOffset,
            fUml1Offset, fUml2Offset }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSLineGo.metaClass);

            fAssociation.setJField(DbSMSAssociationGo.class.getDeclaredField("m_association"));
            fAssociation.setFlags(MetaField.COPY_REFS);
            fMulti1Offset.setJField(DbSMSAssociationGo.class.getDeclaredField("m_multi1Offset"));
            fMulti2Offset.setJField(DbSMSAssociationGo.class.getDeclaredField("m_multi2Offset"));
            fRole1Offset.setJField(DbSMSAssociationGo.class.getDeclaredField("m_role1Offset"));
            fRole2Offset.setJField(DbSMSAssociationGo.class.getDeclaredField("m_role2Offset"));
            fCenterOffset.setJField(DbSMSAssociationGo.class.getDeclaredField("m_centerOffset"));
            fUml1Offset.setJField(DbSMSAssociationGo.class.getDeclaredField("m_uml1Offset"));
            fUml2Offset.setJField(DbSMSAssociationGo.class.getDeclaredField("m_uml2Offset"));

            fAssociation.setOppositeRel(DbSMSAssociation.fAssociationGos);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 2013048218646722088L;

    //Instance variables
    DbSMSAssociation m_association;
    SrPoint m_multi1Offset;
    SrPoint m_multi2Offset;
    SrPoint m_role1Offset;
    SrPoint m_role2Offset;
    SrPoint m_centerOffset;
    SrPoint m_uml1Offset;
    SrPoint m_uml2Offset;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSAssociationGo() {
    }

    /**
     * Creates an instance of DbSMSAssociationGo.
     * 
     * @param composite
     *            org.modelsphere.sms.db.DbSMSDiagram
     * @param frontendgo
     *            org.modelsphere.sms.db.DbSMSGraphicalObject
     * @param backendgo
     *            org.modelsphere.sms.db.DbSMSGraphicalObject
     * @param association
     *            org.modelsphere.sms.db.DbSMSAssociation
     **/
    public DbSMSAssociationGo(DbSMSDiagram composite, DbSMSGraphicalObject frontEndGo,
            DbSMSGraphicalObject backEndGo, DbSMSAssociation association) throws DbException {
        super(composite, frontEndGo, backEndGo);
        basicSet(fAssociation, association);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @return dbobject
     **/
    public DbObject getSO() throws DbException {
        return this.getAssociation();
    }

    /**

 **/
    public void resetLabelsPosition() throws DbException {
        setMulti1Offset(null);
        setMulti2Offset(null);
        setRole1Offset(null);
        setRole2Offset(null);
        setCenterOffset(null);
    }

    //Setters

    /**
     * Sets the association object associated to a DbSMSAssociationGo's instance.
     * 
     * @param value
     *            the association object to be associated
     **/
    public final void setAssociation(DbSMSAssociation value) throws DbException {
        basicSet(fAssociation, value);
    }

    /**
     * Sets the "multi1 offset" property of a DbSMSAssociationGo's instance.
     * 
     * @param value
     *            the "multi1 offset" property
     **/
    public final void setMulti1Offset(Point value) throws DbException {
        basicSet(fMulti1Offset, value);
    }

    /**
     * Sets the "multi2 offset" property of a DbSMSAssociationGo's instance.
     * 
     * @param value
     *            the "multi2 offset" property
     **/
    public final void setMulti2Offset(Point value) throws DbException {
        basicSet(fMulti2Offset, value);
    }

    /**
     * Sets the "role1 offset" property of a DbSMSAssociationGo's instance.
     * 
     * @param value
     *            the "role1 offset" property
     **/
    public final void setRole1Offset(Point value) throws DbException {
        basicSet(fRole1Offset, value);
    }

    /**
     * Sets the "role2 offset" property of a DbSMSAssociationGo's instance.
     * 
     * @param value
     *            the "role2 offset" property
     **/
    public final void setRole2Offset(Point value) throws DbException {
        basicSet(fRole2Offset, value);
    }

    /**
     * Sets the "center offset" property of a DbSMSAssociationGo's instance.
     * 
     * @param value
     *            the "center offset" property
     **/
    public final void setCenterOffset(Point value) throws DbException {
        basicSet(fCenterOffset, value);
    }

    /**
     * Sets the "uml1 offset" property of a DbSMSAssociationGo's instance.
     * 
     * @param value
     *            the "uml1 offset" property
     **/
    public final void setUml1Offset(Point value) throws DbException {
        basicSet(fUml1Offset, value);
    }

    /**
     * Sets the "uml2 offset" property of a DbSMSAssociationGo's instance.
     * 
     * @param value
     *            the "uml2 offset" property
     **/
    public final void setUml2Offset(Point value) throws DbException {
        basicSet(fUml2Offset, value);
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
     * Gets the association object associated to a DbSMSAssociationGo's instance.
     * 
     * @return the association object
     **/
    public final DbSMSAssociation getAssociation() throws DbException {
        return (DbSMSAssociation) get(fAssociation);
    }

    /**
     * Gets the "multi1 offset" of a DbSMSAssociationGo's instance.
     * 
     * @return the "multi1 offset"
     **/
    public final Point getMulti1Offset() throws DbException {
        return (Point) get(fMulti1Offset);
    }

    /**
     * Gets the "multi2 offset" of a DbSMSAssociationGo's instance.
     * 
     * @return the "multi2 offset"
     **/
    public final Point getMulti2Offset() throws DbException {
        return (Point) get(fMulti2Offset);
    }

    /**
     * Gets the "role1 offset" of a DbSMSAssociationGo's instance.
     * 
     * @return the "role1 offset"
     **/
    public final Point getRole1Offset() throws DbException {
        return (Point) get(fRole1Offset);
    }

    /**
     * Gets the "role2 offset" of a DbSMSAssociationGo's instance.
     * 
     * @return the "role2 offset"
     **/
    public final Point getRole2Offset() throws DbException {
        return (Point) get(fRole2Offset);
    }

    /**
     * Gets the "center offset" of a DbSMSAssociationGo's instance.
     * 
     * @return the "center offset"
     **/
    public final Point getCenterOffset() throws DbException {
        return (Point) get(fCenterOffset);
    }

    /**
     * Gets the "uml1 offset" of a DbSMSAssociationGo's instance.
     * 
     * @return the "uml1 offset"
     **/
    public final Point getUml1Offset() throws DbException {
        return (Point) get(fUml1Offset);
    }

    /**
     * Gets the "uml2 offset" of a DbSMSAssociationGo's instance.
     * 
     * @return the "uml2 offset"
     **/
    public final Point getUml2Offset() throws DbException {
        return (Point) get(fUml2Offset);
    }

}
