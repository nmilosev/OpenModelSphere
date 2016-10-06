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
package org.modelsphere.sms.or.informix.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.informix.db.srtypes.*;
import org.modelsphere.sms.or.informix.international.LocaleMgr;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFDatabase.html">DbINFDatabase</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbINFDbspace extends DbSMSSemanticalObject {

    //Meta

    public static final MetaRelationN fFragments = new MetaRelationN(LocaleMgr.db
            .getString("fragments"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fTables = new MetaRelationN(LocaleMgr.db.getString("tables"),
            0, MetaRelationN.cardN);
    public static final MetaRelation1 fDatabase = new MetaRelation1(LocaleMgr.db
            .getString("database"), 0);
    public static final MetaRelationN fIndexes = new MetaRelationN(LocaleMgr.db
            .getString("indexes"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbINFDbspace"),
            DbINFDbspace.class, new MetaField[] { fFragments, fTables, fDatabase, fIndexes },
            MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);
            metaClass.setIcon("dbinfdbspace.gif");

            fFragments.setJField(DbINFDbspace.class.getDeclaredField("m_fragments"));
            fTables.setJField(DbINFDbspace.class.getDeclaredField("m_tables"));
            fDatabase.setJField(DbINFDbspace.class.getDeclaredField("m_database"));
            fDatabase.setRendererPluginName("DbORDatabase");
            fDatabase.setVisibleInScreen(false);
            fIndexes.setJField(DbINFDbspace.class.getDeclaredField("m_indexes"));

            fDatabase.setOppositeRel(DbINFDatabase.fDbspace);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbRelationN m_fragments;
    DbRelationN m_tables;
    DbINFDatabase m_database;
    DbRelationN m_indexes;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbINFDbspace() {
    }

    /**
     * Creates an instance of DbINFDbspace.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbINFDbspace(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setName(LocaleMgr.misc.getString("dbspace"));
    }

    //Setters

    /**
     * Sets the database object associated to a DbINFDbspace's instance.
     * 
     * @param value
     *            the database object to be associated
     **/
    public final void setDatabase(DbINFDatabase value) throws DbException {
        basicSet(fDatabase, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fFragments)
                ((DbINFFragment) value).setDbspace(this);
            else if (metaField == fTables)
                ((DbINFTable) value).setDbspace(this);
            else if (metaField == fIndexes)
                ((DbINFIndex) value).setDbspace(this);
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
     * Gets the list of fragments associated to a DbINFDbspace's instance.
     * 
     * @return the list of fragments.
     **/
    public final DbRelationN getFragments() throws DbException {
        return (DbRelationN) get(fFragments);
    }

    /**
     * Gets the list of tables associated to a DbINFDbspace's instance.
     * 
     * @return the list of tables.
     **/
    public final DbRelationN getTables() throws DbException {
        return (DbRelationN) get(fTables);
    }

    /**
     * Gets the database object associated to a DbINFDbspace's instance.
     * 
     * @return the database object
     **/
    public final DbINFDatabase getDatabase() throws DbException {
        return (DbINFDatabase) get(fDatabase);
    }

    /**
     * Gets the list of indexes associated to a DbINFDbspace's instance.
     * 
     * @return the list of indexes.
     **/
    public final DbRelationN getIndexes() throws DbException {
        return (DbRelationN) get(fIndexes);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
