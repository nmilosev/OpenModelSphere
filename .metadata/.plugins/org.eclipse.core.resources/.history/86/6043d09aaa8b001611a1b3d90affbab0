/**
 * 
 */
package org.modelsphere.plugins.layout.nodes.kandinsky.planarization;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.modelsphere.jack.srtool.features.layout.graph.DummyVertex;
import org.modelsphere.jack.srtool.features.layout.graph.Edge;
import org.modelsphere.jack.srtool.features.layout.graph.Graph;
import org.modelsphere.jack.srtool.features.layout.graph.Vertex;

/**
 * Referred as st-graph in Automatic Layout of UML Class Diagrams.</p>
 * 
 * A directed acyclic graph with exactly one sink and one source.<br>
 * In this kind of graph vertices are disposed on a line in a specific order. Edges are above the
 * line or below the line. This graph is a plane graph representation of his base graph. Which means
 * that this graph is always planar.<br>
 * Some edges may be removed to obtains this result.
 * 
 * @author David
 * 
 */
public class UpwardPlaneStringGraph extends Graph {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2248429332188511890L;

    /**
     * The Enum DartDirection.
     * 
     * @see Dart
     * @author Gabriel
     */
    public enum DartDirection {

        /** The SAME. */
        SAME,
        /** The OPPOSITE. */
        OPPOSITE
    }

    /**
     * <p>
     * Represents a Dart in a plane graph.
     * <p>
     * For each edge inserted in this graph, two darts are created, one pointing the same way as the
     * edge and one pointing the opposite way.
     * <p>
     * Those darts are used to describe a {@link Face}.
     * <p>
     * Reference: Section 2.1.4
     * 
     * @author Gabriel
     * 
     */
    public class Dart {

        /** The edge. */
        private Edge edge;

        /** The direction. */
        private DartDirection direction;

        /**
         * Creates a new instance of <tt>Dart</tt>.
         * 
         * @param edge
         *            The edge it is referring to.
         * @param direction
         *            Whether the <tt>Dart</tt> is in the same direction as the edge or in the
         *            opposite.
         */
        public Dart(Edge edge, DartDirection direction) {
            if (edge == null) {
                throw new NullPointerException("edge");
            }
            if (direction == null) {
                throw new NullPointerException("direction");
            }
            this.edge = edge;
            this.direction = direction;
        }

        /**
         * Gets the source.
         * 
         * @return this 's source.
         */
        public Vertex getSource() {
            if (direction == DartDirection.SAME) {
                return getEdgeSource(edge);
            } else {
                return getEdgeTarget(edge);
            }
        }

        /**
         * Gets the target.
         * 
         * @return this 's target.
         */
        public Vertex getTarget() {
            if (direction == DartDirection.SAME) {
                return getEdgeTarget(edge);
            } else {
                return getEdgeSource(edge);
            }
        }

        /**
         * Gets the edge.
         * 
         * @return this 's corresponding edge.
         */
        public Edge getEdge() {
            return edge;
        }

        /**
         * Gets the direction.
         * 
         * @return this .
         */
        public DartDirection getDirection() {
            return direction;
        }
    }

    /**
     * <p>
     * Each face in this graph is represented by the cyclic list of dart encountered when walking
     * around the face in clockwise order.
     * <p>
     * By definition, the first face is the outer one.
     * <p>
     * Reference: Section 2.1.4
     * 
     * @author Gabriel
     */
    public class Face {
        /**
         * The list of darts encountered when walking around the face in clockwise order.
         */
        private Dart[] darts;

        /**
         * Creates a new instance of <tt>Face</tt>.
         * 
         * @param darts
         *            Specifies the darts defining the face.
         */
        public Face(Dart[] darts) {
            if (darts == null) {
                throw new NullPointerException("darts");
            }
            if (darts.length == 0) {
                throw new RuntimeException("The dart array must not be empty.");
            }
            this.darts = darts;
        }

        /**
         * Gets the darts.
         * 
         * @return the darts
         */
        public Dart[] getDarts() {
            return darts;
        }

        /**
         * Gets the dart.
         * 
         * @param i
         *            the i
         * @return the dart
         */
        public Dart getDart(int i) {
            return darts[i];
        }

        /**
         * Gets the dart count.
         * 
         * @return the dart count
         */
        public int getDartCount() {
            return darts.length;
        }
    }

