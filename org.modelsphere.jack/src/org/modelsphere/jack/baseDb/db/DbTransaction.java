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

import org.modelsphere.jack.baseDb.db.event.DbUpdateListenerCall;
import org.modelsphere.jack.util.SrVector;

/**
 * Implements the transaction concept for the Db framework. A transaction starts with a
 * <code>beginReadTrans()</code> or a <code>beginWriteTrans()</code>. A transactions terminates with
 * a <code>commitTrans()</code> (successful completion) or with a a <code>abortTrans()</code>
 * (unsuccessful completion). All the accesses to DbObject's values (either read accesses or writer
 * accesses) must occur within a transaction, otherwise a DbException is thrown. <br>
 * <br>
 * A transaction is ACID (Atomic, Concurrent, Isolated and Durable). Atomic means the transaction is
 * either totally committed or totally rolled back. Concurrent means multi-user access is handled by
 * the transaction framework. Isolated means all the changes to the DbObject are not visible to
 * other users until the transaction is committed. Durable means the transaction are stored in a
 * transaction history, and can be undone or redone. <br>
 * <br>
 * A transaction contains an ordered collection of all the commands performed. When the transaction
 * starts, the command list is empty. An invocation to a setter actually creates a command and add
 * it to the command list. At the commit time, all the commands are executed. If an error occurs
 * when executing a command, and the previously executed commands are rolled back, in order to leave
 * the DbObject instance in the same state it was when the transaction begun. <br>
 * <br>
 * Transactions are managed by the Db instance. Transaction commands can be executed in reverse
 * order (undo operation).
 * 
 * @see Db#beginReadTrans()
 * @see Db#beginWriteTrans(String)
 * @see Db#commitTrans()
 * @see Db#abortTrans()
 * 
 * @see Db#undo()
 * @see Db#redo()
 */
public class DbTransaction {

    private SrVector commands = new SrVector(100);

    private Db db;

    private String name;

    private String description = "";

    DbTransaction(Db db, String name) {
        this.db = db;
        this.name = name;
    }

    /**
     * @return The localized transaction name.
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name
     *            The new localized name. This value is visible to the user.
     */
    public final void setName(String name) {
        this.name = name;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(String description) {
        this.description = description;
    }

    final void rollBack() throws DbException {
        for (int i = commands.size(); --i >= 0;) {
            DbUpdateCommand cmd = (DbUpdateCommand) commands.elementAt(i);
            cmd.undo();
        }
    }

    final void rollForward() throws DbException {
        int nb = commands.size();
        for (int i = 0; i < nb; i++) {
            DbUpdateCommand cmd = (DbUpdateCommand) commands.elementAt(i);
            cmd.redo();
        }
    }

    final void addCommand(DbUpdateCommand aCommand) {
        commands.addElement(aCommand);
    }

    final int getNbCommands() {
        return commands.size();
    }

    final void concat(DbTransaction newTrans) {
        commands.replaceElementsFromVector(commands.size(), 0, newTrans.commands, 0,
                newTrans.commands.size());
    }

    /**
     * Fire the DbUpdateListeners only for the modifications made before commitTrans; the listeners
     * are called in priority order.
     * 
     * @throws DbException
     */
    final void fireDbUpdateListeners() throws DbException {
        if (commands.size() == 0)
            return;
        SrVector listenerCalls = new SrVector(10);
        int i, nb;
        for (i = 0, nb = commands.size(); i < nb; i++) {
            DbUpdateCommand cmd = (DbUpdateCommand) commands.elementAt(i);
            cmd.addDbUpdateListenerCalls(listenerCalls); /*
                                                          * generate calls for this command
                                                          */
        }
        listenerCalls.sort(); /* sort all the listener calls by priority */
        db.fireDbUpdatePassListeners(false); /* then perform the calls. */
        for (i = 0, nb = listenerCalls.size(); i < nb; i++) {
            DbUpdateListenerCall listenerCall = (DbUpdateListenerCall) listenerCalls.elementAt(i);
            listenerCall.listener.dbUpdated(listenerCall.event);
        }
        db.fireDbUpdatePassListeners(true);
    }

    final void fireDbRefreshListeners() throws DbException {
        int nb = commands.size();
        for (int i = 0; i < nb; i++) {
            DbUpdateCommand cmd = (DbUpdateCommand) commands.elementAt(i);
            cmd.fireDbRefreshListeners();
        }
    }
}
