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
 * <code>refreshAfterDbUpdate()</code> is called at the end of a transaction (when it is committed),
 * and also at the end of an undo or redo transaction, in order to refresh the interface elements
 * from the modifications just committed in the database. <code>refreshAfterDbUpdate()</code> is
 * called if a setter method has been invoked on a db object during the transaction (it is similar
 * to 'update triggers' in databases. It is less costly to use than DbUpdateListener, so it is
 * better to use it when applicable. Refresh listeners are typically used to refresh the diagrams
 * and other GUI components. <br>
 * <br>
 * Listeners are installed by the following way :
 * 
 * <pre>
 *    //Listen to modifications on a DbObject
 *    DbRefreshListener listener = new DbRefreshListener() {..};
 *    MetaField[] mfs = new MetaField[] {..}; //specify which metafields are listened
 *    MetaField.addDbRefreshListner(listener, project, mfs);
 * </pre>
 * 
 * If you want to listener creations or deletions of db objects (a kind of 'create' or 'delete
 * triggers', instead of modifications of db object fields, install the listener on the fComponents
 * field of its composite.
 * 
 * <pre>
 * //Listen to creations/deletions of DbObject
 * MetaField[] mf = new MetaField[] { DbJVClass.fComponents }; //method, a component of class
 * MetaField.addDbRefreshListner(listener, project, mf);
 * </pre>
 * 
 * Do no forget to remove listeners when they are no longer required, otherwise a general
 * performance regression of the application will result.
 * 
 * <pre>
 * MetaField.removeDbRefreshListener(listener);
 * </pre>
 * 
 * @see org.modelsphere.jack.baseDb.db.event.DbUpdateListener
 */
public interface DbRefreshListener {

    /* Parm <when> of <DbObject.addDbRefreshListener> */
    public final static int CALL_FOR_EVERY_FIELD = 0;
    public final static int CALL_ONCE = 1;

    public void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException;
}
