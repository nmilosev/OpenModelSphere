package org.modelsphere.jack.properties;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.modelsphere.jack.srtool.ApplicationContext;

public class PropertySet {
    private static Map<Class, PropertySet> propertySet = null;
    private Properties properties = new Properties();
    private Class claz;

    public static PropertySet getInstance(Class claz) {
        if (propertySet == null) {
            propertySet = new HashMap<Class, PropertySet>();
        }

        PropertySet properties = propertySet.get(claz);
        if (properties == null) {
            properties = new PropertySet(claz);
            propertySet.put(claz, properties);
        }

        return properties;
    }

    private PropertySet(Class claz) {
        try {
            this.claz = claz;
            File file = getPropertiesFile();

            if (!file.exists()) {
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                properties.store(out, "---No Comment---");
                out.close();

            } else {
                FileInputStream in = new FileInputStream(file);
                properties.load(in);
                in.close();
            }

        } catch (IOException ex) {
        }
    }

    private File getPropertiesFile() {
        File propertiesFolder = getPropertiesFolder();
        String filename = claz.getName() + ".properties";
        File file = new File(propertiesFolder, filename);
        return file;
    }

    private File getPropertiesFolder() {
        File folder = new File(ApplicationContext.getPropertiesFolderPath());
        if (!folder.exists()) {
            folder.mkdir();
        }

        return folder;
    }

    public String getProperty(String codeGeneration) {
        return getProperty(codeGeneration, null);
    }

    public String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            value = defaultValue;
            setProperty(key, value);
        }

        return value;
    }

    public void setProperty(String key, String value) {

        if (value != null) {
            properties.setProperty(key, value);
        }

        File file = getPropertiesFile();
        try {
            FileOutputStream out = new FileOutputStream(file);
            properties.store(out, "---No Comment---");
            out.close();
        } catch (IOException ex) {
        }
    }

}
