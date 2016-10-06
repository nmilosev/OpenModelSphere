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

package org.modelsphere.sms.actions;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.baseDb.screen.PUKLookupDialog;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.AddElement;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.actions.ExpandAllAction;
import org.modelsphere.jack.srtool.actions.ShowDiagramAction;
import org.modelsphere.jack.srtool.explorer.ExplorerPanel;
import org.modelsphere.jack.srtool.explorer.ExplorerView;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.db.util.DbUtility;
import org.modelsphere.sms.graphic.SMSClassifierBox;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.generic.db.DbGECheck;
import org.modelsphere.sms.preference.DiagramAutomaticCreationOptionGroup;
import org.modelsphere.sms.screen.SMSScreenPerspective;

public final class AddAction extends org.modelsphere.jack.srtool.actions.AddAction {
    protected static final String kAddTransName0 = LocaleMgr.misc.getString("Add0");

    TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    public AddAction() {
    	setVisible(ScreenPerspective.isFullVersion());
    }

    public void performAction(DbObject[] dragObjs, DbObject dropObj) throws DbException {

        // pre-condition: all dragged objects and the drop container are valid

        // //
        // this is typically called by the object-to-container drag and drop
        // feature
        // in this case we must create the AddElement item programatically

        DbMultiTrans.beginTrans(Db.WRITE_TRANS, dragObjs, "drag and drop");
        for (int i = 0; i < dragObjs.length; i++) {

            // //
            // build AddElement item for the AddAction logic

            MetaClass mc = dragObjs[i].getMetaClass();

            AddElement ae = new AddElement(mc, dropObj.getMetaClass());
            Object[] retval = performAction(mc, dropObj, ae);
            DbObject newObject = (DbObject) retval[0];
            DbObject oldObject = dragObjs[i];

            // //
            // copy the metafield values from the source to the target object

            MetaField[] metaFields = mc.getAllMetaFields();
            for (int j = 0; j < metaFields.length; j++) {
                try {
                    if (metaFields[j] != DbObject.fComposite)
                        newObject.set(metaFields[j], oldObject.get(metaFields[j]));
                } catch (Exception dbe) {
                }
            }
        }
        DbMultiTrans.commitTrans(dragObjs);

    }

