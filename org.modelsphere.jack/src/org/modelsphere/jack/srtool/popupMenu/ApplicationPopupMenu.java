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

package org.modelsphere.jack.srtool.popupMenu;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Component;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.Action;

import org.modelsphere.jack.actions.*;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.awt.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.xml.XmlPluginAction;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.FocusManager;
import org.modelsphere.jack.srtool.actions.AbstractSelectAction;
import org.modelsphere.jack.srtool.actions.PluginGenerationAction;
import org.modelsphere.jack.srtool.explorer.ExplorerView;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.jack.util.SrVector;

public abstract class ApplicationPopupMenu {

    protected static final String SEPARATOR = "separator"; //NOT LOCALIZABLE, property key
    private static final String kSelect = LocaleMgr.screen.getString("Select");
    private static final String kRestrictedConcept = LocaleMgr.screen
            .getString("RestrictedConcept");

    /**
     * This array is a mirror of a popupmenu
     * 
     * Order is important. Each action type is seperated by a SEPARATOR
     */
    protected static String[] itemMenuNames = null;

    /**
     * This array contains all objects associate to a popupmenu
     */
    protected static Object[] instanceWithPopupMenu = null;

    private static HashMap itemNameMap = null;
    private static Object[] popupMenuByClass = null;

    private static final int DISCARD = 0x10000;

    // mult be called before first call to getPopupMenu();
    public void registerPopups(Object[] popupMenus, String[] actionkeys) {
        if (popupMenus == null || itemMenuNames == null || instanceWithPopupMenu == null)
            return;
        SrVector vecPopupMenus = new SrVector(Arrays.asList(instanceWithPopupMenu));
        vecPopupMenus.insertElements(vecPopupMenus.size(), popupMenus, 0, popupMenus.length);
        instanceWithPopupMenu = vecPopupMenus.toArray();

        SrVector vecItemMenuNames = new SrVector(Arrays.asList(itemMenuNames));
        vecItemMenuNames.insertElements(vecItemMenuNames.size(), actionkeys, 0, actionkeys.length);
        int size = vecItemMenuNames.size();
        itemMenuNames = new String[size];
        for (int i = 0; i < size; i++) {
            itemMenuNames[i] = (String) vecItemMenuNames.get(i);
        }
    }

    public abstract JackPopupMenu getPopupMenu(boolean useContainer);

    protected static JackPopupMenu getPopupMenu(AbstractActionsStore actionStore,
            boolean useContainer, String[] explorerPopupMenu, String[] diagramPopupMenu) {
        initialize();
        FocusManager focusManager = ApplicationContext.getFocusManager();
        focusManager.update(); // fire pending events of focus manager.

        int[] selArray = new int[itemMenuNames.length];
        int selCount = 0;
        Object[] selObjects = focusManager.getSelectedObjects();

        if (selObjects.length > 0 && !useContainer) {
            HashSet classes = new HashSet();
            for (int i = 0; i < selObjects.length; i++) {
                Object object = selObjects[i];
                if (!classes.add(object.getClass()))
                    continue;
                int[] indices = getItemIndices(object);
                if (indices == null)
                    return null;
                for (int j = 0; j < indices.length; j++) {
                    if ((indices[j] & DISCARD) == 0)
                        selArray[indices[j]]++;
                }
                selCount++;
            }
        } else {
            DbProject project = focusManager.getCurrentProject();
            selObjects = (project == null ? new Object[0] : new Object[] { project });

            Object focusObject = focusManager.getFocusObject();
            int[] indices = null;
            if (focusObject instanceof ExplorerView)
                indices = convertToIndices(explorerPopupMenu);
            else if (focusObject instanceof ApplicationDiagram)
                indices = convertToIndices(diagramPopupMenu);
            if (indices == null)
                return null;
            for (int j = 0; j < indices.length; j++) {
                if ((indices[j] & DISCARD) == 0)
                    selArray[indices[j]]++;
            }
            selCount++;
        }

        try {
            DbMultiTrans.beginTrans(Db.READ_TRANS, selObjects, null);
            JackPopupMenu popup = createPopupMenu(actionStore, selArray, selCount, selObjects);
            if (selObjects.length == 1) {
                Class claz = selObjects[0].getClass();
                String name = claz.getName();
                popup.setName(name); // QA need this for single popup selection tests
            }
            //DbMultiTrans.commitTrans(selObjects);

            ArrayList arrayToCommit = new ArrayList();
            for (int w = 0; w < selObjects.length; w++) {
                Db db = null;
                if (selObjects[w] instanceof DbObject)
                    db = ((DbObject) selObjects[w]).getDb();
                else if (selObjects[w] instanceof ActionInformation)
                    db = ((ActionInformation) selObjects[w]).getDb();
                if (db != null)
                    if (db.getTransMode() != Db.TRANS_NONE)
                        arrayToCommit.add(selObjects[w]);
            }
            DbMultiTrans.commitTrans(arrayToCommit.toArray());

            return popup;
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
            return null;
        }
    }

