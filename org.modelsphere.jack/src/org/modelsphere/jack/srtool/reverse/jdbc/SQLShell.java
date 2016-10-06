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

import java.io.*;
import java.util.*;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.script.Shell;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.reverse.SQLLexerStream;
import org.modelsphere.jack.srtool.reverse.jdbc.ActiveConnectionManager;
import org.modelsphere.jack.srtool.services.ConnectionMessage;
import org.modelsphere.jack.srtool.services.ServiceProtocolList;
import org.modelsphere.jack.srtool.services.StatementMessage;
import org.modelsphere.jack.text.MessageFormat;

public final class SQLShell extends Shell {
    private static final String SQL_SHELL = LocaleMgr.misc.getString("SQL_SHELL");
    private static final String SQL_PROMPT = LocaleMgr.misc.getString("SQL_PROMPT");
    private static final String SQL_SCRIPTS = LocaleMgr.misc.getString("SQL_SCRIPTS");
    private static final String CONNECTED_PATTERN = LocaleMgr.message
            .getString("CONNECTED_PATTERN");
    private static final String NewProject = LocaleMgr.misc.getString("NewProject");
    private static final String kNotConnected = LocaleMgr.misc.getString("NotConnected");
    private static final String kResult0 = LocaleMgr.screen.getString("Result0");
    private static final String kUpdate0 = LocaleMgr.message.getString("Update0");

    private boolean connected = false;
    private ConnectionMessage connection;
    private StatementMessage statementMsg;
    private String addressIP = ServiceProtocolList.getServerIP();
    private int port = ServiceProtocolList.STATEMENT_SERVICE;

    private int resultCount = 0;

    private Shell.ShellInitializer shellInitializer = new Shell.ShellInitializer(SQL_SHELL,
            SQL_PROMPT + " ", "sql", // NOT LOCALIZABLE
            SQL_SCRIPTS);

    private ActiveConnectionListener connectionListener = new ActiveConnectionListener() {
        public void activeConnectionChanged(ConnectionMessage cm) {
            setConnection(cm);
        }
    };

    public SQLShell() {
        super();
        super.init(shellInitializer);
        setConnection(ActiveConnectionManager.getActiveConnectionMessage());
        ActiveConnectionManager.addActiveConnectionListener(connectionListener);
    }

    public void setConnection(ConnectionMessage aConnection) {
        connection = aConnection;
        welcome();
        updateTitle();
    }

    public void dispose() {
        super.dispose();
        ActiveConnectionManager.removeActiveConnectionListener(connectionListener);
    }

    private void updateTitle() {
        if (connection == null) {
            setTitle(shellInitializer.shellTitle + " - " + kNotConnected);
        } else {
            setTitle(shellInitializer.shellTitle + " - " + connection.databaseProductName + " ["
                    + connection.driverInfoName + "]");
        }
    }

    // print welcome message
    public void welcome() {
        String msg = kNotConnected;
        if (connection != null)
            msg = MessageFormat.format(CONNECTED_PATTERN, new Object[] {
                    connection.databaseProductName, connection.databaseProductVersion });
        resultArea.append(msg + NEWLINE);
    }

    // Execute and get results
    public String execute(String command) throws ShellException {
        String result = "";
        if (connection == null) {
            result = kNotConnected + NEWLINE;
            return result;
        }
        try {
            buffer.reset();

            statementMsg = new StatementMessage(connection.connectionId, command);
            java.net.Socket s = new java.net.Socket(addressIP, port);
            ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
            output.writeObject(statementMsg);

            ObjectInputStream input = new ObjectInputStream(s.getInputStream());
            statementMsg = (StatementMessage) input.readObject();
            String errMsg = statementMsg.errorMessage;
            int updateCount = statementMsg.updateCount;

            if (errMsg != null) {
                result += "\n" + errMsg + "\n"; // NOT LOCALIZABLE, escade code
            } else if (statementMsg.resultList != null) {
                ArrayList resultList = statementMsg.resultList;

                // for each row
                Iterator iter = resultList.iterator();
                while (iter.hasNext()) {
                    ArrayList row = (ArrayList) iter.next();

                    // for each column
                    Iterator iter2 = row.iterator();
                    while (iter2.hasNext()) {
                        String column = (String) iter2.next();
                        result += column + "\t"; // NOT LOCALIZABLE, escade code
                    } // end of each column

                    result += "\n"; // new line //NOT LOCALIZABLE, escade code
                } // end of each row

                result += "\n"; // new line //NOT LOCALIZABLE, escade code

                if (resultList.size() > 0 && statementMsg.columnList.size() > 0) {
                    resultCount++;
                    String paneTitle = MessageFormat.format(kResult0, new Object[] { new Integer(
                            resultCount) });
                    addSecondaryOutputPanel(new SQLShellResultSetPanel(statementMsg), paneTitle);
                }
            } // end if
            else {
                result += MessageFormat.format(kUpdate0, new Object[] { new Integer(updateCount) })
                        + "\n"; // NOT LOCALIZABLE, escade code
            }

        } catch (Exception ex) {
            result += "\n" + ex.toString() + "\n"; // NOT LOCALIZABLE, escade
            // code
        }

        return result;
    }

    // Overrides Shell for performance purposes.
    protected void executeFile(File file) {
        if (connection == null) {
            resultArea.append(kNotConnected + NEWLINE);
            return;
        }

        String command = "@" + file.getName();
        Shell.CommandLine commandLine = getCommandLine();

        commandLine.append(command);
        resultArea.append(commandLine.getPrompt() + command + NEWLINE);
        buffer.reset();
        commandLine.addHistory(command);
        commandLine.refresh();

        try {
            // open stream
            FileInputStream input = new FileInputStream(file);
            SQLLexerStream stream = new SQLLexerStream(new BufferedReader(new InputStreamReader(
                    input)));
            // actions = new

            // parse it
            boolean done = false;
            while (!done) {
                int tokenID = stream.nextToken();

                // only take care of statement tokens
                switch (tokenID) {
                case SQLLexerStream.TT_STATEMENT:
                    String text = stream.sval;
                    if (text != null) {
                        Debug.trace(text);
                        resultArea.append(commandLine.getPrompt() + text + NEWLINE);
                        String result = execute(text);
                        resultArea.append(result);
                    }
                    break;
                case SQLLexerStream.TT_EOF:
                    done = true;
                    break;
                default:
                    break;
                }
            } // end while
        } catch (FileNotFoundException ex) {
            resultArea.append(ex.toString() + NEWLINE);
        } catch (IOException ex) {
            resultArea.append(ex.toString() + NEWLINE);
        } catch (Shell.ShellException ex) {
            resultArea.append(ex.toString() + NEWLINE);
        } catch (Exception ex) {
            resultArea.append(ex.toString() + NEWLINE);
        }
    }

    // INNER CLASSES
    protected static class SQLShellException extends Shell.ShellException {
        SQLShellException(String msg) {
            super(msg);
        }
    }

}
