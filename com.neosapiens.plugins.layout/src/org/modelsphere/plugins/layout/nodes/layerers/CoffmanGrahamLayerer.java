package org.modelsphere.plugins.layout.nodes.layerers;

import java.util.*;

import org.jgrapht.DirectedGraph;
import org.modelsphere.jack.srtool.features.layout.LayoutException;
import org.modelsphere.jack.srtool.features.layout.graph.Edge;
import org.modelsphere.jack.srtool.features.layout.graph.Graph;
import org.modelsphere.jack.srtool.features.layout.graph.Vertex;
import org.modelsphere.plugins.layout.CyclicGraphException;

/**
 * Layerer that implements the Coffman Graham layering algorithm.
 */
public class CoffmanGrahamLayerer extends Layerer {

    /** The maximum number of vertices that can be put inside a single layer */
    private int maximumLayerSize;

    public CoffmanGrahamLayerer() {
        maximumLayerSize = Integer.MAX_VALUE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.modelsphere.plugins.layout.nodes.layerers.Layerer#performLayering(org.modelsphere.jack
     * .srtool.features.layout.graph.Graph)
     */
    @Override
    public LayeredGraph performLayering(Graph graph) throws LayoutException {
        if (graph == null) {
            throw new NullPointerException("graph");
        }

        LayeredGraph layeredGraph = performDownwardLayering(graph);

        // copy all the edges.
        for (Edge e : graph.edgeSet()) {
            layeredGraph.addEdge(graph.getEdgeSource(e), graph.getEdgeTarget(e), e);
        }
        return layeredGraph;
    }

    /**
     * Perform downward layering.
     * 
     * @param graph
     *            the graph
     * @return the layered graph
     * @throws LayoutException
     *             the layout exception
     */
    private LayeredGraph performDownwardLayering(DirectedGraph<Vertex, Edge> graph)
            throws LayoutException {
        // First phase: Order the vertices by lexicographical order of
        // predecessor set.
        List<Vertex> sortedVertices = orderVertices(graph);

        // Second phase: Assign layers
        LayeredGraph layeredGraph = new LayeredGraph();
        List<Vertex> remainingVertices = new ArrayList<Vertex>(graph.vertexSet());
        int topLayer = 0;
        while (!remainingVertices.isEmpty()) {

            // Choose a vertex in remainingVertices...
            Vertex chosenVertex = null;
            int highestSuccessorLayer = -1;

            // ...maximizing the order in the sorted list...
            for (int index = sortedVertices.size() - 1; index >= 0 && chosenVertex == null; index--) {
                Vertex vertex = sortedVertices.get(index);
                if (!remainingVertices.contains(vertex)) {
                    continue;
                }

                // ...so that every successor of that vertex...
                Set<Edge> outgoingEdges = graph.outgoingEdgesOf(vertex);
                List<Vertex> successors = new ArrayList<Vertex>();
                for (Edge e : outgoingEdges) {
                    successors.add(graph.getEdgeTarget(e));
                }

                // ...have been proccessed.
                if (layeredGraph.vertexSet().containsAll(successors)) {
                    chosenVertex = vertex;
                    // keep track of highest successors layer number.
                    for (Vertex successor : successors) {
                        int successorLayer = layeredGraph.getLayer(successor);
                        if (successorLayer > highestSuccessorLayer) {
                            highestSuccessorLayer = successorLayer;
                        }
                    }
                }
            }
            if (chosenVertex == null) {
                throw new CyclicGraphException(null);
            }

            // If the top layer isn't too crowded already and that all of the
            // chosen vertex's
            // successor are on lower layer numbers...
            if (layeredGraph.verticesOnLayer(topLayer).size() < maximumLayerSize
                    && highestSuccessorLayer < topLayer) {
                // add it to the top layer.
                layeredGraph.addVertex(chosenVertex, topLayer);
            } else {
                // else, "create" a new layer to put it in.
                topLayer++;
                layeredGraph.addVertex(chosenVertex, topLayer);
            }
            remainingVertices.remove(chosenVertex);
        }
        return layeredGraph;
    }

