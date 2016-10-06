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

package org.modelsphere.sms.popup;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.be.actions.BEActionConstants;
import org.modelsphere.sms.or.actions.ORActionConstants;

public final class SMSPopupMenuPool {
    private SMSPopupMenuPool() {
    }

    public static final String[] graphicComponent = { SMSActionsStore.COPY_IMAGE,
            SMSActionsStore.FIT, SMSActionsStore.AUTO_FIT, SMSActionsStore.REMOVE_GRAPHIC,
            SMSActionsStore.SEND_TO_DIAGRAM, SMSActionsStore.FORMAT };

    public static final String[] diagramImage = { AbstractActionsStore.DELETE,
            AbstractActionsStore.COPY_IMAGE,
            SMSActionsStore.DISCARD + SMSActionsStore.REMOVE_GRAPHIC,
            SMSActionsStore.DISCARD + SMSActionsStore.FORMAT,
            AbstractActionsStore.REVERT_TO_ORIGINAL_SIZE, SMSActionsStore.SEND_TO_DIAGRAM, };

    public static final String[] lineLabel = { SMSActionsStore.DISCARD + SMSActionsStore.COPY,
    // SMSActionsStore.DISCARD + SMSActionsStore.DELETE,
            SMSActionsStore.DISCARD + SMSActionsStore.PASTE, SMSActionsStore.REPOSITION_LABEL };

    public static final String[] smsDiagram = { SMSActionsStore.SHOW_DIAGRAM, SMSActionsStore.COPY,
            SMSActionsStore.DELETE, SMSActionsStore.CREATE_MISSING_GRAPHICS,
            SMSActionsStore.SAVE_DIAGRAM, SMSActionsStore.CONSOLIDATE_DIAGRAMS,
            ORActionConstants.OR_CONVERT_DATAMODEL_WORKMODE, };

    public static final String[] diagramContainer = {
            SMSActionsStore.SELECT_ALL,
            SMSActionsStore.SELECT_CLASSES,
            BEActionConstants.BE_SELECT_ALL_ACTORS,
            BEActionConstants.BE_SELECT_ALL_STORES,
            SMSActionsStore.SELECT_LABELS,
            SMSActionsStore.SELECT_LINES,
            SMSActionsStore.PAGE_BREAKS,
            SMSActionsStore.ADD,
            // SMSActionsStore.PROJECT_STYLE,
            SMSActionsStore.PASTE,
            SMSActionsStore.PASTE_DIAGRAM_IMAGE,
            SMSActionsStore.SEND_TO_DIAGRAM,
            SMSActionsStore.CREATE_MISSING_GRAPHICS,
            SMSActionsStore.LAYOUT_DIAGRAM,
            // SMSActionsStore.PROJECT_PRINT,
            SMSActionsStore.SET_DRAWING_AREA, SMSActionsStore.SET_DIAGRAM_DEFAULT_STYLE,
            SMSActionsStore.INSERT_DIAGRAM_IMAGE, SMSActionsStore.SET_PAGE_NUMBER,
            SMSActionsStore.SET_NOTATION,
            // SMSActionsStore.SAVE_DIAGRAM,
            ORActionConstants.OR_CONVERT_DATAMODEL_WORKMODE,
            BEActionConstants.BE_UPDATE_ENVIRONMENT_USECASE, SMSActionsStore.CONSOLIDATE_DIAGRAMS, };

    public static final String[] smsProject = { SMSActionsStore.PROPERTIES, SMSActionsStore.LIST,
            SMSActionsStore.ADD, SMSActionsStore.PASTE, SMSActionsStore.DELETE,
            SMSActionsStore.PROJECT_UDF, SMSActionsStore.SMS_SET_PROJECT_DEFAULT_OR_NOTATION,
            SMSActionsStore.SMS_SET_PROJECT_DEFAULT_ER_NOTATION,
            SMSActionsStore.SMS_SET_PROJECT_DEFAULT_BE_NOTATION,
            ORActionConstants.OR_SETUP_TARGET_SYSTEM, ORActionConstants.OR_GENERATE_COMMON_ITEMS, };

    public static final String[] smsSemanticalObject = { SMSActionsStore.PROPERTIES,
            SMSActionsStore.LIST, SMSActionsStore.ADD, SMSActionsStore.COPY, SMSActionsStore.PASTE,
            SMSActionsStore.DELETE, SMSActionsStore.FIND_IN_EXPLORER, };

    public static final String[] smsRestrictedSemanticalObject = { SMSActionsStore.PROPERTIES,
            SMSActionsStore.SEND_TO_DIAGRAM, SMSActionsStore.FIND_IN_DIAGRAM, SMSActionsStore.COPY,
            SMSActionsStore.PASTE, SMSActionsStore.DELETE, };

    public static final String[] smsPackage = { SMSActionsStore.CONSOLIDATE_DIAGRAMS, };

    public static final String[] smsUserDefinedPackage = { SMSActionsStore.MOVE, };

    public static final String[] smsUmlExtensionsNode = {};

    public static final String[] smsUmlStereotype = { SMSActionsStore.PROPERTIES };

    public static final String[] smsUmlConstraint = { SMSActionsStore.PROPERTIES };

    public static final String[] srLine = { SMSActionsStore.RIGHT_ANGLE,
            SMSActionsStore.STRAIGHTEN, SMSActionsStore.SET_GO_STYLE };

    public static final String[] zoneBox = { SMSActionsStore.ALIGN_ZONE_BOX };

    public static final String[] smsLinkModel = {};

    public static final String[] smsNotice = { SMSActionsStore.PROPERTIES,
            SMSActionsStore.SEND_TO_DIAGRAM, SMSActionsStore.FIND_IN_DIAGRAM, SMSActionsStore.COPY,
            SMSActionsStore.PASTE, SMSActionsStore.DELETE, SMSActionsStore.SET_GO_STYLE };

}
