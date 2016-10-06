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

package org.modelsphere.jack.baseDb.screen;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.model.DbLookupNode;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModel;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModelListener;
import org.modelsphere.jack.international.LocaleMgr;

/**
 * display a hierarchical lookup dialog of DbObjects
 */
public class DbTreeLookupDialog extends JDialog {

    public static final int SELECT = 1;
    public static final int CANCEL = 2;

    /*
     * public Object selectOneUML(Component comp, String title, DbObject[] roots, MetaClass[]
     * metaClasses, DbTreeModelListener listener, String nullStr, DbObject selDbo){ return
     * DbTreeLookupDialog.selectOne(comp, title, roots, metaClasses, listener, nullStr, selDbo); };
     */

    public Object selectOneUML(Component comp, String title, DbObject[] roots,
            MetaClass[] metaClasses, DbTreeModelListener listener, String nullStr, DbObject selDbo,
            boolean isUML) {
        return DbTreeLookupDialog.selectOne(comp, title, roots, metaClasses, listener, nullStr,
                selDbo);
    }

    private int userAction = CANCEL; // value returned if the user clicks in the
    // Close box
    private JTree tree = new JTree();
    private JButton selectBtn = new JButton(LocaleMgr.screen.getString("Select"));
    private ActionListener cancelAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            userAction = CANCEL;
            dispose();
        }
    };
    private ActionListener selectAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            userAction = SELECT;
            dispose();
        }
    };

    // If nullStr specified, the method returns nullStr if the user selects the
    // nullNode.
    public static Object selectOne(Component comp, String title, DbObject[] roots,
            MetaClass[] metaClasses, DbTreeModelListener listener, String nullStr, DbObject selDbo) {
        try {
            DbTreeModel model = new DbTreeModel(roots, metaClasses, listener, nullStr);
            DbTreeLookupDialog ld = new DbTreeLookupDialog(comp, title, model, false);
            ld.find(selDbo);
            ld.setVisible(true);
            if (ld.getUserAction() == DbTreeLookupDialog.SELECT) {
                DbObject[] selObjs = ld.getSelectedObjects();
                if (selObjs.length > 0)
                    return (selObjs[0] == null ? (Object) nullStr : (Object) selObjs[0]);
            }
            return null;
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(comp, ex);
            return null;
        }
    }

    public static DbObject[] selectMany(Component comp, String title, DbObject[] roots,
            MetaClass[] metaClasses, DbTreeModelListener listener) {
        try {
            DbTreeModel model = new DbTreeModel(roots, metaClasses, listener, null);
            DbTreeLookupDialog ld = new DbTreeLookupDialog(comp, title, model, true);
            ld.setVisible(true);
            return (ld.getUserAction() == DbTreeLookupDialog.SELECT ? ld.getSelectedObjects()
                    : new DbObject[0]);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(comp, ex);
            return new DbObject[0];
        }
    }

    public DbTreeLookupDialog(Component comp, String title, DbTreeModel model,
            boolean multipleSelection) {
        super((comp instanceof Frame ? (Frame) comp : (Frame) SwingUtilities.getAncestorOfClass(
                Frame.class, comp)), title, true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(true);
        tree.setModel(model);
        tree.setShowsRootHandles(true);
        tree.setRootVisible(false);

        final TreeCellRenderer oldRenderer = tree.getCellRenderer();
        TreeCellRenderer newRenderer = new TreeCellRenderer() {
            public Component getTreeCellRendererComponent(JTree tree, Object value,
                    boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component c = oldRenderer.getTreeCellRendererComponent(tree, value, selected,
                        expanded, leaf, row, hasFocus);
                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    Icon icon = ((DbLookupNode) value).toIcon();
                    if (icon != null)
                        label.setIcon(icon);
                    label.setToolTipText(label.getText());
                }
                return c;
            }
        };
        tree.setCellRenderer(newRenderer);

        tree.addTreeWillExpandListener(new TreeWillExpandListener() {
            public void treeWillExpand(TreeExpansionEvent event) {
                DbLookupNode node = (DbLookupNode) event.getPath().getLastPathComponent();
                try {
                    ((DbTreeModel) tree.getModel()).loadChildren(node);
                } catch (Exception ex) {
                    org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                            DbTreeLookupDialog.this, ex);
                }
            }

            public void treeWillCollapse(TreeExpansionEvent event) {
            }
        });

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                manageSelectButton();
            }
        });
        tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    selectBtn.doClick();
                }
            }
        });
        JPanel treePanel = new JPanel(new BorderLayout());
        treePanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));
        treePanel.add(new JScrollPane(tree), BorderLayout.CENTER);

        selectBtn.setEnabled(false);
        selectBtn.addActionListener(selectAction);
        getRootPane().setDefaultButton(selectBtn);
        JButton cancelBtn = new JButton(LocaleMgr.screen.getString("Cancel"));
        cancelBtn.addActionListener(cancelAction);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(5, 7, 6, 6));
        AwtUtil.normalizeComponentDimension(new JButton[] { selectBtn, cancelBtn });
        btnPanel.add(selectBtn);
        btnPanel.add(cancelBtn);

        Container c = getContentPane();
        c.add(treePanel, BorderLayout.CENTER);
        c.add(btnPanel, BorderLayout.SOUTH);

        int selectMode = multipleSelection ? TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION
                : TreeSelectionModel.SINGLE_TREE_SELECTION;
        tree.getSelectionModel().setSelectionMode(selectMode);

        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                tree.requestFocus();
            }
        });
        setSize(Math.max(400, Math.min(600, tree.getPreferredSize().width + 20)), 600);
        setLocationRelativeTo(comp);

        new HotKeysSupport(this, cancelBtn, null);
    }

    private void manageSelectButton() {
        selectBtn.setEnabled(getSelectedObjects().length > 0);
    }

    public final int getUserAction() {
        return userAction;
    }

    public final DbObject[] getSelectedObjects() {
        DbObject[] objs = null;
        TreePath[] selectPaths = tree.getSelectionPaths();
        if (selectPaths != null) {
            int ctr = 0;
            DbObject[] tmpObjs = new DbObject[selectPaths.length];
            for (int i = 0; i < tmpObjs.length; i++) {
                DbLookupNode node = (DbLookupNode) selectPaths[i].getLastPathComponent();
                if (node.isSelectable())
                    tmpObjs[ctr++] = (DbObject) node.getUserObject();
            }
            objs = new DbObject[ctr];
            System.arraycopy(tmpObjs, 0, objs, 0, ctr);
        } else {
            objs = new DbObject[0];
        }
        return objs;
    }

    public final void find(DbObject dbo) throws DbException {
        DbLookupNode node = (DbLookupNode) ((DbTreeModel) tree.getModel()).findNode(dbo);
        if (node != null) {
            TreePath path = new TreePath(node.getPath());
            tree.addSelectionPath(path);
            tree.scrollPathToVisible(path);
        }
    }
}
