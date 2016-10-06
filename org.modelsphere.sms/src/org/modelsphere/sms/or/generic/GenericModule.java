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

package org.modelsphere.sms.or.generic;

import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.sms.Module;
import org.modelsphere.sms.actions.ActionFactory;
import org.modelsphere.sms.or.ORModule;
import org.modelsphere.sms.or.generic.actions.GenericActionFactory;
import org.modelsphere.sms.or.generic.db.ApplClasses;
import org.modelsphere.sms.or.generic.db.DbGECheck;
import org.modelsphere.sms.or.generic.db.DbGEColumn;
import org.modelsphere.sms.or.generic.db.DbGEDataModel;
import org.modelsphere.sms.or.generic.db.DbGEOperationLibrary;
import org.modelsphere.sms.or.generic.db.DbGETrigger;
import org.modelsphere.sms.or.generic.popup.GEPopupMenuPool;

public final class GenericModule extends Module {
    private static GenericModule singleton;

    static {
        singleton = new GenericModule();
    }

    private GenericModule() {
    }

    public static final GenericModule getSingleton() {
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
        new GEDataModelToolkit();
        new GEOperationLibraryToolkit();
    }

    public void initAndInstallOtherToolBars(DefaultMainFrame frame) {
    }

    //
    // ///////////////////////////////////////////////////////////////////

    // ///////////////////////////////////////////////////////////////////
    // Method used during initialisation

    protected ActionFactory getActionFactory() {
        return GenericActionFactory.getSingleton();
    }

    public Object[] getPopupMenuMapping() {
        return new Object[] { DbGEDataModel.class, GEPopupMenuPool.geDataModel,
                DbGEOperationLibrary.class, GEPopupMenuPool.geOperationLibrary, DbGECheck.class,
                GEPopupMenuPool.geCheck, DbGEColumn.class, GEPopupMenuPool.geColumn,
                DbGETrigger.class, GEPopupMenuPool.geTrigger, };
    }
    //
    // ///////////////////////////////////////////////////////////////////

}
