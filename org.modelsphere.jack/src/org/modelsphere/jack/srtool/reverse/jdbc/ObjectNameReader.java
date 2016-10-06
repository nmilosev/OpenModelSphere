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

package org.modelsphere.jack.srtool.reverse.jdbc; //org.modelsphere.jack.srtool.reverse;

import java.io.*;
import java.util.*;

import org.modelsphere.jack.io.LexerStream;
import org.modelsphere.jack.srtool.reverse.InputReadable;

public final class ObjectNameReader implements InputReadable {
    // ObjectBuilder builder;
    ArrayList nameList;

    public ObjectNameReader(ArrayList anArray) {
        nameList = anArray;
    }

    public void readInputFile(Reader reader) {

        BufferedReader buffer = new BufferedReader(reader);
        boolean done = false;
        String line;
        try {
            while (!done) {
                line = buffer.readLine();
                if (line == null)
                    done = true;
                else {
                    nameList.add(line);
                }
            }
        } catch (IOException ex) {
            // end of text?
        }

    } // end readInputFile()

    // INNER CLASSES
    class InputFileParserStream extends LexerStream {
        private final String eol = System.getProperty("line.separator");

        InputFileParserStream(Reader reader) {
            super(reader);

            // make eol significant
            super.eolIsSignificant(true);

            // Dashes & digits considered as ordinary characters anymore
            super.ordinaryChar('-');
            super.ordinaryChars('0', '9');

            // Accept underscores, dashes, quotes & digits as part of tokens
            super.wordChars('_', '_');
            super.wordChars('-', '-');
            super.wordChars('\'', '\'');
            super.wordChars('\"', '\"');
            super.wordChars('0', '9');

            // ignore lines starting with '--'
            super.defineLineComment("--");
        }

        // if word one of {'comments', 'oid_text', ..}, read attr text
        private boolean isTextAttr(String word) {
            String[] keywords = new String[] { "comments", "oid_text", "view_text", // NOT LOCALIZABLE, keywords
                    "data_default", "search", "part_value", "description", "trig_body", // NOT LOCALIZABLE, keywords
                    "text" }; // NOT LOCALIZABLE, keywords

            boolean found = false;
            for (int i = 0; i < keywords.length; i++) {
                if (word.equals(keywords[i])) {
                    found = true;
                    break;
                }
            }
            return found;
        }

        private void readStatement(String word) throws Exception {
        }
    }

    class ParseException extends Exception {
        ParseException(String msg) {
            super(msg);
        }
    }

    // MAIN FUNCTION
    /*
     * public static final void main(String[] args) { try { PrintObjectBuilder builder = new
     * PrintObjectBuilder(); InputFileParser parser = new InputFileParser(builder); File file = new
     * File("y.txt"); //NOT LOCALIZABLE, unit test FileInputStream istream = new
     * FileInputStream(file); InputStreamReader ireader = new InputStreamReader(istream);
     * BufferedReader bufReader = new BufferedReader(ireader); parser.readInputFile(bufReader); }
     * catch (Exception ex) { org.modelsphere.jack.debug.Debug.trace(ex.toString()); }
     * 
     * 
     * byte[] buf = new byte[256]; try { System.out.println("Press ENTER to exit.");
     * System.in.read(buf, 0, 256); } catch (IOException ex) { //ignore } }
     */
}
