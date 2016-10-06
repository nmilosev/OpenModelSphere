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

import org.modelsphere.jack.awt.ThinBevelBorder;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.gui.wizard2.AbstractPage;
import org.modelsphere.jack.gui.wizard2.SectionHeader;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.db.DbSMSNotation;
import org.modelsphere.sms.db.util.DbInitialization;
import org.modelsphere.sms.features.international.LocaleMgr;

@SuppressWarnings("serial")
class UMLDiagramTypePage extends AbstractPage {
    private static final String kUse_Case_Diagram = LocaleMgr.screen.getString("Use_Case_Diagram");
    private static final String kClass_Diagram = LocaleMgr.screen.getString("Class_Diagram");
    private static final String kPackage_Diagram = LocaleMgr.screen.getString("Package_Diagram");
    private static final String kSequence_Diagram = LocaleMgr.screen.getString("Sequence_Diagram");
    private static final String kStatechart_Diagram = LocaleMgr.screen
            .getString("Statechart_Diagram");
    private static final String kCollaboration_Diagram = LocaleMgr.screen
            .getString("Collaboration_Diagram");
    private static final String kActivity_Diagram = LocaleMgr.screen.getString("Activity_Diagram");
    private static final String kComponent_Diagram = LocaleMgr.screen
            .getString("Component_Diagram");
    private static final String kDeployment_Diagram = LocaleMgr.screen
            .getString("Deployment_Diagram");
    private static final String kBehavioral_Diagrams = LocaleMgr.screen
            .getString("Behavioral_Diagrams");
    private static final String kChoose_a_Diagram = LocaleMgr.screen.getString("Choose_a_Diagram");
    private static final String kStructural_Diagrams = LocaleMgr.screen
            .getString("Structural_Diagrams");
    private static final String kTitle = LocaleMgr.screen.getString("DiagramType");

    private static final Image imgUmlClass = LocaleMgr.screen.getImage("umlClass");
    private static final Image imgUmlUsecase = LocaleMgr.screen.getImage("umlUsecase");
    private static final Image imgUmlPack = LocaleMgr.screen.getImage("umlPackage");
    private static final Image imgUmlSeq = LocaleMgr.screen.getImage("umlSequence");
    private static final Image imgUmlState = LocaleMgr.screen.getImage("umlState");
    private static final Image imgUmlCollabo = LocaleMgr.screen.getImage("umlCollabo");
    private static final Image imgUmlActivity = LocaleMgr.screen.getImage("umlActivity");
    private static final Image imgUmlComp = LocaleMgr.screen.getImage("umlComponent");
    private static final Image imgUmlDeploy = LocaleMgr.screen.getImage("umlDeployment");

    private JRadioButton useCaseButton = new JRadioButton(kUse_Case_Diagram);
    private JRadioButton classButton = new JRadioButton(kClass_Diagram);
    private JRadioButton packageButton = new JRadioButton(kPackage_Diagram);
    private JRadioButton sequenceButton = new JRadioButton(kSequence_Diagram);
    private JRadioButton statementButton = new JRadioButton(kStatechart_Diagram);
    private JRadioButton collaborButton = new JRadioButton(kCollaboration_Diagram);
    private JRadioButton activityButton = new JRadioButton(kActivity_Diagram);
    private JRadioButton componentButton = new JRadioButton(kComponent_Diagram);
    private JRadioButton deployButton = new JRadioButton(kDeployment_Diagram);

    private ImageIcon previewIcon;
    private JLabel previewLabel = new JLabel();

    private StartupWizardModel model = null;

    UMLDiagramTypePage(StartupWizardModel model) {
        super(new GridBagLayout());
        this.model = model;
        init();
    }

