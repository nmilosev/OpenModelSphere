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
package org.modelsphere.sms.screen;

//Java
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.screen.DbListView;
import org.modelsphere.jack.baseDb.screen.DbTreeLookupDialog;
import org.modelsphere.jack.baseDb.screen.model.DbListModel;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModelListener;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.SrScreenContext;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.DbSMSUmlConstraint;
import org.modelsphere.sms.db.util.AnySemObject;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.plugins.TargetSystem;
import org.modelsphere.sms.plugins.TargetSystemManager;
import org.modelsphere.sms.screen.model.SMSListModel;

public class SMSListView extends DbListView {

    private static final String DO_NOT_DELETE_TS = LocaleMgr.screen
            .getString("DoNotDeleteTargetSystem");
    private static final String DO_NOT_DELETE_LOGICAL_TS = LocaleMgr.screen
            .getString("DoNotDeleteLogicalTargetSystem");
    private static final String kDelUserTarget = LocaleMgr.message.getString("DelUserTarget");
    private static final String kDelAllUserTargets = LocaleMgr.message
            .getString("DelAllUserTargets");
    private static final String kDelOneORMoreUserTarget = LocaleMgr.message
            .getString("DelOneORMoreUserTarget");

    public SMSListView(DbObject semObj, MetaRelationN listRelation, MetaClass listClass,
            int actionMode) throws DbException {
        super(SrScreenContext.singleton, semObj, listRelation, listClass, actionMode);
    }

    public SMSListView(DbObject semObj, MetaRelationN[] listRelations, MetaClass listClass,
            int actionMode) throws DbException {
        super(SrScreenContext.singleton, semObj, listRelations, listClass, actionMode);
    }

    protected final DbListModel createListModel() throws DbException {
        return new SMSListModel(this, semObj, listRelations, listClass);
    }

    public final DbObject[] getSelection() {
        if (listClass == DbSMSTargetSystem.metaClass)
            return new DbObject[0];
        else
            return super.getSelection();
    }

    public final void addAction() {
        if (listClass == DbSMSTargetSystem.metaClass)
            addTargetSystem();
        else
            super.addAction();
    }

    private void addTargetSystem() {
        try {
            TargetSystemManager.getSingleton().addTargetSystem(this, semObj, true);
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(this, e);
        }
    }

    public final void deleteAction() {
        if (listClass == DbSMSTargetSystem.metaClass) {
            if (checkDeleteTargetSystem())
                super.deleteAction();
        } else
            super.deleteAction();
    }

    private boolean checkDeleteTargetSystem() {
        try {
            boolean ret = true;
            boolean delLogic = false;
            DbSMSTargetSystem anyDbts = null;
            DbSMSTargetSystem userDbts = null;
            boolean isFreeUserTS = false;
            ArrayList userTargetList = new ArrayList();
            semObj.getDb().beginTrans(Db.WRITE_TRANS);
            DbListModel model = (DbListModel) getModel();
            int[] selRows = getSelectedRows();
            for (int i = 0; i < selRows.length; i++) {
                anyDbts = (DbSMSTargetSystem) model.getParentValue(selRows[i]);
                int rootID = AnyORObject.getRootIDFromTargetSystem(anyDbts);
                if (rootID == TargetSystem.SGBD_LOGICAL) {
                    delLogic = true;
                    ret = false;
                    break;
                }
                if (anyDbts.getPackages().size() > 1) {
                    ret = false;
                    break;
                }
                if (!anyDbts.isBuiltIn()) {
                    userTargetList.add(anyDbts);
                }
            }
            JFrame frame = ApplicationContext.getDefaultMainFrame();
            String message = null;
            if (ret && userTargetList.size() > 0) {
                if (userTargetList.size() == 1 && selRows.length == 1) {
                    message = kDelUserTarget; // le target user...
                } else if (userTargetList.size() != selRows.length) {
                    message = kDelOneORMoreUserTarget; // un de la liste est
                    // user
                } else if (userTargetList.size() == selRows.length) {
                    message = kDelAllUserTargets; // Tous les target user
                    // seront...
                }
                int rc = JOptionPane.showConfirmDialog(frame, message,
                        ApplicationContext.getApplicationName(), JOptionPane.YES_NO_CANCEL_OPTION);
                if (rc == JOptionPane.YES_OPTION) {
                    for (int j = 0; j < userTargetList.size(); j++) {
                        userDbts = (DbSMSTargetSystem) userTargetList.get(j);
                        TargetSystem.removeUserTargetSystem(userDbts.getID());
                    }
                } else {
                    ret = false;
                    isFreeUserTS = true;
                }
            }
            semObj.getDb().commitTrans();

            if (!ret) {
                if (delLogic)
                    ExceptionHandler.showErrorMessage(this, DO_NOT_DELETE_LOGICAL_TS);
                else if (!isFreeUserTS)
                    ExceptionHandler.showErrorMessage(this, DO_NOT_DELETE_TS);// message
                // et
                // comportement
                // incorrect
            }
            userTargetList.clear();
            return ret;
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(this, e);
            return false;
        }
    }

    protected final DbObject[] showLinkDialog() {
        if (listRelations[0] == DbSMSLink.fSourceObjects
                || listRelations[0] == DbSMSLink.fTargetObjects) {
            DbTreeModelListener listener = new DbTreeModelListener() {
                public boolean filterNode(DbObject dbo) throws DbException {
                    return !(dbo instanceof DbSMSLinkModel || dbo instanceof DbSMSTargetSystem);
                }

                public boolean isSelectable(DbObject dbo) throws DbException {
                    return (AnySemObject.supportsLinks(dbo.getMetaClass())
                            && ((DbSMSLink) semObj).getSourceObjects().indexOf(dbo) == -1 && ((DbSMSLink) semObj)
                            .getTargetObjects().indexOf(dbo) == -1);
                }
            };
            return DbTreeLookupDialog.selectMany(this, getTabName(), new DbObject[] { semObj
                    .getProject() }, new MetaClass[] { listClass }, listener);
        } else if (listRelations[0] == DbSMSSemanticalObject.fUmlConstraints) {
            DbTreeModelListener listener = new DbTreeModelListener() {
                public boolean filterNode(DbObject dbo) throws DbException {
                    if (!(dbo instanceof DbSMSUmlConstraint))
                        return true;
                    MetaClass metaclass = getDbObject().getMetaClass();
                    DbSMSUmlConstraint constraint = (DbSMSUmlConstraint) dbo;
                    String filterMetaClassName = constraint.getMetaClassName();
                    MetaClass filterMetaClass = null;
                    if (filterMetaClassName != null)
                        filterMetaClass = MetaClass.find(filterMetaClassName);
                    if (filterMetaClass == null)
                        filterMetaClass = DbSMSSemanticalObject.metaClass;
                    return filterMetaClass.isAssignableFrom(metaclass);
                }

                public boolean isSelectable(DbObject dbo) throws DbException {
                    return (dbo instanceof DbSMSUmlConstraint);
                }
            };
            return DbTreeLookupDialog.selectMany(this, getTabName(), new DbObject[] { semObj
                    .getProject() }, new MetaClass[] { listClass }, listener);
        }
        return super.showLinkDialog();
    }
}
