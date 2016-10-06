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

package org.modelsphere.sms.oo;

import org.modelsphere.sms.Module;
import org.modelsphere.sms.actions.ActionFactory;
import org.modelsphere.sms.oo.actions.OOActionFactory;
import org.modelsphere.sms.oo.db.ApplClasses;
import org.modelsphere.sms.oo.db.DbOOClassModel;
import org.modelsphere.sms.oo.db.DbOOPackage;
import org.modelsphere.sms.oo.graphic.OOAssociation;
import org.modelsphere.sms.oo.graphic.OOInheritance;
import org.modelsphere.sms.oo.graphic.OOPackage;
import org.modelsphere.sms.oo.popup.OOPopupMenuPool;

public final class OOModule extends Module {
    private static OOModule singleton;

    static {
        singleton = new OOModule();
    }

    private OOModule() {
    }

    public static final OOModule getSingleton() {
        return singleton;
    }

    // ///////////////////////////////////////////////////////////////////
    // Initialisation methods

    public void initIntegrity() {
        new OOSemanticalIntegrity();
    }

    public void loadMeta() {
        ApplClasses.getFinalClasses();
    }

    //
    // ///////////////////////////////////////////////////////////////////

    // ///////////////////////////////////////////////////////////////////
    // Method used during initialisation

    protected ActionFactory getActionFactory() {
        return OOActionFactory.getSingleton();
    }

    public Object[] getPopupMenuMapping() {
        return new Object[] { DbOOClassModel.class, OOPopupMenuPool.ooClassModel,
                DbOOPackage.class, OOPopupMenuPool.ooPackage, OOPackage.class,
                OOPopupMenuPool.ooPackageGo, OOAssociation.class, OOPopupMenuPool.ooAssociation,
                OOInheritance.class, OOPopupMenuPool.ooInheritance, };
    }

}
