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

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.event.*;
import javax.swing.tree.TreeModel;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

/**
 * <code>DirectoryChooser</code> provides a simple mechanism for the user to choose a directory.
 * 
 * The following code pops up a directory chooser for the user's home directory:
 * 
 * <pre>
 * DirectoryChooser chooser = new DirectoryChooser();
 * int returnVal = chooser.showOpenDialog(parent, &quot;Select&quot;);
 * if (returnVal == JFileChooser.APPROVE_OPTION) {
 *     System.out.println(&quot;You chose to select this file: &quot; + chooser.getSelectedFile().getName());
 * }
 * </pre>
 * 
 * @author Marco Savard
 * 
 */
public final class DirectoryChooser extends AbstractDirectoryChooser {

    private static final int MAX_HEIGHT = 500;
    private static final int MIN_HEIGHT = 400;
    private boolean m_cancelled = true;

    //
    // CONSTRUCTORS
    //

    /**
     * Constructs a <code>DirectoryChooser</code> pointing to the user's home directory.
     */
    public DirectoryChooser() {
        this(null, FileSystemView.getFileSystemView());
    } // end DirectoryChooser()

    /**
     * Constructs a <code>DirectoryChooser</code> using the given <code>FileSystemView</code>.
     * 
     * @param fsv
     *            a given FileSystemView
     */
    public DirectoryChooser(FileSystemView fsv) {
        this(null, fsv);
    } // end DirectoryChooser()

    /**
     * Constructs a <code>DirectoryChooser</code> using the given path. Passing in a
     * <code>null</code> string causes the directory chooser to point to the user's home directory.
     * 
     * @param currentDirectoryPath
     *            a <code>File</code> giving the path to a directory
     */
    public DirectoryChooser(File currentDirectory) {
        this(currentDirectory, FileSystemView.getFileSystemView());
    } // end DirectoryChooser()

    /**
     * Constructs a <code>DirectoryChooser</code> using the given current directory path and
     * <code>FileSystemView</code>.
     * 
     * @param fsv
     *            a given FileSystemView
     * @param currentDirectoryPath
     *            a <code>File</code> giving the path to a directory
     */
    public DirectoryChooser(File currentDirectory, FileSystemView fsv) {
        super(currentDirectory, fsv);
    } // end DirectoryChooser()

    /**
     * Returns the selected directory.
     * 
     * @return the selected directory
     */
    public File getSelectedFile() {
        return m_selectedDirectory;
    }

    private File m_selectedDirectory = null;

    //
    // PRIVATE METHODS
    //
    protected void initialize(JDialog dialog, String approveText) {
        File root = m_currentDirectory;
        String startPath;
        FileSystemModel model;

        try {
            startPath = (root == null) ? null : root.getParentFile().getCanonicalPath();
        } catch (IOException ex) {
            startPath = null;
        } // end try

        if (startPath != null) {
            model = new FileSystemModel(m_filter, startPath);
        } else {
            model = new FileSystemModel(m_filter);
        } // end if

        File parent;
        if (root == null) {
            root = (File) model.getRoot();
            parent = root;
        } else {
            parent = root.getParentFile();
        }

        DirectoryModel directoryModel = new DirectoryModel(root);
        FileSystemTreePanel fileTree = new FileSystemTreePanel(model);

        JScrollPane treeScroller = new JScrollPane(fileTree);
        DirectoryPanel dirPanel = new DirectoryPanel(this, approveText);
        LookinPanel lookinPanel = new LookinPanel(this, dirPanel, fileTree, parent, m_fsv);

        TreeSelectionListener listener = getTreeSelectionListener(directoryModel, dirPanel, this);
        fileTree.setStartPath(listener, model);
        JTree tree = fileTree.getTree();
        tree.addTreeSelectionListener(listener);

        if (parent != root) {
            TreeModel treeModel = tree.getModel();
            int idx = treeModel.getIndexOfChild(parent, root);
            tree.setSelectionRow(idx);
        }

        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(lookinPanel, BorderLayout.NORTH);
        dialog.getContentPane().add(dirPanel, BorderLayout.SOUTH);
        dialog.getContentPane().add(treeScroller, BorderLayout.CENTER);
        dialog.setModal(true);

        if ((m_width == 0) && (m_height == 0)) { // if not set
            dialog.pack();
        } else {
            dialog.setSize(m_width, m_height);
        }

        if (dialog.getHeight() > MAX_HEIGHT)
            dialog.setSize(dialog.getWidth(), MAX_HEIGHT);
        else if (dialog.getHeight() < MIN_HEIGHT)
            dialog.setSize(dialog.getWidth(), MIN_HEIGHT);

    } // end initialize()

    protected void terminate() {
        if (m_cancelled) {
            m_selectedDirectory = null;
        }
    } // end terminate()

