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

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.actions.ShowDiagramAction;
import org.modelsphere.jack.srtool.reverse.file.AbstractReverser;
import org.modelsphere.jack.srtool.reverse.file.JackReverseEngineeringPlugin;
import org.modelsphere.jack.srtool.reverse.file.ReverseFileOptions;
import org.modelsphere.sms.actions.CreateMissingGraphicsAction;
import org.modelsphere.sms.actions.LayoutSelectionAction;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.oo.db.DbOOAbstractMethod;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVCompilationUnit;

public class ConcreteReverser extends AbstractReverser {

    public void preparePackageForDisplay(DbObject pack) throws Exception {

        pack.getDb().beginReadTrans();
        DbObject dbo = pack.getCompositeOfType(pack.getMetaClass());
        pack.getDb().commitTrans();
        pack = ((dbo == null) ? pack : dbo);
        DbOODiagram packdgm = null;

        if (pack instanceof DbJVClassModel) {
            pack.getDb().beginWriteTrans("");

            packdgm = new DbOODiagram(pack);

            CreateMissingGraphicsAction action2 = (CreateMissingGraphicsAction) ApplicationContext
                    .getActionStore().get(SMSActionsStore.CREATE_MISSING_GRAPHICS);
            action2.performAction(packdgm);

            LayoutSelectionAction action3 = (LayoutSelectionAction) ApplicationContext
                    .getActionStore().get(SMSActionsStore.LAYOUT_SELECTION);
            action3.performAction(new GraphicComponent[] {}, packdgm);

            ShowDiagramAction action = (ShowDiagramAction) ApplicationContext.getActionStore().get(
                    AbstractActionsStore.SHOW_DIAGRAM);
            action.performAction(packdgm);

            pack.getDb().commitTrans();
        }

        if (packdgm != null)
            ApplicationContext.getDefaultMainFrame().findInExplorer(packdgm);

    }

    public ConcreteReverser() {

    }

    public String reverseCurrentFile(DbObject reverseEngineeredPackage,
            JackReverseEngineeringPlugin reverse, /* Class modelClaz, */
            Vector obsoleteObjectVector, HashMap diagMap, File file, DbProject project,
            StringWriter logBuffer) throws Exception {

        String errMsg;
        DbSMSPackage pack = (DbSMSPackage) reverseEngineeredPackage;
        //Reverse javaReverse = (Reverse)reverse;

        /*
         * //if class already in hash table, then get its package //otherwise, create an instance of
         * the package //and put the package and its instance in hash table if
         * (modelTable.containsKey(modelClaz)) { pack = (DbSMSPackage)modelTable.get(modelClaz); }
         * else { pack = (DbSMSPackage)reverse.createNewPackage(modelClaz, project);
         * modelTable.put(modelClaz, pack); } //end if
         */

        ReverseFileOptions options = new ReverseFileOptions(obsoleteObjectVector, diagMap,
                m_controller, m_fileLength, m_totalSize);
        errMsg = reverse.reverseFile(pack, logBuffer, options, file);
        //for all diagrams, create graphical objects 

        ArrayList<DbOODiagram> diagrams = new ArrayList<DbOODiagram>();
        pack.getDb().beginReadTrans();
        DbEnumeration dbEnum = pack.componentTree(DbOODiagram.metaClass);
        while (dbEnum.hasMoreElements())
            diagrams.add((DbOODiagram) dbEnum.nextElement());
        dbEnum.close();
        pack.getDb().commitTrans();

        pack.getDb().beginWriteTrans("");
        for (int i = 0; i < diagrams.size(); i++) {
            CreateMissingGraphicsAction action2 = (CreateMissingGraphicsAction) ApplicationContext
                    .getActionStore().get(SMSActionsStore.CREATE_MISSING_GRAPHICS);
            action2.performAction(diagrams.get(i));
            LayoutSelectionAction action3 = (LayoutSelectionAction) ApplicationContext
                    .getActionStore().get(SMSActionsStore.LAYOUT_SELECTION);
            action3.performAction(new GraphicComponent[] {}, diagrams.get(i));
        }
        pack.getDb().commitTrans();

        return errMsg;

    }

    public void clearObjects(Vector obsoleteObjectVector, DbObject dboDestination)
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
    } // clearObjects

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
    } // deleteEmptyCompilUnits

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

} // end ConcreteReverser
