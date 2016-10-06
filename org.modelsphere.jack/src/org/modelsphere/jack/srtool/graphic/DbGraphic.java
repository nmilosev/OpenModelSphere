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
import java.awt.print.PageFormat;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.event.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.graphic.*;
import org.modelsphere.jack.srtool.ApplicationContext;

public abstract class DbGraphic {

    public static MetaRelation1 fProjectDefaultStyle;
    public static MetaField fStyleName;
    public static MetaField fDiagramName;
    public static MetaField fDiagramPageFormat;
    public static MetaField fDiagramPrintScale;
    public static MetaField fDiagramNbPages;
    public static MetaRelation1 fDiagramStyle;
    public static MetaField fDiagramPageNoFont;
    public static MetaField fDiagramPageNoPsition;
    public static MetaField fGraphicalObjectRectangle;
    public static MetaField fGraphicalObjectAutoFit;
    public static MetaRelationN fGraphicalObjectFrontEndLineGos;
    public static MetaRelationN fGraphicalObjectBackEndLineGos;
    public static MetaRelation1 fGraphicalObjectStyle;
    public static MetaField fLineGoPolyline;
    public static MetaField fLineGoRightAngle;
    public static MetaRelation1 fLineGoFrontEndGo;
    public static MetaRelation1 fLineGoBackEndGo;
    public static MetaField fFreeLineGoPolyline;
    public static MetaField fFreeLineGoRightAngle;
    public static MetaField fUserTextGoText;
    public static MetaField fUserTextGoFont;
    public static MetaField fUserTextGoTextColor;
    public static MetaField fUserTextGoFillColor;
    public static MetaField fImageGoDiagramImage;
    public static MetaField fImageGoOpacityFactor;
    public static MetaField fStampGoCompanyLogo;
    public static MetaField fFreeGraphicGoDashStyle;
    public static MetaField fFreeGraphicGoHighlight;
    public static MetaField fFreeGraphicGoLineColor;
    public static MetaField fFreeFormGoBackgroundColor;
    public static MetaField fDbDiagramName;

    public static void installTriggers() {
        // Enforce positioning constraints in diagrams
        MetaField.addDbUpdateListener(new DiagramLayoutListener(), 0, new MetaField[] {
                DbObject.fComposite, fGraphicalObjectRectangle, fLineGoPolyline,
                fFreeLineGoPolyline });

        MetaField.addDbRefreshListener(new GraphicRefreshListener(), null, new MetaField[] {
                fGraphicalObjectRectangle, fGraphicalObjectAutoFit, fLineGoPolyline,
                fLineGoRightAngle, fLineGoFrontEndGo, fLineGoBackEndGo, fFreeLineGoPolyline,
                fFreeLineGoRightAngle });
    }

    // If autofit and diagram opened, take the size of the rectangle from the diagram
    // (but take the position from DbGraphicalObject, the diagram may not be yet updated).
    public static Rectangle getRectangle(DbObject go) throws DbException {
        Rectangle rect = (Rectangle) go.get(fGraphicalObjectRectangle);
        if (((Boolean) go.get(fGraphicalObjectAutoFit)).booleanValue()) {
            GraphicComponent gc = (GraphicComponent) ((DbGraphicalObjectI) go).getGraphicPeer();
            if (gc != null) {
                Rectangle rect2 = gc.getRectangle();
                rect = GraphicUtil.rectangleResize(rect, rect2.width, rect2.height);
                /*
                 * if(!ApplicationDiagram.lockGridAlignment || Grid.getGrid().isActive()){ int x =
                 * rect.x; int y = rect.y; Point pt = Grid.getGrid().getClosestGridBoundary(new
                 * Point(x, y));
                 * 
                 * //if the values changed,we need to compensate for the offset if(pt.x != x || pt.y
                 * != y){ rect.x = pt.x; rect.y = pt.y; } }
                 */
            }
        }
        return rect;
    }

    public static Dimension getPageSize(DbObject diagram) throws DbException {
        PageFormat pageFormat = (PageFormat) diagram.get(fDiagramPageFormat);
        int scale = ((Integer) diagram.get(fDiagramPrintScale)).intValue();
        return Diagram.getPageSize(pageFormat, scale);
    }

    // Create the straightened polyline for a LineGo.
    // The line must be linked to a node on both ends.
    public static void createPolyline(DbObject lineGo) throws DbException {
        createPolyline(lineGo, false);
    }

