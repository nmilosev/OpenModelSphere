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
import java.awt.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import javax.swing.event.TreeSelectionListener;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.international.LocaleMgr;

import java.io.File;
import java.io.IOException;
import java.io.FileFilter;
import java.util.Vector;

//Contains DirectoryChooser's & MultiDirectoryChooser's common stuff
abstract class AbstractDirectoryChooser {

    protected static final String LOOK_IN = LocaleMgr.screen.getString("LookIn");
    protected static final String DIRECTORY_NAME = LocaleMgr.screen.getString("DirectoryName");
    protected static final String CANCEL = LocaleMgr.screen.getString("Cancel");
    protected static final String ERROR_TITLE = LocaleMgr.screen.getString("Error");

    protected JDialog m_currentDialog = null; // null when not activated
    protected File m_currentDirectory;
    protected FileSystemView m_fsv;

    protected AbstractDirectoryChooser(File currentDirectory, FileSystemView fsv) {
        m_currentDirectory = currentDirectory;
        m_fsv = fsv;
    }

    // RESULT PROPERTY
    protected int m_result = JFileChooser.CANCEL_OPTION;

    // SIZE PROPERTY
    protected int m_width = 0;
    protected int m_height = 0;

    public void setSize(int width, int height) {
        m_width = width;
        m_height = height;
    }

    // FILE FILTER PROPERTY
    FileFilter m_filter = null;

    public void setFileFilter(FileFilter filter) {
        m_filter = filter;
    }

    /**
     * Pops a custom directory chooser dialog with a custom approve button. For example, the
     * following code pops up a directory chooser with a "Set Directory" button
     * 
     * <pre>
     * directoryChooser.showDialog(parentDialog, &quot;Set Directory&quot;);
     * </pre>
     * 
     * @param parent
     *            a frame or a dialog containing the directory chooser; can be <code>null</code>
     * @param approveButtonText
     *            the text of the <code>ApproveButton</code>
     * @param title
     *            (optional) the text of the <code>JDialog's title</code>
     * @return the return state of the file chooser on popdown:
     *         <ul>
     *         <li>JFileChooser.CANCEL_OPTION
     *         <li>JFileChooser.APPROVE_OPTION
     *         <li>JFileCHooser.ERROR_OPTION if an error occurs or the dialog is dismissed
     *         </ul>
     * 
     *         Note : returns contains defined in JFileChooser class for interoperability with this
     *         class.
     */
    public int showDialog(Dialog owner, String approveText, String title) {
        m_currentDialog = new DirectoryDialog(owner, title, this);
        initialize(m_currentDialog, approveText);
        AwtUtil.centerWindow(m_currentDialog);
        m_currentDialog.setVisible(true);
        m_currentDialog = null;
        return m_result;
    }

    public int showDialog(Dialog owner, String approveText) {
        int result = this.showDialog(owner, approveText, approveText);
        return result;
    }

    /**
     * Same as showDialog(dialog, approveText), but with a frame as first parameter.
     */
    public int showDialog(Frame owner, String approveText, String title) {
        m_currentDialog = new DirectoryDialog(owner, title, this);
        initialize(m_currentDialog, approveText);
        AwtUtil.centerWindow(m_currentDialog);
        m_currentDialog.setVisible(true);
        m_currentDialog = null;
        return m_result;
    }

    public int showDialog(Frame owner, String approveText) {
        int result = this.showDialog(owner, approveText, approveText);
        return result;
    }

    protected abstract void initialize(JDialog dialog, String approveText);

    protected abstract TreeSelectionListener getTreeSelectionListener(
            DirectoryModel directoryModel, JPanel dirPanel, AbstractDirectoryChooser chooser);

    protected abstract TreeSelectionListener getTreeSelectionListener();

    protected abstract void terminate();

    //
    // INNER CLASSES
    //
    private static final class DirectoryDialog extends JDialog {
        private AbstractDirectoryChooser m_chooser;

