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
import java.io.IOException;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.plugins.PluginDescriptor;
import org.modelsphere.jack.util.FileUtils;

public class InstallCommand extends ConfigurationCommand {
    private File source;

    public InstallCommand(File source) {
        super(ConfigurationCommandFactory.ACTION_INSTALL);
        this.source = source;
    }

    public InstallCommand() {
        super(ConfigurationCommandFactory.ACTION_INSTALL);
    }

    @Override
    PluginDescriptor execute(PluginDescriptor pluginDescriptor) {
        if (source == null || !source.exists())
            return null;

        File destination = XMLPluginUtilities.getHomeDirectory(pluginDescriptor);

        boolean ok = true;
        if (destination.exists()) {
            try {
                ok = FileUtils.deleteTree(destination, true);
            } catch (IOException e) {
                ok = false;
                Debug.trace(e);
            }
        }
        if (ok) {
            boolean renamed = source.renameTo(destination);
            if (!renamed) {
                System.out.println("InstallCommand: Failed to rename source " + source);
            }
        } else {
            System.out.println("InstallCommand: Failed to install " + source);
        }
        return pluginDescriptor;
    }

    @Override
    String getValues() {
        if (source == null) {
            return "";
        }
        return source.getPath();
    }

    @Override
    void setValues(String values) {
        if (values == null || values.trim().length() == 0) {
            source = null;
        } else {
            source = new File(values);
        }
    }
}
