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

package org.modelsphere.jack.srtool.forward;

import java.io.Serializable;
import java.text.MessageFormat;

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.srtool.forward.exceptions.VariableNotDefinedRuleException;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class BooleanModifier extends Modifier {
    public static final int BOOLEAN_VARIABLE = 0;
    public static final int NOT_EXPRESSION = 1;
    public static final int AND_OPERATION = 2;
    public static final int OR_OPERATION = 3;
    public static final int GREATER_THAN = 4;
    public static final int LESS_THAN = 5;
    public static final int EQUALS = 6;
    public static final int INSTANCEOF_EXPRESSION = 7;
    public static final int BOOLEAN_CONSTANT = 8;
    public static final int TEXT_PREDICATE = 9;

    private int m_operation;
    private Modifier m_operand1 = null, m_operand2 = null;
    private VariableDecl.VariableStructure m_variable = null; // a boolean
    // variable
    private MetaClass m_mc = null;
    private boolean m_booleanConstant;

    private static final String ERROR_PATTERN = LocaleMgr.message.getString("VariableNotDefined2");

    public BooleanModifier(VariableScope scope, String varname)
            throws VariableNotDefinedRuleException {
        super(null);
        m_operation = BOOLEAN_VARIABLE;
        m_variable = (VariableDecl.VariableStructure) scope.getVariable(varname);
        if (m_variable == null) {
            String errMsg = MessageFormat.format(ERROR_PATTERN, new Object[] { varname });
            throw new VariableNotDefinedRuleException(errMsg);
        } // end if
    } // end BooleanModifier()

    public BooleanModifier(int operation, BooleanModifier operand) {
        super(null);
        // assert operation is NOT
        m_operation = operation;
        m_operand1 = operand;
    }

    public BooleanModifier(int operation, Modifier operand1, Modifier operand2) {
        super(null);
        // assert operation is OR, AND, <, > or ==
        m_operation = operation;
        m_operand1 = operand1;
        m_operand2 = operand2;
    }

    public BooleanModifier(int operation, MetaClass mc) {
        super(null);
        // assert operation is INSTANCEOF
        m_operation = operation;
        m_mc = mc;
    }

    public BooleanModifier(boolean trueOrFalse) {
        super(null);
        m_operation = BOOLEAN_CONSTANT;
        m_booleanConstant = trueOrFalse;
    }

    // variables only used for TEXT_PREDICATE
    private int m_predicate = -1;
    private int m_operand = -1;
    private static final int CONTAINS = 0, ONLYCONTAINS = 1, STARTSWITH = 2;
    private static final int LETTER = 0, DIGIT = 1, UPPER = 2, LOWER = 3, UNDERSCORE = 4;

    public BooleanModifier(int operation, String pred, String operand, VariableScope scope,
            String varname) throws VariableNotDefinedRuleException {
        super(null);
        // assert operation is TEXT_PREDICATE
        m_operation = TEXT_PREDICATE;
        m_variable = (VariableDecl.VariableStructure) scope.getVariable(varname);
        if (m_variable == null) {
            String errMsg = MessageFormat.format(ERROR_PATTERN, new Object[] { varname });
            throw new VariableNotDefinedRuleException(errMsg);
        } // end if

        if (pred.equals("CONTAINS")) { // NOT LOCALIZABLE, keyword
            m_predicate = CONTAINS;
        } else if (pred.equals("ONLYCONTAINS")) { // NOT LOCALIZABLE, keyword
            m_predicate = ONLYCONTAINS;
        } else if (pred.equals("STARTSWITH")) { // NOT LOCALIZABLE, keyword
            m_predicate = STARTSWITH;
        } // end if

        if (operand.equals("LETTER")) { // NOT LOCALIZABLE, keyword
            m_operand = LETTER;
        } else if (operand.equals("DIGIT")) { // NOT LOCALIZABLE, keyword
            m_operand = DIGIT;
        } else if (operand.equals("UPPER")) { // NOT LOCALIZABLE, keyword
            m_operand = UPPER;
        } else if (operand.equals("LOWER")) { // NOT LOCALIZABLE, keyword
            m_operand = LOWER;
        } else if (operand.equals("UNDERSCORE")) { // NOT LOCALIZABLE, keyword
            m_operand = UNDERSCORE;
        } // end if
    }

    private boolean contains(int operand, String s) {
        boolean result = false;

        switch (operand) {
        case LOWER:
            for (char ch = 'a'; ch <= 'z'; ch++) {
                int idx = s.indexOf(ch);
                if (idx != -1) {
                    result = true;
                    break;
                }
            } // end for
            break;
        case UPPER:
            for (char ch = 'A'; ch <= 'Z'; ch++) {
                int idx = s.indexOf(ch);
                if (idx != -1) {
                    result = true;
                    break;
                }
            } // end for
            break;
        case DIGIT:
            for (char ch = '0'; ch <= '9'; ch++) {
                int idx = s.indexOf(ch);
                if (idx != -1) {
                    result = true;
                    break;
                }
            } // end for
        case LETTER:
            result = contains(LOWER, s) || contains(UPPER, s);
            break;
        case UNDERSCORE:
            result = (s.indexOf('_') != -1) ? true : false;
            break;
        } // and switch

        return result;
    } // end contains()

    private boolean onlyContains(int operand, String s) {
        boolean result = true;
        int nb = s.length();

        switch (operand) {
        case LOWER:
            for (int i = 0; i < nb; i++) {
                char ch = s.charAt(i);
                if (!Character.isLowerCase(ch)) {
                    result = false;
                    break;
                }
            } // end for
            break;
        case UPPER:
            for (int i = 0; i < nb; i++) {
                char ch = s.charAt(i);
                if (!Character.isUpperCase(ch)) {
                    result = false;
                    break;
                }
            } // end for
            break;
        case LETTER:
            for (int i = 0; i < nb; i++) {
                char ch = s.charAt(i);
                if (!Character.isLetter(ch)) {
                    result = false;
                    break;
                }
            } // end for
            break;
        case DIGIT:
            for (int i = 0; i < nb; i++) {
                char ch = s.charAt(i);
                if (!Character.isDigit(ch)) {
                    result = false;
                    break;
                }
            } // end for
            break;
        case UNDERSCORE:
            for (int i = 0; i < nb; i++) {
                char ch = s.charAt(i);
                if (ch != '_') {
                    result = false;
                    break;
                }
            } // end for
            break;
        } // and switch

        return result;
    } // end contains()

    private boolean evaluatePredicate() {
        boolean result = false;
        String s = m_variable.getValue().toString();

        switch (m_predicate) {
        case CONTAINS:
            result = contains(m_operand, s);
            break;
        case ONLYCONTAINS:
            // result = onlyContains(m_operand, s);
            break;
        case STARTSWITH:
            // result = startsWith(m_operand, s);
            break;
        default:
            result = false;
            break;
        } // end switch

        return result;
    }

    public boolean evaluate(Serializable object) throws RuleException {
        boolean result;

        BooleanModifier bm1 = (m_operand1 instanceof BooleanModifier) ? (BooleanModifier) m_operand1
                : null;
        BooleanModifier bm2 = (m_operand2 instanceof BooleanModifier) ? (BooleanModifier) m_operand2
                : null;
        IntegerModifier im1 = (m_operand1 instanceof IntegerModifier) ? (IntegerModifier) m_operand1
                : null;
        IntegerModifier im2 = (m_operand2 instanceof IntegerModifier) ? (IntegerModifier) m_operand2
                : null;

        switch (m_operation) {
        case BOOLEAN_VARIABLE:
            Boolean value = (Boolean) m_variable.getValue();
            result = value.booleanValue();
            break;
        case NOT_EXPRESSION:
            result = !bm1.evaluate();
            break;
        case AND_OPERATION:
            result = bm1.evaluate() && bm2.evaluate();
            break;
        case OR_OPERATION:
            result = bm1.evaluate() || bm2.evaluate();
            break;
        case GREATER_THAN:
            result = im1.evaluate(object) > im2.evaluate(object);
            break;
        case LESS_THAN:
            result = im1.evaluate(object) < im2.evaluate(object);
            break;
        case EQUALS:
            result = im1.evaluate(object) == im2.evaluate(object);
            break;
        case INSTANCEOF_EXPRESSION:
            if (object == null)
                throw new IllegalArgumentException(
                        "evaluate()'s parameter cannot be null if operation is INSTANCEOF"); // NOT
            // LOCALIZABLE,
            // not
            // yet
            // supported
            if (object instanceof DbObject) {
                DbObject dbObj = (DbObject) object;
                MetaClass mc = dbObj.getMetaClass();
                result = (mc == m_mc) ? true : false;
            } else {
                result = false;
            }
            break;
        case BOOLEAN_CONSTANT:
            result = m_booleanConstant;
            break;
        case TEXT_PREDICATE:
            result = evaluatePredicate();
            break;
        default:
            result = false;
        } // end switch()

        return result;
    } // evaluate()

    private boolean evaluate() throws RuleException {
        return evaluate(null);
    }

    // UNIT TEST
    public static void main(String[] args) throws RuleException {
        VariableScope scope = new VariableScope("unittest"); // NOT LOCALIZABLE,
        // unit test

        BooleanModifier bm1 = null, bm2 = null;
        try {
            VariableDecl.declare(scope, "toto", VariableDecl.BOOLEAN, "FALSE", false); // NOT LOCALIZABLE, unit test
            bm1 = new BooleanModifier(scope, "toto"); // NOT LOCALIZABLE, unit
            // test
            bm2 = new BooleanModifier(BooleanModifier.NOT_EXPRESSION, bm1);
        } catch (RuleException ex) {
            System.out.println("exception = " + ex.toString());
        } catch (VariableNotDefinedRuleException ex) {
            System.out.println("exception = " + ex.toString());
        }

        System.out.println("result = " + bm2.evaluate());

    } // main()
}
