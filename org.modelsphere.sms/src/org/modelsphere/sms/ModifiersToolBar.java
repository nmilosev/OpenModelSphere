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

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.awt.JackToolBar;
import org.modelsphere.sms.international.LocaleMgr;

public class ModifiersToolBar extends JackToolBar {

    public ModifiersToolBar() {
        Module[] modules = Application.MODULES;
        int nbi = modules.length;
        for (int i = 0; i < nbi; i++) {
            AbstractApplicationAction[] modifiers = modules[i].getModifierActions();
            if (modifiers == null || modifiers.length == 0) {
                continue;
            }

            if (i > 0 && this.getComponentCount() > 0) {
                addSeparator();
            }

            int nbj = modifiers.length;
            for (int j = 0; j < nbj; j++) {
                add(modifiers[j]);
            }
        }
        setName(LocaleMgr.misc.getString("ModifiersToolBarName"));
    }

    // Allow hiding in GUI for non supported features.
    public int getFeatureSet() {
        return SMSFilter.JAVA | SMSFilter.RELATIONAL;
    }

}
