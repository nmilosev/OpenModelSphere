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

// Notes:
// -->  Null Value:
//      domain possible values include all possible values but not null value.
//      If null value is supported, override getStringForNullValue()
// -->  Order of possible values:
//      the first possible value is considered as default value

public abstract class Domain extends DbtAbstract {

    static final long serialVersionUID = 5831935602003179565L;

    /*
     * Returns a dummy object that allows to access the methods of the class. jClass is a concrete
     * sub-class of Domain.
     */
    public static Domain getAnyInstance(Class jClass) {
        try {
            return ((Domain[]) jClass.getField("objectPossibleValues").get(null))[0]; // NOT LOCALIZABLE field name
        } catch (Exception e) {
            throw new RuntimeException("Method Domain.getAnyInstance"); // NOT LOCALIZABLE RuntimeException
        }
    }

    public final String toString() {
        return getStringPossibleValues()[indexOf()];
    }

    public final int indexOf(String string) {
        String[] strings = getStringPossibleValues();
        for (int i = 0; i < strings.length; i++) {
            if (string.equals(strings[i]))
                return i;
        }
        return -1;
    }

    public abstract Domain[] getObjectPossibleValues();

    public abstract String[] getStringPossibleValues();

    public abstract int indexOf();

    // If not null, the returned string is added to the edition combo and
    // represents the null value.
    public String getStringForNullValue() {
        return null;
    }

}
