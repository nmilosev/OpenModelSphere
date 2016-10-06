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

package org.modelsphere.jack.srtool.forward;

import java.util.Enumeration;

import org.modelsphere.jack.debug.TestException;
import org.modelsphere.jack.debug.Testable;
import org.modelsphere.jack.srtool.forward.EditionCode;
import org.modelsphere.jack.srtool.forward.exceptions.TemplateFormatException;

public final class TemplateEnumeration implements Testable, Enumeration {
    private String m_formatString;
    private boolean moreElements = true;
    private int start = 0;
    private int end = 0;
    private final String kNewLine = "$n;"; // NOT LOCALIZABLE, edition code
    private final String kTabSign = "$t;"; // NOT LOCALIZABLE, edition code
    private final String kDollarSign = "$d;"; // NOT LOCALIZABLE, edition code
    private final String kQuoteSign = "$q;"; // NOT LOCALIZABLE, edition code
    private static final char REPLACEMENT_CHAR = '\b'; // non-printable character used as temporary replacement character

    public TemplateEnumeration(String formatString) {
        // TODO: replace $ within quotes with an alternative character
        StringBuffer formatStringBuffer = new StringBuffer(formatString);

        int nbQuotes = 0;
        int len = formatStringBuffer.length();
        for (int i = 0; i < len; i++) {
            if (formatStringBuffer.charAt(i) == '\"') {
                nbQuotes++;
            } // end if

            if (formatStringBuffer.charAt(i) == '$') {
                // if number of quotes encountered if an odd number, we are
                // inside quotes
                if ((nbQuotes % 2) == 1) {
                    formatStringBuffer.setCharAt(i, REPLACEMENT_CHAR);
                }
            } // end if
        } // end for

        m_formatString = formatStringBuffer.toString();
    } // end TemplateEnumeration()

    private EditionCode isEditionCode(String tmpl) {
        EditionCode editionCode = null;

        if (tmpl.equals(kNewLine)) {
            editionCode = new EndOfLine();
        }
        if (tmpl.equals(kTabSign)) {
            editionCode = new TabSign();
        }
        if (tmpl.equals(kDollarSign)) {
            editionCode = new DollarSign();
        }
        if (tmpl.equals(kQuoteSign)) {
            editionCode = new DoubleQuoteSign();
        } else if (tmpl.charAt(1) == 'm') {
            char c = tmpl.charAt(2);
            if (c == '+') {
                editionCode = new Margin(true, tmpl.substring(3, tmpl.indexOf(';')));
            } else if (c == '-') {
                editionCode = new Margin(false, tmpl.substring(3, tmpl.indexOf(';')));
            }
            // TODO : generate an error
        }

        return editionCode;
    }

    /* get index of the next singleChar not followed by the itself */
    /* singleChar contained within double quotes are ignored */
    private int indexOfSingleChar(String str, char singleChar, int start) {
        int index;
        boolean done = false;
        do {
            index = str.indexOf(singleChar, start);
            if (index == -1)
                done = true;
            else {
                if (index == str.length() - 1) { // if last char
                    index = -1;
                    done = true;
                } else if (str.charAt(index + 1) == singleChar) {
                    start = index + 2; // skip because next character is the
                    // same
                } else {
                    done = true; // found it!
                } // end if
            } // end if
        } while (!done);

        return index;
    } // end indexOfSingleChar()

    /*
     * Remove consecutive characters, if any. For example, //removeConsecutiveChars("Mississippi",
     * 'p') gives "Mississipi" and //NOT LOCALIZABLE, comment
     * //removeConsecutiveChars("Mississippi", 's') gives "Misisippi" //NOT LOCALIZABLE, comment
     */
    private StringStructure removeConsecutiveChars(String rawStr, char singleChar) {
        StringStructure trimmedStr = new StringStructure("");
        int start = 0;
        int index;
        boolean done = false;

        do {
            index = rawStr.indexOf(singleChar, start);
            if (index == -1) {
                trimmedStr.str = trimmedStr.str + rawStr.substring(start);
                done = true;
            } else {
                if (index == rawStr.length() - 1) {
                    trimmedStr.str = trimmedStr.str + rawStr.substring(start);
                    done = true;
                } else if (rawStr.charAt(index + 1) == singleChar) {
                    trimmedStr.str = trimmedStr.str + rawStr.substring(start, index + 1);
                    start = index + 2;
                } else {
                    trimmedStr.str = trimmedStr.str + rawStr.substring(start, index);
                    start = index + 1;
                }
            }
        } while (!done);

        return trimmedStr;
    }

