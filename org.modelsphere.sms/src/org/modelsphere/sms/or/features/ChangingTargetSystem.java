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

package org.modelsphere.sms.or.features;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.sms.SMSDeepCopyCustomizer;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.DbSMSUserDefinedPackage;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORModel;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.plugins.TargetSystem;

final class ChangingTargetSystem {
    static final int UNLINK_RELATED_MODELS = 1;
    static final int BACKUP_COPY = 2;
    static final int CONVERT_SUB_MODELS = 4;
    static final int CREATE_NEW_ITEMS = 8;
    static final int REUSE_SAME_ITEMS = 16;

    private static final String TEMPORARY_COPY_STR = LocaleMgr.message.getString("aTemporaryCopy");
    private static final String BACKUP_COPY_STR = LocaleMgr.message.getString("aBackupCopy");
    private static final String BACKUP = LocaleMgr.message.getString("BackupCopy");
    private static final String CREATED_FOR_PATTERN = LocaleMgr.message.getString("CreatedFor");
    private static final String CONVERTING_PATTERN = LocaleMgr.message.getString("ConvertingTo");
    private static final String RETRIEVING_2_PATTERN = LocaleMgr.message
            .getString("CreationOfANewDomainNamed");
    private static final String DELETING_TEMPORARY_MODEL = LocaleMgr.message
            .getString("DeletingTemporaryModels");
    private static final String KEEPING_0_OF_1_INTO_2_PATTERN = LocaleMgr.message
            .getString("Keeping0Of1IntoThe2Package");
    private static final String RETRIEVING_1_PATTERN = LocaleMgr.message
            .getString("The0TypeFoundInThe1TargetSystem");
    private static final String RETRIEVING_3_PATTERN = LocaleMgr.message
            .getString("PleaseGiveA0ValidSourceType");
    private static final String SUB_MODELS_DID_NOT_HAVE_PATTERN = LocaleMgr.message
            .getString("SubModel0DidNotHaveToBeConverted");
    private static final String UNLINK_COMMON_ITEM_PATTERN = LocaleMgr.message
            .getString("UnlinkCommonItemFromColumn");

    private static final String OUPUT_OPTIONS_TITLE = LocaleMgr.message
            .getString("AboutToChangeTargetSystem");
    private static final String OUPUT_OPTIONS_2 = LocaleMgr.message.getString("ModelToConvert");
    private static final String OUPUT_OPTIONS_3 = LocaleMgr.message.getString("ModelCategory");
    private static final String OUPUT_OPTIONS_4 = LocaleMgr.screen.getString("SourceTargetSystem");
    private static final String OUPUT_OPTIONS_5 = LocaleMgr.screen
            .getString("DestinationTargetSystem");
    private static final String OUPUT_OPTIONS_6 = LocaleMgr.message
            .getString("UnlinkAssociatedModels");
    private static final String OUPUT_OPTIONS_7 = BACKUP;
    private static final String OUPUT_OPTIONS_8 = LocaleMgr.message.getString("ConvertSubModels");
    private static final String OUPUT_OPTIONS_9 = LocaleMgr.message.getString("UnlinkCommonItems");

    private static final String YES = LocaleMgr.screen.getString("Yes");
    private static final String NO = LocaleMgr.screen.getString("No");

    private String m_title;
    private ChangingTargetSystemOptions m_options;

    // constructor (called by ChangingTargetSystemWizard)
    ChangingTargetSystem(String title, ChangingTargetSystemOptions options) {
        m_title = title;
        m_options = options;
    }

    // entry points (called by ChangingTargetSystemWizard)
    void execute() {
        DefaultController controller = new DefaultController(m_title, false, null);
        try {
            ChangingTargetSystemWorker worker = new ChangingTargetSystemWorker(m_title, m_options);
            controller.start(worker);
        } catch (DbException ex) {
            controller.println(ex.toString());
        }
    } // end execute()

    //
    // INNER CLASSES
    //
    static class ChangingTargetSystemOptions {
        DbORModel m_model;
        DbORDomainModel m_domainModel;
        DbSMSTargetSystem m_ts;
        int m_params;

