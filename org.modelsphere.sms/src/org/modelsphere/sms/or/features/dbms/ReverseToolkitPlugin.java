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

import java.awt.event.ActionEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.gui.task.GuiController;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.jack.gui.wizard.WizardPage;
import org.modelsphere.jack.plugins.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;
import org.modelsphere.jack.srtool.integrate.IntegrateFrame;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectScope;
import org.modelsphere.jack.srtool.reverse.Actions;
import org.modelsphere.jack.srtool.reverse.engine.ReverseBuilder;
import org.modelsphere.jack.srtool.services.*;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.SMSSynchroModel.SynchroObject;
import org.modelsphere.sms.db.DbSMSUserDefinedPackage;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.or.international.LocaleMgr;

public abstract class ReverseToolkitPlugin implements Plugin {
    protected static final String kReverseEng = LocaleMgr.misc.getString("ReverseEng");
    protected static final String kSynchro = LocaleMgr.misc.getString("Synchro");
    private static final String kReverseInTempModel = LocaleMgr.misc
            .getString("ReverseInTempModel");
    private static final String kCreateMissingSchema = LocaleMgr.message
            .getString("CreateMissingSchema");
    private static final String kCreateMissingOperationLib = LocaleMgr.message
            .getString("CreateMissingOperationLib");
    private static final String kCreateMissingUDTModel = LocaleMgr.message
            .getString("CreateMissingUDTModel");
    private static final String kCreateMissingDomainModel = LocaleMgr.message
            .getString("CreateMissingDomainModelModel");
    private static final String ERROR = LocaleMgr.message.getString("Error");

    public static final String DEFAULT_LOG_FILENAME = ApplicationContext.getLogPath()
            + System.getProperty("file.separator") + "reverse.log";

    // The default toolkit is considered as a Null Object.
    // Getting the toolkit doesn't need a (toolkit != null) verification.
    private static ReverseToolkitPlugin defaultToolkit = new DefaultReverseToolkit();

    // Toolkits - DBMS name mapping
    private static ArrayList toolkitsDBMS = new ArrayList(20);
    // Toolkits - Target id mapping
    private static ArrayList toolkitsTarget = new ArrayList(20);
    // Supported extension file filters
    private static ArrayList toolkitsExtension = new ArrayList(20);
    private static ArrayList extensionFileFilters = new ArrayList(5);

    // Optimization purposes
    private static ReverseToolkitPlugin lastKit = null;

    // Fields to identify the Toolkit to use.
    private static ConnectionMessage connection = null;
    private static int activeDiagramTargetId = -1;

    // Used for execute() during a getToolkitForConnection(cm);
    private static ConnectionMessage tempConnection = null;

    /**
     * A Default Class for Toolkit. The instance of this class represent a null toolkit that will be
     * returned from a call to getToolkit when no toolkit is applicable. Default or Generic behovior
     * should be implemented in this class
     */
    private static final class DefaultReverseToolkit extends ReverseToolkitPlugin {
        private PluginSignature signature;

        DefaultReverseToolkit() {
        }

        public PluginSignature getSignature() {
            if (signature == null) {
                signature = new PluginSignature(createTitle(false), "$Revision: 9 $",
                        "$Date: 6/04/04 9:59a $", 150); // NOT LOCALIZABLE
            }
            return signature;
        }
    }

    private class KitDBMSMapping {
        String dbmsName;
        ReverseToolkitPlugin kit;

        KitDBMSMapping(String name, ReverseToolkitPlugin aKit) {
            dbmsName = name;
            kit = aKit;
        }
    }

    private class KitTargetMapping {
        int targetId;
        ReverseToolkitPlugin kit;

        KitTargetMapping(int id, ReverseToolkitPlugin aKit) {
            targetId = id;
            kit = aKit;
        }
    }

    private class KitFileExtensionMapping {
        ExtensionFileFilter filter;
        ReverseToolkitPlugin kit;

