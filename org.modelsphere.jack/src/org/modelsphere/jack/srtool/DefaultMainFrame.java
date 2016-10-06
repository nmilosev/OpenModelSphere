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

package org.modelsphere.jack.srtool;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DefaultDesktopManager;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.DesktopPaneUI;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.StatusBar;
import org.modelsphere.jack.awt.StatusBarModel;
import org.modelsphere.jack.awt.TextViewerDialog;
import org.modelsphere.jack.awt.TextViewerFrame;
import org.modelsphere.jack.awt.ToolBarManager;
import org.modelsphere.jack.awt.ToolBarPanel;
import org.modelsphere.jack.awt.themes.ThemeBank;
import org.modelsphere.jack.baseDb.assistant.JHtmlBallonHelp;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbLogin;
import org.modelsphere.jack.baseDb.db.DbLoginGroup;
import org.modelsphere.jack.baseDb.db.DbLoginNode;
import org.modelsphere.jack.baseDb.db.DbLoginUser;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbRAM;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbUDF;
import org.modelsphere.jack.baseDb.db.DbUDFValue;
import org.modelsphere.jack.baseDb.db.ReadOnly;
import org.modelsphere.jack.baseDb.db.VersionConverter;
import org.modelsphere.jack.baseDb.db.clipboard.Clipboard;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbRefreshPassListener;
import org.modelsphere.jack.baseDb.db.event.DbTransListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.DbDataEntryFrame;
import org.modelsphere.jack.baseDb.screen.DbDescriptionView;
import org.modelsphere.jack.baseDb.screen.DbListView;
import org.modelsphere.jack.baseDb.screen.LoginListView;
import org.modelsphere.jack.baseDb.screen.PropertiesFrame;
import org.modelsphere.jack.baseDb.screen.ScreenPlugins;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.screen.UDFListView;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.debug.Log;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.MagnifierView;
import org.modelsphere.jack.graphic.tool.ToolButtonGroup;
import org.modelsphere.jack.io.PathFile;
import org.modelsphere.jack.plugins.xml.extensions.OpenFileExtension;
import org.modelsphere.jack.plugins.xml.extensions.SaveFileExtension;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.preference.RecentFiles;
import org.modelsphere.jack.preference.context.ContextManager;
import org.modelsphere.jack.srtool.actions.FilesAbstractAction;
import org.modelsphere.jack.srtool.actions.ShowAbstractAction;
import org.modelsphere.jack.srtool.actions.ShowDesignPanelAction;
import org.modelsphere.jack.srtool.actions.ShowExplorerAction;
import org.modelsphere.jack.srtool.explorer.ExplorerPanel;
import org.modelsphere.jack.srtool.explorer.ExplorerView;
import org.modelsphere.jack.srtool.features.DbFindSession;
import org.modelsphere.jack.srtool.features.SafeMode;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.jack.srtool.graphic.DiagramViewInternalFrame;
import org.modelsphere.jack.srtool.graphic.MagnifierInternalFrame;
import org.modelsphere.jack.srtool.graphic.OverviewInternalFrame;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.list.ListDescriptor;
import org.modelsphere.jack.srtool.list.ListInternalFrame;
import org.modelsphere.jack.srtool.screen.design.DesignPanel;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.DataIntegrity;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.jack.util.SrVector;
import org.modelsphere.jack.util.StringUtil;

/**
 * If you application use the standard Silverun lookAndfeel, your mainfram should extends this
 * class, this class will manage the following aspect of an application: -Find -Explorer -Internal
 * Frames (PropertiesFrame, DiagramInternalFrame) -Open/Save -Exit/Close -Printing -lookAndFeel
 * -help (only the menu help management)
 */
