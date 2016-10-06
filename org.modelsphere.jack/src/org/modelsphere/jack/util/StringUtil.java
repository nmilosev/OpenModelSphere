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

import java.awt.FontMetrics;
import java.io.File;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.modelsphere.jack.debug.TestException;
import org.modelsphere.jack.debug.Testable;
import org.modelsphere.jack.international.LocaleMgr;

/**
 * Utility methods for String processing.
 * 
 * @author Marco Savard
 */
public final class StringUtil implements Testable {

    public static final String EOL = System.getProperty("line.separator");
    public static final String k3Dots = "...";
    private static final String UNDEFINED = LocaleMgr.screen.getString("Undefined");
    private static final String DEC_FORMAT = "0.0#####"; // at least 1 decimal

    // and at most 5
    // more decimals

    public static int compareString(String str1, String str2) {
        if (str1 == null)
            str1 = "";
        if (str2 == null)
            str2 = "";
        return str1.compareTo(str2);
    }

    /**
     * Capitalize string to make it a title (uppercase the 1st character)
     */
    public static String titleString(String s) {
        String fc = s.substring(0, 1);
        return fc.toUpperCase() + s.substring(1, s.length());
    }

    public static String getFileName(String path) {
        int i = path.lastIndexOf(File.separatorChar);
        return (i == -1 ? path : path.substring(i + 1));
    }

    /**
     * Replace invalid characters in file name
     * 
     * @param name
     *            : file name to validate
     * @param replaceSpace
     *            : if true replace space by "_"
     * @return a valid file name
     */
    public static String getValideFileName(String name, boolean replaceSpace) {
        String invalidStr = (replaceSpace ? " |\\/:*?\"<>()" : "|\\/:*?\"<>()");
        char[] invalidChar = invalidStr.toCharArray();

        for (int i = 0; i < invalidChar.length; i++) {
            name = name.replace(invalidChar[i], '_');
        }
        while (name.indexOf("___") != -1) {
            name = replaceWords(name, "___", "_");
        }
        while (name.indexOf("__") != -1) {
            name = replaceWords(name, "__", "_");
        }
        return name;
    }

    public static String appendFileExt(String path, String ext) {
        if (getFileName(path).indexOf('.') == -1)
            path = path + '.' + ext;
        return path;
    }

    /**
     * Returns the position of two consecutive characters, -1 if not found Example : int pos =
     * consecutiveCharacters("Tennessee", 'e'); //NOT LOCALIZABLE returns 7 (position of the third
     * e).
     * 
     * Note: used with consecutiveCharacters(text, ch1, ch2), it is a faster alternative to
     * String.indexOf(text, str);
     */
    public static int consecutiveCharacters(String text, char ch) {
        int pos = -1;
        int start = 0;
        int len = text.length();

        do {
            pos = text.indexOf(ch, start);
            if (pos == -1) {
                break;
            }

            if (pos == (len - 1)) {
                pos = -1;
                break;
            }

            if (text.charAt(pos + 1) == ch) {
                break;
            }

            start = pos + 1;
            pos = -1; // continue the loop
        } while (pos == -1);

        return pos;
    } // end consecutiveCharacters()

    /**
     * Returns the position of two consecutive characters, -1 if not found Example : int pos =
     * consecutiveCharacters("Tennessee", 'a', 'z'); //NOT LOCALIZABLE returns 2 (position of the
     * first n).
     */
    public static int consecutiveCharacters(String text, char rangeStart, char rangeEnd) {
        // assert rangeStart > rangeEnd
        int currPos = -1;
        int firstPos = Integer.MAX_VALUE;
        for (char ch = rangeStart; ch <= rangeEnd; ch++) {
            currPos = consecutiveCharacters(text, ch);
            if ((currPos != -1) && (firstPos > currPos)) {
                firstPos = currPos;
            }
        } // end for

        return (firstPos == Integer.MAX_VALUE) ? -1 : firstPos;
    } // end consecutiveCharacters()

    public static String truncateFileName(String fileName) {
        return truncateFileName(fileName, 50);
    }

    public static String truncateFileName(String fileName, int maxlen) {
        if (maxlen < 5)
            return fileName.length() <= maxlen ? fileName : fileName.substring(0, maxlen);

        int len = fileName.length();
        if (len <= maxlen)
            return fileName;

        String shortName = fileName;
        int i1 = fileName.indexOf(File.separatorChar);
        int i2 = fileName.lastIndexOf(File.separatorChar);
        if (i1 != i2) {
            while (true) {
                int i = fileName.indexOf(File.separatorChar, i1 + 1);
                if (i + 4 + len - i2 > maxlen)
                    break;
                i1 = i;
                i = fileName.lastIndexOf(File.separatorChar, i2 - 1);
                if (i1 + 4 + len - i > maxlen)
                    break;
                i2 = i;
            }
            shortName = fileName.substring(0, i1 + 1) + k3Dots + fileName.substring(i2);
        }
        if (shortName.length() > maxlen)
            shortName = shortName.substring(0, maxlen - 3) + k3Dots;
        return shortName;
    }