        DirectoryDialog(Dialog owner, String approveText, AbstractDirectoryChooser chooser) {
            super(owner, approveText, true);
            m_chooser = chooser;
        }

        DirectoryDialog(Frame owner, String approveText, AbstractDirectoryChooser chooser) {
            super(owner, approveText, true);
            m_chooser = chooser;
        }

        protected void processWindowEvent(WindowEvent e) {
            if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                m_chooser.terminate();
            }
            super.processWindowEvent(e);
        }
    } // end DirectoryDialog

    protected static abstract class AbstractDirectoryPanel extends JPanel {
        protected abstract void reset();
    }

    static class LookinPanel extends JPanel {
        AbstractDirectoryChooser m_theChooser;

        LookinPanel(AbstractDirectoryChooser chooser,
                AbstractDirectoryChooser.AbstractDirectoryPanel dirPanel,
                FileSystemTreePanel fileTree, File root, FileSystemView fsv) {
            m_theChooser = chooser;
            GridBagLayout layout = new GridBagLayout();
            this.setLayout(layout);
            Vector vec = new Vector();
            vec.add(root);
            File[] roots = getRoots(fsv);

            for (int i = 0; i < roots.length; i++) {
                vec.add(roots[i]);
            }

            JLabel label = new JLabel(LOOK_IN);
            JComboBox combo = new JComboBox(vec);
            this.add(label, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));
            this.add(combo, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets(6, 6, 6, 6), 0, 0));
            addListeners(dirPanel, fileTree, combo, root);
        } // end LookinPanel()

        private File[] roots = null;

        private File[] getRoots(FileSystemView fsv) {
            if (roots == null) {
                Vector vec = new Vector();
                if (isWindows()) {
                    vec.add(new File("A:\\"));
                    for (char ch = 'C'; ch <= 'Z'; ch++) {
                        String drive = ch + ":\\";
                        File f = new File(drive);
                        if (f.exists()) {
                            vec.add(f);
                        }
                    } // end for
                    roots = new File[vec.size()];
                    for (int i = 0; i < vec.size(); i++) {
                        roots[i] = (File) vec.elementAt(i);
                    }
                } else { // Not Windwos 2000
                    roots = fsv.getRoots();
                }
            } // end if

            return roots;
        } // end getRoots()

        // IS RUNNING UNDER Windows 2000
        private boolean isWindows() {
            String osName = System.getProperty("os.name"); // NOT LOCALIZABLE,
            // property
            return osName.equals("Windows 2000"); // NOT LOCALIZABLE, os name
        }

        private void addListeners(final AbstractDirectoryChooser.AbstractDirectoryPanel dirPanel,
                final FileSystemTreePanel fileTree, final JComboBox combo, final File root) {
            combo.addItemListener(new ItemListener() {
                private File m_previous = root;

                public void itemStateChanged(ItemEvent ev) {
                    if (ev.getStateChange() == ItemEvent.DESELECTED) {
                        m_previous = (File) ev.getItem();
                    } else if (ev.getStateChange() == ItemEvent.SELECTED) {
                        File root = (File) ev.getItem();
                        try {
                            String startPath = root.getCanonicalPath();
                            TreeSelectionListener tsl = m_theChooser.getTreeSelectionListener();
                            FileSystemModel model = new FileSystemModel(m_theChooser.m_filter,
                                    startPath);
                            fileTree.setStartPath(tsl, model);
                            dirPanel.reset();
                        } catch (IOException ex) {
                            String msg = ex.getLocalizedMessage();
                            JOptionPane.showMessageDialog(combo, msg, ERROR_TITLE,
                                    JOptionPane.ERROR_MESSAGE);
                            combo.setSelectedItem(m_previous);
                        } // end try
                    } // end if
                } // end itemStateChanged()
            });
        } // end addListeners()
    } // end LookinPanel()

} // end AbstractDirectoryChooser
