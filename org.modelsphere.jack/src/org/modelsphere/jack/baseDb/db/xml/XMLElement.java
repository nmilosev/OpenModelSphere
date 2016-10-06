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
import java.util.*;

import org.modelsphere.jack.baseDb.db.*;

abstract class XMLElement implements XMLConstants {
    String name;
    private ArrayList attributes = new ArrayList();
    private ArrayList elements = new ArrayList();

    XMLElement(String name) {
        this.name = name;
    }

    void write(XMLWriter writer) throws DbException, IOException {
        if (name == null)
            return;
        writer.write(ELEMENT_BEGIN_PREFIX + name, true);
        writeAttributes(writer);
        boolean elementWritten = writeElements(writer);
        if (elementWritten) {
            writer.writeln();
        }
    }

    void add(XMLAttribute attr) {
        if (attr == null)
            return;
        attributes.add(attr);
    }

    void add(XMLElement element) {
        if (element == null)
            return;
        elements.add(element);
    }

    boolean writeElements(XMLWriter writer) throws DbException, IOException {
        boolean elementWritten = false;
        Iterator iter = elements.iterator();
        if (iter.hasNext()) {
            writer.write(ELEMENT_BEGIN_SUFFIX, false);
            writer.increaseIndent();
        }

        while (iter.hasNext()) {
            if (!elementWritten)
                writer.writeln();

            XMLElement element = (XMLElement) iter.next();
            element.write(writer);
            elementWritten = true;
        } // end while

        if (elementWritten) {
            writer.decreaseIndent();
            writer.writeln(ELEMENT_END_PREFIX + name + ELEMENT_END_SUFFIX, true);
        } else {
            writer.writeln(ELEMENT_EMPTY_SUFFIX, false);
        }

        return elementWritten;
    } // end writeElements()

    void writeAttributes(XMLWriter writer) throws DbException, IOException {
        if (attributes.size() == 0)
            return;
        writer.increaseIndent();
        writer.writeln();
        Iterator iter = attributes.iterator();
        while (iter.hasNext()) {
            XMLAttribute attr = (XMLAttribute) iter.next();
            attr.write(writer);
        }
        writer.write("", true);
        writer.decreaseIndent();
    }
    // abstract void
}
