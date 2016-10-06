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

package org.modelsphere.plugins.diagram.tree;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.plugins.diagram.tree.international.LocaleMgr;

public class TreeDiagramOptionPanel extends JPanel {
    private JPanel jOrientationPanel = new JPanel();
    private TitledBorder titledBorder1;
    private JPanel jAlignementPanel = new JPanel();
    private TitledBorder titledBorder2;
    private JCheckBox jFitChkBox = new JCheckBox();
    private JPanel jSpanPanel = new JPanel();
    private TitledBorder titledBorder3;
    private JCheckBox jExplodedChkBox = new JCheckBox();
    private JRadioButton jNormalRadioBtn = new JRadioButton();
    private JRadioButton jMinimizedRadioBtn = new JRadioButton();
    private JRadioButton jHorizontalRadioBtn = new JRadioButton();
    private JRadioButton jVerticalRadioBtn = new JRadioButton();
    private JRadioButton jLeftRadioBtn = new JRadioButton();
    private JRadioButton jCenterRadioBtn = new JRadioButton();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private GridBagLayout gridBagLayout2 = new GridBagLayout();
    private GridBagLayout gridBagLayout3 = new GridBagLayout();
    private GridBagLayout gridBagLayout4 = new GridBagLayout();
    private JPanel jPanel4 = new JPanel();
    private JButton jOKButton = new JButton();
    private JButton jCancelButton = new JButton();
    private GridBagLayout gridBagLayout5 = new GridBagLayout();

    //strings
    private static final String OK = LocaleMgr.screen.getString("OK");
    private static final String CANCEL = LocaleMgr.screen.getString("Cancel");
    private static final String CENTER = LocaleMgr.misc.getString("Center");
    private static final String ALIGNEMENT = LocaleMgr.misc.getString("Alignment");
    private static final String DISPLAY_ONLY_EXPLODED = LocaleMgr.misc
            .getString("DisplayOnlyExplodedProcesses");
    private static final String FIT_PROCESSES = LocaleMgr.misc.getString("FitProcesses");
    private static final String HORIZONTAL = LocaleMgr.misc.getString("Horizontal");
    private static final String LEFT = LocaleMgr.misc.getString("Left");
    private static final String MINIMIZED = LocaleMgr.misc.getString("Minimized");
    private static final String NORMAL = LocaleMgr.misc.getString("Normal");
    private static final String ORIENTATION = LocaleMgr.misc.getString("Orientation");
    private static final String PROCESS_TREE_GENERATON = LocaleMgr.misc
            .getString("ProcessTreeGeneration");
    private static final String PROCESS_TREE_UPDATE = LocaleMgr.misc.getString("ProcessTreeUpdate");
    private static final String TOP = LocaleMgr.misc.getString("Top");
    private static final String SPAN = LocaleMgr.misc.getString("Span");
    private static final String VERTICAL = LocaleMgr.misc.getString("Vertical");

    private TreeDiagramOptions m_options;
    private JDialog m_dialog = null;

