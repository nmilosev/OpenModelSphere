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

package org.modelsphere.jack.actions;

import java.util.Enumeration;
import java.util.Hashtable;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.actions.*;

/**
 * 
 * All the actions are managed by an AbstractActionsStore. When adding a menu item or a button, use:
 * menu.add(AbstractActionsStore.getSingleton().getAction(key)); where key = a static field of the
 * AbstractActionsStore. example: to add a menu item that must perform an exit action:
 * mymenu.add(AbstractActionsStore.getSingleton().getAction
 * (AbstractActionsStore.APPLICATION_EXIT)); This way, there is only one instance for each action in
 * the application. Never use the key string directly. Use the static field instead. This string may
 * be internationalize.
 * 
 */
public abstract class AbstractActionsStore extends Hashtable {

    public static final char DISCARD = '~';

    // //////////////////////////////////////////////////////////////////////////
    // Keys

    public static final String ALIGN_ZONE_BOX = "Align Zone Box"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String APPLICATION_CLOSE_ALL = "Close All Project"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String APPLICATION_EXIT = "Exit Application"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String APPLICATION_REFRESH = "Refresh Application"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String APPLICATION_SHOW_DESIGN_PANEL = "Show Design Panel"; // NOT
    // LOCALIZABLE,
    // internal
    // code
    public static final String APPLICATION_SHOW_EXPLORER = "Show Explorer"; // NOT
    // LOCALIZABLE,
    // internal
    // code
    public static final String APPLICATION_SHOW_GRID = "Show Grid"; // NOT
    // LOCALIZABLE,
    // internal
    // code
    public static final String APPLICATION_ACTIVATE_GRID = "Activate Grid"; // NOT
    // LOCALIZABLE,
    // internal
    // code
    public static final String APPLICATION_SHOW_PLUGINS_INFO = "Show Plugins Info"; // NOT
    // LOCALIZABLE,
    // internal
    // code
    public static final String APPLICATION_SHOW_STATUSBAR = "Show Status Bar";// NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String APPLICATION_SHOW_OVERVIEW_WINDOW = "Show Overview Window"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String APPLICATION_SHOW_MAGNIFIER_WINDOW = "Show Magnifier Window"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String APPLICATION_SHOW_TECH_INFO = "Show Teck Info"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String AUTO_FIT = "autoFit"; // NOT LOCALIZABLE,
    // property key
    public static final String CHANGE_STAMP_IMAGE = "ChangeStampImage"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String REMOVE_STAMP_IMAGE = "RemoveStampImage"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String COPY = "copy"; // NOT LOCALIZABLE, property key
    public static final String COPY_IMAGE = "copyImage"; // NOT LOCALIZABLE,
    // property key
    public static final String COPY_STAMP_IMAGE = "CopyStampImage"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String DB_CONNECT = "db Connect"; // NOT LOCALIZABLE,
    // property key
    public static final String DB_CONNECTION_INFO = "db Connection Info"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String DELETE = "delete"; // NOT LOCALIZABLE, property
    // key
    public static final String DIAGRAM_LAYOUT_CASCADE = "Cascade JInternalFrame"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String DIAGRAM_LAYOUT_TILE_HORIZONTAL = "Tile Horizontal JInternalFrame"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String DIAGRAM_LAYOUT_TILE_VERTICAL = "Tile Vertical JInternalFrame"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String DIAGRAM_CLOSE_ALL = "Close All JInternalFrame"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String DIAGRAM_ARRANGE_ICONS = "Arrange JInternalFrame"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String FIND_IN_DIAGRAM = "FindInDiagram"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String FIND_IN_EXPLORER = "FindInExplorer"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String FIT = "fit"; // NOT LOCALIZABLE, property key
    public static final String FONT = "font"; // NOT LOCALIZABLE, property key
    public static final String HELP_SHOW = "Show Help"; // NOT LOCALIZABLE,
    // property key
    public static final String INSERT_DIAGRAM_IMAGE = "InsertDiagramImage"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String LAYOUT_RIGHT_ANGLE = "layoutRightAngle"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String LIST = "list"; // NOT LOCALIZABLE, property key
    public static final String PAGE_BREAKS = "pageBreaks"; // NOT LOCALIZABLE,
    // property key
    public static final String PASTE = "paste"; // NOT LOCALIZABLE, property key
    public static final String PASTE_DIAGRAM_IMAGE = "pasteImageInDiagram";// NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String PASTE_STAMP_IMAGE = "PasteStampImage"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String PROJECT_PAGE_SETUP = "Project Page Setup"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String PROJECT_CLOSE = "Close Project"; // NOT
    // LOCALIZABLE,
    // property key
    public static final String PROJECT_FIND = "Find in Project..."; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String PROJECT_FIND_NEXT = "Find Next"; // NOT
    // LOCALIZABLE,
    // property key
    public static final String PROJECT_FIND_PREVIOUS = "Find Previous"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String PROJECT_NEW = "New Project"; // NOT LOCALIZABLE,
    // property key
    public static final String PROJECT_OPEN = "Open Project"; // NOT
    // LOCALIZABLE,
    // property key
    public static final String PROJECT_PRINT = "Print Project"; // NOT
    // LOCALIZABLE,
    // property key
    public static final String PROJECT_PROPERTIES = "Project Properties"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String PROJECT_UNDO = "Undo Project Command"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String PROJECT_REDO = "Redo Project Command"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String PROJECT_SAVE = "Save Project"; // NOT
    // LOCALIZABLE,
    // property key
    public static final String PROJECT_SAVE_AS = "Save As Project"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String PROJECT_UDF = "Edit Project UDF"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String PROPERTIES = "properties"; // NOT LOCALIZABLE,
    // property key
    public static final String PYTHON_SHELL = "pythonShell"; // NOT LOCALIZABLE,
    // property key
    public static final String REFRESH_ALL = "refreshAll"; // NOT LOCALIZABLE,
    // property key
    public static final String REVERT_TO_ORIGINAL_SIZE = "RevertToOriginalSize"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String RIGHT_ANGLE = "rightAngle"; // NOT LOCALIZABLE,
    // property key
    public static final String SAVE_CLIPBOARD_IMAGE = "saveCipboardImage"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String SELECT_ALL = "selectAll"; // NOT LOCALIZABLE,
    // property key
    public static final String SELECT_LINES = "select Lines"; // NOT
    // LOCALIZABLE,
    // property key
    public static final String SELECT_LABELS = "select Labels"; // NOT
    // LOCALIZABLE,
    // property key
    public static final String SET_DRAWING_AREA = "setDrawingArea"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String SET_IMAGE_OPACITY = "setImageOpacity"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String SET_PAGE_NUMBER = "setPageNumber"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String SHOW_DIAGRAM = "showDiagram"; // NOT LOCALIZABLE,
    // property key
    public static final String STRAIGHTEN = "straighten"; // NOT LOCALIZABLE,
    // property key
    public static final String SYSTEM_GARBAGE_COLLECT = "System Garbage Collect";// NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String REPOSITION_LABEL = "Reposition Label"; // NOT
    // LOCALIZABLE,
    // property
    // key
    public static final String EXPAND_ALL = "Expand All"; // NOT LOCALIZABLE,
    // property key
    public static final String COLLAPSE_ALL = "Collapse All"; // NOT
    // LOCALIZABLE,
    // property key
    public static final String ZOOM_IN = "Zoom In"; // NOT LOCALIZABLE, property
    // key
    public static final String ZOOM_OUT = "Zoom Out"; // NOT LOCALIZABLE,

