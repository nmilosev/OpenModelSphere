/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.oo.java;

import java.util.ArrayList;
import java.util.Iterator;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.db.event.DbUpdateListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdatePassListener;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.sms.oo.java.db.DbJVAssociationEnd;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVInheritance;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVParameter;

public final class JavaSemanticalIntegrity implements DbUpdateListener, DbUpdatePassListener {

    private static JavaSemanticalIntegrity singleton = null;

    private ArrayList toUpdate = new ArrayList();

    {
        singleton = this;
    }

    public JavaSemanticalIntegrity() {
        MetaField.addDbUpdateListener(this, 0, new MetaField[] { DbObject.fComposite/*
                                                                                     * ,DbObject.
                                                                                     * fComponents
                                                                                     */,
                DbSemanticalObject.fName, DbJVMethod.fStatic, DbJVMethod.fAbstract,
                DbJVMethod.fVisibility, DbJVMethod.fFinal, DbJVDataMember.fStatic,
                DbJVDataMember.fVisibility, DbJVDataMember.fFinal, DbJVParameter.fType,
                DbJVInheritance.fSubClass, DbJVInheritance.fSuperClass,
                DbJVAssociationEnd.fNavigable, DbJVAssociationEnd.fMultiplicity });
        Db.addDbUpdatePassListener(this);
    }

    // //////////////////////////////
    // DbUpdateListener SUPPORT
    //
    public void dbUpdated(DbUpdateEvent event) throws DbException {
        if (event.dbo.getDb().getTransMode() == Db.TRANS_LOAD || !event.dbo.getDb().isValid()
                || !event.dbo.isAlive() || (event.dbo.getDb().getRoot() == null))
            return;
        if (event.metaField == DbObject.fComposite) {
            if ((event.dbo instanceof DbJVMethod || event.dbo instanceof DbJVDataMember || event.dbo instanceof DbJVInheritance)
                    && (event.dbo.getTransStatus() == Db.OBJ_ADDED || event.dbo.getTransStatus() == Db.OBJ_REMOVED)) {
                if (event.dbo instanceof DbJVInheritance
                        && event.dbo.getTransStatus() == Db.OBJ_REMOVED)
                    processUpdatedObject(event.dbo, true, true);
                else
                    processUpdatedObject(event.dbo, false, true);
            }
        } else if (event.metaField == DbSemanticalObject.fName
                || event.metaField == DbJVMethod.fStatic || event.metaField == DbJVMethod.fAbstract
                || event.metaField == DbJVMethod.fVisibility
                || event.metaField == DbJVMethod.fFinal
                || event.metaField == DbJVDataMember.fStatic
                || event.metaField == DbJVDataMember.fVisibility
                || event.metaField == DbJVDataMember.fFinal
                || event.metaField == DbJVParameter.fType) {
            if (event.dbo instanceof DbJVMethod || event.dbo instanceof DbJVDataMember
                    || event.dbo instanceof DbJVParameter) {
                processUpdatedObject(event.dbo, false, true);
            }
        } else if (event.metaField == DbJVInheritance.fSubClass
                || event.metaField == DbJVInheritance.fSuperClass) {
            processUpdatedObject(event.dbo, false, true);
        } else if ((event.dbo instanceof DbJVAssociationEnd)
                && (event.metaField == DbJVAssociationEnd.fNavigable || event.metaField == DbJVAssociationEnd.fMultiplicity)) {
            processUpdatedObject(((DbJVAssociationEnd) event.dbo).getAssociationMember(), false,
                    true);
        }

    }

    //
    // End of DbUpdateListener SUPPORT
    // ////////////////////////////////

    private void processUpdatedObject(DbObject dbo, boolean includeSuper, boolean includeSub)
            throws DbException {
        ArrayList updatedObjects = new ArrayList();
        if (dbo == null)
            return;
        add(updatedObjects, dbo);
        if (includeSuper)
            addSuperClasses(updatedObjects, dbo);
        if (includeSub)
            addSubClasses(updatedObjects, dbo);
        addOffline(updatedObjects);
    }

