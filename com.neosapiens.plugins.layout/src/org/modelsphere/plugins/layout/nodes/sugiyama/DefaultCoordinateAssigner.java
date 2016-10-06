package org.modelsphere.plugins.layout.nodes.sugiyama;

import java.awt.Dimension;
import java.awt.Point;
import java.util.*;

import org.modelsphere.jack.srtool.features.layout.LayoutException;
import org.modelsphere.jack.srtool.features.layout.graph.Node;
import org.modelsphere.jack.srtool.features.layout.graph.Vertex;
import org.modelsphere.plugins.layout.nodes.layerers.LayeredGraph;

/**
 * The Class DefaultCoordinateAssigner.
 */
public class DefaultCoordinateAssigner extends CoordinateAssigner {

    /** The DEFAUL t_ nod e_ margin. */
    static public int DEFAULT_NODE_MARGIN = 40;

    /** The DEFAUL t_ laye r_ margin. */
    static public int DEFAULT_LAYER_MARGIN = 80;

    /** The node margin. */
    private int nodeMargin;

    /** The layer margin. */
    private int layerMargin;

    /** The graph. */
    private LayeredGraph graph;

    /**
     * The Class Interval.
     */
    private class Interval {

        /** The start. */
        public int start;

        /** The length. */
        public int length;

        /**
         * Instantiates a new interval.
         * 
         * @param start
         *            the start
         * @param length
         *            the length
         */
        public Interval(int start, int length) {
            this.start = start;
            this.length = length;
        }
    }

    /**
     * Instantiates a new default coordinate assigner.
     */
    public DefaultCoordinateAssigner() {
        nodeMargin = DEFAULT_NODE_MARGIN;
        layerMargin = DEFAULT_LAYER_MARGIN;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.modelsphere.plugins.layout.nodes.sugiyama.CoordinateAssigner# assignCoordinates(org.
     * modelsphere.plugins.layout.nodes.sugiyama.LayeredGraph)
     */
    @Override
    public void assignCoordinates(LayeredGraph graph) throws LayoutException {
        if (graph == null) {
            throw new NullPointerException("graph");
        }

        this.graph = graph;

        int nbLayer = graph.getTopLayer() + 1;
        List<Integer> widths = computeLayerWidths(graph);
        List<Integer> heights = computeLayerHeights(graph);

        int maxWidth = Collections.max(widths);
        int widestLayer = widths.indexOf(maxWidth);

        // the widest layer is the most compact (all the space is used)
        int layer = widestLayer;
        int layerY = computeLayerY(graph, heights, layer);
        int currentX = 0;
        // set all the vertex's positions in it.
        List<Vertex> orderedLayer = graph.getLayerOrder(layer);
        for (Vertex v : orderedLayer) {
            Dimension dimension = v.getDimension();
            int currentY = layerY;
            currentY += (heights.get(layer) - dimension.height) / 2;

            v.setLocation(new Point(currentX, currentY));
            currentX += dimension.width + nodeMargin;
        }

        // set the layers going downward from the widest one.
        for (layer = widestLayer - 1; layer >= 0; layer--) {
            setLayerCoordinates(graph, widths, heights, layer, layer + 1);
        }

        // set the layers going upward from the widest one.
        for (layer = widestLayer + 1; layer < nbLayer; layer++) {
            setLayerCoordinates(graph, widths, heights, layer, layer - 1);
        }
    }

