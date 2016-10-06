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

package org.modelsphere.jack.baseDb.db;

import java.awt.Component;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.modelsphere.jack.awt.NullFrame;
import org.modelsphere.jack.awt.WaitDialog;
import org.modelsphere.jack.baseDb.db.event.*;
import org.modelsphere.jack.baseDb.db.srtypes.DbtPassword;
import org.modelsphere.jack.baseDb.db.srtypes.UserType;
import org.modelsphere.jack.baseDb.international.LocaleMgr;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.screen.LoginDialog;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.ExceptionMessage;
import org.modelsphere.jack.util.SrVector;

/**
 * This is the facade of the DB modeling framework. All the instances of DbObject belong to a Db
 * instance, which can be easily obtained.
 * 
 * <pre>
 * DbObject dbo;
 * Db db = dbo.getDb();
 * </pre>
 * 
 * Once the Db instance is obtained, we can use it to start or close a transaction.
 * 
 * <pre>
 * try {
 *   db.beginReadTrans();
 *   dbo.getValue();
 *   db.commitTrans();
 * } catch (DbException ex) {
 *   ..
 * }
 * </pre>
 * 
 * @see org.modelsphere.jack.baseDb.db.Db#getDbs()
 */
public abstract class Db {
    public static final String PROPERTY_CONMMAND_HISTORY_SIZE = "CommandHistorySize"; //NOT LOCALIZABLE, property key
    public static final Integer PROPERTY_CONMMAND_HISTORY_SIZE_DEFAULT = new Integer(10);
    public static final int PROPERTY_CONMMAND_HISTORY_SIZE_MAX = 100;
    public static final int PROPERTY_CONMMAND_HISTORY_SIZE_MIN = 1;

    public static final String PROPERTY_REPOSITORY_CONNECTION_STRING = "Repository_ConnectionString"; //NOT LOCALIZABLE, property key
    public static final String PROPERTY_REPOSITORY_CONNECTION_STRING_DEFAULT = "";
    public static final String EXCEPTION_MESSAGE_READ_ONLY_PROJECT = LocaleMgr.db
            .getString("read_only_project");

    private static final String kConnectToRepos = LocaleMgr.message
            .getString("connectToRepository");

    protected static final String PROPERTY_LOGIN_NAME = "LoginName"; //NOT LOCALIZABLE, property key
    protected static final String PROPERTY_LOGIN_NAME_DEFAULT = ""; //NOT LOCALIZABLE, default login

    /* Parm <access> of <beginTrans> */
    public final static int READ_TRANS = 0;
    public final static int WRITE_TRANS = 1;

    /* Return code of <Db.getTransMode> */
    public final static int TRANS_NONE = 0;
    public final static int TRANS_NORMAL = 1;
    public final static int TRANS_UNDO = 2;
    public final static int TRANS_REDO = 3;
    public final static int TRANS_LOAD = 4;
    public final static int TRANS_REFRESH = 5;
    public final static int TRANS_ABORT = 6;

    /* Return code of <DbObject.getTransStatus> */
    public final static int OBJ_UNTOUCHED = 0;
    public final static int OBJ_ADDED = 1;
    public final static int OBJ_MODIFIED = 2;
    public final static int OBJ_REMOVED = 3;

    /* Parm <direction> of <DbRelationN.elements> */
    public final static int ENUM_FORWARD = 0;
    public final static int ENUM_REVERSE = 1;

    /* Operations on a RelationN */
    public final static int ADD_TO_RELN = 1;
    public final static int REMOVE_FROM_RELN = 2;
    public final static int REINSERT_IN_RELN = 3;

    /* Parm <which> of <DbObject.basicGet> */
    public final static int NEW_VALUE = 0;
    public final static int OLD_VALUE = 1;

    /* Parm <histAction> of <commitTrans> */
    public final static int ADD_HIST = 0;
    public final static int CHAIN_HIST = 1;
    public final static int NO_ADD_HIST = 2;

    private final static int MAX_HISTORY_TRANS = getHistoryTrans();

    private static int getHistoryTrans() {
        int historyTrans;
        PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
        historyTrans = (prefs == null) ? 0 : prefs.getPropertyInteger(Db.class,
                PROPERTY_CONMMAND_HISTORY_SIZE, PROPERTY_CONMMAND_HISTORY_SIZE_DEFAULT).intValue();
        return historyTrans;
    }

