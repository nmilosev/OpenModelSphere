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
 * HREF="../../../../org/modelsphere/sms/oo/db/DbOOAssociation.html">DbOOAssociation</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbORAssociation.html">DbORAssociation</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbSMSAssociation extends DbSMSAbstractRelationship {

    //Meta

    public static final MetaRelationN fAssociationGos = new MetaRelationN(LocaleMgr.db
            .getString("associationGos"), 0, MetaRelationN.cardN);
    public static final MetaField fToFrontEnd = new MetaField(LocaleMgr.db.getString("toFrontEnd"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSAssociation"), DbSMSAssociation.class, new MetaField[] {
            fAssociationGos, fToFrontEnd }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSAbstractRelationship.metaClass);

            fAssociationGos.setJField(DbSMSAssociation.class.getDeclaredField("m_associationGos"));
            fToFrontEnd.setJField(DbSMSAssociation.class.getDeclaredField("m_toFrontEnd"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbRelationN m_associationGos;
    boolean m_toFrontEnd;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSAssociation() {
    }

    /**
     * Creates an instance of DbSMSAssociation.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSAssociation(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        org.modelsphere.sms.db.util.DbInitialization.initAssociation(this);
    }

    //Setters

    /**
     * Sets the "to front end" property of a DbSMSAssociation's instance.
     * 
     * @param value
     *            the "to front end" property
     **/
    public final void setToFrontEnd(Boolean value) throws DbException {
        basicSet(fToFrontEnd, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fAssociationGos)
                ((DbSMSAssociationGo) value).setAssociation(this);
            else
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
     * Gets the list of graphical objects associated to a DbSMSAssociation's instance.
     * 
     * @return the list of graphical objects.
     **/
    public final DbRelationN getAssociationGos() throws DbException {
        return (DbRelationN) get(fAssociationGos);
    }

    /**
     * Gets the "to front end" property's Boolean value of a DbSMSAssociation's instance.
     * 
     * @return the "to front end" property's Boolean value
     * @deprecated use isToFrontEnd() method instead
     **/
    public final Boolean getToFrontEnd() throws DbException {
        return (Boolean) get(fToFrontEnd);
    }

    /**
     * Tells whether a DbSMSAssociation's instance is toFrontEnd or not.
     * 
     * @return boolean
     **/
    public final boolean isToFrontEnd() throws DbException {
        return getToFrontEnd().booleanValue();
    }

}
