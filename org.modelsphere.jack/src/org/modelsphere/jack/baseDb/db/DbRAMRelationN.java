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

/**
 * 
 * This class implements a general support for relation N, that may be used for any DBMS (not only
 * DbRAM); so, for some DBMSs, we may prefer to use this implementation instead of the DBMS's own
 * collections. <br>
 * <br>
 * IMPORTANT: if for a DBMS, we have a particular implementation, we must provide the methods
 * <code>writeObject()</code> and <code>readObject()</code> to convert the DBMS representation to
 * DbRAMRelationN and vice-versa.
 */
public final class DbRAMRelationN extends DbRelationN {

    static final long serialVersionUID = 5819091681359386373L;

    private static final int INCR_ALLOC = 4;

    DbObject[] array;
    int nbElem;

    public DbRAMRelationN() {
        array = new DbObject[INCR_ALLOC];
        nbElem = 0;
    }

    public final int size() throws DbException {
        return nbElem;
    }

    public final DbObject elementAt(int index) throws DbException {
        if (index >= nbElem)
            throw new ArrayIndexOutOfBoundsException(index + " >= " + nbElem);
        DbObject dbo = array[index];
        dbo.initTransientFields(getParent()); /*
                                               * before returning a DbObject, always insure that
                                               * <db> is initialized
                                               */
        return dbo;
    }

    public final int indexOf(DbObject dbo) throws DbException {
        for (int i = 0; i < nbElem; i++) {
            if (dbo == array[i])
                return i;
        }
        return -1;
    }

    final void insertElementAt(DbObject dbo, int index) throws DbException {
        if (index > nbElem)
            throw new ArrayIndexOutOfBoundsException(index + " > " + nbElem);
        adjustCapacity(1);
        if (index < nbElem)
            System.arraycopy(array, index, array, index + 1, nbElem - index);
        array[index] = dbo;
        nbElem++;

        inserted(index); /* update cursor of all opened enumerators for insertion */
    }

    final void removeElementAt(int index) throws DbException {
        if (index >= nbElem)
            throw new ArrayIndexOutOfBoundsException(index + " >= " + nbElem);
        nbElem--;
        if (index < nbElem)
            System.arraycopy(array, index + 1, array, index, nbElem - index);
        array[nbElem] = null; /* to let gc do its work */
        adjustCapacity(0);

        removed(index); /* update cursor of all opened enumerators for removal */
    }

    final void dbFetch(Db db) throws DbException {
        db.fetch(this);
        db.fetch(array);
    }

    final void dbDirty(Db db) throws DbException {
        db.dirty(this);
        db.dirty(array);
    }

    final void dbCluster(Db db, Object parent) throws DbException {
        db.cluster(this, parent);
        db.cluster(array, this);
    }

    private final void adjustCapacity(int incrElem) {
        int spare = array.length - (nbElem + incrElem);
        int newSpare = (INCR_ALLOC > array.length / 8 ? INCR_ALLOC : array.length / 8);
        if (spare >= 0 && spare < newSpare * 2)
            return;

        DbObject[] oldArray = array;
        array = new DbObject[nbElem + incrElem + newSpare];
        System.arraycopy(oldArray, 0, array, 0, nbElem);
    }
}