    /**
     * <p>
     * Darts in this graph.
     */
    private List<Dart> darts;

    /**
     * <p>
     * Faces in this graph.
     * <p>
     * By definition, the first face should by the outer face.
     */
    private List<Face> faces;

    /** The base. */
    private Graph base;

    /** The ordered vertices. */
    private List<Vertex> orderedVertices;

    /** The above edges. */
    private List<Edge> aboveEdges;

    /** The below edges. */
    private List<Edge> belowEdges;

    /** The removed edges. */
    private List<Edge> removedEdges;

    /** The dummy edges. */
    private List<Edge> dummyEdges;

    /**
     * StringGraph constructor is private because it is a complex task to build a StringGraph. Use
     * StringGraph.build method instead.
     */
    private UpwardPlaneStringGraph() {
        aboveEdges = new ArrayList<Edge>();
        belowEdges = new ArrayList<Edge>();
        removedEdges = new ArrayList<Edge>();
        dummyEdges = new ArrayList<Edge>();
        this.darts = null;
        this.faces = null;
    }

    /**
     * For testing only. Don't use this constructor in application environment.
     * 
     * @param base
     *            the base
     * @param orderedVertices
     *            the ordered vertices
     */
    UpwardPlaneStringGraph(Graph base, List<Vertex> orderedVertices) {
        this();
        this.base = base;
        this.orderedVertices = orderedVertices;
    }

    /**
     * Gets the base graph.
     * 
     * @return the base
     */
    public Graph getBaseGraph() {
        return base;
    }

    /**
     * Gets the ordered vertices.
     * 
     * @return the orderedVertices
     */
    public List<Vertex> getOrderedVertices() {
        return orderedVertices;
    }

    /**
     * Gets the above edges.
     * 
     * @return the above
     */
    public List<Edge> getAboveEdges() {
        return aboveEdges;
    }

    /**
     * Gets the below edges.
     * 
     * @return the below
     */
    public List<Edge> getBelowEdges() {
        return belowEdges;
    }

    /**
     * Gets the removed edges.
     * 
     * @return the removed
     */
    public List<Edge> getRemovedEdges() {
        return removedEdges;
    }

    /**
     * Gets the dummy edges.
     * 
     * @return the dummyEdges
     */
    public List<Edge> getDummyEdges() {
        return dummyEdges;
    }

    /**
     * Adds the removed edge.
     * 
     * @param edgeToInsert
     *            the edge to insert
     */
    public void addRemovedEdge(Edge edgeToInsert) {
        if (!removedEdges.contains(edgeToInsert)) {
            throw new IllegalArgumentException("The edge to insert must always be a removedEdges.");
        }

        List<Edge> crossingEdges = getCrossingEdgesInBaseGraph(this, getBelowEdges(), edgeToInsert);

        Vertex source = getBaseGraph().getEdgeSource(edgeToInsert);
        Vertex target = getBaseGraph().getEdgeTarget(edgeToInsert);

        // Assume that a crossing is made by a pair of edges
        for (Edge crossingEdge : crossingEdges) {
            addEdge(source, target, edgeToInsert);
            replaceCrossingByDummyVertex(crossingEdge, edgeToInsert);

            belowEdges.remove(crossingEdge);
        }
        removedEdges.remove(edgeToInsert);
    }

