package org.modelsphere.plugins.layout.nodes.layerers;

import java.util.HashSet;
import java.util.Set;

import org.modelsphere.jack.srtool.features.layout.LayoutException;
import org.modelsphere.jack.srtool.features.layout.graph.Edge;
import org.modelsphere.jack.srtool.features.layout.graph.Graph;
import org.modelsphere.jack.srtool.features.layout.graph.Vertex;

/**
 * The Class SinkConsumerLayerer.
 */
public class SinkConsumerLayerer extends Layerer {

    /**
     * Produces a proper layered graph from a supplied directed graph by "consuming the sinks".
     * 
     * Focuses on minimizing width. Drawback: Produces a tall graph (many layers).
     * 
     * @param directedGraph
     *            the directed graph
     * @return A proper layered representation of .
     * @throws LayoutException
     *             If <code>directedGraph</code> contains no more sink at some point. This will
     *             eventually happens if the source graph has a cycle.
     */
    @Override
    public LayeredGraph performLayering(Graph directedGraph) throws LayoutException {

        if (directedGraph == null) {
            throw new NullPointerException();
        }

        Graph copy = directedGraph.clone();

        LayeredGraph layeredGraph = new LayeredGraph();
        int layer = 0;
        Set<Vertex> vertices = copy.vertexSet();
        while (vertices.size() > 0) {

            Set<Vertex> nextLayer = new HashSet<Vertex>();
            for (Vertex n : vertices) {
                if (copy.outDegreeOf(n) == 0) {
                    nextLayer.add(n);
                }
            }

            if (nextLayer.size() == 0) {
                throw new LayoutException(null,
                        "Layering stage: No sink were found in the source graph. It contains a cycle.");
            }

            for (Vertex n : nextLayer) {
                layeredGraph.addVertex(n, layer);
            }

            copy.removeAllVertices(nextLayer);
            layer++;
            vertices = copy.vertexSet();
        }

        Set<Edge> edges = directedGraph.edgeSet();
        for (Edge edge : edges) {
            layeredGraph.addEdge(directedGraph.getEdgeSource(edge), directedGraph
                    .getEdgeTarget(edge), edge);
        }

        layeredGraph.makeProper();

        return layeredGraph;
    }
}
