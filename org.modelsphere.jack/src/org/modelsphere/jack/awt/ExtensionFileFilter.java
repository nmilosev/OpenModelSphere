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

package org.modelsphere.jack.awt;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.modelsphere.jack.international.LocaleMgr;

/**
 * A convenience implementation of FileFilter that filters out all files except for those type
 * extensions that it knows about.
 * 
 * Extensions are of the type '.foo', which is typically found on Windows and Unix boxes, but not on
 * Macinthosh. Case is ignored.
 * 
 * Example - create a new filter that filerts out all files but gif and jpg image files:
 * 
 * JFileChooser chooser = new JFileChooser(); ExtensionFileFilter filter = new ExtensionFileFilter(
 * new String[] {'gif', 'jpg'}, 'JPEG & GIF Images') chooser.addChoosableFileFilter(filter);
 * chooser.showOpenDialog(this);
 * 
 * @version 1.4 04/14/98
 * @author Jeff Dinkins (adapted by RCA for jack)
 */
public class ExtensionFileFilter extends FileFilter {

    public static final ExtensionFileFilter jpgFileFilter = new ExtensionFileFilter("jpg",
            LocaleMgr.misc.getString("jpgExtDesc")); //NOT LOCALIZABLE
    
    public static final ExtensionFileFilter pngFileFilter = new ExtensionFileFilter("png",
            LocaleMgr.misc.getString("pngExtDesc")); //NOT LOCALIZABLE

    public static final ExtensionFileFilter allImagesFilter = new ExtensionFileFilter(new String[] {
            "png", "jpg", "jpeg", "gif" }, LocaleMgr.misc.getString("allImagesExtDesc")); //NOT LOCALIZABLE

    public static final ExtensionFileFilter htmlFileFilter = new ExtensionFileFilter(new String[] {
            "html", "htm" }, LocaleMgr.misc.getString("htmlExtDesc")); //NOT LOCALIZABLE

    public static final ExtensionFileFilter xmlFileFilter = new ExtensionFileFilter("xml",
            LocaleMgr.misc.getString("xmlExtDesc")); //NOT LOCALIZABLE

    public static final ExtensionFileFilter txtFileFilter = new ExtensionFileFilter("txt",
            LocaleMgr.misc.getString("txtExtDesc")); //NOT LOCALIZABLE

    public static final ExtensionFileFilter sqlFileFilter = new ExtensionFileFilter("sql",
            LocaleMgr.misc.getString("sqlExtDesc")); //NOT LOCALIZABLE

    private String[] extensions = null;
    private String description = null;
    private String fullDescription = null;

    /**
     * Return the extension portion of the file's name . Return null if no extension.
     */
    public static String getExtension(File f) {
        String filename = f.getName();
        int i = filename.lastIndexOf('.');
        if (i > 0 && i < filename.length() - 1)
            return filename.substring(i + 1).toLowerCase();
        return null;
    }

    /**
     * Creates a file filter that accepts the given file type. Example: new
     * ExtensionFileFilter('jpg', 'JPEG Image Images');
     */
    public ExtensionFileFilter(String extension, String description) {
        this(new String[] { extension }, description);
    }

    /**
     * Creates a file filter from the given string array and description. Example: new
     * ExtensionFileFilter(new String[] {'gif', 'jpg'}, 'Gif and JPG Images');
     */
    public ExtensionFileFilter(String[] extensions, String description) {
        this.extensions = new String[extensions.length];
        for (int i = 0; i < extensions.length; i++)
            this.extensions[i] = extensions[i].toLowerCase();
        this.description = description;
    }

    /**
     * Return true if this file should be shown in the directory pane, false if it shouldn't.
     * 
     * Files that begin with "." are ignored.
     */
    public boolean accept(File f) {
        if (f == null)
            return false;
        if (f.isDirectory())
            return true;
        return isValidExtension(getExtension(f));
    }

    public boolean isValidExtension(String extension) {
        if (extension != null) {
            for (int i = 0; i < extensions.length; i++) {
                if (extension.equals(extensions[i]))
                    return true;
            }
        }
        return false;
    }

    public String getExtension() {
        return extensions[0];
    }

    public String[] getExtensions() {
        return extensions;
    }

    /**
     * Returns the human readable description of this filter. For example: 'JPEG and GIF Image Files
     * (*.jpg, *.gif)'
     */
    public String getDescription() {
        if (fullDescription == null) {
            fullDescription = description + " (";
            for (int i = 0; i < extensions.length; i++) {
                if (i != 0)
                    fullDescription += ", ";
                fullDescription += "*." + extensions[i];
            }
            fullDescription += ")";
        }
        return fullDescription;
    }
}
