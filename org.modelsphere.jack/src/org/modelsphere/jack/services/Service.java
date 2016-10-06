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

package org.modelsphere.jack.services;

import java.io.*;
import java.util.Enumeration;
import java.util.StringTokenizer;

import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

/**
 * Here is the Service interface that we have seen so much of. It defines only a single method which
 * is invoked to provide the service. serve() will be passed an input stream and an output stream to
 * the client. It should do whatever it wants with them, and should close them before returning.
 * 
 * All connections through the same port to this service share a single Service object. Thus, any
 * state local to an individual connection must be stored in local variables within the serve()
 * method. State that should be global to all connections on the same port should be stored in
 * instance variables of the Service class. If the same Service is running on more than one port,
 * there will typically be different Service instances for each port. Data that should be global to
 * all connections on any port should be stored in static variables.
 * 
 * Note that implementations of this interface must have a no-argument constructor if they are to be
 * dynamically instantiated by the main() method of the Server class.
 **/
public interface Service {
    static final String kMoreThanOneContrlConnError = "Only one control connection allowed at a time."; // NOT LOCALIZABLE
    static final String kInvalidPwError = "Invalid Password"; // NOT LOCALIZABLE
    static final String kPwRequiredError = "Password Required"; // NOT LOCALIZABLE
    static final String kNoArgConstrMissingError = "Service must have a no-argument constructor."; // NOT LOCALIZABLE
    static final String kUnrecognizedError = "Unrecognized Command"; // NOT
    // LOCALIZABLE
    static final String kGeneralError = "Exception while parsing or executing command:"; // NOT LOCALIZABLE

    static final String kServiceAddedMsg = "Service added"; // NOT LOCALIZABLE
    static final String kServiceRemovedMsg = "Service removed"; // NOT LOCALIZABLE
    static final String kMaxConnectionMsg = "Max Connections changed"; // NOT LOCALIZABLE

    static final String kServiceAndPortPattern = "Service {0} on port {1}"; // NOT LOCALIZABLE
    static final String kMaxConnectionsPattern = "Max connections: {0}"; // NOT LOCALIZABLE

    static final String kCommandsText = "COMMANDS:\n\tpassword <password>\n\tadd <service> <port>\n\t"
            + // NOT LOCALIZABLE
            "remove <port>\n\tmax <max-connections>\n\tstatus\n\thelp\n\tquit"; // NOT LOCALIZABLE

    static final String kPassword = "password"; // NOT LOCALIZABLE
    static final String kAdd = "add"; // NOT LOCALIZABLE
    static final String kRemove = "remove"; // NOT LOCALIZABLE
    static final String kMax = "max"; // NOT LOCALIZABLE
    static final String kStatus = "statis"; // NOT LOCALIZABLE
    static final String kHelp = "help"; // NOT LOCALIZABLE
    static final String kQuit = "quit"; // NOT LOCALIZABLE

    static final String kOK = LocaleMgr.screen.getString("OK");

    public void serve(InputStream in, OutputStream out) throws IOException;

}

/**
 * This is a non-trivial service. It implements a command-based protocol that gives
 * password-protected runtime control over the operation of the server. See the main() method of the
 * Server class to see how this service is started.
 * 
 * The recognized commands are: password: give password; authorization is required for most commands
 * add: dynamically add a named service on a specified port remove: dynamically remove the service
 * running on a specified port max: change the current maximum connection limit. status: display
 * current services, connections, and connection limit help: display a help message quit: disconnect
 * 
 * This service displays a prompt, and sends all of its output to the user in capital letters. Only
 * one client is allowed to connect to this service at a time.
 **/
