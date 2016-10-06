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

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.io.PathFile;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DbApplication;
import org.modelsphere.jack.util.ExceptionHandler;
import org.w3c.dom.*;

/**
 * IO class for reading / writing the context's file.
 * 
 * @author gpelletier
 * 
 */
public class ContextIO {
    private static final String CONTEXT_TAG = "context";
    private static final String PROJECT_TAG = "project";
    private static final String COMPONENT_TAG = "component";
    private static final String COMPONENT_CONFIGURATION_TAG = "configuration";
    private static final String COMPONENT_ID = "id";
    private static final String PROJECT_PATH = "path";

    private static final String WORKSPACE_FILE = "workspace.xml";

    ContextIO() {
    }

    //called by SafeModeDialog
    public static File getWorkspaceFile() {
        File file = new File(ApplicationContext.getUserHomeDirectory(), WORKSPACE_FILE);
        return file;
    }

    boolean load(Map<String, ContextComponent> components) {
        File file = getWorkspaceFile();
        if (!file.exists()) {
            return false;
        }

        boolean fileRead = false;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            NodeList nodes = document.getChildNodes();
            int rootcount = nodes.getLength();
            for (int i = 0; i < rootcount; i++) {
                Node rootNode = nodes.item(i);
                if (rootNode.getNodeType() == Node.ELEMENT_NODE
                        && rootNode.getNodeName().equals(CONTEXT_TAG)) {
                    NamedNodeMap attributes = rootNode.getAttributes();
                    Node buildNode = attributes == null ? null : attributes.getNamedItem("build");
                    String sBuild = buildNode == null ? null : buildNode.getNodeValue();
                    int build = -1;
                    if (sBuild != null) {
                        try {
                            build = Integer.parseInt(sBuild);
                        } catch (Exception e) {
                        }
                    }
                    if (build == -1) {
                        return false;
                    } else if (build < ApplicationContext.APPLICATION_BUILD_ID) {
                        // TODO - Update file format if needed. - from build 905
                    } else if (build > ApplicationContext.APPLICATION_BUILD_ID) {
                        return false; // configuration file has been created with a newer version
                    } // else this is the same build, do nothing.

                    loadProjects(rootNode);
                    if (components != null) {
                        loadComponents(rootNode, components);
                    }
                    break;
                }
            }

            fileRead = true;
        } catch (Exception e) {
            fileRead = false;
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        }

