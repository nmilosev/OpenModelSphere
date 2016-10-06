package org.modelsphere.plugins.layout.nodes.kandinsky;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.NullArgumentException;
import org.modelsphere.jack.srtool.features.layout.graph.*;
import org.modelsphere.jack.srtool.features.layout.graph.Edge.RelationType;
import org.modelsphere.jack.srtool.features.layout.graph.HyperVertex.HyperVertexType;
import org.modelsphere.plugins.layout.nodes.kandinsky.struct.LoopEdgeRef;

/**
 * <pre>
 * Definitions:
 *  Hyperedge
 *      Edges must be admissible, for example a edge representing an inheritance.
 *      There is a hyperedge if and only if there are 2 or more admissible edges for a given vertex.
 *      
 *      Outgoing hyperedge : All edges has the same source but different target
 *      Incoming hyperedge : All edges has the same target but different source
 * </pre>
 * 
 * @author David
 * 
 */
public class Preprocessing {

    /** The graph. */
    private Graph graph = null;

    /**
     * Sets the graph to process.
     * 
     * @param graph
     *            the graph
     */
    public void setGraphToProcess(Graph graph) {
        this.graph = graph;
    }

    /**
     * Preprocessing Step 1.
     * 
     * Remove self loop edges in graph
     * 
     * From Orthogonal Graph Drawing with Constraints
     * 
     * @return List of self loop edges
     */
    public List<LoopEdgeRef> removeSelfLoopEdges() {
        if (graph == null) {
            throw new NullArgumentException(
                    "You must set the graph to process before calling this method. Use setGraphToProcess method.");
        }
        List<LoopEdgeRef> selfLoopEdges = new ArrayList<LoopEdgeRef>();

        for (Edge edge : graph.edgeSet()) {
            if (graph.getEdgeSource(edge) == graph.getEdgeTarget(edge)) {
                selfLoopEdges.add(new LoopEdgeRef(edge, graph.getEdgeSource(edge)));
                graph.removeEdge(edge);
            }
        }
        return selfLoopEdges;
    }

    /**
     * Preprocessing Step 2.
     * 
     * The edges in H are substituted by stars.
     * 
     * @return Hyperedge List
     */
    public void substituteHyperedges() {

        List<List<Edge>> incomingHyperedges = getAdmissibleIncomingEdges();
        for (List<Edge> list : incomingHyperedges) {
            substituteIncomingHyperedge(list);
        }

        List<List<Edge>> outgoingHyperedges = getAdmissibleOutgoingEdges();
        for (List<Edge> list : outgoingHyperedges) {
            substituteOutgoingHyperedge(list);
        }
    }

    /**
     * Should not be called outside of the class but for testing.
     * 
     * @param incomingEdges
     *            the incoming edges
     */
    void substituteIncomingHyperedge(List<Edge> incomingEdges) {
        Vertex hyperVertex = new HyperVertex((Edge[]) incomingEdges.toArray(),
                HyperVertexType.incoming);

        // All incomingEdges have the same target.
        Vertex target = graph.getEdgeTarget(incomingEdges.get(0));

        graph.addVertex(hyperVertex);

        for (Edge edge : incomingEdges) {
            Edge dummyEdge = Edge.buildDummyEdgeOf(edge);
            Vertex source = graph.getEdgeSource(edge);

            graph.removeEdge(edge);
            graph.addEdge(source, hyperVertex, dummyEdge);
        }

        graph.addEdge(hyperVertex, target);
    }

    /**
     * Should not be called outside of the class but for testing.
     * 
     * @param outgoingEdges
     *            the outgoing edges
     */
    void substituteOutgoingHyperedge(List<Edge> outgoingEdges) {
        Vertex hyperVertex = new HyperVertex((Edge[]) outgoingEdges.toArray(),
                HyperVertexType.outgoing);

        // All outgoinEdges have the same source.
        Vertex source = graph.getEdgeSource(outgoingEdges.get(0));

        graph.addVertex(hyperVertex);
        graph.addEdge(source, hyperVertex);

        for (Edge edge : outgoingEdges) {
            Edge dummyEdge = Edge.buildDummyEdgeOf(edge);
            Vertex target = graph.getEdgeTarget(edge);

            graph.removeEdge(edge);
            graph.addEdge(hyperVertex, target, dummyEdge);
        }
    }

    /**
     * Gets the admissible incoming edges.
     * 
     * @return the admissible incoming edges
     */
    List<List<Edge>> getAdmissibleIncomingEdges() {
        List<List<Edge>> edges = new ArrayList<List<Edge>>();

        for (Vertex vertex : graph.vertexSet()) {
            List<Edge> admissibles = new ArrayList<Edge>();

            Set<Edge> incomingEdges = graph.incomingEdgesOf(vertex);
            for (Edge edge : incomingEdges) {
                if (edge.getRelationType() == RelationType.Generalization
                        || edge.getRelationType() == RelationType.Realization) {
                    admissibles.add(edge);
                }
            }

            if (!admissibles.isEmpty()) {
                edges.add(admissibles);
            }
        }

        return edges;
    }

    /**
     * Gets the admissible outgoing edges.
     * 
     * @return the admissible outgoing edges
     */
    List<List<Edge>> getAdmissibleOutgoingEdges() {
        List<List<Edge>> edges = new ArrayList<List<Edge>>();

        for (Vertex vertex : graph.vertexSet()) {
            List<Edge> admissibles = new ArrayList<Edge>();

            Set<Edge> outgoingEdges = graph.outgoingEdgesOf(vertex);
            for (Edge edge : outgoingEdges) {
                if (edge.getRelationType() == RelationType.Dependency) {
                    admissibles.add(edge);
                }
            }

            if (!admissibles.isEmpty()) {
                edges.add(admissibles);
            }
        }

        return edges;
    }

}
