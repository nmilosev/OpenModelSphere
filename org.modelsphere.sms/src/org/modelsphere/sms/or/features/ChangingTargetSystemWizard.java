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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.ThinBevelBorder;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbRoot;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.DbSMSUserDefinedPackage;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORModel;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.plugins.TargetSystem;
import org.modelsphere.sms.plugins.TargetSystemManager;

public class ChangingTargetSystemWizard extends JDialog {
    private TitledBorder titledBorder1;

    private JPanel mainCardPanel = new JPanel();
    private JPanel controlBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    private JPanel jPanel4 = new JPanel();
    private JButton previousBtn = new JButton();
    private JButton nextBtn = new JButton();
    private JButton cancelBtn = new JButton();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JLabel jLabel1 = new JLabel();
    private CardLayout cardLayout = new CardLayout();

    // //////////////////////////////////////////
    // Options come from these components
    //

    // set at page 1
    private DbSMSTargetSystem m_selectedDestTarget;

    // set at page 2
    private JRadioButton pg2UnlinkDbBtn = new JRadioButton();
    private JRadioButton pg2ConvertDbBtn = new JRadioButton();
    private JCheckBox pg2ModelBackupChkBox = new JCheckBox();

    // set at page 3
    private JLabel pg3Question1 = new JLabel("???");
    private JLabel pg3Question2 = new JLabel("???");
    private JLabel pg3Question3 = new JLabel("???");
    private JRadioButton pg3ConvertSubModelBtn = new JRadioButton();
    private JRadioButton pg3NOTConvertSubModelBtn = new JRadioButton();
    private JRadioButton pg3CreateDomainModelBtn = new JRadioButton();
    private JRadioButton pg3StoreDomainPatternBtn = new JRadioButton();
    private JRadioButton pg3UnlinkItemsBtn = new JRadioButton();
    private JRadioButton pg3CreateItemsBtn = new JRadioButton();
    private JRadioButton pg3UseSameItemsBtn = new JRadioButton();
    private DbORDomainModel m_domainModel = null;

    // set at page 4
    private JTextArea m_messageArea = new JTextArea();

    // set at page 5
    private JTextArea m_messageArea2 = new JTextArea();
    //
    // /////////////////////////////////////////////

    private DbSMSProject m_project;
    private DbORModel m_model;
    private JTextArea m_textArea = new JTextArea(10, 20);
    private String m_baseTitle;
    private boolean m_linkedToOtherModel = false;

    private static final String CANCEL = LocaleMgr.screen.getString("Cancel");
    private static final String NEXT = LocaleMgr.screen.getString("Next");
    private static final String PREVIOUS = LocaleMgr.screen.getString("Previous");
    private static final String ERROR = LocaleMgr.screen.getString("Error");
    private static final String CONVERT = LocaleMgr.screen.getString("Convert");
    private static final String UNSPECIFIED = LocaleMgr.screen.getString("Unspecified");

