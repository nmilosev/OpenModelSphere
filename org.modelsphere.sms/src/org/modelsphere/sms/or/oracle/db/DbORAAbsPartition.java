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
package org.modelsphere.sms.or.oracle.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.oracle.db.srtypes.*;
import org.modelsphere.sms.or.oracle.international.LocaleMgr;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAPartition.html">DbORAPartition</A>,
 * <A HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORASubPartition.html">
 * DbORASubPartition</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbORAAbsPartition extends DbSMSSemanticalObject {

    //Meta

    public static final MetaRelation1 fTablespace = new MetaRelation1(LocaleMgr.db
            .getString("tablespace"), 0);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORAAbsPartition"), DbORAAbsPartition.class,
            new MetaField[] { fTablespace }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);
            metaClass.setIcon("dborapartition.gif");

            fTablespace.setJField(DbORAAbsPartition.class.getDeclaredField("m_tablespace"));
            fTablespace.setFlags(MetaField.INTEGRABLE_BY_NAME);
            fTablespace.setRendererPluginName("DbORATablespace");

            fTablespace.setOppositeRel(DbORATablespace.fAbsPartitions);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbORATablespace m_tablespace;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORAAbsPartition() {
    }

    /**
     * Creates an instance of DbORAAbsPartition.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORAAbsPartition(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    //Setters

    /**
     * Sets the tablespace object associated to a DbORAAbsPartition's instance.
     * 
     * @param value
     *            the tablespace object to be associated
     **/
    public final void setTablespace(DbORATablespace value) throws DbException {
        basicSet(fTablespace, value);
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
     * Gets the tablespace object associated to a DbORAAbsPartition's instance.
     * 
     * @return the tablespace object
     **/
    public final DbORATablespace getTablespace() throws DbException {
        return (DbORATablespace) get(fTablespace);
    }

}
