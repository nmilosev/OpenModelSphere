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

package org.modelsphere.sms.plugins;

import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.db.srtypes.JVClassCategory;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORConstraint;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTable;

public abstract class ReverseUtilities {

    // Link indexes to PUF constraints with the same composition.
    public static void linkConstraintIndexes(DbORDataModel model) throws DbException {
        if (model == null)
            return;
        DbEnumeration enumTables = model.componentTree(DbORAbsTable.metaClass,
                new MetaClass[] { DbORDataModel.metaClass });
        while (enumTables.hasMoreElements()) {
            DbORAbsTable table = (DbORAbsTable) enumTables.nextElement();
            ArrayList pufKeys = new ArrayList(); // list of PUF constraints
            // without index
            ArrayList indexes = new ArrayList(); // list of indexes without
            // constraint
            DbEnumeration dbEnum = table.getComponents().elements();
            while (dbEnum.hasMoreElements()) {
                DbObject dbo = dbEnum.nextElement();
                if (dbo instanceof DbORPrimaryUnique) {
                    if (((DbORPrimaryUnique) dbo).getIndex() == null)
                        pufKeys.add(dbo);
                } else if (dbo instanceof DbORForeign) {
                    if (((DbORForeign) dbo).getIndex() == null)
                        pufKeys.add(dbo);
                } else if (dbo instanceof DbORIndex) {
                    if (((DbORIndex) dbo).getConstraint() == null)
                        indexes.add(dbo);
                }
            }
            dbEnum.close();

            // Compare the composition of all constraints with all indexes and
            // link the matching pairs
            for (int i = 0; i < pufKeys.size(); i++) {
                DbORConstraint pufKey = (DbORConstraint) pufKeys.get(i);
                for (int j = 0; j < indexes.size(); j++) {
                    DbORIndex index = (DbORIndex) indexes.get(j);
                    if (index.matchesConstraint(pufKey)) {
                        // Avoid matching of non unique indexes with
                        // DbORPrimaryUnique constraint
                        if (pufKey instanceof DbORPrimaryUnique && !index.isUnique())
                            continue;
                        index.setConstraint(pufKey);
                        indexes.remove(j); // remove from the unlinked list
                        break;
                    }
                }
            }
        }
        enumTables.close();
    }

    // Called at the end of the reverse process to deduce the primary/unique key
    // dependencies
    // from the foreign columns present in primary/unique keys.
    public static void reverseDependencies(DbORDataModel model) throws DbException {
        DbEnumeration enumTables = model.getComponents().elements(DbORAbsTable.metaClass);
        while (enumTables.hasMoreElements()) {
            DbORAbsTable table = (DbORAbsTable) enumTables.nextElement();
            DbEnumeration dbEnum = table.getComponents().elements(DbORPrimaryUnique.metaClass);
            while (dbEnum.hasMoreElements())
                reverseDependencies((DbORPrimaryUnique) dbEnum.nextElement());
            dbEnum.close();
        }
        enumTables.close();
    }

    private static void reverseDependencies(DbORPrimaryUnique puKey) throws DbException {
        // First get in a list the foreign columns present in the primary/unique
        // key.
        ArrayList foreignCols = new ArrayList();
        DbRelationN cols = puKey.getColumns();
        int i;
        for (i = 0; i < cols.size(); i++) {
            DbORColumn col = (DbORColumn) cols.elementAt(i);
            if (col.getFKeyColumns().size() != 0)
                foreignCols.add(col);
        }

        while (foreignCols.size() > 0) {
            // For each foreign column, check if one of its foreign keys has all
            // its columns present in the PU key;
            // if one such foreign key is found, add it to the dependencies of
            // the PU key.
            DbORColumn foreignCol = (DbORColumn) foreignCols.get(0);
            DbRelationN foreignKeys = foreignCol.getFKeyColumns();
            for (i = 0; i < foreignKeys.size(); i++) {
                DbORForeign foreignKey = (DbORForeign) foreignKeys.elementAt(i).getComposite();
                if (isFKeyInPUKey(foreignKey, foreignCols, false)) {
                    puKey.addToAssociationDependencies(foreignKey.getAssociationEnd());
                    // Remove the foreign key columns from the list of columns
                    // to be processed
                    isFKeyInPUKey(foreignKey, foreignCols, true);
                    break;
                }
            }
            // Just in case the column is still in the list (i.e. no foreign key
            // was found for the dependency).
            foreignCols.remove(foreignCol);
        }
    }

