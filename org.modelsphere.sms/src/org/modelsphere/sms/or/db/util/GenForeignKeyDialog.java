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

You can redistribute and/or modify this particular file even under the
terms of the GNU Lesser General Public License (LGPL) as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License 
(LGPL) along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.or.db.util;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.border.*;

import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.international.LocaleMgr;

public class GenForeignKeyDialog extends JDialog {

    private static final String kUpdateDescriptor = LocaleMgr.screen.getString("UpdateDescriptor");
    private static final String kGenerateBtn = LocaleMgr.screen.getString("GenerateBtn");
    private static final String kGenerateFKTitle = LocaleMgr.screen.getString("GenerateFK");
    private static final String kReportBtn = LocaleMgr.screen.getString("ReportBtn");
    private static final String kCancel = LocaleMgr.screen.getString("Cancel");
    private static final String kExistingOptions = LocaleMgr.screen
            .getString("ExistingForeignColOpt");
    private static final String kDelOrphanCol = LocaleMgr.screen.getString("DelOrphanCol");
    private static final String kKeepOrphanCol = LocaleMgr.screen.getString("KeepOrphanCol");
    private static final String kSetOrphanCol = LocaleMgr.screen.getString("SetOrphanCol");
    private static final String kOrphanPanelTitle = LocaleMgr.screen.getString("OrphanPanelTitle");

    private JPanel containerPanel = new JPanel();
    private JPanel existingOptionPanel = new JPanel();
    private JPanel propagatePanel = new JPanel();
    private JPanel indentInternalPanel = new JPanel();
    private JPanel orphanPanel = new JPanel();
    private ButtonGroup btnGroup = new ButtonGroup();
    private static JRadioButton keepOrphanBtn = new JRadioButton(kKeepOrphanCol);
    private static JRadioButton delOrphanBtn = new JRadioButton(kDelOrphanCol);
    private static JRadioButton setOrphanBtn = new JRadioButton(kSetOrphanCol);
    private boolean keepOrphanBackup;
    private boolean delOrphanBackup;
    private boolean setOrphanBackup;
    private static boolean firstTimeOpen = true;

    private JPanel controlBtnPanel = new JPanel();
    private JButton okButton = new JButton(kGenerateBtn);
    private JButton reportButton = new JButton(kReportBtn);
    private JButton cancelButton = new JButton(kCancel);
    private int[] copyFlagsList;
    private static JCheckBox[] checkBoxes;
    private Boolean[] checkBoxesBackup;
    private int nbChkbox = 0;
    private int buttonSelected = ForeignKey.CANCEL;
    private int orphanRadioBtnSelected = ForeignKey.DELETE_ORPHAN;
    private DbORDataModel m_dataModel;

    public static String buildTitle(String dataModelName) {
        String title = MessageFormat.format(kGenerateFKTitle, new Object[] { dataModelName });
        return title;
    } //end buildTitle()

    public GenForeignKeyDialog(Frame frame, String title, boolean modal, int[] copyFlagsList,
            DbORDataModel dataModel) {
        super(frame, title, modal);
        this.copyFlagsList = copyFlagsList;
        nbChkbox = copyFlagsList.length;
        m_dataModel = dataModel;
        
        jbInit();
        this.pack();
        this.setLocationRelativeTo(frame);
    }

