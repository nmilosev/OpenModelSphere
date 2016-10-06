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

package org.modelsphere.sms;

import java.awt.Point;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyVetoException;
import java.text.MessageFormat;

import javax.swing.JInternalFrame;

import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.SemanticalModel;
import org.modelsphere.jack.srtool.features.SrDragDrop;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.sms.actions.CreateCommonItemColumnsAction;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.actions.SendToDiagramAction;
import org.modelsphere.sms.be.actions.BEActionConstants;
import org.modelsphere.sms.be.actions.LinkQualifiersAndResourcesAction;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.java.db.util.AnyAdt;
import org.modelsphere.sms.or.actions.ORActionFactory;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.util.AnyORObject;

public class SMSDragDrop extends SrDragDrop {

    public static final SMSDragDrop singleton = new SMSDragDrop();

    private SMSDragDrop() {
    }

    // Called in a read transaction for each object in <dragObjs>
    protected final int getActionsAllowed(DbObject[] dragObjs) throws DbException {
        int actions = super.getActionsAllowed(dragObjs);
        return actions;
    }

    // No transaction started when it is called
    // Since it is called at each mouse move, you should avoid to start a
    // transaction here.
    protected final boolean isDropObjectAcceptable(DbObject[] dragObjs, DbObject dropObj, int action)
            throws DbException {
        if (dragObjs != null && (action == DnDConstants.ACTION_MOVE))
            return ApplicationContext.getSemanticalModel().isValidForDrop(dragObjs,
                    new DbObject[] { dropObj });

        if (dragObjs != null && (action | DnDConstants.ACTION_COPY) != 0)
            return ApplicationContext.getSemanticalModel().isValidForDrop(dragObjs,
                    new DbObject[] { dropObj });

        return false;
    }

