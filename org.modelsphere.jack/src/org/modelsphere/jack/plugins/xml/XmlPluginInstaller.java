/*************************************************************************

Copyright (C) 2009 by neosapiens inc.

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

You can reach neosapiens inc. at: 

neosapiens inc.
1236 Gaudias-Petitclerc
Qu&eacute;bec, Qc, G1Y 3G2
CANADA
Telephone: 418-561-8403
Fax: 418-650-2375
http://www.neosapiens.com/
Email: marco.savard@neosapiens.com
       gino.pelletier@neosapiens.com

 **********************************************************************/
package org.modelsphere.jack.plugins.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.modelsphere.jack.plugins.Plugin;
import org.modelsphere.jack.plugins.PluginDescriptor;
import org.modelsphere.jack.plugins.PluginsConfiguration;
import org.modelsphere.jack.plugins.PluginsRegistry;
import org.modelsphere.jack.plugins.io.PluginContext;
import org.modelsphere.jack.plugins.io.PluginLoader;
import org.modelsphere.jack.plugins.io.XMLPluginContext;
import org.modelsphere.jack.plugins.io.XMLPluginUtilities;
import org.modelsphere.jack.plugins.xml.extensions.AbstractPluginExtension;
import org.modelsphere.jack.plugins.xml.extensions.PopupExtension;
import org.modelsphere.jack.plugins.xml.fs.FsEntry;
import org.modelsphere.jack.plugins.xml.fs.FsFile;
import org.modelsphere.jack.plugins.xml.fs.FsFolder;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.Splash;
import org.modelsphere.jack.srtool.popupMenu.ApplicationPopupMenu;

public class XmlPluginInstaller {

    private static XmlPluginInstaller g_singleInstance = null;
    public static XmlPluginInstaller getInstance() {
        if (g_singleInstance == null) {
            g_singleInstance = new XmlPluginInstaller();
        }
        return g_singleInstance; 
    }

