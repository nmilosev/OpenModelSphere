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

package org.modelsphere.sms.or.features;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORNotation;
import org.modelsphere.sms.or.international.LocaleMgr;

public class ConvertDataModelWorkModeDialog extends JDialog {
    private TitledBorder titledBorder1;

    private JPanel jMainPanel = new JPanel();
    private JPanel jOptionsPanel = new JPanel();
    private JPanel jMessagePanel = new JPanel();
    private JPanel jWarningPanel = new JPanel();
    private JPanel jScopePanel = new JPanel();
    private TitledBorder titledBorder;
    private JButton jConvertButton = new JButton();
    private JButton jCancelButton = new JButton();
    private JButton[] jButtonList = new JButton[] { jConvertButton, jCancelButton };
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private GridBagLayout gridBagLayout2 = new GridBagLayout();
    private GridBagLayout gridBagLayout3 = new GridBagLayout();
    private GridBagLayout gridBagLayout4 = new GridBagLayout();
    private GridBagLayout gridBagLayout5 = new GridBagLayout();
    private JLabel jNotationLabel = new JLabel();
    private JComboBox jNotationCombo = new JComboBox();
    private JCheckBox jAbsorbRelChkBox = new JCheckBox();
    private JLabel jFKMessageLabel = new JLabel();
    private JRadioButton jCurrentWithSubModelsRadioBtn = new JRadioButton();
    private JRadioButton jCurrentModelOnlyRadioBtn = new JRadioButton();
    // private JRadioButton jCurrentDiagRadioBtn = new JRadioButton();
    private CardLayout cardLayout = new CardLayout();
    private DbSMSProject m_project;
    private DbObject m_selectedObj;
    private DbObject m_semObjToConvert;
    private int m_currentWorkMode = 0;
    private int m_convertOption = 0;
    // private boolean m_isDiagramOnly = true;
    private String m_destinationNotation = null;
    private boolean m_withSubmodels = false;
    private boolean m_isAbsorbRel = true;
    private boolean m_submodelsValidated = false;
    private boolean m_currentModelValidated = false;

    // private DbORDataModel m_newDataModel = null;
    private JTextArea m_messageTextArea = new JTextArea();
    private JTextArea m_messageValid = new JTextArea();
    private String m_baseTitle;

    // Generic
    private static final String kCancel = LocaleMgr.screen.getString("Cancel");
    private static final String kConvert = LocaleMgr.screen.getString("Convert");
    private static final String kModelWithSubmodel = LocaleMgr.screen.getString("completeModel");
    private static final String kCurrentModel = LocaleMgr.screen.getString("currentModel");
    // private static final String kCurrentDiagram =
    // LocaleMgr.screen.getString("currentDiagram");
    private static final String kAbsorbRelationship = LocaleMgr.screen
            .getString("absorbRelationship");
    private static final String kScopeTitle = LocaleMgr.screen.getString("scopeTitle");
    private static final String kNotationLabel = LocaleMgr.screen.getString("notationLabel");
    private static final String kConversionMsgIntro = LocaleMgr.screen
            .getString("conversionMsgIntro");
    private static final String kCurrentModelNoValid = LocaleMgr.screen
            .getString("currentModelNoValid");
    private static final String kSubModelNoValid = LocaleMgr.screen.getString("subModelNoValid");
    private static final String kDeleteFKMessage = LocaleMgr.screen.getString("deleteFKMessage");
    private static final String kGenerateFKMessage = LocaleMgr.screen
            .getString("generateFKMessage");

    private ArrayList m_SubModelsList = new ArrayList();

