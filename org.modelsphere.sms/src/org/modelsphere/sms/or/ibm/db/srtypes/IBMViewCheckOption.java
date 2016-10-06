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

public class IBMViewCheckOption extends IntDomain {

    static final long serialVersionUID = 0;
    public static final int WITH_CHECK_OPTION = 1;
    public static final int WITH_CASCADED_CHECK_OPTION = 2;
    public static final int WITH_LOCAL_CHECK_OPTION = 3;

    protected IBMViewCheckOption(int value) {
        super(value);
    }

    public final String[] getStringPossibleValues() {
        return stringPossibleValues;
    }

    public final DbtAbstract duplicate() {
        return new IBMViewCheckOption(value);
    }

    public static final String[] stringPossibleValues = new String[] { "",
            LocaleMgr.misc.getString("WithCheckOption"),
            LocaleMgr.misc.getString("WithCascadedCheckOption"),
            LocaleMgr.misc.getString("WithLocalCheckOption"), };

    public static final IBMViewCheckOption[] objectPossibleValues = new IBMViewCheckOption[] {
            new IBMViewCheckOption(0), new IBMViewCheckOption(WITH_CHECK_OPTION),
            new IBMViewCheckOption(WITH_CASCADED_CHECK_OPTION),
            new IBMViewCheckOption(WITH_LOCAL_CHECK_OPTION), };

    public final Domain[] getObjectPossibleValues() {
        return objectPossibleValues;
    }

}
