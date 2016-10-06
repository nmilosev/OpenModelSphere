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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.Icon;

import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.DbUDF;
import org.modelsphere.jack.baseDb.db.DeepCopy;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelation1;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.explorer.GroupParams;
import org.modelsphere.jack.srtool.integrate.IntegrateModel;
import org.modelsphere.jack.srtool.integrate.IntegrateNode;
import org.modelsphere.jack.srtool.integrate.IntegrateScopeDialog;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEActorGo;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEStoreGo;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.util.AnySemObject;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.db.DbOOAbstractMethod;
import org.modelsphere.sms.oo.db.DbOOClass;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.DbOOInheritance;
import org.modelsphere.sms.oo.db.DbOOOperation;
import org.modelsphere.sms.oo.db.DbOOParameter;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVCompilationUnit;
import org.modelsphere.sms.oo.java.db.DbJVConstructor;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVImport;
import org.modelsphere.sms.oo.java.db.DbJVInheritance;
import org.modelsphere.sms.oo.java.db.DbJVInitBlock;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVParameter;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAllowableValue;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORConstraint;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORField;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORIndexKey;
import org.modelsphere.sms.or.db.DbORModel;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORPackage;
import org.modelsphere.sms.or.db.DbORParameter;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORProcedure;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTrigger;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.db.DbORUserNode;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.db.srtypes.ORDomainCategory;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.or.generic.db.DbGEDatabase;
import org.modelsphere.sms.or.ibm.db.DbIBMDatabase;
import org.modelsphere.sms.or.informix.db.DbINFDatabase;
import org.modelsphere.sms.or.informix.db.DbINFDbspace;
import org.modelsphere.sms.or.informix.db.DbINFFragment;
import org.modelsphere.sms.or.informix.db.DbINFSbspace;
import org.modelsphere.sms.or.oracle.db.DbORAAbsPartition;
import org.modelsphere.sms.or.oracle.db.DbORAAbsTable;
import org.modelsphere.sms.or.oracle.db.DbORADataFile;
import org.modelsphere.sms.or.oracle.db.DbORADataModel;
import org.modelsphere.sms.or.oracle.db.DbORADatabase;
import org.modelsphere.sms.or.oracle.db.DbORAIndex;
import org.modelsphere.sms.or.oracle.db.DbORALobStorage;
import org.modelsphere.sms.or.oracle.db.DbORANestedTableStorage;
import org.modelsphere.sms.or.oracle.db.DbORAPackage;
import org.modelsphere.sms.or.oracle.db.DbORAPartition;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogFile;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogGroup;
import org.modelsphere.sms.or.oracle.db.DbORARollbackSegment;
import org.modelsphere.sms.or.oracle.db.DbORASequence;
import org.modelsphere.sms.or.oracle.db.DbORASubPartition;
import org.modelsphere.sms.or.oracle.db.DbORATable;
import org.modelsphere.sms.or.oracle.db.DbORATablespace;
import org.modelsphere.sms.plugins.TargetSystem;
import org.modelsphere.sms.plugins.TargetSystemInfo;
import org.modelsphere.sms.plugins.TargetSystemManager;

public class SMSIntegrateModel extends IntegrateModel {
    protected static final String kDate = LocaleMgr.misc.getString("Date");
    protected static final String kUnspecifiedUser = LocaleMgr.misc.getString("UnspecifiedUser");
    protected static final String kSequence = LocaleMgr.misc.getString("ComponentSequence");

    private static TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    // used to define a node with a custom path for accessing parent side
    // association end values
    // Do not modify the string. It is serialized in .scp file for synchro
    protected static final String FK_PARENT_INSERT_RULE = "FK_PARENT_INSERT_RULE"; // NOT
    // LOCALIZABLE
    protected static final String FK_PARENT_UPDATE_RULE = "FK_PARENT_UPDATE_RULE"; // NOT
    // LOCALIZABLE
    protected static final String FK_PARENT_DELETE_RULE = "FK_PARENT_DELETE_RULE"; // NOT
    // LOCALIZABLE

    private static final String[] synchroColHeaders = new String[] {
            LocaleMgr.screen.getString("SMSModel"), LocaleMgr.screen.getString("PhysicalModel"),
            LocaleMgr.screen.getString("Action") };

    private static final String[] synchroPropHeaders = new String[] {
            LocaleMgr.screen.getString("Property"), LocaleMgr.screen.getString("SMSValue"),
            LocaleMgr.screen.getString("PhysicalValue"), LocaleMgr.screen.getString("Action") };

    private static final String[] synchroActionNames = new String[] {
            LocaleMgr.screen.getString("None"), LocaleMgr.screen.getString("AddInSMS"),
            LocaleMgr.screen.getString("DeleteInSMS"), LocaleMgr.screen.getString("MergeInSMS"),
            LocaleMgr.screen.getString("ReplaceInSMS"), LocaleMgr.screen.getString("ModifyInSMS"),
            LocaleMgr.screen.getString("AddInPhysical"),
            LocaleMgr.screen.getString("DeleteInPhysical"),
            LocaleMgr.screen.getString("MergeInPhysical"),
            LocaleMgr.screen.getString("ReplaceInPhysical"),
            LocaleMgr.screen.getString("ModifyInPhysical") };

    private static final String synchroFrameTitle = LocaleMgr.screen.getString("Synchronization");
    private static final String synchroButtonName = LocaleMgr.screen.getString("Synchronize");
    private static final String synchroActionName = LocaleMgr.action.getString("synchronization");

    private static final int NO_SUPER = 0;
    private static final int RIGHT_SUPER = 1;
    private static final int LEFT_SUPER = 2;

    private TargetSystemInfo leftTSInfo = null;
    private TargetSystemInfo rightTSInfo = null;
    private DbORDomainModel leftDomainModel = null;
    private DbORDomainModel rightDomainModel = null;
    private boolean differentTS = false; // true if different DBMS's or
    // different versions of the same
    // DBMS
    private int rootID = -1; // -1 if different DBMS's
    private int matchType; // RIGHT_SUPER or LEFT_SUPER means to reflect object
    // matching/unmatching on the <superCopy> relation
    private boolean loadDatabase = false; // true means to integrate the
    // database as part of the model
    private boolean loadDomainModel = false;
    private boolean loadOperationLibrary = false;
    private boolean loadDataModel = false;
    private boolean loadUdtModel = false;
    private boolean syncUser;
    private boolean isSynchro;

    // Entry point for integration
    public SMSIntegrateModel(DbObject leftModel, DbObject rightModel, CheckTreeNode fieldTree,
            boolean usePhysName, boolean ignoreCase, boolean syncUser) throws DbException {
        this(leftModel, rightModel, fieldTree, false, usePhysName, ignoreCase, syncUser, false);
    }

    // Entry point for integration
    public SMSIntegrateModel(DbObject leftModel, DbObject rightModel, CheckTreeNode fieldTree,
            boolean externalMatchByName, boolean usePhysName, boolean ignoreCase, boolean syncUser)
            throws DbException {
        this(leftModel, rightModel, fieldTree, externalMatchByName, usePhysName, ignoreCase,
                syncUser, false);
    }

    // Entry point for synchro subclass: super(leftModel, rightModel, fieldTree,
    // usePhysName, ignoreCase, syncUser, true);
    protected SMSIntegrateModel(DbObject leftModel, DbObject rightModel, CheckTreeNode fieldTree,
            boolean externalMatchByName, boolean usePhysName, boolean ignoreCase, boolean syncUser,
            boolean isSynchro) throws DbException {
        super(leftModel, rightModel, fieldTree, externalMatchByName, usePhysName, ignoreCase,
                isSynchro);
        this.syncUser = syncUser;
        this.isSynchro = isSynchro;
        if (isSynchro) {
            colHeaders = synchroColHeaders;
            propHeaders = synchroPropHeaders;
            actionNames = synchroActionNames;
            frameTitle = synchroFrameTitle;
            integButtonName = synchroButtonName;
            integActionName = synchroActionName;
        }
        DbSMSTargetSystem leftTS = AnyORObject.getTargetSystem(leftModel);
        if (leftTS != null) {
            leftTSInfo = TargetSystemManager.getSingleton().getTargetSystemInfo(leftTS);
            rightTSInfo = TargetSystemManager.getSingleton().getTargetSystemInfo(
                    AnyORObject.getTargetSystem(rightModel));
            differentTS = (leftTSInfo.getID() != rightTSInfo.getID());
            if (leftTSInfo.getRootID() == rightTSInfo.getRootID())
                rootID = leftTSInfo.getRootID();
        }
        matchActionName = LocaleMgr.action.getString("setSuperLink");
        matchType = NO_SUPER;
        if (leftModel instanceof DbORDataModel) {
            if (rightModel == leftModel.getComposite())
                matchType = RIGHT_SUPER;
            else if (leftModel == rightModel.getComposite())
                matchType = LEFT_SUPER;
        }

        if (leftModel instanceof DbORDataModel) {
            DbORDatabase leftBase = ((DbORDataModel) leftModel).getDeploymentDatabase();
            DbORDatabase rightBase = ((DbORDataModel) rightModel).getDeploymentDatabase();
            if (leftBase != null)
                leftBase.setMatchingObject(rightBase);
            else if (rightBase != null)
                rightBase.setMatchingObject(null);

            if (leftBase != null && rightBase != null && leftBase != rightBase) {
                if (leftBase.getDefaultDomainModel() != null
                        && rightBase.getDefaultDomainModel() != null) {
                    leftDomainModel = (DbORDomainModel) leftBase.getDefaultDomainModel();
                    rightDomainModel = (DbORDomainModel) rightBase.getDefaultDomainModel();
                    leftDomainModel.setMatchingObject(rightDomainModel);
                }
                // loadDatabase = (leftBase.getClass() == rightBase.getClass());
            }
        } else if (leftModel instanceof DbORDatabase && isSynchro) {
            DbORDatabase leftBase = (DbORDatabase) leftModel;
            DbORDatabase rightBase = (DbORDatabase) rightModel;
            leftBase.setMatchingObject(rightBase);

            loadDataModel = (leftBase.getSchema() != null && rightBase.getSchema() != null);
            if (loadDataModel)
                leftBase.getSchema().setMatchingObject(rightBase.getSchema());

            loadOperationLibrary = (leftBase.getOperationLibrary() != null && rightBase
                    .getOperationLibrary() != null);
            if (loadOperationLibrary)
                leftBase.getOperationLibrary().setMatchingObject(rightBase.getOperationLibrary());

            if (leftBase.getDefaultDomainModel() != null
                    && rightBase.getDefaultDomainModel() != null) {
                leftDomainModel = (DbORDomainModel) leftBase.getDefaultDomainModel();
                rightDomainModel = (DbORDomainModel) rightBase.getDefaultDomainModel();
                leftDomainModel.setMatchingObject(rightDomainModel);
            }
            loadDomainModel = (leftDomainModel != null);

            loadUdtModel = (leftBase.getUdtModel() != null && rightBase.getUdtModel() != null
                    && leftBase.getUdtModel() != leftBase.getDefaultDomainModel() && rightBase
                    .getUdtModel() != rightBase.getDefaultDomainModel());
            if (loadUdtModel) {
                leftBase.getUdtModel().setMatchingObject(rightBase.getUdtModel());
            }
        } else if (leftModel instanceof DbOROperationLibrary) {
            DbORDatabase leftBase = ((DbOROperationLibrary) leftModel).getDeploymentDatabase();
            DbORDatabase rightBase = ((DbOROperationLibrary) rightModel).getDeploymentDatabase();
            if (leftBase != null && rightBase != null && leftBase != rightBase
                    && leftBase.getDefaultDomainModel() != null
                    && rightBase.getDefaultDomainModel() != null) {
                leftDomainModel = (DbORDomainModel) leftBase.getDefaultDomainModel();
                rightDomainModel = (DbORDomainModel) rightBase.getDefaultDomainModel();
                leftDomainModel.setMatchingObject(rightDomainModel);
            }
        } else if (leftModel instanceof DbORDomainModel) {
            leftDomainModel = (DbORDomainModel) leftModel;
            rightDomainModel = (DbORDomainModel) rightModel;
        }

        if (syncUser && leftModel.getProject() != rightModel.getProject())
            prematchUsers();

        populate();
    }

