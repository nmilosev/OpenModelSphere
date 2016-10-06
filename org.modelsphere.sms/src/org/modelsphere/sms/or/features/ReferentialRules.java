package org.modelsphere.sms.or.features;

import java.text.MessageFormat;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.srtypes.ORValidationRule;

/**
 * This class contains the logic of determining which referential rule is appropriate with each case
 * specified by parent and child multiplicities, and dependent-key.
 * 
 * For instance, with: Parent's multiplicity of 0..n and child's multiplicity of 0..1 default INSERT
 * rule is NONE default UPDATE rule is NONE default DELETE rule is SET_NULL
 * 
 * @author msavard
 * 
 */
public class ReferentialRules {

    // singleton pattern
    private static ReferentialRules instance = null;

    private ReferentialRules() {
    }

    public static ReferentialRules getInstance() {
        if (instance == null) {
            instance = new ReferentialRules();
        }

        return instance;
    }

    // constants
    public static final int INSERT = 0;
    public static final int UPDATE = 1;
    public static final int DELETE = 2;

    public static final int PARENT_SIDE = 0;
    public static final int CHILD_SIDE = 1;

    /**
     * Generates the default rule for the case specified by the parameters
     * 
     * Note: because the isDependentKey() method is called each time this method is called, if the
     * client plans to make several calls to getDefaultRule(), to avoid re-executing the same method
     * several times, it is more efficient to call isDependentKey() externally, and then use the
     * getDefaultRule() implementation that takes the parentMult, childMult and dependantKey as
     * parameters.
     * 
     * @param parentSide
     *            : the side with 0..n or 1..n multiplicity
     * @param action
     *            : one of {INSERT, UPDATE, DELETE}
     * @param side
     *            : one of {PARENT_SIDE, CHILD_SIDE}
     * 
     * @return the default rule
     */
    public ORValidationRule getDefaultRule(DbORAssociationEnd parentSide, int action, int side)
            throws DbException {

        DbORAssociationEnd childSide = parentSide.getOppositeEnd();
        boolean dependantKey = isDependentKey(childSide);
        int parentMult = parentSide.getMultiplicity().getValue();
        int childMult = childSide.getMultiplicity().getValue();
        ORValidationRule rule = getDefaultRule(parentMult, childMult, dependantKey, action, side);

        return rule;
    }

    /**
     * Generates the default rule for the case specified by the parameters
     * 
     * @param parentMult
     *            : one of {SMSMultiplicity.MANY, SMSMultiplicity.ONE_OR_MORE}
     * @param childMult
     *            : one of {SMSMultiplicity.OPTIONAL, SMSMultiplicity.EXACTLY_ONE}
     * @param dependantKey
     *            : true if 1..1 underscored
     * @param action
     *            : one of {INSERT, UPDATE, DELETE}
     * @param side
     *            : one of {PARENT_SIDE, CHILD_SIDE}
     * 
     * @return the default rule
     */
    public ORValidationRule getDefaultRule(int parentMult, int childMult, boolean dependantKey,
            int action, int side) {

        int ruleSet = getRuleSet(parentMult, childMult, dependantKey);
        ORValidationRule rule = getDefaultRule(ruleSet, action, side);
        return rule;
    }

    /**
     * Generates the allowed rules for the case specified by the parameters; the first rule is the
     * default rule.
     * 
     * @param parentMult
     *            : one of {SMSMultiplicity.MANY, SMSMultiplicity.ONE_OR_MORE}
     * @param childMult
     *            : one of {SMSMultiplicity.OPTIONAL, SMSMultiplicity.EXACTLY_ONE}
     * @param dependantKey
     *            : true if 1..1 underscored
     * @param action
     *            : one of {INSERT, UPDATE, DELETE}
     * @param side
     *            : one of {PARENT_SIDE, CHILD_SIDE}
     * 
     * @return the allowed rules
     */
    public ORValidationRule[] getAllowedRules(int parentMult, int childMult, boolean dependantKey,
            int action, int side) {

        int ruleSet = getRuleSet(parentMult, childMult, dependantKey);
        int[] allowedRules = REFERENTIAL_RULE_MATRIX[ruleSet * 3 + action][side];
        ORValidationRule[] rules = new ORValidationRule[allowedRules.length];

        for (int i = 0; i < allowedRules.length; i++) {
            rules[i] = ORValidationRule.getInstance(allowedRules[i]);
        }

        return rules;
    }

