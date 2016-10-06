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
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * This class is used to generate HTML-formatted text.  It accepts input strings in ASCII
 * format, but filters HTML special characters (such as '&' and '<') and replaces them
 * with the appropriate HTML tags (such as &amp; and &lt;).
 *
 * There are two ways to use it.  You can first use it as a special StringWriter
 * object, by calling the parameterless constructor.  Each subsequent call to
 * write() methods will write data in an internal string buffer, but will filter
 * HTML special characters.  At the end, you retrieve the buffer's content with
 * a call to toSring() :
 *    <code>
 *    HtmlWriter writer = new HtmlWriter();
 *    writer.write("//symbols: יאן\n");
 *    writer.write("if (i < 0) {..");
 *    String htmlText = writer.toString();
 *    </code>
 *
 * You can also use it to wrap an other kind of writer, such as FileWriter.
 * This is particularly useful if you want to generate a HTML file without
 * having to manually replace all the HTML special characters:
 *    <code>
 *    HtmlWriter writer = new HtmlWriter(new FileWriter("index.html"));
 *    writer.write("//symbols: יאן\n");
 *    writer.write("if (i < 0) {..");
 *    writer.close();
 *    </code>
 *
 *  See unit test below for a running example.
 */
public class HtmlWriter extends Writer {
    private static final String NBSP = "&nbsp;"; // NOT LOCALIZABLE - HTML tag
    private static final String BR = "<br>\n"; // NOT LOCALIZABLE - HTML tag

    private static String SEPARATORS = ",.(;:?! \n"; // NOT LOCALIZABLE (Note:
    // SEPARATORS is updated
    // in setMapping())

    private static ArrayList g_replacementList;
    private static HashMap g_replacementMapping;

    private static void setMapping(char replacementChar, String replacementString) {
        int idx = (int) replacementChar;
        g_replacementList.add(idx, replacementString);
        g_replacementMapping.put(String.valueOf(replacementChar), replacementString);
        SEPARATORS = SEPARATORS + replacementChar;
    }

    private static boolean init = false;

    private void init() {
        if (!init) {
            // create 256 null elements
            g_replacementList = new ArrayList();
            for (int i = 0; i < 256; i++) {
                g_replacementList.add(null);
            }

            g_replacementMapping = new HashMap();
            // common replacements
            setMapping('&', "&amp;"); // NOT LOCALIZABLE - HTML tag
            setMapping('>', "&gt;"); // NOT LOCALIZABLE - HTML tag
            setMapping('<', "&lt;"); // NOT LOCALIZABLE - HTML tag
            setMapping('\"', "&quot;"); // NOT LOCALIZABLE - HTML tag
            setMapping('©', "&copy;"); // NOT LOCALIZABLE - HTML tag
            setMapping('®', "&reg;"); // NOT LOCALIZABLE - HTML tag

            // diacritical characters
            setMapping('ג', "&acirc;"); // NOT LOCALIZABLE - HTML tag
            setMapping('א', "&agrave;"); // NOT LOCALIZABLE - HTML tag
            setMapping('ח', "&ccedil;"); // NOT LOCALIZABLE - HTML tag

            setMapping('ך', "&ecirc;"); // NOT LOCALIZABLE - HTML tag
            setMapping('ט', "&egrave;"); // NOT LOCALIZABLE - HTML tag
            setMapping('י', "&eacute;"); // NOT LOCALIZABLE - HTML tag
            setMapping('כ', "&euml;"); // NOT LOCALIZABLE - HTML tag

            setMapping('מ', "&icirc;"); // NOT LOCALIZABLE - HTML tag
            setMapping('ן', "&iuml;"); // NOT LOCALIZABLE - HTML tag
            setMapping('פ', "&ocirc;"); // NOT LOCALIZABLE - HTML tag

            setMapping('û', "&ucirc;"); // NOT LOCALIZABLE - HTML tag
            setMapping('ש', "&ugrave;"); // NOT LOCALIZABLE - HTML tag

            init = true;
        } // end if
    } // end init()

