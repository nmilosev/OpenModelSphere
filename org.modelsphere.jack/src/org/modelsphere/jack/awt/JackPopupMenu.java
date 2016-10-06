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

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.jack.actions.AbstractTriStatesAction;
import org.modelsphere.jack.graphic.GraphicComponent;

public class JackPopupMenu extends JPopupMenu {

    private static final int screenMargin = 25;

    private Point diagramLocation = null;
    private GraphicComponent graphicComponent = null;

    // This is a flag to avoid a false remove in java 1.4 (insert(comp, index) in 1.4 use a remove on components at index and components.length)
    private boolean inserting = false;

    public JackPopupMenu() {
        super();
    }

    public JackPopupMenu(String s) {
        super(s);
    }

    public final JMenuItem add(Action a, Object[] selObjects) {
        JMenuItem item = add(a, null, selObjects);
        return item;
    }

    public final JMenuItem add(Action a, String text, Object[] selObjects) {
        if (a == null)
            return null;

        JMenuItem item;
        if (a instanceof AbstractApplicationAction) {
            AbstractApplicationAction applAction = (AbstractApplicationAction) a;
            item = applAction.createItem(this);
        } else {
            item = super.add(a);
        } //end if

        if (a instanceof AbstractApplicationAction) {
            AbstractApplicationAction applAction = (AbstractApplicationAction) a;
            applAction.init(item, selObjects);
        }

        if (text != null) {
            item.setText(text);
        }

        return item;
    }

    public final void insert(Action a, int index) {
        if (a == null)
            return;

        inserting = true;
        super.insert(a, index);
        inserting = false;
    }

    public final void remove(Component comp) {
        super.remove(comp);
        if (!inserting && comp instanceof JMenuItem) {
            JMenuItem mi = (JMenuItem) comp;
            mi.setAction(null);
        }
    }

