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

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;

import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public final class XmlReverse {
    private static final String EXTENSION = "xml"; // NOT LOCALIZABLE, file extension
    private static final String DISPLAY_NAME = LocaleMgr.screen.getString("XMIFiles");
    private static final String PARSING_ERROR = LocaleMgr.message.getString("ParsingError");
    private static final String PARSING_ERROR_PATTERN = LocaleMgr.message
            .getString("ParsingErrorPattern");
    private static final String FILE = LocaleMgr.message.getString("file");

    // TODO: add encoding argument for localization
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    public String getExtension() {
        return EXTENSION;
    }

    public boolean canReverse(File file) {
        boolean reversable = false;

        String filename = file.getName();
        int index = filename.indexOf('.');
        if (index != -1) {
            String ext = filename.substring(index + 1);
            if (ext.equals(EXTENSION)) {
                reversable = true;
            }
        }

        return reversable;
    }

    String parseFile(String filename, Writer writer, StringWriter logBuffer) {
        String errMsg = null;

        try {
            String uri = FILE + ":" + new File(filename).getAbsolutePath();

            //
            // turn it into an in-memory object.
            //
            XmlParser.setWriter(writer);
            XmlParser.setNativeLineSeparator(true); // write in a file
            Parser parser = XmlParser.getParser();
            parser.setDocumentHandler(new XmlParser());
            parser.setErrorHandler(new XmlParser.MyErrorHandler());
            parser.parse(uri);
            XmlParser.setNativeLineSeparator(false); // restore for furthur
            // reverse

        } catch (SAXParseException err) {
            errMsg = PARSING_ERROR;
            String pattern = PARSING_ERROR_PATTERN;
            String msg = MessageFormat.format(pattern, new Object[] { PARSING_ERROR,
                    err.getLineNumber() + "", err.getSystemId() + "" });
            logBuffer.write(msg);
            logBuffer.write("   " + err.getMessage());

        } catch (SAXException e) {
            Exception x = e;
            if (e.getException() != null)
                x = e.getException();
            x.printStackTrace();

        } catch (Throwable t) {
            t.printStackTrace();
        }

        return errMsg;
    } // end parseFile()

    private static int PRIORITY = 15;

    public int getPriority() {
        return PRIORITY;
    }

}
