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

package org.modelsphere.jack.international;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.plugins.PluginsConfiguration;
import org.modelsphere.jack.util.StringUtil;

/**
 * Get locale-dependent resources (such as strings, images, etc.) from the resource files
 * (.properties). This class is the superclass of all Localemgr classes in different international
 * packages (sms.oo.international.LocaleMgr, sms.or.international.LocaleMgr, etc.).
 * 
 * At the creation time, build a parent chain of resources. For instance, if
 * sms.or.international.LocaleMgr is a subclass of sms.international.LocaleMgr, which is at its turn
 * a subclass of jack.international.LocaleMgr, then a chain of all the
 * sms.or.international.LocaleMgr's parents is build at its creation time.
 * 
 * When localeMgr.getString(key) is called, the value associated with the key is searched into the
 * current LocaleMgr (sms.or.international.LocaleMgr for instance); if it is not found, the key is
 * then searched into its direct parent, and so on until jack.international.LocaleMgr. Ultimately,
 * if it is not found even in this base class, then the key itself is returned.
 * 
 * Here is an example of LocaleMgr's invocation, assuming locale = FRENCH: String keyword =
 * sms.or.international.LocaleMgr.screen.getString("Cancel"); if "Cancel=Annuler" found in
 * sms.or.international.ScreenResources_fr.properties, then return "Annuler" else if
 * "Cancel=Annuler" found in sms.international.ScreenResources_fr.properties, then return "Annuler"
 * else if "Cancel=Annuler" found in jack.international.ScreenResources_fr.properties, then return
 * "Annuler" else return "Cancel"
 */
public class LocaleMgr {

    public static final LocaleMgr action = new LocaleMgr(
            "org.modelsphere.jack.international.ActionResources"); //NOT LOCALIZABLE, class name
    public static final LocaleMgr message = new LocaleMgr(
            "org.modelsphere.jack.international.MessageResources"); //NOT LOCALIZABLE, class name
    public static final LocaleMgr misc = new LocaleMgr(
            "org.modelsphere.jack.international.MiscResources"); //NOT LOCALIZABLE, class name
    public static final LocaleMgr screen = new LocaleMgr(
            "org.modelsphere.jack.international.ScreenResources"); //NOT LOCALIZABLE, class name

    /** Tokens for multi-strings in properties files */
    public static final int STR_TOKEN = 0; // index 0 is the string itself
    public static final int MNC_TOKEN = 1; // index 1 is the beginning of the tokens defined in <kTags>
    public static final int ACL_TOKEN = 2;
    public static final int IMA_TOKEN = 3;
    public static final int URL_TOKEN = 4;
    public static final int TIP_TOKEN = 5;

    private static final String[] kTags = new String[] { "Mnc", "Acl", "Ima", "Url", "Tip" }; //NOT LOCALIZABLE
    private static final String kBeginOfTag = "[>"; //NOT LOCALIZABLE
    private static final String kEndOfTag = "<]"; //NOT LOCALIZABLE

    private ResourceBundle resBundle;

    private String m_resName;

    private String getResName() {
        return m_resName;
    }

    private ArrayList m_parentChain = new ArrayList();

    protected LocaleMgr(String resName) {
        m_resName = resName;
        buildParentChain(this.getClass(), resName); //fill m_parentChain
        initLocale();
        //resBundle = findResourceBundle(resName);
    }

    private void initLocale() {
        Locale locale = getLocaleFromPreferences(Locale.getDefault());


        // locale = Locale.ENGLISH;  //REMOVE THIS LINE FOR THE INTERNATIONAL VERSION
        Locale.setDefault(locale);
    } //end initLocale()

    private ResourceBundle findResourceBundle(String resName) {
        ResourceBundle bundle = null;
        Locale locale = getLocaleFromPreferences(Locale.getDefault());
        
        // WE MUST use this object class loader because plugins resources may not be in the application class path, thus unknown to the
        // default system class loader.
        ClassLoader loader = getClass().getClassLoader();

        String msg = null;
        try {
            bundle = ResourceBundle.getBundle(resName, locale, loader);
        } catch (MissingResourceException e) {
            bundle = null;
            msg = e.getMessage();
        }

        if (bundle == null) {
            if (msg != null)
                Debug.trace(msg);

            Locale.setDefault(Locale.ENGLISH);
            bundle = ResourceBundle.getBundle(resName, Locale.ENGLISH, getClass().getClassLoader());
        } //end if

        return bundle;
    } //end findResourceBundle()

