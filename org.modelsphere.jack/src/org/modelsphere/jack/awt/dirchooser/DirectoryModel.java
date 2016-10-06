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
import javax.swing.table.*;
import java.io.*;

class DirectoryModel extends AbstractTableModel {
    protected File directory;
    protected String[] children;
    protected int rowCount;
    protected Object dirIcon;
    protected Object fileIcon;

    DirectoryModel() {
        init();
    }

    DirectoryModel(File dir) {
        init();
        directory = dir;
        children = dir.list();
        rowCount = children.length;
    }

    protected void init() {
        dirIcon = UIManager.get("DirectoryPane.directoryIcon");
        fileIcon = UIManager.get("DirectoryPane.fileIcon");
    }

    void setDirectory(File dir) {
        if (dir != null) {
            directory = dir;
            children = dir.list();
            rowCount = children.length;
        } else {
            directory = null;
            children = null;
            rowCount = 0;
        }
        fireTableDataChanged();
    }

    // /////////////////////////////
    // OVERRIDES AbstractTableModel
    public int getRowCount() {
        return children != null ? rowCount : 0;
    }

    public int getColumnCount() {
        return children != null ? 3 : 0;
    }

    public Object getValueAt(int row, int column) {
        if (directory == null || children == null) {
            return null;
        }

        File fileSysEntity = new File(directory, children[row]);

        switch (column) {
        case 0:
            return fileSysEntity.isDirectory() ? dirIcon : fileIcon;
        case 1:
            return fileSysEntity.getName();
        case 2:
            if (fileSysEntity.isDirectory()) {
                return "--";
            } else {
                return new Long(fileSysEntity.length());
            }
        default:
            return "";
        }
    }

    public String getColumnName(int column) {
        switch (column) {
        case 0:
            return "Type";
        case 1:
            return "Name";
        case 2:
            return "Bytes";
        default:
            return "unknown";
        }
    }

    public Class getColumnClass(int column) {
        if (column == 0) {
            return getValueAt(0, column).getClass();
        } else {
            return super.getColumnClass(column);
        }
    }
    // OVERRIDES AbstractTableModel
    // //////////////////////////////////////
} // end DirectoryModel

