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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.modelsphere.jack.srtool.services.TargetPanelMessage;
import org.modelsphere.jack.srtool.services.TargetPanelServiceProtocol;

/**
 * 
 * A very simple service. It displays the current time on the server to the client, and closes the
 * connection.
 * 
 */
public final class TargetPanelService extends TargetPanelServiceProtocol {

    // get drivername, user, pw & server, and return connection id
    public void serve(InputStream i, OutputStream o) throws IOException {
        ServiceList.getSingleInstance();
        TargetPanelMessage targetPanelMessage = null;

        /*
         * try { //read input to know which target panel is required //ObjectInputStream input = new
         * ObjectInputStream(i); //targetPanelMessage = (TargetPanelMessage)input.readObject();
         * 
         * //TODO: load the required panel, according targetPanelMessage.dbmsName // Class
         * panelClass = org.modelsphere.sms.plugins.oracle.OracleOptionsPanel.class; // Class
         * pageClass = org.modelsphere.sms.plugins.oracle.OracleOptionsPanel.Page.class;
         * 
         * //targetPanelMessage.panelClass = panelClass; //targetPanelMessage.pageClass = pageClass;
         * 
         * //create the required options, according targetPanelMessage.dbmsName
         * //targetPanelMessage.targetOptions = new OracleOptions();
         * 
         * //create the required actions, according targetPanelMessage.dbmsName
         * //targetPanelMessage.targetActionsClass = OracleActions.class;
         * 
         * } catch (ClassNotFoundException ex) { targetPanelMessage.errorMessage = ex.toString(); }
         */
        // send object to the caller
        ObjectOutputStream output = new ObjectOutputStream(o);
        output.writeObject(targetPanelMessage);

        // end socket connection
        o.close();
        i.close();
    }
}