    private void init() {
        SectionHeader header = new SectionHeader(kChoose_a_Diagram);
        JLabel behaviorHeader = new JLabel(kBehavioral_Diagrams + ":");
        JLabel structuralHeader = new JLabel(kStructural_Diagrams + ":");
        SectionHeader previewHeader = new SectionHeader(StartupWizard.kPreview);

        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(useCaseButton);
        radioGroup.add(classButton);
        radioGroup.add(packageButton);
        radioGroup.add(sequenceButton);
        radioGroup.add(statementButton);
        radioGroup.add(collaborButton);
        radioGroup.add(activityButton);
        radioGroup.add(componentButton);
        radioGroup.add(deployButton);

        classButton.setSelected(true);

        previewIcon = new ImageIcon(imgUmlClass);
        previewLabel.setIcon(previewIcon);
        previewLabel.setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));

        add(header, new GridBagConstraints(0, 0, 2, 1, 1.0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        add(structuralHeader, new GridBagConstraints(0, 1, 1, 1, 1.0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 24, 0, 0), 0, 0));
        add(classButton, new GridBagConstraints(0, 2, 1, 1, 1.0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 48, 0, 0), 0, 0));
        add(packageButton, new GridBagConstraints(0, 3, 1, 1, 1.0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 48, 0, 0), 0, 0));
        add(componentButton, new GridBagConstraints(0, 4, 1, 1, 1.0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 48, 0, 0), 0, 0));
        add(deployButton, new GridBagConstraints(0, 5, 1, 1, 1.0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 48, 0, 0), 0, 0));

        add(behaviorHeader, new GridBagConstraints(1, 1, 1, 1, 1.0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 6, 0, 0), 0, 0));
        add(useCaseButton, new GridBagConstraints(1, 2, 1, 1, 1.0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 32, 0, 0), 0, 0));
        add(sequenceButton, new GridBagConstraints(1, 3, 1, 1, 1.0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 32, 0, 0), 0, 0));
        add(collaborButton, new GridBagConstraints(1, 4, 1, 1, 1.0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 32, 0, 0), 0, 0));
        add(statementButton, new GridBagConstraints(1, 5, 1, 1, 1.0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 32, 0, 0), 0, 0));
        add(activityButton, new GridBagConstraints(1, 6, 1, 1, 1.0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 32, 0, 0), 0, 0));

        add(previewHeader, new GridBagConstraints(0, 20, 2, 1, 1.0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(12, 0, 0, 0), 0, 0));
        add(previewLabel, new GridBagConstraints(0, 21, 2, 1, 1.0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(12, 24, 0, 0), 0, 0));

        add(Box.createVerticalGlue(), new GridBagConstraints(0, 25, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        switch (model.getUmlModelType()) {
        case StartupWizardModel.UML_MODEL_TYPE_CLASS:
            previewIcon = new ImageIcon(imgUmlClass);
            previewLabel.setIcon(previewIcon);
            classButton.setSelected(true);
            break;
        case StartupWizardModel.UML_MODEL_TYPE_USECASE:
            previewIcon = new ImageIcon(imgUmlUsecase);
            previewLabel.setIcon(previewIcon);
            useCaseButton.setSelected(true);
            break;
        case StartupWizardModel.UML_MODEL_TYPE_PACKAGE:
            previewIcon = new ImageIcon(imgUmlPack);
            previewLabel.setIcon(previewIcon);
            packageButton.setSelected(true);
            break;
        case StartupWizardModel.UML_MODEL_TYPE_SEQUENCE:
            previewIcon = new ImageIcon(imgUmlSeq);
            previewLabel.setIcon(previewIcon);
            sequenceButton.setSelected(true);
            break;
        case StartupWizardModel.UML_MODEL_TYPE_STATEMENT:
            previewIcon = new ImageIcon(imgUmlState);
            previewLabel.setIcon(previewIcon);
            statementButton.setSelected(true);
            break;
        case StartupWizardModel.UML_MODEL_TYPE_COLLABORATOR:
            previewIcon = new ImageIcon(imgUmlCollabo);
            previewLabel.setIcon(previewIcon);
            collaborButton.setSelected(true);
            break;
        case StartupWizardModel.UML_MODEL_TYPE_ACTIVITY:
            previewIcon = new ImageIcon(imgUmlActivity);
            previewLabel.setIcon(previewIcon);
            activityButton.setSelected(true);
            break;
        case StartupWizardModel.UML_MODEL_TYPE_COMPONENT:
            previewIcon = new ImageIcon(imgUmlComp);
            previewLabel.setIcon(previewIcon);
            componentButton.setSelected(true);
            break;
        case StartupWizardModel.UML_MODEL_TYPE_DEPLOY:
            previewIcon = new ImageIcon(imgUmlDeploy);
            previewLabel.setIcon(previewIcon);
            deployButton.setSelected(true);
            break;

        default:
            break;
        }

        updateNotation();

        useCaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setUmlModelType(StartupWizardModel.UML_MODEL_TYPE_USECASE);
                updateNotation();
                previewIcon = new ImageIcon(imgUmlUsecase);
                previewLabel.setIcon(previewIcon);
            }
        });
        classButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setUmlModelType(StartupWizardModel.UML_MODEL_TYPE_CLASS);
                previewIcon = new ImageIcon(imgUmlClass);
                previewLabel.setIcon(previewIcon);
            }
        });
        packageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setUmlModelType(StartupWizardModel.UML_MODEL_TYPE_PACKAGE);
                previewIcon = new ImageIcon(imgUmlPack);
                previewLabel.setIcon(previewIcon);
            }
        });
        sequenceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setUmlModelType(StartupWizardModel.UML_MODEL_TYPE_SEQUENCE);
                updateNotation();
                previewIcon = new ImageIcon(imgUmlSeq);
                previewLabel.setIcon(previewIcon);

            }
        });
        statementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setUmlModelType(StartupWizardModel.UML_MODEL_TYPE_STATEMENT);
                updateNotation();
                previewIcon = new ImageIcon(imgUmlState);
                previewLabel.setIcon(previewIcon);

            }
        });
        collaborButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setUmlModelType(StartupWizardModel.UML_MODEL_TYPE_COLLABORATOR);
                updateNotation();
                previewIcon = new ImageIcon(imgUmlCollabo);
                previewLabel.setIcon(previewIcon);

            }
        });

        activityButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setUmlModelType(StartupWizardModel.UML_MODEL_TYPE_ACTIVITY);
                updateNotation();
                previewIcon = new ImageIcon(imgUmlActivity);
                previewLabel.setIcon(previewIcon);

            }
        });
        componentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setUmlModelType(StartupWizardModel.UML_MODEL_TYPE_COMPONENT);
                updateNotation();
                previewIcon = new ImageIcon(imgUmlComp);
                previewLabel.setIcon(previewIcon);

            }
        });
        deployButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                model.setUmlModelType(StartupWizardModel.UML_MODEL_TYPE_DEPLOY);
                updateNotation();
                previewIcon = new ImageIcon(imgUmlDeploy);
                previewLabel.setIcon(previewIcon);
            }
        });

    }

    private void updateNotation() {
        try {
            switch (model.getUmlModelType()) {
            case StartupWizardModel.UML_MODEL_TYPE_CLASS:
                break;
            case StartupWizardModel.UML_MODEL_TYPE_USECASE:
                setChoosenNotationfromStr(DbBENotation.UML_USE_CASE);
                break;
            case StartupWizardModel.UML_MODEL_TYPE_PACKAGE:
                break;
            case StartupWizardModel.UML_MODEL_TYPE_SEQUENCE:
                setChoosenNotationfromStr(DbBENotation.UML_SEQUENCE_DIAGRAM);
                break;
            case StartupWizardModel.UML_MODEL_TYPE_STATEMENT:
                setChoosenNotationfromStr(DbBENotation.UML_STATE_DIAGRAM);
                break;
            case StartupWizardModel.UML_MODEL_TYPE_COLLABORATOR:
                setChoosenNotationfromStr(DbBENotation.UML_COLLABORATION_DIAGRAM);
                break;
            case StartupWizardModel.UML_MODEL_TYPE_ACTIVITY:
                setChoosenNotationfromStr(DbInitialization.UML_ACTIVITY_DIAGRAM_TXT);
                break;
            case StartupWizardModel.UML_MODEL_TYPE_COMPONENT:
                setChoosenNotationfromStr(DbInitialization.UML_COMPONENT_DIAGRAM_TXT);
                break;
            case StartupWizardModel.UML_MODEL_TYPE_DEPLOY:
                setChoosenNotationfromStr(DbInitialization.UML_DEPLOYMENT_DIAGRAM_TXT);
                break;

            default:
                break;
            }

        } catch (DbException ex) {
            ExceptionHandler.processUncatchedException(this, ex);
        }
    }

    private void setChoosenNotationfromStr(String notationStr) throws DbException {
        DbProject project = model.getChosenProject();

        if (project != null) {
	        project.getDb().beginReadTrans();
	        DbSMSNotation notation = (DbSMSNotation) project.findComponentByName(
	                DbBENotation.metaClass, notationStr);
	        model.setNotation(notation);
	        project.getDb().commitTrans();
        }
    }

    @Override
    public void load() throws DbException {
    }

    @Override
    public void save() throws DbException {
    }

    @Override
    public void rollback() throws DbException {
    }

    @Override
    public String getTitle() {
        return kTitle;
    }

}
