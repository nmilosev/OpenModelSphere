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

import org.modelsphere.jack.util.SrVector;

/**
 * Super class for all transaction's commands. For each update occurring inside a transaction, a
 * command object is added to the transaction object. Sub classes are required to store a new and
 * old value for undo redo operations. The object for which the update applies can't be used for
 * holding these values since many changes can occur on the same object-property within the same
 * transaction.
 * 
 * @see org.modelsphere.jack.baseDb.db.DbTransaction
 */
public abstract class DbUpdateCommand {

    /**
     * Set the old value on the object to restore its previous state.
     * 
     * @throws DbException
     */
    public abstract void undo() throws DbException;

    /**
     * Reapply the new value on the object.
     * 
     * @throws DbException
     */
    public abstract void redo() throws DbException;

    /**
     * @param listenerCalls
     */
    abstract void addDbUpdateListenerCalls(SrVector listenerCalls);

    /**
     * Notify refresh listeners.
     * 
     * @throws DbException
     */
    abstract void fireDbRefreshListeners() throws DbException;
}
