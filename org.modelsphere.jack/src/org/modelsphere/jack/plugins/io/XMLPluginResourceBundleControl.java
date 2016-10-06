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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

class XMLPluginResourceBundleControl extends ResourceBundle.Control {
    private File home;
    private String baseName;

    XMLPluginResourceBundleControl(File home, String baseName) {
        this.home = home;
        this.baseName = baseName;
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
        String bundleName = toBundleName(this.baseName, locale);
        String resourceName = toResourceName(bundleName, "properties");
        ResourceBundle bundle = null;
        FileReader reader = null;
        File file = new File(home, resourceName);
        reader = new FileReader(file);
        bundle = new PropertyResourceBundle(reader);
        reader.close();
        return bundle;
    }

}
