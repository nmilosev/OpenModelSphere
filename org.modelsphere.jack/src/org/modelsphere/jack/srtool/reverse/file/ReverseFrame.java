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

package org.modelsphere.jack.srtool.reverse.file;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.util.DirectoryScanner;

public abstract class ReverseFrame extends JDialog {
    private static final String HELP = org.modelsphere.jack.international.LocaleMgr.screen
            .getString("help");

    protected ReversePlugins plugins = null;
    protected boolean isDone = false;
    protected boolean canceledByUser = false;
    DefaultListModel m_fileModel = new DefaultListModel();
    DefaultListModel m_packageModel = new DefaultListModel();
    JList fileList = new JList(m_fileModel);
    JList packageList = new JList(m_packageModel);

    transient protected static String filesPath = ApplicationContext.getDefaultWorkingDirectory();
    transient protected static String dirPath = ApplicationContext.getDefaultWorkingDirectory();

    Enumeration m_files = null;
    Enumeration m_packages = null;
    protected DefaultListModel m_model = new DefaultListModel();
    protected Vector fileVector;

    // context
    protected JButton helpBtn = new JButton();
    // context
    protected JButton addFilesButton = new JButton();
    protected JButton addPackagesButton = new JButton();
    protected JButton removeFilesButton = new JButton();
    protected JButton removePackagesButton = new JButton();
    protected JButton cancelButton = new JButton();
    protected JButton reverseButton = new JButton();
    protected JLabel filesListTitle = new JLabel();
    protected JLabel packagesListTitle = new JLabel();

