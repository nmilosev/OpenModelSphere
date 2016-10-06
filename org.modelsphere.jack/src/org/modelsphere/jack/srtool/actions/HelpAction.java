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

package org.modelsphere.jack.srtool.actions;

import java.net.URL;
import javax.swing.*;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.srtool.international.LocaleMgr;

import java.awt.event.*;

public class HelpAction extends AbstractApplicationAction {

    protected String start = "welcome.html"; // NOT LOCALIZABLE
    protected String helpDir = "help/idehelp"; // NOT LOCALIZABLE
    protected URL startURL;

    public HelpAction() {
        super(LocaleMgr.action.getString("help"), LocaleMgr.action.getImageIcon("help"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("help"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        this.setEnabled(org.modelsphere.jack.srtool.ApplicationContext.getDefaultMainFrame()
                .isHelpInstalled());
    }

    protected final void doActionPerformed() {
        org.modelsphere.jack.srtool.ApplicationContext.getDefaultMainFrame().displayJavaHelp();
    }
}
