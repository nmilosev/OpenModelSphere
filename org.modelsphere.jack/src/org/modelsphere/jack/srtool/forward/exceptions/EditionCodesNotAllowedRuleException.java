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

/**
 * Thrown by Modifier
 * 
 * Edition codes are not allowed in SMS templates; each time we have the following code:
 * 
 * templ_1 TEMPL "$subtempl;", //NOT LOCALIZABLE PREF = "{$m+2$", //NOT LOCALIZABLE SUF = "$m-2;}";
 * //NOT LOCALIZABLE
 * 
 * we can either put the edition codes in the template itself:
 * 
 * templ_1 TEMPL "$m+2;$subtempl;$m-2;", //NOT LOCALIZABLE PREF = "{", SUF = "}";
 * 
 * or call a subtemplate in PREF or SUF modifiers:
 * 
 * templ_1 TEMPL "$subtempl;", //NOT LOCALIZABLE PREF = "$open_brace;", //NOT LOCALIZABLE SUF =
 * "$close_brace;"; //NOT LOCALIZABLE
 * 
 * open_brace TEMPL "{$m+2"; //NOT LOCALIZABLE close_brace TEMPL "$m-2;}"; //NOT LOCALIZABLE
 */
public final class EditionCodesNotAllowedRuleException extends RuleException {

    private static final String MESSAGE_PATTERN = LocaleMgr.message
            .getString("EditionCodesNotAllowed");

    public EditionCodesNotAllowedRuleException() {
        super(MESSAGE_PATTERN);
    }
}