    private final static int MAX_HISTORY_CMDS = 10000;

    private static final int HUGE_TRANS_CMDS_COUNT = 500;

    /* List of opened Dbs. */
    private static SrVector dbs = new SrVector();

    private static SrVector transListeners = new SrVector();
    private static SrVector updatePassListeners = new SrVector();
    private static SrVector refreshPassListeners = new SrVector();
    private static SrVector undoRedoListeners = new SrVector();
    private static SrVector dbListeners = new SrVector();

    private static boolean logBeginTransCount = (System.getProperty("logBeginTransCount") != null);
    private static int beginTransCount = 0;

    /* List of modified DbObjects in the transaction. */
    private SrVector modifiedObjects = new SrVector(100);

    /* List of DbRelationN in enumeration; all closed at abortTrans */
    private SrVector enumeratedRelNs = new SrVector();

    private DbTransaction rootTransaction;
    private SrVector transHistory = new SrVector(MAX_HISTORY_TRANS + 1);
    private int historyIndex = 0;
    private int transDepth;
    private int transMode = TRANS_NONE;
    private int transAccess;
    private Thread transThread;
    private int transCount = 0;

    private DbRoot dbRoot;
    private DbLoginNode loginNode = null;
    private DbLoginUser dbLogin = null;
    private String loginName = "";
    private int loginType = UserType.ADMIN;

    private boolean hugeTrans = false;
    private boolean terminating = false;

    // DbObject id 
    long nextId = 0;

    /*
     * Create the dbRepository object according to the subclass of Db linked with the application;
     * return null if RAM only application. dbClassNames contains the list of subclassses of Db
     * (except DbRAM)
     */
    private static Db tempdb = null;

    public static Db createDbRepository(final Component owner, final String title,
            final String rootName, final VersionConverter converter, final boolean convert) {
        tempdb = null;
        Runnable runnable = new Runnable() {
            public void run() {
                String[] dbClassNames = new String[] { "DbGemStone", "DbObjectivity",
                        "DbObjectStore" }; // NOT LOCALIZABLE - Class name
                //Db db = null;
                for (int i = 0; i < dbClassNames.length; i++) {
                    try {
                        Class dbClass = Class.forName("org.modelsphere.jack.baseDb.db."
                                + dbClassNames[i]); // NOT LOCALIZABLE - Package name
                        Constructor dbConstructor = dbClass.getDeclaredConstructor(new Class[] {
                                String.class, int.class, boolean.class });
                        tempdb = (Db) dbConstructor.newInstance(new Object[] { rootName,
                                new Integer(converter.getCurrentVersion()),
                                (convert ? Boolean.FALSE : Boolean.TRUE) });
                    } catch (ClassNotFoundException e1) {
                        Debug.trace(e1);
                        //org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(null, e1);
                    } catch (Exception e2) {
                        org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(null,
                                e2);
                    }

                    if (tempdb != null)
                        break;
                }
            }
        };
        WaitDialog.wait(owner, title, kConnectToRepos + ' ' + getConnectionString(), runnable);
        Db db = tempdb;
        tempdb = null;
        if (db == null)
            return null;

        try {
            if (!db.doLogin(owner)) {
                db.terminate();
                return null;
            }
            if (convert)
                db.convertRepository(converter);
            fireDbListeners(db, false);
            return db;
        } catch (Exception e) {
            db.terminate();
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(null, e);
            return null;
        }
    }

    private void convertRepository(VersionConverter converter) throws DbException {
        beginWriteTrans("");
        int oldVersion = dbRoot.getVersion();
        int newVersion = converter.getCurrentVersion();
        if (oldVersion > newVersion)
            throw new DbException(this, LocaleMgr.db.getString("badVersion"));
        Debug.trace("Converting repository..."); // NOT LOCALIZABLE
        converter.setOldVersion(oldVersion);
        DbEnumeration dbEnum = dbRoot.getComponents().elements();
        while (dbEnum.hasMoreElements())
            converter.convertAfterLoad(dbEnum.nextElement());
        dbEnum.close();
        dbRoot.setVersion(newVersion);
        Debug.trace("Convert completed."); // NOT LOCALIZABLE
        commitTrans(NO_ADD_HIST);
    }