    public final void focusContainingFrame(ApplicationDiagram diagram) {

        DbObject diagGo = diagram.getDiagramGO();
        ApplicationDiagram m_matchingAppDiag = null;

        JInternalFrame[] frames = ApplicationContext.getDefaultMainFrame()
                .getDiagramInternalFrames();
        int i, nb;
        for (i = nb = 0; i < frames.length; i++) {
            diagram = (ApplicationDiagram) ((DiagramInternalFrame) frames[i]).getDiagram();
            DbSMSDiagram matchingDiagGo = (DbSMSDiagram) diagram.getDiagramGO();
            if (diagGo.equals(matchingDiagGo)) {
                try {
                    DiagramInternalFrame diagFrame = ((DiagramInternalFrame) frames[i]);
                    if (!diagFrame.isSelected()) {
                        diagFrame.setSelected(true);
                        // diagFrame.setf
                        ApplicationContext.getFocusManager().update();
                        break;
                    }
                } catch (PropertyVetoException pv) {
                    org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                            ApplicationContext.getDefaultMainFrame(), pv);
                }
            }
        }
    }

    // No transaction started when it is called
    protected final void performAction(DbObject[] dragObjs, DbObject dropObj, int action,
            Point location) throws DbException {
        if ((action & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
            if (ApplicationContext.getSemanticalModel().isCreateCommonItems(dragObjs, dropObj)) {
                CreateCommonItemColumnsAction smsAction = (CreateCommonItemColumnsAction) ApplicationContext
                        .getActionStore().get(ORActionFactory.CREATE_COMMON_ITEM_COLUMNS);
                smsAction.performAction(dragObjs, dropObj, location);
            } else if (ApplicationContext.getSemanticalModel().isValidCopyOperation(dragObjs,
                    dropObj)) {

                if (ApplicationContext.getSemanticalModel().isLinkQualifierOrResource(dragObjs,
                        dropObj)) {
                    LinkQualifiersAndResourcesAction smsAction = (LinkQualifiersAndResourcesAction) ApplicationContext
                            .getActionStore().get(
                                    BEActionConstants.BE_LINK_RESOURCES_AND_QUALIFIERS);
                    smsAction.performAction(dragObjs, dropObj, location);
                } else if ((action & DnDConstants.ACTION_COPY) != 0) {
                    DbMultiTrans.beginTrans(Db.WRITE_TRANS, dragObjs, getTransactionName(kCopy,
                            kCopy0, dragObjs));
                    ApplicationContext.getDefaultMainFrame().getClipboard().copy(dragObjs);
                    ApplicationContext.getDefaultMainFrame().getClipboard().pasteTo(
                            new DbObject[] { dropObj }, location);
                    DbMultiTrans.commitTrans(dragObjs);
                } else if ((action & DnDConstants.ACTION_MOVE) != 0) {
                    DbMultiTrans.beginTrans(Db.WRITE_TRANS, dragObjs, getTransactionName(kMove,
                            kMove0, dragObjs));
                    ApplicationContext.getDefaultMainFrame().getClipboard().copy(dragObjs);
                    ApplicationContext.getDefaultMainFrame().getClipboard().pasteTo(
                            new DbObject[] { dropObj }, false, location);
                    for (int i = 0; i < dragObjs.length; i++)
                        dragObjs[i].remove();
                    DbMultiTrans.commitTrans(dragObjs);
                } else {

                }
            } else {
                SendToDiagramAction smsAction = (SendToDiagramAction) ApplicationContext
                        .getActionStore().get(SMSActionsStore.SEND_TO_DIAGRAM);
                smsAction.performAction(dragObjs, dropObj, location);
            }
        }
    }

    private String getTransactionName(String genericName, String pattern, DbObject[] dragObjs)
            throws DbException {
        if (dragObjs != null && dragObjs.length == 1 && dragObjs[0] != null) {
            return MessageFormat.format(pattern, new Object[] { ApplicationContext
                    .getSemanticalModel().getDisplayText(dragObjs[0].getMetaClass(), null,
                            SMSDragDrop.class) });
        }
        return genericName;
    }

    public boolean wantHighlight(DbObject[] dragObjs, DbObject dropObj) {
        /*
         * if(ApplicationContext.getSemanticalModel().isCreateCommonItems(dragObjs , dropObj))
         * return true; else if(ApplicationContext.getSemanticalModel
         * ().isValidCopyOperation(dragObjs, dropObj)) return true; else
         */
        return true;
    }

    private boolean isTypable(DbObject dbo, DbObject type) throws DbException {
        if (dbo.getProject() != type.getProject())
            return false;
        if (type instanceof DbORTypeClassifier) {
            if (dbo instanceof DbORCommonItem)
                return true;
            if (AnyORObject.getTypeField(dbo) == null)
                return false;
            dbo.getDb().beginTrans(Db.READ_TRANS);
            boolean typable = (AnyORObject.getTargetSystem(dbo) == AnyORObject
                    .getTargetSystem(type));
            dbo.getDb().commitTrans();
            return typable;
        } else if (type instanceof DbORCommonItem) {
            return (dbo instanceof DbORColumn);
        } else { // type instanceof DbOOAdt
            return (AnyAdt.getTypeField(dbo) != null);
        }
    }

    /*
     * OO: DataMember/Method/Parameter -> Adt OR:
     * CommonItem/Domain/Attribute/Parameter/Procedure/Method -> TypeClassifier Column -> CommonItem
     */
    private void setType(DbObject dbo, DbObject type) throws DbException {
        MetaRelationship field;
        String transName = LocaleMgr.action.getString("setType");
        if (type instanceof DbORTypeClassifier) {
            field = AnyORObject.getTypeField(dbo);
        } else if (type instanceof DbORCommonItem) {
            field = DbORColumn.fCommonItem;
            transName = org.modelsphere.sms.or.international.LocaleMgr.action
                    .getString("setCommonItem");
        } else { // type instanceof DbOOAdt
            field = AnyAdt.getTypeField(dbo);
        }
        dbo.getDb().beginTrans(Db.WRITE_TRANS, transName);
        dbo.set(field, type);
        dbo.getDb().commitTrans();
    }
}
