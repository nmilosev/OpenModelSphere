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

import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.reverse.SRXConstants;
import org.modelsphere.jack.srtool.services.StatementMessage;
import org.modelsphere.jack.srtool.services.StatementServiceProtocol;

/**
 * A very simple service. It displays the current time on the server to the client, and closes the
 * connection.
 **/
public final class StatementService extends StatementServiceProtocol implements SRXConstants {
    private static final String kConnectionClosed = LocaleMgr.message.getString("ConnectionClosed");
    private static final String EOL = System.getProperty("line.separator");

    /**
     * Mode for building the srx format with the sql result
     */
    public static final int MODE_NO_FORMAT = 0;
    public static final int MODE_FORMAT_USING_COLUMN_NAME = 1;

    private static int resultMode = MODE_NO_FORMAT;

    Writer writer = null;
    Reader reader = null;

    /**
     * Mode for building the srx format with the sql result Possible values: MODE_NO_FORMAT
     * (Default): Drop each column of the sql result in the srx structures.
     * MODE_FORMAT_USING_COLUMN_NAME: Use the column name as the key for the value and append a EOF
     * after each row of the srx.
     */
    public static void setResultMode(int mode) {
        resultMode = mode;
    }

    public static void ressetResultMode() {
        resultMode = MODE_NO_FORMAT;
    }

    public void initialize() {
        try {
            PipedWriter src = new PipedWriter();
            reader = new PipedReader(src);
            writer = new PrintWriter(src);
        } catch (IOException ex) {
            /*
             * if (Debug.isDebug()) ex.printStackTrace();
             */
        }
    }

    public StatementService() {
        initialize();
    }

    // execute and get results
    /*
     * private void execute(Connection conn, String text, Writer writer, boolean commaSeparator)
     * throws SQLException {
     * 
     * BufferedWriter buffer = new BufferedWriter(writer); Statement stmt = conn.createStatement();
     * stmt.execute(text); ResultSet rs = stmt.getResultSet(); ResultSetMetaData metadata =
     * rs.getMetaData(); int nbCols = metadata.getColumnCount(); String[] labels = new
     * String[nbCols]; int[] colwidths = new int[nbCols]; int[] colpos = new int[nbCols]; int
     * linewidth = 1;
     * 
     * //read each occurrence try { while (rs.next()) { for (int i=0; i<nbCols; i++) { Object value
     * = rs.getObject(i+1); if (value != null) { buffer.write(value.toString()); if (commaSeparator)
     * buffer.write(","); // NOT LOCALIZABLE } } } buffer.flush(); rs.close(); } catch (IOException
     * ex) { ex.printStackTrace(); //ok, exit from the loop } catch (SQLException ex) {
     * ex.printStackTrace(); } }
     */

    // get connectionId and statement and return result
    public void serve(InputStream i, OutputStream o) throws IOException {
        // TODOServiceList.getSingleInstance();
        StatementMessage statementMessage = null;

        try {
            // read input to know which target panel is required
            ObjectInputStream input = new ObjectInputStream(i);
            statementMessage = (StatementMessage) input.readObject();

            // get connection, and create a statement
            int connectionId = statementMessage.connectionId;
            Connection connection = ConnectionService.getConnection(connectionId);
            Statement stmt = connection.createStatement();

            // execute statement
            stmt.execute(statementMessage.statement);
            ResultSet rs = stmt.getResultSet();
            if (rs != null) {
                ResultSetMetaData metadata = rs.getMetaData();
                int nbCols = metadata.getColumnCount();

                for (int j = 1; j <= nbCols; j++) {
                    String columnname = metadata.getColumnName(j);
                    if (columnname == null)
                        columnname = "";
                    statementMessage.columnList.add(columnname);
                }

                // get result and fill result list depending of the selected
                // mode
                if (resultMode == MODE_NO_FORMAT) {
                    while (rs.next()) {
                        ArrayList row = new ArrayList();
                        for (int j = 1; j <= nbCols; j++) {
                            try {
                                Object value = null;
                                try {
                                    value = rs.getObject(j);
                                } catch (SQLException ex) {
                                    /*
                                     * if (Debug.isDebug()) ex.printStackTrace();
                                     */
                                    value = "";
                                }

                                if (value == null)
                                    value = "";

                                row.add(value.toString()); // add column in
                                // current row
                            } catch (NegativeArraySizeException ex) {
                                // ignore
                            }
                        }
                        statementMessage.resultList.add(row); // add current row
                        // in result
                        // list
                    }
                } else { // use column name as tag and append a EOL after each
                    // column - informix
                    while (rs.next()) {
                        ArrayList row = new ArrayList();
                        for (int j = 1; j <= nbCols; j++) {
                            try {
                                Object value = null;
                                try {
                                    value = rs.getObject(j);
                                } catch (SQLException ex) {
                                    /*
                                     * if (Debug.isDebug()) ex.printStackTrace();
                                     */
                                    value = "";
                                }

                                if (value == null)
                                    value = "";

                                String colName = metadata.getColumnName(j);
                                if (colName.toLowerCase().equals(kEOT)) {
                                    if (row.size() > 1) {
                                        String lastAddedRow = (String) row.get(row.size() - 1);
                                        int idx = lastAddedRow.lastIndexOf(EOL);
                                        if (idx != -1) { // Remove previously
                                            // EOL added
                                            lastAddedRow = lastAddedRow.substring(0, idx);
                                        }
                                        // value should be ' ';
                                        row.set(row.size() - 1, lastAddedRow.concat(value
                                                .toString()
                                                + kEOT + EOL));
                                    }
                                } else {
                                    String sRow = colName + " " + value.toString() + EOL;
                                    row.add(sRow); // add column in current row
                                }

                            } catch (NegativeArraySizeException ex) {
                                // ignore
                            }
                        }
                        statementMessage.resultList.add(row); // add current row
                        // in result
                        // list
                    }
                }
                rs.close();
                stmt.close();

            } else {
                int updateCount = stmt.getUpdateCount();
                statementMessage.updateCount = (updateCount < 0 ? 0 : updateCount);
                statementMessage.resultList = null;
                statementMessage.columnList = null;
                stmt.close();
            }
        } catch (ClassNotFoundException ex) {
            /*
             * if (Debug.isDebug()) ex.printStackTrace();
             */
            statementMessage.errorMessage = ex.toString();
        } catch (SQLException ex) {
            /*
             * if (Debug.isDebug()) ex.printStackTrace();
             */
            statementMessage.errorMessage = ex.toString();
        } catch (Exception ex) {
            /*
             * if (Debug.isDebug()) ex.printStackTrace();
             */
            statementMessage.errorMessage = ex.toString();
        }

        // send object to the caller
        ObjectOutputStream output = new ObjectOutputStream(o);
        output.writeObject(statementMessage);

        // end socket connection
        o.close();
        i.close();
    }
}
