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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.srtool.actions.ShowAbstractAction;

public class ShowHideButton extends JButton {
    private boolean componentVisible = false;

    public ShowHideButton(ShowAbstractAction a) { // a must be a not null value
        super(a);
    }

    protected final void configurePropertiesFromAction(Action a) {
        ShowAbstractAction action = (ShowAbstractAction) a;
        //setText((action!=null?(String)action.getValue(Action.NAME):null));
        setText("");
        setIcon((action != null ? (Icon) action.getValue(Action.SMALL_ICON) : null));
        setEnabled((action != null ? action.isEnabled() : true));
        setToolTipText((action != null ? (String) action.getValue(Action.SHORT_DESCRIPTION) : null));
        ActionHelpPropertySupport.registerHelpSupport(this, action);
        setComponentVisible(action.isComponentVisible());
        updateVisible();
    }

    protected final PropertyChangeListener createActionPropertyChangeListener(Action a) {
        return new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                String propertyName = e.getPropertyName();
                /*
                 * if (propertyName.equals(Action.NAME)) { String text = (String) e.getNewValue();
                 * setText(text); repaint(); } else
                 */if (propertyName.equals("enabled")) { //NOT LOCALIZABLE
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
                /*
                 * else if (propertyName.equals(Action.MNEMONIC_KEY)) { Integer mn = (Integer)
                 * e.getNewValue(); setMnemonic(mn.intValue()); invalidate(); repaint(); }
                 */
                else if (propertyName.equals(AbstractApplicationAction.VISIBLE)
                        || propertyName.equals(AbstractApplicationAction.TOOLBAR_VISIBLE_OPTION)) {
                    updateVisible();
                } else if (propertyName.equals(ShowAbstractAction.COMPONENT_VISIBLE)) {
                    boolean newValue = ((Boolean) e.getNewValue()).booleanValue();
                    setComponentVisible(newValue);
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

    private final void setComponentVisible(boolean b) {
        componentVisible = b;
        updateLook();
    }

    private final void updateLook() {
        if (isEnabled())
            if (componentVisible)
                setLookOn();
            else
                setLookOff();
        else
            setLookNotApplicable();
    }

    private final void setLookNotApplicable() {
        setSelected(false);
        setEnabled(false);
    }

    private final void setLookOn() {
        setSelected(true);
        setEnabled(true);
    }

    private final void setLookOff() {
        setSelected(false);
        setEnabled(true);
    }

    public final void updateUI() {
        super.updateUI();
        updateLook();
    }

}
