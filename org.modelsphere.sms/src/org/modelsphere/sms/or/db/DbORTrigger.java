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
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/oracle/db/DbORATrigger.html">DbORATrigger</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/ibm/db/DbIBMTrigger.html">DbIBMTrigger</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/informix/db/DbINFTrigger.html">DbINFTrigger</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/generic/db/DbGETrigger.html">DbGETrigger</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbORTrigger extends DbORAbstractMethod {

    //Meta

    public static final MetaField fEvent = new MetaField(LocaleMgr.db.getString("event"));
    public static final MetaField fOldAlias = new MetaField(LocaleMgr.db.getString("oldAlias"));
    public static final MetaField fNewAlias = new MetaField(LocaleMgr.db.getString("newAlias"));
    public static final MetaField fWhenCondition = new MetaField(LocaleMgr.db
            .getString("whenCondition"));
    public static final MetaRelationN fColumns = new MetaRelationN(LocaleMgr.db
            .getString("columns"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbORTrigger"),
            DbORTrigger.class, new MetaField[] { fEvent, fOldAlias, fNewAlias, fWhenCondition,
                    fColumns }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORAbstractMethod.metaClass);
            metaClass.setIcon("dbortrigger.gif");

            fEvent.setJField(DbORTrigger.class.getDeclaredField("m_event"));
            fEvent.setScreenOrder("<body");
            fOldAlias.setJField(DbORTrigger.class.getDeclaredField("m_oldAlias"));
            fNewAlias.setJField(DbORTrigger.class.getDeclaredField("m_newAlias"));
            fWhenCondition.setJField(DbORTrigger.class.getDeclaredField("m_whenCondition"));
            fWhenCondition.setRendererPluginName("LookupDescription");
            fColumns.setJField(DbORTrigger.class.getDeclaredField("m_columns"));
            fColumns.setFlags(MetaField.INTEGRABLE | MetaField.WRITE_CHECK);

            fColumns.setOppositeRel(DbORColumn.fTriggers);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    ORTriggerEvent m_event;
    String m_oldAlias;
    String m_newAlias;
    String m_whenCondition;
    DbRelationN m_columns;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORTrigger() {
    }

    /**
     * Creates an instance of DbORTrigger.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORTrigger(DbObject composite) throws DbException {
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
     * Sets the "event" property of a DbORTrigger's instance.
     * 
     * @param value
     *            the "event" property
     **/
    public final void setEvent(ORTriggerEvent value) throws DbException {
        basicSet(fEvent, value);
    }

    /**
     * Sets the "old alias" property of a DbORTrigger's instance.
     * 
     * @param value
     *            the "old alias" property
     **/
    public final void setOldAlias(String value) throws DbException {
        basicSet(fOldAlias, value);
    }

    /**
     * Sets the "new alias" property of a DbORTrigger's instance.
     * 
     * @param value
     *            the "new alias" property
     **/
    public final void setNewAlias(String value) throws DbException {
        basicSet(fNewAlias, value);
    }

    /**
     * Sets the "when condition" property of a DbORTrigger's instance.
     * 
     * @param value
     *            the "when condition" property
     **/
    public final void setWhenCondition(String value) throws DbException {
        basicSet(fWhenCondition, value);
    }

    /**
     * Adds an element to or removes an element from the list of columns associated to a
     * DbORTrigger's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setColumns(DbORColumn value, int op) throws DbException {
        setRelationNN(fColumns, value, op);
    }

    /**
     * Adds an element to the list of columns associated to a DbORTrigger's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToColumns(DbORColumn value) throws DbException {
        setColumns(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of columns associated to a DbORTrigger's instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromColumns(DbORColumn value) throws DbException {
        setColumns(value, Db.REMOVE_FROM_RELN);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fColumns)
                setColumns((DbORColumn) value, Db.ADD_TO_RELN);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fColumns)
            setColumns((DbORColumn) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the "event" of a DbORTrigger's instance.
     * 
     * @return the "event"
     **/
    public final ORTriggerEvent getEvent() throws DbException {
        return (ORTriggerEvent) get(fEvent);
    }

    /**
     * Gets the "old alias" property of a DbORTrigger's instance.
     * 
     * @return the "old alias" property
     **/
    public final String getOldAlias() throws DbException {
        return (String) get(fOldAlias);
    }

    /**
     * Gets the "new alias" property of a DbORTrigger's instance.
     * 
     * @return the "new alias" property
     **/
    public final String getNewAlias() throws DbException {
        return (String) get(fNewAlias);
    }

    /**
     * Gets the "when condition" property of a DbORTrigger's instance.
     * 
     * @return the "when condition" property
     **/
    public final String getWhenCondition() throws DbException {
        return (String) get(fWhenCondition);
    }

    /**
     * Gets the list of columns associated to a DbORTrigger's instance.
     * 
     * @return the list of columns.
     **/
    public final DbRelationN getColumns() throws DbException {
        return (DbRelationN) get(fColumns);
    }

}