    private static JackPopupMenu createPopupMenu(AbstractActionsStore actionStore, int[] selArray,
            int selCount) throws Exception {
        JackPopupMenu popup = createPopupMenu(actionStore, selArray, selCount, null);
        return popup;
    }

    private static JackPopupMenu createPopupMenu(AbstractActionsStore actionStore, int[] selArray,
            int selCount, Object[] selObjects) throws Exception {
        JackPopupMenu popup = new JackPopupMenu();
        JackMenu generateMenu = null;
        JackMenu selectMenu = null;
        ArrayList pluginMenuActions = null;

        // check if not the last item and
        // check if two SEPARATOR without item menu
        boolean previousItemIsSeparator = true;

        for (int i = 0; i < itemMenuNames.length; i++) {
            if (itemMenuNames[i].equals(SEPARATOR)) {
                if (!previousItemIsSeparator)
                    popup.addSeparator();
                previousItemIsSeparator = true;
                continue;
            }
            if (selArray[i] < selCount)
                continue;

            String key = itemMenuNames[i];
            AbstractApplicationAction action = actionStore.getAction(key, selObjects);

            if (action != null) {

                // Make sure an error on one action (may be a plugin action) does not corrupt the popup
                try {
                    // Update action state before showing the menu, except if ONLINE action,
                    // because in this case the action is always up to date.
                    if (action.getUpdateSelectionMode() == AbstractApplicationAction.UPDATE_SELECTION_OFFLINE) {
                    	SelectionActionListener selectionAction = (SelectionActionListener)action;
                    	selectionAction.updateSelectionAction();
                    }
                } catch (Exception ex) {
                    //ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), ex);
                    if (Debug.isDebug()) {
                        ex.printStackTrace();
                    }
                    action.setEnabled(false);
                } catch (Error er) {
                    if (Debug.isDebug()) {
                        er.printStackTrace();
                    }
                    //ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), er);
                    action.setEnabled(false);
                } //end try

                if (action.isVisible()) {
                    boolean itemaddedtopluginsmenu = false;
                    if (action instanceof AbstractSelectAction) {
                        if (selectMenu == null) {
                            selectMenu = new JackMenu(kSelect);
                            popup.add(selectMenu);
                        }
                        selectMenu.add(action);
                    } else if (action instanceof PluginGenerationAction) { // must group all add actions in a submenu if more than one
                        if (generateMenu == null) {
                            generateMenu = new JackMenu(LocaleMgr.action.getString("Generate"),
                                    true);
                        }
                        generateMenu.add(action);
                        itemaddedtopluginsmenu = true;
                    } else if (action instanceof PluginAction) { // must group all add actions in a submenu if more than one
                        if (pluginMenuActions == null) {
                            pluginMenuActions = new ArrayList();
                        }
                        pluginMenuActions.add(action);
                        itemaddedtopluginsmenu = true;
                    } else if (action instanceof XmlPluginAction) {
                    	XmlPluginAction xmlPluginAction = (XmlPluginAction)action;
                    	xmlPluginAction.addActionInPopup(popup, selObjects);
                    } else {
                        String text = action.getText(selObjects);
                        boolean enabled = action.isEnabled(selObjects);
                        if (enabled) {
                            popup.add(action, text, selObjects);
                        }
                    } //end if

                    if (!itemaddedtopluginsmenu) {
                        previousItemIsSeparator = false;
                    } //end if
                } //end if
            } //end if
        } //end for

        if (generateMenu != null || pluginMenuActions != null) {
            if ((popup.getComponentCount() > 0)
                    && !(popup.getComponent(popup.getComponentCount() - 1) instanceof JPopupMenu.Separator))
                popup.addSeparator();
            if (generateMenu != null) {
                popup.add(generateMenu);
                // remove 'Generete' from sub items
                String generateText = generateMenu.getText();
                Component[] items = generateMenu.getMenuComponents();
                for (int i = 0; items != null && i < items.length; i++) {
                    if (!(items[i] instanceof JMenuItem))
                        continue;
                    JMenuItem subitem = (JMenuItem) items[i];
                    String itemtext = subitem.getText();
                    if (!itemtext.toLowerCase().startsWith(generateText.toLowerCase()))
                        continue;
                    subitem.setText(itemtext.substring(generateText.length(), itemtext.length())
                            .trim());
                } //end for
            } //end if
            if (pluginMenuActions != null) {
                if (pluginMenuActions.size() > 2) { // create a submenu
                    JackMenu pluginMenu = new JackMenu(LocaleMgr.action.getString("Plugins"), true);
                    Iterator iter = pluginMenuActions.iterator();
                    while (iter.hasNext()) {
                        pluginMenu.add((Action) iter.next());
                    }
                    popup.add(pluginMenu);
                } else { // add to popup
                    Iterator iter = pluginMenuActions.iterator();
                    while (iter.hasNext()) {
                        popup.add((Action) iter.next());
                    }
                }
            } //end if
        } //end if

        int popupcomponentcount = popup.getComponentCount();
        if (popupcomponentcount == 0)
            popup = null;
        else if (popup.getComponent(popupcomponentcount - 1) instanceof JPopupMenu.Separator)
            popup.remove(popupcomponentcount - 1);

        DbObject[] objs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        boolean bShow = TerminologyUtil.getShowPhysicalConcepts(objs);
        boolean bIsPureERSet = TerminologyUtil.getInstance().isPureERSet(objs);
        if (!bIsPureERSet && bShow == false) {
            popup = new JackPopupMenu();
            JMenuItem menu = new JMenuItem(kRestrictedConcept);
            menu.setEnabled(false);
            popup.add(menu);
            return popup;
        }

        return popup;
    }

