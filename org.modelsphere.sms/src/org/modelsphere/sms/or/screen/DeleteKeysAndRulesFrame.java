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
package org.modelsphere.sms.or.screen;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.or.actions.DeleteForeignKeysAction;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.international.LocaleMgr;

public class DeleteKeysAndRulesFrame extends JDialog implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final boolean REFERENTIAL_RULE_VISIBLE = false; //for this version, do not show referential rules

    private DeleteKeysOptions m_options;
    private int[] m_occurrences;

    private JCheckBox pkBox, ukBox, fkBox, referentialRuleBox;
    private JCheckBox primaryColumnBox, uniqueColumnBox, box3a;
    private JCheckBox insertRuleBox, updateRuleBox, deleteRuleBox;
    private JButton okButton, cancelButton;

    public DeleteKeysAndRulesFrame(JFrame frame, String title, int[] occurrences)
            throws DbException {
        super(frame, title, true);
        m_occurrences = occurrences;
        m_options = new DeleteKeysOptions();

        initContents();
        initValues();
        addListeners();

        this.pack();
        this.setLocationRelativeTo(frame);
    }

    // has canceled
    private boolean cancelled = true;

    public boolean hasCancelled() {
        return cancelled;
    }

    private void initContents() {
        setLayout(new GridBagLayout());
        int row = 0;

        JLabel titleLabel = new JLabel(LocaleMgr.action.getString("DeleteTheFollowingItems"));
        add(titleLabel, new GridBagConstraints(0, row, 3, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(6, 6, 0, 6), 0, 0));
        row++;

        // Delete referential rules
        String pattern = LocaleMgr.action.getString("Delete0RefRuleFoundInDataModel");
        String msg = MessageFormat
                .format(pattern,
                        new Object[] { m_occurrences[DeleteForeignKeysAction.KeyAndRules.RULES
                                .ordinal()] });
        referentialRuleBox = new JCheckBox(msg);
        add(referentialRuleBox, new GridBagConstraints(0, row, 3, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(6, 6, 0, 6), 0, 0));
        row++;

        // Delete insert rules
        pattern = LocaleMgr.action.getString("Delete0");
        msg = MessageFormat.format(pattern, new Object[] { DbORAssociationEnd.fInsertRule
                .getGUIName() });
        insertRuleBox = new JCheckBox(msg);
        add(insertRuleBox, new GridBagConstraints(0, row, 3, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 36, 0, 6), 0, 0));
        row++;

        msg = MessageFormat.format(pattern, new Object[] { DbORAssociationEnd.fUpdateRule
                .getGUIName() });
        updateRuleBox = new JCheckBox(msg);
        add(updateRuleBox, new GridBagConstraints(0, row, 3, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 36, 0, 6), 0, 0));
        row++;

        msg = MessageFormat.format(pattern, new Object[] { DbORAssociationEnd.fDeleteRule
                .getGUIName() });
        deleteRuleBox = new JCheckBox(msg);
        add(deleteRuleBox, new GridBagConstraints(0, row, 3, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 36, 0, 6), 0, 0));
        row++;

        // Delete FKs
        pattern = LocaleMgr.action.getString("Delete0FKFoundInDataModel");
        msg = MessageFormat.format(pattern,
                new Object[] { m_occurrences[DeleteForeignKeysAction.KeyAndRules.FK.ordinal()] });
        fkBox = new JCheckBox(msg);
        add(fkBox, new GridBagConstraints(0, row, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 6, 0, 6), 0, 0));
        row++;

        //Delete foreign column
        msg = LocaleMgr.action.getString("DeleteForeignColumns");
        box3a = new JCheckBox(msg);
        add(box3a, new GridBagConstraints(0, row, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 36, 0, 6), 0, 0));
        row++;

        // Delete PKs
        pattern = LocaleMgr.action.getString("Delete0PKFoundInDataModel");
        msg = MessageFormat.format(pattern,
                new Object[] { m_occurrences[DeleteForeignKeysAction.KeyAndRules.PK.ordinal()] });
        pkBox = new JCheckBox(msg);
        add(pkBox, new GridBagConstraints(0, row, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 6, 0, 6), 0, 0));
        row++;

        //Delete primary column
        msg = LocaleMgr.action.getString("DeletePrimaryColumns");
        primaryColumnBox = new JCheckBox(msg);
        add(primaryColumnBox, new GridBagConstraints(0, row, 3, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 36, 0, 6), 0, 0));
        row++;

        // Delete UKs
        pattern = LocaleMgr.action.getString("Delete0UKFoundInDataModel");
        msg = MessageFormat.format(pattern,
                new Object[] { m_occurrences[DeleteForeignKeysAction.KeyAndRules.UK.ordinal()] });
        ukBox = new JCheckBox(msg);
        add(ukBox, new GridBagConstraints(0, row, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 6, 0, 6), 0, 0));
        row++;

        //Delete unique column
        msg = LocaleMgr.action.getString("DeleteUniqueColumns");
        uniqueColumnBox = new JCheckBox(msg);
        add(uniqueColumnBox, new GridBagConstraints(0, row, 3, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 36, 0, 6), 0, 0));
        row++;

        // OK and Cancel
        okButton = new JButton(LocaleMgr.screen.getString("OK"));
        cancelButton = new JButton(LocaleMgr.screen.getString("Cancel"));
        JPanel controlBtnPanel = new JPanel();
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.setHgap(5);
        controlBtnPanel.setLayout(gridLayout1);
        controlBtnPanel.add(okButton, null);
        controlBtnPanel.add(cancelButton, null);

        add(controlBtnPanel, new GridBagConstraints(0, row, GridBagConstraints.REMAINDER, 1, 0.0,
                0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
                new Insets(30, 30, 6, 6), 0, 0));
        row++;
    }

    private void addListeners() {
        referentialRuleBox.addActionListener(this);
        insertRuleBox.addActionListener(this);
        updateRuleBox.addActionListener(this);
        deleteRuleBox.addActionListener(this);

        pkBox.addActionListener(this);
        ukBox.addActionListener(this);
        fkBox.addActionListener(this);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    private void initValues() {
        //show referential rule
        referentialRuleBox.setVisible(REFERENTIAL_RULE_VISIBLE);
        insertRuleBox.setVisible(REFERENTIAL_RULE_VISIBLE);
        updateRuleBox.setVisible(REFERENTIAL_RULE_VISIBLE);
        deleteRuleBox.setVisible(REFERENTIAL_RULE_VISIBLE);

        //enable referential rule
        boolean atLeastOneRule = m_occurrences[DeleteForeignKeysAction.KeyAndRules.RULES.ordinal()] > 0;
        referentialRuleBox.setEnabled(atLeastOneRule);

        //enable FKs
        boolean atLeastOneFK = m_occurrences[DeleteForeignKeysAction.KeyAndRules.FK.ordinal()] > 0;
        fkBox.setEnabled(atLeastOneFK);

        //enable PKs
        boolean atLeastOnePK = m_occurrences[DeleteForeignKeysAction.KeyAndRules.PK.ordinal()] > 0;
        pkBox.setEnabled(atLeastOnePK);
        primaryColumnBox.setEnabled(atLeastOnePK);

        //enable UKs
        boolean atLeastOneUK = m_occurrences[DeleteForeignKeysAction.KeyAndRules.UK.ordinal()] > 0;
        ukBox.setEnabled(atLeastOneUK);
        uniqueColumnBox.setEnabled(atLeastOneUK);

        updateButtonStatus();
    }

    @Override
    public void actionPerformed(ActionEvent ev) {

        Object src = ev.getSource();
        if (src.equals(pkBox)) {
            updateButtonStatus();
        } else if (src.equals(ukBox)) {
            updateButtonStatus();
        } else if (src.equals(fkBox)) {
            updateButtonStatus();
        } else if (src.equals(referentialRuleBox)) {
            updateButtonStatus();
        } else if (src.equals(insertRuleBox)) {
            updateButtonStatus();
        } else if (src.equals(updateRuleBox)) {
            updateButtonStatus();
        } else if (src.equals(deleteRuleBox)) {
            updateButtonStatus();
        } else if (src.equals(okButton)) {
            updateOptions();
            cancelled = false;
            dispose();
        } else if (src.equals(cancelButton)) {
            cancelled = true;
            dispose();
        } // end if

    } // end actionPerformed()

    private void updateButtonStatus() {
        boolean atLeastOneFK = m_occurrences[DeleteForeignKeysAction.KeyAndRules.FK.ordinal()] > 0;
        boolean atLeastOnePK = m_occurrences[DeleteForeignKeysAction.KeyAndRules.PK.ordinal()] > 0;
        boolean atLeastOneUK = m_occurrences[DeleteForeignKeysAction.KeyAndRules.UK.ordinal()] > 0;

        //referential rules
        insertRuleBox.setEnabled(referentialRuleBox.isSelected());
        updateRuleBox.setEnabled(referentialRuleBox.isSelected());
        deleteRuleBox.setEnabled(referentialRuleBox.isSelected());

        if (atLeastOneFK) {
            boolean enabled = REFERENTIAL_RULE_VISIBLE ? (!referentialRuleBox.isEnabled() || referentialRuleBox
                    .isSelected())
                    : true;
            fkBox.setEnabled(enabled);
        }

        if (referentialRuleBox.isEnabled() && !referentialRuleBox.isSelected()) {
            insertRuleBox.setSelected(false);
            updateRuleBox.setSelected(false);
            deleteRuleBox.setSelected(false);

            if (REFERENTIAL_RULE_VISIBLE) {
                fkBox.setSelected(false);
            }
        }

        //foreign keys
        box3a.setEnabled(atLeastOneFK && fkBox.isSelected());

        if (fkBox.isEnabled() && !fkBox.isSelected()) {
            box3a.setSelected(false);
            pkBox.setSelected(false);
            ukBox.setSelected(false);
        }

        boolean referentialRuleDisabled = REFERENTIAL_RULE_VISIBLE ? !referentialRuleBox
                .isEnabled() : true;

        //primary and unique keys 
        if (atLeastOnePK) {
            boolean parentDisabled = referentialRuleDisabled && !fkBox.isEnabled();
            pkBox.setEnabled(parentDisabled || fkBox.isSelected());
            primaryColumnBox.setEnabled(pkBox.isSelected());
        }

        if (atLeastOneUK) {
            boolean parentDisabled = referentialRuleDisabled && !fkBox.isEnabled();
            ukBox.setEnabled(parentDisabled || fkBox.isSelected());
            uniqueColumnBox.setEnabled(ukBox.isSelected());
        }

        if (!pkBox.isSelected()) {
            primaryColumnBox.setSelected(false);
        }

        if (!ukBox.isSelected()) {
            uniqueColumnBox.setSelected(false);
        }

        boolean atLeastOneRuleSelected = insertRuleBox.isSelected() || updateRuleBox.isSelected()
                || deleteRuleBox.isSelected();
        boolean atLeastOneSelected = atLeastOneRuleSelected || pkBox.isSelected()
                || ukBox.isSelected() || fkBox.isSelected();
        okButton.setEnabled(atLeastOneSelected);
        cancelButton.setText(atLeastOneSelected ? LocaleMgr.screen.getString("Cancel")
                : LocaleMgr.screen.getString("Close"));
    }

    private void updateOptions() {
        m_options.deletePrimaryKeys = pkBox.isSelected();
        m_options.deletePrimaryColumns = primaryColumnBox.isSelected();

        m_options.deleteUniqueKeys = ukBox.isSelected();
        m_options.deleteUniqueColumns = uniqueColumnBox.isSelected();

        m_options.deleteForeignKeys = fkBox.isSelected();
        m_options.deleteForeignColumns = box3a.isSelected();

        m_options.deleteRules = referentialRuleBox.isSelected();
        m_options.deleteInsertRules = insertRuleBox.isSelected();
        m_options.deleteUpdateRules = updateRuleBox.isSelected();
        m_options.deleteDeleteRules = deleteRuleBox.isSelected();
    }

    public DeleteKeysOptions getOptions() {
        return m_options;
    }

    //
    // inner classes
    //
    public static class DeleteKeysOptions {
        public boolean deletePrimaryKeys;
        public boolean deletePrimaryColumns;
        public boolean deleteUniqueKeys;
        public boolean deleteUniqueColumns;
        public boolean deleteForeignKeys;
        public boolean deleteForeignColumns;
        public boolean deleteRules;
        public boolean deleteInsertRules;
        public boolean deleteUpdateRules;
        public boolean deleteDeleteRules;
    }

    // *************
    // DEMO FUNCTION
    // *************
    private static void createAndShowGUI() throws DbException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
        }

        // Create and set up the window.
        JFrame frame = new JFrame("GeneratePrimaryKeysDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DbORDataModel model = null;
        int[] occurrences = new int[] { 2, 2, 3, 4 };
        JDialog dialog = new DeleteKeysAndRulesFrame(frame, "Demo", occurrences);
        dialog.setVisible(true);
    }

    // Run the demo
    //
    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (DbException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
