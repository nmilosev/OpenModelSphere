package org.modelsphere.jack.plugins;

import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.net.URL;

import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.plugins.io.PluginContext;
import org.modelsphere.jack.srtool.features.layout.LayoutPlugin;
import org.modelsphere.jack.srtool.forward.Rule;

public interface PluginDescriptor extends Comparable<PluginDescriptor> {
    public static final String KUnknownAuthor = LocaleMgr.misc.getString("UnknownAuthor");
    public static final String kDisabled = LocaleMgr.misc.getString("disabled");

    public static enum PLUGIN_TYPE {
        PLUGIN(Plugin.class), PLUGIN2(Plugin2.class), RULE(Rule.class), LAYOUT(LayoutPlugin.class);
        private Class<? extends Plugin> type;

        PLUGIN_TYPE(Class<? extends Plugin> type) {
            this.type = type;
        }

        public Class<? extends Plugin> getType() {
            return type;
        }

        public static PLUGIN_TYPE toType(String typeName) {
            if (typeName == null)
                return null;
            PLUGIN_TYPE[] types = PLUGIN_TYPE.values();
            for (int i = 0; i < types.length; i++) {
                if (types[i].toString().equals(typeName)) {
                    return types[i];
                }
            }
            return null;
        }

        public static PLUGIN_TYPE getType(Class<? extends Object> pluginClass) {
            if (pluginClass == null)
                return null;
            if (Rule.class.isAssignableFrom(pluginClass)) {
                return RULE;
            }
            if (LayoutPlugin.class.isAssignableFrom(pluginClass)) {
                return LAYOUT;
            }
            if (Plugin2.class.isAssignableFrom(pluginClass)) {
                return PLUGIN2;
            }
            if (Plugin.class.isAssignableFrom(pluginClass)) {
                return PLUGIN;
            }
            return null;
        }

        public String toString() {
            return type.getName();
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public boolean isEnabled();

    public void setEnabled(boolean enabled);

    public String getClassName();

    public PluginContext getContext();

    public Class<? extends Plugin> getPluginClass();

    public String getAuthor();

    public String getName();

    public String getAuthorURL();

    public String getDescription();

    public String getVersion();

    public String getDate();

    public String getAuthorEmail();

    public Image getImage();

    public PLUGIN_TYPE getType();

    public String getStatusFormattedText();

    public String getStatusText();

    public URL getLicenseURL();

    public void setVersion(String version);
}
