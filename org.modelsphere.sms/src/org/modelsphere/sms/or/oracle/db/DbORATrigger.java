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
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAAbsTable.html">DbORAAbsTable</A>,
 * <A HREF="../../../../../../org/modelsphere/sms/or/oracle/db/DbORAView.html">DbORAView</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbORATrigger extends DbORTrigger {

    //Meta

    public static final MetaField fTime = new MetaField(LocaleMgr.db.getString("time"));
    public static final MetaField fParentAlias = new MetaField(LocaleMgr.db
            .getString("parentAlias"));
    public static final MetaField fRowTrigger = new MetaField(LocaleMgr.db.getString("rowTrigger"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbORATrigger"),
            DbORATrigger.class, new MetaField[] { fTime, fParentAlias, fRowTrigger },
            MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORTrigger.metaClass);

            fTime.setJField(DbORATrigger.class.getDeclaredField("m_time"));
            fTime.setScreenOrder("<event");
            fParentAlias.setJField(DbORATrigger.class.getDeclaredField("m_parentAlias"));
            fParentAlias.setScreenOrder(">newAlias");
            fRowTrigger.setJField(DbORATrigger.class.getDeclaredField("m_rowTrigger"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    ORATriggerTime m_time;
    String m_parentAlias;
    boolean m_rowTrigger;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORATrigger() {
    }

    /**
     * Creates an instance of DbORATrigger.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORATrigger(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));
    }

    //Setters

    /**
     * Sets the "time" property of a DbORATrigger's instance.
     * 
     * @param value
     *            the "time" property
     **/
    public final void setTime(ORATriggerTime value) throws DbException {
        basicSet(fTime, value);
    }

    /**
     * Sets the "parent alias" property of a DbORATrigger's instance.
     * 
     * @param value
     *            the "parent alias" property
     **/
    public final void setParentAlias(String value) throws DbException {
        basicSet(fParentAlias, value);
    }

    /**
     * Sets the "row trigger" property of a DbORATrigger's instance.
     * 
     * @param value
     *            the "row trigger" property
     **/
    public final void setRowTrigger(Boolean value) throws DbException {
        basicSet(fRowTrigger, value);
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
     * Gets the "time" of a DbORATrigger's instance.
     * 
     * @return the "time"
     **/
    public final ORATriggerTime getTime() throws DbException {
        return (ORATriggerTime) get(fTime);
    }

    /**
     * Gets the "parent alias" property of a DbORATrigger's instance.
     * 
     * @return the "parent alias" property
     **/
    public final String getParentAlias() throws DbException {
        return (String) get(fParentAlias);
    }

    /**
     * Gets the "row trigger" property's Boolean value of a DbORATrigger's instance.
     * 
     * @return the "row trigger" property's Boolean value
     * @deprecated use isRowTrigger() method instead
     **/
    public final Boolean getRowTrigger() throws DbException {
        return (Boolean) get(fRowTrigger);
    }

    /**
     * Tells whether a DbORATrigger's instance is rowTrigger or not.
     * 
     * @return boolean
     **/
    public final boolean isRowTrigger() throws DbException {
        return getRowTrigger().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
