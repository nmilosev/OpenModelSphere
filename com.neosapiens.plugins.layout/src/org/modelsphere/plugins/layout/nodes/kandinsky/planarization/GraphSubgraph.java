/**
 * 
 */
package org.modelsphere.plugins.layout.nodes.kandinsky.planarization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DirectedSubgraph;
import org.modelsphere.jack.srtool.features.layout.graph.Edge;
import org.modelsphere.jack.srtool.features.layout.graph.Graph;
import org.modelsphere.jack.srtool.features.layout.graph.Vertex;

/**
 * The Class GraphSubgraph.
 * 
 * @author David
 */
public class GraphSubgraph extends DirectedSubgraph<Vertex, Edge> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4048806925120778658L;

    /**
     * Creates a new subgraph with all vertices and edges of base graph.
     * 
     * @param base
     *            the base
     */
    public GraphSubgraph(Graph base) {
        super(base, null, null);
    }

    /**
     * Creates a new subgraph of a Graph.
     * 
     * @param base
     *            the base (backing) graph on which the subgraph will be based.
     * @param vertexSubset
     *            vertices to include in the subgraph. If <code>null</code> then all vertices are
     *            included.
     * @param edgeSubset
     *            edges to in include in the subgraph. If <code>null</code> then all the edges whose
     *            vertices found in the graph are included.
     */
    public GraphSubgraph(DirectedGraph<Vertex, Edge> base, Set<Vertex> vertexSubset,
            Set<Edge> edgeSubset) {
        super(base, vertexSubset, edgeSubset);
    }

    /**
     * Neighbors of.
     * 
     * @param vertex
     *            A vertex in this subgraph.
     * @return The collection of vertices that can be accessed within "one edge of reach" of . This
     *         conception of neighborhood does not consider edge direction. Meaning that for each
     *         incident edges of (incoming or outgoing) there is a neighbor (the other end of the
     *         edge). An empty set is returned if has no edges, or if it is not part of this graph.
     */
    public Collection<Vertex> neighborsOf(Vertex vertex) {
        Set<Vertex> neighbors = new LinkedHashSet<Vertex>();
        if (!containsVertex(vertex)) {
            return neighbors;
        }

        Set<Edge> inEdges = incomingEdgesOf(vertex);
        for (Edge in : inEdges) {
            neighbors.add(getEdgeSource(in));
        }
        Set<Edge> outEdges = outgoingEdgesOf(vertex);
        for (Edge out : outEdges) {
            neighbors.add(getEdgeTarget(out));
        }
        return neighbors;
    }

    /**
     * Neighbor count.
     * 
     * @param vertex
     *            the vertex
     * @return Neighbors count
     */
    public int neighborCount(Vertex vertex) {
        return neighborsOf(vertex).size();
    }

    /**
     * Degree of.
     * 
     * @param vertex
     *            the vertex
     * @return Sum of incomingDegree and ougoingDegree.
     */
    public int degreeOf(Vertex vertex) {
        return inDegreeOf(vertex) + outDegreeOf(vertex);
    }

    /**
     * Gets the vertex with minimal degree.
     * 
     * @return A vertex of the graph with minimal degree.
     */
    public Vertex getVertexWithMinimalDegree() {
        int minimalDegree = Integer.MAX_VALUE;
        Vertex minimalDegreeVertex = null;

        for (Vertex vertex : vertexSet()) {
            int vertexDegree = degreeOf(vertex);
            if (vertexDegree < minimalDegree) {
                minimalDegree = vertexDegree;
                minimalDegreeVertex = vertex;
            }
        }

        if (minimalDegreeVertex == null) {
            throw new IllegalArgumentException("This graph doesn't contains any vertex.");
        }
        return minimalDegreeVertex;
    }

    /**
     * A sink is a vertex with outgoing degree of 0. Which means this vertex doesn't have outgoing
     * edges.
     * 
     * @return The list of all sink of the graph.
     */
    public List<Vertex> getAllSink() {
        List<Vertex> sinks = new ArrayList<Vertex>();

        for (Vertex vertex : vertexSet()) {
            if (outDegreeOf(vertex) == 0) {
                sinks.add(vertex);
            }
        }
        return sinks;
    }

    /**
     * Gets the sink count.
     * 
     * @return The number of sink in this graph.
     */
    public int getSinkCount() {
        return getAllSink().size();
    }

    /**
     * A source in a graph is a vertex with incoming degree of 0. Which means this vertex doesn't
     * have incoming edges. Don't confuse it with a edge source. Which is the starting vertex of an
     * edge.
     * 
     * @return the all source
     */
    public List<Vertex> getAllSource() {
        List<Vertex> sources = new ArrayList<Vertex>();

        for (Vertex vertex : vertexSet()) {
            if (inDegreeOf(vertex) == 0) {
                sources.add(vertex);
            }
        }
        return sources;
    }

    /**
     * Gets the source count.
     * 
     * @return The number of source in this graph.
     */
    public int getSourceCount() {
        return getAllSource().size();
    }
}
