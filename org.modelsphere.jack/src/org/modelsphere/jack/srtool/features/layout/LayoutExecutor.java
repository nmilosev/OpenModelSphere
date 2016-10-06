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

package org.modelsphere.jack.srtool.features.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.modelsphere.jack.srtool.features.layout.graph.Graph;
import org.modelsphere.jack.srtool.features.layout.graph.Node;

/**
 * The Class LayoutExecutor.
 */
public class LayoutExecutor {

    /** The clusters algo. */
    private ClustersLayoutAlgorithm clustersAlgo;

    /** The nodes algo. */
    private NodesLayoutAlgorithm nodesAlgo;

    /** The edges algo. */
    private EdgesLayoutAlgorithm edgesAlgo;

    /** The clusters. */
    private HashMap<Node, Integer> clusters = new HashMap<Node, Integer>();

    /** The clusters count. */
    private int clustersCount = 0;

    /** The graph. */
    private Graph graph;

    /**
     * Instantiates a new layout executor.
     * 
     * @param graph
     *            the graph
     * @param clustersAlgo
     *            the clusters algo
     * @param nodesAlgo
     *            the nodes algo
     * @param edgesAlgo
     *            the edges algo
     */
    public LayoutExecutor(Graph graph, ClustersLayoutAlgorithm clustersAlgo,
            NodesLayoutAlgorithm nodesAlgo, EdgesLayoutAlgorithm edgesAlgo) {
        this.clustersAlgo = clustersAlgo;
        this.nodesAlgo = nodesAlgo;
        this.edgesAlgo = edgesAlgo;
        this.graph = graph;
    }

    /**
     * Instantiates a new layout executor.
     * 
     * @param graph
     *            the graph
     * @param clustersAlgo
     *            the clusters algo
     * @param nodesAlgo
     *            the nodes algo
     * @param edgesAlgo
     *            the edges algo
     * @param clusters
     *            the clusters
     */
    public LayoutExecutor(Graph graph, ClustersLayoutAlgorithm clustersAlgo,
            NodesLayoutAlgorithm nodesAlgo, EdgesLayoutAlgorithm edgesAlgo,
            HashMap<Node, Integer> clusters) {
        this(graph, clustersAlgo, nodesAlgo, edgesAlgo);
        this.clusters = clusters;
    }

    /**
     * Clusters.
     */
    void clusters() {
        // TODO: add mechanisms to optionally append to a cluster
        // other nodes with similar visual attributes (background color??)

        Collection<Node> nodes = graph.nodeSet();
        for (Node node : nodes) {
            if (!clusters.containsKey(node)) {
                clustersCount++;
                clusters.put(node, clustersCount);
                clustersNeighbors(node, clustersCount);
            }
        }
    }

    /**
     * Clusters neighbors.
     * 
     * @param node
     *            the node
     * @param cluster
     *            the cluster
     */
    void clustersNeighbors(Node node, int cluster) {
        Collection<Node> neighbors = graph.connectedNodes(node);
        if (neighbors == null) {
            return;
        }
        for (Node neighbor : neighbors) {
            if (!clusters.containsKey(neighbor)) {
                clusters.put(neighbor, cluster);
                clustersNeighbors(neighbor, cluster);
            }
        }
    }

    /**
     * Execute.
     * 
     * @param attributes
     *            the attributes
     * @throws Exception
     *             the exception
     */
    public void execute(HashMap<LayoutAttribute<?>, Object> attributes) throws Exception {
        // identify clusters
        clusters();

        // layout the nodes independently of other clusters' nodes
        if (nodesAlgo != null) {
            List<Node> nodes = null;
            for (int i = 1; i <= clustersCount; i++) {
                nodes = getClusterNodes(i);
                nodesAlgo.layoutClusterNodes(graph, nodes);
            }
        }

        if (edgesAlgo != null) {
            List<Node> nodes = null;
            for (int i = 1; i <= clustersCount; i++) {
                nodes = getClusterNodes(i);
                edgesAlgo.layoutClusterEdges(graph, nodes);
            }
        }

        if (clustersAlgo != null) {
            clustersAlgo.layoutClusters(graph, clusters, attributes);
        }

        LayoutAttributes.CANVAS_DIMENSION.setValue(attributes, graph.getDimension());
    }

    /**
     * Gets the cluster nodes.
     * 
     * @param cluster
     *            the cluster
     * @return the cluster nodes
     */
    List<Node> getClusterNodes(int cluster) {
        List<Node> clusterNodes = new ArrayList<Node>();
        for (Node node : clusters.keySet()) {
            Integer c = clusters.get(node);
            if (c == cluster) {
                clusterNodes.add(node);
            }
        }
        return clusterNodes;
    }

}