    // property key

    protected AbstractActionsStore() {
        super();
        put(ALIGN_ZONE_BOX, new AlignBoxAction());
        put(APPLICATION_CLOSE_ALL, new CloseAllAction());
        put(APPLICATION_EXIT, new ExitAction());
        put(APPLICATION_REFRESH, new RefreshAction());
        put(APPLICATION_SHOW_DESIGN_PANEL, new ShowDesignPanelAction());
        put(APPLICATION_SHOW_EXPLORER, new ShowExplorerAction());
        put(APPLICATION_SHOW_GRID, new ShowGridAction());
        put(APPLICATION_ACTIVATE_GRID, new ActivateGridAction());
        put(APPLICATION_SHOW_PLUGINS_INFO, new ShowPluginListAction());
        put(APPLICATION_SHOW_STATUSBAR, new ShowStatusBarAction());
        put(APPLICATION_SHOW_OVERVIEW_WINDOW, new ShowOverviewAction());
        put(APPLICATION_SHOW_MAGNIFIER_WINDOW, new ShowMagnifierAction());
        put(APPLICATION_SHOW_TECH_INFO, new ShowTechInfoAction());
        put(AUTO_FIT, new AutoFitAction());
        put(CHANGE_STAMP_IMAGE, new ChangeStampImageAction());
        put(REMOVE_STAMP_IMAGE, new RemoveStampImageAction());
        put(COPY, new CopyAction());
        put(COPY_IMAGE, new CopyImageAction());
        put(COPY_STAMP_IMAGE, new CopyStampImageAction());
        put(DB_CONNECT, new ConnectAction());
        put(DB_CONNECTION_INFO, new ConnectionInfoAction());
        put(DELETE, new DeleteAction());
        put(DIAGRAM_LAYOUT_CASCADE, new JInternalFrameCascadeLayoutAction());
        put(DIAGRAM_LAYOUT_TILE_HORIZONTAL, new JInternalFrameTileHorizontalLayoutAction());
        put(DIAGRAM_LAYOUT_TILE_VERTICAL, new JInternalFrameTileVerticalLayoutAction());
        put(DIAGRAM_CLOSE_ALL, new JInternalFrameCloseAllAction());
        put(DIAGRAM_ARRANGE_ICONS, new JInternalFrameArrangeIconsAction());
        put(FIND_IN_DIAGRAM, new FindInDiagramAction());
        put(FIND_IN_EXPLORER, new FindInExplorerAction());
        put(FIT, new FitAction());
        put(FONT, new FontAction());
        put(HELP_SHOW, new HelpAction());
        put(INSERT_DIAGRAM_IMAGE, new InsertDiagramImageAction());
        put(LAYOUT_RIGHT_ANGLE, new LayoutRightAngleAction());
        put(LIST, new ListAction());
        put(PAGE_BREAKS, new PageBreaksAction());
        put(PASTE, new PasteAction());
        put(PASTE_DIAGRAM_IMAGE, new PasteDiagramImageAction());
        put(PASTE_STAMP_IMAGE, new PasteStampImageAction());
        put(PROJECT_PAGE_SETUP, new PageSetupAction());
        put(PROJECT_CLOSE, new CloseAction());
        put(PROJECT_FIND, new FindAction());
        put(PROJECT_FIND_NEXT, new FindNextAction());
        put(PROJECT_FIND_PREVIOUS, new FindPreviousAction());
        put(PROJECT_NEW, new NewAction());
        put(PROJECT_OPEN, new OpenAction());
        put(PROJECT_PRINT, new PrintAction());
        put(PROJECT_PROPERTIES, new ProjectPropertiesAction());
        put(PROJECT_UNDO, new UndoAction());
        put(PROJECT_REDO, new RedoAction());
        put(PROJECT_SAVE, new SaveAction());
        put(PROJECT_SAVE_AS, new SaveAsAction());
        put(PROJECT_UDF, new UDFAction());
        put(PROPERTIES, new PropertiesAction());
        put(PYTHON_SHELL, PythonShellAction.getSingleton());
        put(REFRESH_ALL, new RefreshAllAction());
        put(REVERT_TO_ORIGINAL_SIZE, new RevertToOriginalSizeAction());
        put(RIGHT_ANGLE, new RightAngleAction());
        put(SAVE_CLIPBOARD_IMAGE, new SaveClipboardImageAction());
        put(SELECT_ALL, new SelectAllAction());
        put(SELECT_LABELS, new SelectAllLabelsAction());
        put(SET_DRAWING_AREA, new SetDrawingAreaAction());
        put(SET_IMAGE_OPACITY, new SetImageOpacityAction());
        put(SET_PAGE_NUMBER, new SetPageNumberAction());
        put(SHOW_DIAGRAM, new ShowDiagramAction());
        put(STRAIGHTEN, new StraightenAction());
        put(SYSTEM_GARBAGE_COLLECT, new DoGarbageCollectAction());
        put(REPOSITION_LABEL, new RepositionLabelAction());
        put(EXPAND_ALL, new ExpandAllAction());
        put(COLLAPSE_ALL, new CollapseAllAction());
        put(ZOOM_IN, new ZoomAction(true));
        put(ZOOM_OUT, new ZoomAction(false));
    }

