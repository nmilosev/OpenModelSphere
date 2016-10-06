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
package org.modelsphere.sms.oo.java.actions;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.sms.actions.ActionFactory;

public class JavaActionFactory implements ActionFactory, JavaActionConstants {
    private static JavaActionFactory singleton;

    static {
        singleton = new JavaActionFactory();
    }

    private JavaActionFactory() {
    }

    public static ActionFactory getSingleton() {
        return singleton;
    }

    public final void registerActions(AbstractActionsStore actionStore) {
        actionStore.put(JAVA_CREATE_ASSOCIATION, new CreateAssociationAction());
        actionStore.put(JAVA_GENERATE_DATA_MODEL, new GenerateDataModelAction());
        actionStore.put(JAVA_DIAGRAM_STATIC_MODIFIER, new StaticModifierAction());
        actionStore.put(JAVA_DIAGRAM_FINAL_MODIFIER, new FinalModifierAction());
        actionStore.put(JAVA_SET_VISIBILITY, new VisibilityAction());
        actionStore.put(JAVA_SET_COLLECTION_DATA_TYPE, new SetCollectionDataTypeAction());
        actionStore.put(JAVA_APPLY_PATTERN, new ApplyPatternAction());
    }

}
