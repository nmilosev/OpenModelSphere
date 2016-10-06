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
 * HREF="../../../../../../org/modelsphere/sms/or/db/DbORFKeyColumn.html">DbORFKeyColumn</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbIBMForeign extends DbORForeign {

    //Meta

    public static final MetaField fOnDeleteAction = new MetaField(LocaleMgr.db
            .getString("onDeleteAction"));
    public static final MetaField fOnUpdateRestrict = new MetaField(LocaleMgr.db
            .getString("OnUpdateRestrict"));
    public static final MetaField fEnforced = new MetaField(LocaleMgr.db.getString("enforced"));
    public static final MetaField fEnableQueryOptimization = new MetaField(LocaleMgr.db
            .getString("enableQueryOptimization"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbIBMForeign"),
            DbIBMForeign.class, new MetaField[] { fOnDeleteAction, fOnUpdateRestrict, fEnforced,
                    fEnableQueryOptimization }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORForeign.metaClass);

            fOnDeleteAction.setJField(DbIBMForeign.class.getDeclaredField("m_onDeleteAction"));
            fOnUpdateRestrict.setJField(DbIBMForeign.class.getDeclaredField("m_OnUpdateRestrict"));
            fEnforced.setJField(DbIBMForeign.class.getDeclaredField("m_enforced"));
            fEnableQueryOptimization.setJField(DbIBMForeign.class
                    .getDeclaredField("m_enableQueryOptimization"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    IBMFKOnDeleteActionType m_onDeleteAction;
    boolean m_OnUpdateRestrict;
    IBMCheckEnforceType m_enforced;
    IBMCheckEnableQueryOptType m_enableQueryOptimization;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbIBMForeign() {
    }

    /**
     * Creates an instance of DbIBMForeign.
     * 
     * @param assocend
     *            org.modelsphere.sms.or.db.DbORAssociationEnd
     **/
    public DbIBMForeign(DbORAssociationEnd assocEnd) throws DbException {
        super(assocEnd);
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
     * Sets the "on delete action" property of a DbIBMForeign's instance.
     * 
     * @param value
     *            the "on delete action" property
     **/
    public final void setOnDeleteAction(IBMFKOnDeleteActionType value) throws DbException {
        basicSet(fOnDeleteAction, value);
    }

    /**
     * Sets the "on update restrict" property of a DbIBMForeign's instance.
     * 
     * @param value
     *            the "on update restrict" property
     **/
    public final void setOnUpdateRestrict(Boolean value) throws DbException {
        basicSet(fOnUpdateRestrict, value);
    }

    /**
     * Sets the "enforced" property of a DbIBMForeign's instance.
     * 
     * @param value
     *            the "enforced" property
     **/
    public final void setEnforced(IBMCheckEnforceType value) throws DbException {
        basicSet(fEnforced, value);
    }

    /**
     * Sets the "enable query optimization" property of a DbIBMForeign's instance.
     * 
     * @param value
     *            the "enable query optimization" property
     **/
    public final void setEnableQueryOptimization(IBMCheckEnableQueryOptType value)
            throws DbException {
        basicSet(fEnableQueryOptimization, value);
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
     * Gets the "on delete action" of a DbIBMForeign's instance.
     * 
     * @return the "on delete action"
     **/
    public final IBMFKOnDeleteActionType getOnDeleteAction() throws DbException {
        return (IBMFKOnDeleteActionType) get(fOnDeleteAction);
    }

    /**
     * Gets the "on update restrict" property's Boolean value of a DbIBMForeign's instance.
     * 
     * @return the "on update restrict" property's Boolean value
     * @deprecated use isOnUpdateRestrict() method instead
     **/
    public final Boolean getOnUpdateRestrict() throws DbException {
        return (Boolean) get(fOnUpdateRestrict);
    }

    /**
     * Tells whether a DbIBMForeign's instance is OnUpdateRestrict or not.
     * 
     * @return boolean
     **/
    public final boolean isOnUpdateRestrict() throws DbException {
        return getOnUpdateRestrict().booleanValue();
    }

    /**
     * Gets the "enforced" of a DbIBMForeign's instance.
     * 
     * @return the "enforced"
     **/
    public final IBMCheckEnforceType getEnforced() throws DbException {
        return (IBMCheckEnforceType) get(fEnforced);
    }

    /**
     * Gets the "enable query optimization" of a DbIBMForeign's instance.
     * 
     * @return the "enable query optimization"
     **/
    public final IBMCheckEnableQueryOptType getEnableQueryOptimization() throws DbException {
        return (IBMCheckEnableQueryOptType) get(fEnableQueryOptimization);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
