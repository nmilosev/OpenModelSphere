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
package org.modelsphere.sms.be.screen;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.screen.DbListView;
import org.modelsphere.jack.baseDb.screen.DbTreeLookupDialog;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModelListener;
import org.modelsphere.jack.srtool.SrScreenContext;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEActorQualifier;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEFlowQualifier;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEStoreQualifier;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseQualifier;
import org.modelsphere.sms.be.db.DbBEUseCaseResource;
import org.modelsphere.sms.db.DbSMSSemanticalObject;

public class BEListView extends DbListView {

    public BEListView(DbObject semObj, MetaClass listClass) throws DbException {
        this(semObj, DbObject.fComponents, listClass, ADD_DEL_ACTIONS | REINSERT_ACTION);
    }

    public BEListView(DbObject semObj, MetaRelationN listRelation, MetaClass listClass,
            int actionMode) throws DbException {
        super(SrScreenContext.singleton, semObj, listRelation, listClass, actionMode);
    }

    public BEListView(DbObject semObj, MetaRelationN[] listRelations, MetaClass listClass,
            int actionMode) throws DbException {
        super(SrScreenContext.singleton, semObj, listRelations, listClass, actionMode);
    }

    protected final void createLinkComponent(DbObject parent) throws DbException {
        if (listClass == DbBEUseCaseQualifier.metaClass) {
            if (semObj instanceof DbBEUseCase)
                new DbBEUseCaseQualifier((DbBEUseCase) semObj, (DbSMSSemanticalObject) parent);
            if (semObj instanceof DbBEQualifier)
                new DbBEUseCaseQualifier((DbBEUseCase) parent, (DbSMSSemanticalObject) semObj);
        } else if (listClass == DbBEActorQualifier.metaClass) {
            if (semObj instanceof DbBEActor)
                new DbBEActorQualifier((DbBEActor) semObj, (DbSMSSemanticalObject) parent);
            if (semObj instanceof DbBEQualifier)
                new DbBEActorQualifier((DbBEActor) parent, (DbSMSSemanticalObject) semObj);
        } else if (listClass == DbBEStoreQualifier.metaClass) {
            if (semObj instanceof DbBEStore)
                new DbBEStoreQualifier((DbBEStore) semObj, (DbSMSSemanticalObject) parent);
            if (semObj instanceof DbBEQualifier)
                new DbBEStoreQualifier((DbBEStore) parent, (DbSMSSemanticalObject) semObj);
        } else if (listClass == DbBEFlowQualifier.metaClass) {
            if (semObj instanceof DbBEFlow)
                new DbBEFlowQualifier((DbBEFlow) semObj, (DbSMSSemanticalObject) parent);
            if (semObj instanceof DbBEQualifier)
                new DbBEFlowQualifier((DbBEFlow) parent, (DbSMSSemanticalObject) semObj);
        } else if (listClass == DbBEUseCaseResource.metaClass) {
            if (semObj instanceof DbBEResource)
                new DbBEUseCaseResource((DbBEUseCase) parent, (DbBEResource) semObj);
            if (semObj instanceof DbBEUseCase)
                new DbBEUseCaseResource((DbBEUseCase) semObj, (DbBEResource) parent);
        }
    }

    protected final DbObject[] showLinkDialog() {
        if (listClass == DbBEUseCaseQualifier.metaClass) {
            if (semObj instanceof DbBEUseCase)
                return showQualifierLinkDialog();
            if (semObj instanceof DbBEQualifier)
                return showUseCaseLinkDialog();
        } else if (listClass == DbBEActorQualifier.metaClass) {
            if (semObj instanceof DbBEActor)
                return showQualifierLinkDialog();
        } else if (listClass == DbBEStoreQualifier.metaClass) {
            if (semObj instanceof DbBEStore)
                return showQualifierLinkDialog();
        } else if (listClass == DbBEFlowQualifier.metaClass) {
            if (semObj instanceof DbBEFlow)
                return showQualifierLinkDialog();
        } else if (listClass == DbBEUseCaseResource.metaClass) {
            if (semObj instanceof DbBEResource)
                return this.showUseCaseLinkDialog();
        }
        return super.showLinkDialog(); // display a flat list of linkable
        // objects
    }

