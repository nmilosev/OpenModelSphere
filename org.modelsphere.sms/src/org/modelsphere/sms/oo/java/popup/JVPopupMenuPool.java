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

package org.modelsphere.sms.oo.java.popup;

import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.oo.actions.OOActionConstants;
import org.modelsphere.sms.oo.java.actions.JavaActionConstants;
import org.modelsphere.sms.or.actions.ORActionConstants;

public final class JVPopupMenuPool {
    private JVPopupMenuPool() {
    }

    public static final String[] jvAssociation = { OOActionConstants.OO_DELETE_PRESERVED_FIELDS,
            SMSActionsStore.DELETE, SMSActionsStore.FIND_IN_DIAGRAM };

    // class popups
    public static final String[] jvClass = {
            //SMSActionsStore.OO_UPDATE_INHERITANCE,
            OOActionConstants.OO_VALIDATE_FOR_JAVA, SMSActionsStore.SEND_TO_DIAGRAM,
            SMSActionsStore.MOVE, SMSActionsStore.FIND_IN_DIAGRAM,
            JavaActionConstants.JAVA_APPLY_PATTERN,
            ORActionConstants.OR_PROPAGATE_COMMON_ITEM_VALUES, };

    public static final String[] jvClassGo = { SMSActionsStore.LAYOUT_RIGHT_ANGLE,
            SMSActionsStore.SET_GO_STYLE };
    // End class popups

    public static final String[] jvClassModel = {
            //SMSActionsStore.OO_UPDATE_INHERITANCE,
            SMSActionsStore.INTEGRATE, JavaActionConstants.JAVA_GENERATE_DATA_MODEL,
            OOActionConstants.OO_VALIDATE_FOR_JAVA, SMSActionsStore.MOVE,
            ORActionConstants.OR_PROPAGATE_COMMON_ITEM_VALUES, };

    public static final String[] jvCompilationUnit = {};

    public static final String[] jvField = { OOActionConstants.OO_SET_TYPE,
            OOActionConstants.OO_LINK_CONSTRAINT, JavaActionConstants.JAVA_CREATE_ASSOCIATION,
            ORActionConstants.OR_PROPAGATE_COMMON_ITEM_VALUES, SMSActionsStore.CREATE_LINK };

    public static final String[] jvInheritance = { SMSActionsStore.RIGHT_ANGLE,
            SMSActionsStore.DELETE, SMSActionsStore.STRAIGHTEN, SMSActionsStore.SET_GO_STYLE,
            SMSActionsStore.FIND_IN_DIAGRAM };

    public static final String[] jvInitBlock = {};

    public static final String[] jvMethod = { OOActionConstants.OO_SET_TYPE, };

    public static final String[] jvMultiplicityLabel = { OOActionConstants.OO_SET_MULTIPLICITY,
            OOActionConstants.OO_NAVIGABLE, OOActionConstants.OO_SET_AGGREGATION,
            JavaActionConstants.JAVA_SET_COLLECTION_DATA_TYPE, };

    // Package popups
    public static final String[] jvPackage = {
            //SMSActionsStore.OO_UPDATE_INHERITANCE,
            SMSActionsStore.INTEGRATE, OOActionConstants.OO_VALIDATE_FOR_JAVA,
            JavaActionConstants.JAVA_GENERATE_DATA_MODEL, SMSActionsStore.SEND_TO_DIAGRAM,
            SMSActionsStore.FIND_IN_DIAGRAM, SMSActionsStore.MOVE, };

    public static final String[] jvPackageGo = { SMSActionsStore.SET_GO_STYLE };
    // End of Package Popups

    public static final String[] jvParameter = { OOActionConstants.OO_SET_TYPE, };

    public static final String[] jvRoleLabel = { OOActionConstants.OO_SET_MULTIPLICITY,
            OOActionConstants.OO_NAVIGABLE, OOActionConstants.OO_SET_AGGREGATION,
            JavaActionConstants.JAVA_SET_COLLECTION_DATA_TYPE, };

}
