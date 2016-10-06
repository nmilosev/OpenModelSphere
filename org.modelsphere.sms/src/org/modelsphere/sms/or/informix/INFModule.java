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
package org.modelsphere.sms.or.informix;

import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.sms.Module;
import org.modelsphere.sms.actions.ActionFactory;
import org.modelsphere.sms.or.ORModule;
import org.modelsphere.sms.or.informix.actions.INFActionFactory;
import org.modelsphere.sms.or.informix.db.ApplClasses;
import org.modelsphere.sms.or.informix.db.DbINFDataModel;
import org.modelsphere.sms.or.informix.db.DbINFDatabase;
import org.modelsphere.sms.or.informix.db.DbINFOperationLibrary;
import org.modelsphere.sms.or.informix.popup.INFPopupMenuPool;

public final class INFModule extends Module {
    private static INFModule singleton;

    static {
        singleton = new INFModule();
    }

    private INFModule() {
    }

    public static final INFModule getSingleton() {
        return singleton;
    }

    public Module getSuperModule() {
        return ORModule.getSingleton();
    }

    // ///////////////////////////////////////////////////////////////////
    // Initialisation methods

    public void loadMeta() {
        ApplClasses.getFinalClasses();
    }

    public void initMeta() {
    }

    public void initIntegrity() {
    }

    public void initToolkits() {
        new INFDataModelToolkit();
        new INFOperationLibraryToolkit();
    }

    public void initAndInstallOtherToolBars(DefaultMainFrame frame) {
    }

    //
    // ///////////////////////////////////////////////////////////////////

    // ///////////////////////////////////////////////////////////////////
    // Method used during initialisation

    protected ActionFactory getActionFactory() {
        return INFActionFactory.getSingleton();
    }

    public Object[] getPopupMenuMapping() {
        return new Object[] { DbINFDatabase.class, INFPopupMenuPool.infDatabase,
                DbINFDataModel.class, INFPopupMenuPool.infDataModel, DbINFOperationLibrary.class,
                INFPopupMenuPool.infOperationLibrary, };
    }

}
