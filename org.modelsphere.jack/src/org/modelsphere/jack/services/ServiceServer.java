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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import org.modelsphere.jack.debug.Log;
import org.modelsphere.jack.text.MessageFormat;

/**
 * 
 * Services Server This class is a generic framework for a flexible, multi-threaded server. It
 * listens on any number of specified ports, and, when it receives a connection on a port, passes
 * input and output streams to a specified Service object which provides the actual service. It can
 * limit the number of concurrent connections, and logs activity to a specified stream.
 * 
 */
public class ServiceServer {
    private boolean invokedStandalone = false;
    private static final String kControl = "-control"; // NOT LOCALIZABLE, internal use only
    private static final String kMaxConnectionsError = "Connection refused; server has reached maximum number of clients."; // NOT LOCALIZABLE, internal use only

    private static final String kPortAlreadyInUsePattern = "Port {0} already in use."; // NOT LOCALIZABLE, internal use only
    private static final String kStartingServicePattern = "Starting service {0} on port {1}."; // NOT LOCALIZABLE, internal use only
    private static final String kStoppingServicePattern = "Stopping service {0} on port {1}."; // NOT LOCALIZABLE, internal use only
    private static final String kConnectionMgrPattern = "Starting connection manager.  Max connections: {0}."; // NOT LOCALIZABLE, internal use only
    private static final String kMaxConnectionsPattern = "Connection refused to {0}:{1}: max connections reached."; // NOT LOCALIZABLE, internal use only
    private static final String kSuccessfulConnectPattern = "Connected to {0}:{1} on port {2} for service {3}."; // NOT LOCALIZABLE, internal use only
    private static final String kConnectionClosedPattern = "Connection to {0}:{1} closed."; // NOT LOCALIZABLE, internal use only

    private static void sleep(int mseconds) {
        try {
            Thread.sleep(mseconds);
        } catch (InterruptedException e) {
        }
    }

    public static void installServiceServer(String servicePackage, String[] args)
            throws java.io.IOException, ClassNotFoundException, IllegalAccessException,
            InstantiationException {
        // Create a Server object that uses standard out as its log and
        // has a limit of ten concurrent connections at once.
        ServiceServer s = new ServiceServer(System.out, 10);
        s.invokedStandalone = true;

        // Parse the argument list
        int i = 0;
        while (i < args.length) {
            if (args[i].equals(kControl)) { // Handle the -control argument
                i++;
                String password = args[i++];
                int port = Integer.parseInt(args[i++]);
                s.addService(new Control(s, password), port); // add control service
            } else {
                // Otherwise start a named service on the specified port.
                // Dynamically load and instantiate a class that implements
                // Service.
                String serviceName = servicePackage + "." + args[i++];
                Class serviceClass = Class.forName(serviceName); // dynamic load: forName() mandatory
                Service service = (Service) serviceClass.newInstance(); // instantiate
                int port = Integer.parseInt(args[i++]);
                s.addService(service, port);
            }
        } // end while
    }

    /**
     * A main() method for running the server as a standalone program. The command-line arguments to
     * the program should be pairs of servicenames and port numbers. For each pair, the program will
     * dynamically load the named Service class, instantiate it, and tell the server to provide that
     * Service on the specified port. The special -control argument should be followed by a password
     * and port, and will start special server control service running on the specified port,
     * protected by the specified password.
     **/
    /*
     * public static void main(String[] args) { try { args = new String[] {"InformationService",
     * "1024"}; //NOT LOCALIZABLE, used in main() function only
     * 
     * if (args.length < 2) // Check number of arguments throw new
     * IllegalArgumentException("Must start at least one service"); //NOT LOCALIZABLE, used in
     * main() function only
     * 
     * installServiceServer("org.modelsphere.sms.services", args); //NOT LOCALIZABLE, used in main()
     * function only } catch (Exception e) { // Display a message if anything goes wrong
     * System.err.println("Server: " + e); //NOT LOCALIZABLE, used in main() function only
     * System.err.println("Usage: java Server [-control <password> <port>] " + //NOT LOCALIZABLE,
     * used in main() function only "[<servicename> <port> ... ]"); //NOT LOCALIZABLE, used in
     * main() function only //ServiceServer.sleep(5000); try { System.in.read(); } catch
     * (java.io.IOException ex) { //ignore it } System.exit(1); } }
     */