    public ConvertDataModelWorkModeDialog(Frame frame, String title, boolean modal,
            DbObject[] semObjs, int workMode) {
        super(frame, title, modal);
        try {
            m_baseTitle = title;
            m_selectedObj = semObjs[0];
            m_currentWorkMode = workMode;
            semObjs[0].getDb().beginReadTrans();
            m_project = semObjs[0] == null ? null : (DbSMSProject) semObjs[0].getProject();
            semObjs[0].getDb().commitTrans();
            verifyModelsValidityStatus();
            jbInit();
            init();
            pack();
            addListeners();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        titledBorder1 = new TitledBorder("");
        this.getContentPane().setLayout(gridBagLayout1);
        jMainPanel.setBorder(BorderFactory.createEtchedBorder());
        jMainPanel.setLayout(cardLayout);
        jOptionsPanel.setLayout(gridBagLayout2);
        jMessagePanel.setLayout(gridBagLayout3);
        jWarningPanel.setLayout(gridBagLayout5);
        jScopePanel.setLayout(gridBagLayout4);
        titledBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(
                148, 145, 140)), kScopeTitle);
        jScopePanel.setBorder(titledBorder);
        jCurrentWithSubModelsRadioBtn.setText(kModelWithSubmodel);
        jCurrentModelOnlyRadioBtn.setText(kCurrentModel);
        // jCurrentDiagRadioBtn.setText(kCurrentDiagram);
        jConvertButton.setText(kConvert);
        jCancelButton.setText(kCancel);
        AwtUtil.normalizeComponentDimension(jButtonList);
        jAbsorbRelChkBox.setText(kAbsorbRelationship);
        jFKMessageLabel.setText(kDeleteFKMessage);
        jFKMessageLabel.setForeground(Color.GRAY.darker());
        jNotationLabel.setText(kNotationLabel);
        m_messageTextArea.setText(kConversionMsgIntro);
        // message initial
        m_messageValid.setText(kCurrentModelNoValid);

        m_messageTextArea.setEditable(false);
        m_messageTextArea.setFont(jCurrentWithSubModelsRadioBtn.getFont());
        m_messageTextArea.setForeground(Color.GRAY.darker());
        m_messageTextArea.setLineWrap(true);
        m_messageTextArea.setWrapStyleWord(true);
        m_messageTextArea.setBackground(jMainPanel.getBackground());

        m_messageValid.setEditable(false);
        m_messageValid.setFont(jCurrentWithSubModelsRadioBtn.getFont());
        m_messageValid.setForeground(Color.RED.darker());
        m_messageValid.setLineWrap(true);
        m_messageValid.setWrapStyleWord(true);
        m_messageValid.setBackground(jMainPanel.getBackground());

