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
import java.io.File;
import java.util.Vector;
import javax.swing.*;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.util.DirectoryScanner;

public final class FileListPreviewer extends JPanel {

    private DefaultListModel fileModel;
    private JList filesList;
    private File[] selectedFiles = null;
    private JFileChooser fileChooser;
    private int selectMode;

    /** Instruction to display only files. */
    public static final int FILES_ONLY = 0;
    /** Instruction to display only directories. */
    public static final int DIRECTORIES_ONLY = 1;
    /** Instruction to display both files and directories. */
    public static final int FILES_AND_DIRECTORIES = 2;

    public FileListPreviewer(JFileChooser fc, int mode) {
        super(new GridBagLayout());
        selectMode = mode;
        fileModel = new DefaultListModel();
        filesList = new JList(fileModel);
        fileChooser = fc;
        JScrollPane scrollPanel = new JScrollPane(filesList);
        scrollPanel.setMinimumSize(new Dimension(235, 130));

        JPanel innerButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        JButton addButton = new JButton(LocaleMgr.screen.getString("Add"));
        addButton.setMnemonic(LocaleMgr.screen.getMnemonic("Add"));
        addButton.setToolTipText(LocaleMgr.screen.getToolTip("Add"));
        JButton removeButton = new JButton(LocaleMgr.screen.getString("Remove"));
        removeButton.setMnemonic(LocaleMgr.screen.getMnemonic("Remove"));
        removeButton.setToolTipText(LocaleMgr.screen.getToolTip("Remove"));

        this.add(scrollPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));

        innerButtonPanel.add(addButton);
        JButton addMultipleButton = getButtonAccording2Mode();
        innerButtonPanel.add(addMultipleButton);
        innerButtonPanel.add(removeButton);
        if (selectMode == FILES_ONLY)
            AwtUtil.normalizeComponentDimension(new JButton[] { addButton, removeButton,
                    addMultipleButton });

        this.add(innerButtonPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
                0, 0));

        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) { // to do: multiple
                // selection
                File file = fileChooser.getSelectedFile();
                if (file != null) {
                    if (fileModel.indexOf(file) == -1)
                        fileModel.addElement(file);
                }
                /*
                 * //Multiple selection Object[] files = fileChooser.getSelectedFiles(); for (int i
                 * = 0; i < files.length; i++){ if (fileModel.indexOf(files[i]) == -1)
                 * fileModel.addElement(files[i]); }
                 */
            }
        });
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object[] files = filesList.getSelectedValues();
                for (int i = 0; i < files.length; i++) {
                    fileModel.removeElement(files[i]);
                }
            }
        });

    }

    public DefaultListModel getFilesFromList() {
        return fileModel;
    }

    public void clearFilesFromList() {
        fileModel.clear();
    }

    private JButton getButtonAccording2Mode() {
        JButton button;
        switch (selectMode) {
        case 1:
        case 2: {
            button = new JButton(LocaleMgr.screen.getString("AddRecursive"));
            button.setMnemonic(LocaleMgr.screen.getMnemonic("AddRecursive"));
            button.setToolTipText(LocaleMgr.screen.getToolTip("AddRecursive"));
            button.addActionListener(new addRecursive_actionAdapter());
        }
            break;
        case 0:
        default: {
            button = new JButton(LocaleMgr.screen.getString("AddAll"));
            button.setMnemonic(LocaleMgr.screen.getMnemonic("AddAll"));
            button.setToolTipText(LocaleMgr.screen.getToolTip("AddAll"));
            button.addActionListener(new addAll_actionAdapter());

        }
        }
        return button;

    }

    class addRecursive_actionAdapter implements java.awt.event.ActionListener {

        public addRecursive_actionAdapter() {
        }

        public void actionPerformed(ActionEvent e) {
            /*
             * // ******* The multiple selection not work (Know bug of Java #4170956) ***********
             * File[] selectedFiles = fileChooser.getSelectedFiles();
             * 
             * for (int t = 0; t < selectedFiles.length; t++){ if (selectedFiles[t] != null){ if
             * (selectedFiles[t].isDirectory()){ DirectoryScanner dirScan = new DirectoryScanner();
             * Vector filesList = dirScan.getDirList(selectedFiles[t], true); for (int i = 0; i <
             * filesList.size(); i++){ if (fileModel.indexOf((File)filesList.elementAt(i)) == -1)
             * fileModel.addElement((File)filesList.elementAt(i)); } } } }
             */

            File selectedFile = fileChooser.getSelectedFile();

            if (selectedFile != null) {
                if (selectedFile.isDirectory()) {
                    DirectoryScanner dirScan = new DirectoryScanner();
                    Vector filesList = dirScan.getDirList(selectedFile, true);
                    for (int i = 0; i < filesList.size(); i++) {
                        if (fileModel.indexOf((File) filesList.elementAt(i)) == -1)
                            fileModel.addElement((File) filesList.elementAt(i));
                    }
                }
            }

        }

    }

    class addAll_actionAdapter implements java.awt.event.ActionListener {
        public addAll_actionAdapter() {
        }

        public void actionPerformed(ActionEvent e) {
            // To do: Multiple selection (when the bug will solve, see above)
            DirectoryScanner dirScan = new DirectoryScanner();
            Vector filesList = dirScan.getFilesList(fileChooser.getCurrentDirectory(), false,
                    fileChooser.getFileFilter());
            for (int i = 0; i < filesList.size(); i++) {
                if (fileModel.indexOf((File) filesList.elementAt(i)) == -1)
                    fileModel.addElement((File) filesList.elementAt(i));
            }
        }
    }
}
