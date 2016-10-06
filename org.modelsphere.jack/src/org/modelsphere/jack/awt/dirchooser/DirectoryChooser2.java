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

package org.modelsphere.jack.awt.dirchooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.NullFrame;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.util.FileUtils;

import sun.awt.shell.ShellFolder;

public final class DirectoryChooser2 extends JDialog {
    private static final String kDefaultTitle = LocaleMgr.screen.getString("SelectDirectory");

    private static long startInitFileChooser = System.currentTimeMillis();
    private static long finishInitFileChooser = System.currentTimeMillis();
    private JTree m_view;
    private JLabel lookInLabel = new JLabel(LocaleMgr.screen.getString("LookIn"));
    private JComboBox lookIn = new LookInComboBox(/* new DirectoryComboBoxModel() */);
    private JTextField selectedName = new JTextField("");
    private JButton select = new JButton(LocaleMgr.screen.getString("Select"));
    private JButton cancel = new JButton(LocaleMgr.screen.getString("Cancel"));
    private JButton up = new JButton("..");

    // private HashMap iconsMap = new HashMap(80);

    private File selected = null;
    private boolean noModelSwitch = false;
    private boolean showFiles = false;

    private static final Comparator directoryComparator = new DirectoryComparator();

    final static class IndentIcon implements Icon {
        Icon icon = null;
        int depth = 0;

        public void paintIcon(Component c, Graphics g, int x, int y) {
            if (c.getComponentOrientation().isLeftToRight()) {
                icon.paintIcon(c, g, x + depth * 10, y);
            } else {
                icon.paintIcon(c, g, x, y);
            }
        }

        public int getIconWidth() {
            return icon.getIconWidth() + depth * 10;
        }

        public int getIconHeight() {
            return icon.getIconHeight();
        }
    }

    private class LookInComboBox extends JComboBox {
        LookInComboBox() {
            super();
            setEditable(false);
            setRenderer(new DirectoryComboBoxRenderer());
        }

        LookInComboBox(DirectoryComboBoxModel model) {
            super(model);
            setEditable(false);
            setRenderer(new DirectoryComboBoxRenderer());
        }
    }

