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

package org.modelsphere.jack.srtool;

import java.awt.Image;
import java.io.*;
import java.util.*;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.debug.Log;
import org.modelsphere.jack.io.PathFile;
import org.modelsphere.jack.plugins.io.PluginLoader;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.features.SrDragDrop;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.popupMenu.ApplicationPopupMenu;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.util.ExceptionHandler;

public abstract class ApplicationContext {
    // property keys for look&feel
    // NOTE: Should not be moved to DefaultMainFrame - The DefaultMainFrame class must be loaded after setting l&f
    public static final String LF_PROPERTY = "LF"; // NOT LOCALIZABLE - property key
    public static final String THEME_PROPERTY = "Theme"; // NOT LOCALIZABLE - property key

    public static final String START_NO_SPLASH_PROPERTY = "nosplash"; // NOT LOCALIZABLE - property key
    public static final String START_OPEN_FILE_PROPERTY = "open"; // NOT LOCALIZABLE - property key

    public static final String PROPERTIES_FOLDER = "properties"; // NOT LOCALIZABLE - FOLDER
    private static final String LOG_FOLDER = "log"; // NOT LOCALIZABLE - FOLDER
    public static final String DEFAULT_WORKING_DIRECTORY = "DefaultWorkingDirectory"; // NOT LOCALIZABLE - property key

    public static final String APPL_LOCALE = "ApplicationLocale"; // NOT LOCALIZABLE
    public static final String APPL_LOCALE_DEFAULT = Locale.getDefault().toString(); // NOT LOCALIZABLE
    public static final String MODELSPHERE_ARGS = "modelsphere.args"; //NOT LOCALIZABLE, filename

    public static final String MODEL_FILE_EXTENSION = "Sms"; //NOT LOCALIZABLE

    // Used to identify JVM Version
    public static final int JVM_UNSUPORTED_RELEASE = -1;
    public static final int JVM_BASE_RELEASE = 1;
    public static final int JVM_NEXT_RELEASE = 2;
    private static int JVM_VERSION = 0;

    // This field should be initialized early during the initialisation of the application.
    public static Image APPLICATION_IMAGE_ICON = null;
    public static int APPLICATION_BUILD_ID = -1;
    public static String APPLICATION_BUILD_ID_EXTENSION = "";
    public static final String APPLICATION_AUTHOR = LocaleMgr.misc.getString("Vendor"); // NOT LOCALIZABLE - property key
    public static final String APPLICATION_AUTHOR_CONTACT = LocaleMgr.misc
            .getString("VendorContact"); // NOT LOCALIZABLE - property key
    public static final String APPLICATION_AUTHOR_URI = LocaleMgr.misc.getString("VendorURI"); // NOT LOCALIZABLE - property key
    //public  static String               SUITE_NAME              = null;    //Used for directory structure in user.home
    //  public  static String               PRODUCT_NAME            = LocaleMgr.misc.getString("Vendor");  //Used for directory structure in user.home

    // Controlled access to application name
    private static String APPLICATION_NAME = null;
    public static String getApplicationName() {
    	if (APPLICATION_NAME == null) {
    		ScreenPerspective.handleApplicationHasNoName();
    	}
    	
    	return APPLICATION_NAME; 
    }
	public static void setApplicationName(String applName) {
		APPLICATION_NAME = applName;
	}
	
	// Controlled access to application version
	private static String APPLICATION_VERSION = null;
	public static String getApplicationVersion() {
		return APPLICATION_VERSION;
	}
	public static void setApplicationVersion(String version) {
		APPLICATION_VERSION = version;
	}
    
    public static String REPOSITORY_ROOT_NAME = "Root"; // NOT LOCALIZABLE

    private static DefaultMainFrame mf;
    private static FocusManager fm;
    private static AbstractActionsStore actionStore;
    private static ApplicationPopupMenu applPopupMenu;
    private static SrDragDrop dragDrop;
    private static Db dbRepos;
    private static Properties commandLineProperties;

    private static SemanticalModel semanticalModel;

    public static DefaultMainFrame getDefaultMainFrame() {
        return mf;
    }

    public static void setDefaultMainFrame(DefaultMainFrame mf) {
        ApplicationContext.mf = mf;
    }

    public static FocusManager getFocusManager() {
        return fm;
    }
    
