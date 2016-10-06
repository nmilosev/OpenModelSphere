/**
 * 
 */
package org.modelsphere.plugins.layout.nodes.kandinsky.planarization;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.modelsphere.jack.srtool.features.layout.graph.Edge;
import org.modelsphere.jack.srtool.features.layout.graph.Graph;
import org.modelsphere.jack.srtool.features.layout.graph.Vertex;
import org.modelsphere.jack.srtool.features.layout.graph.Edge.OrientationConstraint;
import org.modelsphere.plugins.layout.CyclicGraphException;

/**
 * Execute algorithm mixed-upward-planarization as planarization step.
 * 
 * <pre>
 * Definitions of Automatic Layout of UML Class Diagrams
 *  
 *  Directed edge
 *      source: origin vertex
 *      target: destination vertex
 *  
 *  Graph
 *      directed graph:   Contains only directed edges
 *      undirected graph: Contains only undirected edges
 *      underlying graph: undirected graph
 *      
 *      mixed-graph: A graph that contains both, directed and undirected edges
 *      st-graph:    A directed acyclic graph with exactly one sink and one source.
 *  
 *      face:   has exactly one source and one sink
 *              (is it some kind of subgraph?)
 *      source: vertex with in-degree 0 (Don't confuse it with edge source!)
 *      sink:   vertex with out-degree 0
 *  Symbole
 *      p(v): mapping a vertex to a point in the plane
 *      c(e): mapping a edge with an open Jordan curve (OMG! What is it?!)
 *      r(v): mapping a vertex to a rectangle
 *      v(f, i): mapping a face and a number with a vertex
 *      w(e): the weight of an edge
 * 
 * </pre>
 * 
 * @author David
 * 
 */
public class Planarization {

    /**
     * Referred as MUP in Automatic Layout of UML Class Diagrams p66.
     * 
     * Input: Mixed graph G = (V,Ed Union Eu), weight w : Ed U Eu -> IN Output: Mixed upward
     * planarization Gamma' of G.
     * 
     * @param graph
     *            Mixed graph G = (V,Ed U Eu), weight w : Ed U Eu -> IN
     * @return the upward plane string graph
     * @throws CyclicGraphException
     *             If the cycle removal of the graph screwed up.
     */
    public UpwardPlaneStringGraph planarizeGraph(Graph graph) throws CyclicGraphException {

        UpwardPlaneStringGraph stringGraph = UpwardPlaneStringGraph.build(graph,
                getOrderedVertices(graph));

        List<Edge> removedEdges = stringGraph.getRemovedEdges();

        // Not supported yet.
        // List<Edge> replacedUndirectedEdges =
        // directUndirectedEdges(stringGraph);

        // Insert all directed edges removed in StringGraph
        insertAllDirectedEdges(stringGraph, removedEdges);

        // Remove all dummy edges added by augmentToStGraph
        stringGraph.removeAllEdges(stringGraph.getDummyEdges());

        // Restore all undirected edges replaced by directUndirectedEdges
        // undirectTemporarilyDirectedEdges(stringGraph,
        // replacedUndirectedEdges);

        // Not supported yet.
        // insertUndirectedEdges(stringGraph, removedUndirectedEdges);
        return stringGraph;
    }

