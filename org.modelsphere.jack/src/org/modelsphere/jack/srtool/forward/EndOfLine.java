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

import org.modelsphere.jack.srtool.forward.exceptions.NegativeMarginRuleException;

public final class EndOfLine extends EditionCode {
    private static final String EOL = System.getProperty("line.separator");
    private static final String NEW_LINE = "$n;"; // NOT LOCALIZABLE
    private static final int MAX_INDENTATION = 80;

    /*
     * Create an array of already-built indentations to speed up getIndent()
     */
    private static String[] g_indentations = new String[MAX_INDENTATION];
    static {
        for (int i = 0; i < MAX_INDENTATION; i++) {
            StringBuffer indentation = new StringBuffer(i + 4);
            indentation.append(EOL);
            for (int j = 0; j < i; j++) {
                indentation.append(" ");
            }

            g_indentations[i] = indentation.toString();
        } // end for
    }

    /*
     * Very slow (called only if g_margin > MAX_INDENTATION)
     */
    private static String buildIndent() {
        StringBuffer indentation = new StringBuffer(g_margin + 4);
        indentation.append(EOL);
        for (int i = 0; i < g_margin; i++) {
            indentation.append(" ");
        } // end for

        return indentation.toString();
    }

    /*
     * This method must be VERY fast (can be called more than 10 000 times!)
     */
    static final String getIndent() {
        if (g_margin < 0) {
            return null;
        }
        return (g_margin < MAX_INDENTATION) ? g_indentations[g_margin] : buildIndent();
    }

    void checkError() throws RuleException {
        if (g_margin < 0) {
            String msg = NegativeMarginRuleException.buildMessage(m_ruleName);
            throw new NegativeMarginRuleException(msg);
        }
    }

    String process() throws RuleException {
        checkError();
        return getIndent();
    }

    public boolean expand(Writer output, Serializable object, RuleOptions options)
            throws IOException, RuleException {
        String s = NEW_LINE;
        output.write(s);

        return true;
    }
}
