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

import org.modelsphere.jack.awt.JackPopupMenu;
import org.modelsphere.jack.srtool.popupMenu.DbPopupMenu;
import org.modelsphere.jack.util.SrVector;
import org.modelsphere.sms.Application;
import org.modelsphere.sms.Module;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.be.actions.BEActionConstants;
import org.modelsphere.sms.oo.actions.OOActionConstants;
import org.modelsphere.sms.oo.java.actions.JavaActionConstants;
import org.modelsphere.sms.or.actions.ORActionConstants;

public final class ApplicationPopupMenu extends
        org.modelsphere.jack.srtool.popupMenu.ApplicationPopupMenu {

    public static ApplicationPopupMenu singleton = new ApplicationPopupMenu();

    /**
     * This array (itemMenuNames in constructor) is a mirror of a popupmenu
     * 
     * Order is important. Each action type is seperated by a SEPARATOR
     */

    private ApplicationPopupMenu() {
        itemMenuNames = new String[] { SMSActionsStore.PROPERTIES, SMSActionsStore.LIST, SEPARATOR,
                SMSActionsStore.SHOW_DIAGRAM, SEPARATOR, SMSActionsStore.PROJECT_NEW,
                SMSActionsStore.PROJECT_OPEN, SEPARATOR, SMSActionsStore.ADD,
                BEActionConstants.BE_INSERT_ROW, BEActionConstants.BE_INSERT_COLUMN,
                OOActionConstants.OO_SET_TYPE, OOActionConstants.OO_LINK_CONSTRAINT,
                ORActionConstants.OR_SET_TYPE, SMSActionsStore.COPY, SMSActionsStore.COPY_IMAGE,
                SMSActionsStore.COPY_STAMP_IMAGE, SMSActionsStore.PASTE,
                SMSActionsStore.PASTE_DIAGRAM_IMAGE, SMSActionsStore.PASTE_STAMP_IMAGE,
                SMSActionsStore.DELETE, BEActionConstants.BE_REMOVE_ROW,
                BEActionConstants.BE_REMOVE_COLUMN, OOActionConstants.OO_DELETE_PRESERVED_FIELDS,
                SMSActionsStore.REMOVE_GRAPHIC, SMSActionsStore.REMOVE_STAMP_IMAGE,
                SMSActionsStore.MOVE, SMSActionsStore.SEND_TO_DIAGRAM, SEPARATOR,
                SMSActionsStore.CHANGE_STAMP_IMAGE, SMSActionsStore.INSERT_DIAGRAM_IMAGE,
                SMSActionsStore.SET_DRAWING_AREA, SMSActionsStore.SET_PAGE_NUMBER,
                SMSActionsStore.PAGE_BREAKS, SEPARATOR, SMSActionsStore.RIGHT_ANGLE,
                SMSActionsStore.STRAIGHTEN, BEActionConstants.BE_FLOW_DIRECTION,
                OOActionConstants.OO_SET_MULTIPLICITY, ORActionConstants.OR_SET_MULTIPLICITY,
                JavaActionConstants.JAVA_SET_VISIBILITY, OOActionConstants.OO_NAVIGABLE,
                ORActionConstants.OR_SET_NAVIGABLE, ORActionConstants.OR_SET_REFERENCED_CONSTRAINT,
                OOActionConstants.OO_SET_AGGREGATION,
                JavaActionConstants.JAVA_SET_COLLECTION_DATA_TYPE, SEPARATOR, SMSActionsStore.FONT,
                SMSActionsStore.AUTO_FIT, SMSActionsStore.FIT, SMSActionsStore.ALIGN_ZONE_BOX,
                SMSActionsStore.REPOSITION_LABELS, BEActionConstants.BE_REPOSITION_QUALIFIERS,
                SMSActionsStore.REPOSITION_LABEL, SMSActionsStore.SET_IMAGE_OPACITY,
                SMSActionsStore.REVERT_TO_ORIGINAL_SIZE, SMSActionsStore.PROJECT_UDF,
                SMSActionsStore.PROJECT_STYLE, SMSActionsStore.SET_GO_STYLE,
                SMSActionsStore.SET_DIAGRAM_DEFAULT_STYLE,
                ORActionConstants.OR_SETUP_TARGET_SYSTEM,
                SMSActionsStore.SMS_SET_PROJECT_DEFAULT_OR_NOTATION,
                SMSActionsStore.SMS_SET_PROJECT_DEFAULT_ER_NOTATION,
                SMSActionsStore.SMS_SET_PROJECT_DEFAULT_BE_NOTATION, SMSActionsStore.FORMAT,
                SMSActionsStore.SET_NOTATION, SEPARATOR, OOActionConstants.OO_UPDATE_INHERITANCE,
                JavaActionConstants.JAVA_CREATE_ASSOCIATION,
                ORActionConstants.OR_CHANGE_TARGET_SYSTEM,
                ORActionConstants.OR_CONVERT_DATAMODEL_WORKMODE,
                ORActionConstants.OR_GENERATE_CLASS_MODEL,
                ORActionConstants.OR_GENERATE_COMMON_ITEMS,
                ORActionConstants.OR_PROPAGATE_COMMON_ITEM_VALUES,
                ORActionConstants.OR_DELETE_ORPHAN_ITEMS, ORActionConstants.OR_MERGE_COLUMN,
                ORActionConstants.OR_SPLIT_COLUMN, ORActionConstants.OR_PROPAG_ATTRS,
                SMSActionsStore.CREATE_LINK, OOActionConstants.OO_GENERATE_COMMON_ITEMS,
                JavaActionConstants.JAVA_GENERATE_DATA_MODEL, SMSActionsStore.INTEGRATE,
                OOActionConstants.OO_PROJECT_REVERSE,
                OOActionConstants.OO_VALIDATE_FOR_JAVA,
                BEActionConstants.BE_UPDATE_ENVIRONMENT_USECASE,
                JavaActionConstants.JAVA_APPLY_PATTERN, SEPARATOR,
                ORActionConstants.OR_GENERATE_PRIMARY_KEYS,
                ORActionConstants.OR_GENERATE_FOREIGN_KEYS,
                ORActionConstants.OR_GENERATE_REFERENTIAL_RULES,
                ORActionConstants.OR_DELETE_FOREIGN_KEYS, SEPARATOR,
                SMSActionsStore.FIND_IN_DIAGRAM, SMSActionsStore.FIND_IN_EXPLORER, SEPARATOR,
                SMSActionsStore.SELECT_ALL, SMSActionsStore.SELECT_CLASSES,
                BEActionConstants.BE_SELECT_ALL_ACTORS, BEActionConstants.BE_SELECT_ALL_STORES,
                SMSActionsStore.SELECT_LABELS, SMSActionsStore.SELECT_LINES, SEPARATOR,
                SMSActionsStore.CREATE_MISSING_GRAPHICS, SMSActionsStore.CONSOLIDATE_DIAGRAMS,
                SMSActionsStore.LAYOUT_DIAGRAM, SMSActionsStore.LAYOUT_SELECTION, SMSActionsStore.LAYOUT_RIGHT_ANGLE,
                SMSActionsStore.SAVE_DIAGRAM, SMSActionsStore.PROJECT_PRINT, SEPARATOR, };

        // add popupmenu info from modules
        SrVector vecPopupMenus = new SrVector(100);
        Module[] modules = Application.MODULES;
        for (int i = 0; i < modules.length; i++) {
            Object[] popupMenus = modules[i].getPopupMenuMapping();
            if (popupMenus != null)
                vecPopupMenus
                        .insertElements(vecPopupMenus.size(), popupMenus, 0, popupMenus.length);
        }
        instanceWithPopupMenu = vecPopupMenus.toArray();

    }

    public final JackPopupMenu getPopupMenu(boolean useContainer) {
        SMSActionsStore store = SMSActionsStore.getSingleton();
        JackPopupMenu menu = getPopupMenu(store, useContainer, DbPopupMenu.actionsName,
                SMSPopupMenuPool.diagramContainer);
        return menu;
    }
}
