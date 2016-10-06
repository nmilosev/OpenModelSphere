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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.tree.*;

import org.modelsphere.jack.awt.AbstractTableCellEditor;

/**
 * An expandable/collapsable tree with several columns (like a table). Not currently supported by
 * Swing.
 */
public class TreeTable extends JTable {
    private TreeTableCellRenderer m_tree;

    protected TreeTableCellRenderer getTreeRenderer() {
        return m_tree;
    }

    public TreeTable(TreeTableModel treeTableModel) {
        super();
        m_tree = new TreeTableCellRenderer(treeTableModel); // create the tree
        TableModel model = new TreeTableModelAdapter(treeTableModel, m_tree);
        super.setModel(model);

        ListToTreeSelectionModel selectionWrapper = new ListToTreeSelectionModel(m_tree);
        m_tree.setSelectionModel(selectionWrapper);
        setSelectionModel(selectionWrapper.getListSelectionModel());
        setDefaultRenderer(TreeTableModel.class, m_tree);
        setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());

        setRowHeight(getRowHeight() + 1);

    }

    public void updateUI() {
        super.updateUI();
        if (m_tree != null) {
            m_tree.updateUI();
        }

        LookAndFeel.installColorsAndFont(this, "Tree.background", "Tree.foreground", "Tree.font"); // NOT LOCALIZABLE - property

        InputMap inputMap = this.getInputMap();

        ActionMap actionMap = getActionMap();
        actionMap.put("ToggleJTreeTable_Collapse", new AbstractAction() { // NOT
                    // LOCALIZABLE
                    // -
                    // Action
                    // key

                    public void actionPerformed(ActionEvent e) {
                        if (m_tree != null) {
                            final int selRow = m_tree.getLeadSelectionRow();
                            int[] selRows = TreeTable.this.getSelectedRows();
                            TreePath path = m_tree.getPathForRow(selRow);
                            boolean leaf = true;
                            if (path != null) {
                                TreeModel model = m_tree.getModel();
                                Object component = path.getLastPathComponent();
                                leaf = model.isLeaf(component);
                            }

                            if (selRow != -1 && !leaf) {
                                TreePath aPath = m_tree.getAnchorSelectionPath();
                                TreePath lPath = m_tree.getLeadSelectionPath();

                                if (m_tree.isExpanded(selRow)) {
                                    m_tree.collapseRow(selRow);

                                    m_tree.setAnchorSelectionPath(aPath);
                                    m_tree.setLeadSelectionPath(lPath);
                                    ListSelectionModel model = TreeTable.this.getSelectionModel();

                                    for (int i = 0; i < selRows.length; i++) {
                                        model.addSelectionInterval(selRows[i], selRows[i]);
                                    } // end for
                                } // end if
                            } // end if
                        } // end if
                    } // end actionPerformed()
                });

        actionMap.put("ToggleJTreeTable_Expand", new AbstractAction() { // NOT
                    // LOCALIZABLE
                    // -
                    // Action
                    // key
                    public void actionPerformed(ActionEvent e) {
                        if (m_tree != null) {
                            final int selRow = m_tree.getLeadSelectionRow();
                            int[] selRows = TreeTable.this.getSelectedRows();

                            TreePath path = m_tree.getPathForRow(selRow);
                            boolean leaf = true;
                            if (path != null)
                                leaf = m_tree.getModel().isLeaf(path.getLastPathComponent());
                            if (selRow != -1 && !leaf) {
                                TreePath aPath = m_tree.getAnchorSelectionPath();
                                TreePath lPath = m_tree.getLeadSelectionPath();

                                if (!m_tree.isExpanded(selRow)) {
                                    m_tree.expandRow(selRow);

                                    m_tree.setAnchorSelectionPath(aPath);
                                    m_tree.setLeadSelectionPath(lPath);
                                    ListSelectionModel model = TreeTable.this.getSelectionModel();

                                    for (int i = 0; i < selRows.length; i++) {
                                        model.addSelectionInterval(selRows[i], selRows[i]);
                                    } // end for
                                } // end if
                            } // end if
                        } // end if
                    } // end actionPerformed()
                });

        inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
        inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "ToggleJTreeTable_Collapse"); // NOT LOCALIZABLE - Action key
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "ToggleJTreeTable_Expand"); // NOT LOCALIZABLE - Action key
    } // end updateUI()

    public int getEditingRow() {
        int row = (getColumnClass(editingColumn) == TreeTableModel.class) ? -1 : editingRow;
        return row;
    }

    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);
        if (m_tree != null && m_tree.getRowHeight() != rowHeight) {
            m_tree.setRowHeight(rowHeight);
        }
    }

    public JTree getTree() {
        return m_tree;
    }

    public void setTreeSelectionMode(int mode) {
        m_tree.getSelectionModel().setSelectionMode(mode);
    }

    public void addTreeSelectionListener(TreeSelectionListener tsl) {
        m_tree.addTreeSelectionListener(tsl);
    }

    public class TreeTableCellRenderer extends JTree implements TableCellRenderer {
        protected int visibleRow;

        public TreeTableCellRenderer(TreeModel model) {
            super(model);
        }

        public void updateUI() {
            super.updateUI();
            TreeCellRenderer tcr = getCellRenderer();
            if (tcr instanceof DefaultTreeCellRenderer) {
                DefaultTreeCellRenderer dtcr = ((DefaultTreeCellRenderer) tcr);
                dtcr.setTextSelectionColor(UIManager.getColor("Table.selectionForeground")); // NOT
                // LOCALIZABLE
                // -
                // property
                dtcr.setBackgroundSelectionColor(UIManager.getColor("Table.selectionBackground")); // NOT
                // LOCALIZABLE
                // -
                // property
            }
        }

        public void setRowHeight(int rowHeight) {
            if (rowHeight > 0) {
                super.setRowHeight(rowHeight);
                if (TreeTable.this != null) {
                    if (TreeTable.this.getRowHeight() != rowHeight)
                        TreeTable.this.setRowHeight(rowHeight);
                }
            }
        }

        public void setBounds(int x, int y, int w, int h) {
            super.setBounds(x, 0, w, TreeTable.this.getHeight());
        }

        public void paint(Graphics g) {
            g.translate(0, -visibleRow * getRowHeight());
            super.paint(g);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            visibleRow = row;
            return this;
        }
    }

    public class TreeTableCellEditor extends AbstractTableCellEditor {
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int r, int c) {
            return m_tree;
        }

        public boolean isCellEditable(EventObject e) {
            if (e instanceof MouseEvent) {
                for (int counter = getColumnCount() - 1; counter >= 0; counter--) {
                    if (getColumnClass(counter) == TreeTableModel.class) {
                        MouseEvent me = (MouseEvent) e;
                        MouseEvent newME = new MouseEvent(m_tree, me.getID(), me.getWhen(), me
                                .getModifiers(), me.getX() - getCellRect(0, counter, true).x, me
                                .getY(), me.getClickCount(), me.isPopupTrigger());
                        m_tree.dispatchEvent(newME);
                        break;
                    }
                }
            }
            return false;
        }
    }
}
