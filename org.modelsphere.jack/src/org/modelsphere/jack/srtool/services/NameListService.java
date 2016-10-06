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
import java.util.*;
import java.sql.*;
import java.net.URL;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.reverse.SQLLexerStream;
import org.modelsphere.jack.srtool.reverse.jdbc.ObjectNameReader;

/**
 * A very simple service. It displays the current time on the server to the client, and closes the
 * connection.
 **/
public final class NameListService extends NameListServiceProtocol {

    private static final String kConnectionClosed = LocaleMgr.message.getString("ConnectionClosed");
    Writer writer = null;
    Reader reader = null;

    public NameListService() {
        initialize();
    }

    public void initialize() {
        try {
            PipedWriter src = new PipedWriter();
            reader = new PipedReader(src);
            writer = new PrintWriter(src);
        } catch (IOException ex) {
            if (Debug.isDebug())
                ex.printStackTrace();
        }
    }

    // execute and get results
    private void execute(Connection conn, String text, Writer writer, boolean commaSeparator)
            throws SQLException {

        BufferedWriter buffer = new BufferedWriter(writer);
        Statement stmt = conn.createStatement();
        stmt.execute(text);
        ResultSet rs = stmt.getResultSet();
        ResultSetMetaData metadata = rs.getMetaData();
        int nbCols = metadata.getColumnCount();
        String[] labels = new String[nbCols];
        int[] colwidths = new int[nbCols];
        int[] colpos = new int[nbCols];
        int linewidth = 1;

        // read each occurrence
        try {
            while (rs.next()) {
                for (int i = 0; i < nbCols; i++) {
                    Object value = rs.getObject(i + 1);
                    if (value != null) {
                        buffer.write(value.toString());
                        if (commaSeparator)
                            buffer.write(",");
                    }
                }
            }
            buffer.flush();
            rs.close();
        } catch (IOException ex) {
            if (Debug.isDebug())
                ex.printStackTrace();
            // ok, exit from the loop
        } catch (SQLException ex) {
            if (Debug.isDebug())
                ex.printStackTrace();
        }
    }

    private void executeParsedFile(NameListMessage nameListMessage, ArrayList cmds) {
        BufferedWriter buffer = null;
        int connectionId = nameListMessage.connectionId;
        boolean commaSeparated = nameListMessage.commaSeparated;

        // get java.sql.Connection from connectionId
        Connection connection = ConnectionService.getConnection(connectionId);

        if (connection == null) {
            nameListMessage.errorMessage = kConnectionClosed;
        } else {

            try {
                Thread th = null;
                buffer = new BufferedWriter(writer);

                // if reader exists, create reading thread
                if (reader != null) {
                    final ObjectNameReader objectReader = new ObjectNameReader(
                            nameListMessage.nameList);

                    // This thread will read stream until meets EOL (ie. when
                    // writer is closed)
                    th = new Thread() {
                        public void run() {
                            objectReader.readInputFile(reader);
                        }
                    };
                } // end if

                // if no reader (offline reverse), never start the reading
                // thread
                // (because output is written down directly to the disk without
                // processing in counterpart).
                if (reader != null)
                    th.start();

                // execute each SQL statement
                // we skip objects that don't need to be reversed
                Iterator iter = cmds.iterator();
                int index = -1;
                while (iter.hasNext()) {
                    String cmd = (String) iter.next();
                    index++;
                    // check if this statement id is marked as <to skip>
                    if (nameListMessage.ignoredStatementIds != null
                            && nameListMessage.ignoredStatementIds.contains(new Integer(index))) {
                        if (commaSeparated) {
                            buffer.flush();
                            buffer.newLine();
                        }
                        continue;
                    }
                    if (writer != null) {
                        if (cmd != null) {
                            try {
                                execute(connection, cmd, buffer, commaSeparated);
                            } catch (SQLException e) {
                                if (Debug.isDebug())
                                    e.printStackTrace();
                                nameListMessage.errorMessage = nameListMessage.errorMessage == null ? e
                                        .toString()
                                        : nameListMessage.errorMessage.concat("\n" + e.toString()); // NOT
                                // LOCALIZABLE,
                                // escape
                                // code
                            }
                        }
                        if (commaSeparated) {
                            buffer.flush();
                            buffer.newLine();
                        }
                    }
                }
            }
            /*
             * catch (SQLException ex) { if (Debug.isDebug()) ex.printStackTrace();
             * nameListMessage.errorMessage = nameListMessage.errorMessage == null ? ex.toString() :
             * nameListMessage.errorMessage.concat("\n" + ex.toString()); //NOT LOCALIZABLE, escape
             * code }
             */
            catch (IOException ex) {
                if (Debug.isDebug())
                    ex.printStackTrace();
                nameListMessage.errorMessage = nameListMessage.errorMessage == null ? ex.toString()
                        : nameListMessage.errorMessage.concat("\n" + ex.toString()); // NOT LOCALIZABLE, escape
                // code
            }

            // close writer: the reading thread terminates.
            try {
                // WARNING: we must make a pause after closing the buffer
                // otherwise
                // the nameList variable won't be well initialized...strange!
                // [FG]
                buffer.close();
                Thread.sleep(400);
            } catch (IOException ex) {
            } catch (InterruptedException ex) {
            }
        } // end if
    } // end parseSqlFile()

    private void parseSqlFile(NameListMessage nameListMessage) {
        BufferedWriter buffer = null;
        ArrayList cmds = new ArrayList();
        try {
            URL url = new URL(nameListMessage.filename.toExternalForm());
            InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
            // InputStream inputStream = new
            // FileInputStream(nameListMessage.filename); //reporté à 2.6
            // InputStreamReader inputStreamReader = new
            // InputStreamReader(inputStream); //reporté à 2.6
            BufferedReader bugReader = new BufferedReader(inputStreamReader);
            SQLLexerStream stream = new SQLLexerStream(bugReader);

            // execute each SQL statement
            boolean done = false;
            int i = 0;

            while (!done) {
                int tokenID = stream.nextToken();

                // only print statement tokens
                switch (tokenID) {
                case SQLLexerStream.TT_STATEMENT:
                    i++;
                    String text = stream.sval;
                    if (text != null) {
                        cmds.add(text);
                    }
                    break;
                case SQLLexerStream.TT_EOF:
                    done = true;
                    break;
                default:
                    break;
                } // end switch
            } // end while
        } catch (Exception ex) {
            if (Debug.isDebug())
                ex.printStackTrace();
            nameListMessage.errorMessage = ex.toString();
        }

        executeParsedFile(nameListMessage, cmds);
    } // end parseSqlFile()

    // get drivername, user, pw & server, and return connection id
    public void serve(InputStream i, OutputStream o) throws IOException {
        // TODOServiceList.getSingleInstance();
        initialize();
        NameListMessage nameListMessage = null;

        try {
            // read input to know which target panel is required
            ObjectInputStream input = new ObjectInputStream(i);
            nameListMessage = (NameListMessage) input.readObject();

            // parse the required panel
            parseSqlFile(nameListMessage);

        } catch (ClassNotFoundException ex) {
            if (Debug.isDebug())
                ex.printStackTrace();
            nameListMessage.errorMessage = ex.toString();
        }

        // send object to the caller
        ObjectOutputStream output = new ObjectOutputStream(o);
        output.writeObject(nameListMessage);

        // end socket connection
        o.close();
        i.close();
    }
}
