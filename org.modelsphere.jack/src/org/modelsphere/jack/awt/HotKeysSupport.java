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

import java.awt.event.*;

import javax.swing.*;

/**
 * This class provide Swing Containers support for hot keys 'Escape' for a cancel button and 'F1'
 * for a Help button Exemple (using a JDialog): new HotKeysSupport(this, myCancelButton,
 * myHelpButton);
 * 
 * or HotKeysSupport hks = new HotKeysSupport(this); hks.setCancelButton(myCancelButton);
 * hks.setHelpButton(myHelpButton);
 * 
 * Those buttons can be modified or set to null;
 * 
 * When the key is pressed, the action of the corresponding button is triggered
 * 
 * This class was written using the code provided for the default button support in swing class
 * JRootPane (see method setDefaultButton)
 * 
 * Known problems: On some components (JComboBox, JTextFields), VK_ESCAPE and VK_ENTER may not
 * works. It's a swing bug. Sun is working on it (Sun bug id 4256046). Update: This bug has been
 * solved in 1.4 RC for VK_ESCAPE. VK_ENTER seems to be normal behavior!
 */

public class HotKeysSupport implements WindowListener {
    private JRootPane rootPane;

    private JButton cancelButton;
    private HotKeysAction cancelPressAction;
    private HotKeysAction cancelReleaseAction;

    private JButton helpButton;
    private HotKeysAction helpPressAction;
    private HotKeysAction helpReleaseAction;

    public HotKeysSupport(RootPaneContainer container) {
        this(container, null, null);
    }

    public HotKeysSupport(RootPaneContainer container, JButton cancelButton, JButton helpButton) {
        try {
            org.modelsphere.jack.debug.Debug.assert2(container != null,
                    "RootPaneContainer can't be null for HotKeysSupport");
            rootPane = container.getRootPane();
            if (cancelButton != null)
                setCancelButton(cancelButton);
            if (helpButton != null)
                setHelpButton(helpButton);
        } catch (Exception e) {
        }
    }

