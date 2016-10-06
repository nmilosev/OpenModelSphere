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

import org.modelsphere.jack.srtool.forward.exceptions.DomainNotFoundRuleException;
import org.modelsphere.jack.srtool.forward.exceptions.NoModifierAllowedInRepositoryRuleException;

/* UserDefinedFunction Class */
public abstract class UserDefinedField extends Rule {
    private String m_subrule;
    private Rule m_actualSubrule = null;

    protected Rule getSubRule() {
        return m_actualSubrule;
    }

    public UserDefinedField() {
    } // Parameter-less constructor required by jack.io.Plugins

    public UserDefinedField(String ruleName, String subrule, Modifier[] modifiers)
            throws RuleException {
        super(ruleName);
        if (modifiers.length > 0) {
            String msg = NoModifierAllowedInRepositoryRuleException.buildMessage(ruleName);
            throw new NoModifierAllowedInRepositoryRuleException(msg);
        }

        m_subrule = subrule;
    }

    // set actual actual subrules from its string rule;
    public void setActualSubRules(Hashtable table) throws RuleException {

        if (m_subrule != null) {
            m_actualSubrule = (Rule) table.get(m_subrule);
        }

        // change modifiers with their actual rules
        super.setActualModifiers(table);
    } // end setActualSubRules

    protected String getPropertyName() {

        // //
        // check if there are quotes, if so remove them
        // the template parser/compiler should block the relay of the "rule"
        // if not quotes are present, as it will attempt to expand it as a
        // subrule instead

        String sSubruleNoQuotes = m_subrule;

        int nLen = m_subrule.length();

        if (nLen >= 2) {
            if (m_subrule.charAt(0) == '\"' && m_subrule.charAt(nLen - 1) == '\"') {
                sSubruleNoQuotes = m_subrule.substring(1, nLen - 1); 
            }
        }
        return sSubruleNoQuotes;
    }

    protected boolean m_domainMode = false;

    protected boolean expandWithDomainModifier(Writer output, Serializable object,
            RuleOptions options) throws IOException, RuleException {
        boolean expanded;

        CharacterDomain cdom = domainModifier.getDomain();
        StringWriter sw = new StringWriter();
        m_domainMode = true;
        expanded = expand(sw, object, options);
        m_domainMode = false;
        String strValue = sw.toString();

        if (cdom == null) {
            String msg = DomainNotFoundRuleException.buildMessage(m_ruleName);
            throw new DomainNotFoundRuleException(msg);
        }

        expanded |= cdom.expand(output, object, options, strValue);
        return expanded;
    } // end expandWithDomainModifier()

    protected void processModifier(Writer output, Serializable object, RuleOptions options)
            throws /* DbException, */IOException, RuleException {
        boolean expanded = false;

        // special case if domain is specified
        if (!m_domainMode) { // to avoid endless loop
            if (domainModifier != null) {
                expanded |= expandWithDomainModifier(output, object, options);
                super.terminate(output, options);
                // return expanded;
            } // end if
        } // end if
    } // end processModifier()

    public abstract boolean expand(Writer output, Serializable object, Rule.RuleOptions options)
            throws /* DbException, */IOException, RuleException;
} // end UserDefinedField
