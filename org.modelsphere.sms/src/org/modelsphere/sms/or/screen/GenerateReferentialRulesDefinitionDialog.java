package org.modelsphere.sms.or.screen;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.or.actions.GenerateReferentialRulesAction;
import org.modelsphere.sms.or.actions.GenerateReferentialRulesAction.RuleCounts;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.srtypes.ORValidationRule;
import org.modelsphere.sms.or.features.ReferentialRules;

public class GenerateReferentialRulesDefinitionDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = 1L;

    private static final String LINE_1 = LocaleMgr.screen
            .getString("GenerateReferentialRuleDialogLine1");
    private static final String LINE_2 = LocaleMgr.screen
            .getString("GenerateReferentialRuleDialogLine2");
    private static final String PARENT_SIDE_MULT = LocaleMgr.screen
            .getString("ParentSideMultiplicity");
    private static final String CHILD_SIDE_MULT = LocaleMgr.screen
            .getString("ChildSideMultiplicity");
    private static final String CLOSE = LocaleMgr.screen.getString("Close");

    private static final String[] PARENT_MULTIPLICITIES = new String[] { "0..N", "1..N" };
    private static String[] CHILD_MULTIPLICITIES = new String[] { new String("0..1"),
            new String("1..1"), new String("<u>1..1</u>") };

    private int parentMultiplicity = 0;
    private int childMultiplicity = 0;

    private JComboBox parentCombo, childCombo;
    private JLabel imageLabel;
    private ReferentialRuleMatrix referentialRuleMatrix;
    private JButton closeButton;

    public GenerateReferentialRulesDefinitionDialog(JFrame frame, DbORDataModel dataModel,
            RuleCounts counts) {
        super(frame, LocaleMgr.action.getString("genReferentialRules"), true);

        initContents();
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
        //set layout
        setLayout(new GridBagLayout());
        int row = 0;

        //set multiplicities
        JLabel label1 = new JLabel(LINE_1);
        add(label1, new GridBagConstraints(0, row, 4, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 6, 0, 6), 0, 0));
        row++;

        //set multiplicities
        label1 = new JLabel(PARENT_SIDE_MULT);
        add(label1, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 6, 0, 6), 0, 0));

        parentCombo = new JComboBox(PARENT_MULTIPLICITIES);
        add(parentCombo, new GridBagConstraints(1, row, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 6, 0, 6), 0, 0));

        JLabel label2 = new JLabel(CHILD_SIDE_MULT);
        add(label2, new GridBagConstraints(2, row, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 6, 0, 6), 0, 0));

        Integer[] childMultiplicity = new Integer[] { 0, 1, 2 };
        childCombo = new JComboBox(childMultiplicity);
        ChildMultiplicityRenderer renderer = new ChildMultiplicityRenderer();
        childCombo.setRenderer(renderer);
        childCombo.setEditable(false);

        add(childCombo, new GridBagConstraints(3, row, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 6, 0, 6), 0, 0));
        row++;

        //image preview
        JPanel previewPanel = new JPanel();
        previewPanel.setLayout(new FlowLayout());
        previewPanel.setBackground(Color.WHITE);
        Icon icon = createImageIcon("referentialRuleParent0_N_Child_0_1.png");
        imageLabel = new JLabel(icon);
        previewPanel.add(imageLabel);

        previewPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createTitledBorder(""), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        add(previewPanel, new GridBagConstraints(0, row, 5, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(6, 6, 0, 6), 0, 0));
        row++;

        //set multiplicities
        JLabel label3 = new JLabel(LINE_2);
        add(label3, new GridBagConstraints(0, row, 4, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 6, 6, 6), 0, 0));
        row++;

        //rule matrix
        int ruleSet = 0;
        referentialRuleMatrix = new ReferentialRuleMatrix(ruleSet);
        add(referentialRuleMatrix, new GridBagConstraints(0, row, 4, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(6, 6, 6, 6), 0, 0));

        //add spacer
        JLabel spacer = new JLabel();
        add(spacer, new GridBagConstraints(0, row, 4, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0));
        row++;

        //close button
        closeButton = new JButton(CLOSE);
        add(closeButton,
                new GridBagConstraints(3, row, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST,
                        GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));
        row++;
    }

    private void addListeners() {
        parentCombo.addActionListener(this);
        childCombo.addActionListener(this);
        closeButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        Object src = ev.getSource();
        boolean doClose = false;

        if (src.equals(parentCombo)) {
            parentMultiplicity = parentCombo.getSelectedIndex();
        } else if (src.equals(childCombo)) {
            childMultiplicity = childCombo.getSelectedIndex();
        } else if (src.equals(closeButton)) {
            doClose = true;
        } //end if

        refreshPreview();
        refreshRuleSet();

        if (doClose) {
            this.setVisible(false);
        }
    } // end actionPerformed()  

    private void refreshPreview() {
        int multiplicity = parentMultiplicity * 3 + childMultiplicity;
        String res = null;

        switch (multiplicity) {
        case 0:
            res = "referentialRuleParent0_N_Child_0_1.png";
            break;
        case 1:
            res = "referentialRuleParent0_N_Child_1_1.png";
            break;
        case 2:
            res = "referentialRuleParent0_N_Child_1_1D.png";
            break;
        case 3:
            res = "referentialRuleParent1_N_Child_0_1.png";
            break;
        case 4:
            res = "referentialRuleParent1_N_Child_1_1.png";
            break;
        case 5:
            res = "referentialRuleParent1_N_Child_1_1D.png";
            break;
        }

        Icon icon = createImageIcon(res);
        imageLabel.setIcon(icon);
    }

    private void refreshRuleSet() {
        //refresh lables and matrix
        int ruleSet = parentMultiplicity * 3 + childMultiplicity;
        referentialRuleMatrix.refreshValue(ruleSet, parentMultiplicity, childMultiplicity);
    }

    //returns an ImageIcon, or null if the pathname was invalid
    private static ImageIcon createImageIcon(String pathname) {
        URL imgURL = LocaleMgr.class.getResource("resources/" + pathname);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            return null;
        }
    }

    //
    // inner classes
    //

    private static class ChildMultiplicityRenderer extends JLabel implements ListCellRenderer {
        private static final long serialVersionUID = 1L;

        public ChildMultiplicityRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            int idx = (Integer) value;
            String text = "<html>" + CHILD_MULTIPLICITIES[idx] + "</html>";
            setText(text);
            return this;
        }
    }

    private static class ReferentialRuleMatrix extends JPanel {
        private static final long serialVersionUID = 1L;
        private JTextField[] parentSideTexts;
        private JTextField[] childSideTexts;
        private JLabel parentSideLabel;
        private JLabel childSideLabel;

        public ReferentialRuleMatrix(int ruleSet) {
            this.setLayout(new GridBagLayout());

            int row = (ruleSet * 4) + 1;
            String parentSide = LocaleMgr.screen.getString("ParentSide");
            String childSide = LocaleMgr.screen.getString("ChildSide");
            String start = "", end = "";
            String parentSideTitle = start + parentSide + " 0..N :" + end;
            String childSideTitle = start + childSide + " 0..1 :" + end;

            parentSideLabel = new JLabel(parentSideTitle);
            this.add(parentSideLabel, new GridBagConstraints(1, row, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 6, 0, 6), 0,
                    0));
            childSideLabel = new JLabel(childSideTitle);
            this.add(childSideLabel, new GridBagConstraints(2, row, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 6, 0, 6), 0,
                    0));
            row++;

            int nbActions = 1 + ReferentialRules.DELETE;
            parentSideTexts = new JTextField[nbActions];
            childSideTexts = new JTextField[nbActions];

            ReferentialRules referentialRules = ReferentialRules.getInstance();

            //for each of {INSERT, UPDATE and DELETE}
            for (int action = ReferentialRules.INSERT; action <= ReferentialRules.DELETE; action++) {
                String actionName = getActionName(action);
                JLabel actionLabel = new JLabel(actionName);
                this.add(actionLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 6),
                        0, 0));

                int side = ReferentialRules.PARENT_SIDE;
                ORValidationRule rule = referentialRules.getDefaultRule(ruleSet, action, side);
                String ruleName = rule.toString();
                parentSideTexts[action] = new JTextField(ruleName);
                parentSideTexts[action].setEditable(false);
                parentSideTexts[action].setBackground(Color.WHITE);
                this.add(parentSideTexts[action], new GridBagConstraints(1, row, 1, 1, 1.0, 0.0,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0,
                                0, 0, 0), 0, 0));

                side = ReferentialRules.CHILD_SIDE;
                rule = referentialRules.getDefaultRule(ruleSet, action, side);
                ruleName = rule.toString();
                childSideTexts[action] = new JTextField(ruleName);
                childSideTexts[action].setEditable(false);
                childSideTexts[action].setBackground(Color.WHITE);
                this.add(childSideTexts[action], new GridBagConstraints(2, row, 1, 1, 1.0, 0.0,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0,
                                0, 0, 0), 0, 0));

                row++;
            } //end for
        }

        private void refreshValue(int ruleSet, int parentMultiplicity, int childMultiplicity) {
            String parentSide = LocaleMgr.screen.getString("ParentSide");
            String childSide = LocaleMgr.screen.getString("ChildSide");

            String parentMultiplicityText = PARENT_MULTIPLICITIES[parentMultiplicity];
            String childMultiplicityText = CHILD_MULTIPLICITIES[childMultiplicity];
            String parentSideTitle = parentSide + " " + parentMultiplicityText + " : ";
            String childSideTitle = "<html>" + childSide + " " + childMultiplicityText + " : "
                    + "</html>";
            parentSideLabel.setText(parentSideTitle);
            childSideLabel.setText(childSideTitle);

            ReferentialRules referentialRules = ReferentialRules.getInstance();

            //for each of {INSERT, UPDATE and DELETE}
            for (int action = ReferentialRules.INSERT; action <= ReferentialRules.DELETE; action++) {
                int side = ReferentialRules.PARENT_SIDE;
                ORValidationRule rule = referentialRules.getDefaultRule(ruleSet, action, side);
                String ruleName = rule.toString();
                parentSideTexts[action].setText(ruleName);

                side = ReferentialRules.CHILD_SIDE;
                rule = referentialRules.getDefaultRule(ruleSet, action, side);
                ruleName = rule.toString();
                childSideTexts[action].setText(ruleName);
            } //end for
        }

        private String getActionName(int ruleSet) {
            String ruleName = "?";

            switch (ruleSet) {
            case ReferentialRules.INSERT:
                ruleName = DbORAssociationEnd.fInsertRule.getGUIName();
                break;
            case ReferentialRules.UPDATE:
                ruleName = DbORAssociationEnd.fUpdateRule.getGUIName();
                break;
            case ReferentialRules.DELETE:
                ruleName = DbORAssociationEnd.fDeleteRule.getGUIName();
                break;
            }

            return ruleName;
        }
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
        RuleCounts counts = new RuleCounts();
        JDialog dialog = new GenerateReferentialRulesDefinitionDialog(frame, model, counts);
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
