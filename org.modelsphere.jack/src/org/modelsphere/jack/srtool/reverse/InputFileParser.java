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

package org.modelsphere.jack.srtool.reverse;

import java.io.*;

import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.io.TemporaryFileWriter;

public final class InputFileParser implements InputReadable, SRXConstants {
    private Controller controller;

    ObjectBuilder builder;

    public InputFileParser(ObjectBuilder aBuilder) {
        builder = aBuilder;
    }

    public void setControler(Controller controller) {
        this.controller = controller;
    }

    private String isNewOccurrence(String line) {
        String newOccurrence = null;
        String lowerCase = line.toLowerCase();

        if (lowerCase.startsWith(kOccurrenceTypeAlt)) {
            newOccurrence = lowerCase.substring(kOccurrenceTypeAlt.length());
        } else if (lowerCase.startsWith(kOccurrenceType)) {
            newOccurrence = lowerCase.substring(kOccurrenceType.length());
        }

        return newOccurrence;
    }

    private String getFirstWord(String line) {
        String firstWord = null;
        String lowerCase = line.toLowerCase();
        int index = lowerCase.indexOf(' ');
        if (index != -1) {
            firstWord = lowerCase.substring(0, lowerCase.indexOf(' '));
        }

        return firstWord;
    }

    // if word one of {"comments", "oid_text", .."}, read attr text //NOT
    // LOCALIZABLE
    private boolean isTextAttr(String word) {
        boolean found = false;
        for (int i = 0; i < kTextKeywords.length; i++) {
            String lowercase = word.toLowerCase();
            if (lowercase.equals(kTextKeywords[i])) {
                found = true;
                break;
            }
        }
        return found;
    }

    private void readLineAttrStatement(String attr, String line) throws Exception {
        String value = line.trim();
        builder.makeStringAttr(attr, value);
    }

    private void readTextAttrStatement(String attr, String line, RandomAccessFile file)
            throws Exception {
        String value = "";
        do {
            int index = line.indexOf(kEOT);
            if (index == -1) {
                value += line + '\n';
            } else {
                if (index == 0) {
                    // remove last \n before the kEOT (part of the srx format)
                    if (value.length() > 0 && value.lastIndexOf('\n') == (value.length() - 1))
                        value = value.substring(0, value.length() - 1);
                } else
                    value += line.substring(0, index - 1); // -1 is for removing
                // the space before
                // kEOT (part of the
                // srx format)
                break;
            }
            line = file.readLine();
        } while (line != null);

        builder.makeTextAttr(attr, value);
    }

    public void readInputFile(Reader reader) throws Exception {
        // dump reader's output into a temporary file
        TemporaryFileWriter writer = new TemporaryFileWriter();
        writer.dump(reader);
        writer.close();

        // read from the temporary file
        File tempFile = writer.getFile();
        RandomAccessFile file = new RandomAccessFile(tempFile, "r"); // NOT
        // LOCALIZABLE,
        // io
        // constant

        try {
            String line;
            while (controller == null || (controller != null && controller.checkPoint())) {
                line = file.readLine();
                if (line == null) {
                    break;
                }

                String newOccurrence = isNewOccurrence(line);
                if (newOccurrence != null) {
                    builder.makeOccurrence(newOccurrence.trim());
                } else {
                    String firstWord = getFirstWord(line);
                    if (firstWord != null) { // otherwise ignore
                        if (firstWord.startsWith("--")) {
                            // ignore comments
                        } else if (!isTextAttr(firstWord)) {
                            // if word not one of {"comments", "oid_text", .."},
                            // read attr string
                            line = line.substring(1 + firstWord.length());
                            readLineAttrStatement(firstWord, line);
                        } else {
                            // else read attr text
                            line = line.substring(1 + firstWord.length());
                            readTextAttrStatement(firstWord, line, file);
                        } // end if
                    } // end if
                } // end if
            } // end while
        } catch (EOFException ex) {
            // Excepting this exception to exit from the loop
        }

        /*
         * InputFileParserStream parser = new InputFileParserStream(reader);
         * 
         * //read until end of file boolean done = false; while (!done && ( controler == null ||
         * (controler != null && controler.checkPoint()))) { //try { int tokenID =
         * parser.nextToken();
         * 
         * //only keep statement tokens switch (tokenID) { case LexerStream.TT_WORD:
         * parser.readStatement(parser.sval); break; case LexerStream.TT_EOF: done = true; break;
         * default: break; } } //end while
         */
    } // end readInputFile()

