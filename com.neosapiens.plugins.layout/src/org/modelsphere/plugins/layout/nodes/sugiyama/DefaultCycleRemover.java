package org.modelsphere.plugins.layout.nodes.sugiyama;

import java.util.*;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.CycleDetector;
import org.modelsphere.jack.srtool.features.layout.LayoutException;
import org.modelsphere.jack.srtool.features.layout.graph.*;
import org.modelsphere.plugins.layout.nodes.kandinsky.struct.DirectedEdgeRef;
import org.modelsphere.plugins.layout.nodes.kandinsky.struct.LoopEdgeRef;

/**
 * Temporarily remove cycles by reverting an edge in each cycle found. (or removing the edge if it
 * is a loop)
 * 
 * @author Gabriel
 */
public class DefaultCycleRemover extends CycleRemover {

    /**
     * Keeps track of the edges that have been reverted to artificially remove a cycle in
     * <tt>removeCycles</tt> so that can be restored in <tt>restoreCycles</tt>.
     */
    private Set<DirectedEdgeRef> revertedEdges;

    /**
     * Keeps track of the edges that were removed because they were <i>loops</i> (directed edge
     * pointing from and to the same vertex). Holds the related Node and edge.
     */
    private Set<LoopEdgeRef> removedLoops;

    /**
     * Instantiates a new default cycle remover.
     */
    public DefaultCycleRemover() {
        revertedEdges = new HashSet<DirectedEdgeRef>();
        removedLoops = new HashSet<LoopEdgeRef>();
    }

    /**
     * Removes the cycles.
     * 
     * @param graph
     *            The graph to work on which may or may not contain a cycle. After the method
     *            returns, it should not contains any cycle.
     * @throws LayoutException
     *             If the method fails to remove the cycles in <tt>graph</tt>.
     */
    @Override
    public void removeCycles(Graph graph) throws LayoutException {
        if (graph == null) {
            throw new NullPointerException("graph");
        }
        if (graph.vertexSet().size() == 0) {
            return;
        }
        revertedEdges = new HashSet<DirectedEdgeRef>();
        removedLoops = new HashSet<LoopEdgeRef>();

        Set<Edge> edges = graph.edgeSet();
        int maxTry = edges.size();
        int tryCount = 0;

        removeLoops(graph);

        if (graph.vertexSet().size() == 1) {
            return;
        }

        CycleDetector<Vertex, Edge> cycleDetector = new CycleDetector<Vertex, Edge>(graph);
        while (cycleDetector.detectCycles() && tryCount < maxTry) {

            Set<Vertex> vertices = cycleDetector.findCycles();

            Iterator<Vertex> prospects = vertices.iterator();
            Vertex target = null;
            Vertex source = null;

            if (prospects.hasNext()) {
                target = prospects.next();
            }

            // from all the edges coming to 'targetNode'.
            Set<Edge> inEdges = graph.incomingEdgesOf(target);

            // find one that comes from another node in a cycle.
            for (Edge e : inEdges) {
                Vertex sourceCandidate = graph.getEdgeSource(e);
                if (vertices.contains(sourceCandidate)) {
                    if (!isRevertedEdge(sourceCandidate, target)) {
                        source = sourceCandidate;
                    }
                }
            }

            revertEdge(graph, source, target);

            // we have to instantiate a new CycleDetector or else it won't
            // consider the new graph.
            cycleDetector = new CycleDetector<Vertex, Edge>(graph);
            tryCount++;
        }

        if (tryCount == maxTry) {
            throw new LayoutException(null, "Unable to remove the cycles in the graph");
        }

        // Success.
    }

    /**
     * Restore cycles.
     * 
     * @param graph
     *            the graph
     * @throws LayoutException
     *             the layout exception
     */
    @Override
    public void restoreCycles(Graph graph) throws LayoutException {
        restoreRemovedLoops(graph);
        restoreRevertedEdges(graph);
    }

