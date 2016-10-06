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

package org.modelsphere.jack.srtool.reverse.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.services.ConnectionMessage;
import org.modelsphere.jack.srtool.services.ConnectionService;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.jack.util.SrVector;

public final class ActiveConnectionManager {
    private static SrVector listeners = new SrVector();

    private static ConnectionMessage cm = null;

    private ActiveConnectionManager() {
    };

    public static void addActiveConnectionListener(ActiveConnectionListener listener) {
        if (listener == null || listeners.contains(listener))
            return;
        listeners.add(listener);
    }

    public static void removeActiveConnectionListener(ActiveConnectionListener listener) {
        if (listener == null || !listeners.contains(listener))
            return;
        listeners.remove(listener);
    }

    private static void setActiveConnection(ConnectionMessage cm) {
        if (cm == ActiveConnectionManager.cm)
            return;
        ActiveConnectionManager.cm = cm;
        fireActiveConnectionChanged();
    }

    private static void fireActiveConnectionChanged() {
        int nb = listeners.size();
        while (--nb >= 0) {
            ActiveConnectionListener listener = (ActiveConnectionListener) listeners.elementAt(nb);
            listener.activeConnectionChanged(cm);
        }
    }

    public static Connection getActiveConnection() {
        return cm == null ? null : ConnectionService.getConnection(cm.connectionId);
    }

    public static ConnectionMessage getActiveConnectionMessage() {
        return cm;
    }

    public static void performConnection() {
        //if not connected, try to connect
        if (cm == null)
            openDriverFrame();
        else {
            // disconnect
            try {
                Connection connection = getActiveConnection();
                if (connection != null && !connection.isClosed())
                    connection.close();
            } catch (SQLException e) {
            }
            setActiveConnection(null);
        }
    }

    //try to open driver frame
    private static void openDriverFrame() {
        //Connection c = null;
        ConnectionMessage cm = null;
        DriverDialog frame = null;
        try {
            frame = new DriverDialog();
            frame.setVisible(true);
            cm = frame.getConnectionMessage();

            /*
             * if (cm != null) { ConnectDialog connectDialog = new ConnectDialog(driver);
             * connectDialog.setVisible(true); cm = connectDialog.getConnectionMessage(); if (cm !=
             * null) { c = ConnectionService.getConnection(cm.connectionId); cm.driverName = driver;
             * } }
             */
        } catch (Exception ex) {
            if (frame != null)
                frame.dispose();
            //c = null;
            cm = null;
            ExceptionHandler
                    .processUncatchedException(ApplicationContext.getDefaultMainFrame(), ex);
        }
        setActiveConnection(cm);
    }

}