    public static String truncateFileName(String fileName, FontMetrics fm, int maxWidth) {
        if (fm.stringWidth(fileName) <= maxWidth)
            return fileName;

        String shortName = fileName;
        int i2 = fileName.lastIndexOf(File.separatorChar);
        if (i2 != -1) {
            shortName = k3Dots + fileName.substring(i2);
            if (fm.stringWidth(shortName) <= maxWidth) {
                int i1 = -1;
                while (true) {
                    int i = fileName.indexOf(File.separatorChar, i1 + 1);
                    if (i == i2)
                        break;
                    String name = fileName.substring(0, i + 1) + k3Dots + fileName.substring(i2);
                    if (fm.stringWidth(name) > maxWidth)
                        break;
                    i1 = i;
                    i = fileName.lastIndexOf(File.separatorChar, i2 - 1);
                    if (i == i1)
                        break;
                    name = fileName.substring(0, i1 + 1) + k3Dots + fileName.substring(i);
                    if (fm.stringWidth(name) > maxWidth)
                        break;
                    i2 = i;
                }
                return fileName.substring(0, i1 + 1) + k3Dots + fileName.substring(i2);
            }
        }
        return truncateString(shortName, fm, maxWidth, k3Dots);
    }

    /**
     * Use this method to obtains a new String for a Swing Component Text. This method will ensure
     * that the string will be visible and if not, this string will be troncated and the extension
     * added.
     * 
     * @param string
     *            : The string to be troncated
     * @param fm
     *            : The FontMetrics of the JComponent that will display the String
     * @param maxWidth
     *            : The Maximum width that the string may use (usually the JComponent.getWidth())
     * @param extension
     *            : The extension to add to the String if it get troncated
     */

    public static String truncateString(String string, FontMetrics fm, int maxWidth,
            String extension) {
        if (extension == null)
            extension = "";

        int maxCharWidth = fm.getMaxAdvance();
        String newString = string;
        int charCount = string.length();
        while (true) {
            int width = fm.stringWidth(newString);
            if (width <= maxWidth)
                break;
            charCount = charCount - Math.max(((width - maxWidth) / maxCharWidth), 1);
            if (charCount <= 0) {
                newString = extension;
                break;
            }
            newString = string.substring(0, charCount) + extension;
        }
        return newString;
    }

    public static String truncateString(String string, FontMetrics fm, int maxwidth) {
        return StringUtil.truncateString(string, fm, maxwidth, k3Dots);
    }

    /*
     * Replace words in a string (same as String.replace(), but replace words instead of
     * characters).
     */
    public static String replaceWords(String text, String oldWord, String newWord) {
        String filteredText = "";
        int lastIndex = 0;
        int index;

        do {
            index = text.indexOf(oldWord, lastIndex);
            if (index == -1) {
                filteredText = filteredText + text.substring(lastIndex);
                break;
            }

            filteredText = filteredText + text.substring(lastIndex, index) + newWord;
            lastIndex = index + oldWord.length();
        } while (true);

        return filteredText;
    }

    public static String pathStr(String s) {
        return s.replace('/', File.separatorChar);
    }

