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

import java.net.*;

import org.modelsphere.jack.debug.Debug;

public abstract class ServiceProtocolList {
    public static final String PROPERTY_INITITIAL_PORT = "service.initial.port"; // NOT LOCALIZABLE
    public static final int PROPERTY_INITITIAL_PORT_DEFAULT = 3000;

    // reset in concrete ServiceList
    public static int SERVICE_INITIAL_PORT = 0;
    public static int INFORMATION_SERVICE = 0;
    public static int DRIVER_SERVICE = 0;
    public static int CONNECTION_SERVICE = 0;
    public static int TARGET_PANEL_SERVICE = 0;
    public static int NAME_LIST_SERVICE = 0;
    public static int STATEMENT_SERVICE = 0;

    private static InetAddress address = null;

    // Keep a reference to the actual service list
    private static ServiceProtocolList g_serviceList = null;

    // sms.Application sets the actual service list
    public static void setActualServiceList(ServiceProtocolList serviceList) {
        g_serviceList = serviceList;
    }

    public static final String getServerIP() {
        Debug.assert2(g_serviceList != null,
                "sms.Application() must call ServiceProtocolList.setActualServiceList().");

        // if not started yet, start it now
        if (!g_serviceList.isStarted()) {
            g_serviceList.start();
        } // end if

        if (address == null) {
            try {
                address = InetAddress.getLocalHost();
            } catch (UnknownHostException ex) {
                // ignore
            }
        } // end if

        String ipAddress = address.getHostAddress();
        return ipAddress;
    } // end getServerIP()

    protected abstract boolean isStarted();

    protected abstract void start();
} // end ServiceProtocolList