    private static Properties COMMAND_LINE_PROPERTIES = null;
    public static Properties getCommandLineProperties() {
        return COMMAND_LINE_PROPERTIES;
    }
    private static void setCommandLineProperties(Properties properties) {
        COMMAND_LINE_PROPERTIES = properties; 
    }

    /**
     * Used to identify JVM Version. JVM_NEXT_RELEASE may offer additionnal support. This specific
     * VM check should not be used for general purposes.
     * 
     * @return JVM_UNSUPORTED_RELEASE, JVM_BASE_RELEASE or JVM_NEXT_RELEASE
     */
    public static int getJavaVersion() {
        if (JVM_VERSION != 0)
            return JVM_VERSION;
        String version = System.getProperty("java.version"); // NOT LOCALIZABLE
        if (version == null || version.length() == 0)
            JVM_VERSION = JVM_BASE_RELEASE;
        else if (version.indexOf("1.0") == 0) // NOT LOCALIZABLE
            JVM_VERSION = JVM_UNSUPORTED_RELEASE;
        else if (version.indexOf("1.1") == 0) // NOT LOCALIZABLE
            JVM_VERSION = JVM_UNSUPORTED_RELEASE;
        else if (version.indexOf("1.2") == 0) // NOT LOCALIZABLE
            JVM_VERSION = JVM_UNSUPORTED_RELEASE;
        else if (version.indexOf("1.3") == 0) // NOT LOCALIZABLE
            JVM_VERSION = JVM_UNSUPORTED_RELEASE;
        else if (version.indexOf("1.4") == 0) // NOT LOCALIZABLE
            JVM_VERSION = JVM_UNSUPORTED_RELEASE;
        else if (version.indexOf("1.5") == 0) // NOT LOCALIZABLE
            JVM_VERSION = JVM_UNSUPORTED_RELEASE;
        else if (version.indexOf("1.6") == 0) // NOT LOCALIZABLE
            JVM_VERSION = JVM_BASE_RELEASE;
        else
            JVM_VERSION = JVM_NEXT_RELEASE;
        return JVM_VERSION;
    }

    public static void setFocusManager(FocusManager fm) {
        ApplicationContext.fm = fm;
    }

    public static AbstractActionsStore getActionStore() {
        return actionStore;
    }

    public static void setActionStore(AbstractActionsStore actionStore) {
        ApplicationContext.actionStore = actionStore;
    }

    public static ApplicationPopupMenu getApplPopupMenu() {
        return applPopupMenu;
    }

    public static void setApplPopupMenu(ApplicationPopupMenu applPopupMenu) {
        ApplicationContext.applPopupMenu = applPopupMenu;
    }

    public static SrDragDrop getDragDrop() {
        return dragDrop;
    }

    public static void setDragDrop(SrDragDrop dragDrop) {
        ApplicationContext.dragDrop = dragDrop;
    }

    public static Db getDbRepos() {
        return dbRepos;
    }

    public static void setDbRepos(Db dbRepos) {
        ApplicationContext.dbRepos = dbRepos;
    }

    public static SemanticalModel getSemanticalModel() {
        return semanticalModel;
    }

    public static void setSemanticalModel(SemanticalModel semanticalModel) {
        if (ApplicationContext.semanticalModel != null || semanticalModel == null)
            return;
        ApplicationContext.semanticalModel = semanticalModel;
    }

    public static String getApplicationDirectory() {
        return System.getProperty("user.dir");
    }

    public static String getUserHomeDirectory() {
        return getPathOf(null);
    }

    public static String getPropertiesFolderPath() {
        return getPathOf(PROPERTIES_FOLDER);
    }

    public static String getLogPath() {
        return getPathOf(LOG_FOLDER);
    }

    public static String getPathOf(String folder) {
        String SEPARATOR = System.getProperty("file.separator");
        String fixPath = System.getProperty("user.home");
        String path = "." + APPLICATION_NAME + (folder == null ? "" : SEPARATOR + folder); //NOT LOCALIZABLE, property key

        File dir = new File(fixPath + SEPARATOR + path);
        try {
            if (!dir.exists())
                dir = createPath(fixPath, path);

        } catch (IOException e) {
        }
        return dir.getPath();
    }

