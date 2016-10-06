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
import java.awt.Font;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.AbstractTriStatesAction;

public class TriStateCheckBoxMenuItem extends JCheckBoxMenuItem {
    public static final int STATE_ON_OFF = AbstractTriStatesAction.STATE_ON_OFF;
    public static final int STATE_OFF = AbstractTriStatesAction.STATE_OFF;
    public static final int STATE_ON = AbstractTriStatesAction.STATE_ON;
    public static final int STATE_NOT_APPLICABLE = AbstractTriStatesAction.STATE_NOT_APPLICABLE;

    public static final String TRI_STATE_PROPERTY = new String("triState"); // NOT LOCALIZABLE, property key

    private int triState = STATE_NOT_APPLICABLE;
    private boolean visibility = true;

    private HierarchyListener hierarchyListener = new HierarchyListener() {
        public void hierarchyChanged(HierarchyEvent e) {
            if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0) {
                if (e.getChangedParent() != null)
                    updateVisible();
            }
        }

    };

    public TriStateCheckBoxMenuItem(AbstractTriStatesAction action) {
        super(action);
        addHierarchyListener(hierarchyListener);
    }

    protected final void configurePropertiesFromAction(Action a) {
        AbstractTriStatesAction action = (AbstractTriStatesAction) a;
        setText((action != null ? (String) action.getValue(Action.NAME) : null));
        setIcon((action != null ? (Icon) action.getValue(Action.SMALL_ICON) : null));
        setEnabled((action != null ? action.isEnabled() : true));
        setTriState((action != null ? action.getState() : STATE_NOT_APPLICABLE));
        // updateVisible();
        setAccelerator((action != null ? action.getAccelerator() : null));
        if (action != null) {
            Integer i = (Integer) action.getValue(AbstractApplicationAction.MNEMONIC_KEY);
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
                } else if (propertyName.equals("enabled")) { // NOT LOCALIZABLE
                    Boolean enabledState = (Boolean) e.getNewValue();
                    setEnabled(enabledState.booleanValue());
                    repaint();
                } else if (propertyName.equals(AbstractApplicationAction.MNEMONIC_KEY)) {
                    Integer mn = (Integer) e.getNewValue();
                    setMnemonic((mn != null) ? mn.intValue() : KeyEvent.VK_UNDEFINED);
                    invalidate();
                    repaint();
                } else if (propertyName.equals(AbstractTriStatesAction.STATE)) {
                    Integer state = (Integer) e.getNewValue();
                    if (triState != state.intValue())
                        setTriState(state.intValue());
                    invalidate();
                    repaint();
                } else if (propertyName.equals(AbstractApplicationAction.VISIBLE)) {
                    // updateVisible();
                } else if (propertyName.equals(AbstractApplicationAction.ACCELERATOR_KEY)) {
                    KeyStroke newValue = (KeyStroke) e.getNewValue();
                    setAccelerator(newValue);
                    invalidate();
                    repaint();
                }
            }
        };
    }

    public final void setTriState(int newState) {
        if (newState != triState) {
            triState = newState;
            updateFontUI();
            setSelected(triState == STATE_ON);
            setEnabled(triState != STATE_NOT_APPLICABLE);
            putClientProperty(AbstractTriStatesAction.STATE, new Integer(triState));
        }
    }

    public final int getTriState() {
        return triState;
    }

    private final void updateFontUI() {
        Font font = UIManager.getFont("CheckBoxMenuItem.font"); // NOT LOCALIZABLE
        Font newfFont;
        if (font == null)
            font = (new JCheckBoxMenuItem()).getFont();
        int newfontstyle = font.getStyle();
        if (triState == STATE_ON_OFF) {
            newfFont = new Font(font.getName(), Font.ITALIC | newfontstyle, font.getSize());
        } else {
            newfFont = new Font(font.getName(), newfontstyle, font.getSize());
        }
        setFont(newfFont);
    }

    public void updateUI() {
        super.updateUI();
        updateFontUI();
    }

    private void updateVisible() {
        if (getParent() != null && getAction() instanceof AbstractApplicationAction) {
            boolean newValue = true;
            AbstractApplicationAction action = (AbstractApplicationAction) getAction();
            Component ancestor = SwingUtilities.getAncestorOfClass(JackPopupMenu.class, this);
            // if parent not a JackPopupMenu, this component has been added to a
            // JMenu's JPopupMenu.
            if ((ancestor == null && ((action.getVisibilityMode() & AbstractApplicationAction.VISIBILITY_ALWAYS_VISIBLE_IN_MENU) == 0))
                    || (ancestor != null && ((action.getVisibilityMode() & AbstractApplicationAction.VISIBILITY_ALWAYS_VISIBLE_IN_POPUPMENU) == 0)))
                newValue = action.isVisible() && newValue;
            setVisible(newValue);
            invalidate();
            repaint();
        }
    }

    public void setIcon(Icon icon) {
        super.setIcon(icon);
    }
}