    protected boolean doLogin(Component owner) throws DbException {
        String title = LocaleMgr.db.getString("loginRepository");
        String name = PropertiesManager.APPLICATION_PROPERTIES_SET.getPropertyString(Db.class,
                PROPERTY_LOGIN_NAME, PROPERTY_LOGIN_NAME_DEFAULT);
        while (true) {
            LoginDialog dialog = null;
            if (owner instanceof JDialog)
                dialog = new LoginDialog((JDialog) owner, title, name);
            else if (owner instanceof JFrame)
                dialog = new LoginDialog((JFrame) owner, title, name);
            else
                dialog = new LoginDialog(NullFrame.singleton, title, name);
            dialog.setVisible(true);
            if (dialog.cancelled())
                break;
            name = dialog.getUserName();
            DbtPassword password = new DbtPassword(dialog.getPassword());
            beginWriteTrans(""); // may add a login node
            if (loginNode == null) {
                DbEnumeration dbEnum = dbRoot.getComponents().elements(DbLoginNode.metaClass);
                if (dbEnum.hasMoreElements())
                    loginNode = (DbLoginNode) dbEnum.nextElement();
                else {
                    loginNode = new DbLoginNode(dbRoot);
                    new DbLoginUser(loginNode, true);
                }
                dbEnum.close();
            }
            DbEnumeration dbEnum = loginNode.getComponents().elements(DbLoginUser.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbLoginUser login = (DbLoginUser) dbEnum.nextElement();
                if (name.equals(login.getName()) && password.equals(login.getPassword())) {
                    dbLogin = login;
                    loginName = name;
                    loginType = login.getUserType().getValue();
                    PropertiesManager.APPLICATION_PROPERTIES_SET.setProperty(Db.class,
                            PROPERTY_LOGIN_NAME, name);
                    break;
                }
            }
            dbEnum.close();
            commitTrans(NO_ADD_HIST);

            if (dbLogin != null)
                break;
            title = LocaleMgr.db.getString("loginFailed");
        }
        return (dbLogin != null);
    }

    public static Db[] getDbs() {
        Db[] arrayDbs = new Db[dbs.size()];
        dbs.getElements(0, arrayDbs.length, arrayDbs, 0);
        return arrayDbs;
    }

    public static void abortAllTrans() {
        int nb = dbs.size();
        while (--nb >= 0)
            ((Db) dbs.elementAt(nb)).abortTrans();
    }

    public static void terminateAll() {
        int nb = dbs.size();
        while (--nb >= 0)
            /* backward scan because terminate() remove the element from the vector. */
            ((Db) dbs.elementAt(nb)).terminate();
    }

    public static void beginMatching() {
        if (DbObject.matchingMap != null)
            throw new RuntimeException("Matching facility already used"); // NOT LOCALIZABLE RuntimeException
        DbObject.matchingMap = new HashMap();
    }

    public static void endMatching() {
        DbObject.matchingMap = null;
    }

    public static void addDbTransListener(DbTransListener listener) {
        if (transListeners.indexOf(listener) == -1)
            transListeners.addElement(listener);
    }

    public static void removeDbTransListener(DbTransListener listener) {
        transListeners.removeElement(listener);
    }

    public static void addDbUpdatePassListener(DbUpdatePassListener listener) {
        if (updatePassListeners.indexOf(listener) == -1)
            updatePassListeners.addElement(listener);
    }

    public static void removeDbUpdatePassListener(DbUpdatePassListener listener) {
        updatePassListeners.removeElement(listener);
    }

    public static void addDbRefreshPassListener(DbRefreshPassListener listener) {
        if (refreshPassListeners.indexOf(listener) == -1)
            refreshPassListeners.addElement(listener);
    }

    public static void removeDbRefreshPassListener(DbRefreshPassListener listener) {
        refreshPassListeners.removeElement(listener);
    }

    public static void addDbListener(DbListener listener) {
        if (dbListeners.indexOf(listener) == -1)
            dbListeners.addElement(listener);
    }

    public static void removeDbListener(DbListener listener) {
        dbListeners.removeElement(listener);
    }

    public static void addDbUndoRedoListener(DbUndoRedoListener listener) {
        if (undoRedoListeners.indexOf(listener) == -1)
            undoRedoListeners.addElement(listener);
    }

