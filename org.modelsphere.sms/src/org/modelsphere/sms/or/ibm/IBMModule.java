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
package org.modelsphere.sms.or.ibm;

import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.sms.Module;
import org.modelsphere.sms.actions.ActionFactory;
import org.modelsphere.sms.or.ORModule;
import org.modelsphere.sms.or.ibm.actions.IBMActionFactory;
import org.modelsphere.sms.or.ibm.db.ApplClasses;
import org.modelsphere.sms.or.ibm.db.DbIBMDataModel;
import org.modelsphere.sms.or.ibm.db.DbIBMOperationLibrary;
import org.modelsphere.sms.or.ibm.popup.IBMPopupMenuPool;

public final class IBMModule extends Module {
    private static IBMModule singleton;

    static {
        singleton = new IBMModule();
    }

    private IBMModule() {
    }

    public static final IBMModule getSingleton() {
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
        new IBMDataModelToolkit();
        new IBMOperationLibraryToolkit();
    }

    public void initAndInstallOtherToolBars(DefaultMainFrame frame) {
    }

    //
    // ///////////////////////////////////////////////////////////////////

    // ///////////////////////////////////////////////////////////////////
    // Method used during initialisation

    protected ActionFactory getActionFactory() {
        return IBMActionFactory.getSingleton();
    }

    public Object[] getPopupMenuMapping() {
        return new Object[] { DbIBMDataModel.class, IBMPopupMenuPool.ibmDataModel,
                DbIBMOperationLibrary.class, IBMPopupMenuPool.ibmOperationLibrary, };
    }

}
