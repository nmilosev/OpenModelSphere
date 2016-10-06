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

import java.awt.Rectangle;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import org.modelsphere.jack.international.LocaleMgr;

public class JInternalFrameArrangeIconsAction extends JInternalFrameAbstractAction {

    JInternalFrameArrangeIconsAction() {
        super(LocaleMgr.action.getString("ArrangeIcons"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("ArrangeIcons"));
        this.setEnabled(true);
    }

    protected final void doActionPerformed() {
        JDesktopPane desktop = getJDesktopPane();
        if (desktop != null) {
            JInternalFrame[] frames = desktop.getAllFrames();
            int frameCount = frames.length;

            Rectangle lastBounds = new Rectangle(0, desktop.getBounds().height, 0, 0);
            int desktopwidth = desktop.getBounds().width;

            for (int i = 0; i < frameCount; i++) {
                if (frames[i].isIcon()) {
                    Rectangle r = new Rectangle(frames[i].getDesktopIcon().getBounds());
                    if (((lastBounds.x + lastBounds.width) + r.width) > desktopwidth) { // start
                        // a
                        // new
                        // line?
                        lastBounds.y = Math.max(lastBounds.y - lastBounds.height, 0);
                        lastBounds.x = 0;
                        lastBounds.width = 0;
                        lastBounds.height = 0;
                    }

                    // may not work in further swing release (written with swing
                    // 1_0_2)
                    // in documentation, they say that the desktop icon will be
                    // manage in the JInternalFrame
                    frames[i].getDesktopIcon().setBounds(lastBounds.x + lastBounds.width,
                            Math.max(lastBounds.y - r.height, 0), r.width, r.height);
                    r = new Rectangle(frames[i].getDesktopIcon().getBounds());
                    lastBounds.x = r.x;
                    lastBounds.width = r.width;
                    lastBounds.height = r.height;

                }
            }
        }
    }

}
