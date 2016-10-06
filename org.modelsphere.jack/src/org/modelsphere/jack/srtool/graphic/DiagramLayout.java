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

import java.util.HashMap;
import java.awt.*;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.graphic.*;
import org.modelsphere.jack.util.SrVector;

public abstract class DiagramLayout {

    private static final int LINK_DELTA = (int) (Diagram.PIXELS_PER_MM * 8);

    private DbObject diagGo;
    private GraphicComponent[] selComps;
    private GraphLayout layout;
    private HashMap nodeMap = new HashMap();
    private SrVector recursiveLinks = new SrVector();
    private Point posArea;
    private Dimension dimArea;

    /*
     * If selComps is null, layout the whole diagram. Must be called in a write transaction.
     */
    protected DiagramLayout(DbObject diagGo, GraphicComponent[] selComps) throws DbException {
        this.diagGo = diagGo;
        this.selComps = selComps;
        layout = new GraphLayout();
        addNodes();
        addLinks();
        dimArea = layout.doLayout(posArea);
        moveNodes();
        moveLinks();
        moveRecursiveLinks();
        layoutRightAngleLinks();

        Dimension dimPage = DbGraphic.getPageSize(diagGo);
        Dimension nbPages = (Dimension) diagGo.get(DbGraphic.fDiagramNbPages);
        dimArea.width = dimArea.width / dimPage.width + 1;
        dimArea.height = dimArea.height / dimPage.height + 1;
        if (nbPages.width < dimArea.width)
            nbPages.width = dimArea.width;
        if (nbPages.height < dimArea.height)
            nbPages.height = dimArea.height;
        diagGo.set(DbGraphic.fDiagramNbPages, nbPages);

        layout.dispose();
    }

    private final void addNodes() throws DbException {
        if (selComps == null) {
            DbEnumeration dbEnum = diagGo.getComponents().elements();
            while (dbEnum.hasMoreElements()) {
                DbObject go = dbEnum.nextElement();
                if (!isNode(go))
                    continue;
                Rectangle rect = DbGraphic.getRectangle(go);
                GraphLayoutNode node = layout.addNode(rect, go);
                nodeMap.put(go, node);
            }
            dbEnum.close();
        } else {
            for (int i = 0; i < selComps.length; i++) {
                DbObject go = null;
                if (selComps[i] instanceof ActionInformation)
                    go = ((ActionInformation) selComps[i]).getGraphicalObject();
                if (go == null || !isNode(go) || nodeMap.containsKey(go))
                    continue;
                Rectangle rect = DbGraphic.getRectangle(go);
                GraphLayoutNode node = layout.addNode(rect, go);
                nodeMap.put(go, node);
                if (posArea == null)
                    posArea = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
                if (posArea.x > rect.x)
                    posArea.x = Math.max(0, rect.x);
                if (posArea.y > rect.y)
                    posArea.y = Math.max(0, rect.y);
            }
        }
    }

    /*
     * Add all the links connected to the selected nodes. For every selected node, scan all the
     * links <front end> and add the ones whose opposite node is selected.
     */
    private final void addLinks() throws DbException {
        SrVector nodes = layout.getNodes();
        int nb = nodes.size();
        for (int i = 0; i < nb; i++) {
            GraphLayoutNode frontNode = (GraphLayoutNode) nodes.elementAt(i);
            DbObject frontGo = (DbObject) frontNode.getUserNode();
            DbEnumeration dbEnum = ((DbRelationN) frontGo
                    .get(DbGraphic.fGraphicalObjectFrontEndLineGos)).elements();
            while (dbEnum.hasMoreElements()) {
                DbObject lineGo = dbEnum.nextElement();
                DbObject backGo = (DbObject) lineGo.get(DbGraphic.fLineGoBackEndGo);
                GraphLayoutNode backNode = (GraphLayoutNode) nodeMap.get(backGo);
                if (backNode == null)
                    continue; /* not selected */
                if (backNode == frontNode) {
                    recursiveLinks.addElement(lineGo); /*
                                                        * keep the list of recursive links
                                                        */
                    continue; /* we have to move them at the end. */
                }
                GraphLayoutLink link = layout.addLink(backNode, frontNode, lineGo);
                setLinkHierarchy(link, lineGo, frontGo, frontNode, backGo, backNode);
            }
            dbEnum.close();
        }
    }

