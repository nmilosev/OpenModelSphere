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
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMDataModel.html">DbIBMDataModel</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMColumn.html">DbIBMColumn</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMForeign.html">DbIBMForeign</A>, <A
 * HREF
 * ="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMPrimaryUnique.html">DbIBMPrimaryUnique<
 * /A>, <A HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMCheck.html">DbIBMCheck</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMIndex.html">DbIBMIndex</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMTrigger.html">DbIBMTrigger</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbIBMTable extends DbORTable {

    //Meta

    public static final MetaField fReplicateData = new MetaField(LocaleMgr.db
            .getString("replicateData"));
    public static final MetaField fNoLogChanges = new MetaField(LocaleMgr.db
            .getString("noLogChanges"));
    public static final MetaRelation1 fInTablespace = new MetaRelation1(LocaleMgr.db
            .getString("inTablespace"), 0);
    public static final MetaRelation1 fLongInTablespace = new MetaRelation1(LocaleMgr.db
            .getString("longInTablespace"), 0);
    public static final MetaRelation1 fIndexInTablespace = new MetaRelation1(LocaleMgr.db
            .getString("indexInTablespace"), 0);
    public static final MetaField fDataCapture = new MetaField(LocaleMgr.db
            .getString("dataCapture"));
    public static final MetaField fIsFederated = new MetaField(LocaleMgr.db
            .getString("isFederated"));
    public static final MetaField fValueCompression = new MetaField(LocaleMgr.db
            .getString("valueCompression"));
    public static final MetaField fWithRestrictOnDrop = new MetaField(LocaleMgr.db
            .getString("withRestrictOnDrop"));
    public static final MetaField fNotLoggedInitially = new MetaField(LocaleMgr.db
            .getString("notLoggedInitially"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbIBMTable"),
            DbIBMTable.class, new MetaField[] { fReplicateData, fNoLogChanges, fInTablespace,
                    fLongInTablespace, fIndexInTablespace, fDataCapture, fIsFederated,
                    fValueCompression, fWithRestrictOnDrop, fNotLoggedInitially },
            MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORTable.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbIBMColumn.metaClass,
                    DbIBMForeign.metaClass, DbIBMPrimaryUnique.metaClass, DbIBMCheck.metaClass,
                    DbIBMIndex.metaClass, DbIBMTrigger.metaClass });

            fReplicateData.setJField(DbIBMTable.class.getDeclaredField("m_replicateData"));
            fNoLogChanges.setJField(DbIBMTable.class.getDeclaredField("m_noLogChanges"));
            fNoLogChanges.setVisibleInScreen(false);
            fInTablespace.setJField(DbIBMTable.class.getDeclaredField("m_inTablespace"));
            fLongInTablespace.setJField(DbIBMTable.class.getDeclaredField("m_longInTablespace"));
            fIndexInTablespace.setJField(DbIBMTable.class.getDeclaredField("m_indexInTablespace"));
            fDataCapture.setJField(DbIBMTable.class.getDeclaredField("m_dataCapture"));
            fIsFederated.setJField(DbIBMTable.class.getDeclaredField("m_isFederated"));
            fValueCompression.setJField(DbIBMTable.class.getDeclaredField("m_valueCompression"));
            fWithRestrictOnDrop
                    .setJField(DbIBMTable.class.getDeclaredField("m_withRestrictOnDrop"));
            fNotLoggedInitially
                    .setJField(DbIBMTable.class.getDeclaredField("m_notLoggedInitially"));

            fInTablespace.setOppositeRel(DbIBMTablespace.fTables);
            fLongInTablespace.setOppositeRel(DbIBMTablespace.fTablesForLOB);
            fIndexInTablespace.setOppositeRel(DbIBMTablespace.fTablesForIndexes);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    boolean m_replicateData;
    boolean m_noLogChanges;
    DbIBMTablespace m_inTablespace;
    DbIBMTablespace m_longInTablespace;
    DbIBMTablespace m_indexInTablespace;
    IBMDataCaptureOption m_dataCapture;
    IBMFederatedOption m_isFederated;
    boolean m_valueCompression;
    boolean m_withRestrictOnDrop;
    boolean m_notLoggedInitially;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbIBMTable() {
    }

    /**
     * Creates an instance of DbIBMTable.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbIBMTable(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setReplicateData(Boolean.FALSE);
        setNoLogChanges(Boolean.FALSE);
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));
    }

    //Setters

    /**
     * Sets the "replicated" property of a DbIBMTable's instance.
     * 
     * @param value
     *            the "replicated" property
     **/
    public final void setReplicateData(Boolean value) throws DbException {
        basicSet(fReplicateData, value);
    }

    /**
     * Sets the "changes not logged" property of a DbIBMTable's instance.
     * 
     * @param value
     *            the "changes not logged" property
     **/
    public final void setNoLogChanges(Boolean value) throws DbException {
        basicSet(fNoLogChanges, value);
    }

    /**
     * Sets the in tablespace object associated to a DbIBMTable's instance.
     * 
     * @param value
     *            the in tablespace object to be associated
     **/
    public final void setInTablespace(DbIBMTablespace value) throws DbException {
        basicSet(fInTablespace, value);
    }

    /**
     * Sets the long objects in tablespace object associated to a DbIBMTable's instance.
     * 
     * @param value
     *            the long objects in tablespace object to be associated
     **/
    public final void setLongInTablespace(DbIBMTablespace value) throws DbException {
        basicSet(fLongInTablespace, value);
    }

    /**
     * Sets the index in tablespace object associated to a DbIBMTable's instance.
     * 
     * @param value
     *            the index in tablespace object to be associated
     **/
    public final void setIndexInTablespace(DbIBMTablespace value) throws DbException {
        basicSet(fIndexInTablespace, value);
    }

    /**
     * Sets the "data capture" property of a DbIBMTable's instance.
     * 
     * @param value
     *            the "data capture" property
     **/
    public final void setDataCapture(IBMDataCaptureOption value) throws DbException {
        basicSet(fDataCapture, value);
    }

    /**
     * Sets the "is federated" property of a DbIBMTable's instance.
     * 
     * @param value
     *            the "is federated" property
     **/
    public final void setIsFederated(IBMFederatedOption value) throws DbException {
        basicSet(fIsFederated, value);
    }

    /**
     * Sets the "value compression" property of a DbIBMTable's instance.
     * 
     * @param value
     *            the "value compression" property
     **/
    public final void setValueCompression(Boolean value) throws DbException {
        basicSet(fValueCompression, value);
    }

    /**
     * Sets the "with restrict on drop" property of a DbIBMTable's instance.
     * 
     * @param value
     *            the "with restrict on drop" property
     **/
    public final void setWithRestrictOnDrop(Boolean value) throws DbException {
        basicSet(fWithRestrictOnDrop, value);
    }

    /**
     * Sets the "not logged initially" property of a DbIBMTable's instance.
     * 
     * @param value
     *            the "not logged initially" property
     **/
    public final void setNotLoggedInitially(Boolean value) throws DbException {
        basicSet(fNotLoggedInitially, value);
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
     * Gets the "replicated" property's Boolean value of a DbIBMTable's instance.
     * 
     * @return the "replicated" property's Boolean value
     * @deprecated use isReplicateData() method instead
     **/
    public final Boolean getReplicateData() throws DbException {
        return (Boolean) get(fReplicateData);
    }

    /**
     * Tells whether a DbIBMTable's instance is replicateData or not.
     * 
     * @return boolean
     **/
    public final boolean isReplicateData() throws DbException {
        return getReplicateData().booleanValue();
    }

    /**
     * Gets the "changes not logged" property's Boolean value of a DbIBMTable's instance.
     * 
     * @return the "changes not logged" property's Boolean value
     * @deprecated use isNoLogChanges() method instead
     **/
    public final Boolean getNoLogChanges() throws DbException {
        return (Boolean) get(fNoLogChanges);
    }

    /**
     * Tells whether a DbIBMTable's instance is noLogChanges or not.
     * 
     * @return boolean
     **/
    public final boolean isNoLogChanges() throws DbException {
        return getNoLogChanges().booleanValue();
    }

    /**
     * Gets the in tablespace object associated to a DbIBMTable's instance.
     * 
     * @return the in tablespace object
     **/
    public final DbIBMTablespace getInTablespace() throws DbException {
        return (DbIBMTablespace) get(fInTablespace);
    }

    /**
     * Gets the long objects in tablespace object associated to a DbIBMTable's instance.
     * 
     * @return the long objects in tablespace object
     **/
    public final DbIBMTablespace getLongInTablespace() throws DbException {
        return (DbIBMTablespace) get(fLongInTablespace);
    }

    /**
     * Gets the index in tablespace object associated to a DbIBMTable's instance.
     * 
     * @return the index in tablespace object
     **/
    public final DbIBMTablespace getIndexInTablespace() throws DbException {
        return (DbIBMTablespace) get(fIndexInTablespace);
    }

    /**
     * Gets the "data capture" of a DbIBMTable's instance.
     * 
     * @return the "data capture"
     **/
    public final IBMDataCaptureOption getDataCapture() throws DbException {
        return (IBMDataCaptureOption) get(fDataCapture);
    }

    /**
     * Gets the "is federated" of a DbIBMTable's instance.
     * 
     * @return the "is federated"
     **/
    public final IBMFederatedOption getIsFederated() throws DbException {
        return (IBMFederatedOption) get(fIsFederated);
    }

    /**
     * Gets the "value compression" property's Boolean value of a DbIBMTable's instance.
     * 
     * @return the "value compression" property's Boolean value
     * @deprecated use isValueCompression() method instead
     **/
    public final Boolean getValueCompression() throws DbException {
        return (Boolean) get(fValueCompression);
    }

    /**
     * Tells whether a DbIBMTable's instance is valueCompression or not.
     * 
     * @return boolean
     **/
    public final boolean isValueCompression() throws DbException {
        return getValueCompression().booleanValue();
    }

    /**
     * Gets the "with restrict on drop" property's Boolean value of a DbIBMTable's instance.
     * 
     * @return the "with restrict on drop" property's Boolean value
     * @deprecated use isWithRestrictOnDrop() method instead
     **/
    public final Boolean getWithRestrictOnDrop() throws DbException {
        return (Boolean) get(fWithRestrictOnDrop);
    }

    /**
     * Tells whether a DbIBMTable's instance is withRestrictOnDrop or not.
     * 
     * @return boolean
     **/
    public final boolean isWithRestrictOnDrop() throws DbException {
        return getWithRestrictOnDrop().booleanValue();
    }

    /**
     * Gets the "not logged initially" property's Boolean value of a DbIBMTable's instance.
     * 
     * @return the "not logged initially" property's Boolean value
     * @deprecated use isNotLoggedInitially() method instead
     **/
    public final Boolean getNotLoggedInitially() throws DbException {
        return (Boolean) get(fNotLoggedInitially);
    }

    /**
     * Tells whether a DbIBMTable's instance is notLoggedInitially or not.
     * 
     * @return boolean
     **/
    public final boolean isNotLoggedInitially() throws DbException {
        return getNotLoggedInitially().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
