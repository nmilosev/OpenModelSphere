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

package org.modelsphere.jack.srtool.graphic;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.util.HashSet;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.awt.JackPopupMenu;
import org.modelsphere.jack.awt.choosers.PageTitlePanel;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbGraphicalObjectI;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.db.srtypes.PageNoPosition;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.Grid;
import org.modelsphere.jack.graphic.ZoneBox;
import org.modelsphere.jack.graphic.tool.ToolButtonGroup;
import org.modelsphere.jack.graphic.zone.CellID;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class ApplicationDiagram extends Diagram implements DbRefreshListener {

    public static final String PAGE_TITLE = "diagram.pageTitle"; // NOT LOCALIZABLE, property
    public static final String PAGE_TITLE_DEFAULT = "{1},{2}";

    public static boolean lockGridAlignment = true;

    /**
     * @return Returns the diagGO.
     */
    protected DbSemanticalObject semObj;
    protected DbObject diagGO;
    protected DbProject project;
    protected ApplDiagramView mainView;
    private GraphicComponentFactory gcFactory;

    public DbSemanticalObject getSemObj() {
        return semObj;
    }

    public ApplicationDiagram(DbSemanticalObject semObj, DbObject diagGO,
            GraphicComponentFactory gcFactory, ToolButtonGroup toolGroup) throws DbException {
        this.semObj = semObj;
        this.diagGO = diagGO;
        this.gcFactory = gcFactory;
        project = diagGO.getProject();
        addObjectTriggers();
        populateContent();
        mainView = new ApplDiagramView(this);
        mainView.setToolGroup(toolGroup);
    }

    private void addObjectTriggers() {
        diagGO.addDbRefreshListener(this);
    }

    private void removeObjectTriggers() {
        diagGO.removeDbRefreshListener(this);
    }

    public final void delete() {
        removeObjectTriggers();
        super.delete();
    }

    public final DbSemanticalObject getSemanticalObject() {
        return semObj;
    }

    public final DbObject getDiagramGO() {
        return diagGO;
    }

    public final DbProject getProject() {
        return project;
    }

    public final DiagramView getMainView() {
        return mainView;
    }

    public final DiagramInternalFrame getDiagramInternalFrame() {
        return (DiagramInternalFrame) SwingUtilities.getAncestorOfClass(DiagramInternalFrame.class,
                mainView);
    }

    public final Object[] getSelectedObjects() {
        GraphicComponent[] selComps = getSelectedComponents();
        if (selComps.length == 0)
            return new Object[0];
        HashSet selObjs = new HashSet();
        for (int i = 0; i < selComps.length; i++) {
            GraphicComponent gc = selComps[i];
            if (gc.selectAtCellLevel()) {
                CellID[] cellIds = ((ZoneBox) gc).getSelectedCells();
                if (cellIds.length == 0)
                    selObjs.add(gc);
                else {
                    for (int j = 0; j < cellIds.length; j++) {
                        Object obj = ((ZoneBox) gc).getCell(cellIds[j]).getObject();
                        selObjs.add(obj == ((ActionInformation) gc).getSemanticalObject() ? gc
                                : obj);
                    }
                }
            } else
                selObjs.add(gc);
        }
        return selObjs.toArray();
    }

    public final void populateContent() throws DbException {
        try {
            removeAll();
            setDrawingArea();
            setPageNoParams();
            if (diagGO.getDb().getTransMode() != Db.TRANS_REFRESH)
                beginComputePos();
            DbEnumeration dbEnum = diagGO.getComponents().elements();
            while (dbEnum.hasMoreElements()) {
                DbObject obj = dbEnum.nextElement();
                createComponent(obj);
            }
            dbEnum.close();
            if (diagGO.getDb().getTransMode() != Db.TRANS_REFRESH) {
                Graphics g = ApplicationContext.getDefaultMainFrame().getGraphics();
                endComputePos(g);
                g.dispose();
            }
        } catch (DbException ex) {
            throw ex;
        }
    }

    public final void fireSelectionChanged() {
        super.fireSelectionChanged();
        ApplicationContext.getFocusManager().selectionChanged(this);
    }

    public final JPopupMenu getPopupMenu(Point ptClicked, GraphicComponent gcClicked) {
        JackPopupMenu popup = ApplicationContext.getApplPopupMenu().getPopupMenu(gcClicked == null);
        if (popup != null) {
            popup.setDiagramLocation(ptClicked, gcClicked);
        }

        return popup;
    }

    public final void editCell(ZoneBox box, CellID cellID) {
        // If the cell to be edited is outside the box,
        // increase the height of the box to make the cell visible.
        if (cellID == null)
            return;
        Rectangle rect = box.getCellRect(cellID);
        int heightIncr = rect.y + rect.height;
        rect = box.getContentRect();
        heightIncr -= rect.y + rect.height;
        if (heightIncr > 0) {
            rect = new Rectangle(box.getRectangle());
            rect.y -= heightIncr / 2;
            rect.height += heightIncr;
            DbObject go = ((ActionInformation) box).getGraphicalObject();
            try {
                go.getDb().beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("fit"));
                go.set(DbGraphic.fGraphicalObjectRectangle, rect);
                go.getDb().commitTrans();
            } catch (Exception e) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(mainView, e);
                return;
            }
        }
        setEditor(mainView, box, cellID);
    }

    // ////////////////////////////////////////////
    // DbRefreshListener SUPPORT
    //
    // Overridden
    public void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (event.metaField == DbObject.fComponents) {
            if (event.op == Db.ADD_TO_RELN) {
                createComponent(event.neighbor);
            } else if (event.op == Db.REMOVE_FROM_RELN) {
                GraphicComponent gc = (GraphicComponent) ((DbGraphicalObjectI) event.neighbor)
                        .getGraphicPeer();
                if (gc != null)
                    gc.delete(false);
            }
        } else if (event.metaField == DbGraphic.fDiagramNbPages
                || event.metaField == DbGraphic.fDiagramPageFormat
                || event.metaField == DbGraphic.fDiagramPrintScale) {
            setDrawingArea();
        } else if (event.metaField == DbGraphic.fDiagramStyle) {
            populateContent();
        } else if (event.metaField == DbGraphic.fDiagramPageNoFont
                || event.metaField == DbGraphic.fDiagramPageNoPsition) {
            setPageNoParams();
        }

    }

    //
    // End of DbRefreshListener SUPPORT
    // ///////////////////////////////////////////

    // called from Diagram
    protected String getStringPageNo(int row, int col, Dimension nbPages) {
        // get pattern
        PropertiesSet options = PropertiesManager.APPLICATION_PROPERTIES_SET;
        String pattern = options.getPropertyString(ApplicationDiagram.class,
                ApplicationDiagram.PAGE_TITLE, ApplicationDiagram.PAGE_TITLE_DEFAULT);

        // get diagram and project names
        String diagramName, projectName;

        try {

            boolean bManageTrans = false;
            Db db = diagGO.getDb();
            if (db.getTransMode() == Db.TRANS_NONE) {
                bManageTrans = true;
                db.beginReadTrans();
            }
            diagramName = diagGO.getName();
            DbProject proj = diagGO.getProject();
            projectName = proj.getName();

            if (bManageTrans)
                db.commitTrans();

        } catch (DbException ex) {
            diagramName = "";
            projectName = "";
        } // end try

        String str = PageTitlePanel.getPageTitle(pattern, row, col, nbPages, diagramName,
                projectName);
        return str;
    }

    //
    // private methods
    // 

    private void setDrawingArea() throws DbException {
        PageFormat pageFormat = (PageFormat) diagGO.get(DbGraphic.fDiagramPageFormat);
        int printScale = ((Integer) diagGO.get(DbGraphic.fDiagramPrintScale)).intValue();
        Dimension nbPages = (Dimension) diagGO.get(DbGraphic.fDiagramNbPages);
        setDrawingArea(pageFormat, printScale, nbPages);
    }

    private void setPageNoParams() throws DbException {
        PageNoPosition pnp = (PageNoPosition) diagGO.get(DbGraphic.fDiagramPageNoPsition);
        Font pageNoFont = (Font) diagGO.get(DbGraphic.fDiagramPageNoFont);
        if (pnp != null && pageNoFont != null)
            setPageNoParams(pnp.getValue(), pageNoFont);
    }

    private GraphicComponent createComponent(DbObject go) throws DbException {
        GraphicComponent gc = (GraphicComponent) ((DbGraphicalObjectI) go).getGraphicPeer();
        if (gc != null)
            return gc; // already created
        if (go.hasField(DbGraphic.fLineGoPolyline)) {
            DbObject frontGo = (DbObject) go.get(DbGraphic.fLineGoFrontEndGo);
            DbObject backGo = (DbObject) go.get(DbGraphic.fLineGoBackEndGo);
            GraphicNode node1 = (frontGo == null ? null : (GraphicNode) createComponent(frontGo));
            GraphicNode node2 = (backGo == null ? null : (GraphicNode) createComponent(backGo));
            if (gcFactory != null) {
                gc = gcFactory.createLine(this, go, node1, node2);
            } // end if
        } else {
            if (gcFactory != null) {
                gc = gcFactory.createGraphic(this, go);
            } // end if
        } // end if

        return gc;
    }
}
