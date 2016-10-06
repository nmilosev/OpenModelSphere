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

import org.modelsphere.jack.srtool.forward.exceptions.VariableNotDefinedRuleException;
import org.modelsphere.jack.srtool.forward.exceptions.VariableNotInitializedRuleException;

public final class GetVariable extends Rule {

    private String m_varname = null;
    private VariableScope m_varList;

    public GetVariable(VariableScope varList, String rulename, String varname) throws RuleException {
        super(rulename);
        m_varname = varname;
        m_varList = varList;
    }

    // ignore object, just write 'value' in the output
    public boolean expand(Writer output, Serializable object, RuleOptions options)
            throws IOException, RuleException {
        boolean expanded = false;

        VariableDecl.VariableStructure struct = (VariableDecl.VariableStructure) m_varList
                .getVariable(m_varname);

        if (struct == null) {
            String msg = VariableNotDefinedRuleException.buildMessage(m_ruleName, m_varname);
            throw new RuleException(msg);
        }

        Serializable value = struct.getValue();

        if (value == null) {
            String msg = VariableNotInitializedRuleException.buildMessage(m_ruleName, m_varname);
            throw new VariableNotInitializedRuleException(msg);
        }

        String s = value.toString();
        output.write(s);
        expanded = true;

        return expanded;
    }

} // SetVariable
