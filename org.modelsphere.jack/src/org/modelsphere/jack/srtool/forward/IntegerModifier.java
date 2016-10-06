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

import org.modelsphere.jack.srtool.forward.exceptions.VariableNotDefinedRuleException;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class IntegerModifier extends Modifier {
    public static final int INTEGER_VARIABLE = 0;
    public static final int INTEGER_CONSTANT = 1;
    public static final int INTEGER_NEGATION = 2;
    public static final int INTEGER_ADDITION = 3;
    public static final int INTEGER_SUBSTRACTION = 4;
    public static final int INTEGER_MULTIPLICATION = 5;
    public static final int INTEGER_DIVISION = 6;
    public static final int INTEGER_LENGTH = 7; // length of a string

    private int m_operation;
    private int m_intValue;
    private VariableDecl.VariableStructure m_variable = null; // an integer or a
    // string
    // variable
    private IntegerModifier m_operand1;
    private IntegerModifier m_operand2;

    private static final String VARIABLE_NOT_FOUND_PATTERN = LocaleMgr.message
            .getString("VariableNotDefined2");

    public IntegerModifier(VariableScope scope, String varname)
            throws VariableNotDefinedRuleException {
        super(null);
        m_operation = INTEGER_VARIABLE;

        // Just verify that variable has been already defined
        m_variable = (VariableDecl.VariableStructure) scope.getVariable(varname);
        if (m_variable == null) {
            String msg = MessageFormat.format(VARIABLE_NOT_FOUND_PATTERN, new Object[] { varname });
            throw new VariableNotDefinedRuleException(msg);
        } // end if
    } // end IntegerModifier()

    public IntegerModifier(String value) {
        super(null);
        m_operation = INTEGER_CONSTANT;
        m_intValue = Integer.parseInt(value);
    }

    public IntegerModifier(int operation, IntegerModifier im1) {
        super(null);
        // assert operation == INTEGER_NEGATION
        m_operation = operation;
        m_operand1 = im1;
    } // end IntegerModifier

    public IntegerModifier(int operation, VariableScope scope, String varname)
            throws VariableNotDefinedRuleException {
        super(null);
        // assert operation == INTEGER_LENGTH
        m_operation = operation;

        // Just verify that variable has been already defined
        m_variable = (VariableDecl.VariableStructure) scope.getVariable(varname);
        if (m_variable == null) {
            String msg = MessageFormat.format(VARIABLE_NOT_FOUND_PATTERN, new Object[] { varname });
            throw new VariableNotDefinedRuleException(msg);
        } // end if
    }

    public IntegerModifier(int operation, IntegerModifier im1, IntegerModifier im2) {
        super(null);
        // assert operation is a 2-part operation such as INTEGER_ADDITION, ..
        m_operation = operation;
        m_operand1 = im1;
        m_operand2 = im2;
    } // end IntegerModifier

    public int evaluate(Serializable object) throws RuleException {
        int result = 0;

        try {
            switch (m_operation) {
            case INTEGER_VARIABLE:
                Integer value = (Integer) m_variable.getValue();
                result = value.intValue();
                break;
            case INTEGER_CONSTANT:
                result = m_intValue;
                break;
            case INTEGER_NEGATION:
                result = -(m_operand1.evaluate(object));
                break;
            case INTEGER_ADDITION:
                result = m_operand1.evaluate(object) + m_operand2.evaluate(object);
                break;
            case INTEGER_SUBSTRACTION:
                result = m_operand2.evaluate(object) - m_operand1.evaluate(object); // substract op1 from
                // op2
                break;
            case INTEGER_MULTIPLICATION:
                result = m_operand1.evaluate(object) * m_operand2.evaluate(object);
                break;
            case INTEGER_DIVISION:
                result = m_operand1.evaluate(object) / m_operand2.evaluate(object);
                break;
            case INTEGER_LENGTH:
                String strValue = m_variable.getValue().toString();
                result = strValue.length();
                break;
            default:
                result = 0;
            } // end switch()
        } catch (ArithmeticException ex) {
            String msg = ex.getMessage();
            throw new RuleException(msg);
        } // end try

        return result;
    } // evaluate()
}
