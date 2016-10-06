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

package org.modelsphere.sms.features.reverse;

//Java
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.oo.db.DbOOAbstractMethod;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVCompilationUnit;

public final class ReverseTask extends org.modelsphere.jack.srtool.reverse.file.ReverseTask {

    public ReverseTask(Vector aFileVector, ArrayList aReverseList, DbProject targetProj) {
        super(aFileVector, aReverseList, targetProj, MainFrame.getSingleton());
    }

    private boolean isEmpty(DbJVCompilationUnit compilUnit) throws DbException {
        return compilUnit.getNbNeighbors(DbJVCompilationUnit.fClasses) == 0;
    }

    private boolean comeFromCompiledFile(DbJVClass adt) throws DbException {
        while (true) {
            DbObject parent = adt.getComposite();
            if (!(parent instanceof DbJVClass))
                break;
            adt = (DbJVClass) parent;
        }
        DbJVCompilationUnit compilUnit = adt.getCompilationUnit();
        return (compilUnit != null && compilUnit.isCompiled());
    }

    // delete empty compilation units
    private void deleteEmptyCompilUnits(DbObject dboDestination) throws DbException {
        Db db = dboDestination.getDb();
        db.beginTrans(Db.WRITE_TRANS);
        DbEnumeration dbEnum = dboDestination.componentTree(DbJVCompilationUnit.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVCompilationUnit compilUnit = (DbJVCompilationUnit) dbEnum.nextElement();
            if (isEmpty(compilUnit))
                compilUnit.remove();
        }
        dbEnum.close();
        db.commitTrans();
    }

    // mark each adt without body (just there for referencing purposes)
    private void markBodyLessAdts(DbObject dboDestination) throws DbException {
        Db db = dboDestination.getDb();
        db.beginTrans(Db.READ_TRANS);
        DbEnumeration dbEnum = dboDestination.componentTree(DbJVClass.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVClass adt = (DbJVClass) dbEnum.nextElement();
            /****
             * MODIFY if ((adt.bodyAvailable != null) && (! adt.bodyAvailable.booleanValue())) {
             * String msg = (adt.isInterface() ?
             * LocaleMgr.message.getString("InterfaceBodyNotAvailableComment") :
             * LocaleMgr.message.getString("ClassBodyNotAvailableComment"));
             * adt.setDescription(msg); }
             */
        }
        dbEnum.close();
        db.commitTrans();
    }

    // mark each operation in compiled compilation unit
    private void markBodyLessOperations(DbObject dboDestination) throws DbException {
        Db db = dboDestination.getDb();
        db.beginTrans(Db.READ_TRANS);
        DbEnumeration enumAdts = dboDestination.componentTree(DbJVClass.metaClass);
        while (enumAdts.hasMoreElements()) {
            DbJVClass adt = (DbJVClass) enumAdts.nextElement();
            if (!comeFromCompiledFile(adt))
                continue;
            DbEnumeration enumOpers = adt.getComponents().elements(DbOOAbstractMethod.metaClass);
            while (enumOpers.hasMoreElements()) {
                DbOOAbstractMethod oper = (DbOOAbstractMethod) enumOpers.nextElement();
                /****
                 * MODIFY oper.bodyAvailable = Boolean.FALSE; String msg = (oper instanceof
                 * DbOOConstructor ? LocaleMgr.message.getString(
                 * "ConstructorBodyNotAvailableComment") : LocaleMgr.message.getString
                 * ("MethodBodyNotAvailableComment")); oper.setBody("\n  " + msg + "\n"); //NOT
                 * LOCALIZABLE
                 */
            }
            enumOpers.close();
        }
        enumAdts.close();
        db.commitTrans();
    }

    protected final void clearObjects(Vector obsoleteObjectVector, DbObject dboDestination)
            throws DbException {

        // delete each obsolete object
        Enumeration enumeration = obsoleteObjectVector.elements();
        while (enumeration.hasMoreElements()) {
            DbObject object = (DbObject) enumeration.nextElement();
            object.remove();
        } // end while

        // delete each empty compilation unit
        deleteEmptyCompilUnits(dboDestination);

        // mark each adt without body (just there for referencing purposes)
        markBodyLessAdts(dboDestination);

        // mark each operation in compiled compilation unit
        markBodyLessOperations(dboDestination);
    }

    protected String reverseCurrentFile(Hashtable modelTable,
            org.modelsphere.jack.srtool.reverse.file.JackReverseEngineeringPlugin reverse,
            Class modelClaz, Vector obsoleteObjectVector, HashMap diagMap, File file)
            throws Exception {
        String errMsg;
        /*
         * DbSMSPackage pack; FocusManager fm = ApplicationContext.getFocusManager(); DbProject
         * project = fm.getCurrentProject(); Db db = project.getDb();
         * 
         * Reverse javaReverse = (Reverse)reverse;
         * 
         * //if class already in hash table, then get its package //otherwise, create an instance of
         * the package //and put the package and its instance in hash table if
         * (modelTable.containsKey(modelClaz)) { pack = (DbSMSPackage)modelTable.get(modelClaz); }
         * else { //pack = javaReverse.createNewPackage(modelClaz, project);
         * //modelTable.put(modelClaz, pack); }
         */
        errMsg = null; // javaReverse.reverseFile(pack, logBuffer, this,
        // options, file);
        return errMsg;
    }
} // end ReverseTask
