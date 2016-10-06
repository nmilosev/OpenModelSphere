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

package org.modelsphere.sms.or.features.dbms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectScope;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectSelection;
import org.modelsphere.jack.srtool.plugins.generic.dbms.UserInfo;
import org.modelsphere.jack.srtool.reverse.Actions;
import org.modelsphere.jack.srtool.reverse.InputFileParser;
import org.modelsphere.jack.srtool.reverse.SQLLexerStream;
import org.modelsphere.jack.srtool.reverse.TargetObjectBuilder;
import org.modelsphere.jack.srtool.reverse.engine.ReverseBuilder;
import org.modelsphere.jack.srtool.services.ConnectionMessage;
import org.modelsphere.jack.srtool.services.ServiceProtocolList;
import org.modelsphere.jack.srtool.services.StatementMessage;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.international.LocaleMgr;

public class DBMSReverseWorker extends Worker {
    private static final String kReverseJobSynchro = LocaleMgr.misc.getString("ReverseInSynchro");
    private static final String kReverse = LocaleMgr.misc.getString("Reverse");
    private static final String kError = LocaleMgr.misc.getString("Error_");
    private static final String kSQLStatementError = LocaleMgr.misc.getString("ErrorStatement_");
    private static final String kSQLError = LocaleMgr.misc.getString("ErrorSQL_");
    private static final String kExtractingCatalog = LocaleMgr.misc.getString("ExtractingCatalog");

    // Possible mode for columns order in the extract file
    // MODE_COLUMN_NAME_BEFORE_VALUE: SELECT 'sr_occurrence_type tablespace'...
    // (See Oracle extraction file)
    // MODE_COLUMN_NAME_AFTER_VALUE: SELECT 'user' sr_occurrence, ... (See
    // Informix extraction file)
    public static final int MODE_COLUMN_NAME_BEFORE_VALUE = 0;
    public static final int MODE_COLUMN_NAME_AFTER_VALUE = 1;

    private volatile boolean done = false;

    private int sqlMode = MODE_COLUMN_NAME_BEFORE_VALUE;
    protected boolean dumpSrxFile = false; // should be usefull for debug
    // purpose only

    private FileWriter srxFileWriter;
    private DBMSReverseOptions options;
    private Actions actions;

    public DBMSReverseWorker(DBMSReverseOptions options, Actions actions) {
        this(options, actions, MODE_COLUMN_NAME_BEFORE_VALUE);
    }

    // sqlmode values: MODE_COLUMN_NAME_BEFORE_VALUE (default),
    // MODE_COLUMN_NAME_AFTER_VALUE
    public DBMSReverseWorker(DBMSReverseOptions options, Actions actions, int sqlmode) {
        if (options == null)
            throw new NullPointerException();

        sqlMode = sqlmode;
        this.options = options;
        this.actions = actions;
    }

    protected String getJobTitle() {
        if (options.synchro) {
            return kReverseJobSynchro;
        } else {
            return kReverse;
        }
    }

    protected static String g_srxFilename = null;

    protected String getSrxFileName() {
        if (g_srxFilename == null) {
            String ext = "srx"; // NOT LOCALIZABLE, file extension
            g_srxFilename = System.getProperty("user.dir") + System.getProperty("file.separator")
                    + "reverse." + ext; // NOT
            // LOCALIZABLE,
            // file
            // name
        }

        return g_srxFilename;
    }

    // perform the reverse
    public void runJob() throws Exception {
        if (actions instanceof ReverseBuilder)
            ((ReverseBuilder) actions).setController(getController());

        getController().println();
        getController().println(options.toString());

        try {
            if (dumpSrxFile || Debug.isDebug()) {
                if (!options.fromExtractFile) {
                    String srxFilename = getSrxFileName();
                    String pattern = LocaleMgr.misc.getString("GENERATING");
                    String msg = MessageFormat.format(pattern, new Object[] { srxFilename });
                    getController().println(msg);
                    srxFileWriter = new FileWriter(srxFilename, false);
                    writeHeader(srxFileWriter);
                }
            }
        } catch (IOException e) {
            srxFileWriter = null;
        }

        // process data - do reverse
        try {
            makeSQLRequests();
        } finally {
            closeSrxFile();
            if (actions instanceof ReverseBuilder)
                ((ReverseBuilder) actions).buildORDiagram();
            actions = null;
            options = null;
            srxFileWriter = null;
        }

    }