    private final void moveNodes() throws DbException {
        SrVector nodes = layout.getNodes();
        int nb = nodes.size();
        for (int i = 0; i < nb; i++) {
            GraphLayoutNode node = (GraphLayoutNode) nodes.elementAt(i);
            DbObject go = (DbObject) node.getUserNode();
            go.set(DbGraphic.fGraphicalObjectRectangle, node.getRect());
        }
    }

    /*
     * Recalculate the point array of each link. If more than one link between two
     * DbGraphicalObject's, add a point at the center of each link and move this point at a distance
     * of LINK_DELTA pixels from the same point of the previous link.
     */
    private final void moveLinks() throws DbException {
        SrVector links = layout.getLinks();
        int nb = links.size();
        for (int i = 0; i < nb; i++) {
            SrVector userLinks = ((GraphLayoutLink) links.elementAt(i)).getUserLinks();
            int nb2 = userLinks.size();
            for (int i2 = 0; i2 < nb2; i2++) {
                DbObject lineGo = (DbObject) userLinks.elementAt(i2);
                DbObject go = (DbObject) lineGo.get(DbGraphic.fLineGoFrontEndGo);
                Point firstPoint = GraphicUtil.rectangleGetCenter((Rectangle) go
                        .get(DbGraphic.fGraphicalObjectRectangle));
                go = (DbObject) lineGo.get(DbGraphic.fLineGoBackEndGo);
                Point lastPoint = GraphicUtil.rectangleGetCenter((Rectangle) go
                        .get(DbGraphic.fGraphicalObjectRectangle));
                Polygon poly = new Polygon();
                poly.addPoint(firstPoint.x, firstPoint.y);
                if (((Boolean) lineGo.get(DbGraphic.fLineGoRightAngle)).booleanValue())
                    poly.addPoint(firstPoint.x, lastPoint.y);
                else if (nb2 > 1) {
                    int dx, dy;
                    if (firstPoint.x == lastPoint.x) {
                        dx = LINK_DELTA;
                        dy = 0;
                    } else {
                        double m = (double) (firstPoint.y - lastPoint.y)
                                / (lastPoint.x - firstPoint.x);
                        dy = (int) (LINK_DELTA / Math.sqrt(m * m + 1));
                        dx = (int) (m * dy);
                    }
                    int mul = i2 - nb2 / 2;
                    if (mul >= 0 && nb2 % 2 == 0)
                        mul++;
                    poly.addPoint((firstPoint.x + lastPoint.x) / 2 + dx * mul,
                            (firstPoint.y + lastPoint.y) / 2 + dy * mul);
                }
                poly.addPoint(lastPoint.x, lastPoint.y);
                lineGo.set(DbGraphic.fLineGoPolyline, poly);
                resetLabelPos(lineGo);
            }
        }
    }