    /**
     * Replace undirected edge by directed edge in a mixedGraph.
     * 
     * @param mixedGraph
     *            the mixed graph
     * @return The list of all replaced undirected edge.
     */
    List<Edge> directUndirectedEdges(Graph mixedGraph) {

        List<Edge> undirectedEdges = new ArrayList<Edge>();
        for (Edge edge : mixedGraph.edgeSet()) {
            if (edge.getOrientationConstraint() == OrientationConstraint.Free) {
                undirectedEdges.add(edge);
            }
        }

        List<Edge> directedEdges = new ArrayList<Edge>();
        List<Vertex> order = getOrderedVertices(mixedGraph);

        for (Edge edge : undirectedEdges) {
            int sourceOrderIndex = order.indexOf(mixedGraph.getEdgeSource(edge));
            int targetOrderIndex = order.indexOf(mixedGraph.getEdgeTarget(edge));

            Vertex source = mixedGraph.getEdgeSource(edge);
            Vertex target = mixedGraph.getEdgeTarget(edge);
            // The undirected edge is removed from the graph.
            mixedGraph.removeEdge(edge);

            // The equivalent directed edge is added to the graph according to
            // vertexOrder
            if (sourceOrderIndex < targetOrderIndex) {
                edge.setOrientationConstraint(OrientationConstraint.Upward);
                mixedGraph.addEdge(source, target, edge);
                directedEdges.add(edge);
            }
            if (sourceOrderIndex > targetOrderIndex) {
                // TODO: on ne retient pas que le edge a ete change de direction
                // The edge direction is reversed,
                // edge's target should always be the biggest order index.
                edge.setOrientationConstraint(OrientationConstraint.Upward);
                mixedGraph.addEdge(target, source, edge);
                directedEdges.add(edge);
            }
        }
        return directedEdges;
    }

    /**
     * Undirect temporarily directed edges.
     * 
     * @param mixedGraph
     *            the mixed graph
     * @param replacedUndirectedEdges
     *            the replaced undirected edges
     */
    void undirectTemporarilyDirectedEdges(DirectedGraph<Vertex, Edge> mixedGraph,
            List<Edge> replacedUndirectedEdges) {

        for (Edge edge : replacedUndirectedEdges) {
            // the representation of the undirected edge is removed
            Vertex source = mixedGraph.getEdgeSource(edge);
            Vertex target = mixedGraph.getEdgeTarget(edge);

            mixedGraph.removeEdge(edge);
            edge.setOrientationConstraint(OrientationConstraint.Free);
            mixedGraph.addEdge(source, target, edge);

            // TODO : the real undirected edge is added to the graph
            // p66
        }
    }

    /**
     * Insert undirected edges.
     * 
     * @param mixedGraph
     *            the mixed graph
     * @param undirectedEdges
     *            the undirected edges
     */
    void insertUndirectedEdges(DirectedGraph<Vertex, Edge> mixedGraph, List<Edge> undirectedEdges) {
        for (Edge edge : undirectedEdges) {
            insertUndirectedEdge(mixedGraph, edge);
        }

        // TODO: Reroute undirected edges
    }

    /**
     * Referred as UEI in Automatic Layout of UML Class Diagrams.
     * 
     * @param mixedGraph
     *            the mixed graph
     * @param edge
     *            the edge
     */
    void insertUndirectedEdge(DirectedGraph<Vertex, Edge> mixedGraph, Edge edge) {
        // TODO
    }

