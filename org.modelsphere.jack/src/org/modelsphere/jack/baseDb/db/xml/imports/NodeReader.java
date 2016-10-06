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
 * Title:        Sms
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Grandite
 * @author
 * @version 1.0
 */

/**
 * This class specifies the procedure to read a XML-formatted output file
 * Package visibility: only used by XmlFileOpener
 *
 * When a XML node is encountered, searches for its id attribute. If such an
 * attribute exists, it's a concept node; if not, it's a value node.
 * Fills the element map while the XML file is parsed.
 *
 */

import java.util.ArrayList;
import java.util.HashMap;

import org.modelsphere.jack.baseDb.db.xml.XMLConstants;
import org.w3c.dom.*;

class NodeReader {
    private HashMap m_elementMap;
    private ArrayList m_keyList;
    private HashMap m_idMap;
    private HeaderStructure m_struct;

    NodeReader(HashMap elementMap, ArrayList keyList, HashMap idMap, HeaderStructure struct) {
        m_elementMap = elementMap;
        m_keyList = keyList;
        m_idMap = idMap;
        m_struct = struct;
    } // end NodeReader()

    void readNode(Node node, String parentId) {
        // read name
        short type = node.getNodeType();

        switch (type) {
        case Node.DOCUMENT_NODE:
            Document doc = (Document) node;
            Element elem = doc.getDocumentElement();
            readNode(elem, null);
            break;
        case Node.ELEMENT_NODE:
            String nodeName = node.getNodeName();
            if (nodeName.equals(XMLConstants.TAG_PROJECT)) {
                NodeList list = node.getChildNodes();
                int nb = list.getLength();
                for (int i = 0; i < nb; i++) {
                    Node item = list.item(i);
                    readNode(item, null);
                } // end for
            } else if (nodeName.equals(XMLConstants.TAG_HEADER)) {
                NamedNodeMap attrs = node.getAttributes();
                Node converterItem = attrs.getNamedItem(XMLConstants.TAG_CONVERTER);
                Node versionItem = attrs.getNamedItem(XMLConstants.TAG_VERSION);
                m_struct.converter = converterItem.getNodeValue();
                m_struct.version = versionItem.getNodeValue();
            } else if (nodeName.equals(XMLConstants.TAG_ID_POOL)) {
                NodeList list = node.getChildNodes();
                int nb = list.getLength();
                for (int i = 0; i < nb; i++) {
                    Node item = list.item(i);
                    readNode(item, null);
                } // end for
            } else if (nodeName.equals(XMLConstants.TAG_ID)) {
                // read attributes
                NamedNodeMap attrs = node.getAttributes();
                Node shortItem = attrs.getNamedItem(XMLConstants.ID_SHORT);
                String shortVal = shortItem.getNodeValue();
                Node longItem = attrs.getNamedItem(XMLConstants.ID_LONG);
                String longVal = longItem.getNodeValue();
                m_idMap.put(shortVal, longVal);
            } else if (nodeName.equals(XMLConstants.TAG_COMPONENT)) {
                String id = readNodeContent(node, parentId);
                NodeList list = node.getChildNodes();
                int nb = list.getLength();
                for (int i = 0; i < nb; i++) {
                    Node item = list.item(i);
                    readNode(item, id);
                } // end for
            } // end if

            break;
        } // end switch
    } // end read()

    private static ArrayList g_propertyList = new ArrayList();

    private String readNodeContent(Node node, String parentId) {
        g_propertyList.clear();

        // read attributes
        NamedNodeMap attrs = node.getAttributes();
        Node idItem = attrs.getNamedItem(XMLConstants.TAG_ID);
        String id = idItem.getNodeValue();
        String concept = getConceptFromId(id);

        int nb = attrs.getLength();
        for (int i = 0; i < nb; i++) {
            Node item = attrs.item(i);
            String name = item.getNodeName();
            String value = item.getNodeValue();
            value = correctString(value);
            g_propertyList.add(new ElementStructure.Property(name, value));
        } // end for

        // read non-concept sub elements
        NodeList list = node.getChildNodes();
        nb = list.getLength();
        for (int i = 0; i < nb; i++) {
            Node item = list.item(i);
            short type = item.getNodeType();
            if (type == Node.ELEMENT_NODE) {
                String nodeName = item.getNodeName();
                // boolean isConcept = isConcept(item);
                // if (! isConcept) {
                if (!nodeName.equals(XMLConstants.TAG_COMPONENT)) {
                    String name = item.getNodeName();
                    String value = getCData(item);
                    g_propertyList.add(new ElementStructure.Property(name, value));
                } // end if
            } // end if
        } // end for

        nb = g_propertyList.size();
        ElementStructure.Property[] props = new ElementStructure.Property[nb];
        for (int i = 0; i < nb; i++) {
            props[i] = (ElementStructure.Property) g_propertyList.get(i);

        } // end for
        ElementStructure struct = new ElementStructure(parentId, concept, props);
        m_elementMap.put(id, struct);
        m_keyList.add(id);
        return id;
    } // end readNodeContent()

    // Remove blanks right after an equal sign
    private String correctString(String value) {
        StringBuffer buffer = new StringBuffer();
        char lastChar = '\0';
        char currChar;
        for (int i = 0; i < value.length(); i++) {
            currChar = value.charAt(i);
            if (!(lastChar == '=') || !(currChar == ' ')) {
                buffer.append(currChar);
            }

            lastChar = currChar;
        } // end for

        return buffer.toString();
    } // end correctString()

    private String getConceptFromId(String id) {
        String concept = null;
        int idx = id.indexOf(':');
        if (idx != -1) {
            concept = id.substring(0, idx);
        }

        return concept;
    }

    private String getCData(Node node) {
        String cData = null;

        // read non-concept sub elements
        NodeList list = node.getChildNodes();
        int nb = list.getLength();
        for (int i = 0; i < nb; i++) {
            Node item = list.item(i);
            short type = item.getNodeType();
            if (type == Node.CDATA_SECTION_NODE) {
                cData = item.getNodeValue();
                break;
            } // end if
        } // end for

        return cData;
    } // end getCData

    //
    // INNER CLASS
    //
    static class HeaderStructure {
        String converter = null;
        String version = null;
    } // end HeaderStructure()

} // end NodeReader
