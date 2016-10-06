/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.plugins.report;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;

import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;

/*
 This class is inspired (copied) a lot from org.modelsphere.jack.preference.PropertiesSet
 Thank you Gino!

 I removed all Class parameters
 */

public class PropertiesSet {
    private static final String COMMENTS = "//"; //NOT LOCALIZABLE
    private static final String SEPARATOR = "="; //NOT LOCALIZABLE
    private static final String EOL = "\r\n";//NOT LOCALIZABLE

    private Hashtable properties;
    private File file;
    private boolean setFileEditable = false;

    public PropertiesSet(File f) {
        file = f;
        load();
    }

    private void load() {
        properties = new Hashtable();
        try {
            if (file.exists()) {
                FileInputStream inFile = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inFile));
                String line = reader.readLine();
                while (line != null) {
                    if (line.trim().indexOf(COMMENTS) != 0)
                        loadProperty(line);
                    line = reader.readLine();
                }
                reader.close();
                inFile.close();
            }
        } catch (Exception e) {
            // This may occur if bad access to folder
            // These files must be writable
            org.modelsphere.jack.util.ExceptionHandler.showBadInstallationMessage(
                    ApplicationContext.getDefaultMainFrame(), e, null);
        }
    }

    private void loadProperty(String line) {
        int separatorIdx = line.indexOf(SEPARATOR);
        if (separatorIdx > 0) { // valid key entry?
            String key = line.substring(0, separatorIdx);
            String value = line.substring(separatorIdx + SEPARATOR.length(), line.length());
            if (isValidKey(key))
                properties.put(key, value);
        }
    }

    // Use this method if you want to write or not the editable warning in the property file
    public void setFileEditable(boolean editableFile) {
        setFileEditable = editableFile;
    }

    public void save() {
        try {
            if (!file.exists())
                file.createNewFile();
            FileOutputStream outFile = new FileOutputStream(file);
            DataOutputStream outStream = new DataOutputStream(outFile);

            if (!setFileEditable) {
                outStream.writeBytes(COMMENTS + EOL);
                outStream.writeBytes(COMMENTS + "  "
                        + LocaleMgr.misc.getString("Donoteditfilemanually") + EOL);
                outStream.writeBytes(COMMENTS + EOL);
                outStream.writeBytes(EOL);
            }

            HashSet keySet = new HashSet(properties.keySet());
            Object[] sortedkeys = keySet.toArray();
            Arrays.sort(sortedkeys);

            for (int i = 0; i < sortedkeys.length; i++) {
                String key = (String) sortedkeys[i];
                String value = (String) properties.get(key);
                if ((key != null) && (key != null))
                    outStream.writeBytes(key + SEPARATOR + value + EOL);
            }

            outStream.close();
            outFile.close();
        } catch (Exception e) {
            // This may occur on application exit
            // These files must be writable
            org.modelsphere.jack.util.ExceptionHandler.showBadInstallationMessage(
                    ApplicationContext.getDefaultMainFrame(), e, LocaleMgr.misc
                            .getString("OptionsLost"));
        }
    }

    /**
     * Set the Property for the specified key. If no value found, it will be added. If value already
     * exists, it will be replaced with the new value. The class fully qualified name will be added
     * in the key (before key value).
     */

    public void setProperty(String key, String value) {
        if (isValidKey(key))
            properties.put(key, value);
        else
            org.modelsphere.jack.debug.Debug.assert2(false,
                    "PropertiesSet:  Trying to set an invalid key.  Key = " + key);
    }

    public void setProperty(String key, int value) {
        setProperty(key, new Integer(value).toString());
    }

    public void setProperty(String key, long value) {
        setProperty(key, new Long(value).toString());
    }

    public void setProperty(String key, boolean value) {
        setProperty(key, new Boolean(value).toString());
    }

    /**
     * Get the String value represented by the specified key. If no value found, store the provided
     * defaultValue and return it.
     */
    public String getPropertyString(String key, String defaultValue) {
        String value = (String) properties.get(key);
        if (value != null)
            return value;
        if (defaultValue != null) {
            setProperty(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * Get the Integer value represented by the specified key. If no value found, store the provided
     * defaultValue and return it.
     */
    public Integer getPropertyInteger(String key, Integer defaultValue) {
        String value = (String) properties.get(key);
        Integer valueInt = defaultValue;
        if (value != null) {
            try {
                valueInt = new Integer(value);
                return valueInt;
            } catch (NumberFormatException e) {
            }
        }
        if (defaultValue != null) {
            setProperty(key, defaultValue.intValue());
        }
        return valueInt;
    }

    /**
     * Get the Boolean value represented by the specified key. If no value found, store the provided
     * defaultValue and return it.
     */
    public Boolean getPropertyBoolean(String key, Boolean defaultValue) {
        String value = (String) properties.get(key);
        Boolean valueBoolean = defaultValue;
        if (value != null) {
            valueBoolean = new Boolean(value.equalsIgnoreCase("true")); //NOT LOCALIZABLE
            return valueBoolean;
        }
        if (defaultValue != null) {
            setProperty(key, defaultValue.booleanValue());
        }
        return valueBoolean;
    }

    /**
     * Get the Long value represented by the specified key. If no value found, store the provided
     * defaultValue and return it.
     */
    public Long getPropertyLong(String key, Long defaultValue) {
        String value = (String) properties.get(key);
        Long valueLong = defaultValue;
        if (value != null) {
            try {
                valueLong = new Long(value);
                return valueLong;
            } catch (NumberFormatException e) {
            }
        }
        if (defaultValue != null) {
            setProperty(key, defaultValue.longValue());
        }
        return valueLong;
    }

    /**
     * Remove the specified key from the properties table Use this method if you want to remove a
     * key that is not use anymore.
     */
    public void removeProperty(String key) {
        properties.remove(key);
    }

    /**
     * Use this method if you want to test if a property corresponding to the specified key exists.
     */
    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }

    /**
     * Use this method for validating a key before setting it. If running in debug, the key value is
     * tested before adding it to the table.
     */
    public static final boolean isValidKey(Class c, String key) {
        String result = new String(key);
        return (result.indexOf(' ') == -1) && (result.trim().indexOf(COMMENTS) != 0);
    }

    private static final boolean isValidKey(String key) {
        String result = new String(key);
        return (result.indexOf(' ') == -1) && (result.trim().indexOf(COMMENTS) != 0);
    }

    private static final String getFullKey(Class c, String key) {
        return c.getName() + "." + key;
    }
}
