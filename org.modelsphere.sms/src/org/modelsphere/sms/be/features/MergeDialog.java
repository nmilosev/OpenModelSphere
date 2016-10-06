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

package org.modelsphere.sms.be.features;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.DbTreeLookupDialog;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModelListener;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DbApplication;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.international.LocaleMgr;

public final class MergeDialog extends JDialog implements ActionListener {
    private JButton mergeButton = new JButton(LocaleMgr.screen.getString("Merge"));
    private JButton cancelButton = new JButton(LocaleMgr.screen.getString("Cancel"));

    private JLabel sourceLabel = new JLabel(LocaleMgr.screen.getString("MergeProcess"));
    private JLabel targetLabel = new JLabel(LocaleMgr.screen.getString("UnderProcess"));
    private JTextField sourceField = new JTextField(""); // NOT LOCALIZABLE
    private JTextField targetField = new JTextField(""); // NOT LOCALIZABLE
    private JButton targetButton = new JButton("..."); // NOT LOCALIZABLE

    private JCheckBox updateMatchConceptFromSrcChkBox = new JCheckBox(LocaleMgr.screen
            .getString("updateMatchingFromSource"));
    private JCheckBox mergeQualifiersChkBox = new JCheckBox(LocaleMgr.screen
            .getString("mergeQualifiers"));
    private JCheckBox mergeResourcesChkBox = new JCheckBox(LocaleMgr.screen
            .getString("mergeResources"));
    private JCheckBox mergeUnequalCommentsChkBox = new JCheckBox(LocaleMgr.screen
            .getString("mergeUnequalComments"));

    private DbBEUseCase target = null;
    private DbBEUseCase source = null;

    public static boolean update;
    public static boolean mergeQualifiers;
    public static boolean mergeResources;
    public static boolean mergeComments;

    private MergeDialog() {
    }