        ChangingTargetSystemOptions(DbORModel model, DbORDomainModel domainModel,
                DbSMSTargetSystem ts, int params) throws DbException {
            m_model = model;
            m_domainModel = domainModel;
            m_ts = ts;
            m_params = params;

            if ((model instanceof DbORDataModel) && (m_domainModel == null)) {
                m_domainModel = new DbORDomainModel(model.getComposite(), ts);
            }
        } // end ChangingTargetSystemOptions()
    } // end ChangingTargetSystemOptions

    //
    // ChangingTargetSystemWorker
    //
    private static class ChangingTargetSystemWorker extends Worker {
        private String m_title;
        private ChangingTargetSystemOptions m_options;
        private DbSMSUserDefinedPackage m_backupPackage;

        public ChangingTargetSystemWorker(String title, ChangingTargetSystemOptions options)
                throws DbException {
            m_title = title;
            m_options = options;
            DbProject project = options.m_model.getProject();

            // find back-up package, create it if not found
            m_backupPackage = (DbSMSUserDefinedPackage) findComponentByName(project,
                    DbSMSUserDefinedPackage.metaClass, BACKUP);
            if (m_backupPackage == null) {
                m_backupPackage = new DbSMSUserDefinedPackage(project);
                m_backupPackage.setName(BACKUP);
                String desc = MessageFormat.format(CREATED_FOR_PATTERN, new Object[] { title });
                m_backupPackage.setDescription(desc);
            } // end if
        } // end ChangingTargetSystemWorker

        // Return this job's title
        protected String getJobTitle() {
            return m_title;
        }

        protected void runJob() throws Exception {
            HashMap convertedModelsMap = new HashMap();

            // Output options
            outputOptions();

            // Convert models
            if (m_options.m_model instanceof DbORDataModel) {
                DbORDataModel datamodel = (DbORDataModel) m_options.m_model;
                changeTargetSystem(convertedModelsMap, datamodel);
            } else if (m_options.m_model instanceof DbORDatabase) {
                DbORDatabase database = (DbORDatabase) m_options.m_model;
                changeTargetSystem(convertedModelsMap, database);
            } else if (m_options.m_model instanceof DbORDomainModel) {
                DbORDomainModel domainModel = (DbORDomainModel) m_options.m_model;
                changeTargetSystem(convertedModelsMap, domainModel);
            } else if (m_options.m_model instanceof DbOROperationLibrary) {
                DbOROperationLibrary library = (DbOROperationLibrary) m_options.m_model;
                changeTargetSystem(convertedModelsMap, library);
            } // end if

        } // end runJob()

        //
        // PRIVATE METHODS
        //
        private void outputOptions() throws DbException {
            getController().println();
            getController().println(OUPUT_OPTIONS_TITLE);
            getController().println();
            int len = OUPUT_OPTIONS_5.length(); // longest string

            outputOption(len, OUPUT_OPTIONS_2, m_options.m_model.getName());
            outputOption(len, OUPUT_OPTIONS_3, m_options.m_model.getMetaClass().getGUIName());

            DbSMSTargetSystem srcTs = m_options.m_model.getTargetSystem();
            String srcTsName = srcTs.getName() + " " + srcTs.getVersion();
            outputOption(len, OUPUT_OPTIONS_4, srcTsName);

            String dstTsName = m_options.m_ts.getName() + " " + m_options.m_ts.getVersion();
            outputOption(len, OUPUT_OPTIONS_5, dstTsName);

            String unlinkOpt = ((m_options.m_params & UNLINK_RELATED_MODELS) != 0) ? YES : NO;
            outputOption(len, OUPUT_OPTIONS_6, unlinkOpt);

            String backupOpt = ((m_options.m_params & BACKUP_COPY) != 0) ? YES : NO;
            outputOption(len, OUPUT_OPTIONS_7, backupOpt);

            String submodelOpt = ((m_options.m_params & CONVERT_SUB_MODELS) != 0) ? YES : NO;
            outputOption(len, OUPUT_OPTIONS_8, submodelOpt);

            String itemOpt = ((m_options.m_params & (CREATE_NEW_ITEMS + REUSE_SAME_ITEMS)) == 0) ? YES
                    : NO;
            outputOption(len, OUPUT_OPTIONS_9, itemOpt);

            getController().println();
        } // end outputOptions()

        // private static final String FILLER =
        // "                                          ";
        private void outputOption(int totalLen, String optionName, String value) {
            getController().print(optionName);
            int len = optionName.length();
            if (len < 20) {
                getController().print("\t");
            }

            getController().print("\t: " + value);
            getController().println();
        } // end outputOption()