    /**
     * Replace crossing by dummy vertex.
     * 
     * @param crossingEdge
     *            the crossing edge
     * @param crossedEdge
     *            the crossed edge
     */
    void replaceCrossingByDummyVertex(Edge crossingEdge, Edge crossedEdge) {

        Vertex crossedEdgeSource = getEdgeSource(crossedEdge);
        Vertex crossedEdgeTarget = getEdgeTarget(crossedEdge);

        Vertex crossingEdgeSource = getEdgeSource(crossingEdge);
        Vertex crossingEdgeTarget = getEdgeTarget(crossingEdge);

        Vertex dummyVertex = new DummyVertex(new Edge[] { crossingEdge, crossedEdge });
        addVertex(dummyVertex);

        removeEdge(crossingEdge);
        removeEdge(crossedEdge);

        Edge dummyCrossedEdge1 = Edge.buildDummyEdgeOf(crossedEdge);
        Edge dummyCrossedEdge2 = Edge.buildDummyEdgeOf(crossedEdge);

        Edge dummyCrossingEdge1 = Edge.buildDummyEdgeOf(crossingEdge);
        Edge dummyCrossingEdge2 = Edge.buildDummyEdgeOf(crossingEdge);

        addEdge(crossedEdgeSource, dummyVertex, dummyCrossedEdge1);
        addEdge(dummyVertex, crossedEdgeTarget, dummyCrossedEdge2);

        addEdge(crossingEdgeSource, dummyVertex, dummyCrossingEdge1);
        addEdge(dummyVertex, crossingEdgeTarget, dummyCrossingEdge2);

        int crossedEdgeTargetOrder = orderedVertices.indexOf(crossedEdgeTarget);
        int crossingEdgeTargetOrder = orderedVertices.indexOf(crossingEdgeTarget);

        // Insert the new dummy vertex in orderedVertices
        if (crossedEdgeTargetOrder > crossingEdgeTargetOrder) {
            orderedVertices.add(crossingEdgeTargetOrder, dummyVertex);
        } else if (crossedEdgeTargetOrder < crossingEdgeTargetOrder) {
            orderedVertices.add(crossedEdgeTargetOrder, dummyVertex);
        } else {
            throw new InvalidParameterException(
                    "crossedEdgeTargetOrder can not be the same as crossingEdgeTargetOrder.");
        }

        // Add dummy edges above or below the vertices line.
        Edge[] dummyEdges = new Edge[] { dummyCrossedEdge1, dummyCrossedEdge2, dummyCrossingEdge1,
                dummyCrossingEdge2 };

        for (int i = 0; i < dummyEdges.length; i++) {
            Edge edge = dummyEdges[i];

            List<Edge> crossingEdges = getCrossingEdges(getAboveEdges(), edge);

            // If possible add the edge above else add it below
            if (crossingEdges.isEmpty()) {
                aboveEdges.add(edge);
            } else {
                belowEdges.add(edge);
            }
        }
    }

    /**
     * This method build a StringGraph of base graph using a specific vertex order given by
     * verticesOrder.
     * 
     * @param base
     *            The graph from which we want to obtain a string graph.
     * @param verticesOrder
     *            Contains the base graph vertices order. The quality of builded StringGraph depend
     *            strongly of this order.
     * @return A StringGraph of base graph.
     */
    public static UpwardPlaneStringGraph build(Graph base, List<Vertex> verticesOrder) {
        UpwardPlaneStringGraph stringGraph = new UpwardPlaneStringGraph();

        stringGraph.base = base;
        stringGraph.orderedVertices = verticesOrder;

        makePlaneGraph(stringGraph);
        augmentToStringGraph(stringGraph);

        return stringGraph;
    }

    /**
     * A directed acyclic graph is called an st-graph if it has exactly one sink and one source.
     * 
     * Again we use the ordering PI for this task. Assume there is a sink v in the graph with PI(v)
     * = k < n. Then we insert a directed edge (v,w) with PI(w) = k+1 into G'. Analogously we insert
     * a directed edge (w, v) with PI(w) = k - 1 into G' if v is a source with PI(v) > 1. Clearly G'
     * is an st-graph after this step. We need to perform the st-completion only once before the
     * edge insertion process since Lemma 3.3 guarantees that the graph remains an st-graph after
     * inserting an edge.
     * 
     * @param graph
     *            the graph
     * @return the list
     */
    static void augmentToStringGraph(UpwardPlaneStringGraph graph) {

        List<Edge> augmentedEdges = graph.dummyEdges;
        List<Vertex> vertexOrder = graph.orderedVertices;

        // To make a st-graph we must conserve only one sink.
        List<Vertex> sinks = graph.getAllSink();
        int i = sinks.size() - 1;
        while (sinks.size() > 1) {
            Vertex sink = sinks.get(i);

            int k = vertexOrder.indexOf(sink);
            if (k < vertexOrder.size() - 1) {
                Vertex wTarget = vertexOrder.get(k + 1);
                Edge edge = graph.addEdge(sink, wTarget);

                augmentedEdges.add(edge);
                sinks.remove(sink);
            }
            i--;
        }

        // To make a st-graph we must conserve only one source.
        List<Vertex> sources = graph.getAllSource();
        i = sources.size() - 1;
        while (sources.size() > 1) {
            Vertex source = sources.get(i);

            int k = vertexOrder.indexOf(source);
            if (k > 0) {
                Vertex wSource = vertexOrder.get(k - 1);
                Edge edge = graph.addEdge(wSource, source);

                augmentedEdges.add(edge);
                sources.remove(source);
            }
            i--;
        }
    }

