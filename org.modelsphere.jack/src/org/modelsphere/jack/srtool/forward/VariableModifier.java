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

import java.util.Hashtable;
import java.io.*;

/**
 * <H1>EXECUTION VARIABLES</H1><BR>
 * Execution variables are strings that are assigned at execution time. User can create how many
 * execution variables she wants, but each of them must have a different name.<BR>
 * <BR>
 * Rule variables of any class can assign values variables; the execution variable X is assigned by
 * specified the modifier VariableModifier(X, rule), where X is the name of the variable, and rule
 * is the rule that is immediately processed to generate the string to store. For example,
 * VariableModifier('action', new Template('INSERT')) assigns the string 'INSERT' to the execution
 * variable 'action'. The second parameter of the VariableModifier() constructor is a Rule; this
 * Rule is processed immediately at the level of the template is assigned to the execution variable
 * that defines it, and the result string of the processed rule is assigned to the execution
 * variable. In other words, execution variables contains strings, not rules.<BR>
 * <BR>
 * A value assigned to an execution variable is accessible only to Rule variable that assigned it
 * and its descendant.
 */
public final class VariableModifier extends Modifier {
    private static final String kRule = "$rule;"; // NOT LOCALIZABLE, internal use only

    static Hashtable allVariables = new Hashtable();
    String varKey;
    Rule rule;

    public VariableModifier(String aVarKey, Rule aRule) {
        super(kRule, new Rule[] { aRule });
        varKey = aVarKey;
        rule = aRule;
    }

    // called by the defining rule
    // set result member, output is ignored
    public boolean expand(Writer output, Serializable object) throws IOException {
        boolean expanded = false;
        StringWriter writer = new StringWriter();

        // TODO: uncomment
        // if (m_tmpl != null)
        // expanded |= m_tmpl.expand(writer, object);

        // if expanded, store result
        if (expanded) {
            String result = writer.toString();
            allVariables.put(varKey, result);
        }

        return expanded;
    } // end expand()

    public static String getValueOf(String varKey) {
        String value = (String) allVariables.get(varKey);
        return value;
    }

    public static void clear() {
        allVariables.clear();
    }
}
