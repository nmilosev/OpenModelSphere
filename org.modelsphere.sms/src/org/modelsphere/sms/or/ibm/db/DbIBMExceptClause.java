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
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMBufferPool.html">DbIBMBufferPool</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbIBMExceptClause extends DbObject {

    //Meta

    public static final MetaField fFromPartition = new MetaField(LocaleMgr.db
            .getString("fromPartition"));
    public static final MetaField fToPartition = new MetaField(LocaleMgr.db
            .getString("toPartition"));
    public static final MetaField fNumberOfPages = new MetaField(LocaleMgr.db
            .getString("numberOfPages"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbIBMExceptClause"), DbIBMExceptClause.class, new MetaField[] {
            fFromPartition, fToPartition, fNumberOfPages }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbObject.metaClass);
            metaClass.setIcon("dbibmexceptclause.gif");

            fFromPartition.setJField(DbIBMExceptClause.class.getDeclaredField("m_fromPartition"));
            fToPartition.setJField(DbIBMExceptClause.class.getDeclaredField("m_toPartition"));
            fNumberOfPages.setJField(DbIBMExceptClause.class.getDeclaredField("m_numberOfPages"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    int m_fromPartition;
    int m_toPartition;
    int m_numberOfPages;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbIBMExceptClause() {
    }

    /**
     * Creates an instance of DbIBMExceptClause.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbIBMExceptClause(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        org.modelsphere.sms.or.ibm.db.util.IBMUtility.getInstance().setInitialValues(this);
    }

    //Setters

    /**
     * Sets the "start partition" property of a DbIBMExceptClause's instance.
     * 
     * @param value
     *            the "start partition" property
     **/
    public final void setFromPartition(Integer value) throws DbException {
        basicSet(fFromPartition, value);
    }

    /**
     * Sets the "end partition" property of a DbIBMExceptClause's instance.
     * 
     * @param value
     *            the "end partition" property
     **/
    public final void setToPartition(Integer value) throws DbException {
        basicSet(fToPartition, value);
    }

    /**
     * Sets the "number of pages" property of a DbIBMExceptClause's instance.
     * 
     * @param value
     *            the "number of pages" property
     **/
    public final void setNumberOfPages(Integer value) throws DbException {
        basicSet(fNumberOfPages, value);
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
     * Gets the "start partition" property of a DbIBMExceptClause's instance.
     * 
     * @return the "start partition" property
     **/
    public final Integer getFromPartition() throws DbException {
        return (Integer) get(fFromPartition);
    }

    /**
     * Gets the "end partition" property of a DbIBMExceptClause's instance.
     * 
     * @return the "end partition" property
     **/
    public final Integer getToPartition() throws DbException {
        return (Integer) get(fToPartition);
    }

    /**
     * Gets the "number of pages" property of a DbIBMExceptClause's instance.
     * 
     * @return the "number of pages" property
     **/
    public final Integer getNumberOfPages() throws DbException {
        return (Integer) get(fNumberOfPages);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
