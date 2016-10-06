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

package org.modelsphere.sms;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.SemanticalModel;
import org.modelsphere.jack.srtool.explorer.DynamicNode;
import org.modelsphere.jack.srtool.explorer.Explorer;
import org.modelsphere.jack.srtool.explorer.GroupParams;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.SrVector;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEActorGo;
import org.modelsphere.sms.be.db.DbBEContextCell;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEStoreGo;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseGo;
import org.modelsphere.sms.be.db.DbBEUseCaseResource;
import org.modelsphere.sms.db.DbSMSBuiltInTypeNode;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSNotice;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSParameter;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.DbSMSUmlConstraint;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.db.DbSMSUserDefinedPackage;
import org.modelsphere.sms.db.util.DbInitialization;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.db.DbOOAbstractMethod;
import org.modelsphere.sms.oo.db.DbOOAssociationEnd;
import org.modelsphere.sms.oo.db.DbOOClass;
import org.modelsphere.sms.oo.db.DbOOClassModel;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.db.DbOOOperation;
import org.modelsphere.sms.oo.db.DbOOParameter;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVCompilationUnit;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVInitBlock;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVPrimitiveType;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAbstractMethod;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORAttribute;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORChoiceOrSpecialization;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORField;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORIndexKey;
import org.modelsphere.sms.or.db.DbORModel;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORParameter;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORProcedure;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTrigger;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.db.DbORUserNode;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.db.srtypes.ORChoiceSpecializationCategory;
import org.modelsphere.sms.or.db.srtypes.ORDomainCategory;
import org.modelsphere.sms.or.generic.db.DbGEProcedure;
import org.modelsphere.sms.or.ibm.db.DbIBMBufferPool;
import org.modelsphere.sms.or.ibm.db.DbIBMDataModel;
import org.modelsphere.sms.or.ibm.db.DbIBMDatabase;
import org.modelsphere.sms.or.ibm.db.DbIBMDbPartitionGroup;
import org.modelsphere.sms.or.ibm.db.DbIBMProcedure;
import org.modelsphere.sms.or.ibm.db.DbIBMSequence;
import org.modelsphere.sms.or.ibm.db.DbIBMTablespace;
import org.modelsphere.sms.or.informix.db.DbINFDatabase;
import org.modelsphere.sms.or.informix.db.DbINFDbspace;
import org.modelsphere.sms.or.informix.db.DbINFProcedure;
import org.modelsphere.sms.or.informix.db.DbINFSbspace;
import org.modelsphere.sms.or.oracle.db.DbORAAbsTable;
import org.modelsphere.sms.or.oracle.db.DbORADataFile;
import org.modelsphere.sms.or.oracle.db.DbORADataModel;
import org.modelsphere.sms.or.oracle.db.DbORADatabase;
import org.modelsphere.sms.or.oracle.db.DbORAIndex;
import org.modelsphere.sms.or.oracle.db.DbORALobStorage;
import org.modelsphere.sms.or.oracle.db.DbORANestedTableStorage;
import org.modelsphere.sms.or.oracle.db.DbORAPackage;
import org.modelsphere.sms.or.oracle.db.DbORAPartition;
import org.modelsphere.sms.or.oracle.db.DbORAProcedure;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogFile;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogGroup;
import org.modelsphere.sms.or.oracle.db.DbORARollbackSegment;
import org.modelsphere.sms.or.oracle.db.DbORASequence;
import org.modelsphere.sms.or.oracle.db.DbORASubPartition;
import org.modelsphere.sms.or.oracle.db.DbORATablespace;

@SuppressWarnings("serial")
public final class SMSExplorer extends Explorer {
    private static final String kDeployed0 = LocaleMgr.misc.getString("Deployed0");
    private static final String kUsed0 = LocaleMgr.misc.getString("Used0");
    private static final String kDefault0 = LocaleMgr.misc.getString("Default0");
    private static final String kUserDefinedTypes = LocaleMgr.misc.getString("UserDefinedTypes");

    private static final Icon flowLineImage = GraphicUtil.loadIcon(SMSExplorer.class,
            "be/international/resources/flowline.gif");
    private static final Icon flowBiDirImage = GraphicUtil.loadIcon(SMSExplorer.class,
            "be/international/resources/flowbidir.gif");
    private static final Icon flowRigthImage = GraphicUtil.loadIcon(SMSExplorer.class,
            "be/international/resources/flowright.gif");
    public static final Icon beExternalUseCaseImage = GraphicUtil.loadIcon(SMSExplorer.class,
            "be/international/resources/externalusecase.gif");
    public static final Icon udtModelImage = GraphicUtil.loadIcon(SMSExplorer.class,
            "or/international/resources/udtmodel.gif");
    public static final Icon udtImage = GraphicUtil.loadIcon(SMSExplorer.class,
            "or/international/resources/udt.gif");
    public static final Icon noticeImage = GraphicUtil.loadIcon(SMSExplorer.class,
            "db/resources/dbsmsnotice.gif");
    public static final Icon treeDiagramImage = GraphicUtil.loadIcon(SMSExplorer.class,
            "be/db/resources/dbbetreediagram.jpg");
    public static final Icon associationTableIcon = GraphicUtil.loadIcon(SMSExplorer.class,
            "or/db/resources/dborasstable.gif");
    public static final Icon erModelIcon = GraphicUtil.loadIcon(SMSExplorer.class,
            "or/db/resources/dborconceptualmodel.gif");
    public static final Icon arcIcon = GraphicUtil.loadIcon(SMSExplorer.class,
            "or/db/resources/dborarc.gif");
    public static final Icon specIcon = GraphicUtil.loadIcon(SMSExplorer.class,
            "or/db/resources/dborspecialization.gif");
    public static final Icon choiceIcon = GraphicUtil.loadIcon(SMSExplorer.class,
            "or/db/resources/dborchoice.gif");

    private ExplorerRefreshListener explorerRefreshListener = new ExplorerRefreshListener();

    private TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    // Groups under DbSMSProject, DbSMSUserDefinedPackage
    public static final GroupParams builtInNodeGroup = new GroupParams(1);
    public static final GroupParams umlExtensibilityGroup = new GroupParams(2);
    public static final GroupParams userNodeGroup = new GroupParams(3);
    public static final GroupParams userPackageGroup = new GroupParams(4);
    public static final GroupParams classModelGroup = new GroupParams(5);
    public static final GroupParams domainModelGroup = new GroupParams(6);
    public static final GroupParams commonItemModelGroup = new GroupParams(7);
    public static final GroupParams dataModelGroup = new GroupParams(8);
    public static final GroupParams operationLibraryGroup = new GroupParams(9);
    public static final GroupParams databaseGroup = new GroupParams(10);
    public static final GroupParams bpmModelGroup = new GroupParams(11);
    public static final GroupParams linkModelGroup = new GroupParams(12);

    // Groups under DbSMSUmlExtensibility
    public static final GroupParams stereotypeGroup = new GroupParams(1, DbSMSStereotype.metaClass
            .getGUIName(true), DbSMSStereotype.metaClass.getIcon());
    public static final GroupParams umlConstraintGroup = new GroupParams(2,
            DbSMSUmlConstraint.metaClass.getGUIName(true), DbSMSUmlConstraint.metaClass.getIcon());

    // Groups under DbOOAbsPackage, DbORCommonItemModel, DbORDataModel,
    // DbORDomainModel
    public static final GroupParams diagramGroup = new GroupParams(2);

    // Groups under DbOOAbsPackage
    public static final GroupParams packageGroup = new GroupParams(5);
    public static final GroupParams classGroup = new GroupParams(3, DbOOClass.metaClass
            .getGUIName(true), DbJVClass.metaClass.getIcon());
    public static final GroupParams compilGroup = new GroupParams(4, DbJVCompilationUnit.metaClass
            .getGUIName(true), DbJVCompilationUnit.metaClass.getIcon());
    public static final GroupParams noticeGroup = new GroupParams(70, DbSMSNotice.metaClass
            .getGUIName(true), noticeImage);
    //private static final String ENTITY_RELATIONSHIP_TXT = org.modelsphere.sms.or.international.LocaleMgr.misc
    //        .getString("EntityRelationship");