    MouseListener mouseListener4Files = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            int ind = fileList.getSelectedIndex();
            if (ind > -1)
                removeFilesButton.setEnabled(true);
            else
                removeFilesButton.setEnabled(false);
        }
    };

    MouseListener mouseListener4Packages = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            int ind = packageList.getSelectedIndex();
            if (ind > -1)
                removePackagesButton.setEnabled(true);
            else
                removePackagesButton.setEnabled(false);
        }
    };

    private String subTitle;
    protected SourceChooser m_FileChooser;
    protected SourceChooser m_PackageChooser;
    protected FileListPreviewer fileListToAdd;
    protected FileListPreviewer PackageListToAdd;
    protected Vector filterVector = new Vector();
    protected JPanel sourcePanel;

    protected abstract void init();

    public ReverseFrame(JFrame owner, Vector aFileVector, JPanel aPanel) {
        super(owner, LocaleMgr.screen.getString("ReverseEngineering"), true);
        sourcePanel = aPanel;
        fileVector = aFileVector;
        init();
    }

    protected void jbInit() {
        JTabbedPane centerPanel = new JTabbedPane();
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        setLabels();
        this.getContentPane().setLayout(new BorderLayout());
        this.setSize(new Dimension(600, 400));

        fileList.addMouseListener(mouseListener4Files);
        packageList.addMouseListener(mouseListener4Packages);
        centerPanel.addTab(LocaleMgr.screen.getString("FilesSelecTabName"), getCenterPanel());

        Iterator iter = plugins.getReverseObjectList().iterator();
        while (iter.hasNext()) {
            JackReverseEngineeringPlugin rev = (JackReverseEngineeringPlugin) iter.next();
            ReverseParameters revParam = rev.getRevParameters();
            if (revParam != null) {
                JPanel revParamTab = revParam.getParametersTab();
                if (revParamTab != null) {
                    String revParamTabName = revParam.getParametersTabName();
                    if (revParamTabName == null)
                        revParamTabName = rev.getDisplayName();
                    centerPanel.addTab(revParamTabName, revParamTab);
                }
            }
        } // end while
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(getControlPanel(), BorderLayout.SOUTH);
    }

    private final void setLabels() {
        // enabled help if mainframe found
        DefaultMainFrame mainframe = ApplicationContext.getDefaultMainFrame();
        if (mainframe != null) {
            helpBtn.setEnabled(mainframe.isHelpInstalled());
        } // end if

        helpBtn.setText(HELP);
        addFilesButton.setText(LocaleMgr.screen.getString("AddFiles"));
        addFilesButton.setMnemonic(LocaleMgr.screen.getMnemonic("AddFiles"));
        addFilesButton.setToolTipText(LocaleMgr.screen.getToolTip("AddFiles"));

        addPackagesButton.setText(LocaleMgr.screen.getString("AddDirectories"));
        addPackagesButton.setMnemonic(LocaleMgr.screen.getMnemonic("AddDirectories"));
        addPackagesButton.setToolTipText(LocaleMgr.screen.getToolTip("AddDirectories"));

        removeFilesButton.setText(LocaleMgr.screen.getString("Remove"));
        removeFilesButton.setMnemonic(LocaleMgr.screen.getMnemonic("Remove"));
        removeFilesButton.setToolTipText(LocaleMgr.screen.getToolTip("Remove"));

        removePackagesButton.setText(LocaleMgr.screen.getString("Remove"));
        removePackagesButton.setMnemonic(LocaleMgr.screen.getMnemonic("Remove"));
        removePackagesButton.setToolTipText(LocaleMgr.screen.getToolTip("Remove"));

        cancelButton.setText(LocaleMgr.screen.getString("Cancel"));
        cancelButton.setToolTipText(LocaleMgr.screen.getToolTip("Cancel"));

        reverseButton.setText(LocaleMgr.screen.getString("Reverse"));
        reverseButton.setMnemonic(LocaleMgr.screen.getMnemonic("Reverse"));
        reverseButton.setToolTipText(LocaleMgr.screen.getToolTip("Reverse"));

        filesListTitle.setText(LocaleMgr.screen.getString("ListFilesToReverse"));
        packagesListTitle.setText(LocaleMgr.screen.getString("ListDirectoriesToReverse"));
    }

    private final JPanel getCenterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel packageListPanel = new JPanel(new BorderLayout());
        JPanel packageButtonPanel = new JPanel();
        JPanel fileListPanel = new JPanel(new BorderLayout());
        JPanel fileButtonPanel = new JPanel();

        // Packages
        packageListPanel.add(packagesListTitle, BorderLayout.NORTH);
        packageListPanel.add(new JScrollPane(packageList), BorderLayout.CENTER);
        panel
                .add(packageListPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(12, 12, 6, 6), 0, 0));
        panel.add(packageButtonPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 1), 0, 0));
        packageButtonPanel.add(addPackagesButton);
        packageButtonPanel.add(removePackagesButton);

        // Files
        fileListPanel.add(filesListTitle, BorderLayout.NORTH);
        fileListPanel.add(new JScrollPane(fileList), BorderLayout.CENTER);
        panel
                .add(fileListPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(12, 6, 6, 10), 0, 0));
        panel.add(fileButtonPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
        fileButtonPanel.add(addFilesButton);
        fileButtonPanel.add(removeFilesButton);

        return panel;
    }

    private final JPanel getControlPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 7, 7));
        buttonPanel.add(reverseButton);
        buttonPanel.add(cancelButton);
        // HIDEHELPforV1//buttonPanel.add(helpBtn);
        return buttonPanel;
    }

    // context
    void helpBtn_actionPerformed(ActionEvent e) {
        ApplicationContext.getDefaultMainFrame().displayJavaHelp("reverse"); // NOT
        // LOCALIZABLE
    }

    // context

    protected abstract void terminate(boolean canceledByUser);

    void cancelButton_actionPerformed(ActionEvent e) {
        // m_can_continue = false;
        isDone = true;
        terminate(true);
    }

    // find the approveButton from open dialog
    private JButton getApproveButton(java.awt.Container parent, String label) {
        JButton ab = null;
        for (int i = 0; ab == null && i < parent.getComponentCount(); i++) {
            java.awt.Component comp = parent.getComponent(i);
            if (comp instanceof JButton) {
                String text = ((JButton) comp).getText();
                if (text != null && text.equals(label))
                    ab = (JButton) comp;
            } else if (comp instanceof java.awt.Container)
                ab = getApproveButton((java.awt.Container) comp, label);
        }
        return ab;
    }

    void addFilesButton_actionPerformed(ActionEvent e) {
        ((FileListPreviewer) m_FileChooser.getAccessory()).clearFilesFromList();
        m_FileChooser.setApproveButtonText(LocaleMgr.screen.getString("AddtoReverse"));
        // problem
        // m_FileChooser.setApproveButtonMnemonic(LocaleMgr.screen.getString("AddtoRevMnemonic").charAt(0));
        // m_FileChooser.setApproveButtonToolTipText(LocaleMgr.screen.getString("AddtoRevTooltip"));
        String label = LocaleMgr.screen.getString("AddtoReverse");
        JButton button = getApproveButton(m_FileChooser, label);
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                m_FileChooser.setApproveMethod(SourceChooser.BUTTONAPPROVE);
                File[] files = { m_FileChooser.getSelectedFile() };
                // File[] files = m_FileChooser.getSelectedFiles(); //multiple
                // selection
                DefaultListModel fileToAdd = ((FileListPreviewer) m_FileChooser.getAccessory())
                        .getFilesFromList();
                for (int i = 0; i < files.length; i++) {
                    if (files[i] != null) {
                        if (files[i].exists())
                            fileToAdd.addElement(files[i]);
                    }
                }
                for (int i = 0; i < fileToAdd.size(); i++) {
                    if (m_fileModel.indexOf(fileToAdd.elementAt(i)) == -1)
                        m_fileModel.addElement(fileToAdd.elementAt(i));
                }
                m_FileChooser.approveSelection();
            }
        } // end actionPerformed
                );

        if (filesPath != null) {
            File directory = new File(filesPath);
            m_FileChooser.setCurrentDirectory(directory);
        } // end if

        /*
         * Rectangle rect = m_FileChooser.getBounds(); rect.width = rect.width * 2;
         * m_FileChooser.reshape(rect.x, rect.y, rect.width, rect.height);
         */

        int returnVal = m_FileChooser.show(this);
        if ((returnVal == JFileChooser.APPROVE_OPTION)) {
            File file = m_FileChooser.getSelectedFile();
            if (file != null) {
                filesPath = file.getParent();
                if (m_FileChooser.getApproveMethod() == SourceChooser.DOUBLECLICKAPPROVE) {
                    if (m_fileModel.indexOf(m_FileChooser.getSelectedFile()) == -1) {
                        m_fileModel.addElement(m_FileChooser.getSelectedFile());
                    } // end if
                } // end if
            } // end if
        } // end if
    } // end addFilesButton_actionPerformed()

    void addPackagesButton_actionPerformed(ActionEvent e) {
        m_PackageChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        ((FileListPreviewer) m_PackageChooser.getAccessory()).clearFilesFromList();
        m_PackageChooser.setApproveButtonText(LocaleMgr.screen.getString("AddtoReverse"));
        // m_PackageChooser.setApproveButtonMnemonic(LocaleMgr.screen.getMnemonic("AddtoReverse"));
        // m_PackageChooser.setApproveButtonToolTipText(LocaleMgr.screen.getToolTip("AddtoReverse"));
        String label = LocaleMgr.screen.getString("AddtoReverse");
        JButton button = getApproveButton(m_PackageChooser, label);
        // getApproveButton(m_PackageChooser,LocaleMgr.screen.getString("AddtoReverse")).addActionListener(new
        // java.awt.event.ActionListener(){
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File[] packages = { m_PackageChooser.getSelectedFile() };
                // File[] packages = m_PackageChooser.getSelectedFiles();
                // //multiple selection
                DefaultListModel packageToAdd = ((FileListPreviewer) m_PackageChooser
                        .getAccessory()).getFilesFromList();
                for (int i = 0; i < packages.length; i++) {
                    if (packages[i] != null) {
                        if (packages[i].exists())
                            packageToAdd.addElement(packages[i]);
                    }
                }
                for (int i = 0; i < packageToAdd.size(); i++) {
                    if (m_packageModel.indexOf(packageToAdd.elementAt(i)) == -1)
                        m_packageModel.addElement(packageToAdd.elementAt(i));
                }
                m_PackageChooser.approveSelection();
            }
        });

        if (dirPath != null) {
            File directory = new File(dirPath);
            m_PackageChooser.setCurrentDirectory(directory);
        } // end if

        int returnVal = m_PackageChooser.show(this);
        if ((returnVal == JFileChooser.APPROVE_OPTION)) {
            dirPath = m_PackageChooser.getCurrentDirectory().toString();
        }
    }

    void removeFilesButton_actionPerformed(ActionEvent e) {
    }

    void removePackagesButton_actionPerformed(ActionEvent e) {
    }

    void reverseButton_actionPerformed(ActionEvent e) {
        // m_model = new DefaultListModel();
        // m_model = getFilesFromPackages();
        getFilesFromPackages();

        int i;
        for (i = 0; i < m_fileModel.size(); i++) {
            m_model.addElement(m_fileModel.elementAt(i));
        }
        m_files = m_model.elements();

        isDone = true;
        terminate(false);
    }

    private final void getFilesFromPackages() {
        DirectoryScanner dirScan = new DirectoryScanner();
        Vector filesVector;
        Vector extVector = new Vector();
        m_packages = m_packageModel.elements();

        // Build extension vector
        Enumeration enumeration = filterVector.elements();
        while (enumeration.hasMoreElements()) {
            ExtensionFileFilter currFilter = (ExtensionFileFilter) enumeration.nextElement();
            String ext = currFilter.getExtension();
            extVector.addElement(ext);
        } // end while

        // For each package
        while (m_packages.hasMoreElements()) {
            File currPackage = (File) m_packages.nextElement();

            String[] list = currPackage.list();
            for (int i = 0; i < list.length; i++) {
                String filename = list[i];
                String ext = filename.substring(filename.indexOf('.') + 1);
                if (extVector.indexOf(ext) != -1) {
                    File file = new File(currPackage, filename);
                    m_model.addElement(file);
                }
            }
        } // end for each package
    }

    public DefaultListModel getModel() {
        return m_model;
    }

    public final boolean is_cancel() {
        return canceledByUser;
    }

    protected DbProject m_project;

    public void setProject(DbProject project) {
        m_project = project;
    }

    protected void updateParametersValues() {
        Iterator iter = plugins.getReverseObjectList().iterator();
        while (iter.hasNext()) {
            JackReverseEngineeringPlugin rev = (JackReverseEngineeringPlugin) iter.next();
            ReverseParameters revParam = rev.getRevParameters();
            if (revParam != null) {
                revParam.updateOptionValue();
            } // end if
        } // end while
    }

    protected final boolean nothingToReverse() {
        boolean filesToReverse = fileList.getModel().getSize() > 0 ? true : false;
        boolean packageToReverse = packageList.getModel().getSize() > 0 ? true : false;
        return (filesToReverse || packageToReverse ? false : true);
    }

    protected final void init_actionListeners() {
        jbInit();

        /* Add listeners to buttons */
        // context
        helpBtn.addActionListener(new helpBtn_actionAdapter(this));
        // context
        addFilesButton.addActionListener(new addFilesButton_actionAdapter(this));
        addPackagesButton.addActionListener(new addPackagesButton_actionAdapter(this));
        cancelButton.addActionListener(new cancelButton_actionAdapter(this));
        removeFilesButton.addActionListener(new removeFilesButton_actionAdapter(this));
        removePackagesButton.addActionListener(new removePackagesButton_actionAdapter(this));
        reverseButton.addActionListener(new reverseButton_actionAdapter(this));

        /* Disable buttons */
        removeFilesButton.setEnabled(false);
        removePackagesButton.setEnabled(false);
        reverseButton.setEnabled(false);

    } // end of init_actionListeners()
} // end of ReverseFrame

