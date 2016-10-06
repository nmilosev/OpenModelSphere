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

import java.io.*;
import java.util.Hashtable;
import java.util.ArrayList;

import org.modelsphere.jack.srtool.forward.exceptions.TypeMismatchRuleException;
import org.modelsphere.jack.srtool.forward.exceptions.VariableNotDefinedRuleException;

public final class SetVariable extends Rule {

    // possible values of m_operation
    public static final int SET_VARIABLE = 0;
    public static final int ADD_TO_LIST = 1;
    public static final int CLEAR_LIST = 2;

    // possible values of m_kindOf (may also be SET_VARIABLE)
    private static final int SET_RULE = 1;
    private static final int SET_CONVERT = 2;
    private static final int SET_COMPUTE = 3;
    private static final int SET_BOOLEAN = 4;
    private static final int SET_INTEGER = 5;
    private static final int SET_STRING = 6;

    private VariableDecl.VariableStructure m_targetVariable;
    private VariableDecl.VariableStructure m_sourceVariable = null; // for SET_VARIABLE & SET_CONVERT

    private String m_ruleString = null;
    private Rule m_rule;
    private String m_varname;
    private VariableScope m_varList;
    private int m_kindOf;
    private String m_convertKind;
    private Modifier m_modifier = null;
    private int m_operation;
    private String m_literalString = null;

    public SetVariable(int operation, VariableScope varList, String rulename, String varname,
            Object value) throws RuleException, VariableNotDefinedRuleException {
        super(rulename);
        m_operation = operation;
        m_targetVariable = (VariableDecl.VariableStructure) varList.getVariable(varname);

        if (m_targetVariable == null) {
            String msg = VariableNotDefinedRuleException.buildMessage(m_ruleName, varname);
            throw new VariableNotDefinedRuleException(msg);
        }

        // store variables to be used in expand()
        m_varList = varList;
        m_varname = varname;

        String ruleString = (value instanceof String) ? (String) value : null;
        Modifier modifier;
        if (value instanceof BooleanModifier) {
            modifier = (BooleanModifier) value;
            m_kindOf = SET_BOOLEAN;
        } else if (value instanceof IntegerModifier) {
            modifier = (IntegerModifier) value;
            m_kindOf = SET_INTEGER;
        } else if (value instanceof Rule) {
            m_rule = (Rule) value;
            m_kindOf = SET_RULE;
            modifier = null;
        } else {
            modifier = null;
        }

        if (ruleString != null) {
            setString(ruleString);
        } else if (modifier != null) {
            m_modifier = modifier;
        }

    } // end SetVariable()

    private void setString(String ruleString) throws RuleException, VariableNotDefinedRuleException {
        int equalsIdx = ruleString.indexOf('=');
        if (equalsIdx == -1) {
            if (ruleString.charAt(0) == '\"') {
                m_kindOf = SET_STRING;
                m_literalString = unquote(ruleString);
            } else {
                m_kindOf = SET_VARIABLE;
                m_sourceVariable = (VariableDecl.VariableStructure) m_varList
                        .getVariable(ruleString);
                m_ruleString = ruleString; // store the variable's name

                if (m_sourceVariable == null) {
                    String msg = VariableNotDefinedRuleException.buildMessage(m_ruleName,
                            ruleString);
                    throw new VariableNotDefinedRuleException(msg);
                }

                // Throw TypeMismatch if target's and source's types differ
                if (m_targetVariable.getType() != m_sourceVariable.getType()) {
                    String msg = TypeMismatchRuleException.buildMessage(m_ruleName,
                            m_targetVariable.getName(), m_sourceVariable.getName());
                    throw new TypeMismatchRuleException(msg);
                } // end if
            } // end if
        } else {
            String keyword = ruleString.substring(0, equalsIdx);
            if (keyword.equals("RULE")) { // NOT LOCALIZABLE, keyword
                m_kindOf = SET_RULE;
                m_ruleString = ruleString.substring(equalsIdx + 1); // m_rule
                // will be
                // set later
                // (in
                // setActualSubRules())
            } else if (keyword.equals("COMPUTE")) { // NOT LOCALIZABLE, keyword
                m_kindOf = SET_COMPUTE;
                m_ruleString = ruleString.substring(equalsIdx + 1);
            } else {
                m_kindOf = SET_CONVERT;
                m_convertKind = keyword; // TOSTRING or TOINTEGER
                m_ruleString = ruleString.substring(equalsIdx + 1); // store
                // variable
                m_sourceVariable = (VariableDecl.VariableStructure) m_varList
                        .getVariable(m_ruleString);

                if (m_sourceVariable == null) {
                    String msg = VariableNotDefinedRuleException.buildMessage(m_ruleName,
                            m_ruleString);
                    throw new VariableNotDefinedRuleException(msg);
                }

                if (m_convertKind.equals("TOSTRING")) { // NOT LOCALIZABLE,
                    // keyword
                    if (m_targetVariable.getType() != VariableDecl.STRING) {
                        String msg = TypeMismatchRuleException.buildMessage(m_ruleName,
                                m_targetVariable.getName(), "TOSTRING()"); // NOT LOCALIZABLE, keyword
                        throw new TypeMismatchRuleException(msg);
                    }
                } else if (m_convertKind.equals("TOINTEGER")) { // NOT
                    // LOCALIZABLE,
                    // keyword
                    if (m_targetVariable.getType() != VariableDecl.INTEGER) {
                        String msg = TypeMismatchRuleException.buildMessage(m_ruleName,
                                m_targetVariable.getName(), "TOINTEGER()"); // NOT LOCALIZABLE, keyword
                        throw new TypeMismatchRuleException(msg);
                    }
                } // end if
            }
        } // end if
    } // end setString

