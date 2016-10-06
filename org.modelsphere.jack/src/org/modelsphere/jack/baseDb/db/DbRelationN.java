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

import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;

/**
 * A common interface for DbRAMRelationN and DbHugeRAMRelationN.
 * 
 */
public abstract class DbRelationN extends PersistentObject {

    static final long serialVersionUID = 1399783330007803969L;

    private transient DbObject parent;
    private transient MetaRelationN metaRelation;
    private transient DbRelationNEnum firstEnum; /*
                                                  * head of the list of opened enumerators
                                                  */

    public abstract int size() throws DbException;

    public abstract DbObject elementAt(int index) throws DbException;

    public abstract int indexOf(DbObject dbo) throws DbException;

    abstract void insertElementAt(DbObject dbo, int index) throws DbException;

    abstract void removeElementAt(int index) throws DbException;

    public final DbObject getParent() {
        return parent;
    }

    public final MetaRelationN getMetaRelation() {
        return metaRelation;
    }

    public final DbEnumeration elements() throws DbException {
        return elements(null, Db.ENUM_FORWARD);
    }

    public final DbEnumeration elements(MetaClass metaClass) throws DbException {
        return elements(metaClass, Db.ENUM_FORWARD);
    }

    public final DbEnumeration elements(MetaClass metaClass, int direction) throws DbException {
        return new DbRelationNEnum(metaClass, direction);
    }

    /* Close all enumerators of a DbRelationN. */
    public final void closeAllEnums() {
        while (firstEnum != null)
            firstEnum.close();
    }

    abstract void dbFetch(Db db) throws DbException;

    abstract void dbDirty(Db db) throws DbException;

    abstract void dbCluster(Db db, Object parent) throws DbException;

    final void initTransientFields(DbObject parent, MetaRelationN metaRelation) {
        this.parent = parent;
        this.metaRelation = metaRelation;
    }

    /* Update all enumerators when inserting an element */
    final void inserted(int index) {
        for (DbRelationNEnum dbEnum = firstEnum; dbEnum != null; dbEnum = dbEnum.nextEnum)
            dbEnum.inserted(index);
    }

    /* Update all enumerators when removing an element */
    final void removed(int index) {
        for (DbRelationNEnum dbEnum = firstEnum; dbEnum != null; dbEnum = dbEnum.nextEnum)
            dbEnum.removed(index);
    }

    /**
     * DbRelationNEnum: enumerator on a DbRelationN. All the opened enumerators of a DbRelationN are
     * kept in a linked list; when elements are inserted in or removed from the DbRelationN, the
     * enumerators are updated accordingly.
     */
    private final class DbRelationNEnum implements DbEnumeration {

        MetaClass metaClass;
        DbObject nextDbo;
        int direction;
        int index;
        int increment;
        DbRelationNEnum nextEnum; /* next in the list of opened enumerators */

        DbRelationNEnum(MetaClass metaClass, int direction) throws DbException {
            this.metaClass = metaClass;
            this.direction = direction;
            if (direction == Db.ENUM_FORWARD) {
                index = -1;
                increment = 1;
            } else {
                index = size();
                increment = -1;
            }
            nextEnum = firstEnum; /* insert the enumerator at the top of the list */
            firstEnum = this;
            if (nextEnum == null) /*
                                   * if first enumerator, add the dbRelN to the list of enumerated
                                   * DbRelationN's
                                   */
                parent.getDb().addEnumeratedRelN(DbRelationN.this);
        }

        public boolean hasMoreElements() throws DbException {
            while (hasMore()) {
                DbObject dbo = elementAt(index + increment);
                if (metaClass == null || metaClass.getJClass().isInstance(dbo)) {
                    nextDbo = dbo;
                    return true;
                }
                index += increment;
            }
            nextDbo = null;
            return false;
        }

        public DbObject nextElement() throws DbException {
            if (nextDbo == null)
                throw new RuntimeException("DbRelationNEnum: nextElement without hasMoreElements"); // NOT
            // LOCALIZABLE
            // RuntimeException
            index += increment;
            DbObject dbo = nextDbo;
            nextDbo = null;
            return dbo;
        }

        private boolean hasMore() throws DbException {
            if (direction == Db.ENUM_FORWARD)
                return (index + 1 < size());
            return (index > 0);
        }

        /* Remove the enumerator from the list. */
        public void close() {
            if (firstEnum == this) {
                firstEnum = nextEnum;
                if (firstEnum == null) /*
                                        * if no more enumerator, remove the dbRelN from the list of
                                        * enumerated DbRelationN's
                                        */
                    parent.getDb().removeEnumeratedRelN(DbRelationN.this);
            } else {
                for (DbRelationNEnum prevEnum = firstEnum; prevEnum != null; prevEnum = prevEnum.nextEnum) {
                    if (prevEnum.nextEnum == this) {
                        prevEnum.nextEnum = nextEnum;
                        break;
                    }
                }
            }
        }

        /* Update all enumerators when inserting an element */
        final void inserted(int index) {
            if (this.index >= index)
                this.index++;
        }

        /* Update all enumerators when removing an element */
        final void removed(int index) {
            if (this.index > index || (this.index == index && direction == Db.ENUM_FORWARD))
                this.index--;
        }
    }
}