        private void changeTargetSystem(HashMap convertedModelsMap, DbORDataModel model)
                throws DbException {
            DbProject project = model.getProject();
            DbObject composite = model.getComposite();
            ChangeTargetSystemCopyCustomizer customizer = new ChangeTargetSystemCopyCustomizer(
                    project, composite, m_options.m_ts);
            DbSMSTargetSystem srcTS = model.getTargetSystem();
            String srcTargetSystem = srcTS.getName() + " " + srcTS.getVersion();
            String destTargetSystem = m_options.m_ts.getName() + " " + m_options.m_ts.getVersion();

            if (!convertedModelsMap.containsKey(model)) {
                // deepcopy the whole data model (and its submodels)
                String msg = MessageFormat.format(CONVERTING_PATTERN, new Object[] {
                        model.getName(), destTargetSystem });
                getController().println(msg);
                getController().println();
                DbObject[] targetObjs = DbObject.deepCopy(new DbObject[] { model }, composite,
                        customizer, false);
                DbORDataModel targetModel = (DbORDataModel) targetObjs[0];
                convertedModelsMap.put(model, targetModel);

                // setting deployment database, libraries,
                if ((m_options.m_params & UNLINK_RELATED_MODELS) == 0) { // if
                    // convert
                    // related
                    // models
                    setTargetDependantProperties(convertedModelsMap, model, customizer);
                } // end if
            } // end if

            // Keep a backup/temporary copy
            backup(model);

            // if submodels were not to convert..
            if ((m_options.m_params & CONVERT_SUB_MODELS) == 0) {
                recoverSubModelFromSource(convertedModelsMap, model, destTargetSystem);
                getController().println();
            } // end if

            // if items have to be unlinked..
            if ((m_options.m_params & (CREATE_NEW_ITEMS + REUSE_SAME_ITEMS)) == 0) {
                unlinkCommonItems(convertedModelsMap, model);
                getController().println();
            } // end if

            // retrieve data types lost during the conversion
            DbORDataModel destModel = (DbORDataModel) convertedModelsMap.get(model);
            DbORDomainModel domainModel = m_options.m_domainModel;
            DbSMSTargetSystem ts = domainModel.getTargetSystem();
            if (!ts.equals(m_options.m_ts)) {
                DbORDomainModel convertedDomainModel = (DbORDomainModel) convertedModelsMap
                        .get(domainModel);
                if (convertedDomainModel != null) {
                    domainModel = convertedDomainModel;
                }
            } // end if
            retrieveLostDataTypes(destModel, model, domainModel, srcTargetSystem, destTargetSystem);

            // delete temporary copy, if required
            if ((m_options.m_params & BACKUP_COPY) == 0) {
                m_backupPackage.remove();
                getController().println(DELETING_TEMPORARY_MODEL);
                getController().println();
            }
        } // end changeTargetSystem()

        private void recoverSubModelFromSource(HashMap convertedModelsMap, DbORDataModel srcModel,
                String destTargetSystem) throws DbException {
            ArrayList srcList = new ArrayList();
            DbORDataModel destModel = (DbORDataModel) convertedModelsMap.get(srcModel);

            // PASS 1 : delete converted submodels
            DbRelationN relN = destModel.getComponents();
            DbEnumeration dbEnum = relN.elements(DbORDataModel.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORDataModel subModel = (DbORDataModel) dbEnum.nextElement();
                subModel.remove();
            } // end while
            dbEnum.close();

            // PASS 2 : copy the original submodels into destination (target
            // system will be changed to destination's one)
            relN = srcModel.getComponents();
            dbEnum = relN.elements(DbORDataModel.metaClass);

            while (dbEnum.hasMoreElements()) {
                DbORDataModel subModel = (DbORDataModel) dbEnum.nextElement();
                srcList.add(subModel);

                // get the original target system
                DbSMSTargetSystem ts = subModel.getTargetSystem();

                // recreate the customizer, using original target tsystem
                SMSDeepCopyCustomizer customizer = new ChangeTargetSystemCopyCustomizer(srcModel
                        .getProject(), destModel, ts);

                // deep copy under source model
                DbObject[] targetObjs = DbObject.deepCopy(new DbObject[] { subModel }, destModel,
                        customizer, false);
                convertedModelsMap.put(subModel, targetObjs[0]);

                String srcTargetSystem = ts.getName() + " " + ts.getVersion();
                String msg = MessageFormat.format(SUB_MODELS_DID_NOT_HAVE_PATTERN, new Object[] {
                        subModel.getName(), destTargetSystem, srcTargetSystem });
                getController().println(msg);
            } // end while
            dbEnum.close();

            // continue to sub models
            int nb = srcList.size();
            for (int i = 0; i < nb; i++) {
                DbORDataModel src = (DbORDataModel) srcList.get(i);
                recoverSubModelFromSource(convertedModelsMap, src, destTargetSystem);
            } // end for

        } // end recoverSubModelFromSource()