    /*
     * Use this method for setting, changing or deleting the association with the hotkey
     * 'KeyEvent.VK_ESCAPE'
     */
    public void setCancelButton(JButton cancelButton) {
        try {
            org.modelsphere.jack.debug.Debug.assert2(rootPane != null,
                    "RootPaneContainer can't be null for HotKeysSupport");
            JButton oldCancel = this.cancelButton;
            if (oldCancel != cancelButton) {
                this.cancelButton = cancelButton;

                if (cancelButton != null) {
                    if (cancelPressAction == null) {
                        cancelPressAction = new HotKeysAction(rootPane, true);
                        cancelReleaseAction = new HotKeysAction(rootPane, false);
                        rootPane.registerKeyboardAction(cancelPressAction, KeyStroke.getKeyStroke(
                                KeyEvent.VK_ESCAPE, 0, false), JComponent.WHEN_IN_FOCUSED_WINDOW);
                        rootPane.registerKeyboardAction(cancelReleaseAction, KeyStroke
                                .getKeyStroke(KeyEvent.VK_ESCAPE, 0, true),
                                JComponent.WHEN_IN_FOCUSED_WINDOW);
                        // VK_ESCAPE with CTRL_MASK cause problem on windows
                        // (Start Menu activated)
                        /*
                         * rootPane.registerKeyboardAction(cancelPressAction,
                         * KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, InputEvent.CTRL_MASK, false),
                         * JComponent.WHEN_IN_FOCUSED_WINDOW);
                         * rootPane.registerKeyboardAction(cancelReleaseAction,
                         * KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, InputEvent.CTRL_MASK, true),
                         * JComponent.WHEN_IN_FOCUSED_WINDOW);
                         */
                    }
                    cancelPressAction.setOwner(cancelButton);
                    cancelReleaseAction.setOwner(cancelButton);

                    // Break links for gc on dispose
                    JWindow win = (JWindow) SwingUtilities.getAncestorOfClass(JWindow.class,
                            rootPane);
                    if (win != null) {
                        win.addWindowListener(this);
                    }
                } else {
                    rootPane.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0,
                            false));
                    rootPane.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0,
                            true));
                    // VK_ESCAPE with CTRL_MASK cause problem on windows (Start
                    // Menu activated)
                    /*
                     * rootPane.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent .VK_ESCAPE,
                     * InputEvent.CTRL_MASK, false)); rootPane.unregisterKeyboardAction
                     * (KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, InputEvent.CTRL_MASK, true));
                     */
                    cancelPressAction.root = null;
                    cancelPressAction.owner = null;
                    cancelPressAction = null;
                    cancelReleaseAction.root = null;
                    cancelReleaseAction.owner = null;
                    cancelReleaseAction = null;
                    cancelPressAction = null;
                    cancelReleaseAction = null;
                }

                if (oldCancel != null) {
                    oldCancel.repaint();
                }
                if (cancelButton != null) {
                    cancelButton.repaint();
                }
            }
        } catch (Exception e) {
        }
    }

    /*
     * Use this method for setting, changing or deleting (null parameter) the association with the
     * hotkeys
     */
    public void setHelpButton(JButton helpButton) {
        try {
            org.modelsphere.jack.debug.Debug.assert2(rootPane != null,
                    "RootPaneContainer can't be null for HotKeysSupport");
            JButton oldHelp = this.helpButton;
            if (oldHelp != helpButton) {
                this.helpButton = helpButton;

                if (helpButton != null) {
                    if (helpPressAction == null) {
                        helpPressAction = new HotKeysAction(rootPane, true);
                        helpReleaseAction = new HotKeysAction(rootPane, false);
                        rootPane.registerKeyboardAction(helpPressAction, KeyStroke.getKeyStroke(
                                KeyEvent.VK_F1, 0, false), JComponent.WHEN_IN_FOCUSED_WINDOW);
                        rootPane.registerKeyboardAction(helpReleaseAction, KeyStroke.getKeyStroke(
                                KeyEvent.VK_F1, 0, true), JComponent.WHEN_IN_FOCUSED_WINDOW);
                        rootPane.registerKeyboardAction(helpPressAction, KeyStroke.getKeyStroke(
                                KeyEvent.VK_F1, InputEvent.CTRL_MASK, false),
                                JComponent.WHEN_IN_FOCUSED_WINDOW);
                        rootPane.registerKeyboardAction(helpReleaseAction, KeyStroke.getKeyStroke(
                                KeyEvent.VK_F1, InputEvent.CTRL_MASK, true),
                                JComponent.WHEN_IN_FOCUSED_WINDOW);
                    }
                    helpPressAction.setOwner(helpButton);
                    helpReleaseAction.setOwner(helpButton);

                    // Break links for gc on dispose
                    JWindow win = (JWindow) SwingUtilities.getAncestorOfClass(JWindow.class,
                            rootPane);
                    if (win != null) {
                        win.addWindowListener(this);
                    }
                } else {
                    rootPane.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0,
                            false));
                    rootPane.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0,
                            true));
                    rootPane.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
                            InputEvent.CTRL_MASK, false));
                    rootPane.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
                            InputEvent.CTRL_MASK, true));
                    helpPressAction.root = null;
                    helpPressAction.owner = null;
                    helpPressAction = null;
                    helpReleaseAction.root = null;
                    helpReleaseAction.owner = null;
                    helpReleaseAction = null;
                }

                if (oldHelp != null) {
                    oldHelp.repaint();
                }
                if (helpButton != null) {
                    helpButton.repaint();
                }
            }
        } catch (Exception e) {
        }
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
        if (helpPressAction != null) {
            if (rootPane != null) {
                rootPane.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0, false));
                rootPane.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0, true));
                rootPane.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
                        InputEvent.CTRL_MASK, false));
                rootPane.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
                        InputEvent.CTRL_MASK, true));
            }
            helpPressAction.root = null;
            helpPressAction.owner = null;
            helpPressAction = null;
        }
        if (helpReleaseAction != null) {
            helpReleaseAction.root = null;
            helpReleaseAction.owner = null;
            helpReleaseAction = null;
        }

        if (cancelPressAction != null) {
            if (rootPane != null) {
                rootPane.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0,
                        false));
                rootPane.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0,
                        true));
            }
            cancelPressAction.root = null;
            cancelPressAction.owner = null;
            cancelPressAction = null;
        }
        if (cancelReleaseAction != null) {
            cancelReleaseAction.root = null;
            cancelReleaseAction.owner = null;
            cancelReleaseAction = null;
        }
        rootPane = null;
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    // This class defines the action to perform when the escape key is pressed
    // (or F1 for help)
    // Reference: See the method setDefaultButton in class JRootPane. The
    // implementation is the same as
    // for the default action
    static class HotKeysAction extends AbstractAction {
        JButton owner;
        JRootPane root;
        boolean press;

        HotKeysAction(JRootPane root, boolean press) {
            this.root = root;
            this.press = press;
        }

        public void setOwner(JButton owner) {
            this.owner = owner;
        }

        public void actionPerformed(ActionEvent e) {
            if (owner != null && SwingUtilities.getRootPane(owner) == root) {
                ButtonModel model = owner.getModel();
                if (press) {
                    model.setArmed(true);
                    model.setPressed(true);
                } else {
                    model.setPressed(false);
                }
            }
        }

        public boolean isEnabled() {
            return owner.getModel().isEnabled();
        }

    }

}
