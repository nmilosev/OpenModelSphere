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

package org.modelsphere.jack.io;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Iterates recursively in a directory hierarchy. Creates a directory iterator, the constructor
 * requires a root directory. next() returns the next directory in the hierarchy. hasNext() returns
 * false if there's no more visited directory. Order of iteration is not specified. TODO: Allows the
 * user to select PRE-ORDER, IN-ORDER and POST-ORDER way to traverse the directory tree.
 */
public final class DirectoryList {
    public static final boolean DIRECTORY_ONLY = true;
    private ArrayList m_dirList = new ArrayList();

    //
    // public constructors
    //

    // scan all the directories under 'rootdir'
    public DirectoryList(File rootdir) {
        this(rootdir, true, null);
    } // end DirectoryIterator

    // scan all the files under 'rootdir' if directoryOnly == false
    // scan only the directories under 'rootdir' if directoryOnly == true
    public DirectoryList(File rootdir, boolean directoryOnly) {
        this(rootdir, directoryOnly, null);
    } // end DirectoryIterator

    // scan all the files under 'rootdir' which are accepted by the filter
    public DirectoryList(File rootdir, FileFilter filter) {
        this(rootdir, false, filter);
    } // end DirectoryIterator

    // general and private constructor
    // if directoryOnly == true, filter parameter is ignored
    private DirectoryList(File rootdir, boolean directoryOnly, FileFilter filter) {
        scanSubdir(m_dirList, rootdir, directoryOnly, filter);
    } // end DirectoryIterator

    private void scanSubdir(ArrayList dirlist, File root, boolean directoryOnly, FileFilter filter) {
        String[] list = root.list();
        for (int i = 0; i < list.length; i++) {
            String listElem = list[i];
            File currFile = new File(root, listElem);

            if (directoryOnly == true) {
                if (currFile.isDirectory()) {
                    dirlist.add(currFile);
                }
            } else { // all the files
                if (filter != null) {
                    if (filter.accept(currFile))
                        dirlist.add(currFile);
                } else { // no filter, add anything
                    dirlist.add(currFile);
                } // end if
            } // end if

            if (currFile.isDirectory()) {
                scanSubdir(dirlist, currFile, directoryOnly, filter);
            }
        } // end for
    } // end scanSubfolder()

    // iterates in the list
    public Iterator getIterator() {
        return m_dirList.iterator();
    }

    public int getSize() {
        return m_dirList.size();
    }

} // end DirectoryIterator
