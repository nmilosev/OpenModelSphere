/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.be.text;

import java.text.MessageFormat;
import java.util.Locale;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.sms.be.international.LocaleMgr;

//access to names of BE concepts should be localized here
public class BETextUtil {

    private static BETextUtil g_singleton = null;

    private BETextUtil() {
    }

    public static BETextUtil getSingleton() {
        if (g_singleton == null) {
            g_singleton = new BETextUtil();
        }

        return g_singleton;
    } // end getSingleton()

    //
    // PUBLIC METHODS
    //
    public String getCreationText(MetaClass metaClass) {
        String pattern = LocaleMgr.action.getString("0Creation");
        String equivalent = getNotationEquivalent(metaClass);
        String creationText = MessageFormat.format(pattern, new Object[] { equivalent });
        return creationText;
    } // end getCreationText()

    public String getCreationText(MetaClass metaClass, Terminology terminology) {
        String pattern = LocaleMgr.action.getString("0Creation");
        String term = terminology.getTerm(metaClass);
        String creationText = MessageFormat.format(pattern, new Object[] { term });
        return creationText;
    } // end getCreationText()

    public String getSelectAllText(MetaClass metaClass) {
        String pattern = LocaleMgr.action.getString("SelectAll0");
        String equivalent = getNotationEquivalent(metaClass, true);
        String selectAllText = MessageFormat.format(pattern, new Object[] { equivalent });
        return selectAllText;
    } // end getSelectAllText()

    public String getSelectAllText(DbObject compositeObject, MetaClass metaClass)
            throws DbException {
        TerminologyUtil termUtil = TerminologyUtil.getInstance();
        Terminology terminology = termUtil.findModelTerminology(compositeObject);
        String term = terminology.getTerm(metaClass, true);
        String pattern = LocaleMgr.action.getString("SelectAll0");
        String selectAllText = MessageFormat.format(pattern, new Object[] { term });
        return selectAllText;
    } // end getSelectAllText()

    //
    // PRIVATE METHODS
    //
    private String getNotationEquivalent(MetaClass metaClass) {
        return getNotationEquivalent(metaClass, false);
    }

    private String getNotationEquivalent(MetaClass metaClass, boolean plural) {
        String equivalent = metaClass.getGUIName(plural);

        // In French, use lower case words
        if (Locale.getDefault().equals(Locale.FRENCH)) {
            equivalent = lowerFirstChar(equivalent);
        }

        return equivalent;
    } // end getNotationEquivalent()

    private String lowerFirstChar(String equivalent) {
        char firstChar = equivalent.charAt(0);
        return Character.toLowerCase(firstChar) + equivalent.substring(1);
    } // end lowerFirstChar()

} // end BETextUtil
