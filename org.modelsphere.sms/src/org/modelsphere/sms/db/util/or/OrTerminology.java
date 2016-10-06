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
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORChoiceOrSpecialization;
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
import org.modelsphere.sms.or.informix.db.DbINFFragment;
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

//Defines Object-Relational Terminology
public class OrTerminology extends TerminologyInitializer {

	@Override
	public void defineTerminology(Terminology terminology) {
        // //
        // In the OR mode, there is a special case or workaround when
        // defining a term for a metafield
        /*
         * GROUP names must be defined for metafields for which a "single" name is provided, but
         * they must contain the "not grouped" name. This necessity comes from the fact that
         * NULL group will revert to the "GUIName" of the object. This is highly indesirable, as
         * it would mean we cannot define alternate metafield names unless we are in the ER
         * mode. We therefore provide for the name in the GROUP parameter as well.
         */

        terminology.define(DbORDataModel.metaClass, OR_MODEL_TXT);
        terminology.define(DbGEDataModel.metaClass, OR_MODEL_TXT);
        terminology.define(DbORADataModel.metaClass, OR_MODEL_TXT);
        terminology.define(DbIBMDataModel.metaClass, OR_MODEL_TXT);
        terminology.define(DbINFDataModel.metaClass, OR_MODEL_TXT);
        
        terminology.define(DbORPrimaryUnique.metaClass, PRIMARY_KEY);
        terminology.define(DbORAPrimaryUnique.metaClass, PRIMARY_KEY);
        terminology.define(DbINFPrimaryUnique.metaClass, PRIMARY_KEY);
        terminology.define(DbIBMPrimaryUnique.metaClass, PRIMARY_KEY);
        terminology.define(DbGEPrimaryUnique.metaClass, PRIMARY_KEY);

        terminology.define(DbORAssociation.metaClass, DbORAssociation.fSuperCopy,
                SUPERASSOCIATION);
        
        terminology.define(DbORTable.metaClass, DbORTable.fSuperCopy, SUPERTABLE, SUPERTABLE);
        terminology.define(DbGETable.metaClass, DbGETable.fSuperCopy, SUPERTABLE, SUPERTABLE);
        terminology.define(DbORATable.metaClass, DbORATable.fSuperCopy, SUPERTABLE, SUPERTABLE);
        terminology.define(DbINFTable.metaClass, DbINFTable.fSuperCopy, SUPERTABLE, SUPERTABLE);
        terminology.define(DbIBMTable.metaClass, DbIBMTable.fSuperCopy, SUPERTABLE, SUPERTABLE);
                
        terminology.define(DbORTable.metaClass, DbORTable.fSubCopies, LocaleMgr.term
                .getString("Subtable"), LocaleMgr.term.getString("Subtables"));
        terminology.define(DbORAssociation.metaClass, DbORAssociation.fSubCopies,
                LocaleMgr.term.getString("Subassociation"), LocaleMgr.term
                        .getString("Subassociations"));
        terminology.define(DbORPrimaryUnique.metaClass, DbORPrimaryUnique.fSubCopies,
                LocaleMgr.term.getString("Subkey"), LocaleMgr.term.getString("Subkeys"));
        terminology.define(DbORForeign.metaClass, DbORForeign.fSubCopies, LocaleMgr.term
                .getString("Subkey"), LocaleMgr.term.getString("Subkeys"));

        terminology.define(DbORColumn.metaClass, DbORColumn.fSubCopies, SUBCOLUMN, SUBCOLUMNS);
        terminology
                .define(DbORAColumn.metaClass, DbORAColumn.fSubCopies, SUBCOLUMN, SUBCOLUMNS);
        terminology
                .define(DbINFColumn.metaClass, DbINFColumn.fSubCopies, SUBCOLUMN, SUBCOLUMNS);
        terminology
                .define(DbIBMColumn.metaClass, DbINFColumn.fSubCopies, SUBCOLUMN, SUBCOLUMNS);

        terminology.define(DbORIndex.metaClass, DbORIndex.fSubCopies, SUBINDEXES, SUBINDEXES);
        terminology.define(DbGEIndex.metaClass, DbGEIndex.fSubCopies, SUBINDEXES, SUBINDEXES);
        terminology.define(DbORAIndex.metaClass, DbORAIndex.fSubCopies, SUBINDEXES, SUBINDEXES);
        terminology.define(DbINFIndex.metaClass, DbINFIndex.fSubCopies, SUBINDEXES, SUBINDEXES);
        terminology.define(DbIBMIndex.metaClass, DbIBMIndex.fSubCopies, SUBINDEXES, SUBINDEXES);
        
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fSubCopies, SUBTRIGGER,
                SUBTRIGGERS);
        terminology.define(DbORATrigger.metaClass, DbORATrigger.fSubCopies, SUBTRIGGER,
                SUBTRIGGERS);
        terminology.define(DbINFTrigger.metaClass, DbINFTrigger.fSubCopies, SUBTRIGGER,
                SUBTRIGGERS);
        terminology.define(DbIBMTrigger.metaClass, DbINFTrigger.fSubCopies, SUBTRIGGER,
                SUBTRIGGERS);

