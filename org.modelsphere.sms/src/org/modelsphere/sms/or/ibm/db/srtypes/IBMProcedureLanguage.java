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

public class IBMProcedureLanguage extends IntDomain {

    static final long serialVersionUID = 0;
    public static final int SQL = 1;
    public static final int JAVA = 2;
    public static final int C = 3;
    public static final int OLE = 4;

    protected IBMProcedureLanguage(int value) {
        super(value);
    }

    public final String[] getStringPossibleValues() {
        return stringPossibleValues;
    }

    public final DbtAbstract duplicate() {
        return new IBMProcedureLanguage(value);
    }

    public static final String[] stringPossibleValues = new String[] { "",
            LocaleMgr.misc.getString("SQL"), LocaleMgr.misc.getString("Java"),
            LocaleMgr.misc.getString("C"), LocaleMgr.misc.getString("OLE"), };

    public static final IBMProcedureLanguage[] objectPossibleValues = new IBMProcedureLanguage[] {
            new IBMProcedureLanguage(0), new IBMProcedureLanguage(JAVA),
            new IBMProcedureLanguage(SQL), new IBMProcedureLanguage(C),
            new IBMProcedureLanguage(OLE), };

    public final Domain[] getObjectPossibleValues() {
        return objectPossibleValues;
    }

}