    // Create the straightened polyline for a LineGo.
    // The line must be linked to a node on both ends.
    public static void createPolyline(DbObject lineGo, boolean fromStraightenAction)
            throws DbException {
        DbObject go1 = (DbObject) lineGo.get(fLineGoFrontEndGo);
        DbObject go2 = (DbObject) lineGo.get(fLineGoBackEndGo);
        boolean rightAngle = ((Boolean) lineGo.get(fLineGoRightAngle)).booleanValue();
        Polygon poly = new Polygon();
        if (go1 == null || go2 == null) {
            if (fromStraightenAction) {
                Polygon oldPoly = (Polygon) lineGo.get(fLineGoPolyline);
                if (rightAngle)
                    poly = new Polygon(new int[] { oldPoly.xpoints[0], oldPoly.xpoints[0],
                            oldPoly.xpoints[oldPoly.npoints - 1] }, new int[] { oldPoly.ypoints[0],
                            oldPoly.ypoints[oldPoly.npoints - 1],
                            oldPoly.ypoints[oldPoly.npoints - 1] }, 3);
                else
                    poly = new Polygon(new int[] { oldPoly.xpoints[0],
                            oldPoly.xpoints[oldPoly.npoints - 1] }, new int[] { oldPoly.ypoints[0],
                            oldPoly.ypoints[oldPoly.npoints - 1] }, 2);

                lineGo.set(fLineGoPolyline, poly);
                return;
            }
            if (go1 == null && go2 == null) {
                return; // use the default polygon
            }
            Rectangle rect;
            if (go1 == null) {
                rect = getRectangle(go2);
                poly = new Polygon(new int[] { rect.x + rect.width / 2 - Math.max(200, rect.width),
                        rect.x + rect.width / 2 }, new int[] { rect.y + rect.height / 2,
                        rect.y + rect.height / 2 }, 2);
            } else {
                rect = getRectangle(go1);
                poly = new Polygon(new int[] { rect.x + rect.width / 2,
                        rect.x + rect.width / 2 + Math.max(200, rect.width) }, new int[] {
                        rect.y + rect.height / 2, rect.y + rect.height / 2 }, 2);
            }
        } else {
            Rectangle rect = getRectangle(go1);
            poly.addPoint(rect.x + rect.width / 2, rect.y + rect.height / 2);
            if (go1 == go2) {
                rect = GraphicUtil.rectangleResize(rect, Math.max(200, rect.width), rect.height);
                if (rightAngle) {
                    poly.addPoint(poly.xpoints[0], rect.y - 20);
                    poly.addPoint(rect.x + rect.width + 20, rect.y - 20);
                    poly.addPoint(rect.x + rect.width + 20, poly.ypoints[0]);
                } else {
                    poly.addPoint(rect.x + rect.width + 20, rect.y - 20);
                    poly.addPoint(rect.x + rect.width + 20, rect.y + rect.height + 20);
                }
                poly.addPoint(poly.xpoints[0], poly.ypoints[0]);
            } else {
                rect = getRectangle(go2);
                poly.addPoint(rect.x + rect.width / 2, rect.y + rect.height / 2);
                if (rightAngle) {
                    poly.addPoint(poly.xpoints[1], poly.ypoints[1]);
                    poly.xpoints[1] = poly.xpoints[0];
                }
            }
        }
        lineGo.set(fLineGoPolyline, poly);
    }

    // Return the first graph. rep. of <semObj> in the diagram <diag>;
    // if more than one graph rep. and one is selected, return the selected one.
    public static DbObject getFirstGraphicalObject(DbObject diag, DbObject semObj)
            throws DbException {
        return getFirstGraphicalObject(diag, semObj, null);
    }

    public static DbObject getFirstGraphicalObject(DbObject diag, DbObject semObj, DbObject goExcl)
            throws DbException {
        return getFirstGraphicalObject(diag, semObj, goExcl, null);
    }