        private void changeTargetSystem(HashMap convertedModelsMap, DbORDatabase database)
                throws DbException {
            DbProject project = database.getProject();
            DbObject composite = database.getComposite();
            ChangeTargetSystemCopyCustomizer customizer = new ChangeTargetSystemCopyCustomizer(
                    project, composite, m_options.m_ts);

            if (!convertedModelsMap.containsKey(database)) {
                // deepcopy the database
                String destTargetSystem = m_options.m_ts.getName() + " "
                        + m_options.m_ts.getVersion();
                String msg = MessageFormat.format(CONVERTING_PATTERN, new Object[] {
                        database.getName(), destTargetSystem });
                getController().println(msg);
                getController().println();

                DbObject[] targetObjs = DbObject.deepCopy(new DbObject[] { database }, composite,
                        customizer, false);
                DbORDatabase targetDatabase = (DbORDatabase) targetObjs[0];
                convertedModelsMap.put(database, targetDatabase);
            } // end if

            // setting deployment database, libraries,
            if ((m_options.m_params & UNLINK_RELATED_MODELS) == 0) { // if
                // convert
                // related
                // models
                setTargetDependantProperties(convertedModelsMap, database, customizer);
            } // end if

            // Keep a backup/temporary copy
            backup(database);
        } // end changeTargetSystem()

        private void changeTargetSystem(HashMap convertedModelsMap, DbORDomainModel domainModel)
                throws DbException {
            DbProject project = domainModel.getProject();
            DbObject composite = domainModel.getComposite();
            ChangeTargetSystemCopyCustomizer customizer = new ChangeTargetSystemCopyCustomizer(
                    project, composite, m_options.m_ts);

            if (!convertedModelsMap.containsKey(domainModel)) {
                // deepcopy the domain model
                String destTargetSystem = m_options.m_ts.getName() + " "
                        + m_options.m_ts.getVersion();
                String msg = MessageFormat.format(CONVERTING_PATTERN, new Object[] {
                        domainModel.getName(), destTargetSystem });
                getController().println(msg);
                getController().println();

                DbObject[] targetObjs = DbObject.deepCopy(new DbObject[] { domainModel },
                        composite, customizer, false);
                DbORDomainModel targetDomainModel = (DbORDomainModel) targetObjs[0];
                convertedModelsMap.put(domainModel, targetDomainModel);
            } // end if

            // setting deployment database, if any
            if ((m_options.m_params & UNLINK_RELATED_MODELS) == 0) { // if
                // convert
                // related
                // models
                DbORDatabase database = domainModel.getDeploymentDatabase();
                changeTargetSystem(convertedModelsMap, database);
            } // end if

            // Keep a backup/temporary copy
            backup(domainModel);
        } // end changeTargetSystem()

        private void changeTargetSystem(HashMap convertedModelsMap,
                DbOROperationLibrary operationLibrary) throws DbException {
            DbProject project = operationLibrary.getProject();
            DbObject composite = operationLibrary.getComposite();
            ChangeTargetSystemCopyCustomizer customizer = new ChangeTargetSystemCopyCustomizer(
                    project, composite, m_options.m_ts);

            if (!convertedModelsMap.containsKey(operationLibrary)) {
                // deepcopy the domain model
                String destTargetSystem = m_options.m_ts.getName() + " "
                        + m_options.m_ts.getVersion();
                String msg = MessageFormat.format(CONVERTING_PATTERN, new Object[] {
                        operationLibrary.getName(), destTargetSystem });
                getController().println(msg);
                getController().println();

                DbObject[] targetObjs = DbObject.deepCopy(new DbObject[] { operationLibrary },
                        composite, customizer, false);
                DbOROperationLibrary targetLibrary = (DbOROperationLibrary) targetObjs[0];
                convertedModelsMap.put(operationLibrary, targetLibrary);
            } // end if

            // setting deployment database, if any
            if ((m_options.m_params & UNLINK_RELATED_MODELS) == 0) { // if
                // convert
                // related
                // models
                DbORDatabase database = operationLibrary.getDeploymentDatabase();
                changeTargetSystem(convertedModelsMap, database);
            } // end if

            // Keep a backup/temporary copy
            backup(operationLibrary);
        } // end changeTargetSystem()

