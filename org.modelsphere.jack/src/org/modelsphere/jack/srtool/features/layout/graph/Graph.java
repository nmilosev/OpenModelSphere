package org.modelsphere.jack.srtool.features.layout.graph;

import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.DirectedMultigraph;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.features.layout.graph.Edge.OrientationConstraint;

/**
 * <p>
 * A directed graph containing <tt>Vertex</tt> (or any subclass) as vertices and <tt>Edge</tt> (or
 * any subclass) as edges.
 * <p>
 * <strong>NOTE</strong>: The graph is aware of <tt>Node</tt> and <tt>DummyVertex</tt> as sub-types
 * of <tt>Vertex</tt>. In so, it offers to retrieve only the set of one of these to sub-types. See
 * {@link Graph#nodeSet()} and {@link Graph#dummyVertexSet()}
 * 
 * @author Gabriel
 * 
 */
public class Graph extends DirectedMultigraph<Vertex, Edge> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 9095540376030245761L;

    /**
     * Instantiates a new graph.
     */
    public Graph() {
        super(new GraphEdgeFactory());
    }

    /**
     * Instantiates a new graph.
     * 
     * @param edgeFactory
     *            the edge factory
     */
    public Graph(EdgeFactory<Vertex, Edge> edgeFactory) {
        super(edgeFactory);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jgrapht.graph.AbstractBaseGraph#degreeOf(java.lang.Object)
     */
    @Override
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

    /**
     * Gets the edge direction.
     * 
     * @param edge
     *            the edge
     * @return the edge direction
     */
    public OrientationConstraint getEdgeDirection(Edge edge) {
        return edge.getOrientationConstraint();
    }

    /**
     * Gets the dimension.
     * 
     * @return the dimension
     */
    public Dimension getDimension() {
        Rectangle bounds = getBounds();
        return new Dimension(bounds.width, bounds.height);
    }

    /**
     * Gets the bounds.
     * 
     * @return the bounds
     */
    public Rectangle getBounds() {
        Rectangle graphBounds = null;
        Rectangle nodeBounds = null;

        for (Node node : nodeSet()) {
            nodeBounds = node.getBounds();

            if (graphBounds == null){
                graphBounds = new Rectangle(nodeBounds.x, nodeBounds.y, nodeBounds.width, nodeBounds.height);
            } else {
                graphBounds.add(nodeBounds);
            }
        }

        return graphBounds;
    }

    /**
     * Round up.
     * 
     * @param value
     *            the value
     * @return the long
     */
    private long roundUp(double value) {
        if (value > Math.round(value)) {
            return Math.round(value) + 1;
        } else {
            return Math.round(value);
        }
    }

    /**
     * Gets the incident count.
     * 
     * @param edge
     *            the edge
     * @return the incident count
     */
    public int getIncidentCount(Edge edge) {
        if (getEdgeSource(edge) == getEdgeTarget(edge)) {
            return 1;
        }
        return 2;
    }

    /**
     * Gets the incident vertices.
     * 
     * @param edge
     *            the edge
     * @return the incident vertices
     */
    public Collection<Vertex> getIncidentVertices(Edge edge) {
        if (!containsEdge(edge)) {
            return null;
        }
        List<Vertex> nodes = new ArrayList<Vertex>();
        nodes.add(getEdgeSource(edge));
        nodes.add(getEdgeTarget(edge));
        return nodes;
    }

    /**
     * Neighbor count.
     * 
     * @param vertex
     *            the vertex
     * @return the int
     */
    public int neighborCount(Vertex vertex) {
        return neighborsOf(vertex).size();
    }

    /**
     * Neighbors of.
     * 
     * @param vertex
     *            A vertex in this graph.
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
     * Connected nodes.
     * 
     * @param node
     *            A node in this graph.
     * @return The set of nodes connected to this one and the ones connected to them, recursively.
     *         Also referred to as the set of nodes of the sub-component of this graph that contains
     *         .
     */
    public Collection<Node> connectedNodes(Node node) {
        Set<Node> nodes = new LinkedHashSet<Node>();
        if (!containsVertex(node)) {
            return nodes;
        }
        collectConnectedNodesRec(nodes, node);

        return nodes;
    }

    /**
     * The recursive traversal of the graph to collect a set of connected <tt>Node</tt> (not
     * <tt>Vertex</tt>).
     * 
     * @param collection
     *            The collection where the nodes are collected.
     * @param node
     *            The current node.
     */
    private void collectConnectedNodesRec(Collection<Node> collection, Node node) {
        if (!collection.contains(node)) {
            collection.add(node);

            Collection<Vertex> neighbors = neighborsOf(node);
            for (Vertex neighbor : neighbors) {
                if (neighbor instanceof Node) {
                    collectConnectedNodesRec(collection, (Node) neighbor);
                }
            }
        }
    }

    /**
     * Gets the node for graphical object.
     * 
     * @param go
     *            the go
     * @return the node for graphical object
     */
    public Node getNodeForGraphicalObject(DbObject go) {
        if (go == null) {
            return null;
        }
        for (Node node : nodeSet()) {
            if (node.getGO() == go) {
                return node;
            }
        }
        return null;
    }

    /**
     * Gets the opposite.
     * 
     * @param vertex
     *            the vertex
     * @param edge
     *            the edge
     * @return the opposite
     */
    public Vertex getOpposite(Vertex vertex, Edge edge) {
        Vertex source = getEdgeSource(edge);
        Vertex target = getEdgeTarget(edge);

        if (source == vertex) {
            return target;
        }
        if (target == vertex) {
            return source;
        }
        return null;
    }

    /**
     * Gets the predecessor count.
     * 
     * @param node
     *            the node
     * @return the predecessor count
     */
    public int getPredecessorCount(Node node) {
        return incomingEdgesOf(node).size();
    }

    /**
     * Gets the predecessors.
     * 
     * @param node
     *            the node
     * @return the predecessors
     */
    public Collection<Vertex> getPredecessors(Vertex node) {
        List<Vertex> predecessors = new ArrayList<Vertex>();
        Set<Edge> inEdges = incomingEdgesOf(node);
        for (Edge edge : inEdges) {
            predecessors.add(getEdgeSource(edge));
        }
        return predecessors;
    }

    /**
     * Gets the successor count.
     * 
     * @param node
     *            the node
     * @return the successor count
     */
    public int getSuccessorCount(Node node) {
        return outgoingEdgesOf(node).size();
    }

    /**
     * Gets the successors.
     * 
     * @param vertex
     *            the vertex
     * @return the successors
     */
    public Collection<Vertex> getSuccessors(Vertex vertex) {
        List<Vertex> successors = new ArrayList<Vertex>();
        Set<Edge> outEdges = outgoingEdgesOf(vertex);
        for (Edge edge : outEdges) {
            successors.add(getEdgeTarget(edge));
        }
        return successors;
    }

    /**
     * Checks if is target.
     * 
     * @param node
     *            the node
     * @param edge
     *            the edge
     * @return true, if is target
     */
    public boolean isTarget(Node node, Edge edge) {
        return getEdgeTarget(edge) == node;
    }

    /**
     * Checks if is incident.
     * 
     * @param node
     *            the node
     * @param edge
     *            the edge
     * @return true, if is incident
     */
    public boolean isIncident(Node node, Edge edge) {
        if (edgesOf(node).contains(edge)) {
            return true;
        }
        return false;
    }

    /**
     * Are neighbors.
     * 
     * @param node1
     *            the node1
     * @param node2
     *            the node2
     * @return true, if successful
     */
    public boolean areNeighbors(Vertex node1, Vertex node2) {
        Collection<Vertex> neighbors = neighborsOf(node1);
        if (neighbors != null && neighbors.contains(node2)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if is predecessor.
     * 
     * @param node1
     *            the node1
     * @param node2
     *            the node2
     * @return true, if is predecessor
     */
    public boolean isPredecessor(Node node1, Node node2) {
        Set<Edge> inEdges = incomingEdgesOf(node2);
        for (Edge edge : inEdges) {
            if (getEdgeSource(edge) == node1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if is source.
     * 
     * @param node
     *            the node
     * @param edge
     *            the edge
     * @return true, if is source
     */
    public boolean isSource(Node node, Edge edge) {
        return getEdgeSource(edge) == node;
    }

    /**
     * Checks if is successor.
     * 
     * @param node1
     *            the node1
     * @param node2
     *            the node2
     * @return true, if is successor
     */
    public boolean isSuccessor(Node node1, Node node2) {
        Set<Edge> outEdges = outgoingEdgesOf(node2);
        for (Edge edge : outEdges) {
            if (getEdgeTarget(edge) == node1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the poly line.
     * 
     * @param edge
     *            the edge
     * @return the poly line
     */
    public Polygon getPolyLine(Edge edge) {
        if (edge == null) {
            throw new NullPointerException("edge");
        }
        Polygon polyLine = new Polygon();

        if (!containsEdge(edge)) {
            return polyLine;
        }

        Vertex origin = getEdgeSource(edge);
        Vertex destination = getEdgeTarget(edge);
        if (!(origin instanceof Node) || !(destination instanceof Node)) {
            return polyLine;
        }

        Rectangle originBounds;
        Rectangle destinationBounds;

        if (edge.isDiagramReversed()) {
            originBounds = ((Node) destination).getBounds();
            destinationBounds = ((Node) origin).getBounds();
        } else {
            originBounds = ((Node) origin).getBounds();
            destinationBounds = ((Node) destination).getBounds();
        }

        polyLine.addPoint((int) originBounds.getCenterX(), (int) originBounds.getCenterY());

        Polygon intermediateCoordinates = edge.getIntermediateCoordinates();

        if (intermediateCoordinates != null) {
            if (edge.isDiagramReversed()) {
                for (int i = intermediateCoordinates.npoints - 1; i >= 0; i--) {
                    polyLine.addPoint(intermediateCoordinates.xpoints[i],
                            intermediateCoordinates.ypoints[i]);
                }
            } else {
                for (int i = 0; i < intermediateCoordinates.npoints; i++) {
                    polyLine.addPoint(intermediateCoordinates.xpoints[i],
                            intermediateCoordinates.ypoints[i]);
                }
            }
        }
        polyLine.addPoint((int) destinationBounds.getCenterX(),
                (int) destinationBounds.getCenterY());
        return polyLine;
    }

    /**
     * Node set.
     * 
     * @return The set of vertices in this graph that are of type . The set in question is not
     *         backed by the graph.
     */
    public Set<Node> nodeSet() {
        Set<Node> nodes = new LinkedHashSet<Node>();
        for (Vertex v : vertexSet()) {
            if (v instanceof Node) {
                nodes.add((Node) v);
            }
        }
        return nodes;
    }

    /**
     * Dummy vertex set.
     * 
     * @return The set of vertices in this graph that are of type . The set in question is not
     *         backed by the graph.
     */
    public Set<DummyVertex> dummyVertexSet() {
        Set<DummyVertex> dummies = new LinkedHashSet<DummyVertex>();
        for (Vertex v : vertexSet()) {
            if (v instanceof DummyVertex) {
                dummies.add((DummyVertex) v);
            }
        }
        return dummies;
    }

    /**
     * Find dummy vertex path.
     * 
     * @param sourceVertex
     *            The vertex from which to begin the search.
     * @param targetVertex
     *            The vertex to which the desired path should lead.
     * @return The list of dummy vertices encountered from to if any. An empty list otherwise.
     */
    public List<DummyVertex> findDummyVertexPath(Vertex sourceVertex, Vertex targetVertex) {
        if (sourceVertex == null) {
            throw new NullPointerException("sourceVertex");
        }
        if (targetVertex == null) {
            throw new NullPointerException("targetVertex");
        }

        List<DummyVertex> list = new ArrayList<DummyVertex>();

        if (!containsVertex(sourceVertex) || !containsVertex(targetVertex)) {
            return list; // empty.
        }

        Stack<DummyVertex> stack = new Stack<DummyVertex>();

        if (findDummyVertexPathRecursive(stack, sourceVertex, targetVertex) != targetVertex) {
            return list; // empty.
        }

        for (int i = 0; i < stack.size(); i++) {
            list.add(i, stack.get(i));
        }
        return list; // filled.
    }

    /**
     * Find dummy vertex path recursive.
     * 
     * @param stack
     *            the stack
     * @param currentNode
     *            the current node
     * @param targetNode
     *            the target node
     * @return the vertex
     */
    private Vertex findDummyVertexPathRecursive(Stack<DummyVertex> stack, Vertex currentNode,
            Vertex targetNode) {

        // find all the neighbors.
        Collection<Vertex> neighbors = neighborsOf(currentNode);

        // if the target is among the neighbors, we did it!
        if (neighbors.contains(targetNode)) {
            return targetNode; // finished.
        }

        // find all the dummy-neighbors that haven't already been stacked.
        Set<DummyVertex> dummyNeighbors = new HashSet<DummyVertex>();
        for (Vertex v : neighbors) {
            if (v instanceof DummyVertex && !stack.contains(v)) {
                dummyNeighbors.add((DummyVertex) v);
            }
        }

        // if there are any
        if (dummyNeighbors.size() != 0) {
            // try each one recursively.
            for (DummyVertex neighbor : dummyNeighbors) {
                stack.add(neighbor);
                if (findDummyVertexPathRecursive(stack, neighbor, targetNode) == targetNode) {
                    return targetNode; // success... the stack contains the
                    // path.
                } else {
                    stack.remove(neighbor);
                }
            }
        }
        return null; // forget this branch!
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jgrapht.graph.AbstractBaseGraph#clone()
     */
    @Override
    public Graph clone() {
        Graph graph = new Graph();
        for (Vertex v : vertexSet()) {
            graph.addVertex(v);
        }
        for (Edge e : edgeSet()) {
            graph.addEdge(getEdgeSource(e), getEdgeTarget(e), e);
        }
        return graph;
    }
}
