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

package org.modelsphere.sms;

import java.util.ArrayList;

import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.DbUDF;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.util.AnySemObject;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVCompilationUnit;
import org.modelsphere.sms.oo.java.db.DbJVConstructor;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVImport;
import org.modelsphere.sms.oo.java.db.DbJVInitBlock;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVParameter;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAllowableValue;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORField;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndexKey;
import org.modelsphere.sms.or.db.DbORModel;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORParameter;
import org.modelsphere.sms.or.db.DbORProcedure;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.or.generic.db.DbGEDatabase;
import org.modelsphere.sms.or.ibm.db.DbIBMDatabase;
import org.modelsphere.sms.or.informix.db.DbINFDatabase;
import org.modelsphere.sms.or.informix.db.DbINFDbspace;
import org.modelsphere.sms.or.informix.db.DbINFFragment;
import org.modelsphere.sms.or.informix.db.DbINFSbspace;
import org.modelsphere.sms.or.oracle.db.DbORADataFile;
import org.modelsphere.sms.or.oracle.db.DbORADatabase;
import org.modelsphere.sms.or.oracle.db.DbORALobStorage;
import org.modelsphere.sms.or.oracle.db.DbORANestedTableStorage;
import org.modelsphere.sms.or.oracle.db.DbORAPackage;
import org.modelsphere.sms.or.oracle.db.DbORAPartition;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogFile;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogGroup;
import org.modelsphere.sms.or.oracle.db.DbORARollbackSegment;
import org.modelsphere.sms.or.oracle.db.DbORASequence;
import org.modelsphere.sms.or.oracle.db.DbORASubPartition;
import org.modelsphere.sms.or.oracle.db.DbORATablespace;
import org.modelsphere.sms.plugins.TargetSystem;

public class SMSIntegrateModelUtil {

    private TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    protected final String kSequence = LocaleMgr.misc.getString("ComponentSequence");

    //
    // SINGLETON
    //
    private SMSIntegrateModelUtil() {
    }

    private static SMSIntegrateModelUtil g_singleInstance = null;

    public static SMSIntegrateModelUtil getSingleInstance() {
        if (g_singleInstance == null) {
            g_singleInstance = new SMSIntegrateModelUtil();
        }

        return g_singleInstance;
    } // end getSingleInstance()

    //
    // SINGLETON's methods
    //
    /*
     * private CheckTreeNode getFieldTree(DbSMSPackage leftModel, DbSMSPackage rightModel) {
     * CheckTreeNode rootNode = new CheckTreeNode(null, true, true); int rootID = -1; if (leftModel
     * instanceof DbORModel && leftModel.getMetaClass() == rightModel.getMetaClass()) rootID =
     * AnyORObject.getMetaClassRootID(leftModel.getMetaClass());
     * 
     * if (leftModel instanceof DbORDataModel) getDataModelFieldTree(rootNode, rootID, false); else
     * if (leftModel instanceof DbOROperationLibrary) getOperationLibraryFieldTree(rootNode, rootID,
     * false); else if (leftModel instanceof DbORDomainModel) getDomainModelFieldTree(rootNode,
     * false); else if (leftModel instanceof DbORCommonItemModel)
     * getCommonItemModelFieldTree(rootNode, false); else if (leftModel instanceof DbOOAbsPackage)
     * getOOPackageFieldTree(rootNode, false); else if (leftModel instanceof DbORDatabase)
     * getDatabaseFieldTree(rootNode, rootID, false);
     * 
     * return rootNode; } //end getFieldTree()
     * 
     * private CheckTreeNode getFieldTree(DbSMSPackage model) { return getFieldTree(model, false); }
     */

