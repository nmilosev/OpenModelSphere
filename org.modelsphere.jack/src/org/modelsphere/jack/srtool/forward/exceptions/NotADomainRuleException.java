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
 * Thrown by Property
 * 
 * When we refer to a rule, such as isNullPossibleDomain in the following example, with the DOM
 * modifier, we expect to find isNullPossibleDomain defined as a CDOM rule; if it is not, then a
 * NotADomainRuleException is thrown.
 * 
 * column ATTR ColumnIsnullpossible, DOM = isNullPossibleDomain;
 * 
 * isNullPossible TEMPL "";
 */
public final class NotADomainRuleException extends RuleException {

    private static final String MESSAGE_PATTERN = LocaleMgr.message
            .getString("NotACharacterDomain");

    public static String buildMessage(String domain) {
        String msg = MessageFormat.format(MESSAGE_PATTERN, new Object[] { domain });
        return msg;
    }

    public NotADomainRuleException(String msg) {
        super(msg);
    }
}
