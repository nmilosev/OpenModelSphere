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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JList;

import org.modelsphere.jack.awt.JackToolBar;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.tool.DefaultLinkUnlinkListPanel;
import org.modelsphere.jack.graphic.tool.LinkUnlinkTool;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.graphic.tool.ToolButtonGroup;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.CurrentFocusListener;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.international.LocaleMgr;

public class ResourceToolBar extends JackToolBar implements CurrentFocusListener, DbRefreshListener {
    public static final String KEY = "ToolbarResource"; // NOT LOCALIZABLE
    private ToolButtonGroup toolGroup;
    private DefaultLinkUnlinkListPanel listpanel;
    private DbObject lastModel; // retains last model to limit list refresh

    public ResourceToolBar(ArrayList tools, ArrayList components, ToolButtonGroup toolGroup) {
        super();
        SMSActionsStore actionsStore = SMSActionsStore.getSingleton();
        setName(LocaleMgr.misc.getString("ResourceToolBarName"));
        listpanel = new DefaultLinkUnlinkListPanel();
        add(listpanel);
        this.toolGroup = toolGroup;

        Iterator iter = components.iterator();
        while (iter.hasNext()) {
            listpanel.addToolComponent((JComponent) iter.next());
        }

        iter = tools.iterator();
        while (iter.hasNext()) {
            Tool tool = (Tool) iter.next();
            if (!(tool instanceof LinkUnlinkTool))
                continue;
            ((LinkUnlinkTool) tool).setListComponent(listpanel.getJList());
        }

        install();
    }

    public void setVisible(boolean visible) {
        if (visible) {
            install();
            updateList();
        } else
            uninstall();

        super.setVisible(visible);

        // prevent invisible tool selection
        if (!visible) {
            JInternalFrame[] diagrams = ApplicationContext.getDefaultMainFrame()
                    .getDiagramInternalFrames();
            if (diagrams == null)
                return;
            for (int i = 0; i < diagrams.length; i++) {
                if (diagrams[i] instanceof DiagramInternalFrame) {
                    DiagramInternalFrame diagframe = (DiagramInternalFrame) diagrams[i];
                    DiagramView view = diagframe.getDiagram().getMainView();
                    toolGroup.setSelectedTool(view, 0);
                    toolGroup.setMasterTool(view, 0);
                }
            }
        }
    }

    private void install() {
        ApplicationContext.getFocusManager().addCurrentFocusListener(this);
        MetaField.addDbRefreshListener(this, null, new MetaField[] { DbObject.fComponents,
                DbSemanticalObject.fName });
    }

    private void uninstall() {
        ApplicationContext.getFocusManager().removeCurrentFocusListener(this);
        MetaField.removeDbRefreshListener(this, new MetaField[] { DbObject.fComponents,
                DbSemanticalObject.fName });
    }

    public int getFeatureSet() {
        return SMSFilter.BPM;
    }

    public void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (event.metaField == DbObject.fComponents) {
            if (!(event.neighbor instanceof DbBEResource))
                return;
            DbObject model = event.neighbor.getCompositeOfType(DbBEModel.metaClass);
            if (model != null && model == lastModel)
                loadListData(model);
        } else if (event.metaField == DbSemanticalObject.fName) {
            if (!(event.dbo instanceof DbBEResource))
                return;
            DbObject model = event.dbo.getCompositeOfType(DbBEModel.metaClass);
            if (model != null && model == lastModel)
                loadListData(model);
        }
    }

    public void currentFocusChanged(Object oldFocusObject, Object focusObject) throws DbException {
        if (focusObject instanceof ApplicationDiagram) {
            if (oldFocusObject != focusObject) {
                DbObject process = ((ApplicationDiagram) focusObject).getSemanticalObject();
                DbObject model = process.getCompositeOfType(DbBEModel.metaClass);
                if (model != null && model != lastModel) {
                    loadListData(model);
                    lastModel = model;
                }
            }
        }
    }

    private void updateList() {
        Object focus = ApplicationContext.getFocusManager().getFocusObject();
        if (!(focus instanceof ApplicationDiagram))
            return;
        ApplicationDiagram diag = ((ApplicationDiagram) focus);
        DbObject semobj = diag.getSemanticalObject();
        if (semobj == null)
            return;
        try {
            semobj.getDb().beginReadTrans();
            loadListData(semobj);
            semobj.getDb().commitTrans();
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(this, e);
        }
    }

    private void loadListData(DbObject dbo) throws DbException {
        JList list = listpanel.getJList();
        // backup selection
        Object[] backupSelection = list.getSelectedValues();

        list.removeAll();
        ArrayList listItems = new ArrayList();
        DbObject model = (dbo instanceof DbBEModel ? dbo : dbo
                .getCompositeOfType(DbBEModel.metaClass));
        if (model != null) {
            DbEnumeration dbEnum = model.componentTree(DbBEResource.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbBEResource resource = (DbBEResource) dbEnum.nextElement();
                listItems.add(new DefaultComparableElement(resource, ApplicationContext
                        .getSemanticalModel().getDisplayText(resource, DbObject.SHORT_FORM, model,
                                null), null));
            }
            dbEnum.close();
        }
        Collections.sort(listItems);
        list.setListData(listItems.toArray());

        // restore selection
        if (backupSelection == null || backupSelection.length == 0)
            return;
        Object[] values = listItems.toArray();
        for (int i = 0; i < values.length; i++) {
            Object obj = ((DefaultComparableElement) values[i]).object;
            boolean objSelected = false;
            for (int j = 0; !objSelected && j < backupSelection.length; j++) {
                Object oldobj = ((DefaultComparableElement) backupSelection[j]).object;
                if (oldobj == obj)
                    objSelected = true;
            }
            if (objSelected)
                list.addSelectionInterval(i, i);
        }
    }

}
