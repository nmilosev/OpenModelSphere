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
package org.modelsphere.sms.or.ibm.db.srtypes;

import org.modelsphere.jack.baseDb.db.srtypes.DbtAbstract;
import org.modelsphere.jack.baseDb.db.srtypes.Domain;
import org.modelsphere.jack.baseDb.db.srtypes.IntDomain;
import org.modelsphere.sms.or.ibm.international.LocaleMgr;

public class IBMProcedureFence extends IntDomain {

    static final long serialVersionUID = 0;
    public static final int FENCED = 1;
    public static final int FENCED_THREADSAFE = 2;
    public static final int FENCED_NOT_THREADSAFE = 3;
    public static final int NOT_FENCED_THREADSAFE = 4;
    public static final int NOT_FENCED = 5;

    protected IBMProcedureFence(int value) {
        super(value);
    }

    public final String[] getStringPossibleValues() {
        return stringPossibleValues;
    }

    public final DbtAbstract duplicate() {
        return new IBMProcedureFence(value);
    }

    public static final String[] stringPossibleValues = new String[] { "",
            LocaleMgr.misc.getString("Fenced"), LocaleMgr.misc.getString("FencedThreadSafe"),
            LocaleMgr.misc.getString("FencedNotThreadSafe"),
            LocaleMgr.misc.getString("NotFencedThreadSafe"), LocaleMgr.misc.getString("NotFenced"), };

    public static final IBMProcedureFence[] objectPossibleValues = new IBMProcedureFence[] {
            new IBMProcedureFence(0), new IBMProcedureFence(FENCED),
            new IBMProcedureFence(FENCED_THREADSAFE), new IBMProcedureFence(FENCED_NOT_THREADSAFE),
            new IBMProcedureFence(NOT_FENCED_THREADSAFE), new IBMProcedureFence(NOT_FENCED),

    };

    public final Domain[] getObjectPossibleValues() {
        return objectPossibleValues;
    }

}
