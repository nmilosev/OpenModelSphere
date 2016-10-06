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
 * component. (Explorer on top of the Design Panel)
 */

class LeftLeftMFLayoutManager implements MFLayoutManager {
    private Container root;
    private JPanel desktopCenterPanel = new JPanel();
    private JSplitPane designPanelContainer = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            new EmptyPanel(), new EmptyPanel());
    private JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            designPanelContainer, desktopCenterPanel);

    public void setRootContainer(Container root) {
        this.root = root;
        root.setLayout(new BorderLayout());

        desktopCenterPanel.setLayout(new BorderLayout());
        mainSplitPane.setMinimumSize(new Dimension(30, 30));

        root.add(mainSplitPane, BorderLayout.CENTER);
        designPanelContainer.setResizeWeight(0.5);
        designPanelContainer.setBorder(null);
    }

    public void setDesktop(JDesktopPane desktopPane) {
        desktopCenterPanel.add(desktopPane, BorderLayout.CENTER);
    }

    public void setExplorer(ExplorerPanel explorerPanel) {
        designPanelContainer.setTopComponent(explorerPanel);
    }

    public void setDesignPanel(DesignPanel designPanel) {
        designPanelContainer.setBottomComponent(designPanel);
    }

    public void setPreferredSize(DesignPanel designPanel, final Dimension size) {
        if (size.height > -1) {
            if (designPanelContainer.getHeight() == 0) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        mainSplitPane.doLayout();
                        designPanelContainer.setDividerLocation(designPanelContainer.getHeight()
                                - size.height);
                        designPanelContainer.validate();
                    }
                });
            } else
                designPanelContainer.setDividerLocation(designPanelContainer.getHeight()
                        - size.height);
        }
        if (size.width > -1) {
            mainSplitPane.setDividerLocation(size.width);
            designPanelContainer.validate();
        }
    }

    public void setPreferredSize(ExplorerPanel explorerPanel, Dimension size) {
        if (size.height > -1)
            designPanelContainer.setDividerLocation(size.height);
        if (size.width > -1)
            mainSplitPane.setDividerLocation(size.width);
        designPanelContainer.validate();
    }

    public Dimension getPreferredSize(DesignPanel designPanel) {
        Dimension dim = new Dimension(mainSplitPane.getDividerLocation(), designPanelContainer
                .getSize().height
                - designPanelContainer.getDividerLocation());
        return dim;
    }

    public Dimension getPreferredSize(ExplorerPanel explorerPanel) {
        return new Dimension(mainSplitPane.getDividerLocation(), designPanelContainer
                .getDividerLocation());
    }

    public void removeAll() {
        desktopCenterPanel.removeAll();
        designPanelContainer.removeAll();
        mainSplitPane.removeAll();
    }
}