@SuppressWarnings("serial")
public abstract class DefaultMainFrame extends JFrame implements DbRefreshListener,
        DbRefreshPassListener, DbTransListener {
    // DESKTOP LAYERS
    public static final Integer APPLICATION_DIAGRAM_LAYER = JLayeredPane.DEFAULT_LAYER;

    public static final Integer LIST_LAYER = JLayeredPane.DEFAULT_LAYER;

    public static final Integer PROPERTY_LAYER = JLayeredPane.DEFAULT_LAYER;

    public static final Integer HELP_LAYER = JLayeredPane.DEFAULT_LAYER;

    public static final Integer PALETTE_LAYER = JLayeredPane.PALETTE_LAYER;

    public static final Integer MODAL_LAYER = JLayeredPane.MODAL_LAYER;

    // Explorer Preference keys
    public static final String PROPERTY_X = "X"; //NOT LOCALIZABLE, property key

    public static final String PROPERTY_Y = "Y"; //NOT LOCALIZABLE, property key

    public static final String PROPERTY_WIDTH = "Width"; //NOT LOCALIZABLE, property key

    public static final String PROPERTY_HEIGHT = "Height"; //NOT LOCALIZABLE, property key

    public static final String PROPERTY_MAXIMIZED = "Maximized"; //NOT LOCALIZABLE,
    // property key

    public static final String PROPERTY_VISIBILITY = "Visibility"; //NOT LOCALIZABLE, property key

    public static final String COMPONENTS_HEADER_VISIBILITY = "ComponentsHeaderVisible"; // NOT LOCALIZABLE

    public static final Boolean COMPONENTS_HEADER_VISIBILITY_DEFAULT = Boolean.TRUE;

    // Component Layout
    public static final int LAYOUT_LEFT_RIGHT = 0;

    public static final int LAYOUT_LEFT_BOTTOM = 1;

    public static final int LAYOUT_LEFT_LEFT = 2;

    public static final String PROPERTY_LAYOUT = "layout"; //NOT LOCALIZABLE, property key

    public static final Integer PROPERTY_LAYOUT_DEFAULT = new Integer(LAYOUT_LEFT_LEFT);

    // Frame Preference keys
    public static final String STATUSBAR_PROPERTY = "StatusBar";//NOT LOCALIZABLE, property key

    private static final String APPLICATION_DIRECTORY = System.getProperty("user.dir"); //NOT LOCALIZABLE, property key

    private static final String kRepositoryLogin0 = LocaleMgr.screen.getString("repositoryLogin0");

    private static final String k0Members = LocaleMgr.screen.getString("0Members");

    private static final String k0Groups = LocaleMgr.screen.getString("0Groups");

    private static final String kCannotOpenBuild0 = LocaleMgr.message.getString("CannotOpenBuild0");

    private int waitCursorCount = 0;

    private String kAppNameModels = MessageFormat.format(LocaleMgr.screen.getString("0Models"),
            new Object[] { ApplicationContext.getApplicationName() });

    private ExtensionFileFilter smsFileFilter = new ExtensionFileFilter(getModelFileExtension(),
            kAppNameModels);

    // Keep current directory of file chooser (to start chooser at the same
    // directory that the last time) [MS]
    transient private String m_fileChooserCurrentDirectory = null;

    // nice windows
    private OverviewInternalFrame overviewFrame = null;

    private MagnifierInternalFrame magnifierFrame = null;

    private static List<Runnable> initialisers = new ArrayList<Runnable>();

    private List<Runnable> preFinalizers = new ArrayList<Runnable>();
    private List<Runnable> postFinalizers = new ArrayList<Runnable>();

    private DesktopPaneContext desktopPaneContext;

    private InternalFrameListener overviewMagnifierListener = new InternalFrameAdapter() {
        public void internalFrameClosing(InternalFrameEvent e) {
            if (e.getInternalFrame() == overviewFrame) {
                uninstallOverviewFrame();
                ShowAbstractAction action = (ShowAbstractAction) ApplicationContext
                        .getActionStore()
                        .get(AbstractActionsStore.APPLICATION_SHOW_OVERVIEW_WINDOW);
                action.performAction();
            } else if (e.getInternalFrame() == magnifierFrame) {
                uninstallMagnifierFrame();
                ShowAbstractAction action = (ShowAbstractAction) ApplicationContext
                        .getActionStore().get(
                                AbstractActionsStore.APPLICATION_SHOW_MAGNIFIER_WINDOW);
                action.performAction();
            }
        }
    };

    private PropertyChangeListener preferencesListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            String property = evt.getPropertyName();
            if (property == null)
                return;
            if (property.indexOf(MagnifierView.ZOOM_FACTOR_PROPERTY) > -1) {
                if (magnifierFrame != null && evt.getNewValue() != null
                        && (evt.getNewValue() instanceof Float)) {
                    magnifierFrame.setZoomFactor(((Float) evt.getNewValue()).floatValue());
                }
            } else if (property.indexOf(ApplicationContext.LF_PROPERTY) > -1
                    && evt.getNewValue() != null) {
                String theme = PropertiesManager.APPLICATION_PROPERTIES_SET.getPropertyString(
                        ApplicationContext.class, ApplicationContext.THEME_PROPERTY,
                        ThemeBank.themes[0].getName());
                setLookAndFeel((String) evt.getNewValue(), ThemeBank.getMetalTheme(theme));
            } else if (property.indexOf(ApplicationContext.THEME_PROPERTY) > -1
                    && evt.getNewValue() != null) {
                String theme = (String) evt.getNewValue();
                setLookAndFeel(PropertiesManager.APPLICATION_PROPERTIES_SET.getPropertyString(
                        ApplicationContext.class, ApplicationContext.LF_PROPERTY, UIManager
                                .getLookAndFeel().getClass().getName()), ThemeBank
                        .getMetalTheme(theme));
            } else if (property.indexOf(RecentFiles.PROPERTY_NB_RECENT_FILE) > -1) {
                if (recentFiles != null)
                    recentFiles.uninstall();
                recentFiles = new RecentFiles();
                mfMenu.updateRecentFiles();
            } else {
                if (evt.getNewValue() != null && (evt.getNewValue() instanceof Integer)) {
                    setMFLayout(((Integer) evt.getNewValue()).intValue());
                }
            }
        }
    };

    // installed ToolBars
    public static final String FILE_TOOLBAR = "ToolbarFile"; //NOT LOCALIZABLE, property key

    public static final String EDIT_TOOLBAR = "ToolbarEdit"; //NOT LOCALIZABLE, property key

    public static final String DISPLAY_TOOLBAR = "ToolbarDisplay"; //NOT LOCALIZABLE, property key

    public static final String FORMAT_TOOLBAR = "ToolbarFormat"; //NOT LOCALIZABLE, property key

    public static final String CREATION_TOOLBAR = "ToolbarCreation"; //NOT LOCALIZABLE, property key

    public static final String DRAWING_TOOLBAR = "ToolbarDrawing"; //NOT LOCALIZABLE, property key

    public static final String MODIFIER_TOOLBAR = "ToolbarModifier"; //NOT LOCALIZABLE, property key

    public static final String TOOLS_TOOLBAR = "ToolbarTools"; //NOT LOCALIZABLE, property key

    public static final String HELP_TOOLBAR = "ToolbarHelp"; //NOT LOCALIZABLE, property key

    public static final String NAVIGATION_TOOLBAR = "ToolbarNavigation";//NOT LOCALIZABLE, property key

    private ToolBarPanel toolBarsPanel = new ToolBarPanel();

    // optional components
    private PropertiesSet applPref = null;

    //private PropertiesSet preferencesSet = null;

    private RecentFiles recentFiles = null;

    private MainFrameMenu mfMenu = null;

    private StatusBar statusBar = null;

    private StatusBarModel statusBarModel = null;

    private DesignPanel designPanel = null;

    // Optional layout for explorers and design panel
    private int mfLayoutType = -1;

    private MFLayoutManager mfLayout;

    // The root panel for layout of explorer and design panel
    private JPanel contentPanel = new JPanel();

    private JDesktopPane internalFrameContainer = new JDesktopPane() {
        public void setUI(DesktopPaneUI ui) {
            if (UIManager.getLookAndFeel().getClass().getName().equals(
                    "com.sun.java.swing.plaf.windows.WindowsLookAndFeel")) { // NOT LOCALIZABLE
                // We don't want the windows behavior. Conflicts with our
                // magnifier and overview frames
                super.setUI(ui);
                setDesktopManager(new DefaultDesktopManager());
            } else
                super.setUI(ui);
        }
    };

    private DbFindSession findSession = new DbFindSession();

    private SrVector projectsNeedingRefresh;

    private ArrayList lockingObjects = new ArrayList();

    private boolean explorerMayBeLocked = false; // Used for locking the

    // explorer

    abstract public void installToolBars();

    //Construct the frame
    public DefaultMainFrame() {
        // Application properties
        applPref = PropertiesManager.APPLICATION_PROPERTIES_SET;
        if (applPref != null) {
            applPref.addPropertyChangeListener(ApplicationContext.class,
                    ApplicationContext.LF_PROPERTY, preferencesListener);
            applPref.addPropertyChangeListener(ApplicationContext.class,
                    ApplicationContext.THEME_PROPERTY, preferencesListener);
        }

        recentFiles = new RecentFiles();

        // Preferences properties
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        if (preferences != null) {
            preferences.addPropertyChangeListener(DefaultMainFrame.class, PROPERTY_LAYOUT,
                    preferencesListener);
            preferences.addPropertyChangeListener(MagnifierView.class,
                    MagnifierView.ZOOM_FACTOR_PROPERTY, preferencesListener);
            preferences.addPropertyChangeListener(RecentFiles.class,
                    RecentFiles.PROPERTY_NB_RECENT_FILE, preferencesListener);
        }

        desktopPaneContext = new DesktopPaneContext(this);
        ContextManager.getInstance().registerComponent(desktopPaneContext);

        jbInit();
        Db.addDbTransListener(this);
        Db.addDbRefreshPassListener(this);
        designPanel = new DesignPanel();

    }

    //Component initialization
    private void jbInit() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setSize(new Dimension(400, 300));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        this.setTitle(ApplicationContext.getApplicationName());
        Image icon = this.getApplicationIconImage();
        if (icon != null)
            this.setIconImage(icon);
        
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
               System.out.println("Mouse Clicked!"); 
 
            } 
        }); 
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (prepareToExitApplication()) {
                    dispose();
                    System.exit(0);
                }
            }

            @Override
            public void windowOpened(WindowEvent e) {
                if (initialisers == null)
                    return;
                for (Runnable initialiser : initialisers) {
                    runInitialiser(initialiser);
                }
                initialisers = null;
            }

        });
        toolBarsPanel.setBorder(null);

        internalFrameContainer.setDragMode(JDesktopPane.LIVE_DRAG_MODE);

        contentPane.add(contentPanel, BorderLayout.CENTER);
        contentPane.add(toolBarsPanel, BorderLayout.NORTH);
        // Delegate content panel management to a configurable layout manager
        PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
        if (prefs != null) {
            setMFLayout(prefs.getPropertyInteger(DefaultMainFrame.class, PROPERTY_LAYOUT,
                    PROPERTY_LAYOUT_DEFAULT).intValue());
        }

        // setting frame location and size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (applPref != null) {
            if (applPref.getPropertyInteger(DefaultMainFrame.class, PROPERTY_X, null) == null) { // use default
                setSize(new Dimension(screenSize.width - 80, screenSize.height - 80));
                org.modelsphere.jack.awt.AwtUtil.centerWindow(this);
            } else {
                int framewidth = Math.max(applPref.getPropertyInteger(DefaultMainFrame.class,
                        PROPERTY_WIDTH, new Integer(screenSize.width - 80)).intValue(), 300);
                int frameheight = Math.max(applPref.getPropertyInteger(DefaultMainFrame.class,
                        PROPERTY_HEIGHT, new Integer(screenSize.height - 80)).intValue(), 200);
                int framex = Math.max(applPref.getPropertyInteger(DefaultMainFrame.class,
                        PROPERTY_X, new Integer(40)).intValue(), 0);
                int framey = Math.max(applPref.getPropertyInteger(DefaultMainFrame.class,
                        PROPERTY_Y, new Integer(40)).intValue(), 0);
                // ensure size not out of desktop bounds
                framewidth = Math.min(framewidth, screenSize.width);
                frameheight = Math.min(frameheight, screenSize.height);
                // ensure location not out of desktop bounds
                framex = Math.min(Math.max(framex, 0), screenSize.width - 100);
                framey = Math.min(Math.max(framey, 0), screenSize.height - 100);
                setSize(framewidth, frameheight);
                setLocation(framex, framey);

                int state = applPref.getPropertyInteger(DefaultMainFrame.class, PROPERTY_MAXIMIZED,
                        getExtendedState());
                setExtendedState(state);

            } //end if
        } //end if
    }

    private void runInitialiser(final Runnable initialiser) {
        try {
            initialiser.run();
        } catch (Exception ex) {
            Debug.trace(ex);
        }
    }

    /////////////////////////////////////////////////
    // Layout settings Management
    //

    private void setMFLayout(int layout) {
        if (mfLayoutType == layout) {
            return;
        }
        saveComponentsPreferredSize();
        mfLayoutType = layout;
        contentPanel.removeAll();

        if (mfLayout != null)
            mfLayout.removeAll();

        int explvisibility = -999;
        if (getExplorerPanel() != null)
            explvisibility = getExplorerPanel().getVisibility();
        int designPanelvisibility = -999;
        if (getDesignPanel() != null)
            designPanelvisibility = getDesignPanel().isVisible() ? 1 : 0;

        switch (mfLayoutType) {
        case (LAYOUT_LEFT_BOTTOM):
            mfLayout = new LeftBottomMFLayoutManager();
            break;
        case (LAYOUT_LEFT_LEFT):
            mfLayout = new LeftLeftMFLayoutManager();
            break;
        case (LAYOUT_LEFT_RIGHT):
            mfLayout = new LeftRightMFLayoutManager();
            break;
        default:
            mfLayout = null;
            break;
        }

        Debug.assert2(mfLayout != null, "Null MFLayoutManager in DefaultMainFrame");
        if (mfLayout == null)
            return;
        mfLayout.setRootContainer(contentPanel);
        mfLayout.setDesktop(internalFrameContainer);
        mfLayout.setDesignPanel(getDesignPanel());
        mfLayout.setExplorer(getExplorerPanel());

        if (explvisibility != -999)
            setExplorerVisibility(explvisibility);
        if (designPanelvisibility != -999)
            setDesignPanelVisible(designPanelvisibility == 1);

        applyComponentsPreferredSize();
        synchronized(getTreeLock()){
        	validateTree();
        }
    }

    private void saveComponentsPreferredSize() {
        if (mfLayout == null)
            return;
        Dimension dim = null;
        int visibility = getExplorerPanel().getVisibility();
        if (visibility != ExplorerPanel.EXPLORER_HIDE) {
            dim = mfLayout.getPreferredSize(getExplorerPanel());
            if (dim.width > -1)
                applPref.setProperty(ExplorerPanel.class, PROPERTY_WIDTH, dim.width);
            if (dim.height > -1)
                applPref.setProperty(ExplorerPanel.class, PROPERTY_HEIGHT, dim.height);
        }
        boolean designPanelVisible = getDesignPanel() == null ? false : getDesignPanel()
                .isVisible();
        if (designPanelVisible) {
            dim = mfLayout.getPreferredSize(getDesignPanel());
            if (dim.width > -1)
                applPref.setProperty(DesignPanel.class, PROPERTY_WIDTH, dim.width);
            if (dim.height > -1)
                applPref.setProperty(DesignPanel.class, PROPERTY_HEIGHT, dim.height);
        }
    }

    private void applyComponentsPreferredSize() {
        if (mfLayout == null || !isVisible())
            return;
        int width = applPref.getPropertyInteger(ExplorerPanel.class, PROPERTY_WIDTH,
                new Integer((int) (getWidth() * 0.30))).intValue();
        int height = applPref.getPropertyInteger(ExplorerPanel.class, PROPERTY_HEIGHT,
                new Integer((int) (getHeight() * 0.30))).intValue();
        mfLayout.setPreferredSize(getExplorerPanel(), new Dimension(width, height));

        Integer defwidth = new Integer((int) (getWidth() * 0.30));
        Integer defheight = new Integer((int) (getHeight() * 0.30));
        width = applPref.getPropertyInteger(DesignPanel.class, PROPERTY_WIDTH, defwidth).intValue();
        height = applPref.getPropertyInteger(DesignPanel.class, PROPERTY_HEIGHT, defheight)
                .intValue();
        mfLayout.setPreferredSize(getDesignPanel(), new Dimension(width, height));
    }

    /////////////////////////////////////////////////
    // Initial setting methods
    //
    /*
     * All set API: setMainFrameMenu(MainFrameMenu mfMenu); setStatusBar(StatusBar statusBar);
     * installApplicationToolBar(JComponent toolbar); installDiagramToolBar(JComponent toolbar);
     */
    /**
     * should be set as early as you can, if you're application has preferences
     */
    public final PropertiesSet getApplicationPreferences() {
        return applPref;
    }

    public final RecentFiles getRecentFiles() {
        return recentFiles;
    }

    public final PropertiesSet getPreferencesSet() {
        PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
        return prefs;
    }

    /**
     * should be set as early as you can, if you're application has a MainFrameMenu
     */
    public final void setMainFrameMenu(MainFrameMenu mfMenu) {
        this.mfMenu = mfMenu;
        this.setJMenuBar(mfMenu.getMenuBar());
    }

    public final MainFrameMenu getMainFrameMenu() {
        return mfMenu;
    }

    /**
     * should be set as early as you can, if you're application has a StatusBar
     */
    public final void setStatusBar(StatusBar statusBar) {
        this.statusBar = statusBar;
        statusBarModel = statusBar.getModel();
        getContentPane().add(statusBar, BorderLayout.SOUTH);
        AbstractActionsStore store = ApplicationContext.getActionStore();
        ShowAbstractAction action = (ShowAbstractAction) store
                .getAction(AbstractActionsStore.APPLICATION_SHOW_STATUSBAR);

        boolean isStatusBar = (applPref == null) ? false : applPref.getPropertyBoolean(
                DefaultMainFrame.class, STATUSBAR_PROPERTY, Boolean.TRUE).booleanValue();

        if (action.isComponentVisible() ^ isStatusBar)
            action.performAction();
    }

    public final StatusBar getStatusBar() {
        return statusBar;
    }

    public final void setStatusMessage(String message) {
        if (statusBarModel != null)
            statusBarModel.setMessage(message);
    }

    /**
     * This method will install any toolbar. The visible order will be from left to right using the
     * installation order. The key is used to identify the toolbar. Five keys are defined in this
     * class: FILE_TOOLBAR, EDIT_TOOLBAR, DISPLAY_TOOLBAR, TOOLS_TOOLBAR and HELP_TOOLBAR.
     * installLocation: ToolBarManager.INSTALL_IN_DEFAULT_CONTAINER or
     * ToolBarManager.INSTALL_IN_DIALOG
     */
    public final void installToolBar(JToolBar toolBar, String key, int installLocation) {
        ToolBarManager.installToolBarComponent(installLocation, toolBar, key);
    }

    public final JToolBar getInstallToolBar(String key) {
        return ToolBarManager.getInstallToolBar(key);
    }

    // This container acts as the default container for the toolbars. They may
    // also be installed on a ToolBarInternalFrame
    // in the desktop pane.
    public final ToolBarPanel getToolBarPanel() {
        return toolBarsPanel;
    }

    /**
     * Use this method for adding to a JMenu the menu items for managing the toolbars Warning: This
     * method can not be call during the menubar instanciation Update the toolbar menu when
     * MenuListener.menuSelected() is triggered
     */
    public final void populateToolBarMenu(JMenu menu) {
        if (menu != null)
            SwingUtilities.updateComponentTreeUI(menu);
        ToolBarManager.populateMenu(menu);
    }

    protected final Image getApplicationIconImage() {
        return ApplicationContext.APPLICATION_IMAGE_ICON;
    }

    //
    // End of Initial setting methods
    ////////////////////////////////////////////

    //////////////////////////////////////
    // Find management
    //

    public final boolean findInExplorer(DbObject dbo) throws DbException {
        return findInExplorer(new DbObject[] { dbo });
    }

    public final boolean findInExplorer(DbObject[] dbos) throws DbException {

        ExplorerPanel panel = getExplorerPanel();
        if (panel.getVisibility() == ExplorerPanel.EXPLORER_HIDE)
            setExplorerVisibility(ExplorerPanel.EXPLORER_SINGLE);
        ExplorerView view = panel.getMainView();
        if (((panel.getVisibility() == ExplorerPanel.EXPLORER_LEFT_RIGHT) || (panel.getVisibility() == ExplorerPanel.EXPLORER_TOP_BOTTOM))
                && !view.hasFocusState() && panel.getSecondView().hasFocusState())
            view = panel.getSecondView();
        view.clearSelection();
        boolean found = false;
        for (int i = 0; i < dbos.length; i++) {
            if (view.find(dbos[i]))
                found = true;
        }
        return found;
    }

    public final DbFindSession getFindSession() {
        return findSession;
    }

    //
    // End of Find management
    //////////////////////////////////////////

    //////////////////////////////////////////
    // Explorer management
    //
    public synchronized final void setExplorerVisibility(int newVisibility) {
        if (lockingObjects.size() > 0)
            return;
        ExplorerPanel panel = getExplorerPanel();
        int visibility = panel.getVisibility();
        if (newVisibility != visibility) {
            panel.setVisibility(newVisibility);
            if (newVisibility == ExplorerPanel.EXPLORER_HIDE) {
                Dimension dim = mfLayout.getPreferredSize(panel);
                if (dim.width > -1)
                    applPref.setProperty(ExplorerPanel.class, PROPERTY_WIDTH, dim.width);
                if (dim.height > -1)
                    applPref.setProperty(ExplorerPanel.class, PROPERTY_HEIGHT, dim.height);
                mfLayout.setExplorer(null);
                explorerMayBeLocked = false;
            } else if (visibility == ExplorerPanel.EXPLORER_HIDE) {
                mfLayout.setExplorer(panel);
                int width = applPref.getPropertyInteger(ExplorerPanel.class, PROPERTY_WIDTH,
                        new Integer((int) (getWidth() * 0.30))).intValue();
                int height = applPref.getPropertyInteger(ExplorerPanel.class, PROPERTY_HEIGHT,
                        new Integer((int) (getHeight() * 0.30))).intValue();
                mfLayout.setPreferredSize(panel, new Dimension(width, height));
                explorerMayBeLocked = true;
            }
        }

        //Note: I don't know why, but breaking the previous multi-statements
        // line in
        //several one-statement lines makes it work on my machine. Probably
        //it's because when I put the statements on several lines, I need to
        //declare an AbstractApplicationAction variable, thus I need to import
        //AbstractApplicationAction in this class, so it helps JBuilder to
        // build
        //the dependencies among the files correctly. Anyway, it's within the
        //standards to put only one statement per line [MS].
        AbstractActionsStore store = ApplicationContext.getActionStore();
        AbstractApplicationAction absAction = store
                .getAction(AbstractActionsStore.APPLICATION_SHOW_EXPLORER);
        ShowExplorerAction action = (ShowExplorerAction) absAction;
        action.update();
    }

    //
    // End of Explorer management
    //////////////////////////////////////////

    //////////////////////////////////////////
    // GUI Management
    //
    // This method is used to ensure dead lock protection on the explorer when a
    // refresh
    // on db occurs in threads other than the event dispatch queue.
    // If other GUI not 100% thread safe are added to the application, include
    // these components
    // in this method (These components should way to add remove db listeners.
    // This ensure that
    // these components will not be updated in the incorrect threads)
    public synchronized final void lockDbRefreshInGUI(Object lockingObject) {
        ApplicationContext.getFocusManager().setGuiLocked(true);
        if (lockingObjects.contains(lockingObject))
            return;
        lockingObjects.add(lockingObject);
        if (lockingObjects.size() > 1)
            return;
        if (explorerMayBeLocked) { // explorerMayBeLocked only if the explorer
            // is not hidden
            getExplorerPanel().getExplorer().installDbListeners(false);
        }
        DesignPanel designpanel = getDesignPanel();
        if (designpanel != null)
            designpanel.lockGUI();
        // Lock all list
        JInternalFrame[] lists = getListInternalFrames();
        for (int i = 0; i < lists.length; i++) {
            ListInternalFrame list = (ListInternalFrame) lists[i];
            list.lockGUI();
        }
    }

    // Unlock previous lock
    public synchronized final void unlockDbRefreshInGUI(Object lockingObject) {
        if (!lockingObjects.contains(lockingObject))
            return;
        lockingObjects.remove(lockingObject);
        if (lockingObjects.size() > 0)
            return;
        ApplicationContext.getFocusManager().setGuiLocked(false);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    ApplicationContext.getFocusManager().update();
                    if (explorerMayBeLocked) {
                        getExplorerPanel().getExplorer().installDbListeners(true);
                        getExplorerPanel().getExplorer().refresh();
                    }
                    // unLock all list
                    JInternalFrame[] lists = getListInternalFrames();
                    for (int i = 0; i < lists.length; i++) {
                        ListInternalFrame list = (ListInternalFrame) lists[i];
                        list.unlockGUI();
                    }
                    ApplicationContext.getFocusManager().update();
                    DesignPanel designpanel = getDesignPanel();
                    if (designpanel != null)
                        designpanel.unlockGUI();
                } catch (Exception e) {
                    ExceptionHandler.processUncatchedException(DefaultMainFrame.this, e);
                }
            }
        });
    }

    //
    // End of GUI Management
    //////////////////////////////////////////

    //////////////////////////////////////////
    // Design Panel management
    //
    public final void setDesignPanelVisible(boolean visible) {
        DesignPanel panel = getDesignPanel();
        if (panel != null) {
            boolean oldvisible = panel.isDesignPanelVisible();
            if (visible != oldvisible) {
                if (visible) {
                    panel.setDesignPanelVisible(true);
                    mfLayout.setDesignPanel(panel);
                    Integer defwidth = new Integer((int) (getWidth() * 0.30));
                    Integer defheight = new Integer((int) (getHeight() * 0.30));
                    int width = applPref.getPropertyInteger(DesignPanel.class, PROPERTY_WIDTH,
                            defwidth).intValue();
                    int height = applPref.getPropertyInteger(DesignPanel.class, PROPERTY_HEIGHT,
                            defheight).intValue();
                    mfLayout.setPreferredSize(panel, new Dimension(width, height));
                } else {
                    Dimension dim = mfLayout.getPreferredSize(panel);
                    if (dim.width > -1)
                        applPref.setProperty(DesignPanel.class, PROPERTY_WIDTH, dim.width);
                    if (dim.height > -1)
                        applPref.setProperty(DesignPanel.class, PROPERTY_HEIGHT, dim.height);
                    panel.setDesignPanelVisible(false);
                    mfLayout.setDesignPanel(null);
                }
            }
        }
        AbstractActionsStore store = ApplicationContext.getActionStore();
        AbstractApplicationAction absAction = store
                .getAction(AbstractActionsStore.APPLICATION_SHOW_DESIGN_PANEL);
        ShowDesignPanelAction action = (ShowDesignPanelAction) absAction;
        action.update();
    }

    //
    // End of Design Panel management
    //////////////////////////////////////////

    //////////////////////////////////////////
    // Exit/Close management
    //
    public final void closeProject(DbProject project) {
        if (!(project.getDb() instanceof DbRAM))
            return;
        String action = LocaleMgr.action.getString("closingProject");
        if (!userAcceptClosingDataEntryFrames(project, action))
            return;
        if (!userAcceptClosingProject(project, action))
            return;

        ReadOnly readOnly = ReadOnly.getSingleton();
        boolean retVal = readOnly.isReadOnlyEnabled();
        try {
            Db db = project.getDb();
            readOnly.setReadOnlyEnabled(false);
            db.terminate();
        } catch (Exception ex) {
            readOnly.setReadOnlyEnabled(retVal);
            ExceptionHandler.processUncatchedException(this, ex);
        }
    }

    private boolean userAcceptClosingDataEntryFrames(DbProject project, String action) {
        JInternalFrame[] frames = getDataEntryInternalFrames();
        for (int i = 0; i < frames.length; i++) {
            if (project == null || project == ((DbDataEntryFrame) frames[i]).getProject()) {
                if (!((DbDataEntryFrame) frames[i]).requestClose(action))
                    return false;
            }
        }
        return true;
    }

    private boolean userAcceptClosingProject(DbProject project, String action) {
        if (project.getLastSaveTrans() == project.getDb().getTransCount())
            return true;
        String projectName = getProjectNameForFileMenu(project);
        String dialogText = LocaleMgr.screen.getString("someModificationsMadeIn0WantSaveBefore1");
        int userResponse = JOptionPane.showConfirmDialog(this, MessageFormat.format(dialogText,
                new Object[] { projectName, action }), ApplicationContext.getApplicationName(),
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        switch (userResponse) {
        case JOptionPane.YES_OPTION:
            return saveCurrentProject(project, false);
        case JOptionPane.NO_OPTION:
            return true;
        default:
            return false;
        }
    }

    private String getProjectNameForFileMenu(DbProject project) {
        String projectName = project.getRamFileName();
        if (projectName == null) {
            try {
                project.getDb().beginTrans(Db.READ_TRANS);
                projectName = project.getName();
                project.getDb().commitTrans();
            } catch (Exception ex) {
                projectName = "???";
            }
        }
        return projectName;
    }

    public boolean prepareToExitApplication() {
        // call pre finalisers first
        for (Runnable finalizer : preFinalizers) {
            try {
                finalizer.run();
            } catch (Exception e) {
                Debug.trace(e);
            }
        }

        String action = LocaleMgr.action.getString("exitingApplication");
        if (!userAcceptClosingDataEntryFrames(null, action))
            return false;

        Db[] dbs = Db.getDbs();

        boolean ok = true;
        for (int i = 0; i < dbs.length; i++) {
            if (!(dbs[i] instanceof DbRAM))
                continue;
            try {
                DbProject project = DbApplication.getFirstProjectFor(dbs[i]);
                if (!userAcceptClosingProject(project, action)) {
                    ok = false;
                    break;
                }
            } catch (Exception ex) {
            }
        }
        if (!ok)
            return false;

        // Save some GUI preferences
        Point p = this.getLocation();
        Dimension dim = this.getSize();
        applPref.setProperty(DefaultMainFrame.class, PROPERTY_WIDTH, dim.width);
        applPref.setProperty(DefaultMainFrame.class, PROPERTY_HEIGHT, dim.height);
        applPref.setProperty(DefaultMainFrame.class, PROPERTY_X, p.x);
        applPref.setProperty(DefaultMainFrame.class, PROPERTY_Y, p.y);
        applPref.setProperty(DefaultMainFrame.class, STATUSBAR_PROPERTY, statusBar == null ? false
                : statusBar.isVisible());
        applPref.setProperty(DefaultMainFrame.class, PROPERTY_MAXIMIZED, getExtendedState());
        int visibility = getExplorerPanel().getVisibility();
        applPref.setProperty(ExplorerPanel.class, PROPERTY_VISIBILITY, visibility);
        if (visibility != ExplorerPanel.EXPLORER_HIDE && mfLayout != null) {
            dim = mfLayout.getPreferredSize(getExplorerPanel());
            if (dim.width > -1)
                applPref.setProperty(ExplorerPanel.class, PROPERTY_WIDTH, dim.width);
            if (dim.height > -1)
                applPref.setProperty(ExplorerPanel.class, PROPERTY_HEIGHT, dim.height);
        }
        boolean designPanelVisible = getDesignPanel() == null ? false : getDesignPanel()
                .isVisible();
        applPref.setProperty(DesignPanel.class, PROPERTY_VISIBILITY, designPanelVisible);
        if (designPanelVisible && mfLayout != null) {
            dim = mfLayout.getPreferredSize(getDesignPanel());
            if (dim.width > -1)
                applPref.setProperty(DesignPanel.class, PROPERTY_WIDTH, dim.width);
            if (dim.height > -1)
                applPref.setProperty(DesignPanel.class, PROPERTY_HEIGHT, dim.height);
        }

        applPref.setProperty(MagnifierInternalFrame.class, PROPERTY_VISIBILITY,
                isMagnifierWindowVisible());
        if (isMagnifierWindowVisible()) {
            Rectangle rect = magnifierFrame.getBounds();
            if (rect != null) {
                applPref.setProperty(MagnifierInternalFrame.class, PROPERTY_X, rect.x);
                applPref.setProperty(MagnifierInternalFrame.class, PROPERTY_Y, rect.y);
                applPref.setProperty(MagnifierInternalFrame.class, PROPERTY_WIDTH, rect.width);
                applPref.setProperty(MagnifierInternalFrame.class, PROPERTY_HEIGHT, rect.height);
            }
        }
        applPref.setProperty(OverviewInternalFrame.class, PROPERTY_VISIBILITY,
                isOverviewWindowVisible());
        if (isOverviewWindowVisible()) {
            Rectangle rect = overviewFrame.getBounds();
            if (rect != null) {
                applPref.setProperty(OverviewInternalFrame.class, PROPERTY_X, rect.x);
                applPref.setProperty(OverviewInternalFrame.class, PROPERTY_Y, rect.y);
                applPref.setProperty(OverviewInternalFrame.class, PROPERTY_WIDTH, rect.width);
                applPref.setProperty(OverviewInternalFrame.class, PROPERTY_HEIGHT, rect.height);
            }
        }

        PropertiesManager.save();

        Log.stop();

        // call post finalisers
        for (Runnable finalizer : postFinalizers) {
            try {
                finalizer.run();
            } catch (Exception e) {
                Debug.trace(e);
            }
        }

        Db.terminateAll();
        
        //Tell SafeMode we have existed normally
        SafeMode.exitNormally(true);
        
        return true;
    }

    //
    // Exit/close management
    /////////////////////////////////////////

    ///////////////////////////////////////////
    // Printing management
    //
    public final void doPrint() {
        DiagramInternalFrame diagFrame = getSelectedDiagramInternalFrame();
        if (diagFrame != null) {
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            Diagram diag = diagFrame.getDiagram();
            printerJob.setJobName(diagFrame.getTitle());
            printerJob.setPageable(diag);
            //printerJob.setPrintable(diag);
            //PrintRequestAttributeSet attr = new
            // HashPrintRequestAttributeSet();
            //if (printerJob.printDialog(attr)){
            if (printerJob.printDialog()) {
                try {
                    printerJob.print();
                } catch (PrinterException ex) {
                    String pattern = LocaleMgr.message.getString("errorDuringPrinting0");
                    String mess = MessageFormat.format(pattern, new Object[] { ex.getMessage() });
                    ExceptionHandler.showErrorMessage(this, mess);
                }
            }
        }
    }

    public final void doPageSetup() {
        DiagramInternalFrame diagFrame = getSelectedDiagramInternalFrame();
        if (diagFrame != null) {
            ApplicationDiagram diag = diagFrame.getDiagram();
            PageFormat pageFormat = PrinterJob.getPrinterJob().pageDialog(diag.getPageFormat());
            updatePageFormat(diag, pageFormat);
        }
    }

    private void updatePageFormat(ApplicationDiagram diag, PageFormat pageFormat) {
        Dimension pageSize = Diagram.getPageSize(pageFormat, diag.getPrintScale());
        Dimension nbPages = diag.getNbPages();
        if (!pageSize.equals(diag.getPageSize())) {
            Dimension minPages = getMinPages(diag, pageSize);
            Rectangle drawingArea = diag.getDrawingArea();
            nbPages = new Dimension(Math.max(drawingArea.width / pageSize.width, minPages.width),
                    Math.max(drawingArea.height / pageSize.height, minPages.height));
        }
        try {
            DbObject diagGO = diag.getDiagramGO();
            diagGO.getDb().beginTrans(Db.WRITE_TRANS,
                    LocaleMgr.action.getString("pageDimensionModification"));
            diagGO.set(DbGraphic.fDiagramPageFormat, pageFormat);
            diagGO.set(DbGraphic.fDiagramNbPages, nbPages);
            diagGO.getDb().commitTrans();
        } catch (Exception ex) {
            ExceptionHandler.processUncatchedException(this, ex);
        }
    }

    // Compute the number of pages covered by all the graphic components of the
    // diagram.
    // Cannot be computed from the DbGraphicalObjects, which do not have the
    // complete size information.
    private Dimension getMinPages(Diagram diag, Dimension pageSize) {
        Dimension minPages = new Dimension(0, 0);
        Enumeration enumeration = diag.components();
        while (enumeration.hasMoreElements()) {
            GraphicComponent gc = (GraphicComponent) enumeration.nextElement();
            Rectangle rect = gc.getRectangle();
            minPages.width = Math.max(minPages.width, rect.x + rect.width);
            minPages.height = Math.max(minPages.height, rect.y + rect.height);
        }
        minPages.width = 1 + minPages.width / pageSize.width;
        minPages.height = 1 + minPages.height / pageSize.height;
        return minPages;
    }

    //
    // End of Printing Management
    ////////////////////////////////////////

    ////////////////////////////////////////
    // InternalFrames management
    //
    public final JInternalFrame[] getDiagramInternalFrames() {
        return filterJInternalFrames(internalFrameContainer.getAllFrames(),
                DiagramInternalFrame.class, true);
    }

    public final JInternalFrame[] getPropertyInternalFrames() {
        return filterJInternalFrames(internalFrameContainer.getAllFrames(), PropertiesFrame.class,
                true);
    }

    public final JInternalFrame[] getListInternalFrames() {
        return filterJInternalFrames(internalFrameContainer.getAllFrames(),
                ListInternalFrame.class, true);
    }

    public final JInternalFrame[] getDataEntryInternalFrames() {
        return filterJInternalFrames(internalFrameContainer.getAllFrames(), DbDataEntryFrame.class,
                true);
    }

    protected JInternalFrame[] filterJInternalFrames(JInternalFrame[] frames, Class classCheck,
            boolean isClassWanted) {
        JInternalFrame[] filteredFrames = new JInternalFrame[frames.length];
        int ctr = 0;
        for (int i = 0; i < frames.length; i++) {
            if (classCheck.isInstance(frames[i]) == isClassWanted) {
                filteredFrames[ctr++] = frames[i];
            }
        }
        JInternalFrame[] filteredFramesAdjust = new JInternalFrame[ctr];
        System.arraycopy(filteredFrames, 0, filteredFramesAdjust, 0, ctr);
        return filteredFramesAdjust;
    }

    public final JDesktopPane getJDesktopPane() {
        return internalFrameContainer;
    }

    public final DiagramInternalFrame getSelectedDiagramInternalFrame() {
        JInternalFrame frame = getSelectedInternalFrame();
        if (frame instanceof DiagramInternalFrame)
            return (DiagramInternalFrame) frame;
        if (frame instanceof DiagramViewInternalFrame)
            return ((DiagramViewInternalFrame) frame).getDiagFrame();
        return null;
    }

    public final PropertiesFrame getSelectedPropertiesFrame() {
        JInternalFrame frame = getSelectedInternalFrame();
        if (frame instanceof PropertiesFrame)
            return (PropertiesFrame) frame;
        return null;
    }

    public final JInternalFrame getSelectedInternalFrame() {
        return internalFrameContainer.getSelectedFrame();
        /*
         * JInternalFrame[] frames = internalFrameContainer.getAllFrames(); for (int i = 0; i <
         * frames.length; i++) { if (frames[i].isSelected()) return (frames[i].isIcon() ? null :
         * frames[i]); } return null;
         */
    }

    public final DiagramInternalFrame addDiagramInternalFrame(DbObject diag) throws DbException {
        DiagramInternalFrame diagFrame = getDiagramInternalFrame(diag);
        if (diagFrame == null) {
            diagFrame = createDiagramInternalFrame(diag);
            if (diagFrame == null)
                return null;
            internalFrameContainer.add(diagFrame, APPLICATION_DIAGRAM_LAYER);
            try {
                if (diagFrame.getInitialOptionSize() == 4)
                    diagFrame.setMaximum(true);
            } catch (PropertyVetoException ex) {
            }

            diagFrame.setVisible(true);
        }
        try {
            diagFrame.setIcon(false);
            diagFrame.setSelected(true);
        } catch (PropertyVetoException e) {
        }
        return diagFrame;
    }

    public DiagramInternalFrame getDiagramInternalFrame(DbObject diag) {
        DiagramInternalFrame diagFound = null;
        JInternalFrame[] diagFrames = getDiagramInternalFrames();
        for (int i = 0; i < diagFrames.length; i++)
            if (((DiagramInternalFrame) diagFrames[i]).getDiagram().getDiagramGO() == diag) {
                diagFound = (DiagramInternalFrame) diagFrames[i];
                break;
            }
        return diagFound;
    }

    public DesignPanel getDesignPanel() {
        return designPanel;
    }

    // Return null if this object cannot have a properties frame
    public final PropertiesFrame addPropertyInternalFrame(DbObject obj) throws DbException {
        PropertiesFrame frame = getPropertyInternalFrame(obj, PropertiesFrame.TYPE_PROPERTY);
        if (frame == null) {
            if (obj instanceof DbUDF) {
                String titlePattern = MessageFormat.format(LocaleMgr.screen.getString("01"),
                        new Object[] { DbUDF.metaClass.getGUIName(), "{0}" });
                ScreenView[] tabs = new ScreenView[2];
                tabs[0] = new DbDescriptionView(SrScreenContext.singleton, obj);
                DbListView list = new DbListView(SrScreenContext.singleton, obj,
                        DbObject.fComponents, DbUDFValue.metaClass, ScreenView.PROPERTIES_BTN
                                | ScreenView.SORTED_LIST);
                tabs[1] = list;
                list.setParentRel(DbUDFValue.fDbObject);
                list.setTabName(LocaleMgr.screen.getString("usedBy"));
                frame = new PropertiesFrame(SrScreenContext.singleton, obj, tabs, titlePattern);
            } else if (obj.getProject() == null) {
                ScreenView[] tabs = null;
                String titlePattern = null;
                if (obj instanceof DbLoginNode) {
                    titlePattern = MessageFormat.format(kRepositoryLogin0, new Object[] { obj
                            .getDb().getDBMSName() });
                    tabs = new ScreenView[] {
                            new LoginListView(SrScreenContext.singleton, (DbLoginNode) obj,
                                    DbLoginUser.metaClass),
                            new LoginListView(SrScreenContext.singleton, (DbLoginNode) obj,
                                    DbLoginGroup.metaClass) };
                } else if (obj instanceof DbLoginGroup) {
                    titlePattern = k0Members;
                    tabs = new ScreenView[] { new LoginListView(SrScreenContext.singleton,
                            (DbLogin) obj, DbLoginGroup.fMembers, DbLogin.metaClass) };
                } else if (obj instanceof DbLoginUser) {
                    titlePattern = k0Groups;
                    tabs = new ScreenView[] { new LoginListView(SrScreenContext.singleton,
                            (DbLogin) obj, DbLogin.fGroups, DbLoginGroup.metaClass) };
                }
                if (tabs != null)
                    frame = new PropertiesFrame(SrScreenContext.singleton, obj, tabs, titlePattern);
            } else
                frame = createPropertiesFrame(obj); // may return null

            if (frame != null) {
                internalFrameContainer.add(frame, PROPERTY_LAYER);
                frame.setVisible(true);
            }
        }

        if (frame != null) {
            try {
                frame.setIcon(false);
                frame.setSelected(true);
            } catch (PropertyVetoException e) {
            }
        }
        return frame;
    }

    // Return null if this object cannot have a properties frame
    public final ListInternalFrame addListInternalFrame(DbObject root, ListDescriptor descriptor)
            throws DbException {

        ListInternalFrame frame = null;
        Boolean relationship = descriptor.HasRelationship();
        if (relationship != null) {
            if (relationship.booleanValue() == true)
                frame = FindRelationshipFrame(root, descriptor.getNeighborsMetaClass());
            else
                frame = FindEntityFrame(root, descriptor.getNeighborsMetaClass());
        } else {
            frame = getListInternalFrame(root, descriptor.getNeighborsMetaClass());
        }
        if (frame == null) {
            frame = this.createListFrame(root, descriptor); // may return null

            if (frame != null) {
                internalFrameContainer.add(frame, LIST_LAYER);
                frame.setVisible(true);
            }
        }

        if (frame != null) {
            try {
                frame.setIcon(false);
                frame.setSelected(true);
            } catch (PropertyVetoException e) {
            }
        }
        return frame;
    }

    public final PropertiesFrame showUDFFrame(DbProject project) throws DbException {
        PropertiesFrame frame = getPropertyInternalFrame(project, PropertiesFrame.TYPE_UDF);
        if (frame == null) {
            String name = "{0}"; // placeholder for project name (automatically
            // refreeshed when it changes)
            if (project.getDb() instanceof DbRAM && project.getRamFileName() != null)
                name = StringUtil.getFileName(project.getRamFileName()); // fixed
            // (not
            // refreshed)
            // RAM
            // file
            // name
            String titlePattern = MessageFormat.format(LocaleMgr.screen.getString("0UDFs"),
                    new Object[] { name });
            frame = new PropertiesFrame(SrScreenContext.singleton, project,
                    new ScreenView[] { new UDFListView(SrScreenContext.singleton, project) },
                    titlePattern);
            frame.setType(PropertiesFrame.TYPE_UDF);
            frame.setSize(getDefaultInternalFrameSize());
            internalFrameContainer.add(frame, PROPERTY_LAYER);
            frame.getContentPane().doLayout();
            frame.setVisible(true);
        }
        try {
            frame.setIcon(false);
            frame.setSelected(true);
        } catch (PropertyVetoException e) {
        }
        return frame;
    }

    public final PropertiesFrame getPropertyInternalFrame(DbObject obj, int type) {
        PropertiesFrame propFound = null;
        JInternalFrame[] propFrames = getPropertyInternalFrames();
        for (int i = 0; i < propFrames.length; i++) {
            PropertiesFrame propFrame = (PropertiesFrame) propFrames[i];
            if (propFrame.getSemanticalObject() == obj && propFrame.getType() == type) {
                propFound = propFrame;
                break;
            }
        }
        return propFound;
    }

    public ListInternalFrame FindRelationshipFrame(DbObject root, MetaClass neighborsMetaClass) {
        boolean relationshipFrameFound = false;
        ListInternalFrame listFound = null;
        JInternalFrame[] listFrames = getListInternalFrames();
        for (int i = 0; i < listFrames.length; i++) {
            ListInternalFrame listFrame = (ListInternalFrame) listFrames[i];
            Boolean value = listFrame.isRelationships();
            if (value != null)
                if (value.booleanValue() == true) {
                    listFound = listFrame;
                    break;
                }
        }
        return listFound;
    }

    public ListInternalFrame FindEntityFrame(DbObject root, MetaClass neighborsMetaClass) {
        boolean entityFrameFound = false;
        ListInternalFrame listFound = null;
        JInternalFrame[] listFrames = getListInternalFrames();
        for (int i = 0; i < listFrames.length; i++) {
            ListInternalFrame listFrame = (ListInternalFrame) listFrames[i];
            Boolean value = listFrame.isRelationships();
            if (value != null)
                if (value.booleanValue() == false) {
                    listFound = listFrame;
                    break;
                }
        }
        return listFound;
    }

    public final ListInternalFrame getListInternalFrame(DbObject root, MetaClass neighborsMetaClass) {
        ListInternalFrame listFound = null;
        JInternalFrame[] listFrames = getListInternalFrames();
        for (int i = 0; i < listFrames.length; i++) {
            ListInternalFrame listFrame = (ListInternalFrame) listFrames[i];
            if (listFrame.getSemanticalObject() == root
                    && listFrame.getNeighborsMetaClass() == neighborsMetaClass) {
                listFound = listFrame;
                break;
            }
        }
        return listFound;
    }

    public final void refreshAllDiagrams() {
        try {
            JInternalFrame[] diagFrames = getDiagramInternalFrames();
            for (int i = 0; i < diagFrames.length; i++) {
                ApplicationDiagram diag = ((DiagramInternalFrame) diagFrames[i]).getDiagram();
                Db db = diag.getDiagramGO().getDb();
                db.beginTrans(Db.READ_TRANS);
                diag.populateContent();
                db.commitTrans();
            }
        } catch (Exception ex) {
            ExceptionHandler.processUncatchedException(this, ex);
        }
    }

    public final void setOverviewWindowVisibility(boolean visible) {
        if (visible == isOverviewWindowVisible())
            return;
        if (visible) {
            overviewFrame = new OverviewInternalFrame();
            int x = applPref.getPropertyInteger(OverviewInternalFrame.class, PROPERTY_X,
                    new Integer(-1)).intValue();
            int y = applPref.getPropertyInteger(OverviewInternalFrame.class, PROPERTY_Y,
                    new Integer(-1)).intValue();
            int w = applPref.getPropertyInteger(OverviewInternalFrame.class, PROPERTY_WIDTH,
                    new Integer(-1)).intValue();
            int h = applPref.getPropertyInteger(OverviewInternalFrame.class, PROPERTY_HEIGHT,
                    new Integer(-1)).intValue();
            positionViewWindow(overviewFrame, (w == -1 || h == -1 ? null
                    : new Rectangle(x, y, w, h)), magnifierFrame);
            internalFrameContainer.add(overviewFrame, PALETTE_LAYER);
            overviewFrame.installDiagramView();
            overviewFrame.addInternalFrameListener(overviewMagnifierListener);
            overviewFrame.setVisible(true);
        } else {
            uninstallOverviewFrame();
        }
    }

    public final boolean isOverviewWindowVisible() {
        return overviewFrame != null;
    }

    public final void setMagnifierWindowVisibility(boolean state) {
        if (state == isMagnifierWindowVisible())
            return;
        if (state) {
            magnifierFrame = new MagnifierInternalFrame();
            int x = applPref.getPropertyInteger(MagnifierInternalFrame.class, PROPERTY_X,
                    new Integer(-1)).intValue();
            int y = applPref.getPropertyInteger(MagnifierInternalFrame.class, PROPERTY_Y,
                    new Integer(-1)).intValue();
            int w = applPref.getPropertyInteger(MagnifierInternalFrame.class, PROPERTY_WIDTH,
                    new Integer(-1)).intValue();
            int h = applPref.getPropertyInteger(MagnifierInternalFrame.class, PROPERTY_HEIGHT,
                    new Integer(-1)).intValue();
            positionViewWindow(magnifierFrame, (w == -1 || h == -1 ? null : new Rectangle(x, y, w,
                    h)), overviewFrame);
            internalFrameContainer.add(magnifierFrame, PALETTE_LAYER);
            magnifierFrame.installDiagramView();
            magnifierFrame.addInternalFrameListener(overviewMagnifierListener);
            magnifierFrame.setVisible(true);
        } else {
            uninstallMagnifierFrame();
        }
    }

    private void uninstallMagnifierFrame() {
        if (magnifierFrame == null)
            return;
        Rectangle rect = magnifierFrame.getBounds();
        if (rect != null) {
            applPref.setProperty(MagnifierInternalFrame.class, PROPERTY_X, rect.x);
            applPref.setProperty(MagnifierInternalFrame.class, PROPERTY_Y, rect.y);
            applPref.setProperty(MagnifierInternalFrame.class, PROPERTY_WIDTH, rect.width);
            applPref.setProperty(MagnifierInternalFrame.class, PROPERTY_HEIGHT, rect.height);
        }
        magnifierFrame.removeInternalFrameListener(overviewMagnifierListener);
        magnifierFrame.close();
        magnifierFrame = null;
    }

    private void uninstallOverviewFrame() {
        if (overviewFrame == null)
            return;
        Rectangle rect = overviewFrame.getBounds();
        if (rect != null) {
            applPref.setProperty(OverviewInternalFrame.class, PROPERTY_X, rect.x);
            applPref.setProperty(OverviewInternalFrame.class, PROPERTY_Y, rect.y);
            applPref.setProperty(OverviewInternalFrame.class, PROPERTY_WIDTH, rect.width);
            applPref.setProperty(OverviewInternalFrame.class, PROPERTY_HEIGHT, rect.height);
        }
        overviewFrame.removeInternalFrameListener(overviewMagnifierListener);
        overviewFrame.close();
        overviewFrame = null;
    }

    public final boolean isMagnifierWindowVisible() {
        return magnifierFrame != null;
    }

    //place the window to the down-right corner
    private void positionViewWindow(JInternalFrame frame1, Rectangle frame1PrefBounds,
            JInternalFrame frame2) {
        Rectangle screenBounds = internalFrameContainer.getBounds();
        Rectangle visibleRect = internalFrameContainer.getVisibleRect();
        Rectangle frame1Bounds = null;
        if (frame1PrefBounds != null) {
            Rectangle intersection = SwingUtilities.computeIntersection(frame1PrefBounds.x,
                    frame1PrefBounds.y, frame1PrefBounds.width, frame1PrefBounds.height,
                    visibleRect);
            if (intersection != null && intersection.width > 25 && intersection.height > 25) { // Ensure a minimum part of
                // the frame is visible
                frame1Bounds = frame1PrefBounds;
            }
        }
        int xBorder = screenBounds.x + screenBounds.width;
        if (frame2 != null) {
            Rectangle frame2Bounds = frame2.getBounds();
            if (frame2Bounds.x + frame2Bounds.width == screenBounds.x + screenBounds.width)
                xBorder = frame2Bounds.x;
        }
        if (frame1Bounds == null) {
            Dimension frame1Size = frame1.getSize();
            frame1.setLocation(xBorder - frame1Size.width, screenBounds.height - frame1Size.height);
        } else {
            frame1.setLocation(frame1Bounds.x, frame1Bounds.y);
            frame1.setSize(frame1Bounds.width, frame1Bounds.height);
        }
    }

    //
    // End of InternalFrames management
    ///////////////////////////////////////////////

    /////////////////////////////////////////////////
    // Open/Save Management
    //
    public final void doOpen(Db db) {
        if (db == null)
            db = new DbRAM();
        JFileChooser chooser = new JFileChooser(
                m_fileChooserCurrentDirectory == null ? ApplicationContext
                        .getDefaultWorkingDirectory() : m_fileChooserCurrentDirectory);
        
        //Add built-in filters
        chooser.addChoosableFileFilter(smsFileFilter);
        chooser.addChoosableFileFilter(ExtensionFileFilter.txtFileFilter);
        chooser.addChoosableFileFilter(ExtensionFileFilter.htmlFileFilter);
        chooser.addChoosableFileFilter(ExtensionFileFilter.sqlFileFilter);
        
        //Add pluggable filters
        OpenFileExtension.addPluginFileFilers(chooser); 
        
        //TODO: uncomment to following line in order to activate the 'Open XML
        // file' feature
        //this feature is not available in version 2.1 [MS]
        //chooser.addChoosableFileFilter(ExtensionFileFilter.xmlFileFilter);

        chooser.setFileFilter(smsFileFilter);
        chooser.setDialogTitle(LocaleMgr.screen.getString("open"));
        Dimension dim = chooser.getPreferredSize();
        // add extra space to the chooser (not evaluated correctly (as of java
        // 1.3.1)
        chooser.setPreferredSize(new Dimension(dim.width + 75, dim.height + 75));
        int retval = chooser.showOpenDialog(this);
        File theFile = chooser.getSelectedFile();
        if (retval == JFileChooser.APPROVE_OPTION && theFile != null) {

            if (!theFile.exists()) {
                //generate an exception
                try {
                    FileReader reader = new FileReader(theFile);
                } catch (FileNotFoundException ex) {
                    String message = ex.getLocalizedMessage();
                    ExceptionHandler.showErrorMessage(this, message);
                }
            } else {
                String fileName = theFile.getAbsolutePath();
                String extension = ExtensionFileFilter.getExtension(theFile);

                // open file
                if (smsFileFilter.isValidExtension(extension)) {
                    doOpenFromFile(db, fileName, false);
                } else if (ExtensionFileFilter.xmlFileFilter.isValidExtension(extension)) {
                    //TODO: uncomment the following line to call code
                    // associated to the 'Open XML file' feature [MS}
                    //doOpenFromXmlFile(db, fileName);
                } else if (OpenFileExtension.canOpen(extension)) {
                    OpenFileExtension.openFile(extension, chooser, db, theFile);
                } else {
                    TextViewerFrame textFrame = new TextViewerFrame("", theFile);
                    textFrame.showTextViewerFrame(this.getJDesktopPane(), PROPERTY_LAYER);
                }
            } //end if

            // keep current directory for further invocation of file chooser
            // [MS]
            m_fileChooserCurrentDirectory = theFile.getParent();
        } //end if
    } //end doOpen()

    public final DbProject doOpenFromFile(String fileName) {
        return doOpenFromFile(new DbRAM(), fileName, false);
    }

    public final DbProject doOpenFromFile(Db db, String fileName, boolean doItLikeANew) {
        DbProject newProject = null;
        ObjectInputStream inStream = null;
        String message = null;

        if (!doItLikeANew && statusBarModel != null)
            statusBarModel.startWaitingBar(MessageFormat.format(LocaleMgr.message
                    .getString("openingFile0"), new Object[] { fileName }), 3000);

        boolean isReadOnly;
        try {
            try {
                File file = new File(fileName);
                long length = file.length();
                isReadOnly = (length == 0) ? false : !(file.canWrite());

                if (isReadOnly) {
                    ReadOnly readonly = ReadOnly.getSingleton();
                    readonly.showWarning(this, file);
                }
                inStream = new ObjectInputStream(new FileInputStream(fileName));
                if (inStream.readLong() != getFileMagicNumber()) {
                    message = MessageFormat.format(LocaleMgr.message.getString("not0Model"),
                            new Object[] { ApplicationContext.getApplicationName() });
                    throw new Exception();
                }
            } catch (FileNotFoundException ex) {
                message = ex.getLocalizedMessage();
                throw ex;
            } catch (Exception ex) {
                message = MessageFormat.format(LocaleMgr.message.getString("not0Model"),
                        new Object[] { ApplicationContext.getApplicationName() });
                throw ex;
            }

            VersionConverter converter = getVersionConverter();
            int versions = inStream.readInt(); // left bits = build id, right
            // bits = version
            int version = versions & 0x0000FFFF;
            int build = versions >> 16;
            if (build < getMinimumBuild()) {
                message = MessageFormat.format(kCannotOpenBuild0, new Object[] { new Integer(
                        getMinimumBuild()) });
                throw new Exception();
            }
            if (version > converter.getCurrentVersion()) {
                message = LocaleMgr.message.getString("thisModelMadeByLaterVersionCannotOpened");
                throw new Exception();
            }
            converter.setOldVersion(version);

            if (version >= 14) { // since 3.1 (version 14)
                int distributorID = inStream.readInt();
                int releaseStatus = inStream.readInt();
                converter.setOldDistributorID(distributorID);
                converter.setOldReleaseStatus(releaseStatus);
            }

            //load file
            ReadOnly readOnly = ReadOnly.getSingleton();
            readOnly.setReadOnlyEnabled(false); //can create objects during a load
            newProject = (DbProject) db.getRoot().load(inStream, converter);
            readOnly.setReadOnly(newProject, isReadOnly);
            readOnly.setReadOnlyEnabled(true);

            inStream.close();
            inStream = null;
            newProject.setLastSaveTrans(db.getTransCount());

            db.beginTrans(Db.WRITE_TRANS);

            // Beta site should be able to convert their projects. Comment next
            // line for systematic call to convertForDebugUpdate.
            if (org.modelsphere.jack.debug.Debug.isDebug()) {
                convertForDebugUpdate(newProject, version, build);
            }

            if (db instanceof DbRAM) {
                StringWriter report = new StringWriter();
                boolean integrityOK = DataIntegrity.checkDataIntegrity(newProject, true, report);
                if (!integrityOK) {
                    TextViewerDialog.showTextDialog(this, LocaleMgr.message
                            .getString("DataIntegrityErrors")
                            + " - " + fileName, report.toString(), true, true);
                }
            } //end if

            //expand one level of the explorer 
            expandFirstLevelInExplorer(newProject);

            db.commitTrans();
            db.resetHistory();

            //update the Recent Files
            if (!doItLikeANew) {
                newProject.setRamFileName(fileName);
                if (applPref != null) {
                    recentFiles.add(fileName);
                    if (mfMenu != null)
                        mfMenu.updateRecentFiles();
                } //end if
                if (statusBarModel != null)
                    statusBarModel.stopWaitingBar(MessageFormat.format(LocaleMgr.message
                            .getString("0Opened"), new Object[] { fileName }));
            } //end if

        } catch (StackOverflowError err) {
            closeProperly(db, inStream);
            message = err.toString();
            message += "\n\n";
            message += LocaleMgr.message.getString("IncreaseStackSize");
            ExceptionHandler.showErrorMessage(this, message);
        } catch (Throwable th) {
            closeProperly(db, inStream);

            if (!doItLikeANew) {
                if (statusBarModel != null)
                    statusBarModel.stopWaitingBar(null);
                if (message != null)
                    ExceptionHandler.showErrorMessage(this, message);
                else
                    ExceptionHandler.processUncatchedException(this, th);
            }
        }
        return newProject;
    }

    private void closeProperly(Db db, InputStream inStream) {
        try {
            if (inStream != null)
                inStream.close();
        } catch (Exception e) {
        }

        db.abortTrans();

        if (mfMenu != null) {
            mfMenu.updateRecentFiles();
        }
    } //end closeProperly()

    //expand one level of the explorer 
    private void expandFirstLevelInExplorer(DbProject newProject) throws DbException {
        DbRelationN relN = newProject.getComponents();
        DbObject child = relN.elementAt(0);
        if (child != null) {
            findInExplorer(child);
        } //end if
    } //end openExplorer()

    //implemented in MainFrame
    public abstract void doOpenFromXmlFile(Db db, String fileName);

    public boolean saveCurrentProject(DbProject selProject, boolean saveAs) {
        boolean saved = false;
        if (selProject == null) { //prevents a NullPointerException
            String msg = LocaleMgr.message.getString("NoProjectSelected");
            String title = LocaleMgr.message.getString("Error");
            int type = JOptionPane.ERROR_MESSAGE;
            JOptionPane.showMessageDialog(this, msg, title, type);
            return false;
        }
        
        String fileName = selProject.getRamFileName();
        if (fileName == null) {
            fileName = (m_fileChooserCurrentDirectory == null ? ApplicationContext
                    .getDefaultWorkingDirectory() : m_fileChooserCurrentDirectory);
            try {
                selProject.getDb().beginReadTrans();
                fileName += System.getProperty("file.separator") + selProject.getName() + "."
                        + smsFileFilter.getExtension();
                selProject.getDb().commitTrans();
            } catch (DbException e) {
            }
            saveAs = true;
        }
        File file = (fileName != null ? new File(fileName) : null);
        String text = LocaleMgr.screen.getString("saveProject");
        ExtensionFileFilter[] filters = SaveFileExtension.getFileFilters(); 
        AwtUtil.FileAndFilter selection = AwtUtil.showSaveAsDialog(this, text, smsFileFilter, filters, file, !saveAs);
        file = (selection == null) ? null : selection.getFile(); 
        
        if (file == null)
            return false;
        fileName = file.getAbsolutePath();

        // keep current directory for further invocation of file chooser
        // [MS]
        if (statusBarModel != null)
            statusBarModel.startWaitingBar(MessageFormat.format(LocaleMgr.message
                    .getString("savingFile0"), new Object[] { fileName }), 3000);
        m_fileChooserCurrentDirectory = fileName;

        selProject.setRamFileName(fileName);
        ObjectOutputStream outStream = null;
        File renamedFile = new File(fileName + PathFile.BACKUP_EXTENSION);
        boolean renamed = false;
        try {
            //Does file already exist? [MS]
            if (file.exists()) {
                //rename current file to make it a backup file [MS]
                if (renamedFile.exists())
                    renamedFile.delete();
                renamed = file.renameTo(renamedFile);
            }
          
            FileFilter filter = selection.getFileFilter();
            if (SaveFileExtension.canSaveFile(filter)) {
                SaveFileExtension.saveFile(filter, file);
            } else {
                saveProjectInSerializedFormat(fileName, selProject);
            }
            
            selProject.setLastSaveTrans(selProject.getDb().getTransCount());
            
            //update the Recent Files
            if (applPref != null) {
                recentFiles.add(fileName);
                if (mfMenu != null)
                    mfMenu.updateRecentFiles();
            }

            selProject.getDb().beginTrans(Db.READ_TRANS);
            JInternalFrame[] frames = getDiagramInternalFrames();
            for (int i = 0; i < frames.length; i++) {
                DiagramInternalFrame diagFrame = (DiagramInternalFrame) frames[i];
                if (diagFrame.getDiagram().getProject() == selProject)
                    diagFrame.refreshTitle();
            }
            // update some actions
            FilesAbstractAction saveAction = (FilesAbstractAction) ApplicationContext
                    .getActionStore().getAction(AbstractActionsStore.PROJECT_SAVE);
            FilesAbstractAction closeAction = (FilesAbstractAction) ApplicationContext
                    .getActionStore().getAction(AbstractActionsStore.PROJECT_CLOSE);
            saveAction.refreshNoCatch();
            closeAction.refreshNoCatch();
            selProject.getDb().commitTrans();
            if (statusBarModel != null)
                statusBarModel.stopWaitingBar(MessageFormat.format(LocaleMgr.message
                        .getString("0Saved"), new Object[] { fileName }));
            saved = true;
        } catch (Exception ex) {
            try {
                if (outStream != null) {
                    outStream.close();
                    file.delete();
                }
                if (renamed)
                    renamedFile.renameTo(file);
            } catch (Exception e) {
            }

            if (statusBarModel != null)
                statusBarModel.stopWaitingBar(null);
            ExceptionHandler.processUncatchedException(this, ex);
        }
        return saved;
    }


    private void saveProjectInSerializedFormat(String fileName, DbProject selProject) throws IOException, DbException {
        VersionConverter converter = getVersionConverter();
        ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(fileName));
        outStream.writeLong(getFileMagicNumber());
        int versions = (ApplicationContext.APPLICATION_BUILD_ID << 16)
                | (converter.getCurrentVersion() & 0x0000FFFF);
        outStream.writeInt(versions);
        outStream.writeInt(converter.getCurrentDistributorID());
        outStream.writeInt(converter.getCurrentReleaseStatus());
        selProject.save(outStream);
        outStream.close();
        outStream = null;
    }
    
    //
    // End of Open/Save Management
    //////////////////////////////////////////////

    //////////////////////////////////////////////
    // LookAndFeel Management
    //

    public class ChangeLAF implements Runnable {
        private String className;

        private JFrame frame;

        public ChangeLAF(String className, JFrame frame) {
            this.className = className;
            this.frame = frame;
        }

        public void run() {
            try {
                UIManager.setLookAndFeel(className);
                SwingUtilities.updateComponentTreeUI(frame);
            } catch (Exception ex) {
                ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), ex);
            }
        }

    }

    public final void setLookAndFeel(String lf, MetalTheme theme) {
        try {
            if (lf.equals("javax.swing.plaf.metal.MetalLookAndFeel")) { //NOT
                // LOCALIZABLE
                if (theme == null)
                    MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
                else
                    MetalLookAndFeel.setCurrentTheme(theme);
            }
            SwingUtilities.invokeLater(new ChangeLAF(lf, this));

            // Force updateUI of the designPanel if the designPanel is not
            // visible
            if (designPanel != null && !designPanel.isDesignPanelVisible())
                SwingUtilities.updateComponentTreeUI(designPanel);
        } catch (Exception ex) {
            ExceptionHandler.processUncatchedException(this, ex);
        }
        // Ensure that the renderers singletons are updated
        ScreenPlugins.updateRenderersUI();
    }

    public final void setLookAndFeel(String lf) {
        setLookAndFeel(lf, null);
    }

    //
    // End of LookAndFeel Management
    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    // Help management
    //
    public final void displayJavaHelp() {
        displayJavaHelp(null);
    }

    public final void displayJavaHelp(Object helpKey) {
        JHtmlBallonHelp helpFrame = getHelpFrame();
        if (helpFrame == null) {
            helpFrame = getJHtmlBallonHelp();
            internalFrameContainer.add(helpFrame, HELP_LAYER);
            helpFrame.setVisible(true);
        }
        try {
            helpFrame.setIcon(false);
            helpFrame.setSelected(true);
        } catch (PropertyVetoException e) {
        }
        if (helpKey != null)
            helpFrame.display(helpKey);
    }

    private JHtmlBallonHelp getHelpFrame() {
        JInternalFrame[] frames = internalFrameContainer.getAllFrames();
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] instanceof JHtmlBallonHelp)
                return (JHtmlBallonHelp) frames[i];
        }
        return null;
    }

    //
    // End of Help management
    /////////////////////////////////////////////////

    //////////////////////////////////////////////////
    // Utility Methods
    //
    public final DbProject createDefaultProject(Db db) {//throws DbException{

        if (db == null)
            db = new DbRAM();
        DbProject project = null;
        if (APPLICATION_DIRECTORY != null) {
            String fs = System.getProperty("file.separator");
            project = doOpenFromFile(db, APPLICATION_DIRECTORY + fs + getDefaultFileNameForNew(),
                    true);
        }
        if (project == null)
            project = createProject(db);

        try {
            db.beginReadTrans();
            expandFirstLevelInExplorer(project);
            db.commitTrans();
        } catch (DbException ex) {
            //ignore, do not open explorer
        }
        return project;
    }

    //must be call within a beginTrans
    public final void refreshProjectDiagrams(DbProject project) throws DbException {
        JInternalFrame[] diagFrames = getDiagramInternalFrames();
        for (int i = 0; i < diagFrames.length; i++) {
            ApplicationDiagram diag = ((DiagramInternalFrame) diagFrames[i]).getDiagram();
            if (project == diag.getProject())
                diag.populateContent();
        }
    }

    //
    // End of Utility Methods
    /////////////////////////////////////////////

    //////////////////////////////////////////////
    // DbTransListener SUPPORT
    //
    public final void dbTransBegun(Db db) {
        if (db.getTransAccess() == Db.WRITE_TRANS) {
            JInternalFrame[] frames = internalFrameContainer.getAllFrames();
            for (int i = 0; i < frames.length; i++) {
                if (frames[i] instanceof PropertiesFrame)
                    ((PropertiesFrame) frames[i]).stopEditing();
            }
        }
        if (waitCursorCount == 0)
            setCursorOnAllWindows(Cursor.WAIT_CURSOR);
        waitCursorCount++;
    }

    public final void dbTransEnded(Db db) {
        // Note that the locking occurs in beforeRefreshPass (this ensure that
        // the transaction size is known)
        // We unlock during dbTransEnded() to make sure that the GUI is unlocked
        // even if a terminate Db or abort Trans occurs.
        //if (db.isHugeTrans())
        unlockDbRefreshInGUI(this);
        if (waitCursorCount == 0)
            return;
        if (--waitCursorCount == 0)
            setCursorOnAllWindows(Cursor.DEFAULT_CURSOR);
    }

    //
    // End of DbTransListener SUPPORT
    //////////////////////////////////////////////

    //////////////////////////////////////////////
    // DbRefreshPassListener SUPPORT
    // Refresh diagrams for graphical components position
    //
    public final void beforeRefreshPass(Db db) throws DbException {
        JInternalFrame[] diagFrames = getDiagramInternalFrames();
        for (int i = 0; i < diagFrames.length; i++) {
            ApplicationDiagram diag = ((DiagramInternalFrame) diagFrames[i]).getDiagram();
            if (diag.getDiagramGO().getDb() == db)
                diag.beginComputePos();
        }
        projectsNeedingRefresh = null;
        if (db.isHugeTrans() && !db.isTerminating()) {
            lockDbRefreshInGUI(this);
        }
    }

    public final void afterRefreshPass(Db db) throws DbException {
        if (projectsNeedingRefresh != null) {
            int nb = projectsNeedingRefresh.size();
            for (int i = 0; i < nb; i++)
                refreshProjectDiagrams((DbProject) projectsNeedingRefresh.elementAt(i));
            projectsNeedingRefresh = null;
        }
        Graphics g = getGraphics();
        JInternalFrame[] diagFrames = getDiagramInternalFrames();
        for (int i = 0; i < diagFrames.length; i++) {
            ApplicationDiagram diag = ((DiagramInternalFrame) diagFrames[i]).getDiagram();
            if (diag.getDiagramGO().getDb() == db)
                diag.endComputePos(g);
        }

        if (g != null) {
            g.dispose(); //to avoid a NullPointerException [MS]
        }
    }

    //
    // End of DbRefreshPassListener SUPPORT
    /////////////////////////////////////////////

    //////////////////////////////////////////////
    // DbRefreshListener SUPPORT
    //
    public final void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (!event.dbo.hasField(DbGraphic.fStyleName))
            return;
        DbObject style = event.dbo;
        /*
         * if (style.getTransStatus() != Db.OBJ_REMOVED &&
         * style.get(DbGraphic.fProjectDefaultStyle.getOppositeRel(null)) == null) { // not the
         * default style if (style.getNbNeighbors(DbGraphic.fDiagramStyle.getOppositeRel(null)) == 0
         * && style.getNbNeighbors(DbGraphic.fGraphicalObjectStyle.getOppositeRel(null)) == 0)
         * return; // no reference to the style means nothing to refresh. }
         */DbProject project = (DbProject) style.getAccordingToStatus(DbObject.fComposite);
        if (project.getTransStatus() != Db.OBJ_REMOVED) {
            if (projectsNeedingRefresh == null)
                projectsNeedingRefresh = new SrVector();
            if (!projectsNeedingRefresh.contains(project))
                projectsNeedingRefresh.addElement(project);
        }
    }

    //
    // End of DbRefreshListener SUPPORT
    /////////////////////////////////////////////

    //////////////////////////////////////////
    // Private Methods
    //
    private void setCursorOnAllWindows(int cursor) {
        JRootPane rootpane = getRootPane();
        if (rootpane != null)
            rootpane.setCursor(Cursor.getPredefinedCursor(cursor));
    }

    //
    // End of Private Methods
    //////////////////////////////////////////

    // Getters
    public final String getApplicationDirectory() {
        return APPLICATION_DIRECTORY;
    }

    public static final void registerInitialiser(Runnable runnable) {
        if (initialisers == null) // Initialisation process is completed
            return;
        if (!initialisers.contains(runnable))
            initialisers.add(runnable);
    }

    public final void registerPreFinalizer(Runnable runnable) {
        if (!preFinalizers.contains(runnable))
            preFinalizers.add(runnable);
    }

    public final void registerPostFinalizer(Runnable runnable) {
        if (!postFinalizers.contains(runnable))
            postFinalizers.add(runnable);
    }

    //////////////////////////////////////////
    // Abstract Methods
    //
    protected abstract long getFileMagicNumber();

    protected abstract int getMinimumBuild();

    protected abstract JHtmlBallonHelp getJHtmlBallonHelp();

    protected abstract VersionConverter getVersionConverter();

    protected abstract void convertForDebugUpdate(DbProject project, int version, int build)
            throws DbException;

    protected abstract String getModelFileExtension();

    protected abstract String getDefaultFileNameForNew();

    public abstract ExplorerPanel getExplorerPanel();

    public abstract boolean isHelpInstalled();

    public abstract Clipboard getClipboard();

    protected abstract DiagramInternalFrame createDiagramInternalFrame(DbObject diag)
            throws DbException;

    public abstract ToolButtonGroup getDiagramsToolGroup();

    public abstract Dimension getDefaultInternalFrameSize();

    protected abstract PropertiesFrame createPropertiesFrame(DbObject obj) throws DbException;

    protected abstract ListInternalFrame createListFrame(DbObject root, ListDescriptor descriptor)
            throws DbException;

    public abstract boolean supportsPropertiesFrame(DbObject obj) throws DbException;

    public abstract DbProject createProject(Db db);
    //
    // End of Abstract Method
    ////////////////////////////////////////////////
}