    /**
     * Sets the layer coordinates.
     * 
     * @param graph
     *            the graph
     * @param widths
     *            the widths
     * @param heights
     *            the heights
     * @param currentLayer
     *            the current layer
     * @param fixedLayer
     *            the fixed layer
     */
    private void setLayerCoordinates(LayeredGraph graph, List<Integer> widths,
            List<Integer> heights, int currentLayer, int fixedLayer) {

        int layerY = computeLayerY(graph, heights, currentLayer);

        List<Vertex> orderedVertices = graph.getLayerOrder(currentLayer);
        List<Integer> desiredCenters = new ArrayList<Integer>(orderedVertices.size());

        int vertexCount = orderedVertices.size();
        int maxWidth = Collections.max(widths);
        for (int i = 0; i < vertexCount; i++) {
            Vertex v = orderedVertices.get(i);

            Set<Vertex> fixedNeighbors = graph.neighborsOnLayer(v, fixedLayer);
            if (fixedNeighbors.size() != 0) {
                int sum = 0;
                for (Vertex fixedNeighbor : fixedNeighbors) {
                    sum += fixedNeighbor.getLocation().x + fixedNeighbor.getDimension().width / 2;
                }
                int desiredCenter = sum / fixedNeighbors.size();
                desiredCenters.add(i, desiredCenter);
            } else {
                int meanSpace = maxWidth / vertexCount;
                int desiredCenter = meanSpace * i + meanSpace / 2;
                desiredCenters.add(i, desiredCenter);
            }
        }

        /* fix collisions induced by the desired centers. */
        fixCenters(orderedVertices, desiredCenters, maxWidth);

        for (int i = 0; i < orderedVertices.size(); i++) {
            Vertex vertex = orderedVertices.get(i);
            int currentY = layerY;
            currentY += (heights.get(currentLayer) - vertex.getDimension().height) / 2;
            vertex.setLocation(new Point(desiredCenters.get(i) - vertex.getDimension().width / 2,
                    currentY));
        }
    }

    /**
     * Fix centers.
     * 
     * @param orderedVertices
     *            the ordered vertices
     * @param desiredCenters
     *            the desired centers
     * @param maxWidth
     *            the max width
     */
    private void fixCenters(List<Vertex> orderedVertices, List<Integer> desiredCenters, int maxWidth) {
        List<Interval> collisions = findCollisions(orderedVertices, desiredCenters, maxWidth);
        while (!collisions.isEmpty()) {
            for (Interval collision : collisions) {
                int neededWidth = 0;
                for (int i = 0; i < collision.length; i++) {
                    if (neededWidth != 0) {
                        neededWidth += nodeMargin;
                    }
                    neededWidth += orderedVertices.get(collision.start + i).getDimension().width;
                }
                Vertex firstVertex = orderedVertices.get(collision.start);
                int leftBound = desiredCenters.get(collision.start)
                        - firstVertex.getDimension().width / 2;
                Vertex lastVertex = orderedVertices.get(collision.start + collision.length - 1);
                int rightBound = desiredCenters.get(collision.start + collision.length - 1)
                        + lastVertex.getDimension().width / 2;
                int collidingGroupCenter = (leftBound + rightBound) / 2;

                // Expand the group.
                int newLeftBound = collidingGroupCenter - neededWidth / 2;

                // is it too much to the left?
                if (newLeftBound < 0) {
                    newLeftBound = 0;
                }

                // or maybe it's too much to the right?
                if (newLeftBound + neededWidth > maxWidth) {
                    newLeftBound = maxWidth - neededWidth;
                }

                int currentX = newLeftBound;
                for (int i = 0; i < collision.length; i++) {
                    int index = collision.start + i;
                    Vertex vertex = orderedVertices.get(index);
                    int vertexWidth = vertex.getDimension().width;
                    int centerX = currentX + vertexWidth / 2;

                    desiredCenters.remove(index);
                    desiredCenters.add(index, centerX);

                    currentX += vertexWidth + nodeMargin;
                }
            }
            collisions = findCollisions(orderedVertices, desiredCenters, maxWidth);
        }
    }

