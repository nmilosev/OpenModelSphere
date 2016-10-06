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

public abstract class IntDomain extends Domain {

    static final long serialVersionUID = 3754635774858969116L;

    protected int value;

    public IntDomain() {
    }

    protected IntDomain(int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public boolean equals(Object domain) {
        if (domain == this)
            return true;
        if (domain == null)
            return false;
        if (!(domain instanceof IntDomain))
            return false;
        return (value == ((IntDomain) domain).value);
    }

    public final int indexOf() {
        return indexOf(value);
    }

    public final int indexOf(int value) {
        IntDomain[] domains = (IntDomain[]) getObjectPossibleValues();
        for (int i = 0; i < domains.length; i++) {
            if (value == domains[i].value)
                return i;
        }
        return -1;
    }
}
