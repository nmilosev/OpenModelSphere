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

package org.modelsphere.sms;

import java.lang.reflect.Method;

import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.international.LocaleMgr;

public abstract class SMSBackup {

    public static final long CHECKWORD = -7717910379514949485L;
    public static final String kFileExt = "smsbackup"; // NOT LOCALIZABLE

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(LocaleMgr.message.getString("backupUsage"));
            System.exit(1);
        }
        String fileName = StringUtil.appendFileExt(args[0], kFileExt);
        Application.initMeta();
        try {
            Class dbClass = Class.forName("org.modelsphere.jack.baseDb.db.DbGemStone"); // NOT
            // LOCALIZABLE
            Method method = dbClass.getDeclaredMethod("backupRepository", // NOT
                    // LOCALIZABLE
                    new Class[] { String.class, int.class, long.class, String.class });
            Boolean done = (Boolean) method.invoke(null, new Object[] {
                    Application.REPOSITORY_ROOT_NAME, new Integer(SMSVersionConverter.VERSION),
                    new Long(CHECKWORD), fileName });
            if (done.booleanValue())
                System.out.println(LocaleMgr.message.getString("backupCompleted"));
        } catch (Exception e) {
        }
        System.exit(0);
    }
}
