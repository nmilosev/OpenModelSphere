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

import java.util.ArrayList;
import java.util.Vector;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.features.PropagateCommonItemDialog;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.db.DbOOClass;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.graphic.AdtBox;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.graphic.ORTable;
import org.modelsphere.sms.or.graphic.ORTableBox;
import org.modelsphere.sms.or.international.LocaleMgr;

public final class PropagateCommonItemValuesAction extends AbstractApplicationAction implements
        SelectionActionListener {

    private static final String PROPAGATE_VALUES = LocaleMgr.action
            .getString("PropagateCommonItemValues");

    public PropagateCommonItemValuesAction() {
        super(PROPAGATE_VALUES + "...");
        setEnabled(true);
        setVisible(ScreenPerspective.isFullVersion());
    }

    protected final void doActionPerformed() {
        try {
            MainFrame frame = MainFrame.getSingleton();
            DbObject[] semObjs = null;

            Object focusObject = ApplicationContext.getFocusManager().getFocusObject();
            if (focusObject instanceof ApplicationDiagram) {
                ArrayList<DbObject> selectedObjectsList = new ArrayList<DbObject>();
                Object[] objs = ApplicationContext.getFocusManager().getActiveDiagram()
                        .getSelectedObjects();
                for (int i = 0; i < objs.length; i++) {
                    if (objs[i] instanceof ORTableBox) {
                        selectedObjectsList.add(((ORTableBox) objs[i]).getSemanticalObject());
                    } else if (objs[i] instanceof DbORColumn)
                        selectedObjectsList.add((DbObject) objs[i]);
                    else if (objs[i] instanceof AdtBox) {
                        selectedObjectsList.add(((AdtBox) objs[i]).getSemanticalObject());
                    } else if (objs[i] instanceof DbJVDataMember) {
                        selectedObjectsList.add((DbObject) objs[i]);
                    }
                }
                int count = selectedObjectsList.size();
                if (selectedObjectsList.size() != 0) {
                    semObjs = new DbObject[count];
                    for (int i = 0; i < count; i++)
                        semObjs[i] = selectedObjectsList.get(i);
                } else {
                    if (count == 0) { //act on the entire model that is the composite of the diagram 
                        ArrayList<DbObject> objectsList = new ArrayList<DbObject>();
                        DbObject diagramObject = ((ApplicationDiagram) focusObject).getDiagramGO();
                        diagramObject.getDb().beginReadTrans();
                        DbObject object = diagramObject.getComposite();
                        diagramObject.getDb().commitTrans();
                        object.getDb().beginReadTrans();

                        if (object instanceof DbOOAbsPackage) {
                            DbEnumeration dbEnum = object.getComponents().elements(
                                    DbOOClass.metaClass);
                            while (dbEnum.hasMoreElements())
                                objectsList.add(dbEnum.nextElement());
                            dbEnum.close();
                        } else if (object instanceof DbORDataModel) {
                            DbEnumeration dbEnum = object.getComponents().elements(
                                    DbORAbsTable.metaClass);
                            while (dbEnum.hasMoreElements())
                                objectsList.add(dbEnum.nextElement());
                            dbEnum.close();
                        }

                        object.getDb().commitTrans();

                        semObjs = new DbObject[objectsList.size()];
                        objectsList.toArray(semObjs);
                    }
                }
            } else
                semObjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();

            if (semObjs == null || semObjs.length < 1)
                return;

            PropagateCommonItemDialog dialog = new PropagateCommonItemDialog(frame,
                    PROPAGATE_VALUES, true, semObjs);
            dialog.pack();
            AwtUtil.centerWindow(dialog);
            dialog.setVisible(true);

        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        } //end try

    }

    public final void updateSelectionAction() throws DbException {

        // common items apply to the rdata model and the class model, the
        // project,
        // the class field, the table columns, the tables, the classes, and
        // finally when the diagram
        // window has the focus, we want to act on the model itself

        setEnabled(false);
        Object obj = ApplicationContext.getFocusManager().getFocusObject();
        DbObject dbObjs[] = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        if (obj instanceof ApplicationDiagram && dbObjs.length == 0) {
            DbObject dbo = ((ApplicationDiagram) obj).getDiagramGO();
            if (dbo instanceof DbORDiagram || dbo instanceof DbOODiagram)
                setEnabled(true);
            return;
        } else if (dbObjs.length == 0)
            return;

        boolean enabled = true;
        for (int i = 0; i < dbObjs.length; i++) {
            if (!(dbObjs[i] instanceof DbORAbsTable || dbObjs[i] instanceof DbOOClass
                    || dbObjs[i] instanceof DbOODataMember || dbObjs[i] instanceof DbORColumn
                    || dbObjs[i] instanceof DbORCommonItem
                    || dbObjs[i] instanceof DbORCommonItemModel
                    || dbObjs[i] instanceof DbORDataModel || dbObjs[i] instanceof DbOOAbsPackage || dbObjs[i] instanceof DbProject)) {
                enabled = false;
                break;
            }
        }
        setEnabled(enabled);
    }

    protected int getFeatureSet() {
        return SMSFilter.RELATIONAL;
    }
} // end GenerateCommonItemsAction