    private MergeDialog(DbBEUseCase source) {
        super(ApplicationContext.getDefaultMainFrame(), LocaleMgr.screen.getString("MergeTitle"),
                true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.source = source;
        initGUI();
    }

    private void initGUI() {
        getContentPane().setLayout(new GridBagLayout());
        AwtUtil.normalizeComponentDimension(new JButton[] { mergeButton, cancelButton });
        JPanel buttonPanel = new JPanel(new GridBagLayout());

        getContentPane().add(
                sourceLabel,
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(12, 12, 6, 6), 0, 0));
        getContentPane().add(
                sourceField,
                new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.HORIZONTAL, new Insets(12, 0, 6, 6), 100, 0));
        getContentPane().add(
                Box.createHorizontalGlue(),
                new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        getContentPane().add(
                targetLabel,
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(6, 12, 6, 6), 0, 0));
        getContentPane().add(
                targetField,
                new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.HORIZONTAL, new Insets(6, 0, 6, 6), 100, 0));
        getContentPane().add(
                targetButton,
                new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(6, 0, 6, 12), 0, -5));

        getContentPane().add(
                updateMatchConceptFromSrcChkBox,
                new GridBagConstraints(0, 2, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(6, 9, 5, 12), 0, 0));
        getContentPane().add(
                mergeQualifiersChkBox,
                new GridBagConstraints(0, 3, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(0, 30, 0, 11), 0, 0));
        getContentPane().add(
                mergeResourcesChkBox,
                new GridBagConstraints(0, 4, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(0, 30, 0, 11), 0, 0));
        getContentPane().add(
                mergeUnequalCommentsChkBox,
                new GridBagConstraints(0, 5, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(0, 30, 17, 11), 0, 0));

        getContentPane().add(
                Box.createVerticalGlue(),
                new GridBagConstraints(0, 6, 3, 1, 1.0, 1.0, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        buttonPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        buttonPanel.add(mergeButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 12, 12, 6), 0, 0));
        buttonPanel.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 0, 12, 12), 0, 0));
        getContentPane().add(
                buttonPanel,
                new GridBagConstraints(0, 7, 3, 1, 1.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        updateMatchConceptFromSrcChkBox.setSelected(true);
        mergeQualifiersChkBox.setSelected(true);
        mergeResourcesChkBox.setSelected(true);
        mergeUnequalCommentsChkBox.setSelected(true);

        updateMatchConceptFromSrcChkBox.addActionListener(this);

        sourceField.setEditable(false);
        targetField.setEditable(false);
        mergeButton.addActionListener(this);
        cancelButton.addActionListener(this);
        targetButton.addActionListener(this);

        mergeButton.setEnabled(false);

        updateCheckBoxes();

        new HotKeysSupport(this, cancelButton, null);
    }

    private void initValues() throws DbException {
        sourceField.setText(ApplicationContext.getSemanticalModel().getDisplayText(source,
                DbSemanticalObject.LONG_FORM, null, null));
    }

    private static DbBEUseCase selectTargetInternal(DbBEUseCase source, DbBEUseCase target) {
        if (source == null)
            return null;

        MergeDialog dialog = new MergeDialog(source);
        try {
            source.getDb().beginReadTrans();
            dialog.initValues();
            if (target != null) {
                dialog.targetButton.setEnabled(false);
                dialog.targetField.setText(ApplicationContext.getSemanticalModel().getDisplayText(
                        target, DbSemanticalObject.LONG_FORM, null, null));
                dialog.mergeButton.setEnabled(target != null);
            }
            source.getDb().commitTrans();
        } catch (DbException e) {
            dialog.dispose();
            return null;
        } // end try

        dialog.pack();
        Dimension dim = dialog.getSize();
        if (dim != null) {
            dialog.setSize(Math.max(dim.width, 400), Math.max(dim.height, 200));
        }

        AwtUtil.centerWindow(dialog);
        dialog.setVisible(true);

        MergeDialog.update = dialog.updateMatchConceptFromSrcChkBox.isSelected();
        MergeDialog.mergeQualifiers = dialog.mergeQualifiersChkBox.isSelected();
        MergeDialog.mergeComments = dialog.mergeUnequalCommentsChkBox.isSelected();
        MergeDialog.mergeResources = dialog.mergeResourcesChkBox.isSelected();

        return dialog.target;
    }

    // Called by org.modelsphere.sms.be.actions.MergeAction
    public static DbBEUseCase selectTarget(DbBEUseCase source) {
        DbBEUseCase selection = selectTargetInternal(source, null);
        return selection;
    }

    // Called by org.modelsphere.sms.screen.pluginsDbBEUseCaseExternalEditor
    public static DbBEUseCase selectOptions(DbBEUseCase context, DbBEUseCase externalProcess) {
        DbBEUseCase selection = selectTargetInternal(context, externalProcess);
        return externalProcess;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == mergeButton) {
            dispose();
        } else if (source == cancelButton) {
            target = null;
            dispose();
        } else if (source == updateMatchConceptFromSrcChkBox) {
            updateCheckBoxes();
        } else if (source == targetButton) {
            selectTargetUseCase();
            mergeButton.setEnabled(target != null);
            if (target == null) {
                targetField.setText(""); // NOT LOCALIZABLE
            } else {
                try {
                    target.getDb().beginReadTrans();
                    targetField.setText(ApplicationContext.getSemanticalModel().getDisplayText(
                            target, DbSemanticalObject.LONG_FORM, null, null));
                    target.getDb().commitTrans();
                } catch (DbException ex) {
                    target = null;
                    targetField.setText(""); // NOT LOCALIZABLE
                }
            }
        }
    }

    private void updateCheckBoxes() {
        mergeQualifiersChkBox.setEnabled(updateMatchConceptFromSrcChkBox.isSelected());
        mergeResourcesChkBox.setEnabled(updateMatchConceptFromSrcChkBox.isSelected());
        mergeUnequalCommentsChkBox.setEnabled(updateMatchConceptFromSrcChkBox.isSelected());
    }

    private void selectTargetUseCase() {
        DbTreeModelListener listener = new DbTreeModelListener() {
            public boolean isSelectable(DbObject dbo) throws DbException {
                // reject if the target usecase is a sub usecase of the source,
                // or if dbo is the source object
                DbObject composite = dbo;
                while (composite != null && !(composite instanceof DbBEModel)) {
                    if (composite == source)
                        return false;
                    composite = composite.getComposite();
                } // end while
                return !((DbBEUseCase) dbo).isContext() && ((DbBEUseCase) dbo).isExternal();
            } // end isSelectable()
        };

        String name = source.getMetaClass().getGUIName();
        String pattn = LocaleMgr.screen.getString("SelectProcess");
        String msg = MessageFormat.format(pattn, new Object[] { name });
        target = (DbBEUseCase) DbTreeLookupDialog.selectOne(this, msg, DbApplication.getProjects(),
                new MetaClass[] { DbBEUseCase.metaClass }, listener, null, target);
    }
}