    protected void writeHeader(FileWriter srxFileWriter) {
    } // end writeHeader

    private void closeSrxFile() {
        try {
            if (srxFileWriter != null)
                srxFileWriter.close();
        } catch (Exception e) {
        }
    }

    private void executeSQL(ConnectionMessage connection, String sql, Writer writer)
            throws SQLException, IOException {
        String errMsg = null;
        StatementMessage statementMsg = null;

        // Build the message and send it to the statement service
        String addressIP = ServiceProtocolList.getServerIP();
        int port = ServiceProtocolList.STATEMENT_SERVICE;

        try {
            Socket s = new Socket(addressIP, port);
            ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
            statementMsg = new StatementMessage(connection.connectionId, sql);

            output.writeObject(statementMsg);

            ObjectInputStream input = new ObjectInputStream(s.getInputStream());
            statementMsg = (StatementMessage) input.readObject();
            if (dumpSrxFile && statementMsg != null && Debug.isDebug())
                dumpSRXToFile(statementMsg.resultList);
            errMsg = statementMsg.errorMessage;
        } catch (IOException ex) {
            errMsg = ex.toString();
        } catch (ClassNotFoundException ex) {
            errMsg = ex.toString();
        }

        ArrayList list = statementMsg.resultList;
        int len = list.size();
        for (int i = 0; i < len; i++) {
            ArrayList sublist = (ArrayList) list.get(i);
            int sublen = sublist.size();
            for (int j = 0; j < sublen; j++) {
                String s = (String) sublist.get(j);
                writer.write(s);
            }
        }
        if (errMsg != null) {
            Controller controller = getController();
            controller.println();
            controller.println(kError);
            controller.println("  " + errMsg);
            controller.println("\"" + statementMsg.statement + "\""); // NOT
            // LOCALIZABLE
            controller.println();
            controller.incrementErrorsCounter();
        }
    } // end executeSQL()

    private void makeSQLRequests() throws Exception {
        try {
            if ((options != null) && (options.root != null))
                actions.init(options.root.getProject(), options);

            final Reader reader;
            Writer writer;

            if (options.fromExtractFile) {
                reader = new FileReader(options.extractFilename);
                writer = null;
            } else {
                // open piped reader & writer
                PipedWriter src = new PipedWriter();
                reader = new PipedReader(src);
                writer = new PrintWriter(src);
            }

            TargetObjectBuilder builder = new TargetObjectBuilder(actions);
            final InputFileParser inputReader = new InputFileParser(builder);
            inputReader.setControler(getController());

            SQLLexerStream stream = null;
            if (!options.fromExtractFile) {
                // get SQL file
                final String requestFile = options.getRequestFile();
                // open stream
                URL url = ReverseToolkitPlugin.getToolkit().getClass().getResource(requestFile);
                stream = new SQLLexerStream(new BufferedReader(new InputStreamReader(url
                        .openStream())));
            }

            // this thread will read stream until meets EOL (ie. when writer is
            // closed)
            final Controller controller = getController();
            Thread th = new Thread("reader") { // NOT LOCALIZABLE - Debug
                // information
                public void run() {
                    try {
                        inputReader.readInputFile(reader);
                    } catch (Exception e) {
                        controller.processException(e);
                    }
                }
            };
            th.start();

            if (!options.fromExtractFile) {
                // parse it
                done = false;
                controller.println(kExtractingCatalog);
                startNewThread(new Thread() {
                    public void run() {
                        try {
                            getController().println();
                            while (!done && controller.checkPoint()) {
                                controller.print(". "); // NOT LOCALIZABLE
                                sleep(200);
                            }
                        } catch (InterruptedException ex) {
                            Debug.trace(ex.toString());
                        }
                    }
                });
                while (!done && getController().checkPoint()) {
                    int tokenID = stream.nextToken();
                    // only take care of statement tokens
                    switch (tokenID) {
                    case SQLLexerStream.TT_STATEMENT:
                        String request = stream.sval;
                        if (request != null) {
                            try {
                                processRequest(request, writer);
                                System.gc();
                            } catch (SQLException ex) {
                                final String errorMsg = ex.toString();
                                controller.println(kSQLError + "  " + ex.toString()); // NOT LOCALIZABLE
                                controller.incrementErrorsCounter();
                            }
                        }
                        break;
                    case SQLLexerStream.TT_EOF:
                        done = true;
                        break;
                    default:
                        break;
                    }
                }
            } // end if

            // close writer: the reading thread terminates.
            if (writer != null)
                writer.close();

            // wait until input stream is completely read
            try {
                while (th.isAlive()) { // like th.join() but give more
                    // performance
                    sleep(500);
                }
            } catch (InterruptedException ex) {
                Debug.trace(ex.toString());
            }

            if (getController().getState() == Controller.STATE_ABORTING
                    || getController().getState() == Controller.STATE_ABORTED)
                actions.abort();
            else if (getController().getState() != Controller.STATE_ERROR)
                actions.exit();

        } catch (IOException ex) {
            Debug.trace(ex.toString());
        }
    }

