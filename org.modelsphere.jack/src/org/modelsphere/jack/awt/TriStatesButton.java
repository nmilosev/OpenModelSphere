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
import org.modelsphere.jack.actions.AbstractTriStatesAction;
import org.modelsphere.jack.util.ExceptionHandler;

public class TriStatesButton extends JButton {
    public static final int STATE_NOT_APPLICABLE = -1;
    public static final int STATE_OFF = 0;
    public static final int STATE_ON = 1;
    public static final int STATE_ON_OFF = 2;

    public static final int DEFAULT_STATE = STATE_NOT_APPLICABLE;

    private int state = DEFAULT_STATE;

    public TriStatesButton() {
        super();
        setState(STATE_NOT_APPLICABLE);
        setText("");
    }

    public TriStatesButton(Icon icon) {
        super(icon);
        setState(STATE_NOT_APPLICABLE);
        setText("");
    }

    public TriStatesButton(String text) {
        super(text);
        setState(STATE_NOT_APPLICABLE);
        setText("");
    }

    public TriStatesButton(String text, Icon icon) {
        super(text, icon);
        setState(STATE_NOT_APPLICABLE);
        setText("");
    }

    public TriStatesButton(AbstractTriStatesAction a) { // a must be a not null value
        super(a);
    }

    protected final void configurePropertiesFromAction(Action a) {
        AbstractTriStatesAction action = (AbstractTriStatesAction) a;
        //setText((action!=null?(String)action.getValue(Action.NAME):null));
        setText("");
        setIcon((action != null ? (Icon) action.getValue(Action.SMALL_ICON) : null));
        setEnabled((action != null ? action.isEnabled() : true));
        setToolTipText((action != null ? (String) action.getValue(Action.SHORT_DESCRIPTION) : null));
        ActionHelpPropertySupport.registerHelpSupport(this, action);
        int actionstate = action.getState();
        switch (actionstate) {
        case AbstractTriStatesAction.STATE_ON:
            setState(STATE_ON);
            break;
        case AbstractTriStatesAction.STATE_OFF:
            setState(STATE_OFF);
            break;
        case AbstractTriStatesAction.STATE_ON_OFF:
            setState(STATE_ON_OFF);
            break;
        case AbstractTriStatesAction.STATE_NOT_APPLICABLE:
            setState(STATE_NOT_APPLICABLE);
            break;
        }
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
                } else if (propertyName.equals(AbstractTriStatesAction.STATE)) {
                    int newValue = ((Integer) e.getNewValue()).intValue();
                    switch (newValue) {
                    case AbstractTriStatesAction.STATE_ON:
                        setState(STATE_ON);
                        break;
                    case AbstractTriStatesAction.STATE_OFF:
                        setState(STATE_OFF);
                        break;
                    case AbstractTriStatesAction.STATE_ON_OFF:
                        setState(STATE_ON_OFF);
                        break;
                    case AbstractTriStatesAction.STATE_NOT_APPLICABLE:
                        setState(STATE_NOT_APPLICABLE);
                        break;
                    }
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

    public final void setState(int newState) {
        try {
            org.modelsphere.jack.debug.Debug
                    .assert2(
                            newState == STATE_ON || newState == STATE_OFF
                                    || newState == STATE_ON_OFF || newState == STATE_NOT_APPLICABLE,
                            "int (newState) argument value must be one of: STATE_ON, STATE_OFF, STATE_ON_OFF or STATE_NOT_APPLICABLE values. TriStateButton.setState(int)."); //NOT LOCALIZABLE
        } catch (Exception ex) {
            ExceptionHandler.processUncatchedException(null, ex);
        }
        state = newState;
        updateLook();
        repaint();
    }

    private final void updateLook() {
        switch (state) {
        case STATE_ON:
            setLookOn();
            break;
        case STATE_OFF:
            setLookOff();
            break;
        case STATE_ON_OFF:
            setLookOnOff();
            break;
        case STATE_NOT_APPLICABLE:
            setLookNotApplicable();
            break;
        }
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

    private final void setLookOnOff() {
        setLookOff();
    }

    public final int getState() {
        return state;
    }

    public final void updateUI() {
        super.updateUI();
        updateLook();
    }

}
