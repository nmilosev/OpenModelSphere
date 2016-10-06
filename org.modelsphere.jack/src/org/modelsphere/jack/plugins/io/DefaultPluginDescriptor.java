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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.net.URL;
import java.util.StringTokenizer;

import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.plugins.Plugin;
import org.modelsphere.jack.plugins.PluginConstants;
import org.modelsphere.jack.plugins.PluginDescriptor;

final class DefaultPluginDescriptor implements PluginDescriptor, PluginConstants {
    private static final Image DEFAULT_IMAGE;

    static {
        URL url = LocaleMgr.class.getResource("resources/plugin_32.png");
        DEFAULT_IMAGE = ImageUtilities.loadImage(url);
    }

    private Class<? extends Plugin> pluginClass;
    private String className;
    private PluginContext context;
    private PLUGIN_TYPE pluginType;

    private String name;
    private String author;
    private String description;
    private String date;
    private String authorEmail;
    private String authorURL;
    private String version;
    private Image image;
    private URL licenseURL;

    private boolean enabled;

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    DefaultPluginDescriptor(String pluginClassName) {
        if (pluginClassName == null) {
            throw new IllegalArgumentException("DefaultPluginDescriptor:  Null class not allowed.");
        }
        this.className = pluginClassName;
        enabled = true;
    }

    void setPluginClass(Class<? extends Plugin> pluginClass) {
        if (pluginClass == null) {
            return;
        }
        if (this.pluginClass != null) {
            throw new IllegalStateException("DefaultPluginDescriptor:  Class already specified.");
        }
        if (!pluginClass.getName().equals(this.className)) {
            throw new IllegalArgumentException(
                    "DefaultPluginDescriptor:  Class does not match with class name.");
        }
        if (!Plugin.class.isAssignableFrom(pluginClass)) {
            throw new IllegalArgumentException(
                    "DefaultPluginDescriptor:  Non Plugin class not allowed.");
        }
        if (this.pluginType != null && !this.pluginType.getType().isAssignableFrom(pluginClass)) {
            throw new IllegalStateException(
                    "DefaultPluginDescriptor:  Type does not match the specified class.");
        }
        this.pluginClass = pluginClass;
        if (this.pluginType == null)
            this.pluginType = PLUGIN_TYPE.getType(pluginClass);
    }

    public Class<? extends Plugin> getPluginClass() {
        return pluginClass;
    }

    public URL getLicenseURL() {
        return licenseURL;
    }

    void setLicenseURL(URL licenseURL) {
        this.licenseURL = licenseURL;
    }

    public PLUGIN_TYPE getType() {
        return pluginType;
    }

