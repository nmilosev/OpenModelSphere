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

package org.modelsphere.jack.plugins.io;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.modelsphere.jack.baseDb.international.LocaleMgr;
import org.modelsphere.jack.plugins.PluginConstants;
import org.modelsphere.jack.plugins.PluginDescriptor;
import org.modelsphere.jack.plugins.PluginDescriptor.PLUGIN_TYPE;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XMLPluginUtilities implements PluginConstants {
    public static final String LICENSE = "license"; // NOT LOCALIZABLE, keyword
    public static final String IMAGE = "image"; // NOT LOCALIZABLE, keyword

    public static PluginDescriptor loadSignatureFile(File xmlFile) throws IOException {
        if (xmlFile == null)
            return null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        Map<String, String> attributes = null;
        try {
            documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);
            NodeList nodes = document.getChildNodes();
            Node pluginNode = null;
            int count = nodes.getLength();
            for (int i = 0; i < count; i++) {
                Node childNode = nodes.item(i);
                if (childNode.getNodeType() == Node.ELEMENT_NODE
                        && childNode.getNodeName().equals("plugin")) {
                    pluginNode = childNode;
                    break;
                }
            }

            if (pluginNode != null) {
                attributes = loadAttributes(pluginNode);
            } else {
                return null;
            }
        } catch (ParserConfigurationException e) {
            return null;
        } catch (SAXException e) {
            return null;
        }

        PluginDescriptor pluginDescriptor = null;
        try {
            pluginDescriptor = createPluginDescriptor(xmlFile, attributes);
        } catch (Exception e) {
            // invalid signature
        }
        return pluginDescriptor;
    }

    private static PluginDescriptor createPluginDescriptor(File xmlFile,
            Map<String, String> attributes) throws InvalidSignatureException {
        String className = (String) attributes.get(ID);
        if (className == null) {
            return null;
        }

        String jar = (String) attributes.get(JAR);
        if (jar == null || jar.trim().length() == 0) {
            return null;
        }

        File homeDirectory = xmlFile.getParentFile();
        File jarFile = new File(homeDirectory, jar);
        URL jarURL = null;
        try {
            jarURL = jarFile.toURL();
        } catch (MalformedURLException e) {
            return null;
        }

        DefaultPluginDescriptor pluginDescriptor = new DefaultPluginDescriptor(className);

        XMLPluginLoader pluginLoader = (XMLPluginLoader) PluginLoader.getXMLInstance();
        XMLPluginContext context = (XMLPluginContext) pluginLoader.createPluginContext(jarURL);
        context.setHomeDirectory(homeDirectory);

        pluginDescriptor.setContext(context);

        String baseName = attributes.get(RESOURCEBUNDLE);
        if (baseName != null && baseName.trim().length() > 0) {
            try {
                ResourceBundle resourceBundle = ResourceBundle.getBundle(homeDirectory.getPath()
                        + "_" + baseName, LocaleMgr.getLocaleFromPreferences(Locale.getDefault(), true),
                        new XMLPluginResourceBundleControl(homeDirectory, baseName));
                context.setResourceBundle(resourceBundle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        pluginDescriptor.setName(context.getLocalizedString((String) attributes.get(NAME)));
        pluginDescriptor.setPluginType(PLUGIN_TYPE.toType((String) attributes.get(TYPE)));
        pluginDescriptor.setDescription(context.getLocalizedString((String) attributes
                .get(DESCRIPTION)));
        pluginDescriptor.setDate(context.getLocalizedString((String) attributes.get(DATE)));
        pluginDescriptor.setVersion(context.getLocalizedString((String) attributes.get(VERSION)));
        pluginDescriptor.setAuthor(context.getLocalizedString((String) attributes.get(AUTHOR)));
        pluginDescriptor.setAuthorEmail(context.getLocalizedString((String) attributes
                .get(AUTHOREMAIL)));
        pluginDescriptor.setAuthorURL(context
                .getLocalizedString((String) attributes.get(AUTHORURL)));

        String sbuild = (String) attributes.get(REQUIREDBUILD);
        if (sbuild != null) {
            try {
                int build = Integer.parseInt(sbuild);
                context.setBuildRequired(build);
            } catch (Exception e) {
            }
        }

        String licenseResource = context.getLocalizedString((String) attributes.get(LICENSE));
        if (licenseResource != null && licenseResource.trim().length() > 0) {
            URL licenseURL = null;
            try {
                File licenseFile = new File(homeDirectory, licenseResource);
                licenseURL = licenseFile.toURL();
                pluginDescriptor.setLicenseURL(licenseURL);
            } catch (MalformedURLException e) {
            }
        }

        String imageResource = context.getLocalizedString((String) attributes.get(IMAGE));
        if (imageResource != null && imageResource.trim().length() > 0) {
            URL imageURL = null;
            try {
                File imageFile = new File(homeDirectory, imageResource);
                if (imageFile.exists()) {
                    imageURL = imageFile.toURL();
                    Image image = ImageUtilities.loadImage(imageURL);
                    pluginDescriptor.setImage(image);
                }
            } catch (MalformedURLException e) {
            }
        }
        return pluginDescriptor;
    }

    private static Map<String, String> loadAttributes(Node pluginNode) {
        if (pluginNode == null)
            return null;

        Map<String, String> extendedAttributes = new HashMap<String, String>();
        NamedNodeMap attributes = pluginNode.getAttributes();
        if (attributes != null) {
            // set mandatory attributes
            Node classNameNode = attributes.getNamedItem(ID);
            if (classNameNode != null) {
                extendedAttributes.put(ID, classNameNode.getNodeValue());
            }
        }

        NodeList children = pluginNode.getChildNodes();
        int count = children.getLength();
        for (int i = 0; i < count; i++) {
            Node childNode = children.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE)
                continue;
            String name = childNode.getNodeName();
            if (name == null)
                continue;
            String value = childNode.getTextContent();

            if (value == null || value.length() == 0)
                continue;

            extendedAttributes.put(name, value);
        }

        return extendedAttributes;
    }

    public static File getHomeDirectory(PluginDescriptor pluginDescriptor) {
        String className = pluginDescriptor.getClassName();
        String homeName = className.replace(".", "_");
        File home = new File(PluginLoader.getPluginsDirectory(), homeName);
        return home;
    }

}