    /**
     * Build a check tree for all concepts under model
     * 
     * @return the check tree that has been built
     * @param model
     *            either a database, a datamodel, an operation library, etc.
     * @param deepTraversal
     *            if true, traverse operation libary and domain model if linked to a datamodel
     *            (default=false)
     */
    // used by sms.actions.GenerateFromTemplateAction.displayExternalRules()
    public CheckTreeNode getFieldTree(DbSMSPackage model, boolean deepTraversal) throws DbException {
        CheckTreeNode rootNode = new CheckTreeNode(null, true, true);
        int rootID = -1;
        if (model instanceof DbORModel)
            rootID = AnyORObject.getMetaClassRootID(model.getMetaClass());

        // if database, look if it is connected to a datamodel
        if (model instanceof DbORDatabase) {
            DbORDatabase database = (DbORDatabase) model;
            try {
                Db db = database.getDb();
                db.beginTrans(Db.READ_TRANS);
                DbORDataModel datamodel = database.getSchema();
                db.commitTrans();
                if (datamodel != null) {
                    model = datamodel;
                } // end if
            } catch (DbException ex) {
                // do nothing, model stays to refer to an actual database
            }
        } // end if

        // get appropriate field tree, depending the kind of model
        if (model instanceof DbORDataModel) {
            DbORDataModel dataModel = (DbORDataModel) model;
            boolean isSynchro = false;
            getDataModelFieldTree(rootNode, rootID, isSynchro, dataModel);
            if (deepTraversal) {
                doDeepTraversal(rootNode, rootID, (DbORDataModel) model);
            }
        } else {
            DbSMSPackage smsPackage = null;
            if (!(model instanceof DbSMSPackage))
                smsPackage = (DbSMSPackage) model.getCompositeOfType(DbSMSPackage.metaClass);
            else
                smsPackage = (DbSMSPackage) model;

            if (model instanceof DbOROperationLibrary) {
                getOperationLibraryFieldTree(smsPackage, rootNode, rootID, false);
            } else if (model instanceof DbORDomainModel) {
                getDomainModelFieldTree(smsPackage, rootNode, false);
            } else if (model instanceof DbORCommonItemModel) {
                getCommonItemModelFieldTree(smsPackage, rootNode, false);
            } else if (model instanceof DbOOAbsPackage) {
                getOOPackageFieldTree(smsPackage, rootNode, false);
            } else if (model instanceof DbBEModel) {
                getBEModelFieldTree(smsPackage, rootNode, false);
            } else if (model instanceof DbORDatabase) {
                getDatabaseFieldTree(smsPackage, rootNode, rootID, false);
            }
        }
        return rootNode;
    } // end getFieldTree()

    private void doDeepTraversal(CheckTreeNode rootNode, int rootID, DbORDataModel dataModel) {
        try {
            dataModel.getDb().beginReadTrans();
            DbORDatabase database = dataModel.getDeploymentDatabase();
            if (database == null) {
                dataModel.getDb().commitTrans();
                return;
            }

            DbOROperationLibrary operationLibrary = database.getOperationLibrary();
            if (operationLibrary != null) {
                getOperationLibraryFieldTree(dataModel, rootNode, rootID, false);
            }

            DbORDomainModel domainModel = database.getUdtModel();
            if (domainModel != null) {
                getDomainModelFieldTree(domainModel, rootNode, false);
            }

            dataModel.getDb().commitTrans();
        } catch (DbException ex) {
            // do nothing, don't add any new node to the checktree
        } // end try
    } // end doDeepTraversal()

