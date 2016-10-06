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
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

//add new functionnalities to java.io.File
public final class PathFile {
    public static final char SEPARATOR = File.separatorChar;
    public static final char ALT_SEPARATOR = '/';
    public static final String BACKUP_EXTENSION = ".bak";
    private static final String CANNOT_CREATE_PATTERN = LocaleMgr.message
            .getString("CANNOT_CREATE_PATTERN");

    //Create a file, and create sub folders if they don't already exist.
    public static File createFile(String filename) throws IOException {
        return createFile(filename, false, false);
    }

    public static File createFile(String filename, boolean backupFileIfRequired) throws IOException {
        return createFile(filename, backupFileIfRequired, false);
    }

    //Create a file, and create sub folders if they don't already exist.
    //backupFileIfRequired : if true, create a filename.ext~ backup file if filename.ext already exist
    //deleteOnExit : if true, all created directory and the file will be marked as deleteOnExit (file.deleteOnExit()). Default = false;
    public static File createFile(String filename, boolean backupFileIfRequired,
            boolean deleteOnExit) throws IOException {
        //Replace '\\' by '/'
        String newFilename = "";
        String separator = "" + SEPARATOR;
        StringTokenizer st = new StringTokenizer(filename, separator, true);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.equals(separator)) {
                token = "" + ALT_SEPARATOR;
            }

            newFilename += token;
        } //end while

        File file = null;
        if (newFilename.charAt(0) == '/') {
            // Absolute dir
            file = new File(System.getProperty("file.separator"));
        }

        //for each token
        st = new StringTokenizer(newFilename, "" + ALT_SEPARATOR);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (st.hasMoreElements()) { //directory
                java.io.File dir = new File(file, token);
                if (!dir.exists()) {
                    dir.mkdir();
                    if (deleteOnExit)
                        dir.deleteOnExit();
                } else {
                    if (!dir.isDirectory()) {
                        String fileNamed = dir.getName();
                        String path = dir.getParentFile().getAbsolutePath();
                        String msg = MessageFormat.format(CANNOT_CREATE_PATTERN, new Object[] {
                                filename, fileNamed, path });
                        throw new IOException(msg);
                    }
                }
                file = dir;
            } else { //file
                if (backupFileIfRequired) {
                    backupFile(file, token);
                }

                file = new File(file, token);
                if (deleteOnExit)
                    file.deleteOnExit();
            }
        } //end while

        return file;
    } // createFile

    private static void backupFile(File dir, String filename) throws IOException {
        File file = new File(dir, filename);
        if (file.exists()) {
            File backupFile = new File(dir, filename + BACKUP_EXTENSION);
            backupFile.delete();
            file.renameTo(backupFile);
        }
    }

    public static boolean isAbsolutePath(String filename) {
        boolean isAbsolute = false;

        //if it starts with '/' or '\\'
        char firstChar = filename.charAt(0);
        if ((firstChar == SEPARATOR) || (firstChar == ALT_SEPARATOR)) {
            isAbsolute = true;
        } else { //if Windows-like absolute directory
            int driveIndex = filename.indexOf(':');
            if (driveIndex > 0) {
                String driveName = filename.substring(0, driveIndex);

                if ((driveName.indexOf(SEPARATOR) == -1)
                        && (driveName.indexOf(ALT_SEPARATOR) == -1))
                    isAbsolute = true;
            } //end if
        } //end if

        return isAbsolute;
    } //end boolean

    private static void test() {
        try {
            String filename = "/java/z1/z2/z3.txt"; //NOT LOCALIZABLE
            File file = createFile(filename);
            FileWriter fw = new FileWriter(file);
        } catch (Exception ex) {
            Debug.trace(ex.toString());
        }
    } //end test()

    public static void main(String[] args) throws IOException {
        test();
        Debug.trace("Press ENTER to quit.");
        byte[] buf = new byte[256];
        System.in.read(buf, 0, 255);
    } //end main
} //end File
