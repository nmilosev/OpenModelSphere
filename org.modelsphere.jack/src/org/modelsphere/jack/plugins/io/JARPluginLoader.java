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
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.modelsphere.jack.plugins.Plugin;
import org.modelsphere.jack.plugins.PluginDescriptor;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.Splash;

class JARPluginLoader extends PluginLoader {
    JARPluginLoader() {
    }

    @Override
    protected List<PluginDescriptor> scan(File directory, File scanRoot) {
        ArrayList<PluginDescriptor> scannedPluginInfo = new ArrayList<PluginDescriptor>();
        File[] files = directory.listFiles();
        if (files == null) // not a directory
            return scannedPluginInfo;

        // check if this is an xml plugin directory - skip if so (will be checked later)
        if (!scanRoot.equals(directory) && directory.getParentFile().equals(scanRoot)) {
            for (File file : files) {
                if (file == null || file.isDirectory())
                    continue;
                if (file.getName().toLowerCase().equals("plugin.xml")) {
                    return scannedPluginInfo;
                }
            }
        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile()) {
                PluginDescriptor pluginDescriptor = scanFile(file);
                if (pluginDescriptor != null) {
                    scannedPluginInfo.add(pluginDescriptor);
                }
            } else {
                List<PluginDescriptor> subList = scan(file, scanRoot);
                if (subList != null)
                    scannedPluginInfo.addAll(subList);
            }
        }

        return scannedPluginInfo;
    }

    private PluginDescriptor scanFile(File file) {
        if (file == null || !file.isFile()) {
            return null;
        }
        DefaultPluginDescriptor pluginDescriptor = null;
        try {
            // Check for jar file
            if (file.getName().toLowerCase().endsWith(".jar")) {
                // file extension
                JarFile jar = new JarFile(file);
                if (jar == null)
                    return null;
                URL jarurl = file.toURL();
                if (jarurl == null)
                    return null;

                JARPluginContext context = (JARPluginContext) createPluginContext(jarurl);
                PluginManifest manifest = context.getPluginManifest();

                String entryPoint = manifest == null ? null : manifest.getPluginClassName();
                if (entryPoint != null) {
                    try {
                        ClassLoader pluginloader = new URLClassLoader(new URL[] { jarurl },
                                defaultClassLoader);
                        if (entryPoint.indexOf('$') < 0) {
                            pluginDescriptor = new DefaultPluginDescriptor(entryPoint);
                            pluginDescriptor.setContext(context);
                            if (entryPoint.toLowerCase().endsWith(CLASS_EXTENSION)) {
                                entryPoint = entryPoint.substring(0, entryPoint.toLowerCase()
                                        .lastIndexOf(CLASS_EXTENSION));
                                entryPoint = entryPoint.replace('/', '.');
                                entryPoint = entryPoint.replace('\\', '.');
                            }
                            context.setClassLoader(pluginloader);
                            context.setJAR(jarurl);
                            pluginDescriptor
                                    .setPluginType(DefaultPluginDescriptor.PLUGIN_TYPE.PLUGIN);
                            initFromManifest(pluginDescriptor);
                        }
                    } catch (Throwable th) {
                        invalidate(pluginDescriptor, th, null, file);
                    }
                }
            }
        } catch (IOException ioe) {
            pluginDescriptor = null;
        } // Invalid url access, skip
        return pluginDescriptor;
    }

    protected void initFromManifest(PluginDescriptor pluginDescriptor) {
        JARPluginContext context = (JARPluginContext) pluginDescriptor.getContext();
        PluginManifest manifest = context.getPluginManifest();
        String requiredBuild = manifest.getPluginRequiredVersion();
        DefaultPluginDescriptor descriptor = (DefaultPluginDescriptor) pluginDescriptor;
        if (requiredBuild != null) {
            try {
                int build = Integer.parseInt(requiredBuild);
                context.setBuildRequired(build);
            } catch (Exception e) {
            }
        }
        descriptor.setName(manifest.getPluginTitle());
        descriptor.setAuthor(manifest.getPluginVendor());
        descriptor.setAuthorURL(manifest.getPluginVendorURL());
    }

    @Override
    public boolean initPluginInstance(PluginDescriptor pluginDescriptor, Splash splashScreen) {
        boolean result = super.initPluginInstance(pluginDescriptor, splashScreen);
        if (result) {
            JARPluginContext context = (JARPluginContext) pluginDescriptor.getContext();
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

    @Override
    public PluginContext createPluginContext(URL url) {
        JARPluginContext context = new JARPluginContext(this, url);
        if (url != null) {
            try {
                JarFile jar = new JarFile(url.getFile());
                Manifest manifest = jar.getManifest();
                if (manifest != null) {
                    PluginManifest pluginManifest = new PluginManifest(manifest);
                    context.setPluginManifest(pluginManifest);
                }
            } catch (Exception e) {
            }
        }

        return context;
    }

    @Override
    public String getID() {
        return "jar";
    }

    @Override
    protected Map<String, Object> getExtendedAttributes(PluginDescriptor pluginDescriptor) {
        Map<String, Object> attributes = super.getExtendedAttributes(pluginDescriptor);
        PluginContext context = pluginDescriptor.getContext();

        attributes.put(AUTHOR, pluginDescriptor.getAuthor());
        attributes.put(AUTHORURL, pluginDescriptor.getAuthorURL());
        attributes.put(AUTHOREMAIL, pluginDescriptor.getAuthorEmail());
        attributes.put(DATE, pluginDescriptor.getDate());
        attributes.put(VERSION, pluginDescriptor.getVersion());
        attributes.put(DESCRIPTION, pluginDescriptor.getDescription());

        URL url = (URL) context.get(JARPluginContext.IMAGE_URL);
        if (url != null)
            attributes.put(IMAGEURL, url.toString());

        return attributes;
    }

    @Override
    protected void setExtendedAttributes(PluginDescriptor pluginDescriptor,
            Map<String, Object> attributes) {
        super.setExtendedAttributes(pluginDescriptor, attributes);
        JARPluginContext context = (JARPluginContext) pluginDescriptor.getContext();
        DefaultPluginDescriptor defaultDescriptor = (DefaultPluginDescriptor) pluginDescriptor;

        defaultDescriptor.setDescription((String) attributes.get(DESCRIPTION));
        defaultDescriptor.setDate((String) attributes.get(DATE));
        defaultDescriptor.setVersion((String) attributes.get(VERSION));
        defaultDescriptor.setAuthor((String) attributes.get(AUTHOR));
        defaultDescriptor.setAuthorEmail((String) attributes.get(AUTHOREMAIL));
        defaultDescriptor.setAuthorURL((String) attributes.get(AUTHORURL));

        URL url = context.getURL();

        if (url != null && url.toString().toLowerCase().endsWith(".jar")) {
            try {
                JarFile jar = new JarFile(url.getFile());
                if (jar != null) {
                    context.setJAR(url);
                }
            } catch (Exception e) {
            }
        }

        String sImageURL = (String) attributes.get(IMAGEURL);
        if (sImageURL != null) {
            try {
                URL imageURL = new URL(sImageURL);
                Image image = ImageUtilities.loadImage(imageURL);
                defaultDescriptor.setImage(image);
            } catch (Exception e) {
            }
        }
    }

}