    // Groups under DbORDataModel

    public static final GroupParams relationshipGroup = new GroupParams(4, DbORTable.metaClass
            .getGUIName(true), associationTableIcon);

    public static final GroupParams tableGroup = new GroupParams(3, DbORTable.metaClass
            .getGUIName(true), DbORTable.metaClass.getIcon());

    public static final GroupParams viewGroup = new GroupParams(7,
            DbORView.metaClass.getGUIName(true), DbORView.metaClass.getIcon());

    public static final GroupParams assocGroup = new GroupParams(6, DbORAssociation.metaClass
            .getGUIName(true), DbORAssociation.metaClass.getIcon());

    public static final GroupParams arcGroup = new GroupParams(5, DbORAssociation.metaClass
            .getGUIName(true), arcIcon);

    public static final GroupParams specGroup = new GroupParams(8,
            DbORChoiceOrSpecialization.metaClass.getGUIName(true), specIcon);

    // Oracle sequences
    public static final GroupParams sequenceGroup = new GroupParams(5, DbORASequence.metaClass
            .getGUIName(true, false), DbORASequence.metaClass.getIcon());
    // DB2-UDB sequences
    public static final GroupParams sequenceGroup2 = new GroupParams(5, DbIBMSequence.metaClass
            .getGUIName(true, false), DbIBMSequence.metaClass.getIcon());

    public static final GroupParams subModelGroup = new GroupParams(60, LocaleMgr.screen
            .getString("subModels"), DbORDataModel.metaClass.getIcon());

    public static final GroupParams subModelGroupER = new GroupParams(60, LocaleMgr.screen
            .getString("subModels"), erModelIcon);

    public static final GroupParams noticeGroup2 = new GroupParams(70, DbSMSNotice.metaClass
            .getGUIName(true), noticeImage);

    // Groups under DbORCommonItemModel
    public static final GroupParams commonItemGroup = new GroupParams(3, DbORCommonItem.metaClass
            .getGUIName(true), DbORCommonItem.metaClass.getIcon());
    public static final GroupParams noticeGroup3 = new GroupParams(70, DbSMSNotice.metaClass
            .getGUIName(true), noticeImage);

    // Groups under DbBEModel
    public static final GroupParams contextGroup = new GroupParams(3);
    public static final GroupParams actorGroup = new GroupParams(4, DbBEActor.metaClass
            .getGUIName(true), DbBEActor.metaClass.getIcon());
    public static final GroupParams storeGroup = new GroupParams(5, DbBEStore.metaClass
            .getGUIName(true), DbBEStore.metaClass.getIcon());
    public static final GroupParams resourceGroup = new GroupParams(6, DbBEResource.metaClass
            .getGUIName(true), DbBEResource.metaClass.getIcon());
    public static final GroupParams qualifierGroup = new GroupParams(7, DbBEQualifier.metaClass
            .getGUIName(true), DbBEQualifier.metaClass.getIcon());
    public static final GroupParams noticeGroup4 = new GroupParams(70, DbSMSNotice.metaClass
            .getGUIName(true), noticeImage);

    // Groups under DbBEUseCase
    public static final GroupParams subUseCaseGroup = new GroupParams(4/*
                                                                        * , DbBEUseCase . metaClass
                                                                        * . getGUIName ( true) ,
                                                                        * LocaleMgr . misc.
                                                                        * getImageIcon (
                                                                        * "subUseCaseGroup" )
                                                                        */);
    public static final GroupParams flowGroup = new GroupParams(9, DbBEFlow.metaClass
            .getGUIName(true), DbBEFlow.metaClass.getIcon());
    public static final GroupParams flowEndsGroup = new GroupParams(10);

    public static final GroupParams deployedActorsGroup = new GroupParams(11, MessageFormat.format(
            kUsed0, new Object[] { DbBEActor.metaClass.getGUIName(true) }), DbBEActor.metaClass
            .getIcon(), true);
    public static final GroupParams deployedStoresGroup = new GroupParams(12, MessageFormat.format(
            kUsed0, new Object[] { DbBEStore.metaClass.getGUIName(true) }), DbBEStore.metaClass
            .getIcon(), true);

    // Groups under DbOROperationLibrary
    public static final GroupParams oraPackageGroup = new GroupParams(1, DbORAPackage.metaClass
            .getGUIName(true, false), DbORAPackage.metaClass.getIcon());
    public static final GroupParams procedureGroup = new GroupParams(2, DbORProcedure.metaClass
            .getGUIName(true), LocaleMgr.misc.getImageIcon("Procedures"));

    // Groups under DbORDatabase

    public static final GroupParams defaultDomainsGroup = new GroupParams(4, MessageFormat.format(
            kDefault0, new Object[] { DbORDomain.metaClass.getGUIName(true) }),
            DbORDomain.metaClass.getIcon());

    public static final GroupParams deployedTypesGroup = new GroupParams(5, MessageFormat.format(
            kDeployed0, new Object[] { kUserDefinedTypes }), udtImage, true);
    public static final GroupParams deployedTablesGroup = new GroupParams(6, MessageFormat.format(
            kDeployed0, new Object[] { DbORTable.metaClass.getGUIName(true) }), DbORTable.metaClass
            .getIcon(), true);
    // not used
    public static final GroupParams deployedAssociationsGroup = new GroupParams(7, MessageFormat
            .format(kDeployed0, new Object[] { DbORAssociation.metaClass.getGUIName(true) }),
            DbORAssociation.metaClass.getIcon(), true);
    public static final GroupParams deployedProceduresGroup = new GroupParams(10, MessageFormat
            .format(kDeployed0, new Object[] { DbORProcedure.metaClass.getGUIName(true) }),
            LocaleMgr.misc.getImageIcon("Procedures"), true);

    // Groups under DbORADatabase
    public static final GroupParams redoLogFilGroup = new GroupParams(1,
            DbORARedoLogGroup.metaClass.getGUIName(true, false), DbORARedoLogGroup.metaClass
                    .getIcon());
    public static final GroupParams rollbackGroup = new GroupParams(2,
            DbORARollbackSegment.metaClass.getGUIName(true, false), DbORARollbackSegment.metaClass
                    .getIcon());
    public static final GroupParams tablespaceGroup = new GroupParams(3, DbORATablespace.metaClass
            .getGUIName(true, false), DbORATablespace.metaClass.getIcon());
    public static final GroupParams deployedSequencesGroup = new GroupParams(8, MessageFormat
            .format(kDeployed0, new Object[] { DbORASequence.metaClass.getGUIName(true, false) }),
            DbORASequence.metaClass.getIcon(), true);
    public static final GroupParams deployedORAPackagesGroup = new GroupParams(9, MessageFormat
            .format(kDeployed0, new Object[] { DbORAPackage.metaClass.getGUIName(true, false) }),
            DbORAPackage.metaClass.getIcon(), true);

    // Groups under DbOINFDatabase
    public static final GroupParams dbspaceGroup = new GroupParams(1, DbINFDbspace.metaClass
            .getGUIName(true, false), DbINFDbspace.metaClass.getIcon());
    public static final GroupParams sbspaceGroup = new GroupParams(2, DbINFSbspace.metaClass
            .getGUIName(true, false), DbINFSbspace.metaClass.getIcon());

    // Groups under DbOOClass
    public static final GroupParams fieldGroup = new GroupParams(1, DbOODataMember.metaClass
            .getGUIName(true), DbJVDataMember.metaClass.getIcon());
    public static final GroupParams operationGroup = new GroupParams(2, DbOOOperation.metaClass
            .getGUIName(true), DbJVMethod.metaClass.getIcon());
    public static final GroupParams innerClassGroup = new GroupParams(3, LocaleMgr.screen
            .getString("Nested/InnerClasses"), DbJVClass.metaClass.getIcon());

