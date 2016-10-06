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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.AbstractColorDomainAction;
import org.modelsphere.jack.international.LocaleMgr;

public class PopupColorPanel extends PopupButton {
    private static final String kRed = LocaleMgr.misc.getString("Red");
    private static final String kGreen = LocaleMgr.misc.getString("Green");
    private static final String kBlue = LocaleMgr.misc.getString("Blue");
    private static final String kOpacity = LocaleMgr.misc.getString("Opacity");

    public static final int COL_COUNT = 8;

    private static final int COLOR_ICON_WIDTH_HEIGHT = 10;

    static class ColorButton extends JButton {
        ColorButton(Color c) {
            super(new ColorIcon(c, COLOR_ICON_WIDTH_HEIGHT, COLOR_ICON_WIDTH_HEIGHT));
            setFocusable(false);
        }
    }

    // Contains 2 icons from top to bottom - First, the action's icon, and an
    // icon representing the selected color
    private class ColorMultiIcon implements Icon {
        private static final int Y_GAP = 1; // space between the 2 icons
        private static final int COLOR_HEIGHT = 3; // space between the 2 icons
        private ImageIcon icon;
        private ImageIcon disabledIcon;

        ColorMultiIcon(ImageIcon icon) {
            this.icon = icon;
            if (icon != null) {
                disabledIcon = new ImageIcon(GrayFilter.createDisabledImage(icon.getImage()));
            }
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            boolean enabled = c.isEnabled();
            int iconwidth = (icon != null) ? icon.getIconWidth() : 0;
            int iconheight = (icon != null) ? icon.getIconHeight() : 8;

            // paint the icon
            Icon paintIcon = enabled ? icon : disabledIcon;
            if (paintIcon != null)
                paintIcon.paintIcon(c, g, x, y);

            // backup current color
            Color oldColor = g.getColor();

            Color selectedColor = getSelectedColor();
            // paint the color zone
            if (selectedColor != null) {
                g.setColor(selectedColor);
                g.fillRect(x, y + iconheight + Y_GAP, iconwidth, COLOR_HEIGHT);
            }

            // restore previous color
            g.setColor(oldColor);
        }

        public int getIconWidth() {
            int width = 0;
            if (icon != null)
                width = icon.getIconWidth();
            width = Math.max(width, 8);
            return width;
        }

        public int getIconHeight() {
            int height = 0;
            if (icon != null)
                height += icon.getIconHeight();
            height += Y_GAP + COLOR_HEIGHT;
            return height;
        }
    }

    private boolean uiSelectionVisible = true;
    private PopupPanel popup;
    private JToolBar toolbar;

    private Icon primaryIcon;

    // This field is used to control events when the action properties are
    // changed.
    protected boolean preventEvent = false;

    // The index of the selected active value
    private int selected;

    private ActionListener actionListener;

    public PopupColorPanel() {
        this(null);
    }

    public PopupColorPanel(AbstractColorDomainAction action) {
        super(action);
    }

    private ActionListener getActionListener() {
        if (actionListener == null) {
            actionListener = new ActionListener() {

                // Triggered if a button from the popup get triggerred. If so,
                // update the
                // selection button and hide the popup
                @Override
                public void actionPerformed(ActionEvent e) {
                    Object source = e.getSource();
                    Component[] comps = toolbar.getComponents();
                    for (int i = 0; i < comps.length; i++) {
                        if (comps[i] == source) {
                            hidePopup();
                            setSelectedIndex(i);
                            fireAction(i);
                            // fireActionPerformed(new ActionEvent(this,
                            // ActionEvent.ACTION_PERFORMED, "", 0));
                            // This line is to avoid double click to activate a
                            // JMenu after
                            // hiding the popup.
                            MenuSelectionManager.defaultManager().clearSelectedPath();
                            return;
                        }
                    }
                }
            };
        }
        return actionListener;
    }

    public void setIcon(Icon icon) {
        this.primaryIcon = new ColorMultiIcon((ImageIcon) icon);
        super.setIcon(primaryIcon);
    }

    private void initValues() {
        hidePopup();
        addComponents(popup, getValues());
        setSelectedIndex(0);
    }

