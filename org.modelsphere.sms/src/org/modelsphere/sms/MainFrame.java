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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JComponent;

import org.modelsphere.jack.awt.StatusBar;
import org.modelsphere.jack.awt.ToolBarManager;
import org.modelsphere.jack.baseDb.assistant.JHtmlBallonHelp;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbRAM;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.VersionConverter;
import org.modelsphere.jack.baseDb.db.clipboard.Clipboard;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.screen.PropertiesFrame;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.graphic.tool.ToolButtonGroup;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.FocusManager;
import org.modelsphere.jack.srtool.explorer.ExplorerPanel;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.CascadingJInternalFrame;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.jack.srtool.list.ListDescriptor;
import org.modelsphere.jack.srtool.list.ListInternalFrame;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.srtool.screen.design.DesignPanel;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.be.QualifierToolBar;
import org.modelsphere.sms.be.ResourceToolBar;
import org.modelsphere.sms.be.db.DbBEStyle;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.features.DisplayRecentModifications;
import org.modelsphere.sms.graphic.tool.SMSGraphicalLinkTool;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOStyle;
import org.modelsphere.sms.or.db.DbORCommonItemStyle;
import org.modelsphere.sms.or.db.DbORDomainStyle;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORStyle;
import org.modelsphere.sms.preference.DisplayWindowsOptionGroup;
import org.modelsphere.sms.style.StyleFrame;

public class MainFrame extends DefaultMainFrame {
    private static final long SMS_FILE_CHECKWORD = 81500477737620267L;
    private static final String kModelFileExtension = ApplicationContext.MODEL_FILE_EXTENSION;
    private static final String kModelFileName = LocaleMgr.misc.getString("DefaultModel");
    private static final String kDefaultFileNameForNew = kModelFileName + kModelFileExtension;

    private static MainFrame singleton = null;
    private ExplorerPanel explorerPanel = null;
    private DesignPanel designPanel = null;
    private Clipboard applClipboard = new Clipboard();
    private ToolButtonGroup diagramsToolGroup = null;

    public static final int WINDOWS_SMALL = 0; // 640x480
    public static final int WINDOWS_MEDIUM = 1; // 800x600
    public static final int WINDOWS_LARGE = 2; // 1024x768
    public static final int WINDOWS_3_4_DESKTOPSIZE = 3; // 3/4 Desktop visible size
    public static final int WINDOWS_DESKTOPSIZE = 4; // Desktop visible size

    private static final int MINIMUM_BUILD_ID = 150; // If file build id < MINIMUM_BUILD_ID, it won't be loaded

    public static final String[] windowsSizePossibleValues = new String[] {
            LocaleMgr.misc.getString("Small"), LocaleMgr.misc.getString("Medium"),
            LocaleMgr.misc.getString("Large"), LocaleMgr.misc.getString("34AvailablaSpace"),
            LocaleMgr.misc.getString("DesktopSize") };

    // Keep current directory of file chooser (to start chooser at the same
    // directory that the last time) [MS]
    transient private String m_fileChooserCurrentDirectory = null;

