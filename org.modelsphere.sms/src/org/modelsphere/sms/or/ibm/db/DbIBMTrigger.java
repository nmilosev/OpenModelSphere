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
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMTable.html">DbIBMTable</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMView.html">DbIBMView</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbIBMTrigger extends DbORTrigger {

    //Meta

    public static final MetaField fTime = new MetaField(LocaleMgr.db.getString("time"));
    public static final MetaField fOldTable = new MetaField(LocaleMgr.db.getString("oldTable"));
    public static final MetaField fNewTable = new MetaField(LocaleMgr.db.getString("newTable"));
    public static final MetaField fForEachStatement = new MetaField(LocaleMgr.db
            .getString("forEachStatement"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbIBMTrigger"),
            DbIBMTrigger.class, new MetaField[] { fTime, fOldTable, fNewTable, fForEachStatement },
            MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORTrigger.metaClass);

            fTime.setJField(DbIBMTrigger.class.getDeclaredField("m_time"));
            fOldTable.setJField(DbIBMTrigger.class.getDeclaredField("m_oldTable"));
            fNewTable.setJField(DbIBMTrigger.class.getDeclaredField("m_newTable"));
            fForEachStatement.setJField(DbIBMTrigger.class.getDeclaredField("m_forEachStatement"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    IBMTriggerTime m_time;
    String m_oldTable;
    String m_newTable;
    boolean m_forEachStatement;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbIBMTrigger() {
    }

    /**
     * Creates an instance of DbIBMTrigger.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbIBMTrigger(DbObject composite) throws DbException {
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
     * Sets the "time" property of a DbIBMTrigger's instance.
     * 
     * @param value
     *            the "time" property
     **/
    public final void setTime(IBMTriggerTime value) throws DbException {
        basicSet(fTime, value);
    }

    /**
     * Sets the "old table" property of a DbIBMTrigger's instance.
     * 
     * @param value
     *            the "old table" property
     **/
    public final void setOldTable(String value) throws DbException {
        basicSet(fOldTable, value);
    }

    /**
     * Sets the "new table" property of a DbIBMTrigger's instance.
     * 
     * @param value
     *            the "new table" property
     **/
    public final void setNewTable(String value) throws DbException {
        basicSet(fNewTable, value);
    }

    /**
     * Sets the "for each statement" property of a DbIBMTrigger's instance.
     * 
     * @param value
     *            the "for each statement" property
     **/
    public final void setForEachStatement(Boolean value) throws DbException {
        basicSet(fForEachStatement, value);
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
     * Gets the "time" of a DbIBMTrigger's instance.
     * 
     * @return the "time"
     **/
    public final IBMTriggerTime getTime() throws DbException {
        return (IBMTriggerTime) get(fTime);
    }

    /**
     * Gets the "old table" property of a DbIBMTrigger's instance.
     * 
     * @return the "old table" property
     **/
    public final String getOldTable() throws DbException {
        return (String) get(fOldTable);
    }

    /**
     * Gets the "new table" property of a DbIBMTrigger's instance.
     * 
     * @return the "new table" property
     **/
    public final String getNewTable() throws DbException {
        return (String) get(fNewTable);
    }

    /**
     * Gets the "for each statement" property's Boolean value of a DbIBMTrigger's instance.
     * 
     * @return the "for each statement" property's Boolean value
     * @deprecated use isForEachStatement() method instead
     **/
    public final Boolean getForEachStatement() throws DbException {
        return (Boolean) get(fForEachStatement);
    }

    /**
     * Tells whether a DbIBMTrigger's instance is forEachStatement or not.
     * 
     * @return boolean
     **/
    public final boolean isForEachStatement() throws DbException {
        return getForEachStatement().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