    // Use to obtain an action corresponding to the key.
    // key: use static field of this class.
    public final AbstractApplicationAction getAction(String key) {
        AbstractApplicationAction action = getAction(key, null);
        return action;
    } // end AbstractApplicationAction()

    public final AbstractApplicationAction getAction(String key, Object[] selObjects) {
        Debug.assert2(key != null, "null key on AbstractActionsStore.getAction(String)"); // NOT
        // LOCALIZABLE,
        // debug
        // only
        if (key != null) {
            if (containsKey(key)) {
                AbstractApplicationAction action = (AbstractApplicationAction) get(key);
                return action;
            } else {
                return null;
            }
        } // end if

        return null;
    } // end getAction()

    // Use to obtain an action instance corresponding to the action class.
    public final AbstractApplicationAction getAction(Class c) {
        Debug.assert2(c != null, "null class on AbstractActionsStore.getAction(Class)"); // NOT
        // LOCALIZABLE,
        // debug
        // only
        if (c != null) {
            Enumeration enumeration = keys();
            while (enumeration.hasMoreElements()) {
                Object key = (Object) enumeration.nextElement();
                if (key == null)
                    continue;
                Object action = get(key);
                if (c.isInstance(action))
                    return (AbstractApplicationAction) action;
            }
        }
        return null;
    }

    // Call this method to perform the action using the key representing the
    // action.
    public final void performAction(String key) {
        AbstractApplicationAction a = getAction(key);
        if (a != null)
            a.doActionPerformed();
        else {
            Debug.assert2(false, "invalid key specified on AbstractActionsStore.performAction()"); // NOT
            // LOCALIZABLE,
            // debug
            // only
        }
    }

    public final Object put(Object key, Object value) {
        Debug.assert2(key instanceof String, "AbstractActionsStore.put():  Key must be a String"); // NOT
        // LOCALIZABLE,
        // debug
        // only
        Debug.assert2(((String) key).charAt(0) != DISCARD,
                "AbstractActionsStore.put():  Key must not begin with " + DISCARD); // NOT LOCALIZABLE, debug only
        Debug.assert2(value instanceof AbstractApplicationAction,
                "AbstractActionsStore.put():  Must be an AbstractApplicationAction object"); // NOT
        // LOCALIZABLE,
        // debug
        // only
        Object old = super.put(key, value);
        Debug.assert2(old == null, "AbstractActionsStore.put():  Action key duplicated.  Key="
                + key + "."); // NOT LOCALIZABLE, debug only
        return old;
    }
}
