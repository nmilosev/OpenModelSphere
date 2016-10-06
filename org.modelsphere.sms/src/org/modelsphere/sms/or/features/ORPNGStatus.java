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
// Disabled
// Partial
// Complete
package org.modelsphere.sms.or.features;

import org.modelsphere.jack.baseDb.db.srtypes.DbtAbstract;
import org.modelsphere.jack.baseDb.db.srtypes.Domain;
import org.modelsphere.jack.baseDb.db.srtypes.IntDomain;
import org.modelsphere.sms.or.international.LocaleMgr;

public class ORPNGStatus extends IntDomain {

    static final long serialVersionUID = -3953338490669378768L;

    public static final int DISABLED = 1;
    public static final int PARTIAL = 2;
    public static final int COMPLETE = 3;

    public static final ORPNGStatus[] objectPossibleValues = new ORPNGStatus[] {
            new ORPNGStatus(DISABLED), new ORPNGStatus(PARTIAL), new ORPNGStatus(COMPLETE) };

    public static final String[] stringPossibleValues = new String[] {
            LocaleMgr.misc.getString("disabled"), LocaleMgr.misc.getString("partial"),
            LocaleMgr.misc.getString("complete") };

    public static ORPNGStatus getInstance(int value) {
        int index = objectPossibleValues[0].indexOf(value);
        return index == -1 ? null : objectPossibleValues[index];
    }

    protected ORPNGStatus(int value) {
        super(value);
    }

    public final DbtAbstract duplicate() {
        return new ORPNGStatus(value);
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