    public TreeDiagramOptionPanel(TreeDiagramOptions options) {
        try {
            jbInit();
            init(options);
            addListeners();
            m_options = options;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        titledBorder1 = new TitledBorder(ORIENTATION);
        titledBorder2 = new TitledBorder(ALIGNEMENT);
        titledBorder3 = new TitledBorder(SPAN);
        this.setLayout(gridBagLayout1);
        jOrientationPanel.setBorder(titledBorder1);
        jOrientationPanel.setLayout(gridBagLayout3);
        jAlignementPanel.setBorder(titledBorder2);
        jAlignementPanel.setLayout(gridBagLayout4);
        jFitChkBox.setText(FIT_PROCESSES);
        jSpanPanel.setBorder(titledBorder3);
        jSpanPanel.setLayout(gridBagLayout2);
        jExplodedChkBox.setText(DISPLAY_ONLY_EXPLODED);
        jNormalRadioBtn.setText(NORMAL);
        jMinimizedRadioBtn.setText(MINIMIZED);
        jHorizontalRadioBtn.setText(HORIZONTAL);
        jVerticalRadioBtn.setText(VERTICAL);
        jLeftRadioBtn.setText(LEFT);
        jCenterRadioBtn.setText(CENTER);
        jPanel4.setLayout(gridBagLayout5);
        jOKButton.setText(OK);
        jCancelButton.setText(CANCEL);

        this.add(jSpanPanel,
                new GridBagConstraints(0, 0, 2, 1, 0.0, 1.0, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(12, 12, 6, 11), 0, 0));
        jSpanPanel.add(jNormalRadioBtn, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 6, 0, 11), 0,
                0));
        jSpanPanel.add(jMinimizedRadioBtn, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 6, 6, 11), 0,
                0));

        this.add(jOrientationPanel, new GridBagConstraints(0, 1, 1, 1, 0.3, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(6, 12, 6, 6), 0, 0));
        jOrientationPanel.add(jHorizontalRadioBtn, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 6, 0, 11), 0, 0));
        jOrientationPanel.add(jVerticalRadioBtn, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 6, 6, 11), 0,
                0));

        this.add(jAlignementPanel, new GridBagConstraints(1, 1, 1, 1, 0.7, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(6, 6, 6, 11), 0, 0));
        jAlignementPanel.add(jLeftRadioBtn, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 6, 0, 11), 0,
                0));
        jAlignementPanel.add(jCenterRadioBtn, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 6, 6, 11), 0,
                0));

        this.add(jFitChkBox, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 12, 0, 11), 0, 0));
        this.add(jExplodedChkBox, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 5, 11), 0, 0));

        this.add(jPanel4, new GridBagConstraints(0, 4, 2, 1, 0.5, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.HORIZONTAL,
                new Insets(12, 0, 0, 0), 0, 0));
        jPanel4.add(jOKButton, new GridBagConstraints(0, 0, 1, 1, 0.2, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(6, 12, 12, 6),
                30, 0));
        jPanel4.add(jCancelButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(6, 6, 12, 11), 0,
                0));
    } //end jbInit()

    private void init(TreeDiagramOptions options) {
        //Span group
        ButtonGroup group1 = new ButtonGroup();
        group1.add(jNormalRadioBtn);
        group1.add(jMinimizedRadioBtn);
        jNormalRadioBtn.setSelected(options.isSpan());
        jMinimizedRadioBtn.setSelected(!options.isSpan());

        //Orientation group
        ButtonGroup group2 = new ButtonGroup();
        group2.add(jHorizontalRadioBtn);
        group2.add(jVerticalRadioBtn);
        jHorizontalRadioBtn.setSelected(options.isHorizontal());
        jVerticalRadioBtn.setSelected(!options.isHorizontal());

        //Alignment group
        ButtonGroup group3 = new ButtonGroup();
        group3.add(jLeftRadioBtn);
        group3.add(jCenterRadioBtn);
        jLeftRadioBtn.setSelected(!options.isCenter());
        jCenterRadioBtn.setSelected(options.isCenter());
        jLeftRadioBtn.setText(options.isHorizontal() ? LEFT : TOP);

        //Checkboxes
        jFitChkBox.setSelected(options.fitProcesses());
        jExplodedChkBox.setSelected(options.onlyExploded());
    } //end init()

    private void addListeners() {
        //Orientation buttons
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                Object obj = ev.getSource();
                if (obj instanceof JRadioButton) {
                    if (obj == jHorizontalRadioBtn) {
                        jLeftRadioBtn.setText(LEFT);
                    } else if (obj == jVerticalRadioBtn) {
                        jLeftRadioBtn.setText(TOP);
                    } //end if
                } //end if
            } //end actionPerformed()
        };
        jHorizontalRadioBtn.addActionListener(listener);
        jVerticalRadioBtn.addActionListener(listener);

        //OK button
        jOKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                m_options.setSpan(jNormalRadioBtn.isSelected());
                m_options.setHorizontal(jHorizontalRadioBtn.isSelected());
                m_options.setCenter(jCenterRadioBtn.isSelected());
                m_options.setfitProcesses(jFitChkBox.isSelected());
                m_options.setOnlyExploded(jExplodedChkBox.isSelected());
                m_dialog.setVisible(false);
            } //end actionPerformed()
        });

        //Cancel button
        jCancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                m_options = null;
                m_dialog.setVisible(false);
            } //end actionPerformed()
        });
    }

    String getDialogTitle(DbBEDiagram diagram) {
        String title;
        try {
            DbSMSDiagram parent = diagram.getParentDiagram();
            title = (parent == null) ? PROCESS_TREE_GENERATON : PROCESS_TREE_UPDATE;
            m_options.setDoUpdate(parent != null);
        } catch (DbException ex) {
            title = PROCESS_TREE_GENERATON;
        }

        return title;
    } //end getDialogTitle()

    TreeDiagramOptions getOptions() {
        return m_options;
    }

    void setDialog(JDialog dialog) {
        m_dialog = dialog;
        dialog.getContentPane().add(this);
    }

    //unit test
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle(PROCESS_TREE_GENERATON);
        TreeDiagramOptions options = new TreeDiagramOptions();
        JPanel panel = new TreeDiagramOptionPanel(options);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    } //end main()
} //end TreeDiagramOptionPanel

