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
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.plaf.ToolBarUI;
import javax.swing.plaf.basic.BasicToolBarUI;

import org.modelsphere.jack.actions.*;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.actions.ShowAbstractAction;
import org.modelsphere.jack.util.ExceptionHandler;

public class JackToolBar extends JToolBar {

    private class VisibleChangeListener extends ComponentAdapter {
        VisibleChangeListener() {
        }

        /*
         * public void propertyChange(PropertyChangeEvent e){ if (e.getPropertyName() == null ||
         * !e.getPropertyName().equals(AbstractApplicationAction.TOOLBAR_VISIBLE_OPTION)) return;
         * validateSeparators(); }
         */
        public void componentShown(ComponentEvent e) {
            if (e.getComponent() instanceof JSeparator)
                return;
            if (e.getComponent() instanceof JackSeparator)
                return;
            validateSeparators();
        }

        public void componentHidden(ComponentEvent e) {
            validateSeparators();
        }
    }

    private VisibleChangeListener visibleListener = new VisibleChangeListener();

    public JackToolBar() {
        super();
        init();
    }

    public JackToolBar(int orientation) {
        super(orientation);
        init();
    }

    private void init() {
        setFloatable(true);
    }

    protected final JButton createActionComponent(Action a) {
        JButton b = null;
        if (a instanceof AbstractTriStatesAction) {
            b = new TriStatesButton((AbstractTriStatesAction) a);
        } else if (a instanceof ShowAbstractAction) {
            b = new ShowHideButton((ShowAbstractAction) a);
        } else if (a instanceof AbstractApplicationAction) {
            b = new AbstractApplicationActionButton(a);
        } else {
            b = new DefaultButton(a);
        }

        if (a != null && b != null) {
            b.setName(a.getClass().getName());
        }

        // work on this one to make it configurable for the user
        Icon icon = (Icon) a.getValue(Action.SMALL_ICON);
        if (icon != null) {
            b.putClientProperty("hideActionText", Boolean.TRUE); // NOT LOCALIZABLE - property
        }
        b.setFocusable(false);
        b.setFocusPainted(false);
        return b;
    }

    public JButton add(Action a) {
        if (a == null)
            return null;
        
        if (a instanceof AbstractApplicationAction) {
            AbstractApplicationAction action = (AbstractApplicationAction)a; 
            
            if (isVisible() && (a instanceof SelectionActionListener)) {
                action.setUpdateSelectionMode(AbstractApplicationAction.UPDATE_SELECTION_ONLINE);
            }
            
            if (! action.isVisible()) {
                return null;
            } //end if
        } //end if
        
        if (a instanceof UndoRedoAbstractAction) {
            PopupCommandHistoryPanel c = new PopupCommandHistoryPanel((UndoRedoAbstractAction) a);
            c.setName(a.getClass().getName());
            add(c);
            validateSeparators();
            c.addComponentListener(visibleListener);
            return null;
        }
        if (a instanceof AbstractColorDomainAction) {
            PopupColorPanel c = new PopupColorPanel((AbstractColorDomainAction) a);
            c.setName(a.getClass().getName());
            add(c);
            validateSeparators();
            c.addComponentListener(visibleListener);
            return null;
        }
        if (a instanceof AbstractMenuAction) {
            PopupMenuButton c = new PopupMenuButton((AbstractMenuAction) a);
            c.setName(a.getClass().getName());
            add(c);
            validateSeparators();
            c.addComponentListener(visibleListener);
            return null;
        }
        if (a instanceof AbstractDomainAction) {
            DomainComboBox c = new DomainComboBox((AbstractDomainAction) a);
            c.setName(a.getClass().getName());
            add(c);
            validateSeparators();
            c.addComponentListener(visibleListener);
            return null;
        }

        JButton c = super.add(a);
        validateSeparators();
        c.addComponentListener(visibleListener);
        c.setFocusable(false);
        c.setFocusPainted(false);
        return c;
    }

