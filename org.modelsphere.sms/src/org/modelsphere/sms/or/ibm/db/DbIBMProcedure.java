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
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMOperationLibrary.html"
 * >DbIBMOperationLibrary</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/ibm/db/DbIBMParameter.html">DbIBMParameter</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbIBMProcedure extends DbORProcedure {

    //Meta

    public static final MetaField fLocator = new MetaField(LocaleMgr.db.getString("locator"));
    public static final MetaField fSpecificName = new MetaField(LocaleMgr.db
            .getString("specificName"));
    public static final MetaField fExternalName = new MetaField(LocaleMgr.db
            .getString("externalName"));
    public static final MetaField fCardinality = new MetaField(LocaleMgr.db
            .getString("cardinality"));
    public static final MetaField fDynamicResultSets = new MetaField(LocaleMgr.db
            .getString("dynamicResultSets"));
    public static final MetaField fExternal = new MetaField(LocaleMgr.db.getString("external"));
    public static final MetaField fLanguage = new MetaField(LocaleMgr.db.getString("language"));
    public static final MetaField fParameterStyle = new MetaField(LocaleMgr.db
            .getString("parameterStyle"));
    public static final MetaField fDeterministic = new MetaField(LocaleMgr.db
            .getString("deterministic"));
    public static final MetaField fFenced = new MetaField(LocaleMgr.db.getString("fenced"));
    public static final MetaField fNullInput = new MetaField(LocaleMgr.db.getString("nullInput"));
    public static final MetaField fSql = new MetaField(LocaleMgr.db.getString("sql"));
    public static final MetaField fStaticDispatch = new MetaField(LocaleMgr.db
            .getString("staticDispatch"));
    public static final MetaField fExternalAction = new MetaField(LocaleMgr.db
            .getString("externalAction"));
    public static final MetaField fScratchpad = new MetaField(LocaleMgr.db.getString("scratchpad"));
    public static final MetaField fFinalCall = new MetaField(LocaleMgr.db.getString("finalCall"));
    public static final MetaField fParallelism = new MetaField(LocaleMgr.db
            .getString("parallelism"));
    public static final MetaField fDbInfo = new MetaField(LocaleMgr.db.getString("dbInfo"));
    public static final MetaField fPredicate = new MetaField(LocaleMgr.db.getString("predicate"));
    public static final MetaField fInheritSpecialRegisters = new MetaField(LocaleMgr.db
            .getString("inheritSpecialRegisters"));
    public static final MetaField fFederated = new MetaField(LocaleMgr.db.getString("federated"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbIBMProcedure"), DbIBMProcedure.class, new MetaField[] { fLocator,
            fSpecificName, fExternalName, fCardinality, fDynamicResultSets, fExternal, fLanguage,
            fParameterStyle, fDeterministic, fFenced, fNullInput, fSql, fStaticDispatch,
            fExternalAction, fScratchpad, fFinalCall, fParallelism, fDbInfo, fPredicate,
            fInheritSpecialRegisters, fFederated }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORProcedure.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbIBMParameter.metaClass });

            fLocator.setJField(DbIBMProcedure.class.getDeclaredField("m_locator"));
            fSpecificName.setJField(DbIBMProcedure.class.getDeclaredField("m_specificName"));
            fExternalName.setJField(DbIBMProcedure.class.getDeclaredField("m_externalName"));
            fCardinality.setJField(DbIBMProcedure.class.getDeclaredField("m_cardinality"));
            fDynamicResultSets.setJField(DbIBMProcedure.class
                    .getDeclaredField("m_dynamicResultSets"));
            fExternal.setJField(DbIBMProcedure.class.getDeclaredField("m_external"));
            fLanguage.setJField(DbIBMProcedure.class.getDeclaredField("m_language"));
            fParameterStyle.setJField(DbIBMProcedure.class.getDeclaredField("m_parameterStyle"));
            fDeterministic.setJField(DbIBMProcedure.class.getDeclaredField("m_deterministic"));
            fFenced.setJField(DbIBMProcedure.class.getDeclaredField("m_fenced"));
            fNullInput.setJField(DbIBMProcedure.class.getDeclaredField("m_nullInput"));
            fSql.setJField(DbIBMProcedure.class.getDeclaredField("m_sql"));
            fStaticDispatch.setJField(DbIBMProcedure.class.getDeclaredField("m_staticDispatch"));
            fExternalAction.setJField(DbIBMProcedure.class.getDeclaredField("m_externalAction"));
            fScratchpad.setJField(DbIBMProcedure.class.getDeclaredField("m_scratchpad"));
            fFinalCall.setJField(DbIBMProcedure.class.getDeclaredField("m_finalCall"));
            fParallelism.setJField(DbIBMProcedure.class.getDeclaredField("m_parallelism"));
            fDbInfo.setJField(DbIBMProcedure.class.getDeclaredField("m_dbInfo"));
            fPredicate.setJField(DbIBMProcedure.class.getDeclaredField("m_predicate"));
            fInheritSpecialRegisters.setJField(DbIBMProcedure.class
                    .getDeclaredField("m_inheritSpecialRegisters"));
            fFederated.setJField(DbIBMProcedure.class.getDeclaredField("m_federated"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    boolean m_locator;
    String m_specificName;
    String m_externalName;
    SrInteger m_cardinality;
    SrInteger m_dynamicResultSets;
    boolean m_external;
    IBMProcedureLanguage m_language;
    IBMProcedureLanguage m_parameterStyle;
    IBMProcedureDeterministic m_deterministic;
    IBMProcedureFence m_fenced;
    IBMProcedureNullInput m_nullInput;
    IBMProcedureSql m_sql;
    boolean m_staticDispatch;
    IBMProcedureExtAction m_externalAction;
    int m_scratchpad;
    IBMProcedureFinalCall m_finalCall;
    IBMProcedureParallel m_parallelism;
    IBMProcedureDbInfo m_dbInfo;
    String m_predicate;
    boolean m_inheritSpecialRegisters;
    IBMFederatedOption m_federated;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbIBMProcedure() {
    }

    /**
     * Creates an instance of DbIBMProcedure.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbIBMProcedure(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setExternal(Boolean.FALSE);
        org.modelsphere.sms.or.ibm.db.util.IBMUtility.getInstance().setInitialValues(this);
    }

    //Setters

    /**
     * Sets the "locator" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "locator" property
     **/
    public final void setLocator(Boolean value) throws DbException {
        basicSet(fLocator, value);
    }

    /**
     * Sets the "specific name" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "specific name" property
     **/
    public final void setSpecificName(String value) throws DbException {
        basicSet(fSpecificName, value);
    }

    /**
     * Sets the "external name" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "external name" property
     **/
    public final void setExternalName(String value) throws DbException {
        basicSet(fExternalName, value);
    }

    /**
     * Sets the "cardinality" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "cardinality" property
     **/
    public final void setCardinality(Integer value) throws DbException {
        basicSet(fCardinality, value);
    }

    /**
     * Sets the "dynamic result sets" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "dynamic result sets" property
     **/
    public final void setDynamicResultSets(Integer value) throws DbException {
        basicSet(fDynamicResultSets, value);
    }

    /**
     * Sets the "external" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "external" property
     **/
    public final void setExternal(Boolean value) throws DbException {
        basicSet(fExternal, value);
    }

    /**
     * Sets the "language" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "language" property
     **/
    public final void setLanguage(IBMProcedureLanguage value) throws DbException {
        basicSet(fLanguage, value);
    }

    /**
     * Sets the "parameterstyle" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "parameterstyle" property
     **/
    public final void setParameterStyle(IBMProcedureLanguage value) throws DbException {
        basicSet(fParameterStyle, value);
    }

    /**
     * Sets the "deterministic" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "deterministic" property
     **/
    public final void setDeterministic(IBMProcedureDeterministic value) throws DbException {
        basicSet(fDeterministic, value);
    }

    /**
     * Sets the "fenced" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "fenced" property
     **/
    public final void setFenced(IBMProcedureFence value) throws DbException {
        basicSet(fFenced, value);
    }

    /**
     * Sets the "null input" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "null input" property
     **/
    public final void setNullInput(IBMProcedureNullInput value) throws DbException {
        basicSet(fNullInput, value);
    }

    /**
     * Sets the "sql" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "sql" property
     **/
    public final void setSql(IBMProcedureSql value) throws DbException {
        basicSet(fSql, value);
    }

    /**
     * Sets the "static dispatch" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "static dispatch" property
     **/
    public final void setStaticDispatch(Boolean value) throws DbException {
        basicSet(fStaticDispatch, value);
    }

    /**
     * Sets the "external action" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "external action" property
     **/
    public final void setExternalAction(IBMProcedureExtAction value) throws DbException {
        basicSet(fExternalAction, value);
    }

    /**
     * Sets the "scratch pad" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "scratch pad" property
     **/
    public final void setScratchpad(Integer value) throws DbException {
        basicSet(fScratchpad, value);
    }

    /**
     * Sets the "final call" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "final call" property
     **/
    public final void setFinalCall(IBMProcedureFinalCall value) throws DbException {
        basicSet(fFinalCall, value);
    }

    /**
     * Sets the "parallelism" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "parallelism" property
     **/
    public final void setParallelism(IBMProcedureParallel value) throws DbException {
        basicSet(fParallelism, value);
    }

    /**
     * Sets the "db info" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "db info" property
     **/
    public final void setDbInfo(IBMProcedureDbInfo value) throws DbException {
        basicSet(fDbInfo, value);
    }

    /**
     * Sets the "predicate" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "predicate" property
     **/
    public final void setPredicate(String value) throws DbException {
        basicSet(fPredicate, value);
    }

    /**
     * Sets the "inherit special registers" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "inherit special registers" property
     **/
    public final void setInheritSpecialRegisters(Boolean value) throws DbException {
        basicSet(fInheritSpecialRegisters, value);
    }

    /**
     * Sets the "federated" property of a DbIBMProcedure's instance.
     * 
     * @param value
     *            the "federated" property
     **/
    public final void setFederated(IBMFederatedOption value) throws DbException {
        basicSet(fFederated, value);
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
     * Gets the "locator" property's Boolean value of a DbIBMProcedure's instance.
     * 
     * @return the "locator" property's Boolean value
     * @deprecated use isLocator() method instead
     **/
    public final Boolean getLocator() throws DbException {
        return (Boolean) get(fLocator);
    }

    /**
     * Tells whether a DbIBMProcedure's instance is locator or not.
     * 
     * @return boolean
     **/
    public final boolean isLocator() throws DbException {
        return getLocator().booleanValue();
    }

    /**
     * Gets the "specific name" property of a DbIBMProcedure's instance.
     * 
     * @return the "specific name" property
     **/
    public final String getSpecificName() throws DbException {
        return (String) get(fSpecificName);
    }

    /**
     * Gets the "external name" property of a DbIBMProcedure's instance.
     * 
     * @return the "external name" property
     **/
    public final String getExternalName() throws DbException {
        return (String) get(fExternalName);
    }

    /**
     * Gets the "cardinality" of a DbIBMProcedure's instance.
     * 
     * @return the "cardinality"
     **/
    public final Integer getCardinality() throws DbException {
        return (Integer) get(fCardinality);
    }

    /**
     * Gets the "dynamic result sets" of a DbIBMProcedure's instance.
     * 
     * @return the "dynamic result sets"
     **/
    public final Integer getDynamicResultSets() throws DbException {
        return (Integer) get(fDynamicResultSets);
    }

    /**
     * Gets the "external" property's Boolean value of a DbIBMProcedure's instance.
     * 
     * @return the "external" property's Boolean value
     * @deprecated use isExternal() method instead
     **/
    public final Boolean getExternal() throws DbException {
        return (Boolean) get(fExternal);
    }

    /**
     * Tells whether a DbIBMProcedure's instance is external or not.
     * 
     * @return boolean
     **/
    public final boolean isExternal() throws DbException {
        return getExternal().booleanValue();
    }

    /**
     * Gets the "language" of a DbIBMProcedure's instance.
     * 
     * @return the "language"
     **/
    public final IBMProcedureLanguage getLanguage() throws DbException {
        return (IBMProcedureLanguage) get(fLanguage);
    }

    /**
     * Gets the "parameterstyle" of a DbIBMProcedure's instance.
     * 
     * @return the "parameterstyle"
     **/
    public final IBMProcedureLanguage getParameterStyle() throws DbException {
        return (IBMProcedureLanguage) get(fParameterStyle);
    }

    /**
     * Gets the "deterministic" of a DbIBMProcedure's instance.
     * 
     * @return the "deterministic"
     **/
    public final IBMProcedureDeterministic getDeterministic() throws DbException {
        return (IBMProcedureDeterministic) get(fDeterministic);
    }

    /**
     * Gets the "fenced" of a DbIBMProcedure's instance.
     * 
     * @return the "fenced"
     **/
    public final IBMProcedureFence getFenced() throws DbException {
        return (IBMProcedureFence) get(fFenced);
    }

    /**
     * Gets the "null input" of a DbIBMProcedure's instance.
     * 
     * @return the "null input"
     **/
    public final IBMProcedureNullInput getNullInput() throws DbException {
        return (IBMProcedureNullInput) get(fNullInput);
    }

    /**
     * Gets the "sql" of a DbIBMProcedure's instance.
     * 
     * @return the "sql"
     **/
    public final IBMProcedureSql getSql() throws DbException {
        return (IBMProcedureSql) get(fSql);
    }

    /**
     * Gets the "static dispatch" property's Boolean value of a DbIBMProcedure's instance.
     * 
     * @return the "static dispatch" property's Boolean value
     * @deprecated use isStaticDispatch() method instead
     **/
    public final Boolean getStaticDispatch() throws DbException {
        return (Boolean) get(fStaticDispatch);
    }

    /**
     * Tells whether a DbIBMProcedure's instance is staticDispatch or not.
     * 
     * @return boolean
     **/
    public final boolean isStaticDispatch() throws DbException {
        return getStaticDispatch().booleanValue();
    }

    /**
     * Gets the "external action" of a DbIBMProcedure's instance.
     * 
     * @return the "external action"
     **/
    public final IBMProcedureExtAction getExternalAction() throws DbException {
        return (IBMProcedureExtAction) get(fExternalAction);
    }

    /**
     * Gets the "scratch pad" property of a DbIBMProcedure's instance.
     * 
     * @return the "scratch pad" property
     **/
    public final Integer getScratchpad() throws DbException {
        return (Integer) get(fScratchpad);
    }

    /**
     * Gets the "final call" of a DbIBMProcedure's instance.
     * 
     * @return the "final call"
     **/
    public final IBMProcedureFinalCall getFinalCall() throws DbException {
        return (IBMProcedureFinalCall) get(fFinalCall);
    }

    /**
     * Gets the "parallelism" of a DbIBMProcedure's instance.
     * 
     * @return the "parallelism"
     **/
    public final IBMProcedureParallel getParallelism() throws DbException {
        return (IBMProcedureParallel) get(fParallelism);
    }

    /**
     * Gets the "db info" of a DbIBMProcedure's instance.
     * 
     * @return the "db info"
     **/
    public final IBMProcedureDbInfo getDbInfo() throws DbException {
        return (IBMProcedureDbInfo) get(fDbInfo);
    }

    /**
     * Gets the "predicate" property of a DbIBMProcedure's instance.
     * 
     * @return the "predicate" property
     **/
    public final String getPredicate() throws DbException {
        return (String) get(fPredicate);
    }

    /**
     * Gets the "inherit special registers" property's Boolean value of a DbIBMProcedure's instance.
     * 
     * @return the "inherit special registers" property's Boolean value
     * @deprecated use isInheritSpecialRegisters() method instead
     **/
    public final Boolean getInheritSpecialRegisters() throws DbException {
        return (Boolean) get(fInheritSpecialRegisters);
    }

    /**
     * Tells whether a DbIBMProcedure's instance is inheritSpecialRegisters or not.
     * 
     * @return boolean
     **/
    public final boolean isInheritSpecialRegisters() throws DbException {
        return getInheritSpecialRegisters().booleanValue();
    }

    /**
     * Gets the "federated" of a DbIBMProcedure's instance.
     * 
     * @return the "federated"
     **/
    public final IBMFederatedOption getFederated() throws DbException {
        return (IBMFederatedOption) get(fFederated);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