/*
 * ACTION LISTENERS
 */
class cancelButton_actionAdapter implements java.awt.event.ActionListener {
    ReverseFrame adaptee;

    cancelButton_actionAdapter(ReverseFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.cancelButton_actionPerformed(e);
    }
}

// context
class helpBtn_actionAdapter implements java.awt.event.ActionListener {
    ReverseFrame adaptee;

    helpBtn_actionAdapter(ReverseFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.helpBtn_actionPerformed(e);
    }
}

class addFilesButton_actionAdapter implements java.awt.event.ActionListener {
    ReverseFrame adaptee;

    addFilesButton_actionAdapter(ReverseFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.addFilesButton_actionPerformed(e);

        if (adaptee.fileList.getModel().getSize() > 0) {
            adaptee.reverseButton.setEnabled(true);
        }
    }
}

class addPackagesButton_actionAdapter implements java.awt.event.ActionListener {
    ReverseFrame adaptee;

    addPackagesButton_actionAdapter(ReverseFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.addPackagesButton_actionPerformed(e);

        if (adaptee.packageList.getModel().getSize() > 0) {
            adaptee.reverseButton.setEnabled(true);
        }
    }
}

class removeFilesButton_actionAdapter implements java.awt.event.ActionListener {
    ReverseFrame adaptee;