    public void remove(Component comp) {
        if (comp == null)
            return;
        if (comp instanceof AbstractButton)
            ((AbstractButton) comp).setAction(null);
        else if (comp instanceof JackComboBox)
            ((JackComboBox) comp).setAction(null);
        else if (comp instanceof PopupColorPanel)
            ((PopupColorPanel) comp).setAction(null);
        else if (comp instanceof PopupCommandHistoryPanel)
            ((PopupCommandHistoryPanel) comp).setAction(null);

        super.remove(comp);
        comp.removeComponentListener(visibleListener);
        validateSeparators();
    }

    protected final JButton addFormated(Action action) {
        JButton button = add(action);
        if (button != null) {
            String tooltips = (String) action.getValue(Action.SHORT_DESCRIPTION);
            button.setToolTipText(tooltips);
        }
        return button;
    }

    protected final JButton addFormated(Action action, String text, Icon icon) {
        JButton button = add(action);
        if (button != null) {
            button.setActionCommand(text);
            button.setIcon(icon);
            button.setToolTipText(text);
        }
        return button;
    }

    protected final void addLineSeparator() {
        JackSeparator s = new JackSeparator();
        add(s);
    }

    // Overrided to manage the update selection mode for AbstractApplicationAction instances
    // If 'this' not visible, the components actions don't need to be updated when selection change
    // WARNING:  THIS MAY NOT WORK PROPERLY IF THE ACTION IS ADDED TO MORE THAN ONE TOOLBAR (SHOULD NOT HAPPEN
    // BECAUSE THERE IS ONLY ONE TOOLBAR DEDICATED FOR EACH SPECIFIC GROUP OF ACTIONS (EDIT, FILE, ...))
    // Overriding method must call super.setVisible(boolean);
    public void setVisible(boolean b) {
        ToolBarUI ui = (ToolBarUI) getUI();
        if (ui instanceof BasicToolBarUI) {
            // There is a bug as of 1.4.1 :  If floating, the window containing the toolbar won't be hidden
            if (((BasicToolBarUI) ui).isFloating()) {
                Window win = (Window) SwingUtilities.getAncestorOfClass(Window.class, this);
                if (win != null)
                    win.setVisible(b);
            }
        }
        Component[] components = this.getComponents();
        try {
            Object[] selObjects = ApplicationContext.getFocusManager().getSelectedObjects();
            DbMultiTrans.beginTrans(Db.READ_TRANS, selObjects, "");
            for (int i = 0; i < components.length; i++) {
                Action a = null;
                if (components[i] instanceof AbstractButton)
                    a = ((AbstractButton) components[i]).getAction();
                else if (components[i] instanceof JComboBox)
                    a = ((JComboBox) components[i]).getAction();
                else if (components[i] instanceof PopupColorPanel)
                    a = ((PopupColorPanel) components[i]).getAction();
                else if (components[i] instanceof PopupCommandHistoryPanel)
                    a = ((PopupCommandHistoryPanel) components[i]).getAction();
                if ((a instanceof SelectionActionListener)
                        && (a instanceof AbstractApplicationAction)) {
                    boolean hasaccelerator = ((AbstractApplicationAction) a).getAccelerator() != null;
                    ((AbstractApplicationAction) a)
                            .setUpdateSelectionMode((b || hasaccelerator) ? AbstractApplicationAction.UPDATE_SELECTION_ONLINE
                                    : AbstractApplicationAction.UPDATE_SELECTION_OFFLINE);
                    if (b) // if visible, make sure the action is updated
                        ((SelectionActionListener) a).updateSelectionAction();
                }
            }
            DbMultiTrans.commitTrans(selObjects);
            setEnabled(true);
        } catch (DbException e) {
            setEnabled(false);
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        }
        super.setVisible(b);
    }

    private void validateSeparators() {
        Component[] components = getComponents();
        if (components == null)
            return;
        boolean separatorAllowed = false;
        Component lastVisibleSeparator = null;
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JSeparator || components[i] instanceof JackSeparator) {
                if (separatorAllowed) {
                    components[i].setVisible(true);
                    separatorAllowed = false;
                    lastVisibleSeparator = components[i];
                    continue;
                }
                components[i].setVisible(false);
            }
            if (components[i].isVisible()) {
                separatorAllowed = true;
                continue;
            }
        }
        // hide last separator if this is separator is last visible component
        if (!separatorAllowed && lastVisibleSeparator != null)
            lastVisibleSeparator.setVisible(false);
    }

    @Override
    public Dimension getPreferredSize() {
        Component[] components = getComponents();
        if (components != null) {
            boolean visible = false;
            for (int i = 0; i < components.length; i++) {
                if (components[i].isVisible()) {
                    visible = true;
                    break;
                }
            }
            if (!visible) {
                return new Dimension(0, 0);
            }
        }
        return super.getPreferredSize();
    }

}

