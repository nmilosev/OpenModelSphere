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

package org.modelsphere.jack.awt;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.modelsphere.jack.actions.*;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.ExceptionHandler;

/**
 * We should always use this class instead of JMenu. This class provides support for actions more
 * sophisticated than the swings actions.
 */

public class JackMenu extends JMenu implements MenuListener {
    private boolean popupSubMenu = false;
    private JComponent[] hiddenComponents = null;

    private class VisiblePropertyChangeListener implements PropertyChangeListener {
        VisiblePropertyChangeListener() {
        }

        public void propertyChange(PropertyChangeEvent e) {
            if (e.getPropertyName() == null
                    || !e.getPropertyName().equals(AbstractApplicationAction.VISIBLE))
                return;
            validateSeparators();
        }
    }

    private PropertyChangeListener visibleListener = new VisiblePropertyChangeListener();

    /* Not used constructor
    public JackMenu() {
        super();
        addMenuListener(this);
    }
    */
   
    public JackMenu(String s) {
        this(s, false);
        addMenuListener(this);
        menuKey = s;
    }

    public JackMenu(String s, boolean b) {
        super(s);
        popupSubMenu = b;
        addMenuListener(this);
        menuKey = s;

        // Add a listener for removing all references between the action and the
        // component after the popup will be removed
        // this is necessary because AbstractApplicationAction actions are
        // singleton and a reference to them is kept
        // in the action store ... blocking the gc from removing the action and
        // the component.
        // This listener will call a setAction(null).
        if (popupSubMenu)
            new MenuItemActionUninstaller(this); // this will remove all
        // subitems-action links
    }

    public JMenuItem add(Action a) {
        if (a == null)
            return null;
        if (a instanceof AbstractApplicationAction) {
            a.addPropertyChangeListener(visibleListener);
        }
        JMenuItem mi = super.add(a);
        validateSeparators();
        return mi;
    }

    public final JMenuItem insert(Action a, int index) {
        if (a == null)
            return null;
        if (a instanceof AbstractApplicationAction) {
            a.addPropertyChangeListener(visibleListener);
        }
        JMenuItem mi = super.insert(a, index);
        validateSeparators();
        return mi;
    }