    /**
     * Generates the default rule for the case specified by the parameters
     * 
     * ruleSet is 0 if parent is SMSMultiplicity.MANY, child is SMSMultiplicity.OPTIONAL 1 if parent
     * is SMSMultiplicity.MANY, child is SMSMultiplicity.EXACTLY_ONE 2 if parent is
     * SMSMultiplicity.MANY, child is SMSMultiplicity.EXACTLY_ONE DEPENDENT 3 if parent is
     * SMSMultiplicity.ONE_OR_MORE, child is SMSMultiplicity.OPTIONAL 4 if parent is
     * SMSMultiplicity.ONE_OR_MORE, child is SMSMultiplicity.EXACTLY_ONE 5 if parent is
     * SMSMultiplicity.ONE_OR_MORE, child is SMSMultiplicity.EXACTLY_ONE DEPENDENT
     * 
     * @param ruleSet
     *            : 0 to 5
     * @param action
     *            : one of {INSERT, UPDATE, DELETE}
     * @param side
     *            : one of {PARENT_SIDE, CHILD_SIDE}
     * 
     * @return the default rule
     */
    public ORValidationRule getDefaultRule(int ruleSet, int action, int side) {
        int[] allowedRules = REFERENTIAL_RULE_MATRIX[ruleSet * 3 + action][side];
        ORValidationRule rule = ORValidationRule.getInstance(allowedRules[0]);
        return rule;
    }

    /**
     * Tells if the child side association end represents a dependent key (shown graphically as an
     * 1..1 underscored)
     * 
     * @param childSide
     *            the 1..1 association end
     * @return true if it is dependent
     * @throws DbException
     */
    public boolean isDependentKey(DbORAssociationEnd childSide) throws DbException {
        boolean dependantKey = false;
        DbORForeign fk = childSide.getMember();
        
        //prevents a NullPointerException
        if (fk == null) {
            return false;
        }
        
        DbORAbsTable table = childSide.getClassifier();
        DbRelationN relN = table.getComponents();
        DbEnumeration enu = relN.elements(DbORPrimaryUnique.metaClass);

        while (enu.hasMoreElements()) {
            DbORPrimaryUnique key = (DbORPrimaryUnique) enu.nextElement();
            if (key.isPrimary()) {
                dependantKey = shareCommonColumn(key, fk);
                if (dependantKey) {
                    break;
                }
            } // end if
        } // end while
        enu.close();

        return dependantKey;
    }

    private static int[] NONE = new int[] { ORValidationRule.NONE };
    private static int[] SETNULL_RESTRICT_CASC = new int[] { ORValidationRule.SETNULL,
            ORValidationRule.RESTRICT, ORValidationRule.CASCADE };
    private static int[] SETNULL_RESTRICT_PROP = new int[] { ORValidationRule.SETNULL,
            ORValidationRule.RESTRICT, ORValidationRule.PROPAGATE };
    private static int[] SETNULL_CASC_NOTALL = new int[] { ORValidationRule.SETNULL,
            ORValidationRule.CASCADE, ORValidationRule.NOTALLOWED };
    private static int[] RESTRICT = new int[] { ORValidationRule.RESTRICT };
    private static int[] RESTRICT_CASC = new int[] { ORValidationRule.RESTRICT,
            ORValidationRule.CASCADE };
    private static int[] RESTRICT_PROP = new int[] { ORValidationRule.RESTRICT,
            ORValidationRule.PROPAGATE };
    private static int[] RESTRICT_NOTALL = new int[] { ORValidationRule.RESTRICT,
            ORValidationRule.NOTALLOWED };
    private static int[] RESTRICT_PROP_NOTALL = new int[] { ORValidationRule.RESTRICT,
            ORValidationRule.PROPAGATE, ORValidationRule.NOTALLOWED };
    private static int[] NOTALLOWED = new int[] { ORValidationRule.NOTALLOWED };
    private static int[] PROPAGATE = new int[] { ORValidationRule.PROPAGATE };
    private static int[] CASCADE = new int[] { ORValidationRule.CASCADE };
    private static int[] CASCADE_NOTAL = new int[] { ORValidationRule.CASCADE,
            ORValidationRule.NOTALLOWED };
    private static int[] ATTACH_PROP = new int[] { ORValidationRule.ATTACH,
            ORValidationRule.PROPAGATE };

    /*
     * This matrix contains all the cases for referential rules
     */
    private static final int[][][] REFERENTIAL_RULE_MATRIX = new int[][][] {
    // Parent-Side 0..N //Child-Side 0..1
            new int[][] { NONE, SETNULL_RESTRICT_PROP }, // INSERT
            new int[][] { NONE, SETNULL_RESTRICT_PROP }, // UPDATE
            new int[][] { SETNULL_RESTRICT_CASC, NONE }, // DELETE
            // Parent-Side 0..N //Child-Side 1..1
            new int[][] { NONE, RESTRICT_PROP }, // INSERT
            new int[][] { NONE, RESTRICT_PROP_NOTALL }, // UPDATE
            new int[][] { RESTRICT_CASC, NONE }, // DELETE
            // Parent-Side 0..N //Child-Side 1..1 Dependent-Key
            new int[][] { NONE, RESTRICT_PROP }, // INSERT
            new int[][] { NONE, NOTALLOWED }, // UPDATE
            new int[][] { RESTRICT_CASC, NONE }, // DELETE
            // Parent-Side 1..N //Child-Side 0..1
            new int[][] { ATTACH_PROP, SETNULL_RESTRICT_PROP }, // INSERT
            new int[][] { NONE, RESTRICT }, // UPDATE
            new int[][] { SETNULL_CASC_NOTALL, RESTRICT_CASC }, // DELETE
            // Parent-Side 1..N //Child-Side 1..1
            new int[][] { PROPAGATE, RESTRICT_PROP }, // INSERT
            new int[][] { NONE, RESTRICT_NOTALL }, // UPDATE
            new int[][] { CASCADE, RESTRICT_CASC }, // DELETE
            // Parent-Side 1..N //Child-Side 1..1 Dependent-Key
            new int[][] { PROPAGATE, RESTRICT_PROP }, // INSERT
            new int[][] { NONE, NOTALLOWED }, // UPDATE
            new int[][] { CASCADE_NOTAL, RESTRICT_CASC }, // DELETE
    };

