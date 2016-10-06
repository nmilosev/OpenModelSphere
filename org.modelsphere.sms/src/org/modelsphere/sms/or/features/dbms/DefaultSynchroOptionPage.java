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

package org.modelsphere.sms.or.features.dbms;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.gui.wizard.WizardPage;
import org.modelsphere.jack.srtool.integrate.IntegrateScopeDialog;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectScope;
import org.modelsphere.jack.srtool.services.NameListMessage;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.international.LocaleMgr;

/**
 * 
 * For each kind of database object (tablespace, table, index, etc.), a user can decide if he/she
 * wants to reverse it. Only selected kinds of objects will be reverse engineered.
 * 
 */
public class DefaultSynchroOptionPage extends WizardPage {
    private static final String kReverseObjectUser = LocaleMgr.screen.getString("SynchronizeUser");
    private static final String kConceptsToSync = LocaleMgr.screen.getString("ConceptsToSync");
    private static final String kSQLScriptoutput = LocaleMgr.screen.getString("SQLScriptoutput");
    private static final String kDatabaseOptions = LocaleMgr.screen.getString("DatabaseOptions");
    private static final String kGenObjectWithUser = LocaleMgr.screen
            .getString("GenObjectWithUser");
    private static final String kDefine = LocaleMgr.screen.getString("Define");

    private JPanel optionPanel = new JPanel(new GridBagLayout());
    private TitledBorder scopePanelBorder = new TitledBorder(kConceptsToSync);
    private JPanel scopePanel = new JPanel(new GridBagLayout());
    private TitledBorder generateOptionsPanelBorder = new TitledBorder(kSQLScriptoutput);
    private JPanel generateOptionsPanel = new JPanel(new GridBagLayout());
    private TitledBorder dbOptionsPanelBorder = new TitledBorder(kDatabaseOptions);
    private JPanel dbOptionsPanel = new JPanel(new GridBagLayout());
    protected JCheckBox reverseObjectUserCheckBox = new JCheckBox(kReverseObjectUser);
    protected JCheckBox genObjectUserCheckBox = new JCheckBox(kGenObjectWithUser);
    protected JButton scopeButton = new JButton(kDefine);

    private DBMSReverseOptions options;

    public DefaultSynchroOptionPage() {
        super();
    }

