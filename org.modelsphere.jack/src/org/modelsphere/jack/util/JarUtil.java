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

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.modelsphere.jack.io.PathFile;

/**
 * Utility methods for Java archive handling
 * 
 * @author Marco Savard
 */
public final class JarUtil {
    // Extract all files mathing the specified extension in the temporary folder
    // extension: example: '.py'
    // The directory structure within the jarfile will be kept
    public static File[] createTemporaryFiles(JarFile jarfile, String extension) throws IOException {
        if (jarfile == null || extension == null)
            return null;
        ArrayList files = new ArrayList();
        extension = extension.toLowerCase();
        Enumeration entries = jarfile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = (JarEntry) entries.nextElement();
            if (entry.getName().toLowerCase().endsWith(extension)) {
                File tempfile = createTemporaryFile(jarfile, entry);
                files.add(tempfile);
            }
        }
        File[] arrayfiles = new File[files.size()];
        for (int i = 0; i < arrayfiles.length; i++)
            arrayfiles[i] = (File) files.get(i);
        return arrayfiles;
    }

    public static JarFile getJarFile(String url) throws IOException {
        String jarFileName = getJarAbsolutePath(url);
        if (jarFileName == null)
            return null;
        return new JarFile(jarFileName);
    }

    // Return the entry name part of this url or null if not a jar file
    public static String getEntryName(String url) {
        if (url == null)
            return null;
        String sURL = url;
        if (!(sURL.indexOf("jar:") == 0) || !(sURL.indexOf("!/") > -1)) // NOT
            // LOCALIZABLE
            return null;
        String entryName = sURL.substring(sURL.indexOf("!/") + 2, sURL.length()); // NOT
        // LOCALIZABLE
        return entryName;
    }

    // Return the entry name part of this url or null if not a jar file
    public static String getJarAbsolutePath(String url) {
        if (url == null)
            return null;
        String sURL = url;
        if (!(sURL.indexOf("jar:") == 0) || !(sURL.indexOf("!/") > -1)) // NOT
            // LOCALIZABLE
            return null;
        String jarFileName = sURL.substring("jar:file:".length(), sURL.indexOf("!/")); // NOT LOCALIZABLE
        return jarFileName;
    }

    // Extract all files mathing the specified extension in the temporary folder
    // extension: example: '.py'
    // The directory structure within the jarfile will be kept
    public static File[] createTemporaryFiles(String jarfilename, String extension)
            throws IOException {
        if (jarfilename == null)
            return null;
        return createTemporaryFiles(new JarFile(new File(jarfilename)), extension);
    }

    // Extract all files mathing the specified extension in the temporary folder
    // extension: example: '.py'
    // The directory structure within the jarfile will be kept
    public static File[] createTemporaryFiles(File jarfile, String extension) throws IOException {
        if (jarfile == null)
            return null;
        return createTemporaryFiles(new JarFile(jarfile), extension);
    }

    // Extract the file represented by entry in the temporary folder
    // The directory structure within the jarfile will be kept
    public static File createTemporaryFile(JarFile jarfile, JarEntry entry) throws IOException {
        if (jarfile == null || entry == null) {
            return null;
        }
        return createTemporaryFile(jarfile, entry.getName());
    }

    // Extract the file represented by entry in the temporary folder
    // The directory structure within the jarfile will be kept
    public static File createTemporaryFile(JarFile jarfile, String entryname) throws IOException {
        if (jarfile == null || entryname == null)
            return null;
        
        JarEntry entry = jarfile.getJarEntry(entryname);
        InputStream input = jarfile.getInputStream(entry);
        String fileName = entry.getName();
        File file = createTemporaryFile(input, fileName);
        
        /*
        String jarFileName = jarfile.getName();
        jarFileName = URLEncoder.encode(jarFileName, "UTF-8");
        String url =  "jar:file:" + jarFileName + "!/" + entryname;
        File file = createTemporaryFile(url);
        */
        return file;
    }

    // Extract the file represented by the url in the temporary folder
    // The directory structure within the jarfile will be kept
    public static File createTemporaryFile(InputStream input, String fileName) throws IOException {
    	int ch;
    	String tempDir = System.getProperty("java.io.tmpdir");
    	String tempFileName = tempDir + System.getProperty("file.separator") + fileName;
    	File tempfile = PathFile.createFile(tempFileName, false, true);
    	
    	FileOutputStream out = new FileOutputStream(tempfile);
    	
    	while ((ch = input.read()) != -1) {
            out.write(ch);
        }
    	input.close();
    	out.close();
    	
    	
    	return tempfile;
    }
    
    public static File createTemporaryFile(String resourceURL) throws IOException {
        if (resourceURL == null)
            return null;

        // validate the url file entry format
        String sURL = resourceURL;
        if (!(sURL.indexOf("jar:") == 0) || !(sURL.indexOf("!/") > -1)) // NOT
            // LOCALIZABLE
            return null;

        InputStream in = null;
        FileOutputStream out = null;
        File tempfile = null;
        String tempDir = System.getProperty("java.io.tmpdir"); // NOT
        // LOCALIZABLE
        String jarFileName = sURL.substring("jar:file:".length(), sURL.indexOf("!/")); // NOT LOCALIZABLE
        JarFile jarfile = new JarFile(new File(jarFileName));
        String fileName = sURL.substring(sURL.indexOf("!/") + 2, sURL.length()); // NOT
        // LOCALIZABLE
        JarEntry entry = jarfile.getJarEntry(fileName);
        if (entry == null)
            return null;

        String tempFileName = tempDir + System.getProperty("file.separator") + fileName;

        try {
            tempfile = PathFile.createFile(tempFileName, false, true);

            // delete old temp file
            if (tempfile.exists())
                tempfile.delete();

            out = new FileOutputStream(tempfile);
            in = jarfile.getInputStream(entry);
            int ch;
            while ((ch = in.read()) != -1) {
                out.write(ch);
            }
        } finally {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        }
        return tempfile;
    }
}
