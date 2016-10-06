/**
 * ----------------------------------------------------------------------------------------
 * Description : A class that fits subrectangles into a power-of-2 rectangle
 * (C) Copyright 2000-2002 by Javier Arevalo
 * This code is free to use and modify for all purposes
 * ----------------------------------------------------------------------------------------
 * 
 * http://www.iguanademos.com/Jare/Articles.php?view=RectPlace
 * 
 * You have a bunch of rectangular pieces. You need to arrange them in a
 * rectangular surface so that they don't overlap, keeping the total area of the
 * rectangle as small as possible. This is fairly common when arranging characters
 * in a bitmapped font, lightmaps for a 3D engine, and I guess other situations as
 * well.
 * 
 * The idea of this algorithm is that, as we add rectangles, we can pre-select
 * "interesting" places where we can try to add the next rectangles. For optimal
 * results, the rectangles should be added in order. I initially tried using area
 * as a sorting criteria, but it didn't work well with very tall or very flat
 * rectangles. I then tried using the longest dimension as a selector, and it
 * worked much better. So much for intuition...
 * 
 * These "interesting" places are just to the right and just below the currently
 * added rectangle. The first rectangle, obviously, goes at the top left, the next
 * one would go either to the right or below this one, and so on. It is a weird way
 * to do it, but it seems to work very nicely.
 * 
 * The way we search here is fairly brute-force, the fact being that for most off-
 * line purposes the performance seems more than adequate. I have generated a
 * japanese font with around 8500 characters and all the time was spent generating
 * the bitmaps.
 * 
 * Also, for all we care, we could grow the parent rectangle in a different way
 * than power of two. It just happens that power of 2 is very convenient for
 * graphics hardware layers.
 * 
 * I'd be interested in hearing of other approaches to this problem. Make sure
 * to post them on http://www.flipcode.com
 */
package org.modelsphere.plugins.layout.clusters.arevalo;

import java.awt.Rectangle;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.JComponent;

import org.modelsphere.jack.srtool.features.layout.ClustersLayoutAlgorithm;
import org.modelsphere.jack.srtool.features.layout.LayoutAttribute;
import org.modelsphere.jack.srtool.features.layout.LayoutException;
import org.modelsphere.jack.srtool.features.layout.graph.Graph;
import org.modelsphere.jack.srtool.features.layout.graph.Node;

/**
 * The Class ArevaloPackingAlgorithm.
 * 
 * @author David
 */
public class ArevaloPackingAlgorithm implements ClustersLayoutAlgorithm {

    /** The Constant NODES_GAP. */
    private static final int NODES_GAP = 69;

    /** The super cluster. */
    private ClusterDisposer superCluster;

    /** The clusters bounds. */
    private HashMap<Bounds, Integer> clustersBounds;

    /** The nodes bounds. */
    private HashMap<Node, Bounds> nodesBounds;

    /** The nodes cluster. */
    private HashMap<Node, Integer> nodesCluster;

    /**
     * Instantiates a new arevalo packing algorithm.
     */
    public ArevaloPackingAlgorithm() {
        superCluster = new ClusterDisposer(-1);
        clustersBounds = new HashMap<Bounds, Integer>();
        nodesBounds = new HashMap<Node, Bounds>();
    }