    private void initGUI(final DBMSReverseOptions options) {
        this.options = options;
        setLayout(new GridBagLayout());

        scopeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                IntegrateScopeDialog scopediag = new IntegrateScopeDialog(options.fieldTree, null,
                        IntegrateScopeDialog.INTEGRATION);
                scopediag.setVisible(true);
                updateNextButton();
            }
        });

        scopePanel.setBorder(scopePanelBorder);
        scopePanel.add(scopeButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(6, 12, 12, 12),
                0, 0));
        scopePanel.add(Box.createVerticalGlue(), new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        generateOptionsPanel.setBorder(generateOptionsPanelBorder);
        generateOptionsPanel.add(genObjectUserCheckBox, new GridBagConstraints(0, 0, 1, 1, 0.0,
                0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(6, 12, 12,
                        12), 0, 0));
        generateOptionsPanel.add(Box.createVerticalGlue(),
                new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        dbOptionsPanel.setBorder(dbOptionsPanelBorder);
        dbOptionsPanel.add(reverseObjectUserCheckBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(6, 12, 12, 12),
                0, 0));
        dbOptionsPanel.add(Box.createVerticalGlue(), new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        JPanel specificPanel = getSpecificOptionsPanel();
        if (specificPanel != null) {
            optionPanel.add(scopePanel,
                    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                            GridBagConstraints.BOTH, new Insets(0, 0, 6, 0), 0, 0));
            optionPanel.add(generateOptionsPanel,
                    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                            GridBagConstraints.BOTH, new Insets(0, 0, 6, 0), 0, 0));
            optionPanel.add(dbOptionsPanel,
                    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                            GridBagConstraints.BOTH, new Insets(0, 0, 6, 0), 0, 0));
            optionPanel.add(specificPanel,
                    new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                            GridBagConstraints.BOTH, new Insets(6, 0, 0, 0), 0, 0));
        } else {
            optionPanel.add(scopePanel,
                    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                            GridBagConstraints.BOTH, new Insets(0, 0, 6, 0), 0, 0));
            optionPanel.add(generateOptionsPanel,
                    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                            GridBagConstraints.BOTH, new Insets(0, 0, 6, 0), 0, 0));
            optionPanel.add(dbOptionsPanel,
                    new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                            GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        }

        add(optionPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, new Insets(12, 12, 12, 12), 0, 0));

    }

    protected JPanel getSpecificOptionsPanel() {
        return null;
    }

    public final boolean initialize(Object opt) {
        DBMSReverseOptions options = (DBMSReverseOptions) opt;
        try {
            if (options.fieldTree == null) {
                ReverseToolkitPlugin kit = ReverseToolkitPlugin.getToolkit();
                String scopeFileName = kit.getFileName_ScopeSynchro(options);
                if (scopeFileName == null)
                    throw new RuntimeException("No synchronization scope file defined in toolkit. ");
                options.fieldTree = IntegrateScopeDialog.loadTemplateScope(kit, scopeFileName);
            }
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(this, e);
        }

        initGUI(options);

        // init GUI from options
        reverseObjectUserCheckBox.setSelected(options.synchroUseUser);
        genObjectUserCheckBox.setSelected(options.genObjectWithUser);

        updateNextButton();
        initSpecific(options);
        return true;
    }

    public boolean terminate(Object opt, boolean saveData) {
        DBMSReverseOptions options = (DBMSReverseOptions) opt;

        // save GUI setting
        options.synchroUseUser = reverseObjectUserCheckBox.isSelected();
        options.reverseObjectUser = true;
        options.genObjectWithUser = genObjectUserCheckBox.isSelected();

        ReverseToolkitPlugin kit = ReverseToolkitPlugin.getToolkit();

        terminateSpecific(options);

        if (!saveData) {
            options = null;
            return true;
        }

        if (options.getConnection() == null) { // if sync using 2 models
            options.setRequestFile(kit.getSQLFileName_Xtr(options));
        } else {
            // Convert the selection in options.fieldTree to treemodel selection
            ScopeTreeModel treemodel = new ScopeTreeModel(
                    new DefaultMutableTreeNode("JTree", true), options); // NOT
            // LOCALIZABLE
            TreeNode revRoot = (TreeNode) treemodel.getRoot();
            CheckTreeNode integRoot = options.fieldTree;
            convertNode(revRoot);

            ObjectScope userScope = null;
            userScope = ObjectScope.findConceptInObjectScopeWithConceptName(options
                    .getObjectsScope(), DbORUser.metaClass.getGUIName());
            userScope.isSelected = true;

            options.setRequestFile(kit.getSQLFileName_Gets(options));

            NameListMessage nameListMessage = DefaultReverseWizardOptionPage.getNameList(options);
            ArrayList nameList = nameListMessage.nameList;
            if (nameList.size() == 0)
                return false;

            kit.processNameListMessage(options, nameListMessage);

            options.nameList = nameList; // store results in options

            DefaultReverseWizardOptionPage.restructureNameListData(options);
        }

        optionPanel = null;
        options = null;
        return true;
    }

    private void convertNode(TreeNode node) {
        if (node == null)
            return;
        if (node instanceof ScopeTreeNode) {
            ScopeTreeNode scopenode = (ScopeTreeNode) node;
            MetaClass metaclass = scopenode.getObjectScope().metaClass;
            if (isSelectedInIntegrationScope(metaclass)) {
                scopenode.setSelected(true);
                int count = scopenode.getChildCount();
                for (int i = 0; i < count; i++) {
                    TreeNode child = (TreeNode) scopenode.getChildAt(i);
                    convertNode(child);
                }
            } else {
                scopenode.setSelected(false); // if false, don't need to process
                // sub nodes
            }
        } else {
            int count = node.getChildCount();
            for (int i = 0; i < count; i++) {
                TreeNode child = (TreeNode) node.getChildAt(i);
                convertNode(child);
            }
        }
        return;
    }

    private boolean isSelectedInIntegrationScope(MetaClass metaclass) {
        if (metaclass == null)
            return false;
        CheckTreeNode integRoot = (CheckTreeNode) options.fieldTree;
        // just need to check first level
        int count = integRoot.getChildCount();
        for (int i = 0; i < count; i++) {
            CheckTreeNode child = (CheckTreeNode) integRoot.getChildAt(i);
            Object userObj = child.getUserObject();
            if (!(userObj instanceof MetaClass))
                continue;
            MetaClass mc = (MetaClass) userObj;
            if (mc == metaclass) // obsolete metaClass
                return child.isSelected();
        }
        return false;
    }

    protected void initSpecific(DBMSReverseOptions options) {
    }

    protected void terminateSpecific(DBMSReverseOptions options) {
    }

    private void updateNextButton() {
        boolean enable = false;
        int count = options.fieldTree.getChildCount();
        for (int i = 0; i < count; i++) {
            CheckTreeNode child = (CheckTreeNode) options.fieldTree.getChildAt(i);
            if (child.isSelected()) {
                enable = true;
                break;
            }
        }
        setNextEnabled(enable);
    }

}