    // Determines if the specified request must be executed depending of the
    // scope selection
    private boolean toBeExecuted(String request) {
        boolean executeIt = true;
        String occurrenceType = null;
        switch (sqlMode) {
        case MODE_COLUMN_NAME_BEFORE_VALUE:
            // Try to find "sr_occurrence_type"
            int index = request.indexOf(InputFileParser.kOccurrenceTypeAlt);

            // If found, go at the end of the token
            if (index != -1) {
                index += InputFileParser.kOccurrenceTypeAlt.length();
            } else { // If not found, try to find "sr_occurrence" at least
                // // NOT LOCALIZABLE
                index = request.indexOf(InputFileParser.kOccurrenceType);
                if (index != -1) {
                    // If found, go at the end of the token
                    index += InputFileParser.kOccurrenceType.length();
                }
            }
            // if either "sr_occurrence_type" or "sr_occurrence" found
            if (index != -1) {
                // Skip each whitespace
                while (Character.isWhitespace(request.charAt(index)))
                    index++;

                // Get the occurrence type (one of: tablespace, table, view,
                // etc.)
                occurrenceType = request.substring(index, request.indexOf('\'', index));
            }
            break;
        case MODE_COLUMN_NAME_AFTER_VALUE:
            // Try to find "sr_occurrence"
            int idx = request.indexOf('\'');

            if (idx != -1) {
                String subText = request.substring(idx + 1, request.length());
                idx = subText.indexOf('\'');
                if (idx != -1) {
                    occurrenceType = subText.substring(0, idx);
                    occurrenceType = occurrenceType.trim();
                }
            }
            break;
        }

        if (occurrenceType != null) {
            // verify that the concept is selected
            ObjectScope[] scope = options.getObjectsScope();
            ObjectScope concept = ObjectScope.findConceptInObjectScopeWithMappingName(scope,
                    occurrenceType);
            executeIt = ReverseToolkitPlugin.getToolkit().executeSQLRequest(options, concept);
        }

        return executeIt;
    }

    private void processRequest(String request, Writer writer) throws SQLException, IOException {
        if (toBeExecuted(request)) {
            ArrayList requests = customize(request);
            Iterator iter = requests.iterator();
            while (iter.hasNext()) {
                String sql = (String) iter.next();
                if (sql != null) {
                    executeSQL(options.getConnection(), sql, writer);
                }
            }
        }
    }

