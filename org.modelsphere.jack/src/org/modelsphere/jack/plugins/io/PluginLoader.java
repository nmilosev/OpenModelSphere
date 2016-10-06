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
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.debug.Log;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.plugins.*;
import org.modelsphere.jack.plugins.PluginDescriptor.PLUGIN_TYPE;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.Splash;
import org.modelsphere.jack.srtool.forward.Rule;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.text.MessageFormat;
import org.w3c.dom.*;

public abstract class PluginLoader implements PluginConstants {
    protected static final String kMissingResource = LocaleMgr.misc.getString("MissingResource");
    protected static final String kInvalidJarEntry = LocaleMgr.misc.getString("InvalidJarEntry");
    protected static final String kLoadingPlugin = LocaleMgr.misc.getString("LoadingPlugins0");
    protected static final String kLoadingRepository = LocaleMgr.misc
            .getString("LoadingRepository0");
    protected static final String kLoadedSuccess = LocaleMgr.misc.getString("LoadedSuccess");
    protected static final String kLoadingError = LocaleMgr.misc.getString("LoadingError");
    protected static final String kInvalidRelease = LocaleMgr.misc.getString("InvalidRelease");
    protected static final String kNotInstantiable = LocaleMgr.misc.getString("NotInstantiable");
    protected static final String kRequireAppl = LocaleMgr.misc.getString("RequireAppl0");
    public static final String START_OPTION_PLUGINS_PATH = "pluginpath"; // NOT LOCALIZABLE

    private static final String DEFAULT_PLUGINS_PATH = "." + System.getProperty("file.separator")
            + "plugins"; // NOT LOCALIZABLE

    private static PluginLoader XMLInstance;
    private static PluginLoader builtInInstance;
    private static PluginLoader JARInstance;
    private static PluginLoader classInstance;

    protected static final String CLASS_EXTENSION = ".class"; // NOT LOCALIZABLE - file extension
    protected static PluginClassLoader defaultClassLoader = new PluginClassLoader(ClassLoader
            .getSystemClassLoader());

    private static List<String> pluginsPath;

    private static File pluginsDirectory;

    private static HashMap<String, PluginLoader> loaders = new HashMap<String, PluginLoader>();

    // We use the context to store invalidated plugins instead of the descriptor.  Mode than 1 
    // descriptors can be equals() even if they represent different scanned/installed plugins
    // (case of duplicates).
    private List<PluginContext> invalidated = new ArrayList<PluginContext>();

    protected List<String> packages = new ArrayList<String>();

