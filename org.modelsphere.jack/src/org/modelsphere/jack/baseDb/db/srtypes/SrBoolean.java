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

public final class SrBoolean extends Domain {

    public static final String[] stringPossibleValues = new String[] { "NULL", "NOT NULL" };

    public int indexOf(int value) {
        return value;
    }

    public int indexOf() {
        return indexOf((value == true) ? 0 : 1);
    }

    public Object toApplType() {
        return (value ? Boolean.TRUE : Boolean.FALSE);
    }

    public static final Domain[] objectPossibleValues = new SrBoolean[] {
            new SrBoolean(Boolean.TRUE), new SrBoolean(Boolean.FALSE) };

    public Domain[] getObjectPossibleValues() {
        return objectPossibleValues;
    }

    public String getStringForNullValue() {
        return new String("Undefined");
    }

    public String[] getStringPossibleValues() {
        return stringPossibleValues;
    }

    public DbtAbstract duplicate() {
        return new SrBoolean(new Boolean(value));
    }

    static final long serialVersionUID = 9213187441216640025L;

    boolean value;

    // Parameterless constructor
    public SrBoolean() {
    }

    public SrBoolean(Boolean value) {
        this.value = value.booleanValue();
    }

    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SrBoolean))
            return false;
        return (value == ((SrBoolean) obj).value);
    }
}