    //Create a path structure if they don't already exist.
    private static File createPath(String fixPath, String pathname) throws IOException {
        String separator = System.getProperty("file.separator");

        File file = new File(fixPath);
        //for each token
        StringTokenizer st = new StringTokenizer(pathname, separator);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();

            java.io.File dir = new File(file, token);
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdir();
            }
            file = dir;
        } //end while

        return file;
    } // createPath

    public static void processCommandLineArguments(String[] args) {
        args = readAdditionalArguments(args);
        Properties temp_properties = new Properties();
        if (args != null) {
            int last = args.length - 1;
            String pluginPath = "-" + PluginLoader.START_OPTION_PLUGINS_PATH; 
            
            //for each argument, starting from the last one (last one overrides the first one)
            for (int i = last; i >= 0; i--) {
                String param = args[i];
                boolean isPath = param.startsWith(pluginPath); 
                if (! isPath) {
                    param = param.toLowerCase();
                }
                
                if (param.startsWith("-") && param.length() > 1) { //NOT LOCALIZABLE
                    String key, value;
                    if (param.indexOf("=") != -1) { //NOT LOCALIZABLE
                        param = param.substring(param.indexOf("-") + 1, param.length()); //NOT LOCALIZABLE
                        key = param.substring(0, param.indexOf("=")); //NOT LOCALIZABLE
                        value = param.substring(param.indexOf("=") + 1); //NOT LOCALIZABLE
                    } else {
                        param = param.substring(param.indexOf("-") + 1, param.length()); //NOT LOCALIZABLE
                        key = param.substring(0, param.length());
                        value = "true"; //NOT LOCALIZABLE
                    }

                    //Do not redefine if it has already been defined
                    if (temp_properties.get(key) == null) {
                        temp_properties.setProperty(key, value);
                    }

                } else if (param.indexOf("." + MODEL_FILE_EXTENSION.toLowerCase()) != -1) { //file to open  //NOT LOCALIZABLE
                    if (temp_properties.get(START_OPEN_FILE_PROPERTY) == null) {
                        temp_properties.setProperty(START_OPEN_FILE_PROPERTY, param);
                    }
                } //end if

            } //end for
        } //end if

        setCommandLineProperties(temp_properties);
    } //end processCommandLineArguments()

    public static String getDefaultWorkingDirectory() {
        String defaultWorkingDir = null;
        DefaultMainFrame mainframe = getDefaultMainFrame();
        if (mainframe != null) {
            String appDir = mainframe.getApplicationDirectory();
            if (appDir != null) {
                PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
                if (preferences != null) {
                    defaultWorkingDir = preferences.getPropertyString(ApplicationContext.class,
                            DEFAULT_WORKING_DIRECTORY, appDir);
                }
            }
        } //end if

        return defaultWorkingDir;
    }

    public static String getApplicationLocal() {
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        return preferences.getPropertyString(ApplicationContext.class, APPL_LOCALE,
                APPL_LOCALE_DEFAULT);
    }

    //called by sms.features.SafeMode, jack.plugins.PluginsDialog
    public static ArrayList<String> getArgList(File file) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            do {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                StringTokenizer st = new StringTokenizer(line, ";");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    list.add(token);
                } //end while

            } while (true);
            reader.close();
        } catch (FileNotFoundException ex) {
            //this file is optional, so it is not an error if it does not exist
        } catch (IOException ex) {
            //ignore
        } //end try

        return list;
    } //end getArgList()

    //called by sms.features.SafeMode, jack.plugins.PluginsDialog
    public static void setArgList(File file, ArrayList list) throws IOException {
        File renamedFile = new File(file.getParent(), file.getName() + PathFile.BACKUP_EXTENSION);
        if (renamedFile.exists()) {
            renamedFile.delete();
        }
        file.renameTo(renamedFile);

        PrintWriter writer = new PrintWriter(new FileWriter(file));
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            String arg = (String) iter.next();
            writer.println(arg);
        } //end while
        writer.close();
    } //end setArgList()

    //Read 'modelsphere.args' and add additional arguments
    private static String[] readAdditionalArguments(String[] args) {
        File file = new File(System.getProperty("user.dir"), MODELSPHERE_ARGS); //NOT LOCALIZABLE, property
        ArrayList list = getArgList(file);

        //if modelsphere.args does not exist, or it's empty, then return the original args
        if (list.size() == 0) {
            return args;
        } //end if

        //return the original args plus those specified in modelsphere.args
        int nb = args.length + list.size();
        String[] newArgs = new String[nb];
        for (int i = 0; i < args.length; i++) {
            newArgs[i] = args[i];
        }
        for (int i = 0; i < list.size(); i++) {
            newArgs[args.length + i] = (String) list.get(i);
        }

        return newArgs;
    } //end private





}