    //Construct the frame
    private MainFrame() {
        //we must set the ApplicationContext before creating actions and explorers
        ApplicationContext.setDefaultMainFrame(this);
        ApplicationContext.setFocusManager(FocusManager.getSingleton());
        ApplicationContext.setActionStore(SMSActionsStore.getSingleton());
        ApplicationContext.setDragDrop(org.modelsphere.sms.SMSDragDrop.singleton);
        // Modules actions init
        for (int i = 0; i < Application.MODULES.length; i++) {
            Application.MODULES[i].initActions();
        }

        // Modules toolkit init
        for (int i = 0; i < Application.MODULES.length; i++) {
            Application.MODULES[i].initToolkits();
        }

        installToolBars();
        ApplicationContext
                .setApplPopupMenu(org.modelsphere.sms.popup.ApplicationPopupMenu.singleton);
        explorerPanel = new ExplorerPanel(new SMSExplorer());
        explorerPanel.setName("SemanticalExplorerPanel"); // NOT LOCALIZABLE  -  For QA
        setStatusBar(new StatusBar(new MainFrameStatusBarModel()));
        FocusManager.getSingleton().setMainFrame(this);
        setMainFrameMenu(new MainFrameMenu(this));

        // Init modules other toolbar
        for (int i = 0; i < Application.MODULES.length; i++) {
            Application.MODULES[i].initAndInstallOtherToolBars(this);
        }

        // Not used in 1.0 : installToolBar(new HelpToolBar(), DefaultMainFrame.HELP_TOOLBAR);
        // tracks Styles add/remove
        DbObject.fComposite.addDbRefreshListener(this);
        MetaField[][] optionGroups = StyleFrame.getOptionGroups(DbOOStyle.class,
                "oojv_optionGroups"); // NOT LOCALIZABLE
        for (int i = 0; i < optionGroups.length; i++)
            for (int j = 0; j < optionGroups[i].length; j++)
                optionGroups[i][j].addDbRefreshListener(this);

        MetaField[][] orOptionGroups = StyleFrame.getOptionGroups(DbORStyle.class,
                "or_optionGroups"); // NOT LOCALIZABLE
        for (int i = 0; i < orOptionGroups.length; i++)
            for (int j = 0; j < orOptionGroups[i].length; j++)
                orOptionGroups[i][j].addDbRefreshListener(this);

        MetaField[][] domainOptionGroups = StyleFrame.getOptionGroups(DbORDomainStyle.class,
                "domain_optionGroups"); // NOT LOCALIZABLE
        for (int i = 0; i < domainOptionGroups.length; i++)
            for (int j = 0; j < domainOptionGroups[i].length; j++)
                domainOptionGroups[i][j].addDbRefreshListener(this);

        MetaField[][] commonItemOptionGroups = StyleFrame.getOptionGroups(
                DbORCommonItemStyle.class, "commonItem_optionGroups"); // NOT LOCALIZABLE
        for (int i = 0; i < commonItemOptionGroups.length; i++)
            for (int j = 0; j < commonItemOptionGroups[i].length; j++)
                commonItemOptionGroups[i][j].addDbRefreshListener(this);

        MetaField[][] beOptionGroups = StyleFrame.getOptionGroups(DbBEStyle.class,
                "be_optionGroups"); // NOT LOCALIZABLE
        for (int i = 0; i < beOptionGroups.length; i++)
            for (int j = 0; j < beOptionGroups[i].length; j++)
                beOptionGroups[i][j].addDbRefreshListener(this);

    }

