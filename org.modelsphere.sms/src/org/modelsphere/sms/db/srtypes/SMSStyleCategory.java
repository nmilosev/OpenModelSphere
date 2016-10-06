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

package org.modelsphere.sms.db.srtypes;

import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.international.LocaleMgr;

public class SMSStyleCategory extends IntDomain {

    static final long serialVersionUID = -4616616633998708602L;

    public static final int CLASS_STYLE = 0;
    public static final int DATA_STYLE = 1;
    public static final int TYPE_STYLE = 2;
    public static final int BUILTIN_STYLE = 3;
    public static final int OPERATION_STYLE = 4;
    public static final int DOMAIN_STYLE = 5;
    public static final int COMMON_ITEM_STYLE = 6;

    public static final SMSStyleCategory[] objectPossibleValues = new SMSStyleCategory[] {
            new SMSStyleCategory(CLASS_STYLE), new SMSStyleCategory(DATA_STYLE),
            new SMSStyleCategory(TYPE_STYLE), new SMSStyleCategory(BUILTIN_STYLE),
            new SMSStyleCategory(OPERATION_STYLE), new SMSStyleCategory(DOMAIN_STYLE),
            new SMSStyleCategory(COMMON_ITEM_STYLE) };

    public static final String[] stringPossibleValues = new String[] {
            LocaleMgr.misc.getString("ClassStyleCategory"),
            LocaleMgr.misc.getString("DataStyleCategory"),
            LocaleMgr.misc.getString("TypeStyleCategory"),
            LocaleMgr.misc.getString("BuiltInStyleCategory"),
            LocaleMgr.misc.getString("OperationStyleCategory"),
            LocaleMgr.misc.getString("DomainStyleCategory"),
            LocaleMgr.misc.getString("CommonItemStyleCategory") };

    public static SMSStyleCategory getInstance(int value) {
        return objectPossibleValues[objectPossibleValues[0].indexOf(value)];
    }

    //Parameterless constructor
    public SMSStyleCategory() {
    }

    protected SMSStyleCategory(int value) {
        super(value);
    }

    public final DbtAbstract duplicate() {
        return new SMSStyleCategory(value);
    }

    public final Domain[] getObjectPossibleValues() {
        return objectPossibleValues;
    }

    public final String[] getStringPossibleValues() {
        return stringPossibleValues;
    }
}
