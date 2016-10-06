package org.modelsphere.sms.or.screen;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.or.actions.GenerateReferentialRulesAction;
import org.modelsphere.sms.or.actions.GenerateReferentialRulesAction.RuleCounts;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.srtypes.ORValidationRule;
import org.modelsphere.sms.or.features.ReferentialRules;
import org.modelsphere.sms.or.international.LocaleMgr;

public class GenerateReferentialRulesFrame extends JDialog implements ActionListener {
    private static final long serialVersionUID = 1L;
    private RuleCounts fCounts;
    private boolean fDetailShown = false;
    private JFrame fFrame;

    JPanel referentialRuleMatrix;
    JButton detailsButton;
    JButton okButton;
    JButton cancelButton;

    public GenerateReferentialRulesFrame(JFrame frame, String title, DbORDataModel dataModel,
            RuleCounts counts) throws DbException {
        super(frame, title, true);
        fFrame = frame;
        fCounts = counts;

        initContents();
        initValues();
        addListeners();

        this.pack();
        this.setLocationRelativeTo(frame);
    }

    // has cancelled
    private boolean cancelled = true;

    public boolean hasCancelled() {
        return cancelled;
    }

    private void initContents() {
        setLayout(new GridBagLayout());

        int row = 0;

        //Insert Rules
        String ruleName = DbORAssociationEnd.fInsertRule.getGUIName().toLowerCase();
        String pattern = LocaleMgr.screen.getString("0ReferentialRulesAreDefined");
        Object[] counts = new Object[] { fCounts.nbDefinedRules[ReferentialRules.INSERT],
                fCounts.nbRules[ReferentialRules.INSERT], ruleName };
        String msg = MessageFormat.format(pattern, counts);
        JLabel label1 = new JLabel(msg);
        add(label1, new GridBagConstraints(0, row, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(6, 6, 0, 6), 0, 0));
        row++;

        //Update Rules
        ruleName = DbORAssociationEnd.fUpdateRule.getGUIName().toLowerCase();
        counts = new Object[] { fCounts.nbDefinedRules[ReferentialRules.UPDATE],
                fCounts.nbRules[ReferentialRules.UPDATE], ruleName };
        msg = MessageFormat.format(pattern, counts);
        JLabel label2 = new JLabel(msg);
        add(label2, new GridBagConstraints(0, row, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(6, 6, 0, 6), 0, 0));
        row++;

        //Delete Rules
        ruleName = DbORAssociationEnd.fDeleteRule.getGUIName().toLowerCase();
        counts = new Object[] { fCounts.nbDefinedRules[ReferentialRules.DELETE],
                fCounts.nbRules[ReferentialRules.DELETE], ruleName };
        msg = MessageFormat.format(pattern, counts);
        JLabel label3 = new JLabel(msg);
        add(label3, new GridBagConstraints(0, row, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(6, 6, 0, 6), 0, 0));
        row++;

        /*
         * //Referential Matrix referentialRuleMatrix = new JPanel();
         * initReferentialRuleMatrix(referentialRuleMatrix); add(referentialRuleMatrix, new
         * GridBagConstraints(0, row, 3, 1, 1.0, 1.0, GridBagConstraints.SOUTHWEST,
         * GridBagConstraints.BOTH, new Insets(18, 12, 0, 12), 0, 0));
         * referentialRuleMatrix.setVisible(fDetailShown); row++;
         */

        //Action label
        int nbRules = fCounts.getNbRules();
        int nbMissingRules = fCounts.getNbMissingRules();

        if (nbRules == 0) {
            msg = LocaleMgr.screen.getString("ReferentialRulesNoticeNoOccurrences");
        } else if (nbMissingRules == 0) {
            msg = LocaleMgr.screen.getString("ReferentialRulesNoticeAllDefined");
        } else {
            pattern = LocaleMgr.screen.getString("ReferentialRulesNotice0Undefined");
            msg = MessageFormat.format(pattern, new Object[] { nbMissingRules });
        } //end if

        JLabel label4 = new JLabel(msg);
        add(label4, new GridBagConstraints(0, row, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(18, 6, 0, 6), 0, 0));
        row++;

        //Control Panel
        JPanel controlBtnPanel = new JPanel();
        initControlBtnPanel(controlBtnPanel, nbMissingRules);
        add(controlBtnPanel, new GridBagConstraints(0, row, GridBagConstraints.REMAINDER, 1, 0.0,
                1.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
                new Insets(30, 6, 6, 6), 0, 0));
        row++;

    }

    private void initControlBtnPanel(JPanel controlBtnPanel, int nbMissingRules) {
        controlBtnPanel.setLayout(new GridBagLayout());

        final String DETAILS = LocaleMgr.screen.getString("Details") + "...";
        //String text = detailShown ? "<< " + DETAILS : DETAILS + " >>";
        detailsButton = new JButton();
        detailsButton.setText(DETAILS);
        //detailsButton.setVisible(false); //removed until future implementation decision
        okButton = new JButton(LocaleMgr.screen.getString("OK"));
        String closeOrCancel = (nbMissingRules > 0) ? LocaleMgr.screen.getString("Cancel")
                : LocaleMgr.screen.getString("Close");
        cancelButton = new JButton(closeOrCancel);
        okButton.setVisible(nbMissingRules > 0);
        org.modelsphere.jack.awt.AwtUtil.normalizeComponentDimension(new JButton[] { okButton,
                cancelButton });

        //add details button
        controlBtnPanel.add(detailsButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(18, 6, 0, 6), 0,
                0));

