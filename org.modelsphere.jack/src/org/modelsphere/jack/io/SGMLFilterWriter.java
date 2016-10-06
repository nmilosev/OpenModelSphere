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

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

// ************************************************************************** //
// inspired (a lot!) from the inner class HtmlFilterStringWriter, class JavaForward
// ************************************************************************** //
public final class SGMLFilterWriter extends FilterWriter {

    private final String LESS_THAN = "&lt;"; // NOT LOCALIZABLE, SGML metacharacter
    private final String GREATER_THAN = "&gt;"; // NOT LOCALIZABLE, SGML metacharacter
    private final String AMPERSAND = "&amp;"; // NOT LOCALIZABLE, SGML metacharacter

    private Writer out = null; // +

    public SGMLFilterWriter(Writer o) {
        super(o);
        out = o;// +
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) {
            write(cbuf[i]);
        }
    }

    public void write(int c) throws IOException {
        char ch = (char) c;

        // +
        switch (ch) {
        case '<':
            out.write(LESS_THAN);
            break;
        case '>':
            out.write(GREATER_THAN);
            break;
        case '&':
            out.write(AMPERSAND);
            break;
        default:
            out.write(c);
        } // end switch
        /*
         * switch(ch) { case '<': super.write(LESS_THAN); break; case '>':
         * super.write(GREATER_THAN); break; case '&': super.write(AMPERSAND); break; default:
         * super.write(c); } //end switch
         */
    }

    public void write(String str, int off, int len) throws IOException {
        for (int i = off; i < off + len; i++) {
            write(str.charAt(i));
        }
    }
}
