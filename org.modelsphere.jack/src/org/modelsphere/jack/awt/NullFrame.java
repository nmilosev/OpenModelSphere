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

package org.modelsphere.jack.awt;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.modelsphere.jack.srtool.ApplicationContext;

// This is a frame used to provide if the DefaultMainFrame is not initialized yet.
// This frame never become visible unless '-DnullFrameVisible' is specified on VM startup (QA purposes).
// Allow an icon to be set on the Dialog.
public final class NullFrame extends JFrame {
    private static final String NULL_FRAME_VISIBLE_PROPERTY = "nullFrameVisible"; // NOT LOCALIZABLE
    public static final NullFrame singleton = new NullFrame();
    private boolean allowVisible = false;

    {
        setIconImage(ApplicationContext.APPLICATION_IMAGE_ICON);
        setTitle(ApplicationContext.getApplicationName());
        allowVisible = System.getProperty(NULL_FRAME_VISIBLE_PROPERTY) != null;
        if (allowVisible) {
            setSize(100, 50);
            AwtUtil.centerWindow(this);
            setVisible(true);
        }
        try {
            String sDefaultLF = UIManager.getSystemLookAndFeelClassName();
            if (sDefaultLF == null)
                sDefaultLF = UIManager.getLookAndFeel().getClass().getName();
            UIManager.setLookAndFeel(sDefaultLF);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
        }
    }

    public void show() {
        if (allowVisible)
            super.setVisible(true);
    }

    public void setVisible(boolean b) {
        super.setVisible(allowVisible && b);
    }
};
