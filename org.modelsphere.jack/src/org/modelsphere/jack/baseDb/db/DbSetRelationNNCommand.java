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
 * This command class is used to record setRelationNN() operations on a given DbObject.
 * 
 * @see org.modelsphere.jack.baseDb.db.DbSetCommand
 * @see Db#setRelationNN(DbObject, MetaRelationN, DbObject, int, int, int)
 */
public class DbSetRelationNNCommand extends DbUpdateCommand {

    private DbObject updatedObject;

    private MetaRelationN relation;

    private DbObject neighbor;

    private int op;

    private int index;

    private int oppositeIndex;

    public DbSetRelationNNCommand(DbObject updatedObject, MetaRelationN relation,
            DbObject neighbor, int op, int index, int oppositeIndex) {
        this.updatedObject = updatedObject;
        this.relation = relation;
        this.neighbor = neighbor;
        this.op = op;
        this.index = index;
        this.oppositeIndex = oppositeIndex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.baseDb.db.DbUpdateCommand#undo()
     */
    public final void undo() throws DbException {
        if (!updatedObject
                .setRelationNN(relation, neighbor, (op == Db.ADD_TO_RELN ? Db.REMOVE_FROM_RELN
                        : Db.ADD_TO_RELN), index, oppositeIndex)) {
            if (updatedObject.getDb().getTransMode() != Db.TRANS_ABORT)
                updatedObject.getDb().throwDbUndoRedoConflictException();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.baseDb.db.DbUpdateCommand#redo()
     */
    public final void redo() throws DbException {
        if (!updatedObject.setRelationNN(relation, neighbor, op, index, oppositeIndex))
            updatedObject.getDb().throwDbUndoRedoConflictException();
    }

    /**
     * Generate listener calls for modification of the relation on updatedObject. Also generates
     * calls for modification of the opposite relation on neighbor. Do not generate calls on added
     * or removed object.
     * 
     * @param listenerCalls
     * @see DbUpdateCommand#addDbUpdateListenerCalls(SrVector)
     */
    final void addDbUpdateListenerCalls(SrVector listenerCalls) {
        if (relation.hasDbUpdateListeners() && updatedObject.getTransStatus() == Db.OBJ_MODIFIED
                && (neighbor.getTransStatus() != Db.OBJ_REMOVED || op == Db.REMOVE_FROM_RELN)) {
            DbUpdateEvent event = new DbUpdateEvent(updatedObject, relation, neighbor, index, -1,
                    op);
            relation.addDbUpdateListenerCalls(listenerCalls, event);
        }

        MetaRelationN oppositeRel = (MetaRelationN) relation.getOppositeRel(neighbor);
        if (oppositeRel.hasDbUpdateListeners() && neighbor.getTransStatus() == Db.OBJ_MODIFIED
                && (updatedObject.getTransStatus() != Db.OBJ_REMOVED || op == Db.REMOVE_FROM_RELN)) {
            DbUpdateEvent event = new DbUpdateEvent(neighbor, oppositeRel, updatedObject,
                    oppositeIndex, -1, op);
            oppositeRel.addDbUpdateListenerCalls(listenerCalls, event);
        }
    }

    /**
     * Call RefreshListeners for modification of the relation on updatedObject; call also
     * RefreshListeners for modification of the opposite relation on neighbor. Do not generate calls
     * on added or removed object.
     * 
     * @see DbUpdateCommand#fireDbRefreshListeners()
     */
    final void fireDbRefreshListeners() throws DbException {
        if ((relation.hasDbRefreshListeners() || updatedObject.hasDbRefreshListeners())
                && updatedObject.getTransStatus() == Db.OBJ_MODIFIED
                && (neighbor.getTransStatus() != Db.OBJ_REMOVED || op == Db.REMOVE_FROM_RELN)) {
            DbUpdateEvent event = new DbUpdateEvent(updatedObject, relation, neighbor, index, -1,
                    op);
            relation.fireDbRefreshListeners(event);
            updatedObject.fireDbRefreshListeners(event, DbRefreshListener.CALL_FOR_EVERY_FIELD);
        }

        MetaRelationN oppositeRel = (MetaRelationN) relation.getOppositeRel(neighbor);
        if ((oppositeRel.hasDbRefreshListeners() || neighbor.hasDbRefreshListeners())
                && neighbor.getTransStatus() == Db.OBJ_MODIFIED
                && (updatedObject.getTransStatus() != Db.OBJ_REMOVED || op == Db.REMOVE_FROM_RELN)) {
            DbUpdateEvent event = new DbUpdateEvent(neighbor, oppositeRel, updatedObject,
                    oppositeIndex, -1, op);
            oppositeRel.fireDbRefreshListeners(event);
            neighbor.fireDbRefreshListeners(event, DbRefreshListener.CALL_FOR_EVERY_FIELD);
        }
    }
}
