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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.jack.actions.AbstractMenuAction;
import org.modelsphere.jack.srtool.AddElement;
import org.modelsphere.jack.srtool.list.ListDescriptor;

/**
 * This class define a component for supporting the AbstractDomainAction in a JackMenu. See
 * AbstractDomainAction for more informations.
 */

final class DomainMenu extends JackMenu implements ActionListener {
    private static final int COLOR_ICON_WIDTH = 40;
    private static final int COLOR_ICON_HEIGHT;
    private static final String DOMAIN = "domain"; // NOT LOCALIZABLE - Property Key

    static {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        if (dim != null && dim.getHeight() > 700)
            COLOR_ICON_HEIGHT = 8;
        else
            COLOR_ICON_HEIGHT = 7;
    }

    // If used in a hierarchie of menu (for AbstractMenuAction), use a prefixed
    // This mode does not support uiSelectionVisible
    private static final String ACTION_COMMAND_PREFIX = "[ACTCMD]"; // NOT
    // LOCALIZABLE

    // may contains null - represented by separator
    private ArrayList items = new ArrayList(0);
    private Object oldSelection = null;
    private boolean uiSelectionVisible = true;
    private ButtonGroup group = new ButtonGroup();

    // Backup new values to update the item list later (on menu selected)
    private Object[] lastValues = new Object[] {};
    private int lastSelected = -1;

    // The accelerator will be applied to the first item
    private KeyStroke accelerator = null;