class AbstractApplicationActionButton extends JButton {

    AbstractApplicationActionButton(Action a) {
        super(a);
    }

    protected void configurePropertiesFromAction(Action a) {
        //setText((a!=null?(String)a.getValue(Action.NAME):null));
        setIcon((a != null ? (Icon) a.getValue(Action.SMALL_ICON) : null));
        setEnabled((a != null ? a.isEnabled() : true));
        setToolTipText((a != null ? (String) a.getValue(Action.SHORT_DESCRIPTION) : null));
        updateVisible();
        ActionHelpPropertySupport.registerHelpSupport(this, (AbstractApplicationAction) a);
    }

    protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
        return new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                String propertyName = e.getPropertyName();
                /*
                 * if (propertyName.equals(Action.NAME)) { String text = (String) e.getNewValue();
                 * setText(text); repaint(); } else
                 */if (propertyName.equals("enabled")) { // NOT LOCALIZABLE
                    Boolean enabledState = (Boolean) e.getNewValue();
                    setEnabled(enabledState.booleanValue());
                    repaint();
                } else if (propertyName.equals(Action.SMALL_ICON)) {
                    Icon icon = (Icon) e.getNewValue();
                    setIcon(icon);
                    invalidate();
                    repaint();
                } else if (propertyName.equals(Action.SHORT_DESCRIPTION)) {
                    String tooltip = (String) e.getNewValue();
                    setToolTipText(tooltip);
                    invalidate();
                    repaint();
                } else if (propertyName.equals(AbstractApplicationAction.VISIBLE)
                        || propertyName.equals(AbstractApplicationAction.TOOLBAR_VISIBLE_OPTION)) {
                    updateVisible();
                }
            }
        };
    }

    private void updateVisible() {
        if (getAction() instanceof AbstractApplicationAction) {
            boolean newValue = true;
            AbstractApplicationAction action = (AbstractApplicationAction) getAction();
            if ((action.getVisibilityMode() & AbstractApplicationAction.VISIBILITY_ALWAYS_VISIBLE_IN_TOOLBAR) == 0)
                newValue = action.isVisible() && newValue;
            else if (!action.getToolBarVisibilityOption())
                newValue = false;
            setVisible(newValue);
            invalidate();
            repaint();
        }
    }

}

class DefaultButton extends JButton {
    DefaultButton(Action a) {
        super(a);
    }

    protected void configurePropertiesFromAction(Action a) {
        //setText((a!=null?(String)a.getValue(Action.NAME):null));
        setIcon((a != null ? (Icon) a.getValue(Action.SMALL_ICON) : null));
        setEnabled((a != null ? a.isEnabled() : true));
        setToolTipText((a != null ? (String) a.getValue(Action.SHORT_DESCRIPTION) : null));
    }

    protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
        return new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                String propertyName = e.getPropertyName();
                /*
                 * if (propertyName.equals(Action.NAME)) { String text = (String) e.getNewValue();
                 * setText(text); repaint(); } else
                 */if (propertyName.equals("enabled")) { // NOT LOCALIZABLE - property
                    Boolean enabledState = (Boolean) e.getNewValue();
                    setEnabled(enabledState.booleanValue());
                    repaint();
                } else if (propertyName.equals(Action.SMALL_ICON)) {
                    Icon icon = (Icon) e.getNewValue();
                    setIcon(icon);
                    invalidate();
                    repaint();
                } else if (propertyName.equals(Action.SHORT_DESCRIPTION)) {
                    String tooltip = (String) e.getNewValue();
                    setToolTipText(tooltip);
                    invalidate();
                    repaint();
                }
            }
        };
    }

}
