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

/**
 * A Choice rule is constructed with three parameters, the first one being a boolean condition, and
 * the two other ones being Template rules.
 * 
 * A Choice rule behaves like the ?: operator in Java: it evaluates a boolean expression: if its
 * value is true, it expands the first template; if it's not, it expands the second one.
 * 
 */
public final class Choice extends Rule {
    private Rule m_trueRule;
    private Rule m_falseRule;
    private String m_trueRuleString;
    private String m_falseRuleString;

    // private static final String NO_IF_CONDITION_PATTRN =
    // LocaleMgr.message.getString("IfConditionMissing");

    public Choice(String ruleName, String trueCondition, String falseCondition, Modifier[] modifiers) {
        super(ruleName, modifiers);
        m_trueRuleString = trueCondition;
        m_falseRuleString = falseCondition;
    } // end Choice()

    // set actual actual subrules from its string rule;
    public void setActualSubRules(Hashtable table) throws RuleException {
        // find true and false conditions, if any

        if (m_trueRuleString != null)
            m_trueRule = (Rule) table.get(m_trueRuleString);

        if (m_falseRuleString != null)
            m_falseRule = (Rule) table.get(m_falseRuleString);

        // change modifiers with their actual rules
        super.setActualModifiers(table);
    }

    private BooleanModifier getBooleanModifier() {
        return super.m_booleanModifier;
    }

    private boolean expandChoice(Writer output, Serializable object, RuleOptions options)
            throws IOException, RuleException {
        boolean expanded = false;
        BooleanModifier bm = getBooleanModifier();
        boolean value = bm.evaluate(object);
        if (value == true) {
            if (m_trueRule != null) {
                expanded = m_trueRule.expand(output, object, options);
            }
        } else { // value is false
            if (m_falseRule != null) {
                expanded = m_falseRule.expand(output, object, options);
            }
        } // end if
        return expanded;
    } // end expandChoice()

    public boolean expand(Writer output, Serializable object, RuleOptions options)
            throws IOException, RuleException {

        // expanded in a string writer
        StringWriter writer = new StringWriter();
        boolean expanded = expandChoice(writer, object, options);

        if (expanded) {
            if (super.prefixModifier != null) {
                prefixModifier.expand(output, object, options);
            }

            output.write(writer.toString());

            if (super.suffixModifier != null) {
                suffixModifier.expand(output, object, options);
            }
        } else { // if not expanded
            if (super.nullModifier != null) {
                nullModifier.expand(output, object, options);
            }
        } // end if

        super.terminate(output, options);
        return expanded;
    } // end expand()
} // end Choice
