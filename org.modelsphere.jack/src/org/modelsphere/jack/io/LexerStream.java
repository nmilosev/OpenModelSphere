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

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * Parse Stream
 * 
 */
// known subclass : org.modelsphere.jack.srtool.reverse.SQLLexerStream
public class LexerStream extends StreamTokenizer {

    // note: values from -1 to -4 are reversed by superclass
    public static final int TT_COMMENT = -5;
    public static final int TT_STATEMENT = -6;
    public static final int FORCED_DOUBLE_QUOTE = 64; // @

    private ArrayList commentList = new ArrayList();
    private StatementList statementList = new StatementList();
    private int quoteChar = '\0';
    private boolean secondDoubleQuoteToken = false;
    private boolean significant = false; // default value in super class.

    public LexerStream(Reader reader) {
        super(reader);
        super.eolIsSignificant(true);
    }

    public void eolIsSignificant(boolean isSignificant) {
        significant = isSignificant;
    }

    public void quoteChar(int ch) {
        super.quoteChar(ch);
        quoteChar = ch;
    }

    public void defineLineComment(String str) {
        commentList.add(str);
    }

    public void defineStatement(String kw, char terminator) {
        statementList.add(new Statement(kw, terminator));
    }

    // read the rest of the list
    private void readComment() throws IOException {
        boolean endOfLine = false;
        int tokenID;
        do {
            tokenID = super.nextToken();
            if ((TT_EOL == tokenID) || (TT_EOF == tokenID))
                endOfLine = true;

        } while (!endOfLine);
    }

    // read next tokens until meet terminator
    private void readStatement() throws IOException {
        final char terminator = statementList.getTerminator(sval);
        int tokenID;
        boolean endOfStatement = false;
        secondDoubleQuoteToken = false;

        String statement = sval + " ";
        do {
            tokenID = super.nextToken();
            switch (tokenID) {
            case TT_EOL:
                if (significant)
                    statement = statement.concat((char) TT_EOL + "");
                break;
            case TT_NUMBER:
                // if it's an integer
                if ((nval % 1) == 0)
                    statement = statement.concat((int) nval + "");
                else
                    statement = statement.concat(nval + "");
                break;
            case TT_WORD:
                statement = statement.concat(sval + " ");
                break;
            default:
                if (tokenID == FORCED_DOUBLE_QUOTE) {
                    if (secondDoubleQuoteToken)
                        statement = statement.substring(0, statement.length() - 1); // delete the last space
                    // added after each word
                    // (TT_WORD)

                    statement = statement.concat(String.valueOf('"'));
                    secondDoubleQuoteToken = !secondDoubleQuoteToken;
                } else if (tokenID == quoteChar)
                    statement = statement.concat(String.valueOf((char) tokenID) + sval
                            + String.valueOf((char) tokenID) + " ");
                else if (tokenID != terminator)
                    statement = statement.concat(String.valueOf((char) tokenID));
                break;
            }

            if ((tokenID == terminator) || (tokenID == TT_EOF))
                endOfStatement = true;
        } while (!endOfStatement);

        sval = statement;
    }

    public int nextToken() throws IOException {
        int tokenID = super.nextToken();

        // if EOL not significant, skip them
        if (!significant) {
            while (tokenID == TT_EOL)
                tokenID = super.nextToken();
        }

        if (TT_WORD == tokenID) {
            // if COMMENT
            if (commentList.contains(sval)) {
                readComment();
                tokenID = TT_COMMENT;
            } // else if SEPARATOR
            else if (statementList.contains(sval)) {
                readStatement();
                tokenID = TT_STATEMENT;
            }
        } // end if

        return tokenID;
    }

    // INNER CLASSES
    private class Statement {
        String keyword;
        char terminator;

        Statement(String aKw, char aTerminator) {
            keyword = aKw;
            terminator = aTerminator;
        }
    }

    private class StatementList extends ArrayList {
        StatementList() {
        }

        public boolean contains(Object o) {
            boolean contained = false;
            Iterator iterator = this.iterator();

            while (iterator.hasNext()) {
                Statement statement = (Statement) iterator.next();
                if (o instanceof String) {
                    if (statement.keyword.equalsIgnoreCase((String) o)) {
                        contained = true;
                        break;
                    }
                } else {
                    if (statement.keyword.equals(o)) {
                        contained = true;
                        break;
                    }
                }
            } // end while

            return contained;
        }

        char getTerminator(String kw) {
            char terminator = '\0';
            Iterator iterator = this.iterator();

            while (iterator.hasNext()) {
                Statement statement = (Statement) iterator.next();
                if (statement.keyword.equalsIgnoreCase(kw)) {
                    terminator = statement.terminator;
                    break;
                }
            } // end while

            return terminator;
        }
    }

    // MAIN FUNCTION : read tokens from SQL
    /*
     * static final void main(String[] args) {
     * 
     * try {
     * 
     * java.net.URL url =
     * org.modelsphere.sms.oo.OOModule.class.getResource("target/Xtr8_dba_owner.sql" );
     * BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
     * LexerStream stream = new LexerStream(reader);
     * 
     * //ignore lines starting with the following words stream.defineLineComment("ACCEPT"); //NOT
     * LOCALIZABLE, used in main() function only stream.defineLineComment("PROMPT"); //NOT
     * LOCALIZABLE, used in main() function only stream.defineLineComment("REM"); //NOT LOCALIZABLE,
     * used in main() function only stream.defineLineComment("SET"); //NOT LOCALIZABLE, used in
     * main() function only stream.defineLineComment("SPOOL"); //NOT LOCALIZABLE, used in main()
     * function only
     * 
     * //Each SELECT statement begins by SELECT keyword and ends by ';'
     * stream.defineStatement("SELECT", ';'); //NOT LOCALIZABLE, used in main() function only
     * 
     * //In SQL, use single quote as quote character stream.quoteChar('\'');
     * 
     * //Accept underscores in identifiers stream.wordChars('_', '_');
     * 
     * //Read the 80 first tokens for (int i=0; i<80; i++) { int tokenID = stream.nextToken();
     * 
     * //only print statement tokens switch (tokenID) { case TT_STATEMENT: Debug.trace(tokenID +
     * ": " + stream.sval + "\n"); break; default: break; } } //end for } catch (Exception ex) {
     * //ignore }
     * 
     * try { byte[] buf = new byte[256]; System.in.read(buf, 0, 255); } catch (IOException ex) {
     * //ignore it }
     * 
     * }
     */
}