    /**
     * Order vertices.
     * 
     * @param graph
     *            A graph to work on.
     * @return A list of all the vertices in ordered by ascending lexicographical order of
     *         predecessor set.
     */
    private List<Vertex> orderVertices(DirectedGraph<Vertex, Edge> graph) {
        List<Vertex> list = new ArrayList<Vertex>();
        Set<Vertex> remainingVertices = new LinkedHashSet<Vertex>(graph.vertexSet());

        for (int i = 0; i < graph.vertexSet().size(); i++) {
            Vertex chosenVertex = null;

            Set<Integer> smallestPredecessorSet = new LinkedHashSet<Integer>();
            // fill it so that it is "bigger" (in lexicographic order, see:
            // smallerInLexicographicOrder)
            // then any other possible vertex's predecessor set.
            smallestPredecessorSet.add(graph.vertexSet().size() + 1);

            for (Vertex vertex : remainingVertices) {
                Set<Integer> predecessorSet = buildPredecessorSet(graph, list, vertex);
                if (smallerInLexicographicOrder(predecessorSet, smallestPredecessorSet)) {
                    smallestPredecessorSet = predecessorSet;
                    chosenVertex = vertex;
                }
            }
            list.add(i, chosenVertex);
            remainingVertices.remove(chosenVertex);
        }
        return list;
    }

    /**
     * <p>
     * Used by {@link orderVertices}.
     * <p>
     * This method computes what is referred to as the <i>predecessor set</i> of <tt>vertex</tt>.
     * For each direct predecessor of <tt>vertex</tt> in graph, the integer corresponding to the
     * predecessor's index in <tt>list</tt> is inserted in the set. If a predecessor is not present
     * in <tt>list</tt>, the value (<i>number of vertex in</i> <tt>graph</tt>) is added to the set.
     * 
     * @param graph
     *            A directed-graph to work on.
     * @param list
     *            A list of already sorted vertices.
     * @param vertex
     *            A vertex in <tt>graph</tt>.
     * @return The predecessor set of assuming contains already sorted vertices.
     */
    private Set<Integer> buildPredecessorSet(DirectedGraph<Vertex, Edge> graph, List<Vertex> list,
            Vertex vertex) {
        Set<Edge> incomingEdges = graph.incomingEdgesOf(vertex);
        Set<Vertex> predecessors = new HashSet<Vertex>();
        for (Edge e : incomingEdges) {
            predecessors.add(graph.getEdgeSource(e));
        }

        Set<Integer> set = new HashSet<Integer>();
        for (Vertex predecessor : predecessors) {
            if (list.contains(predecessor)) {
                set.add(list.indexOf(predecessor));
            } else {
                set.add(graph.vertexSet().size());
            }
        }
        return set;
    }

    /**
     * <p>
     * The lexicographic order is defined as follow (where {} represents the empty set):
     * <p>
     * S has a lowest lexicographic order than T if either:
     * <ul>
     * <li>S == {} and T != {};
     * <li>S != {}, T != {} and max(S) < max(T);
     * <li>S != {}, T != {}, max(S) == max(T) and S \ {max(S)} has a lower lexicographical ordering
     * than T \ {max(T)}
     * </ul>.
     * 
     * @param s
     *            A set of integers.
     * @param t
     *            A set of integers.
     * @return Whether the set S has a lower lexicographical order than T.
     * 
     */
    private boolean smallerInLexicographicOrder(Set<Integer> s, Set<Integer> t) {
        if (s.isEmpty() && !t.isEmpty()) {
            return true;
        }
        if (!s.isEmpty() && !t.isEmpty()) {
            int sMax = Collections.max(s);
            int tMax = Collections.max(t);

            if (sMax < tMax) {
                return true;
            }
            if (sMax == tMax) {
                Set<Integer> sTruncated = new HashSet<Integer>(s);
                sTruncated.remove(sMax);
                Set<Integer> tTruncated = new HashSet<Integer>(t);
                tTruncated.remove(tMax);

                return smallerInLexicographicOrder(sTruncated, tTruncated);
            }
        }
        return false;
    }

    public int getMaximumLayerSize() {
        return maximumLayerSize;
    }

    public void setMaximumLayerSize(int maximumLayerSize) {
        this.maximumLayerSize = maximumLayerSize;
    }
}
