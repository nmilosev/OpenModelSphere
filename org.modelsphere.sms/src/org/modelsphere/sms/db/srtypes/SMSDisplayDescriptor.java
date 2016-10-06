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

import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.international.LocaleMgr;

public class SMSDisplayDescriptor extends IntDomain {

    static final long serialVersionUID = -238219088823636083L;

    public static final int NOT_DISPLAYED = 0;
    public static final int NAME = 1;
    public static final int PHYSICAL_NAME = 2;
    public static final int ALIAS = 3;

    public static final SMSDisplayDescriptor[] objectPossibleValues = new SMSDisplayDescriptor[] {
            new SMSDisplayDescriptor(NOT_DISPLAYED), new SMSDisplayDescriptor(NAME),
            new SMSDisplayDescriptor(PHYSICAL_NAME), new SMSDisplayDescriptor(ALIAS) };

    public static final String[] stringPossibleValues = new String[] {
            LocaleMgr.misc.getString("NotDisplayedDisplayDescriptor"),
            LocaleMgr.misc.getString("NameDisplayDescriptor"),
            LocaleMgr.misc.getString("PhysicalNameDisplayDescriptor"),
            LocaleMgr.misc.getString("AliasDisplayDescriptor") };

    public static SMSDisplayDescriptor getInstance(int value) {
        return objectPossibleValues[objectPossibleValues[0].indexOf(value)];
    }

    //Parameterless constructor
    public SMSDisplayDescriptor() {
    }

    protected SMSDisplayDescriptor(int value) {
        super(value);
    }

    public final DbtAbstract duplicate() {
        return new SMSDisplayDescriptor(value);
    }

    public final Domain[] getObjectPossibleValues() {
        return objectPossibleValues;
    }

    public final String[] getStringPossibleValues() {
        return stringPossibleValues;
    }
}