    /**
     * Referred as MGT in Automatic Layout of UML Class Diagrams
     * 
     * Algorithm MGT computes MaxIterations vertex-orders and computes for each vertex order a
     * mixed-upward-planar subgraph.
     * 
     * The edges in the graph are weighted according to model the difference of importance of
     * different types of edges.
     * 
     * In a planarization of a weighted graph, the weight of a crossing is the product of the weight
     * of the crossing edges.
     * 
     * @param stringGraph
     *            the graph
     * @return The mixed-upward planar subgraph with the greatest weight. The weight of a graph is
     *         sum of the weights given to all edges.
     */
    static void makePlaneGraph(UpwardPlaneStringGraph stringGraph) {
        buildIndependentSets(stringGraph);

        for (Vertex vertex : stringGraph.base.vertexSet()) {
            stringGraph.addVertex(vertex);
        }

        for (Edge edge : stringGraph.aboveEdges) {
            Vertex source = stringGraph.base.getEdgeSource(edge);
            Vertex target = stringGraph.base.getEdgeTarget(edge);
            stringGraph.addEdge(source, target, edge);
        }
        for (Edge edge : stringGraph.belowEdges) {
            Vertex source = stringGraph.base.getEdgeSource(edge);
            Vertex target = stringGraph.base.getEdgeTarget(edge);
            stringGraph.addEdge(source, target, edge);
        }
    }

    /**
     * Builds the independent sets.
     * 
     * @param stringGraph
     *            the graph
     */
    static void buildIndependentSets(UpwardPlaneStringGraph stringGraph) {

        List<Edge> above = stringGraph.aboveEdges;
        List<Edge> below = stringGraph.belowEdges;
        List<Edge> removed = stringGraph.removedEdges;

        // Order edges by their number of crossing
        PriorityQueueExtend<Edge, Integer> orderedEdges = getEdgesOrderedByCrossing(stringGraph);

        for (QueueElement<Edge, Integer> queueElement : orderedEdges) {
            above.add(queueElement.getElement());
        }

        // Dispatch edges between above, below and removed if crossing remain.
        while (!orderedEdges.isEmpty()) {
            QueueElement<Edge, Integer> queueElement = orderedEdges.peek();
            Edge edge = queueElement.getElement();

            if (queueElement.getComparableValue() > 0) {
                List<Edge> crossedEdgesAbove = getCrossingEdgesInBaseGraph(stringGraph, above, edge);
                List<Edge> crossedEdgesBelow = getCrossingEdgesInBaseGraph(stringGraph, below, edge);

                if (crossedEdgesBelow.size() > 0) {

                    boolean bestEdgeFound = false;
                    PriorityQueueExtend<Edge, Integer> orderedCrossedEdges = getEdgesOrderedByCrossing(
                            stringGraph, crossedEdgesAbove);

                    while (!orderedCrossedEdges.isEmpty()) {
                        QueueElement<Edge, Integer> crossedElement = orderedCrossedEdges.poll();
                        Edge crossedEdge = crossedElement.getElement();

                        crossedEdgesAbove = getCrossingEdgesInBaseGraph(stringGraph, above,
                                crossedEdge);
                        crossedEdgesBelow = getCrossingEdgesInBaseGraph(stringGraph, below,
                                crossedEdge);

                        if (crossedEdgesBelow.isEmpty()) {
                            above.remove(crossedEdge);
                            below.add(crossedEdge);
                            orderedEdges.removeElement(crossedEdge);
                            bestEdgeFound = true;
                            break;
                        }
                    }

                    if (!bestEdgeFound) {
                        crossedEdgesAbove = getCrossingEdgesInBaseGraph(stringGraph, above, edge);
                        above.remove(edge);
                        removed.add(edge);
                        orderedEdges.poll();
                    }

                } else {
                    above.remove(edge);
                    below.add(edge);
                    orderedEdges.poll();
                }

                // Update all above crossed edges, their crossing will be minus
                // by 1.
                updateCrossedEdges(orderedEdges, crossedEdgesAbove);
            } else {
                orderedEdges.poll();
            }
        }
    }

