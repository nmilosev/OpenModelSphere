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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import java.io.*;

import org.modelsphere.jack.srtool.forward.exceptions.GroupRuleException;
import org.modelsphere.jack.srtool.forward.exceptions.SubruleMissingRuleException;

public final class Group extends Rule {
    Rule[] rules;
    String stringRules; // comma-separated

    // CONSTRUCTORS
    public Group(String ruleName, Rule[] someRules) {
        super(ruleName);
        rules = someRules;
    }

    public Group(String ruleName, String someStringRules, Modifier[] someModifiers) {
        super(ruleName, someModifiers);
        stringRules = someStringRules;
    }

    // set actual subrules from its string rule;
    public void setActualSubRules(Hashtable table) throws RuleException {
        if (stringRules != null) {
            ArrayList list = new ArrayList();
            StringTokenizer st = new StringTokenizer(stringRules, ",");
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                Rule elementRule = (Rule) table.get(token);
                list.add(elementRule);
            } // end while

            // TODO: put them in rules[]
            int nb = list.size();
            rules = new Rule[nb];
            for (int i = 0; i < nb; i++) {
                rules[i] = (Rule) list.get(i);
            } // end for
        } // end if

        // change modifiers with their actual rules
        super.setActualModifiers(table);
    }

    // EXPAND
    public boolean expand(Writer output, Serializable object, RuleOptions options)
            throws /* DbException, */IOException, RuleException {
        boolean groupExpanded = false;
        boolean elementExpanded = false;
        boolean lastElementExpanded = false;

        boolean excluded = verifyExclusion(object, options);
        if (excluded) {
            return false;
        }

        try {
            if (rules != null) {
                StringWriter groupBuffer = new StringWriter();
                StringWriter elementBuffer = new StringWriter();

                int nb = rules.length;
                for (int i = 0; i < nb; i++) {
                    // expand rule in the element buffer
                    Rule rule = rules[i];
                    if (rule == null) {
                        String msg = SubruleMissingRuleException.buildMessage(m_ruleName,
                                stringRules);
                        throw new SubruleMissingRuleException(msg);
                    }

                    elementExpanded = (rule == null) ? false : rule.expand(elementBuffer, object,
                            options);
                    groupExpanded |= elementExpanded;

                    // if the last element was expanded (never true at the 1st
                    // iteration)
                    if (lastElementExpanded) {
                        // AND if the current one is expanded
                        if (elementExpanded) {
                            // expand the SEParator, if any
                            if (separatorModifier != null) {
                                separatorModifier.expand(groupBuffer, object, options);
                            } // end if
                        } // end if
                    } // end if

                    // dump content of elementBuffer in groupBuffer, if any
                    if (elementExpanded) {
                        String element = elementBuffer.toString();
                        groupBuffer.write(element);
                    }

                    // clear element buffer
                    elementBuffer = new StringWriter();

                    // keep lastElementExpanded for the next iteration
                    if (elementExpanded) {
                        lastElementExpanded = true;
                    }
                } // end for

                if (groupExpanded == true) {
                    if (prefixModifier != null) {
                        prefixModifier.expand(output, object, options);
                    }

                    String s = groupBuffer.toString();
                    output.write(s);
                } // end if
            } // end if

            // write suffix, if any
            if ((groupExpanded) && (suffixModifier != null)) {
                suffixModifier.expand(output, object, options);
            }

            if (!groupExpanded) {
                if (nullModifier != null) {
                    groupExpanded = nullModifier.expand(output, object, options);
                }
            }

            super.terminate(output, options);
        } catch (NullPointerException ex) {
            String msg = GroupRuleException.buildMessage(m_ruleName, stringRules);
            throw new RuleException(msg);
        }

        return groupExpanded;
    }
}
