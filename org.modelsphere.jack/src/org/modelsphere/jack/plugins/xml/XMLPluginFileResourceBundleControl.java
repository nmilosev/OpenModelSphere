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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.modelsphere.jack.baseDb.international.LocaleMgr;
import org.modelsphere.jack.plugins.xml.fs.FsEntry;
import org.modelsphere.jack.plugins.xml.fs.FsFile;
import org.modelsphere.jack.plugins.xml.fs.FsFolder;

public class XMLPluginFileResourceBundleControl extends ResourceBundle.Control  {
	private FsFile m_pluginXML; 
	private String  m_basename; 
	
	public XMLPluginFileResourceBundleControl(FsFile pluginXML, String basename) {
		m_pluginXML = pluginXML;
		m_basename = basename;
	}

	//return an unique resource name
	public String getResourceName() {
		FsFolder folder = m_pluginXML.getParent(); 
		String path = folder.getPath();
		//Locale locale = LocaleMgr.getLocaleFromPreferences(true); 
		//String bundleName = toBundleName(m_basename, locale);
		
		String ressName = path + "_" + m_basename; //homeDirectory.getPath() + "_" + baseName; 
		return ressName;
	}
	
	@Override
    public String toBundleName(String baseName, Locale locale) {
        if (baseName != null && baseName.toLowerCase().endsWith(".properties")) {
            baseName = baseName.substring(0, baseName.length() - 11);
        }
        return super.toBundleName(baseName, locale);
    }
	
	@Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format,
            ClassLoader loader, boolean reload) throws IllegalAccessException,
            InstantiationException, IOException {
		
		String bundleName = toBundleName(m_basename, locale);
        String resourceName = toResourceName(bundleName, "properties");
		FsFolder folder = m_pluginXML.getParent(); 
		FsEntry elem = folder.getNamedDescendent(resourceName);
		
		ResourceBundle bundle;
		if (elem instanceof FsFile) {
			FsFile resFile = (FsFile)elem;
			InputStream input = resFile.openInputStream();
			bundle = new PropertyResourceBundle(input);		
			input.close();
		} else {
			bundle = null;
		}
        
		
		return bundle;
		
		/*
        
        ResourceBundle bundle = null;
        FileReader reader = null;
        File file = new File(home, resourceName);
        reader = new FileReader(file);
        bundle = new PropertyResourceBundle(reader);
        reader.close();
        return bundle;
        */
    }
	
	
	/*
	static InputStream openResource(FileRef pluginXmlFile, String baseName) {
		
		
    	
    	//String resourceName = toResourceName(bundleName, "properties");
		

    
    	
    	//folder.getNamedChild();
    	
    	//File ressourceFile = new File(homeDirectory, resourceName);
    	
    	
		InputStream input = null; //new FileInputStream(resourceFile);
		return input;
	}
	
    private String toBundleName(String baseName, Locale locale) {
        if (baseName != null && baseName.toLowerCase().endsWith(".properties")) {
            baseName = baseName.substring(0, baseName.length() - 11);
        }
        return super.toBundleName(baseName, locale);
    }
   */
}
