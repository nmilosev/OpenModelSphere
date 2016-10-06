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

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.actions.ActionFactory;
import org.modelsphere.sms.actions.CreateCommonItemColumnsAction;
import org.modelsphere.sms.actions.CreateLinkAction;
import org.modelsphere.sms.actions.DeleteOrphanCommonItemsAction;
import org.modelsphere.sms.actions.GenerateCommonItemsAction;
import org.modelsphere.sms.actions.PropagateCommonItemValuesAction;
import org.modelsphere.sms.actions.SMSActionsStore;

public class ORActionFactory implements ActionFactory, ORActionConstants {
    private static ORActionFactory singleton;

    static {
        singleton = new ORActionFactory();
    }

    private ORActionFactory() {
    }

    public static ActionFactory getSingleton() {
        return singleton;
    }

    public final void registerActions(AbstractActionsStore actionStore) {
        actionStore.put(OR_CHANGE_TARGET_SYSTEM, new ChangeTargetSystemAction());
        actionStore.put(OR_DELETE_FOREIGN_KEYS, new DeleteForeignKeysAction());
        actionStore.put(OR_DELETE_ORPHAN_ITEMS, new DeleteOrphanCommonItemsAction());
        actionStore.put(OR_FORWARD_DATAMODEL, new ForwardDataModelAction());
        actionStore.put(OR_GENERATE_REFERENTIAL_RULES, new GenerateReferentialRulesAction());
        actionStore.put(OR_GENERATE_CLASS_MODEL, new GenerateClassModelAction());
        actionStore.put(OR_GENERATE_DDL, new GenerateDDLAction());
        actionStore.put(OR_GENERATE_FOREIGN_KEYS, new GenerateForeignKeysAction());
        actionStore.put(OR_GENERATE_PHYSICAL_NAMES, new GeneratePhysicalNamesAction());
        actionStore.put(OR_GENERATE_PRIMARY_KEYS, new GeneratePrimaryKeysAction());
        actionStore.put(OR_GENERATE_COMMON_ITEMS, new GenerateCommonItemsAction());
        actionStore.put(OR_MERGE_COLUMN, new MergeColumnAction());
        actionStore.put(OR_NULL_MODIFIER, new NullModifierAction());
        actionStore.put(OR_PROPAG_ATTRS, new PropagAttributesAction());
        actionStore.put(OR_PROPAGATE_COMMON_ITEM_VALUES, new PropagateCommonItemValuesAction());
        actionStore.put(OR_REVERSE_DB, new ReverseDBAction());
        actionStore.put(OR_SET_MULTIPLICITY, new SetMultiplicityAction());
        actionStore.put(OR_SET_NAVIGABLE, new SetNavigableAction());
        actionStore.put(OR_SET_REFERENCED_CONSTRAINT, new SetReferencedConstraintAction());
        actionStore.put(OR_SET_TYPE, new SetTypeAction());
        actionStore.put(OR_SETUP_TARGET_SYSTEM, new SetupTargetSystemAction());
        actionStore.put(OR_SPLIT_COLUMN, new SplitColumnAction());
        actionStore.put(OR_SYNCHRO_DB, new SynchroDBAction());
        actionStore.put(OR_CONVERT_DATAMODEL_WORKMODE, new ConvertDataModelWorkModeAction());
        actionStore.put(CREATE_COMMON_ITEM_COLUMNS, new CreateCommonItemColumnsAction()); //SILENT ACTION NOT SHOWN IN MENU
    }

    //Called by several updateSelectionAction() methods in this package
    //Called by sms.actions.ValidateAction
    public static DbObject[] getActiveObjects() {
        DbObject[] activeObjects = null;

        //enable if active diagram is a relational diagram
        ApplicationDiagram diagram = ApplicationContext.getFocusManager().getActiveDiagram();
        if (diagram != null) {
            activeObjects = new DbObject[] { diagram.getDiagramGO() };
        } //end if

        //if no active diagram, enable if selected object in a data model
        if (activeObjects == null) {
            activeObjects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        } //end if

        if ((activeObjects != null) && (activeObjects.length == 0)) {
            activeObjects = null;
        } //end if

        return activeObjects;
    } //end isActionEnabled()

}