        //add spacer
        JLabel spacer = new JLabel();
        controlBtnPanel.add(spacer,
                new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                        GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0));

        //add ok and cancel button
        controlBtnPanel.add(okButton, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(18, 3, 0, 3), 0,
                0));
        controlBtnPanel.add(cancelButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(18, 3, 0, 3), 0,
                0));

    }

    private void initReferentialRuleMatrix(JPanel referentialRuleMatrix) {
        referentialRuleMatrix.setLayout(new GridBagLayout());

        JLabel label2 = new JLabel(LocaleMgr.screen.getString("ReferentialRulesWillBeGenerated"));
        referentialRuleMatrix.add(label2,
                new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, 0));

        String start = "<html><body><b>";
        String end = "</b></body></html>";
        String parentSide = LocaleMgr.screen.getString("ParentSide");
        String childSide = LocaleMgr.screen.getString("ChildSide");

        int actionSet = 0;
        String parentSideTitle = start + parentSide + " 0..N :" + end;
        String childSideTitle = start + childSide + " 0..1 :" + end;
        initActionSet(referentialRuleMatrix, actionSet, parentSideTitle, childSideTitle);

        actionSet++;
        parentSideTitle = start + parentSide + " 0..N :" + end;
        childSideTitle = start + childSide + " 1..1 :" + end;
        initActionSet(referentialRuleMatrix, actionSet, parentSideTitle, childSideTitle);

        actionSet++;
        parentSideTitle = start + parentSide + " 0..N :" + end;
        childSideTitle = start + childSide + " <u>1..1</u> :" + end;
        initActionSet(referentialRuleMatrix, actionSet, parentSideTitle, childSideTitle);

        actionSet++;
        parentSideTitle = start + parentSide + " 1..N :" + end;
        childSideTitle = start + childSide + " 0..1 :" + end;
        initActionSet(referentialRuleMatrix, actionSet, parentSideTitle, childSideTitle);

        actionSet++;
        parentSideTitle = start + parentSide + " 1..N :" + end;
        childSideTitle = start + childSide + " 1..1 :" + end;
        initActionSet(referentialRuleMatrix, actionSet, parentSideTitle, childSideTitle);

        actionSet++;
        parentSideTitle = start + parentSide + " 1..N :" + end;
        childSideTitle = start + childSide + " <u>1..1</u> :" + end;
        initActionSet(referentialRuleMatrix, actionSet, parentSideTitle, childSideTitle);
    }

    private void initActionSet(JPanel referentialRuleMatrix, int actionSet, String parentSideTitle,
            String childSideTitle) {
        int row = actionSet * 4 + 1;
        JLabel label1 = new JLabel(parentSideTitle);
        referentialRuleMatrix.add(label1, new GridBagConstraints(1, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 6, 0, 6), 0, 0));
        JLabel label2 = new JLabel(childSideTitle);
        referentialRuleMatrix.add(label2, new GridBagConstraints(2, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 6, 0, 6), 0, 0));
        row++;

        ReferentialRules referentialRules = ReferentialRules.getInstance();

        //for each of {INSERT, UPDATE and DELETE}
        for (int action = ReferentialRules.INSERT; action <= ReferentialRules.DELETE; action++) {
            String actionName = getActionName(action);
            JLabel actionLabel = new JLabel(actionName);
            referentialRuleMatrix
                    .add(actionLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(0, 0, 0, 6), 0, 0));

            int side = ReferentialRules.PARENT_SIDE;
            ORValidationRule rule = referentialRules.getDefaultRule(actionSet, action, side);
            String ruleName = rule.toString();
            JTextField text1 = new JTextField(ruleName);
            text1.setEditable(false);
            text1.setBackground(Color.WHITE);
            referentialRuleMatrix.add(text1, new GridBagConstraints(1, row, 1, 1, 1.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
                            0, 0), 0, 0));

            side = ReferentialRules.CHILD_SIDE;
            rule = referentialRules.getDefaultRule(actionSet, action, side);
            ruleName = rule.toString();
            JTextField text2 = new JTextField(ruleName);
            text2.setEditable(false);
            text2.setBackground(Color.WHITE);
            referentialRuleMatrix.add(text2, new GridBagConstraints(2, row, 1, 1, 1.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
                            0, 0), 0, 0));

            row++;
        } //end for
    }

    private String getActionName(int action) {
        String actionName = "?";

        switch (action) {
        case ReferentialRules.INSERT:
            actionName = DbORAssociationEnd.fInsertRule.getGUIName();
            break;
        case ReferentialRules.UPDATE:
            actionName = DbORAssociationEnd.fUpdateRule.getGUIName();
            break;
        case ReferentialRules.DELETE:
            actionName = DbORAssociationEnd.fDeleteRule.getGUIName();
            break;
        }

        return actionName;
    }

    private void addListeners() {
        detailsButton.addActionListener(this);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {

        Object src = ev.getSource();
        if (src.equals(detailsButton)) {
            fDetailShown = !(fDetailShown);
            final String DETAILS = LocaleMgr.screen.getString("Details") + "...";
            detailsButton.setText(DETAILS);

            //show dialog
            DbORDataModel model = null;
            RuleCounts counts = new RuleCounts();
            GenerateReferentialRulesDefinitionDialog dialog = new GenerateReferentialRulesDefinitionDialog(
                    fFrame, model, counts);
            dialog.setVisible(true);

            //referentialRuleMatrix.setVisible(detailShown);
            //pack();
        } else if (src.equals(okButton)) {
            cancelled = false;
            dispose();
        } else if (src.equals(cancelButton)) {
            cancelled = true;
            dispose();
        } // end if

    } // end actionPerformed()

    private void initValues() {

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
        JDialog dialog = new GenerateReferentialRulesFrame(frame, "Demo", model, counts);
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