    private final void moveRecursiveLinks() throws DbException {
        int nb = recursiveLinks.size();
        for (int i = 0; i < nb; i++) {
            DbObject lineGo = (DbObject) recursiveLinks.elementAt(i);
            DbObject go = (DbObject) lineGo.get(DbGraphic.fLineGoFrontEndGo);
            Point point = GraphicUtil.rectangleGetCenter((Rectangle) go
                    .get(DbGraphic.fGraphicalObjectRectangle));
            Polygon poly = (Polygon) lineGo.get(DbGraphic.fLineGoPolyline);
            poly.translate(point.x - poly.xpoints[0], point.y - poly.ypoints[0]);
            Rectangle rect = GraphicUtil.getBounds(poly);
            Rectangle rectGo = (Rectangle) go.get(DbGraphic.fGraphicalObjectRectangle);
            if (rectGo.contains(rect) || (rect.height == 0 && rect.width == 0)) {
                DbGraphic.createPolyline(lineGo);
                poly = (Polygon) lineGo.get(DbGraphic.fLineGoPolyline);
                rect = GraphicUtil.getBounds(poly);
            } else
                lineGo.set(DbGraphic.fLineGoPolyline, poly);
            dimArea.width = Math.max(dimArea.width, rect.x + rect.width);
            dimArea.height = Math.max(dimArea.height, rect.y + rect.height);
        }
    }

    private final void layoutRightAngleLinks() throws DbException {
        SrVector nodes = layout.getNodes();
        int nb = nodes.size();
        for (int i = 0; i < nb; i++) {
            GraphLayoutNode node = (GraphLayoutNode) nodes.elementAt(i);
            DbObject go = (DbObject) node.getUserNode();
            layoutRightAngleLines(go);
        }
    }

    protected abstract boolean isNode(DbObject go);

    // If directional link, mark the link as hierarchical, and give it a
    // priority.
    // Overridden
    protected void setLinkHierarchy(GraphLayoutLink link, DbObject lineGo, DbObject frontGo,
            GraphLayoutNode frontNode, DbObject backGo, GraphLayoutNode backNode)
            throws DbException {
    }

    // Overridden
    protected void resetLabelPos(DbObject lineGo) throws DbException {
    }

    // Distribute the right angle lines along each side of the box.
    public static void layoutRightAngleLines(DbObject go) throws DbException {
        SrVector leftLines = new SrVector();
        SrVector rightLines = new SrVector();
        SrVector topLines = new SrVector();
        SrVector bottomLines = new SrVector();
        Rectangle goRect = DbGraphic.getRectangle(go);
        collectRightAngleLines(go, goRect, false, leftLines, rightLines, topLines, bottomLines);
        collectRightAngleLines(go, goRect, true, leftLines, rightLines, topLines, bottomLines);
        cascadeRightAngleLines(leftLines, goRect, true);
        cascadeRightAngleLines(rightLines, goRect, true);
        cascadeRightAngleLines(topLines, goRect, false);
        cascadeRightAngleLines(bottomLines, goRect, false);
    }

    // Classify the right angle lines in 4 vectors (one for each side of the
    // box)
    private static void collectRightAngleLines(DbObject go, Rectangle goRect, boolean reverse,
            SrVector leftLines, SrVector rightLines, SrVector topLines, SrVector bottomLines)
            throws DbException {
        DbEnumeration dbEnum = ((DbRelationN) go
                .get(reverse ? DbGraphic.fGraphicalObjectBackEndLineGos
                        : DbGraphic.fGraphicalObjectFrontEndLineGos)).elements();
        while (dbEnum.hasMoreElements()) {
            DbObject lineGo = dbEnum.nextElement();
            if (!((Boolean) lineGo.get(DbGraphic.fLineGoRightAngle)).booleanValue())
                continue;
            Polygon poly = (Polygon) lineGo.get(DbGraphic.fLineGoPolyline);
            int outInd = GraphicUtil.getFirstOutPointIndex(poly, goRect, reverse);
            if (outInd == -1)
                continue;
            int inInd, outInd2;
            if (reverse) {
                inInd = outInd + 1;
                outInd2 = Math.max(0, outInd - 1);
            } else {
                inInd = outInd - 1;
                outInd2 = Math.min(poly.npoints - 1, outInd + 1);
            }
            int weight;
            SrVector lines;
            if (poly.xpoints[inInd] == poly.xpoints[outInd]) { // vertical line
                if (poly.ypoints[inInd] > poly.ypoints[outInd]) {
                    lines = topLines;
                    weight = goRect.y - poly.ypoints[outInd];
                } else {
                    lines = bottomLines;
                    weight = poly.ypoints[outInd] - goRect.y - goRect.height;
                }
                if (poly.xpoints[outInd2] > poly.xpoints[outInd])
                    weight = Integer.MAX_VALUE - weight;
            } else { // horizontal line
                if (poly.xpoints[inInd] > poly.xpoints[outInd]) {
                    lines = leftLines;
                    weight = goRect.x - poly.xpoints[outInd];
                } else {
                    lines = rightLines;
                    weight = poly.xpoints[outInd] - goRect.x - goRect.width;
                }
                if (poly.ypoints[outInd2] > poly.ypoints[outInd])
                    weight = Integer.MAX_VALUE - weight;
            }
            lines.addElement(new RightAngleElement(lineGo, poly, outInd, reverse, weight));
        }
        dbEnum.close();
    }