        private void findUsedDomains(ArrayList domainList, DbORDataModel model) throws DbException {
            DbRelationN relN = model.getComponents();
            DbEnumeration dbEnum = relN.elements(DbORTable.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORTable table = (DbORTable) dbEnum.nextElement();
                DbRelationN relN2 = table.getComponents();
                DbEnumeration enum2 = relN2.elements(DbORColumn.metaClass);
                while (enum2.hasMoreElements()) {
                    DbORColumn col = (DbORColumn) enum2.nextElement();
                    DbORTypeClassifier type = col.getType();
                    if (type instanceof DbORDomain) {
                        if (!domainList.contains(type)) {
                            domainList.add(type);
                        }
                    } // end if
                } // end while
                enum2.close();

            } // end while
            dbEnum.close();

            dbEnum = relN.elements(DbORDataModel.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORDataModel subModel = (DbORDataModel) dbEnum.nextElement();
                findUsedDomains(domainList, subModel);
            } // end while
            dbEnum.close();

        } // end findUsedDomains()

        private void fillDomainModel(DbORDomainModel domainModel, ArrayList domainList,
                SMSDeepCopyCustomizer customizer) throws DbException {
            Iterator iter = domainList.iterator();
            while (iter.hasNext()) {
                DbORDomain sourceDomain = (DbORDomain) iter.next();
                DbObject[] targetDomains = DbObject.deepCopy(new DbObject[] { sourceDomain },
                        domainModel, customizer, false);
            } // end iter()
        } // end fillDomainModel()

        private void setTargetDependantProperties(HashMap convertedModelsMap,
                DbORDataModel sourceModel, SMSDeepCopyCustomizer customizer) throws DbException {
            // Set database
            DbORDatabase sourceDatabase = sourceModel.getDeploymentDatabase();
            if (sourceDatabase != null) {
                changeTargetSystem(convertedModelsMap, sourceDatabase);
            } // end if

            if ((m_options.m_params & CONVERT_SUB_MODELS) != 0) {
                // for each sub data model
                ArrayList sourceSubModels = new ArrayList();
                ArrayList targetSubModels = new ArrayList();

                DbRelationN relN = sourceModel.getComponents();
                DbEnumeration dbEnum = relN.elements(DbORDataModel.metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbORDataModel subModel = (DbORDataModel) dbEnum.nextElement();
                    sourceSubModels.add(subModel);
                } // end while
                dbEnum.close();

                DbORDataModel targetModel = (DbORDataModel) convertedModelsMap.get(sourceModel);
                relN = targetModel.getComponents();
                dbEnum = relN.elements(DbORDataModel.metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbORDataModel subModel = (DbORDataModel) dbEnum.nextElement();
                    targetSubModels.add(subModel);
                } // end while
                dbEnum.close();

                int nb = sourceSubModels.size();
                for (int i = 0; i < nb; i++) {
                    convertedModelsMap.put(sourceSubModels.get(i), targetSubModels.get(i));
                    setTargetDependantProperties(convertedModelsMap,
                            (DbORDataModel) sourceSubModels.get(i), customizer);
                } // end for
            } // end if

        } // end setTargetDependantProperties()

