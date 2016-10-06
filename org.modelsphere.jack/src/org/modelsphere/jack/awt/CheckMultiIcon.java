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
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.*;

/**
 * This class provide a way to merge 2 icons in one. The first icon is a checkbox icon (specific UI)
 * and the second is a user icon.
 */

public final class CheckMultiIcon extends ImageIcon {
    private static final int USER_ICON_GAP = 3; // space between the 2 icons
    private JCheckBox checkbox = new EmptyCheckBox();
    private Icon checkIcon = UIManager.getIcon("CheckBox.icon"); // NOT
    // LOCALIZABLE
    // -
    // property
    private Icon userIcon;
    private Icon userIconDisabled;

    public CheckMultiIcon(boolean selected, boolean enabled) {
        checkbox.setEnabled(enabled);
        checkbox.setSelected(selected);
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        int checky = y;
        int usery = y;
        int checkwidth = (checkIcon != null) ? checkIcon.getIconWidth() : 0;
        int checkheight = (checkIcon != null) ? checkIcon.getIconHeight() : 0;
        int userheight = (userIcon != null) ? userIcon.getIconHeight() : 0;

        // center the icons
        if (userheight > (checkheight + 1))
            checky += (int) ((userheight - checkheight) / 2);
        else if ((userheight + 1) < checkheight)
            usery += (int) ((checkheight - userheight) / 2);

        // paint the check icon
        if (checkIcon != null)
            checkIcon.paintIcon(checkbox, g, x, checky); // checkbox is needed
        // to provide
        // selected property

        // paint the user icon
        if (userIcon != null) {
            x = (checkwidth > 0) ? checkwidth + USER_ICON_GAP : x;
            // paint the disabled or enabled icon
            if (!c.isEnabled() && (userIconDisabled != null))
                userIconDisabled.paintIcon(c, g, x, usery);
            else
                userIcon.paintIcon(c, g, x, usery);
        }
    }

    public void setUserIcon(Icon icon) {
        userIcon = icon;
        if (userIcon != null && userIcon instanceof ImageIcon) {
            userIconDisabled = new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) userIcon)
                    .getImage()));
        }
    }

    public int getIconWidth() {
        int width = 0;
        if (checkIcon != null)
            width += checkIcon.getIconWidth();
        if (userIcon != null)
            width += userIcon.getIconWidth() + USER_ICON_GAP;
        return width;
    }

    public int getIconHeight() {
        int height = 0;
        if (checkIcon != null)
            height = checkIcon.getIconHeight();
        if (userIcon != null)
            height = Math.max(height, userIcon.getIconHeight());
        return height;
    }
}

// This class is just a lure to get the checkbox icon (specific UI) painted.
// (The UI Checkbox icon expect the component of the paintIcon method to be a
// JCheckBox (Use cast). Since
// the renderer is a JLabel, we can't use the component received in the
// paintIcon method of class MultiIcon
// to call the paintIcon method of the UI CheckBox. So we use this class
// instead.)

final class EmptyCheckBox extends JCheckBox {
    // Methods overrided for performance reasons
    public void paint(Graphics g) {
    }

    public void repaint(long tm, int x, int y, int width, int height) {
    }

    public void repaint(Rectangle r) {
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    }

    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
    }

    public void firePropertyChange(String propertyName, char oldValue, char newValue) {
    }

    public void firePropertyChange(String propertyName, short oldValue, short newValue) {
    }

    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
    }

    public void firePropertyChange(String propertyName, long oldValue, long newValue) {
    }

    public void firePropertyChange(String propertyName, float oldValue, float newValue) {
    }

    public void firePropertyChange(String propertyName, double oldValue, double newValue) {
    }

    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    }

    protected void fireItemStateChanged(ItemEvent event) {
    }

    protected void fireActionPerformed(ActionEvent event) {
    }

    protected void fireStateChanged() {
    }
}
