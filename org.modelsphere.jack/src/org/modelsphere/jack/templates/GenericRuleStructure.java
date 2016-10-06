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

package org.modelsphere.jack.templates;

import java.util.ArrayList;

import org.modelsphere.jack.srtool.forward.*;
import org.modelsphere.jack.srtool.forward.exceptions.TemplateFormatException;
import org.modelsphere.jack.util.StringUtil;

//A .tpl file is a list of several RuleStructures
public class GenericRuleStructure {
    // Rule Categories
    public static final int CONNECTOR = 0;
    public static final int PROPERTY = 1;
    public static final int TEMPLATE = 2;
    public static final int CDOM = 3;
    public static final int USERFN = 10;
    public static final int GROUP = 11;
    public static final int CONDITION = 12;
    public static final int VAR_DECL = 13;
    public static final int SET_VAR = 14;
    public static final int GET_VAR = 15;
    public static final int FOREACH_ITER = 16;
    public static final int FOREACH_ENUM = 17;
    public static final int ADD_TO_LIST = 18;
    public static final int CLEAR_LIST = 19;
    public static final int IMPORT = 20;
    public static final int USERCONN = 21;

    // Structure fields
    public int m_ruleCategory;
    private String m_ruleName;
    private String m_ruleText;
    private String m_subRule;
    private String m_beanClassFile;

    public String[] m_stringRules;
    public String[] m_stringDomains;
    public GenericModifierStructure[] m_modifiers;
    public BooleanModifier m_booleanModifier;

    // Variables-related
    public String m_varname;
    public String m_listname;
    public int m_type = 0;
    public Object m_value;

    GenericRuleStructure(int category, String classFile) {
        m_ruleCategory = category; // assert IMPORT
        m_beanClassFile = classFile;
    }

    GenericRuleStructure(int category, String ruleName, String ruleText, String subRule,
            String[] stringRules, String[] stringDomains, GenericModifierStructure[] modifiers) {
        m_ruleCategory = category;
        m_ruleName = ruleName;
        m_ruleText = ruleText;
        m_subRule = subRule;
        m_stringRules = stringRules;
        m_stringDomains = stringDomains;
        m_modifiers = modifiers;
    }

    GenericRuleStructure(int category, String ruleName, String varname, String type, Object value) {

        m_ruleCategory = category;
        m_ruleName = ruleName;
        m_varname = varname;
        m_value = value;

        // TODO: set m_type according type
        m_type = VariableDecl.STRING;
    }

    GenericRuleStructure(int category, String ruleName, String varname, String listname,
            String subRule, GenericModifierStructure[] modifiers) {
        m_ruleCategory = category; // asser FOREACH
        m_ruleName = ruleName;
        m_varname = varname;
        m_listname = listname;
        m_subRule = subRule;
        m_modifiers = modifiers;
    }

    public final String getRuleName() {
        return m_ruleName;
    }

    public final String getRuleText() {
        return m_ruleText;
    }

    public final String getSubRule() {
        return m_subRule;
    }

    public final String getBeanClassFile() {
        return m_beanClassFile;
    }

    final static class GenericTemplateStructure extends GenericRuleStructure {
        GenericTemplateStructure(String ruleName, String ruleText, String aDomQual,
                GenericModifierStructure[] modifiers) {
            super(TEMPLATE, ruleName, ruleText, aDomQual, null, null, modifiers);

            validateRuleText(ruleText); // throws IllegalArgumentException if
            // invalid

            TemplateEnumeration enumeration = new TemplateEnumeration(ruleText);
            ArrayList ruleList = new ArrayList();
            while (enumeration.hasMoreElements()) {
                StringStructure ss = (StringStructure) enumeration.nextElement();
                if (ss.stringRule != null) {
                    String s = ss.stringRule;
                    s = s.substring(1, s.length() - 1);
                    ruleList.add(s);
                }
            } // end while

            m_stringRules = new String[ruleList.size()];

            for (int i = 0; i < ruleList.size(); i++) {
                String s = (String) ruleList.get(i);
                m_stringRules[i] = s;
            } // end for
        } // end GenericTemplateStructure()

        // throws IllegalArgumentException if invalid
        private void validateRuleText(String ruleText) {
            // an empty string is perfectly legal
            if (ruleText.length() == 0) {
                return;
            }

            // raises an exception if last character is a dollar sign
            if (ruleText.charAt(ruleText.length() - 1) == '$') {
                String msg = TemplateFormatException.buildMessage(
                        TemplateFormatException.LAST_CHARACTER_IS_DOLLAR_SIGN, ruleText);
                throw new IllegalArgumentException(msg);
            } // end if

            // raise an exception if there are two consecutive dollar signs
            int idx = StringUtil.consecutiveCharacters(ruleText, '$');
            if (idx != -1) {
                String msg = TemplateFormatException.buildMessage(
                        TemplateFormatException.TWO_CONSECUTIVE_DOLLAR_SIGNS, ruleText);
                throw new IllegalArgumentException(msg);
            } // end if

        } // end validateRuleText()
    } // end GenericTemplateStructure