    public void installToolBars() {

        JComponent toolComponent = null;
        ArrayList creationComponents = new ArrayList();
        ArrayList drawingComponents = new ArrayList();
        ArrayList resourceComponents = new ArrayList();
        ArrayList resourceTools = new ArrayList();
        ArrayList qualifierComponents = new ArrayList();
        ArrayList qualifierTools = new ArrayList();
        diagramsToolGroup = new ToolButtonGroup();
        Tool[] tools = SMSToolkit.getTools();
        for (int i = 0; i < tools.length; i++) {
            toolComponent = diagramsToolGroup.addTool(tools[i], i == 0);
            if (tools[i].getToolBar() == null){
                if(tools[i].getClass().equals(SMSGraphicalLinkTool.class) && !ScreenPerspective.isFullVersion())
            		continue;
                creationComponents.add(toolComponent);
            }
            else if (tools[i].getToolBar().equals(CREATION_TOOLBAR))
                creationComponents.add(toolComponent);
            else if (tools[i].getToolBar().equals(DRAWING_TOOLBAR))
                drawingComponents.add(toolComponent);
            else if (tools[i].getToolBar().equals(ResourceToolBar.KEY)) {
                resourceComponents.add(toolComponent);
                resourceTools.add(tools[i]);
            } else if (tools[i].getToolBar().equals(QualifierToolBar.KEY)) {
                qualifierComponents.add(toolComponent);
                qualifierTools.add(tools[i]);
            }
        }

        installToolBar(new FileToolBar(), DefaultMainFrame.FILE_TOOLBAR,
                ToolBarManager.INSTALL_IN_DEFAULT_CONTAINER);
        installToolBar(new EditToolBar(), DefaultMainFrame.EDIT_TOOLBAR,
                ToolBarManager.INSTALL_IN_DEFAULT_CONTAINER);
        installToolBar(new DisplayToolBar(), DefaultMainFrame.DISPLAY_TOOLBAR,
                ToolBarManager.INSTALL_IN_DEFAULT_CONTAINER);
        installToolBar(new FormatToolBar(), DefaultMainFrame.FORMAT_TOOLBAR,
                ToolBarManager.INSTALL_IN_DEFAULT_CONTAINER);
        
        if (ScreenPerspective.isFullVersion()) {
        	installToolBar(new ToolsToolBar(), DefaultMainFrame.TOOLS_TOOLBAR,
                ToolBarManager.INSTALL_IN_DEFAULT_CONTAINER);
        }
        
        installToolBar(new NavigationToolBar(), DefaultMainFrame.NAVIGATION_TOOLBAR,
            ToolBarManager.INSTALL_IN_DEFAULT_CONTAINER);
        
        installToolBar(new CreationToolBar(creationComponents, diagramsToolGroup),
            DefaultMainFrame.CREATION_TOOLBAR, ToolBarManager.INSTALL_IN_DEFAULT_CONTAINER);

        installToolBar(new DrawingToolBar(drawingComponents, diagramsToolGroup),
                DefaultMainFrame.DRAWING_TOOLBAR, ToolBarManager.INSTALL_IN_DEFAULT_CONTAINER);
        
        if (ScreenPerspective.isFullVersion()) {
        	installToolBar(new ModifiersToolBar(), DefaultMainFrame.MODIFIER_TOOLBAR,
                ToolBarManager.INSTALL_IN_DEFAULT_CONTAINER);
        	installToolBar(new ResourceToolBar(resourceTools, resourceComponents, diagramsToolGroup),
                ResourceToolBar.KEY, ToolBarManager.INSTALL_IN_DIALOG);
        	installToolBar( new QualifierToolBar(qualifierTools, qualifierComponents, diagramsToolGroup),
                QualifierToolBar.KEY, ToolBarManager.INSTALL_IN_DIALOG);
        } //end if
    } //end installToolBars()

    public final static MainFrame getSingleton() {
        if (singleton == null) {
            singleton = new MainFrame();
        }
        return singleton;
    }

    public final ToolButtonGroup getDiagramsToolGroup() {
        return diagramsToolGroup;
    }

    public final Clipboard getClipboard() {
        return applClipboard;
    }

    public final ExplorerPanel getExplorerPanel() {
        return explorerPanel;
    }

    protected final DiagramInternalFrame createDiagramInternalFrame(DbObject diag)
            throws DbException {
        if (diag instanceof DbSMSDiagram) {
            DbSemanticalObject so = (DbSemanticalObject) diag.getComposite();
            SMSToolkit kit = SMSToolkit.getToolkit(so);
            DiagramInternalFrame frame = new DiagramInternalFrame(new ApplicationDiagram(so, diag,
                    kit.createGraphicalComponentFactory(), diagramsToolGroup));
            setInternalFrameBounds(frame, DisplayWindowsOptionGroup.getDiagramFrameDefaultSize());
            return frame;
        } else {
            return null;
        }
    }

    // Return null if this object cannot have a properties frame
    protected final PropertiesFrame createPropertiesFrame(DbObject obj) throws DbException {
        if (supportsPropertiesFrame(obj)) {
            PropertiesFrame frame = new org.modelsphere.sms.screen.SMSPropertiesFrame(obj);
            setInternalFrameBounds(frame, DisplayWindowsOptionGroup.getPropertyFrameDefaultSize());
            return frame;
        }
        return null;
    }

    protected final ListInternalFrame createListFrame(DbObject root, ListDescriptor descriptor)
            throws DbException {
        ListInternalFrame frame = new ListInternalFrame(ApplicationContext.getSemanticalModel()
                .createListTableModel(root, descriptor), descriptor.HasRelationship());
        setInternalFrameBounds(frame, DisplayWindowsOptionGroup.getPropertyFrameDefaultSize());
        return frame;
    }

    public final Dimension getDefaultInternalFrameSize() {
        return getDefaultInternalFrameSize(DisplayWindowsOptionGroup.getPropertyFrameDefaultSize());
    }

