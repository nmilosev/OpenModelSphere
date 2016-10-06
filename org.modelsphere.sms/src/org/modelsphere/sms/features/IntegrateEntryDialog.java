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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbRoot;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.DbTreeLookupDialog;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModelListener;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DbApplication;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.integrate.IntegrateScopeDialog;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.Application;
import org.modelsphere.sms.SMSIntegrateModel;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.db.DbOOPackage;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.screen.SMSTreeLookupDialog;

public class IntegrateEntryDialog extends JDialog {
    private JPanel containerPanel = new JPanel();
    private JTextField modelTxtField1 = new JTextField();
    private JLabel modelLabel1 = new JLabel(LocaleMgr.screen.getString("Model1"));
    private JTextField modelTxtField2 = new JTextField();
    private JLabel modelLabel2 = new JLabel(LocaleMgr.screen.getString("Model2"));
    private JButton scopeButton = new JButton(LocaleMgr.screen.getString("ScopeDots"));
    private JTextField scopeFileTField = new JTextField(LocaleMgr.screen
            .getString("DefaultScopeFile"));

    private JPanel displayOptionPanel = new JPanel();
    private TitledBorder titledBorder1 = new TitledBorder(LocaleMgr.screen
            .getString("DisplayOptions"));
    private ButtonGroup radioGroup = new ButtonGroup();
    private JRadioButton nameRadioBtn = new JRadioButton(LocaleMgr.screen.getString("Name"));
    private JRadioButton physNameRadioBtn = new JRadioButton(LocaleMgr.screen
            .getString("PhysicalName"));

    private JPanel externalOptionPanel = new JPanel();
    private TitledBorder titledBorder2 = new TitledBorder(LocaleMgr.screen
            .getString("ExternalOptions"));
    private ButtonGroup radioGroup2 = new ButtonGroup();
    private JRadioButton referenceRadioBtn = new JRadioButton(LocaleMgr.screen
            .getString("Reference"));
    private JRadioButton identifierRadioBtn = new JRadioButton(LocaleMgr.screen
            .getString("Identifier"));

    private JPanel controlBtnPanel = new JPanel();
    private JButton nextButton = new JButton(LocaleMgr.screen.getString("Next"));
    private JButton cancelButton = new JButton(LocaleMgr.screen.getString("Cancel"));
    private JButton advancedButton = new JButton(LocaleMgr.screen.getString("Advanced"));
    private JButton helpButton = new JButton(LocaleMgr.screen.getString("Help"));
    private GridLayout gridLayout1 = new GridLayout();

    private DbObject semObj1 = null;
    private DbObject semObj2 = null;
    private CheckTreeNode fieldTree = null;
    private IntegrateScopeDialog scopeDlg;
    private boolean IntegrationCanceled = true; // Because the close box cannot
    // be trapped
    private File scopeParamFile = null;
    private String scopeParamFileName = null;
    private JButton model2SelectBtn = new JButton();
    private SMSIntegrateModel model = null;
    private String kLookupTitle = LocaleMgr.screen.getString("SelectModel2");

    private static final String kLibrary1 = LocaleMgr.screen.getString("Library1");
    private static final String kLibrary2 = LocaleMgr.screen.getString("Library2");
    private static final String kPackage1 = LocaleMgr.screen.getString("Package1");
    private static final String kPackage2 = LocaleMgr.screen.getString("Package2");

    public IntegrateEntryDialog(DbObject aSemObj, String title, boolean modal) {
        super(ApplicationContext.getDefaultMainFrame(), title, modal);

        this.semObj1 = aSemObj;
        jbInit();
        this.pack();
        Dimension dim = this.getSize();
        setSize(new Dimension(Math.max(700, dim.width), dim.height));
        this.setLocationRelativeTo(ApplicationContext.getDefaultMainFrame());
    }

    public IntegrateEntryDialog(DbObject aSemObj, String title) {
        this(aSemObj, title, true);
    }

