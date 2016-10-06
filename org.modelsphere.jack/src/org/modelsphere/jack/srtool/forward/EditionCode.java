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

import java.io.*;

public abstract class EditionCode extends Rule {
    protected static int g_margin = 0;

    EditionCode() {
        super(null);
    }

    abstract String process() throws RuleException;

    public static final int getMargin() {
        return g_margin;
    }

    static final void increment(int indentation) {
        g_margin += indentation;
    }

    static final void decrement(int indentation) {
        g_margin -= indentation;
    }

    public static final void reset(int value) {
        g_margin = value;
    }

    public static final String processEditionCode(String unprocessed) throws RuleException {
        StringWriter writer = new StringWriter();
        return processEditionCode(unprocessed, writer);
    }

    public static final String processEditionCode(String unprocessed, Writer writer)
            throws RuleException {
        boolean expectEditionCode = true;
        String processedString;

        try {
            TemplateEnumeration enumeration = new TemplateEnumeration(unprocessed);
            int nbSubStrings = 0;
            while (enumeration.hasMoreElements()) {
                StringStructure ss = (StringStructure) enumeration.nextElement();

                if (ss.str != "") {
                    writer.write(ss.str);
                }

                if (ss.editionCode != null) {
                    EditionCode code = ss.editionCode;
                    if (code instanceof EndOfLine) {
                        String s = EndOfLine.getIndent();
                        if (s == null) {
                            EndOfLine eol = (EndOfLine) code;
                            eol.checkError();
                            s = "";
                        }
                        writer.write(s);
                    } else {
                        String result = code.process();
                        if (result != null) {
                            writer.write(result);
                        }
                    }
                } // end if
            } // end while
            processedString = writer.toString();
        } catch (IOException ex) {
            processedString = ex.toString();
        }

        return processedString;
    }

    // Valid edition codes are : n, m, t, d, q
    static boolean isValidEditionCode(char firstChar) {
        boolean isValid;

        switch (firstChar) {
        case 'n':
        case 'm':
        case 't':
        case 'd':
        case 'q':
            isValid = true;
            break;
        default:
            isValid = false;
            break;
        } // end switch

        return isValid;
    }

    // Unit test
    // Expected output:
    // This; is
    // a test;
    // ; case to
    // ;; test
    // the $edition codes;

    /*
     * public static void main(String[] args) throws IOException, RuleException { String unprocessed
     * = //"$m+2;$n;A $m+2;$n;B $m+2;$n;C $n;D $n;E $m-2;$n;F $m-2;$n;G"; //NOT LOCALIZABLE, unit
     * test "This; is $m+2;$n; a test;$n;; case to$m+2;$n;;; test$m-2;$n; the $$edition codes;" ;
     * //NOT LOCALIZABLE, unit test String processed = processEditionCode(unprocessed);
     * org.modelsphere.jack.debug.Debug.trace(processed);
     * 
     * org.modelsphere.jack.debug.Debug.trace("Press ENTER to quit."); byte[] buffer = new
     * byte[256]; System.in.read(buffer, 0, 256); } //end main()
     */
} // end of EditionCode