        KitFileExtensionMapping(ExtensionFileFilter filter, ReverseToolkitPlugin aKit) {
            this.filter = filter;
            kit = aKit;
        }
    }

    protected ReverseToolkitPlugin() {
    }

    /**
     * This method is called after all plugins have been loaded by the plugin manager to avoid
     * requesting info from the manager during instantiation of this (the info is not valid yet at
     * that time since the manager must perform additional operations)
     */
    public void init() {
        PluginMgr mgr = PluginMgr.getSingleInstance();
        PluginDescriptor pluginDescriptor = mgr.getPluginsRegistry().getPluginInfo(this);
        if (mgr.isValid(pluginDescriptor)) {
            // Register this toolkit with supported dbmss names
            if (!(this instanceof DefaultReverseToolkit)) {
                String[] supportedDBMSs = getSupportedDBMSNames();
                if (supportedDBMSs != null) {
                    for (int i = 0; i < supportedDBMSs.length; i++)
                        toolkitsDBMS.add(new KitDBMSMapping(supportedDBMSs[i], this));
                }
                int[] supportedTargets = getSupportedTargetIds();
                if (supportedTargets != null) {
                    for (int i = 0; i < supportedTargets.length; i++)
                        toolkitsTarget.add(new KitTargetMapping(supportedTargets[i], this));
                }
                ExtensionFileFilter[] supportedFilters = getSupportedExtensionFileFilter();
                if (supportedFilters != null) {
                    for (int i = 0; i < supportedFilters.length; i++) {
                        toolkitsExtension
                                .add(new KitFileExtensionMapping(supportedFilters[i], this));
                        extensionFileFilters.add(supportedFilters[i]);
                    }
                }
            }

            // check if default toolkit
            if (this.isDefaultToolkit())
                defaultToolkit = this;
        }
    }

    // Get the toolkit to handle both
    public static final ReverseToolkitPlugin getToolkit() {
        if (lastKit != null)
            return lastKit;

        if (connection == null) {
            lastKit = getToolkitForTargetSystem();
            return lastKit;
        } else if (activeDiagramTargetId == -1) {
            lastKit = getToolkitForConnection();
            return lastKit;
        }
        String dbmsName = connection.databaseProductName;
        if (dbmsName == null)
            return defaultToolkit;

        String dbmsVersion = connection.databaseProductVersion;

        //search the kit
        Object[] map = toolkitsDBMS.toArray();
        for (int i = 0; i < map.length; i++) {
            if (dbmsName.toLowerCase().indexOf(((KitDBMSMapping) map[i]).dbmsName.toLowerCase()) != -1) {
                // check in the toolkit if the version is supported
                int id = ((KitDBMSMapping) map[i]).kit.getTargetSystemId(dbmsVersion);
                if (id != -1) {
                    ArrayList kitsForTarget = findMatchingToolkitsForTargetId(id);
                    if (kitsForTarget.contains(((KitDBMSMapping) map[i]).kit)) {
                        lastKit = ((KitDBMSMapping) map[i]).kit;
                        return lastKit;
                    }
                }
            }
        }
        return defaultToolkit;
    }

    private static final ArrayList findMatchingToolkitsForTargetId(int id) {
        ArrayList kits = new ArrayList(5);
        Object[] map = toolkitsTarget.toArray();
        for (int i = 0; i < map.length; i++) {
            if (id == ((KitTargetMapping) map[i]).targetId) {
                kits.add(((KitTargetMapping) map[i]).kit);
            }
        }
        return kits;
    }

    public static final ReverseToolkitPlugin getToolkitForConnection() {
        if (lastKit != null)
            return lastKit;

        if (connection == null)
            return defaultToolkit;

        String dbmsName = connection.databaseProductName;
        if (dbmsName == null)
            return defaultToolkit;

        String dbmsVersion = connection.databaseProductVersion;

        //search the kit
        Object[] map = toolkitsDBMS.toArray();
        String lwDbmsName = dbmsName.toLowerCase();

        for (int i = 0; i < map.length; i++) {
            KitDBMSMapping mapping = (KitDBMSMapping) map[i];
            if (lwDbmsName.indexOf(mapping.dbmsName.toLowerCase()) != -1) {
                // check in the toolkit if the version is supported
                if (mapping.kit.getTargetSystemId(dbmsVersion) != -1) {
                    return ((KitDBMSMapping) map[i]).kit;
                }
            }
        }
        return defaultToolkit;
    }

