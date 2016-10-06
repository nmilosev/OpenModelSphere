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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.*;

import org.modelsphere.jack.srtool.explorer.ExplorerPanel;
import org.modelsphere.jack.srtool.screen.design.DesignPanel;

/**
 * A DefaultMainFrame components layout manager. Layout the explorer on the left side of the root
 * component. Then layout the design panel on the bottom of the available space.
 */

class LeftBottomMFLayoutManager implements MFLayoutManager {
    private Container root;
    private JPanel desktopCenterPanel = new JPanel();
    private JSplitPane designPanelContainer = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            desktopCenterPanel, new EmptyPanel());
    private JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            new EmptyPanel(), new EmptyPanel());

    public void setRootContainer(Container root) {
        this.root = root;
        root.setLayout(new BorderLayout());

        desktopCenterPanel.setLayout(new BorderLayout());
        mainSplitPane.setMinimumSize(new Dimension(30, 30));

        root.add(mainSplitPane, BorderLayout.CENTER);
        mainSplitPane.setLeftComponent(null);
        mainSplitPane.setRightComponent(designPanelContainer);
    }

    public void setDesktop(JDesktopPane desktopPane) {
        desktopCenterPanel.add(desktopPane, BorderLayout.CENTER);
    }

    public void setExplorer(ExplorerPanel explorerPanel) {
        mainSplitPane.setLeftComponent(explorerPanel);
    }

    public void setDesignPanel(DesignPanel designPanel) {
        designPanelContainer.setBottomComponent(designPanel);
    }

    public void setPreferredSize(DesignPanel designPanel, final Dimension size) {
        if (designPanelContainer.getHeight() > 0) {
            designPanelContainer.setDividerLocation(designPanelContainer.getHeight() - size.height);
            designPanelContainer.validate();
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (designPanelContainer.getHeight() == 0)
                        designPanelContainer.setDividerLocation(root.getHeight() - size.height);
                    else
                        designPanelContainer.setDividerLocation(designPanelContainer.getHeight()
                                - size.height);
                    designPanelContainer.invalidate();
                    mainSplitPane.validate();
                }
            });
        }
    }

    public void setPreferredSize(ExplorerPanel explorerPanel, Dimension size) {
        mainSplitPane.setDividerLocation(size.width);
        mainSplitPane.validate();
    }

    public Dimension getPreferredSize(DesignPanel designPanel) {
        return new Dimension(-1, designPanelContainer.getSize().height
                - designPanelContainer.getDividerLocation());
    }

    public Dimension getPreferredSize(ExplorerPanel explorerPanel) {
        return new Dimension(mainSplitPane.getDividerLocation(), -1);
    }

    public void removeAll() {
        desktopCenterPanel.removeAll();
        designPanelContainer.removeAll();
        mainSplitPane.removeAll();
    }
}