    /**
     * Instantiates a new arevalo packing algorithm.
     * 
     * @param superCluster
     *            the super cluster
     */
    ArevaloPackingAlgorithm(ClusterDisposer superCluster) {
        this.superCluster = superCluster;
        nodesBounds = new HashMap<Node, Bounds>();
        clustersBounds = new HashMap<Bounds, Integer>();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.modelsphere.jack.srtool.features.layout.ClustersLayoutAlgorithm# layoutClusters(org.
     * modelsphere.jack.srtool.features.layout.graph.Graph, java.util.HashMap, java.util.HashMap)
     */
    @Override
    public void layoutClusters(Graph graph, HashMap<Node, Integer> clusters,
            HashMap<LayoutAttribute<?>, Object> attributes) throws LayoutException {

        clustersBounds.clear();
        nodesBounds.clear();
        nodesCluster = clusters;

        for (Entry<Node, Integer> entry : nodesCluster.entrySet()) {

            Bounds nodeBounds = new Bounds(entry.getKey().getBounds());
            nodesBounds.put(entry.getKey(), nodeBounds);
        }

        for (int clusterId : nodesCluster.values()) {

            List<Node> nodes = new ArrayList<Node>();
            for (Entry<Node, Integer> nodeEntry : nodesCluster.entrySet()) {
                if (nodeEntry.getValue() == clusterId) {
                    nodes.add(nodeEntry.getKey());
                }
            }

            Bounds clusterBounds = getClusterBounds(nodes);
            clustersBounds.put(clusterBounds, clusterId);
        }

        if (!clustersBounds.isEmpty()) {
            layout(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.modelsphere.jack.srtool.features.layout.LayoutAlgorithm# createOptionComponent()
     */
    @Override
    public JComponent createOptionComponent() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.srtool.features.layout.LayoutAlgorithm#getText()
     */
    @Override
    public String getText() {
        return "Arevalo Packing";
    }

    /**
     * Layout.
     * 
     * @param maxLayerWidth
     *            the max layer width
     * @param maxLayerHeight
     *            the max layer height
     * @throws LayoutException
     *             the layout exception
     */
    void layout(int maxLayerWidth, int maxLayerHeight) throws LayoutException {
        List<Bounds> clustersBoundsList = new ArrayList<Bounds>();
        clustersBoundsList.addAll(clustersBounds.keySet());

        // Sort the subRects based on dimensions, larger dimension goes first.
        Comparator<Bounds> comparator = Collections.reverseOrder();
        Collections.sort(clustersBoundsList, comparator);

        superCluster = new ClusterDisposer(-1);

        for (Bounds clusterBounds : clustersBoundsList) {

            // We make sure we leave NODES_GAP between Node.
            Bounds nextBounds = new Bounds(0, 0, clusterBounds.width + NODES_GAP,
                    clusterBounds.height + NODES_GAP);

            boolean isPlaced = superCluster.addAtEmptySpotAutoGrow(nextBounds, maxLayerWidth,
                    maxLayerHeight);

            // TODO Use Cluster.setPosition() when implemented.
            if (isPlaced) {
                for (Entry<Node, Integer> nodeEntry : nodesCluster.entrySet()) {

                    if (nodeEntry.getValue() == clustersBounds.get(clusterBounds)) {
                        int x = nextBounds.x + nodeEntry.getKey().getX() - clusterBounds.x;
                        int y = nextBounds.y + nodeEntry.getKey().getY() - clusterBounds.y;
                        nodeEntry.getKey().setLocation(x, y);
                    }
                }
                clusterBounds.x = nextBounds.x;
                clusterBounds.y = nextBounds.y;
            } else {
                throw new LayoutException(this);
            }
        }
    }

    /**
     * Gets the cluster bounds.
     * 
     * @param clusterNodes
     *            the cluster nodes
     * @return the cluster bounds
     */
    Bounds getClusterBounds(List<Node> clusterNodes) {
        Bounds clusterBounds = new Bounds(0, 0, 0, 0);
        Rectangle nodeBounds;

        int maxX = 0;
        int maxY = 0;
        for (Node node : clusterNodes) {
            nodeBounds = node.getBounds();

            if (nodeBounds.getMaxX() > maxX) {
                maxX = (int) nodeBounds.getMaxX();
            }

            if (nodeBounds.getMaxY() > maxY) {
                maxY = (int) nodeBounds.getMaxY();
            }
        }

        int minX = maxX;
        int minY = maxY;
        for (Node node : clusterNodes) {
            nodeBounds = node.getBounds();

            if (nodeBounds.getMinX() < minX) {
                minX = (int) nodeBounds.getMinX();
            }

            if (nodeBounds.getMinY() < minY) {
                minY = (int) nodeBounds.getMinY();
            }
        }

        clusterBounds.width = maxX - minX;
        clusterBounds.height = maxY - minY;
        clusterBounds.x = minX;
        clusterBounds.y = minY;

        return clusterBounds;
    }

    /**
     * Gets the super cluster.
     * 
     * @return the super cluster
     */
    ClusterDisposer getSuperCluster() {
        return superCluster;
    }

}
