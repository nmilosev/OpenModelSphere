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

package org.modelsphere.jack.util;

import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JFileChooser;

public class FileUtils {
    private static final JFileChooser basicChooser = new JFileChooser();

    private FileUtils() {
    }

    /**
     * Recursively delete a directory and its content.
     * 
     * @param file
     *            The directory to delete
     * @param check
     *            Indicates if a canWrite() check must be performed on the tree before deleting any
     *            file
     * @return true if all the tree has been deleted.
     * @throws IOException
     */
    public static boolean deleteTree(File file, boolean check) throws IOException {
        if (file == null || !file.exists())
            return true;
        boolean result = true;
        if (check) {
            result = checkDeleteTree(file);
            if (!result)
                return false;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File child : files) {
                result = deleteTree(child, false);
                if (!result)
                    break;
            }
        }
        if (result)
            result = file.delete();
        return result;
    }

    /**
     * Recursively delete a directory and its content.
     * 
     * @param file
     *            The directory to delete
     * @return true if all the tree has been deleted.
     * @throws IOException
     */
    public static boolean deleteTree(File file) throws IOException {
        return deleteTree(file, false);
    }

    /**
     * Verify if a directory and its recursive content can be deleted.
     * <p>
     * This is done by performing a canWrite() on all files in the hierarchy.
     * 
     * @param file
     *            The directory to check.
     * @return true if all the content in the tree can be written.
     * @throws IOException
     */
    private static boolean checkDeleteTree(File file) throws IOException {
        if (file == null || !file.exists())
            return true;
        boolean result = true;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File child : files) {
                result = checkDeleteTree(child);
                if (!result)
                    break;
            }
        }
        if (result)
            result = file.canWrite();
        return result;
    }

    public static Icon getIcon(File file) {
        if (file == null) {
            return null;
        }
        return basicChooser.getIcon(file);
    }

}
