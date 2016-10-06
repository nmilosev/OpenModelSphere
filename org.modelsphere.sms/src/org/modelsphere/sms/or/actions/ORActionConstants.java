/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.or.actions;

import org.modelsphere.sms.actions.SMSActionsStore;

public interface ORActionConstants {
    ////////////////////////////////////////////////////////////////////////////
    // OR Keys, other keys are defined in AbstractActionsStore
    public static final String OR_GENERATE_CLASS_MODEL = "or Generate Class Model"; //NOT LOCALIZABLE, property key
    public static final String OR_GENERATE_COMMON_ITEMS = "or Generate Common Items"; //NOT LOCALIZABLE, property key
    public static final String OR_DELETE_FOREIGN_KEYS = "or Delete Foreign Keys"; //NOT LOCALIZABLE, property key
    public static final String OR_DELETE_ORPHAN_ITEMS = "or Delete Orphan Items"; //NOT LOCALIZABLE, property key
    public static final String OR_FORWARD_DATAMODEL = "or Forward DataModel"; //NOT LOCALIZABLE, property key
    public static final String OR_GENERATE_REFERENTIAL_RULES = "or Generate Action Rules"; //NOT LOCALIZABLE, property key
    public static final String OR_GENERATE_DDL = "or Generate DDL"; //NOT LOCALIZABLE, property key
    public static final String OR_GENERATE_FOREIGN_KEYS = "or Generate Foreign Keys"; //NOT LOCALIZABLE, property key
    public static final String OR_GENERATE_PHYSICAL_NAMES = "or Generate Physical Names"; //NOT LOCALIZABLE, property key
    public static final String OR_GENERATE_PRIMARY_KEYS = "or Generate Primaryn Keys"; //NOT LOCALIZABLE, property key
    public static final String OR_MERGE_COLUMN = "or Merge Column"; //NOT LOCALIZABLE, property key
    public static final String OR_NULL_MODIFIER = "or null modifier"; //NOT LOCALIZABLE, property key
    public static final String OR_PROPAG_ATTRS = "or Propag Attrs"; //NOT LOCALIZABLE, property key
    public static final String OR_PROPAGATE_COMMON_ITEM_VALUES = "or Propagate Common Item Values"; //NOT LOCALIZABLE, property key
    public static final String OR_SPLIT_COLUMN = "or Split Column"; //NOT LOCALIZABLE, property key
    public static final String OR_REVERSE_DB = "or Reverse DB"; //NOT LOCALIZABLE, property key
    public static final String OR_ADD_COMMMON_ITEM_MODEL = "or Add Common Item Model"; //NOT LOCALIZABLE, internal code
    public static final String OR_ADD_DOMAIN_MODEL = "or Add Domain Model"; //NOT LOCALIZABLE, internal code
    public static final String OR_SET_MULTIPLICITY = "or Set Multiplicity"; //NOT LOCALIZABLE, property key
    public static final String OR_SET_NAVIGABLE = "or Set Navigable"; //NOT LOCALIZABLE, property key
    public static final String OR_SET_REFERENCED_CONSTRAINT = "or Set Referenced Constraint"; //NOT LOCALIZABLE, property key
    public static final String OR_SET_TYPE = "or Set Type"; //NOT LOCALIZABLE, property key
    public static final String OR_SETUP_TARGET_SYSTEM = "or Setup Target System"; //NOT LOCALIZABLE, property key
    public static final String OR_SYNCHRO_DB = "or Synchro DB"; //NOT LOCALIZABLE, property key
    public static final String OR_CHANGE_TARGET_SYSTEM = "or Change Target System"; //NOT LOCALIZABLE, property key
    public static final String OR_CONVERT_DATAMODEL_WORKMODE = "or Convert DataModel WorkMode"; //NOT LOCALIZABLE, property key
    public static final String CREATE_COMMON_ITEM_COLUMNS = "or Add column(s) via common items"; //NOT LOCALIZABLE, property key

    // End Keys
    ///////////////////////////////////////////////////////////////////////////

}
