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
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.preference.OptionPanel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.international.LocaleMgr;

public class DisplayWindowsOptionGroup extends OptionGroup {
    private static final String DIAGRAM_FRAME_DEFAULT_SIZE = "DiagramFrameDefaultSize"; // NOT
    // LOCALIZABLE
    private static final String PROPERTY_FRAME_DEFAULT_SIZE = "PropertyFrameDefaultSize"; // NOT

    // LOCALIZABLE

    private static class DisplayWindowsOptionPanel extends OptionPanel implements ActionListener {
        private JLabel diagSizeLabel = new JLabel();
        private JComboBox diagSizeCombo = new JComboBox(MainFrame.windowsSizePossibleValues);
        private JLabel propertySizeLabel = new JLabel();
        private JComboBox propertySizeCombo = new JComboBox(MainFrame.windowsSizePossibleValues);
        private JButton defButton = new JButton(LocaleMgr.misc.getString("Default"));

        DisplayWindowsOptionPanel() {
            setLayout(new GridBagLayout());

            diagSizeLabel.setText(LocaleMgr.misc.getString("DiagramPrefSize"));
            propertySizeLabel.setText(LocaleMgr.misc.getString("PropertyPrefSize"));

            add(diagSizeLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(18, 9, 6, 11), 0,
                    0));
            add(diagSizeCombo,
                    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                            GridBagConstraints.NONE, new Insets(18, 0, 6, 5), 0, 0));

            add(propertySizeLabel,
                    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                            GridBagConstraints.NONE, new Insets(6, 9, 6, 11), 0, 0));
            add(propertySizeCombo, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 0, 6, 5), 0, 0));

            add(Box.createVerticalGlue(), new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            add(defButton, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(0, 0, 6, 5), 0, 0));

            diagSizeCombo.addActionListener(this);
            propertySizeCombo.addActionListener(this);
            defButton.addActionListener(this);

        }

        public void init() {
            PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
            diagSizeCombo.setSelectedIndex((prefs.getPropertyInteger(
                    DisplayWindowsOptionGroup.class, DIAGRAM_FRAME_DEFAULT_SIZE, new Integer(
                            MainFrame.WINDOWS_DESKTOPSIZE)).intValue()));
            propertySizeCombo.setSelectedIndex((prefs.getPropertyInteger(
                    DisplayWindowsOptionGroup.class, PROPERTY_FRAME_DEFAULT_SIZE, new Integer(
                            MainFrame.WINDOWS_3_4_DESKTOPSIZE)).intValue()));
        }

        public void actionPerformed(ActionEvent e) {
            PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
            Object source = e.getSource();
            if (source == diagSizeCombo)
                fireOptionChanged(prefs, DisplayWindowsOptionGroup.class,
                        DIAGRAM_FRAME_DEFAULT_SIZE, new Integer(diagSizeCombo.getSelectedIndex()));
            else if (source == propertySizeCombo)
                fireOptionChanged(prefs, DisplayWindowsOptionGroup.class,
                        PROPERTY_FRAME_DEFAULT_SIZE, new Integer(propertySizeCombo
                                .getSelectedIndex()));
            else if (source == defButton) {
                diagSizeCombo.setSelectedIndex(MainFrame.WINDOWS_3_4_DESKTOPSIZE);
                propertySizeCombo.setSelectedIndex(MainFrame.WINDOWS_3_4_DESKTOPSIZE);
            }
        }

    };

    public DisplayWindowsOptionGroup() {
        super(LocaleMgr.screen.getString("Desktop"));
    }

    protected OptionPanel createOptionPanel() {
        return new DisplayWindowsOptionPanel();
    }

    // Service Methods to access this options group values

    public static int getDiagramFrameDefaultSize() {
        PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
        return prefs.getPropertyInteger(DisplayWindowsOptionGroup.class,
                DIAGRAM_FRAME_DEFAULT_SIZE, new Integer(MainFrame.WINDOWS_DESKTOPSIZE)).intValue();
    }

    public static int getPropertyFrameDefaultSize() {
        PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
        return prefs.getPropertyInteger(DisplayWindowsOptionGroup.class,
                PROPERTY_FRAME_DEFAULT_SIZE, new Integer(MainFrame.WINDOWS_3_4_DESKTOPSIZE))
                .intValue();
    }

}
