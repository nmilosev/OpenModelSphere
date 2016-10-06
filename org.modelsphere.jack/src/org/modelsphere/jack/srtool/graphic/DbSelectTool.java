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

import java.awt.*;
import java.util.HashSet;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.graphic.*;
import org.modelsphere.jack.graphic.tool.SelectTool;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class DbSelectTool extends SelectTool {

    public DbSelectTool(int userId, String text, Image icon) {
        super(userId, text, icon);
    }

    /*
     * for 2.1 protected boolean isCellCreationAllowed(int x, int y){ return false; }
     * 
     * protected Rectangle getMaxCellCreationRectangle(int x, int y){ return null; }
     * 
     * protected void doCellCreation(GraphicComponent gc, int xPressed, int yPressed, Polygon line){
     * }
     */
    protected final boolean isMergeAllowed(int x, int y) {
        GraphicComponent[] selComps = model.getSelectedComponents();
        if (selComps.length != 1)
            return false;
        if (selComps[0].getLayer() != Diagram.LAYER_GRAPHIC)
            return false;
        GraphicComponent gc = model.graphicAt(view, x, y, 1 << Diagram.LAYER_GRAPHIC, false);
        if (selComps[0] == gc)
            return false;
        if (!(selComps[0] instanceof GraphicNode))
            return false;
        if (!((gc instanceof GraphicNode) && (gc instanceof ActionInformation)))
            gc = null;
        if (gc != null) {
            if (((ActionInformation) gc).getSemanticalObject() == ((ActionInformation) selComps[0])
                    .getSemanticalObject())
                return true;
        }
        return false;
    } // end isMergeAllowed()

    protected final void doMerge(int x, int y) {
        GraphicComponent[] selComps = model.getSelectedComponents();
        GraphicNode sourceNode = (GraphicNode) selComps[0];
        DbObject sourceGo = ((ActionInformation) sourceNode).getGraphicalObject();
        GraphicNode destNode = (GraphicNode) model.graphicAt(view, x, y,
                1 << Diagram.LAYER_GRAPHIC, false);
        try {
            Db db = ((ApplicationDiagram) model).getDiagramGO().getDb();
            db.beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("mergeDuplicate"));
            Rectangle sourceRect = (Rectangle) sourceGo.get(DbGraphic.fGraphicalObjectRectangle);
            Line[] lines = sourceNode.getLines();
            HashSet proceedLines = new HashSet();
            // reconnect each line to destination go
            for (int i = 0; i < lines.length; i++) {
                Line line = lines[i];
                DbObject lineGo = ((ActionInformation) line).getGraphicalObject();
                Polygon poly = (Polygon) lineGo.get(DbGraphic.fLineGoPolyline);

                if ((!proceedLines.contains(line)) && (line.getNode1() == sourceNode)
                        && (line.getNode2() == sourceNode)) {
                    int dx = (destNode.getRectangle().x + destNode.getRectangle().width / 2)
                            - (sourceRect.x + sourceNode.getRectangle().width / 2);
                    int dy = (destNode.getRectangle().y + destNode.getRectangle().height / 2)
                            - (sourceRect.y + sourceNode.getRectangle().height / 2);
                    poly.translate(dx, dy);
                }
                int[] xs = poly.xpoints;
                int[] ys = poly.ypoints;

                if (line.getNode1() == sourceNode) {
                    xs[0] = destNode.getRectangle().x + destNode.getRectangle().width / 2;
                    ys[0] = destNode.getRectangle().y + destNode.getRectangle().height / 2;
                    lineGo.set(DbGraphic.fLineGoFrontEndGo, ((ActionInformation) destNode)
                            .getGraphicalObject());
                }
                if (line.getNode2() == sourceNode) {
                    xs[xs.length - 1] = destNode.getRectangle().x + destNode.getRectangle().width
                            / 2;
                    ys[ys.length - 1] = destNode.getRectangle().y + destNode.getRectangle().height
                            / 2;
                    lineGo.set(DbGraphic.fLineGoBackEndGo, ((ActionInformation) destNode)
                            .getGraphicalObject());
                }
                poly = new Polygon(xs, ys, poly.npoints);
                if (line.isRightAngle())
                    poly = convertToRightAnglePolygon(poly);
                lineGo.set(DbGraphic.fLineGoPolyline, poly);
                proceedLines.add(line);
            }
            ((ActionInformation) sourceNode).getGraphicalObject().remove();

            db.commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    } // end doMerge()

    private final Polygon convertToRightAnglePolygon(Polygon poly) {
        int i = 1;
        int nb = poly.npoints;
        poly.addPoint(poly.xpoints[nb - 1], poly.ypoints[nb - 1]);
        if (Line.startHorz(poly)) {
            poly.ypoints[1] = poly.ypoints[0];
            i++;
        }
        while (true) {
            if (i == nb)
                break;
            poly.xpoints[i] = poly.xpoints[i - 1];
            i++;
            if (i == nb)
                break;
            poly.ypoints[i] = poly.ypoints[i - 1];
            i++;
        }
        if (nb > 2 && poly.xpoints[nb - 1] == poly.xpoints[nb]
                && poly.ypoints[nb - 1] == poly.ypoints[nb])
            poly.npoints--;
        return poly;
    } // end convertToRightAnglePolygon()

    protected final void translateComponents(GraphicComponent[] gcs, int dx, int dy) {
        try {
            DbObject diagramGo = ((ApplicationDiagram) model).getDiagramGO();
            Db db = ((ApplicationDiagram) model).getDiagramGO().getDb();
            db.beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("graphicMove"));
            translate(gcs, dx, dy);
            translateComponentsAfter(diagramGo, gcs, dx, dy);
            ApplicationDiagram.lockGridAlignment = true;
            db.commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
            ApplicationDiagram.lockGridAlignment = true;
        } // end try
    } // end translateComponents()

    public static final void translate(GraphicComponent[] gcs, int dx, int dy) throws DbException {

        for (int i = 0; i < gcs.length; i++) {
            if (gcs[i] instanceof Line) { // only floating lines may appear in
                // <gcs>.
                Polygon poly = ((Line) gcs[i]).getPoly();
                poly = new Polygon(poly.xpoints, poly.ypoints, poly.npoints);
                poly.translate(dx, dy);
                DbObject lineGo = ((ActionInformation) gcs[i]).getGraphicalObject();
                lineGo.set((gcs[i] instanceof FreeLine ? DbGraphic.fFreeLineGoPolyline
                        : DbGraphic.fLineGoPolyline), poly);
            } else if (gcs[i] instanceof SrLineLabel) {
                ((SrLineLabel) gcs[i]).translateLabel(dx, dy);
            } else if (gcs[i] instanceof SrAttachment) {
                ((SrAttachment) gcs[i]).translate(dx, dy);
            } else {
                DbObject go = ((ActionInformation) gcs[i]).getGraphicalObject();
                Rectangle rect = new Rectangle(gcs[i].getRectangle());
                rect.translate(dx, dy);

                /*
                 * if(grid.isActive() && gcs.length == 1){ ApplicationDiagram.lockGridAlignment =
                 * false; Point pt = grid.getClosestGridBoundary(new Point(rect.x, rect.y)); rect.x
                 * = pt.x; rect.y = pt.y; }
                 */

                // go.getDb().beginWriteTrans("");
                go.set(DbGraphic.fGraphicalObjectRectangle, rect);
                // go.getDb().commitTrans();
            }
        }

    }

    // By default, do nothing
    protected void translateComponentsAfter(DbObject diagramGo, GraphicComponent[] gcs, int dx,
            int dy) throws DbException {
    }

    protected final void resizeComponent(GraphicComponent gc, Rectangle rect) {
        try {
            DbObject go = ((ActionInformation) gc).getGraphicalObject();
            go.getDb().beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("graphicResize"));
            if (gc instanceof SrLineLabel)
                ((SrLineLabel) gc).resizeLabel(rect.width);
            else if (gc instanceof SrAttachment)
                ((SrAttachment) gc).resize(rect.width);
            else
                go.set(DbGraphic.fGraphicalObjectRectangle, rect);
            go.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }

    protected final void setLinePoly(Line line, Polygon poly) {
        try {
            DbObject lineGo = ((ActionInformation) line).getGraphicalObject();
            lineGo.getDb().beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("lineReshape"));
            if (lineGo.hasField(DbGraphic.fLineGoPolyline))
                lineGo.set(DbGraphic.fLineGoPolyline, poly);
            else
                lineGo.set(DbGraphic.fFreeLineGoPolyline, poly);

            lineGo.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }

    // Reconnexion of a LineGo to a graphic duplicate of the same semantical
    // object
    // Overridden for BPM
    protected void setLineNode(Line line, GraphicNode node, Polygon poly, int where) {
        if (node == null)
            return;
        GraphicNode oldNode = (where == Line.AT_END1 ? line.getNode1() : line.getNode2());
        if (oldNode != null)
            if (((ActionInformation) oldNode).getSemanticalObject() != ((ActionInformation) node)
                    .getSemanticalObject())
                return;
        try {
            DbObject lineGo = ((ActionInformation) line).getGraphicalObject();
            DbObject nodeGo = ((ActionInformation) node).getGraphicalObject();
            lineGo.getDb().beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("lineReshape"));
            lineGo.set((where == Line.AT_END1 ? DbGraphic.fLineGoFrontEndGo
                    : DbGraphic.fLineGoBackEndGo), nodeGo);
            lineGo.set(DbGraphic.fLineGoPolyline, poly);
            lineGo.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }
}