    // Add Components to the PopupPanel (default layout manager is
    // GridBagLayout)
    private void addComponents(JComponent popupPanel, Object[] values) {
        int x = 0;
        int y = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Color) {
                Color c = (Color) values[i];
                JButton button = new ColorButton(c);
                String tooltips = "<html>"; // NOT LOCALIZABLE, html tag
                tooltips += kRed + " " + c.getRed() + "<br>"; // NOT
                // LOCALIZABLE,
                // html tag
                tooltips += kGreen + " " + c.getGreen() + "<br>"; // NOT
                // LOCALIZABLE,
                // html tag
                tooltips += kBlue + " " + c.getBlue();
                if (c.getAlpha() < 255) {
                    int opacity = (c.getAlpha() * 100 / 255);
                    tooltips += "<br>" + kOpacity + " " + opacity + "%"; // NOT
                    // LOCALIZABLE,
                    // html
                    // tag
                }
                tooltips += "</html>"; // NOT LOCALIZABLE, html tag
                button.setToolTipText(tooltips);
                toolbar.add(button, new GridBagConstraints(x, y, 1, 1, 0, 0,
                        GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, x == 0 ? 1
                                : 0, 0, x == COL_COUNT - 1 ? 1 : 0), 0, 0));
                x++;
                if (x > COL_COUNT - 1) {
                    x = 0;
                    y++;
                }
                button.addActionListener(getActionListener());
            } else if (values[i] instanceof String) {
                JButton button = new JButton((String) values[i]);
                AwtUtil.formatJButton(button, (String) values[i]);
                button.setText((String) values[i]);
                if (x > 0)
                    y++;
                x = 0;
                toolbar.add(button, new GridBagConstraints(x, y, COL_COUNT, 1, 0, 0,
                        GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 1, 0, 1),
                        0, 0));
                y++;
                button.addActionListener(getActionListener());
            }
        }
    }

    @Override
    protected PopupPanel createPopupPanel() {
        if (popup == null) {
            popup = super.createPopupPanel();
            toolbar = new JToolBar();
            toolbar.setLayout(new GridBagLayout());
            toolbar.setFloatable(false);
            toolbar.setBorderPainted(false);
            popup.setLayout(new BorderLayout());
            popup.add(toolbar);
        }
        initValues();
        return popup;
    }

    private void fireAction(int selected) {
        if (!preventEvent) {
            AbstractColorDomainAction action = (AbstractColorDomainAction) getAction();
            action.setSelectedPrivate_(selected, this);
        }
    }

    protected void removeAllItems() {
        hidePopup();

        if (popup != null) {
            Component[] comps = popup.getComponents();
            for (int i = 0; i < comps.length; i++) {
                if (comps[i] instanceof AbstractButton) {
                    ((AbstractButton) comps[i]).removeActionListener(getActionListener());
                }
            }
            toolbar.removeAll();
        }

        setValues(new Object[] {});
    }

    protected final void updateItems(Object[] values) {
        int oldidx = this.getSelectedIndex();
        setSelectedIndex(-1);
        removeAllItems();

        setValues(values);
        initValues();
        if (oldidx < getValues().length)
            setSelectedIndex(oldidx);
    }

    public int getSelectedIndex() {
        return selected;
    }

    protected final PropertyChangeListener createActionPropertyChangeListener(Action a) {
        return new PropertyChangeListener() {
            public final void propertyChange(PropertyChangeEvent e) {
                String propertyName = e.getPropertyName();
                if (propertyName.equals(AbstractColorDomainAction.VALUES)) {
                    preventEvent = true;
                    updateItems((Object[]) e.getNewValue());
                    preventEvent = false;
                } else if (propertyName.equals(AbstractColorDomainAction.SELECTED_VALUE)) {
                    if (uiSelectionVisible) {
                        int newsel = e.getNewValue() == null ? -1 : ((Integer) e.getNewValue())
                                .intValue();
                        preventEvent = true;
                        setSelectedIndex(newsel);
                        preventEvent = false;
                    }
                } else if (propertyName.equals("enabled")) // NOT LOCALIZABLE,
                    // property key
                    setEnabled(((Boolean) e.getNewValue()).booleanValue());
                else if (propertyName.equals(Action.SHORT_DESCRIPTION))
                    setToolTipText(e.getNewValue() == null ? "" : (String) e.getNewValue());
                else if (propertyName.equals(AbstractApplicationAction.VISIBLE)
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

    protected final void configurePropertiesFromAction(Action a) {
        setToolTipText(a != null ? (String) a.getValue(Action.SHORT_DESCRIPTION) : null);
        setIcon(a != null ? (Icon) ((AbstractColorDomainAction) a).getValue(Action.SMALL_ICON)
                : null);
        setEnabled(a != null ? a.isEnabled() : false);
        updateItems(a != null ? ((AbstractColorDomainAction) a).getDomainValues() : null);
        uiSelectionVisible = (a != null) ? ((AbstractColorDomainAction) a).isUISelectionVisible()
                : true;
        // if (uiSelectionVisible)
        setSelectedIndex(a != null ? ((AbstractColorDomainAction) a).getSelectedIndex() : -1);
        ActionHelpPropertySupport.registerHelpSupport(this, (AbstractApplicationAction) a);
        updateVisible();
    }

    protected void setSelectedIndex(int idx) {
        if (idx == selected)
            return;
        selected = idx;
        repaint();
    }

    protected Color getSelectedColor() {
        Object[] values = getValues();
        if (selected > -1 && values != null && values.length > selected
                && (values[selected] instanceof Color)) {
            return (Color) values[selected];
        }
        return null;
    }

};