    public static void removeDbUndoRedoListener(DbUndoRedoListener listener) {
        undoRedoListeners.removeElement(listener);
    }

    public final DbRoot getRoot() {
        return dbRoot;
    }

    public final int getTransMode() {
        return transMode;
    }

    public final int getTransAccess() {
        return transAccess;
    }

    public final int getTransCount() {
        return transCount;
    }

    // Note:  This state is unknown during DbRefreshPassListener.beforeRefreshPass()
    public final boolean isHugeTrans() {
        return hugeTrans;
    }

    public final boolean isValid() {
        return (dbRoot != null);
    }

    public final boolean isInEnum() {
        return (enumeratedRelNs.size() != 0);
    }

    final DbTransaction getRootTransaction() {
        return rootTransaction;
    }

    public final String getTransName() {
        return rootTransaction.getName();
    }

    public final void setTransName(String name) {
        rootTransaction.setName(name);
    }

    public final String getTransDescription() {
        return rootTransaction.getDescription();
    }

    public final void setTransDescription(String description) {
        rootTransaction.setDescription(description);
    }

    /*
     * <undo/redo> sets the transaction mode to TRANS_UNDO/TRANS_REDO. DbObject.load sets the
     * transaction mode to TRANS_LOAD.
     */
    final void setTransMode(int i) {
        transMode = i;
    }

    public final DbLoginNode getLoginNode() {
        return loginNode;
    }

    public final DbLoginUser getLogin() {
        return dbLogin;
    }

    public final String getLoginName() {
        return loginName;
    }

    public final int getLoginType() {
        return loginType;
    }

    // Change the password of the current login.
    // This is the only write operation allowed for a GUEST login.
    public final void changePassword(String oldPass, String newPass) throws DbException {
        if (transMode != TRANS_NONE)
            throw new RuntimeException("changePassword during a transaction"); // NOT LOCALIZABLE RuntimeException
        beginTransAux(WRITE_TRANS, "");
        if (!dbLogin.getPassword().equals(new DbtPassword(oldPass)))
            throw new DbException(this, LocaleMgr.db.getString("oldPassMismatch"));
        dbLogin.basicSet(DbLoginUser.fPassword, new DbtPassword(newPass));
        commitTrans(NO_ADD_HIST);
    }

    public final void beginReadTrans() throws DbException {
        beginTrans(READ_TRANS, "");
    }

    public final void beginWriteTrans(String name) throws DbException {
        beginTrans(WRITE_TRANS, name);
    }

    public final void beginTrans(int access) throws DbException {
        beginTrans(access, "");
    }

    public final void beginTrans(int access, String name) throws DbException {
        if (loginType == UserType.GUEST)
            access = READ_TRANS;
        beginTransAux(access, name);
    }

    private synchronized void beginTransAux(int access, String name) throws DbException,
            ExceptionMessage {

        if (transMode == Db.TRANS_NONE) {
            if (!isValid()) {
                //throw new RuntimeException("Invalid Db");  // NOT LOCALIZABLE RuntimeException
                ApplicationContext.getFocusManager().setNullProject();
                ApplicationContext.getFocusManager().update();
                return;
            }

            DBMSBeginTrans(access);
            rootTransaction = new DbTransaction(this, name);
            setTransMode(TRANS_NORMAL);
            transAccess = access;
            transDepth = 1;
            transThread = Thread.currentThread();

            fireDbTransListeners(false);

            if (logBeginTransCount) {
                beginTransCount++;
                System.out.println(beginTransCount);
            }
        } else {
            if (transThread != Thread.currentThread())
                throw new RuntimeException("beginTrans in " + Thread.currentThread().getName() + // NOT LOCALIZABLE RuntimeException
                        " while a transaction is active in " + transThread.getName()); // NOT LOCALIZABLE RuntimeException
            if (transDepth != 0) /* if not called from an UpdateListener */
                transDepth++;
        }
    }

    public final void commitTrans() throws DbException {
        commitTrans(ADD_HIST);
    }

