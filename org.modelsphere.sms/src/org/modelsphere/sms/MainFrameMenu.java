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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.awt.JackMenu;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.desktop.JInternalFrameManagerMenu;
import org.modelsphere.jack.plugins.Plugin;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.jack.plugins.PluginsRegistry;
import org.modelsphere.jack.srtool.actions.PluginDefaultAction;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.be.actions.BEActionConstants;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.actions.OOActionConstants;
import org.modelsphere.sms.oo.actions.ReverseAction;
import org.modelsphere.sms.oo.java.actions.JavaActionConstants;
import org.modelsphere.sms.or.actions.ORActionConstants;

public class MainFrameMenu extends org.modelsphere.jack.srtool.MainFrameMenu {
	//locale-independent keys
	private static final String REOPEN = "reopen"; // NOT LOCALIZABLE - Property Key
	private static final String TOOLBARS = "toolbars"; // NOT LOCALIZABLE - Property Key
	private static final String IMPORT_FROM = "importFrom"; // NOT LOCALIZABLE - Property Key
	private static final String EXPORT_TO = "exportTo"; // NOT LOCALIZABLE - Property Key
	private static final String DIAGRAM = "diagram"; // NOT LOCALIZABLE - Property Key
	private static final String SELECT = "select"; // NOT LOCALIZABLE - Property Key
	private static final String GRID = "grid"; // NOT LOCALIZABLE - Property Key
	private static final String DATA_MODEL = "dataModel"; // NOT LOCALIZABLE - Property Key
	private static final String DATABASE = "database"; // NOT LOCALIZABLE - Property Key
	private static final String BPM = "bpm"; // NOT LOCALIZABLE - Property Key
	private static final String GENERATE = "generate"; // NOT LOCALIZABLE - Property Key
	private static final String JAVA = "java"; // NOT LOCALIZABLE - Property Key
	private static final String ADVANCED = "advanced"; // NOT LOCALIZABLE - Property Key
	
    // provided to override default action name for their respective plugins
    private static final String kGenerateJava = LocaleMgr.action.getString("GenerateJava");
    private static final Integer kGenJavaMnemonic = new Integer(LocaleMgr.action
            .getMnemonic("GenerateJava"));
    private static final String kGenerateXMI = LocaleMgr.action.getString("GenerateXMI");
    private static final Integer kGenXMIMnemonic = new Integer(LocaleMgr.action
            .getMnemonic("GenerateXMI"));
    private static final String kGenerate = LocaleMgr.misc.getString("Generate");
    private static final String kDataModel = LocaleMgr.misc.getString("DataModel");
    private static final String kSelect = LocaleMgr.misc.getString("Select");
    private static final String kDatabase = LocaleMgr.misc.getString("Database");
    private static final String kJavaXMI = LocaleMgr.misc.getString("JavaXMI");
    private static final String kImportFrom = LocaleMgr.misc.getString("ImportFrom");
    private static final String kExportTo = LocaleMgr.misc.getString("ExportTo");
    private static final String kReopen = LocaleMgr.misc.getString("Reopen");
    private static final String kDiagram = LocaleMgr.misc.getString("diagram");
    private static final String kAdvanced = LocaleMgr.screen.getString("Advanced");
    private static final String kBusinessProcess = org.modelsphere.sms.be.international.LocaleMgr.misc
            .getString("bpmmodel");
    private static final String kGenerateProcessTree = org.modelsphere.sms.be.international.LocaleMgr.misc
            .getString("generateUpdateProcessTree");
    private static final Integer kGenProcessTreeMnemonic = new Integer(0);
    private static final String kValidateBPM = LocaleMgr.action.getString("Validate") + "...";
    private static final Integer kValidateMnemonic = new Integer(LocaleMgr.action
            .getMnemonic("Validate"));
    private static final String kCleanUpModel = LocaleMgr.action.getString("CleanUpModel");
    private static final Integer kCleanUpModelMnemonic = new Integer(LocaleMgr.action
            .getMnemonic("CleanUpModel"));
    private static final Icon kCleanUpMdelIcon = LocaleMgr.action.getImageIcon("CleanUpModel");
    private static final String kNew = LocaleMgr.screen.getString("new");