    private final Dimension getDefaultInternalFrameSize(int sizeoption) {
        Dimension desktopsize = getJDesktopPane().getSize();
        switch (sizeoption) {
        case WINDOWS_MEDIUM:
            return new Dimension(720, 520);
        case WINDOWS_LARGE:
            return new Dimension(900, 650);
        case WINDOWS_DESKTOPSIZE:
            return new Dimension(Math.max(desktopsize.width, 300), Math
                    .max(desktopsize.height, 200));
        case WINDOWS_SMALL:
            return new Dimension(540, 350);
        default: // WINDOWS_3_4_DESKTOPSIZE:
            return new Dimension(Math.max((desktopsize.width * 75) / 100, 350), Math.max(
                    (desktopsize.height * 75) / 100, 250));
        }
    }

    private void setInternalFrameBounds(CascadingJInternalFrame frame, int sizeoption) {
        Rectangle bounds = frame.getBounds();
        frame.setInitialOptionSize(sizeoption);
        Dimension size = getDefaultInternalFrameSize(sizeoption);

        switch (sizeoption) {
        case WINDOWS_MEDIUM:
            frame.setBounds(bounds.x, bounds.y, size.width, size.height);
            break;
        case WINDOWS_LARGE:
            frame.setBounds(bounds.x, bounds.y, size.width, size.height);
            break;
        case WINDOWS_DESKTOPSIZE:
            frame.setBounds(0, 0, size.width, size.height);
            break;
        case WINDOWS_SMALL:
            frame.setBounds(bounds.x, bounds.y, size.width, size.height);
            break;
        default: // WINDOWS_3_4_DESKTOPSIZE:
            frame.setBounds(bounds.x, bounds.y, size.width, size.height);
            break;
        }
    }

    public final boolean supportsPropertiesFrame(DbObject obj) throws DbException {
        return !(obj instanceof DbSMSDiagram || obj instanceof DbSMSGraphicalObject || obj instanceof DbORFKeyColumn);
    }

    /////////////////////////////////////////
    // abstract methods from DefaultMainFrame
    //
    // @returns null on failure
    public final DbProject createProject(Db db) {
        DbSMSProject project = null;

        try {
            ApplicationContext.getFocusManager().setNullProject();
            db.beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("newProject"));
            project = new DbSMSProject(db.getRoot());
            db.commitTrans();

            if (db instanceof DbRAM) {
                db.resetHistory();
                project.setLastSaveTrans(db.getTransCount());
            }

        } catch (DbException ex) {
            DefaultMainFrame fm = ApplicationContext.getDefaultMainFrame();
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(fm, ex);
        } //end try

        return project;
    }

    protected int getMinimumBuild() {
        return MINIMUM_BUILD_ID;
    }

    protected long getFileMagicNumber() {
        return SMS_FILE_CHECKWORD;
    }

    protected final JHtmlBallonHelp getJHtmlBallonHelp() {
        return null;//new SMSAssistant();
    }

    protected final VersionConverter getVersionConverter() {
        return new SMSVersionConverter();
    }

    protected final void convertForDebugUpdate(DbProject project, int version, int build)
            throws DbException {
        //getVersionConverter().convertStyles(project);
    }

    protected final String getModelFileExtension() {
        return kModelFileExtension;
    }

    protected final String getDefaultFileNameForNew() {
        return kDefaultFileNameForNew;
    }

    public final boolean isHelpInstalled() {
        return false;//SMSAssistant.getHelpDirectory() != null;
    }

    @Override
    public boolean saveCurrentProject(DbProject selProject, boolean saveAs) {
        if (selProject instanceof DbSMSProject) {
            DbSMSProject proj = (DbSMSProject) selProject;
            DisplayRecentModifications.notifySaveProject(proj);
        }

        //call the super
        boolean saved = super.saveCurrentProject(selProject, saveAs);
        return saved;
    }

    //Entry point to open a XML-formatted file
    public void doOpenFromXmlFile(Db db, String filename) {
        /*
         * TODO XmlFileOpener openFile = XmlFileOpener.getSingleton(); openFile.doOpenFile(db,
         * filename);
         */
    } //end doOpenFromXmlFile()

    //
    // End of abstract methods from DefautlMainFrame
    //////////////////////////////////////////////////////
}