    /**
     * Restore removed loops.
     * 
     * @param graph
     *            the graph
     */
    private void restoreRemovedLoops(Graph graph) {
        for (LoopEdgeRef loopRef : removedLoops) {
            graph.addEdge(loopRef.getVertex(), loopRef.getVertex(), loopRef.getEdge());
        }
    }

    /**
     * Restore reverted edges.
     * 
     * @param graph
     *            the graph
     */
    private void restoreRevertedEdges(Graph graph) {
        for (DirectedEdgeRef ref : revertedEdges) {
            Edge e = ref.getEdge();

            // if the edge is in the graph.
            if (graph.containsEdge(e)) {
                // revert it.
                graph.removeEdge(e);
                graph.addEdge(ref.getSource(), ref.getTarget(), e);
            } else {
                // otherwise it has been replaced by a dummy-vertex path.
                List<DummyVertex> path = graph.findDummyVertexPath(ref.getTarget(), ref.getSource());
                if (path.size() > 0) {
                    // follow the path and revert each edge.
                    Vertex previous = ref.getTarget();
                    for (DummyVertex current : path) {
                        Edge revertedEdge = graph.getEdge(previous, current);
                        graph.removeEdge(revertedEdge);
                        graph.addEdge(current, previous, revertedEdge);
                        previous = current;
                    }
                    Edge revertedEdge = graph.getEdge(previous, ref.getSource());
                    graph.removeEdge(previous, ref.getSource());
                    graph.addEdge(ref.getSource(), previous, revertedEdge);
                }
            }
        }
    }

    /**
     * Revert edge.
     * 
     * @param graph
     *            the graph
     * @param sourceVertex
     *            the source vertex
     * @param targetVertex
     *            the target vertex
     */
    private void revertEdge(DirectedGraph<Vertex, Edge> graph, Vertex sourceVertex,
            Vertex targetVertex) {

        if (!graph.containsEdge(sourceVertex, targetVertex)) {
            return;
        }

        Edge e = graph.getEdge(sourceVertex, targetVertex);
        graph.removeEdge(e);
        graph.addEdge(targetVertex, sourceVertex, e);

        revertedEdges.add(new DirectedEdgeRef(e, sourceVertex, targetVertex));
    }

    /**
     * Checks if is reverted edge.
     * 
     * @param sourceVertex
     *            the source vertex
     * @param targetVertex
     *            The current target vertex of an edge in <tt>graph</tt>.
     * @return whether the current edge going from has been reverted by this cycle remover during
     *         prior treatment.
     */
    public boolean isRevertedEdge(Vertex sourceVertex, Vertex targetVertex) {
        for (DirectedEdgeRef edgeRef : revertedEdges) {
            if (edgeRef.getSource().equals(sourceVertex)
                    && edgeRef.getTarget().equals(targetVertex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if is reverted edge.
     * 
     * @param e
     *            the e
     * @return true, if is reverted edge
     */
    public boolean isRevertedEdge(Edge e) {
        for (DirectedEdgeRef edgeRef : revertedEdges) {
            if (edgeRef.getEdge().equals(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Remove every edge that is a <i>loop</i> from <tt>graph</tt>.
     * 
     * @param graph
     *            the graph
     */
    private void removeLoops(DirectedGraph<Vertex, Edge> graph) {
        Set<Edge> edges = graph.edgeSet();

        for (Edge edge : edges) {
            if (graph.getEdgeSource(edge).equals(graph.getEdgeTarget(edge))) {
                removedLoops.add(new LoopEdgeRef(edge, graph.getEdgeSource(edge)));
            }
        }
        for (LoopEdgeRef loopRef : removedLoops) {
            graph.removeEdge(loopRef.getEdge());
        }
    }

    /**
     * Gets the removed loops.
     * 
     * @return the removed loops
     */
    public Set<LoopEdgeRef> getRemovedLoops() {
        return new HashSet<LoopEdgeRef>(removedLoops);
    }

    /**
     * Gets the reverted edges.
     * 
     * @return the reverted edges
     */
    public Set<DirectedEdgeRef> getRevertedEdges() {
        return new HashSet<DirectedEdgeRef>(revertedEdges);
    }
}