        terminology.define(DbORCheck.metaClass, DbORCheck.fSubCopies, SUBCHECK, SUBCHECKS);
        terminology.define(DbGECheck.metaClass, DbGECheck.fSubCopies, SUBCHECK, SUBCHECKS);
        terminology.define(DbORACheck.metaClass, DbORACheck.fSubCopies, SUBCHECK, SUBCHECKS);
        terminology.define(DbIBMCheck.metaClass, DbIBMCheck.fSubCopies, SUBCHECK, SUBCHECKS);
        terminology.define(DbINFCheck.metaClass, DbINFCheck.fSubCopies, SUBCHECK, SUBCHECKS);

        terminology.define(DbORAssociation.metaClass, DbORAssociation.fComponents, ROLE, ROLES);
        terminology.define(DbORAssociationEnd.metaClass, DbORAssociation.fComponents, ROLE,
                ROLES);
        
        terminology.define(DbORForeign.metaClass, DbORForeign.fComponents, FOREIGN_KEYS,
                FOREIGN_KEYS);
        terminology.define(DbORFKeyColumn.metaClass, DbORFKeyColumn.fComponents, COLUMNS,
                COLUMNS);

        terminology.define(DbGEForeign.metaClass, DbGEForeign.fComponents, FOREIGN_KEYS,
                FOREIGN_KEYS);
        terminology.define(DbORAForeign.metaClass, DbORAForeign.fComponents, FOREIGN_KEYS,
                FOREIGN_KEYS);
        terminology.define(DbINFForeign.metaClass, DbINFForeign.fComponents, FOREIGN_KEYS,
                FOREIGN_KEYS);
        terminology.define(DbINFFragment.metaClass, DbINFIndex.fComponents, LocaleMgr.term
                .getString("FragmentList"), LocaleMgr.term.getString("FragmentList"));
        terminology.define(DbIBMForeign.metaClass, DbIBMForeign.fComponents, FOREIGN_KEYS,
                FOREIGN_KEYS);
        terminology.define(DbORIndexKey.metaClass, DbORIndexKey.fComponents, LocaleMgr.term
                .getString("IndexedElements"), LocaleMgr.term.getString("IndexedElements"));
        
     // //////////////////////////////////////////////////////////////////////////////////////////////
        // OR Meta classes

        // meta classes

        terminology.define(DbORPrimaryUnique.metaClass, PRIMARY_KEY, PRIMARY_UNIQUE_KEYS);
        terminology.define(DbGEPrimaryUnique.metaClass, PRIMARY_KEY, PRIMARY_UNIQUE_KEYS);
        terminology.define(DbORAPrimaryUnique.metaClass, PRIMARY_KEY, PRIMARY_UNIQUE_KEYS);
        terminology.define(DbINFPrimaryUnique.metaClass, PRIMARY_KEY, PRIMARY_UNIQUE_KEYS);
        terminology.define(DbIBMPrimaryUnique.metaClass, PRIMARY_KEY, PRIMARY_UNIQUE_KEYS);

        // meta fields

        // - column concept

        terminology.define(DbORColumn.metaClass, DbORColumn.fSuperCopy, SUPERCOLUMN,
                SUPERCOLUMN);
        terminology.define(DbORColumn.metaClass, DbORPrimaryUnique.fColumns, COLUMNS, COLUMNS);

        // - association concept

        terminology.define(DbORAssociation.metaClass, DbORAssociation.fSuperCopy,
                LocaleMgr.term.getString("Superassociation"), LocaleMgr.term
                        .getString("Superassociation"));

        // - role concept

        terminology.define(DbORView.metaClass, DbORView.fSuperCopy, SUPERVIEW, SUPERVIEW);

        // - primary or unique concept

        terminology.define(DbORPrimaryUnique.metaClass, DbORPrimaryUnique.fSuperCopy, SUPERKEY,
                SUPERKEY);

        // - foreign key concept

        terminology.define(DbORForeign.metaClass, DbORForeign.fSuperCopy, SUPERKEY, SUPERKEY);

        // - check concept

        terminology.define(DbORConstraint.metaClass, DbORConstraint.fSuperCopy, LocaleMgr.term
                .getString("Superconstraint"), LocaleMgr.term.getString("Superconstraint"));
        terminology.define(DbORCheck.metaClass, DbORCheck.fSuperCopy, SUPERCHECK, SUPERCHECK);

        // - Index concept
        terminology.define(DbORIndex.metaClass, DbORIndex.fSuperCopy, SUPERINDEX, SUPERINDEX);

