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

import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.util.SrVector;

/**
 * Command for reordering inside a relation (reinsert()). This command uses old and new indexes.
 * 
 * @see Db#reinsert(DbRelationN, DbObject, int, int)
 */
public class DbReinsertCommand extends DbUpdateCommand {

    private DbRelationN dbRelN;
    private DbObject parent;
    private MetaRelationN metaRelation;
    private DbObject neighbor;
    private int oldIndex;
    private int newIndex;

    public DbReinsertCommand(DbRelationN dbRelN, DbObject neighbor, int oldIndex, int newIndex) {
        this.dbRelN = dbRelN;
        this.parent = dbRelN.getParent(); /*
                                           * save <parent, metaRelation> in the command because
                                           */
        this.metaRelation = dbRelN.getMetaRelation(); /*
                                                       * after a remove() they are no more
                                                       * accessible
                                                       */
        this.neighbor = neighbor;
        this.oldIndex = oldIndex;
        this.newIndex = newIndex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.baseDb.db.DbUpdateCommand#undo()
     */
    public final void undo() throws DbException {
        undoRedo(newIndex, oldIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.baseDb.db.DbUpdateCommand#redo()
     */
    public final void redo() throws DbException {
        undoRedo(oldIndex, newIndex);
    }

    /**
     * Undo the reinsert command if the element to reinsert is at the same place; otherwise, skip
     * this command and continue undoing the other commands.
     */
    private final void undoRedo(int oldIndex, int newIndex) throws DbException {
        Db db = parent.getDb();
        db.fetch(parent);
        dbRelN.dbFetch(db);
        if (oldIndex < dbRelN.size() && newIndex < dbRelN.size()
                && neighbor == dbRelN.elementAt(oldIndex)) {
            parent.basicReinsert(metaRelation, oldIndex, newIndex);
        }
    }

    /**
     * Do not call DbUpdateListeners for a reinsert if the DbObject is added or removed, or if the
     * neighbor is removed.
     * 
     * @param listenerCalls
     * @see DbUpdateCommand#addDbUpdateListenerCalls(SrVector)
     */
    final void addDbUpdateListenerCalls(SrVector listenerCalls) {
        if (metaRelation.hasDbUpdateListeners() && parent.getTransStatus() == Db.OBJ_MODIFIED
                && neighbor.getTransStatus() != Db.OBJ_REMOVED) {
            DbUpdateEvent event = new DbUpdateEvent(parent, metaRelation, neighbor, oldIndex,
                    newIndex, Db.REINSERT_IN_RELN);
            metaRelation.addDbUpdateListenerCalls(listenerCalls, event);
        }
    }

    /**
     * Do not call DbRefreshListeners for a reinsert if the DbObject is added or removed, or if the
     * neighbor is removed.
     * 
     * @see DbUpdateCommand#fireDbRefreshListeners()
     */
    final void fireDbRefreshListeners() throws DbException {
        if (parent.getTransStatus() != Db.OBJ_MODIFIED
                || neighbor.getTransStatus() == Db.OBJ_REMOVED)
            return;
        if (metaRelation.hasDbRefreshListeners() || parent.hasDbRefreshListeners()) {
            DbUpdateEvent event = new DbUpdateEvent(parent, metaRelation, neighbor, oldIndex,
                    newIndex, Db.REINSERT_IN_RELN);
            metaRelation.fireDbRefreshListeners(event);
            parent.fireDbRefreshListeners(event, DbRefreshListener.CALL_FOR_EVERY_FIELD);
        }
    }
}
