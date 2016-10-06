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
package org.modelsphere.sms.or.ibm.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.ibm.db.srtypes.*;
import org.modelsphere.sms.or.ibm.international.LocaleMgr;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMTablespace.html">DbIBMTablespace</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMContainerItem.html"
 * >DbIBMContainerItem</A>.<br>
 **/
public final class DbIBMContainerClause extends DbObject {

    //Meta

    public static final MetaField fOnDBPartitionGroup = new MetaField(LocaleMgr.db
            .getString("onDBPartitionGroup"));
    public static final MetaField fContainerString = new MetaField(LocaleMgr.db
            .getString("containerString"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbIBMContainerClause"), DbIBMContainerClause.class, new MetaField[] {
            fOnDBPartitionGroup, fContainerString }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbObject.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbIBMContainerItem.metaClass });
            metaClass.setIcon("dbibmcontainer.gif");

            fOnDBPartitionGroup.setJField(DbIBMContainerClause.class
                    .getDeclaredField("m_onDBPartitionGroup"));
            fContainerString.setJField(DbIBMContainerClause.class
                    .getDeclaredField("m_containerString"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    String m_onDBPartitionGroup;
    String m_containerString;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbIBMContainerClause() {
    }

    /**
     * Creates an instance of DbIBMContainerClause.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbIBMContainerClause(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        org.modelsphere.sms.or.ibm.db.util.IBMUtility.getInstance().setInitialValues(this);
    }

    //Setters

    /**
     * Sets the "on database partition group" property of a DbIBMContainerClause's instance.
     * 
     * @param value
     *            the "on database partition group" property
     **/
    public final void setOnDBPartitionGroup(String value) throws DbException {
        basicSet(fOnDBPartitionGroup, value);
    }

    /**
     * Sets the "system container text" property of a DbIBMContainerClause's instance.
     * 
     * @param value
     *            the "system container text" property
     **/
    public final void setContainerString(String value) throws DbException {
        basicSet(fContainerString, value);
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
     * Gets the "on database partition group" property of a DbIBMContainerClause's instance.
     * 
     * @return the "on database partition group" property
     **/
    public final String getOnDBPartitionGroup() throws DbException {
        return (String) get(fOnDBPartitionGroup);
    }

    /**
     * Gets the "system container text" property of a DbIBMContainerClause's instance.
     * 
     * @return the "system container text" property
     **/
    public final String getContainerString() throws DbException {
        return (String) get(fContainerString);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
