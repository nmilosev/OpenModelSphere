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

public final class CharacterDomain extends Rule {
    Rule[] rules = null;
    String[] stringRules;
    ArrayList m_domainList = new ArrayList(); // contains string lists
    public static final String DEFAULT_STRING = "ORG.MODELSPHERE.SR_DEFAULT"; // NOT LOCALIZABLE
    public static final String NULL_STRING = "ORG.MODELSPHERE.SR_NULL"; // NOT LOCALIZABLE

    private void processDomainStrings(String[] strings) {
        int nb = strings.length;
        for (int i = 0; i < nb; i++) {
            ArrayList subList = new ArrayList();
            String domain = strings[i];

            if (domain.equals("")) {
                // this is a special case
                subList.add("");
            } else {
                StringTokenizer st = new StringTokenizer(domain, ",");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    if (token.charAt(0) == '\"') {
                        token = token.substring(1);
                    }
                    if (token.charAt(token.length() - 1) == '\"') {
                        token = token.substring(0, token.length() - 1);
                    }
                    subList.add(token);
                } // end while
            }

            m_domainList.add(subList);
        }
    }

    public CharacterDomain(String ruleName, String[] someStrings, Rule[] someRules) {
        super(ruleName);
        String[] strings = someStrings;
        rules = someRules;
        processDomainStrings(strings);
    }

    public CharacterDomain(String ruleName, String[] someStrings, String[] someStringRules) {
        super(ruleName);
        String[] strings = someStrings;
        stringRules = someStringRules;
        processDomainStrings(strings);
    }

    // set actual actual subrules from its string rule;
    public void setActualSubRules(Hashtable table) {
        if (stringRules == null) {
            return;
        }

        int nb = stringRules.length;
        rules = new Rule[nb];
        for (int i = 0; i < nb; i++) {
            String s = stringRules[i];

            // TODO: from s='$domain;', extract subRules={'domain'}
            TemplateEnumeration enumeration = new TemplateEnumeration(s);
            ArrayList list = new ArrayList();

            while (enumeration.hasMoreElements()) {
                StringStructure ss = (StringStructure) enumeration.nextElement();
                list.add(ss);
            } // end while

            int size = list.size();
            Rule[] subRules = new Rule[size];
            for (int j = 0; j < size; j++) {
                StringStructure ss = (StringStructure) list.get(j);
                String str = ss.stringRule;
                if (str != null) {
                    str = str.substring(1, str.length() - 1); // in '$rule;',
                    // remove '$'
                    // and ';'
                    subRules[j] = (Rule) table.get(str);
                }
            } // end for

            Rule r = new Template(s, s, subRules, null, new Modifier[] {});
            // r.setActualSubRules(table);
            rules[i] = r;
        } // end for
    }

    public boolean expand(Writer output, Serializable object, RuleOptions options) {
        // Do nothing
        return true;
    }

    public boolean expand(Writer output, Serializable object, RuleOptions options, String aString)
            throws IOException, RuleException {
        boolean expanded = false;
        boolean contained = false;

        int nbDomains = m_domainList.size();
        for (int i = 0; i < nbDomains; i++) {
            ArrayList subList = (ArrayList) m_domainList.get(i);
            if (subList.contains(aString)) {
                Rule r = rules[i];
                expanded |= r.expand(output, object, options);
                contained = true;
            } // end if
        } // end for

        // get the DEFAULT clause, if any (always the last one)
        if (contained == false) {
            ArrayList sr_default = (ArrayList) m_domainList.get(nbDomains - 1);
            if (sr_default.contains(DEFAULT_STRING)) {
                Rule r = rules[nbDomains - 1];
                expanded |= r.expand(output, object, options);
                contained = true;
            } // end if
        } // end if

        super.terminate(output, options);

        return expanded;
    }
}
