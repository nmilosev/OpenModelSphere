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

//To Convert text files using different line-feed conventions
//(for instance, context a UNIX text file (using '\n' as line
//separator) into a Windows text file (using '\r\n' instead)

//Version 1: only UNIX to Windows convertion is supported
public final class LineFeedFilterWriter extends FilterWriter {

    public LineFeedFilterWriter(Writer writer) throws IOException {
        super(writer);
    }

    // Very simple convertion algorith. Can surely be improved in
    // terms of optimization
    public void write(int c) throws IOException {
        if (c == '\n') { // NOT LOCALIZABLE
            super.write('\r'); // NOT LOCALIZABLE
            super.write('\n'); // NOT LOCALIZABLE
        } else {
            super.write(c);
        }
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            char c = cbuf[off + i];
            write(c);
        } // end for
    } // end write

    public void write(String str, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            char c = str.charAt(off + i);
            write(c);
        } // end for
    } // end write
}
