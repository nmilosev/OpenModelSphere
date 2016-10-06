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
import java.awt.Cursor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.NullFrame;
import org.modelsphere.jack.awt.ToolBarManager;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.screen.ScreenPlugins;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.gui.InfoDialog;
import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.preference.context.ContextManager;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.actions.SelectAllLabelsAction;
import org.modelsphere.jack.srtool.actions.ShowAbstractAction;
import org.modelsphere.jack.srtool.explorer.ExplorerPanel;
import org.modelsphere.jack.srtool.features.SafeMode;
import org.modelsphere.jack.srtool.graphic.MagnifierInternalFrame;
import org.modelsphere.jack.srtool.graphic.OverviewInternalFrame;
import org.modelsphere.jack.srtool.preference.DisplayToolTipsOptionGroup;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.srtool.screen.design.DesignPanel;
import org.modelsphere.jack.srtool.services.ServiceProtocolList;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.actions.ShowLicenseAction;
import org.modelsphere.sms.actions.StartingWizardAction;
import org.modelsphere.sms.be.BEModule;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.features.startupwizard.StartupWizardModel;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.OOModule;
import org.modelsphere.sms.oo.db.DbOOAbstractMethod;
import org.modelsphere.sms.oo.db.DbOOClass;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.java.JavaModule;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.or.ORModule;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORAttribute;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.generic.GenericModule;
import org.modelsphere.sms.or.ibm.IBMModule;
import org.modelsphere.sms.or.informix.INFModule;
import org.modelsphere.sms.or.oracle.OracleModule;
import org.modelsphere.sms.plugins.BuiltinPluginsInializer;
import org.modelsphere.sms.screen.SMSScreenPerspective;
import org.modelsphere.sms.screen.plugins.external.ORValidationRuleEditor;
import org.modelsphere.sms.services.ServiceList;

/**
 * The main class of ModelSphere. The main() method of this class launches a ModelSphere session.
 * 
 */
public class Application {
    private static final String kLoadingConfig = LocaleMgr.misc.getString("LoadingConfig");
    private static final String kLoadingModules = LocaleMgr.misc.getString("LoadingModules");
    private static final String kInit = LocaleMgr.misc.getString("Init");
    private static final String kInitModules = LocaleMgr.misc.getString("InitModules");
    private static final String kLoadingAppl = LocaleMgr.misc.getString("LoadingAppl");
    private static final String kSearchingPlugin = LocaleMgr.misc.getString("SearchingPlugins");

    private static final int BUILD_ID = 971;
    public static final int getBuildId() { return BUILD_ID; }

    static final String REPOSITORY_ROOT_NAME = "smsRoot";
    public static final String getRepositoryName() { return REPOSITORY_ROOT_NAME; }

    public static final Module[] MODULES = { SMSModule.getSingleton(), OOModule.getSingleton(),
            JavaModule.getSingleton(), ORModule.getSingleton(), GenericModule.getSingleton(),
            OracleModule.getSingleton(), IBMModule.getSingleton(), INFModule.getSingleton(),
            BEModule.getSingleton() };
    public static final String kInfoDialogTest = LocaleMgr.screen.getString("InfoDialogTest");
    public static final String kJVMNotSupported = LocaleMgr.message.getString("JVMNotSupported");

    /*
    public static void initApplicationContext() {
        ApplicationContext.APPLICATION_NAME = LocaleMgr.misc.getString("ApplicationName");
        ApplicationContext.APPLICATION_VERSION = LocaleMgr.misc.getString("ApplicationVersion");
        ApplicationContext.APPLICATION_BUILD_ID = BUILD_ID;
        ApplicationContext.APPLICATION_BUILD_ID_EXTENSION = LocaleMgr.misc.getString("ApplicationBuildExt");
        //Product and suite names are now language-independant (they stay the same for all languages).
        //This is necessary because the application folder is based on these names,
        //and this folder must stay the same notwithstanding the current locale. [MS]
        ApplicationContext.APPLICATION_IMAGE_ICON = Toolkit.getDefaultToolkit().getImage(
                LocaleMgr.misc.getUrl("ApplicationName"));
        ApplicationContext.REPOSITORY_ROOT_NAME = REPOSITORY_ROOT_NAME;
    }*/