    protected DbEnumeration getLinkableEnum() throws DbException {
        if (listClass == DbBEActorQualifier.metaClass && semObj instanceof DbBEQualifier) {
            return getDefaultRoot().componentTree(DbBEActor.metaClass);
        } else if (listClass == DbBEStoreQualifier.metaClass && semObj instanceof DbBEQualifier) {
            return getDefaultRoot().componentTree(DbBEStore.metaClass);
        } else if (listClass == DbBEFlowQualifier.metaClass && semObj instanceof DbBEQualifier) {
            return getDefaultRoot().componentTree(DbBEFlow.metaClass);
        } else if (listClass == DbBEUseCaseQualifier.metaClass && semObj instanceof DbBEQualifier) {
            return getDefaultRoot().componentTree(DbBEUseCase.metaClass);
        } else if (listClass == DbBEUseCaseResource.metaClass && semObj instanceof DbBEUseCase) {
            return getDefaultRoot().componentTree(DbBEResource.metaClass);
        }
        return super.getLinkableEnum();
    }

    // Processes have a specific dialog
    protected String getLinkDialogTitle(MetaClass linkClass) {
        if (semObj instanceof DbBEQualifier) {
            if (listClass == DbBEActorQualifier.metaClass)
                return DbBEActor.metaClass.getGUIName(true, false);
            if (listClass == DbBEStoreQualifier.metaClass)
                return DbBEStore.metaClass.getGUIName(true, false);
            if (listClass == DbBEFlowQualifier.metaClass)
                return DbBEFlow.metaClass.getGUIName(true, false);
        }
        return super.getLinkDialogTitle(linkClass);
    }

    private DbObject[] showQualifierLinkDialog() {
        return DbTreeLookupDialog.selectMany(this, DbBEQualifier.metaClass.getGUIName(true, false),
                new DbObject[] { getDefaultRoot() }, new MetaClass[] { DbBEQualifier.metaClass },
                new DbTreeModelListener() {

                    public boolean isSelectable(DbObject dbo) throws DbException {
                        boolean selectable = true;
                        DbRelationN dbRelN = (DbRelationN) semObj.get(listRelations[0]);
                        DbEnumeration dbEnum = dbRelN.elements(listClass);
                        while (dbEnum.hasMoreElements()) {
                            DbObject comp = dbEnum.nextElement();
                            if (parentRel != null)
                                comp = (DbObject) comp.get(parentRel);
                            if (comp == dbo)
                                selectable = false;
                        }
                        dbEnum.close();
                        return selectable;
                    }

                    public Icon getIcon(DbObject dbo) throws DbException {
                        if (dbo != null && dbo instanceof DbBEQualifier) {
                            Image icon = ((DbBEQualifier) dbo).getIcon();
                            return icon == null ? AwtUtil.NULL_ICON : new ImageIcon(icon);
                        }
                        return super.getIcon(dbo);
                    }
                });
    }

    private DbObject[] showUseCaseLinkDialog() {

        DbTreeModelListener listener = new DbTreeModelListener() {

            public boolean isSelectable(DbObject dbo) throws DbException {
                // Already linked object is not selectable
                DbRelationN dbRelN = (DbRelationN) semObj.get(listRelations[0]);
                int nb = dbRelN.size();
                for (int i = 0; i < nb; i++) {
                    DbObject linkedDbo = dbRelN.elementAt(i);
                    if (parentRel != null)
                        linkedDbo = (DbObject) linkedDbo.get(parentRel);
                    if (linkedDbo == dbo)
                        return false;
                }
                return true;
            }

        };

        return DbTreeLookupDialog.selectMany(this, DbBEUseCase.metaClass.getGUIName(true, false),
                new DbObject[] { getDefaultRoot() }, new MetaClass[] { DbBEUseCase.metaClass },
                listener);
    }

    private DbObject getDefaultRoot() {
        DbObject root = null;
        try {
            semObj.getDb().beginTrans(Db.READ_TRANS);
            root = semObj.getCompositeOfType(DbBEModel.metaClass);
            semObj.getDb().commitTrans();

        } catch (DbException dbe) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(this, dbe);
        }
        return root;
    }

}
