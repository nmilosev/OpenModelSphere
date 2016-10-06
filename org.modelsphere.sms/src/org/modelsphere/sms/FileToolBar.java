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

import javax.swing.JButton;

import org.modelsphere.jack.awt.JackToolBar;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.international.LocaleMgr;

public class FileToolBar extends JackToolBar {
    private JButton buttonNew;
    private JButton buttonOpen;
    private JButton buttonClose;
    private JButton buttonSave;
    private JButton buttonPrint;

    public FileToolBar() {
        SMSActionsStore actionsStore = SMSActionsStore.getSingleton();
        buttonNew = addFormated(actionsStore.getAction(SMSActionsStore.PROJECT_NEW));
        buttonOpen = addFormated(actionsStore.getAction(SMSActionsStore.PROJECT_OPEN));
        buttonClose = addFormated(actionsStore.getAction(SMSActionsStore.PROJECT_CLOSE));
        buttonSave = addFormated(actionsStore.getAction(SMSActionsStore.PROJECT_SAVE));
        buttonPrint = addFormated(actionsStore.getAction(SMSActionsStore.PROJECT_PRINT));
        setName(LocaleMgr.misc.getString("FileToolBarName"));
    }

}
