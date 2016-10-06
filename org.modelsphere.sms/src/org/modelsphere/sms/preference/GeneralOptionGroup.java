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
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.preference.OptionPanel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.preference.RecentFiles;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.features.startupwizard.StartupWizardModel;
import org.modelsphere.sms.international.LocaleMgr;

public class GeneralOptionGroup extends OptionGroup {
    @SuppressWarnings("serial")
    private static class GeneralOptionPanel extends OptionPanel implements ActionListener,
            ChangeListener {
        // private JLabel startingWizardLabel = new JLabel();
        private JCheckBox startingBox = new JCheckBox();
        private JLabel undoRedoLabel = new JLabel();
        private JSpinner undoRedoSpinner = new JSpinner();
        private JLabel recentFilesLabel = new JLabel();
        private JSpinner recentFilesSpinner = new JSpinner();
        private JButton defButton = new JButton(LocaleMgr.misc.getString("Default"));

        GeneralOptionPanel() {
            setLayout(new GridBagLayout());
            int row = 0;
            int gap = 18;

            //first row
            if (ScreenPerspective.isFullVersion()) {
            	startingBox.setText(LocaleMgr.screen.getString("ModelCreationWizardAppears"));
            	add(startingBox, new GridBagConstraints(0, row, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(gap, 3, 11, 5), 0, 0));
            	row++;
            	gap = 0;
            }
            
            //second row
            undoRedoLabel.setText(LocaleMgr.screen.getString("UndoRedoLevel") + " "
                    + LocaleMgr.misc.getString("needRestart"));
            add(undoRedoLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(gap, 9, 11, 11), 0,
                        0));
            add(undoRedoSpinner, new GridBagConstraints(1, row, 1, 1, 0.5, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(gap, 9, 11, 5), 20,
                    0));
            row++;
           
            recentFilesLabel.setText(LocaleMgr.screen.getString("recentFilesItems"));
            add(recentFilesLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 9, 11, 11), 0,
                    0));
            add(recentFilesSpinner, new GridBagConstraints(1, row, 1, 1, 0.5, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 9, 11, 5), 20,
                    0));
            row++;
            
            add(Box.createVerticalGlue(), new GridBagConstraints(0, row, 2, 1, 1.0, 1.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            row++; 
            
            add(defButton, new GridBagConstraints(1, row, 2, 1, 0.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(0, 0, 6, 5), 0, 0));

            AwtUtil.normalizeComponentDimension(new JComponent[] { undoRedoSpinner,
                    recentFilesSpinner });
            startingBox.addActionListener(this);
            recentFilesSpinner.addChangeListener(this);
            defButton.addActionListener(this);
        } // end GeneralOptionPanel()

        public void init() {
            PropertiesSet prefOptions = PropertiesManager.getPreferencePropertiesSet();
            PropertiesSet applOptions = PropertiesManager.APPLICATION_PROPERTIES_SET;

            // model creation wizard
            boolean modelCreationWizard = applOptions.getPropertyBoolean(StartupWizardModel.class,
                    StartupWizardModel.MODEL_CREATION_WIZARD_ENABLED_KEY,
                    StartupWizardModel.MODEL_CREATION_WIZARD_ENABLED_DEFAULT).booleanValue();
            startingBox.setSelected(modelCreationWizard);

            // undo levels
            int undoLevels = prefOptions.getPropertyInteger(Db.class,
                    Db.PROPERTY_CONMMAND_HISTORY_SIZE, Db.PROPERTY_CONMMAND_HISTORY_SIZE_DEFAULT)
                    .intValue();
            SpinnerNumberModel model = new SpinnerNumberModel(undoLevels,
                    Db.PROPERTY_CONMMAND_HISTORY_SIZE_MIN, Db.PROPERTY_CONMMAND_HISTORY_SIZE_MAX, 1);
            undoRedoSpinner.setModel(model);
            undoRedoSpinner.addChangeListener(this);

            // recent files
            int nbRecentFiles = prefOptions.getPropertyInteger(RecentFiles.class,
                    RecentFiles.PROPERTY_NB_RECENT_FILE,
                    RecentFiles.PROPERTY_NB_RECENT_FILE_DEFAULT).intValue();
            SpinnerNumberModel model2 = new SpinnerNumberModel(nbRecentFiles,
                    RecentFiles.PROPERTY_NB_RECENT_FILE_MIN,
                    RecentFiles.PROPERTY_NB_RECENT_FILE_MAX, 1);
            recentFilesSpinner.setModel(model2);
        } // end init()

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            if (source == startingBox) {
                boolean modelCreationWizard = startingBox.isSelected();
                fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET,
                        StartupWizardModel.class,
                        StartupWizardModel.MODEL_CREATION_WIZARD_ENABLED_KEY, Boolean
                                .valueOf(modelCreationWizard));
            }

            if (source == defButton) {
                startingBox.setSelected(StartupWizardModel.MODEL_CREATION_WIZARD_ENABLED_DEFAULT
                        .booleanValue());
                undoRedoSpinner.setValue(Db.PROPERTY_CONMMAND_HISTORY_SIZE_DEFAULT);
                recentFilesSpinner.setValue(RecentFiles.PROPERTY_NB_RECENT_FILE_DEFAULT);
            }
        }

        public void stateChanged(ChangeEvent e) {
            Object source = e.getSource();
            if (source == undoRedoSpinner) {
                updateUndoRedo();
            } else if (source == recentFilesSpinner) {
                PropertiesSet prefOptions = PropertiesManager.getPreferencePropertiesSet();
                fireOptionChanged(prefOptions, RecentFiles.class,
                        RecentFiles.PROPERTY_NB_RECENT_FILE, (Integer) recentFilesSpinner
                                .getValue());
            }
        }

        private void updateUndoRedo() {
            PropertiesSet prefOptions = PropertiesManager.getPreferencePropertiesSet();
            Integer value = (Integer) undoRedoSpinner.getValue();
            fireOptionChanged(prefOptions, Db.class, Db.PROPERTY_CONMMAND_HISTORY_SIZE, value);
            setRequireRestart();
        }
    };

    public GeneralOptionGroup() {
        super(LocaleMgr.screen.getString("General"));
    }

    protected OptionPanel createOptionPanel() {
        return new GeneralOptionPanel();
    }

    // Service Methods to access this options group values

}
