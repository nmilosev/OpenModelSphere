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

package org.modelsphere.sms.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.Icon;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.screen.DbDescriptionView;
import org.modelsphere.jack.baseDb.screen.DbListView;
import org.modelsphere.jack.baseDb.screen.PropertiesFrame;
import org.modelsphere.jack.baseDb.screen.ScreenTabPanel;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.jack.plugins.PluginsRegistry;
import org.modelsphere.jack.srtool.SrScreenContext;
import org.modelsphere.jack.srtool.forward.JackForwardEngineeringPlugin;
import org.modelsphere.jack.srtool.forward.PropertyScreenPreviewInfo;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.SMSExplorer;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEActorQualifier;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEFlowQualifier;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEStoreQualifier;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseQualifier;
import org.modelsphere.sms.be.db.DbBEUseCaseResource;
import org.modelsphere.sms.be.screen.BEListView;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSUmlConstraint;
import org.modelsphere.sms.db.util.AnySemObject;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.DbOOClass;
import org.modelsphere.sms.oo.db.DbOOInheritance;
import org.modelsphere.sms.oo.java.db.DbJVAssociation;
import org.modelsphere.sms.oo.java.db.DbJVAssociationEnd;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVCompilationUnit;
import org.modelsphere.sms.oo.java.db.DbJVConstructor;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVImport;
import org.modelsphere.sms.oo.java.db.DbJVInheritance;
import org.modelsphere.sms.oo.java.db.DbJVInitBlock;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVParameter;
import org.modelsphere.sms.oo.java.screen.JVListView;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAbstractMethod;
import org.modelsphere.sms.or.db.DbORAllowableValue;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORCheck;
import org.modelsphere.sms.or.db.DbORChoiceOrSpecialization;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
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
import org.modelsphere.sms.or.db.DbORParameter;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORProcedure;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTrigger;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.db.srtypes.ORDomainCategory;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.or.ibm.db.DbIBMBufferPool;
import org.modelsphere.sms.or.ibm.db.DbIBMDbPartitionGroup;
import org.modelsphere.sms.or.ibm.db.DbIBMTablespace;
import org.modelsphere.sms.or.informix.db.DbINFCheck;
import org.modelsphere.sms.or.informix.db.DbINFColumn;
import org.modelsphere.sms.or.informix.db.DbINFDatabase;
import org.modelsphere.sms.or.informix.db.DbINFDbspace;
import org.modelsphere.sms.or.informix.db.DbINFForeign;
import org.modelsphere.sms.or.informix.db.DbINFFragment;
import org.modelsphere.sms.or.informix.db.DbINFIndex;
import org.modelsphere.sms.or.informix.db.DbINFPrimaryUnique;
import org.modelsphere.sms.or.informix.db.DbINFSbspace;
import org.modelsphere.sms.or.informix.db.DbINFTable;
import org.modelsphere.sms.or.oracle.db.DbORAAbsPartition;
import org.modelsphere.sms.or.oracle.db.DbORAColumn;
import org.modelsphere.sms.or.oracle.db.DbORADataFile;
import org.modelsphere.sms.or.oracle.db.DbORADatabase;
import org.modelsphere.sms.or.oracle.db.DbORAIndex;
import org.modelsphere.sms.or.oracle.db.DbORALobStorage;
import org.modelsphere.sms.or.oracle.db.DbORANestedTableStorage;
import org.modelsphere.sms.or.oracle.db.DbORAPartition;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogFile;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogGroup;
import org.modelsphere.sms.or.oracle.db.DbORARollbackSegment;
import org.modelsphere.sms.or.oracle.db.DbORASequence;
import org.modelsphere.sms.or.oracle.db.DbORASubPartition;
import org.modelsphere.sms.or.oracle.db.DbORATable;
import org.modelsphere.sms.or.oracle.db.DbORATablespace;
import org.modelsphere.sms.or.oracle.db.DbORAView;
import org.modelsphere.sms.or.screen.ORListView;
import org.modelsphere.sms.plugins.ForwardPanel;

@SuppressWarnings("serial")
public class SMSPropertiesFrame extends PropertiesFrame {

    public static final int TYPE_TARGET_SYSTEM = 10;

    private static TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    // Used by SMSDescriptionModel to identify the <Links? tab.
    public static final MetaRelationN[] linksTabRelations = new MetaRelationN[] {
            DbSMSSemanticalObject.fSourceLinks, DbSMSSemanticalObject.fTargetLinks };

    private static final String kUsedBy = LocaleMgr.screen.getString("usedBy");

    public SMSPropertiesFrame(DbObject semObj) throws DbException {
        super(SrScreenContext.singleton, semObj, SMSPropertiesFrame.getScreenPanels(semObj));
    }

