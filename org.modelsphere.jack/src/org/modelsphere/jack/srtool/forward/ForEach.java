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

import java.io.Writer;
import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Iterator;

import org.modelsphere.jack.srtool.forward.exceptions.EndlessLoopRuleException;
import org.modelsphere.jack.srtool.forward.exceptions.VariableNotDefinedRuleException;
import org.modelsphere.jack.templates.GenericModifierStructure;

public class ForEach extends Rule {

    private VariableDecl.VariableStructure m_targetVariable;
    private VariableDecl.VariableStructure m_list;
    private String m_childStrRule;
    private Rule m_childRule;
    private IntegerModifier m_start;
    private IntegerModifier m_end;
    private boolean isIter;
    private int m_duplication;

    public ForEach(VariableScope varList, String rulename, String varname, String childRule,
            IntegerModifier start, IntegerModifier end) throws VariableNotDefinedRuleException {
        super(rulename);
        isIter = true;
        m_targetVariable = (VariableDecl.VariableStructure) varList.getVariable(varname);

        if (m_targetVariable == null) {
            String msg = VariableNotDefinedRuleException.buildMessage(rulename, varname);
            throw new VariableNotDefinedRuleException(msg);
        }

        m_childStrRule = childRule;
        m_start = start;
        m_end = end;
    } // end ForEach()

    public ForEach(VariableScope varList, String rulename, String varname, String childRule,
            String listname, int duplication) throws VariableNotDefinedRuleException {
        super(rulename);
        isIter = false;
        m_targetVariable = (VariableDecl.VariableStructure) varList.getVariable(varname);
        m_list = (VariableDecl.VariableStructure) varList.getVariable(listname);
        m_duplication = duplication;

        if (m_targetVariable == null) {
            String msg = VariableNotDefinedRuleException.buildMessage(rulename, varname);
            throw new VariableNotDefinedRuleException(msg);
        }

        if (m_list == null) {
            String msg = VariableNotDefinedRuleException.buildMessage(rulename, listname);
            throw new VariableNotDefinedRuleException(msg);
        }

        m_childStrRule = childRule;
    } // end ForEach()

    // set actual actual subrules from its string rule;
    public void setActualSubRules(Hashtable table) throws RuleException {
        // find childRule from stringRule
        m_childRule = (Rule) table.get(m_childStrRule);

        // change modifiers with their actual rules
        super.setActualModifiers(table);
    } // end setActualSubRules

    // expand()
    private boolean expandIter(Writer writer, Serializable obj, RuleOptions options)
            throws IOException, RuleException {
        boolean expanded = false;

        // start iterator
        int currentValue = m_start.evaluate(obj);
        m_targetVariable.setValue(new Integer(currentValue));

        // while end value not reached
        int nbIterations = 0;
        while (currentValue <= m_end.evaluate(obj)) {

            // expand child
            expanded |= m_childRule.expand(writer, obj, options);

            // get iterator value (may have been changed by the child rule)
            Integer intValue = (Integer) m_targetVariable.getValue();
            currentValue = intValue.intValue();

            // increment iterator
            currentValue++;
            m_targetVariable.setValue(new Integer(currentValue));

            // prevent endless loop
            nbIterations++;
            if (nbIterations > 1024) {
                String msg = "FOREACH > 1024"; // NOT LOCALIZABLE, FOREACH is a
                // keyword
                throw new EndlessLoopRuleException(msg);
            } // end if
        } // end while

        return expanded;
    } // end expandIter()

    private boolean expandAllEnums(Writer writer, Serializable obj, RuleOptions options)
            throws /* DbException, */IOException, RuleException {
        boolean expanded = false;

        ArrayList list = (ArrayList) m_list.getValue();
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            // set enumerator
            Serializable o = (Serializable) iter.next();
            m_targetVariable.setValue(o);

            // expand child
            expanded |= m_childRule.expand(writer, obj, options);
        } // end while

        return expanded;
    } // end expandAllEnums()

    private boolean expandDuplicatedEnums(Writer writer, Serializable obj, RuleOptions options,
            int duplication) throws /* DbException, */IOException, RuleException {
        boolean expanded = false;

        // find duplicates
        ArrayList list = (ArrayList) m_list.getValue();
        ArrayList uniList = new ArrayList(); // list of unique occurrences
        ArrayList dupList = new ArrayList(); // list od duplicated occurrences

        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Serializable o = (Serializable) iter.next();

            // if o already detected as duplicated
            if (dupList.contains(o)) {
                // nothing to do
            } else if (uniList.contains(o)) { // if o was unique until now
                uniList.remove(o); // not unique anymore
                dupList.add(o); // duplicated instead
            } else { // first time o is found
                uniList.add(o); // it is unique
            }
        } // end while

        // enumerates duplicates
        iter = (duplication == GenericModifierStructure.UNIQUE_MODIFIER) ? uniList.iterator()
                : dupList.iterator();
        while (iter.hasNext()) {
            // set enumerator
            Serializable o = (Serializable) iter.next();
            m_targetVariable.setValue(o);

            // expand child
            expanded |= m_childRule.expand(writer, obj, options);
        } // end while

        return expanded;
    } // end expandDuplicatedEnums()

    private boolean expandEnum(Writer writer, Serializable obj, RuleOptions options)
            throws /* DbException, */IOException, RuleException {
        return (m_duplication != GenericModifierStructure.NONE) ? expandDuplicatedEnums(writer,
                obj, options, m_duplication) : expandAllEnums(writer, obj, options);
    } // end expandEnum()

    public boolean expand(Writer writer, Serializable obj, RuleOptions options)
            throws /* DbException, */IOException, RuleException {
        return (isIter) ? expandIter(writer, obj, options) : expandEnum(writer, obj, options);
    } // end expand()

} // end ForEach

