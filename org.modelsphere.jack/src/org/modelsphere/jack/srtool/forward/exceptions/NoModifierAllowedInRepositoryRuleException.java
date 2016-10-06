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
 * Thrown by UserDefinedField, Property and Connector
 * 
 * This exception is thrown when we try to use a modifier (PREF, SUF, SEP, NULL, and so on) in a
 * USERFN clause, or in a ATTR or CONN clause calling a repository function.
 * 
 * Because anyone can write his/her own repository functions, if modifiers would be allowed on
 * repository functions, it would be up to its implementor to deal with the all the twelve different
 * kinds of modifier. Because it's too dangerous to forget to take in account one of the modifiers,
 * all the modifiers are prohibited on any repository function.
 * 
 * As a work-around, we can wrap the repository function with a template on which we can define the
 * required modifiers.
 * 
 * templ_1 USERFN ObjectId, PREF = "<ObjectId=", //NOT LOCALIZABLE SUF = ">";
 * 
 * can be rewritten like this:
 * 
 * templ_1 TEMPL "$wrappedTempl;", //NOT LOCALIZABLE PREF = "<ObjectId=", //NOT LOCALIZABLE SUF =
 * ">";
 * 
 * wrappedTempl USERFN ObjectId;
 */
public final class NoModifierAllowedInRepositoryRuleException extends RuleException {

    private static final String MESSAGE_PATTERN = LocaleMgr.message
            .getString("NoModifiersAllowedInRepository");

    public static String buildMessage(String rulename) {
        String msg = MessageFormat.format(MESSAGE_PATTERN, new Object[] { rulename });
        return msg;
    } // end buildMessage();

    public NoModifierAllowedInRepositoryRuleException(String msg) {
        super(msg);
    }
}
