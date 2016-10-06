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
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.srtool.ApplicationContext;

class ActionHelpPropertySupport implements PropertyChangeListener {
    private static final String HELP_KEY = "SRHelpContextualText"; // NOT
    // LOCALIZABLE,
    // property
    // key
    private static JComponentHelpListenerSupport jComponentHelpListener = new JComponentHelpListenerSupport();

    private JComponent component;

    private ActionHelpPropertySupport(JComponent c) {
        component = c;
    }

    static final void registerHelpSupport(JComponent c, AbstractApplicationAction a) {
        if (c == null || a == null)
            return;
        String helpText = (String) a.getValue(AbstractApplicationAction.LONG_DESCRIPTION);
        if (helpText != null) {
            c.putClientProperty(HELP_KEY, helpText);
            registerComponentListeners(c);
        }
        c.addPropertyChangeListener(new ActionHelpPropertySupport(c));
    }

    static final void unregisterHelpSupport(JComponent c, AbstractApplicationAction a) {
        if (c == null)
            return;
        unregisterComponentListeners(c);
    }

    private static void registerComponentListeners(JComponent c) {
        c.addMouseListener(jComponentHelpListener);
        c.addFocusListener(jComponentHelpListener);
        if (c instanceof AbstractButton)
            ((AbstractButton) c).addChangeListener(jComponentHelpListener);
    }

    private static void unregisterComponentListeners(JComponent c) {
        c.removeMouseListener(jComponentHelpListener);
        c.removeFocusListener(jComponentHelpListener);
        if (c instanceof AbstractButton)
            ((AbstractButton) c).removeChangeListener(jComponentHelpListener);
    }

    public final void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(HELP_KEY)) {
            // component.putClientProperty(HELP_KEY, evt.getNewValue());
            if ((evt.getOldValue() == null) && (evt.getNewValue() != null)) {
                registerComponentListeners(component);
            } else if ((evt.getOldValue() != null) && (evt.getNewValue() == null)) {
                unregisterComponentListeners(component);
            }
        }
    }

    // This class provide the behaviors of the help displayed
    private static class JComponentHelpListenerSupport extends MouseAdapter implements
            FocusListener, ChangeListener {
        JComponentHelpListenerSupport() {
        }

        public final void stateChanged(ChangeEvent e) {
            AbstractButton button = (AbstractButton) e.getSource();
            setHelpVisible(button, button.getModel().isArmed());
        }

        public final void mouseEntered(MouseEvent e) {
            setHelpVisible(e.getComponent(), true);
        }

        public final void mouseExited(MouseEvent e) {
            setHelpVisible(e.getComponent(), false);
        }

        public final void mouseReleased(MouseEvent e) {
            setHelpVisible(e.getComponent(), false);
        }

        public final void focusGained(FocusEvent e) {
            setHelpVisible(e.getComponent(), true);
        }

        public final void focusLost(FocusEvent e) {
            setHelpVisible(e.getComponent(), false);
        }

        private void setHelpVisible(Component component, boolean visible) {
            if (component instanceof JComponent) {
                JComponent c = (JComponent) component;
                String helpText = (String) c.getClientProperty(HELP_KEY);
                ApplicationContext.getDefaultMainFrame().getStatusBar().setMessage(
                        visible ? helpText : "");
            }
        }
    }

}