        private void setTargetDependantProperties(HashMap convertedModelsMap,
                DbORDatabase sourceDB, SMSDeepCopyCustomizer customizer) throws DbException {
            String destTargetSystem = m_options.m_ts.getName() + " " + m_options.m_ts.getVersion();

            // Set schema
            DbORDataModel sourceDataModel = sourceDB.getSchema();
            DbORDatabase targetDB = (DbORDatabase) convertedModelsMap.get(sourceDB);
            if (sourceDataModel != null) {
                DbSMSTargetSystem ts = sourceDataModel.getTargetSystem();
                if (ts.equals(m_options.m_ts)) {
                    targetDB.setSchema(sourceDataModel);
                } else {
                    DbORDataModel targetDatamodel = (DbORDataModel) convertedModelsMap
                            .get(sourceDataModel);
                    if (targetDatamodel == null) {
                        DbObject composite = sourceDataModel.getComposite();
                        DbObject[] targetObjs = DbObject.deepCopy(
                                new DbObject[] { sourceDataModel }, composite, customizer, false);
                        targetDatamodel = (DbORDataModel) targetObjs[0];
                    } // end if

                    targetDB.setSchema(targetDatamodel);
                    backup(sourceDataModel);
                    convertedModelsMap.put(sourceDataModel, targetDatamodel);

                    String msg = MessageFormat.format(CONVERTING_PATTERN, new Object[] {
                            sourceDataModel.getName(), destTargetSystem });
                    getController().println(msg);
                    getController().println();
                } // end if
            } // end if

            // Set operation library
            DbOROperationLibrary sourceLibrary = sourceDB.getOperationLibrary();
            if (sourceLibrary != null) {
                DbSMSTargetSystem ts = sourceLibrary.getTargetSystem();
                if (ts.equals(m_options.m_ts)) {
                    targetDB.setOperationLibrary(sourceLibrary);
                } else {
                    DbOROperationLibrary targetLibrary = (DbOROperationLibrary) convertedModelsMap
                            .get(sourceLibrary);
                    if (targetLibrary == null) {
                        DbObject composite = sourceDB.getComposite();
                        DbObject[] targetObjs = DbObject.deepCopy(new DbObject[] { sourceLibrary },
                                composite, customizer, false);
                        targetLibrary = (DbOROperationLibrary) targetObjs[0];
                    } // end if

                    targetDB.setOperationLibrary(targetLibrary);
                    backup(sourceLibrary);
                    convertedModelsMap.put(sourceLibrary, targetLibrary);

                    String msg = MessageFormat.format(CONVERTING_PATTERN, new Object[] {
                            sourceLibrary.getName(), destTargetSystem });
                    getController().println(msg);
                    getController().println();
                } // end if
            } // end if

            // Set default domain model
            DbORDomainModel sourceDomainModel = sourceDB.getDefaultDomainModel();
            if (sourceDomainModel != null) {
                DbSMSTargetSystem ts = sourceDomainModel.getTargetSystem();
                if (ts.equals(m_options.m_ts)) {
                    targetDB.setDefaultDomainModel(sourceDomainModel);
                } else {
                    DbORDomainModel targetDomainModel = (DbORDomainModel) convertedModelsMap
                            .get(sourceDomainModel);
                    if (targetDomainModel == null) {
                        DbObject composite = sourceDB.getComposite();
                        DbObject[] targetObjs = DbObject.deepCopy(
                                new DbObject[] { sourceDomainModel }, composite, customizer, false);
                        targetDomainModel = (DbORDomainModel) targetObjs[0];
                    } // end if

                    targetDB.setDefaultDomainModel(targetDomainModel);
                    backup(sourceDomainModel);
                    convertedModelsMap.put(sourceDomainModel, targetDomainModel);

                    String msg = MessageFormat.format(CONVERTING_PATTERN, new Object[] {
                            sourceDomainModel.getName(), destTargetSystem });
                    getController().println(msg);
                    getController().println();
                } // end if
            } // end if

            // Set UDT domain model
            DbORDomainModel sourceUdtModel = sourceDB.getUdtModel();
            if (sourceUdtModel != null) {
                DbSMSTargetSystem ts = sourceUdtModel.getTargetSystem();
                if (ts.equals(m_options.m_ts)) {
                    targetDB.setDefaultDomainModel(sourceUdtModel);
                } else {
                    DbORDomainModel targetUdtModel = (DbORDomainModel) convertedModelsMap
                            .get(sourceUdtModel);
                    if (targetUdtModel == null) {
                        DbObject composite = sourceDB.getComposite();
                        DbObject[] targetObjs = DbObject.deepCopy(
                                new DbObject[] { sourceUdtModel }, composite, customizer, false);
                        targetUdtModel = (DbORDomainModel) targetObjs[0];
                    } // end if

                    targetDB.setUdtModel(targetUdtModel);
                    backup(sourceUdtModel);
                    convertedModelsMap.put(sourceUdtModel, targetUdtModel);

                    String msg = MessageFormat.format(CONVERTING_PATTERN, new Object[] {
                            sourceUdtModel.getName(), destTargetSystem });
                    getController().println(msg);
                    getController().println();
                } // end if
            } // end if
        } // end setTargetDependantProperties()