    // This is the state for the server
    ConnectionManager connectionManager; // The ConnectionManager object
    Hashtable services; // The current services and their ports
    ThreadGroup threadGroup; // The threadgroup for all our threads
    PrintWriter logStream; // Where we send our logging output to

    /**
     * This is the Server() constructor. It must be passed a stream to send log output to (may be
     * null), and the limit on the number of concurrent connections. It creates and starts a
     * ConnectionManager thread which enforces this limit on connections.
     **/
    public ServiceServer(OutputStream logStream, int maxConnections) {
        setLogStream(logStream);
        log("Starting server"); // NOT LOCALIZABLE, just for logging
        threadGroup = new ThreadGroup("Server"); // NOT LOCALIZABLE, internal code
        connectionManager = new ConnectionManager(threadGroup, maxConnections);
        connectionManager.start();
        services = new Hashtable();
    }

    /**
     * A public method to set the current logging stream. Pass null to turn logging off
     **/
    public void setLogStream(OutputStream out) {
        if (out != null)
            logStream = new PrintWriter(new OutputStreamWriter(out));
        else
            logStream = null;
    }

    /** Write the specified string to the log */
    protected synchronized void log(String s) {
        if (logStream != null) {
            logStream.println("[" + new Date() + "] " + s); // NOT LOCALIZABLE
            logStream.flush();
        }
        Log.add(s, Log.LOG_SERVICES);
    }

    /** Write the specified object to the log */
    protected void log(Object o) {
        log(o.toString());
    }

    /**
     * This method makes the server start providing a new service. It runs the specified Service
     * object on the specified port.
     **/
    public void addService(String serviceName, int port) throws IOException {
        try {
            // forName(0 here is mandatory, service being not known at compile
            // time
            Class serviceClass = Class.forName(serviceName);
            Service service = (Service) serviceClass.newInstance();
            addService(service, port);
        } catch (ClassNotFoundException e) {
        } catch (IllegalAccessException e) {
        } catch (InstantiationException e) {
        }
    }

    public void addService(Service service, int port) throws IOException {
        Integer key = new Integer(port); // the hashtable key
        // Check whether a service is already on that port
        if (services.get(key) != null) {

            String msg = MessageFormat.format(kPortAlreadyInUsePattern, new Object[] { String
                    .valueOf(port) });
            throw new IllegalArgumentException(msg);
        }

        // Create a Listener object to listen for connections on the port
        Listener listener = new Listener(threadGroup, port, service);
        // Store it in the hashtable
        services.put(key, listener);
        // Log it
        String msg = MessageFormat.format(kStartingServicePattern, new Object[] {
                service.getClass().getName(), String.valueOf(port) });
        log(msg);
        // Start the listener running.
        listener.start();
    }

    /**
     * This method makes the server stop providing a service on a port. It does not terminate any
     * pending connections to that service, merely causes the server to stop accepting new
     * connections
     **/
    public void removeService(int port) {
        Integer key = new Integer(port); // hashtable key
        // Look up the Listener object for the port in the hashtable of services
        final Listener listener = (Listener) services.get(key);
        if (listener == null)
            return;
        // Ask the listener to stop
        listener.pleaseStop();
        // Remove it from the hashtable
        services.remove(key);
        // And log it.
        String msg = MessageFormat.format(kStoppingServicePattern, new Object[] {
                listener.service.getClass().getName(), String.valueOf(port) });
        log(msg);
    }