    private Writer m_nestedWriter;
    private int m_lineLen = 80;
    private boolean m_lastTokenIsBlank = false;

    public HtmlWriter() {
        this(new StringWriter());
        init();
    } // end HtmlWriter()

    // Use the 'decorator' design pattern
    public HtmlWriter(Writer nestedWriter) {
        m_nestedWriter = nestedWriter;
        init();
    } // end HtmlWriter()

    // ///////////////////////
    // OVERRIDES Writer
    //
    public void write(int c) throws IOException {
        m_lastTokenIsBlank = false;

        switch (c) {
        case ' ':
            m_lastTokenIsBlank = true;
            m_nestedWriter.write(NBSP);
            break;
        case '\n':
            m_nestedWriter.write(BR);
            break;
        default:
            String replacement = (String) g_replacementList.get((int) c);
            if (replacement != null) {
                m_nestedWriter.write(replacement);
            } else {
                m_nestedWriter.write(c);
            } // end if

            break;
        } // end switch
    } // end write()

    public void write(char cbuf[], int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            m_nestedWriter.write(cbuf[off + i]);
        } // end for
    } // end write()

    /**
     * Write a string.
     * 
     * @param str
     *            String to be written
     * 
     * @exception IOException
     *                If an I/O error occurs
     */
    public void write(String str) throws IOException {
        int charinline = 0;
        StringBuffer line = new StringBuffer();

        StringTokenizer st = new StringTokenizer(str, SEPARATORS, true);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            String replacement = (String) g_replacementMapping.get(token);
            if (replacement != null) {
                line.append(replacement);
                charinline += 1;
                m_lastTokenIsBlank = false;
            } else {

                if (token.equals("\n")) { // NOT LOCALIZABLE
                    line.append(BR);
                    m_nestedWriter.write(line.toString());
                    line = new StringBuffer();
                    charinline = 0;
                    m_lastTokenIsBlank = false;
                } else if (charinline >= m_lineLen) {
                    line.append(BR);
                    line.append(getActualToken(token));
                    m_nestedWriter.write(line.toString());
                    line = new StringBuffer();
                    charinline = 0;
                    charinline += token.length();
                } else {
                    line.append(getActualToken(token));
                    charinline += token.length();
                } // end if
            } // end if
        } // end while

        m_nestedWriter.write(line.toString());
    } // end write()

    public String toString() {
        return m_nestedWriter.toString();
    }

    public void flush() throws IOException {
        m_nestedWriter.flush();
    }

    public void close() throws IOException {
        m_nestedWriter.close();
    }

    //
    // ///////////////////////////////

    //
    // private methods
    //
    private String getActualToken(String token) {
        String actualToken;

        if (token.equals(" ")) {
            actualToken = m_lastTokenIsBlank ? NBSP : " ";
            m_lastTokenIsBlank = true;
        } else {
            actualToken = token;
            m_lastTokenIsBlank = false;
        } // end if

        return actualToken;
    } // end getActualToken()

    //
    // UNIT TEST
    //
    public static void main(String[] args) {

        try {
            // test HTML buffer
            HtmlWriter htmlBuffer = new HtmlWriter();
            htmlBuffer.write("//symbols:  יאן\n"); // NOT LOCALIZABLE, unit test
            htmlBuffer.write("if (i < 0) {.."); // NOT LOCALIZABLE, unit test
            String htmlText = htmlBuffer.toString();
            System.out.println(htmlText);

            // test HTML file
            HtmlWriter htmlFileWriter = new HtmlWriter(new java.io.FileWriter("index.html")); // NOT LOCALIZABLE, unit test
            htmlFileWriter.write("<html><body>"); // NOT LOCALIZABLE, unit test
            htmlFileWriter.write("//symbols:  יאן\n"); // NOT LOCALIZABLE, unit
            // test
            htmlFileWriter.write("if (i < 0) {.."); // NOT LOCALIZABLE, unit
            // test
            htmlFileWriter.write("</body></html>"); // NOT LOCALIZABLE, unit
            // test
            htmlFileWriter.close();

        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    } // end main()
} // end HtmlWriter
