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
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.util.SrVector;

/**
 * This command class is used to record set(), setRelation11(), and setRelation1N() operations on a
 * given DbObject.
 * 
 * @see org.modelsphere.jack.baseDb.db.DbSetRelationNNCommand
 * @see Db#set(DbObject, MetaField, Object, Object, int, int)
 */
public class DbSetCommand extends DbUpdateCommand {

    private DbObject updatedObject;
    private MetaField updatedField;
    private Object oldValue;
    private Object newValue;
    private int oldIndex;
    private int newIndex;

    public DbSetCommand(DbObject anUpdatedObject, MetaField anUpdatedField, Object anOldValue,
            Object aNewValue, int anOldIndex, int aNewIndex) {
        updatedObject = anUpdatedObject;
        updatedField = anUpdatedField;
        oldValue = anOldValue;
        newValue = aNewValue;
        oldIndex = anOldIndex;
        newIndex = aNewIndex;
    }

    public final void undo() throws DbException {
        undoRedo(newValue, oldValue, oldIndex);
    }

    public final void redo() throws DbException {
        undoRedo(oldValue, newValue, newIndex);
    }

    /*
     * If another user has changed the value in the meantime, cancel the Undo operation with a data
     * conflict exception.
     */
    private final void undoRedo(Object oldValue, Object newValue, int index) throws DbException {
        if (!DbObject.valuesAreEqual(updatedObject.basicGet(updatedField), oldValue)) {
            if (updatedObject.getDb().getTransMode() != Db.TRANS_ABORT)
                updatedObject.getDb().throwDbUndoRedoConflictException();
        }
        updatedObject.basicSet(updatedField, newValue, index);
    }

    /**
     * Generate listener calls for modification of this attribute on this object; if relation,
     * generate also calls for modification of opposite relation on 2 objects: the one it was
     * connected to before , and the one is is connectod to now.
     * 
     * If the object is added or removed, generate listener calls only for the event <modif of the
     * composite>.
     * 
     * @param listenerCalls
     * @see org.modelsphere.jack.baseDb.db.DbUpdateCommand#addDbUpdateListenerCalls(org.modelsphere.jack.util.SrVector)
     */
    final void addDbUpdateListenerCalls(SrVector listenerCalls) {
        if (updatedField.hasDbUpdateListeners()) {
            if (updatedObject.getTransStatus() == Db.OBJ_MODIFIED
                    || updatedField == DbObject.fComposite) {
                DbUpdateEvent event = new DbUpdateEvent(updatedObject, updatedField,
                        (DbObject) null, newIndex, -1, 0);
                updatedField.addDbUpdateListenerCalls(listenerCalls, event);
            }
        }

        if (updatedField instanceof MetaRelationship) {
            MetaRelationship oppositeRel = ((MetaRelationship) updatedField)
                    .getOppositeRel((DbObject) (oldValue != null ? oldValue : newValue));
            if (oppositeRel.hasDbUpdateListeners()) {
                if (oldValue != null && ((DbObject) oldValue).getTransStatus() == Db.OBJ_MODIFIED) {
                    DbUpdateEvent event;
                    if (oppositeRel instanceof MetaRelationN)
                        event = new DbUpdateEvent((DbObject) oldValue, oppositeRel, updatedObject,
                                oldIndex, -1, Db.REMOVE_FROM_RELN);
                    else
                        event = new DbUpdateEvent((DbObject) oldValue, oppositeRel);
                    oppositeRel.addDbUpdateListenerCalls(listenerCalls, event);
                }
                if (newValue != null && ((DbObject) newValue).getTransStatus() == Db.OBJ_MODIFIED
                        && updatedObject.getTransStatus() != Db.OBJ_REMOVED) {
                    DbUpdateEvent event;
                    if (oppositeRel instanceof MetaRelationN)
                        event = new DbUpdateEvent((DbObject) newValue, oppositeRel, updatedObject,
                                newIndex, -1, Db.ADD_TO_RELN);
                    else
                        event = new DbUpdateEvent((DbObject) newValue, oppositeRel);
                    oppositeRel.addDbUpdateListenerCalls(listenerCalls, event);
                }
            }
        }
    }

    /**
     * Call RefreshListeners for modification of this attribute on this object.
     * <p>
     * If non composite associations, also call RefreshListeners for modification of opposite
     * relation on 2 objects: the one it was connected to before , and the one it is connected to
     * now.
     * 
     * If the object is added or removed, call RefreshListeners only for the event <modif of the
     * composite>; do not call any if object is added and removed in the same transaction.
     * 
     * @see org.modelsphere.jack.baseDb.db.DbUpdateCommand#fireDbRefreshListeners()
     */
    final void fireDbRefreshListeners() throws DbException {
        if (updatedField.hasDbRefreshListeners() || updatedObject.hasDbRefreshListeners()) {
            if (updatedObject.getTransStatus() == Db.OBJ_MODIFIED
                    || (updatedField == DbObject.fComposite && !updatedObject.isAddedAndRemoved())) {
                DbUpdateEvent event = new DbUpdateEvent(updatedObject, updatedField,
                        (DbObject) null, newIndex, -1, 0);
                updatedField.fireDbRefreshListeners(event);
                updatedObject.fireDbRefreshListeners(event, DbRefreshListener.CALL_FOR_EVERY_FIELD);
            }
        }

        if (updatedField instanceof MetaRelationship) {
            MetaRelationship oppositeRel = ((MetaRelationship) updatedField)
                    .getOppositeRel((DbObject) (oldValue != null ? oldValue : newValue));
            if (oldValue != null) {
                DbObject dbo = (DbObject) oldValue;
                if ((oppositeRel.hasDbRefreshListeners() || dbo.hasDbRefreshListeners())
                        && dbo.getTransStatus() == Db.OBJ_MODIFIED) {
                    DbUpdateEvent event;
                    if (oppositeRel instanceof MetaRelationN)
                        event = new DbUpdateEvent(dbo, oppositeRel, updatedObject, oldIndex, -1,
                                Db.REMOVE_FROM_RELN);
                    else
                        event = new DbUpdateEvent(dbo, oppositeRel);
                    oppositeRel.fireDbRefreshListeners(event);
                    dbo.fireDbRefreshListeners(event, DbRefreshListener.CALL_FOR_EVERY_FIELD);
                }
            }
            if (newValue != null && updatedObject.getTransStatus() != Db.OBJ_REMOVED) {
                DbObject dbo = (DbObject) newValue;
                if ((oppositeRel.hasDbRefreshListeners() || dbo.hasDbRefreshListeners())
                        && dbo.getTransStatus() == Db.OBJ_MODIFIED) {
                    DbUpdateEvent event;
                    if (oppositeRel instanceof MetaRelationN)
                        event = new DbUpdateEvent(dbo, oppositeRel, updatedObject, newIndex, -1,
                                Db.ADD_TO_RELN);
                    else
                        event = new DbUpdateEvent(dbo, oppositeRel);
                    oppositeRel.fireDbRefreshListeners(event);
                    dbo.fireDbRefreshListeners(event, DbRefreshListener.CALL_FOR_EVERY_FIELD);
                }
            }
        }
    }
}