    void jbInit() {

    	int nbCols = 3;
    	int nbRows = (int)Math.ceil((double)nbChkbox / nbCols);
        GridLayout gridLayout2 = new GridLayout(nbRows, nbCols);
        if (firstTimeOpen) {
            checkBoxes = new JCheckBox[nbChkbox];
            for (int i = 0; i < nbChkbox; i++) {
                checkBoxes[i] = new JCheckBox();
                checkBoxes[i].setText(ForeignKey.getAttrLabel(copyFlagsList[i]));
            }
        }

        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        // Graphic Design Utility.  Keep it into comments when modification are done.

        /*
         * okButton.setText("Generate Foreign Keys"); reportButton.setText("Report");
         * cancelButton.setText("Cancel"); keepOrphanBtn.setText("kKeepOrphanCol");
         * delOrphanBtn.setText("kDelOrphanCol"); setOrphanBtn.setText("kSetOrphanCol");
         */
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        containerPanel.setLayout(new GridBagLayout());
        propagatePanel.setLayout(new GridBagLayout());
        indentInternalPanel.setLayout(gridLayout2);
        existingOptionPanel.setBorder(new TitledBorder(kExistingOptions));
        existingOptionPanel.setLayout(new GridBagLayout());
        propagatePanel.setBorder(new TitledBorder(kUpdateDescriptor));
        orphanPanel.setLayout(new GridBagLayout());
        orphanPanel.setBorder(new TitledBorder(kOrphanPanelTitle));
        controlBtnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(containerPanel);

        // Global Option Panel
        containerPanel.add(existingOptionPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(12, 12, 0, 12),
                0, 0));
        // Orphan Panel
        existingOptionPanel.add(orphanPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(7, 12, 0, 12), 0,
                0));

        orphanPanel.add(delOrphanBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0,
                0));
        orphanPanel.add(keepOrphanBtn, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0,
                0));
        orphanPanel.add(setOrphanBtn, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 12, 6, 12), 0,
                0));

        // Propagate Panel
        existingOptionPanel.add(propagatePanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(12, 12, 12, 12),
                0, 0));
        propagatePanel.add(indentInternalPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 12, 6, 0), 0, 0));
        for (int i = 0; i < nbChkbox; i++)
            indentInternalPanel.add(checkBoxes[i], null);
        
        // Naming Options
        JButton namingOptionButton = new ForeignKeyNamingButton(this, m_dataModel); 
        existingOptionPanel.add(namingOptionButton, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(6, 12, 12, 12),
                0, 0));
        namingOptionButton.setVisible(true);

        // Control Button Panel
        containerPanel.add(controlBtnPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(17, 7, 12, 7), 0,
                0));

        btnGroup.add(delOrphanBtn);
        btnGroup.add(keepOrphanBtn);
        btnGroup.add(setOrphanBtn);
        if (firstTimeOpen)
            delOrphanBtn.setSelected(true);
        else {
            if (delOrphanBtn.isSelected())
                orphanRadioBtnSelected = ForeignKey.DELETE_ORPHAN;
            else if (keepOrphanBtn.isSelected())
                orphanRadioBtnSelected = ForeignKey.KEEP_ORPHAN;
            else if (setOrphanBtn.isSelected())
                orphanRadioBtnSelected = ForeignKey.SET_ORPHAN_AS_BASIC;
        }

        //backup options if the user cancel generation
        backUpOptions();
        firstTimeOpen = false;

        keepOrphanBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                orphanRadioBtnSelected = ForeignKey.KEEP_ORPHAN;
            }
        });

        delOrphanBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                orphanRadioBtnSelected = ForeignKey.DELETE_ORPHAN;
            }
        });

        setOrphanBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                orphanRadioBtnSelected = ForeignKey.SET_ORPHAN_AS_BASIC;
            }
        });

        controlBtnPanel.add(reportButton, null);
        controlBtnPanel.add(okButton, null);
        controlBtnPanel.add(cancelButton, null);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonSelected = ForeignKey.GENERATE;
                dispose();
            }
        });

        final JDialog thisDialog = this;
        reportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonSelected = ForeignKey.REPORTONLY;
                int flags = getSelectedFlags();
                int orphanValue = getOrphanOptionSelect();
                boolean resorderPUCol = isReorderPuCols();
                int buttonSelect = getButtonSelect();
                ForeignKey genFKey = new ForeignKey();

                try {
                    genFKey.generate(m_dataModel, getTitle(), flags, orphanValue, resorderPUCol,
                            (buttonSelect == ForeignKey.REPORTONLY));
                } catch (DbException ex) {
                    ExceptionHandler.processUncatchedException(thisDialog, ex);
                } //end try 

                //dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonSelected = ForeignKey.CANCEL;
                restoreOptions();
                dispose();
            }
        });

        getRootPane().setDefaultButton(okButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        new HotKeysSupport(this, cancelButton, null);
    }

    public int getSelectedFlags() {
        int flags = 0;

        for (int i = 0; i < nbChkbox; i++)
            if (checkBoxes[i].isSelected())
                flags |= copyFlagsList[i];

        return flags;
    }

    public int getButtonSelect() {
        return buttonSelected;
    }

    public int getOrphanOptionSelect() {
        return orphanRadioBtnSelected;
    }

    private final void backUpOptions() {
        delOrphanBackup = delOrphanBtn.isSelected();
        keepOrphanBackup = keepOrphanBtn.isSelected();
        setOrphanBackup = setOrphanBtn.isSelected();
        checkBoxesBackup = new Boolean[checkBoxes.length];
        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxesBackup[i] = new Boolean(checkBoxes[i].isSelected());
        }
    }

    public final boolean isReorderPuCols() {
        return true;
    }

    private final void restoreOptions() {
        delOrphanBtn.setSelected(delOrphanBackup);
        keepOrphanBtn.setSelected(keepOrphanBackup);
        setOrphanBtn.setSelected(setOrphanBackup);
        for (int i = 0; i < checkBoxesBackup.length; i++) {
            checkBoxes[i].setSelected(checkBoxesBackup[i].booleanValue());
        }
    }

    // *************
    // DEMO FUNCTION
    // *************
    private static void runDemo() {
        String[] checkboxList = { "Name", "Physical Name", "Type, Length, Nbr.Decimal, Picture",
                "Common Item", };

        /*
         * GenForeignKeyDialog dialog = new GenForeignKeyDialog(null, checkboxList);
         * dialog.setVisible(true); boolean done = false; do { try { Thread.sleep(200); } catch
         * (InterruptedException ex) {}
         * 
         * if (!dialog.isShowing()) { dialog.dispose(); dialog = null; done = true; } } while (!
         * done); System.exit(0);
         */
    } //end runDemo()

    //Run the demo
    /*
     * public static void main(String[] args) { runDemo(); }
     */
    //end main()

} //end GenForeignKeyDialog

