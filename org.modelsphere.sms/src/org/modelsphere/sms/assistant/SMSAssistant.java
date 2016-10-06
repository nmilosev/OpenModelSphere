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

package org.modelsphere.sms.assistant;

import java.net.URL;

import org.modelsphere.jack.baseDb.assistant.JHtmlBallonHelp;
import org.modelsphere.sms.international.LocaleMgr;

public class SMSAssistant extends JHtmlBallonHelp {

    public static final String kRootNodeName = LocaleMgr.misc.getString("RootNodeName");
    public static final String kHelpPackageName = LocaleMgr.misc.getString("HelpPackageName");
    public static final String kPreference = LocaleMgr.misc.getString("Preference");
    public static final String kPreferences = LocaleMgr.misc.getString("Preferences");
    public static final String kReverse = LocaleMgr.misc.getString("Reverse");
    public static final String kWelcome = LocaleMgr.misc.getString("Welcome");

    public static String getHelpDirectory() {
        URL url = LocaleMgr.misc.getUrl("HelpWelcome");
        if (url == null)
            return null;
        String helpDir = url.getFile();
        return helpDir.substring(0, helpDir.lastIndexOf('/'));
    }

    public SMSAssistant() {
        super(getHelpDirectory());
    }

    public final String getRootNodeName() {
        return kRootNodeName;
    }

    public final String getHelpPackageName() {
        return kHelpPackageName;

    }

    // context-sensitive method
    public final String getURLForObjectKey(Object helpObjectKey) {
        /*
         * if (helpObjectKey instanceof DbClass) //return "objClass";
         * 
         * if (helpObjectKey instanceof DbCompilationUnit) //return "objCompilation";
         * 
         * if (helpObjectKey instanceof DbInterface) //return "objInterface";
         * 
         * if (helpObjectKey instanceof DbConstructor) //return "objConstructor";
         * 
         * if (helpObjectKey instanceof DbJavaException) //return "objException";
         * 
         * if (helpObjectKey instanceof DbPackage) //return "objPackage";
         * 
         * if (helpObjectKey instanceof DbMember) //return "objMember";
         * 
         * if (helpObjectKey instanceof DbParameter) //return "objParameter";
         * 
         * if (helpObjectKey instanceof DbOperation) //return "objOperation";
         * 
         * if (helpObjectKey instanceof DbJDProject) //return "objProject";
         */
        if (helpObjectKey.equals(kPreference))
            return kPreferences;

        if (helpObjectKey.equals(kReverse))
            return kReverse;

        if (helpObjectKey.equals(kWelcome))
            return kWelcome;

        return null;
    }
}
