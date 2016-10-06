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

package org.modelsphere.sms.or.screen;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.DbLookupDialog;
import org.modelsphere.jack.baseDb.screen.DbTreeLookupDialog;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.util.AnySemObject;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.international.LocaleMgr;

public class GenerateClassModelOptionFrame extends JDialog {
    private static final String kUnspecified = LocaleMgr.screen.getString("Unspecified");
    private static final String kLinkModel = LocaleMgr.screen.getString("linkModel");
    private static final String kGenerateUnder = LocaleMgr.screen.getString("GenerateUnder");

    private DbSMSProject project = null;
    public boolean cancel = true;
    private DbORDataModel dataModel = null;
    public DbSMSLinkModel linkModel = null;
    public boolean createLinks = true;
    public JVVisibility visibility = null;
    public DbOOAbsPackage destinationPkg = null;

    JPanel containerPanel = new JPanel();
    JPanel controlBtnPanel = new JPanel();
    JLabel fieldVisibilityLabel = new JLabel(LocaleMgr.screen.getString("FieldVisibility"));
    JLabel linkModelLabel = new JLabel(kLinkModel + " :");// NOT LOCALIZABLE
    JTextField linkModelTxFld = new JTextField(kUnspecified);
    JLabel genUnderLabel = new JLabel(kGenerateUnder + " :"); // NOT LOCALIZABLE
    JTextField genUnderTxFld = new JTextField(kUnspecified);
    JButton destinationLookUp = new JButton("..."); // NOT LOCALIZABLE
    JButton linkModelLookUp = new JButton("..."); // NOT LOCALIZABLE
    JButton okButton = new JButton(LocaleMgr.screen.getString("Generate"));
    JButton cancelButton = new JButton(LocaleMgr.screen.getString("Cancel"));
    JCheckBox createLinksCheckBox = new JCheckBox(LocaleMgr.screen.getString("CreateLinks"));
    JComboBox visibilityComboBox = new JComboBox(JVVisibility.objectPossibleValues);
    GridLayout gridLayout1 = new GridLayout();

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == createLinksCheckBox) {
                linkModelLabel.setEnabled(createLinksCheckBox.isSelected());
                linkModelTxFld.setEnabled(createLinksCheckBox.isSelected());
                linkModelLookUp.setEnabled(createLinksCheckBox.isSelected());
                okButton.setEnabled(isValideForGeneration());
            } else if (e.getSource() == linkModelLookUp) {
                try {
                    project.getDb().beginTrans(Db.READ_TRANS); // close by
                    // DbLookupDialog
                    DefaultComparableElement item = DbLookupDialog.selectDbo(
                            GenerateClassModelOptionFrame.this, kLinkModel, null, project.getDb(),
                            AnySemObject.getAllDbSMSLinkModel(project), linkModel, null, true);
                    if (item != null) {
                        project.getDb().beginTrans(Db.READ_TRANS);
                        linkModel = (DbSMSLinkModel) item.object;
                        linkModelTxFld.setText(linkModel.getName());
                        okButton.setEnabled(isValideForGeneration());
                        project.getDb().commitTrans();
                    }
                } catch (DbException dbE) {
                    org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                            ApplicationContext.getDefaultMainFrame(), dbE);
                }
            } else if (e.getSource() == destinationLookUp) {
                Object item = DbTreeLookupDialog.selectOne(GenerateClassModelOptionFrame.this,
                        kGenerateUnder, new DbObject[] { project }, new MetaClass[] {
                                DbJVClassModel.metaClass, DbJVPackage.metaClass }, null, null,
                        destinationPkg);
                if (item != null) {
                    try {
                        project.getDb().beginTrans(Db.READ_TRANS);
                        destinationPkg = (DbOOAbsPackage) item;
                        genUnderTxFld.setText(destinationPkg.getName());
                        okButton.setEnabled(isValideForGeneration());
                        project.getDb().commitTrans();
                    } catch (DbException dbE) {
                        org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                                ApplicationContext.getDefaultMainFrame(), dbE);
                    }
                }

            } else if (e.getSource() == okButton) {
                createLinks = createLinksCheckBox.isSelected();
                visibility = (JVVisibility) visibilityComboBox.getSelectedItem();
                cancel = false;
                dispose();

            } else if (e.getSource() == cancelButton) {
                dispose();
            }
        }
    };

    public GenerateClassModelOptionFrame(Frame frame, String title, boolean modal,
            DbORDataModel aDataModel) throws DbException {
        super(frame, title, modal);
        jbInit();
        dataModel = aDataModel;
        project = (DbSMSProject) dataModel.getProject();
        linkModel = project.getDefaultLinkModel();
        linkModelTxFld.setText(linkModel == null ? kUnspecified : linkModel.getName());

        okButton.setEnabled(isValideForGeneration());
        visibilityComboBox.setSelectedItem(JVVisibility.getInstance(JVVisibility.PRIVATE));
        this.pack();
        this.setLocationRelativeTo(frame);
    }

    public GenerateClassModelOptionFrame(Frame frame, DbORDataModel aDataModel) throws DbException {
        this(frame, LocaleMgr.screen.getString("GenerateJavaPackage"), true, aDataModel);
    }

    void jbInit() {
        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        // Graphic Design Utility. Keep it into comments when modification are
        // done.

        /*
         * linkModelLabel.setText("Link Model :"); //NOT LOCALIZABLE, jbUnit()
         * okButton.setText("Generate"); //NOT LOCALIZABLE, jbUnit() cancelButton.setText("Cancel");
         * //NOT LOCALIZABLE, jbUnit() createLinksCheckBox.setSelected(true);
         * createLinksCheckBox.setText("Create Links"); //NOT LOCALIZABLE, jbUnit()
         * fieldVisibilityLabel.setText("Field Visibility"); //NOT LOCALIZABLE, jbUnit()
         * linkModelLookUp.setText("..."); genUnderLabel.setText("Generate Under :"); //NOT
         * LOCALIZABLE, jbUnit() destinationLookUp.setText("...");
         */

        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        containerPanel.setLayout(new GridBagLayout());
        getContentPane().add(containerPanel);
        gridLayout1.setHgap(5);
        controlBtnPanel.setLayout(gridLayout1);
        createLinksCheckBox.setSelected(true);
        genUnderTxFld.setEditable(false);
        linkModelTxFld.setEditable(false);

        // Main Panel
        containerPanel.add(genUnderLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 0, 12), 0, 0));
        containerPanel.add(genUnderTxFld, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 0, 6),
                175, 5));
        containerPanel
                .add(destinationLookUp, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(12, 0, 0, 12), 0, 0));
        containerPanel.add(createLinksCheckBox, new GridBagConstraints(0, 1,
                GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 12, 0, 12), 0, 0));
        containerPanel.add(linkModelLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 24, 0, 12), 0, 0));
        containerPanel.add(linkModelTxFld, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 0, 0, 6),
                175, 5));
        containerPanel.add(linkModelLookUp, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 12), 0, 0));
        containerPanel.add(fieldVisibilityLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 5, 12), 0, 0));
        containerPanel.add(visibilityComboBox, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 0, 5, 12), 0, 0));

        // Control Button Panel
        containerPanel.add(controlBtnPanel, new GridBagConstraints(0, 4,
                GridBagConstraints.REMAINDER, 1, 0.0, 1.0, GridBagConstraints.SOUTHEAST,
                GridBagConstraints.NONE, new Insets(17, 12, 12, 12), 0, 0));
        controlBtnPanel.add(okButton, null);
        controlBtnPanel.add(cancelButton, null);

        createLinksCheckBox.addActionListener(actionListener);
        linkModelLookUp.addActionListener(actionListener);
        okButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);
        destinationLookUp.addActionListener(actionListener);

        getRootPane().setDefaultButton(okButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        new HotKeysSupport(this, cancelButton, null);
    }

    private final boolean isValideForGeneration() {
        if (destinationPkg != null) {
            if (createLinksCheckBox.isSelected()) {
                return linkModel != null;
            } else {
                return true;
            }
        } else
            return false;
    }

    // *************
    // DEMO FUNCTION
    // *************
    /*
     * private static void runDemo() { GenerateClassModelOptionFrame dialogue = new
     * GenerateClassModelOptionFrame(null); dialogue.setVisible(true); boolean done = false; do {
     * try { Thread.sleep(200); } catch (InterruptedException ex) {}
     * 
     * if (!dialogue.isShowing()) { dialogue.dispose(); dialogue = null; done = true; } } while (!
     * done); System.exit(0); } //end runDemo()
     * 
     * public static void main(String[] args) { runDemo(); } //end main()
     */
} // end GenerateClassModelOptionFrame