    private static final String kGrid = LocaleMgr.action.getString("Grid");

    transient private DbProject currentProject;
    transient JInternalFrameManagerMenu menuWindow;
    transient JackMenu toolBarMenu;
    String kFile = LocaleMgr.action.getString("File");
    String kEdit = LocaleMgr.action.getString("Edit");
    String kDisplay = LocaleMgr.action.getString("DisplayAsNoun");
    String kFormat = LocaleMgr.action.getString("Format");
    String kTools = LocaleMgr.action.getString("Tools");
    String kHelp = org.modelsphere.jack.srtool.international.LocaleMgr.action.getString("help");
    String kDebug = LocaleMgr.action.getString("Debug");
    char[] kMnemoChar = new char[] { LocaleMgr.action.getMnemonic("File"),
            LocaleMgr.action.getMnemonic("Edit"), LocaleMgr.action.getMnemonic("DisplayAsNoun"),
            LocaleMgr.action.getMnemonic("Format"), LocaleMgr.action.getMnemonic("Tools"),
            LocaleMgr.action.getMnemonic("help"), LocaleMgr.action.getMnemonic("Debug") };
    String kMnemonic = new String();
    JackMenu reopenSubMenu = new JackMenu(REOPEN);

    private JackMenu m_subMenuJavaXMI;

    public JackMenu getMenuJavaXMI() {
        return m_subMenuJavaXMI;
    }

    public MainFrameMenu(MainFrame newMf) {
        super(newMf);

    }

