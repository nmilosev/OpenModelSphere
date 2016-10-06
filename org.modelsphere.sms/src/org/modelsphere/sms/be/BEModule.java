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

package org.modelsphere.sms.be;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.sms.Module;
import org.modelsphere.sms.actions.ActionFactory;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.be.actions.BEActionFactory;
import org.modelsphere.sms.be.db.*;
import org.modelsphere.sms.be.graphic.*;
import org.modelsphere.sms.be.popup.BEPopupMenuPool;

public final class BEModule extends Module {
    private static BEModule singleton;

    static {
        singleton = new BEModule();
    }

    private BEModule() {
    } //end BEModule()

    public static final BEModule getSingleton() {
        return singleton;
    }

    protected ActionFactory getActionFactory() {
        return BEActionFactory.getSingleton();
    }

    public void initIntegrity() {
        new BESemanticalIntegrity();
    }

    public void initToolkits() {
        new BEModelToolkit();
    }

    public void loadMeta() {
        ApplClasses.getFinalClasses();
    }

    public Object[] getPopupMenuMapping() {
        return new Object[] { DbBEModel.class, BEPopupMenuPool.beModel, DbBEDiagram.class,
                BEPopupMenuPool.beDiagram, DbBEUseCase.class, BEPopupMenuPool.beUseCase,
                BEUseCaseBox.class, BEPopupMenuPool.beUseCaseBox, DbBEStore.class,
                BEPopupMenuPool.beStore, BEStoreBox.class, BEPopupMenuPool.beStoreBox,
                DbBEActor.class, BEPopupMenuPool.beActor, BEActorBox.class,
                BEPopupMenuPool.beActorBox, DbBEFlow.class,
                BEPopupMenuPool.beFlow,
                BEFlow.class,
                BEPopupMenuPool.beFlowPeer,

                //Graphical object
                BEContext.class, BEPopupMenuPool.beUseCaseFrame, BEFlowLabel.class,
                BEPopupMenuPool.beFlowLabel, BEQualifier.class, BEPopupMenuPool.beQualifier,
                DbBEContextGo.class, BEPopupMenuPool.beContextGo, DbBEContextCell.class,
                BEPopupMenuPool.beContextCell, };
    }

    public AbstractApplicationAction[] getModifierActions() {
        SMSActionsStore actionsStore = SMSActionsStore.getSingleton();
        return null;
    }

}
