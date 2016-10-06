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

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * An indent writer is a print writer that outputs indentation whitespace after each newline.
 * Calling <code>indent</code> and <code>outdent</code> changes the indentation level. The default
 * indentation width is 4 spaces.
 * 
 * @author Marco Savard
 * 
 */
public class IndentWriter extends PrintWriter {

    protected static final int DEFAULT_INDENTATION_WIDTH = 4;

    protected int width;
    protected int level;
    protected boolean newline;

    /**
     * Constructor, same as the <code>PrintWriter</code> version.
     * 
     * @param out
     *            an output stream
     */
    public IndentWriter(OutputStream out) {
        super(out);
        init(DEFAULT_INDENTATION_WIDTH);
    }

    /**
     * Constructor, same as <code>PrintWriter</code> version without the <i>width</i> parameter.
     * 
     * @param out
     *            an output stream
     * @param autoFlush
     *            if <code>true</code>, the <code>println()</code> methods will flush the output
     *            bufferset to flush after every line
     * @param width
     *            indentation width in spaces
     */
    public IndentWriter(OutputStream out, boolean autoFlush, int width) {
        super(out, autoFlush);
        init(width);
    }

    /**
     * Constructor, same as the <code>PrintWriter</code> version.
     * 
     * @param out
     *            a writer
     */
    public IndentWriter(Writer out) {
        super(out);
        init(DEFAULT_INDENTATION_WIDTH);
    }

    /**
     * Constructor, same as the <code>PrintWriter</code> version.
     * 
     * @param out
     *            a writer
     * @param autoFlush
     *            if <code>true</code>, the <code>println()</code> methods will flush the output
     *            bufferset to flush after every line
     * @param width
     *            indentation width in spaces
     */
    public IndentWriter(Writer out, boolean autoFlush, int width) {
        super(out, autoFlush);
        init(width);
    }

    private boolean spacesOnly = false;

    public void setSpacesOnly(boolean spacesOnly) {
        this.spacesOnly = spacesOnly;
    }

    /**
     * Initializes some instance variables. Called from constructors.
     * 
     * @param indentationWidth
     *            number of spaces per indentation level
     */
    protected void init(int indentationWidth) {
        width = indentationWidth;
        level = 0;
        newline = true;
    }

    /**
     * Increases the indentation level by one.
     */
    public void indent() {
        ++level;
    }

    /**
     * Decreases the indentation level by one.
     */
    public void unindent() {
        if (--level < 0)
            level = 0;
    }

    public void print(boolean b) {
        doIndent();
        super.print(b);
    }

    public void print(char c) {
        doIndent();
        super.print(c);
        if (c == '\n')
            newline = true;
    }

    public void print(char[] s) {
        for (int i = 0; i < s.length; ++i)
            print(s[i]);
    }

    public void print(double d) {
        doIndent();
        super.print(d);
    }

    public void print(float f) {
        doIndent();
        super.print(f);
    }

    public void print(int i) {
        doIndent();
        super.print(i);
    }

    public void print(long l) {
        doIndent();
        super.print(l);
    }

    public void print(Object obj) {
        print(obj.toString());
    }

    /**
     * This method does not handle newlines embedded in the string.
     * 
     * @param str
     *            the string to output
     */
    public void print(String str) {
        doIndent();
        super.print(str);
    }

    public void println() {
        super.println();
        newline = true;
    }

    public void println(boolean b) {
        doIndent();
        super.println(b);
    }

    public void println(char c) {
        doIndent();
        super.println(c);
        if (c == '\n') {
            newline = true;
            doIndent();
            newline = true;
        }
    }

    public void println(char[] s) {
        for (int i = 0; i < s.length; ++i)
            print(s[i]);
        println();
    }

    public void println(double d) {
        doIndent();
        super.println(d);
        newline = true;
    }

    public void println(float f) {
        doIndent();
        super.println(f);
        newline = true;
    }

    public void println(int i) {
        doIndent();
        super.println(i);
        newline = true;
    }

    public void println(long l) {
        doIndent();
        super.println(l);
        newline = true;
    }

    public void println(Object obj) {
        println(obj.toString());
    }

    // FIX: this is not correct, but it is not worth fixing right now.
    // It does not handle newlines embedded in the string, but I do not care.
    public void println(String str) {
        doIndent();
        super.println(str);
        newline = true;
    }

    /**
     * Performs indentation by printing the correct number of tabs and spaces.
     */
    protected void doIndent() {
        if (newline) {
            int spaces = level * width;

            if (!spacesOnly) {
                while (spaces >= 8) {
                    super.print("\t");
                    spaces -= 8;
                }
            }

            super.print("        ".substring(0, spaces));
            newline = false;
        }
    }

    //
    // UNIT TEST
    //

    public static void main(String[] args) {
        java.io.StringWriter sw = new java.io.StringWriter();
        IndentWriter iw = new IndentWriter(sw, true, 2);
        iw.println("<aa>");
        iw.indent();
        iw.println("<bb>");
        iw.indent();
        iw.println("ccc");
        iw.unindent();
        iw.println("</bb>");
        iw.unindent();
        iw.println("</aa>");
        iw.close();

        String s = sw.toString();
        System.out.println(s);
    } // end main()

} // end IndentWriter
