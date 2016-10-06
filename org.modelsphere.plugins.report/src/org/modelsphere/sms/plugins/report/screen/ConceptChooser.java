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

package org.modelsphere.sms.plugins.report.screen;

// JDK
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.awt.tree.CheckTreeModel;
import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.sms.plugins.report.LocaleMgr;
import org.modelsphere.sms.plugins.report.model.ReportModel;

public class ConceptChooser {

    public static int APPROVE_OPTION = 1;
    public static int CANCEL_OPTION = 2;

    private ReportModel model;

    private CheckTreeNode selectedConceptNode;

    public ConceptChooser(ReportModel model) {
        this.model = model;
        setSelectedConceptNode(this.model.getOptions().getWelcomeConceptNode());
    }

    public void showDialog(Dialog parent) {
        ConceptChooserDialog dialog = new ConceptChooserDialog(parent);
        AwtUtil.centerWindow(dialog);
        dialog.setVisible(true);
    }

    public void setSelectedConceptNode(CheckTreeNode node) {
        selectedConceptNode = node;
    }

    public CheckTreeNode getSelectedConceptNode() {
        return selectedConceptNode;
    }

    private class ConceptChooserDialog extends JDialog implements ActionListener {

        JTree conceptTree;
        JScrollPane treeScrollPane;

        JPanel containerPanel = new JPanel();
        JLabel jLabel1 = new JLabel();
        JTextField jTextField1 = new JTextField();
        JPanel controlBtnPanel = new JPanel();
        JButton selectButton = new JButton();
        JButton cancelButton = new JButton();
        JButton helpButton = new JButton();
        GridLayout gridLayout1 = new GridLayout();
        GridBagLayout gridBagLayout1 = new GridBagLayout();

        public ConceptChooserDialog(Dialog dialog, String title, boolean modal) {
            super(dialog, title, modal);

            CheckTreeModel treeModel = model.getTreeReprentation();
            CheckTreeNode root = (CheckTreeNode) treeModel.getRoot();
            Enumeration enumeration = root.children();

            DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode();
            DefaultTreeModel newTreeModel = new DefaultTreeModel(newRoot);

            while (enumeration.hasMoreElements()) {
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(
                        enumeration.nextElement(), false);
                newRoot.add(child);
            }

            conceptTree = new JTree(newTreeModel);
            conceptTree.setRootVisible(false);
            treeScrollPane = new JScrollPane(conceptTree);

            jbInit();
            this.pack();
            this.setLocationRelativeTo(dialog);
        }

        public ConceptChooserDialog(Dialog dialog, String title) {
            this(dialog, title, true);
        }

        public ConceptChooserDialog(Dialog dialog) {
            this(dialog, LocaleMgr.misc.getString("ChooseConcept"), true); // NOT LOCALIZABLE
        }

        void jbInit() {
            //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            // CODE POUR CONCEPTION GRAPHIQUE (Libellés temporaires)
            //A mettre en commentaire lorsque completé

            selectButton.setText(LocaleMgr.misc.getString("Select")); // NOT LOCALIZABLE
            cancelButton.setText(LocaleMgr.misc.getString("Cancel")); // NOT LOCALIZABLE
            //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

            containerPanel.setLayout(gridBagLayout1);
            gridLayout1.setHgap(5);
            controlBtnPanel.setLayout(gridLayout1);
            getContentPane().add(containerPanel);

            // Main Panel
            containerPanel.add(treeScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(12, 12, 12, 12),
                    0, 0));

            // Control Button Panel
            containerPanel.add(controlBtnPanel, new GridBagConstraints(0, 1,
                    GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST,
                    GridBagConstraints.NONE, new Insets(5, 12, 12, 12), 0, 0));

            controlBtnPanel.add(selectButton, null);
            controlBtnPanel.add(cancelButton, null);

            selectButton.addActionListener(this);

            cancelButton.addActionListener(this);

            getRootPane().setDefaultButton(selectButton);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            new HotKeysSupport(this, cancelButton, null);
        }

        // ***************************************************************************
        // implements AtionListener
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            if (source == selectButton) {
                if (conceptTree.getSelectionPath() != null) {
                    Object[] objects = conceptTree.getSelectionPath().getPath();
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) objects[objects.length - 1];
                    setSelectedConceptNode((CheckTreeNode) selectedNode.getUserObject());
                }

                dispose();
            } else if (source == cancelButton)
                dispose();
        }
        // end - implements AtionListener
        // ***************************************************************************
    }
}
