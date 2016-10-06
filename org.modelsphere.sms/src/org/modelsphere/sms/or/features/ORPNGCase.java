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
// Possible Values :
// No Change
// UPPER
// lower
package org.modelsphere.sms.or.features;

import org.modelsphere.jack.baseDb.db.srtypes.DbtAbstract;
import org.modelsphere.jack.baseDb.db.srtypes.Domain;
import org.modelsphere.jack.baseDb.db.srtypes.IntDomain;
import org.modelsphere.sms.or.international.LocaleMgr;

public class ORPNGCase extends IntDomain {

    static final long serialVersionUID = 1238515374804961209L;

    public static final int NO_CHANGE = 1;
    public static final int UPPER = 2;
    public static final int LOWER = 3;

    public static final ORPNGCase[] objectPossibleValues = new ORPNGCase[] {
            new ORPNGCase(NO_CHANGE), new ORPNGCase(UPPER), new ORPNGCase(LOWER) };

    public static final String[] stringPossibleValues = new String[] {
            LocaleMgr.misc.getString("noChange"), LocaleMgr.misc.getString("upper"),
            LocaleMgr.misc.getString("lower") };

    public static ORPNGCase getInstance(int value) {
        int index = objectPossibleValues[0].indexOf(value);
        return index == -1 ? null : objectPossibleValues[index];
    }

    protected ORPNGCase(int value) {
        super(value);
    }

    public final DbtAbstract duplicate() {
        return new ORPNGCase(value);
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
