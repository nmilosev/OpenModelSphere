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
package org.modelsphere.jack.baseDb.assistant;

import java.io.*;
import java.net.URL;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.util.DirectoryScanner;

public class JSearch {

    protected Vector vecFiles;
    protected Vector resultURL = new Vector();
    protected Vector titles = new Vector();
    protected Vector url = new Vector();

    protected URL foundURL;

    protected String kFileBuild = LocaleMgr.screen.getString("File:");
    protected String title;
    protected StringBuffer buffer;

    protected int p = 0;
    protected int q = 0;

    public JSearch(String searchTextField, String helpDir) {

        DirectoryScanner dirScan = new DirectoryScanner();
        File fileDir = new File(helpDir);
        JFileChooser fileChooser = new JFileChooser(fileDir);
        FileFilter fileFilter = fileChooser.getFileFilter();
        vecFiles = dirScan.getFilesList(fileDir, true, fileFilter);

        for (int b = 0; b < vecFiles.size(); b++) {
            String inFile = vecFiles.elementAt(b).toString();

            try {
                FileReader fileReader = new FileReader(inFile);
                StreamTokenizer st = new StreamTokenizer(fileReader);
                st.wordChars('<', '>');
                st.wordChars('/', '>');
                try {
                    for (int tokenType = st.nextToken(); tokenType != StreamTokenizer.TT_EOF; tokenType = st
                            .nextToken()) {
                        switch (tokenType) {
                        case StreamTokenizer.TT_WORD:
                            if (st.sval.equalsIgnoreCase(searchTextField)) {
                                foundURL = new URL(kFileBuild + inFile);
                                resultURL.addElement(foundURL);
                                if (p >= 1) {
                                    if (foundURL.equals(resultURL.elementAt(p - 1))) {
                                        resultURL.removeElementAt(p);
                                        p--;
                                    }
                                }
                                p++;
                                // System.out.println("title: " +
                                // scanTitle(inFile));
                            }
                            break;
                        }
                    }
                } catch (IOException g) {
                    System.out.println("I/O failure" + g);
                }
            } catch (FileNotFoundException e) {
                System.out.println("Can't find " + e);
            }
        }
    }

    public Vector getSearchResultVector() {
        return resultURL;
    }

    public Vector getTitle() {
        scanTitle();

        return titles;
    }

    public Vector scanTitle() {
        try {
            Vector url = getSearchResultVector();

            for (int x = 0; x < url.size(); x++) {
                String urlElement = url.elementAt(x).toString().substring(5);
                // System.out.println("urlElement :" + urlElement);
                FileReader fileReader = new FileReader(urlElement);
                StreamTokenizer st = new StreamTokenizer(fileReader);
                st.parseNumbers();
                st.wordChars('<', '>');

                try {
                    for (int tokenType = st.nextToken(); tokenType != StreamTokenizer.TT_EOF; tokenType = st
                            .nextToken()) {
                        switch (tokenType) {
                        case StreamTokenizer.TT_WORD:
                            if (st.sval.equalsIgnoreCase("<title>")) // NOT
                            // LOCALIZABLE
                            {
                                st.nextToken();
                                buffer = new StringBuffer(st.sval);
                                while (!st.sval.equalsIgnoreCase("<")) {
                                    st.nextToken();
                                    buffer.append(" " + st.sval);
                                    title = buffer.toString().replace('<', ' ');
                                }
                            }
                        }
                    }
                    titles.addElement(title);
                    // System.out.println("Vector titles :" + title);
                } catch (IOException g) {
                    System.out.println("I/O failure" + g);
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("Can't find " + e);
        }

        return titles;
    }

}
