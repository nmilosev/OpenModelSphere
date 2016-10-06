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

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.modelsphere.jack.awt.CheckMultiIcon;

/**
 * This renderer will display a node in a CheckableTree. Values (displayed text, icon, selected) are
 * set using a CheckTreeNode. If a node is not a CheckTreeNode, it will be managed like the swing
 * default tree renderer.
 * 
 */
public class CheckRenderer extends DefaultTreeCellRenderer {
    private CheckMultiIcon nonSelectedIconEnabled = new CheckMultiIcon(false, true);
    private CheckMultiIcon selectedIconEnabled = new CheckMultiIcon(true, true);
    private CheckMultiIcon nonSelectedIconDisabled = new CheckMultiIcon(false, false);
    private CheckMultiIcon selectedIconDisabled = new CheckMultiIcon(true, false);
    private int checkIconWidth = UIManager.getIcon("CheckBox.icon").getIconWidth(); // NOT LOCALIZABLE - property

    public CheckRenderer() {
        super();
    }

    public final Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
                hasFocus);
        String stringValue = tree.convertValueToText(value, sel, expanded, leaf, row, hasFocus);
        this.hasFocus = hasFocus;
        setText(stringValue);

        if (sel)
            setForeground(getTextSelectionColor());
        else
            setForeground(getTextNonSelectionColor());

        CheckTreeNode node = ((CheckableTree) tree).getCheckTreeNodeForRow(row);
        boolean enable = false;
        if (node == null) {
            TreePath path = tree.getPathForRow(row);
            if (path != null)
                enable = ((CheckableTree) tree).isEnabled((TreeNode) path.getLastPathComponent());
        } else
            enable = ((CheckableTree) tree).isEnabled(node);

        if (node != null) {
            CheckMultiIcon icon = selectedIconEnabled;
            if (node.isSelected()) {
                icon = enable ? selectedIconEnabled : selectedIconDisabled;

            } else {
                icon = enable ? nonSelectedIconEnabled : nonSelectedIconDisabled;
            }
            icon.setUserIcon(node.getIcon());
            setIcon(icon);
        } else { // default Renderer icons
            if (leaf) {
                setIcon(getLeafIcon());
            } else if (expanded) {
                setIcon(getOpenIcon());
            } else {
                setIcon(getClosedIcon());
            }
        }

        setEnabled(true);
        selected = sel;
        // setComponentOrientation(tree.getComponentOrientation());
        return this;
    }

    public Dimension getPreferredSize() {
        Dimension retDimension = super.getPreferredSize();
        if (retDimension != null) {
            retDimension = new Dimension(retDimension.width + checkIconWidth, Math.max(
                    retDimension.height, this.getDefaultLeafIcon().getIconHeight()));
        }
        return retDimension;
    }

}
