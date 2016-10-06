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

package org.modelsphere.jack.preference.context;

import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.util.ExceptionHandler;
import org.w3c.dom.*;

/**
 * Utility class for ContextComponents.
 * 
 * @author gpelletier
 * 
 */
public class ContextUtils {
    private static final String PROJECT = "project";
    private static final String DBOBJECT_TAG = "dbobject";
    private static final String PATH_TAG = "path";
    private static final String PATH_ELEMENT_TAG = "element";
    private static final String PATH_TYPE = "type";
    private static final String PATH_VALUE = "value";

    /**
     * Append a new DOM Element to the provided parent for storing a reference to the specified
     * DbObject.
     * <p>
     * Stored DbObject can be restored by using the {@link #readDOMElement(Element)} method.
     * <p>
     * The provided parent element should represents the same parent as provided during the read
     * process.
     * <p>
     * Note that this method does not store the DbObject as with the serialization mecanisms. It
     * only store the required information needed to find the object.
     * 
     * @param document
     *            The document to use for creating new elements. Can't be null.
     * @param parent
     *            The parent element to which the dbobject node will be added. Can't be null.
     * @param dbo
     *            The DbObject to store. Can't be null.
     * @return true if the node has been added, false otherwise.
     * @see #readDOMElement(Element)
     */
    public static boolean writeDOMElement(Document document, Element parent, DbObject dbo) {
        if (document == null || dbo == null)
            return false;

        Db db = dbo.getDb();
        if (!(db instanceof DbRAM))
            return false;

        DbProject project = dbo.getProject();
        if (project == null)
            return false;

        String projectPath = project.getRamFileName();
        if (projectPath == null || projectPath.trim().length() == 0)
            return false;

        Element path = null;
        try {
            db.beginReadTrans();
            path = createDOMPathElement(document, dbo);
            db.commitTrans();
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(null, e);
        }
        
        Element dboElement = document.createElement(DBOBJECT_TAG);
        dboElement.setAttribute(PROJECT, projectPath);
        
        //prevents NullPointerException
        if (path != null) {
        	dboElement.appendChild(path);
        }

        parent.appendChild(dboElement);
        return true;
    }

    private static Element createDOMPathElement(Document document, DbObject dbo) throws DbException {
        Element path = document.createElement(PATH_TAG);
        DbObject composite = dbo;
        ArrayList<Element> elements = new ArrayList<Element>();
        while (composite != null && !(composite instanceof DbProject)) {
            String name = composite.getName();
            if (name == null || name.trim().length() == 0)
                return null;
            Element pathelement = document.createElement(PATH_ELEMENT_TAG);
            pathelement.setAttribute(PATH_TYPE, composite.getMetaClass().getJClass().getName());
            pathelement.setAttribute(PATH_VALUE, name);
            elements.add(pathelement);
            composite = composite.getComposite();
        }
        // insert in the correct order
        for (int i = elements.size() - 1; i >= 0; i--) {
            path.appendChild(elements.get(i));
        }
        return path;
    }

    /**
     * Find a DbObject among the opened projects matching the specifications contained in element.
     * <p>
     * This method restore a DbObject stored using the method
     * {@link #writeDOMElement(Document, Element, DbObject)}.
     * 
     * <p>
     * Important: This method uses name matching and objects' types. Since the application doesn't
     * enforce uniqueness for names, this method should not be used for critical operations. It is
     * not guaranteed, with the current implementation, that the restored object will be the same as
     * the stored object.
     * 
     * @param element
     * @return The DbObject found. If the element does not contain a DbObject node or if the
     *         specifications do not match with any of the opened project, the method return null.
     * 
     * @see #writeDOMElement(Document, Element, DbObject)
     */
    public static DbObject readDOMElement(Element element) {
        if (element == null)
            return null;

        // Find the DBOBJECT_TAG node
        NodeList nodes = element.getChildNodes();
        int count = nodes.getLength();
        Node dboNode = null;
        for (int i = 0; i < count; i++) {
            Node temp = nodes.item(i);
            if (temp.getNodeType() == Node.ELEMENT_NODE && temp.getNodeName().equals(DBOBJECT_TAG)) {
                dboNode = temp;
            }
        }
        if (dboNode == null) {
            return null;
        }

        // Find the project
        NamedNodeMap attributes = dboNode.getAttributes();
        Node projectNode = attributes == null ? null : attributes.getNamedItem(PROJECT);
        String path = projectNode == null ? null : projectNode.getNodeValue();

        DbProject project = findProject(path);
        if (project == null)
            return null;

        // Find the path node
        nodes = dboNode.getChildNodes();
        count = nodes.getLength();
        Node pathNode = null;
        for (int i = 0; i < count; i++) {
            Node temp = nodes.item(i);
            if (temp.getNodeType() == Node.ELEMENT_NODE && temp.getNodeName().equals(PATH_TAG)) {
                pathNode = temp;
            }
        }
        if (pathNode == null) {
            return null;
        }

        DbObject dbo = null;
        try {
            project.getDb().beginReadTrans();
            dbo = resolveDOMPath((Element) pathNode, project);
            project.getDb().commitTrans();
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(null, e);
        }

        return dbo;
    }

    private static DbObject resolveDOMPath(Element element, DbProject project) throws DbException {
        DbObject dbo = project;
        NodeList nodes = element.getChildNodes();
        int count = nodes.getLength();
        boolean empty = true;
        for (int i = 0; i < count; i++) {
            Node temp = nodes.item(i);
            if (temp.getNodeType() == Node.ELEMENT_NODE
                    && temp.getNodeName().equals(PATH_ELEMENT_TAG)) {
                NamedNodeMap attributes = temp.getAttributes();
                Node typeNode = attributes == null ? null : attributes.getNamedItem(PATH_TYPE);
                Node valueNode = attributes == null ? null : attributes.getNamedItem(PATH_VALUE);
                if (typeNode == null || valueNode == null) {
                    return null;
                }

                String dboName = valueNode.getNodeValue();
                String typeName = typeNode.getNodeValue();
                if (dboName == null || typeName == null) {
                    return null;
                }

                MetaClass metaclass = MetaClass.find(typeName);
                if (metaclass == null) {
                    continue;
                }

                empty = false;
                dbo = dbo.findComponentByName(metaclass, dboName, true);

                if (dbo == null)
                    break;
            }
        }
        if (empty)
            return null;
        return dbo;
    }

    private static DbProject findProject(String path) {
        if (path == null)
            return null;
        Db[] dbs = Db.getDbs();
        DbProject project = null;
        for (int i = 0; i < dbs.length; i++) {
            if (!(dbs[i] instanceof DbRAM))
                continue;
            DbRoot root = dbs[i].getRoot();
            if (root == null)
                continue;

            DbProject temp = null;
            try {
                dbs[i].beginReadTrans();
                if (root.getComponents().size() > 0) {
                    temp = (DbProject) root.getComponents().elementAt(0);
                }
                dbs[i].commitTrans();
            } catch (Exception e) {
                ExceptionHandler.processUncatchedException(null, e);
            }
            if (temp == null)
                continue;

            String filename = temp.getRamFileName();
            if (filename == null || !filename.equals(path))
                continue;
            project = temp;
            break;
        }
        return project;
    }

}