    // Return true if all the columns of the foreign key are present in the list
    // of PU columns.
    // If purge, remove from the list all the columns of the foreign key.
    private static boolean isFKeyInPUKey(DbORForeign foreignKey, ArrayList puCols, boolean purge)
            throws DbException {
        boolean isPresent = true;
        DbRelationN foreignCols = foreignKey.getComponents();
        for (int i = 0; i < foreignCols.size(); i++) {
            DbORColumn foreignCol = ((DbORFKeyColumn) foreignCols.elementAt(i)).getColumn();
            if (purge) {
                puCols.remove(foreignCol);
            } else if (!puCols.contains(foreignCol)) {
                isPresent = false;
                break;
            }
        }
        return isPresent;
    }

    public static void deduceMultiplicity(DbORAssociation association) throws DbException {
        if (association == null)
            return;
        int child_min = 0;
        int parent_max = Integer.MAX_VALUE; // N

        DbORAssociationEnd frontend = association.getFrontEnd();
        DbORAssociationEnd backend = association.getBackEnd();
        DbORAssociationEnd parent = null;
        DbORAssociationEnd child = null;
        DbORForeign fk = null;

        if ((frontend == null) || (backend == null))
            return;

        // Determines child - parent
        if (frontend.getReferencedConstraint() != null) {
            child = frontend;
            parent = backend;
            fk = frontend.getMember();
        } else if (backend.getReferencedConstraint() != null) {
            child = backend;
            parent = frontend;
            fk = backend.getMember();
        } else {
            return; // ref constraint not set
        }

        if (fk == null)
            return;

        // check child min - if at least one not null column on the fk, set 1,
        // otherwise 0;
        DbEnumeration cols = fk.getComponents().elements(DbORFKeyColumn.metaClass);
        while (cols.hasMoreElements()) {
            DbORColumn col = ((DbORFKeyColumn) cols.nextElement()).getColumn();
            if (col == null)
                continue;
            if (!col.isNull()) {
                child_min = 1;
                break;
            }
        }
        cols.close();

        // check parent max - if unique index or puk on the fk columns, set 1,
        // otherwise N;
        DbORTable childTable = (DbORTable) fk.getComposite();
        DbORIndex fkIndex = fk.getIndex();
        if ((fkIndex != null) && (fkIndex.isUnique()))
            parent_max = 1;
        else {
            // check for other unique in the childTable - with same set of
            // columns ???
        }

        child.setMultiplicity((child_min == 0) ? SMSMultiplicity
                .getInstance(SMSMultiplicity.OPTIONAL) : SMSMultiplicity
                .getInstance(SMSMultiplicity.EXACTLY_ONE));

        parent.setMultiplicity((parent_max == 1) ? SMSMultiplicity
                .getInstance(SMSMultiplicity.OPTIONAL) : SMSMultiplicity
                .getInstance(SMSMultiplicity.MANY));

    }

    public static void deduceMultiplicity(DbORDataModel dataModel) throws DbException {
        DbEnumeration dbEnum = dataModel.getComponents().elements(DbORAssociation.metaClass);
        while (dbEnum.hasMoreElements()) {
            deduceMultiplicity((DbORAssociation) dbEnum.nextElement());
        }
        dbEnum.close();
    }

    public static DbJVClass findClassInOOPackage(DbOOAbsPackage ooPackage, String name)
            throws DbException {
        return findInOOPackage(ooPackage, name, JVClassCategory.CLASS);
    }

    public static DbJVClass findInterfaceInOOPackage(DbOOAbsPackage ooPackage, String name)
            throws DbException {
        return findInOOPackage(ooPackage, name, JVClassCategory.INTERFACE);
    }

    public static DbJVClass findExceptionInOOPackage(DbOOAbsPackage ooPackage, String name)
            throws DbException {
        return findInOOPackage(ooPackage, name, JVClassCategory.EXCEPTION);
    }

    private static DbJVClass findInOOPackage(DbOOAbsPackage ooPackage, String name, int type)
            throws DbException {

        DbJVClass aClass = null;

        DbEnumeration dbEnum = ooPackage.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject child = dbEnum.nextElement();
            if (child instanceof DbJVClass) {
                String childName = child.getSemanticalName(DbObject.LONG_FORM);
                if (name.equals(childName)
                        && ((DbJVClass) child).getStereotype().getValue() == type) {
                    aClass = (DbJVClass) child;
                    break;
                }
            } else if (child instanceof DbJVPackage) {
                aClass = findInOOPackage((DbOOAbsPackage) child, name, type);
                if (aClass != null)
                    break;
            }
        }
        dbEnum.close();
        return aClass;
    }
}
