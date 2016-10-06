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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.modelsphere.jack.plugins.Plugin;
import org.modelsphere.jack.plugins.PluginDescriptor;
import org.modelsphere.jack.srtool.forward.Rule;

public class ClassPluginLoader extends JARPluginLoader {

    ClassPluginLoader() {
    }

    @Override
    protected List<PluginDescriptor> scan(File directory, File scanRoot) {
        return scan(directory, scanRoot, "");
    }

    /**
     * 
     * @param packagename
     *            We construct the package name during the drill down and use it as the package for
     *            a found class. This allows support for classes not located in a jar file.
     * @param directory
     * @return
     */
    private ArrayList<PluginDescriptor> scan(File directory, File scanRoot, String packagename) {
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
                List<? extends PluginDescriptor> pluginDescriptors = scanFile(file, packagename);
                if (pluginDescriptors != null) {
                    scannedPluginInfo.addAll(pluginDescriptors);
                }
            } else {
                String subpackage = packagename + file.getName() + "."; // NOT LOCALIZABLE - package separator
                ArrayList<PluginDescriptor> subList = scan(file, scanRoot, subpackage);
                if (subList != null)
                    scannedPluginInfo.addAll(subList);
            }
        }

        return scannedPluginInfo;
    }

    private List<? extends PluginDescriptor> scanFile(File file, String packagename) {
        if (file == null || !file.isFile()) {
            return null;
        }
        List<DefaultPluginDescriptor> descriptors = new ArrayList<DefaultPluginDescriptor>();
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

                ClassPluginContext context = (ClassPluginContext) createPluginContext(jarurl);
                PluginManifest manifest = context.getPluginManifest();

                String entryPoint = manifest == null ? null : manifest.getPluginClassName();

                if (entryPoint == null) {
                    ClassLoader pluginloader = new URLClassLoader(new URL[] { jarurl },
                            defaultClassLoader);
                    // Allow many repository functions in one jar if no class specified in manifest
                    Enumeration<JarEntry> enumeration = jar.entries();
                    while (enumeration.hasMoreElements()) {
                        JarEntry entry = enumeration.nextElement();
                        if (!entry.getName().toLowerCase().endsWith(CLASS_EXTENSION))
                            continue;
                        // TODO --- This override the descriptor on each iteration
                        DefaultPluginDescriptor descriptor = addFromJar(jarurl, entry.getName(),
                                pluginloader, Rule.class);
                        if (descriptor != null) {
                            descriptors.add(descriptor);
                        }
                    }
                }
            } else {
                DefaultPluginDescriptor descriptor = addFromFile(file, packagename,
                        defaultClassLoader, Plugin.class);
                if (descriptor != null)
                    descriptors.add(descriptor);
            }
        } catch (IOException ioe) {
            descriptors = null;
        } // Invalid url access, skip
        return descriptors;
    }

    private DefaultPluginDescriptor addFromFile(File file, String packageName,
            ClassLoader classLoader, Class<? extends Plugin> assignableFrom)
            throws MalformedURLException {
        if (file == null || packageName == null || classLoader == null)
            return null;

        DefaultPluginDescriptor pluginDescriptor = null;
        try {
            if (packageName != null && file != null) {
                String filename = file.getName();
                if (file.canRead() && filename.endsWith(CLASS_EXTENSION)
                        && filename.indexOf('$') < 0) { // NOT LOCALIZABLE
                    URL url = getBaseURL(file, packageName);
                    String className = packageName
                            + filename.substring(0, filename.lastIndexOf(CLASS_EXTENSION));
                    pluginDescriptor = new DefaultPluginDescriptor(className);
                    ClassPluginContext context = (ClassPluginContext) createPluginContext(url);
                    context.setClassLoader(defaultClassLoader);
                    pluginDescriptor = new DefaultPluginDescriptor(className);
                    pluginDescriptor.setPluginType(DefaultPluginDescriptor.PLUGIN_TYPE
                            .getType(assignableFrom));
                    pluginDescriptor.setContext(context);
                }
            }
        } catch (Throwable th) {
            pluginDescriptor = null;
        }

        return pluginDescriptor;
    }

    private DefaultPluginDescriptor addFromJar(URL jarURL, String className,
            ClassLoader classLoader, Class<? extends Plugin> assignableFrom) {
        if (jarURL == null || className == null || classLoader == null)
            return null;

        DefaultPluginDescriptor pluginDescriptor = null;
        try {
            if (className != null && className.indexOf('$') < 0) {
                ClassPluginContext context = (ClassPluginContext) createPluginContext(jarURL);
                if (className.toLowerCase().endsWith(CLASS_EXTENSION)) {
                    className = className.substring(0, className.toLowerCase().lastIndexOf(
                            CLASS_EXTENSION));
                    className = className.replace('/', '.');
                    className = className.replace('\\', '.');
                    pluginDescriptor = new DefaultPluginDescriptor(className);
                } else {
                    pluginDescriptor = new DefaultPluginDescriptor(className);
                }
                pluginDescriptor.setContext(context);
                context.setClassLoader(classLoader);
                context.setJAR(jarURL);
                
                DefaultPluginDescriptor.PLUGIN_TYPE type = DefaultPluginDescriptor.PLUGIN_TYPE.getType(assignableFrom); 
                pluginDescriptor.setPluginType(type);	
   
            }
        } catch (Throwable th) {
            invalidate(pluginDescriptor, th, null, new File(jarURL.getFile()));
        }

        return pluginDescriptor;
    }

    @Override
    public String getID() {
        return "class";
    }

    @Override
    public PluginContext createPluginContext(URL url) {
        ClassPluginContext context = new ClassPluginContext(this, url);
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

}
