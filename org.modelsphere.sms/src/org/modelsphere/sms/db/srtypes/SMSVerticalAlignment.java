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

import org.modelsphere.jack.baseDb.db.srtypes.DbtAbstract;
import org.modelsphere.jack.baseDb.db.srtypes.Domain;
import org.modelsphere.jack.baseDb.db.srtypes.IntDomain;

public class SMSVerticalAlignment extends IntDomain {

    static final long serialVersionUID = -1;

    public static final int TOP = 0;
    public static final int CENTER = 1;
    public static final int BOTTOM = 2;

    private static final String TOP_TXT = org.modelsphere.jack.international.LocaleMgr.action
            .getString("AlignTop");
    private static final String CENTER_TXT = org.modelsphere.jack.international.LocaleMgr.misc
            .getString("Center");
    private static final String BOTTOM_TXT = org.modelsphere.jack.international.LocaleMgr.action
            .getString("AlignBottom");

    public static final SMSVerticalAlignment[] objectPossibleValues = new SMSVerticalAlignment[] {
            new SMSVerticalAlignment(TOP), new SMSVerticalAlignment(CENTER),
            new SMSVerticalAlignment(BOTTOM) };

    public static final String[] stringPossibleValues = new String[] { TOP_TXT, CENTER_TXT,
            BOTTOM_TXT };

    public static SMSVerticalAlignment getInstance(int value) {
        return objectPossibleValues[objectPossibleValues[0].indexOf(value)];
    }

    // Parameterless constructor
    public SMSVerticalAlignment() {
    }

    protected SMSVerticalAlignment(int value) {
        super(value);
    }

    public final DbtAbstract duplicate() {
        return new SMSVerticalAlignment(value);
    }

    public final Domain[] getObjectPossibleValues() {
        return objectPossibleValues;
    }

    public final String[] getStringPossibleValues() {
        return stringPossibleValues;
    }
} // end SMSVerticalAlignment

