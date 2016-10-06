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
package org.modelsphere.jack.awt.tree;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.*;
import javax.swing.tree.*;

import org.modelsphere.jack.debug.TestableWindow;
import org.modelsphere.jack.international.LocaleMgr;

/**
 * This class works is a JTree except that it supports the check items
 * 
 */

@SuppressWarnings("serial")
public class CheckableTree extends JTree implements TestableWindow, ActionListener {

    private Rectangle iconRect;
    private JPopupMenu popup;
    private JMenuItem mi;

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("check")) {
            int[] selectedRows = getSelectionRows();
            for (int i = 0; i < selectedRows.length; i++) {
                CheckTreeNode node = getCheckTreeNodeForRow(selectedRows[i]);
                node.setSelected(true);
                ((DefaultTreeModel) this.getModel()).nodeChanged(node);
            }
        }
        if (e.getActionCommand().equals("uncheck")) {
            int[] selectedRows = getSelectionRows();
            for (int i = 0; i < selectedRows.length; i++) {
                CheckTreeNode node = getCheckTreeNodeForRow(selectedRows[i]);
                node.setSelected(false);
                ((DefaultTreeModel) this.getModel()).nodeChanged(node);
            }
        }
    }

    public CheckableTree() {
        this(new DefaultMutableTreeNode());
    }

    public CheckableTree(TreeNode root) {
        this(root, false);
    }

    public CheckableTree(TreeNode root, boolean asksAllowsChildren) {
        this(new CheckTreeModel(root, asksAllowsChildren));
    }

    public CheckableTree(CheckTreeModel newModel) {
        super(newModel);
        init();
    }

    // If we activate editable, we will have a conflict on mouse click event
    public final void setEditable(boolean b) {
        super.setEditable(false);
    }

    // If we activate toggle, we will have a conflict on mouse click event
    public final void setToggleClickCount(int i) {
        super.setToggleClickCount(0);
    }

    private void init() {
        setEditable(false);
        setCellRenderer(new CheckRenderer());
        getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                processMouseClicked(e);
            }
        });

        popup = new JPopupMenu();
        mi = new JMenuItem("Check selection");
        mi.addActionListener(this);
        mi.setActionCommand("check");
        popup.add(mi);
        mi = new JMenuItem("Uncheck selection");
        mi.addActionListener(this);
        mi.setActionCommand("uncheck");
        popup.add(mi);
        popup.setOpaque(true);
        popup.setLightWeightPopupEnabled(true);

        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popup.show((JComponent) e.getSource(), e.getX(), e.getY());
                }
            }
        });
    }

    private void processMouseClicked(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == 0)
            return;
        TreePath clickPath = getPathForLocation(e.getX(), e.getY());
        if (clickPath == null || !isPathSelected(clickPath))
            return;
        if (!(clickPath.getLastPathComponent() instanceof CheckTreeNode))
            return;

        // evaluate click location
        if (iconRect == null) {
            Icon checkIcon = UIManager.getIcon("CheckBox.icon"); // NOT LOCALIZABLE

            if (checkIcon != null) {
                iconRect = new Rectangle(checkIcon.getIconWidth(), checkIcon.getIconHeight());
            } else {
                iconRect = new Rectangle(16, 16);
            }
        }

        Rectangle rect = getRowBounds(this.getRowForPath(clickPath));

        if (rect == null) // should not happen
            return;

        // check if click point is inside the checkIcon
        if ((iconRect.width + ((JComponent) getCellRenderer()).getInsets().left) < (e.getPoint().x - rect.x))
            return;

        boolean newState = !((CheckTreeNode) clickPath.getLastPathComponent()).isSelected();
        TreePath[] paths = getSelectionPaths();
        ArrayList nodeList = new ArrayList();
        int i;
        for (i = 0; i < paths.length; i++) {
            Object node = paths[i].getLastPathComponent();
            if (node instanceof CheckTreeNode)
                nodeList.add(node);
        }
        CheckTreeNode[] nodes = new CheckTreeNode[nodeList.size()];
        nodeList.toArray(nodes);
        for (i = 0; i < nodes.length; i++) {
            nodes[i].setSelected(newState);
            ((CheckTreeModel) getModel()).nodeChanged(nodes[i]);
        }
        repaint();
        fireCheckListener(new CheckEvent(this, nodes));
    }

    public final void addCheckListener(CheckListener l) {
        listenerList.add(CheckListener.class, l);
    }

    public final void removeCheckListener(CheckListener l) {
        listenerList.remove(CheckListener.class, l);
    }

    protected final void fireCheckListener(CheckEvent e) {
        EventListener[] listeners = listenerList.getListeners(CheckListener.class);
        for (int i = listeners.length; --i >= 0;) {
            CheckListener listener = (CheckListener) listeners[i];
            listener.itemChecked(e);
        }
    }

    // Get the CheckTreeNode for the specified location.  Will only return a CheckTreeNode.
    // This method is used by the mouse listener for managing the check-uncheck
    public final CheckTreeNode getCheckTreeNodeForLocation(int x, int y) {
        TreePath path = getPathForLocation(x, y);
        if (path != null) {
            Object component = path.getLastPathComponent();
            return ((component != null) && (component instanceof CheckTreeNode)) ? (CheckTreeNode) component
                    : null;
        }
        return null;
    }

    // Get the CheckTreeNode for the specified row.  If the treenode at the specified row is not
    // an instance of CheckTreeNode, the method will return null.
    // Used by the CheckRenderer.
    public final CheckTreeNode getCheckTreeNodeForRow(int row) {
        TreePath path = getPathForRow(row);
        if (path != null) {
            Object component = path.getLastPathComponent();
            return ((component != null) && (component instanceof CheckTreeNode)) ? (CheckTreeNode) component
                    : null;
        }
        return null;
    }

    // Evaluate if the renderer for a TreeNode should be set enabled.
    public final boolean isEnabled(TreeNode node) {
        if (!isEnabled())
            return false;
        if (node == null)
            return true;
        Object root = this.getModel().getRoot();
        if (node == root)
            return true;
        node = node.getParent();
        while ((node != root) && (node != null)) {
            if (node instanceof CheckTreeNode) {
                if (!((CheckTreeNode) node).isSelected())
                    return false;
            }
            node = node.getParent();
        }
        return true;
    }

    public void updateUI() {
        iconRect = null;
        super.updateUI();
    }

    protected static CheckTreeModel getDefaultModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("JTree", true); // NOT LOCALIZABLE - only used to provide non null root
        DefaultMutableTreeNode parent;

        parent = new DefaultMutableTreeNode("colors", true); // NOT LOCALIZABLE - only used to provide non null root
        root.add(parent);
        parent.add(new CheckTreeNode("blue", false, true)); // NOT LOCALIZABLE - only used to provide non null root
        parent.add(new CheckTreeNode("violet", false, false)); // NOT LOCALIZABLE - only used to provide non null root
        parent.add(new CheckTreeNode("red", false, false)); // NOT LOCALIZABLE - only used to provide non null root
        parent.add(new CheckTreeNode("yellow", false, false)); // NOT LOCALIZABLE - only used to provide non null root

        parent = new CheckTreeNode("sports", true, true); // NOT LOCALIZABLE - only used to provide non null root
        root.add(parent);
        parent.add(new CheckTreeNode("basketball", false, false));// NOT LOCALIZABLE - only used to provide non null root
        parent.add(new CheckTreeNode("soccer", false, true)); // NOT LOCALIZABLE - only used to provide non null root
        parent.add(new CheckTreeNode("football", false, false)); // NOT LOCALIZABLE - only used to provide non null root
        parent.add(new CheckTreeNode("hockey", false, false)); // NOT LOCALIZABLE - only used to provide non null root

        parent = new DefaultMutableTreeNode("planet", true); // NOT LOCALIZABLE - only used to provide non null root
        root.add(parent);
        parent.add(new DefaultMutableTreeNode("saturn", false)); // NOT LOCALIZABLE - only used to provide non null root
        parent.add(new DefaultMutableTreeNode("pluto", false)); // NOT LOCALIZABLE - only used to provide non null root
        parent.add(new DefaultMutableTreeNode("venus", false)); // NOT LOCALIZABLE - only used to provide non null root

        parent = new CheckTreeNode("Test", true, true); // NOT LOCALIZABLE - only used to provide non null root
        root.add(parent);
        parent.add(new DefaultMutableTreeNode("Test-Item1", false)); // NOT LOCALIZABLE - only used to provide non null root
        parent.add(new DefaultMutableTreeNode("Test-Item2", false)); // NOT LOCALIZABLE - only used to provide non null root

        parent = new CheckTreeNode("Dictionnary", true, true); // NOT LOCALIZABLE - only used to provide non null root
        root.add(parent);
        parent.add(new CheckTreeNode("A", false, true)); // NOT LOCALIZABLE - only used to provide non null root
        parent.add(new CheckTreeNode("B", false, false)); // NOT LOCALIZABLE - only used to provide non null root

        CheckTreeNode parent2 = new CheckTreeNode("C", true, false); // NOT LOCALIZABLE - only used to provide non null root
        parent2.add(new CheckTreeNode("Car", false, true)); // NOT LOCALIZABLE - only used to provide non null root
        parent2.add(new CheckTreeNode("Card", false, false)); // NOT LOCALIZABLE - only used to provide non null root
        parent.add(parent2);

        parent2 = new CheckTreeNode("P", true, true); // NOT LOCALIZABLE - only used to provide non null root
        //ImageIcon icon = new ImageIcon(CheckableTree.class.getResource("print.gif"));
        //parent2.add(new CheckTreeNode("Printer", false, true, "Printer", icon)); // NOT LOCALIZABLE - only used to provide non null root
        parent.add(parent2);

        return new CheckTreeModel(root);
    }

    /*
     * ************************************************** DEMO FUNCTION : How to use clickable tree
     * explorer *************************************************
     */

    private static class TreeDemo extends JFrame {
        private JEditorPane htmlPane;

        public TreeDemo() {
            super("TreeDemo"); // NOT LOCALIZABLE - this is a demo

            //Create a tree that allows one selection at a time.
            final CheckableTree tree = new CheckableTree(getDefaultModel());
            //final JTree tree = new JTree(top);
            tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

            //Listen for when the selection changes.
            tree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
                public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                            .getLastSelectedPathComponent();
                    if (node == null)
                        return;

                    //Do something in the HTML panel
                }
            });

            tree.addCheckListener(new CheckListener() {
                public void itemChecked(CheckEvent e) {
                    CheckTreeNode node = e.getNode();
                }
            });

            //Create the scroll pane and add the tree to it.
            JScrollPane treeView = new JScrollPane(tree);

            //Create the HTML viewing pane.
            htmlPane = new JEditorPane();
            htmlPane.setEditable(false);
            //initHelp();
            JScrollPane htmlView = new JScrollPane(htmlPane);

            //Add the scroll panes to a split pane.
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            splitPane.setTopComponent(treeView);
            splitPane.setBottomComponent(htmlView);

            Dimension minimumSize = new Dimension(100, 50);
            htmlView.setMinimumSize(minimumSize);
            treeView.setMinimumSize(minimumSize);
            splitPane.setDividerLocation(100);
            splitPane.setPreferredSize(new Dimension(500, 300));

            //Add the split pane to this frame.
            getContentPane().add(splitPane, BorderLayout.CENTER);
        }
    } //end TreeDemo

    private static void runDemo() {
        TreeDemo frame = new TreeDemo();

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    //IMPLEMENTS TestableWindow
    public Window createTestWindow(Container owner) {
        TreeDemo frame = new TreeDemo();
        return frame;
    }

}