    public final void commitTrans(int histAction) throws DbException {
        checkTrans();
        if (transDepth == 0) /* if called from an UpdateListener */
            return;
        transDepth--;
        if (transDepth != 0)
            return;

        if (rootTransaction.getNbCommands() != 0) {
            hugeTrans = rootTransaction.getNbCommands() >= HUGE_TRANS_CMDS_COUNT
                    && transMode != Db.TRANS_LOAD && dbRoot != null
                    && dbRoot.getTransStatus() != Db.OBJ_ADDED;
            if (transMode == Db.TRANS_NORMAL) { /* no UpdateListener if Undo/Redo */
                rootTransaction.fireDbUpdateListeners();
            }

            if (transMode == Db.TRANS_NORMAL) {
                int nb = modifiedObjects.size();
                for (int i = 0; i < nb; i++)
                    ((DbObject) modifiedObjects.elementAt(i)).clusterGraph();
            }
            DBMSCheckPoint(rootTransaction.getDescription());
            transCount++;
            if (transMode == Db.TRANS_NORMAL && histAction != NO_ADD_HIST)
                addToHistory(rootTransaction, (histAction == CHAIN_HIST));

            DbTransaction refreshTrans = rootTransaction;
            rootTransaction = new DbTransaction(this, refreshTrans.getName());
            setTransMode(TRANS_REFRESH);
            transAccess = Db.READ_TRANS;
            fireDbRefreshPassListeners(false);
            fireCallOnceRefreshListeners();
            refreshTrans.fireDbRefreshListeners();
            fireDbRefreshPassListeners(true);
        }
        DBMSCommitTrans("");

        if (enumeratedRelNs.size() != 0)
            throw new RuntimeException("Enumerations opened at commit time"); // NOT LOCALIZABLE RuntimeException
        resetAllModifiedObjects();
        rootTransaction = null;
        setTransMode(TRANS_NONE); /* must be the last statement. */
        fireDbTransListeners(true);
        hugeTrans = false;
    }

    /*
     * abortTrans cannot throw any exception; it must always recuperate from any condition.
     */
    public final void abortTrans() {
        if (transMode == Db.TRANS_NONE)
            return;
        setTransMode(TRANS_ABORT);
        DBMSAbortTrans();

        /* Close all opened emumerators. */
        int nb = enumeratedRelNs.size();
        while (--nb >= 0) {
            ((DbRelationN) enumeratedRelNs.elementAt(nb)).closeAllEnums();
        }
        resetAllModifiedObjects();
        rootTransaction = null;
        setTransMode(TRANS_NONE); /* must be the last statement. */
        fireDbTransListeners(true);
        hugeTrans = false;
    }

    final void begin(DbRoot dbRoot) {
        this.dbRoot = dbRoot;
        dbs.addElement(this);
    }

    public final void terminate() {
        terminating = true;
        abortTrans();
        DBMSTerminate();
        dbRoot = null;
        dbLogin = null;
        transHistory = null;
        dbs.removeElement(this);
        fireDbListeners(this, true);
    }

    public final boolean isTerminating() {
        return terminating;
    }

    public final void undo() throws DbException {
        if (transMode != TRANS_NONE)
            throw new RuntimeException("Undo during a transaction"); // NOT LOCALIZABLE RuntimeException
        if (historyIndex == 0)
            return;
        DbTransaction trans = (DbTransaction) transHistory.elementAt(historyIndex - 1);
        beginWriteTrans("");
        if (trans.getDescription().length() != 0)
            setTransDescription(MessageFormat.format(LocaleMgr.db.getString("Undo0"),
                    new Object[] { trans.getDescription() }));
        setTransMode(TRANS_UNDO);
        trans.rollBack();
        checkUndoRedoConflicts();
        commitTrans();
        historyIndex--;
        fireDbUndoRedoListeners();
    }

    public final void redo() throws DbException {
        if (transMode != TRANS_NONE)
            throw new RuntimeException("Redo during a transaction"); // NOT LOCALIZABLE RuntimeException
        if (historyIndex == transHistory.size())
            return;
        DbTransaction trans = (DbTransaction) transHistory.elementAt(historyIndex);
        beginWriteTrans("");
        if (trans.getDescription().length() != 0)
            setTransDescription(MessageFormat.format(LocaleMgr.db.getString("Redo0"),
                    new Object[] { trans.getDescription() }));
        setTransMode(TRANS_REDO);
        trans.rollForward();
        checkUndoRedoConflicts();
        commitTrans();
        historyIndex++;
        fireDbUndoRedoListeners();
    }

    public final void resetHistory() {
        transHistory.removeElements(0, transHistory.size());
        historyIndex = 0;
        fireDbUndoRedoListeners();
    }