    public final JMenuItem insertAfter(Action a, String actionKey) {
        if (a == null)
            return null;
        int idx = -1;
        AbstractAction referenceAction = ApplicationContext.getActionStore().getAction(actionKey);
        if (referenceAction == null) {
            return add(a);
        }
        Component[] components = getMenuComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof AbstractButton) {
                Action action = ((AbstractButton) components[i]).getAction();
                if (action == referenceAction) {
                    idx = i + 1;
                    break;
                }
            }
        }

        if (idx > -1)
            return insert(a, idx);
        else
            return add(a);
    }

    public final JMenuItem insertBefore(Action a, String actionKey) {
        if (a == null)
            return null;
        int idx = -1;
        AbstractAction referenceAction = ApplicationContext.getActionStore().getAction(actionKey);
        if (referenceAction == null) {
            return add(a);
        }
        Component[] components = getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof AbstractButton) {
                Action action = ((AbstractButton) components[i]).getAction();
                if (action == referenceAction) {
                    idx = i;
                    break;
                }
            }
        }

        if (idx > -1)
            return insert(a, idx);
        else
            return add(a);
    }

    final boolean isPopupSubMenu() {
        return popupSubMenu;
    }

    public final void remove(JMenuItem mi) {
        if (mi.getAction() != null) {
            mi.getAction().removePropertyChangeListener(visibleListener);
            mi.setAction(null);
        }
        super.remove(mi);
        validateSeparators();
    }

    public final void remove(int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero."); // NOT
            // LOCALIZABLE
        }
        if (pos > getItemCount()) {
            throw new IllegalArgumentException("index greater than the number of items."); // NOT
            // LOCALIZABLE
        }
        Component c = getItem(pos);
        if (c instanceof JMenuItem) {
            JMenuItem mi = (JMenuItem) c;
            if (mi.getAction() != null) {
                mi.getAction().removePropertyChangeListener(visibleListener);
                mi.setAction(null);
            }
        }
        super.remove(pos);
        validateSeparators();
    }

    public final void remove(Component c) {
        if (c instanceof JMenuItem) {
            JMenuItem mi = (JMenuItem) c;
            if (mi.getAction() != null) {
                mi.getAction().removePropertyChangeListener(visibleListener);
                mi.setAction(null);
            }
        }
        super.remove(c);
        validateSeparators();
    }

    private void validateSeparators() {
        Component[] components = getMenuComponents();
        if (components == null)
            return;
        boolean separatorAllowed = false;
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JMenuItem) {
                Action action = ((JMenuItem) components[i]).getAction();
                if (action != null && (action instanceof AbstractApplicationAction)) {
                    // TODO - should also check if action always visible in Menu
                    if (((AbstractApplicationAction) action).isVisible()) {
                        separatorAllowed = true;
                        continue;
                    }
                }
                if (components[i].isVisible()) {
                    separatorAllowed = true;
                    continue;
                } else {
                    // separatorAllowed = false;
                    continue;
                }
            }
            if (components[i] instanceof JSeparator) {
                if (separatorAllowed) {
                    components[i].setVisible(true);
                    separatorAllowed = false;
                    continue;
                }
                components[i].setVisible(false);
            }
        }
    }

    protected PropertyChangeListener createActionPropertyChangeListener(Action b) {
        // should be overrided
        org.modelsphere.jack.debug.Debug.assert2(false,
                "JackMenu:  createActionPropertyChangeListener -- should not be in this method");
        return null;
    }

    protected final JMenuItem createActionComponent(Action a) {
        JMenuItem mi = null;
        if (a instanceof AbstractTriStatesAction) {
            mi = new TriStateCheckBoxMenuItem((AbstractTriStatesAction) a);
        } else if (a instanceof AbstractDomainAction) {
            mi = new DomainMenu((AbstractDomainAction) a);
        } else if (a instanceof AbstractApplicationAction) {
            mi = new JMenuItem(a) {
                protected void configurePropertiesFromAction(Action a) {
                    AbstractApplicationAction action = (AbstractApplicationAction) a;
                    setIcon((a != null ? (Icon) a.getValue(Action.SMALL_ICON) : null));
                    setText((action != null ? (String) action.getValue(Action.NAME) : null));
                    setEnabled((action != null ? action.isEnabled() : true));
                    if (a != null
                            && (action.getVisibilityMode() & AbstractApplicationAction.VISIBILITY_ALWAYS_VISIBLE_IN_MENU) == 0)
                        setVisible((action != null ? action.isVisible() : true)); // Disabled
                    // in
                    // menu
                    // (item
                    // must
                    // stay
                    // visible)
                    else
                        setVisible(true);
                    setAccelerator((action != null ? action.getAccelerator() : null));
                    if (action != null) {
                        Integer i = (Integer) action.getValue(Action.MNEMONIC_KEY);
                        if (i != null)
                            setMnemonic(i.intValue());
                    }
                    ActionHelpPropertySupport.registerHelpSupport(this, action);
                }

                protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
                    return new PropertyChangeListener() {
                        public void propertyChange(PropertyChangeEvent e) {
                            String propertyName = e.getPropertyName();
                            if (propertyName.equals(Action.NAME)) {
                                String text = (String) e.getNewValue();
                                setText(text);
                                repaint();
                            } else if (propertyName.equals("enabled")) { // NOT
                                // LOCALIZABLE
                                // -
                                // property
                                Boolean enabledState = (Boolean) e.getNewValue();
                                setEnabled(enabledState.booleanValue());
                                repaint();
                            } else if (propertyName.equals(Action.SMALL_ICON)) {
                                Icon icon = (Icon) e.getNewValue();
                                setIcon(icon);
                                invalidate();
                                repaint();
                            } else if (propertyName.equals(Action.MNEMONIC_KEY)) {
                                Integer mn = (Integer) e.getNewValue();
                                setMnemonic(mn.intValue());
                                invalidate();
                                repaint();
                            } else if (propertyName.equals(AbstractApplicationAction.VISIBLE)) { // Disabled
                                // in
                                // menu
                                // (item
                                // must
                                // stay
                                // visible)
                                if (getAction() instanceof AbstractApplicationAction) {
                                    boolean newValue = true;
                                    if ((((AbstractApplicationAction) getAction())
                                            .getVisibilityMode() & AbstractApplicationAction.VISIBILITY_ALWAYS_VISIBLE_IN_MENU) == 0)
                                        newValue = ((Boolean) e.getNewValue()).booleanValue();
                                    setVisible(newValue);
                                    invalidate();
                                    repaint();
                                }
                            } else if (propertyName
                                    .equals(AbstractApplicationAction.ACCELERATOR_KEY)) {
                                KeyStroke newValue = (KeyStroke) e.getNewValue();
                                setAccelerator(newValue);
                                invalidate();
                                repaint();
                            }
                        }
                    };
                }
            };
        } else {
            mi = new JMenuItem(a) {
                protected void configurePropertiesFromAction(Action a) {
                    setText((a != null ? (String) a.getValue(Action.NAME) : null));
                    setIcon((a != null ? (Icon) a.getValue(Action.SMALL_ICON) : null));
                    setEnabled((a != null ? a.isEnabled() : true));
                    if (a != null) {
                        Integer i = (Integer) a.getValue(Action.MNEMONIC_KEY);
                        if (i != null)
                            setMnemonic(i.intValue());
                    }
                }

                protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
                    return new PropertyChangeListener() {
                        public void propertyChange(PropertyChangeEvent e) {
                            String propertyName = e.getPropertyName();
                            if (propertyName.equals(Action.NAME)) {
                                String text = (String) e.getNewValue();
                                setText(text);
                                repaint();
                            } else if (propertyName.equals("enabled")) { // NOT
                                // LOCALIZABLE
                                // -
                                // property
                                Boolean enabledState = (Boolean) e.getNewValue();
                                setEnabled(enabledState.booleanValue());
                                repaint();
                            } else if (propertyName.equals(Action.SMALL_ICON)) {
                                Icon icon = (Icon) e.getNewValue();
                                setIcon(icon);
                                invalidate();
                                repaint();
                            } else if (propertyName.equals(Action.MNEMONIC_KEY)) {
                                Integer mn = (Integer) e.getNewValue();
                                setMnemonic(mn.intValue());
                                invalidate();
                                repaint();
                            }
                        }
                    };
                }
                
                @Override 
                public String toString() {
                    return menuKey;
                }
            };
        }

        mi.setHorizontalTextPosition(JButton.RIGHT);
        mi.setVerticalTextPosition(JButton.CENTER);

        if (a != null && mi != null) {
            mi.setName(a.getClass().getName());
        }

        return mi;
    }

    // ////////////////////////////////////////////
    // MenuListener SUPPORT
    //

    private boolean preventEvent = false;

    // Call <updateSelectionAction> of each offline action of this menu (and its
    // submenus),
    // before displaying the menu.
    // Start a transaction for each Db encompassed by the selection; if the
    // selection is empty,
    // start a transaction for the current project.
    // Overridden
    public void menuSelected(MenuEvent e) {
        if (preventEvent)
            return;
        if (!(getParent() instanceof JMenuBar))
            return;
        try {
            Object[] selObjects = ApplicationContext.getFocusManager().getSelectedObjects();
            if (selObjects.length == 0) {
                DbProject project = ApplicationContext.getFocusManager().getCurrentProject();
                if (project != null)
                    selObjects = new Object[] { project };
            }
            DbMultiTrans.beginTrans(Db.READ_TRANS, selObjects, null);
            menuSelectedAux();
            try {
                DbMultiTrans.commitTrans(selObjects);
            } catch (RuntimeException w) {
            }
        } catch (Exception ex) {
            preventEvent = true;
            final boolean oldEnabled = isEnabled();
            setEnabled(false);
            Component parentWindow = SwingUtilities.getAncestorOfClass(JFrame.class, JackMenu.this);
            if (parentWindow == null) {
                parentWindow = SwingUtilities.getAncestorOfClass(JDialog.class, JackMenu.this);
            }
            ExceptionHandler.processUncatchedException(parentWindow, ex);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    preventEvent = false;
                    setEnabled(oldEnabled);
                    setSelected(false);
                }
            });
        }

        // avoid last item as separator
        int itemcount = getMenuComponentCount();
        JSeparator lastSeparator = null;
        for (int i = itemcount - 1; i > 0 && lastSeparator == null; i--) {
            Component comp = getMenuComponent(i);
            if (comp instanceof JSeparator) {
                if (!comp.isVisible())
                    continue;
                lastSeparator = (JSeparator) comp;
            }
            if (comp.isVisible()) {
                break;
            }
        }
        if (lastSeparator != null) {
            hiddenComponents = new JComponent[] { lastSeparator };
            lastSeparator.setVisible(false);
        }
    }

    // Overridden
    protected void menuSelectedAux() throws DbException {
        int nb = getMenuComponentCount();
        for (int i = 0; i < nb; i++) {
            Component item = getMenuComponent(i);
            if (item instanceof JackMenu && !(item instanceof DomainMenu)) {
                ((JackMenu) item).menuSelectedAux();
            } else if (item instanceof JMenuItem) {
                Action action = ((JMenuItem) item).getAction();
                if (action instanceof AbstractApplicationAction
                        && ((AbstractApplicationAction) action).getUpdateSelectionMode() == AbstractApplicationAction.UPDATE_SELECTION_OFFLINE) {
                    ((SelectionActionListener) action).updateSelectionAction();
                    if (Debug.isDebug()) {
                        DbProject project = ApplicationContext.getFocusManager()
                                .getCurrentProject();
                        if (project != null
                                && (project.getDb().getTransMode() == Db.TRANS_NONE || project
                                        .getDb().getTransMode() == Db.TRANS_ABORT))
                            Debug
                                    .trace("JackMenu:  Action "
                                            + action.getClass().getName()
                                            + " performed an illegal try catch DbException or invalid abordTrans on update selection."); // NOT
                        // LOCALIZABLE
                        // -
                        // Debug
                    }
                }
            }
        }
    }

    public final void menuDeselected(MenuEvent e) {
        // Restore the visibility for separator hiden in menuSelected(). Some
        // actions may be hiden on some specific selection only.
        // We can't assume that those components will always be hidden.
        if (hiddenComponents != null) {
            for (int i = 0; i < hiddenComponents.length; i++)
                hiddenComponents[i].setVisible(true);
            hiddenComponents = null;
        }
    }

    public final void menuCanceled(MenuEvent e) {
    }

    //
    // End of MenuListener SUPPORT
    // ///////////////////////////////////////////

    // Overriden to avoid duplicate separator
    public void addSeparator() {
        if (getMenuComponentCount() == 0) {
            return;
        }
        Component last = getMenuComponent(getMenuComponentCount() - 1);
        if (last instanceof JPopupMenu.Separator)
            return;
        super.addSeparator();
    }

    @Override 
    public String toString() {
        return menuKey;
    }
    
    //A key that uniquely identifies a menu
    private String menuKey;
	public String getMenuKey() {
		return menuKey;
	}
}