    /*
     * Check if the string is empty (length = 0, only space char)
     */
    public static boolean isEmptyString(String s) {
        boolean empty = true;
        if (s == null)
            return empty;
        if (s.length() > 0) {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) != ' ') {
                    empty = false;
                    break;
                }
            } // end for
        }

        return empty;
    }

    /*
     * Use this method to insert a String "strToInsert" at position "pos" of the input String "str"
     * //NOT LOCALIZABLE
     * 
     * @param str: The input font
     * 
     * @param strToInsert: The string to insert
     * 
     * @param pos: The position to insert the string
     * 
     * @return String: return a new string. If pos > str.length return str
     */
    public static String insert(String str, String strToInsert, int pos) {
        String newString;
        if (pos > str.length())
            return str;
        newString = str.substring(0, pos);
        newString = newString.concat(strToInsert);
        newString = newString.concat(str.substring(pos));
        return newString;
    }

    // Convert the specified String to ensure that html special characters are
    // converted to their
    // html tag.
    // Supported characters are <>&"©®
    public static String replaceHTMLSpecialCharacters(String s) {
        if (s == null)
            return null;
        StringWriter writer = new StringWriter();

        int index = -1;
        StringTokenizer st = new StringTokenizer(s, "<>&\"©®", true); // NOT
        // LOCALIZABLE
        while (st.hasMoreElements()) {
            String token = (String) st.nextElement();
            if (token.equals("<"))
                writer.write("&lt;"); // NOT LOCALIZABLE
            else if (token.equals(">"))
                writer.write("&gt;"); // NOT LOCALIZABLE
            else if (token.equals("&"))
                writer.write("&amp;"); // NOT LOCALIZABLE
            else if (token.equals("\""))
                writer.write("&quot;"); // NOT LOCALIZABLE
            else if (token.equals("©"))
                writer.write("&copy;"); // NOT LOCALIZABLE
            else if (token.equals("®"))
                writer.write("&reg;"); // NOT LOCALIZABLE
            else
                writer.write(token);
        }

        return writer.toString();
    }

    /**
     * Returns how many times 'ch' is contained in string 'text'. Returns 0 if 'ch' doesn't appear
     * in 'text'
     */
    public static int getNbCharacters(String text, char ch) {
        int nbChars = 0;

        int nb = text.length();
        for (int i = 0; i < nb; i++) {
            if (text.charAt(i) == ch) {
                nbChars++;
            }
        }

        return nbChars;
    }

    public static Integer parseInteger(String s) {
        String buffer = new String();
        Integer newInteger = null;

        if (s == null)
            return newInteger;

        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isDigit(c))
                buffer = buffer + new String(new char[] { c });
        }
        if (buffer.length() > 0) {
            try {
                newInteger = Integer.decode(buffer);
            } catch (Exception e) {
            };
        }
        return newInteger;
    }

    public static String splitInSeveralRows(String originalText, int rowSize) {
        String formattedText = "";

        int charInRows = 0;
        // words are separated by blank or period characters.
        StringTokenizer st = new StringTokenizer(originalText, " .\n", true); // NOT
        // LOCALIZABLE,
        // escape
        // code
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            int len = token.length();

            // if enough room in the current row to add new token?
            if ((len == 1) || (charInRows + len <= rowSize)) {
                formattedText += token;
                charInRows += token.length();
            } else {
                formattedText += "\n" + token; // NOT LOCALIZABLE, escape code
                charInRows = token.length();
            } // end if
        } // end while

        return formattedText;
    }

    //
    // Gives a string representation to an object (most sophisticated than
    // obj.toString() )
    // Used by :
    // org.modelsphere.jack.baseDb.screen : DefaultRenderer,
    // MultiDefaultRenderer
    // org.modelsphere.jack.graphic.zone : TextAreaCellEditor,
    // TextFieldCellEditor
    // org.modelsphere.sms.be.graphic : BEUseCaseBox
    //
    public static String getDisplayString(Object obj) {
        String str = null;

        if (obj == null) {
            return "";
        }

        // special case : Float
        if (obj instanceof Float) {
            Float f = (Float) obj;
            if (f.isNaN()) {
                str = UNDEFINED;
            } else {
                DecimalFormat formatter = new DecimalFormat(DEC_FORMAT);
                str = formatter.format(f);
            } // end if
            // special case : Double
        } else if (obj instanceof Double) {
            Double d = (Double) obj;
            if (d.isNaN()) {
                str = UNDEFINED;
            } else {
                DecimalFormat formatter = new DecimalFormat(DEC_FORMAT);
                str = formatter.format(d);
            } // end if
        } else if (obj instanceof Date) {
            Date date = (Date) obj;
            str = DateFormat.getDateInstance().format(date);
        } // end if

        // if default case
        if (str == null) {
            str = obj.toString();
        }

        return str;
    } // end getDisplayString()

    //
    // UK equivalent
    //
    private static HashMap g_ukMapping = null;

    public static String getUKEquivalent(String str) {
        /*
         * if (g_ukMapping == null) { g_ukMapping = new HashMap(); g_ukMapping.put("color",
         * "olour"); }
         * 
         * int idx = str.indexOf("Color"); if (idx != -1) { str = str.substring(0, idx+1) + "olour"
         * + str.substring(idx+5); }
         */
        return str;
    } // end getUKEquivalent()

    //
    // UNIT TEST
    //
    public void test() throws TestException {
        if (Testable.ENABLE_TEST) {
            int pos = consecutiveCharacters("Tennessee", 'e'); // NOT
            // LOCALIZABLE,
            // unit test
            if (pos != 7) { // position of 'ee' at 7
                String msg = "expected 7"; // NOT LOCALIZABLE, unit test
                throw new TestException(msg);
            }

            pos = consecutiveCharacters("Tennessee", 'a', 'z'); // NOT
            // LOCALIZABLE,
            // unit test
            if (pos != 2) { // position of 'nn' at 2
                String msg = "expected 2"; // NOT LOCALIZABLE, unit test
                throw new TestException(msg);
            }

            String src = "replace <this> by &lt;that&gt;"; // NOT LOCALIZABLE,
            // unit test
            String expectedDest = "replace &lt;this&gt; by &amp;lt;that&amp;gt;"; // NOT
            // LOCALIZABLE,
            // unit
            // test
            String dest = replaceHTMLSpecialCharacters(src);
            if (!dest.equals(expectedDest)) {
                String msg = "strings are different"; // NOT LOCALIZABLE, unit
                // test
                throw new TestException(msg);
            }
        } // end if
    } // end test()

    public static Testable createInstanceForTesting() {
        Testable testable = new StringUtil();
        return testable;
    }

    // Unit test
    public static void main(String[] args) {
        Testable testable = StringUtil.createInstanceForTesting();
        try {
            testable.test();
            System.out.println("success"); // NOT LOCALIZABLE, unit test
        } catch (TestException ex) {
            System.out.println("failure"); // NOT LOCALIZABLE, unit test
        }
    }
} // end StringUtil

