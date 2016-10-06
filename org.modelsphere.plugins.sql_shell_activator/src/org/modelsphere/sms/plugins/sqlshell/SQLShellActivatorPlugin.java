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

package org.modelsphere.sms.plugins.sqlshell;

import java.awt.event.ActionEvent;

import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;
import org.modelsphere.jack.srtool.reverse.jdbc.SQLShell;
import org.modelsphere.sms.SMSFilter;

/**
 * 
 * For each kind of database object (tablespace, table, index, etc.), a user can decide if he/she
 * wants to reverse it. Only selected kinds of objects will be reverse engineered.
 * 
 */
public final class SQLShellActivatorPlugin implements Plugin2 {
    public int getFeatureSet() {
        return SMSFilter.RELATIONAL;
    }

    public PluginSignature getSignature() {
        return null;
    }

    public String installAction(DefaultMainFrame frame, MainFrameMenu menuManager) {
        return null;
    }

    public Class[] getSupportedClasses() {
        return null;
    }
    
    @Override
    public OptionGroup getOptionGroup() {
        return null;
    }
    
    @Override
    public PluginAction getPluginAction() {
        /*String actionName = LocaleMgr.misc.getString("SQLShell") + "...";
        PluginAction action = new PluginAction(this, actionName);
        return action;*/
    	return null;
    }

    public void execute(ActionEvent actEvent) throws Exception {
        SQLShell sqlShell = new SQLShell();
        ApplicationContext.getDefaultMainFrame().getJDesktopPane().add(sqlShell,
                DefaultMainFrame.PROPERTY_LAYER);
        sqlShell.setVisible(true);
    }
    
    public boolean doListenSelection() { return false; }
}