    private static final String SOURCE_TARGET_SYSTEM = LocaleMgr.screen
            .getString("SourceTargetSystem");
    private static final String DEST_TARGET_SYSTEM = LocaleMgr.screen
            .getString("DestinationTargetSystem");
    private static final String CONVERT_ALL_THE_SUB_MODELS = LocaleMgr.screen
            .getString("ConvertAllTheSubModelsRecursively");
    private static final String DO_NOT_CONVERT_SUB_MODELS = LocaleMgr.screen
            .getString("DoNotConvertSubModels");
    private static final String CREATE_A_NEW_DOMAIN_MODEL = LocaleMgr.screen
            .getString("CreateADomainModelToStoreNewDomains");
    private static final String STORE_NEW_DOMAINS_PATTERN = LocaleMgr.screen
            .getString("StoreNewDomainsInThisDomainModels");
    private static final String UNLINK_ALL_ITEMS = LocaleMgr.screen
            .getString("UnlinkAllTheCommonItemsFromColumns");
    private static final String CREATE_NEW_ITEMS = LocaleMgr.screen
            .getString("CreateNewCommonItemsTypedWithDestinationTargetSystem");
    private static final String USE_SAME_ITEMS = LocaleMgr.screen
            .getString("UseTheSameCommonItemsTypedWithSourceTargetSystem");
    private static final String DATA_MODEL_HAS_SUBMODELS = LocaleMgr.screen
            .getString("DataModelHasOneOrSeveralSubmodels");
    private static final String DATA_MODEL_DOES_NOT_HAVE_SUBMODELS = LocaleMgr.screen
            .getString("DataModelDoesNotHaveAnySubmodels");
    private static final String NO_DOMAIN_MODEL = LocaleMgr.screen
            .getString("NoDomainModelLinkedTo");
    private static final String IF_CONVERSION_NEEDS_TO_CREATE = LocaleMgr.screen
            .getString("IfConversionNeedsToCreateNewDomains");
    private static final String IF_SOME_COLUMNS_ARE_LINKED = LocaleMgr.screen
            .getString("IfThereAreColumnsLinkedToCommonItems");
    private static final String CONVERT_TO_PATTERN = LocaleMgr.screen.getString("ConvertToPattern");
    private static final String UNLINK_FROM_PATTERN = LocaleMgr.screen
            .getString("UnlinkFromPattern");
    private static final String LEAVE_TO_PATTERN = LocaleMgr.screen.getString("LeaveToPattern");
    private static final String ABOUT_TO_CONVERT_PATTERN = LocaleMgr.screen
            .getString("AboutToConvert0FromVersion1To2");
    private static final String ALL_THE_SPECIFIC_PROPERTIES = LocaleMgr.screen
            .getString("WarningAllThe0SpecificProperties");;
    private static final String CHOOSE_A_DEST_TARGET_DIFFERENT_FROM = LocaleMgr.screen
            .getString("ChooseADestinationTargetSystemDifferentFromTheSourceTargetSystem");
    private static final String HAS_NO_SCHEMA = LocaleMgr.screen
            .getString("0HasNoSchemaDomainModelOrOperationLibrary");
    private static final String HAS_A_SCHEMA = LocaleMgr.screen
            .getString("0HasASchemaADomainModelOrAnOperationLibrary");
    private static final String UNLINK_SCHEMA = LocaleMgr.screen
            .getString("unlinkTheSchemaTheDomainModelAndTheOperationLibrary");;
    private static final String CONVERT_THE_SCHEMA = LocaleMgr.screen
            .getString("ConvertTheSchemaTheDomainMmodelAndTheOperationLibrary");
    private static final String KEEP_A_BACKUP_COPY = LocaleMgr.screen.getString("KeepABackUpCopy");
    private static final String MODEL_HAS_NO_DEPLOYMENT_DB = LocaleMgr.screen
            .getString("0HasNoDeploymentDatabase");
    private static final String MODEL_HAS_A_DEPLOYMENT_DB = LocaleMgr.screen
            .getString("0HasADeploymentDatabase");
    private static final String LIBRARY_HAS_NO_DEPLOYMENT_DB = LocaleMgr.screen
            .getString("1HasNoDeploymentDatabase");
    private static final String LIBRARY_HAS_A_DEPLOYMENT_DB = LocaleMgr.screen
            .getString("1HasADeploymentDatabase");
    private static final String UNLINK_THE_DEPLOYMENT_DATABASE = LocaleMgr.screen
            .getString("unlinkTheDeploymentDatabase");
    private static final String CONVERT_THE_DEPLOYMENT_DATABASE = LocaleMgr.screen
            .getString("ConvertTheDeploymentDatabaseAndItsWholeContent");

    public ChangingTargetSystemWizard(Frame frame, String title, boolean modal, DbORModel model) {
        super(frame, title, modal);
        try {
            m_baseTitle = title;
            m_model = model;
            m_project = (DbSMSProject) model.getProject();
            jbInit();
            pack();
            init();
            addListeners();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ChangingTargetSystemWizard() {
        this(null, "", false, null);
    }

    private void jbInit() throws Exception {
        titledBorder1 = new TitledBorder("");
        this.getContentPane().setLayout(gridBagLayout1);
        mainCardPanel.setBorder(BorderFactory.createEtchedBorder());
        mainCardPanel.setLayout(cardLayout);

        previousBtn.setText("< " + PREVIOUS);
        nextBtn.setText(NEXT + " >");
        cancelBtn.setText(CANCEL);
        AwtUtil.normalizeComponentDimension(new JButton[] { previousBtn, nextBtn });
        jLabel1.setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));
        jLabel1.setText("");

        // Color labelColor =
        // UIManager.getColor("Tree.selectionBackground").darker(); //NOT
        // LOCALIZABLE, color code
        this.getContentPane().add(
                mainCardPanel,
                new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                        GridBagConstraints.BOTH, new Insets(12, 12, 11, 11), 0, 0));

        JPanel page1 = createPage1();
        JPanel page2 = createPage2();
        JPanel page3 = createPage3();
        JPanel page4 = createPage4();
        JPanel page5 = createPage5();

        mainCardPanel.add(page1, "0");
        mainCardPanel.add(page2, "1");
        mainCardPanel.add(page3, "2");
        mainCardPanel.add(page4, "3");
        mainCardPanel.add(page5, "4");

