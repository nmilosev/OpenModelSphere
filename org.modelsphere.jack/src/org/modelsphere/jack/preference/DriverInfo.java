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

import java.io.File;

import org.modelsphere.jack.srtool.ApplicationContext;

public final class DriverInfo extends PropertiesSet {
    public static final String DRIVER_NAME = "name"; // NOT LOCALIZABLE
    public static final String DRIVER_JDBC = "driver"; // NOT LOCALIZABLE
    public static final String DRIVER_URL = "url"; // NOT LOCALIZABLE
    public static final String DRIVER_DESCRIPTION = "description"; // NOT LOCALIZABLE
    public static final String DRIVER_DEFAULT_USER = "user"; // NOT LOCALIZABLE
    public static final String DRIVER_DEFAULT_PASSWORD = "passwd"; // NOT LOCALIZABLE
    public static final String DRIVER_DATA_SOURCE = "datasource"; // NOT LOCALIZABLE

    public static final String DEFAULT_JDBC_VALUE = "sun.jdbc.odbc.JdbcOdbcDriver"; // NOT LOCALIZABLE - Class name

    private File binaryFile = null;

    DriverInfo(File file) {
        super(file);
    }

    void delete() {
        super.delete();
        try {
            if (binaryFile == null)
                return;
            if (binaryFile.exists()) {
                binaryFile.delete();
            }
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.showBadInstallationMessage(
                    ApplicationContext.getDefaultMainFrame(), e, kNoAccess);
        }
    }

}