    // Combine the menu lists of all the classes or interfaces the object belongs to.
    private static int[] getItemIndices(Object object) {
        int[] indices = null;
        Object semObj = null; // Used to combine semantical popup to the graphical object popup
        if (object instanceof ActionInformation)
            semObj = ((ActionInformation) object).getSemanticalObject();
        for (int i = 0; i < popupMenuByClass.length; i += 2) {
            if (!((Class) popupMenuByClass[i]).isInstance(object)
                    && !((Class) popupMenuByClass[i]).isInstance(semObj))
                continue;
            if (indices == null)
                indices = (int[]) popupMenuByClass[i + 1];
            else {
                int[] oldInds = indices;
                int[] newInds = (int[]) popupMenuByClass[i + 1];
                indices = new int[oldInds.length + newInds.length];
                System.arraycopy(oldInds, 0, indices, 0, oldInds.length);
                // If the same index appears in both old and new list, OR the DISCARD flag of both entries
                // and save it in new list, then set entry in old list to 0xffffffff.
                for (int j = 0; j < newInds.length; j++) {
                    int ind = newInds[j];
                    for (int k = 0; k < oldInds.length; k++) {
                        if ((indices[k] | DISCARD) == (ind | DISCARD)) {
                            ind |= indices[k]; // keep the DISCARD flag
                            indices[k] = 0xffffffff;
                        }
                    }
                    indices[oldInds.length + j] = ind;
                }
            }
        }
        return indices;
    }

    // Build once <popupMenuByClass> where the item names are replaced by their indice in <itemMenuNames>
    // This allows a much faster compilation of popup menus.
    private static void initialize() {
        if (itemNameMap != null)
            return;
        itemNameMap = new HashMap();
        int i;
        for (i = 0; i < itemMenuNames.length; i++)
            itemNameMap.put(itemMenuNames[i], new Integer(i));

        popupMenuByClass = new Object[instanceWithPopupMenu.length];
        for (i = 0; i < popupMenuByClass.length; i += 2) {
            popupMenuByClass[i] = instanceWithPopupMenu[i];
            String[] itemNames = (String[]) instanceWithPopupMenu[i + 1];
            popupMenuByClass[i + 1] = convertToIndices(itemNames);
        }
    }

    private static int[] convertToIndices(String[] itemNames) {
        int[] indices = new int[itemNames.length];
        for (int i = 0; i < itemNames.length; i++) {
            int discard = 0;
            String name = itemNames[i];
            if (name.charAt(0) == AbstractActionsStore.DISCARD) {
                discard = DISCARD;
                name = name.substring(1);
            }
            Integer index = (Integer) itemNameMap.get(name);
            if (index == null) {
                String msg = "INTERNAL ERROR : String '"
                        + name
                        + "'; See jack.srtool.popup.ApplicationPopupMenu.convertToIndices() code source to have hints about fixing this bug."; //NOT LOCALIZABLE, for Modelsphere's developers
                throw new RuntimeException(msg);
                //One kwnon cause can produce this exception, if you generate it, here is what you can do:
                //
                //   a) Open in sms.popup.ApplicationPopupMenu class
                //   b) Look at its constructor
                //   c) SMSActionsStore.JAVA_ADD_XXX shoulb be missing
            }

            if (Debug.isDebug()) {
                Debug.assert2(index != null, name + " missing in itemMenuNames");
                for (int j = 0; j < i; j++)
                    Debug
                            .assert2((indices[j] & ~DISCARD) != index.intValue(), name
                                    + " duplicated");
            }
            indices[i] = index.intValue() | discard;
        }
        return indices;
    }
}
