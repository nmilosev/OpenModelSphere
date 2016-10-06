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

package org.modelsphere.sms.oo.actions;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.sms.actions.ActionFactory;
import org.modelsphere.sms.actions.CreateLinkAction;
import org.modelsphere.sms.actions.GenerateCommonItemsAction;
import org.modelsphere.sms.actions.SMSActionsStore;

public class OOActionFactory implements ActionFactory {
    private static OOActionFactory singleton;

    static {
        singleton = new OOActionFactory();
    }

    private OOActionFactory() {
    }

    public static ActionFactory getSingleton() {
        return singleton;
    }

    public final void registerActions(AbstractActionsStore actionStore) {
        actionStore.put(OOActionConstants.OO_DELETE_PRESERVED_FIELDS,
                new DeletePreservedFieldsAction());
        actionStore
                .put(OOActionConstants.OO_GENERATE_COMMON_ITEMS, new GenerateCommonItemsAction());
        actionStore.put(OOActionConstants.OO_NAVIGABLE, new NavigableAction());
        actionStore.put(OOActionConstants.OO_PROJECT_REVERSE, new ReverseAction());
        actionStore.put(OOActionConstants.OO_DIAGRAM_ABSTRACT_MODIFIER,
                new AbstractModifierAction());
        actionStore.put(OOActionConstants.OO_SET_AGGREGATION, new SetAggregationAction());
        actionStore.put(OOActionConstants.OO_SET_MULTIPLICITY, new SetMultiplicityAction());
        actionStore.put(OOActionConstants.OO_SET_TYPE, new SetTypeAction());
        actionStore.put(OOActionConstants.OO_LINK_CONSTRAINT, new LinkConstraintAction());
        actionStore.put(OOActionConstants.OO_UPDATE_INHERITANCE, new UpdateInheritanceAction());
        actionStore.put(OOActionConstants.OO_VALIDATE_FOR_JAVA, new ValidateComponentAction());
    }

}
