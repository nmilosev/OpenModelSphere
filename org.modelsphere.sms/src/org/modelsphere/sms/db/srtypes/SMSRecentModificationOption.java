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

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.or.db.DbORDiagram;

public class SMSRecentModificationOption extends IntDomain {

    static final long serialVersionUID = -6189338175335611972L;

    public static final int DO_NOT_SHOW_RECENT_MODIFS = 0;
    public static final int SHOW_SINCE_LAST_SAVE = 1;
    public static final int SHOW_DURING_CURRENT_SESSION = 2;
    // public static final int    SHOW_SINCE_SPECIFIC_DATE    = 3;

    public static final SMSRecentModificationOption[] objectPossibleValues = new SMSRecentModificationOption[] {
            new SMSRecentModificationOption(DO_NOT_SHOW_RECENT_MODIFS),
            new SMSRecentModificationOption(SHOW_SINCE_LAST_SAVE),
            new SMSRecentModificationOption(SHOW_DURING_CURRENT_SESSION),
    //	  new SMSRecentModificationOption(SHOW_SINCE_SPECIFIC_DATE)
    };

    public static String[] stringPossibleValues = new String[] {
            LocaleMgr.misc.getString("RecentDoNotShow"),
            LocaleMgr.misc.getString("RecentSinceLastSave"),
            LocaleMgr.misc.getString("RecentDuringCurrentSession"),
    //LocaleMgr.misc.getString("RecentSinceSpecificDate"),
    };

    public static SMSRecentModificationOption getInstance(int value) {
        return objectPossibleValues[objectPossibleValues[0].indexOf(value)];
    }

    //Parameterless constructor
    public SMSRecentModificationOption() {
        stringPossibleValues = new String[] { doNotShow, sinceLastSave /*
                                                                        * , duringCurrentSession,
                                                                        * sinceSpecificDate
                                                                        */};
    }

    protected SMSRecentModificationOption(int value) {
        super(value);
    }

    public final DbtAbstract duplicate() {
        return new SMSRecentModificationOption(value);
    }

    public final Domain[] getObjectPossibleValues() {
        return objectPossibleValues;
    }

    public static String doNotShow = LocaleMgr.misc.getString("RecentDoNotShow");
    public static String sinceLastSave = LocaleMgr.misc.getString("RecentSinceLastSave");
    public static String duringCurrentSession = LocaleMgr.misc
            .getString("RecentDuringCurrentSession");

    //public static String sinceSpecificDate = LocaleMgr.misc.getString("RecentSinceSpecificDate");

    public final String[] getStringPossibleValues() {
        return new String[] { doNotShow, sinceLastSave, duringCurrentSession, /* sinceSpecificDate */};
    }

    public final String[] getUIStringPossibleValues() {

        ApplicationDiagram ad = ApplicationContext.getFocusManager().getActiveDiagram();

        return new String[] { doNotShow, sinceLastSave, duringCurrentSession, /* sinceSpecificDate */};
    }

}
