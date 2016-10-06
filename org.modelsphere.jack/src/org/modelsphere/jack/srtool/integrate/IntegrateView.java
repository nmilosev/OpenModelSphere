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

package org.modelsphere.jack.srtool.integrate;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.treeTable.*;
import org.modelsphere.jack.awt.treeTable.TreeTable.TreeTableCellRenderer;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class IntegrateView extends TreeTable {
    private static final int ACT_MATCH = -1;
    private static final int ACT_UNMATCH = -2;
    private static final int ACT_REFRESH = -3;

    public static final int ONLY_IN_LEFT = 1;
    public static final int ONLY_IN_RIGHT = 2;
    public static final int OBJECTS_DIFFERS = 3;
    public static final int OBJECTS_EQUALS = 4;

    private static final Color ONLY_IN_LEFT_COLOR = Color.blue;
    private static final Color ONLY_IN_RIGHT_COLOR = new Color(0, 128, 0); // darker green
    private static final Color OBJECTS_DIFFERS_COLOR = Color.red;
    //private static final Color OBJECTS_EQUALS_COLOR   = Color.black;

    private Font nodeFont;
    private Font groupFont;

    public IntegrateView(IntegrateModel model) {
        super(model);
        final TreeTableCellRenderer tree = getTreeRenderer();
        nodeFont = tree.getFont();
        groupFont = nodeFont.deriveFont(Font.ITALIC);
        setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
        TableColumnModel columnModel = getColumnModel();
        columnModel.getColumn(IntegrateModel.COL_LEFT).setPreferredWidth(200);
        columnModel.getColumn(IntegrateModel.COL_RIGHT).setPreferredWidth(150);
        columnModel.getColumn(IntegrateModel.COL_ACTION).setPreferredWidth(100);
        getTableHeader().setUpdateTableInRealTime(false);
        getTableHeader().setReorderingAllowed(false);
        setTreeSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        tree.setShowsRootHandles(true);

        tree.setCellRenderer(new DefaultTreeCellRenderer() {
            public Component getTreeCellRendererComponent(JTree tree, Object value,
                    boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row,
                        hasFocus);
                IntegrateNode node = (IntegrateNode) value;
                setIcon(node.getIcon());
                setFont(node.isGroup() ? groupFont : nodeFont);
                setEnabled(node.isGroup() || node.getLeftDbo() != null);
                if (!selected)
                    setForeground(getDisplayColor(node));
                return this;
            }
        });

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super
                        .getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                                column);
                IntegrateNode node = (IntegrateNode) tree.getPathForRow(row).getLastPathComponent();
                setEnabled(column == IntegrateModel.COL_ACTION || node.getRightDbo() != null);
                if (!isSelected)
                    setForeground(getDisplayColor(node));
                return this;
            }
        };
        columnModel.getColumn(IntegrateModel.COL_RIGHT).setCellRenderer(cellRenderer);
        columnModel.getColumn(IntegrateModel.COL_ACTION).setCellRenderer(cellRenderer);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    int iRow = rowAtPoint(evt.getPoint());
                    if (iRow == -1)
                        return;
                    if (!isRowSelected(iRow))
                        setRowSelectionInterval(iRow, iRow);
                    displayPopup(evt);
                }

            }

            public void mouseReleased(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    int iRow = rowAtPoint(evt.getPoint());
                    if (iRow == -1)
                        return;
                    if (!isRowSelected(iRow))
                        setRowSelectionInterval(iRow, iRow);
                    displayPopup(evt);
                }
            }
        });

    }

    private Color getDisplayColor(IntegrateNode node) {
        Color color = null;
        switch (getDifferenceId(node)) {
        case ONLY_IN_LEFT:
            color = ONLY_IN_LEFT_COLOR;
            break;
        case ONLY_IN_RIGHT:
            color = ONLY_IN_RIGHT_COLOR;
            break;
        case OBJECTS_DIFFERS:
            color = OBJECTS_DIFFERS_COLOR;
            break;
        default:
            color = UIManager.getColor("table.foreground"); //NOT LOCALIZABLE
        }
        return color;
    }

    public final int getDifferenceId(IntegrateNode node) {
        int id = (node.isDifferent() ? OBJECTS_DIFFERS : OBJECTS_EQUALS);
        if (!node.isGroup()) {
            if (node.getLeftDbo() == null)
                id = ONLY_IN_RIGHT; // darker green
            else if (node.getRightDbo() == null)
                id = ONLY_IN_LEFT;
        }
        return id;
    }

    private void displayPopup(MouseEvent evt) {
        IntegrateNode[] nodes = getSelectedNodes();
        TreeTableCellRenderer tree = getTreeRenderer();
        IntegrateModel model = (IntegrateModel) tree.getModel();
        int[] actions = model.getPossibleActions(nodes);
        String[] actionNames = model.getActionNames();
        JPopupMenu popup = new JPopupMenu();
        JMenu actionMenu = new JMenu(LocaleMgr.screen.getString("action"));
        popup.add(actionMenu);
        for (int i = 0; i < actions.length; i++) {
            actionMenu.add(new PopupItem(actionNames[actions[i]], actions[i], nodes));
        }
        PopupItem matchItem = new PopupItem(LocaleMgr.screen.getString("associateDots"), ACT_MATCH,
                nodes);
        PopupItem unmatchItem = new PopupItem(LocaleMgr.screen.getString("deassociate"),
                ACT_UNMATCH, nodes);
        PopupItem refreshItem = new PopupItem(LocaleMgr.screen.getString("refreshProperties"),
                ACT_REFRESH, nodes);
        popup.add(matchItem);
        popup.add(unmatchItem);
        //popup.add(refreshItem);

        IntegrateNode node = nodes[0];
        boolean matched = (node.getLeftDbo() != null && node.getRightDbo() != null);
        if (nodes.length != 1 || node.isGroup()) {
            matchItem.setEnabled(false);
            unmatchItem.setEnabled(false);
        } else {
            matchItem.setEnabled(!matched);
            unmatchItem.setEnabled(matched && node != model.getRoot() && model.isUnmatchable(node));
        }
        refreshItem.setEnabled(nodes.length == 1 && (node.isGroup() || matched));

        popup.show(this, evt.getX(), evt.getY());
    }

    private class PopupItem extends JMenuItem implements ActionListener {
        private IntegrateNode[] nodes;
        private int action;

        PopupItem(String name, int action, IntegrateNode[] nodes) {
            super(name);
            this.action = action;
            this.nodes = nodes;
            addActionListener(this);
        }

        public final void actionPerformed(ActionEvent e) {
            TreeTableCellRenderer tree = getTreeRenderer();
            IntegrateModel model = (IntegrateModel) tree.getModel();
            if (action == ACT_MATCH) {
                model.doMatch(nodes[0]);
                model.getFrame().getLayeredPane().repaint(); // refresh bug when a modal dialog is called from a popup
                model.refreshProperties(null);
            } else if (action == ACT_UNMATCH) {
                model.doUnmatch(nodes[0]);
                model.refreshProperties(null);
            } else if (action == ACT_REFRESH)
                model.refreshProperties(nodes[0]);
            else {
                for (int i = 0; i < nodes.length; i++)
                    model.propagAction(nodes[i], action);
            }
        }
    } // End of class PopupItem

    public final IntegrateNode[] getSelectedNodes() {
        TreeTableCellRenderer tree = getTreeRenderer();
        TreePath[] treePaths = tree.getSelectionPaths();
        if (treePaths == null)
            return new IntegrateNode[0];
        IntegrateNode[] nodes = new IntegrateNode[treePaths.length];
        for (int i = 0; i < treePaths.length; i++)
            nodes[i] = (IntegrateNode) treePaths[i].getLastPathComponent();
        return nodes;
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setGridColor(AwtUtil.darker(getBackground(), 0.9f));

    }

}
