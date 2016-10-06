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

package org.modelsphere.sms.actions;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.sms.international.LocaleMgr;

@SuppressWarnings("serial")
public final class PluginMgrAction extends AbstractApplicationAction {
    private static final String PLUG_IN_MANAGER = LocaleMgr.action.getString("PluginManager");

    public PluginMgrAction() {
        super(PLUG_IN_MANAGER + "...");
    }

    protected final void doActionPerformed() {
        PluginMgr.getSingleInstance().showConfigurationDialog();
    }
}