    // Prematch because matchingObject facility matches by name with case
    // significance.
    private void prematchUsers() throws DbException {
        DbORUserNode leftNode = ((DbSMSProject) leftModel.getProject()).getUserNode();
        DbORUserNode rightNode = ((DbSMSProject) rightModel.getProject()).getUserNode();
        DbEnumeration dbEnum = rightNode.getComponents().elements(DbORUser.metaClass);
        while (dbEnum.hasMoreElements())
            dbEnum.nextElement().setMatchingObject(null);
        dbEnum.close();

        dbEnum = leftNode.getComponents().elements(DbORUser.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORUser leftUser = (DbORUser) dbEnum.nextElement();
            String name = getName(leftUser);
            DbORUser rightUser = null;
            DbEnumeration dbEnum2 = rightNode.getComponents().elements(DbORUser.metaClass);
            while (dbEnum2.hasMoreElements()) {
                DbORUser user = (DbORUser) dbEnum2.nextElement();
                if (user.getMatchingObject() != null)
                    continue;
                if (equalsName(name, getName(user))) {
                    rightUser = user;
                    break;
                }
            }
            dbEnum2.close();
            leftUser.setMatchingObject(rightUser);
        }
        dbEnum.close();
    }

    /*
     * Two children of matched parents are matchable if they belong to the same class (considering
     * the last subclass independent of the target system); in addition, for associations, their
     * linked tables must be matched; for foreign keys, their associations must be matched (their
     * primary tables in synchro because there is no associations in synchro).
     */
    protected final boolean isMatchable(DbObject dbo1, DbObject dbo2) throws DbException {
        boolean matchable;
        int i = AnyORObject.getMetaClassIndex(dbo1.getMetaClass());
        if (i != -1)
            matchable = (i == AnyORObject.getMetaClassIndex(dbo2.getMetaClass()));
        else
            matchable = (dbo1.getMetaClass() == dbo2.getMetaClass());

        if (matchable) {
            if (dbo1 instanceof DbORAssociation) {
                DbObject frontTable1 = ((DbORAssociation) dbo1).getFrontEnd().getClassifier()
                        .getMatchingObject();
                DbObject backTable1 = ((DbORAssociation) dbo1).getBackEnd().getClassifier()
                        .getMatchingObject();
                DbObject frontTable2 = ((DbORAssociation) dbo2).getFrontEnd().getClassifier();
                DbObject backTable2 = ((DbORAssociation) dbo2).getBackEnd().getClassifier();
                matchable = ((frontTable1 == frontTable2 && backTable1 == backTable2) || (frontTable1 == backTable2 && backTable1 == frontTable2));
            } else if (dbo1 instanceof DbORForeign) {
                if (isSynchro) {
                    DbORAssociationEnd end1 = ((DbORForeign) dbo1).getAssociationEnd();
                    DbORAssociationEnd end2 = ((DbORForeign) dbo2).getAssociationEnd();
                    DbObject table1 = end1.getOppositeEnd().getClassifier().getMatchingObject();
                    DbObject table2 = end2.getOppositeEnd().getClassifier();
                    matchable = (table1 == table2 && end1.getMatchingObject() == null && end2
                            .getMatchingObject() == null);
                } else {
                    DbObject assoc1 = ((DbORForeign) dbo1).getAssociationEnd().getComposite()
                            .getMatchingObject();
                    DbObject assoc2 = ((DbORForeign) dbo2).getAssociationEnd().getComposite();
                    matchable = (assoc1 == assoc2);
                }
            }
        }
        return matchable;
    }

    /*
     * Return true if pre-match is complete, i.e. loadChildren does not attempt additionnal
     * matching.
     * 
     * If associations, pre-match the association ends. If tables, pre-match the foreign keys (for
     * integration only, not for synchro). If integration between a super-model and a sub-model and
     * initial loading, pre-match all the objects according to the <superCopy> relation.
     */
    protected final boolean preMatch(IntegrateNode parentNode, boolean loading) throws DbException {
        if (parentNode.getLeftDbo() instanceof DbORAssociation) {
            DbORAssociation assoc1 = (DbORAssociation) parentNode.getLeftDbo();
            DbORAssociation assoc2 = (DbORAssociation) parentNode.getRightDbo();
            DbORAssociationEnd frontEnd1 = assoc1.getFrontEnd();
            DbORAssociationEnd backEnd1 = assoc1.getBackEnd();
            DbORAssociationEnd frontEnd2 = assoc2.getFrontEnd();
            DbORAssociationEnd backEnd2 = assoc2.getBackEnd();
            if (frontEnd1.getClassifier().getMatchingObject() == frontEnd2.getClassifier()) {
                frontEnd1.setMatchingObject(frontEnd2);
                backEnd1.setMatchingObject(backEnd2);
            } else {
                frontEnd1.setMatchingObject(backEnd2);
                backEnd1.setMatchingObject(frontEnd2);
            }
            return true;
        }

        if (!isSynchro && parentNode.getLeftDbo() instanceof DbORAbsTable) {
            DbEnumeration dbEnum = parentNode.getLeftDbo().getComponents().elements(
                    DbORForeign.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORForeign leftFKey = (DbORForeign) dbEnum.nextElement();
                DbORAssociationEnd leftEnd = leftFKey.getAssociationEnd();
                DbORAssociation leftAssoc = (DbORAssociation) leftEnd.getComposite();
                DbORAssociation rightAssoc = (DbORAssociation) leftAssoc.getMatchingObject();
                if (rightAssoc == null)
                    continue;
                DbORAssociationEnd rightEnd = (leftEnd.isFrontEnd() ? rightAssoc.getFrontEnd()
                        : rightAssoc.getBackEnd());
                if (rightEnd.getClassifier() != parentNode.getRightDbo())
                    rightEnd = rightEnd.getOppositeEnd();
                DbORForeign rightFKey = rightEnd.getMember();
                if (rightFKey != null)
                    leftFKey.setMatchingObject(rightFKey);
            }
            dbEnum.close();
        }

        if (!loading || matchType == NO_SUPER)
            return false;
        DbObject subParent = (matchType == RIGHT_SUPER ? parentNode.getLeftDbo() : parentNode
                .getRightDbo());
        DbEnumeration dbEnum = subParent.getComponents().elements(DbSMSSemanticalObject.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSSemanticalObject subDbo = (DbSMSSemanticalObject) dbEnum.nextElement();
            DbSMSSemanticalObject superDbo = subDbo.getSuperCopy();
            if (superDbo != null)
                subDbo.setMatchingObject(superDbo);
        }
        dbEnum.close();
        return true;
    }

