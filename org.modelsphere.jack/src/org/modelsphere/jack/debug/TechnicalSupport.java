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

package org.modelsphere.jack.debug;

import java.util.Properties;

import javax.swing.UIManager;

import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.jack.plugins.io.PluginLoader;
import org.modelsphere.jack.srtool.ApplicationContext;

public final class TechnicalSupport {
    private static final String TECH_SUPPORT_EMAIL = "???.com"; // NOT LOCALIZABLE - email
    private static final String TECH_SUPPORT_NAME = "Technical Support"; // NOT LOCALIZABLE - email

    // System property to disable double buffering in some screen (bug related
    // to some NVidia video cards)
    private static final String DOUBLE_BUFFERING_DISABLED = "nodoublebuffering"; // NOT LOCALIZABLE - property

    private TechnicalSupport() {
    }

    public static String getSupportInfo() {
        Properties props = ApplicationContext.getCommandLineProperties(); 
        String info = "\n"; // NOT LOCALIZABLE LINE SEPARATOR
        info += ApplicationContext.getApplicationName() + " " + ApplicationContext.getApplicationVersion() 
                + "\n"; // NOT LOCALIZABLE
        info += "build " + ApplicationContext.APPLICATION_BUILD_ID + "\n\n"; // NOT LOCALIZABLE

        info += "\nVirtual Machine Information: " + "\n"; // NOT LOCALIZABLE
        info += "java.vm.name = " + System.getProperty("java.vm.name") + "\n"; // NOT LOCALIZABLE
        info += "java.vm.version = " + System.getProperty("java.vm.version") + "\n"; // NOT LOCALIZABLE
        info += "java.vm.info = " + System.getProperty("java.vm.info") + "\n"; // NOT LOCALIZABLE
        info += "java.vm.vendor = " + System.getProperty("java.vm.vendor") + "\n"; // NOT LOCALIZABLE
        info += "java.vendor.url = " + System.getProperty("java.vendor.url") + "\n"; // NOT LOCALIZABLE
        info += "java.vendor.url.bug = " + System.getProperty("java.vendor.url.bug") + "\n"; // NOT LOCALIZABLE
        info += "java.runtime.name = " + System.getProperty("java.runtime.name") + "\n"; // NOT LOCALIZABLE
        info += "java.runtime.version = " + System.getProperty("java.runtime.version") + "\n"; // NOT LOCALIZABLE
        info += "java.specification.name = " + System.getProperty("java.specification.name") + "\n"; // NOT LOCALIZABLE
        info += "java.specification.version = " + System.getProperty("java.specification.version")
                + "\n"; // NOT LOCALIZABLE
        info += "java.specification.vendor = " + System.getProperty("java.specification.vendor")
                + "\n"; // NOT LOCALIZABLE
        info += "os.name = " + System.getProperty("os.name") + "\n"; // NOT LOCALIZABLE
        info += "os.arch = " + System.getProperty("os.arch") + "\n"; // NOT LOCALIZABLE
        info += "os.version = " + System.getProperty("os.version") + "\n"; // NOT LOCALIZABLE
        info += "user.name = " + System.getProperty("user.name") + "\n"; // NOT LOCALIZABLE
        info += "user.home = " + System.getProperty("user.home") + "\n"; // NOT LOCALIZABLE
        info += "user.dir = " + System.getProperty("user.dir") + "\n"; // NOT LOCALIZABLE
        info += "user.region = " + System.getProperty("user.region") + "\n"; // NOT LOCALIZABLE
        info += "user.language = " + System.getProperty("user.language") + "\n"; // NOT LOCALIZABLE
        info += "user.timezone = " + System.getProperty("user.timezone") + "\n"; // NOT LOCALIZABLE
        info += "\nawt.toolkit = " + System.getProperty("awt.toolkit") + "\n"; // NOT LOCALIZABLE
        info += "java.awt.graphicsenv = " + System.getProperty("java.awt.graphicsenv") + "\n"; // NOT LOCALIZABLE
        info += "sun.java2d.noddraw = " + System.getProperty("sun.java2d.noddraw") + "\n"; // NOT LOCALIZABLE
        info += "sun.java2d.ddlock = " + System.getProperty("sun.java2d.ddlock") + "\n"; // NOT LOCALIZABLE
        info += "suppressSwingDropSupport = " + System.getProperty("suppressSwingDropSupport")
                + "\n"; // NOT LOCALIZABLE
        info += "java.awt.printerjob = " + System.getProperty("java.awt.printerjob") + "\n"; // NOT LOCALIZABLE
        info += "java.io.tmpdir = " + System.getProperty("java.io.tmpdir") + "\n"; // NOT LOCALIZABLE
        info += "java.library.path = " + System.getProperty("java.library.path") + "\n"; // NOT LOCALIZABLE
        info += "java.ext.dirs = " + System.getProperty("java.ext.dirs") + "\n"; // NOT LOCALIZABLE
        String linesep = (String) System.getProperty("line.separator");
        char[] chars = linesep == null ? new char[] {} : linesep.toCharArray();
        String charsint = "";
        for (int i = 0; i < chars.length; i++) {
            charsint += new Integer((int) chars[i]).toString() + " ";
        }
        info += "line.separator = " + charsint + "\n"; // NOT LOCALIZABLE
        info += "path.separator = " + System.getProperty("path.separator") + "\n"; // NOT LOCALIZABLE
        info += "file.separator = " + System.getProperty("file.separator") + "\n"; // NOT LOCALIZABLE
        info += "file.encoding = " + System.getProperty("file.encoding") + "\n"; // NOT LOCALIZABLE
        info += "file.encoding.pkg = " + System.getProperty("file.encoding.pkg") + "\n"; // NOT LOCALIZABLE
        info += "sun.boot.library.path = " + System.getProperty("sun.boot.library.path") + "\n"; // NOT LOCALIZABLE
        info += "sun.boot.class.path = " + System.getProperty("sun.boot.class.path") + "\n"; // NOT LOCALIZABLE
        info += "sun.cpu.isalist = " + System.getProperty("sun.cpu.isalist") + "\n"; // NOT LOCALIZABLE
        info += "VM Total Memory = " + Runtime.getRuntime().totalMemory() + "\n"; // NOT LOCALIZABLE
        info += "VM Free Memory = " + Runtime.getRuntime().freeMemory() + "\n"; // NOT LOCALIZABLE
        info += "Look & Feel = "
                + (UIManager.getLookAndFeel() != null ? UIManager.getLookAndFeel().toString()
                        : null) + "\n"; // NOT LOCALIZABLE
        info += "java.class.version = " + System.getProperty("java.class.version") + "\n"; // NOT LOCALIZABLE
        info += "java.class.path = " + System.getProperty("java.class.path") + "\n"; // NOT LOCALIZABLE
        info += "\ngsj.systemname = " + System.getProperty("gsj.systemname") + "\n"; // NOT LOCALIZABLE
        info += "gns.host = " + System.getProperty("gns.host") + "\n"; // NOT LOCALIZABLE
        info += "\n"
                + PluginLoader.getOptionPluginsPath()
                + " = "
                + props.getProperty(PluginLoader.getOptionPluginsPath()) + "\n"; // NOT LOCALIZABLE
        info += "\n"
                + PluginMgr.START_OPTION_NO_PLUGINS
                + " = "
                + props.getProperty(PluginMgr.START_OPTION_NO_PLUGINS) + "\n\n"; // NOT LOCALIZABLE
        return info;
    }
}
