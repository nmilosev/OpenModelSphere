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

package org.modelsphere.jack.srtool;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JDesktopPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.modelsphere.jack.srtool.explorer.ExplorerPanel;
import org.modelsphere.jack.srtool.screen.design.DesignPanel;

/**
 * Basic interface to define a DefaultMainFrame components layout manager.
 */

public interface MFLayoutManager {

    // Just a class to get an empty white panel instead of the default components of JSplitPane (famous 'left button')
    public static class EmptyPanel extends JPanel {
        public final Color getBackground() {
            return UIManager.getColor("Tree.background"); //NOT LOCALIZABLE, property key
        }
    }

    public void setRootContainer(Container root);

    public void setDesktop(JDesktopPane desktopPane);

    public void setExplorer(ExplorerPanel explorer);

    public void setDesignPanel(DesignPanel designPanel);

    public void setPreferredSize(DesignPanel designPanel, Dimension size);

    public void setPreferredSize(ExplorerPanel explorerPanel, Dimension size);

    public Dimension getPreferredSize(DesignPanel designPanel);

    public Dimension getPreferredSize(ExplorerPanel explorerPanel);

    public void removeAll();
}
