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

package org.modelsphere.sms.or.screen;

import java.util.ArrayList;
import java.util.Collection;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.baseDb.screen.DbListView;
import org.modelsphere.jack.baseDb.screen.DbTreeLookupDialog;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.screen.model.DbListModel;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModelListener;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.SrScreenContext;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAbstractMethod;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORIndexKey;
import org.modelsphere.sms.or.db.DbORPackage;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORProcedure;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTrigger;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.or.generic.db.DbGEForeign;
import org.modelsphere.sms.or.generic.db.DbGEIndex;
import org.modelsphere.sms.or.generic.db.DbGEPrimaryUnique;
import org.modelsphere.sms.or.generic.db.DbGETable;
import org.modelsphere.sms.or.generic.db.DbGETrigger;
import org.modelsphere.sms.or.ibm.db.DbIBMCheck;
import org.modelsphere.sms.or.ibm.db.DbIBMForeign;
import org.modelsphere.sms.or.ibm.db.DbIBMIndex;
import org.modelsphere.sms.or.ibm.db.DbIBMPrimaryUnique;
import org.modelsphere.sms.or.ibm.db.DbIBMTable;
import org.modelsphere.sms.or.ibm.db.DbIBMTrigger;
import org.modelsphere.sms.or.informix.db.DbINFCheck;
import org.modelsphere.sms.or.informix.db.DbINFColumn;
import org.modelsphere.sms.or.informix.db.DbINFForeign;
import org.modelsphere.sms.or.informix.db.DbINFIndex;
import org.modelsphere.sms.or.informix.db.DbINFPrimaryUnique;
import org.modelsphere.sms.or.informix.db.DbINFSbspace;
import org.modelsphere.sms.or.informix.db.DbINFTable;
import org.modelsphere.sms.or.informix.db.DbINFTrigger;
import org.modelsphere.sms.or.oracle.db.DbORACheck;
import org.modelsphere.sms.or.oracle.db.DbORAColumn;
import org.modelsphere.sms.or.oracle.db.DbORADatabase;
import org.modelsphere.sms.or.oracle.db.DbORAForeign;
import org.modelsphere.sms.or.oracle.db.DbORAIndex;
import org.modelsphere.sms.or.oracle.db.DbORALobStorage;
import org.modelsphere.sms.or.oracle.db.DbORAPrimaryUnique;
import org.modelsphere.sms.or.oracle.db.DbORATable;
import org.modelsphere.sms.or.oracle.db.DbORATablespace;
import org.modelsphere.sms.or.oracle.db.DbORATrigger;
import org.modelsphere.sms.or.screen.model.ORListModel;

public class ORListView extends DbListView {

    private static final String kBlobspace = org.modelsphere.sms.or.informix.international.LocaleMgr.db
            .getString("blobSpace");

    private DbObject semObj1 = null;
    
    //
    // Constructors
    //

    public ORListView(DbObject semObj, MetaClass listClass) throws DbException {
        this(semObj, DbObject.fComponents, listClass, ADD_DEL_ACTIONS | REINSERT_ACTION);
        setTerminology(semObj, listClass);
        setTabName(listClass);
    }
    
    public ORListView(DbObject semObj, MetaRelationN listRelation, MetaClass listClass,
            int actionMode) throws DbException {
        super(SrScreenContext.singleton, semObj, listRelation, listClass, actionMode);
        this.semObj1 = semObj;
        setTerminology(semObj, listClass);
        setTabName(listClass, listRelation);
        //TODO: Roles or Directions??
    }

    public ORListView(DbObject semObj, MetaRelationN[] listRelations, MetaClass listClass,
            int actionMode) throws DbException {
        super(SrScreenContext.singleton, semObj, listRelations, listClass, actionMode);
        setTerminology(semObj, listClass);
        setTabName(listClass);
    }

    public ORListView(DbObject semObj, MetaRelationN[] listRelations, MetaClass listClass,
            Terminology terminology, int actionMode) throws DbException {
        super(SrScreenContext.singleton, semObj, listRelations, listClass, terminology, actionMode);
        setTerminology(semObj, listClass);
        setTabName(listClass);
    }
    
    //
    // Methods
    //

