/*************************************************************************

Copyright (C) 2010 Grandite

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

package org.modelsphere.jack.srtool.actions;

import org.modelsphere.jack.debug.Log;
import org.modelsphere.jack.srtool.ApplicationContext;

// FOR DEBUG PURPOSE

public class LogAction extends ShowAbstractAction {

    public LogAction() {
        super("Start Logging", "Stop Logging", false); // NOT LOCALIZABLE, Debug only
    }

    protected final void showComponent() {
        //e.g. /Users/Admin/.Open ModelSphere/sms.log
        String sep = System.getProperty("file.separator"); // NOT LOCALIZABLE, property 
        String home = System.getProperty("user.home"); // NOT LOCALIZABLE, property 
        String logFile = home + sep + "." + ApplicationContext.getApplicationName() + sep + "sms.log"; 
        Log.start(logFile, true);
    }

    protected final void hideComponent() {
        Log.stop();
    }
}