    protected final void postMatch(IntegrateNode node) throws DbException {
        // In synchro, associations are not shown; so they must be manually
        // matched when their foreign keys are matched.
        if (isSynchro && node.getLeftDbo() instanceof DbORForeign) {
            DbORAssociationEnd leftEnd = ((DbORForeign) node.getLeftDbo()).getAssociationEnd();
            DbORAssociationEnd rightEnd = ((DbORForeign) node.getRightDbo()).getAssociationEnd();
            leftEnd.setMatchingObject(rightEnd);
            leftEnd.getOppositeEnd().setMatchingObject(rightEnd.getOppositeEnd());
            leftEnd.getComposite().setMatchingObject(rightEnd.getComposite());
        }
        if (matchType == NO_SUPER)
            return;
        // We can get here only in an Associate operation, where a write
        // transaction is started
        if (AnySemObject.supportsSuper(node.getLeftDbo().getMetaClass())) {
            DbSMSSemanticalObject leftDbo = (DbSMSSemanticalObject) node.getLeftDbo();
            DbSMSSemanticalObject rightDbo = (DbSMSSemanticalObject) node.getRightDbo();
            if (matchType == RIGHT_SUPER)
                leftDbo.setSuperCopy(rightDbo);
            else
                rightDbo.setSuperCopy(leftDbo);
        }
    }

    // If matching associations, match also the related foreign keys.
    protected final void match(IntegrateNode leftNode, IntegrateNode rightNode) throws DbException {
        if (leftNode.getLeftDbo() instanceof DbORAssociation) {
            DbORAssociation leftAssoc = (DbORAssociation) leftNode.getLeftDbo();
            DbORAssociation rightAssoc = (DbORAssociation) rightNode.getRightDbo();
            matchFKey(leftAssoc.getFrontEnd(), rightAssoc.getFrontEnd());
            matchFKey(leftAssoc.getBackEnd(), rightAssoc.getBackEnd());
        }
        matchAux(leftNode, rightNode);
    }

    private void matchFKey(DbORAssociationEnd leftEnd, DbORAssociationEnd rightEnd)
            throws DbException {
        if (rightEnd.getClassifier() != leftEnd.getClassifier().getMatchingObject())
            rightEnd = rightEnd.getOppositeEnd();
        DbORForeign leftFKey = leftEnd.getMember();
        DbORForeign rightFKey = rightEnd.getMember();
        if (leftFKey != null && rightFKey != null) {
            IntegrateNode tableNode = findNode((IntegrateNode) getRoot(), leftFKey.getComposite(),
                    false);
            IntegrateNode leftNode = findNode(tableNode, leftFKey, false);
            IntegrateNode rightNode = findNode(tableNode, rightFKey, true);
            matchAux(leftNode, rightNode);
        }
    }

    // Cannot unmatch a database; cannot unmatch an association end (must
    // unmatch instead the association).
    // If integration (not synchro), cannot unmatch a foreign key (must unmatch
    // instead the association)
    protected final boolean isUnmatchable(IntegrateNode node) {
        DbObject child = node.getLeftDbo();
        if (child instanceof DbORAssociationEnd || child instanceof DbORModel)
            return false;
        if (!isSynchro && child instanceof DbORForeign)
            return false;
        return true;
    }

