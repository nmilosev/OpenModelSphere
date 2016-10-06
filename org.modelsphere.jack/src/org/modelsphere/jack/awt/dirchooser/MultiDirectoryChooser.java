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
import javax.swing.tree.*;

import org.modelsphere.jack.international.LocaleMgr;

import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * <code>MultiDirectoryChooser</code> provides a simple mechanism for the user to choose several
 * directories.
 * 
 * @author Marco Savard
 * 
 */
public final class MultiDirectoryChooser extends AbstractDirectoryChooser {
    private static final String SELECTED_DIRECTORIES = LocaleMgr.screen
            .getString("SelectedDirectories");
    private static final String ADD_TO_LIST = LocaleMgr.screen.getString("AddToList");
    private static final String REMOVE_FROM_LIST = LocaleMgr.screen.getString("RemoveFromList");
    private static final String DONE = LocaleMgr.screen.getString("Done");

    private FileSystemTreePanel m_fileTree;
    private DefaultListModel m_listModel = new DefaultListModel();

    //
    // CONSTRUCTORS
    //

    /**
     * Constructs a <code>MultiDirectoryChooser</code> pointing to the user's home directory.
     */
    public MultiDirectoryChooser() {
        this(null, FileSystemView.getFileSystemView());
    } // end DirectoryChooser()

    /**
     * Constructs a <code>MultiDirectoryChooser</code> using the given <code>FileSystemView</code>.
     * 
     * @param fsv
     *            a given FileSystemView
     */
    public MultiDirectoryChooser(FileSystemView fsv) {
        this(null, fsv);
    } // end DirectoryChooser()

    /**
     * Constructs a <code>MultiDirectoryChooser</code> using the given path. Passing in a
     * <code>null</code> string causes the directory chooser to point to the user's home directory.
     * 
     * @param currentDirectoryPath
     *            a <code>File</code> giving the path to a directory
     */
    public MultiDirectoryChooser(File currentDirectory) {
        this(currentDirectory, FileSystemView.getFileSystemView());
    } // end DirectoryChooser()

    /**
     * Constructs a <code>MultiDirectoryChooser</code> using the given current directory path and
     * <code>FileSystemView</code>.
     * 
     * @param fsv
     *            a given FileSystemView
     * @param currentDirectoryPath
     *            a <code>File</code> giving the path to a directory
     */
    public MultiDirectoryChooser(File currentDirectory, FileSystemView fsv) {
        super(currentDirectory, fsv);
    } // end DirectoryChooser()

    /**
     * Returns a list of selected directories.
     */
    public File[] getSelectedFiles() {
        int nb = m_listModel.getSize();
        File[] directories = new File[nb];
        for (int i = 0; i < nb; i++) {
            directories[i] = (File) m_listModel.getElementAt(i);
        }

        return directories;
    }

    public void setSelectedFiles(File[] selectedDirectories) {
        if (selectedDirectories != null) {
            int nb = selectedDirectories.length;
            for (int i = 0; i < nb; i++) {
                m_listModel.addElement(selectedDirectories[i]);
            }
        }
    } // end setSelectedFiles()

    // SIZE PROPERTY
    private int m_width = 0;
    private int m_height = 0;

    public void setSize(int width, int height) {
        m_width = width;
        m_height = height;
    }

    //
    // PRIVATE METHODS
    //
    protected void initialize(JDialog dialog, String approveText) {
        File root = m_currentDirectory;
        FileSystemModel model = new FileSystemModel(m_filter);

        if (root == null) {
            root = (File) model.getRoot();
        }

        DirectoryModel directoryModel = new DirectoryModel(root);
        m_fileTree = new FileSystemTreePanel(model);

        JScrollPane treeScroller = new JScrollPane(m_fileTree);
        DirectoryPanel dirPanel = new DirectoryPanel(this, approveText);
        LookinPanel lookinPanel = new LookinPanel(this, dirPanel, m_fileTree, root, m_fsv);

        TreeSelectionListener listener = getTreeSelectionListener(directoryModel, dirPanel, this);
        m_fileTree.getTree().addTreeSelectionListener(listener);

        dialog.getContentPane().setLayout(new GridBagLayout());

        dialog.getContentPane().add(
                lookinPanel,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.HORIZONTAL, new Insets(6, 6, 6, 6), 0, 0));
        dialog.getContentPane().add(
                treeScroller,
                new GridBagConstraints(0, 1, 1, 1, 1.0, 0.6, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0));
        dialog.getContentPane().add(
                dirPanel,
                new GridBagConstraints(0, 2, 1, 1, 1.0, 0.4, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0));

        dialog.setModal(true);

