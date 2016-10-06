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
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.graphic.tool.ZoomCombo;
import org.modelsphere.jack.srtool.actions.ZoomAction;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.international.LocaleMgr;

public class DisplayToolBar extends JackToolBar {

    private ZoomCombo zoomCombo = new ZoomCombo();
    private JButton buttonRefresh;
    private JButton buttonExpand;
    private JButton buttonCollapse;

    public DisplayToolBar() {
        SMSActionsStore actionsStore = SMSActionsStore.getSingleton();
        buttonExpand = addFormated(actionsStore.getAction(SMSActionsStore.EXPAND_ALL));
        buttonCollapse = addFormated(actionsStore.getAction(SMSActionsStore.COLLAPSE_ALL));
        addSeparator();
        buttonRefresh = addFormated(actionsStore.getAction(SMSActionsStore.APPLICATION_REFRESH));
        addSeparator();
        add(zoomCombo);
        addFormated(actionsStore.getAction(SMSActionsStore.ZOOM_IN));
        addFormated(actionsStore.getAction(SMSActionsStore.ZOOM_OUT));
        addSeparator();
        addFormated(actionsStore.getAction(SMSActionsStore.SHOW_DIAGRAM));
        add(actionsStore.getAction(SMSActionsStore.APPLICATION_SHOW_OVERVIEW_WINDOW));
        add(actionsStore.getAction(SMSActionsStore.APPLICATION_SHOW_MAGNIFIER_WINDOW));

        setName(LocaleMgr.misc.getString("DisplayToolBarName"));

        ZoomAction zoomin = (ZoomAction) actionsStore.getAction(SMSActionsStore.ZOOM_IN);
        ZoomAction zoomout = (ZoomAction) actionsStore.getAction(SMSActionsStore.ZOOM_OUT);
        Debug.assert2(zoomin != null && zoomout != null, "Init Error:  Null zoom action");
        zoomin.setZoomCombo(zoomCombo);
        zoomout.setZoomCombo(zoomCombo);
    }

}
