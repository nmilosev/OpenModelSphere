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

package org.modelsphere.sms.or.features.dbms;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.actions.ExpandAllAction;
import org.modelsphere.jack.srtool.actions.ShowDiagramAction;
import org.modelsphere.jack.srtool.reverse.engine.ReverseBuilder;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.actions.CreateMissingGraphicsAction;
import org.modelsphere.sms.actions.LayoutSelectionAction;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.db.DbORUserNode;
import org.modelsphere.sms.or.db.srtypes.ORDomainCategory;
import org.modelsphere.sms.or.generic.db.DbGEDataModel;
import org.modelsphere.sms.or.generic.db.DbGEDatabase;
import org.modelsphere.sms.or.generic.db.DbGEOperationLibrary;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.plugins.ReverseUtilities;
import org.modelsphere.sms.plugins.TargetSystem;
import org.modelsphere.sms.plugins.TargetSystemManager;

public abstract class GenericBuilder extends ReverseBuilder {
    // The models used as root for searching composite or used as composite.
    private static final String kUserDefinedTypeModel = LocaleMgr.misc
            .getString("UserDefinedTypeModel");

    protected DbORDataModel dataModel;
    protected DbORDiagram diagram;
    protected DbORDomainModel domainModel;
    protected DbORDomainModel udtModel;
    protected DbORDatabase database;
    protected DbOROperationLibrary operationLibrary;
    protected DbORUserNode userNode;

    protected final void initSpecific() throws DbException {
        addGlobalOption(CHAIN_COMMIT, Boolean.TRUE);

        TargetSystem targetSystem = TargetSystemManager.getSingleton();
        DbSMSTargetSystem ts = TargetSystem.getSpecificTargetSystem(project,
                getDBMSReverseOptions().targetSystemId);
        if (ts == null)
            ts = targetSystem.createTargetSystem(project, getDBMSReverseOptions().targetSystemId);

        dataModel = createORDataModel(root, ts);
        if (dataModel != null) {
            appendOutputText(MessageFormat.format(PATTERN_DBO_CREATED, new Object[] {
                    dataModel.getMetaClass().getGUIName(),
                    dataModel.getSemanticalName(DbObject.SHORT_FORM) }));
            diagram = new DbORDiagram(dataModel);
        }
        database = createORDatabase(root, ts);
        if (database != null)
            appendOutputText(MessageFormat.format(PATTERN_DBO_CREATED, new Object[] {
                    database.getMetaClass().getGUIName(),
                    database.getSemanticalName(DbObject.SHORT_FORM) }));
        operationLibrary = createOROperationLibrary(root, ts);
        if (operationLibrary != null)
            appendOutputText(MessageFormat.format(PATTERN_DBO_CREATED, new Object[] {
                    operationLibrary.getMetaClass().getGUIName(),
                    operationLibrary.getSemanticalName(DbObject.SHORT_FORM) }));
        userNode = ((DbSMSProject) project).getUserNode();
        if (getDBMSReverseOptions().domainModel == null) {
            domainModel = new DbORDomainModel(root, ts);
            database.setDefaultDomainModel(domainModel);
            if (domainModel != null)
                appendOutputText(MessageFormat.format(PATTERN_DBO_CREATED, new Object[] {
                        domainModel.getMetaClass().getGUIName(),
                        domainModel.getSemanticalName(DbObject.SHORT_FORM) }));
        } else {
            domainModel = (DbORDomainModel) getDBMSReverseOptions().domainModel;
            database.setDefaultDomainModel(domainModel);
        }

        udtModel = new DbORDomainModel(root, ts);
        udtModel.setName(kUserDefinedTypeModel);
        udtModel.setPhysicalName(kUserDefinedTypeModel);
        udtModel.setDeploymentDatabase(database);

        dataModel.setDeploymentDatabase(database);
        operationLibrary.setDeploymentDatabase(database);

        initDBMSSpecific();
    } //end initSpecific()