    // customize the WHERE clause, build the $scope tag
    // The request may be splitted in many sql request (according to
    // MAX_DBMS_LIST_ELEMENTS).
    // The returned ArrayList contains all the String corresponding to each of
    // these sql request.
    private ArrayList customize(String request) {
        ArrayList requests = new ArrayList();

        // find the "$scope" tag...
        int index = request.lastIndexOf("$scope"); // NOT LOCALIZABLE - sql tag
        // (if found), insert the "WHERE" clause in the statement
        if (index == -1) {
            requests.add(request);
            return requests;
        }

        String conceptName, scopeTag;
        int indexEnd = request.indexOf(')', index) + 1;
        scopeTag = request.substring(index, indexEnd);
        conceptName = scopeTag.substring(scopeTag.indexOf('_') + 1, scopeTag.indexOf('(')).trim();

        // get the 3 parameters of the "$scope" tag
        String parameters = scopeTag.substring(scopeTag.indexOf('(') + 1, scopeTag.indexOf(')'));
        StringTokenizer st = new StringTokenizer(parameters, ",");
        String owner = st.nextToken().trim();
        String field = st.nextToken().trim();
        String keyword = st.nextToken().trim();

        ObjectScope[] scope = options.getObjectsScope();
        ObjectScope concept = ObjectScope.findConceptInObjectScopeWithMappingName(scope,
                conceptName);
        ObjectScope userScope = ObjectScope.findConceptInObjectScopeWithMetaClass(scope,
                DbORUser.metaClass);

        // Determine the prefix and suffix to be added to all sql requests
        String prefix = request.substring(0, index);
        String suffix = request.substring(indexEnd, request.length());
        boolean appendWHERE = keyword.equalsIgnoreCase("pref_where"); // NOT
        // LOCALIZABLE
        // - tag
        boolean appendAND = keyword.equalsIgnoreCase("suf_and"); // NOT
        // LOCALIZABLE
        // - tag
        String sql = null;
        String filter = null;

        filter = getScopeFilter(concept, userScope, owner, field);
        if (filter != null) {
            sql = prefix;
            if (filter.length() > 0) {
                if (appendWHERE)
                    sql += " WHERE "; // NOT LOCALIZABLE - sql tag
                sql += filter;
                if (appendAND)
                    sql += " AND "; // NOT LOCALIZABLE - sql tag
            }
            sql += suffix;
            requests.add(sql);
        }

        return requests;
    }

    // if return null, the scope is empty (skip the request)
    private String getScopeFilter(ObjectScope concept, ObjectScope userScope, String owner,
            String field) {
        String filter = "";

        // For the UnOwned Objects...
        if (!concept.isOwnedObject && owner.equals("?")) { // NOT LOCALIZABLE -
            // sql tag
            int filtermode = getOptimizedSQLFilterMode(concept, null);
            if (filtermode == FILTER_SKIP) {
                return null;
            } else if (filtermode == FILTER_NONE) {
                return "";
            }
            String INclause = getINClause(concept.occurences, filtermode == FILTER_IN);
            if (INclause != null && INclause.length() > 0) {
                filter += "("; // NOT LOCALIZABLE - sql tag
                filter += field;
                filter += INclause;
                filter += ")"; // NOT LOCALIZABLE - sql tag
            }
            return filter;
        }

        // For the Owned Objects
        boolean userAdded = false;
        String userClause = "";
        for (int j = 0; j < userScope.occurences.size(); j++) {
            userClause = getUserClause(concept, (ObjectSelection) userScope.occurences.get(j),
                    owner, field);
            if (userClause == null)
                continue;
            if (userAdded)
                filter += " OR ";
            filter += userClause;
            userAdded = true;
        }

        // if no user in scope, don't send the request
        if (!userAdded)
            return null;

        if (filter.length() > 0)
            filter = " (" + filter + ") ";

        return filter;
    }

    private String getINClause(ArrayList selections, boolean in) {
        String list = "";
        boolean first = true;
        ObjectSelection selection = null;
        for (int i = 0; i < selections.size(); i++) {
            selection = (ObjectSelection) selections.get(i);
            if ((in && selection.getIsSelected()) || (!in && !selection.getIsSelected())) {
                if (!first)
                    list += ","; // NOT LOCALIZABLE - sql tag
                list += "\'" + selection.name + "\'"; // NOT LOCALIZABLE - sql
                // tag
                first = false;
            }
        }
        if (list.length() > 0) {
            list = (in ? "" : " NOT") + " IN (" + list + ") "; // NOT
            // LOCALIZABLE -
            // sql tag
        }
        return list;
    }