    public final String getUndoTransName() {
        if (historyIndex == 0)
            return null;
        return ((DbTransaction) transHistory.elementAt(historyIndex - 1)).getName();
    }

    public final String getUndoTransName(int index) {
        if ((historyIndex - index) <= 0)
            return null;
        return ((DbTransaction) transHistory.elementAt(historyIndex - index - 1)).getName();
    }

    public final String getRedoTransName() {
        if (historyIndex == transHistory.size())
            return null;
        return ((DbTransaction) transHistory.elementAt(historyIndex)).getName();
    }

    public final String getRedoTransName(int index) {
        if ((historyIndex + index) >= transHistory.size())
            return null;
        return ((DbTransaction) transHistory.elementAt(historyIndex + index)).getName();
    }

    private void addToHistory(DbTransaction trans, boolean chain) {
        // remove all redo transactions before adding a new transaction
        transHistory.removeElements(historyIndex, transHistory.size() - historyIndex);
        if (chain) {
            DbTransaction prevTrans = (DbTransaction) transHistory.elementAt(historyIndex - 1);
            prevTrans.concat(trans);
        } else {
            transHistory.addElement(trans);
            historyIndex++;
        }
        // keep the last transactions up to MAX_HISTORY_CMDS commands or MAX_HISTORY_TRANS transactions
        int nbCmds = 0;
        int nbTrans = historyIndex;
        while (nbTrans > 0 && nbCmds <= MAX_HISTORY_CMDS) {
            nbTrans--;
            nbCmds += ((DbTransaction) transHistory.elementAt(nbTrans)).getNbCommands();
        }
        if (historyIndex - nbTrans > MAX_HISTORY_TRANS)
            nbTrans++;
        transHistory.removeElements(0, nbTrans);
        historyIndex -= nbTrans;
        fireDbUndoRedoListeners();
    }

    private void checkUndoRedoConflicts() throws DbException {
        int nb = modifiedObjects.size();
        for (int i = 0; i < nb; i++)
            ((DbObject) modifiedObjects.elementAt(i)).checkUndoRedoConflicts();
    }

    public abstract String getDBMSName();

    // If <description> is empty, the transaction is not logged.
    abstract void DBMSBeginTrans(int access) throws DbException;

    abstract void DBMSCommitTrans(String description) throws DbException;

    abstract void DBMSAbortTrans();

    abstract void DBMSTerminate();

    // Overridden: commits the transaction and starts a new one (read mode) to allow RefreshListeners to access data.
    // If <description> is empty, the transaction is not logged.
    void DBMSCheckPoint(String description) throws DbException {
        DBMSCommitTrans(description);
        DBMSBeginTrans(Db.READ_TRANS);
    }

    DbRelationN createDbRelationN(DbObject parent, MetaRelationN metaRelation) {
        return parent.createRAMRelN(metaRelation);
    }

    boolean hasFetch() {
        return true;
    }

    public abstract void fetch(Object obj) throws DbException;

    abstract void dirty(Object obj) throws DbException;

    public abstract void cluster(Object obj, Object container) throws DbException;

    final void checkTrans() {
        if (transMode == Db.TRANS_NONE)
            throw new RuntimeException("Not in a transaction"); // NOT LOCALIZABLE RuntimeException
    }

    final void checkWriteTrans() throws DbException {
        checkTrans();
        if (transAccess != Db.WRITE_TRANS) {
            if (loginType == UserType.GUEST)
                throw new DbException(this, LocaleMgr.db.getString("ReadOnlyRepository"));
            else
                throw new RuntimeException("Not in a write transaction"); // NOT LOCALIZABLE RuntimeException
        }
    }

    final void set(DbObject dbo, MetaField metaField, Object oldValue, Object value, int oldIndex,
            int index) {
        if (transMode != TRANS_ABORT) {
            DbSetCommand cmd = new DbSetCommand(dbo, metaField, oldValue, value, oldIndex, index);
            rootTransaction.addCommand(cmd);
        }
    }

    final void setRelationNN(DbObject dbo, MetaRelationN relation, DbObject neighbor, int op,
            int index, int oppositeIndex) {
        if (transMode != TRANS_ABORT) {
            DbSetRelationNNCommand cmd = new DbSetRelationNNCommand(dbo, relation, neighbor, op,
                    index, oppositeIndex);
            rootTransaction.addCommand(cmd);
        }
    }

