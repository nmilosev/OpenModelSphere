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
package org.modelsphere.sms.or.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.or.oracle.db.DbORASequence;
import org.modelsphere.sms.or.informix.db.DbINFCheck;
import org.modelsphere.sms.or.informix.db.DbINFForeign;
import org.modelsphere.sms.or.informix.db.DbINFPrimaryUnique;
import org.modelsphere.sms.or.ibm.db.DbIBMSequence;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORUserNode.html">DbORUserNode</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORUser extends DbSMSUser {

    //Meta

    public static final MetaRelationN fTables = new MetaRelationN(LocaleMgr.db.getString("tables"),
            0, MetaRelationN.cardN);
    public static final MetaRelationN fIndexes = new MetaRelationN(LocaleMgr.db
            .getString("indexes"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fOperations = new MetaRelationN(LocaleMgr.db
            .getString("operations"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fDomains = new MetaRelationN(LocaleMgr.db
            .getString("domains"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fSequences = new MetaRelationN(LocaleMgr.db
            .getString("sequences"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fForeignKeys = new MetaRelationN(LocaleMgr.db
            .getString("foreignKeys"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fPrimaryUniqueKeys = new MetaRelationN(LocaleMgr.db
            .getString("primaryUniqueKeys"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fCheckConstraints = new MetaRelationN(LocaleMgr.db
            .getString("checkConstraints"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fIbmSequences = new MetaRelationN(LocaleMgr.db
            .getString("ibmSequences"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbORUser"),
            DbORUser.class, new MetaField[] { fTables, fIndexes, fOperations, fDomains, fSequences,
                    fForeignKeys, fPrimaryUniqueKeys, fCheckConstraints, fIbmSequences },
            MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSUser.metaClass);
            metaClass.setIcon("dboruser.gif");

            fTables.setJField(DbORUser.class.getDeclaredField("m_tables"));
            fTables.setFlags(MetaField.HUGE_RELN);
            fIndexes.setJField(DbORUser.class.getDeclaredField("m_indexes"));
            fIndexes.setFlags(MetaField.HUGE_RELN);
            fOperations.setJField(DbORUser.class.getDeclaredField("m_operations"));
            fOperations.setFlags(MetaField.HUGE_RELN);
            fDomains.setJField(DbORUser.class.getDeclaredField("m_domains"));
            fDomains.setFlags(MetaField.HUGE_RELN);
            fSequences.setJField(DbORUser.class.getDeclaredField("m_sequences"));
            fSequences.setFlags(MetaField.HUGE_RELN);
            fForeignKeys.setJField(DbORUser.class.getDeclaredField("m_foreignKeys"));
            fForeignKeys.setFlags(MetaField.HUGE_RELN);
            fPrimaryUniqueKeys.setJField(DbORUser.class.getDeclaredField("m_primaryUniqueKeys"));
            fPrimaryUniqueKeys.setFlags(MetaField.HUGE_RELN);
            fCheckConstraints.setJField(DbORUser.class.getDeclaredField("m_checkConstraints"));
            fCheckConstraints.setFlags(MetaField.HUGE_RELN);
            fIbmSequences.setJField(DbORUser.class.getDeclaredField("m_ibmSequences"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbRelationN m_tables;
    DbRelationN m_indexes;
    DbRelationN m_operations;
    DbRelationN m_domains;
    DbRelationN m_sequences;
    DbRelationN m_foreignKeys;
    DbRelationN m_primaryUniqueKeys;
    DbRelationN m_checkConstraints;
    DbRelationN m_ibmSequences;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORUser() {
    }

    /**
     * Creates an instance of DbORUser.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORUser(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setName(LocaleMgr.misc.getString("user"));
    }

    //Setters

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fTables)
                ((DbORAbsTable) value).setUser(this);
            else if (metaField == fIndexes)
                ((DbORIndex) value).setUser(this);
            else if (metaField == fOperations)
                ((DbORAbstractMethod) value).setUser(this);
            else if (metaField == fDomains)
                ((DbORDomain) value).setUser(this);
            else if (metaField == fSequences)
                ((DbORASequence) value).setUser(this);
            else if (metaField == fForeignKeys)
                ((DbINFForeign) value).setUser(this);
            else if (metaField == fPrimaryUniqueKeys)
                ((DbINFPrimaryUnique) value).setUser(this);
            else if (metaField == fCheckConstraints)
                ((DbINFCheck) value).setUser(this);
            else if (metaField == fIbmSequences)
                ((DbIBMSequence) value).setUser(this);
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
     * Gets the list of tables/views associated to a DbORUser's instance.
     * 
     * @return the list of tables/views.
     **/
    public final DbRelationN getTables() throws DbException {
        return (DbRelationN) get(fTables);
    }

    /**
     * Gets the list of indexes associated to a DbORUser's instance.
     * 
     * @return the list of indexes.
     **/
    public final DbRelationN getIndexes() throws DbException {
        return (DbRelationN) get(fIndexes);
    }

    /**
     * Gets the list of operations associated to a DbORUser's instance.
     * 
     * @return the list of operations.
     **/
    public final DbRelationN getOperations() throws DbException {
        return (DbRelationN) get(fOperations);
    }

    /**
     * Gets the list of domains associated to a DbORUser's instance.
     * 
     * @return the list of domains.
     **/
    public final DbRelationN getDomains() throws DbException {
        return (DbRelationN) get(fDomains);
    }

    /**
     * Gets the list of oracle sequences associated to a DbORUser's instance.
     * 
     * @return the list of oracle sequences.
     **/
    public final DbRelationN getSequences() throws DbException {
        return (DbRelationN) get(fSequences);
    }

    /**
     * Gets the list of foreign keys associated to a DbORUser's instance.
     * 
     * @return the list of foreign keys.
     **/
    public final DbRelationN getForeignKeys() throws DbException {
        return (DbRelationN) get(fForeignKeys);
    }

    /**
     * Gets the list of primary / unique keys associated to a DbORUser's instance.
     * 
     * @return the list of primary / unique keys.
     **/
    public final DbRelationN getPrimaryUniqueKeys() throws DbException {
        return (DbRelationN) get(fPrimaryUniqueKeys);
    }

    /**
     * Gets the list of check constraints associated to a DbORUser's instance.
     * 
     * @return the list of check constraints.
     **/
    public final DbRelationN getCheckConstraints() throws DbException {
        return (DbRelationN) get(fCheckConstraints);
    }

    /**
     * Gets the list of db2-udb sequences associated to a DbORUser's instance.
     * 
     * @return the list of db2-udb sequences.
     **/
    public final DbRelationN getIbmSequences() throws DbException {
        return (DbRelationN) get(fIbmSequences);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