    // For each line, change the end points of the line segment crossing the box
    // boundary
    // to distribute evenly the lines connecting to the same side of the box.
    private static void cascadeRightAngleLines(SrVector lines, Rectangle goRect, boolean horz)
            throws DbException {
        int nbElem = lines.size();
        if (nbElem < 2)
            return;
        lines.sort();
        int pos, delta;
        if (horz) {
            delta = goRect.height / nbElem;
            pos = goRect.y + (goRect.height - (nbElem - 1) * delta) / 2;
        } else {
            delta = goRect.width / nbElem;
            pos = goRect.x + (goRect.width - (nbElem - 1) * delta) / 2;
        }
        for (int i = 0; i < nbElem; i++, pos += delta) {
            RightAngleElement elem = (RightAngleElement) lines.elementAt(i);
            int outInd = elem.outInd;
            int inInd = (elem.reverse ? outInd + 1 : outInd - 1);
            int nbPts = elem.poly.npoints;
            int[] xs = new int[nbPts + 2]; // room for 2 more points
            int[] ys = new int[nbPts + 2];
            // If the segment to be modified is the first of the line, add a
            // segment at the beginning.
            if (inInd == 0 || outInd == 0) {
                System.arraycopy(elem.poly.xpoints, 0, xs, 1, nbPts);
                System.arraycopy(elem.poly.ypoints, 0, ys, 1, nbPts);
                xs[0] = xs[1];
                ys[0] = ys[1];
                inInd++;
                outInd++;
                nbPts++;
            } else {
                System.arraycopy(elem.poly.xpoints, 0, xs, 0, nbPts);
                System.arraycopy(elem.poly.ypoints, 0, ys, 0, nbPts);
            }
            // If the segment to be modified is the last of the line, add a
            // segment at the end.
            if (inInd == nbPts - 1 || outInd == nbPts - 1) {
                xs[nbPts] = xs[nbPts - 1];
                ys[nbPts] = ys[nbPts - 1];
                nbPts++;
            }
            if (horz)
                ys[inInd] = ys[outInd] = pos;
            else
                xs[inInd] = xs[outInd] = pos;
            elem.lineGo.set(DbGraphic.fLineGoPolyline, new Polygon(xs, ys, nbPts));
        }
    }

    private static class RightAngleElement implements Comparable {
        public DbObject lineGo;
        public Polygon poly;
        public int outInd;
        public boolean reverse;
        public int weight;

        public RightAngleElement(DbObject lineGo, Polygon poly, int outInd, boolean reverse,
                int weight) {
            this.lineGo = lineGo;
            this.poly = poly;
            this.outInd = outInd;
            this.reverse = reverse;
            this.weight = weight;
        }

        public final int compareTo(Object obj) {
            if (weight < ((RightAngleElement) obj).weight)
                return -1;
            if (weight == ((RightAngleElement) obj).weight)
                return 0;
            return 1;
        }
    }
}