    final void reinsert(DbRelationN dbRelN, DbObject neighbor, int oldIndex, int newIndex) {
        if (transMode != TRANS_ABORT) {
            DbReinsertCommand cmd = new DbReinsertCommand(dbRelN, neighbor, oldIndex, newIndex);
            rootTransaction.addCommand(cmd);
        }
    }

    private final void fireDbTransListeners(boolean end) {
        int nb = transListeners.size();
        while (--nb >= 0) {
            DbTransListener listener = (DbTransListener) transListeners.elementAt(nb);
            if (end)
                listener.dbTransEnded(this);
            else
                listener.dbTransBegun(this);
        }
    }

    final void fireDbUpdatePassListeners(boolean after) throws DbException {
        int nb = updatePassListeners.size();
        while (--nb >= 0) {
            DbUpdatePassListener listener = (DbUpdatePassListener) updatePassListeners
                    .elementAt(nb);
            if (after)
                listener.afterUpdatePass(this);
            else
                listener.beforeUpdatePass(this);
        }
    }

    private final void fireDbRefreshPassListeners(boolean after) throws DbException {
        int nb = refreshPassListeners.size();
        while (--nb >= 0) {
            DbRefreshPassListener listener = (DbRefreshPassListener) refreshPassListeners
                    .elementAt(nb);
            if (after)
                listener.afterRefreshPass(this);
            else
                listener.beforeRefreshPass(this);
        }
    }

    protected static final void fireDbListeners(Db db, boolean terminated) {
        int nb = dbListeners.size();
        while (--nb >= 0) {
            DbListener listener = (DbListener) dbListeners.elementAt(nb);
            if (terminated)
                listener.dbTerminated(db);
            else
                listener.dbCreated(db);
        }
    }

    private final void fireDbUndoRedoListeners() {
        int nb = undoRedoListeners.size();
        while (--nb >= 0) {
            DbUndoRedoListener listener = (DbUndoRedoListener) undoRedoListeners.elementAt(nb);
            listener.refresh(this);
        }
    }

    final void addEnumeratedRelN(DbRelationN dbRelN) {
        enumeratedRelNs.addElement(dbRelN);
    }

    final void removeEnumeratedRelN(DbRelationN dbRelN) {
        enumeratedRelNs.removeElement(dbRelN);
    }

    final void addModifiedObject(DbObject dbo) {
        modifiedObjects.addElement(dbo);
    }

    final void throwDbDataConflictException() throws DbException {
        throw new DbException(this, LocaleMgr.db.getString("DataConflict"));
    }

    final void throwDbUndoRedoConflictException() throws DbException {
        String opn = LocaleMgr.db.getString(transMode == TRANS_REDO ? "Redo" : "Undo");
        String msg = LocaleMgr.db.getString("UndoRedoConflict");
        throw new DbException(this, MessageFormat.format(msg, new Object[] { opn }));
    }

    /* Reset transStatus and free oldValues of all objects in the transaction. */
    private final void resetAllModifiedObjects() {
        int nb = modifiedObjects.size();
        for (int i = 0; i < nb; i++)
            ((DbObject) modifiedObjects.elementAt(i)).resetTransStatus();
        modifiedObjects.removeElements(0, nb);
    }

    /* Fire the dbRefreshListeners attached to dbObjects that have <when = CALL_ONCE>. */
    private final void fireCallOnceRefreshListeners() throws DbException {
        int nb = modifiedObjects.size();
        for (int i = 0; i < nb; i++) {
            DbObject dbo = (DbObject) modifiedObjects.elementAt(i);
            if (dbo.hasDbRefreshListeners() && !dbo.isAddedAndRemoved()) {
                DbUpdateEvent event = new DbUpdateEvent(dbo, (MetaField) null);
                dbo.fireDbRefreshListeners(event, DbRefreshListener.CALL_ONCE);
            }
        }
    }

    public static final String getConnectionString() {
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        String connectionString = preferences.getPropertyString(Db.class,
                PROPERTY_REPOSITORY_CONNECTION_STRING,
                PROPERTY_REPOSITORY_CONNECTION_STRING_DEFAULT);
        return connectionString;
    }

}
