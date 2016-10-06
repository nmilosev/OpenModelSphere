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

package org.modelsphere.jack.srtool.forward.exceptions;

import org.modelsphere.jack.srtool.forward.*;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

/**
 * Thrown by Property, Template, UserDefinedField
 * 
 * This exception is thrown when we refer to a non-existing rule with the DOMain keyword. In the
 * following example, we refer to the rule domain_1 in the template templ_1; but domain_1 has not
 * been defined, so a DomainNotFoundRuleException is thrown:
 * 
 * templ_1 TEMPL "", DOM = domain_1;
 * 
 * domain_2 CDOM (..)
 * 
 * 
 */
public final class DomainNotFoundRuleException extends RuleException {

    private static final String MESSAGE_PATTERN = LocaleMgr.message
            .getString("ReferredDomainNotFound");

    public static String buildMessage(String rulename) {
        return MessageFormat.format(MESSAGE_PATTERN, new Object[] { rulename });
    }

    public DomainNotFoundRuleException(String msg) {
        super(msg);
    }
}