        controlBtnPanel.add(previousBtn);
        controlBtnPanel.add(nextBtn);
        controlBtnPanel.add(Box.createHorizontalStrut(50));
        controlBtnPanel.add(cancelBtn);
        controlBtnPanel.setBorder(BorderFactory.createEmptyBorder(6, 7, 6, 6));

        this.getContentPane().add(
                controlBtnPanel,
                new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTH,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    //
    // CREATE WIZARD PAGES
    //
    // private final JDialog thisDialog = this;
    private JPanel createPage1() throws DbException {
        JPanel cardPanel1 = new JPanel(new GridBagLayout());
        JPanel targetPanel = new JPanel(new GridBagLayout());

        JLabel sourceTSLbl = new JLabel(SOURCE_TARGET_SYSTEM);
        JLabel descTSLbl = new JLabel(DEST_TARGET_SYSTEM);
        DbSMSTargetSystem ts = m_model.getTargetSystem();
        JLabel sourceTS = new JLabel(ts.getName() + " " + ts.getVersion());
        final JLabel descTS = new JLabel(UNSPECIFIED);
        JButton browseBtn = new JButton("...");

        sourceTS.setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));
        descTS.setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));

        // LINE 1 : SOURCE
        targetPanel.add(sourceTSLbl, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(18, 12, 5, 5), 0, 0));
        targetPanel.add(sourceTS, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(18, 6, 5, 5), 0,
                0));

        // LINE 2 : DESTINATION
        targetPanel.add(descTSLbl, new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 11, 5), 0, 0));
        targetPanel.add(descTS, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 6, 11, 5),
                0, 0));
        targetPanel
                .add(browseBtn, new GridBagConstraints(2, 1, 1, 1, 0.0, 1.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(12, 6, 11, 11), 0, -5));

        cardPanel1.add(targetPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 24, 0, 24),
                0, 0));

        browseBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    TargetSystem ts = TargetSystemManager.getSingleton();
                    boolean showAll = true;
                    ArrayList newtarget = ts.addTargetSystem(ApplicationContext
                            .getDefaultMainFrame(), m_project, false, showAll);
                    if (newtarget != null && newtarget.size() == 1 && newtarget.get(0) != null) {
                        m_selectedDestTarget = (DbSMSTargetSystem) newtarget.get(0);
                        String name = m_selectedDestTarget.getName() + " "
                                + m_selectedDestTarget.getVersion();
                        descTS.setText(name);
                        nextBtn.setEnabled(true);
                    } else {
                        nextBtn.setEnabled(false);
                    } // end if
                } catch (DbException ex) {
                } // end try
            } // end actionPerformed()
        });

        return cardPanel1;
    } // end createPage1()

    private JPanel createPage2() throws DbException {
        JPanel cardPanel2 = new JPanel(new GridBagLayout());
        JLabel question = new JLabel();
        ButtonGroup group = new ButtonGroup();
        group.add(pg2UnlinkDbBtn);
        group.add(pg2ConvertDbBtn);
        pg2UnlinkDbBtn.setSelected(true);
        pg2ModelBackupChkBox.setText(KEEP_A_BACKUP_COPY);
        pg2ModelBackupChkBox.setSelected(true);

        setPage2OptionText(m_model, question, pg2UnlinkDbBtn, pg2ConvertDbBtn);

        cardPanel2.add(pg2ModelBackupChkBox, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(12, 6, 11,
                        11), 0, 0));

        cardPanel2.add(question, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(12, 12, 5,
                        11), 0, 0));
        cardPanel2.add(pg2UnlinkDbBtn, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 24, 0,
                        11), 0, 0));
        cardPanel2.add(pg2ConvertDbBtn, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 24, 11,
                        11), 0, 0));

        return cardPanel2;
    } // end createPage2()

    private JPanel createPage3() {
        JPanel cardPanel3 = new JPanel(new GridBagLayout());

        pg3ConvertSubModelBtn.setText(CONVERT_ALL_THE_SUB_MODELS);
        pg3NOTConvertSubModelBtn.setText(DO_NOT_CONVERT_SUB_MODELS);
        ButtonGroup group1 = new ButtonGroup();
        group1.add(pg3ConvertSubModelBtn);
        group1.add(pg3NOTConvertSubModelBtn);

        pg3CreateDomainModelBtn.setText(CREATE_A_NEW_DOMAIN_MODEL);
        String msg = MessageFormat.format(STORE_NEW_DOMAINS_PATTERN, new Object[] { UNSPECIFIED });
        pg3StoreDomainPatternBtn.setText(msg);
        ButtonGroup group2 = new ButtonGroup();
        group2.add(pg3CreateDomainModelBtn);
        group2.add(pg3StoreDomainPatternBtn);

        pg3UnlinkItemsBtn.setText(UNLINK_ALL_ITEMS);
        pg3CreateItemsBtn.setText(CREATE_NEW_ITEMS);
        pg3CreateItemsBtn.setVisible(false); // NOT IMPLEMENTED YET
        pg3UseSameItemsBtn.setText(USE_SAME_ITEMS);
        ButtonGroup group3 = new ButtonGroup();
        group3.add(pg3UnlinkItemsBtn);
        group3.add(pg3CreateItemsBtn);
        group3.add(pg3UseSameItemsBtn);

        // LINE 1-3 : FIRST QUESTION
        cardPanel3.add(pg3Question1, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(12, 12, 5,
                        11), 0, 0));
        cardPanel3.add(pg3ConvertSubModelBtn, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 24, 0,
                        11), 0, 0));
        cardPanel3.add(pg3NOTConvertSubModelBtn, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.3,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 24, 11,
                        11), 0, 0));

        // LINE 4-6 : SECOND QUESTION
        cardPanel3.add(pg3Question2, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(12, 12, 5,
                        11), 0, 0));
        cardPanel3.add(pg3CreateDomainModelBtn, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 24, 0,
                        11), 0, 0));
        cardPanel3.add(pg3StoreDomainPatternBtn, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.3,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 24, 11,
                        11), 0, 0));

        // LINE 7-10 : THIRD QUESTION
        cardPanel3.add(pg3Question3, new GridBagConstraints(0, 6, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(12, 12, 5,
                        11), 0, 0));
        cardPanel3.add(pg3UnlinkItemsBtn, new GridBagConstraints(0, 7, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 24, 0,
                        11), 0, 0));
        cardPanel3.add(pg3CreateItemsBtn, new GridBagConstraints(0, 8, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 24, 0,
                        11), 0, 0));
        cardPanel3.add(pg3UseSameItemsBtn, new GridBagConstraints(0, 9, 1, 1, 1.0, 0.3,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 24, 17,
                        11), 0, 0));

        pg3ConvertSubModelBtn.setSelected(true);
        pg3CreateDomainModelBtn.setSelected(true);
        pg3UseSameItemsBtn.setSelected(true);

        return cardPanel3;
    } // end createPage3()

    private void initPage3(DbORDataModel dataModel, DbORDomainModel udtModel,
            DbORDomainModel defaultDomainModel) throws DbException {
        // Does datamodel have sub models?
        DbRelationN relN = dataModel.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORDataModel.metaClass);
        if (dbEnum.hasMoreElements()) {
            pg3Question1.setText(DATA_MODEL_HAS_SUBMODELS);
            pg3ConvertSubModelBtn.setEnabled(true);
            pg3NOTConvertSubModelBtn.setEnabled(true);
        } else {
            pg3Question1.setText(DATA_MODEL_DOES_NOT_HAVE_SUBMODELS);
            pg3ConvertSubModelBtn.setEnabled(false);
            pg3NOTConvertSubModelBtn.setEnabled(false);
        } // end if
        dbEnum.close();

        if (udtModel != null) {
            pg3StoreDomainPatternBtn.setEnabled(true);
            String text = MessageFormat.format(STORE_NEW_DOMAINS_PATTERN, new Object[] { udtModel
                    .getName() });
            pg3StoreDomainPatternBtn.setText(text);
            m_domainModel = udtModel;
        } else if (defaultDomainModel != null) {
            pg3StoreDomainPatternBtn.setEnabled(true);
            String text = MessageFormat.format(STORE_NEW_DOMAINS_PATTERN,
                    new Object[] { defaultDomainModel.getName() });
            pg3StoreDomainPatternBtn.setText(text);
            m_domainModel = defaultDomainModel;
        } else {
            // If no UDT nor default domain model linked, disable button 4
            pg3StoreDomainPatternBtn.setEnabled(false);
            String patt = NO_DOMAIN_MODEL;
            String text = MessageFormat.format(patt, new Object[] { dataModel.getName() });
            pg3StoreDomainPatternBtn.setText(text);
        } // end if

        /*
         * //Do domain models already exist? ArrayList domainList = new ArrayList(); DbObject proj =
         * dataModel.getProject(); findDomainModels(domainList, proj, m_selectedDestTarget);
         */
        pg3Question2.setText(IF_CONVERSION_NEEDS_TO_CREATE);

        // Are columns linked to common items?
        pg3Question3.setText(IF_SOME_COLUMNS_ARE_LINKED);
    } // end initPage3()

    private JPanel createPage4() throws DbException {
        JPanel panel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        panel.setLayout(gridBagLayout);

        m_messageArea.setBorder(BorderFactory.createLoweredBevelBorder());
        m_messageArea.setEditable(false);
        m_messageArea.setLineWrap(true);
        m_messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(m_messageArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // LINE 1 : SOURCE
        panel.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.3,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(12, 12, 12, 12), 0,
                0));

        return panel;
    } // end createPage4()

    private JPanel createPage5() throws DbException {
        JPanel panel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        panel.setLayout(gridBagLayout);

        m_messageArea2.setBorder(BorderFactory.createLoweredBevelBorder());
        m_messageArea2.setEditable(false);
        m_messageArea2.setLineWrap(true);
        m_messageArea2.setWrapStyleWord(true);

        // LINE 1 : SOURCE
        panel.add(m_messageArea2, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.3,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(12, 12, 12, 12), 0,
                0));

        return panel;
    } // end createPage5()

    private void initPage4() throws DbException {
        // if RDBMS of 2 different families
        DbSMSTargetSystem sourceTs = m_model.getTargetSystem();
        ArrayList modelList = new ArrayList();
        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        String indentation = "";
        buildMessagePage4(modelList, writer, indentation, m_model, null, CONVERT_ACTION);
        String message = sw.getBuffer().toString();
        m_messageArea.setText(message);
    } // end initPage4()

    private void initPage5() throws DbException {
        // if RDBMS of 2 different families
        DbSMSTargetSystem sourceTs = m_model.getTargetSystem();
        String message = getMessagePage5(sourceTs, m_selectedDestTarget);
        m_messageArea2.setText(message);
    } // end initPage4()

    private static final int CONVERT_ACTION = 1;
    private static final int UNLINK_ACTION = 2;
    private static final int LEAVE_TS_ACTION = 3;

    private void buildMessagePage4(ArrayList modelList, PrintWriter writer, String indentation,
            DbORModel model, DbORModel parentModel, int action) throws DbException {
        // if parentModel not linked to any model, or if model already is list
        if ((model == null) || (modelList.contains(model))) {
            return;
        } else {
            modelList.add(model);
        }

        indentation += "    ";
        String msg = "";
        String qualifiedName1, qualifiedName2;
        switch (action) {
        case CONVERT_ACTION:
            qualifiedName1 = getQualifiedName(model);
            msg = MessageFormat.format(CONVERT_TO_PATTERN, new Object[] { qualifiedName1,
                    m_selectedDestTarget.getName(), m_selectedDestTarget.getVersion() });
            break;
        case UNLINK_ACTION:
            qualifiedName1 = getQualifiedName(model);
            qualifiedName2 = getQualifiedName(parentModel);
            msg = MessageFormat.format(UNLINK_FROM_PATTERN, new Object[] { qualifiedName1,
                    qualifiedName2 });
            break;
        case LEAVE_TS_ACTION:
            DbSMSTargetSystem currentTS = model.getTargetSystem();
            qualifiedName1 = getQualifiedName(model);
            msg = MessageFormat.format(LEAVE_TO_PATTERN, new Object[] { qualifiedName1,
                    currentTS.getName(), currentTS.getVersion() });
            break;
        } // end switch()
        writer.println(indentation + msg);

        if (action == UNLINK_ACTION) {
            return;
        }

        if (model instanceof DbORDataModel) {
            DbORDataModel dataModel = (DbORDataModel) model;
            DbORDatabase database = dataModel.getDeploymentDatabase();
            if (database != null) {
                action = (action == LEAVE_TS_ACTION) ? LEAVE_TS_ACTION : pg2UnlinkDbBtn
                        .isSelected() ? UNLINK_ACTION : CONVERT_ACTION;
                buildMessagePage4(modelList, writer, indentation, database, model, action);
            }

            // sub models
            DbRelationN relN = dataModel.getComponents();
            DbEnumeration dbEnum = relN.elements(DbORDataModel.metaClass);
            action = pg3ConvertSubModelBtn.isSelected() ? CONVERT_ACTION : LEAVE_TS_ACTION;
            while (dbEnum.hasMoreElements()) {
                DbORDataModel subModel = (DbORDataModel) dbEnum.nextElement();
                buildMessagePage4(modelList, writer, indentation, subModel, model, action);
            } // end while
            dbEnum.close();
        } else if (model instanceof DbORDomainModel) {
            DbORDomainModel domainModel = (DbORDomainModel) model;
            DbORDatabase database = domainModel.getDeploymentDatabase();
            if (database != null) {
                action = (action == LEAVE_TS_ACTION) ? LEAVE_TS_ACTION : pg2UnlinkDbBtn
                        .isSelected() ? UNLINK_ACTION : CONVERT_ACTION;
                buildMessagePage4(modelList, writer, indentation, database, model, action);
            }
        } else if (model instanceof DbOROperationLibrary) {
            DbOROperationLibrary library = (DbOROperationLibrary) model;
            DbORDatabase database = library.getDeploymentDatabase();
            if (database != null) {
                action = (action == LEAVE_TS_ACTION) ? LEAVE_TS_ACTION : pg2UnlinkDbBtn
                        .isSelected() ? UNLINK_ACTION : CONVERT_ACTION;
                buildMessagePage4(modelList, writer, indentation, database, model, action);
            }
        } else if (model instanceof DbORDatabase) {
            DbORDatabase database = (DbORDatabase) model;
            action = (action == LEAVE_TS_ACTION) ? LEAVE_TS_ACTION
                    : pg2UnlinkDbBtn.isSelected() ? UNLINK_ACTION : CONVERT_ACTION;
            buildMessagePage4(modelList, writer, indentation, database.getSchema(), database,
                    action);
            buildMessagePage4(modelList, writer, indentation, database.getDefaultDomainModel(),
                    database, action);
            buildMessagePage4(modelList, writer, indentation, database.getUdtModel(), database,
                    action);
            buildMessagePage4(modelList, writer, indentation, database.getOperationLibrary(),
                    database, action);
        } // end if

        // m_selectedDestTarget
    }

    private String getQualifiedName(DbObject object) throws DbException {
        String qualifiedName = object.getName();

        DbObject composite = object.getComposite();
        if ((composite != null) && !(composite instanceof DbRoot)) {
            qualifiedName = getQualifiedName(composite) + "." + qualifiedName;
        }

        return qualifiedName;
    } // end getQualifiedName()

    private String getMessagePage5(DbSMSTargetSystem sourceTs, DbSMSTargetSystem dstTs)
            throws DbException {
        String message;
        String srcName = sourceTs.getName();
        String dstName = dstTs.getName();

        if (srcName.equals(dstName)) {
            message = MessageFormat.format(ABOUT_TO_CONVERT_PATTERN, new Object[] { srcName,
                    sourceTs.getVersion(), dstTs.getVersion() });
        } else {
            message = MessageFormat.format(ALL_THE_SPECIFIC_PROPERTIES, new Object[] { srcName,
                    dstName });
        } // end if

        return message;
    } // end getMessage

    private JPanel createPageX() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        m_textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(m_textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jPanel4.add(scrollPane, BorderLayout.CENTER);
        return panel;
    } // end createPageX

    private void init() {
        previousBtn.setEnabled(false);
        nextBtn.setEnabled(false);
    } // end init()

    private int m_currentPage = 0;

    private void addListeners() {
        final JDialog thisDialog = this;

        previousBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    m_currentPage--;
                    changePage(false); // backward
                } catch (DbException ex) {
                } // end try
            }
        });

        nextBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    m_currentPage++;
                    changePage(true); // forward
                } catch (DbException ex) {
                } // end try
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                thisDialog.dispose();
            }
        });
    } // end addListeners()

    //
    // PRIVATE METHODS
    //
    private void changePage(boolean forward) throws DbException {

        // if forward to page 1, but destination target identical to source
        // target,
        // notify the user and return to page 0
        if (forward) {
            if (m_currentPage == 1) {
                DbSMSTargetSystem sourceTS = m_model.getTargetSystem();
                if (sourceTS.getName().equals(m_selectedDestTarget.getName())) {
                    if (sourceTS.getVersion().equals(m_selectedDestTarget.getVersion())) {
                        String msg = CHOOSE_A_DEST_TARGET_DIFFERENT_FROM;
                        JOptionPane.showMessageDialog(this, msg, ERROR, JOptionPane.ERROR_MESSAGE);
                        m_currentPage = 0;
                    } // end if
                } // end if
            } // end if
        } // end if

        // page 2 is related to data model, skip it if not relevent
        if (m_currentPage == 2) {
            // if a data model
            if (m_model instanceof DbORDataModel) {
                DbORDataModel dataModel = (DbORDataModel) m_model;
                DbORDomainModel udtModel = pg2ConvertDbBtn.isSelected() ? getLinkedDomainModel(
                        m_model, true) : null;
                DbORDomainModel defaultDomainModel = pg2ConvertDbBtn.isSelected() ? getLinkedDomainModel(
                        m_model, false)
                        : null;
                initPage3(dataModel, udtModel, defaultDomainModel);
            } else { // if not a data model
                DbORDataModel linkedDataModel = getLinkedDataModel(m_model);
                // if has no schema or if unlink
                if ((linkedDataModel == null) || (pg2UnlinkDbBtn.isSelected())) {
                    m_currentPage = forward ? 3 : 1; // skip to page 3 if
                    // forward, to page 1 if
                    // backward
                } else {
                    DbORDomainModel udtModel = getLinkedDomainModel(m_model, true);
                    DbORDomainModel defaultDomainModel = getLinkedDomainModel(m_model, false);
                    initPage3(linkedDataModel, udtModel, defaultDomainModel);
                } // end if
            } // end if
        } // end if

        // disable previous button if first page
        previousBtn.setEnabled(m_currentPage > 0);

        if (m_currentPage == 3) {
            initPage4();
        } else if (m_currentPage == 4) {
            initPage5();
        }

        if (m_currentPage == 4) {
            nextBtn.setText(CONVERT);
        } else if (m_currentPage == 5) {
            performConversion();
        } else {
            nextBtn.setText(NEXT + " >");
        }

        cardLayout.show(mainCardPanel, Integer.toString(m_currentPage));
    } // changePage ()

    // returns null if not linked to a data model
    private DbORDataModel getLinkedDataModel(DbORModel model) throws DbException {
        DbORDataModel linkedDataModel = null;

        if (model instanceof DbORDatabase) {
            DbORDatabase database = (DbORDatabase) model;
            linkedDataModel = database.getSchema();
        } else if (model instanceof DbORDomainModel) {
            DbORDomainModel domainModel = (DbORDomainModel) model;
            DbORDatabase database = domainModel.getDeploymentDatabase();
            if (database != null) {
                linkedDataModel = database.getSchema();
            }
        } else if (model instanceof DbOROperationLibrary) {
            DbOROperationLibrary library = (DbOROperationLibrary) model;
            DbORDatabase database = library.getDeploymentDatabase();
            if (database != null) {
                linkedDataModel = database.getSchema();
            }
        } // end if

        return linkedDataModel;
    } // end isLinkedToDataModel()

    // returns null if not linked to a data model
    private DbORDomainModel getLinkedDomainModel(DbORModel model, boolean getUDT)
            throws DbException {
        DbORDomainModel linkedDomainModel = null;

        if (model instanceof DbORDomainModel) {
            linkedDomainModel = (DbORDomainModel) model;
        } else {

            // find linked database, if any
            DbORDatabase database = null;
            if (model instanceof DbORDatabase) {
                database = (DbORDatabase) model;
            } else if (model instanceof DbORDataModel) {
                DbORDataModel dataModel = (DbORDataModel) model;
                database = dataModel.getDeploymentDatabase();
            } else if (model instanceof DbOROperationLibrary) {
                DbOROperationLibrary library = (DbOROperationLibrary) model;
                database = library.getDeploymentDatabase();
            } // end if

            // if linked to a database
            if (database != null) {
                linkedDomainModel = getUDT ? database.getUdtModel() : database
                        .getDefaultDomainModel();
            } // end if

        } // end if

        return linkedDomainModel;
    } // end getLinkedDomainModel()

    private void setPage2OptionText(DbORModel model, JLabel question, JRadioButton radioBtn1,
            JRadioButton radioBtn2) throws DbException {
        if (model instanceof DbORDatabase) {
            DbORDatabase db = (DbORDatabase) model;
            if ((db.getSchema() == null) && (db.getDefaultDomainModel() == null)
                    && (db.getOperationLibrary() == null)) {
                m_linkedToOtherModel = false;
                question.setText(HAS_NO_SCHEMA);
                radioBtn1.setEnabled(false);
                radioBtn2.setEnabled(false);
            } else {
                m_linkedToOtherModel = true;
                question.setText(HAS_A_SCHEMA);
            }
            radioBtn1.setText(UNLINK_SCHEMA);
            radioBtn2.setText(CONVERT_THE_SCHEMA);

        } else if (model instanceof DbORDataModel) {
            DbORDataModel datamodel = (DbORDataModel) model;
            DbORDatabase db = datamodel.getDeploymentDatabase();
            String name = datamodel.getName();
            setOptionText2(db, false, question, radioBtn1, radioBtn2);
        } else if (model instanceof DbORDomainModel) {
            DbORDomainModel domainmodel = (DbORDomainModel) model;
            DbORDatabase db = domainmodel.getDeploymentDatabase();
            String name = domainmodel.getName();
            setOptionText2(db, false, question, radioBtn1, radioBtn2);
        } else if (model instanceof DbOROperationLibrary) {
            DbOROperationLibrary library = (DbOROperationLibrary) model;
            DbORDatabase db = library.getDeploymentDatabase();
            String name = library.getName();
            setOptionText2(db, true, question, radioBtn1, radioBtn2);
        } // end if

        // String msg = MessageFormat.format(KEEP_A_BACKUP_COPY, new Object[]
        // {model.getName()});
        // checkBox.setText(msg);
    } // end setOptionText

    private void setOptionText(DbORDatabase db, String modelName, JLabel question,
            JRadioButton radioBtn1, JRadioButton radioBtn2) {
        if (db == null) {
            m_linkedToOtherModel = false;
            String msg = MessageFormat.format(MODEL_HAS_NO_DEPLOYMENT_DB,
                    new Object[] { modelName });
            question.setText(msg);
            radioBtn1.setEnabled(false);
            radioBtn2.setEnabled(false);
        } else {
            m_linkedToOtherModel = true;
            String msg = MessageFormat
                    .format(MODEL_HAS_A_DEPLOYMENT_DB, new Object[] { modelName });
            question.setText(msg);
        }

        radioBtn1.setText(UNLINK_THE_DEPLOYMENT_DATABASE);
        radioBtn2.setText(CONVERT_THE_DEPLOYMENT_DATABASE);
    } // end setOptionText()

    private void setOptionText2(DbORDatabase db, boolean isLibrary, JLabel question,
            JRadioButton radioBtn1, JRadioButton radioBtn2) {
        if (db == null) {
            m_linkedToOtherModel = false;
            question.setText(isLibrary ? LIBRARY_HAS_NO_DEPLOYMENT_DB : MODEL_HAS_NO_DEPLOYMENT_DB);
            radioBtn1.setEnabled(false);
            radioBtn2.setEnabled(false);
        } else {
            m_linkedToOtherModel = true;
            question.setText(isLibrary ? LIBRARY_HAS_A_DEPLOYMENT_DB : MODEL_HAS_A_DEPLOYMENT_DB);
        }

        radioBtn1.setText(UNLINK_THE_DEPLOYMENT_DATABASE);
        radioBtn2.setText(CONVERT_THE_DEPLOYMENT_DATABASE);
    } // end setOptionText()

    private void findDomainModels(ArrayList list, DbObject composite, DbSMSTargetSystem destTarget)
            throws DbException {
        DbRelationN relN = composite.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORDomainModel.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORDomainModel model = (DbORDomainModel) dbEnum.nextElement();
            DbSMSTargetSystem ts = model.getTargetSystem();
            if (ts.equals(destTarget)) {
                list.add(model);
            }
        } // end while
        dbEnum.close();

        dbEnum = relN.elements(DbSMSUserDefinedPackage.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSUserDefinedPackage udp = (DbSMSUserDefinedPackage) dbEnum.nextElement();
            findDomainModels(list, udp, destTarget);
        } // end while
        dbEnum.close();

    } // end findDomainModels()

    private void performConversion() throws DbException {
        // Get conversion options, from GUI components
        int params = 0;
        params += pg2UnlinkDbBtn.isSelected() ? ChangingTargetSystem.UNLINK_RELATED_MODELS : 0;
        params += pg2ModelBackupChkBox.isSelected() ? ChangingTargetSystem.BACKUP_COPY : 0;
        params += pg3ConvertSubModelBtn.isSelected() ? ChangingTargetSystem.CONVERT_SUB_MODELS : 0;
        params += pg3CreateItemsBtn.isSelected() ? ChangingTargetSystem.CREATE_NEW_ITEMS : 0;
        params += pg3UseSameItemsBtn.isSelected() ? ChangingTargetSystem.REUSE_SAME_ITEMS : 0;

        ChangingTargetSystem.ChangingTargetSystemOptions options = new ChangingTargetSystem.ChangingTargetSystemOptions(
                m_model, m_domainModel, m_selectedDestTarget, params);

        // launch a conversion operation
        ChangingTargetSystem changingTS = new ChangingTargetSystem(m_baseTitle, options);
        changingTS.execute();

        // close the wizard
        this.dispose();
    }

    //
    // UNIT TEST
    //
    public static void main(String args[]) throws DbException {
        // create frame
        JFrame mainframe = new JFrame(""); // NOT LOCALIZABLE, unit test
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ChangingTargetSystemWizard wizard = new ChangingTargetSystemWizard(mainframe, "Convert to",
                true, null); // NOT LOCALIZABLE, unit
        // test

        boolean test1 = false;
        if (test1) {
            JPanel page1 = wizard.createPage1();
            JPanel page2 = wizard.createPage2();
            JPanel page3 = wizard.createPage3();
            JPanel page4 = wizard.createPage4();

            mainframe.getContentPane().add(page4);
            mainframe.pack();
            mainframe.setVisible(true);
        } else {
            wizard.setVisible(true);
        } // end if

    } // end main()
} // end ChangingTargetSystemWizard
