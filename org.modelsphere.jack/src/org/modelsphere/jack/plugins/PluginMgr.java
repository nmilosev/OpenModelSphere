/*************************************************************************

Copyright (C) 2010 Grandite

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

import java.awt.Window;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JMenu;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.NullFrame;
import org.modelsphere.jack.awt.TextViewerDialog;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.plugins.PluginDescriptor.PLUGIN_TYPE;
import org.modelsphere.jack.plugins.dialog.PluginsManagerDialog;
import org.modelsphere.jack.plugins.io.BuiltInPluginLoader;
import org.modelsphere.jack.plugins.io.PluginContext;
import org.modelsphere.jack.plugins.io.PluginLoader;
import org.modelsphere.jack.plugins.io.XMLPluginContext;
import org.modelsphere.jack.plugins.xml.XmlPluginInstaller;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;
import org.modelsphere.jack.srtool.Splash;
import org.modelsphere.jack.srtool.actions.PluginDefaultAction;
import org.modelsphere.jack.srtool.actions.PluginGenerationAction;
import org.modelsphere.jack.srtool.forward.JackForwardEngineeringPlugin;
import org.modelsphere.jack.srtool.popupMenu.ApplicationPopupMenu;

/**
 * Uses for reverse, forward and jdbc driver plugins.
 * This class provides services for managing plugins.
 * 
 * <p>
 * Plugin should not use this class.
 * 
 * 
 * @see Plugin
 * 
 * @version 1 10/15/2000
 * @author Gino Pelletier
 * @author Marco Savard
 * 
 *         HISTORY : - version 9 [MS] : Re-engineered PluginMgr to convert a utility-pattern class
 *         into a singleton-pattern class. A utility class (whose methods are static) creates
 *         problems when these methods share data, because those data must be static in order to be
 *         used by the static methods, and static data are set when the class is loaded. Because we
 *         have little control of the order when the classes are loaded, in this case is better to
 *         use a singleton.
 *         
 *         VERSION 3.2 : Support of XML Plug-ins (Eclipse-like)
 *           load time version 3.1: 2977 ms
 *                     version 3.2: 24 ms
 */

public final class PluginMgr {
    public static final String START_OPTION_NO_PLUGINS = "noplugins"; // NOT LOCALIZABLE

    private static final String kPluginTitle = LocaleMgr.misc.getString("PluginTitle");
    private static final String kInstallingPlugins = LocaleMgr.misc.getString("InstallingPlugins");
    private static final String kDuplicatedSignature = LocaleMgr.misc
            .getString("DuplicatedSignature");
    private static final String kInitializing = LocaleMgr.misc.getString("Initializing");
    private static final String kLoadingConfiguration = LocaleMgr.misc
            .getString("LoadingConfiguration");
    private static final String kScanning = LocaleMgr.misc.getString("ScanningForPlugins");
    private static final String kVerifyingPlugins = LocaleMgr.misc.getString("VerifyingPlugins");

    //	private ArrayList<DefaultPluginDescriptor> defaultPluginDescriptors = new ArrayList<DefaultPluginDescriptor>();

    private boolean initialized = false;
    private boolean pluginsLoaded = false;

    private Splash splashWindow;

    private PluginsManagerDialog dialog;

    private PluginsRegistry pluginsRegistry = new PluginsRegistry();

    private static boolean pluginEnable = true;

    private ArrayList<PluginsListener> pluginsListeners = new ArrayList<PluginsListener>();

