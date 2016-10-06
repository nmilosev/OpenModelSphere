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

package org.modelsphere.sms.or.actions;

import java.text.MessageFormat;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.jack.util.ExceptionMessage;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.srtypes.ORValidationRule;
import org.modelsphere.sms.or.features.ReferentialRules;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.or.screen.GenerateReferentialRulesFrame;

public final class GenerateReferentialRulesAction extends AbstractApplicationAction implements
        SelectionActionListener {
    private static final long serialVersionUID = 1L;
    private static final String kGenPrimaryKeysDots = LocaleMgr.action
            .getString("genReferentialRulesDots");
    private TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    GenerateReferentialRulesAction() {
        super(kGenPrimaryKeysDots);
        this.setMnemonic(LocaleMgr.action.getMnemonic("genPrimaryKeysDots"));
        setEnabled(true);
    }

    public final void updateSelectionAction() throws DbException {
        DbObject[] activeObjs = ORActionFactory.getActiveObjects();

        boolean enabled = false;

        if (activeObjs != null) {
            enabled = true;

            if (activeObjs[0] instanceof DbORDataModel) {
                DbORDataModel dataModel = (DbORDataModel) activeObjs[0];
                if (terminologyUtil.getModelLogicalMode(dataModel) == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                    enabled = false;
            } else {
                DbObject dataModel = terminologyUtil.isCompositeDataModel(activeObjs[0]);
                if (dataModel == null)
                    enabled = false;
                else if (terminologyUtil.getModelLogicalMode(dataModel) == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                    enabled = false;
            }
        }
        setEnabled(enabled);
    } // end updateSelectionAction()

    protected final void doActionPerformed() {
        try {
            DbORDataModel dataModel;
            DbObject[] activeObjs = ORActionFactory.getActiveObjects();
            activeObjs[0].getDb().beginReadTrans();
            if (activeObjs[0] instanceof DbORDataModel) {
                dataModel = ((DbORDataModel) activeObjs[0]);
            } else {
                dataModel = (DbORDataModel) activeObjs[0]
                        .getCompositeOfType(DbORDataModel.metaClass);
            }
            activeObjs[0].getDb().commitTrans();

            if (dataModel == null)
                return;

            // how many rules to generate
            dataModel.getDb().beginReadTrans();
            RuleCounts counts = new RuleCounts();
            countMissingReferentialRules(dataModel, counts);
            dataModel.getDb().commitTrans();

            // Open dialog
            JFrame mainFrame = ApplicationContext.getDefaultMainFrame();
            String title = LocaleMgr.action.getString("genReferentialRules");
            GenerateReferentialRulesFrame dialog = new GenerateReferentialRulesFrame(mainFrame,
                    title, dataModel, counts);
            dialog.setVisible(true);

            // Generate, if user pressed OK
            if (!dialog.hasCancelled()) {
                // generate rules
                String txName = LocaleMgr.action.getString("genReferentialRules");
                dataModel.getDb().beginWriteTrans(txName);
                int rulesGenerated = generateReferentialRules(dataModel);
                dataModel.getDb().commitTrans();

                // show results
                String pattern = LocaleMgr.action.getString("genReferentialRulesSuccess");
                String msg = MessageFormat.format(pattern, rulesGenerated);

                JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(), msg,
                        ApplicationContext.getApplicationName(), ExceptionMessage.E_INFORMATION);
            }

        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        } // end try
    } // end doActionPerformed()

    protected int getFeatureSet() {
        return SMSFilter.RELATIONAL;
    }

    //
    // private methods
    //
    private void countMissingReferentialRules(DbORDataModel dataModel, RuleCounts counts)
            throws DbException {
        DbRelationN relN = dataModel.getComponents();
        DbEnumeration enu = relN.elements(DbORAssociation.metaClass);

        while (enu.hasMoreElements()) {
            DbORAssociation assoc = (DbORAssociation) enu.nextElement();
            DbORAssociationEnd backEnd = assoc.getBackEnd();
            DbORAssociationEnd frontEnd = assoc.getFrontEnd();
            countReferentialActions(backEnd, frontEnd, counts);
        }
        enu.close();
    }

    private void countReferentialActions(DbORAssociationEnd backEnd, DbORAssociationEnd frontEnd,
            RuleCounts counts) throws DbException {
        DbORAssociationEnd parentSide = findParentSide(backEnd, frontEnd);
        if (parentSide == null) {
            // do not generate actions for one-to-one or many-to-many
            // associations
            return;
        }

        DbORAssociationEnd childSide = parentSide.getOppositeEnd();
        counts.nbRules[ReferentialRules.INSERT] += 2;
        counts.nbRules[ReferentialRules.UPDATE] += 2;
        counts.nbRules[ReferentialRules.DELETE] += 2;

        if (parentSide.getInsertRule() != null) {
            counts.nbDefinedRules[ReferentialRules.INSERT]++;
        }

        if (parentSide.getUpdateRule() != null) {
            counts.nbDefinedRules[ReferentialRules.UPDATE]++;
        }

        if (parentSide.getDeleteRule() != null) {
            counts.nbDefinedRules[ReferentialRules.DELETE]++;
        }

        if (childSide.getInsertRule() != null) {
            counts.nbDefinedRules[ReferentialRules.INSERT]++;
        }

        if (childSide.getUpdateRule() != null) {
            counts.nbDefinedRules[ReferentialRules.UPDATE]++;
        }

        if (childSide.getDeleteRule() != null) {
            counts.nbDefinedRules[ReferentialRules.DELETE]++;
        }
    }

    private int generateReferentialRules(DbORDataModel dataModel) throws DbException {
        int actionsGenerated = 0;
        DbRelationN relN = dataModel.getComponents();
        DbEnumeration enu = relN.elements(DbORAssociation.metaClass);

        while (enu.hasMoreElements()) {
            DbORAssociation assoc = (DbORAssociation) enu.nextElement();
            DbORAssociationEnd backEnd = assoc.getBackEnd();
            DbORAssociationEnd frontEnd = assoc.getFrontEnd();
            actionsGenerated += generateReferentialActions(backEnd, frontEnd);
        }
        enu.close();

        return actionsGenerated;
    } // end generateReferentialActions()

    private int generateReferentialActions(DbORAssociationEnd backEnd, DbORAssociationEnd frontEnd)
            throws DbException {
        int actionsGenerated = 0;
        DbORAssociationEnd parentSide = findParentSide(backEnd, frontEnd);
        if (parentSide == null) {
            // do not generate actions for one-to-one or many-to-many
            // associations
            return 0;
        }

        ReferentialRules referentialRules = ReferentialRules.getInstance();
        DbORAssociationEnd childSide = parentSide.getOppositeEnd();
        boolean dependantKey = referentialRules.isDependentKey(childSide);

        int parentMult = parentSide.getMultiplicity().getValue();
        int childMult = childSide.getMultiplicity().getValue();

        if (parentSide.getInsertRule() == null) {
            ORValidationRule rule = referentialRules.getDefaultRule(parentMult, childMult,
                    dependantKey, ReferentialRules.INSERT, ReferentialRules.PARENT_SIDE);
            parentSide.setInsertRule(rule);
            actionsGenerated++;
        }

        if (parentSide.getUpdateRule() == null) {
            ORValidationRule rule = referentialRules.getDefaultRule(parentMult, childMult,
                    dependantKey, ReferentialRules.UPDATE, ReferentialRules.PARENT_SIDE);
            parentSide.setUpdateRule(rule);
            actionsGenerated++;
        }

        if (parentSide.getDeleteRule() == null) {
            ORValidationRule rule = referentialRules.getDefaultRule(parentMult, childMult,
                    dependantKey, ReferentialRules.DELETE, ReferentialRules.PARENT_SIDE);
            parentSide.setDeleteRule(rule);
            actionsGenerated++;
        }

        if (childSide.getInsertRule() == null) {
            ORValidationRule rule = referentialRules.getDefaultRule(parentMult, childMult,
                    dependantKey, ReferentialRules.INSERT, ReferentialRules.CHILD_SIDE);
            childSide.setInsertRule(rule);
            actionsGenerated++;
        }

        if (childSide.getUpdateRule() == null) {
            ORValidationRule rule = referentialRules.getDefaultRule(parentMult, childMult,
                    dependantKey, ReferentialRules.UPDATE, ReferentialRules.CHILD_SIDE);
            childSide.setUpdateRule(rule);
            actionsGenerated++;
        }

        if (childSide.getDeleteRule() == null) {
            ORValidationRule rule = referentialRules.getDefaultRule(parentMult, childMult,
                    dependantKey, ReferentialRules.DELETE, ReferentialRules.CHILD_SIDE);
            childSide.setDeleteRule(rule);
            actionsGenerated++;
        }

        return actionsGenerated;
    } // end generateReferentialActions()

    // find parent side according its multiplicity
    private DbORAssociationEnd findParentSide(DbORAssociationEnd backEnd,
            DbORAssociationEnd frontEnd) throws DbException {
        DbORAssociationEnd parentSide = null;
        int backMult = backEnd.getMultiplicity().getValue();
        int frontMult = frontEnd.getMultiplicity().getValue();

        boolean backOne = (backMult == SMSMultiplicity.OPTIONAL)
                || (backMult == SMSMultiplicity.EXACTLY_ONE);
        boolean frontOne = (frontMult == SMSMultiplicity.OPTIONAL)
                || (frontMult == SMSMultiplicity.EXACTLY_ONE);

        if (backOne && !frontOne) {
            parentSide = frontEnd;
        } else if (!backOne && frontOne) {
            parentSide = backEnd;
        }

        return parentSide;
    }

    /*
     * private ActionSet findAppropriateActionSet(int parentMult, int childMult, boolean
     * dependantKey) { int row = -1;
     * 
     * //find the row switch (parentMult) { case SMSMultiplicity.MANY: switch (childMult) { case
     * SMSMultiplicity.OPTIONAL: row = 0; break; case SMSMultiplicity.EXACTLY_ONE: row =
     * dependantKey ? 2 : 1; break; } //end switch break; case SMSMultiplicity.ONE_OR_MORE: switch
     * (childMult) { case SMSMultiplicity.OPTIONAL: row = 3; break; case
     * SMSMultiplicity.EXACTLY_ONE: row = dependantKey ? 5 : 4; break; } //end switch break; } //end
     * switch()
     * 
     * //set the action set row *= 3;
     * 
     * ReferentialRuleGeneration referentialRules = ReferentialRuleGeneration.getInstance();
     * 
     * ActionSet actionSet = new ActionSet(); actionSet.parentInsertAction =
     * REFERENTIAL_RULE_MATRIX[row+INSERT][PARENT_SIDE]; actionSet.parentUpdateAction =
     * REFERENTIAL_RULE_MATRIX[row+UPDATE][PARENT_SIDE]; actionSet.parentDeleteAction =
     * REFERENTIAL_RULE_MATRIX[row+DELETE][PARENT_SIDE]; actionSet.childInsertAction =
     * REFERENTIAL_RULE_MATRIX[row+INSERT][CHILD_SIDE]; actionSet.childUpdateAction =
     * REFERENTIAL_RULE_MATRIX[row+UPDATE][CHILD_SIDE]; actionSet.childDeleteAction =
     * REFERENTIAL_RULE_MATRIX[row+DELETE][CHILD_SIDE];
     * 
     * return actionSet; }
     */

    //
    // number of rules to generate
    //
    public static class RuleCounts {
        public int[] nbDefinedRules = new int[3];
        public int[] nbRules = new int[3];

        public int getNbMissingRules() {
            int nbRules = getNbRules();
            int nbDefined = nbDefinedRules[ReferentialRules.INSERT]
                    + nbDefinedRules[ReferentialRules.UPDATE]
                    + nbDefinedRules[ReferentialRules.DELETE];
            return nbRules - nbDefined;
        }

        public int getNbRules() {
            int nb = nbRules[ReferentialRules.INSERT] + nbRules[ReferentialRules.UPDATE]
                    + nbRules[ReferentialRules.DELETE];
            return nb;
        }
    }

    //
    // the action matrix
    //  
    /*
     * public static final int INSERT = 0; public static final int UPDATE = 1; public static final
     * int DELETE = 2;
     * 
     * public static final int PARENT_SIDE = 0; public static final int CHILD_SIDE = 1;
     */
    /*
     * public static final int[][] REFERENTIAL_RULE_MATRIX = new int[][] { //Parent-Side 0..N
     * //Child-Side 0..1 new int[] {ORValidationRule.NONE, ORValidationRule.SETNULL}, //INSERT new
     * int[] {ORValidationRule.NONE, ORValidationRule.SETNULL}, //UPDATE new int[]
     * {ORValidationRule.SETNULL, ORValidationRule.NONE}, //DELETE //Parent-Side 0..N //Child-Side
     * 1..1 new int[] {ORValidationRule.NONE, ORValidationRule.RESTRICT}, //INSERT new int[]
     * {ORValidationRule.NONE, ORValidationRule.RESTRICT}, //UPDATE new int[]
     * {ORValidationRule.RESTRICT, ORValidationRule.NONE}, //DELETE //Parent-Side 0..N //Child-Side
     * 1..1 Dependent-Key new int[] {ORValidationRule.NONE, ORValidationRule.RESTRICT}, //INSERT new
     * int[] {ORValidationRule.NONE, ORValidationRule.NOTALLOWED}, //UPDATE new int[]
     * {ORValidationRule.RESTRICT, ORValidationRule.NONE}, //DELETE //Parent-Side 1..N //Child-Side
     * 0..1 new int[] {ORValidationRule.ATTACH, ORValidationRule.SETNULL}, //INSERT new int[]
     * {ORValidationRule.NONE, ORValidationRule.RESTRICT}, //UPDATE new int[]
     * {ORValidationRule.SETNULL, ORValidationRule.RESTRICT}, //DELETE //Parent-Side 1..N
     * //Child-Side 1..1 new int[] {ORValidationRule.PROPAGATE, ORValidationRule.RESTRICT}, //INSERT
     * new int[] {ORValidationRule.NONE, ORValidationRule.RESTRICT}, //UPDATE new int[]
     * {ORValidationRule.CASCADE, ORValidationRule.RESTRICT}, //DELETE //Parent-Side 1..N
     * //Child-Side 1..1 Dependent-Key new int[] {ORValidationRule.PROPAGATE,
     * ORValidationRule.RESTRICT}, //INSERT new int[] {ORValidationRule.NONE,
     * ORValidationRule.NOTALLOWED}, //UPDATE new int[] {ORValidationRule.CASCADE,
     * ORValidationRule.RESTRICT}, //DELETE };
     */

    private static class ActionSet {
        int parentInsertAction;
        int parentUpdateAction;
        int parentDeleteAction;
        int childInsertAction;
        int childUpdateAction;
        int childDeleteAction;
    }

} // GeneratePrimaryKeysAction