    // set actual actual subrules from its string rule;
    public void setActualSubRules(Hashtable table) throws RuleException {
        // find rule from ruleString
        if (m_rule == null) {
            if (m_kindOf == SET_RULE) {
                m_rule = (Rule) table.get(m_ruleString);
            }
        }

        // change modifiers with their actual rules (if any)
        super.setActualModifiers(table);
    }

    // Expand nothing
    // @param output : ignored
    public boolean expand(Writer output, Serializable object, RuleOptions options)
            throws /* DbException, */IOException, RuleException {
        boolean expanded = false;

        // compute value
        Serializable value = null;

        if (m_targetVariable != null) {
            if (m_operation != CLEAR_LIST) { // don't have to compute value if
                // it's a clear operation
                switch (m_kindOf) {
                case SET_RULE:
                    if (m_rule != null) {
                        StringWriter writer = new StringWriter();
                        m_rule.expand(writer, object, options);
                        value = writer.toString();
                        expanded = true;
                    } // end if
                    break;
                case SET_VARIABLE:
                    value = m_sourceVariable.getValue();
                    expanded = true;
                    break;
                case SET_CONVERT:
                    if (m_convertKind.equals("TOSTRING")) { // NOT
                        // LOCALIZABLE,
                        // keyword
                        value = m_sourceVariable.getValue().toString();
                        expanded = true;
                    } else if (m_convertKind.equals("TOINTEGER")) { // NOT
                        // LOCALIZABLE,
                        // keyword
                        int i = Integer.parseInt(m_sourceVariable.getValue().toString());
                        value = new Integer(i);
                    }
                    break;
                case SET_COMPUTE:
                    // ignore
                    break;
                case SET_BOOLEAN:
                    BooleanModifier bm = (BooleanModifier) m_modifier;
                    boolean bValue = bm.evaluate(object);
                    value = new Boolean(bValue);
                    expanded = true;
                    break;
                case SET_INTEGER:
                    IntegerModifier im = (IntegerModifier) m_modifier;
                    int intValue = im.evaluate(object);
                    value = new Integer(intValue);
                    expanded = true;
                    break;
                case SET_STRING:
                    value = m_literalString;
                    expanded = true;
                    break;
                } // end switch
            } // end if

            // set or add values?
            ArrayList list;
            switch (m_operation) {
            case SET_VARIABLE:
                m_targetVariable.setValue(value);
                break;
            case ADD_TO_LIST:
                list = (ArrayList) m_targetVariable.getValue();
                list.add(value);
                break;
            case CLEAR_LIST:
                list = (ArrayList) m_targetVariable.getValue();
                list.clear();
                break;
            } // end switch
        } // end if

        return expanded;
    }

    // To move in StringUtil?
    private static String unquote(String s) {
        if (s == null)
            return null;

        if (s.length() < 2)
            return null;

        if (s.charAt(0) != '\"')
            return null;

        int len = s.length();
        if (s.charAt(len - 1) != '\"')
            return null;

        String unquoted = s.substring(1, len - 1);
        return unquoted;
    } // end unqtote

} // SetVariable