    // Groups under DbORAbsTable
    // Note that primaryGroup and uniqueGroup have same name; so they are under
    // the same grouping node.
    public static GroupParams columnGroup = new GroupParams(1, DbORColumn.metaClass
            .getGUIName(true), DbORColumn.metaClass.getIcon(), false);
    public static final GroupParams primaryGroup = new GroupParams(2, DbORPrimaryUnique.metaClass
            .getGUIName(true), DbORPrimaryUnique.metaClass.getIcon());
    public static final GroupParams uniqueGroup = new GroupParams(3, DbORPrimaryUnique.metaClass
            .getGUIName(true), DbORPrimaryUnique.metaClass.getIcon());
    public static final GroupParams foreignGroup = new GroupParams(4, DbORForeign.metaClass
            .getGUIName(true), DbORForeign.metaClass.getIcon());
    public static final GroupParams indexGroup = new GroupParams(5, DbORIndex.metaClass
            .getGUIName(true), DbORIndex.metaClass.getIcon());
    public static GroupParams checkGroup = new GroupParams(6, DbORCheck.metaClass.getGUIName(true),
            DbORCheck.metaClass.getIcon());
    public static GroupParams triggerGroup = new GroupParams(7, DbORTrigger.metaClass
            .getGUIName(true), DbORTrigger.metaClass.getIcon());
    // Groups under DbORAAbsTable
    public static final GroupParams lobStorageGroup = new GroupParams(8, LocaleMgr.misc
            .getString("lobstorages"), DbORALobStorage.metaClass.getIcon());
    public static final GroupParams partitionGroup = new GroupParams(9, DbORAPartition.metaClass
            .getGUIName(true, false), DbORAPartition.metaClass.getIcon());
    public static final GroupParams storageTabGroup = new GroupParams(10, LocaleMgr.misc
            .getString("nestedtables"), DbORANestedTableStorage.metaClass.getIcon());

    // Groups under DbORIndex
    public static final GroupParams indexKeyGroup = new GroupParams(1, DbORIndexKey.metaClass
            .getGUIName(true), DbORIndexKey.metaClass.getIcon());

    // Groups under DbORDomain
    public static final GroupParams orFieldGroup = new GroupParams(1, DbORField.metaClass
            .getGUIName(true), DbORField.metaClass.getIcon());

    // Groups under DbSMSBehavioralFeature
    public static final GroupParams parameterGroup = new GroupParams(1, DbSMSParameter.metaClass
            .getGUIName(true), DbSMSParameter.metaClass.getIcon(), false);
    // Groups under DbORAPTablespace
    public static final GroupParams dataFileGroup = new GroupParams(1, DbORADataFile.metaClass
            .getGUIName(true, false), DbORADataFile.metaClass.getIcon());
    // Groups under DbORAPartition
    public static final GroupParams subPartGroup = new GroupParams(1, LocaleMgr.screen
            .getString("SubPartitionList"), DbORASubPartition.metaClass.getIcon());

    // Groups under DbIBMDatabase
    public static final GroupParams ibmDbPartGroup = new GroupParams(1,
            DbIBMDbPartitionGroup.metaClass.getGUIName(true), DbIBMDbPartitionGroup.metaClass
                    .getIcon());
    public static final GroupParams ibmBufPoolGroup = new GroupParams(2, DbIBMBufferPool.metaClass
            .getGUIName(true), DbIBMBufferPool.metaClass.getIcon());
    public static final GroupParams ibmTablespaceGroup = new GroupParams(3,
            DbIBMTablespace.metaClass.getGUIName(true), DbIBMTablespace.metaClass.getIcon());

    // Groups under DbIBMDatabase
    // public static final GroupParams ibmContainerClauseGroup = new
    // GroupParams(1, DbIBMContainerClause.metaClass.getGUIName(true),
    // DbIBMContainerClause.metaClass.getIcon());

    // Special comparator for use cases
    private static Comparator<DynamicNode> numericIdComparator = new Comparator<DynamicNode>() {

        @Override
        public int compare(DynamicNode node1, DynamicNode node2) {
            if (node1.getGroupParams().index != node2.getGroupParams().index)
                return (node1.getGroupParams().index < node2.getGroupParams().index ? -1 : 1);
            if (node1.getGroupParams().sorted) {
                String value1 = node1.toString();
                String value2 = node2.toString();
                if (node1.getUserObject() instanceof DbBEUseCase) {
                    Integer int1 = null;
                    Integer int2 = null;
                    try {
                        // try parsing an integer with the string value
                        // if the value contains the numeric id, children must
                        // be sorted
                        // with a numeric order (ie: 1, 10, 11, 100 instead of
                        // 1, 10, 100, 11)
                        int1 = value1 == null ? new Integer(0) : new Integer(Integer
                                .parseInt(value1));
                        int2 = value2 == null ? new Integer(0) : new Integer(Integer
                                .parseInt(value2));
                    } catch (Exception e) {
                    }
                    if (int1 != null && int2 != null)
                        return int1.compareTo(int2);
                    else
                        return CollationComparator.getDefaultCollator().compare(value1, value2);
                }
                return CollationComparator.getDefaultCollator().compare(value1, value2);
            } else
                return (node1.getInsertIndex() < node2.getInsertIndex() ? -1 : 1);
        }

        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    };

