package org.modelsphere.jack.plugins;

public final class PluginSecurityManager {
    private static PluginSecurityManager instance;

    private PluginSecurityManager() {
    }

    public static PluginSecurityManager getInstance() {
        if (instance == null) {
            instance = new PluginSecurityManager();
        }
        return instance;
    }

    public boolean verify(PluginDescriptor pluginDescriptor) {
        if (pluginDescriptor == null)
            return false;
        return true;
    }

}