    /**
     * Find collisions.
     * 
     * @param orderedVertex
     *            the ordered vertex
     * @param desiredCenters
     *            the desired centers
     * @param maxWidth
     *            the max width
     * @return the list
     */
    private List<Interval> findCollisions(List<Vertex> orderedVertex, List<Integer> desiredCenters,
            int maxWidth) {
        int vertexCount = orderedVertex.size();

        List<Interval> collisions = new ArrayList<Interval>();

        boolean inCollision = false;
        int collisionCount = 0;
        for (int i = 0; i < vertexCount - 1; i++) {
            Vertex currentVertex = orderedVertex.get(i);
            int currentVertexRightBound = desiredCenters.get(i)
                    + currentVertex.getDimension().width / 2;
            Vertex nextVertex = orderedVertex.get(i + 1);
            int nextVertexLeftBound = desiredCenters.get(i + 1) - nextVertex.getDimension().width
                    / 2;

            if (currentVertexRightBound > nextVertexLeftBound) {
                if (!inCollision) {
                    inCollision = true;
                    collisions.add(collisionCount, new Interval(i, 2));
                } else {
                    Interval c = collisions.get(collisionCount);
                    c.length += 1;
                }
            } else {
                if (inCollision) {
                    inCollision = false;
                    collisionCount++;
                }
            }
        }
        return collisions;
    }

    // TODO Feature envy, move to LayeredGraph.
    /**
     * Compute layer y.
     * 
     * @param graph
     *            the graph
     * @param heights
     *            the heights
     * @param layer
     *            the layer
     * @return the int
     */
    private int computeLayerY(LayeredGraph graph, List<Integer> heights, int layer) {
        int y = 0;
        int layerCount = graph.getNbLayers();
        for (int layerAbove = layerCount - 1; layerAbove > layer; layerAbove--) {
            y += heights.get(layerAbove) + layerMargin;
        }
        return y;
    }

    // TODO Feature envy, move to LayeredGraph.
    /**
     * Compute layer widths.
     * 
     * @param graph
     *            the graph
     * @return the list
     */
    private List<Integer> computeLayerWidths(LayeredGraph graph) {
        List<Integer> widths = new ArrayList<Integer>(graph.getNbLayers());
        for (int layer = 0; layer < graph.getNbLayers(); layer++) {
            widths.add(layer, computeLayerWidth(layer));
        }
        return widths;
    }

    // TODO Feature envy, move to LayeredGraph.
    /**
     * Compute layer heights.
     * 
     * @param graph
     *            the graph
     * @return the list
     */
    private List<Integer> computeLayerHeights(LayeredGraph graph) {
        List<Integer> heights = new ArrayList<Integer>(graph.getNbLayers());
        for (int layer = 0; layer < graph.getNbLayers(); layer++) {
            heights.add(layer, computeLayerHeight(layer));
        }
        return heights;
    }

    /**
     * Compute layer width.
     * 
     * @param layer
     *            the layer
     * @return the int
     */
    // TODO Feature envy, move to LayeredGraph.
    private int computeLayerWidth(int layer) {
        int layerWidth = 0;
        Set<Vertex> vertices = graph.verticesOnLayer(layer);
        for (Vertex v : vertices) {
            int width = v.getDimension().width;
            layerWidth += width;
        }
        layerWidth += nodeMargin * (vertices.size() - 1);

        return layerWidth;
    }

    /**
     * Compute layer height.
     * 
     * @param layer
     *            the layer
     * @return the int
     */
    // TODO Feature envy, move to LayeredGraph.
    private int computeLayerHeight(int layer) {
        int layerHeight = 0;
        Set<Vertex> nodes = graph.verticesOnLayer(layer);
        for (Vertex v : nodes) {
            if (v instanceof Node) {
                int height = ((Node) v).getHeight();
                if (height > layerHeight) {
                    layerHeight = height;
                }
            }
        }
        return layerHeight;
    }

    /**
     * Sets the node margin.
     * 
     * @param nodeMargin
     *            the new node margin
     */
    public void setNodeMargin(int nodeMargin) {
        this.nodeMargin = nodeMargin;
    }

    /**
     * Gets the node margin.
     * 
     * @return the node margin
     */
    public int getNodeMargin() {
        return nodeMargin;
    }

    /**
     * Sets the layer margin.
     * 
     * @param layerMargin
     *            the new layer margin
     */
    public void setLayerMargin(int layerMargin) {
        this.layerMargin = layerMargin;
    }

    /**
     * Gets the layer margin.
     * 
     * @return the layer margin
     */
    public int getLayerMargin() {
        return layerMargin;
    }
}