    // No transaction started
    protected final void preUnmatch(IntegrateNode node) throws DbException {
        if (matchType == NO_SUPER)
            return;
        if (AnySemObject.supportsSuper(node.getLeftDbo().getMetaClass())) {
            DbSMSSemanticalObject leftDbo = (DbSMSSemanticalObject) node.getLeftDbo();
            DbSMSSemanticalObject rightDbo = (DbSMSSemanticalObject) node.getRightDbo();
            leftDbo.getDb()
                    .beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("breakSuperLink"));
            if (matchType == RIGHT_SUPER)
                leftDbo.setSuperCopy(null);
            else
                rightDbo.setSuperCopy(null);
            leftDbo.getDb().commitTrans();
        }
    }

    // If unmatching a table, unmatch also all its associations and its foreign
    // keys.
    // if unmatching an association, unmatch also its foreign keys.
    protected final void unmatch(IntegrateNode node) throws DbException {
        if (node.getLeftDbo() instanceof DbORAbsTable) {
            DbEnumeration dbEnum = ((DbORAbsTable) node.getLeftDbo()).getAssociationEnds()
                    .elements();
            while (dbEnum.hasMoreElements()) {
                DbORAssociationEnd end = (DbORAssociationEnd) dbEnum.nextElement();
                DbORAssociation assoc = (DbORAssociation) end.getComposite();
                if (assoc.getMatchingObject() != null) {
                    if (isSynchro) { // associations are not shown in synchro
                        assoc.setMatchingObject(null);
                        end.setMatchingObject(null);
                        end.getOppositeEnd().setMatchingObject(null);
                    } else {
                        IntegrateNode node2 = findNode((IntegrateNode) getRoot(), assoc, false);
                        unmatchAux(node2);
                    }
                }
                unmatchFKey(end.getOppositeEnd().getMember());
            }
            dbEnum.close();
        } else if (node.getLeftDbo() instanceof DbORAssociation) {
            DbORAssociation assoc = (DbORAssociation) node.getLeftDbo();
            unmatchFKey(assoc.getFrontEnd().getMember());
            unmatchFKey(assoc.getBackEnd().getMember());
        } else if (isSynchro && node.getLeftDbo() instanceof DbORForeign) { // associations
            // are
            // not
            // shown
            // in
            // synchro
            DbORAssociationEnd end = ((DbORForeign) node.getLeftDbo()).getAssociationEnd();
            end.setMatchingObject(null);
            end.getOppositeEnd().setMatchingObject(null);
            end.getComposite().setMatchingObject(null);
        }

        unmatchAux(node);
    }

    private void unmatchFKey(DbORForeign leftFKey) throws DbException {
        if (leftFKey != null && leftFKey.getMatchingObject() != null) {
            if (isSynchro) {
                // In synchro, the root node represents the database, not the
                // data model
                IntegrateNode datamodelNode = findNode((IntegrateNode) getRoot(), leftFKey
                        .getComposite().getComposite(), false);
                IntegrateNode node = findNode(datamodelNode, leftFKey.getComposite(), false);
                node = findNode(node, leftFKey, false);
                unmatchAux(node);
            } else {
                IntegrateNode node = findNode((IntegrateNode) getRoot(), leftFKey.getComposite(),
                        false);
                node = findNode(node, leftFKey, false);
                unmatchAux(node);
            }
        }
    }

    protected final boolean allowsChildren(DbObject child) throws DbException {
        if (child instanceof DbSMSPackage)
            return true;

        if (leftModel instanceof DbORModel) {
            if (child instanceof DbORAbsTable || child instanceof DbORAssociation
                    || child instanceof DbORDomain || child instanceof DbORProcedure
                    || child instanceof DbORPackage)
                return true;
            if (rootID == TargetSystem.SGBD_ORACLE_ROOT)
                return (child instanceof DbORAIndex || child instanceof DbORAAbsPartition
                        || child instanceof DbORATablespace || child instanceof DbORARedoLogGroup);
        } else if (leftModel instanceof DbOOAbsPackage) {
            return (child instanceof DbOOClass || child instanceof DbOOAbstractMethod || child instanceof DbJVCompilationUnit);
        } else if (leftModel instanceof DbBEModel) {
            return (child instanceof DbBEUseCase || child instanceof DbBEActor
                    || child instanceof DbBEStore || child instanceof DbBEResource || child instanceof DbBEQualifier);
        } else if (leftModel instanceof DbORCommonItemModel) {
            return false;
        }
        return false;
    }

    // For dataModels, the list is built in 2 passes: in first pass, we add to
    // the list
    // the tables, in second pass, the associations; this is because the
    // associations
    // can be matched only if their tables are already matched.
    protected final ArrayList getChildren(DbObject parent) throws DbException {
        ArrayList children = new ArrayList();
        DbEnumeration dbEnum = parent.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject child = dbEnum.nextElement();
            if (filterChild(parent, child))
                children.add(child);
        }
        dbEnum.close();

        if (parent instanceof DbORDataModel) {
            if (!isSynchro) {
                dbEnum = parent.getComponents().elements(DbORAssociation.metaClass);
                while (dbEnum.hasMoreElements())
                    children.add(dbEnum.nextElement());
                dbEnum.close();
            }
            if (!isSynchro && loadDatabase)
                children.add(((DbORDataModel) parent).getDeploymentDatabase());
            if (!isSynchro && loadDomainModel)
                children.add(((DbORDataModel) parent).getDeploymentDatabase().getUdtModel());
            if (!isSynchro && loadOperationLibrary)
                children
                        .add(((DbORDataModel) parent).getDeploymentDatabase().getOperationLibrary());
        }

        if (parent instanceof DbORDatabase && isSynchro) {
            if (loadDataModel)
                children.add(((DbORDatabase) parent).getSchema());
            if (loadDomainModel)
                children.add(((DbORDatabase) parent).getDefaultDomainModel());
            if (loadUdtModel)
                children.add(((DbORDatabase) parent).getUdtModel());
            if (loadOperationLibrary)
                children.add(((DbORDatabase) parent).getOperationLibrary());
        }
        return children;
    }

    private boolean filterChild(DbObject parent, DbObject child) throws DbException {
        if (leftModel instanceof DbORModel) {
            if (parent instanceof DbORDataModel) {
                if (child instanceof DbORAbsTable) // associations picked up in
                    // a second pass
                    return true;
            } else if (parent instanceof DbORAbsTable) {
                if (child instanceof DbORColumn || child instanceof DbORConstraint
                        || child instanceof DbORIndex || child instanceof DbORTrigger)
                    return true;
            } else if (parent instanceof DbORAssociation) {
                return (child instanceof DbORAssociationEnd);
            } else if (parent instanceof DbOROperationLibrary)
                return (child instanceof DbORProcedure || child instanceof DbORPackage);
            else if (parent instanceof DbORProcedure)
                return (child instanceof DbORParameter);
            else if (parent instanceof DbORPackage)
                return (child instanceof DbORProcedure);
            else if (parent instanceof DbORDomainModel) {
                return (child instanceof DbORDomain // && /* TODO CHECK IF NEXT
                // CONDITION IS VALID */
                /*
                 * ! (isSynchro && ((DbORDomain)child).getCategory().getValue() ==
                 * ORDomainCategory.DOMAIN)
                 */);
            } else if (parent instanceof DbORDomain)
                return (child instanceof DbORField);

            if (rootID == TargetSystem.SGBD_ORACLE_ROOT) {
                if (parent instanceof DbORADataModel)
                    return (child instanceof DbORASequence);
                else if (parent instanceof DbORATable)
                    return (child instanceof DbORANestedTableStorage
                            || child instanceof DbORALobStorage || child instanceof DbORAPartition);
                else if (parent instanceof DbORAIndex)
                    return (child instanceof DbORAPartition);
                else if (parent instanceof DbORADatabase)
                    return (child instanceof DbORATablespace
                            || child instanceof DbORARollbackSegment || child instanceof DbORARedoLogGroup);
                else if (parent instanceof DbORAPartition)
                    return (child instanceof DbORASubPartition || child instanceof DbORALobStorage);
                else if (parent instanceof DbORASubPartition)
                    return (child instanceof DbORALobStorage);
                else if (parent instanceof DbORATablespace)
                    return (child instanceof DbORADataFile);
                else if (parent instanceof DbORARedoLogGroup)
                    return (child instanceof DbORARedoLogFile);
            } else if (rootID == TargetSystem.SGBD_INFORMIX_ROOT) {
                if (parent instanceof DbINFDatabase)
                    return (child instanceof DbINFDbspace || child instanceof DbINFSbspace);
            }
        }

        else if (leftModel instanceof DbOOAbsPackage) {
            if (parent instanceof DbOOAbsPackage)
                return (child instanceof DbOOClass || child instanceof DbJVCompilationUnit);
            else if (parent instanceof DbOOClass)
                return (child instanceof DbOODataMember || child instanceof DbOOOperation || child instanceof DbOOClass);
            else if (parent instanceof DbOOAbstractMethod)
                return (child instanceof DbOOParameter);
            else if (parent instanceof DbJVCompilationUnit)
                return (child instanceof DbJVImport);
        }

        else if (leftModel instanceof DbBEModel) {
            if (parent instanceof DbBEModel)
                return (child instanceof DbBEUseCase || child instanceof DbBEActor
                        || child instanceof DbBEStore || child instanceof DbBEResource || child instanceof DbBEQualifier);
            else if (parent instanceof DbBEUseCase)
                return (child instanceof DbBEUseCase || child instanceof DbBEFlow);
        }

        else if (leftModel instanceof DbORCommonItemModel) {
            if (parent instanceof DbORCommonItemModel)
                return (child instanceof DbORCommonItem);
        }

        else if (leftModel instanceof DbORDatabase) {
            if (rootID == TargetSystem.SGBD_ORACLE_ROOT) {
                if (parent instanceof DbORADatabase)
                    return (child instanceof DbORATablespace
                            || child instanceof DbORARollbackSegment || child instanceof DbORARedoLogGroup);
                else if (parent instanceof DbORATablespace)
                    return (child instanceof DbORADataFile);
                else if (parent instanceof DbORARedoLogGroup)
                    return (child instanceof DbORARedoLogFile);
            } else if (rootID == TargetSystem.SGBD_INFORMIX_ROOT) {
                if (parent instanceof DbINFDatabase)
                    return (child instanceof DbINFDbspace || child instanceof DbINFSbspace);
            }
        }

        return false;
    }

    protected String getDisplayName(DbObject child) throws DbException {
        if (child != null && (child == leftModel || child == rightModel)) {
            String name = getName(child) + (child instanceof DbORModel ? getTSSuffix(child) : "");
            for (DbObject parent = child.getComposite(); parent != null
                    && !(parent instanceof DbProject); parent = parent.getComposite()) {
                String parentName = getName(parent);
                if (parentName != null && (name.length() + parentName.length()) < 100)
                    name = (parentName == null ? "" : parentName) + "." + name;
                else {
                    name = "..." + name;
                    break;
                }
            }
            return name;
        }
        if (child instanceof DbORModel) {
            return child.getName() + getTSSuffix(child);
        }
        if (child instanceof DbOOAbstractMethod) {
            return ((DbOOAbstractMethod) child).buildSignature(DbObject.SHORT_FORM);
        }
        if (child instanceof DbJVImport) {
            String name = ((DbJVImport) child).getImportedObject().getSemanticalName(
                    DbObject.LONG_FORM);
            if (((DbJVImport) child).isAll())
                name = name + ".*";
            return name;
        }
        if (child instanceof DbBEUseCase) {
            DbBEUseCase process = (DbBEUseCase) child;
            String name = process.getAlphanumericHierID() + " " + process.getName();
            return name;
        }
        if (child instanceof DbBEFlow) {
            DbBEFlow flow = (DbBEFlow) child;
            String name = flow.getId() + " " + super.getName(flow);
            return name;
        }

        if (isSynchro && syncUser && child != null && AnyORObject.getUserField(child) != null
                && !(child instanceof DbORANestedTableStorage)) {
            DbORUser user = (DbORUser) child.get(AnyORObject.getUserField(child));
            String username = getName(user);
            if (username == null || username.length() == 0)
                username = kUnspecifiedUser;
            return username + "." + getName(child);
        }
        return getName(child);
    }

    protected final String getNameForMatchByName(DbObject dbo) throws DbException {
        if (dbo instanceof DbJVClass)
            return ((DbSemanticalObject) dbo).getSemanticalName(DbObject.LONG_FORM);
        else
            return super.getNameForMatchByName(dbo);
    }

    protected final GroupParams getGroupParams(DbObject parent, DbObject child) throws DbException {
        // OO Package
        if (parent instanceof DbOOAbsPackage) {
            if (child instanceof DbOOClass)
                return SMSExplorer.classGroup;
            if (child instanceof DbJVCompilationUnit)
                return SMSExplorer.compilGroup;
        }
        // OR Operation Library
        else if (parent instanceof DbOROperationLibrary) {
            if (child instanceof DbORProcedure)
                return SMSExplorer.procedureGroup;
            if (child instanceof DbORAPackage)
                return SMSExplorer.oraPackageGroup;
        }
        // BE Model
        else if (parent instanceof DbBEModel) {
            TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
            Terminology terminologyChild = terminologyUtil.findModelTerminology(child);
            Terminology terminologyParent = terminologyUtil.findModelTerminology(parent);

            if (child instanceof DbBEActor) {
                Icon actorIcon = terminologyParent.getIcon(DbBEActor.metaClass);
                String term = terminologyParent.getTerm(DbBEActor.metaClass, true);
                return new GroupParams(4, term, actorIcon, true);
            }
            if (child instanceof DbBEStore) {
                Icon storeIcon = terminologyParent.getIcon(DbBEStoreGo.metaClass);
                String term = terminologyParent.getTerm(DbBEStore.metaClass, true);
                return new GroupParams(5, term, storeIcon, true);
            }
            if (child instanceof DbBEQualifier)
                return SMSExplorer.qualifierGroup;
            if (child instanceof DbBEResource)
                return SMSExplorer.resourceGroup;
        } else if (parent instanceof DbBEUseCase) {
            if (child instanceof DbBEFlow) {
                Terminology terminologyParent = terminologyUtil.findModelTerminology(parent);
                String term = terminologyParent.getTerm(DbBEFlow.metaClass, true);
                Icon flowsIcon = terminologyParent.getIcon(DbBEFlow.metaClass);
                return new GroupParams(9, term, flowsIcon);
            }
        }
        // OR Data Model
        else if (parent instanceof DbORDataModel) {
            if (child instanceof DbORAbsTable) {
                if (child instanceof DbORTable) {
                    DbORTable table = (DbORTable) child;
                    if (table.isIsAssociation() == false)
                        return SMSExplorer.tableGroup;
                    else
                        return SMSExplorer.relationshipGroup;
                } else if (child instanceof DbORView)
                    return SMSExplorer.viewGroup;
                else
                    return SMSExplorer.tableGroup;

            }
            if (child instanceof DbORAssociation)
                return SMSExplorer.assocGroup;
            if (child instanceof DbORASequence)
                return SMSExplorer.sequenceGroup;
        }
        // OR Database
        else if (parent instanceof DbORDatabase) {
            // Oracle
            if (parent instanceof DbORADatabase) {
                if (child instanceof DbORARedoLogGroup)
                    return SMSExplorer.redoLogFilGroup;
                if (child instanceof DbORARollbackSegment)
                    return SMSExplorer.rollbackGroup;
                if (child instanceof DbORATablespace)
                    return SMSExplorer.tablespaceGroup;
            }
            // Informix
            else if (parent instanceof DbINFDatabase) {
                if (child instanceof DbINFDbspace)
                    return SMSExplorer.dbspaceGroup;
                if (child instanceof DbINFSbspace)
                    return SMSExplorer.sbspaceGroup;
            }
        }

        else if (parent instanceof DbOOClass) {
            if (child instanceof DbOODataMember)
                return SMSExplorer.fieldGroup;
            if (child instanceof DbOOOperation)
                return SMSExplorer.operationGroup;
            if (child instanceof DbOOClass)
                return SMSExplorer.innerClassGroup;
        } else if (parent instanceof DbORAbsTable) {
            if (child instanceof DbORColumn)
                return SMSExplorer.columnGroup;
            if (child instanceof DbORPrimaryUnique) // do not separate primaries
                // and uniques like the
                // explorer
                return SMSExplorer.uniqueGroup;
            if (child instanceof DbORForeign)
                return SMSExplorer.foreignGroup;
            if (child instanceof DbORIndex)
                return SMSExplorer.indexGroup;
            if (child instanceof DbORCheck)
                return SMSExplorer.checkGroup;
            if (child instanceof DbORTrigger)
                return SMSExplorer.triggerGroup;
            // Oracle
            if (parent instanceof DbORAAbsTable) {
                if (child instanceof DbORALobStorage)
                    return SMSExplorer.lobStorageGroup;
                if (child instanceof DbORAPartition)
                    return SMSExplorer.partitionGroup;
                if (child instanceof DbORANestedTableStorage)
                    return SMSExplorer.storageTabGroup;
            }
        }
        // Oracle
        else if (parent instanceof DbORAPartition) {
            if (child instanceof DbORASubPartition)
                return SMSExplorer.subPartGroup;
            if (child instanceof DbORALobStorage)
                return SMSExplorer.lobStorageGroup;
        }

        // Oracle
        else if (parent instanceof DbORASubPartition) {
            if (child instanceof DbORALobStorage)
                return SMSExplorer.lobStorageGroup;
        }

        return GroupParams.defaultGroupParams;
    }

    protected final void initCopyCustom() throws DbException {
        leftCopyCustom = new SMSIntegDeepCopyCustomizer(this, rightModel.getProject(), leftModel,
                rightDomainModel);
        rightCopyCustom = (noRightUpdate ? null : new SMSIntegDeepCopyCustomizer(this, leftModel
                .getProject(), rightModel, leftDomainModel));
    }

    private static class SMSIntegDeepCopyCustomizer extends SMSDeepCopyCustomizer {

        private SMSIntegrateModel integModel;
        private DbSMSProject destProject;
        private DbORDomainModel sourceDomainModel; // may be null

        SMSIntegDeepCopyCustomizer(SMSIntegrateModel integModel, DbProject srcProject,
                DbObject destComposite, DbORDomainModel sourceDomainModel) throws DbException {
            super(srcProject, destComposite);
            this.integModel = integModel;
            destProject = (DbSMSProject) destComposite.getProject();
            this.sourceDomainModel = sourceDomainModel;
        }

        public final DbObject resolveLink(DbObject srcObj, MetaRelationship metaRel,
                DbObject srcNeighbor) throws DbException {
            if (!isValidRel(metaRel))
                return null;
            if (srcNeighbor instanceof DbORTypeClassifier) {
                if (srcNeighbor.getComposite() == sourceDomainModel)
                    return srcNeighbor.findMatchingObject();
                return translateType((DbORTypeClassifier) srcNeighbor);
            }
            if (srcNeighbor instanceof DbORUser) {
                if (!integModel.syncUser)
                    return null;
                return getTargetUser((DbORUser) srcNeighbor, destProject);
            }
            return srcNeighbor.findMatchingObject();
        }
    } // End of class SMSIntegDeepCopyCustomizer

    /*
     * Return true to allow properties to be compare for the root DbObject
     */
    protected final boolean allowRootPropertiesCompare(DbObject rootDbObject) {
        return rootDbObject instanceof DbORDatabase;
    }

    protected final boolean equalProperty(MetaRelationship metaRel, DbObject leftVal,
            DbObject rightVal) throws DbException {
        if (differentTS && leftVal instanceof DbORTypeClassifier
                && rightVal instanceof DbORTypeClassifier) {
            if (leftVal.getMetaClass() != rightVal.getMetaClass())
                return false;
            if (leftVal instanceof DbORBuiltInType
                    && leftTSInfo.getRootID() != rightTSInfo.getRootID()) {
                String leftName = leftTSInfo.typeToLogical(getName(leftVal));
                String rightName = rightTSInfo.typeToLogical(getName(rightVal));
                return (leftName != null && leftName.equals(rightName));
            } else
                return DbObject.valuesAreEqual(getName(leftVal), getName(rightVal));
        } else if (isSynchro && (leftVal instanceof DbORDomain || rightVal instanceof DbORDomain)){
            while (leftVal instanceof DbORDomain
                    && ((DbORDomain) leftVal).getCategory().getValue() == ORDomainCategory.DOMAIN)
                leftVal = ((DbORDomain) leftVal).getSourceType();
            while (rightVal instanceof DbORDomain
                    && ((DbORDomain) rightVal).getCategory().getValue() == ORDomainCategory.DOMAIN)
                rightVal = ((DbORDomain) rightVal).getSourceType();
            if (leftVal instanceof DbORBuiltInType && rightVal instanceof DbORBuiltInType)
                return leftTSInfo.isAlias(getName(leftVal), getName(rightVal));
            else
                return super.equalProperty(metaRel, leftVal, rightVal);
        } else if (isSynchro
                && (metaRel == DbORDatabase.fSchema || metaRel == DbORDatabase.fOperationLibrary || metaRel == DbORDatabase.fUdtModel)) {
            return true;
        } else if (!syncUser && metaRel instanceof MetaRelation1
                && metaRel.getOppositeRel(null).getMetaClass() == DbORUser.metaClass)
            return true;
        else
            return super.equalProperty(metaRel, leftVal, rightVal);
    }

    protected final boolean equalProperty(MetaRelationN metaRelN, DbObject[] leftVal,
            DbObject[] rightVal) throws DbException {
        if (metaRelN == DbOOClass.fSuperInheritances) {
            if (leftVal.length != rightVal.length)
                return false;
            for (int i = 0; i < leftVal.length; i++) {
                DbOOClass leftClass = ((DbOOInheritance) leftVal[i]).getSuperClass();
                DbOOClass rightClass = ((DbOOInheritance) rightVal[i]).getSuperClass();
                if (externalMatchByName
                        && ((metaRelN.getFlags() & MetaField.INTEGRABLE_BY_NAME) != 0)
                        && leftClass != null
                        && rightClass != null
                        && ((leftClass.getMatchingObject() == null && rightClass
                                .getMatchingObject() == null) || (leftClass == leftClass
                                .getMatchingObject()))) {
                    String leftName = getNameForMatchByName(leftClass);
                    String rightName = getNameForMatchByName(rightClass);
                    if (equalsName(leftName, rightName))
                        continue;
                }
                if (leftClass.findMatchingObject() != rightClass)
                    return false;
            }
            return true;
        }
        // Sequences of components (columns and parameters)
        if (metaRelN == DbObject.fComponents) {
            if (leftVal == null && rightVal == null)
                return true;
            if (leftVal == null)
                leftVal = new DbObject[0];
            if (rightVal == null)
                rightVal = new DbObject[0];
            MetaClass compositeMetaClass = null;
            if (leftVal.length > 0)
                compositeMetaClass = leftVal[0].getComposite().getMetaClass();
            else if (rightVal.length > 0)
                compositeMetaClass = rightVal[0].getComposite().getMetaClass();
            else
                return true;

            if (DbORAbsTable.metaClass.isAssignableFrom(compositeMetaClass)) {
                ArrayList leftColumns = new ArrayList();
                for (int i = 0; i < leftVal.length; i++) {
                    // Check sequence only for objects with a corresponding
                    // matching object
                    // (avoid returning not equals if column added or removed)
                    if (leftVal[i] instanceof DbORColumn && leftVal[i].getMatchingObject() != null)
                        leftColumns.add(leftVal[i]);
                }
                ArrayList rightColumns = new ArrayList();
                for (int i = 0; i < rightVal.length; i++) {
                    if (rightVal[i] instanceof DbORColumn
                            && rightVal[i].getMatchingObject() != null)
                        rightColumns.add(rightVal[i]);
                }
                // if (leftColumns.size() != rightColumns.size())
                // return true; // Component added or removed
                int size = leftColumns.size();
                for (int i = 0; i < size; i++) {
                    DbObject left = (DbObject) leftColumns.get(i);
                    DbObject right = (DbObject) rightColumns.get(i);
                    if (left.getMatchingObject() != null && left.getMatchingObject() != right)
                        return false;
                }
                return true;
            } else if (DbORProcedure.metaClass.isAssignableFrom(compositeMetaClass)) {
                ArrayList leftParams = new ArrayList();
                for (int i = 0; i < leftVal.length; i++) {
                    if (leftVal[i] instanceof DbORParameter
                            && leftVal[i].getMatchingObject() != null)
                        leftParams.add(leftVal[i]);
                }
                ArrayList rightParams = new ArrayList();
                for (int i = 0; i < rightVal.length; i++) {
                    if (rightVal[i] instanceof DbORParameter
                            && rightVal[i].getMatchingObject() != null)
                        rightParams.add(rightVal[i]);
                }
                // if (leftParams.size() != rightParams.size())
                // return true; // Component added or removed
                int size = leftParams.size();
                for (int i = 0; i < size; i++) {
                    DbObject left = (DbObject) leftParams.get(i);
                    DbObject right = (DbObject) rightParams.get(i);
                    if (left.getMatchingObject() != null && left.getMatchingObject() != right)
                        return false;
                }
                return true;
            }

        }
        return super.equalProperty(metaRelN, leftVal, rightVal);
    }

    protected final boolean equalProperty(MetaClass metaClass, DbObject[] leftVal,
            DbObject[] rightVal) throws DbException {
        if (leftVal.length != rightVal.length)
            return false;
        for (int i = 0; i < leftVal.length; i++) {
            if (metaClass == DbORAllowableValue.metaClass) {
                DbORAllowableValue leftValue = (DbORAllowableValue) leftVal[i];
                DbORAllowableValue rightValue = (DbORAllowableValue) rightVal[i];
                if (!DbObject.valuesAreEqual(leftValue.getValue(), rightValue.getValue()))
                    return false;
                if (!DbObject.valuesAreEqual(leftValue.getDefinition(), rightValue.getDefinition()))
                    return false;
            } else if (metaClass == DbORFKeyColumn.metaClass) {
                DbORColumn leftCol = ((DbORFKeyColumn) leftVal[i]).getColumn();
                DbORColumn rightCol = ((DbORFKeyColumn) rightVal[i]).getColumn();
                if (leftCol.findMatchingObject() != rightCol)
                    return false;
            } else if (metaClass == DbORIndexKey.metaClass) {
                DbORIndexKey leftKey = (DbORIndexKey) leftVal[i];
                DbORIndexKey rightKey = (DbORIndexKey) rightVal[i];
                DbORColumn leftCol = leftKey.getIndexedElement();
                DbORColumn rightCol = rightKey.getIndexedElement();
                if (leftCol != null) {
                    if (rightCol == null || leftCol.findMatchingObject() != rightCol)
                        return false;
                } else {
                    if (rightCol != null)
                        return false;
                }
                if (!DbObject.valuesAreEqual(leftKey.getExpression(), rightKey.getExpression()))
                    return false;
                if (!DbObject.valuesAreEqual(leftKey.getSortOption(), rightKey.getSortOption()))
                    return false;
            } else if (metaClass == DbINFFragment.metaClass) {
                DbINFFragment leftFrag = (DbINFFragment) leftVal[i];
                DbINFFragment rightFrag = (DbINFFragment) rightVal[i];
                DbINFDbspace dbspace = leftFrag.getDbspace();
                if (dbspace != null) {
                    dbspace = (DbINFDbspace) dbspace.findMatchingObject();
                    if (dbspace == null)
                        return false;
                }
                if (dbspace != rightFrag.getDbspace())
                    return false;
                if (!DbObject.valuesAreEqual(leftFrag.getExpression(), rightFrag.getExpression()))
                    return false;
                if (!DbObject.valuesAreEqual(leftFrag.getRemainder(), rightFrag.getRemainder()))
                    return false;
            } else
                return false; // missing case
        }
        return true;
    }

    protected final String getPropertyString(MetaRelationN metaRelN, DbObject[] value)
            throws DbException {
        if (metaRelN == DbOOClass.fSuperInheritances) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < value.length; i++) {
                if (i > 0)
                    buffer.append(", ");
                DbOOClass clas = ((DbOOInheritance) value[i]).getSuperClass();
                buffer.append(getName(clas));
            }
            return buffer.toString();
        }
        if (metaRelN == DbObject.fComponents) {
            if (value == null || value.length == 0)
                return "";
            if (DbORAbsTable.metaClass.isAssignableFrom(value[0].getComposite().getMetaClass())) {
                StringBuffer buffer = new StringBuffer();
                boolean first = true;
                for (int i = 0; i < value.length; i++) {
                    if (!(value[i] instanceof DbORColumn))
                        continue;
                    if (!first)
                        buffer.append(", ");
                    buffer.append(getName(value[i]));
                    first = false;
                }
                return buffer.toString();
            } else if (DbORProcedure.metaClass.isAssignableFrom(value[0].getComposite()
                    .getMetaClass())) {
                StringBuffer buffer = new StringBuffer();
                boolean first = true;
                for (int i = 0; i < value.length; i++) {
                    if (!(value[i] instanceof DbORParameter))
                        continue;
                    if (!first)
                        buffer.append(", ");
                    buffer.append(getName(value[i]));
                    first = false;
                }
                return buffer.toString();
            }
        }
        return super.getPropertyString(metaRelN, value);
    }

    protected final String getPropertyString(MetaClass metaClass, DbObject[] value)
            throws DbException {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < value.length; i++) {
            if (i > 0)
                buffer.append(", "); // NOT LOCALIZABLE
            if (metaClass == DbORAllowableValue.metaClass) {
                DbORAllowableValue domVal = (DbORAllowableValue) value[i];
                buffer.append(domVal.getValue());
                if (domVal.getDefinition() != null)
                    buffer.append(" <" + domVal.getDefinition() + ">"); // NOT
                // LOCALIZABLE
            } else if (metaClass == DbORFKeyColumn.metaClass) {
                DbORColumn column = ((DbORFKeyColumn) value[i]).getColumn();
                buffer.append(getName(column));
            } else if (metaClass == DbORIndexKey.metaClass) {
                DbORIndexKey key = (DbORIndexKey) value[i];
                DbORColumn column = key.getIndexedElement();
                buffer.append(column != null ? getName(column) : key.getExpression());
                if (key.getSortOption() != null)
                    buffer.append(" <" + key.getSortOption() + ">"); // NOT
                // LOCALIZABLE
            } else if (metaClass == DbINFFragment.metaClass) {
                DbINFFragment frag = (DbINFFragment) value[i];
                if (frag.getDbspace() != null)
                    buffer.append(getName(frag.getDbspace()));
                buffer.append(" <" + (frag.isRemainder() ? "REMAINDER" : frag.getExpression())
                        + ">"); // NOT LOCALIZABLE
            } else
                // missing case
                buffer.append(getName(value[i]));
        }
        return buffer.toString();
    }

    protected final void delete(DbObject dbo) throws DbException {
        if (isSynchro && dbo instanceof DbORForeign) { // in synchro,
            // associations are not
            // shown.
            DbORAssociationEnd end = ((DbORForeign) dbo).getAssociationEnd();
            if (end != null)
                end.setNavigable(Boolean.FALSE); // should we delete the
            // association if both ends
            // not navigable ?
        }
        dbo.remove();
    }

    protected final void add(DeepCopy deepCopy, DbObject srcObj) throws DbException {
        if (isSynchro) { // in synchro, associations are not shown; so we must
            // add them explicitly.
            if (srcObj instanceof DbORForeign) {
                DbORAssociation assoc = (DbORAssociation) ((DbORForeign) srcObj)
                        .getAssociationEnd().getComposite();
                if (assoc.getMatchingObject() == null) // if not already added
                    deepCopy.create(assoc, assoc.getComposite().getMatchingObject(), false);
            } else if (srcObj instanceof DbORAbsTable) {
                DbEnumeration dbEnum = srcObj.getComponents().elements(DbORForeign.metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbORAssociation assoc = (DbORAssociation) ((DbORForeign) dbEnum.nextElement())
                            .getAssociationEnd().getComposite();
                    if (assoc.getMatchingObject() == null) // if not already
                        // added
                        deepCopy.create(assoc, assoc.getComposite().getMatchingObject(), false);
                }
                dbEnum.close();
            }
        }
        deepCopy.create(srcObj, srcObj.getComposite().getMatchingObject(), false);
    }

    protected final void setRelation(DbObject sourceDbo, DbObject targetDbo,
            MetaRelationship metaRel, int action) throws DbException {
        DbObject neighbor = (DbObject) sourceDbo.get(metaRel);
        if (differentTS && neighbor instanceof DbORTypeClassifier) {
            SMSIntegDeepCopyCustomizer copyCustom = (action == ACT_MODIFY_RIGHT ? (SMSIntegDeepCopyCustomizer) rightCopyCustom
                    : (SMSIntegDeepCopyCustomizer) leftCopyCustom);
            String name = neighbor.getName();
            if (neighbor instanceof DbORBuiltInType) {
                if (leftTSInfo.getRootID() != rightTSInfo.getRootID()) {
                    if (action == ACT_MODIFY_RIGHT)
                        name = leftTSInfo.mapType(name, rightTSInfo);
                    else
                        name = rightTSInfo.mapType(name, leftTSInfo);
                }
                neighbor = copyCustom.getTypeFromName(name);
            } else {
                if (neighbor.getComposite() == (action == ACT_MODIFY_RIGHT ? leftDomainModel
                        : rightDomainModel))
                    neighbor = neighbor.findMatchingObject();
                else
                    neighbor = copyCustom.getDomainFromName(name);
            }

            if (neighbor != null)
                targetDbo.set(metaRel, neighbor);
        } else if (neighbor instanceof DbORUser) {
            DbORUser targetUser = getTargetUser((DbORUser) neighbor, (DbSMSProject) targetDbo
                    .getProject());
            targetDbo.set(metaRel, targetUser);
        } else
            super.setRelation(sourceDbo, targetDbo, metaRel, action);
    }

    private static DbORUser getTargetUser(DbORUser sourceUser, DbSMSProject targetProject)
            throws DbException {
        DbORUser targetUser = (DbORUser) sourceUser.findMatchingObject();
        if (targetUser == null) {
            targetUser = new DbORUser(targetProject.getUserNode());
            targetUser.setName(sourceUser.getName());
            targetUser.setPhysicalName(sourceUser.getPhysicalName());
            sourceUser.setMatchingObject(targetUser);
        }
        return targetUser;
    }

    protected final void setRelationN(DbObject sourceDbo, DbObject targetDbo,
            MetaRelationN metaRelN, int action) throws DbException {
        if (metaRelN == DbOOClass.fSuperInheritances) {
            DbRelationN sourceRelN = (DbRelationN) sourceDbo.get(metaRelN);
            DbRelationN targetRelN;
            int nb = 0;
            for (int i = 0; i < sourceRelN.size(); i++) {
                DbOOClass clas = ((DbOOInheritance) sourceRelN.elementAt(i)).getSuperClass();
                clas = (DbOOClass) clas.findMatchingObject();
                if (clas == null)
                    continue;
                targetRelN = (DbRelationN) targetDbo.get(metaRelN);
                int j = nb;
                for (; j < targetRelN.size(); j++) {
                    DbOOClass clas2 = ((DbOOInheritance) targetRelN.elementAt(j)).getSuperClass();
                    if (clas == clas2)
                        break;
                }
                if (j == targetRelN.size())
                    new DbJVInheritance((DbJVClass) targetDbo, (DbJVClass) clas);
                targetDbo.reinsert(metaRelN, j, nb);
                nb++;
            }
            targetRelN = (DbRelationN) targetDbo.get(metaRelN);
            while (nb < targetRelN.size())
                targetRelN.elementAt(nb).remove();
        } else
            super.setRelationN(sourceDbo, targetDbo, metaRelN, action);
    }

    protected final void setRelationN(DbObject sourceDbo, DbObject targetDbo, MetaClass metaClass,
            int action) throws DbException {
        DbEnumeration dbEnum = targetDbo.getComponents().elements(metaClass);
        while (dbEnum.hasMoreElements())
            dbEnum.nextElement().remove();
        dbEnum.close();

        dbEnum = sourceDbo.getComponents().elements(metaClass);
        while (dbEnum.hasMoreElements()) {
            if (metaClass == DbORAllowableValue.metaClass) {
                DbORAllowableValue sourceVal = (DbORAllowableValue) dbEnum.nextElement();
                DbORAllowableValue targetVal = new DbORAllowableValue(targetDbo);
                targetVal.setValue(sourceVal.getValue());
                targetVal.setDefinition(sourceVal.getDefinition());
            } else if (metaClass == DbORFKeyColumn.metaClass) {
                DbORFKeyColumn fKeyCol = (DbORFKeyColumn) dbEnum.nextElement();
                DbORColumn foreignCol = (DbORColumn) fKeyCol.getColumn().findMatchingObject();
                DbORColumn sourceCol = (DbORColumn) fKeyCol.getSourceColumn().findMatchingObject();
                if (foreignCol != null && sourceCol != null)
                    new DbORFKeyColumn((DbORForeign) targetDbo, foreignCol, sourceCol);
            } else if (metaClass == DbORIndexKey.metaClass) {
                DbORIndexKey sourceKey = (DbORIndexKey) dbEnum.nextElement();
                DbORColumn column = sourceKey.getIndexedElement();
                if (column != null) {
                    column = (DbORColumn) column.findMatchingObject();
                    if (column == null)
                        continue;
                }
                DbORIndexKey targetKey = new DbORIndexKey(targetDbo);
                targetKey.setIndexedElement(column);
                targetKey.setExpression(sourceKey.getExpression());
                targetKey.setSortOption(sourceKey.getSortOption());
            } else if (metaClass == DbINFFragment.metaClass) {
                DbINFFragment sourceFrag = (DbINFFragment) dbEnum.nextElement();
                DbINFDbspace dbspace = sourceFrag.getDbspace();
                if (dbspace != null) {
                    dbspace = (DbINFDbspace) dbspace.findMatchingObject();
                    if (dbspace == null)
                        continue;
                }
                DbINFFragment targetFrag = new DbINFFragment(targetDbo);
                targetFrag.setDbspace(dbspace);
                targetFrag.setExpression(sourceFrag.getExpression());
                targetFrag.setRemainder(sourceFrag.getRemainder());
            }
        }
        dbEnum.close();
    }

    protected void reportHeader(StringBuffer reportBuffer) throws DbException {
        reportBuffer.append(colHeaders[COL_LEFT] + ": " + leftModel.getFullDisplayName()
                + getTSSuffix(leftModel) + "\n"); // NOT LOCALIZABLE
        reportBuffer.append(colHeaders[COL_RIGHT] + ": " + rightModel.getFullDisplayName()
                + getTSSuffix(rightModel) + "\n"); // NOT LOCALIZABLE
        reportBuffer.append(kDate + ": "
                + DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()))
                + "\n\n"); // NOT
        // LOCALIZABLE
    }

    public static String getTSSuffix(DbObject dbo) throws DbException {
        if (!(dbo instanceof DbORModel))
            return "";
        DbSMSTargetSystem dbts = ((DbORModel) dbo).getTargetSystem();
        return " <" + dbts.getName() + " " + dbts.getVersion() + ">"; // NOT
        // LOCALIZABLE
    }

    public static CheckTreeNode getFieldTree(DbObject leftModel, DbObject rightModel) {
        CheckTreeNode rootNode = new CheckTreeNode(null, true, true);
        int rootID = -1;
        if (leftModel instanceof DbORModel && leftModel.getMetaClass() == rightModel.getMetaClass())
            rootID = AnyORObject.getMetaClassRootID(leftModel.getMetaClass());

        if (leftModel instanceof DbORDataModel)
            getDataModelFieldTree((DbORDataModel) leftModel, rootNode, rootID, false);
        else if (leftModel instanceof DbOROperationLibrary)
            getOperationLibraryFieldTree((DbOROperationLibrary) leftModel, rootNode, rootID, false);
        else if (leftModel instanceof DbORDomainModel)
            getDomainModelFieldTree((DbORDomainModel) leftModel, rootNode, false);
        else if (leftModel instanceof DbORCommonItemModel)
            getCommonItemModelFieldTree((DbORCommonItemModel) leftModel, rootNode, false);
        else if (leftModel instanceof DbOOAbsPackage)
            getOOPackageFieldTree((DbOOAbsPackage) leftModel, rootNode, false);
        else if (leftModel instanceof DbORDatabase)
            getDatabaseFieldTree((DbORDatabase) leftModel, rootNode, rootID, false);
        else if (leftModel instanceof DbBEModel)
            getBEModelFieldTree((DbBEModel) leftModel, rootNode, false);

        return rootNode;
    }

    // Internal action to prepare synchronization scopes.
    public static void editSynchroScope(int rootID) {
        CheckTreeNode rootNode = new CheckTreeNode(null, true, true);
        getDataModelFieldTree(null, rootNode, rootID, true);
        getOperationLibraryFieldTree(null, rootNode, rootID, true);
        getDomainModelFieldTree(null, rootNode, true);
        IntegrateScopeDialog scopeDlg = new IntegrateScopeDialog(rootNode, null,
                IntegrateScopeDialog.INTEGRATION);
        scopeDlg.setVisible(true);
    }

    private static void getDataModelFieldTree(DbORDataModel dataModel, CheckTreeNode rootNode,
            int rootID, boolean isSynchro) {
        MetaClass[] metaClasses = (rootID == -1 ? AnyORObject.ORMetaClasses : AnyORObject
                .getTargetMetaClasses(rootID));
        CheckTreeNode classNode;

        classNode = getClassNode(dataModel, metaClasses[AnyORObject.I_TABLE], isSynchro);
        if (rootID == TargetSystem.SGBD_INFORMIX_ROOT)
            if (classNode != null)
                classNode.add(new CheckTreeNode(DbINFFragment.metaClass, false, true,
                        DbINFFragment.metaClass.getGUIName(true)));
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(dataModel, metaClasses[AnyORObject.I_VIEW], isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(dataModel, metaClasses[AnyORObject.I_COLUMN], isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(dataModel, metaClasses[AnyORObject.I_PRIMARYUNIQUE], isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(dataModel, metaClasses[AnyORObject.I_FOREIGN], isSynchro);
        if (classNode != null) {
            classNode.add(new CheckTreeNode(DbORFKeyColumn.metaClass, false, true,
                    DbORFKeyColumn.metaClass.getGUIName(true)));
            if (isSynchro) {
                classNode.add(new CheckTreeNode(new MetaField[] { DbORForeign.fAssociationEnd,
                        DbORAssociationEnd.fInsertRule }, false, true, MessageFormat.format(
                        LocaleMgr.misc.getString("RuleChild0"),
                        new Object[] { DbORAssociationEnd.fInsertRule.getGUIName() })));
                classNode.add(new CheckTreeNode(new MetaField[] { DbORForeign.fAssociationEnd,
                        DbORAssociationEnd.fUpdateRule }, false, true, MessageFormat.format(
                        LocaleMgr.misc.getString("RuleChild0"),
                        new Object[] { DbORAssociationEnd.fUpdateRule.getGUIName() })));
                classNode.add(new CheckTreeNode(new MetaField[] { DbORForeign.fAssociationEnd,
                        DbORAssociationEnd.fDeleteRule }, false, true, MessageFormat.format(
                        LocaleMgr.misc.getString("RuleChild0"),
                        new Object[] { DbORAssociationEnd.fDeleteRule.getGUIName() })));
                classNode.add(new CheckTreeNode(FK_PARENT_INSERT_RULE, false, true, MessageFormat
                        .format(LocaleMgr.misc.getString("RuleParent0"),
                                new Object[] { DbORAssociationEnd.fInsertRule.getGUIName() })));
                classNode.add(new CheckTreeNode(FK_PARENT_UPDATE_RULE, false, true, MessageFormat
                        .format(LocaleMgr.misc.getString("RuleParent0"),
                                new Object[] { DbORAssociationEnd.fUpdateRule.getGUIName() })));
                classNode.add(new CheckTreeNode(FK_PARENT_DELETE_RULE, false, true, MessageFormat
                        .format(LocaleMgr.misc.getString("RuleParent0"),
                                new Object[] { DbORAssociationEnd.fDeleteRule.getGUIName() })));
                classNode.add(new CheckTreeNode(new MetaField[] { DbORForeign.fAssociationEnd,
                        DbORAssociationEnd.fReferencedConstraint }, false, true,
                        DbORAssociationEnd.fReferencedConstraint.getGUIName()));
            }
        }
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(dataModel, metaClasses[AnyORObject.I_INDEX], isSynchro);
        if (classNode != null)
            classNode.add(new CheckTreeNode(DbORIndexKey.metaClass, false, true,
                    DbORIndexKey.metaClass.getGUIName(true)));
        if (rootID == TargetSystem.SGBD_INFORMIX_ROOT)
            if (classNode != null)
                classNode.add(new CheckTreeNode(DbINFFragment.metaClass, false, true,
                        DbINFFragment.metaClass.getGUIName(true)));
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(dataModel, metaClasses[AnyORObject.I_CHECK], isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(dataModel, metaClasses[AnyORObject.I_TRIGGER], isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        if (!isSynchro) {
            classNode = getClassNode(dataModel, DbORAssociation.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(dataModel, DbORAssociationEnd.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);
        }

        if (rootID == TargetSystem.SGBD_ORACLE_ROOT) {
            classNode = getClassNode(dataModel, DbORADatabase.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(dataModel, DbORADataFile.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(dataModel, DbORALobStorage.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(dataModel, DbORANestedTableStorage.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(dataModel, DbORAPartition.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(dataModel, DbORASubPartition.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(dataModel, DbORARedoLogGroup.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(dataModel, DbORARedoLogFile.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(dataModel, DbORARollbackSegment.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(dataModel, DbORASequence.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(dataModel, DbORATablespace.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);
        }

        else if (rootID == TargetSystem.SGBD_INFORMIX_ROOT) {
            classNode = getClassNode(dataModel, DbINFDatabase.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(dataModel, DbINFDbspace.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(dataModel, DbINFSbspace.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);
        }

        else if (rootID == TargetSystem.SGBD_IBM_DB2_ROOT) {
            classNode = getClassNode(dataModel, DbIBMDatabase.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);
        } else {
            classNode = getClassNode(dataModel, DbGEDatabase.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);
        }
    }

    // This method is used for non standard path (non metafield or metaclass)
    // elements
    protected void buildCustomProperty(ArrayList propList, DbObject leftDbo, DbObject rightDbo,
            CheckTreeNode fieldNode, int action) throws DbException {
        if (!isSynchro)
            return;
        Object property = fieldNode.getUserObject();
        if (property == null)
            return;
        if (!(leftDbo instanceof DbORForeign) && !(leftDbo instanceof DbORForeign))
            return;
        MetaField field = null;
        if (property.equals(SMSIntegrateModel.FK_PARENT_INSERT_RULE)) {
            field = DbORAssociationEnd.fInsertRule;
        } else if (property.equals(SMSIntegrateModel.FK_PARENT_UPDATE_RULE)) {
            field = DbORAssociationEnd.fUpdateRule;
        } else if (property.equals(SMSIntegrateModel.FK_PARENT_DELETE_RULE)) {
            field = DbORAssociationEnd.fDeleteRule;
        }
        if (field != null) {
            Object leftVal = (leftDbo != null ? ((DbORForeign) leftDbo).getAssociationEnd()
                    .getOppositeEnd().get(field) : null);
            Object rightVal = (rightDbo != null ? ((DbORForeign) rightDbo).getAssociationEnd()
                    .getOppositeEnd().get(field) : null);
            boolean isEqual = DbObject.valuesAreEqual(leftVal, rightVal);
            if (!isEqual && !isEquivalent(field, leftDbo, leftVal, rightDbo, rightVal)) {
                String leftStr = (leftVal != null ? leftVal.toString() : null);
                String rightStr = (rightVal != null ? rightVal.toString() : null);
                propList.add(new IntegrateNode.IntegrateProperty(property, fieldNode.toString(),
                        leftStr, rightStr, action));
            }
        }
    }

    // update the targetDbo value with sourceDbo value
    protected void integrateCustomProperty(DbObject sourceDbo, DbObject targetDbo, String property)
            throws DbException {
        if (!isSynchro)
            return;
        MetaField field = null;
        if (property.equals(SMSIntegrateModel.FK_PARENT_INSERT_RULE)) {
            field = DbORAssociationEnd.fInsertRule;
        } else if (property.equals(SMSIntegrateModel.FK_PARENT_UPDATE_RULE)) {
            field = DbORAssociationEnd.fUpdateRule;
        } else if (property.equals(SMSIntegrateModel.FK_PARENT_DELETE_RULE)) {
            field = DbORAssociationEnd.fDeleteRule;
        }
        if (field != null) {
            ((DbORForeign) targetDbo).getAssociationEnd().getOppositeEnd().set(field,
                    ((DbORForeign) sourceDbo).getAssociationEnd().getOppositeEnd().get(field));
        }
    }

    private static void getDatabaseFieldTree(DbORDatabase model, CheckTreeNode rootNode,
            int rootID, boolean isSynchro) {
        if (isSynchro)
            return;

        CheckTreeNode classNode;

        if (rootID == TargetSystem.SGBD_ORACLE_ROOT) {
            classNode = getClassNode(model, DbORADatabase.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(model, DbORADataFile.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(model, DbORARedoLogGroup.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(model, DbORARedoLogFile.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(model, DbORARollbackSegment.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(model, DbORATablespace.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);
        }

        else if (rootID == TargetSystem.SGBD_INFORMIX_ROOT) {
            classNode = getClassNode(model, DbINFDatabase.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(model, DbINFDbspace.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);

            classNode = getClassNode(model, DbINFSbspace.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);
        } else if (rootID == TargetSystem.SGBD_IBM_DB2_ROOT) {
            classNode = getClassNode(model, DbIBMDatabase.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);
        } else {
            classNode = getClassNode(model, DbGEDatabase.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);
        }
    }

    private static void getOperationLibraryFieldTree(DbOROperationLibrary model,
            CheckTreeNode rootNode, int rootID, boolean isSynchro) {
        MetaClass[] metaClasses = (rootID == -1 ? AnyORObject.ORMetaClasses : AnyORObject
                .getTargetMetaClasses(rootID));
        CheckTreeNode classNode;

        classNode = getClassNode(model, metaClasses[AnyORObject.I_PROCEDURE], isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(model, metaClasses[AnyORObject.I_PARAMETER], isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        if (rootID == TargetSystem.SGBD_ORACLE_ROOT) {
            classNode = getClassNode(model, DbORAPackage.metaClass, isSynchro);
            addClassNode(rootNode, classNode, isSynchro);
        }
    }

    private static void getDomainModelFieldTree(DbORDomainModel model, CheckTreeNode rootNode,
            boolean isSynchro) {
        CheckTreeNode classNode;
        classNode = getClassNode(model, DbORDomain.metaClass, isSynchro);
        classNode.add(new CheckTreeNode(DbORAllowableValue.metaClass, false, true,
                DbORAllowableValue.metaClass.getGUIName(true)));
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(model, DbORField.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);
    }

    private static void getCommonItemModelFieldTree(DbORCommonItemModel model,
            CheckTreeNode rootNode, boolean isSynchro) {
        CheckTreeNode classNode;
        classNode = getClassNode(model, DbORCommonItem.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);
    }

    private static void getOOPackageFieldTree(DbOOAbsPackage model, CheckTreeNode rootNode,
            boolean isSynchro) {
        CheckTreeNode classNode;
        classNode = getClassNode(model, DbJVClass.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(model, DbJVDataMember.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(model, DbJVMethod.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(model, DbJVConstructor.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(model, DbJVParameter.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(model, DbJVInitBlock.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(model, DbJVCompilationUnit.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);

        classNode = getClassNode(model, DbJVImport.metaClass, isSynchro);
        addClassNode(rootNode, classNode, isSynchro);
    }

    private static void getBEModelFieldTree(DbBEModel model, CheckTreeNode rootNode,
            boolean isSynchro) {
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

    private static CheckTreeNode getClassNode(DbSMSPackage smsPackage, MetaClass metaClass,
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

        String term = terminology.getTerm(metaClass);
        if (term == null)
            return null;

        CheckTreeNode classNode = new CheckTreeNode(metaClass, true, true, term);
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

            term = terminology.getTerm(metaClass, metaField, true);
            if (term == null)
                continue;
            classNode.add(new CheckTreeNode(metaField, false, true, term));
        }

        MetaField[] allFields = metaClass.getAllMetaFields();
        for (i = 0; i < allFields.length; i++) {
            MetaField metaField = allFields[i];
            if (metaField instanceof MetaRelationN && isValidRel((MetaRelationship) metaField)) {
                term = terminology.getTerm(metaClass, metaField, true);
                if (term == null)
                    continue;
                classNode.add(new CheckTreeNode(metaField, false, true, term));
            }
        }

        if (classNode.getChildCount() == 0) {

            // we have a concept that was disallowed by the terminology (no
            // fields are accessible)
            // so we must delete that node from the list
            return null;

        } else {
            if (DbORAbsTable.metaClass.isAssignableFrom(metaClass)) {
                term = terminology.getTerm(DbORColumn.metaClass);
                if (term != null)
                    classNode.add(new CheckTreeNode(DbObject.fComponents, false, true, kSequence
                            + " - " + term));
            } else if (DbORProcedure.metaClass.isAssignableFrom(metaClass)) {
                term = terminology.getTerm(DbORParameter.metaClass, true);
                if (term != null)
                    classNode.add(new CheckTreeNode(DbObject.fComponents, false, true, kSequence
                            + " - " + term));
            }
        }

        return classNode;
    }

    private static void addClassNode(CheckTreeNode rootNode, CheckTreeNode classNode,
            boolean isSynchro) {
        if (classNode == null)
            return;

        if (!isSynchro) {
            MetaClass metaClass = (MetaClass) classNode.getUserObject();
            if (DbSemanticalObject.class.isAssignableFrom(metaClass.getJClass())
                    && (metaClass.getFlags() & MetaClass.NO_UDF) == 0)
                classNode
                        .add(new CheckTreeNode(null, false, true, DbUDF.metaClass.getGUIName(true)));
        }
        rootNode.add(classNode);
    }

    // If list of 1-relations to be excluded becomes too long, better define a
    // User Property <not integrable> in GenMeta.
    private static boolean isValidRel(MetaRelationship metaRel) {
        if (metaRel instanceof MetaRelationN)
            return (metaRel.getFlags() & MetaField.INTEGRABLE) != 0;
        else
            return (metaRel != DbSMSSemanticalObject.fSuperCopy && metaRel != DbSMSAbstractPackage.fTargetSystem);
    }

}