    static{   
            pluginsDirectory = new File(System.getProperty("user.dir"), "plugins");
            if ((!pluginsDirectory.exists()) && (ScreenPerspective.isFullVersion())) {
                try {
                    if (!pluginsDirectory.mkdir()) {
                        System.out.println("Failed to creating directory: " + pluginsDirectory);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            getInstances();       
    }
    private static void addToPluginsPath(String path) {
        if (pluginsPath == null)
            return;

        if (!pluginsPath.contains(path)) {
            pluginsPath.add(path);
        }
    }

    private static ArrayList<String> getPaths(String path) {
        if (path == null)
            return new ArrayList<String>();
        ArrayList<String> paths = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(path, File.pathSeparator, false);
        while (st.hasMoreElements()) {
            String token = (String) st.nextElement();
            paths.add(token);
        }
        return paths;
    }

    private static void initPluginPath() {
        Properties properties = ApplicationContext.getCommandLineProperties(); 
        String startpathparam = null;

        if (properties != null) { // was not checked before, a NullPointerException was thrown [MS]
            String pathParameter = getOptionPluginsPath();
            startpathparam = properties.getProperty(pathParameter);
        }

        if (startpathparam == null) {
            startpathparam = DEFAULT_PLUGINS_PATH;
        }

        pluginsPath = getPaths(startpathparam);
    }
    
    public static String getOptionPluginsPath() {
        return START_OPTION_PLUGINS_PATH;
    }

    public static File getPluginsDirectory() {
        return pluginsDirectory;
    }

    public static List<String> getPluginsPath() {
        if (pluginsPath == null)
            initPluginPath();
        return pluginsPath;
    }

    PluginLoader() {
        String id = getID();
        if (id == null) {
            throw new IllegalStateException("Invalid ID for loader " + getClass());
        }
        if (loaders.containsKey(id)) {
            throw new IllegalStateException("Duplicate ID for loader " + getClass());
        }
        loaders.put(id, this);
    }

    public static PluginLoader getXMLInstance() {
        if (XMLInstance == null) {
            XMLInstance = new XMLPluginLoader();
        }
        return XMLInstance;
    }

    public static PluginLoader getBuiltInInstance() {
        if (builtInInstance == null) {
            builtInInstance = new BuiltInPluginLoader();
        }
        return builtInInstance;
    }

    public static PluginLoader getJARInstance() {
        if (JARInstance == null) {
            JARInstance = new JARPluginLoader();
        }
        return JARInstance;
    }

    public static PluginLoader getClassInstance() {
        if (classInstance == null) {
            classInstance = new ClassPluginLoader();
        }
        return classInstance;
    }

    public static PluginLoader[] getInstances() {
        return new PluginLoader[] { getBuiltInInstance(), getJARInstance(), getXMLInstance(),
                getClassInstance() };
    }

    public static PluginLoader getPluginLoader(String id) {
        if (id == null)
            return null;
        return loaders.get(id);
    }

    public abstract String getID();

    public PluginContext createPluginContext(URL url) {
        return new PluginContext(this, url);
    }

    public List<PluginDescriptor> scan() {
        ArrayList<String> scannedPath = new ArrayList<String>();
        List<PluginDescriptor> pluginInfos = new ArrayList<PluginDescriptor>();

        Iterator<String> iterPath = getPluginsPath().iterator();
        while (iterPath.hasNext()) {
            String path = iterPath.next();
            if (scannedPath.contains(path))
                continue;
            List<PluginDescriptor> scannedPluginInfos = scan(path);
            if (scannedPluginInfos == null)
                continue;
            pluginInfos.addAll(scannedPluginInfos);
            scannedPath.add(path);
        }
        return pluginInfos;
    }

    protected List<PluginDescriptor> scan(String path) {
        File dirFile = new File(path);
        if (dirFile == null || !dirFile.exists())
            return null;
        // allow check for plugin in directories and jar files
        String name = dirFile.getName().toLowerCase();
        if (!dirFile.isDirectory() && !name.endsWith(".jar") && !name.endsWith(".class")) // NOT LOCALIZABLE, file extension
            return null;
        List<PluginDescriptor> foundClasses = scan(dirFile, dirFile);
        packages.clear();
        return foundClasses;
    }

    protected List<PluginDescriptor> scan(File directory, File scanRoot) {
        return null;
    }

    protected URL getBaseURL(File file, String packageName) throws MalformedURLException {
        packageName = packageName.replace('.', File.separatorChar);
        String filename = packageName + file.getName();
        String path = file.getAbsolutePath();
        int idx = path.indexOf(filename);
        if (idx != -1) {
            path = path.substring(0, idx - 1);
            File base = new File(path);
            URL url = base.toURL();
            return url;
        } else {
            URL url = file.toURL();
            return url;
        }
    }

    public boolean initPluginClass(PluginDescriptor pluginDescriptor, Splash splashScreen) {
        if (!isValid(pluginDescriptor))
            return true;
        Class<? extends Plugin> pluginClass = null;
        PluginContext context = pluginDescriptor.getContext();
        
        //XML plugin (format 2.0 for OMS 3.2) will be initialized later
        if (context instanceof XMLPluginContext) {
        	return false;
        }
        
        String className = pluginDescriptor.getClassName();

        try {
            ClassLoader classLoader = context.getClassLoader();
            if (classLoader == null) {
                URL url = context.getURL();
                if (url != null) {
                    classLoader = new URLClassLoader(new URL[] { url });
                } else {
                    System.out.println("Undefined class loader for " + pluginDescriptor);
                    return false;
                }
                context.setClassLoader(classLoader);
            }
            pluginClass = (Class<? extends Plugin>) Class.forName(className, true, classLoader);
            if (pluginClass != null && Plugin.class.isAssignableFrom(pluginClass)
                    && Modifier.isPublic(pluginClass.getModifiers())) {
                ((DefaultPluginDescriptor) pluginDescriptor).setPluginClass(pluginClass);
                ((DefaultPluginDescriptor) pluginDescriptor).setPluginType(PLUGIN_TYPE
                        .getType(pluginClass));
                return true;
            } else {
                // not a plugin
                ((DefaultPluginDescriptor) pluginDescriptor).setPluginType(null);
                return false;
            }
        } catch (ClassNotFoundException e1) {
            // If class not found, this means that the packagename is not the valid package name for the class
            // Do not display this error ... except if in jar (bad class specification in jarfile)
            URL url = context.getURL();
            if (url != null && url.getFile() != null
                    && url.getFile().toLowerCase().endsWith(".jar")) { // NOT LOCALIZABLE, file extension
                String errortext = e1.toString();
                context.setStatusText(kInvalidJarEntry + "  (" + errortext + ")");
            } else if (Debug.isDebug())
                e1.printStackTrace();
        } catch (NoClassDefFoundError noclasserror) {
            String errortext = noclasserror.toString();
            context.setStatusText(kMissingResource + "  (" + errortext + ")"); // NOT LOCALIZABLE
        } catch (ExceptionInInitializerError initerror) {
            Throwable th = initerror.getException();
            String errortext = th.toString();
            context.setStatusText(errortext);
        } catch (Throwable th) {
            String errortext = th.toString();
            context.setStatusText(errortext);
        }
        invalidate(pluginDescriptor, null, null, null);
        return true;
    }

    public boolean initPluginInstance(PluginDescriptor pluginDescriptor, Splash splashScreen) {
        if (pluginDescriptor == null) {
            return false;
        }
        PluginContext context = pluginDescriptor.getContext();
        Class<? extends Plugin> pluginClass = pluginDescriptor.getPluginClass();
        if (pluginClass == null) {
            return true;
        }
        if (pluginClass.isInterface() || Modifier.isAbstract(pluginClass.getModifiers())) {
            return false;
        }

        if (pluginDescriptor.getContext().getInstance() != null) {
            throw new IllegalStateException("initPluginInstance():  Instance already created.");
        }

        String loadingmessage = "";
        if (Rule.class.isAssignableFrom(pluginClass)) {
            loadingmessage = MessageFormat.format(kLoadingRepository, new Object[] {
                    pluginClass.getName(), "...", "\'" });
        } else {
            loadingmessage = MessageFormat.format(kLoadingPlugin, new Object[] {
                    pluginClass.getName(), "...", "\'" });
        }

        String errortext = "";
        if (splashScreen != null)
            splashScreen.setGUIText(loadingmessage);

        Plugin plugin = null;
        try {
            plugin = (Plugin) pluginClass.newInstance();
            loadingmessage += kLoadedSuccess;
        } catch (Exception ex1) {
            plugin = null;
            loadingmessage += kLoadingError;
            errortext = kNotInstantiable + "  (" + ex1.toString() + ")"; // NOT LOCALIZABLE
        } catch (NoClassDefFoundError ex2) {
            plugin = null;
            loadingmessage += kLoadingError;
            errortext = kMissingResource + "  (" + ex2.getMessage() + ")"; // NOT LOCALIZABLE
        } catch (Error ex3) {
            plugin = null;
            loadingmessage += kLoadingError;
            errortext = ex3.getMessage();
        }

        if (plugin != null) {
            context.setInstance(plugin);
            try {
                if (!isValid(pluginDescriptor)) {
                    errortext = MessageFormat.format(kRequireAppl, new Object[] { new Integer(
                            (plugin).getSignature().getBuildRequired()) });
                    plugin = null;
                    loadingmessage += kInvalidRelease;
                }
            } catch (Exception e1) {
                plugin = null;
                loadingmessage += kLoadingError;
                errortext = e1.getMessage();
            } catch (NoClassDefFoundError e2) {
                plugin = null;
                loadingmessage += kLoadingError;
                errortext = kMissingResource + "  (" + e2.getMessage() + ")"; // NOT LOCALIZABLE
            } catch (Error e3) {
                plugin = null;
                loadingmessage += kLoadingError;
                errortext = e3.getMessage();
            } // end try
        } // end if

        Log.add(loadingmessage, Log.LOG_PLUGIN);
        if (splashScreen != null)
            splashScreen.setGUIText(loadingmessage);

        if (plugin == null) {
            invalidate(pluginDescriptor, null, errortext, null);
        }

        return true;
    }

    public void invalidate(PluginDescriptor pluginDescriptor, Throwable throwable, String status,
            File file) {
        String errortext = null;
        if (status != null) {
            errortext = status;
        }
        if (throwable != null) {
            if (errortext != null) {
                errortext += ":  ";
            } else {
                errortext = "";
            }
            errortext += throwable.getLocalizedMessage();
        }
        if (pluginDescriptor != null) {
            invalidated.add(pluginDescriptor.getContext());
            if (status != null || throwable != null) // Do not override
                pluginDescriptor.getContext().setStatusText(errortext);
        } else if (file != null) {
            Debug.trace(file.getPath() + ": " + errortext);
        }

        if (throwable != null) {
            Debug.trace(throwable);
        }
    }

    public static boolean save(PluginDescriptor pluginDescriptor, Document document, Element element) {
        if (pluginDescriptor == null || document == null || element == null)
            return false;

        PluginContext context = pluginDescriptor.getContext();
        PluginLoader loader = context.getLoader();
        boolean saved = loader.saveImpl(pluginDescriptor, document, element);
        return saved;
    }

    protected boolean saveImpl(PluginDescriptor pluginDescriptor, Document document, Element element) {
        if (pluginDescriptor == null || document == null || element == null)
            return false;
        Map<String, Object> attributes = getExtendedAttributes(pluginDescriptor);

        element.setAttribute(ID, pluginDescriptor.getClassName());
        element.setAttribute(LOADER, pluginDescriptor.getContext().getLoader().getID());

        if (attributes != null) {
            for (String key : attributes.keySet()) {
                Object value = attributes.get(key);
                if (value == null)
                    continue;
                Element childElement = document.createElement(key);
                childElement.setTextContent(value.toString());
                element.appendChild(childElement);
            }
        }
        return true;
    }

    protected Map<String, Object> getExtendedAttributes(PluginDescriptor pluginDescriptor) {
        Map<String, Object> attributes = new HashMap<String, Object>();
        PluginContext context = pluginDescriptor.getContext();

        attributes.put(TYPE, pluginDescriptor.getType().toString());
        attributes.put(NAME, pluginDescriptor.getName());

        URL url = context.getURL();
        if (url != null)
            attributes.put(URL, url.toString());

        attributes.put(ENABLED, pluginDescriptor.isEnabled());

        ConfigurationCommand command = context.getConfigurationCommand();
        if (command != null) {
            String commandName = command.getCommand();
            String values = command.getValues();
            if (commandName != null) {
                if (values == null)
                    values = "";
                attributes.put("command", commandName + ":" + values);
            }
        }
        return attributes;
    }

    public static PluginDescriptor load(Node pluginNode) {
        if (pluginNode == null)
            return null;

        Map<String, Object> extendedAttributes = new HashMap<String, Object>();
        NamedNodeMap attributes = pluginNode.getAttributes();
        String loaderID = null;
        if (attributes != null) {
            // set mandatory attributes
            Node classNameNode = attributes.getNamedItem(ID);
            if (classNameNode != null) {
                String className = classNameNode.getNodeValue();
                extendedAttributes.put(ID, className);
            }
            Node loaderNode = attributes.getNamedItem(LOADER);
            if (loaderNode != null) {
                loaderID = loaderNode.getNodeValue();
                extendedAttributes.put(LOADER, loaderID);
            }
        }

        if (loaderID == null || loaderID.trim().length() == 0) {
            return null;
        }

        PluginLoader loader = getPluginLoader(loaderID);
        if (loader == null)
            return null;

        PluginDescriptor desc = loader.loadImpl(pluginNode, extendedAttributes);
        return desc;
    }

    protected PluginDescriptor loadImpl(Node pluginNode, Map<String, Object> extendedAttributes) {
        NodeList children = pluginNode.getChildNodes();
        int count = children.getLength();
        String commandname = null;
        String commandvalues = null;

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

            if (name.equals("command")) {
                int index = value.indexOf(':');
                if (index > -1) {
                    commandname = value.substring(0, index);
                    commandvalues = null;
                    if (index < value.length())
                        commandvalues = value.substring(index + 1, value.length());
                }
            } else {
                extendedAttributes.put(name, value);
            }
        } //end for

        String sURL = (String) extendedAttributes.get(URL);
        URL url;
        try {
            url = (sURL == null) ? null : new URL(sURL);
        } catch (MalformedURLException e) {
            url = null;
        }

        String id = (String) extendedAttributes.get(ID);
        PluginDescriptor pluginDescriptor = new DefaultPluginDescriptor(id);
        PluginContext context = createPluginContext(url);
        ((DefaultPluginDescriptor) pluginDescriptor).setContext(context);

        setExtendedAttributes(pluginDescriptor, extendedAttributes);

        if (commandname != null && isConfigurationCommandSupported(commandname)) {
            ConfigurationCommand command = ConfigurationCommandFactory.createConfigurationCommand(
                    commandname, commandvalues);
            if (command != null) {
                pluginDescriptor = command.execute(pluginDescriptor);
            }
        }

        return pluginDescriptor;
    }

    protected void setExtendedAttributes(PluginDescriptor pluginDescriptor,
            Map<String, Object> attributes) {
        DefaultPluginDescriptor defaultDescriptor = (DefaultPluginDescriptor) pluginDescriptor;
        defaultDescriptor.setName((String) attributes.get(NAME));
        defaultDescriptor.setPluginType(PLUGIN_TYPE.toType((String) attributes.get(TYPE)));
        defaultDescriptor.setEnabled(Boolean.parseBoolean((String) attributes.get(ENABLED)));
    }

    public static boolean isValid(PluginDescriptor pluginDescriptor) {
        PluginContext context = pluginDescriptor.getContext();
        PluginLoader loader = context.getLoader();
        if (loader == null)
            return false;
        return loader.isValidImpl(pluginDescriptor) && checkCompatibility(pluginDescriptor)
                && PluginSecurityManager.getInstance().verify(pluginDescriptor);
    }

    protected boolean isValidImpl(PluginDescriptor pluginDescriptor) {
        PluginContext context = pluginDescriptor.getContext();
        return !invalidated.contains(context);
    }

    protected static boolean checkCompatibility(PluginDescriptor descriptor) {
        PluginContext context = descriptor.getContext();
        if (context == null)
            return false;
        return context.getBuildRequired() <= ApplicationContext.APPLICATION_BUILD_ID;
    }

    public boolean isConfigurationCommandSupported(String command) {
        return false;
    }

    public static void registerCommand(PluginDescriptor pluginDescriptor,
            ConfigurationCommand configurationCommand) {
        if (pluginDescriptor == null || configurationCommand == null) {
            return;
        }
        PluginContext context = pluginDescriptor.getContext();
        if (context == null) {
            return;
        }
        PluginLoader loader = context.getLoader();
        if (loader == null) {
            return;
        }
        if (!loader.isConfigurationCommandSupported(configurationCommand.getCommand())) {
            return;
        }
        context.setConfigurationCommand(configurationCommand);
    }

    public static void applyConfiguration(PluginDescriptor pluginDescriptor,
            PluginDescriptor configurationDescriptor) {
        if (pluginDescriptor == null || configurationDescriptor == null)
            return;
        PluginContext context = pluginDescriptor.getContext();
        if (context == null) {
            return;
        }
        PluginLoader loader = context.getLoader();
        if (loader == null) {
            return;
        }
        if (configurationDescriptor.getContext() == null
                || configurationDescriptor.getContext().getLoader() != loader) {
            return;
        }
        loader.applyConfigurationImpl(pluginDescriptor, configurationDescriptor);
    }

    protected void applyConfigurationImpl(PluginDescriptor pluginDescriptor,
            PluginDescriptor configurationDescriptor) {
        pluginDescriptor.setEnabled(configurationDescriptor.isEnabled());
    }

    public static int compareVersions(PluginDescriptor pluginDescriptor1,
            PluginDescriptor pluginDescriptor2) {
        if (pluginDescriptor1 == null || pluginDescriptor2 == null)
            return -100;
        PluginContext context = pluginDescriptor1.getContext();
        if (context == null) {
            return -100;
        }
        PluginLoader loader = context.getLoader();
        if (loader == null) {
            return -100;
        }
        if (pluginDescriptor2.getContext() == null
                || pluginDescriptor2.getContext().getLoader() != loader) {
            return -100;
        }
        String version1 = pluginDescriptor1.getVersion();
        String version2 = pluginDescriptor2.getVersion();
        if (version2 == null && version1 == null) {
            return 0;
        }
        if (version2 == null ^ version1 == null) {
            return version2 == null ? -1 : 1;
        }
        if (version1.equals(version2)) {
            return 0;
        }
        int[] versions1 = splitVersion(version1);
        int[] versions2 = splitVersion(version2);
        return compareVersions(versions1, versions2);
    }

    private static int[] splitVersion(String version) {
        ArrayList<Integer> versions = new ArrayList<Integer>();
        StringTokenizer st = new StringTokenizer(version, "._- ()[]{}", false);
        // note: this algo doesnt support cases like 1.2 beta2 1.2 beta3 which would both result in 1,2,-4
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.length() == 0)
                continue;
            try {
                int value = Integer.parseInt(token);
                versions.add(value);
                continue;
            } catch (Exception e) {
            }
            token = token.toLowerCase();
            if (token.length() == 1) {
                // apply the char value
                char ch = token.charAt(0);
                if (Character.isLetter(ch)) {
                    versions.add((int) ch);
                }
                continue; // else ignore
            }
            if (token.contains("early")) {
                versions.add(-6);
            } else if (token.contains("alpha")) {
                versions.add(-5);
            } else if (token.contains("beta")) {
                versions.add(-4);
            } else if (token.contains("pre")) {
                versions.add(-2);
            } else if (token.contains("rc") || token.contains("candidate")) {
                versions.add(-1);
            }
        }
        int[] result = new int[versions.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = versions.get(i);
        }
        return result;
    }

    private static int compareVersions(int[] versions1, int[] versions2) {
        int count = Math.max(versions1.length, versions2.length);

        for (int i = 0; i < count; i++) {
            int value1 = versions1.length > i ? versions1[i] : 0;
            int value2 = versions2.length > i ? versions2[i] : 0;
            if (value1 == value2) {
                continue;
            }
            return value1 > value2 ? 1 : -1;
        }

        return 0;
    }

}