        return fileRead;
    }

    private void loadProjects(Node rootNode) {
        NodeList projectNodes = rootNode.getChildNodes();
        int count = projectNodes.getLength();
        for (int i = 0; i < count; i++) {
            Node projectNode = projectNodes.item(i);
            String name = projectNode.getNodeName();
            if (rootNode.getNodeType() != Node.ELEMENT_NODE || name == null
                    || !name.equals(PROJECT_TAG)) {
                continue;
            }
            NamedNodeMap attributes = projectNode.getAttributes();
            Node pathNode = attributes == null ? null : attributes.getNamedItem(PROJECT_PATH);
            String path = pathNode == null ? null : pathNode.getNodeValue();
            if (path != null && path.trim().length() > 0) {
                try {
                    File file = new File(path);
                    if (file.exists() && file.canRead())
                        ApplicationContext.getDefaultMainFrame().doOpenFromFile(path);
                } catch (Exception e) {
                    Debug.trace(e);
                }
            }
        }

    }

    private void loadComponents(Node rootNode, Map<String, ContextComponent> components)
            throws Exception {
        Db[] dbs = Db.getDbs();
        for (int i = 0; i < dbs.length; i++) {
            if (dbs[i] instanceof DbRAM) {
                dbs[i].beginReadTrans();
            }
        }

        loadComponents_(rootNode, components);

        for (int i = 0; i < dbs.length; i++) {
            if (dbs[i] instanceof DbRAM) {
                dbs[i].commitTrans();
            }
        }
    }

    private void loadComponents_(Node rootNode, Map<String, ContextComponent> components)
            throws DbException {
        NodeList componentNodes = rootNode.getChildNodes();
        int count = componentNodes.getLength();
        for (int i = 0; i < count; i++) {
            Node componentNode = componentNodes.item(i);
            String name = componentNode.getNodeName();
            if (rootNode.getNodeType() != Node.ELEMENT_NODE || name == null
                    || !name.equals(COMPONENT_TAG)) {
                continue;
            }
            NamedNodeMap attributes = componentNode.getAttributes();
            Node idNode = attributes == null ? null : attributes.getNamedItem(COMPONENT_ID);
            String id = idNode == null ? null : idNode.getNodeValue();
            if (id != null && id.trim().length() > 0) {
                ContextComponent component = components.get(id);
                if (component == null)
                    continue;

                NodeList componentSubNodes = componentNode.getChildNodes();
                int subNodesCount = componentSubNodes.getLength();
                Node configurationNode = null;
                for (int j = 0; j < subNodesCount; j++) {
                    configurationNode = componentSubNodes.item(j);
                    name = configurationNode.getNodeName();
                    if (rootNode.getNodeType() != Node.ELEMENT_NODE || name == null
                            || !name.equals(COMPONENT_CONFIGURATION_TAG)) {
                        configurationNode = null;
                        continue;
                    }
                    break;
                }

                if (configurationNode == null)
                    continue;
                component.loadContext((Element) configurationNode);
            }
        }
    }

    private void save(File file, Map<String, ContextComponent> components) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            Document document = parser.newDocument();

            Element rootElement = document.createElement(CONTEXT_TAG);
            rootElement.setAttribute("build", new Integer(ApplicationContext.APPLICATION_BUILD_ID)
                    .toString());
            document.appendChild(rootElement);

            saveProjects(document, rootElement);

            if (components != null) {
                saveComponents(components, document, rootElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        }
    }

    private void saveProjects(Document document, Element rootElement) {
        Db[] dbs = Db.getDbs();

        for (int i = 0; i < dbs.length; i++) {
            if (!(dbs[i] instanceof DbRAM))
                continue;
            try {
                DbProject project = DbApplication.getFirstProjectFor(dbs[i]);
                String filename = project.getRamFileName();
                if (filename == null || filename.trim().length() == 0)
                    continue;

                Element projectElement = document.createElement(PROJECT_TAG);
                projectElement.setAttribute(PROJECT_PATH, filename);

                rootElement.appendChild(projectElement);
            } catch (Exception ex) {
                Debug.trace(ex);
            }
        }
    }

    private void saveComponents(Map<String, ContextComponent> components, Document document,
            Element rootElement) throws Exception {
        Db[] dbs = Db.getDbs();
        for (int i = 0; i < dbs.length; i++) {
            if (dbs[i] instanceof DbRAM) {
                dbs[i].beginReadTrans();
            }
        }

        saveComponents_(components, document, rootElement);

        for (int i = 0; i < dbs.length; i++) {
            if (dbs[i] instanceof DbRAM) {
                dbs[i].commitTrans();
            }
        }
    }

    private void saveComponents_(Map<String, ContextComponent> components, Document document,
            Element rootElement) throws DbException {
        for (String id : components.keySet()) {
            ContextComponent component = components.get(id);
            if (component == null)
                continue;

            Element componentElement = document.createElement(COMPONENT_TAG);
            componentElement.setAttribute(COMPONENT_ID, id);
            Element componentConfigurationElement = document
                    .createElement(COMPONENT_CONFIGURATION_TAG);
            componentElement.setAttribute(COMPONENT_ID, id);
            boolean saved = component.saveContext(document, componentConfigurationElement);
            if (saved) {
                rootElement.appendChild(componentElement);
                componentElement.appendChild(componentConfigurationElement);
            }
        }
    }

    void save(Map<String, ContextComponent> components) {
        // Rename 'FILENAME' to 'FILENAME~'
        File file = getWorkspaceFile();
        File renamedFile = new File(file.getParent(), WORKSPACE_FILE + PathFile.BACKUP_EXTENSION);// NOT LOCALIZABLE
        if (!file.exists()) {
            try {
                if (!file.createNewFile())
                    return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (renamedFile.exists()) {
            renamedFile.delete();
        }

        file.renameTo(renamedFile);

        save(file, components);
    }
}
