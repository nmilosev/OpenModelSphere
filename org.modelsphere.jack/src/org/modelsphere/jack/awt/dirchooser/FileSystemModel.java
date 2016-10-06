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

import javax.swing.tree.*;
import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

class FileSystemModel extends AbstractTreeModel implements Serializable {
    public static final int SHOW_ALL = 0;
    public static final int DIRECTORIES_ONLY = 1;

    private FileFilter m_filter;
    String root;

    //
    // Constructors
    //
    FileSystemModel(FileFilter filter) {
        this(filter, System.getProperty("user.home"));
    }

    FileSystemModel(FileFilter filter, String startPath) {
        root = startPath;
        m_filter = filter;
    }

    // //////////////////////////
    // OVERRIDES AbstractTreeModel
    public Object getRoot() {
        return new File(root);
    }

    public Object getChild(Object parent, int index) {
        File directory = (File) parent;
        File[] children = directory.listFiles(m_filter);
        String childName = children[index].getName();
        File child = new File(directory, childName);

        return child;
    } // end getChild()

    public int getChildCount(Object parent) {
        int childCount;

        File fileSysEntity = (File) parent;
        if (fileSysEntity.isDirectory()) {
            File[] children = fileSysEntity.listFiles(m_filter);
            childCount = children.length;
        } else {
            childCount = 0;
        }

        return childCount;
    } // end getChildCount()

    public boolean isLeaf(Object node) {
        return ((File) node).isFile();
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    public int getIndexOfChild(Object parent, Object child) {
        File directory = (File) parent;
        File fileSysEntity = (File) child;
        File[] children = directory.listFiles(m_filter);
        int result = -1;

        for (int i = 0; i < children.length; ++i) {
            String childName = children[i].getName();
            if (fileSysEntity.getName().equals(childName)) {
                result = i;
                break;
            }
        } // end for

        return result;
    }
    // OVERRIDES AbstractTreeModel
    // ///////////////////////////

} // end FileSystemModel

