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

import org.modelsphere.jack.util.SrSort;

/**
 * This class is a faster alternative to DbRAMRelationN for relations that may become big, but in
 * counterpart it does not preserve the order of elements, so it cannot be used for lists whose
 * order is controlled by the user.
 */
public final class DbHugeRAMRelationN extends DbRelationN {

    static final long serialVersionUID = 1900310408745769375L;

    private static final int SEG_LENGTH = 1000;

    DbSegment rootSegment; // if 2 levels, objects[i] = segment i,
    // hashCodes[i] = hashCode of last element in segment i, MAX_INT for last
    // segment
    int[] indexes; // if 2 levels, indexes[i] = index of first element in
    // segment i. indexes[nbSegment] = nbElem
    int nbElem; // total nb. of elements in the relation N
    int nbSegment; // nb. of segments if 2 levels, 0 if 1 level.
    int indSegment; // if 2 levels, last segment referenced for optimization.

    public DbHugeRAMRelationN() {
        rootSegment = new DbSegment(getSpare(0));
        indexes = null;
        nbElem = 0;
        nbSegment = 0;
    }

    public final int size() throws DbException {
        return nbElem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.baseDb.db.DbRelationN#elementAt(int)
     */
    public final DbObject elementAt(int index) throws DbException {
        if (index >= nbElem)
            throw new ArrayIndexOutOfBoundsException(index + " >= " + nbElem);
        Db db = getParent().getDb();
        DbObject dbo;
        if (nbSegment == 0)
            dbo = (DbObject) rootSegment.objects[index];
        else {
            findSegment(index);
            DbSegment segment = (DbSegment) rootSegment.objects[indSegment];
            segment.dbFetch(db);
            dbo = (DbObject) segment.objects[index - indexes[indSegment]];
        }
        dbo.initTransientFields(getParent()); /*
                                               * before returning a DbObject, always insure that
                                               * <db> is initialized
                                               */
        return dbo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.baseDb.db.DbRelationN#indexOf(org.modelsphere.jack
     * .baseDb.db.DbObject)
     */
    public final int indexOf(DbObject dbo) throws DbException {
        Db db = getParent().getDb();
        int hashCode = dbo.getTs();
        DbSegment segment;
        int nb;
        if (nbSegment == 0) {
            segment = rootSegment;
            nb = nbElem;
        } else {
            findSegmentByHash(hashCode);
            segment = (DbSegment) rootSegment.objects[indSegment];
            segment.dbFetch(db);
            nb = getSegmentSize(indSegment);
        }
        int index = SrSort.binarySearchArray(segment.hashCodes, nb, hashCode);
        if (index < 0)
            return -1;
        // binarySearch returns the first element if more than one element with
        // the same hashCode.
        // So we scan from the starting index given by binarySearch up to the
        // first element
        // with a different hashCode; the scan may cross segment boundaries.
        while (dbo != segment.objects[index]) {
            index++;
            if (index == nb) {
                if (nbSegment == 0 || nbSegment == indSegment + 1)
                    return -1;
                indSegment++;
                segment = (DbSegment) rootSegment.objects[indSegment];
                segment.dbFetch(db);
                nb = getSegmentSize(indSegment);
                index = 0;
            }
            if (hashCode != segment.hashCodes[index])
                return -1;
        }
        if (nbSegment != 0)
            index += indexes[indSegment];
        return index;
    }

    // Parameter <index> is ignored.
    final void insertElementAt(DbObject dbo, int index) throws DbException {
        int hashCode = dbo.getTs();
        DbSegment segment;
        int nb;
        if (nbSegment == 0) {
            segment = rootSegment;
            nb = nbElem;
        } else {
            findSegmentByHash(hashCode);
            segment = (DbSegment) rootSegment.objects[indSegment];
            segment.dbDirty(getParent().getDb());
            nb = getSegmentSize(indSegment);
        }
        index = SrSort.binarySearchArray(segment.hashCodes, nb, hashCode);
        if (index < 0)
            index = -(index + 1);

        insertInSegment(segment, nb, index, dbo, hashCode, -1);
        nbElem++;
        if (nbSegment != 0) {
            index += indexes[indSegment];
            for (int i = indSegment + 1; i <= nbSegment; i++)
                indexes[i]++;
        }
        if (nb > SEG_LENGTH)
            splitSegment(segment, nb + 1);

        inserted(index); /* update cursor of all opened enumerators for insertion */
    }

    final void removeElementAt(int index) throws DbException {
        if (index >= nbElem)
            throw new ArrayIndexOutOfBoundsException(index + " >= " + nbElem);
        if (nbSegment == 0) {
            removeFromSegment(rootSegment, nbElem, index, false);
            nbElem--;
        } else {
            findSegment(index);
            DbSegment segment = (DbSegment) rootSegment.objects[indSegment];
            segment.dbDirty(getParent().getDb());
            int nb = getSegmentSize(indSegment);
            removeFromSegment(segment, nb, index - indexes[indSegment], false);
            // Update hashCode of last element in case this is the one removed,
            if (indSegment + 1 != nbSegment) // except for last segment, where
                // hashCode = MAX_INT
                rootSegment.hashCodes[indSegment] = segment.hashCodes[nb - 2];
            for (int i = indSegment + 1; i <= nbSegment; i++)
                indexes[i]--;
            nbElem--;

            if (nb < SEG_LENGTH / 4)
                mergeSegment();
        }

        removed(index); /* update cursor of all opened enumerators for removal */
    }

    final void dbFetch(Db db) throws DbException {
        db.fetch(this);
        rootSegment.dbFetch(db);
        if (indexes != null)
            db.fetch(indexes);
    }

    final void dbDirty(Db db) throws DbException {
        db.dirty(this);
        rootSegment.dbDirty(db);
        if (indexes != null)
            db.dirty(indexes);
    }

    final void dbCluster(Db db, Object parent) throws DbException {
        db.cluster(this, parent);
        rootSegment.dbCluster(db, this);
        if (indexes != null)
            db.cluster(indexes, this);
        for (int i = 0; i < nbSegment; i++)
            ((DbSegment) rootSegment.objects[i]).dbCluster(db, this);
    }

    // Find segment containing the element at <index>.
    // This method must be as fast as possible.
    private void findSegment(int index) {
        if (index < indexes[indSegment] || index >= indexes[indSegment + 1]) {
            int lo = -1;
            int hi = nbSegment;
            int mid;
            while (lo + 1 < hi) {
                mid = (lo + hi) / 2;
                if (index < indexes[mid])
                    hi = mid;
                else
                    lo = mid;
            }
            indSegment = lo;
        }
    }

    // Find segment where to insert an object with this hashCode
    private void findSegmentByHash(int hashCode) {
        indSegment = SrSort.binarySearchArray(rootSegment.hashCodes, nbSegment, hashCode);
        if (indSegment < 0)
            indSegment = -(indSegment + 1);
    }

    private int getSegmentSize(int iseg) {
        return indexes[iseg + 1] - indexes[iseg];
    }

    // Insert an element at <index> in the segment;
    // (indIncr != -1) means we have 2 levels and we are inserting at level 1.
    private void insertInSegment(DbSegment segment, int nb, int index, PersistentObject object,
            int hashCode, int indIncr) {
        // Realloc if we have no space left for inserting
        if (segment.objects.length == nb)
            reallocSegment(segment, nb, (indIncr != -1));

        if (index < nb) {
            System.arraycopy(segment.objects, index, segment.objects, index + 1, nb - index);
            System.arraycopy(segment.hashCodes, index, segment.hashCodes, index + 1, nb - index);
        }
        segment.objects[index] = object;
        segment.hashCodes[index] = hashCode;

        if (indIncr != -1) {
            System.arraycopy(indexes, index, indexes, index + 1, nb - index + 1);
            indexes[index + 1] += indIncr;
        }
    }

    // Remove an element at <index> in the segment;
    // withIndexes means we have 2 levels and we are inserting at level 1.
    private void removeFromSegment(DbSegment segment, int nb, int index, boolean withIndexes) {
        nb--;
        if (index < nb) {
            System.arraycopy(segment.objects, index + 1, segment.objects, index, nb - index);
            System.arraycopy(segment.hashCodes, index + 1, segment.hashCodes, index, nb - index);
        }
        segment.objects[nb] = null; // to let gc do its work
        if (withIndexes)
            System.arraycopy(indexes, index + 2, indexes, index + 1, nb - index);

        // Realloc if we have more than 2 times the spare space
        if (segment.objects.length - nb > 2 * getSpare(nb))
            reallocSegment(segment, nb, withIndexes);
    }

    // Adjust the segment size to the nb. of elements + the spare space.
    private void reallocSegment(DbSegment segment, int nb, boolean withIndexes) {
        int length = nb + getSpare(nb);
        PersistentObject[] oldObjects = segment.objects;
        int[] oldHashCodes = segment.hashCodes;
        segment.objects = new PersistentObject[length];
        segment.hashCodes = new int[length];
        System.arraycopy(oldObjects, 0, segment.objects, 0, nb);
        System.arraycopy(oldHashCodes, 0, segment.hashCodes, 0, nb);
        if (withIndexes) {
            int[] oldIndexes = indexes;
            indexes = new int[length + 1]; // indexes has an extra entry which
            // is nbElem
            System.arraycopy(oldIndexes, 0, indexes, 0, nb + 1);
        }
    }

    private int getSpare(int nb) {
        return Math.max(4, nb / 8);
    }

    // The current segment has more than SEG_LENGTH elements; split it in 2
    // segments
    private void splitSegment(DbSegment segment, int nb) {
        int nb1 = nb / 2;
        int nb2 = nb - nb1;
        int length = nb2 + getSpare(nb2);
        PersistentObject[] oldObjects = segment.objects;
        int[] oldHashCodes = segment.hashCodes;
        segment.objects = new PersistentObject[length];
        segment.hashCodes = new int[length];
        DbSegment segBefore = new DbSegment(length);
        System.arraycopy(oldObjects, 0, segBefore.objects, 0, nb1);
        System.arraycopy(oldObjects, nb1, segment.objects, 0, nb2);
        System.arraycopy(oldHashCodes, 0, segBefore.hashCodes, 0, nb1);
        System.arraycopy(oldHashCodes, nb1, segment.hashCodes, 0, nb2);

        // If we have 1 level, we transform to a 2-level structure
        // containing the root segment and 2 segments at level 2.
        if (nbSegment == 0) {
            length = 2 + getSpare(2);
            rootSegment = new DbSegment(length);
            indexes = new int[length + 1]; // indexes has an extra entry which
            // is nbElem
            rootSegment.objects[0] = segBefore;
            rootSegment.hashCodes[0] = segBefore.hashCodes[nb1 - 1];
            rootSegment.objects[1] = segment;
            rootSegment.hashCodes[1] = Integer.MAX_VALUE;
            indexes[0] = 0;
            indexes[1] = nb1;
            indexes[2] = nbElem;
            nbSegment = 2;
            indSegment = 0;
        } else { // add to the root segment an element representing the new
            // segment <segBefore>
            insertInSegment(rootSegment, nbSegment, indSegment, segBefore,
                    segBefore.hashCodes[nb1 - 1], nb1);
            nbSegment++;
        }
    }

    // The current segment has less than SEG_LENGTH/4 elements; merge it with
    // its smallest neighbor.
    private void mergeSegment() throws DbException {
        if (indSegment > 0) {
            if (indSegment + 1 == nbSegment
                    || getSegmentSize(indSegment - 1) < getSegmentSize(indSegment + 1))
                indSegment--;
        }
        Db db = getParent().getDb();
        DbSegment segBefore = (DbSegment) rootSegment.objects[indSegment];
        segBefore.dbFetch(db);
        DbSegment segment = (DbSegment) rootSegment.objects[indSegment + 1];
        segment.dbDirty(db);
        int nb1 = getSegmentSize(indSegment);
        int nb2 = getSegmentSize(indSegment + 1);
        int length = nb1 + nb2 + getSpare(nb1 + nb2);
        PersistentObject[] newObjects = new PersistentObject[length];
        int[] newHashCodes = new int[length];
        System.arraycopy(segBefore.objects, 0, newObjects, 0, nb1);
        System.arraycopy(segment.objects, 0, newObjects, nb1, nb2);
        System.arraycopy(segBefore.hashCodes, 0, newHashCodes, 0, nb1);
        System.arraycopy(segment.hashCodes, 0, newHashCodes, nb1, nb2);
        segment.objects = newObjects;
        segment.hashCodes = newHashCodes;

        // If we are merging the last 2 segments, transform to a 1-level
        // structure.
        if (nbSegment == 2) {
            rootSegment = segment;
            indexes = null;
            nbSegment = 0;
        } else { // remove from the root segment the element representing the
            // segment <segBefore>
            removeFromSegment(rootSegment, nbSegment, indSegment, true);
            nbSegment--;
        }
    }

    public final boolean checkIntegrity() throws DbException {
        if (nbSegment == 0)
            return checkSegmentIntegrity(rootSegment, nbElem, Integer.MIN_VALUE);
        if (indexes[0] != 0 || indexes[nbSegment] != nbElem)
            return false;
        int hashCode = Integer.MIN_VALUE;
        for (int i = 0; i < nbSegment; i++) {
            DbSegment segment = (DbSegment) rootSegment.objects[i];
            segment.dbFetch(getParent().getDb());
            int nb = getSegmentSize(i);
            if (nb <= 0)
                return false;
            if (!checkSegmentIntegrity(segment, nb, hashCode))
                return false;
            hashCode = (i + 1 == nbSegment ? Integer.MAX_VALUE : segment.hashCodes[nb - 1]);
            if (hashCode != rootSegment.hashCodes[i])
                return false;
        }
        return true;
    }

    private boolean checkSegmentIntegrity(DbSegment segment, int nb, int hashCode) {
        for (int i = 0; i < nb; i++) {
            if (hashCode > segment.hashCodes[i])
                return false;
            hashCode = segment.hashCodes[i];
        }
        return true;
    }

    private static final class DbSegment extends PersistentObject {

        static final long serialVersionUID = 4058907248655120795L;

        PersistentObject[] objects;
        int[] hashCodes;

        DbSegment(int length) {
            objects = new PersistentObject[length];
            hashCodes = new int[length];
        }

        final void dbFetch(Db db) throws DbException {
            db.fetch(this);
            db.fetch(objects);
            db.fetch(hashCodes);
        }

        final void dbDirty(Db db) throws DbException {
            db.dirty(this);
            db.dirty(objects);
            db.dirty(hashCodes);
        }

        final void dbCluster(Db db, Object parent) throws DbException {
            db.cluster(this, parent);
            db.cluster(objects, this);
            db.cluster(hashCodes, this);
        }
    }
}