    /**
     * Referred as MVO in Automatic Layout of UML Class Diagrams
     * 
     * The first vertex in PI is a vertex with minimal degree in G. After the first k vertices of
     * the ordering have been determined, say v_1, v_2, ..., v_k, the next vertex v_(k+1) is
     * selected from the vertices adjacent to v_k in G having the least adjacencies in the subgraph
     * G_k of G induced by V \ {v_1, v_2, ..., v_k}. If there is no vertex in G_k adjacent to v_k in
     * G, then v_(k+1) is selected as a minimum degree vertex in G_k.
     * 
     * Input: A mixed graph G = (V,E_d union E_u) Output: An ordering PI on the vertices
     * 
     * @param graph
     *            the graph
     * @return An ordering Pi on the vertices
     */
    List<Vertex> getOrderedVertices(Graph graph) {
        List<Vertex> vertexOrder = new ArrayList<Vertex>();

        if (graph.vertexSet().isEmpty()) {
            return vertexOrder;
        }

        Vertex minimalDegreeVertex = graph.getVertexWithMinimalDegree();

        // The first vertex is a vertex with minimal degree.
        vertexOrder.add(minimalDegreeVertex);

        // The next vertex is the one adjacent to the last vertexe v_k and with
        // the least
        // adjacencies excluding all previous vertices.
        Set<Vertex> remainingVertices = new LinkedHashSet<Vertex>();
        remainingVertices.addAll(graph.vertexSet());
        remainingVertices.remove(minimalDegreeVertex);

        Vertex lastVertex = minimalDegreeVertex;
        while (!remainingVertices.isEmpty()) {

            GraphSubgraph remainingVerticesSubgraph = new GraphSubgraph(graph, remainingVertices,
                    null);

            Collection<Vertex> neighbors = graph.neighborsOf(lastVertex);

            int leastNeighbors = Integer.MAX_VALUE;
            Vertex leastNeighborsVertex = null;
            for (Vertex vertex : neighbors) {

                if (remainingVerticesSubgraph.containsVertex(vertex)) {
                    int neighborCount = remainingVerticesSubgraph.neighborCount(vertex);
                    if (neighborCount < leastNeighbors) {
                        leastNeighbors = neighborCount;
                        leastNeighborsVertex = vertex;
                    }
                }
            }

            if (leastNeighborsVertex == null) {
                leastNeighborsVertex = remainingVerticesSubgraph.getVertexWithMinimalDegree();
            }

            vertexOrder.add(leastNeighborsVertex);
            remainingVertices.remove(leastNeighborsVertex);
            lastVertex = leastNeighborsVertex;
        }

        return vertexOrder;
    }

    /**
     * Insert all removed directed edges from createEmbeddedMixedUpwardPlanarSubgraphOf.
     * 
     * @param planeGraph
     *            the embedded planar subgraph
     * @param directedEdgesToInsert
     *            the directed edges to insert
     * @throws CyclicGraphException
     *             If the graph given in parameter contains cycle or if insertDirectedEdge screwed
     *             up and created cycle in the graph.
     */
    void insertAllDirectedEdges(UpwardPlaneStringGraph planeGraph, List<Edge> directedEdgesToInsert)
            throws CyclicGraphException {

        // TODO : Compute embedding of G' from Pi
        for (Edge edge : directedEdgesToInsert) {
            if (!planeGraph.getBaseGraph().containsEdge(edge)) {
                throw new InvalidParameterException(
                        "All edges to insert must be present in the base graph of this embeddedPlanarSubgraph.");
            }
            if (planeGraph.containsEdge(edge)) {
                throw new InvalidParameterException(
                        "The embeddedPlanarSubgraph must not contains any edge to insert.");
            }
        }

        List<Edge> remainingEdges = new ArrayList<Edge>();
        remainingEdges.addAll(directedEdgesToInsert);

        for (Edge edge : directedEdgesToInsert) {
            insertDirectedEdge(planeGraph, edge);
            remainingEdges.remove(edge);
        }

        // TODO: Reroute directed edges
    }

    /**
     * Referred as DEI in Automatic Layout of UML Class Diagrams.
     * 
     * We have to avoid cycles when we insert edges. We avoid this by layering the graph. We define
     * a valid layering l as a mapping of V to integers such that l(v) > l(u) for each edge (u, v)
     * in E union R
     * 
     * @param planeGraph
     *            An upward embedded graph G = (V,E,F)
     * @param edgeToInsert
     *            The directed edge to insert in the graph
     * @return Embedded upward planarized st-graph with edge inserted.
     */
    void insertDirectedEdge(UpwardPlaneStringGraph planeGraph, Edge edgeToInsert) {

        if (!planeGraph.getBaseGraph().containsEdge(edgeToInsert)) {
            throw new InvalidParameterException(
                    "The edge to insert must be in the base graph of upwardPlanarEbeddingGraph.");
        }

        planeGraph.addRemovedEdge(edgeToInsert);
    }

}
