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

package org.modelsphere.sms;

import javax.swing.Icon;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.awt.JackToolBar;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.actions.OOActionConstants;

public class ToolsToolBar extends JackToolBar {

    public ToolsToolBar() {
        setName(LocaleMgr.misc.getString("ToolsToolBarName"));
    }

    // For this toolbar, we must install actions after plugins has been loaded
    // because of Integrity plugin (otherwise, the action does not exist)
    // Called within Application (once plugins has been instanciated)
    void init() {
        SMSActionsStore actionsStore = SMSActionsStore.getSingleton();
        addFormated(actionsStore.getAction(SMSActionsStore.CREATE_LINK));
        AbstractApplicationAction action = actionsStore
                .getAction("org.modelsphere.sms.plugins.integrity.Integrity"); // NOT
        // LOCALIZABLE
        if (action != null) {
            addFormated(action);
            String text = LocaleMgr.action.getString("CleanUpModel");
            Icon cleanUpIcon = LocaleMgr.action.getImageIcon("CleanUpModel");
            addFormated(action, text, cleanUpIcon);
        }

        // AbstractApplicationAction action1 =
        // actionsStore.getAction("org.modelsphere.sms.plugins.integrity.CleanUp")
        // ; // NOT LOCALIZABLE
        // if (action1 != null)
        // addFormated(action1); // NOT LOCALIZABLE

        addFormated(actionsStore.getAction(OOActionConstants.OO_VALIDATE_FOR_JAVA));
    }
}
