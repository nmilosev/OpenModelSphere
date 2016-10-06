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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/
package org.modelsphere.sms.features.startupwizard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.modelsphere.jack.awt.ColorIcon;
import org.modelsphere.jack.awt.ThinBevelBorder;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.gui.wizard2.AbstractPage;
import org.modelsphere.jack.gui.wizard2.SectionHeader;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.FocusManager;
import org.modelsphere.sms.features.international.LocaleMgr;

@SuppressWarnings("serial")
class ProjectModelPage extends AbstractPage {
    private static final Image imgDM = LocaleMgr.screen.getImage("DataModel");
    private static final Image imgBeM = LocaleMgr.screen.getImage("BeModel");
    private static final Image imgCM = LocaleMgr.screen.getImage("ClassModel");
    private static final String kData_Model = LocaleMgr.screen.getString("Data_Model");
    private static final String kBusiness_Process_Model = LocaleMgr.screen
            .getString("Business_Process_Model");
    private static final String kUML_Model = LocaleMgr.screen.getString("UML_Model");
    private static final String kInto_the_Current_Project = LocaleMgr.screen
            .getString("Into_the_Current_Project");
    private static final String kInto_a_New_Project = LocaleMgr.screen
            .getString("Into_a_New_Project");
    private static final String kSelectAProject = LocaleMgr.screen.getString("SelectProject");
    private static final String kSelectAModel = LocaleMgr.screen.getString("SelectModel");
    private static final String kTitle = LocaleMgr.screen.getString("SelectProjectModel");

    private JLabel previewLabel = new JLabel();

    private ImageIcon iconDM;
    private ImageIcon iconBeM;
    private ImageIcon iconCM;

    private JRadioButton DMButton = new JRadioButton(kData_Model);
    private JRadioButton BPMButton = new JRadioButton(kBusiness_Process_Model);
    private JRadioButton UMLButton = new JRadioButton(kUML_Model);
    private JRadioButton currentProjectButton = new JRadioButton(kInto_the_Current_Project);
    private JRadioButton newProjectButton = new JRadioButton(kInto_a_New_Project);
    private JTextField newProjectField = new JTextField();

    private StartupWizardModel model = null;

    ProjectModelPage(StartupWizardModel model) {
        super(new GridBagLayout());
        this.model = model;
        init();
    }

    private void init() {
        ButtonGroup modelTypeGroup = new ButtonGroup();
        ButtonGroup projectGroup = new ButtonGroup();

        newProjectField.setText(model.getNewProjectName());

        SectionHeader projectHeader = new SectionHeader(kSelectAProject);
        SectionHeader newModelHeader = new SectionHeader(kSelectAModel);
        SectionHeader previewHeader = new SectionHeader(StartupWizard.kPreview);

        previewLabel.setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));

        iconDM = new ImageIcon(imgDM);
        iconBeM = new ImageIcon(imgBeM);
        iconCM = new ImageIcon(imgCM);

        modelTypeGroup.add(DMButton);
        modelTypeGroup.add(BPMButton);
        modelTypeGroup.add(UMLButton);

        projectGroup.add(currentProjectButton);
        projectGroup.add(newProjectButton);

        add(projectHeader, new GridBagConstraints(0, 0, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        add(currentProjectButton, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 24, 0, 0), 0, 0));
        add(newProjectButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 24, 0, 6), 0, 0));
        add(newProjectField, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 0, 0, 0), 0, 0));
        add(Box.createHorizontalGlue(), new GridBagConstraints(2, 2, 1, 1, 0.2, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0,
                0));

        add(newModelHeader, new GridBagConstraints(0, 5, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(12, 0, 0, 0), 0, 0));
        add(DMButton, new GridBagConstraints(0, 6, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 24, 0, 0), 0, 0));
        add(BPMButton, new GridBagConstraints(0, 7, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 24, 0, 0), 0, 0));
        add(UMLButton, new GridBagConstraints(0, 8, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 24, 0, 0), 0, 0));

        add(previewHeader, new GridBagConstraints(0, 10, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(12, 0, 0, 0), 0, 0));
        add(previewLabel, new GridBagConstraints(0, 11, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 24, 0, 0), 0, 0));

        add(Box.createVerticalGlue(), new GridBagConstraints(0, 15, 3, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        previewLabel.setIcon(new ColorIcon(Color.WHITE, iconDM.getIconWidth(), iconDM
                .getIconHeight(), false));
        newProjectButton.setSelected(true);

        DbProject project = ApplicationContext.getFocusManager().getCurrentProject();
        if (project != null) {
            model.setChosenProject(project);
        }

        currentProjectButton.setSelected(project != null);
        currentProjectButton.setEnabled(project != null);
        newProjectField.setEnabled(project == null);
        newProjectField.setEditable(project == null);

        switch (model.getModelType()) {
        case StartupWizardModel.MODEL_TYPE_DM:
            DMButton.setSelected(true);
            previewLabel.setIcon(iconDM);
            break;
        case StartupWizardModel.MODEL_TYPE_BPM:
            BPMButton.setSelected(true);
            previewLabel.setIcon(iconBeM);
            break;
        case StartupWizardModel.MODEL_TYPE_UML:
            UMLButton.setSelected(true);
            previewLabel.setIcon(iconCM);
            break;

        default:
            break;
        }

        DMButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setModelType(StartupWizardModel.MODEL_TYPE_DM);
                previewLabel.setIcon(iconDM);
            }
        });

        BPMButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setModelType(StartupWizardModel.MODEL_TYPE_BPM);
                previewLabel.setIcon(iconBeM);
            }
        });

        UMLButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setModelType(StartupWizardModel.MODEL_TYPE_UML);
                previewLabel.setIcon(iconCM);
            }
        });

        currentProjectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                newProjectField.setEnabled(false);
                newProjectField.setEditable(false);
                model.setNewProject(false);
                model.setChosenProject(FocusManager.getSingleton().getCurrentProject());
            }
        });

        newProjectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                newProjectField.setEnabled(true);
                newProjectField.setEditable(true);
                model.setNewProject(true);
                model.setChosenProject(null);
            }
        });

        newProjectField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                model.setNewProjectName(newProjectField.getText());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                model.setNewProjectName(newProjectField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                model.setNewProjectName(newProjectField.getText());
            }
        });

    }

    @Override
    public void load() throws DbException {
        this.newProjectField.setText(model.getNewProjectName());
        currentProjectButton.setSelected(!model.isNewProject());
        currentProjectButton
                .setEnabled(ApplicationContext.getFocusManager().getCurrentProject() != null);

    }

    @Override
    public void save() throws DbException {
        if (model.isNewProject()) {
            model.createProject();
        }
    }

    @Override
    public void rollback() throws DbException {
        if (model.isNewProject()) {
            model.deleteProject();
        }
    }

    @Override
    public String getTitle() {
        return kTitle;
    }

}
