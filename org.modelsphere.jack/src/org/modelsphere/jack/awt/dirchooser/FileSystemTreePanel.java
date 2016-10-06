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
import javax.swing.event.TreeSelectionListener;
import java.io.*;
import java.awt.*;

class FileSystemTreePanel extends JPanel {
    private JTree tree;

    //
    // Constructors
    //
    FileSystemTreePanel() {
        this(new FileSystemModel(null));
    }

    FileSystemTreePanel(String startPath) {
        this(new FileSystemModel(null, startPath));
    }

    FileSystemTreePanel(FileSystemModel model) {
        tree = new JTree(model) {
            public String convertValueToText(Object value, boolean selected, boolean expanded,
                    boolean leaf, int row, boolean hasFocus) {
                return ((File) value).getName();
            }
        };

        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.putClientProperty("JTree.lineStyle", "Angled");

        setLayout(new BorderLayout());
        add(tree, BorderLayout.CENTER);
    }

    //
    // Package methods
    //

    void setStartPath(TreeSelectionListener tsl, FileSystemModel model) {
        tree.removeTreeSelectionListener(tsl);
        this.remove(tree);
        tree = new JTree(model) {
            public String convertValueToText(Object value, boolean selected, boolean expanded,
                    boolean leaf, int row, boolean hasFocus) {
                return ((File) value).getName();
            }
        };
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.putClientProperty("JTree.lineStyle", "Angled");
        tree.addTreeSelectionListener(tsl);
        this.add(tree, BorderLayout.CENTER);
        tree.updateUI();
    } // end setStartPath()

    JTree getTree() {
        return tree;
    }
}