    private void setTerminology(DbObject semObj, MetaClass metaClass) throws DbException {
        DbORDataModel dataModel = null;
        if (semObj instanceof DbORDataModel) {
            dataModel = (DbORDataModel) semObj;
        } else {
            DbObject dbo = semObj.getCompositeOfType(DbORDataModel.metaClass);
            if (dbo != null)
                dataModel = (DbORDataModel) dbo;
        }
        int nMode = TerminologyUtil.LOGICAL_MODE_OBJECT_RELATIONAL;

        if (dataModel != null) {
            TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
            terminology = terminologyUtil.findModelTerminology(dataModel);
            nMode = terminologyUtil.getModelLogicalMode(dataModel);
        }

        // if we are in ER mode, do not add relational-specific tabs
        if (nMode != TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
            return;
        
        int properties = actionMode & ScreenView.PROPERTIES_BTN;
        int newActionField = properties;

        if (semObj instanceof DbGETable) {
            if (metaClass == DbGEIndex.metaClass)
                actionMode = newActionField;
            /*
             * else if(metaClass == DbGECheck.metaClass) actionMode = newActionField;
             */
            else if (metaClass == DbGETrigger.metaClass)
                actionMode = newActionField;
            else if (metaClass == DbGEForeign.metaClass || metaClass == DbORFKeyColumn.metaClass
                    || metaClass == DbORForeign.metaClass)
                actionMode = newActionField;
            else if (metaClass == DbGEPrimaryUnique.metaClass) {
                DbGETable table = (DbGETable) semObj;
                if (table.isIsAssociation() == true)
                    actionMode = newActionField;
            }
        } else if (semObj instanceof DbORATable) {
            if (metaClass == DbORAIndex.metaClass)
                actionMode = newActionField;
            else if (metaClass == DbORACheck.metaClass)
                actionMode = newActionField;
            else if (metaClass == DbORATrigger.metaClass)
                actionMode = newActionField;
            else if (metaClass == DbORAForeign.metaClass || metaClass == DbORFKeyColumn.metaClass
                    || metaClass == DbORForeign.metaClass)
                actionMode = newActionField;
            else if (metaClass == DbORAPrimaryUnique.metaClass) {
                DbORATable table = (DbORATable) semObj;
                if (table.isIsAssociation() == true)
                    actionMode = newActionField;
            }
        } else if (semObj instanceof DbINFTable) {
            if (metaClass == DbINFIndex.metaClass)
                actionMode = newActionField;
            else if (metaClass == DbINFCheck.metaClass)
                actionMode = newActionField;
            else if (metaClass == DbINFTrigger.metaClass)
                actionMode = newActionField;
            else if (metaClass == DbINFForeign.metaClass || metaClass == DbORFKeyColumn.metaClass
                    || metaClass == DbORForeign.metaClass)
                actionMode = newActionField;
            else if (metaClass == DbINFPrimaryUnique.metaClass) {
                DbINFTable table = (DbINFTable) semObj;
                if (table.isIsAssociation() == true)
                    actionMode = newActionField;
            }
        } else if (semObj instanceof DbIBMTable) {
            if (metaClass == DbIBMIndex.metaClass)
                actionMode = newActionField;
            else if (metaClass == DbIBMCheck.metaClass)
                actionMode = newActionField;
            else if (metaClass == DbIBMTrigger.metaClass)
                actionMode = newActionField;
            else if (metaClass == DbIBMForeign.metaClass || metaClass == DbORFKeyColumn.metaClass
                    || metaClass == DbORForeign.metaClass)
                actionMode = newActionField;
            else if (metaClass == DbIBMPrimaryUnique.metaClass) {
                DbIBMTable table = (DbIBMTable) semObj;
                if (table.isIsAssociation() == true)
                    actionMode = newActionField;
            }

        } else if (semObj instanceof DbORTable) {
            if (metaClass == DbORIndex.metaClass)
                actionMode = newActionField;
            /*
             * else if(metaClass == DbORCheck.metaClass) actionMode = newActionField;
             */
            else if (metaClass == DbORTrigger.metaClass)
                actionMode = newActionField;
            else if (metaClass == DbORForeign.metaClass || metaClass == DbORFKeyColumn.metaClass
                    || metaClass == DbORForeign.metaClass)
                actionMode = newActionField;
            else if (metaClass == DbORPrimaryUnique.metaClass) {
                DbORTable table = (DbORTable) semObj;
                if (table.isIsAssociation() == true)
                    actionMode = newActionField;
            }
        } else if (semObj instanceof DbORPrimaryUnique) {
            DbObject dbo = semObj.getCompositeOfType(DbORTable.metaClass);
            if (dbo != null) {
                DbORTable table = (DbORTable) dbo;
                if (table.isIsAssociation() == true)
                    actionMode = newActionField;
            }
        } else // preemptively disable all ther concepts accessibility buttons
        // (except maybe attribute and PK)
        {
            boolean bPreserveNameOnlyField = (actionMode & ScreenView.NAME_ONLY) != 0;
            if (bPreserveNameOnlyField)
                actionMode = newActionField | ScreenView.NAME_ONLY;
            else
                actionMode = newActionField;
        }

    }

    protected final DbListModel createListModel() throws DbException {
        return new ORListModel(this, semObj, listRelations, listClass);
    }

    protected final DbEnumeration getLinkableEnum() throws DbException {

        if (listRelations[0] == DbORPrimaryUnique.fColumns
                || listRelations[0] == DbORTrigger.fColumns
                || listRelations[0] == DbORALobStorage.fLobItems) {
            return semObj.getComposite().getComponents().elements(DbORColumn.metaClass);
        }
        if (listRelations[0] == DbORATable.fPartitionKeyColumns) {
            return semObj.getComponents().elements(DbORAColumn.metaClass);
        }

        if (listRelations[0] == DbORATable.fSubpartitionKeyColumns) {
            return semObj.getComponents().elements(DbORAColumn.metaClass);
        }

        if (listRelations[0] == DbORPrimaryUnique.fAssociationDependencies) {
            return ((DbORAbsTable) semObj.getComposite()).getAssociationEnds().elements();
        }

        if (listRelations[0] == DbORATable.fTablespaces
                || listRelations[0] == DbINFColumn.fSbspaces) {
            DbORDataModel model = (DbORDataModel) semObj
                    .getCompositeOfType(DbORDataModel.metaClass);
            DbORDatabase database = model.getDeploymentDatabase();
            if (database != null) {
                if (listRelations[0] == DbORATable.fTablespaces) {
                    return database.getComponents().elements(DbORATablespace.metaClass);
                } else if (listRelations[0] == DbINFColumn.fSbspaces) {
                    return database.getComponents().elements(DbINFSbspace.metaClass);
                }
            } else
                return null;
        }

        if (listRelations[0] == DbINFTable.fFragmentationKeyColumns) {
            return semObj.getComponents().elements(DbINFColumn.metaClass);
        }
        return super.getLinkableEnum();
    }

    protected Collection getLinkableSet() throws DbException {
        if (listRelations[0] == DbORAIndex.fPartitionKeyColumns
                || listRelations[0] == DbORAIndex.fSubpartitionKeyColumns
                || listRelations[0] == DbINFIndex.fFragmentationKeyColumns) {
            ArrayList indexedElements = new ArrayList();
            DbEnumeration enumKeys = semObj.getComponents().elements(DbORIndexKey.metaClass);

            while (enumKeys.hasMoreElements()) {
                DbORIndexKey nextKey = (DbORIndexKey) enumKeys.nextElement();
                if (nextKey.getIndexedElement() != null) {
                    indexedElements.add(nextKey.getIndexedElement());
                }
            }
            enumKeys.close();
            return indexedElements;
        }

        if (listRelations[0] == DbORDomainModel.fDatabasesDefaultDomains) {
            ArrayList databases = new ArrayList();
            DbEnumeration dbEnum = semObj.getProject().componentTree(DbORDatabase.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORDatabase dbo = (DbORDatabase) dbEnum.nextElement();
                if (AnyORObject.getTargetSystem(semObj) == AnyORObject.getTargetSystem(dbo))
                    databases.add(dbo);
            }
            dbEnum.close();
            return databases;
        }

        return null;
    }

    protected final String getLinkableObjName(DbObject dbo) throws DbException {
        if (listRelations[0] == DbORPrimaryUnique.fColumns) {
            // For primary/unique keys, the user cannot add directly foreign
            // columns to the composition of the key,
            // he must add the corresponding association in the Dependencies
            // Tab.
            if (((DbORColumn) dbo).getFKeyColumns().size() != 0)
                return null;
            return dbo.getName();
        }
        if (listRelations[0] == DbORPrimaryUnique.fAssociationDependencies) {
            if (!AnyORObject.isForeignAssocEnd((DbORAssociationEnd) dbo))
                return null;
            return dbo.getName();
        }
        if (listRelations[0] == DbINFColumn.fSbspaces) {
            if (((DbINFSbspace) dbo).isBlobSpace())
                return super.getLinkableObjName(dbo) + " <" + kBlobspace + ">";
            return super.getLinkableObjName(dbo);
        }
        if (listRelations[0] == DbORTrigger.fColumns
                || listRelations[0] == DbORATable.fPartitionKeyColumns
                || listRelations[0] == DbORAIndex.fPartitionKeyColumns
                || listRelations[0] == DbORATable.fSubpartitionKeyColumns
                || listRelations[0] == DbORAIndex.fSubpartitionKeyColumns
                || listRelations[0] == DbORATable.fTablespaces
                || listRelations[0] == DbORALobStorage.fLobItems
                || listRelations[0] == DbINFIndex.fFragmentationKeyColumns
                || listRelations[0] == DbINFTable.fFragmentationKeyColumns) {
            return dbo.getName();
        }

        return super.getLinkableObjName(dbo);
    }

    protected final DbObject[] showLinkDialog() {
        if (listRelations[0].getMetaClass() == DbORUser.metaClass) {
            MetaClass[] classes = new MetaClass[] { listClass };
            if (listClass == DbORAbstractMethod.metaClass)
                classes = new MetaClass[] { DbORPackage.metaClass, DbORProcedure.metaClass };

            DbTreeModelListener listener = new DbTreeModelListener() {
                public boolean filterNode(DbObject dbo) throws DbException {
                    MetaRelationship relUser = listRelations[0].getOppositeRel(null);
                    if (dbo.hasField(relUser) && dbo.get(relUser) != null)
                        return false;
                    return true;
                }
            };
            return DbTreeLookupDialog.selectMany(this, getTabName(), new DbObject[] { semObj
                    .getProject() }, classes, listener);
        }

        if (listRelations[0] == DbORCommonItem.fColumns) {
            return DbTreeLookupDialog.selectMany(this, getTabName(), new DbObject[] { semObj
                    .getProject() }, new MetaClass[] { listClass }, null);
        }

        if (listRelations[0] == DbORATablespace.fTables) {
            DbTreeModelListener listener = new DbTreeModelListener() {
                public boolean filterNode(DbObject dbo) throws DbException {
                    if (dbo instanceof DbORATable) {
                        DbEnumeration dbEnum = ((DbORATable) dbo).getTablespaces().elements(
                                DbORATablespace.metaClass);
                        while (dbEnum.hasMoreElements()) {
                            DbObject obj = dbEnum.nextElement();
                            if (obj == semObj1) {
                                dbEnum.close();
                                return false;
                            }
                            continue;
                        }
                        dbEnum.close();
                        return true;
                    }
                    if (dbo instanceof DbSMSProject)
                        return true;
                    DbORADatabase database = (DbORADatabase) semObj1
                            .getCompositeOfType(DbORADatabase.metaClass);
                    /*
                     * DbEnumeration dbEnum = database.getDataModels().elements(DbORADataModel
                     * .metaClass); while (dbEnum.hasMoreElements()){ DbObject obj =
                     * dbEnum.nextElement(); if (obj.isDescendingFrom(dbo)){ dbEnum.close(); return
                     * true; } continue; } dbEnum.close();
                     */
                    if (database.getSchema() != null && database.getSchema().isDescendingFrom(dbo))
                        return true;
                    return false;
                }
            };
            return DbTreeLookupDialog.selectMany(this, getTabName(), new DbObject[] { semObj
                    .getProject() }, new MetaClass[] { listClass }, listener);
        }

        if (listRelations[0] == DbORATablespace.fIndexes) {
            DbTreeModelListener listener = new DbTreeModelListener() {
                public boolean filterNode(DbObject dbo) throws DbException {
                    if (dbo instanceof DbORATable) {
                        return true;
                    }
                    if (dbo instanceof DbORAIndex) {
                        if (((DbORAIndex) dbo).getTablespace() == semObj1) {
                            return false;
                        }
                        return true;
                    }
                    if (dbo instanceof DbSMSProject)
                        return true;
                    DbORADatabase database = (DbORADatabase) semObj1
                            .getCompositeOfType(DbORADatabase.metaClass);
                    /*
                     * DbEnumeration dbEnum = database.getDataModels().elements(DbORADataModel
                     * .metaClass); while (dbEnum.hasMoreElements()){ DbObject obj =
                     * dbEnum.nextElement(); if (obj.isDescendingFrom(dbo)){ dbEnum.close(); return
                     * true; } continue; } dbEnum.close();
                     */
                    if (database.getSchema() != null && database.getSchema().isDescendingFrom(dbo))
                        return true;
                    return false;
                }
            };
            return DbTreeLookupDialog.selectMany(this, getTabName(), new DbObject[] { semObj
                    .getProject() }, new MetaClass[] { listClass }, listener);
        }
        return super.showLinkDialog(); // display a flat list of linkable
        // objects
    }
}