    // Builds the parent chain of this resource (fills m_parentChain)
    // If an item is not found in a resource file, then look in the resource's parent
    private void buildParentChain(Class currClass, String resName) {
        try {
            m_parentChain.add(resName);
            currClass = currClass.getSuperclass();
            while (currClass != Object.class) { //while LocaleMgr has still a superclass
                int idx1 = resName.lastIndexOf('.');
                int idx2 = resName.lastIndexOf("Resources"); //NOT LOCALIZABLE, resource name
                char ch = Character.toLowerCase(resName.charAt(idx1 + 1));
                String basename = ch + resName.substring(idx1 + 2, idx2);
                Field field = currClass.getField(basename);
                Object obj = field.get(null); //try to get the static variable
                if (obj == null) { //returns null when LocaleMgr is being loading and it's setting its static fields
                    break;
                } else {
                    LocaleMgr mgr = (LocaleMgr) obj;
                    String parentResName = mgr.getResName();
                    m_parentChain.add(parentResName);
                    currClass = currClass.getSuperclass(); //get its superclass
                } //end if
            } //end while
        } catch (Exception ex) {
        } //end try
    } //end getParentChain()

    //
    // GET AND SET DEFAULT LOCALE
    //
    private static Locale g_locale = null;
    private static String g_header = "#Specifies in which locale the application starts up."; //NOT LOCALIZABLE, language independant

    private static File getLocalePropertyFile() {
        //get the "locale.properties" file
        String folderPath = System.getProperty("user.dir"); //NOT LOCALIZABLE, property
        String filename = "locale.properties"; //NOT LOCALIZABLE, independant from current language
        File file = new File(folderPath, filename);
        return file;
    } //getLocalePropertyFile

    //get locale stored in "locale.properties" file
    //returns null if either the file is not found or Locale entry does not exist in the file
    public static void setLocaleInPreferences(Locale locale) throws IOException {
        //get the "locale.properties" file
        File file = getLocalePropertyFile();

        FileWriter fileWriter = new FileWriter(file);
        PrintWriter writer = new PrintWriter(fileWriter); //throws FileNotFoundException

        //write locale line
        String localeLine = "Locale=" + locale.toString(); //NOT LOCALIZABLE
        writer.println(g_header);
        writer.println(localeLine);
        writer.close();

        //"modelsphere.plugins" contains language-dependant information
        PluginsConfiguration pluginsConfiguration = PluginsConfiguration.getSingleton();
        File plFile = pluginsConfiguration.getFile();
        if (plFile.exists()) {
            plFile.delete();
        }
    } //end setLocaleInPreferences()

    //get locale stored in "locale.properties" file
    //returns null if either the file is not found or Locale entry does not exist in the file
    public static Locale getLocaleFromPreferences(Locale defaultLocale) {
        return getLocaleFromPreferences(defaultLocale, false); 
    }
    
    public static Locale getLocaleFromPreferences(Locale defaultLocale, boolean forceOpeningPropertyFile) {
        Locale locale = getLocaleFromPreferences(forceOpeningPropertyFile);
        
        if (locale == null) {
            locale = defaultLocale;
        }
        
        return locale;
    }