    void setPluginType(PLUGIN_TYPE pluginType) {
        if (this.pluginType == pluginType)
            return;
        if (this.pluginClass != null && pluginType != null) {
            if (!pluginType.getType().isAssignableFrom(pluginClass)) {
                throw new IllegalStateException(
                        "DefaultPluginDescriptor:  Type does not match the specified class.");
            }
        }
        this.pluginType = pluginType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled)
            return;
        this.enabled = enabled;
        propertyChangeSupport.firePropertyChange("enabled", !this.enabled, this.enabled);
    }

    void setContext(PluginContext context) {
        this.context = context;
    }

    public String getClassName() {
        return className;
    }

    public boolean equals(Object o) {
        if (!(o instanceof DefaultPluginDescriptor))
            return false;
        return className.equals(((DefaultPluginDescriptor) o).className);
    }

    @Override
    public int hashCode() {
        return className.hashCode() + 1000;
    }

    public int compareTo(PluginDescriptor that) {
        String nameThis = getName();
        String nameThat = that.getName();
        if (nameThis == null) {
            nameThis = className;
        }
        if (nameThat == null) {
            nameThat = that.getClassName();
        }
        int result = nameThis.compareTo(nameThat);
        return result;
    }

    public String getName() {
        if (name != null) {
            return name;
        }
        return className;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        if (author != null) {
            return author;
        }
        return "Unspecified";
    }

    void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorURL() {
        return authorURL;
    }

    void setAuthorURL(String authorURL) {
        this.authorURL = authorURL;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getDescription() {
        if (description != null) {
            return description;
        }
        return "";
    }

    void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        if (date != null) {
            return date;
        }
        return "";
    }

    void setDate(String date) {
        this.date = date;
    }

    public String getVersion() {
        if (version != null) {
            return version;
        }
        return "Unspecified";
    }

    public void setVersion(String version) {
        if (this.version != null && version != null && version.equals(this.version))
            return;
        String oldversion = this.version;
        this.version = version;
        propertyChangeSupport.firePropertyChange("version", oldversion, version);
    }

    public Image getImage() {
        if (image == null)
            return DEFAULT_IMAGE;
        return image;
    }

    void setImage(Image image) {
        this.image = image;
    }

    public final void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public final void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public String getStatusText() {
        String temp = "";
        temp += getName();
        if (!enabled) {
            temp += " " + kDisabled;
        }
        temp += " ("; // NOT LOCALIZABLE

        String version = getVersion();
        if (version != null) {
            temp += version;
        }

        String date = getDate();
        if (date != null && date.length() > 0) {
            if (version != null)
                temp += " - "; // NOT LOCALIZABLE
            temp += date;
        }

        temp += "), "; // NOT LOCALIZABLE
        String author = getAuthor();
        if (author == null || author.trim().length() == 0) {
            temp += KUnknownAuthor;
        } else {
            temp += author;
        }

        temp += ", " + className; // NOT LOCALIZABLE - HTML Tag

        URL url = context.getURL();
        if (url != null) {
            temp += "["; // NOT LOCALIZABLE 
            temp += new File(url.getFile()).getPath();
            temp += "], "; // NOT LOCALIZABLE
        }

        String status = context.getStatusText();
        if (status != null) {
            temp += ":  " + status; // NOT LOCALIZABLE - HTML Tag
        }
        return temp;
    }

    public String getStatusFormattedText() {
        String temp = "";

        if (enabled)
            temp += "<b>"; // NOT LOCALIZABLE - HTML Tag
        temp += "<font color=\"#000080\">"; // NOT LOCALIZABLE - HTML Tag
        temp += getName();
        temp += "<font color=\"#000000\">"; // NOT LOCALIZABLE - HTML Tag
        if (enabled) {
            temp += "</b>"; // NOT LOCALIZABLE - HTML Tag
        } else {
            temp += " - " + kDisabled;
        }
        temp += " ("; // NOT LOCALIZABLE

        String version = getVersion();
        if (version != null) {
            temp += version;
        }

        String date = getDate();
        if (date != null && date.length() > 0) {
            if (version != null)
                temp += " - "; // NOT LOCALIZABLE
            temp += date;
        }

        temp += ")"; // NOT LOCALIZABLE
        temp += "<br>&nbsp; &nbsp; &nbsp; <b>"; // NOT LOCALIZABLE - HTML Tag

        String author = getAuthor();
        if (author == null || author.trim().length() == 0) {
            temp += KUnknownAuthor;
        } else {
            temp += author;
        }
        temp += "</b>"; // NOT LOCALIZABLE - HTML Tag

        temp += "<br>&nbsp; &nbsp; &nbsp; <i>"; // NOT LOCALIZABLE - HTML Tag
        temp += className;
        temp += "</i>"; // NOT LOCALIZABLE

        URL url = context.getURL();
        if (url != null) {
            temp += "<br>&nbsp; &nbsp; &nbsp; <i>["; // NOT LOCALIZABLE - HTML Tag
            temp += new File(url.getFile()).getPath();
            temp += "]</i>"; // NOT LOCALIZABLE
        }

        String status = context.getStatusText();
        if (status != null) {
            temp += "<br><b>&nbsp; &nbsp; &nbsp; <font color=\"AA0000\">" + status + "</font></b>";// NOT LOCALIZABLE - HTML Tag
        }
        return temp;
    }

    public final String toString() {
        String sign = "";
        sign += getName();
        sign += " ("; // NOT LOCALIZABLE

        if (getVersion() != null) {
            String v = "";
            StringTokenizer st = new StringTokenizer(getVersion(), "$", false); // NOT LOCALIZABLE - Source Safe Tag
            while (st.hasMoreTokens()) {
                v += st.nextToken();
            }
            sign += v;
            sign += " - "; // NOT LOCALIZABLE
        }

        if (getDate() != null)
            sign += getDate();
        else {
            String d = "";
            StringTokenizer st = new StringTokenizer(getDate(), "$", false); // NOT LOCALIZABLE - Source Safe Tag
            while (st.hasMoreTokens()) {
                d += st.nextToken();
            }
            sign += d;
        }

        sign += "), "; // NOT LOCALIZABLE
        sign += ((getAuthor() == null || getAuthor().trim().length() == 0) ? PluginDescriptor.KUnknownAuthor
                : getAuthor());

        return sign;
    }

    public PluginContext getContext() {
        return context;
    }

}