    private SMSSplash splash = null;

    private Runnable initSplashRunnable = new Runnable() {

        @Override
        public void run() {
            try {
                Properties props = ApplicationContext.getCommandLineProperties(); 
                String value = props.getProperty(ApplicationContext.START_NO_SPLASH_PROPERTY);
                boolean splashNotVisible = Boolean.valueOf(value).booleanValue();
                if (splashNotVisible)
                    return;
                splash = new SMSSplash(null, true, true);
                splash.setVisible(true);
                splash.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                splash.toFront();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Runnable initFrameRunnable = new Runnable() {

        @Override
        public void run() {
            if (isStartupFailed()) {
                return;
            }

            try {
                MainFrame frame = MainFrame.getSingleton();
                DefaultMainFrame.registerInitialiser(configureFrameRunnable);

                // Load Plugins
                if (splash != null) {
                    splash.setGUIText(kSearchingPlugin);
                }

                if (isStartupFailed()) {
                    return;
                }

                PluginMgr pluginManager = PluginMgr.getSingleInstance();

                try {
                    Class[] builtIns = new Class[] { org.modelsphere.sms.plugins.jdbc.bridge.JdbcReverseToolkitPlugin.class,
                    /* org.modelsphere.sms.be.preview.BPMPreview.class */};
                    pluginManager.loadPlugins(splash, builtIns);

                    BuiltinPluginsInializer.initPlugins();
                } catch (Exception e) {
                    if (Debug.isDebug())
                        e.printStackTrace();
                }

                frame.getMainFrameMenu().initMenus();
                ToolsToolBar toolbar = (ToolsToolBar)frame.getInstallToolBar(MainFrame.TOOLS_TOOLBAR); 
                if (toolbar != null) {
                	toolbar.init();
                }
                
                try {
                    pluginManager.initPlugins(splash);
                } catch (Exception e) {
                    if (Debug.isDebug())
                        e.printStackTrace();
                } catch (Error e) {
                    if (Debug.isDebug())
                        e.printStackTrace();
                }

                if (isStartupFailed()) {
                    return;
                }

                frame.setVisible(true);
            } catch (Throwable th) {
                handle(th);
            }
        }
    };

    private Runnable configureFrameRunnable = new Runnable() {

        @Override
        public void run() {
            if (isStartupFailed()) {
                return;
            }

            try {
                // The frame has become visible at this point, ensure the splash is on top of it.
                if (splash != null)
                    splash.toFront();

                // Setting explorer and design panel visibility
                PropertiesSet appSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
                int visibility = appSet.getPropertyInteger(ExplorerPanel.class,
                        MainFrame.PROPERTY_VISIBILITY, new Integer(ExplorerPanel.EXPLORER_SINGLE))
                        .intValue();
                ApplicationContext.getDefaultMainFrame().setExplorerVisibility(visibility);

                boolean visible = appSet.getPropertyBoolean(DesignPanel.class,
                        MainFrame.PROPERTY_VISIBILITY, Boolean.TRUE).booleanValue();
                ApplicationContext.getDefaultMainFrame().setDesignPanelVisible(visible);

                // Setting magnifier and overview visibility
                visible = appSet.getPropertyBoolean(MagnifierInternalFrame.class,
                        MainFrame.PROPERTY_VISIBILITY, Boolean.FALSE).booleanValue();
                if (visible) {
                    ShowAbstractAction action = (ShowAbstractAction) ApplicationContext
                            .getActionStore().getAction(
                                    SMSActionsStore.APPLICATION_SHOW_MAGNIFIER_WINDOW);
                    action.performAction();
                }
                visible = appSet.getPropertyBoolean(OverviewInternalFrame.class,
                        MainFrame.PROPERTY_VISIBILITY, Boolean.FALSE).booleanValue();
                if (visible) {
                    ShowAbstractAction action = (ShowAbstractAction) ApplicationContext
                            .getActionStore().getAction(
                                    SMSActionsStore.APPLICATION_SHOW_OVERVIEW_WINDOW);
                    action.performAction();
                }

                // GP: Does not work - bug sun ID 4529206
                //ToolBarManager.restoreFloatingState();

                // Set the actual service list
                ServiceList serviceList = new ServiceList();
                ServiceProtocolList.setActualServiceList(serviceList);

                // Init these actions scope
                SelectAllLabelsAction labelsAction = (SelectAllLabelsAction) ApplicationContext
                        .getActionStore().getAction(SelectAllLabelsAction.class);
                SelectAllLabelsAction.setExcludedModels(new Class[] { DbORDomainModel.class,
                        DbORCommonItemModel.class });

                for (int i = 0; i < MODULES.length; i++) {
                    MODULES[i].installListeners();
                }
                
                Properties props = ApplicationContext.getCommandLineProperties(); 
                
                if (props.getProperty(ApplicationContext.START_OPEN_FILE_PROPERTY) != null)
                    ApplicationContext.getDefaultMainFrame().doOpenFromFile(
                            props.getProperty(ApplicationContext.START_OPEN_FILE_PROPERTY));

                ToolBarManager.initDialogToolBars();

                // post on EDT to ensure the frame has been validated and repainted.
                SwingUtilities.invokeLater(restoreWorkspaceRunnable);
            } catch (Throwable th) {
                handle(th);
            }
        }
    };

    private Runnable restoreWorkspaceRunnable = new Runnable() {

        @Override
        public void run() {
            if (isStartupFailed()) {
                return;
            }
            if (splash != null) {
                splash.setGUIText("Restoring Workspace...");
            }
            Component rootComponent = ApplicationContext.getDefaultMainFrame().getRootPane();
            rootComponent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            ContextManager.getInstance().init();
            rootComponent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            if (splash != null) {
                splash.dispose();
                splash = null;
            }
            //Show Startup Dialogs
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    showStartupDialogs();
                }
            });
        }
    };

