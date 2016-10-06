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
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

public class FileExplorer {
    public static void main(String[] argv) {
        JFrame frame = new JFrame("File Explorer");

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        FileSystemModel model = new FileSystemModel((FileFilter) null);
        DirectoryModel directoryModel = new DirectoryModel((File) model.getRoot());
        JTable table = new JTable(directoryModel);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 2));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumn("Type").setCellRenderer(new DirectoryRenderer());
        table.getColumn("Type").setMaxWidth(32);
        table.getColumn("Type").setMinWidth(32);

        FileSystemTreePanel fileTree = new FileSystemTreePanel(model);
        fileTree.getTree().addTreeSelectionListener(new TreeListener(directoryModel));

        JScrollPane treeScroller = new JScrollPane(fileTree);
        JScrollPane tableScroller = new JScrollPane(table);
        treeScroller.setMinimumSize(new Dimension(0, 0));
        tableScroller.setMinimumSize(new Dimension(0, 0));
        tableScroller.setBackground(Color.white);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScroller,
                tableScroller);
        splitPane.setContinuousLayout(true);

        frame.getContentPane().add(splitPane);

        frame.setSize(400, 400);
        frame.pack();
        frame.setVisible(true);
    }

    protected static class TreeListener implements TreeSelectionListener {
        DirectoryModel model;

        public TreeListener(DirectoryModel mdl) {
            model = mdl;
        }

        public void valueChanged(TreeSelectionEvent e) {
            File fileSysEntity = (File) e.getPath().getLastPathComponent();
            if (fileSysEntity.isDirectory()) {
                model.setDirectory(fileSysEntity);
            } else {
                model.setDirectory(null);
            }
        }
    }
}