    // Overrides Enumeration
    public boolean hasMoreElements() {
        return moreElements;
    }

    // Overrides Enumeration
    public Object nextElement() {
        end = indexOfSingleChar(m_formatString, '$', start);
        if (end == -1) {
            moreElements = false;
            end = m_formatString.length();
        } else {
            int pos = m_formatString.indexOf(';', end);
            if (pos == -1) {
                String msg = TemplateFormatException.buildMessage(
                        TemplateFormatException.SEMI_COLON_IS_MISSING, m_formatString);
                throw new IllegalArgumentException(msg);
            }
        } // end if

        String s = m_formatString.substring(start, end);
        s = replaceInString(s, REPLACEMENT_CHAR, '$');
        StringStructure ss = removeConsecutiveChars(s, '$');

        start = 1 + m_formatString.indexOf(';', end);

        if (start == 0) {
            moreElements = false;
        } else {
            String tmpl;

            if (start < m_formatString.length())
                tmpl = m_formatString.substring(end, start);
            else
                tmpl = m_formatString.substring(end);

            validate(tmpl); // throws an exception if not valid
            ss.editionCode = isEditionCode(tmpl);
            if (ss.editionCode == null) {
                ss.stringRule = tmpl;
            } else {
                ss.stringRule = null;
            }
        } // end if
        return ss;
    } // end nextElement()

    // replace all 'oldChar' in 's' by 'newChar'
    private String replaceInString(String s, char oldChar, char newChar) {
        int len = s.length();
        StringBuffer buffer = new StringBuffer(s);
        for (int i = 0; i < len; i++) {
            if (buffer.charAt(i) == oldChar) {
                buffer.setCharAt(i, newChar);
            } // end if
        } // end for
        return buffer.toString();
    } // end replaceInString()

    // throw new
    private boolean validate(String s) {
        String name = s.substring(1, s.length() - 1);
        char firstChar = name.charAt(0);
        int len = name.length();

        // one-character name not allowed except for edition codes
        if (len == 1) {
            if (!EditionCode.isValidEditionCode(firstChar)) {
                String msg = TemplateFormatException.buildMessage(
                        TemplateFormatException.ONE_CHARACTER, s);
                throw new IllegalArgumentException(msg);
            }
        } // end if

        // first character must be strictly a letter (underscore and digit not
        // allowed)
        if (!Character.isLetter(firstChar)) {
            String msg = TemplateFormatException.buildMessage(
                    TemplateFormatException.MUST_BEGIN_BY_A_LETTER, s);
            throw new IllegalArgumentException(msg);
        } // end if

        // each character must be a letter (including underscore and digit)
        for (int i = 0; i < len; i++) {
            char c = name.charAt(i);
            if ((!Character.isJavaIdentifierPart(c)) && (c != '+') && (c != '-')) {
                String msg = TemplateFormatException.buildMessage(
                        TemplateFormatException.INVALID_CHARACTER, s);
                throw new IllegalArgumentException(msg);
            }
        } // end for

        return false;
    } // end isInvalid

    //
    // UNIT TEST
    //
    public void test() throws TestException {
        int nbSplittedElements = 0; // we expect to split the line into 11
        // elements
        String lineToSplit = "$m+2;$n;$n;class X {$m+2;$n;int field;$n;String msg = \"$n;$n;\";$m-2;$n;}//end class$m-2;$n;";
        TemplateEnumeration enumeration = new TemplateEnumeration(lineToSplit);
        while (enumeration.hasMoreElements()) {
            StringStructure ss = (StringStructure) enumeration.nextElement();
            System.out.println(ss.str);
            nbSplittedElements++;
        } // end while

        if (nbSplittedElements != 11) {
            String msg = "Expected number of splitted elements: 11.  Actual number of splitted elements: "
                    + nbSplittedElements;
            throw new TestException(msg);
        } // end if
    } // end test()

    public static void main(String[] args) {
        TemplateEnumeration testInstance = new TemplateEnumeration("");
        try {
            testInstance.test();
            System.out.println("Success!");
        } catch (TestException ex) {
            System.out.println(ex.toString());
        }
    } // end main()

} // end class TemplateEnumeration

