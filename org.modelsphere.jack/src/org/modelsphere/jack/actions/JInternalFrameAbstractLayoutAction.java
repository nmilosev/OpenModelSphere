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

import javax.swing.*;

import org.modelsphere.jack.srtool.graphic.MagnifierInternalFrame;
import org.modelsphere.jack.srtool.graphic.OverviewInternalFrame;

public abstract class JInternalFrameAbstractLayoutAction extends JInternalFrameAbstractAction {

    protected static final int ACTIVE_FRAME_FIRST = 0; // switch the active
    // frame to be set as
    // the first frame
    protected static final int ACTIVE_FRAME_LAST = 1; // switch the active frame
    // to be set as the last
    // frame
    protected static final int ACTIVE_FRAME_IGNORE = 2; // do not change order
    // of the active frame

    private int activeFrameMode;

    JInternalFrameAbstractLayoutAction(String name, ImageIcon icon, int activeframemode) {
        super(name, icon);
        this.setEnabled(true);
        activeFrameMode = activeframemode;
    }

    JInternalFrameAbstractLayoutAction(String name, ImageIcon icon) {
        this(name, icon, ACTIVE_FRAME_FIRST);
    }

    JInternalFrameAbstractLayoutAction(String name, int activeframemode) {
        this(name, null, activeframemode);
    }

    JInternalFrameAbstractLayoutAction(String name) {
        this(name, null, ACTIVE_FRAME_FIRST);
    }

    protected abstract Rectangle getInternalFrameNewBounds(int cursor, int frameCount,
            int desktopwidth, int desktopheight);

    public final void doActionPerformed() {
        JDesktopPane desktop = getJDesktopPane();
        if (desktop != null) {
            JInternalFrame[] frames = desktop.getAllFrames();
            int frameCount = frames.length;

            int framesNotIconifiedCount = 0;

            for (int i = 0; i < frameCount; i++) {
                if (!frames[i].isIcon() && !(frames[i] instanceof OverviewInternalFrame)
                        && !(frames[i] instanceof MagnifierInternalFrame)) {
                    framesNotIconifiedCount++;
                }
            }
            JInternalFrame[] framesNotIconified = new JInternalFrame[framesNotIconifiedCount];

            JInternalFrame activeFrame = null;
            int activeFramePosition = -1;

            int j = 0;
            for (int i = 0; i < frameCount; i++) {
                if (!frames[i].isIcon() && !(frames[i] instanceof OverviewInternalFrame)
                        && !(frames[i] instanceof MagnifierInternalFrame)) {
                    framesNotIconified[j] = frames[i];
                    if (frames[i].isSelected()) {
                        activeFrame = frames[i];
                        activeFramePosition = j;
                    }
                    j++;
                }
            }

            DesktopManager dm = desktop.getDesktopManager();

            if ((activeFrame != null)) { // Switch the active Frame to be on top
                switch (activeFrameMode) {
                case ACTIVE_FRAME_FIRST:
                    framesNotIconified[activeFramePosition] = framesNotIconified[0];
                    framesNotIconified[0] = activeFrame;
                    activeFramePosition = 0;
                    break;
                case ACTIVE_FRAME_LAST:
                    framesNotIconified[activeFramePosition] = framesNotIconified[framesNotIconifiedCount - 1];
                    framesNotIconified[framesNotIconifiedCount - 1] = activeFrame;
                    activeFramePosition = framesNotIconifiedCount - 1;
                    break;
                }
            }

            int w = desktop.getBounds().width;
            int h = desktop.getBounds().height;

            for (int i = 0; i < framesNotIconifiedCount; i++) {
                Rectangle newbounds = getInternalFrameNewBounds(i, framesNotIconifiedCount, w, h);
                try {
                    if (framesNotIconified[i].isMaximum())
                        framesNotIconified[i].setMaximum(false);
                    dm.setBoundsForFrame(framesNotIconified[i], newbounds.x, newbounds.y,
                            newbounds.width, newbounds.height);
                } catch (Exception ex) {
                    org.modelsphere.jack.debug.Debug.handleException(ex);
                }
                framesNotIconified[i].toFront();
            }

            // reactivate the active frame
            if (activeFrame != null) {
                try {
                    activeFrame.toFront();
                    activeFrame.setSelected(true);
                    dm.activateFrame(activeFrame);
                } catch (Exception ex) {
                    org.modelsphere.jack.debug.Debug.handleException(ex);
                }
            }
        }
    }

}
