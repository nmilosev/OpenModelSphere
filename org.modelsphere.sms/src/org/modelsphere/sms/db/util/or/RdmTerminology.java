/*************************************************************************

Copyright (C) 2010 Grandite

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
package org.modelsphere.sms.db.util.or;

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.DbSMSUser;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTrigger;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.generic.db.DbGECheck;
import org.modelsphere.sms.or.generic.db.DbGEColumn;
import org.modelsphere.sms.or.generic.db.DbGEDataModel;
import org.modelsphere.sms.or.generic.db.DbGEForeign;
import org.modelsphere.sms.or.generic.db.DbGEIndex;
import org.modelsphere.sms.or.generic.db.DbGEPrimaryUnique;
import org.modelsphere.sms.or.generic.db.DbGETable;
import org.modelsphere.sms.or.generic.db.DbGETrigger;
import org.modelsphere.sms.or.generic.db.DbGEView;
import org.modelsphere.sms.or.ibm.db.DbIBMCheck;
import org.modelsphere.sms.or.ibm.db.DbIBMColumn;
import org.modelsphere.sms.or.ibm.db.DbIBMDataModel;
import org.modelsphere.sms.or.ibm.db.DbIBMForeign;
import org.modelsphere.sms.or.ibm.db.DbIBMIndex;
import org.modelsphere.sms.or.ibm.db.DbIBMPrimaryUnique;
import org.modelsphere.sms.or.ibm.db.DbIBMTable;
import org.modelsphere.sms.or.ibm.db.DbIBMTrigger;
import org.modelsphere.sms.or.ibm.db.DbIBMView;
import org.modelsphere.sms.or.informix.db.DbINFCheck;
import org.modelsphere.sms.or.informix.db.DbINFColumn;
import org.modelsphere.sms.or.informix.db.DbINFDataModel;
import org.modelsphere.sms.or.informix.db.DbINFForeign;
import org.modelsphere.sms.or.informix.db.DbINFIndex;
import org.modelsphere.sms.or.informix.db.DbINFPrimaryUnique;
import org.modelsphere.sms.or.informix.db.DbINFTable;
import org.modelsphere.sms.or.informix.db.DbINFTrigger;
import org.modelsphere.sms.or.informix.db.DbINFView;
import org.modelsphere.sms.or.oracle.db.DbORACheck;
import org.modelsphere.sms.or.oracle.db.DbORAColumn;
import org.modelsphere.sms.or.oracle.db.DbORADataModel;
import org.modelsphere.sms.or.oracle.db.DbORAForeign;
import org.modelsphere.sms.or.oracle.db.DbORAIndex;
import org.modelsphere.sms.or.oracle.db.DbORAPrimaryUnique;
import org.modelsphere.sms.or.oracle.db.DbORATable;
import org.modelsphere.sms.or.oracle.db.DbORATrigger;
import org.modelsphere.sms.or.oracle.db.DbORAView;

//Defines Relational Data Model (RDM) Terminology
public class RdmTerminology extends OrTerminology {
	//RDM Terms
	private static final String ACTION = LocaleMgr.term.getString("Action"); 
	private static final String ACTIONS = LocaleMgr.term.getString("Actions"); 
	private static final String ACTION_CATEGORY = LocaleMgr.term.getString("ActionCategory");
	private static final String CHILD_DIRECTION = LocaleMgr.term.getString("ChildDirection"); 
    private static final String CODED_NAME = LocaleMgr.term.getString("CodedName"); 
	private static final String COMMENT = LocaleMgr.term.getString("Comment"); 
	private static final String CONNECTIVITIES = LocaleMgr.term.getString("Connectivities"); 
	private static final String CONNECTOR = LocaleMgr.term.getString("Connector"); 
	private static final String CONNECTORS = LocaleMgr.term.getString("Connectors"); 
    private static final String CONSTRAINT = LocaleMgr.term.getString("Constraint"); 
    private static final String CONSTRAINTS = LocaleMgr.term.getString("Constraints"); 
	private static final String CREATOR = LocaleMgr.term.getString("Creator");
	private static final String DOMAIN_DATA_TYPE = LocaleMgr.term.getString("DomainDataType"); 
	private static final String DEFAULT_VALUE = LocaleMgr.term.getString("DefaultValue"); 
	private static final String DATA_TYPE = LocaleMgr.term.getString("DataType"); 
	private static final String DATA_TYPES = LocaleMgr.term.getString("DataTypes"); 
    //private static final String DEFINITION = LocaleMgr.term.getString("Definition"); 
	private static final String DIRECTION = LocaleMgr.term.getString("Direction"); 
	private static final String DIRECTIONS = LocaleMgr.term.getString("Directions"); 
	private static final String INSTRUCTIONS = LocaleMgr.term.getString("Instructions"); 
	private static final String LOGICAL_DOMAIN = LocaleMgr.term.getString("LogicalDomaimDataType"); 
	private static final String MANAGER = LocaleMgr.term.getString("Manager"); 
    private static final String NULL_VALUE_POSSIBLE = LocaleMgr.term.getString("NullValuePossible"); 
    private static final String ORIGIN_TABLE = LocaleMgr.term.getString("OriginTable"); 
    private static final String PRE_CONDITION = LocaleMgr.term.getString("PreCondition"); 
    private static final String PRIMARY_ALTERNATE_KEY = LocaleMgr.term.getString("PrimaryAlternateKey");  
    private static final String PRIMARY_ALTERNATE_KEYS = LocaleMgr.term.getString("PrimaryAlternateKeys");  
    private static final String SCHEMA = LocaleMgr.term.getString("Schema");
    private static final String SCHEMAS = LocaleMgr.term.getString("Schemas");
    private static final String SPECIFIC_RANGE_CONNECTIVITIES = LocaleMgr.term.getString("SpecificRangeConnectivities"); 
    private static final String TARGET_SYSTEM = LocaleMgr.term.getString("TargetSystem"); 
    private static final String TARGET_SYSTEMS = LocaleMgr.term.getString("TargetSystems"); 
	
	@Override
	public void defineTerminology(Terminology terminology) {
		super.defineTerminology(terminology); 
		
		//
		// Define terms for meta classes
		//
		
		//data model
		terminology.define(DbORDataModel.metaClass, SCHEMA, SCHEMAS);
        terminology.define(DbGEDataModel.metaClass, SCHEMA, SCHEMAS);
        terminology.define(DbORADataModel.metaClass, SCHEMA, SCHEMAS);
        terminology.define(DbIBMDataModel.metaClass, SCHEMA, SCHEMAS);
        terminology.define(DbINFDataModel.metaClass, SCHEMA, SCHEMAS);
        
        //association
        terminology.define(DbORAssociation.metaClass, CONNECTOR, CONNECTORS);
        terminology.define(DbORAssociationEnd.metaClass, DIRECTION, DIRECTIONS);
        
        //constraint
        terminology.define(DbORCheck.metaClass, CONSTRAINT, CONSTRAINTS);
        terminology.define(DbGECheck.metaClass, CONSTRAINT, CONSTRAINTS);
        terminology.define(DbORACheck.metaClass, CONSTRAINT, CONSTRAINTS);
        terminology.define(DbIBMCheck.metaClass, CONSTRAINT, CONSTRAINTS);
        terminology.define(DbINFCheck.metaClass, CONSTRAINT, CONSTRAINTS);
        
        //trigger
        terminology.define(DbORTrigger.metaClass, ACTION, ACTIONS);
        terminology.define(DbORATrigger.metaClass, ACTION, ACTIONS);
        terminology.define(DbINFTrigger.metaClass, ACTION, ACTIONS);
        terminology.define(DbIBMTrigger.metaClass, ACTION, ACTIONS);
        terminology.define(DbGETrigger.metaClass, ACTION, ACTIONS);
        
        //primary/unique key
        terminology.define(DbORPrimaryUnique.metaClass, PRIMARY_ALTERNATE_KEY, PRIMARY_ALTERNATE_KEYS);
        terminology.define(DbORAPrimaryUnique.metaClass, PRIMARY_ALTERNATE_KEY, PRIMARY_ALTERNATE_KEYS);
        terminology.define(DbINFPrimaryUnique.metaClass, PRIMARY_ALTERNATE_KEY, PRIMARY_ALTERNATE_KEYS);
        terminology.define(DbIBMPrimaryUnique.metaClass, PRIMARY_ALTERNATE_KEY, PRIMARY_ALTERNATE_KEYS);
        terminology.define(DbGEPrimaryUnique.metaClass, PRIMARY_ALTERNATE_KEY, PRIMARY_ALTERNATE_KEYS);
        
        //built-in type
        terminology.define(DbSMSBuiltInTypePackage.metaClass, TARGET_SYSTEM, TARGET_SYSTEMS);
        terminology.define(DbORBuiltInType.metaClass, DATA_TYPE, DATA_TYPES);

        
		//
		// Define terms for meta fields
		//
        
        //data model
        
        //rename physical name, description and hide UML stereotype
	    defineSemanticalObjectMetaFields(terminology, DbORDataModel.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbGEDataModel.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbORADataModel.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbIBMDataModel.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbINFDataModel.metaClass); 
        
		terminology.define(DbORDataModel.metaClass,  DbORDataModel.fAuthor, CREATOR, CREATOR);
		terminology.define(DbGEDataModel.metaClass,  DbGEDataModel.fAuthor, CREATOR, CREATOR);
		terminology.define(DbORADataModel.metaClass, DbORADataModel.fAuthor, CREATOR, CREATOR);
		terminology.define(DbIBMDataModel.metaClass, DbIBMDataModel.fAuthor, CREATOR, CREATOR);
		terminology.define(DbINFDataModel.metaClass, DbINFDataModel.fAuthor, CREATOR, CREATOR);
		
		DbORDataModel.fIsValidated.setVisibleInScreen(false); 
		DbGEDataModel.fIsValidated.setVisibleInScreen(false); 
		DbORADataModel.fIsValidated.setVisibleInScreen(false); 
		DbIBMDataModel.fIsValidated.setVisibleInScreen(false); 
		DbINFDataModel.fIsValidated.setVisibleInScreen(false); 
		
		DbORDataModel.fDeploymentDatabase.setVisibleInScreen(false); 
		DbGEDataModel.fDeploymentDatabase.setVisibleInScreen(false); 
		DbORADataModel.fDeploymentDatabase.setVisibleInScreen(false); 
		DbIBMDataModel.fDeploymentDatabase.setVisibleInScreen(false); 
		DbINFDataModel.fDeploymentDatabase.setVisibleInScreen(false); 

		//table
	    
	    //rename physical name, description and hide UML stereotype
	    defineSemanticalObjectMetaFields(terminology, DbORTable.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbGETable.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbORATable.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbIBMTable.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbINFTable.metaClass); 

        DbORTable.fSelectionRule.setVisibleInScreen(true);
        DbGETable.fSelectionRule.setVisibleInScreen(true);
        DbORATable.fSelectionRule.setVisibleInScreen(true);
        DbIBMTable.fSelectionRule.setVisibleInScreen(true);
        DbINFTable.fSelectionRule.setVisibleInScreen(true);
	    
	    //view
        
	    //rename physical name, description and hide UML stereotype
	    defineSemanticalObjectMetaFields(terminology, DbORView.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbGEView.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbORAView.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbIBMView.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbINFView.metaClass); 
	    	    
	    //column
	    
	    //rename physical name, description and hide UML stereotype
	    defineSemanticalObjectMetaFields(terminology, DbORColumn.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbGEColumn.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbORAColumn.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbIBMColumn.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbINFColumn.metaClass); 
	    
	    terminology.define(DbORColumn.metaClass,  DbORColumn.fType, DOMAIN_DATA_TYPE, DOMAIN_DATA_TYPE);
	    terminology.define(DbGEColumn.metaClass,  DbGEColumn.fType, DOMAIN_DATA_TYPE, DOMAIN_DATA_TYPE);
	    terminology.define(DbORAColumn.metaClass, DbORAColumn.fType, DOMAIN_DATA_TYPE, DOMAIN_DATA_TYPE);
	    terminology.define(DbIBMColumn.metaClass, DbIBMColumn.fType, DOMAIN_DATA_TYPE, DOMAIN_DATA_TYPE);
	    terminology.define(DbINFColumn.metaClass, DbINFColumn.fType, DOMAIN_DATA_TYPE, DOMAIN_DATA_TYPE);
	    
	    terminology.define(DbORColumn.metaClass,  DbORColumn.fNull, NULL_VALUE_POSSIBLE, NULL_VALUE_POSSIBLE);
	    terminology.define(DbGEColumn.metaClass,  DbGEColumn.fNull, NULL_VALUE_POSSIBLE, NULL_VALUE_POSSIBLE);
	    terminology.define(DbORAColumn.metaClass, DbORAColumn.fNull, NULL_VALUE_POSSIBLE, NULL_VALUE_POSSIBLE);
	    terminology.define(DbIBMColumn.metaClass, DbIBMColumn.fNull, NULL_VALUE_POSSIBLE, NULL_VALUE_POSSIBLE);
	    terminology.define(DbINFColumn.metaClass, DbINFColumn.fNull, NULL_VALUE_POSSIBLE, NULL_VALUE_POSSIBLE);
	    
	    terminology.define(DbORColumn.metaClass,  DbORColumn.fInitialValue, DEFAULT_VALUE, DEFAULT_VALUE);
	    terminology.define(DbGEColumn.metaClass,  DbGEColumn.fInitialValue, DEFAULT_VALUE, DEFAULT_VALUE);
	    terminology.define(DbORAColumn.metaClass, DbORAColumn.fInitialValue, DEFAULT_VALUE, DEFAULT_VALUE);
	    terminology.define(DbIBMColumn.metaClass, DbIBMColumn.fInitialValue, DEFAULT_VALUE, DEFAULT_VALUE);
	    terminology.define(DbINFColumn.metaClass, DbINFColumn.fInitialValue, DEFAULT_VALUE, DEFAULT_VALUE);
	    
	    //association
	    defineSemanticalObjectMetaFields(terminology, DbORAssociation.metaClass); 
	    
	    DbORAssociation.fToFrontEnd.setVisibleInScreen(false);
	    DbORAssociation.fSuperCopy.setVisibleInScreen(false); 
	    DbORAssociation.fSubCopies.setVisibleInScreen(false); 
	    
	    //association end
	    defineSemanticalObjectMetaFields(terminology, DbORAssociationEnd.metaClass); 
	    
	    terminology.define(DbORAssociationEnd.metaClass, DbORAssociationEnd.fMultiplicity, CONNECTIVITIES, CONNECTIVITIES);
	    terminology.define(DbORAssociationEnd.metaClass, DbORAssociationEnd.fSpecificRangeMultiplicity, SPECIFIC_RANGE_CONNECTIVITIES, SPECIFIC_RANGE_CONNECTIVITIES);
	    terminology.define(DbORAssociationEnd.metaClass, DbObject.fComponents, DIRECTIONS, DIRECTIONS);
	    
	    terminology.define(DbORAssociationEnd.metaClass, DbORAssociationEnd.fClassifier, ORIGIN_TABLE, ORIGIN_TABLE);
	    terminology.define(DbORAssociationEnd.metaClass, DbORAssociationEnd.fNavigable, CHILD_DIRECTION, CHILD_DIRECTION);
	    
	    DbORAssociationEnd.fConstraintType.setVisibleInScreen(false); 
	    DbORAssociationEnd.fReferencedConstraint.setVisibleInScreen(false); 
	    
	    //constraint
	    
	    //rename physical name, description and hide UML stereotype
	    defineSemanticalObjectMetaFields(terminology, DbORCheck.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbGECheck.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbORACheck.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbIBMCheck.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbINFCheck.metaClass); 
        
        DbORCheck.fSuperCopy.setVisibleInScreen(false);
        DbGECheck.fSuperCopy.setVisibleInScreen(false);
        DbORACheck.fSuperCopy.setVisibleInScreen(false);
        DbIBMCheck.fSuperCopy.setVisibleInScreen(false);
        DbINFCheck.fSuperCopy.setVisibleInScreen(false);
        
        //index
        
	    //rename physical name, description and hide UML stereotype
	    defineSemanticalObjectMetaFields(terminology, DbORIndex.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbGEIndex.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbORAIndex.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbIBMIndex.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbINFIndex.metaClass); 
	            
        DbORIndex.fSuperCopy.setVisibleInScreen(false);
        DbGEIndex.fSuperCopy.setVisibleInScreen(false);
        DbORAIndex.fSuperCopy.setVisibleInScreen(false);
        DbIBMIndex.fSuperCopy.setVisibleInScreen(false);
        DbINFIndex.fSuperCopy.setVisibleInScreen(false);
        
        DbORIndex.fSubCopies.setVisibleInScreen(false);
        DbGEIndex.fSubCopies.setVisibleInScreen(false);
        DbORAIndex.fSubCopies.setVisibleInScreen(false);
        DbIBMIndex.fSubCopies.setVisibleInScreen(false);
        DbINFIndex.fSubCopies.setVisibleInScreen(false);
        
        DbORIndex.fConstraint.setVisibleInScreen(false);
        DbGEIndex.fConstraint.setVisibleInScreen(false);
        DbORAIndex.fConstraint.setVisibleInScreen(false);
        DbIBMIndex.fConstraint.setVisibleInScreen(false);
        DbINFIndex.fConstraint.setVisibleInScreen(false);
        
        //trigger
        
	    //rename physical name, description and hide UML stereotype
	    defineSemanticalObjectMetaFields(terminology, DbORTrigger.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbGETrigger.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbORATrigger.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbIBMTrigger.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbINFTrigger.metaClass); 
	            
        terminology.define(DbORTrigger.metaClass,  DbORTrigger.fEvent, ACTION_CATEGORY, ACTION_CATEGORY);
        terminology.define(DbGETrigger.metaClass,  DbGETrigger.fEvent, ACTION_CATEGORY, ACTION_CATEGORY);
        terminology.define(DbORATrigger.metaClass, DbORATrigger.fEvent, ACTION_CATEGORY, ACTION_CATEGORY);
        terminology.define(DbIBMTrigger.metaClass, DbIBMTrigger.fEvent, ACTION_CATEGORY, ACTION_CATEGORY);
        terminology.define(DbINFTrigger.metaClass, DbINFTrigger.fEvent, ACTION_CATEGORY, ACTION_CATEGORY);
        
        terminology.define(DbORTrigger.metaClass,  DbORTrigger.fWhenCondition, PRE_CONDITION, PRE_CONDITION);
        terminology.define(DbGETrigger.metaClass,  DbGETrigger.fWhenCondition, PRE_CONDITION, PRE_CONDITION);
        terminology.define(DbORATrigger.metaClass, DbORATrigger.fWhenCondition, PRE_CONDITION, PRE_CONDITION);
        terminology.define(DbIBMTrigger.metaClass, DbIBMTrigger.fWhenCondition, PRE_CONDITION, PRE_CONDITION);
        terminology.define(DbINFTrigger.metaClass, DbINFTrigger.fWhenCondition, PRE_CONDITION, PRE_CONDITION);
        
        terminology.define(DbORTrigger.metaClass,  DbORTrigger.fBody, INSTRUCTIONS, INSTRUCTIONS);
        terminology.define(DbGETrigger.metaClass,  DbGETrigger.fBody, INSTRUCTIONS, INSTRUCTIONS);
        terminology.define(DbORATrigger.metaClass, DbORATrigger.fBody, INSTRUCTIONS, INSTRUCTIONS);
        terminology.define(DbIBMTrigger.metaClass, DbIBMTrigger.fBody, INSTRUCTIONS, INSTRUCTIONS);
        terminology.define(DbINFTrigger.metaClass, DbINFTrigger.fBody, INSTRUCTIONS, INSTRUCTIONS);
        
        DbORTrigger.fOldAlias.setVisibleInScreen(false);
        DbGETrigger.fOldAlias.setVisibleInScreen(false);
        DbORATrigger.fOldAlias.setVisibleInScreen(false);
        DbIBMTrigger.fOldAlias.setVisibleInScreen(false);
        DbINFTrigger.fOldAlias.setVisibleInScreen(false);
        
        DbORTrigger.fNewAlias.setVisibleInScreen(false);
        DbGETrigger.fNewAlias.setVisibleInScreen(false);
        DbORATrigger.fNewAlias.setVisibleInScreen(false);
        DbIBMTrigger.fNewAlias.setVisibleInScreen(false);
        DbINFTrigger.fNewAlias.setVisibleInScreen(false);
           
        //primary/unique key
        
	    //rename physical name, description and hide UML stereotype
	    defineSemanticalObjectMetaFields(terminology, DbORPrimaryUnique.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbGEPrimaryUnique.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbORAPrimaryUnique.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbIBMPrimaryUnique.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbINFPrimaryUnique.metaClass); 
	            
        //foreign key
	    
	    //rename physical name, description and hide UML stereotype
	    defineSemanticalObjectMetaFields(terminology, DbORForeign.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbGEForeign.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbORAForeign.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbIBMForeign.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbINFForeign.metaClass); 
        
        terminology.define(DbORForeign.metaClass,  DbORForeign.fAssociationEnd, DIRECTION, DIRECTION);
        terminology.define(DbGEForeign.metaClass,  DbGEForeign.fAssociationEnd, DIRECTION, DIRECTION);
        terminology.define(DbORAForeign.metaClass, DbORAForeign.fAssociationEnd, DIRECTION, DIRECTION);
        terminology.define(DbIBMForeign.metaClass, DbIBMForeign.fAssociationEnd, DIRECTION, DIRECTION);
        terminology.define(DbINFForeign.metaClass, DbINFForeign.fAssociationEnd, DIRECTION, DIRECTION);
        
        //domain
        defineSemanticalObjectMetaFields(terminology, DbORDomainModel.metaClass); 
        defineSemanticalObjectMetaFields(terminology, DbORDomain.metaClass); 

        terminology.define(DbORDomain.metaClass,  DbORDomain.fSourceType, LOGICAL_DOMAIN, LOGICAL_DOMAIN);
        terminology.define(DbORDomain.metaClass,  DbORDomain.fUser, MANAGER, MANAGER);
	    terminology.define(DbORDomain.metaClass,  DbORDomain.fNull, NULL_VALUE_POSSIBLE, NULL_VALUE_POSSIBLE);
	    
	    DbORDomain.fCategory.setVisibleInScreen(false); 
	    DbORDomain.fOrderedCollection.setVisibleInScreen(false);
	    DbORDomain.fSuperDomain.setVisibleInScreen(false);
	    DbORDomain.fOoSourceType.setVisibleInScreen(false);
	    DbORDomain.fPropagated.setVisibleInScreen(false);
	    DbORDomain.fFields.setVisibleInScreen(false);
	    
        //common item
	    defineSemanticalObjectMetaFields(terminology, DbORCommonItemModel.metaClass); 
	    defineSemanticalObjectMetaFields(terminology, DbORCommonItem.metaClass); 
        terminology.define(DbORCommonItem.metaClass,  DbORCommonItem.fType, DOMAIN_DATA_TYPE, DOMAIN_DATA_TYPE);
        
        DbORCommonItem.fVisibility.setVisibleInScreen(false);
        DbORCommonItem.fOoType.setVisibleInScreen(false);
        DbORCommonItem.fTypeUse.setVisibleInScreen(false);
        DbORCommonItem.fTypeUseStyle.setVisibleInScreen(false);
        DbORCommonItem.fStatic.setVisibleInScreen(false);
        DbORCommonItem.fFinal.setVisibleInScreen(false);
        DbORCommonItem.fTransient.setVisibleInScreen(false);
        DbORCommonItem.fVolatile.setVisibleInScreen(false);
        
        //built-in type package
        defineSemanticalObjectMetaFields(terminology, DbSMSBuiltInTypePackage.metaClass); 
        terminology.define(DbSMSBuiltInTypePackage.metaClass, DbSMSBuiltInTypePackage.fAuthor, CREATOR, CREATOR);
        DbSMSBuiltInTypePackage.fIsValidated.setVisibleInScreen(false); 
        DbSMSBuiltInTypePackage.fBuiltIn.setVisibleInScreen(false); 
        
        //built-in type
        defineSemanticalObjectMetaFields(terminology, DbORBuiltInType.metaClass); 
        DbORBuiltInType.fBuiltIn.setVisibleInScreen(false);
        
        //user
        defineSemanticalObjectMetaFields(terminology, DbSMSUser.metaClass); 
	}

	private void defineSemanticalObjectMetaFields(Terminology terminology, MetaClass mc) {
		String physicalName = DbSMSSemanticalObject.fPhysicalName.getJName(); 
		String description = DbSMSSemanticalObject.fDescription.getJName(); 
		String umlStereotype = DbSMSSemanticalObject.fUmlStereotype.getJName(); 
		
		MetaField[] mfs = mc.getAllMetaFields(); 
		for (MetaField mf : mfs) {
			String jname = mf.getJName(); 
			
			if (physicalName.equals(jname)) {
				terminology.define(mc, mf, CODED_NAME, CODED_NAME);
			} else if (description.equals(jname)) {
				terminology.define(mc, mf, COMMENT, COMMENT);
			} else if (umlStereotype.equals(jname)) {
				mf.setVisibleInScreen(false);
			} //end if
		} //end for
	} //end defineGeneralMetaFields
	


}