    /**
     * This nested Thread subclass is a listener. It listens for connections on a specified port
     * (using a ServerSocket) and when it gets a connection request, it calls a method of the
     * ConnectionManager to accept (or reject) the connection. There is one Listener for each
     * Service being provided by the Server. The Listener passes the Server object to the
     * ConnectionManager, but doesn't do anything with it itself.
     */
    public class Listener extends Thread {
        ServerSocket listen_socket; // The socket we listen for connections on
        int port; // The port we're listening on
        Service service; // The service to provide on that port
        boolean stop = false; // Whether we've been asked to stop

        /**
         * The Listener constructor creates a thread for itself in the specified threadgroup. It
         * creates a ServerSocket to listen for connections on the specified port. It arranges for
         * the ServerSocket to be interruptible, so that services can be removed from the server.
         **/
        public Listener(ThreadGroup group, int port, Service service) throws IOException {
            super(group, "Listener:" + port); // NOT LOCALIZABLE, Listener being
            // a Class name
            listen_socket = new ServerSocket(port);
            // give the socket a non-zero timeout so accept() can be interrupted
            listen_socket.setSoTimeout(600000);
            this.port = port;
            this.service = service;
        }

        /** This is the nice way to get a Listener to stop accepting connections */
        public void pleaseStop() {
            this.stop = true; // set the stop flag
            this.interrupt(); // and make the accept() call stop blocking
        }

        /**
         * A Listener is a Thread, and this is its body. Wait for connection requests, accept them,
         * and pass the socket on to the ConnectionManager object of this server
         **/
        public void run() {
            while (!stop) { // loop until we're asked to stop.
                try {
                    Socket client = listen_socket.accept();
                    connectionManager.addConnection(client, service);
                } catch (InterruptedIOException e) {
                } catch (IOException e) {
                    log(e);
                }
            }
        }
    }

    /**
     * This nested class manages client connections for the server. It maintains a list of current
     * connections and enforces the maximum connection limit. It creates a separate thread (one per
     * server) that sits around and wait()s to be notify()'d that a connection has terminated. When
     * this happens, it updates the list of connections.
     **/
    public class ConnectionManager extends Thread {
        int maxConnections; // The maximum number of allowed connections
        Vector connections; // The current list of connections

        /**
         * Create a ConnectionManager in the specified thread group to enforce the specified maximum
         * connection limit. Make it a daemon thread so the interpreter won't wait around for it to
         * exit.
         **/
        public ConnectionManager(ThreadGroup group, int maxConnections) {
            super(group, "ConnectionManager"); // NOT LOCALIZABLE, ConnectionManager being a Class name
            this.setDaemon(true);
            this.maxConnections = maxConnections;
            connections = new Vector(maxConnections);
            String msg = MessageFormat.format(kConnectionMgrPattern, new Object[] { String
                    .valueOf(maxConnections) });
            log(msg);
        }

        /**
         * This is the method that Listener objects call when they accept a connection from a
         * client. It either creates a Connection object for the connection and adds it to the list
         * of current connections, or, if the limit on connections has been reached, it closes the
         * connection.
         **/
        synchronized void addConnection(Socket s, Service service) {
            // If the connection limit has been reached
            if (connections.size() >= maxConnections) {
                try {
                    PrintWriter out = new PrintWriter(s.getOutputStream());
                    // Then tell the client it is being rejected.
                    out.println(kMaxConnectionsError);
                    out.flush();
                    // And close the connection to the rejected client.
                    s.close();
                    // And log it, of course
                    String msg = MessageFormat.format(kMaxConnectionsPattern, new Object[] {
                            s.getInetAddress().getHostAddress(), String.valueOf(s.getPort()) });
                    log(msg);
                } catch (IOException e) {
                    log(e);
                }
            } else { // Otherwise, if the limit has not been reached
                // Create a Connection thread to handle this connection
                Connection c = new Connection(s, service);
                // Add it to the list of current connections
                connections.addElement(c);
                // Log this new connection
                String msg = MessageFormat.format(kSuccessfulConnectPattern, new Object[] {
                        s.getInetAddress().getHostAddress(), String.valueOf(s.getPort()),
                        String.valueOf(s.getLocalPort()), service.getClass().getName() });

                log(msg);
                // And start the Connection thread running to provide the
                // service
                c.start();
            }
        }

