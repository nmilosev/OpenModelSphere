/*************************************************************************

Copyright (C) 2009 Grandite

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

You can redistribute and/or modify this particular file even under the
terms of the GNU Lesser General Public License (LGPL) as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License 
(LGPL) along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.jack.actions;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

// Currently supported for Toolbar only

public abstract class AbstractMenuAction extends AbstractDomainAction {
    public static final String ACTION_COMMAND_SUB_VALUE_SEPARATOR = "[SUB]"; // NOT
    // LOCALIZABLE

    protected int subIndex = -1;

    public AbstractMenuAction(String name, Icon icon, Object[] values) {
        super(name, icon, values, false);
    }

    protected final void doActionPerformed(ActionEvent e) {
        // if null, the component that triggered the event does not use the
        // action command to specify the selected index
        // but instead use the mecanism specified in AbstractDomainAction
        if (e.getActionCommand() == null || e.getActionCommand().length() == 0) {
            super.doActionPerformed(e);
            return;
        }

        int index = -1;
        subIndex = -1;
        try {
            String actionCommand = e.getActionCommand();
            if (actionCommand.indexOf(ACTION_COMMAND_SUB_VALUE_SEPARATOR) != -1) {
                index = Integer.parseInt(actionCommand.substring(0, actionCommand
                        .indexOf(ACTION_COMMAND_SUB_VALUE_SEPARATOR)));
                subIndex = Integer.parseInt(actionCommand.substring(actionCommand
                        .indexOf(ACTION_COMMAND_SUB_VALUE_SEPARATOR)
                        + ACTION_COMMAND_SUB_VALUE_SEPARATOR.length(), actionCommand.length()));
            } else {
                index = Integer.parseInt(actionCommand);
            }
        } catch (Exception ex) {
            index = -1;
        }
        if (index != -1) {
            setSelectedIndex(index);
            super.doActionPerformed(e);
            setSelectedIndex(-1);
        }
    }

}
