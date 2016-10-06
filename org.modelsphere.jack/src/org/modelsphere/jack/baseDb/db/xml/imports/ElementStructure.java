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

package org.modelsphere.jack.baseDb.db.xml.imports;

/**
 * This class is a structure to keep data about an item in a XML file Package visibility: only used
 * by XmlFileOpener
 * 
 * Each structure is kept in a hash table, identified by a unique id. The hash table is filled
 * during the parsing of the XML file; once the file is completely read, then the DbObject's are
 * created according the data contained in this structure.
 * 
 * compositeId : Id of the composite. The composite is always read before and created before all its
 * components. For instance, table IDs occur before column IDs in the XML file, and or created
 * before. elementType: element type such as org.modelsphere.sms.oracle.DbORATable props : array of
 * properties; each property is a name/value pair
 */
class ElementStructure {
    String m_compositeId;
    String m_elementType;
    Property[] m_props;

    ElementStructure(String compositeId, String elementType, Property[] props) {
        m_compositeId = compositeId;
        m_elementType = elementType;
        m_props = props;
    } // end ElementStructure()

    Property getProperty(String propName) {
        Property prop = null;

        for (int i = 0; i < m_props.length; i++) {
            Property currProp = m_props[i];
            if (currProp.m_name.equals(propName)) {
                prop = currProp;
                break;
            }
        } // end for

        return prop;
    } // end getProperty

    static class Property {
        String m_name;
        String m_value;

        Property(String name, String value) {
            m_name = name;
            m_value = value;
        }
    }

    // OVERRIDES java.lang.Object
    public String toString() {
        return "( parent=" + m_compositeId + "; nbProps=" + m_props.length + " )";
    }
} // end ElementStructure
