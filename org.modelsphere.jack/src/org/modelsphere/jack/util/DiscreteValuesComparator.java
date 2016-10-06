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

package org.modelsphere.jack.util;

import java.util.Comparator;

public final class DiscreteValuesComparator implements Comparator {

    public static final int EQUAL = 0;
    public static final int GREATER = 1;
    public static final int LESS_THAN = -1;
    public static final int NOT_COMPARABLE = 5;

    private Object[] discreteValues;

    // array values must contain all discrete values, sorted in ascending order
    // values[i-1] < values[i] < values[i+1]
    public DiscreteValuesComparator(Object[] values) {
        discreteValues = new Object[values.length];
        System.arraycopy(values, 0, discreteValues, 0, values.length);
    }

    // compare obj1 with obj2: return GREATER if obj1 > obj2
    public final int compare(Object obj1, Object obj2) {
        int obj1Index = -1;
        int obj2Index = -1;
        for (int i = 0; i < discreteValues.length; i++) {
            if (obj1Index < 0 && discreteValues[i].equals(obj1)) {
                obj1Index = i;
            }
            if (obj2Index < 0 && discreteValues[i].equals(obj2)) {
                obj2Index = i;
            }
            if (obj1Index >= 0 && obj2Index >= 0) {
                break;
            }
        }
        int compare = DiscreteValuesComparator.NOT_COMPARABLE;
        if (obj1Index < 0 || obj2Index < 0) {
            compare = DiscreteValuesComparator.NOT_COMPARABLE;
        } else if (obj1Index == obj2Index) {
            compare = DiscreteValuesComparator.EQUAL;;
        } else if (obj1Index > obj2Index) {
            compare = DiscreteValuesComparator.GREATER;
        } else if (obj1Index < obj2Index) {
            compare = DiscreteValuesComparator.LESS_THAN;
        }
        return compare;
    }
}
