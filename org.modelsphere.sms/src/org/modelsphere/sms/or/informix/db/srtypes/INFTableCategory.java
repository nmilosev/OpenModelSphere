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
package org.modelsphere.sms.or.informix.db.srtypes;

import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.informix.international.LocaleMgr;

public class INFTableCategory extends IntDomain {

    static final long serialVersionUID = 0;

    public static final int RAW = 1;
    public static final int STATIC = 2;
    public static final int OPERATIONAL = 3;
    public static final int TEMP = 4;
    public static final int SCRATCH = 5;
    public static final int STANDARD = 6;

    public static final INFTableCategory[] objectPossibleValues = new INFTableCategory[] {
            new INFTableCategory(RAW), new INFTableCategory(STATIC),
            new INFTableCategory(OPERATIONAL), new INFTableCategory(STANDARD),
            new INFTableCategory(TEMP), new INFTableCategory(SCRATCH) };

    public static final String[] stringPossibleValues = new String[] {
            LocaleMgr.misc.getString("raw"), LocaleMgr.misc.getString("static"),
            LocaleMgr.misc.getString("operational"), LocaleMgr.misc.getString("standard"),
            LocaleMgr.misc.getString("temp"), LocaleMgr.misc.getString("scratch") };

    public static INFTableCategory getInstance(int value) {
        return objectPossibleValues[objectPossibleValues[0].indexOf(value)];
    }

    protected INFTableCategory(int value) {
        super(value);
    }

    //Parameterless constructor
    public INFTableCategory() {
    }

    public final DbtAbstract duplicate() {
        return new INFTableCategory(value);
    }

    public final Domain[] getObjectPossibleValues() {
        return objectPossibleValues;
    }

    public final String[] getStringPossibleValues() {
        return stringPossibleValues;
    }

    public String getStringForNullValue() {
        return org.modelsphere.jack.baseDb.screen.DefaultRenderer.kUnspecified;
    }
}
