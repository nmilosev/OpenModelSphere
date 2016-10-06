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

package org.modelsphere.jack.baseDb.objectviewer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.modelsphere.jack.awt.JackMenu;
import org.modelsphere.jack.awt.StatusBar;
import org.modelsphere.jack.awt.StatusBarModel;

public final class Viewer extends JDialog {
    private VExplorer explorer;

    private StatusBarModel statusBarModel = new StatusBarModel() {
        JLabel messageLabel = new JLabel(" "); // NOT LOCALIZABLE

        public int getComponentCount() {
            return 1;
        }

        public JComponent getComponentAt(int col) {
            switch (col) {
            case 0:
                return messageLabel;
            default:
                return new JLabel(""); // NOT LOCALIZABLE
            }
        }

        public int getWidthAt(int col) {
            switch (col) {
            case 0:
                return StatusBar.RELATIVE_WIDTH;
            default:
                return 1;
            }
        }

        public JComponent getTitleForComponentAt(int col) {
            switch (col) {
            case 0:
                return null;
            default:
                return null;
            }
        }

        public void startWaitingBar(String message) {
        }

        public void startWaitingBar(String message, long timeBeforeStarting) {
        }

        public void stopWaitingBar(String message) {
        }

        public void setMessage(String message) {
            if (message == null || message.length() == 0)
                message = " ";
            messageLabel.setText(message);
            messageLabel.setToolTipText(message);
        }
    };
    private StatusBar statusBar = new StatusBar(statusBarModel);

    private class VMenuBar extends JMenuBar implements ActionListener {
        JackMenu menuFile = new JackMenu("File"); // NOT LOCALIZABLE
        JackMenu menuEdit = new JackMenu("Edit"); // NOT LOCALIZABLE
        JackMenu menuDisplay = new JackMenu("Display"); // NOT LOCALIZABLE
        JMenu menuNew = new JMenu("New"); // NOT LOCALIZABLE
        JMenuItem close = new JMenuItem("Close"); // NOT LOCALIZABLE
        JMenuItem newviewer = new JMenuItem("Window"); // NOT LOCALIZABLE
        JMenuItem newviewerFromSelected = new JMenuItem("Window from Selection"); // NOT

        // LOCALIZABLE

        VMenuBar() {
            add(menuFile);
            add(menuEdit);
            add(menuDisplay);

            menuFile.add(menuNew);
            menuFile.addSeparator();
            menuFile.add(close);

            menuNew.add(newviewer);
            menuNew.add(newviewerFromSelected);

            close.addActionListener(this);
            newviewer.addActionListener(this);
            newviewerFromSelected.addActionListener(this);
        }

        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == close) {
                Viewer.this.dispose();
            } else if (event.getSource() == newviewer) {
                Object root = explorer.getModel().getRoot();
                ArrayList tempRoots = new ArrayList();
                Enumeration enumeration = ((DefaultMutableTreeNode) root).children();
                while (enumeration.hasMoreElements()) {
                    tempRoots.add(((DefaultMutableTreeNode) enumeration.nextElement())
                            .getUserObject());
                }
                Viewer.showViewer(Viewer.this.getParent(), tempRoots.toArray());
            } else if (event.getSource() == newviewerFromSelected) {
                TreePath[] selectedPath = explorer.getSelectionPaths();
                if (selectedPath == null)
                    return;
                ArrayList tempRoots = new ArrayList();
                for (int i = 0; i < selectedPath.length; i++) {
                    Object obj = selectedPath[i].getLastPathComponent();
                    tempRoots.add(((DefaultMutableTreeNode) obj).getUserObject());
                }
                Viewer.showViewer(Viewer.this.getParent(), tempRoots.toArray());
            }
        }
    }

    private Viewer(JFrame parent, Object[] roots) {
        super(parent, "gandalf (alpha)", false); // NOT LOCALIZABLE
        init(roots);
    }

    private Viewer(JDialog parent, Object[] roots) {
        super(parent, "gandalf (alpha)", false); // NOT LOCALIZABLE
        init(roots);
    }

    private void init(Object[] roots) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setJMenuBar(new VMenuBar());

        explorer = new VExplorer(roots);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(explorer), BorderLayout.CENTER);
        contentPane.add(statusBar, BorderLayout.SOUTH);
    }

    public static void showViewer(Component parent, Object[] roots) {
        Viewer viewer = null;
        if (parent instanceof JDialog) {
            viewer = new Viewer((JDialog) parent, roots);
        } else if (parent instanceof JFrame) {
            viewer = new Viewer((JFrame) parent, roots);
        } else
            return;

        viewer.setSize(600, 500);
        viewer.setLocationRelativeTo(parent);
        viewer.setVisible(true);
    }
}
