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
import java.util.Vector;

import javax.swing.filechooser.FileFilter;

public class DirectoryScanner {
    private Vector filesList;

    public DirectoryScanner() {
        filesList = new Vector();
    }

    private void scanFiles(File dirFile, FileScanner scanner, boolean includeSubDir) {
        if (dirFile.isDirectory()) {
            String[] files = dirFile.list();
            for (int i = 0; i < files.length; i++) {
                String path = dirFile.getAbsolutePath();
                if (!path.endsWith(File.separator))
                    path += File.separator;
                File file = new File(path + files[i]);
                if (file != null) {
                    if (file.isFile())
                        scanner.scanning(file);
                    else if (file.isDirectory() && includeSubDir)
                        scanFiles(file, scanner, includeSubDir);
                }
            }
        }
    }

    private void scanDirectory(File dirFile, FileScanner scanner, boolean includeSubDir) {
        if (dirFile.isDirectory()) {
            scanner.scanning(dirFile);
            String[] files = dirFile.list();
            for (int i = 0; i < files.length; i++) {
                String path = dirFile.getAbsolutePath();
                if (!path.endsWith(File.separator))
                    path += File.separator;
                File file = new File(path + files[i]);
                if (file != null) {
                    if (file.isDirectory() && !includeSubDir)
                        scanner.scanning(file);
                    else if (file.isDirectory() && includeSubDir) {
                        scanDirectory(file, scanner, includeSubDir);
                    }
                }
            }
        }
    }

    public Vector getFilesList(File dirFile, boolean includeSubDir, FileFilter filter) {
        scanFiles(dirFile, new FilterSearcher(filter), includeSubDir);
        return filesList;
    }

    public Vector getDirList(File dirFile, boolean includeSubDir) {
        scanDirectory(dirFile, new DirectorySearcher(), includeSubDir);
        return filesList;
    }

    public interface FileScanner {
        public void scanning(File file);
    }

    class FilterSearcher implements FileScanner {
        FileFilter filter;

        public FilterSearcher(FileFilter f) {
            filter = f;
        }

        public void scanning(File file) {
            if (filter.accept(file)) {
                filesList.addElement(file);
            }
        }
    }

    class DirectorySearcher implements FileScanner {

        public DirectorySearcher() {
        }

        public void scanning(File file) {
            if (file.isDirectory())
                filesList.addElement(file);

        }
    }

}
