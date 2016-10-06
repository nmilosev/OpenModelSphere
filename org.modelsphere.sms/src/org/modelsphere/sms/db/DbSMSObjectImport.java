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
 * HREF="../../../../org/modelsphere/sms/db/DbSMSSemanticalObject.html">DbSMSSemanticalObject</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbSMSObjectImport extends DbObject {

    //Meta

    public static final MetaField fAlias = new MetaField(LocaleMgr.db.getString("alias"));
    public static final MetaRelation1 fSourceObject = new MetaRelation1(LocaleMgr.db
            .getString("sourceObject"), 1);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSObjectImport"), DbSMSObjectImport.class, new MetaField[] { fAlias,
            fSourceObject }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbObject.metaClass);

            fAlias.setJField(DbSMSObjectImport.class.getDeclaredField("m_alias"));
            fSourceObject.setJField(DbSMSObjectImport.class.getDeclaredField("m_sourceObject"));

            fSourceObject.setOppositeRel(DbSMSSemanticalObject.fObjectImports);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    String m_alias;
    DbSMSSemanticalObject m_sourceObject;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSObjectImport() {
    }

    /**
     * Creates an instance of DbSMSObjectImport.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSObjectImport(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    //Setters

    /**
     * Sets the "alias" property of a DbSMSObjectImport's instance.
     * 
     * @param value
     *            the "alias" property
     **/
    public final void setAlias(String value) throws DbException {
        basicSet(fAlias, value);
    }

    /**
     * Sets the source object object associated to a DbSMSObjectImport's instance.
     * 
     * @param value
     *            the source object object to be associated
     **/
    public final void setSourceObject(DbSMSSemanticalObject value) throws DbException {
        basicSet(fSourceObject, value);
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
     * Gets the "alias" property of a DbSMSObjectImport's instance.
     * 
     * @return the "alias" property
     **/
    public final String getAlias() throws DbException {
        return (String) get(fAlias);
    }

    /**
     * Gets the source object object associated to a DbSMSObjectImport's instance.
     * 
     * @return the source object object
     **/
    public final DbSMSSemanticalObject getSourceObject() throws DbException {
        return (DbSMSSemanticalObject) get(fSourceObject);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
