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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

import javax.swing.*;

import org.modelsphere.jack.awt.JackPopupMenu;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.debug.Log;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.SelectionChangedEvent;
import org.modelsphere.jack.srtool.SelectionListener;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.util.SrVector;

/**
 * 
 * All application actions should inherit this class. When adding a menu item to a Menu, use
 * menu.add(applicationAction). This method return the JMenuItem (Provided by JMenu). Work the same
 * way for the JToolBar buttons. The action ensures that: -- the code for an action is always the
 * same. -- the text is the same. -- the image is the same. ApplicationAction allows other classes
 * to register as ApplicationActionListener. An ApplicationActionListener get triggerred after the
 * user trigger an ActionEvent on the action menuitems or buttons.
 * 
 * Some abstract actions classes exist with more specific needs.
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractApplicationAction extends AbstractAction implements SelectionListener {

    private static final Random random = new Random(System.currentTimeMillis());

    // keys for the properties
    public static final String VISIBLE = "visible"; // NOT LOCALIZABLE, property
    // key
    public static final String TOOLBAR_VISIBLE_OPTION = "ToolBarVisible"; // NOT
    // LOCALIZABLE,
    // property
    // key

    // New Action Properties -- support for PropertyChangeListener Mechanism
    private boolean visible = true;
    // VISIBILITY_DEFAULT: The visibility will depends on the visibility
    // property for the action.
    public static final int VISIBILITY_DEFAULT = 0x00;
    // VISIBILITY_ALWAYS_VISIBLE_IN_MENU: The visibility for this action menu
    // components will always be true.
    // Menu components won't be shown of hidden according to the visibility
    // property. A call to setVisible on the action
    // won't affect the menu components.
    public static final int VISIBILITY_ALWAYS_VISIBLE_IN_MENU = 0x01;
    // VISIBILITY_ALWAYS_VISIBLE_IN_TOOLBAR: The visibility for this action
    // toolbar components will always be true.
    // Toolbar components won't be shown of hidden according to the visibility
    // property. A call to setVisible on the action
    // won't affect the toolbar components.
    public static final int VISIBILITY_ALWAYS_VISIBLE_IN_TOOLBAR = 0x02;
    // VISIBILITY_ALWAYS_VISIBLE_IN_POPUPMENU: The visibility for this action
    // popup menu components will always be true.
    // Popup menu components won't be shown of hidden according to the
    // visibility property. A call to setVisible on the action
    // won't affect the popup menu components.
    public static final int VISIBILITY_ALWAYS_VISIBLE_IN_POPUPMENU = 0x04;
    private int visibilityMode = VISIBILITY_ALWAYS_VISIBLE_IN_MENU
            | VISIBILITY_ALWAYS_VISIBLE_IN_TOOLBAR;

    // This field is useless if the action has not previously been added to a
    // JackToolBar.
    private boolean defaultToolBarVisibility = true;

    private SrVector applicationActionListeners = new SrVector();

    // updateSelectionMode possible values
    public static final int UPDATE_SELECTION_NONE = 0; // action not instanceof
    // SelectionActionListener
    public static final int UPDATE_SELECTION_ONLINE = 1; // action that must be
    // updated
    // continuouly
    // (always visible
    // in GUI)
    public static final int UPDATE_SELECTION_OFFLINE = 2; // action that is
    // updated on demand
    // (before action
    // components become
    // visible)

    private int updateSelectionMode = UPDATE_SELECTION_NONE;
    private boolean useNameAsToolTips = true; // If true, the name will be used
    // as tooltips. Will stay true
    // unless a call to
    // putValue(Action.SHORT_DESCRIPTION, value) is executed.
    private String preferenceID = "";

    class PreferenceListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            if ((visibilityMode & VISIBILITY_ALWAYS_VISIBLE_IN_TOOLBAR) != 0) {
                firePropertyChange(TOOLBAR_VISIBLE_OPTION, null, new Boolean(
                        getToolBarVisibilityOption()));
            }
        }
    }

    private final PreferenceListener prefListener = new PreferenceListener();

    // Constructors
    public AbstractApplicationAction(String name) {
        this(name, null);
    }

    public AbstractApplicationAction(String name, Icon icon) {
        super(name, icon);
        Debug.assert2(name != null && name.length() > 0, "null String for ApplicationAction name");

        if (name != null) {
            super.putValue(Action.SHORT_DESCRIPTION, name); // By default:
            // Duplication of
            // name for use with
            // tooltips
        }

        if (this instanceof SelectionActionListener) {
            updateSelectionMode = UPDATE_SELECTION_OFFLINE;
        }

        String key = getToolBarVisibilityKey(this);
        PropertiesManager.getPropertiesSet(key);
        PropertiesSet properties = PropertiesManager.getPreferencePropertiesSet();

        if (properties != null) {
            properties.addPropertyChangeListener(key, TOOLBAR_VISIBLE_OPTION, prefListener);
        }
    }

    // end constructors

    public final String toString() {
        return (String) getValue(NAME);
    }

    public final int getUpdateSelectionMode() {
        return updateSelectionMode;
    }

    /**
     * Mode Possible values: UPDATE_SELECTION_NONE : action not instanceof SelectionActionListener
     * UPDATE_SELECTION_ONLINE : action that must be updated continuouly (always visible in GUI)
     * UPDATE_SELECTION_OFFLINE : action that is updated on demand (before action components become
     * visible)
     * 
     * IMPORTANT: This method should not be called by subclasses. This method is used by JackToolbar
     * and this class. The updateSelectionMode is managed automatically in jack packages. The only
     * raisons for using mode UPDATE_SELECTION_ONLINE is when an action is added to a toolbar and if
     * this toolbar is visible or if there is an accelerator key specified. So the mode is switch to
     * online-offline by classes JackToolbar and AbstractApplicationAction depending if the toolbar
     * is visible or not. NOTE: Action implementing SelectionActionListener should not be added to
     * more than one toolbar (NO GOOD REASONS TO DO SO).
     */
    public final void setUpdateSelectionMode(int mode) {
        if (updateSelectionMode != UPDATE_SELECTION_NONE && mode != updateSelectionMode) {
            updateSelectionMode = mode;
            if (mode == UPDATE_SELECTION_ONLINE)
                ApplicationContext.getFocusManager().addSelectionListener(this);
            else
                ApplicationContext.getFocusManager().removeSelectionListener(this);
        }
    }

    public final boolean isVisible() {
        return visible;
    }

    // Use this method to propagate the visible property on all the components
    // created with this action
    // Default behavior is always visible in JMenuBar
    public final synchronized void setVisible(boolean newValue) {
        boolean oldValue = this.visible;
        this.visible = newValue;
        firePropertyChange(VISIBLE, new Boolean(oldValue), new Boolean(newValue));
    }

    // Use this method to hide in specific components
    // Any change after an action component has been created will not be
    // instantly propagated to the components like other properties
    // Action must be visible
    // default = VISIBILITY_ALWAYS_VISIBLE_IN_MENU |
    // VISIBILITY_ALWAYS_VISIBLE_IN_TOOLBAR;
    protected final void setVisibilityMode(int visibilityMode) {
        this.visibilityMode = visibilityMode;
    }

    public final int getVisibilityMode() {
        return visibilityMode;
    }

    // A call to this method is the same as calling
    // '(String)getValue(LONG_DESCRIPTION)' on the action
    public final String getHelpText() {
        return (String) getValue(LONG_DESCRIPTION);
    }

    // Use this method to provide help (actual implementation display this help
    // text in the status bar when the mouse
    // is pass over the item)
    // A call to this method is the same as calling 'putValue(LONG_DESCRIPTION,
    // newValue)' on the action
    public final void setHelpText(String newValue) {
        putValue(LONG_DESCRIPTION, newValue);
    }

    // WARNING: DO NOT USE putValue(ACCELERATOR_KEY, object)
    // Use this method instead
    public final void setAccelerator(KeyStroke newValue) {
        super.putValue(ACCELERATOR_KEY, newValue);
        if (newValue != null)
            setUpdateSelectionMode(UPDATE_SELECTION_ONLINE); // Cannot remove if
        // changed to
        // null, action
        // may be
        // present on a
        // toolbar
    }

    public final void setIcon(Icon newValue) {
        super.putValue(Action.SMALL_ICON, newValue);
    }

    public final KeyStroke getAccelerator() {
        KeyStroke value = (KeyStroke) getValue(ACCELERATOR_KEY);
        if (value == null)
            return null;
        return value;
    }

    public final void setMnemonicInt(Integer newValue) {
        putValue(MNEMONIC_KEY, newValue);
    }

    public final void setMnemonic(String newValue) {
        if (newValue == null)
            setMnemonicInt(null);
        else
            setMnemonic(newValue.charAt(0));
    }

    public final void setMnemonic(char newValue) {
        putValue(MNEMONIC_KEY, new Integer(newValue));
    }

    // return the integer representing the mnemonic character
    public final int getMnemonic() {
        Integer value = (Integer) getValue(MNEMONIC_KEY);
        if (value == null)
            return KeyEvent.CHAR_UNDEFINED;
        return value.intValue();
    }

    public final void performAction() {
        doActionPerformed();
    }

    public final void setName(String name) {
        putValue(Action.NAME, name);
    }

    public final String getName() {
        Object value = getValue(Action.NAME);
        return value == null ? null : value.toString();
    }

    // Can be overridden to customize action name according selObjects
    public String getText(Object[] selObjects) {
        return null;
    }

    // Can be overridden to disable the action according selObjects
    public boolean isEnabled(Object[] selObjects) {
        return true;
    }

    // Overrided for tooltips value and accelerator control for selection
    // listener
    public void putValue(String key, Object newValue) {
        if (key.equals(AbstractApplicationAction.ACCELERATOR_KEY)) {
            setAccelerator((KeyStroke) newValue);
            return;
        }
        if (key.equals(Action.SHORT_DESCRIPTION))
            useNameAsToolTips = false;
        if (useNameAsToolTips && key.equals(Action.NAME))
            super.putValue(Action.SHORT_DESCRIPTION, newValue);
        super.putValue(key, newValue);
    }

    // Override this method in sub-classes to perform the action without the
    // need for an ActionEvent.
    // Will be called by the static method performAction(actionkey)
    protected void doActionPerformed() {
    }

    protected void doActionPerformed(ActionEvent e) {
        doActionPerformed();
    }

    // This one must stay final to ensure that the applicationActionListeners
    // are triggerred.
    // The implementation of doActionPerformed(ActionEvent) will produce the
    // same effect.
    public final void actionPerformed(ActionEvent e) {
        Log.add(this.getClass().getName(), Log.LOG_ACTION);
        long start = System.currentTimeMillis();
        doActionPerformed(e);
        Log.add(this.getClass().getName() + ":  Completion time = "
                + (new Long(System.currentTimeMillis() - start)).toString() + " ms.",
                Log.LOG_STATISTIC); // NOT LOCALIZABLE
        if (applicationActionListeners.size() > 0) {
            ApplicationActionEvent aae = new ApplicationActionEvent(
                    ApplicationActionEvent.ACTION_PERFORMED, this, e);
            fireApplicationActionListeners(aae);
        }
    }

    protected final boolean isApplicationDiagramHaveFocus() {
        return (ApplicationContext.getFocusManager().getFocusObject() instanceof ApplicationDiagram);
    }

    protected final Point getDiagramLocation(ActionEvent e) {
        if (e != null && e.getSource() instanceof JMenuItem) {
            Object parent = ((JMenuItem) e.getSource()).getParent();
            if (parent instanceof JackPopupMenu)
                return ((JackPopupMenu) parent).getDiagramLocation();
        }
        return null;
    }

    protected final GraphicComponent getGraphicComponent(ActionEvent e) {
        if (e != null && e.getSource() instanceof JMenuItem) {
            Object parent = ((JMenuItem) e.getSource()).getParent();
            if (parent instanceof JackPopupMenu)
                return ((JackPopupMenu) parent).getGraphicComponent();
        }
        return null;
    }

    // /////////////////////////////////////////
    // ApplicationActionListeners Management
    //

    public final void addApplicationActionListener(ApplicationActionListener l) {
        if (applicationActionListeners.indexOf(l) == -1)
            applicationActionListeners.addElement(l);
    }

    public final void removeApplicationActionListener(ApplicationActionListener l) {
        if (l != null)
            applicationActionListeners.removeElement(l);
    }

    protected final void fireApplicationActionListeners(ApplicationActionEvent aae) {
        int i = applicationActionListeners.size();
        switch (aae.getId()) {
        case ApplicationActionEvent.ACTION_PERFORMED:
            for (int j = 0; j < i; j++) {
                ApplicationActionListener listener = (ApplicationActionListener) applicationActionListeners
                        .elementAt(j);
                listener.actionPerformed(aae);
            }
            break;
        }
    }

    //
    // End of ApplicationActionListeners Management
    // //////////////////////////////////////////

    // //////////////////////////////////////////////
    // SelectionListener Support
    //
    public void selectionChanged(SelectionChangedEvent e) throws DbException {
        ((SelectionActionListener) this).updateSelectionAction();
    }

    //
    // End of SelectionListener Support
    // //////////////////////////////////////////////

    // //////////////////////////////////////////////
    // Access Control

    public final boolean isEnabled() {
        return enabled;
    }

    // End Access Control
    // //////////////////////////////////////////////

    final boolean getDefaultToolBarVisibility() {
        return defaultToolBarVisibility;
    }

    // Option Configuration for actions added to a toolbar.
    // This property may bypassed (depending on security) the visible property
    // of the action.
    // default value is true
    protected final void setDefaultToolBarVisibility(boolean b) {
        defaultToolBarVisibility = b;
    }

    public final boolean getToolBarVisibilityOption() {
        PropertiesSet properties = PropertiesManager.getPreferencePropertiesSet();
        boolean toolbarVisible = (properties == null) ? false : properties.getPropertyBoolean(
                getToolBarVisibilityKey(this), TOOLBAR_VISIBLE_OPTION,
                defaultToolBarVisibility ? Boolean.TRUE : Boolean.FALSE).booleanValue();
        if(ScreenPerspective.isFullVersion())
            return toolbarVisible;
        else
            return true; 
    }

    static String getToolBarVisibilityKey(Object action) {
        if (action == null)
            return null;
        String key = action.getClass().getName();
        if (action instanceof AbstractApplicationAction) {
            String ext = ((AbstractApplicationAction) action).getPreferenceID();
            if (ext != null && ext.length() > 0) {
                key += "." + ext; // NOT LOCALIZABLE
            }
        }
        return key;
    }

    // Set the preference String for this instance action. This is used to allow
    // multiple instances of the same Action class
    // to be configured independantly. By default, the class name is used to
    // identify the action.
    protected void setPreferenceID(String id) {
        if (id == null)
            return;
        PropertiesSet properties = PropertiesManager.getPreferencePropertiesSet();
        if (properties != null) {
            properties.removePropertyChangeListener(getToolBarVisibilityKey(this),
                    TOOLBAR_VISIBLE_OPTION, prefListener);
            preferenceID = id;
            properties.addPropertyChangeListener(getToolBarVisibilityKey(this),
                    TOOLBAR_VISIBLE_OPTION, prefListener);
        }
    }

    protected String getPreferenceID() {
        return preferenceID;
    }

    /*
     * public JMenuItem createMenuItem(JackPopupMenu menu, Object[] selObjects) { //JMenuItem item =
     * menu.add(this); JMenuItem item = new JackPopupMenu.AbstractApplicationActionMenuItem(this);
     * menu.add(item); return item; }
     */

    public JMenuItem createItem(JackPopupMenu jackPopupMenu) {
        JMenuItem item = jackPopupMenu.add(this); // super.add(a);
        return item;
    }

    public void init(JMenuItem item, Object[] selObjects) {
    }

}
