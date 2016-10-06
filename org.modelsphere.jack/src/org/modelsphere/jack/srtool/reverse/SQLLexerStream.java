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

import org.modelsphere.jack.io.LexerStream;

public final class SQLLexerStream extends LexerStream {

    public SQLLexerStream(Reader reader) {
        super(reader);

        // ignore lines starting with the following words
        defineLineComment("ACCEPT"); // NOT LOCALIZABLE, keyword
        defineLineComment("PROMPT"); // NOT LOCALIZABLE, keyword
        defineLineComment("REM"); // NOT LOCALIZABLE, keyword
        defineLineComment("SET"); // NOT LOCALIZABLE, keyword
        defineLineComment("SPOOL"); // NOT LOCALIZABLE, keyword

        // Needed for informix
        defineLineComment("--");

        // Each SELECT statement begins by SELECT keyword and ends by ';'
        defineStatement("SELECT", ';'); // NOT LOCALIZABLE, keyword
        defineStatement("CREATE", ';'); // NOT LOCALIZABLE, keyword
        defineStatement("DROP", ';'); // NOT LOCALIZABLE, keyword
        defineStatement("ALTER", ';'); // NOT LOCALIZABLE, keyword
        defineStatement("MODIFY", ';'); // NOT LOCALIZABLE, keyword
        defineStatement("INSERT", ';'); // NOT LOCALIZABLE, keyword
        defineStatement("DELETE", ';'); // NOT LOCALIZABLE, keyword
        defineStatement("UPDATE", ';'); // NOT LOCALIZABLE, keyword

        // In SQL, use single quote as quote character
        quoteChar('\'');

        // Accept underscores in identifiers
        wordChars('_', '_');
        wordChars('$', '$');
        wordChars('#', '#'); // Mandatory for accessing some column of v$xxx
        // oracle dynamic views

        // This char can be used on some dbms to refer to specific system user
        // objects
        // Can not be considered as part of a number.
        ordinaryChar('.');
        ordinaryChar(' ');
        ordinaryChars(0, 9);
        ordinaryChar('-');
        ordinaryChar('/');
        ordinaryChar('*');
        ordinaryChar('+');
    }
}
