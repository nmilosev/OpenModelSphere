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
import org.modelsphere.jack.baseDb.meta.*;

class TextElement extends XMLElement {

    private String text;

    TextElement(MetaField field, String text) {
        super(XMLUtilities.createAttributeName(field));
        this.text = text;
    }

    // TODO: check for "]]>"
    boolean writeElements(XMLWriter writer) throws DbException, IOException {
        boolean elementWritten;
        if ((text == null) || (text.length() == 0)) {
            writer.writeln(ELEMENT_EMPTY_SUFFIX, false);
            elementWritten = false;
        } else {
            writer.write(ELEMENT_BEGIN_SUFFIX, false);
            writer.write(XMLConstants.CHARACTER_DATA_PREFIX + text
                    + XMLConstants.CHARACTER_DATA_SUFFIX, false);
            writer.writeln(ELEMENT_END_PREFIX + name + ELEMENT_END_SUFFIX, true);
            elementWritten = true;
        }

        return elementWritten;
    } // end writeElements()
} // end TextElement