    removeFilesButton_actionAdapter(ReverseFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.removeFilesButton_actionPerformed(e);
        Object[] selectedFiles = adaptee.fileList.getSelectedValues();

        for (int i = 0; i < selectedFiles.length; i++) {
            adaptee.m_fileModel.removeElement((File) selectedFiles[i]);
        }

        adaptee.fileList.clearSelection();
        adaptee.removeFilesButton.setEnabled(false);
        if (adaptee.nothingToReverse()) {
            adaptee.reverseButton.setEnabled(false);
        }

    }
}

class removePackagesButton_actionAdapter implements java.awt.event.ActionListener {
    ReverseFrame adaptee;

    removePackagesButton_actionAdapter(ReverseFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.removePackagesButton_actionPerformed(e);
        Object[] selectedPackages = adaptee.packageList.getSelectedValues();

        for (int i = 0; i < selectedPackages.length; i++) {
            adaptee.m_packageModel.removeElement((File) selectedPackages[i]);
        }

        adaptee.packageList.clearSelection();
        adaptee.removePackagesButton.setEnabled(false);
        if (adaptee.nothingToReverse()) {
            adaptee.reverseButton.setEnabled(false);
        }
    }
}

class reverseButton_actionAdapter implements java.awt.event.ActionListener {
    ReverseFrame adaptee;

    reverseButton_actionAdapter(ReverseFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.reverseButton_actionPerformed(e);
    }
}