    // Return a valid toolkit for the connection (ignoring model targets system)
    public static final ReverseToolkitPlugin getToolkitForConnection(ConnectionMessage cm) {
        if (cm == null)
            return defaultToolkit;

        String dbmsName = cm.databaseProductName;
        if (dbmsName == null)
            return defaultToolkit;

        String dbmsVersion = cm.databaseProductVersion;

        //search the kit
        Object[] map = toolkitsDBMS.toArray();
        tempConnection = cm;
        for (int i = 0; i < map.length; i++) {
            if (dbmsName.toLowerCase().indexOf(((KitDBMSMapping) map[i]).dbmsName.toLowerCase()) != -1) {
                // check in the toolkit if the version is supported
                if (((KitDBMSMapping) map[i]).kit.getTargetSystemId(dbmsVersion) != -1) {
                    return ((KitDBMSMapping) map[i]).kit;
                }
            }
        }
        tempConnection = null;
        return defaultToolkit;
    }

    public static final ReverseToolkitPlugin getToolkitForExtension(String extension) {
        if (extension == null || extension.length() == 0)
            return defaultToolkit;
        Iterator iter = toolkitsExtension.iterator();
        while (iter.hasNext()) {
            KitFileExtensionMapping filtermap = (KitFileExtensionMapping) iter.next();
            if (filtermap.filter.getExtension() == null)
                continue;
            if (filtermap.filter.getExtension().equalsIgnoreCase(extension))
                return filtermap.kit;
        }
        return defaultToolkit;
    }

    public static final ReverseToolkitPlugin getToolkitForTargetSystem() {
        if (lastKit != null)
            return lastKit;

        if (activeDiagramTargetId < 0)
            return defaultToolkit;

        //search the kit
        Object[] map = toolkitsTarget.toArray();
        for (int i = 0; i < map.length; i++) {
            if (activeDiagramTargetId == ((KitTargetMapping) map[i]).targetId) {
                return ((KitTargetMapping) map[i]).kit;
            }
        }
        return defaultToolkit;
    }

    // Return a valid toolkit for the targets system id (ignoring connection)
    public static final ReverseToolkitPlugin getToolkitForTargetSystem(int ts) {
        if (ts < 0)
            return defaultToolkit;

        //search the kit
        Object[] map = toolkitsTarget.toArray();
        for (int i = 0; i < map.length; i++) {
            if (ts == ((KitTargetMapping) map[i]).targetId) {
                return ((KitTargetMapping) map[i]).kit;
            }
        }
        return defaultToolkit;
    }

    // Calling this method will invalidate the active toolkit.
    public static final void setConnection(ConnectionMessage cm) {
        /*
         * if (cm == connection) return;
         */
        connection = cm;
        lastKit = null;
    }

    // Calling this method will invalidate the active toolkit.
    public static final void setActiveDiagramTarget(int targetId) {
        if (activeDiagramTargetId == targetId)
            return;
        activeDiagramTargetId = targetId;
        lastKit = null;
    }

    public static final ArrayList getExtensionFileFilters() {
        return extensionFileFilters;
    }

    /////////////////////////////////////////////////////////////////////////////////
    // Internal Toolkits management methods

    // return an array of String representing the DBMS names supported by this toolkit
    // these String object will be used to identify wich toolkit can be used
    protected String[] getSupportedDBMSNames() {
        return null;
    }

    protected int[] getSupportedTargetIds() {
        return null;
    }

    // For debug purpose (v1)
    protected ExtensionFileFilter[] getSupportedExtensionFileFilter() {
        return null;
    }

