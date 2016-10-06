/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.services;

import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.services.ServiceServer;
import org.modelsphere.jack.srtool.services.ServiceProtocolList;

public final class ServiceList extends ServiceProtocolList {

    public ServiceList() {
        Integer port = new Integer(PROPERTY_INITITIAL_PORT_DEFAULT);
        PropertiesSet set = PropertiesManager.APPLICATION_PROPERTIES_SET;
        if (set != null) {
            Integer initPort = set.getPropertyInteger(ServiceProtocolList.class,
                    PROPERTY_INITITIAL_PORT, port);
            SERVICE_INITIAL_PORT = initPort.intValue();
            INFORMATION_SERVICE = SERVICE_INITIAL_PORT;
            DRIVER_SERVICE = SERVICE_INITIAL_PORT + 1;
            CONNECTION_SERVICE = SERVICE_INITIAL_PORT + 2;
            TARGET_PANEL_SERVICE = SERVICE_INITIAL_PORT + 3;
            NAME_LIST_SERVICE = SERVICE_INITIAL_PORT + 4;
            STATEMENT_SERVICE = SERVICE_INITIAL_PORT + 5;
        }
    } // end ServiveList()

    // initialize single instance
    private static ServiceList g_serviceList = null;

    public static ServiceList getSingleInstance() {
        if (g_serviceList == null) {
            g_serviceList = new ServiceList();
            g_serviceList.start();
        }

        return g_serviceList;
    } // end getSingleInstance()

    private boolean m_started = false;

    protected boolean isStarted() {
        return m_started;
    }

    protected void start() {
        // Start services
        final int MAX_SERVICES = 10;
        ServiceServer s = new ServiceServer(null, MAX_SERVICES);

        try {
            s.addService("org.modelsphere.sms.services.InformationService", INFORMATION_SERVICE); // NOT LOCALIZABLE
            s.addService("org.modelsphere.jack.srtool.services.DriverService", DRIVER_SERVICE); // NOT LOCALIZABLE
            s.addService("org.modelsphere.jack.srtool.services.ConnectionService",
                    CONNECTION_SERVICE); // NOT LOCALIZABLE
            s.addService("org.modelsphere.sms.services.TargetPanelService", TARGET_PANEL_SERVICE); // NOT LOCALIZABLE
            s.addService("org.modelsphere.jack.srtool.services.NameListService", NAME_LIST_SERVICE); // NOT LOCALIZABLE
            s
                    .addService("org.modelsphere.jack.srtool.services.StatementService",
                            STATEMENT_SERVICE); // NOT LOCALIZABLE
            m_started = true;
        } catch (java.io.IOException ex) {
            org.modelsphere.jack.debug.Debug.trace(ex.toString());
        }
    }
}
