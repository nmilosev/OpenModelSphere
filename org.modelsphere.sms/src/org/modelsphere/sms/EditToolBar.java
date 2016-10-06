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

import org.modelsphere.jack.awt.JackToolBar;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.international.LocaleMgr;

public class EditToolBar extends JackToolBar {

    public EditToolBar() {
        SMSActionsStore actionsStore = SMSActionsStore.getSingleton();
        addFormated(actionsStore.getAction(SMSActionsStore.PROJECT_UNDO));
        addFormated(actionsStore.getAction(SMSActionsStore.PROJECT_REDO));

        addSeparator();
        addFormated(actionsStore.getAction(SMSActionsStore.ADD));
        addFormated(actionsStore.getAction(SMSActionsStore.COPY));
        addFormated(actionsStore.getAction(SMSActionsStore.PASTE));
        addFormated(actionsStore.getAction(SMSActionsStore.DELETE));

        addSeparator();
        addFormated(actionsStore.getAction(SMSActionsStore.STRAIGHTEN));

        addSeparator();
        addFormated(actionsStore.getAction(SMSActionsStore.PROJECT_FIND));
        addFormated(actionsStore.getAction(SMSActionsStore.PROJECT_FIND_NEXT));
        addFormated(actionsStore.getAction(SMSActionsStore.PROJECT_FIND_PREVIOUS));

        setName(LocaleMgr.misc.getString("EditToolBarName"));
    }
}