    // Execute the statement
    protected final String executeStatement(String statement) {
        if ((connection == null && tempConnection == null) || statement == null
                || statement.length() == 1)
            return null;
        try {
            StatementMessage statementMsg = null;
            String addressIP = ServiceProtocolList.getServerIP();
            int port = ServiceProtocolList.STATEMENT_SERVICE;

            Socket s = new Socket(addressIP, port);
            ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
            statementMsg = new StatementMessage(tempConnection == null ? connection.connectionId
                    : tempConnection.connectionId, statement);

            output.writeObject(statementMsg);

            ObjectInputStream input = new ObjectInputStream(s.getInputStream());
            statementMsg = (StatementMessage) input.readObject();
            if (statementMsg.errorMessage != null)
                return null;
            return statementMsg.resultList.toString();
        } catch (Exception e) {
            return null;
        }
    }

    // return true if the specified version is supported by this toolkit
    // the DBMS name is already validated
    // deprecated:  protected boolean versionSupported(String version){return false;}

    // return a unique id representing the target system and version.  return -1 if not supported
    // parameter version contain the connection DBMS version or a file extension (offline)
    protected int getTargetSystemId(String version) {
        return -1;
    }

    // parameter extension contain the a file extension (offline)
    public int getTargetSystemIdForExtension(String extension) {
        return -1;
    }

    // There should be only one default toolkit.  This toolkit must be able to handle any situations and target.
    protected boolean isDefaultToolkit() {
        return false;
    }

    public boolean isSynchroSupported() {
        return false;
    }

    // End of Internal Toolkits management methods
    /////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////
    // Navigation methods

    protected boolean beforePageChange(int pageid, DBMSReverseOptions options) {
        return true;
    }

    protected boolean afterPageChange(int pageid, DBMSReverseOptions options) {
        return true;
    }

    // This method is called when the reverse process (wizard) is cancel
    // Override this method if the kit need to make some clean up
    protected boolean reverseCanceled() {
        return true;
    }

    // This method is called after completion of the reverse process
    // Kits can override this method to clean up references, ...
    //protected void reverseCompleted(){}

    // End of Navigation methods
    /////////////////////////////////////////////////////////////////////////////////

    public String getSQLFileName_Gets(DBMSReverseOptions options) {
        return null;
    }

    public String getSQLFileName_Xtr(DBMSReverseOptions options) {
        return null;
    }

    public String getFileName_ScopeSynchro(DBMSReverseOptions options) {
        return null;
    }

    // If null:  default order will be used (SMSSynchroModel.defaultClassOrder)
    public Class[] getGenClassOrder_Synchro(DBMSReverseOptions options) {
        return null;
    }

    // Return dependencies for wich we must propagate drop and create actions.
    public MetaRelationship[] getDependencies_Synchro() {
        return null;
    }

    // Return true if a modification for the specified metafield is supported by the modify(alter) clause for the specified dbo.
    // If the desired behavior is to generate nothing on a modified metafield value, remove it from the synchro scope (.scp).
    // The default behevior is to return false only if the specified metafield is the user.
    // Returning false will generate a drop/create instead.
    public boolean isSupportedInModify_Synchro(DbObject dbo, MetaField metafield)
            throws DbException {
        if (dbo == null || metafield == null)
            return true;
        if (AnyORObject.getUserField(dbo) == metafield)
            return false;
        return true;
    }

    // Return true if the create entry point SQL clause for the composite include the creation of the specified component MetaClass
    // Example:  Column is always included in the Create table clause
    // Default: If check, index, PK, UK or Trigger: false.  Otherwise: true.
    // If true, any create statement of a composite will ensure that a create statement is added for the components not supported in the
    // create statement of this composite.
    // NOTE:  This method is not used to determine the generation behavior of the Foreign Keys
    public boolean isSupportedInCreate_Synchro(MetaClass composite, MetaClass component) {
        if (composite == null || component == null)
            return true;
        if (DbORAbsTable.metaClass.isAssignableFrom(composite)) {
            if (DbORCheck.metaClass.isAssignableFrom(component))
                return false;
            if (DbORIndex.metaClass.isAssignableFrom(component))
                return false;
            if (DbORPrimaryUnique.metaClass.isAssignableFrom(component))
                return false;
            if (DbORTrigger.metaClass.isAssignableFrom(component))
                return false;
        }
        return true;
    }

