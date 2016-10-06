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

/**
 * Title:        All-purpose snippets
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Marco Savard
 * @version 1.0
 */

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * This class is used to generate XML-formatted text.  It accepts input strings in ASCII
 * format, but filters XML special characters (such as '&' and '<') and replaces them
 * with the appropriate XML tags (such as &amp; and &lt;).
 *
 * There are two ways to use it.  You can first use it as a special StringWriter
 * object, by calling the parameterless constructor.  Each subsequent call to
 * write() methods will write data in an internal string buffer, but will filter
 * XML special characters.  At the end, you retrieve the buffer's content with
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
 * having to manually replace all the XML special characters:
 *    <code>
 *    HtmlWriter writer = new HtmlWriter(new FileWriter("index.html"));
 *    writer.write("//symbols: יאן\n");
 *    writer.write("if (i < 0) {..");
 *    writer.close();
 *    </code>
 *
 *  Because several XML viewers (such as MS-Explorer) and several XML parsers
 *  do not support neither diacritical characters (יא..) nor HTML special tags
 *  (&eacute;, &agrave;, ..), those characters are translated as ASCII-coded
 *  tags (&#226, ..).
 *
 *  See unit test below for a running example.
 */
public class XmlWriter extends Writer {
    private static final String NBSP = " "; // NOT LOCALIZABLE - HTML tag
    private static final String BR = "\n"; // NOT LOCALIZABLE - HTML tag

    private static String SEPARATORS1 = ",.(;:?! \n"; // NOT LOCALIZABLE (Note:
    // SEPARATORS is updated
    // in setMapping())
    private static String SEPARATORS2 = ""; // NOT LOCALIZABLE (Note: SEPARATORS
    // is updated in setMapping())

    private static ArrayList g_replacementList;
    private static HashMap g_replacementMapping;

    private static void setMapping(char replacementChar, String replacementString) {
        int idx = (int) replacementChar;
        g_replacementList.add(idx, replacementString);
        g_replacementMapping.put(String.valueOf(replacementChar), replacementString);
        SEPARATORS1 = SEPARATORS1 + replacementChar;
        SEPARATORS2 = SEPARATORS2 + replacementChar;
    } // end setMapping()

    private static boolean init = false;

    private void init() {
        if (!init) {
            // create 256 null elements
            g_replacementList = new ArrayList();
            for (int i = 0; i < 256; i++) {
                g_replacementList.add(null);
            } // end for

            g_replacementMapping = new HashMap();
            // these 5 entities are pre-defined in XML
            setMapping('&', "&amp;"); // NOT LOCALIZABLE - HTML tag
            setMapping('>', "&gt;"); // NOT LOCALIZABLE - HTML tag
            setMapping('<', "&lt;"); // NOT LOCALIZABLE - HTML tag
            setMapping('\'', "&apos;"); // NOT LOCALIZABLE - HTML tag
            setMapping('\"', "&quot;"); // NOT LOCALIZABLE - HTML tag

            // diacritical characters, such as יאפ.. (from 192 (a grave) to 255
            // (y umlaut) )
            // &eacute; not always supported in XML
            for (int i = 192; i <= 255; i++) {
                setMapping((char) i, "&#" + i + ";");
            } // end for

            init = true;
        } // end if
    } // end init()

    private Writer m_nestedWriter;
    private boolean m_breakLines;
    private int m_lineLen = 80;
    private boolean m_lastTokenIsBlank = false;

    public XmlWriter() {
        this(new StringWriter(), true);
    } // end XmlWriter()

    public XmlWriter(boolean breakLines) {
        this(new StringWriter(), breakLines);
    } // end XmlWriter()

    // Use the 'decorator' design pattern
    public XmlWriter(Writer nestedWriter) {
        this(nestedWriter, true);
    } // end XmlWriter()

    // Use the 'decorator' design pattern
    public XmlWriter(Writer nestedWriter, boolean breakLines) {
        m_nestedWriter = nestedWriter;
        m_breakLines = breakLines;
        init();
    } // end XmlWriter()

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

        String separators = (m_breakLines) ? SEPARATORS1 : SEPARATORS2;

        StringTokenizer st = new StringTokenizer(str, separators, true);
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
                } else if ((charinline >= m_lineLen) && (m_breakLines)) {
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