    private static ScreenTabPanel[] getScreenPanels(DbObject semObj) throws DbException {
        Vector vecPanels = new Vector(10);
        vecPanels.add(new DbDescriptionView(SrScreenContext.singleton, semObj));
        MetaClass[] metaClasses = AnyORObject.getTargetMetaClasses(semObj);
        DbListView listView;

        boolean bShowMarkedFields = TerminologyUtil
                .getShowPhysicalConcepts(new DbObject[] { semObj });

        /* Generic */
        if (semObj instanceof DbSMSStereotype) {
            listView = new SMSListView(semObj, DbSMSStereotype.fSuperStereotypes,
                    DbSMSStereotype.metaClass, ScreenView.LINK_UNL_ACTIONS | ScreenView.NAME_ONLY);
            vecPanels.add(listView);
            listView = new SMSListView(semObj, DbSMSStereotype.fSubStereotypes,
                    DbSMSStereotype.metaClass, ScreenView.PROPERTIES_BTN | ScreenView.NAME_ONLY);
            vecPanels.add(listView);
            listView = new SMSListView(semObj, DbSMSStereotype.fStereotypedObjects,
                    DbSMSSemanticalObject.metaClass, ScreenView.PROPERTIES_BTN
                            | ScreenView.NAME_ONLY);
            vecPanels.add(listView);
        }

        if (semObj instanceof DbSMSUmlConstraint) {
            listView = new SMSListView(semObj, DbSMSUmlConstraint.fConstrainedObjects,
                    DbSMSSemanticalObject.metaClass, ScreenView.PROPERTIES_BTN
                            | ScreenView.NAME_ONLY);
            vecPanels.add(listView);
        }

        /** OR Frames */
        else if (semObj instanceof DbORAssociation) {
        	
        	//Roles
        	listView = new ORListView(semObj, DbORAssociation.fComponents,
                    DbORAssociationEnd.metaClass, ScreenView.PROPERTIES_BTN
                            | ScreenView.APPLY_ACTION); 
            vecPanels.add(listView);
        	
        	//Sub Associations (only for full versions)
            if (ScreenPerspective.isFullVersion()) {
            	listView = new ORListView(semObj, DbORAssociation.fSubCopies,
                        DbORAssociation.metaClass, ScreenView.UNLINK_BTN
                                | ScreenView.PROPERTIES_BTN | ScreenView.SORTED_LIST
                                | ScreenView.NAME_ONLY | ScreenView.FULL_NAME); 
            	vecPanels.add(listView);
            	//listView.setTabName(LocaleMgr.screen.getString("subAssociations"));
            }
        } 

        else if (semObj instanceof DbORColumn) {
            vecPanels
                    .add(listView = new ORListView(semObj, DbORColumn.fSubCopies,
                            DbORColumn.metaClass, ScreenView.UNLINK_BTN | ScreenView.PROPERTIES_BTN
                                    | ScreenView.SORTED_LIST | ScreenView.NAME_ONLY
                                    | ScreenView.FULL_NAME));
            //listView.setTabName(LocaleMgr.screen.getString("subColumns"));

            /** Informix */
            if (semObj instanceof DbINFColumn) {
                vecPanels.add(listView = new ORListView(semObj, DbINFColumn.fSbspaces,
                        DbINFSbspace.metaClass, ScreenView.LINK_UNL_ACTIONS
                                | ScreenView.REINSERT_ACTION | ScreenView.NAME_ONLY));
                //listView.setTabName(LocaleMgr.screen.getString("SbspaceList"));
            }
        }

        else if (semObj instanceof DbORCommonItem) {
            vecPanels.add(listView = new ORListView(semObj, DbORCommonItem.fColumns,
                    DbORColumn.metaClass, ScreenView.LINK_UNL_ACTIONS | ScreenView.SORTED_LIST
                            | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));

            vecPanels.add(listView = new ORListView(semObj, DbORCommonItem.fFields,
                    DbJVDataMember.metaClass, ScreenView.LINK_UNL_ACTIONS | ScreenView.SORTED_LIST
                            | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
        }

        else if (semObj instanceof DbORConstraint) {
            DbObject dbo = semObj.getCompositeOfType(DbORDataModel.metaClass);
            int mode = DbORDataModel.LOGICAL_MODE_OBJECT_RELATIONAL;

            if (semObj instanceof DbORPrimaryUnique) {

                //this panel only makes sense if the data model is runnign in ER mode
                //and the table that owns it is an Entity, (as opposed to an association!)
                boolean bKeepAttributesPanel = true;
                boolean bKeepDependenciesPanel = true;
                if (dbo != null) {
                    DbORDataModel dataModel = (DbORDataModel) dbo;
                    mode = dataModel.getOperationalMode();
                    if (mode == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                        DbORTable table = (DbORTable) semObj
                                .getCompositeOfType(DbORTable.metaClass);
                        if (table != null)
                            if (table.isIsAssociation() == true) {
                                bKeepAttributesPanel = false;
                                bKeepDependenciesPanel = false;
                            }
                    }
                }

                if (bKeepDependenciesPanel) {
                    vecPanels.add(listView = new ORListView(semObj,
                            DbORPrimaryUnique.fAssociationDependencies,
                            DbORAssociationEnd.metaClass, ScreenView.LINK_UNL_ACTIONS
                                    | ScreenView.REINSERT_ACTION | ScreenView.NAME_ONLY));
                }

                if (bKeepAttributesPanel) {
                    vecPanels.add(listView = new ORListView(semObj, DbORPrimaryUnique.fColumns,
                            DbORColumn.metaClass, ScreenView.LINK_UNL_ACTIONS
                                    | ScreenView.REINSERT_ACTION | ScreenView.NAME_ONLY));
                }
                vecPanels.add(listView = new ORListView(semObj, DbORPrimaryUnique.fSubCopies,
                        DbORPrimaryUnique.metaClass, ScreenView.UNLINK_BTN
                                | ScreenView.PROPERTIES_BTN | ScreenView.SORTED_LIST
                                | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));

            } else if (semObj instanceof DbORForeign) {
                vecPanels.add(listView = new ORListView(semObj, DbORFKeyColumn.fComponents,
                        DbORFKeyColumn.metaClass, ScreenView.PROPERTIES_BTN));
                listView.setParentRel(DbORFKeyColumn.fColumn);
            } else if (semObj instanceof DbORCheck) {
                vecPanels.add(listView = new ORListView(semObj, DbORCheck.fSubCopies,
                        DbORCheck.metaClass, ScreenView.UNLINK_BTN | ScreenView.PROPERTIES_BTN
                                | ScreenView.SORTED_LIST | ScreenView.NAME_ONLY
                                | ScreenView.FULL_NAME));
            } else {
                vecPanels.add(listView = new ORListView(semObj, DbSMSSemanticalObject.fSubCopies,
                        DbSMSSemanticalObject.metaClass, ScreenView.UNLINK_BTN
                                | ScreenView.PROPERTIES_BTN | ScreenView.SORTED_LIST
                                | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
                //listView.setTabName(LocaleMgr.screen.getString("subConstraints"));
            }
        } else if (semObj instanceof DbORDomainModel) {
            vecPanels
                    .add(listView = new ORListView(semObj,
                            DbORDomainModel.fDatabasesDefaultDomains, DbORDatabase.metaClass,
                            ScreenView.LINK_UNL_ACTIONS | ScreenView.REINSERT_ACTION
                                    | ScreenView.NAME_ONLY));
        } else if (semObj instanceof DbORDatabase) {
            /*
             * vecPanels.add(listView = new ORListView(semObj, DbORDatabase.fDataModels,
             * DbORDataModel.metaClass, ScreenView.UNLINK_BTN | ScreenView.PROPERTIES_BTN |
             * ScreenView.REINSERT_ACTION | ScreenView.NAME_ONLY)); vecPanels.add(listView = new
             * ORListView(semObj, DbORDatabase.fDomainModels, DbORDomainModel.metaClass,
             * ScreenView.UNLINK_BTN | ScreenView.PROPERTIES_BTN | ScreenView.REINSERT_ACTION |
             * ScreenView.NAME_ONLY)); vecPanels.add(listView = new ORListView(semObj,
             * DbORDatabase.fOperationLibraries, DbOROperationLibrary.metaClass,
             * ScreenView.UNLINK_BTN | ScreenView.PROPERTIES_BTN | ScreenView.REINSERT_ACTION |
             * ScreenView.NAME_ONLY));
             */

            /** Oracle */
            if (semObj instanceof DbORADatabase) {
                vecPanels.add(listView = new ORListView(semObj, DbORATablespace.metaClass));
                //listView.setTabName(LocaleMgr.screen.getString("TablespaceList"));
                vecPanels.add(listView = new ORListView(semObj, DbORARedoLogGroup.metaClass));
                //listView.setTabName(LocaleMgr.screen.getString("RedoLogGroupList"));
                vecPanels.add(listView = new ORListView(semObj, DbORARollbackSegment.metaClass));
                //listView.setTabName(LocaleMgr.screen.getString("RollbackSegmentList"));
            }
            /** Informix */
            else if (semObj instanceof DbINFDatabase) {
                vecPanels.add(listView = new ORListView(semObj, DbINFDbspace.metaClass));
                listView.setTabName(LocaleMgr.screen.getString("DbspaceList"));
                vecPanels.add(listView = new ORListView(semObj, DbINFSbspace.metaClass));
                listView.setTabName(LocaleMgr.screen.getString("SbspaceList"));
            }
        }

        /** Informix */
        else if (semObj instanceof DbINFDbspace) {
            vecPanels.add(listView = new ORListView(semObj, DbINFDbspace.fTables,
                    DbINFTable.metaClass, ScreenView.UNLINK_BTN | ScreenView.PROPERTIES_BTN
                            | ScreenView.SORTED_LIST | ScreenView.NAME_ONLY));
            //listView.setTabName(DbORTable.metaClass);
            vecPanels.add(listView = new ORListView(semObj, DbINFDbspace.fFragments,
                    DbINFFragment.metaClass, ScreenView.PROPERTIES_BTN | ScreenView.SORTED_LIST
                            | ScreenView.DISPLAY_CLASS | ScreenView.NAME_ONLY));
            listView.setParentRel(DbObject.fComposite);
            //listView.setTabName(LocaleMgr.screen.getString("FragmentList"));

        }

        else if (semObj instanceof DbORIndex) {
            vecPanels.add(listView = new ORListView(semObj, DbORIndexKey.fComponents,
                    DbORIndexKey.metaClass, ScreenView.ADD_BTN | ScreenView.DELETE_BTN
                            | ScreenView.APPLY_ACTION | ScreenView.REINSERT_ACTION));
            vecPanels
                    .add(listView = new ORListView(semObj, DbORIndex.fSubCopies,
                            DbORIndex.metaClass, ScreenView.UNLINK_BTN | ScreenView.PROPERTIES_BTN
                                    | ScreenView.SORTED_LIST | ScreenView.NAME_ONLY
                                    | ScreenView.FULL_NAME));

            /** Oracle */
            if (semObj instanceof DbORAIndex) {
                vecPanels.add(listView = new ORListView(semObj, DbORAPartition.metaClass));
                //listView.setTabName(LocaleMgr.screen.getString("PartitionList"));
                vecPanels.add(listView = new ORListView(semObj, DbORAIndex.fPartitionKeyColumns,
                        DbORAColumn.metaClass, ScreenView.LINK_UNL_ACTIONS
                                | ScreenView.REINSERT_ACTION | ScreenView.NAME_ONLY));
                //listView.setTabName(LocaleMgr.screen.getString("PartitionKeyList"));
                vecPanels.add(listView = new ORListView(semObj, DbORAIndex.fSubpartitionKeyColumns,
                        DbORAColumn.metaClass, ScreenView.LINK_UNL_ACTIONS
                                | ScreenView.REINSERT_ACTION | ScreenView.NAME_ONLY));
                //listView.setTabName(LocaleMgr.screen.getString("SubPartitionKeyList"));
            }
            /** Informix */
            else if (semObj instanceof DbINFIndex) {
                vecPanels.add(listView = new ORListView(semObj, DbINFIndex.fComponents,
                        DbINFFragment.metaClass, ScreenView.ADD_BTN | ScreenView.DELETE_BTN
                                | ScreenView.APPLY_ACTION | ScreenView.REINSERT_ACTION));
                //listView.setTabName(LocaleMgr.screen.getString("FragmentList"));
                vecPanels.add(listView = new ORListView(semObj,
                        DbINFIndex.fFragmentationKeyColumns, DbINFColumn.metaClass,
                        ScreenView.LINK_UNL_ACTIONS | ScreenView.REINSERT_ACTION
                                | ScreenView.NAME_ONLY));
                //listView.setTabName(LocaleMgr.screen.getString("FragmentationKeyList"));
            }
        }

        /** Oracle */
        else if (semObj instanceof DbORALobStorage) {
            vecPanels.add(listView = new ORListView(semObj, DbORALobStorage.fLobItems,
                    DbORAColumn.metaClass, ScreenView.LINK_UNL_ACTIONS | ScreenView.REINSERT_ACTION
                            | ScreenView.NAME_ONLY));
            //listView.setTabName(DbORColumn.metaClass);
        }

        /** Oracle */
        /*
         * Disabled for this release else if (semObj instanceof DbORAPackage) {
         * vecPanels.add(listView = new ORListView(semObj, DbORAProcedure.metaClass));
         * listView.setTabName(LocaleMgr.screen.getString("OraProcedureList")); }
         */

        /** Oracle */
        else if (semObj instanceof DbORAPartition) {
            //vecPanels.add(listView = new ORListView(semObj, DbORALobStorage.metaClass));
            //listView.setTabName(LocaleMgr.screen.getString("LOBStorageList"));
            vecPanels.add(listView = new ORListView(semObj, DbORASubPartition.metaClass));
            //listView.setTabName(LocaleMgr.screen.getString("SubPartitionList"));
        }

        else if (semObj instanceof DbORASubPartition) {
            //vecPanels.add(listView = new ORListView(semObj, DbORALobStorage.metaClass));
            //listView.setTabName(LocaleMgr.screen.getString("LOBStorageList"));
        }

        else if (semObj instanceof DbORProcedure) {
            vecPanels.add(listView = new ORListView(semObj, metaClasses[AnyORObject.I_PARAMETER]));
            listView.setTabName(DbORParameter.metaClass);
        }

        /** Oracle */
        else if (semObj instanceof DbORARedoLogGroup) {
            vecPanels.add(listView = new ORListView(semObj, DbORARedoLogFile.metaClass));
            //listView.setTabName(LocaleMgr.screen.getString("RedoLogFileList"));
        }

        /** Informix */
        else if (semObj instanceof DbINFSbspace) {
            vecPanels.add(listView = new ORListView(semObj, DbINFSbspace.fLobColumns,
                    DbINFColumn.metaClass, ScreenView.UNLINK_BTN | ScreenView.PROPERTIES_BTN
                            | ScreenView.REINSERT_ACTION | ScreenView.NAME_ONLY));
            //listView.setTabName(DbORColumn.metaClass);
        }

        else if (semObj instanceof DbORTable) {

            boolean bAssociationTable = ((DbORTable) semObj).isIsAssociation();

            vecPanels.add(listView = new ORListView(semObj, metaClasses[AnyORObject.I_COLUMN]));
            if (bAssociationTable) {
                if (bShowMarkedFields) {
                    vecPanels.add(listView = new ORListView(semObj,
                            metaClasses[AnyORObject.I_PRIMARYUNIQUE]));
                }
            } else
                vecPanels.add(listView = new ORListView(semObj,
                        metaClasses[AnyORObject.I_PRIMARYUNIQUE]));

            if (bShowMarkedFields && !bAssociationTable)
                vecPanels.add(listView = new ORListView(semObj, DbORForeign.fComponents,
                        DbORForeign.metaClass, ScreenView.DELETE_BTN | ScreenView.PROPERTIES_BTN
                                | ScreenView.APPLY_ACTION | ScreenView.REINSERT_ACTION));
            //if(bShowMarkedFields && !bAssociationTable)
            vecPanels.add(listView = new ORListView(semObj, metaClasses[AnyORObject.I_CHECK]));

            if (bShowMarkedFields && !bAssociationTable)
                vecPanels.add(listView = new ORListView(semObj, metaClasses[AnyORObject.I_INDEX]));
            if (bShowMarkedFields && !bAssociationTable)
                vecPanels
                        .add(listView = new ORListView(semObj, metaClasses[AnyORObject.I_TRIGGER]));

            if (bAssociationTable == false)
                vecPanels.add(listView = new ORListView(semObj, DbORTable.fSubCopies,
                        DbORTable.metaClass, ScreenView.UNLINK_BTN | ScreenView.PROPERTIES_BTN
                                | ScreenView.SORTED_LIST | ScreenView.NAME_ONLY
                                | ScreenView.FULL_NAME));
            else
                vecPanels.add(listView = new ORListView(semObj, DbOOClass.fSubCopies,
                        DbOOClass.metaClass, ScreenView.UNLINK_BTN | ScreenView.PROPERTIES_BTN
                                | ScreenView.SORTED_LIST | ScreenView.NAME_ONLY
                                | ScreenView.FULL_NAME));

            /** Oracle */
            if (semObj instanceof DbORATable) {
                vecPanels.add(listView = new ORListView(semObj, DbORANestedTableStorage.metaClass));
                //listView.setTabName(LocaleMgr.screen.getString("NestedTableList"));
                vecPanels.add(listView = new ORListView(semObj, DbORALobStorage.metaClass));
                //listView.setTabName(LocaleMgr.screen.getString("LOBStorageList"));
                vecPanels.add(listView = new ORListView(semObj, DbORAPartition.metaClass));
                //listView.setTabName(LocaleMgr.screen.getString("PartitionList"));
                vecPanels.add(listView = new ORListView(semObj, DbORATable.fPartitionKeyColumns,
                        DbORAColumn.metaClass, ScreenView.LINK_UNL_ACTIONS
                                | ScreenView.REINSERT_ACTION | ScreenView.NAME_ONLY));
                //listView.setTabName(LocaleMgr.screen.getString("PartitionKeyList"));
                vecPanels.add(listView = new ORListView(semObj, DbORATable.fSubpartitionKeyColumns,
                        DbORAColumn.metaClass, ScreenView.LINK_UNL_ACTIONS
                                | ScreenView.REINSERT_ACTION | ScreenView.NAME_ONLY));
                //listView.setTabName(LocaleMgr.screen.getString("SubPartitionKeyList"));
                vecPanels.add(listView = new ORListView(semObj, DbORATable.fTablespaces,
                        DbORATablespace.metaClass, ScreenView.LINK_UNL_ACTIONS
                                | ScreenView.REINSERT_ACTION | ScreenView.NAME_ONLY));
                //listView.setTabName(LocaleMgr.screen.getString("TablespaceList"));
            }
            /*
             * réévaluer la problématique avec Steph. int action = (semObj instanceof
             * DbORANestedTableStorage ? ScreenView.PROPERTIES_BTN | ScreenView.NAME_ONLY :
             * ScreenView.LINK_UNL_ACTIONS | ScreenView.REINSERT_ACTION | ScreenView.NAME_ONLY);
             * vecPanels.add(listView = new ORListView(semObj, DbORAAbsTable.fTablespaces,
             * DbORATablespace.metaClass, action));
             */
            /** Informix */
            else if (semObj instanceof DbINFTable) {
                vecPanels.add(listView = new ORListView(semObj, DbObject.fComponents,
                        DbINFFragment.metaClass, ScreenView.ADD_BTN | ScreenView.DELETE_BTN
                                | ScreenView.APPLY_ACTION | ScreenView.REINSERT_ACTION));
                //listView.setTabName(LocaleMgr.screen.getString("FragmentList"));
                vecPanels.add(listView = new ORListView(semObj,
                        DbINFTable.fFragmentationKeyColumns, DbINFColumn.metaClass,
                        ScreenView.LINK_UNL_ACTIONS | ScreenView.REINSERT_ACTION
                                | ScreenView.NAME_ONLY));
                //listView.setTabName(LocaleMgr.screen.getString("FragmentationKeyList"));
            }
        }

        /** Oracle */
        else if (semObj instanceof DbORATablespace) {
            vecPanels.add(listView = new ORListView(semObj, DbORADataFile.metaClass));
            //listView.setTabName(LocaleMgr.screen.getString("DataFileList"));
            vecPanels.add(listView = new ORListView(semObj, DbORATablespace.fTables,
                    DbORATable.metaClass, ScreenView.LINK_UNL_ACTIONS | ScreenView.PROPERTIES_BTN
                            | ScreenView.REINSERT_ACTION | ScreenView.NAME_ONLY));
            //listView.setTabName(DbORTable.metaClass);
            vecPanels.add(listView = new ORListView(semObj, DbORATablespace.fIndexes,
                    DbORAIndex.metaClass, ScreenView.LINK_UNL_ACTIONS | ScreenView.PROPERTIES_BTN
                            | ScreenView.REINSERT_ACTION | ScreenView.NAME_ONLY));
            //listView.setTabName(DbORIndex.metaClass);
            vecPanels.add(listView = new ORListView(semObj, DbORATablespace.fLobs,
                    DbORALobStorage.metaClass, ScreenView.UNLINK_BTN | ScreenView.PROPERTIES_BTN
                            | ScreenView.REINSERT_ACTION | ScreenView.NAME_ONLY));
            //listView.setTabName(LocaleMgr.screen.getString("LOBStorageList"));
            vecPanels.add(listView = new ORListView(semObj, DbORATablespace.fAbsPartitions,
                    DbORAAbsPartition.metaClass, ScreenView.UNLINK_BTN | ScreenView.PROPERTIES_BTN
                            | ScreenView.REINSERT_ACTION | ScreenView.NAME_ONLY));
            //listView.setTabName(LocaleMgr.screen.getString("PartitionList"));
            vecPanels.add(listView = new ORListView(semObj, DbORATablespace.fRollbackSegments,
                    DbORARollbackSegment.metaClass, ScreenView.UNLINK_BTN
                            | ScreenView.PROPERTIES_BTN | ScreenView.REINSERT_ACTION
                            | ScreenView.NAME_ONLY));
            //listView.setTabName(LocaleMgr.screen.getString("RollbackSegmentList"));

        }

        else if (semObj instanceof DbORTrigger) {
            vecPanels.add(listView = new ORListView(semObj, DbORTrigger.fColumns,
                    DbORTrigger.metaClass, ScreenView.LINK_UNL_ACTIONS | ScreenView.REINSERT_ACTION
                            | ScreenView.NAME_ONLY));
            vecPanels
                    .add(listView = new ORListView(semObj, DbORTrigger.fSubCopies,
                            DbORTrigger.metaClass, ScreenView.UNLINK_BTN
                                    | ScreenView.PROPERTIES_BTN | ScreenView.SORTED_LIST
                                    | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
            //listView.setTabName(LocaleMgr.screen.getString("subTriggers"));
        }

        else if (semObj instanceof DbORTypeClassifier) {
            if (semObj instanceof DbORDomain) {
            	//allowable Values
                listView = new ORListView(semObj, DbORAllowableValue.fComponents,
                        DbORAllowableValue.metaClass, ScreenView.ADD_BTN | ScreenView.DELETE_BTN
                                | ScreenView.APPLY_ACTION | ScreenView.REINSERT_ACTION);
                listView.setTabName(LocaleMgr.term.getString("AllowableValueComponents"));
                vecPanels.add(listView);
                
                //domain fields
                if (ScreenPerspective.isFullVersion()) {
                	listView = new ORListView(semObj, DbORField.metaClass); 
                	vecPanels.add(listView);
                }
            } //end if
            
            vecPanels.add(listView = new ORListView(semObj, new MetaRelationN[] {
                    DbORTypeClassifier.fTypedAttributes, DbORTypeClassifier.fCommonItems,
                    DbORTypeClassifier.fDomains, DbORTypeClassifier.fReturnTypedProcedures,
                    DbORTypeClassifier.fTypedParameters }, DbSemanticalObject.metaClass,
                    ScreenView.PROPERTIES_BTN | ScreenView.SORTED_LIST | ScreenView.DISPLAY_CLASS
                            | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
            listView.setTabName(kUsedBy);
        }

        else if (semObj instanceof DbORView) {
            vecPanels.add(listView = new ORListView(semObj, metaClasses[AnyORObject.I_COLUMN]));
            //listView.setTabName(DbORColumn.metaClass);
            vecPanels.add(listView = new ORListView(semObj,
                    metaClasses[AnyORObject.I_PRIMARYUNIQUE]));
            //listView.setTabName(DbORPrimaryUnique.metaClass);
            vecPanels.add(listView = new ORListView(semObj, metaClasses[AnyORObject.I_CHECK]));
            //listView.setTabName(DbORCheck.metaClass);
            vecPanels.add(listView = new ORListView(semObj, DbORCheck.fComponents,
                    metaClasses[AnyORObject.I_FOREIGN], ScreenView.DELETE_BTN
                            | ScreenView.PROPERTIES_BTN | ScreenView.APPLY_ACTION
                            | ScreenView.REINSERT_ACTION));
            //listView.setTabName(DbORForeign.metaClass);
            vecPanels.add(listView = new ORListView(semObj, metaClasses[AnyORObject.I_INDEX]));
            //listView.setTabName(DbORIndex.metaClass);
            vecPanels.add(listView = new ORListView(semObj, metaClasses[AnyORObject.I_TRIGGER]));
            //listView.setTabName(DbORTrigger.metaClass);
            vecPanels
                    .add(listView = new ORListView(semObj, DbORTrigger.fSubCopies,
                            DbORTrigger.metaClass, ScreenView.UNLINK_BTN
                                    | ScreenView.PROPERTIES_BTN | ScreenView.SORTED_LIST
                                    | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
            //listView.setTabName(LocaleMgr.screen.getString("subTables"));
            /** Oracle */
            if (semObj instanceof DbORAView) {
                vecPanels.add(listView = new ORListView(semObj, metaClasses[AnyORObject.I_CHECK]));
                //listView.setTabName(DbORCheck.metaClass);
                vecPanels
                        .add(listView = new ORListView(semObj, metaClasses[AnyORObject.I_TRIGGER]));
                //listView.setTabName(DbORTrigger.metaClass);
            }
        }

        else if (semObj instanceof DbORUser) {
            vecPanels.add(listView = new ORListView(semObj, DbORUser.fTables,
                    DbORAbsTable.metaClass, ScreenView.LINK_UNL_ACTIONS | ScreenView.SORTED_LIST
                            | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
            vecPanels.add(listView = new ORListView(semObj, DbORUser.fIndexes, DbORIndex.metaClass,
                    ScreenView.LINK_UNL_ACTIONS | ScreenView.SORTED_LIST | ScreenView.NAME_ONLY
                            | ScreenView.FULL_NAME));
            vecPanels.add(listView = new ORListView(semObj, DbORUser.fOperations,
                    DbORTrigger.metaClass, ScreenView.LINK_UNL_ACTIONS | ScreenView.SORTED_LIST
                            | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
            listView.setTabName(LocaleMgr.screen.getString("Triggers"));
            vecPanels
                    .add(listView = new ORListView(semObj, DbORUser.fOperations,
                            DbORAbstractMethod.metaClass, ScreenView.LINK_UNL_ACTIONS
                                    | ScreenView.SORTED_LIST | ScreenView.NAME_ONLY
                                    | ScreenView.FULL_NAME));
            listView.setTabName(LocaleMgr.screen.getString("Procedures"));
            vecPanels.add(listView = new ORListView(semObj, DbORUser.fDomains,
                    DbORDomain.metaClass, ScreenView.LINK_UNL_ACTIONS | ScreenView.SORTED_LIST
                            | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
            vecPanels.add(listView = new ORListView(semObj, DbORUser.fSequences,
                    DbORASequence.metaClass, ScreenView.LINK_UNL_ACTIONS | ScreenView.SORTED_LIST
                            | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
            vecPanels
                    .add(listView = new ORListView(semObj, DbORUser.fPrimaryUniqueKeys,
                            DbINFPrimaryUnique.metaClass, ScreenView.LINK_UNL_ACTIONS
                                    | ScreenView.SORTED_LIST | ScreenView.NAME_ONLY
                                    | ScreenView.FULL_NAME));
            vecPanels.add(listView = new ORListView(semObj, DbORUser.fForeignKeys,
                    DbINFForeign.metaClass, ScreenView.LINK_UNL_ACTIONS | ScreenView.SORTED_LIST
                            | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
            vecPanels.add(listView = new ORListView(semObj, DbORUser.fCheckConstraints,
                    DbINFCheck.metaClass, ScreenView.LINK_UNL_ACTIONS | ScreenView.SORTED_LIST
                            | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
        }

        else if (semObj instanceof DbORChoiceOrSpecialization) {
            //Associated roles
            int mode = ScreenView.LINK_UNL_ACTIONS | ScreenView.SORTED_LIST | ScreenView.FULL_NAME;
            listView = new ORListView(semObj, DbORChoiceOrSpecialization.fAssociationEnds,
                    DbORAssociationEnd.metaClass, mode);
            vecPanels.add(listView);

            //Sub choices/specializations
            mode = ScreenView.UNLINK_BTN | ScreenView.PROPERTIES_BTN | ScreenView.SORTED_LIST
                    | ScreenView.NAME_ONLY | ScreenView.FULL_NAME;
            listView = new ORListView(semObj, DbORChoiceOrSpecialization.fSubCopies,
                    DbORChoiceOrSpecialization.metaClass, mode);
            String term = LocaleMgr.term.getString("SubChoicesSpecs");
            listView.setTabName(term);
            vecPanels.add(listView);
        }

        /** Business process frames **/
        else if (semObj instanceof DbBEUseCase) {
            vecPanels.add(listView = new BEListView(semObj, new MetaRelationN[] {
                    DbBEUseCase.fFirstEndFlows, DbBEUseCase.fSecondEndFlows }, DbBEFlow.metaClass,
                    ScreenView.PROPERTIES_BTN | ScreenView.FULL_NAME));
            listView.setTabName(LocaleMgr.screen.getString("InputOutputFlows"));

            vecPanels.add(listView = new BEListView(semObj, DbObject.fComponents,
                    DbBEUseCaseResource.metaClass, ScreenView.LINK_UNL_ACTIONS
                            | ScreenView.APPLY_ACTION | ScreenView.FULL_NAME));
            listView.setParentRel(DbBEUseCaseResource.fResource);
            vecPanels.add(listView = new BEListView(semObj, DbObject.fComponents,
                    DbBEUseCaseQualifier.metaClass, ScreenView.LINK_UNL_ACTIONS
                            | ScreenView.APPLY_ACTION | ScreenView.FULL_NAME));
            listView.setParentRel(DbBEUseCaseQualifier.fQualifier);

        } else if (semObj instanceof DbBEActor) {
            vecPanels.add(listView = new BEListView(semObj, DbObject.fComponents,
                    DbBEActorQualifier.metaClass, ScreenView.LINK_UNL_ACTIONS
                            | ScreenView.APPLY_ACTION | ScreenView.FULL_NAME));
            listView.setParentRel(DbBEActorQualifier.fQualifier);
        } else if (semObj instanceof DbBEStore) {
            vecPanels.add(listView = new BEListView(semObj, DbObject.fComponents,
                    DbBEStoreQualifier.metaClass, ScreenView.LINK_UNL_ACTIONS
                            | ScreenView.APPLY_ACTION | ScreenView.FULL_NAME));
            listView.setParentRel(DbBEStoreQualifier.fQualifier);
        } else if (semObj instanceof DbBEFlow) {
            vecPanels.add(listView = new BEListView(semObj, DbObject.fComponents,
                    DbBEFlowQualifier.metaClass, ScreenView.LINK_UNL_ACTIONS
                            | ScreenView.APPLY_ACTION | ScreenView.FULL_NAME));
            listView.setParentRel(DbBEFlowQualifier.fQualifier);
            vecPanels.add(listView = new BEListView(semObj, DbSMSSemanticalObject.fSubCopies,
                    DbSMSSemanticalObject.metaClass, ScreenView.UNLINK_BTN
                            | ScreenView.PROPERTIES_BTN | ScreenView.SORTED_LIST
                            | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
            listView.setTabName(LocaleMgr.screen.getString("subFlows"));
        } else if (semObj instanceof DbBEResource) {
            vecPanels.add(listView = new BEListView(semObj, DbBEResource.fProcessUsages,
                    DbBEUseCaseResource.metaClass, ScreenView.LINK_UNL_ACTIONS
                            | ScreenView.APPLY_ACTION | ScreenView.DISPLAY_PARENT_NAME
                            | ScreenView.FULL_NAME));
            listView.setParentRel(DbBEUseCaseResource.fComposite);
        } else if (semObj instanceof DbBEQualifier) {
            vecPanels.add(listView = new BEListView(semObj, DbBEQualifier.fQualifiedUseCases,
                    DbBEUseCaseQualifier.metaClass, ScreenView.LINK_UNL_ACTIONS
                            | ScreenView.APPLY_ACTION | ScreenView.DISPLAY_PARENT_NAME
                            | ScreenView.FULL_NAME));
            listView.setParentRel(DbBEUseCaseQualifier.fComposite);
            vecPanels.add(listView = new BEListView(semObj, DbBEQualifier.fQualifiedActors,
                    DbBEActorQualifier.metaClass, ScreenView.LINK_UNL_ACTIONS
                            | ScreenView.APPLY_ACTION | ScreenView.DISPLAY_PARENT_NAME
                            | ScreenView.FULL_NAME));
            listView.setParentRel(DbBEActorQualifier.fComposite);
            vecPanels.add(listView = new BEListView(semObj, DbBEQualifier.fQualifiedStores,
                    DbBEStoreQualifier.metaClass, ScreenView.LINK_UNL_ACTIONS
                            | ScreenView.APPLY_ACTION | ScreenView.DISPLAY_PARENT_NAME
                            | ScreenView.FULL_NAME));
            listView.setParentRel(DbBEStoreQualifier.fComposite);
            vecPanels.add(listView = new BEListView(semObj, DbBEQualifier.fQualifiedFlows,
                    DbBEFlowQualifier.metaClass, ScreenView.LINK_UNL_ACTIONS
                            | ScreenView.APPLY_ACTION | ScreenView.DISPLAY_PARENT_NAME
                            | ScreenView.FULL_NAME));
            listView.setParentRel(DbBEFlowQualifier.fComposite);
        }
        /** Java Frames */
        else if (semObj instanceof DbOOAdt) {
            if (semObj instanceof DbJVClass) {
                vecPanels.add(listView = new JVListView(semObj, DbJVDataMember.metaClass));
                vecPanels.add(listView = new JVListView(semObj, DbJVMethod.metaClass));
                vecPanels.add(listView = new JVListView(semObj, DbJVConstructor.metaClass));
                vecPanels.add(listView = new JVListView(semObj, DbJVInitBlock.metaClass));

                vecPanels.add(listView = new JVListView(semObj, DbOOClass.fSuperInheritances,
                        DbJVInheritance.metaClass, ScreenView.LINK_UNL_ACTIONS
                                | ScreenView.REINSERT_ACTION | ScreenView.DISPLAY_CLASS
                                | ScreenView.FULL_NAME));
                listView.setParentRel(DbOOInheritance.fSuperClass);
                listView.setTabName(LocaleMgr.screen.getString("DirectSuperclasses"));

                vecPanels.add(listView = new JVListView(semObj, DbOOClass.fSubInheritances,
                        DbJVInheritance.metaClass, ScreenView.PROPERTIES_BTN
                                | ScreenView.SORTED_LIST | ScreenView.DISPLAY_CLASS
                                | ScreenView.FULL_NAME));
                listView.setParentRel(DbOOInheritance.fSubClass);
                listView.setTabName(LocaleMgr.screen.getString("DirectSubclasses"));

                vecPanels.add(listView = new JVListView(semObj, DbObject.fComponents,
                        DbJVClass.metaClass, ScreenView.DELETE_BTN | ScreenView.PROPERTIES_BTN
                                | ScreenView.APPLY_ACTION | ScreenView.REINSERT_ACTION
                                | ScreenView.DISPLAY_CLASS));
                listView.setTabName(LocaleMgr.screen.getString("Nested/InnerClasses"));

                if (((DbJVClass) semObj).isException()) {
                    vecPanels.add(listView = new JVListView(semObj, new MetaRelationN[] {
                            DbJVClass.fThrowingMethods, DbJVClass.fThrowingConstructors },
                            DbSemanticalObject.metaClass, ScreenView.PROPERTIES_BTN
                                    | ScreenView.SORTED_LIST | ScreenView.DISPLAY_CLASS
                                    | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
                    listView.setTabName(DbJVClass.fThrowingMethods.getGUIName());
                }
            }
            vecPanels.add(listView = new JVListView(semObj, new MetaRelationN[] {
                    DbOOAdt.fTypedDataMembers, DbOOAdt.fTypedMethods, DbOOAdt.fTypedParameters },
                    DbSemanticalObject.metaClass, ScreenView.PROPERTIES_BTN
                            | ScreenView.SORTED_LIST | ScreenView.DISPLAY_CLASS
                            | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
            listView.setTabName(kUsedBy);
        }

        else if (semObj instanceof DbJVMethod) {
            vecPanels.add(listView = new JVListView(semObj, DbJVParameter.metaClass));
            vecPanels.add(listView = new JVListView(semObj, DbJVMethod.fJavaExceptions,
                    DbJVClass.metaClass, ScreenView.LINK_UNL_ACTIONS | ScreenView.REINSERT_ACTION
                            | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
        } else if (semObj instanceof DbJVConstructor) {
            vecPanels.add(listView = new JVListView(semObj, DbJVParameter.metaClass));
            vecPanels.add(listView = new JVListView(semObj, DbJVConstructor.fJavaExceptions,
                    DbJVClass.metaClass, ScreenView.LINK_UNL_ACTIONS | ScreenView.REINSERT_ACTION
                            | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
        } else if (semObj instanceof DbJVCompilationUnit) {
            vecPanels.add(listView = new JVListView(semObj, DbJVCompilationUnit.fClasses,
                    DbJVClass.metaClass, ScreenView.LINK_UNL_ACTIONS | ScreenView.REINSERT_ACTION
                            | ScreenView.DISPLAY_CLASS | ScreenView.NAME_ONLY));

            vecPanels.add(listView = new JVListView(semObj, DbObject.fComponents,
                    DbJVImport.metaClass, ScreenView.LINK_UNL_ACTIONS | ScreenView.APPLY_ACTION
                            | ScreenView.REINSERT_ACTION | ScreenView.DISPLAY_CLASS
                            | ScreenView.FULL_NAME));
            listView.setParentRel(DbJVImport.fImportedObject);
        } else if (semObj instanceof DbJVAssociation) {
            vecPanels.add(listView = new JVListView(semObj, DbObject.fComponents,
                    DbJVAssociationEnd.metaClass, ScreenView.APPLY_ACTION));
        }

        // DB2-UDB
        else if (semObj instanceof DbIBMDbPartitionGroup) {
            vecPanels.add(listView = new SMSListView(semObj,
                    DbIBMDbPartitionGroup.fDbIBMBufferPools, DbIBMBufferPool.metaClass,
                    ScreenView.PROPERTIES_BTN | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
            vecPanels.add(listView = new SMSListView(semObj, DbIBMDbPartitionGroup.fTablespaces,
                    DbIBMTablespace.metaClass, ScreenView.PROPERTIES_BTN | ScreenView.NAME_ONLY
                            | ScreenView.FULL_NAME));
        }

        // DB2-UDB
        else if (semObj instanceof DbIBMBufferPool) {
            vecPanels.add(listView = new SMSListView(semObj,
                    DbIBMBufferPool.fDbIBMDbPartitionGroups, DbIBMDbPartitionGroup.metaClass,
                    ScreenView.LINK_UNL_ACTIONS | ScreenView.REINSERT_ACTION
                            | ScreenView.DISPLAY_CLASS | ScreenView.NAME_ONLY
                            | ScreenView.FULL_NAME));
            vecPanels.add(listView = new SMSListView(semObj, DbIBMBufferPool.fTablespaces,
                    DbIBMTablespace.metaClass, ScreenView.PROPERTIES_BTN | ScreenView.NAME_ONLY
                            | ScreenView.FULL_NAME));
        }

        else if (semObj instanceof DbSMSLink) {
            vecPanels.add(listView = new SMSListView(semObj, DbSMSLink.fSourceObjects,
                    DbSMSSemanticalObject.metaClass, ScreenView.LINK_UNL_ACTIONS
                            | ScreenView.REINSERT_ACTION | ScreenView.DISPLAY_CLASS
                            | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
            vecPanels.add(listView = new SMSListView(semObj, DbSMSLink.fTargetObjects,
                    DbSMSSemanticalObject.metaClass, ScreenView.LINK_UNL_ACTIONS
                            | ScreenView.REINSERT_ACTION | ScreenView.DISPLAY_CLASS
                            | ScreenView.NAME_ONLY | ScreenView.FULL_NAME));
        }

        // Add "UML Constraints" at the end
        // Do not add "UML Constraints" if perspective is Data Model Viewer
        if (semObj instanceof DbSMSSemanticalObject) {
        	if (ScreenPerspective.isFullVersion()) {
        		listView = new SMSListView(semObj, DbSMSSemanticalObject.fUmlConstraints,
                    DbSMSUmlConstraint.metaClass, ScreenView.LINK_UNL_ACTIONS
                            | ScreenView.NAME_ONLY);
        		vecPanels.add(listView);
        	}
        } //end if

        // Additionnal <Links> tab for all semantical objects
        if (AnySemObject.supportsLinks(semObj.getMetaClass())) {
            vecPanels.add(listView = new SMSListView(semObj, linksTabRelations,
                    DbSMSLink.metaClass, ScreenView.PROPERTIES_BTN | ScreenView.APPLY_ACTION
                            | ScreenView.SORTED_LIST | ScreenView.FULL_NAME));
            listView.setTabName(DbSMSLink.metaClass);
        }
        
        //For OO or OR frames
        //Do not add F/W eng. tabs if perspective is "Data Model Viewer"
        if (ScreenPerspective.isFullVersion()) {
        	getForwardPanels(vecPanels, semObj);
        } 

        ScreenTabPanel[] panels = new ScreenTabPanel[vecPanels.size()];
        vecPanels.copyInto(panels);
        return panels;
    }

    private static boolean isSupported(Class claz, Class[] supportedClasses) {
        boolean supported = false;

        for (int i = 0; i < supportedClasses.length; i++) {
            Class supportedClass = supportedClasses[i];
            if (supportedClass.isAssignableFrom(claz)) {
                supported = true;
                break;
            } //end if
        } //end for

        return supported;
    }

    public void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
        super.refreshAfterDbUpdate(e);
        if (e.dbo instanceof DbORPrimaryUnique && e.metaField == DbORPrimaryUnique.fPrimary) {
            Icon icon = semObj.getSemanticalIcon(DbObject.SHORT_FORM);
            if (icon != null)
                setFrameIcon(icon);
        } else if (e.dbo instanceof DbORProcedure && e.metaField == DbORProcedure.fFunction) {
            Icon icon = semObj.getSemanticalIcon(DbObject.SHORT_FORM);
            if (icon != null)
                setFrameIcon(icon);
        } else if (e.dbo instanceof DbINFSbspace && e.metaField == DbINFSbspace.fBlobSpace) {
            Icon icon = semObj.getSemanticalIcon(DbObject.SHORT_FORM);
            if (icon != null)
                setFrameIcon(icon);
        } else if (e.dbo instanceof DbORDomain && e.metaField == DbORDomain.fCategory) {
            Icon icon = getSemanticalIcon(e.dbo);
            if (icon != null)
                setFrameIcon(icon);
        } else if (e.dbo instanceof DbORBuiltInType && e.metaField == DbORBuiltInType.fBuiltIn) {
            Icon icon = semObj.getSemanticalIcon(DbObject.SHORT_FORM);
            if (icon != null)
                setFrameIcon(icon);
        }
    }

    protected Icon getSemanticalIcon(DbObject semObject) throws DbException {
        if (semObject instanceof DbORDomainModel) {
            if (((DbORDomainModel) semObject).getDeploymentDatabase() != null) {
                return SMSExplorer.udtModelImage;
            }
        } else if (semObject instanceof DbORDomain) {
            if (((DbORDomain) semObject).getCategory() != null
                    && !((DbORDomain) semObject).getCategory().equals(
                            ORDomainCategory.getInstance(ORDomainCategory.DOMAIN))) {
                return SMSExplorer.udtImage;
            }
        }
        return super.getSemanticalIcon(semObject);
    }

    private static void getForwardPanels(Vector vecPanels, DbObject semObj) throws DbException {
        DbORDataModel dataModel = null;
        if (semObj instanceof DbORDataModel)
            dataModel = (DbORDataModel) semObj;
        else {
            DbObject dbo = semObj.getCompositeOfType(DbORDataModel.metaClass);
            if (dbo != null)
                dataModel = (DbORDataModel) dbo;
        }
        if (dataModel != null) {
            if (terminologyUtil.getModelLogicalMode(dataModel) != DbORDataModel.LOGICAL_MODE_OBJECT_RELATIONAL)
                return;
        }

        PluginMgr mgr = PluginMgr.getSingleInstance();
        PluginsRegistry registry = mgr.getPluginsRegistry();
        try {
        	List forwardList = registry.getActivePluginInstances(JackForwardEngineeringPlugin.class);
        
	        for (int i = 0; i < forwardList.size(); i++) {
	            JackForwardEngineeringPlugin forward = (JackForwardEngineeringPlugin) forwardList
	                    .get(i);
	            PropertyScreenPreviewInfo info = forward.getPropertyScreenPreviewInfo();
	            if (info != null) {
	                Class[] supportedClasses = info.getSupportedClasses();
	                Class claz = semObj.getClass();
	                if (isSupported(claz, supportedClasses)) {
	                    boolean isEditable = false; //by default
	                    ForwardPanel panel = new ForwardPanel((DbSemanticalObject) semObj, forward,
	                            isEditable);
	                    vecPanels.add(panel);
	                } //end if
	            } //end if
	        } //end for
        } catch (Throwable th) {
        	th.printStackTrace(); 
        }
    } //end getForwardPanels()
} //end SMSPropertiesFrame
