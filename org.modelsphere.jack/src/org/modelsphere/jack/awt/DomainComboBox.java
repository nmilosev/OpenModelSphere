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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.jack.srtool.ApplicationContext;

/**
 * This class define a component for supporting the AbstractDomainAction in a JackToolbar. See
 * AbstractDomainAction for more informations.
 */

class DomainComboBox extends JackComboBox {
    private static final int MAXIMUM_WIDTH = 50;
    private boolean uiSelectionVisible = true;

    // This field is used to control events when the action properties are
    // changed.
    private boolean preventEvent = false;

    DomainComboBox(AbstractDomainAction a) {
        super();
        setModel(new DomainComboBoxModel());
        setAction(a);
        setEditable(false);
        setRequestFocusEnabled(false);
        setMaximumRowCount(15);
        removeActionListener(a);
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireAction();
            }
        });

    }

    public void updateUI() {

        try {
            super.updateUI();
        } catch (Exception ex) {

            ApplicationContext.getDefaultMainFrame().getToolBarPanel().removeAll();
            ApplicationContext.getDefaultMainFrame().installToolBars();
        }

    }

    // Overrided to avoid a 'setAction(null)'. The action must not be an
    // actionListener for this component.
    public final void removeActionListener(ActionListener l) {
        if (l != null)
            listenerList.remove(ActionListener.class, l);
    }

    private void fireAction() {
        if (!preventEvent) {
            AbstractDomainAction action = (AbstractDomainAction) getAction();
            action.setSelectedPrivate_(getSelectedIndex(), this);
        }
    }

    protected final PropertyChangeListener createActionPropertyChangeListener(Action a) {
        return new PropertyChangeListener() {
            public final void propertyChange(PropertyChangeEvent e) {
                String propertyName = e.getPropertyName();
                if (propertyName.equals("enabled")) { // NOT LOCALIZABLE,
                    // property key
                    setEnabled(((Boolean) e.getNewValue()).booleanValue());
                } else if (propertyName.equals(AbstractApplicationAction.VISIBLE)
                        || propertyName.equals(AbstractApplicationAction.TOOLBAR_VISIBLE_OPTION)) {
                    updateVisible();
                } else if (propertyName.equals(AbstractDomainAction.VALUES)) {
                    preventEvent = true;
                    updateItems((Object[]) e.getNewValue());
                    preventEvent = false;
                } else if (propertyName.equals(AbstractDomainAction.SELECTED_VALUE)) {
                    if (uiSelectionVisible) {
                        int newsel = e.getNewValue() == null ? -1 : ((Integer) e.getNewValue())
                                .intValue();
                        preventEvent = true;
                        setSelectedIndex(newsel);
                        preventEvent = false;
                    }
                } else if (propertyName.equals(Action.SHORT_DESCRIPTION))
                    setToolTipText(e.getNewValue() == null ? "" : (String) e.getNewValue());
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

    protected final void configurePropertiesFromAction(Action a) {
        setEnabled(a != null ? a.isEnabled() : false);
        setToolTipText(a != null ? (String) a.getValue(Action.SHORT_DESCRIPTION) : null);
        updateItems(a != null ? ((AbstractDomainAction) a).getDomainValues() : null);
        uiSelectionVisible = (a != null) ? ((AbstractDomainAction) a).isUISelectionVisible() : true;
        if (uiSelectionVisible)
            setSelectedIndex(a != null ? ((AbstractDomainAction) a).getSelectedIndex() : -1);
        ActionHelpPropertySupport.registerHelpSupport(this, (AbstractDomainAction) a);
        updateVisible();
    }

    private void updateItems(Object[] values) {
        setSelectedItem(null);
        removeAllItems();
        if (values != null) {
            Object item = null;
            for (int i = 0; i < values.length; i++) {
                Object value = values[i];
                if (value == null)
                    item = new SeparatorIcon();
                else if (value instanceof Color)
                    item = new ColorIcon((Color) value);
                else if (value instanceof Icon)
                    item = value;
                else
                    item = value; // will be converted as toString()
                insertItemAt(item, i);
            }
        }
    }

    static class DomainComboBoxModel extends DefaultComboBoxModel {

    }
}
