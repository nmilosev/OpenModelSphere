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
import java.util.*;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.forward.Rule.RuleOptions;
import org.modelsphere.jack.srtool.forward.exceptions.EditionCodesNotAllowedRuleException;

public abstract class Modifier implements Serializable {
    Template m_tmpl = null;
    Rule[] m_rules;
    String m_format;

    public Modifier(String aString) {
        this(aString, new Rule[] {});
    }

    Modifier(String aString, Rule[] rules) {
        m_format = aString;
        m_rules = rules;
    }

    public boolean expand(Writer output, Serializable object, RuleOptions options)
            throws IOException, RuleException {
        boolean expanded = false;

        if (m_tmpl != null)
            expanded |= m_tmpl.expand(output, object, options);
        else
            Debug.trace("NULL");

        return expanded;
    }

    // if a kind of rule (template, connector, etc.) supports string subrules,
    // then set actual actual subrules from its string rules; (this method is
    // then overriden in the given kind of rule).
    // otherwise, do nothing.
    public void setActualSubRules(Hashtable table, boolean editCodesAllowed) throws RuleException {
        // extract rule names from m_tmpl
        TemplateEnumeration enumeration = new TemplateEnumeration(m_format);
        ArrayList list = new ArrayList();

        while (enumeration.hasMoreElements()) {
            StringStructure ss = (StringStructure) enumeration.nextElement();
            list.add(ss);
        } // end while

        int nb = list.size();
        int rawRulesIndex = 0;
        Rule[] subActualRules = new Rule[nb];
        for (int i = 0; i < nb; i++) {
            StringStructure ss = (StringStructure) list.get(i);
            if (ss.editionCode != null) {
                if (!editCodesAllowed) {
                    throw new EditionCodesNotAllowedRuleException();
                }

                subActualRules[i] = ss.editionCode;
            } else if (ss.stringRule != null) {
                String rulename = ss.stringRule;
                rulename = rulename.substring(1, rulename.length() - 1);
                Rule r;
                char c0 = rulename.charAt(0);
                if ((c0 == 'm') && ((rulename.charAt(1) == '+') || (rulename.charAt(1) == '-'))) {
                    throw new EditionCodesNotAllowedRuleException();
                } else {
                    r = (Rule) table.get(rulename); // find actual rule from
                    // table
                }

                r.setActualSubRules(table);
                subActualRules[i] = r;
            } else {
                String s = ss.str;
                Template t = new Template(s, s, new Rule[] {}, null, new Modifier[] {});
                subActualRules[i] = t;
            } // end if
            //
        } // end for

        // set m_temp's rules
        m_tmpl = new Template(m_format, m_format, subActualRules, null, new Modifier[] {});
        m_tmpl.setActualRules(subActualRules);
    }
}
