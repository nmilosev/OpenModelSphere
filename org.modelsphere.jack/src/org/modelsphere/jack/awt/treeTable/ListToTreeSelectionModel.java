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

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import org.modelsphere.jack.awt.treeTable.TreeTable.TreeTableCellRenderer;

class ListToTreeSelectionModel extends DefaultTreeSelectionModel {
    protected boolean updatingListSelectionModel;
    private TreeTableCellRenderer m_tree;

    public ListToTreeSelectionModel(TreeTableCellRenderer tree) {
        super();
        m_tree = tree;
        getListSelectionModel().addListSelectionListener(createListSelectionListener());
    }

    ListSelectionModel getListSelectionModel() {
        return listSelectionModel;
    }

    public void resetRowSelection() {
        if (!updatingListSelectionModel) {
            updatingListSelectionModel = true;
            try {
                super.resetRowSelection();
            } finally {
                updatingListSelectionModel = false;
            }
        }
    }

    protected ListSelectionListener createListSelectionListener() {
        return new ListSelectionHandler();
    }

    protected void updateSelectedPathsFromSelectedRows() {
        if (!updatingListSelectionModel) {
            updatingListSelectionModel = true;
            try {
                int min = listSelectionModel.getMinSelectionIndex();
                int max = listSelectionModel.getMaxSelectionIndex();

                clearSelection();
                if (min != -1 && max != -1) {
                    for (int counter = min; counter <= max; counter++) {
                        if (listSelectionModel.isSelectedIndex(counter)) {
                            TreePath selPath = m_tree.getPathForRow(counter);

                            if (selPath != null) {
                                addSelectionPath(selPath);
                            }
                        }
                    }
                }
            } finally {
                updatingListSelectionModel = false;
            }
        }
    }

    class ListSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            updateSelectedPathsFromSelectedRows();
        }
    }
}