    private Object[] performAction(MetaClass componentMetaClass, DbObject composite,
            AddElement selected) throws DbException {

        DbObject object = null;
        Object focusObj = ApplicationContext.getFocusManager().getFocusObject();

        boolean abortOverride = false, bExpandModelNode = false;

        // if composite is a process and component metaclass is a diagram,
        // this is a special case
        if (composite instanceof DbBEUseCase) {
            if (focusObj instanceof ApplicationDiagram) {
                ApplicationDiagram diag = (ApplicationDiagram) focusObj;
                DbBEDiagram beDiagram = (DbBEDiagram) diag.getDiagramGO();
                if (componentMetaClass.equals(DbBEDiagram.metaClass)) {
                    object = DbUtility.addDiagramElement((DbBEUseCase) composite, beDiagram);
                } else {
                    object = selected.createElement(composite);
                    processUseCase((DbBEUseCase) object, beDiagram);
                }
            } else if (focusObj instanceof ExplorerView) {
                DbBEDiagram beDiagram = (DbBEDiagram) DbUtility
                        .selectOneDiagram((DbBEUseCase) composite);
                if (componentMetaClass.equals(DbBEDiagram.metaClass)) {
                    object = DbUtility.addDiagramElement((DbBEUseCase) composite, beDiagram);
                } else {
                    object = selected.createElement(composite);
                    processUseCase((DbBEUseCase) object, beDiagram);
                }
            } // end if
        } else if (componentMetaClass.equals(DbORColumn.metaClass)) {
            object = selected.createElement(composite);
            if (focusObj instanceof ApplicationDiagram) {
                ApplicationDiagram diag = (ApplicationDiagram) focusObj;
                DbORDiagram orDiagram = (DbORDiagram) diag.getDiagramGO();
                processColumn((DbORColumn) object, orDiagram);
            } else if (focusObj instanceof ExplorerView) {
                DbORDiagram orDiagram = (DbORDiagram) DbUtility
                        .selectOneDiagram((DbORAbsTable) composite);
                processColumn((DbORColumn) object, orDiagram);
            } // end if
        } else {
            object = selected.createElement(composite);
            if (object instanceof DbSMSDiagram) {
                DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
                mainFrame.getExplorerPanel().getExplorer().refresh();
                ShowDiagramAction action = (ShowDiagramAction) ApplicationContext.getActionStore()
                        .get(AbstractActionsStore.SHOW_DIAGRAM);
                action.performAction(object);
            } else if (object instanceof DbJVClassModel) {
                DbOODiagram diagram = new DbOODiagram(object);
                ShowDiagramAction action = (ShowDiagramAction) ApplicationContext.getActionStore()
                        .get(AbstractActionsStore.SHOW_DIAGRAM);
                action.performAction(diagram);
                bExpandModelNode = true;
            } else if (object instanceof DbORDataModel) {
                if (DiagramAutomaticCreationOptionGroup.isCreateDataModelDiagram()) {
                    autoCreateDiagramFrame(object);
                }
                bExpandModelNode = true;
            } else if (object instanceof DbORDomainModel) {
                if (DiagramAutomaticCreationOptionGroup.isCreateDomainModelDiagram()) {
                    autoCreateDiagramFrame(object);
                }
                bExpandModelNode = true;
            } else if (object instanceof DbORCommonItemModel) {
                if (DiagramAutomaticCreationOptionGroup.isCreateCommonItemModelDiagram()) {
                    autoCreateDiagramFrame(object);
                }
                bExpandModelNode = true;
            } else if (object instanceof DbJVPackage) {
                if (DiagramAutomaticCreationOptionGroup.isCreateJavaDiagram()) {
                    autoCreateDiagramFrame(object);
                }
                bExpandModelNode = true;
            } else if (object instanceof DbBEModel) {
                if (DiagramAutomaticCreationOptionGroup.isCreateBPMModelContextDiagram()) {
                    autoCreateDiagramFrame(object);
                }
                bExpandModelNode = true;
            }
        }
        return new Object[] { object, new Boolean(abortOverride), new Boolean(bExpandModelNode) };
    }

    private void autoCreateDiagramFrame(DbObject object) throws DbException {
        if (object == null)
            return;

        if (object instanceof DbBEModel) { // must get the context in place of
            // model node
            DbEnumeration dbe = (DbEnumeration) object.getComponents().elements(
                    DbBEUseCase.metaClass);
            if (dbe.hasMoreElements())
                object = dbe.nextElement();
            dbe.close();
        }
        DbEnumeration dbe = (DbEnumeration) object.getComponents().elements(DbSMSDiagram.metaClass);
        DbObject diagramObject = null;
        if (dbe.hasMoreElements())
            diagramObject = dbe.nextElement();
        dbe.close();

        ShowDiagramAction action = (ShowDiagramAction) ApplicationContext.getActionStore().get(
                AbstractActionsStore.SHOW_DIAGRAM);
        action.performAction(diagramObject);
    }

    private void autoExpandModelNode(DbObject object) {

        ExplorerView view = ApplicationContext.getDefaultMainFrame().getExplorerPanel()
                .getMainView();
        view.clearSelection();

        ExpandAllAction action2 = (ExpandAllAction) ApplicationContext.getActionStore().get(
                AbstractActionsStore.EXPAND_ALL);
        action2.performAction(object);
    }
    
    @Override
    protected boolean isVisibleElement(AddElement element) {
        return (ScreenPerspective.isFullVersion());
    }