        // - trigger concept
        terminology.define(DbORTrigger.metaClass, DbORTrigger.fSuperCopy, SUPERTRIGGER,
                SUPERTRIGGER);

        // - trigger concept
        terminology.define(DbORChoiceOrSpecialization.metaClass,
                DbORChoiceOrSpecialization.fSuperCopy, SUPER_CHOICE_SPEC, SUPER_CHOICE_SPEC);

        // //////////////////////////////////////////////////////////////////////////////////////////////
        // GE Meta classes

        // GE Metafields

        terminology.define(DbGEColumn.metaClass, DbGEColumn.fSuperCopy, SUPERCOLUMN,
                SUPERCOLUMN);
        terminology.define(DbGEView.metaClass, DbGEView.fSuperCopy, SUPERVIEW, SUPERVIEW);
        terminology.define(DbGEPrimaryUnique.metaClass, DbGEPrimaryUnique.fSuperCopy, SUPERKEY,
                SUPERKEY);
        terminology.define(DbGEForeign.metaClass, DbGEForeign.fSuperCopy, SUPERKEY, SUPERKEY);
        terminology.define(DbGECheck.metaClass, DbGECheck.fSuperCopy, SUPERCHECK, SUPERCHECK);
        terminology.define(DbGEIndex.metaClass, DbGEIndex.fSuperCopy, SUPERINDEX, SUPERINDEX);
        terminology.define(DbGETrigger.metaClass, DbGETrigger.fSuperCopy, SUPERTRIGGER,
                SUPERTRIGGER);

        // //////////////////////////////////////////////////////////////////////////////////////////////
        // ORA Meta fields

        terminology.define(DbORAColumn.metaClass, DbORAColumn.fSuperCopy, SUPERCOLUMN,
                SUPERCOLUMN);
        terminology.define(DbORAView.metaClass, DbORAView.fSuperCopy, SUPERVIEW, SUPERVIEW);
        terminology.define(DbORAPrimaryUnique.metaClass, DbORAPrimaryUnique.fSuperCopy,
                SUPERKEY, SUPERKEY);
        terminology.define(DbORAForeign.metaClass, DbORAForeign.fSuperCopy, SUPERKEY, SUPERKEY);
        terminology.define(DbORACheck.metaClass, DbORACheck.fSuperCopy, SUPERCHECK, SUPERCHECK);
        terminology.define(DbORAIndex.metaClass, DbORAIndex.fSuperCopy, SUPERINDEX, SUPERINDEX);
        terminology.define(DbORATrigger.metaClass, DbORATrigger.fSuperCopy, SUPERTRIGGER,
                SUPERTRIGGER);

        // //////////////////////////////////////////////////////////////////////////////////////////////
        // INF Meta fields

        terminology.define(DbINFColumn.metaClass, DbINFColumn.fSuperCopy, SUPERCOLUMN,
                SUPERCOLUMN);
        terminology.define(DbINFView.metaClass, DbINFView.fSuperCopy, SUPERVIEW, SUPERVIEW);
        terminology.define(DbINFPrimaryUnique.metaClass, DbINFPrimaryUnique.fSuperCopy,
                SUPERKEY, SUPERKEY);
        terminology.define(DbINFForeign.metaClass, DbINFForeign.fSuperCopy, SUPERKEY, SUPERKEY);
        terminology.define(DbINFCheck.metaClass, DbINFCheck.fSuperCopy, SUPERCHECK, SUPERCHECK);
        terminology.define(DbINFIndex.metaClass, DbINFIndex.fSuperCopy, SUPERINDEX, SUPERINDEX);
        terminology.define(DbINFTrigger.metaClass, DbINFTrigger.fSuperCopy, SUPERTRIGGER,
                SUPERTRIGGER);

        // ////////////////////////////////////////////////////////////////////////////////////////////////
        // IBM Meta fields

        terminology.define(DbIBMColumn.metaClass, DbIBMColumn.fSuperCopy, SUPERCOLUMN,
                SUPERCOLUMN);
        terminology.define(DbIBMView.metaClass, DbIBMView.fSuperCopy, SUPERVIEW, SUPERVIEW);
        terminology.define(DbIBMPrimaryUnique.metaClass, DbIBMPrimaryUnique.fSuperCopy,
                SUPERKEY, SUPERKEY);
        terminology.define(DbIBMForeign.metaClass, DbIBMForeign.fSuperCopy, SUPERKEY, SUPERKEY);
        terminology.define(DbIBMCheck.metaClass, DbIBMCheck.fSuperCopy, SUPERCHECK, SUPERCHECK);
        terminology.define(DbIBMIndex.metaClass, DbIBMIndex.fSuperCopy, SUPERINDEX, SUPERINDEX);
        terminology.define(DbIBMTrigger.metaClass, DbIBMTrigger.fSuperCopy, SUPERTRIGGER,
                SUPERTRIGGER);
	}

}
