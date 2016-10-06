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

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.Action;

import org.modelsphere.jack.plugins.Plugin;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.preference.OptionGroup;

public class PluginContext {
    public static final String STATUS_TEXT = "PluginContext.status";

    private PluginLoader loader;
    private URL url;
    private ClassLoader classLoader;

    private Plugin instance;
    private OptionGroup optionGroup;

    private int buildRequired = -1;

    private HashMap<String, Object> properties = new HashMap<String, Object>();

    private HashMap<String, Action> actionsMap = new HashMap<String, Action>();

    private ConfigurationCommand configurationCommand;

    private ResourceBundle resourceBundle;

    public PluginContext(PluginLoader loader, URL pluginURL) {
        this.loader = loader;
        this.url = pluginURL;
    }

    public Object get(String key) {
        return properties.get(key);
    }

    public Object put(String key, Object value) {
        return properties.put(key, value);
    }

    public final PluginLoader getLoader() {
        return loader;
    }

    void setLoader(PluginLoader loader) {
        this.loader = loader;
    }

    public final URL getURL() {
        return url;
    }

    public final ClassLoader getClassLoader() {
        return classLoader;
    }

    public final void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String getStatusText() {
        return (String) get(STATUS_TEXT);
    }

    final void setStatusText(String text) {
        put(STATUS_TEXT, text);
    }

    public Plugin getInstance() {
        return instance;
    }

    public void setInstance(Plugin instance) {
        this.instance = instance;
        if (instance instanceof Plugin2) {
            optionGroup = ((Plugin2) instance).getOptionGroup();
            Action action = ((Plugin2) instance).getPluginAction();
            if (action != null) {
                String command = (String) action.getValue(Action.ACTION_COMMAND_KEY);
                if (command == null) {
                    command = (String) action.getValue(Action.NAME);
                }
                if (command == null) {
                    command = action.getClass().getName();
                }
                actionsMap.put(command, action);
            }
        }
    }

    public HashMap<String, Action> getActionsMap() {
        return actionsMap;
    }

    public int getBuildRequired() {
        return buildRequired;
    }

    public void setBuildRequired(int buildRequired) {
        this.buildRequired = buildRequired;
    }

    public Action getAction(String key) {
        if (key == null) {
            // return default from old implementation
            if (actionsMap.size() > 0)
                return actionsMap.values().iterator().next();
        }
        return actionsMap.get(key);
    }

    public OptionGroup getOptionGroup() {
        return optionGroup;
    }

    public ConfigurationCommand getConfigurationCommand() {
        return configurationCommand;
    }

    void setConfigurationCommand(ConfigurationCommand configurationCommand) {
        this.configurationCommand = configurationCommand;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public String getLocalizedString(String value) {
        if (value == null)
            return null;
        if (resourceBundle == null)
            return value;
        String key = value.trim();
        if (key.startsWith("${") && key.endsWith("}")) {
            key = key.substring(2, key.length() - 1);
            ResourceBundle bundle = getResourceBundle();
            return bundle.getString(key);
        }
        return value;
    }

}
