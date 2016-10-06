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
package org.modelsphere.jack.plugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.modelsphere.jack.io.PathFile;
import org.modelsphere.jack.plugins.io.PluginLoader;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.w3c.dom.*;

public final class PluginsConfiguration {
    private static final String FILENAME = "plugins.xml"; // NOT LOCALIZABLE, file name

    private PluginsConfiguration() {
    }

    private static PluginsConfiguration singleton = null;

    public static PluginsConfiguration getSingleton() {
        if (singleton == null) {
            singleton = new PluginsConfiguration();
        }

        return singleton;
    }

    //Return the plugins.xml file
    private File _pluginsXmlFile = null; 
    public File getFile() {
        if (_pluginsXmlFile == null) {
        	//User home (e.g. C:\Users\Admin\)
            String userHome = System.getProperty("user.home"); // NOT LOCALIZABLE, property
            File userFolder = new File(userHome); 
            
            //e.g. C:\Users\Admin\.Open ModelSphere
            String applName = "." + ApplicationContext.getApplicationName();
            File applFolder = new File(userFolder, applName); 
            
            String version = ApplicationContext.getApplicationVersion();
            version = version.replace('.', '_');
            File versionFolder = new File(applFolder, version); 
            
            //e.g. C:\Users\Admin\.Open ModelSphere\plugins.xml
            _pluginsXmlFile = new File(versionFolder, FILENAME);
        }
        
        return _pluginsXmlFile;
    }

    public boolean load(List<PluginDescriptor> pluginDescriptors) {
        File file = getFile();
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
                        && rootNode.getNodeName().equals("plugins")) {
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

                    NodeList pluginsNode = rootNode.getChildNodes();
                    int count = pluginsNode.getLength();
                    for (int j = 0; j < count; j++) {
                        Node pluginNode = pluginsNode.item(j);
                        String name = pluginNode.getNodeName();
                        if (rootNode.getNodeType() != Node.ELEMENT_NODE || name == null
                                || !name.equals("plugin")) {
                            continue;
                        }
                        PluginDescriptor pluginDescriptor = PluginLoader.load(pluginNode);
                        if (pluginDescriptor != null) {
                            pluginDescriptors.add(pluginDescriptor);
                        }
                    }
                    break;
                }
            }

            fileRead = true;
        } catch (Exception e) {
            fileRead = false;
            e.printStackTrace();
        }

        return fileRead;
    }

    private void save(File file, ArrayList<PluginDescriptor> pluginDescriptors) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            Document document = parser.newDocument();

            Element rootElement = document.createElement("plugins");
            rootElement.setAttribute("build", new Integer(ApplicationContext.APPLICATION_BUILD_ID)
                    .toString());
            document.appendChild(rootElement);

            for (PluginDescriptor pluginDescriptor : pluginDescriptors) {
                if (!PluginLoader.isValid(pluginDescriptor))
                    continue;
                Element pluginElement = document.createElement("plugin");
                try {
                    boolean saved = PluginLoader.save(pluginDescriptor, document, pluginElement);
                    if (saved) {
                        rootElement.appendChild(pluginElement);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
            e.printStackTrace();
        }
    }

    void save() {
        // Rename 'plugins.xml' to 'plugins.xml.bak'
        File pluginsXmlFile = getFile(); 
        File folder = pluginsXmlFile.getParentFile();
        if (! folder.exists()) {
        	folder.mkdirs();
        }
        
        File renamedFile = new File(folder, FILENAME + PathFile.BACKUP_EXTENSION);
        
        if (! pluginsXmlFile.exists()) {
            try {
                if (! pluginsXmlFile.createNewFile())
                    return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (renamedFile.exists()) {
            renamedFile.delete();
        }

        pluginsXmlFile.renameTo(renamedFile);

        ArrayList<PluginDescriptor> pluginDescriptors = new ArrayList<PluginDescriptor>();
        PluginsRegistry registry = PluginMgr.getSingleInstance().getPluginsRegistry();
        pluginDescriptors.addAll(registry.getValidPlugins());

        save(pluginsXmlFile, pluginDescriptors);
    }
}
