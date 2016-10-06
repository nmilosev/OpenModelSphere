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

package org.modelsphere.jack.awt;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.io.*;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.JTextComponent;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.text.MessageFormat;

// Internal use for TextViewerFrame, TextViewerDialog
class TextViewerController implements ActionListener {// , HyperlinkListener {
    private static final String KB = LocaleMgr.misc.getString("KB");

    private Component frame; // JInternalFrame or JDialog
    private JMenuItem menuFileOpen = new JMenuItem(LocaleMgr.action.getString("TextViewerOpen"));
    private JMenuItem menuFileSave = new JMenuItem(LocaleMgr.action.getString("TextViewerSave"));
    private JMenuItem menuFileSaveAs = new JMenuItem(LocaleMgr.action.getString("TextViewerSaveAs"));
    private JMenuItem menuFilePrint = new JMenuItem(LocaleMgr.action.getString("TextViewerPrint"));
    private JMenuItem menuFileClose = new JMenuItem(LocaleMgr.action.getString("TextViewerClose"));
    private JLabel currentVMTotalMemory = new JLabel();
    private JLabel currentVMFreeMemory = new JLabel();
    private Timer vMUpdater;

    private class TextViewerStatusBarModel implements StatusBarModel {
        JLabel messageLabel = new JLabel(" ");

        TextViewerStatusBarModel() {
        }

        public int getComponentCount() {
            return vmMenu ? 3 : 1;
        }

        public JComponent getComponentAt(int col) {
            switch (col) {
            case 0:
                return messageLabel;
            case 1:
                return currentVMTotalMemory;
            case 2:
                return currentVMFreeMemory;
            default:
                return new JLabel("");
            }
        }

        public int getWidthAt(int col) {
            switch (col) {
            case 0:
                return StatusBar.RELATIVE_WIDTH;
            case 1:
                return 70;
            case 2:
                return 60;
            default:
                return 1;
            }
        }

        public JComponent getTitleForComponentAt(int col) {
            switch (col) {
            case 0:
                return null;
            case 1:
                return new JLabel(LocaleMgr.misc.getString("Total"));
            case 2:
                return new JLabel(LocaleMgr.misc.getString("Free"));
            default:
                return null;
            }
        }

        public void startWaitingBar(String message) {
        }

        public void startWaitingBar(String message, long timeBeforeStarting) {
        }

        public void stopWaitingBar(String message) {
        }

        public void setMessage(String message) {
            if (message == null || message.length() == 0)
                message = " ";
            messageLabel.setText(message);
            messageLabel.setToolTipText(message);
        }
    }

    private StatusBarModel statusBarModel;
    private StatusBar statusBar1;
    private JTextComponent textPanel;
    boolean isHtml;
    private Container contentPane;
    private JMenu menuFile;
    private JMenu menuVirtualMachine;
    private File currentFile = null;
    private boolean textIsNotSave = false;
    private boolean vmMenu = false;
    private String kSaveMessage = LocaleMgr.message.getString("SaveMessage");

    transient private static String defaultDirectory = ApplicationContext
            .getDefaultWorkingDirectory();

    TextViewerController(Component frame, String text, boolean isHtml, File file,
            boolean mailToEnable) {
        this(frame, text, isHtml, file, mailToEnable, false);
    }

