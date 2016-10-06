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
import java.lang.reflect.Field;

import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

public final class NamedRule extends Rule {
    Rule actualRule = null;
    String className;
    String ruleName;
    private static final String ILLEGAL_ACCESS_PATERN = LocaleMgr.message
            .getString("IllegalAccessPattern");
    private static final String ERROR_PATTERN = LocaleMgr.message.getString("ErrorPattern");

    public NamedRule(String ruleName, String aClassName, String aRuleName) {
        super(ruleName);
        className = aClassName;
        ruleName = aRuleName;
    }

    public boolean expand(Writer output, Serializable obj, RuleOptions options) throws IOException,
            RuleException {
        boolean expanded = false;

        if (actualRule == null) {
            String msg = null;
            try {
                // forName() here is mandatory, because className is passed by
                // constructor.
                Class actualClass = Class.forName(className);
                Field field = actualClass.getField(ruleName);
                actualRule = (Rule) field.get(null); // null because static

            } catch (ClassNotFoundException ex) {
                msg = ex.toString();
            } catch (NoSuchFieldException ex) {
                msg = ex.toString();
            } catch (IllegalAccessException ex) {
                msg = MessageFormat.format(ILLEGAL_ACCESS_PATERN, new Object[] { ruleName });
            }

            // if fails
            if (msg != null) {
                String newMsg = MessageFormat.format(ERROR_PATTERN, new Object[] { msg });
                actualRule = new Template(null, newMsg);
            }
        }

        expanded = actualRule.expand(output, obj, options);
        super.terminate(output, options);
        return expanded;
    }
}