    // if return null, do not add the user to the scope
    private String getUserClause(ObjectScope concept, ObjectSelection userselection, String owner,
            String field) {
        if (!userselection.getIsSelected())
            return null;

        String clause = "";
        boolean ownerspecified = !owner.equals("?")
                && !DBMSUtil.NULL_USER.equals(userselection.name);

        // customize with the owner tag
        if (ownerspecified)
            clause += owner + " = \'" + userselection.name + "\'"; // NOT
        // LOCALIZABLE
        // - sql tag

        int filtermode = getOptimizedSQLFilterMode(concept, userselection);
        if (filtermode == FILTER_SKIP) {
            return null;
        } else if (filtermode == FILTER_NONE) {
            if (!ownerspecified)
                return "";
            return "(" + clause + ")";
        }

        // customize with selected occurences
        if (!field.equals("?")) { // NOT LOCALIZABLE - sql tag
            if (ownerspecified)
                clause += " AND "; // NOT LOCALIZABLE - sql tag
            clause += field + " ";
            for (int i = 0; i < concept.occurences.size(); i++) {
                UserInfo userInfo = (UserInfo) concept.occurences.get(i);
                if (!userInfo.username.equals(userselection.name))
                    continue;
                clause += getINClause(userInfo.occurences, filtermode == FILTER_IN);
                break;
            }
        }

        return "(" + clause + ")"; // NOT LOCALIZABLE - sql tag
    }

    private static final int FILTER_NONE = 0; // Do not customize the request
    private static final int FILTER_SKIP = 1; // Skip the request
    private static final int FILTER_IN = 2; // Use the IN Sql clause
    private static final int FILTER_NOT_IN = 3; // Use the NOT IN Sql clause

    // if userselection == null, evaluate according to all user
    private int getOptimizedSQLFilterMode(ObjectScope concept, ObjectSelection userselection) {
        if (concept == null)
            return FILTER_NONE;

        ObjectSelection occurence = null;
        int selectedcount = 0;
        int notselectedcount = 0;
        for (int i = 0; i < concept.occurences.size(); i++) {
            if (concept.isOwnedObject) {
                UserInfo userInfo = (UserInfo) concept.occurences.get(i);
                for (int x = 0; x < userInfo.occurences.size(); x++) {
                    if (userselection != null && !userselection.name.equals(userInfo.username))
                        continue;
                    occurence = (ObjectSelection) userInfo.occurences.get(x);
                    if (occurence.getIsSelected())
                        selectedcount++;
                    else
                        notselectedcount++;
                }
            } else {
                occurence = (ObjectSelection) concept.occurences.get(i);
                if (occurence.getIsSelected())
                    selectedcount++;
                else
                    notselectedcount++;
            }
        }

        if (selectedcount == 0)
            return FILTER_SKIP;
        if (notselectedcount == 0)
            return FILTER_NONE;
        if (selectedcount > notselectedcount)
            return FILTER_NOT_IN;
        return FILTER_IN;
    }

    // synchronized ??? must block other threads to close srxFileWriter (if
    // exception)
    private void dumpSRXToFile(ArrayList srxResult) {
        if (srxResult == null || srxFileWriter == null)
            return;

        String eol = System.getProperty("line.separator");
        try {
            Iterator iter1 = srxResult.iterator();
            while (iter1.hasNext()) {
                String result = new String("");
                ArrayList srxObject = (ArrayList) iter1.next();
                if (srxObject != null) {
                    Iterator iter2 = srxObject.iterator();
                    while (iter2.hasNext()) {
                        Object line = iter2.next();
                        if (line != null)
                            result = result.concat(line.toString());
                    }
                }
                srxFileWriter.write(result.concat(eol));
            }
        } catch (IOException e) {
        }
    }

}
