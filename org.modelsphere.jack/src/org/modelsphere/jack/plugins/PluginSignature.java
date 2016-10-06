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

package org.modelsphere.jack.plugins;

import java.util.Date;
import java.util.StringTokenizer;

/**
 * 
 * <p>
 * This class defines the elements of a plugin signature. A PluginSignature object must be provided
 * for a pluggin to be loaded.
 * 
 * 
 * @see Plugin
 * 
 * @version 1 10/15/2000
 * @author Gino Pelletier
 * 
 * @deprecated
 */

public class PluginSignature {
    private String name;
    private String version;
    private String author;
    private String sDate;
    private Date dDate;
    private int buildRequired;
    private boolean m_hide = false;
    private String description;
    private String authorURI;
    private String authorEmail;
    private String imageResource;

    PluginSignature() {
    }

    /**
     * <p>
     * Creates a PluginSignature.
     * 
     * @param name
     *            the name of the plugin. Cannot be null.
     * @param version
     *            the version of the plugin. Cannot be null. Plugin developers are free to manage
     *            versions as they wish.
     * @param date
     *            the creation date of the plugin. Cannot be null.
     * @param buildrequired
     *            the minimum application build id required to execute the plugin. PluginSignature
     *            providing a 'buildrequired > application build id' will result not be loaded by
     *            the application. Refer to the About dialog to obtain the application build id.
     * 
     */
    public PluginSignature(String name, String version, String date, int buildrequired) {
        if (name == null || version == null || date == null)
            throw new NullPointerException("Invalid PluginSignature"); // NOT
        // LOCALIZABLE
        // Exception
        if (name.trim().length() == 0)
            throw new IllegalArgumentException("Invalid Plugin Name"); // NOT
        // LOCALIZABLE
        // Exception

        this.name = name;
        this.version = version;
        this.sDate = date;
        this.buildRequired = buildrequired;
    }

    /**
     * <p>
     * Creates a PluginSignature.
     * 
     * @param name
     *            the name of the plugin. Cannot be null.
     * @param version
     *            the version of the plugin. Cannot be null. Plugin developers are free to manage
     *            versions as they wish.
     * @param date
     *            a Date object representing the creation date of the plugin. Cannot be null.
     * @param buildrequired
     *            the minimum application build id required to execute the plugin. PluginSignature
     *            providing a 'buildrequired > application build id' will result not be loaded by
     *            the application. Refer to the About dialog to obtain the application build id.
     * 
     */
    public PluginSignature(String name, String version, Date date, int buildrequired) {
        if (name == null || version == null || date == null)
            throw new NullPointerException("Invalid PluginSignature"); // NOT
        // LOCALIZABLE
        // Exception
        if (name.trim().length() == 0)
            throw new IllegalArgumentException("Invalid Plugin Name"); // NOT
        // LOCALIZABLE
        // Exception

        this.name = name;
        this.version = version;
        this.dDate = date;
        this.buildRequired = buildrequired;
    }

    public PluginSignature(String name, String version, String author, String date,
            int buildrequired) {
        this(name, null, version, author, null, null, date, null, buildrequired);
    }

    /**
     * <p>
     * Creates a PluginSignature.
     * 
     * @param name
     *            the name of the plugin. Cannot be null.
     * @param version
     *            the version of the plugin. Cannot be null. Plugin developers are free to manage
     *            versions as they wish.
     * @param author
     *            the author of the plugin. Can be null.
     * @param date
     *            the creation date of the plugin. Cannot be null.
     * @param imageResource
     *            the resource name (relative to the plugin class) representing a 32x32 image.
     * @param buildrequired
     *            the minimum application build id required to execute the plugin. PluginSignature
     *            providing a 'buildrequired > application build id' will result not be loaded by
     *            the application. Refer to the About dialog to obtain the application build id.
     * 
     */
    public PluginSignature(String name, String description, String version, String author,
            String authorURI, String authorEmail, String date, String imageResource,
            int buildrequired) {
        if (name == null || version == null || date == null)
            throw new NullPointerException("Invalid PluginSignature"); // NOT
        // LOCALIZABLE
        // Exception
        if (name.trim().length() == 0)
            throw new IllegalArgumentException("Invalid Plugin Name"); // NOT
        // LOCALIZABLE
        // Exception

        this.name = name;
        this.version = version;
        this.sDate = date;
        this.buildRequired = buildrequired;
        this.author = author;
        this.description = description;
        this.authorEmail = authorEmail;
        this.authorURI = authorURI;
        this.imageResource = imageResource;
    }

