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

package org.modelsphere.sms.features;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.screen.DbLookupDialog;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSUserDefinedPackage;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.international.LocaleMgr;

public class GenerateCommonItemsDialog extends JDialog {

    private JPanel mainCardPanel = new JPanel();
    private JPanel optionsPanel = new JPanel();

    private JPanel generationScopePanel = new JPanel();
    private JPanel modelChoosenPanel = new JPanel();
    private JPanel generationOptionsPanel = new JPanel();

    private JPanel reportPanel = new JPanel();
    private JButton optionBtn = new JButton();
    private JButton nextBtn = new JButton();
    private JButton canceltBtn = new JButton();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JRadioButton partialScopeRadioBtn = new JRadioButton();
    private JRadioButton totalScopeRadioBtn = new JRadioButton();
    private JCheckBox skipReportChkBox = new JCheckBox();
    private JCheckBox useNameChkBox = new JCheckBox();
    private JRadioButton createModelRadioBtn = new JRadioButton();
    private JRadioButton selectModelRadioBtn = new JRadioButton();
    private JLabel jLabel1 = new JLabel();
    private JLabel noneLabel = new JLabel();
    private JLabel generateForEachLabel = new JLabel();
    private JLabel scopeLabel = new JLabel();
    private JLabel optionsLabel = new JLabel();
    private JLabel modelLabel = new JLabel();
    private JComboBox forEachComboBox = new JComboBox();
    private JButton selectModelBtn = new JButton();
    private GridBagLayout gridBagLayout2 = new GridBagLayout();
    private CardLayout cardLayout = new CardLayout();

    private DbSMSProject m_project;
    private DbObject[] m_semObjs;
    private DbORCommonItemModel m_selectedCommonItemModel = null;
    private ArrayList m_commonItemModelList;
    private JTextArea m_textArea = new JTextArea(10, 20);
    private String m_baseTitle;

    // Generic
    private static final String CANCEL = LocaleMgr.screen.getString("Cancel");
    private static final String GENERATE = LocaleMgr.screen.getString("Generate");
    private static final String NONE = LocaleMgr.screen.getString("None");
    private static final String OPTIONS = LocaleMgr.screen.getString("Options");
    private static final String REPORT = LocaleMgr.screen.getString("ReportBtn");
    private static final String PARTIAL_OPT = LocaleMgr.screen.getString("PartialOption");
    private static final String TOTAL_OPT = LocaleMgr.screen.getString("TotalOption");
    private static final String CREATE_OPT = LocaleMgr.screen.getString("CreateNewCommonItemModel");
    private static final String SELECT_OPT = LocaleMgr.screen.getString("SelectCommonItemModel");
    private static final String SKIP_REPORT = LocaleMgr.screen.getString("SkipReport");
    private static final String GENERATION_SCOPE = LocaleMgr.screen.getString("GenerationScope");
    private static final String GENERATION_OPTIONS = LocaleMgr.screen
            .getString("GenerationOptions");
    private static final String NO_ITEM_GENERATED = LocaleMgr.message
            .getString("NoCommonItemHasToBeGenerated");
    private static final String ITEMS_GENERATED_PATN = LocaleMgr.message
            .getString("CommonItemsGeneratedPattern");
    private static final String COMMON_ITEM_MODEL = DbORCommonItemModel.metaClass.getGUIName();

    // Relational & Object
    private static final String USE_COLUMN_NAME = LocaleMgr.screen.getString("UseColumnNames");
    private static final String COLUMN_INSTANCE = LocaleMgr.screen.getString("columnInstance");
    private static final String COLUMN_NAME = LocaleMgr.screen.getString("columnName");
    private static final String COLUMN_TYPE_PROFILE = LocaleMgr.screen
            .getString("columnTypeProfile");
    private static final String GENERATE_A_COMMON_ITEM_PER = LocaleMgr.screen
            .getString("GenerateACommonItemPer");

    private TitledBorder scopeTitle = new TitledBorder(GENERATION_SCOPE);
    private TitledBorder modelTitle = new TitledBorder(COMMON_ITEM_MODEL);
    private TitledBorder optionsTitle = new TitledBorder(GENERATION_OPTIONS);

