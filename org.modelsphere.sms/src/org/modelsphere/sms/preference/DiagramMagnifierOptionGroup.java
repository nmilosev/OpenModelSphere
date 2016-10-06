/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.preference;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.MagnifierView;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.preference.OptionPanel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.sms.international.LocaleMgr;

public class DiagramMagnifierOptionGroup extends OptionGroup {

    private static class DiagramMagnifierOptionPanel extends OptionPanel implements ActionListener,
            ChangeListener {
        private JPanel diagOptionPanel = new JPanel();
        private JButton defButton = new JButton(LocaleMgr.misc.getString("Default"));
        private JLabel magnifierZoomLabel = new JLabel(LocaleMgr.screen.getString("magnifierZoom"));
        private JSlider magnifierZoom = new JSlider(JSlider.HORIZONTAL, 60, 300, 100);
        private JLabel defaultZoomLabel = new JLabel(LocaleMgr.screen.getString("defaultZoom"));
        private JSlider defaultZoom = new JSlider(JSlider.HORIZONTAL, 50, 150, 100);

        DiagramMagnifierOptionPanel() {
            setLayout(new GridBagLayout());
            JPanel magnifierZoomPanel = new JPanel(new GridBagLayout());

            JPanel defaultZoomPanel = new JPanel(new GridBagLayout());

            magnifierZoomPanel.add(magnifierZoomLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 6, 5), 0, 0));
            magnifierZoomPanel.add(magnifierZoom, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 24, 6,
                            5), 0, 0));

            defaultZoomPanel.add(defaultZoomLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 6, 5), 0, 0));
            defaultZoomPanel.add(defaultZoom, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 24, 6,
                            5), 0, 0));

            add(magnifierZoomPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(18, 9,
                            11, 5), 0, 0));
            add(defaultZoomPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(12, 9,
                            6, 5), 0, 0));
            add(Box.createVerticalGlue(), new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
                            0, 0), 0, 0));
            add(defButton, new GridBagConstraints(0, 3, 4, 1, 1.0, 1.0,
                    GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 6, 5),
                    0, 0));

            defButton.addActionListener(this);
            magnifierZoom.addChangeListener(this);
            magnifierZoom.setMajorTickSpacing(20);
            magnifierZoom.setMinorTickSpacing(10);
            magnifierZoom.setPaintLabels(true);
            magnifierZoom.setPaintTicks(true);
            magnifierZoom.setPaintTrack(true);
            magnifierZoom.setSnapToTicks(true);

            defaultZoom.addChangeListener(this);
            defaultZoom.setMajorTickSpacing(10);
            defaultZoom.setMinorTickSpacing(5);
            defaultZoom.setPaintLabels(true);
            defaultZoom.setPaintTicks(true);
            defaultZoom.setPaintTrack(true);
            defaultZoom.setSnapToTicks(false);
        }

        public void init() {
            PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();

            int zoom = (int) (prefs.getPropertyFloat(MagnifierView.class,
                    MagnifierView.ZOOM_FACTOR_PROPERTY,
                    new Float(MagnifierView.ZOOM_FACTOR_PROPERTY_DEFAULT)).floatValue() * 100);
            magnifierZoom.setValue(zoom);

            zoom = (int) (prefs.getPropertyFloat(DiagramView.class,
                    DiagramView.ZOOM_FACTOR_PROPERTY,
                    new Float(DiagramView.ZOOM_FACTOR_PROPERTY_DEFAULT)).floatValue() * 100);
            defaultZoom.setValue(zoom);
        }

        public void stateChanged(ChangeEvent e) {
            PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
            Object source = e.getSource();
            if (source == magnifierZoom && !magnifierZoom.getValueIsAdjusting())
                fireOptionChanged(prefs, MagnifierView.class, MagnifierView.ZOOM_FACTOR_PROPERTY,
                        new Float((float) (((float) magnifierZoom.getValue()) / 100)));
            if (source == defaultZoom && !defaultZoom.getValueIsAdjusting())
                fireOptionChanged(prefs, DiagramView.class, DiagramView.ZOOM_FACTOR_PROPERTY,
                        new Float((float) (((float) defaultZoom.getValue()) / 100)));
        }

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == defButton) {
                PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
                magnifierZoom.setValue((int) (new Float(
                        MagnifierView.ZOOM_FACTOR_PROPERTY_DEFAULT * 100)).floatValue());
                fireOptionChanged(prefs, MagnifierView.class, MagnifierView.ZOOM_FACTOR_PROPERTY,
                        new Float(MagnifierView.ZOOM_FACTOR_PROPERTY_DEFAULT));
                defaultZoom.setValue((int) (new Float(
                        DiagramView.ZOOM_FACTOR_PROPERTY_DEFAULT * 100)).floatValue());
                fireOptionChanged(prefs, DiagramView.class, DiagramView.ZOOM_FACTOR_PROPERTY,
                        new Float(DiagramView.ZOOM_FACTOR_PROPERTY_DEFAULT));
            }
        }

    };

    public DiagramMagnifierOptionGroup() {
        super(LocaleMgr.screen.getString("ZoomMagnifier"));
    }

    protected OptionPanel createOptionPanel() {
        return new DiagramMagnifierOptionPanel();
    }

}
