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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelsphere.jack.plugins.PluginDescriptor;

class XMLPluginLoader extends PluginLoader {
    static final String XML_SIGNATURE_FILE = "plugin.xml";

    XMLPluginLoader() {
    }

    @Override
    protected List<PluginDescriptor> scan(File directory, File scanRoot) {
        if (!scanRoot.equals(getPluginsDirectory()))
            return null;
        ArrayList<PluginDescriptor> scannedPluginInfo = new ArrayList<PluginDescriptor>();
        File[] files = directory.listFiles();
        if (files == null)
            return scannedPluginInfo;

        // check if this is an xml plugin directory - skip if so (will be checked later)
        for (File file : files) {
            if (file == null || !file.isDirectory())
                continue;
            File[] subFiles = file.listFiles();
            if (subFiles == null)
                continue;
            for (File subFile : subFiles) {
                if (subFile.getName().toLowerCase().equals(XML_SIGNATURE_FILE)) {
                    DefaultPluginDescriptor defaultPluginDescriptor = (DefaultPluginDescriptor) scanXML(subFile);
                    if (defaultPluginDescriptor != null) {
                        scannedPluginInfo.add(defaultPluginDescriptor);
                    }
                    break;
                }
            }
        }
        return scannedPluginInfo;
    }

    @Override
    public List<PluginDescriptor> scan() {
        File pluginDirectory = getPluginsDirectory();

        if (pluginDirectory == null)
            return null;

        List<PluginDescriptor> pluginInfos = scan(pluginDirectory, pluginDirectory);
        return pluginInfos;
    }

    private PluginDescriptor scanXML(File xmlFile) {
        if (xmlFile == null || !xmlFile.isFile() || !xmlFile.canRead()) {
            return null;
        }
        PluginDescriptor pluginDescriptor = null;
        try {
            pluginDescriptor = XMLPluginUtilities.loadSignatureFile(xmlFile);
        } catch (Exception e) {
            pluginDescriptor = null;
        }
        return pluginDescriptor;
    }

    @Override
    public PluginContext createPluginContext(URL url) {
        XMLPluginContext context = new XMLPluginContext(this, url);
        return context;
    }

    @Override
    public String getID() {
        return "xml";
    }

    protected Map<String, Object> getExtendedAttributes(PluginDescriptor pluginDescriptor) {
        // do not save any other attributes in the configuration, always restore from the plugin.xml file
        return super.getExtendedAttributes(pluginDescriptor);
    }

    @Override
    protected void setExtendedAttributes(PluginDescriptor pluginDescriptor,
            Map<String, Object> attributes) {
        super.setExtendedAttributes(pluginDescriptor, attributes);
        // do nothing - xml plugins are always rescanned on startup, the scan descriptor will be used instead
        // (with all attributes set using the plugin.xml file)
    }

    @Override
    public boolean isConfigurationCommandSupported(String command) {
        if (command.equals(ConfigurationCommandFactory.ACTION_DELETE)
                || command.equals(ConfigurationCommandFactory.ACTION_INSTALL)) {
            return true;
        }
        return super.isConfigurationCommandSupported(command);
    }

}