    TextViewerController(Component frame, String text, boolean isHtml, File file,
            boolean mailToEnable, boolean vmMenu) {
        this.frame = frame;
        this.vmMenu = vmMenu;
        contentPane = (frame instanceof JInternalFrame ? ((JInternalFrame) frame).getContentPane()
                : ((JDialog) frame).getContentPane());

        statusBarModel = new TextViewerStatusBarModel();
        statusBar1 = new StatusBar(statusBarModel);

        // Setup File Menu
        JMenuBar mainMenu = new JMenuBar();
        if (frame instanceof JInternalFrame)
            ((JInternalFrame) frame).setJMenuBar(mainMenu);
        else
            ((JDialog) frame).setJMenuBar(mainMenu);

        menuFile = new JMenu(LocaleMgr.action.getString("TextViewerFile"));
        mainMenu.add(menuFile);

        menuFileOpen.addActionListener(this);
        menuFileSave.addActionListener(this);
        menuFileSaveAs.addActionListener(this);
        menuFilePrint.addActionListener(this);
        menuFileClose.addActionListener(this);

        if (file != null) {
            if (readDocumentFile(file)) {
                String title = (frame instanceof JInternalFrame ? ((JInternalFrame) frame)
                        .getTitle() : ((JDialog) frame).getTitle());
                if (title == null || title.length() == 0) {
                    if (frame instanceof JInternalFrame) {
                        ((JInternalFrame) frame).setTitle(file.getName());
                    } else {
                        ((JDialog) frame).setTitle(file.getName());
                    }
                }
                return;
            }
        }

        if (vmMenu) {
            menuVirtualMachine = new JMenu(LocaleMgr.action.getString("VirtualMachine"));
            AbstractActionsStore store = ApplicationContext.getActionStore();
            menuVirtualMachine.add(store.getAction(AbstractActionsStore.SYSTEM_GARBAGE_COLLECT));
            mainMenu.add(menuVirtualMachine);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    vMUpdater = new Timer(2000, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            currentVMTotalMemory.setText(new Long(Runtime.getRuntime()
                                    .totalMemory() / 1024).toString()
                                    + " " + KB);
                            currentVMFreeMemory.setText(new Long(
                                    Runtime.getRuntime().freeMemory() / 1024).toString()
                                    + " " + KB);
                        }
                    });
                    vMUpdater.start();
                }
            });

            if (frame instanceof JDialog) {
                ((JDialog) frame).addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        if (vMUpdater != null && vMUpdater.isRunning()) {
                            vMUpdater.stop();
                            vMUpdater = null;
                        }
                    }

                    public void windowClosed(WindowEvent e) {
                        if (vMUpdater != null && vMUpdater.isRunning()) {
                            vMUpdater.stop();
                            vMUpdater = null;
                        }
                    }
                });
            } else if (frame instanceof JInternalFrame) {
                ((JInternalFrame) frame).addInternalFrameListener(new InternalFrameAdapter() {
                    public void internalFrameClosing(InternalFrameEvent e) {
                        if (vMUpdater != null && vMUpdater.isRunning()) {
                            vMUpdater.stop();
                            vMUpdater = null;
                        }
                    }
                });
            }
        }

        this.isHtml = isHtml;
        textPanel = createTextPanelType(isHtml);
        textPanel.setText(text);
        if (text != null && text.length() > 0)
            textPanel.setCaretPosition(0);
        manageContentPane();
        manageMenuItems();
    }

    private JTextComponent createTextPanelType(boolean isHtml) {
        JTextComponent textPanel;
        if (isHtml) {
            textPanel = new JEditorPane();
            ((JEditorPane) textPanel).setContentType("text/html");// NOT
            // LOCALIZABLE
            textPanel.setEditable(false);
        } else {
            textPanel = new JTextArea();
            ((JTextArea) textPanel).setLineWrap(true);
            ((JTextArea) textPanel).setWrapStyleWord(true);
        }
        return textPanel;
    }

    private void manageContentPane() {

        contentPane.removeAll();
        contentPane.add(statusBar1, BorderLayout.SOUTH);
        contentPane.add(new JScrollPane(textPanel), BorderLayout.CENTER);
        // contentPane.repaint();
    }

    private void manageMenuItems() {

        menuFile.removeAll();
        menuFile.add(menuFileOpen);
        menuFile.add(menuFileSave);
        menuFile.add(menuFileSaveAs);
        menuFile.addSeparator();
        menuFile.add(menuFilePrint);
        menuFile.addSeparator();
        menuFile.add(menuFileClose);
    }

    public final void actionPerformed(ActionEvent e) {
        if (e.getSource() == menuFileOpen)
            openFile();
        else if (e.getSource() == menuFileSave)
            saveAsFile(true);
        else if (e.getSource() == menuFileSaveAs)
            saveAsFile(false);
        else if (e.getSource() == menuFilePrint)
            printFile();
        else if (e.getSource() == menuFileClose)
            closeViewer();
    }

    public final JTextComponent getTextPanel() {
        return textPanel;
    }

    public void setDocumentState(boolean textIsChanged) {
        textIsNotSave = textIsChanged;
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser(defaultDirectory);
        chooser.setDialogTitle(LocaleMgr.screen.getString("OpenTextFile"));
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file != null)
                readDocumentFile(file);
        }
    }

    private boolean readDocumentFile(File file) {
        String extension = ExtensionFileFilter.getExtension(file);
        boolean html = ExtensionFileFilter.htmlFileFilter.isValidExtension(extension);
        JTextComponent tPanel = createTextPanelType(html);

        try {
            FileReader in = new FileReader(file);
            tPanel.read(in, null);
            in.close();
        } catch (IOException ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(frame, ex);
            return false;
        }

        defaultDirectory = file.getParent();
        currentFile = file;
        isHtml = html;
        textPanel = tPanel;
        manageContentPane();
        manageMenuItems();
        String pattern = LocaleMgr.message.getString("{0}Opened");
        setStatusText(MessageFormat.format(pattern, new Object[] { file.getName() }));
        return true;
    }

    private void saveAsFile(boolean isSave) {
        ExtensionFileFilter filter = (isHtml ? ExtensionFileFilter.htmlFileFilter
                : ExtensionFileFilter.txtFileFilter);
        if (currentFile == null)
            currentFile = new File(defaultDirectory);
        ExtensionFileFilter[] optionalFilters = null;
        AwtUtil.FileAndFilter selection = AwtUtil.showSaveAsDialog(frame, LocaleMgr.screen.getString("SaveTextFile"),
                filter, optionalFilters, currentFile, isSave);
        File file = (selection == null) ? null : selection.getFile(); 
        
        if (file != null) {
            currentFile = file;
            saveCurrentDocumentFile();
        }
    }

    private void saveCurrentDocumentFile() {
        try {
            FileWriter out = new FileWriter(currentFile);
            textPanel.write(out);
            out.close();
            String pattern = LocaleMgr.message.getString("{0}Saved");
            textIsNotSave = false;
            setStatusText(MessageFormat.format(pattern, new Object[] { currentFile.getName() }));
        } catch (IOException ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(frame, ex);
        }
    }

    private void printFile() {
        String title = (frame instanceof JDialog) ? ((JDialog) frame).getTitle()
                : ((JInternalFrame) frame).getTitle();
        String name = (currentFile != null) ? currentFile.getName() : title;

        JTextComponentPrintManager print = new JTextComponentPrintManager(textPanel, name);
        print.run();
        frame.repaint();
    }

    public void closeViewer() {
        if (textIsNotSave) {
            int rc = JOptionPane.showConfirmDialog(this.frame, kSaveMessage, "",
                    JOptionPane.YES_NO_OPTION);
            if (rc == JOptionPane.YES_OPTION)
                saveCurrentDocumentFile();
        }
        if (vMUpdater != null && vMUpdater.isRunning()) {
            vMUpdater.stop();
            vMUpdater = null;
        }
        if (frame instanceof JInternalFrame) {
            try {
                ((JInternalFrame) frame).setClosed(true);
            } catch (PropertyVetoException e) {
            }
        } else {
            ((JDialog) frame).dispose();
        }
    }

    public void setStatusText(String text) {
        if (text == null)
            text = " ";
        statusBarModel.setMessage(text);
    }
}