    /**
     * Gets the crossing edges.
     * 
     * @param edges
     *            the edges
     * @param crossedEdge
     *            the crossed edge
     * @return the crossing edges
     */
    List<Edge> getCrossingEdges(List<Edge> edges, Edge crossedEdge) {
        return getCrossingEdges(this, orderedVertices, edges, crossedEdge);
    }

    /**
     * Gets the crossed edges.
     * 
     * @param stringGraph
     *            the string graph
     * @param edges
     *            the edges
     * @param crossedEdge
     *            the edge
     * @return the crossed edges
     */
    static List<Edge> getCrossingEdgesInBaseGraph(UpwardPlaneStringGraph stringGraph,
            List<Edge> edges, Edge crossedEdge) {

        return getCrossingEdges(stringGraph.base, stringGraph.orderedVertices, edges, crossedEdge);
    }

    /**
     * Gets the crossing edges.
     * 
     * @param graph
     *            the graph
     * @param orderedVertices
     *            the ordered vertices
     * @param edges
     *            the edges
     * @param crossedEdge
     *            the crossed edge
     * @return the crossing edges
     */
    static List<Edge> getCrossingEdges(Graph graph, List<Vertex> orderedVertices, List<Edge> edges,
            Edge crossedEdge) {

        if (!graph.containsEdge(crossedEdge)) {
            throw new InvalidParameterException("Edge must be in the graph.");
        }

        for (Edge edge : edges) {
            if (!graph.containsEdge(edge)) {
                throw new InvalidParameterException("All edges must be in the graph.");
            }
        }

        for (Vertex vertex : orderedVertices) {
            if (!graph.containsVertex(vertex)) {
                throw new InvalidParameterException("All vertices must be in the graph.");
            }
        }

        List<Edge> crossingEdges = new ArrayList<Edge>();

        int sourceIndex = orderedVertices.indexOf(graph.getEdgeSource(crossedEdge));
        int targetIndex = orderedVertices.indexOf(graph.getEdgeTarget(crossedEdge));

        int startIndex = sourceIndex;
        int endIndex = targetIndex;
        if (sourceIndex > targetIndex) {
            startIndex = targetIndex;
            endIndex = sourceIndex;
        }

        for (int i = startIndex + 1; i < endIndex; i++) {
            Vertex vertex = orderedVertices.get(i);

            for (Vertex neighbor : graph.neighborsOf(vertex)) {
                int neighborIndex = orderedVertices.indexOf(neighbor);

                if (neighborIndex < startIndex || neighborIndex > endIndex) {
                    Edge crossingEdge = graph.getEdge(vertex, neighbor);
                    if (crossingEdge == null) {
                        crossingEdge = graph.getEdge(neighbor, vertex);
                    }
                    if (edges.contains(crossingEdge)) {
                        crossingEdges.add(crossingEdge);
                    }
                }
            }
        }
        return crossingEdges;
    }

    /**
     * Update crossed edges.
     * 
     * @param orderedEdges
     *            the ordered edges
     * @param crossedEdges
     *            the crossed edges
     */
    static void updateCrossedEdges(PriorityQueueExtend<Edge, Integer> orderedEdges,
            List<Edge> crossedEdges) {

        if (!orderedEdges.containsAllElements(crossedEdges)) {
            throw new InvalidParameterException("All crossed edges must be in orderedEdges.");
        }

        List<QueueElement<Edge, Integer>> elements = new ArrayList<QueueElement<Edge, Integer>>();

        for (QueueElement<Edge, Integer> queueElement : orderedEdges) {
            Edge edge = queueElement.getElement();
            Integer crossing = queueElement.getComparableValue();

            if (crossedEdges.contains(edge)) {
                if (crossing < 1) {
                    throw new InvalidParameterException(
                            "All crossedEdges must have at least one crossing.");
                }
                elements.add(queueElement);
            }
        }

        for (QueueElement<Edge, Integer> queueElement : elements) {
            Edge edge = queueElement.getElement();
            Integer crossing = queueElement.getComparableValue();

            orderedEdges.setValue(edge, crossing - 1);
        }
    }

