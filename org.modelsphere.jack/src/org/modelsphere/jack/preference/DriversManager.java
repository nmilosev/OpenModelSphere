/*************************************************************************

Copyright (C) 2010 Grandite

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.modelsphere.jack.srtool.screen.ScreenPerspective;

public final class DriversManager {

    private static final String DRIVERS_PREFIX = "jdbc_"; // NOT LOCALIZABLE, driver prefix
    private static final String DRIVERS_SUFFIX = ".properties"; // NOT LOCALIZABLE, file extension
   
    private static Map<String, DriverInfo> drivers = new HashMap<String, DriverInfo>();

    private DriversManager() {
    };

    public static DriverInfo getDriver(String driverName) {
        DriverInfo driver = (DriverInfo) drivers.get(driverName);
        return driver;
    }

    public static DriverInfo addDriver(String driverName) {
        if (driverName == null || driverName.trim().length() == 0) {
            throw new NullPointerException("Invalid Driver Name"); // NOT LOCALIZABLE, exception
        }

        if (getDriver(driverName) != null) {
            return null;
        }

        DriverInfo driver = new DriverInfo(new File(getDriverFileNameForName(driverName)));
        driver.setProperty(DriverInfo.class, DriverInfo.DRIVER_NAME, driverName);

        drivers.put(driverName, driver);
        return driver;
    }

    public static void removeDriver(String driverName) {
        if (driverName == null || driverName.trim().length() == 0)
            return;

        DriverInfo driver = getDriver(driverName);

        if (driver == null)
            return;

        drivers.remove(driverName);
        driver.delete();
    }

    public static void loadDrivers() {
        if (! ScreenPerspective.isFullVersion()) {
            return;
        }
        
        try {
            drivers = new HashMap<String, DriverInfo>();
            String driverPath = getDriverPath();
            File driversPath = new File(driverPath);
            if (!driversPath.exists()) {
                driversPath.mkdir();
            }
            if (driversPath.isDirectory()) {
                File[] driverFiles = driversPath.listFiles();
                if (driverFiles == null)
                    return;
                for (int i = 0; i < driverFiles.length; i++) {
                    if (!driverFiles[i].isFile())
                        continue;
                    String name = driverFiles[i].getName();
                    if (!name.toLowerCase().endsWith(DRIVERS_SUFFIX)) // not a valid driver file
                        continue;
                    name = getDriverNameForFileName(name);
                    DriverInfo driver = new DriverInfo(driverFiles[i]);
                    drivers.put(name, driver);
                    // no conversion yet but keep the version updated
                    PropertiesManager.convert(driver);
                }
            }
        } catch (Exception e) {
        }
    }

    public static void saveDrivers() {
        Iterator<String> iter = drivers.keySet().iterator();
        while (iter.hasNext()) {
            String driverName = (String)iter.next();
            DriverInfo driver = drivers.get(driverName);
            driver.save();
        }
    }

    public static boolean renameDriver(String oldname, String newname) {
        DriverInfo driver = (DriverInfo) drivers.get(oldname);
        if (driver == null)
            return false;

        String newfilename = getDriverFileNameForName(newname);

        boolean renamed = driver.rename(new File(newfilename));
        if (renamed)
            driver.setProperty(DriverInfo.class, DriverInfo.DRIVER_NAME, newname);

        return renamed;
    }

    public static String[] getDrivers() {
        Object[] temp = drivers.keySet().toArray();
        String[] driverKeys = new String[temp.length];
        for (int i = 0; i < temp.length; i++) {
            driverKeys[i] = (String) temp[i];
        }
        return driverKeys;
    }
    
    //
    // private methods
    //
    private static final String DRIVERS_DIR = "drivers"; // NOT LOCALIZABLE, folder name
    private static String _driverPath = null;
    private static String getDriverPath() {
        if (_driverPath == null) { 
            
            _driverPath = System.getProperty("user.dir") 
                + System.getProperty("file.separator")
                + DRIVERS_DIR; 
        }
        
        return _driverPath;
    }

    private static String getDriverNameForFileName(String filename) {
        if (filename == null)
            return null;
        if (filename.toLowerCase().indexOf(DRIVERS_PREFIX) != 0)
            return null;
        if (filename.toLowerCase().lastIndexOf(DRIVERS_SUFFIX) != (filename.length() - DRIVERS_SUFFIX
                .length()))
            return null;
        filename = filename.substring(DRIVERS_PREFIX.length(), filename.length());
        filename = filename.substring(0, filename.length() - DRIVERS_SUFFIX.length());
        return filename;
    }

    private static String getDriverFileNameForName(String drivername) {
        if (drivername == null || drivername.length() == 0)
            return null;
        String driverPath = getDriverPath();
        String driversPath = driverPath 
            + System.getProperty("file.separator")
            + DRIVERS_PREFIX
            + drivername.trim()
            + DRIVERS_SUFFIX;
            
        return driversPath;
    }

}