    public void initMenus() {
        toolBarMenu = new JackMenu(TOOLBARS);

        JackMenu menuFile = createDefaultFileMenu();
        JackMenu menuEdit = createDefaultEditMenu();
        JackMenu menuDisplay = createDefaultDisplayMenu();
        JackMenu menuFormat = createDefaultFormatMenu();
        JackMenu menuTools = createDefaultToolMenu();
        menuWindow = new JInternalFrameManagerMenu(getDefaultMainFrame().getJDesktopPane());
        JackMenu menuHelp = createDefaultHelpMenu();

        setMenuForRecentFile(reopenSubMenu);
        // setMenuForRecentFile(menuFile);
        updateRecentFiles(); // add the Recent Files menu Item

        // MENUBAR
        addMenuToMenuBar(menuFile, MENU_FILE);
        addMenuToMenuBar(menuEdit, MENU_EDIT);
        addMenuToMenuBar(menuDisplay, MENU_DISPLAY);
        addMenuToMenuBar(menuFormat, MENU_FORMAT);
        // addMenuToMenuBar(menuTarget, null);
        addMenuToMenuBar(menuTools, MENU_TOOLS);
        addMenuToMenuBar(menuWindow, null);
        addMenuToMenuBar(menuHelp, MENU_HELP);

        if (org.modelsphere.jack.debug.Debug.isDebug()) {
            JackMenu menuDebug = createDefaultDebugMenu();
            getMenuBar().add(Box.createHorizontalGlue());
            addMenuToMenuBar(menuDebug, MENU_DEBUG);
        }

        menuDisplay.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                updateMenuToolBar();
            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }
        });
    }

    protected JackMenu createDefaultFileMenu() {
        JackMenu menuFile;
        SMSActionsStore as = SMSActionsStore.getSingleton();

        JackMenu importFromSubMenu = new JackMenu(IMPORT_FROM);
        importFromSubMenu.setVisible(false);
        AbstractApplicationAction action = as.getAction(SMSActionsStore.IMPORT_FROM_XML); 
        if (action.isVisible()) {
            importFromSubMenu.add(action);
            importFromSubMenu.setVisible(true);
        }

        JackMenu exportToSubMenu = new JackMenu(EXPORT_TO);
        exportToSubMenu.setVisible(false);
        action = as.getAction(SMSActionsStore.EXPORT_TO_XML);
        if (action.isVisible()) {
            exportToSubMenu.add(action);
            exportToSubMenu.setVisible(true);
        }

        Object[] objects = {
                kFile,
                String.copyValueOf(kMnemoChar, 0, 1),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.PROJECT_NEW),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.STARTING_WIZARD),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.PROJECT_OPEN),
                reopenSubMenu,
                kReopen,
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.PROJECT_CLOSE),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.APPLICATION_CLOSE_ALL),
                null, // separator
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.PROJECT_SAVE),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.PROJECT_SAVE_AS),
                null, // separator
                importFromSubMenu, kImportFrom, exportToSubMenu,
                kExportTo,
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.SAVE_DIAGRAM),
                null, // separator
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.PROJECT_PAGE_SETUP),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.PROJECT_PRINT),
                null, // separator,
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.PROJECT_PROPERTIES),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.PROJECT_UDF), null, // separator,
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.APPLICATION_EXIT) };

        menuFile = MainFrameMenu.createMenu(MENU_FILE, objects);
        importFromSubMenu.setMnemonic(LocaleMgr.misc.getMnemonic("ImportFrom"));
        exportToSubMenu.setMnemonic(LocaleMgr.misc.getMnemonic("ExportTo"));
        return menuFile;
    }

    protected JackMenu createDefaultEditMenu() {
        JackMenu subMenuDiagram = new JackMenu(DIAGRAM);
        SMSActionsStore actionStore = SMSActionsStore.getSingleton();
        subMenuDiagram.add(actionStore.getAction(SMSActionsStore.SET_PAGE_NUMBER));
        subMenuDiagram.add(SMSActionsStore.getSingleton().getAction(
                SMSActionsStore.SET_DRAWING_AREA));
        subMenuDiagram.addSeparator();
        subMenuDiagram.add(actionStore.getAction(SMSActionsStore.LAYOUT_DIAGRAM));
        subMenuDiagram.addSeparator();
        subMenuDiagram.add(SMSActionsStore.getSingleton().getAction(
                SMSActionsStore.CREATE_MISSING_GRAPHICS));
 
        JackMenu subMenuSelect = new JackMenu(SELECT);
        subMenuSelect.setText(kSelect);
        subMenuSelect.add(actionStore.getAction(SMSActionsStore.SELECT_ALL));
        subMenuSelect.add(actionStore.getAction(SMSActionsStore.SELECT_CLASSES));
        subMenuSelect.add(actionStore.getAction(BEActionConstants.BE_SELECT_ALL_ACTORS));
        subMenuSelect.add(actionStore.getAction(BEActionConstants.BE_SELECT_ALL_STORES));
        subMenuSelect.add(actionStore.getAction(SMSActionsStore.SELECT_LINES));
        subMenuSelect.add(actionStore.getAction(SMSActionsStore.SELECT_LABELS));

        Object[] objects = {
                kEdit,
                String.copyValueOf(kMnemoChar, 1, 1),
                actionStore.getAction(SMSActionsStore.PROJECT_UNDO),
                actionStore.getAction(SMSActionsStore.PROJECT_REDO),
                null, // separator
                actionStore.getAction(SMSActionsStore.ADD),
                actionStore.getAction(SMSActionsStore.COPY),
                actionStore.getAction(SMSActionsStore.COPY_IMAGE),
                actionStore.getAction(SMSActionsStore.PASTE),
                actionStore.getAction(SMSActionsStore.PASTE_DIAGRAM_IMAGE),
                actionStore.getAction(SMSActionsStore.DELETE),
                actionStore.getAction(SMSActionsStore.REMOVE_GRAPHIC),
                actionStore.getAction(SMSActionsStore.INSERT_DIAGRAM_IMAGE),
                actionStore.getAction(SMSActionsStore.MOVE),
                null, // separator
                actionStore.getAction(SMSActionsStore.ALIGN_ZONE_BOX),
                actionStore.getAction(SMSActionsStore.STRAIGHTEN),
                actionStore.getAction(SMSActionsStore.REPOSITION_LABEL),
                actionStore.getAction(SMSActionsStore.AUTO_FIT),
                actionStore.getAction(SMSActionsStore.FIT),
                actionStore.getAction(SMSActionsStore.REVERT_TO_ORIGINAL_SIZE),
                null, // separator
                subMenuDiagram,
                kDiagram,
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.SEND_TO_DIAGRAM),
                null, // separator
                subMenuSelect,
                kSelect,
                null, // separator
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.PROJECT_FIND),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.PROJECT_FIND_NEXT),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.PROJECT_FIND_PREVIOUS),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.FIND_IN_DIAGRAM),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.FIND_IN_EXPLORER), };

        JackMenu menu = MainFrameMenu.createMenu(MENU_EDIT, objects);
        subMenuSelect.setMnemonic(LocaleMgr.misc.getMnemonic("Select"));
        return menu;
    }

    protected JackMenu createDefaultDisplayMenu() {
        // JackMenu subMenuDiagram = new JackMenu();
        // subMenuDiagram.add(SMSActionsStore.getSingleton().getAction(SMSActionsStore.SHOW_DIAGRAM));
        // subMenuDiagram.add(SMSActionsStore.getSingleton().getAction(SMSActionsStore.PAGE_BREAKS));

        SMSActionsStore actionStore = SMSActionsStore.getSingleton();

        JackMenu subMenuGrid = new JackMenu(GRID);
        subMenuGrid.add(SMSActionsStore.getSingleton().getAction(
                SMSActionsStore.APPLICATION_SHOW_GRID));
        subMenuGrid.add(SMSActionsStore.getSingleton().getAction(
                SMSActionsStore.APPLICATION_ACTIVATE_GRID));
        subMenuGrid.addSeparator();
        subMenuGrid.add(SMSActionsStore.getSingleton().getAction(
                SMSActionsStore.APPLICATION_CONFIGURE_GRID));

        Object[] objects = {
                kDisplay,
                String.copyValueOf(kMnemoChar, 2, 1),
                toolBarMenu,
                LocaleMgr.action.getString("ToolBars"),
                actionStore.getAction(SMSActionsStore.APPLICATION_SHOW_EXPLORER),
                actionStore.getAction(SMSActionsStore.APPLICATION_SHOW_DESIGN_PANEL),
                // actionStore.getAction(SMSActionsStore.APPLICATION_SHOW_TOOLBAR),
                // LocaleMgr.action.getString("TShowHideToolBar").charAt(0),
                actionStore.getAction(SMSActionsStore.APPLICATION_SHOW_STATUSBAR),
                subMenuGrid,
                kGrid,
                null, // separator
                // subMenuDiagram, kDiagram,
                actionStore.getAction(SMSActionsStore.SHOW_DIAGRAM),
                actionStore.getAction(SMSActionsStore.PAGE_BREAKS),
                null, // separator
                actionStore.getAction(SMSActionsStore.PROPERTIES),
                actionStore.getAction(SMSActionsStore.LIST),
                null, // separator
                // actionStore.getAction(SMSActionsStore.SHOW_DIAGRAM),
                actionStore.getAction(SMSActionsStore.APPLICATION_SHOW_OVERVIEW_WINDOW),
                actionStore.getAction(SMSActionsStore.APPLICATION_SHOW_MAGNIFIER_WINDOW),
                actionStore.getAction(SMSActionsStore.ZOOM_IN),
                actionStore.getAction(SMSActionsStore.ZOOM_OUT),
                null, // separator
                actionStore.getAction(SMSActionsStore.EXPAND_ALL),
                actionStore.getAction(SMSActionsStore.COLLAPSE_ALL),
                null, // separator
                actionStore.getAction(SMSActionsStore.APPLICATION_REFRESH),
                actionStore.getAction(SMSActionsStore.REFRESH_ALL), };
        JackMenu menu = MainFrameMenu.createMenu(MENU_DISPLAY, objects);
        toolBarMenu.setMnemonic(LocaleMgr.action.getMnemonic("ToolBars"));
        return menu;
    }

    public JackMenu createDefaultFormatMenu() {
        Object[] objects = {
                kFormat,
                String.copyValueOf(kMnemoChar, 3, 1),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.FORMAT),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.HIGHLIGHT),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.DASHLINE),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.COLOR_BORDER),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.COLOR_FILL),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.COLOR_TEXT),
                null, // separator
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.SET_GO_STYLE),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.SET_DIAGRAM_DEFAULT_STYLE),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.PROJECT_STYLE),
                null, // separator
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.SET_NOTATION),
                SMSActionsStore.getSingleton().getAction(
                        SMSActionsStore.SMS_SET_PROJECT_DEFAULT_OR_NOTATION),
                SMSActionsStore.getSingleton().getAction(
                        SMSActionsStore.SMS_SET_PROJECT_DEFAULT_ER_NOTATION),
                SMSActionsStore.getSingleton().getAction(
                        SMSActionsStore.SMS_SET_PROJECT_DEFAULT_BE_NOTATION),
                SMSActionsStore.getSingleton().getAction(SMSActionsStore.EDIT_NOTATIONS), };
        return MainFrameMenu.createMenu(MENU_FORMAT, objects);
    }

    public JackMenu createDefaultToolMenu() {

        //Data Model Sub Menu
        JackMenu subMenuDataModel = new JackMenu(DATA_MODEL);
        subMenuDataModel.setVisible(ScreenPerspective.isFullVersion());
        
        subMenuDataModel.add(SMSActionsStore.getSingleton().getAction(
                ORActionConstants.OR_SETUP_TARGET_SYSTEM));
        subMenuDataModel.addSeparator();
        subMenuDataModel.add(SMSActionsStore.getSingleton().getAction(
                ORActionConstants.OR_GENERATE_PRIMARY_KEYS));
        subMenuDataModel.add(SMSActionsStore.getSingleton().getAction(
                ORActionConstants.OR_GENERATE_FOREIGN_KEYS));
        subMenuDataModel.add(SMSActionsStore.getSingleton().getAction(
                ORActionConstants.OR_GENERATE_REFERENTIAL_RULES));
        subMenuDataModel.add(SMSActionsStore.getSingleton().getAction(
                ORActionConstants.OR_GENERATE_CLASS_MODEL));
        subMenuDataModel.add(SMSActionsStore.getSingleton().getAction(
                ORActionConstants.OR_DELETE_FOREIGN_KEYS));
        subMenuDataModel.addSeparator();
        subMenuDataModel.add(SMSActionsStore.getSingleton().getAction(
                ORActionConstants.OR_CONVERT_DATAMODEL_WORKMODE));

        //Database Sub Menu
        JackMenu subMenuDatabase = new JackMenu(DATABASE);
        subMenuDatabase.setVisible(ScreenPerspective.isFullVersion());
        
        subMenuDatabase.add(SMSActionsStore.getSingleton().getAction(SMSActionsStore.DB_CONNECT));
        subMenuDatabase.add(SMSActionsStore.getSingleton().getAction(
                SMSActionsStore.DB_CONNECTION_INFO));
        subMenuDatabase.addSeparator();
        subMenuDatabase.add(SMSActionsStore.getSingleton().getAction(
                ORActionConstants.OR_SYNCHRO_DB));
        subMenuDatabase.add(SMSActionsStore.getSingleton().getAction(
                ORActionConstants.OR_FORWARD_DATAMODEL));
        subMenuDatabase.add(SMSActionsStore.getSingleton().getAction(
                ORActionConstants.OR_REVERSE_DB));

        //Java Sub Menu
        m_subMenuJavaXMI = addJavaMenuItems();
        m_subMenuJavaXMI.setVisible(ScreenPerspective.isFullVersion());

        //Business Process Sub Menu
        JackMenu subMenuBPM = new JackMenu(BPM);
        subMenuBPM.setVisible(ScreenPerspective.isFullVersion());
        
        subMenuBPM.add(SMSActionsStore.getSingleton()
                .getAction(BEActionConstants.BE_RENUM_SETTINGS));
        subMenuBPM.addSeparator();
        subMenuBPM
                .add(SMSActionsStore.getSingleton().getAction(BEActionConstants.BE_MERGE_USECASE));
        subMenuBPM
                .add(SMSActionsStore.getSingleton().getAction(BEActionConstants.BE_SPLIT_USECASE));
        subMenuBPM.addSeparator();
        subMenuBPM.add(SMSActionsStore.getSingleton().getAction(
                BEActionConstants.BE_UPDATE_ENVIRONMENT_USECASE));
        //subMenuBPM.add(verifyIntegrityAction);

        //Generate Menu
        JackMenu subMenuGenerate = new JackMenu(GENERATE);
        subMenuGenerate.setVisible(ScreenPerspective.isFullVersion());
        
        subMenuGenerate.add(SMSActionsStore.getSingleton().getAction(
                ORActionConstants.OR_GENERATE_PHYSICAL_NAMES));
        subMenuGenerate.add(SMSActionsStore.getSingleton().getAction(
                ORActionConstants.OR_GENERATE_COMMON_ITEMS));
        subMenuGenerate.add(SMSActionsStore.getSingleton().getAction(
                ORActionConstants.CREATE_COMMON_ITEM_COLUMNS));
        subMenuGenerate.addSeparator();
        PluginAction action = createPluginAction(
                "org.modelsphere.sms.plugins.bpm.ProcessTreePlugin", kGenerateProcessTree,
                kGenProcessTreeMnemonic); // NOT LOCALIZABLE
        subMenuGenerate.add(action);
        subMenuGenerate
                .add(createPluginAction("org.modelsphere.sms.plugins.report.ReportGeneratorPlugin")); // NOT LOCALIZABLE
        subMenuGenerate.addSeparator();
        subMenuGenerate.add(SMSActionsStore.getSingleton().getAction(
                SMSActionsStore.GENERATE_FROM_TEMPLATES));

        //action = createPluginAction("org.modelsphere.sms.plugins.bpm.validation.BpmValidation", kValidateBPM, kValidateMnemonic);  // NOT LOCALIZABLE
        //subMenuBPM.add(action); 

        ArrayList<Object> tempObjects = new ArrayList<Object>();
        tempObjects.add(kTools);
        tempObjects.add(String.copyValueOf(kMnemoChar, 4, 1));

        //action = createPluginAction("org.modelsphere.sms.plugins.integrity.CleanUp");
        //tempObjects.add(action); // NOT LOCALIZABLE
        tempObjects.add(null); // separator
        tempObjects.add(subMenuDataModel);
        tempObjects.add(kDataModel);
        tempObjects.add(subMenuDatabase);
        tempObjects.add(kDatabase);
        tempObjects.add(m_subMenuJavaXMI);
        tempObjects.add(kJavaXMI);
        tempObjects.add(subMenuBPM);
        tempObjects.add(kBusinessProcess);
        tempObjects.add(subMenuGenerate);
        tempObjects.add(kGenerate);
        tempObjects.add(null); // separator

        //subMenuDataModel.addSeparator();
        action = createPluginAction("org.modelsphere.sms.plugins.integrity.IntegrityPlugin",
                kCleanUpModel, kCleanUpModelMnemonic, kCleanUpMdelIcon);// NOT LOCALIZABLE
        tempObjects.add(action);
        Action verifyIntegrityAction = createPluginAction("org.modelsphere.sms.plugins.integrity.IntegrityPlugin");// NOT LOCALIZABLE
        tempObjects.add(verifyIntegrityAction);
        tempObjects.add(null); // separator

        tempObjects.add(SMSActionsStore.getSingleton().getAction(
                ORActionConstants.OR_PROPAGATE_COMMON_ITEM_VALUES));
        tempObjects.add(SMSActionsStore.getSingleton().getAction(
                SMSActionsStore.CONSOLIDATE_DIAGRAMS));
        tempObjects.add(null); // separator

        tempObjects.add(SMSActionsStore.getSingleton().getAction(SMSActionsStore.INTEGRATE));
        tempObjects.add(SMSActionsStore.getSingleton().getAction(SMSActionsStore.CREATE_LINK));
        tempObjects.add(null); // separator
        tempObjects.add(SMSActionsStore.getSingleton().getAction(SMSActionsStore.PYTHON_SHELL));

        tempObjects.add(null); // separator
        tempObjects.add(SMSActionsStore.getSingleton().getAction(
                SMSActionsStore.APPLICATION_PREFERENCES));

        Object[] objects = tempObjects.toArray();
        JackMenu menu = MainFrameMenu.createMenu(MENU_TOOLS, objects);
        subMenuDataModel.setMnemonic(LocaleMgr.misc.getMnemonic("DataModel"));
        subMenuDatabase.setMnemonic(LocaleMgr.misc.getMnemonic("Database"));
        subMenuGenerate.setMnemonic(LocaleMgr.misc.getMnemonic("Generate"));
        m_subMenuJavaXMI.setMnemonic(LocaleMgr.misc.getMnemonic("JavaXMI"));
        updateMenuGenerateItems(subMenuGenerate);
        return menu;
    }

    private boolean m_JavaMenuSubItemsCreated = false;

    private JackMenu addJavaMenuItems() {
        final JackMenu subMenuJavaXMI = new JackMenu(JAVA);

        subMenuJavaXMI.addMenuListener(new MenuListener() {

            public void menuSelected(MenuEvent e) {
                if (!m_JavaMenuSubItemsCreated) {
                    SMSActionsStore actionStore = SMSActionsStore.getSingleton();
                    ReverseAction action = (ReverseAction) actionStore
                            .getAction(OOActionConstants.OO_PROJECT_REVERSE);
                    boolean visible = action.isVisible();
                    if (visible) {
                        if (!action.isReverseListEmpty()) {
                            subMenuJavaXMI.add(action);
                        }
                    }

                    AbstractApplicationAction action2 = actionStore
                            .getAction(OOActionConstants.OO_VALIDATE_FOR_JAVA);
                    visible = action2.isVisible();
                    if (visible) {
                        subMenuJavaXMI.add(action2);
                    }

                    subMenuJavaXMI.add(SMSActionsStore.getSingleton().getAction(
                            JavaActionConstants.JAVA_GENERATE_DATA_MODEL));

                    m_JavaMenuSubItemsCreated = true;
                } // end if
            } // end if

            public void menuCanceled(MenuEvent e) {
            }

            public void menuDeselected(MenuEvent e) {
            }
        });
        subMenuJavaXMI.add(createPluginAction(
                "com.grandite.sms.plugins.java.forward.JavaForwardEngineeringPlugin",
                kGenerateJava, kGenJavaMnemonic)); // NOT LOCALIZABLE
        return subMenuJavaXMI;
    }

    private void updateMenuGenerateItems(JMenu generateMenu) {
        if (generateMenu == null)
            return;
        // remove "Generete" from sub items
        String generateText = generateMenu.getText();
        Component[] items = generateMenu.getMenuComponents();
        for (int i = 0; items != null && i < items.length; i++) {
            if (!(items[i] instanceof JMenuItem))
                continue;
            JMenuItem subitem = (JMenuItem) items[i];
            String itemtext = subitem.getText();
            if (!itemtext.toLowerCase().startsWith(generateText.toLowerCase()))
                continue;
            String tmpStr = itemtext.substring(generateText.length(), itemtext.length()).trim();
            String resultStr = tmpStr.substring(0, 1).toUpperCase()
                    + tmpStr.substring(1, tmpStr.length());
            subitem.setText(resultStr);
        }
    }

    private PluginAction createPluginAction(String classname, String actionName, Integer mnemonic,
            Icon icon) {
        PluginAction action = null;
        try {
            // If no need to bypass default name, get the existing action for
            // this plugin. No need to
            // create a new one to provide a different name.
            if (actionName == null) {
                action = (PluginAction) SMSActionsStore.getSingleton().getAction(classname);
            }

            if (action == null) {
                PluginMgr mgr = PluginMgr.getSingleInstance();
                Class c = mgr.getPluginClass(classname);
                PluginsRegistry registry = mgr.getPluginsRegistry();
                ArrayList plugins = registry.getActivePluginInstances(c);
                if (plugins != null && plugins.size() > 0) {
                    action = new PluginDefaultAction((Plugin) plugins.get(0), actionName, mnemonic,
                            icon) {
                        protected int getFeatureSet() {
                            return SMSFilter.COMMON;
                        }
                    };
                }
            }
        } catch (Exception e) {
            action = null;
            if (Debug.isDebug())
                e.printStackTrace();
        } catch (Error error) {
            action = null;
            if (Debug.isDebug())
                error.printStackTrace();
        }
        return action;
    }

    private PluginAction createPluginAction(String classname) {
        return createPluginAction(classname, null, null, null);
    }

    private PluginAction createPluginAction(String classname, String actionName) {
        return createPluginAction(classname, actionName, null, null);
    }

    private PluginAction createPluginAction(String classname, String actionName, Integer mnemonic) {
        return createPluginAction(classname, actionName, mnemonic, null);
    }

    public JackMenu createDefaultHelpMenu() {

        Vector actionVector = new Vector();

        JackMenu subMenuAdvanced = new JackMenu(ADVANCED);
        subMenuAdvanced.add(SMSActionsStore.getSingleton().getAction(SMSActionsStore.SAFE_MODE));

        actionVector.add(kHelp);
        actionVector.add(String.copyValueOf(kMnemoChar, 5, 1));
        actionVector
                .add(SMSActionsStore.getSingleton().getAction(SMSActionsStore.APPLICATION_HELP));
        // Temporary commercial plugin Help items
        addComHelpPluginAction(actionVector);
        actionVector.add(null); // separator
        actionVector.add(SMSActionsStore.getSingleton().getAction(SMSActionsStore.PLUGIN_MGR));
        actionVector.add(SMSActionsStore.getSingleton().getAction(
                SMSActionsStore.APPLICATION_SHOW_TECH_INFO));
        actionVector.add(null); // separator
        actionVector.add(SMSActionsStore.getSingleton().getAction(
                SMSActionsStore.APPLICATION_LICENSE));
        actionVector.add(SMSActionsStore.getSingleton()
                .getAction(SMSActionsStore.APPLICATION_ABOUT));

        Object[] objects = actionVector.toArray();
        return MainFrameMenu.createMenu(MENU_HELP, objects);
    }

    private JackMenu createDefaultDebugMenu() {

        Vector actionVector = new Vector();

        // put in a vector to be able to do conditional inclusions
        actionVector.add(kDebug);
        actionVector.add(String.copyValueOf(kMnemoChar, 6, 1));
        actionVector.add(new org.modelsphere.jack.srtool.actions.DisplayDbViewerAction());
        actionVector.add(null);
        actionVector.add(new org.modelsphere.sms.actions.ShowMemoryInfo());
        actionVector.add(org.modelsphere.sms.actions.UpdateMemoryInfo.getSingleton());
        actionVector.add(org.modelsphere.sms.actions.AutoUpdateMemoryInfo.getSingleton());
        actionVector.add(null);
        actionVector.add(new org.modelsphere.jack.srtool.actions.LogAction());
        actionVector.add(new org.modelsphere.jack.srtool.actions.ShowLogDialogAction());
        actionVector.add(null);
        actionVector.add(new org.modelsphere.sms.actions.EditSynchroScopeAction());
        actionVector.add(new org.modelsphere.sms.actions.OfflineReverseDbAction());
        actionVector.add(new org.modelsphere.sms.actions.SpawnAction());
        actionVector.add(null);
        actionVector.add(new org.modelsphere.sms.or.actions.ChangeTargetSystemAction());
        Object[] objects = actionVector.toArray();

        JackMenu menu = MainFrameMenu.createMenu(MENU_DEBUG, objects);
        return menu;
    }

    private void updateMenuToolBar() {
        MainFrame.getSingleton().populateToolBarMenu(toolBarMenu);
    }

    private void addComHelpPluginAction(Vector actionVector) {
        String helpPluginPath = null;
        //Locale locale = LocaleMgr.getLocaleFromPreferences();

        String folderPath = System.getProperty("user.dir");
        folderPath = folderPath.concat("/plugins/bin/com");
        String filename = "ComPlugins.properties"; // NOT LOCALIZABLE
        File file = new File(folderPath, filename);
        try {
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while (line != null) {
                    if (line.startsWith("HelpDoc")) {
                        int i = line.indexOf("=");
                        helpPluginPath = "com." + line.substring(i + 1, line.length());
                        actionVector.add(createPluginAction(helpPluginPath));
                    }
                    line = reader.readLine();
                }
                reader.close();
            }
        } catch (Exception e) {
        }
    }
}