    static class GenericConnectorStructure extends GenericRuleStructure {
        GenericConnectorStructure(String aRuleName, String aConnector, String aChildQual,
                String aOneChild, GenericModifierStructure[] modifiers) {
            super(CONNECTOR, aRuleName, aConnector, aChildQual, new String[] { aOneChild }, null,
                    modifiers);
        }
    } // end GenericConnectorStructure

    static class GenericConditionStructure extends GenericRuleStructure {
        GenericConditionStructure(String aRuleName, BooleanModifier condition, String aRuleList[],
                GenericModifierStructure[] modifiers) {
            super(CONDITION, aRuleName, null, null, aRuleList, null, modifiers);
            super.m_booleanModifier = condition;
        }
    } // end GenericConnectorStructure

    static class GenericGroupStructure extends GenericRuleStructure {
        GenericGroupStructure(String aRuleName, String aGroup, GenericModifierStructure[] modifiers) {
            super(GROUP, aRuleName, aGroup, null, null, null, modifiers);
        }
    } // end GenericConnectorStructure

    static class GenericPropertyStructure extends GenericRuleStructure {
        GenericPropertyStructure(String aRuleName, String anAttribute, String aDomQual,
                GenericModifierStructure[] modifiers) {
            super(PROPERTY, aRuleName, anAttribute, aDomQual, null, null, modifiers);
        }
    } // end GenericConnectorStructure

    static class GenericCDomStructure extends GenericRuleStructure {
        GenericCDomStructure(String aRuleName, String[] domains, String[] rules,
                GenericModifierStructure[] modifiers) {
            super(CDOM, aRuleName, null, null, rules, domains, modifiers);
        }
    } // end GenericConnectorStructure

    static class GenericUserFnStructure extends GenericRuleStructure {
        GenericUserFnStructure(String aRuleName, String anUserFn, String aSubRule,
                GenericModifierStructure[] modifiers) {
            super(USERFN, aRuleName, anUserFn, aSubRule, null, null, modifiers);
        }
    } // end GenericConnectorStructure

    static class GenericUserConnStructure extends GenericRuleStructure {
        GenericUserConnStructure(String aRuleName, String anUserFn, String aSubRule,
                GenericModifierStructure[] modifiers) {
            super(USERCONN, aRuleName, anUserFn, aSubRule, null, null, modifiers);
        }
    } // end GenericConnectorStructure

    static class GenericVariableDeclStructure extends GenericRuleStructure {
        GenericVariableDeclStructure(String varname, String type) {
            super(VAR_DECL, null, varname, type, null);
        }
    }

    static class GenericSetVariableStructure extends GenericRuleStructure {
        GenericSetVariableStructure(String aRuleName, String varname, Object value) {
            super(SET_VAR, aRuleName, varname, null, value);
        }
    }

    static class GenericAddToListStructure extends GenericRuleStructure {
        GenericAddToListStructure(String aRuleName, String varname, Object value) {
            super(ADD_TO_LIST, aRuleName, varname, null, value);
        }
    }

    static class GenericClearListStructure extends GenericRuleStructure {
        GenericClearListStructure(String aRuleName, String varname) {
            super(CLEAR_LIST, aRuleName, varname, null, null);
        }
    }

    static class GenericGetVariableStructure extends GenericRuleStructure {
        GenericGetVariableStructure(String aRuleName, String varname) {
            super(GET_VAR, aRuleName, varname, null, null);
        }
    }

    static class GenericForEachIterStructure extends GenericRuleStructure {
        GenericForEachIterStructure(String aRuleName, String varname, String listname,
                String subRule, GenericModifierStructure[] modifiers) {
            super(FOREACH_ITER, aRuleName, varname, listname, subRule, modifiers);
        }
    }

    static class GenericForEachEnumStructure extends GenericRuleStructure {
        GenericForEachEnumStructure(String aRuleName, String varname, String listname,
                String subRule, GenericModifierStructure[] modifiers) {
            super(FOREACH_ENUM, aRuleName, varname, listname, subRule, modifiers);
        }
    }

    static class GenericImportStructure extends GenericRuleStructure {
        GenericImportStructure(String beanClassFile) {
            super(IMPORT, beanClassFile);
        }
    }
} // end of RuleStructure
