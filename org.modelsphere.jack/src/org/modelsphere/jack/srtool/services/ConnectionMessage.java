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

import org.modelsphere.jack.services.ServiceMessage;

public final class ConnectionMessage extends ServiceMessage {
    public boolean startConnection;
    public String driverName; // in
    public String userName; // in
    public String passWord; // in
    public String server; // in
    public String driverInfoName; // in - The driver info name for DriverManager
    public String databaseProductName; // out
    public String databaseProductVersion; // out
    public String jdbcDriverName; // out
    public int jdbcDriverMajorVersion; // out
    public int jdbcDriverMinorVersion; // out
    public boolean jdbcDriverJDBCCompliant; // out
    public int connectionId; // out
    public Class databasePanelClass; // out
    public Class databasePageClass; // out

    public ConnectionMessage(String aDrivername, String aUser, String aPw, String aServer,
            String aDriverInfoName) {
        startConnection = true;
        driverName = aDrivername;
        userName = aUser;
        passWord = aPw;
        server = aServer;
        driverInfoName = aDriverInfoName;
    }
}