    // INNER CLASSES
    /*
     * class InputFileParserStream extends LexerStream { private final String eol =
     * System.getProperty("line.separator"); private Reader m_reader;
     * 
     * InputFileParserStream(Reader reader) { super(reader); m_reader = reader;
     * Debug.trace("isMarkSupported ? " + (reader.markSupported() ? "true" : "false")); //NOT
     * LOCALIZABLE, trace()
     * 
     * //make eol significant super.eolIsSignificant(true);
     * 
     * //make the parser case sensitive, to get the right table names super.lowerCaseMode(false);
     * 
     * //Dashes & digits now considered as ordinary characters super.ordinaryChar('-');
     * super.ordinaryChar('.'); super.ordinaryChars('0', '9');
     * 
     * //Accept the following characters as part of tokens super.wordChars('\"', '\"'); // double
     * quote super.wordChars('\'', '\''); // single quote super.wordChars('-', '-'); // dash
     * super.wordChars('_', '_'); // underscore super.wordChars('0', '9'); // digits
     * super.wordChars('Ç', 'Ü'); // characters with accents (é, à, etc.)
     * 
     * //ignore lines starting with '--' //super.defineLineComment(kComment); }
     * 
     * private void readAttrLineStatement(String lowercase) throws Exception { //read until end of
     * line boolean done = false; String attrValue = kEmpty; while (! done) { int tokenID =
     * nextToken(); switch(tokenID) { case TT_EOL: case TT_EOF: done = true; break; case TT_WORD:
     * attrValue += sval; break; case ',': attrValue += ','; break; default: if
     * (Character.isLetterOrDigit((char)tokenID)) { attrValue += tokenID; } break; } //end switch }
     * //end while
     * 
     * builder.makeStringAttr(lowercase, attrValue); } //end readAttrLineStatement()
     * 
     * private final int READ_AHEAD_LIMIT = 1024; private void readAttrTextStatement(String
     * lowercase) throws Exception { boolean done = false; boolean lastTokenWasAWord = false; String
     * textValue = kEmpty; m_reader.mark(READ_AHEAD_LIMIT);
     * 
     * while (! done) { int tokenID = nextToken(); switch(tokenID) { case TT_EOF: done = true;
     * break; case TT_EOL: textValue += eol; lastTokenWasAWord = false; break; case TT_WORD: if
     * (sval.equals(kEOT)) done = true; else { if (lastTokenWasAWord) { textValue += " "; }
     * textValue += sval; lastTokenWasAWord = true; } break; default: //if it's a printable
     * character (including blanks) if ((tokenID > 31) && (tokenID < 128)) { textValue +=
     * (char)tokenID; lastTokenWasAWord = false; } break; } //end switch } //end while
     * 
     * builder.makeTextAttr(lowercase, textValue); } //end readAttrTextStatement()
     * 
     * private void readStatement(String word) throws Exception { int tokenID; String lowercase =
     * word.toLowerCase();
     * 
     * //if comment if (word.substring(0, 2).equals(kComment)) {
     * 
     * //read all tokens until end of line boolean done = false; String comment = kEmpty; while (!
     * done) { tokenID = nextToken(); switch(tokenID) { case TT_EOL: case TT_EOF: done = true;
     * break; case TT_WORD: comment = comment + sval; break; default: break; } } //end while
     * builder.makeComment(comment); } else if ((lowercase.equals(kOccurrenceType)) ||
     * (lowercase.equals(kOccurrenceTypeAlt))) {
     * 
     * //read occurrence type tokenID = nextToken(); if (tokenID != TT_WORD) throw new
     * ParseException("Expecting word", -1); //NOT LOCALIZABLE
     * 
     * builder.makeOccurrence(sval);
     * 
     * //expect eol tokenID = nextToken(); if (tokenID != TT_EOL) throw new
     * ParseException("Expecting EOL", -1); //NOT LOCALIZABLE } else { //if word not one of
     * {"comments", "oid_text", .."}, read attr string //NOT LOCALIZABLE if (!isTextAttr(lowercase))
     * { readAttrLineStatement(lowercase); } else { //else read attr text
     * readAttrTextStatement(lowercase); } } } //end readStatement() } //InputFileParserStream()
     */

    // MAIN FUNCTION
    /*
     * public static final void main(String[] args) { try { PrintObjectBuilder builder = new
     * PrintObjectBuilder(); InputFileParser parser = new InputFileParser(builder); File file = new
     * File("reverse.srx"); //NOT LOCALIZABLE, unit test
     * 
     * FileInputStream istream = new FileInputStream(file); InputStreamReader ireader = new
     * InputStreamReader(istream); BufferedReader bufReader = new BufferedReader(ireader);
     * parser.readInputFile(bufReader); } catch (Exception ex) { Debug.trace(ex.toString());
     * ex.printStackTrace(System.out); }
     * 
     * 
     * byte[] buf = new byte[256]; try { Debug.trace("Press ENTER to exit."); System.in.read(buf, 0,
     * 256); } catch (IOException ex) { //ignore } }
     */
}