        /**
         * A Connection object calls this method just before it exits. This method uses notify() to
         * tell the ConnectionManager thread to wake up and delete the thread that has exited.
         **/
        public synchronized void endConnection() {
            this.notify();
        }

        /** Change the current connection limit */
        public synchronized void setMaxConnections(int max) {
            maxConnections = max;
        }

        /**
         * Output the current list of connections to the specified stream. This method is used by
         * the Control service defined below.
         **/
        public synchronized void printConnections(PrintWriter out) {
            for (int i = 0; i < connections.size(); i++) {
                Connection c = (Connection) connections.elementAt(i);
                String msg = MessageFormat.format(kSuccessfulConnectPattern, new Object[] {
                        c.client.getInetAddress().getHostAddress(),
                        String.valueOf(c.client.getPort()),
                        String.valueOf(c.client.getLocalPort()), c.service.getClass().getName() });

                out.println(msg);
            }
        }

        /**
         * The ConnectionManager is a thread, and this is the body of that thread. While the
         * ConnectionManager methods above are called by other threads, this method is run in its
         * own thread. The job of this thread is to keep the list of connections up to date by
         * removing connections that are no longer alive. It uses wait() to block until notify()'d
         * by the endConnection() method.
         **/
        public void run() {
            while (true) { // infinite loop
                // Check through the list of connections, removing dead ones
                for (int i = 0; i < connections.size(); i++) {
                    Connection c = (Connection) connections.elementAt(i);
                    if (!c.isAlive()) {
                        connections.removeElementAt(i);
                        String msg = MessageFormat.format(kConnectionClosedPattern, new Object[] {
                                c.client.getInetAddress().getHostAddress(),
                                String.valueOf(c.client.getPort()) });
                        log(msg);
                    }
                }
                // Now wait to be notify()'d that a connection has exited
                // When we wake up we'll check the list of connections again.
                try {
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /**
     * This class is a subclass of Thread that handles an individual connection between a client and
     * a Service provided by this server. Because each such connection has a thread of its own, each
     * Service can have multiple connections pending at once. Despite all the other threads in use,
     * this is the key feature that makes this a multi-threaded server implementation.
     **/
    public class Connection extends Thread {
        Socket client; // The socket to talk to the client through
        Service service; // The service being provided to that client

        /**
         * This constructor just saves some state and calls the superclass constructor to create a
         * thread to handle the connection. Connection objects are created by Listener threads.
         * These threads are part of the server's ThreadGroup, so all Connection threads are part of
         * that group, too.
         **/
        public Connection(Socket client, Service service) {
            super("Server.Connection:" + client.getInetAddress().getHostAddress() + // NOT LOCALIZABLE, Thread name
                    ":" + client.getPort());
            this.client = client;
            this.service = service;
        }

        /**
         * This is the body of each and every Connection thread. All it does is pass the client
         * input and output streams to the serve() method of the specified Service object. That
         * method is responsible for reading from and writing to those streams to provide the actual
         * service. Recall that the Service object has been passed from the Server.addService()
         * method to a Listener object to the ConnectionManager.addConnection() to this Connection
         * object, and is now finally getting used to provide the service. Note that just before
         * this thread exits it calls the ConnectionManager.endConnection() method to wake up the
         * ConnectionManager thread so that it can remove this Connection from its list of active
         * connections.
         **/
        public void run() {
            try {
                InputStream in = client.getInputStream();
                OutputStream out = client.getOutputStream();
                service.serve(in, out);
            } catch (Exception e) {
                log(e);
            } finally {
                connectionManager.endConnection();
            }
        }
    }
}
