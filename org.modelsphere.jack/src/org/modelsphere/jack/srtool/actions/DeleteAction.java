/*************************************************************************

Copyright (C) 2009 Grandite

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

You can redistribute and/or modify this particular file even under the
terms of the GNU Lesser General Public License (LGPL) as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License 
(LGPL) along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.jack.srtool.actions;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationN;
import org.modelsphere.jack.graphic.Attachment;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.LineLabel;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.FocusManager;
import org.modelsphere.jack.srtool.explorer.ExplorerView;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;

public class DeleteAction extends AbstractApplicationAction implements SelectionActionListener {

    private static final long serialVersionUID = 1L;

    public static String kDeleteAction = LocaleMgr.action.getString("delete");
    public static String kDeleteActionInvokedFromDgm = LocaleMgr.action.getString("deleteFromDgm");
    public static String kConfirmDeleteMultiple = LocaleMgr.action
            .getString("confirmDeleteMultiple");
    public static Icon deleteIcon = LocaleMgr.action.getImageIcon("delete");
    public static Icon emptyIcon = new ImageIcon(GraphicUtil.loadImage(ApplicationContext.class,
            "international/resources/empty.gif"));

    private enum Action {
        DELETE_ALL, REMOVE_GO, DO_NOTHING
    };

    public DeleteAction() {
        super(kDeleteActionInvokedFromDgm, deleteIcon);
        setEnabled(false);
        setVisible(ScreenPerspective.isFullVersion());
    }

    protected final void doActionPerformed() {
        try {
            FocusManager mgr = ApplicationContext.getFocusManager();
            Object[] selectedObjs = mgr.getSelectedObjects();

            //start transaction
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, selectedObjs, LocaleMgr.action
                    .getString("delete"));

            //build list of graphical objects selected by user (empty list if selection made from explorer)  
            List<DbObject> goList = buildListOfSelectedGraphicalObjects(selectedObjs);
            Action action = null;

            //for each selected object
            for (Object selectedObj : selectedObjs) {
                DbObject so = getSemanticObject(selectedObj);
                DbObject go = getGraphicalObject(selectedObj);

                boolean deleteMultiple = true;
                if ((so != null) && (go == null)) {
                    List<String> dependencies = findDependentObjects(so);
                    action = Action.DELETE_ALL;
                } else if ((so != null) && (go != null)) {
                    action = deleteGraphicalObject(so, go, selectedObjs, goList);
                } //end if

                DbObject dbo = getDbObjectToDelete(selectedObj);
                if (dbo != null && dbo.isDeletable()) {
                    if ((action == null) || (action == Action.DO_NOTHING)) {
                        //do nothing
                    } else if (action == Action.DELETE_ALL) {
                        dbo.doDeleteAction();
                    } else if (action == Action.REMOVE_GO) {
                        if (go != null)
                            go.remove();
                    } //end if

                } //end if 
            } //end for
            DbMultiTrans.commitTrans(selectedObjs);

            ////
            // deleting object in the selection causes DbDeadObject exception to occur if we
            // go directly in the menu after deleting to reset the state of the FocusManager,
            // return focus to the Explorer programmatically

            ApplicationContext.getFocusManager().setFocusToExplorer(
                    ApplicationContext.getDefaultMainFrame().getExplorerPanel().getMainView());
            ApplicationContext.getDefaultMainFrame().getExplorerPanel().getExplorer().refresh();

        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        } //end try
    } //end doActionPerformed()

    protected boolean objectHasRepresentationsInOtherDiagrams(List<DbObject> goList)
            throws DbException {
        boolean hasRepresentations = false;

        out: for (int i = 0; i < goList.size(); i++) {
            DbObject go1 = goList.get(i);
            for (int j = 0; j < goList.size(); j++) {
                if (j != i) {
                    DbObject go2 = goList.get(j);

                    //if same diagram
                    if (go1.getComposite().equals(go2.getComposite()) == false) {
                        hasRepresentations = true;
                        break out;
                    }
                }
            } //end for
        } //end for

        return hasRepresentations;
    }

    private List<String> findDependentObjects(DbObject so) throws DbException {
        List<String> dependencies = new ArrayList<String>();
        MetaClass mc = so.getMetaClass();
        MetaField[] mfs = mc.getAllMetaFields();

        for (int j = 0; j < mfs.length; j++) {
            if (mfs[j] instanceof MetaRelationN) {
                MetaRelationN mr = (MetaRelationN) mfs[j];
                DbRelationN relN = (DbRelationN) so.get(mr);
                DbEnumeration dbEnum = relN.elements();
                int count = 0;
                while (dbEnum.hasMoreElements()) {
                    dbEnum.nextElement();
                    count++;
                } //end while
                dbEnum.close();

                if (count > 0) {
                    String s = mr.getGUIName();
                    String msg = s + ":" + count;
                    dependencies.add(msg);
                }
            } //end if
        } //end for

        return dependencies;
    } //end findDependentObjects

    private Action deleteGraphicalObject(DbObject so, DbObject go, Object[] selectedObjs,
            List<DbObject> goList) throws DbException {
        boolean bYesToAll = false;
        boolean bNoToAll = false;
        MetaRelationN goRelationN = getGORelationN(so, go);
        boolean deleteMultiple = true;

        if (goRelationN != null) {
            List<DbObject> goListAllGOS = getAllGraphicalObjects(so, goRelationN);

            for (int j = 0; j < goList.size(); j++) {

                if (objectHasRepresentationsInOtherDiagrams(goListAllGOS)) {

                    if (bYesToAll) {
                        deleteMultiple = true;
                    } else if (bNoToAll) {
                        deleteMultiple = false;
                    } else if (!bYesToAll && !bNoToAll) {
                        int retval = JOptionPane.showConfirmDialog(ApplicationContext
                                .getDefaultMainFrame(), kConfirmDeleteMultiple,
                                ApplicationContext.getApplicationName(), JOptionPane.YES_NO_OPTION);

                        if (retval == JOptionPane.YES_OPTION) {
                            deleteMultiple = true;
                            bYesToAll = true;
                        } else if (retval == JOptionPane.NO_OPTION) {
                            deleteMultiple = false;
                            bNoToAll = true;
                            /*
                             * } else if (retval == JOptionPane.NO_OPTION) { for (int w = 0; w <
                             * selectedObjs.length; w++) { DbObject toDelete =
                             * getDbObjectToDelete(selectedObjs[w]); if (toDelete != null)
                             * toDelete.getDb().abortTrans(); } return deleteMultiple;
                             */
                        } //end if
                    } //end if

                    break;
                } //end if
            } //end for
        } //end if 

        Action action = deleteMultiple ? Action.DELETE_ALL : Action.DO_NOTHING;
        return action;
    } //end deleteGraphicalObject()

    private List<DbObject> getAllGraphicalObjects(DbObject so, MetaRelationN goRelationN)
            throws DbException {
        List<DbObject> goListAllGOS = new ArrayList<DbObject>();
        DbRelationN relN = (DbRelationN) so.get(goRelationN);
        DbEnumeration dbEnum = relN.elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbo2 = dbEnum.nextElement();
            goListAllGOS.add(dbo2);
        }
        dbEnum.close();
        return goListAllGOS;
    }

    public final void updateSelectionAction() throws DbException {
        Object[] selectedObjs = ApplicationContext.getFocusManager().getSelectedObjects();
        List<DbObject> goList = buildListOfSelectedGraphicalObjects(selectedObjs);

        Object object = ApplicationContext.getFocusManager().getFocusObject();
        if (object instanceof ExplorerView) {
            setName(kDeleteAction);
            setIcon(deleteIcon);
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        } else {
            setName(kDeleteActionInvokedFromDgm);
            setIcon(deleteIcon);
            setAccelerator(KeyStroke.getKeyStroke("shift DELETE"));
        }
        //for each semantic object
        boolean enabled = false;
        for (int i = 0; i < selectedObjs.length; i++) {
            DbObject so = getSemanticObject(selectedObjs[i]);
            DbObject go = getGraphicalObject(selectedObjs[i]);

            if ((so != null) || (go != null)) {
                enabled = true;
            }

            if ((so != null) && (go != null)) {
                MetaRelationN goRelationN = getGORelationN(so, go);
                if (goRelationN != null) {
                    //for each graphical representation of the semantic object
                    DbRelationN relN = (DbRelationN) so.get(goRelationN);
                    DbEnumeration dbEnum = relN.elements();
                    while (dbEnum.hasMoreElements()) {
                        //if at least one graphical object exists outside of the selection 
                        DbObject dbo = dbEnum.nextElement();
                        if (!goList.contains(dbo)) {
                            //disable the Delete action
                            //enabled = false;
                            break;
                        }
                    } //end while
                    dbEnum.close();
                } //end if
            } //end if

            if (enabled == false)
                break;
        } //end for

        setEnabled(enabled);
    } //end updateSelectionAction()

    //
    // private methods
    //

    //build list of graphical objects selected by user (empty list if selection made from explorer)  
    private List<DbObject> buildListOfSelectedGraphicalObjects(Object[] selectedObjs) {
        List<DbObject> goList = new ArrayList<DbObject>();
        for (int i = 0; i < selectedObjs.length; i++) {
            DbObject go = getGraphicalObject(selectedObjs[i]);
            if (go != null) {
                goList.add(go);
            }
        } //end for
        return goList;
    } //end buildListOfSelectedGraphicalObjects()

    private DbObject getDbObjectToDelete(Object obj) throws DbException {
        boolean isDeletable = true;
        DbObject dbo;
        if (obj instanceof ActionInformation) {
            if (obj instanceof LineLabel || obj instanceof Attachment)
                return null;
            ActionInformation info = (ActionInformation) obj;
            dbo = info.getSemanticalObject();
            if (dbo == null) {
                dbo = info.getGraphicalObject();
                isDeletable = dbo.isDeletable();
            } else {
                DbObject go = info.getGraphicalObject();
                isDeletable = dbo.isDeletable() && go.isDeletable();
            }
        } else if (obj instanceof DbObject) {
            dbo = (DbObject) obj;
            isDeletable = dbo.isDeletable();
        } else
            // explorer node <RAM> or <Repository>
            return null;

        return (dbo.getTransStatus() != Db.OBJ_REMOVED && isDeletable ? dbo : null);
    } //end getDeleteObject()

    private DbObject getGraphicalObject(Object selectedObj) {
        DbObject go = null;

        if (selectedObj instanceof ActionInformation) {
            ActionInformation info = (ActionInformation) selectedObj;
            go = info.getGraphicalObject();
        }

        return go;
    } //end getGraphicalObject()

    private DbObject getSemanticObject(Object selectedObj) throws DbException {
        DbObject so = null;

        if (selectedObj instanceof ActionInformation) {
            ActionInformation info = (ActionInformation) selectedObj;
            so = info.getSemanticalObject();
        }

        if (so == null) {
            so = getDbObjectToDelete(selectedObj);
        }

        return so;
    } //end getSemanticObject()

    //get the MetaRelationN allowing to navigate from a semantic object to all its graphical objects
    // @param so : semantic object
    // @param go : one of the graphical objects accessible by the relationN
    private MetaRelationN getGORelationN(DbObject so, DbObject go) throws DbException {
        MetaRelationN goRelationN = null;
        MetaClass mc = so.getMetaClass();

        MetaField[] mfs = mc.getAllMetaFields();
        for (int j = 0; j < mfs.length; j++) {
            if (mfs[j] instanceof MetaRelationN) {
                MetaRelationN mr = (MetaRelationN) mfs[j];
                DbRelationN relN = (DbRelationN) so.get(mr);
                DbEnumeration dbEnum = relN.elements();
                while (dbEnum.hasMoreElements()) {
                    DbObject dbo = dbEnum.nextElement();
                    if (dbo.equals(go)) {
                        goRelationN = mr;
                        break;
                    }
                } //end while
                dbEnum.close();
            } //end if

            if (goRelationN != null)
                break;
        } //end for

        return goRelationN;
    } //end getGORelationN()
} //end DeleteAction

