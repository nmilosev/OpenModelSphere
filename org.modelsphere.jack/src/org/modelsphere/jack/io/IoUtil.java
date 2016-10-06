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

import java.awt.Component;
import java.io.*;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.modelsphere.jack.awt.dirchooser.DirectoryChooser;
import org.modelsphere.jack.awt.dirchooser.DirectoryChooser2;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;

/**
 * 
 * Input-output utility methods.
 * 
 * @author Marco Savard
 * 
 */
public final class IoUtil {

    protected IoUtil() {
    }

    /**
     * Convenience method to copy a file from a source to a destination. Overwrite is prevented, and
     * the last modified is kept.
     * 
     * @throws IOException
     */
    public static void copyFile(String sourceFile, String destFile) throws IOException {
        copyFile(new File(sourceFile), new File(destFile), false, true);
    }

    /**
     * Method to copy a file from a source to a destination specifying if source files may overwrite
     * newer destination files and the last modified time of <code>destFile</code> file should be
     * made equal to the last modified time of <code>sourceFile</code>.
     * 
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File destFile, boolean overwrite,
            boolean preserveLastModified) throws IOException {
        if (overwrite || !destFile.exists() || destFile.lastModified() < sourceFile.lastModified()) {
            if (destFile.exists() && destFile.isFile()) {
                destFile.delete();
            }

            // ensure that parent dir of dest file exists!
            // not using getParentFile method to stay 1.1 compat
            File parent = new File(destFile.getParent());
            if (!parent.exists()) {
                parent.mkdirs();
            }

            FileInputStream in = new FileInputStream(sourceFile);
            FileOutputStream out = new FileOutputStream(destFile);

            byte[] buffer = new byte[8 * 1024];
            int count = 0;
            do {
                out.write(buffer, 0, count);
                count = in.read(buffer, 0, buffer.length);
            } while (count != -1);

            in.close();
            out.close();

            if (preserveLastModified) {
                destFile.setLastModified(sourceFile.lastModified());
            }
        }
    }

    /**
     * Delete the specified file. If it is a folder, then delete all its contents recursively. It's
     * because java.io.File.delete() cannot delete a folder if it is not empty.
     */
    public static boolean deleteRecusively(File file) {
        if (file.isDirectory()) {
            File[] subfiles = file.listFiles();
            int nb = subfiles.length;
            for (int i = 0; i < nb; i++) {
                deleteRecusively(subfiles[i]);
            } // end for
        } // end if

        return file.delete();
    } // end deleteRecusively

    /**
     * Change directory chooser strategy in a single place.
     */
    public static File selectDirectory(Component parent, String defaultDirectory, String title,
            String approve) {
        // File file = selectDirectory1(parent, defaultDirectory, title,
        // approve);
        File file = selectDirectory2(parent, defaultDirectory, title, approve);
        // File file = selectDirectory3(parent, defaultDirectory, title,
        // approve);
        return file;
    } // end selectDirectory

    // Stategy 1 : use a JFileChooser
    private static File selectDirectory1(Component parent, String defaultDirectory, String title,
            String approve) {
        JFileChooser chooser = new JFileChooser(defaultDirectory);
        File chosenDirectory = null;
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        int result = chooser.showDialog(parent, title);

        if (result == JFileChooser.APPROVE_OPTION) {
            chosenDirectory = chooser.getSelectedFile();
        }

        return chosenDirectory;
    } // end selectOutputDirectory1()

    // Stategy 2 : use a DirectoryChooser2
    // pro : cute GUI
    // con : slow
    private static File selectDirectory2(Component parent, String defaultDirectory, String title,
            String approve) {
        File file = new File(defaultDirectory);
        final DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();

        /*
         * mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); try {
         * Thread.sleep(2000); } catch (InterruptedException ex) { }
         * 
         * mainFrame.setCursor(Cursor.getDefaultCursor());
         */

        File chosenDir = DirectoryChooser2.selectDirectory(parent, file, title);

        // mainFrame.setCursor(Cursor.getDefaultCursor());

        return chosenDir;
    } // end selectOutputDirectory2()

    // Stategy 3 : use a DirectoryChooser
    // pro : fast
    // con : crude GUI
    private static File selectDirectory3(Component parent, String defaultDirectory, String title,
            String approve) {
        File file = new File(defaultDirectory);
        DirectoryChooser chooser = new DirectoryChooser(file);
        int result = JFileChooser.CANCEL_OPTION;
        if (parent instanceof JFrame) {
            result = chooser.showDialog((JFrame) parent, approve, title);
        } else if (parent instanceof JDialog) {
            result = chooser.showDialog((JDialog) parent, approve, title);
        }

        File chosenDir = (result == JFileChooser.APPROVE_OPTION) ? chooser.getSelectedFile() : null;
        return chosenDir;
    } // end selectOutputDirectory1()

    //
    // get parent fiel whose name is 'namedParent'
    //
    public static File getNamedParent(File file, String parentName) {
        File namedParent = null; // not found

        do {
            file = file.getParentFile();
            if (file == null) {
                break;
            }

            String name = file.getName();
            if (name.equals(parentName)) {
                namedParent = file;
                break;
            } // end if

        } while (true);

        return namedParent;
    } // end getNamedParent()

    /**
     * load a text from a text file from a class relative path.
     * 
     * @param relToThisClass
     *            the class location is prepend to the relImageName to an absolute path.
     * @param relFileName
     *            the relative text file path.
     * @return the text if found null otherwise.
     */
    public static String loadTextFile(Class relToThisClass, String relFileName) throws IOException {
        URL url = getURL(relToThisClass, relFileName);
        String eol = "\n"; // NOT LOCALIZABLE, for GUI display, no need to get
        // the OS-specific EOL
        String text;

        if (url != null) {
            StringBuffer buffer = new StringBuffer();
            InputStream input = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;
            do {
                line = reader.readLine();
                if (line != null) {
                    buffer.append(line);
                    buffer.append(eol);
                }
            } while (line != null);

            text = buffer.toString();
        } else {
            text = null;
        } // end if

        if (text == null) {
            IOException ex = new FileNotFoundException(relFileName);
            throw ex;
        } // end if

        return text;
    }

    /**
     * get the URL of a file from a class relative path.
     * 
     * @param relToThisClass
     *            the class location is prepend to the relImageName to an absolute path.
     * @param relFileName
     *            the relative text file path.
     * @return the url of this file.
     */
    public static URL getURL(Class relToThisClass, String resourceName) {
        URL url = relToThisClass.getResource(resourceName);
        return url;
    }
} // end IoUtil
