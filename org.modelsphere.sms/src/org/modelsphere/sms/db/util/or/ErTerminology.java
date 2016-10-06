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

import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.sms.db.util.TerminologyInitializer;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOClass;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORConstraint;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORIndexKey;
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

//Defines Entity-Relationship Terminology
public class ErTerminology extends TerminologyInitializer {

	@Override
	public void defineTerminology(Terminology terminology) {
		//
		// define terms for meta classes
		//
        terminology.define(DbORColumn.metaClass, ATTRIBUTE_TXT, ATTRIBUTE_GROUP_TXT);
        terminology.define(DbORTable.metaClass, ENTITY_TXT, ENTITY_GROUP_TXT);
        terminology.define(DbORAbsTable.metaClass, ENTITY_TXT, ENTITY_GROUP_TXT);
        terminology.define(DbORAssociation.metaClass, ARCS_TXT, ARCS_GROUP_TXT);
        terminology.define(DbOOClass.metaClass, RELATIONSHIP_TXT, RELATIONSHIP_GROUP_TXT);

        terminology.define(DbORDataModel.metaClass, ER_MODEL_TXT, LocaleMgr.term
                .getString("ConceptualDataModels"));
        terminology.define(DbORView.metaClass, LocaleMgr.term.getString("View"), LocaleMgr.term
                .getString("Views"));

        terminology.define(DbORPrimaryUnique.metaClass, PRIMARY_KEY, PRIMARY_UNIQUE_KEYS);
        terminology.define(DbORForeign.metaClass, FOREIGN_KEY, FOREIGN_KEYS);
        terminology.define(DbORCheck.metaClass, LocaleMgr.term.getString("Constraint"),
                LocaleMgr.term.getString("Constraints"));
        terminology.define(DbORConstraint.metaClass, LocaleMgr.term.getString("Constraint"),
                LocaleMgr.term.getString("Constraints"));
        terminology.define(DbORIndex.metaClass, LocaleMgr.term.getString("Index"),
                LocaleMgr.term.getString("Indexes"));
        terminology.define(DbORIndexKey.metaClass, LocaleMgr.term.getString("IndexedElement"),
                LocaleMgr.term.getString("IndexedElements"));
        terminology.define(DbORTrigger.metaClass, LocaleMgr.term.getString("Trigger"),
                LocaleMgr.term.getString("Triggers"));
        terminology.define(DbORFKeyColumn.metaClass, ATTRIBUTE, ATTRIBUTES);
        terminology.define(DbORAssociationEnd.metaClass, ROLE, ROLES);
        
		//
		// define terms for meta fields
		//
        
        // - data model concept
        terminology.define(DbORDataModel.metaClass, DbORDataModel.fDeploymentDatabase, NULL,
                NULL);

        // - table concept
        terminology.define(DbORTable.metaClass, DbORTable.fSuperCopy, SUPERENTITY, SUPERENTITY);
        terminology.define(DbORTable.metaClass, DbORTable.fSelectionRule, NULL, NULL);
        terminology.define(DbORTable.metaClass, DbORTable.fSubCopies, SUBENTITIES, SUBENTITIES);

        terminology.define(DbOOClass.metaClass, DbOOClass.fSuperCopy, LocaleMgr.term
                .getString("Superrelationship"), LocaleMgr.term.getString("Superrelationship"));
        terminology.define(DbOOClass.metaClass, DbOOClass.fSubCopies, LocaleMgr.term
                .getString("Subrelationship"), LocaleMgr.term.getString("Subrelationships"));

        // - column concept
        terminology.define(DbORColumn.metaClass, DbORColumn.fSuperCopy, SUPERATTRIBUTE,
                LocaleMgr.term.getString("Superattribute"));
        terminology.define(DbORColumn.metaClass, DbORColumn.fSubCopies, LocaleMgr.term
                .getString("Subattribute"), LocaleMgr.term.getString("Subattributes"));
        terminology.define(DbORColumn.metaClass, DbORColumn.fForeign, NULL, NULL);
        terminology.define(DbORColumn.metaClass, DbORPrimaryUnique.fColumns, ATTRIBUTE,
                ATTRIBUTES);
        
        // - association concept
        terminology.define(DbORAssociation.metaClass, DbORAssociation.fSuperCopy, SUPERARC,
                LocaleMgr.term.getString("Superarc"));
        terminology.define(DbORAssociation.metaClass, DbORAssociation.fSubCopies,
                LocaleMgr.term.getString("Subarc"), LocaleMgr.term.getString("Subarcs"));
        terminology.define(DbORAssociation.metaClass, DbORAssociation.fComponents, ROLES,
                "Roles");
        
        // - role concept
        terminology.define(DbORAssociationEnd.metaClass, DbORAssociationEnd.fInsertRule, NULL,
                NULL);
        terminology.define(DbORAssociationEnd.metaClass, DbORAssociationEnd.fUpdateRule, NULL,
                NULL);
        terminology.define(DbORAssociationEnd.metaClass, DbORAssociationEnd.fDeleteRule, NULL,
                NULL);
        terminology.define(DbORAssociationEnd.metaClass, DbORAssociationEnd.fConstraintType,
                NULL, NULL);
        terminology.define(DbORAssociationEnd.metaClass,
                DbORAssociationEnd.fReferencedConstraint, NULL, NULL);
        terminology.define(DbORAssociationEnd.metaClass, DbORAssociation.fComponents, ROLES,
                ROLES);
        
        // - view concept
        terminology.define(DbORView.metaClass, DbORView.fAdminAccessList, NULL, NULL);
        terminology.define(DbORView.metaClass, DbORView.fAlias, NULL, NULL);
        terminology.define(DbORView.metaClass, DbORView.fCreationTime, NULL, NULL);
        terminology.define(DbORView.metaClass, DbORView.fDescription, NULL, NULL);
        terminology.define(DbORView.metaClass, DbORView.fModificationTime, NULL, NULL);
        terminology.define(DbORView.metaClass, DbORView.fName, NULL, NULL);
        terminology.define(DbORView.metaClass, DbORView.fSelectionRule, NULL, NULL);
        terminology.define(DbORView.metaClass, DbORView.fWriteAccessList, NULL, NULL);
        terminology.define(DbORView.metaClass, DbORView.fSuperCopy, NULL, NULL);
        terminology.define(DbORView.metaClass, DbORView.fUser, NULL, NULL);
        terminology.define(DbORView.metaClass, DbORView.fUmlStereotype, NULL, NULL);

        // - primary or unique concept
        terminology.define(DbORPrimaryUnique.metaClass, DbORPrimaryUnique.fPhysicalName, NULL,
                NULL);
        terminology.define(DbORPrimaryUnique.metaClass, DbORPrimaryUnique.fSuperCopy, NULL,
                NULL);
        terminology.define(DbORPrimaryUnique.metaClass, DbORPrimaryUnique.fIndex, NULL, NULL);
        terminology.define(DbORPrimaryUnique.metaClass, DbORPrimaryUnique.fColumns, ATTRIBUTES,
                "Attributes");
        terminology.define(DbORPrimaryUnique.metaClass, DbORPrimaryUnique.fSubCopies,
                LocaleMgr.term.getString("Subkey"), LocaleMgr.term.getString("Subkeys"));
        
        // - foreign key concept
        terminology.define(DbORForeign.metaClass, DbORForeign.fAlias, NULL, NULL);
        terminology.define(DbORForeign.metaClass, DbORForeign.fCreationTime, NULL, NULL);
        terminology.define(DbORForeign.metaClass, DbORForeign.fDescription, NULL, NULL);
        terminology.define(DbORForeign.metaClass, DbORForeign.fModificationTime, NULL, NULL);
        terminology.define(DbORForeign.metaClass, DbORForeign.fName, NULL, NULL);
        terminology.define(DbORForeign.metaClass, DbORForeign.fPhysicalName, NULL, NULL);
        terminology.define(DbORForeign.metaClass, DbORForeign.fWriteAccessList, NULL, NULL);
        terminology.define(DbORForeign.metaClass, DbORForeign.fSuperCopy, NULL, NULL);
        terminology.define(DbORForeign.metaClass, DbORForeign.fUmlStereotype, NULL, NULL);
        terminology.define(DbORForeign.metaClass, DbORForeign.fIndex, NULL, NULL);
        terminology.define(DbORForeign.metaClass, DbORForeign.fAssociationEnd, LocaleMgr.term
                .getString("ArcRole"), NULL);
        terminology.define(DbORForeign.metaClass, DbORForeign.fSubCopies, LocaleMgr.term
                .getString("Subkey"), LocaleMgr.term.getString("Subkeys"));
        terminology.define(DbORForeign.metaClass, DbORForeign.fComponents, FOREIGN_KEYS,
                "Foreign Keys");

        terminology.define(DbORFKeyColumn.metaClass, DbORFKeyColumn.fComponents, ATTRIBUTES,
                ATTRIBUTES);
        terminology.define(DbORFKeyColumn.metaClass, DbORFKeyColumn.fColumn, ATTRIBUTE,
                ATTRIBUTE);
        terminology.define(DbORFKeyColumn.metaClass, DbORFKeyColumn.fSourceColumn,
                LocaleMgr.term.getString("SourceAttribute"), LocaleMgr.term
                        .getString("SourceAttribute"));
        
        // - check concept
        terminology.define(DbORCheck.metaClass, DbORCheck.fColumn, ATTRIBUTE, NULL);
        terminology.define(DbORCheck.metaClass, DbORCheck.fSuperCopy, LocaleMgr.term
                .getString("Superconstraint"), LocaleMgr.term.getString("Superconstraint"));
        terminology.define(DbORCheck.metaClass, DbORCheck.fSubCopies, LocaleMgr.term
                .getString("Subconstraints"), LocaleMgr.term.getString("Subconstraints"));
        
        // - Index concept
        terminology.define(DbORIndex.metaClass, DbORIndex.fAlias, NULL, NULL);
        terminology.define(DbORIndex.metaClass, DbORIndex.fCreationTime, NULL, NULL);
        terminology.define(DbORIndex.metaClass, DbORIndex.fDescription, NULL, NULL);
        terminology.define(DbORIndex.metaClass, DbORIndex.fModificationTime, NULL, NULL);
        terminology.define(DbORIndex.metaClass, DbORIndex.fName, NULL, NULL);
        terminology.define(DbORIndex.metaClass, DbORIndex.fPhysicalName, NULL, NULL);
        terminology.define(DbORIndex.metaClass, DbORIndex.fSuperCopy, SUPERINDEX, NULL);
        terminology.define(DbORIndex.metaClass, DbORIndex.fSubCopies, SUBINDEXES, SUBINDEXES);
        terminology.define(DbORIndex.metaClass, DbORIndex.fWriteAccessList, NULL, NULL);
        terminology.define(DbORIndex.metaClass, DbORIndex.fUser, NULL, NULL);
        terminology.define(DbORIndex.metaClass, DbORIndex.fUmlStereotype, NULL, NULL);
        terminology.define(DbORIndex.metaClass, DbORIndex.fUnique, NULL, NULL);
        terminology.define(DbORIndex.metaClass, DbORIndex.fConstraint, NULL, NULL);
        terminology.define(DbORIndex.metaClass, DbORIndex.fPrimary, NULL, NULL);

        terminology.define(DbORIndexKey.metaClass, DbORIndexKey.fCreationTime, NULL, NULL);
        terminology.define(DbORIndexKey.metaClass, DbORIndexKey.fModificationTime, NULL, NULL);

        terminology.define(DbORIndexKey.metaClass, DbORIndexKey.fComponents, LocaleMgr.term
                .getString("IndexedElements"), LocaleMgr.term.getString("IndexedElements"));
        terminology.define(DbORIndexKey.metaClass, DbORIndexKey.fExpression, NULL, NULL);
        terminology.define(DbORIndexKey.metaClass, DbORIndexKey.fSortOption, NULL, NULL);
        terminology.define(DbORIndexKey.metaClass, DbORIndexKey.fIndexedElement, NULL, NULL);
        
        // - trigger concept
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fAlias, NULL, NULL);
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fCreationTime, NULL, NULL);
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fDescription, NULL, NULL);
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fModificationTime, NULL, NULL);
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fName, NULL, NULL);
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fPhysicalName, NULL, NULL);
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fWriteAccessList, NULL, NULL);
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fSuperCopy, SUPERTRIGGER, NULL);
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fSubCopies, SUBTRIGGERS,
                SUBTRIGGERS);
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fUser, NULL, NULL);
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fEvent, NULL, NULL);
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fOldAlias, NULL, NULL);
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fNewAlias, NULL, NULL);
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fWhenCondition, NULL, NULL);
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fBody, NULL, NULL);
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fUmlStereotype, NULL, NULL);
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fUser, NULL, NULL);
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fColumns, ATTRIBUTES, ATTRIBUTES);
        
        // //////////////////////////////////////////////////////////////////////////////////////////////
        // GE Meta classes

        // meta classes

        terminology.define(DbGEColumn.metaClass, ATTRIBUTE_TXT, ATTRIBUTE_GROUP_TXT);
        terminology.define(DbGETable.metaClass, ENTITY_TXT, ENTITY_GROUP_TXT);
        terminology.define(DbGEDataModel.metaClass, ER_MODEL_TXT, LocaleMgr.term
                .getString("ConceptualDataModels"));
        terminology.define(DbGEView.metaClass, LocaleMgr.term.getString("View"), LocaleMgr.term
                .getString("Views"));
        terminology.define(DbGEPrimaryUnique.metaClass, PRIMARY_KEY, PRIMARY_UNIQUE_KEYS);
        terminology.define(DbGEForeign.metaClass, FOREIGN_KEY, FOREIGN_KEYS);
        terminology.define(DbGECheck.metaClass, LocaleMgr.term.getString("Constraint"),
                LocaleMgr.term.getString("Constraints"));
        terminology.define(DbGEIndex.metaClass, LocaleMgr.term.getString("Index"),
                LocaleMgr.term.getString("Indexes"));
        terminology.define(DbGETrigger.metaClass, LocaleMgr.term.getString("Trigger"),
                LocaleMgr.term.getString("Triggers"));

        // meta fields

        // - table concept

        terminology.define(DbGETable.metaClass, DbGETable.fSuperCopy, SUPERENTITY, SUPERENTITY);
        terminology.define(DbGETable.metaClass, DbGETable.fSelectionRule, NULL, NULL);

        // - column concept

        terminology.define(DbGEColumn.metaClass, DbGEColumn.fSuperCopy, SUPERATTRIBUTE,
                LocaleMgr.term.getString("Superattribute"));
        terminology.define(DbGEColumn.metaClass, DbGEColumn.fSubCopies, LocaleMgr.term
                .getString("Subattribute"), LocaleMgr.term.getString("Subattributes"));
        terminology.define(DbGEColumn.metaClass, DbGEColumn.fForeign, NULL, NULL);

        // - association concept

        // - role concept

        terminology.define(DbGEDataModel.metaClass, DbGEDataModel.fDeploymentDatabase, NULL,
                NULL);

        terminology.define(DbGEView.metaClass, DbGEView.fAdminAccessList, NULL, NULL);
        terminology.define(DbGEView.metaClass, DbGEView.fAlias, NULL, NULL);
        terminology.define(DbGEView.metaClass, DbGEView.fCreationTime, NULL, NULL);
        terminology.define(DbGEView.metaClass, DbGEView.fDescription, NULL, NULL);
        terminology.define(DbGEView.metaClass, DbGEView.fModificationTime, NULL, NULL);
        terminology.define(DbGEView.metaClass, DbGEView.fName, NULL, NULL);
        terminology.define(DbGEView.metaClass, DbGEView.fPhysicalName, NULL, NULL);
        terminology.define(DbGEView.metaClass, DbGEView.fSelectionRule, NULL, NULL);
        terminology.define(DbGEView.metaClass, DbGEView.fWriteAccessList, NULL, NULL);
        terminology.define(DbGEView.metaClass, DbGEView.fSuperCopy, SUPERVIEW, NULL);
        terminology.define(DbGEView.metaClass, DbGEView.fUser, NULL, NULL);
        terminology.define(DbGEView.metaClass, DbGEView.fUmlStereotype, NULL, NULL);

        // - primary or unique concept

        terminology.define(DbGEPrimaryUnique.metaClass, DbGEPrimaryUnique.fSuperCopy, SUPERKEY,
                SUPERKEY);
        terminology.define(DbGEPrimaryUnique.metaClass, DbGEPrimaryUnique.fSubCopies,
                LocaleMgr.term.getString("Subkeys"), LocaleMgr.term.getString("Subkeys"));
        terminology.define(DbGEPrimaryUnique.metaClass, DbGEPrimaryUnique.fIndex, NULL, NULL);

        // - foreign key concept
        terminology.define(DbGEForeign.metaClass, DbGEForeign.fAlias, NULL, NULL);
        terminology.define(DbGEForeign.metaClass, DbGEForeign.fCreationTime, NULL, NULL);
        terminology.define(DbGEForeign.metaClass, DbGEForeign.fDescription, NULL, NULL);
        terminology.define(DbGEForeign.metaClass, DbGEForeign.fModificationTime, NULL, NULL);
        terminology.define(DbGEForeign.metaClass, DbGEForeign.fName, NULL, NULL);
        terminology.define(DbGEForeign.metaClass, DbGEForeign.fPhysicalName, NULL, NULL);
        terminology.define(DbGEForeign.metaClass, DbGEForeign.fWriteAccessList, NULL, NULL);
        terminology.define(DbGEForeign.metaClass, DbGEForeign.fSuperCopy, SUPERKEY, NULL);
        terminology.define(DbGEForeign.metaClass, DbGEForeign.fSubCopies, LocaleMgr.term
                .getString("Subkeys"), LocaleMgr.term.getString("Subkeys"));
        terminology.define(DbGEForeign.metaClass, DbGEForeign.fUmlStereotype, NULL, NULL);
        terminology.define(DbGEForeign.metaClass, DbGEForeign.fIndex, NULL, NULL);
        terminology.define(DbGEForeign.metaClass, DbGEForeign.fAssociationEnd, LocaleMgr.term
                .getString("ArcRole"), NULL);
        terminology.define(DbGEForeign.metaClass, DbGEForeign.fComponents, FOREIGN_KEYS,
                FOREIGN_KEYS);

        // - check concept
        terminology.define(DbGECheck.metaClass, DbGECheck.fSuperCopy, LocaleMgr.term
                .getString("Superconstraint"), LocaleMgr.term.getString("Superconstraint"));
        terminology.define(DbGECheck.metaClass, DbGECheck.fColumn, ATTRIBUTE, NULL);
        terminology.define(DbGECheck.metaClass, DbGECheck.fSubCopies, LocaleMgr.term
                .getString("Subconstraints"), LocaleMgr.term.getString("Subconstraints"));

        // - Index concept
        terminology.define(DbGEIndex.metaClass, DbGEIndex.fAlias, NULL, NULL);
        terminology.define(DbGEIndex.metaClass, DbGEIndex.fCreationTime, NULL, NULL);
        terminology.define(DbGEIndex.metaClass, DbGEIndex.fDescription, NULL, NULL);
        terminology.define(DbGEIndex.metaClass, DbGEIndex.fModificationTime, NULL, NULL);
        terminology.define(DbGEIndex.metaClass, DbGEIndex.fName, NULL, NULL);
        terminology.define(DbGEIndex.metaClass, DbGEIndex.fPhysicalName, NULL, NULL);
        terminology.define(DbGEIndex.metaClass, DbGEIndex.fSuperCopy, SUPERINDEX, NULL);
        terminology.define(DbGEIndex.metaClass, DbGEIndex.fSubCopies, SUBINDEXES, SUBINDEXES);
        terminology.define(DbGEIndex.metaClass, DbGEIndex.fWriteAccessList, NULL, NULL);
        terminology.define(DbGEIndex.metaClass, DbGEIndex.fUser, NULL, NULL);
        terminology.define(DbGEIndex.metaClass, DbGEIndex.fUmlStereotype, NULL, NULL);
        terminology.define(DbGEIndex.metaClass, DbGEIndex.fUnique, NULL, NULL);
        terminology.define(DbGEIndex.metaClass, DbGEIndex.fConstraint, NULL, NULL);
        terminology.define(DbGEIndex.metaClass, DbGEIndex.fPrimary, NULL, NULL);

        // - trigger concept
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fAlias, NULL, NULL);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fCreationTime, NULL, NULL);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fDescription, NULL, NULL);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fModificationTime, NULL, NULL);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fName, NULL, NULL);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fPhysicalName, NULL, NULL);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fWriteAccessList, NULL, NULL);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fSuperCopy, SUPERTRIGGER, NULL);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fSubCopies, SUBTRIGGERS,
                SUBTRIGGERS);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fUser, NULL, NULL);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fEvent, NULL, NULL);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fOldAlias, NULL, NULL);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fNewAlias, NULL, NULL);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fWhenCondition, NULL, NULL);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fBody, NULL, NULL);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fUmlStereotype, NULL, NULL);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fUser, NULL, NULL);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fColumns, ATTRIBUTES, ATTRIBUTES);
	} //end defineTerminology()

}