    public Application(String[] args) {
        try {
            //Set application name and icon here
            SMSScreenPerspective.setApplicationNameAndSymbols();
            ApplicationContext.processCommandLineArguments(args);

            checkJavaCompatibility();
            
            //Use the system L&F (Windows L&F usually)
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                if (Debug.isDebug())
                    e.printStackTrace();
            }
            
            //TODO (in development) 
            //Check if we should ask the user to start in safe mode
            SafeMode.checkSafeMode(); //user can exit here
            
            // Init ScreenPlugins path for screen plugins
            System.setProperty("sr_application", "Application"); // NOT LOCALIZABLE
            ScreenPlugins
                    .setAuxiliaryPackages(new String[] { "org.modelsphere.sms.screen.plugins" }); // NOT LOCALIZABLE

            SwingUtilities.invokeAndWait(initSplashRunnable);

            if (splash != null)
                splash.setGUIText(kLoadingConfig);

            SMSTerminologyUtil.initInstance();
            SMSTerminologyManager.initInstance();

            PropertiesManager.setVersion(BUILD_ID);
            PropertiesManager.setPropertiesConverter(new SMSPropertiesConverter());
            PropertiesManager.installDefaultPropertiesSet();

            if (splash != null)
                splash.setGUIText(kLoadingModules);
            for (int i = 0; i < MODULES.length; i++) {
                MODULES[i].initModule();
            }

            if (splash != null)
                splash.setGUIText(kInit);
            initMeta();

            // here was the code for the login

            if (splash != null)
                splash.setGUIText(kInitModules);

            new SMSSemanticalIntegrity();
            for (int i = 0; i < MODULES.length; i++) {
                MODULES[i].initIntegrity();
            }

            ApplicationContext.setSemanticalModel(new SMSSemanticalModel());

            // Available tooltips for DbObject (only works in explorer for now)
            MetaField[] tooltipsFields = new MetaField[] { DbSemanticalObject.fPhysicalName,
                    DbSemanticalObject.fAlias, DbSMSSemanticalObject.fUmlStereotype,
                    DbOOClass.fAbstract, DbJVClass.fVisibility, DbJVClass.fStatic,
                    DbJVClass.fFinal, DbOODataMember.fType, DbOODataMember.fStatic,
                    DbOOAbstractMethod.fVisibility, DbORAttribute.fType, DbORAttribute.fLength,
                    DbORAttribute.fNbDecimal, DbORColumn.fNull, DbBEUseCase.fNumericIdentifier,
                    DbBEUseCase.fAlphanumericIdentifier, DbSemanticalObject.fDescription };
            DisplayToolTipsOptionGroup.setAvailableMetaFields(tooltipsFields);

            ScreenPlugins.installPlugin(DbORAssociationEnd.fInsertRule,
                    ORValidationRuleEditor.class.getPackage().getName(), "ORValidationRule");
            ScreenPlugins.installPlugin(DbORAssociationEnd.fUpdateRule,
                    ORValidationRuleEditor.class.getPackage().getName(), "ORValidationRule");
            ScreenPlugins.installPlugin(DbORAssociationEnd.fDeleteRule,
                    ORValidationRuleEditor.class.getPackage().getName(), "ORValidationRule");

            if (splash != null)
                splash.setGUIText(kLoadingAppl);

            // After the setVisible, any further initialization code must be posted
            //  to the event dispatch thread.
            //
            SwingUtilities.invokeLater(initFrameRunnable);
        } catch (Throwable th) {
            handle(th);
        }
    }

    private void checkJavaCompatibility() {
        if (ApplicationContext.getJavaVersion() == ApplicationContext.JVM_UNSUPORTED_RELEASE) {
            JOptionPane.showMessageDialog(NullFrame.singleton, kJVMNotSupported,
                    ApplicationContext.getApplicationName(), JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } else {
            Debug
                    .trace("JVM Version:  "
                            + (ApplicationContext.getJavaVersion() == ApplicationContext.JVM_BASE_RELEASE ? "Base version "
                                    : "Newer version"));
        }
    }

    public static void main(String[] args) {
        new Application(args);
    }

    private void showStartupDialogs() {

        ShowLicenseAction action = (ShowLicenseAction) SMSActionsStore.getSingleton().getAction(
                ShowLicenseAction.class);

        //Show licence, if required
        boolean showLicenceAtOpening = action.getShowLicenceAtStartupPreference();
        if (showLicenceAtOpening) {
            action.doActionPerformed();
        }

        //Display starting wizard, if required 
        boolean modelCreationAtOpening = ScreenPerspective.isFullVersion() && StartupWizardModel.getModelCreationWizardProperty();
        if (modelCreationAtOpening) {
            StartingWizardAction action2 = (StartingWizardAction) SMSActionsStore.getSingleton()
                    .getAction(StartingWizardAction.class);
            action2.doActionPerformed(true);
        }

    }

    // Made this method public because can be used in unit tests [MS]
    // Make this method static because no references to instance variables [MS]
    private static Boolean isInitialized = false; //initialized only once

    public static void initMeta() {
        synchronized (isInitialized) {
            if (!isInitialized) {
                SMSModule.initAll();
                org.modelsphere.jack.baseDb.db.ApplClasses.getFinalClasses();
                for (int i = 0; i < MODULES.length; i++) {
                    MODULES[i].loadMeta();
                }

                MetaClass.initMetaClasses();

                for (int i = 0; i < MODULES.length; i++) {
                    MODULES[i].initMeta();
                }

                isInitialized = true; //successful initialization
            }
        }
    }

    private boolean startupFailed = false;

    private void handle(Throwable th) {
        th.printStackTrace();

        setStartupFailed(true);

        //This is the last chance to catch an error : writing in English at that point
        //is acceptable, because a call to LocaleMgr can itself generate an error!
        //Anyway, the Throwable message will be in English [MS]
        String title = "The application failed to initialize."; //NOT LOCALIZABLE

        //if ExceptionInInitializerError, get the actual error
        if (th instanceof ExceptionInInitializerError) {
            ExceptionInInitializerError err = (ExceptionInInitializerError) th;
            th = err.getException();
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(outStream);
        th.printStackTrace(out);
        out.close();

        if (splash != null) {
            splash.dispose();
            splash = null;
        }

        // Create an instance of InfoDialog, with title and message specified
        final InfoDialog dialog = new InfoDialog(NullFrame.singleton, title, outStream.toString());
        AwtUtil.centerWindow(dialog);

        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                dialog.toFront();
            }
        });

        dialog.setVisible(true);
        System.exit(1);
    }

    private synchronized boolean isStartupFailed() {
        return startupFailed;
    }

    private synchronized void setStartupFailed(boolean startupFailed) {
        this.startupFailed = startupFailed;
    }

}