    /**
     * Gets the edges ordered by crossing.
     * 
     * @param stringGraph
     *            the string graph
     * @return the edges ordered by crossing
     */
    static PriorityQueueExtend<Edge, Integer> getEdgesOrderedByCrossing(
            UpwardPlaneStringGraph stringGraph) {

        Graph graph = stringGraph.base;
        List<Vertex> orderedVertices = stringGraph.orderedVertices;

        Comparator<QueueElement<Edge, Integer>> comparator = Collections.reverseOrder();
        PriorityQueueExtend<Edge, Integer> orderedEdges = new PriorityQueueExtend<Edge, Integer>(
                comparator);

        for (Vertex vertex : orderedVertices) {
            if (!graph.containsVertex(vertex)) {
                throw new InvalidParameterException(
                        "orderedVertices contains a vertex that is not in graph.");
            }
        }

        List<Edge> allEdges = new ArrayList<Edge>(graph.edgeSet());

        for (Vertex vertex : orderedVertices) {
            for (Edge edge : graph.edgesOf(vertex)) {
                if (!orderedEdges.containsElement(edge)) {
                    List<Edge> crossedEdges = getCrossingEdgesInBaseGraph(stringGraph, allEdges,
                            edge);
                    orderedEdges.add(edge, crossedEdges.size());
                }
            }
        }
        return orderedEdges;
    }

    /**
     * Gets the edges ordered by crossing.
     * 
     * @param stringGraph
     *            the string graph
     * @param onlyThoseEdges
     *            the only those edges
     * @return the edges ordered by crossing
     */
    static PriorityQueueExtend<Edge, Integer> getEdgesOrderedByCrossing(
            UpwardPlaneStringGraph stringGraph, List<Edge> onlyThoseEdges) {

        Graph graph = stringGraph.base;
        List<Vertex> orderedVertices = stringGraph.orderedVertices;

        Comparator<QueueElement<Edge, Integer>> comparator = Collections.reverseOrder();
        PriorityQueueExtend<Edge, Integer> orderedEdges = new PriorityQueueExtend<Edge, Integer>(
                comparator);

        for (Vertex vertex : orderedVertices) {
            if (!graph.containsVertex(vertex)) {
                throw new InvalidParameterException(
                        "orderedVertices contains a vertex that is not in graph.");
            }
        }

        List<Edge> allEdges = new ArrayList<Edge>(graph.edgeSet());

        for (Vertex vertex : orderedVertices) {
            for (Edge edge : graph.edgesOf(vertex)) {
                if (!orderedEdges.containsElement(edge)) {
                    if (onlyThoseEdges.contains(edge)) {
                        List<Edge> crossedEdges = getCrossingEdgesInBaseGraph(stringGraph,
                                allEdges, edge);
                        orderedEdges.add(edge, crossedEdges.size());
                    }
                }
            }
        }
        return orderedEdges;
    }

    /**
     * Gets the darts.
     * 
     * @return The list of darts in the graph. If the list has not been created, create it. Every
     *         subsequent calls will return the same list.
     */
    public List<Dart> getDarts() {
        if (this.darts == null) {
            buildDarts();
        }
        return this.darts;
    }

    /**
     * Builds the darts.
     */
    private void buildDarts() {
        this.darts = new ArrayList<Dart>(edgeSet().size() * 2);
        for (Edge e : edgeSet()) {
            this.darts.add(new Dart(e, DartDirection.SAME));
            this.darts.add(new Dart(e, DartDirection.OPPOSITE));
        }
    }

    /**
     * Gets the faces.
     * 
     * @return The list of faces in the graph, If the list has not been created, create it. Evey
     *         subsequent calls will return the same list.
     */
    public List<Face> getFaces() {
        if (this.faces == null) {
            buildFaces();
        }
        return this.faces;
    }

    /**
     * Builds the faces.
     */
    private void buildFaces() {
        this.faces = new ArrayList<Face>();

        int vertexCount = vertexSet().size();
        if (vertexCount > 0 && edgeSet().size() >= 2) {
            List<Dart> faceDarts = new ArrayList<Dart>();

            // FIRST the outer face.

            // start with the sink.
            Vertex sink = this.orderedVertices.get(vertexCount - 1);

            // going downward (and below if applicable)
            Dart dart = findLongestDartGoingDownwardFrom(sink);
            Vertex currentVertex = sink;
            while (dart != null) {
                faceDarts.add(dart);
                currentVertex = dart.getTarget();
                dart = findLongestDartGoingDownwardFrom(currentVertex);
            }

            // we should now be at the source.
            assert (currentVertex.equals(this.orderedVertices.get(0)));

            // now we go upward (and above if applicable).
            dart = findLongestDartGoingUpwardFrom(currentVertex);
            while (dart != null) {
                faceDarts.add(dart);
                currentVertex = dart.getTarget();
                dart = findLongestDartGoingUpwardFrom(currentVertex);
            }

            this.faces.add(new Face(faceDarts.toArray(new Dart[0])));

            // TODO continue to code. Only the first (outer) face is processed.
        }
    }