    /*
     * public SynchroObject[] getEquivalent_Synchro(SynchroObject synchroObject) throws DbException{
     * return null; }
     */

    // Get a combination of the options:
    // DefaultSynchroModel.DROP_TABLE_CASCADE_FK : IF DROP TABLE support Cascading to constraints
    // Default value is DROP_TABLE_CASCADE_FK.
    public int getSynchroOptions(DBMSReverseOptions options) {
        return DefaultSynchroModel.OPTION_DROP_TABLE_CASCADE_FK;
    }

    public Class getForwardPluginClass(DBMSReverseOptions options) {
        return null;
    }

    // Override to create equivalence during integration process.  This method is called by the
    // integration only if values already differ using default comparaison method.
    protected boolean isEquivalentInSynchro(MetaField field, DbObject refDbo, Object refValue,
            DbObject physDbo, Object physValue) throws DbException {
        // if String type, ignore cases
        if (refValue == null || physValue == null)
            return false;
        if (refValue instanceof String && physValue instanceof String)
            return ((String) refValue).trim().equalsIgnoreCase(((String) physValue).trim());

        return false;
    }

    // This method is used to determine what to do if there is no sql clause specified in the SQLForward for
    // this concept.
    // If true, the DefaultSynchroModel will try a drop create on the composite to resolve the unsuported clause of
    // the component.  If false, the modifications of the components will be marked as not modified in the report and
    // no sql clause will be generated for these modifications (apply to create/drop/modify/rename).
    public boolean isAllowCompositeDropCreateForComponentUpdate_Synchro(MetaClass componentMetaClass) {
        return true;
    }

    // Override this method to specify replacements for this synchro object. The method must return the
    // synchroObj if no replacement.
    public SynchroObject[] getEquivalentSynchroObject(SynchroObject syncObj) throws DbException {
        return new SynchroObject[] { syncObj };
    }

    // Configure the specific options for this dbms
    // Note:  options may already be configure for this toolkit.
    //        Check specific object instance before configuring otherwise options may be
    //        resset each time the wizard page is changed.
    public Object createSpecificOptions() {
        return null;
    }

    public ObjectScope[] createObjectsScope() {
        return new ObjectScope[] {};
    }

    // Determines if the sql request corresponding to the specified concept should be executed.
    // The default behavior is to execute the request according to the scope selection
    public boolean executeSQLRequest(DBMSReverseOptions options, ObjectScope concept) {
        if (concept == null)
            return true;
        return ObjectScope.verifyConceptSelection(options.getObjectsScope(), concept);
    }

    public String createTitle(boolean synchro) {
        return synchro ? kSynchro : kReverseEng;
    }

    // Override this method if the toolkit need to override specific wizard pages
    public WizardPage createWizardPage(int pageid, boolean synchro) {
        return null;
    }

    public void processNameListMessage(DBMSReverseOptions options, NameListMessage nameListMessage) {
    }

    public Actions createActions() {
        return null;
    }

    // Used during synchronization for creating a temporary data model for reverse eng.
    //protected DbORDataModel createDataModel(DBMSReverseOptions options, DbObject composite) throws DbException{return null;}

    // This class instance will be invoke on the event dispatch thread after reverse worker has completed.
    private class SynchroLauncher implements Runnable {
        DbObject source;
        DbObject target;
        boolean deleteTargetDb;
        DBMSReverseOptions options;

