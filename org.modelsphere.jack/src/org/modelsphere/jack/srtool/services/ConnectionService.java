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

package org.modelsphere.jack.srtool.services;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Properties;

import org.modelsphere.jack.preference.DriverInfo;
import org.modelsphere.jack.preference.DriversManager;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.services.ConnectionMessage;
import org.modelsphere.jack.srtool.services.ConnectionServiceProtocol;

/**
 * 
 * A very simple service. It displays the current time on the server to the client, and closes the
 * connection.
 * 
 */
public final class ConnectionService extends ConnectionServiceProtocol {
    public static final String kNoSuchDriver = LocaleMgr.message.getString("NoSuchDriver");

    private static HashMap connectionMap = new HashMap();
    private static int currentConnectionID = 0;

    public static Connection getConnection(int connectionId) {
        Connection connection = (Connection) connectionMap.get(new Integer(connectionId));
        return connection;
    }

    // get drivername, user, pw & server, and return connection id
    public void serve(InputStream i, OutputStream o) throws IOException {
        // TODOServiceList.getSingleInstance();

        // get Driver
        DriverInfo driverInfo = null;
        ConnectionMessage connectionMessage = null;
        ObjectInputStream input = new ObjectInputStream(i);
        try {
            connectionMessage = (ConnectionMessage) input.readObject();
            driverInfo = DriversManager.getDriver(connectionMessage.driverName);
            // check if driver class exists
            if (driverInfo != null) {
                Class driverClass = Class.forName(driverInfo.getPropertyString(DriverInfo.class,
                        DriverInfo.DRIVER_JDBC, DriverInfo.DEFAULT_JDBC_VALUE));
            }
        } catch (ClassNotFoundException ex) {
            driverInfo = null;
        }

        if (driverInfo == null) {
            connectionMessage.errorMessage = kNoSuchDriver;
        } else {
            // start Connection & log it in hashmap
            if (connectionMessage.startConnection) {
                try {
                    Properties info = new Properties();
                    info.put("user", connectionMessage.userName); // NOT
                    // LOCALIZABLE
                    info.put("password", connectionMessage.passWord); // NOT
                    // LOCALIZABLE
                    Connection connection = null;
                    String url = driverInfo.getPropertyString(DriverInfo.class,
                            DriverInfo.DRIVER_URL, "");
                    connection = DriverManager.getConnection(url, info);

                    currentConnectionID++;
                    connectionMap.put(new Integer(currentConnectionID), connection);
                    DatabaseMetaData metadata = connection.getMetaData();
                    connectionMessage.databaseProductName = metadata.getDatabaseProductName();
                    connectionMessage.databaseProductVersion = metadata.getDatabaseProductVersion();
                    connectionMessage.connectionId = currentConnectionID;
                    connectionMessage.jdbcDriverName = metadata.getDriverName();
                    connectionMessage.jdbcDriverMajorVersion = metadata.getDriverMajorVersion();
                    connectionMessage.jdbcDriverMinorVersion = metadata.getDriverMinorVersion();
                    connectionMessage.jdbcDriverJDBCCompliant = DriverManager.getDriver(url)
                            .jdbcCompliant();
                } catch (SQLException ex) {
                    connectionMessage.errorMessage = ex.toString();
                }
            }
        }

        // send object to the caller
        ObjectOutputStream output = new ObjectOutputStream(o);
        output.writeObject(connectionMessage);

        // end socket connection
        o.close();
        i.close();
    }

}