        if ((m_width == 0) && (m_height == 0)) { // if not set
            dialog.pack();
        } else {
            dialog.setSize(m_width, m_height);
        }
    } // end initialize()

    protected void terminate() {
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
        JList m_directoryList;
        JButton m_addBtn;
        JButton m_removeBtn;
        JButton m_doneBtn;
        JButton m_cancelBtn;
        private final MultiDirectoryChooser m_theDialog;

        DirectoryPanel(MultiDirectoryChooser dialog, String approveText) {
            m_theDialog = dialog;
            GridBagLayout layout = new GridBagLayout();
            this.setLayout(layout);

            JLabel label = new JLabel(SELECTED_DIRECTORIES);
            m_directoryList = new JList(m_theDialog.m_listModel);
            m_addBtn = new JButton(ADD_TO_LIST);
            m_addBtn.setEnabled(false);
            m_removeBtn = new JButton(REMOVE_FROM_LIST);
            m_removeBtn.setEnabled(false);
            m_doneBtn = new JButton(DONE);
            m_doneBtn.setEnabled(false);
            m_cancelBtn = new JButton(CANCEL);
            this
                    .add(m_addBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(6, 6, 6, 6), 0, 0));
            this
                    .add(m_removeBtn, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(6, 6, 6, 6), 0, 0));

            JScrollPane scrollPane = new JScrollPane(m_directoryList);
            this.add(scrollPane, new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 30,
                    30));

            this
                    .add(m_doneBtn, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
                            GridBagConstraints.EAST, GridBagConstraints.NONE,
                            new Insets(6, 6, 6, 6), 0, 0));
            this
                    .add(m_cancelBtn, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                            GridBagConstraints.EAST, GridBagConstraints.NONE,
                            new Insets(6, 6, 6, 6), 0, 0));
            addListeners();
        }

        protected void reset() {
            m_addBtn.setEnabled(false);
        }

        private void addListeners() {
            m_directoryList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent ev) {
                    m_removeBtn.setEnabled(true);
                }
            });

            m_addBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    TreePath selectedPath = m_theDialog.m_fileTree.getTree().getSelectionPath();
                    if (selectedPath != null) {
                        File directory = (File) selectedPath.getLastPathComponent();
                        if (!m_theDialog.m_listModel.contains(directory)) {
                            m_theDialog.m_listModel.addElement(directory);
                            m_doneBtn.setEnabled(true);
                        }
                    }
                }
            });

            m_removeBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    ListModel model = m_directoryList.getModel();
                    Object[] objects = m_directoryList.getSelectedValues();
                    for (int i = 0; i < objects.length; i++) {
                        m_theDialog.m_listModel.removeElement(objects[i]);
                    }
                    m_removeBtn.setEnabled(false);
                    m_doneBtn.setEnabled(true);
                    if (m_theDialog.m_listModel.isEmpty()) {
                        m_doneBtn.setEnabled(false);
                    }
                }
            });

            m_doneBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    m_theDialog.m_result = JFileChooser.APPROVE_OPTION;
                    m_theDialog.dispose();
                }
            });

            m_cancelBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    m_theDialog.m_result = JFileChooser.CANCEL_OPTION;
                    m_theDialog.dispose();
                }
            });
        }
    } // end DirectoryPanel

    protected static class TreeListener implements TreeSelectionListener {
        DirectoryModel model;
        DirectoryPanel m_dirPanel;
        private final MultiDirectoryChooser m_theDialog;

        public TreeListener(DirectoryModel mdl, DirectoryPanel dirPanel,
                AbstractDirectoryChooser dialog) {
            model = mdl;
            m_dirPanel = dirPanel;
            m_theDialog = (MultiDirectoryChooser) dialog;
        }

        public void valueChanged(TreeSelectionEvent e) {
            File fileSysEntity = (File) e.getPath().getLastPathComponent();
            m_dirPanel.m_addBtn.setEnabled(fileSysEntity.isDirectory());
        } // end valueChanged()
    } // end TreeListener

    private static final FileFilter HTML_FILTER = new FileFilter() {
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
                if (ext.equals("html") || ext.equals("htm")) { // NOT
                    // LOCALIZABLE,
                    // just used in
                    // unit test
                    return true;
                } else {
                    return false;
                }
            }
        }
    };

    //
    // UNIT TEST
    //
    public static void main(String[] argv) {

        // load context, if any
        String home = System.getProperty("user.home"); // NOT LOCALIZABLE, unit
        // test
        File contextFile = new File(home, "context.ser"); // NOT LOCALIZABLE,
        // unit test
        File[] selectedDirectories;
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(contextFile));
            selectedDirectories = (File[]) input.readObject();
        } catch (IOException ex) {
            selectedDirectories = null;
        } catch (ClassNotFoundException ex) {
            selectedDirectories = null;
        } // end try

        JFrame frame = new JFrame();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File[] roots = fsv.getRoots();

        // JFileChooser chooser = new JFileChooser(fsv);
        MultiDirectoryChooser chooser = new MultiDirectoryChooser(fsv);
        chooser.setSelectedFiles(selectedDirectories);
        chooser.setSize(400, 400);
        chooser.setFileFilter(HTML_FILTER);

        int result = chooser.showDialog(frame, "Select"); // NOT LOCALIZABLE,
        // unit test
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedDirectories = chooser.getSelectedFiles();
            System.out.println("Selected directories : " + selectedDirectories.length); // NOT LOCALIZABLE, unit test

            // save context
            try {
                ObjectOutputStream output = new ObjectOutputStream(
                        new FileOutputStream(contextFile));
                output.writeObject(selectedDirectories);
                output.close();
            } catch (IOException ex) {
                // ???
            }
        } else {
            System.out.println("Cancelled"); // NOT LOCALIZABLE, unit test
        } // end if

        System.exit(-1);
        // frame.dispose();

    } // end main()

} // end DirectoryExplorer
