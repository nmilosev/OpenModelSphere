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
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.AbstractMenuAction;
import org.modelsphere.jack.srtool.AddElement;
import org.modelsphere.jack.srtool.list.ListDescriptor;

public class PopupMenuButton extends JButton implements ActionListener {

    private Object[] values = null;
    private PopupPanel popup = new PopupPanel();

    public PopupMenuButton(AbstractMenuAction action) {
        super(action);
        setText(null);
        setFocusable(false);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                showPopup();
            }
        });
    }

    public PopupMenuButton() {
        super();
        setText(null);
        setFocusable(false);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                showPopup();
            }
        });
    }

    private void showPopup() {
        if (isEnabled()) {
            if (popup.isVisible()) {
                popup.setVisible(false);
            } else {
                Component[] components = popup.getComponents();
                if (components != null) {
                    for (int i = 0; i < components.length; i++) {
                        if (components[i] instanceof Item)
                            ((Item) components[i]).removeActionListener(this);
                        else if (components[i] instanceof MenuItem) {
                            Component[] subcomponents = ((MenuItem) components[i]).getComponents();
                            for (int j = 0; j < components.length; j++) {
                                if (components[j] instanceof Item)
                                    ((Item) components[j]).removeActionListener(this);
                            }
                        }
                    }
                }
                popup.removeAll();
                if (values == null)
                    return;
                for (int i = 0; i < values.length; i++) {
                    JMenuItem item = null;
                    if (values[i] instanceof ListDescriptor) {
                        ListDescriptor descriptor = (ListDescriptor) values[i];
                        Icon icon = descriptor.getIcon();
                        item = new Item(values[i].toString(), icon, i);
                        item.addActionListener(this);
                    } else if (values[i] instanceof AddElement) {
                        AddElement element = (AddElement) values[i];
                        Icon icon = element.getIcon();

                        Object[] choiceValues = element.getChoiceValues();
                        if (choiceValues != null) {
                            item = new MenuItem(element.toString(), icon, i);
                            for (int j = 0; j < choiceValues.length; j++) {
                                if (choiceValues[j] == null) {
                                    ((JMenu) item).addSeparator();
                                    continue;
                                }
                                Item subItem = new Item(choiceValues[j].toString(), j);
                                ((JMenu) item).add(subItem);
                                subItem.setParentIndex(i);
                                subItem.addActionListener(this);
                            }
                        } else {
                            item = new Item(element.toString(), icon, i);
                            item.addActionListener(this);
                        }
                        item.setVisible(element.isVisible());
                        item.setEnabled(element.isEnabled());
                    } else {
                        if (values[i] != null) {
                            item = new Item(values[i].toString(), i);
                            item.addActionListener(this);
                        }
                    }
                    if (item != null) {
                        popup.add(item);
                    } else
                        popup.addSeparator();

                }
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        AwtUtil.showPopupPanel(PopupMenuButton.this, popup);
                    }
                });
            }
        }
    }

    private class Item extends JMenuItem {
        int index = -1;
        int parentIndex = -1;

        Item(String text, int index) {
            super(text);
            this.index = index;
        }

        Item(String text, Icon icon, int index) {
            super(text, icon);
            this.index = index;
        }

        void setParentIndex(int index) {
            parentIndex = index;
        }
    }

    private class MenuItem extends JMenu {
        int index = -1;

        MenuItem(String text, int index) {
            super(text);
            this.index = index;
        }

        MenuItem(String text, Icon icon, int index) {
            super(text);
            this.index = index;
            this.setIcon(icon);
        }
    }

    protected void setValues(Object[] values) {
        this.values = values;
    }

    // This class draw an arrow pointing in the south direction.
    // The main algo for this come from
    // javax.swing.plaf.basic.DefaultArrowButton.
    // In JDK 1.3, it is not possible to get this icon (or button) using the UI
    // properties
    // table (no entry for this).
    private static class ArrowIcon implements Icon {
        int height = 16;
        int width = 8;

        ArrowIcon() {
        }

        public int getIconWidth() {
            return width;
        }

        public int getIconHeight() {
            return height;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Color oldColor = g.getColor();
            boolean enabled = c.isEnabled();
            boolean pressed = false;
            if (c instanceof JButton) {
                pressed = ((JButton) c).getModel().isPressed();
            }

            int i = 0;
            int j = 0;
            int w = Math.min(width, c.getWidth());
            int h = c.getHeight();
            if (h < 5 || w < 5) { // not enough space for the arrow
                g.setColor(oldColor);
                return;
            }

            int size = Math.min(h / 2, w / 2);
            size = Math.max(size, 2);
            int mid = size / 2;

            x = ((w - size) / 2); // center arrow
            y = (h - size) / 2; // center arrow
            if (pressed) {
                x++;
                y++;
            }
            g.translate(x, y); // move the x,y origin in the graphic

            if (enabled)
                g.setColor(UIManager.getColor("controlDkShadow")); // NOT
            // LOCALIZABLE
            else
                g.setColor(UIManager.getColor("controlShadow")); // NOT
            // LOCALIZABLE

            if (!enabled) {
                g.translate(1, 1);
                g.setColor(UIManager.getColor("controlLtHighlight")); // NOT
                // LOCALIZABLE
                for (i = size - 1; i >= 0; i--) {
                    g.drawLine(mid - i, j, mid + i, j);
                    j++;
                }
                g.translate(-1, -1);
                g.setColor(UIManager.getColor("controlShadow")); // NOT
                // LOCALIZABLE
            }

            j = 0;
            for (i = size - 1; i >= 0; i--) {
                g.drawLine(mid - i, j, mid + i, j);
                j++;
            }

            g.translate(-x, -y);
            g.setColor(oldColor);
        }

    }

    // Contains 2 icons - The action icon and the arrow
    private static class ArrowMultiIcon implements Icon {
        private static final int X_GAP = 2; // space between the 2 icons
        private ImageIcon icon;
        private ImageIcon disabledIcon;
        private ArrowIcon arrow;

        ArrowMultiIcon(ImageIcon icon) {
            this.icon = icon;
            if (icon != null) {
                disabledIcon = new ImageIcon(GrayFilter.createDisabledImage(icon.getImage()));
            }
            arrow = new ArrowIcon();
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            boolean enabled = c.isEnabled();

            // paint the icon
            Icon paintedIcon = enabled ? icon : disabledIcon;
            if (paintedIcon != null)
                paintedIcon.paintIcon(c, g, x, y);

            // backup current color
            Color oldColor = g.getColor();
            int insetx = 4;
            if (c instanceof JComponent) {
                Insets borderinset = ((JComponent) c).getBorder().getBorderInsets(c);
                insetx = borderinset.left;
            }
            if (paintedIcon != null) {
                g.translate(paintedIcon.getIconWidth() + X_GAP + insetx, 0);
            }

            arrow.paintIcon(c, g, x, y);
            if (paintedIcon != null) {
                g.translate(-paintedIcon.getIconWidth() - X_GAP - insetx, 0);
            }

            // restore previous color
            g.setColor(oldColor);
        }

        public int getIconWidth() {
            int width = arrow.getIconWidth();
            if (icon != null)
                width += icon.getIconWidth();
            width += X_GAP;
            return width;
        }

        public int getIconHeight() {
            int height = arrow.getIconHeight();
            if (icon != null)
                height = Math.max(icon.getIconHeight(), height);
            return height;
        }
    }

    public final void setIcon(Icon icon) {
        super.setIcon(new ArrowMultiIcon((ImageIcon) icon));
    }

    public void updateUI() {
        super.updateUI();
        if (popup != null)
            popup.updateUI();
    }

    // Triggered if an item from the popup get triggerred.
    public final void actionPerformed(ActionEvent e) {
        if (!(e.getSource() instanceof Item))
            return;

        final Item item = (Item) e.getSource();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (item.parentIndex == -1)
                    fireAction(item.index, -1);
                else
                    fireAction(item.parentIndex, item.index);
            }
        });
    }

    protected final void configurePropertiesFromAction(Action a) {
        AbstractMenuAction action = (AbstractMenuAction) a;
        setText((action != null ? (String) action.getValue(Action.NAME) : null));
        setIcon((action != null ? (Icon) action.getValue(Action.SMALL_ICON) : null));
        setEnabled((action != null ? action.isEnabled() : true));
        setToolTipText((action != null ? (String) action.getValue(Action.SHORT_DESCRIPTION) : null));
        values = action != null ? action.getDomainValues() : null;
        updateVisible();
        ActionHelpPropertySupport.registerHelpSupport(this, action);
    }

    protected final PropertyChangeListener createActionPropertyChangeListener(Action a) {
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
                } else if (propertyName.equals(AbstractMenuAction.VALUES)) {
                    setValues(((AbstractMenuAction) getAction()).getDomainValues());
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

    // This method is used to trigger the action performed on a menu item to the
    // actionListeners.
    // The action command contain an integer representing the activated item
    // index in the popup
    protected void fireAction(int index, int subindex) {
        AbstractMenuAction action = (AbstractMenuAction) getAction();
        /*
         * if (action != null){ action.setSelectedPrivate_(index, this); }
         */
        fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, new Integer(index)
                .toString()
                + AbstractMenuAction.ACTION_COMMAND_SUB_VALUE_SEPARATOR
                + new Integer(subindex).toString(), 0));
    }

    // This class provide a way to popup
    // Extending JPopupMenu seems to be the only way to get a custom popup
    // component since
    // the property 'alwaysVisible' has a package local visibility and is
    // mandatory if we want the
    // popup to be visible outside the JFrame. Also, using this class avoid
    // implementing all the stuff
    // for displaying the popup.
    private static class PopupPanel extends JPopupMenu {
        PopupPanel() {
        }

        public void show(Component invoker, int x, int y) {
            if (invoker != null) {
                y += invoker.getHeight(); // add the height of the invoker to
                // locate the popup at the bottom of
                // this invoker
            }
            super.show(invoker, x, y);
        }
    }

    // This class is responsible for managing the arrow button look behavior
    private static class LookManager implements MouseListener, PopupMenuListener {
        private static final Border RAISED_BORDER = new ThinBevelBorder(ThinBevelBorder.RAISED);
        private static final Border LOWERED_BORDER = new ThinBevelBorder(ThinBevelBorder.LOWERED);
        private PopupPanel popup;
        private JButton arrow;
        private boolean popupVisible = false;
        private boolean rollOver = false;

        LookManager(JButton arrowbutton, PopupPanel popuppanel) {
            arrow = arrowbutton;
            popup = popuppanel;
            popup.addPopupMenuListener(this);
            initButton(arrow);
            arrow.addMouseListener(this);
        }

        private void initButton(JButton button) {
            button.setText("");
            button.setBorder(RAISED_BORDER);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setVerticalAlignment(SwingConstants.CENTER);
        }

        public void mouseClicked(MouseEvent e) {
            updateAllLook();
        }

        public void mousePressed(MouseEvent e) {
            updateAllLook();
        }

        public void mouseReleased(MouseEvent e) {
            updateAllLook();
        }

        public void mouseEntered(MouseEvent e) {
            rollOver = true;
            updateAllLook();
        }

        public void mouseExited(MouseEvent e) {
            rollOver = false;
            updateAllLook();
        }

        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            popupVisible = true;
            arrow.setSelected(true);
            updateAllLook();
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            popupVisible = false;
            arrow.setSelected(false);
            updateAllLook();
        }

        public void popupMenuCanceled(PopupMenuEvent e) {
            popupVisible = false;
            arrow.setSelected(false);
            updateAllLook();
        }

        private void setLookNormal(JButton button) {
            button.setBorder(RAISED_BORDER);
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setVerticalAlignment(SwingConstants.CENTER);
            button.setBorderPainted(false);
        }

        private void setLookRaised(JButton button) {
            button.setBorder(RAISED_BORDER);
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setVerticalAlignment(SwingConstants.CENTER);
            button.setBorderPainted(true);
        }

        private void setLookLowered(JButton button) {
            button.setBorder(LOWERED_BORDER);
            button.setHorizontalAlignment(SwingConstants.RIGHT);
            button.setVerticalAlignment(SwingConstants.BOTTOM);
            button.setBorderPainted(true);
        }

        void updateAllLook() {
            updateLook(arrow);
        }

        private void updateBackgroundColor(int oldstate) {
        }

        void updateButtonUI() {
            rollOver = false;
        }

        private void updateLook(JButton button) {
            ButtonModel model = button.getModel();
            boolean enabled = model.isEnabled();
            if (!enabled) {
                setLookNormal(button);
                return;
            }

            boolean pressed = model.isPressed();
            boolean selected = model.isSelected();
            if (pressed || selected) {
                setLookLowered(button);
                return;
            }

            if ((button == arrow) && popupVisible) {
                setLookLowered(button);
                return;
            }

            if (rollOver) {
                setLookRaised(button);
                return;
            }
            if (arrow.getModel().isSelected()) {
                setLookRaised(button);
                return;
            }
            setLookNormal(button);
        }
    }
}
