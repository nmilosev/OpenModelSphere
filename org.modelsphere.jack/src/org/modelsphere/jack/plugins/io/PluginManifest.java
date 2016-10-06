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

import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class PluginManifest {
    private static final String MANIFEST_PLUGIN_CLASS_ATTR = "Implementation-Class";
    private static final String MANIFEST_PLUGIN_TITLE_ATTR = "Implementation-Title";
    private static final String MANIFEST_PLUGIN_VENDOR_ATTR = "Implementation-Vendor";
    private static final String MANIFEST_PLUGIN_VENDORURL_ATTR = "Implementation-URL";
    private static final String MANIFEST_PLUGIN_REQUIREDVERSION_ATTR = "Specification-Version";

    private String pluginClassName;
    private String pluginTitle;
    private String pluginVendor;
    private String pluginVendorURL;
    private String pluginRequiredVersion;

    private boolean checked = false;

    PluginManifest(Manifest manifest) {
        if (manifest == null) {
            return;
        }

        Attributes attributes = manifest.getMainAttributes();
        pluginClassName = attributes.getValue(MANIFEST_PLUGIN_CLASS_ATTR);
        if (pluginClassName != null && pluginClassName.trim().length() > 0) {
            checked = true;
        }

        pluginTitle = attributes.getValue(MANIFEST_PLUGIN_TITLE_ATTR);
        pluginVendor = attributes.getValue(MANIFEST_PLUGIN_VENDOR_ATTR);
        pluginVendorURL = attributes.getValue(MANIFEST_PLUGIN_VENDORURL_ATTR);
        pluginRequiredVersion = attributes.getValue(MANIFEST_PLUGIN_REQUIREDVERSION_ATTR);
    }

    public String getPluginClassName() {
        return pluginClassName;
    }

    void setPluginClassName(String pluginClassName) {
        this.pluginClassName = pluginClassName;
    }

    public String getPluginTitle() {
        return pluginTitle;
    }

    void setPluginTitle(String pluginTitle) {
        this.pluginTitle = pluginTitle;
    }

    public String getPluginVendor() {
        return pluginVendor;
    }

    void setPluginVendor(String pluginVendor) {
        this.pluginVendor = pluginVendor;
    }

    public String getPluginVendorURL() {
        return pluginVendorURL;
    }

    void setPluginVendorURL(String pluginVendorURL) {
        this.pluginVendorURL = pluginVendorURL;
    }

    public String getPluginRequiredVersion() {
        return pluginRequiredVersion;
    }

    void setPluginRequiredVersion(String pluginRequiredVersion) {
        this.pluginRequiredVersion = pluginRequiredVersion;
    }

    public boolean isChecked() {
        return checked;
    }
}
