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
import java.net.URL;
import java.util.*;

import org.modelsphere.jack.plugins.Plugin;
import org.modelsphere.jack.plugins.PluginDescriptor;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.Splash;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class BuiltInPluginLoader extends PluginLoader {
    private Class<? extends Plugin>[] builtInPlugins;

    BuiltInPluginLoader() {
    }

    @Override
    public List<PluginDescriptor> scan() {
        List<PluginDescriptor> pluginDescriptors = new ArrayList<PluginDescriptor>();
        for (int i = 0; i < builtInPlugins.length; i++) {
            Class<? extends Plugin> claz = builtInPlugins[i];
            DefaultPluginDescriptor defaultPluginDescriptor = new DefaultPluginDescriptor(claz
                    .getName());
            PluginContext context = createPluginContext(null);
            context.setClassLoader(defaultClassLoader);
            defaultPluginDescriptor.setContext(context);
            pluginDescriptors.add(defaultPluginDescriptor);
        }
        return pluginDescriptors;
    }

    @Override
    public boolean initPluginInstance(PluginDescriptor pluginDescriptor, Splash splashScreen) {
        boolean result = super.initPluginInstance(pluginDescriptor, splashScreen);
        if (result) {
            ClassPluginContext context = (ClassPluginContext) pluginDescriptor.getContext();
            DefaultPluginDescriptor descriptor = (DefaultPluginDescriptor) pluginDescriptor;
            // init descriptor properties using the instance signature
            Plugin plugin = context.getInstance();
            PluginSignature signature = plugin == null ? null : plugin.getSignature();
            context.setSignature(signature);
            if (signature != null) {
                String resource = signature.getImage();
                if (resource != null) {
                    URL imageResourceURL = pluginDescriptor.getPluginClass().getResource(resource);
                    Image image = ImageUtilities.loadImage(imageResourceURL);
                    ((DefaultPluginDescriptor) pluginDescriptor).setImage(image);
                    context.put(IMAGEURL, imageResourceURL);
                }
                descriptor.setVersion(signature.getVersion());
                descriptor.setDescription(signature.getDescription());
                descriptor.setAuthor(signature.getAuthor());
                descriptor.setAuthorEmail(signature.getAuthorEmail());
                descriptor.setAuthorURL(signature.getAuthorURI());
                descriptor.setName(signature.getName());
                String date = signature.getStringDate();
                if (date != null) {
                    String temp = "";
                    StringTokenizer st = new StringTokenizer(date, "$", false); // NOT LOCALIZABLE - Source Safe Tag
                    while (st.hasMoreTokens()) {
                        temp += st.nextToken();
                    }
                    descriptor.setDate(temp);
                }
                context.setBuildRequired(signature.getBuildRequired());
            }
        }
        return result;
    }

    public PluginContext createPluginContext(URL url) {
        return new ClassPluginContext(this, url);
    }

    /**
     * Set once by the plugin manager. Any subsequent calls will generate an exception.
     * 
     * @param builtInPlugins
     */
    public void setBuiltInPlugins(Class<? extends Plugin>[] builtInPlugins) {
        if (this.builtInPlugins != null) {
            throw new RuntimeException("Changing built in plugins is not allowed.");
        }
        if (builtInPlugins == null) {
            this.builtInPlugins = new Class[] {};
        } else {
            this.builtInPlugins = builtInPlugins;
        }
    }

    @Override
    public String getID() {
        return "builtin";
    }

    @Override
    protected PluginDescriptor loadImpl(Node pluginNode, Map<String, Object> extendedAttributes) {
        return null;
    }

    @Override
    protected boolean saveImpl(PluginDescriptor pluginDescriptor, Document document, Element element) {
        //Do not save built-in plug-in in plugins.xml
        return false;
    }

}
