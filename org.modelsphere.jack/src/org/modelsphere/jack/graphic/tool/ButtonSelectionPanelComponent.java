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

package org.modelsphere.jack.graphic.tool;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.modelsphere.jack.awt.PopupButton;

/**
 * This is the default component for a ButtonSelectionPanel
 * 
 * This component is composed of 2 components: the selectionButton and the arrowButton. The
 * arrowButton is a JButton that is used to make the popup visible or hidden on action performed The
 * selectionButton is used to display the selected value. It represents the active tool for this
 * ToolComponent
 * 
 */

class ButtonSelectionPanelComponent extends PopupButton implements ActionListener, ToolComponent {
    // Contains one button for each possible values.
    private DefaultButton[] buttons;
    private String[] tooltips;

    // The index of the selected value
    private int selected = -1;

    // Support of ToolComponent
    private int state = ToolComponent.STATE_NORMAL;

    // Parameter values must contains objects of class Icon or Image.
    public ButtonSelectionPanelComponent(Object[] values, String[] tooltips) {
        this.tooltips = tooltips;
        setValues(values);
    }

    private void setSelectedIndex(int idx) {
        if (idx > -1) {
            selected = idx;
            setIcon(buttons[idx].getIcon());
        }
    }

    public void setState(int newState) {
        if (this.state == newState)
            return;
        state = newState;
        setSelected((state & (STATE_SELECTED | STATE_MASTER)) != 0);
        repaint();
    }

    public int getState() {
        return state;
    }

    public int getAuxiliaryIndex() {
        return selected;
    }

    public void setAuxiliaryIndex(int auxiliaryIndex) {
        setSelectedIndex(auxiliaryIndex);
    }

    // Triggered if a button from the popup get triggerred. If so, update the
    // selection button and hide the popup
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == source) {
                setSelectedIndex(i);
                fireActionPerformed(ActionEvent.ACTION_PERFORMED, "");
                // This line is to avoid double click to activate a JMenu after
                // hiding the popup.
                MenuSelectionManager.defaultManager().clearSelectedPath();
                return;
            }
        }
    }

    protected PopupPanel createPopupPanel() {
        PopupPanel popup = super.createPopupPanel();
        JToolBar toolbar = new JToolBar();
        toolbar.setBorderPainted(false);
        toolbar.setFloatable(false);
        Object[] values = getValues();
        buttons = new DefaultButton[values.length];
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Icon)
                buttons[i] = new DefaultButton((Icon) values[i]);
            else if (values[i] instanceof Image)
                buttons[i] = new DefaultButton(new ImageIcon((Image) values[i]));
            else {
                org.modelsphere.jack.debug.Debug
                        .assert2(false,
                                "ButtonSelectionPanelComponent:  Invalid object type for possible values (Image or Icon only)."); // NOT
                // LOCALIZABLE
                // --
                // Debug
                // only
            }
            if (tooltips != null && buttons[i] != null)
                buttons[i].setToolTipText(tooltips[i]);
        }

        popup.removeAll();
        for (int i = 0; i < buttons.length; i++) {
            toolbar.add(buttons[i]);
            buttons[i].addActionListener(this);
        }
        popup.add(toolbar);
        setSelectedIndex(0);
        return popup;
    }

    // A custom formatted JButton for displaying alternate tools in the popup
    // component
    private static class DefaultButton extends JButton {
        DefaultButton(Icon icon) {
            super(icon);
            this.setFocusable(false);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if ((state & ToolComponent.STATE_MASTER) != 0) {
            Tool.paintMasterToolEffect((Graphics2D) g, getRenderer());
        }

    }

}