    void jbInit() {
        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        // Graphic Design Utility. Keep it into comments when modification are
        // done.

        /*
         * titledBorder1 = new TitledBorder("Display Options"); // NOT LOCALIZABLE - commented
         * modelLabel1.setText("Model 1:"); // NOT LOCALIZABLE - commented
         * nextButton.setText("Next"); // NOT LOCALIZABLE - commented
         * cancelButton.setText("Cancel"); // NOT LOCALIZABLE - commented
         * helpButton.setText("Help"); // NOT LOCALIZABLE - commented
         * modelLabel2.setText("Model 2:"); // NOT LOCALIZABLE - commented
         * nameRadioBtn.setText("Name"); // NOT LOCALIZABLE - commented
         * physNameRadioBtn.setText("Physical Name"); // NOT LOCALIZABLE - commented
         * scopeButton.setText(" Scope ..."); // NOT LOCALIZABLE - commented
         * externalOptionPanel.setVisible(true);
         */// NOT LOCALIZABLE - commented
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        containerPanel.setLayout(new GridBagLayout());
        gridLayout1.setHgap(5);
        controlBtnPanel.setLayout(gridLayout1);
        displayOptionPanel.setBorder(titledBorder1);
        displayOptionPanel.setLayout(new GridBagLayout());
        externalOptionPanel.setBorder(titledBorder2);
        externalOptionPanel.setLayout(new GridBagLayout());
        model2SelectBtn.setText("..."); // NOT LOCALIZABLE
        getContentPane().add(containerPanel);
        scopeFileTField.setEditable(false);
        modelTxtField1.setEditable(false);
        modelTxtField2.setEditable(false);
        scopeButton.setEnabled(false);
        nextButton.setEnabled(false);
        if (semObj1 instanceof DbOROperationLibrary) {
            modelLabel1.setText(kLibrary1);
            modelLabel2.setText(kLibrary2);
            kLookupTitle = LocaleMgr.screen.getString("SelectLibrary2");
        } else if (semObj1 instanceof DbOOPackage) {
            modelLabel1.setText(kPackage1);
            modelLabel2.setText(kPackage2);
            kLookupTitle = LocaleMgr.screen.getString("SelectPackage2");
        }

        // Main Panel
        containerPanel.add(modelLabel1, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 6, 12), 0, 0));
        containerPanel.add(modelTxtField1, new GridBagConstraints(2, 0,
                GridBagConstraints.REMAINDER, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(12, 0, 6, 12), 10, 5));

        containerPanel.add(modelLabel2, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 12, 12, 12), 0, 0));
        containerPanel.add(modelTxtField2, new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 12, 0),
                10, 5));
        containerPanel.add(model2SelectBtn, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 12, 12, 12), 0, 0));

        containerPanel.add(displayOptionPanel, new GridBagConstraints(0, 2,
                GridBagConstraints.REMAINDER, 1, 1.0, 0.5, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(6, 10, 6, 10), 0, 1));

        containerPanel.add(scopeButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 12, 12, 0), 0, 0));
        containerPanel.add(scopeFileTField, new GridBagConstraints(1, 3,
                GridBagConstraints.REMAINDER, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 12, 12, 12), 0, 5));

        containerPanel.add(externalOptionPanel, new GridBagConstraints(0, 4,
                GridBagConstraints.REMAINDER, 1, 1.0, 0.5, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(6, 10, 6, 10), 0, 1));

        try {
            semObj1.getDb().beginTrans(Db.READ_TRANS);
            modelTxtField1.setText(createDisplayName(semObj1));
            semObj1.getDb().commitTrans();
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        }

        // Display Option Panel
        displayOptionPanel.add(nameRadioBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0, 0));
        displayOptionPanel.add(physNameRadioBtn, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 6, 12), 0, 0));
        displayOptionPanel.add(Box.createVerticalGlue(),
                new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        radioGroup.add(nameRadioBtn);
        radioGroup.add(physNameRadioBtn);
        nameRadioBtn.setSelected(true);
        physNameRadioBtn.setEnabled(!(semObj1 instanceof DbOOAbsPackage));

        // External Option Panel
        externalOptionPanel.add(identifierRadioBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0, 0));
        externalOptionPanel.add(referenceRadioBtn, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 6, 12), 0, 0));
        externalOptionPanel.add(Box.createVerticalGlue(),
                new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        radioGroup2.add(identifierRadioBtn);
        radioGroup2.add(referenceRadioBtn);
        identifierRadioBtn.setSelected(true);

        // Control Button Panel
        containerPanel.add(controlBtnPanel, new GridBagConstraints(0, 5,
                GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(12, 12, 12, 12), 0, 0));

        controlBtnPanel.add(advancedButton, null);
        controlBtnPanel.add(nextButton, null);
        controlBtnPanel.add(cancelButton, null);
        // HIDEHELPforV1//controlBtnPanel.add(helpButton, null);

        model2SelectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectSecondModel()) {
                    try {
                        semObj2.getDb().beginTrans(Db.READ_TRANS);
                        modelTxtField2.setText(createDisplayName(semObj2));
                        semObj2.getDb().commitTrans();
                        fieldTree = SMSIntegrateModel.getFieldTree(semObj1, semObj2);
                        manageButtons();
                    } catch (Exception ex) {
                        org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                                ApplicationContext.getDefaultMainFrame(), ex);
                    }
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                IntegrationCanceled = false;
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                IntegrationCanceled = true;
                dispose();
            }
        });

        helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        advancedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                advancedButton.setVisible(false);
                externalOptionPanel.setVisible(true);
                validate();
                setSize(getWidth(), getPreferredSize().height);
                synchronized(getTreeLock()){
                	validateTree();
                }
            }
        });

        scopeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scopeDlg = new IntegrateScopeDialog(fieldTree, scopeParamFile,
                        IntegrateScopeDialog.INTEGRATION);
                scopeDlg.setVisible(true);
                scopeParamFile = scopeDlg.getScopeFile();
                if (scopeParamFile != null) {
                    scopeFileTField.setText(scopeParamFile.getName());
                }
            }
        });

        getRootPane().setDefaultButton(nextButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        externalOptionPanel.setVisible(false);
        new HotKeysSupport(this, cancelButton, helpButton);
    }

    private final String createDisplayName(DbObject selModel) throws DbException {
        if (selModel == null)
            return "";
        String name = selModel.getName();
        for (DbObject parent = selModel.getComposite(); parent != null
                && !(parent instanceof DbRoot); parent = parent.getComposite()) {
            String parentName = parent.getName();
            if (parentName != null && (name.length() + parentName.length()) < 140)
                name = (parentName == null ? "" : parentName) + "." + name;
            else {
                name = "..." + name;
                break;
            }
        }
        return name;
    }

    public final boolean isCanceled() {
        return IntegrationCanceled;
    }

    public final boolean isPhysNameSelected() {
        return physNameRadioBtn.isSelected();
    }

    public final DbObject getSecondSemObj() {
        return semObj2;
    }

    public final CheckTreeNode getFieldTree() {
        return fieldTree;
    }

    public final boolean isExternalMatchByName() {
        return identifierRadioBtn.isSelected();
    }

    private void manageButtons() {
        scopeButton.setEnabled(modelTxtField2 != null);
        nextButton.setEnabled(modelTxtField2 != null);
    }

    /**
     * Display a tree dialog allowing the user to select the second model to integrate
     */
    private boolean selectSecondModel() {
        DbObject semObjTmp = null;
        DbProject[] projects = DbApplication.getProjects();
        MetaClass[] metaClasses = getCompatibleMetaClasses(semObj1);

        DbTreeModelListener modelListener = new DbTreeModelListener() {
            public boolean isSelectable(DbObject dbo) throws DbException {
                return (semObj1 != dbo);
            }

            public String getDisplayText(DbObject dbo, Object callingObject) throws DbException {
                String name = "";
                if (dbo instanceof DbORDataModel
                        && (((DbORDataModel) dbo).getDeploymentDatabase() != null)) {
                    name += "(" + ((DbORDataModel) dbo).getDeploymentDatabase().getName() + ") ";
                } else if (dbo instanceof DbOROperationLibrary
                        && (((DbOROperationLibrary) dbo).getDeploymentDatabase() != null)) {
                    name += "(" + ((DbOROperationLibrary) dbo).getDeploymentDatabase().getName()
                            + ") ";
                } else if (dbo instanceof DbORDomainModel
                        && (((DbORDomainModel) dbo).getDeploymentDatabase() != null)) {
                    name += "(" + ((DbORDomainModel) dbo).getDeploymentDatabase().getName() + ") ";
                }
                name += dbo.getName() + SMSIntegrateModel.getTSSuffix(dbo);
                return name;
            }
        };

        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

        if (terminologyUtil.isDataModel(semObj1)) {
            int mode = terminologyUtil.getModelLogicalMode(semObj1);
            semObjTmp = (DbObject) SMSTreeLookupDialog.selectOne(this, kLookupTitle, projects,
                    metaClasses, modelListener, null, null, mode);
        } else
            semObjTmp = (DbObject) DbTreeLookupDialog.selectOne(this, kLookupTitle, projects,
                    metaClasses, modelListener, null, null);

        if (semObjTmp != null)
            semObj2 = semObjTmp;

        return (semObjTmp != null);
    }

    private MetaClass[] getCompatibleMetaClasses(DbObject semObj) {
        MetaClass[] metaClasses = new MetaClass[] { null };

        if (semObj instanceof DbORDataModel) {
            metaClasses = new MetaClass[] { DbORDataModel.metaClass };
        } else if (semObj instanceof DbOROperationLibrary) {
            metaClasses = new MetaClass[] { DbOROperationLibrary.metaClass };
        } else if (semObj instanceof DbORDomainModel || semObj instanceof DbORCommonItemModel
                || semObj instanceof DbORDatabase || semObj instanceof DbOOAbsPackage
                || semObj instanceof DbBEModel) {
            metaClasses = new MetaClass[] { semObj.getMetaClass() };
        }
        return metaClasses;
    }

    // *************
    // DEMO FUNCTION
    // *************

    private static void runDemo() throws DbException {
        Application.initMeta();
        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        DbObject obj = null;
        boolean modal = true;
        IntegrateEntryDialog dialog = new IntegrateEntryDialog(obj, "Integration", modal);
        dialog.setVisible(true);
        boolean done = false;
        do {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }

            if (!dialog.isShowing()) {
                dialog.dispose();
                dialog = null;
                done = true;
            }
        } while (!done);
        System.exit(0);
    } // end runDemo()

    /*
     * //Run the demo public static void main(String[] args) { runDemo(); } //end main()
     */

} // end IntegrateEntryPoint
