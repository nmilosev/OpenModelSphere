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

package org.modelsphere.sms.or.popup;

import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.or.actions.ORActionConstants;

public final class ORPopupMenuPool {
    private ORPopupMenuPool() {
    }

    // abstract table popups
    public static final String[] orAbsTable = { SMSActionsStore.SEND_TO_DIAGRAM,
            SMSActionsStore.FIND_IN_DIAGRAM, ORActionConstants.OR_GENERATE_COMMON_ITEMS,
            ORActionConstants.OR_PROPAGATE_COMMON_ITEM_VALUES,

    };

    public static final String[] orAbsTableGo = { SMSActionsStore.LAYOUT_RIGHT_ANGLE,
            SMSActionsStore.SET_GO_STYLE, };
    // end of abstract table popups

    // table popups
    public static final String[] orTable = { ORActionConstants.OR_GENERATE_COMMON_ITEMS,
            ORActionConstants.OR_PROPAGATE_COMMON_ITEM_VALUES,

    };

    public static final String[] orTableGo = {};
    // end of table popups

    // view popups
    public static final String[] orView = {};

    public static final String[] orViewGo = {};
    // end of view popups

    public static final String[] orAttribute = { ORActionConstants.OR_SET_TYPE,
            SMSActionsStore.FIND_IN_DIAGRAM, };

    public static final String[] orColumn = { ORActionConstants.OR_GENERATE_COMMON_ITEMS,
            ORActionConstants.OR_PROPAGATE_COMMON_ITEM_VALUES, ORActionConstants.OR_MERGE_COLUMN,
            ORActionConstants.OR_SPLIT_COLUMN, SMSActionsStore.CREATE_LINK };

    public static final String[] orPrimaryUnique = {};

    public static final String[] orForeign = {};

    public static final String[] orMultiplicityLabel = { ORActionConstants.OR_SET_MULTIPLICITY,
            ORActionConstants.OR_SET_NAVIGABLE, ORActionConstants.OR_SET_REFERENCED_CONSTRAINT, };

    public static final String[] orDataModel = { ORActionConstants.OR_CHANGE_TARGET_SYSTEM,
            ORActionConstants.OR_CONVERT_DATAMODEL_WORKMODE,
            ORActionConstants.OR_GENERATE_CLASS_MODEL, ORActionConstants.OR_GENERATE_COMMON_ITEMS,
            ORActionConstants.OR_GENERATE_FOREIGN_KEYS, ORActionConstants.OR_DELETE_FOREIGN_KEYS,
            SMSActionsStore.INTEGRATE, SMSActionsStore.MOVE, SMSActionsStore.SEND_TO_DIAGRAM,
            SMSActionsStore.FIND_IN_DIAGRAM, ORActionConstants.OR_PROPAGATE_COMMON_ITEM_VALUES, };

    public static final String[] orOperationLibrary = { ORActionConstants.OR_CHANGE_TARGET_SYSTEM,
            SMSActionsStore.INTEGRATE, SMSActionsStore.MOVE, };

    public static final String[] orProcedure = { ORActionConstants.OR_SET_TYPE, };

    public static final String[] orParameter = { ORActionConstants.OR_SET_TYPE,
            ORActionConstants.OR_PROPAGATE_COMMON_ITEM_VALUES, };

    public static final String[] orDomain = { ORActionConstants.OR_SET_TYPE, };

    public static final String[] orTypeGo = { SMSActionsStore.SET_GO_STYLE };

    public static final String[] orDomainModel = { ORActionConstants.OR_CHANGE_TARGET_SYSTEM,
            SMSActionsStore.INTEGRATE, SMSActionsStore.MOVE, };

    public static final String[] orCommonItem = { SMSActionsStore.SEND_TO_DIAGRAM,
            SMSActionsStore.FIND_IN_DIAGRAM, ORActionConstants.OR_SET_TYPE,
            ORActionConstants.OR_PROPAGATE_COMMON_ITEM_VALUES,
            ORActionConstants.OR_DELETE_ORPHAN_ITEMS };

    public static final String[] orCommonItemGo = { SMSActionsStore.SET_GO_STYLE };

    public static final String[] orCommonItemModel = { SMSActionsStore.INTEGRATE,
            SMSActionsStore.MOVE, ORActionConstants.OR_PROPAGATE_COMMON_ITEM_VALUES,
            ORActionConstants.OR_DELETE_ORPHAN_ITEMS };

    public static final String[] orTypeClassifier = { SMSActionsStore.SEND_TO_DIAGRAM,
            SMSActionsStore.FIND_IN_DIAGRAM };

    public static final String[] orAssociation = { SMSActionsStore.REPOSITION_LABELS };

    public static final String[] orDbAssociation = { SMSActionsStore.SEND_TO_DIAGRAM,
            SMSActionsStore.FIND_IN_DIAGRAM };

    public static final String[] orDbAssociationEnd = { SMSActionsStore.FIND_IN_DIAGRAM };

    public static final String[] orUserNode = {};

    public static final String[] orDatabase = { ORActionConstants.OR_CHANGE_TARGET_SYSTEM,
             SMSActionsStore.INTEGRATE, SMSActionsStore.MOVE, };

    public static final String[] orIBMContainerClause = { SMSActionsStore.PROPERTIES,
            SMSActionsStore.ADD, };
}