    protected final void doActionPerformed() {

        AddElement selected = (AddElement) getSelectedObject();

        if (selected == null)
            return;

        // pre configure the delegate
        selected.setChoiceValuesSelectedIndex(subIndex);

        boolean bExpandModelNode = false;

        try {
            DbObject[] objects = ApplicationContext.getFocusManager()
                    .getSelectedSemanticalObjects();
            int i;
            DbObject object = null;
            SMSClassifierBox box = null;
            Object focusObject = ApplicationContext.getFocusManager().getFocusObject();
            if (objects.length == 0) {
                if (focusObject instanceof ApplicationDiagram) {
                    DbObject diagramGO = ((ApplicationDiagram) focusObject).getDiagramGO();
                    objects = new DbObject[] { diagramGO };
                }
            }

            MetaClass componentMetaClass = selected.getMetaClass();
            String transName = selected.getTransName();
            if (transName == null)
                transName = MessageFormat.format(kAddTransName0, new Object[] { selected
                        .getMetaClass() });

            boolean abortOverride = false;

            DbObject[] toSelectInExplorer = new DbObject[objects.length];
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, objects, transName);
            for (i = 0; i < objects.length; i++) {
                DbObject composite;
                if (objects[i] instanceof ActionInformation) {
                    composite = ((ActionInformation) objects[i]).getSemanticalObject();
                    if (objects[i] instanceof SMSClassifierBox)
                        box = (SMSClassifierBox) objects[i];
                } else
                    composite = (DbObject) objects[i];

                Object[] retval = performAction(componentMetaClass, composite, selected);

                object = (DbObject) retval[0];
                abortOverride = ((Boolean) retval[1]).booleanValue();
                bExpandModelNode = ((Boolean) retval[2]).booleanValue();
                toSelectInExplorer[i] = object;

                if (object instanceof DbORPrimaryUnique) {

                    TerminologyUtil tm = TerminologyUtil.getInstance();
                    DbORDataModel model = (DbORDataModel) tm.isCompositeDataModel(object);
                    if (tm.getModelLogicalMode(model) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                        if (abortOverride == true)
                            break;
                        MetaRelationN[] listRelations = { DbORPrimaryUnique.fColumns };
                        MetaRelationship parentRel = null;
                        boolean[] outValCancel = { false };
                        PUKLookupDialog lookup = new PUKLookupDialog(object, listRelations,
                                DbORColumn.metaClass, parentRel);
                        DbObject[] dbo = lookup.linkAction(outValCancel);
                        if (outValCancel[0] == true) // the dialog was
                            // dismissed, cancel the
                            // transaction
                            abortOverride = true;
                        else {
                            if (dbo == null) {
                                this.startEditing(ApplicationContext.getDefaultMainFrame()
                                        .getExplorerPanel().getMainView(), object, box);
                            }
                        }
                    }
                }
                if (object instanceof DbGECheck) {
                    TerminologyUtil tm = TerminologyUtil.getInstance();
                    DbORDataModel model = (DbORDataModel) tm.isCompositeDataModel(object);
                    if (tm.getModelLogicalMode(model) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                        this.startEditing(ApplicationContext.getDefaultMainFrame()
                                .getExplorerPanel().getMainView(), object, box);
                    }
                }

            } // end if

            if (abortOverride == false) {
                DbMultiTrans.commitTrans(objects);

                if (toSelectInExplorer.length > 1)
                    ApplicationContext.getDefaultMainFrame().findInExplorer(toSelectInExplorer);
            }

            else
                for (i = 0; i < objects.length; i++)
                    if (objects[i] instanceof DbObject)
                        ((DbObject) objects[i]).getDb().abortTrans();

            if (objects.length == 1 && object != null) {
                Object obj = ApplicationContext.getFocusManager().getFocusObject();
                startEditing(obj, object, box);
            }
            if (bExpandModelNode == true)
                autoExpandModelNode(object);

        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    protected final void startEditing(Object origin, DbObject editingObject, SMSClassifierBox box) {
        if (origin instanceof ExplorerView)
            ((ExplorerView) origin).startEditing(editingObject);
        else if (box != null)
            box.editMember((DbSemanticalObject) editingObject);
    }

    private void processColumn(DbORColumn column, DbORDiagram orDiagram) throws DbException {
        DbORDataModel dataModel = (DbORDataModel) column
                .getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        String sterm = term.getTerm(DbORColumn.metaClass);
        column.setName(sterm);
    } // end processColumn()

    private void processUseCase(DbBEUseCase useCase, DbBEDiagram beDiagram) throws DbException {
        if (useCase == null)
            return;

        DbObject dbo = useCase.getCompositeOfType(DbSMSPackage.metaClass);
        Terminology terminology = terminologyUtil.findModelTerminology(dbo);
        if (terminology != null) {
            String term = terminology.getTerm(DbBEUseCase.metaClass);
            useCase.setName(term);
        }
    } // end processUseCase()

} // end AddAction