    public static DbObject getFirstGraphicalObject(DbObject diag, DbObject semObj, DbObject goExcl,
            MetaClass mc) throws DbException {
        DbRelationN gos = ApplicationContext.getSemanticalModel().getGos(semObj);
        if (gos == null)
            return null;
        DbObject goFound = null;
        DbEnumeration dbEnum = gos.elements();
        while (dbEnum.hasMoreElements()) {
            DbObject go = dbEnum.nextElement();
            if (go == goExcl)
                continue;
            if (mc != null && !(mc.isAssignableFrom(go.getMetaClass())))
                continue;
            if (go.getComposite() == diag) {
                GraphicComponent gc = (GraphicComponent) ((DbGraphicalObjectI) go).getGraphicPeer();
                if (gc != null && gc.isSelected()) {
                    goFound = go;
                    break;
                }
                if (goFound == null)
                    goFound = go;
            }
        }
        dbEnum.close();
        return goFound;
    }

    private static class GraphicRefreshListener implements DbRefreshListener {
        GraphicRefreshListener() {
        }

        public void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
            GraphicComponent gc = (GraphicComponent) ((DbGraphicalObjectI) event.dbo)
                    .getGraphicPeer();
            if (gc == null)
                return;
            Object data = event.dbo.get(event.metaField);
            if (gc instanceof Line) {
                Line line = (Line) gc;
                if (event.metaField == fLineGoPolyline || event.metaField == fFreeLineGoPolyline)
                    line.setPoly((Polygon) data);
                else if (event.metaField == fLineGoRightAngle
                        || event.metaField == fFreeLineGoRightAngle)
                    line.setRightAngle(((Boolean) data).booleanValue());
                else if (event.metaField == fLineGoFrontEndGo)
                    line.setNode1(data == null ? null : (GraphicNode) ((DbGraphicalObjectI) data)
                            .getGraphicPeer());
                else if (event.metaField == fLineGoBackEndGo)
                    line.setNode2(data == null ? null : (GraphicNode) ((DbGraphicalObjectI) data)
                            .getGraphicPeer());
            } else {
                if (event.metaField == fGraphicalObjectRectangle)
                    gc.setRectangle((Rectangle) data);
                else if (event.metaField == fGraphicalObjectAutoFit)
                    gc.setAutoFit(((Boolean) data).booleanValue());
            }
        }
    }

    // Enforce positioning constraints in diagram
    private static class DiagramLayoutListener implements DbUpdateListener {
        DiagramLayoutListener() {
        }

        public void dbUpdated(DbUpdateEvent event) throws DbException {
            if (event.metaField == DbObject.fComposite) {
                if (event.dbo.getTransStatus() == Db.OBJ_ADDED
                        && event.dbo.hasField(fGraphicalObjectRectangle))
                    goMoved(event.dbo);
            } else if (event.metaField == fGraphicalObjectRectangle) {
                if (!(event.dbo.hasField(fLineGoPolyline))
                        && !(event.dbo.hasField(fFreeLineGoPolyline)))
                    goMoved(event.dbo);
            } else if (event.metaField == fLineGoPolyline || event.metaField == fFreeLineGoPolyline)
                goMoved(event.dbo);
        }
    }

    private static void goMoved(DbObject go) throws DbException {
        DbObject diagram = go.getComposite();
        Dimension pageSize = getPageSize(diagram);
        Dimension nbPages = (Dimension) diagram.get(fDiagramNbPages);
        Rectangle drawingArea = new Rectangle(0, 0, pageSize.width * nbPages.width, pageSize.height
                * nbPages.height);
        if (go.hasField(fLineGoPolyline)) {
            Polygon poly = (Polygon) go.get(fLineGoPolyline);
            if (GraphicUtil.confineToRect(poly, drawingArea))
                go.set(fLineGoPolyline, poly);
        } else if (go.hasField(fFreeLineGoPolyline)) {
            Polygon poly = (Polygon) go.get(fFreeLineGoPolyline);
            if (GraphicUtil.confineToRect(poly, drawingArea))
                go.set(fFreeLineGoPolyline, poly);
        } else {
            Rectangle rect = (Rectangle) go.get(fGraphicalObjectRectangle);
            if (rect != null) {
                if (GraphicUtil.confineCenterToRect(rect, drawingArea))
                    go.set(fGraphicalObjectRectangle, rect);

                moveLines(go, rect, drawingArea, true);
                moveLines(go, rect, drawingArea, false);
            }
        } // end if
    } // end goMoved()

    /*
     * If a box is moved, change the connecting point of each line connected to the box. If both
     * boxes connected by a line are moved, move all the points of the line (not only the end
     * points).
     */
    private static void moveLines(DbObject go, Rectangle rect, Rectangle drawingArea, boolean front)
            throws DbException {
        Point center = GraphicUtil.rectangleGetCenter(rect);
        DbRelationN lines = (DbRelationN) go.get(front ? fGraphicalObjectFrontEndLineGos
                : fGraphicalObjectBackEndLineGos);
        for (int i = lines.size(); --i >= 0;) {
            DbObject lineGo = lines.elementAt(i);
            Polygon poly = (Polygon) lineGo.get(fLineGoPolyline);
            int ind = (front ? 0 : poly.npoints - 1);
            if (center.x == poly.xpoints[ind] && center.y == poly.ypoints[ind])
                continue; // line already moved by the processing of the
            // opposite box
            DbObject oppGo = (DbObject) lineGo.get(front ? fLineGoBackEndGo : fLineGoFrontEndGo);
            if (oppGo == null) {
                poly.translate(center.x - poly.xpoints[ind], center.y - poly.ypoints[ind]);
            } else if (oppGo.hasChanged(fGraphicalObjectRectangle)) {
                // both boxes moved, move all the points of the line
                poly.translate(center.x - poly.xpoints[ind], center.y - poly.ypoints[ind]);
                Rectangle oppRect = (Rectangle) oppGo.get(fGraphicalObjectRectangle); // be sure to center
                // the opposite end
                // point within its
                // box
                if (GraphicUtil.confineCenterToRect(oppRect, drawingArea))
                    oppGo.set(fGraphicalObjectRectangle, oppRect);
                Point oppCenter = GraphicUtil.rectangleGetCenter(oppRect);
                moveLineEnd(oppGo, lineGo, oppCenter, poly, !front);
            } else {
                moveLineEnd(go, lineGo, center, poly, front);
            }
            GraphicUtil.confineToRect(poly, drawingArea);
            lineGo.set(fLineGoPolyline, poly);
        }
    }

    // Set the end point of the line to the center of the node.
    // If right-angle line, translate all the points inside the node, and
    // constrain the
    // first point going outside the node to the previous point.
    private static void moveLineEnd(DbObject go, DbObject lineGo, Point center, Polygon poly,
            boolean front) throws DbException {
        boolean startHorz = Line.startHorz(poly); // must be called before
        // modifying poly.
        int i = (front ? 0 : poly.npoints - 1);
        int dx = center.x - poly.xpoints[i];
        int dy = center.y - poly.ypoints[i];
        if (dx == 0 && dy == 0)
            return;
        poly.xpoints[i] += dx;
        poly.ypoints[i] += dy;
        if (!((Boolean) lineGo.get(fLineGoRightAngle)).booleanValue() || poly.npoints < 3)
            return;
        // Get the rectangle from the graphic peer, which is at the old position
        // with the correct size
        // (the size is not correct in DbGraphicalObject if auto fit).
        // Translate all the points up to (excluding) the first point outside
        // the node,
        // or the previous-to-last point.
        GraphicComponent gc = (GraphicComponent) ((DbGraphicalObjectI) go).getGraphicPeer();
        Line line = (Line) ((DbGraphicalObjectI) lineGo).getGraphicPeer();
        if (gc != null && line != null && line.getPoly().npoints == poly.npoints) {
            Rectangle oldRect = gc.getRectangle();
            Polygon oldPoly = line.getPoly();
            while (true) {
                int i2;
                if (front) {
                    i2 = i + 1;
                    if (i2 == poly.npoints - 2)
                        break;
                } else {
                    i2 = i - 1;
                    if (i2 == 1)
                        break;
                }
                if (!oldRect.contains(oldPoly.xpoints[i2], oldPoly.ypoints[i2]))
                    break;
                i = i2;
                poly.xpoints[i] += dx;
                poly.ypoints[i] += dy;
            }
        }
        // Constrain the point following the last translated (cannot be an end
        // point).
        if (front) {
            if ((i & 1) == 0 ? startHorz : !startHorz)
                poly.ypoints[i + 1] = poly.ypoints[i];
            else
                poly.xpoints[i + 1] = poly.xpoints[i];
        } else {
            if ((i & 1) == 1 ? startHorz : !startHorz)
                poly.ypoints[i - 1] = poly.ypoints[i];
            else
                poly.xpoints[i - 1] = poly.xpoints[i];
        }
    }
}
