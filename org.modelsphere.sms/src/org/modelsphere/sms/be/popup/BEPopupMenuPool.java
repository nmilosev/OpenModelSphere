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

package org.modelsphere.sms.be.popup;

import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.be.actions.BEActionConstants;

public final class BEPopupMenuPool {
    private BEPopupMenuPool() {
    }

    public static final String[] beModel = { SMSActionsStore.INTEGRATE, SMSActionsStore.MOVE, };

    // bediagram popups
    public static final String[] beDiagram = { SMSActionsStore.ADD };

    // beUseCase popups
    public static final String[] beUseCase = { BEActionConstants.BE_UPDATE_ENVIRONMENT_USECASE,
            SMSActionsStore.SEND_TO_DIAGRAM, SMSActionsStore.FIND_IN_DIAGRAM,
            SMSActionsStore.CONSOLIDATE_DIAGRAMS };

    // beUseCaseGo popups
    public static final String[] beUseCaseBox = { SMSActionsStore.SET_GO_STYLE,
            SMSActionsStore.LAYOUT_RIGHT_ANGLE, BEActionConstants.BE_REPOSITION_QUALIFIERS, };

    // beUseCase popups
    public static final String[] beUseCaseFrame = { SMSActionsStore.DISCARD + SMSActionsStore.ADD,
            SMSActionsStore.DISCARD + SMSActionsStore.DELETE,
            SMSActionsStore.DISCARD + SMSActionsStore.FIT,
            SMSActionsStore.DISCARD + SMSActionsStore.AUTO_FIT,
            SMSActionsStore.DISCARD + SMSActionsStore.ALIGN_ZONE_BOX,
            SMSActionsStore.DISCARD + SMSActionsStore.FIND_IN_DIAGRAM, };

    // beStore popups
    public static final String[] beStore = { SMSActionsStore.SEND_TO_DIAGRAM,
            SMSActionsStore.FIND_IN_DIAGRAM };

    // beStoreGo popups
    public static final String[] beStoreBox = { SMSActionsStore.SET_GO_STYLE,
            SMSActionsStore.LAYOUT_RIGHT_ANGLE, BEActionConstants.BE_REPOSITION_QUALIFIERS, };

    // beActor popups
    public static final String[] beActor = { SMSActionsStore.SEND_TO_DIAGRAM,
            SMSActionsStore.FIND_IN_DIAGRAM };

    // beActorGo popups
    public static final String[] beActorBox = { SMSActionsStore.SET_GO_STYLE,
            SMSActionsStore.LAYOUT_RIGHT_ANGLE, BEActionConstants.BE_REPOSITION_QUALIFIERS, };

    // beFlow popups
    public static final String[] beFlow = { SMSActionsStore.DELETE,
            SMSActionsStore.SEND_TO_DIAGRAM, SMSActionsStore.FIND_IN_DIAGRAM };

    public static final String[] beFlowPeer = { SMSActionsStore.RIGHT_ANGLE,
            SMSActionsStore.STRAIGHTEN, SMSActionsStore.SET_GO_STYLE,
            SMSActionsStore.REPOSITION_LABELS, BEActionConstants.BE_FLOW_DIRECTION, };

    public static final String[] beFlowLabel = { SMSActionsStore.REPOSITION_LABEL, };

    // beQualifier popups
    public static final String[] beQualifier = { SMSActionsStore.REPOSITION_LABEL, };

    // beContextGo
    public static final String[] beContextGo = { BEActionConstants.BE_INSERT_ROW,
            BEActionConstants.BE_INSERT_COLUMN, BEActionConstants.BE_REMOVE_ROW,
            BEActionConstants.BE_REMOVE_COLUMN,
    // SMSActionsStore.ADD,
    // SMSActionsStore.DELETE,
    };

    public static final String[] beContextCell = { SMSActionsStore.PROPERTIES, };

} // end BEPopupMenuPool