    // /////////////
    // SINGLETON
    private PluginMgr() {
        Properties properties = ApplicationContext.getCommandLineProperties(); 
        if (properties != null) {
            String property = properties.getProperty(START_OPTION_NO_PLUGINS);
            pluginEnable = (property == null);
        } else {
            pluginEnable = PluginsActivator.isPluginsEnabled();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            @Override
            public void run() {
                if (pluginEnable) {
                    PluginsConfiguration.getSingleton().save();
                }
            }
        }));

    } // end pluginMgr()

    private static PluginMgr g_singleInstance = null;

    public static PluginMgr getSingleInstance() {
        if (g_singleInstance == null) {
            g_singleInstance = new PluginMgr();
        }

        return g_singleInstance;
    }

    //
    // //////////////////
    
    public void loadPlugins(Splash splash, Class<? extends Plugin>[] builtInPlugins) {    	
    	try {
        	//load old-style plug-ins
        	loadOldPlugins(splash, builtInPlugins);
        	
        	//load new-style plug-ins
        	XmlPluginInstaller installer = XmlPluginInstaller.getInstance();
        	installer.installSignatures(splash, pluginsRegistry); 
    	} finally {
    		splashWindow = null;
            pluginsLoaded = true;
    	}
    }
    
    public final void initPlugins(Splash splash) {    
    	//init old-style plug-ins
    	initOldPlugins(splash);
    	
    	//init new-style plug-ins
    	XmlPluginInstaller installer = XmlPluginInstaller.getInstance();
    	installer.install(splash, pluginsRegistry); 
    }

    private void loadOldPlugins(Splash splash, Class<? extends Plugin>[] builtInPlugins) {
        // return now if no plug-in to load
        if (initialized || !pluginEnable)
            return;
        splashWindow = splash;

        if (splashWindow != null)
            splashWindow.setGUIText(kInitializing);

        ((BuiltInPluginLoader) PluginLoader.getBuiltInInstance()).setBuiltInPlugins(builtInPlugins);

        ArrayList<PluginDescriptor> tempPlugins = new ArrayList<PluginDescriptor>();

        // add built ins first
        if (builtInPlugins != null) {
            List<PluginDescriptor> pluginDescriptors = PluginLoader.getBuiltInInstance().scan();
            for (PluginDescriptor pluginDescriptor : pluginDescriptors) {
                tempPlugins.add(pluginDescriptor);
            }
        }

        // keep Plugin classes not instance of Rule class (must be init after all Rule plugins)
        ArrayList<PluginDescriptor> notRuleClassStructs = new ArrayList<PluginDescriptor>();

        PluginsConfiguration pluginsConfiguration = PluginsConfiguration.getSingleton();

        if (splashWindow != null)
            splashWindow.setGUIText(kLoadingConfiguration);

        List<PluginDescriptor> scannedPlugins = new ArrayList<PluginDescriptor>();
        List<PluginDescriptor> configuredPlugins = new ArrayList<PluginDescriptor>();

        boolean configurationLoaded = pluginsConfiguration.load(configuredPlugins);
        PluginLoader xmlPluginLoader = PluginLoader.getXMLInstance();
        List<PluginDescriptor> xmlPlugins = xmlPluginLoader.scan();

        if (configurationLoaded) {
            // Check if the scanned plugin (from xml) has a saved configuration.
            // If so, use the entry from the scanning and apply the configurable 
            // properties from the configuration to it. (This ensure that all the xml properties
            // contains in the plugin.xml file are available.  Configuration from xml plugins 
            // only save minor properties.  We also ensure the use of up to date values)
            for (Iterator<PluginDescriptor> pluginsIterator = xmlPlugins.iterator(); pluginsIterator
                    .hasNext();) {
                PluginDescriptor scannedXMLPlugin = pluginsIterator.next();
                int index = configuredPlugins.indexOf(scannedXMLPlugin);
                if (index > -1) {
                    PluginDescriptor configuredPlugin = configuredPlugins.get(index);
                    PluginLoader.applyConfiguration(scannedXMLPlugin, configuredPlugin);
                    configuredPlugins.remove(index);
                }
            }
            // Check if a configuration exist without the scanned entry.  
            // If so, remove the configuration (deleted plugin)
            for (Iterator<PluginDescriptor> iterator = configuredPlugins.iterator(); iterator
                    .hasNext();) {
                PluginDescriptor configuredPlugin = iterator.next();
                if (configuredPlugin.getContext().getLoader() == xmlPluginLoader
                        && !xmlPlugins.contains(configuredPlugin)) {
                    iterator.remove();
                }
            }
            tempPlugins.addAll(configuredPlugins);
        } else {
            if (splashWindow != null)
                splashWindow.setGUIText(kScanning);
            List<PluginDescriptor> plugins = PluginLoader.getJARInstance().scan();
            for (PluginDescriptor pluginDescriptor : plugins) {
                scannedPlugins.add(pluginDescriptor);
            }
            plugins = PluginLoader.getClassInstance().scan();
            for (PluginDescriptor pluginDescriptor : plugins) {
                scannedPlugins.add(pluginDescriptor);
            }
            tempPlugins.addAll(scannedPlugins);
        }

        tempPlugins.addAll(xmlPlugins);

        for (PluginDescriptor pluginDescriptor : tempPlugins) {
            if (pluginsRegistry.contains(pluginDescriptor)) {
                pluginDescriptor.getContext().getLoader().invalidate(pluginDescriptor, null,
                        kDuplicatedSignature, null);
            }

            pluginsRegistry.add(pluginDescriptor);
        }

        if (splashWindow != null)
            splashWindow.setGUIText(kVerifyingPlugins);

        // init plugin classes if enabled
        Iterator<PluginDescriptor> iter = pluginsRegistry.iterator();
        while (iter.hasNext()) {
            PluginDescriptor pluginDescriptor = iter.next();
            if (!pluginDescriptor.isEnabled())
                continue;
            
            boolean initialized = initPluginClass(pluginDescriptor);
            
            if (! initialized) {
            	PluginContext context = pluginDescriptor.getContext();
            	
            	// if a result from a file scan, purge any pluginInfo for which the class is not resolved
            	// this is not applicable for OMS 3.2 plug-ins
            	if (! (context instanceof XMLPluginContext)) { 
            		iter.remove();
            	}
            }
        }

        // create Rule instances first
        iter = pluginsRegistry.iterator();
        while (iter.hasNext()) {
            PluginDescriptor pluginDescriptor = iter.next();
            if (!pluginDescriptor.isEnabled())
                continue;
            if (pluginDescriptor.getType() != PLUGIN_TYPE.RULE) {
                notRuleClassStructs.add(pluginDescriptor);
            } else {
                if (!initPluginInstance(pluginDescriptor)) {
                    iter.remove();
                }
            }
        }

        iter = pluginsRegistry.iterator();
        while (iter.hasNext()) {
            PluginDescriptor pluginDescriptor = iter.next();
            if (!pluginDescriptor.isEnabled())
                continue;
            if (pluginDescriptor.getType() == PLUGIN_TYPE.RULE) {
                continue;
            } else {
                if (!initPluginInstance(pluginDescriptor)) {
                    iter.remove();
                } else if (isActive(pluginDescriptor)) {
                    initPluginAction(pluginDescriptor);
                }
            }
        }
    }

    public void showPluginsDialog(Window frame) {
        // if parameter -noplugins was not specified, but plug-ins are not
        // loaded yet, then an error occured so throws an exception
        if (pluginEnable) {
            if (pluginsLoaded == false) {
                throw new RuntimeException("Plugins not loaded"); // NOT LOCALIZABLE - exception
            }
        }
        PluginsReportBuilder builder = new PluginsReportBuilder();
        builder.write(pluginsRegistry);

        String sText = builder.toString();
        final TextViewerDialog diag = new TextViewerDialog((frame == null ? NullFrame.singleton
                : frame), kPluginTitle, sText, true, true);
        diag.setSize(800, 600);
        AwtUtil.centerWindow(diag);
        diag.setVisible(true);
    }

    private class PopupMap {
        Class<?> clazz;
        ArrayList<String> keys = new ArrayList<String>();

        PopupMap(Class<?> c) {
            this.clazz = c;
        }

        void add(String key) {
            keys.add(key);
        }
    }

    private void initPluginAction(PluginDescriptor pluginDescriptor) {
        if (pluginDescriptor == null)
            return;
        PluginContext context = pluginDescriptor.getContext();
        Plugin plugin = context.getInstance();
        if (plugin == null)
            return;
        try {
            PluginAction action = null;
            if (plugin instanceof Plugin2) {
                Plugin2 p2 = (Plugin2) plugin;
                action = p2.getPluginAction();
                if (action != null && action.getPlugin() != plugin)
                    action = null;
            }

            if (action == null && plugin instanceof JackForwardEngineeringPlugin) {
                action = new PluginGenerationAction((JackForwardEngineeringPlugin) plugin);
            } else if (action == null) {
                action = new PluginDefaultAction(plugin, pluginDescriptor.getName());
            }

            AbstractActionsStore actionstore = ApplicationContext.getActionStore();
            String actionkey = plugin.getClass().getName();
            if (actionstore.get(actionkey) == null) {
                actionstore.put(actionkey, action);
            }
        } catch (Throwable th) {
            context.getLoader().invalidate(pluginDescriptor, th, null, null);
        }
    }

    private final void initOldPlugins(Splash splash) {
        if (initialized || !pluginEnable)
            return;
        splashWindow = splash;

        if (splash != null)
            splash.setGUIText(kInstallingPlugins + "... "); // NOT LOCALIZABLE

        DefaultMainFrame frame = ApplicationContext.getDefaultMainFrame();
        if (frame == null)
            return;
        MainFrameMenu menumanager = frame.getMainFrameMenu();

        Iterator<PluginDescriptor> iter = pluginsRegistry.iterator();
        ArrayList<String> separatorAdded = new ArrayList<String>(5);
        AbstractActionsStore actionstore = ApplicationContext.getActionStore();

        while (iter.hasNext()) {
            PluginDescriptor pluginDescriptor = iter.next();
            if (!isActive(pluginDescriptor)) {
                continue;
            }
            Plugin plugin = pluginDescriptor.getContext().getInstance();
            try {
                String menukey = plugin.installAction(frame, menumanager);
                String actionkey = plugin.getClass().getName();
                AbstractApplicationAction action = actionstore.getAction(actionkey);
                if (menukey != null && menukey.length() > 0) { // install in
                    // menu
                    JMenu menu = menumanager.getMenuForKey(menukey);
                    if (menu == null)
                        continue;
                    if (!separatorAdded.contains(menukey)) {
                        separatorAdded.add(menukey);
                        menu.addSeparator();
                    }
                    if (action != null) {
                        menu.add(action);
                    }
                }
            } catch (Exception e) {
            }
        }

        ApplicationPopupMenu applpopup = ApplicationContext.getApplPopupMenu();
        HashMap<Class<? extends Object>, PopupMap> popupmaps = new HashMap<Class<? extends Object>, PopupMap>();
        ArrayList<String> actionkeys = new ArrayList<String>();

        Iterator<PluginDescriptor> iter2 = pluginsRegistry.iterator();
        while (iter2.hasNext()) {
            PluginDescriptor pluginDescriptor = iter2.next();
            if (!isActive(pluginDescriptor)) {
                continue;
            }
            Plugin plugin = pluginDescriptor.getContext().getInstance();
            Class<? extends Object>[] supportedclasses = plugin.getSupportedClasses();
            if (supportedclasses == null)
                continue;
            // plugin.getActionsKeys
            String actionkey = plugin.getClass().getName();
            actionkeys.add(actionkey);
            actionkeys.add(actionkey);

            for (int i = 0; i < supportedclasses.length; i++) {
                PopupMap map = popupmaps.get(supportedclasses[i]);
                if (map == null) {
                    map = new PopupMap(supportedclasses[i]);
                    popupmaps.put(supportedclasses[i], map);
                }
                map.add(actionkey);
            }
        }

        Iterator<Class<? extends Object>> iter3 = popupmaps.keySet().iterator();
        ArrayList<Object> popups = new ArrayList<Object>();
        while (iter3.hasNext()) {
            PopupMap map = (PopupMap) popupmaps.get(iter3.next());
            Object[] actionkeysobj = map.keys.toArray();
            popups.add(map.clazz);
            String[] actkeys = new String[actionkeysobj.length];
            for (int i = 0; i < actionkeysobj.length; i++) {
                actkeys[i] = (String) actionkeysobj[i];
            }
            popups.add(actkeys);
        }

        String[] actionkeysarray = new String[actionkeys.size()];
        for (int i = 0; i < actionkeys.size(); i++) {
            actionkeysarray[i] = (String) actionkeys.get(i);
        }

        applpopup.registerPopups(popups.toArray(), actionkeysarray);

        initialized = true;
        splashWindow = null;
        firePluginsListener();
    }

    /**
     * @param <T>
     * @param pluginclass
     * @return
     * @deprecated - replaced with getPluginsStore().getActivePluginInstances(Class<T>)
     */
    public final <T extends Plugin> ArrayList<T> getActivePluginInstances(Class<T> pluginclass) {
        return pluginsRegistry.getActivePluginInstances(pluginclass);
    }

    /**
     * @param pluginClassName
     * @return
     * @deprecated use getPluginsStore().getPluginClass(String) instead
     */
    public final Class<? extends Plugin> getPluginClass(String pluginClassName) {
        return pluginsRegistry.getPluginClass(pluginClassName);
    }

    public final Class<? extends Plugin> findActivePluginClass(String pluginClassName,
            String className) {
        if (!pluginEnable)
            return null;
        if (pluginClassName == null || className == null)
            return null;

        PluginDescriptor pluginDescriptor = pluginsRegistry.getPluginInfo(pluginClassName);
        if (pluginDescriptor == null || !isActive(pluginDescriptor))
            return null;

        ClassLoader loader = pluginDescriptor.getContext().getClassLoader();
        if (loader == null) {
            System.out.println("Undefined class loader for " + pluginDescriptor);
            return null;
        }
        try {
            @SuppressWarnings("unchecked")
            Class<? extends Plugin> c = (Class<? extends Plugin>) loader.loadClass(className);
            return c;
        } catch (Exception e) {
        } catch (Error er) {
        }
        return null;
    }

    public final Map<Plugin, OptionGroup> getPluginOptionGroups() {
        List<PluginDescriptor> pluginDescriptors = pluginsRegistry.getActivePlugins();
        Map<Plugin, OptionGroup> optionGroupMap = new HashMap<Plugin, OptionGroup>();
        for (PluginDescriptor pluginDescriptor : pluginDescriptors) {
            OptionGroup optionGroup = pluginDescriptor.getContext().getOptionGroup();
            if (optionGroup != null) {
                optionGroupMap.put(pluginDescriptor.getContext().getInstance(), optionGroup);
            }
        }
        return optionGroupMap;
    }

    private boolean initPluginClass(PluginDescriptor pluginDescriptor) {
        PluginContext context = pluginDescriptor.getContext();
        PluginLoader loader = context == null ? null : context.getLoader();
        return loader.initPluginClass(pluginDescriptor, splashWindow);
    }

    private boolean initPluginInstance(PluginDescriptor pluginDescriptor) {
        PluginContext context = pluginDescriptor.getContext();
        PluginLoader loader = context == null ? null : context.getLoader();
        return loader.initPluginInstance(pluginDescriptor, splashWindow);
    }

    public void showConfigurationDialog() {
        if (dialog == null) {
            List<PluginDescriptor> plugins = pluginsRegistry.getValidPlugins();

            for (Iterator<PluginDescriptor> iterator = plugins.iterator(); iterator.hasNext();) {
                PluginDescriptor pluginDescriptor = iterator.next();
                if (pluginDescriptor.getType() == PLUGIN_TYPE.RULE)
                    iterator.remove();
            }

            dialog = new PluginsManagerDialog(plugins, new PluginConfigurationHandler());
        }
        dialog.setVisible(true);
    }

    public PluginsRegistry getPluginsRegistry() {
        return pluginsRegistry;
    }

    public boolean isValid(PluginDescriptor descriptor) {
        if (descriptor == null)
            return false;
        return PluginLoader.isValid(descriptor);
    }

    public boolean isActive(PluginDescriptor descriptor) {
        if (descriptor == null)
            return false;
        PluginContext context = descriptor.getContext();
        if (context == null)
            return false;
        
        boolean active = context.getInstance() != null;
        active &= descriptor.isEnabled();
        active &= isValid(descriptor);
        active &= pluginsRegistry.contains(descriptor);
        
        return active;
    }

    void installPlugin(PluginDescriptor pluginDescriptor) {
        if (pluginDescriptor == null)
            return;
        pluginsRegistry.add(pluginDescriptor);
    }

    void removePlugin(PluginDescriptor pluginDescriptor) {
        if (pluginDescriptor == null)
            return;
        pluginsRegistry.remove(pluginDescriptor);
    }

    public final void addPluginsListener(PluginsListener l) {
        if (l != null && pluginsListeners.indexOf(l) == -1) {
            pluginsListeners.add(l);
            if (initialized)
                firePluginsListener(l);
        }
    }

    public final void removePluginsListener(PluginsListener l) {
        pluginsListeners.remove(l);
    }

    private void firePluginsListener() {
        for (int i = pluginsListeners.size(); --i >= 0;) {
            PluginsListener listener = pluginsListeners.get(i);
            listener.pluginsLoaded();
        }
    }

    private void firePluginsListener(PluginsListener listener) {
        if (listener != null)
            listener.pluginsLoaded();
    }

}
