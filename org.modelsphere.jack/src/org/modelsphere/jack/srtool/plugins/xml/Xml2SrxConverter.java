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

package org.modelsphere.jack.srtool.plugins.xml;

import java.io.*;
import javax.swing.JFileChooser;

import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.international.LocaleMgr;

//[INTERACTIVE MODE] SUPPORT

//This class is outside the scope of ModelSphere.  It contains a main() method
//to start a utility program that converts XML documents into SRX documents.

//Further enhancements:
//Support of interactive (GUI) mode, uncomment lines/blocks whose comments is:
//  '[INTERACTIVE MODE]'
//-m (main option) : default ON
//  if OFF, reverse everything (not just the main concepts)

public final class Xml2SrxConverter {
    private static String kTitle = LocaleMgr.screen.getString("Select");
    private static String kSelect = LocaleMgr.screen.getString("Select");
    transient private static String defaultDirectory = ApplicationContext
            .getDefaultWorkingDirectory();

    /* [INTERACTIVE MODE] SUPPORT */
    private File chooseSourceFile(String title) {
        JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(ExtensionFileFilter.xmlFileFilter);
        fc.setFileFilter(ExtensionFileFilter.xmlFileFilter);
        fc.setDialogTitle(title);
        File file = null;

        if (JFileChooser.APPROVE_OPTION == fc.showDialog(null, kSelect)) {
            file = fc.getSelectedFile();
            defaultDirectory = file.getParent();
        }

        return file;
    } // end chooseSourceFile()

    private static final String EXTENTION = ".srx"; // NOT LOCALIZABLE, file

    // extention
    private static void convert(String filename) throws IOException {
        File file = null;
        /* [INTERACTIVE MODE] SUPPORT */
        if (filename == null) {
            Xml2SrxConverter converter = new Xml2SrxConverter();
            file = converter.chooseSourceFile(kTitle);
        }
        if (file != null)
            filename = file.getAbsolutePath();

        if (filename != null) {
            // replace .xml by .srx
            String outputFile = filename.substring(0, filename.lastIndexOf('.')) + EXTENTION;

            XmlReverse reverse = new XmlReverse();
            StringWriter buffer = new StringWriter();
            FileWriter writer = new FileWriter(outputFile);
            String errMsg = reverse.parseFile(filename, writer, buffer);

            if (errMsg != null) {
                Debug.trace(errMsg);
                Debug.trace(buffer.toString());
            } else {
                Debug.trace("Success : file \"" + outputFile + "\" has been generated.");
            }
        } // filename is not null
    }

    private static void displayUnknownOption(String option) {
        Debug.trace("ERROR: option " + option + " is not valid.");
    }

    private static void displayInvalidExtension(String filename) {
        Debug.trace("ERROR: filename \"" + filename + "\" is not a .xml file.");
    }

    private static void displayHelp() {
        // Debug.trace("USAGE: java Xml2SrxConverter [-i] | [-m] <filename>");
        Debug.trace("USAGE: java Xml2SrxConverter [-i] <filename>");
        Debug.trace();
        Debug.trace("-i: interactive mode (GUI file chooser)"); // [INTERACTIVE
        // MODE] SUPPORT
        // Debug.trace("-m: main concepts only (domains, tables, columns, associations)");
        Debug.trace("filename: .xml file to convert");
        Debug.trace("RESULT: .srx file whose name has the same basename as the input file");
        Debug.trace();
        Debug.trace("EXAMPLE: java Xml2SrxConverter test.xmi");
        Debug.trace("RESULT:  a test.srx file is generated.");
        Debug.trace();
    }

    private static final String INTERACTIVE_OPTION = "-i"; // NOT LOCALIZABLE,
    // INTERACTIVE
    // OPTION
    private static final String XML_EXTENSION = "xml"; // NOT LOCALIZABLE, file

    // extension
    private static void process(String[] args) throws IOException {
        int nb = args.length;
        boolean isMain = true;

        if ((nb == 0) || (nb > 1)) {
            displayHelp();
        } else if (nb == 1) {
            String arg = args[0];
            if (arg.charAt(0) == '-') {
                // it's an option
                if (arg.equals(INTERACTIVE_OPTION)) {
                    convert(null); // [INTERACTIVE MODE] SUPPORT
                } else {
                    displayUnknownOption(arg);
                    displayHelp();
                }
            } else {
                // it's a file
                int index = arg.lastIndexOf(".");
                if (index == -1) {
                    displayInvalidExtension(arg);
                    displayHelp();
                } else {
                    String ext = arg.substring(index + 1);
                    if (!ext.equals(XML_EXTENSION)) {
                        displayInvalidExtension(arg);
                        displayHelp();
                    } else {
                        convert(arg);
                    }
                } // end if
            } // end if
        } // end if
    }

    public static void main(String[] args) throws IOException {
        process(args);
        System.exit(0);
    }
}
