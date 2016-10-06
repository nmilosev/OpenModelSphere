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

package org.modelsphere.jack.baseDb.db.event;

import org.modelsphere.jack.baseDb.db.DbException;

/**
 * 
 * Listener on db object changes. <br>
 * <br>
 * <code>dbUpdated()</code> is called when a setter method is invoked on a db object. It is very
 * costy, so it is better to use DbRefreshListener (which is called at commit time) when applicable.
 * As a rule of thumb, use DbUpdateListener to check semantic integrity, and DbRefreshListener to
 * refresh diagrams and the UI. <br>
 * <br>
 * The update listeners are called during the commitTrans process to complete the transaction before
 * the physical commit. They correspond to the concept of trigger in RDBMS. They are called for all
 * the modifications made to the database before the commitTrans. They are not called for
 * modifications made during the commitTrans process by other update listeners. <br>
 * <br>
 * An update listener is registered to a metaField, and is called for each object having the status
 * Db.OBJ_MODIFIED for which this field has been modified during the transaction. The field may be
 * an N-relation. <br>
 * <br>
 * For added or removed objects, the update listeners are not called, except the ones registered on
 * the field <code>DbObject.fComposite</code>. For this particular field, the update listeners are
 * called for each object added or removed during the transaction, and also for each object whose
 * composite has changed during the transaction. <br>
 * <br>
 * 
 * @see org.modelsphere.jack.baseDb.db.event.DbRefreshListener
 */
public interface DbUpdateListener {

    public void dbUpdated(DbUpdateEvent event) throws DbException;
}