    /**
     * <p>
     * Creates a PluginSignature.
     * 
     * @param name
     *            the name of the plugin. Cannot be null.
     * @param version
     *            the version of the plugin. Cannot be null. Plugin developers are free to manage
     *            versions as they wish.
     * @param author
     *            the author of the plugin. Can be null.
     * @param date
     *            the creation date of the plugin. Cannot be null.
     * @param imageResource
     *            the resource name (relative to the plugin class) representing a 32x32 image.
     * @param buildrequired
     *            the minimum application build id required to execute the plugin. PluginSignature
     *            providing a 'buildrequired > application build id' will result not be loaded by
     *            the application. Refer to the About dialog to obtain the application build id.
     * 
     */
    public PluginSignature(String name, String description, String version, String author,
            String authorURI, String authorEmail, Date date, String imageResource, int buildrequired) {
        if (name == null || version == null || date == null)
            throw new NullPointerException("Invalid PluginSignature"); // NOT
        // LOCALIZABLE
        // Exception
        if (name.trim().length() == 0)
            throw new IllegalArgumentException("Invalid Plugin Name"); // NOT
        // LOCALIZABLE
        // Exception

        this.name = name;
        this.version = version;
        this.dDate = date;
        this.buildRequired = buildrequired;
        this.author = author;
        this.description = description;
        this.authorEmail = authorEmail;
        this.authorURI = authorURI;
        this.imageResource = imageResource;
    }

    /**
     * <p>
     * Creates a PluginSignature.
     * 
     * @param name
     *            the name of the plugin. Cannot be null.
     * @param version
     *            the version of the plugin. Cannot be null. Plugin developers are free to manage
     *            versions as they wish.
     * @param author
     *            the author of the plugin. Can be null.
     * @param date
     *            a Date object representing the creation date of the plugin. Cannot be null.
     * @param buildrequired
     *            the minimum application build id required to execute the plugin. PluginSignature
     *            providing a 'buildrequired > application build id' will result not be loaded by
     *            the application. Refer to the About dialog to obtain the application build id.
     * 
     */
    public PluginSignature(String name, String version, String author, Date date, int buildrequired) {
        this(name, null, version, author, null, null, date, null, buildrequired);
    }

    public PluginSignature(String name, String version, String author, String date,
            int buildrequired, boolean hide) {
        this(name, version, author, date, buildrequired);
        m_hide = hide;
    }

    /**
     * <p>
     * Get the plugin name.
     * 
     * @return the name of the plugin.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Get the plugin author.
     * 
     * @return a String representing the author of the plugin.
     * 
     */
    public String getAuthor() {
        return author;
    }

    /**
     * <p>
     * Get the plugin version.
     * 
     * @return a String representing the version of the plugin.
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * <p>
     * Get the plugin creation date.
     * 
     * @return a String representing the creation date of the plugin.
     * 
     */
    public String getStringDate() {
        if (dDate != null)
            return dDate.toString();
        return sDate;
    }

    /**
     * <p>
     * Get the plugin creation date.
     * 
     * @return a Date representing the creation date of the plugin.
     * 
     */
    public Date getDate() {
        return dDate;
    }

    /**
     * <p>
     * Get the minimum application build id required by the plugin.
     * 
     * @return an int representing the minimum application build id required by the plugin.
     * 
     */
    public int getBuildRequired() {
        return buildRequired;
    }

    public boolean isHidden() {
        return m_hide;
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
            StringTokenizer st = new StringTokenizer(getStringDate(), "$", false); // NOT LOCALIZABLE - Source Safe Tag
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

    /**
     * <p>
     * Get the equivalent of toString() using HTML tags
     * 
     * @return the equivalent html String of toString().
     * 
     */
    public final String toHtml() {
        String sign = "";
        sign += "<b><font color=\"#000080\">" + getName() + "</b><font color=\"#000000\">"; // NOT LOCALIZABLE - HTML Tag
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
            StringTokenizer st = new StringTokenizer(getStringDate(), "$", false); // NOT LOCALIZABLE  - Source Safe Tag
            while (st.hasMoreTokens()) {
                d += st.nextToken();
            }
            sign += d;
        }

        sign += ")"; // NOT LOCALIZABLE
        sign += "<br>&nbsp; &nbsp; &nbsp; <b>"
                + ((getAuthor() == null || getAuthor().trim().length() == 0) ? PluginDescriptor.KUnknownAuthor
                        : getAuthor()) + "</b>"; // NOT LOCALIZABLE - HTML Tag

        return sign;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthorURI() {
        return authorURI;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public String getImage() {
        return imageResource;
    }

}
