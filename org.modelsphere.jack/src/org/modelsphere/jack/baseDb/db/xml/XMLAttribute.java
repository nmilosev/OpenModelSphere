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

package org.modelsphere.jack.baseDb.db.xml;

import java.io.*;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.io.XmlWriter;

abstract class XMLAttribute implements XMLConstants {
    protected String name;

    XMLAttribute(String name) {
        this.name = name;
    }

    abstract String getValue() throws DbException;

    String getActualType() {
        return null;
    }

    public void write(XMLWriter writer) throws DbException, IOException {
        String value = getValue();
        if (value == null)
            return;

        // Write each element separately (more efficient than string
        // concatenation)
        writer.write(ATTR_NAME_PREFIX, true);
        writer.write(name);
        writer.write(ATTR_NAME_SUFFIX);
        writer.write(ATTR_NAME_VALUE_SEPARATOR); // write '='

        writer.write(ATTR_VALUE_PREFIX); // write '\"'
        String actualType = getActualType();
        if (actualType != null) {
            writer.write("(" + actualType + ")");
        }

        // Generates XML meta-characters if required (&quot;, &lt;..)
        // Note: If XMLWriter was a real subclass of java.io.Writer, we could
        // write
        // directly in this writer, resulting in a more efficient write() method
        // [MS]
        XmlWriter xw = new XmlWriter(false);
        xw.write(value);
        writer.write(xw.toString());

        writer.writeln(ATTR_VALUE_SUFFIX, false);
    } // end write()
} // end XMLAttribute()