        SynchroLauncher(DbObject source, DbObject target, DBMSReverseOptions options,
                boolean deleteTargetDb) {
            this.source = source;
            this.target = target;
            this.deleteTargetDb = deleteTargetDb;
            this.options = options;
        }

        public void run() {
            try {
                Db.beginMatching();

                source.getDb().beginTrans(Db.READ_TRANS);
                target.getDb().beginTrans(Db.READ_TRANS);

                DbORDatabase targetDatabase = null;
                DbORDatabase sourceDatabase = null;

                if (target instanceof DbORDatabase)
                    targetDatabase = (DbORDatabase) target;
                else {
                    DbEnumeration dbEnum = target.getComponents().elements(DbORDatabase.metaClass);
                    targetDatabase = (DbORDatabase) (dbEnum.hasMoreElements() ? dbEnum
                            .nextElement() : null);
                    dbEnum.close();
                }
                if (source instanceof DbORDatabase)
                    sourceDatabase = (DbORDatabase) source;
                else {
                    DbEnumeration dbEnum = source.getComponents().elements(DbORDatabase.metaClass);
                    sourceDatabase = (DbORDatabase) (dbEnum.hasMoreElements() ? dbEnum
                            .nextElement() : null);
                    dbEnum.close();
                }

                DbProject targetProject = (target instanceof DbProject) ? (DbProject) target
                        : (DbProject) target.getCompositeOfType(DbProject.metaClass);

                DbORDataModel targetSchema = targetDatabase.getSchema();
                DbOROperationLibrary targetOperationLib = targetDatabase.getOperationLibrary();
                DbORDomainModel targetUDTModel = targetDatabase.getUdtModel();
                DbORDomainModel targetDomainModel = targetDatabase.getDefaultDomainModel();
                DbORDataModel sourceSchema = sourceDatabase.getSchema();
                DbOROperationLibrary sourceOperationLib = sourceDatabase.getOperationLibrary();
                DbORDomainModel sourceUDTModel = sourceDatabase.getUdtModel();
                DbORDomainModel sourceDomainModel = sourceDatabase.getDefaultDomainModel();

                source.getDb().commitTrans();
                target.getDb().commitTrans();

                boolean cancel = false;
                if (targetSchema == null ^ sourceSchema == null) {
                    // Add missing nodes
                    int result = JOptionPane.showConfirmDialog(ApplicationContext
                            .getDefaultMainFrame(), kCreateMissingSchema, createTitle(true),
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        String transName = MessageFormat.format(
                                org.modelsphere.sms.international.LocaleMgr.misc.getString("Add0"),
                                new Object[] { DbORDataModel.metaClass.getGUIName() });
                        source.getDb().beginTrans(Db.WRITE_TRANS, transName);
                        target.getDb().beginTrans(Db.WRITE_TRANS, transName);
                        if (targetSchema == null) {
                            targetSchema = AnyORObject.createDataModel(targetDatabase
                                    .getComposite(), targetDatabase.getTargetSystem());
                            targetDatabase.setSchema(targetSchema);
                        }
                        if (sourceSchema == null) {
                            sourceSchema = AnyORObject.createDataModel(sourceDatabase
                                    .getComposite(), sourceDatabase.getTargetSystem());
                            sourceDatabase.setSchema(sourceSchema);
                        }
                        source.getDb().commitTrans();
                        target.getDb().commitTrans();
                    } else if (result == JOptionPane.CANCEL_OPTION) {
                        cancel = true;
                    }
                }

                if (!cancel && (targetOperationLib == null ^ sourceOperationLib == null)) {
                    // Add missing nodes
                    int result = JOptionPane.showConfirmDialog(ApplicationContext
                            .getDefaultMainFrame(), kCreateMissingOperationLib, createTitle(true),
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        String transName = MessageFormat.format(
                                org.modelsphere.sms.international.LocaleMgr.misc.getString("Add0"),
                                new Object[] { DbOROperationLibrary.metaClass.getGUIName() });
                        source.getDb().beginTrans(Db.WRITE_TRANS, transName);
                        target.getDb().beginTrans(Db.WRITE_TRANS, transName);
                        if (targetOperationLib == null) {
                            targetOperationLib = AnyORObject.createOperationLibrary(targetDatabase
                                    .getComposite(), targetDatabase.getTargetSystem());
                            targetDatabase.setOperationLibrary(targetOperationLib);
                        }
                        if (sourceOperationLib == null) {
                            sourceOperationLib = AnyORObject.createOperationLibrary(sourceDatabase
                                    .getComposite(), sourceDatabase.getTargetSystem());
                            sourceDatabase.setOperationLibrary(sourceOperationLib);
                        }
                        source.getDb().commitTrans();
                        target.getDb().commitTrans();
                    } else if (result == JOptionPane.CANCEL_OPTION) {
                        cancel = true;
                    }
                }

                if (!cancel && (sourceUDTModel == null ^ targetUDTModel == null)) {
                    // Add missing nodes
                    int result = JOptionPane.showConfirmDialog(ApplicationContext
                            .getDefaultMainFrame(), kCreateMissingUDTModel, createTitle(true),
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        String transName = MessageFormat.format(
                                org.modelsphere.sms.international.LocaleMgr.misc.getString("Add0"),
                                new Object[] { DbORDomainModel.metaClass.getGUIName() });
                        source.getDb().beginTrans(Db.WRITE_TRANS, transName);
                        target.getDb().beginTrans(Db.WRITE_TRANS, transName);
                        if (targetUDTModel == null) {
                            targetUDTModel = new DbORDomainModel(targetDatabase.getComposite(),
                                    targetDatabase.getTargetSystem());
                            targetDatabase.setUdtModel(targetUDTModel);
                        }
                        if (sourceUDTModel == null) {
                            sourceUDTModel = new DbORDomainModel(sourceDatabase.getComposite(),
                                    sourceDatabase.getTargetSystem());
                            sourceDatabase.setUdtModel(sourceUDTModel);
                        }
                        source.getDb().commitTrans();
                        target.getDb().commitTrans();
                    } else if (result == JOptionPane.CANCEL_OPTION) {
                        cancel = true;
                    }
                }

                if (!cancel && (sourceDomainModel == null ^ targetDomainModel == null)) {
                    // Add missing nodes
                    int result = JOptionPane.showConfirmDialog(ApplicationContext
                            .getDefaultMainFrame(), kCreateMissingDomainModel, createTitle(true),
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        String transName = MessageFormat.format(
                                org.modelsphere.sms.international.LocaleMgr.misc.getString("Add0"),
                                new Object[] { DbORDomainModel.metaClass.getGUIName() });
                        source.getDb().beginTrans(Db.WRITE_TRANS, transName);
                        target.getDb().beginTrans(Db.WRITE_TRANS, transName);
                        if (targetDomainModel == null) {
                            targetDomainModel = new DbORDomainModel(targetDatabase.getComposite(),
                                    targetDatabase.getTargetSystem());
                            targetDatabase.setDefaultDomainModel(targetDomainModel);
                        }
                        if (sourceDomainModel == null) {
                            sourceDomainModel = new DbORDomainModel(sourceDatabase.getComposite(),
                                    sourceDatabase.getTargetSystem());
                            sourceDatabase.setDefaultDomainModel(sourceDomainModel);
                        }
                        source.getDb().commitTrans();
                        target.getDb().commitTrans();
                    } else if (result == JOptionPane.CANCEL_OPTION) {
                        cancel = true;
                    }
                }

                if (!cancel) {
                    source.getDb().beginTrans(Db.READ_TRANS);
                    target.getDb().beginTrans(Db.READ_TRANS);
                    DefaultSynchroModel model = new DefaultSynchroModel(targetDatabase, source,
                            options.fieldTree, true, options.synchroUseUser, options);
                    source.getDb().commitTrans();
                    target.getDb().commitTrans();

                    IntegrateFrame frame = new IntegrateFrame(model);
                    frame.setVisible(true);
                }

                Db.endMatching();

                if (deleteTargetDb) {
                    Db db = targetProject.getDb();
                    db.beginTrans(Db.WRITE_TRANS);
                    targetProject.remove();
                    db.commitTrans();
                    db.terminate();
                }
                options = null;
            } catch (Exception e) {
                Db.endMatching();
                options = null;
                ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), e);
            }
        }
    } //end SynchroLauncher()

    // This method must do the reverse job
    public void doReverse(DBMSReverseOptions options) {
        GuiController controller = null;
        if (options.synchro) {
            controller = new DefaultController(createTitle(options.synchro), false,
                    DEFAULT_LOG_FILENAME, kReverseInTempModel);
            DbObject source = options.synchroSourceDatabase;
            DbObject target = options.synchroTargetDatabase;
            boolean useconnection = (target == null);
            options.synchroOnline = useconnection;
            if (target == null) {
                try {
                    target = ApplicationContext.getDefaultMainFrame().createProject(new DbRAM());
                    if (target != null) {
                        target.getDb().beginTrans(Db.WRITE_TRANS, "");
                        target = new DbSMSUserDefinedPackage(target);
                        target.getDb().commitTrans();
                    }
                } catch (DbException e) {
                    ExceptionHandler.processUncatchedException(ApplicationContext
                            .getDefaultMainFrame(), e);
                }
                options.root = target;
                controller.setCloseOnTerminate(true);
            }
            SynchroLauncher synchro = new SynchroLauncher(source, target, options, useconnection);
            if (!useconnection) { // skip reverse
                synchro.run();
                return;
            }
            controller.invokeOnDispose(synchro);
        } else {
            if (options.root == null) {
                options.root = ApplicationContext.getDefaultMainFrame().createDefaultProject(null);
            }

            if (options.root != null) {
                try {
                    Db db = options.root.getDb();
                    db.beginTrans(Db.WRITE_TRANS, ReverseBuilder.kReverse);
                    options.root = new DbSMSUserDefinedPackage(options.root);
                    if (options.getConnection() != null) {
                        String name = options.getConnection().server;
                        if (name == null || name.length() == 0)
                            name = options.getConnection().databaseProductName;
                        options.root.setName(name);
                    }
                    db.commitTrans();
                } catch (DbException e) {
                    ExceptionHandler.processUncatchedException(ApplicationContext
                            .getDefaultMainFrame(), e);
                }
            }
            if (options.root == null)
                return;
            controller = new DefaultController(createTitle(options.synchro), false,
                    DEFAULT_LOG_FILENAME);
        }

        Worker worker = createReverseWorker(options);
        if (worker == null)
            return;
        controller.start(worker);
    }

    protected Worker createReverseWorker(DBMSReverseOptions options) {
        Actions actions = createActions();
        if (!options.synchro && actions == null)
            return null;
        return new DBMSReverseWorker(options, actions, getSQLRequestParsingMode());
    }

    // Possible mode for columns order in the sql extract file
    // MODE_COLUMN_NAME_BEFORE_VALUE:   SELECT 'sr_occurrence_type tablespace'...  (See Oracle extraction file)
    // MODE_COLUMN_NAME_AFTER_VALUE:    SELECT 'user' sr_occurrence, ...  (See Informix extraction file)
    // Default mode = MODE_COLUMN_NAME_BEFORE_VALUE
    protected int getSQLRequestParsingMode() {
        return DBMSReverseWorker.MODE_COLUMN_NAME_BEFORE_VALUE;
    }

    ////////////////////////
    // Plugin implementation
    public final String installAction(DefaultMainFrame frame, MainFrameMenu menuManager) {
        return null;
    }

    public final Class[] getSupportedClasses() {
        return null;
    }

    public final void execute(ActionEvent actEvent) throws Exception {
    }
    
    public boolean doListenSelection() { return (this instanceof PluginSelectionListener); }
    //
    ////////////////////////

} //end ReverseToolkit