        private void backup(DbORModel model) throws DbException {
            // sub models do not have to be backed-up (only the root model has
            // to)
            DbObject composite = model.getComposite();
            if (composite instanceof DbORDataModel) {
                return;
            }

            model.setComposite(m_backupPackage);
            String copyKind = ((m_options.m_params & BACKUP_COPY) == 0) ? TEMPORARY_COPY_STR
                    : BACKUP_COPY_STR;
            String msg = MessageFormat.format(KEEPING_0_OF_1_INTO_2_PATTERN, new Object[] {
                    copyKind, model.getName(), m_backupPackage.getName() });
            getController().println(msg);
            getController().println();
            String name = model.getName();
            model.setName(name + "~");
        } // end backup()

        //
        // RETRIEVE LOST DATA TYPES
        //

        // retrieve data types lost during the conversion
        // If a data type exists in source target system (e.g. BLOB in Logical)
        // and not in
        // the destination target system (e.g. no BLOB in Informix), then create
        // a domain
        // called BLOB for Informix
        private void retrieveLostDataTypes(DbORDataModel destModel, DbORDataModel srcModel,
                DbORDomainModel domainModel, String srcTargetSystem, String destTargetSystem)
                throws DbException {
            DbRelationN relN = destModel.getComponents();
            DbRelationN relN2 = srcModel.getComponents();

            // returns if both models have the same target system
            DbSMSTargetSystem ts_1 = destModel.getTargetSystem();
            DbSMSTargetSystem ts_2 = srcModel.getTargetSystem();
            if (ts_1.equals(ts_2)) {
                return;
            }

            // for each table in both models
            DbEnumeration dbEnum = relN.elements(DbORTable.metaClass);
            DbEnumeration enum2 = relN2.elements(DbORTable.metaClass);
            while (dbEnum.hasMoreElements()) {
                if (!enum2.hasMoreElements()) {
                    break;
                }
                DbORTable destTable = (DbORTable) dbEnum.nextElement();
                DbORTable srcTable = (DbORTable) enum2.nextElement();
                retrieveLostDataTypes(destTable, srcTable, domainModel, srcTargetSystem,
                        destTargetSystem);
            } // end while
            dbEnum.close();
            enum2.close();

            // for each sub model in both models
            dbEnum = relN.elements(DbORDataModel.metaClass);
            enum2 = relN2.elements(DbORDataModel.metaClass);
            while (dbEnum.hasMoreElements()) {
                if (!enum2.hasMoreElements()) {
                    break;
                }

                enum2.hasMoreElements();
                DbORDataModel destSubDataModel = (DbORDataModel) dbEnum.nextElement();
                DbORDataModel srcSubDataModel = (DbORDataModel) enum2.nextElement();
                retrieveLostDataTypes(destSubDataModel, srcSubDataModel, domainModel,
                        srcTargetSystem, destTargetSystem);
            } // end while
            dbEnum.close();
            enum2.close();

        } // end retrieveLostDataTypes()