    private void getDataModelFieldTree(CheckTreeNode rootNode, int rootID, boolean isSynchro,
            DbORDataModel datamodel) throws DbException {
        MetaClass[] metaClasses = (rootID == -1 ? AnyORObject.ORMetaClasses : AnyORObject
                .getTargetMetaClasses(rootID));
        CheckTreeNode classNode;

        classNode = getClassNode(datamodel, metaClasses[AnyORObject.I_TABLE], isSynchro);
        if (rootID == TargetSystem.SGBD_INFORMIX_ROOT)
            classNode.add(new CheckTreeNode(DbINFFragment.metaClass, false, true,
                    DbINFFragment.metaClass.getGUIName(true)));
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(datamodel, metaClasses[AnyORObject.I_VIEW], isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(datamodel, metaClasses[AnyORObject.I_COLUMN], isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(datamodel, metaClasses[AnyORObject.I_PRIMARYUNIQUE], isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(datamodel, metaClasses[AnyORObject.I_FOREIGN], isSynchro);
        classNode.add(new CheckTreeNode(DbORFKeyColumn.metaClass, false, true,
                DbORFKeyColumn.metaClass.getGUIName(true)));
        if (isSynchro) {
            classNode.add(new CheckTreeNode(new MetaField[] { DbORForeign.fAssociationEnd,
                    DbORAssociationEnd.fInsertRule }, false, true, DbORAssociationEnd.fInsertRule
                    .getGUIName()));
            classNode.add(new CheckTreeNode(new MetaField[] { DbORForeign.fAssociationEnd,
                    DbORAssociationEnd.fUpdateRule }, false, true, DbORAssociationEnd.fUpdateRule
                    .getGUIName()));
            classNode.add(new CheckTreeNode(new MetaField[] { DbORForeign.fAssociationEnd,
                    DbORAssociationEnd.fDeleteRule }, false, true, DbORAssociationEnd.fDeleteRule
                    .getGUIName()));
            classNode.add(new CheckTreeNode(new MetaField[] { DbORForeign.fAssociationEnd,
                    DbORAssociationEnd.fReferencedConstraint }, false, true,
                    DbORAssociationEnd.fReferencedConstraint.getGUIName()));
        }
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(datamodel, metaClasses[AnyORObject.I_INDEX], isSynchro);
        classNode.add(new CheckTreeNode(DbORIndexKey.metaClass, false, true, DbORIndexKey.metaClass
                .getGUIName(true)));
        if (rootID == TargetSystem.SGBD_INFORMIX_ROOT)
            classNode.add(new CheckTreeNode(DbINFFragment.metaClass, false, true,
                    DbINFFragment.metaClass.getGUIName(true)));
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(datamodel, metaClasses[AnyORObject.I_CHECK], isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(datamodel, metaClasses[AnyORObject.I_TRIGGER], isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        if (!isSynchro) {
            classNode = getClassNode(datamodel, DbORAssociation.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(datamodel, DbORAssociationEnd.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);
        }

        // add target system dependant nodes
        addTargetSystemDependantNodes(rootNode, rootID, isSynchro, classNode, datamodel);

    } // end getDataModelFieldTree()

    private void addTargetSystemDependantNodes(CheckTreeNode rootNode, int rootID,
            boolean isSynchro, CheckTreeNode classNode, DbORDataModel datamodel) throws DbException {

        switch (rootID) {

        case TargetSystem.SGBD_ORACLE_ROOT:
            classNode = getClassNode(datamodel, DbORADatabase.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(datamodel, DbORADataFile.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(datamodel, DbORALobStorage.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(datamodel, DbORANestedTableStorage.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(datamodel, DbORAPartition.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(datamodel, DbORASubPartition.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(datamodel, DbORARedoLogGroup.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(datamodel, DbORARedoLogFile.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(datamodel, DbORARollbackSegment.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(datamodel, DbORASequence.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(datamodel, DbORATablespace.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);
            break;

        case TargetSystem.SGBD_INFORMIX_ROOT:
            classNode = getClassNode(datamodel, DbINFDatabase.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(datamodel, DbINFDbspace.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(datamodel, DbINFSbspace.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);
            break;

        case TargetSystem.SGBD_IBM_DB2_ROOT:
            Db db1 = datamodel.getDb();
            db1.beginTrans(Db.READ_TRANS);
            DbORDatabase database1 = datamodel.getDeploymentDatabase();
            if (database1 != null) {
                classNode = getClassNode(datamodel, DbIBMDatabase.metaClass, isSynchro);
                addClassNode(rootNode, classNode, isSynchro);
            } // end if
            db1.commitTrans();
            break;

        case TargetSystem.SGBD_LOGICAL:
            Db db2 = datamodel.getDb();
            db2.beginTrans(Db.READ_TRANS);
            DbORDatabase database2 = datamodel.getDeploymentDatabase();
            if (database2 != null) {
                classNode = getClassNode(datamodel, DbGEDatabase.metaClass, isSynchro);
                addClassNode(rootNode, classNode, isSynchro);
            } // end if
            db2.commitTrans();
            break;
        } // end switch()
    } // end addTargetSystemDependantNodes()

    private void getOperationLibraryFieldTree(DbSMSPackage datamodel, CheckTreeNode rootNode,
            int rootID, boolean isSynchro) {
        MetaClass[] metaClasses = (rootID == -1 ? AnyORObject.ORMetaClasses : AnyORObject
                .getTargetMetaClasses(rootID));
        CheckTreeNode classNode;

        classNode = getClassNode(datamodel, metaClasses[AnyORObject.I_PROCEDURE], isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(datamodel, metaClasses[AnyORObject.I_PARAMETER], isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        if (rootID == TargetSystem.SGBD_ORACLE_ROOT) {
            classNode = getClassNode(datamodel, DbORAPackage.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);
        }
    } // end getOperationLibraryFieldTree()

    private void getDomainModelFieldTree(DbSMSPackage datamodel, CheckTreeNode rootNode,
            boolean isSynchro) {
        CheckTreeNode classNode;
        classNode = getClassNode(datamodel, DbORDomain.metaClass, isSynchro);
        classNode.add(new CheckTreeNode(DbORAllowableValue.metaClass, false, true,
                DbORAllowableValue.metaClass.getGUIName(true)));
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(datamodel, DbORField.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);
    } // end getDomainModelFieldTree()

    private void getCommonItemModelFieldTree(DbSMSPackage dataModel, CheckTreeNode rootNode,
            boolean isSynchro) {
        CheckTreeNode classNode;
        classNode = getClassNode(dataModel, DbORCommonItem.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);
    } // end getCommonItemModelFieldTree()

    private void getOOPackageFieldTree(DbSMSPackage smsPackage, CheckTreeNode rootNode,
            boolean isSynchro) {
        CheckTreeNode classNode;
        classNode = getClassNode(smsPackage, DbJVClass.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(smsPackage, DbJVDataMember.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(smsPackage, DbJVMethod.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(smsPackage, DbJVConstructor.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(smsPackage, DbJVParameter.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(smsPackage, DbJVInitBlock.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(smsPackage, DbJVCompilationUnit.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(smsPackage, DbJVImport.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);
    } // end getOOPackageFieldTree()

    private void getBEModelFieldTree(DbSMSPackage model, CheckTreeNode rootNode, boolean isSynchro) {
        CheckTreeNode classNode;
        classNode = getClassNode(model, DbBEUseCase.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(model, DbBEActor.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(model, DbBEStore.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(model, DbBEFlow.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(model, DbBEResource.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(model, DbBEQualifier.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);
    } // end getBEModelFieldTree()

    private CheckTreeNode getClassNode(DbSMSPackage smsPackage, MetaClass metaClass,
            boolean isSynchro) {

        Terminology terminology = null;
        try {
            smsPackage.getDb().beginReadTrans();
            terminology = terminologyUtil.findModelTerminology(smsPackage);
            smsPackage.getDb().commitTrans();
        } catch (DbException dbe) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), dbe);
            return null;
        }

        CheckTreeNode classNode = new CheckTreeNode(metaClass, true, true, terminology
                .getTerm(metaClass));
        ArrayList screenFields = metaClass.getScreenMetaFields();
        int i;
        for (i = 0; i < screenFields.size(); i++) {
            MetaField metaField = (MetaField) screenFields.get(i);
            if (!metaField.isEditable())
                continue;
            if (isSynchro
                    && (metaField == DbSemanticalObject.fName || metaField == DbSemanticalObject.fAlias))
                continue;
            if (metaField == DbSemanticalObject.fPhysicalName
                    && !AnySemObject.supportsPhysicalName(metaClass))
                continue;
            if (metaField instanceof MetaRelationship && !isValidRel((MetaRelationship) metaField))
                continue;
            classNode.add(new CheckTreeNode(metaField, false, true, terminology.getTerm(metaClass,
                    metaField)));
        }

        MetaField[] allFields = metaClass.getAllMetaFields();
        for (i = 0; i < allFields.length; i++) {
            MetaField metaField = allFields[i];
            if (metaField instanceof MetaRelationN && isValidRel((MetaRelationship) metaField))
                classNode.add(new CheckTreeNode(metaField, false, true, terminology
                        .getTerm(metaClass)));
        }

        if (DbORAbsTable.metaClass.isAssignableFrom(metaClass)) {
            classNode.add(new CheckTreeNode(DbObject.fComponents, false, true, kSequence + " - "
                    + terminology.getTerm(DbORColumn.metaClass, true)));
        } else if (DbORProcedure.metaClass.isAssignableFrom(metaClass)) {
            classNode.add(new CheckTreeNode(DbObject.fComponents, false, true, kSequence + " - "
                    + terminology.getTerm(DbORParameter.metaClass, true)));
        }

        return classNode;
    } // end getClassNode()

    private void addClassNode(CheckTreeNode rootNode, CheckTreeNode classNode, boolean isSynchro) {
        if (!isSynchro) {
            MetaClass metaClass = (MetaClass) classNode.getUserObject();
            if (DbSemanticalObject.class.isAssignableFrom(metaClass.getJClass())
                    && (metaClass.getFlags() & MetaClass.NO_UDF) == 0)
                classNode
                        .add(new CheckTreeNode(null, false, true, DbUDF.metaClass.getGUIName(true)));
        }
        rootNode.add(classNode);
    } // end addClassNode()

    private void getDatabaseFieldTree(DbSMSPackage smsPackage, CheckTreeNode rootNode, int rootID,
            boolean isSynchro) {
        if (isSynchro)
            return;

        CheckTreeNode classNode;

        if (rootID == TargetSystem.SGBD_ORACLE_ROOT) {
            classNode = getClassNode(smsPackage, DbORADatabase.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(smsPackage, DbORADataFile.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(smsPackage, DbORARedoLogGroup.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(smsPackage, DbORARedoLogFile.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(smsPackage, DbORARollbackSegment.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(smsPackage, DbORATablespace.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);
        }

        else if (rootID == TargetSystem.SGBD_INFORMIX_ROOT) {
            classNode = getClassNode(smsPackage, DbINFDatabase.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(smsPackage, DbINFDbspace.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(smsPackage, DbINFSbspace.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);
        }

        else if (rootID == TargetSystem.SGBD_LOGICAL) {
            classNode = getClassNode(smsPackage, DbORDatabase.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);
        }
    } // end getDatabaseFieldTree()

    // If list of 1-relations to be excluded becomes too long, better define a
    // User Property <not integrable> in GenMeta.
    private boolean isValidRel(MetaRelationship metaRel) {
        if (metaRel instanceof MetaRelationN)
            return (metaRel.getFlags() & MetaField.INTEGRABLE) != 0;
        else
            return (metaRel != DbSMSSemanticalObject.fSuperCopy && metaRel != DbSMSAbstractPackage.fTargetSystem);
    } // end isValidRel()

} // end SMSIntegrateModelUtil
