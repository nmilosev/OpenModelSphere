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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.*;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;

/**
 * This class provides a way to store properties in (key, value). Supported Types are int, String,
 * boolean and long
 * 
 * The standard format a property full key is: <The fully qualified class name to wich the property
 * applied> + key So the same String value can be used without conflict by different classes.
 */

public class PropertiesSet {
    private static final String kOptionLost = LocaleMgr.misc.getString("OptionsLost");
    static final String kNoAccess = LocaleMgr.misc.getString("NoAccess");

    private static final String COMMENTS = "//"; //NOT LOCALIZABLE
    private static final String SEPARATOR = "="; //NOT LOCALIZABLE
    private static final String EOL = "\r\n";//NOT LOCALIZABLE
    private static final String VERSION = "@version="; //NOT LOCALIZABLE

    private Hashtable properties;
    File file;
    private String header = LocaleMgr.misc.getString("Donoteditfilemanually");
    private int version = 0;

    private ArrayList prefixListeners = new ArrayList();

    private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    PropertiesSet(File f) {
        file = f;
        version = load();
    }

    void convertToVersion(int version, PropertiesConverter converter) {
        if (this.version != version) {
            if (converter != null)
                converter.convert(this, this.version);
            this.version = version;
        }
    }

    private int load() {
        properties = new Hashtable();
        int fileversion = 0;
        try {
            if (file.exists()) {
                FileReader inFile = new FileReader(file);
                BufferedReader reader = new BufferedReader(inFile);
                String line = reader.readLine();
                while (line != null) {
                    if (line.trim().indexOf(COMMENTS) != 0) {
                        if (line.trim().indexOf(VERSION) != 0)
                            loadProperty(line);
                        else {
                            line = line.substring(line.indexOf(VERSION) + VERSION.length(), line
                                    .length());
                            try {
                                fileversion = Integer.parseInt(line.trim());
                            } catch (Exception e2) {
                                Debug.trace("Error checking version in properties file: " + file);
                            }
                        }
                    }
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
        return fileversion;
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

    // Change the default header in the file
    void setHeader(String header) {
        this.header = header;
    }

    void save() {
        try {
            if (!file.exists())
                file.createNewFile();
            FileWriter outFile = new FileWriter(file);

            if (header != null && header.length() > 0) {
                outFile.write(COMMENTS + EOL);
                outFile.write(COMMENTS + "  " + header + EOL);
                outFile.write(COMMENTS + EOL);
                outFile.write(EOL);
            }

            // write version
            outFile.write(VERSION + version + EOL + EOL);

            HashSet keySet = new HashSet(properties.keySet());
            Object[] sortedkeys = keySet.toArray();
            Arrays.sort(sortedkeys);

            for (int i = 0; i < sortedkeys.length; i++) {
                String key = (String) sortedkeys[i];
                String value = (String) properties.get(key);
                if ((key != null) && (key != null))
                    outFile.write(key + SEPARATOR + value + EOL);
            }

            outFile.close();
        } catch (Exception e) {
            // This may occur on application exit
            // These files must be writable
            // ignore - may be a result of reset from options screen
            //      org.modelsphere.jack.util.ExceptionHandler.showBadInstallationMessage(
            //                ApplicationContext.getDefaultMainFrame(), e, kOptionLost);
        }
    }

    void delete() {
        try {
            if (file == null)
                return;
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.showBadInstallationMessage(
                    ApplicationContext.getDefaultMainFrame(), e, kNoAccess);
        }
    }

    boolean rename(File newfile) {
        try {
            if (newfile == null || newfile.exists())
                return false;
            if (file == null)
                return false;
            if (file.exists()) {
                return file.renameTo(newfile);
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * Set the Property for the specified key. If no value found, it will be added. If value already
     * exists, it will be replaced with the new value. The class fully qualified name will be added
     * in the key (before key value).
     */

    public void setProperty(Class c, String key, String value) {
        Object old = setProperty_(c, key, value);
        // If null, do not fire a property change, this is just a first call init
        if (old != null)
            firePropertyChange(getFullKey(c, key), old, value);
    }

    public void setProperty(String c, String key, String value) {
        Object old = setProperty_(c, key, value);
        // If null, do not fire a property change, this is just a first call init
        if (old != null)
            firePropertyChange(getFullKey(c, key), old, value);
    }

    public void setProperty(Class c, String key, int value) {
        Object old = setProperty_(c, key, new Integer(value).toString());
        // If null, do not fire a property change, this is just a first call init
        if (old != null)
            firePropertyChange(getFullKey(c, key), old, new Integer(value));
    }

    public void setProperty(Class c, String key, long value) {
        Object old = setProperty_(c, key, new Long(value).toString());
        // If null, do not fire a property change, this is just a first call init
        if (old != null)
            firePropertyChange(getFullKey(c, key), old, new Long(value));
    }

    public void setProperty(String c, String key, long value) {
        Object old = setProperty_(c, key, new Long(value).toString());
        // If null, do not fire a property change, this is just a first call init
        if (old != null)
            firePropertyChange(getFullKey(c, key), old, new Long(value));
    }

    public void setProperty(Class c, String key, float value) {
        Object old = setProperty_(c, key, new Float(value).toString());
        // If null, do not fire a property change, this is just a first call init
        if (old != null)
            firePropertyChange(getFullKey(c, key), old, new Float(value));
    }

    public void setProperty(String c, String key, float value) {
        Object old = setProperty_(c, key, new Float(value).toString());
        // If null, do not fire a property change, this is just a first call init
        if (old != null)
            firePropertyChange(getFullKey(c, key), old, new Float(value));
    }

    public void setProperty(Class c, String key, boolean value) {
        Object old = setProperty_(c, key, new Boolean(value).toString());
        // If null, do not fire a property change, this is just a first call init
        if (old != null)
            firePropertyChange(getFullKey(c, key), old, new Boolean(value));
    }

    public void setProperty(String c, String key, boolean value) {
        Object old = setProperty_(c, key, new Boolean(value).toString());
        // If null, do not fire a property change, this is just a first call init
        if (old != null)
            firePropertyChange(getFullKey(c, key), old, new Boolean(value));
    }

    private Object setProperty_(Class c, String key, String value) {
        Object old = null;
        if (isValidKey(c, key)) {
            String fullkey = getFullKey(c, key);
            old = properties.put(fullkey, value);
        } else
            org.modelsphere.jack.debug.Debug.assert2(false,
                    "PropertiesSet:  Trying to set an invalid key.  Key = " + key);
        return old;
    }

    private Object setProperty_(String c, String key, String value) {
        Object old = null;
        if (isValidKey(c, key)) {
            String fullkey = getFullKey(c, key);
            old = properties.put(fullkey, value);
        } else
            org.modelsphere.jack.debug.Debug.assert2(false,
                    "PropertiesSet:  Trying to set an invalid key.  Key = " + key);
        return old;
    }

    /**
     * Get the String value represented by the specified key. If no value found, store the provided
     * defaultValue and return it.
     */
    public String getPropertyString(Class c, String key, String defaultValue) {
        String value = (String) properties.get(getFullKey(c, key));
        if (value != null)
            return value;
        if (defaultValue != null) {
            setProperty(c, key, defaultValue);
        }
        return defaultValue;
    }

    public String getPropertyString(String c, String key, String defaultValue) {
        String value = (String) properties.get(getFullKey(c, key));
        if (value != null)
            return value;
        if (defaultValue != null) {
            setProperty(c, key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * Get the Integer value represented by the specified key. If no value found, store the provided
     * defaultValue and return it.
     */
    public Integer getPropertyInteger(Class c, String key, Integer defaultValue) {
        String value = (String) properties.get(getFullKey(c, key));
        Integer valueInt = defaultValue;
        if (value != null) {
            try {
                valueInt = new Integer(value);
                return valueInt;
            } catch (NumberFormatException e) {
            }
        }
        if (defaultValue != null) {
            setProperty(c, key, defaultValue.intValue());
        }
        return valueInt;
    }

    public Integer getPropertyInteger(String c, String key, Integer defaultValue) {
        String value = (String) properties.get(getFullKey(c, key));
        Integer valueInt = defaultValue;
        if (value != null) {
            try {
                valueInt = new Integer(value);
                return valueInt;
            } catch (NumberFormatException e) {
            }
        }
        if (defaultValue != null) {
            setProperty(c, key, defaultValue.intValue());
        }
        return valueInt;
    }

    /**
     * Get the Boolean value represented by the specified key. If no value found, store the provided
     * defaultValue and return it.
     */
    public Boolean getPropertyBoolean(Class c, String key, Boolean defaultValue) {
        String value = (String) properties.get(getFullKey(c, key));
        Boolean valueBoolean = defaultValue;
        if (value != null) {
            boolean bValue = value.equalsIgnoreCase("true"); //NOT LOCALIZABLE
            if (!bValue) {
                bValue = value.equalsIgnoreCase(Boolean.TRUE.toString());
            }
            valueBoolean = new Boolean(bValue);
            return valueBoolean;
        }
        if (defaultValue != null) {
            setProperty(c, key, defaultValue.booleanValue());
        }
        return valueBoolean;
    }

    public Boolean getPropertyBoolean(String c, String key, Boolean defaultValue) {
        String value = (String) properties.get(getFullKey(c, key));
        Boolean valueBoolean = defaultValue;
        if (value != null) {
            boolean bValue = value.equalsIgnoreCase("true"); //NOT LOCALIZABLE
            if (!bValue) {
                bValue = value.equalsIgnoreCase(Boolean.TRUE.toString());
            }
            valueBoolean = new Boolean(bValue);
            return valueBoolean;
        }
        if (defaultValue != null) {
            setProperty(c, key, defaultValue.booleanValue());
        }
        return valueBoolean;
    }

    /**
     * Get the Long value represented by the specified key. If no value found, store the provided
     * defaultValue and return it.
     */
    public Long getPropertyLong(Class c, String key, Long defaultValue) {
        String value = (String) properties.get(getFullKey(c, key));
        Long valueLong = defaultValue;
        if (value != null) {
            try {
                valueLong = new Long(value);
                return valueLong;
            } catch (NumberFormatException e) {
            }
        }
        if (defaultValue != null) {
            setProperty(c, key, defaultValue.longValue());
        }
        return valueLong;
    }

    public Long getPropertyLong(String c, String key, Long defaultValue) {
        String value = (String) properties.get(getFullKey(c, key));
        Long valueLong = defaultValue;
        if (value != null) {
            try {
                valueLong = new Long(value);
                return valueLong;
            } catch (NumberFormatException e) {
            }
        }
        if (defaultValue != null) {
            setProperty(c, key, defaultValue.longValue());
        }
        return valueLong;
    }

    /**
     * Get the Float value represented by the specified key. If no value found, store the provided
     * defaultValue and return it.
     */
    public Float getPropertyFloat(Class c, String key, Float defaultValue) {
        String value = (String) properties.get(getFullKey(c, key));
        Float valueFloat = defaultValue;
        if (value != null) {
            try {
                valueFloat = new Float(value);
                return valueFloat;
            } catch (NumberFormatException e) {
            }
        }
        if (defaultValue != null) {
            setProperty(c, key, defaultValue.floatValue());
        }
        return valueFloat;
    }

    public Float getPropertyFloat(String c, String key, Float defaultValue) {
        String value = (String) properties.get(getFullKey(c, key));
        Float valueFloat = defaultValue;
        if (value != null) {
            try {
                valueFloat = new Float(value);
                return valueFloat;
            } catch (NumberFormatException e) {
            }
        }
        if (defaultValue != null) {
            setProperty(c, key, defaultValue.floatValue());
        }
        return valueFloat;
    }

    String getProperty(String c, String key) {
        return (String) properties.get(getFullKey(c, key));
    }

    String getProperty(Class c, String key) {
        return (String) properties.get(getFullKey(c, key));
    }

    /**
     * Remove the specified key from the properties table Use this method if you want to remove a
     * key that is not use anymore.
     */
    public void removeProperty(Class c, String key) {
        properties.remove(getFullKey(c, key));
    }

    /**
     * Remove the specified key from the properties table Use this method if you want to remove a
     * key that is not use anymore.
     */
    public void removeProperty(String classfullname, String key) {
        String fullname = getFullKey(classfullname, key);
        properties.remove(fullname);
    }

    /**
     * Rename a key in this PropertiesSet. For maintenance purposes.
     */
    public void renameKey(Class oldKeyClass, String oldKey, Class newKeyClass, String newKey) {
        Object value = properties.remove(getFullKey(oldKeyClass, oldKey));
        if (value != null) {
            Debug.trace(file.getName() + ":  Converting key \"" + oldKey + "\" to \""
                    + getFullKey(newKeyClass, newKey) + "\""); // NOT LOCALIZABLE
            properties.put(getFullKey(newKeyClass, newKey), value);
        }
    }

    /**
     * Use this method if you want to test if a property corresponding to the specified key exists.
     */
    public boolean containsKey(Class c, String key) {
        return properties.containsKey(getFullKey(c, key));
    }

    /**
     * Use this method for validating a key before setting it. If running in debug, the key value is
     * tested before adding it to the table.
     */
    public static final boolean isValidKey(Class c, String key) {
        String result = new String(key);
        return (result.indexOf(SEPARATOR) == -1) && (result.trim().indexOf(COMMENTS) != 0);
    }

    public static final boolean isValidKey(String fullClassName, String key) {
        String result = new String(key);
        return (result.indexOf(SEPARATOR) == -1) && (result.trim().indexOf(COMMENTS) != 0);
    }

    private static final boolean isValidKey(String key) {
        String result = new String(key);
        return (result.indexOf(SEPARATOR) == -1) && (result.trim().indexOf(COMMENTS) != 0);
    }

    private static final String EMPTY_STRING = "";
    private static final String DOT_STRING = ".";

    private static final String getFullKey(Class c, String key) {
        return (c == null ? EMPTY_STRING : c.getName() + DOT_STRING) + key;
    }

    private static final String getFullKey(String c, String key) {
        return (c == null ? EMPTY_STRING : c + DOT_STRING) + key;
    }

    public final void addPropertyChangeListener(Class c, String property, PropertyChangeListener l) {
        if (!isValidKey(c, property))
            return;
        listeners.addPropertyChangeListener(getFullKey(c, property), l);
    }

    public final void addPropertyChangeListener(String c, String property, PropertyChangeListener l) {
        if (!isValidKey(c, property))
            return;
        listeners.addPropertyChangeListener(getFullKey(c, property), l);
    }

    public final void addPropertyChangeListener(PropertyChangeListener l) {
        listeners.addPropertyChangeListener(l);
    }

    public final void addPrefixPropertyChangeListener(Class c, String startWidth,
            PropertyChangeListener l) {
        if (!isValidKey(c, startWidth))
            return;
        String key = getFullKey(c, startWidth);
        listeners.addPropertyChangeListener(key, l);
        if (!prefixListeners.contains(key))
            prefixListeners.add(key);
    }

    public final void removePropertyChangeListener(Class c, String property,
            PropertyChangeListener l) {
        if (!isValidKey(c, property))
            return;
        listeners.removePropertyChangeListener(getFullKey(c, property), l);
    }

    public final void removePropertyChangeListener(String c, String property,
            PropertyChangeListener l) {
        if (!isValidKey(c, property))
            return;
        listeners.removePropertyChangeListener(getFullKey(c, property), l);
    }

    public final void removePrefixPropertyChangeListener(Class c, String startWidth,
            PropertyChangeListener l) {
        if (!isValidKey(c, startWidth))
            return;
        listeners.removePropertyChangeListener(getFullKey(c, startWidth), l);
    }

    public final void removePropertyChangeListener(PropertyChangeListener l) {
        listeners.removePropertyChangeListener(l);
    }

    protected final void firePropertyChange(String property, Object oldValue, Object newValue) {
        listeners.firePropertyChange(property, oldValue, newValue);
        // fire listeners with property starting with prefixProperties
        Iterator iter = prefixListeners.iterator();
        while (iter.hasNext()) {
            String prefix = (String) iter.next();
            if (!property.startsWith(prefix))
                continue;
            listeners.firePropertyChange(prefix, oldValue, newValue);
        }
    }

}