    public void installSignatures(Splash splash, PluginsRegistry pluginsRegistry) {

        /*
	    try {
	        //URL url = new URL("http://kenai.com/downloads/org-modelsphere-sms/lib/"); 
	        URL url = new URL("http://www.modelsphere.org/SILVERRUN_Viewer/dist-oms-build-954/plugins/"); 
	        java.net.URLConnection con = url.openConnection();
	        con.connect();

	        InputStreamReader ir = new InputStreamReader(con.getInputStream());
	        BufferedReader br = new BufferedReader(ir);
	        String line;

	        for (; (line = br.readLine()) != null; ) {
	            System.out.println(line); 
	        }

	    } catch (MalformedURLException ex) {
	        ex.printStackTrace(); 
	    } catch (IOException ex) {
	        ex.printStackTrace(); 
	    }*/
        
        //read plugins.xml
        PluginsConfiguration pluginsConfiguration = PluginsConfiguration.getSingleton();
        List<PluginDescriptor> configuredPlugins = new ArrayList<PluginDescriptor>();
        pluginsConfiguration.load(configuredPlugins);

        //return plugin paths
        List<FsFolder> pluginPaths = getPluginPaths(pluginsRegistry);

        //get URIs of all plugin.xml files
        List<FsFile> pluginXmlList = new ArrayList<FsFile>();
        int searchDepth = 2;
        for (FsFolder path : pluginPaths) {
            scanPath(pluginXmlList, path, searchDepth);
        }

        //read plugin.xml files
        List<XmlPluginDescriptor> descriptorList = new ArrayList<XmlPluginDescriptor>();
        for (FsFile pluginXml : pluginXmlList) {	
            try {
                XmlPluginDescriptor descriptor = XmlPluginDescriptor.create(pluginXml);
                if (descriptor != null) {
                    if (! descriptorList.contains(descriptor)) {
                        descriptorList.add(descriptor);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } //end for

        //register plug-ins
        for (XmlPluginDescriptor pluginDescriptor : descriptorList) {
            PluginDescriptor configuredPlugin = findConfiguredPlugin(configuredPlugins, pluginDescriptor); 
            boolean enabled = (configuredPlugin == null) ? true : configuredPlugin.isEnabled(); 
            pluginDescriptor.setEnabled(enabled);
            pluginsRegistry.add(pluginDescriptor);
        }
    } //end find()

    private PluginDescriptor findConfiguredPlugin(List<PluginDescriptor> configuredPlugins,
            XmlPluginDescriptor pluginDescriptor) {
        PluginDescriptor foundConfiguredPlugin = null;
        
        for (PluginDescriptor configuredPlugin : configuredPlugins) {
            String classname = configuredPlugin.getClassName();
            if (pluginDescriptor.getClassName().equals(classname)) {
                foundConfiguredPlugin = configuredPlugin;
                break;
            }
        } //end for
        
        return foundConfiguredPlugin;
    }

    public void install(Splash splash, PluginsRegistry pluginsRegistry) {
        List<PluginDescriptor> descriptorList = pluginsRegistry.getValidPlugins();
        Map<Class<? extends Object>, PopupMap> popupmaps = new HashMap<Class<? extends Object>, PopupMap>();
        List<String> actionkeys = new ArrayList<String>();

        for (PluginDescriptor pluginDescriptor : descriptorList) {
            if (pluginDescriptor instanceof XmlPluginDescriptor) {
                XmlPluginDescriptor xmlDescriptor = (XmlPluginDescriptor)pluginDescriptor; 

                try {
                    Class<?> claz = xmlDescriptor.getPluginClass();
                    if (claz != null) {
                        //for each extension
                        List<AbstractPluginExtension> extensions = xmlDescriptor.getPluginExtensions();
                        
                        if (extensions == null) {
                            String actionKey = pluginDescriptor.getClassName();
                            actionkeys.add(actionKey);
                            //actionkeys.add(actionKey);
                            addInPopupMaps(popupmaps, xmlDescriptor, actionKey);
                        } else {
                            for (AbstractPluginExtension extension : extensions) {
                                String actionKey = extension.getActionKey();
                                actionkeys.add(actionKey);
                                //actionkeys.add(actionKey);
                                addInPopupMaps(popupmaps, xmlDescriptor, actionKey);
                            } //end for
                        } //end if
                    } //end if
                } catch (Exception ex) {
                    ex.printStackTrace();
                } //end try
            } //end if
        } //end for

        List<Object> popups = new ArrayList<Object>();
        for (Object o : popupmaps.keySet()) {
            PopupMap map = popupmaps.get(o);
            popups.add(map.clazz);

            Object[] actionkeysobj = map.keys.toArray();
            String[] actkeys = new String[actionkeysobj.length];
            for (int i = 0; i < actionkeysobj.length; i++) {
                actkeys[i] = (String) actionkeysobj[i];
            }
            popups.add(actkeys);
        }

        String[] actionkeysarray = new String[actionkeys.size()];
        for (int i = 0; i < actionkeys.size(); i++) {
            actionkeysarray[i] = (String) actionkeys.get(i);
        }

        ApplicationPopupMenu applpopup = ApplicationContext.getApplPopupMenu();
        applpopup.registerPopups(popups.toArray(), actionkeysarray);

        //install plug-ins
        XmlPluginActionFactory factory = XmlPluginActionFactory.getInstance();
        for (PluginDescriptor pluginDescriptor : descriptorList) {
            if (pluginDescriptor instanceof XmlPluginDescriptor) {
                factory.createPluginAction(splash, (XmlPluginDescriptor)pluginDescriptor);
            }
        }
    }

    private void addInPopupMaps(Map<Class<? extends Object>, PopupMap> popupmaps, XmlPluginDescriptor xmlDescriptor, String actionKey) {
        Class<?>[] supportedclasses = xmlDescriptor.getSupportedClasses(); 
        if (supportedclasses != null) {
            for (Class<?> supportedclass : supportedclasses) {
                PopupMap map = popupmaps.get(supportedclass);
                if (map == null) {
                    map = new PopupMap(supportedclass);
                    popupmaps.put(supportedclass, map);
                }
                map.add(actionKey);
            } //end for
        } //end if
    }

    //
    // private methods
    //

    private File uriToFile(URI uri) {
        String filename = uri.getPath();
        File file = new File(filename);
        return file;
    }


    private void scanPath(List<FsFile> plugInList, FsFolder path, int searchDepth) {

        if (searchDepth < 0) {
            return;
        }

        List<FsEntry> entries = path.getFolderElements(); 
        for (FsEntry entry : entries) {
            if (entry instanceof FsFolder) {
                FsFolder folder = (FsFolder)entry;
                String foldername = folder.getSimpleName();
                if (! "src".equals(foldername)) { //skip duplicates in src folder
                    scanPath(plugInList, folder, searchDepth-1);
                }
            } else if (entry instanceof FsFile) {
                FsFile fileRef = (FsFile)entry;
                String filename = fileRef.getSimpleName();
                if ("plugin.xml".equals(filename)) {
                    plugInList.add(fileRef);
                }
            }
        } //end for

        /*
		File files[] = path.listFiles(); 
		for (File file : files) {
			if (file.isDirectory()) {
				if (searchDepth > 0) {
					String foldername = file.getName();
					if (! "src".equals(foldername)) { //skip duplicates in src folder
						scanPath(uriList, file, searchDepth-1);
					}
				}
			} else {
				String filename = file.getName();
				if ("plugin.xml".equals(filename)) {
					URI uri = file.toURI();
					uriList.add(uri);
				} else {
					int idx = filename.lastIndexOf('.');
					String ext = (idx == -1) ? null : filename.substring(idx+1);
					if ("zip".equals(ext)) {
						scanPath(uriList, file, searchDepth-1);
					}
				}
			} //end if
		}//end for
         */
    } //end scanPath

    private List<FsFolder> getPluginPaths(PluginsRegistry pluginsRegistry) {
        List<FsFolder> pluginPaths = new ArrayList<FsFolder>();

        //add folder(s) specified by -pluginpath VM parameter
        Properties properties = ApplicationContext.getCommandLineProperties(); 
        String path = properties.getProperty(PluginLoader.START_OPTION_PLUGINS_PATH);
        
        if (path == null) {
        	path = getDefaultPath();
        }
        
        StringTokenizer st = new StringTokenizer(path, File.pathSeparator, false);

        while (st.hasMoreElements()) {
            String resource = (String) st.nextElement();
            FsFolder root = FsFolder.createRoot(resource);

            if (root != null) {
                pluginPaths.add(root); 
            }
        } //end while

        //add XML plug-ins
        List<PluginDescriptor> pluginsDescriptors = pluginsRegistry.getAllPlugins();
        for (PluginDescriptor pluginsDescriptor : pluginsDescriptors) {
            PluginContext context = pluginsDescriptor.getContext();
            if (context instanceof XMLPluginContext) {
                XMLPluginContext xmlpluginContext = (XMLPluginContext)context;
                File file = xmlpluginContext.getHomeDirectory(); 
                if (file.exists() && file.isDirectory()) {
                    FsFolder root = FsFolder.createRoot(file);
                    pluginPaths.add(root); 
                }
            }
        } //end for

        return pluginPaths;
    }

    private String getDefaultPath() {
    	String dir = System.getProperty("user.dir");
		File path = new File(dir, "plugins");
		
		if (! path.exists()) {
			path.mkdirs();
		}
		
		String defPath = path.toString();
		return defPath;
	}

	private static class PopupMap {
        Class<?> clazz;
        ArrayList<String> keys = new ArrayList<String>();

        PopupMap(Class<?> c) {
            this.clazz = c;
        }

        void add(String key) {
            keys.add(key);
        }
    }
}
