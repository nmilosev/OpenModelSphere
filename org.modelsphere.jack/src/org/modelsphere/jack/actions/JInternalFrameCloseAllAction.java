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

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import org.modelsphere.jack.international.LocaleMgr;

public class JInternalFrameCloseAllAction extends JInternalFrameAbstractAction {

    JInternalFrameCloseAllAction() {
        super(LocaleMgr.action.getString("CloseAll"), LocaleMgr.action.getImageIcon("CloseAll"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("CloseAll"));
        this.setEnabled(true);
    }

    protected final void doActionPerformed() {
        JDesktopPane desktop = getJDesktopPane();
        if (desktop != null) {
            JInternalFrame[] frames = desktop.getAllFrames();
            int frameCount = frames.length;
            try {
                for (int i = 0; i < frameCount; i++) {
                    if (frames[i].isClosable())
                        frames[i].setClosed(true);
                }
            } catch (java.beans.PropertyVetoException e) {
            } // stop the process at the first frame closing vetoed.
        }
    }

}