    final class DirectoryComboBoxRenderer extends DefaultListCellRenderer {
        IndentIcon ii = new IndentIcon();

        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value == null) {
                setText("");
                return this;
            }
            File directory = (File) value;
            setText(FileSystemView.getFileSystemView().getSystemDisplayName(directory));
            // Icon icon =
            // FileSystemView.getFileSystemView().getSystemIcon(directory);
            Icon icon = FileUtils.getIcon(directory);
            /*
             * Icon icon = (Icon)iconsMap.get(iconsMap); if (icon == null){ icon =
             * basicChooser.getIcon(directory); iconsMap.put(directory, icon); }
             */
            ii.icon = icon;
            ii.depth = ((DirectoryComboBoxModel) list.getModel()).getDepth(index);
            setIcon(ii);
            return this;
        }
    }

    final class DirectoryComboBoxModel extends AbstractListModel implements ComboBoxModel {
        Vector directories = new Vector();
        int[] depths = null;
        File selectedDirectory = null;

        public DirectoryComboBoxModel() {
            File dir = selected == null ? FileSystemView.getFileSystemView().getRoots()[0]
                    : selected;
            if (dir != null) {
                addItem(dir);
            }
        }

        private void addItem(File directory) {
            if (directory == null) {
                return;
            }
            directories.clear();
            FileSystemView fsv = FileSystemView.getFileSystemView();
            File[] baseFolders = (File[]) ShellFolder.get("fileChooserComboBoxFolders"); // NOT LOCALIZABLE
            directories.addAll(Arrays.asList(baseFolders));

            File canonical = null;
            try {
                canonical = directory.getCanonicalFile();
            } catch (IOException e) {
                // Maybe drive is not ready. Can't abort here.
                canonical = directory;
            }

            File sf = canonical;
            File f = sf;
            Vector path = new Vector(10);
            do {
                path.addElement(f);
            } while ((f = f.getParentFile()) != null);

            int pathCount = path.size();
            // Insert chain at appropriate place in vector
            for (int i = 0; i < pathCount; i++) {
                f = (File) path.get(i);
                if (directories.contains(f)) {
                    int topIndex = directories.indexOf(f);
                    for (int j = i - 1; j >= 0; j--) {
                        directories.insertElementAt(path.get(j), topIndex + i - j);
                    }
                    break;
                }
            }
            calculateDepths();
            setSelectedItem(sf);
        }

        private void calculateDepths() {
            depths = new int[directories.size()];
            for (int i = 0; i < depths.length; i++) {
                File dir = (File) directories.get(i);
                File parent = dir.getParentFile();
                depths[i] = 0;
                if (parent != null) {
                    for (int j = i - 1; j >= 0; j--) {
                        if (parent.equals((File) directories.get(j))) {
                            depths[i] = depths[j] + 1;
                            break;
                        }
                    }
                }
            }
        }

        public int getDepth(int i) {
            return (depths != null && i >= 0 && i < depths.length) ? depths[i] : 0;
        }

        public void setSelectedItem(Object selectedDirectory) {
            this.selectedDirectory = (File) selectedDirectory;
            if (this.selectedDirectory == null && directories.size() > 0)
                this.selectedDirectory = (File) directories.get(0);
            fireContentsChanged(this, -1, -1);
        }

        public Object getSelectedItem() {
            return selectedDirectory;
        }

        public int getSize() {
            return directories.size();
        }

        public Object getElementAt(int index) {
            return directories.elementAt(index);
        }
    }

    private class DirectoryChooserTree extends JTree implements TreeWillExpandListener,
            TreeSelectionListener {
        DirectoryChooserTree(File selected) {
            setRootVisible(false);
            // Add roots (in Windows, we have one root, the Desktop)
            File[] roots = FileSystemView.getFileSystemView().getRoots();
            // TODO: Add 'selected' file into roots?

            setModel(new DirectoryChooserModel(roots, showFiles));
            addTreeWillExpandListener(this);
            setCellRenderer(createTreeCellRenderer());
            getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            addTreeSelectionListener(this);
            setScrollsOnExpand(true);
            // this.setRowHeight(getRowHeight() + 2);
        }

        public void treeWillExpand(TreeExpansionEvent event) {
            TreeNode node = (TreeNode) event.getPath().getLastPathComponent();
            try {
                ((DirectoryChooserModel) getModel()).loadChildren(node);
            } catch (Exception ex) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(this, ex);
            }
        }

        public void treeWillCollapse(TreeExpansionEvent event) {
        }

        public void valueChanged(TreeSelectionEvent e) {
            Object selectedPath = getLastSelectedPathComponent();
            if (selectedPath instanceof DirectoryTreeNode) {
                selected = (File) ((DirectoryTreeNode) selectedPath).getUserObject();
                if (!noModelSwitch)
                    lookIn.setModel(new DirectoryComboBoxModel());
                selectedName.setText(selected == null ? "" : selected.toString());
            }
        }

        protected TreeCellRenderer createTreeCellRenderer() {
            TreeCellRenderer newRenderer = new DefaultTreeCellRenderer() {
                public Component getTreeCellRendererComponent(JTree tree, Object value,
                        boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                    Component c = super.getTreeCellRendererComponent(tree, value, selected,
                            expanded, leaf, row, hasFocus);
                    if (c instanceof JLabel) {
                        JLabel label = (JLabel) c;
                        label.setToolTipText(label.getText());
                        if (value instanceof FileTreeNode) {
                            FileTreeNode node = (FileTreeNode) value;
                            label.setToolTipText(node.toString());
                            label.setIcon(node.getIcon());
                            if (value instanceof DirectoryTreeNode) {
                                setForeground(selected ? getTextSelectionColor()
                                        : getTextNonSelectionColor());
                            } else {
                                setForeground(UIManager.getColor("Label.disabledForeground"));
                                // label.setIcon(new
                                // ImageIcon(GrayFilter.createDisabledImage(((ImageIcon)node.getIcon()).getImage())));
                            }
                        } else {
                            setForeground(UIManager.getColor("Label.foreground"));
                            label.setIcon(null);
                        }
                    }

                    return c;
                }
            };
            return newRenderer;
        }
    }

    private long timeinload = 0;

    private class DirectoryChooserModel extends DefaultTreeModel {
        boolean showFiles = false;

        DirectoryChooserModel(File[] roots, boolean showFiles) {
            super(new DirectoryTreeRootNode(), true);
            this.showFiles = showFiles;
            for (int i = 0; roots != null && i < roots.length; i++) {
                ((DirectoryTreeRootNode) getRoot()).add(new DirectoryTreeNode(roots[i]));
            }
        }

        void loadChildren(TreeNode node) {
            if (!(node instanceof DirectoryTreeNode) || !node.getAllowsChildren())
                return;
            if (((FileTreeNode) node).loaded)
                return;
            File file = (File) ((FileTreeNode) node).getUserObject();
            if (file == null) {
                ((FileTreeNode) node).loaded = true;
                return;
            }

            if (!file.canRead()) {
                ((FileTreeNode) node).loaded = true;
                return;
            }
            // should not happen - already flagged has loaded when directory
            // expanded
            if (!file.isDirectory()) {
                ((FileTreeNode) node).loaded = true;
                return;
            }

            long start = System.currentTimeMillis();
            if (node instanceof DirectoryTreeNode && !file.isFile()) {
                File[] children = file.listFiles();
                if (children != null)
                    Arrays.sort(children, directoryComparator);
                for (int i = 0; children != null && i < children.length; i++) {
                    if (FileSystemView.getFileSystemView().isDrive(children[i])) {
                        ((DirectoryTreeNode) node).add(new DirectoryTreeNode(children[i]));
                        continue;
                    }
                    if (children[i].isDirectory()) {
                        ((FileTreeNode) node).add(new DirectoryTreeNode(children[i]));
                    } else if (showFiles) {
                        FileTreeNode child = new FileTreeNode(children[i]);
                        ((FileTreeNode) node).add(child);
                        child.loaded = true;
                    }
                }
            }
            ((FileTreeNode) node).loaded = true;

            timeinload += (System.currentTimeMillis() - start);
        }

    }

    private static class DirectoryComparator implements Comparator {
        DirectoryComparator() {
        }

        public int compare(Object o1, Object o2) {
            if (!(o1 instanceof File) || !(o2 instanceof File))
                return -200;
            if (FileSystemView.getFileSystemView().isDrive((File) o1))
                return -150;
            if (FileSystemView.getFileSystemView().isDrive((File) o1))
                return 150;
            if ((((File) o1).isDirectory() && ((File) o2).isDirectory())
                    || (!((File) o1).isDirectory() && !((File) o2).isDirectory())) {
                return ((File) o1).compareTo((File) o2);
            }
            if (((File) o1).isDirectory()) {
                return -100;
            }
            return 100;
        }

        public boolean equals(Object obj) {
            return false;
        }
    }

    private class DirectoryTreeNode extends FileTreeNode {
        DirectoryTreeNode(File file) {
            super(file, true);
        }
    }

    private class FileTreeNode extends DefaultMutableTreeNode {
        private String display = null;
        private Icon icon = null;
        boolean loaded = false;

        FileTreeNode(File file, boolean allowChildren) {
            super(file, allowChildren);
            display = FileSystemView.getFileSystemView().getSystemDisplayName(file);
            // icon = FileSystemView.getFileSystemView().getSystemIcon(file);
            icon = FileUtils.getIcon(file);
            /*
             * icon = (Icon)iconsMap.get(file); if (icon == null){ icon =
             * basicChooser.getIcon(file); iconsMap.put(file, icon); }
             */
        }

        FileTreeNode(File file) {
            this(file, false);
        }

        public String toString() {
            return display;
        }

        public Icon getIcon() {
            return icon;
        }

    }

    private class DirectoryTreeRootNode extends DefaultMutableTreeNode {
        DirectoryTreeRootNode() {
            super("Root", true); // NOT LOCALIZABLE
        }
    }

    private DirectoryChooser2(JDialog parent, File selected, boolean fileVisible) {
        super(parent, true);
        init(selected, fileVisible);
    }

    private DirectoryChooser2(JFrame parent, File selected, boolean fileVisible) {
        super(parent, true);
        init(selected, fileVisible);
    }

    private void init(File selected, boolean showFiles) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.showFiles = showFiles;
        m_view = new DirectoryChooserTree(selected);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        buttonPanel.add(select, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 12, 12, 6), 0, 0));
        buttonPanel.add(cancel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 0, 12, 12), 0, 0));

        Container contentpane = getContentPane();
        contentpane.setLayout(new GridBagLayout());
        contentpane.add(lookInLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(12, 12, 6, 6), 0, 0));
        contentpane.add(lookIn, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(12, 0, 6, 12), 0, 0));
        // contentpane.add(up, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
        // GridBagConstraints.WEST, GridBagConstraints.BOTH,
        // new Insets(12,0,6,12), 0, 0));
        contentpane
                .add(new JScrollPane(m_view), new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0,
                        GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 12, 6, 12),
                        40, 60));
        contentpane.add(selectedName, new GridBagConstraints(0, 2, 3, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 12, 6, 12), 0, 0));
        contentpane.add(buttonPanel, new GridBagConstraints(0, 3, 3, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        getRootPane().setDefaultButton(select);

        noModelSwitch = true;
        selectedName.setEditable(false);
        select(selected);

        lookIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lookIn((File) lookIn.getSelectedItem());
            }
        });
        lookIn.setSelectedItem(this.selected);

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                result = CANCEL;
                dispose();
            }
        });
        select.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                result = OK;
                dispose();
            }
        });
        up.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (DirectoryChooser2.this.selected == null)
                    return;
                File parent = DirectoryChooser2.this.selected.getParentFile();
                if (parent == null)
                    return;
                select(parent);
            }
        });
        noModelSwitch = false;
    }

    private void lookIn(File file) {
        if (noModelSwitch)
            return;
        m_model = null;
        m_view.setModel(new DirectoryChooserModel(new File[] { file }, showFiles));
        if (m_view.getRowCount() > 0) {
            m_view.expandRow(0);
        }
    }

    // TODO :very slow (1.5 second), need to be optimized
    private void select(File file) {
        if ((selected == file || (file != null && !file.isDirectory()) || (selected != null
                && file != null && selected.equals(file)))
                && (file == null || (file != null && file.exists()))) {
            return;
        }

        if (file != null && !file.exists()) {
            File[] roots = FileSystemView.getFileSystemView().getRoots();
            file = roots != null && roots.length > 0 ? roots[0] : new File(System
                    .getProperty("user.dir"));
        }

        selected = file;
        lookIn.setModel(new DirectoryComboBoxModel()); // 110 ms

        if (m_view.getRowCount() > 0)
            m_view.expandRow(0); // 120 ms
        selectedName.setText(selected == null ? "" : selected.toString());

        // select in tree
        FileTreeNode selectedNode = getNode(selected); // 700 ms

        if (selectedNode != null) {
            TreePath path = new TreePath(selectedNode.getPath()); // 0 ms
            // m_view.expandPath(path); //30 ms
            m_view.setSelectionPath(path); // 150 ms
            m_view.scrollPathToVisible(path); // 10 ms
            // m_view.makeVisible(path);
        }
    }

    // Note : 700 ms to run, needs to be optimized
    private DirectoryChooserModel m_model = null; // set at instantiation i/o
    // one each getNode() call
    private final ArrayList m_parentFiles = new ArrayList(); // set at

    // instantiation
    // i/o one each
    // getNode()
    // call
    private FileTreeNode getNode(File file) {
        long start = System.currentTimeMillis();
        if (m_model == null) {
            m_model = (DirectoryChooserModel) m_view.getModel();
        }

        // Build the directory path from 'root' to 'file' (110ms)
        File parentF = file;
        DirectoryTreeRootNode root = (DirectoryTreeRootNode) m_model.getRoot();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        while (parentF != null) {
            m_parentFiles.add(parentF);
            parentF = fsv.getParentDirectory(parentF); // File.getParent is
            // faster? ... maybe but
            // does not provide the
            // native hierarchical
            // view (GP)
        }

        // Retrieve the node associated to 'file' in the while file tree (500
        // ms)
        FileTreeNode node = null;
        int nb = m_parentFiles.size();
        for (int i = nb - 1; i > -1; i--) {
            File parentFile = (File) m_parentFiles.get(i);
            DefaultMutableTreeNode parentNode = (node == null) ? (DefaultMutableTreeNode) root
                    : node;
            m_model.loadChildren(parentNode);
            int count = parentNode.getChildCount();
            for (int j = 0; j < count; j++) {
                FileTreeNode child = (FileTreeNode) parentNode.getChildAt(j);
                Object obj = child.getUserObject();
                if (obj.equals(parentFile)) {
                    node = child;
                    break;
                }
            } // end for
        } // end for
        m_parentFiles.clear(); // for furthur use

        // System.out.println("getNode: " + ( System.currentTimeMillis() -
        // start));
        if (node != null && node.getUserObject().equals(file)) {
            return node;
        }
        return null;
    }

    private static final int CANCEL = 1;
    private static final int OK = 2;
    private int result = 0;

    private int showDialog() {
        pack();
        Dimension dim = getSize();
        setSize(dim.width + 100, dim.height);
        AwtUtil.centerWindow(this);
        setVisible(true);
        dispose();
        if (result == 0) // Diag default close operation
            result = CANCEL;
        return result;
    }

    public static File selectDirectory(Component parent, File selected, String title,
            String selectText, boolean showFiles) {
        DirectoryChooser2 chooser = null;
        long start = System.currentTimeMillis();
        Debug.assert2(
                (parent instanceof JFrame) || (parent instanceof JDialog) || (parent == null),
                "DirectoryChooser: Invalid Parent JComponent"); // NOT
        // LOCALIZABLE
        if (parent instanceof JFrame)
            chooser = new DirectoryChooser2((JFrame) parent, selected, showFiles);
        else if (parent instanceof JDialog)
            chooser = new DirectoryChooser2((JDialog) parent, selected, showFiles);
        else if (parent == null)
            chooser = new DirectoryChooser2(NullFrame.singleton, selected, showFiles);
        else
            return selected;
        // System.out.println("Time in LoadChildren: " + chooser.timeinload);
        // System.out.println("Total: " + ( System.currentTimeMillis() -
        // start));
        // configure
        if (title != null)
            chooser.setTitle(title);
        if (selectText != null)
            chooser.select.setText(selectText);

        int result = chooser.showDialog();

        if (result == CANCEL)
            return null;

        return chooser.selected;
    }

    public static File selectDirectory(Component parent, File selected, String title) {
        return selectDirectory(parent, selected, title, null, false);
    }

    public static File selectDirectory(Component parent, File selected) {
        return selectDirectory(parent, selected, null, null, false);
    }

    public static File selectDirectory(Component parent, File selected, String title,
            String selectText) {
        return selectDirectory(parent, selected, title, selectText, false);
    }

    //
    // UNIT TEST
    //
    public static void main(String[] argv) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        JFrame frame = new JFrame();
        // System.out.println("Init File Chooser (once): " +
        // (finishInitFileChooser - startInitFileChooser));
        // File file = new
        // File("C:/java/modelsphere21/src/org/modelsphere/jack/awt");
        File file = new File("C:/java/modelsphere21");
        for (int i = 0; i < 5; i++) {
            File selectedFile = DirectoryChooser2.selectDirectory(frame, file, "Browse", "Select",
                    true);
            selectedFile = DirectoryChooser2
                    .selectDirectory(frame, file, "Browse", "Select", false);
        }
        System.exit(0);
    } // end main()
} // end DirectoryChooser2