    DomainMenu(AbstractDomainAction a) {
        super(DOMAIN);
        setAction(a);
        addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                updateItems_Impl();
                updateSelectedItem_Impl();
            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }
        });
    }

    protected void configurePropertiesFromAction(Action a) {
        AbstractDomainAction action = (AbstractDomainAction) a;
        setText(a != null ? (String) a.getValue(Action.NAME) : null);
        setIcon(a != null ? (Icon) a.getValue(Action.SMALL_ICON) : null);
        // System.out.println(a.getValue(Action.NAME) + " " +
        // a.getValue(Action.SMALL_ICON));
        setEnabled(a != null ? a.isEnabled() : true);
        // The parent should be null at this time ... the visibility for the
        // popup menu components is managed within the popupmenu manager.
        // So I used the default visibility behavior as if part of a menu. If in
        // popup, the popup manager will take care of it.
        if (a != null
                && (action.getVisibilityMode() & AbstractApplicationAction.VISIBILITY_ALWAYS_VISIBLE_IN_MENU) == 0)
            setVisible(action.isVisible());
        if (a != null) {
            Integer i = (Integer) a.getValue(AbstractApplicationAction.MNEMONIC_KEY);
            if (i != null)
                setMnemonic(i.intValue());
        }
        uiSelectionVisible = (action != null) ? action.isUISelectionVisible() : true;
        updateItems(a != null ? action.getDomainValues() : null);
        updateSelectedItem(a != null ? action.getSelectedIndex() : -1);
        accelerator = a != null ? action.getAccelerator() : null;
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
                } else if (propertyName.equals("enabled")) { // NOT LOCALIZABLE,
                    // property key
                    Boolean enabledState = (Boolean) e.getNewValue();
                    setEnabled(enabledState.booleanValue());
                    repaint();
                } else if (propertyName.equals(Action.SMALL_ICON)) {
                    Icon icon = (Icon) e.getNewValue();
                    setIcon(icon);
                    invalidate();
                    repaint();
                } else if (propertyName.equals(AbstractApplicationAction.MNEMONIC_KEY)) {
                    Integer mn = (Integer) e.getNewValue();
                    setMnemonic((mn != null) ? mn.intValue() : KeyEvent.VK_UNDEFINED);
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
                        // Since we use JackPopupMenu for our popups, I use that
                        // information to know if the context is within a real
                        // popup or a menu
                        // In swings, all items added to a menu are instead
                        // added to the menu's popup component (a JPopupMenu).
                        // Not used yet ... if problems, reuse hierarchyListener
                        // for offline visibility update (see
                        // TreeStateCheckBoxMenuItem)
                        if ((getParent() instanceof JackPopupMenu)
                                && (((AbstractApplicationAction) getAction()).getVisibilityMode() & AbstractApplicationAction.VISIBILITY_ALWAYS_VISIBLE_IN_POPUPMENU) == 0) {
                            newValue = ((Boolean) e.getNewValue()).booleanValue();
                        } else if ((getParent() instanceof JPopupMenu)
                                && (((AbstractApplicationAction) getAction()).getVisibilityMode() & AbstractApplicationAction.VISIBILITY_ALWAYS_VISIBLE_IN_MENU) == 0) {
                            newValue = ((Boolean) e.getNewValue()).booleanValue();
                        }
                        setVisible(newValue);
                        invalidate();
                        repaint();
                    }
                } else if (propertyName.equals(AbstractDomainAction.VALUES)) {
                    updateItems((Object[]) e.getNewValue());
                } else if (propertyName.equals(AbstractDomainAction.SELECTED_VALUE)) {
                    int newsel = e.getNewValue() == null ? -1 : ((Integer) e.getNewValue())
                            .intValue();
                    updateSelectedItem(newsel);
                }
            }
        };
    }

    private void updateItems(Object[] values) {
        lastValues = values;
    }

    private void updateItems_Impl() {
        removeAll();
        group = new ButtonGroup();

        if (lastValues != null) {
            JMenuItem item = null;
            for (int i = 0; i < lastValues.length; i++) {
                Object value = lastValues[i];
                item = null;
                if (value == null)
                    addJMenuItem(null);
                else {
                    if (value instanceof Icon) {
                        item = uiSelectionVisible ? new JRadioButtonMenuItem((Icon) value, false)
                                : new JMenuItem((Icon) value);
                    } else if (value instanceof Color) {
                        item = uiSelectionVisible ? new JRadioButtonMenuItem(new ColorIcon(
                                (Color) value, COLOR_ICON_WIDTH, COLOR_ICON_HEIGHT), false)
                                : new JMenuItem(new ColorIcon((Color) value, COLOR_ICON_WIDTH,
                                        COLOR_ICON_HEIGHT));
                        StringBuilder text = new StringBuilder();
                        text.append("RGB [").append(((Color) value).getRed()).append(
                                ", " + ((Color) value).getGreen()).append(", ").append(
                                ((Color) value).getBlue()).append("]");
                        item.setText(text.toString());
                    } else if (value instanceof ListDescriptor) {
                        ListDescriptor descriptor = (ListDescriptor) value;
                        Icon icon = descriptor.getIcon();
                        // if (icon == null)
                        // icon = AwtUtil.NULL_ICON;
                        item = uiSelectionVisible ? new JRadioButtonMenuItem(icon, false)
                                : new JMenuItem(value.toString(), icon);
                    } else if ((value instanceof AddElement)
                            && (getAction() instanceof AbstractMenuAction)) {
                        // For AbstractMenuAction
                        // The action event will be triggered using action
                        // command to store index and sub index of the activated
                        // menu item
                        // Does not support flag uiSelectionVisible
                        AddElement element = (AddElement) value;

                        Icon icon = element.getIcon();

                        Object[] choiceValues = element.getChoiceValues();
                        if (choiceValues != null) {
                            item = new JMenu(value.toString());
                            item.setIcon(icon);
                            for (int j = 0; j < choiceValues.length; j++) {
                                if (choiceValues[j] == null) {
                                    ((JMenu) item).addSeparator();
                                    continue;
                                }
                                JMenuItem subItem = new JMenuItem(choiceValues[j].toString());
                                ((JMenu) item).add(subItem);
                                subItem.setActionCommand(ACTION_COMMAND_PREFIX
                                        + new Integer(i).toString()
                                        + AbstractMenuAction.ACTION_COMMAND_SUB_VALUE_SEPARATOR
                                        + new Integer(j).toString());
                                subItem.addActionListener(this);
                            }
                        } else {
                            item = new JMenuItem(value.toString(), icon);
                            item
                                    .setActionCommand(ACTION_COMMAND_PREFIX
                                            + new Integer(i).toString());
                        }
                        item.setVisible(element.isVisible());
                        item.setEnabled(element.isEnabled());
                    } else
                        item = uiSelectionVisible ? new JRadioButtonMenuItem(value.toString(),
                                false) : new JMenuItem(value.toString());
                    addJMenuItem(item);
                    group.add(item);
                    if (i == 0 && accelerator != null)
                        item.setAccelerator(accelerator);
                }
            }
        }
    }

    public void removeAll() {
        oldSelection = null;
        for (int i = 0; i < items.size(); i++) {
            Object obj = items.get(i);
            if (obj != null)
                ((JMenuItem) obj).removeActionListener(this);
        }
        items.clear();
        super.removeAll();
    }

    private void addJMenuItem(JMenuItem item) {
        items.add(item);
        if (item != null) {
            add(item);
            item.addActionListener(this);
        } else
            addSeparator();
    }

    private void updateSelectedItem(int newSel) {
        lastSelected = newSel;
    }

    private void updateSelectedItem_Impl() {
        if (lastSelected != -1) { // set new selection
            ((JMenuItem) items.get(lastSelected)).setSelected(true);
        }
        oldSelection = (lastSelected == -1) ? null : items.get(lastSelected);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        if (heightFix > -1) {
            dim.height = heightFix;
        }
        return dim;
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand != null && (getAction() instanceof AbstractMenuAction)
                && actionCommand.startsWith(ACTION_COMMAND_PREFIX)) {
            fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCommand
                    .substring(ACTION_COMMAND_PREFIX.length(), actionCommand.length()), 0));
        } else {
            Object newSelection = e.getSource();
            oldSelection = newSelection;
            int selidx = items.indexOf(newSelection);
            ((AbstractDomainAction) getAction()).setSelectedPrivate_(selidx, this); // This
            // will
            // trigger
            // the
            // action
            // performed
            if (newSelection == oldSelection)
                ((JMenuItem) newSelection).setSelected(true); // restore the
            // selected
            // state for the
            // menu item
        }
    }

    private int heightFix = -1;

    @Override
    public void setIcon(Icon defaultIcon) {
        // backup the height - Vista LF (possibly XP) calculate height smaller when providing an icon
        heightFix = super.getPreferredSize().height;
        super.setIcon(defaultIcon);
    }

}
