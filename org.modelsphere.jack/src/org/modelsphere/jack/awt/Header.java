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

import javax.swing.*;

@SuppressWarnings("serial")
public class Header extends JPanel {
    private JLabel label;

    private boolean selected = true;
    private Color activeForeground;
    private Color inactiveForeground;
    private Gradient activeGradient;
    private Gradient inactiveGradient;

    public Header(String text) {
        super(new BorderLayout());
        setSelected(false);
        label.setText(text);
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }

    public void updateUI() {
        super.updateUI();

        activeForeground = UIManager.getColor("InternalFrame.activeTitleForeground");
        inactiveForeground = UIManager.getColor("InternalFrame.inactiveTitleForeground");
        if (inactiveForeground == null) {
            inactiveForeground = activeForeground;
        }

        activeGradient = new Gradient(UIManager.getColor("activeCaption"), UIManager
                .getColor("InternalFrame.activeTitleGradient")); //NOT LOCALIZABLE
        inactiveGradient = new Gradient(UIManager.getColor("inactiveCaption"), UIManager
                .getColor("InternalFrame.inactiveTitleGradient")); //NOT LOCALIZABLE

        label = new JLabel();
        label.setOpaque(false);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setBorder(BorderFactory.createEmptyBorder(1, 3, 1, 1));
        add(label);
        Font font = UIManager.getFont("InternalFrame.titleFont");
        if (font != null) {
            label.setFont(font);
        }
    }

    public void setText(String s) {
        if (s == null)
            label.setText(null);
        else
            label.setText(s); // NOT LOCALIZABLE
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Insets insets = getInsets();
        int width = getWidth();
        int height = getHeight();
        Gradient gradient = selected ? activeGradient : inactiveGradient;

        if (gradient != null && g instanceof Graphics2D) {
            gradient.paint(this, g, insets.left, insets.top, width - insets.left - insets.right,
                    height - insets.top - insets.bottom);
        } else {
            Color old = g.getColor();
            g.setColor(getBackground());
            g.fillRect(insets.left, insets.top, width - insets.left - insets.right, height
                    - insets.top - insets.bottom);
            g.setColor(old);
        }

    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        if (this.selected == selected)
            return;
        this.selected = selected;
        if (selected) {
            if (activeForeground != null) {
                label.setForeground(activeForeground);
            }
        } else {
            if (inactiveForeground != null) {
                label.setForeground(inactiveForeground);
            }
        }
        repaint();
    }
}