    /**
     * Find longest dart going downward from.
     * 
     * @param v
     *            A vertex in this graph.
     * @return The longest Dart that can be found originating from and going to a another vertex
     *         with a lower order than (downward). Darts that are made from edges below are
     *         prioritized.
     */
    private Dart findLongestDartGoingDownwardFrom(Vertex v) {
        Set<Edge> edges = new LinkedHashSet<Edge>(outgoingEdgesOf(v));
        edges.addAll(incomingEdgesOf(v));

        Set<Edge> below = new LinkedHashSet<Edge>();
        Set<Edge> above = new LinkedHashSet<Edge>();

        for (Edge e : edges) {
            if (this.belowEdges.contains(e)) {
                below.add(e);
            } else {
                above.add(e);
            }
        }

        int startIndex = this.orderedVertices.indexOf(v);

        int maxLength = Integer.MIN_VALUE;
        Dart chosenDart = null;

        // if we have below edges (for that vertex), look among those...
        Set<Edge> selectedSet = null;
        if (below.size() != 0) {
            selectedSet = below;
        } else {
            selectedSet = above;
        }

        for (Edge e : selectedSet) {
            Vertex other = getOpposite(v, e);

            int otherIndex = this.orderedVertices.indexOf(other);

            if (otherIndex < startIndex) {
                int length = startIndex - otherIndex;
                if (length > maxLength) {
                    maxLength = length;
                    chosenDart = getDart(v, other);
                }
            }
        }
        return chosenDart;
    }

    /**
     * Find longest dart going upward from.
     * 
     * @param v
     *            A vertex in this graph.
     * @return The longest Dart that can be found originating from and going to a another vertex
     *         with a higher order than (upward). Darts that are made from edges above are
     *         prioritized.
     */
    private Dart findLongestDartGoingUpwardFrom(Vertex v) {
        Set<Edge> edges = new LinkedHashSet<Edge>(outgoingEdgesOf(v));
        edges.addAll(incomingEdgesOf(v));

        Set<Edge> below = new LinkedHashSet<Edge>();
        Set<Edge> above = new LinkedHashSet<Edge>();

        for (Edge e : edges) {
            if (this.belowEdges.contains(e)) {
                below.add(e);
            } else {
                above.add(e);
            }
        }

        int startIndex = this.orderedVertices.indexOf(v);

        int maxLength = Integer.MIN_VALUE;
        Dart chosenDart = null;

        // if we have above edges (for that vertex), look among those...
        Set<Edge> selectedSet = null;
        if (above.size() != 0) {
            selectedSet = above;
        } else {
            selectedSet = below;
        }

        for (Edge e : selectedSet) {
            Vertex other = getOpposite(v, e);

            int otherIndex = this.orderedVertices.indexOf(other);

            if (otherIndex > startIndex) {
                int length = otherIndex - startIndex;
                if (length > maxLength) {
                    maxLength = length;
                    chosenDart = getDart(v, other);
                }
            }
        }
        return chosenDart;
    }

    /**
     * Gets the dart.
     * 
     * @param sourceVertex
     *            the source vertex
     * @param targetVertex
     *            the target vertex
     * @return The dart going from to . Otherwise, returns null.
     */
    public Dart getDart(Vertex sourceVertex, Vertex targetVertex) {
        if (this.darts == null) {
            buildDarts();
        }

        DartDirection direction = DartDirection.SAME;
        Edge edge = getEdge(sourceVertex, targetVertex);
        if (edge == null) {
            direction = DartDirection.OPPOSITE;
            edge = getEdge(targetVertex, sourceVertex);
            if (edge == null) {
                throw new RuntimeException(
                        "no edge linking \"sourceVertex\" and \"targetVertex\" together, hence no dart can be found");
            }
        }

        for (Dart d : this.darts) {
            if (d.getEdge() == edge && d.getDirection() == direction) {
                return d;
            }
        }
        return null;
    }
}