    private TreeListener m_treeListener = null;

    protected TreeSelectionListener getTreeSelectionListener(DirectoryModel directoryModel,
            JPanel dirPanel, AbstractDirectoryChooser chooser) {
        if (m_treeListener == null) {
            m_treeListener = new TreeListener(directoryModel, (DirectoryPanel) dirPanel, chooser);
        }

        return m_treeListener;
    } // end getTreeListener()

    protected TreeSelectionListener getTreeSelectionListener() {
        return m_treeListener;
    }

    private void dispose() {
        if (m_currentDialog != null) {
            m_currentDialog.dispose();
        }
    } // end dispose()

    //
    // INNER CLASSES
    //
    static class DirectoryPanel extends AbstractDirectoryChooser.AbstractDirectoryPanel {
        JTextField m_field;
        JButton m_selectBtn;
        JButton m_cancelBtn;
        private final DirectoryChooser m_theDialog;

        DirectoryPanel(DirectoryChooser dialog, String approveText) {
            m_theDialog = dialog;
            GridBagLayout layout = new GridBagLayout();
            this.setLayout(layout);

            JLabel label = new JLabel(DIRECTORY_NAME);
            m_field = new JTextField();
            m_field.setEditable(false);
            m_selectBtn = new JButton(approveText);
            m_selectBtn.setEnabled(false);
            m_cancelBtn = new JButton(CANCEL);
            this.add(label, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));
            this.add(m_field, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.HORIZONTAL, new Insets(6, 6, 6, 6), 0, 0));
            this
                    .add(m_selectBtn, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                            GridBagConstraints.EAST, GridBagConstraints.NONE,
                            new Insets(6, 6, 6, 6), 0, 0));
            this
                    .add(m_cancelBtn, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                            GridBagConstraints.EAST, GridBagConstraints.NONE,
                            new Insets(6, 6, 6, 6), 0, 0));
            addListeners();
        }

        protected void reset() {
            m_field.setText("");
            m_selectBtn.setEnabled(false);
        }

        private void addListeners() {
            m_selectBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    m_theDialog.m_result = JFileChooser.APPROVE_OPTION;
                    m_theDialog.m_cancelled = false;
                    m_theDialog.dispose();
                }
            });

            m_cancelBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    m_theDialog.m_result = JFileChooser.CANCEL_OPTION;
                    m_theDialog.m_selectedDirectory = null;
                    m_theDialog.m_cancelled = true;
                    m_theDialog.dispose();
                }
            });
        }
    } // end DirectoryPanel

    protected static class TreeListener implements TreeSelectionListener {
        DirectoryModel model;
        DirectoryPanel m_dirPanel;
        private final DirectoryChooser m_theDialog;

        public TreeListener(DirectoryModel mdl, DirectoryPanel dirPanel,
                AbstractDirectoryChooser dialog) {
            model = mdl;
            m_dirPanel = dirPanel;
            m_theDialog = (DirectoryChooser) dialog;
        }

        public void valueChanged(TreeSelectionEvent e) {
            File fileSysEntity = (File) e.getPath().getLastPathComponent();
            if (fileSysEntity.isDirectory()) {
                try {
                    String dirName = fileSysEntity.getCanonicalPath();
                    m_theDialog.m_selectedDirectory = fileSysEntity;
                    m_dirPanel.m_field.setText(dirName);
                    m_dirPanel.m_selectBtn.setEnabled(true);
                } catch (IOException ex) {
                    String msg = ex.getLocalizedMessage();
                    JOptionPane.showMessageDialog(m_dirPanel, msg, ERROR_TITLE,
                            JOptionPane.ERROR_MESSAGE);
                } // end try
            } else {
                m_dirPanel.m_field.setText("");
                m_dirPanel.m_selectBtn.setEnabled(false);
            } // end if
        } // end valueChanged()
    } // end TreeListener

    //
    // UNIT TEST
    //
    public static void main(String[] argv) throws IOException {

        JFrame frame = new JFrame();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        DirectoryChooser chooser = new DirectoryChooser(fsv);
        chooser.setSize(400, 400);

        FileFilter filter = new FileFilter() {
            public String getDescription() {
                return "";
            }

            public boolean accept(File file) {
                if (file.isDirectory())
                    return true;

                String name = file.getName();
                int idx = name.lastIndexOf('.');
                if (idx == -1) {
                    return false;
                } else {
                    String ext = name.substring(idx + 1);
                    if (ext.equals("html") || ext.equals("htm")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        };
        chooser.setFileFilter(filter);

        int result = chooser.showDialog(frame, "Select");
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            System.out.println("Selected : " + selectedFile.getCanonicalPath());
        } else {
            System.out.println("Cancelled");
        } // end if

        System.exit(-1);
        // frame.dispose();

    } // end main()

} // end DirectoryExplorer