    private void add(ArrayList updatedObjects, DbObject dbo) throws DbException {
        if (dbo == null || updatedObjects == null)
            return;
        DbJVClass clas = null;
        if (dbo instanceof DbJVClass) {
            clas = (DbJVClass) dbo;
        } else {
            clas = (DbJVClass) dbo.getCompositeOfType(DbJVClass.metaClass);
        }
        if (clas == null)
            return;
        updatedObjects.add(clas);
    }

    private void addSubClasses(ArrayList updatedObjects, DbObject dbo) throws DbException {
        if (dbo == null)
            return;
        DbJVClass clas = null;
        if (dbo instanceof DbJVClass) {
            clas = (DbJVClass) dbo;
        } else {
            clas = (DbJVClass) dbo.getCompositeOfType(DbJVClass.metaClass);
        }
        if (clas == null)
            return;
        if (clas.getSubInheritances() != null && clas.getSubInheritances().size() > 0) {
            ArrayList subclasses = new ArrayList();
            addSub(subclasses, clas);
            Iterator iter = subclasses.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                if (!updatedObjects.contains(obj))
                    updatedObjects.add(obj);
            }
        }
    }

    private void addSuperClasses(ArrayList updatedObjects, DbObject dbo) throws DbException {
        if (dbo == null)
            return;
        DbJVClass clas = null;
        if (dbo instanceof DbJVClass) {
            clas = (DbJVClass) dbo;
        } else {
            clas = (DbJVClass) dbo.getCompositeOfType(DbJVClass.metaClass);
        }
        if (clas == null)
            return;
        if (clas.getSuperInheritances() != null && clas.getSuperInheritances().size() > 0) {
            ArrayList superclasses = new ArrayList();
            addSuper(superclasses, clas);
            Iterator iter = superclasses.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                if (!updatedObjects.contains(obj))
                    updatedObjects.add(obj);
            }
        }
    }

    private void addSub(ArrayList subclasses, DbObject dbo) throws DbException {
        if (dbo == null || !(dbo instanceof DbJVClass))
            return;
        DbEnumeration dbEnum = ((DbJVClass) dbo).getSubInheritances().elements(
                DbJVInheritance.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVInheritance inher = (DbJVInheritance) dbEnum.nextElement();
            DbJVClass sub = (DbJVClass) inher.getSubClass();
            if (!subclasses.contains(sub)) {
                subclasses.add(sub);
                addSub(subclasses, sub);
            }
        }
        dbEnum.close();
    }

    private void addSuper(ArrayList superclasses, DbObject dbo) throws DbException {
        if (dbo == null || !(dbo instanceof DbJVClass))
            return;
        DbEnumeration dbEnum = ((DbJVClass) dbo).getSuperInheritances().elements(
                DbJVInheritance.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVInheritance inher = (DbJVInheritance) dbEnum.nextElement();
            DbJVClass superc = (DbJVClass) inher.getSuperClass();
            if (!superclasses.contains(superc)) {
                superclasses.add(superc);
                addSuper(superclasses, superc);
            }
        }
        dbEnum.close();
    }

    private void addOffline(ArrayList dbos) {
        if (dbos == null || dbos.size() == 0)
            return;

        for (int i = 0; i < dbos.size(); i++) {
            if (!toUpdate.contains(dbos.get(i))) {
                toUpdate.add(dbos.get(i));
            }
        }
    }

    // Must be called within a WRITE TRANS on the specified Db
    private void fireOfflineUpdates(Db db) throws DbException {
        Iterator iter = toUpdate.iterator();
        ArrayList dbUpdate = new ArrayList();
        while (iter.hasNext()) {
            DbObject dbo = (DbObject) iter.next();
            if (dbo.getDb() != db)
                continue;
            iter.remove();
            dbUpdate.add(dbo);
        }
        DbObject[] dbos = new DbObject[dbUpdate.size()];
        for (int i = 0; i < dbos.length; i++) {
            dbos[i] = (DbObject) dbUpdate.get(i);
        }
        if (dbos.length == 0)
            return;
        new JavaOverridingUpdate(dbos);
    }

    public void beforeUpdatePass(Db db) throws DbException {
    }

    public void afterUpdatePass(Db db) throws DbException {
        fireOfflineUpdates(db);
    }

}
