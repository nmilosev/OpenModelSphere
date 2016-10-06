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

package org.modelsphere.jack.baseDb.db.srtypes;

import org.modelsphere.jack.international.LocaleMgr;

public class UDFValueType extends IntDomain {

    static final long serialVersionUID = -6551392154688856978L;

    public static final int STRING = 1;
    public static final int LONG = 2;
    public static final int DOUBLE = 3;
    public static final int BOOLEAN = 4;
    public static final int TEXT = 5;

    public static final UDFValueType[] objectPossibleValues = new UDFValueType[] {
            new UDFValueType(STRING), new UDFValueType(TEXT), new UDFValueType(BOOLEAN),
            new UDFValueType(LONG), new UDFValueType(DOUBLE) };

    public static final String[] stringPossibleValues = new String[] {
            LocaleMgr.misc.getString("String"), LocaleMgr.misc.getString("MultiLineText"),
            LocaleMgr.misc.getString("Boolean"), LocaleMgr.misc.getString("NumericLong"),
            LocaleMgr.misc.getString("NumericDouble") };

    public static UDFValueType getInstance(int value) {
        return objectPossibleValues[objectPossibleValues[0].indexOf(value)];
    }

    // Parameterless constructor
    public UDFValueType() {
    }

    protected UDFValueType(int value) {
        super(value);
    }

    public final DbtAbstract duplicate() {
        return new UDFValueType(value);
    }

    public final Domain[] getObjectPossibleValues() {
        return objectPossibleValues;
    }

    public final String[] getStringPossibleValues() {
        return stringPossibleValues;
    }

}