    protected final GroupParams getGroupParams(DbObject dbParent, DbObject dbo) throws DbException {
        if (dbParent instanceof DbSMSProject || dbParent instanceof DbSMSUserDefinedPackage) {
            if (dbo instanceof DbSMSBuiltInTypeNode)
                return builtInNodeGroup;
            if (dbo instanceof DbORUserNode)
                return userNodeGroup;
            if (dbo instanceof DbSMSUmlExtensibility)
                return umlExtensibilityGroup;
            if (dbo instanceof DbSMSUserDefinedPackage)
                return userPackageGroup;
            if (dbo instanceof DbOOClassModel)
                return classModelGroup;
            if (dbo instanceof DbORDomainModel)
                return domainModelGroup;
            if (dbo instanceof DbORCommonItemModel)
                return commonItemModelGroup;
            if (dbo instanceof DbORDataModel)
                return dataModelGroup;
            if (dbo instanceof DbOROperationLibrary)
                return operationLibraryGroup;
            if (dbo instanceof DbORDatabase)
                return databaseGroup;
            if (dbo instanceof DbBEModel)
                return bpmModelGroup;
            if (dbo instanceof DbSMSLinkModel)
                return linkModelGroup;
        } else if (dbParent instanceof DbSMSPackage) {
            // *OO Package
            if (dbParent instanceof DbOOAbsPackage) {
                if (dbo instanceof DbOOAbsPackage)
                    return packageGroup;
                if (dbo instanceof DbOODiagram)
                    return diagramGroup;
                if (dbo instanceof DbOOClass)
                    return classGroup;
                if (dbo instanceof DbJVCompilationUnit)
                    return compilGroup;
                if (dbo instanceof DbSMSNotice)
                    return noticeGroup;
            }
            // *OR Common Item Model
            else if (dbParent instanceof DbORCommonItemModel) {
                if (dbo instanceof DbORCommonItem)
                    return commonItemGroup;
                if (dbo instanceof DbSMSNotice)
                    return noticeGroup3;
            }
            // *OR Model
            else if (dbParent instanceof DbORModel) {
                if (dbo instanceof DbORDiagram) {
                    return diagramGroup;
                }
                // **Data Model
                else if (dbParent instanceof DbORDataModel) {
                    if (dbo instanceof DbORDataModel) {
                        DbORDataModel dataModel = (DbORDataModel) dbo;
                        if (dataModel.getOperationalMode() == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                            // subModelGroup = new GroupParams(6,
                            // LocaleMgr.screen.getString("subModels"),
                            // erModelIcon);
                            return subModelGroupER;
                        else
                            return subModelGroup;// = new GroupParams(6,
                        // LocaleMgr.screen.getString("subModels"),
                        // DbORDataModel.metaClass.getIcon());
                    }
                    if (dbo instanceof DbORAbsTable) {
                        if (dbo instanceof DbORTable) {

                            DbORTable ORObj = (DbORTable) dbo;
                            DbORDataModel dataModel = (DbORDataModel) ORObj
                                    .getCompositeOfType(DbORDataModel.metaClass);

                            TerminologyUtil terminologyManager = terminologyUtil;
                            Terminology term = terminologyManager.findModelTerminology(dataModel);

                            if (true == ORObj.isIsAssociation()
                                    && dataModel.getOperationalMode() == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                                relationshipGroup.setName(term.getTerm(DbOOClass.metaClass, true));
                                return relationshipGroup;
                            } else {
                                // tableGroup = new GroupParams(3,
                                // term.getTerm(DbORTable.metaClass, true),
                                // DbORTable.metaClass.getIcon());
                                tableGroup.setName(term.getTerm(DbORTable.metaClass, true));
                                return tableGroup;
                            }

                        } else if (dbo instanceof DbORView) {
                            DbORView ORObj = (DbORView) dbo;
                            DbORDataModel dataModel = (DbORDataModel) ORObj
                                    .getCompositeOfType(DbORDataModel.metaClass);
                            TerminologyUtil terminologyManager = terminologyUtil;
                            Terminology term = terminologyManager.findModelTerminology(dataModel);
                            // viewGroup = new GroupParams(3,
                            // term.getTerm(DbORView.metaClass, true),
                            // DbORView.metaClass.getIcon());
                            viewGroup.setName(term.getTerm(DbORView.metaClass, true));
                            return viewGroup;
                        } else if (dbo instanceof DbORChoiceOrSpecialization) {
                            DbORChoiceOrSpecialization spec = (DbORChoiceOrSpecialization) dbo;
                            DbORDataModel dataModel = (DbORDataModel) spec
                                    .getCompositeOfType(DbORDataModel.metaClass);
                            TerminologyUtil terminologyManager = terminologyUtil;
                            Terminology terminology = terminologyManager.findModelTerminology(dataModel);
                            String term = terminology.getTerm(DbORChoiceOrSpecialization.metaClass,
                                    true);
                            specGroup.setName(term);
                            return specGroup;
                        }

                        return tableGroup;
                    }
                    if (dbo instanceof DbORAssociation) {
                        DbORAssociation ORObj = (DbORAssociation) dbo;
                        DbORDataModel dataModel = (DbORDataModel) ORObj
                                .getCompositeOfType(DbORDataModel.metaClass);
                        TerminologyUtil terminologyManager = terminologyUtil;
                        Terminology term = terminologyManager.findModelTerminology(dataModel);
                        if (dataModel.getOperationalMode() == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                            arcGroup.setName(term.getTerm(DbORAssociation.metaClass, true));
                            return arcGroup;
                        } else {
                            assocGroup.setName(term.getTerm(DbORAssociation.metaClass, true));
                            return assocGroup;
                        }
                    }
                    if (dbo instanceof DbSMSNotice)
                        return noticeGroup2;
                    if (dbParent instanceof DbORADataModel) {
                        if (dbo instanceof DbORASequence)
                            return sequenceGroup;
                    }
                    if (dbParent instanceof DbIBMDataModel) {
                        if (dbo instanceof DbIBMSequence)
                            return sequenceGroup2;
                    }
                }
                // **Domain Model
                else if (dbParent instanceof DbORDomainModel) {
                    // if (dbo instanceof DbORDomain)
                    // return domainGroup;
                    if (dbo instanceof DbSMSNotice)
                        return noticeGroup4;
                }
                // **Operation Library
                else if (dbParent instanceof DbOROperationLibrary) {
                    if (dbo instanceof DbORProcedure)
                        return procedureGroup;
                    if (dbo instanceof DbORAPackage)
                        return oraPackageGroup;
                }
                // **Database
                else if (dbParent instanceof DbORDatabase) {
                    if (dbo instanceof DbORAbsTable)
                        return deployedTablesGroup;
                    /*
                     * else if (dbo instanceof DbORAssociation) return deployedAssociationsGroup;
                     */
                    else if (dbo instanceof DbORDomain) {
                        if (dbo.getComposite() == ((DbORDatabase) dbParent).getUdtModel())
                            return deployedTypesGroup;
                        return defaultDomainsGroup;
                    } else if (dbo instanceof DbORProcedure)
                        return deployedProceduresGroup;
                    // ***Oracle
                    if (dbParent instanceof DbORADatabase) {
                        if (dbo instanceof DbORARedoLogGroup)
                            return redoLogFilGroup;
                        if (dbo instanceof DbORARollbackSegment)
                            return rollbackGroup;
                        if (dbo instanceof DbORATablespace)
                            return tablespaceGroup;
                        if (dbo instanceof DbORASequence)
                            return deployedSequencesGroup;
                        if (dbo instanceof DbORAPackage)
                            return deployedORAPackagesGroup;
                    }
                    // ***Informix
                    else if (dbParent instanceof DbINFDatabase) {
                        if (dbo instanceof DbINFDbspace)
                            return dbspaceGroup;
                        if (dbo instanceof DbINFSbspace)
                            return sbspaceGroup;
                    }
                    // ***DB2 UDB
                    else if (dbParent instanceof DbIBMDatabase) {
                        if (dbo instanceof DbIBMDbPartitionGroup)
                            return ibmDbPartGroup;
                        if (dbo instanceof DbIBMBufferPool)
                            return ibmBufPoolGroup;
                        if (dbo instanceof DbIBMTablespace)
                            return ibmTablespaceGroup;
                    }
                }
            }

            // *
            else if (dbParent instanceof DbSMSUmlExtensibility) {
                if (dbo instanceof DbSMSStereotype)
                    return stereotypeGroup;
                if (dbo instanceof DbSMSUmlConstraint)
                    return umlConstraintGroup;
            } else if (dbParent instanceof DbBEModel) {
                if (dbo instanceof DbBEActor) {
                    Terminology terminology = terminologyUtil.findModelTerminology(dbo
                            .getComposite());
                    Icon actorIcon = terminology.getIcon(DbBEActorGo.metaClass);
                    String term = terminology.getTerm(DbBEActor.metaClass, true);
                    return new GroupParams(4, term, actorIcon, true);
                }
                if (dbo instanceof DbBEQualifier)
                    return qualifierGroup;
                if (dbo instanceof DbBEResource)
                    return resourceGroup;
                if (dbo instanceof DbBEStore) {
                    Terminology terminology = terminologyUtil.findModelTerminology(dbo
                            .getComposite());
                    Icon storeIcon = terminology.getIcon(DbBEStoreGo.metaClass);
                    String term = terminology.getTerm(DbBEStore.metaClass, true);
                    return new GroupParams(5, term, storeIcon, true);
                }
                if (dbo instanceof DbBEUseCase)
                    return contextGroup;
                if (dbo instanceof DbSMSNotice)
                    return noticeGroup4;
            } else { // for any other package type, insure diagrams appear first
                if (dbo instanceof DbSMSDiagram)
                    return diagramGroup;
            }
        } else if (dbParent instanceof DbOOClass) {
            if (dbo instanceof DbOODataMember)
                return fieldGroup;
            if (dbo instanceof DbOOOperation)
                return operationGroup;
            if (dbo instanceof DbOOClass)
                return innerClassGroup;
        } else if (dbParent instanceof DbORAbsTable) {
            if (dbo instanceof DbORColumn) {
                DbORColumn ORObj = (DbORColumn) dbo;
                DbORDataModel dataModel = (DbORDataModel) ORObj
                        .getCompositeOfType(DbORDataModel.metaClass);
                TerminologyUtil terminologyManager = terminologyUtil;
                Terminology term = terminologyManager.findModelTerminology(dataModel);
                columnGroup = new GroupParams(1, term.getTerm(DbORColumn.metaClass, true),
                        DbORColumn.metaClass.getIcon());
                return columnGroup;
            }
            if (dbo instanceof DbORPrimaryUnique) {
            	DbORPrimaryUnique pu = (DbORPrimaryUnique)dbo; 
            	GroupParams group = pu.isPrimary() ? primaryGroup : uniqueGroup;
            	DbORDataModel dataModel = (DbORDataModel)pu.getCompositeOfType(DbORDataModel.metaClass);
            	Terminology terms = terminologyUtil.findModelTerminology(dataModel);
            	int idx = group.index; 
            	String name = terms.getTerm(DbORPrimaryUnique.metaClass, true);
            	Icon icon = group.icon;
            	group = new GroupParams(idx, name, icon);
                return group;
            }
            if (dbo instanceof DbORForeign)
                return foreignGroup;
            if (dbo instanceof DbORIndex)
                return indexGroup;
            if (dbo instanceof DbORCheck) {
                DbORCheck ORObj = (DbORCheck) dbo;
                DbORDataModel dataModel = (DbORDataModel) ORObj
                        .getCompositeOfType(DbORDataModel.metaClass);
                Terminology term = terminologyUtil.findModelTerminology(dataModel);
                checkGroup = new GroupParams(1, term.getTerm(DbORCheck.metaClass, true),
                        DbORCheck.metaClass.getIcon());
                return checkGroup;
            }
            if (dbo instanceof DbORTrigger) {
            	DbORDataModel dataModel = (DbORDataModel)dbo.getCompositeOfType(DbORDataModel.metaClass);
            	Terminology terms = terminologyUtil.findModelTerminology(dataModel);
            	int idx = triggerGroup.index;
            	String name = terms.getTerm(DbORTrigger.metaClass, true);
            	Icon icon = triggerGroup.icon;
            	triggerGroup = new GroupParams(idx, name, icon);
                return triggerGroup;
            }
            
            // Oracle
            if (dbParent instanceof DbORAAbsTable) {
                if (dbo instanceof DbORALobStorage)
                    return lobStorageGroup;
                if (dbo instanceof DbORAPartition)
                    return partitionGroup;
                if (dbo instanceof DbORANestedTableStorage)
                    return storageTabGroup;
            }
        } else if (dbParent instanceof DbBEUseCase) {
            if (dbo instanceof DbBEUseCase)
                return subUseCaseGroup;
            if (dbo instanceof DbBEFlow) {
                // return flowGroup;
                Terminology terminology = terminologyUtil.findModelTerminology(dbo.getComposite());
                String term = terminology.getTerm(DbBEFlow.metaClass, true);
                Icon flowsIcon = terminology.getIcon(DbBEFlow.metaClass);
                return new GroupParams(9, term, flowsIcon);
            }
            if (dbo instanceof DbBEDiagram)
                return diagramGroup;
            if (dbo instanceof DbBEActor) {
                Terminology terminology = terminologyUtil.findModelTerminology(dbo.getComposite());
                Icon actorIcon = terminology.getIcon(DbBEActorGo.metaClass);
                String term = terminology.getTerm(DbBEActor.metaClass, true);
                return new GroupParams(11, MessageFormat.format(kUsed0, new Object[] { term }),
                        actorIcon, true);
            }
            if (dbo instanceof DbBEStore) {
                Terminology terminology = terminologyUtil.findModelTerminology(dbo.getComposite());
                Icon storeIcon = terminology.getIcon(DbBEStoreGo.metaClass);
                String term = terminology.getTerm(DbBEStore.metaClass, true);
                return new GroupParams(12, MessageFormat.format(kUsed0, new Object[] { term }),
                        storeIcon, true);
            }
            if (dbo instanceof DbSMSNotice)
                return noticeGroup4;
        } else if (dbParent instanceof DbBEFlow) {
            if (dbo instanceof DbSMSClassifier)
                return flowEndsGroup;
        }
        /*
         * else if (dbParent instanceof DbORIndex) { if (dbo instanceof DbORIndexKey) return
         * indexKeyGroup; }
         */
        else if (dbParent instanceof DbORDomain) {
            if (dbo instanceof DbORField)
                return orFieldGroup;
        }
        /*
         * else if (dbParent instanceof DbSMSBehavioralFeature) { if (dbo instanceof DbSMSParameter)
         * return parameterGroup; if (dbo instanceof DbORAProcedure) return procedureGroup; }
         */

        // Oracle
        else if (dbParent instanceof DbORAPartition) {
            if (dbo instanceof DbORASubPartition)
                return subPartGroup;
            if (dbo instanceof DbORALobStorage)
                return lobStorageGroup;
        } else if (dbParent instanceof DbORASubPartition) {
            if (dbo instanceof DbORALobStorage)
                return lobStorageGroup;
        } else if (dbParent instanceof DbORAIndex) {
            if (dbo instanceof DbORAPartition)
                return partitionGroup;
        }

        return GroupParams.defaultGroupParams;
    }

    protected final boolean isLeaf(DbObject dbParent, DbObject dbo) throws DbException {
        boolean isLeaf = (/* dbo instanceof DbSMSDiagram || */dbo instanceof DbSMSLink
                || dbo instanceof DbSMSStereotype || dbo instanceof DbSMSUmlConstraint
                || dbo instanceof DbOODataMember || dbo instanceof DbOOParameter
                || dbo instanceof DbJVCompilationUnit || dbo instanceof DbORAttribute
                || dbo instanceof DbORParameter || dbo instanceof DbORIndexKey
                || dbo instanceof DbORFKeyColumn || dbo instanceof DbORCheck
                || dbo instanceof DbBEQualifier || dbo instanceof DbBEResource
                || dbo instanceof DbBEActor || dbo instanceof DbBEStore
                || dbo instanceof DbORBuiltInType || dbo instanceof DbJVPrimitiveType
                || dbo instanceof DbJVInitBlock || dbo instanceof DbORUser
                || dbo instanceof DbORASequence || dbo instanceof DbORAssociationEnd
                || dbo instanceof DbORTrigger || dbo instanceof DbORASubPartition
                || dbo instanceof DbORALobStorage || dbo instanceof DbORAPackage
                || dbo instanceof DbORARedoLogFile || dbo instanceof DbORARollbackSegment
                || dbo instanceof DbORADataFile || dbo instanceof DbORCommonItem
                || dbo instanceof DbINFDbspace || dbo instanceof DbINFSbspace);

        return isLeaf;
    } // end idLeaf()

    // Overridden
    protected boolean isContainerRoot(Object object) {
        return object instanceof DbSMSPackage;
    }

    protected final boolean childrenAreSorted(DbObject dbo) throws DbException {
        return !(dbo instanceof DbOOAbstractMethod || dbo instanceof DbORAbstractMethod
                || dbo instanceof DbORPrimaryUnique || dbo instanceof DbORForeign
                || dbo instanceof DbORIndex || dbo instanceof DbGEProcedure
                || dbo instanceof DbIBMProcedure || dbo instanceof DbINFProcedure
                || dbo instanceof DbORAProcedure || dbo instanceof DbBEFlow);
    }

    /**
     * 
     * @param parent
     * @return the Comparator object to use for sorting this parent's children or null for using
     *         default Comparator
     * @throws DbException
     */
    protected Comparator<DynamicNode> getComparator(DbObject parent) throws DbException {
        if (parent == null)
            return null;
        if (parent instanceof DbBEModel || parent instanceof DbBEUseCase) {
            // we must use numericId comparator instead of default (use name)
            return numericIdComparator;
        }
        return null;
    }

    /*
     * Specifies an action to activate on double click. Beware that it must not conflict with the
     * default double click behavior of JTree (expand, collapse). Recommanded to avoid returning an
     * action for a 'not leaf' object.
     */
    protected final AbstractApplicationAction getDefaultAction(DbObject dbo) throws DbException {
        if (dbo instanceof DbSMSDiagram) {
            return SMSActionsStore.getSingleton().getAction(SMSActionsStore.SHOW_DIAGRAM);
        }
        return null;
    }

    /*
     * Called for parents displaying their children in sequence order (parents for which
     * childrenAreSorted returns false).
     */
    protected final int getIndex(DbObject dbParent, DbObject dbo) throws DbException {
        if (dbParent instanceof DbORDatabase && dbo.getComposite() instanceof DbORModel) {
            DbORModel model = (DbORModel) dbo.getComposite();
            if (model != null) {
                return model.getComponents().indexOf(dbo);
            }
        }
        MetaRelationN metaRelN = DbObject.fComponents;
        if (dbParent instanceof DbORPrimaryUnique)
            metaRelN = DbORPrimaryUnique.fColumns;
        return ((DbRelationN) dbParent.get(metaRelN)).indexOf(dbo);
    }

    protected final void insertComponents(SrVector children, DbObject dbParent) throws DbException {
        if (dbParent instanceof DbORDatabase && (((DbORDatabase) dbParent).getSchema() != null)) {
            DbEnumeration dbEnum = ((DbORDatabase) dbParent).getSchema().getComponents().elements();
            while (dbEnum.hasMoreElements()) {
                DbObject dbo = dbEnum.nextElement();
                if (dbo instanceof DbSMSPackage || dbo instanceof DbORAssociation
                        || !(dbo instanceof DbSemanticalObject))
                    continue;
                DynamicNode childNode = createSecondaryNode(dbParent, dbo);
                if (childNode == null)
                    continue;
                children.addElement(childNode);
            }
            dbEnum.close();
        }
        if (dbParent instanceof DbORDatabase
                && (((DbORDatabase) dbParent).getOperationLibrary() != null)) {
            DbEnumeration dbEnum = ((DbORDatabase) dbParent).getOperationLibrary().getComponents()
                    .elements();
            while (dbEnum.hasMoreElements()) {
                DbObject dbo = dbEnum.nextElement();
                if (dbo instanceof DbSMSPackage || !(dbo instanceof DbSemanticalObject))
                    continue;
                DynamicNode childNode = createSecondaryNode(dbParent, dbo);
                if (childNode == null)
                    continue;
                children.addElement(childNode);
            }
            dbEnum.close();
        }
        if (dbParent instanceof DbORDatabase && (((DbORDatabase) dbParent).getUdtModel() != null)) {
            DbEnumeration dbEnum = ((DbORDatabase) dbParent).getUdtModel().getComponents()
                    .elements();
            while (dbEnum.hasMoreElements()) {
                DbObject dbo = dbEnum.nextElement();
                if (dbo instanceof DbORCommonItem || dbo instanceof DbSMSPackage
                        || !(dbo instanceof DbSemanticalObject))
                    continue;
                DynamicNode childNode = createSecondaryNode(dbParent, dbo);
                if (childNode == null)
                    continue;
                children.addElement(childNode);
            }
            dbEnum.close();
        }
        if (dbParent instanceof DbORDatabase
                && (((DbORDatabase) dbParent).getDefaultDomainModel() != null)) {
            DbEnumeration dbEnum = ((DbORDatabase) dbParent).getDefaultDomainModel()
                    .getComponents().elements();
            while (dbEnum.hasMoreElements()) {
                DbObject dbo = dbEnum.nextElement();
                if (dbo instanceof DbORCommonItem || dbo instanceof DbSMSPackage
                        || !(dbo instanceof DbSemanticalObject))
                    continue;
                DynamicNode childNode = createSecondaryNode(dbParent, dbo);
                if (childNode == null)
                    continue;
                children.addElement(childNode);
            }
            dbEnum.close();
        }
        if (dbParent instanceof DbBEUseCase) {
            List<DbObject> usedDbos = new ArrayList<DbObject>();
            DbEnumeration dbEnum = ((DbBEUseCase) dbParent).getComponents().elements(
                    DbBEFlow.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbBEFlow flow = (DbBEFlow) dbEnum.nextElement();
                DbObject end1 = flow.getFirstEnd();
                DbObject end2 = flow.getSecondEnd();
                if (end1 instanceof DbBEActor && !usedDbos.contains(end1))
                    usedDbos.add(end1);
                else if (end1 instanceof DbBEStore && !usedDbos.contains(end1))
                    usedDbos.add(end1);
                if (end2 instanceof DbBEActor && !usedDbos.contains(end2))
                    usedDbos.add(end2);
                else if (end2 instanceof DbBEStore && !usedDbos.contains(end2))
                    usedDbos.add(end2);
            }
            dbEnum.close();
            dbEnum = ((DbBEUseCase) dbParent).componentTree(DbBEActorGo.metaClass,
                    new MetaClass[] { DbBEUseCase.metaClass });
            while (dbEnum.hasMoreElements()) {
                DbBEActorGo actorgo = (DbBEActorGo) dbEnum.nextElement();
                DbObject actor = actorgo.getSO();
                if (!usedDbos.contains(actor))
                    usedDbos.add(actor);
            }
            dbEnum.close();
            dbEnum = ((DbBEUseCase) dbParent).componentTree(DbBEStoreGo.metaClass,
                    new MetaClass[] { DbBEUseCase.metaClass });
            while (dbEnum.hasMoreElements()) {
                DbBEStoreGo storego = (DbBEStoreGo) dbEnum.nextElement();
                DbObject store = storego.getSO();
                if (!usedDbos.contains(store))
                    usedDbos.add(store);
            }
            dbEnum.close();
            Iterator<DbObject> iter = usedDbos.iterator();
            while (iter.hasNext()) {
                DynamicNode childNode = createSecondaryNode(dbParent, iter.next());
                if (childNode == null)
                    continue;
                children.addElement(childNode);
            }
        }
        if (dbParent instanceof DbBEFlow) {
            DbBEFlow flow = (DbBEFlow) dbParent;
            if (flow.getFirstEnd() != null) {
                DynamicNode childNode = createSecondaryNode(dbParent, flow.getFirstEnd());
                if (childNode != null)
                    children.addElement(childNode);
            }
            if (flow.getSecondEnd() != null) {
                DynamicNode childNode = createSecondaryNode(dbParent, flow.getSecondEnd());
                if (childNode != null)
                    children.addElement(childNode);
            }
        }
        /*
         * if (dbParent instanceof DbBEModel && (((DbORDatabase)dbParent).getUdtModel() != null)){
         * DbEnumeration dbEnum = ((DbORDatabase)dbParent).getUdtModel().getComponents().elements();
         * while (dbEnum.hasMoreElements()) { DbObject dbo = dbEnum.nextElement(); if (dbo
         * instanceof DbORCommonItem || dbo instanceof DbSMSPackage || !(dbo instanceof
         * DbSemanticalObject)) continue; DynamicNode childNode = createSecondaryNode(dbParent,
         * dbo); if (childNode == null) continue; children.addElement(childNode);
         * //insertComponents(new SrVector(), dbo); } dbEnum.close(); }
         */

        MetaRelationN metaRelN = null;
        if (dbParent instanceof DbORPrimaryUnique)
            metaRelN = DbORPrimaryUnique.fColumns;
        else if (dbParent instanceof DbORTrigger)
            metaRelN = DbORTrigger.fColumns;

        if (metaRelN != null) {
            DbEnumeration dbEnum = ((DbRelationN) dbParent.get(metaRelN)).elements();
            while (dbEnum.hasMoreElements()) {
                DbObject dbo = dbEnum.nextElement();
                children.addElement(createSecondaryNode(dbParent, dbo));
            }
            dbEnum.close();
        } else if (dbParent instanceof DbORAssociation) {
            DbORAssociation association = (DbORAssociation) dbParent;
            SemanticalModel model = ApplicationContext.getSemanticalModel();
            DbEnumeration dbEnum = dbParent.getComponents().elements();
            boolean roleset = false;
            while (dbEnum.hasMoreElements()) {
                DbObject dbo = dbEnum.nextElement();
                if (association.isIsArc()) {
                    if (roleset)
                        continue;

                    boolean isVisible = model.isVisibleOnScreen(dbParent, dbo, SMSExplorer.class);
                    if (isVisible)
                        children.addElement(createPrimaryNode(dbParent, association.getArcEnd()));
                    roleset = true;
                } else {
                    boolean isVisible = model.isVisibleOnScreen(dbParent, dbo, SMSExplorer.class);
                    if (isVisible)
                        children.addElement(createPrimaryNode(dbParent, dbo));
                }
            }
            dbEnum.close();
        } else
            super.insertComponents(children, dbParent);
    }

    public final void installDbListeners(boolean install) {
        MetaField[] metaFields = new MetaField[] { DbObject.fComponents, DbSemanticalObject.fName,
                DbSMSDiagram.fName, DbJVClass.fStereotype, DbOOAssociationEnd.fNavigable,
                DbORPrimaryUnique.fColumns, DbORPrimaryUnique.fPrimary, DbORTrigger.fColumns,
                DbORIndexKey.fIndexedElement, DbORAttribute.fType, DbOODataMember.fType,
                DbORDomain.fSourceType, DbORDomain.fCategory, DbORCommonItem.fType,
                DbORParameter.fType, DbOOParameter.fType, DbORDataModel.fDeploymentDatabase,
                DbORDomainModel.fDeploymentDatabase, DbOROperationLibrary.fDeploymentDatabase,
                DbORProcedure.fFunction, DbORDatabase.fSchema, DbORDatabase.fOperationLibrary,
                DbORDatabase.fUdtModel, DbORDatabase.fDefaultDomainModel, DbINFSbspace.fBlobSpace,
                DbBEQualifier.fIcon, DbBEFlow.fArrowFirstEnd, DbBEFlow.fArrowSecondEnd,
                DbBEFlow.fIdentifier, DbBEFlow.fFirstEnd, DbBEFlow.fSecondEnd,
                DbBEUseCase.fAlphanumericIdentifier, DbBEUseCase.fNumericIdentifier,
                DbBEUseCase.fExternal, DbBEUseCase.fSourceAlphanumericIdentifier,
                DbBEUseCase.fSourceNumericIdentifier };

        super.installDbListeners(install);
        if (install)
            MetaField.addDbRefreshListener(explorerRefreshListener, null, metaFields);
        else
            MetaField.removeDbRefreshListener(explorerRefreshListener, metaFields);
    }

    private class ExplorerRefreshListener implements DbRefreshListener {
        ExplorerRefreshListener() {
        }

        public final void refreshAfterDbUpdate(DbUpdateEvent evt) throws DbException {
            if (evt.metaField == DbObject.fComponents || evt.metaField == DbSemanticalObject.fName) { // test
                // first, it
                // is by far
                // the most
                // frequent
                if (evt.metaField == DbObject.fComponents
                        && (evt.dbo instanceof DbORProcedure || evt.dbo instanceof DbOOAbstractMethod))
                    updateNode(evt.dbo); // update method signature

                evt.dbo.getDb().beginReadTrans();
                DbORModel model = (evt.dbo instanceof DbORModel) ? (DbORModel) evt.dbo
                        : (DbORModel) evt.dbo.getCompositeOfType(DbORModel.metaClass);
                evt.dbo.getDb().commitTrans();
                if (model != null) {
                    DbORDatabase database = null;
                    if (model instanceof DbORDataModel) {
                        if (((DbORDataModel) model).getDeploymentDatabase() != null) {
                            database = ((DbORDataModel) model).getDeploymentDatabase();
                        }
                    } else if (model instanceof DbOROperationLibrary
                            && ((DbOROperationLibrary) model).getDeploymentDatabase() != null) {
                        database = ((DbOROperationLibrary) model).getDeploymentDatabase();
                    } else if (model instanceof DbORDomainModel
                            && ((DbORDomainModel) model).getDeploymentDatabase() != null) {
                        database = ((DbORDomainModel) model).getDeploymentDatabase();
                    }
                    if (database != null) {
                        DynamicNode node = getDynamicNode(database, false);
                        if (node != null) {
                            if (evt.metaField == DbObject.fComponents) {
                                refresh(node);
                            } else { // rename
                                refresh(node);
                            }
                        }
                    }
                    if (model instanceof DbORDomainModel) {
                        DbRelationN dbdefaultDomains = ((DbORDomainModel) model)
                                .getDatabasesDefaultDomains();
                        if (dbdefaultDomains != null && dbdefaultDomains.size() > 0) {
                            DbEnumeration dbEnum = dbdefaultDomains.elements();
                            while (dbEnum.hasMoreElements()) {
                                database = (DbORDatabase) dbEnum.nextElement();
                                DynamicNode node = getDynamicNode(database, false);
                                if (node == null)
                                    continue;
                                if (evt.metaField == DbObject.fComponents) {
                                    refresh(node);
                                } else { // rename
                                    refresh(node);
                                }
                            }
                            dbEnum.close();
                        }
                    }
                    if (evt.metaField == DbSemanticalObject.fName
                            && evt.dbo instanceof DbORDatabase) {
                        DbORDatabase db = (DbORDatabase) evt.dbo;
                        if (db.getSchema() != null) {
                            updateNode(db.getSchema());
                        }
                        if (db.getOperationLibrary() != null) {
                            updateNode(db.getOperationLibrary());
                        }
                        if (db.getUdtModel() != null) {
                            updateNode(db.getUdtModel());
                        }
                    }
                    return;
                }
            } else if (evt.metaField == DbSMSDiagram.fName
                    || evt.metaField == DbJVClass.fStereotype) {
                updateNode(evt.dbo);
            } else if (evt.metaField == DbOOAssociationEnd.fNavigable) {
                DbOOAssociationEnd assocEnd = (DbOOAssociationEnd) evt.dbo;
                DbOODataMember member = assocEnd.getAssociationMember();
                // getDynamicNode adds the node if it becomes navigable.
                DynamicNode node = getDynamicNode(member, false);
                if (node != null && !assocEnd.isNavigable())
                    removeNode(node);
            } else if (evt.metaField == DbORPrimaryUnique.fColumns
                    || evt.metaField == DbORTrigger.fColumns) {
                updateSecondaryChildren(evt);
            } else if (evt.metaField == DbORAttribute.fType
                    || evt.metaField == DbOODataMember.fType
                    || evt.metaField == DbORDomain.fSourceType
                    || evt.metaField == DbORCommonItem.fType) {
                updateNode(evt.dbo);
            } else if (evt.metaField == DbORParameter.fType || evt.metaField == DbOOParameter.fType) {
                updateNode(evt.dbo);
                updateNode(evt.dbo.getComposite()); // update method signature
            } else if (evt.metaField == DbORIndexKey.fIndexedElement) {
                updateNode(evt.dbo);
            } else if (evt.metaField == DbORPrimaryUnique.fPrimary) {
                updateNode(evt.dbo);
            } else if (evt.metaField == DbORProcedure.fFunction) {
                updateNode(evt.dbo);
            } else if (evt.metaField == DbINFSbspace.fBlobSpace) {
                updateNode(evt.dbo);
            } else if (evt.metaField == DbORDataModel.fDeploymentDatabase) {
                updateNode(evt.dbo);
            } else if (evt.metaField == DbOROperationLibrary.fDeploymentDatabase) {
                updateNode(evt.dbo);
            } else if (evt.metaField == DbORDomainModel.fDeploymentDatabase) {
                updateNode(evt.dbo);
            } else if (evt.metaField == DbORDatabase.fSchema) {
                DynamicNode node = getDynamicNode(evt.dbo, false);
                refresh(node);
            } else if (evt.metaField == DbORDatabase.fOperationLibrary) {
                DynamicNode node = getDynamicNode(evt.dbo, false);
                refresh(node);
            } else if (evt.metaField == DbORDatabase.fUdtModel) {
                DynamicNode node = getDynamicNode(evt.dbo, false);
                refresh(node);
            } else if (evt.metaField == DbORDatabase.fDefaultDomainModel) {
                DynamicNode node = getDynamicNode(evt.dbo, false);
                refresh(node);
            } else if (evt.metaField == DbORIndexKey.fIndexedElement) {
                updateNode(evt.dbo);
            } else if (evt.metaField == DbORPrimaryUnique.fPrimary) {
                updateNode(evt.dbo);
            } else if (evt.metaField == DbBEQualifier.fIcon) {
                updateNode(evt.dbo);
            } else if (evt.metaField == DbBEFlow.fArrowFirstEnd
                    || evt.metaField == DbBEFlow.fArrowSecondEnd
                    || evt.metaField == DbBEFlow.fIdentifier) {
                updateNode(evt.dbo);
            } else if (evt.metaField == DbBEFlow.fFirstEnd || evt.metaField == DbBEFlow.fSecondEnd) {
                DynamicNode node = getDynamicNode(evt.dbo, false);
                refresh(node);
            } else if (evt.metaField == DbBEUseCase.fExternal
                    || evt.metaField == DbBEUseCase.fSourceAlphanumericIdentifier
                    || evt.metaField == DbBEUseCase.fSourceNumericIdentifier) {
                updateNode(evt.dbo);
            } else if (evt.metaField == DbBEUseCase.fAlphanumericIdentifier
                    || evt.metaField == DbBEUseCase.fNumericIdentifier) {
                updateNode(evt.dbo);
                DynamicNode node = getDynamicNode(evt.dbo, false);
                refresh(node);
            } else if (evt.metaField == DbORDomain.fCategory) {
                updateNode(evt.dbo);
            }
        }
    }

    // The value type for the returned MetaField MUST be String.
    protected MetaField getEditableMetaField(DbObject dbo) {
        if (dbo instanceof DbBEUseCase)
            return DbBEUseCase.fAlphanumericIdentifier;
        return super.getEditableMetaField(dbo);
    }

    protected Icon getIcon(DbObject dbo) throws DbException {
        Icon icon = null;

        if (dbo instanceof DbBEFlow) {
            // determine icon based on arrow direction
            DbBEFlow flow = (DbBEFlow) dbo;
            //DbObject first = flow.getFirstEnd();
            //DbObject second = flow.getSecondEnd();

            icon = null;
            if (flow.isArrowFirstEnd() && flow.isArrowSecondEnd())
                icon = flowBiDirImage;
            else if (flow.isArrowFirstEnd() || flow.isArrowSecondEnd())
                icon = flowRigthImage;
            else
                icon = flowLineImage;
        } else if (dbo instanceof DbBEQualifier) {
            Image image = ((DbBEQualifier) dbo).getIcon();
            icon = (image == null) ? AwtUtil.NULL_ICON : new ImageIcon(image) {
                private int iconMaxHeight = 16;
                int w = 0;
                int h = 0;
                {
                    Icon treeicon = UIManager.getIcon("Tree.leafIcon");
                    if (treeicon != null) {
                        iconMaxHeight = Math.max(treeicon.getIconHeight(), iconMaxHeight);
                    }
                }

                public int getIconWidth() {
                    w = super.getIconWidth();
                    w = Math.min(w, 50);
                    return w;
                }

                public int getIconHeight() {
                    h = super.getIconHeight();
                    h = Math.min(h, iconMaxHeight);
                    return h;
                }

                public void paintIcon(Component c, Graphics g, int x, int y) {
                    Rectangle oldclip = g.getClipBounds();
                    g.setClip(x, y, w, h);
                    super.paintIcon(c, g, x, y);
                    g.setClip(oldclip.x, oldclip.y, oldclip.width, oldclip.height);
                }
            };
        } else if (dbo instanceof DbBEUseCase) {
            DbBEUseCase useCase = ((DbBEUseCase) dbo);
            if (useCase.isExternal()) {

                Terminology terminology = terminologyUtil.findModelTerminology(dbo.getComposite());
                if (terminology != null)
                    icon = terminology.getIcon(DbBEUseCaseResource.metaClass);
                if (icon == null)
                    icon = beExternalUseCaseImage;

            } else if (useCase.isContext()) {
                Terminology terminology = terminologyUtil.findModelTerminology(dbo.getComposite());
                if (terminology != null)
                    icon = terminology.getIcon(DbBEUseCaseGo.metaClass);
            } else {
                boolean displayStereotypedIcon = false;
                DbSMSStereotype stereotype = (DbSMSStereotype) useCase.getUmlStereotype();
                if (stereotype != null) {
                    DbObject objComp = useCase.getComposite();
                    if (objComp != null) {
                        Terminology currentTerminology = terminologyUtil
                                .findModelTerminology(objComp);
                        Terminology termState = Terminology
                                .findTerminologyByName(DbBENotation.UML_STATE_DIAGRAM);
                        Terminology termActivity = Terminology
                                .findTerminologyByName(DbInitialization.UML_ACTIVITY_DIAGRAM_TXT);
                        if (currentTerminology.equals(termState)
                                || currentTerminology.equals(termActivity))
                            displayStereotypedIcon = true;
                    }
                    if (displayStereotypedIcon) {
                        Image img = stereotype.getIcon();
                        if (img != null)
                            icon = new ImageIcon(stereotype.getIcon().getScaledInstance(14, 14, 0));
                        else
                            icon = terminologyUtil.findModelTerminology(dbo.getComposite())
                                    .getIcon(DbBEUseCase.metaClass);
                    }
                }
                if (displayStereotypedIcon == false)
                    icon = terminologyUtil.findModelTerminology(dbo.getComposite()).getIcon(
                            DbBEUseCase.metaClass);
            }
        } else if (dbo instanceof DbBEStore) {
            Terminology terminology = terminologyUtil.findModelTerminology(dbo);
            if (terminology != null)
                icon = terminology.getIcon(DbBEStore.metaClass);
        } else if (dbo instanceof DbBEModel) {
            Terminology terminology = terminologyUtil.findModelTerminology(dbo);
            if (terminology != null)
                icon = terminology.getIcon(DbBEModel.metaClass);
        } else if (dbo instanceof DbBEActor) {
            Terminology terminology = terminologyUtil.findModelTerminology(dbo);
            if (terminology != null)
                icon = terminology.getIcon(DbBEActor.metaClass);
        } else if (dbo instanceof DbORDomainModel) {
            if (((DbORDomainModel) dbo).getDeploymentDatabase() != null) {
                icon = udtModelImage;
            }
        } else if (dbo instanceof DbORDataModel) {
            DbORDataModel dataModel = (DbORDataModel) dbo;
            if (dataModel.getOperationalMode() == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                icon = erModelIcon;
            } else
                icon = DbORDataModel.metaClass.getIcon();

        } else if (dbo instanceof DbORDomain) {
            if (((DbORDomain) dbo).getCategory() != null
                    && !((DbORDomain) dbo).getCategory().equals(
                            ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))) {
                icon = udtImage;
            } // end if
        } else if (dbo instanceof DbBEDiagram) {
            DbBEDiagram diag = (DbBEDiagram) dbo;
            DbSMSDiagram parent = diag.getParentDiagram();
            if (parent == null) {
                Terminology terminology = Terminology.findTerminologyByName(diag
                        .findMasterNotation().getName());
                if (terminology != null)
                    icon = terminology.getIcon(DbBEDiagram.metaClass);
            } else
                icon = treeDiagramImage;
        } else if (dbo instanceof DbORDiagram) {
            icon = ((DbORDiagram) dbo).getSemanticalIcon(0);
        } else if (dbo instanceof DbORTable) {
            DbORTable ORObj = (DbORTable) dbo;
            DbORDataModel dataModel = (DbORDataModel) ORObj
                    .getCompositeOfType(DbORDataModel.metaClass);
            if (true == ORObj.isIsAssociation()
                    && dataModel.getOperationalMode() == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                icon = associationTableIcon;
            }
        } else if (dbo instanceof DbORChoiceOrSpecialization) {
            DbORChoiceOrSpecialization spec = (DbORChoiceOrSpecialization) dbo;
            ORChoiceSpecializationCategory categ = (ORChoiceSpecializationCategory) spec
                    .getCategory();
            int value = (categ == null) ? 0 : categ.getValue();

            if (value == ORChoiceSpecializationCategory.SPECIALIZATION) {
                icon = specIcon;
            } else if (value == ORChoiceSpecializationCategory.CHOICE) {
                icon = choiceIcon;
            } //end if

        } else if (dbo instanceof DbORAssociation) {
            DbORAssociation ORObj = (DbORAssociation) dbo;
            DbORDataModel dataModel = (DbORDataModel) ORObj
                    .getCompositeOfType(DbORDataModel.metaClass);
            if (true == ORObj.isIsArc()
                    && dataModel.getOperationalMode() == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                icon = arcIcon;
            else
                icon = DbORAssociation.metaClass.getIcon();
        }

        if (icon == null) {
            icon = super.getIcon(dbo);
        } // end if

        if (dbo instanceof DbBEContextGo) {
        }

        if (dbo instanceof DbBEContextCell) {
        }

        if (dbo instanceof DbSMSTargetSystem) {
        }

        return icon;
    } // end getIcon()

} // end SMSExplorer
