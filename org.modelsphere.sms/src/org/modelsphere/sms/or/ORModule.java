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
package org.modelsphere.sms.or;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.srtool.graphic.SrLineEndZone;
import org.modelsphere.sms.Module;
import org.modelsphere.sms.actions.ActionFactory;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.or.actions.ORActionConstants;
import org.modelsphere.sms.or.actions.ORActionFactory;
import org.modelsphere.sms.or.db.ApplClasses;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORAttribute;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDatabase;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbOROperationLibrary;
import org.modelsphere.sms.or.db.DbORParameter;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORProcedure;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.DbORUserNode;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.graphic.MultiplicityLabel;
import org.modelsphere.sms.or.graphic.ORAssociation;
import org.modelsphere.sms.or.graphic.ORCommonItemBox;
import org.modelsphere.sms.or.graphic.ORTable;
import org.modelsphere.sms.or.graphic.ORTableBox;
import org.modelsphere.sms.or.graphic.ORTypeBox;
import org.modelsphere.sms.or.graphic.ORView;
import org.modelsphere.sms.or.ibm.db.DbIBMContainerClause;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.or.popup.ORPopupMenuPool;

public final class ORModule extends Module {
    private static ORModule singleton;
    private static final String kTableCreationTool = LocaleMgr.screen
            .getString("TableCreationTool");

    static {
        singleton = new ORModule();
    }

    private ORModule() {
    }

    public static final ORModule getSingleton() {
        return singleton;
    }

    protected ActionFactory getActionFactory() {
        return ORActionFactory.getSingleton();
    }

    public void initIntegrity() {
        new ORSemanticalIntegrity();
    }

    public void initToolkits() {
        new ORCommonItemModelToolkit();
        new ORDomainModelToolkit();
    }

    public void loadMeta() {
        ApplClasses.getFinalClasses();
    }

    public Object[] getPopupMenuMapping() {
        return new Object[] { DbORCommonItemModel.class, ORPopupMenuPool.orCommonItemModel,
                DbORCommonItem.class, ORPopupMenuPool.orCommonItem, DbORDomainModel.class,
                ORPopupMenuPool.orDomainModel, DbORAbsTable.class, ORPopupMenuPool.orAbsTable,
                DbORDomain.class, ORPopupMenuPool.orDomain, DbORView.class, ORPopupMenuPool.orView,
                DbORTable.class, ORPopupMenuPool.orTable, DbORAttribute.class,
                ORPopupMenuPool.orAttribute, DbORColumn.class, ORPopupMenuPool.orColumn,
                DbORPrimaryUnique.class, ORPopupMenuPool.orPrimaryUnique, DbORForeign.class,
                ORPopupMenuPool.orForeign, DbORAssociation.class, ORPopupMenuPool.orDbAssociation,
                DbORAssociationEnd.class, ORPopupMenuPool.orDbAssociationEnd, DbORDataModel.class,
                ORPopupMenuPool.orDataModel, DbOROperationLibrary.class,
                ORPopupMenuPool.orOperationLibrary, DbORProcedure.class,
                ORPopupMenuPool.orProcedure, DbORParameter.class, ORPopupMenuPool.orParameter,
                DbORTypeClassifier.class, ORPopupMenuPool.orTypeClassifier,
                DbORUserNode.class,
                ORPopupMenuPool.orUserNode,
                DbORDatabase.class,
                ORPopupMenuPool.orDatabase,
                DbIBMContainerClause.class,
                ORPopupMenuPool.orIBMContainerClause,

                // Graphical object
                ORAssociation.class, ORPopupMenuPool.orAssociation, ORTableBox.class,
                ORPopupMenuPool.orAbsTableGo, ORView.class, ORPopupMenuPool.orViewGo,
                ORTable.class, ORPopupMenuPool.orTableGo, ORTypeBox.class,
                ORPopupMenuPool.orTypeGo, ORCommonItemBox.class, ORPopupMenuPool.orCommonItemGo,
                MultiplicityLabel.class, ORPopupMenuPool.orMultiplicityLabel, SrLineEndZone.class,
                ORPopupMenuPool.orMultiplicityLabel, };
    }

    public AbstractApplicationAction[] getModifierActions() {
        SMSActionsStore actionsStore = SMSActionsStore.getSingleton();
        return new AbstractApplicationAction[] { actionsStore
                .getAction(ORActionConstants.OR_NULL_MODIFIER), };
    }

}
