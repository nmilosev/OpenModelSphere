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

package org.modelsphere.sms.or.db.srtypes;

import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.international.LocaleMgr;

public class ORTriggerEvent extends IntDomain {

    static final long serialVersionUID = 0;

    public static final int INSERT = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;
    public static final int INSERT_UPDATE = 4;
    public static final int INSERT_DELETE = 5;
    public static final int UPDATE_DELETE = 6;
    public static final int INSERT_UPDATE_DELETE = 7;

    public static final ORTriggerEvent[] objectPossibleValues = new ORTriggerEvent[] {
            new ORTriggerEvent(INSERT), new ORTriggerEvent(UPDATE), new ORTriggerEvent(DELETE),
            new ORTriggerEvent(INSERT_UPDATE), new ORTriggerEvent(INSERT_DELETE),
            new ORTriggerEvent(UPDATE_DELETE), new ORTriggerEvent(INSERT_UPDATE_DELETE) };

    public static final String[] stringPossibleValues = new String[] {
            LocaleMgr.misc.getString("insert"), LocaleMgr.misc.getString("update"),
            LocaleMgr.misc.getString("delete"), LocaleMgr.misc.getString("insertUpdate"),
            LocaleMgr.misc.getString("insertDelete"), LocaleMgr.misc.getString("updateDelete"),
            LocaleMgr.misc.getString("insertUpdateDelete") };

    public static ORTriggerEvent getInstance(int value) {
        return objectPossibleValues[objectPossibleValues[0].indexOf(value)];
    }

    protected ORTriggerEvent(int value) {
        super(value);
    }

    //Parameterless constructor
    public ORTriggerEvent() {
    }

    public final DbtAbstract duplicate() {
        return new ORTriggerEvent(value);
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
