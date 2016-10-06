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

import org.modelsphere.jack.baseDb.international.LocaleMgr;

/**
 * RAM implementation of the modelsphere database. All the graph's objects are available in memory.
 * <p>
 * This implementation uses the java serialization mechanisms for persistence.
 */
public final class DbRAM extends Db {
    public static final String DISPLAY_NAME = LocaleMgr.db.getString("RAM");

    public DbRAM() {
        begin(new DbRoot(this, 1));
        fireDbListeners(DbRAM.this, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.baseDb.db.Db#getDBMSName()
     */
    public final String getDBMSName() {
        return DISPLAY_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.baseDb.db.Db#DBMSBeginTrans(int)
     */
    final void DBMSBeginTrans(int access) throws DbException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.baseDb.db.Db#DBMSCommitTrans(java.lang.String)
     */
    final void DBMSCommitTrans(String description) throws DbException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.baseDb.db.Db#DBMSAbortTrans()
     */
    final void DBMSAbortTrans() {
        try {
            getRootTransaction().rollBack();
        } catch (DbException e) {
            throw new RuntimeException("DbException in abortTrans"); // NOT
            // LOCALIZABLE
            // RuntimeException
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.baseDb.db.Db#DBMSTerminate()
     */
    final void DBMSTerminate() {
        // To allow gc to do its work, break any circular references.
        try {
            beginWriteTrans("");
            setTransMode(Db.TRANS_ABORT); /*
                                           * inhibits commands registration in transaction.
                                           */
            getRoot().remove(); /*
                                 * removes all inter-object references in the database.
                                 */
            abortTrans(); /* no rollback done since transaction is empty */
        } catch (Exception e) {
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.baseDb.db.Db#hasFetch()
     */
    final boolean hasFetch() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.baseDb.db.Db#fetch(java.lang.Object)
     */
    public final void fetch(Object obj) throws DbException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.baseDb.db.Db#dirty(java.lang.Object)
     */
    final void dirty(Object obj) throws DbException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.baseDb.db.Db#cluster(java.lang.Object, java.lang.Object)
     */
    public final void cluster(Object obj, Object container) throws DbException {
    }
}
