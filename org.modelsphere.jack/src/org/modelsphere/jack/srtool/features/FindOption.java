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

package org.modelsphere.jack.srtool.features;

import java.util.ArrayList;

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.srtool.graphic.DbGraphic;

public class FindOption {
    private static final int NAME = 0;
    private static final int CODED_NAME = 1;
    private static final int ALIAS = 2;
    private static final int DESCRIPTION = 3;
    private static final int ALL_OTHER_TEXT = 4;

    private String textToFind;
    private String lowerCaseTextToFind = null;// initialize if caseSensitive ==
    // false and useGrep == false
    private boolean caseSensitive;
    private boolean wholeWord;
    private boolean useGrep;
    private boolean recursive;
    private RE re = null;
    private ArrayList scopeOptions = new ArrayList();

    public FindOption(String textToFind, boolean caseSensitive, boolean wholeWord, boolean useGrep,
            boolean recursive, ArrayList scopeOptions) throws RESyntaxException {
        this.textToFind = textToFind;
        this.caseSensitive = caseSensitive;
        this.wholeWord = wholeWord;
        this.useGrep = useGrep;
        this.recursive = recursive;
        this.scopeOptions = scopeOptions;
        if (useGrep)
            re = new RE(textToFind, caseSensitive ? 0 : RE.MATCH_CASEINDEPENDENT);
        else if (!caseSensitive)
            lowerCaseTextToFind = textToFind.toLowerCase();
    }

    public boolean match(String textToMatch) {
        if (textToMatch == null)
            return false;
        if (re != null)
            return re.match(textToMatch);
        int foundIndex;
        if (caseSensitive)
            foundIndex = textToMatch.indexOf(textToFind);
        else
            foundIndex = textToMatch.toLowerCase().indexOf(lowerCaseTextToFind);
        if (wholeWord && foundIndex != -1) {
            int lengthTTT = textToFind.length();
            int lengthTTM = textToMatch.length();
            if (foundIndex > 0 && !Character.isWhitespace(textToMatch.charAt(foundIndex - 1))
                    || foundIndex + lengthTTT < lengthTTM
                    && !Character.isWhitespace(textToMatch.charAt(foundIndex + lengthTTT)))
                foundIndex = -1;
        }
        return foundIndex != -1;
    }

    public boolean selectedInScope(MetaField field) {
        if (field == DbSemanticalObject.fName || field == DbGraphic.fDiagramName)
            return ((Boolean) scopeOptions.get(NAME)).booleanValue();
        else if (field == DbSemanticalObject.fPhysicalName)
            return ((Boolean) scopeOptions.get(CODED_NAME)).booleanValue();
        else if (field == DbSemanticalObject.fAlias)
            return ((Boolean) scopeOptions.get(ALIAS)).booleanValue();
        else if (field == DbSemanticalObject.fDescription)
            return ((Boolean) scopeOptions.get(DESCRIPTION)).booleanValue();
        else
            return ((Boolean) scopeOptions.get(ALL_OTHER_TEXT)).booleanValue();
    }

    public boolean browseUDF() {
        return ((Boolean) scopeOptions.get(ALL_OTHER_TEXT)).booleanValue();
    }

    public String getTextToFind() {
        return textToFind;
    }

    public boolean getRecursive() {
        return recursive;
    }
}
