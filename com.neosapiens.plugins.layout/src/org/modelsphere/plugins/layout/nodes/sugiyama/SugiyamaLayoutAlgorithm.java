package org.modelsphere.plugins.layout.nodes.sugiyama;

import java.awt.Polygon;
import java.util.List;

import javax.swing.JComponent;

import org.modelsphere.jack.srtool.features.layout.LayoutException;
import org.modelsphere.jack.srtool.features.layout.NodesLayoutAlgorithm;
import org.modelsphere.jack.srtool.features.layout.graph.*;
import org.modelsphere.plugins.layout.nodes.layerers.LayeredGraph;
import org.modelsphere.plugins.layout.nodes.layerers.Layerer;

/**
 * The Class SugiyamaLayoutAlgorithm.
 */
public class SugiyamaLayoutAlgorithm implements NodesLayoutAlgorithm {

    /** The cycle remover. */
    private CycleRemover cycleRemover;

    /** The layerer. */
    private Layerer layerer;

    /** The node orderer. */
    private NodeOrderer nodeOrderer;

    /** The coordinate assigner. */
    private CoordinateAssigner coordinateAssigner;

    /** The DEFAUL t_ text. */
    static private String DEFAULT_TEXT = "Sugiyama";

    /** The text. */
    private String text;

    /**
     * Instantiates a new sugiyama layout algorithm.
     * 
     * @param cycleRemover
     *            the cycle remover
     * @param layerer
     *            the layerer
     * @param nodeOrderer
     *            the node orderer
     * @param coordinateAssigner
     *            the coordinate assigner
     */
    public SugiyamaLayoutAlgorithm(CycleRemover cycleRemover, Layerer layerer,
            NodeOrderer nodeOrderer, CoordinateAssigner coordinateAssigner) {
        if (cycleRemover == null || layerer == null || nodeOrderer == null
                || coordinateAssigner == null) {
            throw new NullPointerException();
        }

        this.cycleRemover = cycleRemover;
        this.layerer = layerer;
        this.nodeOrderer = nodeOrderer;
        this.coordinateAssigner = coordinateAssigner;
        text = DEFAULT_TEXT;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.modelsphere.jack.srtool.features.layout.NodesLayoutAlgorithm# layoutClusterNodes(org.
     * modelsphere.jack.srtool.features.layout.graph.Graph, java.util.List)
     */
    @Override
    public void layoutClusterNodes(Graph directedGraph, List<Node> clusterNodes)
            throws LayoutException {
        if (directedGraph == null || clusterNodes == null || clusterNodes.size() <= 1) {
            return;
        }

        Graph subGraph = createClusterSubGraph(directedGraph, clusterNodes);

        cycleRemover.removeCycles(subGraph);
        LayeredGraph layeredGraph = layerer.performLayering(subGraph);
        layeredGraph.makeProper();
        nodeOrderer.orderNodes(layeredGraph);

        cycleRemover.restoreCycles(layeredGraph);

        coordinateAssigner.assignCoordinates(layeredGraph);
        makeEdgesWithIntermediateCoordinates(subGraph, layeredGraph);
    }

    /**
     * Creates the cluster sub graph.
     * 
     * @param directedGraph
     *            the directed graph
     * @param clusterNodes
     *            the cluster nodes
     * @return the graph
     */
    private Graph createClusterSubGraph(Graph directedGraph, List<Node> clusterNodes) {
        Graph subGraph = new Graph();

        for (Vertex n : directedGraph.vertexSet()) {
            if (clusterNodes.contains(n)) {
                subGraph.addVertex(n);
            }
        }
        for (Edge e : directedGraph.edgeSet()) {
            Vertex src = directedGraph.getEdgeSource(e);
            Vertex dst = directedGraph.getEdgeTarget(e);
            if (clusterNodes.contains(src) && clusterNodes.contains(dst)) {
                subGraph.addEdge(src, dst, e);
            }
        }
        return subGraph;
    }

    /**
     * Make edges with intermediate coordinates.
     * 
     * @param directedGraph
     *            the directed graph
     * @param layeredGraph
     *            the layered graph
     */
    private void makeEdgesWithIntermediateCoordinates(Graph directedGraph, LayeredGraph layeredGraph) {
        for (Edge edge : directedGraph.edgeSet()) {
            edge.setRightAngled(false);

            // among all the edges we had to deal with, those that were lost
            // in the layered graph have intermediate positions.
            if (!layeredGraph.containsEdge(edge)) {

                Vertex source = directedGraph.getEdgeSource(edge);
                Vertex destination = directedGraph.getEdgeTarget(edge);

                Polygon intermediateCoordinates = new Polygon();

                List<DummyVertex> path = layeredGraph.findDummyVertexPath(source, destination);

                if (path.size() > 0) {
                    for (DummyVertex dummy : path) {
                        intermediateCoordinates.addPoint(dummy.getX(), dummy.getY());
                    }
                }
                edge.setIntermediateCoordinates(intermediateCoordinates);
            }
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
        return text;
    }

    /**
     * Sets the text.
     * 
     * @param text
     *            the new text
     */
    public void setText(String text) {
        this.text = text;
    }
}