    private static Locale getLocaleFromPreferences(boolean forceOpeningPropertyFile) {
        if (!forceOpeningPropertyFile) {
            //if locale has already been defined, return it
            if (g_locale != null) {
                return g_locale;
            }
        } //end if

        //get the "locale.properties" file
        File file = getLocalePropertyFile();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file)); //throws FileNotFoundException
            try {
                g_header = reader.readLine(); //keep the header, and skip it
                String line = reader.readLine(); //read the "Locale=en" line
                if (line == null) {
                    //create "locale.properties" with current locale
                    writeLocalePropertiesFile(file);
                } else {
                    g_locale = getLocaleFromLine(line); //analyze the line
                } //end if
            } catch (IOException ex) {
                //cannot read "locale.properties" : just return null
            } //end try

        } catch (FileNotFoundException ex) { // "locale.properties" does not exist yet

            //create "locale.properties" with current locale
            writeLocalePropertiesFile(file);

        } //end try

        return g_locale; //can be null
    } //end getLocaleFromPreferences()

    //create "locale.properties" with current locale
    private static void writeLocalePropertiesFile(File file) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            g_locale = Locale.getDefault();
            String header = "#Specifies in which locale the application starts up."; //NOT LOCALIZABLE, language-independant
            writer.println(header);
            String line = "Locale=" + g_locale.toString(); //NOT LOCALIZABLE, property
            writer.println(line);
            writer.close();

        } catch (IOException ex2) {
            //unable to create "locale.properties" : do nothing and return null
        } //end try
    }

    //read the "Locale=en" line
    private static Locale getLocaleFromLine(String line) {
        Locale locale = null;

        int idx = line.indexOf('=');
        String value = line.substring(idx + 1);

        Locale[] locales = Locale.getAvailableLocales();
        for (int i = 0; i < locales.length; i++) {
            Locale currLocale = locales[i];
            String s = currLocale.toString();
            if (value.equals(s)) {
                locale = currLocale;
                break;
            } //end if
        } //end for

        return locale;
    } //end getLocaleFromLine()

    public final String getString(String key) {
        String res = getRes(key);
        int i = res.indexOf(kBeginOfTag);
        String str = (i == -1 ? res : res.substring(0, i));
        if (Locale.getDefault().equals(Locale.UK)) {
            str = StringUtil.getUKEquivalent(str);
        }

        return str;
    } //end getString()

    public final String[] getTokens(String key) {
        String[] tokens = new String[kTags.length + 1];
        String res = getRes(key);
        int iToken = 0;
        while (true) {
            int i = res.indexOf(kBeginOfTag);
            if (i == -1) {
                tokens[iToken] = res;
                break;
            }
            tokens[iToken] = res.substring(0, i);
            res = res.substring(i + kBeginOfTag.length());
            i = res.indexOf(kEndOfTag);
            String tag = res.substring(0, i);
            res = res.substring(i + kEndOfTag.length());
            int nb = kTags.length;
            i = 0;
            for (int j = 0; j < nb; j++) {
                if (tag.equals(kTags[j])) {
                    i = j;
                    break;
                }
            } //end for

            //for (int j = 0;  ! tag.equals(kTags[j]);  j++)
            //   ;
            iToken = i + 1;
        }
        return tokens;
    }

    public final void initAbstractButton(AbstractButton button, String key) {
        String[] tokens = getTokens(key);
        button.setText(tokens[STR_TOKEN]);
        if (tokens[MNC_TOKEN] != null)
            button.setMnemonic(tokens[MNC_TOKEN].charAt(0));
        if (tokens[IMA_TOKEN] != null) {
            URL url = getClass().getResource(tokens[IMA_TOKEN]);
            if (url != null)
                button.setIcon(new ImageIcon(url));
        }
    }

    public final char getMnemonic(String key) {
        String[] tokens = getTokens(key);
        return (tokens[MNC_TOKEN] == null ? (char) 0 : tokens[MNC_TOKEN].charAt(0));
    }

    public final char getAccelerator(String key) {
        String[] tokens = getTokens(key);
        return (tokens[ACL_TOKEN] == null ? (char) 0 : tokens[ACL_TOKEN].charAt(0));
    }

    public final ImageIcon getImageIcon(String key) {
        ImageIcon imageIcon = null;
        String[] tokens = getTokens(key);
        if (tokens[IMA_TOKEN] != null) {
            URL url = getClass().getResource(tokens[IMA_TOKEN]);
            if (url != null)
                imageIcon = new ImageIcon(url);
        }
        return imageIcon;
    }

    public final Image getImage(String key) {
        Image image = null;
        String[] tokens = getTokens(key);
        if (tokens[IMA_TOKEN] != null) {
            URL url = getClass().getResource(tokens[IMA_TOKEN]);
            if (url != null)
                image = Toolkit.getDefaultToolkit().getImage(url);
        }
        return image;
    }

    public final URL getUrl(String key) {
        String[] tokens = getTokens(key);
        return (tokens[URL_TOKEN] == null ? null : getClass().getResource(tokens[URL_TOKEN]));
    }

    public final String getToolTip(String key) {
        String[] tokens = getTokens(key);
        return tokens[TIP_TOKEN];
    }

    private String getRes(String key) {
        String res = null;
        String msg = null;
        Iterator iter = m_parentChain.iterator();
        while (iter.hasNext()) { //iterates in the parent chain
            String resName = (String) iter.next();
            ResourceBundle bundle = findResourceBundle(resName);
            try {
                res = bundle.getString(key);
                msg = null; //erases previous error message
                res = getResourceEquivalent(resName, res);
            } catch (MissingResourceException e) {
                res = null;
                msg = e.getMessage();
            } //end try

            if (res != null) //if res found, exits the loop
                break;
        } //end while

        if (msg != null) {
            Debug.trace(msg);
        }

        return (res == null) ? key : res;
    } //end getRes()

    private static final String DB_RESOURCES = "DbResources"; //NOT LOCALIZABLE, resource name
    private static final String MISC_RESOURCES = "MiscResources"; //NOT LOCALIZABLE, resource name

    private String getResourceEquivalent(String resName, String res) {
        if (g_equivalenceMapping == null) {
            return res;
        } else if (resName.endsWith(DB_RESOURCES) || resName.endsWith(MISC_RESOURCES)) {
            res = getResourceEquivalent(res);
        }

        return res;
    } //end getResourceEquivalent()

    public static String getResourceEquivalent(String res) {
        if (g_equivalenceMapping == null) {
            return res;
        } else {
            String value = (String) g_equivalenceMapping.get(res);
            if (value == null) {
                value = res;
            }
            return value;
        } //end if
    } //end getResourceEquivalent()

    private static HashMap g_equivalenceMapping = null;

    private static void setEquivalenceMapping(HashMap mapping) {
        g_equivalenceMapping = mapping;
    }

} //end LocaleMgr