    protected final DBMSReverseOptions getDBMSReverseOptions() {
        return (DBMSReverseOptions) configuration;
    }

    public void buildORDiagram() {

        CreateMissingGraphicsAction action2 = (CreateMissingGraphicsAction) ApplicationContext
                .getActionStore().get("CreateMissingGraphics");
        action2.performAction(diagram);

        ExpandAllAction action4 = (ExpandAllAction) ApplicationContext.getActionStore().get(
                AbstractActionsStore.EXPAND_ALL);
        action4.performAction(diagram);

        ShowDiagramAction action = (ShowDiagramAction) ApplicationContext.getActionStore().get(
                AbstractActionsStore.SHOW_DIAGRAM);
        action.performAction(diagram);

        LayoutSelectionAction action3 = (LayoutSelectionAction) ApplicationContext.getActionStore()
                .get("LayoutSelection");
        action3.performAction(new GraphicComponent[] {}, diagram);

    }

    // Override this method to init specific mapping
    protected abstract void initDBMSSpecific() throws DbException;

    protected final DbObject getRoot() throws Exception {
        return ((DBMSReverseOptions) configuration).root;
    }

    protected DbORDataModel createORDataModel(DbObject root, DbSMSTargetSystem ts)
            throws DbException {
        return new DbGEDataModel(root, ts);
    }

    protected DbORDatabase createORDatabase(DbObject root, DbSMSTargetSystem ts) throws DbException {
        return new DbGEDatabase(root, ts);
    }

    protected DbOROperationLibrary createOROperationLibrary(DbObject root, DbSMSTargetSystem ts)
            throws DbException {
        return new DbGEOperationLibrary(root, ts);
    }

    // Override to allow automatic duplication of name in alias (apply to all concept)
    protected boolean duplicateNameInAlias() {
        return false;
    }

    protected final void exitSpecific() throws DbException {
        ReverseUtilities.linkConstraintIndexes(dataModel);
        ReverseUtilities.reverseDependencies(dataModel);
        ReverseUtilities.deduceMultiplicity(dataModel);

        // Make sure all user (may have been created with option CREATE_IF_NOT_FOUND) have a physical name
        DbEnumeration dbEnum = userNode.getComponents().elements(DbORUser.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORUser user = (DbORUser) dbEnum.nextElement();
            if (user.getPhysicalName() == null || user.getPhysicalName().length() == 0) {
                user.setPhysicalName(user.getName());
            }
        }
        dbEnum.close();

        exitDBMSSpecific();
    } //end exitSpecific()

    // Override to remove all references that may prevent garbage collection
    protected abstract void exitDBMSSpecific() throws DbException;

    protected final void abortSpecific() throws DbException {
        abortDBMSSpecific();
    }

    protected abstract void abortDBMSSpecific() throws DbException;

    /* Utility methods */

    // If non existing domain, create it
    protected DbObject getDbORDomain(String name, DbObject sourceType, ORDomainCategory category,
            boolean allowSearch) throws DbException {
        DBMSReverseOptions options = (DBMSReverseOptions) configuration;
        DbORDomain domain = null;
        if (allowSearch) {
            domain = (DbORDomain) udtModel.findComponentByName(DbORDomain.metaClass, name);
            if (domain == null)
                domain = (DbORDomain) domainModel.findComponentByName(DbORDomain.metaClass, name);
        }
        if (domain == null) {
            if (category == null || category.getValue() == ORDomainCategory.DOMAIN)
                domain = new DbORDomain(domainModel);
            else
                domain = new DbORDomain(udtModel);
            domain.setName(name);
            domain.setPhysicalName(name);
            if (category != null)
                domain.setCategory(category);
            if ((sourceType != null) && (sourceType instanceof DbORTypeClassifier))
                domain.setSourceType((DbORTypeClassifier) sourceType);
        }
        return domain;
    }

}