        private void retrieveLostDataTypes(DbORTable destTable, DbORTable srcTable,
                DbORDomainModel domainModel, String srcTargetSystem, String destTargetSystem)
                throws DbException {
            DbRelationN relN = destTable.getComponents();
            DbRelationN relN2 = srcTable.getComponents();

            // for each column in both tables
            DbEnumeration dbEnum = relN.elements(DbORColumn.metaClass);
            DbEnumeration enum2 = relN2.elements(DbORColumn.metaClass);
            while (dbEnum.hasMoreElements()) {
                if (!enum2.hasMoreElements()) {
                    break;
                }

                DbORColumn destColumn = (DbORColumn) dbEnum.nextElement();
                DbORColumn srcColumn = (DbORColumn) enum2.nextElement();

                // if source column is typed, but not its equivalent destination
                // column
                DbORTypeClassifier srcType = srcColumn.getType();
                if (srcType != null) {
                    if (destColumn.getType() == null) {
                        // find a domain with the same name if domainModel
                        String name = srcType.getName();
                        DbORDomain domain = (DbORDomain) findComponentByName(domainModel,
                                DbORDomain.metaClass, name);
                        if (domain != null) { // if same-name domain exists,
                            // type destColumn with it
                            destColumn.setType(domain);
                        } else { // create a domain with the same name, and type
                            // destColumn with it
                            domain = new DbORDomain(domainModel);
                            domain.setName(name);
                            destColumn.setType(domain);

                            if (srcType instanceof DbORBuiltInType) {
                                // notify the user
                                String msg = MessageFormat.format(RETRIEVING_1_PATTERN,
                                        new Object[] { name, srcTargetSystem, destTargetSystem });
                                getController().println(msg);

                                msg = MessageFormat.format(RETRIEVING_2_PATTERN, new Object[] {
                                        name, domainModel.getName(), srcTargetSystem });
                                getController().println(msg);

                                msg = MessageFormat.format(RETRIEVING_3_PATTERN, new Object[] {
                                        destTargetSystem, domainModel.getName(), name });
                                getController().println(msg);
                                getController().println();
                            } // end if
                        } // end if
                    }// end if
                } // end if
            } // end while
            dbEnum.close();
            enum2.close();

        } // end retrieveLostDataTypes()

        private DbObject findComponentByName(DbObject composite, MetaClass componentMetaClass,
                String searchedName) throws DbException {
            DbObject component = null;

            DbRelationN relN = composite.getComponents();
            DbEnumeration dbEnum = relN.elements(componentMetaClass);
            while (dbEnum.hasMoreElements()) {
                DbObject obj = (DbObject) dbEnum.nextElement();
                String name = obj.getName();
                if ((name != null) && (name.equals(searchedName))) {
                    component = obj;
                    break;
                } // end if
            } // end while
            dbEnum.close();

            return component;
        } // end findDomainByName()

        //
        // UNLINK COMMON ITEMS
        //
        private void unlinkCommonItems(HashMap convertedModelsMap, DbORDataModel model)
                throws DbException {
            DbORDataModel destModel = (DbORDataModel) convertedModelsMap.get(model);
            if (destModel == null) {
                return;
            }

            DbRelationN relN = destModel.getComponents();

            DbEnumeration dbEnum = relN.elements(DbORTable.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORTable table = (DbORTable) dbEnum.nextElement();
                unlinkCommonItems(table);
            } // end while
            dbEnum.close();

            dbEnum = relN.elements(DbORDataModel.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORDataModel subModel = (DbORDataModel) dbEnum.nextElement();
                unlinkCommonItems(convertedModelsMap, subModel);
            } // end while
            dbEnum.close();
            getController().println();
        } // end unlinkCommonItems()

        private void unlinkCommonItems(DbORTable table) throws DbException {
            DbRelationN relN = table.getComponents();
            DbEnumeration dbEnum = relN.elements(DbORColumn.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORColumn column = (DbORColumn) dbEnum.nextElement();
                DbORCommonItem item = column.getCommonItem();
                if (item != null) {
                    DbORCommonItemModel itemModel = (DbORCommonItemModel) item
                            .getCompositeOfType(DbORCommonItemModel.metaClass);
                    column.setCommonItem(null);
                    String msg = MessageFormat.format(UNLINK_COMMON_ITEM_PATTERN,
                            new Object[] { itemModel.getName(), item.getName(), table.getName(),
                                    column.getName() });
                    getController().println(msg);
                }
            } // end while
            dbEnum.close();
        } // end unlinkCommonItems()

    } // end ChangingTargetSystemWorker

    private static class ChangeTargetSystemCopyCustomizer extends SMSDeepCopyCustomizer {
        public ChangeTargetSystemCopyCustomizer(DbProject project, DbObject composite,
                DbSMSTargetSystem ts) throws DbException {
            super(project, composite);
            super.targetSystem = ts;
            super.rootID = AnyORObject.getRootIDFromTargetSystem(ts);
            if (super.rootID == TargetSystem.SGBD_JAVA)
                super.rootID = -1;
        } // end ChangeTargetSystemCopyCustomizer()
    } // end ChangeTargetSystemCopyCustomizer

    //
    // UNIT TEST
    //
    public static void main(String args[]) {

    } // end main()
} // end ChangingTargetSystem