/* public static */class Control implements Service {
    ServiceServer server; // The server we control
    String password; // The password we require
    boolean connected = false; // Whether a client is already connected to us

    /**
     * Create a new Control service. It will control the specified Server object, and will require
     * the specified password for authorization Note that this Service does not have a no argument
     * constructor, which means that it cannot be dynamically instantiated and added as the other,
     * generic services above can be.
     **/
    public Control(ServiceServer server, String password) {
        this.server = server;
        this.password = password;
    }

    /**
     * This is the serve method that provides the service. It reads a line the client, and uses
     * java.util.StringTokenizer to parse it into commands and arguments. It does various things
     * depending on the command.
     **/
    public void serve(InputStream i, OutputStream o) throws IOException {
        // Setup the streams
        BufferedReader in = new BufferedReader(new InputStreamReader(i));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(o));
        String line;
        boolean authorized = false; // Has the user has given the password yet?
        int num;

        // If there is already a client connected to this service, display a
        // message to this client and close the connection. We use a
        // synchronized block to prevent a race condition.
        synchronized (this) {
            if (connected) {
                out.println(kMoreThanOneContrlConnError);
                out.close();
                return;
            } else
                connected = true;
        }

        for (;;) { // infinite loop
            out.print("> "); // Display a prompt
            out.flush(); // Make it appear right away
            line = in.readLine(); // Get the user's input
            if (line == null)
                break; // Quit if we get EOF.
            try {
                // Use a StringTokenizer to parse the user's command
                StringTokenizer t = new StringTokenizer(line);
                if (!t.hasMoreTokens())
                    continue; // if input was blank line
                // Get the first word of the input and convert to lower case
                String command = t.nextToken().toLowerCase();
                // Now compare it to each of the possible commands, doing the
                // appropriate thing for each command
                if (command.equals(kPassword)) { // Password command
                    String p = t.nextToken(); // Get the next word of input
                    if (p.equals(this.password)) { // Does it equal the password
                        out.println(kOK); // Say so
                        authorized = true; // Grant authorization
                    } else
                        out.println(kInvalidPwError); // Otherwise fail
                } else if (command.equals(kAdd)) { // Add Service command
                    // Check whether password has been given
                    if (!authorized)
                        out.println(kPwRequiredError);
                    else {
                        // Get the name of the service and try to dynamically
                        // load
                        // and instantiate it. Exceptions will be handled below
                        String serviceName = t.nextToken();
                        // serviceName not known at compile time: forName()
                        // mandatory
                        Class serviceClass = Class.forName(serviceName);
                        Service service;
                        try {
                            service = (Service) serviceClass.newInstance();
                        } catch (NoSuchMethodError e) {
                            throw new IllegalArgumentException(kNoArgConstrMissingError);
                        }
                        int port = Integer.parseInt(t.nextToken());
                        // If no exceptions occurred, add the service
                        server.addService(service, port);
                        out.println(kServiceAddedMsg); // acknowledge
                    }
                } else if (command.equals(kRemove)) { // Remove service command
                    if (!authorized)
                        out.println(kPwRequiredError);
                    else {
                        int port = Integer.parseInt(t.nextToken()); // get port
                        server.removeService(port); // remove the service on it
                        out.println(kServiceRemovedMsg); // acknowledge
                    }
                } else if (command.equals(kMax)) { // Set max connection limit
                    if (!authorized)
                        out.println(kPwRequiredError);
                    else {
                        int max = Integer.parseInt(t.nextToken()); // get limit
                        server.connectionManager.setMaxConnections(max); // set limit
                        out.println(kMaxConnectionMsg); // acknowledge
                    }
                } else if (command.equals(kStatus)) { // Status Display command
                    if (!authorized)
                        out.println(kPwRequiredError);
                    else {
                        // Display a list of all services currently running
                        Enumeration keys = server.services.keys();
                        while (keys.hasMoreElements()) {
                            Integer port = (Integer) keys.nextElement();
                            ServiceServer.Listener listener = (ServiceServer.Listener) server.services
                                    .get(port);
                            String msg = MessageFormat.format(kServiceAndPortPattern, new Object[] {
                                    listener.service.getClass().getName(), String.valueOf(port) });
                            out.println(msg);
                        }
                        // Display a list of all current connections
                        server.connectionManager.printConnections(out);
                        // Display the current connection limit
                        String msg = MessageFormat.format(kMaxConnectionsPattern,
                                new Object[] { String
                                        .valueOf(server.connectionManager.maxConnections) });
                        out.println(msg);
                    }
                } else if (command.equals(kHelp)) { // Help command
                    // Display command syntax. Password not required
                    out.println(kCommandsText);
                } else if (command.equals(kQuit))
                    break; // Quit command. Exit.
                else
                    out.println(kUnrecognizedError); // Unknown command error
            } catch (Exception e) {
                // If an exception occurred during the command, print an error
                // message, then output details of the exception.
                out.println(kGeneralError);
                out.println(e);
            }
        }
        // Finally, when the loop command loop ends, close the streams
        // and set our connected flag to false so that other clients can
        // now connect.
        out.close();
        in.close();
        connected = false;
    }
}