        this.getContentPane().add(
                jMainPanel,
                new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                        GridBagConstraints.BOTH, new Insets(12, 12, 7, 11), 0, 0));

        jMainPanel.add(jOptionsPanel, "1");

        // Options Panel-message
        jOptionsPanel.add(jMessagePanel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0));

        jMessagePanel.add(m_messageTextArea, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(12, 12, 11, 11), 0,
                0));
        jMessagePanel.add(jFKMessageLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(6, 12, 5, 11), 0, 0));
        // Options Panel-scope
        jOptionsPanel.add(jScopePanel, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(12, 12, 0, 11), 100,
                0));
        /*
         * jScopePanel.add(jCurrentDiagRadioBtn, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
         * ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 0, 11),
         * 0, 0));
         */
        jScopePanel.add(jCurrentModelOnlyRadioBtn, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 0,
                        11), 0, 0));
        jScopePanel.add(jCurrentWithSubModelsRadioBtn, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 6,
                        11), 0, 0));
        // Notation Combo Box
        jOptionsPanel
                .add(jNotationLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                        new Insets(12, 15, 11, 0), 0, 0));
        jOptionsPanel
                .add(jNotationCombo, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                        new Insets(12, 6, 11, 11), 0, 0));
        // checkbox pour la convrsion ER --> OR
        jOptionsPanel.add(jAbsorbRelChkBox, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(6, 12, 5, 11), 0, 0));
        // Modele VALIDE ?
        jOptionsPanel.add(jWarningPanel, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0));
        jWarningPanel
                .add(m_messageValid, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(6, 12, 6, 11), 0, 0));
        // Control Buttons
        this.getContentPane().add(
                jCancelButton,
                new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                        GridBagConstraints.NONE, new Insets(6, 6, 12, 12), 0, 0));
        this.getContentPane().add(
                jConvertButton,
                new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
                        GridBagConstraints.NONE, new Insets(6, 6, 12, 6), 0, 0));

    }

    private void init() throws Exception {
        ButtonGroup radioBtnGroup = new ButtonGroup();
        radioBtnGroup.add(jCurrentModelOnlyRadioBtn);
        radioBtnGroup.add(jCurrentWithSubModelsRadioBtn);
        // radioBtnGroup.add(jCurrentDiagRadioBtn);

        m_project.getDb().beginReadTrans();
        if (m_currentWorkMode == DbORDataModel.LOGICAL_MODE_OBJECT_RELATIONAL) {
            m_destinationNotation = m_project.getErDefaultNotation().getName();
            jAbsorbRelChkBox.setSelected(false);
            jAbsorbRelChkBox.setVisible(false);
            jFKMessageLabel.setText(kDeleteFKMessage);
            m_isAbsorbRel = false;
        } else {
            m_destinationNotation = m_project.getOrDefaultNotation().getName();
            jAbsorbRelChkBox.setSelected(true);
            jAbsorbRelChkBox.setVisible(true);
            jFKMessageLabel.setText(kGenerateFKMessage);
            m_isAbsorbRel = true;
        }
        m_project.getDb().commitTrans();

        if (m_currentModelValidated) {
            jWarningPanel.setVisible(false);
            m_messageValid.setVisible(false);
            pack();
            m_messageValid.validate();
            jWarningPanel.validate();
            pack();
        }
        setNotationCombo(m_currentWorkMode);
        jNotationCombo.setSelectedItem(m_destinationNotation);
        jCurrentModelOnlyRadioBtn.setSelected(true);
        /*
         * if (m_selectedObj instanceof DbORDataModel) { jCurrentDiagRadioBtn.setEnabled(false);
         * jCurrentWithSubModelsRadioBtn.setSelected(true); m_isDiagramOnly = false; } else{
         * jCurrentDiagRadioBtn.setSelected(true); m_isDiagramOnly = true; }
         */
    } // end init()

    private void addListeners() {
        final JDialog thisDialog = this;

        jCurrentModelOnlyRadioBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // m_isDiagramOnly = false;
                m_withSubmodels = false;
                if (m_currentModelValidated) {
                    jWarningPanel.setVisible(false);
                    m_messageValid.setVisible(false);
                    pack();
                    m_messageValid.validate();
                    jWarningPanel.validate();
                    pack();
                } else {
                    m_messageValid.setText(kCurrentModelNoValid);
                    jWarningPanel.setVisible(true);
                    m_messageValid.setVisible(true);
                    pack();
                    m_messageValid.validate();
                    jWarningPanel.validate();
                    pack();
                }
            }
        });

        jCurrentWithSubModelsRadioBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // m_isDiagramOnly = false;
                m_withSubmodels = true;
                if (m_submodelsValidated) {
                    if (m_currentModelValidated) {
                        jWarningPanel.setVisible(false);
                        m_messageValid.setVisible(false);
                        pack();
                        m_messageValid.validate();
                        jWarningPanel.validate();
                        pack();
                    } else {
                        m_messageValid.setText(kCurrentModelNoValid);
                        jWarningPanel.setVisible(true);
                        m_messageValid.setVisible(true);
                        pack();
                        m_messageValid.validate();
                        jWarningPanel.validate();
                        pack();
                    }
                } else {
                    m_messageValid.setText(kSubModelNoValid);
                    jWarningPanel.setVisible(true);
                    m_messageValid.setVisible(true);
                    pack();
                    m_messageValid.validate();
                    jWarningPanel.validate();
                    pack();
                }
            }
        });

        /*
         * jCurrentDiagRadioBtn.addActionListener(new ActionListener() { public void
         * actionPerformed(ActionEvent ev) { m_isDiagramOnly = true; } });
         */

        jConvertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                ProcessConversion();
            }
        });

        jCancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                thisDialog.dispose();
            }
        });

        jAbsorbRelChkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                m_isAbsorbRel = jAbsorbRelChkBox.isSelected() ? true : false;
            }
        });

    } // end addListeners()

    //
    // PRIVATE METHODS
    //
    private void optionsPanel() {
        cardLayout.first(jMainPanel);
        jConvertButton.setText(kConvert);
        this.setTitle(m_baseTitle);
    }

    private void setNotationCombo(int workMode) throws Exception {
        m_project.getDb().beginReadTrans();
        Integer mode = null;
        DbEnumeration dbEnum = null;
        dbEnum = m_project.getComponents().elements(DbORNotation.metaClass);
        while (dbEnum.hasMoreElements()) {
            Object notation = dbEnum.nextElement();
            String notationName = ((DbORNotation) notation).getName();
            mode = ((DbORNotation) notation).getNotationMode();
            if (workMode != mode.intValue()) {
                jNotationCombo.addItem(notationName);
            }
        }
        dbEnum.close();
        m_project.getDb().commitTrans();

    } // end setNotationCombo();

    private void ProcessConversion() {
        String message;
        try {
            this.setConversionOption();
            ConvertDataModelWorkMode command = new ConvertDataModelWorkMode(m_semObjToConvert,
                    m_withSubmodels, m_destinationNotation, m_convertOption, m_baseTitle);
            command.execute();
            message = command.getMessage();
            // result = command.getResult();
        } catch (Exception ex) {
            ExceptionHandler.processUncatchedException(this, ex);
            message = ex.toString();
        } // end try

        this.dispose();
    } // end ProcessConversion();

    private void setConversionOption() throws Exception {
        m_semObjToConvert = m_selectedObj;

        m_project.getDb().beginReadTrans();
        if (m_selectedObj instanceof DbORDiagram) // && (!m_isDiagramOnly))
            m_semObjToConvert = ((DbORDiagram) m_selectedObj)
                    .getCompositeOfType(DbORDataModel.metaClass);
        m_project.getDb().commitTrans();

        if (m_currentWorkMode == DbORDataModel.LOGICAL_MODE_OBJECT_RELATIONAL)
            m_convertOption = ConvertDataModelWorkMode.OR_TO_ER;
        else
            m_convertOption = m_isAbsorbRel ? ConvertDataModelWorkMode.ER_TO_OR_ABS
                    : ConvertDataModelWorkMode.ER_TO_OR_NOABS;
        m_destinationNotation = (String) jNotationCombo.getSelectedItem();

    }

    private void verifyModelsValidityStatus() throws Exception {
        DbORDataModel currentModel = null;
        DbObject dbo = m_selectedObj;
        int nbSubModelNoValid = 0;

        m_project.getDb().beginReadTrans();
        if (m_selectedObj instanceof DbORDiagram)
            dbo = ((DbORDiagram) m_selectedObj).getCompositeOfType(DbORDataModel.metaClass);
        currentModel = (DbORDataModel) dbo;

        this.getAllSubModels(currentModel);
        DbObject[] models = new DbObject[m_SubModelsList.size()];
        for (int i = 0; i < models.length; i++) {
            models[i] = (DbObject) m_SubModelsList.get(i);
            if (!((DbORDataModel) models[i]).isIsValidated())
                nbSubModelNoValid++;
        }
        m_currentModelValidated = currentModel.isIsValidated();
        m_submodelsValidated = nbSubModelNoValid == 0 ? true : false;
        m_project.getDb().commitTrans();

        m_SubModelsList.clear();
    }

    private void getAllSubModels(DbORDataModel model) throws Exception {
        DbEnumeration dbEnum = model.getComponents().elements(DbORDataModel.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORDataModel subModel = (DbORDataModel) dbEnum.nextElement();
            m_SubModelsList.add(subModel);
            getAllSubModels(subModel);
        }
        dbEnum.close();
    }

    //
    // UNIT TEST
    //
    public static void main(String args[]) {
        // create frame
        JFrame mainframe = new JFrame(""); // NOT LOCALIZABLE, unit test
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add property dialog
        ConvertDataModelWorkModeDialog dialog = new ConvertDataModelWorkModeDialog(mainframe,
                "Conversion Conceptuel-Relationel", true, null, 1); // NOT
        // LOCALIZABLE,
        // unit
        // test

        boolean test1 = false;

        if (test1) {
            mainframe.getContentPane().add(dialog.jOptionsPanel);
            mainframe.setVisible(true);
        } else {
            dialog.setVisible(true);
        }
    } // end main()
} // end GenerateCommonItemsDialog
