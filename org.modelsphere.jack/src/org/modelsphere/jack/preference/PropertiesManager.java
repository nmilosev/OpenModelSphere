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

package org.modelsphere.jack.preference;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.modelsphere.jack.srtool.ApplicationContext;

/**
 * This class manege all the properties of the application. Each PropertiesSet store its properties
 * in a different file. A propertiesSet can be retrieve using a key. Before using a propertiesSet,
 * you must install it using method installPropertiesSet.
 * 
 */

public class PropertiesManager {
    private static final String PROPERTIES_DIRECTORY = ApplicationContext.getPropertiesFolderPath();

    // Keys for identifying the default propertiesSets and file name for the
    // propertiesSet
    private static final String PROPERTIES_EXTENTION = ".properties"; // NOT LOCALIZABLE, property key
    private static final String APPLICATION_PROPERTIES = "application properties"; // NOT LOCALIZABLE, property key
    private static final String PREFERENCES_PROPERTIES = "preference properties"; // NOT LOCALIZABLE, property key
    private static final String PREFERENCES_PROPERTIES_FILE_NAME = "preference"
            + PROPERTIES_EXTENTION; // NOT LOCALIZABLE, property key
    private static final String PLUGINS_PROPERTIES = "plugins properties"; // NOT LOCALIZABLE, property key
    private static final String PLUGINS_PROPERTIES_FILE_NAME = "plugins" + PROPERTIES_EXTENTION; // NOT LOCALIZABLE, property key

    // Public, used by sms.features.SafeMode
    public static final String APPLICATION_PROPERTIES_FILE_NAME = "application"
            + PROPERTIES_EXTENTION; // NOT LOCALIZABLE, property key

    // Default PropertiesSet
    public static PropertiesSet APPLICATION_PROPERTIES_SET = null;
    public static PropertiesSet PLUGINS_PROPERTIES_SET = null;

    // Preference Properties
    private static PropertiesSet PREFERENCES_PROPERTIES_SET = null;

    public static PropertiesSet getPreferencePropertiesSet() {
        return PREFERENCES_PROPERTIES_SET;
    } // end getPreferencePropertiesSet()

    private static Hashtable propertiesSets = new Hashtable(10);

    private static ArrayList propertiesProviders = new ArrayList(2);

    private static PropertiesConverter propertiesConverter = null;
    private static int version = 0;

    public static void save() {
        for (int i = 0; i < propertiesProviders.size(); i++) {
            ((PropertiesProvider) propertiesProviders.get(i)).updateProperties();
        }

        Enumeration enumeration = propertiesSets.elements();
        while (enumeration.hasMoreElements()) {
            PropertiesSet set = (PropertiesSet) enumeration.nextElement();
            set.save();
        }

        DriversManager.saveDrivers();
    }

    public static void installDefaultPropertiesSet() {
        // install the property files
        // Application properties
        installPropertiesSet(APPLICATION_PROPERTIES, new File(PROPERTIES_DIRECTORY
                + System.getProperty("file.separator") + APPLICATION_PROPERTIES_FILE_NAME), false); // NOT LOCALIZABLE, property key
        // Preferences properties
        installPropertiesSet(PREFERENCES_PROPERTIES, new File(PROPERTIES_DIRECTORY
                + System.getProperty("file.separator") + PREFERENCES_PROPERTIES_FILE_NAME), false); // NOT LOCALIZABLE, property key
        // Plugins properties
        installPropertiesSet(PLUGINS_PROPERTIES, new File(PROPERTIES_DIRECTORY
                + System.getProperty("file.separator") + PLUGINS_PROPERTIES_FILE_NAME), false); // NOT LOCALIZABLE,
        // property key
        APPLICATION_PROPERTIES_SET = getPropertiesSet(APPLICATION_PROPERTIES);
        PREFERENCES_PROPERTIES_SET = getPropertiesSet(PREFERENCES_PROPERTIES);
        PLUGINS_PROPERTIES_SET = getPropertiesSet(PLUGINS_PROPERTIES);

        DriversManager.loadDrivers();

        // Convert default
        APPLICATION_PROPERTIES_SET.convertToVersion(version, propertiesConverter);
        PREFERENCES_PROPERTIES_SET.convertToVersion(version, propertiesConverter);
        PLUGINS_PROPERTIES_SET.convertToVersion(version, propertiesConverter);
    }

    public static void installPropertiesSet(String key, File file) {
        installPropertiesSet(key, file, true);
    }

    public static void installPropertiesSet(String key, String setName) {
        installPropertiesSet(key, new File(PROPERTIES_DIRECTORY
                + System.getProperty("file.separator") + setName + PROPERTIES_EXTENTION), true);
    }

    static void installPropertiesSet(String key, File file, boolean convert) {
        if ((key != null) && (file != null) && (propertiesSets.get(key) == null)) {
            PropertiesSet set = new PropertiesSet(file);
            propertiesSets.put(key, set);

            // Convert default sets with the version converter
            if (convert)
                set.convertToVersion(version, propertiesConverter);
        }
    }

    static void convert(PropertiesSet set) {
        if (set != null)
            set.convertToVersion(version, propertiesConverter);
    }

    public static PropertiesSet getPropertiesSet(String key) {
        return (PropertiesSet) propertiesSets.get(key);
    }

    /**
     * Register the PropertiesProvider to be notified before saving the file.
     */
    public static void registerPropertiesProvider(PropertiesProvider provider) {
        if ((provider != null) && (propertiesProviders.indexOf(provider) == -1))
            propertiesProviders.add(provider);
    }

    public static void unregisterPropertiesProvider(PropertiesProvider provider) {
        propertiesProviders.remove(provider);
    }

    public static void setPropertiesConverter(PropertiesConverter converter) {
        propertiesConverter = converter;
    }

    public static void setVersion(int version) {
        PropertiesManager.version = version;
    }
}
