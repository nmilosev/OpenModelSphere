/*************************************************************************

Copyright (C) 2012 Grandite

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

package org.modelsphere.jack.srtool.features.layout.ui;

import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Dimension2D;
import java.util.*;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbGraphicalObjectI;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.FocusManager;
import org.modelsphere.jack.srtool.features.layout.*;
import org.modelsphere.jack.srtool.features.layout.graph.Edge;
import org.modelsphere.jack.srtool.features.layout.graph.Graph;
import org.modelsphere.jack.srtool.features.layout.graph.Node;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.srtool.international.LocaleMgr;

/**
 * The Class LayoutDialogDataOperator.
 */
public class LayoutDialogDataOperator {
   
    /**
     * Layout executor.
     * 
     * @param growWidth
     *            the grow width
     * @param growHeight
     *            the grow height
     * @param clustersAlgo
     *            the clusters algo
     * @param nodesAlgo
     *            the nodes algo
     * @param edgesAlgo
     *            the edges algo
     * @param diagram
     *            the diagram
     * @param isSelected
     *            the is selected
     * @throws DbException
     *             the db exception
     * @throws Exception
     *             the exception
     */
    public static void layoutExecutor(boolean growWidth, boolean growHeight, boolean randomize,
            boolean selectionOnly, ClustersLayoutAlgorithm clustersAlgo,
            NodesLayoutAlgorithm nodesAlgo, EdgesLayoutAlgorithm edgesAlgo,
            ApplicationDiagram diagram) throws DbException, Exception {
    	
    	String key = selectionOnly ? "layoutSelection" : "layoutAll";
    	String tx = LocaleMgr.action.getString(key);
        diagram.getDiagramGO().getDb().beginWriteTrans(tx);

        List<DbGraphicalObjectI> scope = null;
        if (selectionOnly) {
            Object[] selection = FocusManager.singleton.getSelectedObjects();
            for (int i = 0; i < selection.length; i++) {
                if (selection[i] instanceof ActionInformation) {
                    if (scope == null) {
                        scope = new ArrayList<DbGraphicalObjectI>();
                    }
                    scope.add((DbGraphicalObjectI) ((ActionInformation) selection[i])
                            .getGraphicalObject());
                }
            }

        }

        Graph graph = GraphFactory.getDefaultFactory().createGraph(diagram.getDiagramGO(),
                diagram.getMainView().getModel(), scope);

        Rectangle area = diagram.getDrawingArea();
        Dimension initialSize = new Dimension((int) area.getWidth(), (int) area.getHeight());
        Dimension pagesize = diagram.getPageSize();
        Rectangle graphBounds = graph.getBounds();

        LayoutExecutor executor = new LayoutExecutor(graph, clustersAlgo, nodesAlgo, edgesAlgo);

        HashMap<LayoutAttribute<?>, Object> attributes = new HashMap<LayoutAttribute<?>, Object>();

        setLayoutAttributes(growWidth, growHeight, randomize, initialSize, pagesize, attributes);

        executor.execute(attributes);

        Dimension2D actualSize = LayoutAttributes.CANVAS_DIMENSION.getValue(attributes);
        if (!initialSize.equals(actualSize) && !selectionOnly) {
            int pageswidth = setPageWidth(pagesize, actualSize);
            int pagesheight = (int) (actualSize.getHeight() / pagesize.getHeight());
            if ((actualSize.getHeight() % pagesize.getHeight()) > 0) {
                pagesheight += 1;
            }
            Dimension nbPages = new Dimension(pageswidth, pagesheight);
            diagram.getDiagramGO().set(DbGraphic.fDiagramNbPages, nbPages);
        }

        int deltax = 0;
        int deltay = 0;
        if (selectionOnly) {
            // adjust position to match as best as possible the x,y coordinate of the selection initial coordinates
            Rectangle newGraphBounds = graph.getBounds();
            newGraphBounds.x = graphBounds.x;
            newGraphBounds.y = graphBounds.y;

            Rectangle diagramBounds = new Rectangle(0, 0, initialSize.width, initialSize.height);
            if (!diagramBounds.contains(newGraphBounds)) {
                if (diagramBounds.width < newGraphBounds.x + newGraphBounds.width) {
                    newGraphBounds.x = diagramBounds.width - newGraphBounds.width - 1;
                }
                if (diagramBounds.height < newGraphBounds.y + newGraphBounds.height) {
                    newGraphBounds.y = diagramBounds.height - newGraphBounds.height - 1;
                }
            }

            deltax = newGraphBounds.x;
            deltay = newGraphBounds.y;
        }

        // this is a sanity check in case some algo expands a graph so much that it can't be fit inside the drawing area.
        // TODO we should check the expands options and expands the drawing area in that case if it is permitted by the user.
        if (deltax >= 0 && deltay >= 0) {
            setEachNodes(graph, deltax, deltay);
            setEachEdges(graph, deltax, deltay);
        }

        diagram.getDiagramGO().getDb().commitTrans();
    }

