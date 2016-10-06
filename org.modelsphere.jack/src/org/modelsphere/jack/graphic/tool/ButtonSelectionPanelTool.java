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
import java.awt.event.*;

import javax.swing.JComponent;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;

public abstract class ButtonSelectionPanelTool extends Tool {
    static final String SELECTED_INDEX_PROPERTY = "Selected"; // NOT LOCALIZABLE

    private Cursor[] cursors = null;
    private Image[] secondaryToolImages = null;
    private String[] secondaryTooltips = null;
    private int defaultPreferenceIndex = -1; // If -1, the selection will not be
    // save in PropertiesManager
    private ButtonSelectionPanelComponent component;

    public ButtonSelectionPanelTool(int userId, String text, String[] secondaryTooltips,
            Image image, Image[] secondaryimages) {
        this(userId, text, secondaryTooltips, image, secondaryimages, -1);
    }

    public ButtonSelectionPanelTool(int userId, String text, String[] secondaryTooltips,
            Image image, Image[] secondaryimages, int defaultPreferenceIndex) {
        super(userId, text, image);
        secondaryToolImages = secondaryimages;
        this.secondaryTooltips = secondaryTooltips;
        this.defaultPreferenceIndex = defaultPreferenceIndex;
        if (defaultPreferenceIndex == -1)
            auxiliaryIndex = 0;
        else {
            PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
            auxiliaryIndex = (preferences == null) ? 0 : preferences.getPropertyInteger(getClass(),
                    SELECTED_INDEX_PROPERTY, new Integer(defaultPreferenceIndex)).intValue();
        }

        // cursors = loadDefaultCursors();
    }

    protected Cursor[] loadDefaultCursors() {
        Cursor[] defaultcursors = new Cursor[secondaryToolImages.length];
        for (int i = 0; i < secondaryToolImages.length; i++) {
            defaultcursors[i] = AwtUtil.createCursor((Image) secondaryToolImages[i],
                    new Point(1, 1), "key" + i); // NOT
            // LOCALIZABLE
        }
        return defaultcursors;
    }

    public Cursor getCursor() {
        return cursors[auxiliaryIndex];
    }

    public abstract void mousePressed(MouseEvent e);

    protected JComponent createUIComponent() {
        component = new ButtonSelectionPanelComponent(secondaryToolImages, secondaryTooltips);
        if (defaultPreferenceIndex != -1) {
            component.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ButtonSelectionPanelComponent comp = (ButtonSelectionPanelComponent) e
                            .getSource();
                    PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
                    preferences.setProperty(ButtonSelectionPanelTool.this.getClass(),
                            SELECTED_INDEX_PROPERTY, comp.getAuxiliaryIndex());
                }
            });
        }
        component.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (component.isEnabled() && e.getClickCount() > 1) {
                    Rectangle rect = component.computeInnerArea();
                    if (rect.contains(e.getPoint())) {
                        fireMasterActivationListeners();
                    }
                }
            }
        });

        String toolTip = getText();
        component.setToolTipText(toolTip);
        component.setAuxiliaryIndex(auxiliaryIndex);
        return component;
    }

    // I override this one because Cursor doesn't work properly if I don't
    // recreate them.
    public Object clone() {
        ButtonSelectionPanelTool object = (ButtonSelectionPanelTool) super.clone();
        object.cursors = object.loadDefaultCursors();
        return object;
    }

}