    public final void remove(int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero."); //NOT LOCALIZABLE
        }
        if (pos > getComponentCount() - 1) {
            throw new IllegalArgumentException("index greater than the number of items."); //NOT LOCALIZABLE
        }
        Component c = getComponent(pos);
        if (!inserting && c instanceof JMenuItem) {
            JMenuItem mi = (JMenuItem) c;
            mi.setAction(null);
        }
        super.remove(pos);
    }

    public final Point getDiagramLocation() {
        return diagramLocation;
    }

    public final GraphicComponent getGraphicComponent() {
        return graphicComponent;
    }

    public final void setDiagramLocation(Point pt, GraphicComponent gc) {
        diagramLocation = pt;
        graphicComponent = gc;
    }

    @Override
    protected final JMenuItem createActionComponent(Action a) {
        JMenuItem mi = null;

        if (a instanceof AbstractTriStatesAction) {
            mi = new TriStateCheckBoxMenuItem((AbstractTriStatesAction) a);
        } else if (a instanceof AbstractDomainAction) {
            mi = new DomainMenu((AbstractDomainAction) a);
        } else if (a instanceof AbstractApplicationAction) {
            AbstractApplicationAction applAction = (AbstractApplicationAction) a;
            mi = new AbstractApplicationActionMenuItem(applAction);
            //mi = applAction.createMenuItem(this, selObjects);
        } else {
            mi = new JMenuItem(a) {
                protected void configurePropertiesFromAction(Action a) {
                    setText((a != null ? (String) a.getValue(Action.NAME) : null));
                    setIcon((a != null ? (Icon) a.getValue(Action.SMALL_ICON) : null));
                    setEnabled((a != null ? a.isEnabled() : true));
                    if (a != null) { // may not work on popup - swing bug 1.3RC1  - ok in 1.4
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
                            } else if (propertyName.equals("enabled")) { // NOT LOCALIZABLE - property
                                Boolean enabledState = (Boolean) e.getNewValue();
                                setEnabled(enabledState.booleanValue());
                                repaint();
                            } else if (propertyName.equals(Action.SMALL_ICON)) {
                                Icon icon = (Icon) e.getNewValue();
                                setIcon(icon);
                                invalidate();
                                repaint();
                            } else if (propertyName.equals(Action.MNEMONIC_KEY)) { // may not work on popup - swing bug 1.3RC1  - ok in 1.4
                                Integer mn = (Integer) e.getNewValue();
                                setMnemonic(mn.intValue());
                                invalidate();
                                repaint();
                            }
                        }
                    };
                }
            };
        }

        // Add a listener for removing all references between the action and the component after the popup will be removed
        // this is necessary because AbstractApplicationAction actions are singleton and a reference to them is kept
        // in the action store ... blocking the gc from removing the action and the component.
        // This listener will call a setAction(null).
        if (a instanceof AbstractApplicationAction)
            new MenuItemActionUninstaller(mi);

        mi.setHorizontalTextPosition(JButton.RIGHT);
        mi.setVerticalTextPosition(JButton.CENTER);

        if (a != null && mi != null) {
            mi.setName(a.getClass().getName());
        }

        return mi;
    }

    /**
     * Override the implementation in JMenu in order to be more intelligent about determining the
     * placement of the popup menu.
     * 
     * set to the super a Point in the coordinate space of the menu instance which should be used as
     * the origin of the JMenu's popup menu.
     */
    public void setLocation(int userx, int usery) {
        int x = userx;
        int y = usery;

        // Figure out the sizes needed to caclulate the menu position
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Dimension pmSize = getSize();
        // For the first time the menu is popped up,
        // the size has not yet been initiated
        if (pmSize.width == 0)
            pmSize = getPreferredSize();

        Container parent = getParent();
        if (parent instanceof JPopupMenu) {
            // We are a submenu (pull-right)
            Dimension s = parent.getSize();

            // First determine x:
            if (userx + s.width + pmSize.width < screenSize.width)
                x = s.width; // Prefer placement to the right
            else
                x = 0 - pmSize.width; // Otherwise place to the left
            // Then the y:
            if (usery + pmSize.height < screenSize.height)
                y = 0; // Prefer dropping down
            else
                y = s.height - pmSize.height; // Otherwise drop 'up'
        } else {
            // We are a toplevel menu (pull-down)

            // First determine the x:
            if (userx + pmSize.width > screenSize.width - screenMargin) {
                x = screenSize.width - screenMargin - pmSize.width;
                if (x < screenMargin)
                    x = screenMargin;
            }

            // Then the y:
            if (usery + pmSize.height > screenSize.height - screenMargin) {
                y = screenSize.height - screenMargin - pmSize.height;
                if (y < screenMargin)
                    y = screenMargin;
            }
        }
        //patch , since the click that activate the popup seems to activate the
        //menu item place under the mouse position when I override the position,I
        //offset the x position by 4 pix so the mouse won't be over the popup menu
        if (usery != y)
            x += 4;
        super.setLocation(x, y);
    }

    public static class AbstractApplicationActionMenuItem extends JMenuItem {

        public AbstractApplicationActionMenuItem(AbstractApplicationAction a) {
            super(a);
        }

        protected void configurePropertiesFromAction(Action a) {
            AbstractApplicationAction action = (AbstractApplicationAction) a;
            setIcon((a != null ? (Icon) a.getValue(Action.SMALL_ICON) : null));
            setText((action != null ? (String) action.getValue(Action.NAME) : null));
            setEnabled((action != null ? action.isEnabled() : true));
            if (a != null
                    && (action.getVisibilityMode() & AbstractApplicationAction.VISIBILITY_ALWAYS_VISIBLE_IN_POPUPMENU) == 0)
                setVisible((action != null ? action.isVisible() : true));
            else
                setVisible(true);
            if (action != null) { // may not work on popup - swing bug 1.3RC1
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
                    } else if (propertyName.equals("enabled")) { // NOT LOCALIZABLE - property
                        Boolean enabledState = (Boolean) e.getNewValue();
                        setEnabled(enabledState.booleanValue());
                        repaint();
                    } else if (propertyName.equals(Action.MNEMONIC_KEY)) { // may not work on popup - swing bug 1.3RC1
                        Integer mn = (Integer) e.getNewValue();
                        setMnemonic(mn.intValue());
                        invalidate();
                        repaint();
                    } else if (propertyName.equals(Action.SMALL_ICON)) {
                        Icon icon = (Icon) e.getNewValue();
                        setIcon(icon);
                        invalidate();
                        repaint();
                    } else if (propertyName.equals(AbstractApplicationAction.VISIBLE)) {
                        if (getAction() instanceof AbstractApplicationAction) {
                            boolean newValue = true;
                            if ((((AbstractApplicationAction) getAction()).getVisibilityMode() & AbstractApplicationAction.VISIBILITY_ALWAYS_VISIBLE_IN_POPUPMENU) == 0)
                                newValue = ((Boolean) e.getNewValue()).booleanValue();
                            setVisible(newValue);
                            invalidate();
                            repaint();
                        }
                    }
                }
            };
        }

    }

}