    /**
     * Sets the page width.
     * 
     * @param pagesize
     *            the pagesize
     * @param actualSize
     *            the actual size
     * @return the int
     */
    private static int setPageWidth(Dimension pagesize, Dimension2D actualSize) {
        int pageswidth = (int) (actualSize.getWidth() / pagesize.getWidth());
        if ((actualSize.getWidth() % pagesize.getWidth()) > 0) {
            pageswidth += 1;
        }
        return pageswidth;
    }

    /**
     * Sets the each edges.
     * 
     * @param graph
     *            the new each edges
     * @throws DbException
     *             the db exception
     */
    private static void setEachEdges(Graph graph, int deltaX, int deltaY) throws DbException {
        Collection<Edge> edges = graph.edgeSet();
        for (Edge edge : edges) {
            DbObject go = edge.getGo();
            go.set(DbGraphic.fLineGoRightAngle, edge.isRightAngled());
            if (edge.getIntermediateCoordinates() != null) {
                Polygon polygon = graph.getPolyLine(edge);
                Polygon translatedPoly = new Polygon();
                for (int i = 0; i < polygon.npoints; i++) {
                    translatedPoly.addPoint(polygon.xpoints[i] + deltaX, polygon.xpoints[i]
                            + deltaY);
                }
                go.set(DbGraphic.fLineGoPolyline, translatedPoly);
            } else {
                DbGraphic.createPolyline(go, true);
            }
        }
    }

    /**
     * Sets the each nodes.
     * 
     * @param graph
     *            the graph
     * @throws DbException
     *             the db exception
     */
    private static void setEachNodes(Graph graph, int deltaX, int deltaY) throws DbException {
        Collection<Node> nodes = graph.nodeSet();
        for (Node node : nodes) {
            DbObject go = node.getGO();
            int x = (int) node.getBounds().getX() + deltaX;
            int y = (int) node.getBounds().getY() + deltaY;
            int w = node.getWidth();
            int h = node.getHeight();

            go.set(DbGraphic.fGraphicalObjectRectangle, new Rectangle(x, y, w, h));
        }
    }

    /**
     * Sets the layout attributes.
     * 
     * @param growWidth
     *            the grow width
     * @param growHeight
     *            the grow height
     * @param randomize
     *            the randomize
     * @param initialSize
     *            the initial size
     * @param pagesize
     *            the pagesize
     * @param attributes
     *            the attributes
     */
    private static void setLayoutAttributes(boolean growWidth, boolean growHeight,
            boolean randomize, Dimension initialSize, Dimension pagesize,
            HashMap<LayoutAttribute<?>, Object> attributes) {
        LayoutAttributes.RANDOMIZE.setValue(attributes, randomize);
        LayoutAttributes.CANVAS_HORIZONTAL_RESIZE_INCREMENT.setValue(attributes,
                (double) (growWidth ? pagesize.width : 0));
        LayoutAttributes.CANVAS_VERTICAL_RESIZE_INCREMENT.setValue(attributes,
                (double) (growHeight ? pagesize.height : 0));
        LayoutAttributes.CANVAS_DIMENSION.setValue(attributes, new Dimension(initialSize.width,
                initialSize.height));
    }
}
