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

package org.modelsphere.sms.db.util;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.DbTreeLookupDialog;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModelListener;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSUserDefinedPackage;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVCompilationUnit;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORModel;

public final class SMSMove {

    private DbObject[] dbos;
    private DbObject oldParent;
    private DbObject newParent;

    private SMSMove(DbObject[] dbos) {
        this.dbos = dbos;
        if (selectNewParent())
            doMove();
    }

    private SMSMove(DbObject[] dbos, DbObject newParent) {
        this.dbos = dbos;
        this.newParent = newParent;
        doMove();
    }

    /*
     * Move a set of objects belonging to the same parent to a new parent.
     */
    public static void move(DbObject[] dbos) {
        new SMSMove(dbos);
    }

    public static void move(DbObject[] dbos, DbObject newParent) {
        new SMSMove(dbos, newParent);
    }

    /*
     * Check that the selection is valid for the move operation. All selected items must belong to
     * the same parent.
     */
    public static boolean isValidForMove(DbObject[] dbos) throws DbException {
        if (dbos.length == 0)
            return false;
        Db db = dbos[0].getDb();
        db.beginTrans(Db.READ_TRANS);
        DbObject parent = dbos[0].getComposite();
        int i;
        for (i = 0; i < dbos.length; i++) {
            DbObject dbo = dbos[i];
            if (!(dbo instanceof DbSMSUserDefinedPackage || dbo instanceof DbORModel
                    || dbo instanceof DbORCommonItemModel || dbo instanceof DbOOAbsPackage
                    || dbo instanceof DbJVClass || dbo instanceof DbBEModel))
                break;
            if (dbo.getDb() != db || dbo.getComposite() != parent)
                break;
            if (dbo instanceof DbORModel && parent instanceof DbORModel)
                break; // cannot move a sub-schema.
        }
        db.commitTrans();
        return (i == dbos.length);
    }

    /*
     * Check that <newParent> is valid as new parent for all objects in <dbos>. <dbos> is already
     * checked as valid for the move.
     */
    public static boolean isNewParentValid(DbObject[] dbos, DbObject newParent) throws DbException {
        // All moved objects have the same parent.
        boolean javaMove = (dbos[0] instanceof DbJVPackage || dbos[0] instanceof DbJVClass);
        boolean valid;
        if (javaMove) {
            if (newParent instanceof DbJVClass) {
                valid = true;
                for (int i = 0; i < dbos.length && valid; i++)
                    valid = (dbos[i] instanceof DbJVClass);
            } else
                valid = (newParent instanceof DbJVPackage || newParent instanceof DbJVClassModel);
        } else
            valid = (newParent instanceof DbSMSProject || newParent instanceof DbSMSUserDefinedPackage);

        if (valid)
            valid = (newParent.getProject() == dbos[0].getProject());
        if (valid) {
            newParent.getDb().beginTrans(Db.READ_TRANS);
            DbObject oldParent = dbos[0].getComposite();
            valid = (newParent != oldParent);
            if (valid) {
                // Check that the new parent is not a descendant of one of the
                // moved objects.
                DbObject current, parent;
                for (current = newParent; (parent = current.getComposite()) != null; current = parent) {
                    if (parent == oldParent) {
                        valid = !contains(dbos, current);
                        break;
                    }
                }
            }
            newParent.getDb().commitTrans();
        }
        return valid;
    }

    private static boolean contains(DbObject[] dbos, DbObject dbo) {
        for (int i = 0; i < dbos.length; i++) {
            if (dbo == dbos[i])
                return true;
        }
        return false;
    }

    /*
     * Display a tree dialog allowing the user to select the parent under which to move
     */
    private boolean selectNewParent() {
        boolean javaMove = (dbos[0] instanceof DbJVPackage || dbos[0] instanceof DbJVClass);
        MetaClass[] metaClasses;
        if (javaMove) {
            metaClasses = new MetaClass[] { DbJVClassModel.metaClass, DbJVPackage.metaClass,
                    DbJVClass.metaClass };
            for (int i = 0; i < dbos.length; i++) {
                if (dbos[i] instanceof DbJVPackage) {
                    metaClasses = new MetaClass[] { DbJVClassModel.metaClass, DbJVPackage.metaClass };
                    break;
                }
            }
        } else
            metaClasses = new MetaClass[] { DbSMSProject.metaClass,
                    DbSMSUserDefinedPackage.metaClass };

        DbTreeModelListener modelListener = new DbTreeModelListener() {
            public boolean filterNode(DbObject dbo) throws DbException {
                return !contains(dbos, dbo);
            }
        };
        newParent = (DbObject) DbTreeLookupDialog.selectOne(ApplicationContext
                .getDefaultMainFrame(), LocaleMgr.screen.getString("SelectNode"),
                new DbObject[] { dbos[0].getProject() }, metaClasses, modelListener, null, null);
        return (newParent != null);
    }

    private void doMove() {
        try {
            boolean javaMove = (dbos[0] instanceof DbJVPackage || dbos[0] instanceof DbJVClass);
            newParent.getDb().beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("Move"));
            oldParent = dbos[0].getComposite();
            if (newParent != oldParent) {
                for (int i = 0; i < dbos.length; i++) {
                    DbObject dbo = dbos[i];
                    dbo.setComposite(newParent);
                    if (javaMove) {
                        removeOldGos(dbo);
                        if (dbo instanceof DbJVClass)
                            moveCompilUnit((DbJVClass) dbo);
                    }
                }
            }
            newParent.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }

    // Move the compilation unit to the new parent only if all the adts in it
    // are moved.
    private void moveCompilUnit(DbJVClass adt) throws DbException {
        if (newParent instanceof DbJVClass) {
            adt.setCompilationUnit(null); // no compilation unit for an inner
            // class.
            return;
        }
        DbJVCompilationUnit compil = adt.getCompilationUnit();
        if (compil == null)
            return;
        if (compil.getComposite() == newParent) // already moved from a previous
            // adt
            return;
        DbRelationN adts = compil.getClasses();
        int nb = adts.size();
        if (nb > 1) {
            for (int i = 0; i < nb; i++) {
                if (!contains(dbos, adts.elementAt(i))) {
                    adt.setCompilationUnit(null);
                    return;
                }
            }
        }
        compil.setComposite(newParent);
    }

    /*
     * Remove all the graphical repres. of the moved adt/package in the diagrams of the old parent,
     * except the graphical repres. that have connectors.
     */
    private void removeOldGos(DbObject dbo) throws DbException {
        if (oldParent instanceof DbJVClass) // previously inner class
            return;
        DbEnumeration dbEnum = (dbo instanceof DbSMSClassifier ? ((DbSMSClassifier) dbo)
                .getClassifierGos().elements() : ((DbSMSPackage) dbo).getPackageGos().elements());
        while (dbEnum.hasMoreElements()) {
            DbSMSGraphicalObject go = (DbSMSGraphicalObject) dbEnum.nextElement();
            if (oldParent == go.getComposite().getComposite()
                    && go.getFrontEndLineGos().size() == 0 && go.getBackEndLineGos().size() == 0)
                go.remove();
        }
        dbEnum.close();
    }
}