    public GenerateCommonItemsDialog(Frame frame, String title, boolean modal, DbObject[] semObjs) {
        super(frame, title + " - " + OPTIONS, modal);
        try {
            jbInit();
            pack();
            m_baseTitle = title;
            m_semObjs = semObjs;
            m_project = semObjs[0] == null ? null : (DbSMSProject) semObjs[0].getProject();
            m_commonItemModelList = getCommonItemModelList(m_project);
            init();
            addListeners();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public GenerateCommonItemsDialog() {
        this(null, "", false, null);
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(gridBagLayout1);
        mainCardPanel.setBorder(BorderFactory.createEtchedBorder());

        mainCardPanel.setLayout(cardLayout);
        optionsPanel.setLayout(gridBagLayout2);
        reportPanel.setLayout(new BorderLayout());

        forEachComboBox.addItem(COLUMN_INSTANCE);
        forEachComboBox.addItem(COLUMN_NAME);
        forEachComboBox.addItem(COLUMN_TYPE_PROFILE);

        optionBtn.setText("< " + OPTIONS);
        nextBtn.setText(REPORT + " >");
        canceltBtn.setText(CANCEL);
        AwtUtil.normalizeComponentDimension(new JButton[] { optionBtn, nextBtn, canceltBtn });
        partialScopeRadioBtn.setText(PARTIAL_OPT);
        totalScopeRadioBtn.setText(TOTAL_OPT);
        createModelRadioBtn.setText(CREATE_OPT);
        selectModelRadioBtn.setText(SELECT_OPT);
        jLabel1.setBorder(BorderFactory.createLoweredBevelBorder());
        jLabel1.setText("");
        noneLabel.setBorder(BorderFactory.createEtchedBorder());
        noneLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noneLabel.setText(NONE);
        generateForEachLabel.setText(GENERATE_A_COMMON_ITEM_PER);
        scopeLabel.setText(GENERATION_SCOPE);
        optionsLabel.setText(GENERATION_OPTIONS);
        modelLabel.setText(COMMON_ITEM_MODEL);

        Color labelColor = UIManager.getColor("Tree.selectionBackground").darker(); // NOT LOCALIZABLE, color code
        scopeLabel.setForeground(labelColor);
        optionsLabel.setForeground(labelColor);
        modelLabel.setForeground(labelColor);
        selectModelBtn.setText("...");

        skipReportChkBox.setText(SKIP_REPORT);
        useNameChkBox.setText(USE_COLUMN_NAME);

        generationScopePanel.setBorder(scopeTitle);
        generationScopePanel.setLayout(new GridBagLayout());
        modelChoosenPanel.setBorder(modelTitle);
        modelChoosenPanel.setLayout(new GridBagLayout());
        generationOptionsPanel.setBorder(optionsTitle);
        generationOptionsPanel.setLayout(new GridBagLayout());

        this.getContentPane().add(
                mainCardPanel,
                new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                        GridBagConstraints.BOTH, new Insets(6, 6, 3, 6), 0, 0));

        mainCardPanel.add(optionsPanel, "1");
        mainCardPanel.add(reportPanel, "2");

        // Generation Scope
        optionsPanel.add(generationScopePanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(12, 12, 11, 11),
                0, 0));

        generationScopePanel.add(partialScopeRadioBtn,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                        GridBagConstraints.BOTH, new Insets(3, 9, 3, 3), 0, 0));
        generationScopePanel.add(totalScopeRadioBtn, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(3, 9, 11, 3), 0,
                0));

        // Model Choosen
        optionsPanel.add(modelChoosenPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 12, 11, 11),
                0, 0));

        modelChoosenPanel.add(selectModelRadioBtn,
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST,
                        GridBagConstraints.NONE, new Insets(3, 9, 3, 0), 0, 0));
        modelChoosenPanel.add(noneLabel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(3, 12, 3, 0), 8, 3));
        modelChoosenPanel.add(selectModelBtn, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(3, 6, 3, 11), 0,
                -3));

        modelChoosenPanel.add(createModelRadioBtn, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(3, 9, 11, 3), 0,
                0));

        // Generation Options
        optionsPanel.add(generationOptionsPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 12, 11, 11),
                0, 0));

        generationOptionsPanel.add(generateForEachLabel, new GridBagConstraints(0, 0, 1, 1, 0.0,
                0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(3, 12, 11,
                        11), 0, 0));
        generationOptionsPanel.add(forEachComboBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(3, 3, 11, 11), 0,
                0));

        generationOptionsPanel.add(skipReportChkBox, new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(3, 9, 11, 0), 0,
                0));
        generationOptionsPanel.add(useNameChkBox, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(3, 0, 11, 3), 0,
                0));

        this.getContentPane().add(
                canceltBtn,
                new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                        GridBagConstraints.NONE, new Insets(12, 6, 12, 12), 0, 0));
        this.getContentPane().add(
                nextBtn,
                new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                        GridBagConstraints.NONE, new Insets(12, 6, 12, 6), 0, 0));
        this.getContentPane().add(
                optionBtn,
                new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
                        GridBagConstraints.NONE, new Insets(12, 6, 12, 6), 0, 0));

        m_textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(m_textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        reportPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void init() {
        ButtonGroup group1 = new ButtonGroup();
        group1.add(partialScopeRadioBtn);
        group1.add(totalScopeRadioBtn);

        ButtonGroup group2 = new ButtonGroup();
        group2.add(createModelRadioBtn);
        group2.add(selectModelRadioBtn);

        partialScopeRadioBtn.setSelected(true);
        selectModelRadioBtn.setSelected(true);
        optionBtn.setEnabled(false);
        noneLabel.setEnabled(false);
        useNameChkBox.setSelected(true);

        if (m_commonItemModelList.isEmpty()) {
            createModelRadioBtn.setSelected(true);
            selectModelRadioBtn.setEnabled(false);
            selectModelBtn.setEnabled(false);
        } else {
            m_selectedCommonItemModel = (DbORCommonItemModel) m_commonItemModelList.get(0);
            try {
                noneLabel.setText(m_selectedCommonItemModel.getName());
            } catch (DbException ex) {
                noneLabel.setText(NONE);
            }
            noneLabel.setEnabled(true);
        } // end if
    } // end init()

    private void addListeners() {
        final JDialog thisDialog = this;

        createModelRadioBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                jLabel1.setEnabled(createModelRadioBtn.isSelected());
                noneLabel.setEnabled(!createModelRadioBtn.isSelected());
                nextBtn.setEnabled(true);
                selectModelBtn.setEnabled(!createModelRadioBtn.isSelected());
            }
        });

        selectModelRadioBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                jLabel1.setEnabled(!selectModelRadioBtn.isSelected());
                noneLabel.setEnabled(selectModelRadioBtn.isSelected());
                selectModelBtn.setEnabled(selectModelRadioBtn.isSelected());
                nextBtn.setEnabled(m_selectedCommonItemModel != null);
            }
        });

        optionBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                optionsPanel();
            }
        });

        nextBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                reportPanel();
            }
        });

        canceltBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                g_reportDisplayed = false;
                thisDialog.dispose();
            }
        });

        selectModelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                chooseCommonItemModel();
            }
        });

        skipReportChkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String next = skipReportChkBox.isSelected() ? GENERATE : REPORT + " >";
                nextBtn.setText(next);
            }
        });
    } // end addListeners()

    //
    // PRIVATE METHODS
    //
    private void chooseCommonItemModel() {
        try {
            Point start = GenerateCommonItemsDialog.this.getLocation();
            Dimension dim = GenerateCommonItemsDialog.this.getSize();
            Point center = new Point(start.x + dim.width, start.y + dim.height);
            Db db = m_project.getDb();
            db.beginReadTrans();
            String title = DbORCommonItemModel.metaClass.getGUIName();
            DefaultComparableElement elem = DbLookupDialog.selectDbo(
                    GenerateCommonItemsDialog.this, title, center, m_project.getDb(),
                    m_commonItemModelList, m_selectedCommonItemModel, NONE, false);
            // Notice: DbLookupDialog.selectDbo() closes the read transaction
            if (elem != null) {
                m_selectedCommonItemModel = null;
                Object obj = elem.object;
                if (obj instanceof DbORCommonItemModel) {
                    m_selectedCommonItemModel = (DbORCommonItemModel) obj;
                    String name = m_selectedCommonItemModel.getName();
                    noneLabel.setText(name);
                } else {
                    m_selectedCommonItemModel = null;
                    noneLabel.setText(NONE);
                } // end of
            } // end if
            nextBtn.setEnabled(m_selectedCommonItemModel != null);
        } catch (DbException ex) {
        } // end try
    } // end chooseCommonItemModel()

    private ArrayList getCommonItemModelList(DbSMSProject project) throws DbException {
        ArrayList list = new ArrayList();
        if (project == null) {
            return list;
        }

        DbRelationN relN = project.getComponents();
        DbEnumeration dbEnum = relN.elements();
        while (dbEnum.hasMoreElements()) {
            DbObject obj = dbEnum.nextElement();
            if (obj instanceof DbORCommonItemModel) {
                list.add(obj);
            } else if (obj instanceof DbSMSUserDefinedPackage) {
                DbSMSUserDefinedPackage userPackage = (DbSMSUserDefinedPackage) obj;
                addInCommonItemModelList(list, userPackage);
            }
        } // end while
        dbEnum.close();

        // list.add():
        return list;
    } // end getCommonItemModelList()

    private void addInCommonItemModelList(ArrayList list, DbSMSUserDefinedPackage userPackage)
            throws DbException {
        DbRelationN relN = userPackage.getComponents();
        DbEnumeration dbEnum = relN.elements();
        while (dbEnum.hasMoreElements()) {
            DbObject obj = dbEnum.nextElement();
            if (obj instanceof DbORCommonItemModel) {
                list.add(obj);
            } else if (obj instanceof DbSMSUserDefinedPackage) {
                DbSMSUserDefinedPackage subUserPackage = (DbSMSUserDefinedPackage) obj;
                addInCommonItemModelList(list, subUserPackage);
            }
        } // end while
        dbEnum.close();
    } // end addInCommonItemModelList()

    private void optionsPanel() {
        cardLayout.first(mainCardPanel);
        optionBtn.setEnabled(false);
        nextBtn.setText(REPORT + " >");
        nextBtn.setEnabled(true);
        this.setTitle(m_baseTitle + " - " + OPTIONS);
        g_reportDisplayed = false; // invalidate previous report, if any
    }

    private static boolean g_reportDisplayed = false;

    private void reportPanel() {
        // Getting options
        int options = totalScopeRadioBtn.isSelected() ? GenerateCommonItems.TOTAL_OPTION
                : GenerateCommonItems.PARTIAL_OPTION;
        options += useNameChkBox.isSelected() ? GenerateCommonItems.USE_COLUMN_NAME : 0;

        int idx = forEachComboBox.getSelectedIndex();
        if (idx == 1) {
            options += GenerateCommonItems.ONE_ITEM_PER_COLUMN_NAME;
        } else if (idx == 2) {
            options += GenerateCommonItems.ONE_ITEM_PER_TYPE_PROFILE;
        }

        // skip report ?
        boolean skipReport = skipReportChkBox.isSelected();

        if ((!g_reportDisplayed) && (!skipReport)) { // If no report already
            // exists, and if user
            // wants it, then
            // generate report
            cardLayout.next(mainCardPanel);
            optionBtn.setEnabled(true);
            nextBtn.setText(GENERATE);
            this.setTitle(m_baseTitle + " - " + REPORT);

            try {
                // create a temporary common item model
                if (createModelRadioBtn.isSelected()) {
                    m_selectedCommonItemModel = new DbORCommonItemModel(m_project);
                }

                // Generate report and display it
                GenerateCommonItems command = new GenerateCommonItems(
                        GenerateCommonItems.GENERATE_REPORT, m_semObjs, m_selectedCommonItemModel,
                        options, m_baseTitle);
                command.execute();

                // try to delete the temporary common item model
                if (createModelRadioBtn.isSelected()) {
                    m_selectedCommonItemModel.remove();
                    m_selectedCommonItemModel = null;
                }

                String report = command.getReport();
                m_textArea.setText("");
                m_textArea.append(report);

                int nbItems = command.getCounter();
                if (nbItems == 0) {
                    report = NO_ITEM_GENERATED;
                } else {
                    report = MessageFormat.format(ITEMS_GENERATED_PATN, new Object[] { new Integer(
                            nbItems) });
                }
                m_textArea.append(report);
                g_reportDisplayed = true;

                nextBtn.setEnabled(nbItems > 0);
            } catch (Exception ex) {
                ExceptionHandler.processUncatchedException(this, ex);
            } // end try
        } else { // Generate common item
            int result;
            String message;

            try {
                // create a brand-new common item model
                if (createModelRadioBtn.isSelected()) {
                    m_selectedCommonItemModel = new DbORCommonItemModel(m_project);
                }

                GenerateCommonItems command = new GenerateCommonItems(
                        GenerateCommonItems.GENERATE_COMMON_ITEMS, m_semObjs,
                        m_selectedCommonItemModel, options, m_baseTitle);
                command.execute();
                message = command.getMessage();
                result = command.getResult();
            } catch (Exception ex) {
                ExceptionHandler.processUncatchedException(this, ex);
                message = ex.toString();
                result = JOptionPane.ERROR_MESSAGE;
            } // end try

            // (useless) JOptionPane.showMessageDialog(this, message,
            // m_baseTitle, result);
            g_reportDisplayed = false; // invalidate previous report, if any
            this.dispose();
        } // end if
    } // end nextPanel();

    //
    // UNIT TEST
    //
    public static void main(String args[]) {
        // create frame
        JFrame mainframe = new JFrame(""); // NOT LOCALIZABLE, unit test
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add property dialog
        GenerateCommonItemsDialog dialog = new GenerateCommonItemsDialog(mainframe,
                "Generate Common Items", true, null); // NOT
        // LOCALIZABLE,
        // unit test

        boolean test1 = false;

        if (test1) {
            mainframe.getContentPane().add(dialog.optionsPanel);
            mainframe.setVisible(true);
        } else {
            dialog.setVisible(true);
        }
    } // end main()
} // end GenerateCommonItemsDialog
