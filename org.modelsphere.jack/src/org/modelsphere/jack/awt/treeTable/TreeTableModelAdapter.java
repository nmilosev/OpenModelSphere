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

package org.modelsphere.jack.awt.treeTable;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

public class TreeTableModelAdapter extends AbstractTableModel {
    JTree tree;
    TreeTableModel treeTableModel;
    boolean tableDataChanged = false;

    public TreeTableModelAdapter(TreeTableModel treeTableModel, JTree tree) {
        this.tree = tree;
        this.treeTableModel = treeTableModel;

        tree.addTreeExpansionListener(new TreeExpansionListener() {
            public void treeExpanded(TreeExpansionEvent event) {
                delayedFireTableDataChanged();
            }

            public void treeCollapsed(TreeExpansionEvent event) {
                delayedFireTableDataChanged();
            }
        });

        treeTableModel.addTreeModelListener(new TreeModelListener() {
            public void treeNodesChanged(TreeModelEvent e) {
                delayedFireTableDataChanged();
            }

            public void treeNodesInserted(TreeModelEvent e) {
                delayedFireTableDataChanged();
            }

            public void treeNodesRemoved(TreeModelEvent e) {
                delayedFireTableDataChanged();
            }

            public void treeStructureChanged(TreeModelEvent e) {
                delayedFireTableDataChanged();
            }
        });
    }

    public int getColumnCount() {
        return treeTableModel.getColumnCount();
    }

    public String getColumnName(int column) {
        return treeTableModel.getColumnName(column);
    }

    public Class getColumnClass(int column) {
        return treeTableModel.getColumnClass(column);
    }

    public int getRowCount() {
        return tree.getRowCount();
    }

    protected Object nodeForRow(int row) {
        TreePath treePath = tree.getPathForRow(row);
        return treePath.getLastPathComponent();
    }

    public Object getValueAt(int row, int column) {
        return treeTableModel.getValueAt(nodeForRow(row), column);
    }

    public boolean isCellEditable(int row, int column) {
        return treeTableModel.isCellEditable(nodeForRow(row), column);
    }

    public void setValueAt(Object value, int row, int column) {
        treeTableModel.setValueAt(value, nodeForRow(row), column);
    }

    protected void delayedFireTableDataChanged() {
        if (tableDataChanged)
            return;
        tableDataChanged = true;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                tableDataChanged = false;
                fireTableDataChanged();
            }
        });
    }
}
