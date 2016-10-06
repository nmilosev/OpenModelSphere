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

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorChooserComponentFactory;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.text.MessageFormat;

public class JackColorChooser implements ActionListener {
    private static final String kRessetTransparency0 = LocaleMgr.message
            .getString("RessetTransparency0");
    private boolean cancel = true;
    private TransparencyPanel transparencyPanel;
    private Color initialColor;
    private JColorChooser chooser;
    private JDialog dialog;
    private DefaultColorSelectionModel colorSelectionModel = null;
    private static int alpha = 255;
    private static JackColorChooser singleton = null;
    private static boolean questionAsked = false;

    private static class TransparencyPanel extends AbstractColorChooserPanel {
        private JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        private JLabel transparentL = new JLabel(LocaleMgr.screen.getString("Transparent"));
        private JLabel opaqueL = new JLabel(LocaleMgr.screen.getString("Opaque"));
        private ChangeListener listener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (!slider.getValueIsAdjusting()) {
                    Color selectedColor = getColorSelectionModel().getSelectedColor();
                    alpha = (slider.getValue() * 255) / 100;
                    getColorSelectionModel().setSelectedColor(
                            new Color(selectedColor.getRed(), selectedColor.getGreen(),
                                    selectedColor.getBlue(), alpha));
                }
            }
        };

        TransparencyPanel() {
        }

        public void updateChooser() {
            Color selectedColor = this.getColorFromModel();
            if (selectedColor != null)
                slider.setValue(selectedColor.getAlpha() * 100 / 255);
        }

        protected void buildChooser() {
            setLayout(new GridBagLayout());
            slider.setMajorTickSpacing(20);
            slider.setMinorTickSpacing(2);
            slider.setPaintLabels(true);
            slider.setPaintTicks(true);
            slider.setPaintTrack(true);
            // If SnapToTicks == false, slider does not behave properly on mouse
            // click or arrow keys
            slider.setSnapToTicks(true);
            Color selectedColor = getColorSelectionModel().getSelectedColor();
            slider.setValue(selectedColor == null ? 100 : (selectedColor.getAlpha() * 100 / 255));
            removeAll();
            add(transparentL, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(12, 12, 12, 0), 0, 0));
            add(slider, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH, new Insets(12, 5, 12, 0), 100, 0));
            add(opaqueL, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(12, 5, 12, 12), 0, 0));

            slider.addChangeListener(listener);
        }

        // Mnemonic will work for 1.4+
        public int getMnemonic() {
            return LocaleMgr.screen.getMnemonic("Opacity");
        }

        public int getDisplayedMnemonicIndex() {
            return LocaleMgr.screen.getString("Opacity").indexOf(getMnemonic());
        }

        public String getDisplayName() {
            return LocaleMgr.screen.getString("Opacity");
        }

        public Icon getSmallDisplayIcon() {
            return null;
        }

        public Icon getLargeDisplayIcon() {
            return null;
        }
    };

    private class ChooserColorSelectionModel extends DefaultColorSelectionModel {
        ChooserColorSelectionModel(Color c) {
            super(c);
        }

        public void setSelectedColor(Color color) {
            if (color != null) {
                Color old = this.getSelectedColor();
                if (!questionAsked
                        && color != null
                        && alpha < 10
                        && old != null
                        && (old.getRed() != color.getRed() || old.getGreen() != color.getGreen() || old
                                .getBlue() != color.getBlue())) {
                    String message = MessageFormat.format(JackColorChooser.kRessetTransparency0,
                            new Object[] { new Integer(alpha) });
                    int result = JOptionPane.showConfirmDialog(JackColorChooser.this.dialog,
                            message, ApplicationContext.getApplicationName(),
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION)
                        alpha = 255;
                    questionAsked = true;
                }
                super.setSelectedColor(new Color(color.getRed(), color.getGreen(), color.getBlue(),
                        alpha));
            } else
                super.setSelectedColor(color);
        }
    }

    private JackColorChooser() {
        colorSelectionModel = new ChooserColorSelectionModel(Color.white);
        chooser = new JColorChooser(colorSelectionModel);
    }

    private void init(Component parent, String title, Color initialColor, boolean hasalpha) {
        alpha = initialColor == null ? 255 : initialColor.getAlpha();
        this.initialColor = initialColor == null ? null : new Color(initialColor.getRGB());
        colorSelectionModel.setSelectedColor(this.initialColor);
        dialog = JColorChooser.createDialog(parent, title, true, chooser, this, this);

        if (hasalpha) {
            transparencyPanel = new TransparencyPanel();
            // BUG 1.4 (maybe 1.3): We must recreate the default panels
            // otherwise the existed one will not work properly when adding
            // a custom panel.
            AbstractColorChooserPanel[] panels = ColorChooserComponentFactory
                    .getDefaultChooserPanels();
            AbstractColorChooserPanel[] newpanels = new AbstractColorChooserPanel[panels.length + 1];
            System.arraycopy(panels, 0, newpanels, 0, panels.length);
            newpanels[panels.length] = transparencyPanel;
            chooser.setChooserPanels(newpanels);
        }

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OK")) { // NOT LOCALIZABLE - Hard coded
            // in swings as of 1.4.0!
            cancel = false;
        }
    }

    private Color showDialog_Impl(Component parent, String title, Color initialColor, boolean alpha) {
        init(parent, title, initialColor, alpha);
        dialog.setVisible(true);
        Color selColor = null;
        if (cancel)
            selColor = initialColor;
        else
            selColor = chooser.getColor();
        return selColor;
    }

    public static Color showDialog(Component parent, String title, Color initialColor, boolean alpha) {
        if (singleton == null)
            singleton = new JackColorChooser();
        Color newcolor = singleton.showDialog_Impl(parent, title, initialColor, alpha);
        return newcolor;
    }

}
