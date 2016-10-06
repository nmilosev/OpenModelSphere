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

package org.modelsphere.jack.plugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.modelsphere.jack.io.PathFile;
import org.modelsphere.jack.srtool.ApplicationContext;

/**
 *
 */
public class PluginsActivator {
    private static final String NOPLUGINS = "-noplugins"; // NOT LOCALIZABLE, argument

    private PluginsActivator() {
    }

    // Called by SafeMode.perform
    public static boolean isPluginsEnabled() {
        boolean noplugins = false;
        File file = new File(System.getProperty("user.dir"), ApplicationContext.MODELSPHERE_ARGS);
        if (file.exists()) {
            ArrayList<String> list = ApplicationContext.getArgList(file);
            noplugins = list.contains(NOPLUGINS);
        }

        return !noplugins;
    }

    // Called by SafeMode.perform
    public static void setPluginsEnabled(boolean value) {
        // Rename 'modelsphere.args' to 'modelsphere.args~', if any
        String userDir = System.getProperty("user.dir");
        File file = new File(userDir, ApplicationContext.MODELSPHERE_ARGS);
        File renamedFile = new File(userDir, ApplicationContext.MODELSPHERE_ARGS
                + PathFile.BACKUP_EXTENSION);
        try {
            if (!file.exists()) {
                boolean created = file.createNewFile();
                renamedFile = file;
                if (created == false) {
                    // TODO: output exception
                }
            } else {
                if (renamedFile.exists()) {
                    renamedFile.delete();
                }
                file.renameTo(renamedFile);
            }

            // add -noplugins in modelsphere.args
            ArrayList<String> list = ApplicationContext.getArgList(renamedFile);
            if (!value) { // add -noplugins in the argument list, if not
                // already there
                if (!list.contains(NOPLUGINS)) {
                    list.add(NOPLUGINS);
                }
            } else { // remove -noplugins in the argument list, if any
                if (list.contains(NOPLUGINS)) {
                    list.remove(NOPLUGINS);
                }
            }

            ApplicationContext.setArgList(file, list);
        } catch (IOException ex) {
            // TODO: output exception
        }
    }

}