    //
    // private method
    //
    private int getRuleSet(int parentMult, int childMult, boolean dependantKey) {
        // find the row
        int ruleSet = 0;

        switch (parentMult) {
        case SMSMultiplicity.MANY:
            switch (childMult) {
            case SMSMultiplicity.OPTIONAL:
                ruleSet = 0;
                break;
            case SMSMultiplicity.EXACTLY_ONE:
                ruleSet = dependantKey ? 2 : 1;
                break;
            } // end switch
            break;
        case SMSMultiplicity.ONE_OR_MORE:
            switch (childMult) {
            case SMSMultiplicity.OPTIONAL:
                ruleSet = 3;
                break;
            case SMSMultiplicity.EXACTLY_ONE:
                ruleSet = dependantKey ? 5 : 4;
                break;
            } // end switch
            break;
        } // end switch()

        return ruleSet;
    }

    // Tells if pk and fk have at least one common column
    private boolean shareCommonColumn(DbORPrimaryUnique pk, DbORForeign fk) throws DbException {
        boolean shareCommonColumn = false;

        DbRelationN relN = pk.getColumns();
        DbEnumeration enu = relN.elements(DbORColumn.metaClass);
        while (enu.hasMoreElements()) {
            DbORColumn col = (DbORColumn) enu.nextElement();

            shareCommonColumn = containColumn(fk, col);
            if (shareCommonColumn) {
                break;
            } // end if
        } // end while
        enu.close();

        return shareCommonColumn;
    }

    private boolean containColumn(DbORForeign fk, DbORColumn col) throws DbException {
        boolean contained = false;

        DbRelationN relN = fk.getComponents();
        DbEnumeration enu = relN.elements(DbORFKeyColumn.metaClass);
        while (enu.hasMoreElements()) {
            DbORFKeyColumn kc = (DbORFKeyColumn) enu.nextElement();
            if (col.equals(kc.getColumn())) {
                contained = true;
                break;
            }
        } // end while
        enu.close();

        return contained;
    }

    //
    // how to use it
    //

    public static void main(String[] args) {
        ReferentialRules rules = ReferentialRules.getInstance();
        int[] parentMultiplicities = new int[] { SMSMultiplicity.MANY, SMSMultiplicity.ONE_OR_MORE };
        int[] childMultiplicities = new int[] { SMSMultiplicity.OPTIONAL,
                SMSMultiplicity.EXACTLY_ONE };
        String pattern = "Parent: {0}  Child : {1}";

        //for each parent and child multiplicity
        for (int parentMult : parentMultiplicities) {

            for (int childMult : childMultiplicities) {

                String msg = MessageFormat.format(pattern, SMSMultiplicity.getInstance(parentMult)
                        .toString(), SMSMultiplicity.getInstance(childMult).toString());
                System.out.println(msg);

                //for each action 
                for (int action = INSERT; action <= DELETE; action++) {
                    String actionName = getActionName(action);
                    System.out.println("  " + actionName);

                    ORValidationRule[] parentRules = rules.getAllowedRules(parentMult, childMult,
                            false, action, PARENT_SIDE);
                    String allowedRules = getAllowedRules(parentRules);
                    System.out.println("    parent : " + allowedRules);

                    ORValidationRule[] childRules = rules.getAllowedRules(parentMult, childMult,
                            false, action, CHILD_SIDE);
                    allowedRules = getAllowedRules(childRules);
                    System.out.println("    child : " + allowedRules);
                }

                System.out.println();
            } //end for
        } //end for
    } //end main()

    private static String getActionName(int action) {
        if (action == INSERT) {
            return "INSERT";
        } else if (action == UPDATE) {
            return "UPDATE";
        }

        return "DELETE";
    }

    private static String getAllowedRules(ORValidationRule[] rules) {
        String text = "";

        for (ORValidationRule rule : rules) {
            text += rule.toString() + " ";
        }

        return text;
    }
}
