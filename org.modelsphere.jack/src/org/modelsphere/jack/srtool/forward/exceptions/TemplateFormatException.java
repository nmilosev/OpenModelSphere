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

/**
 * Title:        Sms
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

import java.text.MessageFormat;

import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.templates.parsing.ParseException;

public class TemplateFormatException extends ParseException {
    public static final int ONE_CHARACTER = 0;
    public static final int MUST_BEGIN_BY_A_LETTER = 1;
    public static final int INVALID_CHARACTER = 2;
    public static final int LAST_CHARACTER_IS_DOLLAR_SIGN = 3;
    public static final int TWO_CONSECUTIVE_DOLLAR_SIGNS = 4;
    public static final int SEMI_COLON_IS_MISSING = 5;

    private static final String ONE_CHARACTER_RULE_MSG = LocaleMgr.message
            .getString("OneCharacterRule");
    private static final String MUST_BEGIN_BY_A_LETTER_MSG = LocaleMgr.message
            .getString("MustBeginByALetter");
    private static final String INVALID_CHARACTER_MSG = LocaleMgr.message
            .getString("InvalidCharacter");
    private static final String LAST_CHARACETER_IS_DOLLAR_SIGN_MSG = LocaleMgr.message
            .getString("LastCharacterIsDollarSign");
    private static final String TWO_CONSECUTIVE_DOLLAR_SIGNS_MSG = LocaleMgr.message
            .getString("TwoConsecutiveDollarSigns");
    private static final String SEMI_COLON_IS_MISSING_MSG = LocaleMgr.message
            .getString("SemiColonIsMissing");

    public static String buildMessage(int kindOf, String s) {
        String msg = null, fmt;

        switch (kindOf) {
        case ONE_CHARACTER:
            msg = MessageFormat.format(ONE_CHARACTER_RULE_MSG, new Object[] { s });
            break;
        case MUST_BEGIN_BY_A_LETTER:
            msg = MessageFormat.format(MUST_BEGIN_BY_A_LETTER_MSG, new Object[] { s });
            break;
        case INVALID_CHARACTER:
            msg = MessageFormat.format(INVALID_CHARACTER_MSG, new Object[] { s });
            break;
        case LAST_CHARACTER_IS_DOLLAR_SIGN:
            msg = MessageFormat.format(LAST_CHARACETER_IS_DOLLAR_SIGN_MSG, new Object[] { s });
            break;
        case TWO_CONSECUTIVE_DOLLAR_SIGNS:
            msg = MessageFormat.format(TWO_CONSECUTIVE_DOLLAR_SIGNS_MSG,
                    new Object[] { s, "$d;d;" }); // NOT LOCALIZABLE, special
            // edition code
            break;
        case SEMI_COLON_IS_MISSING:
            msg = MessageFormat.format(SEMI_COLON_IS_MISSING_MSG, new Object[] { s });
            break;
        } // end switch

        return msg;
    }

    public TemplateFormatException(String msg) {
        super(msg);
    } // end TemplateFormatException()
} // end TemplateFormatException
